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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author Eike Stepper
 */
public class SecondaryToolItem extends ToolItem
{
  private Image grayImage;

  public SecondaryToolItem(ToolBar parent, int style, Image image)
  {
    super(parent, style);
    init(image);
  }

  public SecondaryToolItem(ToolBar parent, int style, int index, Image image)
  {
    super(parent, style, index);
    init(image);
  }

  @Override
  public void dispose()
  {
    grayImage.dispose();
    super.dispose();
  }

  @Override
  protected void checkSubclass()
  {
    // Do nothing.
  }

  public void init(Image image)
  {
    if (grayImage != null)
    {
      grayImage.dispose();
    }

    grayImage = new Image(getDisplay(), image, SWT.IMAGE_GRAY);
    setImage(grayImage);
    setHotImage(image);
  }
}