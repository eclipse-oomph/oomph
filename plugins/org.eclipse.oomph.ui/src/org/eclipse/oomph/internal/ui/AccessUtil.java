/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ed Merks
 */
public final class AccessUtil
{
  private static final String DATA_KEY = "oomph.access.key"; //$NON-NLS-1$

  public static void setKey(Widget widget, Object key)
  {
    widget.setData(DATA_KEY, key);
    widget.setData("org.eclipse.swtbot.widget.key", key); //$NON-NLS-1$
  }

  public static Image capture(Control control)
  {
    if (control instanceof Shell)
    {
      Display display = control.getDisplay();
      Shell shell = (Shell)control;
      Rectangle bounds = shell.getBounds();
      bounds.x -= 12;
      bounds.y -= 11;
      bounds.width += 28;
      bounds.height += 28;
      return captureDrawable(display, display, bounds);
    }
    else if (control instanceof Button)
    {
      Point size = control.getSize();
      Rectangle bounds = (control.getStyle() & SWT.CHECK) != 0 ? new Rectangle(0, 2, size.x, size.y - 4) : new Rectangle(0, 1, size.x, size.y - 2);
      return capture(control, bounds);
    }
    else
    {
      Point size = control.getSize();
      Rectangle bounds = new Rectangle(0, 0, size.x, size.y);
      return capture(control, bounds);
    }
  }

  public static Image capture(Display display, Rectangle bounds)
  {
    return captureDrawable(display, display, bounds);
  }

  public static Image capture(Control control, Rectangle bounds)
  {
    return captureDrawable(control.getDisplay(), control, bounds);
  }

  private static Image captureDrawable(Device device, Drawable drawable, Rectangle bounds)
  {
    GC gc = new GC(drawable);
    final Image image = new Image(device, bounds.width, bounds.height);
    gc.copyArea(image, bounds.x, bounds.y);
    gc.dispose();
    return image;
  }

  public static void save(File file, Control control) throws IOException
  {
    Point size = control.getSize();
    save(file, control, new Rectangle(0, 0, size.x, size.y));
  }

  public static void save(File file, Control control, Rectangle bounds) throws IOException
  {
    OutputStream out = null;
    Image image = null;
    try
    {
      out = new FileOutputStream(file);
      image = capture(control, bounds);
      save(out, image, getImageType(URI.createURI(file.getName()).fileExtension()), image.getBounds());
    }
    finally
    {
      IOUtil.close(out);
      if (image != null)
      {
        image.dispose();
      }
    }
  }

  public static void save(OutputStream out, final Image image, int type) throws IOException
  {
    final ImageData imageData = image.getImageData();
    final Rectangle bounds = getBounds(imageData);
    ImageData effectiveImageData = imageData;
    if (bounds.x != 0 || bounds.y != 0 || bounds.width != imageData.width || bounds.height != imageData.height)
    {
      effectiveImageData = new WorkUnit<ImageData, IOException>(IOException.class)
      {
        @Override
        protected ImageData doExecute() throws IOException
        {
          Image clippedImage = new Image(image.getDevice(), bounds.width, bounds.height);
          GC gc = new GC(clippedImage);
          gc.setBackground(image.getDevice().getSystemColor(SWT.COLOR_WHITE));
          gc.setForeground(image.getDevice().getSystemColor(SWT.COLOR_WHITE));
          gc.drawRectangle(0, 0, bounds.width, bounds.height);
          gc.drawImage(image, bounds.x, bounds.y, bounds.width, bounds.height, 0, 0, bounds.width, bounds.height);
          gc.dispose();
          ImageData result = clippedImage.getImageData();
          clippedImage.dispose();
          return result;
        }
      }.execute();
    }

    save(out, effectiveImageData, type, bounds);
  }

  public static void save(OutputStream out, final Image image, int type, Rectangle bounds) throws IOException
  {
    save(out, image.getImageData(), type, bounds);
  }

  public static void save(OutputStream out, ImageData imageData, int type) throws IOException
  {
    save(out, imageData, type, getBounds(imageData));
  }

  public static void save(OutputStream out, ImageData imageData, int type, Rectangle bounds) throws IOException
  {
    ImageLoader imageLoader = new ImageLoader();
    imageLoader.data = new ImageData[] { imageData };
    imageLoader.save(out, type);
  }

