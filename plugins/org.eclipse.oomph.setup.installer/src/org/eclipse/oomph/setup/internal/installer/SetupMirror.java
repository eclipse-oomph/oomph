/*
 * Copyright (c) 2015 Ed Merks and others.
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Ed Merks
 */
public class SetupMirror implements IApplication
{
  // private static final URI GIT_C_PREFIX = URI.createURI("http://git.eclipse.org/c/");

  public Object start(IApplicationContext context) throws Exception
  {
    // String[] arguments = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);

    Set<String> entryNames = new HashSet<String>();

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

      ZipFile zipFile = null;
      try
      {
        zipFile = new ZipFile(temp);
        for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();)
        {
          ZipEntry zipEntry = entries.nextElement();

          String name = zipEntry.getName();
          entryNames.add(name);

          URI path = URI.createURI(name);
          URI uri = URI.createURI(path.segment(0) + ":" + "//" + path.segment(1));
          for (int i = 2, length = path.segmentCount(); i < length; ++i)
          {
            uri = uri.appendSegment(path.segment(i));
          }

          URI archiveEntry = URI.createURI("archive:" + URI.createFileURI(file.toString()) + "!/" + path);

          System.err.println("! " + uri + " -> " + archiveEntry);
        }
      }
      finally
      {
        IOUtil.closeSilent(zipFile);
      }
    }

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

      URI normalizedURI = uriConverter.normalize(uri);
      String scheme = normalizedURI.scheme();
      if (normalizedURI.query() == null && ("http".equals(scheme) || "https".equals(scheme)))
      {
        URI path = URI.createURI(scheme);
        path = path.appendSegment(normalizedURI.authority());
        path = path.appendSegments(normalizedURI.segments());
        System.err.println("###" + normalizedURI);
        // URI deresolvedURI = normalizedURI.deresolve(GIT_C_PREFIX, true, true, false);
        // if (deresolvedURI.hasRelativePath())
        //
        System.out.println("Mirroring " + normalizedURI);

        // URI output = deresolvedURI.resolve(outputLocation);
        URI output = path.resolve(outputLocation);

        entryNames.remove(path.toString());

        uriMap.put(uri, output);
        resource.save(options);
      }
      else
      {
        System.out.println("Ignoring  " + normalizedURI);
      }
    }

    for (String entryName : entryNames)
    {
      URI archiveEntry = URI.createURI(outputLocation + entryName);
      uriConverter.delete(archiveEntry, null);
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
