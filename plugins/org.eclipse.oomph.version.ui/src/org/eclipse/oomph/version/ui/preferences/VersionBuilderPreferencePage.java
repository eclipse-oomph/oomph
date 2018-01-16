/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.oomph.internal.version.Activator.LaxLowerBoundCheckMode;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class VersionBuilderPreferencePage extends AbstractPreferencePage
{
  private final Map<String, ReleaseCheckMode> releaseCheckModes = new LinkedHashMap<String, ReleaseCheckMode>();

  private final Map<String, LaxLowerBoundCheckMode> lowerBoundCheckModes = new LinkedHashMap<String, LaxLowerBoundCheckMode>();

  public VersionBuilderPreferencePage()
  {
    noDefaultButton();

    final List<String> releasePaths = new ArrayList<String>(Activator.getReleasePaths());
    Collections.sort(releasePaths);
    for (String releasePath : releasePaths)
    {
      ReleaseCheckMode releaseCheckMode = Activator.getReleaseCheckMode(releasePath);
      if (releaseCheckMode == null)
      {
        releaseCheckMode = ReleaseCheckMode.FULL;
      }

      releaseCheckModes.put(releasePath, releaseCheckMode);

      LaxLowerBoundCheckMode laxLowerBoundCheckMode = Activator.getLaxLowerBoundCheckMode(releasePath);
      if (laxLowerBoundCheckMode == null)
      {
        laxLowerBoundCheckMode = LaxLowerBoundCheckMode.SAME_RELEASE;
      }

      lowerBoundCheckModes.put(releasePath, laxLowerBoundCheckMode);
    }
  }

  @Override
  protected Control doCreateContents(Composite parent)
  {
    final Set<String> releasePaths = releaseCheckModes.keySet();

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
    checkModeColumn.pack();
    checkModeColumn.setWidth(checkModeColumn.getWidth() + 10);
    checkModeColumn.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        boolean uniform = true;
        ReleaseCheckMode minimumReleaseCheckMode = null;
        for (String releasePath : releasePaths)
        {
          ReleaseCheckMode releaseCheckMode = releaseCheckModes.get(releasePath);
          if (minimumReleaseCheckMode == null)
          {
            minimumReleaseCheckMode = releaseCheckMode;
          }
          else if (!minimumReleaseCheckMode.equals(releaseCheckMode))
          {
            uniform = false;
            if (releaseCheckMode.compareTo(minimumReleaseCheckMode) < 0)
            {
              minimumReleaseCheckMode = releaseCheckMode;
            }
          }
        }

        if (uniform)
        {
          ReleaseCheckMode[] values = ReleaseCheckMode.values();
          minimumReleaseCheckMode = values[(minimumReleaseCheckMode.ordinal() + 1) % values.length];
        }

        for (String releasePath : releasePaths)
        {
          releaseCheckModes.put(releasePath, minimumReleaseCheckMode);
        }

        viewer.refresh();
      }
    });

    TableViewerColumn lowerBoundCheckModeViewerColumn = new TableViewerColumn(viewer, SWT.NONE);
    final TableColumn lowerBoundCheckModeColumn = lowerBoundCheckModeViewerColumn.getColumn();
    lowerBoundCheckModeColumn.setText("Lower Bound Check Mode");
    lowerBoundCheckModeColumn.setResizable(false);
    lowerBoundCheckModeColumn.setMoveable(false);
    lowerBoundCheckModeColumn.pack();
    lowerBoundCheckModeColumn.setWidth(lowerBoundCheckModeColumn.getWidth() + 10);
    lowerBoundCheckModeColumn.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        boolean uniform = true;
        LaxLowerBoundCheckMode minimumLowerBoundCheckMode = null;
        for (String releasePath : releasePaths)
        {
          LaxLowerBoundCheckMode lowerBoundCheckMode = lowerBoundCheckModes.get(releasePath);

          if (minimumLowerBoundCheckMode == null)
          {
            minimumLowerBoundCheckMode = lowerBoundCheckMode;
          }
          else if (!minimumLowerBoundCheckMode.equals(lowerBoundCheckMode))
          {
            uniform = false;
            if (lowerBoundCheckMode.compareTo(minimumLowerBoundCheckMode) < 0)
            {
              minimumLowerBoundCheckMode = lowerBoundCheckMode;
            }
          }
        }

        if (uniform)
        {
          LaxLowerBoundCheckMode[] values = LaxLowerBoundCheckMode.values();
          minimumLowerBoundCheckMode = values[(minimumLowerBoundCheckMode.ordinal() + 1) % values.length];
        }

        for (String releasePath : releasePaths)
        {
          lowerBoundCheckModes.put(releasePath, minimumLowerBoundCheckMode);
        }

        viewer.refresh();
      }
    });

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

        releaseColumn.setWidth(size.x - checkModeColumn.getWidth() - lowerBoundCheckModeColumn.getWidth());
      }
    };

    table.addControlListener(columnResizer);
    table.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        columnResizer.controlResized(null);
      }
    });

    viewer.setColumnProperties(new String[] { "releasePath", "checkMode", "lowerBoundCheckMode" });

    viewer.setContentProvider(new ArrayContentProvider());

    viewer.setLabelProvider(new ReleaseCheckModeLabelProvider());

    viewer.setCellModifier(new ICellModifier()
    {
      public boolean canModify(Object element, String property)
      {
        return "checkMode".equals(property) || "lowerBoundCheckMode".equals(property);
      }

      public Object getValue(Object element, String property)
      {
        if (property.equals("checkMode"))
        {
          return releaseCheckModes.get(element);
        }

        return lowerBoundCheckModes.get(element);
      }

      public void modify(Object element, String property, Object value)
      {
        if (element instanceof TableItem)
        {
          element = ((TableItem)element).getData();
        }

        String releasePath = (String)element;
        if (property.equals("checkMode"))
        {
          releaseCheckModes.put(releasePath, (ReleaseCheckMode)value);
        }
        else
        {
          lowerBoundCheckModes.put(releasePath, (LaxLowerBoundCheckMode)value);
        }

        viewer.update(element, new String[] { property });
      }
    });

    ComboBoxViewerCellEditor checkModeCellEditor = new ComboBoxViewerCellEditor(table, SWT.READ_ONLY);
    checkModeCellEditor.setContentProvider(new IStructuredContentProvider()
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

    checkModeCellEditor.setLabelProvider(new LabelProvider());
    checkModeCellEditor.setInput(releasePaths);

    ComboBoxViewerCellEditor lowerBoundCheckModeCellEditor = new ComboBoxViewerCellEditor(table, SWT.READ_ONLY);
    lowerBoundCheckModeCellEditor.setContentProvider(new IStructuredContentProvider()
    {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public void dispose()
      {
      }

      public Object[] getElements(Object inputElement)
      {
        return LaxLowerBoundCheckMode.values();
      }
    });

    lowerBoundCheckModeCellEditor.setLabelProvider(new LabelProvider());
    lowerBoundCheckModeCellEditor.setInput(releasePaths);

    viewer.setCellEditors(new CellEditor[] { null, checkModeCellEditor, lowerBoundCheckModeCellEditor });

    viewer.setInput(releasePaths);

    releaseColumn.pack();
    table.setSize(releaseColumn.getWidth() + checkModeColumn.getWidth() + lowerBoundCheckModeColumn.getWidth(), 100);

    return table;
  }

  @Override
  public boolean performOk()
  {
    performApply();
    return true;
  }

  @Override
  protected void performApply()
  {
    Set<String> changedReleases = new HashSet<String>();

    for (Map.Entry<String, ReleaseCheckMode> entry : releaseCheckModes.entrySet())
    {
      String releasePath = entry.getKey();
      ReleaseCheckMode releaseCheckMode = entry.getValue();
      ReleaseCheckMode oldReleaseCheckMode = Activator.getReleaseCheckMode(releasePath);

      if (releaseCheckMode != oldReleaseCheckMode)
      {
        Activator.setReleaseCheckMode(releasePath, releaseCheckMode);
        if (oldReleaseCheckMode != null || releaseCheckMode != ReleaseCheckMode.FULL)
        {
          changedReleases.add(releasePath);
        }
      }
    }

    for (Map.Entry<String, LaxLowerBoundCheckMode> entry : lowerBoundCheckModes.entrySet())
    {
      String releasePath = entry.getKey();
      LaxLowerBoundCheckMode laxLowerBoundCheckMode = entry.getValue();
      LaxLowerBoundCheckMode oldLaxLowerBoundCheckMode = Activator.getLaxLowerBoundCheckMode(releasePath);

      if (laxLowerBoundCheckMode != oldLaxLowerBoundCheckMode)
      {
        Activator.setLaxLowerBoundCheckMode(releasePath, laxLowerBoundCheckMode);
        if (oldLaxLowerBoundCheckMode != null || laxLowerBoundCheckMode != LaxLowerBoundCheckMode.SAME_RELEASE)
        {
          changedReleases.add(releasePath);
        }
      }
    }

    if (!changedReleases.isEmpty())
    {
      VersionUtil.rebuildReleaseProjects(changedReleases);
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class ReleaseCheckModeLabelProvider extends LabelProvider implements ITableLabelProvider
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

      if (columnIndex == 1)
      {
        ReleaseCheckMode releaseCheckMode = releaseCheckModes.get(releasePath);
        return releaseCheckMode.toString();
      }

      LaxLowerBoundCheckMode laxLowerBoundCheckMode = lowerBoundCheckModes.get(releasePath);
      return laxLowerBoundCheckMode.toString();
    }
  }
}
