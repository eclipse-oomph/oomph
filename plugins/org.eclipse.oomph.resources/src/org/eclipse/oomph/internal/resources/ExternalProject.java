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
package org.eclipse.oomph.internal.resources;

import org.eclipse.oomph.resources.ResourcesFactory.ProjectDescriptionFactory;
import org.eclipse.oomph.util.XMLUtil;

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

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class ExternalProject extends ExternalContainer implements IProject
{
  private final File folder;

  private final IProjectDescription description;

  public ExternalProject(File folder, IProjectDescription description)
  {
    super(null, description.getName());
    this.folder = folder;
    this.description = description;

    if (description instanceof Description)
    {
      ((Description)description).setProject(this);
    }
  }

  @Override
  protected File getFile()
  {
    return folder;
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

  public int getType()
  {
    return PROJECT;
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

  public static IProject load(File folder, ProjectDescriptionFactory... factories)
  {
    try
    {
      for (ProjectDescriptionFactory factory : factories)
      {
        IProjectDescription description = factory.createDescription(folder);
        if (description != null)
        {
          return new ExternalProject(folder, description);
        }
      }
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }

    return null;
  }

  /**
   * @author Eike Stepper
   */
  public static final class Description implements IProjectDescription
  {
    private final File folder;

    private String name;

    private String comment;

    private IBuildConfiguration[] buildConfigurations;

    private final List<ICommand> commands = new ArrayList<ICommand>();

    private final List<String> natureIDs = new ArrayList<String>();

    public Description(File folder)
    {
      this.folder = folder;
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
      return new Path(folder.getAbsolutePath());
    }

    public URI getLocationURI()
    {
      return folder.toURI();
    }

    public IProject[] getReferencedProjects()
    {
      return new IProject[0];
    }

    public ICommand newCommand()
    {
      throw new ReadOnlyException();
    }

    public void setActiveBuildConfig(String configName)
    {
      throw new ReadOnlyException();
    }

    public void setBuildConfigs(String[] configNames)
    {
      throw new ReadOnlyException();
    }

    public void setBuildConfigReferences(String configName, IBuildConfiguration[] references)
    {
      throw new ReadOnlyException();
    }

    public void setBuildSpec(ICommand[] buildSpec)
    {
      throw new ReadOnlyException();
    }

    public void setComment(String comment)
    {
      throw new ReadOnlyException();
    }

    public void setDynamicReferences(IProject[] projects)
    {
      throw new ReadOnlyException();
    }

    public void setLocation(IPath location)
    {
      throw new ReadOnlyException();
    }

    public void setLocationURI(URI location)
    {
      throw new ReadOnlyException();
    }

    public void setName(String projectName)
    {
      throw new ReadOnlyException();
    }

    public void setNatureIds(String[] natures)
    {
      throw new ReadOnlyException();
    }

    public void setReferencedProjects(IProject[] projects)
    {
      throw new ReadOnlyException();
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
        throw new ReadOnlyException();
      }

      public void setBuilderName(String builderName)
      {
        throw new ReadOnlyException();
      }

      public void setBuilding(int kind, boolean value)
      {
        throw new ReadOnlyException();
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static abstract class AbstractXMLDescriptionFactory implements ProjectDescriptionFactory
  {
    public final IProjectDescription createDescription(File folder) throws Exception
    {
      File projectFile = new File(folder, getProjectFileName());
      if (!projectFile.isFile())
      {
        return null;
      }

      DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
      Element rootElement = XMLUtil.loadRootElement(documentBuilder, projectFile);

      Description description = new Description(folder);
      fillDescription(description, rootElement);
      return description;
    }

    protected abstract String getProjectFileName();

    protected abstract void fillDescription(final Description description, Element rootElement) throws Exception;

    /**
     * @author Eike Stepper
     */
    public static final class Eclipse extends AbstractXMLDescriptionFactory
    {
      @Override
      protected String getProjectFileName()
      {
        return ".project";
      }

      @Override
      protected void fillDescription(final Description description, Element rootElement) throws Exception
      {
        XMLUtil.handleChildElements(rootElement, new XMLUtil.ElementHandler()
        {
          public void handleElement(Element element) throws Exception
          {
            if ("name".equals(element.getTagName()))
            {
              String name = element.getTextContent().trim();
              description.internalSetName(name);
            }
            else if ("comment".equals(element.getTagName()))
            {
              String comment = element.getTextContent().trim();
              description.internalSetComment(comment);
            }
            else if ("buildSpec".equals(element.getTagName()))
            {
              XMLUtil.handleChildElements(element, new XMLUtil.ElementHandler()
              {
                public void handleElement(Element buildCommandElement) throws Exception
                {
                  XMLUtil.handleChildElements(buildCommandElement, new XMLUtil.ElementHandler()
                  {
                    public void handleElement(Element nameElement) throws Exception
                    {
                      String builderName = nameElement.getTextContent().trim();
                      description.internalAddCommand(builderName);
                    }
                  });
                }
              });
            }
            else if ("natures".equals(element.getTagName()))
            {
              XMLUtil.handleChildElements(element, new XMLUtil.ElementHandler()
              {
                public void handleElement(Element natureElement) throws Exception
                {
                  String natureID = natureElement.getTextContent().trim();
                  description.internalAddNatureID(natureID);
                }
              });
            }
          }
        });
      }
    }

    /**
     * @author Eike Stepper
     */
    public static final class Maven extends AbstractXMLDescriptionFactory
    {
      @Override
      protected String getProjectFileName()
      {
        return "pom.xml";
      }

      @Override
      protected void fillDescription(final Description description, Element rootElement) throws Exception
      {
        XMLUtil.handleChildElements(rootElement, new XMLUtil.ElementHandler()
        {
          public void handleElement(Element element) throws Exception
          {
            if ("artifactId".equals(element.getTagName()))
            {
              String name = element.getTextContent().trim();
              description.internalSetName(name);
            }
            else if ("name".equals(element.getTagName()))
            {
              String comment = element.getTextContent().trim();
              description.internalSetComment(comment);
            }
          }
        });
      }
    }
  }
}
