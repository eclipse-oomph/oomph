/*
 * Copyright (c) 2020 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ed Merks
 */
class Screenshot
{
  private static final File LOCATION;

  private static int SCALE;

  static
  {
    File folderLocation = null;
    int scale = 100;
    String location = PropertiesUtil.getProperty("org.eclipse.oomph.ui.screenshot"); //$NON-NLS-1$
    if (location != null)
    {
      File file = new File(location);
      if (file.isDirectory())
      {
        folderLocation = file;
      }

      String scaleProperty = PropertiesUtil.getProperty("org.eclipse.oomph.ui.screenshot.scale"); //$NON-NLS-1$
      if (scaleProperty != null)
      {
        scale = Integer.parseInt(scaleProperty);
      }
    }

    LOCATION = folderLocation;
    SCALE = scale;
  }

  private static final Display DISPLAY;

  static
  {
    Display display = null;
    if (LOCATION != null)
    {
      try
      {
        display = Display.getDefault();
      }
      catch (Throwable throwable)
      {
        //$FALL-THROUGH$
      }
    }

    DISPLAY = display;
  }

  Screenshot()
  {
    if (DISPLAY != null && LOCATION != null)
    {
      DISPLAY.asyncExec(new Runnable()
      {
        @Override
        public void run()
        {
          DisplayMenuListener displayMenuListener = new DisplayMenuListener();
          DisplayMouseDownListener displayMouseUpListener = new DisplayMouseDownListener();
          DISPLAY.addFilter(SWT.MouseDown, displayMouseUpListener);

          DISPLAY.addFilter(SWT.KeyDown, new DisplayKeyDownListener(displayMenuListener, displayMouseUpListener));

          DISPLAY.addFilter(SWT.Show, displayMenuListener);
          DISPLAY.addFilter(SWT.Hide, displayMenuListener);
        }
      });
    }
  }

  /**
   * @author Ed Merks
   */
  private static final class DisplayMouseDownListener implements Listener
  {
    private ToolBar toolBar;

    @Override
    public void handleEvent(Event event)
    {
      if (event.widget instanceof ToolBar)
      {
        toolBar = (ToolBar)event.widget;
      }
      else
      {
        toolBar = null;
      }
    }
  }

  /**
   * @author Ed Merks
   */
  private static final class DisplayMenuListener implements Listener
  {
    private List<Menu> menus = new ArrayList<>();

    @Override
    public void handleEvent(Event event)
    {
      if (event.widget instanceof Menu)
      {
        Menu menu = (Menu)event.widget;
        if (event.type == SWT.Show)
        {
          menus.add(menu);
        }
        else
        {
          menus.remove(menu);
        }
      }
    }
  }

  /**
   * @author Ed Merks
   */
  private static final class DisplayKeyDownListener implements Listener
  {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss"); //$NON-NLS-1$

    private final DisplayMenuListener displayMenuListener;

    private final DisplayMouseDownListener displayMouseUpListener;

    private long lastControl;

    private long lastShift;

    public DisplayKeyDownListener(DisplayMenuListener displayMenuListener, DisplayMouseDownListener displayMouseUpListener)
    {
      this.displayMenuListener = displayMenuListener;
      this.displayMouseUpListener = displayMouseUpListener;
    }

