/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.core.ProfileTransaction.CommitContext;
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.p2.impl.P2TaskImpl;
import org.eclipse.oomph.setup.ui.UnsignedContentDialog;
import org.eclipse.oomph.setup.ui.wizards.ProgressPage;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UICallback;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.ExceptionHandler;
import org.eclipse.oomph.util.IRunnable;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.swt.widgets.Shell;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class SelfUpdate
{
  private static final String PROP_INSTALLER_UPDATE_URL = "oomph.installer.update.url";

  private static final String DEFAULT_INSTALLER_UPDATE_URL = "http://download.eclipse.org/oomph/products/repository";

  public static final String INSTALLER_UPDATE_URL = PropertiesUtil.getProperty(PROP_INSTALLER_UPDATE_URL, DEFAULT_INSTALLER_UPDATE_URL).replace('\\', '/');

  public static Resolution resolve(final User user, IProgressMonitor monitor) throws CoreException
  {
    Agent agent = P2Util.getAgentManager().getCurrentAgent();
    Profile profile = agent.getCurrentProfile();
    ProfileTransaction transaction = profile.change();

    final boolean repositoryChanged = changeRepositoryIfNeeded(transaction);

    CommitContext commitContext = new CommitContext()
    {
      private IProvisioningPlan provisioningPlan;

      @Override
      public boolean handleProvisioningPlan(IProvisioningPlan provisioningPlan, Map<IInstallableUnit, DeltaType> iuDeltas,
          Map<IInstallableUnit, Map<String, Pair<Object, Object>>> propertyDeltas, List<IMetadataRepository> metadataRepositories) throws CoreException
      {
        if (repositoryChanged && iuDeltas.isEmpty() && propertyDeltas.size() <= 1)
        {
          // Cancel if only the repository addition would be committed.
          return false;
        }

        this.provisioningPlan = provisioningPlan;
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
        provisioningPlan = null;

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
    if (repositories.size() != 1 || !INSTALLER_UPDATE_URL.equals(repositories.get(0).getURL()))
    {
      Repository repository = P2Factory.eINSTANCE.createRepository(INSTALLER_UPDATE_URL);
      profileDefinition.setRepositories(ECollections.singletonEList(repository));
      return true;
    }

    return false;
  }
}
