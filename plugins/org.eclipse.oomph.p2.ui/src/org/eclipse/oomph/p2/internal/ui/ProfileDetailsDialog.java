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
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.ui.OomphDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class ProfileDetailsDialog extends OomphDialog
{
  public static final String TITLE = Messages.ProfileDetailsDialog_title;

  private final Profile profile;

  private ProfileDetailsComposite composite;

  public ProfileDetailsDialog(Shell parentShell, Profile profile)
  {
    super(parentShell, TITLE, 800, 600, P2UIPlugin.INSTANCE, false);
    this.profile = profile;
    setShellStyle(SWT.TITLE | SWT.MAX | SWT.RESIZE | SWT.BORDER | SWT.APPLICATION_MODAL);
  }

  public final ProfileDetailsComposite getComposite()
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
    return Messages.ProfileDetailsDialog_defaultMessage;
  }

  @Override
  protected String getImagePath()
  {
    return "wizban/ProfileDetails.png"; //$NON-NLS-1$
  }

  @Override
  protected int getContainerMargin()
  {
    return 10;
  }

  @Override
  protected void createUI(Composite parent)
  {
    getShell().setImage(P2UIPlugin.INSTANCE.getSWTImage("full/obj16/ProfileDefinition")); //$NON-NLS-1$

    composite = new ProfileDetailsComposite(parent, SWT.NONE, profile);
    composite.setLayoutData(new GridData(GridData.FILL_BOTH));
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, true);
  }
}
