/*
 * Copyright (c) 2019 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.junit.JUnitFactory;
import org.eclipse.oomph.junit.JUnitPackage;
import org.eclipse.oomph.junit.ProblemType;
import org.eclipse.oomph.junit.TestCaseType;
import org.eclipse.oomph.junit.TestSuite;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.internal.core.RepositoryIntegrityAnalyzer.InstallableUnitWriter.ValueHandler;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.XMLUtil;
import org.eclipse.oomph.util.ZIPUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.URIConverter.ReadableInputStream;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.XMLOptions;
import org.eclipse.emf.ecore.xmi.XMLParserPool;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLSave;
import org.eclipse.emf.ecore.xmi.impl.GenericXMLResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLOptionsImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLSaveImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLString;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.XMLTypeDocumentRoot;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.internal.p2.artifact.repository.CompositeArtifactRepository;
import org.eclipse.equinox.internal.p2.artifact.repository.simple.SimpleArtifactRepository;
import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.internal.p2.metadata.RequiredCapability;
import org.eclipse.equinox.internal.p2.metadata.repository.CompositeMetadataRepository;
import org.eclipse.equinox.internal.p2.metadata.repository.io.MetadataWriter;
import org.eclipse.equinox.internal.p2.metadata.repository.io.XMLConstants;
import org.eclipse.equinox.internal.p2.persistence.CompositeRepositoryIO;
import org.eclipse.equinox.internal.p2.persistence.CompositeRepositoryState;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.ILicense;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.ICompositeRepository;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactDescriptor;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IProcessingStepDescriptor;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.equinox.spi.p2.publisher.PublisherHelper;
import org.eclipse.osgi.signedcontent.SignedContent;
import org.eclipse.osgi.signedcontent.SignedContentFactory;
import org.eclipse.osgi.signedcontent.SignerInfo;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.security.auth.x500.X500Principal;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringWriter;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ed Merks
 */
@SuppressWarnings({ "restriction", "nls" })
public class RepositoryIntegrityAnalyzer implements IApplication
{
  private static final String DOWNLOAD_ECLIPSE_ORG_AUTHORITY = "download.eclipse.org";

  private static final File DOWNLOAD_ECLIPSE_ORG_FOLDER = new File("/home/data/httpd/" + DOWNLOAD_ECLIPSE_ORG_AUTHORITY);

  private static final boolean DOWNLOAD_ECLIPSE_ORG_FOLDER_EXISTS = DOWNLOAD_ECLIPSE_ORG_FOLDER.exists();

  private static final String DOWNLOAD_ECLIPSE_ORG_FOLDER_URI = DOWNLOAD_ECLIPSE_ORG_FOLDER.toURI().toString();

  private static final String DOWNLOAD_ECLIPSE_ORG_SERVER_URI = "https://" + DOWNLOAD_ECLIPSE_ORG_AUTHORITY + "/";

  private static final String PROCESSED = ".processed";

  private static final int DOWNLOAD_RETRY_COUNT = 3;

  private static final Comparator<IInstallableUnit> NAME_VERSION_COMPARATOR = new Comparator<>()
  {
    private final Comparator<String> comparator = CommonPlugin.INSTANCE.getComparator();

    @Override
    public int compare(IInstallableUnit iu1, IInstallableUnit iu2)
    {
      String name1 = iu1.getProperty(IInstallableUnit.PROP_NAME, null);
      if (name1 == null)
      {
        name1 = iu1.getId();
      }
      String name2 = iu2.getProperty(IInstallableUnit.PROP_NAME, null);
      if (name2 == null)
      {
        name2 = iu2.getId();
      }
      int result = comparator.compare(name1, name2);
      if (result == 0)
      {
        result = iu1.getVersion().compareTo(iu2.getVersion());
      }

      return result;
    }
  };

  private final Map<String, Report.LicenseDetail> details = new LinkedHashMap<>();

  private final Map<URI, Report> reports = new LinkedHashMap<>();

  private final Map<java.net.URI, IMetadataRepository> metadataRepositories = new LinkedHashMap<>();

  private final Map<java.net.URI, IArtifactRepository> artifactRepositories = new LinkedHashMap<>();

  private final Map<URI, byte[]> imageBytes = new HashMap<>();

  private final Map<Object, String> images = new HashMap<>();

  private final Map<File, Future<List<String>>> fileIndices = new TreeMap<>();

  private Agent agent;

  private ExecutorService executor;

  private boolean verbose;

  private boolean aggregator;

  @Override
  public Object start(IApplicationContext context) throws Exception
  {
    try
    {
      String[] arguments = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
      Set<URI> uris = new LinkedHashSet<>();
      File outputLocation = new File(".").getCanonicalFile();
      File publishLocation = null;
      File testsLocation = null;

      String reportSource = "https://ci.eclipse.org/oomph/job/repository-analyzer/";
      String reportBranding = "https://wiki.eclipse.org/images/d/dc/Oomph_Project_Logo.png";
      if (arguments != null)
      {
        for (int i = 0; i < arguments.length; ++i)
        {
          String option = arguments[i];
          if ("-output".equals(option) || "-o".equals(option))
          {
            File file = new File(arguments[++i]);
            outputLocation = file.getCanonicalFile();
          }
          else if ("-publish".equals(option) || "-p".equals(option))
          {
            File file = new File(arguments[++i]);
            publishLocation = file.getCanonicalFile();
          }
          else if ("-source".equals(option) || "-s".equals(option))
          {
            reportSource = arguments[++i];
          }
          else if ("-branding".equals(option) || "-b".equals(option))
          {
            reportBranding = arguments[++i];
          }
          else if ("-verbose".equals(option) || "-v".equals(option))
          {
            verbose = true;
          }
          else if ("-aggregator".equals(option) || "-a".equals(option))
          {
            aggregator = true;
          }
          else if ("-test".equals(option) || "-t".equals(option))
          {
            File file = new File(arguments[++i]);
            testsLocation = file.getCanonicalFile();
          }
          else
          {
            URI uri = URI.createURI(arguments[i]);
            if ("".equals(uri.lastSegment()))
            {
              uri = uri.trimSegments(1);
            }
            uris.add(uri);
          }
        }
      }

      createFolders(outputLocation);

      if (aggregator)
      {
        uris.addAll(loadAggregator(outputLocation));
      }

      CompositeMetadataRepository metadataRepository = CompositeMetadataRepository.createMemoryComposite(getAgent().getProvisioningAgent());
      metadataRepositories.put(metadataRepository.getLocation(), metadataRepository);
      for (URI uri : uris)
      {
        metadataRepository.addChild(toInternalRepositoryLocation(uri));
      }

      CompositeArtifactRepository artifactRepository = CompositeArtifactRepository.createMemoryComposite(getAgent().getProvisioningAgent());
      artifactRepositories.put(metadataRepository.getLocation(), artifactRepository);

      for (URI uri : uris)
      {
        artifactRepository.addChild(toInternalRepositoryLocation(uri));
      }

      File artifactCacheLocation = new File(outputLocation, "artifacts");
      artifactCacheLocation.mkdir();

      RepositoryIndex repositoryIndex = RepositoryIndex.create("\n");
      Set<File> reportLocations = new HashSet<>();
      Map<String, String> allReports = new TreeMap<>();

      Set<File> reportsWithErrors = new HashSet<>();

      if (testsLocation != null)
      {
        testsLocation.mkdirs();
        for (File file : testsLocation.listFiles())
        {
          IOUtil.deleteBestEffort(file);
        }
      }

      Map<IArtifactKey, Future<Map<IArtifactDescriptor, File>>> artifactCache = new LinkedHashMap<>();

      for (URI uri : uris)
      {
        if (verbose)
        {
          System.out.println("Computing report information for '" + uri + "'");
        }

        String relativePath = toRelativePath(uri);
        File reportLocation = new File(outputLocation, relativePath);
        reportLocations.add(reportLocation);

        // Clean it up before creating it.
        IOUtil.deleteBestEffort(reportLocation);
        createFolders(reportLocation);

        Report report = generateReport(null, uri, outputLocation, uri, reportLocation, artifactCache, artifactCacheLocation, reportSource, reportBranding);

        if (verbose)
        {
          System.out.println("Waiting for report information completion for '" + uri + "'");
        }

        shutDownExecutor();

        emitReport(allReports, new HashSet<Report>(), report, reportLocation, testsLocation, repositoryIndex);

        if (publishLocation != null)
        {
          File reportPublishLocation = new File(publishLocation, relativePath);
          publish(reportLocation, reportPublishLocation);

          if (verbose)
          {
            System.out.println("Publish report for '" + uri + "' to '" + reportPublishLocation);
          }
        }

        if (aggregator)
        {
          if (!report.getUnsignedIUs().isEmpty() || !report.getBadProviderIUs().isEmpty() || !report.getBadLicenseIUs().isEmpty()
              || !report.getBrokenBrandingIUs().isEmpty())
          {
            reportsWithErrors.add(reportLocation);
          }
        }

        images.clear();
        reports.clear();
      }

      reportLocations.add(artifactCacheLocation);
      if (aggregator)
      {
        reportLocations.add(new File(outputLocation, "aggrcons"));
      }

      IndexReport indexReport = new IndexReport(null, reportSource, reportBranding, outputLocation, publishLocation, reportLocations, allReports,
          reportsWithErrors);
      generateIndex(indexReport, repositoryIndex, reportSource, reportBranding);
    }
    finally
    {
      shutDownExecutor();
    }

    return null;
  }

