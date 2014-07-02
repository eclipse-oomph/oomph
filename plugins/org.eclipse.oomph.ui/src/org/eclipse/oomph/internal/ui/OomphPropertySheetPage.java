/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertySheetEntry;

/**
 * @author Ed Merks
 */
public class OomphPropertySheetPage extends ExtendedPropertySheetPage
{
  private CopyValuePropertyAction copyPropertyValueAction;

  private Clipboard clipboard;

  public OomphPropertySheetPage(AdapterFactoryEditingDomain editingDomain, Decoration decoration, IDialogSettings dialogSettings)
  {
    super(editingDomain, decoration, dialogSettings);
  }

  @Override
  public void setRootEntry(IPropertySheetEntry entry)
  {
    Shell shell = getControl().getShell();
    clipboard = new Clipboard(shell.getDisplay());
    copyPropertyValueAction = new CopyValuePropertyAction(clipboard);

    super.setRootEntry(entry);
  }

  @Override
  public void createControl(Composite parent)
  {
    super.createControl(parent);

    Menu menu = getControl().getMenu();
    IMenuManager menuManager = (IMenuManager)menu.getData("org.eclipse.jface.action.MenuManager.managerKey");
    menuManager.insertAfter("copy", copyPropertyValueAction);
  }

  @Override
  public void handleEntrySelection(ISelection selection)
  {
    super.handleEntrySelection(selection);

    copyPropertyValueAction.selectionChanged((IStructuredSelection)selection);
  }

  @Override
  public void dispose()
  {
    super.dispose();

    clipboard.dispose();
  }

  private static class CopyValuePropertyAction extends Action
  {
    private Clipboard clipboard;

    private IStructuredSelection selection;

    public CopyValuePropertyAction(Clipboard clipboard)
    {
      super("Copy &Value");
      this.clipboard = clipboard;
      setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
    }

    @Override
    public void run()
    {
      if (selection != null && !selection.isEmpty())
      {
        IPropertySheetEntry entry = (IPropertySheetEntry)selection.getFirstElement();
        setClipboard(entry.getValueAsString());
      }
    }

    public void selectionChanged(IStructuredSelection selection)
    {
      this.selection = selection;
      setEnabled(!selection.isEmpty());
    }

    private void setClipboard(String text)
    {
      try
      {
        Object[] data = new Object[] { text };
        Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
        clipboard.setContents(data, transferTypes);
      }
      catch (SWTError ex)
      {
        UIPlugin.INSTANCE.log(ex);
      }
    }
  }
}
