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

import org.eclipse.oomph.setup.ui.questionaire.GearAnimator.Answer;
import org.eclipse.oomph.setup.ui.questionaire.GearAnimator.Listener;
import org.eclipse.oomph.setup.ui.questionaire.GearAnimator.Page;
import org.eclipse.oomph.setup.ui.questionaire.GearAnimator.SummaryPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class GearShell extends AnimatedShell implements Listener
{
  private static final boolean TEST_OVERLAYS = false;

  private GearAnimator animator;

  public GearShell(Display display)
  {
    super(display, SWT.APPLICATION_MODAL);
  }

  public GearShell(Shell parent)
  {
    super(parent, SWT.APPLICATION_MODAL);
  }

  public final GearAnimator getAnimator()
  {
    return animator;
  }

  public void onAnswer(GearAnimator animator, Page page, Answer answer)
  {
    // Do nothing.
  }

  public void onExit(GearAnimator animator, Page page)
  {
    dispose();
  }

  @Override
  protected void init()
  {
    super.init();
    Display display = getDisplay();

    animator = new GearAnimator(display)
    {
      @Override
      protected boolean onKeyPressed(KeyEvent e)
      {
        if (TEST_OVERLAYS)
        {
          GearAnimator animator = getAnimator();
          Page page = animator.getSelectedPage();

          if (page instanceof QuestionPage)
          {
            if (e.keyCode == SWT.ARROW_RIGHT)
            {
              animator.updateOverlay(1, 0);
              return true;
            }

            if (e.keyCode == SWT.ARROW_LEFT)
            {
              animator.updateOverlay(-1, 0);
              return true;
            }

            if (e.keyCode == SWT.ARROW_DOWN)
            {
              animator.updateOverlay(0, 1);
              return true;
            }

            if (e.keyCode == SWT.ARROW_UP)
            {
              animator.updateOverlay(0, -1);
              return true;
            }
          }
        }

        return super.onKeyPressed(e);
      }
    };

    animator.addListener(this);

    getCanvas().addAnimator(animator);

    int width = Math.max(animator.getWidth(), GearAnimator.PAGE_WIDTH) + 2 * GearAnimator.BORDER;
    int height = animator.getHeight() + GearAnimator.PAGE_HEIGHT + 3 * GearAnimator.BORDER;
    setSize(width, height);
  }

  public static void main(String[] args)
  {
    final Display display = new Display();

    GearShell shell = new GearShell(display)
    {
      @Override
      public void onAnswer(GearAnimator animator, Page page, Answer answer)
      {
        if (page instanceof SummaryPage)
        {
          System.out.println("Finish:");
          Page[] pages = animator.getPages();
          for (int i = 1; i < GearAnimator.GEARS; i++)
          {
            System.out.println("  " + pages[i].getTitle() + " = " + pages[i].getChoice());
          }

          display.dispose();
        }
      }
    };

    shell.setText("Questionnaire Test");
    shell.openModal();

    display.dispose();
  }
}
