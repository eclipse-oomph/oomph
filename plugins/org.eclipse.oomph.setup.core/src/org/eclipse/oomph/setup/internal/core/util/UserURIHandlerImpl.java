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

import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupCorePlugin;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class UserURIHandlerImpl extends URIHandlerImpl implements URIResolver
{
  private static final String FEATURE_PATTERN_SUFFIX = "='([^']*)'";

  private static final Pattern CLASS_PATTERN = Pattern.compile("class" + FEATURE_PATTERN_SUFFIX);

  public UserURIHandlerImpl()
  {
  }

  @Override
  public boolean canHandle(URI uri)
  {
    return SetupContext.isUserScheme(uri.scheme());
  }

  public URI resolve(URI uri)
  {
    return SetupContext.GLOBAL_SETUPS_LOCATION_URI.appendSegments(uri.segments()).appendFragment(uri.fragment());
  }

  @Override
  public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException
  {
    URI normalizedURI = resolve(uri);
    URIConverter uriConverter = getURIConverter(options);
    if (!uriConverter.exists(normalizedURI, options))
    {
      Resource resource = create(uri, normalizedURI);
      if (resource != null && normalizedURI.lastSegment().equals("${setup.filename}"))
      {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        resource.save(out, options);
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
      }
    }

    return uriConverter.createInputStream(normalizedURI, options);
  }

  @Override
  public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException
  {
    URI normalizedURI = resolve(uri);
    URIConverter uriConverter = getURIConverter(options);
    return uriConverter.createOutputStream(normalizedURI, options);
  }

  @Override
  public void delete(URI uri, Map<?, ?> options) throws IOException
  {
    URI normalizedURI = resolve(uri);
    URIConverter uriConverter = getURIConverter(options);
    uriConverter.delete(normalizedURI, options);
  }

  @Override
  public Map<String, ?> contentDescription(URI uri, Map<?, ?> options) throws IOException
  {
    URI normalizedURI = resolve(uri);
    URIConverter uriConverter = getURIConverter(options);
    if (!uriConverter.exists(normalizedURI, options))
    {
      create(uri, normalizedURI);
    }

    return uriConverter.contentDescription(normalizedURI, options);
  }

  @Override
  public boolean exists(URI uri, Map<?, ?> options)
  {
    URI normalizedURI = resolve(uri);
    URIConverter uriConverter = getURIConverter(options);
    return uriConverter.exists(normalizedURI, options) || create(uri, normalizedURI) != null;
  }

  @Override
  public Map<String, ?> getAttributes(URI uri, Map<?, ?> options)
  {
    URI normalizedURI = resolve(uri);
    URIConverter uriConverter = getURIConverter(options);
    if (!uriConverter.exists(normalizedURI, options))
    {
      create(uri, normalizedURI);
    }

    return uriConverter.getAttributes(normalizedURI, options);
  }

  @Override
  public void setAttributes(URI uri, Map<String, ?> attributes, Map<?, ?> options) throws IOException
  {
    URI normalizedURI = resolve(uri);
    URIConverter uriConverter = getURIConverter(options);
    if (!uriConverter.exists(normalizedURI, options))
    {
      create(uri, normalizedURI);
    }

    uriConverter.setAttributes(normalizedURI, attributes, options);
  }

  private Resource create(URI uri, URI normalizedURI)
  {
    String query = uri.query();
    if (query != null)
    {
      Resource resource = SetupCoreUtil.createResourceSet().createResource(normalizedURI);

      String decodedQuery = URI.decode(query);

      Matcher classMatcher = CLASS_PATTERN.matcher(decodedQuery);
      URI classURI = URI.createURI(classMatcher.find() ? classMatcher.group(1) : SetupPackage.eNS_URI + "#//Project");

      EPackage ePackage = EPackage.Registry.INSTANCE.getEPackage(classURI.trimFragment().toString());
      if (ePackage == null)
      {
        throw new IllegalArgumentException("No package registered for " + classURI);
      }

      EObject eObject = ePackage.eResource().getEObject(classURI.fragment());
      if (eObject instanceof EClass)
      {
        EClass eClass = (EClass)eObject;
        EObject instance = EcoreUtil.create(eClass);

        for (EAttribute eAttribute : eClass.getEAllAttributes())
        {
          if (!eAttribute.isMany())
          {
            Pattern pattern = Pattern.compile(eAttribute.getName() + FEATURE_PATTERN_SUFFIX);
            Matcher matcher = pattern.matcher(decodedQuery);
            if (matcher.find())
            {
              String value = matcher.group(1);
              instance.eSet(eAttribute, EcoreUtil.createFromString(eAttribute.getEAttributeType(), value));
            }
          }
        }

        if (instance instanceof ProductCatalog)
        {
          ProductCatalog productCatalog = (ProductCatalog)instance;
          EList<SetupTask> setupTasks = productCatalog.getSetupTasks();
          InstallationTask installationTask = SetupFactory.eINSTANCE.createInstallationTask();
          installationTask.setID("installation");
          setupTasks.add(installationTask);
        }

        resource.getContents().add(instance);
      }
      else
      {
        throw new IllegalArgumentException("No class registered for " + classURI);
      }

      return saveResource(resource);
    }

    if (SetupContext.USER_SETUP_URI.equals(uri))
    {
      User user = SetupContext.createUser();
      Resource resource = SetupCoreUtil.createResourceSet().createResource(normalizedURI);
      resource.getContents().add(user);
      return saveResource(resource);
    }

    return null;
  }

  private static Resource saveResource(Resource resource)
  {
    if (resource.getURI().lastSegment().equals("${setup.filename}"))
    {
      return resource;
    }

    try
    {
      resource.save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
      return resource;
    }
    catch (IOException ex)
    {
      SetupCorePlugin.INSTANCE.log(ex);
      return null;
    }
  }
}
