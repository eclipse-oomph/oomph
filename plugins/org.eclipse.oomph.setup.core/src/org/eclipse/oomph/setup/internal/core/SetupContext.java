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
package org.eclipse.oomph.setup.internal.core;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.LocationCatalog;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.impl.InstallationTaskImpl;
import org.eclipse.oomph.setup.internal.core.util.IndexManager;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.internal.core.util.URIResolver;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Eike Stepper
 */
public class SetupContext
{
  public static final boolean USE_RESOURCES_BUNDLE = PropertiesUtil.getProperty(SetupProperties.PROP_DO_NOT_LOAD_RESOURCES_PLUGIN) == null
      && CommonPlugin.IS_RESOURCES_BUNDLE_AVAILABLE;

  public static final String OOMPH_NODE = "org.eclipse.oomph.setup";

  public static final String LOG_FILE_NAME = "setup.log";

  public static final String USER_SCHEME = "user";

  public static boolean isUserScheme(String scheme)
  {
    return USER_SCHEME.equals(scheme) || "user-ext".equals(scheme);
  }

  /**
   * Resolves using the appropriate URI handler's implementation.
   * E.g., it resolves a '{@link #USER_SCHEME user}' scheme URI to a 'file' scheme URI.
   */
  public static URI resolve(URI uri)
  {
    for (URIHandler uriHandler : SetupCoreUtil.URI_CONVERTER.getURIHandlers())
    {
      if (uriHandler.canHandle(uri))
      {
        if (uriHandler instanceof URIResolver)
        {
          URIResolver uriResolver = (URIResolver)uriHandler;
          return uriResolver.resolve(uri);
        }

        break;
      }
    }

    return uri;
  }

  // Basic locations

  public static final URI CONFIGURATION_LOCATION_URI = getStaticConfigurationLocation();

  public static final URI WORKSPACE_LOCATION_URI = USE_RESOURCES_BUNDLE ? WorkspaceUtil.getStaticWorkspaceLocationURI() : null;

  public static final URI PRODUCT_LOCATION = getStaticInstallLocation();

  public static final URI PRODUCT_ROOT_LOCATION = PRODUCT_LOCATION.trimSegments(OS.INSTANCE.isMac() ? 3 : 1);

  // State locations

  public static final URI GLOBAL_STATE_LOCATION_URI = URI.createFileURI(PropertiesUtil.getUserHome()).appendSegments(new String[] { ".eclipse", OOMPH_NODE });

  public static final URI GLOBAL_SETUPS_URI = URI.createURI(USER_SCHEME + ":/");

  public static final URI GLOBAL_SETUPS_LOCATION_URI = GLOBAL_STATE_LOCATION_URI.appendSegment("setups");

  public static final URI CONFIGURATION_STATE_LOCATION_URI = CONFIGURATION_LOCATION_URI.appendSegment(OOMPH_NODE);

  public static final URI WORKSPACE_STATE_LOCATION_URI = WORKSPACE_LOCATION_URI == null ? null
      : WORKSPACE_LOCATION_URI.appendSegments(new String[] { ".metadata", ".plugins", OOMPH_NODE });

  // Resource locations

  public static final URI SETUP_LOG_URI = CONFIGURATION_STATE_LOCATION_URI.appendSegment(LOG_FILE_NAME);

  public static final String INDEX_SETUP_NAME = "org.eclipse.setup";

  public static final URI INDEX_ROOT_URI = URI.createURI("index:/");

  public static final URI INDEX_SETUP_URI = INDEX_ROOT_URI.appendSegment(INDEX_SETUP_NAME);

  public static final URI INDEX_ROOT_LOCATION_URI = URI.createURI("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/");

  public static final URI INDEX_SETUP_LOCATION_URI = INDEX_ROOT_LOCATION_URI.trimSegments(1).appendSegment(INDEX_SETUP_NAME);

  public static final URI INDEX_SETUP_ARCHIVE_LOCATION_URI = URI.createURI("http://www.eclipse.org/setups/setups.zip");

  public static final URI INSTALLATION_SETUP_FILE_NAME_URI = URI.createURI("installation.setup");

  public static final URI INSTALLATION_SETUP_URI = CONFIGURATION_STATE_LOCATION_URI.appendSegment(INSTALLATION_SETUP_FILE_NAME_URI.lastSegment());

  public static final URI WORKSPACE_SETUP_FILE_NAME_URI = URI.createURI("workspace.setup");

