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
package org.eclipse.oomph.internal.ui;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.action.CopyAction;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import java.util.HashMap;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class GeneralDragAdapter extends OomphDragAdapter
{
  private CopyAction copyAction;

  private MenuManager contextMenu;

  public GeneralDragAdapter(Viewer viewer, DraggedObjectsFactory factory, List<? extends OomphTransferDelegate> delegates)
  {
    super(createEditingDomain(delegates), createSelectionProvider(viewer, factory), delegates);
    createContextMenu(viewer.getControl());
  }

  public EditingDomain getEditingDomain()
  {
    return domain;
  }

  public CopyAction getCopyAction()
  {
    return copyAction;
  }

  public MenuManager getContextMenu()
  {
    return contextMenu;
  }

  private void createContextMenu(Control control)
  {
    copyAction = new CopyAction(domain);
    copyAction.setId(ActionFactory.COPY.getId());

    try
    {
      ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
      copyAction.setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
    }
    catch (RuntimeException ex)
    {
      // Ignore it if we can't set an image.
    }

    contextMenu = new MenuManager("#PopUp");

    contextMenu.add(new Separator("additions"));
    contextMenu.setRemoveAllWhenShown(true);
    contextMenu.addMenuListener(new IMenuListener()
    {
      public void menuAboutToShow(IMenuManager manager)
      {
        IStructuredSelection selection = (IStructuredSelection)selectionProvider.getSelection();
        copyAction.setEnabled(copyAction.updateSelection(selection));
        if (copyAction.isEnabled())
        {
          manager.add(copyAction);
        }
      }
    });

    Menu menu = contextMenu.createContextMenu(control);
    control.setMenu(menu);
  }

  private static EditingDomain createEditingDomain(List<? extends OomphTransferDelegate> delegates)
  {
    AdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
    EditingDomain editingDomain = new OomphEditingDomain(adapterFactory, new BasicCommandStack(), new HashMap<Resource, Boolean>(), delegates);
    return editingDomain;
  }

  private static ISelectionProvider createSelectionProvider(final Viewer viewer, final DraggedObjectsFactory factory)
  {
    return new ISelectionProvider()
    {
      public ISelection getSelection()
      {
        ISelection selection = viewer.getSelection();

        try
        {
          List<Object> objects = factory.createDraggedObjects(selection);
          if (objects != null && !objects.isEmpty())
          {
            return new StructuredSelection(objects);
          }
        }
        catch (Exception ex)
        {
          UIPlugin.INSTANCE.log(ex);
        }

        return StructuredSelection.EMPTY;
      }

      public void setSelection(ISelection selection)
      {
        // Do nothing.
      }

      public void removeSelectionChangedListener(ISelectionChangedListener listener)
      {
        // Do nothing.
      }

      public void addSelectionChangedListener(ISelectionChangedListener listener)
      {
        // Do nothing.
      }
    };
  }

  /**
   * @author Eike Stepper
   */
  public interface DraggedObjectsFactory
  {
    public List<Object> createDraggedObjects(ISelection selection) throws Exception;
  }
}
