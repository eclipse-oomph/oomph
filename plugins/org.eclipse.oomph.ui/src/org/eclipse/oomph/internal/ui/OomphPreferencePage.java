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
package org.eclipse.oomph.internal.ui;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Eike Stepper
 */
public class OomphPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
  public static final String ID = "org.eclipse.oomph.ui.preferences.OomphPreferencePage";

  public OomphPreferencePage()
  {
    setDescription("Expand the tree to edit preferences for a specific feature.");
    noDefaultAndApplyButton();
  }

  public void init(IWorkbench workbench)
  {
    // Do nothing
  }

  @Override
  protected Control createContents(Composite parent)
  {
    return new Composite(parent, SWT.NULL);
  }
}
