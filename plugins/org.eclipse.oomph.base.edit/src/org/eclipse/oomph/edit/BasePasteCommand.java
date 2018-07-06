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
package org.eclipse.oomph.edit;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

import java.util.Collection;

/**
 * This is not a real type of command but rather a marker to create a specialized {@link AddCommand}
 *
 * @author Ed Merks
 */
public abstract class BasePasteCommand implements Command
{
  /**
   * This creates a command to insert a collection of values at a particular index in the specified feature of the owner.
   * The feature will often be null because the domain will deduce it.
   */
  public static Command create(EditingDomain domain, Object owner, Object feature, Collection<?> collection, int index)
  {
    return domain.createCommand(BasePasteCommand.class, new CommandParameter(owner, feature, collection, index));
  }
}
