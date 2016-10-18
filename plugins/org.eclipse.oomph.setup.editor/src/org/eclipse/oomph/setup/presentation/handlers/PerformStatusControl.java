/*
 * Copyright (c) 2014-2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.wizards.ProgressPage;
import org.eclipse.oomph.ui.ButtonAnimator;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import java.lang.reflect.Method;

/**
 * @author Ed Merks
 */
public class PerformStatusControl extends WorkbenchWindowControlContribution
{
  public PerformStatusControl()
  {
  }

  protected void resetPerforming()
  {
    SetupPropertyTester.setPerformingShell(null);
    SetupPropertyTester.setPerformingStatus(null);
  }

  @Override
  protected Control createControl(final Composite parent)
  {
    final ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.HORIZONTAL);

    final ToolItem toolItem = new ToolItem(toolBar, SWT.CHECK);
    toolItem.setImage(SetupEditorPlugin.INSTANCE.getSWTImage("progress0"));
    toolItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Shell shell = SetupPropertyTester.getPerformingShell();
        if (shell != null)
        {
          boolean visible = !shell.isVisible();
          shell.setVisible(visible);

          if (SetupPropertyTester.getPerformingStatus() != null)
          {
            resetPerforming();
          }

          if (visible)
          {
            shell.setFocus();
          }
        }
      }
    });

    if (OS.INSTANCE.isLinux())
    {
      toolBar.addControlListener(new ControlAdapter()
      {
        @Override
        public void controlResized(ControlEvent e)
        {
          parent.pack();
          toolBar.removeControlListener(this);
        }
      });
    }

    // Create an animator to indicate the a setup is being performed, and to provide feedback once it's done.
    final ButtonAnimator buttonAnimator = new ButtonAnimator(SetupEditorPlugin.INSTANCE, toolItem, "progress", 7, true)
    {
      private boolean done;

      private boolean fixed;

      @Override
      public void run()
      {
        if (!toolItem.isDisposed())
        {
          // Fix the position once or when the tool bar isn't visible.
          if (!fixed || !toolBar.isVisible())
          {
            fixed = fixPosition();
          }

          super.run();
        }
      }

      @Override
      public Shell getShell()
      {
        Shell shell = SetupPropertyTester.getPerformingShell();
        if (shell != null && !shell.isDisposed())
        {
          boolean visible = shell.isVisible();
          if (toolItem.getSelection() != visible)
          {
            toolItem.setSelection(visible);
          }

          String text = shell.getText();
          if (!text.equals(toolItem.getToolTipText()))
          {
            toolItem.setToolTipText(text);
          }

          IStatus status = SetupPropertyTester.getPerformingStatus();
          if (status == null)
          {
            // If there is a status, reflect the feedback into the images.
            status = (IStatus)shell.getData(ProgressPage.PROGRESS_STATUS);

            if (status != null)
            {
              SetupPropertyTester.setPerformingStatus(status);

              String statusImage;
              switch (status.getSeverity())
              {
                case IStatus.OK:
                {
                  statusImage = "progress_success";
                  delayedDispose(shell);
                  break;
                }
                case IStatus.WARNING:
                {
                  statusImage = "progress_warning";
                  break;
                }
                case IStatus.ERROR:
                {
                  statusImage = "progress_error";
                  break;
                }
                case IStatus.CANCEL:
                {
                  statusImage = "progress_cancel";
                  delayedDispose(shell);
                  break;
                }
                default:
                {
                  statusImage = "progress";
                  break;
                }
              }

              if (images == null)
              {
                images = new Image[1 + additionalImages];
              }

              for (int i = 0; i < images.length; ++i)
              {
                images[i] = SetupEditorPlugin.INSTANCE.getSWTImage(i >= 4 ? statusImage : "progress");
              }

              done = true;
            }
          }
        }

        return shell;
      }

      @Override
      protected int getQuietCycles()
      {
        if (done)
        {
          return 2;
        }

        return super.getQuietCycles();
      }

      private void delayedDispose(final Shell shell)
      {
        UIUtil.timerExec(15000, new Runnable()
        {
          public void run()
          {
            if (!toolItem.isDisposed() && !shell.isDisposed() && !shell.isVisible())
            {
              shell.dispose();
            }
          }
        });
      }

      @Override
      protected boolean doAnimate()
      {
        // Keep animating once there is a status.
        if (SetupPropertyTester.getPerformingStatus() != null)
        {
          Shell shell = getShell();
          if (shell.isVisible())
          {
            resetPerforming();
          }

          return true;
        }

        // When the job is done, reset the performing state to re-enable the menus.
        if (Job.getJobManager().find(ProgressPage.PROGRESS_FAMILY).length == 0)
        {
          resetPerforming();
        }

        return true;
      }
    };

    buttonAnimator.run();

    return toolBar;
  }

  private boolean fixPosition()
  {
    IWorkbenchWindow workbenchWindow = getWorkbenchWindow();

    try
    {
      // Use the model service to move the perform status control before the progress bar.
      Object model = ReflectUtil.getValue("model", workbenchWindow);
      Object modelService = ReflectUtil.getValue("modelService", workbenchWindow);
      Class<?> elementClass = CommonPlugin.loadClass("org.eclipse.ui.workbench", "org.eclipse.e4.ui.model.application.ui.MUIElement");
      Method findMethod = ReflectUtil.getMethod(modelService.getClass(), "find", String.class, elementClass);

      EObject progressBar = (EObject)ReflectUtil.invokeMethod(findMethod, modelService, "org.eclipse.ui.ProgressBar", model);
      EObject performStatusBar = (EObject)ReflectUtil.invokeMethod(findMethod, modelService, "org.eclipse.oomph.setup.status", model);

      if (progressBar == null)
      {
        // Try again later.
        return false;
      }

      // Just moving it in the model doesn't update the IDE, so make sure it's definitely added.
      @SuppressWarnings("unchecked")
      EList<EObject> children = (EList<EObject>)progressBar.eContainer().eGet(progressBar.eContainmentFeature());
      children.remove(performStatusBar);
      children.add(children.indexOf(progressBar), performStatusBar);

      // The layout is needed to ensure the bar is made visible.
      workbenchWindow.getShell().layout(true, true);
    }
    catch (Throwable t)
    {
      // Ignore.
    }

    return true;
  }
}
