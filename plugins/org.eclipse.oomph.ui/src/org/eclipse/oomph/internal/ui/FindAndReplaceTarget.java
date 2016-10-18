/*
 * Copyright (c) 2015, 2016 Ed Merks(Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.ui.viewer.IStyledLabelDecorator;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.DelegatingStyledCellLabelProvider;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertySource;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.IFindReplaceTargetExtension;
import org.eclipse.jface.text.IFindReplaceTargetExtension3;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetEntry;
import org.eclipse.ui.views.properties.PropertySheetPage;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ed Merks
 */
public class FindAndReplaceTarget implements IFindReplaceTarget, IFindReplaceTargetExtension, IFindReplaceTargetExtension3
{
  private static final Map<IWorkbenchPart, FindAndReplaceTarget> FIND_AND_REPLACE_TARGETS = new WeakHashMap<IWorkbenchPart, FindAndReplaceTarget>();

  private static final Field FILTER_ACTION_FIELD = ReflectUtil.getField(PropertySheetPage.class, "filterAction");

  private static final Method GET_DESCRIPTOR_METHOD = ReflectUtil.getMethod(PropertySheetEntry.class, "getDescriptor");

  private static final Field OBJECT_FIELD = ReflectUtil.getField(PropertyDescriptor.class, "object");

  private static final Field ITEM_PROPERTY_DESCRIPTOR_FIELD = ReflectUtil.getField(PropertyDescriptor.class, "itemPropertyDescriptor");

  private static final Field ITEM_PROPERTY_SOURCE_FIELD = ReflectUtil.getField(PropertySource.class, "itemPropertySource");

  private static final Styler MATCH_STYLER = new Styler()
  {
    @Override
    public void applyStyles(TextStyle textStyle)
    {
      textStyle.borderStyle = SWT.BORDER_SOLID;
    }
  };

  private IWorkbenchPart workbenchPart;

  private Runnable propertiesCleanup;

  private Runnable sessionCleanup;

  private List<?> selectionScope;

  private Set<Object> selectionScopeObjects;

  private String selectionText;

  private Data.Item selectedItem;

  private int selectedItemStart;

  private Pattern selectedItemPattern;

  private CompoundCommand replaceAllCommand;

  private int pendingReplacements = -1;

  private boolean findReplaceable;

  private FindAndReplaceTarget.SearchType searchType;

  private TreeItem specialTreeItem;

  private int specialStart;

  private boolean suspendScopeChanges;

  public FindAndReplaceTarget(IWorkbenchPart workbenchPart)
  {
    this.workbenchPart = workbenchPart;
  }

  /**
   * Extracts the viewer from the workbench part.
   */
  protected StructuredViewer getViewer()
  {
    if (workbenchPart instanceof IViewerProvider)
    {
      IViewerProvider viewerProvider = (IViewerProvider)workbenchPart;
      Viewer viewer = viewerProvider.getViewer();
      if (viewer instanceof StructuredViewer)
      {
        return (StructuredViewer)viewer;
      }
    }

    return null;
  }

  /**
   * Returns the property sheet page of the workbench page's active property sheet.
   */
  protected PropertySheetPage getActivePropertySheetPage()
  {
    IWorkbenchPart activePart = workbenchPart.getSite().getPage().getActivePart();
    if (activePart instanceof PropertySheet)
    {
      PropertySheet propertySheet = (PropertySheet)activePart;
      IPage currentPage = propertySheet.getCurrentPage();
      if (currentPage != null)
      {
        if (currentPage instanceof PropertySheetPage)
        {
          PropertySheetPage propertySheetPage = (PropertySheetPage)currentPage;
          return propertySheetPage;
        }
      }
    }

    return null;
  }

  /**
   * Returns the tree of the active property sheet page.
   */
  protected Tree getActivePropertySheetTree()
  {
    PropertySheetPage propertySheetPage = getActivePropertySheetPage();
    if (propertySheetPage != null)
    {
      Control control = propertySheetPage.getControl();
      if (control instanceof Tree)
      {
        Tree tree = (Tree)control;
        return tree;
      }
    }

    return null;
  }

  public boolean isEditable()
  {
    // Editing is always supported.
    // Replace is selectively disabled when a particular selection is not editable.
    return true;
  }

  public boolean canPerformFind()
  {
    // As long as there is a viewer is appropriate label and content providers, we can support find.
    StructuredViewer viewer = getViewer();
    return viewer != null && viewer.getLabelProvider() instanceof ILabelProvider && viewer.getContentProvider() instanceof IStructuredContentProvider;
  }

  public void initialize(IWorkbenchPart workbenchPart)
  {
    // This method is called by the find and replace action before it opens the find and replace dialog.
    // It has the opportunity to see the state before the find and replace dialog takes focus away.
    // In particular, it can see the selected text in an active cell editor in the properties view.
    StructuredViewer viewer = getViewer();
    Tree propertySheetTree = getActivePropertySheetTree();
    if (propertySheetTree != null)
    {
      // If there is an active property sheet with a tree, iterate over the selected tree items.
      for (TreeItem treeItem : propertySheetTree.getSelection())
      {
        // Determine if there is an EMF property descriptor associated with it.
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(treeItem);
        if (propertyDescriptor != null)
        {
          // If so, extract the object and look for it in the data.
          Object object = getObject(propertyDescriptor);
          for (FindAndReplaceTarget.Data data : new TextData(viewer))
          {
            if (data.object == object)
            {
              // Look for the feature in the data items.
              Object feature = getFeature(propertyDescriptor);
              for (Data.Item item : data.items)
              {
                Object itemFeature = item.getFeature();
                if (itemFeature == feature)
                {
                  // If there is a focus text control, it must be the cell editor of this property.
                  Control control = workbenchPart.getSite().getShell().getDisplay().getFocusControl();
                  if (control instanceof Text)
                  {
                    // Extract the selected text, if any...
                    Text text = (Text)control;
                    selectionText = text.getSelectionText();
                    if (selectionText.length() > 0)
                    {
                      // Use this item's selected text as our initial selection.
                      Point selection = text.getSelection();
                      setSelection(true, viewer, item, selection.x, Pattern.compile(Pattern.quote(selectionText)));

                      // Then we're done.
                      return;
                    }
                  }

                  // Use this overall item as our initial selection.
                  setSelection(true, viewer, item, 0, Pattern.compile(Pattern.quote(item.value)));
                  return;
                }
              }

              // Once we've passed the object of interest, there is nothing left to do.
              break;
            }
          }
        }
      }
    }
    else
    {
      // Otherwise, use the first item selected in the viewer as our initial selection.
      List<?> list = viewer.getStructuredSelection().toList();
      for (FindAndReplaceTarget.Data data : new TextData(viewer))
      {
        if (list.contains(data.object))
        {
          Data.Item item = data.items.get(0);
          setSelection(true, viewer, item, 0, Pattern.compile(Pattern.quote(item.value)));
          return;
        }
      }
    }
  }

  public void beginSession()
  {
    // When the session start, we add our search-type control.
    addSearchTypeControl();
  }

