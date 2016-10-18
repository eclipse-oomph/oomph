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
package org.eclipse.oomph.setup.internal.core.util;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupCorePlugin;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class CatalogManager
{
  public static final String PROPERTY_INDEX = "PROPERTY_INDEX";

  private final IndexManager indexManager = new IndexManager();

  private final CatalogSelection selection;

  private Index index;

  private PropertyChangeSupport propertyChangeSupport;

  public CatalogManager()
  {
    selection = SetupFactory.eINSTANCE.createCatalogSelection();
    Resource selectionResource = SetupCoreUtil.RESOURCE_FACTORY_REGISTRY.getFactory(SetupContext.CATALOG_SELECTION_SETUP_URI)
        .createResource(SetupContext.CATALOG_SELECTION_SETUP_URI);
    selectionResource.getContents().add(selection);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    getPropertyChangeSupport().addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener)
  {
    getPropertyChangeSupport().removePropertyChangeListener(listener);
  }

  private synchronized PropertyChangeSupport getPropertyChangeSupport()
  {
    if (propertyChangeSupport == null)
    {
      propertyChangeSupport = new PropertyChangeSupport(this);
    }

    return propertyChangeSupport;
  }

  public Index getIndex()
  {
    return index;
  }

  public boolean isCurrentIndex(URI indexURI)
  {
    if (index == null)
    {
      return false;
    }

    Resource resource = index.eResource();
    URI currentIndexURI = resource.getResourceSet().getURIConverter().normalize(resource.getURI());
    if (currentIndexURI.equals(indexURI))
    {
      return true;
    }

    if (currentIndexURI.isArchive())
    {
      String authority = currentIndexURI.authority();
      if (authority.equals(indexURI + "!"))
      {
        return true;
      }
    }

    return false;
  }

  public CatalogSelection getSelection()
  {
    return selection;
  }

  public Scope getCatalog(boolean product, String name)
  {
    for (Scope scope : getCatalogs(product))
    {
      if (name.equals(scope.getName()))
      {
        return scope;
      }
    }

    return null;
  }

  public List<? extends Scope> getCatalogs(boolean product)
  {
    if (index == null)
    {
      return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    List<? extends Scope> result = (List<? extends Scope>)index
        .eGet(product ? SetupPackage.Literals.INDEX__PRODUCT_CATALOGS : SetupPackage.Literals.INDEX__PROJECT_CATALOGS);
    return result;
  }

  public List<? extends Scope> getSelectedCatalogs(boolean product)
  {
    @SuppressWarnings("unchecked")
    List<? extends Scope> result = (List<? extends Scope>)selection
        .eGet(product ? SetupPackage.Literals.CATALOG_SELECTION__PRODUCT_CATALOGS : SetupPackage.Literals.CATALOG_SELECTION__PROJECT_CATALOGS);
    return result;
  }

  public void selectCatalog(boolean product, Scope scope, boolean on)
  {
    @SuppressWarnings("unchecked")
    InternalEList<Scope> list = (InternalEList<Scope>)selection
        .eGet(product ? SetupPackage.Literals.CATALOG_SELECTION__PRODUCT_CATALOGS : SetupPackage.Literals.CATALOG_SELECTION__PROJECT_CATALOGS);

    boolean changed = on ? list.add(scope) : list.remove(scope);
    if (changed)
    {
      saveSelection();
    }
  }

  public void indexLoaded(Index index)
  {
    if (index == null)
    {
      return;
    }

    Index oldIndex = this.index;
    this.index = index;

    // If the name of the index changes, we clear out the current state of the selection.
    String indexName = index.getName();
    if (oldIndex != null && !ObjectUtil.equals(indexName, oldIndex.getName()))
    {
      selection.getDefaultProductVersions().clear();
      selection.getDefaultStreams().clear();
      selection.getProductCatalogs().clear();
      selection.getProjectCatalogs().clear();
      selection.getSelectedStreams().clear();
      selection.getAnnotations().clear();
    }

    indexManager.addIndex(index);

    ResourceSet resourceSet = index.eResource().getResourceSet();
    URIConverter uriConverter = resourceSet.getURIConverter();

    // Determine the URI of the selection from the name of the index.
    URI catalogURI = StringUtil.isEmpty(indexName) || "Eclipse".equals(indexName) ? SetupContext.CATALOG_SELECTION_SETUP_URI
        : SetupContext.CATALOG_SELECTION_SETUP_URI.trimSegments(1)
            .appendSegment(URI.encodeSegment(indexName.replace(' ', '.').toLowerCase(), false) + '.' + SetupContext.CATALOG_SELECTION_SETUP_URI.lastSegment());
    if (catalogURI != null && !catalogURI.equals(SetupContext.CATALOG_SELECTION_SETUP_URI))
    {
      // If it's not the default, redirect the default to the location.
      uriConverter.getURIMap().put(SetupContext.CATALOG_SELECTION_SETUP_URI, catalogURI);
    }
    else
    {
      // Otherwise remove the mapping we might have added previously.
      uriConverter.getURIMap().remove(SetupContext.CATALOG_SELECTION_SETUP_URI);
    }

    // Load the local selection into the same resource set as the index, but only if the local selection exists.
    if (uriConverter.exists(SetupContext.CATALOG_SELECTION_SETUP_URI, null))
    {
      // Load local selection file
      Resource selectionResource = BaseUtil.loadResourceSafely(resourceSet, SetupContext.CATALOG_SELECTION_SETUP_URI);
      CatalogSelection selection = (CatalogSelection)EcoreUtil.getObjectByType(selectionResource.getContents(), SetupPackage.Literals.CATALOG_SELECTION);

      // Ensure that all proxies are resolved and then remove the loaded selection from its resource.
      EcoreUtil.resolveAll(selectionResource);
      selectionResource.getContents().clear();

      // Add the managed selection to that same resource so its proxies can resolve.
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

        EList<ProductCatalog> productCatalogs = this.selection.getProductCatalogs();
        productCatalogs.clear();
        productCatalogs.addAll(selection.getProductCatalogs());
        filter(productCatalogs, index.getProductCatalogs());

        EList<ProjectCatalog> projectCatalogs = this.selection.getProjectCatalogs();
        projectCatalogs.clear();
        projectCatalogs.addAll(selection.getProjectCatalogs());
        filter(projectCatalogs, index.getProjectCatalogs());
      }

      // Filter out any proxies that remain in the managed selection.
      filterProxies(this.selection.getDefaultProductVersions());
      filterProxies(this.selection.getDefaultStreams());
      filterProxies(this.selection.getProductCatalogs());
      filterProxies(this.selection.getProjectCatalogs());

      // If the managed selection's product catalog is empty, fill it with all available product catalogs from the index.
      if (this.selection.getProductCatalogs().isEmpty())
      {
        populateSelectedProductCatalogs(this.selection, index);
      }

      // If the managed selection's project catalog is empty, fill it with all available project catalogs from the index.
      if (this.selection.getProjectCatalogs().isEmpty())
      {
        populateSelectedProjectCatalogs(this.selection, index);
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

    getPropertyChangeSupport().firePropertyChange(PROPERTY_INDEX, oldIndex, this.index);
  }

  private <T extends Scope> void filter(EList<T> selectionScopes, EList<T> indexScopes)
  {
    List<T> scopes = new ArrayList<T>(indexScopes);
    scopes.retainAll(selectionScopes);
    for (Iterator<T> it = scopes.iterator(); it.hasNext();)
    {
      T scope = it.next();
      if ("redirectable".equals(scope.getName()))
      {
        it.remove();
      }
    }

    ECollections.setEList(selectionScopes, scopes);
  }

  private void populateSelectedProductCatalogs(CatalogSelection selection, Index index)
  {
    EList<ProductCatalog> productCatalogs = selection.getProductCatalogs();
    for (ProductCatalog productCatalog : index.getProductCatalogs())
    {
      URI uri = EcoreUtil.getURI(productCatalog);
      if (!"catalog".equals(uri.scheme()) && !SelfProductCatalogURIHandlerImpl.SELF_PRODUCT_CATALOG_NAME.equals(productCatalog.getName())
          && !productCatalog.getProducts().isEmpty())
      {
        productCatalogs.add(productCatalog);
      }
    }
  }

  private void populateSelectedProjectCatalogs(CatalogSelection selection, Index index)
  {
    EList<ProjectCatalog> projectCatalogs = selection.getProjectCatalogs();
    for (ProjectCatalog projectCatalog : index.getProjectCatalogs())
    {
      if (!projectCatalog.getProjects().isEmpty())
      {
        projectCatalogs.add(projectCatalog);
      }
    }
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
    // Check if any containers are proxies.
    // In this case save will fail.
    // This case should only happen if we're trying to save while reloading the index and in that case we don't need to save.
    for (EObject eObject : selection.eCrossReferences())
    {
      InternalEObject internalEObject = (InternalEObject)eObject;
      for (InternalEObject eContainer = internalEObject.eInternalContainer(); eContainer != null; eContainer = eContainer.eInternalContainer())
      {
        if (eContainer.eIsProxy())
        {
          return;
        }
      }
    }

    try
    {
      selection.eResource().save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
    }
    catch (Exception ex)
    {
      SetupCorePlugin.INSTANCE.log(ex);
    }
  }
}