  private Set<URI> loadAggregator(File outputLocation) throws IOException, ProvisionException, OperationCanceledException, URISyntaxException
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    final URIConverter uriConverter = resourceSet.getURIConverter();
    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new GenericXMLResourceFactoryImpl());
    URI aggregatorURI = URI.createURI("https://git.eclipse.org/c/simrel/org.eclipse.simrel.build.git/plain/simrel.aggr");
    Resource aggregatorResource = resourceSet.getResource(aggregatorURI, true);
    // aggregatorResource.save(System.out, null);

    Map<URI, Future<InputStream>> aggrcons = new LinkedHashMap<>();
    ExecutorService executor = getExecutor();
    for (Iterator<EObject> it = aggregatorResource.getAllContents(); it.hasNext();)
    {
      EObject eObject = it.next();
      String href = get(eObject, "contributions", "href");
      if (href != null)
      {
        URI uri = URI.createURI(href);
        final URI resolvedURI = uri.resolve(aggregatorURI);
        aggrcons.put(resolvedURI, executor.submit(new Callable<InputStream>()
        {
          @Override
          public InputStream call() throws Exception
          {
            InputStream inputStream = null;
            try
            {
              inputStream = uriConverter.createInputStream(resolvedURI);
              ByteArrayOutputStream output = new ByteArrayOutputStream();
              IOUtil.copy(inputStream, output);
              output.close();
              return new ByteArrayInputStream(output.toByteArray());
            }
            finally
            {
              IOUtil.closeSilent(inputStream);
            }
          }
        }));
      }
    }

    Set<URI> composites = new LinkedHashSet<>();
    LOOP: for (Map.Entry<URI, Future<InputStream>> entry : aggrcons.entrySet())
    {
      URI aggrcon = entry.getKey();
      URI repository = null;
      Resource aggrconResource = resourceSet.createResource(aggrcon);
      try
      {
        aggrconResource.load(entry.getValue().get(), null);
      }
      catch (Exception ex)
      {
      }

      aggrconResource.save(System.out, null);

      Map<URI, Set<Requirement>> requirements = new LinkedHashMap<>();
      Set<String> contacts = new LinkedHashSet<>();
      String label = null;
      for (Iterator<EObject> it = aggrconResource.getAllContents(); it.hasNext();)
      {
        EObject eObject = it.next();
        if (label == null)
        {
          label = get(eObject, "Contribution", "label");
          if ("false".equals(get(eObject, "Contribution", "enabled")))
          {
            System.out.println("Disabled");
            continue LOOP;
          }
        }

        if ("false".equals(get(eObject, "repositories", "enabled")))
        {
          System.out.println("Disabled repository");
          continue LOOP;
        }

        String location = get(eObject, "repositories", "location");
        if (location != null)
        {
          String normalizedLocation = location.replaceAll("/$", "");
          repository = URI
              .createURI(normalizedLocation.contains("//repo.eclipse.org") ? normalizedLocation : normalizedLocation.replaceAll("^https:", "http:"));
        }
        else
        {
          for (String elementName : new String[] { "features", "bundles", "products" })
          {
            String iuID = get(eObject, elementName, "name");
            if (iuID != null)
            {
              String versionRangeLiteral = get(eObject, elementName, "versionRange");
              VersionRange versionRange = null;
              try
              {
                versionRange = VersionRange.create(versionRangeLiteral);
              }
              catch (RuntimeException ex)
              {
                Pattern exactVersionPattern = Pattern.compile("\\[([^,]*)\\]");
                Matcher matcher = exactVersionPattern.matcher(versionRangeLiteral);
                if (matcher.matches())
                {
                  Version version = Version.parseVersion(matcher.group(1));
                  versionRange = new VersionRange(version, true, version, true);
                }
                else
                {
                  throw ex;
                }
              }

              Requirement requirement = P2Factory.eINSTANCE.createRequirement(iuID, versionRange);
              CollectionUtil.add(requirements, repository, requirement);
            }
          }

          String contact = get(eObject, "contacts", "href");
          if (contact != null)
          {
            Pattern emailPattern = Pattern.compile("email='([^']+)'");
            Matcher matcher = emailPattern.matcher(contact);
            if (matcher.find())
            {
              contacts.add(matcher.group(1));
            }
          }
        }
      }

      String compositeName = URI.encodeSegment(aggrcon.trimFragment().toString(), false);
      compositeName = compositeName.replace(":", "%3A");
      composites.add(createAggrconComposite(outputLocation, label, aggrcon.toString().replace("/plain/", "/tree/").replace("#/$", ""), compositeName,
          requirements, contacts));
    }

    return composites;
  }

  private URI createAggrconComposite(File outputLocation, String label, String aggrconHref, String aggrconName, Map<URI, Set<Requirement>> requirements,
      Set<String> contacts) throws ProvisionException, OperationCanceledException, URISyntaxException
  {
    File compositeAggrconLocations = new File(outputLocation, "aggrcons");
    compositeAggrconLocations.mkdirs();
    File compositeAggrconLocation = new File(compositeAggrconLocations, aggrconName);
    IOUtil.deleteBestEffort(compositeAggrconLocation);
    Map<String, String> properties = new LinkedHashMap<>();
    for (Map.Entry<URI, Set<Requirement>> entry : requirements.entrySet())
    {
      URI uri = entry.getKey();
      StringBuilder value = new StringBuilder();
      for (Requirement requirement : entry.getValue())
      {
        if (value.length() != 0)
        {
          value.append(';');
        }

        value.append(requirement.getName());
        VersionRange versionRange = requirement.getVersionRange();
        if (versionRange != null && !versionRange.equals(VersionRange.emptyRange))
        {
          value.append(' ');
          value.append(versionRange);
        }
      }

      properties.put(uri.toString(), value.toString());
    }

    properties.put("contacts", contacts.toString());
    properties.put("aggrcon", aggrconHref);

    java.net.URI locationURI = compositeAggrconLocation.toURI();
    getMetadataRepositoryManager().createRepository(locationURI, label, IMetadataRepositoryManager.TYPE_COMPOSITE_REPOSITORY, properties);

    properties.remove("contacts");
    properties.remove("aggrcon");
    for (Map.Entry<String, String> entry : properties.entrySet())
    {
      entry.setValue("");
    }

    getArtifactRepositoryManager().createRepository(locationURI, label, IArtifactRepositoryManager.TYPE_COMPOSITE_REPOSITORY, properties);

    return URI.createURI(locationURI.toString());
  }

  private String get(EObject eObject, String elementName, String attributeName)
  {
    if (eObject instanceof AnyType)
    {
      AnyType anyType = (AnyType)eObject;
      if (elementName.equals(anyType.eContainmentFeature().getName()))
      {
        FeatureMap anyAttribute = anyType.getAnyAttribute();
        for (FeatureMap.Entry entry : anyAttribute)
        {
          if (attributeName.equals(entry.getEStructuralFeature().getName()))
          {
            return entry.getValue().toString();
          }
        }
      }
    }
    return null;
  }

  private java.net.URI toInternalRepositoryLocation(URI uri) throws URISyntaxException
  {
    if (DOWNLOAD_ECLIPSE_ORG_AUTHORITY.equals(uri.authority()) && DOWNLOAD_ECLIPSE_ORG_FOLDER_EXISTS)
    {
      File file = DOWNLOAD_ECLIPSE_ORG_FOLDER;
      for (String segment : uri.segments())
      {
        file = new File(file, segment);
      }

      boolean exists = file.exists();
      if (verbose)
      {
        System.out.println("Redirecting '" + uri + "' to '" + file + "' success=" + exists);
      }

      if (exists)
      {
        return file.toURI();
      }
    }

    java.net.URI location = new java.net.URI(uri.toString());
    return location;
  }

  private URI toExternalRepositoryLocation(java.net.URI child)
  {
    String childLocation = child.toString();
    String downloadEclipseOrgFolderLocation = DOWNLOAD_ECLIPSE_ORG_FOLDER_URI.toString();
    if (childLocation.startsWith(downloadEclipseOrgFolderLocation))
    {
      URI.createURI(DOWNLOAD_ECLIPSE_ORG_SERVER_URI + childLocation.substring(DOWNLOAD_ECLIPSE_ORG_FOLDER_URI.length()));
    }

    URI uri = URI.createURI(child.toString());
    if ("".equals(uri.lastSegment()))
    {
      uri = uri.trimSegments(1);
    }

    return uri;
  }

  private static File[] listSortedFiles(File folder)
  {
    File[] files = folder.listFiles();
    final Comparator<String> comparator = CommonPlugin.INSTANCE.getComparator();
    Arrays.sort(files, new Comparator<File>()
    {
      @Override
      public int compare(File f1, File f2)
      {
        return comparator.compare(f1.getPath(), f2.getPath());
      }
    });

    return files;
  }

  private static String toRelativePath(URI uri)
  {
    if (isAggrconRepositoryURI(uri))
    {
      String lastSegment = uri.lastSegment();
      String decode = URI.decode(URI.decode(lastSegment));
      return URI.createURI(decode).lastSegment();
    }

    StringBuilder result = new StringBuilder();
    String authority = uri.authority();
    if (!StringUtil.isEmpty(authority))
    {
      result.append(IOUtil.encodeFileName(authority));
    }

    String device = uri.device();
    if (!StringUtil.isEmpty(device))
    {
      if (result.length() != 0)
      {
        result.append("/");
      }

      result.append(IOUtil.encodeFileName(device));
    }

    for (String segment : uri.segments())
    {
      if (!StringUtil.isEmpty(segment))
      {
        if (result.length() != 0)
        {
          result.append("/");
        }

        result.append(IOUtil.encodeFileName(segment));
      }
    }

    return result.toString();
  }

  private static String toTestPackageName(URI uri)
  {
    if (isAggrconRepositoryURI(uri))
    {
      return toTestPackageName(URI.createURI(uri.lastSegment()));
    }

    StringBuilder result = new StringBuilder();
    for (String segment : uri.segments())
    {
      if (!StringUtil.isEmpty(segment))
      {
        if (result.length() != 0)
        {
          result.append(".");
        }

        StringBuilder packageName = new StringBuilder();
        for (char ch : segment.toCharArray())
        {
          if (packageName.length() == 0)
          {
            if (!Character.isJavaIdentifierStart(ch))
            {
              packageName.append('_');
            }
          }

          if (Character.isJavaIdentifierPart(ch))
          {
            packageName.append(ch);
          }
          else
          {
            packageName.append('_');
          }
        }

        result.append(packageName);
      }
    }

    return result.toString();
  }

  private static TestSuite createTestSuite(File testsLocation, String fileName, String qualifiedClassName)
  {
    URI uri = URI.createFileURI(new File(testsLocation, fileName).toString());
    Resource resource = Resource.Factory.Registry.INSTANCE.getFactory(uri, JUnitPackage.eCONTENT_TYPE).createResource(uri);
    TestSuite testSuite = JUnitFactory.eINSTANCE.createTestSuite();
    testSuite.setName(qualifiedClassName);
    testSuite.setProperties(JUnitFactory.eINSTANCE.createPropertiesType());
    testSuite.setTimestamp(System.currentTimeMillis());
    resource.getContents().add(testSuite);
    return testSuite;
  }

  private static TestCaseType createTestCase(TestSuite testSuite, String name)
  {
    TestCaseType testCase = JUnitFactory.eINSTANCE.createTestCaseType();
    testCase.setClassName(testSuite.getName());
    testCase.setName(name);
    testSuite.getTestCases().add(testCase);
    return testCase;
  }

  private static void addFailure(TestCaseType testCase, String message, String value)
  {
    ProblemType problemType = JUnitFactory.eINSTANCE.createProblemType();
    problemType.setMessage(message);
    problemType.setType(RepositoryIntegrityAnalyzer.class.getName());
    problemType.setValue(message + "\n" + value);
    testCase.setFailure(problemType);
  }

  private static void publish(File outputLocation, File publishLocation) throws IOException
  {
    File newPublishLocation = new File(publishLocation.toString() + ".new");
    if (newPublishLocation.exists())
    {
      if (!IOUtil.deleteBestEffort(newPublishLocation))
      {
        throw new IOException("The location '" + newPublishLocation + "' cannot be deleted");
      }
    }

    IOUtil.copyTree(outputLocation, newPublishLocation);

    File oldPublishLocation = null;
    if (publishLocation.exists())
    {
      oldPublishLocation = new File(publishLocation.toString() + ".old");
      if (oldPublishLocation.exists())
      {
        if (!IOUtil.deleteBestEffort(oldPublishLocation))
        {
          throw new IOException("The location '" + oldPublishLocation + "' cannot be deleted");
        }
      }

      if (!publishLocation.renameTo(oldPublishLocation))
      {
        throw new IOException("The location '" + publishLocation + "' cannot be renamed to '" + oldPublishLocation + "'");
      }
    }

    if (!newPublishLocation.renameTo(publishLocation))
    {
      throw new IOException("The location '" + newPublishLocation + "' cannot be renamed to '" + publishLocation + "'");
    }

    if (oldPublishLocation != null)
    {
      IOUtil.deleteBestEffort(oldPublishLocation);
    }
  }

  private static void createFolders(File folder) throws IOException
  {
    if (folder.exists())
    {
      if (!folder.isDirectory())
      {
        throw new IOException("The location '" + folder + "' already exists as a file");
      }
    }
    else
    {
      if (!folder.mkdirs())
      {
        throw new IOException("The location '" + folder + "' cannot be created as a directory");
      }
    }
  }

  private void generateIndex(IndexReport indexReport, RepositoryIndex repositoryIndex, String reportSource, String reportBranding) throws IOException
  {
    if (verbose)
    {
      System.out.println("Generating report index for '" + indexReport.getFolder());
    }

    String result = repositoryIndex.generate(indexReport);
    images.clear();

    OutputStream out = null;
    try
    {
      File outputLocation = indexReport.getFolder();
      File index = new File(outputLocation, "index.html");
      out = new FileOutputStream(index);
      new PrintStream(out, false, "UTF-8").print(result);

      File publishLocation = indexReport.getPublishLocation();
      if (publishLocation != null)
      {
        IOUtil.copyFile(index, new File(publishLocation, "index.html"));
        for (File file : outputLocation.listFiles())
        {
          String name = file.getName();
          if (name.endsWith(".png"))
          {
            IOUtil.copyFile(file, new File(publishLocation, name));
          }
        }
      }
    }
    finally
    {
      IOUtil.closeSilent(out);
    }

    for (IndexReport child : indexReport.getChildren())
    {
      generateIndex(child, repositoryIndex, reportSource, reportBranding);
    }
  }

  private void emitReport(Map<String, String> allReports, Set<Report> visited, Report report, final File outputLocation, File testsLocation,
      final RepositoryIndex repositoryIndexEmitter) throws IOException, InterruptedException
  {
    if (visited.add(report))
    {
      if (verbose)
      {
        System.out.println("Emmitting report for '" + report.getSiteURL() + "'");
      }

      String result = repositoryIndexEmitter.generate(report);
      String relativeIndexURL = report.getRelativeIndexURL();
      if (outputLocation == null)
      {
        System.out.println("---" + relativeIndexURL + "---");
        System.out.println(result);
      }
      else
      {
        OutputStream out = null;
        try
        {
          File reportOutputLocation = new File(outputLocation, relativeIndexURL);
          out = new FileOutputStream(reportOutputLocation);
          allReports.put(report.getSiteURL(), reportOutputLocation.toString());
          new PrintStream(out, false, "UTF-8").print(result);
        }
        finally
        {
          IOUtil.closeSilent(out);
        }
      }

      if (testsLocation != null)
      {
        URI siteURI = URI.createURI(report.getSiteURL());
        String testPackageName = toTestPackageName(siteURI);

        System.err.println("###" + testsLocation);
        System.err.println("###>" + testPackageName);
        boolean simple = report.isSimple();
        TestSuite testSuite;
        if (simple || isAggrconRepositoryURI(siteURI))
        {
          String qualifiedClassName = testPackageName + (simple ? ".Simple" : ".AggrCon");
          testSuite = createTestSuite(testsLocation, "TEST-" + qualifiedClassName + ".xml", qualifiedClassName);

          Set<IInstallableUnit> allIUs = report.getAllIUs();
          Set<IInstallableUnit> badLicenseIUs = report.getBadLicenseIUs();
          Set<IInstallableUnit> badProviderIUs = report.getBadProviderIUs();
          Map<String, Set<IInstallableUnit>> featureProviders = report.getFeatureProviders();
          Set<IInstallableUnit> brokenBrandingIUs = report.getBrokenBrandingIUs();
          Map<List<Certificate>, Map<String, IInstallableUnit>> certificates = report.getCertificates();
          Map<String, IInstallableUnit> unsigned = certificates.get(Collections.emptyList());
          Collection<IInstallableUnit> pluginsWithMissingPackGZ = report.getPluginsWithMissingPackGZ();
          Set<IInstallableUnit> classContainingIUs = report.getClassContainingIUs();
          for (IInstallableUnit iu : allIUs)
          {
            if (iu.getId().endsWith(".feature.group"))
            {
              {
                long start = System.currentTimeMillis();
                TestCaseType testCase = createTestCase(testSuite, "validLicense_" + iu);
                if (badLicenseIUs.contains(iu))
                {
                  addFailure(testCase, "The feature IU '" + iu + "' does not have a valid license", siteURI.toString());
                }
                long end = System.currentTimeMillis();
                testCase.setTime((end - start) / 1000.0);
              }

              {
                long start = System.currentTimeMillis();
                TestCaseType testCase = createTestCase(testSuite, "validProvider_" + iu);
                if (badProviderIUs.contains(iu))
                {
                  StringBuilder message = new StringBuilder();
                  for (Map.Entry<String, Set<IInstallableUnit>> entry : featureProviders.entrySet())
                  {
                    if (entry.getValue().contains(iu))
                    {
                      message.append(entry.getKey()).append('\n');
                      break;
                    }
                  }

                  message.append(siteURI);

                  addFailure(testCase, "The feature IU '" + iu + "' does not valid provider", message.toString());
                }
                long end = System.currentTimeMillis();
                testCase.setTime((end - start) / 1000.0);
              }

              {
                long start = System.currentTimeMillis();
                TestCaseType testCase = createTestCase(testSuite, "validBranding_" + iu);
                if (brokenBrandingIUs.contains(iu))
                {
                  addFailure(testCase, "The feature IU '" + iu + "' has a broken branding image", siteURI.toString());
                }
                long end = System.currentTimeMillis();
                testCase.setTime((end - start) / 1000.0);
              }
            }

            {
              long start = System.currentTimeMillis();
              Map<String, Boolean> iuArtifacts = report.getIUArtifacts(iu);
              if (!iuArtifacts.isEmpty())
              {
                TestCaseType testCase = createTestCase(testSuite, "validSignedArtifacts_" + iu);
                if (unsigned != null && unsigned.containsValue(iu))
                {
                  StringBuilder message = new StringBuilder();
                  for (String artifact : iuArtifacts.keySet())
                  {
                    if (message.length() != 0)
                    {
                      message.append('\n');
                    }

                    message.append(siteURI).append('/').append(artifact);
                  }

                  addFailure(testCase, "The IU '" + iu + "' has unsigned artifacts", message.toString());
                }

                long end = System.currentTimeMillis();
                testCase.setTime((end - start) / 1000.0);
              }
            }

            if (classContainingIUs.contains(iu))
            {
              long start = System.currentTimeMillis();
              TestCaseType testCase = createTestCase(testSuite, "validCorrespondingPackGZ_" + iu);

              if (pluginsWithMissingPackGZ.contains(iu))
              {
                Map<String, Boolean> iuArtifacts = report.getIUArtifacts(iu);
                StringBuilder message = new StringBuilder();
                for (String artifact : iuArtifacts.keySet())
                {
                  if (message.length() != 0)
                  {
                    message.append('\n');
                  }

                  message.append(siteURI).append('/').append(artifact);
                }

                addFailure(testCase, "The plugin IU '" + iu + "' has no corresponding .pack.gz artifact", message.toString());
              }

              long end = System.currentTimeMillis();
              testCase.setTime((end - start) / 1000.0);
            }
          }
        }
        else
        {
          String qualifiedClassName = testPackageName + ".Composite";
          testSuite = createTestSuite(testsLocation, "TEST-" + qualifiedClassName + ".xml", qualifiedClassName);

          {
            long start = System.currentTimeMillis();
            TestCaseType testCase = createTestCase(testSuite, "validMetadataLocations");
            String metadataXML = report.getMetadataXML();
            if (metadataXML != null && metadataXML.contains("bad-absolute-location"))
            {
              addFailure(testCase, "The composite content XML contains an absolute URI referencing the same host", siteURI.toString());
            }
            long end = System.currentTimeMillis();
            testCase.setTime((end - start) / 1000.0);
          }

          {
            long start = System.currentTimeMillis();
            TestCaseType testCase = createTestCase(testSuite, "validArtifactLocations");
            String artifactXML = report.getArtifactML();
            if (artifactXML != null && artifactXML.contains("bad-absolute-location"))
            {
              addFailure(testCase, "The composite artifact XML contains an absolute URI referencing the same host", siteURI.toString());
            }
            long end = System.currentTimeMillis();
            testCase.setTime((end - start) / 1000.0);
          }
        }

        testSuite.summarize();
        Diagnostic diagnostic = Diagnostician.INSTANCE.validate(testSuite);
        if (diagnostic.getSeverity() != Diagnostic.OK)
        {
          System.err.println("###");
        }

        testSuite.eResource().save(null);
      }

      List<IUReport> iuReports = report.getIUReports();
      if (!iuReports.isEmpty())
      {
        URI iuReportFolder = URI.createURI(relativeIndexURL).trimFileExtension();
        new File(outputLocation, iuReportFolder.toString()).mkdir();
        ExecutorService executor = getExecutor();
        List<Future<Void>> futures = new ArrayList<>();
        for (final IUReport iuReport : iuReports)
        {
          futures.add(executor.submit(new Callable<Void>()
          {
            @Override
            public Void call() throws Exception
            {
              String iuResult = repositoryIndexEmitter.generate(iuReport);
              String relativeIUReportURL = iuReport.getRelativeReportURL();
              OutputStream out = null;
              try
              {
                out = new FileOutputStream(new File(outputLocation, relativeIUReportURL));
                new PrintStream(out, false, "UTF-8").print(iuResult);
              }
              finally
              {
                IOUtil.closeSilent(out);
              }
              return null;
            }
          }));
        }

        if (verbose)
        {
          System.out.println("Emmitting report IU details for '" + report.getSiteURL() + "'");
        }

        for (Future<Void> future : futures)
        {
          get(future);
        }
      }

      for (Report childReport : report.getChildren())
      {
        emitReport(allReports, visited, childReport, outputLocation, testsLocation, repositoryIndexEmitter);
      }
    }

  }

  @Override
  public void stop()
  {
  }

  private Report.LicenseDetail getLicenseDetail(ILicense license, boolean demandCreate)
  {
    java.net.URI location = license.getLocation();
    URI locationURI = location == null ? Report.NO_LICENSE_URI : URI.createURI(location.toString());
    String uuid = license.getUUID();
    Report.LicenseDetail licenseDetail = details.get(uuid);
    if (licenseDetail == null && demandCreate)
    {
      licenseDetail = new Report.LicenseDetail(locationURI, license);
      details.put(uuid, licenseDetail);
    }

    return licenseDetail;
  }

  private List<Report.LicenseDetail> getLicenses(IInstallableUnit installableUnit)
  {
    List<Report.LicenseDetail> result = new ArrayList<>();
    Collection<ILicense> licenses = installableUnit.getLicenses(null);
    if (licenses.isEmpty())
    {
      result.add(getLicenseDetail(Report.NO_LICENSE, true));
    }
    else
    {
      for (ILicense license : licenses)
      {
        result.add(getLicenseDetail(license, true));
      }
    }

    return result;
  }

  private Set<IInstallableUnit> query(IMetadataRepository metadataRepository, IQuery<IInstallableUnit> query)
  {
    IQueryResult<IInstallableUnit> queryResult = metadataRepository.query(query, new NullProgressMonitor());
    Set<IInstallableUnit> result = new TreeSet<>();
    for (IInstallableUnit iu : P2Util.asIterable(queryResult))
    {
      result.add(iu);
    }

    return result;
  }

  public Report generateReport(final Report parentReport, final URI rootURI, final File rootOutputLocation, final URI uri, final File outputLocation,
      final Map<IArtifactKey, Future<Map<IArtifactDescriptor, File>>> artifactCache, final File artifactCacheFolder, final String reportSource,
      final String reportBranding) throws Exception
  {
    Report report = reports.get(uri);
    if (report == null)
    {
      reports.put(uri, null);

      final IMetadataRepository metadataRepository = loadMetadataRepository(getMetadataRepositoryManager(), uri);
      IArtifactRepository loadedArtifactRepository;
      try
      {
        loadedArtifactRepository = loadArtifactRepository(getArtifactRepositoryManager(), uri);
      }
      catch (ProvisionException exception)
      {
        loadedArtifactRepository = null;
      }

      final IArtifactRepository artifactRepository = loadedArtifactRepository;

      final Set<IInstallableUnit> allIUs = getAllIUs(metadataRepository);

      if (artifactRepository != null)
      {
        for (IInstallableUnit iu : query(metadataRepository, QueryUtil.createIUAnyQuery()))
        {
          for (IArtifactKey artifactKey : iu.getArtifacts())
          {
            getArtifacts(artifactKey, artifactRepository, artifactCache, artifactCacheFolder);
          }
        }
      }

      final Set<IInstallableUnit> productIUs = new TreeSet<>();
      for (IInstallableUnit iu : allIUs)
      {
        if ("true".equals(iu.getProperty(MetadataFactory.InstallableUnitDescription.PROP_TYPE_PRODUCT)))
        {
          productIUs.add(iu);
        }
      }

      final Map<Report.LicenseDetail, Set<IInstallableUnit>> licenseIUs = new LinkedHashMap<>();
      for (ILicense sua : Report.SUAS)
      {
        licenseIUs.put(getLicenseDetail(sua, true), new LinkedHashSet<IInstallableUnit>());
      }

      final Set<String> expectedDuplicates = new HashSet<>();
      expectedDuplicates.add("a.jre.javase");
      expectedDuplicates.add("config.a.jre.javase");
      for (IInstallableUnit iu : allIUs)
      {
        if ("true".equals(iu.getProperty(MetadataFactory.InstallableUnitDescription.PROP_TYPE_GROUP)))
        {
          CollectionUtil.addAll(licenseIUs, getLicenses(iu), iu);

          Set<String> ids = new HashSet<>();
          for (final IRequirement requirement : iu.getRequirements())
          {
            if (requirement instanceof IRequiredCapability)
            {
              IRequiredCapability requiredCapability = (IRequiredCapability)requirement;
              String namespace = requiredCapability.getNamespace();
              if (IInstallableUnit.NAMESPACE_IU_ID.equals(namespace))
              {
                String name = requiredCapability.getName();
                if (!ids.add(name))
                {
                  expectedDuplicates.add(name);
                }
              }
            }
          }
        }
      }

      final Map<Report.LicenseDetail, Set<IInstallableUnit>> sortedLicenseIUs = new LinkedHashMap<>();
      while (!licenseIUs.isEmpty())
      {
        Map.Entry<Report.LicenseDetail, Set<IInstallableUnit>> biggestEntry = null;
        for (Map.Entry<Report.LicenseDetail, Set<IInstallableUnit>> entry : licenseIUs.entrySet())
        {
          if (biggestEntry == null)
          {
            biggestEntry = entry;
          }
          else if (biggestEntry.getValue().size() < entry.getValue().size())
          {
            biggestEntry = entry;
          }
        }

        licenseIUs.remove(biggestEntry.getKey());

        Set<IInstallableUnit> ius = biggestEntry.getValue();
        if (ius.isEmpty())
        {
          // Omit the ones that are empty.
          break;
        }

        TreeSet<IInstallableUnit> sortedLicenseIUValues = new TreeSet<>(NAME_VERSION_COMPARATOR);
        sortedLicenseIUValues.addAll(biggestEntry.getValue());
        sortedLicenseIUs.put(biggestEntry.getKey(), sortedLicenseIUValues);
      }

      final List<IInstallableUnit> featureIUs = new ArrayList<>();
      for (IInstallableUnit featureIU : allIUs)
      {
        if (featureIU.getId().endsWith(Requirement.FEATURE_SUFFIX))
        {
          featureIUs.add(featureIU);
        }
      }

      Collections.sort(featureIUs, NAME_VERSION_COMPARATOR);

      final Map<IInstallableUnit, Future<URI>> brandingImages = new LinkedHashMap<>();
      if (artifactRepository != null)
      {
        for (IInstallableUnit featureIU : featureIUs)
        {
          getBrandingImage(featureIU, metadataRepository, artifactRepository, brandingImages, artifactCache, artifactCacheFolder);
        }
      }

      Map<IInstallableUnit, Map<File, Future<SignedContent>>> signedContentCache = new LinkedHashMap<>();
      for (IInstallableUnit iu : allIUs)
      {
        getSignedContent(iu, artifactCache, signedContentCache);
      }

      final Map<List<String>, IRequirement> requiredCapabilities = new HashMap<>();
      Map<IRequirement, Future<Set<IInstallableUnit>>> futures = new HashMap<>();
      Map<IRequirement, Set<IInstallableUnit>> requiringIUs = new HashMap<>();
      for (IInstallableUnit iu : allIUs)
      {
        for (final IRequirement requirement : iu.getRequirements())
        {
          CollectionUtil.add(requiringIUs, requirement, iu);

          Future<Set<IInstallableUnit>> future = futures.get(requirement);
          if (future == null)
          {
            IMatchExpression<IInstallableUnit> match = requirement.getMatches();
            if (RequiredCapability.isVersionRangeRequirement(match))
            {
              List<String> key = new ArrayList<>(3);
              key.add(RequiredCapability.extractNamespace(match));
              key.add(RequiredCapability.extractName(match));
              key.add(RequiredCapability.extractRange(match).toString());
              requiredCapabilities.put(key, requirement);
            }

            futures.put(requirement, getExecutor().submit(new Callable<Set<IInstallableUnit>>()
            {
              @Override
              public Set<IInstallableUnit> call() throws Exception
              {
                return query(metadataRepository, QueryUtil.createMatchQuery(requirement.getMatches()));
              }
            }));
          }
        }
      }

      final Map<List<String>, IProvidedCapability> allProvidedCapabilities = new HashMap<>();

      final Map<IRequirement, Set<IInstallableUnit>> resolvedRequirements = new HashMap<>();
      final Map<IProvidedCapability, Set<IInstallableUnit>> resolvedCapabilities = new HashMap<>();
      final Map<IProvidedCapability, Set<IRequirement>> capabilityResolutions = new HashMap<>();
      final Map<IRequirement, Set<IProvidedCapability>> requirementResolutions = new HashMap<>();
      for (Map.Entry<IRequirement, Future<Set<IInstallableUnit>>> entry : futures.entrySet())
      {
        IRequirement requirement = entry.getKey();
        Set<IInstallableUnit> ius = get(entry.getValue());
        resolvedRequirements.put(requirement, ius);

        if (requirement instanceof IRequiredCapability)
        {
          IRequiredCapability requiredCapability = (IRequiredCapability)requirement;
          String requirementNamespace = requiredCapability.getNamespace();
          String requirementName = requiredCapability.getName();
          VersionRange range = requiredCapability.getRange();

          for (IInstallableUnit iu : ius)
          {
            Collection<IProvidedCapability> providedCapabilities = iu.getProvidedCapabilities();
            for (IProvidedCapability providedCapability : providedCapabilities)
            {
              String namespace = providedCapability.getNamespace();
              String name = providedCapability.getName();
              Version version = providedCapability.getVersion();

              List<String> key = new ArrayList<>();
              key.add(namespace);
              key.add(name);
              key.add(version.toString());
              allProvidedCapabilities.put(key, providedCapability);

              if (ObjectUtil.equals(requirementNamespace, namespace) && ObjectUtil.equals(requirementName, name) && range.isIncluded(version))
              {
                CollectionUtil.add(capabilityResolutions, providedCapability, requirement);
                CollectionUtil.add(requirementResolutions, requirement, providedCapability);
                Set<IInstallableUnit> resolvedIUs = requiringIUs.get(requirement);
                if (resolvedIUs != null)
                {
                  for (IInstallableUnit resolvedIU : resolvedIUs)
                  {
                    CollectionUtil.add(resolvedCapabilities, providedCapability, resolvedIU);
                  }
                }
              }
            }
          }
        }
      }

      final Map<IInstallableUnit, Set<File>> iuFiles = new TreeMap<>();
      final Map<File, SignedContent> fileSignedContents = new TreeMap<>();
      final Map<File, List<String>> localFileIndices = new TreeMap<>();
      final Set<IInstallableUnit> classContainingIUs = new TreeSet<>();
      final Map<String, Set<Version>> iuIDVersions = new TreeMap<>();
      for (IInstallableUnit iu : allIUs)
      {
        Set<File> files = new LinkedHashSet<>();
        for (Map.Entry<File, Future<SignedContent>> entry : signedContentCache.get(iu).entrySet())
        {
          File file = entry.getKey();
          files.add(file);
          SignedContent signedContent = get(entry.getValue());
          fileSignedContents.put(file, signedContent);

          List<String> index = get(getIndex(file));
          localFileIndices.put(file, index);

          for (String string : index)
          {
            if (string.endsWith(".class"))
            {
              classContainingIUs.add(iu);
              break;
            }
          }
        }

        iuFiles.put(iu, files);

        CollectionUtil.add(iuIDVersions, iu.getId(), iu.getVersion());
      }

      final List<Report> childReports = new ArrayList<>();

      class MyReport extends Report
      {
        @SuppressWarnings("unused")
        public void getArtifacts()
        {
          Map<File, Set<IInstallableUnit>> artifacts = new TreeMap<>();
          for (IInstallableUnit iu : allIUs)
          {
            for (IArtifactKey artifactKey : iu.getArtifacts())
            {
              Future<Map<IArtifactDescriptor, File>> artifactCacheFuture = artifactCache.get(artifactKey);
              if (artifactCacheFuture != null)
              {
                for (File file : get(artifactCacheFuture).values())
                {
                  CollectionUtil.add(artifacts, file, iu);
                }
              }
            }
          }
        }

        private Map<String, String> breadcrumbs;

        @Override
        public Map<String, String> getBreadcrumbs()
        {
          if (breadcrumbs == null)
          {
            Map<String, String> result = new LinkedHashMap<>();

            String rootFolderName = rootOutputLocation.getName();
            result.put(rootFolderName, null);

            int segmentCount = uri.segmentCount();
            URI relativeURI = URI.createURI(toRelativePath(uri));
            int relativeSegmentCount = relativeURI.segmentCount();
            int extraSegmentCount = isAggrconRepositoryURI(uri) ? 1 : relativeSegmentCount - segmentCount;

            int depth = URI.createURI(toRelativePath(rootURI)).segmentCount();

            for (int i = 0; i < relativeSegmentCount; ++i)
            {
              String segment = relativeURI.segment(i);
              URI href = null;
              URI parentURI = uri.trimSegments(relativeSegmentCount - i - extraSegmentCount);
              Report parentReport = reports.get(parentURI);
              if (parentReport == null)
              {
                for (int j = i + extraSegmentCount; j < depth; ++j)
                {
                  href = href == null ? URI.createURI("..") : href.appendSegment("..");
                }
                href = href == null ? URI.createURI("index.html") : href.appendSegment("index.html");
              }
              else if (parentReport != this)
              {
                for (int j = i + extraSegmentCount; j < depth; ++j)
                {
                  href = href == null ? URI.createURI("..") : href.appendSegment("..");
                }

                String relativeIndexURL = parentReport.getRelativeIndexURL();
                href = href == null ? URI.createURI(relativeIndexURL) : href.appendSegment(relativeIndexURL);
                result.put(segment, href.toString());
              }

              result.put(segment, href == null ? null : href.toString());

              if (i == 0)
              {
                result.put(rootFolderName, href == null ? "../index.html" : "../" + href);
              }
            }

            breadcrumbs = result;
          }

          return breadcrumbs;
        }

        @Override
        public boolean isSimple()
        {
          return !(metadataRepository instanceof ICompositeRepository<?>);
        }

        @Override
        public boolean isRoot()
        {
          return rootURI.equals(uri);
        }

        @Override
        public String getReportSource()
        {
          return reportSource;
        }

        @Override
        public String getReportBrandingImage()
        {
          return getImage(URI.createURI(reportBranding), outputLocation);
        }

        @Override
        public boolean hasBrandingImage(IInstallableUnit iu)
        {
          String brandingImage = getBrandingImage(iu);
          return !brandingImage.equals(getWarningImage(outputLocation)) && !brandingImage.equals(getErrorImage());
        }

        @Override
        public boolean hasBrokenBrandingImage(IInstallableUnit iu)
        {
          String brandingImage = getBrandingImage(iu);
          return brandingImage.equals(getErrorImage());
        }

        @Override
        public String getBrandingImage(IInstallableUnit iu)
        {
          Future<URI> brandingImage = brandingImages.get(iu);
          if (brandingImage != null)
          {
            try
            {
              URI brandingImageURI = brandingImage.get();
              if (brandingImageURI != null)
              {
                return getImage(brandingImageURI, outputLocation);
              }

              return getWarningImage(outputLocation);
            }
            catch (Exception ex)
            {
              throw new RuntimeException(ex);
            }
          }

          return null;
        }

        @Override
        public Map<String, Set<IInstallableUnit>> getFeatureProviders()
        {
          Map<String, Set<IInstallableUnit>> result = new TreeMap<>();
          for (IInstallableUnit iu : featureIUs)
          {
            String provider = iu.getProperty(IInstallableUnit.PROP_PROVIDER, null);
            CollectionUtil.add(result, String.valueOf(provider), iu);
          }

          return result;
        }

        @Override
        public Set<String> getBrandingImages(Collection<IInstallableUnit> ius)
        {
          Set<String> result = new LinkedHashSet<>();
          for (IInstallableUnit iu : ius)
          {
            result.add(getBrandingImage(iu));
          }

          String image = getWarningImage(outputLocation);
          if (result.size() > 1)
          {
            result.remove(image);
          }

          return result;
        }

        @Override
        public String getSignedImage(boolean signed)
        {
          return getImage("org.eclipse.ui", signed ? "icons/full/obj16/signed_yes_tbl.png" : "icons/full/obj16/signed_no_tbl.png", outputLocation);
        }

        @Override
        public Map<String, Set<Version>> getIUVersions()
        {
          return iuIDVersions;
        }

        @Override
        public Set<IInstallableUnit> getClassContainingIUs()
        {
          return classContainingIUs;
        }

        private Set<IInstallableUnit> pluginsWithMissingPackGZ;

        @Override
        public Set<IInstallableUnit> getPluginsWithMissingPackGZ()
        {
          if (pluginsWithMissingPackGZ == null)
          {
            pluginsWithMissingPackGZ = new TreeSet<>();
            for (IInstallableUnit iu : classContainingIUs)
            {
              Set<File> files = iuFiles.get(iu);
              if (files.size() != 2)
              {
                pluginsWithMissingPackGZ.add(iu);
              }
            }
          }

          return pluginsWithMissingPackGZ;
        }

        private Set<IInstallableUnit> unsignedIUs;

        @Override
        public Set<IInstallableUnit> getUnsignedIUs()
        {
          if (unsignedIUs == null)
          {
            unsignedIUs = new TreeSet<>();
            for (IInstallableUnit iu : allIUs)
            {
              Map<String, Boolean> iuArtifacts = getIUArtifacts(iu);
              if (iuArtifacts.containsValue(Boolean.FALSE))
              {
                unsignedIUs.add(iu);
              }
            }
          }
          return unsignedIUs;
        }

        private Set<IInstallableUnit> badProviderIUs;

        @Override
        public Set<IInstallableUnit> getBadProviderIUs()
        {
          if (badProviderIUs == null)
          {
            badProviderIUs = new TreeSet<>();
            Map<String, Set<IInstallableUnit>> featureProviders = getFeatureProviders();
            for (Map.Entry<String, Set<IInstallableUnit>> entry : featureProviders.entrySet())
            {
              String provider = entry.getKey();
              if (provider == null || !provider.toLowerCase().contains("eclipse"))
              {
                badProviderIUs.addAll(entry.getValue());
              }
            }
          }
          return badProviderIUs;
        }

        private Set<IInstallableUnit> badLicenseIUs;

        @Override
        public Set<IInstallableUnit> getBadLicenseIUs()
        {
          if (badLicenseIUs == null)
          {
            badLicenseIUs = new TreeSet<>();
            Map<LicenseDetail, Set<IInstallableUnit>> licenses = getLicenses();
            for (Map.Entry<LicenseDetail, Set<IInstallableUnit>> entry : licenses.entrySet())
            {
              if (!entry.getKey().isSUA())
              {
                badLicenseIUs.addAll(entry.getValue());
              }
            }
          }
          return badLicenseIUs;
        }

        private Set<IInstallableUnit> brokenBrandingIUs;

        @Override
        public Set<IInstallableUnit> getBrokenBrandingIUs()
        {
          if (brokenBrandingIUs == null)
          {
            brokenBrandingIUs = new TreeSet<>();
            for (IInstallableUnit iu : getFeatureIUs())
            {
              if (hasBrokenBrandingImage(iu))
              {
                brokenBrandingIUs.add(iu);
              }
            }
          }
          return brokenBrandingIUs;
        }

        @Override
        public Map<String, Boolean> getIUArtifacts(IInstallableUnit iu)
        {
          Map<String, Boolean> result = new TreeMap<>();
          Set<File> files = iuFiles.get(iu);
          for (File file : files)
          {
            SignedContent signedContent = fileSignedContents.get(file);
            int prefixLength = artifactCacheFolder.toString().length() + 1;
            String path = file.toString().substring(prefixLength).replace('\\', '/');
            result.put(path, signedContent == null || path.startsWith("binary/") ? null : signedContent.isSigned());
          }

          return result;
        }

        @Override
        public String getRepositoryURL(String artifact)
        {
          for (Future<Map<IArtifactDescriptor, File>> future : artifactCache.values())
          {
            Map<IArtifactDescriptor, File> artifactFiles = get(future);
            for (Map.Entry<IArtifactDescriptor, File> entry : artifactFiles.entrySet())
            {
              File file = entry.getValue();
              String uriPath = file.toURI().toString();
              if (uriPath.endsWith(artifact))
              {
                String location = entry.getKey().getRepository().getLocation().toString();
                return location.replaceAll("/$", "");
              }
            }
          }

          throw new IllegalStateException();
        }

        @Override
        public List<String> getArtifactStatus(String artifact)
        {
          for (Future<Map<IArtifactDescriptor, File>> future : artifactCache.values())
          {
            Map<IArtifactDescriptor, File> artifactFiles = get(future);
            for (Map.Entry<IArtifactDescriptor, File> entry : artifactFiles.entrySet())
            {
              File file = entry.getValue();
              String uriPath = file.toURI().toString();
              if (uriPath.endsWith(artifact))
              {
                URI statusFile = URI.createURI(uriPath + ".processed.fail");
                if (URIConverter.INSTANCE.exists(statusFile, Collections.emptyMap()))
                {
                  try
                  {
                    return IOUtil.readLines(URIConverter.INSTANCE.createInputStream(statusFile), null);
                  }
                  catch (IOException ex)
                  {
                  }
                }

                statusFile = URI.createURI(uriPath + ".fail");
                if (URIConverter.INSTANCE.exists(statusFile, Collections.emptyMap()))
                {
                  try
                  {
                    return IOUtil.readLines(URIConverter.INSTANCE.createInputStream(statusFile), null);
                  }
                  catch (IOException ex)
                  {
                  }
                }
              }
            }
          }

          return Collections.emptyList();
        }

        @Override
        public boolean isExpired(Certificate certificate)
        {
          X509Certificate x509Certificate = (X509Certificate)certificate;
          return new Date().after(x509Certificate.getNotAfter());
        }

        @Override
        public Map<String, String> getCertificateComponents(Certificate certificate)
        {
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd");
          X509Certificate x509Certificate = (X509Certificate)certificate;
          String notBefore = dateFormat.format(x509Certificate.getNotBefore());
          String notAfter = dateFormat.format(x509Certificate.getNotAfter());
          X500Principal subjectX500Principal = x509Certificate.getSubjectX500Principal();
          String name = subjectX500Principal.getName();
          Pattern namePattern = Pattern.compile("([A-Za-z]+)=(([^,\\\\]|\\\\,)+),");
          Matcher matcher = namePattern.matcher(name);
          Map<String, String> components = new LinkedHashMap<>();
          while (matcher.find())
          {
            components.put(matcher.group(1), matcher.group(2).replaceAll("\\\\", ""));
          }

          components.put("from", notBefore);
          components.put("to", notAfter);

          return components;
        }

        @Override
        public List<Certificate> getCertificates(IInstallableUnit iu)
        {
          for (Map.Entry<List<Certificate>, Map<String, IInstallableUnit>> entry : getCertificates().entrySet())
          {
            if (entry.getValue().values().contains(iu))
            {
              return entry.getKey();
            }
          }

          return null;
        }

        private Map<List<Certificate>, Map<String, IInstallableUnit>> certificates;

        @Override
        public Map<List<Certificate>, Map<String, IInstallableUnit>> getCertificates()
        {
          if (certificates == null)
          {
            Comparator<List<Certificate>> certificateComparator = new Comparator<>()
            {
              @Override
              public int compare(List<Certificate> o1, List<Certificate> o2)
              {
                int size = o1.size();
                int result = Integer.compare(size, o2.size());
                if (result == 0 && size != 0)
                {
                  X509Certificate certificate1 = (X509Certificate)o1.get(0);
                  X509Certificate certificate2 = (X509Certificate)o2.get(0);
                  result = certificate1.getSubjectX500Principal().getName().compareTo(certificate2.getSubjectX500Principal().getName());
                }

                return result;
              }
            };

            int prefixLength = artifactCacheFolder.toString().length() + 1;
            certificates = new TreeMap<>(certificateComparator);
            for (Map.Entry<IInstallableUnit, Set<File>> entry : iuFiles.entrySet())
            {
              IInstallableUnit iu = entry.getKey();
              Set<File> files = entry.getValue();
              for (File file : files)
              {
                String path = file.toString().substring(prefixLength).replace('\\', '/');
                if (!path.startsWith("binary/"))
                {
                  SignedContent signedContent = fileSignedContents.get(file);
                  if (signedContent != null && signedContent.isSigned())
                  {
                    SignerInfo[] signerInfos = signedContent.getSignerInfos();
                    for (SignerInfo signerInfo : signerInfos)
                    {
                      Certificate[] certificateChain = signerInfo.getCertificateChain();
                      List<Certificate> certificateList = Arrays.asList(certificateChain);
                      Map<String, IInstallableUnit> artifacts = certificates.get(certificateList);
                      if (artifacts == null)
                      {
                        artifacts = new TreeMap<>();
                        certificates.put(certificateList, artifacts);
                      }

                      artifacts.put(path, iu);
                    }
                  }
                  else
                  {
                    Map<String, IInstallableUnit> artifacts = certificates.get(Collections.emptyList());
                    if (artifacts == null)
                    {
                      artifacts = new TreeMap<>();
                      certificates.put(Collections.<Certificate> emptyList(), artifacts);
                    }

                    artifacts.put(path, iu);
                  }
                }
              }
            }
          }

          return certificates;
        }

        @Override
        public List<String> getXML(final IInstallableUnit iu, Map<String, String> replacements)
        {
          getLicenses(iu);
          if (Boolean.FALSE)
          {
            Collection<IRequirement> requirements = iu.getRequirements();
            final Map<List<String>, IRequirement> requiredCapabilities = new HashMap<>();
            for (IRequirement requirement : requirements)
            {
              IMatchExpression<IInstallableUnit> match = requirement.getMatches();
              if (RequiredCapability.isVersionRangeRequirement(match))
              {
                List<String> key = new ArrayList<>(3);
                key.add(RequiredCapability.extractNamespace(match));
                key.add(RequiredCapability.extractName(match));
                key.add(RequiredCapability.extractRange(match).toString());
                requiredCapabilities.put(key, requirement);
              }
            }
          }

          final Map<String, String> ids = new HashMap<>();
          ValueHandler valueHandler = new InstallableUnitWriter.ValueHandler()
          {
            private String uuid;

            private String namespace;

            private String name;

            private String handleLicense(String content)
            {
              String plainContent = new InstallableUnitWriter().expandEntities(content);
              ILicense license = MetadataFactory.createLicense(null, plainContent);
              LicenseDetail licenseDetail = getLicenseDetail(license, false);
              if (licenseDetail != null)
              {
                return licenseDetail.getUUID();
              }

              return null;
            }

            @Override
            public String handleElementContent(List<String> elementNames, String elementContent)
            {
              if (XMLConstants.LICENSE_ELEMENT.equals(getCurrentElementName(elementNames)))
              {
                String licenseReplacement = handleLicense(elementContent);
                if (licenseReplacement != null)
                {
                  return licenseReplacement;
                }
              }

              return super.handleElementContent(elementNames, elementContent);
            }

            @Override
            public void startElement(List<String> elementNames, String uuid)
            {
              this.uuid = uuid;
            }

            @Override
            public String handleAttributeValue(List<String> elementNames, String attributeName, String attributeValue)
            {
              String elementName = getCurrentElementName(elementNames);
              if (XMLConstants.PROPERTY_ELEMENT.equals(elementName))
              {
                if (XMLConstants.PROPERTY_VALUE_ATTRIBUTE.equals(attributeName) && attributeValue.contains("&#xA;"))
                {
                  String licenseReplacement = handleLicense(attributeValue);
                  if (licenseReplacement != null)
                  {
                    return licenseReplacement;
                  }
                }
              }
              else if (XMLConstants.PROVIDED_CAPABILITY_ELEMENT.equals(elementName))
              {
                if (XMLConstants.NAMESPACE_ATTRIBUTE.equals(attributeName))
                {
                  namespace = attributeValue;
                }
                else if (XMLConstants.NAME_ATTRIBUTE.equals(attributeName))
                {
                  name = attributeValue;
                }
                else if (XMLConstants.VERSION_ATTRIBUTE.equals(attributeName))
                {
                  List<String> key = new ArrayList<>();
                  key.add(namespace);
                  key.add(name);
                  key.add(attributeValue);
                  IProvidedCapability providedCapability = allProvidedCapabilities.get(key);
                  if (providedCapability != null)
                  {
                    ids.put(uuid, getProvidedCapabilityID(providedCapability));
                  }

                  Set<IInstallableUnit> matchingIUs = resolvedCapabilities.get(providedCapability);
                  if (matchingIUs == null || matchingIUs.isEmpty())
                  {
                    return "<span class=\"unused-capability\">" + attributeValue + "</span>";
                  }

                  StringBuilder links = new StringBuilder();
                  String id = "_" + EcoreUtil.generateUUID();
                  links.append(" <button id=\"" + id
                      + "_arrows\" style=\"font-size: 70%; bottom-margin: 1pt; \" class=\"orange bb xml-links_button\" onclick=\"expand_collapse_inline_block('"
                      + id + "');\">&#x25B7;</button>");
                  links.append("<span id=\"" + id + "\" class=\"xml-links\" style=\"display: none; margin-top: 0.2ex; vertical-align : top;\">");
                  for (IInstallableUnit iu : new TreeSet<>(matchingIUs))
                  {
                    links.append(" <a href=\"");
                    links.append(getIUID(iu));
                    links.append(".html");
                    HashSet<IRequirement> requirements = new HashSet<>(iu.getRequirements());
                    requirements.retainAll(capabilityResolutions.get(providedCapability));
                    if (!requirements.isEmpty())
                    {
                      links.append('#');
                      links.append(getRequirementID(requirements.iterator().next()));
                    }

                    links.append('"');
                    links.append(">\u27a6");
                    links.append(iu.getId());
                    links.append(" ");
                    links.append(iu.getVersion());
                    links.append("</a><br/>");
                  }

                  links.append("</span>");

                  return "<span class=\"used-capability\">" + attributeValue + links + "</span>";
                }
              }
              else if (XMLConstants.REQUIREMENT_ELEMENT.equals(elementName))
              {
                if (XMLConstants.NAMESPACE_ATTRIBUTE.equals(attributeName))
                {
                  namespace = attributeValue;
                }
                else if (XMLConstants.NAME_ATTRIBUTE.equals(attributeName))
                {
                  name = attributeValue;
                }
                else if (XMLConstants.VERSION_RANGE_ATTRIBUTE.equals(attributeName))
                {
                  List<String> key = new ArrayList<>();
                  key.add(namespace);
                  key.add(name);
                  key.add(attributeValue);
                  IRequirement requirement = requiredCapabilities.get(key);
                  if (requirement != null)
                  {
                    ids.put(uuid, getRequirementID(requirement));
                  }

                  Set<IInstallableUnit> matchingIUs = resolvedRequirements.get(requirement);
                  if (matchingIUs == null || matchingIUs.isEmpty())
                  {
                    return "<span class=\"unresolved-requirement\">" + attributeValue + "</span>";
                  }

                  String requirementName = "";
                  if (requirement instanceof IRequiredCapability)
                  {
                    requirementName = ((IRequiredCapability)requirement).getName();
                  }

                  StringBuilder links = new StringBuilder();
                  String id = "_" + EcoreUtil.generateUUID();
                  links.append(" <button id=\"" + id
                      + "_arrows\" style=\"font-size: 70%; bottom-marin: 1pt;\" class=\"orange bb xml-links_button\" onclick=\"expand_collapse_inline_block('"
                      + id + "');\">&#x25B7;</button>");
                  links.append("<span id=\"" + id + "\" class=\"xml-links\" style=\"display: none; margin-top: 0.2ex; vertical-align : top;\">");
                  for (IInstallableUnit iu : new TreeSet<>(matchingIUs))
                  {
                    links.append(" <a href=\"");
                    links.append(getIUID(iu));
                    links.append(".html");
                    HashSet<IProvidedCapability> providedCapabilities = new HashSet<>(iu.getProvidedCapabilities());
                    providedCapabilities.retainAll(requirementResolutions.get(requirement));
                    if (!providedCapabilities.isEmpty())
                    {
                      links.append('#');
                      links.append(getProvidedCapabilityID(providedCapabilities.iterator().next()));
                    }

                    links.append('"');
                    links.append(">\u27a5");
                    String iuID = iu.getId();
                    if (!requirementName.equals(iuID))
                    {
                      links.append(iuID);
                      links.append(' ');
                    }

                    links.append(iu.getVersion());
                    links.append("</a><br/>");
                  }

                  links.append("</span>");

                  ids.put(uuid, getRequirementID(requirement));
                  return "<span class=\"resolved-requirement\">" + attributeValue + links + "</span>";
                }
              }

              return super.handleAttributeValue(elementNames, attributeName, attributeValue);
            }
          };

          String xml = new InstallableUnitWriter().toHTML(iu, valueHandler);
          if (replacements != null)
          {
            for (Map.Entry<String, String> entry : replacements.entrySet())
            {
              xml = xml.replace(entry.getKey(), entry.getValue());
            }
          }

          StringBuffer result = new StringBuffer();
          Matcher matcher = Pattern.compile("id=\"([^\"]+)\"").matcher(xml);
          while (matcher.find())
          {
            String id = matcher.group(1);
            String replacement = ids.get(id);
            if (replacement != null)
            {
              matcher.appendReplacement(result, Matcher.quoteReplacement("id=\"" + replacement + '"'));
            }
          }
          matcher.appendTail(result);

          return StringUtil.explode(result.toString(), "\n");
        }

        @Override
        public String getErrorImage()
        {
          return RepositoryIntegrityAnalyzer.this.getErrorImage(outputLocation);
        }

        @Override
        public String getHelpLink()
        {
          return isAggrconRepository(metadataRepository) ? "https://wiki.eclipse.org/Oomph_Repository_Analyzer#Simultaneous_Release"
              : "https://wiki.eclipse.org/Oomph_Repository_Analyzer#Update_Site_Pages";
        }

        @Override
        public String getHelpImage()
        {
          return getImage(URI.createURI("https://git.eclipse.org/c/platform/eclipse.platform.ui.git/plain/bundles/org.eclipse.jface/icons/full/help@2x.png"),
              outputLocation);
        }

        @Override
        public String getHelpText()
        {
          return isAggrconRepository(metadataRepository) ? "filtered aggrcon index" : "update site index";
        }

        @Override
        public Report getParent()
        {
          return parentReport;
        }

        @Override
        public List<Report> getChildren()
        {
          return childReports;
        }

        @Override
        public Map<String, String> getNavigation()
        {
          Map<String, String> result = new LinkedHashMap<>();
          for (Report report : reports.values())
          {
            String title = report.getTitle(true);
            if (report == this)
            {
              title += '@';
            }

            result.put(report.getRelativeIndexURL(), title);
          }

          return result;
        }

        @Override
        public String getSiteURL()
        {
          return metadataRepository.getLocation().toString();
        }

        private String getXML(CompositeRepositoryState state, String type)
        {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          new CompositeRepositoryIO().write(state, out, type);
          final String siteHost = URI.createURI(getSiteURL()).host();
          ValueHandler valueHandler = new ValueHandler()
          {
            @Override
            public String handleAttributeValue(List<String> elementNames, String attributeName, String attributeValue)
            {
              if ("location".equals(attributeName))
              {
                URI uri = URI.createURI(attributeValue);
                String host = uri.host();
                if (host != null && host.equals(siteHost))
                {
                  return "<span class=\"bad-absolute-location\">" + toHref(attributeValue) + "</span>";
                }
              }

              return super.handleAttributeValue(elementNames, toHref(attributeName), toHref(attributeValue));
            }

            private String toHref(String value)
            {
              return value.startsWith("http://") || value.startsWith("https://") ? "<a href=\"" + value + "\"/>" + value + "</a>" : value;
            }
          };

          InstallableUnitWriter.HTMLResource htmlResource = new InstallableUnitWriter.HTMLResource(valueHandler);
          try
          {
            htmlResource.load(new ByteArrayInputStream(out.toByteArray()), null);
            out.reset();
            XMLTypeDocumentRoot documentRoot = (XMLTypeDocumentRoot)htmlResource.getContents().get(0);
            FeatureMap mixed = documentRoot.getMixed();
            for (Iterator<FeatureMap.Entry> it = mixed.iterator(); it.hasNext();)
            {
              FeatureMap.Entry entry = it.next();
              if (FeatureMapUtil.isProcessingInstruction(entry))
              {
                it.remove();
              }
            }

            htmlResource.save(out, null);
            return new String(out.toByteArray(), "UTF-8");
          }
          catch (IOException ex)
          {
            return null;
          }
        }

        @Override
        public String getMetadataXML()
        {
          if (metadataRepository instanceof CompositeMetadataRepository)
          {
            CompositeMetadataRepository compositeMetadataRepository = (CompositeMetadataRepository)metadataRepository;
            return getXML(compositeMetadataRepository.toState(), CompositeArtifactRepository.PI_REPOSITORY_TYPE);
          }

          return null;
        }

        @Override
        public String getArtifactML()
        {
          if (artifactRepository instanceof CompositeArtifactRepository)
          {
            CompositeArtifactRepository compositeArtifactRepository = (CompositeArtifactRepository)artifactRepository;
            return getXML(compositeArtifactRepository.toState(), CompositeArtifactRepository.PI_REPOSITORY_TYPE);
          }

          return null;
        }

        @Override
        public Map<Report.LicenseDetail, Set<IInstallableUnit>> getLicenses()
        {
          return sortedLicenseIUs;
        }

        @Override
        public String getTitle()
        {
          String name = metadataRepository.getName();
          return name.isEmpty() ? uri.lastSegment() : name;
        }

        @Override
        public String getTitle(boolean narrow)
        {
          String name = metadataRepository.getName();
          if (StringUtil.isEmpty(name))
          {
            name = "<span style=\"color: FireBrick;\">Unnamed Repository</span>";
          }

          if (aggregator)
          {
            return name;
          }

          String repoIdentifier = uri.lastSegment();

          return name + (narrow ? "<br style=\"line-height: 4ex;\"/>" : " ") + "<span style=\"color: DarkOliveGreen;\">" + repoIdentifier + "</span>";
        }

        @Override
        public String getDate()
        {
          String date = "unknown";
          Map<String, String> properties = metadataRepository.getProperties();
          String timestamp = properties.get(IRepository.PROP_TIMESTAMP);
          if (timestamp != null)
          {
            try
            {
              long time = Long.parseLong(timestamp);
              date = new SimpleDateFormat("yyyy'-'MM'-'dd' at 'HH':'mm ").format(new Date(time));
            }
            catch (RuntimeException ex)
            {
              //$FALL-THROUGH$
            }
          }

          return date;
        }

        @Override
        public List<String> getFeatures(Iterable<IInstallableUnit> features)
        {
          List<String> result = new ArrayList<>();
          for (IInstallableUnit iu : features)
          {
            String name = getNameAndVersion(iu);
            if (!result.contains(name))
            {
              result.add(name);
            }
          }

          Collections.sort(result, CommonPlugin.INSTANCE.getComparator());
          return result;
        }

        @Override
        public List<String> getFeatures()
        {
          return getFeatures(getFeatureIUs());
        }

        @Override
        public List<IInstallableUnit> getFeatureIUs()
        {
          return featureIUs;
        }

        @Override
        public List<LicenseDetail> getLicenses(IInstallableUnit iu)
        {
          if ("true".equals(iu.getProperty(QueryUtil.PROP_TYPE_GROUP)))
          {
            return RepositoryIntegrityAnalyzer.this.getLicenses(iu);
          }

          return null;
        }

        @Override
        public Set<IInstallableUnit> getProducts()
        {
          return productIUs;
        }

        @Override
        public boolean isDuplicationExpected(String id)
        {
          return expectedDuplicates.contains(id);
        }

        @Override
        public Set<IInstallableUnit> getAllIUs()
        {
          return allIUs;
        }

        @Override
        public Set<IInstallableUnit> getResolvedRequirements(IInstallableUnit iu)
        {
          Set<IInstallableUnit> result = new TreeSet<>();
          for (IRequirement requirement : iu.getRequirements())
          {
            Set<IInstallableUnit> ius = resolvedRequirements.get(requirement);
            if (ius != null)
            {
              result.addAll(ius);
            }
          }

          return result;
        }

        @Override
        public String getArtifactImage(String artifact)
        {
          if (artifact.startsWith("binary"))
          {
            return getBinaryImage();
          }

          if (artifact.endsWith(".jar"))
          {
            return getJarImage();
          }

          if (artifact.endsWith(".pack.gz"))
          {
            return getPackGZImage();
          }

          return null;
        }

        @Override
        public String getArtifactSize(String artifact)
        {
          long length = new File(artifactCacheFolder, artifact).length();
          return format(length);
        }

        public String getBinaryImage()
        {
          return getImage(
              URI.createURI("https://git.eclipse.org/c/platform/eclipse.platform.ui.git/plain/bundles/org.eclipse.ui.ide/icons/full/etool16/build_exec@2x.png"),
              outputLocation);
        }

        public String getJarImage()
        {
          return getImage(URI.createURI("https://git.eclipse.org/c/jdt/eclipse.jdt.ui.git/plain/org.eclipse.jdt.ui/icons/full/etool16/exportjar_wiz@2x.png"),
              outputLocation);
        }

        public String getPackGZImage()
        {
          return getImage(
              URI.createURI(
                  "https://git.eclipse.org/c/platform/eclipse.platform.ui.git/plain/bundles/org.eclipse.ui.ide/icons/full/etool16/exportzip_wiz@2x.png"),
              outputLocation);
        }

        @Override
        public String getIUImage(IInstallableUnit iu)
        {
          if (isCategory(iu))
          {
            return getCategoryImage();
          }

          if (isProduct(iu))
          {
            return getProductImage();
          }

          if (isGroup(iu))
          {
            return getFeatureImage();
          }

          for (IProvidedCapability providedCapability : iu.getProvidedCapabilities())
          {
            if (PublisherHelper.CAPABILITY_NS_JAVA_PACKAGE.equals(providedCapability.getNamespace()))
            {
              return getPluginImage();
            }
          }

          for (IRequirement requirement : iu.getRequirements())
          {
            IMatchExpression<IInstallableUnit> match = requirement.getMatches();
            if (RequiredCapability.isVersionRangeRequirement(match)
                && PublisherHelper.CAPABILITY_NS_JAVA_PACKAGE.equals(RequiredCapability.extractNamespace(match)))
            {
              return getPluginImage();
            }
          }

          return getBundleImage();
        }

        @Override
        public String getLicenseImage()
        {
          return getImage(URI.createURI("https://git.eclipse.org/c/pde/eclipse.pde.ui.git/plain/ui/org.eclipse.pde.ui/icons/obj16/license_obj@2x.png"),
              outputLocation);
        }

        @Override
        public String getRepositoryImage()
        {
          return getImage(
              URI.createURI("https://git.eclipse.org/c/equinox/rt.equinox.p2.git/plain/bundles/org.eclipse.equinox.p2.ui/icons/obj/metadata_repo_obj@2x.png"),
              outputLocation);
        }

        @Override
        public String getProviderImage()
        {
          return getImage(URI.createURI("https://git.eclipse.org/c/pde/eclipse.pde.ui.git/plain/ui/org.eclipse.pde/eclipse32.png"), outputLocation);
        }

        @Override
        public String getFeatureImage()
        {
          return getImage(URI.createURI("https://git.eclipse.org/c/pde/eclipse.pde.ui.git/plain/ui/org.eclipse.pde.ui/icons/obj16/feature_obj@2x.png"),
              outputLocation);
        }

        @Override
        public String getProductImage()
        {
          return getImage(URI.createURI("https://git.eclipse.org/c/pde/eclipse.pde.ui.git/plain/ui/org.eclipse.pde.ui/icons/obj16/product_xml_obj@2x.png"),
              outputLocation);
        }

        public String getPluginImage()
        {
          return getImage(URI.createURI("https://git.eclipse.org/c/pde/eclipse.pde.ui.git/plain/ui/org.eclipse.pde.ui/icons/obj16/plugin_obj@2x.png"),
              outputLocation);
        }

        @Override
        public String getBundleImage()
        {
          return getImage(URI.createURI("https://git.eclipse.org/c/pde/eclipse.pde.ui.git/plain/ui/org.eclipse.pde.ui/icons/obj16/bundle_obj@2x.png"),
              outputLocation);
        }

        @Override
        public String getCategoryImage()
        {
          return getImage(
              URI.createURI("https://git.eclipse.org/c/equinox/rt.equinox.p2.git/plain/bundles/org.eclipse.equinox.p2.ui/icons/obj/category_obj@2x.png"),
              outputLocation);
        }

        @Override
        public Map<String, List<String>> getBundles()
        {
          Map<String, List<String>> result = new TreeMap<>(CommonPlugin.INSTANCE.getComparator());
          for (IInstallableUnit iu : allIUs)
          {
            String id = iu.getId();
            List<String> lines = new ArrayList<>();
            for (IProvidedCapability providedCapability : iu.getProvidedCapabilities())
            {
              String namespace = providedCapability.getNamespace();
              String name = providedCapability.getName();
              if ("org.eclipse.equinox.p2.eclipse.type".equals(namespace) && "bundle".equals(name))
              {
                String iuName = iu.getProperty("org.eclipse.equinox.p2.name", null);
                iuName += " " + iu.getVersion();
                iuName = iuName.substring(0, iuName.lastIndexOf('.'));
                if (!result.containsKey(iuName))
                {
                  lines.add(0, "\u21D6 " + id + " " + iu.getVersion());
                  result.put(iuName, lines);
                }
              }
              else if ("java.package".equals(namespace))
              {
                Version version = providedCapability.getVersion();
                lines.add("\u2196 " + name + (Version.emptyVersion.equals(version) ? "" : " " + version));
              }
            }

            for (IRequirement requirement : iu.getRequirements())
            {
              if (requirement instanceof IRequiredCapability)
              {
                IRequiredCapability requiredCapability = (IRequiredCapability)requirement;
                String namespace = requiredCapability.getNamespace();
                String line = null;
                if ("osgi.bundle".equals(namespace))
                {
                  line = "\u21D8 ";
                }
                else if ("java.package".equals(namespace))
                {
                  line = "\u2198 ";
                }

                if (line != null)
                {
                  String name = requiredCapability.getName();
                  VersionRange range = requiredCapability.getRange();

                  line += name;
                  if (!VersionRange.emptyRange.equals(range))
                  {
                    line += " " + range;
                  }

                  if (requiredCapability.getMin() == 0)
                  {
                    line += " optional";
                    if (requiredCapability.isGreedy())
                    {
                      line += " greedy";
                    }
                  }

                  lines.add(line);
                }
              }
            }
          }

          return result;
        }

        @Override
        public List<IUReport> getIUReports()
        {
          List<IUReport> result = new ArrayList<>();
          for (final IInstallableUnit iu : allIUs)
          {
            class MyIUReport extends IUReport
            {
              @Override
              public Report getReport()
              {
                return MyReport.this;
              }

              @Override
              public String getNow()
              {
                return getReport().getNow();
              }

              @Override
              public IInstallableUnit getIU()
              {
                return iu;
              }

              @Override
              public String getDescription()
              {
                String description = iu.getProperty(IInstallableUnit.PROP_DESCRIPTION, null);
                return description;
              }

              @Override
              public String getProvider()
              {
                String provider = iu.getProperty(IInstallableUnit.PROP_PROVIDER, null);
                return provider;
              }

              @Override
              public String getReportBrandingImage()
              {
                return "../" + getReport().getReportBrandingImage();
              }

              @Override
              public String getHelpLink()
              {
                return "https://wiki.eclipse.org/Oomph_Repository_Analyzer#Installable_Unit_Pages";
              }

              @Override
              public String getHelpImage()
              {
                return "../" + getReport().getHelpImage();
              }

              @Override
              public String getHelpText()
              {
                return "installable unit index";
              }

              @Override
              public String getReportSource()
              {
                return getReport().getReportSource();
              }

              @Override
              public String getTitle()
              {
                return iu.toString();
              }

              @Override
              public String getTitle(boolean narrow)
              {
                String id = iu.getId();
                Version version = iu.getVersion();
                return narrow ? id + "<br/><span style=\"color: DarkOliveGreen;\">" + version + "</span>" : id + " " + version;
              }

              @Override
              public String getRelativeReportURL()
              {
                return getRelativeIUReportURL(iu);
              }

              @Override
              public Map<String, String> getBreadcrumbs()
              {
                Map<String, String> breadcrumbs = new LinkedHashMap<>(getReport().getBreadcrumbs());
                for (Map.Entry<String, String> entry : breadcrumbs.entrySet())
                {
                  String href = entry.getValue();
                  entry.setValue(href == null ? "../" + getReport().getRelativeIndexURL() : "../" + href);
                }

                breadcrumbs.put(iu.toString(), null);
                return breadcrumbs;
              }

              @Override
              public Map<String, String> getNavigation()
              {
                Map<String, String> navigation = new LinkedHashMap<>();
                if (!aggregator)
                {
                  navigation.put("../" + getReport().getRelativeIndexURL(), getReport().getTitle(true));
                }
                return navigation;
              }
            }

            result.add(new MyIUReport());
          }

          return result;
        }
      }

      report = new MyReport();

      if (metadataRepository instanceof ICompositeRepository<?>)
      {
        ICompositeRepository<?> compositeRepository = (ICompositeRepository<?>)metadataRepository;
        List<java.net.URI> children = compositeRepository.getChildren();
        for (java.net.URI child : children)
        {
          Report childReport = generateReport(report, rootURI, rootOutputLocation, toExternalRepositoryLocation(child), outputLocation, artifactCache,
              artifactCacheFolder, reportSource, reportBranding);
          childReports.add(childReport);
        }
      }

      reports.put(uri, report);
    }

    return report;
  }

  private Set<IInstallableUnit> getAllIUs(IMetadataRepository metadataRepository)
  {
    if (isAggrconRepository(metadataRepository))
    {
      Set<IInstallableUnit> allIUs = new TreeSet<>();
      Map<String, String> properties = metadataRepository.getProperties();
      for (Map.Entry<String, String> entry : properties.entrySet())
      {
        URI uri = URI.createURI(entry.getKey());
        String scheme = uri.scheme();
        if ("http".equals(scheme) || "https".equals(scheme))
        {
          List<String> requirements = StringUtil.explode(entry.getValue(), ";");
          for (String requirement : requirements)
          {
            List<String> parts = StringUtil.explode(requirement, " ");
            String id = parts.get(0);

            IQuery<IInstallableUnit> iuQuery = parts.size() > 1 ? QueryUtil.createIUQuery(id, VersionRange.create(parts.get(1))) : QueryUtil.createIUQuery(id);
            Set<IInstallableUnit> result = query(metadataRepository, QueryUtil.createCompoundQuery(iuQuery, QueryUtil.createLatestIUQuery(), true));
            if (result.isEmpty())
            {
              System.err.println("### not found " + requirement + " in" + metadataRepository.getLocation());
            }
            else
            {
              IInstallableUnit iu = result.iterator().next();
              collectChildren(metadataRepository, allIUs, iu);
            }
          }
        }
      }
      return allIUs;
    }

    Set<IInstallableUnit> allIUs = query(metadataRepository, QueryUtil.createIUAnyQuery());
    return allIUs;
  }

  private void collectChildren(IMetadataRepository metadataRepository, Set<IInstallableUnit> allIUs, IInstallableUnit iu)
  {
    if (allIUs.add(iu))
    {
      for (IRequirement requirement : iu.getRequirements())
      {
        if (requirement instanceof IRequiredCapability)
        {
          IRequiredCapability requiredCapability = (IRequiredCapability)requirement;
          if (IInstallableUnit.NAMESPACE_IU_ID.equals(requiredCapability.getNamespace()))
          {
            VersionRange range = requiredCapability.getRange();
            Version minimum = range.getMinimum();
            if (minimum.equals(range.getMaximum()))
            {
              Set<IInstallableUnit> result = query(metadataRepository, QueryUtil.createIUQuery(requiredCapability.getName(), minimum));
              if (result.isEmpty())
              {
                System.err.println("### not found " + requirement + " in" + metadataRepository.getLocation());
              }
              else
              {
                IInstallableUnit requiredIU = result.iterator().next();
                collectChildren(metadataRepository, allIUs, requiredIU);
              }
            }
          }
        }
      }
    }
  }

  private Future<List<String>> getIndex(final File file)
  {
    final String path = file.toString();
    final File effectiveFile = path.endsWith(".pack.gz") ? new File(path + PROCESSED) : file;
    Future<List<String>> future = fileIndices.get(effectiveFile);
    if (future == null)
    {
      future = getExecutor().submit(new Callable<List<String>>()
      {
        @Override
        public List<String> call() throws Exception
        {
          final List<String> result = new ArrayList<>();
          ZIPUtil.unzip(effectiveFile, new ZIPUtil.UnzipHandler()
          {
            @Override
            public void unzipFile(String name, InputStream zipStream) throws IOException
            {
              result.add(name);
            }

            @Override
            public void unzipDirectory(String name) throws IOException
            {
            }
          });
          return result;
        }
      });

      fileIndices.put(effectiveFile, future);
    }

    fileIndices.put(file, future);
    return future;
  }

  private Future<URI> getBrandingImage(final IInstallableUnit iu, final IMetadataRepository metadataRepository, final IArtifactRepository artifactRepository,
      Map<IInstallableUnit, Future<URI>> brandingImages, final Map<IArtifactKey, Future<Map<IArtifactDescriptor, File>>> artifactCache, final File cache)
  {
    Future<URI> result = brandingImages.get(iu);
    if (result == null)
    {
      final String id = iu.getId();
      final String baseID = id.replaceAll("(\\.source)?\\.feature\\.group$", "");
      if (id.endsWith(".source.feature.group"))
      {
        IQueryResult<IInstallableUnit> query = metadataRepository.query(QueryUtil.createIUQuery(baseID + ".feature.group"), new NullProgressMonitor());
        if (!query.isEmpty())
        {
          result = getBrandingImage(P2Util.asIterable(query).iterator().next(), metadataRepository, artifactRepository, brandingImages, artifactCache, cache);
          brandingImages.put(iu, result);
          return result;
        }
      }

      String jarID = baseID + ".feature.jar";
      Version version = iu.getVersion();
      IQueryResult<IInstallableUnit> jarQuery = metadataRepository.query(QueryUtil.createIUQuery(jarID, version), new NullProgressMonitor());
      final List<Future<Map<IArtifactDescriptor, File>>> futures = new ArrayList<>();
      for (IInstallableUnit jarIU : P2Util.asIterable(jarQuery))
      {
        Collection<IArtifactKey> artifacts = jarIU.getArtifacts();
        for (IArtifactKey artifactKey : artifacts)
        {
          futures.add(getArtifacts(artifactKey, artifactRepository, artifactCache, cache));
        }
      }

      result = getExecutor().submit(new Callable<URI>()
      {
        @Override
        public URI call() throws Exception
        {
          for (Future<Map<IArtifactDescriptor, File>> future : futures)
          {
            Collection<File> values = future.get().values();
            for (File file : values)
            {
              URI artifactURI = URI.createFileURI(file.toString());
              if ("jar".equals(artifactURI.fileExtension()))
              {
                URI featureXML = URI.createURI("archive:" + artifactURI + "!/feature.xml");
                Document document = loadXML(featureXML);
                Element documentElement = document.getDocumentElement();
                String plugin = documentElement.getAttribute("plugin");
                if (StringUtil.isEmpty(plugin))
                {
                  plugin = baseID;
                }

                IQueryResult<IInstallableUnit> query = metadataRepository.query(QueryUtil.createIUQuery(plugin), new NullProgressMonitor());
                for (IInstallableUnit brandingPlugin : P2Util.asIterable(query))
                {
                  for (IArtifactKey artifactKey : brandingPlugin.getArtifacts())
                  {
                    Future<Map<IArtifactDescriptor, File>> artifactCacheFuture = artifactCache.get(artifactKey);
                    if (artifactCacheFuture != null)
                    {

                      for (File brandingFile : artifactCacheFuture.get().values())
                      {
                        URI brandingArtifactURI = URI.createFileURI(brandingFile.toString());
                        if ("jar".equals(brandingArtifactURI.fileExtension()))
                        {
                          URI aboutINI = URI.createURI("archive:" + brandingArtifactURI + "!/about.ini");
                          try
                          {
                            Properties properties = loadProperties(aboutINI);
                            Object featureImage = properties.get("featureImage");
                            if (featureImage != null)
                            {
                              URI brandingImageURI = URI.createURI("archive:" + brandingArtifactURI + "!/" + featureImage.toString().replaceAll("^/*", ""));
                              return brandingImageURI;
                            }
                          }
                          catch (IOException ex)
                          {
                            //$FALL-THROUGH$
                          }
                        }
                      }
                    }
                  }
                }
              }

              return null;
            }
          }

          return null;
        }
      });

      brandingImages.put(iu, result);
    }

    return result;
  }

  private Properties loadProperties(URI uri) throws IOException
  {
    Properties properties = new Properties();
    InputStream propertiesIn = null;
    try
    {
      propertiesIn = URIConverter.INSTANCE.createInputStream(uri);
      properties.load(propertiesIn);
      return properties;
    }
    finally
    {
      IOUtil.close(propertiesIn);
    }

  }

  private Document loadXML(URI uri) throws IOException, ParserConfigurationException, SAXException
  {
    DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
    InputStream in = null;
    try
    {
      in = URIConverter.INSTANCE.createInputStream(uri);
      Document document = XMLUtil.loadDocument(documentBuilder, in);
      return document;
    }
    finally
    {
      IOUtil.close(in);
    }

  }

  private byte[] getImageBytes(URI imageURI)
  {
    byte[] bytes = imageBytes.get(imageURI);
    if (bytes == null)
    {
      InputStream in = null;
      try
      {
        in = URIConverter.INSTANCE.createInputStream(imageURI);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtil.copy(in, out);
        bytes = out.toByteArray();
      }
      catch (IOException ex)
      {
        System.err.println("###" + ex.getLocalizedMessage());
        bytes = getImageBytes(URI.createURI("https://git.eclipse.org/c/pde/eclipse.pde.ui.git/plain/ui/org.eclipse.pde.ui/icons/obj16/error_st_obj@2x.png"));
      }
      finally
      {
        IOUtil.close(in);
      }

      imageBytes.put(imageURI, bytes);
    }

    return bytes;
  }

  private String getImage(URI imageURI, File cache)
  {
    String result = images.get(imageURI);
    if (result == null || !new File(cache, result).isFile())
    {
      String key = null;
      OutputStream imageOut = null;
      try
      {
        byte[] imageBytes = getImageBytes(imageURI);
        key = IOUtil.encodeFileName(XMLTypeFactory.eINSTANCE.convertBase64Binary(IOUtil.getSHA1(new ByteArrayInputStream(imageBytes))).replace('=', '_'));
        result = images.get(key);
        if (result == null)
        {
          String location = key + '.' + imageURI.fileExtension();
          File locationFile = new File(cache, location);
          imageOut = new FileOutputStream(locationFile);
          IOUtil.copy(new ByteArrayInputStream(imageBytes), imageOut);
          result = location;
          images.put(key, result);
          images.put(imageURI, result);
        }
      }
      catch (IOException ex)
      {
        result = getErrorImage(cache);
        images.put(imageURI, result);
        if (key != null)
        {
          images.put(key, result);
        }
      }
      catch (NoSuchAlgorithmException ex)
      {
        //$FALL-THROUGH$
      }
      finally
      {
        IOUtil.close(imageOut);
      }
    }

    return result;
  }

  private String getOKImage(File cache)
  {
    return getImage(URI.createURI("https://git.eclipse.org/c/platform/eclipse.platform.ui.git/plain/bundles/org.eclipse.ui/icons/full/obj16/activity@2x.png"),
        cache);
  }

  private String getErrorImage(File cache)
  {
    return getImage(URI.createURI("https://git.eclipse.org/c/pde/eclipse.pde.ui.git/plain/ui/org.eclipse.pde.ui/icons/obj16/error_st_obj@2x.png"), cache);
  }

  private String getWarningImage(File cache)
  {
    return getImage(URI.createURI("https://git.eclipse.org/c/pde/eclipse.pde.ui.git/plain/ui/org.eclipse.pde.ui/icons/obj16/warning_st_obj@2x.png"), cache);
  }

  private String getImage(String pluginID, String imagePath, File cache)
  {
    Bundle bundle = Platform.getBundle(pluginID);
    URL imageLocationURL = bundle.getEntry(imagePath);
    URI imagelocationURI = URI.createURI(imageLocationURL.toString());
    return getImage(imagelocationURI, cache);
  }

  private Future<Map<IArtifactDescriptor, File>> getArtifacts(final IArtifactKey artifactKey, final IArtifactRepository artifactRepository,
      Map<IArtifactKey, Future<Map<IArtifactDescriptor, File>>> artifactCache, final File cache)
  {
    Future<Map<IArtifactDescriptor, File>> result = artifactCache.get(artifactKey);
    if (result == null)
    {
      result = getExecutor().submit(new Callable<Map<IArtifactDescriptor, File>>()
      {
        @Override
        public Map<IArtifactDescriptor, File> call() throws Exception
        {
          Map<IArtifactDescriptor, File> artifacts = new LinkedHashMap<>();
          IArtifactDescriptor[] artifactDescriptors = artifactRepository.getArtifactDescriptors(artifactKey);
          for (IArtifactDescriptor artifactDescriptor : artifactDescriptors)
          {
            IArtifactRepository repository = artifactDescriptor.getRepository();
            if (repository instanceof SimpleArtifactRepository)
            {
              SimpleArtifactRepository simpleArtifactRepository = (SimpleArtifactRepository)repository;
              java.net.URI artifactLocation = simpleArtifactRepository.getLocation(artifactDescriptor);
              java.net.URI location = repository.getLocation();
              java.net.URI relativeLocation = location.relativize(artifactLocation);
              File targetLocation = new File(cache, relativeLocation.toString());
              if (!targetLocation.isFile())
              {
                targetLocation.getParentFile().mkdirs();

                String uuid = EcoreUtil.generateUUID();
                File downloadLocation = new File(targetLocation.getPath() + "." + uuid);
                IStatus status = Status.CANCEL_STATUS;
                for (int i = 1; i <= DOWNLOAD_RETRY_COUNT; ++i)
                {
                  FileOutputStream out = null;
                  try
                  {
                    out = new FileOutputStream(downloadLocation);
                    status = artifactRepository.getRawArtifact(artifactDescriptor, out, new NullProgressMonitor());
                    if (!status.isOK())
                    {
                      throw new IOException("Failed to download: '" + downloadLocation + "' because " + status.getMessage());
                    }

                    IOUtil.closeSilent(out);

                    if (!downloadLocation.renameTo(targetLocation))
                    {
                      throw new IOException("Could not rename '" + downloadLocation + "' to '" + targetLocation + "'");
                    }

                    if (relativeLocation.toString().startsWith("binary/"))
                    {
                      ZIPUtil.unzip(targetLocation, new File(cache, "binary/unpacked/" + relativeLocation.toString().substring("binary/".length())));
                    }
                  }
                  catch (Exception ex)
                  {
                    IOUtil.close(out);
                    targetLocation.delete();
                    downloadLocation.delete();
                    if (i == DOWNLOAD_RETRY_COUNT)
                    {
                      new FileOutputStream(targetLocation).close();
                      OutputStream failure = new FileOutputStream(new File(targetLocation + ".fail"));
                      PrintStream printStream = new PrintStream(failure);
                      printStream.print(status);
                      printStream.close();
                      System.err.println("Failed: " + targetLocation);
                      break;
                    }

                    System.err.println("Retrying: " + targetLocation);
                    continue;
                  }
                  finally
                  {
                    downloadLocation.delete();
                    IOUtil.close(out);
                  }

                  if (validZip(targetLocation))
                  {
                    break;
                  }
                }

                IProcessingStepDescriptor[] processingSteps = artifactDescriptor.getProcessingSteps();
                if (processingSteps.length != 0)
                {
                  File targetProcessedLocation = new File(cache, relativeLocation.toString() + PROCESSED);
                  File processedDownloadLocation = new File(targetProcessedLocation.getPath() + "." + uuid);
                  for (int i = 1; i <= DOWNLOAD_RETRY_COUNT; ++i)
                  {
                    FileOutputStream out = null;
                    status = Status.CANCEL_STATUS;
                    try
                    {
                      out = new FileOutputStream(processedDownloadLocation);
                      status = artifactRepository.getArtifact(artifactDescriptor, out, new NullProgressMonitor());
                      if (!status.isOK())
                      {
                        throw new IOException("Failed to download: '" + processedDownloadLocation + "' because " + status.getMessage());
                      }

                      IOUtil.closeSilent(out);

                      if (!processedDownloadLocation.renameTo(targetProcessedLocation))
                      {
                        throw new IOException("Could not rename '" + processedDownloadLocation + "' to '" + targetProcessedLocation + "'");
                      }
                    }
                    catch (Exception ex)
                    {
                      IOUtil.close(out);
                      targetProcessedLocation.delete();
                      processedDownloadLocation.delete();
                      if (i == DOWNLOAD_RETRY_COUNT)
                      {
                        new FileOutputStream(targetProcessedLocation).close();
                        OutputStream failure = new FileOutputStream(new File(targetProcessedLocation + ".fail"));
                        PrintStream printStream = new PrintStream(failure);
                        printStream.print(status);
                        printStream.close();
                        System.err.println("Failed: " + targetProcessedLocation);
                        break;
                      }

                      System.err.println("Retrying: " + targetProcessedLocation);
                      continue;
                    }
                    finally
                    {
                      IOUtil.close(out);
                    }

                    if (validZip(targetProcessedLocation))
                    {
                      break;
                    }
                  }
                }
              }

              artifacts.put(artifactDescriptor, targetLocation);
            }
            else
            {
              throw new RuntimeException("Invalid repository type " + repository);
            }
          }

          return artifacts;
        }
      });

      artifactCache.put(artifactKey, result);
    }

    return result;
  }

  private boolean validZip(File file)
  {
    if (file.length() == 0)
    {
      return false;
    }

    if (file.getName().endsWith(".pack.gz"))
    {
      return true;
    }

    try
    {
      File copy = new File(file.getAbsolutePath() + ".copy");
      IOUtil.copyFile(file, copy);
      ZIPUtil.unzip(copy, new ZIPUtil.UnzipHandler()
      {
        @Override
        public void unzipFile(String name, InputStream zipStream) throws IOException
        {
        }

        @Override
        public void unzipDirectory(String name) throws IOException
        {
        }
      });

      return true;
    }
    catch (IORuntimeException ex)
    {
      return false;
    }
  }

  private Map<File, Future<SignedContent>> getSignedContent(IInstallableUnit iu, final Map<IArtifactKey, Future<Map<IArtifactDescriptor, File>>> artifactCache,
      Map<IInstallableUnit, Map<File, Future<SignedContent>>> signedContentCache)
  {
    Map<File, Future<SignedContent>> artifactSignedContent = signedContentCache.get(iu);
    if (artifactSignedContent == null)
    {
      Map<IInstallableUnit, Map<File, Future<SignedContent>>> result = new LinkedHashMap<>();
      final BundleContext context = P2CorePlugin.INSTANCE.getBundleContext();
      ServiceReference<SignedContentFactory> contentFactoryRef = context.getServiceReference(SignedContentFactory.class);
      final SignedContentFactory verifierFactory = context.getService(contentFactoryRef);
      try
      {
        ExecutorService executor = getExecutor();
        artifactSignedContent = new LinkedHashMap<>();
        for (IArtifactKey artifactKey : iu.getArtifacts())
        {
          Future<Map<IArtifactDescriptor, File>> artifactCacheFuture = artifactCache.get(artifactKey);
          if (artifactCacheFuture != null)
          {
            for (final File file : get(artifactCacheFuture).values())
            {
              Future<SignedContent> signedContent = executor.submit(new Callable<SignedContent>()
              {
                @Override
                public SignedContent call() throws Exception
                {
                  File processedFile = new File(file.toString() + PROCESSED);
                  File targetFile = processedFile.isFile() && processedFile.length() != 0 ? processedFile : file;
                  if (targetFile.toString().endsWith(".pack.gz") || targetFile.length() == 0)
                  {
                    return null;
                  }

                  return verifierFactory.getSignedContent(targetFile);
                }
              });

              artifactSignedContent.put(file, signedContent);

              getIndex(file);
            }
          }

          result.put(iu, artifactSignedContent);
        }

        signedContentCache.put(iu, artifactSignedContent);
      }
      finally
      {
        context.ungetService(contentFactoryRef);
      }
    }

    return artifactSignedContent;
  }

  private static <T> T get(Future<T> future)
  {
    try
    {
      return future.get();
    }
    catch (InterruptedException ex)
    {
      throw new RuntimeException(ex);
    }
    catch (ExecutionException ex)
    {
      throw new RuntimeException(ex);
    }
  }

  private static boolean isAggrconRepositoryURI(URI uri)
  {
    return "file".equals(uri.scheme()) && "aggrcon".equals(uri.fileExtension());
  }

  private static boolean isAggrconRepository(IRepository<?> repository)
  {
    java.net.URI location = repository.getLocation();
    return "file".equals(location.getScheme()) && location.toString().endsWith(".aggrcon") && repository instanceof ICompositeRepository<?>;
  }

  private void loadAggrconChildren(IRepository<?> repository) throws URISyntaxException
  {
    if (isAggrconRepository(repository))
    {
      ICompositeRepository<?> aggrconRepository = (ICompositeRepository<?>)repository;
      Map<String, String> properties = aggrconRepository.getProperties();
      for (Map.Entry<String, String> entry : properties.entrySet())
      {
        String key = entry.getKey();
        if (key.startsWith("http:") || key.startsWith("https:"))
        {
          java.net.URI child = new java.net.URI(key);
          aggrconRepository.addChild(child);
        }
      }
    }
  }

  private IMetadataRepository loadMetadataRepository(IMetadataRepositoryManager manager, URI uri) throws URISyntaxException, ProvisionException
  {
    if (verbose)
    {
      System.out.println("Loading metadata repository '" + uri + "'");
    }

    java.net.URI location = new java.net.URI(uri.toString());
    IMetadataRepository metadataRepository = metadataRepositories.get(location);
    if (metadataRepository == null)
    {
      metadataRepository = manager.loadRepository(location, null);
      loadAggrconChildren(metadataRepository);
      metadataRepositories.put(location, metadataRepository);
    }

    return metadataRepository;
  }

  private IArtifactRepository loadArtifactRepository(IArtifactRepositoryManager manager, URI uri) throws URISyntaxException, ProvisionException
  {
    if (verbose)
    {
      System.out.println("Loading artifact repository '" + uri + "'");
    }

    java.net.URI location = new java.net.URI(uri.toString());
    IArtifactRepository artifactRepository = artifactRepositories.get(location);
    if (artifactRepository == null)
    {
      artifactRepository = manager.loadRepository(location, null);
      loadAggrconChildren(artifactRepository);
      artifactRepositories.put(location, artifactRepository);
      if (artifactRepository instanceof ICompositeRepository<?>)
      {
        ICompositeRepository<?> compositeRepository = (ICompositeRepository<?>)artifactRepository;
        for (java.net.URI child : compositeRepository.getChildren())
        {
          loadArtifactRepository(manager, URI.createURI(child.toString()));
        }
      }
    }

    return artifactRepository;
  }

  private ExecutorService getExecutor()
  {
    if (executor == null)
    {
      executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
    }

    return executor;
  }

  private void shutDownExecutor() throws InterruptedException
  {
    if (executor != null)
    {
      executor.shutdown();
      executor.awaitTermination(10, TimeUnit.MINUTES);
      executor = null;
    }
  }

  public Agent getAgent()
  {
    if (agent == null)
    {
      try
      {
        File agentLocation = File.createTempFile("test-", "-agent");
        agentLocation.delete();
        agentLocation.mkdirs();
        agentLocation.deleteOnExit();

        agent = new AgentImpl(null, agentLocation);
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
    }

    return agent;
  }

  public void setAgent(Agent agent)
  {
    this.agent = agent;
  }

  private IMetadataRepositoryManager getMetadataRepositoryManager()
  {
    return getAgent().getMetadataRepositoryManager();
  }

  private IArtifactRepositoryManager getArtifactRepositoryManager()
  {
    return getAgent().getArtifactRepositoryManager();
  }

  private static String format(float size)
  {
    NumberFormat instance = NumberFormat.getInstance(Locale.US);
    instance.setMaximumFractionDigits(1);
    instance.setRoundingMode(RoundingMode.DOWN);
    if (size < 1024f)
    {
      return instance.format(size);
    }
    else if (size < 1024f * 1024f)
    {
      return instance.format(size / 1024) + "K";
    }
    else if (size < 1024f * 1024f * 1024f)
    {
      return instance.format(size / 1024f / 1024f) + "M";
    }
    else if (size < 1024f * 1024f * 1024f * 1024f)
    {
      return instance.format(size / 1024f / 1024f / 1024f) + "G";
    }
    else
    {
      return instance.format(size / 1024f / 1024f / 1024f / 1024f) + "T";
    }
  }

  private static String getIUID(IInstallableUnit iu)
  {
    return getID(iu.toString());
  }

  private static String getRequirementID(IRequirement requirement)
  {
    return getID(requirement.toString());
  }

  private static String getProvidedCapabilityID(IProvidedCapability capability)
  {
    return getID(capability.toString());
  }

  private static String getID(String literal)
  {
    return literal.replace(' ', '_').replace('"', '_').replace('&', '_').replace('<', '_').replace('\'', '_').replace('>', ' ').replace('#', '_').replace('/',
        '_');
  }

  public interface Reporter
  {
    String getTitle();

    Map<String, String> getBreadcrumbs();

    Map<String, String> getNavigation();

    String getTitle(boolean narrow);

    String getReportBrandingImage();

    String getHelpLink();

    String getHelpImage();

    String getHelpText();

    String getReportSource();

    String getNow();
  }

  public static abstract class Report implements Reporter
  {
    private static final URI NO_LICENSE_URI = URI.createURI("");

    public static class LicenseDetail
    {
      private final URI licenseURL;

      private final ILicense license;

      private final ILicense matchedLicense;

      private final String body;

      private int prefix;

      private int suffix;

      private String replacement;

      private LicenseDetail(URI licenseURL, ILicense license)
      {
        this.licenseURL = licenseURL;
        this.license = license;

        body = license.getBody().trim();
        if (SUA_10.equals(license))
        {
          matchedLicense = SUA_10;
          prefix = suffix = body.length();
        }
        else if (SUA_11.equals(license))
        {
          matchedLicense = SUA_11;
          prefix = suffix = body.length();
        }
        else if (SUA_20.equals(license))
        {
          matchedLicense = SUA_20;
          prefix = suffix = body.length();
        }
        else
        {
          ILicense bestMatch = null;
          String bestMatchBody = null;
          int longest = -1;
          int longestMatchBody = -1;
          for (ILicense otherLicense : new ILicense[] { SUA_10, SUA_11, SUA_20 })
          {
            String otherBody = otherLicense.getBody().trim();
            boolean newLine = false;
            for (int bodyIndex = 0, otherBodyIndex = 0, bodyLength = body.length(), otherBodyLength = otherBody.length(); bodyIndex < bodyLength
                && otherBodyIndex < otherBodyLength; ++bodyIndex, ++otherBodyIndex)
            {
              char bodyChar = body.charAt(bodyIndex);
              char otherBodyChar = otherBody.charAt(otherBodyIndex);

              if (Character.isWhitespace(bodyChar) && Character.isWhitespace(otherBodyChar))
              {
                if (bodyChar == '\n')
                {
                  newLine = true;
                }

                while (++bodyIndex < bodyLength)
                {
                  bodyChar = body.charAt(bodyIndex);
                  if (Character.isWhitespace(bodyChar))
                  {
                    if (bodyChar == '\n')
                    {
                      newLine = true;
                    }
                  }
                  else
                  {
                    break;
                  }
                }

                while (++otherBodyIndex < otherBodyLength)
                {
                  otherBodyChar = otherBody.charAt(otherBodyIndex);
                  if (!Character.isWhitespace(otherBodyChar))
                  {
                    break;
                  }
                }
              }

              if (bodyChar != otherBodyChar)
              {
                break;
              }

              if (bodyIndex > longest && newLine)
              {
                bestMatch = otherLicense;
                bestMatchBody = otherBody;
                longest = bodyIndex;
                longestMatchBody = otherBodyIndex;
              }
            }
          }

          if (bestMatch != null)
          {
            prefix = longest + 1;
            suffix = body.length();
            for (int bodyIndex = suffix - 1, otherBodyIndex = bestMatchBody.length() - 1; bodyIndex >= 0 && otherBodyIndex >= 0; --bodyIndex, --otherBodyIndex)
            {
              char bodyChar = body.charAt(bodyIndex);
              char otherBodyChar = bestMatchBody.charAt(otherBodyIndex);

              if (Character.isWhitespace(bodyChar) && Character.isWhitespace(otherBodyChar))
              {
                while (--bodyIndex >= 0)
                {
                  bodyChar = body.charAt(bodyIndex);
                  if (!Character.isWhitespace(bodyChar))
                  {
                    break;
                  }
                }

                while (--otherBodyIndex >= 0)
                {
                  otherBodyChar = bestMatchBody.charAt(otherBodyIndex);
                  if (!Character.isWhitespace(otherBodyChar))
                  {
                    break;
                  }
                }
              }

              if (bodyChar != otherBodyChar)
              {
                suffix = bodyIndex + 1;
                replacement = bestMatchBody.substring(longestMatchBody + 1, otherBodyIndex + 1);
                break;
              }
            }
          }
          else
          {
            prefix = 0;
            suffix = body.length();
          }

          if (SUA_10.equals(bestMatch))
          {
            matchedLicense = SUA_10;
          }
          else if (SUA_11.equals(bestMatch))
          {
            matchedLicense = SUA_11;
          }
          else if (SUA_20.equals(bestMatch))
          {
            matchedLicense = SUA_20;
          }
          else
          {
            matchedLicense = null;
          }
        }
      }

      public URI getLicenseURL()
      {
        return licenseURL;
      }

      public ILicense getLicense()
      {
        return license;
      }

      public String getUUID()
      {
        return license.getUUID();
      }

      public ILicense getMatchedLicense()
      {
        return matchedLicense;
      }

      public String getMatchingPrefix()
      {
        return body.substring(0, prefix);
      }

      public String getMismatching()
      {
        return body.substring(prefix, suffix);
      }

      public String getReplacement()
      {
        return replacement == null ? "" : replacement;
      }

      public String getMatchingSuffix()
      {
        return body.substring(suffix);
      }

      public boolean isSUA()
      {
        return SUAS.contains(license);
      }

      public boolean isMatchedSUA()
      {
        return SUAS.contains(matchedLicense);
      }

      public String getVersion()
      {
        int index = SUAS.indexOf(license);
        if (index == -1)
        {
          index = SUAS.indexOf(matchedLicense);
        }

        switch (index)
        {
          case 0:
          {
            return "SUA 1.0";
          }
          case 1:
          {
            return "SUA 1.1";
          }
          case 2:
          {
            return "SUA 2.0";
          }
        }

        return getSummary();
      }

      public String getSummary()
      {
        return license.getBody().replaceAll("[\n\r].*", "");
      }
    }

    public static final ILicense NO_LICENSE;

    static
    {
      try
      {
        NO_LICENSE = MetadataFactory.createLicense(new java.net.URI("https://wwww.eclipse.org"), "No License");
      }
      catch (URISyntaxException ex)
      {
        throw new RuntimeException(ex);
      }
    }

    public static final ILicense SUA_10 = load(URI.createURI("https://www.eclipse.org/legal/epl-v10.html"),
        URI.createURI("https://raw.githubusercontent.com/eclipse-cbi/epl-license-feature/license-1.0.0.v20131003-1638/org.eclipse.license/feature.properties"));

    public static final ILicense SUA_11 = load(URI.createURI("https://www.eclipse.org/legal/epl-v10.html"),
        URI.createURI("https://raw.githubusercontent.com/eclipse-cbi/epl-license-feature/license-1.0.1.v20140414-1359/org.eclipse.license/feature.properties"));

    public static final ILicense SUA_20 = load(URI.createURI("https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html"),
        URI.createURI("https://raw.githubusercontent.com/eclipse-cbi/epl-license-feature/license-2.0.1.v20180423-1114/org.eclipse.license/feature.properties"));

    public static final List<ILicense> SUAS = Collections.unmodifiableList(Arrays.asList(new ILicense[] { SUA_10, SUA_11, SUA_20 }));

    public static final int RETAINED_NIGHTLY_BUILDS = -1;

    @Override
    public abstract String getReportSource();

    @Override
    public abstract String getReportBrandingImage();

    public abstract Map<Report.LicenseDetail, Set<IInstallableUnit>> getLicenses();

    public abstract List<String> getFeatures(Iterable<IInstallableUnit> features);

    @Override
    public abstract Map<String, String> getBreadcrumbs();

    public List<String> getXML(IInstallableUnit iu)
    {
      return getXML(iu, null);
    }

    public abstract List<String> getXML(IInstallableUnit iu, Map<String, String> replacements);

    public abstract String getErrorImage();

    public abstract Report getParent();

    public abstract List<Report> getChildren();

    public abstract String getBrandingImage(IInstallableUnit iu);

    public abstract boolean hasBrandingImage(IInstallableUnit iu);

    public abstract boolean hasBrokenBrandingImage(IInstallableUnit iu);

    public abstract Map<String, Set<IInstallableUnit>> getFeatureProviders();

    public abstract Set<String> getBrandingImages(Collection<IInstallableUnit> ius);

    public abstract String getSignedImage(boolean signed);

    public abstract String getLicenseImage();

    public abstract String getRepositoryImage();

    public abstract String getProviderImage();

    public abstract String getFeatureImage();

    public abstract String getProductImage();

    public abstract String getCategoryImage();

    public abstract String getBundleImage();

    public abstract Map<String, String> getCertificateComponents(Certificate certificate);

    public abstract boolean isExpired(Certificate certificate);

    public abstract Map<List<Certificate>, Map<String, IInstallableUnit>> getCertificates();

    public abstract List<Certificate> getCertificates(IInstallableUnit iu);

    public abstract Map<String, Boolean> getIUArtifacts(IInstallableUnit iu);

    public abstract String getRepositoryURL(String artifact);

    public abstract List<String> getArtifactStatus(String artifact);

    public abstract String getArtifactImage(String artifact);

    public abstract String getArtifactSize(String artifact);

    public abstract Map<String, Set<Version>> getIUVersions();

    public abstract Set<IInstallableUnit> getUnsignedIUs();

    public abstract Set<IInstallableUnit> getBadProviderIUs();

    public abstract Set<IInstallableUnit> getBadLicenseIUs();

    public abstract Set<IInstallableUnit> getBrokenBrandingIUs();

    public abstract Set<IInstallableUnit> getClassContainingIUs();

    public abstract Set<IInstallableUnit> getPluginsWithMissingPackGZ();

    public abstract boolean isSimple();

    public abstract boolean isRoot();

    public abstract String getDate();

    @Override
    public String getNow()
    {
      return new SimpleDateFormat("yyyy'-'MM'-'dd' at 'HH':'mm ").format(System.currentTimeMillis());
    }

    public abstract List<String> getFeatures();

    public abstract List<IInstallableUnit> getFeatureIUs();

    public boolean isFeature(IInstallableUnit iu)
    {
      String id = iu.getId();
      return id.endsWith(Requirement.FEATURE_SUFFIX);
    }

    public abstract List<Report.LicenseDetail> getLicenses(IInstallableUnit iu);

    public String getName(IInstallableUnit iu, boolean defaultToID)
    {
      String name = iu.getProperty(IInstallableUnit.PROP_NAME, null);
      if (name == null)
      {
        name = iu.getId();
      }
      return name;
    }

    public String getVersion(IInstallableUnit iu)
    {
      return iu.getVersion().toString();
    }

    public String getNameAndVersion(IInstallableUnit iu)
    {
      String name = iu.getProperty(IInstallableUnit.PROP_NAME, null);
      name += " " + iu.getVersion();
      return name;
    }

    public abstract Map<String, List<String>> getBundles();

    public abstract boolean isDuplicationExpected(String id);

    public abstract Set<IInstallableUnit> getAllIUs();

    public Set<IInstallableUnit> getSortedByName(Collection<? extends IInstallableUnit> ius)
    {
      TreeSet<IInstallableUnit> result = new TreeSet<>(NAME_VERSION_COMPARATOR);
      result.addAll(ius);
      return result;
    }

    public abstract Set<IInstallableUnit> getProducts();

    public Set<IInstallableUnit> getCategories()
    {
      Set<IInstallableUnit> categories = new TreeSet<>();
      for (IInstallableUnit iu : getAllIUs())
      {
        if (isCategory(iu))
        {
          categories.add(iu);
        }
      }

      return categories;
    }

    public boolean isCategory(IInstallableUnit iu)
    {
      return "true".equals(iu.getProperty(MetadataFactory.InstallableUnitDescription.PROP_TYPE_CATEGORY));
    }

    public boolean isGroup(IInstallableUnit iu)
    {
      return "true".equals(iu.getProperty(MetadataFactory.InstallableUnitDescription.PROP_TYPE_GROUP));
    }

    public boolean isProduct(IInstallableUnit iu)
    {
      return "true".equals(iu.getProperty(MetadataFactory.InstallableUnitDescription.PROP_TYPE_PRODUCT));
    }

    public boolean isPlugin(IInstallableUnit iu)
    {
      return !isCategory(iu) && !isGroup(iu);
    }

    public abstract String getIUImage(IInstallableUnit iu);

    public abstract Set<IInstallableUnit> getResolvedRequirements(IInstallableUnit iu);

    public String getRelativeIndexURL()
    {
      String location = isRoot() ? "index.html" : IOUtil.encodeFileName(getSiteURL()) + ".html";
      return location;
    }

    protected String getRelativeIUReportURL(IInstallableUnit iu)
    {
      URI indexURL = URI.createURI(getRelativeIndexURL());
      return indexURL.trimFileExtension().appendSegment(getIUID(iu)).appendFileExtension("html").toString();
    }

    public String getIUID(IInstallableUnit iu)
    {
      return RepositoryIntegrityAnalyzer.getIUID(iu);
    }

    public String getFolderID(String folder)
    {
      return "_" + new File(folder).getName().replace('.', '_').replace('\'', '_');
    }

    @Override
    public abstract Map<String, String> getNavigation();

    public abstract String getSiteURL();

    public abstract String getMetadataXML();

    public abstract String getArtifactML();

    @Override
    public abstract String getTitle(boolean narrow);

    @Override
    public abstract String getTitle();

    private static ILicense load(URI licenseURI, URI uri)
    {
      InputStream in = null;
      try
      {
        in = URIConverter.INSTANCE.createInputStream(uri);
        Properties properties = new Properties();
        Reader reader = new InputStreamReader(in, "UTF-8");
        properties.load(reader);
        Object license = properties.get("license");
        return MetadataFactory.createLicense(new java.net.URI(licenseURI.toString()), license.toString());
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
      catch (URISyntaxException ex)
      {
        throw new IORuntimeException(ex);
      }
      finally
      {
        IOUtil.closeSilent(in);
      }
    }

    public abstract List<IUReport> getIUReports();
  }

  public static abstract class IUReport implements Reporter
  {
    public abstract Report getReport();

    public abstract String getRelativeReportURL();

    public abstract IInstallableUnit getIU();

    public abstract String getDescription();

    public abstract String getProvider();
  }

  public class IndexReport implements Reporter
  {
    private final IndexReport parent;

    private final String reportSource;

    private final String reportBranding;

    private final File folder;

    private final Set<File> reportLocations;

    private final Map<String, String> allReports;

    private final File publishLocation;

    private final Set<File> reportsWithErrors;

    private List<IndexReport> children;

    public IndexReport(IndexReport parent, String reportSource, String reportBranding, File folder, File publishLocation, Set<File> reportLocations,
        Map<String, String> allReports, Set<File> reportsWithErrors)
    {
      this.parent = parent;
      this.reportSource = reportSource;
      this.reportBranding = reportBranding;
      this.folder = folder;
      this.publishLocation = publishLocation;
      this.reportLocations = reportLocations;
      this.allReports = allReports;
      this.reportsWithErrors = reportsWithErrors;
    }

    @Override
    public String getHelpLink()
    {
      return parent == null && aggregator ? "https://wiki.eclipse.org/Oomph_Repository_Analyzer#Simultaneous_Release"
          : "https://wiki.eclipse.org/Oomph_Repository_Analyzer#Description";
    }

    @Override
    public String getHelpImage()
    {
      return getImage(URI.createURI("https://git.eclipse.org/c/platform/eclipse.platform.ui.git/plain/bundles/org.eclipse.jface/icons/full/help@2x.png"),
          folder);
    }

    @Override
    public String getHelpText()
    {
      return parent == null && aggregator ? "simrel aggregator" : "folder index";
    }

    @Override
    public String getNow()
    {
      return new SimpleDateFormat("yyyy'-'MM'-'dd' at 'HH':'mm ").format(System.currentTimeMillis());
    }

    public File getFolder()
    {
      return folder;
    }

    public File getPublishLocation()
    {
      return publishLocation;
    }

    @Override
    public String getTitle()
    {
      return folder.getName();
    }

    @Override
    public Map<String, String> getBreadcrumbs()
    {
      Map<String, String> result = parent == null ? new LinkedHashMap<>() : parent.getBreadcrumbs();
      for (Map.Entry<String, String> entry : result.entrySet())
      {
        String href = entry.getValue();
        entry.setValue(href == null ? "../index.html" : "../" + href);
      }

      result.put(folder.getName(), null);
      return result;
    }

    @Override
    public Map<String, String> getNavigation()
    {
      Map<String, String> result = new LinkedHashMap<>();
      for (File file : listSortedFiles(folder))
      {
        if (file.isDirectory() && (!reportLocations.contains(file) || parent != null || aggregator && file.getName().endsWith(".aggrcon")))
        {
          String name = file.getName();
          String label = name;
          if (reportsWithErrors.contains(file))
          {
            String errorImage = getErrorImage(getFolder());
            label = "<img style=\"position: static;\" class=\"fit-image\" src=\"" + errorImage + "\"/> " + label.replaceAll("\\.aggrcon$", "");
          }
          else if (name.endsWith(".aggrcon"))
          {
            String okImage = getOKImage(getFolder());
            label = "<img style=\"position: static;\" class=\"fit-image\" src=\"" + okImage + "\"/> " + label.replaceAll("\\.aggrcon$", "");
          }

          result.put(name + "/index.html", label);
        }
      }

      return result;
    }

    @Override
    public String getTitle(boolean narrow)
    {
      return getTitle();
    }

    @Override
    public String getReportSource()
    {
      return reportSource;
    }

    @Override
    public String getReportBrandingImage()
    {
      return getImage(URI.createURI(reportBranding), folder);
    }

    public List<IndexReport> getChildren()
    {
      if (children == null)
      {
        children = new ArrayList<>();
        for (File file : listSortedFiles(folder))
        {
          if (file.isDirectory() && !reportLocations.contains(file))
          {
            File childPulishLocation = publishLocation == null ? null : new File(publishLocation, file.getName());
            children.add(new IndexReport(this, reportSource, reportBranding, file, childPulishLocation, reportLocations, null, reportsWithErrors));
          }
        }
      }

      return children;
    }

    public Map<String, String> getAllReports()
    {
      if (allReports != null)
      {
        Map<String, String> result = new TreeMap<>();
        java.net.URI baseURI = folder.toURI();
        for (Map.Entry<String, String> entry : allReports.entrySet())
        {
          String site = entry.getKey();
          if (!site.startsWith("file:"))
          {
            result.put(site, baseURI.relativize(new File(entry.getValue()).toURI()).toString());
          }
        }

        return result;
      }

      return null;
    }
  }

  public static class InstallableUnitWriter extends MetadataWriter
  {
    private ByteArrayOutputStream output;

    public InstallableUnitWriter()
    {
      this(new ByteArrayOutputStream());
    }

    private InstallableUnitWriter(OutputStream output)
    {
      super(output, null);
      this.output = (ByteArrayOutputStream)output;
    }

    public String toHTML(IInstallableUnit iu, ValueHandler valueHandler)
    {
      try
      {
        super.writeInstallableUnit(iu);
        flush();
        byte[] bytes = output.toByteArray();
        output.reset();
        Resource resource = new HTMLResource(valueHandler);
        resource.load(new ByteArrayInputStream(bytes), null);
        StringWriter writer = new StringWriter();
        resource.save(new URIConverter.WriteableOutputStream(writer, "UTF-8"), null);
        return writer.toString();
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
      finally
      {
        flush();
        output.reset();
      }
    }

    public String expandEntities(String xmlElementContent)
    {
      Resource resource = new HTMLResource(null);
      String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<element>" + xmlElementContent + "</element>";
      ReadableInputStream input = new URIConverter.ReadableInputStream(xml);
      try
      {
        resource.load(input, null);
        EObject documentRoot = resource.getContents().get(0);
        AnyType anyType = (AnyType)documentRoot.eContents().get(0);
        Object value = anyType.getMixed().get(0).getValue();
        return value.toString();
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
    }

    public static class ValueHandler
    {
      protected String getCurrentElementName(List<String> elementNames)
      {
        int size = elementNames.size();
        return size == 0 ? "" : elementNames.get(size - 1);
      }

      public String handleAttributeValue(List<String> elementNames, String attributeName, String attributeValue)
      {
        return attributeValue;
      }

      public String handleElementContent(List<String> elementNames, String elementContent)
      {
        return elementContent;
      }

      public void startElement(List<String> elementNames, String uuid)
      {
      }
    }

    private static class HTMLResource extends XMLResourceImpl
    {
      private static final URI RESOURCE_URI = URI.createURI("iu.xml");

      private static final XMLParserPool XML_PARSER_POOL = new XMLParserPoolImpl();

      private final ValueHandler valueHandler;

      public HTMLResource(ValueHandler valueHandler)
      {
        super(RESOURCE_URI);
        this.valueHandler = valueHandler;

        setEncoding("UTF-8");

        Map<Object, Object> defaultLoadOptions = getDefaultLoadOptions();
        BasicExtendedMetaData extendedMetaData = new BasicExtendedMetaData();
        defaultLoadOptions.put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
        getDefaultSaveOptions().put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
        defaultLoadOptions.put(XMLResource.OPTION_USE_LEXICAL_HANDLER, Boolean.TRUE);
        defaultLoadOptions.put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
        defaultLoadOptions.put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
        defaultLoadOptions.put(XMLResource.OPTION_USE_PARSER_POOL, XML_PARSER_POOL);
        XMLOptions xmlOptions = new XMLOptionsImpl();
        xmlOptions.setProcessAnyXML(true);
        defaultLoadOptions.put(XMLResource.OPTION_XML_OPTIONS, xmlOptions);
      }

      @Override
      protected XMLSave createXMLSave()
      {
        return new HTMLSave(createXMLHelper(), valueHandler);
      }

      private static class HTMLSave extends XMLSaveImpl
      {
        private final ValueHandler valueHandler;

        public HTMLSave(XMLHelper helper, ValueHandler valueHandler)
        {
          super(helper);
          this.valueHandler = valueHandler;
        }

        @Override
        protected void init(XMLResource resource, Map<?, ?> options)
        {
          super.init(resource, options);
          doc = new HTMLString(valueHandler);
          declareXML = false;
        }

        private static class HTMLString extends XMLString
        {
          private static final String LINE_SEPARATOR = "\n";

          private static final String LINE_ELEMENT_NAME = "span";

          private static final String LINE_ELEMENT_START_PREFIX = "<" + LINE_ELEMENT_NAME;

          private static final String LINE_ELEMENT_END = "</" + LINE_ELEMENT_NAME + ">";

          private static final String LINE_ELEMENT_SEPARATOR_PREFIX = LINE_ELEMENT_END + LINE_SEPARATOR + LINE_ELEMENT_START_PREFIX;

          private static final String ATTRIBUTE_QUOTE = "<span class='xml-token'>\"</span>";

          private static final String ATTRIBUTE_EQUALS = "<span class='xml-token'>=</span>";

          private static final String ELEMENT_START = "<span class='xml-token'>&lt;</span>";

          private static final String ELEMENT_END = "<span class='xml-token'>&gt;</span>";

          private static final String EMPTY_ELEMENT_END = "<span class='xml-token'>/&gt;</span>";

          private static final String ELEMENT_CLOSE_START = "<span class='xml-token'>&lt;/</span>";

          private static final String ATTRIBUTE_NAME_START = "<span class='xml-attribute'>";

          private static final String ATTRIBUTE_NAME_END = "</span>";

          private static final String ELEMENT_NAME_START = "<span class='xml-element'>";

          private static final String ELEMENT_NAME_END = "</span>";

          private static final String ATTRIBUTE_VALUE_START = "<span class='xml-attribute-value'>";

          private static final String ATTRIBUTE_VALUE_END = "</span>";

          private static final Pattern ELEMENT_VALUE_PATTERN = Pattern.compile("((\\S+[\\s&&[^\n\r]]*)+)");

          private static final String ELEMENT_VALUE_REPLACEMENT = "<span class='xml-element-value'>$1</span>";

          private static final String ATTRIBUTE_SEPARATOR = " ";

          private static final long serialVersionUID = 1L;

          private final ValueHandler valueHandler;

          private String uuid;

          public HTMLString(ValueHandler valueHandler)
          {
            super(Integer.MAX_VALUE);
            this.valueHandler = valueHandler;
            setLineSeparator(LINE_SEPARATOR);
            add(LINE_ELEMENT_START_PREFIX);
            addLineID();
          }

          private void addLineID()
          {
            add(" id=\"");
            uuid = EcoreUtil.generateUUID();
            add(uuid);
            add("\">");
          }

          private String replaceLineSeparator(String string)
          {
            Matcher matcher = Pattern.compile("\r?\n").matcher(string);
            StringBuffer result = new StringBuffer();
            if (matcher.find())
            {
              do
              {
                matcher.appendReplacement(result, LINE_ELEMENT_SEPARATOR_PREFIX);
                result.append(" id=\"");
                uuid = EcoreUtil.generateUUID();
                result.append(uuid);
                result.append("\">");
              } while (matcher.find());
              matcher.appendTail(result);
              return result.toString();
            }

            return string;
          }

          private String surroundElementContent(String string)
          {
            return ELEMENT_VALUE_PATTERN.matcher(string).replaceAll(ELEMENT_VALUE_REPLACEMENT);
          }

          @Override
          public void add(String string)
          {
            super.add(replaceLineSeparator(string));
          }

          @Override
          public void addText(String string)
          {
            String elementContent = valueHandler.handleElementContent(elementNames, string);
            if (string.equals(elementContent))
            {
              super.addText(replaceLineSeparator(surroundElementContent(string)));
            }
            else
            {
              super.addText(elementContent);
            }
          }

          @Override
          public void addAttribute(String name, String value)
          {
            add(ATTRIBUTE_SEPARATOR);
            add(ATTRIBUTE_NAME_START);
            add(name);
            add(ATTRIBUTE_NAME_END);
            add(ATTRIBUTE_EQUALS);
            add(ATTRIBUTE_QUOTE);
            String attributeValue = valueHandler.handleAttributeValue(elementNames, name, value);
            if (attributeValue.equals(value))
            {
              add(ATTRIBUTE_VALUE_START);
              add(value);
              add(ATTRIBUTE_VALUE_END);
            }
            else
            {
              add(attributeValue);
            }
            add(ATTRIBUTE_QUOTE);
          }

          @Override
          protected void closeStartElement()
          {
            add(ELEMENT_END);
            lastElementIsStart = false;
          }

          @Override
          public void startElement(String name)
          {
            if (lastElementIsStart)
            {
              closeStartElement();
            }
            elementNames.add(name);
            if (name != null)
            {
              ++depth;
              add(ELEMENT_START);
              add(ELEMENT_NAME_START);
              add(name);
              valueHandler.startElement(elementNames, uuid);
              add(ELEMENT_NAME_END);
              lastElementIsStart = true;
            }

            mixed.add(Boolean.TRUE);
          }

          @Override
          public void endElement()
          {
            if (lastElementIsStart)
            {
              endEmptyElement();
            }
            else
            {
              String name = removeLast();
              if (name != null)
              {
                add(ELEMENT_CLOSE_START);
                add(ELEMENT_NAME_START);
                add(name);
                add(ELEMENT_NAME_END);
                add(ELEMENT_END);
              }
            }

            if (elementNames.isEmpty())
            {
              add(LINE_ELEMENT_END);
            }
          }

          @Override
          public void endContentElement(String content)
          {
            add(ELEMENT_END);
            super.addText(replaceLineSeparator(surroundElementContent(content)));
            add(ELEMENT_CLOSE_START);
            String name = removeLast();
            add(ELEMENT_NAME_START);
            add(name);
            add(ELEMENT_NAME_END);
            add(ELEMENT_END);
            lastElementIsStart = false;
          }

          @Override
          public void endEmptyElement()
          {
            removeLast();
            add(EMPTY_ELEMENT_END);
            lastElementIsStart = false;
          }
        }
      }
    }
  }
}
