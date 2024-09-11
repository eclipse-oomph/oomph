/*
 * Copyright (c) 2019 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.ResourceMirror;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Factory;
import org.eclipse.oomph.setup.p2.SetupP2Package;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.UserCallback;
import org.eclipse.oomph.util.WorkerPool;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.GenericXMLResourceFactoryImpl;
import org.eclipse.emf.ecore.xml.type.AnyType;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IInstallableUnitPatch;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.ICompositeRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import javax.net.ssl.SSLHandshakeException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Ed Merks
 */
@SuppressWarnings("nls")
public class MarketplaceCatalogGenerator implements IApplication
{
  private static final String PROCESSING_ANNOTATION_SOURCE = "Processing";

  private static final String CATEGORIES_ANNOTATION_SOURCE = "http://www.eclipse.org/oomph/setup/Categories";

  private static final String TAGS_ANNOTATION_SOURCE = "http://www.eclipse.org/oomph/setup/Tags";

  private static final String UNSELECTED_ANNOTATION_SOURCE = "Unselected";

  private static final String SELECTED_ANNOTATION_SOURCE = "Selected";

  private File outputLocation;

  private URI outputLocationURI;

  private File poolLocation;

  private ResourceSet resourceSet;

  private IMetadataRepositoryManager manager;

  private long await = 60;

