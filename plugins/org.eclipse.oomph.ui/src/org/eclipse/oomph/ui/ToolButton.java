/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author Eike Stepper
 */
public class ToolButton extends ToolBar
{
  private final ToolItem toolItem;

  public ToolButton(Composite parent, int style, Image image, boolean secondary)
  {
    super(parent, SWT.FLAT);

    if (secondary)
    {
      toolItem = new SecondaryToolItem(this, style, image);
    }
    else
    {
      toolItem = new ToolItem(this, style);
      toolItem.setImage(image);
    }
  }

  public final ToolItem getToolItem()
  {
    return toolItem;
  }

  public void setImage(Image image)
  {
    if (toolItem instanceof SecondaryToolItem)
    {
      SecondaryToolItem secondaryToolItem = (SecondaryToolItem)toolItem;
      secondaryToolItem.init(image);
    }
    else
    {
      toolItem.setImage(image);
    }
  }

  public void addSelectionListener(SelectionListener listener)
  {
    toolItem.addSelectionListener(listener);
  }

  public boolean getSelection()
  {
    return toolItem.getSelection();
  }

  @Override
  public String getToolTipText()
  {
    return toolItem.getToolTipText();
  }

  public void removeSelectionListener(SelectionListener listener)
  {
    toolItem.removeSelectionListener(listener);
  }

  public void setSelection(boolean selected)
  {
    toolItem.setSelection(selected);
  }

  @Override
  public void setToolTipText(String string)
  {
    toolItem.setToolTipText(string);
  }

  @Override
  protected void checkSubclass()
  {
    // Do nothing.
  }
}
