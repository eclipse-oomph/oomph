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

  private final int additionalImages;

  private Image[] images;

  private int index;

  private boolean backward;

  public ButtonAnimator(OomphUIPlugin plugin, ToolItem button, String imagePrefix, int additionalImages)
  {
    this.plugin = plugin;
    this.button = button;
    this.imagePrefix = imagePrefix;
    this.additionalImages = additionalImages;
  }

  public void run()
  {
    Shell shell = getShell();
    if (shell != null && !shell.isDisposed())
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
            if (--index == -10)
            {
              backward = false;
            }
          }
          else
          {
            if (++index == images.length)
            {
              index = images.length - 2;
              backward = true;
            }
          }

          shell.getDisplay().timerExec(40, this);
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

    for (int i = 0; i < images.length - 1; i++)
    {
      images[i + 1] = plugin.getSWTImage(imagePrefix + i);
    }
  }
}
