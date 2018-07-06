/*
 * Copyright (c) 2017, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.version.ui.dialogs;

import org.eclipse.oomph.internal.version.IVersionBuilderArguments;
import org.eclipse.oomph.internal.version.VersionBuilderArguments;
import org.eclipse.oomph.version.ui.Activator;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class ExtendedConfigurationDialog extends TitleAreaDialog
{
  private static final String BUILDER_CONFIGURATION = "Extended Version Builder Configuration";

  private static final ColumnHandler[] COLUMN_HANDLERS = {
      new ColumnHandler(IVersionBuilderArguments.IGNORE_MALFORMED_VERSIONS_ARGUMENT, "Ignore malformed versions"),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_FEATURE_NATURE_ARGUMENT, "Ignore feature nature"),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_SCHEMA_BUILDER_ARGUMENT, "Ignore schema builder"),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_DEBUG_OPTIONS_ARGUMENT, "Ignore debug options"),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_DEPENDENCY_RANGES_ARGUMENT, "Ignore missing dependency version ranges"),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_LAX_LOWER_BOUND_VERSIONS_ARGUMENT, "Ignore lax lower bound dependency versions"),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_EXPORT_VERSIONS_ARGUMENT, "Ignore missing package export versions"),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_CONTENT_CHANGES_ARGUMENT, "Ignore feature content changes"),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_CONTENT_REDUNDANCY_ARGUMENT, "Ignore feature content redundancy"),
      new ColumnHandler(IVersionBuilderArguments.CHECK_CLOSURE_COMPLETENESS_ARGUMENT, "Check feature closure completeness"),
      new ColumnHandler(IVersionBuilderArguments.CHECK_CLOSURE_CONTENT_ARGUMENT, "Check feature closure content"),
      new ColumnHandler(IVersionBuilderArguments.CHECK_MAVEN_POM_ARGUMENT, "Check Maven POM") };

  private final Map<IProject, VersionBuilderArguments> map = new HashMap<IProject, VersionBuilderArguments>();

  private Table table;

  private Image projectImage;

  public ExtendedConfigurationDialog(Shell parentShell, Map<IProject, VersionBuilderArguments> map)
  {
    super(parentShell);
    setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.APPLICATION_MODAL);
    setHelpAvailable(false);

    for (Map.Entry<IProject, VersionBuilderArguments> entry : map.entrySet())
    {
      VersionBuilderArguments arguments = entry.getValue();
      if (arguments != null)
      {
        arguments = new VersionBuilderArguments(arguments);
      }

      this.map.put(entry.getKey(), arguments);
    }
  }

  public final Map<IProject, VersionBuilderArguments> getMap()
  {
    return map;
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    newShell.setText(BUILDER_CONFIGURATION);
    super.configureShell(newShell);
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    setTitle(BUILDER_CONFIGURATION);
    setMessage("Select a release specification file and check additional settings.");

    projectImage = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/Project.gif").createImage();

    Composite dialogArea = (Composite)super.createDialogArea(parent);

    Composite composite = new Composite(dialogArea, SWT.NONE);
    composite.setLayout(new FillLayout());
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));

    table = new Table(composite, SWT.SINGLE | SWT.FULL_SELECTION);
    table.setHeaderVisible(true);

    TableColumn column0 = new TableColumn(table, SWT.NONE);
    column0.setWidth(300);
    column0.setText("Project");

    for (ColumnHandler columnHandler : COLUMN_HANDLERS)
    {
      columnHandler.createColumn(table);
    }

    List<IProject> list = new ArrayList<IProject>(map.keySet());
    Collections.sort(list, new Comparator<IProject>()
    {
      public int compare(IProject p1, IProject p2)
      {
        return p1.getName().compareTo(p2.getName());
      }
    });

    for (IProject project : list)
    {
      VersionBuilderArguments arguments = map.get(project);

      TableItem item = new TableItem(table, SWT.NONE);
      item.setText(0, project.getName());
      item.setImage(0, projectImage);

      if (arguments == null)
      {
        item.setForeground(0, table.getDisplay().getSystemColor(SWT.COLOR_GRAY));
      }
      else
      {
        int column = 1;
        for (ColumnHandler columnHandler : COLUMN_HANDLERS)
        {
          columnHandler.populateItem(table, item, arguments, column++);
        }
      }
    }

    return dialogArea;
  }

  @Override
  public boolean close()
  {
    if (projectImage != null)
    {
      projectImage.dispose();
    }

    return super.close();
  }

  /**
   * @author Eike Stepper
   */
  private static final class ColumnHandler
  {
    private String key;

    private String header;

    public ColumnHandler(String key, String header)
    {
      this.key = key;
      this.header = header;
    }

    public void createColumn(Table table)
    {
      GC gc = new GC(table);
      Point extent = gc.textExtent(header);
      gc.dispose();

      TableColumn column = new TableColumn(table, SWT.CENTER);
      column.setWidth(10 + extent.x + 10);
      column.setText(header);

      final ArrayList<Button> buttons = new ArrayList<Button>();
      column.setData("buttons", buttons);
      column.addSelectionListener(new SelectionListener()
      {
        public void widgetSelected(SelectionEvent e)
        {
          boolean selected = false;
          for (Button button : buttons)
          {
            if (button.getSelection())
            {
              selected = true;
            }
          }

          for (Button button : buttons)
          {
            button.setSelection(!selected);
            button.notifyListeners(SWT.Selection, null);
          }
        }

        public void widgetDefaultSelected(SelectionEvent e)
        {
        }
      });

    }

    public void populateItem(Table table, TableItem item, final VersionBuilderArguments arguments, int column)
    {
      final Button button = new Button(table, SWT.CHECK);
      button.setSelection(isChecked(arguments));
      button.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          setChecked(arguments, button.getSelection());
        }
      });

      Point size = button.getSize();

      TableEditor editor = new TableEditor(table);
      editor.grabHorizontal = true;
      editor.minimumWidth = size.x;
      editor.minimumHeight = size.y;
      editor.setEditor(button, item, column);

      @SuppressWarnings("unchecked")
      List<Button> buttons = (List<Button>)table.getColumns()[column].getData("buttons");
      buttons.add(button);
    }

    protected boolean isChecked(VersionBuilderArguments arguments)
    {
      return "true".equals(arguments.get(key));
    }

    protected void setChecked(VersionBuilderArguments arguments, boolean checked)
    {
      if (checked)
      {
        arguments.put(key, Boolean.toString(true));
      }
      else
      {
        arguments.remove(key);
      }
    }
  }
}
