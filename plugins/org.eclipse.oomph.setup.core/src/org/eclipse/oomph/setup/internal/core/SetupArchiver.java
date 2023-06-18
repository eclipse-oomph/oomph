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

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.base.util.BytesResourceFactoryImpl;
import org.eclipse.oomph.base.util.EAnnotations;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.Configuration;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.CacheHandling;
import org.eclipse.oomph.setup.internal.core.util.ResourceMirror;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;
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
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Ed Merks
 */
public class SetupArchiver implements IApplication
{
  private static final URI GITHUB_MODELS = URI.createURI("https://raw.githubusercontent.com/eclipse-oomph/oomph/master/setups/models/"); //$NON-NLS-1$

  private static final URI HTTPS_LEGACY_MODELS = URI.createURI("https://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/"); //$NON-NLS-1$

  private static final URI HTTP_LEGACY_MODELS = URI.createURI("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/"); //$NON-NLS-1$

  @Override
  public Object start(IApplicationContext context)
  {
    String[] arguments = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);

    // The default target file is the cache location of the local setup archive.
    final ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
    final URIConverter uriConverter = resourceSet.getURIConverter();

    final Map<URI, URI> configurationImages = new ConcurrentHashMap<>();

    EList<URIHandler> uriHandlers = uriConverter.getURIHandlers();
    uriHandlers.add(0, new URIHandlerImpl()
    {
      @Override
      public boolean canHandle(URI uri)
      {
        return configurationImages.containsKey(uri);
      }

      @Override
      public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException
      {
        return super.createInputStream(configurationImages.get(uri), options);
      }
    });

    for (ListIterator<URIHandler> it = uriHandlers.listIterator(); it.hasNext();)
    {
      // Create a delegating handling for ECFURIHandler...
      // The GITC is serving bytes that randomly have trailing garbage.
      final URIHandler uriHandler = it.next();
      if (uriHandler instanceof ECFURIHandlerImpl)
      {
        it.set(new URIHandler()
        {
          @Override
          public void setAttributes(URI uri, Map<String, ?> attributes, Map<?, ?> options) throws IOException
          {
            uriHandler.setAttributes(uri, attributes, options);
          }

          @Override
          public Map<String, ?> getAttributes(URI uri, Map<?, ?> options)
          {
            return uriHandler.getAttributes(uri, options);
          }

          @Override
          public boolean exists(URI uri, Map<?, ?> options)
          {
            return uriHandler.exists(uri, options);
          }

          @Override
          public void delete(URI uri, Map<?, ?> options) throws IOException
          {
            uriHandler.delete(uri, options);
          }

          @Override
          public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException
          {
            return uriHandler.createOutputStream(uri, options);
          }

          @Override
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

          @Override
          public Map<String, ?> contentDescription(URI uri, Map<?, ?> options) throws IOException
          {
            return uriHandler.contentDescription(uri, options);
          }

          @Override
          public boolean canHandle(URI uri)
          {
            return uriHandler.canHandle(uri);
          }
        });
      }
    }

    URI archiveLocation = uriConverter.normalize(SetupContext.INDEX_SETUP_ARCHIVE_LOCATION_URI);
    File file = new File(ECFURIHandlerImpl.getCacheFile(archiveLocation).toFileString());

