/*
 * Copyright (c) 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.core.util;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupCorePlugin;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Factory;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
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
import org.eclipse.osgi.util.NLS;

import org.osgi.framework.Bundle;

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
public class SelfProductCatalogURIHandlerImpl extends URIHandlerImpl
{
  public static final String SELF_PRODUCT_CATALOG_NAME = "self"; //$NON-NLS-1$

  private static final URI SELF_PRODUCT_CATALOG_URI = URI.createURI("catalog:/self-product-catalog.setup"); //$NON-NLS-1$

  private static final String UNKNOWN_VERSION = "0.0.0"; //$NON-NLS-1$

  private static final Date NOW = new Date();

  @Override
  public boolean canHandle(URI uri)
  {
    return "catalog".equals(uri.scheme()); //$NON-NLS-1$
  }

  @Override
  public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException
  {
    if (SELF_PRODUCT_CATALOG_URI.equals(uri))
    {
      class ProductCatalogInput extends InputStream implements URIConverter.Loadable
      {
        private InputStream in;

        @Override
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
            Resource resource = SetupCoreUtil.RESOURCE_FACTORY_REGISTRY.getFactory(SELF_PRODUCT_CATALOG_URI).createResource(SELF_PRODUCT_CATALOG_URI);
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
      throw new IOException(NLS.bind(Messages.SelfProductCatalogURIHandlerImpl_NoProductCatalog_exception, uri));
    }
  }

  private ProductCatalog create()
  {
    ProductCatalog productCatalog = SetupFactory.eINSTANCE.createProductCatalog();
    productCatalog.setName(SELF_PRODUCT_CATALOG_NAME);
    productCatalog.setLabel(Messages.SelfProductCatalogURIHandlerImpl_SelfProductsCatalog_label);
    productCatalog.setDescription(Messages.SelfProductCatalogURIHandlerImpl_SelfProductCatalog_description);

    InstallationTask installationTask = SetupFactory.eINSTANCE.createInstallationTask();
    installationTask.setID("installation"); //$NON-NLS-1$
    productCatalog.getSetupTasks().add(installationTask);

    {
      Product selfProduct = SetupFactory.eINSTANCE.createProduct();
      selfProduct.setName("product"); //$NON-NLS-1$
      selfProduct.setLabel(Messages.SelfProductCatalogURIHandlerImpl_SefProduct_label);
      selfProduct.setDescription(Messages.SelfProductCatalogURIHandlerImpl_SelfProduct_description);
      productCatalog.getProducts().add(selfProduct);

      VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
      variable.setName("installation.location"); //$NON-NLS-1$
      variable.setValue(SetupContext.PRODUCT_ROOT_LOCATION.toFileString());
      selfProduct.getSetupTasks().add(variable);

      ProductVersion selfProductVersion = SetupFactory.eINSTANCE.createProductVersion();
      selfProductVersion.setName("version"); //$NON-NLS-1$
      selfProductVersion.setLabel(UNKNOWN_VERSION);
      selfProductVersion.setDescription(Messages.SelfProductCatalogURIHandlerImpl_SelfProductVersion_descripition);
      selfProduct.getVersions().add(selfProductVersion);

      try
      {
        IProduct product = Platform.getProduct();
        if (product != null)
        {
          String name = product.getProperty("aboutText"); //$NON-NLS-1$
          if (name != null && name.startsWith("Eclipse")) //$NON-NLS-1$
          {
            name = name.replaceAll("[\n\r]+.*", ""); //$NON-NLS-1$ //$NON-NLS-2$
          }

          if (StringUtil.isEmpty(name))
          {
            name = product.getName();
          }

          if (!StringUtil.isEmpty(name))
          {
            selfProduct.setLabel(name);
          }

          Bundle bundle = product.getDefiningBundle();
          if (bundle != null)
          {
            selfProductVersion.setLabel(bundle.getVersion().toString());
          }
        }
      }
      catch (Throwable ex)
      {
        //$FALL-THROUGH$
      }

      Annotation annotation = BaseFactory.eINSTANCE.createAnnotation();
      annotation.setSource(AnnotationConstants.ANNOTATION_BRANDING_INFO);

      String folderName = SetupContext.PRODUCT_LOCATION.segmentCount() == 0 ? "" //$NON-NLS-1$
          : URI.decode(SetupContext.PRODUCT_LOCATION.segment(SetupContext.PRODUCT_ROOT_LOCATION.segmentCount()));
      annotation.getDetails().put(AnnotationConstants.KEY_FOLDER_NAME, folderName);
      selfProductVersion.getAnnotations().add(annotation);

      P2Task selfP2Task = SetupP2Factory.eINSTANCE.createP2Task();
      selfProductVersion.getSetupTasks().add(selfP2Task);

      try
      {
        IProvisioningAgent agent = P2Util.getCurrentProvisioningAgent();
        IProfileRegistry profileRegistry = (IProfileRegistry)agent.getService(IProfileRegistry.class.getName());
        IProfile profile = profileRegistry.getProfile(IProfileRegistry.SELF);
        if (profile != null)
        {
          EList<Requirement> requirements = selfP2Task.getRequirements();

          IQueryResult<IInstallableUnit> query = profile.query(QueryUtil.createIUAnyQuery(), null);
          for (IInstallableUnit iu : P2Util.asIterable(query))
          {
            if ("true".equals(profile.getInstallableUnitProperty(iu, IProfile.PROP_PROFILE_ROOT_IU))) //$NON-NLS-1$
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

              requirement.setName(iu.getId());
              requirement.setMatchExpression(iu.getFilter());
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
      catch (Throwable throwable)
      {
        SetupCorePlugin.INSTANCE.log(throwable, IStatus.WARNING);
      }
    }

    {
      Product emptyProduct = SetupFactory.eINSTANCE.createProduct();
      emptyProduct.setName("empty.product"); //$NON-NLS-1$
      emptyProduct.setLabel(Messages.SelfProductCatalogURIHandlerImpl_EmptyProduct_label);
      emptyProduct.setDescription(Messages.SelfProductCatalogURIHandlerImpl_EmptyProduct_description);
      productCatalog.getProducts().add(emptyProduct);

      ProductVersion emptyProductVersion = SetupFactory.eINSTANCE.createProductVersion();
      emptyProductVersion.setName("version"); //$NON-NLS-1$
      emptyProductVersion.setLabel(UNKNOWN_VERSION);
      emptyProductVersion.setDescription(Messages.SelfProductCatalogURIHandlerImpl_EmptyProductVersion_description);
      emptyProduct.getVersions().add(emptyProductVersion);
    }

    return productCatalog;
  }

  @Override
  public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException
  {
    throw new IOException(NLS.bind(Messages.SelfProductCatalogURIHandlerImpl_CannotWrite_exception, uri));
  }

  @Override
  public void delete(URI uri, Map<?, ?> options) throws IOException
  {
    throw new IOException(NLS.bind(Messages.SelfProductCatalogURIHandlerImpl_CannotDelete_exception, uri));
  }

  @Override
  public boolean exists(URI uri, Map<?, ?> options)
  {
    return true;
  }

  @Override
  public Map<String, ?> getAttributes(URI uri, Map<?, ?> options)
  {
    Map<String, Object> result = new HashMap<>();
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
    throw new IOException(NLS.bind(Messages.SelfProductCatalogURIHandlerImpl_CannotSetAttributes_exception, uri));
  }
}
