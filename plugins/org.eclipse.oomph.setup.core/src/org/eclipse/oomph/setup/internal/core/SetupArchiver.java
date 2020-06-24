/*
 * Copyright (c) 2016, 2017 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.core;

import org.eclipse.oomph.base.util.EAnnotations;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.CacheHandling;
import org.eclipse.oomph.setup.internal.core.util.ResourceMirror;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xml.type.AnyType;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.osgi.util.NLS;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
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

    // The default target file is the cache location of the local setup archive.
    final ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
    final URIConverter uriConverter = resourceSet.getURIConverter();

    for (ListIterator<URIHandler> it = uriConverter.getURIHandlers().listIterator(); it.hasNext();)
    {
      // Create a delegating handling for ECFURIHandler...
      // The GITC is serving bytes that randomly have trailing garbage.
      final URIHandler uriHandler = it.next();
      if (uriHandler instanceof ECFURIHandlerImpl)
      {
        it.set(new URIHandler()
        {
          public void setAttributes(URI uri, Map<String, ?> attributes, Map<?, ?> options) throws IOException
          {
            uriHandler.setAttributes(uri, attributes, options);
          }

          public Map<String, ?> getAttributes(URI uri, Map<?, ?> options)
          {
            return uriHandler.getAttributes(uri, options);
          }

          public boolean exists(URI uri, Map<?, ?> options)
          {
            return uriHandler.exists(uri, options);
          }

          public void delete(URI uri, Map<?, ?> options) throws IOException
          {
            uriHandler.delete(uri, options);
          }

          public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException
          {
            return uriHandler.createOutputStream(uri, options);
          }

          public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException
          {
            InputStream result = uriHandler.createInputStream(uri, options);
            try
            {
              // Copy the bytes out of the stream.
              ByteArrayOutputStream initialOut = new ByteArrayOutputStream();
              IOUtil.copy(result, initialOut);
              result.close();
              byte[] initialBytes = initialOut.toByteArray();

              // Create yet another stream.
              result = uriHandler.createInputStream(uri, options);

              // Read this one too, and check if the bytes are the same.
              ByteArrayOutputStream secondaryOut = new ByteArrayOutputStream();
              IOUtil.copy(result, secondaryOut);
              result.close();
              byte[] secondaryBytes = secondaryOut.toByteArray();
              if (Arrays.equals(initialBytes, secondaryBytes))
              {
                // If so we can return a stream for those bytes.
                return new ByteArrayInputStream(initialBytes);
              }
              else
              {
                // If not, we fail early so we don't even try to load the resource.
                // This way we don't end up with a resource with what's likely to be bad contents.
                // At least for XML parsing fails, but with images, we can't check if the image is valid.
                throw new IOException(NLS.bind(Messages.SetupArchiver_InconsistentResults_exception, uri));
              }
            }
            catch (IORuntimeException ex)
            {
              throw new IOException(ex);
            }
          }

          public Map<String, ?> contentDescription(URI uri, Map<?, ?> options) throws IOException
          {
            return uriHandler.contentDescription(uri, options);
          }

          public boolean canHandle(URI uri)
          {
            return uriHandler.canHandle(uri);
          }
        });
      }
    }

    URI archiveLocation = uriConverter.normalize(SetupContext.INDEX_SETUP_ARCHIVE_LOCATION_URI);
    File file = new File(ECFURIHandlerImpl.getCacheFile(archiveLocation).toFileString());

    Set<URI> uris = new LinkedHashSet<URI>();
    uris.add(SetupContext.INDEX_SETUP_URI);

    boolean expectURIs = false;
    for (int i = 0; i < arguments.length; ++i)
    {
      String argument = arguments[i];
      if (argument.startsWith("-")) //$NON-NLS-1$
      {
        expectURIs = false;
      }

      if (expectURIs)
      {
        uris.add(URI.createURI(argument));
      }
      else if ("-target".equals(argument)) //$NON-NLS-1$
      {
        file = new File(arguments[++i]);
      }
      else if ("-uris".equals(argument)) //$NON-NLS-1$
      {
        expectURIs = true;
      }
    }

    String url = file.getAbsolutePath();
    if (url.startsWith("/home/data/httpd/")) //$NON-NLS-1$
    {
      url = "http://" + url.substring("/home/data/httpd/".length()); //$NON-NLS-1$ //$NON-NLS-2$
      System.out.println();
      System.out.println("--> " + url); //$NON-NLS-1$
      System.out.println();
    }

    Set<String> entryNames = new HashSet<String>();
    long lastModified = file.lastModified();
    File temp = new File(file.toString() + ".tmp"); //$NON-NLS-1$
    URI outputLocation;

    if (lastModified == 0)
    {
      outputLocation = URI.createURI("archive:" + URI.createFileURI(file.toString()) + "!/"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    else
    {
      IOUtil.copyFile(file, temp);

      if (!temp.setLastModified(lastModified))
      {
        throw new IORuntimeException(NLS.bind(Messages.SetupArchiver_CouldNotSetTimestamp_exception, temp));
      }

      outputLocation = URI.createURI("archive:" + URI.createFileURI(temp.toString()) + "!/"); //$NON-NLS-1$ //$NON-NLS-2$

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
          URI uri = URI.createURI(path.segment(0) + ":" + "//" + path.segment(1)); //$NON-NLS-1$ //$NON-NLS-2$
          for (int i = 2, length = path.segmentCount(); i < length; ++i)
          {
            uri = uri.appendSegment(path.segment(i));
          }

          URI archiveEntry = URI.createURI("archive:" + URI.createFileURI(file.toString()) + "!/" + path); //$NON-NLS-1$ //$NON-NLS-2$

          System.out.println(NLS.bind(Messages.SetupArchiver_PreviouslyMirrored_message, uri, archiveEntry));
        }
      }
      catch (IOException ex)
      {
        if (!file.delete())
        {
          throw new IORuntimeException(NLS.bind(Messages.SetupArchiver_CouldNotDeleteBadVersion_exception, file));
        }

        lastModified = 0;
        outputLocation = URI.createURI("archive:" + URI.createFileURI(file.toString()) + "!/"); //$NON-NLS-1$ //$NON-NLS-2$
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

    resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, CacheHandling.CACHE_IGNORE);

    ResourceMirror resourceMirror = new ResourceMirror.WithProductImages(resourceSet)
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

    resourceMirror.perform(uris);
    resourceMirror.dispose();
    EcoreUtil.resolveAll(resourceSet);

    ECFURIHandlerImpl.clearExpectedETags();

    Map<URI, URI> uriMap = uriConverter.getURIMap();
    Map<Object, Object> options = new HashMap<Object, Object>();
    if (lastModified != 0)
    {
      options.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
    }

    options.put(Resource.OPTION_LINE_DELIMITER, "\n"); //$NON-NLS-1$

    // Remove any folder redirections that might be in place for the location of the setups folder and folders under that.
    for (Iterator<URI> it = uriMap.keySet().iterator(); it.hasNext();)
    {
      URI uri = it.next();
      URI deresolvedURI = uri.deresolve(SetupContext.INDEX_ROOT_LOCATION_URI);
      if (deresolvedURI.isRelative())
      {
        it.remove();
      }
    }

    uriMap.remove(SetupContext.INDEX_ROOT_LOCATION_URI);

    // If Ecore models fail to load correct, the org.eclipse.setup will resolve the package proxies incorrectly and will look changed.
    // We don't want that, so terminate early.
    boolean hasEcoreFailures = false;
    for (Resource resource : resourceSet.getResources())
    {
      URI uri = resource.getURI();
      URI normalizedURI = uriConverter.normalize(uri);
      if ("ecore".equals(uri.fileExtension()) && (resource.getContents().isEmpty() || !resource.getErrors().isEmpty())) //$NON-NLS-1$
      {
        System.err.println(NLS.bind(Messages.SetupArchiver_FailedToLoad_message, normalizedURI));
        printDiagnostics(resource.getErrors());
        System.err.println(Messages.SetupArchiver_Aborting_message);
        hasEcoreFailures = true;
        break;
      }
    }

    if (!hasEcoreFailures)
    {
      boolean hasFailures = false;
      for (Resource resource : resourceSet.getResources())
      {
        URI uri = resource.getURI();

        URI normalizedURI = uriConverter.normalize(uri);
        String scheme = normalizedURI.scheme();
        if (normalizedURI.query() == null && ("http".equals(scheme) || "https".equals(scheme))) //$NON-NLS-1$ //$NON-NLS-2$
        {
          URI path = URI.createURI(scheme);
          path = path.appendSegment(normalizedURI.authority());
          path = path.appendSegments(normalizedURI.segments());
          System.out.println(NLS.bind(Messages.SetupArchiver_Mirroring_message, normalizedURI));

          URI output = path.resolve(outputLocation);
          entryNames.remove(path.toString());
          uriMap.put(uri, output);

          if (resource.getContents().isEmpty() || !resource.getErrors().isEmpty())
          {
            System.err.println(NLS.bind(Messages.SetupArchiver_FailedToLoad_message, normalizedURI));
            printDiagnostics(resource.getErrors());
            hasFailures = true;
          }
          else if (hasUnrecongizedXMLContent(resource))
          {
            System.err.println(NLS.bind(Messages.SetupArchiver_FailedToLoadProperly_message, normalizedURI));
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
                System.err.println(NLS.bind(Messages.SetupArchiver_Changed_message, normalizedURI));
              }
            }
            catch (IOException ex)
            {
              System.err.println(NLS.bind(Messages.SetupArchiver_FailedToSave_message, normalizedURI));
              ex.printStackTrace();
            }
          }
        }
        else
        {
          System.out.println(NLS.bind(Messages.SetupArchiver_Ignoring_message, normalizedURI));
        }
      }

      if (hasFailures)
      {
        System.err.println(Messages.SetupArchiver_Failures_message);
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
    }

    long finalLastModified = lastModified == 0 ? file.lastModified() : temp.lastModified();
    if (lastModified != finalLastModified)
    {
      if (OS.INSTANCE.isWin())
      {
        if (lastModified != 0 && !file.delete())
        {
          System.err.println(NLS.bind(Messages.SetupArchiver_CouldNotDelete_message, file));
        }
      }

      if (lastModified == 0)
      {
        if (isDamaged(file))
        {
          System.err.println(NLS.bind(Messages.SetupArchiver_ArchiveDamaged_message, file));
          file.delete();
        }
        else
        {
          System.out.println(NLS.bind(Messages.SetupArchiver_SuccessfullyCreated_message, file));
        }
      }
      else if (isDamaged(temp))
      {
        System.err.println(NLS.bind(Messages.SetupArchiver_ArchiveDamagedKeepOld_message, temp));
        temp.delete();
      }
      else
      {
        File backup = new File(file.getParentFile(), file.getName() + ".bak"); //$NON-NLS-1$
        try
        {
          IOUtil.copyFile(temp, backup);
        }
        catch (Throwable throwable)
        {
          System.err.println(NLS.bind(Messages.SetupArchiver_CouldNotBackUp_message, backup));
        }

        if (temp.renameTo(file))
        {
          System.out.println(NLS.bind(Messages.SetupArchiver_SuccessfulUpdates_message, file));
        }
        else
        {
          System.err.println(NLS.bind(Messages.SetupArchiver_CouldNotRename_message, temp, file));
        }
      }
    }
    else
    {
      System.out.println(NLS.bind(Messages.SetupArchiver_NoUpdates_message, file));
      if (!temp.delete())
      {
        System.err.println(NLS.bind(Messages.SetupArchiver_CouldNotDelete_message, temp));
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

  private boolean hasUnrecongizedXMLContent(Resource resource)
  {
    if (resource instanceof XMLResource)
    {
      Map<EObject, AnyType> eObjectToExtensionMap = ((XMLResource)resource).getEObjectToExtensionMap();
      for (AnyType anyType : eObjectToExtensionMap.values())
      {
        FeatureMap any = anyType.getAny();
        if (!any.isEmpty())
        {
          return true;
        }
      }
    }

    return false;
  }

  private void printDiagnostics(List<Resource.Diagnostic> diagnostics)
  {
    for (Resource.Diagnostic diagnostic : diagnostics)
    {
      System.err.println(NLS.bind(Messages.SetupArchiver_DiagnosticError_message,
          new Object[] { diagnostic.getMessage(), diagnostic.getLine(), diagnostic.getLine(), diagnostic.getColumn() }));
    }
  }

  public void stop()
  {
  }
}
