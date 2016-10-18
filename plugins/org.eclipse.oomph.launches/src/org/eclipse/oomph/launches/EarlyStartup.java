/*
 * Copyright (c) 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.launches;

import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IDecorationContext;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import java.lang.reflect.Constructor;

/**
 * @author Eike Stepper
 */
public class EarlyStartup implements IStartup
{
  private static final boolean DECORATE = PropertiesUtil.isProperty("org.eclipse.oomph.launches.TestElementLabelDecorator");

  private static final String JUNIT_PART_NAME = "org.eclipse.jdt.junit.ResultView";

  private final IWindowListener windowListener = new IWindowListener()
  {
    public void windowOpened(IWorkbenchWindow window)
    {
      window.addPageListener(pageListener);
    }

    public void windowClosed(IWorkbenchWindow window)
    {
      window.removePageListener(pageListener);
    }

    public void windowActivated(IWorkbenchWindow window)
    {
      // Ignore.
    }

    public void windowDeactivated(IWorkbenchWindow window)
    {
      // Ignore.
    }
  };

  private final IPageListener pageListener = new IPageListener()
  {
    public void pageOpened(IWorkbenchPage page)
    {
      page.addPartListener(partListener);
    }

    public void pageClosed(IWorkbenchPage page)
    {
      page.removePartListener(partListener);
    }

    public void pageActivated(IWorkbenchPage page)
    {
      // Ignore.
    }
  };

  private final IPartListener partListener = new IPartListener()
  {
    public void partOpened(IWorkbenchPart part)
    {
      try
      {
        if (JUNIT_PART_NAME.equals(part.getSite().getId()))
        {
          handleJUnitView(part);
        }
      }
      catch (Throwable ex)
      {
        ex.printStackTrace();
      }
    }

    public void partClosed(IWorkbenchPart part)
    {
      // Ignore.
    }

    public void partActivated(IWorkbenchPart part)
    {
      // Ignore.
    }

    public void partDeactivated(IWorkbenchPart part)
    {
      // Ignore.
    }

    public void partBroughtToTop(IWorkbenchPart part)
    {
      // Ignore.
    }
  };

  public void earlyStartup()
  {
    if (DECORATE)
    {
      try
      {
        IWorkbench workbench = PlatformUI.getWorkbench();

        for (IWorkbenchWindow window : workbench.getWorkbenchWindows())
        {
          for (IWorkbenchPage page : window.getPages())
          {
            for (IViewReference viewReference : page.getViewReferences())
            {
              if (JUNIT_PART_NAME.equals(viewReference.getId()))
              {
                final IWorkbenchPart part = viewReference.getView(true);
                if (part != null)
                {
                  workbench.getDisplay().syncExec(new Runnable()
                  {
                    public void run()
                    {
                      handleJUnitView(part);
                    }
                  });
                }
              }
            }

            page.addPartListener(partListener);
          }

          window.addPageListener(pageListener);
        }

        workbench.addWindowListener(windowListener);
      }
      catch (Throwable ex)
      {
        ex.printStackTrace();
      }
    }
  }

  private void handleJUnitView(IWorkbenchPart part)
  {
    try
    {
      Object fTestViewer = ReflectUtil.getValue("fTestViewer", part);
      TreeViewer fTreeViewer = ReflectUtil.getValue("fTreeViewer", fTestViewer);
      IStyledLabelProvider fTreeLabelProvider = ReflectUtil.getValue("fTreeLabelProvider", fTestViewer);
      IBaseLabelProvider labelProvider = fTreeViewer.getLabelProvider();

      Constructor<? extends IBaseLabelProvider> constructor = ReflectUtil.getConstructor(labelProvider.getClass(), IStyledLabelProvider.class,
          ILabelDecorator.class, IDecorationContext.class);
      labelProvider = ReflectUtil.newInstance(constructor, fTreeLabelProvider, new TestElementLabelDecorator(), null);
      fTreeViewer.setLabelProvider(labelProvider);
    }
    catch (Throwable ex)
    {
      ex.printStackTrace();
    }
  }
}
