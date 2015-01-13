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

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Eike Stepper
 */
public class ShellMove implements MouseTrackListener, MouseMoveListener, MouseListener
{
  private Point start;

  public void mouseDoubleClick(MouseEvent e)
  {
    // Do nothing.
  }

  public void mouseDown(MouseEvent e)
  {
    if (e.button == 1)
    {
      start = new Point(e.x, e.y);
    }
  }

  public void mouseUp(MouseEvent e)
  {
    if (start != null)
    {
      start = null;
    }
  }

  public void mouseMove(MouseEvent e)
  {
    onMouseMove(e.widget, e.x, e.y);
  }

  public void mouseEnter(MouseEvent e)
  {
    // Do nothing.
  }

  public void mouseExit(MouseEvent e)
  {
    onMouseMove(e.widget, Integer.MIN_VALUE, Integer.MIN_VALUE);
  }

  public void mouseHover(MouseEvent e)
  {
    // Do nothing.
  }

  protected void onMouseMove(Widget widget, int x, int y)
  {
    if (start != null)
    {
      if (widget instanceof Control)
      {
        Control control = (Control)widget;

        Shell shell = control.getShell();
        Point location = shell.getLocation();
        location.x += x - start.x;
        location.y += y - start.y;
        shell.setLocation(location);
      }
    }
  }

  public void hookControl(Control control)
  {
    if (shouldHookControl(control))
    {
      control.addMouseTrackListener(this);
      control.addMouseMoveListener(this);
      control.addMouseListener(this);
    }

    if (control instanceof Composite)
    {
      for (Control child : ((Composite)control).getChildren())
      {
        hookControl(child);
      }
    }
  }

  protected boolean shouldHookControl(Control control)
  {
    Class<? extends Control> c = control.getClass();
    return c == Composite.class || c == StackComposite.class || control instanceof Shell || c == Label.class || c == Link.class;
  }
}
