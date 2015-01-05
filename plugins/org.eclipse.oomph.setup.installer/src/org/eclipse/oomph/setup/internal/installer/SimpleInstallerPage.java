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
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.ui.wizards.SetupWizard.Installer;

import org.eclipse.emf.common.util.URI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Eike Stepper
 */
public abstract class SimpleInstallerPage extends Composite
{
  public static final RGB HOVER_RGB = new RGB(175, 187, 220);

  public static final RGB ACTIVE_RGB = new RGB(196, 211, 254);

  private static final URI FONT_URI = URI.createURI("font://Helvetica/14///");

  protected final Font font;

  protected final Installer installer;

  protected final SimpleInstallerDialog dialog;

  public SimpleInstallerPage(final Composite parent, int style, final SimpleInstallerDialog dialog)
  {
    super(parent, style);
    font = SetupInstallerPlugin.getFont(parent.getFont(), FONT_URI);
    installer = dialog.getInstaller();
    this.dialog = dialog;
  }

  @Override
  protected void checkSubclass()
  {
    // Disable the check that prevents subclassing of SWT components.
  }

  public static String hex(RGB color)
  {
    return hex(color.red) + hex(color.green) + hex(color.blue);
  }

  public static String hex(int byteValue)
  {
    String hexString = Integer.toHexString(byteValue);
    if (hexString.length() == 1)
    {
      hexString = "0" + hexString;
    }

    return hexString;
  }

  /**
   * @author Eike Stepper
   */
  public static abstract class ImageButton extends Label implements MouseTrackListener, MouseMoveListener, MouseListener, SelectionListener
  {
    private static final Color HOVER_COLOR = SetupInstallerPlugin.getColor(HOVER_RGB);

    private static final Color ACTIVE_COLOR = SetupInstallerPlugin.getColor(ACTIVE_RGB);

    private Color oldBackground;

    private boolean mouseDown;

    public ImageButton(Composite parent, Image image)
    {
      super(parent, SWT.NONE);
      setImage(image);

      addMouseTrackListener(this);
      addMouseMoveListener(this);
      addMouseListener(this);
    }

    public void mouseEnter(MouseEvent e)
    {
      if (oldBackground == null)
      {
        oldBackground = getBackground();
      }

      if (mouseDown)
      {
        setBackground(ACTIVE_COLOR);
      }
      else
      {
        setBackground(HOVER_COLOR);
      }
    }

    public void mouseExit(MouseEvent e)
    {
      if (oldBackground != null)
      {
        setBackground(oldBackground);
        oldBackground = null;
      }
    }

    public void mouseHover(MouseEvent e)
    {
      // Do nothing.
    }

    public void mouseMove(MouseEvent e)
    {
      Rectangle bounds = getBounds();
      bounds.x = 0;
      bounds.y = 0;

      if (bounds.contains(e.x, e.y))
      {
        if (oldBackground == null)
        {
          mouseEnter(null);
        }
      }
      else
      {
        if (oldBackground != null)
        {
          mouseExit(null);
        }
      }
    }

    public void mouseDoubleClick(MouseEvent e)
    {
      // Do nothing.
    }

    public void mouseDown(MouseEvent e)
    {
      mouseDown = true;
      setBackground(ACTIVE_COLOR);
    }

    public void mouseUp(MouseEvent e)
    {
      if (oldBackground != null)
      {
        setBackground(HOVER_COLOR);
        widgetSelected();
      }
      else
      {
        mouseExit(null);
      }

      mouseDown = false;
    }

    public void widgetDefaultSelected(SelectionEvent e)
    {
      // Do nothing.
    }

    public void widgetSelected(SelectionEvent e)
    {
      widgetSelected();
    }

    @Override
    protected void checkSubclass()
    {
      // Disable the check that prevents subclassing of SWT components.
    }

    protected abstract void widgetSelected();
  }
}
