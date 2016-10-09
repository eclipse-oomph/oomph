/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.oomph.internal.ui.UIPlugin;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PerspectiveAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A Dialog in a non-modal shell associated with a workbench window where it can be docked.
 * Generally the constructor is not used to create an instance but rather the {@link #openFor(Class, Factory, IWorkbenchWindow) open} method which maintains one instance per workbench window.
 *
 * @author Ed Merks
 */
public abstract class DockableDialog extends Dialog
{
  public interface Factory<T extends DockableDialog>
  {
    public T create(IWorkbenchWindow workbenchWindow);
  }

  /**
   * There can be at most one per workbench window.
   */
  private static final Map<IWorkbenchWindow, Map<Class<?>, DockableDialog>> DIALOGS = new HashMap<IWorkbenchWindow, Map<Class<?>, DockableDialog>>();

  /**
   * Remember where the dialog is docked per workbench window.
   */
  private static final Map<IWorkbenchWindow, Map<Class<?>, Set<IWorkbenchPartReference>>> DOCKED_PARTS = new HashMap<IWorkbenchWindow, Map<Class<?>, Set<IWorkbenchPartReference>>>();

  private final IWorkbenchWindow workbenchWindow;

  private final List<WeakReference<IAction>> actions = new ArrayList<WeakReference<IAction>>();

  protected DockableDialog(IWorkbenchWindow workbenchWindow)
  {
    super(workbenchWindow.getShell());

    setShellStyle(getShellStyle() ^ SWT.APPLICATION_MODAL | SWT.MODELESS | SWT.RESIZE | SWT.MAX | SWT.MIN);
    setBlockOnOpen(false);

    this.workbenchWindow = workbenchWindow;
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    super.configureShell(newShell);
  }

  protected abstract boolean handleWorkbenchPart(IWorkbenchPart part);

  /**
   * The associated action will be checked and unchecked based on the visibility of the dialog.
   */
  public void associate(IAction action)
  {
    if (!getActions().contains(action))
    {
      actions.add(new WeakReference<IAction>(action));
      action.setChecked(true);
    }
  }

  public List<IAction> getActions()
  {
    List<IAction> result = new ArrayList<IAction>();
    for (Iterator<WeakReference<IAction>> it = actions.iterator(); it.hasNext();)
    {
      WeakReference<IAction> actionReference = it.next();
      IAction referencedAction = actionReference.get();
      if (referencedAction == null)
      {
        it.remove();
      }
      else
      {
        result.add(referencedAction);
      }
    }

    return result;
  }

  public IWorkbenchWindow getWorkbenchWindow()
  {
    return workbenchWindow;
  }

  public void setWorkbenchPart(IWorkbenchPart part)
  {
    // If there is no setup editor, hide the shell, otherwise show the shell.
    Shell shell = getShell();
    if (!handleWorkbenchPart(part))
    {
      shell.setVisible(false);
      shell.setData("forced", true);
      shell.setMinimized(true);
      shell.notifyListeners(SWT.Iconify, new Event());
    }
    else if (Boolean.TRUE.equals(shell.getData("forced")))
    {
      shell.setMinimized(false);
      shell.notifyListeners(SWT.Deiconify, new Event());
      shell.setData("forced", false);
      shell.setVisible(true);
    }
  }

  private static Image[] getDockedImages(Image[] images)
  {
    Image[] dockedImages = new Image[images.length];
    for (int i = 0, length = images.length; i < length; ++i)
    {
      dockedImages[i] = getDockedImage(images[i]);
    }

    return dockedImages;
  }

  private static Image getDockedImage(Image image)
  {
    if (image == null)
    {
      return null;
    }

    List<Image> images = new ArrayList<Image>();
    images.add(image);
    images.add(UIPlugin.INSTANCE.getSWTImage("docked_overlay"));
    return ExtendedImageRegistry.INSTANCE.getImage(new DockedOverlayImage(images));
  }

  private static class DockedOverlayImage extends ComposedImage
  {
    private DockedOverlayImage(Collection<?> images)
    {
      super(images);
    }

    @Override
    public List<Point> getDrawPoints(Size size)
    {
      List<Point> result = new ArrayList<Point>();
      result.add(new Point());
      Point overlay = new Point();
      overlay.x = size.width - 7;
      overlay.y = size.height - 8;
      result.add(overlay);
      return result;
    }
  }

  /**
   * Return the instance for this workbench window, if there is one.
   */
  public static <T extends DockableDialog> T getFor(Class<T> type, IWorkbenchWindow workbenchWindow)
  {
    Map<Class<?>, DockableDialog> typedDialogs = DIALOGS.get(workbenchWindow);
    if (typedDialogs != null)
    {
      @SuppressWarnings("unchecked")
      T dockableDialog = (T)typedDialogs.get(type);
      return dockableDialog;
    }

    return null;
  }

  /**
   * Close the instance for this workbench window, if there is one.
   */
  public static void closeFor(Class<? extends DockableDialog> type, IWorkbenchWindow workbenchWindow)
  {
    Map<Class<?>, DockableDialog> typedDialogs = DIALOGS.get(workbenchWindow);
    if (typedDialogs != null)
    {
      DockableDialog dockableDialog = typedDialogs.get(type);
      if (dockableDialog != null)
      {
        Shell shell = dockableDialog.getShell();
        shell.notifyListeners(SWT.Close, new Event());
        dockableDialog.close();
      }
    }
  }

  /**
   * Reopen or create the instance for this workbench window.
   */
  public static <T extends DockableDialog> T openFor(final Class<T> type, Factory<T> factory, final IWorkbenchWindow workbenchWindow)
  {
    // Create a new one if there isn't an existing one.
    Map<Class<?>, DockableDialog> typedDialogs = DIALOGS.get(workbenchWindow);

    @SuppressWarnings("unchecked")
    T dockableDialog = (T)(typedDialogs == null ? null : typedDialogs.get(type));
    if (dockableDialog == null)
    {
      dockableDialog = factory.create(workbenchWindow);
      if (typedDialogs == null)
      {
        typedDialogs = new HashMap<Class<?>, DockableDialog>();
        DIALOGS.put(workbenchWindow, typedDialogs);
      }

      typedDialogs.put(type, dockableDialog);

      dockableDialog.open();

      Map<Class<?>, Set<IWorkbenchPartReference>> typedDockedParts = DOCKED_PARTS.get(workbenchWindow);
      Set<IWorkbenchPartReference> dockedParts = typedDockedParts == null ? null : typedDockedParts.get(type);
      if (dockedParts == null)
      {
        dockedParts = new HashSet<IWorkbenchPartReference>();
        if (typedDockedParts == null)
        {
          typedDockedParts = new HashMap<Class<?>, Set<IWorkbenchPartReference>>();
          DOCKED_PARTS.put(workbenchWindow, typedDockedParts);
        }

        typedDockedParts.put(type, dockedParts);
      }

      // Be sure to clean it from the map when either the workbench window or the dialog itself is disposed.
      DisposeListener disposeListener = new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          DIALOGS.remove(workbenchWindow);
          DOCKED_PARTS.remove(workbenchWindow);
        }
      };

      // Clean up if the workbench window is disposed.
      final Shell windowShell = workbenchWindow.getShell();
      windowShell.addDisposeListener(disposeListener);

      /**
       * This monitors the shell events, such as dragging to a new position.
       * It's primary purpose to to support docking of the dialog shell into a part stack on a workbench window.
       *
       * @author Ed Merks
       */
      class ShellHandler extends ShellAdapter implements ControlListener, DisposeListener, Runnable
      {
        private final DockableDialog dockableDialog;

        private final Shell shell;

        private final Display display;

        /**
         * A map from a tab folder (corresponding to a part stack) to the absolute display bounds where it is located.
         */
        private final Map<CTabFolder, Rectangle> tabFolders = new HashMap<CTabFolder, Rectangle>();

        /**
         * A map from a tab folder to the part references in that part stack.
         */
        private final Map<CTabFolder, Set<IWorkbenchPartReference>> tabFolderParts = new HashMap<CTabFolder, Set<IWorkbenchPartReference>>();

        /**
         * The cursor we show when in a docking location.
         */
        private final Cursor sizeAllCursor;

        /**
         * A listener that listens to the tab folder and to the overall workbench window to track resizing and movement so that the docking position can be maintained.
         */
        private final ControlListener dockingListener = new ControlListener()
        {
          public void controlResized(ControlEvent e)
          {
            update();
          }

          public void controlMoved(ControlEvent e)
          {
            update();
          }
        };

        /**
         * The point at which we most recently hovered in a location where we could dock the shell.
         */
        private Point snapPoint;

        /**
         * This is true only while we are doing the positioning of the shell.
         */
        private boolean ignoreControlMoved;

        /**
         * Because we don't receive events such as mouse moves or mouse clicks from the shell, we keep track of how and whether the cursor is moving.
         * @see #run()
         */
        private Point cursorLocation;

        /**
         * This records the most recent time the cursor has been moved to a different location.
         * @see #run()
         */
        private long timeOfLastCursorChange;

        /**
         * This is the most recent bounds where we can dock the shell.
         */
        private Rectangle hotZone;

        /**
         * Because we want to be able to drag the docked shell slightly away from the docking site to undock it,
         * we keep track of the hot zone changes and ignore it initially for a short period of time.
         */
        private long timeOfLastHotZoneChange;

        /**
         * It's very hard to clear the hotZone and the snapPoint because we could just get no events when the interaction ends.
         * So we keep track of when the last time a moved happened and if a short period of time passes, we assume we're starting a new interaction.
         */
        private long timeOfLastMove;

        /**
         * This is set after docking.
         * When we hover in a docking position for a short period of time, we set the shell to be at that bounds of that hot zone.
         * But when the mouse is released the shell automatically will move to it's original position, for which we get an event,
         * and then we can use these bounds to set the shell back yet again to the right bounds.
         *
         * @see #dock(Rectangle)
         */
        private Rectangle snapBounds;

        /**
         * This the part stack where we are docked and that we will track when it resizes or moves.
         */
        private CTabFolder dockedTabFolder;

        /**
         * These are the part references at the part stack.
         * We record these so that if we turn to a new perspective, we can try to dock at a different part stack that contains one of the part references.
         */
        private final Set<IWorkbenchPartReference> dockedParts;

        private final Image shellImage;

        private final Image dockedShellImage;

        private final Image[] shellImages;

        private final Image[] dockedShellImages;

        public ShellHandler(DockableDialog dockableDialog, Set<IWorkbenchPartReference> dockedParts)
        {

          this.dockableDialog = dockableDialog;
          shell = dockableDialog.getShell();
          this.dockedParts = dockedParts;

          display = shell.getDisplay();
          sizeAllCursor = display.getSystemCursor(SWT.CURSOR_SIZEALL);

          shell.addShellListener(this);
          shell.addControlListener(this);
          shell.addDisposeListener(this);

          shellImage = shell.getImage();
          dockedShellImage = getDockedImage(shellImage);
          shellImages = shell.getImages();
          dockedShellImages = getDockedImages(shellImages);
        }

        @Override
        public void shellIconified(ShellEvent e)
        {
          shell.setVisible(false);
          for (IAction action : dockableDialog.getActions())
          {
            action.setChecked(false);
          }
        }

        @Override
        public void shellDeiconified(ShellEvent e)
        {
          for (IAction action : dockableDialog.getActions())
          {
            action.setChecked(true);
          }
        }

        public void controlResized(ControlEvent e)
        {
          // Ignore.
        }

        public void controlMoved(ControlEvent e)
        {
          // When the shell is maximized, we get a moved event, and we can use that to clear the snap bounds
          // so that the restore goes back to the right position rather than the snap position.
          //
          boolean maximized = shell.getMaximized();
          if (maximized)
          {
            snapBounds = null;
          }

          // We do nothing we are the ones moving the shell or the shell is maximized.
          if (!ignoreControlMoved && !maximized)
          {
            // We have snap bounds to apply, because releasing the mouse after docking moved the shell yet again, we can apply it now, and we never have to do
            // it again.
            if (snapBounds != null)
            {
              setBounds(snapBounds);
              dock(snapBounds);
              snapBounds = null;
            }
            else
            {
              // If we have a docking site...
              if (dockedTabFolder != null)
              {
                // And we moved outside of it...
                if (!getBounds(dockedTabFolder).equals(shell.getBounds()))
                {
                  // Undock the shell.
                  dock(null);
                }
                else
                {
                  // Otherwise we have moved to exactly the docking site, in which case we don't want to do anything!
                  return;
                }
              }
            }

            // If we haven't moved the shell for a short white, assume we're staring a new interaction.
            long newTimeOfLastMove = System.currentTimeMillis();
            if (newTimeOfLastMove - timeOfLastMove > 1000)
            {
              hotZone = null;
              snapPoint = null;
            }

            timeOfLastMove = newTimeOfLastMove;

            // Determine the hot zone at the cursor location.
            Point cursorLocation = display.getCursorLocation();
            Rectangle newHotZone = getHotZone(cursorLocation);
            if (newHotZone != null)
            {
              // If it's a new one...
              if (!newHotZone.equals(hotZone))
              {
                // Keep track of when we first entered this new hot zone.
                timeOfLastHotZoneChange = System.currentTimeMillis();
                hotZone = newHotZone;
              }

              // If we've been in this hot zone for a short period of time...
              if (System.currentTimeMillis() - timeOfLastHotZoneChange > 400)
              {
                // If we have no snap point for this yet...
                if (snapPoint == null)
                {
                  // Show a different cursor and start the heartbeat runnable.
                  shell.setCursor(sizeAllCursor);
                  updateShellImage(true);
                  display.timerExec(100, this);
                }

                // keep track of where we started.
                snapPoint = cursorLocation;
              }
            }
            else
            {
              // If we're outside of a hot zone, reset all the state.
              shell.setCursor(null);
              updateShellImage(false);
              snapPoint = null;
              hotZone = null;
              dock(null);
            }
          }
        }

        /**
         * This is the heart beat runnable that's started once we've hovered over a hot zone for a short period of time.
         */
        public void run()
        {
          // Keep track of cursor movements, so that only if you hover for a while in the same location will the shell be snapped to the hot zone.
          Point newCursorLocation = display.getCursorLocation();
          if (cursorLocation == null || !cursorLocation.equals(newCursorLocation))
          {
            timeOfLastCursorChange = System.currentTimeMillis();
            cursorLocation = newCursorLocation;
          }

          if (snapPoint == null)
          {
            // Not in hot zone.
            shell.setCursor(null);
          }
          else if (System.currentTimeMillis() - timeOfLastCursorChange > 500)
          {
            // Get the bounds for the hot zone, and dock to that location.
            snapBounds = getHotZone(snapPoint);
            setBounds(snapBounds);
            snapPoint = null;
            timeOfLastCursorChange = System.currentTimeMillis();
            dock(snapBounds);
            shell.setCursor(null);
          }
          else
          {
            // Still moving the cursor, so check again in a while.
            display.timerExec(100, this);
          }
        }

        /**
         * Returns the hot zone rectangle if the cursor is within the tab area of that zone.
         */
        private Rectangle getHotZone(Point cursorLocation)
        {
          for (Rectangle rectangle : getHotZones())
          {
            final Rectangle hotZone = new Rectangle(rectangle.x, rectangle.y, rectangle.width, 30);
            if (hotZone.contains(cursorLocation))
            {
              return rectangle;
            }
          }

          return null;
        }

        /**
         * We call this when we set to bounds of the shell so that we can ignore the events we're causing ourselves.
         */
        private void setBounds(Rectangle bounds)
        {
          ignoreControlMoved = true;
          shell.setBounds(bounds);
          ignoreControlMoved = false;
        }

        /**
         * Docks the shell at this rectangle.
         * We listen to the tab folder and the workbench window shell, so we need to maintain those listeners properly.
         */
        private void dock(Rectangle rectangle)
        {
          gatherTabFolders();

          // Remove any existing listeners.
          windowShell.removeControlListener(dockingListener);
          if (dockedTabFolder != null)
          {
            dockedTabFolder.removeControlListener(dockingListener);
          }

          // Clean up the docking points; they'll be populated new, if possible.
          dockedParts.clear();

          for (Map.Entry<CTabFolder, Rectangle> entry : tabFolders.entrySet())
          {
            if (entry.getValue().equals(rectangle))
            {
              // If we find an appropriate part stack,
              // add the listeners and record the information about the docking site and the part references associated with it.
              windowShell.addControlListener(dockingListener);
              dockedTabFolder = entry.getKey();
              dockedTabFolder.addControlListener(dockingListener);
              dockedParts.addAll(tabFolderParts.get(dockedTabFolder));
              updateShellImage(true);
              return;
            }
          }

          // Clear the recorded information to undock the shell.
          dockedTabFolder = null;
          updateShellImage(false);
        }

        private void updateShellImage(boolean docking)
        {
          if (shellImage != null)
          {
            shell.setImage(docking ? dockedShellImage : shellImage);
          }

          if (shellImages != null)
          {
            shell.setImages(docking ? dockedShellImages : shellImages);
          }

        }

        private void dispose()
        {
          // Clear up any docking listeners that might currently be in place.
          if (!windowShell.isDisposed())
          {
            windowShell.removeControlListener(dockingListener);
          }

          if (dockedTabFolder != null && !dockedTabFolder.isDisposed())
          {
            dockedTabFolder.removeControlListener(dockingListener);
          }
        }

        public void widgetDisposed(DisposeEvent e)
        {
          Map<Class<?>, DockableDialog> typedDialogs = DIALOGS.get(workbenchWindow);
          if (typedDialogs != null)
          {
            typedDialogs.remove(type);
          }

          for (IAction action : dockableDialog.getActions())
          {
            action.setChecked(false);
          }

          dispose();
        }

        /**
         * This is called when the docking site or workbench window shell moves or changes because of perspective switching.
         */
        private void update()
        {
          if (!dockedParts.isEmpty() && (dockedTabFolder == null || !dockedTabFolder.isDisposed()))
          {
            gatherTabFolders();

            // If our docking site available or isn't visible...
            if (dockedTabFolder == null || !dockedTabFolder.isVisible())
            {
              // Find a new corresponding docking site, i.e., one that contains one of the part references in the part stack to which we're currently docked.
              for (IWorkbenchPartReference partReference : dockedParts)
              {
                for (Map.Entry<CTabFolder, Set<IWorkbenchPartReference>> entry : tabFolderParts.entrySet())
                {
                  if (entry.getValue().contains(partReference))
                  {
                    // Dock to this new replacement.
                    CTabFolder tabFolder = entry.getKey();
                    Rectangle bounds = getBounds(tabFolder);
                    setBounds(bounds);
                    dock(bounds);

                    // If we've been forced to minimize the shell because there is no docking site in the perspective, but now we do have a docking site,
                    // make the shell visible again.
                    if (shell.getMinimized() && Boolean.TRUE.equals(shell.getData("forced")))
                    {
                      shell.setData("forced", null);
                      shell.setMinimized(false);
                      shell.setVisible(true);
                    }

                    return;
                  }
                }
              }

              if (dockedTabFolder != null)
              {
                // There is no docking site, for minimize the shell and mark that as forced (as opposed to the user having mimimized the shell.
                shell.setMinimized(true);
                shell.setVisible(false);
                shell.setData("forced", true);
              }
            }
            else
            {
              // Get the new bounds for the docking site and apply them.
              // Restore the shell if it's been forced into minimized state.
              Rectangle bounds = getBounds(dockedTabFolder);
              setBounds(bounds);
              if (shell.getMinimized() && Boolean.TRUE.equals(shell.getData("forced")))
              {
                shell.setData("forced", null);
                shell.setMinimized(false);
                shell.setVisible(true);
              }
            }
          }
        }

        private Collection<Rectangle> getHotZones()
        {
          gatherTabFolders();
          return tabFolders.values();
        }

        /**
         * Gets the bounds for the tab folder in display absolute coordinates.
         */
        private Rectangle getBounds(CTabFolder tabFolder)
        {
          Rectangle bounds = tabFolder.getBounds();
          Point displayPoint = tabFolder.getParent().toDisplay(bounds.x, bounds.y);
          bounds.x = displayPoint.x;
          bounds.y = displayPoint.y;
          return bounds;
        }

        public void gatherTabFolders()
        {
          tabFolders.clear();
          tabFolderParts.clear();

          // Visit all the part references of the page.
          IWorkbenchPage page = workbenchWindow.getActivePage();
          if (page != null)
          {
            IViewReference[] viewReferences = page.getViewReferences();
            for (IViewReference viewReference : viewReferences)
            {
              gatherTabFolders(viewReference);
            }

            IEditorReference[] editorReferences = page.getEditorReferences();
            for (IEditorReference editorReference : editorReferences)
            {
              gatherTabFolders(editorReference);
            }
          }
        }

        private void gatherTabFolders(IWorkbenchPartReference partReference)
        {
          // Get the widget associated the the part...
          Object part = ReflectUtil.getValue("part", partReference);
          if (part != null)
          {
            Object widget = ReflectUtil.invokeMethod("getWidget", part);
            if (widget instanceof Control)
            {
              // Walk up until we hit a visible tab folder.
              for (Control control = (Control)widget; control != null; control = control.getParent())
              {
                if (control.isVisible() && control instanceof CTabFolder)
                {
                  // Add it to the map, keeping track of the part references in each part stack.
                  CTabFolder tabFolder = (CTabFolder)control;
                  tabFolders.put(tabFolder, getBounds(tabFolder));
                  CollectionUtil.add(tabFolderParts, tabFolder, partReference);
                }
              }
            }
          }
        }
      }

      // Listen for shell and control events.
      final Shell shell = dockableDialog.getShell();
      final ShellHandler shellHandler = new ShellHandler(dockableDialog, dockedParts);

      // Listen to perspective changes so we can update the docking site as needed.
      workbenchWindow.addPerspectiveListener(new PerspectiveAdapter()
      {
        @Override
        public void perspectiveActivated(IWorkbenchPage page, IPerspectiveDescriptor perspective)
        {
          UIUtil.asyncExec(shell, new Runnable()
          {
            public void run()
            {
              shellHandler.update();
            }
          });
        }
      });

      shellHandler.update();
    }
    else
    {
      // Show the shell if it already exists.
      final Shell shell = dockableDialog.getShell();
      shell.setMinimized(false);
      shell.setVisible(true);
      shell.setData("forced", null);
      shell.setFocus();
    }

    return dockableDialog;
  }
}
