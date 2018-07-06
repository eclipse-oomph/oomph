/*
 * Copyright (c) 2015, 2017 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Yatta Solutions - [466264] initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Special button that does not draw any borders on hover/down coming from the OS.
 *
 * @author Andreas Scharf
 */
public class FlatButton extends Canvas implements Listener, PaintListener
{
  private static final int[] LISTENER_TYPES = { SWT.MouseEnter, SWT.MouseExit, SWT.MouseDown, SWT.MouseUp, SWT.MouseMove, SWT.KeyDown, SWT.Traverse,
      SWT.FocusIn, SWT.FocusOut };

  private static final Cursor CURSOR_HAND = UIUtil.getDisplay().getSystemCursor(SWT.CURSOR_HAND);

  private static final int DEFAULT_ICON_TEXT_GAP = 5;

  protected static final Color COLOR_DEFAULT_FOCUS_FOREGROUND = UIPlugin.getColor(180, 180, 180);

  protected static final Color COLOR_DEFAULT_DISABLED_BACKGROUND = UIPlugin.getColor(238, 238, 238);

  protected static final Color COLOR_DEFAULT_DISABLED_FOREGROUND = UIPlugin.getColor(170, 170, 170);

  private final List<SelectionListener> selectionListeners = new ArrayList<SelectionListener>();

  private final int buttonStyle;

  private boolean hover;

  private Color hoverColor = UIUtil.getEclipseThemeColor();

  private Color disabledBackgroundColor = COLOR_DEFAULT_DISABLED_BACKGROUND;

  private Color disabledForegroundColor = COLOR_DEFAULT_DISABLED_FOREGROUND;

  private Color internalBackgroundColor;

  private Color focusForegroundColor = COLOR_DEFAULT_FOCUS_FOREGROUND;

  private Image image;

  private String text;

  private int cornerWidth;

  private int iconTextGap = DEFAULT_ICON_TEXT_GAP;

  private int alignment = SWT.LEFT;

  private boolean listenersPaused;

  private boolean mouseDown;

  private boolean mouseLockedInBounds;

  private boolean showButtonDownState = true;

  public FlatButton(Composite parent, int buttonStyle)
  {
    super(parent, SWT.NO_BACKGROUND | SWT.DOUBLE_BUFFERED);
    this.buttonStyle = buttonStyle;

    setLayout(new GridLayout(1, false));
    setCursor(CURSOR_HAND);

    hookListeners();
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    super.setEnabled(enabled);
    setCursor(enabled ? CURSOR_HAND : null);
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    if (this.text != text)
    {
      this.text = text;
      redraw();
    }
  }

  public Image getImage()
  {
    return image;
  }

  public void setImage(Image image)
  {
    if (this.image != image)
    {
      this.image = image;
      redraw();
    }
  }

  public int getAlignment()
  {
    return alignment;
  }

  public void setAlignment(int alignment)
  {
    switch (alignment)
    {
      case SWT.LEFT:
      case SWT.CENTER:
      case SWT.RIGHT:
        // Do nothing.
        break;

      default:
        throw new IllegalArgumentException("Alignment must be one of SWT.LEFT, SWT.CENTER, SWT.RIGHT");
    }

    if (this.alignment != alignment)
    {
      this.alignment = alignment;
      redraw();
    }
  }

  public int getCornerWidth()
  {
    return cornerWidth;
  }

  public void setCornerWidth(int cornerWidth)
  {
    if (this.cornerWidth != cornerWidth)
    {
      this.cornerWidth = cornerWidth;
      redraw();
    }
  }

  @Override
  public Color getBackground()
  {
    if (internalBackgroundColor == null)
    {
      return super.getBackground();
    }

    return internalBackgroundColor;
  }

  @Override
  public void setBackground(Color color)
  {
    if (internalBackgroundColor != color)
    {
      internalBackgroundColor = color;
      redraw();
    }
  }

