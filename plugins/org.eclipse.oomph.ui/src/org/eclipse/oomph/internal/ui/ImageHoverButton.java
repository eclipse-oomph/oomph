/*
 * Copyright (c) 2015 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yatta Solutions - [466264] initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

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

    setImage(computeImage());
  }

  protected Image computeImage()
  {
    if (!isEnabled())
    {
      return getDisabledImage() != null ? getDisabledImage() : getDefaultImage();
    }

    return isHover() ? getHoverImage() : getDefaultImage();
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
    setImage(computeImage());
  }

  public void setDefaultImage(Image defaultImage)
  {
    if (this.defaultImage != defaultImage)
    {
      this.defaultImage = defaultImage;
      setImage(computeImage());
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
      setImage(computeImage());
    }
  }

  public Image getHoverImage()
  {
    return hoverImage;
  }

  @Override
  protected void onHover()
  {
    setImage(computeImage());
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
      setImage(computeImage());
    }
  }
}
