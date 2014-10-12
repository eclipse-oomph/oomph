package org.eclipse.oomph.setup.ui.questionaire;

import org.eclipse.oomph.setup.ui.questionaire.AnimatedCanvas.Animator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * @author Eike Stepper
 */
public final class GearAnimator extends Animator
{
  public static final int PAGE_WIDTH = 620;

  public static final int PAGE_HEIGHT = 420;

  public static final int BORDER = 30;

  private static final int GEARS = 7;

  private static final int TEETH = 8;

  private static final float ANGLE = 360 / TEETH;

  private static final double RADIAN = 2 * Math.PI / 360;

  private static final int BIG_FONT_PX = 48;

  private static final int NONE = -1;

  private static final int EXIT = NONE - 1;

  private static final int CHOICES = EXIT - 1;

  private final Font font;

  private final Font bigFont;

  private final Font hoverFont;

  private final Image exit;

  private final Image exitHover;

  private final Image[] yesImages = new Image[4];

  private final Image[] noImages = new Image[4];

  private final Page[] pages = new Page[GEARS];

  private final Path[] gearPaths = new Path[GEARS];

  private final Color[] gearBackground = new Color[2];

  private final Color[] gearForeground = new Color[2];

  private Color purple;

  private final float radius;

  private final int pageY;

  private float angle;

  private float speed;

  private boolean overflow;

  private int selection;

  private int oldSelection = NONE;

  private int hover = NONE;

  private int oldHover = NONE;

  private Image pageBuffer;

  private Image oldPageBuffer;

  private boolean pageBufferUpdated;

  private boolean oldShowOverlay;

  private Rectangle exitBox;

  public GearAnimator(final Display display, Font font)
  {
    super(display);
    this.font = font;
    bigFont = createFont(display, font, BIG_FONT_PX);
    hoverFont = createFont(display, font, BIG_FONT_PX + 6);

    exit = new Image(display, "questionaire/exit.png");
    exitHover = new Image(display, "questionaire/exit_hover.png");

    yesImages[0] = new Image(display, "questionaire/yes.png");
    yesImages[1] = new Image(display, "questionaire/yes_select.png");
    yesImages[2] = new Image(display, "questionaire/yes_hover.png");
    yesImages[3] = new Image(display, "questionaire/yes_badge.png");
    noImages[0] = new Image(display, "questionaire/no.png");
    noImages[1] = new Image(display, "questionaire/no_select.png");
    noImages[2] = new Image(display, "questionaire/no_hover.png");
    noImages[3] = new Image(display, "questionaire/no_badge.png");

    radius = 32;
    setSize((int)(GEARS * 2 * radius), (int)(2 * radius));
    pageY = getHeight() + 2 * BORDER;

    // Not selected.
    gearBackground[0] = new Color(display, 169, 171, 202);
    gearForeground[0] = new Color(display, 140, 132, 171);

    // Selected.
    gearBackground[1] = new Color(display, 247, 148, 30);
    gearForeground[1] = new Color(display, 207, 108, 0);

    purple = new Color(display, 43, 34, 84);

    pages[0] = new QuestionPage(0, "Welcome to Eclipse Oomph", 0, 0, new TextAnswer("Proceed"));
    pages[1] = new QuestionPage(1, "Refresh Resources Automatically?", 5, 29);
    pages[2] = new QuestionPage(2, "Show Line Numbers in Editors?", 19, 30);
    pages[3] = new QuestionPage(3, "Check Spelling in Text Editors?", 186, 37);
    pages[4] = new QuestionPage(4, "Execute Jobs in Background?", 23, 160);
    pages[5] = new QuestionPage(5, "Encode Text Files with UTF-8?", 181, 95);
    pages[6] = new QuestionPage(6, "Enable Preference Recorder?", 57, 82);
  }

  @Override
  public void dispose()
  {
    for (Path path : gearPaths)
    {
      if (path != null)
      {
        path.dispose();
      }
    }

    for (Page page : pages)
    {
      if (page != null)
      {
        page.dispose();
      }
    }

    purple.dispose();
    gearForeground[0].dispose();
    gearBackground[0].dispose();
    gearForeground[1].dispose();
    gearBackground[1].dispose();

    hoverFont.dispose();
    bigFont.dispose();
    font.dispose();
    exit.dispose();
    super.dispose();
  }