  @Override
  @SuppressWarnings("restriction")
  public Object start(IApplicationContext context) throws Exception
  {
    String[] arguments = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
    Map<String, URI> nodeURIs = new LinkedHashMap<String, URI>();
    outputLocation = File.createTempFile("marketplace", "-report");
    boolean skip = false;
    if (arguments != null)
    {
      for (int i = 0; i < arguments.length; ++i)
      {
        String option = arguments[i];
        if ("-output".equals(option) || "-o".equals(option))
        {
          outputLocation = new File(arguments[++i]).getCanonicalFile();
        }
        else if ("-skip".equals(option) || "-s".equals(option))
        {
          skip = true;
        }
        if ("-listing".equals(option) || "-l".equals(option))
        {
          nodeURIs.put("", URI.createURI(arguments[++i]));
        }
        if ("-await".equals(option))
        {
          await = Integer.parseInt(arguments[++i]);
        }
      }
    }

    outputLocation.mkdirs();
    if (!outputLocation.isDirectory())
    {
      throw new RuntimeException("Cannot create '" + outputLocation + "'");
    }

    File userHome = new File(outputLocation, "home");
    userHome.mkdirs();
    System.setProperty("user.home", userHome.toString());
    System.out.println("Local user home: '" + userHome + "'");

    outputLocationURI = URI.createFileURI(outputLocation.toString());

    poolLocation = new File(userHome, ".p2/pool");

    resourceSet = SetupCoreUtil.createResourceSet();

    manager = getMetadataRepositoryManager();

    try
    {
      resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITH_ETAG_CHECKING);

      if (!skip)
      {
        perform(nodeURIs);
      }

      if (Boolean.FALSE)
      {
        generateReport();
      }

      generateMinimizedResult();

      return null;
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
      throw new RuntimeException(throwable);
    }
    finally
    {
      Job.getJobManager().join(org.eclipse.equinox.internal.p2.engine.ProfilePreferences.PROFILE_SAVE_JOB_FAMILY, new NullProgressMonitor());
    }
  }

  private URI getJREURI()
  {
    return outputLocationURI.appendSegment("jre");
  }

  private URI getTargetResourceURI()
  {
    return outputLocationURI.appendSegment("generated.setup");
  }

  private URI getResolvedResourceURI()
  {
    return outputLocationURI.appendSegment("resolved.setup");
  }

  private URI getResolvedMinimizedResourceURI()
  {
    return outputLocationURI.appendSegment("resolved-minimized.setup");
  }

  @SuppressWarnings("restriction")
  public void perform(Map<String, URI> nodeURIs) throws Exception
  {
    boolean normalizeP2Tasks = true;

    resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new GenericXMLResourceFactoryImpl());

    long startListings = System.currentTimeMillis();
    if (nodeURIs.isEmpty())
    {
      long startCategories = System.currentTimeMillis();

      Set<URI> uris = new LinkedHashSet<URI>();
      {
        URI baseSearchURI = URI.createURI("https://marketplace.eclipse.org/api/p/search/apachesolr_search/%20");
        Resource resource = resourceSet.getResource(baseSearchURI, true);
        EObject documentRoot = resource.getContents().get(0);
        EObject marketplace = documentRoot.eContents().get(0);
        String searchCount = (String)get(marketplace, "count");
        if (searchCount == null)
        {
          searchCount = (String)get(marketplace.eContents().get(0), "count");
        }

        int count = Integer.parseInt(searchCount);
        System.err.println("Listing count: " + count);
        for (int i = 0; i * 10 < count; ++i)
        {
          URI listingURI = baseSearchURI.appendQuery("page_num=" + i);
          uris.add(listingURI);
        }
      }

      loadResources(uris);

      System.out.println("Gathering " + uris.size() + " categories: " + (startListings - startCategories) / 1000);

      for (URI listingURI : uris)
      {
        System.out.println("Loading " + listingURI);
        Resource listingResource = resourceSet.getResource(listingURI, true);
        EObject documentRoot = listingResource.getContents().get(0);
        List<AnyType> searches = get(documentRoot, "search");
        if (searches == null)
        {
          EObject marketplace = documentRoot.eContents().get(0);
          searches = get(marketplace, "search");
        }

        for (AnyType search : searches)
        {
          List<AnyType> nodes = get(search, "node");
          for (AnyType node : nodes)
          {
            String updateURL = getContent(node, "updateurl");
            if (StringUtil.isEmpty(updateURL))
            {
              continue;
            }

            String id = get(node, "id");
            URI nodeURI = nodeURIs.get(id);
            if (nodeURI == null)
            {
              String nodeURL = get(node, "url");
              nodeURI = URI.createURI(nodeURL);
              nodeURIs.put(id, nodeURI);
            }
          }
        }
      }
    }

    Map<String, String> platformVersions = new LinkedHashMap<String, String>();
    // platformVersions.put("platform.version=4.2", "Juno");
    // platformVersions.put("platform.version=4.3", "Kepler");
    // platformVersions.put("platform.version=4.4", "Luna");
    // platformVersions.put("platform.version=4.5", "Mars");
    // platformVersions.put("platform.version=4.6", "Neon");
    // platformVersions.put("platform.version=4.7", "Oxygen");
    // platformVersions.put("platform.version=4.8", "Photon");
    // platformVersions.put("platform.version=4.9", "2018-09");
    // platformVersions.put("platform.version=4.10", "2018-12");
    // platformVersions.put("platform.version=4.11", "2019-03");
    // platformVersions.put("platform.version=4.12", "2019-06");
    // platformVersions.put("platform.version=4.13", "2019-09");
    // platformVersions.put("platform.version=4.14", "2019-12");
    // platformVersions.put("platform.version=4.15", "2020-03");
    // platformVersions.put("platform.version=4.16", "2020-06");
    // platformVersions.put("platform.version=4.17", "2020-09");
    // platformVersions.put("platform.version=4.18", "2020-12");
    // platformVersions.put("platform.version=4.19", "2021-03");
    // platformVersions.put("platform.version=4.20", "2021-06");
    platformVersions.put("platform.version=4.21", "2021-09");
    platformVersions.put("platform.version=4.22", "2021-12");
    platformVersions.put("platform.version=4.23", "2022-03");
    platformVersions.put("platform.version=4.24", "2022-06");
    platformVersions.put("platform.version=4.25", "2022-09");
    platformVersions.put("platform.version=4.26", "2022-12");
    platformVersions.put("platform.version=4.27", "2023-03");
    platformVersions.put("platform.version=4.28", "2023-06");
    platformVersions.put("platform.version=4.29", "2023-09");
    platformVersions.put("platform.version=4.30", "2023-12");
    platformVersions.put("platform.version=4.31", "2024-03");
    platformVersions.put("platform.version=4.32", "2024-06");
    platformVersions.put("platform.version=4.33", "2024-09");
    platformVersions.put("platform.version=4.34", "2024-12");

    Set<URI> nodeQueryURIs = new LinkedHashSet<URI>();
    {
      String[] querySegments = new String[] { "api", "p" };
      for (URI nodeURI : nodeURIs.values())
      {
        for (String platformVersion : platformVersions.keySet())
        {
          nodeQueryURIs.add(nodeURI.appendSegments(querySegments).appendQuery(platformVersion));
        }
      }

      loadResources(nodeQueryURIs);
    }

    long startRepositoryLoads = System.currentTimeMillis();
    System.out.println("Gathering " + nodeQueryURIs.size() + " listings: " + (startRepositoryLoads - startListings) / 1000 + " seconds");
    {
      Set<String> updateURLs = new LinkedHashSet<String>();
      for (URI listingURI : nodeQueryURIs)
      {
        System.out.println("Loading: " + listingURI);
        Resource listingResource = resourceSet.getResource(listingURI, true);
        EList<EObject> contents = listingResource.getContents();
        if (contents.isEmpty())
        {
          System.err.println("Empty listing: '" + listingURI + "'");
          continue;
        }

        EObject documentRoot = contents.get(0);
        List<AnyType> nodes = get(documentRoot, "node");
        if (nodes == null)
        {
          EObject marketplace = documentRoot.eContents().get(0);
          nodes = get(marketplace, "node");
        }

        for (AnyType node : nodes)
        {
          String updateURL = getContent(node, "updateurl");
          if (StringUtil.isEmpty(updateURL))
          {
            continue;
          }

          if (updateURL.endsWith("/"))
          {
            updateURL = updateURL.substring(0, updateURL.length() - 1);
          }

          updateURLs.add(updateURL);
        }
      }

      System.out.println("Loading repositories");
      Resource productCatalogResource = resourceSet.getResource(URI.createURI("index:/org.eclipse.products.setup"), true);
      for (TreeIterator<EObject> it = productCatalogResource.getAllContents(); it.hasNext();)
      {
        EObject eObject = it.next();
        if (eObject instanceof Repository)
        {
          updateURLs.add(((Repository)eObject).getURL());
        }
      }

      for (String version : platformVersions.values())
      {
        updateURLs.add("https://download.eclipse.org/releases/" + version.toLowerCase());
      }

      updateURLs.add("https://download.eclipse.org/oomph/updates/milestone/latest");

      RepositoryLoader repositoryLoader = new RepositoryLoader(this);
      repositoryLoader.perform(updateURLs);
      repositoryLoader.dispose();
    }

    System.out.println("Free memory MB: " + Runtime.getRuntime().freeMemory() / 1024 / 1024);
    System.out.println("Total memory MB: " + Runtime.getRuntime().totalMemory() / 1024 / 1024);
    System.out.println("Max memory MB: " + Runtime.getRuntime().maxMemory() / 1024 / 1024);

    List<IQuery<IInstallableUnit>> jreQuery = Arrays.asList(QueryUtil.createIUQuery("a.jre.javase"), QueryUtil.createLatestIUQuery());
    IQueryResult<IInstallableUnit> jreResult = manager.query(QueryUtil.createCompoundQuery(jreQuery, true), new NullProgressMonitor());
    IInstallableUnit jreIU = jreResult.iterator().next();
    System.out.println("JRE IU + " + jreIU);

    URI jrep2uri = getJREURI();
    IOUtil.deleteBestEffort(new File(jrep2uri.toFileString()));
    java.net.URI jreLocation = new java.net.URI(jrep2uri.toString());
    manager.removeRepository(jreLocation);
    IMetadataRepository jreRepository = manager.createRepository(jreLocation, "jre", IMetadataRepositoryManager.TYPE_SIMPLE_REPOSITORY,
        Collections.<String, String> emptyMap());
    jreRepository.addInstallableUnits(Collections.singleton(jreIU));

    System.out.println("Processing + " + nodeQueryURIs.size() + " listings");
    Map<String, Project> projects = new LinkedHashMap<String, Project>();
    {
      for (URI listingURI : nodeQueryURIs)
      {
        Resource listingResource = resourceSet.getResource(listingURI, true);
        EList<EObject> contents = listingResource.getContents();
        if (contents.isEmpty())
        {
          System.err.println("Empty Listing: '" + listingURI);
          continue;
        }

        EObject documentRoot = contents.get(0);
        List<AnyType> nodes = get(documentRoot, "node");
        if (nodes == null)
        {
          EObject marketplace = documentRoot.eContents().get(0);
          nodes = get(marketplace, "node");
        }

        for (AnyType node : nodes)
        {
          String id = get(node, "id");
          Project project = projects.get(id);
          if (project == null)
          {
            String projectLabel = get(node, "name");
            projectLabel = projectLabel.trim().replace("&amp;", "&");
            project = SetupFactory.eINSTANCE.createProject();
            project.setLabel(projectLabel);

            project.setDescription(SetupUtil.escape(getContent(node, "shortdescription")));
            String image = getContent(node, "image");
            if (image != null)
            {
              URI imageURI = URI.createURI(image);
              imageURI = imageURI.trimQuery();
              BaseUtil.setAnnotation(project, AnnotationConstants.ANNOTATION_BRANDING_INFO, AnnotationConstants.KEY_IMAGE_URI, imageURI.toString());
            }

            String nodeURL = get(node, "url");
            URI nodeURI = URI.createURI(nodeURL);

            String title = nodeURI.lastSegment();
            project.setName(title);

            // BaseUtil.setAnnotation(project, AnnotationConstants.ANNOTATION_BRANDING_INFO, AnnotationConstants.KEY_SITE_URI, getContent(node,
            // "homepageurl"));
            BaseUtil.setAnnotation(project, AnnotationConstants.ANNOTATION_BRANDING_INFO, AnnotationConstants.KEY_SITE_URI, nodeURL);

            BaseUtil.setAnnotation(project, AnnotationConstants.ANNOTATION_BRANDING_INFO, "marketplace", listingURI.trimQuery().toString());

            BaseUtil.setAnnotation(project, AnnotationConstants.ANNOTATION_BRANDING_INFO, "marketplaceID", id);

            List<AnyType> nodeCategories = get(node, "categories", "category");
            for (AnyType nodeCategory : nodeCategories)
            {
              String projectCategoryName = get(nodeCategory, "name");
              String projectCategoryURL = get(nodeCategory, "url");
              BaseUtil.setAnnotation(project, CATEGORIES_ANNOTATION_SOURCE, projectCategoryName, projectCategoryURL);
            }

            List<AnyType> nodeTags = get(node, "tags", "tag");
            for (AnyType nodeTag : nodeTags)
            {
              String projectCategoryName = get(nodeTag, "name");
              String projectCategoryURL = get(nodeTag, "url");
              BaseUtil.setAnnotation(project, TAGS_ANNOTATION_SOURCE, projectCategoryName, projectCategoryURL);
            }

            projects.put(id, project);
          }

          // Stream
          {
            Stream stream = SetupFactory.eINSTANCE.createStream();
            String platformVersionQuery = listingURI.query();
            String streamVersion = platformVersions.get(platformVersionQuery);
            String version = getContent(node, "version");
            stream.setName(streamVersion.toLowerCase());
            stream.setLabel(StringUtil.isEmpty(version) ? streamVersion : streamVersion + " - " + version);

            BaseUtil.setAnnotation(stream, AnnotationConstants.ANNOTATION_BRANDING_INFO, "marketplace", listingURI.toString());

            String updateURL = getContent(node, "updateurl");
            if (StringUtil.isEmpty(updateURL))
            {
              continue;
            }

            if (updateURL.endsWith("/"))
            {
              updateURL = updateURL.substring(0, updateURL.length() - 1);
            }

            P2Task p2Task = SetupP2Factory.eINSTANCE.createP2Task();
            Repository updateRepository = P2Factory.eINSTANCE.createRepository(updateURL);
            p2Task.getRepositories().add(updateRepository);

            p2Task.getRepositories().add(P2Factory.eINSTANCE.createRepository(getJREURI().toString()));

            EList<Requirement> requirements = p2Task.getRequirements();
            List<AnyType> ius = get(node, "ius", "iu");
            for (AnyType iu : ius)
            {
              String iuID = getContent(iu);
              String name = getName(updateURL, iuID);
              if (name == null)
              {
                name = getName("https://download.eclipse.org/releases/" + streamVersion.toLowerCase(), iuID);
              }

              if (name == null && !iuID.endsWith(".feature.group"))
              {
                name = getName(updateURL, iuID + ".feature.group");
                if (name == null)
                {
                  name = getName("https://download.eclipse.org/releases/" + streamVersion.toLowerCase(), iuID + ".feature.group");
                }

                if (name != null)
                {
                  iuID += ".feature.group";
                }
              }

              Requirement requirement = P2Factory.eINSTANCE.createRequirement(iuID);
              VersionRange iuVersionRange = getVersionRange(updateURL, iuID);
              if (iuVersionRange != null)
              {
                requirement.setVersionRange(iuVersionRange);
              }

              requirements.add(requirement);

              Set<IInstallableUnit> specificIUs = getIUs(updateURL, iuID);
              for (IInstallableUnit specificIU : specificIUs)
              {
                if (specificIU instanceof IInstallableUnitPatch)
                {
                  IInstallableUnitPatch iuPatch = (IInstallableUnitPatch)specificIU;
                  IRequirement lifeCycle = iuPatch.getLifeCycle();
                  if (lifeCycle instanceof org.eclipse.equinox.internal.p2.metadata.IRequiredCapability)
                  {
                    org.eclipse.equinox.internal.p2.metadata.IRequiredCapability requiredCapability = (org.eclipse.equinox.internal.p2.metadata.IRequiredCapability)lifeCycle;
                    String patchedNamespace = requiredCapability.getNamespace();
                    String patchedName = requiredCapability.getName();
                    Requirement patchRequirement = P2Factory.eINSTANCE.createRequirement(patchedName);
                    patchRequirement.setNamespace(patchedNamespace);
                    requirements.add(patchRequirement);
                    break;
                  }
                }
              }

              if (!bogusRepos.containsKey(updateURL))
              {
                BaseUtil.setAnnotation(requirement, "Data", "name", name == null ? "BogusIU" : name);

                IMetadataRepository repository = repos.get(updateURL);
                if (!updateURL.equals(repository.getLocation().toString()))
                {
                  System.err.println("###");
                }
              }

              if (Boolean.FALSE)
              {
                String selected = get(iu, "selected");
                if (selected != null)
                {
                  requirement.getAnnotations()
                      .add(BaseFactory.eINSTANCE.createAnnotation("FALSE".equals(selected) ? UNSELECTED_ANNOTATION_SOURCE : SELECTED_ANNOTATION_SOURCE));
                }
              }
            }

            Exception bogusRepoException = bogusRepos.get(updateURL);
            if (bogusRepoException != null)
            {
              BaseUtil.setAnnotation(updateRepository, "BogusRepo", "message", buildMessage(bogusRepoException));
            }

            if (requirements.isEmpty())
            {
              BaseUtil.setAnnotation(stream, "BogusEmptyRequirements", "message", "The listing contains no <ius>");
            }

            p2Task.setLabel(project.getLabel());
            stream.getSetupTasks().add(p2Task);

            List<AnyType> platforms = get(node, "platforms", "platform");
            if (platforms.isEmpty())
            {
              BaseUtil.setAnnotation(stream, "BogusEmptyPlatforms", "message", "The listing contains no <platforms>");
              System.err.println("###");
            }

            int platformsSize = platforms.size();
            if (platformsSize < 3)
            {
              boolean or = platformsSize > 1;
              StringBuilder filter = new StringBuilder();
              if (or)
              {
                filter.append("(|");
              }

              for (AnyType platform : platforms)
              {
                String platformName = getContent(platform);
                if ("Windows".equals(platformName))
                {
                  filter.append("(osgi.os=win32)");
                }
                else if ("Mac".equals(platformName))
                {
                  filter.append("(osgi.os=mac)");
                }
                else if ("Linux/GTK".equals(platformName))
                {
                  filter.append("(&(osgi.os=linux)(osgi.ws=gtk))");
                }
              }

              if (or)
              {
                filter.append(')');
              }

              p2Task.setFilter(filter.toString());
            }

            Set<Version> nodePlatformVersions = new TreeSet<Version>();
            String eclipseVersions = getContent(node, "eclipseversion");
            if (StringUtil.isEmpty(eclipseVersions))
            {
              BaseUtil.setAnnotation(stream, "BogusEmptyEclipseVersion", "message", "The listing contains no eclipseversion");
              System.err.println("###");
            }
            else
            {
              for (String eclipseVersion : StringUtil.explode(eclipseVersions, ","))
              {
                nodePlatformVersions.add(Version.create(eclipseVersion.trim()));
              }
            }

            // org.eclipse.platform.feature.group
            if (nodePlatformVersions.isEmpty())
            {
              System.err.println("###");
            }
            else
            {
              List<Version> versions = new ArrayList<Version>(nodePlatformVersions);
              Version lowerBound = versions.get(0);
              Version upperBound = versions.get(versions.size() - 1);
              VersionRange upperVersionRange = P2Factory.eINSTANCE.createVersionRange(upperBound, VersionSegment.MINOR);

              Version upperBoundVersion = upperVersionRange.getMaximum();
              VersionRange versionRange = lowerBound.equals(upperBoundVersion) ? new VersionRange(lowerBound, true, upperBoundVersion, true)
                  : new VersionRange(lowerBound, true, upperBoundVersion, upperVersionRange.getIncludeMaximum());

              String platformVersion = platformVersionQuery.substring(platformVersionQuery.indexOf('=') + 1);
              VersionRange minimalRange = P2Factory.eINSTANCE.createVersionRange(Version.create(platformVersion), VersionSegment.MINOR);

              VersionRange intersection = minimalRange.intersect(versionRange);
              BaseUtil.setAnnotation(stream, "VersionRange", intersection == null ? "reject" : "accept", versionRange.toString());
              if (intersection == null)
              {
                BaseUtil.setAnnotation(stream, "VersionRange", "reject", minimalRange.toString());
              }

              Requirement platformRequirement = P2Factory.eINSTANCE.createRequirement("org.eclipse.platform.feature.group");
              requirements.add(0, platformRequirement);
            }

            project.getStreams().add(stream);
          }
        }
      }
    }

    List<Project> projectList = new ArrayList<Project>(projects.values());
    Collections.sort(projectList, new Comparator<Project>()
    {
      @Override
      public int compare(Project p1, Project p2)
      {
        return p1.getLabel().compareToIgnoreCase(p2.getLabel());
      }
    });

    for (Iterator<Project> it = projectList.iterator(); it.hasNext();)
    {
      Project project = it.next();
      EList<Stream> streams = project.getStreams();
      int size = streams.size();
      if (size == 0)
      {
        it.remove();
      }
      else if (size > 1 && normalizeP2Tasks)
      {
        Stream stream = streams.get(0);
        EList<SetupTask> setupTasks = stream.getSetupTasks();
        boolean allEqual = true;
        for (int i = 1; i < size; ++i)
        {
          Stream otherStream = streams.get(i);
          if (!EcoreUtil.equals(setupTasks, otherStream.getSetupTasks()))
          {
            allEqual = false;
            break;
          }
        }

        if (allEqual)
        {
          project.getSetupTasks().addAll(setupTasks);
          for (int i = 1; i < size; ++i)
          {
            Stream otherStream = streams.get(i);
            otherStream.getSetupTasks().clear();
          }
        }
        else
        {
          P2Task p2Task = SetupP2Factory.eINSTANCE.createP2Task();
          String label = project.getLabel();
          p2Task.setLabel(label);
          for (Stream s : streams)
          {
            P2Task streamSetupTask = (P2Task)s.getSetupTasks().get(0);
            streamSetupTask.setLabel(null);
          }

          EList<Requirement> requirements = p2Task.getRequirements();

          P2Task streamP2Task = (P2Task)stream.getSetupTasks().get(0);
          for (Iterator<Requirement> it2 = streamP2Task.getRequirements().iterator(); it2.hasNext();)
          {
            Requirement requirement = it2.next();
            List<Requirement> requirementsToRemove = new ArrayList<Requirement>();
            for (int i = 1; i < size; ++i)
            {
              Stream otherStream = streams.get(i);
              P2Task otherStreamP2Task = (P2Task)otherStream.getSetupTasks().get(0);
              for (Requirement otherRequirement : otherStreamP2Task.getRequirements())
              {
                if (EcoreUtil.equals(requirement, otherRequirement))
                {
                  requirementsToRemove.add(otherRequirement);
                  break;
                }
              }
            }

            if (requirementsToRemove.size() == size - 1)
            {
              it2.remove();
              requirements.add(requirement);
              for (int i = 1; i < size; ++i)
              {
                Stream otherStream = streams.get(i);
                P2Task otherStreamP2Task = (P2Task)otherStream.getSetupTasks().get(0);
                otherStreamP2Task.getRequirements().removeAll(requirementsToRemove);
              }
            }
          }

          project.getSetupTasks().add(p2Task);
          project.setLabel(label + " **");
        }
      }
    }

    Resource output = resourceSet.createResource(getTargetResourceURI());
    ProjectCatalog projectCatalog = SetupFactory.eINSTANCE.createProjectCatalog();
    projectCatalog.setName("extensions");
    projectCatalog.setLabel("Extensions");
    projectCatalog.getProjects().addAll(projectList);
    output.getContents().add(projectCatalog);
    output.save(null);

    long startTesting = System.currentTimeMillis();
    System.out.println("Loaded repositories : " + (startTesting - startRepositoryLoads) / 1000 + " seconds");
    System.out.println("Start testing for at most " + await + " minutes");
    test();
    long finishTesting = System.currentTimeMillis();
    System.out.println("Testing : " + (finishTesting - startTesting) / 1000 + " seconds");
  }

  private void loadResources(Set<URI> uris)
  {
    ResourceMirror resourceMirror = new ResourceMirror(resourceSet, 50)
    {
      @Override
      protected LoadJob createWorker(final URI key, int workerID, boolean secondary)
      {
        LoadJob result = super.createWorker(key, workerID, secondary);
        result.addJobChangeListener(new JobChangeAdapter()
        {
          @Override
          public void running(IJobChangeEvent event)
          {
            System.out.println("Loading " + key);
            super.running(event);
          }
        });
        return result;
      }
    };

    resourceMirror.perform(uris);
    resourceMirror.dispose();
  }

  private void generateReport() throws Exception
  {
    Resource resource = resourceSet.getResource(getResolvedResourceURI(), true);
    generateReport(resource);
  }

  private void generateMinimizedResult()
  {
    System.out.println("Generating minimized result from:" + getResolvedResourceURI());

    Resource resource = resourceSet.getResource(getResolvedResourceURI(), true);

    System.out.println("Loaded result from:" + getResolvedResourceURI());

    Set<EObject> objectsToDelete = new HashSet<EObject>();
    for (Iterator<EObject> it = resource.getAllContents(); it.hasNext();)
    {
      EObject eObject = it.next();
      if (eObject instanceof Annotation)
      {
        Annotation annotation = (Annotation)eObject;
        String source = annotation.getSource();
        if (TAGS_ANNOTATION_SOURCE.equals(source) || CATEGORIES_ANNOTATION_SOURCE.equals(source) || UNSELECTED_ANNOTATION_SOURCE.equals(source)
            || SELECTED_ANNOTATION_SOURCE.equals(source) || PROCESSING_ANNOTATION_SOURCE.equals(source))
        {
          objectsToDelete.add(annotation);
        }
        else if (AnnotationConstants.ANNOTATION_BRANDING_INFO.equals(source) && annotation.eContainer() instanceof Stream)
        {
          objectsToDelete.add(annotation);
        }
      }
      else if (eObject instanceof BasicEMap.Entry<?, ?>)
      {
        if ("marketplace".equals(((BasicEMap.Entry<?, ?>)eObject).getKey()))
        {
          objectsToDelete.add(eObject);
        }
      }
    }

    for (EObject eObject : objectsToDelete)
    {
      EcoreUtil.remove(eObject);
    }

    resource.setURI(getResolvedMinimizedResourceURI());

    try
    {
      System.out.println("Saving to :" + getResolvedMinimizedResourceURI());
      resource.save(Collections.singletonMap(XMLResource.OPTION_LINE_WIDTH, Integer.MAX_VALUE));
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
  }

  @SuppressWarnings("unused")
  private void dump(EObject eObject)
  {
    if (eObject != null)
    {
      Resource resource = eObject.eResource();
      if (resource != null)
      {
        Map<Object, Object> options = new HashMap<Object, Object>();
        options.put(XMLResource.OPTION_ROOT_OBJECTS, Collections.singletonList(eObject));
        try
        {
          resource.save(System.out, options);
          System.out.println();
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }
      }
    }
  }

  private void ensureEntryDataExists(ModelElement element)
  {
    if (element != null)
    {
      for (Annotation annotation : element.getAnnotations())
      {
        annotation.getDetails().get(null);
      }
    }
  }

  private void test() throws Exception
  {
    final File installationLocationRoot = new File(outputLocation, "installation-test");

    installationLocationRoot.delete();
    installationLocationRoot.mkdirs();

    Resource resource = resourceSet.getResource(getTargetResourceURI(), true);

    resource.setURI(getResolvedResourceURI());

    ProjectCatalog projectCatalog = (ProjectCatalog)resource.getContents().get(0);

    Resource productCatalogResource = resourceSet.getResource(URI.createURI("index:/org.eclipse.products.setup"), true);
    Product platformIDEProduct = (Product)productCatalogResource.getEObject("//@products[name='org.eclipse.platform.ide']");
    ensureEntryDataExists(platformIDEProduct);
    ensureEntryDataExists(platformIDEProduct.getParentScope());
    ensureEntryDataExists(platformIDEProduct.getParentScope().getParentScope());
    final Map<String, ProductVersion> productVersions = new LinkedHashMap<String, ProductVersion>();
    for (ProductVersion productVersion : platformIDEProduct.getVersions())
    {
      ensureEntryDataExists(productVersion);
      productVersions.put(productVersion.getName(), productVersion);
    }

    int count = Integer.parseInt(System.getProperty("oomph.mpc.thread.pool.size", Integer.toString(Runtime.getRuntime().availableProcessors())));
    ExecutorService executor = Executors.newFixedThreadPool(count);

    for (final Project project : projectCatalog.getProjects())
    {
      executor.submit(new Runnable()
      {
        @Override
        public void run()
        {
          for (Stream stream : project.getStreams())
          {
            String state = BaseUtil.getAnnotation(stream, PROCESSING_ANNOTATION_SOURCE, "state");
            if ("done".equals(state) || "ignored".equals(state))
            {
              continue;
            }

            BaseUtil.setAnnotation(stream, PROCESSING_ANNOTATION_SOURCE, "state", "started");

            Workspace workspace = SetupContext.createWorkspace();
            EList<Stream> workspaceStreams = workspace.getStreams();
            workspaceStreams.clear();
            workspaceStreams.add(stream);
            ProductVersion productVersion = productVersions.get(stream.getName());
            if (productVersion == null)
            {
              BaseUtil.setAnnotation(stream, PROCESSING_ANNOTATION_SOURCE, "state", "ignored");
              continue;
            }

            Installation installation = SetupContext.createInstallation();
            Resource installationResource = resourceSet.getResourceFactoryRegistry().getFactory(SetupContext.INSTALLATION_SETUP_FILE_NAME_URI)
                .createResource(SetupContext.INSTALLATION_SETUP_FILE_NAME_URI);
            installationResource.getContents().add(installation);
            installation.setProductVersion(productVersion);

            try
            {
              final String location = new File(installationLocationRoot, productVersion.getQualifiedName() + "." + stream.getQualifiedName()).toString();

              SetupPrompter prompter = new SetupPrompter()
              {
                @Override
                public boolean promptVariables(List<? extends SetupTaskContext> performers)
                {
                  List<VariableTask> unresolveVariables = new ArrayList<VariableTask>();
                  for (SetupTaskContext performer : performers)
                  {
                    List<VariableTask> unresolvedVariables = ((SetupTaskPerformer)performer).getUnresolvedVariables();
                    for (VariableTask variable : unresolvedVariables)
                    {
                      String value = getValue(variable);
                      if (value == null)
                      {
                        unresolvedVariables.add(variable);
                      }
                    }
                  }

                  return unresolveVariables.isEmpty();
                }

                @Override
                public String getValue(VariableTask variable)
                {
                  return "installation.location".equals(variable.getName()) ? location : null;
                }

                @Override
                public String getVMPath()
                {
                  return null;
                }

                @Override
                public UserCallback getUserCallback()
                {
                  return null;
                }

                @Override
                public OS getOS()
                {
                  return OS.INSTANCE;
                }
              };

              User user = SetupContext.createUser();
              SetupContext setupContext = SetupContext.create(installation, workspace, user);

              SetupTaskPerformer setupTaskPerformer = SetupTaskPerformer.create(resourceSet.getURIConverter(), prompter, Trigger.BOOTSTRAP, setupContext,
                  false);
              if (setupTaskPerformer == null)
              {
                setupTaskPerformer = SetupTaskPerformer.create(resourceSet.getURIConverter(), prompter, Trigger.BOOTSTRAP, setupContext, false);
              }

              EList<SetupTask> triggeredSetupTasks = setupTaskPerformer.getTriggeredSetupTasks();
              InstallationTask installationTask = (InstallationTask)EcoreUtil.getObjectByType(triggeredSetupTasks, SetupPackage.Literals.INSTALLATION_TASK);
              System.out.println("> " + location);
              installationTask.setLocation(location);
              P2Task p2Task = (P2Task)EcoreUtil.getObjectByType(triggeredSetupTasks, SetupP2Package.Literals.P2_TASK);

              boolean hasBogusRepo = false;
              for (Repository repository : p2Task.getRepositories())
              {
                if (BaseUtil.getAnnotation(repository, "BogusRepo", "message") != null)
                {
                  hasBogusRepo = true;
                  break;
                }
              }

              if (!hasBogusRepo)
              {
                Resolution bogus = new Resolution()
                {
                  @Override
                  public ProfileTransaction getProfileTransaction()
                  {
                    return null;
                  }

                  @Override
                  public IProvisioningPlan getProvisioningPlan()
                  {
                    return null;
                  }

                  @Override
                  public boolean commit(IProgressMonitor monitor) throws CoreException
                  {
                    return false;
                  }

                  @Override
                  public void rollback()
                  {
                  }
                };

                setupTaskPerformer.put(Resolution.class, bogus);
                setupTaskPerformer.put(AgentManager.PROP_BUNDLE_POOL_LOCATION, poolLocation.toString());

                p2Task.setLicenseConfirmationDisabled(true);
                p2Task.perform(setupTaskPerformer);
                Resolution resolution = (Resolution)setupTaskPerformer.get(Resolution.class);
                if (resolution == bogus)
                {
                  System.err.println("No resolution");
                }

                IQueryable<IInstallableUnit> futureState = resolution.getProvisioningPlan().getFutureState();
                IQueryResult<IInstallableUnit> query = futureState.query(QueryUtil.createIUQuery("org.eclipse.platform"), null);
                IInstallableUnit platformIU = query.iterator().next();
                BaseUtil.setAnnotation(stream, "Resolved", platformIU.getId(), platformIU.getVersion().toString());
                resolution.getProfileTransaction().getProfile().delete(true);
              }
            }
            catch (Exception ex)
            {
              BaseUtil.setAnnotation(stream, "Unresolved", "exception", buildMessage(ex));
              // ex.printStackTrace();
            }

            BaseUtil.setAnnotation(stream, PROCESSING_ANNOTATION_SOURCE, "state", "done");
          }

          System.gc();
          System.out.println("available=" + Runtime.getRuntime().freeMemory() / 1000 / 1000);
        }
      });
    }

    executor.shutdown();
    boolean terminated = executor.awaitTermination(await, TimeUnit.MINUTES);
    if (terminated)
    {
      System.out.println("Testing terminated normally.");
    }
    else
    {
      System.out.println("Testing terminated with a timeout after " + await + " minutes");
    }

    try
    {
      resource.save(null);
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }

    // resource.setURI(outputLocation.trimFileExtension().appendFileExtension("resolved.setup"));
  }

  private String buildMessage(Throwable throwable)
  {
    StringBuilder result = new StringBuilder();

    if (throwable instanceof CoreException)
    {
      CoreException coreException = (CoreException)throwable;
      IStatus status = coreException.getStatus();
      buildMessage(result, "", status);
      result.append(StringUtil.NL);
    }

    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    throwable.printStackTrace(printWriter);
    printWriter.close();
    result.append(stringWriter.getBuffer());

    return result.toString();
  }

  private void buildMessage(StringBuilder message, String prefix, IStatus status)
  {
    message.append(prefix).append(status.getMessage()).append(StringUtil.NL);
    prefix = prefix.length() == 0 ? "  - " : "  " + prefix;
    for (IStatus child : status.getChildren())
    {
      buildMessage(message, prefix, child);
    }
  }

  private void generateReport(Resource resource) throws Exception
  {
    ProjectCatalog projectCatalog = (ProjectCatalog)resource.getContents().get(0);
    Set<Project> invalidProjects = new HashSet<Project>();
    Set<Stream> invalidStreams = new HashSet<Stream>();
    for (Project project : projectCatalog.getProjects())
    {
      boolean printedGeneralListing = false;
      EList<SetupTask> setupTasks = project.getSetupTasks();
      if (!setupTasks.isEmpty())
      {
        P2Task p2Task = (P2Task)setupTasks.get(0);
        EList<Repository> repositories = p2Task.getRepositories();
        if (!repositories.isEmpty())
        {
          Repository repository = repositories.get(0);
          if (!repository.getAnnotations().isEmpty())
          {
            invalidProjects.add(project);
            printedGeneralListing = printGeneralListing(project, printedGeneralListing);
            System.out.println("    invalid-update-site=" + repository.getURL());

            String message = BaseUtil.getAnnotation(repository, "BogusRepo", "message");
            if (!StringUtil.isEmpty(message))
            {
              for (String line : message.split("\\r?\\n"))
              {
                System.out.println("      " + line);
                break;
              }
            }
          }
        }

        for (Requirement requirement : p2Task.getRequirements())
        {
          if ("BogusIU".equals(BaseUtil.getAnnotation(requirement, "Data", "name")))
          {
            invalidProjects.add(project);
            printedGeneralListing = printGeneralListing(project, printedGeneralListing);
            System.out.println("    invalid-iu=" + requirement.getName());
          }
        }
      }

      for (Stream stream : project.getStreams())
      {
        if (stream.getAnnotation("Resolved") == null)
        {
          invalidStreams.add(stream);
        }

        String specificListing = BaseUtil.getAnnotation(stream, AnnotationConstants.ANNOTATION_BRANDING_INFO, "marketplace");
        boolean printedSpecificListing = false;
        EList<SetupTask> streamSetupTasks = stream.getSetupTasks();
        if (!streamSetupTasks.isEmpty())
        {
          P2Task p2Task = (P2Task)streamSetupTasks.get(0);
          EList<Repository> repositories = p2Task.getRepositories();
          if (!repositories.isEmpty())
          {
            Repository repository = repositories.get(0);
            if (!repository.getAnnotations().isEmpty())
            {
              printedGeneralListing = printGeneralListing(project, printedGeneralListing);

              System.out.println("    " + specificListing);
              printedSpecificListing = true;

              System.out.println("      invalid-update-site=" + repository.getURL());

              String message = BaseUtil.getAnnotation(repository, "BogusRepo", "message");
              if (!StringUtil.isEmpty(message))
              {
                for (String line : message.split("\\n\\r?"))
                {
                  System.out.println("      " + line);
                  break;
                }
              }
            }
          }

          for (Requirement requirement : p2Task.getRequirements())
          {
            if ("BogusIU".equals(BaseUtil.getAnnotation(requirement, "Data", "name")))
            {
              printedGeneralListing = printGeneralListing(project, printedGeneralListing);

              if (!printedSpecificListing)
              {
                System.out.println("    " + specificListing);
                printedSpecificListing = true;
              }

              System.out.println("      invalid-iu=" + requirement.getName());
            }
          }
        }

        for (Annotation annotation : stream.getAnnotations())
        {
          String message = annotation.getDetails().get("message");
          if (!StringUtil.isEmpty(message))
          {
            if (!printedSpecificListing)
            {
              System.out.println("    " + specificListing);
              printedSpecificListing = true;
            }

            System.out.println("      " + message);
          }
        }

        String exception = BaseUtil.getAnnotation(stream, "Unresolved", "exception");
        if (!StringUtil.isEmpty(exception))
        {
          printedGeneralListing = printGeneralListing(project, printedGeneralListing);

          if (!printedSpecificListing)
          {
            System.out.println("    " + specificListing);
            printedSpecificListing = true;
            System.out.println("      Failed Resolution:");
            printedSpecificListing = true;
          }

          for (String line : exception.split("\\n\\r?"))
          {
            if (line.startsWith("  - Missing requirement:"))
            {
              System.out.println("      " + line);
            }
          }
        }
      }
    }

    EcoreUtil.deleteAll(invalidStreams, false);

    for (Project project : projectCatalog.getProjects())
    {
      if (project.getStreams().isEmpty())
      {
        invalidProjects.add(project);
      }
    }

    EcoreUtil.deleteAll(invalidProjects, false);

    URI uri = resource.getURI();
    resource.setURI(uri.trimSegments(1).appendSegment(uri.trimFileExtension().lastSegment() + "-pruned.setup"));
    resource.save(null);
  }

  private boolean printGeneralListing(Project project, boolean printedGeneralListing)
  {
    if (!printedGeneralListing)
    {
      String id = BaseUtil.getAnnotation(project, AnnotationConstants.ANNOTATION_BRANDING_INFO, "marketplaceID");
      System.out.println("" + id);
      System.out.println("  '" + project.getLabel() + "'");
      String generalListing = BaseUtil.getAnnotation(project, AnnotationConstants.ANNOTATION_BRANDING_INFO, "marketplace");
      System.out.println("  " + generalListing);
    }

    return true;
  }

  private String getContent(EObject eObject, String feature)
  {
    List<AnyType> value = get(eObject, feature);
    if (value == null)
    {
      return null;
    }

    return getContent(value.get(0));
  }

  private String getContent(AnyType anyType)
  {
    StringBuilder result = new StringBuilder();
    for (FeatureMap.Entry entry : anyType.getMixed())
    {
      if (FeatureMapUtil.isCDATA(entry) || FeatureMapUtil.isText(entry))
      {
        result.append(entry.getValue());
      }
    }

    return result.toString().trim();
  }

  private <T> T get(EObject eObject, String feature)
  {
    AnyType anyType = (AnyType)eObject;
    FeatureMap anyAttribute = anyType.getAnyAttribute();
    for (FeatureMap.Entry entry : anyAttribute)
    {
      if (entry.getEStructuralFeature().getName().equals(feature))
      {
        @SuppressWarnings("unchecked")
        T value = (T)entry.getValue();
        return value;
      }
    }

    List<Object> values = new ArrayList<Object>();
    for (FeatureMap.Entry entry : anyType.getMixed())
    {
      if (entry.getEStructuralFeature().getName().equals(feature))
      {
        values.add(entry.getValue());
      }
    }

    if (values.isEmpty())
    {
      return null;
    }

    @SuppressWarnings("unchecked")
    T result = (T)values;
    return result;
  }

  private List<AnyType> get(EObject eObject, String feature, String childFeature)
  {
    List<AnyType> result = new ArrayList<AnyType>();

    List<AnyType> values = get(eObject, feature);
    if (values != null)
    {
      for (AnyType anyType : values)
      {
        List<AnyType> childValues = get(anyType, childFeature);
        result.addAll(childValues);
      }
    }

    return result;
  }

  @Override
  public void stop()
  {
  }

  private final Map<String, IMetadataRepository> repos = Collections.synchronizedMap(new HashMap<String, IMetadataRepository>());

  private final Map<String, Exception> bogusRepos = new HashMap<String, Exception>();

  private Set<IInstallableUnit> getIUs(String url, String id)
  {
    Set<IInstallableUnit> result = new TreeSet<IInstallableUnit>();
    try
    {
      IMetadataRepository repository = repos.get(url);
      if (repository == null && !bogusRepos.containsKey(url))
      {
        repository = manager.loadRepository(new java.net.URI(url), null);
        handleComposite(repository, id);
        repos.put(url, repository);
        System.err.println("loaded: " + url);
      }

      if (repository != null)
      {
        IQueryResult<IInstallableUnit> ius = repository.query(QueryUtil.createIUQuery(id), null);
        for (IInstallableUnit iu : ius)
        {
          result.add(iu);
        }
      }
    }
    catch (Exception ex)
    {
      if (ex instanceof ProvisionException && ((ProvisionException)ex).getStatus().getException() instanceof SSLHandshakeException && url.startsWith("http:"))
      {
        System.err.println("retry without https: " + url);
        String httpsURL = "https:" + url.substring("http:".length());
        Set<IInstallableUnit> ius = getIUs(httpsURL, id);
        if (!bogusRepos.containsKey(httpsURL))
        {
          repos.put(url, repos.get(httpsURL));
          return ius;
        }
      }

      System.err.println("failed: " + url);
      repos.put(url, null);
      bogusRepos.put(url, ex);
    }

    return result;
  }

  private VersionRange getVersionRange(String url, String id)
  {
    Version minVersion = null;
    Version maxVersion = null;
    Set<IInstallableUnit> result = getIUs(url, id);
    for (IInstallableUnit iu : result)
    {
      Version version = iu.getVersion();
      if (minVersion == null || version.compareTo(minVersion) < 0)
      {
        minVersion = version;
      }
      if (maxVersion == null || version.compareTo(maxVersion) > 0)
      {
        maxVersion = version;
      }
    }

    if (minVersion != null)
    {
      return new VersionRange(minVersion, true, maxVersion, true);
    }

    return null;
  }

  private String getName(String url, String id)
  {
    Set<IInstallableUnit> result = getIUs(url, id);
    for (IInstallableUnit iu : result)
    {
      return P2Util.getName(iu);
    }

    return null;
  }

  private void handleComposite(IMetadataRepository repository, String id)
  {
    if (repository instanceof ICompositeRepository<?>)
    {
      ICompositeRepository<?> compositeRepository = (ICompositeRepository<?>)repository;
      List<java.net.URI> children = compositeRepository.getChildren();
      for (java.net.URI uri : children)
      {
        getName(uri.toString(), id);
      }
    }
  }

  private IMetadataRepositoryManager getMetadataRepositoryManager()
  {
    Agent agent = P2Util.getAgentManager().getBundlePool(poolLocation).getAgent();
    IMetadataRepositoryManager manager = agent.getMetadataRepositoryManager();
    return manager;
  }

  /**
   * @author Ed Merks
   */
  public static class RepositoryLoader extends WorkerPool<RepositoryLoader, String, RepositoryLoader.LoadJob>
  {
    private MarketplaceCatalogGenerator generator;

    public RepositoryLoader(MarketplaceCatalogGenerator generatorx)
    {
      generator = generatorx;
      ReflectUtil.setValue("maxWorker", this, 50);
    }

    /**
     * @author Ed Merks
     */
    public class LoadJob extends WorkerPool.Worker<String, RepositoryLoader>
    {
      private LoadJob(RepositoryLoader loader, String updateURL, int id, boolean secondary)
      {
        super("Load " + updateURL, loader, updateURL, id, secondary);
      }

      @Override
      protected IStatus perform(IProgressMonitor monitor)
      {
        generator.getName(getKey(), "bogus");
        return Status.OK_STATUS;
      }
    }

    @Override
    protected LoadJob createWorker(String key, int workerID, boolean secondary)
    {
      return new LoadJob(this, key, workerID, secondary);
    }
  }
}