    Set<URI> uris = new LinkedHashSet<>();
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
      url = "https://" + url.substring("/home/data/httpd/".length()); //$NON-NLS-1$ //$NON-NLS-2$
      System.out.println();
      System.out.println("--> " + url); //$NON-NLS-1$
      System.out.println();
    }

    Set<String> entryNames = new HashSet<>();
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

    CacheHandling cacheHandling = CacheHandling.valueOf(System.getProperty(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, CacheHandling.CACHE_IGNORE.toString()));
    resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, cacheHandling);
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("svg", new BytesResourceFactoryImpl()); //$NON-NLS-1$

    Map<URI, URI> uriMap = uriConverter.getURIMap();
    uriMap.put(HTTPS_LEGACY_MODELS, GITHUB_MODELS);
    uriMap.put(HTTP_LEGACY_MODELS, GITHUB_MODELS);

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

        if (eObject instanceof Configuration)
        {
          Configuration configuration = (Configuration)eObject;
          String badgeLabel = BaseUtil.getAnnotation(configuration, AnnotationConstants.ANNOTATION_BRANDING_INFO, AnnotationConstants.KEY_BADGE_LABEL);
          if (!StringUtil.isEmpty(badgeLabel))
          {
            schedule(badgeLabel, badgeLabel);
          }
          else
          {
            Workspace workspace = configuration.getWorkspace();
            if (workspace != null)
            {
              List<String> projects = new UniqueEList<>();
              for (Stream stream : workspace.getStreams())
              {
                String proxyURI = EcoreUtil.getURI(stream).toString();
                Matcher matcher = Pattern.compile("/@projects\\[name='([^']+)'\\]").matcher(proxyURI); //$NON-NLS-1$
                if (matcher.find())
                {
                  projects.add(URI.decode(matcher.group(1)));
                }
              }

              if (!projects.isEmpty())
              {
                StringBuilder compositeLabel = new StringBuilder();
                StringBuilder compositeFileName = new StringBuilder();
                for (String project : projects)
                {
                  if (compositeLabel.length() != 0)
                  {
                    compositeLabel.append(" + "); //$NON-NLS-1$
                    compositeFileName.append("_"); //$NON-NLS-1$
                  }

                  compositeLabel.append(project);
                  compositeFileName.append(project);

                  if (projects.size() > 1)
                  {
                    schedule(project, project);
                  }
                }

                schedule(compositeFileName.toString(), compositeLabel.toString());
              }
            }
          }
        }

        super.visit(eObject);
      }

      private void schedule(String fileName, String label)
      {
        URI svgURI = URI.createURI("svg:/" + IOUtil.encodeFileName(fileName).replace(' ', '_') + ".svg"); //$NON-NLS-1$ //$NON-NLS-2$
        URI imageURI = URI.createURI(SetupArchiver.this.getConfigurationImage(label));
        configurationImages.put(svgURI, imageURI);
        schedule(svgURI);
      }
    };

    resourceMirror.perform(uris);
    resourceMirror.dispose();
    List<Resource> resources = resourceSet.getResources();
    for (int i = 0; i < resources.size(); ++i)
    {
      for (EObject eObject : resources.get(i).getContents())
      {
        try
        {
          eObject.eContainer();
        }
        catch (RuntimeException ex)
        {
          System.err.println("Unresolved proxy " + ((InternalEObject)eObject).eInternalContainer()); //$NON-NLS-1$
        }

        resolveAll(eObject);
      }
    }

    ECFURIHandlerImpl.clearExpectedETags();

    Map<Object, Object> options = new HashMap<>();
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

    // If Ecore models fail to load correctly, the org.eclipse.setup will resolve the package proxies incorrectly and will look changed.
    // We don't want that, so terminate early in that case.
    boolean hasEcoreFailures = false;

    for (Resource resource : resourceSet.getResources())
    {
      URI uri = resource.getURI();
      if ("ecore".equals(uri.fileExtension())) //$NON-NLS-1$
      {
        URI normalizedURI = uriConverter.normalize(uri);
        if (resource.getContents().isEmpty() || !resource.getErrors().isEmpty())
        {
          System.err.println(NLS.bind(Messages.SetupArchiver_FailedToLoad_message, normalizedURI));
          printDiagnostics(resource.getErrors());
          System.err.println(Messages.SetupArchiver_Aborting_message);
          hasEcoreFailures = true;
          break;
        }

        // Ensure that the model resources consistently use their actual location (the schemaLocation annotation of the model), not index:/...
        // It appears that someone has manually changed their schemaLocations to use https leading to non-deterministic changes to the setups.zip.
        EPackage ePackage = (EPackage)EcoreUtil.getObjectByType(resource.getContents(), EcorePackage.Literals.EPACKAGE);
        if (ePackage != null)
        {
          String schemaLocation = EcoreUtil.getAnnotation(ePackage, EcorePackage.eNS_URI, "schemaLocation"); //$NON-NLS-1$
          if (schemaLocation != null)
          {
            URI schemLocationURI = URI.createURI(schemaLocation);
            if (uriConverter.normalize(schemLocationURI).equals(normalizedURI))
            {
              URI newSchemaLocation = schemLocationURI.replacePrefix(HTTP_LEGACY_MODELS, GITHUB_MODELS);
              if (newSchemaLocation != null)
              {
                schemLocationURI = newSchemaLocation;
              }

              resource.setURI(schemLocationURI);
              continue;
            }
          }
        }

        resource.setURI(normalizedURI);
      }
    }

    if (PropertiesUtil.isProperty("org.eclipse.oomph.setup.internal.core.SetupArchiver.traceEcore")) //$NON-NLS-1$
    {
      for (Resource resource : resourceSet.getResources())
      {
        URI uri = resource.getURI();
        URI normalizedURI = uriConverter.normalize(uri);
        if ("ecore".equals(uri.fileExtension()) || "ecore".equals(normalizedURI.fileExtension())) //$NON-NLS-1$ //$NON-NLS-2$
        {
          System.out.println("Model: " + uri + " -> " + normalizedURI); //$NON-NLS-1$ //$NON-NLS-2$
        }
      }
    }

    if (!hasEcoreFailures)
    {
      boolean hasFailures = false;

      Map<Resource, URI> legacyResources = new LinkedHashMap<Resource, URI>();

      for (Resource resource : resourceSet.getResources())
      {
        URI uri = resource.getURI();

        URI normalizedURI = uriConverter.normalize(uri);
        String scheme = normalizedURI.scheme();
        if (normalizedURI.query() == null && ("http".equals(scheme) || "https".equals(scheme) || "svg".equals(scheme))) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        {
          URI path = URI.createURI(scheme);
          if (normalizedURI.hasAuthority())
          {
            path = path.appendSegment(normalizedURI.authority());
          }
          path = path.appendSegments(normalizedURI.segments());
          System.out.println(NLS.bind(Messages.SetupArchiver_Mirroring_message, normalizedURI));

          URI output = path.resolve(outputLocation);
          entryNames.remove(path.toString());
          URI old = uriMap.put(uri, output);

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
              URI legacyURI = normalizedURI.replacePrefix(SetupContext.INDEX_ROOT_LOCATION_URI, SetupContext.LEGACY_INDEX_ROOT_LOCATION_URI);
              if (legacyURI != null)
              {
                // Prepare to serialize all the resources hosted by Oomph's setups to the legacy URI as well.
                legacyResources.put(resource, legacyURI);
              }

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

          uriMap.put(uri, old);
        }
        else
        {
          System.out.println(NLS.bind(Messages.SetupArchiver_Ignoring_message, normalizedURI));
        }
      }

      for (Map.Entry<Resource, URI> entry : legacyResources.entrySet())
      {
        URI legacyURI = entry.getValue();
        if (!"ecore".equals(legacyURI.fileExtension())) //$NON-NLS-1$
        {
          URI indexURI = legacyURI.replacePrefix(SetupContext.LEGACY_INDEX_ROOT_LOCATION_URI, SetupContext.INDEX_ROOT_URI);
          uriMap.put(indexURI, legacyURI);
          entry.getKey().setURI(indexURI);
        }
      }

      for (Map.Entry<Resource, URI> entry : legacyResources.entrySet())
      {
        Resource resource = entry.getKey();
        URI uri = entry.getValue();
        String scheme = uri.scheme();
        URI path = URI.createURI(scheme);
        path = path.appendSegment(uri.authority());
        path = path.appendSegments(uri.segments());
        System.out.println(NLS.bind(Messages.SetupArchiver_MirroringLegacy_message, uri));

        URI output = path.resolve(outputLocation);
        entryNames.remove(path.toString());
        uriMap.put(resource.getURI(), output);
        uriMap.put(uri, output);

        try
        {
          long before = resource.getTimeStamp();
          resource.save(options);
          long after = resource.getTimeStamp();

          if (after - before > 0)
          {
            System.err.println(NLS.bind(Messages.SetupArchiver_Changed_message, uri));
          }
        }
        catch (IOException ex)
        {
          System.err.println(NLS.bind(Messages.SetupArchiver_FailedToSave_message, uri));
          ex.printStackTrace();
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

  @Override
  public void stop()
  {
  }

  private void resolveAll(EObject eObject)
  {
    resolveAll(eObject.eCrossReferences(), false);
    resolveAll(eObject.eContents(), true);
  }

  private void resolveAll(EList<EObject> references, boolean recursive)
  {
    for (Iterator<EObject> it2 = references.iterator(), it3 = ((InternalEList<EObject>)references).basicIterator(); it3.hasNext();)
    {
      EObject reference = it3.next();
      try
      {
        EObject resolvedReference = it2.next();
        if (recursive)
        {
          resolveAll(resolvedReference);
        }
      }
      catch (RuntimeException ex)
      {
        System.err.println("Unresolved proxy " + reference); //$NON-NLS-1$
      }
    }
  }

  String getConfigurationImage(String label)
  {
    String encodedLabel = URI.encodeQuery(StringUtil.safe(label), false).toString().replace("+", "%2B").replace("&", "%26"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    return "https://img.shields.io/static/v1?logo=eclipseide&label=Create%20Development%20Environment&message=" + encodedLabel //$NON-NLS-1$
        + "&style=for-the-badge&logoColor=white&labelColor=darkorange&color=gray"; //$NON-NLS-1$
  }
}
