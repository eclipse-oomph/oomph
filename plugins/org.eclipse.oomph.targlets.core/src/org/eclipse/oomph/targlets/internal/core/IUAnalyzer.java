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
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.ComponentExtension;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.internal.core.IUGenerator.BundleIUGenerator;
import org.eclipse.oomph.targlets.internal.core.IUGenerator.FeatureIUGenerator;
import org.eclipse.oomph.util.XMLUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.metadata.MetadataFactory;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;

import org.w3c.dom.Element;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class IUAnalyzer extends BasicProjectAnalyzer<IInstallableUnit>
{
  private final Set<String> ids = new HashSet<String>();

  public IUAnalyzer()
  {
  }

  public Set<String> getIDs()
  {
    return ids;
  }

  public Map<IInstallableUnit, File> analyze(File folder, EList<Predicate> predicates, boolean locateNestedProjects, IProgressMonitor monitor)
  {
    ProjectVisitor<IInstallableUnit> visitor = new IUVisitor();
    return analyze(folder, predicates, locateNestedProjects, visitor, monitor);
  }

  @Override
  protected IInstallableUnit filter(IInstallableUnit iu)
  {
    ids.add(iu.getId());
    return iu;
  }

  /**
   * @author Eike Stepper
   */
  public static class IUVisitor extends BasicProjectVisitor<IInstallableUnit>
  {
    @Override
    public IInstallableUnit visitPlugin(File manifestFile, IProgressMonitor monitor) throws Exception
    {
      File pluginFolder = manifestFile.getParentFile().getParentFile();
      return BundleIUGenerator.INSTANCE.generateIU(pluginFolder);
    }

    @Override
    public IInstallableUnit visitFeature(File featureFile, IProgressMonitor monitor) throws Exception
    {
      File featureFolder = featureFile.getParentFile();
      return FeatureIUGenerator.INSTANCE.generateIU(featureFolder);
    }

    @Override
    @SuppressWarnings("restriction")
    protected IInstallableUnit visitComponentDefinition(ComponentDefinition componentDefinition, IProgressMonitor monitor) throws Exception
    {
      String id = componentDefinition.getID();
      Version version = componentDefinition.getVersion();

      InstallableUnitDescription description = new InstallableUnitDescription();
      description.setId(id);
      description.setVersion(version);
      description.setProperty(InstallableUnitDescription.PROP_TYPE_GROUP, "true");
      description.setProperty(IUGenerator.IU_PROPERTY_SOURCE, "true");
      description.addProvidedCapabilities(Collections.singleton(MetadataFactory.createProvidedCapability(IInstallableUnit.NAMESPACE_IU_ID, id, version)));
      description.setTouchpointType(org.eclipse.equinox.spi.p2.publisher.PublisherHelper.TOUCHPOINT_OSGI);
      description.setArtifacts(new IArtifactKey[0]);

      IInstallableUnit iu = MetadataFactory.createInstallableUnit(description);
      visitComponentExtension(componentDefinition, iu, monitor);
      return iu;
    }

    @Override
    protected void visitComponentExtension(ComponentExtension componentExtension, IInstallableUnit host, IProgressMonitor monitor) throws Exception
    {
      // TODO It would be better to work with a new InstallableUnitDescription
      if (host instanceof org.eclipse.equinox.internal.p2.metadata.InstallableUnit)
      {
        org.eclipse.equinox.internal.p2.metadata.InstallableUnit iu = (org.eclipse.equinox.internal.p2.metadata.InstallableUnit)host;
        List<IRequirement> requirements = new ArrayList<IRequirement>(iu.getRequirements());

        for (Requirement requirement : componentExtension.getRequirements())
        {
          String id = requirement.getID();
          VersionRange versionRange = requirement.getVersionRange();

          String namespace;
          if (requirement.isFeature())
          {
            namespace = IInstallableUnit.NAMESPACE_IU_ID;
          }
          else
          {
            namespace = "osgi.bundle";
          }

          requirements.add(MetadataFactory.createRequirement(namespace, id, versionRange, null, false, true, true));
        }

        iu.setRequiredCapabilities(requirements.toArray(new IRequirement[requirements.size()]));
      }
    }

    @Override
    public IInstallableUnit visitCSpec(File cspecFile, IProgressMonitor monitor) throws Exception
    {
      File cdefFile = new File(cspecFile.getParentFile(), "component.def");
      if (cdefFile.exists())
      {
        return null;
      }

      Element rootElement = XMLUtil.loadRootElement(getDocumentBuilder(), cspecFile);
      String id = BuckminsterDependencyHandler.getP2ID(rootElement.getAttribute("name"), rootElement.getAttribute("componentType"));
      if (id == null)
      {
        return null;
      }

      ComponentDefinition componentDefinition = TargletFactory.eINSTANCE.createComponentDefinition();
      componentDefinition.setID(id);
      componentDefinition.setVersion(Version.create(rootElement.getAttribute("version")));

      handleBuckminsterDependencies(rootElement, componentDefinition, monitor);

      Resource resource = getResourceSet().createResource(URI.createFileURI(cdefFile.getAbsolutePath()));
      resource.getContents().add(componentDefinition);
      resource.save(null);

      return visitComponentDefinition(componentDefinition, monitor);
    }

    @Override
    public void visitCSpex(File cspexFile, IInstallableUnit host, IProgressMonitor monitor) throws Exception
    {
      File cextFile = new File(cspexFile.getParentFile(), "component.ext");
      if (cextFile.exists())
      {
        return;
      }

      ComponentExtension componentExtension = TargletFactory.eINSTANCE.createComponentExtension();

      Element rootElement = XMLUtil.loadRootElement(getDocumentBuilder(), cspexFile);
      handleBuckminsterDependencies(rootElement, componentExtension, monitor);

      if (!componentExtension.getRequirements().isEmpty())
      {
        Resource resource = getResourceSet().createResource(URI.createFileURI(cextFile.getAbsolutePath()));
        resource.getContents().add(componentExtension);
        resource.save(null);
      }

      visitComponentExtension(componentExtension, host, monitor);
    }

    private void handleBuckminsterDependencies(Element rootElement, final ComponentExtension componentExtension, IProgressMonitor monitor) throws Exception
    {
      new BuckminsterDependencyHandler()
      {
        @Override
        protected void handleDependency(String id, String versionDesignator) throws Exception
        {
          Requirement requirement = P2Factory.eINSTANCE.createRequirement(id);
          if (versionDesignator != null)
          {
            requirement.setVersionRange(new VersionRange(versionDesignator));
          }

          componentExtension.getRequirements().add(requirement);
        }
      }.handleDependencies(rootElement, monitor);
    }
  }
}
