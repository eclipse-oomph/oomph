/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.launches;

import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jdt.junit.model.ITestElementContainer;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.swt.graphics.Image;

import java.text.NumberFormat;

/**
 * @author Eike Stepper
 */
public class TestElementLabelDecorator extends BaseLabelProvider implements ILabelDecorator
{
  private final NumberFormat timeFormat;

  public TestElementLabelDecorator()
  {
    timeFormat = NumberFormat.getNumberInstance();
    timeFormat.setGroupingUsed(true);
    timeFormat.setMinimumFractionDigits(3);
    timeFormat.setMaximumFractionDigits(3);
    timeFormat.setMinimumIntegerDigits(1);
  }

  public Image decorateImage(Image image, Object element)
  {
    return null;
  }

  public String decorateText(String text, Object element)
  {
    if (element instanceof ITestElementContainer)
    {
      ITestElementContainer container = (ITestElementContainer)element;

      double time = container.getElapsedTimeInSeconds();
      if (!Double.isNaN(time))
      {
        int count = countAllChildren(container);
        if (count > 1)
        {
          double average = time / count;
          return text + " = " + count + " x " + timeFormat.format(average) + " s";
        }
      }
    }

    return null;
  }

  private int countAllChildren(ITestElementContainer container)
  {
    int count = 0;
    for (ITestElement child : container.getChildren())
    {
      if (child instanceof ITestElementContainer)
      {
        count += countAllChildren((ITestElementContainer)child);
      }
      else
      {
        ++count;
      }
    }

    return count;
  }
}
