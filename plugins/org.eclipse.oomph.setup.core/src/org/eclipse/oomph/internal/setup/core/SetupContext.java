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
package org.eclipse.oomph.internal.setup.core;

import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class SetupContext
{
  private static final String OOMPH_NODE = "org.eclipse.oomph.setup";

  // Basic locations

  public static final URI CONFIGURATION_LOCATION_URI = getStaticConfigurationLocation();

  public static final URI WORKSPACE_LOCATION_URI = CommonPlugin.IS_RESOURCES_BUNDLE_AVAILABLE ? WorkspaceUtil.getStaticWorkspaceLocationURI() : null;

  public static final URI PRODUCT_LOCATION = getStaticInstallLocation();

  public static final URI PRODUCT_ROOT_LOCATION = PRODUCT_LOCATION.trimSegments(1);

  // State locations

  public static final URI GLOBAL_STATE_LOCATION_URI = URI.createFileURI(PropertiesUtil.USER_HOME).appendSegments(new String[] { ".eclipse", OOMPH_NODE });

  public static final URI GLOBAL_SETUPS_LOCATION_URI = GLOBAL_STATE_LOCATION_URI.appendSegment("setups");

  public static final URI CONFIGURATION_STATE_LOCATION_URI = CONFIGURATION_LOCATION_URI.appendSegment(OOMPH_NODE);

  public static final URI WORKSPACE_STATE_LOCATION_URI = WORKSPACE_LOCATION_URI == null ? null : WORKSPACE_LOCATION_URI.appendSegments(new String[] {
      ".metadata", ".plugins", OOMPH_NODE });

  // Resoure locations

  public static final URI SETUP_LOG_URI = CONFIGURATION_STATE_LOCATION_URI.appendSegment("setup.log");

  public static final URI INDEX_SETUP_URI = URI.createURI("index:/org.eclipse.setup");

  public static final URI INDEX_SETUP_LOCATION_URI = URI.createURI("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/org.eclipse.setup");

  public static final URI INSTALLATION_SETUP_FILE_NAME_URI = URI.createURI("installation.setup");

  public static final URI INSTALLATION_SETUP_URI = CONFIGURATION_STATE_LOCATION_URI.appendSegment(INSTALLATION_SETUP_FILE_NAME_URI.lastSegment());

  public static final URI WORKSPACE_SETUP_FILE_NAME_URI = URI.createURI("workspace.setup");

  public static final URI WORKSPACE_SETUP_URI = WORKSPACE_STATE_LOCATION_URI == null ? null : WORKSPACE_STATE_LOCATION_URI
      .appendSegment(WORKSPACE_SETUP_FILE_NAME_URI.lastSegment());

  public static final URI USER_SETUP_URI = GLOBAL_SETUPS_LOCATION_URI.appendSegment("user.setup");

  public static final URI CATALOG_SELECTION_SETUP_URI = GLOBAL_SETUPS_LOCATION_URI.appendSegment("catalogs.setup");

  private static volatile SetupContext self = new SetupContext();

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
    Installation installation = getInstallation(resourceSet, true, false);
    Workspace workspace = getWorkspace(resourceSet, true, false);
    User user = getUser(resourceSet, false);
    return createSelf(installation, workspace, user);
  }

  public static SetupContext create(ResourceSet resourceSet)
  {
    return new SetupContext(getInstallation(resourceSet, true, true), getWorkspace(resourceSet, true, true), getUser(resourceSet, true));
  }

  public static SetupContext createUserOnly(ResourceSet resourceSet)
  {
    return new SetupContext(null, null, getUser(resourceSet, true));
  }

  public static SetupContext create(ResourceSet resourceSet, ProductVersion productVersion)
  {
    Installation installation = getInstallation(resourceSet, false, true);
    installation.setProductVersion(productVersion);
    return new SetupContext(installation, getWorkspace(resourceSet, false, false), getUser(resourceSet, true));
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
    Workspace workspace = createWorkspace();
    workspace.getStreams().addAll(streams);
    Resource workspaceResource = installation.eResource().getResourceSet().createResource(WORKSPACE_SETUP_FILE_NAME_URI);
    workspaceResource.getContents().add(workspace);

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

  private static URI getStaticInstallLocation()
  {
    try
    {
      if (!Platform.isRunning())
      {
        return URI.createFileURI(File.createTempFile("installation", "").toString());
      }

      Location location = Platform.getInstallLocation();
      URI result = URI.createURI(FileLocator.resolve(location.getURL()).toString());
      return result.hasTrailingPathSeparator() ? result.trimSegments(1) : result;
    }
    catch (IOException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  private static URI getStaticConfigurationLocation()
  {
    try
    {
      if (!Platform.isRunning())
      {
        return URI.createFileURI(File.createTempFile("configuration", "").toString());
      }

      Location location = Platform.getConfigurationLocation();
      URI result = URI.createURI(FileLocator.resolve(location.getURL()).toString());
      return result.hasTrailingPathSeparator() ? result.trimSegments(1) : result;
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
  }

  private static Installation getInstallation(ResourceSet resourceSet, boolean realInstallation, boolean demandCreate)
  {
    Installation installation = null;
    Resource installationResource = null;
    if (realInstallation)
    {
      if (resourceSet.getURIConverter().exists(INSTALLATION_SETUP_URI, null))
      {
        installationResource = EMFUtil.loadResourceSafely(resourceSet, INSTALLATION_SETUP_URI);
        installation = (Installation)EcoreUtil.getObjectByType(installationResource.getContents(), SetupPackage.Literals.INSTALLATION);
      }
    }
    else
    {
      installationResource = resourceSet.getResource(INSTALLATION_SETUP_FILE_NAME_URI, false);
      if (installationResource != null)
      {
        installation = (Installation)installationResource.getContents().get(0);
      }
    }

    if (installation == null && demandCreate)
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
    }

    if (realInstallation && installation != null && installation.getProductVersion() == null)
    {
      Resource indexResource = EMFUtil.loadResourceSafely(resourceSet, INDEX_SETUP_URI);
      Index index = (Index)EcoreUtil.getObjectByType(indexResource.getContents(), SetupPackage.Literals.INDEX);
      if (index == null)
      {
        return null;
      }

      EList<ProductCatalog> productCatalogs = index.getProductCatalogs();
      if (productCatalogs.isEmpty())
      {
        return null;
      }

      ProductCatalog productCatalog = productCatalogs.get(0);
      EList<Product> products = productCatalog.getProducts();
      if (products.isEmpty())
      {
        return null;
      }

      Product product = products.get(0);
      EList<ProductVersion> versions = product.getVersions();
      if (versions.isEmpty())
      {
        return null;
      }

      installation.setProductVersion(versions.get(0));
    }

    return installation;
  }

  private static Workspace getWorkspace(ResourceSet resourceSet, boolean realWorkspace, boolean demandCreate)
  {
    Resource workspaceResource = null;
    Workspace workspace = null;
    if (realWorkspace)
    {
      if (resourceSet.getURIConverter().exists(WORKSPACE_SETUP_URI, null))
      {
        workspaceResource = EMFUtil.loadResourceSafely(resourceSet, WORKSPACE_SETUP_URI);
        workspace = (Workspace)EcoreUtil.getObjectByType(workspaceResource.getContents(), SetupPackage.Literals.WORKSPACE);
      }
    }
    else
    {
      workspaceResource = resourceSet.getResource(WORKSPACE_SETUP_FILE_NAME_URI, false);
      if (workspaceResource != null)
      {
        workspace = (Workspace)workspaceResource.getContents().get(0);
      }
    }

    if (workspace == null && demandCreate)
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
    }

    return workspace;
  }

  private static User getUser(ResourceSet resourceSet, boolean demandCreate)
  {
    Resource userResource = null;
    User user = null;
    if (resourceSet.getURIConverter().exists(USER_SETUP_URI, null))
    {
      userResource = EMFUtil.loadResourceSafely(resourceSet, USER_SETUP_URI);
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
        userResource.save(null);
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
    }

    return user;
  }

  private static User createUser()
  {
    User user = SetupFactory.eINSTANCE.createUser();
    user.setName(PropertiesUtil.getProperty("user.name", "user"));
    return user;
  }

  private static Workspace createWorkspace()
  {
    Workspace workspace = SetupFactory.eINSTANCE.createWorkspace();
    workspace.setName("workspace");
    return workspace;
  }

  private static Installation createInstallation()
  {
    Installation installation = SetupFactory.eINSTANCE.createInstallation();
    installation.setName("installation");
    return installation;
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
      IWorkspaceRoot workspaceRoot = EcorePlugin.getWorkspaceRoot();
      return URI.createFileURI(workspaceRoot.getLocation().toOSString());
    }
  }
}
