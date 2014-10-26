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
package org.eclipse.oomph.ui;

import org.eclipse.oomph.internal.ui.UIPlugin;
import org.eclipse.oomph.internal.util.HTTPServer;

import org.eclipse.jface.dialogs.DialogTray;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class HelpSupport
{
  private static final String HELP_CONTEXT = "/help";

  private final TitleAreaDialog dialog;

  private final Set<Control> calloutControls = new HashSet<Control>();

  private final Image[] calloutImages = new Image[10];

  private final InactivityDetector inactivityDetector = new InactivityDetector(1000, 10000)
  {
    @Override
    protected void handleInactivity(Display display, boolean inactive)
    {
      if (inactive)
      {
        display.asyncExec(helpAnimator);
      }
    }
  };

  private final HelpAnimator helpAnimator = new HelpAnimator();

  private ToolItem helpButton;

  private HTTPServer helpServer;

  private Browser helpBrowser;

  public HelpSupport(TitleAreaDialog dialog)
  {
    this.dialog = dialog;
    dialog.setHelpAvailable(true);
  }

  public final void hook(final ToolItem helpButton)
  {
    this.helpButton = helpButton;

    Shell shell = dialog.getShell();
    shell.addHelpListener(new HelpListener()
    {
      public void helpRequested(HelpEvent e)
      {
        if (dialog.getTray() != null)
        {
          dialog.closeTray();
          helpButton.setSelection(false);
          return;
        }

        DialogTray tray = new DialogTray()
        {
          @Override
          protected Control createContents(Composite parent)
          {
            helpBrowser = new Browser(parent, SWT.NONE);
            helpBrowser.setSize(500, 800);
            helpBrowser.addDisposeListener(new DisposeListener()
            {
              public void widgetDisposed(DisposeEvent e)
              {
                helpBrowser = null;
                redrawCalloutControls();
              }
            });

            updateHelp();
            return helpBrowser;
          }
        };

        dialog.openTray(tray);
        helpButton.setSelection(true);
        redrawCalloutControls();
      }
    });

    detectInactivity(shell);
  }

  public final boolean isHelpOpen()
  {
    return helpBrowser != null;
  }

  public final void updateHelp()
  {
    if (dialog instanceof HelpProvider)
    {
      HelpProvider helpProvider = (HelpProvider)dialog;
      String helpPath = helpProvider.getHelpPath();
      if (helpPath != null)
      {
        setHelpPath(helpPath);
      }
    }
  }

  public final void addHelpCallout(final Control control, final int number)
  {
    control.addPaintListener(new PaintListener()
    {
      public void paintControl(PaintEvent e)
      {
        if (isHelpOpen())
        {
          Image image = getCalloutImage(number);
          if (image != null)
          {
            Rectangle bounds = getBounds(control);
            e.gc.drawImage(image, bounds.width - 31, bounds.y + 10);
          }
        }
      }
    });

    control.addControlListener(new ControlAdapter()
    {
      @Override
      public void controlResized(ControlEvent e)
      {
        if (isHelpOpen())
        {
          control.redraw();
        }
      }
    });

    if (control instanceof Scrollable)
    {
      Scrollable scrollable = (Scrollable)control;
      ScrollBar verticalBar = scrollable.getVerticalBar();
      ScrollBar horizontalBar = scrollable.getHorizontalBar();
      if (verticalBar != null || horizontalBar != null)
      {
        SelectionAdapter listener = new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            if (isHelpOpen())
            {
              control.redraw();
            }
          }
        };

        if (verticalBar != null)
        {
          verticalBar.addSelectionListener(listener);
        }

        if (horizontalBar != null)
        {
          horizontalBar.addSelectionListener(listener);
        }
      }
    }

    calloutControls.add(control);
  }

  public final void detectInactivity(Control control)
  {
    inactivityDetector.monitor(control);
  }

  public void dispose()
  {
    if (helpServer != null)
    {
      try
      {
        helpServer.stop();
      }
      catch (Exception ex)
      {
        UIPlugin.INSTANCE.log(ex);
      }

      helpServer = null;
    }

    for (int i = 0; i < calloutImages.length; i++)
    {
      if (calloutImages[i] != null)
      {
        calloutImages[i].dispose();
        calloutImages[i] = null;
      }
    }
  }

  private Image getCalloutImage(int number)
  {
    if (calloutImages[number] == null)
    {
      calloutImages[number] = UIPlugin.INSTANCE.getSWTImage("callout-" + number + ".png");
    }

    return calloutImages[number];
  }

  private void redrawCalloutControls()
  {
    for (Control control : calloutControls)
    {
      if (!control.isDisposed() && control.isVisible())
      {
        control.redraw();
      }
    }
  }

  private synchronized String getHelpURL(String path)
  {
    if (helpServer == null)
    {
      try
      {
        helpServer = new HTTPServer();
        helpServer.addContext(new HTTPServer.PluginContext(HELP_CONTEXT, true));
      }
      catch (Exception ex)
      {
        UIPlugin.INSTANCE.log(ex);
        return null;
      }
    }

    return "http://localhost:" + helpServer.getPort() + HELP_CONTEXT + path;
  }

  private void setHelpPath(String path)
  {
    if (helpBrowser != null)
    {
      String url = getHelpURL(path);
      if (url != null)
      {
        if (!url.equals(helpBrowser.getUrl()))
        {
          helpBrowser.setUrl(url);
        }
      }
      else
      {
        helpBrowser.setText("<h3>Help content not found.</h3>");
      }
    }
  }

  private static Rectangle getBounds(final Control control)
  {
    Rectangle bounds = control instanceof Scrollable ? ((Scrollable)control).getClientArea() : control.getBounds();
    --bounds.width;
    --bounds.height;

    int y = getHeaderHeight(control);
    bounds.y += y;
    bounds.height -= y;
    return bounds;
  }

  private static int getHeaderHeight(Control control)
  {
    if (control instanceof Tree)
    {
      return ((Tree)control).getHeaderHeight();
    }

    if (control instanceof Table)
    {
      return ((Table)control).getHeaderHeight();
    }

    return 0;
  }

  /**
   * @author Eike Stepper
   */
  public interface HelpProvider
  {
    public String getHelpPath();
  }

  /**
   * @author Eike Stepper
   */
  private final class HelpAnimator implements Runnable
  {
    private Image[] images;

    private int index;

    private boolean backward;

    public void run()
    {
      Shell shell = dialog.getShell();
      if (shell != null && !shell.isDisposed())
      {
        try
        {
          if (images == null)
          {
            initImages(shell);
          }

          if (inactivityDetector.isInactive() && !isHelpOpen())
          {
            helpButton.setImage(images[Math.max(0, index)]);

            if (backward)
            {
              if (--index == -10)
              {
                backward = false;
              }
            }
            else
            {
              if (++index == images.length)
              {
                index = images.length - 2;
                backward = true;
              }
            }

            shell.getDisplay().timerExec(40, this);
          }
          else
          {
            index = 0;
            backward = false;
            helpButton.setImage(images[index]);
          }
        }
        catch (SWTException ex)
        {
          if (!shell.isDisposed())
          {
            throw ex;
          }
        }
      }
    }

    private void initImages(Shell shell)
    {
      images = new Image[11];
      images[0] = helpButton.getImage();
      for (int i = 0; i < images.length - 1; i++)
      {
        images[i + 1] = UIPlugin.INSTANCE.getSWTImage("help" + i);
      }

      shell.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          for (int i = 0; i < images.length - 1; i++)
          {
            images[i + 1].dispose();
          }
        }
      });
    }
  }
}
