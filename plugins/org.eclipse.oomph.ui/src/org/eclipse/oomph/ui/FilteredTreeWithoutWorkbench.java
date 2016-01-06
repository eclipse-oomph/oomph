/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.progress.WorkbenchJob;

/**
 * This subclass is useful because the refresh job won't schedule if there is no workbench, which is the case in the installer wizard.
 * This supports an optional expansion filter that can be used to control how the tree is expanded after a match.
 * This also supports controlling the number of items that will be expanded after a match.
 *
 * @author Ed Merks
 */
public class FilteredTreeWithoutWorkbench extends FilteredTree
{
  private final Object refreshJobFamily = new Object();

  private final ExpansionFilter expansionFilter;

  private int expansionCount;

  public FilteredTreeWithoutWorkbench(Composite parent, int style)
  {
    super(parent, style, new PatternFilter(), true);
    expansionFilter = null;
  }

  public FilteredTreeWithoutWorkbench(Composite parent, int style, PatternFilter patternFilter, ExpansionFilter expansionFilter)
  {
    super(parent, style, patternFilter, true);
    this.expansionFilter = expansionFilter;
  }

  public void setExpansionCount(int expansionCount)
  {
    this.expansionCount = expansionCount;
  }

  public Object getRefreshJobFamily()
  {
    return refreshJobFamily;
  }

  @Override
  protected WorkbenchJob doCreateRefreshJob()
  {
    return new WorkbenchJob("Refresh Filter")
    {
      @Override
      public IStatus runInUIThread(IProgressMonitor monitor)
      {
        if (treeViewer.getControl().isDisposed())
        {
          return Status.CANCEL_STATUS;
        }

        PatternFilter patternFilter = getPatternFilter();

        String text = getFilterString();
        if (StringUtil.isEmpty(text))
        {
          patternFilter.setPattern(null);
          treeViewer.refresh(true);
          return Status.OK_STATUS;
        }

        boolean initial = initialText != null && initialText.equals(text);
        if (initial)
        {
          patternFilter.setPattern(null);
        }
        else
        {
          patternFilter.setPattern(text);
        }

        Control redrawFalseControl = treeComposite != null ? treeComposite : treeViewer.getControl();

        try
        {
          redrawFalseControl.setRedraw(false);
          TreeItem[] is = treeViewer.getTree().getItems();
          for (int i = 0; i < is.length; i++)
          {
            TreeItem item = is[i];
            if (item.getExpanded())
            {
              treeViewer.setExpandedState(item.getData(), false);
            }
          }

          treeViewer.refresh(true);

          if (text.length() > 0 && !initial)
          {
            TreeItem[] items = getViewer().getTree().getItems();
            int treeHeight = getViewer().getTree().getBounds().height;
            int numVisibleItems = treeHeight / getViewer().getTree().getItemHeight();
            long stopTime = 200 + System.currentTimeMillis();
            boolean cancel = false;
            if (items.length > 0 && recursiveExpand(items, monitor, stopTime, new int[] { expansionCount == 0 ? numVisibleItems : expansionCount }))
            {
              cancel = true;
            }

            updateToolbar(true);

            if (cancel)
            {
              return Status.CANCEL_STATUS;
            }
          }
          else
          {
            updateToolbar(false);
          }
        }
        finally
        {
          TreeItem[] items = getViewer().getTree().getItems();
          if (items.length > 0 && getViewer().getTree().getSelectionCount() == 0)
          {
            treeViewer.getTree().setTopItem(items[0]);
          }

          redrawFalseControl.setRedraw(true);
        }

        return Status.OK_STATUS;
      }

      private boolean recursiveExpand(TreeItem[] items, IProgressMonitor monitor, long cancelTime, int[] numItemsLeft)
      {
        boolean canceled = false;
        for (int i = 0; !canceled && i < items.length; i++)
        {
          TreeItem item = items[i];
          boolean visible = numItemsLeft[0]-- >= 0;
          if (monitor.isCanceled() || !visible && System.currentTimeMillis() > cancelTime)
          {
            canceled = true;
          }
          else
          {
            Object itemData = item.getData();
            if (itemData != null && (expansionFilter == null || expansionFilter.shouldExpand(itemData)))
            {
              if (!item.getExpanded())
              {
                treeViewer.setExpandedState(itemData, true);
              }

              TreeItem[] children = item.getItems();
              if (items.length > 0)
              {
                canceled = recursiveExpand(children, monitor, cancelTime, numItemsLeft);
              }
            }
          }
        }

        return canceled;
      }

      @Override
      public Display getDisplay()
      {
        return UIUtil.getDisplay();
      }

      @Override
      public boolean shouldSchedule()
      {
        return true;
      }

      @Override
      public boolean shouldRun()
      {
        return true;
      }

      @Override
      public boolean belongsTo(Object family)
      {
        return family == refreshJobFamily;
      }
    };
  }

  public static interface ExpansionFilter
  {
    public boolean shouldExpand(Object element);
  }
}
