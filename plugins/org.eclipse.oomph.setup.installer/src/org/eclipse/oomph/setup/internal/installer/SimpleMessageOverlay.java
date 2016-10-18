/*
 * Copyright (c) 2015, 2016 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yatta Solutions - [466264] initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Andreas Scharf
 */
public class SimpleMessageOverlay extends Shell implements ControlListener
{
  private static final int DEFAULT_AUTO_DISMISS_MILLIS = 4 * 1000;

  private static final int MAX_MESSAGE_LENGTH = 210;

  private static final int MAX_TOOLTIP_LINE_LENGTH = 60;

  private final SimpleInstallerDialog dialog;

  private final Type type;

  private final ControlRelocator controlRelocator;

  private final boolean dismissAutomatically;

  private final Runnable action;

  private StyledText text;

  private boolean firstShown = true;

  private Runnable closeAction;

  public SimpleMessageOverlay(SimpleInstallerDialog dialog, Type type, ControlRelocator controlRelocator, boolean dismissAutomatically)
  {
    this(dialog, type, controlRelocator, dismissAutomatically, null);
  }

  public SimpleMessageOverlay(SimpleInstallerDialog dialog, Type type, ControlRelocator controlRelocator, boolean dismissAutomatically, final Runnable action)
  {
    this(dialog, type, controlRelocator, dismissAutomatically, null, null);
  }

