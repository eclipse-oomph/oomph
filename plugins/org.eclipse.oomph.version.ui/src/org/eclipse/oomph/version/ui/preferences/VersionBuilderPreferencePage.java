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
package org.eclipse.oomph.version.ui.preferences;

import org.eclipse.oomph.internal.ui.AbstractPreferencePage;
import org.eclipse.oomph.internal.version.Activator;
import org.eclipse.oomph.internal.version.Activator.ReleaseCheckMode;
import org.eclipse.oomph.version.VersionUtil;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Eike Stepper
 */
public class VersionBuilderPreferencePage extends AbstractPreferencePage
{
  public VersionBuilderPreferencePage()
  {
    noDefaultAndApplyButton();
  }

  @Override
  protected Control doCreateContents(Composite parent)
  {
    java.util.List<String> releasePaths = new ArrayList<String>(Activator.getReleasePaths());
    Collections.sort(releasePaths);

    final TableViewer viewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.NO_SCROLL | SWT.V_SCROLL);

    final Table table = viewer.getTable();
    table.setLinesVisible(true);
    table.setHeaderVisible(true);

    TableViewerColumn releaseViewerColumn = new TableViewerColumn(viewer, SWT.NONE);
    final TableColumn releaseColumn = releaseViewerColumn.getColumn();
    releaseColumn.setText("Release");
    releaseColumn.setResizable(false);
    releaseColumn.setMoveable(false);

    TableViewerColumn checkModeViewerColumn = new TableViewerColumn(viewer, SWT.NONE);
    final TableColumn checkModeColumn = checkModeViewerColumn.getColumn();
    checkModeColumn.setText("Check Mode");
    checkModeColumn.setResizable(false);
    checkModeColumn.setMoveable(false);

    final ControlAdapter columnResizer = new ControlAdapter()
    {
      @Override
      public void controlResized(ControlEvent e)
      {
        Point size = table.getSize();
        ScrollBar bar = table.getVerticalBar();
        if (bar != null && bar.isVisible())
        {
          size.x -= bar.getSize().x;
        }

        releaseColumn.setWidth(size.x - checkModeColumn.getWidth());
      }
    };

    checkModeColumn.pack();
    checkModeColumn.setWidth(checkModeColumn.getWidth() + 10);

    table.addControlListener(columnResizer);
    table.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        columnResizer.controlResized(null);
      }
    });

    viewer.setColumnProperties(new String[] { "releasePath", "checkMode" });

    viewer.setContentProvider(new ArrayContentProvider());

    viewer.setLabelProvider(new ReleaseCheckModeLabelProvider());

    viewer.setCellModifier(new ICellModifier()
    {
      public boolean canModify(Object element, String property)
      {
        return "checkMode".equals(property);
      }

      public Object getValue(Object element, String property)
      {
        return Activator.getReleaseCheckMode((String)element);
      }

      public void modify(Object element, String property, Object value)
      {
        if (element instanceof TableItem)
        {
          element = ((TableItem)element).getData();
        }
        String releasePath = (String)element;
        Activator.setReleaseCheckMode(releasePath, (ReleaseCheckMode)value);
        viewer.update(element, new String[] { property });
        VersionUtil.cleanReleaseProjects(releasePath);
      }
    });

    ComboBoxViewerCellEditor cellEditor = new ComboBoxViewerCellEditor(table);
    cellEditor.setContentProvider(new IStructuredContentProvider()
    {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public void dispose()
      {
      }

      public Object[] getElements(Object inputElement)
      {
        return ReleaseCheckMode.values();
      }
    });

    viewer.setCellEditors(new CellEditor[] { null, cellEditor });
    cellEditor.setLabelProvider(new LabelProvider());

    viewer.setInput(releasePaths);
    cellEditor.setInput(releasePaths);

    return table;
  }

  /**
   * @author Eike Stepper
   */
  private static final class ReleaseCheckModeLabelProvider extends LabelProvider implements ITableLabelProvider
  {
    public Image getColumnImage(Object element, int columnIndex)
    {
      return null;
    }

    public String getColumnText(Object element, int columnIndex)
    {
      String releasePath = element.toString();
      if (columnIndex == 0)
      {
        return releasePath;
      }

      ReleaseCheckMode releaseCheckMode = Activator.getReleaseCheckMode(releasePath);
      return releaseCheckMode == null ? "bad" : releaseCheckMode.toString();
    }
  }
}
