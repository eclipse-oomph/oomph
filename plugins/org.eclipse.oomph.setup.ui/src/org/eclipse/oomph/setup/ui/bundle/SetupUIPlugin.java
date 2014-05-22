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
package org.eclipse.oomph.setup.ui.bundle;

import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.internal.setup.core.util.ResourceMirror;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.ui.AbstractOomphUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import org.osgi.framework.BundleContext;

import java.util.Arrays;

/**
 * @author Eike Stepper
 */
public final class SetupUIPlugin extends AbstractOomphUIPlugin
{
  public static final SetupUIPlugin INSTANCE = new SetupUIPlugin();

  public static final String PREF_SKIP_STARTUP_TASKS = "skip.startup.tasks";

  public static final boolean SETUP_IDE = PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_IDE);

  private static final boolean SETUP_SKIP = PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_SKIP);

  private static SetupUIPlugin.Implementation plugin;

  public SetupUIPlugin()
  {
    super(new ResourceLocator[] {});
  }

  public void refreshCache()
  {
    // compute the setup context again.
    // reset the ECF cache map.
  }

  @Override
  public ResourceLocator getPluginResourceLocator()
  {
    return plugin;
  }

  public static boolean isSkipStartupTasks()
  {
    return plugin.getPreferenceStore().getBoolean(PREF_SKIP_STARTUP_TASKS);
  }

  private static void performStartup()
  {
    final Display display = Display.getDefault();
    display.asyncExec(new Runnable()
    {
      public void run()
      {
        String productID = PropertiesUtil.getProperty(SetupProperties.PROP_ECLIPSE_PRODUCT);
        if (!"org.eclipse.oomph.setup.installer.product".equals(productID))
        {
          final IWorkbench workbench = PlatformUI.getWorkbench();
          IExtensionTracker extensionTracker = workbench.getExtensionTracker();
          if (extensionTracker == null || workbench.getWorkbenchWindowCount() == 0)
          {
            display.timerExec(1000, this);
          }
          else
          {
            if (SetupTaskPerformer.REMOTE_DEBUG)
            {
              MessageDialog.openInformation(UIUtil.getShell(), "Remote Debug Pause", "The setup tasks are paused to allow you to attach a remote debugger");
            }

            if (SETUP_IDE && !SETUP_SKIP && !isSkipStartupTasks())
            {
              new Job("Setup check...")
              {
                @Override
                protected IStatus run(IProgressMonitor monitor)
                {
                  performStartup(workbench);
                  return Status.OK_STATUS;
                }
              }.schedule();
            }
            else
            {
              new Job("Refresh Setup Cache")
              {
                @Override
                protected IStatus run(IProgressMonitor monitor)
                {
                  ResourceMirror resourceMirror = new ResourceMirror();
                  resourceMirror.mirror(Arrays.asList(new URI[] { SetupContext.INSTALLATION_SETUP_URI, SetupContext.WORKSPACE_SETUP_URI,
                      SetupContext.USER_SETUP_URI }));

                  SetupContext.setSelf(SetupContext.createSelf(resourceMirror.getResourceSet()));

                  return Status.OK_STATUS;
                }
              }.schedule();
            }
          }
        }
      }
    });
  }

  private static void performStartup(IWorkbench workbench)
  {
    // This performer is only used to detect a need to update or to open the setup wizard.
    SetupTaskPerformer performer = null;

    final ResourceSet resourceSet = EMFUtil.createResourceSet();
    try
    {
      performer = SetupTaskPerformer.createForIDE(resourceSet, SetupPrompter.CANCEL, Trigger.STARTUP);
    }
    catch (OperationCanceledException ex)
    {
      //$FALL-THROUGH$
    }
    catch (Throwable ex)
    {
      INSTANCE.log(ex);
      return;
    }
    finally
    {
      SetupContext.setSelf(SetupContext.createSelf(resourceSet));
    }

    if (performer != null)
    {
      try
      {
        // At this point we know that no prompt was needed.
        EList<SetupTask> neededTasks = performer.initNeededSetupTasks();
        if (neededTasks.isEmpty())
        {
          // No tasks are needed, either. Nothing to do.
          return;
        }
      }
      catch (Throwable ex)
      {
        INSTANCE.log(ex);
        return;
      }
    }

    final SetupTaskPerformer finalPerfomer = performer;
    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        if (finalPerfomer != null)
        {
          resourceSet.getResources().add(finalPerfomer.getUser().eResource());
        }

        SetupWizard updater = new SetupWizard.Updater(finalPerfomer);
        updater.openDialog(UIUtil.getShell());
      }
    });
  }

  /**
   * @author Eike Stepper
   */
  public static class Implementation extends EclipseUIPlugin
  {
    public Implementation()
    {
      plugin = this;
    }

    @Override
    public void start(BundleContext context) throws Exception
    {
      super.start(context);
      performStartup();
    }
  }
}
