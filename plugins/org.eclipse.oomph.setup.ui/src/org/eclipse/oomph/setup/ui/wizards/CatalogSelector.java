/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.internal.core.util.IndexManager;
import org.eclipse.oomph.setup.internal.core.util.SelfProductCatalogURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.IndexManagerDialog;

import org.eclipse.emf.common.util.URI;

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
import java.util.Map;

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

  public List<? extends Scope> getCatalogs()
  {
    return catalogManager.getCatalogs(product);
  }

  public void select(Scope catalog, boolean on)
  {
    catalogManager.selectCatalog(product, catalog, on);
  }

  public void configure(final SetupWizard setupWizard, final ToolItem toolItem, final boolean supportIndexSwitching)
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

        List<? extends Scope> selectedCatalogs = catalogManager.getSelectedCatalogs(product);

        for (final Scope catalog : catalogManager.getCatalogs(product))
        {
          if (!catalog.eIsProxy())
          {
            String name = catalog.getName();
            if (!SelfProductCatalogURIHandlerImpl.SELF_PRODUCT_CATALOG_NAME.equals(name) && !"redirectable".equals(name))
            {
              final MenuItem item = new MenuItem(menu, SWT.CHECK);
              item.setText(SetupCoreUtil.getLabel(catalog));
              item.setSelection(selectedCatalogs.contains(catalog));
              item.addSelectionListener(new SelectionAdapter()
              {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                  boolean on = item.getSelection();
                  select(catalog, on);
                }
              });
            }
          }
        }

        if (supportIndexSwitching)
        {
          IndexManager indexManager = new IndexManager();
          Map<URI, String> indexChoices = indexManager.getIndexLabels(true);
          if (indexManager.getIndexNames(false).size() > 1)
          {
            Index currentIndex = catalogManager.getIndex();
            URI currentIndexLocation = setupWizard.getResourceSet().getURIConverter().normalize(currentIndex.eResource().getURI());

            if (menu.getItemCount() > 0)
            {
              new MenuItem(menu, SWT.SEPARATOR);
            }

            MenuItem menuItem = new MenuItem(menu, SWT.CASCADE);
            menuItem.setText("Switch Catalog Index");
            Menu switchMenu = new Menu(menu);
            menuItem.setMenu(switchMenu);

            for (Map.Entry<URI, String> entry : indexChoices.entrySet())
            {
              final MenuItem item = new MenuItem(switchMenu, SWT.RADIO);
              item.setText(entry.getValue());
              final URI indexProxyURI = entry.getKey();
              if (indexProxyURI.equals(currentIndexLocation))
              {
                item.setSelection(true);
              }

              item.addSelectionListener(new SelectionAdapter()
              {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                  if (item.getSelection())
                  {
                    setupWizard.reloadIndex(indexProxyURI);
                  }
                }
              });
            }

            if (switchMenu.getItemCount() > 0)
            {
              new MenuItem(switchMenu, SWT.SEPARATOR);
            }

            final MenuItem manageItem = new MenuItem(switchMenu, SWT.PUSH);
            manageItem.setText("Manage...");
            manageItem.addSelectionListener(new SelectionAdapter()
            {
              @Override
              public void widgetSelected(SelectionEvent e)
              {
                IndexManagerDialog indexManagerDialog = new IndexManagerDialog(setupWizard.getContainer().getShell());
                indexManagerDialog.open();
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
