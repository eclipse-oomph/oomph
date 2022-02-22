/*
 * Copyright (c) 2019 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.actions;

import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Macro;
import org.eclipse.oomph.setup.MacroTask;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ed Merks
 */
public class InlineMacroTaskAction implements IObjectActionDelegate
{
  private final Set<MacroTask> macroTasks = new HashSet<>();

  public InlineMacroTaskAction()
  {
  }

  @Override
  public void setActivePart(IAction action, IWorkbenchPart targetPart)
  {
  }

  @Override
  public void selectionChanged(IAction action, ISelection selection)
  {
    macroTasks.clear();
    if (selection instanceof IStructuredSelection)
    {
      for (Object element : ((IStructuredSelection)selection).toArray())
      {
        if (element instanceof MacroTask)
        {
          MacroTask macroTask = (MacroTask)element;
          Macro macro = macroTask.getMacro();
          if (macro != null && Diagnostician.INSTANCE.validate(macro.eClass(), macro, null, Diagnostician.INSTANCE.createDefaultContext()))
          {
            macroTasks.add(macroTask);
          }
        }
      }
    }

    action.setEnabled(!macroTasks.isEmpty());
    action.setImageDescriptor(SetupUIPlugin.INSTANCE.getImageDescriptor("inline")); //$NON-NLS-1$
  }

  @Override
  public void run(IAction action)
  {
    MacroTask firstTask = macroTasks.iterator().next();
    EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(firstTask);
    CompoundCommand compoundCommand = new CompoundCommand(Messages.InlineMacroTaskAction_commandLabel);

    EObject rootContainer = EcoreUtil.getRootContainer(firstTask, true);
    EList<Adapter> eAdapters = rootContainer.eAdapters();
    ECrossReferenceAdapter eCrossReferenceAdapter = new ECrossReferenceAdapter();
    eAdapters.add(eCrossReferenceAdapter);

    for (MacroTask macroTask : macroTasks)
    {
      CompoundTask replacement = SetupTaskPerformer.expand(macroTask);
      Collection<EStructuralFeature.Setting> inverseReferences = eCrossReferenceAdapter.getInverseReferences(macroTask);
      for (EStructuralFeature.Setting setting : inverseReferences)
      {
        if (setting.getEStructuralFeature() != SetupPackage.Literals.ARGUMENT__MACRO_TASK)
        {
          compoundCommand
              .append(ReplaceCommand.create(domain, setting.getEObject(), setting.getEStructuralFeature(), macroTask, Collections.singleton(replacement)));
        }
      }
    }

    eAdapters.remove(eCrossReferenceAdapter);

    domain.getCommandStack().execute(compoundCommand);
  }
}
