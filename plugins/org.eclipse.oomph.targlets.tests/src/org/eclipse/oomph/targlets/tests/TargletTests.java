/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.tests.AbstractP2Test;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.internal.core.TargletContainer;
import org.eclipse.oomph.util.pde.TargetPlatformRunnable;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.frameworkadmin.BundleInfo;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetHandle;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.core.target.ITargetPlatformService;
import org.eclipse.pde.core.target.TargetBundle;
import org.eclipse.pde.core.target.TargetFeature;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Eike Stepper
 */
@FixMethodOrder(MethodSorters.JVM)
public class TargletTests extends AbstractP2Test
{
  private static final String TARGET_NAME = "Test Target";

  private static final String CONTAINER_ID = "Test Targlets";

  private static final String TARGLET_NAME = "Test Targlet";

  private ITargetDefinition target;

  private ITargletContainer targletContainer;

  private File getTestFolder()
  {
    return getTestFolder(TargletsTestsPlugin.INSTANCE, testName.getMethodName());
  }

  private Targlet setTarglet(EList<Repository> repositories, EList<Requirement> requirements) throws CoreException
  {
    File testFolder = getTestFolder();
    SourceLocator sourceLocator = ResourcesFactory.eINSTANCE.createSourceLocator(testFolder.getAbsolutePath());

    RepositoryList repositoryList = P2Factory.eINSTANCE.createRepositoryList();
    if (repositories != null)
    {
      repositoryList.getRepositories().addAll(repositories);
    }

    Targlet targlet = TargletFactory.eINSTANCE.createTarglet(TARGLET_NAME);
    targlet.getSourceLocators().add(sourceLocator);
    targlet.getRepositoryLists().add(repositoryList);
    if (requirements != null)
    {
      targlet.getRequirements().addAll(requirements);
    }

    setTarglet(targlet);
    return targlet;
  }

  private void setTarglet(Targlet targlet) throws CoreException
  {
    Set<Targlet> targlets = Collections.singleton(targlet);
    targletContainer.setTarglets(targlets);
    targletContainer.forceUpdate(true, false, LOGGER);
  }

  private static void assertImportedProjects(String... names)
  {
    Set<String> expected = new HashSet<String>(Arrays.asList(names));
    Set<String> actual = new HashSet<String>();

    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    for (IProject project : projects)
    {
      actual.add(project.getName());
    }

    assertThat(actual, is(expected));
  }

  private void assertTargetPlatform(String... ius)
  {
    Set<String> expected = new HashSet<String>(Arrays.asList(ius));
    Set<String> actual = new HashSet<String>();

    TargetBundle[] bundles = target.getAllBundles();
    if (bundles != null)
    {
      for (TargetBundle bundle : bundles)
      {
        BundleInfo bundleInfo = bundle.getBundleInfo();
        actual.add(bundleInfo.getSymbolicName() + "_" + bundleInfo.getVersion());
      }
    }

    TargetFeature[] features = target.getAllFeatures();
    if (features != null)
    {
      for (TargetFeature feature : features)
      {
        actual.add(feature.getId() + ".feature.group_" + feature.getVersion());
      }
    }

    assertThat(actual, is(expected));
  }

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    ResourcesUtil.clearWorkspace();

    TargetPlatformUtil.runWithTargetPlatformService(new TargetPlatformRunnable<Object>()
    {
      public Object run(ITargetPlatformService service) throws CoreException
      {
        for (ITargetHandle targetHandle : service.getTargets(LOGGER))
        {
          ITargetDefinition target = targetHandle.getTargetDefinition();
          if (TARGET_NAME.equals(target.getName()))
          {
            service.deleteTarget(targetHandle);
            break;
          }
        }

        target = service.newTarget();
        target.setName(TARGET_NAME);

        targletContainer = new TargletContainer(CONTAINER_ID);

        ITargetLocation[] newLocations;
        ITargetLocation[] oldLocations = target.getTargetLocations();
        if (oldLocations != null && oldLocations.length != 0)
        {
          newLocations = new ITargetLocation[oldLocations.length + 1];
          System.arraycopy(oldLocations, 0, newLocations, 0, oldLocations.length);
          newLocations[oldLocations.length] = targletContainer;
        }
        else
        {
          newLocations = new ITargetLocation[] { targletContainer };
        }

        target.setTargetLocations(newLocations);
        return null;
      }
    });
  }

  @Override
  public void tearDown() throws Exception
  {
    targletContainer = null;
    target = null;
    super.tearDown();
  }

  @Test
  public void testOnlySources() throws Exception
  {
    assertTargetPlatform();
    assertImportedProjects();

    EList<Requirement> requirements = new BasicEList<Requirement>();
    requirements.add(P2Factory.eINSTANCE.createRequirement("com.foo.project1.feature.group"));

    Targlet targlet = setTarglet(null, requirements);
    assertTargetPlatform();
    assertImportedProjects("com.foo.project1-feature", "com.foo.project1", "com.foo.license-feature");

    targlet.getRequirements().add(P2Factory.eINSTANCE.createRequirement("com.foo.releng"));
    setTarglet(targlet);
    assertTargetPlatform();
    assertImportedProjects("com.foo.project1-feature", "com.foo.project1", "com.foo.license-feature", "com.foo.releng");
  }
}
