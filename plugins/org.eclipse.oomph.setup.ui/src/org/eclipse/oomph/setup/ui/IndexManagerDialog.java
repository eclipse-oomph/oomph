/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.setup.internal.core.util.IndexManager;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.text.AbstractHoverInformationControlManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Ed Merks
 */
public class IndexManagerDialog extends AbstractSetupDialog
{
  public static final String TITLE = "Catalog Index Manager";

  public static final String DESCRIPTION = "Rename or remove indices in the selected scope. To rename, double click or press F2";

  private static final int REMOVE_ID = IDialogConstants.CLIENT_ID + 1;

  private final IndexManager indexManager = new IndexManager();

  private TableViewer indexViewer;

  private Button removeButton;

  private Map<URI, String> originalIndexChoices;

  private Map<URI, String> indexChoices;

  private Map<URI, Boolean> indexAvailability;

  private IndexScope indexScope = IndexScope.LOCAL;

  private URI originalIndexLocation;

  public IndexManagerDialog(Shell parentShell)
  {
    super(parentShell, "Catalog Indices", 750, 300, SetupUIPlugin.INSTANCE, true);
    indexChoices = indexManager.getIndexLabels(true);

    originalIndexChoices = new LinkedHashMap<URI, String>(indexChoices);
    if (!originalIndexChoices.isEmpty())
    {
      originalIndexLocation = originalIndexChoices.keySet().iterator().next();
    }

    new Job("Availablity Checker")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        indexAvailability = indexManager.getIndexAvailability(false);
        UIUtil.asyncExec(indexViewer.getControl(), new Runnable()
        {
          public void run()
          {
            indexViewer.refresh();
          }
        });

        return Status.OK_STATUS;
      }
    }.schedule(1000);
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return DESCRIPTION + ".";
  }

  @Override
  protected void createUI(Composite parent)
  {
    indexViewer = new TableViewer(parent, SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    indexViewer.getTable().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
    indexViewer.setContentProvider(new IStructuredContentProvider()
    {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
        // Do nothing.
      }

      public void dispose()
      {
        // Do nothing.
      }

      public Object[] getElements(Object inputElement)
      {
        return indexChoices.entrySet().toArray();
      }
    });

    final ColumnViewerInformationControlToolTipSupport columnViewerInformationControlToolTipSupport = new ColumnViewerInformationControlToolTipSupport(
        indexViewer, new LocationListener()
        {
          public void changing(LocationEvent event)
          {
          }

          public void changed(LocationEvent event)
          {
          }
        });

    class MyLabelProvider extends CellLabelProvider implements ILabelProvider
    {
      private final Color gray = indexViewer.getTable().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);

      private final Font normalFont = indexViewer.getTable().getFont();

      private final Font italicFont = ExtendedFontRegistry.INSTANCE.getFont(normalFont, IItemFontProvider.ITALIC_FONT);

      private final Font boldFont = ExtendedFontRegistry.INSTANCE.getFont(normalFont, IItemFontProvider.BOLD_FONT);

      public String getText(Object element)
      {
        return asMapEntry(element).getValue();
      }

      public Image getImage(Object element)
      {
        return SetupUIPlugin.INSTANCE.getSWTImage("full/obj16/Index");
      }

      @Override
      public String getToolTipText(Object element)
      {
        URI indexLocation = asMapEntry(element).getKey();
        Map<URI, String> indexNames = indexManager.getIndexNames(true);

        StringBuilder result = new StringBuilder();

        String nameLine = "<divs style='white-space:nowrap;'><b>name</b>:&nbsp;" + indexNames.get(indexLocation) + "</div>\n";
        result.append(nameLine);

        String locationLine = "<div style='white-space:nowrap;'><b>location</b>:&nbsp;" + indexLocation + "</div>\n";
        result.append(locationLine);

        String availabilityLine = "<div style='white-space:nowrap;'><b>availability</b>:&nbsp;"
            + (indexAvailability == null ? "Unknown" : indexAvailability.get(indexLocation) ? "Available" : "Unavailable") + "</div><br/><br/>\n";
        result.append(availabilityLine);

        try
        {
          AbstractHoverInformationControlManager hoverInformationControlManager = ReflectUtil.getValue("hoverInformationControlManager",
              columnViewerInformationControlToolTipSupport);
          int max = Math.max(nameLine.length(), locationLine.length());
          hoverInformationControlManager.setSizeConstraints(max, 6, false, false);
        }
        catch (Throwable throwable)
        {
          // Ignore.
        }

        return result.toString();
      }

      @Override
      public void update(ViewerCell cell)
      {
        Object element = cell.getElement();
        cell.setImage(getImage(element));
        URI indexLocation = asMapEntry(element).getKey();
        if (!originalIndexChoices.containsKey(indexLocation))
        {
          cell.setForeground(gray);
        }

        if (indexLocation.equals(originalIndexLocation))
        {
          cell.setFont(boldFont);
        }
        else if (indexAvailability != null && !indexAvailability.get(indexLocation))
        {
          cell.setFont(italicFont);
        }
        else
        {
          cell.setFont(normalFont);
        }

        cell.setText(getText(element) + "  ");
      }
    }

    final ILabelProvider labelProvider = new MyLabelProvider();
    indexViewer.setLabelProvider(labelProvider);

    final TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(indexViewer, new FocusCellOwnerDrawHighlighter(indexViewer));

    indexViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        focusCellManager.getFocusCell();
        updateEnablement();
      }
    });

    indexViewer.setInput(indexManager);

    TextCellEditor textCellEditor = new TextCellEditor(indexViewer.getTable(), SWT.BORDER);
    indexViewer.setCellEditors(new CellEditor[] { textCellEditor });
    indexViewer.setColumnProperties(new String[] { "label" });

    ColumnViewerEditorActivationStrategy editorActivationStrategy = new ColumnViewerEditorActivationStrategy(indexViewer)
    {
      @Override
      protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event)
      {
        return event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
            || event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.F2;
      }
    };

    TableViewerEditor.create(indexViewer, focusCellManager, editorActivationStrategy, ColumnViewerEditor.KEYBOARD_ACTIVATION);
    indexViewer.setCellModifier(new ICellModifier()
    {
      public void modify(Object element, String property, Object value)
      {
        asMapEntry(((TableItem)element).getData()).setValue((String)value);
        indexViewer.refresh(true);
      }

      public Object getValue(Object element, String property)
      {
        return labelProvider.getText(element).trim();
      }

      public boolean canModify(Object element, String property)
      {
        return true;
      }
    });

    if (!indexChoices.isEmpty())
    {
      indexViewer.setSelection(new StructuredSelection(indexChoices.entrySet().iterator().next()));
    }
  }

  @Override
  protected Control createButtonBar(Composite parent)
  {
    return createButtonBarWithControls(parent);
  }

  @Override
  protected void createControlsForButtonBar(Composite parent)
  {
    createLabel(parent, "  Scope:");

    final Combo indexCombo = createCombo(parent);
    String[] items = new String[] { "Local Indices", "Local and Global Indices", "Global Indices" };
    indexCombo.setItems(items);
    indexCombo.select(0);

    indexCombo.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        IndexScope oldIndexScope = indexScope;
        indexScope = IndexScope.values()[indexCombo.getSelectionIndex()];
        switch (indexScope)
        {
          case LOCAL:
          {
            if (oldIndexScope == IndexScope.GLOBAL)
            {
              indexChoices = indexManager.getIndexLabels(true);
            }
            else
            {
              indexChoices.keySet().retainAll(indexManager.getIndexLabels(true).keySet());
            }
            break;
          }

          case LOCAL_AND_GLOBAL:
          {
            if (oldIndexScope == IndexScope.GLOBAL)
            {
              indexChoices = indexManager.getIndexLabels(false);
            }
            else
            {
              for (Map.Entry<URI, String> entry : indexManager.getIndexLabels(false).entrySet())
              {
                if (!indexChoices.containsKey(entry.getKey()))
                {
                  indexChoices.put(entry.getKey(), entry.getValue());
                }
              }
            }

            break;
          }

          case GLOBAL:
          {
            indexChoices = indexManager.getGlobalIndexManager().getIndexLabels(true);
            break;
          }
        }

        // It just doesn't show an icon or the right font unless we do it twice.
        indexViewer.refresh();
        indexViewer.refresh();

        updateEnablement();
      }
    });
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    removeButton = createButton(parent, REMOVE_ID, "Remove", false);
    removeButton.setEnabled(false);
    super.createButtonsForButtonBar(parent);

    updateEnablement();
  }

  protected void updateEnablement()
  {
    if (removeButton != null)
    {
      removeButton.setEnabled(!indexViewer.getSelection().isEmpty());
    }
  }

  @Override
  protected void buttonPressed(int buttonId)
  {
    if (buttonId == REMOVE_ID)
    {
      IStructuredSelection selection = (IStructuredSelection)indexViewer.getSelection();
      List<?> list = selection.toList();
      Object newSelection = null;
      boolean visitedRemovedEntry = false;
      for (Map.Entry<URI, String> entry : indexChoices.entrySet())
      {
        if (list.contains(entry))
        {
          visitedRemovedEntry = true;
          if (newSelection != null)
          {
            break;
          }
        }
        else
        {
          newSelection = entry;
          if (visitedRemovedEntry)
          {
            break;
          }
        }
      }

      indexChoices.entrySet().removeAll(list);

      if (newSelection != null)
      {
        indexViewer.setSelection(new StructuredSelection(newSelection));
      }

      indexViewer.refresh();
    }

    super.buttonPressed(buttonId);
  }

  protected Map.Entry<URI, String> asMapEntry(Object object)
  {
    @SuppressWarnings("unchecked")
    Map.Entry<URI, String> result = (Entry<URI, String>)object;
    return result;
  }

  @Override
  public String getHelpPath()
  {
    return SetupUIPlugin.INSTANCE.getSymbolicName() + "/html/IndexManagementHelp.html";
  }

  @Override
  protected void okPressed()
  {
    IndexManager indexManager = indexScope == IndexScope.GLOBAL ? this.indexManager.getGlobalIndexManager() : this.indexManager;
    boolean local = indexScope != IndexScope.LOCAL_AND_GLOBAL;

    Map<URI, String> indexChoices = indexManager.getIndexLabels(true);
    Set<URI> removedURIs = new LinkedHashSet<URI>(indexChoices.keySet());
    removedURIs.removeAll(this.indexChoices.keySet());
    for (URI indexLocation : removedURIs)
    {
      indexManager.remove(indexLocation, local);
    }

    Set<URI> addedURIs = new LinkedHashSet<URI>(this.indexChoices.keySet());
    addedURIs.removeAll(indexChoices.keySet());
    if (!addedURIs.isEmpty())
    {
      Map<URI, String> indexNames = indexManager.getIndexNames(false);
      for (URI indexLocation : addedURIs)
      {
        indexManager.addIndex(indexLocation, indexNames.get(indexLocation), true);
        indexManager.setLabel(indexLocation, this.indexChoices.get(indexLocation), local);
        this.indexChoices.remove(indexLocation);
      }

      URI originalFirstIndexLocation = this.indexChoices.keySet().iterator().next();
      indexManager.addIndex(originalFirstIndexLocation, indexNames.get(originalFirstIndexLocation), true);
    }

    for (Map.Entry<URI, String> entry : this.indexChoices.entrySet())
    {
      if (!indexChoices.get(entry.getKey()).equals(entry.getValue()))
      {
        indexManager.setLabel(entry.getKey(), entry.getValue(), local);
      }
    }

    super.okPressed();
  }

  /**
   * @author Ed Merks
   */
  public static enum IndexScope
  {
    LOCAL, LOCAL_AND_GLOBAL, GLOBAL
  }
}
