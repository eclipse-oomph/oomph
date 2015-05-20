/*
 * Copyright (c) 2015 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yatta Solutions - [466264] initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageHoverButton;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Andreas Scharf
 */
public class SimpleInstallerMenuButton extends Composite
{
  static final String ACCESS_KEY = "menuButton";

  private static final int NOTIFICATION_X_OFFSET = 15;

  // TODO We have some issues with transparency of the overlay
  // if we move the overlay a bit down (e.g. by using a y-offset of -7)
  // for example.
  private static final int NOTIFICATION_Y_OFFSET = -9;

  private final Label notificationOverlay;

  private final FlatButton button;

  public SimpleInstallerMenuButton(Composite parent)
  {
    super(parent, SWT.NONE);
    setLayout(new FillLayout());

    Composite container = new Composite(this, SWT.NONE);

    notificationOverlay = new Label(container, SWT.NONE);
    Image overlayImage = SetupInstallerPlugin.INSTANCE.getSWTImage("simple/notification_overlay.png");
    Rectangle overlayImageBounds = overlayImage.getBounds();
    notificationOverlay.setImage(overlayImage);

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
    button.addSelectionListener(listener);
  }

  public void removeSelectionListener(SelectionListener listener)
  {
    button.removeSelectionListener(listener);
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
