/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
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

    PatternFilter patternFilter = new PatternFilter()
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

        return super.filter(viewer, parent, elements);
      }
    };

    filteredTree = new FilteredTree(this, SWT.NONE, patternFilter, true)
    {
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
    filterControl.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.keyCode == SWT.ESC)
        {
          filterControl.setText("");
          e.doit = false;
        }
        else if (e.keyCode == SWT.CR || e.keyCode == SWT.ARROW_DOWN)
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

  @Override
  protected void checkSubclass()
  {
    // Do nothing.
  }

  /**
   * Subclasses may override.
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
