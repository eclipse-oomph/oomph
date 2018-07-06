/*
 * Copyright (c) 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Eike Stepper
 */
public class StackComposite extends Composite
{
  private final StackLayout layout = new StackLayout();

  public StackComposite(Composite parent, int style)
  {
    super(parent, style);
    setLayout(layout);
  }

  public Control getTopControl()
  {
    return layout.topControl;
  }

  public void setTopControl(Control topControl)
  {
    if (layout.topControl != topControl)
    {
      layout.topControl = topControl;

      try
      {
        ReflectUtil.invokeMethod(getClass().getMethod("requestLayout"), this);
      }
      catch (NoSuchMethodException ex)
      {
        // If this version of SWT doesn't have the request layout method, use this instead.
        layout();
      }
    }
  }
}
