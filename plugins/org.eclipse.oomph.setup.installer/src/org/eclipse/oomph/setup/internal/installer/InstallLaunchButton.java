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

import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Andreas Scharf
 */
public class InstallLaunchButton extends ImageHoverButton
{
  private static final Color COLOR_FOREGROUND_DEFAULT = SetupInstallerPlugin.COLOR_WHITE;

  private static final Color COLOR_INSTALL = SetupInstallerPlugin.getColor(250, 148, 0);

  private static final Color COLOR_INSTALLING = SetupInstallerPlugin.getColor(50, 196, 0);

  private static final Color COLOR_INSTALLING_FOREGROUND = SetupInstallerPlugin.COLOR_LABEL_FOREGROUND;

  private static final Color COLOR_LAUNCH = COLOR_INSTALLING;

  private State currentState;

  private float progress;

  public InstallLaunchButton(Composite parent)
  {
    super(parent, SWT.PUSH);

    setForeground(UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE));
    setFont(SetupInstallerPlugin.getFont(getFont(), URI.createURI("font:///14/bold")));
    setCornerWidth(10);
    setAlignment(SWT.CENTER);
    setCurrentState(State.INSTALL);
  }

  public float getProgress()
  {
    return progress;
  }

  public void setProgress(float progress)
  {
    if (progress < 0 || progress > 1)
    {
      throw new IllegalArgumentException("Progress must be in [0..1]");
    }

    this.progress = progress;
    redraw();
  }

  public void setCurrentState(State newState)
  {
    if (newState == null)
    {
      throw new IllegalArgumentException("New state cannot be null!");
    }

    if (currentState != newState)
    {
      State oldState = currentState;
      currentState = newState;
      stateChanged(oldState, currentState);
    }
  }

  private void stateChanged(State oldState, State newState)
  {
    setDefaultImage(newState.icon);
    setHoverImage(newState.hoverIcon);
    setText(newState.label);
    setBackground(newState.backgroundColor);
    setForeground(newState.foregroundColor);

    switch (newState)
    {
      case INSTALLING:
        setListenersPaused(true);
        setCursor(null);
        setShowButtonDownState(false);
        break;

      default:
        setListenersPaused(false);
        setCursor(UIUtil.getDisplay().getSystemCursor(SWT.CURSOR_HAND));
        setShowButtonDownState(true);
    }
  }

  public State getCurrentState()
  {
    return currentState;
  }

  @Override
  public void drawBackground(GC gc, int x, int y, int width, int height, int offsetX, int offsetY)
  {
    if (currentState == State.INSTALLING)
    {
      gc.setBackground(SetupInstallerPlugin.COLOR_LIGHTEST_GRAY);
      gc.fillRoundRectangle(x, y, width, height, getCornerWidth(), getCornerWidth());

      int progressWidth = (int)(width * progress);
      gc.setBackground(currentState.backgroundColor);
      gc.fillRoundRectangle(x, y, progressWidth, height, getCornerWidth(), getCornerWidth());

      // Check if we should draw a hard edge.
      if (progressWidth <= width - getCornerWidth() / 2)
      {
        gc.fillRectangle(progressWidth - getCornerWidth(), y, getCornerWidth(), height);
      }
    }
    else
    {
      super.drawBackground(gc, x, y, width, height, offsetX, offsetY);
    }
  }

  /**
   * @author Andreas Scharf
   */
  public static enum State
  {
    INSTALL("INSTALL", COLOR_INSTALL, COLOR_FOREGROUND_DEFAULT, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_install.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_install_hover.png")),

    INSTALLING("INSTALLING", COLOR_INSTALLING, COLOR_INSTALLING_FOREGROUND),

    INSTALLED("LAUNCH", COLOR_LAUNCH, COLOR_FOREGROUND_DEFAULT, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_launch.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_launch_hover.png"));

    public final Image icon;

    public final Image hoverIcon;

    public final String label;

    public final Color backgroundColor;

    public final Color foregroundColor;

    State(final String label, final Color backgroundColor, final Color foregroundColor)
    {
      this(label, backgroundColor, foregroundColor, null, null);
    }

    State(final String label, final Color backgroundColor, final Color foregroundColor, final Image icon, final Image hoverIcon)
    {
      this.label = label;
      this.backgroundColor = backgroundColor;
      this.foregroundColor = foregroundColor;
      this.icon = icon;
      this.hoverIcon = hoverIcon;
    }
  }
}
