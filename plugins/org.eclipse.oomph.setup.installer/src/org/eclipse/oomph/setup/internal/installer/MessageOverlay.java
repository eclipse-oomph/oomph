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

import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Andreas Scharf
 */
public class MessageOverlay extends Shell implements ControlListener
{
  private static final int DEFAULT_AUTO_DISMISS_MILLIS = 4 * 1000;

  private static final int MAX_MESSAGE_LENGTH = 175;

  private static final int MAX_TOOLTIP_LINE_LENGTH = 60;

  private final SimpleInstallerDialog dialog;

  private final ControlRelocator controlRelocator;

  private final boolean dismissAutomatically;

  private Link link;

  private boolean firstShown = true;

  public MessageOverlay(SimpleInstallerDialog dialog, Type type, ControlRelocator controlRelocator, boolean dismissAutomatically)
  {
    this(dialog, type, controlRelocator, dismissAutomatically, null);
  }

  public MessageOverlay(SimpleInstallerDialog dialog, Type type, ControlRelocator controlRelocator, boolean dismissAutomatically, final Runnable action)
  {
    super(dialog, SWT.NO_TRIM);

    if (type == null)
    {
      throw new IllegalArgumentException("Type must not be null!");
    }

    if (controlRelocator == null)
    {
      throw new IllegalArgumentException("Control relocator must not be null!");
    }

    this.dialog = dialog;
    this.controlRelocator = controlRelocator;
    this.dismissAutomatically = dismissAutomatically;

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

    link = new Link(this, SWT.NONE);
    link.setLayoutData(GridDataFactory.swtDefaults().grab(true, true).create());
    link.setFont(SimpleInstallerDialog.getDefaultFont());
    link.setForeground(type.foregroundColor);
    if (action != null)
    {
      link.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          action.run();
          close();
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
        close();
      }
    });

    // Initial bounds
    controlRelocator.relocate(this);
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
  public void dispose()
  {
    getParent().removeControlListener(this);
    super.dispose();
  }

  @Override
  protected void checkSubclass()
  {
    // Nothing to do
  }

  public void setMessage(String message)
  {
    String tmp = message;
    int maxMessageLength = MAX_MESSAGE_LENGTH;
    if (message.length() > maxMessageLength)
    {
      tmp = StringUtil.shorten(message, maxMessageLength, false);

      String wrapText = StringUtil.wrapText(message, MAX_TOOLTIP_LINE_LENGTH, true);
      wrapText = ensureMaxLineLength(message, wrapText, MAX_TOOLTIP_LINE_LENGTH);

      link.setToolTipText(wrapText);
    }
    else
    {
      link.setToolTipText(null);
    }

    link.setText(tmp);
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
  public static interface ControlRelocator
  {
    public void relocate(Control control);
  }

  /**
   * @author Andreas Scharf
   */
  public static enum Type
  {
    ERROR(SetupInstallerPlugin.getColor(249, 54, 50), AbstractSimpleDialog.COLOR_WHITE, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message_hover.png")), SUCCESS(SetupInstallerPlugin.getColor(58, 195, 4),
            AbstractSimpleDialog.COLOR_WHITE, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message.png"),
            SetupInstallerPlugin.INSTANCE.getSWTImage("simple/close_message_hover.png"));

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
}
