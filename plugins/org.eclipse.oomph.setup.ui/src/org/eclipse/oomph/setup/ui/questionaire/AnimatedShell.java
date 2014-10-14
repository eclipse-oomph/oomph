package org.eclipse.oomph.setup.ui.questionaire;

import org.eclipse.oomph.setup.ui.questionaire.GearAnimator.Answer;
import org.eclipse.oomph.setup.ui.questionaire.GearAnimator.Listener;
import org.eclipse.oomph.setup.ui.questionaire.GearAnimator.Page;
import org.eclipse.oomph.setup.ui.questionaire.GearAnimator.QuestionPage;
import org.eclipse.oomph.setup.ui.questionaire.GearAnimator.SummaryPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class AnimatedShell extends Shell implements Listener
{
  private AnimatedCanvas canvas;

  private GearAnimator animator;

  public AnimatedShell()
  {
    init();
  }

  public AnimatedShell(Display display, int style)
  {
    super(display, style);
    init();
  }

  public AnimatedShell(Display display)
  {
    super(display);
    init();
  }

  public AnimatedShell(int style)
  {
    super(style);
    init();
  }

  public AnimatedShell(Shell parent, int style)
  {
    super(parent, style);
    init();
  }

  public AnimatedShell(Shell parent)
  {
    super(parent);
    init();
  }

  @Override
  protected void checkSubclass()
  {
    // Do nothing.
  }

  public final GearAnimator getAnimator()
  {
    return animator;
  }

  public void onAnswer(GearAnimator animator, Page page, Answer answer)
  {
  }

  public void onExit(GearAnimator animator, Page page)
  {
  }

  private void init()
  {
    Display display = getDisplay();

    Font initialFont = getFont();
    FontData[] fontData = initialFont.getFontData();
    for (int i = 0; i < fontData.length; i++)
    {
      fontData[i].setHeight(16);
      fontData[i].setStyle(SWT.BOLD);
    }

    Font newFont = new Font(display, fontData);
    animator = new GearAnimator(display, newFont);
    animator.addListener(this);

    int width = Math.max(animator.getWidth(), GearAnimator.PAGE_WIDTH) + 2 * GearAnimator.BORDER;
    int height = animator.getHeight() + GearAnimator.PAGE_HEIGHT + 3 * GearAnimator.BORDER;
    setSize(width, height);

    setLayout(new FillLayout());
    addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (!onKeyPressed(e))
        {
          super.keyPressed(e);
        }
      }
    });

    canvas = new AnimatedCanvas(this, SWT.NONE);
    canvas.addAnimator(animator);

    addFocusListener(new FocusListener()
    {
      public void focusGained(FocusEvent e)
      {
        canvas.setFocus(true);
      }

      public void focusLost(FocusEvent e)
      {
        canvas.setFocus(false);
      }
    });
  }

  protected boolean onKeyPressed(KeyEvent e)
  {
    if (e.keyCode == SWT.ESC)
    {
      AnimatedShell.this.dispose();
      return true;
    }

    if (e.keyCode == SWT.HOME)
    {
      animator.setSelection(0);
      return true;
    }

    if (e.keyCode == SWT.END)
    {
      animator.setSelection(GearAnimator.GEARS);
      return true;
    }

    if (e.keyCode == SWT.ARROW_RIGHT)
    {
      animator.setSelection(animator.getSelection() + 1);
      return true;
    }

    if (e.keyCode == SWT.ARROW_LEFT)
    {
      animator.setSelection(animator.getSelection() - 1);
      return true;
    }

    if (e.character >= '0' && e.character <= '6')
    {
      animator.setSelection(e.character - '0');
      return true;
    }

    return false;
  }

  private static final boolean TEST_OVERLAYS = false;

  public static void main(String[] args)
  {
    final Display display = new Display();

    AnimatedShell shell = new AnimatedShell(display, SWT.NONE)
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
    shell.open();
    while (!shell.isDisposed())
    {
      if (!display.readAndDispatch())
      {
        display.sleep();
      }
    }

    display.dispose();
  }
}
