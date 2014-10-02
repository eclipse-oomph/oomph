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
package org.eclipse.oomph.setup.internal.core.util;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupCorePlugin;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class CatalogManager
{
  private Index index;

  private CatalogSelection selection;

  public CatalogManager()
  {
    selection = SetupFactory.eINSTANCE.createCatalogSelection();
    Resource selectionResource = Resource.Factory.Registry.INSTANCE.getFactory(SetupContext.CATALOG_SELECTION_SETUP_URI).createResource(
        SetupContext.CATALOG_SELECTION_SETUP_URI);
    selectionResource.getContents().add(selection);
  }

  public Index getIndex()
  {
    return index;
  }

  public CatalogSelection getSelection()
  {
    return selection;
  }

  public List<? extends Scope> getCatalogs(boolean product)
  {
    @SuppressWarnings("unchecked")
    List<? extends Scope> result = (List<? extends Scope>)index.eGet(product ? SetupPackage.Literals.INDEX__PRODUCT_CATALOGS
        : SetupPackage.Literals.INDEX__PROJECT_CATALOGS);
    return result;
  }

  public List<? extends Scope> getSelectedCatalogs(boolean product)
  {
    @SuppressWarnings("unchecked")
    List<? extends Scope> result = (List<? extends Scope>)selection.eGet(product ? SetupPackage.Literals.CATALOG_SELECTION__PRODUCT_CATALOGS
        : SetupPackage.Literals.CATALOG_SELECTION__PROJECT_CATALOGS);
    return result;
  }

  public void selectCatalog(boolean product, Scope scope, boolean on)
  {
    @SuppressWarnings("unchecked")
    InternalEList<Scope> list = (InternalEList<Scope>)selection.eGet(product ? SetupPackage.Literals.CATALOG_SELECTION__PRODUCT_CATALOGS
        : SetupPackage.Literals.CATALOG_SELECTION__PROJECT_CATALOGS);

    boolean changed = on ? list.add(scope) : list.remove(scope);
    if (changed)
    {
      saveSelection();
    }
  }

  public void indexLoaded(Index index)
  {
    this.index = index;

    // Load the local selection into the same resource set as the index, but only if the local selection exists.
    ResourceSet resourceSet = index.eResource().getResourceSet();
    if (resourceSet.getURIConverter().exists(SetupContext.CATALOG_SELECTION_SETUP_URI, null))
    {
      // Load local selection file
      Resource selectionResource = BaseUtil.loadResourceSafely(resourceSet, SetupContext.CATALOG_SELECTION_SETUP_URI);
      CatalogSelection selection = (CatalogSelection)EcoreUtil.getObjectByType(selectionResource.getContents(), SetupPackage.Literals.CATALOG_SELECTION);

      // Ensure that all proxies are resolved and then remove the loaded selection from its resource.
      EcoreUtil.resolveAll(selectionResource);
      selectionResource.getContents().clear();

      // Add the managed selection to that same resource so it's proxies can resolve.
      selectionResource.getContents().add(this.selection);

      // Update the maps so they're hashed by the resolved proxies.
      updateProxyKeyedMap(this.selection.getDefaultProductVersions());
      updateProxyKeyedMap(this.selection.getDefaultStreams());

      if (selection != null)
      {
        // Copy over all the features of the loaded selection to the managed selection.
        this.selection.getDefaultProductVersions().putAll(selection.getDefaultProductVersions());
        this.selection.getDefaultStreams().putAll(selection.getDefaultStreams());
        EList<Stream> selectedStreams = this.selection.getSelectedStreams();
        selectedStreams.clear();
        selectedStreams.addAll(selection.getSelectedStreams());
        populateSelectedProductCatalogs(this.selection, index);
        populateSelectedProjectCatalogs(this.selection, index);
      }

      // Filter out any proxies that remain in the managed selection.
      filterProxies(this.selection.getDefaultProductVersions());
      filterProxies(this.selection.getDefaultStreams());
      filterProxies(this.selection.getProductCatalogs());
      filterProxies(this.selection.getProjectCatalogs());

      // If the managed selection's product catalog is empty, fill it with all available product catalogs from the index.
      if (this.selection.getProductCatalogs().isEmpty())
      {
        this.selection.getProductCatalogs().addAll(index.getProductCatalogs());
      }

      // If the managed selection's project catalog is empty, fill it with all available project catalogs from the index.
      if (this.selection.getProjectCatalogs().isEmpty())
      {
        this.selection.getProjectCatalogs().addAll(index.getProjectCatalogs());
      }
    }
    else
    {
      // Add the managed selection's resource to the index's resource set.
      Resource selectionResource = selection.eResource();
      resourceSet.getResources().add(selectionResource);

      populateSelectedProductCatalogs(selection, index);
      populateSelectedProjectCatalogs(selection, index);

      saveSelection();
    }
  }

  private void populateSelectedProductCatalogs(CatalogSelection selection, Index index)
  {
    EList<ProductCatalog> productCatalogs = selection.getProductCatalogs();
    for (ProductCatalog productCatalog : index.getProductCatalogs())
    {
      if (!"catalog".equals(EcoreUtil.getURI(productCatalog).scheme()))
      {
        productCatalogs.add(productCatalog);
      }
    }
  }

  private void populateSelectedProjectCatalogs(CatalogSelection selection, Index index)
  {
    selection.getProjectCatalogs().addAll(index.getProjectCatalogs());
  }

  private <K extends EObject, V extends EObject> void updateProxyKeyedMap(EMap<K, V> eMap)
  {
    EMap<K, V> originalEMap = new BasicEMap<K, V>(eMap.map());
    eMap.clear();
    eMap.putAll(originalEMap);
  }

  private <K extends EObject, V extends EObject> void filterProxies(EMap<K, V> eMap)
  {
    for (Iterator<Map.Entry<K, V>> it = eMap.iterator(); it.hasNext();)
    {
      Map.Entry<K, V> entry = it.next();
      K key = entry.getKey();
      V value = entry.getValue();

      if (key == null || key.eIsProxy() || value == null || value.eIsProxy())
      {
        it.remove();
      }
    }
  }

  private void filterProxies(EList<? extends EObject> list)
  {
    for (Iterator<? extends EObject> it = list.iterator(); it.hasNext();)
    {
      if (it.next().eIsProxy())
      {
        it.remove();
      }
    }
  }

  public void saveSelection()
  {
    try
    {
      selection.eResource().save(null);
    }
    catch (Exception ex)
    {
      SetupCorePlugin.INSTANCE.log(ex);
    }
  }
}
