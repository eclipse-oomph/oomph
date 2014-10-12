package org.eclipse.oomph.setup.ui.questionaire;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class AnimatorTest
{
  private static final int BORDER = 30;

  public static void main(String[] args)
  {
    Display display = new Display();
    final Shell shell = new Shell(display, SWT.NONE);
    shell.setText("Eclipse Oomph");

    Font initialFont = shell.getFont();
    FontData[] fontData = initialFont.getFontData();
    for (int i = 0; i < fontData.length; i++)
    {
      fontData[i].setHeight(16);
      fontData[i].setStyle(SWT.BOLD);
    }

    Font newFont = new Font(display, fontData);
    final GearAnimator animator = new GearAnimator(display, newFont);

    int width = Math.max(animator.getWidth(), GearAnimator.PAGE_WIDTH) + 2 * BORDER;
    int height = animator.getHeight() + GearAnimator.PAGE_HEIGHT + 3 * BORDER;
    Rectangle displayBounds = display.getBounds();

    shell.setBounds(shell.getBounds().x, (displayBounds.height - height) / 2, width, height);
    shell.setLayout(new FillLayout());
    shell.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.keyCode == SWT.ESC)
        {
          shell.dispose();
        }
        else if (e.keyCode == SWT.ARROW_RIGHT)
        {
          animator.setSelection(animator.getSelection() + 1);
        }
        else if (e.keyCode == SWT.ARROW_LEFT)
        {
          animator.setSelection(animator.getSelection() - 1);
        }
        else if (e.character >= '0' && e.character <= '6')
        {
          animator.setSelection(e.character - '0');
        }
        else
        {
          super.keyPressed(e);
        }
      }
    });

    AnimatedCanvas canvas = new AnimatedCanvas(shell, SWT.NONE);
    canvas.addAnimator(animator);

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
