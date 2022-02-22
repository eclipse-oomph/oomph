/*
 * Copyright (c) 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Eike Stepper
 */
public abstract class AbstractPreferencePage extends PreferencePage implements IWorkbenchPreferencePage
{
  public AbstractPreferencePage()
  {
  }

  @Override
  public void init(IWorkbench workbench)
  {
    // Do nothing
  }

  @Override
  protected final Control createContents(Composite parent)
  {
    Color widgetBackground = parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
    parent.setBackground(widgetBackground);

    return doCreateContents(parent);
  }

  protected abstract Control doCreateContents(Composite parent);
}