  private Font createFont(Display display, Font baseFont, int pixelHeight)
  {
    GC fontGC = new GC(display);

    try
    {
      FontData[] fontData = baseFont.getFontData();
      int fontSize = 40;
      while (fontSize > 0)
      {
        for (int i = 0; i < fontData.length; i++)
        {
          fontData[i].setHeight(fontSize);
          fontData[i].setStyle(SWT.BOLD);
        }

        Font font = new Font(display, fontData);
        fontGC.setFont(font);
        int height = fontGC.stringExtent("Ag").y;
        if (height <= pixelHeight)
        {
          return font;
        }

        font.dispose();
        --fontSize;
      }

      throw new RuntimeException("Could not create a big font");
    }
    finally
    {
      fontGC.dispose();
    }
  }

  public void restart()
  {
    angle = 0;
    speed = 0;
  }

  public int getSelection()
  {
    return selection;
  }

  public void setSelection(int selection)
  {
    if (selection < 0)
    {
      selection = 0;
      overflow = true;
    }
    else if (selection > GEARS - 1)
    {
      selection = GEARS - 1;
      overflow = true;
    }

    if (overflow)
    {
      overflow = false;
      while (advance())
      {
      }

      overflow = true;
      return;
    }

    oldSelection = this.selection;
    this.selection = selection;

    Image tmpPageBuffer = oldPageBuffer;
    oldPageBuffer = pageBuffer;
    pageBuffer = tmpPageBuffer;
    pageBufferUpdated = false;

    hover = NONE;
    oldHover = NONE;
    restart();
  }

  public int getOldSelection()
  {
    return oldSelection;
  }

  public Page getSelectedPage()
  {
    return pages[selection];
  }

  @Override
  protected boolean onMouseMove(int x, int y)
  {
    if (x != Integer.MIN_VALUE && y != Integer.MIN_VALUE)
    {
      GC gc = getBufferGC();
      if (gc != null)
      {
        for (int i = 0; i < gearPaths.length; i++)
        {
          Path path = gearPaths[i];
          if (path != null && path.contains(x, y, gc, false))
          {
            if (i != hover)
            {
              hover = i;
            }

            return true;
          }
        }

        if (exitBox != null && exitBox.contains(x, y))
        {
          hover = EXIT;
          return true;
        }

        Page page = getSelectedPage();
        if (page != null)
        {
          hover = page.onMouseMove(x, y);
          if (hover < NONE)
          {
            return true;
          }
        }
      }
    }

    hover = NONE;
    return false;
  }

  @Override
  protected boolean onMouseDown(int x, int y)
  {
    if (x != Integer.MIN_VALUE && y != Integer.MIN_VALUE)
    {
      GC gc = getBufferGC();
      if (gc != null)
      {
        for (int i = 0; i < gearPaths.length; i++)
        {
          Path path = gearPaths[i];
          if (path != null && path.contains(x, y, gc, false))
          {
            if (i != getSelection())
            {
              setSelection(i);
            }

            return true;
          }
        }

        Page page = getSelectedPage();
        if (page != null)
        {
          if (page.onMouseDown(x, y))
          {
            return true;
          }
        }
      }
    }

    return false;
  }

  @Override
  protected boolean advance()
  {
    boolean needsRedraw = false;
    if (overflow)
    {
      overflow = false;
      needsRedraw = true;
    }

    boolean showOverlay = showOverlay();
    if (showOverlay != oldShowOverlay)
    {
      oldShowOverlay = showOverlay;
      updatePage();
      needsRedraw = true;
    }

    if (hover != oldHover)
    {
      needsRedraw = true;
    }

    if (speed >= ANGLE)
    {
      return needsRedraw;
    }

    needsRedraw = true;
    speed += .4f;
    angle += speed;
    return needsRedraw;
  }

