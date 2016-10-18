/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.version;

import org.eclipse.oomph.internal.version.Activator;
import org.eclipse.oomph.internal.version.VersionBuilderArguments;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.core.resources.IBuildConfiguration;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.build.IBuild;
import org.eclipse.pde.core.build.IBuildModel;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;

import org.osgi.framework.Version;

import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides static utility methods for {@link Version versions}, I/O tasks and access to the {@link IElement component} model.
 *
 * @author Eike Stepper
 */
public final class VersionUtil
{
  public static final String BUILDER_ID = "org.eclipse.oomph.version.VersionBuilder";

  public static final boolean DEBUG = Boolean.valueOf(System.getProperty("org.eclipse.oomph.version.debug", "false"));

  private static final IWorkspace WORKSPACE = ResourcesPlugin.getWorkspace();

  private VersionUtil()
  {
  }

  public static boolean equals(Object o1, Object o2)
  {
    if (o1 == null)
    {
      return o2 == null;
    }

    return o1.equals(o2);
  }

  public static Version normalize(Version version)
  {
    return new Version(version.getMajor(), version.getMinor(), version.getMicro());
  }

  public static IFile getFile(IPath path, String extension)
  {
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IFile file = root.getFile(path);
    IPath newPath = file.getFullPath().removeFileExtension().addFileExtension(extension);
    return root.getFile(newPath);
  }

  public static String getContents(IFile file) throws CoreException, IOException
  {
    InputStream contents = null;

    try
    {
      contents = file.getContents();
      BufferedReader reader = new BufferedReader(new InputStreamReader(contents, file.getCharset()));
      CharArrayWriter caw = new CharArrayWriter();

      int c;
      while ((c = reader.read()) != -1)
      {
        caw.write(c);
      }

      return caw.toString();
    }
    finally
    {
      IOUtil.closeSilent(contents);
    }
  }

  @SuppressWarnings("resource")
  public static String getLineDelimiter(IFile file) throws IOException
  {
    InputStream inputStream = null;
    try
    {
      inputStream = file.getContents();
      String encoding = file.getCharset();
      Reader reader = encoding == null ? new InputStreamReader(inputStream) : new InputStreamReader(inputStream, encoding);
      char[] text = new char[4048];
      char target = 0;
      for (int count = reader.read(text); count > -1; count = reader.read(text))
      {
        for (int i = 0; i < count; ++i)
        {
          char character = text[i];
          if (character == '\n')
          {
            if (target == '\n')
            {
              return "\n";
            }
            else if (target == '\r')
            {
              return "\r\n";
            }
            else
            {
              target = '\n';
            }
          }
          else if (character == '\r')
          {
            if (target == '\n')
            {
              return "\n\r";
            }
            else if (target == '\r')
            {
              return "\r";
            }
            else
            {
              target = '\r';
            }
          }
        }
      }
    }
    catch (Exception exception)
    {
      // If we can't determine it by reading the file,
      // look at the preferences instead.
    }
    finally
    {
      if (inputStream != null)
      {
        inputStream.close();
      }
    }

    return Platform.getPreferencesService().getString(Platform.PI_RUNTIME, Platform.PREF_LINE_SEPARATOR, System.getProperty(Platform.PREF_LINE_SEPARATOR),
        new IScopeContext[] { new ProjectScope(file.getProject()), InstanceScope.INSTANCE });
  }

  public static synchronized byte[] getSHA1(IFile file) throws NoSuchAlgorithmException, CoreException, IOException
  {
    InputStream contents = file.getContents();
    return IOUtil.getSHA1(contents);
  }

  public static List<IModel> getComponentModels(IProject project)
  {
    List<IModel> result = new ArrayList<IModel>();
    IModel componentModel = PluginRegistry.findModel(project);
    if (componentModel == null)
    {
      componentModel = getFeatureModel(project);
    }

    if (componentModel != null)
    {
      result.add(componentModel);
    }

    result.addAll(getProductModels(project));

    if (result.isEmpty())
    {
      throw new IllegalStateException("The project " + project.getName() + " is neither a plugin nor a feature and contains no products");
    }

    return result;
  }

  public static IModel getComponentModel(IProject project)
  {
    IModel componentModel = PluginRegistry.findModel(project);
    if (componentModel == null)
    {
      componentModel = getFeatureModel(project);
    }

    return componentModel;
  }

  @SuppressWarnings("restriction")
  public static org.eclipse.pde.internal.core.ifeature.IFeatureModel getFeatureModel(IProject project)
  {
    org.eclipse.pde.internal.core.ifeature.IFeatureModel[] featureModels = org.eclipse.pde.internal.core.PDECore.getDefault().getFeatureModelManager()
        .getWorkspaceModels();

    for (org.eclipse.pde.internal.core.ifeature.IFeatureModel featureModel : featureModels)
    {
      if (featureModel.getUnderlyingResource().getProject() == project)
      {
        return featureModel;
      }
    }

    return null;
  }

  @SuppressWarnings("restriction")
  public static List<org.eclipse.pde.internal.core.iproduct.IProductModel> getProductModels(IProject project)
  {
    final List<org.eclipse.pde.internal.core.iproduct.IProductModel> productModels = new ArrayList<org.eclipse.pde.internal.core.iproduct.IProductModel>();
    try
    {
      project.accept(new IResourceProxyVisitor()
      {
        public boolean visit(IResourceProxy proxy) throws CoreException
        {
          if (proxy.getType() == IResource.FILE)
          {
            String name = proxy.getName();
            if (name.endsWith(".product"))
            {
              org.eclipse.pde.internal.core.product.WorkspaceProductModel workspaceProductModel = new org.eclipse.pde.internal.core.product.WorkspaceProductModel(
                  (IFile)proxy.requestResource(), false);
              try
              {
                workspaceProductModel.load();
                productModels.add(workspaceProductModel);
              }
              catch (CoreException ex)
              {
              }
            }
          }

          return true;
        }
      }, IResource.NONE);
    }
    catch (CoreException ex)
    {
      // Ignore
    }

    return productModels;
  }