  public Color getHoverColor()
  {
    return hoverColor;
  }

  public void setHoverColor(Color hoverColor)
  {
    if (this.hoverColor != hoverColor)
    {
      this.hoverColor = hoverColor;
      redraw();
    }
  }

  public Color getDisabledForegroundColor()
  {
    return disabledForegroundColor;
  }

  public void setDisabledForegroundColor(Color color)
  {
    if (!ObjectUtil.equals(disabledForegroundColor, color))
    {
      disabledForegroundColor = color;
      redraw();
    }
  }

  protected void setDisabledBackgroundColor(Color color)
  {
    if (!ObjectUtil.equals(disabledBackgroundColor, color))
    {
      disabledBackgroundColor = color;
      redraw();
    }
  }

  protected Color getDisabledBackgroundColor()
  {
    return disabledBackgroundColor;
  }

  public Color getFocusForegroundColor()
  {
    return focusForegroundColor;
  }

  public void setFocusForegroundColor(Color focusForegroundColor)
  {
    this.focusForegroundColor = focusForegroundColor;
  }

  public boolean isShowButtonDownState()
  {
    return showButtonDownState;
  }

  public void setShowButtonDownState(boolean showButtonDownState)
  {
    if (this.showButtonDownState != showButtonDownState)
    {
      this.showButtonDownState = showButtonDownState;
      redraw();
    }
  }

  public int getIconTextGap()
  {
    return iconTextGap;
  }

  public void setIconTextGap(int iconTextGap)
  {
    if (this.iconTextGap != iconTextGap)
    {
      this.iconTextGap = iconTextGap;

      Composite parent = getParent();
      if (parent != null)
      {
        parent.layout();
      }
    }
  }

  public void addSelectionListener(SelectionListener listener)
  {
    selectionListeners.add(listener);
  }

  public void removeSelectionListener(SelectionListener listener)
  {
    selectionListeners.remove(listener);
  }

  public boolean isHover()
  {
    return hover;
  }

  @Override
  public Point computeSize(int wHint, int hHint, boolean changed)
  {
    Point size = wHint != SWT.DEFAULT && hHint != SWT.DEFAULT ? new Point(wHint, hHint) : getTotalSize();

    if (wHint != SWT.DEFAULT)
    {
      size.x = wHint;
    }

    if (hHint != SWT.DEFAULT)
    {
      size.y = hHint;
    }

    // Extend the size to be able to visualize button down state.
    size.x += 1;
    size.y += 1;

    return size;
  }

  protected Point getTotalSize()
  {
    int width = 0;
    int height = 0;
    if (text != null)
    {
      GC gc = new GC(this);
      Point textSize;

      try
      {
        textSize = gc.textExtent(text);
      }
      finally
      {
        gc.dispose();
      }

      width += textSize.x;

      if (image != null)
      {
        width += iconTextGap;
      }

      height += textSize.y;
    }

    if (image != null)
    {
      Rectangle imgBounds = image.getBounds();
      width += imgBounds.width;

      int heightDiff = imgBounds.height - height;
      if (heightDiff > 0)
      {
        height += heightDiff;
      }
    }

    return new Point(width, height);
  }

  protected void hookListeners()
  {
    for (int type : LISTENER_TYPES)
    {
      addListener(type, this);
    }

    addPaintListener(this);
  }

  protected void unhookListeners()
  {
    removePaintListener(this);

    for (int type : LISTENER_TYPES)
    {
      removeListener(type, (Listener)this);
    }
  }

  @Override
  public void dispose()
  {
    unhookListeners();
    super.dispose();
  }

