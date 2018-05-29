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

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * @author Ed Merks
 */
public class LabelDecorator extends BaseLabelProvider implements ILabelDecorator
{
  public Image decorateImage(Image image, Object element)
  {
    return image;
  }

  public String decorateText(String text, Object element)
  {
    return text;
  }

  public Font decorateFont(Font font, Object element)
  {
    return font;
  }

  public Color decorateForeground(Color foreground, Object element)
  {
    return foreground;
  }

  public Color decorateBackground(Color background, Object element)
  {
    return background;
  }
}