  public static final URI WORKSPACE_SETUP_URI = WORKSPACE_STATE_LOCATION_URI == null ? null
      : WORKSPACE_STATE_LOCATION_URI.appendSegment(WORKSPACE_SETUP_FILE_NAME_URI.lastSegment());

  public static final URI WORKSPACE_SETUP_RELATIVE_URI = URI
      .createHierarchicalURI(new String[] { ".metadata", ".plugins", OOMPH_NODE, WORKSPACE_SETUP_FILE_NAME_URI.lastSegment() }, null, null);

  public static final URI USER_SETUP_URI = GLOBAL_SETUPS_URI.appendSegment("user.setup");

  public static final URI USER_SETUP_LOCATION_URI = GLOBAL_SETUPS_LOCATION_URI.appendSegment("user.setup");

  public static final URI CATALOG_SELECTION_SETUP_URI = GLOBAL_SETUPS_URI.appendSegment("catalogs.setup");

  public static final URI CATALOG_SELECTION_SETUP_LOCATION_URI = GLOBAL_SETUPS_LOCATION_URI.appendSegment("catalogs.setup");

  public static final URI LOCATION_CATALOG_SETUP_URI = GLOBAL_SETUPS_URI.appendSegment("locations.setup");

  private static volatile SetupContext self = new SetupContext();

  // static creation methods

  // private constructors

  private final Installation installation;

  private final Workspace workspace;

  private final User user;

  private SetupContext()
  {
    installation = createInstallation();
    ((InternalEObject)installation).eSetProxyURI(INSTALLATION_SETUP_URI.appendFragment("/"));

    workspace = createWorkspace();
    if (WORKSPACE_SETUP_URI != null)
    {
      ((InternalEObject)workspace).eSetProxyURI(WORKSPACE_SETUP_URI.appendFragment("/"));
    }

    user = createUser();
    ((InternalEObject)user).eSetProxyURI(USER_SETUP_URI.appendFragment("/"));
  }

  private SetupContext(Installation installation, Workspace workspace, User user)
  {
    this.installation = installation;
    this.workspace = workspace;
    this.user = user;
  }

  public Installation getInstallation()
  {
    return installation;
  }

  public Workspace getWorkspace()
  {
    return workspace;
  }

  public User getUser()
  {
    return user;
  }

  /**
   * Returns a setup context consisting purely of proxy instances for the current self-hosted installation.
   */
  public static SetupContext getSelf()
  {
    return self;
  }

  public static void setSelf(SetupContext setupContext)
  {
    self = setupContext;
  }

  // static creation methods

  public static SetupContext createSelf(Installation installation, Workspace workspace, User user)
  {
    return proxify(installation, workspace, user);
  }

  public static SetupContext createSelf(ResourceSet resourceSet)
  {
    Resource indexResource = resourceSet.getResource(INDEX_SETUP_URI, false);
    if (indexResource != null)
    {
      Index index = (Index)EcoreUtil.getObjectByType(indexResource.getContents(), SetupPackage.Literals.INDEX);
      if (index != null)
      {
        new IndexManager().addIndex(index);
      }
    }

    Installation installation = getInstallation(resourceSet, true, Mode.CREATE_AND_SAVE);
    Workspace workspace = getWorkspace(resourceSet, true, Mode.CREATE_AND_SAVE);

    Installation effectiveInstallation = null;
    URI uri = installation.eResource().getURI();
    if (uri.segmentCount() > 3)
    {
      String eclipseLauncher = PropertiesUtil.getProperty("eclipse.launcher");
      if (eclipseLauncher != null)
      {
        File eclipseLauncherExecutable = new File(eclipseLauncher);
        if (eclipseLauncherExecutable.exists())
        {
          effectiveInstallation = installation;
        }
      }
    }

    SetupContext.associate(effectiveInstallation, workspace);

    User user = getUser(resourceSet, false);
    return createSelf(installation, workspace, user);
  }

  public static SetupContext createInstallationAndUser(ResourceSet resourceSet)
  {
    return new SetupContext(getInstallation(resourceSet, true, Mode.CREATE), null, getUser(resourceSet, true));
  }

  public static SetupContext create(ResourceSet resourceSet)
  {
    return new SetupContext(getInstallation(resourceSet, true, Mode.CREATE), getWorkspace(resourceSet, true, Mode.CREATE), getUser(resourceSet, true));
  }

  public static SetupContext createInstallationWorkspaceAndUser(ResourceSet resourceSet)
  {
    return new SetupContext(getInstallation(resourceSet, false, Mode.NONE), getWorkspace(resourceSet, false, Mode.NONE), getUser(resourceSet, false));
  }

