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

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Andreas Scharf
 */
public class SimpleInstallerMenuButton extends Composite
{
  public static final String ACCESS_KEY = "menuButton";

  private static final Cursor CURSOR_HAND = UIUtil.getDisplay().getSystemCursor(SWT.CURSOR_HAND);

  private static final int NOTIFICATION_X_OFFSET = 15;

  // TODO We have some issues with transparency of the overlay
  // if we move the overlay a bit down (e.g. by using a y-offset of -7)
  // for example.
  private static final int NOTIFICATION_Y_OFFSET = -9;

  private final Set<SelectionListener> selectionListeners = new HashSet<SelectionListener>();

  private final Label notificationOverlay;

  private final FlatButton button;

  public SimpleInstallerMenuButton(Composite parent)
  {
    super(parent, SWT.NONE);
    setLayout(new FillLayout());

    Composite container = new Composite(this, SWT.NONE);

    notificationOverlay = new Label(container, SWT.NONE)
    {
      @Override
      protected void checkSubclass()
      {
        // Do nothing.
      }
    };

    notificationOverlay.setCursor(CURSOR_HAND);
    Image overlayImage = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/notification_overlay.png");
    Rectangle overlayImageBounds = overlayImage.getBounds();
    notificationOverlay.setImage(overlayImage);
    notificationOverlay.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseDown(MouseEvent e)
      {
        synchronized (selectionListeners)
        {
          for (SelectionListener listener : selectionListeners)
          {
            try
            {
              listener.widgetSelected(null);
            }
            catch (Exception ex)
            {
              // Ignore.
            }
          }
        }
      }
    });

    int overlayX = notNegative(NOTIFICATION_X_OFFSET);
    int overlayY = notNegative(NOTIFICATION_Y_OFFSET);

    notificationOverlay.setBounds(overlayX, overlayY, overlayImageBounds.width, overlayImageBounds.height);
    notificationOverlay.setVisible(false);

    Image buttonImage = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/menu.png");
    Image buttonHoverImage = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/menu_hover.png");
    Image buttonDisabledImage = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/menu_disabled.png");
    button = new ImageHoverButton(container, SWT.PUSH, buttonImage, buttonHoverImage, buttonDisabledImage);
    AccessUtil.setKey(button, ACCESS_KEY);

    // TODO As soon as the transparancy issues with the overlay are solved,
    // we can re-enable the visualization of the button down state.
    button.setShowButtonDownState(false);

    int baseX = positive(NOTIFICATION_X_OFFSET);
    int baseY = positive(NOTIFICATION_Y_OFFSET);

    Rectangle baseBounds = buttonHoverImage.getBounds();
    button.setBounds(baseX, baseY, baseBounds.width, baseBounds.height);

    Rectangle unionBounds = notificationOverlay.getBounds().union(button.getBounds());
    container.setSize(unionBounds.width, unionBounds.height);
    setNotificationVisible(false);
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
    button.setEnabled(enabled);
    notificationOverlay.setEnabled(enabled);
  }

  public void setNotificationVisible(boolean visible)
  {
    notificationOverlay.setVisible(visible);
  }

  public void addSelectionListener(SelectionListener listener)
  {
    synchronized (selectionListeners)
    {
      selectionListeners.add(listener);
    }

    button.addSelectionListener(listener);
  }

  public void removeSelectionListener(SelectionListener listener)
  {
    button.removeSelectionListener(listener);

    synchronized (selectionListeners)
    {
      selectionListeners.remove(listener);
    }
  }

  private static int notNegative(int value)
  {
    return value >= 0 ? value : 0;
  }

  private static int positive(int value)
  {
    return value >= 0 ? 0 : -value;
  }
}
