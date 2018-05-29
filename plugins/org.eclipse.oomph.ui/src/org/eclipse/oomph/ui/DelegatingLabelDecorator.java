/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * @author Eike Stepper
 */
public class DelegatingLabelDecorator extends LabelDecorator
{
  private LabelDecorator labelDecorator;

  public DelegatingLabelDecorator()
  {
  }

  public DelegatingLabelDecorator(LabelDecorator labelDecorator)
  {
    this.labelDecorator = labelDecorator;
  }

  public void setLabelDecorator(LabelDecorator labelDecorator)
  {
    if (this.labelDecorator != null)
    {
      this.labelDecorator.dispose();
    }

    this.labelDecorator = labelDecorator;
  }

  @Override
  public Image decorateImage(Image image, Object element)
  {
    return labelDecorator == null ? super.decorateImage(image, element) : labelDecorator.decorateImage(image, element);
  }

  @Override
  public String decorateText(String text, Object element)
  {
    return labelDecorator == null ? super.decorateText(text, element) : labelDecorator.decorateText(text, element);
  }

  @Override
  public Font decorateFont(Font font, Object element)
  {
    return labelDecorator == null ? super.decorateFont(font, element) : labelDecorator.decorateFont(font, element);
  }

  @Override
  public void dispose()
  {
    if (labelDecorator != null)
    {
      labelDecorator.dispose();
    }

    super.dispose();
  }

  @Override
  public Color decorateForeground(Color foreground, Object element)
  {
    return labelDecorator == null ? super.decorateForeground(foreground, element) : labelDecorator.decorateForeground(foreground, element);
  }

  @Override
  public Color decorateBackground(Color background, Object element)
  {
    return labelDecorator == null ? super.decorateBackground(background, element) : labelDecorator.decorateBackground(background, element);
  }

  @Override
  public boolean isLabelProperty(Object element, String property)
  {
    return labelDecorator == null ? super.isLabelProperty(element, property) : labelDecorator.isLabelProperty(element, property);
  }
}
