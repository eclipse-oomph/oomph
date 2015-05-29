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

import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eike Stepper
 */
public class SpriteAnimator extends Canvas implements Runnable, PaintListener
{
  private static final int NONE = -1;

  private final Image[] images;

  private final int width;

  private final int height;

  private long delay;

  private int firstIndex = NONE;

  private int lastIndex = NONE;

  private int index = NONE;

  private boolean running;

  public SpriteAnimator(Composite parent, int style, Image sprite, int width, int height, long delay)
  {
    super(parent, style);

    this.width = width;
    this.height = height;
    this.delay = delay;

    ImageData imageData = sprite.getImageData();
    int countX = imageData.width / width;
    int countY = imageData.height / height;

    images = new Image[countX * countY];
    for (int x = 0; x < countX; x++)
    {
      for (int y = 0; y < countY; y++)
      {
        images[x + countX * y] = UIUtil.extractSprite(sprite, x * width, y * width, width, height);
      }
    }

    addPaintListener(this);
  }

  public final int getWidth()
  {
    return width;
  }

  public final int getHeight()
  {
    return height;
  }

  public final void setDelay(long delay)
  {
    this.delay = delay;
  }

  public final long getDelay()
  {
    return delay;
  }

  public final Image[] getImages()
  {
    return images;
  }

  public final void start()
  {
    start(0, images.length - 1);
  }

  public final synchronized void start(int firstIndex, int lastIndex)
  {
    if (running && this.firstIndex == firstIndex && this.lastIndex == lastIndex)
    {
      return;
    }

    this.firstIndex = firstIndex;
    this.lastIndex = lastIndex;
    index = firstIndex;

    running = true;

    getDisplay().asyncExec(this);
  }

  public final synchronized void stop()
  {
    running = false;

    getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        try
        {
          if (!isDisposed())
          {
            redraw();
          }
        }
        catch (SWTException ex)
        {
          if (!isDisposed())
          {
            throw ex;
          }
        }
      }
    });
  }

  public final synchronized void run()
  {
    try
    {
      if (running && !isDisposed())
      {
        redraw();

        if (++index > lastIndex)
        {
          index = firstIndex;
        }

        getDisplay().timerExec((int)delay, this);
      }
    }
    catch (SWTException ex)
    {
      if (!isDisposed())
      {
        throw ex;
      }
    }
  }

  public final void paintControl(PaintEvent e)
  {
    int index = this.index;
    if (index != NONE)
    {
      Rectangle clientArea = getClientArea();
      int x = (clientArea.width - width) / 2;
      int y = (clientArea.height - height) / 2;

      GC gc = e.gc;
      gc.drawImage(images[index], x, y);
    }
  }

  @Override
  public void dispose()
  {
    for (Image image : images)
    {
      image.dispose();
    }

    super.dispose();
  }
}