  /**
   * Called when this button should paint itself.
   *
   * Subclasses may override.
   */
  protected void drawContent(PaintEvent e)
  {
    GC gc = e.gc;

    Point totalSize = getTotalSize();

    Rectangle clientArea = getClientArea();

    int startX = 0;

    switch (getAlignment())
    {
      case SWT.CENTER:
        startX = (clientArea.x + clientArea.width - totalSize.x) / 2;
        break;

      case SWT.RIGHT:
        startX = clientArea.x + clientArea.width - totalSize.x;
        break;
    }

    if (showButtonDownState && mouseLockedInBounds)
    {
      startX++;
    }

    if (image != null)
    {
      Rectangle imgBounds = image.getBounds();
      int imgY = (clientArea.y + clientArea.height - imgBounds.height) / 2;

      if (showButtonDownState && mouseLockedInBounds)
      {
        imgY++;
      }

      drawImage(gc, startX, imgY);

      startX += imgBounds.width;
      if (text != null)
      {
        startX += iconTextGap;
      }
    }

    if (text != null)
    {
      Point textExtent = gc.textExtent(text);
      int textY = (clientArea.y + clientArea.height - textExtent.y) / 2;

      if (showButtonDownState && mouseLockedInBounds)
      {
        textY += 1;
      }

      drawText(gc, startX, textY);
    }
  }

  protected void drawText(GC gc, int x, int y)
  {
    if (isEnabled())
    {
      if (isHover() || isFocusControl() && !listenersPaused && hoverColor != null)
      {
        gc.setForeground(hoverColor);
      }
    }
    else if (disabledForegroundColor != null)
    {
      gc.setForeground(disabledForegroundColor);
    }

    gc.drawText(text, x, y, true);
  }

  protected void drawImage(GC gc, int x, int y)
  {
    gc.drawImage(image, x, y);
  }

  @Override
  public int getStyle()
  {
    return buttonStyle;
  }

  @Override
  protected void checkSubclass()
  {
    // Allow subclassing.
  }

  public void handleEvent(final Event event)
  {
    switch (event.type)
    {
      case SWT.MouseEnter:
        onMouseEnter(event);
        break;

      case SWT.MouseUp:
        onMouseUp(event);
        break;

      case SWT.MouseExit:
        onMouseExit(event);
        break;

      case SWT.MouseDown:
        onMouseDown(event);
        break;

      case SWT.MouseMove:
        mouseMoveInternal(event);
        break;

      case SWT.KeyDown:
        if (event.character == SWT.CR || event.character == SWT.SPACE)
        {
          notifySelectionListeners(null);
        }
        break;

      case SWT.Traverse:
        event.doit = true;
        break;

      case SWT.FocusIn:
        onFocusIn(event);
        break;

      case SWT.FocusOut:
        // Async exec to ensure that focus transfer is completed
        UIUtil.asyncExec(this, new Runnable()
        {
          public void run()
          {
            onFocusOut(event);
          }
        });
        break;
    }

    if (!isDisposed())
    {
      redraw();
    }
  }

  /**
   * Called when the button loses focus.
   *
   * @param event The associated {@link Event}, may be null.
   */
  protected void onFocusOut(Event event)
  {
    // Subclasses may override.
  }

  /**
   * Called when the button gains focus.
   *
   * @param event The associated {@link Event}, may be null.
   */
  protected void onFocusIn(Event event)
  {
    // Subclasses may override.
  }

  private void mouseMoveInternal(Event event)
  {
    if (mouseDown)
    {
      Rectangle bounds = getBounds();
      boolean inBounds = event.x >= 0 && event.x <= bounds.width && event.y >= 0 && event.y <= bounds.height;
      if (inBounds != mouseLockedInBounds)
      {
        mouseLockedInBounds = inBounds;
        if (inBounds)
        {
          onMouseEnter(event);
        }
        else
        {
          onMouseExit(event);
        }
      }
    }
  }

  protected void onMouseDown(Event event)
  {
    mouseDown = true;
    mouseLockedInBounds = true;
  }

  protected void onMouseExit(Event event)
  {
    setHover(false);
  }

  protected void onMouseUp(Event event)
  {
    mouseLockedInBounds = false;
    mouseDown = false;
    setHover(true);
    notifySelectionListeners(new SelectionEvent(event));
  }

