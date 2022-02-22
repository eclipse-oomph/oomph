/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.resources.EclipseProjectFactory;
import org.eclipse.oomph.resources.ProjectHandler;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.resources.backend.BackendContainer;
import org.eclipse.oomph.resources.impl.SourceLocatorImpl;
import org.eclipse.oomph.targlets.IUGenerator;
import org.eclipse.oomph.targlets.core.WorkspaceIUInfo;
import org.eclipse.oomph.targlets.util.VersionGenerator;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class WorkspaceIUAnalyzer
{
  public static final String IU_PROPERTY_WORKSPACE = "org.eclipse.oomph.targlet.workspace"; //$NON-NLS-1$

  public static final String IU_PROPERTY_WORKSPACE_MAIN = "org.eclipse.oomph.targlet.workspace.main"; //$NON-NLS-1$

  private final MultiStatus status = new MultiStatus(TargletsCorePlugin.INSTANCE.getSymbolicName(), 0, Messages.WorkspaceIUAnalyzer_Analysis_message, null);

  private final Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = Collections.synchronizedMap(new HashMap<IInstallableUnit, WorkspaceIUInfo>());

  private final Map<String, Version> iuVersions = Collections.synchronizedMap(new HashMap<String, Version>());

  private final String qualifierReplacement;

  public WorkspaceIUAnalyzer()
  {
    this(VersionGenerator.generateQualifierReplacement());
  }

  public WorkspaceIUAnalyzer(String qualifierReplacement)
  {
    this.qualifierReplacement = qualifierReplacement;
  }

  public Map<IInstallableUnit, WorkspaceIUInfo> getWorkspaceIUInfos()
  {
    return workspaceIUInfos;
  }

  public Map<String, Version> getIUVersions()
  {
    return iuVersions;
  }

  public String getQualifierReplacement()
  {
    return qualifierReplacement;
  }

  public IStatus getStatus()
  {
    return status;
  }

  public EList<IInstallableUnit> analyze(SourceLocator sourceLocator, final EList<IUGenerator> generators, IProgressMonitor monitor)
  {
    final EList<IInstallableUnit> allIUs = ECollections.asEList(Collections.synchronizedList(new ArrayList<IInstallableUnit>()));

    sourceLocator.handleProjects(EclipseProjectFactory.LIST, new ProjectHandler()
    {
      @Override
      public void handleProject(IProject project, BackendContainer backendContainer)
      {
        try
        {
          EList<IInstallableUnit> ius = new BasicEList<>();

          for (IUGenerator generator : generators)
          {
            try
            {
              generator.generateIUs(project, qualifierReplacement, iuVersions, ius);
            }
            catch (Exception ex)
            {
              log(project, ex);
            }
          }

          WorkspaceIUInfo info = new WorkspaceIUInfo(backendContainer, project.getName());
          boolean isMain = true;
          for (IInstallableUnit iu : ius)
          {
            if (iu instanceof InstallableUnit)
            {
              InstallableUnit installableUnit = (InstallableUnit)iu;
              installableUnit.setProperty(IU_PROPERTY_WORKSPACE, Boolean.TRUE.toString());
              if (isMain)
              {
                isMain = false;
                installableUnit.setProperty(IU_PROPERTY_WORKSPACE_MAIN, Boolean.TRUE.toString());
              }
            }

            workspaceIUInfos.put(iu, info);
          }

          allIUs.addAll(ius);
        }
        catch (Exception ex)
        {
          log(project, ex);
        }
      }
    }, status, monitor);

    return allIUs;
  }

  public void adjustOmniRootRequirements(EList<Requirement> rootRequirements)
  {
    Map<String, Requirement> omniRootRequirements;

    if (qualifierReplacement != null)
    {
      omniRootRequirements = new HashMap<>();
      for (Requirement requirement : rootRequirements)
      {
        if (VersionRange.emptyRange.equals(requirement.getVersionRange()))
        {
          omniRootRequirements.put(requirement.getName(), requirement);
        }
      }

      for (IInstallableUnit iu : workspaceIUInfos.keySet())
      {
        if (!iu.isSingleton() && !"true".equals(iu.getProperty(InstallableUnitDescription.PROP_TYPE_GROUP))) //$NON-NLS-1$
        {
          Requirement requirement = omniRootRequirements.remove(iu.getId());
          if (requirement != null)
          {
            requirement.setVersionRange(P2Factory.eINSTANCE.createVersionRange(iu.getVersion(), VersionSegment.MICRO));

            if (omniRootRequirements.isEmpty())
            {
              return;
            }
          }
        }
      }
    }
  }

  protected void log(IProject project, Exception ex)
  {
    SourceLocatorImpl.addStatus(status, TargletsCorePlugin.INSTANCE, "project " + project.getName(), ex); //$NON-NLS-1$
  }
}
