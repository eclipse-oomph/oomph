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
import org.eclipse.oomph.ui.SpriteAnimator;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Andreas Scharf
 */
public class InstallLaunchButton extends ImageHoverButton
{
  private static final Color COLOR_FOREGROUND_DEFAULT = AbstractSimpleDialog.COLOR_WHITE;

  private static final Color COLOR_INSTALL = SetupInstallerPlugin.getColor(250, 148, 0);

  private static final Color COLOR_INSTALLING = SetupInstallerPlugin.getColor(50, 196, 0);

  private static final Color COLOR_INSTALLING_FOREGROUND = AbstractSimpleDialog.COLOR_LABEL_FOREGROUND;

  private static final Color COLOR_LAUNCH = COLOR_INSTALLING;

  private static final long MINIMUM_PROGRESS_ANIMATION_DELAY = 20;

  private static final long MAXIMUM_PROGRESS_ANIMATION_DELAY = 200;

  private State currentState;

  private float progress;

  private SpriteAnimator progressSpinner;

  public InstallLaunchButton(Composite parent)
  {
    super(parent, SWT.PUSH);

    setForeground(UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE));
    setFont(SetupInstallerPlugin.getFont(getFont(), URI.createURI("font:///14/bold")));
    setCornerWidth(10);
    setAlignment(SWT.CENTER);
    setDisabledBackgroundColor(COLOR_DEFAULT_DISABLED_BACKGROUND);
    setBackgroundMode(SWT.INHERIT_FORCE);
    setLayout(null);

    if (SimpleVariablePage.PROGRESS_WATCHDOG_TIMEOUT != 0)
    {
      progressSpinner = new SpriteAnimator(this, SWT.TRANSPARENT, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/progress_sprite.png"), 32, 32, 20);
    }

    addControlListener(new ControlListener()
    {
      public void controlResized(ControlEvent e)
      {
        if (currentState == State.INSTALL)
        {
          relocateProgressAnimation();
        }
      }

      public void controlMoved(ControlEvent e)
      {
        if (currentState == State.INSTALL)
        {
          relocateProgressAnimation();
        }
      }
    });

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

  @Override
  public boolean isHover()
  {
    // No hover during install.
    return currentState == State.INSTALLING ? false : super.isHover();
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
    setDisabledImage(newState.disabledIcon);
    setText(newState.label);
    setBackground(newState.backgroundColor);
    setForeground(newState.foregroundColor);

    switch (newState)
    {
      case INSTALLING:
        setListenersPaused(true);
        setCursor(null);
        setShowButtonDownState(false);
        if (progressSpinner != null)
        {
          progressSpinner.setBackground(newState.backgroundColor);
          progressSpinner.setVisible(true);
        }

        break;

      default:
        setListenersPaused(false);
        setCursor(UIUtil.getDisplay().getSystemCursor(SWT.CURSOR_HAND));
        setShowButtonDownState(true);
        stopProgressAnimation();
        if (progressSpinner != null)
        {
          progressSpinner.setVisible(false);
        }
    }
  }

  public void setProgressAnimationSpeed(float speed)
  {
    if (progressSpinner != null)
    {
      if (speed < 0 || speed > 1)
      {
        throw new IllegalArgumentException("speed must be between [0..1]");
      }

      int delay = Math.round((1f - speed) * MAXIMUM_PROGRESS_ANIMATION_DELAY);
      progressSpinner.setDelay(Math.max(delay, MINIMUM_PROGRESS_ANIMATION_DELAY));
    }
  }

  public void stopProgressAnimation()
  {
    if (progressSpinner != null)
    {
      progressSpinner.stop();
    }
  }

  public void startProgressAnimation()
  {
    if (progressSpinner != null)
    {
      relocateProgressAnimation();
      progressSpinner.start();
    }
  }

  private void relocateProgressAnimation()
  {
    if (progressSpinner != null)
    {
      GC gc = new GC(this);

      try
      {
        Point textExtent = gc.textExtent(getText());
        int spinnerTextGap = 10;
        Rectangle clientArea = getClientArea();
        int startX = (clientArea.width - textExtent.x) / 2 - spinnerTextGap - progressSpinner.getWidth();
        int startY = (clientArea.height - progressSpinner.getHeight()) / 2;
        progressSpinner.setBounds(startX, startY, 32, 32);
      }
      finally
      {
        gc.dispose();
      }
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
      gc.setBackground(AbstractSimpleDialog.COLOR_LIGHTEST_GRAY);
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
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_install_hover.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_install_disabled.png")),

    INSTALLING("INSTALLING", COLOR_INSTALLING, COLOR_INSTALLING_FOREGROUND, null, null, null),

    LAUNCH("LAUNCH", COLOR_LAUNCH, COLOR_FOREGROUND_DEFAULT, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_launch.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_launch_hover.png"), null);

    public final Image icon;

    public final Image hoverIcon;

    public final Image disabledIcon;

    public final String label;

    public final Color backgroundColor;

    public final Color foregroundColor;

    State(final String label, final Color backgroundColor, final Color foregroundColor, final Image icon, final Image hoverIcon, Image disabledIcon)
    {
      this.label = label;
      this.backgroundColor = backgroundColor;
      this.foregroundColor = foregroundColor;
      this.icon = icon;
      this.hoverIcon = hoverIcon;
      this.disabledIcon = disabledIcon;
    }
  }
}
