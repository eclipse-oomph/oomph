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
package org.eclipse.oomph.setup.ui.questionaire;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class AnimatedCanvas extends Canvas
{
  private static final int DEFAULT_TIMER_INTERVAL = 10;

  private final Runnable runnable = new Runnable()
  {
    public void run()
    {
      doRun();
    }
  };

  private final List<Animator> animators = new ArrayList<Animator>();

  private int timerInterval;

  private GC bufferGC;

  private Image buffer;

  private Point shellMoveStart;

  private boolean hasFocus = true;

  public AnimatedCanvas(Composite parent, int style)
  {
    this(parent, style, DEFAULT_TIMER_INTERVAL);
  }

  public AnimatedCanvas(Composite parent, int style, int timerInterval)
  {
    super(parent, style | SWT.DOUBLE_BUFFERED);

    Display display = getDisplay();
    setBackground(display.getSystemColor(SWT.COLOR_WHITE));

    addPaintListener(new PaintListener()
    {
      public void paintControl(PaintEvent event)
      {
        GC canvasGC = event.gc;
        doPaint(canvasGC);
      }
    });

    addMouseTrackListener(new MouseTrackAdapter()
    {
      @Override
      public void mouseExit(MouseEvent e)
      {
        onMouseMove(Integer.MIN_VALUE, Integer.MIN_VALUE);
      }
    });

    addMouseMoveListener(new MouseMoveListener()
    {
      public void mouseMove(MouseEvent e)
      {
        onMouseMove(e.x, e.y);
      }
    });

    addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseDown(MouseEvent e)
      {
        if (e.button == 1)
        {
          onMouseDown(e.x, e.y);
        }
      }

      @Override
      public void mouseUp(MouseEvent e)
      {
        if (shellMoveStart != null)
        {
          shellMoveStart = null;
        }
      }
    });

    display.timerExec(timerInterval, runnable);
  }

  public final Animator[] getAnimators()
  {
    synchronized (animators)
    {
      return animators.toArray(new Animator[animators.size()]);
    }
  }

  public final void addAnimator(Animator animator)
  {
    synchronized (animators)
    {
      animator.setCanvas(this);
      animators.add(animator);
    }
  }

  public final void removeAnimator(Animator animator)
  {
    synchronized (animators)
    {
      animators.remove(animator);
      animator.setCanvas(null);
    }
  }

  public final int getTimerInterval()
  {
    return timerInterval;
  }

  public final void setTimerInterval(int timerInterval)
  {
    this.timerInterval = timerInterval;
  }

  public void setFocus(boolean hasFocus)
  {
    this.hasFocus = hasFocus;

    try
    {
      GC canvasGC = new GC(this);
      doPaint(canvasGC);
    }
    catch (Exception ex)
    {
      // Ignore.
    }
  }

  @Override
  public synchronized void dispose()
  {
    getDisplay().timerExec(-1, runnable);

    for (Animator animator : getAnimators())
    {
      animator.dispose();
    }

    if (buffer != null)
    {
      bufferGC.dispose();
      buffer.dispose();
    }

    super.dispose();
  }

  private void onMouseMove(int x, int y)
  {
    if (shellMoveStart != null)
    {
      Shell shell = getShell();
      Point location = shell.getLocation();
      location.x += x - shellMoveStart.x;
      location.y += y - shellMoveStart.y;
      shell.setLocation(location);
    }

    Animator[] animators = getAnimators();
    for (int i = animators.length - 1; i >= 0; --i)
    {
      Animator animator = animators[i];
      if (animator.onMouseMove(x, y))
      {
        return;
      }
    }
  }

  private void onMouseDown(int x, int y)
  {
    Animator[] animators = getAnimators();
    for (int i = animators.length - 1; i >= 0; --i)
    {
      Animator animator = animators[i];
      if (animator.onMouseDown(x, y))
      {
        return;
      }
    }

    shellMoveStart = new Point(x, y);
  }

  private synchronized void doRun()
  {
    if (isDisposed())
    {
      return;
    }

    boolean needsRedraw = false;
    for (Animator animator : getAnimators())
    {
      if (animator.advance())
      {
        needsRedraw = true;
      }
    }

    if (needsRedraw)
    {
      redraw();
    }

    getDisplay().timerExec(timerInterval, runnable);
  }

  private void doPaint(GC canvasGC)
  {
    if (buffer == null)
    {
      buffer = new Image(getDisplay(), getBounds());
      bufferGC = new GC(buffer);
    }

    bufferGC.setBackground(canvasGC.getBackground());
    bufferGC.fillRectangle(buffer.getBounds());

    for (Animator animator : getAnimators())
    {
      animator.paint(bufferGC, buffer);
    }

    canvasGC.drawImage(buffer, 0, 0);

    if (!hasFocus)
    {
      canvasGC.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
      canvasGC.setAlpha(200);
      canvasGC.fillRectangle(getBounds());
    }
  }

  /**
   * @author Eike Stepper
   */
  public static abstract class Animator
  {
    private final Display display;

    private AnimatedCanvas canvas;

    private int width;

    private int height;

    public Animator(Display display)
    {
      this.display = display;
    }

    public final Display getDisplay()
    {
      return display;
    }

    public final AnimatedCanvas getCanvas()
    {
      return canvas;
    }

    public final GC getBufferGC()
    {
      return canvas.bufferGC;
    }

    public final int getWidth()
    {
      return width;
    }

    public final int getHeight()
    {
      return height;
    }

    public void dispose()
    {
    }

    private void setCanvas(AnimatedCanvas canvas)
    {
      this.canvas = canvas;
    }

    protected final void setSize(int width, int height)
    {
      this.width = width;
      this.height = height;
    }

    protected boolean onMouseMove(int x, int y)
    {
      return false;
    }

    protected boolean onMouseDown(int x, int y)
    {
      return false;
    }

    protected abstract boolean advance();

    protected abstract void paint(GC gc, Image buffer);
  }
}
