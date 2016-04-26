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

import org.eclipse.oomph.base.util.EAnnotations;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.CacheHandling;
import org.eclipse.oomph.setup.internal.core.util.ResourceMirror;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.util.IORuntimeException;
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
import java.io.IOException;
import java.io.InputStream;
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
public class SetupArchiver implements IApplication
{
  public Object start(IApplicationContext context)
  {
    String[] arguments = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);

    File file = new File(System.getProperty("java.io.tmpdir"), "setups.zip");

    for (int i = 0; i < arguments.length; ++i)
    {
      if ("-target".equals(arguments[i]))
      {
        file = new File(arguments[++i]);
      }
    }

    String url = file.getAbsolutePath();
    if (url.startsWith("/home/data/httpd/"))
    {
      url = "http://" + url.substring("/home/data/httpd/".length());
      System.out.println();
      System.out.println("--> " + url);
    }

    System.out.println();
    System.out.println("http.proxyHost = " + System.getProperty("http.proxyHost", ""));
    System.out.println("http.proxyPort = " + System.getProperty("http.proxyPort", ""));
    System.out.println("http.nonProxyHosts = " + System.getProperty("http.nonProxyHosts", ""));
    System.out.println();
    System.out.println("https.proxyHost = " + System.getProperty("https.proxyHost", ""));
    System.out.println("https.proxyPort = " + System.getProperty("https.proxyPort", ""));
    System.out.println("https.nonProxyHosts = " + System.getProperty("https.nonProxyHosts", ""));
    System.out.println();
    System.out.println("ftp.proxyHost = " + System.getProperty("ftp.proxyHost", ""));
    System.out.println("ftp.proxyPort = " + System.getProperty("ftp.proxyPort", ""));
    System.out.println("ftp.nonProxyHosts = " + System.getProperty("ftp.nonProxyHosts", ""));
    System.out.println();

    Set<String> entryNames = new HashSet<String>();
    long lastModified = file.lastModified();
    File temp = new File(file.toString() + ".tmp");
    URI outputLocation;

    if (lastModified == 0)
    {
      outputLocation = URI.createURI("archive:" + URI.createFileURI(file.toString()) + "!/");
    }
    else
    {
      IOUtil.copyFile(file, temp);

      if (!temp.setLastModified(lastModified))
      {
        throw new IORuntimeException("Could not set timestamp of " + temp);
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

          System.out.println("Previously mirrored " + uri + " -> " + archiveEntry);

          // if (path.toString().contains("SmartHome"))
          // {
          // ByteArrayOutputStream out = new ByteArrayOutputStream();
          //
          // ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
          // Resource resource = resourceSet.getResource(archiveEntry, true);
          // resource.save(out, null);
          //
          // System.out.println();
          // System.out.println(new String(out.toByteArray()));
          // System.out.println();
          // }
        }
      }
      catch (IOException ex)
      {
        if (!file.delete())
        {
          throw new IORuntimeException("Could delete bad version of " + file);
        }

        lastModified = 0;
        outputLocation = URI.createURI("archive:" + URI.createFileURI(file.toString()) + "!/");
      }
      finally
      {
        try
        {
          if (zipFile != null)
          {
            zipFile.close();
          }
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }
      }
    }

    final ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
    resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, CacheHandling.CACHE_IGNORE);

    ResourceMirror resourceMirror = new SetupWizard.IndexLoader.ResourceMirrorWithProductImages(resourceSet)
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
            if (imageURI != null && resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().containsKey(imageURI.fileExtension()))
            {
              schedule(imageURI, true);
            }
          }
        }

        super.visit(eObject);
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

    // Remove any folder redirection that might be in place for the location of the setups folder.
    uriMap.remove(SetupContext.INDEX_SETUP_LOCATION_URI.trimSegments(1).appendSegment(""));

    boolean hasFailures = false;
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
        System.out.println("Mirroring " + normalizedURI);

        URI output = path.resolve(outputLocation);
        entryNames.remove(path.toString());
        uriMap.put(uri, output);

        if (resource.getContents().isEmpty())
        {
          System.err.println("FAILED to load " + normalizedURI);
          hasFailures = true;
        }
        else
        {
          try
          {
            long before = resource.getTimeStamp();
            resource.save(options);
            long after = resource.getTimeStamp();

            if (after - before > 0)
            {
              System.err.println("CHANGED! " + normalizedURI);
            }
          }
          catch (IOException ex)
          {
            System.err.println("FAILED to save " + normalizedURI);
            ex.printStackTrace();
          }
        }
      }
      else
      {
        System.out.println("Ignoring  " + normalizedURI);
      }
    }

    if (hasFailures)
    {
      System.err.println("There were failures so no entries will be deleted from the archive");
    }
    else
    {
      for (String entryName : entryNames)
      {
        URI archiveEntry = URI.createURI(outputLocation + entryName);
        try
        {
          uriConverter.delete(archiveEntry, null);
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }
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
        if (isDamaged(file))
        {
          System.err.println("The resulting archive is damaged. Deleting " + file);
          file.delete();
        }
        else
        {
          System.out.println("Successfully created " + file);
        }
      }
      else if (isDamaged(temp))
      {
        System.err.println("The resulting archive is damaged so the old one will be retained. Deleting " + file);
        temp.delete();
      }
      else
      {
        File backup = new File(file.getParentFile(), file.getName() + ".bak");
        try
        {
          IOUtil.copyFile(temp, backup);
        }
        catch (Throwable throwable)
        {
          System.err.println("Could not create backup " + backup);
        }

        if (temp.renameTo(file))
        {
          System.out.println("Successful updates for " + file);
        }
        else
        {
          System.err.println("Could not rename " + temp + " to " + file);
        }
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

  private boolean isDamaged(File file)
  {
    if (file == null || !file.exists())
    {
      return true;
    }

    if (file.isFile())
    {
      ZipFile zipFile = null;

      try
      {
        zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        if (!entries.hasMoreElements())
        {
          return true;
        }

        do
        {
          ZipEntry entry = entries.nextElement();

          entry.getName();
          entry.getCompressedSize();
          entry.getCrc();

          InputStream inputStream = null;

          try
          {
            inputStream = zipFile.getInputStream(entry);
            if (inputStream == null)
            {
              return true;
            }
          }
          finally
          {
            IOUtil.close(inputStream);
          }
        } while (entries.hasMoreElements());
      }
      catch (Exception ex)
      {
        return true;
      }
      finally
      {
        try
        {
          if (zipFile != null)
          {
            zipFile.close();
          }
        }
        catch (IOException ex)
        {
          throw new IORuntimeException(ex);
        }
      }
    }

    return false;
  }

  public void stop()
  {
  }
}