  public static SetupContext createUserOnly(ResourceSet resourceSet)
  {
    return new SetupContext(null, null, getUser(resourceSet, true));
  }

  public static SetupContext create(ResourceSet resourceSet, ProductVersion productVersion)
  {
    Installation installation = getInstallation(resourceSet, false, Mode.CREATE);
    installation.setProductVersion(productVersion);
    return new SetupContext(installation, getWorkspace(resourceSet, false, Mode.NONE), getUser(resourceSet, true));
  }

  public static SetupContext create(ProductVersion productVersion, Stream stream)
  {
    User user = createUser();

    Workspace workspace = createWorkspace();
    workspace.getStreams().add(stream);

    Installation installation = createInstallation();
    installation.setProductVersion(productVersion);

    return new SetupContext(installation, workspace, user);
  }

  public static SetupContext create(Installation installation, Collection<? extends Stream> streams, User user)
  {
    Workspace workspace = getWorkspace(installation.eResource().getResourceSet(), false, Mode.CREATE);
    workspace.getStreams().addAll(streams);
    return new SetupContext(installation, workspace, user);
  }

  public static SetupContext create()
  {
    return new SetupContext();
  }

  public static SetupContext create(Installation installation, Workspace workspace, User user)
  {
    return new SetupContext(installation, workspace, user);
  }

  public static SetupContext create(Installation installation, Workspace workspace)
  {
    User user = createUser();
    Resource userResource = installation.eResource().getResourceSet().getResourceFactoryRegistry().getFactory(USER_SETUP_URI).createResource(USER_SETUP_URI);
    userResource.getContents().add(user);
    return new SetupContext(installation, workspace, user);
  }

  public static SetupContext createCopy(Installation installation, Workspace workspace, User user)
  {
    Resource.Factory.Registry resourceFactoryRegistry = SetupCoreUtil.createResourceSet().getResourceFactoryRegistry();

    Installation copiedInstallation = EcoreUtil.copy(installation);
    Resource installationResource = resourceFactoryRegistry.getFactory(INSTALLATION_SETUP_FILE_NAME_URI).createResource(INSTALLATION_SETUP_FILE_NAME_URI);
    installationResource.getContents().add(copiedInstallation);

    Workspace copiedWorkspace = EcoreUtil.copy(workspace);
    if (workspace != null)
    {
      Resource workResource = resourceFactoryRegistry.getFactory(WORKSPACE_SETUP_FILE_NAME_URI).createResource(WORKSPACE_SETUP_FILE_NAME_URI);
      workResource.getContents().add(copiedWorkspace);
    }

    User copiedUser = EcoreUtil.copy(user);
    Resource userResource = resourceFactoryRegistry.getFactory(USER_SETUP_URI).createResource(USER_SETUP_URI);
    userResource.getContents().add(copiedUser);

    return new SetupContext(copiedInstallation, copiedWorkspace, copiedUser);
  }

  public static User createUser()
  {
    User user = SetupFactory.eINSTANCE.createUser();
    user.setName(PropertiesUtil.getProperty("user.name", "user"));
    return user;
  }

  public static Workspace createWorkspace()
  {
    Workspace workspace = SetupFactory.eINSTANCE.createWorkspace();
    workspace.setName("workspace");
    return workspace;
  }

  public static Installation createInstallation()
  {
    Installation installation = SetupFactory.eINSTANCE.createInstallation();
    installation.setName("installation");
    return installation;
  }

  public static void associate(final Installation installation, final Workspace workspace)
  {
    try
    {
      final ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
      URIConverter uriConverter = resourceSet.getURIConverter();
      BaseUtil.execute(5000, new Runnable()
      {
        public void run()
        {
          associate(resourceSet, installation, workspace);
        }
      }, uriConverter, LOCATION_CATALOG_SETUP_URI, installation == null ? null : installation.eResource().getURI(),
          workspace == null ? null : workspace.eResource().getURI());
    }
    catch (Throwable throwable)
    {
      SetupCorePlugin.INSTANCE.log(throwable, IStatus.WARNING);
    }
  }

  public static LocationCatalog getLocationCatalog(ResourceSet resourceSet)
  {
    return associate(resourceSet, null, null);
  }

