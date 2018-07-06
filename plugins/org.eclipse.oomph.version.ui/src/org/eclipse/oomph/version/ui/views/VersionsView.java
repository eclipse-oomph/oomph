/*
 * Copyright (c) 2014, 2016, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.version.ui.views;

import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.version.VersionUtil;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.osgi.service.resolver.ImportPackageSpecification;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class VersionsView extends ViewPart
{
  public static final String ID = "org.eclipse.oomph.version.VersionsView";

  private TreeViewer viewer;

  private Action action1;

  private Action action2;

  private Action doubleClickAction;

  public VersionsView()
  {
  }

  @Override
  public void setFocus()
  {
    viewer.getControl().setFocus();
  }

  @Override
  public void createPartControl(Composite parent)
  {
    viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
    viewer.setContentProvider(new ViewContentProvider());
    viewer.setLabelProvider(new ViewLabelProvider());
    viewer.setComparator(new NameSorter());
    viewer.setInput(getViewSite());

    getViewSite().setSelectionProvider(viewer);

    makeActions();
    hookContextMenu();
    hookDoubleClickAction();
    contributeToActionBars();
  }

  private void hookContextMenu()
  {
    MenuManager menuMgr = new MenuManager("#PopupMenu");
    menuMgr.setRemoveAllWhenShown(true);
    menuMgr.addMenuListener(new IMenuListener()
    {
      public void menuAboutToShow(IMenuManager manager)
      {
        VersionsView.this.fillContextMenu(manager);
      }
    });

    Menu menu = menuMgr.createContextMenu(viewer.getControl());
    viewer.getControl().setMenu(menu);
    getSite().registerContextMenu(menuMgr, viewer);
  }

  private void contributeToActionBars()
  {
    IActionBars bars = getViewSite().getActionBars();
    fillLocalPullDown(bars.getMenuManager());
    fillLocalToolBar(bars.getToolBarManager());
  }

  private void fillLocalPullDown(IMenuManager manager)
  {
    manager.add(action1);
    manager.add(new Separator());
    manager.add(action2);
  }

  private void fillContextMenu(IMenuManager manager)
  {
    manager.add(action1);
    manager.add(action2);
    manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
  }

  private void fillLocalToolBar(IToolBarManager manager)
  {
    manager.add(action1);
    manager.add(action2);
  }

  private void makeActions()
  {
    action1 = new Action()
    {
      @Override
      public void run()
      {
        showMessage("Action 1 executed");
      }
    };
    action1.setText("Action 1");
    action1.setToolTipText("Action 1 tooltip");
    action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

    action2 = new Action()
    {
      @Override
      public void run()
      {
        showMessage("Action 2 executed");
      }
    };
    action2.setText("Action 2");
    action2.setToolTipText("Action 2 tooltip");
    action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
    doubleClickAction = new Action()
    {
      @Override
      public void run()
      {
        ISelection selection = viewer.getSelection();
        Object obj = ((IStructuredSelection)selection).getFirstElement();
        showMessage("Double-click detected on " + obj.toString());
      }
    };
  }

  private void hookDoubleClickAction()
  {
    viewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        doubleClickAction.run();
      }
    });
  }

  private void showMessage(String message)
  {
    MessageDialog.openInformation(viewer.getControl().getShell(), "Versions", message);
  }

  /**
   * @author Eike Stepper
   */
  class ViewContentProvider implements ITreeContentProvider
  {
    private final Map<String, Set<BundleDescription>> bundles = new HashMap<String, Set<BundleDescription>>();

    private final Map<String, Set<ExportPackageDescription>> packages = new HashMap<String, Set<ExportPackageDescription>>();

    public ViewContentProvider()
    {
      for (IPluginModelBase pluginModel : PluginRegistry.getActiveModels(true))
      {
        BundleDescription bundleDescription = pluginModel.getBundleDescription();
        if (bundleDescription != null)
        {
          CollectionUtil.add(bundles, bundleDescription.getSymbolicName(), bundleDescription);
        }

        for (ExportPackageDescription exportPackageDescription : bundleDescription.getExportPackages())
        {
          CollectionUtil.add(packages, exportPackageDescription.getName(), exportPackageDescription);
        }
      }
    }

    public void inputChanged(Viewer v, Object oldInput, Object newInput)
    {
    }

    public void dispose()
    {
    }

    public Object[] getChildren(Object parent)
    {
      List<Object> result = new ArrayList<Object>();
      if (parent instanceof IPluginModelBase)
      {
        IPluginModelBase pluginModel = (IPluginModelBase)parent;
        BundleDescription bundleDescription = pluginModel.getBundleDescription();
        if (bundleDescription != null)
        {
          BundleSpecification[] requiredBundles = bundleDescription.getRequiredBundles();
          ImportPackageSpecification[] importPackages = bundleDescription.getImportPackages();
          result.addAll(Arrays.asList(requiredBundles));
          result.addAll(Arrays.asList(importPackages));
        }
      }
      else if (parent instanceof BundleSpecification)
      {
        BundleSpecification bundleSpecification = (BundleSpecification)parent;
        Set<BundleDescription> resolvedBundles = bundles.get(bundleSpecification.getName());
        if (resolvedBundles != null)
        {
          result.addAll(resolvedBundles);
        }
      }
      else if (parent instanceof ImportPackageSpecification)
      {
        ImportPackageSpecification importPackageSpecification = (ImportPackageSpecification)parent;
        Set<ExportPackageDescription> resolvedPackages = packages.get(importPackageSpecification.getName());
        if (resolvedPackages != null)
        {
          result.addAll(resolvedPackages);
        }
      }

      return result.toArray();
    }

    public Object[] getElements(Object parent)
    {

      List<IModel> allComponentModels = new ArrayList<IModel>();
      for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects())
      {
        if (project.isAccessible())
        {
          try
          {
            List<IModel> componentModels = VersionUtil.getComponentModels(project);
            allComponentModels.addAll(componentModels);
          }
          catch (RuntimeException ex)
          {
          }
        }
      }

      return allComponentModels.toArray();
    }

    public boolean hasChildren(Object element)
    {
      return getChildren(element).length > 0;
    }

    public Object getParent(Object element)
    {
      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  class ViewLabelProvider extends LabelProvider implements ITableLabelProvider
  {
    public String getColumnText(Object obj, int index)
    {
      return getText(obj);
    }

    public Image getColumnImage(Object obj, int index)
    {
      return getImage(obj);
    }

    @Override
    public Image getImage(Object obj)
    {
      return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
    }
  }

  /**
   * @author Eike Stepper
   */
  class NameSorter extends ViewerComparator
  {
  }
}
