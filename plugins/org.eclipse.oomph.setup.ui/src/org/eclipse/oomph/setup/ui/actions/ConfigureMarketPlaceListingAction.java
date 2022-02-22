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

import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.setup.Argument;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Macro;
import org.eclipse.oomph.setup.MacroTask;
import org.eclipse.oomph.setup.Parameter;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.p2.util.MarketPlaceListing;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.MarketPlaceListingProcessor;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ed Merks
 */
public class ConfigureMarketPlaceListingAction implements IObjectActionDelegate
{
  private final Set<MacroTask> macroTasks = new HashSet<>();

  private Shell shell;

  public ConfigureMarketPlaceListingAction()
  {
  }

  @Override
  public void setActivePart(IAction action, IWorkbenchPart targetPart)
  {
    shell = targetPart == null ? null : targetPart.getSite().getShell();
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
          if (isValid(macroTask))
          {
            macroTasks.add(macroTask);
          }
        }
      }
    }

    action.setEnabled(shell != null && macroTasks.size() == 1);
    action.setImageDescriptor(SetupUIPlugin.INSTANCE.getImageDescriptor("marketplace16.png")); //$NON-NLS-1$
  }

  @Override
  public void run(IAction action)
  {
    MacroTask macroTask = macroTasks.iterator().next();
    EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(macroTask);
    if (domain != null)
    {
      execute(shell, domain, macroTask);
    }
  }

  public static void configure(Shell shell, EditingDomain domain, MacroTask macroTask)
  {
    if (isValid(macroTask))
    {
      execute(shell, domain, macroTask);
    }
  }

  private static boolean isValid(MacroTask macroTask)
  {
    Macro macro = macroTask.getMacro();
    return macro != null && macro.eResource() != null
        && Diagnostician.INSTANCE.validate(macro.eClass(), macro, null, Diagnostician.INSTANCE.createDefaultContext())
        && MarketPlaceListing.isMarketPlaceListing(macro.eResource().getURI());
  }

  private static void execute(Shell shell, final EditingDomain domain, final MacroTask macroTask)
  {
    final Macro macro = macroTask.getMacro();
    Resource resource = macro.eResource();
    URI uri = resource.getURI();
    final Map<String, Argument> arguments = new LinkedHashMap<>();
    for (Argument argument : macroTask.getArguments())
    {
      Parameter parameter = argument.getParameter();
      if (parameter != null)
      {
        String name = parameter.getName();
        if (name != null)
        {
          arguments.put(name, argument);
        }
      }
    }

    new MarketPlaceListingProcessor(shell, MarketPlaceListing.getMarketPlaceListing(uri, domain.getResourceSet().getURIConverter()), resource)
    {
      @Override
      protected boolean isSelected(Requirement requirement)
      {
        String correspondParameterName = getCorrespondParameterName(requirement);
        Argument argument = arguments.get(correspondParameterName);
        return argument != null && "true".equals(argument.getValue()); //$NON-NLS-1$
      }

      @Override
      protected void applyMarketPlaceListing(Set<Requirement> checkedRequirements)
      {
        CompoundTask compoundTask = SetupFactory.eINSTANCE.createCompoundTask();
        MacroTask newMacroTask = createMacroTask(compoundTask, macro, checkedRequirements);

        Map<String, Parameter> parameters = new LinkedHashMap<>();
        for (Parameter parameter : macro.getParameters())
        {
          String name = parameter.getName();
          parameters.put(name, parameter);
        }

        List<Argument> newArguments = new ArrayList<>();
        Map<Argument, String> newArgumentValues = new LinkedHashMap<>();
        boolean addArgument = false;
        for (Argument argument : newMacroTask.getArguments())
        {
          String name = argument.getParameter().getName();
          Argument otherArgument = arguments.get(name);
          if (otherArgument == null)
          {
            otherArgument = SetupFactory.eINSTANCE.createArgument();
            otherArgument.setParameter(parameters.get(name));
            otherArgument.setValue(argument.getValue());
            addArgument = true;
          }
          else
          {
            newArgumentValues.put(otherArgument, argument.getValue());
          }

          newArguments.add(otherArgument);
        }

        CompoundCommand compoundCommand = new CompoundCommand(Messages.ConfigureMarketPlaceListingAction_commandLabel);
        if (addArgument)
        {
          // The command isn't executable unless we really change something, i.e., add a missing argument.
          compoundCommand.append(SetCommand.create(domain, macroTask, SetupPackage.Literals.MACRO_TASK__ARGUMENTS, newArguments));
        }

        for (Map.Entry<Argument, String> entry : newArgumentValues.entrySet())
        {
          compoundCommand.append(SetCommand.create(domain, entry.getKey(), SetupPackage.Literals.ARGUMENT__VALUE, entry.getValue()));
        }

        domain.getCommandStack().execute(compoundCommand);
      }
    }.processMarketPlaceListing();
  }
}