    @Override
    public void handleEvent(Event event)
    {
      long now = System.currentTimeMillis();
      if ((event.stateMask & SWT.MODIFIER_MASK) == 0 && event.keyCode == SWT.CONTROL)
      {
        if (now - lastControl < 500)
        {
          UIPlugin.INSTANCE.log("immediate-capture", IStatus.INFO); //$NON-NLS-1$
          new Capture(new File(LOCATION, DATE_FORMAT.format(new Date(System.currentTimeMillis()))), displayMenuListener.menus, displayMouseUpListener.toolBar)
              .capture();
          displayMouseUpListener.toolBar = null;
        }
        else
        {
          lastControl = now;
        }
      }
      else if ((event.stateMask & SWT.MODIFIER_MASK) == 0 && event.keyCode == SWT.SHIFT)
      {
        if (now - lastShift < 500)
        {
          UIPlugin.INSTANCE.log("delay-capture", IStatus.INFO); //$NON-NLS-1$
          DISPLAY.timerExec(5000, new Runnable()
          {
            @Override
            public void run()
            {
              UIPlugin.INSTANCE.log("capturing-now", IStatus.INFO); //$NON-NLS-1$
              new Capture(new File(LOCATION, DATE_FORMAT.format(new Date(System.currentTimeMillis()))), displayMenuListener.menus,
                  displayMouseUpListener.toolBar).capture();
              displayMouseUpListener.toolBar = null;
            }
          });
        }
        else
        {
          lastShift = now;
        }
      }
      else
      {
        lastControl = 0;
        lastShift = 0;
      }
    }
  }

  /**
   * @author Ed Merks
   */
  private static final class Capture
  {
    private final File folder;

    private final List<Menu> menus;

    private final ToolBar toolBar;

    private final List<Rectangle> itemBounds = new ArrayList<>();

    public Capture(File folder, List<Menu> menus, ToolBar toolBar)
    {
      this.folder = folder;
      this.menus = menus;
      this.toolBar = toolBar;
      folder.mkdirs();
    }

    public void capture()
    {
      Control focusControl = DISPLAY.getFocusControl();
      if (focusControl != null)
      {
        Control effectiveControl = toolBar == null ? focusControl : toolBar;
        capture(0, "FocusControl", effectiveControl); //$NON-NLS-1$
        captureMenus(effectiveControl);
      }
    }

    private void captureMenus(Control control)
    {
      if (!menus.isEmpty())
      {
        int count = 0;
        List<Rectangle> allBounds = new ArrayList<>();
        for (Menu menu : menus)
        {
          Decorations parent = menu.getParent();
          Rectangle bounds = ReflectUtil.invokeMethod("getBounds", menu); //$NON-NLS-1$

          // Expand to include the border.
          --bounds.x;
          --bounds.y;
          bounds.width += 2;
          bounds.height += 2;

          captureDrawable(parent, bounds, "Menu" + toString(++count) + ".png"); //$NON-NLS-1$ //$NON-NLS-2$
          allBounds.add(bounds);
        }

        Shell shell = control.getShell();
        Menu menuBar = shell.getMenuBar();
        if (menuBar != null)
        {
          Decorations parent = menuBar.getParent();
          Rectangle bounds = ReflectUtil.invokeMethod("getBounds", menuBar); //$NON-NLS-1$
          captureDrawable(parent, bounds, "MenuBar.png"); //$NON-NLS-1$
          for (MenuItem menuItem : menuBar.getItems())
          {
            Rectangle menuItemBounds = ReflectUtil.invokeMethod("getBounds", menuItem); //$NON-NLS-1$
            Geometry.moveRectangle(menuItemBounds, new Point(bounds.x, bounds.y));
            itemBounds.add(menuItemBounds);
          }
        }

        if (!allBounds.isEmpty())
        {
          captureDrawable(DISPLAY, allBounds, "Menu" + toString(++count) + ".png"); //$NON-NLS-1$ //$NON-NLS-2$

          for (Rectangle itemBound : itemBounds)
          {
            Rectangle copy = Geometry.copy(itemBound);
            Geometry.expand(copy, 0, 0, 0, 2);
            if (copy.intersects(allBounds.get(0)))
            {
              allBounds.add(0, itemBound);
              captureDrawable(DISPLAY, allBounds, "Menu" + toString(++count) + ".png"); //$NON-NLS-1$ //$NON-NLS-2$
              break;
            }
          }
        }
      }
    }

