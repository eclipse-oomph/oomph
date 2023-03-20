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
import org.eclipse.jface.resource.ImageDescriptor;
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
  private static final ColumnHandler[] COLUMN_HANDLERS = {
      new ColumnHandler(IVersionBuilderArguments.IGNORE_MALFORMED_VERSIONS_ARGUMENT, Messages.ExtendedConfigurationDialog_column_ignoreMalformedVersions),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_FEATURE_NATURE_ARGUMENT, Messages.ExtendedConfigurationDialog_column_ignoreFeatureNature),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_SCHEMA_BUILDER_ARGUMENT, Messages.ExtendedConfigurationDialog_column_ignoreSchemaBuilder),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_DEBUG_OPTIONS_ARGUMENT, Messages.ExtendedConfigurationDialog_column_ignoreDebugOptions),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_DEPENDENCY_RANGES_ARGUMENT, Messages.ExtendedConfigurationDialog_column_ignoreMissingVersionRanges),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_LAX_LOWER_BOUND_VERSIONS_ARGUMENT,
          Messages.ExtendedConfigurationDialog_column_ignoreLaxLowerVersionBound),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_EXPORT_VERSIONS_ARGUMENT,
          Messages.ExtendedConfigurationDialog_column_ignoreMissingPackageExportVersions),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_CONTENT_CHANGES_ARGUMENT, Messages.ExtendedConfigurationDialog_column_ignoreFeatureChanges),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_CONTENT_REDUNDANCY_ARGUMENT, Messages.ExtendedConfigurationDialog_column_ignoreFeatureRedundancy),
      new ColumnHandler(IVersionBuilderArguments.IGNORE_MISSING_NATURE_ARGUMENT, Messages.ExtendedConfigurationDialog_IgnoreMissingVersionNature),
      new ColumnHandler(IVersionBuilderArguments.CHECK_CLOSURE_COMPLETENESS_ARGUMENT,
          Messages.ExtendedConfigurationDialog_column_checkFeatureClosureCompleteness),
      new ColumnHandler(IVersionBuilderArguments.CHECK_CLOSURE_CONTENT_ARGUMENT, Messages.ExtendedConfigurationDialog_column_checkFeatureClosureContent),
      new ColumnHandler(IVersionBuilderArguments.CHECK_MAVEN_POM_ARGUMENT, Messages.ExtendedConfigurationDialog_column_checkMavenPom) };

  private final Map<IProject, VersionBuilderArguments> map = new HashMap<>();

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
    newShell.setText(Messages.ExtendedConfigurationDialog_title);
    super.configureShell(newShell);
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    setTitle(Messages.ExtendedConfigurationDialog_title);
    setMessage(Messages.ExtendedConfigurationDialog_message);

    ImageDescriptor imageDescriptor = Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/Project.gif"); //$NON-NLS-1$
    projectImage = imageDescriptor.createImage();

    Composite dialogArea = (Composite)super.createDialogArea(parent);

    Composite composite = new Composite(dialogArea, SWT.NONE);
    composite.setLayout(new FillLayout());
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));

    table = new Table(composite, SWT.SINGLE | SWT.FULL_SELECTION);
    table.setHeaderVisible(true);

    TableColumn column0 = new TableColumn(table, SWT.NONE);
    column0.setWidth(300);
    column0.setText(Messages.ExtendedConfigurationDialog_column_project);

    for (ColumnHandler columnHandler : COLUMN_HANDLERS)
    {
      columnHandler.createColumn(table);
    }

    List<IProject> list = new ArrayList<>(map.keySet());
    Collections.sort(list, new Comparator<IProject>()
    {
      @Override
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

      final ArrayList<Button> buttons = new ArrayList<>();
      column.setData("buttons", buttons); //$NON-NLS-1$
      column.addSelectionListener(new SelectionListener()
      {
        @Override
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

        @Override
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
      List<Button> buttons = (List<Button>)table.getColumns()[column].getData("buttons"); //$NON-NLS-1$
      buttons.add(button);
    }

    protected boolean isChecked(VersionBuilderArguments arguments)
    {
      return "true".equals(arguments.get(key)); //$NON-NLS-1$
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
