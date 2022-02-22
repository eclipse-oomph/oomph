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
package org.eclipse.oomph.edit;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.IdentityCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CopyCommand;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Ed Merks
 */
public class BaseAdapterFactoryEditingDomain extends AdapterFactoryEditingDomain
{
  private LinkedList<CommandParameter> commandParameters = new LinkedList<>();

  public BaseAdapterFactoryEditingDomain(AdapterFactory adapterFactory, CommandStack commandStack)
  {
    super(adapterFactory, commandStack);
  }

  public BaseAdapterFactoryEditingDomain(AdapterFactory adapterFactory, CommandStack commandStack, Map<Resource, Boolean> resourceToReadOnlyMap)
  {
    super(adapterFactory, commandStack, resourceToReadOnlyMap);
  }

  public BaseAdapterFactoryEditingDomain(AdapterFactory adapterFactory, CommandStack commandStack, ResourceSet resourceSet)
  {
    super(adapterFactory, commandStack, resourceSet);
  }

  public List<CommandParameter> getCommandParameters()
  {
    return Collections.unmodifiableList(commandParameters);
  }

  public void handledAdditions(Collection<?> collection)
  {
  }

  @Override
  public Command createCommand(Class<? extends Command> commandClass, CommandParameter commandParameter)
  {
    commandParameters.push(commandParameter);
    try
    {
      if (commandClass == CopyCommand.class)
      {
        Object owner = commandParameter.getOwner();
        if (owner instanceof URI || owner instanceof String)
        {
          return new IdentityCommand(owner);
        }
      }

      if (commandClass == PasteFromClipboardCommand.class)
      {
        Object owner = commandParameter.getOwner();
        Collection<Object> clipboard = getClipboard();
        Object feature = commandParameter.getFeature();
        int index = commandParameter.getIndex();
        Command primaryPasteCommand = new BasePasteFromClipboardCommand(this, owner, feature, clipboard, index, true);
        if (!primaryPasteCommand.canExecute())
        {
          BasePasteFromClipboardCommand alternativePasteCommand = new BasePasteFromClipboardCommand(this, owner, feature, clipboard, index, false);
          if (alternativePasteCommand.canExecute())
          {
            primaryPasteCommand.dispose();
            return alternativePasteCommand;
          }

          alternativePasteCommand.dispose();
        }

        return primaryPasteCommand;
      }

      return super.createCommand(commandClass, commandParameter);
    }
    finally
    {
      commandParameters.pop();
    }
  }
}
