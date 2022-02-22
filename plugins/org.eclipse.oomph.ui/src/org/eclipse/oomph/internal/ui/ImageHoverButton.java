/*
 * Copyright (c) 2015 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Yatta Solutions - [466264] initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * @author Andreas Scharf
 */
public class ImageHoverButton extends FlatButton
{
  private Image defaultImage;

  private Image hoverImage;

  private Image disabledImage;

  public ImageHoverButton(Composite parent, int buttonStyle)
  {
    this(parent, buttonStyle, null, null);
  }

  public ImageHoverButton(Composite parent, int buttonStyle, Image image, Image hoverImage)
  {
    this(parent, buttonStyle, image, hoverImage, null);
  }

  public ImageHoverButton(Composite parent, int buttonStyle, Image image, Image hoverImage, Image disabledImage)
  {
    super(parent, buttonStyle);
    defaultImage = image;
    this.hoverImage = hoverImage;
    this.disabledImage = disabledImage;

    setDisabledBackgroundColor(null);
    updateImage();
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
    updateImage();
  }

  public void setDefaultImage(Image defaultImage)
  {
    if (this.defaultImage != defaultImage)
    {
      this.defaultImage = defaultImage;
      updateImage();
    }
  }

  public Image getDefaultImage()
  {
    return defaultImage;
  }

  public void setHoverImage(Image hoverImage)
  {
    if (this.hoverImage != hoverImage)
    {
      this.hoverImage = hoverImage;
      updateImage();
    }
  }

  public Image getHoverImage()
  {
    return hoverImage;
  }

  public Image getDisabledImage()
  {
    return disabledImage;
  }

  public void setDisabledImage(Image disabledImage)
  {
    if (this.disabledImage != disabledImage)
    {
      this.disabledImage = disabledImage;
      updateImage();
    }
  }

  @Override
  protected void onHover()
  {
    updateImage();
  }

  @Override
  protected void onFocusIn(Event event)
  {
    updateImage();
  }

  @Override
  protected void onFocusOut(Event event)
  {
    updateImage();
  }

  @Override
  protected void drawFocusState(GC gc, int x, int y, int width, int height)
  {
    // Don't draw any borders
  }

  protected Image computeImage()
  {
    if (!isEnabled())
    {
      return getDisabledImage() != null ? getDisabledImage() : getDefaultImage();
    }

    return isHover() || isFocusControl() ? getHoverImage() : getDefaultImage();
  }

  protected final void updateImage()
  {
    Image image = computeImage();
    setImage(image);
  }
}
