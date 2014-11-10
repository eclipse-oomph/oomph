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
package org.eclipse.oomph.ui;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;

/**
 * @author Eike Stepper
 */
public class FirstChildLayout extends Layout
{
  @Override
  protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache)
  {
    Control children[] = composite.getChildren();
    if (children.length != 0)
    {
      return children[0].computeSize(wHint, hHint, flushCache);
    }

    return new Point(0, 0);
  }

  @Override
  protected boolean flushCache(Control control)
  {
    return true;
  }

  @Override
  protected void layout(Composite composite, boolean flushCache)
  {
    Rectangle rect = composite.getClientArea();

    Control children[] = composite.getChildren();
    for (int i = 0; i < children.length; i++)
    {
      children[i].setBounds(rect);
      children[i].setVisible(i == 0);
    }
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName();
  }
}
