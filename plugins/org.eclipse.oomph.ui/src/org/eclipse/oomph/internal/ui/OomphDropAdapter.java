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

import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TransferData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ed Merks
 */
public class OomphDropAdapter extends EditingDomainViewerDropAdapter
{
  protected OomphTransferDelegate[] delegates;

  protected Set<OomphTransferDelegate> unavailableDelegates;

  public OomphDropAdapter(EditingDomain domain, Viewer viewer, Collection<? extends OomphTransferDelegate> delegates)
  {
    this(domain, viewer, delegates.toArray(new OomphTransferDelegate[delegates.size()]));
  }

  public OomphDropAdapter(EditingDomain domain, Viewer viewer, OomphTransferDelegate... delegates)
  {
    super(domain, viewer);
    this.delegates = delegates;
  }

  @Override
  public void dragEnter(DropTargetEvent event)
  {
    // Transfer data in't available immediate for Motif, but elsewhere it's immediately available.
    if (IS_MOTIF)
    {
      unavailableDelegates = null;
    }
    else
    {
      unavailableDelegates = new HashSet<OomphTransferDelegate>();
    }

    super.dragEnter(event);
  }

  @Override
  public void dropAccept(DropTargetEvent event)
  {
    // Transfer data isn't available until the drop accept phase for Motif.
    if (IS_MOTIF)
    {
      unavailableDelegates = new HashSet<OomphTransferDelegate>();
    }

    super.dropAccept(event);
  }

  @Override
  protected Collection<?> getDragSource(DropTargetEvent event)
  {
    // This will be non-null as soon as we are able to transfer data.
    // For Motif, this won't be the case until we're in the drop accept phase.
    // Elsewhere the data is available during the entire drag and drop process.
    if (unavailableDelegates != null)
    {
      TransferData dataType = event.currentDataType;
      for (OomphTransferDelegate delegate : delegates)
      {
        if (delegate.isSupportedType(dataType))
        {
          // Determine if the delegate as available data.
          Collection<?> data = delegate.getData(domain, dataType);
          if (data != null && !data.isEmpty())
          {
            return data;
          }

          // If not, don't try to use this delegate again.
          unavailableDelegates.add(delegate);
        }
      }

      // Determine if another delegate has support for one of the other data types.
      for (OomphTransferDelegate delegate : delegates)
      {
        if (!unavailableDelegates.contains(delegate))
        {
          for (TransferData availableDataType : event.dataTypes)
          {
            if (delegate.isSupportedType(availableDataType))
            {
              // If so, make that the current data type.
              event.currentDataType = availableDataType;
              return null;
            }
          }
        }
      }

      // If there are no delegates with data for any of the available data types, veto the process.
      event.detail = DND.DROP_NONE;
    }

    // No data is available at this point in the drag and drop process.
    return null;
  }

  @Override
  public void drop(DropTargetEvent event)
  {
    // A command was created if the source was available early, and the information used to create it was cached...
    if (dragAndDropCommandInformation != null)
    {
      // Recreate the command.
      command = dragAndDropCommandInformation.createCommand();
    }
    else
    {
      // Otherwise, the source should be available now as event.data, and we can create the command.
      source = getDragSource(event);
      Object target = extractDropTarget(event.item);
      command = DragAndDropCommand.create(domain, target, getLocation(event), event.operations, originalOperation, source);
    }

    // If the command can execute...
    if (command.canExecute())
    {
      // Execute it.
      domain.getCommandStack().execute(command);
    }
    else
    {
      // Otherwise, let's call the whole thing off.
      event.detail = DND.DROP_NONE;
      command.dispose();
    }

    // Clean up the state.
    command = null;
    commandTarget = null;
    source = null;
    dragAndDropCommandInformation = null;
    unavailableDelegates = null;
  }
}
