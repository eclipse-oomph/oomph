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

import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.internal.core.AgentImpl;
import org.eclipse.oomph.setup.EclipseIniTask;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.p2.SetupP2Factory;
import org.eclipse.oomph.setup.util.SetupResourceFactoryImpl;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
    generate();
    return null;
  }

  public void stop()
  {
  }

  private static String[] getTrains()
  {
    return new String[] { "helios", "indigo", "juno", "kepler", "luna" };
  }

  private static boolean isLatestReleased()
  {
    return false;
  }

  private static boolean testNewUnreleasedProduct()
  {
    return false;
  }

  public static void generate()
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
      Repository relengRepository = P2Factory.eINSTANCE.createRepository("${" + SetupProperties.PROP_UPDATE_URL + "}");

      P2Task p2Task = SetupP2Factory.eINSTANCE.createP2Task();
      p2Task.getRequirements().add(oomphRequirement);
      p2Task.getRepositories().add(relengRepository);
      productCatalog.getSetupTasks().add(p2Task);

      EclipseIniTask eclipseIniTask = SetupFactory.eINSTANCE.createEclipseIniTask();
      eclipseIniTask.setOption("-D" + SetupProperties.PROP_SETUP);
      eclipseIniTask.setValue("=true");
      eclipseIniTask.setVm(true);
      productCatalog.getSetupTasks().add(eclipseIniTask);

      File agentLocation = File.createTempFile("test-", "-agent");
      agentLocation.delete();
      agentLocation.mkdirs();

      Agent agent = new AgentImpl(null, agentLocation);
      IMetadataRepositoryManager manager = agent.getMetadataRepositoryManager();

      final Map<String, List<TrainAndVersion>> trainsAndVersions = new HashMap<String, List<TrainAndVersion>>();
      final Map<String, String> labels = new HashMap<String, String>();

      for (final String train : TRAINS)
      {
        URI uri = new URI(PACKAGES + "/" + train);
        System.out.println(uri);

        Map<String, IInstallableUnit> ius = new HashMap<String, IInstallableUnit>();

        IMetadataRepository metadataRepository = manager.loadRepository(uri, null);
        for (IInstallableUnit iu : metadataRepository.query(QueryUtil.createIUAnyQuery(), null))
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

        for (String id : ius.keySet())
        {
          String label = labels.get(id);
          final Version version = ius.get(id).getVersion();
          System.out.println("  " + label + "  --  " + id + " " + version);

          List<TrainAndVersion> list = trainsAndVersions.get(id);
          if (list == null)
          {
            list = new ArrayList<TrainAndVersion>();
            trainsAndVersions.put(id, list);
          }

          list.add(new TrainAndVersion(train, version));
        }

        System.out.println();
      }

      if (testNewUnreleasedProduct())
      {
        List<TrainAndVersion> list = new ArrayList<TrainAndVersion>();
        list.add(new TrainAndVersion("luna", null));

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

        String latestTrain = list.get(size - 1).getTrain();
        String latestTrainLabel = getTrainLabel(latestTrain);

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

        if (!latestUnreleased || size != 1)
        {
          int offset = 1;
          if (latestUnreleased && size > 1)
          {
            ++offset;
          }

          String releasedTrain = list.get(size - offset).getTrain();
          addProductVersion(product, releasedTrain, "latest.released", "Latest Release (" + getTrainLabel(releasedTrain) + ")");
        }

        addProductVersion(product, latestTrain, "latest", "Latest (" + latestTrainLabel + ")");

        for (int i = 0; i < size; i++)
        {
          TrainAndVersion entry = list.get(size - i - 1);
          String train = entry.getTrain();
          String trainLabel = getTrainLabel(train);
          addProductVersion(product, train, train, trainLabel);
        }

        System.out.println();
      }

      System.out.println("#################################################################################################################");
      System.out.println();

      Resource resource = new SetupResourceFactoryImpl().createResource(org.eclipse.emf.common.util.URI.createURI("catalog"));
      resource.getContents().add(productCatalog);
      resource.save(System.out, null);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private static void addProductVersion(Product product, String train, String name, String label)
  {
    System.out.println("  " + label);

    ProductVersion productVersion = SetupFactory.eINSTANCE.createProductVersion();
    productVersion.setName(name);
    productVersion.setLabel(label);
    product.getVersions().add(productVersion);

    Requirement requirement = P2Factory.eINSTANCE.createRequirement();
    requirement.setID(product.getName());

    Repository packageRepository = P2Factory.eINSTANCE.createRepository();
    packageRepository.setURL(PACKAGES + "/" + train);

    Repository releaseRepository = P2Factory.eINSTANCE.createRepository();
    releaseRepository.setURL(RELEASES + "/" + train);

    P2Task p2Task = SetupP2Factory.eINSTANCE.createP2Task();
    p2Task.getRequirements().add(requirement);
    p2Task.getRepositories().add(packageRepository);
    p2Task.getRepositories().add(releaseRepository);
    productVersion.getSetupTasks().add(p2Task);
  }

  private static String getTrainLabel(String train)
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

    public TrainAndVersion(String train, Version version)
    {
      this.train = train;
      this.version = version;
    }

    public String getTrain()
    {
      return train;
    }

    @SuppressWarnings("unused")
    public Version getVersion()
    {
      return version;
    }
  }
}
