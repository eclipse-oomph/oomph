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
package org.eclipse.oomph.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Eike Stepper
 */
public abstract class MouseHandler
{
  private final MouseTrackListener mouseTrackListener = new MouseTrackListener()
  {
    public void mouseEnter(MouseEvent e)
    {
      // Do nothing.
    }

    public void mouseHover(MouseEvent e)
    {
      // Do nothing.
    }

    public void mouseExit(MouseEvent e)
    {
      onMouseMove(e.widget, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
  };

  private final MouseMoveListener mouseMoveListener = new MouseMoveListener()
  {
    public void mouseMove(MouseEvent e)
    {
      onMouseMove(e.widget, e.x, e.y);
    }
  };

  private final MouseListener mouseListener = new MouseListener()
  {
    public void mouseDoubleClick(MouseEvent e)
    {
      // Do nothing.
    }

    public void mouseDown(MouseEvent e)
    {
      if (e.button == 1)
      {
        if (e.widget instanceof Control)
        {
          Control control = (Control)e.widget;
          start = getStart(control, e.x, e.y);
        }
      }
    }

    public void mouseUp(MouseEvent e)
    {
      if (start != null)
      {
        if (e.widget instanceof Control)
        {
          Control control = (Control)e.widget;
          afterEnd(control, start, e.x, e.y);
        }

        start = null;
      }
    }
  };

  private Point start;

  public MouseHandler()
  {
  }

  public void hookControl(Control control)
  {
    if (shouldHookControl(control))
    {
      control.addMouseTrackListener(mouseTrackListener);
      control.addMouseMoveListener(mouseMoveListener);
      control.addMouseListener(mouseListener);
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

  protected Point getStart(Control control, int x, int y)
  {
    return new Point(x, y);
  }

  protected void beforeStart(Control control, int x, int y)
  {
  }

  protected void afterStart(Control control, Point start, int x, int y)
  {
  }

  protected void afterEnd(Control control, Point start, int x, int y)
  {
  }

  protected boolean moveShell(Control control, Point start, int x, int y)
  {
    Shell shell = control.getShell();
    Point location = shell.getLocation();
    location.x += x - start.x;
    location.y += y - start.y;
    shell.setLocation(location);
    return true;
  }

  private void onMouseMove(Widget widget, int x, int y)
  {
    if (widget instanceof Control)
    {
      Control control = (Control)widget;

      if (start == null)
      {
        beforeStart(control, x, y);
      }
      else
      {
        afterStart(control, start, x, y);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Move extends MouseHandler
  {
    public Move()
    {
    }

    @Override
    protected void afterStart(Control control, Point start, int x, int y)
    {
      moveShell(control, start, x, y);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class MoveAndResize extends MouseHandler
  {
    private static final int AREA_THICKNESS = 5;

    private static final int CORNER_SIZE = 25;

    private static final int NORTH = 1 << 0;

    private static final int SOUTH = 1 << 1;

    private static final int EAST = 1 << 2;

    private static final int WEST = 1 << 3;

    private static final Cursor[] CURSORS = { null, null, null, null, null, null, null, null, null, null, null };

    private final Rectangle[] areas = { null, null, null, null, null, null, null, null, null, null, null };

    private final Control mainControl;

    private BoundsUpdater boundsUpdater;

    private Rectangle bounds;

    private int index;

    public MoveAndResize(Control control)
    {
      mainControl = control;
      computeAreas(control);
      hookControl(control);
    }

    public final int getIndex()
    {
      return index;
    }

    @Override
    protected void beforeStart(Control control, int x, int y)
    {
      if (control == mainControl)
      {
        index = getIndex(control, x, y);
        if (index >= 0)
        {
          control.setCursor(CURSORS[index]);
          return;
        }
      }

      resetCursor(control);
    }

    @Override
    protected void afterStart(Control control, Point start, int x, int y)
    {
      if (control == mainControl)
      {
        if (index >= 0)
        {
          Rectangle currentBounds = control.getBounds();
          Rectangle newBounds = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);

          x -= start.x;
          y -= start.y;

          if ((index & NORTH) != 0)
          {
            y -= bounds.y - currentBounds.y;
            newBounds.y += y;
            newBounds.height -= y;
          }

          if ((index & SOUTH) != 0)
          {
            newBounds.height += y;
          }

          if ((index & WEST) != 0)
          {
            x -= bounds.x - currentBounds.x;
            newBounds.x += x;
            newBounds.width -= x;
          }

          if ((index & EAST) != 0)
          {
            newBounds.width += x;
          }

          // if (!newBounds.equals(currentBounds))
          {
            synchronized (this)
            {
              if (boundsUpdater == null)
              {
                boundsUpdater = new BoundsUpdater();
              }

              boundsUpdater.updateBounds(newBounds);
            }
          }

          return;
        }
      }

      moveShell(control, start, x, y);
      computeAreas(mainControl);
    }

    @Override
    protected void afterEnd(Control control, Point start, int x, int y)
    {
      if (control == mainControl)
      {
        if (index >= 0)
        {
          resetCursor(control);
          computeAreas(control);
        }

        index = -1;
      }
    }

    private void computeAreas(Control control)
    {
      bounds = control.getBounds();

      areas[NORTH] = new Rectangle(bounds.x + CORNER_SIZE, bounds.y, bounds.width - 2 * CORNER_SIZE, AREA_THICKNESS);
      areas[SOUTH] = new Rectangle(bounds.x + CORNER_SIZE, bounds.y + bounds.height - AREA_THICKNESS, bounds.width - 2 * CORNER_SIZE, AREA_THICKNESS);
      areas[WEST] = new Rectangle(bounds.x, bounds.y + CORNER_SIZE, AREA_THICKNESS, bounds.height - 2 * CORNER_SIZE);
      areas[EAST] = new Rectangle(bounds.x + bounds.width - AREA_THICKNESS, bounds.y + CORNER_SIZE, AREA_THICKNESS, bounds.height - 2 * CORNER_SIZE);
      areas[NORTH | WEST] = new Rectangle(bounds.x, bounds.y, CORNER_SIZE, CORNER_SIZE);
      areas[NORTH | EAST] = new Rectangle(bounds.x + bounds.width - CORNER_SIZE, bounds.y, CORNER_SIZE, CORNER_SIZE);
      areas[SOUTH | WEST] = new Rectangle(bounds.x, bounds.y + bounds.height - CORNER_SIZE, CORNER_SIZE, CORNER_SIZE);
      areas[SOUTH | EAST] = new Rectangle(bounds.x + bounds.width - CORNER_SIZE, bounds.y + bounds.height - CORNER_SIZE, CORNER_SIZE, CORNER_SIZE);
    }

    private int getIndex(Control control, int x, int y)
    {
      x += bounds.x;
      y += bounds.y;

      for (int i = 0; i < areas.length; i++)
      {
        Rectangle area = areas[i];
        if (area != null)
        {
          if (area.contains(x, y))
          {
            return i;
          }
        }
      }

      return -1;
    }

    private void resetCursor(Control control)
    {
      if (control.getCursor() != null)
      {
        control.setCursor(null);
      }
    }

    public static String format(int index)
    {
      StringBuilder builder = new StringBuilder();

      if ((index & NORTH) != 0)
      {
        builder.append(" NORTH");
      }

      if ((index & SOUTH) != 0)
      {
        builder.append(" SOUTH");
      }

      if ((index & WEST) != 0)
      {
        builder.append(" WEST");
      }

      if ((index & EAST) != 0)
      {
        builder.append(" EAST");
      }

      return builder.toString().trim();
    }

    static
    {
      Display display = UIUtil.getDisplay();
      CURSORS[NORTH] = display.getSystemCursor(SWT.CURSOR_SIZEN);
      CURSORS[SOUTH] = display.getSystemCursor(SWT.CURSOR_SIZES);
      CURSORS[WEST] = display.getSystemCursor(SWT.CURSOR_SIZEW);
      CURSORS[EAST] = display.getSystemCursor(SWT.CURSOR_SIZEE);
      CURSORS[NORTH | WEST] = display.getSystemCursor(SWT.CURSOR_SIZENW);
      CURSORS[NORTH | EAST] = display.getSystemCursor(SWT.CURSOR_SIZENE);
      CURSORS[SOUTH | WEST] = display.getSystemCursor(SWT.CURSOR_SIZESW);
      CURSORS[SOUTH | EAST] = display.getSystemCursor(SWT.CURSOR_SIZESE);
    }

    /**
     * @author Eike Stepper
     */
    private final class BoundsUpdater implements Runnable
    {
      private Rectangle newBounds;

      public void updateBounds(Rectangle newBounds)
      {
        boolean schedule = this.newBounds == null;
        this.newBounds = newBounds;

        if (schedule)
        {
          mainControl.getDisplay().asyncExec(this);
        }
      }

      public void run()
      {
        try
        {
          Rectangle bounds;
          synchronized (MoveAndResize.this)
          {
            bounds = newBounds;
            newBounds = null;
          }

          if (bounds != null)
          {
            mainControl.setBounds(bounds);
            mainControl.getDisplay().asyncExec(this);
          }
          else
          {
            boundsUpdater = null;
          }
        }
        catch (Exception ex)
        {
          boundsUpdater = null;
        }
      }
    }
  }
}
