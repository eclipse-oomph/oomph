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
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.internal.core.util.SelfProductCatalogURIHandlerImpl;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import java.util.List;

/**
 * @author Eike Stepper
 */
public class CatalogSelector
{
  private final CatalogManager catalogManager;

  private boolean product;

  public CatalogSelector(CatalogManager catalogManager, boolean product)
  {
    this.catalogManager = catalogManager;
    this.product = product;
  }

  public CatalogManager getCatalogManager()
  {
    return catalogManager;
  }

  public CatalogSelection getSelection()
  {
    return catalogManager.getSelection();
  }

  public List<? extends Scope> getSelectedCatalogs()
  {
    return catalogManager.getSelectedCatalogs(product);
  }

  public void configure(final ToolItem toolItem)
  {
    final ToolBar toolBar = toolItem.getParent();
    final Menu menu = new Menu(toolBar);

    toolItem.addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
        menu.dispose();
      }
    });

    toolItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        for (MenuItem menuItem : menu.getItems())
        {
          menuItem.dispose();
        }

        AdapterFactoryLabelProvider labelProvider = new AdapterFactoryLabelProvider(BaseEditUtil.createAdapterFactory());
        List<? extends Scope> selectedCatalogs = catalogManager.getSelectedCatalogs(product);
        for (final Scope catalog : catalogManager.getCatalogs(product))
        {
          if (!SelfProductCatalogURIHandlerImpl.SELF_PRODUCT_CATALOG_NAME.equals(catalog.getName()))
          {
            final MenuItem item = new MenuItem(menu, SWT.CHECK);
            item.setText(labelProvider.getText(catalog));
            item.setSelection(selectedCatalogs.contains(catalog));
            item.addSelectionListener(new SelectionAdapter()
            {
              @Override
              public void widgetSelected(SelectionEvent e)
              {
                boolean on = item.getSelection();
                catalogManager.selectCatalog(product, catalog, on);
              }
            });
          }
        }

        Composite parent = toolBar.getParent();
        Point location = parent.toDisplay(toolBar.getLocation());
        location.y = location.y + toolItem.getBounds().height;

        menu.setLocation(location);
        menu.setVisible(true);
      }
    });
  }
}
