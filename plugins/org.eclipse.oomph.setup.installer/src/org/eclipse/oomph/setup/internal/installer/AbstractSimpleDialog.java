/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Yatta Solutions - [466264] Enhance UX in simple installer
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.ui.MouseHandler;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
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
  public static final Color COLOR_WHITE = UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE);

  public static final Color COLOR_LIGHTEST_GRAY = SetupInstallerPlugin.getColor(245, 245, 245);

  public static final Color COLOR_LABEL_FOREGROUND = SetupInstallerPlugin.getColor(85, 85, 85);

  private Composite titleComposite;

  private int returnCode = Window.OK;

  public AbstractSimpleDialog(Display display, int style, int width, int height)
  {
    super(display, style);

    GridLayout verticalLayout = UIUtil.createGridLayout(1);
    verticalLayout.marginWidth = 1;
    verticalLayout.marginHeight = 1;
    verticalLayout.verticalSpacing = 0;

    setLayout(verticalLayout);
    setSize(width, height);
    setImages(Window.getDefaultImages());
    setText(PropertiesUtil.getProductName());

    setBackground(SetupInstallerPlugin.getColor(207, 207, 207));

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

    GridLayout titleLayout = UIUtil.createGridLayout(2);
    titleLayout.horizontalSpacing = 0;
    titleLayout.verticalSpacing = 0;
    titleLayout.marginLeft = 20;
    titleLayout.marginRight = 14;

    titleComposite = new Composite(this, SWT.NONE);
    titleComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).hint(SWT.DEFAULT, 72).create());
    titleComposite.setLayout(titleLayout);
    titleComposite.setBackgroundMode(SWT.INHERIT_FORCE);
    titleComposite.setBackground(AbstractSimpleDialog.COLOR_LIGHTEST_GRAY);

    Label titleImage = new Label(titleComposite, SWT.NONE);
    titleImage.setLayoutData(GridDataFactory.swtDefaults().grab(true, true).indent(SWT.DEFAULT, 26).align(SWT.BEGINNING, SWT.BEGINNING).create());
    titleImage.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/title.png"));
  }

  public int show()
  {
    createUI(titleComposite);

    new MouseHandler.MoveAndResize(this)
    {
      @Override
      protected boolean shouldHookControl(Control control)
      {
        return super.shouldHookControl(control) || control instanceof SimpleInstallerPage;
      }
    };

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
}
