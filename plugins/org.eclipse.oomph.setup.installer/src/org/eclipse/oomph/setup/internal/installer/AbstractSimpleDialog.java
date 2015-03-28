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
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.ui.ShellMove;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public abstract class AbstractSimpleDialog extends Shell
{
  public static final Color WHITE = UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE);

  private static final ShellMove SHELL_MOVE = new ShellMove()
  {
    @Override
    public void hookControl(Control control)
    {
      control.setBackground(WHITE);
      super.hookControl(control);
    }

    @Override
    protected boolean shouldHookControl(Control control)
    {
      return super.shouldHookControl(control) || control instanceof SimpleInstallerPage;
    }
  };

  private Composite titleComposite;

  private int returnCode = Window.OK;

  public AbstractSimpleDialog(Display display, int style, int width, int height, int marginWidth, int marginHeight)
  {
    super(display, style);

    GridLayout verticalLayout = UIUtil.createGridLayout(1);
    verticalLayout.verticalSpacing = 20;

    setLayout(verticalLayout);
    setSize(width, height);
    setImages(Window.getDefaultImages());
    setText(AbstractSetupDialog.SHELL_TEXT);

    Rectangle bounds = display.getPrimaryMonitor().getBounds();
    setLocation(bounds.x + (bounds.width - width) / 2, bounds.y + (bounds.height - height) / 2);

    addTraverseListener(new TraverseListener()
    {
      public void keyTraversed(TraverseEvent e)
      {
        if (e.detail == SWT.TRAVERSE_ESCAPE)
        {
          exitSelected();
          e.detail = SWT.TRAVERSE_NONE;
          e.doit = false;
        }
      }
    });

    GridLayout titleLayout = UIUtil.createGridLayout(4);
    titleLayout.marginTop = marginHeight;
    titleLayout.marginWidth = marginWidth;
    titleLayout.horizontalSpacing = 0;

    titleComposite = new Composite(this, SWT.NONE);
    titleComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
    titleComposite.setLayout(titleLayout);

    Label titleImage = new Label(titleComposite, SWT.NONE);
    titleImage.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false));
    titleImage.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/title.png"));
  }

  public int show()
  {
    createUI(titleComposite);
    hook(this);

    open();

    Display display = getDisplay();
    while (!isDisposed())
    {
      if (!display.readAndDispatch())
      {
        display.sleep();
      }
    }

    return returnCode;
  }

  protected final int getReturnCode()
  {
    return returnCode;
  }

  protected final void setReturnCode(int returnCode)
  {
    this.returnCode = returnCode;
  }

  protected abstract void createUI(Composite titleComposite);

  protected void exitSelected()
  {
    dispose();
  }

  @Override
  protected void checkSubclass()
  {
    // Do nothing.
  }

  public static void hook(Control control)
  {
    SHELL_MOVE.hookControl(control);
  }
}
