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

import org.eclipse.swt.SWT;
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

  private final long delay;

  private int firstIndex = NONE;

  private int lastIndex = NONE;

  private int index = NONE;

  private boolean clearBackground;

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
        Image image = new Image(getDisplay(), width, height);
        GC gc = new GC(image);
        gc.drawImage(sprite, x * width, y * height, width, height, 0, 0, width, height);
        gc.dispose();

        images[x + countX * y] = image;
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
    this.firstIndex = firstIndex;
    this.lastIndex = lastIndex;
    index = firstIndex;

    clearBackground = (getStyle() & SWT.NO_BACKGROUND) != 0;
    getDisplay().asyncExec(this);
  }

  public final synchronized void stop()
  {
    firstIndex = NONE;
    lastIndex = NONE;
    index = NONE;

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
      if (index != NONE && !isDisposed())
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
    if (index != NONE)
    {
      Rectangle clientArea = getClientArea();
      int x = (clientArea.width - width) / 2;
      int y = (clientArea.height - height) / 2;

      GC gc = e.gc;
      if (clearBackground)
      {
        gc.fillRectangle(clientArea);
        clearBackground = false;
      }

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