  public static int getImageType(String fileExtension)
  {
    if ("jpg".equalsIgnoreCase(fileExtension) || "jpeg".equalsIgnoreCase(fileExtension)) //$NON-NLS-1$ //$NON-NLS-2$
    {
      return SWT.IMAGE_JPEG;
    }

    if ("ico".equalsIgnoreCase(fileExtension)) //$NON-NLS-1$
    {
      return SWT.IMAGE_ICO;
    }

    if ("bmp".equalsIgnoreCase(fileExtension)) //$NON-NLS-1$
    {
      return SWT.IMAGE_BMP;
    }

    if ("gif".equalsIgnoreCase(fileExtension)) //$NON-NLS-1$
    {
      return SWT.IMAGE_GIF;
    }

    if ("tiff".equalsIgnoreCase(fileExtension)) //$NON-NLS-1$
    {
      return SWT.IMAGE_TIFF;
    }

    return SWT.IMAGE_PNG;
  }

  private static Rectangle getBounds(ImageData imageData)
  {
    int left = imageData.width / 2;
    int right = imageData.width - left;
    int top = imageData.height / 2;
    int bottom = imageData.height - top;
    int type = imageData.getTransparencyType();
    if (type == SWT.TRANSPARENCY_ALPHA)
    {
      for (int i = 0; i < imageData.height; ++i)
      {
        for (int j = 0; j < imageData.width; ++j)
        {
          int alpha = imageData.getAlpha(j, i);
          boolean transparent = alpha < 10 ? true : false;
          if (!transparent)
          {
            left = Math.min(left, j);
            right = Math.max(right, j);
            top = Math.min(top, i);
            bottom = Math.max(bottom, i);
          }
          System.err.print(transparent ? "0" : "1"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        System.err.println();
      }
    }
    else if (type == SWT.TRANSPARENCY_MASK || type == SWT.TRANSPARENCY_PIXEL)
    {
      ImageData transparencyMask = imageData.getTransparencyMask();
      for (int i = 0; i < transparencyMask.height; ++i)
      {
        for (int j = 0; j < transparencyMask.width; ++j)
        {
          boolean transparent = transparencyMask.getPixel(j, i) == 0 ? true : false;
          if (!transparent)
          {
            left = Math.min(left, j);
            right = Math.max(right, j);
            top = Math.min(top, i);
            bottom = Math.max(bottom, i);
          }
          System.err.print(transparent ? "0" : "1"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        System.err.println();
      }
    }
    else
    {
      return new Rectangle(0, 0, imageData.width, imageData.height);
    }

    return new Rectangle(left, top, right - left + 1, bottom - top + 1);
  }

  public static void busyWait(int milliseconds)
  {
    busyWait(milliseconds, null);
  }

  public static void busyWait(final int milliseconds, final Runnable runnable)
  {
    final Display display = UIUtil.getDisplay();
    final AtomicBoolean done = new AtomicBoolean();
    new Thread()
    {
      @Override
      public void run()
      {
        try
        {
          // Block on the runnable, if there is one.
          if (runnable != null)
          {
            runnable.run();
          }

          // Wait 2 more seconds.
          UIUtil.asyncExec(new Runnable()
          {
            public void run()
            {
              UIUtil.timerExec(milliseconds, new Runnable()
              {
                public void run()
                {
                  // Ensure that the sleeping display loop wakes up.
                  done.set(true);
                }
              });
            }
          });
        }
        catch (Exception ex)
        {
          // Ignore
        }
      }
    }.start();

    while (!display.isDisposed() && !done.get())
    {
      if (!display.readAndDispatch())
      {
        display.sleep();
      }
    }
  }

  public static Object getKey(Widget widget)
  {
    return widget.getData(DATA_KEY);
  }

  @SuppressWarnings("unchecked")
  public static <T extends Widget> T getWidget(Widget widget, Object key)
  {
    Object data = getKey(widget);
    if (key.equals(data))
    {
      return (T)widget;
    }

    if (widget instanceof Composite)
    {
      Composite composite = (Composite)widget;
      for (Control control : composite.getChildren())
      {
        T result = getWidget(control, key);
        if (result != null)
        {
          return result;
        }
      }
    }

    if (widget instanceof ToolBar)
    {
      ToolBar toolBar = (ToolBar)widget;
      for (ToolItem toolItem : toolBar.getItems())
      {
        T result = getWidget(toolItem, key);
        if (result != null)
        {
          return result;
        }
      }
    }

    return null;
  }

  private AccessUtil()
  {
  }
}
