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
package org.eclipse.oomph.internal.resources;

import org.eclipse.oomph.resources.backend.BackendContainer;
import org.eclipse.oomph.resources.backend.BackendException;

import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.content.IContentTypeMatcher;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class ExternalProject extends ExternalContainer implements IProject
{
  private final IProjectDescription description;

  public ExternalProject(BackendContainer backendContainer, IProjectDescription description)
  {
    super(null, backendContainer);
    this.description = description;

    if (description instanceof Description)
    {
      ((Description)description).setProject(this);
    }
  }

  @Override
  public IPath getFullPath()
  {
    return new Path(getName()).makeAbsolute();
  }

  @Override
  public IProject getProject()
  {
    return this;
  }

  @Override
  public int getType()
  {
    return PROJECT;
  }

  @Override
  public String getName()
  {
    return description.getName();
  }

  public void build(int kind, String builderName, Map<String, String> args, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void build(int kind, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void build(IBuildConfiguration config, int kind, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void close(IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void create(IProjectDescription description, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void create(IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void create(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void delete(boolean deleteContent, boolean force, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public IBuildConfiguration getActiveBuildConfig() throws CoreException
  {
    return getBuildConfig(IBuildConfiguration.DEFAULT_CONFIG_NAME);
  }

  public IBuildConfiguration getBuildConfig(String configName) throws CoreException
  {
    IBuildConfiguration[] buildConfigs = description.getBuildConfigReferences(configName);
    return buildConfigs.length == 0 ? null : buildConfigs[0];
  }

  public IBuildConfiguration[] getBuildConfigs() throws CoreException
  {
    return description.getBuildConfigReferences(IBuildConfiguration.DEFAULT_CONFIG_NAME);
  }

  public IBuildConfiguration[] getReferencedBuildConfigs(String configName, boolean includeMissing) throws CoreException
  {
    return getBuildConfigs();
  }

  public boolean hasBuildConfig(String configName) throws CoreException
  {
    return IBuildConfiguration.DEFAULT_CONFIG_NAME.equals(configName);
  }

  public IContentTypeMatcher getContentTypeMatcher() throws CoreException
  {
    // TODO
    throw new UnsupportedOperationException();
  }

  public IProjectDescription getDescription() throws CoreException
  {
    return description;
  }

  public IFile getFile(String name)
  {
    return null;
  }

  public IFolder getFolder(String name)
  {
    return null;
  }

  public IProjectNature getNature(String natureId) throws CoreException
  {
    if (description.hasNature(natureId))
    {
      return new IProjectNature()
      {
        public IProject getProject()
        {
          return ExternalProject.this;
        }

        public void setProject(IProject project)
        {
          throw new UnsupportedOperationException();
        }

        public void configure() throws CoreException
        {
          throw new UnsupportedOperationException();
        }

        public void deconfigure() throws CoreException
        {
          throw new UnsupportedOperationException();
        }
      };
    }

    return null;
  }

  @Deprecated
  public IPath getPluginWorkingLocation(org.eclipse.core.runtime.IPluginDescriptor plugin)
  {
    throw new UnsupportedOperationException();
  }

  public IPath getWorkingLocation(String id)
  {
    // TODO
    throw new UnsupportedOperationException();
  }

  public IProject[] getReferencedProjects() throws CoreException
  {
    return new IProject[0];
  }

  public IProject[] getReferencingProjects()
  {
    return new IProject[0];
  }

  public void clearCachedDynamicReferences()
  {
  }

  public boolean hasNature(String natureId) throws CoreException
  {
    return description.hasNature(natureId);
  }

  public boolean isNatureEnabled(String natureId) throws CoreException
  {
    return hasNature(natureId);
  }

  public boolean isOpen()
  {
    return true;
  }

  public void loadSnapshot(int options, URI snapshotLocation, IProgressMonitor monitor) throws CoreException
  {
    // TODO
    throw new UnsupportedOperationException();
  }

  public void saveSnapshot(int options, URI snapshotLocation, IProgressMonitor monitor) throws CoreException
  {
    // TODO
    throw new UnsupportedOperationException();
  }

  public void move(IProjectDescription description, boolean force, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void open(int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void open(IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void setDescription(IProjectDescription description, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  public void setDescription(IProjectDescription description, int updateFlags, IProgressMonitor monitor) throws CoreException
  {
    throw new ReadOnlyException();
  }

  /**
   * @author Eike Stepper
   */
  public static final class Description implements IProjectDescription
  {
    private final BackendContainer backendContainer;

    private String name;

    private String comment;

    private IBuildConfiguration[] buildConfigurations;

    private final List<ICommand> commands = new ArrayList<ICommand>();

    private final List<String> natureIDs = new ArrayList<String>();

    public Description(BackendContainer backendContainer)
    {
      this.backendContainer = backendContainer;
    }

    public String getName()
    {
      return name;
    }

    public String getComment()
    {
      return comment;
    }

    public ICommand[] getBuildSpec()
    {
      return commands.toArray(new ICommand[commands.size()]);
    }

    public String[] getNatureIds()
    {
      return natureIDs.toArray(new String[natureIDs.size()]);
    }

    public boolean hasNature(String natureId)
    {
      return natureIDs.contains(natureId);
    }

    public IBuildConfiguration[] getBuildConfigReferences(String configName)
    {
      if (IBuildConfiguration.DEFAULT_CONFIG_NAME.equals(configName))
      {
        return buildConfigurations;
      }

      return new IBuildConfiguration[0];
    }

    public IProject[] getDynamicReferences()
    {
      return new IProject[0];
    }

    @Deprecated
    public IPath getLocation()
    {
      return backendContainer.getLocation();
    }

    public URI getLocationURI()
    {
      try
      {
        return new URI(backendContainer.getAbsoluteURI().toString());
      }
      catch (URISyntaxException ex)
      {
        throw new BackendException(ex);
      }
    }

    public IProject[] getReferencedProjects()
    {
      return new IProject[0];
    }

    public ICommand newCommand()
    {
      return new BuildCommand(null);
    }

    public void setActiveBuildConfig(String configName)
    {
    }

    public void setBuildConfigs(String[] configNames)
    {
    }

    public void setBuildConfigReferences(String configName, IBuildConfiguration[] references)
    {
    }

    public void setBuildSpec(ICommand[] buildSpec)
    {
    }

    public void setComment(String comment)
    {
    }

    @Deprecated
    public void setDynamicReferences(IProject[] projects)
    {
    }

    public void setLocation(IPath location)
    {
    }

    public void setLocationURI(URI location)
    {
    }

    public void setName(String projectName)
    {
    }

    public void setNatureIds(String[] natures)
    {
    }

    public void setReferencedProjects(IProject[] projects)
    {
    }

    public void internalSetName(String name)
    {
      this.name = name;
    }

    public void internalSetComment(String comment)
    {
      this.comment = comment;
    }

    public void internalAddCommand(String builderName)
    {
      commands.add(new BuildCommand(builderName));
    }

    public void internalAddNatureID(String natureID)
    {
      natureIDs.add(natureID);
    }

    void setProject(ExternalProject project)
    {
      buildConfigurations = new IBuildConfiguration[] { new BuildConfiguration(project) };
    }

    /**
     * @author Eike Stepper
     */
    private static final class BuildConfiguration extends PlatformObject implements IBuildConfiguration
    {
      private final ExternalProject project;

      public BuildConfiguration(ExternalProject project)
      {
        this.project = project;
      }

      public IProject getProject()
      {
        return project;
      }

      public String getName()
      {
        return DEFAULT_CONFIG_NAME;
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class BuildCommand implements ICommand
    {
      private final String builderName;

      public BuildCommand(String builderName)
      {
        this.builderName = builderName;
      }

      public String getBuilderName()
      {
        return builderName;
      }

      public Map<String, String> getArguments()
      {
        return Collections.emptyMap();
      }

      public boolean isBuilding(int kind)
      {
        return false;
      }

      public boolean isConfigurable()
      {
        return false;
      }

      public void setArguments(Map<String, String> args)
      {
      }

      public void setBuilderName(String builderName)
      {
      }

      public void setBuilding(int kind, boolean value)
      {
      }
    }
  }
}
