/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

/**
 * @author Eike Stepper
 */
public class SearchField extends Composite
{
  private final FilteredTree filteredTree;

  public SearchField(Composite parent, final FilterHandler filterHandler)
  {
    super(parent, SWT.NONE);
    setLayout(new FirstChildLayout());

    final PatternFilter patternFilter = new PatternFilter()
    {
      @Override
      public Object[] filter(Viewer viewer, Object parent, Object[] elements)
      {
        if (filteredTree != null)
        {
          Text filterControl = filteredTree.getFilterControl();
          String filter = filterControl.getText();
          filterHandler.handleFilter(filter);
        }

        return new Object[0];
      }
    };

    filteredTree = new FilteredTree(this, SWT.NONE, patternFilter, true)
    {
      @Override
      @SuppressWarnings("restriction")
      protected void init(int treeStyle, PatternFilter filter)
      {
        ReflectUtil.setValue("patternFilter", this, filter);

        showFilterControls = PlatformUI.getPreferenceStore().getBoolean(IWorkbenchPreferenceConstants.SHOW_FILTERED_TEXTS);
        createControl(SearchField.this, treeStyle);

        Job refreshJob = new Job("Refresh Filter")
        {
          @Override
          protected IStatus run(IProgressMonitor monitor)
          {
            UIUtil.syncExec(new Runnable()
            {
              public void run()
              {
                patternFilter.filter(treeViewer, (Object)null, null);

                String text = getFilterString();
                updateToolbar(text != null && text.length() > 0 && !(initialText != null && initialText.equals(text)));
              }
            });

            return Status.OK_STATUS;
          }
        };

        refreshJob.setSystem(true);
        ReflectUtil.setValue("refreshJob", this, refreshJob);

        setInitialText(org.eclipse.ui.internal.WorkbenchMessages.FilteredTree_FilterMessage);
        setFont(SearchField.this.getFont());
      }

      @Override
      protected void createControl(Composite xxx, int treeStyle)
      {
        super.createControl(SearchField.this, treeStyle);

        Tree tree = treeViewer.getTree();
        tree.setParent(SearchField.this);
        tree.setLayoutData(new GridData(0, 0));

        treeComposite.dispose();
        treeComposite = null;
      }
    };

    final Text filterControl = filteredTree.getFilterControl();
    filterControl.addTraverseListener(new TraverseListener()
    {
      public void keyTraversed(TraverseEvent e)
      {
        if (e.keyCode == SWT.ESC)
        {
          if (!"".equals(filterControl.getText()))
          {
            filterControl.setText("");
            e.doit = false;
          }
        }
      }
    });

    filterControl.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.keyCode == SWT.CR || e.keyCode == SWT.ARROW_DOWN)
        {
          finishFilter();
          e.doit = false;
        }
      }
    });
  }

  public final PatternFilter getPatternFilter()
  {
    return filteredTree.getPatternFilter();
  }

  public final Text getFilterControl()
  {
    return filteredTree.getFilterControl();
  }

  public final void setInitialText(String text)
  {
    filteredTree.setInitialText(text);
  }

  @Override
  public final boolean getEnabled()
  {
    return filteredTree.getEnabled();
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    filteredTree.setEnabled(enabled);
  }

  @Override
  public boolean setFocus()
  {
    return getFilterControl().setFocus();
  }

  /**
   * Subclasses may override.
   * @param e
   */
  protected void finishFilter()
  {
    // Do nothing.
  }

  /**
   * @author Eike Stepper
   */
  public interface FilterHandler
  {
    public void handleFilter(String filter);
  }
}
