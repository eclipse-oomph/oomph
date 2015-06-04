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

import org.eclipse.oomph.internal.ui.FlatButton;
import org.eclipse.oomph.internal.ui.ImageHoverButton;
import org.eclipse.oomph.ui.SearchField.FilterHandler;
import org.eclipse.oomph.ui.StackComposite;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
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

  private FlatButton clearSearchButton;

  private StackComposite buttonComposite;

  @SuppressWarnings("restriction")
  public SimpleSearchField(final Composite parent, final FilterHandler filterHandler)
  {
    super(parent, SWT.NONE);
    GridLayout layout = UIUtil.createGridLayout(2);
    layout.horizontalSpacing = 0;
    layout.marginLeft = 18;
    layout.marginRight = 24;
    setLayout(layout);

    setBackgroundMode(SWT.INHERIT_FORCE);

    searchField = new Text(this, SWT.NONE);
    searchField.setLayoutData(GridDataFactory.swtDefaults().align(SWT.FILL, SWT.CENTER).grab(true, true).create());
    searchField.setMessage(org.eclipse.ui.internal.WorkbenchMessages.FilteredTree_FilterMessage);
    searchField.setFont(SimpleInstallerDialog.getFont(2, "normal"));
    searchField.setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
    searchField.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        filterHandler.handleFilter(searchField.getText());
        updateSearchAction();
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

    buttonComposite = new StackComposite(this, SWT.NONE);
    buttonComposite.setLayoutData(GridDataFactory.swtDefaults().align(SWT.CENTER, SWT.CENTER).grab(false, true).create());

    searchLabel = new Label(buttonComposite, SWT.NONE);
    searchLabel.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("simple/search.png"));

    clearSearchButton = new ImageHoverButton(buttonComposite, SWT.PUSH, SetupInstallerPlugin.INSTANCE.getSWTImage("simple/search_erase.png"),
        SetupInstallerPlugin.INSTANCE.getSWTImage("simple/search_erase_hover.png"));
    clearSearchButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        searchField.setText("");
      }
    });

    setBackground(UIUtil.getDisplay().getSystemColor(SWT.COLOR_WHITE));

    updateSearchAction();
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

  /**
   * Subclasses may override.
   */
  protected void finishFilter()
  {
    // Do nothing.
  }

  private void updateSearchAction()
  {
    boolean containsText = !StringUtil.isEmpty(searchField.getText());
    buttonComposite.setTopControl(containsText ? clearSearchButton : searchLabel);
  }
}
