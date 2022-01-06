/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.sync;

import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.util.HexUtil;
import org.eclipse.oomph.util.IOExceptionWithCause;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Eike Stepper
 */
public final class SyncUtil
{
  private SyncUtil()
  {
  }

  public static ResourceSet createResourceSet()
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new BaseResourceFactoryImpl()); //$NON-NLS-1$
    return resourceSet;
  }

  public static void inititalizeFile(File file, EClass eClass, ResourceSet resourceSet)
  {
    if (file.length() == 0)
    {
      EObject rootObject = EcoreUtil.create(eClass);

      Resource resource = resourceSet.createResource(org.eclipse.emf.common.util.URI.createFileURI(file.getAbsolutePath()));
      resource.getContents().add(rootObject);
      BaseUtil.saveEObject(rootObject);
    }
  }

  public static String getDigest(File file) throws IOException
  {
    if (!file.exists())
    {
      return null;
    }

    FileInputStream contents = null;

    try
    {
      contents = new FileInputStream(file);
      return HexUtil.bytesToHex(IOUtil.getSHA1(contents));
    }
    catch (IOException ex)
    {
      throw ex;
    }
    catch (NoSuchAlgorithmException ex)
    {
      throw new IOExceptionWithCause(ex);
    }
    finally
    {
      IOUtil.closeSilent(contents);
    }
  }

  public static void deleteFile(File file) throws IOException
  {
    if (file.isFile())
    {
      if (!file.delete())
      {
        throw new IOException(NLS.bind(Messages.SyncUtil_CouldNotDelete_exception, file));
      }
    }
  }
}