  @Override
  protected void paint(Image buffer, GC gc)
  {
    Display display = getDisplay();
    gc.setFont(font);
    gc.setLineWidth(3);
    gc.setAntialias(SWT.ON);

    for (int i = 0; i < GEARS; i++)
    {
      if (i != selection)
      {
        paint(gc, display, i);
      }
    }

    paint(gc, display, selection);

    Image exitImage = exit;
    if (hover == EXIT)
    {
      exitImage = exitHover;
      oldHover = hover;
    }

    int centerX = BORDER + PAGE_WIDTH - exit.getBounds().width / 2;
    int centerY = BORDER + exit.getBounds().height / 2;

    exitBox = exitImage.getBounds();
    exitBox.x = centerX - exitImage.getBounds().width / 2;
    exitBox.y = centerY - exitImage.getBounds().height / 2;
    gc.drawImage(exitImage, exitBox.x, exitBox.y);

    if (hover == NONE)
    {
      oldHover = NONE;
    }

    if (!pageBufferUpdated)
    {
      updatePage();
      pageBufferUpdated = true;
    }

    int alpha = Math.min((int)(255 * speed / ANGLE), 255);
    if (oldSelection == NONE)
    {
      gc.setAlpha(alpha);
      gc.drawImage(pageBuffer, BORDER, pageY);
      gc.setAlpha(255);
    }
    else
    {
      double progress = 2 * speed / ANGLE;
      int slide = Math.min((int)(PAGE_WIDTH * progress * progress), PAGE_WIDTH);

      gc.setAlpha(255 - alpha);
      if (selection > oldSelection)
      {
        gc.drawImage(oldPageBuffer, slide, 0, PAGE_WIDTH - slide, PAGE_HEIGHT, BORDER, pageY, PAGE_WIDTH - slide, PAGE_HEIGHT);
        gc.setAlpha(alpha);
        gc.drawImage(pageBuffer, 0, 0, slide, PAGE_HEIGHT, BORDER + PAGE_WIDTH - slide, pageY, slide, PAGE_HEIGHT);
      }
      else
      {
        gc.drawImage(oldPageBuffer, 0, 0, PAGE_WIDTH - slide, PAGE_HEIGHT, BORDER + slide, pageY, PAGE_WIDTH - slide, PAGE_HEIGHT);
        gc.setAlpha(alpha);
        gc.drawImage(pageBuffer, PAGE_WIDTH - slide, 0, slide, PAGE_HEIGHT, BORDER, pageY, slide, PAGE_HEIGHT);
      }

      gc.setAlpha(255);
    }
  }

  private void paint(GC gc, Display display, int i)
  {
    double offset = 2 * i * radius;
    double x = BORDER + radius + offset;
    double y = BORDER + radius;
    double r2 = (double)radius * .8f;
    double r3 = (double)radius * .5f;

    int selected = 0;
    double factor = 1;
    if (i == oldSelection)
    {
      if (speed < ANGLE / 2)
      {
        selected = 1;
      }
    }
    else if (i == selection)
    {
      if (speed >= ANGLE / 2)
      {
        selected = 1;
        factor += (ANGLE - speed) * .02;
      }
      else
      {
        factor += speed * .02;
      }
    }

    boolean hovered = false;
    if (i == hover)
    {
      factor += .1;
      oldHover = hover;
      if (selected == 0)
      {
        hovered = true;
      }
    }

    double outerR = factor * radius;
    double innerR = factor * r2;
    float angleOffset = (angle + i * ANGLE) * (i % 2 == 1 ? -1 : 1);
    gc.setForeground(hovered ? display.getSystemColor(SWT.COLOR_DARK_GRAY) : gearForeground[selected]);
    gc.setBackground(hovered ? display.getSystemColor(SWT.COLOR_GRAY) : gearBackground[selected]);
    Path path = drawGear(gc, x, y, outerR, innerR, angleOffset);

    if (gearPaths[i] != null)
    {
      gearPaths[i].dispose();
    }

    gearPaths[i] = path;

    int ovalX = (int)(x - factor * r3);
    int ovalY = (int)(y - factor * r3);
    int ovalR = (int)(2 * factor * r3);
    gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    gc.fillOval(ovalX, ovalY, ovalR, ovalR);
    gc.drawOval(ovalX, ovalY, ovalR, ovalR);

    String number = Integer.toString(i);
    Point extent = gc.stringExtent(number);
    Page page = pages[i];

    if (selected == 1)
    {
      gc.setForeground(gearForeground[1]);
    }
    else
    {
      gc.setForeground(display.getSystemColor(SWT.COLOR_GRAY));
    }

    int cX = (int)(x - extent.x / 2);
    int cY = (int)(y - 2 - extent.y / 2);
    gc.drawText(number, cX, cY, true);

    Answer answer = page.getChoiceAnswer();
    if (answer instanceof ImageAnswer)
    {
      ImageAnswer imageAnswer = (ImageAnswer)answer;
      Image image = imageAnswer.getImages()[3];
      gc.drawImage(image, (int)(x - image.getBounds().width / 2), (int)(y - outerR - 12));
    }
  }