  private static LocationCatalog associate(ResourceSet resourceSet, Installation installation, Workspace workspace)
  {
    URIConverter uriConverter = resourceSet.getURIConverter();

    Resource resource;
    if (uriConverter.exists(LOCATION_CATALOG_SETUP_URI, null))
    {
      resource = BaseUtil.loadResourceSafely(resourceSet, LOCATION_CATALOG_SETUP_URI);
    }
    else
    {
      resource = resourceSet.createResource(LOCATION_CATALOG_SETUP_URI);
    }

    EList<EObject> contents = resource.getContents();
    LocationCatalog locationCatalog = (LocationCatalog)EcoreUtil.getObjectByType(contents, SetupPackage.Literals.LOCATION_CATALOG);
    if (locationCatalog == null)
    {
      locationCatalog = SetupFactory.eINSTANCE.createLocationCatalog();
      contents.add(locationCatalog);
    }

    EMap<Installation, EList<Workspace>> installations = locationCatalog.getInstallations();
    removeProxies(installations);

    EMap<Workspace, EList<Installation>> workspaces = locationCatalog.getWorkspaces();
    removeProxies(workspaces);

    Installation localInstallation = installation == null ? null : (Installation)resourceSet.getEObject(EcoreUtil.getURI(installation), true);
    Workspace localWorkspace = workspace == null ? null : (Workspace)resourceSet.getEObject(EcoreUtil.getURI(workspace), true);

    if (installation != null)
    {
      int installationEntryIndex = installations.indexOfKey(localInstallation);
      if (installationEntryIndex == -1)
      {
        installations.put(localInstallation, localWorkspace == null ? ECollections.<Workspace> emptyEList() : ECollections.singletonEList(localWorkspace));
        installations.move(0, installations.size() - 1);
      }
      else if (localWorkspace != null)
      {
        EList<Workspace> associatedWorkspaces = installations.get(installationEntryIndex).getValue();
        int position = associatedWorkspaces.indexOf(localWorkspace);
        if (position == -1)
        {
          associatedWorkspaces.add(0, localWorkspace);
        }
        else
        {
          associatedWorkspaces.move(0, position);
        }

        installations.move(0, installationEntryIndex);
      }
    }

    if (localWorkspace != null)
    {
      int workspaceEntryIndex = workspaces.indexOfKey(localWorkspace);
      if (workspaceEntryIndex == -1)
      {
        workspaces.put(localWorkspace, localInstallation == null ? ECollections.<Installation> emptyEList() : ECollections.singletonEList(localInstallation));
        workspaces.move(0, workspaces.size() - 1);
      }
      else if (localInstallation != null)
      {
        EList<Installation> associatedInstallations = workspaces.get(workspaceEntryIndex).getValue();
        int position = associatedInstallations.indexOf(localInstallation);
        if (position == -1)
        {
          associatedInstallations.add(0, localInstallation);
        }
        else
        {
          associatedInstallations.move(0, position);
        }

        workspaces.move(0, workspaceEntryIndex);
      }
    }

    try
    {
      resource.save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
    }
    catch (IOException ex)
    {
      SetupCorePlugin.INSTANCE.log(ex);
    }

    return locationCatalog;
  }

  private static <K extends EObject, V extends EObject> void removeProxies(EMap<K, EList<V>> map)
  {
    for (Iterator<Entry<K, EList<V>>> it = map.iterator(); it.hasNext();)
    {
      Entry<K, EList<V>> entry = it.next();
      K key = entry.getKey();
      if (key == null || key.eIsProxy())
      {
        it.remove();
      }
      else
      {
        for (Iterator<V> it2 = entry.getValue().iterator(); it2.hasNext();)
        {
          V value = it2.next();
          if (value.eIsProxy())
          {
            it2.remove();
          }
        }
      }
    }
  }

