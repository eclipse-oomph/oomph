/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.actions;

import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public abstract class AbstractContainerAction extends Action
{
  private ISelectionProvider selectionProvider;

  private SetupTaskContainer container;

  public AbstractContainerAction()
  {
  }

  public AbstractContainerAction(String text, ImageDescriptor image)
  {
    super(text, image);
  }

  public AbstractContainerAction(String text, int style)
  {
    super(text, style);
  }

  public AbstractContainerAction(String text)
  {
    super(text);
  }

  public final ISelectionProvider getSelectionProvider()
  {
    return selectionProvider;
  }

  public final SetupTaskContainer getContainer()
  {
    return container;
  }

  public final void selectionChanged(SelectionChangedEvent event)
  {
    if (getStyle() != AS_CHECK_BOX || !isChecked())
    {
      selectionProvider = event.getSelectionProvider();

      ISelection selection = event.getSelection();
      if (selection instanceof IStructuredSelection)
      {
        IStructuredSelection structuredSelection = (IStructuredSelection)selection;
        if (structuredSelection.size() == 1)
        {
          Object element = structuredSelection.getFirstElement();
          if (element instanceof EObject)
          {
            container = getSetupTaskContainer((EObject)element);
            if (container != null)
            {
              setEnabled(true);
              return;
            }
          }
        }
      }

      container = null;
      setEnabled(false);
    }
  }

  @Override
  public final void run()
  {
    if (getStyle() == AS_PUSH_BUTTON || isChecked())
    {
      if (runInit(container))
      {
        ChangeCommand command = new ChangeCommand(container.eResource())
        {
          @Override
          protected void doExecute()
          {
            runModify(container);
          }
        };

        EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(container);
        CommandStack commandStack = editingDomain.getCommandStack();
        commandStack.execute(command);

        runDone(container);

        if (getStyle() == AS_CHECK_BOX)
        {
          setChecked(false);
        }
      }
    }
  }

  protected boolean runInit(SetupTaskContainer container)
  {
    return true;
  }

  protected abstract void runModify(SetupTaskContainer container);

  protected void runDone(SetupTaskContainer container)
  {
    expandItem(container);
  }

  protected final void expandItem(final EObject object)
  {
    if (selectionProvider instanceof IViewerProvider)
    {
      IViewerProvider viewerProvider = (IViewerProvider)selectionProvider;
      final Viewer viewer = viewerProvider.getViewer();
      if (viewer instanceof TreeViewer)
      {
        UIUtil.getDisplay().asyncExec(new Runnable()
        {
          @Override
          public void run()
          {
            TreeViewer treeViewer = (TreeViewer)viewer;
            expand(treeViewer, object);

            IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();

            @SuppressWarnings("unchecked")
            List<Object> list = selection.toList();
            list = new ArrayList<>(list);
            list.add(object);

            treeViewer.setSelection(new StructuredSelection(list));
          }

          private void expand(TreeViewer treeViewer, EObject object)
          {
            treeViewer.setExpandedState(object, true);

            EObject eContainer = object.eContainer();
            if (eContainer != null)
            {
              expand(treeViewer, eContainer);
            }
          }
        });
      }
    }
  }

  private SetupTaskContainer getSetupTaskContainer(EObject object)
  {
    while (object != null && !(object instanceof SetupTaskContainer))
    {
      object = object.eContainer();
    }

    return (SetupTaskContainer)object;
  }
}
