/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetSorter;

/**
 * @author Ed Merks
 */
public class OomphPropertySheetPage extends ExtendedPropertySheetPage
{
  private Tree tree;

  private ControlAdapter columnResizer;

  private CopyValuePropertyAction copyPropertyValueAction;

  private Clipboard clipboard;

  public OomphPropertySheetPage(AdapterFactoryEditingDomain editingDomain, Decoration decoration, IDialogSettings dialogSettings)
  {
    super(editingDomain, decoration, dialogSettings);

    setSorter(new PropertySheetSorter()
    {
      @Override
      public void sort(IPropertySheetEntry[] entries)
      {
        // Intentionally left empty
      }
    });
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
    addColumnResizer();

    Menu menu = getControl().getMenu();
    IMenuManager menuManager = (IMenuManager)menu.getData("org.eclipse.jface.action.MenuManager.managerKey");
    menuManager.insertAfter("copy", copyPropertyValueAction);
  }

  private void addColumnResizer()
  {
    tree = (Tree)getControl();

    final TreeColumn propertyColumn = tree.getColumn(0);
    propertyColumn.setResizable(false);

    final TreeColumn valueColumn = tree.getColumn(1);
    valueColumn.setResizable(false);

    columnResizer = new ControlAdapter()
    {
      private int clientWidth = -1;

      private int propertyWidth = -1;

      private int valueWidth = -1;

      private boolean resizing;

      @Override
      public void controlResized(ControlEvent e)
      {
        if (resizing)
        {
          return;
        }

        Rectangle clientArea = tree.getClientArea();
        int clientWidth = clientArea.width - clientArea.x;
        ScrollBar bar = tree.getVerticalBar();
        if (bar != null && bar.isVisible())
        {
          clientWidth -= bar.getSize().x;
        }

        int propertyWidth = propertyColumn.getWidth();
        int valueWidth = valueColumn.getWidth();

        boolean inputChanged = e == null;
        if (inputChanged || clientWidth != this.clientWidth || propertyWidth != this.propertyWidth || valueWidth != this.valueWidth)
        {
          try
          {
            resizing = true;
            tree.setRedraw(false);

            TreeItem[] items = tree.getItems();
            if (items.length == 0)
            {
              propertyWidth = clientWidth / 2;
              propertyColumn.setWidth(propertyWidth);
              valueColumn.setWidth(clientWidth - propertyWidth);
            }
            else
            {
              propertyColumn.pack();
              propertyWidth = propertyColumn.getWidth() + 20;
              propertyColumn.setWidth(propertyWidth);

              valueColumn.pack();
              valueWidth = valueColumn.getWidth();

              if (propertyWidth + valueWidth < clientWidth)
              {
                valueWidth = clientWidth - propertyWidth;
                valueColumn.setWidth(valueWidth);
              }
            }
          }
          finally
          {
            this.clientWidth = clientWidth;
            this.propertyWidth = propertyWidth;
            this.valueWidth = valueWidth;

            tree.setRedraw(true);
            resizing = false;
          }
        }
      }
    };

    tree.addControlListener(columnResizer);
    propertyColumn.addControlListener(columnResizer);
    valueColumn.addControlListener(columnResizer);
    resizeColumns();
  }

  private void resizeColumns()
  {
    columnResizer.controlResized(null);
  }

  @Override
  public void selectionChanged(IWorkbenchPart part, ISelection selection)
  {
    super.selectionChanged(part, selection);
    resizeColumns();
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

  /**
   * @author Ed Merks
   */
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
