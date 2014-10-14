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
package org.eclipse.oomph.setup.ui.questionaire;

import org.eclipse.oomph.setup.ui.questionaire.AnimatedCanvas.Animator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class ExitShell extends AnimatedShell
{
  private static final int HEIGHT = 200;

  private static final int WIDTH = 300;

  public ExitShell(Shell parent)
  {
    super(parent, SWT.APPLICATION_MODAL);

    Rectangle bounds = parent.getBounds();
    setLocation(bounds.x + (bounds.width - WIDTH) / 2, bounds.y + (bounds.height - HEIGHT) / 2);
    setSize(WIDTH, HEIGHT);
  }

  @Override
  protected void init()
  {
    super.init();

    ExitAnimator animator = new ExitAnimator(getDisplay());
    getCanvas().addAnimator(animator);
  }

  /**
   * @author Eike Stepper
   */
  private class ExitAnimator extends Animator
  {
    public ExitAnimator(Display display)
    {
      super(display);
    }

    @Override
    protected boolean onKeyPressed(KeyEvent e)
    {
      if (e.keyCode == SWT.ESC)
      {
        ExitShell.this.dispose();
        return true;
      }

      return super.onKeyPressed(e);
    }

    @Override
    protected boolean advance()
    {
      return false;
    }

    @Override
    protected void paint(GC gc, Image buffer)
    {
    }
  }
}