  protected void addSearchTypeControl()
  {
    // Look through all child shells...
    Shell workbenchShell = workbenchPart.getSite().getShell();
    Shell[] shells = workbenchShell.getShells();
    for (final Shell shell : shells)
    {
      // If this is a shell for the find and replace dialog...
      Object data = shell.getData();
      if (data instanceof Dialog && "org.eclipse.ui.texteditor.FindReplaceDialog".equals(data.getClass().getName()))
      {
        // Find the last checkbox in the dialog.
        Dialog dialog = (Dialog)data;
        Object checkBox = ReflectUtil.getValue("fIsRegExCheckBox", dialog);
        if (checkBox instanceof Button)
        {
          // It should have grid data.
          Button checkBoxButton = (Button)checkBox;
          Object layoutData = checkBoxButton.getLayoutData();
          if (layoutData instanceof GridData)
          {
            // Change it's span and alignment to make room for our additional control.
            final GridData checkBoxGridData = (GridData)layoutData;
            if (checkBoxGridData.horizontalSpan == 2)
            {
              checkBoxGridData.verticalAlignment = SWT.TOP;
              checkBoxGridData.horizontalSpan = 1;
            }

            // Keep state in a section of our dialog settings.
            final IDialogSettings dialogSettings = UIPlugin.INSTANCE.getDialogSettings("org.eclipse.ui.texteditor.FindReplaceDialog");

            // Create a search-type combo in the same group as the checkbox.
            final Composite group = checkBoxButton.getParent();
            final Combo combo = new Combo(group, SWT.READ_ONLY | SWT.DROP_DOWN);
            combo.setItems(SearchType.getLabels());
            GridData gridData = new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1);
            combo.setLayoutData(gridData);

            // The initial choice is remembered from the dialog settings.
            searchType = SearchType.getSearchType(dialogSettings.get("search-type"));
            combo.select(searchType.ordinal());

            // Listen for changes in the choice.
            combo.addSelectionListener(new SelectionAdapter()
            {
              @Override
              public void widgetSelected(SelectionEvent e)
              {
                // Not only remember the choice, but record it in the dialog settings.
                searchType = SearchType.values()[combo.getSelectionIndex()];
                dialogSettings.put("search-type", searchType.key());
              }
            });

            String oldLabel = null;
            Button selectedRangeButton = null;
            Object selectedRange = ReflectUtil.getValue("fSelectedRangeRadioButton", dialog);
            if (selectedRange instanceof Button)
            {
              selectedRangeButton = (Button)selectedRange;
              String label = selectedRangeButton.getText();
              if (label.endsWith("lines"))
              {
                oldLabel = label;
                selectedRangeButton.setText(label.substring(0, label.length() - 5) + " elements");
              }
            }

            // Relayout the shell.
            shell.layout(true, true);

            // If we've never checked that the default size of the dialog is big enough to fit our controls,
            // do it this time.
            if (!dialogSettings.getBoolean("resized"))
            {
              // Determine the bottom right corner location of the combo in absolute coordinates.
              Rectangle comboBounds = combo.getBounds();
              Point comboBottomRightLocation = group.toDisplay(comboBounds.x + comboBounds.width, comboBounds.y + comboBounds.height);

              // Determine the bottom right corner location of the containing group in absolute coordinates.
              Rectangle groupBounds = group.getBounds();
              Point groupBottomRightLocation = group.getParent().toDisplay(groupBounds.x + groupBounds.width, groupBounds.y + groupBounds.height);

              // Determine how much too small each dimension might be.
              // If there is less that 8 pixes of padding in either direction...
              int widthDelta = groupBottomRightLocation.x - comboBottomRightLocation.x;
              int heightDelta = groupBottomRightLocation.y - comboBottomRightLocation.y;
              if (widthDelta < 8 || heightDelta < 8)
              {
                // Increase the shell size so that the search-type combo fits nicely.
                Point shellSize = shell.getSize();

                if (widthDelta < 8)
                {
                  shellSize.x -= widthDelta - 8;
                }

                if (heightDelta < 8)
                {
                  shellSize.y -= heightDelta - 8;
                }

                shell.setSize(shellSize);
              }

              // Only do this once in this workspace.
              dialogSettings.put("resized", true);
            }

            // Setup the task that needs to be done to undo what we've done here.
            final String finalOldLabel = oldLabel;
            final Button finalSelectRangeButton = selectedRangeButton;
            sessionCleanup = new Runnable()
            {
              public void run()
              {
                // Restore the grid data and dispose our control.
                checkBoxGridData.horizontalSpan = 2;
                checkBoxGridData.verticalAlignment = SWT.CENTER;
                combo.dispose();

                // Clean up the label change that we did.
                if (finalOldLabel != null)
                {
                  finalSelectRangeButton.setText(finalOldLabel);
                }

                UIUtil.asyncExec(group, new Runnable()
                {
                  public void run()
                  {
                    // Defer the layout so that when the editor is switched to another EMF editor that supports find and replace,
                    // we don't see a lot of flickering.
                    shell.layout(true, true);
                  }
                });
              }
            };
          }
        }
      }
    }
  }

  public void endSession()
  {
    // When the session ends, we clean up or search-type control.
    if (sessionCleanup != null)
    {
      sessionCleanup.run();
    }

    // Also do the clean that's done when the find and replace dialog loses focus.
    setScope(null);
  }

  public Point getLineSelection()
  {
    // This method is used only to compute region to pass to setScope.
    // So instead of computing something useless we use this opportunity to remember the viewer's selection.
    StructuredViewer viewer = getViewer();
    selectionScope = viewer.getStructuredSelection().toList();
    return new Point(0, 0);
  }

  public IRegion getScope()
  {
    // This method is kind of useless, we remember our scope when the getLineSelection is called.
    return new Region(0, 0);
  }

  public void setScope(IRegion scope)
  {
    // When the properties view is activated, it's sometimes given focus.
    // In that case we restore the focus to the dialog, but we need to given the scope changes that are caused by the transient focus changes.
    if (suspendScopeChanges)
    {
      return;
    }

    StructuredViewer viewer = getViewer();
    if (scope == null)
    {
      // Remember the objects that need label updating.
      Object[] selectionScopeObjectsToUpdate = selectionScopeObjects == null ? null : selectionScopeObjects.toArray();
      Object selectionToUpdate = selectedItem != null && selectedItem.itemPropertyDescriptor == null ? selectedItem.data.object : null;

      // The scope is set to null when the dialog loses focus.
      // We should forget about most of our state at this point.
      selectionText = null;
      selectedItem = null;
      findReplaceable = false;
      selectionScopeObjects = null;

      propertiesCleanup();

      // Update update the selection scope objects.
      if (selectionScopeObjectsToUpdate != null)
      {
        viewer.update(selectionScopeObjectsToUpdate, null);
      }

      // Update the selection object.
      if (selectionToUpdate != null)
      {
        viewer.update(selectionToUpdate, null);
      }
    }
    else
    {
      // Record the objects in the selection scope.
      // These will be painted in a special way in the first to highlight them.
      selectionScopeObjects = new HashSet<Object>();

      int depth = -1;
      for (FindAndReplaceTarget.Data data : new TextData(viewer))
      {
        // If we hit the next object at the same depend, reset the depth.
        if (data.depth == depth)
        {
          depth = -1;
        }

        // If the object is directly in scope.
        if (selectionScope.contains(data.object))
        {
          // Remember the depth if we haven't remember one already.
          if (depth == -1)
          {
            depth = data.depth;
          }
        }

        // If the object is a selected object or nested under a selected object, include it.
        if (depth != -1)
        {
          selectionScopeObjects.add(data.object);
        }
      }

      // Hook up our decorating label provider for the current viewer.
      hookLabelProvider();

      // Eliminate the selection.
      viewer.setSelection(StructuredSelection.EMPTY);
    }
  }

  protected void propertiesCleanup()
  {
    // Clean up the stuff we did to the properties view.
    if (propertiesCleanup != null)
    {
      propertiesCleanup.run();
      propertiesCleanup = null;
    }
  }

  public void setScopeHighlightColor(Color color)
  {
    // This is never called by the find and replace dialog.
  }

  public String getSelectionText()
  {
    // This method is called for three different reasons.
    // 1 - For the initial text in the find field.
    // 2 - For enabling the replace button.
    // 3 - For recording a "selection" history; goodness knows what that's for though!
    //
    // If we're updating the button state, the selection text is used to enable/disable the replace button state.
    // We want it enabled only if we've selected an editable property.
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    if ("updateButtonState".equals(stackTrace[2].getMethodName()) && (selectedItem == null || selectedItem.itemPropertyDescriptor == null
        || !selectedItem.itemPropertyDescriptor.canSetProperty(selectedItem.data.object)))
    {
      return "";
    }

    // We use this only to initialize the text selection from a cell editor.
    // Once we've done a find, we clean this field.
    if (selectionText != null)
    {
      return selectionText;
    }

    // If there is a selected item, return its value.
    if (selectedItem != null)
    {
      return selectedItem.value;
    }

    // Otherwise we have no selection; we must return a non-null value.
    return "";
  }

  public Point getSelection()
  {
    if (pendingReplacements >= 0)
    {
      return new Point(pendingReplacements, 0);
    }

    // This method is super important for determining the point from which the next find processing will proceed.
    // If there is a selected item, we should proceed from the end of the current match.
    if (selectedItem != null)
    {
      Matcher matcher = selectedItemPattern.matcher(selectedItem.value);
      int size = 0;
      if (selectedItem.value.length() >= selectedItemStart && matcher.find(selectedItemStart))
      {
        size = matcher.group().length();
      }

      Point point = new Point(selectedItem.index + selectedItemStart, size);
      return point;
    }

    // If there is an active property sheet tree.
    StructuredViewer viewer = getViewer();
    Tree propertySheetTree = getActivePropertySheetTree();
    if (propertySheetTree != null)
    {
      // Look through the selection.
      for (final TreeItem treeItem : propertySheetTree.getSelection())
      {
        // If it has and EMF property descriptor
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(treeItem);
        if (propertyDescriptor != null)
        {
          // Determine it's wrapped object and look for it in the induced text data.
          Object object = getObject(propertyDescriptor);
          for (FindAndReplaceTarget.Data data : new TextData(viewer))
          {
            // If we find it...
            if (data.object == object)
            {
              // Determine which feature is the selected feature.
              Object feature = getFeature(propertyDescriptor);

              // Collection all features before the selected feature.
              List<Object> features = new ArrayList<Object>();
              for (final TreeItem otherTreeIem : propertySheetTree.getItems())
              {
                PropertyDescriptor otherPropertyDescriptor = getPropertyDescriptor(otherTreeIem);
                if (otherPropertyDescriptor != null)
                {
                  Object otherFeature = getFeature(otherPropertyDescriptor);
                  features.add(otherFeature);
                  if (otherFeature == feature)
                  {
                    break;
                  }
                }
              }

              // Consider all the items.
              Data.Item candidate = null;
              for (Data.Item item : data.items)
              {
                // If we find an exact match, return the information for it immediately, otherwise consider it a candidate.
                Object itemFeature = item.getFeature();
                if (itemFeature == feature)
                {
                  return new Point(item.index, 0);
                }
                else if (features.contains(itemFeature))
                {
                  candidate = item;
                }
              }

              // If there is a candidate, return the information for it.
              if (candidate != null)
              {
                return new Point(candidate.index, 0);
              }

              // If we find nothing, there's no point in looking anywhere else.
              break;
            }
          }
        }
      }
    }

    // Otherwise find the first item of an object in the selection.
    List<?> list = viewer.getStructuredSelection().toList();
    for (FindAndReplaceTarget.Data data : new TextData(viewer))
    {
      if (list.contains(data.object))
      {
        return new Point(data.items.get(0).index, 0);
      }
    }

    // Start at the beginning.
    return new Point(0, 0);
  }

  public void setSelection(int offset, int length)
  {
    // This method is always called right before setScope.
    // The information it provides is not useful and can be ignored.
  }

  public int findAndSelect(int offset, String findString, boolean searchForward, boolean caseSensitive, boolean wholeWord)
  {
    // This is never called, but we forward it sensibly anyway.
    return findAndSelect(offset, findString, searchForward, caseSensitive, wholeWord, false);
  }

  public int findAndSelect(int offset, String findString, boolean searchForward, boolean caseSensitive, boolean wholeWord, boolean regExSearch)
  {
    // Clear out the text used to populate the search text field once we've done a search.
    selectionText = null;

    // Compile the raw pattern early so it can throw an exception if it's not well formed.
    // The information in that exception is displayed to the user.
    if (regExSearch)
    {
      Pattern.compile(findString);
    }

    // A pattern will be constructed depending on the search parameters.
    String impliedPattern = findString;

    // If we're not doing a regular expression search, quote the pattern.
    if (!regExSearch)
    {
      impliedPattern = Pattern.quote(impliedPattern);
    }

    // If we want case-insensitive matching, encode that in the pattern.
    if (!caseSensitive)
    {
      impliedPattern = "(?i)" + impliedPattern;
    }

    // If we want whole word matching, add the word break delimiters to the pattern.
    if (wholeWord)
    {
      impliedPattern = "\\b" + impliedPattern + "\\b";
    }

    // This should always compile correctly.
    Pattern pattern = Pattern.compile(impliedPattern);

    // If there are pending replacements, don't bother searching.
    if (pendingReplacements >= 0)
    {
      selectedItemPattern = pattern;
      return --pendingReplacements;
    }

    StructuredViewer viewer = getViewer();

    // Iterate over the induced text, keeping track of a candidates for the case of backward searching.
    Data.Item candidate = null;
    int candidateStart = -1;
    LOOP: for (FindAndReplaceTarget.Data data : new TextData(viewer))
    {
      // If we have no restricted scope or the we and the object is in that scope...
      if (selectionScopeObjects == null || selectionScopeObjects.contains(data.object))
      {
        // Iterate over the items.
        for (Data.Item item : data.items)
        {
          // If we're searching forward and the item is at or above the offset or the end of the string is above the offset...
          // Otherwise if were's searching backward and the offset is -1, because we're wrapped, or the item is before the offset...
          if (searchForward ? item.index >= offset || item.index + item.value.length() > offset : offset == -1 || item.index < offset)
          {
            // If we've just done a replace, we make sure that the next find will find something that can be replaced, i.e., a modifiable attribute.
            // Otherwise we make sure that the item is included by the search type.
            if (findReplaceable ? !SearchType.MODIFIABLE_ATTRIBUTE.isIncluded(item) : searchType != null && !searchType.isIncluded(item))
            {
              continue;
            }

            // When searching forward, we need to make sure that we begin the pattern match where it skips the stuff before the current offset.
            int begin = 0;
            if (searchForward && item.index < offset)
            {
              begin = offset - item.index;
            }

            // Look for a match from the starting point.
            Matcher matcher = pattern.matcher(item.value);
            if (matcher.find(begin))
            {
              // Determine the offset of the match.
              int start = matcher.start();

              if (searchForward)
              {
                // If we're searching forward, we're so record and mark this selection point.
                setSelection(false, viewer, item, start, pattern);

                // Mark the fact that we've done a find but not a replace yet.
                findReplaceable = false;

                // Return the appropriate absolute index where we matched.
                return item.index + start;
              }
              // Otherwise, if we're searching backward all the way to the end, or the match is not past the target offset...
              else if (offset == -1 || item.index + start <= offset)
              {
                // This is definitely a candidate.
                candidate = item;
                candidateStart = start;

                // But keep searching for a better candidate later in the string.
                while (matcher.find())
                {
                  // Repeat the same logic.
                  start = matcher.start();
                  if (offset == -1 || item.index + start <= offset)
                  {
                    candidate = item;
                    candidateStart = start;
                  }
                }
              }
            }
          }
          // If we're searching backward and we've gone as far as we need to go...
          else if (!searchForward && item.index > offset)
          {
            // Break from the loop.
            break LOOP;
          }
        }
      }
    }

    // If there is a candidate (which is only possible if we're searching backwards...
    if (candidate != null)
    {
      // Record and mark this selection point.
      setSelection(false, viewer, candidate, candidateStart, pattern);

      // Mark the fact that we've done a find but not a replace yet.
      findReplaceable = false;

      // Return the appropriate absolute index where we matched.
      return candidate.index + candidateStart;
    }

    // There is no match.
    return -1;
  }

  /**
   * This records a match either initially or as a result of a find.
   */
  protected void setSelection(boolean preserve, StructuredViewer viewer, Data.Item item, final int start, Pattern pattern)
  {
    Object selectedObjectToUpdate = selectedItem != null && selectedItem.itemPropertyDescriptor == null ? selectedItem.data.object : null;

    // Remember the information about the item, pattern, and offset within the item of the match.
    selectedItem = item;
    selectedItemPattern = pattern;
    selectedItemStart = start;

    if (selectedObjectToUpdate != null)
    {
      viewer.update(selectedObjectToUpdate, null);
    }

    // There is no special tree item anymore.
    specialTreeItem = null;

    // If we haven't already done so, hook up the special label provider for providing selection feedback.
    hookLabelProvider();

    // Clean any previous stuff we did to decorate the properties view.
    propertiesCleanup();

    // In replace all mode, we don't want to provide any further feedback.
    if (replaceAllCommand == null)
    {
      // Select the item in the viewer, unless we're preserving the selection, i.e., during the initial feedback.
      StructuredSelection selection = new StructuredSelection(new TreePath(item.data.getPath()));
      if (!preserve)
      {
        viewer.setSelection(selection, true);
      }

      // If there is an active property page, update it's selection immediately.
      PropertySheetPage activePropertySheetPage = getActivePropertySheetPage();
      if (activePropertySheetPage != null)
      {
        activePropertySheetPage.selectionChanged(workbenchPart, selection);
      }

      // Make the properties view visible, creating it if necessary.
      IWorkbenchPartSite site = workbenchPart.getSite();
      IWorkbenchPage page = site.getPage();
      IViewPart viewPart = page.findView("org.eclipse.ui.views.PropertySheet");

      // Sometimes showing the properties view gives it focus, e.g., when the editor is maximized.
      Display display = site.getShell().getDisplay();
      Control oldFocusControl = display.getFocusControl();
      try
      {
        // Ignore scope changes while showing the properties view.
        suspendScopeChanges = true;
        if (item.itemPropertyDescriptor != null)
        {
          viewPart = page.showView("org.eclipse.ui.views.PropertySheet", null, IWorkbenchPage.VIEW_VISIBLE);
          if (viewPart == null)
          {
            viewPart = page.showView("org.eclipse.ui.views.PropertySheet", null, IWorkbenchPage.VIEW_CREATE);
          }
        }

        // Restore the focus.
        Control newFocusControl = display.getFocusControl();
        if (oldFocusControl != newFocusControl)
        {
          oldFocusControl.setFocus();
        }
      }
      catch (PartInitException ex)
      {
        UIPlugin.INSTANCE.log(ex);
      }
      finally
      {
        suspendScopeChanges = false;
      }

      // If it is a property sheet, as expected...
      if (viewPart instanceof PropertySheet)
      {
        // And the current page is a property sheet page as expected...
        final PropertySheet propertySheet = (PropertySheet)viewPart;
        IPage currentPage = propertySheet.getCurrentPage();
        if (currentPage instanceof PropertySheetPage)
        {
          // And the control is a tree...
          Control control = currentPage.getControl();
          if (control instanceof Tree)
          {
            final Tree tree = (Tree)control;

            if (item.itemPropertyDescriptor != null)
            {
              // Remember the filter action that we needed to check it to be able to show an advanced property.
              Action filterAction = null;

              // If the property has filter flags...
              String[] filterFlags = item.itemPropertyDescriptor.getFilterFlags(item.data.object);
              if (filterFlags != null)
              {
                for (String filterFlag : filterFlags)
                {
                  // If the filter is one for expert property...
                  if ("org.eclipse.ui.views.properties.expert".equals(filterFlag))
                  {
                    Action action = ReflectUtil.getValue(FILTER_ACTION_FIELD, currentPage);
                    if (!action.isChecked())
                    {
                      // Run the action to show advanced properties, and remember that.
                      action.setChecked(true);
                      action.run();
                      filterAction = action;
                    }
                  }
                }
              }

              // Walk the tree items.
              for (final TreeItem treeItem : tree.getItems())
              {
                // If there is an EMF property descriptor with a feature for the selected item...
                PropertyDescriptor propertyDescriptor = getPropertyDescriptor(treeItem);
                if (propertyDescriptor != null && propertyDescriptor.getFeature() == item.getFeature())
                {
                  // Consider the label shown in the tree verses the value of the selected item...
                  String treeItemText = treeItem.getText(1);
                  String itemValue = item.value;

                  // We might need to replace the tree item's text with a special representation...
                  specialStart = -1;

                  // If they are're identical....
                  if (!treeItemText.equals(itemValue))
                  {
                    // Find the match, which really must be there, do we can determine the length of the match.
                    Matcher matcher = pattern.matcher(itemValue);
                    if (matcher.find(start))
                    {
                      // Remember this special item, because we'll want to update it after we do a replace to show the replaced text.
                      specialTreeItem = treeItem;

                      // If the end of the match is after the end of the tree item's text, or the strings up until the end of the match are not
                      // identical...
                      int end = matcher.end();
                      if (treeItemText.length() < end || !treeItemText.substring(0, end).equals(itemValue.substring(0, end)))
                      {
                        // Consider the starting point of the match, and work our way backward for 20 characters or until the preceding control
                        // character.
                        int begin = matcher.start();
                        specialStart = 2;
                        while (begin >= 0 && specialStart < 20 && !Character.isISOControl(itemValue.charAt(begin)))
                        {
                          ++specialStart;
                          --begin;
                        }

                        // Work our way forward until the end of the string or until we hit a control character.
                        int itemValueLength = itemValue.length();
                        while (end < itemValueLength && !Character.isISOControl(itemValue.charAt(end)))
                        {
                          ++end;
                        }

                        // Create a special string with ellipses at both ends.
                        String specialText = "..." + itemValue.substring(begin + 1, end) + "...";

                        // But that back into the item.
                        treeItem.setText(1, specialText);

                        // Get the tree to redraw itself.
                        tree.redraw();
                      }
                    }
                  }

                  // Create a paint listener to select the match.
                  final Listener paintItemListener = new Listener()
                  {
                    private void paintItem(Event event, TreeItem item, int matchStart)
                    {
                      String text = item.getText(1);
                      Matcher matcher = selectedItemPattern.matcher(text);
                      if (matchStart < text.length() && matcher.find(matchStart))
                      {
                        // Compute the offset of the start of the matching, relative to the start of the text.
                        int start = matcher.start();
                        int x = event.gc.textExtent(text.substring(0, start)).x + item.getTextBounds(1).x - treeItem.getBounds(1).x;

                        // Compute the offset at the end of the match, taking into account the width of the matching text.
                        int width = event.gc.textExtent(matcher.group()).x;
                        event.gc.drawRectangle(event.x + x + 1, event.y, width + 1, event.height - 1);
                      }
                      else if (text.endsWith("..."))
                      {
                        int x = event.gc.textExtent(text.substring(0, text.length() - 3)).x + treeItem.getTextBounds(1).x - treeItem.getBounds(1).x;
                        int width = event.gc.textExtent("...").x;
                        event.gc.drawRectangle(event.x + x + 1, event.y, width + 1, event.height - 1);
                      }
                    }

                    public void handleEvent(Event event)
                    {
                      // If we're painting or special item...
                      TreeItem item = (TreeItem)event.item;
                      if (item == treeItem && event.index == 1)
                      {
                        paintItem(event, item, specialStart == -1 ? start : specialStart);
                      }
                    }
                  };

                  // Add the listener.
                  tree.addListener(SWT.PaintItem, paintItemListener);

                  // Set up the runnable to clean up what we've done here.
                  final PropertySheetPage propertySheetPage = (PropertySheetPage)currentPage;
                  final Action finalFilterAction = filterAction;
                  propertiesCleanup = new Runnable()
                  {
                    public void run()
                    {
                      // Remove the listener.
                      tree.removeListener(SWT.PaintItem, paintItemListener);

                      // If there is a filter action we toggled...
                      if (finalFilterAction != null)
                      {
                        // Toggle it back, which will refresh the view.
                        finalFilterAction.setChecked(false);
                        finalFilterAction.run();
                      }
                      else
                      {
                        // Otherwise refresh the view.
                        propertySheetPage.refresh();
                      }
                    }
                  };

                  // Select the item, and force a repaint.
                  tree.setSelection(treeItem);
                  tree.redraw();

                  // We're done.
                  return;
                }
              }
            }

            // If we didn't find it at all, clear out the selection.
            tree.setSelection(new TreeItem[0]);
          }
        }
      }
    }
  }

  /**
   * This sets up a special label provider in the viewer to be able to highlight the scope and show the selected match.
   */
  protected void hookLabelProvider()
  {
    final StructuredViewer viewer = getViewer();

    // We use this special class so we can detect if the label provider is already hooked up.
    class DecoratingLabelProvider extends DelegatingStyledCellLabelProvider.FontAndColorProvider implements IStyledLabelProvider
    {
      public DecoratingLabelProvider(IStyledLabelProvider styledLabelProvider)
      {
        super(styledLabelProvider);
      }

      @Override
      public StyledString getStyledText(Object element)
      {
        return super.getStyledText(element);
      }
    }

    // If the label provider is already hooked up...
    final ILabelProvider labelProvider = (ILabelProvider)viewer.getLabelProvider();
    if (labelProvider instanceof DecoratingLabelProvider)
    {
      // Update the selection scope objects.
      if (selectionScopeObjects != null)
      {
        viewer.update(selectionScopeObjects.toArray(), null);
      }

      // Update the selected object if it's for a label.
      if (selectedItem != null && selectedItem.itemPropertyDescriptor == null)
      {
        viewer.update(selectedItem.data.object, null);
      }
    }
    else
    {
      // Create a styled label provider that can decorate the text.
      IStyledLabelProvider styledProvider = new DecoratingColumLabelProvider.StyledLabelProvider(labelProvider, new IStyledLabelDecorator()
      {
        // Use the color from the theme that the editor uses to highlight the scope.
        final Color color = workbenchPart.getSite().getWorkbenchWindow().getWorkbench().getThemeManager().getCurrentTheme().getColorRegistry()
            .get("org.eclipse.ui.editors.findScope");

        final Styler scopeStyler = new Styler()
        {
          @Override
          public void applyStyles(TextStyle textStyle)
          {
            textStyle.background = color;
          }
        };

        public void removeListener(ILabelProviderListener listener)
        {
          labelProvider.removeListener(listener);
        }

        public boolean isLabelProperty(Object element, String property)
        {
          return labelProvider.isLabelProperty(element, property);
        }

        public void dispose()
        {
          labelProvider.dispose();
        }

        public void addListener(ILabelProviderListener listener)
        {
          labelProvider.addListener(listener);
        }

        public String decorateText(String text, Object element)
        {
          if (labelProvider instanceof ILabelDecorator)
          {
            ILabelDecorator labelDecorator = (ILabelDecorator)labelProvider;
            return labelDecorator.decorateText(text, element);
          }

          return text;
        }

        public Image decorateImage(Image image, Object element)
        {
          if (labelProvider instanceof ILabelDecorator)
          {
            ILabelDecorator labelDecorator = (ILabelDecorator)labelProvider;
            return labelDecorator.decorateImage(image, element);
          }

          return image;
        }

        public StyledString decorateStyledText(StyledString styledString, Object element)
        {
          if (labelProvider instanceof IStyledLabelDecorator)
          {
            IStyledLabelDecorator styledLabelDecorator = (IStyledLabelDecorator)labelProvider;
            styledString = styledLabelDecorator.decorateStyledText(styledString, element);
          }

          // If we have a selected item, it's the item for the label, and this element is that selected element's object...
          if (selectedItem != null && selectedItem.itemPropertyDescriptor == null && element == selectedItem.data.object)
          {
            // Convert the styled string to just a string.
            String string = styledString.getString();

            // Find the pattern match within that string.
            Matcher matcher = selectedItemPattern.matcher(string);
            if (matcher.find(selectedItemStart))
            {
              // Create a new styles string.
              StyledString result = new StyledString();

              // Recompose the string with the match styled to show a selection bod.
              String group = matcher.group();
              int start = matcher.start();
              int end = matcher.end();
              result.append(string.substring(0, start));
              result.append(group, MATCH_STYLER);
              result.append(string.substring(end));
              return result;
            }
          }

          // If we have scope objects and the element is one of those...
          if (selectionScopeObjects != null && selectionScopeObjects.contains(element))
          {
            // Mark the entire string with the scope styling.
            StyledString result = new StyledString();
            result.append(styledString.getString(), scopeStyler);
            return result;
          }

          // Otherwise just pass through the string.
          return styledString;
        }
      })
      {
        {
          if (labelProvider instanceof CellLabelProvider)
          {
            cellLabelProvider = (CellLabelProvider)labelProvider;
          }
        }
      };

      // Hook up the label provider to be the one used by the view.
      ILabelProvider delegatingLabelProvider = new DecoratingLabelProvider(styledProvider);
      viewer.setLabelProvider(delegatingLabelProvider);
    }
  }

  public void replaceSelection(String text)
  {
    // This is never called, but delegate it appropriately nevertheless.
    replaceSelection(text, false);
  }

  public void replaceSelection(String text, boolean regExReplace)
  {
    // If we're in replace all mode.
    if (replaceAllCommand != null)
    {
      // And this is the first call to replace selection...
      if (pendingReplacements == Integer.MAX_VALUE - 1)
      {
        // Determine all the replacements that are applicable.
        pendingReplacements = replaceSelectionAll(text, regExReplace) - 1;
      }

      // Both find and replace will ignore the next pendingReplacements number of calls.
      return;
    }

    // If the selected item can't be modified...
    if (!SearchType.MODIFIABLE_ATTRIBUTE.isIncluded(selectedItem))
    {
      return;
    }

    // If the pattern doesn't match...
    Matcher matcher = selectedItemPattern.matcher(selectedItem.value);
    if (!matcher.find(selectedItemStart))
    {
      return;
    }

    // Build up the replacement.
    StringBuffer result = new StringBuffer();

    // Remember the start of the match...
    int start = matcher.start();

    // Escape the $ if we're not doing a regular expression replacement.
    String replacement = regExReplace ? text : text.replace("$", "\\$");

    // Append the replacement
    matcher.appendReplacement(result, replacement);

    // Remember exactly what we've replaced the pattern with.
    String actualReplacement = result.substring(start);

    // Complete the composition.
    matcher.appendTail(result);

    // We must have an editing domain and the selected item's feature must be a modifiable attribute.
    EditingDomain domain = ((IEditingDomainProvider)workbenchPart).getEditingDomain();
    EAttribute eAttribute = (EAttribute)selectedItem.getFeature();

    // Try to convert the value modified string to a value; this can fail.
    Object value;
    try
    {
      value = EcoreUtil.createFromString(eAttribute.getEAttributeType(), result.toString());
    }
    catch (RuntimeException exception)
    {
      return;
    }

    // If there is a special item in the properties view, we want to update the text to show the replacement.
    String replacementSpecialText = null;
    if (specialTreeItem != null && !specialTreeItem.isDisposed())
    {
      String specialText = specialTreeItem.getText(1);
      Matcher specialMatcher = selectedItemPattern.matcher(specialText);
      if (specialMatcher.find(specialStart))
      {
        StringBuffer specialResult = new StringBuffer();
        specialMatcher.appendReplacement(specialResult, replacement);
        specialMatcher.appendTail(specialResult);
        replacementSpecialText = specialResult.toString();
      }
    }

    // Remember the replacement as the pattern so that views showing the matching selection continue to show the matching replacement.
    selectedItemPattern = Pattern.compile(Pattern.quote(actualReplacement));

    Command setCommand;
    if (eAttribute.isMany())
    {
      Object propertyValue = selectedItem.itemPropertyDescriptor.getPropertyValue(selectedItem.data.object);
      if (propertyValue instanceof IItemPropertySource)
      {
        propertyValue = ((IItemPropertySource)propertyValue).getEditableValue(selectedItem.data.object);
      }

      // Compute the new overall value for the list.
      List<Object> values = new ArrayList<Object>((List<?>)propertyValue);
      values.set(selectedItem.itemIndex, value);

      // Create a command to set the overall list value.
      setCommand = SetCommand.create(domain, selectedItem.data.object, eAttribute, values);
    }
    else
    {
      // Create a command to set the value.
      setCommand = SetCommand.create(domain, selectedItem.data.object, eAttribute, value);
    }

    // If this is not in replace all mode, we need to be careful that command execution will cause notification that will try to select the affected
    // objects.
    // This messes up or attempts to track the selection progress in the case of replace/find.
    CompoundCommand wrapper = new CompoundCommand(CompoundCommand.MERGE_COMMAND_ALL)
    {
      boolean isFirst = true;

      @Override
      public Collection<?> getAffectedObjects()
      {
        // The first time this is called, it returns the empty list, so that no selection takes place.
        if (isFirst)
        {
          isFirst = false;
          return Collections.emptyList();
        }

        return super.getAffectedObjects();
      }
    };

    // Put the set command in the wrapper and execute the wrapper.
    wrapper.append(setCommand);
    domain.getCommandStack().execute(wrapper);

    // We need to wait for async executed updates to finish.
    Display display = getViewer().getControl().getShell().getDisplay();
    final boolean[] run = new boolean[] { true };
    display.asyncExec(new Runnable()
    {
      public void run()
      {
        run[0] = false;
      }
    });

    // Process the event queue until our runnable has run, at which point other runnables queued before ours will also have been completed.
    while (run[0] && display.readAndDispatch())
    {
      display.sleep();
    }

    // If we have a special item.
    if (specialTreeItem != null)
    {
      // And it's not disposed yet.
      if (!specialTreeItem.isDisposed() && replacementSpecialText != null)
      {
        // Update it to the replacement text, and force it to repaint to highlight the replacement.
        specialTreeItem.setText(1, replacementSpecialText);
        specialTreeItem.getParent().redraw();
      }

      // We can forget about it now.
      specialTreeItem = null;
    }

    // It's important to relocate our selected item, because the replacement could have updated text that appears earlier in our induced text view.
    // This would mess up the current selected item's index.
    for (FindAndReplaceTarget.Data data : new TextData(getViewer()))
    {
      if (data.object == selectedItem.data.object)
      {
        for (Data.Item item : data.items)
        {
          if (selectedItem.itemPropertyDescriptor == item.itemPropertyDescriptor && selectedItem.itemIndex == item.itemIndex)
          {
            selectedItem = item;
          }
        }
      }
    }

    // We want the next find (especially for find/replace) to find only modifiable selections.
    findReplaceable = true;
  }

  protected int replaceSelectionAll(String text, boolean regExReplace)
  {
    // Escape the $ if we're not doing a regular expression replacement.
    String replacement = regExReplace ? text : text.replace("$", "\\$");

    // We must have an editing domain.
    EditingDomain domain = ((IEditingDomainProvider)workbenchPart).getEditingDomain();

    int total = 0;
    for (Data data : new TextData(getViewer()))
    {
      // We must defer creation of the set command for a multi-valued feature until after all its items are processed.
      EAttribute currentListAttribute = null;
      List<Object> currentListValue = null;

      for (Data.Item item : data.items)
      {
        // If the selected item can't be modified...
        if (!SearchType.MODIFIABLE_ATTRIBUTE.isIncluded(item))
        {
          continue;
        }

        // If the pattern doesn't match...
        Matcher matcher = selectedItemPattern.matcher(item.value);
        if (!matcher.find())
        {
          continue;
        }

        // Build up the replacement.
        StringBuffer result = new StringBuffer();

        // Keep track of the number of matches.
        int count = 0;
        do
        {
          // Append the replacement
          matcher.appendReplacement(result, replacement);
          ++count;
        } while (matcher.find());

        // Complete the composition.
        matcher.appendTail(result);

        // The feature must be an attribute.
        EAttribute eAttribute = (EAttribute)item.getFeature();

        // If we've deferred a multi-valued feature change, and this is a different feature
        if (currentListAttribute != null && eAttribute != currentListAttribute)
        {
          Command setCommand = SetCommand.create(domain, data.object, currentListAttribute, currentListValue);
          replaceAllCommand.append(setCommand);

          currentListAttribute = null;
          currentListValue = null;
        }

        // Try to convert the modified string to a value; this can fail.
        Object value;
        try
        {
          value = EcoreUtil.createFromString(eAttribute.getEAttributeType(), result.toString());
        }
        catch (RuntimeException exception)
        {
          continue;
        }

        total += count;

        if (eAttribute.isMany())
        {
          if (currentListValue == null)
          {
            currentListAttribute = eAttribute;

            Object propertyValue = item.itemPropertyDescriptor.getPropertyValue(data.object);
            if (propertyValue instanceof IItemPropertySource)
            {
              propertyValue = ((IItemPropertySource)propertyValue).getEditableValue(data.object);
            }

            // Compute the new overall value for the list.
            currentListValue = new ArrayList<Object>((List<?>)propertyValue);
          }

          currentListValue.set(item.itemIndex, value);
        }
        else
        {
          // Create a command to set the value.
          Command setCommand = SetCommand.create(domain, data.object, eAttribute, value);
          replaceAllCommand.append(setCommand);
        }
      }

      // If we've deferred a multi-valued feature change and it's not been processed in the above loop.
      if (currentListAttribute != null)
      {
        Command setCommand = SetCommand.create(domain, data.object, currentListAttribute, currentListValue);
        replaceAllCommand.append(setCommand);
      }
    }

    return total;
  }

  public void setReplaceAllMode(boolean replaceAll)
  {
    if (replaceAll)
    {
      // We want the next find to find only modifiable selections.
      findReplaceable = true;

      // We want all the commands to be batched into a single undoable command.
      replaceAllCommand = new CompoundCommand(CompoundCommand.MERGE_COMMAND_ALL, "Replace All")
      {
      };
      pendingReplacements = Integer.MAX_VALUE;
    }
    else
    {
      try
      {
        // We must have an editing domain.
        EditingDomain domain = ((IEditingDomainProvider)workbenchPart).getEditingDomain();
        domain.getCommandStack().execute(replaceAllCommand);
      }
      finally
      {
        // We're done now.
        findReplaceable = false;
        replaceAllCommand = null;
        pendingReplacements = -1;
      }
    }
  }

  public static Object getAdapter(Class<?> adapter, IWorkbenchPart workbenchPart)
  {
    if (adapter == IFindReplaceTarget.class)
    {
      synchronized (FindAndReplaceTarget.FIND_AND_REPLACE_TARGETS)
      {
        FindAndReplaceTarget findAndReplaceTarget = FindAndReplaceTarget.FIND_AND_REPLACE_TARGETS.get(workbenchPart);
        if (findAndReplaceTarget == null)
        {
          findAndReplaceTarget = new FindAndReplaceTarget(workbenchPart);
          FindAndReplaceTarget.FIND_AND_REPLACE_TARGETS.put(workbenchPart, findAndReplaceTarget);
        }

        return findAndReplaceTarget;

      }
    }

    return null;
  }

  /**
   * Exacts the textual representation of the attribute-based property descriptor.
   * Returns <code>null</code> if the property is not attribute-based, or if the value is not representable as text.
   */
  protected static List<String> getText(IItemPropertyDescriptor itemPropertyDescriptor, Object object)
  {
    // If the feature is an attribute...
    Object feature = itemPropertyDescriptor.getFeature(object);
    if (feature instanceof EAttribute)
    {
      // If the attribute's type is serializeable...
      EAttribute eAttribute = (EAttribute)feature;
      EDataType eDataType = eAttribute.getEAttributeType();
      if (eDataType.isSerializable())
      {
        // Extract the property value and unwrap it if necessary.
        Object value = itemPropertyDescriptor.getPropertyValue(object);
        if (value instanceof IItemPropertySource)
        {
          value = ((IItemPropertySource)value).getEditableValue(object);
        }

        // If there is a value.
        if (value != null)
        {
          // Always create a list.
          List<String> result = new ArrayList<String>();
          if (eAttribute.isMany())
          {
            // Add the textual representation of each value.
            for (Object item : (List<?>)value)
            {
              result.add(EcoreUtil.convertToString(eDataType, item));
            }
          }
          else
          {
            // Ad the textual representation of the one value.
            result.add(EcoreUtil.convertToString(eDataType, value));
          }

          return result;
        }
      }
    }

    return null;
  }

  /**
   * Returns the EMF property descriptor, if any, of the tree item.
   */
  protected static PropertyDescriptor getPropertyDescriptor(TreeItem treeItem)
  {
    Object data = treeItem.getData();
    if (data instanceof PropertySheetEntry)
    {
      PropertySheetEntry propertySheetEntry = (PropertySheetEntry)data;
      Object descriptor = ReflectUtil.invokeMethod(GET_DESCRIPTOR_METHOD, propertySheetEntry);
      if (descriptor instanceof PropertyDescriptor)
      {
        PropertyDescriptor propertyDescriptor = (PropertyDescriptor)descriptor;
        return propertyDescriptor;
      }
    }

    return null;
  }

  /**
   * Returns the underlying object of the EMF property descriptor.
   */
  protected static Object getObject(PropertyDescriptor propertyDescriptor)
  {
    Object object = ReflectUtil.getValue(OBJECT_FIELD, propertyDescriptor);
    return object;
  }

  /**
   * Returns the feature of item property descriptor of the EMF property descriptor.
   */
  protected static Object getFeature(PropertyDescriptor propertyDescriptor)
  {
    Object object = getObject(propertyDescriptor);
    IItemPropertyDescriptor itemPropertyDescriptor = ReflectUtil.getValue(ITEM_PROPERTY_DESCRIPTOR_FIELD, propertyDescriptor);
    return itemPropertyDescriptor.getFeature(object);
  }

  /**
   * This represents an element associated with each object in a tree.
   * @see TextData
   *
   * @author Ed Merks
   */
  public static class Data
  {
    /**
     * The object in the tree.
     */
    public Object object;

    /**
     * The depth of the object in the tree.
     */
    public int depth;

    /**
     * The parent data in the tree.
     */
    public Data parent;

    /**
     * The items associated with the object.
     */
    public List<Data.Item> items;

    public Data(int depth, Object object, List<Data.Item> items)
    {
      this.depth = depth;
      this.object = object;
      this.items = items;
    }

    public Object[] getPath()
    {
      List<Object> path = new ArrayList<Object>();
      for (Data data = this; data != null; data = data.parent)
      {
        path.add(0, data.object);
      }

      return path.toArray();
    }

    /**
     * This represents an item associated with each data item.
     *
     * @author Ed Merks
     */
    public static class Item
    {
      /**
       * The containing data of this item.
       */
      public FindAndReplaceTarget.Data data;

      /**
       * The index of this item in the overall {@link TextData induced} textual representation.
       */
      public int index;

      /**
       * The property descriptor associated with this item.
       * The <code>null</value> represents the label value of the data object.
       */
      public IItemPropertyDescriptor itemPropertyDescriptor;

      /**
       * Each value in a multi-valued feature is represented as an item.
       * This is the index of that item in its list.
       */
      public int itemIndex;

      /**
       * The textual value of the item.
       */
      public String value;

      public Item(FindAndReplaceTarget.Data data, int index, IItemPropertyDescriptor itemPropertyDescriptor, int itemIndex, String value)
      {
        this.data = data;
        this.index = index;
        this.itemPropertyDescriptor = itemPropertyDescriptor;
        this.itemIndex = itemIndex;
        this.value = value;
      }

      public Object getFeature()
      {
        if (itemPropertyDescriptor != null)
        {
          return itemPropertyDescriptor.getFeature(data.object);
        }

        return null;
      }
    }
  }

  /**
   * This supports iterating over an induced textual representation of a structured viewer's structure.
   *
   * @author Ed Merks
   */
  public static class TextData implements Iterable<FindAndReplaceTarget.Data>
  {
    private StructuredViewer viewer;

    private ILabelProvider labelProvider;

    private IPropertySourceProvider propertySourceProvider;

    public TextData(StructuredViewer viewer)
    {
      this.viewer = viewer;
      labelProvider = (ILabelProvider)viewer.getLabelProvider();
      IContentProvider contentProvider = viewer.getContentProvider();
      if (contentProvider instanceof IPropertySourceProvider)
      {
        propertySourceProvider = (IPropertySourceProvider)contentProvider;
      }
    }

    public Iterator<FindAndReplaceTarget.Data> iterator()
    {
      final StructuredViewerTreeIterator structuredViewerTreeIterator;
      IContentProvider contentProvider = viewer.getContentProvider();
      if (contentProvider instanceof StructuredViewerTreeIterator.Provider)
      {
        structuredViewerTreeIterator = ((StructuredViewerTreeIterator.Provider)contentProvider).create();
      }
      else
      {
        structuredViewerTreeIterator = new StructuredViewerTreeIterator(viewer);
      }

      // This is an iterator that delegates to an iterator that walks the structure of the viewer.
      return new Iterator<FindAndReplaceTarget.Data>()
      {
        private List<Data> parents = new ArrayList<Data>();

        // This keeps track of the textual index as we iterate.
        private int index;

        public boolean hasNext()
        {
          return structuredViewerTreeIterator.hasNext();
        }

        public FindAndReplaceTarget.Data next()
        {
          // Keep track of the depth before calling next.
          int depth = structuredViewerTreeIterator.size();
          Object object = structuredViewerTreeIterator.next();

          // Create a list of items for this object and use that to create a new data representation.
          List<Data.Item> items = new ArrayList<Data.Item>();
          FindAndReplaceTarget.Data data = new Data(depth - 1, object, items);

          if (parents.size() < depth)
          {
            parents.add(data);
          }
          else
          {
            parents.set(depth - 1, data);
          }

          if (depth > 1)
          {
            data.parent = parents.get(depth - 2);
          }

          // Add an item for the label.
          String label = labelProvider.getText(object);
          items.add(new Data.Item(data, index, null, 0, label));
          index += label.length();

          // If we have a source provider...
          if (propertySourceProvider != null)
          {
            // And we have an EMF property source for the object...
            IPropertySource propertySource = propertySourceProvider.getPropertySource(object);
            if (propertySource instanceof PropertySource)
            {
              // Extract the EMF property source so we can iterate directly over the EMF property descriptors.
              PropertySource emfPropertySource = (PropertySource)propertySource;
              IItemPropertySource itemPropertySource = ReflectUtil.getValue(ITEM_PROPERTY_SOURCE_FIELD, emfPropertySource);
              for (IItemPropertyDescriptor itemPropertyDescriptor : itemPropertySource.getPropertyDescriptors(object))
              {
                // Extract the textual values of the property, if there are any.
                List<String> text = getText(itemPropertyDescriptor, object);
                if (text != null)
                {
                  // Create an item for each value.
                  for (int i = 0, size = text.size(); i < size; ++i)
                  {
                    String value = text.get(i);
                    items.add(new Data.Item(data, index, itemPropertyDescriptor, i, value));
                    index += value.length();
                  }
                }
              }
            }
          }

          return data;
        }

        public void remove()
        {
          throw new UnsupportedOperationException("remove");
        }
      };
    }
  }

  /**
   * This enumerates the types of searches that are possible for an EMF-based structured editor.
   * @author Ed Merks
   */
  private enum SearchType
  {
    LABEL_AND_ATTRIBUTE()
    {
      @Override
      public boolean isIncluded(Data.Item item)
      {
        return item != null;
      }

      @Override
      public String key()
      {
        return "label+attribute";
      }

      @Override
      public String label()
      {
        return "Labels and attributes";
      }
    },
    LABEL()
    {
      @Override
      public boolean isIncluded(Data.Item item)
      {
        return item != null && item.itemPropertyDescriptor == null;
      }

      @Override
      public String key()
      {
        return "label";
      }

      @Override
      public String label()
      {
        return "Labels";
      }
    },
    ATTRIBUTE()
    {
      @Override
      public boolean isIncluded(Data.Item item)
      {
        return item != null && item.itemPropertyDescriptor != null;
      }

      @Override
      public String key()
      {
        return "attribute";
      }

      @Override
      public String label()
      {
        return "Attributes";
      }
    },
    MODIFIABLE_ATTRIBUTE
    {
      @Override
      public boolean isIncluded(Data.Item item)
      {
        return item != null && item.itemPropertyDescriptor != null && item.itemPropertyDescriptor.canSetProperty(item.data.object);
      }

      @Override
      public String key()
      {
        return "modifiable-attribute";
      }

      @Override
      public String label()
      {
        return "Modifiable attributes";
      }
    };

    public abstract boolean isIncluded(Data.Item item);

    public abstract String key();

    public abstract String label();

    public static FindAndReplaceTarget.SearchType getSearchType(String key)
    {
      for (FindAndReplaceTarget.SearchType searchType : SearchType.values())
      {
        if (searchType.key().equals(key))
        {
          return searchType;
        }
      }

      return LABEL_AND_ATTRIBUTE;
    }

    public static String[] getLabels()
    {
      FindAndReplaceTarget.SearchType[] values = SearchType.values();
      String[] labels = new String[values.length];
      for (int i = 0; i < values.length; ++i)
      {
        labels[i] = values[i].label();
      }

      return labels;
    }
  }
}