  public SimpleMessageOverlay(SimpleInstallerDialog dialog, Type type, ControlRelocator controlRelocator, boolean dismissAutomatically, final Runnable action,
      final Runnable closeAction)
  {
    super(dialog, SWT.NO_TRIM);
    this.closeAction = closeAction;

    if (type == null)
    {
      throw new IllegalArgumentException("Type must not be null!");
    }

    if (controlRelocator == null)
    {
      throw new IllegalArgumentException("Control relocator must not be null!");
    }

    this.dialog = dialog;
    this.type = type;
    this.controlRelocator = controlRelocator;
    this.dismissAutomatically = dismissAutomatically;
    this.action = action;

    setBackground(type.backgroundColor);
    setBackgroundMode(SWT.INHERIT_FORCE);

    GridLayout layout = new GridLayout(2, false);
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    layout.marginLeft = 22;
    layout.marginRight = 18;
    layout.marginTop = 3;
    layout.marginBottom = 3;
    setLayout(layout);

    text = new StyledText(this, SWT.WRAP | SWT.READ_ONLY);
    text.setLayoutData(GridDataFactory.swtDefaults().grab(true, true).create());
    text.setFont(SimpleInstallerDialog.getDefaultFont());
    text.setForeground(type.foregroundColor);

    if (action != null)
    {
      final Display display = getDisplay();

      text.setCursor(display.getSystemCursor(SWT.CURSOR_HAND));
      text.addMouseListener(new MouseAdapter()
      {
        @Override
        public void mouseUp(MouseEvent e)
        {
          SimpleMessageOverlay.this.dialog.clearMessage();

          try
          {
            action.run();
          }
          catch (Exception ex)
          {
            SetupInstallerPlugin.INSTANCE.log(ex);
          }
        }
      });
    }

    dialog.addControlListener(this);

    FlatButton closeButton = new ImageHoverButton(this, SWT.PUSH, type.closeImg, type.closeImgHover);
    closeButton.setLayoutData(GridDataFactory.swtDefaults().align(SWT.END, SWT.BEGINNING).indent(0, 12).create());
    closeButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        SimpleMessageOverlay.this.dialog.clearMessage();
        if (closeAction != null)
        {
          closeAction.run();
        }
      }
    });

    // Initial bounds
    controlRelocator.relocate(this);

    ParentShellRelocator parentShellRelocator = new ParentShellRelocator(dialog);
    addMouseMoveListener(parentShellRelocator);
    addMouseListener(parentShellRelocator);

    addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
        SimpleMessageOverlay.this.getParent().removeControlListener(SimpleMessageOverlay.this);
      }
    });
  }

  public Runnable getAction()
  {
    return action;
  }

  public Runnable getCloseAction()
  {
    return closeAction;
  }

  public Type getType()
  {
    return type;
  }

  public boolean isDismissAutomatically()
  {
    return dismissAutomatically;
  }

  @Override
  public void setVisible(boolean visible)
  {
    super.setVisible(visible);

    if (firstShown && visible && dismissAutomatically)
    {
      firstShown = false;

      final Display display = getDisplay();
      Thread dismissThread = new Thread(new Runnable()
      {
        public void run()
        {
          try
          {
            Thread.sleep(DEFAULT_AUTO_DISMISS_MILLIS);
          }
          catch (InterruptedException ex)
          {
            // Ignore.
          }

          display.asyncExec(new Runnable()
          {
            public void run()
            {
              dialog.clearMessage();
            }
          });
        }
      });

      dismissThread.setDaemon(true);
      dismissThread.start();
    }
  }

  @Override
  protected void checkSubclass()
  {
    // Nothing to do
  }

  public void setMessage(String message)
  {
    String tmp = message;
    String actionLabel = action instanceof RunnableWithLabel ? ((RunnableWithLabel)action).getLabel() : null;

    int actionLabelStartIndex = Integer.MIN_VALUE;
    int actionLabelLength = Integer.MIN_VALUE;

    if (actionLabel != null)
    {
      tmp += " " + actionLabel;
      actionLabelStartIndex = message.length() + 1;
      actionLabelLength = actionLabel.length();
    }

    if (tmp.length() > MAX_MESSAGE_LENGTH)
    {
      if (actionLabel != null)
      {
        try
        {
          tmp = StringUtil.shorten(message, MAX_MESSAGE_LENGTH - actionLabel.length() - 1, false);
          actionLabelStartIndex = tmp.length() + 1;
          tmp += " " + actionLabel;
          actionLabelLength = actionLabel.length();
        }
        catch (IllegalArgumentException ex)
        {
          // Could happen if we have really long action labels: discard the action label text.
          tmp = StringUtil.shorten(message, MAX_MESSAGE_LENGTH, false);
          actionLabelStartIndex = Integer.MIN_VALUE;
          actionLabelLength = Integer.MIN_VALUE;
        }
      }
      else
      {
        tmp = StringUtil.shorten(message, MAX_MESSAGE_LENGTH, false);
      }

      String wrapText = StringUtil.wrapText(message, MAX_TOOLTIP_LINE_LENGTH, true);
      wrapText = ensureMaxLineLength(message, wrapText, MAX_TOOLTIP_LINE_LENGTH);

      text.setToolTipText(wrapText);
    }
    else
    {
      text.setToolTipText(null);
    }

    text.setText(tmp);

    if (actionLabelStartIndex >= 0)
    {
      StyleRange range = new StyleRange(actionLabelStartIndex, actionLabelLength, type.foregroundColor, type.backgroundColor);
      range.underline = true;
      text.setStyleRange(range);
    }

    layout();
  }

  private String ensureMaxLineLength(String originalText, String wrappedText, int maxLineLength)
  {
    String[] lines = wrappedText.contains(StringUtil.NL) ? wrappedText.split(StringUtil.NL) : new String[] { wrappedText };

    for (String line : lines)
    {
      if (line.length() > maxLineLength)
      {
        wrappedText = StringUtil.wrapText(originalText, maxLineLength, false);
        break;
      }
    }

    return wrappedText;
  }

  public void controlResized(ControlEvent e)
  {
    if (!isDisposed())
    {
      controlRelocator.relocate(this);
    }
  }

  public void controlMoved(ControlEvent e)
  {
    if (!isDisposed())
    {
      controlRelocator.relocate(this);
    }
  }

  /**
   * @author Andreas Scharf
   */
  public static enum Type
  {
    ERROR(SetupInstallerPlugin.getColor(249, 54, 50), AbstractSimpleDialog.COLOR_WHITE, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message_hover.png")), //
    SUCCESS(SetupInstallerPlugin.getColor(58, 195, 4), AbstractSimpleDialog.COLOR_WHITE, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message_hover.png")), //
    WARNING(SetupInstallerPlugin.getColor(240, 173, 78), AbstractSimpleDialog.COLOR_WHITE,
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message.png"), SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message_hover.png"));

    public final Color backgroundColor;

    public final Color foregroundColor;

    public final Image closeImg;

    public final Image closeImgHover;

    private Type(Color backgroundColor, Color foregroundColor, Image closeImg, Image closeImgHover)
    {
      this.backgroundColor = backgroundColor;
      this.foregroundColor = foregroundColor;
      this.closeImg = closeImg;
      this.closeImgHover = closeImgHover;
    }
  }

  /**
   * @author Andreas Scharf
   */
  public static interface ControlRelocator
  {
    public void relocate(Control control);
  }

  /**
   *
   * @author Andreas Scharf
   */
  public static interface RunnableWithLabel extends Runnable
  {
    public String getLabel();
  }

  /**
   * @author Andreas Scharf
   */
  private class ParentShellRelocator extends MouseAdapter implements MouseMoveListener
  {
    private Shell shellToMove;

    private boolean move;

    private Point lastPosition;

    public ParentShellRelocator(Shell shellToMove)
    {
      this.shellToMove = shellToMove;
    }

    @Override
    public void mouseDown(MouseEvent e)
    {
      move = true;
      lastPosition = toDisplay(e.x, e.y);
    }

    @Override
    public void mouseUp(MouseEvent e)
    {
      move = false;
    }

    public void mouseMove(final MouseEvent e)
    {
      if (move)
      {
        Point currentPosition = toDisplay(e.x, e.y);

        int deltaX = currentPosition.x - lastPosition.x;
        int deltaY = currentPosition.y - lastPosition.y;

        lastPosition = currentPosition;

        Point currentShellLocation = shellToMove.getLocation();
        int newX = currentShellLocation.x + deltaX;
        int newY = currentShellLocation.y + deltaY;

        shellToMove.setLocation(newX, newY);
      }
    }

  }
}
