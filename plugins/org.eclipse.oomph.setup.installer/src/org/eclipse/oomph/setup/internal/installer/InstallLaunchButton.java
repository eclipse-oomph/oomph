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

  private ProgressSpinner progressSpinner;

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
      progressSpinner = new ProgressSpinner(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/progress_sprite.png"), 8, 4, 20);

      // Skip first white image.
      progressSpinner.setKeyframeRange(1, 31);
    }

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
    setToolTipText(newState.tooltip);
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
      progressSpinner.setVisible(false);
    }
  }

  public void startProgressAnimation()
  {
    if (progressSpinner != null)
    {
      relocateProgressAnimation();
      progressSpinner.setVisible(true);
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
        progressSpinner.setLocation(startX, startY);
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

      if (progressSpinner != null)
      {
        progressSpinner.update(gc);
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
    INSTALL("INSTALL", "Start product installation", COLOR_INSTALL, COLOR_FOREGROUND_DEFAULT,
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_install.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_install_hover.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_install_disabled.png")),

    INSTALLING("INSTALLING", "Installion in progress", COLOR_INSTALLING, COLOR_INSTALLING_FOREGROUND, null, null, null),

    LAUNCH("LAUNCH", "Launch the selected product", COLOR_LAUNCH, COLOR_FOREGROUND_DEFAULT,
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_launch.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/install_button_launch_hover.png"), null);

    public final Image icon;

    public final Image hoverIcon;

    public final Image disabledIcon;

    public final String label;

    public final String tooltip;

    public final Color backgroundColor;

    public final Color foregroundColor;

    State(final String label, final String tooltip, final Color backgroundColor, final Color foregroundColor, final Image icon, final Image hoverIcon,
        Image disabledIcon)
    {
      this.label = label;
      this.tooltip = tooltip;
      this.backgroundColor = backgroundColor;
      this.foregroundColor = foregroundColor;
      this.icon = icon;
      this.hoverIcon = hoverIcon;
      this.disabledIcon = disabledIcon;
    }
  }

  /**
   * Simple animation of progress. In contrast to {@link SpriteAnimator}, this spinner is
   * not a SWT component, which eases handling of transparency on different platforms.
   *
   * @author Andreas Scharf
   */
  private class ProgressSpinner implements Runnable
  {
    private Image[] sprites;

    private int currentSpriteIndex = 0;

    private long nextKeyframeTime = -1;

    private boolean visible;

    private long delay;

    private int x = -1;

    private int y = -1;

    private int firstKeyframe = -1;

    private int lastKeyframe = -1;

    public ProgressSpinner(Image textureAtlas, int countX, int countY, long delay)
    {
      this.delay = delay;
      sprites = UIUtil.extractSprites(textureAtlas, countX, countY);

      firstKeyframe = 0;
      lastKeyframe = sprites.length - 1;

      visible = false;
    }

    public void setLocation(int x, int y)
    {
      this.x = x;
      this.y = y;
    }

    public void setKeyframeRange(int firstKeyframe, int lastKeyframe)
    {
      this.firstKeyframe = firstKeyframe;
      this.lastKeyframe = lastKeyframe;
    }

    public int getWidth()
    {
      return sprites[0].getBounds().width;
    }

    public int getHeight()
    {
      return sprites[0].getBounds().height;
    }

    public void setDelay(long delay)
    {
      this.delay = delay;
    }

    public void setVisible(boolean visible)
    {
      this.visible = visible;
      if (this.visible)
      {
        schedule();
      }
    }

    public void update(GC gc)
    {
      if (!visible || x < 0 || y < 0)
      {
        return;
      }

      gc.drawImage(sprites[currentSpriteIndex], x, y);

      long now = System.currentTimeMillis();
      if (now >= nextKeyframeTime)
      {
        if (++currentSpriteIndex > lastKeyframe)
        {
          currentSpriteIndex = firstKeyframe;
        }

        nextKeyframeTime = now + delay;
      }
    }

    public void run()
    {
      if (visible && !isDisposed())
      {
        InstallLaunchButton.this.redraw();
        schedule();
      }
    }

    private void schedule()
    {
      getDisplay().timerExec((int)delay, this);
    }
  }
}
