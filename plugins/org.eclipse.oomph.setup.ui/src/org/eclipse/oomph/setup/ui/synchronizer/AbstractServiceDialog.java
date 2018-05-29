/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.oomph.setup.ui.SetupUIPlugin;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.userstorage.IStorageService;
import org.eclipse.userstorage.ui.AbstractDialog;

/**
 * @author Eike Stepper
 */
public abstract class AbstractServiceDialog extends AbstractDialog
{
  private static final String TITLE = "Preference Synchronization";

  private final IStorageService service;

  public AbstractServiceDialog(Shell parentShell, IStorageService service)
  {
    super(parentShell);
    this.service = service;
  }

  public final IStorageService getService()
  {
    return service;
  }

  @Override
  protected IDialogSettings getPluginSettings()
  {
    return SetupUIPlugin.INSTANCE.getDialogSettings();
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    super.configureShell(newShell);
    newShell.setText(TITLE);
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    String serviceLabel = service.getServiceLabel();
    String shortLabel = serviceLabel;

    String authority = service.getServiceURI().getAuthority();
    if (authority != null && authority.endsWith(".eclipse.org"))
    {
      shortLabel = "Eclipse";
    }

    setTitle(TITLE);
    initializeDialogUnits(parent);

    Composite area = (Composite)super.createDialogArea(parent);

    GridLayout layout = new GridLayout(1, false);
    layout.marginWidth = 48;
    layout.marginHeight = 32;

    Composite composite = new Composite(area, SWT.NONE);
    composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
    composite.setLayout(layout);
    createUI(composite, serviceLabel, shortLabel);

    return area;
  }

  protected abstract void createUI(Composite parent, String serviceLabel, String shortLabel);
}
