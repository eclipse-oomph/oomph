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
package org.eclipse.oomph.internal.setup.core.util;

import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Factory;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class ProductCatalogURIHandlerImpl extends URIHandlerImpl
{
  private static final Date NOW = new Date();

  private static final URI SELF_PRODUCT_CATALOG_URI = URI.createURI("catalog:/self-product-catalog.setup");

  @Override
  public boolean canHandle(URI uri)
  {
    return "catalog".equals(uri.scheme());
  }

  @Override
  public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException
  {
    if (SELF_PRODUCT_CATALOG_URI.equals(uri))
    {
      class ProductCatalogInput extends InputStream implements URIConverter.Loadable
      {
        private InputStream in;

        public void loadResource(Resource resource) throws IOException
        {
          resource.getContents().add(create());
        }

        @Override
        public int read() throws IOException
        {
          if (in == null)
          {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Resource resource = Resource.Factory.Registry.INSTANCE.getFactory(SELF_PRODUCT_CATALOG_URI).createResource(SELF_PRODUCT_CATALOG_URI);
            resource.getContents().add(create());
            resource.save(out, null);
            in = new ByteArrayInputStream(out.toByteArray());
          }

          return in.read();
        }
      }

      return new ProductCatalogInput();
    }
    else
    {
      throw new IOException("No product catalog for " + uri);
    }
  }

  private ProductCatalog create()
  {
    ProductCatalog productCatalog = SetupFactory.eINSTANCE.createProductCatalog();
    productCatalog.setName("self");
    productCatalog.setLabel("Self Products");
    productCatalog.setDescription("The product catalog for the self product and the empty product");

    InstallationTask installationTask = SetupFactory.eINSTANCE.createInstallationTask();
    installationTask.setID("installation");
    productCatalog.getSetupTasks().add(installationTask);

    {
      Product selfProduct = SetupFactory.eINSTANCE.createProduct();
      selfProduct.setName("product");
      selfProduct.setLabel("Self Product");
      selfProduct.setDescription("The self product");
      productCatalog.getProducts().add(selfProduct);

      VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
      variable.setName("installation.location");
      variable.setValue(SetupContext.PRODUCT_ROOT_LOCATION.toFileString());
      selfProduct.getSetupTasks().add(variable);

      ProductVersion selfProductVersion = SetupFactory.eINSTANCE.createProductVersion();
      selfProductVersion.setName("version");
      selfProductVersion.setLabel("Self Product Version");
      selfProductVersion.setDescription("The self product version");
      selfProduct.getVersions().add(selfProductVersion);

      P2Task selfP2Task = SetupP2Factory.eINSTANCE.createP2Task();
      selfProductVersion.getSetupTasks().add(selfP2Task);

      IProvisioningAgent agent = P2Util.getCurrentProvisioningAgent();
      IProfileRegistry profileRegistry = (IProfileRegistry)agent.getService(IProfileRegistry.class.getName());
      IProfile profile = profileRegistry.getProfile(IProfileRegistry.SELF);
      if (profile != null)
      {
        int xxx; // TODO Use AgentManager

        EList<Requirement> requirements = selfP2Task.getRequirements();
        IQueryResult<IInstallableUnit> query = profile.query(QueryUtil.createIUAnyQuery(), null);
        for (IInstallableUnit iu : P2Util.asIterable(query))
        {
          if ("true".equals(profile.getInstallableUnitProperty(iu, IProfile.PROP_PROFILE_ROOT_IU)))
          {
            Requirement requirement = P2Factory.eINSTANCE.createRequirement();
            Version version = iu.getVersion();
            if (version.isOSGiCompatible())
            {
              org.osgi.framework.Version osgiVersion = new org.osgi.framework.Version(version.toString());
              int major = osgiVersion.getMajor();
              int minor = osgiVersion.getMinor();
              VersionRange versionRange = new VersionRange(Version.createOSGi(major, minor, 0), true, Version.createOSGi(major, minor + 1, 0), false);
              requirement.setVersionRange(versionRange);
            }

            requirement.setID(iu.getId());
            requirements.add(requirement);
          }
        }
      }

      IMetadataRepositoryManager metadataRepositoryManager = (IMetadataRepositoryManager)agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
      java.net.URI[] knownRepositories = metadataRepositoryManager.getKnownRepositories(IRepositoryManager.REPOSITORIES_NON_SYSTEM);
      if (knownRepositories.length > 0)
      {
        EList<Repository> repositories = selfP2Task.getRepositories();
        for (java.net.URI knownRepository : knownRepositories)
        {
          Repository repository = P2Factory.eINSTANCE.createRepository(knownRepository.toString());
          repositories.add(repository);
        }
      }
    }

    {
      Product emptyProduct = SetupFactory.eINSTANCE.createProduct();
      emptyProduct.setName("empty.product");
      emptyProduct.setLabel("Empty Product");
      emptyProduct.setDescription("The empty product");
      productCatalog.getProducts().add(emptyProduct);

      VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
      variable.setName("installation.location");
      variable.setValue("somewhere");
      emptyProduct.getSetupTasks().add(variable);

      ProductVersion emptyProductVersion = SetupFactory.eINSTANCE.createProductVersion();
      emptyProductVersion.setName("version");
      emptyProductVersion.setLabel("Self Empty Product Version");
      emptyProductVersion.setDescription("The self empty product version");
      emptyProduct.getVersions().add(emptyProductVersion);

      P2Task emptyP2Task = SetupP2Factory.eINSTANCE.createP2Task();
      emptyProductVersion.getSetupTasks().add(emptyP2Task);

      Requirement requirement = P2Factory.eINSTANCE.createRequirement("something");
      emptyP2Task.getRequirements().add(requirement);

      EList<Repository> repositories = emptyP2Task.getRepositories();
      Repository repository = P2Factory.eINSTANCE.createRepository("http://somewhere");
      repositories.add(repository);
    }

    return productCatalog;
  }

  @Override
  public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException
  {
    throw new IOException("Can't write to " + uri);
  }

  @Override
  public void delete(URI uri, Map<?, ?> options) throws IOException
  {
    throw new IOException("Can't delete " + uri);
  }

  @Override
  public boolean exists(URI uri, Map<?, ?> options)
  {
    return true;
  }

  @Override
  public Map<String, ?> getAttributes(URI uri, Map<?, ?> options)
  {
    Map<String, Object> result = new HashMap<String, Object>();
    Set<String> requestedAttributes = getRequestedAttributes(options);
    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_TIME_STAMP))
    {
      result.put(URIConverter.ATTRIBUTE_TIME_STAMP, NOW);
    }

    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_LENGTH))
    {
      InputStream inputStream = null;
      try
      {
        inputStream = createInputStream(uri, options);
        result.put(URIConverter.ATTRIBUTE_LENGTH, inputStream.available());
      }
      catch (IOException ex)
      {
        IOUtil.close(inputStream);
      }
    }

    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_READ_ONLY))
    {
      result.put(URIConverter.ATTRIBUTE_READ_ONLY, Boolean.TRUE);
    }

    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_HIDDEN))
    {
      result.put(URIConverter.ATTRIBUTE_HIDDEN, Boolean.FALSE);
    }

    if (requestedAttributes == null || requestedAttributes.contains(URIConverter.ATTRIBUTE_DIRECTORY))
    {
      result.put(URIConverter.ATTRIBUTE_DIRECTORY, Boolean.FALSE);
    }

    return result;
  }

  @Override
  public void setAttributes(URI uri, Map<String, ?> attributes, Map<?, ?> options) throws IOException
  {
    throw new IOException("Can't set attributes for " + uri);
  }
}
