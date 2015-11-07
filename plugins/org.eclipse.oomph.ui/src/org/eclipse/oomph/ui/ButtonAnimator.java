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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author Eike Stepper
 */
public abstract class ButtonAnimator implements Runnable
{
  private final OomphUIPlugin plugin;

  private final ToolItem button;

  private final String imagePrefix;

  protected final int additionalImages;

  private final boolean rotate;

  private int index;

  private boolean backward;

  protected Image[] images;

  public ButtonAnimator(OomphUIPlugin plugin, ToolItem button, String imagePrefix, int additionalImages)
  {
    this(plugin, button, imagePrefix, additionalImages, false);
  }

  public ButtonAnimator(OomphUIPlugin plugin, ToolItem button, String imagePrefix, int additionalImages, boolean rotate)
  {
    this.plugin = plugin;
    this.button = button;
    this.imagePrefix = imagePrefix;
    this.additionalImages = additionalImages;
    this.rotate = rotate;
  }

  public void run()
  {
    Shell shell = getShell();
    if (!button.isDisposed() && shell != null && !shell.isDisposed())
    {
      try
      {
        if (images == null)
        {
          initImages();
        }

        if (shouldAnimate())
        {
          button.setImage(images[Math.max(0, index)]);

          if (backward)
          {
            if (--index == -getQuietCycles())
            {
              backward = false;
            }
          }
          else
          {
            if (++index == images.length)
            {
              if (rotate)
              {
                index = -getQuietCycles();
              }
              else
              {
                index = images.length - 2;
                backward = true;
              }
            }
          }

          shell.getDisplay().timerExec(rotate ? 80 : 40, this);
        }
        else
        {
          index = 0;
          backward = false;
          button.setImage(images[index]);
        }
      }
      catch (SWTException ex)
      {
        if (!button.isDisposed())
        {
          throw ex;
        }
      }
    }
  }

  protected int getQuietCycles()
  {
    if (rotate)
    {
      return 8;
    }

    return 10;
  }

  public abstract Shell getShell();

  public final boolean shouldAnimate()
  {
    return button != null && !button.isDisposed() && button.isEnabled() && doAnimate();
  }

  protected abstract boolean doAnimate();

  private void initImages()
  {
    images = new Image[1 + additionalImages];
    images[0] = button.getImage();

    String prefix = imagePrefix;
    String suffix = "";
    if (imagePrefix.endsWith(".png"))
    {
      prefix = prefix.substring(0, prefix.length() - 4);
      suffix = ".png";
    }

    for (int i = 0; i < images.length - 1; i++)
    {
      images[i + 1] = plugin.getSWTImage(prefix + i + suffix);
    }
  }
}
