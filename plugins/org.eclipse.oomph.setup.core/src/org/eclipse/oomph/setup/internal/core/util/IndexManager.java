/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.core.util;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.base.util.ArchiveResourceImpl;
import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Ed Merks
 */
public class IndexManager
{
  private static URI DEFAULT_INDEX_LOCATION = URI
      .createURI("archive:http://www.eclipse.org/setups/setups.zip!/http/git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/org.eclipse.setup");

  private final ResourceSet resourceSet = new ResourceSetImpl();

  private final URI indicesLocation;

  private final Resource indicesResource;

  private final IndexManager globalIndexManager;

  public IndexManager()
  {
    this(SetupContext.CONFIGURATION_STATE_LOCATION_URI.appendSegment("indices.xmi"),
        new IndexManager(SetupContext.GLOBAL_SETUPS_LOCATION_URI.appendSegment("indices.xmi"), null));
  }

  private IndexManager(URI indicesLocation, IndexManager globalIndexManager)
  {
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new BaseResourceFactoryImpl());
    this.indicesLocation = indicesLocation;
    indicesResource = resourceSet.createResource(indicesLocation);
    this.globalIndexManager = globalIndexManager;
  }

  public IndexManager getGlobalIndexManager()
  {
    return globalIndexManager;
  }

  public boolean addIndex(Index index)
  {
    Resource resource = index.eResource();
    URI uri = resource.getURI();
    URIConverter uriConverter = resource.getResourceSet().getURIConverter();
    URI indexLocation = uriConverter.normalize(uri);

    return addIndex(indexLocation, index.getName(), false);
  }

  public boolean addIndex(URI indexLocation, String name, boolean local)
  {
    if (!local && globalIndexManager != null)
    {
      globalIndexManager.addIndex(indexLocation, name, true);
    }

    Annotation annotation = getAnnotation();
    EMap<String, String> details = annotation.getDetails();
    String oldName = details.put(indexLocation.toString(), name);

    int entryIndex = details.indexOfKey(indexLocation.toString());
    if (entryIndex != 0)
    {
      details.move(0, entryIndex);
    }

    save(annotation);

    return !ObjectUtil.equals(oldName, name);
  }

  public void remove(URI indexLocation, boolean local)
  {
    if (!local && globalIndexManager != null)
    {
      globalIndexManager.remove(indexLocation, true);
    }

    Annotation annotation = getAnnotation();
    Annotation labelsAnnotation = annotation.getAnnotation("labels");

    annotation.getDetails().remove(indexLocation.toString());
    if (labelsAnnotation != null)
    {
      labelsAnnotation.getDetails().remove(indexLocation.toString());
    }

    save(annotation);
  }

  public void setLabel(URI indexLocation, String label, boolean local)
  {
    if (!local && globalIndexManager != null)
    {
      globalIndexManager.setLabel(indexLocation, label, true);
    }

    Annotation annotation = getAnnotation();
    Annotation labelsAnnotation = annotation.getAnnotation("labels");
    if (labelsAnnotation == null)
    {
      labelsAnnotation = BaseFactory.eINSTANCE.createAnnotation();
      labelsAnnotation.setSource("labels");
      annotation.getAnnotations().add(labelsAnnotation);
    }

    labelsAnnotation.getDetails().put(indexLocation.toString(), label);

    save(annotation);
  }

  public void configure(ResourceSet resourceSet)
  {
    // If there are any filter properties in place, we have to assume that we're in a crippled installer
    // or in an application that has specified exactly the index they want to use in this application,
    // so don't switch the most recently used index in this case.
    if (StringUtil.isEmpty(PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_PRODUCT_CATALOG_FILTER))
        && StringUtil.isEmpty(PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_PRODUCT_FILTER))
        && StringUtil.isEmpty(PropertiesUtil.getProperty(SetupProperties.PROP_SETUP_PRODUCT_VERSION_FILTER)))
    {
      // If the resource set is configured to use the default location...
      URI currentIndexLocation = resourceSet.getURIConverter().normalize(SetupContext.INDEX_SETUP_URI);
      if (currentIndexLocation.equals(DEFAULT_INDEX_LOCATION) || currentIndexLocation.equals(SetupContext.INDEX_SETUP_LOCATION_URI))
      {
        // If there are indices in the local indices.xmi...
        Annotation annotation = getAnnotation();
        EMap<String, String> details = annotation.getDetails();
        if (!details.isEmpty())
        {
          // If the first index location is different from the default location (which is already configured in the given resource set)
          // and the current index location is also already in present,
          // only then configure the resource set to use the first location,
          // which is the most recently used index for this application,
          // or, if it's the first time this application is executed,
          // the most recently used index for any application on this machine.
          URI indexLocation = URI.createURI(details.get(0).getKey());
          if (!indexLocation.equals(DEFAULT_INDEX_LOCATION) && currentIndexLocation.equals(SetupContext.INDEX_SETUP_LOCATION_URI)
              || details.containsKey(currentIndexLocation.toString()) && !currentIndexLocation.equals(indexLocation))
          {
            configureForProxy(resourceSet, indexLocation);
          }
        }
      }
    }
  }

  public void configureForProxy(ResourceSet resourceSet, URI indexLocation)
  {
    // If the index location is an entry in an archive...
    if (indexLocation.isArchive())
    {
      // Configure for the resource set for the underlying archive as the whole.
      String authority = indexLocation.authority();
      URI archiveLocation = URI.createURI(authority.substring(0, authority.length() - 1));
      configure(resourceSet, archiveLocation);
    }
    else
    {
      // Configure the resource set to use the default archive location,
      // which the client may have already redirected in this resource set.
      configure(resourceSet, SetupContext.INDEX_SETUP_ARCHIVE_LOCATION_URI);

      // Add a URI mapping to ensure that index folder maps to the folder of the specified index location.
      Map<URI, URI> uriMap = resourceSet.getURIConverter().getURIMap();
      uriMap.put(SetupContext.INDEX_ROOT_URI, indexLocation.trimSegments(1).appendSegment(""));
    }
  }

  public void configure(ResourceSet resourceSet, URI setupArchiveURI)
  {
    // Load the entry information in the archive.
    Resource archiveResource = new ArchiveResourceImpl(setupArchiveURI, resourceSet.getURIConverter());
    try
    {
      archiveResource.load(null);
    }
    catch (IOException ex)
    {
      // Ignore.
    }

    // This will be represented as an annotation.
    Annotation annotation = (Annotation)EcoreUtil.getObjectByType(archiveResource.getContents(), BasePackage.Literals.ANNOTATION);
    if (annotation != null && !annotation.getDetails().isEmpty())
    {
      // Clear any previous redirections and reestablish a clean set of redirections.
      URIConverter uriConverter = resourceSet.getURIConverter();
      Map<URI, URI> uriMap = uriConverter.getURIMap();
      uriMap.clear();
      SetupCoreUtil.configureRedirections(uriMap);

      // Redirect the default archive location to this archive's location.
      uriMap.put(SetupContext.INDEX_SETUP_ARCHIVE_LOCATION_URI, setupArchiveURI);

      for (Map.Entry<String, String> entry : annotation.getDetails())
      {
        // Add mappings for each entry.
        URI sourceURI = URI.createURI(entry.getKey());
        if (sourceURI.equals(uriConverter.normalize(sourceURI)))
        {
          URI targtURI = URI.createURI(entry.getValue());
          uriMap.put(sourceURI, targtURI);

          // If this is the index resource itself...
          if (SetupContext.INDEX_SETUP_NAME.equals(sourceURI.lastSegment()))
          {
            // Map the index location to this location,
            // map the folder of the index location to this location's folder,
            // and map the whole index folder to this locations's folder.
            // This brute force approach is most likely to fully override any mappings that might already be in place in the resource set.
            uriMap.put(SetupContext.INDEX_SETUP_LOCATION_URI, targtURI);
            uriMap.put(SetupContext.INDEX_ROOT_LOCATION_URI, targtURI.trimSegments(1).appendSegment(""));
            uriMap.put(SetupContext.INDEX_ROOT_URI, targtURI.trimSegments(1).appendSegment(""));
          }
        }
      }
    }
  }

  public Map<URI, String> getIndexNames(boolean local)
  {
    Map<URI, String> result = new LinkedHashMap<URI, String>();
    Annotation annotation = getAnnotation();
    for (Map.Entry<String, String> detail : annotation.getDetails())
    {
      String name = detail.getValue();
      if (StringUtil.isEmpty(name))
      {
        name = "<unnamed-index>";
      }

      result.put(URI.createURI(detail.getKey()), name);
    }

    if (!local && globalIndexManager != null)
    {
      for (Map.Entry<URI, String> entry : globalIndexManager.getIndexNames(true).entrySet())
      {
        if (!result.containsKey(entry.getKey()))
        {
          result.put(entry.getKey(), entry.getValue());
        }
      }
    }

    return result;
  }

  public Map<URI, String> getIndexLabels(boolean local)
  {
    Annotation annotation = getAnnotation();
    Annotation labelsAnnotation = annotation.getAnnotation("labels");

    Map<URI, String> result = new LinkedHashMap<URI, String>();
    Set<String> labels = new HashSet<String>();
    Set<String> duplicates = new HashSet<String>();
    for (Map.Entry<String, String> detail : annotation.getDetails())
    {
      String label = null;
      String indexLocation = detail.getKey();
      if (labelsAnnotation != null)
      {
        label = labelsAnnotation.getDetails().get(indexLocation);
      }

      if (label == null)
      {
        String name = detail.getValue();
        if (!labels.add(name))
        {
          duplicates.add(name);
        }
      }

      result.put(URI.createURI(indexLocation), label);
    }

    for (Map.Entry<String, String> detail : annotation.getDetails())
    {
      URI indexLocation = URI.createURI(detail.getKey());
      if (result.get(indexLocation) == null)
      {
        String itemText = detail.getValue();
        if (StringUtil.isEmpty(itemText))
        {
          itemText = "<unnamed-index>";
        }

        if (duplicates.contains(itemText))
        {
          itemText += " - " + indexLocation;
        }

        result.put(indexLocation, itemText);
      }
    }

    if (!local && globalIndexManager != null)
    {
      for (Map.Entry<URI, String> entry : globalIndexManager.getIndexLabels(true).entrySet())
      {
        if (!result.containsKey(entry.getKey()))
        {
          result.put(entry.getKey(), entry.getValue());
        }
      }
    }

    return result;
  }

  public Map<URI, Boolean> getIndexAvailability(boolean local)
  {
    Map<URI, Boolean> result = new LinkedHashMap<URI, Boolean>();
    Set<URI> indexLocations = getIndexNames(local).keySet();
    URIConverter uriConverter = resourceSet.getURIConverter();
    for (URI indexLocation : indexLocations)
    {
      result.put(indexLocation, uriConverter.exists(indexLocation, null));
    }

    return result;
  }

  private Annotation getAnnotation()
  {
    BaseUtil.execute(5000, new Runnable()
    {
      public void run()
      {
        loadIndices();
      }
    }, resourceSet.getURIConverter(), indicesLocation);

    Annotation annotation = (Annotation)EcoreUtil.getObjectByType(indicesResource.getContents(), BasePackage.Literals.ANNOTATION);
    if (annotation == null)
    {
      annotation = createDefaultIndex();
    }

    return annotation;
  }

  private void loadIndices()
  {
    if (resourceSet.getURIConverter().exists(indicesLocation, null))
    {
      try
      {
        indicesResource.unload();
        indicesResource.load(resourceSet.getLoadOptions());
      }
      catch (IOException ex)
      {
        // Ignore.
      }

      Annotation annotation = (Annotation)EcoreUtil.getObjectByType(indicesResource.getContents(), BasePackage.Literals.ANNOTATION);
      if (annotation == null)
      {
        createDefaultIndex();
      }
    }
    else
    {
      createDefaultIndex();
    }
  }

  private Annotation createDefaultIndex()
  {
    Annotation annotation = null;
    if (globalIndexManager != null)
    {
      annotation = globalIndexManager.getAnnotation();
      if (annotation != null)
      {
        annotation = EcoreUtil.copy(annotation);
      }
    }

    if (annotation == null)
    {
      ResourceSet configuredResourceSet = SetupCoreUtil.createResourceSet();
      URI indexLocation = configuredResourceSet.getURIConverter().normalize(SetupContext.INDEX_SETUP_URI);
      annotation = BaseFactory.eINSTANCE.createAnnotation();
      annotation.setSource("IndexLocations");
      EMap<String, String> details = annotation.getDetails();
      details.put(indexLocation.toString(), "Eclipse");
    }

    EList<EObject> contents = indicesResource.getContents();
    contents.clear();
    contents.add(annotation);
    return annotation;
  }

  private void save(final Annotation annotation)
  {
    BaseUtil.execute(5000, new Runnable()
    {
      public void run()
      {
        BaseUtil.saveEObject(annotation);
      }
    }, resourceSet.getURIConverter(), indicesLocation);
  }

  public static URI getUnderlyingLocation(URI indexLocation)
  {
    if (indexLocation.isArchive())
    {
      String authority = indexLocation.authority();
      return URI.createURI(authority.substring(0, authority.length() - 1));
    }

    if (SetupContext.INDEX_SETUP_NAME.equals(indexLocation.lastSegment()))
    {
      return indexLocation.trimSegments(1).appendSegment("");
    }

    return indexLocation;
  }
}