  private Path drawGear(GC gc, double cx, double cy, double outerR, double innerR, float angleOffset)
  {
    double radian2 = ANGLE / 2 * RADIAN;
    double radian3 = .06;

    Display display = getDisplay();
    Path path = new Path(display);

    for (int i = 0; i < TEETH; i++)
    {
      double radian = (i * ANGLE + angleOffset) * RADIAN;

      double x = cx + outerR * Math.cos(radian);
      double y = cy - outerR * Math.sin(radian);

      if (i == 0)
      {
        path.moveTo((int)x, (int)y);
      }

      double r1 = radian + radian3;
      double r3 = radian + radian2;
      double r2 = r3 - radian3;
      double r4 = r3 + radian2;

      x = cx + innerR * Math.cos(r1);
      y = cy - innerR * Math.sin(r1);
      path.lineTo((int)x, (int)y);

      x = cx + innerR * Math.cos(r2);
      y = cy - innerR * Math.sin(r2);
      path.lineTo((int)x, (int)y);

      x = cx + outerR * Math.cos(r3);
      y = cy - outerR * Math.sin(r3);
      path.lineTo((int)x, (int)y);

      x = cx + outerR * Math.cos(r4);
      y = cy - outerR * Math.sin(r4);
      path.lineTo((int)x, (int)y);
    }

    path.close();
    gc.fillPath(path);
    gc.drawPath(path);
    return path;
  }

  private void updatePage()
  {
    Display display = getDisplay();
    if (pageBuffer == null)
    {
      pageBuffer = new Image(display, PAGE_WIDTH, PAGE_HEIGHT);
    }

    GC gc = new GC(pageBuffer);
    gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
    gc.fillRectangle(pageBuffer.getBounds());

    Page page = getSelectedPage();
    if (page != null)
    {
      page.draw(gc);
    }

    gc.dispose();
  }

  private boolean showOverlay()
  {
    long millis2 = (int)(System.currentTimeMillis() / 1000);
    return (millis2 & 1) == 1;
  }

  void updateOverlay(int x, int y)
  {
    Page page = getSelectedPage();
    if (page instanceof QuestionPage)
    {
      QuestionPage imagePage = (QuestionPage)page;
      imagePage.overlayX += x;
      imagePage.overlayY += y;

      System.out.println("" + imagePage.overlayX + ", " + imagePage.overlayY);

      updatePage();
      overflow = true;
    }
  }

  /**
   * @author Eike Stepper
   */
  public abstract class Answer
  {
    public abstract Point getSize(GC gc, int i);

    public abstract Rectangle draw(GC gc, int i, int x, int y, boolean hovered, boolean selected);
  }

  /**
   * @author Eike Stepper
   */
  public class TextAnswer extends Answer
  {
    private final String text;

    public TextAnswer(String text)
    {
      this.text = text;
    }

    public final String getText()
    {
      return text;
    }

    @Override
    public Point getSize(GC gc, int i)
    {
      gc.setFont(hoverFont);
      return gc.stringExtent(text);
    }

    @Override
    public Rectangle draw(GC gc, int i, int x, int y, boolean hovered, boolean selected)
    {
      gc.setFont(hovered ? hoverFont : bigFont);
      gc.setForeground(selected ? purple : getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));

      Point extent = gc.stringExtent(text);
      x -= extent.x / 2 - 60; // TODO -60 is a hack!
      y -= extent.y / 2;
      gc.drawText(text, x, y, true);

