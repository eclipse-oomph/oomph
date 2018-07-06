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
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.ui.FlatButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Andreas Scharf
 */
public class SimpleCheckbox extends FlatButton
{
  private static final Color COLOR_BACKGROUND = SetupInstallerPlugin.getColor(245, 245, 245);

  private static final Color COLOR_HOVER = SetupInstallerPlugin.getColor(217, 217, 217);

  private boolean checked;

  public SimpleCheckbox(Composite parent)
  {
    super(parent, SWT.CHECK);
    setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/checkmark_checked.png"));

    setIconTextGap(10);
    addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        setChecked(!SimpleCheckbox.this.isChecked());
      }
    });
    setFont(SimpleInstallerDialog.getFont(0, "bold"));
    setForeground(AbstractSimpleDialog.COLOR_LABEL_FOREGROUND);
    setDisabledBackgroundColor(null);
  }

  @Override
  protected void drawImage(GC gc, int x, int y)
  {
    Image img = getImage();
    Rectangle imgBounds = img.getBounds();

    Color oldBG = gc.getBackground();
    Color bgColor = isHover() ? COLOR_HOVER : COLOR_BACKGROUND;

    gc.setBackground(bgColor);
    gc.fillRoundRectangle(x, y, imgBounds.width, imgBounds.height, getCornerWidth(), getCornerWidth());
    gc.setBackground(oldBG);

    if (isChecked())
    {
      gc.drawImage(img, x, y);
    }
  }

  public boolean isChecked()
  {
    return checked;
  }

  public void setChecked(boolean checked)
  {
    this.checked = checked;
    redraw();
  }
}
