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
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.internal.setup.core.util.UpdateUtil;
import org.eclipse.oomph.setup.internal.installer.bundle.SetupInstallerPlugin;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;

import org.eclipse.emf.common.util.URI;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class AboutDialog extends AbstractSetupDialog
{
  private static final int ECLIPSE_VERSION_COLUMN_INDEX = 1;

  private final String version;

  public AboutDialog(Shell parentShell, String version)
  {
    super(parentShell, "About Development Environment Installer", 700, 500, SetupInstallerPlugin.INSTANCE, null);
    this.version = version;
  }

  @Override
  protected String getDefaultMessage()
  {
    URI uri = SetupContext.INDEX_SETUP_URI;
    uri = EMFUtil.createResourceSet().getURIConverter().normalize(uri);
    return "The current product version is " + version + ".\n" + uri;
  }

  @Override
  protected void createUI(Composite parent)
  {
    final Table table = new Table(parent, SWT.FULL_SELECTION | SWT.NO_SCROLL | SWT.V_SCROLL);
    table.setHeaderVisible(true);
    table.setLinesVisible(true);
    table.setLayoutData(new GridData(GridData.FILL_BOTH));

    final TableColumn idColumn = new TableColumn(table, SWT.NONE);
    idColumn.setText("Installed Unit");
    idColumn.setResizable(false);
    idColumn.setMoveable(false);

    final TableColumn versionColumn = new TableColumn(table, SWT.NONE);
    versionColumn.setText("Version");
    versionColumn.setResizable(false);
    versionColumn.setMoveable(false);

    IProvisioningAgent agent = SetupInstallerPlugin.INSTANCE.getService(IProvisioningAgent.class);

    try
    {
      ProvisioningSession session = new ProvisioningSession(agent);
      List<IInstallableUnit> installedUnits = UpdateUtil.getInstalledUnits(session).getElement2();

      String[][] rows = new String[installedUnits.size()][];
      for (int i = 0; i < rows.length; i++)
      {
        IInstallableUnit installableUnit = installedUnits.get(i);
        rows[i] = new String[] { installableUnit.getId(), installableUnit.getVersion().toString() };
      }

      Arrays.sort(rows, new Comparator<String[]>()
      {
        public int compare(String[] o1, String[] o2)
        {
          return o1[0].compareTo(o2[0]);
        }
      });

      Color blue = getShell().getDisplay().getSystemColor(SWT.COLOR_BLUE);

      for (int i = 0; i < rows.length; i++)
      {
        TableItem item = new TableItem(table, SWT.NONE);

        String id = rows[i][0];
        item.setText(0, id);

        String version = rows[i][ECLIPSE_VERSION_COLUMN_INDEX];
        item.setText(ECLIPSE_VERSION_COLUMN_INDEX, version);

        if (UpdateUtil.hasPrefix(id))
        {
          item.setForeground(blue);
        }
      }

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

          idColumn.setWidth(size.x - versionColumn.getWidth());
        }
      };

      versionColumn.pack();
      versionColumn.setWidth(versionColumn.getWidth() + 10);

      table.addControlListener(columnResizer);
      table.getDisplay().asyncExec(new Runnable()
      {
        public void run()
        {
          columnResizer.controlResized(null);
        }
      });
    }
    finally
    {
      SetupInstallerPlugin.INSTANCE.ungetService(agent);
    }
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, "Close", true);
  }
}