    private void capture(int count, String fileName, Control control)
    {
      Point size = control.getSize();
      Point preferredSize = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
      if (preferredSize.x < size.x && preferredSize.y < size.y)
      {
        captureDrawable(control, new Rectangle(0, 0, preferredSize.x, preferredSize.y), fileName + toString(++count) + ".png"); //$NON-NLS-1$
      }

      captureDrawable(control, new Rectangle(0, 0, size.x, size.y), fileName + toString(++count) + ".png"); //$NON-NLS-1$

      if (control instanceof ToolBar)
      {
        int toolItemCount = 0;
        ToolBar toolBar = (ToolBar)control;
        for (ToolItem toolItem : toolBar.getItems())
        {
          Rectangle bounds = toolItem.getBounds();
          captureDrawable(toolBar, bounds, "ToolItem" + toString(++toolItemCount) + ".png"); //$NON-NLS-1$ //$NON-NLS-2$
          Point displayPoint = toolBar.toDisplay(bounds.x, bounds.y);
          bounds.x = displayPoint.x;
          bounds.y = displayPoint.y;
          itemBounds.add(bounds);
        }
      }

      Composite parent = control.getParent();
      if (parent != null)
      {
        capture(count, parent);
      }
    }

    private void capture(int count, Composite composite)
    {
      Point size = composite.getSize();
      if (composite instanceof Shell)
      {
        Shell shell = (Shell)composite;
        Rectangle bounds = shell.getBounds();

        // Include the shadow around the top level shell.
        Composite parent = shell.getParent();
        if (parent == null)
        {
          bounds.x -= 16;
          bounds.y -= 16;
          bounds.width += 32;
          bounds.height += 32;
        }

        captureDrawable(composite, bounds, "Parent" + toString(count) + ".png"); //$NON-NLS-1$ //$NON-NLS-2$

        // Also take a shot with the borders.
        if (parent != null)
        {
          Point clientOffset = ((Shell)parent).toControl(shell.toDisplay(0, 0));
          Point targetPoint = shell.toDisplay(clientOffset);

          bounds.x -= 16;
          bounds.y -= 16;
          bounds.width += 32;
          bounds.height += 32;

          captureDrawable(composite, bounds, "Parent" + toString(count) + ".png"); //$NON-NLS-1$ //$NON-NLS-2$

          if (parent.getData() instanceof IWorkbenchWindow)
          {
            IWorkbenchWindow workbenchWindow = (IWorkbenchWindow)parent.getData();
            IWorkbenchPage activePage = workbenchWindow.getActivePage();
            if (activePage != null)
            {
              IWorkbenchPartReference activePartReference = activePage.getActivePartReference();
              if (activePartReference != null)
              {
                Object part = get(activePartReference, "part", Object.class); //$NON-NLS-1$
                Composite activePartComposite = get(part, "widget", Composite.class); //$NON-NLS-1$
                Control control = findControl(activePartComposite, targetPoint);
                if (control != activePartComposite)
                {
                  capture(count, "Parent", activePartComposite); //$NON-NLS-1$
                }
                else
                {
                  capture(count + 1, activePartComposite);
                }
                return;
              }
            }
          }
        }
      }
      else
      {
        captureDrawable(composite, new Rectangle(0, 0, size.x, size.y), "Parent" + toString(count) + ".png"); //$NON-NLS-1$ //$NON-NLS-2$
      }

      // Don't capture parents that cover exactly the same area.
      for (Composite parent = composite.getParent(); parent != null; parent = parent.getParent())
      {

        if (!parent.getSize().equals(size))
        {
          capture(count + 1, parent);
          break;
        }
      }
    }

    private void captureDrawable(Drawable drawable, Rectangle bounds, String fileName)
    {
      GC gc = new GC(DISPLAY);

      Image image = newImage(bounds);

      Point displayPoint = toDisplay(drawable, new Point(bounds.x, bounds.y));
      gc.copyArea(image, scaleUp(displayPoint.x), scaleUp(displayPoint.y));

      drawCaret(drawable, gc, image);

      GC imageGC = new GC(image);
      drawCursor(drawable, imageGC, bounds);
      imageGC.dispose();

      gc.dispose();

      save(image, fileName);
      image.dispose();
    }

