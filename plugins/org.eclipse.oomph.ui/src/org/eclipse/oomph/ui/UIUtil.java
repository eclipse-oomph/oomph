/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
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
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class UIUtil
{
  public static final IWorkbench WORKBENCH;

  private static Image errorImage;

  private static Image warningImage;

  private static Image infoImage;

  private static Boolean browserAvailable;

  static
  {
    IWorkbench workbench = null;

    try
    {
      workbench = PlatformUI.getWorkbench();
    }
    catch (Throwable ex)
    {
      // Workbench has not been created.
    }

    WORKBENCH = workbench;
  }

  private UIUtil()
  {
  }

  public static Display getDisplay()
  {
    Display display = Display.getCurrent();
    if (display == null)
    {
      try
      {
        display = PlatformUI.getWorkbench().getDisplay();
      }
      catch (Throwable ignore)
      {
        //$FALL-THROUGH$
      }
    }

    if (display == null)
    {
      display = Display.getDefault();
    }

    if (display == null)
    {
      display = new Display();
    }

    return display;
  }

  public static Shell getShell()
  {
    final Shell[] shell = { null };

    final Display display = getDisplay();
    display.syncExec(new Runnable()
    {
      public void run()
      {
        shell[0] = display.getActiveShell();

        if (shell[0] == null)
        {
          try
          {
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            if (window == null)
            {
              IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
              if (windows.length != 0)
              {
                window = windows[0];
              }
            }

            if (window != null)
            {
              shell[0] = window.getShell();
            }
          }
          catch (Throwable ignore)
          {
            //$FALL-THROUGH$
          }
        }

        if (shell[0] == null)
        {
          Shell[] shells = display.getShells();
          if (shells.length > 0)
          {
            shell[0] = shells[0];
          }
        }
      }
    });

    return shell[0];
  }

  public static synchronized boolean isBrowserAvailable()
  {
    if (browserAvailable == null)
    {
      syncExec(new Runnable()
      {
        public void run()
        {
          Shell shell = null;

          try
          {
            shell = new Shell();
            new Browser(shell, SWT.NONE);
            browserAvailable = true;
          }
          catch (SWTError ex)
          {
            browserAvailable = false;
          }
          finally
          {
            try
            {
              shell.dispose();
            }
            catch (Exception ex)
            {
              // Ignore.
            }
          }
        }
      });
    }

    return browserAvailable;
  }

  /**
   * Checks if the given {@link Control} is a child of the given
   * parent.
   *
   * @param parent The parent, not <code>null</code>.
   * @param controlToCheck The control to check, not <code>null</code>.
   *
   * @return <code>true</code> if the given control is a child of the given
   * parent, <code>false</code> otherwise.
   */
  public static boolean isParent(Composite parent, Control controlToCheck)
  {
    if (parent == null || controlToCheck == null)
    {
      throw new IllegalArgumentException("Neither parent nor controlToCheck must be null");
    }

    if (controlToCheck == parent)
    {
      return true;
    }

    Composite tmpParent = controlToCheck.getParent();

    while (tmpParent != parent && tmpParent != null)
    {
      tmpParent = tmpParent.getParent();
    }

    return tmpParent == parent;
  }

  public static GridLayout createGridLayout(int numColumns)
  {
    GridLayout layout = new GridLayout(numColumns, false);
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    return layout;
  }

  public static GridData applyGridData(Control control)
  {
    GridData data = new GridData();
    data.grabExcessHorizontalSpace = true;
    data.horizontalAlignment = GridData.FILL;
    control.setLayoutData(data);
    return data;
  }

  public static GridData grabVertical(GridData data)
  {
    data.grabExcessVerticalSpace = true;
    data.verticalAlignment = GridData.FILL;
    return data;
  }

  public static void clearTextSelection(Object control)
  {
    Text text = findTextControl(control);
    if (text != null)
    {
      text.clearSelection();
    }
  }

  public static void setSelectionToEnd(Widget control)
  {
    Text text = findTextControl(control);
    if (text != null)
    {
      String content = text.getText();
      text.setSelection(content.length() + 1);
    }
  }

  public static void setSelectionTo(Widget control, Point selection)
  {
    Text text = findTextControl(control);
    if (text != null)
    {
      text.setSelection(selection);
    }
  }

  public static void selectAllText(Widget control)
  {
    Text text = findTextControl(control);
    if (text != null)
    {
      text.selectAll();
    }
  }

  private static Text findTextControl(Object control)
  {
    if (control instanceof Viewer)
    {
      control = ((Viewer)control).getControl();
    }

    if (control instanceof CCombo)
    {
      CCombo combo = (CCombo)control;

      try
      {
        control = ReflectUtil.getValue("text", combo);
      }
      catch (Throwable ex)
      {
        //$FALL-THROUGH$
      }
    }

    if (control instanceof Text)
    {
      return (Text)control;
    }

    return null;
  }

  public static void runInProgressDialog(Shell shell, IRunnableWithProgress runnable) throws InvocationTargetException, InterruptedException
  {
    ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell)
    {
      @Override
      protected Point getInitialSize()
      {
        Point calculatedSize = super.getInitialSize();
        if (calculatedSize.x < 800)
        {
          calculatedSize.x = 800;
        }

        return calculatedSize;
      }
    };

    try
    {
      dialog.run(true, true, runnable);
    }
    catch (OperationCanceledException ex)
    {
      // Ignore.
    }
    catch (InvocationTargetException ex)
    {
      if (!(ex.getCause() instanceof OperationCanceledException))
      {
        throw ex;
      }
    }
  }

  public static void handleException(Throwable ex)
  {
    UIPlugin.INSTANCE.log(ex);
    ErrorDialog.open(ex);
  }

  public static Color getEclipseThemeColor()
  {
    return UIPlugin.getColor(44, 34, 85);
  }

  /**
   * Extracts all sprites from the given textureatlas.
   *
   * @param textureAtlas The input texture atlas.
   * @param countX The number of sprites in horizontal axis.
   * @param countY The number of sprites in vertical axis.
   *
   * @return An array containing the countX * countY sprites.
   */
  public static Image[] extractSprites(Image textureAtlas, int countX, int countY)
  {
    Rectangle atlasBounds = textureAtlas.getBounds();
    int width = atlasBounds.width / countX;
    int height = atlasBounds.height / countY;

    Image[] sprites = new Image[countX * countY];
    for (int y = 0; y < countY; y++)
    {
      for (int x = 0; x < countX; x++)
      {
        sprites[countX * y + x] = UIUtil.extractSprite(textureAtlas, x * width, y * width, width, height);
      }
    }

    return sprites;
  }

  /**
   * Extracts a single sprite from the given texture atlas.
   * <br>
   * <br>
   * <b>Important:</b> the caller is responsible for disposing the created image.
   *
   * @param textureAtlas The input texture atlas.
   * @param x The x coordinate of the target sprite.
   * @param y The y coordinate of the target sprite.
   * @param width The width of the target sprite.
   * @param height The height of the target sprite.
   *
   * @return A new image with the extracted sprite.
   */
  public static Image extractSprite(Image textureAtlas, int x, int y, int width, int height)
  {
    ImageData textureAtlasData = textureAtlas.getImageData();
    PaletteData textureAtlasPaletteData = textureAtlasData.palette;

    ImageData spriteImageData = new ImageData(width, height, textureAtlasData.depth,
        new PaletteData(textureAtlasPaletteData.redMask, textureAtlasPaletteData.greenMask, textureAtlasPaletteData.blueMask));

    int[] pixels = new int[width * height];
    byte[] alphas = new byte[width * height];

    for (int scanline = 0; scanline < height; scanline++)
    {
      int yOffset = y + scanline;
      textureAtlasData.getPixels(x, yOffset, width, pixels, scanline * width);
      textureAtlasData.getAlphas(x, yOffset, width, alphas, scanline * width);
    }

    spriteImageData.setPixels(0, 0, pixels.length, pixels, 0);
    spriteImageData.setAlphas(0, 0, alphas.length, alphas, 0);

    return new Image(getDisplay(), spriteImageData);
  }

  public static Image getStatusImage(int severity)
  {
    if (severity == IStatus.ERROR)
    {
      if (errorImage == null)
      {
        errorImage = UIPlugin.INSTANCE.getSWTImage("error");
      }

      return errorImage;
    }

    if (severity == IStatus.WARNING)
    {
      if (warningImage == null)
      {
        warningImage = UIPlugin.INSTANCE.getSWTImage("warning");
      }

      return warningImage;
    }

    if (infoImage == null)
    {
      infoImage = UIPlugin.INSTANCE.getSWTImage("info");
    }

    return infoImage;
  }

  public static void exec(Display display, boolean async, Runnable runnable)
  {
    if (async)
    {
      asyncExec(display, runnable);
    }
    else
    {
      syncExec(display, runnable);
    }
  }

  public static void asyncExec(Runnable runnable)
  {
    final Display display = getDisplay();
    if (display != null)
    {
      asyncExec(display, runnable);
    }
  }

  public static void asyncExec(final Control control, final Runnable runnable)
  {
    try
    {
      if (control.isDisposed())
      {
        return;
      }

      control.getDisplay().asyncExec(new Runnable()
      {
        public void run()
        {
          if (!control.isDisposed())
          {
            runnable.run();
          }
        }
      });
    }
    catch (SWTException ex)
    {
      if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
      {
        throw ex;
      }

      //$FALL-THROUGH$
    }
  }

  public static void asyncExec(final Display display, final Runnable runnable)
  {
    try
    {
      if (display.isDisposed())
      {
        return;
      }

      display.asyncExec(new Runnable()
      {
        public void run()
        {
          if (display.isDisposed())
          {
            return;
          }

          try
          {
            runnable.run();
          }
          catch (SWTException ex)
          {
            if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
            {
              throw ex;
            }

            //$FALL-THROUGH$
          }
        }
      });
    }
    catch (SWTException ex)
    {
      if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
      {
        throw ex;
      }

      //$FALL-THROUGH$
    }
  }

  public static void syncExec(final Runnable runnable)
  {
    final Display display = getDisplay();
    if (Display.getCurrent() == display || display == null)
    {
      runnable.run();
    }
    else
    {
      syncExec(display, runnable);
    }
  }

  public static void syncExec(final Control control, final Runnable runnable)
  {
    try
    {
      if (control.isDisposed())
      {
        return;
      }

      control.getDisplay().syncExec(new Runnable()
      {
        public void run()
        {
          if (!control.isDisposed())
          {
            runnable.run();
          }
        }
      });
    }
    catch (SWTException ex)
    {
      if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
      {
        throw ex;
      }

      //$FALL-THROUGH$
    }
  }

  public static void syncExec(final Display display, final Runnable runnable)
  {
    try
    {
      if (display.isDisposed())
      {
        return;
      }

      display.syncExec(new Runnable()
      {
        public void run()
        {
          if (display.isDisposed())
          {
            return;
          }

          try
          {
            runnable.run();
          }
          catch (SWTException ex)
          {
            if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
            {
              throw ex;
            }

            //$FALL-THROUGH$
          }
        }
      });
    }
    catch (SWTException ex)
    {
      if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
      {
        throw ex;
      }

      //$FALL-THROUGH$
    }
  }

  public static void timerExec(int milliseconds, final Runnable runnable)
  {
    final Display display = getDisplay();
    if (display != null)
    {
      timerExec(milliseconds, display, runnable);
    }
  }

  public static void timerExec(int milliseconds, final Display display, final Runnable runnable)
  {
    try
    {
      if (display.isDisposed())
      {
        return;
      }

      display.timerExec(milliseconds, new Runnable()
      {
        public void run()
        {
          if (display.isDisposed())
          {
            return;
          }

          try
          {
            runnable.run();
          }
          catch (SWTException ex)
          {
            if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
            {
              throw ex;
            }

            //$FALL-THROUGH$
          }
        }
      });
    }
    catch (SWTException ex)
    {
      if (ex.code != SWT.ERROR_WIDGET_DISPOSED)
      {
        throw ex;
      }

      //$FALL-THROUGH$
    }
  }

  public static IDialogSettings getOrCreateSection(IDialogSettings settings, String sectionName)
  {
    IDialogSettings section = settings.getSection(sectionName);
    if (section == null)
    {
      section = settings.addNewSection(sectionName);
    }

    return section;
  }

  public static void dispose(Resource... resources)
  {
    for (int i = 0; i < resources.length; i++)
    {
      Resource resource = resources[i];
      if (resource != null && !resource.isDisposed())
      {
        resource.dispose();
      }
    }
  }

  public static void simulateKey(char character)
  {
    Display display = getDisplay();

    Event event = new Event();
    event.type = SWT.KeyDown;
    event.character = character;
    display.post(event);

    try
    {
      Thread.sleep(10);
    }
    catch (InterruptedException ex)
    {
    }

    event.type = SWT.KeyUp;
    display.post(event);

    try
    {
      Thread.sleep(10);
    }
    catch (InterruptedException ex)
    {
    }
  }

  public static String stripHTML(String html)
  {
    try
    {
      final StringBuilder builder = new StringBuilder();
      new ParserDelegator().parse(new StringReader(html), new HTMLEditorKit.ParserCallback()
      {
        @Override
        public void handleText(char[] text, int pos)
        {
          builder.append(text);
        }

        @Override
        public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos)
        {
          if (t.breaksFlow())
          {
            builder.append("\n");
          }
        }
      }, Boolean.TRUE);

      return builder.toString();
    }
    catch (IOException ex)
    {
      return html;
    }
  }

  /**
   * This returns HTML that can be rendered by styled text, which respects some HTML but not all of it well.
   */
  public static String getRenderableHTML(String html)
  {
    return stripHTMLFull(html, true);
  }

  private static String stripHTMLFull(String html, final boolean renderable)
  {
    try
    {
      final StringBuilder result = new StringBuilder();
      new ParserDelegator().parse(new StringReader(html), new HTMLEditorKit.ParserCallback()
      {
        private StringBuilder builder = result;

        private List<List<StringBuilder>> table;

        private List<StringBuilder> row;

        @Override
        public void handleText(char[] text, int pos)
        {
          if (renderable)
          {
            builder.append(DiagnosticDecorator.escapeContent(new String(text)));
          }
          else
          {
            builder.append(text);
          }
        }

        @Override
        public void handleSimpleTag(Tag t, MutableAttributeSet a, int pos)
        {
          if ("img".equals(t.toString()))
          {
            builder.append("      ");
          }
          else if (t.breaksFlow())
          {
            if (renderable && builder == result)
            {
              builder.append("<br/>");
            }

            builder.append("\n");
          }
        }

        @Override
        public void handleStartTag(Tag t, MutableAttributeSet a, int pos)
        {
          String tagName = t.toString();
          if ("table".equals(tagName))
          {
            table = new ArrayList<List<StringBuilder>>();
          }
          else if ("tr".equals(tagName))
          {
            row = new ArrayList<StringBuilder>();
            table.add(row);
          }
          else if ("td".equals(tagName))
          {
            builder = new StringBuilder();
            row.add(builder);
          }
          else if (tagName.startsWith("h") && tagName.length() > 1 && Character.isDigit(tagName.charAt(1)))
          {
            appendLineFeed(true);
            appendLineFeed(false);
            if (renderable)
            {
              builder.append("<b>");
            }
          }
          else
          {
            if (t.breaksFlow())
            {
              appendLineFeed(true);
            }
          }
        }

        @Override
        public void handleEndTag(Tag t, int pos)
        {
          String tagName = t.toString();
          if ("table".equals(tagName))
          {
            int[] widths = null;
            String[][][] tableEntries = new String[table.size()][][];

            builder = result;

            int rowIndex = 0;
            for (List<StringBuilder> row : table)
            {
              if (widths == null)
              {
                widths = new int[row.size()];
              }

              String[][] tableRow = new String[row.size()][];
              tableEntries[rowIndex] = tableRow;

              int columnIndex = 0;
              for (StringBuilder entry : row)
              {
                String value = StringUtil.trimRight(entry.toString());
                String[] lines = value.split("\n");
                tableRow[columnIndex] = lines;

                for (String line : lines)
                {
                  widths[columnIndex] = Math.max(widths[columnIndex], line.length());
                }

                ++columnIndex;
              }

              ++rowIndex;
            }

            if (renderable)
            {
              builder.append("<pre>");
            }

            for (String[][] row : tableEntries)
            {
              int maxLines = 0;
              for (String[] column : row)
              {
                maxLines = Math.max(maxLines, column.length);
              }

              if (renderable)
              {
                for (int i = 0; i < maxLines; ++i)
                {
                  int columnIndex = 0;
                  for (String[] column : row)
                  {
                    String columnText = i < column.length ? column[i] : "";
                    int columnWidth = widths[columnIndex];
                    append(columnText, columnWidth);
                    ++columnIndex;
                  }
                  builder.append("\n");
                }
              }
              else
              {
                appendLineFeed(true);
                for (int i = 0; i < maxLines; ++i)
                {
                  builder.append("|  ");
                  int columnIndex = 0;
                  for (String[] column : row)
                  {
                    append(i < column.length ? column[i] : "", widths[columnIndex]);
                    builder.append(columnIndex == row.length - 1 ? "  |" : "  |  ");
                    ++columnIndex;
                  }

                  appendLineFeed(false);
                }
              }
            }

            if (renderable)
            {
              builder.append("</pre>");

            }

            table = null;
          }
          else if ("tr".equals(tagName))
          {
          }
          else if ("td".equals(tagName))
          {
          }
          else if (tagName.startsWith("h") && tagName.length() > 1 && Character.isDigit(tagName.charAt(1)))
          {
            if (renderable)
            {
              builder.append("</b>");
              appendLineFeed(true);
            }
          }
          else
          {
            if (t.breaksFlow())
            {
              appendLineFeed(true);
            }
          }
        }

        private void append(String value, int width)
        {
          builder.append(value);

          for (int i = value.length(); i < width; ++i)
          {
            builder.append(' ');
          }
        }

        private void appendLineFeed(boolean conditional)
        {
          if (conditional)
          {
            int length = builder.length();
            if (length == 0 || builder.charAt(length - 1) == '\n')
            {
              return;
            }
          }

          if (renderable)
          {
            builder.append("<br/>");
          }

          builder.append('\n');
        }

      }, Boolean.TRUE);

      return result.toString();
    }
    catch (IOException ex)
    {
      return html;
    }
  }

  public static Point caclcuateSize(String html)
  {
    Shell shell = new Shell();
    GC gc = new GC(shell);
    FontRegistry fontRegistry = JFaceResources.getFontRegistry();
    Font font = fontRegistry.get(fontRegistry.hasValueFor("org.eclipse.jdt.ui.javadocfont") ? "org.eclipse.jdt.ui.javadocfont" : JFaceResources.DIALOG_FONT);
    Font fixedPitchFont = JFaceResources.getTextFont();

    gc.setFont(font);
    FontMetrics fontMetrics = gc.getFontMetrics();
    int averageCharWidth = fontMetrics.getAverageCharWidth();

    Font boldFont = ExtendedFontRegistry.INSTANCE.getFont(font, IItemFontProvider.BOLD_FONT);
    gc.setFont(boldFont);

    boolean browerAvailable = isBrowserAvailable();

    String text = stripHTMLFull(html, false).trim();
    String[] lines = text.split("\n");
    int pixelWidth = 0;
    for (String line : lines)
    {
      boolean isTableRow = line.startsWith("|");
      if (!browerAvailable && isTableRow)
      {
        gc.setFont(fixedPitchFont);
      }

      int lineWidth = gc.textExtent(line).x;
      if (browerAvailable && isTableRow)
      {
        lineWidth += 4 * averageCharWidth;
      }

      pixelWidth = Math.max(pixelWidth, lineWidth);
      gc.setFont(font);
    }

    int numberOfAverageCharacters = pixelWidth / averageCharWidth + 2;

    gc.dispose();
    shell.dispose();

    return new Point(numberOfAverageCharacters, lines.length);
  }

  /**
   * This is a runnable for which {@link #schedule() schedule} can be called repeatedly.
   * That will {@link UIUtil#timerExec(int, Runnable) timer execute} this runnable with the delay specified in the constructor,
   * or, if the runnable has already been dispatched, will mark it for redispatching.
   * As such, when {@link #run() run} is called, if schedule has been called in the meantime,
   * the runnable will timer executed again,
   * without calling {@link #perform() perform}.
   * Otherwise, run will call perform to finally perform the delayed behavior.
   * The {@link #prepareForDispatch() prepareForDispatch} method will be called immediate before any timer execute.
   * In this default implementation is does nothing.
   * If the control specified in the constructor is disposed when run is called, run will do nothing.
   *
   * @author Ed Merks
   */
  public static abstract class DelayedRunnable implements Runnable
  {
    final private Control control;

    private int delay;

    private boolean dispatched;

    private boolean redispatch;

    public DelayedRunnable(Control control, int milliseconds)
    {
      this.control = control;
      delay = milliseconds;
    }

    protected abstract void perform();

    protected void prepareForDispatch()
    {
    }

    public void run()
    {
      if (!control.isDisposed())
      {
        dispatched = false;
        if (redispatch)
        {
          schedule();
        }
        else
        {
          perform();
        }
      }
    }

    public void schedule()
    {
      if (dispatched)
      {
        redispatch = true;
      }
      else
      {
        prepareForDispatch();

        dispatched = true;
        redispatch = false;

        UIUtil.timerExec(delay, this);
      }
    }
  }
}
