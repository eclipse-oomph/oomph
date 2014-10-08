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
package org.eclipse.oomph.setup.tests.data;

import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.internal.core.AgentImpl;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Factory;
import org.eclipse.oomph.util.CollectionUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.internal.p2.metadata.OSGiVersion;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.CompoundQueryable;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.ICompositeRepository;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class ProductCatalogGenerator implements IApplication
{
  private static final String PACKAGES = "http://download.eclipse.org/technology/epp/packages";

  private static final String RELEASES = "http://download.eclipse.org/releases";

  public Object start(IApplicationContext context) throws Exception
  {
    String[] arguments = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
    org.eclipse.emf.common.util.URI uri = null;
    if (arguments != null && arguments.length == 2 && "-generatorOutput".equals(arguments[0]))
    {
      uri = org.eclipse.emf.common.util.URI.createURI(arguments[1]);
    }

    generate(uri);
    return null;
  }

  public void stop()
  {
  }

  private String[] getTrains()
  {
    return new String[] { "juno", "kepler", "luna", "mars" };
  }

  private String[] getRootIUs()
  {
    return new String[] { "org.eclipse.platform.feature.group", "org.eclipse.rcp.feature.group", "org.eclipse.jdt.feature.group",
        "org.eclipse.pde.feature.group" };
  }

  private boolean isLatestReleased()
  {
    return false;
  }

  private boolean testNewUnreleasedProduct()
  {
    return false;
  }

  public void generate(org.eclipse.emf.common.util.URI uri)
  {
    final String[] TRAINS = getTrains();
    final String LATEST_TRAIN = TRAINS[TRAINS.length - 1];
    final boolean LATEST_RELEASED = !testNewUnreleasedProduct() && isLatestReleased();

    try
    {
      ProductCatalog productCatalog = SetupFactory.eINSTANCE.createProductCatalog();
      productCatalog.setName("org.eclipse.products");
      productCatalog.setLabel("Eclipse.org");

      InstallationTask installationTask = SetupFactory.eINSTANCE.createInstallationTask();
      installationTask.setID("installation");
      productCatalog.getSetupTasks().add(installationTask);

      Requirement oomphRequirement = P2Factory.eINSTANCE.createRequirement("org.eclipse.oomph.setup.feature.group");
      Repository oomphRepository = P2Factory.eINSTANCE.createRepository("${" + SetupProperties.PROP_UPDATE_URL + "}");
      Repository emfRepository = P2Factory.eINSTANCE.createRepository("http://download.eclipse.org/modeling/emf/emf/updates/2.10/core/R201405190339");

      P2Task p2Task = SetupP2Factory.eINSTANCE.createP2Task();
      p2Task.getRequirements().add(oomphRequirement);
      p2Task.getRepositories().add(oomphRepository);
      p2Task.getRepositories().add(emfRepository);
      productCatalog.getSetupTasks().add(p2Task);

      File agentLocation = File.createTempFile("test-", "-agent");
      agentLocation.delete();
      agentLocation.mkdirs();

      Agent agent = new AgentImpl(null, agentLocation);
      IMetadataRepositoryManager manager = agent.getMetadataRepositoryManager();

      final Map<String, List<TrainAndVersion>> trainsAndVersions = new HashMap<String, List<TrainAndVersion>>();
      final Map<String, String> labels = new HashMap<String, String>();

      for (final String train : TRAINS)
      {
        URI eppURI = new URI(PACKAGES + "/" + train);
        System.out.println(eppURI);
        IMetadataRepository eppMetaDataRepository = manager.loadRepository(eppURI, null);

        Map<String, IInstallableUnit> ius = new HashMap<String, IInstallableUnit>();

        URI releaseURI = new URI(RELEASES + "/" + train);
        System.out.println(releaseURI);
        IMetadataRepository releaseMetaDataRepository = manager.loadRepository(releaseURI, null);

        if (releaseMetaDataRepository instanceof ICompositeRepository<?>)
        {
          ICompositeRepository<?> compositeRepository = (ICompositeRepository<?>)releaseMetaDataRepository;
          long latest = Integer.MIN_VALUE;
          for (URI childURI : compositeRepository.getChildren())
          {
            String childURIString = childURI.toString();
            if (childURIString.endsWith("/"))
            {
              childURI = new URI(childURIString.substring(0, childURIString.length() - 1));
            }

            if (!childURI.equals(eppURI))
            {
              IMetadataRepository childRepository = manager.loadRepository(childURI, null);
              String value = childRepository.getProperties().get(IRepository.PROP_TIMESTAMP);
              long timestamp = Long.parseLong(value);
              if (timestamp > latest)
              {
                releaseURI = childURI;
                latest = timestamp;
              }
            }
          }
        }

        for (IInstallableUnit iu : P2Util.asIterable(eppMetaDataRepository.query(QueryUtil.createIUAnyQuery(), null)))
        {
          String fragment = iu.getProperty("org.eclipse.equinox.p2.type.fragment");
          if ("true".equals(fragment))
          {
            continue;
          }

          String label = iu.getProperty("org.eclipse.equinox.p2.name");
          if (label == null || label.startsWith("%") || label.equals("Uncategorized"))
          {
            continue;
          }

          String id = iu.getId();
          IInstallableUnit existingIU = ius.get(id);
          if (existingIU == null || existingIU.getVersion().compareTo(iu.getVersion()) < 0)
          {
            ius.put(id, iu);
            labels.put(id, label);
          }
        }

        Set<String> requirements = new HashSet<String>();
        for (IInstallableUnit iu : ius.values())
        {
          for (IRequirement requirement : iu.getRequirements())
          {
            if (requirement instanceof IRequiredCapability)
            {
              IRequiredCapability capability = (IRequiredCapability)requirement;
              if ("org.eclipse.equinox.p2.iu".equals(capability.getNamespace()))
              {
                requirements.add(capability.getName());
              }
            }
          }
        }

        for (String requirement : requirements)
        {
          ius.remove(requirement);
        }

        for (Map.Entry<String, IInstallableUnit> entry : ius.entrySet())
        {
          String id = entry.getKey();
          String label = labels.get(id);
          IInstallableUnit iu = entry.getValue();
          final Version version = iu.getVersion();
          System.out.println("  " + label + "  --  " + id + " " + version);

          List<TrainAndVersion> list = trainsAndVersions.get(id);
          if (list == null)
          {
            list = new ArrayList<TrainAndVersion>();
            trainsAndVersions.put(id, list);
          }

          Map<String, Set<IInstallableUnit>> versionIUs = new HashMap<String, Set<IInstallableUnit>>();
          gatherReleaseIUs(versionIUs, iu, releaseMetaDataRepository, eppMetaDataRepository);
          filterRoots(versionIUs);

          list.add(new TrainAndVersion(train, version, releaseURI, versionIUs));
        }

        System.out.println();
      }

      if (testNewUnreleasedProduct())
      {
        List<TrainAndVersion> list = new ArrayList<TrainAndVersion>();
        list.add(new TrainAndVersion("mars", null, null, null));

        trainsAndVersions.put("test.product", list);
        labels.put("test.product", "Eclipse Test Product");
      }

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
            String label1 = labels.get(id1);
            String label2 = labels.get(id2);
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
        String label = labels.get(id);

        List<TrainAndVersion> list = trainsAndVersions.get(id);
        int size = list.size();

        TrainAndVersion latestTrainAndVersion = list.get(size - 1);
        String latestTrain = latestTrainAndVersion.getTrain();
        String latestTrainLabel = getTrainLabel(latestTrain);
        Version latestVersion = latestTrainAndVersion.getVersion();
        Map<String, Set<IInstallableUnit>> latestTrainsIUs = latestTrainAndVersion.getIUs();

        if (latestTrain != LATEST_TRAIN)
        {
          label += " (discontinued after " + latestTrainLabel + ")";
        }

        boolean latestUnreleased = latestTrain == LATEST_TRAIN && !LATEST_RELEASED;
        if (latestUnreleased && size == 1)
        {
          label += " (unreleased before " + latestTrainLabel + ")";
        }

        System.out.println(label);

        Product product = SetupFactory.eINSTANCE.createProduct();
        product.setName(id);
        product.setLabel(label);
        productCatalog.getProducts().add(product);

        addProductVersion(product, latestVersion, VersionSegment.MAJOR, latestTrainAndVersion.getTrainURI(), latestTrain, "latest", "Latest ("
            + latestTrainLabel + ")", latestTrainsIUs);

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

          addProductVersion(product, releasedVersion, VersionSegment.MINOR, releasedTrainAndVersion.getTrainURI(), releasedTrain, "latest.released",
              "Latest Release (" + releasedTrainLabel + ")", releasedTrainAndVersion.getIUs());
        }

        for (int i = 0; i < size; i++)
        {
          TrainAndVersion entry = list.get(size - i - 1);
          String train = entry.getTrain();
          String trainLabel = getTrainLabel(train);
          Version version = entry.getVersion();

          addProductVersion(product, version, VersionSegment.MINOR, entry.getTrainURI(), train, train, trainLabel, entry.getIUs());
        }

        System.out.println();
      }

      System.out.println("#################################################################################################################");
      System.out.println();

      checkVersionRanges(productCatalog);
      postProcess(productCatalog);

      Resource resource = new BaseResourceFactoryImpl().createResource(uri == null ? org.eclipse.emf.common.util.URI.createURI("org.eclipse.products.setup")
          : uri);
      resource.getContents().add(productCatalog);
      resource.save(System.out, null);

      if (uri != null)
      {
        resource.save(null);
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
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
                  OSGiVersion minimum = (OSGiVersion)developmentVersionRange.getMinimum();
                  OSGiVersion maximum = (OSGiVersion)developmentVersionRange.getMaximum();
                  int major = minimum.getMajor();
                  if (major == maximum.getMajor())
                  {
                    developmentRequirement.setVersionRange(new VersionRange(minimum, true, Version.createOSGi(major, maximum.getMinor() + 1, 0), false));
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

  private void postProcess(ProductCatalog productCatalog)
  {
    for (Product product : productCatalog.getProducts())
    {
      if ("epp.package.standard".equals(product.getName()))
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

        return;
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
        if (capabilityName.endsWith(".feature.group"))
        {
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

  private void addProductVersion(Product product, Version version, VersionSegment versionSegment, URI trainURI, String train, String name, String label,
      Map<String, Set<IInstallableUnit>> ius)
  {
    System.out.println("  " + label);

    ProductVersion productVersion = SetupFactory.eINSTANCE.createProductVersion();
    productVersion.setName(name);
    productVersion.setLabel(label);
    product.getVersions().add(productVersion);

    VersionRange versionRange = createVersionRange(version, versionSegment);

    Requirement requirement = P2Factory.eINSTANCE.createRequirement();
    requirement.setName(product.getName());
    requirement.setVersionRange(versionRange);

    Repository packageRepository = P2Factory.eINSTANCE.createRepository();
    packageRepository.setURL(PACKAGES + "/" + train);

    Repository releaseRepository = P2Factory.eINSTANCE.createRepository();
    releaseRepository.setURL(trainURI.toString());

    P2Task p2Task = SetupP2Factory.eINSTANCE.createP2Task();
    p2Task.getRequirements().add(requirement);
    addRootIURequirements(p2Task.getRequirements(), versionSegment, ius);
    p2Task.getRepositories().add(packageRepository);
    p2Task.getRepositories().add(releaseRepository);
    productVersion.getSetupTasks().add(p2Task);
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
        VersionRange range = null;
        for (IInstallableUnit rootIU : rootIUs)
        {
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
          requirement.setName(iu);
          requirement.setVersionRange(range);
          requirements.add(requirement);
        }
      }
    }
  }

  private String getTrainLabel(String train)
  {
    return Character.toString((char)(train.charAt(0) + 'A' - 'a')) + train.substring(1);
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

    public TrainAndVersion(String train, Version version, URI trainURI, Map<String, Set<IInstallableUnit>> ius)
    {
      this.train = train;
      this.version = version;
      this.trainURI = trainURI;
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

    public Map<String, Set<IInstallableUnit>> getIUs()
    {
      return ius;
    }

  }
}