  private static URI getStaticInstallLocation()
  {
    try
    {
      if (!Platform.isRunning())
      {
        return URI.createFileURI(File.createTempFile("installation", "").toString());
      }

      Location location = Platform.getInstallLocation();
      URI result = getURI(location);
      if (OS.INSTANCE.isMac())
      {
        result = result.trimSegments(1).appendSegment("Eclipse");
      }

      return result;
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  private static URI getURI(Location location) throws IOException
  {
    URI result = URI.createURI(FileLocator.resolve(location.getURL()).toString());
    if (result.isFile())
    {
      result = URI.createFileURI(result.toFileString());
    }

    return result.hasTrailingPathSeparator() ? result.trimSegments(1) : result;
  }

  private static URI getStaticConfigurationLocation()
  {
    try
    {
      if (!Platform.isRunning())
      {
        return URI.createFileURI(File.createTempFile(InstallationTaskImpl.CONFIGURATION_FOLDER_NAME, "").toString());
      }

      Location location = Platform.getConfigurationLocation();
      URI result = getURI(location);

      Location parentLocation = location.getParentLocation();
      if (parentLocation != null)
      {
        URI targetInstallation = result.appendSegment(OOMPH_NODE).appendSegment("installation.setup");
        File target = new File(targetInstallation.toFileString());
        if (!target.exists())
        {
          URI parentURI = getURI(parentLocation);
          URI sourceInstallation = parentURI.appendSegment(OOMPH_NODE).appendSegment("installation.setup");
          File source = new File(sourceInstallation.toFileString());
          if (source.exists())
          {
            try
            {
              IOUtil.copyFile(source, target);
            }
            catch (IORuntimeException ex)
            {
              // Ignore.
            }
          }
        }
      }

      return result;
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
  }

  private enum Mode
  {
    NONE, CREATE, CREATE_AND_SAVE
  }

  private static Installation getInstallation(ResourceSet resourceSet, boolean realInstallation, Mode mode)
  {
    Installation installation = null;
    Resource installationResource = null;
    if (realInstallation)
    {
      if (resourceSet.getURIConverter().exists(INSTALLATION_SETUP_URI, null))
      {
        installationResource = BaseUtil.loadResourceSafely(resourceSet, INSTALLATION_SETUP_URI);
        installation = (Installation)EcoreUtil.getObjectByType(installationResource.getContents(), SetupPackage.Literals.INSTALLATION);
      }
    }
    else
    {
      installationResource = resourceSet.getResource(INSTALLATION_SETUP_FILE_NAME_URI, false);
      if (installationResource == null)
      {
        installationResource = resourceSet.getResource(INSTALLATION_SETUP_URI, false);
      }

      if (installationResource != null)
      {
        EList<EObject> contents = installationResource.getContents();
        if (contents.isEmpty())
        {
          // If it's not in the resource, and we're not going to create it and save it, then make sure it exists.
          if (mode == Mode.NONE)
          {
            installation = createInstallation();
            contents.add(installation);
          }
        }
        else
        {
          installation = (Installation)contents.get(0);
        }
      }
    }

    boolean save = false;
    if (installation == null && mode != Mode.NONE)
    {
      if (installationResource == null)
      {
        installationResource = resourceSet.createResource(realInstallation ? INSTALLATION_SETUP_URI : INSTALLATION_SETUP_FILE_NAME_URI);
      }
      else
      {
        installationResource.unload();
      }

      installation = createInstallation();
      installationResource.getContents().add(installation);

      save = mode == Mode.CREATE_AND_SAVE;
    }

    if (realInstallation && installation != null && installation.getProductVersion() == null)
    {
      ProductCatalog productCatalog = null;

      Resource indexResource = BaseUtil.loadResourceSafely(resourceSet, INDEX_SETUP_URI);
      Index index = (Index)EcoreUtil.getObjectByType(indexResource.getContents(), SetupPackage.Literals.INDEX);
      if (index != null)
      {
        EList<ProductCatalog> productCatalogs = index.getProductCatalogs();
        if (!productCatalogs.isEmpty())
        {
          productCatalog = productCatalogs.get(0);
        }
      }

      if (productCatalog == null || productCatalog.getProducts().isEmpty() || productCatalog.getProducts().get(0).getVersions().isEmpty())
      {
        Resource selfProductCatalogResource = BaseUtil.loadResourceSafely(resourceSet, URI.createURI("catalog:/self-product-catalog.setup"));
        productCatalog = (ProductCatalog)EcoreUtil.getObjectByType(selfProductCatalogResource.getContents(), SetupPackage.Literals.PRODUCT_CATALOG);
      }

      EList<Product> products = productCatalog.getProducts();
      EList<ProductVersion> versions = products.get(0).getVersions();
      installation.setProductVersion(versions.get(0));

      if (save)
      {
        BaseUtil.saveEObject(installation);
      }
    }

    return installation;
  }

  private static Workspace getWorkspace(ResourceSet resourceSet, boolean realWorkspace, Mode mode)
  {
    Resource workspaceResource = null;
    Workspace workspace = null;
    if (realWorkspace)
    {
      if (resourceSet.getURIConverter().exists(WORKSPACE_SETUP_URI, null))
      {
        workspaceResource = BaseUtil.loadResourceSafely(resourceSet, WORKSPACE_SETUP_URI);
        workspace = (Workspace)EcoreUtil.getObjectByType(workspaceResource.getContents(), SetupPackage.Literals.WORKSPACE);
      }
    }
    else
    {
      workspaceResource = resourceSet.getResource(WORKSPACE_SETUP_FILE_NAME_URI, false);
      if (workspaceResource == null && WORKSPACE_SETUP_URI != null)
      {
        workspaceResource = resourceSet.getResource(WORKSPACE_SETUP_URI, false);
      }

      if (workspaceResource != null)
      {
        EList<EObject> contents = workspaceResource.getContents();
        if (contents.isEmpty())
        {
          // If it's not in the resource, and we're not going to create it and save it, then make sure it exists.
          if (mode == Mode.NONE)
          {
            workspace = createWorkspace();
            contents.add(workspace);
          }

        }
        else
        {
          workspace = (Workspace)workspaceResource.getContents().get(0);
        }
      }
    }

    if (workspace == null && mode != Mode.NONE)
    {
      if (workspaceResource == null)
      {
        workspaceResource = resourceSet.createResource(realWorkspace ? WORKSPACE_SETUP_URI : WORKSPACE_SETUP_FILE_NAME_URI);
      }
      else
      {
        workspaceResource.unload();
      }

      workspace = createWorkspace();
      workspaceResource.getContents().add(workspace);

      if (mode == Mode.CREATE_AND_SAVE)
      {
        BaseUtil.saveEObject(workspace);
      }
    }

    return workspace;
  }

  private static User getUser(ResourceSet resourceSet, boolean demandCreate)
  {
    Resource userResource = null;
    User user = null;
    if (resourceSet.getURIConverter().exists(USER_SETUP_URI, null))
    {
      userResource = BaseUtil.loadResourceSafely(resourceSet, USER_SETUP_URI);
      user = (User)EcoreUtil.getObjectByType(userResource.getContents(), SetupPackage.Literals.USER);
    }

    if (user == null && demandCreate)
    {
      if (userResource == null)
      {
        userResource = resourceSet.createResource(USER_SETUP_URI);
      }
      else
      {
        userResource.unload();
      }

      user = createUser();
      userResource.getContents().add(user);

      try
      {
        userResource.save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
    }

    return user;
  }

  private static SetupContext proxify(Installation installation, Workspace workspace, User user)
  {
    EcoreUtil.Copier copier = new EcoreUtil.Copier(false, false)
    {
      private static final long serialVersionUID = 1L;

      @Override
      protected void copyContainment(EReference eReference, EObject eObject, EObject copyEObject)
      {
      }

      @Override
      protected void copyProxyURI(EObject eObject, EObject copyEObject)
      {
        ((InternalEObject)copyEObject).eSetProxyURI(EcoreUtil.getURI(eObject));
      }
    };

    Installation installationCopy = (Installation)copier.copy(installation);
    if (installation != null)
    {
      copier.copy(installation.getProductVersion());
    }

    Workspace workspaceCopy = (Workspace)copier.copy(workspace);
    if (workspace != null)
    {
      copier.copyAll(workspace.getStreams());
    }

    User userCopy = (User)copier.copy(user);

    for (Map.Entry<EObject, EObject> entry : new LinkedHashSet<Map.Entry<EObject, EObject>>(copier.entrySet()))
    {
      EObject eObject = entry.getKey();
      EObject copyEObject = entry.getValue();
      for (EObject eContainer = eObject.eContainer(); eContainer != null; eContainer = eContainer.eContainer())
      {
        EObject copyEContainer = copier.get(eContainer);
        if (copyEContainer == null)
        {
          copyEContainer = copier.copy(eContainer);
        }

        @SuppressWarnings("unchecked")
        InternalEList<EObject> list = (InternalEList<EObject>)copyEContainer.eGet(eObject.eContainmentFeature());
        list.addUnique(copyEObject);

        eObject = eContainer;
        copyEObject = copyEContainer;
      }
    }

    copier.copyReferences();

    return new SetupContext(installationCopy, workspaceCopy, userCopy);
  }

  /**
   * @author Eike Stepper
   */
  private static class WorkspaceUtil
  {
    private static URI getStaticWorkspaceLocationURI()
    {
      try
      {
        IWorkspaceRoot workspaceRoot = EcorePlugin.getWorkspaceRoot();
        return URI.createFileURI(workspaceRoot.getLocation().toOSString());
      }
      catch (Throwable throwable)
      {
        return null;
      }
    }
  }
}
