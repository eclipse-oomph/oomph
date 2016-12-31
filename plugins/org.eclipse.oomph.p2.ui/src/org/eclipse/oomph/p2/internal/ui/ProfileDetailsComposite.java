/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.ui.ToolButton;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class ProfileDetailsComposite extends Composite
{
  private final ComposedAdapterFactory adapterFactory = BaseEditUtil.createAdapterFactory();

  private final Profile profile;

  public ProfileDetailsComposite(Composite parent, int style, final Profile profile)
  {
    super(parent, style);
    this.profile = profile;
    setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
    GridLayout gridLayout = new GridLayout(3, false);
    gridLayout.marginHeight = 0;
    gridLayout.marginWidth = 0;
    setLayout(gridLayout);

    Agent agent = profile.getAgent();
    BundlePool bundlePool = profile.getBundlePool();

    addHeaderRow("Profile", profile.getLocation(), profile.getProfileId()).selectAll();
    addHeaderRow("Agent", agent.getLocation(), null);
    addHeaderRow("Bundle pool", bundlePool == null ? null : bundlePool.getLocation(), null);
    addHeaderRow("Installation", profile.getInstallFolder(), null);

    new Label(this, SWT.NONE);
    new Label(this, SWT.NONE);
    new Label(this, SWT.NONE);

    TabFolder tabFolder = new TabFolder(this, SWT.NONE);
    tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));

    createDefinitionTab(tabFolder);
    createInstalledUnitsTab(tabFolder);
    createPropertiesTab(tabFolder);
  }

  public final Profile getProfile()
  {
    return profile;
  }

  @Override
  public void dispose()
  {
    adapterFactory.dispose();
    super.dispose();
  }

  private Text addHeaderRow(String name, final File location, String value)
  {
    Label label = new Label(this, SWT.NONE);
    label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
    label.setText(name + ":");

    Text text = new Text(this, SWT.BORDER | SWT.READ_ONLY);
    text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    text.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    if (value != null)
    {
      text.setText(value);
    }
    else if (location != null)
    {
      text.setText(location.getAbsolutePath());
    }

    ToolButton button = new ToolButton(this, SWT.PUSH, P2UIPlugin.INSTANCE.getSWTImage("obj16/folder"), false);
    button.setToolTipText("Open " + name.toLowerCase() + " folder");
    button.setEnabled(location != null && location.isDirectory());
    button.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        OS.INSTANCE.openSystemBrowser(location.toURI().toString());
      }
    });

    return text;
  }

  private void createDefinitionTab(TabFolder tabFolder)
  {
    TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
    tabItem.setText("Definition");

    final TreeViewer viewer = new TreeViewer(tabFolder, SWT.NONE);
    viewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
    viewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));

    tabItem.setControl(viewer.getTree());

    AgentManagerComposite.addDragSupport(viewer);

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        ProfileDefinition definition = profile.getDefinition();
        ItemProvider requirements = new ItemProvider(adapterFactory, "Requirements", P2UIPlugin.INSTANCE.getSWTImage("full/obj16/ProfileDefinition"),
            definition.getRequirements());
        ItemProvider repositories = new ItemProvider(adapterFactory, "Repositories", P2UIPlugin.INSTANCE.getSWTImage("full/obj16/RepositoryList"),
            definition.getRepositories());

        ItemProvider input = new ItemProvider(adapterFactory);
        input.getChildren().add(requirements);
        input.getChildren().add(repositories);

        viewer.setInput(input);
        viewer.expandAll();
      }
    });
  }

  private void createInstalledUnitsTab(TabFolder tabFolder)
  {
    TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
    tabItem.setText("Installed Units");

    TableColumnLayout layout = new TableColumnLayout();

    Composite composite = new Composite(tabFolder, SWT.NONE);
    composite.setLayout(layout);
    tabItem.setControl(composite);

    final TableViewer viewer = new TableViewer(composite, SWT.VIRTUAL);
    viewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
    viewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));

    Table table = viewer.getTable();
    TableColumn idColumn = new TableColumn(table, SWT.LEFT);
    layout.setColumnData(idColumn, new ColumnWeightData(100));

    AgentManagerComposite.addDragSupport(viewer);

    UIUtil.asyncExec(viewer.getControl(), new Runnable()
    {
      public void run()
      {
        EList<Requirement> children = new BasicEList<Requirement>();

        for (IInstallableUnit iu : profile.query(QueryUtil.createIUAnyQuery(), null))
        {
          String id = iu.getId();
          VersionRange versionRange = new VersionRange(iu.getVersion().toString());
          Requirement requirement = P2Factory.eINSTANCE.createRequirement(id, versionRange);
          requirement.setMatchExpression(iu.getFilter());
          children.add(requirement);
        }

        ECollections.sort(children, Requirement.COMPARATOR);

        ItemProvider input = new ItemProvider(adapterFactory, children);
        viewer.setInput(input);
      }
    });
  }

  private void createPropertiesTab(TabFolder tabFolder)
  {
    TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
    tabItem.setText("Properties");

    TableColumnLayout layout = new TableColumnLayout();

    Composite composite = new Composite(tabFolder, SWT.NONE);
    composite.setLayout(layout);
    tabItem.setControl(composite);

    final TableViewer viewer = new TableViewer(composite, SWT.FULL_SELECTION | SWT.VIRTUAL);
    viewer.setContentProvider(new ArrayContentProvider());
    viewer.setLabelProvider(new PropertiesLabelProvider());

    Table table = viewer.getTable();
    table.setHeaderVisible(true);
    table.setLinesVisible(true);

    TableColumn keyColumn = new TableColumn(table, SWT.LEFT);
    keyColumn.setText("Key");
    layout.setColumnData(keyColumn, new ColumnWeightData(40));

    TableColumn valueColumn = new TableColumn(table, SWT.LEFT);
    valueColumn.setText("Value");
    layout.setColumnData(valueColumn, new ColumnWeightData(60));

    UIUtil.asyncExec(viewer.getControl(), new Runnable()
    {
      public void run()
      {
        viewer.setInput(profile.getProperties().entrySet());
      }
    });
  }

  /**
   * @author Eike Stepper
   */
  private static final class PropertiesLabelProvider extends LabelProvider implements ITableLabelProvider
  {
    public String getColumnText(Object element, int columnIndex)
    {
      if (element instanceof Map.Entry<?, ?>)
      {
        Map.Entry<?, ?> entry = (Map.Entry<?, ?>)element;

        if (columnIndex == 0)
        {
          Object key = entry.getKey();
          if (key instanceof String)
          {
            return (String)key;
          }
        }
        else if (columnIndex == 1)
        {
          Object value = entry.getValue();
          if (value instanceof String)
          {
            return (String)value;
          }
        }
      }

      return null;
    }

    public Image getColumnImage(Object element, int columnIndex)
    {
      return null;
    }
  }
}
