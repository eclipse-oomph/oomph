package org.eclipse.oomph.setup.ui.questionaire;

import org.eclipse.oomph.setup.ui.questionaire.AnimatedCanvas.Animator;
import org.eclipse.oomph.ui.UIUtil;

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

  public static final int GEARS = 7;

  private static final int TEETH = 8;

  private static final float ANGLE = 360 / TEETH;

  private static final double RADIAN = 2 * Math.PI / 360;

  private static final int BIG_FONT_PX = 48;

  private static final int NONE = -1;

  private static final int EXIT = NONE - 1;

  private static final int BACK = EXIT - 1;

  private static final int NEXT = BACK - 1;

  private static final int CHOICES = NEXT - 1;

  private final Font baseFont;

  private final Font bigFont;

  private final Font hoverFont;

  private final Font normalFont;

  private final Image exit;

  private final Image exitHover;

  private final Image question;

  private final Image[] backImages = new Image[2];

  private final Image[] nextImages = new Image[2];

  private final Image[] yesImages = new Image[5];

  private final Image[] noImages = new Image[5];

  private final Page[] pages = new Page[GEARS + 1];

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

  private Rectangle backBox;

  private Rectangle nextBox;

  private int answerY;

  public GearAnimator(final Display display, Font baseFont)
  {
    super(display);
    this.baseFont = baseFont;
    bigFont = createFont(display, BIG_FONT_PX);
    hoverFont = createFont(display, BIG_FONT_PX + 6);
    normalFont = createFont(display, (int)(BIG_FONT_PX * .75));

    exit = new Image(display, "questionaire/exit.png");
    exitHover = new Image(display, "questionaire/exit_hover.png");
    question = new Image(display, "questionaire/question.png");

    backImages[0] = new Image(display, "questionaire/back.png");
    backImages[1] = new Image(display, "questionaire/back_hover.png");

    nextImages[0] = new Image(display, "questionaire/next.png");
    nextImages[1] = new Image(display, "questionaire/next_hover.png");

    yesImages[0] = new Image(display, "questionaire/yes.png");
    yesImages[1] = new Image(display, "questionaire/yes_select.png");
    yesImages[2] = new Image(display, "questionaire/yes_hover.png");
    yesImages[3] = new Image(display, "questionaire/yes_big.png");
    yesImages[4] = new Image(display, "questionaire/yes_badge.png");

    noImages[0] = new Image(display, "questionaire/no.png");
    noImages[1] = new Image(display, "questionaire/no_select.png");
    noImages[2] = new Image(display, "questionaire/no_hover.png");
    noImages[3] = new Image(display, "questionaire/no_big.png");
    noImages[4] = new Image(display, "questionaire/no_badge.png");

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

    pages[0] = new QuestionPage(0, "Welcome to Eclipse Oomph", 0, 0, 0, new TextAnswer(""));
    pages[1] = new QuestionPage(1, "Refresh Resources Automatically?", 0, 5, 29);
    pages[2] = new QuestionPage(2, "Show Line Numbers in Editors?", 1, 19, 30);
    pages[3] = new QuestionPage(3, "Check Spelling in Text Editors?", 1, 186, 37);
    pages[4] = new QuestionPage(4, "Execute Jobs in Background?", 0, 23, 160);
    pages[5] = new QuestionPage(5, "Encode Text Files with UTF-8?", 0, 181, 95);
    pages[6] = new QuestionPage(6, "Enable Preference Recorder?", 1, 57, 82);
    pages[7] = new SummaryPage(6, "Summary");
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

    // Images
    UIUtil.dispose(nextImages);
    UIUtil.dispose(backImages);
    UIUtil.dispose(yesImages);
    UIUtil.dispose(noImages);
    UIUtil.dispose(exit, exitHover, question);

    // Colors
    UIUtil.dispose(purple);
    UIUtil.dispose(gearForeground);
    UIUtil.dispose(gearBackground);

    // Fonts
    UIUtil.dispose(normalFont, hoverFont, bigFont, baseFont);

    super.dispose();
  }

  public void restart()
  {
    angle = 0;
    speed = 0;
  }

  public final Page[] getPages()
  {
    return pages;
  }

  public final int getSelection()
  {
    return selection;
  }

  public final void setSelection(int selection)
  {
    hover = NONE;
    oldHover = NONE;

    if (selection < 0)
    {
      selection = 0;
      overflow = true;
    }
    else if (selection > pages.length - 1)
    {
      selection = pages.length - 1;
      overflow = true;
    }

    if (overflow)
    {
      overflow = false;
      while (advance())
      {
        // Just advance.
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

    restart();
  }

  public final int getOldSelection()
  {
    return oldSelection;
  }

  public final Page getSelectedPage()
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
          if (page.showBack() && backBox != null && backBox.contains(x, y))
          {
            hover = BACK;
            return true;
          }

          if (page.showNext() && nextBox != null && nextBox.contains(x, y))
          {
            hover = NEXT;
            return true;
          }

          x -= BORDER;
          y -= pageY;

          hover = page.onMouseMove(x, y);
          if (hover != NONE)
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

        if (exitBox != null && exitBox.contains(x, y))
        {
          hover = EXIT;
          return true;
        }

        Page page = getSelectedPage();
        if (page != null)
        {
          x -= BORDER;
          y -= pageY;

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
    gc.setFont(baseFont);
    gc.setLineWidth(3);
    gc.setAntialias(SWT.ON);

    int alpha = Math.min((int)(255 * speed / ANGLE), 255);

    for (int i = 0; i < GEARS; i++)
    {
      if (i != selection)
      {
        paint(gc, display, alpha, i);
      }
    }

    if (selection < GEARS)
    {
      paint(gc, display, alpha, selection);
    }

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
      answerY = updatePage();
      pageBufferUpdated = true;
    }

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

      int x = backImages[0].getBounds().width / 2;
      int y = 4 * BORDER + 4 + answerY;

      Page page = getSelectedPage();
      if (page.showBack())
      {
        backBox = drawImage(gc, backImages[hover == BACK ? 1 : 0], BORDER + +x, y);
      }

      if (page.showNext())
      {
        nextBox = drawImage(gc, nextImages[hover == NEXT ? 1 : 0], PAGE_WIDTH + BORDER - x, y);
      }

      oldHover = hover;
    }
  }

  private void paint(GC gc, Display display, int alpha, int i)
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

    if (selection >= GEARS)
    {
      gc.setAlpha(255 - alpha);
    }
    else if (oldSelection >= GEARS)
    {
      gc.setAlpha(alpha);
    }

    Answer answer = page.getChoiceAnswer();
    if (answer instanceof ImageAnswer)
    {
      ImageAnswer imageAnswer = (ImageAnswer)answer;
      Image image = imageAnswer.getImages()[4];
      gc.drawImage(image, (int)(x - image.getBounds().width / 2), (int)(y - outerR - 12));
    }

    gc.setAlpha(255);
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

  private int updatePage()
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
    int answerY = page.draw(gc);

    gc.dispose();
    return answerY;
  }

  private boolean showOverlay()
  {
    Page page = getSelectedPage();
    if (page instanceof QuestionPage)
    {
      QuestionPage questionPage = (QuestionPage)page;

      if (hover <= CHOICES)
      {
        int hoveredChoice = -hover + CHOICES;
        return hoveredChoice == questionPage.getOverlayChoice();
      }

      int choice = questionPage.getChoice();
      if (choice != NONE)
      {
        return choice == questionPage.getOverlayChoice();
      }
    }

    long millis2 = (int)(System.currentTimeMillis() / 1000);
    return (millis2 & 1) == 1;
  }

  void updateOverlay(int x, int y)
  {
    Page page = getSelectedPage();
    if (page instanceof QuestionPage)
    {
      QuestionPage questionPage = (QuestionPage)page;
      questionPage.overlayX += x;
      questionPage.overlayY += y;

      System.out.println("" + questionPage.overlayX + ", " + questionPage.overlayY);

      updatePage();
      overflow = true;
    }
  }

  private Font createFont(Display display, int pixelHeight)
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

  private static Rectangle drawImage(GC gc, Image image, int x, int y)
  {
    Rectangle bounds = image.getBounds();
    x -= bounds.width / 2;
    y -= bounds.height / 2;
    gc.drawImage(image, x, y);

    return new Rectangle(x, y, bounds.width, bounds.height);
  }

  /**
   * @author Eike Stepper
   */
  public abstract class Answer
  {
    public Answer()
    {
    }

    public abstract Point getSize(GC gc, Page page);

    public abstract Rectangle draw(GC gc, Page page, int index, int x, int y, boolean hovered, boolean selected);
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
    public Point getSize(GC gc, Page page)
    {
      gc.setFont(hoverFont);
      return gc.stringExtent(text);
    }

    @Override
    public Rectangle draw(GC gc, Page page, int index, int x, int y, boolean hovered, boolean selected)
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
    public Point getSize(GC gc, Page page)
    {
      Rectangle bounds = images[2].getBounds();
      return new Point(bounds.width, bounds.height);
    }

    @Override
    public Rectangle draw(GC gc, Page page, int index, int x, int y, boolean hovered, boolean selected)
    {
      Image image = images[0];
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
        if (page instanceof QuestionPage && images.length > 3)
        {
          int overlayChoice = ((QuestionPage)page).getOverlayChoice();
          boolean overlayChoiceYes = overlayChoice == index;
          boolean showOverlay = showOverlay();

          if (showOverlay == overlayChoiceYes)
          {
            image = images[3];
          }
        }
      }

      return drawImage(gc, image, x, y);
    }
  }

  /**
   * @author Eike Stepper
   */
  public class BackButton extends ImageAnswer
  {
    public BackButton()
    {
      super(backImages);
    }
  }

  /**
   * @author Eike Stepper
   */
  public class NextButton extends ImageAnswer
  {
    public NextButton()
    {
      super(nextImages);
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

    public final void setAnswers(Answer[] answers)
    {
      this.answers = answers;
      answerBoxes = new Rectangle[answers.length];
    }

    public final int getAnswer(int x, int y)
    {
      for (int i = 0; i < answers.length; i++)
      {
        Rectangle box = answerBoxes[i];
        if (box != null && box.contains(x, y))
        {
          return i;
        }
      }

      return NONE;
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

    protected boolean showBack()
    {
      return index > 0;
    }

    protected boolean showNext()
    {
      return index < GEARS;
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

    protected final int draw(GC gc)
    {
      String title = getTitle();

      Font oldFont = gc.getFont();
      gc.setFont(bigFont);

      Point extent = gc.stringExtent(title);
      gc.setForeground(purple);
      gc.drawText(title, (PAGE_WIDTH - extent.x) / 2, 0, true);

      gc.setFont(oldFont);
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

        sizes[i] = answers[i].getSize(gc, this);
        width += sizes[i].x;
        height = Math.max(height, sizes[i].y);
      }

      int x = (PAGE_WIDTH - width) / 2;
      int y = PAGE_HEIGHT - height / 2 - 1;

      for (int i = 0; i < answers.length; i++)
      {
        Answer answer = answers[i];
        answerBoxes[i] = answer.draw(gc, this, i, x, y, hovereds[i], selecteds[i]);
        x += BORDER + sizes[i].x;
      }

      return y;
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

    private final int overlayChoice;

    private int overlayX;

    private int overlayY;

    public QuestionPage(int index, String title, int overlayChoice, int overlayX, int overlayY, Answer... answers)
    {
      super(index, title);
      this.overlayChoice = overlayChoice;
      setAnswers(answers);

      Display display = getDisplay();
      image = loadImage(display, index, "");
      overlay = loadImage(display, index, "_ovr");

      this.overlayX = overlayX;
      this.overlayY = overlayY;
    }

    public QuestionPage(int index, String title, int overlayChoice, int overlayX, int overlayY)
    {
      this(index, title, overlayChoice, overlayX, overlayY, new Answer[] { new ImageAnswer(yesImages), new ImageAnswer(noImages) });
    }

    public final int getOverlayChoice()
    {
      return overlayChoice;
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

  /**
   * @author Eike Stepper
   */
  public class SummaryPage extends Page
  {
    private Rectangle[] boxes;

    public SummaryPage(int index, String title)
    {
      super(index, title);
      setAnswers(new Answer[] { new TextAnswer("Finish") });
    }

    @Override
    protected boolean showNext()
    {
      return false;
    }

    @Override
    protected int onMouseMove(int x, int y)
    {
      if (boxes != null)
      {
        for (int i = 0; i < boxes.length; i++)
        {
          Rectangle box = boxes[i];
          if (box.contains(x, y))
          {
            return i + 1;
          }
        }
      }

      return super.onMouseMove(x, y);
    }

    @Override
    protected boolean onMouseDown(int x, int y)
    {
      if (boxes != null)
      {
        for (int i = 0; i < boxes.length; i++)
        {
          Rectangle box = boxes[i];
          if (box.contains(x, y))
          {
            setSelection(i + 1);
            return true;
          }
        }
      }

      return super.onMouseDown(x, y);
    }

    @Override
    protected void drawContent(GC gc)
    {
      gc.setFont(normalFont);

      boxes = new Rectangle[GEARS - 1];
      int offsetX = yesImages[4].getBounds().width + 12;
      int minWidth = Integer.MAX_VALUE;
      int maxWidth = 0;

      for (int i = 1; i < GEARS; i++)
      {
        Page page = pages[i];
        Point extent = gc.stringExtent(page.getTitle());
        int width = extent.x;
        minWidth = Math.min(minWidth, width);
        maxWidth = Math.max(maxWidth, width);

        boxes[i - 1] = new Rectangle(0, 0, offsetX + width, extent.y + 4);
      }

      int width = (minWidth + maxWidth) / 2 + offsetX;

      for (int i = 1; i < GEARS; i++)
      {
        int x = (PAGE_WIDTH - width) / 2;
        int y = 40 * (1 + i);

        QuestionPage page = (QuestionPage)pages[i];
        ImageAnswer answer = (ImageAnswer)page.getChoiceAnswer();
        if (answer != null)
        {
          gc.drawImage(answer.images[4], x, y + 8);
          gc.setForeground(purple);
        }
        else
        {
          gc.drawImage(question, x, y + 8);
          gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));
        }

        String title = page.getTitle();
        if (title.endsWith("?"))
        {
          title = title.substring(0, title.length() - 1);
        }

        gc.drawText(title, x + offsetX, y);
        boxes[i - 1].x = x;
        boxes[i - 1].y = y + 4;
      }
    }
  }
}
