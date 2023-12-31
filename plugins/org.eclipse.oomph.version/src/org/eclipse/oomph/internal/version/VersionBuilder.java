/*
 * Copyright (c) 2014-2016, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.version;

import org.eclipse.oomph.internal.version.Activator.LaxLowerBoundCheckMode;
import org.eclipse.oomph.internal.version.Activator.ReleaseCheckMode;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.version.IElement;
import org.eclipse.oomph.version.IElement.Type;
import org.eclipse.oomph.version.IElementResolver;
import org.eclipse.oomph.version.IRelease;
import org.eclipse.oomph.version.IReleaseManager;
import org.eclipse.oomph.version.Markers;
import org.eclipse.oomph.version.VersionUtil;
import org.eclipse.oomph.version.VersionValidator;

import org.eclipse.emf.common.util.UniqueEList;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.service.resolver.BaseDescription;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.osgi.service.resolver.ImportPackageSpecification;
import org.eclipse.osgi.service.resolver.VersionConstraint;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginExtensionPoint;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;

import org.osgi.framework.Version;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class VersionBuilder extends IncrementalProjectBuilder implements IElementResolver
{
  private static final Path DESCRIPTION_PATH = new Path(".project"); //$NON-NLS-1$

  private static final Path OPTIONS_PATH = new Path(".options"); //$NON-NLS-1$

  private static final Path MAVEN_POM_PATH = new Path("pom.xml"); //$NON-NLS-1$

  private static final Path MANIFEST_PATH = new Path("META-INF/MANIFEST.MF"); //$NON-NLS-1$

  private static final Path FEATURE_PATH = new Path("feature.xml"); //$NON-NLS-1$

  public static final String INTEGRATION_PROPERTY_KEY = "baseline.for.integration"; //$NON-NLS-1$

  public static final String SERVICE_PROPERTY_KEY = "provide.service"; //$NON-NLS-1$

  public static final String DEVIATIONS_PROPERTY_KEY = "show.deviations"; //$NON-NLS-1$

  public static final String ROOT_PROJECTS_KEY = "root.projects"; //$NON-NLS-1$

  public static final String IGNORED_REFERENCES_KEY = "ignored.references"; //$NON-NLS-1$

  private static final Pattern DEBUG_OPTION_PATTERN = Pattern.compile("^( *)([^/ \\n\\r]+)/([^ =]+)( *=.*)$", Pattern.MULTILINE); //$NON-NLS-1$

  private static final String AUTOMATIC_MODULE_NAME_HEADER = "Automatic-Module-Name"; //$NON-NLS-1$

  private static final Pattern AUTOMATIC_MODULE_NAME_PATTERN = Pattern.compile(AUTOMATIC_MODULE_NAME_HEADER + ":( *)(.*)"); //$NON-NLS-1$

  private static final Set<String> releasePaths = new HashSet<>();

  private static final Map<IElement, IElement> elementCache = new HashMap<>();

  private static final Map<IElement, Set<IElement>> elementReferences = new HashMap<>();

  private static IResourceChangeListener postBuildListener;

  private static SAXParserFactory parserFactory;

  private IRelease release;

  private Boolean integration;

  private Boolean service;

  private Boolean deviations;

  private Set<String> rootProjects;

  private Set<String> ignoredReferences;

  private VersionBuilderArguments arguments;

  public VersionBuilder()
  {
  }

  @Override
  public IElement resolveElement(IElement key)
  {
    try
    {
      ensureCacheExists();
      return elementCache.get(key);
    }
    catch (Exception ex)
    {
      Activator.log(ex);
      return null;
    }
  }

  public Set<IElement> resolveReferences(IElement key)
  {
    try
    {
      ensureCacheExists();
      return elementReferences.get(key);
    }
    catch (Exception ex)
    {
      Activator.log(ex);
      return null;
    }
  }

  private void ensureCacheExists() throws NoSuchAlgorithmException, CoreException, IOException
  {
    for (String path : Activator.getReleasePaths())
    {
      if (releasePaths.add(path))
      {
        Map<IElement, IElement> elements = IReleaseManager.INSTANCE.createElements(path, false);
        elementCache.putAll(elements);
        for (IElement element : elements.keySet())
        {
          IElement.Type type = element.getType();
          if (type == IElement.Type.FEATURE || type == IElement.Type.PRODUCT)
          {
            for (IElement child : element.getChildren())
            {
              Set<IElement> references = elementReferences.get(child);
              if (references == null)
              {
                references = new HashSet<>();
                elementReferences.put(child, references);
              }

              references.add(element);
            }
          }
        }
      }
    }
  }

  @Override
  protected void clean(IProgressMonitor monitor) throws CoreException
  {
    IProject project = getProject();

    monitor.beginTask("", 1); //$NON-NLS-1$
    monitor.subTask(NLS.bind(Messages.VersionBuilder_Clean_task, project.getName()));

    try
    {
      Activator.clearBuildState(project);
      Markers.deleteAllMarkers(project);
    }
    finally
    {
      monitor.done();
    }
  }

  @Override
  protected final IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException
  {
    List<IProject> buildDependencies = new UniqueEList<>();
    build(kind, args, monitor, buildDependencies);
    return buildDependencies.toArray(new IProject[buildDependencies.size()]);
  }

  private void build(int kind, Map<String, String> args, IProgressMonitor monitor, List<IProject> buildDependencies) throws CoreException
  {
    arguments = new VersionBuilderArguments(args);
    VersionValidator validator = null;

    IProject project = getProject();
    IFile projectDescription = project.getFile(DESCRIPTION_PATH);

    if (postBuildListener == null)
    {
      postBuildListener = new IResourceChangeListener()
      {
        @Override
        public void resourceChanged(IResourceChangeEvent event)
        {
          elementCache.clear();
          elementReferences.clear();
          releasePaths.clear();
          arguments = null;
        }
      };

      project.getWorkspace().addResourceChangeListener(postBuildListener, IResourceChangeEvent.POST_BUILD);
      Activator.setPostBuildListener(postBuildListener);
    }

    BuildState buildState = Activator.getBuildState(project);
    byte[] releaseSpecDigest = buildState.getReleaseSpecDigest();

    VersionBuilderArguments oldVersionBuilderArguments = new VersionBuilderArguments(buildState.getArguments());
    buildState.setArguments(arguments);

    IResourceDelta delta = releaseSpecDigest == null || kind == FULL_BUILD || kind == CLEAN_BUILD || !oldVersionBuilderArguments.equals(arguments) ? null
        : getDelta(project);

    monitor.beginTask("", 1); //$NON-NLS-1$
    monitor.subTask(NLS.bind(Messages.VersionBuilder_Checking_task, project.getName()));

    try
    {
      /*
       * Determine release data to validate against
       */

      String releasePathArg = arguments.getReleasePath();
      if (releasePathArg == null)
      {
        String msg = Messages.VersionBuilder_NoReleaseSpecFile_message;
        Markers.addMarker(projectDescription, msg, IMarker.SEVERITY_ERROR, "(" + VersionUtil.BUILDER_ID + ")"); //$NON-NLS-1$ //$NON-NLS-2$
        return;
      }

      if (Activator.getReleaseCheckMode(releasePathArg) == ReleaseCheckMode.NONE)
      {
        return;
      }

      IPath releasePath = new Path(releasePathArg);

      try
      {
        IFile releaseSpecFile = ResourcesPlugin.getWorkspace().getRoot().getFile(releasePath);
        buildDependencies.add(releaseSpecFile.getProject());

        IRelease release;
        if (!releaseSpecFile.exists())
        {
          release = IReleaseManager.INSTANCE.createRelease(releaseSpecFile);
        }
        else
        {
          release = IReleaseManager.INSTANCE.getRelease(releaseSpecFile);
        }

        byte[] digest = VersionUtil.getSHA1(releaseSpecFile);
        if (releaseSpecDigest == null || !MessageDigest.isEqual(digest, releaseSpecDigest))
        {
          buildState.setReleaseSpecDigest(digest);
          delta = null;
        }

        this.release = release;

        Markers.deleteAllMarkers(projectDescription, Markers.RELEASE_PATH_PROBLEM);
      }
      catch (Exception ex)
      {
        Activator.log(ex, IStatus.WARNING);
        String msg = NLS.bind(Messages.VersionBuilder_ReleaseSpecProblem_message, releasePath, ex.getMessage());
        IMarker marker = Markers.addMarker(projectDescription, msg, IMarker.SEVERITY_ERROR, "(" + releasePath.toString().replace(".", "\\.") + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        if (marker != null)
        {
          marker.setAttribute(Markers.PROBLEM_TYPE, Markers.RELEASE_PATH_PROBLEM);
        }

        return;
      }

      IFile propertiesFile = VersionUtil.getFile(releasePath, "properties"); //$NON-NLS-1$
      long propertiesTimeStamp = propertiesFile.getLocalTimeStamp();
      boolean formerDeviations = buildState.isDeviations();
      boolean deleteDeviationMarkers = false;
      if (buildState.getPropertiesTimeStamp() != propertiesTimeStamp)
      {
        if (initReleaseProperties(propertiesFile))
        {
          delta = null;
        }

        buildState.setDeviations(deviations);
        buildState.setIntegration(integration);
        buildState.setService(service);
        buildState.setRootProjects(rootProjects);
        buildState.setIgnoredReferences(ignoredReferences);
        buildState.setPropertiesTimeStamp(propertiesTimeStamp);

        if (formerDeviations && !deviations)
        {
          deleteDeviationMarkers = true;
        }
      }
      else
      {
        deviations = formerDeviations;
        integration = buildState.isIntegration();
        service = buildState.isService();
        rootProjects = buildState.getRootProjects();
        ignoredReferences = buildState.getIgnoredReferences();
      }

      for (IModel componentModel : VersionUtil.getComponentModels(project))
      {
        IFile componentModelFile = (IFile)componentModel.getUnderlyingResource();

        if (deleteDeviationMarkers)
        {
          Markers.deleteAllMarkers(componentModelFile, Markers.DEVIATION_INFO);
        }

        IPath componentModelPath = componentModelFile.getProjectRelativePath();
        boolean checkComponentModel = delta == null || delta.findMember(componentModelPath) != null;

        if (!arguments.isIgnoreLaxLowerBoundDependencyVersions() && componentModel instanceof IPluginModelBase)
        {
          addDependencies(buildDependencies, project);
        }

        IElement element = IReleaseManager.INSTANCE.createElement(componentModel, true, false);
        IElement.Type elementType = element.getType();
        for (IElement child : element.getAllChildren(this, release))
        {
          IProject childProject = getProject(child);
          if (childProject != null)
          {
            buildDependencies.add(childProject);
            if (checkComponentModel)
            {
              IModel childProjectComponentModel = VersionUtil.getComponentModel(childProject);
              if (childProjectComponentModel != null)
              {
                IResource underlyingResource = childProjectComponentModel.getUnderlyingResource();
                Markers.deleteAllMarkers(underlyingResource, Markers.UNREFERENCED_ELEMENT_PROBLEM);
              }
            }
          }
        }

        if (elementType != IElement.Type.PRODUCT)
        {
          Markers.deleteAllMarkers(componentModelFile, Markers.UNREFERENCED_ELEMENT_PROBLEM);
          if (Activator.getReleaseCheckMode(releasePathArg) == ReleaseCheckMode.FULL && !rootProjects.contains(project.getName()))
          {
            Set<IElement> elementReference = resolveReferences(element);
            if (elementReference == null || elementReference.isEmpty())
            {
              addUnreferencedElementMarker(componentModelFile, element);
            }
          }
        }

        IElement releaseElement = release.getElements().get(element.trimVersion());
        boolean contentMustChange = false;
        if (elementType == IElement.Type.PLUGIN)
        {
          if (!arguments.isIgnoreSchemaBuilder())
          {
            if (delta == null || delta.findMember(DESCRIPTION_PATH) != null)
            {
              checkSchemaBuilder((IPluginModelBase)componentModel, projectDescription);
            }
          }
          else if (!oldVersionBuilderArguments.isIgnoreSchemaBuilder())
          {
            Markers.deleteAllMarkers(projectDescription, Markers.SCHEMA_BUILDER_PROBLEM);
          }

          if (!arguments.isIgnoreDebugOptions())
          {
            if (delta == null || delta.findMember(OPTIONS_PATH) != null)
            {
              checkDebugOptions((IPluginModelBase)componentModel);
            }
          }
          else if (!oldVersionBuilderArguments.isIgnoreDebugOptions())
          {
            Markers.deleteAllMarkers(project.getFile(OPTIONS_PATH), Markers.DEBUG_OPTION_PROBLEM);
          }

          if (!arguments.isIgnoreAutomaticModuleName())
          {
            if (delta == null || delta.findMember(MANIFEST_PATH) != null)
            {
              checkAutomaticModuleName((IPluginModelBase)componentModel);
            }
          }
          else if (!oldVersionBuilderArguments.isIgnoreDebugOptions())
          {
            Markers.deleteAllMarkers(project.getFile(MANIFEST_PATH), Markers.DEBUG_OPTION_PROBLEM);
          }

          if (!arguments.isIgnoreMissingDependencyRanges())
          {
            if (checkComponentModel)
            {
              checkDependencyRanges((IPluginModelBase)componentModel);
            }
          }
          else if (!oldVersionBuilderArguments.isIgnoreMissingDependencyRanges())
          {
            Markers.deleteAllMarkers(getProject().getFile(MANIFEST_PATH), Markers.LOWER_BOUND_VERSION_PROBLEM);
          }

          LaxLowerBoundCheckMode laxLowerBoundCheckMode = Activator.getLaxLowerBoundCheckMode(releasePathArg);
          // Only check the lower bounds if the element itself has been modified.
          // Only in this case does it potentially use changes to the bundles/packages it uses (except maybe for new overloaded methods).
          if (!arguments.isIgnoreLaxLowerBoundDependencyVersions()
              && (releaseElement == null || !releaseElement.getVersion().equals(element.getVersion()) || laxLowerBoundCheckMode == LaxLowerBoundCheckMode.ALL))
          {
            contentMustChange = !checkLowerBoundVersions((IPluginModelBase)componentModel, laxLowerBoundCheckMode);
          }
          else if (!oldVersionBuilderArguments.isIgnoreLaxLowerBoundDependencyVersions())
          {
            Markers.deleteAllMarkers(getProject().getFile(MANIFEST_PATH), Markers.LOWER_BOUND_VERSION_PROBLEM);
          }

          if (!arguments.isIgnoreMissingExportVersions())
          {
            if (checkComponentModel)
            {
              checkPackageExports((IPluginModelBase)componentModel);
            }
          }
          else if (!oldVersionBuilderArguments.isIgnoreMissingExportVersions())
          {
            Markers.deleteAllMarkers(getProject().getFile(MANIFEST_PATH), Markers.EXPORT_VERSION_PROBLEM);
          }

          if (hasAPIToolsMarker((IPluginModelBase)componentModel))
          {
            continue;
          }
        }
        else if (elementType == IElement.Type.FEATURE)
        {
          if (!arguments.isIgnoreFeatureNature())
          {
            if (delta == null || delta.findMember(DESCRIPTION_PATH) != null)
            {
              checkFeatureNature(projectDescription);
            }
          }
          else if (!oldVersionBuilderArguments.isIgnoreFeatureNature())
          {
            Markers.deleteAllMarkers(projectDescription, Markers.FEATURE_NATURE_PROBLEM);
          }
        }

        if (!arguments.isIgnoreMalformedVersions())
        {
          if (checkComponentModel)
          {
            if (checkMalformedVersions(componentModel))
            {
              continue;
            }
          }
        }
        else if (!oldVersionBuilderArguments.isIgnoreMalformedVersions())
        {
          Markers.deleteAllMarkers(componentModelFile, Markers.MALFORMED_VERSION_PROBLEM);
        }

        if (arguments.isCheckMavenPom())
        {
          if (delta == null || delta.findMember(MAVEN_POM_PATH) != null || delta.findMember(componentModelPath) != null)
          {
            checkMavenPom(element);
          }
        }
        else if (oldVersionBuilderArguments.isCheckMavenPom())
        {
          Markers.deleteAllMarkers(project.getFile(MAVEN_POM_PATH), Markers.MAVEN_POM_PROBLEM);
        }

        if (releaseElement == null)
        {
          if (VersionUtil.DEBUG)
          {
            System.out.println(NLS.bind(Messages.VersionBuilder_UnresolveProject_message, project.getName()));
          }

          continue;
        }

        Markers.deleteAllMarkers(componentModelFile, Markers.VERSION_NATURE_PROBLEM);
        if (!arguments.isIgnoreMissingVersionNature())
        {
          for (IElement child : element.getChildren())
          {
            IModel childComponentModel = ReleaseManager.INSTANCE.getComponentModel(child);
            if (childComponentModel != null)
            {
              IResource childComponentModelFile = childComponentModel.getUnderlyingResource();
              if (childComponentModelFile != null)
              {
                IProject childProject = childComponentModelFile.getProject();
                if (!childProject.hasNature(VersionNature.NATURE_ID))
                {
                  Type childType = child.getType();
                  String name = child.getName();
                  String label = childType.toString();
                  String tag = childType.getTag();
                  String msg = NLS.bind(Messages.VersionBuilder_MissingVersionBuilder_message, label, name);
                  IMarker marker = addFeatureChildMarker(componentModelFile, Markers.VERSION_NATURE_PROBLEM, tag, name, msg, child.isLicenseFeature(), false,
                      null, IMarker.SEVERITY_ERROR);
                  marker.setAttribute(Markers.QUICK_FIX_NATURE, VersionNature.NATURE_ID);
                  marker.setAttribute(Markers.QUICK_FIX_PROJECT, childProject.getName());
                }
              }
            }
          }
        }

        Version elementVersion = element.getVersion();
        Version releaseVersion = releaseElement.getVersion();
        Version nextVersion = getNextVersion(releaseVersion);

        int comparison = releaseVersion.compareTo(elementVersion);
        if (comparison != 0 && deviations && checkComponentModel)
        {
          addDeviationMarker(componentModelFile, element, releaseVersion);
        }

        Markers.deleteAllMarkers(componentModelFile, Markers.COMPONENT_VERSION_PROBLEM, Markers.FEATURE_CLOSURE_PROBLEM);
        boolean versionProperlyIncreased = false;

        if (comparison < 0)
        {
          versionProperlyIncreased = nextVersion.equals(elementVersion);
          if (!versionProperlyIncreased)
          {
            boolean noOtherIncrements = elementVersion.getMajor() == nextVersion.getMajor() && elementVersion.getMinor() == nextVersion.getMinor();
            if (noOtherIncrements)
            {
              addVersionMarker(componentModelFile, NLS.bind(Messages.VersionBuilder_VersionShouldBe_message, nextVersion), nextVersion);
            }
          }

          if (elementType == IElement.Type.PLUGIN)
          {
            continue;
          }
        }

        if (comparison > 0)
        {
          addVersionMarker(componentModelFile, NLS.bind(Messages.VersionBuilder_VersionDecreased_message, releaseVersion), releaseVersion);
          continue;
        }

        if (elementType == IElement.Type.FEATURE || elementType == IElement.Type.PRODUCT)
        {
          if (!arguments.isIgnoreFeatureContentRedundancy())
          {
            checkFeatureRedundancy(componentModelFile, element);
          }

          if (arguments.isCheckFeatureClosureCompleteness())
          {
            checkFeatureClosureCompleteness(componentModelFile, element);
          }

          if (!arguments.isIgnoreFeatureContentChanges())
          {
            List<Problem> problems = new ArrayList<>();
            checkFeatureReferences(componentModelFile, element, problems);
            if (!problems.isEmpty())
            {
              createMarkers(componentModelFile, problems, ComponentReferenceType.UNRESOLVED);
              continue;
            }

            ComponentReferenceType change = checkFeatureContentChanges(element, releaseElement, problems);
            if (change != ComponentReferenceType.UNCHANGED)
            {
              Version nextFeatureVersion = getNextFeatureVersion(releaseVersion, nextVersion, change);
              if (elementVersion.compareTo(nextFeatureVersion) < 0)
              {
                IMarker marker = addVersionMarker(componentModelFile,
                    NLS.bind(Messages.VersionBuilder_VersionMustBeIncreaseReferencesChanged_message, nextFeatureVersion, elementType.toString().toLowerCase()),
                    nextFeatureVersion);
                if (marker != null)
                {
                  marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_CONTENT_CHANGES_ARGUMENT);
                }

                createMarkers(componentModelFile, problems, change);
              }

              continue;
            }

            if (!elementVersion.equals(releaseVersion))
            {
              continue;
            }
          }
        }

        if (versionProperlyIncreased || elementVersion.getMajor() > nextVersion.getMajor() || elementVersion.getMinor() > nextVersion.getMinor())
        {
          continue;
        }

        if (contentMustChange)
        {
          addVersionMarker(componentModelFile, NLS.bind(Messages.VersionBuilder_VersionMustBeIncreasedManifestMustChange_message, nextVersion), nextVersion);
          continue;
        }

        // Initialize the validator on demand.
        if (validator == null)
        {
          String validatorClassName = arguments.getValidatorClassName();
          if (validatorClassName == null)
          {
            validatorClassName = IVersionBuilderArguments.DEFAULT_VALIDATOR_CLASS_NAME;
          }

          try
          {
            Markers.deleteAllMarkers(projectDescription, Markers.VALIDATOR_CLASS_PROBLEM);

            Class<?> c = Class.forName(validatorClassName, true, VersionBuilder.class.getClassLoader());
            validator = (VersionValidator)c.getDeclaredConstructor().newInstance();

            if (VersionUtil.DEBUG)
            {
              System.out.println(validator.getClass().getName() + ": " + project.getName()); //$NON-NLS-1$
            }
          }
          catch (Exception ex)
          {
            String msg = ex.getLocalizedMessage() + ": " + validatorClassName; //$NON-NLS-1$
            IMarker marker = Markers.addMarker(projectDescription, msg, IMarker.SEVERITY_ERROR, ".*(" + validatorClassName + ").*"); //$NON-NLS-1$ //$NON-NLS-2$
            if (marker != null)
            {
              marker.setAttribute(Markers.PROBLEM_TYPE, Markers.VALIDATOR_CLASS_PROBLEM);
            }

            return;
          }

          String validatorVersion = validator.getVersion();
          if (!VersionUtil.equals(validatorClassName, buildState.getValidatorClass())
              || !VersionUtil.equals(validatorVersion, buildState.getValidatorVersion()))
          {
            buildState.clearValidatorStates();
            delta = null;
          }

          buildState.setValidatorClass(validatorClassName);
          buildState.setValidatorVersion(validatorVersion);
        }

        // Apply the validator.
        validator.updateBuildState(buildState, release, project, delta, componentModel, monitor);

        if (buildState.isChangedSinceRelease())
        {
          addVersionMarker(componentModelFile, NLS.bind(Messages.VersionBuilder_VersionMustBeIncreasedProjectContentsChanged_message, nextVersion),
              nextVersion);
        }
      }
    }
    catch (Exception ex)
    {
      try
      {
        if (validator != null)
        {
          validator.abort(buildState, project, ex, monitor);
        }

        Activator.log(ex);
      }
      catch (Exception ignore)
      {
        Activator.log(ignore);
      }
    }
    finally
    {
      deviations = null;
      integration = null;
      service = null;
      release = null;
      monitor.done();
    }
  }

  private void addDependencies(List<IProject> buildDependencies, IProject project)
  {
    IModel childProjectComponentModel = VersionUtil.getComponentModel(project);
    if (childProjectComponentModel instanceof IPluginModelBase)
    {
      BundleDescription bundleDescription = ((IPluginModelBase)childProjectComponentModel).getBundleDescription();
      if (bundleDescription != null)
      {
        BundleDescription[] requiredBundles = bundleDescription.getResolvedRequires();
        if (requiredBundles != null)
        {
          for (BundleDescription requiredBundleDescription : requiredBundles)
          {
            IElement element = new Element(Type.PLUGIN, requiredBundleDescription.getSymbolicName(), null, false);
            IProject childProject = getProject(element);
            if (childProject != null)
            {
              buildDependencies.add(childProject);
            }
          }
        }
      }
    }
  }

  private void createMarkers(IFile componentModelFile, List<Problem> problems, ComponentReferenceType change)
  {
    for (Problem problem : problems)
    {
      ComponentReferenceType componentReferenceType = problem.getComponentReferenceType();
      if (componentReferenceType.ordinal() >= change.ordinal())
      {
        addIncludeMarker(componentModelFile, problem.getElement(), problem.getSeverity(), problem.getVersion(), componentReferenceType);
      }
    }
  }

  private boolean hasAPIToolsMarker(IPluginModelBase pluginModel)
  {
    try
    {
      IResource manifest = pluginModel.getUnderlyingResource();
      for (IMarker marker : manifest.findMarkers(IMarker.PROBLEM, true, IResource.DEPTH_ZERO))
      {
        if (marker.getType().startsWith("org.eclipse.pde.api.tools")) //$NON-NLS-1$
        {
          return true;
        }
      }
    }
    catch (CoreException ex)
    {
      Activator.log(ex);
    }

    return false;
  }

  private boolean initReleaseProperties(IFile propertiesFile) throws CoreException, IOException
  {
    if (propertiesFile.exists())
    {
      InputStream contents = null;

      try
      {
        boolean result = false;

        contents = propertiesFile.getContents();

        Properties properties = new Properties();
        properties.load(contents);

        deviations = Boolean.valueOf(properties.getProperty(DEVIATIONS_PROPERTY_KEY, "false")); //$NON-NLS-1$

        rootProjects = new HashSet<>();
        for (String rootProject : Arrays.asList(properties.getProperty(ROOT_PROJECTS_KEY, "").split(" "))) //$NON-NLS-1$ //$NON-NLS-2$
        {
          rootProjects.add(rootProject.replace("\\ ", " ").replace("\\\\", "\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }

        ignoredReferences = new HashSet<>();
        for (String ignoredReference : Arrays.asList(properties.getProperty(IGNORED_REFERENCES_KEY, "").split(" "))) //$NON-NLS-1$ //$NON-NLS-2$
        {
          ignoredReferences.add(ignoredReference.replace("\\ ", " ").replace("\\\\", "\\")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }

        Boolean newIntegrationValue = Boolean.valueOf(properties.getProperty(INTEGRATION_PROPERTY_KEY, "true")); //$NON-NLS-1$
        if (!newIntegrationValue.equals(integration))
        {
          integration = newIntegrationValue;
          result = true;
        }

        Boolean newServiceValue = Boolean.valueOf(properties.getProperty(SERVICE_PROPERTY_KEY, "true")); //$NON-NLS-1$
        if (!newServiceValue.equals(service))
        {
          service = newServiceValue;
          result = true;
        }

        return result;
      }
      finally
      {
        IOUtil.closeSilent(contents);
      }
    }

    deviations = false;
    integration = true;
    service = true;
    rootProjects = new HashSet<>();
    ignoredReferences = new HashSet<>();

    String lineDelimiter = VersionUtil.getLineDelimiter(propertiesFile);
    String contents = INTEGRATION_PROPERTY_KEY + " = " + integration + lineDelimiter + SERVICE_PROPERTY_KEY + " = " + service + lineDelimiter //$NON-NLS-1$ //$NON-NLS-2$
        + DEVIATIONS_PROPERTY_KEY + " = " + deviations + lineDelimiter + ROOT_PROJECTS_KEY + " = "; //$NON-NLS-1$ //$NON-NLS-2$

    String charsetName = propertiesFile.getCharset();
    byte[] bytes = contents.getBytes(charsetName);

    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    propertiesFile.create(bais, true, new NullProgressMonitor());
    return true;
  }

  private Version getNextVersion(Version releaseVersion)
  {
    if (service)
    {
      return new Version(releaseVersion.getMajor(), releaseVersion.getMinor(), releaseVersion.getMicro() + (integration ? 100 : 1));
    }

    return new Version(releaseVersion.getMajor(), releaseVersion.getMinor() + 1, 0);
  }

  private Version getNextFeatureVersion(Version releaseVersion, Version nextImplementationVersion, ComponentReferenceType change)
  {
    switch (change)
    {
      case REMOVED:
      case MAJOR_CHANGED:
        return new Version(releaseVersion.getMajor() + 1, 0, 0);
      case ADDED:
      case FRAGMENT_REMOVED:
      case MINOR_CHANGED:
        return new Version(releaseVersion.getMajor(), releaseVersion.getMinor() + 1, 0);
      case MICRO_CHANGED:
        return nextImplementationVersion;
      default:
        throw new IllegalArgumentException();
    }
  }

  private void checkFeatureReferences(IFile file, IElement element, List<Problem> problems)
  {
    for (IElement child : element.getChildren())
    {
      // PDE already warns for unresolved references, except for license features, see bug 387750
      //
      boolean hasSpecificVersion = !child.isVersionUnresolved();
      if (hasSpecificVersion)
      {
        IElement resolvedChild = resolveElement(child);
        if (resolvedChild == null)
        {
          resolvedChild = resolveElement(child.trimVersion());
          Version resolvedChildVersion = resolvedChild == null ? null : resolvedChild.getVersion();
          addProblem(child, IMarker.SEVERITY_ERROR, ComponentReferenceType.UNRESOLVED, resolvedChildVersion, problems);
        }
      }
    }
  }

  private void checkFeatureRedundancy(IFile file, IElement element)
  {
    Map<IElement, IElement> allFeatureChildren = new HashMap<>();
    List<IElement> children = element.getChildren();
    for (IElement child : children)
    {
      if (child.getType() == IElement.Type.FEATURE)
      {
        child = resolveElement(child);
        if (child != null)
        {
          Set<IElement> allChildren = child.getAllChildren(this, release);
          for (IElement featureChild : allChildren)
          {
            allFeatureChildren.put(featureChild, child);
          }
        }
      }
    }

    int i = 0;
    for (IElement child : children)
    {
      IElement featureElement = allFeatureChildren.get(child);
      if (featureElement != null && !child.isLicenseFeature())
      {
        try
        {
          addRedundancyMarker(file, child, featureElement);
        }
        catch (Exception ex)
        {
          Activator.log(ex);
        }
      }

      if (children.indexOf(child) != i)
      {
        addRedundancyMarker(file, child, null);
      }

      ++i;
    }
  }

  private void checkFeatureClosureCompleteness(IFile file, IElement element)
  {
    List<IElement> children = element.getChildren();
    Set<IElement> plugins = new HashSet<>();
    Map<IElement, IElement> closure = new HashMap<>();

    for (IElement child : children)
    {
      if (child.getType() == IElement.Type.FEATURE)
      {
        collectFeatureClosure(child, child, closure);
      }
      else
      {
        plugins.add(child);
      }
    }

    for (Map.Entry<IElement, IElement> entry : closure.entrySet())
    {
      IElement plugin = entry.getKey();
      if (plugins.add(plugin))
      {
        try
        {
          IElement mainFeature = entry.getValue();
          addFeatureClosureMarker(file, plugin, mainFeature);
        }
        catch (Exception ex)
        {
          Activator.log(ex);
        }
      }
    }
  }

  private void collectFeatureClosure(IElement feature, IElement mainFeature, Map<IElement, IElement> closure)
  {
    feature = resolveElement(feature);
    if (feature != null)
    {
      for (IElement child : feature.getChildren())
      {
        if (child.getType() == IElement.Type.PLUGIN)
        {
          if (!closure.containsKey(child))
          {
            closure.put(child, mainFeature);
          }
        }
        else
        {
          collectFeatureClosure(child, mainFeature, closure);
        }
      }
    }
  }

  private ComponentReferenceType checkFeatureContentChanges(IElement element, IElement releasedElement, List<Problem> problems)
  {
    ComponentReferenceType biggestChange = ComponentReferenceType.UNCHANGED;
    Collection<IElement> children = arguments.isCheckFeatureClosureContent() ? element.getAllChildren(this, release) : element.getChildren();
    for (IElement child : children)
    {
      ComponentReferenceType change = checkFeatureContentChanges(element, releasedElement, child, problems);
      biggestChange = ComponentReferenceType.values()[Math.max(biggestChange.ordinal(), change.ordinal())];
    }

    Collection<IElement> allChildren = element.getAllChildren(this, release);
    Collection<IElement> releasedElementChildren = arguments.isCheckFeatureClosureContent() ? releasedElement.getAllChildren(release, this)
        : releasedElement.getChildren();
    for (IElement releasedElementsChild : releasedElementChildren)
    {
      IElement trimmedVersion = releasedElementsChild.trimVersion();
      if (!children.contains(trimmedVersion))
      {
        // IElement resolvedElement = resolveElement(trimmedVersion);
        // if (resolvedElement != null && !resolvedElement.isVersionUnresolved())
        {
          ComponentReferenceType componentReferenceType = releasedElementsChild.isFragment() ? ComponentReferenceType.FRAGMENT_REMOVED
              : ComponentReferenceType.REMOVED;
          if (addProblem(releasedElementsChild, IMarker.SEVERITY_WARNING, componentReferenceType, null, problems))
          {
            // If we've removed a redundant plugin or feature, we don't want it to be considered a remove that requires a major version increment.
            biggestChange = ComponentReferenceType.values()[Math.max(biggestChange.ordinal(),
                allChildren.contains(trimmedVersion) ? ComponentReferenceType.MINOR_CHANGED.ordinal() : componentReferenceType.ordinal())];
          }
        }
      }
    }

    return biggestChange;
  }

  private ComponentReferenceType checkFeatureContentChanges(IElement element, IElement releasedElement, IElement childElement, List<Problem> problems)
  {
    IElement releasedElementsChild = releasedElement.getChild(release, this, childElement);
    if (releasedElementsChild == null)
    {
      // Don't consider it added if it was present with a different version.
      //
      releasedElementsChild = releasedElement.getChild(release, this, childElement.trimVersion());
      if (releasedElementsChild == null)
      {
        if (addProblem(childElement, IMarker.SEVERITY_WARNING, ComponentReferenceType.ADDED, null, problems))
        {
          return ComponentReferenceType.ADDED;
        }
      }
    }

    IElement childsReleasedElement = release.getElements().get(childElement);
    if (childsReleasedElement == null)
    {
      childsReleasedElement = release.getElements().get(childElement.trimVersion());
      if (childsReleasedElement == null)
      {
        return ComponentReferenceType.UNCHANGED;
      }
    }

    if (!childsReleasedElement.isVersionUnresolved())
    {
      Version releasedVersion = childsReleasedElement.getVersion();
      Version version = childElement.trimVersion().getResolvedVersion();
      if (!version.equals(Version.emptyVersion))
      {
        if (version.getMajor() != releasedVersion.getMajor())
        {
          if (addProblem(childsReleasedElement, IMarker.SEVERITY_WARNING, ComponentReferenceType.MAJOR_CHANGED, version, problems))
          {
            return ComponentReferenceType.MAJOR_CHANGED;
          }
        }

        if (version.getMinor() != releasedVersion.getMinor())
        {
          if (addProblem(childsReleasedElement, IMarker.SEVERITY_WARNING, ComponentReferenceType.MINOR_CHANGED, version, problems))
          {
            return ComponentReferenceType.MINOR_CHANGED;
          }
        }

        if (version.getMicro() != releasedVersion.getMicro())
        {
          if (addProblem(childsReleasedElement, IMarker.SEVERITY_WARNING, ComponentReferenceType.MICRO_CHANGED, version, problems))
          {
            return ComponentReferenceType.MICRO_CHANGED;
          }
        }
      }
    }

    return ComponentReferenceType.UNCHANGED;
  }

  private SAXParser getParser() throws ParserConfigurationException, SAXException
  {
    if (parserFactory == null)
    {
      parserFactory = SAXParserFactory.newInstance();
    }

    return parserFactory.newSAXParser();
  }

  private void checkMavenPom(IElement element) throws CoreException
  {
    final IFile file = getProject().getFile(MAVEN_POM_PATH);
    if (file.isAccessible())
    {
      Markers.deleteAllMarkers(file, Markers.MAVEN_POM_PROBLEM);

      final String componentName = element.getID() == null ? element.getName() : element.getID();
      final String componentVersion = StringUtil.removeSuffix(element.getVersion().toString(), ".qualifier"); //$NON-NLS-1$
      final String componentType = element.getType().toString().toLowerCase();

      InputStream contents = null;

      try
      {
        contents = file.getContents();

        SAXParser parser = getParser();
        parser.parse(new BufferedInputStream(contents), new DefaultHandler()
        {
          private Locator locator;

          private int level;

          private int lineNumber;

          private int found;

          private StringBuilder builder;

          @Override
          public void setDocumentLocator(Locator locator)
          {
            this.locator = locator;
          }

          @Override
          public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
          {
            if (++level == 2)
            {
              if ("artifactId".equals(qName) || "version".equals(qName)) //$NON-NLS-1$ //$NON-NLS-2$
              {
                lineNumber = locator.getLineNumber();
                builder = new StringBuilder();
              }
            }
          }

          @Override
          public void characters(char[] ch, int start, int length) throws SAXException
          {
            if (builder != null)
            {
              builder.append(ch, start, length);
            }
          }

          @Override
          public void endElement(String uri, String localName, String qName) throws SAXException
          {
            --level;

            if (builder != null)
            {
              String textContent = builder.toString().replace('\n', ' ').replace('\r', ' ').replace('\t', ' ').trim();

              if ("artifactId".equals(qName)) //$NON-NLS-1$
              {
                if (!textContent.equals(componentName))
                {
                  addMarker(file, NLS.bind(Messages.VersionBuilder_MavenBadArtifactID_message, new Object[] { textContent, componentType, componentName }),
                      textContent, componentName);
                }
              }
              else
              {
                String version = StringUtil.removeSuffix(textContent, "-SNAPSHOT"); //$NON-NLS-1$
                if (!version.equals(componentVersion))
                {
                  addMarker(file, NLS.bind(Messages.VersionBuilder_MavenBadVersion_message, new Object[] { version, componentType, componentVersion }), version,
                      componentVersion);
                }
              }

              if (++found == 2)
              {
                throw new SAXException("STOP-EARLY"); //$NON-NLS-1$
              }
            }

            builder = null;
          }

          private void addMarker(IFile file, String message, String pomValue, String componentValue)
          {
            StringBuilder regexBuilder = new StringBuilder();
            for (int i = 1; i < lineNumber; i++)
            {
              regexBuilder.append('$');
            }

            regexBuilder.append(".*(" + pomValue.replace(".", "\\.") + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            String regex = regexBuilder.toString();

            try
            {
              IMarker marker = Markers.addMarker(file, message, IMarker.SEVERITY_ERROR, regex);
              if (marker != null)
              {
                marker.setAttribute(Markers.PROBLEM_TYPE, Markers.MAVEN_POM_PROBLEM);
                marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
                marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, componentValue);
                marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.CHECK_MAVEN_POM_ARGUMENT);
                marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_VALUE, "false"); //$NON-NLS-1$
              }
            }
            catch (Exception ex)
            {
              Activator.log(ex);
            }
          }
        });
      }
      catch (SAXException ex)
      {
        if (!"STOP-EARLY".equals(ex.getMessage())) //$NON-NLS-1$
        {
          Activator.log(ex);
        }
      }
      catch (Exception ex)
      {
        Activator.log(ex);
      }
      finally
      {
        IOUtil.closeSilent(contents);
      }
    }
  }

  private boolean addProblem(IElement element, int severity, ComponentReferenceType componentReferenceType, Version version, List<Problem> problems)
  {
    String elementName = element.getName();
    if (!ignoredReferences.contains(elementName))
    {
      problems.add(new Problem(element, severity, componentReferenceType, version));
      return true;
    }

    return false;
  }

  private IProject getProject(IElement element)
  {
    String name = element.getName();
    if (element.getType() == IElement.Type.PLUGIN)
    {
      return getPluginProject(name);
    }

    return getFeatureProject(name);
  }

  private IProject getPluginProject(String name)
  {
    IPluginModelBase pluginModel = PluginRegistry.findModel(name);
    if (pluginModel != null)
    {
      IResource resource = pluginModel.getUnderlyingResource();
      if (resource != null)
      {
        return resource.getProject();
      }
    }

    return null;
  }

  @SuppressWarnings("restriction")
  private IProject getFeatureProject(String name)
  {
    org.eclipse.pde.internal.core.ifeature.IFeatureModel[] featureModels = org.eclipse.pde.internal.core.PDECore.getDefault().getFeatureModelManager()
        .getWorkspaceModels();

    for (org.eclipse.pde.internal.core.ifeature.IFeatureModel featureModel : featureModels)
    {
      IResource resource = featureModel.getUnderlyingResource();
      if (resource != null && name.equals(featureModel.getFeature().getId()))
      {
        return resource.getProject();
      }
    }

    return null;
  }

  private boolean checkMalformedVersions(IModel componentModel) throws CoreException
  {
    IResource underlyingResource = componentModel.getUnderlyingResource();
    if (underlyingResource != null)
    {
      Markers.deleteAllMarkers(underlyingResource, Markers.MALFORMED_VERSION_PROBLEM);

      IProject project = underlyingResource.getProject();
      if (project.isAccessible())
      {
        IFile file = null;
        String regex = null;
        IElement.Type type = VersionUtil.getType(componentModel);
        if (type == IElement.Type.PLUGIN)
        {
          file = project.getFile(MANIFEST_PATH);
          regex = "Bundle-Version: *(\\d+(\\.\\d+(\\.\\d+(\\.[-_a-zA-Z0-9]+)?)?)?)"; //$NON-NLS-1$
        }
        else if (type == IElement.Type.FEATURE)
        {
          file = project.getFile(FEATURE_PATH);
          regex = "feature.*?version\\s*=\\s*[\"'](\\d+(\\.\\d+(\\.\\d+(\\.[-_a-zA-Z0-9]+)?)?)?)"; //$NON-NLS-1$
        }
        else if (type == IElement.Type.PRODUCT)
        {
          file = (IFile)underlyingResource;
          regex = "product.*?version\\s*=\\s*[\"'](\\d+(\\.\\d+(\\.\\d+(\\.[-_a-zA-Z0-9]+)?)?)?)"; //$NON-NLS-1$
        }

        if (file.exists())
        {
          try
          {
            String content = VersionUtil.getContents(file);
            Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
            Matcher matcher = pattern.matcher(content);
            if (matcher.find())
            {
              String version = matcher.group(1);
              if (matcher.groupCount() < 4 || !".qualifier".equals(matcher.group(4))) //$NON-NLS-1$
              {
                Version expectedVersion = new Version(version);
                addMalformedVersionMarker(file, regex,
                    new Version(expectedVersion.getMajor(), expectedVersion.getMinor(), expectedVersion.getMicro(), "qualifier")); //$NON-NLS-1$
                return true;
              }
            }
          }
          catch (Exception ex)
          {
            Activator.log(ex);
          }
        }
      }
    }

    return false;
  }

  private void checkDependencyRanges(IPluginModelBase pluginModel) throws CoreException, IOException
  {
    BundleDescription description = pluginModel.getBundleDescription();
    if (description == null)
    {
      return;
    }

    IFile file = getProject().getFile(MANIFEST_PATH);
    Markers.deleteAllMarkers(file, Markers.DEPENDENCY_RANGE_PROBLEM);

    for (BundleSpecification requiredBundle : description.getRequiredBundles())
    {
      VersionRange range = requiredBundle.getVersionRange();
      if (isUnspecified(getMaximum(range)))
      {
        VersionRange expectedVersionRange = null;
        boolean missing;
        if (VersionRange.emptyRange.equals(range))
        {
          missing = true;
          String name = requiredBundle.getName();
          for (BundleDescription bundleDescription : description.getResolvedRequires())
          {
            if (name.equals(bundleDescription.getSymbolicName()))
            {
              expectedVersionRange = createVersionRange(bundleDescription.getVersion());
              break;
            }
          }
        }
        else
        {
          missing = false;
          Version minVersion = range.getMinimum();
          Version maxVersion = new Version(minVersion.getMajor() + 1, 0, 0);
          expectedVersionRange = new VersionRange(minVersion, range.getIncludeMinimum(), maxVersion, false);
        }

        addRequireMarker(file, requiredBundle.getName(), Messages.VersionBuilder_VersionRangeRequired_message, missing, expectedVersionRange);
      }
      else
      {
        if (!range.getIncludeMinimum())
        {
          VersionRange expectedVersionRange = new VersionRange(range.getMinimum(), true, getMaximum(range), false);
          addRequireMarker(file, requiredBundle.getName(), Messages.VersionBuilder_DependencyRangeMustIncludeMinimum_message, false, expectedVersionRange);
        }

        if (range.getIncludeMaximum())
        {
          VersionRange expectedVersionRange = new VersionRange(range.getMinimum(), true, getMaximum(range), false);
          addRequireMarker(file, requiredBundle.getName(), Messages.VersionBuilder_DependencyRangeMustNotIncludeMaximum_message, false, expectedVersionRange);
        }
      }
    }

    LOOP: for (ImportPackageSpecification importPackage : description.getImportPackages())
    {
      VersionRange range = importPackage.getVersionRange();
      if (isUnspecified(getMaximum(range)))
      {
        VersionRange expectedVersionRange = null;
        boolean missing;
        if (VersionRange.emptyRange.equals(range))
        {
          // Don't report a problem if the package import resolves to a package with the empty version.
          for (ExportPackageDescription exportPackageDescription : description.getResolvedImports())
          {
            if (exportPackageDescription.getName().equals(importPackage.getName()))
            {
              Version version = exportPackageDescription.getVersion();
              if (Version.emptyVersion.equals(version))
              {
                continue LOOP;
              }

              expectedVersionRange = createVersionRange(version);

              break;
            }
          }
          missing = true;
        }
        else
        {
          missing = false;
          Version minVersion = range.getMinimum();
          Version maxVersion = new Version(minVersion.getMajor() + 1, 0, 0);
          expectedVersionRange = new VersionRange(minVersion, range.getIncludeMinimum(), maxVersion, false);
        }

        addImportMarker(file, importPackage.getName(), Messages.VersionBuilder_VersionRangeRequired_message, missing, expectedVersionRange);
      }
      else
      {
        if (!range.getIncludeMinimum())
        {
          VersionRange expectedVersionRange = new VersionRange(range.getMinimum(), true, getMaximum(range), false);
          addImportMarker(file, importPackage.getName(), Messages.VersionBuilder_DependencyRangeMustIncludeMinimum_message, false, expectedVersionRange);
        }

        if (range.getIncludeMaximum())
        {
          VersionRange expectedVersionRange = new VersionRange(range.getMinimum(), true, getMaximum(range), false);
          addImportMarker(file, importPackage.getName(), Messages.VersionBuilder_DependencyRangeMustNotIncludeMaximum_message, false, expectedVersionRange);
        }
      }
    }
  }

  private boolean checkLowerBoundVersions(IPluginModelBase pluginModel, LaxLowerBoundCheckMode laxLowerBoundCheckMode) throws CoreException, IOException
  {
    boolean result = true;

    BundleDescription description = pluginModel.getBundleDescription();
    if (description != null)
    {
      IFile file = getProject().getFile(MANIFEST_PATH);
      Markers.deleteAllMarkers(file, Markers.LOWER_BOUND_VERSION_PROBLEM);

      String releasePath = arguments.getReleasePath();
      for (BundleSpecification requiredBundle : description.getRequiredBundles())
      {
        if (!checkLowerBoundVersions(file, releasePath, requiredBundle, "bundle-version", laxLowerBoundCheckMode)) //$NON-NLS-1$
        {
          result = false;
        }
      }

      for (ImportPackageSpecification importPackage : description.getImportPackages())
      {
        if (!checkLowerBoundVersions(file, releasePath, importPackage, "version", laxLowerBoundCheckMode)) //$NON-NLS-1$
        {
          result = false;
        }
      }
    }

    return result;
  }

  private boolean checkLowerBoundVersions(IFile file, String releasePath, VersionConstraint versionConstraint, String versionAttribute,
      LaxLowerBoundCheckMode laxLowerBoundCheckMode)
  {
    VersionRange requiredVersionRange = versionConstraint.getVersionRange();
    if (!VersionRange.emptyRange.equals(requiredVersionRange))
    {
      BaseDescription supplier = versionConstraint.getSupplier();
      if (supplier != null)
      {
        Version version = supplier.getVersion();
        version = new Version(version.getMajor(), version.getMinor(), 0);
        Version minimumRequiredVersion = requiredVersionRange.getMinimum();
        if (!minimumRequiredVersion.equals(version))
        {
          BundleDescription supplierBundle = supplier.getSupplier();
          if (supplierBundle != null)
          {
            IElement element = resolveElement(new Element(Type.PLUGIN, supplierBundle.getName(), null, supplierBundle.getHost() != null));
            if (element != null)
            {
              IModel componentModel = ReleaseManager.INSTANCE.getComponentModel(element.trimVersion());
              if (componentModel != null)
              {
                IResource underlyingResource = componentModel.getUnderlyingResource();
                if (underlyingResource != null && underlyingResource.isAccessible())
                {
                  IProject project = underlyingResource.getProject();
                  VersionBuilderArguments versionBuilderArguments = new VersionBuilderArguments(project);
                  String elementReleasePath = versionBuilderArguments.getReleasePath();
                  if (releasePath.equals(elementReleasePath) || laxLowerBoundCheckMode == LaxLowerBoundCheckMode.ANY_RELEASE && elementReleasePath != null
                      || laxLowerBoundCheckMode == LaxLowerBoundCheckMode.ALL)
                  {
                    String name = versionConstraint.getName();
                    VersionRange expectedVersionRange = new VersionRange(version, true, getMaximum(requiredVersionRange),
                        requiredVersionRange.getIncludeMaximum());
                    addLaxLowerBoundMarker(file, name, NLS.bind(Messages.VersionBuilder_DependencyRangeMin_message, version), expectedVersionRange,
                        versionAttribute);
                    return false;
                  }
                }
              }
            }
            else if (laxLowerBoundCheckMode == LaxLowerBoundCheckMode.ALL)
            {
              String name = versionConstraint.getName();
              VersionRange expectedVersionRange = new VersionRange(version, true, getMaximum(requiredVersionRange), requiredVersionRange.getIncludeMaximum());
              addLaxLowerBoundMarker(file, name, NLS.bind(Messages.VersionBuilder_DependencyRangeMin_message, version), expectedVersionRange, versionAttribute);
              return false;
            }
          }
        }
      }
    }

    return true;
  }

  @SuppressWarnings("deprecation")
  private Version getMaximum(VersionRange range)
  {
    DeprecationUtil.someDeprecatedCode(); // Just make sure that this method refers to some deprecated code
    return range.getMaximum();
  }

  private boolean isUnspecified(Version version)
  {
    if (version.getMajor() != Integer.MAX_VALUE)
    {
      return false;
    }

    if (version.getMinor() != Integer.MAX_VALUE)
    {
      return false;
    }

    if (version.getMicro() != Integer.MAX_VALUE)
    {
      return false;
    }

    return true;
  }

  private void checkPackageExports(IPluginModelBase pluginModel) throws CoreException, IOException
  {
    IFile file = getProject().getFile(MANIFEST_PATH);
    Markers.deleteAllMarkers(file, Markers.EXPORT_VERSION_PROBLEM);

    BundleDescription description = pluginModel.getBundleDescription();
    Version bundleVersion = VersionUtil.normalize(description.getVersion());

    Set<IPath> paths = new HashSet<>();
    Set<String> prefixes = new HashSet<>();
    getProject().accept(it -> {
      if (it instanceof IFile)
      {
        IPath path = it.getProjectRelativePath();
        if ("java".equals(path.getFileExtension())) //$NON-NLS-1$
        {
          IPath javaPackageFolder = path.removeLastSegments(1);
          if (paths.add(javaPackageFolder))
          {
            prefixes.add(javaPackageFolder.toString().replace('/', '.'));
          }
        }
      }
      return true;
    });

    for (ExportPackageDescription packageExport : description.getExportPackages())
    {
      String packageName = packageExport.getName();
      String suffix = '.' + packageName;
      boolean isBundlePackage = prefixes.stream().anyMatch(it -> it.endsWith(suffix));
      if (isBundlePackage)
      {
        Version packageVersion = packageExport.getVersion();
        if (!packageVersion.equals(bundleVersion))
        {
          addExportMarker(file, packageName, packageVersion, bundleVersion);
        }
      }
    }
  }

  private void checkFeatureNature(IFile file) throws CoreException, IOException
  {
    IProject project = file.getProject();
    Markers.deleteAllMarkers(project, Markers.FEATURE_NATURE_PROBLEM);
    IProjectDescription description = project.getDescription();

    // Check that FeatureBuilder is configured.
    for (ICommand command : description.getBuildSpec())
    {
      if ("org.eclipse.pde.FeatureBuilder".equals(command.getBuilderName())) //$NON-NLS-1$
      {
        if (project.hasNature("org.eclipse.pde.FeatureNature")) //$NON-NLS-1$
        {
          return;
        }
      }
    }

    String regex = "<buildSpec\\s*>()"; //$NON-NLS-1$
    String msg = Messages.VersionBuilder_FeatureBuilderMissing_message;
    IMarker marker = Markers.addMarker(file, msg, IMarker.SEVERITY_WARNING, regex);
    marker.setAttribute(Markers.PROBLEM_TYPE, Markers.FEATURE_NATURE_PROBLEM);
    marker.setAttribute(Markers.QUICK_FIX_NATURE, "org.eclipse.pde.FeatureNature"); //$NON-NLS-1$
    marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_FEATURE_NATURE_ARGUMENT);
  }

  private void checkSchemaBuilder(IPluginModelBase pluginModel, IFile file) throws CoreException, IOException
  {
    Markers.deleteAllMarkers(file, Markers.SCHEMA_BUILDER_PROBLEM);
    IProjectDescription description = getProject().getDescription();

    IPluginBase pluginBase = pluginModel.getPluginBase();
    if (pluginBase != null)
    {
      IPluginExtensionPoint[] extensionPoints = pluginBase.getExtensionPoints();
      if (extensionPoints != null & extensionPoints.length != 0)
      {
        // Plugin has an extension point. Check that SchemaBuilder is configured.
        for (ICommand command : description.getBuildSpec())
        {
          if ("org.eclipse.pde.SchemaBuilder".equals(command.getBuilderName())) //$NON-NLS-1$
          {
            return;
          }
        }

        String regex = "<buildCommand\\s*>\\s*<name>\\s*org.eclipse.pde.ManifestBuilder\\s*</name>.*?</buildCommand>(\\s*)"; //$NON-NLS-1$
        String msg = Messages.VersionBuilder_SchemaBuilderMissing_message;

        IMarker marker = Markers.addMarker(file, msg, IMarker.SEVERITY_WARNING, regex);
        marker.setAttribute(Markers.PROBLEM_TYPE, Markers.SCHEMA_BUILDER_PROBLEM);
        marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
        String lineDelimiter = VersionUtil.getLineDelimiter(file);
        marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT,
            lineDelimiter + "\t\t<buildCommand>" + lineDelimiter + "\t\t\t<name>org.eclipse.pde.SchemaBuilder</name>" + lineDelimiter + "\t\t\t<arguments>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + lineDelimiter + "\t\t\t</arguments>" + lineDelimiter + "\t\t</buildCommand>" + lineDelimiter + "\t\t"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_SCHEMA_BUILDER_ARGUMENT);
        return;
      }
    }

    // Plugin has no extension point(s). Check that SchemaBuilder is not configured.
    for (ICommand command : description.getBuildSpec())
    {
      if ("org.eclipse.pde.SchemaBuilder".equals(command.getBuilderName())) //$NON-NLS-1$
      {
        String regex = "(<buildCommand\\s*>\\s*<name>\\s*org.eclipse.pde.SchemaBuilder\\s*</name>.*?</buildCommand>)\\s*"; //$NON-NLS-1$
        String msg = Messages.VersionBuilder_NoSchemaBuilderNeed_message;

        IMarker marker = Markers.addMarker(file, msg, IMarker.SEVERITY_WARNING, regex);
        marker.setAttribute(Markers.PROBLEM_TYPE, Markers.SCHEMA_BUILDER_PROBLEM);
        marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
        marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_SCHEMA_BUILDER_ARGUMENT);
        break;
      }
    }
  }

  private void checkDebugOptions(IPluginModelBase pluginModel) throws CoreException, IOException
  {
    IFile file = getProject().getFile(OPTIONS_PATH);
    if (file.isAccessible())
    {
      Markers.deleteAllMarkers(file, Markers.DEBUG_OPTION_PROBLEM);

      String symbolicName = pluginModel.getBundleDescription().getSymbolicName();
      String content = VersionUtil.getContents(file);

      Matcher matcher = DEBUG_OPTION_PATTERN.matcher(content);
      while (matcher.find())
      {
        String pluginID = matcher.group(2);
        if (!symbolicName.equals(pluginID))
        {
          String prefix = matcher.group(1);
          String suffix = "/" + (matcher.group(3) + matcher.group(4)).replace(".", "\\."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          pluginID = pluginID.replace(".", "\\."); //$NON-NLS-1$ //$NON-NLS-2$

          String regex = prefix + "(" + pluginID + ")" + suffix; //$NON-NLS-1$ //$NON-NLS-2$
          String msg = NLS.bind(Messages.VersionBuilder_DebugOption_message, symbolicName, matcher.group(3));

          IMarker marker = Markers.addMarker(file, msg, IMarker.SEVERITY_ERROR, regex);
          marker.setAttribute(Markers.PROBLEM_TYPE, Markers.DEBUG_OPTION_PROBLEM);
          marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
          marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, symbolicName);
          marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_DEBUG_OPTIONS_ARGUMENT);
        }
      }
    }
  }

  @SuppressWarnings("restriction")
  private void checkAutomaticModuleName(IPluginModelBase pluginModel) throws CoreException, IOException
  {
    IFile file = getProject().getFile(MANIFEST_PATH);
    if (file.isAccessible())
    {
      Markers.deleteAllMarkers(file, Markers.AUTOMATIC_MODULE_NAME_PROBLEM);

      if (pluginModel instanceof org.eclipse.pde.internal.core.ibundle.IBundlePluginModelBase)
      {
        org.eclipse.pde.internal.core.ibundle.IBundleModel bundleModel = ((org.eclipse.pde.internal.core.ibundle.IBundlePluginModelBase)pluginModel)
            .getBundleModel();
        if (bundleModel != null)
        {
          org.eclipse.pde.internal.core.ibundle.IBundle bundle = bundleModel.getBundle();
          if (bundle != null)
          {
            String header = bundle.getHeader(AUTOMATIC_MODULE_NAME_HEADER);
            if (header != null)
            {
              String symbolicName = pluginModel.getBundleDescription().getSymbolicName();
              if (!header.equals(symbolicName))
              {
                String content = VersionUtil.getContents(file);

                Matcher matcher = AUTOMATIC_MODULE_NAME_PATTERN.matcher(content);
                while (matcher.find())
                {
                  String moduleName = matcher.group(2);
                  if (!symbolicName.equals(moduleName))
                  {
                    String whitespace = matcher.group(1);
                    moduleName = moduleName.replace(".", "\\."); //$NON-NLS-1$ //$NON-NLS-2$

                    String regex = AUTOMATIC_MODULE_NAME_HEADER + ":" + whitespace + "(" + moduleName + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    String msg = NLS.bind(Messages.VersionBuilder_AutomaticModule_message, symbolicName);

                    IMarker marker = Markers.addMarker(file, msg, IMarker.SEVERITY_ERROR, regex);
                    marker.setAttribute(Markers.PROBLEM_TYPE, Markers.AUTOMATIC_MODULE_NAME_PROBLEM);
                    marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
                    marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, symbolicName);
                    marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_AUTOMATIC_MODULE_NAME_ARGUMENT);
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  private void addRequireMarker(IFile file, String name, String message, boolean missing, VersionRange versionRange)
  {
    try
    {
      String regex = missing ? "^Require-Bundle: (?:\r?\n |[^\r\n])*" + name.replace(".", "\\.") + "()(;[^,]*?)?(,|$)" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
          : name.replace(".", "\\.") + ";bundle-version=\"([^\\\"]*)\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

      IMarker marker = Markers.addMarker(file, "'" + name + "' " + message, IMarker.SEVERITY_ERROR, regex); //$NON-NLS-1$ //$NON-NLS-2$
      if (marker != null)
      {
        marker.setAttribute(Markers.PROBLEM_TYPE, Markers.DEPENDENCY_RANGE_PROBLEM);
        marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_DEPENDENCY_RANGES_ARGUMENT);

        if (versionRange != null)
        {
          marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
          marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, missing ? ";bundle-version=\"" + versionRange.toString() + '"' : versionRange.toString()); //$NON-NLS-1$
        }
      }
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }
  }

  private VersionRange createVersionRange(Version version)
  {
    Version minVersion = new Version(version.getMajor(), version.getMinor(), 0);
    Version maxVersion = new Version(version.getMajor() + 1, 0, 0);
    VersionRange versionRange = new VersionRange(minVersion, true, maxVersion, false);
    return versionRange;
  }

  private void addLaxLowerBoundMarker(IFile file, String name, String message, VersionRange versionRange, String versionAttribute)
  {
    try
    {
      String regex = name.replace(".", "\\.") + ";" + versionAttribute + "=\"([^\\\"]*)\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

      IMarker marker = Markers.addMarker(file, "'" + name + "' " + message, IMarker.SEVERITY_ERROR, regex); //$NON-NLS-1$ //$NON-NLS-2$
      if (marker != null)
      {
        marker.setAttribute(Markers.PROBLEM_TYPE, Markers.LOWER_BOUND_VERSION_PROBLEM);
        marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
        marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, versionRange.toString());
        marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_LAX_LOWER_BOUND_VERSIONS_ARGUMENT);
      }
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }
  }

  private void addImportMarker(IFile file, String name, String message, boolean missing, VersionRange versionRange)
  {
    try
    {
      String regex = missing ? "^Import-Package: (?:\r?\n |[^\r\n])*" + name.replace(".", "\\.") + "()(;[^,]*?)?(,|$)" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
          : name.replace(".", "\\.") + ";version=\"([^\\\"]*)\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

      IMarker marker = Markers.addMarker(file, "'" + name + "' " + message, IMarker.SEVERITY_ERROR, regex); //$NON-NLS-1$ //$NON-NLS-2$
      if (marker != null)
      {
        marker.setAttribute(Markers.PROBLEM_TYPE, Markers.DEPENDENCY_RANGE_PROBLEM);
        marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_DEPENDENCY_RANGES_ARGUMENT);

        if (versionRange != null)
        {
          marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
          marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, missing ? ";version=\"" + versionRange.toString() + '"' : versionRange.toString()); //$NON-NLS-1$
        }
      }
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }
  }

  private void addExportMarker(IFile file, String name, Version packageVersion, Version bundleVersion)
  {
    String versionString = bundleVersion.toString();

    try
    {
      String message = NLS.bind(Messages.VersionBuilder_PackageRequiresVersion_message, name, versionString);
      boolean exists = !packageVersion.equals(Version.emptyVersion);
      @SuppressWarnings("nls")
      String regex = !exists ? //
          "(?:\nExport-Package: |\n )" + name.replace(".", "\\.") + "()[;,\r\n]" : //
          name.replace(".", "\\.") + ";version=\"([0123456789\\.]*)\"";

      IMarker marker = Markers.addMarker(file, message, IMarker.SEVERITY_ERROR, regex);
      marker.setAttribute(Markers.PROBLEM_TYPE, Markers.EXPORT_VERSION_PROBLEM);
      marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
      marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, exists ? versionString : ";version=\"" + bundleVersion + "\""); //$NON-NLS-1$ //$NON-NLS-2$
      marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_EXPORT_VERSIONS_ARGUMENT);
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }
  }

  private void addMalformedVersionMarker(IFile file, String regex, Version version)
  {
    try
    {
      String versionString = version.toString();
      IMarker marker = Markers.addMarker(file, NLS.bind(Messages.VersionBuilder_BadVersion_message, versionString), IMarker.SEVERITY_ERROR, regex);
      marker.setAttribute(Markers.PROBLEM_TYPE, Markers.MALFORMED_VERSION_PROBLEM);
      marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
      marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, versionString);
      marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_MALFORMED_VERSIONS_ARGUMENT);
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }
  }

  private IMarker addUnreferencedElementMarker(IFile file, IElement element)
  {
    try
    {
      String type = element.getType().toString();

      String message = NLS.bind(Messages.VersionBuilder_NotReferenceByFeature_message, type, element.getName());
      String regex;
      if (file.getFullPath().lastSegment().equals("MANIFEST.MF")) //$NON-NLS-1$
      {
        regex = "Bundle-SymbolicName: *([^;\n\r]*)"; //$NON-NLS-1$
      }
      else
      {
        regex = "feature.*?id\\s*=\\s*[\"']([^\"']*)"; //$NON-NLS-1$
      }

      IMarker marker = Markers.addMarker(file, message, IMarker.SEVERITY_ERROR, regex);
      if (marker != null)
      {
        marker.setAttribute(Markers.PROBLEM_TYPE, Markers.UNREFERENCED_ELEMENT_PROBLEM);
      }

      return marker;
    }
    catch (Exception ex)
    {
      Activator.log(ex);
      return null;
    }
  }

  private IMarker addDeviationMarker(IFile file, IElement element, Version releasedVersion)
  {
    try
    {
      String type = element.getType().toString();
      Version version = element.getVersion();
      String message = NLS.bind(Messages.VersionBuilder_HasBeenChanged_message, new Object[] { type, element.getName(), releasedVersion, version });
      IMarker marker = addVersionMarker(file, message, version, IMarker.SEVERITY_INFO);
      if (marker != null)
      {
        marker.setAttribute(Markers.PROBLEM_TYPE, Markers.DEVIATION_INFO);
      }

      return marker;
    }
    catch (Exception ex)
    {
      Activator.log(ex);
      return null;
    }
  }

  private IMarker addVersionMarker(IFile file, String message, Version version)
  {
    try
    {
      IMarker marker = addVersionMarker(file, message, version, IMarker.SEVERITY_ERROR);
      if (marker != null)
      {
        marker.setAttribute(Markers.PROBLEM_TYPE, Markers.COMPONENT_VERSION_PROBLEM);
      }

      return marker;
    }
    catch (Exception ex)
    {
      Activator.log(ex);
      return null;
    }
  }

  private IMarker addVersionMarker(IFile file, String message, Version version, int severity)
  {
    try
    {
      String regex;
      if (file.getFullPath().lastSegment().equals("MANIFEST.MF")) //$NON-NLS-1$
      {
        regex = "Bundle-Version: *(\\d+(\\.\\d+(\\.\\d+)?)?)"; //$NON-NLS-1$
      }
      else if (!file.getName().endsWith(".product")) //$NON-NLS-1$
      {
        regex = "feature.*?version\\s*=\\s*[\"'](\\d+(\\.\\d+(\\.\\d+)?)?)"; //$NON-NLS-1$
      }
      else
      {
        regex = "product.*?version\\s*=\\s*[\"'](\\d+(\\.\\d+(\\.\\d+)?)?)"; //$NON-NLS-1$
      }

      IMarker marker = Markers.addMarker(file, message, severity, regex);
      if (severity != IMarker.SEVERITY_INFO)
      {
        marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
        marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, version.toString());
      }

      return marker;
    }
    catch (Exception ex)
    {
      Activator.log(ex);
      return null;
    }
  }

  private void addRedundancyMarker(IFile file, IElement child, IElement feature)
  {
    try
    {
      String name = child.getName();
      Type type = child.getType();

      String cause = feature != null ? NLS.bind(Messages.VersionBuilder_AlredyIncluded_message, feature.getName())
          : Messages.VersionBuilder_RedundantInclude_message;
      String msg = NLS.bind(Messages.VersionBuilder_RedundantReference_message, new Object[] { type, name, cause });

      IMarker marker = addFeatureChildMarker(file, Markers.COMPONENT_VERSION_PROBLEM, type.getTag(), name, msg, false, true, null, IMarker.SEVERITY_WARNING);
      marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_CONTENT_REDUNDANCY_ARGUMENT);
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }
  }

  private void addFeatureClosureMarker(IFile file, IElement pluginChild, IElement featureChild)
  {
    try
    {
      String pluginName = pluginChild.getName();
      String featureName = featureChild.getName();
      String msg = NLS.bind(Messages.VersionBuilder_MissingPlugin_message, pluginName, featureName);

      String lineDelimiter = VersionUtil.getLineDelimiter(file);

      IMarker marker = addFeatureChildMarker(file, Markers.FEATURE_CLOSURE_PROBLEM, "includes", featureName, msg, false, true, null, IMarker.SEVERITY_WARNING); //$NON-NLS-1$
      marker.setAttribute(Markers.QUICK_FIX_PATTERN, "(([ \t]*)<includes(\\s*)id\\s*=\\s*\"" + featureName + "\")"); //$NON-NLS-1$ //$NON-NLS-2$
      marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, "\\2<plugin\\3id=\"" + pluginName //$NON-NLS-1$
          + "\"\\3download-size=\"0\"\\3install-size=\"0\"\\3version=\"0.0.0\"\\3unpack=\"false\"/>" + lineDelimiter + lineDelimiter + "\\1"); //$NON-NLS-1$ //$NON-NLS-2$
      marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.CHECK_CLOSURE_COMPLETENESS_ARGUMENT);
      marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_VALUE, "false"); //$NON-NLS-1$
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }
  }

  private void addIncludeMarker(IFile file, IElement element, int severity, Version version, ComponentReferenceType componentReferenceType)
  {
    try
    {
      String label;
      String tag;
      if (element.getType() == IElement.Type.PLUGIN)
      {
        label = Messages.VersionBuilder_Plugin_label;
        tag = "plugin"; //$NON-NLS-1$
      }
      else if (file.getName().endsWith(".product")) //$NON-NLS-1$
      {
        label = Messages.VersionBuilder_Feature_label;
        tag = "feature"; //$NON-NLS-1$
      }
      else
      {
        label = Messages.VersionBuilder_FeatureLabel;
        tag = "includes"; //$NON-NLS-1$
      }

      String name = element.getName();

      if (componentReferenceType == ComponentReferenceType.REMOVED || componentReferenceType == ComponentReferenceType.FRAGMENT_REMOVED)
      {
        String msg = NLS.bind(Messages.VersionBuilder_Remove_message, label, name);
        IMarker marker = Markers.addMarker(file, msg, severity);
        marker.setAttribute(Markers.PROBLEM_TYPE, Markers.COMPONENT_VERSION_PROBLEM);
        marker.setAttribute(Markers.QUICK_FIX_REFERENCE, name);
      }
      else
      {
        String msg;
        Version replacementVersion = null;
        switch (componentReferenceType)
        {
          case UNRESOLVED:
          {
            if (version == null)
            {
              msg = NLS.bind(Messages.VersionBuilder_Unresolved_message, label, name);
            }
            else
            {
              msg = NLS.bind(Messages.VersionBuilder_ResolvesDifferently_message, new Object[] { label, name, version });
              replacementVersion = version;
            }
            break;
          }
          case ADDED:
          {
            msg = NLS.bind(Messages.VersionBuilder_Added_message, new Object[] { label, name, element.getResolvedVersion() });
            break;
          }
          case MAJOR_CHANGED:
          case MINOR_CHANGED:
          case MICRO_CHANGED:
          {
            msg = NLS.bind(Messages.VersionBuilder_Changed_message, new Object[] { label, name, element.getVersion(), version });
            break;
          }
          default:
          {
            throw new IllegalStateException(Messages.VersionBuilder_Unreachable_message);
          }
        }

        IMarker marker = addFeatureChildMarker(file, Markers.COMPONENT_VERSION_PROBLEM, tag, name, msg, element.isLicenseFeature(),
            componentReferenceType == ComponentReferenceType.ADDED, replacementVersion, severity);
        if (severity == IMarker.SEVERITY_ERROR)
        {
          marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_CONTENT_CHANGES_ARGUMENT);
        }
        else
        {
          marker.setAttribute(Markers.QUICK_FIX_REFERENCE, name);
        }
      }
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }
  }

  private IMarker addFeatureChildMarker(IFile file, String problemType, String tag, String name, String msg, boolean isLicense, boolean hasQuickFix,
      Version version, int severity) throws CoreException, IOException
  {
    String regex = isLicense ? "\\s+.*?license-feature-version\\s*=\\s*[\"']([^\"']*)[\"']" //$NON-NLS-1$
        : version != null ? "[ \\t\\x0B\\f]*<" + tag + "\\s+[^<]*id\\s*=\\s*[\"']" + name.replace(".", "\\.") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            + "[\"'].*?version\\s*=\\s*[\"']([^\"']*)[\"'].*?/>([ \\t\\x0B\\f]*[\\n\\r])*" //$NON-NLS-1$
            : "[ \\t\\x0B\\f]*<" + tag + "\\s+[^<]*id\\s*=\\s*[\"'](" + name.replace(".", "\\.") + ")[\"'].*?/>([ \\t\\x0B\\f]*[\\n\\r])*"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
    IMarker marker = Markers.addMarker(file, msg, severity, regex);
    marker.setAttribute(Markers.PROBLEM_TYPE, problemType);
    if (version != null)
    {
      marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
      marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, version.toString());
      marker.setAttribute(Markers.QUICK_FIX_ALTERNATIVE_REPLACEMENT, "0.0.0"); //$NON-NLS-1$
    }
    else if (hasQuickFix)
    {
      marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
    }

    return marker;
  }

  /**
   * Order of the enumerations is important for determining the "biggest" change.
   * @author Eike Stepper
   */
  private static enum ComponentReferenceType
  {
    UNRESOLVED, UNCHANGED, MICRO_CHANGED, MINOR_CHANGED, ADDED, FRAGMENT_REMOVED, MAJOR_CHANGED, REMOVED
  }

  private static class Problem
  {
    private IElement element;

    private int severity;

    private ComponentReferenceType componentReferenceType;

    private Version version;

    public Problem(IElement element, int severity, ComponentReferenceType componentReferenceType, Version version)
    {
      this.element = element;
      this.severity = severity;
      this.componentReferenceType = componentReferenceType;
      this.version = version;
    }

    public IElement getElement()
    {
      return element;
    }

    public int getSeverity()
    {
      return severity;
    }

    public ComponentReferenceType getComponentReferenceType()
    {
      return componentReferenceType;
    }

    public Version getVersion()
    {
      return version;
    }
  }
}