  /**
   * Called when the hover status of this button changes.
   */
  protected void onHover()
  {
    // Do nothing.
  }

  protected void onMouseEnter(Event event)
  {
    setHover(true);
  }

  private void setHover(boolean hover)
  {
    if (this.hover != hover)
    {
      this.hover = hover;
      onHover();
    }
  }

  private void notifySelectionListeners(SelectionEvent event)
  {
    if (!isEnabled() || listenersPaused)
    {
      return;
    }

    Rectangle r = getBounds();

    // Event could be null if the keyboard was used
    if (event == null || event.x >= 0 && event.x <= r.width && event.y >= 0 && event.y <= r.height && !selectionListeners.isEmpty())
    {
      for (SelectionListener listener : selectionListeners)
      {
        try
        {
          listener.widgetSelected(event);
        }
        catch (Exception ex)
        {
          UIPlugin.INSTANCE.log(ex);
        }
      }
    }
  }

  protected boolean isListenersPaused()
  {
    return listenersPaused;
  }

  protected void setListenersPaused(boolean pause)
  {
    listenersPaused = pause;
  }

  public final void paintControl(PaintEvent e)
  {
    Rectangle clientBounds = getClientArea();
    GC gc = e.gc;
    gc.setAntialias(SWT.ON);
    int clientX = clientBounds.x;
    int clientY = clientBounds.y;

    int clientWidth = clientBounds.width - 1;
    int clientHeight = clientBounds.height - 1;

    if (showButtonDownState && mouseLockedInBounds)
    {
      clientX++;
      clientY++;
    }

    clearBackground(gc, clientBounds);

    drawBackground(gc, clientX, clientY, clientWidth, clientHeight, 0, 0);
    drawContent(e);

    if (isHover())
    {
      drawHoverState(gc, clientX, clientY, clientWidth, clientHeight);
    }

    if (isFocusControl())
    {
      drawFocusState(gc, clientX, clientY, clientWidth, clientHeight);
    }
  }

  /**
   * Draws the focus state of this button. This method will be called last, meaning
   * that everything is painted <b>on top</b> of anything else.
   *
   * @param gc The {@link GC} to draw with
   * @param x The x coordinate
   * @param x The y coordinate
   * @param width The width to visualize
   * @param height The height to visualize
   */
  protected void drawFocusState(GC gc, int x, int y, int width, int height)
  {
    if (focusForegroundColor != null)
    {
      Color oldForeground = gc.getForeground();
      gc.setForeground(focusForegroundColor);
      gc.drawRoundRectangle(x, y, width, height, cornerWidth, cornerWidth);
      gc.setForeground(oldForeground);
    }
  }

  private void clearBackground(GC gc, Rectangle bounds)
  {
    Composite parent = getParent();
    Color parentBackground;

    do
    {
      parentBackground = parent.getBackground();
      parent = parent.getParent();
    } while (parentBackground == null && parent.getParent() != null);

    if (parentBackground != null)
    {
      Color oldBG = gc.getBackground();
      gc.setBackground(parentBackground);
      gc.fillRectangle(bounds);
      gc.setBackground(oldBG);
    }
  }

  protected void drawHoverState(GC gc, int x, int y, int width, int height)
  {
    // Subclasses may override to draw a hover state.
  }

  @Override
  public void drawBackground(GC gc, int x, int y, int width, int height, int offsetX, int offsetY)
  {
    if (isEnabled())
    {
      if (internalBackgroundColor != null)
      {
        gc.setBackground(internalBackgroundColor);
        gc.fillRoundRectangle(x, y, width, height, cornerWidth, cornerWidth);
      }
    }
    else if (disabledBackgroundColor != null)
    {
      gc.setBackground(disabledBackgroundColor);
      gc.fillRoundRectangle(x, y, width, height, cornerWidth, cornerWidth);
    }
  }
}
