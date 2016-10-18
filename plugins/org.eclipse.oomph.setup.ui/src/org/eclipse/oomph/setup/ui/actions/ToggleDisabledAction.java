/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.actions;

import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class ToggleDisabledAction implements IObjectActionDelegate
{
  private final Set<SetupTask> tasks = new HashSet<SetupTask>();

  private boolean disabled;

  public ToggleDisabledAction()
  {
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart)
  {
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
    tasks.clear();
    if (selection instanceof IStructuredSelection)
    {
      IStructuredSelection ssel = (IStructuredSelection)selection;
      for (Iterator<?> it = ssel.iterator(); it.hasNext();)
      {
        Object element = it.next();
        if (element instanceof SetupTask)
        {
          SetupTask task = (SetupTask)element;

          if (tasks.isEmpty())
          {
            disabled = task.isDisabled();
          }
          else
          {
            if (disabled != task.isDisabled())
            {
              tasks.clear();
              action.setEnabled(false);
              action.setChecked(false);
              return;
            }
          }

          tasks.add(task);
        }
      }
    }

    action.setEnabled(!tasks.isEmpty());
    action.setImageDescriptor(SetupUIPlugin.INSTANCE.getImageDescriptor(disabled ? "checkbox_checked" : "checkbox_unchecked"));
    action.setChecked(disabled);
  }

  public void run(IAction action)
  {
    SetupTask firstTask = tasks.iterator().next();
    boolean value = !firstTask.isDisabled();

    EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(firstTask);
    CompoundCommand compoundCommand = new CompoundCommand(value ? "Set Disabled" : "Set Enabled");

    for (SetupTask task : tasks)
    {
      compoundCommand.append(SetCommand.create(editingDomain, task, SetupPackage.Literals.SETUP_TASK__DISABLED, value));
    }

    editingDomain.getCommandStack().execute(compoundCommand);
  }
}
