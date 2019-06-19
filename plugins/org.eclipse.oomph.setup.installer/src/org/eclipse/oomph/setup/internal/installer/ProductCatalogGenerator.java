/*
 * Copyright (c) 2015-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.impl.RequirementImpl;
import org.eclipse.oomph.p2.internal.core.AgentImpl;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.EclipseIniTask;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Factory;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.WorkerPool;
import org.eclipse.oomph.util.XMLUtil;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.internal.p2.metadata.OSGiVersion;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.ITouchpointData;
import org.eclipse.equinox.p2.metadata.ITouchpointInstruction;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;
import org.eclipse.equinox.p2.query.CompoundQueryable;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.ICompositeRepository;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class ProductCatalogGenerator implements IApplication
{
  private static final String JAVA_VERSION_PREFIX = "addJvmArg(jvmArg:-Dosgi.requiredJavaVersion=";

  private static final String PACKAGES = "http://download.eclipse.org/technology/epp/packages";

  private static final String HTTPS_PACKAGES = "https://download.eclipse.org/technology/epp/packages";

  private static final String RELEASES = "http://download.eclipse.org/releases";

  private static final String ICON_URL_PREFIX = "http://www.eclipse.org/downloads/images/";

  private static final URI PACKAGES_URI = URI.createURI("https://www.eclipse.org/downloads/packages/release/");

  private static final URI ECLIPSE_PROJECT_URI = URI.createURI("http://download.eclipse.org/eclipse/updates");

  private static final String ICON_DEFAULT = ICON_URL_PREFIX + "committers.png";

  private static final Map<String, String> ICONS = new HashMap<String, String>();

  private static final List<String> PRODUCT_IDS = Arrays
      .asList(new String[] { "epp.package.java", "epp.package.jee", "epp.package.cpp", "epp.package.javascript", "epp.package.php", "epp.package.committers",
          "epp.package.dsl", "epp.package.reporting", "epp.package.modeling", "epp.package.android", "epp.package.rcp", "epp.package.testing",
          "epp.package.parallel", "epp.package.automotive", "epp.package.scout", "epp.package.rust", "org.eclipse.platform.ide", "org.eclipse.sdk.ide" });

  private static final String ALL_PRODUCT_ID = "all";

  private static final String ECLIPSE_PLATFORM_SDK_PRODUCT_ID = "eclipse.platform.sdk";

  // private static final List<String> SPECIAL_PRODUCT_IDS = Arrays.asList(new String[] { "org.eclipse.platform.ide", "org.eclipse.sdk.ide" });
  private static final List<String> SPECIAL_PRODUCT_IDS = Arrays.asList(new String[] { "org.eclipse.platform.ide" });

  private static final Set<String> EXCLUDED_IDS = new HashSet<String>(Arrays.asList("epp.package.mobile"));

  private static final String EPP_INSTALL_ROOTS_FILTER = "(org.eclipse.epp.install.roots=true)";

  private static final boolean IS_RANGE_NARROW = Boolean.FALSE;

  private URI outputLocation;

  private String stagingTrain;

  private boolean stagingEPPLocationIsActual;

  private boolean latestReleased;

  private boolean stagingUseComposite;

  private List<String> compositeTrains = new ArrayList<String>();

  private URI stagingEPPLocation;

  private URI stagingTrainLocation;

  private URI stagingEclipePlatformLocation;

  private final Map<String, Map<URI, Map<String, URI>>> sites = new LinkedHashMap<String, Map<URI, Map<String, URI>>>();

  private final IMetadataRepositoryManager manager = getMetadataRepositoryManager();

  private final Map<String, IMetadataRepository> eppMetaDataRepositories = new HashMap<String, IMetadataRepository>();

  private final Map<String, List<TrainAndVersion>> trainsAndVersions = new HashMap<String, List<TrainAndVersion>>();

  private final Map<String, String> labels = new HashMap<String, String>();

  private final Map<String, Product> products = new LinkedHashMap<String, Product>();

  private String emfRepositoryLocation;

  private final String[] TRAINS = getTrains();

  private final String LATEST_TRAIN = TRAINS[TRAINS.length - 1];

  public Object start(IApplicationContext context) throws Exception
  {
    // if (true)
    // {
    // String completeLocation = "http://download.eclipse.org/releases/mars/201506241002";
    //
    // Product product = SetupFactory.eINSTANCE.createProduct();
    // product.setName("org.eclipse.complete");
    // product.setLabel("Eclipse IDE Complete");
    //
    // ProductVersion productVersion = SetupFactory.eINSTANCE.createProductVersion();
    // productVersion.setName("mars");
    // productVersion.setLabel("Mars");
    // product.getVersions().add(productVersion);
    //
    // P2Task p2Task = SetupP2Factory.eINSTANCE.createP2Task();
    // p2Task.setLabel("Complete");
    // productVersion.getSetupTasks().add(p2Task);
    //
    // Repository completeRepository = P2Factory.eINSTANCE.createRepository();
    // completeRepository.setURL(completeLocation);
    // p2Task.getRepositories().add(completeRepository);
    //
    // IMetadataRepositoryManager manager = getMetadataRepositoryManager();
    // IMetadataRepository repository = manager.loadRepository(new URI(completeLocation), null);
    //
    // Set<IInstallableUnit> ius = RootAnalyzer.getRootUnits(repository, null);
    // List<IInstallableUnit> rootUnits = new ArrayList<IInstallableUnit>(ius);
    // Collections.sort(rootUnits, new Comparator<IInstallableUnit>()
    // {
    // public int compare(IInstallableUnit iu1, IInstallableUnit iu2)
    // {
    // return iu1.getId().compareTo(iu2.getId());
    // }
    // });
    //
    // for (IInstallableUnit rootUnit : rootUnits)
    // {
    // Requirement requirement = P2Factory.eINSTANCE.createRequirement();
    // requirement.setName(rootUnit.getId());
    // requirement.setMatchExpression(rootUnit.getFilter());
    // p2Task.getRequirements().add(requirement);
    // }
    //
    // System.out.println();
    // System.out.println();
    //
    // Resource resource = new BaseResourceFactoryImpl().createResource(URI.createURI("org.eclipse.complete.setup"));
    // resource.getContents().add(product);
    // resource.save(System.out, null);
    //
    // return null;
    // }

    // luna
    // -staging mars staging-epp staging-train

    String[] arguments = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
    if (arguments != null)
    {
      for (int i = 0; i < arguments.length; ++i)
      {
        String option = arguments[i];
        if ("-outputLocation".equals(option))
        {
          outputLocation = URI.createURI(arguments[++i]);
        }
        else if ("-staging".equals(option))
        {
          stagingTrain = arguments[++i];
          stagingEPPLocation = URI.createURI(arguments[++i]);
          stagingTrainLocation = URI.createURI(arguments[++i]);
          if (i < arguments.length && !arguments[i].startsWith("-"))
          {
            stagingEclipePlatformLocation = URI.createURI(arguments[++i]);
          }
        }
        else if ("-actual".equals(option))
        {
          stagingEPPLocationIsActual = true;
        }
        else if ("-latestReleased".equals(option))
        {
          latestReleased = true;
        }
        else if ("-useComposite".equals(option))
        {
          if (i + 1 < arguments.length && Arrays.asList(getTrains()).contains(arguments[i + 1]))
          {
            do
            {
              compositeTrains.add(arguments[++i]);
            } while (i + 1 < arguments.length && Arrays.asList(getTrains()).contains(arguments[i + 1]));
          }
          else
          {
            stagingUseComposite = true;
          }
        }
      }
    }

    ICONS.put("reporting", ICON_URL_PREFIX + "birt-icon_48x48.png");
    ICONS.put("cpp", ICON_URL_PREFIX + "cdt.png");
    ICONS.put("automotive", ICON_URL_PREFIX + "classic.jpg");
    ICONS.put("standard", ICON_URL_PREFIX + "committers.png");
    ICONS.put("committers", ICON_URL_PREFIX + "committers.png");
    ICONS.put("android", ICON_URL_PREFIX + "committers.png");
    ICONS.put("dsl", ICON_URL_PREFIX + "dsl-package_42.png");
    ICONS.put("java", ICON_URL_PREFIX + "java.png");
    ICONS.put("jee", ICON_URL_PREFIX + "javaee.png");
    // ICONS.put("?", ICON_URL_PREFIX+"JRebel-42x42-dark.png");
    ICONS.put("modeling", ICON_URL_PREFIX + "modeling.png");
    ICONS.put("parallel", ICON_URL_PREFIX + "parallel.png");
    ICONS.put("php", ICON_URL_PREFIX + "php.png");
    ICONS.put("rcp", ICON_URL_PREFIX + "rcp.png");
    ICONS.put("scout", ICON_URL_PREFIX + "scout.jpg");
    ICONS.put("testing", ICON_URL_PREFIX + "testing.png");
    ICONS.put("mobile", ICON_URL_PREFIX + "mobile.jpg");
    ICONS.put("rust", ICON_URL_PREFIX + "committers.png");

    generate();
    return null;
  }

  public void stop()
  {
  }

  private String[] getTrains()
  {
    return new String[] { "juno", "kepler", "luna", "mars", "neon", "oxygen", "photon", "2018-09", "2018-12", "2019-03", "2019-06" };
  }

  private URI getEclipsePlatformSite(String train)
  {
    String[] trains = getTrains();
    for (int i = 0; i < trains.length; ++i)
    {
      if (trains[i].equals(train))
      {
        if (train.equals(stagingTrain) && stagingEclipePlatformLocation != null)
        {
          return stagingEclipePlatformLocation;
        }

        String versionSegment = "4." + (i + 2);
        if (i + 1 == trains.length && !isLatestReleased())
        {
          versionSegment += "-I-builds";
        }

        return ECLIPSE_PROJECT_URI.appendSegment(versionSegment);
      }
    }

    throw new RuntimeException("Invalid train: " + train);
  }

  private int compareTrains(String train1, String train2)
  {
    String[] trains = getTrains();
    int train1Index = -1;
    int train2Index = -1;
    for (int i = 0; i < trains.length; ++i)
    {
      String train = trains[i];
      if (train1.equals(train))
      {
        train1Index = i;
      }
      if (train2.equals(train))
      {
        train2Index = i;
      }
    }

    return train1Index - train2Index;
  }

  private String getMostRecentTrain()
  {
    String[] trains = getTrains();
    return trains[trains.length - 1];
  }

  private String[] getRootIUs()
  {
    return new String[] { "org.eclipse.platform.feature.group", "org.eclipse.rcp.feature.group", "org.eclipse.jdt.feature.group",
        "org.eclipse.pde.feature.group" };
  }

  private boolean isLatestReleased()
  {
    return latestReleased;
  }

  public void generate()
  {
    getPackageBrandingSites();

    try
    {
      ProductCatalog productCatalog = SetupFactory.eINSTANCE.createProductCatalog();
      productCatalog.setName("org.eclipse.products");
      productCatalog.setLabel("Eclipse.org");
      productCatalog.setDescription("The catalog of products available as <a href='https://www.eclipse.org/downloads/'>packaged downloads</a> at Eclipse.org.");

      Annotation brandingInfo = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
      brandingInfo.getDetails().put(AnnotationConstants.KEY_README_PATH, "readme/readme_eclipse.html");
      productCatalog.getAnnotations().add(brandingInfo);

      Annotation statsSending = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_STATS_SENDING);
      statsSending.getDetails().put(AnnotationConstants.KEY_URI, SetupCoreUtil.STATS_URI);
      productCatalog.getAnnotations().add(statsSending);

      InstallationTask installationTask = SetupFactory.eINSTANCE.createInstallationTask();
      installationTask.setID("installation");
      productCatalog.getSetupTasks().add(installationTask);

      productCatalog.getSetupTasks().addAll(createOomphP2Task(null));

      emfRepositoryLocation = trimEmptyTrailingSegment(
          URI.createURI(loadLatestRepository(manager, null, URI.createURI("http://download.eclipse.org/modeling/emf/emf/builds/release/latest"), true)
              .getLocation().toString())).toString();

      new RepositoryLoader(this).perform(TRAINS);

      System.out.println("#################################################################################################################");
      System.out.println();

      List<String> ids = new ArrayList<String>(trainsAndVersions.keySet());
      Collections.sort(ids, new Comparator<String>()
      {
        public int compare(String id1, String id2)
        {
          int result = getLatestTrain(id2) - getLatestTrain(id1);
          if (result == 0)
          {
            String label1 = StringUtil.safe(labels.get(id1));
            String label2 = StringUtil.safe(labels.get(id2));
            result = label1.compareTo(label2);
          }

          return result;
        }

        private int getLatestTrain(String id)
        {
          List<TrainAndVersion> list = trainsAndVersions.get(id);
          TrainAndVersion lastEntry = list.get(list.size() - 1);
          String lastTrain = lastEntry.getTrain();

          for (int i = 0; i < TRAINS.length; i++)
          {
            String train = TRAINS[i];
            if (train == lastTrain)
            {
              return i;
            }
          }

          throw new IllegalStateException();
        }
      });

      for (String id : ids)
      {
        products.put(id, null);
      }

      new ProductGenerator(this).perform(ids);

      final EList<Product> products = productCatalog.getProducts();
      products.addAll(this.products.values());
      final List<String> productIDs = new UniqueEList<String>(PRODUCT_IDS);
      for (Product product : products)
      {
        productIDs.add(product.getName());
      }

      ECollections.sort(products, new Comparator<Product>()
      {
        public int compare(Product product1, Product product2)
        {
          int index1 = productIDs.indexOf(product1.getName());
          int index2 = productIDs.indexOf(product2.getName());
          return index1 - index2;
        }
      });

      System.out.println("#################################################################################################################");
      System.out.println();

      if (IS_RANGE_NARROW)
      {
        checkVersionRanges(productCatalog);
      }
      // addIntegrationVersions(productCatalog);
      postProcess(productCatalog);

      Resource resource = new BaseResourceFactoryImpl().createResource(outputLocation == null ? URI.createURI("org.eclipse.products.setup") : outputLocation);
      resource.getContents().add(productCatalog);

      if (outputLocation != null)
      {
        for (Product product : new ArrayList<Product>(products))
        {
          if (ALL_PRODUCT_ID.equals(product.getName()))
          {
            products.remove(product);

            if (outputLocation != null)
            {
              URI allProductURI = outputLocation.trimSegments(1).appendSegment("org.eclipse.all.product.setup");
              Resource allProductResource = new BaseResourceFactoryImpl().createResource(allProductURI);
              EclipseIniTask eclipseIniTask = SetupFactory.eINSTANCE.createEclipseIniTask();
              eclipseIniTask.setVm(true);
              eclipseIniTask.setOption("-Xmx");
              eclipseIniTask.setValue("5g");
              product.getSetupTasks().add(eclipseIniTask);

              allProductResource.getContents().add(product);

              allProductResource.save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
            }
          }

          if (ECLIPSE_PLATFORM_SDK_PRODUCT_ID.equals(product.getName()))
          {
            products.remove(product);

            if (outputLocation != null)
            {
              URI eclipsePlatformSDKProductURI = outputLocation.trimSegments(1).appendSegment("org.eclipse.platform.sdk.product.setup");
              Resource eclipsePlatformSDKProductResource = new BaseResourceFactoryImpl().createResource(eclipsePlatformSDKProductURI);
              EclipseIniTask eclipseIniTask = SetupFactory.eINSTANCE.createEclipseIniTask();
              eclipseIniTask.setVm(true);
              eclipseIniTask.setOption("-Xmx");
              eclipseIniTask.setValue("3g");
              product.getSetupTasks().add(eclipseIniTask);

              eclipsePlatformSDKProductResource.getContents().add(product);

              eclipsePlatformSDKProductResource
                  .save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
            }
          }
        }

        resource.save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private List<SetupTask> createOomphP2Task(String emfRepository)
  {
    List<SetupTask> result = new ArrayList<SetupTask>();
    Requirement oomphRequirement = P2Factory.eINSTANCE.createRequirement("org.eclipse.oomph.setup.feature.group");
    Repository oomphRepository = P2Factory.eINSTANCE.createRepository("${" + SetupProperties.PROP_UPDATE_URL + "}");

    P2Task p2Task = SetupP2Factory.eINSTANCE.createP2Task();
    p2Task.getRequirements().add(oomphRequirement);
    p2Task.getRepositories().add(oomphRepository);
    result.add(p2Task);

    if (emfRepository != null)
    {
      VariableTask emfVariable = SetupFactory.eINSTANCE.createVariableTask();
      emfVariable.setName("oomph.emf.update.url");
      emfVariable.setValue(emfRepository);
      emfVariable.setLabel("Oomph's EMF Repository Location");
      emfVariable.setDescription("The location of the EMF repository for satisifying Oomph's dependencies on EMF");
      result.add(emfVariable);

      p2Task.getRepositories().add(P2Factory.eINSTANCE.createRepository("${oomph.emf.update.url}"));
    }

    return result;
  }

  private void generateProduct(String id)
  {
    String label = labels.get(id);
    String p2TaskLabel = label;

    List<TrainAndVersion> list = trainsAndVersions.get(id);
    int size = list.size();

    TrainAndVersion latestTrainAndVersion = list.get(size - 1);
    String latestTrain = latestTrainAndVersion.getTrain();
    String latestTrainLabel = getTrainLabel(latestTrain);
    Version latestVersion = latestTrainAndVersion.getVersion();
    Map<String, Set<IInstallableUnit>> latestTrainsIUs = latestTrainAndVersion.getIUs();

    boolean latestUnreleased = latestTrain == LATEST_TRAIN && !isLatestReleased();

    StringBuilder log = new StringBuilder();
    log.append(label).append(" (").append(id).append(')').append('\n');

    Product product = SetupFactory.eINSTANCE.createProduct();
    product.setName(id);
    product.setLabel(label);
    attachBrandingInfos(log, product);
    products.put(id, product);

    if (ALL_PRODUCT_ID.equals(id) || ECLIPSE_PLATFORM_SDK_PRODUCT_ID.equals(id))
    {
      product.getSetupTasks().addAll(createOomphP2Task(ECLIPSE_PLATFORM_SDK_PRODUCT_ID.equals(id) ? emfRepositoryLocation : null));
    }

    String eclipseVersionPrefix = "";
    if (ECLIPSE_PLATFORM_SDK_PRODUCT_ID.equals(id))
    {
      Version version = latestTrainAndVersion.getIUs().values().iterator().next().iterator().next().getVersion();
      eclipseVersionPrefix = version.getSegment(0) + "." + version.getSegment(1) + " - ";
    }

    addProductVersion(log, product, latestVersion, VersionSegment.MAJOR, latestTrainAndVersion.getTrainURI(), latestTrainAndVersion.getEPPURI(), latestTrain,
        eppMetaDataRepositories, "latest", "Latest (" + eclipseVersionPrefix + latestTrainLabel + ")",
        p2TaskLabel + " (" + eclipseVersionPrefix + latestTrainLabel + ")", latestTrainsIUs, emfRepositoryLocation);

    if (!latestUnreleased || size != 1)
    {
      int offset = 1;
      if (latestUnreleased && size > 1)
      {
        ++offset;
      }

      TrainAndVersion releasedTrainAndVersion = list.get(size - offset);
      String releasedTrain = releasedTrainAndVersion.getTrain();
      String releasedTrainLabel = getTrainLabel(releasedTrain);
      Version releasedVersion = releasedTrainAndVersion.getVersion();

      if (ECLIPSE_PLATFORM_SDK_PRODUCT_ID.equals(id))
      {
        Version version = releasedTrainAndVersion.getIUs().values().iterator().next().iterator().next().getVersion();
        eclipseVersionPrefix = version.getSegment(0) + "." + version.getSegment(1) + " - ";
      }

      addProductVersion(log, product, releasedVersion, IS_RANGE_NARROW ? VersionSegment.MINOR : VersionSegment.MAJOR, releasedTrainAndVersion.getTrainURI(),
          releasedTrainAndVersion.getEPPURI(), releasedTrain, eppMetaDataRepositories, "latest.released",
          "Latest Release (" + eclipseVersionPrefix + releasedTrainLabel + ")", p2TaskLabel + " (" + eclipseVersionPrefix + releasedTrainLabel + ")",
          releasedTrainAndVersion.getIUs(), emfRepositoryLocation);
    }

    for (int i = 0; i < size; i++)
    {
      TrainAndVersion entry = list.get(size - i - 1);
      String train = entry.getTrain();
      String trainLabel = getTrainLabel(train);
      Version version = entry.getVersion();

      String name = train;
      if (ECLIPSE_PLATFORM_SDK_PRODUCT_ID.equals(id))
      {
        Version eclipseVersion = entry.getIUs().values().iterator().next().iterator().next().getVersion();
        name = eclipseVersion.getSegment(0) + "." + eclipseVersion.getSegment(1);
        trainLabel = name + " - " + trainLabel;
      }

      addProductVersion(log, product, version, IS_RANGE_NARROW ? VersionSegment.MINOR : VersionSegment.MAJOR, entry.getTrainURI(), entry.getEPPURI(), train,
          eppMetaDataRepositories, name, trainLabel, p2TaskLabel + " (" + trainLabel + ")", entry.getIUs(), emfRepositoryLocation);
    }

    System.out.println(log);
  }

  private void generate(final String train) throws ProvisionException, URISyntaxException
  {
    {
      StringBuilder log = new StringBuilder();

      URI originalEPPURI = URI.createURI(PACKAGES + "/" + train);
      URI eppURI = originalEPPURI;
      log.append(eppURI);
      boolean isStaging = train.equals(stagingTrain);
      if (isStaging)
      {
        log.append(" -> ").append(stagingEPPLocation);
      }

      URI effectiveEPPURI = isStaging ? stagingEPPLocation : eppURI;
      IMetadataRepository eppMetaDataRepository = manager.loadRepository(new java.net.URI(effectiveEPPURI.toString()), null);
      IMetadataRepository latestEPPMetaDataRepository = isStaging && stagingUseComposite || compositeTrains.contains(train) ? eppMetaDataRepository
          : getLatestRepository(manager, eppMetaDataRepository);
      if (latestEPPMetaDataRepository != eppMetaDataRepository)
      {
        URI latestLocation = URI.createURI(latestEPPMetaDataRepository.getLocation().toString());
        log.append(" -> ").append(latestLocation);
        URI relativeLocation = URI.createURI(latestLocation.toString()).deresolve(URI.createURI(effectiveEPPURI.toString()).appendSegment(""));
        if (relativeLocation.isRelative())
        {
          URI actualLatestEPPURI = URI.createURI(URI.createURI(eppURI.toString()).appendSegments(relativeLocation.segments()).toString());
          try
          {
            manager.loadRepository(new java.net.URI(actualLatestEPPURI.toString()), null);
            log.append(" -> ").append(actualLatestEPPURI);
            eppURI = trimEmptyTrailingSegment(actualLatestEPPURI);
          }
          catch (Throwable throwable)
          {
            // Ignore;
          }
        }
      }
      else if (isStaging && stagingEPPLocationIsActual)
      {
        eppURI = stagingEPPLocation;
        log.append(" -> ").append(stagingEPPLocation);
      }

      log.append('\n');

      synchronized (eppMetaDataRepositories)
      {
        eppMetaDataRepositories.put(train, eppMetaDataRepository);
      }

      Map<String, IInstallableUnit> ius = new HashMap<String, IInstallableUnit>();

      URI releaseURI = URI.createURI(RELEASES + "/" + train);
      log.append(releaseURI);

      IMetadataRepository releaseMetaDataRepository = loadLatestRepository(manager, originalEPPURI, isStaging ? stagingTrainLocation : releaseURI,
          !(compositeTrains.contains(train) || isStaging && stagingUseComposite));
      if (compositeTrains.contains(train) || isStaging && stagingUseComposite)
      {
        URI latestURI = trimEmptyTrailingSegment(URI.createURI(releaseMetaDataRepository.getLocation().toString()));
        if (!latestURI.equals(releaseURI))
        {
          releaseURI = latestURI.trimSegments(1);
        }
      }
      else
      {
        releaseURI = trimEmptyTrailingSegment(URI.createURI(releaseMetaDataRepository.getLocation().toString()));
      }

      log.append(" -> ").append(releaseURI).append('\n');

      log.append(generateEclipsePlatformProduct(train));

      generateFullTrainProduct(train, releaseMetaDataRepository, releaseURI);

      String mostRecentTrain = getMostRecentTrain();
      Set<String> requirements = new HashSet<String>();

      for (IInstallableUnit iu : P2Util.asIterable(eppMetaDataRepository.query(QueryUtil.createLatestIUQuery(), null)))
      {
        String fragment = iu.getProperty("org.eclipse.equinox.p2.type.fragment");
        if ("true".equals(fragment))
        {
          continue;
        }

        String label = iu.getProperty("org.eclipse.equinox.p2.name");
        if (label == null || label.startsWith("%") || label.equals("Uncategorized"))
        {
          // Ensure that this is removed later,
          // but that the requirements are still processed for filtering roots.
          requirements.add(iu.getId());
        }
        String id = iu.getId();
        if ("epp.package.standard".equals(id))
        {
          label = "Eclipse Standard";
        }

        IInstallableUnit existingIU = ius.get(id);
        if (existingIU == null)
        {
          ius.put(id, iu);
          synchronized (labels)
          {
            String existingLabel = labels.get(id);
            if (existingLabel == null || train.equals(mostRecentTrain))
            {
              labels.put(id, label);
            }
          }
        }
      }

      for (IInstallableUnit iu : ius.values())
      {
        for (IRequirement requirement : iu.getRequirements())
        {
          if (requirement instanceof IRequiredCapability)
          {
            IRequiredCapability capability = (IRequiredCapability)requirement;
            IMatchExpression<IInstallableUnit> filter = capability.getFilter();
            if (filter != null)
            {
              String value = RequirementImpl.formatMatchExpression(filter);
              if (EPP_INSTALL_ROOTS_FILTER.equals(value))
              {
                continue;
              }
            }

            if ("org.eclipse.equinox.p2.iu".equals(capability.getNamespace()))
            {
              requirements.add(capability.getName());
            }
          }
        }
      }

      ius.keySet().removeAll(requirements);

      for (String specialProductID : SPECIAL_PRODUCT_IDS)
      {
        for (IInstallableUnit iu : P2Util
            .asIterable(releaseMetaDataRepository.query(QueryUtil.createLatestQuery(QueryUtil.createIUQuery(specialProductID)), null)))
        {
          String id = iu.getId();
          String label = iu.getProperty("org.eclipse.equinox.p2.name");
          ius.put(id, iu);
          synchronized (labels)
          {
            labels.put(id, label);
          }
        }
      }

      LOOP: for (Map.Entry<String, IInstallableUnit> entry : ius.entrySet())
      {
        String id = entry.getKey();
        String label = labels.get(id);
        IInstallableUnit iu = entry.getValue();
        final Version version = iu.getVersion();
        log.append("  ").append(label).append("  --  ").append(id).append(" ").append(version).append('\n');

        Map<String, Set<IInstallableUnit>> versionIUs = new HashMap<String, Set<IInstallableUnit>>();
        gatherReleaseIUs(versionIUs, iu, releaseMetaDataRepository, eppMetaDataRepository);
        filterRoots(versionIUs);

        synchronized (trainsAndVersions)
        {
          List<TrainAndVersion> list = trainsAndVersions.get(id);
          if (list == null)
          {
            list = new ArrayList<TrainAndVersion>();
            trainsAndVersions.put(id, list);
          }

          for (int i = 0, size = list.size(); i < size; ++i)
          {
            TrainAndVersion trainAndVersion = list.get(i);
            if (compareTrains(trainAndVersion.train, train) > 0)
            {
              list.add(i, new TrainAndVersion(train, version, releaseURI, eppURI, versionIUs));
              continue LOOP;
            }
          }

          list.add(new TrainAndVersion(train, version, releaseURI, eppURI, versionIUs));
        }
      }

      System.out.println(log.toString());
    }
  }

  private String generateEclipsePlatformProduct(String train) throws ProvisionException, URISyntaxException
  {
    StringBuilder log = new StringBuilder();
    URI eclipsePlatformSite = getEclipsePlatformSite(train);
    log.append(eclipsePlatformSite + " -> ");
    IMetadataRepository repository = loadLatestRepository(manager, null, eclipsePlatformSite, true);
    log.append(repository.getLocation() + "\n");

    Map<String, Set<IInstallableUnit>> versionIUs = new TreeMap<String, Set<IInstallableUnit>>();
    @SuppressWarnings("unchecked")
    IQuery<IInstallableUnit> query = QueryUtil.createCompoundQuery((List<IQuery<IInstallableUnit>>)(List<?>)Arrays
        .asList(new IQuery<?>[] { QueryUtil.createIUQuery("org.eclipse.sdk.ide"), QueryUtil.createLatestIUQuery() }), true);
    IInstallableUnit ide = null;
    for (IInstallableUnit iu : P2Util.asIterable(repository.query(query, null)))
    {
      String id = iu.getId();
      CollectionUtil.add(versionIUs, id, iu);
      ide = iu;
    }

    if (ide != null)
    {
      synchronized (labels)
      {
        labels.put(ECLIPSE_PLATFORM_SDK_PRODUCT_ID, "Eclipse SDK");
      }

      URI releaseURI = eclipsePlatformSite.toString().contains("I-builds") ? eclipsePlatformSite : URI.createURI(repository.getLocation().toString());
      synchronized (trainsAndVersions)
      {
        List<TrainAndVersion> list = trainsAndVersions.get(ECLIPSE_PLATFORM_SDK_PRODUCT_ID);
        if (list == null)
        {
          list = new ArrayList<TrainAndVersion>();
          trainsAndVersions.put(ECLIPSE_PLATFORM_SDK_PRODUCT_ID, list);
        }

        boolean added = false;
        for (int i = 0, size = list.size(); i < size; ++i)
        {
          TrainAndVersion trainAndVersion = list.get(i);
          if (compareTrains(trainAndVersion.train, train) > 0)
          {
            list.add(i, new TrainAndVersion(train, ide.getVersion(), releaseURI, null, versionIUs));
            added = true;
            break;
          }
        }

        if (!added)
        {
          list.add(new TrainAndVersion(train, ide.getVersion(), releaseURI, null, versionIUs));
        }
      }
    }

    return log.toString();
  }

  private void generateFullTrainProduct(String train, IMetadataRepository releaseMetaDataRepository, URI releaseURI)
  {
    Map<String, IInstallableUnit> ius = new HashMap<String, IInstallableUnit>();

    Set<IInstallableUnit> categories = new HashSet<IInstallableUnit>();
    Set<String> categorizedIUs = new HashSet<String>();
    for (IInstallableUnit iu : P2Util.asIterable(releaseMetaDataRepository.query(QueryUtil.createLatestIUQuery(), null)))
    {
      String id = iu.getId();
      IInstallableUnit existingIU = ius.get(id);
      if (existingIU == null)
      {
        ius.put(id, iu);
      }

      boolean isIncludedCategory = "true".equalsIgnoreCase(iu.getProperty(QueryUtil.PROP_TYPE_CATEGORY)) && !"EclipseRT Target Platform Components".equals(id);
      if (isIncludedCategory)
      {
        categories.add(iu);
        categorizedIUs.add(id);
      }
    }

    categorizedIUs.addAll(SPECIAL_PRODUCT_IDS);
    ius.keySet().retainAll(categorizedIUs);

    Map<String, Set<IInstallableUnit>> versionIUs = new TreeMap<String, Set<IInstallableUnit>>();
    for (IInstallableUnit iu : ius.values())
    {
      String id = iu.getId();
      CollectionUtil.add(versionIUs, id, iu);
    }

    synchronized (labels)
    {
      labels.put(ALL_PRODUCT_ID, "Eclipse Eierlegende Wollmilchsau");
    }

    synchronized (trainsAndVersions)
    {
      List<TrainAndVersion> list = trainsAndVersions.get(ALL_PRODUCT_ID);
      if (list == null)
      {
        list = new ArrayList<TrainAndVersion>();
        trainsAndVersions.put(ALL_PRODUCT_ID, list);
      }

      for (String specialProductID : SPECIAL_PRODUCT_IDS)
      {
        IInstallableUnit ide = ius.get(specialProductID);
        boolean added = false;
        for (int i = 0, size = list.size(); i < size; ++i)
        {
          TrainAndVersion trainAndVersion = list.get(i);
          if (compareTrains(trainAndVersion.train, train) > 0)
          {
            list.add(i, new TrainAndVersion(train, ide.getVersion(), releaseURI, null, versionIUs));
            added = true;
            break;
          }
        }

        if (!added)
        {
          list.add(new TrainAndVersion(train, ide.getVersion(), releaseURI, null, versionIUs));
        }
      }
    }
  }

  private URI trimEmptyTrailingSegment(URI uri) throws URISyntaxException
  {
    if (uri.hasTrailingPathSeparator())
    {
      return uri.trimSegments(1);
    }
    return uri;
  }

  private IMetadataRepositoryManager getMetadataRepositoryManager()
  {
    try
    {
      File agentLocation = File.createTempFile("test-", "-agent");
      agentLocation.delete();
      agentLocation.mkdirs();

      Agent agent = new AgentImpl(null, agentLocation);
      IMetadataRepositoryManager manager = agent.getMetadataRepositoryManager();
      return manager;
    }
    catch (IOException ex)
    {
      throw new IORuntimeException(ex);
    }
  }

  private IMetadataRepository getLatestRepository(IMetadataRepositoryManager manager, IMetadataRepository repository)
      throws URISyntaxException, ProvisionException
  {
    IMetadataRepository result = repository;
    if (!isLatestReleased() && repository instanceof ICompositeRepository<?>)
    {
      ICompositeRepository<?> compositeRepository = (ICompositeRepository<?>)repository;
      long latest = Integer.MIN_VALUE;
      for (java.net.URI childURI : compositeRepository.getChildren())
      {
        childURI = new java.net.URI(trimEmptyTrailingSegment(URI.createURI(childURI.toString())).toString());
        IMetadataRepository childRepository = manager.loadRepository(childURI, null);
        String value = childRepository.getProperties().get(IRepository.PROP_TIMESTAMP);
        long timestamp = Long.parseLong(value);
        if (timestamp > latest && !childURI.toString().endsWith("categories"))
        {
          result = childRepository;
          latest = timestamp;
        }
      }
    }

    return result;
  }

  private IMetadataRepository loadLatestRepository(IMetadataRepositoryManager manager, URI eppURI, URI releaseURI, boolean loadLatestChild)
      throws URISyntaxException, ProvisionException
  {
    IMetadataRepository releaseMetaDataRepository = manager.loadRepository(new java.net.URI(releaseURI.toString()), null);
    IMetadataRepository result = releaseMetaDataRepository;
    if (loadLatestChild && releaseMetaDataRepository instanceof ICompositeRepository<?>)
    {
      ICompositeRepository<?> compositeRepository = (ICompositeRepository<?>)releaseMetaDataRepository;
      long latest = Integer.MIN_VALUE;
      for (java.net.URI childURI : compositeRepository.getChildren())
      {
        String trimmedChildURI = trimEmptyTrailingSegment(URI.createURI(childURI.toString())).toString();
        if (!trimmedChildURI.startsWith(PACKAGES) && !trimmedChildURI.startsWith(HTTPS_PACKAGES))
        {
          childURI = new java.net.URI(trimmedChildURI);
          IMetadataRepository childRepository = manager.loadRepository(childURI, null);
          String value = childRepository.getProperties().get(IRepository.PROP_TIMESTAMP);
          long timestamp = Long.parseLong(value);
          if (timestamp > latest)
          {
            result = childRepository;
            latest = timestamp;
          }
        }
      }
    }

    return result;
  }

  @SuppressWarnings("unused")
  private void addIntegrationVersions(ProductCatalog productCatalog)
  {
    List<String> trains = Arrays.asList(getTrains());
    int platformVersion = trains.indexOf(getMostRecentTrain()) - trains.indexOf("neon") + 6;
    for (Product product : productCatalog.getProducts())
    {
      if (SPECIAL_PRODUCT_IDS.contains(product.getName()))
      {
        EList<ProductVersion> versions = product.getVersions();
        ProductVersion version = versions.get(0);
        ProductVersion integrationVersion = EcoreUtil.copy(version);
        integrationVersion.setName("integration");
        integrationVersion.setLabel(version.getLabel().replace("Latest", "Integration"));
        P2Task p2Task = (P2Task)integrationVersion.getSetupTasks().get(0);
        p2Task.getRepositories().add(P2Factory.eINSTANCE.createRepository("http://download.eclipse.org/eclipse/updates/4." + platformVersion + "-I-builds"));
        versions.add(0, integrationVersion);
      }
    }
  }

  private void checkVersionRanges(ProductCatalog productCatalog)
  {
    if (!isLatestReleased())
    {
      for (Product product : productCatalog.getProducts())
      {
        EList<ProductVersion> versions = product.getVersions();
        if (versions.size() > 3)
        {
          ProductVersion latestReleaseVersion = versions.get(1);
          ProductVersion latestDevelopmentVersion = versions.get(2);

          if (getMostRecentTrain().equals(latestDevelopmentVersion.getName()))
          {
            P2Task latestReleaseP2Task = (P2Task)latestReleaseVersion.getSetupTasks().get(0);
            P2Task latestDevelopmentP2Task = (P2Task)latestDevelopmentVersion.getSetupTasks().get(0);

            for (Requirement developmentRequirement : latestDevelopmentP2Task.getRequirements())
            {
              String name = developmentRequirement.getName();
              for (Requirement releaseRequirement : latestReleaseP2Task.getRequirements())
              {
                if (name.equals(releaseRequirement.getName()))
                {
                  VersionRange developmentVersionRange = developmentRequirement.getVersionRange();
                  VersionRange releaseVersionRange = releaseRequirement.getVersionRange();
                  if (developmentVersionRange.equals(releaseVersionRange))
                  {
                    Version minimumVersion = developmentVersionRange.getMinimum();
                    Version maximumVersion = developmentVersionRange.getMaximum();
                    if (minimumVersion instanceof OSGiVersion && maximumVersion instanceof OSGiVersion)
                    {
                      OSGiVersion minimum = (OSGiVersion)minimumVersion;
                      OSGiVersion maximum = (OSGiVersion)maximumVersion;
                      int major = minimum.getMajor();
                      if (major == maximum.getMajor())
                      {
                        developmentRequirement.setVersionRange(new VersionRange(minimum, true, Version.createOSGi(major, maximum.getMinor() + 1, 0), false));
                      }
                    }
                  }

                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  private void postProcess(ProductCatalog productCatalog)
  {
    for (Iterator<Product> it = productCatalog.getProducts().iterator(); it.hasNext();)
    {
      Product product = it.next();
      String id = product.getName();

      if (EXCLUDED_IDS.contains(id))
      {
        it.remove();
      }

      if ("epp.package.standard".equals(id))
      {
        for (ProductVersion version : product.getVersions())
        {
          if (version.getLabel().contains("Mars"))
          {
            P2Task task = (P2Task)version.getSetupTasks().get(0);
            Requirement requirement = task.getRequirements().get(0);
            requirement.setName("epp.package.committers");
          }
        }
      }
    }
  }

  private void gatherReleaseIUs(Map<String, Set<IInstallableUnit>> releaseIUs, IInstallableUnit iu, IMetadataRepository releaseMetaDataRepository,
      IMetadataRepository eppMetaDataRepository)
  {
    for (IRequirement requirement : iu.getRequirements())
    {
      if (requirement instanceof IRequiredCapability)
      {
        IRequiredCapability capability = (IRequiredCapability)requirement;
        String capabilityName = capability.getName();
        if (capabilityName.endsWith(Requirement.FEATURE_SUFFIX))
        {
          IMatchExpression<IInstallableUnit> filter = capability.getFilter();
          if (filter != null)
          {
            String value = RequirementImpl.formatMatchExpression(filter);
            if (EPP_INSTALL_ROOTS_FILTER.equals(value))
            {
              continue;
            }
          }

          @SuppressWarnings("unchecked")
          IQueryable<IInstallableUnit> queriable = new CompoundQueryable<IInstallableUnit>(
              new IQueryable[] { releaseMetaDataRepository, eppMetaDataRepository });
          IQuery<IInstallableUnit> query = QueryUtil.createIUQuery(capabilityName, capability.getRange());
          for (IInstallableUnit requiredIU : P2Util.asIterable(queriable.query(query, null)))
          {
            if (CollectionUtil.add(releaseIUs, capabilityName, requiredIU))
            {
              gatherReleaseIUs(releaseIUs, requiredIU, releaseMetaDataRepository, eppMetaDataRepository);
            }
          }
        }
      }
    }
  }

  private void filterRoots(Map<String, Set<IInstallableUnit>> releaseIUs)
  {
    for (Iterator<Map.Entry<String, Set<IInstallableUnit>>> it = releaseIUs.entrySet().iterator(); it.hasNext();)
    {
      String id = it.next().getKey();
      if (!id.endsWith("feature.group") || id.startsWith("org.eclipse.epp") || id.endsWith("epp.feature.group"))
      {
        it.remove();
      }
    }
  }

  private void addProductVersion(StringBuilder log, Product product, Version version, VersionSegment versionSegment, URI trainURI, URI eppURI, String train,
      Map<String, IMetadataRepository> eppMetaDataRepositories, String name, String label, String p2TaskLabel, Map<String, Set<IInstallableUnit>> ius,
      String emfRepositoryLocation)
  {
    log.append("  ").append(label);

    ProductVersion productVersion = SetupFactory.eINSTANCE.createProductVersion();
    productVersion.setName(name);
    productVersion.setLabel(label);
    product.getVersions().add(productVersion);

    String productName = product.getName();
    VersionRange versionRange = createVersionRange(version, versionSegment);

    Repository packageRepository = null;
    if (eppURI != null)
    {
      packageRepository = P2Factory.eINSTANCE.createRepository();

      // Check if the product IU is really in the EPP repository we will reference.
      // It might be the case that it's only present in an older child of the composite, in which case, we'll must point at the composites.
      if (!ECLIPSE_PLATFORM_SDK_PRODUCT_ID.equals(productName) && !ALL_PRODUCT_ID.equals(productName) && !SPECIAL_PRODUCT_IDS.contains(productName)
          && train.equals(stagingTrain))
      {
        try
        {
          IMetadataRepository eppMetadataRepository = manager.loadRepository(new java.net.URI(eppURI.toString()), null);
          if (!eppMetadataRepository.query(QueryUtil.createIUQuery(productName), null).iterator().hasNext())
          {
            eppURI = eppURI.trimSegments(1);
            trainURI = trainURI.trimSegments(1);
            log.append(" {missing}");
          }
        }
        catch (Exception ex)
        {
          throw new RuntimeException(ex);
        }
      }

      packageRepository.setURL(eppURI.toString());
    }

    Repository releaseRepository = P2Factory.eINSTANCE.createRepository();
    releaseRepository.setURL(trainURI.toString());

    if (!ECLIPSE_PLATFORM_SDK_PRODUCT_ID.equals(productName))
    {
      releaseRepository.getAnnotations().add(BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_RELEASE_TRAIN));
    }

    P2Task p2Task = SetupP2Factory.eINSTANCE.createP2Task();
    p2Task.setLabel(p2TaskLabel);

    if (!ALL_PRODUCT_ID.equals(productName) && !ECLIPSE_PLATFORM_SDK_PRODUCT_ID.equals(productName))
    {
      Requirement requirement = P2Factory.eINSTANCE.createRequirement();
      requirement.setName(productName);
      requirement.setVersionRange(versionRange);
      p2Task.getRequirements().add(requirement);
      addRootIURequirements(p2Task.getRequirements(), versionSegment, ius);
      addAdditionalInstallRootIURequirements(p2Task.getRequirements(), productName, train, eppMetaDataRepositories, ius);
    }
    else
    {
      addAllRootIURequirements(p2Task.getRequirements(), versionSegment, ius);
    }

    if (!SPECIAL_PRODUCT_IDS.contains(productName) && packageRepository != null)
    {
      p2Task.getRepositories().add(packageRepository);
    }

    p2Task.getRepositories().add(releaseRepository);

    if (compareTrains(train, "luna") < 0)
    {
      Repository emfRepository = P2Factory.eINSTANCE.createRepository(emfRepositoryLocation);
      p2Task.getRepositories().add(emfRepository);
    }

    productVersion.getSetupTasks().add(p2Task);

    String idPrefix = "tooling"
        + (SPECIAL_PRODUCT_IDS.contains(productName) || ALL_PRODUCT_ID.equals(productName) || ECLIPSE_PLATFORM_SDK_PRODUCT_ID.equals(productName)
            ? "epp.package.java"
            : productName)
        + ".ini.";

    Version maxJavaVersion = null;
    if (train.compareTo("neon") >= 0)
    {
      maxJavaVersion = Version.create("1.8.0");
    }

    IMetadataRepository eppMetaDataRepository = eppMetaDataRepositories.get(train);
    if (eppMetaDataRepository != null)
    {
      for (IInstallableUnit iu : P2Util.asIterable(eppMetaDataRepository.query(QueryUtil.createIUAnyQuery(), null)))
      {
        String id = iu.getId();
        if (id.startsWith(idPrefix) && (SPECIAL_PRODUCT_IDS.contains(productName) || ALL_PRODUCT_ID.equals(productName)
            || ECLIPSE_PLATFORM_SDK_PRODUCT_ID.equals(productName) || iu.getVersion().equals(version)))
        {
          Collection<ITouchpointData> touchpointDatas = iu.getTouchpointData();
          if (touchpointDatas != null)
          {
            for (ITouchpointData touchpointData : touchpointDatas)
            {
              ITouchpointInstruction instruction = touchpointData.getInstruction("configure");
              if (instruction != null)
              {
                String body = instruction.getBody();
                if (body != null)
                {
                  int pos = body.indexOf(JAVA_VERSION_PREFIX);
                  if (pos != -1)
                  {
                    pos += JAVA_VERSION_PREFIX.length();
                    Version javaVersion = Version.parseVersion(body.substring(pos, body.indexOf(')', pos)));
                    if (maxJavaVersion == null || maxJavaVersion.compareTo(javaVersion) < 0)
                    {
                      maxJavaVersion = javaVersion;
                    }

                    // IMatchExpression<IInstallableUnit> filter = iu.getFilter();
                    // Object[] parameters = filter.getParameters();
                    // if (parameters != null)
                    // {
                    // for (Object parameter : parameters)
                    // {
                    // if (parameter instanceof IFilterExpression)
                    // {
                    // IFilterExpression filterExpression = (IFilterExpression)parameter;
                    // System.out.println(" " + filterExpression.toString() + " --> " + javaVersion);
                    // }
                    // }
                    // }
                  }
                }
              }
            }
          }
        }
      }
    }

    if (maxJavaVersion != null)
    {
      String javaVersion = maxJavaVersion.toString();
      while (javaVersion.endsWith(".0"))
      {
        javaVersion = javaVersion.substring(0, javaVersion.length() - 2);
      }

      productVersion.setRequiredJavaVersion(javaVersion);
      log.append(" --> Java ").append(javaVersion);
    }

    Map<URI, Map<String, URI>> releases = sites.get(train);
    String productLabel = product.getLabel();
    String key = getKey(productLabel);
    URI siteURI = null;
    if (releases != null)
    {
      for (Map.Entry<URI, Map<String, URI>> productEntry : releases.entrySet())
      {
        Map<String, URI> productLocations = productEntry.getValue();
        siteURI = productLocations.get(key);
        if (siteURI == null)
        {
          if (product.getName().startsWith("epp.") || ALL_PRODUCT_ID.equals(product.getName()))
          {
            siteURI = productEntry.getKey();
          }
          else
          {
            siteURI = URI.createURI("https://download.eclipse.org/eclipse/downloads/");
          }

          log.append(" No version specific branding site -> ").append(siteURI);
        }
      }
    }

    if (siteURI != null)
    {
      BaseUtil.setAnnotation(productVersion, AnnotationConstants.ANNOTATION_BRANDING_INFO, AnnotationConstants.KEY_SITE_URI, siteURI.toString());
    }

    log.append('\n');
  }

  private String getKey(String productLabel)
  {
    if (productLabel.contains("Eierlegende"))
    {
      return "All";
    }
    else if (productLabel.contains("JavaScript"))
    {
      return "JavaScript";
    }
    else if (productLabel.contains("Enterprise") || productLabel.contains(" EE ") || productLabel.contains("Web"))
    {
      return "EE";
    }
    else if (productLabel.contains("Java Developer"))
    {
      return "Java";
    }
    else if (productLabel.contains("Mobile"))
    {
      return "Mobile";
    }
    else if (productLabel.contains("C/C++"))
    {
      return "C";
    }
    else if (productLabel.contains("Scout"))
    {
      return "Scout";
    }
    else if (productLabel.contains("Parallel"))
    {
      return "Parallel";
    }
    else if (productLabel.contains(" Report "))
    {
      return "Report";
    }
    else if (productLabel.contains("Automotive"))
    {
      return "Automotive";
    }
    else if (productLabel.contains("DSL"))
    {
      return "DSL";
    }
    else if (productLabel.contains("Modeling"))
    {
      return "Modeling";
    }
    else if (productLabel.contains("Testers"))
    {
      return "Testers";
    }
    else if (productLabel.contains("RCP"))
    {
      return "RCP";
    }
    else if (productLabel.contains("Standard") || productLabel.contains("Committers"))
    {
      return "Committers";
    }
    else if (productLabel.contains("PHP"))
    {
      return "PHP";
    }
    else if (productLabel.contains("Classic"))
    {
      return "Classic";
    }
    else if (productLabel.contains("SOA"))
    {
      return "SOA";
    }
    else if (productLabel.equals("Eclipse Platform"))
    {
      return "Platform";
    }
    else if (productLabel.equals("Eclipse SDK"))
    {
      return "SDK";
    }
    else if (productLabel.contains("Android"))
    {
      return "Android";
    }
    else if (productLabel.contains("Rust"))
    {
      return "Rust";
    }
    else if (productLabel.contains("Scientific"))
    {
      return "Scientific";
    }

    throw new RuntimeException("No key for " + productLabel);
  }

  private VersionRange createVersionRange(Version version, VersionSegment versionSegment)
  {
    VersionRange versionRange = P2Factory.eINSTANCE.createVersionRange(version, versionSegment);
    if (versionSegment == VersionSegment.MAJOR)
    {
      // When expanding to major, we only want to expand the upper bound to major and the lower bound to minor.
      VersionRange minorVersionRange = P2Factory.eINSTANCE.createVersionRange(version, VersionSegment.MINOR);
      versionRange = new VersionRange(minorVersionRange.getMinimum(), true, versionRange.getMaximum(), false);
    }

    return versionRange;
  }

  private void addRootIURequirements(EList<Requirement> requirements, VersionSegment versionSegment, Map<String, Set<IInstallableUnit>> ius)
  {
    for (String iu : getRootIUs())
    {
      Set<IInstallableUnit> rootIUs = ius.get(iu);
      if (rootIUs != null)
      {
        addRootIURequirements(requirements, versionSegment, iu, rootIUs);
      }
    }
  }

  private void addAllRootIURequirements(EList<Requirement> requirements, VersionSegment versionSegment, Map<String, Set<IInstallableUnit>> ius)
  {
    for (Map.Entry<String, Set<IInstallableUnit>> entry : ius.entrySet())
    {
      addRootIURequirements(requirements, versionSegment, entry.getKey(), entry.getValue());
    }
  }

  private void addRootIURequirements(EList<Requirement> requirements, VersionSegment versionSegment, String id, Set<IInstallableUnit> rootIUs)
  {
    VersionRange range = null;
    for (IInstallableUnit rootIU : rootIUs)
    {
      boolean isCategory = "true".equalsIgnoreCase(rootIU.getProperty(QueryUtil.PROP_TYPE_CATEGORY));
      if (isCategory)
      {
        Requirement requirement = P2Factory.eINSTANCE.createRequirement();
        requirement.setName(id);
        requirement.setVersionRange(VersionRange.emptyRange);
        if (id.equals("Linux Tools"))
        {
          requirement.setFilter("(osgi.os=linux)");
        }

        requirements.add(requirement);
        return;
      }

      Version version = rootIU.getVersion();
      VersionRange versionRange = createVersionRange(version, versionSegment);
      if (range == null)
      {
        range = versionRange;
      }
    }

    if (range != null)
    {
      Requirement requirement = P2Factory.eINSTANCE.createRequirement();
      requirement.setName(id);
      requirement.setVersionRange(range);
      requirements.add(requirement);
    }
  }

  private void addAdditionalInstallRootIURequirements(EList<Requirement> requirements, String productName, String train,
      Map<String, IMetadataRepository> eppMetaDataRepositories, Map<String, Set<IInstallableUnit>> ius)
  {
    IMetadataRepository eppMetadataRepository = eppMetaDataRepositories.get(train);
    IInstallableUnit maxProductIU = null;
    if (eppMetadataRepository != null)
    {
      for (IInstallableUnit productIU : eppMetadataRepository.query(QueryUtil.createIUQuery(productName), null))
      {
        if (maxProductIU == null || productIU.getVersion().compareTo(maxProductIU.getVersion()) > 0)
        {
          maxProductIU = productIU;
        }
      }
    }

    if (maxProductIU != null)
    {
      Set<String> rootInstallIUs = getRootInstallIUs(train, maxProductIU);
      if (rootInstallIUs != null)
      {
        for (String id : rootInstallIUs)
        {
          Requirement requirement = P2Factory.eINSTANCE.createRequirement();
          requirement.setName(id);
          requirements.add(requirement);
        }
      }
    }
  }

  private String getTrainLabel(String train)
  {
    return Character.toString(Character.toUpperCase(train.charAt(0))) + train.substring(1);
  }

  private void attachBrandingInfos(final StringBuilder log, final Product product)
  {
    String name = product.getName();
    if (name.equals("org.eclipse.platform.ide"))
    {
      product.setDescription("This package contains the absolute minimal IDE, suitable only as a base for installing other tools.");
      return;
    }

    if (name.equals("org.eclipse.sdk.ide"))
    {
      product.setDescription("This package contains the IDE provided by the Eclipse project's platform build.");
      return;
    }

    if (name.equals(ALL_PRODUCT_ID))
    {
      product.setDescription(
          "This package contains all categorized features of the release train. It can do <a href='https://en.wiktionary.org/wiki/eierlegende_Wollmilchsau'>everything</a>, but it's not pretty.");
      // addImageURI(product, "http://upload.wikimedia.org/wikipedia/commons/a/a7/Wollmilchsau.png");
    }

    if (name.equals(ECLIPSE_PLATFORM_SDK_PRODUCT_ID))
    {
      product.setDescription("This package contains the IDE provided by the Eclipse project's platform build.");
    }

    if (name.startsWith("epp.package."))
    {
      name = name.substring("epp.package.".length());
    }

    final String staticIconURL = ICONS.get(name);
    if (staticIconURL != null)
    {
      addImageURI(product, staticIconURL);
    }

    String[] trains = getTrains();
    for (int i = trains.length; i >= 0; --i)
    {
      InputStream in = null;

      String branch = i == trains.length ? "master" : trains[i].toUpperCase();
      String url = "http://git.eclipse.org/c/epp/org.eclipse.epp.packages.git/plain/packages/org.eclipse.epp.package." + name + ".feature/epp.website.xml"
          + "?h=" + branch;
      try
      {
        in = new URL(url).openStream();
        log.append(url).append('\n');

        DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
        Element rootElement = XMLUtil.loadRootElement(documentBuilder, in);
        XMLUtil.handleElementsByTagName(rootElement, "packageMetaData", new XMLUtil.ElementHandler()
        {
          public void handleElement(Element element) throws Exception
          {
            if (staticIconURL != null)
            {
              log.append(staticIconURL).append('\n');
            }
            else
            {
              String iconurl = element.getAttribute("iconurl");
              if (iconurl != null)
              {
                addImageURI(product, iconurl);
                log.append(iconurl).append('\n');
              }
            }

            XMLUtil.handleChildElements(element, new XMLUtil.ElementHandler()
            {
              public void handleElement(Element childElement) throws Exception
              {
                String localName = childElement.getLocalName();
                if ("description".equals(localName))
                {
                  String description = childElement.getTextContent();
                  if (description != null)
                  {
                    product.setDescription(description.trim());
                  }
                }
              }
            });
          }
        });

        return;
      }
      catch (FileNotFoundException ex)
      {
        log.append(ex.getMessage()).append(" (FAILED)\n");
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
        System.err.println("url=" + url);
      }
      finally
      {
        IOUtil.closeSilent(in);
      }
    }

    if (staticIconURL != null)
    {
      log.append(staticIconURL).append('\n');
    }
  }

  private void addImageURI(Product product, String imageURI)
  {
    if (ICON_DEFAULT.equals(imageURI))
    {
      return;
    }

    // imageURI = IOUtil.encodeImageData(imageURI);

    EMap<String, String> brandingInfos = getBrandingInfos(product);
    if (!brandingInfos.containsKey(AnnotationConstants.KEY_IMAGE_URI))
    {
      brandingInfos.put(AnnotationConstants.KEY_IMAGE_URI, imageURI);
    }
  }

  private EMap<String, String> getBrandingInfos(Product product)
  {
    Annotation annotation = product.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
    if (annotation == null)
    {
      annotation = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
      product.getAnnotations().add(annotation);
    }

    return annotation.getDetails();
  }

  private void getPackageBrandingSites()
  {
    PackageLocationLoader packageLocationLoader = new PackageLocationLoader(this);
    Set<URI> locations = new LinkedHashSet<URI>();
    for (String train : getTrains())
    {
      InputStream packages = null;
      try
      {
        URI packagesURI = PACKAGES_URI.trimSegments(1).appendSegment(train).appendSegment("");
        URL packagesURL = new URL(packagesURI.toString());
        packages = packagesURL.openStream();
        List<String> lines = IOUtil.readLines(packages, "UTF-8");
        Pattern pattern = Pattern.compile("<li class=\"[0-9][^>]+><a href=\"([^\"]+)\"[^>]*>([\\w]+) Packages</a></li>");
        for (String line : lines)
        {
          Matcher matcher = pattern.matcher(line);
          if (matcher.find())
          {
            URI siteURI = URI.createURI(matcher.group(1)).resolve(PACKAGES_URI);
            System.out.println(train + " -> " + siteURI);
            Map<URI, Map<String, URI>> releaseLocations = new LinkedHashMap<URI, Map<String, URI>>();
            Map<String, URI> packageLocations = new LinkedHashMap<String, URI>();
            releaseLocations.put(siteURI, packageLocations);
            sites.put(train, releaseLocations);
            locations.add(siteURI);
            break;
          }
        }

      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }
      finally
      {
        IOUtil.closeSilent(packages);
      }
    }

    URI releasePackages = URI.createURI("http://download.eclipse.org/eclipse/downloads/");
    getPlatformPackageBrandingSites(releasePackages);
    getPlatformPackageBrandingSites(URI.createURI("http://archive.eclipse.org/eclipse/downloads/"));

    packageLocationLoader.perform(locations);
  }

  private void getGeneralPackageBrandingSites(URI releasePackages)
  {
    InputStream inputStream = null;
    try
    {
      URL releasePackagesURL = new URL(releasePackages.toString());
      inputStream = releasePackagesURL.openStream();
      List<String> lines = IOUtil.readLines(inputStream, "UTF-8");
      Pattern pattern = Pattern.compile("^\\s+<a href=\"([^\"]+)\" title=[^>]*>([^<]+)</a>\\s*$");
      // <a href="/downloads/packages/release/oxygen/3a/eclipse-ide-eclipse-committers" title="Eclipse IDE for Eclipse Committers">Eclipse IDE for Eclipse
      // Committers</a>
      for (String line : lines)
      {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find())
        {
          String packageName = matcher.group(2);
          String key = getKey(packageName);
          URI packageURI = URI.createURI(matcher.group(1));
          if (packageURI.isRelative())
          {
            packageURI = packageURI.resolve(releasePackages);
          }

          synchronized (sites)
          {
            LOOP: for (Entry<String, Map<URI, Map<String, URI>>> releaseEntry : sites.entrySet())
            {
              for (Map.Entry<URI, Map<String, URI>> productEntry : releaseEntry.getValue().entrySet())
              {
                if (releasePackages.equals(productEntry.getKey()))
                {
                  System.out.println(releaseEntry.getKey() + " -> " + packageName + " -> " + packageURI);
                  productEntry.getValue().put(key, packageURI);
                  break LOOP;
                }
              }
            }
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      IOUtil.closeSilent(inputStream);
    }
  }

  private void getPlatformPackageBrandingSites(URI releasePackages)
  {
    InputStream inputStream = null;
    try
    {
      URL releasePackagesURL = new URL(releasePackages.toString());
      inputStream = releasePackagesURL.openStream();
      List<String> lines = IOUtil.readLines(inputStream, "UTF-8");
      Pattern pattern = Pattern.compile("<a href=\"([^\"]+)\"[^>]*>(4\\.[^<]+)</a>");
      for (String line : lines)
      {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find())
        {
          String packageVersion = matcher.group(2);
          URI packageURI = URI.createURI(matcher.group(1));
          if (packageURI.isRelative())
          {
            packageURI = packageURI.resolve(releasePackages);
          }

          // Compute the train from the version number.
          int minorVersionIndex = packageVersion.indexOf('.', 3);
          int version = Integer.parseInt(minorVersionIndex == -1 ? packageVersion.substring(2) : packageVersion.substring(2, minorVersionIndex));
          List<String> trains = Arrays.asList(getTrains());
          int index = trains.indexOf("neon") - (6 - version);
          if (index >= 0 && index < trains.size())
          {
            String train = trains.get(index);

            synchronized (sites)
            {
              Map<URI, Map<String, URI>> releaseLocations = sites.get(train);
              if (releaseLocations != null)
              {
                for (Map<String, URI> packageLocations : releaseLocations.values())
                {
                  if (!packageLocations.containsKey("Platform"))
                  {
                    System.out.println(train + " -> " + packageVersion + " -> " + packageURI);
                    packageLocations.put("Platform", packageURI);
                    packageLocations.put("SDK", packageURI);
                  }

                  break;
                }
              }
            }
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      IOUtil.closeSilent(inputStream);
    }
  }

  private Set<String> getRootInstallIUs(String release, IInstallableUnit productIU)
  {
    Set<String> rootInstallIUs = new TreeSet<String>();
    for (IRequirement requirement : productIU.getRequirements())
    {
      if (requirement instanceof IRequiredCapability)
      {
        IRequiredCapability capability = (IRequiredCapability)requirement;
        IMatchExpression<IInstallableUnit> filter = capability.getFilter();
        if (filter != null)
        {
          String value = RequirementImpl.formatMatchExpression(filter);
          if (EPP_INSTALL_ROOTS_FILTER.equals(value))
          {
            rootInstallIUs.add(capability.getName());
          }
        }
      }
    }

    return rootInstallIUs;
  }

  /**
   * @author Eike Stepper
   */
  private static final class TrainAndVersion
  {
    private final String train;

    private final Version version;

    private final Map<String, Set<IInstallableUnit>> ius;

    private final URI trainURI;

    private URI eppURI;

    public TrainAndVersion(String train, Version version, URI trainURI, URI eppURI, Map<String, Set<IInstallableUnit>> ius)
    {
      this.train = train;
      this.version = version;
      this.trainURI = trainURI;
      this.eppURI = eppURI;
      this.ius = ius;
    }

    public String getTrain()
    {
      return train;
    }

    public Version getVersion()
    {
      return version;
    }

    public URI getTrainURI()
    {
      return trainURI;
    }

    public URI getEPPURI()
    {
      return eppURI;

    }

    public Map<String, Set<IInstallableUnit>> getIUs()
    {
      return ius;
    }

    @Override
    public String toString()
    {
      return "" + train + "->" + version;
    }
  }

  /**
   * @author Ed Merks
   */
  public static class PackageLocationLoader extends WorkerPool<PackageLocationLoader, URI, PackageLocationLoader.LoadJob>
  {
    private ProductCatalogGenerator generator;

    public PackageLocationLoader(ProductCatalogGenerator generator)
    {
      this.generator = generator;
    }

    /**
     * @author Ed Merks
     */
    public class LoadJob extends WorkerPool.Worker<URI, PackageLocationLoader>
    {
      private LoadJob(PackageLocationLoader loader, URI uri, int id, boolean secondary)
      {
        super("Load " + uri, loader, uri, id, secondary);
      }

      @Override
      protected IStatus perform(IProgressMonitor monitor)
      {
        generator.getGeneralPackageBrandingSites(getKey());
        return Status.OK_STATUS;
      }
    }

    @Override
    protected LoadJob createWorker(URI key, int workerID, boolean secondary)
    {
      return new LoadJob(this, key, workerID, secondary);
    }
  }

  /**
   * @author Ed Merks
   */
  public static class RepositoryLoader extends WorkerPool<RepositoryLoader, String, RepositoryLoader.LoadJob>
  {
    private ProductCatalogGenerator generator;

    public RepositoryLoader(ProductCatalogGenerator generator)
    {
      this.generator = generator;
    }

    /**
     * @author Ed Merks
     */
    public class LoadJob extends WorkerPool.Worker<String, RepositoryLoader>
    {
      private LoadJob(RepositoryLoader loader, String train, int id, boolean secondary)
      {
        super("Load " + train, loader, train, id, secondary);
      }

      @Override
      protected IStatus perform(IProgressMonitor monitor)
      {
        try
        {
          generator.generate(getKey());
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }

        return Status.OK_STATUS;
      }
    }

    @Override
    protected LoadJob createWorker(String key, int workerID, boolean secondary)
    {
      return new LoadJob(this, key, workerID, secondary);
    }
  }

  /**
   * @author Ed Merks
   */
  public static class ProductGenerator extends WorkerPool<ProductGenerator, String, ProductGenerator.GenerateJob>
  {
    private ProductCatalogGenerator generator;

    public ProductGenerator(ProductCatalogGenerator generator)
    {
      this.generator = generator;
    }

    /**
     * @author Ed Merks
     */
    public class GenerateJob extends WorkerPool.Worker<String, ProductGenerator>
    {
      private GenerateJob(ProductGenerator loader, String productID, int id, boolean secondary)
      {
        super("Load " + productID, loader, productID, id, secondary);
      }

      @Override
      protected IStatus perform(IProgressMonitor monitor)
      {
        try
        {
          generator.generateProduct(getKey());
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }

        return Status.OK_STATUS;
      }
    }

    @Override
    protected GenerateJob createWorker(String key, int workerID, boolean secondary)
    {
      return new GenerateJob(this, key, workerID, secondary);
    }
  }
}
