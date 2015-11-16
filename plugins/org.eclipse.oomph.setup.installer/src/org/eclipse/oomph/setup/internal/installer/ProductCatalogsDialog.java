/*
 * Copyright (c) 2015 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yatta Solutions - [467209] initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.internal.core.util.SelfProductCatalogURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andreas Scharf
 */
public class ProductCatalogsDialog extends AbstractSetupDialog
{
  public static final String TITLE = "Product Catalogs";

  public static final String DESCRIPTION = "Select the product catalogs to use";

  private CatalogManager catalogManager;

  private CheckboxTableViewer catalogViewer;

  private boolean product;

  public ProductCatalogsDialog(Shell parentShell, CatalogManager catalogManager, boolean product)
  {
    super(parentShell, TITLE, 450, 300, SetupInstallerPlugin.INSTANCE, false);
    this.catalogManager = catalogManager;
    this.product = product;
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return DESCRIPTION + ".";
  }

  @Override
  protected void createUI(Composite parent)
  {
    catalogViewer = CheckboxTableViewer.newCheckList(parent, SWT.NONE);
    catalogViewer.getTable().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
    catalogViewer.setContentProvider(new IStructuredContentProvider()
    {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
        // Do nothing.
      }

      public void dispose()
      {
        // Do nothing.
      }

      public Object[] getElements(Object inputElement)
      {
        if (!(inputElement instanceof CatalogManager))
        {
          throw new IllegalArgumentException("input must be a CatalogManager");
        }

        List<Scope> scopes = new ArrayList<Scope>();
        for (Scope scope : catalogManager.getCatalogs(product))
        {
          if (!"redirectable".equals(scope.getName()))
          {
            scopes.add(scope);
          }
        }

        return scopes.toArray();
      }
    });

    catalogViewer.addFilter(new ViewerFilter()
    {
      @Override
      public boolean select(Viewer viewer, Object parentElement, Object element)
      {
        return element instanceof Scope && !SelfProductCatalogURIHandlerImpl.SELF_PRODUCT_CATALOG_NAME.equals(((Scope)element).getName());
      }
    });

    catalogViewer.setLabelProvider(new LabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        return SetupCoreUtil.getLabel((Scope)element);
      }
    });

    catalogViewer.setInput(catalogManager);
    catalogViewer.setCheckedElements(catalogManager.getSelectedCatalogs(product).toArray());
  }

  @Override
  protected void okPressed()
  {
    List<Object> checkedElements = Arrays.asList(catalogViewer.getCheckedElements());
    for (Scope scope : catalogManager.getCatalogs(product))
    {
      catalogManager.selectCatalog(product, scope, checkedElements.contains(scope));
    }

    super.okPressed();
  }
}
