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
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
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
  /**
   * @author Ed Merks
   */
  public static class Dockable
  {
    private final List<WeakReference<IAction>> actions = new ArrayList<WeakReference<IAction>>();

    private Dialog dialog;

    public Dockable(Dialog dialog)
    {
      this.dialog = dialog;
    }

    public Dialog getDialog()
    {
      return dialog;
    }

    public Shell getShell()
    {
      return dialog.getShell();
    }

    public boolean handleWorkbenchPart(IWorkbenchPart part)
    {
      return true;
    }

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

    public IDialogSettings getBoundsSettings()
    {
      return ReflectUtil.invokeMethod("getDialogBoundsSettings", dialog);
    }

    public int open()
    {
      return dialog.open();
    }

    public boolean close()
    {
      return dialog.close();
    }

    public void setWorkbenchPart(IWorkbenchPart part)
    {
      Shell shell = getShell();
      // If there is no support for this workbench part, hide the shell, otherwise show the shell.
      if (!handleWorkbenchPart(part))
      {
        // Only force it invisible if it isn't already invisible.
        // The use may have minimized it himself, so we don't want to mark it forced unless we make it invisible.
        if (shell.isVisible())
        {
          updateVisibility(shell, false);
        }
      }
      else if (Boolean.TRUE.equals(shell.getData("forced")))
      {
        updateVisibility(shell, true);
      }
    }
  }

  /**
   * @author Ed Merks
   */
  public interface Factory<T extends Dialog>
  {
    public T create(IWorkbenchWindow workbenchWindow);
  }

  /**
   * There can be at most one per workbench window.
   */
  private static final Map<IWorkbenchWindow, Map<Class<?>, Dockable>> DIALOGS = new HashMap<IWorkbenchWindow, Map<Class<?>, Dockable>>();

  /**
   * Remember where the dialog is docked per workbench window.
   */
  private static final Map<IWorkbenchWindow, Map<Class<?>, Set<IWorkbenchPartReference>>> DOCKED_PARTS = new HashMap<IWorkbenchWindow, Map<Class<?>, Set<IWorkbenchPartReference>>>();

  /**
   * Be sure to clean the workbench from the map when the workbench window.
   */
  private static final DisposeListener WORKBENCH_DISPOSE_LISTENER = new DisposeListener()
  {
    public void widgetDisposed(DisposeEvent e)
    {
      DIALOGS.remove(e.getSource());
      DOCKED_PARTS.remove(e.getSource());
    }
  };

  private final IWorkbenchWindow workbenchWindow;

  private final Dockable dockable = new Dockable(this)
  {
    @Override
    public boolean handleWorkbenchPart(IWorkbenchPart part)
    {
      return DockableDialog.this.handleWorkbenchPart(part);
    }
  };

  protected DockableDialog(IWorkbenchWindow workbenchWindow)
  {
    super(workbenchWindow.getShell());

    // On the Mac, you can't have an invisible minimized child shell.
    // Minimizing the child shell will minimize the parent shell.
    // On Linux, it just works very poorly.
    setShellStyle(getShellStyle() ^ SWT.APPLICATION_MODAL | SWT.MODELESS | SWT.RESIZE | SWT.MAX | (OS.INSTANCE.isWin() ? SWT.MIN : SWT.NONE));
    setBlockOnOpen(false);

    this.workbenchWindow = workbenchWindow;
  }

  public abstract boolean handleWorkbenchPart(IWorkbenchPart part);

  public Dockable getDockable()
  {
    return dockable;
  }

  public IWorkbenchWindow getWorkbenchWindow()
  {
    return workbenchWindow;
  }

  private static void updateVisibility(Shell shell, boolean visible)
  {
    if (visible)
    {
      shell.setData("forced", null);

      if (OS.INSTANCE.isWin())
      {
        shell.setMinimized(false);
      }

      shell.setVisible(true);
      shell.notifyListeners(SWT.Deiconify, new Event());
    }
    else
    {
      shell.setData("forced", true);

      shell.setVisible(false);
      if (OS.INSTANCE.isWin())
      {
        shell.setMinimized(true);
      }

      shell.notifyListeners(SWT.Iconify, new Event());
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

  /**
   * @author Ed Merks
   */
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
      result.add(overlay);
      return result;
    }
  }

  /**
   * Return the instance for this workbench window, if there is one.
   */
  public static <T extends Dialog> T getFor(Class<T> type, IWorkbenchWindow workbenchWindow)
  {
    Map<Class<?>, Dockable> typedDialogs = DIALOGS.get(workbenchWindow);
    if (typedDialogs != null)
    {
      Dockable dockable = typedDialogs.get(type);
      if (dockable != null)
      {
        @SuppressWarnings("unchecked")
        T dialog = (T)dockable.getDialog();
        return dialog;
      }
    }

    return null;
  }

  /**
   * Close the instance for this workbench window, if there is one.
   */
  public static void closeFor(Class<? extends Dialog> type, IWorkbenchWindow workbenchWindow)
  {
    Map<Class<?>, Dockable> typedDialogs = DIALOGS.get(workbenchWindow);
    if (typedDialogs != null)
    {
      Dockable dockable = typedDialogs.get(type);
      if (dockable != null)
      {
        Shell shell = dockable.getShell();
        shell.notifyListeners(SWT.Close, new Event());
        dockable.close();
      }
    }
  }

  /**
   * Reopen or create the instance for this workbench window.
   */
  public static <T extends Dialog> T openFor(final Class<T> type, Factory<T> factory, final IWorkbenchWindow workbenchWindow)
  {
    // Create a new one if there isn't an existing one.
    Map<Class<?>, Dockable> typedDialogs = DIALOGS.get(workbenchWindow);

    Dockable dockable = typedDialogs == null ? null : typedDialogs.get(type);
    if (dockable == null)
    {
      dockable = ReflectUtil.invokeMethod("getDockable", factory.create(workbenchWindow));
      if (typedDialogs == null)
      {
        typedDialogs = new HashMap<Class<?>, Dockable>();
        DIALOGS.put(workbenchWindow, typedDialogs);
      }

      typedDialogs.put(type, dockable);

      dockable.open();

      Map<Class<?>, Set<IWorkbenchPartReference>> typedDockedParts = DOCKED_PARTS.get(workbenchWindow);
      Set<IWorkbenchPartReference> dockedParts = typedDockedParts == null ? null : typedDockedParts.get(type);
      boolean isInitial = false;
      if (dockedParts == null)
      {
        isInitial = true;
        dockedParts = new HashSet<IWorkbenchPartReference>();
        if (typedDockedParts == null)
        {
          typedDockedParts = new HashMap<Class<?>, Set<IWorkbenchPartReference>>();
          DOCKED_PARTS.put(workbenchWindow, typedDockedParts);
        }

        typedDockedParts.put(type, dockedParts);
      }

      // Clean up if the workbench window is disposed.
      final Shell windowShell = workbenchWindow.getShell();
      windowShell.addDisposeListener(WORKBENCH_DISPOSE_LISTENER);

      /**
       * This monitors the shell events, such as dragging to a new position.
       * It's primary purpose to to support docking of the dialog shell into a part stack on a workbench window.
       *
       * @author Ed Merks
       */
      class ShellHandler extends ShellAdapter implements ControlListener, DisposeListener, Runnable
      {
        private final Dockable dockableDialog;

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
         * This records the most recent time {@link #update()} called {@link #setBounds(Rectangle)}.
         * @see #run()
         */
        private long timeOfLastUpdate;

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

        public ShellHandler(Dockable dockableDialog, Set<IWorkbenchPartReference> dockedParts)
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
          updateActions(false);

          if (!OS.INSTANCE.isMac() && shell.isVisible())
          {
            shell.setVisible(false);
          }
        }

        @Override
        public void shellDeiconified(ShellEvent e)
        {
          if (shell.isVisible())
          {
            updateActions(true);
          }
        }

        protected void updateActions(boolean checked)
        {
          for (IAction action : dockableDialog.getActions())
          {
            action.setChecked(checked);
          }
        }

        public void controlResized(ControlEvent e)
        {
          if (OS.INSTANCE.isMac() && !ignoreControlMoved)
          {
            dock(null);
          }
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

          // We do nothing if we are the ones moving the shell or the shell is maximized.
          // On Linux we see moves of the shell even when we did the update so use the time to guard it.
          if (!ignoreControlMoved && !maximized && (!OS.INSTANCE.isLinux() || System.currentTimeMillis() - timeOfLastUpdate > 500))
          {
            // We have snap bounds to apply, because releasing the mouse after docking moved the shell yet again, we can apply it now, and we never have to do
            // it again.
            if (snapBounds != null)
            {
              if (!OS.INSTANCE.isMac() || System.currentTimeMillis() - timeOfLastMove < 200)
              {
                setBounds(snapBounds);
                dock(snapBounds);
              }

              snapBounds = null;
            }
            // If we have a docking site...
            else if (dockedTabFolder != null)
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

            // If we haven't moved the shell for a short while, assume we're staring a new interaction.
            long newTimeOfLastMove = System.currentTimeMillis();
            if (newTimeOfLastMove - timeOfLastMove > 1000)
            {
              // On the Mac, we don't see any move events until the user stops moving the shell for a short while.
              // As such, the very first move event could be the last move event we'll ever see.
              // So in this case, start a new interaction only if we've not seen the cursor move for while.
              if (!OS.INSTANCE.isMac() || newTimeOfLastMove - timeOfLastCursorChange > 1000)
              {
                hotZone = null;
                snapPoint = null;
                snapBounds = null;
              }

              if (OS.INSTANCE.isMac())
              {
                // For the Mac, we want to start the heart beat runnable and there we'll monitor whether the cursor stays over the shell title for a period of
                // time.
                display.timerExec(100, this);
              }
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
                  // Show a different cursor and start the heart beat runnable.
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
          if (shell.isDisposed())
          {
            return;
          }

          // Keep track of cursor movements, so that only if you hover for a while in the same location will the shell be snapped to the hot zone.
          Point newCursorLocation = display.getCursorLocation();
          if (cursorLocation == null || !cursorLocation.equals(newCursorLocation))
          {
            timeOfLastCursorChange = System.currentTimeMillis();
            cursorLocation = newCursorLocation;
          }

          // On the Mac, we'll check each time of the cursor control is not any part of any other window managed by SWT, i.e., it's staying inside the title
          // area of the shell.
          if (snapPoint == null && (!OS.INSTANCE.isMac() || display.getCursorControl() != null))
          {
            // Not in hot zone.
            shell.setCursor(null);
          }
          else if (System.currentTimeMillis() - timeOfLastCursorChange > 500)
          {
            // Get the bounds for the hot zone, and dock to that location.
            // The case of the snapPoint being null happens only on the Mac, in which case we use the current cursor location as the snap point.
            snapBounds = getHotZone(snapPoint == null ? cursorLocation : snapPoint);
            if (snapBounds != null)
            {
              setBounds(snapBounds);
              snapPoint = null;
              timeOfLastCursorChange = System.currentTimeMillis();
              dock(snapBounds);
              shell.setCursor(null);
            }
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
          // On Linux we'll see later event for the move; one that we want to ignore.
          if (OS.INSTANCE.isLinux())
          {
            snapBounds = bounds;
            timeOfLastMove = System.currentTimeMillis();
          }

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
          Map<Class<?>, Dockable> typedDialogs = DIALOGS.get(workbenchWindow);
          if (typedDialogs != null)
          {
            typedDialogs.remove(type);
          }

          for (IAction action : dockableDialog.getActions())
          {
            action.setChecked(false);
          }

          IDialogSettings dialogBoundsSettings = dockableDialog.getBoundsSettings();
          if (dialogBoundsSettings != null)
          {
            StringBuilder partIDs = new StringBuilder();
            for (IWorkbenchPartReference partReference : dockedParts)
            {
              if (partIDs.length() != 0)
              {
                partIDs.append(' ');
              }

              partIDs.append(partReference.getId());
            }

            dialogBoundsSettings.put("dockedParts", partIDs.toString());
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

                    timeOfLastUpdate = System.currentTimeMillis();

                    // If we've been forced to minimize the shell because there is no docking site in the perspective, but now we do have a docking site,
                    // make the shell visible again.
                    if (!shell.isVisible() && Boolean.TRUE.equals(shell.getData("forced")))
                    {
                      updateVisibility(shell, true);
                    }

                    return;
                  }
                }
              }

              if (dockedTabFolder != null)
              {
                updateVisibility(shell, false);
              }
            }
            else
            {
              // Get the new bounds for the docking site and apply them.
              // Restore the shell if it's been forced into minimized state.
              Rectangle bounds = getBounds(dockedTabFolder);
              setBounds(bounds);
              timeOfLastUpdate = System.currentTimeMillis();
              if (!shell.isVisible() && Boolean.TRUE.equals(shell.getData("forced")))
              {
                updateVisibility(shell, true);
              }
            }
          }
        }

        private void initialize()
        {
          IDialogSettings dialogBoundsSettings = dockableDialog.getBoundsSettings();
          if (dialogBoundsSettings != null)
          {
            String dockedPartIDs = dialogBoundsSettings.get("dockedParts");
            if (!StringUtil.isEmpty(dockedPartIDs))
            {
              gatherTabFolders();
              List<String> ids = StringUtil.explode(dockedPartIDs, " ");
              for (Set<IWorkbenchPartReference> partReferences : tabFolderParts.values())
              {
                for (IWorkbenchPartReference partReference : partReferences)
                {
                  if (ids.contains(partReference.getId()))
                  {
                    dockedParts.add(partReference);
                  }
                }
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

          // Make the bounds one pixel smaller on on sizes so that the shell's resize handles don't completely cover the tab folder's resize handles.
          if (OS.INSTANCE.isMac())
          {
            ++bounds.x;
            bounds.width -= 2;

            ++bounds.y;
            bounds.height -= 2;
          }

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
      final Shell shell = dockable.getShell();
      final ShellHandler shellHandler = new ShellHandler(dockable, dockedParts);

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

      if (isInitial)
      {
        shellHandler.initialize();
      }

      shellHandler.update();
    }
    else
    {
      // Show the shell if it already exists.
      final Shell shell = dockable.getShell();
      updateVisibility(shell, true);
    }

    @SuppressWarnings("unchecked")
    T dialog = (T)dockable.getDialog();
    return dialog;
  }
}
