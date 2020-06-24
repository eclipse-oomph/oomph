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
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.setup.p2.util.MarketPlaceListing;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Ed Merks
 */
public class ExtensionsDialog extends AbstractSetupDialog
{
  public static final String TITLE = Messages.ExtensionsDialog_title;

  public static final String HEADER = Messages.ExtensionsDialog_header;

  public static final String DESCRIPTION = Messages.ExtensionsDialog_description;

  private CheckboxTableViewer extensionViewer;

  private final Collection<? extends Resource> extensions;

  private final Collection<Resource> result = new LinkedHashSet<Resource>();

  public ExtensionsDialog(Shell parentShell, Collection<? extends Resource> extensions)
  {
    super(parentShell, HEADER, 800, 300, SetupUIPlugin.INSTANCE, false);
    this.extensions = extensions;
  }

  public Collection<? extends Resource> getResult()
  {
    return result;
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return DESCRIPTION + "."; //$NON-NLS-1$
  }

  @Override
  protected void createUI(Composite parent)
  {
    extensionViewer = CheckboxTableViewer.newCheckList(parent, SWT.NONE);

    final Table registrationTable = extensionViewer.getTable();
    registrationTable.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

    extensionViewer.setContentProvider(new IStructuredContentProvider()
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
        return extensions.toArray();
      }
    });

    extensionViewer.setLabelProvider(new LabelProvider()
    {
      @Override
      public Image getImage(Object element)
      {
        URI uri = ((Resource)element).getURI();
        if (MarketPlaceListing.isMarketPlaceListing(uri))
        {
          return SetupUIPlugin.INSTANCE.getSWTImage("marketplace16.png"); //$NON-NLS-1$
        }

        return SetupUIPlugin.INSTANCE.getSWTImage("full/obj16/Configuration"); //$NON-NLS-1$
      }

      @Override
      public String getText(Object element)
      {
        return ((Resource)element).getURI().toString();
      }
    });

    extensionViewer.setInput(extensions);

    extensionViewer.setCheckedElements(extensions.toArray());

    extensionViewer.addCheckStateListener(new ICheckStateListener()
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
    return result;
  }

  protected void updateEnablement()
  {
    Set<Object> checkedElements = new HashSet<Object>(Arrays.asList(extensionViewer.getCheckedElements()));
    boolean catalogSelectionChanged = !checkedElements.equals(new HashSet<Object>(extensions));
    boolean applyEnabled = catalogSelectionChanged;
    getButton(IDialogConstants.OK_ID).setEnabled(applyEnabled);
    extensionViewer.refresh();
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.CLIENT_ID, Messages.ExtensionsDialog_selectAllButton_text, true);
    createButton(parent, IDialogConstants.CLIENT_ID + 1, Messages.ExtensionsDialog_deselectAllButton_text, true);

    super.createButtonsForButtonBar(parent);

    updateEnablement();
  }

  @Override
  protected void buttonPressed(int buttonId)
  {
    if (buttonId == IDialogConstants.CLIENT_ID)
    {
      extensionViewer.setCheckedElements(((Collection<?>)extensionViewer.getInput()).toArray());
      updateEnablement();
    }
    else if (buttonId == IDialogConstants.CLIENT_ID + 1)
    {
      extensionViewer.setCheckedElements(new Object[0]);
      updateEnablement();
    }
    else if (buttonId == IDialogConstants.OK_ID)
    {
      Set<Object> checkedElements = new HashSet<Object>(Arrays.asList(extensionViewer.getCheckedElements()));
      result.addAll(extensions);
      result.retainAll(checkedElements);
    }

    super.buttonPressed(buttonId);
  }
}
