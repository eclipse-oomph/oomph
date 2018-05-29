/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.edit;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.common.command.StrictCompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CopyCommand;
import org.eclipse.emf.edit.command.PasteFromClipboardCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Ed Merks
 */
public class BasePasteFromClipboardCommand extends PasteFromClipboardCommand
{
  protected Collection<?> collection;

  protected boolean isPrimary;

  public BasePasteFromClipboardCommand(EditingDomain domain, Object owner, Object feature, Collection<?> collection, int index, boolean isPrimary)
  {
    super(domain, owner, feature, index, true);
    this.collection = collection;
    this.isPrimary = isPrimary;
  }

  @Override
  protected boolean prepare()
  {
    // Create a strict compound command to do a copy and then add the result
    //
    command = new StrictCompoundCommand();

    // Create a command to copy the clipboard.
    //
    final Command copyCommand = CopyCommand.create(domain, collection);
    command.append(copyCommand);

    // Create a proxy that will create an add command.
    //
    command.append(new CommandWrapper()
    {
      protected Collection<Object> copy;

      @Override
      protected Command createCommand()
      {
        copy = new ArrayList<Object>(copyCommand.getResult());
        Command addCommand = createAddCommand(domain, owner, feature, copy, index);
        return addCommand;
      }
    });

    Command addCommand = createAddCommand(domain, owner, feature, collection, index);
    boolean result = addCommand.canExecute();
    addCommand.dispose();
    return result;
  }

  protected Command createAddCommand(EditingDomain domain, Object owner, Object feature, Collection<?> collection, int index)
  {
    return isPrimary ? AddCommand.create(domain, owner, feature, collection, index) : BasePasteCommand.create(domain, owner, feature, collection, index);
  }
}
