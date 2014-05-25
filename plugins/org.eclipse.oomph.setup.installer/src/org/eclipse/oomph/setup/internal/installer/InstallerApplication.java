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

import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.setup.installer.editor.SetupEditorAdvisor;
import org.eclipse.oomph.setup.ui.actions.PreferenceRecorderAction;
import org.eclipse.oomph.ui.ErrorDialog;

import org.eclipse.emf.common.ui.URIEditorInput;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class InstallerApplication implements IApplication
{
  public Object start(final IApplicationContext context) throws Exception
  {
    InstallerStartType startType = InstallerStartType.APPLICATION;
    File restarting = new File(SetupInstallerPlugin.INSTANCE.getStateLocation().toString(), "restarting");

    try
    {
      if (restarting.exists())
      {
        startType = InstallerStartType.RESTART;
        if (!restarting.delete())
        {
          restarting.deleteOnExit();
        }
      }
    }
    catch (Exception ex)
    {
      // Ignore
    }

    try
    {
      final Display display = Display.getDefault();
      display.asyncExec(new Runnable()
      {
        public void run()
        {
          // End the splash screen once the dialog is up.
          context.applicationRunning();
        }
      });

      for (;;)
      {
        InstallerDialog dialog = new InstallerDialog(null, startType);
        final int retcode = dialog.open();
        if (retcode == InstallerDialog.RETURN_RESTART)
        {
          try
          {
            restarting.createNewFile();
          }
          catch (Exception ex)
          {
            // Ignore
          }

          return EXIT_RESTART;
        }

        if (retcode != InstallerDialog.RETURN_WORKBENCH && retcode != InstallerDialog.RETURN_WORKBENCH_NETWORK_PREFERENCES)
        {
          return EXIT_OK;
        }

        display.asyncExec(new Runnable()
        {
          public void run()
          {
            IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (workbenchWindow == null)
            {
              display.asyncExec(this);
            }
            else
            {
              IEditorInput editorInput = new URIEditorInput(SetupContext.USER_SETUP_URI, SetupContext.USER_SETUP_URI.lastSegment());

              try
              {
                IWorkbenchPage page = workbenchWindow.getActivePage();
                IEditorPart editorPart = page.openEditor(editorInput, "org.eclipse.oomph.setup.installer.editor.SetupEditorID");

                if (retcode == InstallerDialog.RETURN_WORKBENCH_NETWORK_PREFERENCES && editorPart instanceof ISelectionProvider)
                {
                  ISelectionProvider selectionProvider = (ISelectionProvider)editorPart;
                  PreferenceRecorderAction preferenceRecorderAction = new PreferenceRecorderAction(true);
                  preferenceRecorderAction.setChecked(false);
                  preferenceRecorderAction.selectionChanged(new SelectionChangedEvent(selectionProvider, selectionProvider.getSelection()));
                  preferenceRecorderAction.setChecked(true);
                  preferenceRecorderAction.run();

                  if (editorPart.isDirty())
                  {
                    editorPart.doSave(new NullProgressMonitor());
                  }

                  page.getWorkbenchWindow().close();
                }
              }
              catch (PartInitException ex)
              {
                SetupInstallerPlugin.INSTANCE.log(ex);
              }
            }
          }
        });

        PlatformUI.createAndRunWorkbench(display, new SetupEditorAdvisor());
      }
    }
    catch (Throwable ex)
    {
      SetupInstallerPlugin.INSTANCE.log(ex);
      ErrorDialog.open(ex);
      return 1;
    }
  }

  public void stop()
  {
  }
}
