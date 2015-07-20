/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.core.ProfileTransaction.CommitContext;
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.p2.impl.P2TaskImpl;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.UnsignedContentDialog;
import org.eclipse.oomph.setup.ui.wizards.ProgressPage;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UICallback;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.ExceptionHandler;
import org.eclipse.oomph.util.IRunnable;
import org.eclipse.oomph.util.OomphPlugin;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.swt.widgets.Shell;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Eike Stepper
 */
public class SelfUpdate
{
  private static final String PROP_INSTALLER_UPDATE_URL = "oomph.installer.update.url";

  private static final String DEFAULT_INSTALLER_UPDATE_URL = "http://download.eclipse.org/oomph/products/repository";

  public static final String INSTALLER_UPDATE_URL = PropertiesUtil.getProperty(PROP_INSTALLER_UPDATE_URL, DEFAULT_INSTALLER_UPDATE_URL).replace('\\', '/');

  public static final String SELF_HOSTING = "Self Hosting";

  public static String getProductVersion()
  {
    Agent agent = P2Util.getAgentManager().getCurrentAgent();

    Profile profile = agent.getCurrentProfile();
    if (profile == null || profile.isSelfHosting())
    {
      return SELF_HOSTING;
    }

    String firstBuildID = null;
    int highestBuildID = 0;

    BundleContext bundleContext = SetupInstallerPlugin.INSTANCE.getBundleContext();
    for (Bundle bundle : bundleContext.getBundles())
    {
      String symbolicName = bundle.getSymbolicName();
      if (symbolicName.startsWith(SetupCoreUtil.OOMPH_NAMESPACE))
      {
        String buildID = OomphPlugin.getBuildID(bundle);
        if (buildID != null)
        {
          if (firstBuildID == null)
          {
            firstBuildID = buildID;
          }

          try
          {
            int id = Integer.parseInt(buildID);
            if (id > highestBuildID)
            {
              highestBuildID = id;
            }
          }
          catch (NumberFormatException ex)
          {
            //$FALL-THROUGH$
          }
        }
      }
    }

    String buildID = highestBuildID != 0 ? Integer.toString(highestBuildID) : firstBuildID;

    for (IInstallableUnit iu : P2Util.asIterable(profile.query(QueryUtil.createIUQuery(SetupUIPlugin.PRODUCT_ID), null)))
    {
      String label;

      Version version = iu.getVersion();
      if (buildID != null && version.getSegmentCount() > 3)
      {
        label = version.getSegment(0) + "." + version.getSegment(1) + "." + version.getSegment(2);
      }
      else
      {
        label = version.toString();
      }

      if (buildID != null)
      {
        label += " Build " + buildID;
      }

      return label;
    }

    return null;
  }

  public static Resolution resolve(final User user, IProgressMonitor monitor) throws CoreException
  {
    Agent agent = P2Util.getAgentManager().getCurrentAgent();
    Profile profile = agent.getCurrentProfile();
    ProfileTransaction transaction = profile.change();

    // TODO Remove this temporary range conversion when all users can be expected to have a major range.
    VersionRange deprecatedVersionRange = new VersionRange("[1.0.0,1.1.0)");
    ProfileDefinition profileDefinition = transaction.getProfileDefinition();
    for (Requirement requirement : profileDefinition.getRequirements())
    {
      if (IInstallableUnit.NAMESPACE_IU_ID.equals(requirement.getNamespace()) && SetupUIPlugin.PRODUCT_ID.equals(requirement.getName())
          && deprecatedVersionRange.equals(requirement.getVersionRange()))
      {
        requirement.setVersionRange(new VersionRange("[1.0.0,2.0.0)"));
        transaction.commit(monitor);
        transaction = profile.change();
        break;
      }
    }

    final boolean repositoryChanged = changeRepositoryIfNeeded(transaction);

    CommitContext commitContext = new CommitContext()
    {
      private IProvisioningPlan provisioningPlan;

      @Override
      public boolean handleProvisioningPlan(ResolutionInfo info) throws CoreException
      {
        if (repositoryChanged && info.getIUDeltas().isEmpty() && info.getPropertyDeltas().size() <= 1)
        {
          // Cancel if only the repository addition would be committed.
          return false;
        }

        provisioningPlan = info.getProvisioningPlan();
        return true;
      }

      @Override
      public Confirmer getUnsignedContentConfirmer()
      {
        if (user == null)
        {
          return Confirmer.ACCEPT;
        }

        P2TaskImpl.processLicenses(provisioningPlan, ProgressPage.LICENSE_CONFIRMER, user, true, new NullProgressMonitor());

        return UnsignedContentDialog.createUnsignedContentConfirmer(user, true);
      }
    };

    return transaction.resolve(commitContext, monitor);
  }

  public static void update(Shell shell, final Resolution resolution, final Runnable successRunnable, final ExceptionHandler<CoreException> exceptionHandler,
      final Runnable finalRunnable)
  {
    String shellText = shell.getText();
    if (!StringUtil.isEmpty(shellText))
    {
      shellText += " ";
    }

    final UICallback callback = new UICallback(shell, shellText + "Update");
    callback.runInProgressDialog(false, new IRunnable()
    {
      public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
      {
        try
        {
          resolution.commit(monitor);

          callback.execInUI(true, new Runnable()
          {
            public void run()
            {
              callback.information(false, "Updates were installed. Press OK to restart.");

              if (successRunnable != null)
              {
                successRunnable.run();
              }
            }
          });
        }
        catch (final CoreException ex)
        {
          if (exceptionHandler != null)
          {
            exceptionHandler.handleException(ex);
          }
          else
          {
            ErrorDialog.open(ex);
          }
        }
        finally
        {
          if (finalRunnable != null)
          {
            finalRunnable.run();
          }
        }
      }
    });
  }

  private static boolean changeRepositoryIfNeeded(ProfileTransaction transaction)
  {
    ProfileDefinition profileDefinition = transaction.getProfileDefinition();

    EList<Repository> repositories = profileDefinition.getRepositories();
    URIConverter uriConverter = SetupCoreUtil.createResourceSet().getURIConverter();
    String installerUpdateURL = uriConverter.normalize(URI.createURI(INSTALLER_UPDATE_URL)).toString();
    if (repositories.size() != 1 || !installerUpdateURL.equals(repositories.get(0).getURL()))
    {
      Repository repository = P2Factory.eINSTANCE.createRepository(installerUpdateURL);
      profileDefinition.setRepositories(ECollections.singletonEList(repository));
      return true;
    }

    return false;
  }
}