  @SuppressWarnings("restriction")
  public static Version getComponentVersion(IModel componentModel)
  {
    if (componentModel instanceof IPluginModelBase)
    {
      IPluginModelBase pluginModel = (IPluginModelBase)componentModel;
      return normalize(pluginModel.getBundleDescription().getVersion());
    }

    if (componentModel instanceof org.eclipse.pde.internal.core.ifeature.IFeatureModel)
    {
      Version version = new Version(((org.eclipse.pde.internal.core.ifeature.IFeatureModel)componentModel).getFeature().getVersion());
      return normalize(version);
    }

    if (componentModel instanceof org.eclipse.pde.internal.core.iproduct.IProductModel)
    {
      Version version = new Version(((org.eclipse.pde.internal.core.iproduct.IProductModel)componentModel).getProduct().getVersion());
      return normalize(version);
    }

    throw new IllegalStateException("Unexpected model type " + componentModel);
  }

  public static void cleanReleaseProjects(final String releasePath)
  {
    new Job("Cleaning workspace")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        try
        {
          IBuildConfiguration[] buildConfigs = getBuildConfigs(releasePath);
          WORKSPACE.build(buildConfigs, IncrementalProjectBuilder.CLEAN_BUILD, true, monitor);
          return Status.OK_STATUS;
        }
        catch (CoreException ex)
        {
          return ex.getStatus();
        }
      }
    }.schedule();
  }

  private static IBuildConfiguration[] getBuildConfigs(String releasePath)
  {
    List<IBuildConfiguration> buildConfigs = new ArrayList<IBuildConfiguration>();
    for (IProject project : WORKSPACE.getRoot().getProjects())
    {
      if (project.isAccessible())
      {
        try
        {
          ICommand[] commands = project.getDescription().getBuildSpec();
          for (ICommand command : commands)
          {
            if (BUILDER_ID.equals(command.getBuilderName()))
            {
              VersionBuilderArguments arguments = new VersionBuilderArguments(project);
              String projectReleasePath = arguments.getReleasePath();
              if (releasePath.equals(projectReleasePath))
              {
                IBuildConfiguration buildConfig = project.getActiveBuildConfig();
                buildConfigs.add(buildConfig);
              }

              break;
            }
          }
        }
        catch (Exception ex)
        {
          Activator.log(ex);
        }
      }
    }

    return buildConfigs.toArray(new IBuildConfiguration[buildConfigs.size()]);
  }

  public static IBuild getBuild(IModel componentModel) throws CoreException
  {
    IBuildModel buildModel = getBuildModel(componentModel);
    if (buildModel == null)
    {
      return null;
    }

    IBuild build = buildModel.getBuild();
    if (build == null)
    {
      throw new IllegalStateException("Could not determine build model for " + getName(componentModel));
    }

    return build;
  }

  @SuppressWarnings("restriction")
  public static IBuildModel getBuildModel(IModel componentModel) throws CoreException
  {
    IProject project = componentModel.getUnderlyingResource().getProject();
    if (project != null)
    {
      IFile buildFile = org.eclipse.pde.internal.core.project.PDEProject.getBuildProperties(project);
      if (buildFile.exists())
      {
        IBuildModel buildModel = new org.eclipse.pde.internal.core.build.WorkspaceBuildModel(buildFile);
        buildModel.load();
        return buildModel;
      }
    }

    if (componentModel instanceof org.eclipse.pde.internal.core.iproduct.IProductModel)
    {
      return null;
    }

    throw new IllegalStateException("Could not determine build model for " + getName(componentModel));
  }

  @SuppressWarnings("restriction")
  private static String getName(IModel componentModel)
  {
    if (componentModel instanceof IPluginModelBase)
    {
      IPluginModelBase pluginModel = (IPluginModelBase)componentModel;
      return pluginModel.getBundleDescription().getSymbolicName();
    }

    if (componentModel instanceof org.eclipse.pde.internal.core.ifeature.IFeatureModel)
    {
      return ((org.eclipse.pde.internal.core.ifeature.IFeatureModel)componentModel).getFeature().getId();
    }

    if (componentModel instanceof org.eclipse.pde.internal.core.iproduct.IProductModel)
    {
      return ((org.eclipse.pde.internal.core.ifeature.IFeatureModel)componentModel).getFeature().getId();
    }

    throw new IllegalStateException("Unexpected model type " + componentModel);
  }

  @SuppressWarnings("restriction")
  public static IElement.Type getType(IModel componentModel)
  {
    if (componentModel instanceof IPluginModelBase)
    {
      return IElement.Type.PLUGIN;
    }

    if (componentModel instanceof org.eclipse.pde.internal.core.ifeature.IFeatureModel)
    {
      return IElement.Type.FEATURE;
    }

    if (componentModel instanceof org.eclipse.pde.internal.core.iproduct.IProductModel)
    {
      return IElement.Type.PRODUCT;
    }

    throw new IllegalStateException("Unexpected model type " + componentModel);
  }
}