      return new Rectangle(x, y, extent.x, extent.y);
    }
  }

  /**
   * @author Eike Stepper
   */
  public class ImageAnswer extends Answer
  {
    private final Image[] images;

    public ImageAnswer(Image... images)
    {
      this.images = images;
    }

    public final Image[] getImages()
    {
      return images;
    }

    @Override
    public Point getSize(GC gc, int i)
    {
      Rectangle bounds = images[2].getBounds();
      return new Point(bounds.width, bounds.height);
    }

    @Override
    public Rectangle draw(GC gc, int i, int x, int y, boolean hovered, boolean selected)
    {
      Image image;
      if (hovered)
      {
        image = images[2];
      }
      else if (selected)
      {
        image = images[1];
      }
      else
      {
        image = images[0];
      }

      Rectangle bounds = image.getBounds();
      x -= bounds.width / 2;
      y -= bounds.height / 2;
      gc.drawImage(image, x, y);

      return new Rectangle(x, y, bounds.width, bounds.height);
    }
  }

  /**
   * @author Eike Stepper
   */
  public abstract class Page
  {
    private final int index;

    private final String title;

    private Answer[] answers;

    private Rectangle[] answerBoxes;

    private int choice = NONE;

    public Page(int index, String title)
    {
      this.index = index;
      this.title = title;
    }

    public final int getIndex()
    {
      return index;
    }

    public final String getTitle()
    {
      return title;
    }

    public final Answer[] getAnswers()
    {
      return answers;
    }

    public void setAnswers(Answer[] answers)
    {
      this.answers = answers;
      answerBoxes = new Rectangle[answers.length];
    }

    public final int getAnswer(int x, int y)
    {
      x -= BORDER;
      y -= pageY;

      for (int i = 0; i < answers.length; i++)
      {
        Rectangle box = answerBoxes[i];
        if (box.contains(x, y))
        {
          return i;
        }
      }

      return NONE;
    }

    public final Rectangle[] getAnswerBoxes()
    {
      return answerBoxes;
    }

    public final Answer getChoiceAnswer()
    {
      return choice == NONE ? null : answers[choice];
    }

    public final int getChoice()
    {
      return choice;
    }

    public final void setChoice(int choice)
    {
      this.choice = choice;
    }

    protected void dispose()
    {
    }

    protected int onMouseMove(int x, int y)
    {
      int i = getAnswer(x, y);
      if (i != NONE)
      {
        pageBufferUpdated = false;
        return CHOICES - i;
      }

      if (hover <= CHOICES)
      {
        pageBufferUpdated = false;
      }

      return NONE;
    }

    protected boolean onMouseDown(int x, int y)
    {
      int i = getAnswer(x, y);
      if (i != NONE)
      {
        setChoice(i);
        updatePage();
        setSelection(getSelection() + 1);
        return true;
      }

      return false;
    }

    protected final void draw(GC gc)
    {
      String title = getTitle();

      Font oldFont = gc.getFont();
      gc.setFont(bigFont);

      Point extent = gc.stringExtent(title);
      gc.setForeground(purple);
      gc.drawText(title, (PAGE_WIDTH - extent.x) / 2, 0, true);

      drawContent(gc);

      boolean selecteds[] = new boolean[answers.length];
      boolean hovereds[] = new boolean[answers.length];
      Point sizes[] = new Point[answers.length];

      int width = (answers.length - 1) * BORDER;
      int height = 0;

      for (int i = 0; i < answers.length; i++)
      {
        selecteds[i] = i == choice;
        if (CHOICES - i == hover)
        {
          oldHover = hover;
          hovereds[i] = true;
        }

        sizes[i] = answers[i].getSize(gc, i);
        width += sizes[i].x;
        height = Math.max(height, sizes[i].y);
      }

      int x = (PAGE_WIDTH - width) / 2;
      int y = PAGE_HEIGHT - height / 2 - 1;

      for (int i = 0; i < answers.length; i++)
      {
        Answer answer = answers[i];
        answerBoxes[i] = answer.draw(gc, i, x, y, hovereds[i], selecteds[i]);
        x += BORDER + sizes[i].x;
      }

      gc.setFont(oldFont);
    }

    protected abstract void drawContent(GC gc);
  }

  /**
   * @author Eike Stepper
   */
  public class QuestionPage extends Page
  {
    private final Image image;

    private final Image overlay;

    private int overlayX;

    private int overlayY;

    public QuestionPage(int index, String title, int overlayX, int overlayY, Answer... answers)
    {
      super(index, title);
      setAnswers(answers);

      Display display = getDisplay();
      image = loadImage(display, index, "");
      overlay = loadImage(display, index, "_ovr");

      this.overlayX = overlayX;
      this.overlayY = overlayY;
    }

    public QuestionPage(int index, String title, int overlayX, int overlayY)
    {
      this(index, title, overlayX, overlayY, new Answer[] { new ImageAnswer(yesImages), new ImageAnswer(noImages) });
    }

    @Override
    protected void dispose()
    {
      if (image != null)
      {
        image.dispose();
      }

      if (overlay != null)
      {
        overlay.dispose();
      }
    }

    @Override
    protected void drawContent(GC gc)
    {
      if (image != null)
      {
        Rectangle bounds = image.getBounds();
        int x = (PAGE_WIDTH - bounds.width) / 2;
        int y = (PAGE_HEIGHT - bounds.height) / 2;
        gc.drawImage(image, x, y);

        if (overlay != null && showOverlay())
        {
          gc.drawImage(overlay, x + overlayX, y + overlayY);
        }
      }
    }

    private Image loadImage(final Display display, int index, String suffix)
    {
      try
      {
        return new Image(display, "questionaire/page" + index + suffix + ".png");
      }
      catch (Exception ex)
      {
        return null;
      }
    }
  }
}
