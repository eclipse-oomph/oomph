/*
 * Copyright (c) 2015 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yatta Solutions - [467209] initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Andreas Scharf
 */
public class ToggleSwitchButton extends FlatButton
{
  private boolean selected;

  private final static Color WHITE = UIPlugin.getColor(255, 255, 255);

  private Color selectedColor = UIPlugin.getColor(58, 195, 4);

  private Color unselectedColor = UIPlugin.getColor(216, 217, 219);

  public ToggleSwitchButton(Composite parent)
  {
    super(parent, SWT.CHECK);
    setCornerWidth(15);
    setShowButtonDownState(false);

    addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        setSelected(!isSelected());
      }
    });

    updateSelected();
    setSelected(true);
  }

  @Override
  protected Point getTotalSize()
  {
    return new Point(25, 16);
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected(boolean selected)
  {
    if (this.selected != selected)
    {
      this.selected = selected;
      updateSelected();
    }
  }

  private void updateSelected()
  {
    setBackground(selected ? selectedColor : unselectedColor);
    redraw();
  }

  @Override
  public void drawBackground(GC gc, int x, int y, int width, int height, int offsetX, int offsetY)
  {
    super.drawBackground(gc, x, y, width, height, offsetX, offsetY);
  }

  @Override
  protected void drawFocusState(GC gc, int x, int y, int width, int height)
  {
    // No focus state for toggle switches
  }

  @Override
  protected void drawContent(PaintEvent e)
  {
    GC gc = e.gc;
    Color oldBackground = gc.getBackground();

    int startX = selected ? 10 : 2;

    gc.setBackground(WHITE);
    gc.fillOval(startX, 1, 14, 14);

    gc.setBackground(oldBackground);
  }
}