    private void captureDrawable(Drawable drawable, List<Rectangle> allBounds, String fileName)
    {
      Rectangle imageBounds = null;
      for (Rectangle rectangle : allBounds)
      {
        if (imageBounds == null)
        {
          imageBounds = new Rectangle(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        }
        else
        {
          imageBounds.add(rectangle);
        }
      }

      GC gc = new GC(DISPLAY);

      Map<Image, Point> images = new LinkedHashMap<>();
      for (Rectangle bounds : allBounds)
      {
        Image image = newImage(bounds);
        Point imagePoint = new Point(bounds.x, bounds.y);
        Point displayPoint = toDisplay(drawable, imagePoint);
        gc.copyArea(image, scaleUp(displayPoint.x), scaleUp(displayPoint.y));
        drawCaret(drawable, gc, image);
        GC imageGC = new GC(image);
        drawCursor(drawable, imageGC, bounds);
        imageGC.dispose();
        images.put(image, imagePoint);
      }

      Image image = newImage(imageBounds, true);
      GC imageGC = new GC(image);

      // // Make the image transparent.
      // imageGC.setAlpha(0);
      // imageGC.setBackground(DISPLAY.getSystemColor(SWT.COLOR_WHITE));
      // imageGC.fillRectangle(-1, -1, imageBounds.width + 2, imageBounds.height + 2);
      // imageGC.setAlpha(255);

      for (Map.Entry<Image, Point> entry : images.entrySet())
      {
        Image childImage = entry.getKey();
        Point point = entry.getValue();
        Point targetPoint = new Point(point.x - imageBounds.x, point.y - imageBounds.y);
        drawImage(imageGC, childImage, targetPoint.x, targetPoint.y);
        childImage.dispose();
      }

      imageGC.dispose();

      gc.dispose();

      save(image, fileName);
      image.dispose();
    }

    private Point toDisplay(Drawable drawable, Point point)
    {
      return drawable instanceof Control && !(drawable instanceof Shell) ? ((Control)drawable).toDisplay(point) : point;
    }

    private Control findControl(Composite composite, Point targetPoint)
    {
      Control[] children = composite.getChildren();
      for (Control child : children)
      {
        Rectangle bounds = child.getBounds();
        Point displayPoint = child.toDisplay(bounds.x, bounds.y);
        bounds.x = displayPoint.x;
        bounds.y = displayPoint.y;
        if (bounds.contains(targetPoint))
        {
          return child instanceof Composite ? findControl((Composite)child, targetPoint) : child;
        }
      }
      return composite;
    }

    private void drawCursor(Drawable drawable, GC gc, Rectangle bounds)
    {
      Control cursorControl = DISPLAY.getCursorControl();
      Cursor cursor = cursorControl instanceof Text ? DISPLAY.getSystemCursor(SWT.CURSOR_IBEAM) : cursorControl == null ? null : cursorControl.getCursor();
      MouseCursor mouseCursor = MouseCursor.getMouseCursor(cursor);
      Point cursorLocation = DISPLAY.getCursorLocation();
      if (drawable instanceof Control)
      {
        cursorLocation = ((Control)drawable).toControl(cursorLocation);
        if (drawable instanceof Shell)
        {
          Shell shell = (Shell)drawable;
          Point clientOffset = shell.toControl(bounds.x, bounds.y);
          cursorLocation.x -= clientOffset.x;
          cursorLocation.y -= clientOffset.y;
        }
      }
      else if (drawable instanceof Display)
      {
        cursorLocation.x -= bounds.x;
        cursorLocation.y -= bounds.y;
      }

      mouseCursor.drawMouseCursor(gc, cursorLocation);
    }

    private void drawCaret(Drawable drawable, GC gc, Image image)
    {
      try
      {
        Caret caret = invoke(drawable, "getCaret", Caret.class); //$NON-NLS-1$
        if (caret != null && caret.getVisible())
        {
          GC imageGC = new GC(image);
          imageGC.setBackground(gc.getForeground());

          Rectangle caretBounds = caret.getBounds();
          imageGC.fillRectangle(scaleUp(caretBounds));

          imageGC.dispose();
        }
      }
      catch (Exception exception)
      {
      }
    }

    private void save(Image image, String fileName)
    {
      ImageLoader loader = new ImageLoader();
      loader.data = new ImageData[] { image.getImageData() };
      loader.save(new File(folder, fileName).toString(), SWT.IMAGE_PNG);
    }

    private Image newImage(Rectangle bounds)
    {
      return newImage(bounds, false);
    }

    private Image newImage(Rectangle bounds, boolean transparent)
    {
      Image image = new Image(DISPLAY, scaleUp(bounds.width), scaleUp(bounds.height));
      if (transparent)
      {
        ImageData imageData = image.getImageData();
        imageData.transparentPixel = imageData.getPixel(0, 0);
        Image transparentImage = new Image(DISPLAY, imageData);
        image.dispose();
        return transparentImage;
      }

      return image;
    }

    private <T> T invoke(Object object, String methodName, Class<T> returnType)
    {
      try
      {
        return returnType.cast(object.getClass().getMethod(methodName).invoke(object));
      }
      catch (Exception exception)
      {
        return null;
      }
    }

    private <T> T get(Object object, String fieldName, Class<T> returnType)
    {
      try
      {
        for (Class<?> aClass = object.getClass(); aClass != null; aClass = aClass.getSuperclass())
        {
          try
          {
            Field field = aClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return returnType.cast(field.get(object));
          }
          catch (Exception exception)
          {
          }
        }
      }
      catch (Exception exception)
      {
        throw new RuntimeException(exception);
      }

      throw new RuntimeException("No such field"); //$NON-NLS-1$
    }

    private static int scaleUp(int value)
    {
      return SCALE * value / 100;
    }

    private static Rectangle scaleUp(Rectangle rectangle)
    {
      return new Rectangle(scaleUp(rectangle.x), scaleUp(rectangle.y), scaleUp(rectangle.width), scaleUp(rectangle.height));
    }

    private static void drawImage(GC gc, Image image, int x, int y)
    {
      Rectangle bounds = image.getBounds();
      int width = bounds.width;
      int height = bounds.height;
      gc.drawImage(image, 0, 0, width, height, scaleUp(x), scaleUp(y), scaleUp(width), scaleUp(height));
    }

    private String toString(int count)
    {
      String result = Integer.toString(count);
      while (result.length() < 2)
      {
        result = "0" + result; //$NON-NLS-1$
      }

      return result;
    }

    /**
     * @author Ed Merks
     */
    private static class MouseCursor
    {
      private static final Map<Cursor, MouseCursor> CURSORS = new HashMap<>();

      private Image image;

      private Point hotspot;

      public static MouseCursor getMouseCursor(Cursor cursor)
      {
        MouseCursor mouseCursor = CURSORS.get(cursor);
        if (mouseCursor == null)
        {
          if (cursor == DISPLAY.getSystemCursor(SWT.CURSOR_IBEAM))
          {
            mouseCursor = new MouseCursor();
            mouseCursor.image = getCursorImage("IBeam"); //$NON-NLS-1$
            Rectangle bounds = mouseCursor.image.getBounds();
            mouseCursor.hotspot = new Point(1, (bounds.height - 1) / 2);
          }
          else
          {
            mouseCursor = new MouseCursor();
            mouseCursor.image = getCursorImage("Default"); //$NON-NLS-1$
            mouseCursor.hotspot = new Point(0, 0);
          }
        }
        return mouseCursor;
      }

      public void drawMouseCursor(GC gc, Point location)
      {
        drawImage(gc, image, location.x - hotspot.x, location.y - hotspot.y);
      }

      private static Image getCursorImage(String type)
      {
        return UIPlugin.INSTANCE.getSWTImage(type.toLowerCase() + "-cursor.png"); //$NON-NLS-1$
      }
    }
  }
}
