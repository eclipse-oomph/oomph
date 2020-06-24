/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.ui;

import org.eclipse.oomph.ui.OomphDialog;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class TargletContainerDialog extends OomphDialog
{
  public static final String TITLE = Messages.TargletContainerDialog_title;

  private final String targletContainerID;

  private TargletContainerComposite composite;

  public TargletContainerDialog(Shell parentShell, String targletContainerID)
  {
    super(parentShell, TITLE, 800, 600, TargletsUIPlugin.INSTANCE, false);
    this.targletContainerID = targletContainerID;
    setShellStyle(SWT.TITLE | SWT.MAX | SWT.RESIZE | SWT.BORDER | SWT.APPLICATION_MODAL);
  }

  public final TargletContainerComposite getComposite()
  {
    return composite;
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return NLS.bind(Messages.TargletContainerDialog_defaultMessage, targletContainerID);
  }

  @Override
  protected String getImagePath()
  {
    return "full/wizban/NewTarglet.png"; //$NON-NLS-1$
  }

  @Override
  protected int getContainerMargin()
  {
    return 10;
  }

  @Override
  protected void createUI(Composite parent)
  {
    getShell().setImage(TargletsUIPlugin.INSTANCE.getSWTImage("full/obj16/TargletModelFile")); //$NON-NLS-1$

    composite = new TargletContainerComposite(parent, SWT.NONE, targletContainerID);
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));
  }
}
