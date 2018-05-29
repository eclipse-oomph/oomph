/*
 * Copyright (c) 2014, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.internal.ui.AbstractPreferencePage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Eike Stepper
 */
public class AgentManagerPreferencePage extends AbstractPreferencePage
{
  public AgentManagerPreferencePage()
  {
    noDefaultAndApplyButton();
  }

  @Override
  protected Control doCreateContents(Composite parent)
  {
    return new AgentManagerComposite(parent, SWT.NONE, null);
  }
}
