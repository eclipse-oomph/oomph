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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Andreas Scharf
 */
public class ImageCheckbox extends ImageHoverButton
{
  private final Image checkedImage;

  private boolean checked;

  public ImageCheckbox(Composite parent, Image defaultImage, Image hoverImage)
  {
    this(parent, defaultImage, hoverImage, hoverImage);
  }

  public ImageCheckbox(Composite parent, Image defaultImage, Image hoverImage, Image checkedImage)
  {
    super(parent, SWT.CHECK, defaultImage, hoverImage);
    this.checkedImage = checkedImage;
  }

  public boolean isChecked()
  {
    return checked;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
    setImage(computeImage());
    redraw();
  }

  @Override
  protected Image computeImage()
  {
    if (!isEnabled())
    {
      return super.computeImage();
    }

    if (isHover() && !isChecked())
    {
      return getHoverImage();
    }

    return isChecked() || isFocusControl() ? getCheckedImage() : getDefaultImage();
  }

  protected Image getCheckedImage()
  {
    return checkedImage;
  }
}
