/*
 * Copyright (c) 2019 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.ui.AbstractSetupDialog;

import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ed Merks
 */
public class URISchemeDialog extends AbstractSetupDialog
{
  public static final String TITLE = "Web Links Activation";

  public static final String DESCRIPTION = "Select the schemes to activate";

  private static final int APPLY_ID = IDialogConstants.CLIENT_ID + 1;

  private Map<String, String> registrations = new LinkedHashMap<String, String>();

  private CheckboxTableViewer registrationViewer;

  private Button applyButton;

  public URISchemeDialog(Shell parentShell)
  {
    super(parentShell, TITLE, 800, 300, SetupInstallerPlugin.INSTANCE, false);

    registrations.putAll(URISchemeUtil.getRegistrations());
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    if (!URISchemeUtil.getConflictingRegistrations().isEmpty())
    {
      return DESCRIPTION + ". Each bold entry will replace another application's registration.";
    }

    return DESCRIPTION + ".";
  }

  @Override
  protected void createUI(Composite parent)
  {
    TableColumnLayout tableLayout = new TableColumnLayout();
    parent.setLayout(tableLayout);

    registrationViewer = CheckboxTableViewer.newCheckList(parent, SWT.NONE);

    final Table registrationTable = registrationViewer.getTable();
    registrationTable.setLinesVisible(true);
    registrationTable.setHeaderVisible(true);
    registrationTable.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

    registrationViewer.setContentProvider(new IStructuredContentProvider()
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
        return registrations.entrySet().toArray();
      }
    });

    TableViewerColumn schemeColumn = new TableViewerColumn(registrationViewer, SWT.NONE);
    schemeColumn.getColumn().setText("Scheme");
    tableLayout.setColumnData(schemeColumn.getColumn(), new ColumnWeightData(30, true));
    schemeColumn.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        return ((Map.Entry<?, ?>)element).getKey().toString();
      }
    });

    TableViewerColumn launcherColumn = new TableViewerColumn(registrationViewer, SWT.NONE);
    launcherColumn.getColumn().setText("Application");
    tableLayout.setColumnData(launcherColumn.getColumn(), new ColumnWeightData(70, true));
    launcherColumn.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        return ((Map.Entry<?, ?>)element).getValue().toString();
      }

      @Override
      public Font getFont(Object element)
      {
        boolean isConflict = URISchemeUtil.getConflictingRegistrations().containsKey(((Map.Entry<?, ?>)element).getKey());
        return isConflict ? ExtendedFontRegistry.INSTANCE.getFont(registrationTable.getFont(), IItemFontProvider.BOLD_FONT) : null;
      }
    });

    registrationViewer.setInput(registrations);

    registrationViewer.setCheckedElements(getCurrentSelfRegistrations().entrySet().toArray());

    registrationViewer.addCheckStateListener(new ICheckStateListener()
    {
      public void checkStateChanged(CheckStateChangedEvent event)
      {
        updateEnablement();
      }
    });
  }

  protected Map<String, String> getCurrentSelfRegistrations()
  {
    Map<String, String> result = new LinkedHashMap<String, String>();
    String selfLauncher = URISchemeUtil.getSelfLauncher();
    Set<String> selfRegistrations = URISchemeUtil.getSelfRegistrations();
    for (String scheme : selfRegistrations)
    {
      result.put(scheme, selfLauncher);

    }
    return result;
  }

  protected void updateEnablement()
  {
    Set<Object> checkedElements = new HashSet<Object>(Arrays.asList(registrationViewer.getCheckedElements()));
    boolean selectionChanged = !checkedElements.equals(getCurrentSelfRegistrations().entrySet());
    boolean applyEnabled = selectionChanged;
    applyButton.setEnabled(applyEnabled);
    applyButton.setText("Apply");

    Map<String, String> currentRegistrations = URISchemeUtil.getRegistrations();
    String selfLauncher = URISchemeUtil.getSelfLauncher();
    for (Map.Entry<String, String> entry : registrations.entrySet())
    {
      if (checkedElements.contains(entry))
      {
        entry.setValue(selfLauncher);
      }
      else
      {
        entry.setValue(currentRegistrations.get(entry.getKey()));
      }
    }

    registrationViewer.refresh();
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    applyButton = createButton(parent, APPLY_ID, "Apply", false);
    applyButton.setEnabled(false);
    super.createButtonsForButtonBar(parent);

    updateEnablement();
  }

  @Override
  protected void buttonPressed(int buttonId)
  {
    if (buttonId == APPLY_ID || buttonId == IDialogConstants.OK_ID)
    {
      Map<String, String> newRegistrations = new LinkedHashMap<String, String>(registrations);
      Set<Object> checkedElements = new HashSet<Object>(Arrays.asList(registrationViewer.getCheckedElements()));
      String selfLauncher = URISchemeUtil.getSelfLauncher();
      for (Map.Entry<String, String> entry : newRegistrations.entrySet())
      {
        if (!checkedElements.contains(entry) && entry.getValue().equals(selfLauncher))
        {
          entry.setValue("");
        }
      }

      URISchemeUtil.setRegistrations(newRegistrations);
    }

    super.buttonPressed(buttonId);
  }
}
