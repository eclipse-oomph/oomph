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
package org.eclipse.oomph.base.util;

import org.eclipse.oomph.internal.base.BasePlugin;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class BaseUtil
{
  private BaseUtil()
  {
  }

  public static BaseResource loadResourceSafely(ResourceSet resourceSet, URI uri)
  {
    try
    {
      return (BaseResource)resourceSet.getResource(uri, true);
    }
    catch (Throwable ex)
    {
      BasePlugin.INSTANCE.log(ex);
      return (BaseResource)resourceSet.getResource(uri, false);
    }
  }

  public static void saveEObject(EObject eObject)
  {
    try
    {
      XMLResource xmlResource = (XMLResource)eObject.eResource();
      xmlResource.save(null);
    }
    catch (IOException ex)
    {
      BasePlugin.INSTANCE.log(ex);
    }
  }

  public static URI getRootURI(EObject eObject)
  {
    if (eObject.eIsProxy())
    {
      return ((InternalEObject)eObject).eProxyURI();
    }
    else
    {
      EObject rootContainer = EcoreUtil.getRootContainer(eObject);
      URI uri = EcoreUtil.getURI(rootContainer);
      String relativeURIFragmentPath = EcoreUtil.getRelativeURIFragmentPath(rootContainer, eObject);
      if (relativeURIFragmentPath.length() != 0)
      {
        uri = uri.trimFragment().appendFragment(uri.fragment() + "/" + relativeURIFragmentPath);
      }

      return uri;
    }
  }

  public static EStructuralFeature getFeature(EClass eClass, String xmlName)
  {
    for (EStructuralFeature eStructuralFeature : eClass.getEAllStructuralFeatures())
    {
      if (xmlName.equals(ExtendedMetaData.INSTANCE.getName(eStructuralFeature)))
      {
        return eStructuralFeature;
      }
    }

    return null;
  }

  private static InputStream openInputStream(URIConverter uriConverter, Map<?, ?> options, URI uri) throws IORuntimeException
  {
    try
    {
      return uriConverter.createInputStream(uri, options);
    }
    catch (IOException ex)
    {
      throw new IORuntimeException();
    }
  }

  private static OutputStream openOutputStream(URIConverter uriConverter, Map<?, ?> options, URI uri) throws IORuntimeException
  {
    try
    {
      return uriConverter.createOutputStream(uri, options);
    }
    catch (IOException ex)
    {
      throw new IORuntimeException();
    }
  }

  public static void copyFile(URIConverter uriConverter, Map<?, ?> options, URI source, URI target) throws IORuntimeException
  {
    InputStream input = null;
    OutputStream output = null;

    try
    {
      input = openInputStream(uriConverter, options, source);
      output = openOutputStream(uriConverter, options, target);
      IOUtil.copy(input, output);
    }
    finally
    {
      IOUtil.closeSilent(input);
      IOUtil.closeSilent(output);
    }
  }

  public static byte[] readFile(URIConverter uriConverter, Map<?, ?> options, URI uri) throws IORuntimeException
  {
    InputStream input = openInputStream(uriConverter, options, uri);

    try
    {
      ByteArrayOutputStream output = new ByteArrayOutputStream(input.available());
      IOUtil.copy(input, output);
      return output.toByteArray();
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
    finally
    {
      IOUtil.closeSilent(input);
    }
  }

  public static void writeFile(URIConverter uriConverter, Map<?, ?> options, URI uri, byte[] bytes) throws IORuntimeException
  {
    OutputStream output = openOutputStream(uriConverter, options, uri);

    try
    {
      ByteArrayInputStream input = new ByteArrayInputStream(bytes);
      IOUtil.copy(input, output);
    }
    finally
    {
      IOUtil.closeSilent(output);
    }
  }

  public static void deleteFile(URIConverter uriConverter, Map<?, ?> options, URI uri) throws IORuntimeException
  {
    try
    {
      uriConverter.delete(uri, options);
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
  }
}
