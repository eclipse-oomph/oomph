/*
 * Copyright (c) 201t Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.base.util.BytesResourceFactoryImpl;
import org.eclipse.oomph.base.util.EAnnotations;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.ResourceMirror;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ed Merks
 */
public class SetupMirror implements IApplication
{
  private static final URI GIT_C_PREFIX = URI.createURI("http://git.eclipse.org/c/");

  public Object start(IApplicationContext context) throws Exception
  {
    // String[] arguments = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);

    ResourceSet resourceSet = SetupCoreUtil.createResourceSet();

    BytesResourceFactoryImpl bytesResourceFactory = new BytesResourceFactoryImpl();
    Map<String, Object> extensionToFactoryMap = resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap();
    extensionToFactoryMap.put("gif", bytesResourceFactory);
    extensionToFactoryMap.put("png", bytesResourceFactory);
    extensionToFactoryMap.put("jpeg", bytesResourceFactory);
    extensionToFactoryMap.put("jpg", bytesResourceFactory);

    ResourceMirror resourceMirror = new ResourceMirror(resourceSet)
    {
      @Override
      protected void visit(EObject eObject)
      {
        if (eObject instanceof EClass)
        {
          EClass eClass = (EClass)eObject;
          if (!eClass.isAbstract())
          {
            final URI imageURI = EAnnotations.getImageURI(eClass);
            if (imageURI != null)
            {
              schedule(imageURI, true);
            }
          }
        }
      }
    };
    resourceMirror.perform(SetupContext.INDEX_SETUP_URI);
    resourceMirror.dispose();
    EcoreUtil.resolveAll(resourceSet);

    File file = new File(System.getProperty("java.io.tmpdir"), "setups.zip");
    long lastModified = file.lastModified();
    File temp = new File(file.toString() + ".tmp");
    URI outputLocation;
    if (lastModified == 0)
    {
      outputLocation = URI.createURI("archive:" + URI.createFileURI(file.toString()) + "!/");
    }
    else
    {
      try
      {
        IOUtil.copyFile(file, temp);
      }
      catch (Throwable throwable)
      {
        throwable.printStackTrace();
      }

      if (!temp.setLastModified(lastModified))
      {
        System.err.println("Count not set timestamp of " + temp);
      }

      outputLocation = URI.createURI("archive:" + URI.createFileURI(temp.toString()) + "!/");
    }

    ECFURIHandlerImpl.clearExpectedETags();

    final URIConverter uriConverter = resourceSet.getURIConverter();
    Map<URI, URI> uriMap = uriConverter.getURIMap();
    Map<Object, Object> options = new HashMap<Object, Object>();
    if (lastModified != 0)
    {
      options.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
      // options.put(Resource.OPTION_LINE_DELIMITER, "\n");
    }

    for (Resource resource : resourceSet.getResources())
    {
      URI uri = resource.getURI();

      /*
       * if ("ecore".equals(uri.fileExtension())) { EPackage ePackage = (EPackage)EcoreUtil.getObjectByType(resource.getContents(),
       * EcorePackage.Literals.EPACKAGE); for (EClassifier eClassifier : ePackage.getEClassifiers()) { if (eClassifier instanceof EClass) { EClass eClass =
       * (EClass)eClassifier; if (!eClass.isAbstract()) { final URI imageURI = EAnnotations.getImageURI(eClass); if (imageURI != null) { System.err.println("##"
       * + imageURI); URI normalizedURI = uriConverter.normalize(imageURI); URI deresolvedURI = normalizedURI.deresolve(GIT_C_PREFIX, true, true, false); if
       * (deresolvedURI.hasRelativePath()) { URI output = deresolvedURI.resolve(outputLocation); Resource imageResource = new ResourceImpl(output) {
       * @Override protected URIConverter getURIConverter() { return uriConverter; }
       * @Override protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException { InputStream inputStream = null; try { inputStream =
       * uriConverter.createInputStream(imageURI); IOUtil.copy(inputStream, outputStream); } finally { IOUtil.closeSilent(inputStream); } } };
       * imageResource.save(options); } } } } } }
       */

      URI normalizedURI = uriConverter.normalize(uri);
      URI deresolvedURI = normalizedURI.deresolve(GIT_C_PREFIX, true, true, false);
      if (deresolvedURI.hasRelativePath())
      {
        System.out.println("Mirroring " + normalizedURI);

        URI output = deresolvedURI.resolve(outputLocation);
        uriMap.put(normalizedURI, output);
        resource.save(options);
      }
      else
      {
        System.out.println("Ignoring  " + normalizedURI);
      }
    }

    long finalLastModified = lastModified == 0 ? file.lastModified() : temp.lastModified();

    if (lastModified != finalLastModified)
    {
      if (OS.INSTANCE.isWin())
      {
        if (lastModified != 0 && !file.delete())
        {
          System.err.println("Could not delete " + file);
        }
      }

      if (lastModified == 0)
      {
        System.out.println("Successfully created " + file);
      }
      else if (temp.renameTo(file))
      {
        System.out.println("Successful updates for " + file);
      }
      else
      {
        System.err.println("Could not rename " + temp + " to " + file);
      }
    }
    else
    {
      System.out.println("No updates for " + file);
      if (!temp.delete())
      {
        System.err.println("Could not delete " + temp);
      }
    }

    return null;
  }

  public void stop()
  {
  }

}
