/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.util.OomphPlugin;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import org.osgi.framework.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class AboutDialog extends AbstractSetupDialog
{
  private static final int ECLIPSE_VERSION_COLUMN_INDEX = 1;

  private static final String SHOW_ALL_PLUGINS = "SHOW_ALL_PLUGINS";

  private final IDialogSettings dialogSettings = getDialogSettings();

  private final String version;

  private boolean showAllPlugins;

  private Profile profile;

  private Table table;

  private TableColumn idColumn;

  private TableColumn versionColumn;

  private ControlAdapter columnResizer = new ControlAdapter()
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

      idColumn.setWidth(size.x - versionColumn.getWidth());
    }
  };

  private Color gray;

  public AboutDialog(Shell parentShell, String theVersion)
  {
    super(parentShell, "About " + parentShell.getText(), 700, 500, SetupInstallerPlugin.INSTANCE, false);
    version = theVersion;
    showAllPlugins = dialogSettings.getBoolean(SHOW_ALL_PLUGINS);
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Installer version: " + version + "\n" + SetupUtil.INSTALLER_UPDATE_URL;
  }

  @Override
  protected void createUI(Composite parent)
  {
    table = new Table(parent, SWT.FULL_SELECTION | SWT.NO_SCROLL | SWT.V_SCROLL);
    table.setHeaderVisible(true);
    table.setLinesVisible(true);
    table.setLayoutData(new GridData(GridData.FILL_BOTH));
    table.addControlListener(columnResizer);

    idColumn = new TableColumn(table, SWT.NONE);
    idColumn.setText("Plugin");
    idColumn.setResizable(false);
    idColumn.setMoveable(false);

    versionColumn = new TableColumn(table, SWT.NONE);
    versionColumn.setText("Version");
    versionColumn.setResizable(false);
    versionColumn.setMoveable(false);

    Agent agent = P2Util.getAgentManager().getCurrentAgent();
    profile = agent.getCurrentProfile();

    gray = getShell().getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE);

    fillTable();
  }

  private void fillTable()
  {
    List<IInstallableUnit> plugins = getPlugins();
    Collections.sort(plugins);

    for (IInstallableUnit plugin : plugins)
    {
      TableItem item = new TableItem(table, SWT.NONE);

      String id = plugin.getId();
      item.setText(0, id);

      String version = plugin.getVersion().toString();

      if (id.startsWith(SetupCoreUtil.OOMPH_NAMESPACE))
      {
        try
        {
          Bundle[] bundles = Platform.getBundles(id, version);
          if (bundles != null)
          {
            for (Bundle bundle : bundles)
            {
              String buildID = OomphPlugin.getBuildID(bundle);
              if (buildID != null)
              {
                version += " Build " + buildID;
                break;
              }
            }
          }
        }
        catch (Exception ex)
        {
          SetupInstallerPlugin.INSTANCE.log(ex);
        }
      }
      else
      {
        item.setForeground(gray);
      }

      item.setText(ECLIPSE_VERSION_COLUMN_INDEX, version);
    }

    versionColumn.pack();
    versionColumn.setWidth(versionColumn.getWidth() + 10);

    table.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        columnResizer.controlResized(null);
      }
    });
  }

  private List<IInstallableUnit> getPlugins()
  {
    List<IInstallableUnit> plugins = new ArrayList<IInstallableUnit>();
    for (IInstallableUnit iu : profile.query(QueryUtil.createIUAnyQuery(), null))
    {
      if (showAllPlugins || iu.getId().startsWith(SetupCoreUtil.OOMPH_NAMESPACE))
      {
        for (IProvidedCapability capability : iu.getProvidedCapabilities())
        {
          if ("osgi.bundle".equals(capability.getNamespace()))
          {
            plugins.add(iu);
            break;
          }
        }
      }
    }

    return plugins;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    final Button showAllPluginsButton = createCheckbox(parent, "Show all plugins");
    showAllPluginsButton.setSelection(showAllPlugins);
    showAllPluginsButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        showAllPlugins = showAllPluginsButton.getSelection();
        dialogSettings.put(SHOW_ALL_PLUGINS, showAllPlugins);

        table.removeAll();
        fillTable();
      }
    });

    createButton(parent, IDialogConstants.OK_ID, "Close", true);
  }

  @Override
  protected String getShellText()
  {
    return PropertiesUtil.getProductName();
  }
}
