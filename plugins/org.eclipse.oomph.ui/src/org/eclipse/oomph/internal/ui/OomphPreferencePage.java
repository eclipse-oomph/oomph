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
package org.eclipse.oomph.internal.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Eike Stepper
 */
public class OomphPreferencePage extends AbstractPreferencePage
{
  public static final String ID = "org.eclipse.oomph.ui.preferences.OomphPreferencePage";

  public OomphPreferencePage()
  {
    setDescription("Expand the tree to edit preferences for a specific feature.");
    noDefaultAndApplyButton();
  }

  @Override
  protected Control doCreateContents(Composite parent)
  {
    return new Composite(parent, SWT.NULL);
  }
}
