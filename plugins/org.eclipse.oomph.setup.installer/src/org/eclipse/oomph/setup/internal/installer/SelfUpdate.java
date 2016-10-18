/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.p2.internal.core.CachingRepositoryManager;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.SelfCommitContext;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UICallback;
import org.eclipse.oomph.util.ExceptionHandler;
import org.eclipse.oomph.util.IRunnable;
import org.eclipse.oomph.util.OomphPlugin;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
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

    for (IInstallableUnit iu : P2Util.asIterable(profile.query(QueryUtil.createIUQuery(PropertiesUtil.getProductID()), null)))
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

  public static Resolution resolve(User user, IProgressMonitor monitor) throws CoreException
  {
    Agent agent = P2Util.getAgentManager().getCurrentAgent();
    Profile profile = agent.getCurrentProfile();
    ProfileTransaction transaction = profile.change();

    SelfCommitContext commitContext = new SelfCommitContext(user);
    transaction = commitContext.migrateProfile(transaction);

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
        boolean originalBetterMirrorSelection = CachingRepositoryManager.enableBetterMirrorSelection();

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
        catch (OperationCanceledException ex)
        {
          // Ignore.
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
          CachingRepositoryManager.setBetterMirrorSelection(originalBetterMirrorSelection);

          if (finalRunnable != null)
          {
            finalRunnable.run();
          }
        }
      }
    });
  }
}
