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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;

import java.util.HashMap;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class GeneralDragAdapter extends OomphDragAdapter
{
  public GeneralDragAdapter(Viewer viewer, DraggedObjectsFactory factory)
  {
    super(createEditingDomain(), createSelectionProvider(viewer, factory), OomphTransferDelegate.DELEGATES);
  }

  private static EditingDomain createEditingDomain()
  {
    AdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
    EditingDomain editingDomain = new OomphEditingDomain(adapterFactory, new BasicCommandStack(), new HashMap<Resource, Boolean>(),
        OomphTransferDelegate.DELEGATES);
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
          List<EObject> objects = factory.createDraggedObjects(selection);
          if (objects != null && !objects.isEmpty())
          {
            return new StructuredSelection(objects);
          }
        }
        catch (Exception ex)
        {
          UIPlugin.INSTANCE.log(ex);
        }

        return new StructuredSelection(new Object());
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
    public List<EObject> createDraggedObjects(ISelection selection) throws Exception;
  }
}
