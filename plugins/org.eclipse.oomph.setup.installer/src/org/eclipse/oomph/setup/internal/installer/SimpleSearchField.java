/*
 * Copyright (c) 2015 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yatta Solutions - [466264] initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.ui.SearchField.FilterHandler;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Andreas Scharf
 */
public class SimpleSearchField extends Composite
{
  private Text searchField;

  private Label searchLabel;

  @SuppressWarnings("restriction")
  public SimpleSearchField(final Composite parent, final FilterHandler filterHandler)
  {
    super(parent, SWT.NONE);
    GridLayout layout = UIUtil.createGridLayout(2);
    layout.horizontalSpacing = 0;
    layout.marginLeft = 18;
    layout.marginRight = 24;
    setLayout(layout);

    searchField = new Text(this, SWT.NONE);
    searchField.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, true).create());
    searchField.setMessage(org.eclipse.ui.internal.WorkbenchMessages.FilteredTree_FilterMessage);
    searchField.setFont(SetupInstallerPlugin.getFont(getFont(), URI.createURI("font:///11/normal")));
    searchField.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        filterHandler.handleFilter(searchField.getText());
      }
    });

    searchField.addTraverseListener(new TraverseListener()
    {
      public void keyTraversed(TraverseEvent e)
      {
        if (e.keyCode == SWT.ESC)
        {
          if (!"".equals(searchField.getText()))
          {
            searchField.setText("");
            e.doit = false;
          }
        }
      }
    });

    searchField.addKeyListener(new KeyAdapter()
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

    searchLabel = new Label(this, SWT.NONE);
    searchLabel.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/search.png"));
    searchLabel.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(false, true).create());
    searchLabel.setBackground(null);
    setBackground(UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE));
  }

  public String getFilterText()
  {
    return searchField.getText();
  }

  @Override
  public void setFont(Font font)
  {
    super.setFont(font);
    searchField.setFont(font);
  }

  @Override
  public void setBackground(Color color)
  {
    super.setBackground(color);
    searchField.setBackground(color);
    searchLabel.setBackground(color);
  }

  /**
   * Subclasses may override.
   */
  protected void finishFilter()
  {
    // Do nothing.
  }
}
