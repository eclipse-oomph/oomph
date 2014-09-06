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
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.resources.EclipseProjectFactory;
import org.eclipse.oomph.resources.ProjectHandler;
import org.eclipse.oomph.resources.SourceLocator;
import org.eclipse.oomph.resources.backend.BackendContainer;
import org.eclipse.oomph.resources.impl.SourceLocatorImpl;
import org.eclipse.oomph.targlets.IUGenerator;
import org.eclipse.oomph.targlets.util.VersionGenerator;

import org.eclipse.emf.common.util.EList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.equinox.internal.p2.metadata.InstallableUnit;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class WorkspaceIUAnalyzer
{
  public static final String IU_PROPERTY_WORKSPACE = "org.eclipse.oomph.targlet.workspace";

  private final MultiStatus status = new MultiStatus(TargletsCorePlugin.INSTANCE.getSymbolicName(), 0, "Workspace IU Analysis", null);

  private final Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = new HashMap<IInstallableUnit, WorkspaceIUInfo>();

  private final Map<String, Version> iuVersions = new HashMap<String, Version>();

  private final String qualifierReplacement;

  private Map<String, Requirement> omniRootRequirements;

  public WorkspaceIUAnalyzer(EList<Requirement> rootRequirements)
  {
    this(rootRequirements, VersionGenerator.generateQualifierReplacement());
  }

  public WorkspaceIUAnalyzer(EList<Requirement> rootRequirements, String qualifierReplacement)
  {
    this.qualifierReplacement = qualifierReplacement;

    if (qualifierReplacement != null && rootRequirements != null)
    {
      omniRootRequirements = new HashMap<String, Requirement>();
      for (Requirement requirement : rootRequirements)
      {
        if (VersionRange.emptyRange.equals(requirement.getVersionRange()))
        {
          omniRootRequirements.put(requirement.getName(), requirement);
        }
      }
    }
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

  public void analyze(SourceLocator sourceLocator, final EList<IUGenerator> iuGenerators, IProgressMonitor monitor)
  {
    sourceLocator.handleProjects(EclipseProjectFactory.LIST, new ProjectHandler()
    {
      public void handleProject(IProject project, BackendContainer backendContainer)
      {
        try
        {
          EList<IInstallableUnit> installableUnits = null;
          IUGenerator generator = null;

          for (IUGenerator iuGenerator : iuGenerators)
          {
            try
            {
              EList<IInstallableUnit> ius = iuGenerator.generateIUs(project, qualifierReplacement, iuVersions);
              if (ius != null && !ius.isEmpty())
              {
                installableUnits = ius;
                generator = iuGenerator;
                break;
              }
            }
            catch (Exception ex)
            {
              log(project, ex);
            }
          }

          if (installableUnits != null)
          {
            for (IInstallableUnit installableUnit : installableUnits)
            {
              if (installableUnit instanceof InstallableUnit)
              {
                ((InstallableUnit)installableUnit).setProperty(IU_PROPERTY_WORKSPACE, Boolean.TRUE.toString());
              }
            }

            for (IUGenerator iuModifier : iuGenerators)
            {
              if (iuModifier != generator)
              {
                try
                {
                  IInstallableUnit mainIU = installableUnits.get(0);
                  iuModifier.modifyIU(mainIU, project, qualifierReplacement, iuVersions);
                }
                catch (Exception ex)
                {
                  log(project, ex);
                }
              }
            }

            boolean main = true;
            for (IInstallableUnit installableUnit : installableUnits)
            {
              adjustOmniRootRequirements(installableUnit);

              WorkspaceIUInfo info = null;
              if (main)
              {
                String projectName = project.getName();
                info = new WorkspaceIUInfo(backendContainer, projectName);
                main = false;
              }

              workspaceIUInfos.put(installableUnit, info);
            }
          }
        }
        catch (Exception ex)
        {
          log(project, ex);
        }
      }
    }, status, monitor);
  }

  private void adjustOmniRootRequirements(IInstallableUnit iu)
  {
    if (omniRootRequirements != null)
    {
      Requirement requirement = omniRootRequirements.remove(iu.getId());
      if (requirement != null)
      {
        requirement.setVersionRange(P2Factory.eINSTANCE.createVersionRange(iu.getVersion(), VersionSegment.MICRO));

        if (omniRootRequirements.isEmpty())
        {
          omniRootRequirements = null;
        }
      }
    }
  }

  protected void log(IProject project, Exception ex)
  {
    SourceLocatorImpl.addStatus(status, TargletsCorePlugin.INSTANCE, "project " + project.getName(), ex);
  }
}
