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
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scrollable;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Ed Merks
 */
public abstract class Capture<T>
{
  protected abstract T create(Shell shell);

  protected abstract Shell getShell(T element);

  protected abstract void open(T element);

  public Image capture()
  {
    final AtomicReference<Image> image = new AtomicReference<Image>();
    final Display display = PlatformUI.getWorkbench().getDisplay();
    final org.eclipse.jface.dialogs.Dialog backgroundDialog = new org.eclipse.jface.dialogs.Dialog((Shell)null)
    {
      {
        setShellStyle(SWT.MAX);
      }

      @Override
      protected Control createDialogArea(Composite parent)
      {
        Control control = super.createDialogArea(parent);
        control.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
        return control;
      }
    };

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        backgroundDialog.open();
      }
    });

    while (backgroundDialog.getShell() == null)
    {
      try
      {
        Thread.sleep(100);
      }
      catch (InterruptedException ex)
      {
        // Ignore.
      }
    }

    final Shell backgroundShell = backgroundDialog.getShell();

    final T element = new WorkUnit<T, RuntimeException>()
    {
      @Override
      protected T doExecute()
      {
        backgroundShell.setMaximized(true);
        return create(backgroundShell);
      }
    }.execute();

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        open(element);
      }
    });

    while (!new WorkUnit<Boolean, RuntimeException>()
    {
      @Override
      protected Boolean doExecute()
      {
        return isReady(element);
      }
    }.execute())
    {
      try
      {
        Thread.sleep(100);
      }
      catch (InterruptedException ex)
      {
        // Ignore.
      }
    }

    try
    {
      Thread.sleep(1000);
    }
    catch (InterruptedException ex)
    {
      // Ignore.
    }

    new WorkUnit.Void<RuntimeException>()
    {
      @Override
      protected void doProcess()
      {
        preprocess(element);
      }
    }.execute();

    new WorkUnit.Void<RuntimeException>()
    {
      @Override
      protected void doProcess()
      {
        postProcess(element);
      }
    }.execute();

    new WorkUnit.Void<RuntimeException>()
    {
      @Override
      protected void doProcess()
      {
        image.set(capture(element));
      }
    }.execute();

    new WorkUnit.Void<RuntimeException>()
    {
      @Override
      protected void doProcess()
      {
        close(element);
        backgroundShell.close();
      }
    }.execute();

    return image.get();
  }

  protected Control getControl(T element)
  {
    return getShell(element);
  }

  protected boolean isReady(T element)
  {
    Shell shell = getShell(element);
    if (shell == null)
    {
      return false;
    }

    if (shell.getShells().length > 1)
    {
      return false;
    }

    return true;
  }

  protected void preprocess(T element)
  {
  }

  protected void postProcess(T element)
  {
    Button defaultButton = getShell(element).getDefaultButton();
    if (defaultButton != null)
    {
      ReflectUtil.invokeMethod(ReflectUtil.getMethod(Button.class, "setDefault", boolean.class), defaultButton, false);
    }
  }

  protected Image capture(T element)
  {
    return AccessUtil.capture(getControl(element));
  }

  protected Image capture(IDialogPage page, Map<Control, Image> decorations)
  {
    Control control = page.getControl();

    // Get the control of the overall page.
    Composite pageControl = control.getParent().getParent().getParent();
    Control[] children = pageControl.getChildren();

    // Get the overall wizard control.
    Composite wizardControl = pageControl.getParent();

    // Compute the bounds to capture by extending the bounds of the page control to include the title area but to exclude the bottom button area.
    Rectangle bounds = pageControl.getBounds();
    Point size = children[1].getSize();
    bounds.height += bounds.y - 2;
    bounds.y = 0;
    bounds.height -= size.y;

    Point displayOffset = wizardControl.toDisplay(bounds.x, bounds.y);
    Image result = AccessUtil.capture(control.getDisplay(), new Rectangle(displayOffset.x, displayOffset.y, bounds.width, bounds.height));
    if (decorations != null && !decorations.isEmpty())
    {
      decorate(result, wizardControl, bounds, decorations);
    }

    return result;
  }

  protected void decorate(Image image, Control parent, Rectangle bounds, Map<Control, Image> decorations)
  {
    GC gc = new GC(image);
    for (Entry<Control, Image> entry : decorations.entrySet())
    {
      Control control = entry.getKey();
      Image decoration = entry.getValue();
      if (decoration != null)
      {
        Rectangle controlBounds = control.getBounds();

        int border = control.getBorderWidth();

        Point controlLocation = control.toDisplay(controlBounds.x + controlBounds.width - 1 - border, controlBounds.y - border);
        Point relativeControlLocation = parent.toControl(controlLocation);

        Rectangle imageBounds = decoration.getBounds();

        imageBounds.x = bounds.x + relativeControlLocation.x - (imageBounds.width - 1) - 11;
        imageBounds.y = bounds.y + relativeControlLocation.y + 11;

        if (control instanceof Scrollable)
        {
          Scrollable scrollable = (Scrollable)control;
          Rectangle clientArea = scrollable.getClientArea();
          imageBounds.x -= controlBounds.width - clientArea.width - 2 * border;
        }

        gc.drawImage(decoration, imageBounds.x, imageBounds.y);

        Display display = parent.getDisplay();
        gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
        gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
      }
    }

    gc.dispose();
  }

  private Image getImage(final Control control)
  {
    Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);

    if (control instanceof Combo)
    {
      Combo combo = (Combo)control;
      GC gc = new GC(control);
      Point textExtent = gc.textExtent(combo.getText());
      size.x = textExtent.x + 30;
      gc.dispose();
    }

    control.setSize(size);
    AccessUtil.busyWait(10);
    Image result = AccessUtil.capture(control);
    return result;
  }

  protected <W extends Widget> W getWidget(T element, String key)
  {
    return AccessUtil.getWidget(getShell(element), key);
  }

  protected Image getImage(T element, String key)
  {
    Widget widget = AccessUtil.getWidget(getShell(element), key);
    if (widget instanceof ToolItem)
    {
      ToolItem toolItem = (ToolItem)widget;
      Image image = toolItem.getImage();
      return new Image(image.getDevice(), image.getImageData());
    }

    if (widget instanceof Button)
    {
      Button button = (Button)widget;
      Shell shell = button.getShell();
      Color background = button.getBackground();
      boolean isEnabled = button.isEnabled();

      try
      {
        int style = button.getStyle();
        if ((style & SWT.PUSH) != 0)
        {
          button.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        }

        button.setEnabled(true);
        AccessUtil.busyWait(1000);
        return AccessUtil.capture(button);
      }
      finally
      {
        button.setBackground(background);
        button.setEnabled(isEnabled);
      }
    }

    if (widget instanceof Text)
    {
      Text text = (Text)widget;
      return getImage(text);
    }

    if (widget instanceof Combo)
    {
      Combo combo = (Combo)widget;
      return getImage(combo);
    }

    if (widget instanceof Control)
    {
      Control control = (Control)widget;
      return AccessUtil.capture(control);
    }

    throw new UnsupportedOperationException("Unexpected widget " + widget);
  }

  protected void close(T element)
  {
    Shell shell = getShell(element);
    shell.close();
  }

  /**
   * @author Ed Merks
   */
  public static abstract class Window<W extends org.eclipse.jface.window.Window> extends Capture<W>
  {
    @Override
    protected Shell getShell(W window)
    {
      return window.getShell();
    }

    @Override
    protected void open(W window)
    {
      window.open();
    }
  }

  /**
   * @author Ed Merks
   */
  public static abstract class Dialog<D extends org.eclipse.swt.widgets.Dialog> extends Capture<D>
  {
    @Override
    protected Shell getShell(D dialog)
    {
      return dialog.getParent();
    }

    @Override
    protected void open(D dialog)
    {
      ReflectUtil.invokeMethod(ReflectUtil.getMethod(dialog.getClass(), "open"), dialog);
    }
  }
}
