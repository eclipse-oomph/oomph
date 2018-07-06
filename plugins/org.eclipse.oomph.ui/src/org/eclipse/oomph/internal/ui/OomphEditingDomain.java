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
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.edit.BaseAdapterFactoryEditingDomain;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Ed Merks
 */
public class OomphEditingDomain extends BaseAdapterFactoryEditingDomain
{
  protected List<OomphTransferDelegate> delegates;

  public OomphEditingDomain(AdapterFactory adapterFactory, CommandStack commandStack, Map<Resource, Boolean> resourceToReadOnlyMap,
      List<? extends OomphTransferDelegate> delegates)
  {
    super(adapterFactory, commandStack, resourceToReadOnlyMap);
    this.delegates = new ArrayList<OomphTransferDelegate>(delegates);
  }

  public void registerDragAndDrop(StructuredViewer viewer)
  {
    int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
    List<? extends Transfer> transfersList = OomphTransferDelegate.asTransfers(delegates);
    Transfer[] transfers = transfersList.toArray(new Transfer[transfersList.size()]);
    viewer.addDragSupport(dndOperations, transfers, new OomphDragAdapter(this, viewer, delegates));
    viewer.addDropSupport(dndOperations, transfers, new OomphDropAdapter(this, viewer, delegates));
  }

  @Override
  public Collection<Object> getClipboard()
  {
    Clipboard systemClipboard = new Clipboard(UIUtil.getDisplay());
    try
    {
      for (OomphTransferDelegate delegate : delegates)
      {
        for (TransferData transferData : systemClipboard.getAvailableTypes())
        {
          if (delegate.isSupportedType(transferData))
          {
            Object data = systemClipboard.getContents(delegate.getTransfer());
            if (data != null)
            {
              Collection<?> value = delegate.getValue(this, data);
              if (!value.isEmpty())
              {
                return new ArrayList<Object>(value);
              }
            }
          }
        }
      }

      return super.getClipboard();
    }
    finally
    {
      systemClipboard.dispose();
    }
  }

  @Override
  public void setClipboard(final Collection<Object> clipboard)
  {
    Clipboard systemClipboard = new Clipboard(UIUtil.getDisplay());
    try
    {
      super.setClipboard(clipboard);

      List<Object> data = new ArrayList<Object>();
      List<Transfer> dataTransfers = new ArrayList<Transfer>();
      for (OomphTransferDelegate delegate : delegates)
      {
        delegate.setSelection(this, clipboard == null ? new StructuredSelection() : new StructuredSelection(clipboard.toArray()));
        Object value = delegate.getData();
        if (value != null)
        {
          data.add(value);
          dataTransfers.add(delegate.getTransfer());
        }
      }

      systemClipboard.setContents(data.toArray(), dataTransfers.toArray(new Transfer[dataTransfers.size()]));
    }
    finally
    {
      systemClipboard.dispose();

      for (OomphTransferDelegate delegate : delegates)
      {
        delegate.clear();
      }
    }
  }
}
