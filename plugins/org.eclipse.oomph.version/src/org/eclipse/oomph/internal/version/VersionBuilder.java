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
package org.eclipse.oomph.internal.version;

import org.eclipse.oomph.internal.version.Activator.ReleaseCheckMode;
import org.eclipse.oomph.version.IElement;
import org.eclipse.oomph.version.IElement.Type;
import org.eclipse.oomph.version.IElementResolver;
import org.eclipse.oomph.version.IRelease;
import org.eclipse.oomph.version.IReleaseManager;
import org.eclipse.oomph.version.Markers;
import org.eclipse.oomph.version.VersionUtil;
import org.eclipse.oomph.version.VersionValidator;

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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.eclipse.osgi.service.resolver.BundleSpecification;
import org.eclipse.osgi.service.resolver.ExportPackageDescription;
import org.eclipse.osgi.service.resolver.ImportPackageSpecification;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.pde.core.IModel;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.plugin.IPluginExtensionPoint;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.core.plugin.PluginRegistry;

import org.osgi.framework.Version;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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
  private static final Path DESCRIPTION_PATH = new Path(".project");

  private static final Path OPTIONS_PATH = new Path(".options");

  private static final Path MANIFEST_PATH = new Path("META-INF/MANIFEST.MF");

  private static final Path FEATURE_PATH = new Path("feature.xml");

  public static final String INTEGRATION_PROPERTY_KEY = "baseline.for.integration";

  public static final String DEVIATIONS_PROPERTY_KEY = "show.deviations";

  public static final String ROOT_PROJECTS_KEY = "root.projects";

  public static final String IGNORED_REFERENCES_KEY = "ignored.references";

  private static final Pattern DEBUG_OPTION_PATTERN = Pattern.compile("^( *)([^/ \\n\\r]+)/([^ =]+)( *=.*)$", Pattern.MULTILINE);

  private static IResourceChangeListener postBuildListener;

  private static final Set<String> releasePaths = new HashSet<String>();

  private static final Map<IElement, IElement> elementCache = new HashMap<IElement, IElement>();

  private static final Map<IElement, Set<IElement>> elementReferences = new HashMap<IElement, Set<IElement>>();

  private IRelease release;

  private Boolean integration;

  private Boolean deviations;

  private Set<String> rootProjects;

  private Set<String> ignoredReferences;

  private VersionBuilderArguments arguments;

  public VersionBuilder()
  {
  }

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
    String path = arguments.getReleasePath();
    if (releasePaths.add(path))
    {
      Map<IElement, IElement> elements = IReleaseManager.INSTANCE.createElements(arguments.getReleasePath(), false);
      elementCache.putAll(elements);
      for (IElement element : elements.keySet())
      {
        if (element.getType() == IElement.Type.FEATURE)
        {
          for (IElement child : element.getChildren())
          {
            Set<IElement> references = elementReferences.get(child);
            if (references == null)
            {
              references = new HashSet<IElement>();
              elementReferences.put(child, references);
            }

            references.add(element);
          }
        }
      }
    }
  }

  @Override
  protected void clean(IProgressMonitor monitor) throws CoreException
  {
    IProject project = getProject();

    monitor.beginTask("", 1);
    monitor.subTask("Cleaning versio validity problems of " + project.getName());

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
    List<IProject> buildDpependencies = new ArrayList<IProject>();
    build(kind, args, monitor, buildDpependencies);
    return buildDpependencies.toArray(new IProject[buildDpependencies.size()]);
  }

  private void build(int kind, Map<String, String> args, IProgressMonitor monitor, List<IProject> buildDpependencies) throws CoreException
  {
    arguments = new VersionBuilderArguments(args);
    VersionValidator validator = null;

    IProject project = getProject();
    IFile projectDescription = project.getFile(DESCRIPTION_PATH);

    if (postBuildListener == null)
    {
      postBuildListener = new IResourceChangeListener()
      {
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

    monitor.beginTask("", 1);
    monitor.subTask("Checking version validity of " + project.getName());

    try
    {
      IModel componentModel = VersionUtil.getComponentModel(project);
      IFile componentModelFile = (IFile)componentModel.getUnderlyingResource();

      /*
       * Determine release data to validate against
       */

      String releasePathArg = arguments.getReleasePath();
      if (releasePathArg == null)
      {
        String msg = "Path to release spec file is not configured";
        Markers.addMarker(projectDescription, msg, IMarker.SEVERITY_ERROR, "(" + VersionUtil.BUILDER_ID + ")");
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
        buildDpependencies.add(releaseSpecFile.getProject());

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
      }
      catch (Exception ex)
      {
        Activator.log(ex);
        String msg = "Problem with release spec: " + releasePath + " (" + ex.getMessage() + ")";
        IMarker marker = Markers.addMarker(projectDescription, msg, IMarker.SEVERITY_ERROR, "(" + releasePath.toString().replace(".", "\\.") + ")");
        if (marker != null)
        {
          marker.setAttribute(Markers.PROBLEM_TYPE, Markers.RELEASE_PATH_PROBLEM);
        }

        return;
      }

      IFile propertiesFile = VersionUtil.getFile(releasePath, "properties");
      long propertiesTimeStamp = propertiesFile.getLocalTimeStamp();
      boolean formerDeviations = buildState.isDeviations();
      if (buildState.getPropertiesTimeStamp() != propertiesTimeStamp)
      {
        if (initReleaseProperties(propertiesFile))
        {
          delta = null;
        }

        buildState.setDeviations(deviations);
        buildState.setIntegration(integration);
        buildState.setRootProjects(rootProjects);
        buildState.setIgnoredReferences(ignoredReferences);
        buildState.setPropertiesTimeStamp(propertiesTimeStamp);

        if (formerDeviations && !deviations)
        {
          Markers.deleteAllMarkers(componentModelFile, Markers.DEVIATION_INFO);
        }
      }
      else
      {
        deviations = formerDeviations;
        integration = buildState.isIntegration();
        rootProjects = buildState.getRootProjects();
        ignoredReferences = buildState.getIgnoredReferences();
      }

      IPath componentModelPath = componentModelFile.getProjectRelativePath();
      boolean checkComponentModel = delta == null || delta.findMember(componentModelPath) != null;

      IElement element = IReleaseManager.INSTANCE.createElement(componentModel, true, false);
      for (IElement child : element.getAllChildren(this, release))
      {
        IProject childProject = getProject(child);
        if (childProject != null)
        {
          buildDpependencies.add(childProject);
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

      Markers.deleteAllMarkers(componentModelFile, Markers.UNREFERENCED_ELEMENT_PROBLEM);
      if (Activator.getReleaseCheckMode(releasePathArg) == ReleaseCheckMode.FULL && !rootProjects.contains(project.getName()))
      {
        Set<IElement> elementReference = resolveReferences(element);
        if (elementReference == null || elementReference.isEmpty())
        {
          addUnreferencedElementMarker(componentModelFile, element);
        }
      }

      if (componentModel instanceof IPluginModelBase)
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

        if (!arguments.isIgnoreMissingDependencyRanges())
        {
          if (checkComponentModel)
          {
            checkDependencyRanges((IPluginModelBase)componentModel);
          }
        }
        else if (!oldVersionBuilderArguments.isIgnoreMissingDependencyRanges())
        {
          Markers.deleteAllMarkers(getProject().getFile(MANIFEST_PATH), Markers.DEPENDENCY_RANGE_PROBLEM);
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
          return;
        }
      }
      else
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
            return;
          }
        }
      }
      else if (!oldVersionBuilderArguments.isIgnoreMalformedVersions())
      {
        Markers.deleteAllMarkers(componentModelFile, Markers.MALFORMED_VERSION_PROBLEM);
      }

      IElement releaseElement = release.getElements().get(element.trimVersion());
      if (releaseElement == null)
      {
        if (VersionUtil.DEBUG)
        {
          System.out.println("Project has not been released: " + project.getName());
        }

        return;
      }

      Markers.deleteAllMarkers(componentModelFile, Markers.VERSION_NATURE_PROBLEM);
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
              String msg = label + " '" + name + "' is missing the version management builder";
              IMarker marker = addFeatureChildMarker(componentModelFile, Markers.VERSION_NATURE_PROBLEM, tag, name, msg, child.isLicenseFeature(), false, null,
                  IMarker.SEVERITY_ERROR);
              marker.setAttribute(Markers.QUICK_FIX_NATURE, VersionNature.NATURE_ID);
              marker.setAttribute(Markers.QUICK_FIX_PROJECT, childProject.getName());
            }
          }
        }
      }

      Version elementVersion = element.getVersion();
      Version releaseVersion = releaseElement.getVersion();
      Version nextMicroVersion = getNextMicroVersion(releaseVersion);

      int comparison = releaseVersion.compareTo(elementVersion);
      if (comparison != 0 && deviations && checkComponentModel)
      {
        addDeviationMarker(componentModelFile, element, releaseVersion);
      }

      Markers.deleteAllMarkers(componentModelFile, Markers.COMPONENT_VERSION_PROBLEM);
      boolean microVersionProperlyIncreased = false;

      if (comparison < 0)
      {
        microVersionProperlyIncreased = nextMicroVersion.equals(elementVersion);
        if (!microVersionProperlyIncreased)
        {
          boolean noOtherIncrements = elementVersion.getMajor() == nextMicroVersion.getMajor() && elementVersion.getMinor() == nextMicroVersion.getMinor();
          if (noOtherIncrements)
          {
            addVersionMarker(componentModelFile, "Version should be " + nextMicroVersion, nextMicroVersion);
          }
        }

        if (element.getType() == IElement.Type.PLUGIN)
        {
          return;
        }
      }

      if (comparison > 0)
      {
        addVersionMarker(componentModelFile, "Version has been decreased after release " + releaseVersion, releaseVersion);
        return;
      }

      if (element.getType() == IElement.Type.FEATURE)
      {
        if (!arguments.isIgnoreFeatureContentRedundancy())
        {
          checkFeatureRedundancy(componentModelFile, element);
        }

        if (!arguments.isIgnoreFeatureContentChanges())
        {
          List<Problem> problems = new ArrayList<Problem>();
          checkFeatureReferences(componentModelFile, element, problems);
          if (!problems.isEmpty())
          {
            createMarkers(componentModelFile, problems, ComponentReferenceType.UNRESOLVED);
            return;
          }

          ComponentReferenceType change = checkFeatureContentChanges(element, releaseElement, problems);
          if (change != ComponentReferenceType.UNCHANGED)
          {
            Version nextFeatureVersion = getNextFeatureVersion(releaseVersion, nextMicroVersion, change);
            if (elementVersion.compareTo(nextFeatureVersion) < 0)
            {
              IMarker marker = addVersionMarker(componentModelFile, "Version must be increased to " + nextFeatureVersion
                  + " because the feature's references have changed", nextFeatureVersion);
              if (marker != null)
              {
                marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_CONTENT_CHANGES_ARGUMENT);
              }

              createMarkers(componentModelFile, problems, change);
            }

            return;
          }

          if (!elementVersion.equals(releaseVersion))
          {
            return;
          }
        }
      }

      if (microVersionProperlyIncreased)
      {
        return;
      }

      /*
       * Determine validator to use
       */

      String validatorClassName = arguments.getValidatorClassName();
      if (validatorClassName == null)
      {
        validatorClassName = IVersionBuilderArguments.DEFAULT_VALIDATOR_CLASS_NAME;
      }

      try
      {
        Markers.deleteAllMarkers(projectDescription, Markers.VALIDATOR_CLASS_PROBLEM);

        Class<?> c = Class.forName(validatorClassName, true, VersionBuilder.class.getClassLoader());
        validator = (VersionValidator)c.newInstance();

        if (VersionUtil.DEBUG)
        {
          System.out.println(validator.getClass().getName() + ": " + project.getName());
        }
      }
      catch (Exception ex)
      {
        String msg = ex.getLocalizedMessage() + ": " + validatorClassName;
        IMarker marker = Markers.addMarker(projectDescription, msg, IMarker.SEVERITY_ERROR, ".*(" + validatorClassName + ").*");
        if (marker != null)
        {
          marker.setAttribute(Markers.PROBLEM_TYPE, Markers.VALIDATOR_CLASS_PROBLEM);
        }

        return;
      }

      String validatorVersion = validator.getVersion();
      if (!VersionUtil.equals(validatorClassName, buildState.getValidatorClass()) || !VersionUtil.equals(validatorVersion, buildState.getValidatorVersion()))
      {
        buildState.setValidatorState(null);
        delta = null;
      }

      buildState.setValidatorClass(validatorClassName);
      buildState.setValidatorVersion(validatorVersion);

      /*
       * Do the validation
       */

      validator.updateBuildState(buildState, release, project, delta, componentModel, monitor);

      if (buildState.isChangedSinceRelease())
      {
        addVersionMarker(componentModelFile, "Version must be increased to " + nextMicroVersion + " because the project's contents have changed",
            nextMicroVersion);
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
      release = null;
      monitor.done();
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
        if (marker.getType().startsWith("org.eclipse.pde.api.tools"))
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
        contents = propertiesFile.getContents();

        Properties properties = new Properties();
        properties.load(contents);

        deviations = Boolean.valueOf(properties.getProperty(DEVIATIONS_PROPERTY_KEY, "false"));

        rootProjects = new HashSet<String>();
        for (String rootProject : Arrays.asList(properties.getProperty(ROOT_PROJECTS_KEY, "").split(" ")))
        {
          rootProjects.add(rootProject.replace("\\ ", " ").replace("\\\\", "\\"));
        }

        ignoredReferences = new HashSet<String>();
        for (String ignoredReference : Arrays.asList(properties.getProperty(IGNORED_REFERENCES_KEY, "").split(" ")))
        {
          ignoredReferences.add(ignoredReference.replace("\\ ", " ").replace("\\\\", "\\"));
        }

        Boolean newValue = Boolean.valueOf(properties.getProperty(INTEGRATION_PROPERTY_KEY, "true"));
        if (!newValue.equals(integration))
        {
          integration = newValue;
          return true;
        }

        return false;
      }
      finally
      {
        VersionUtil.close(contents);
      }
    }

    deviations = false;
    integration = true;
    rootProjects = new HashSet<String>();
    ignoredReferences = new HashSet<String>();

    String lineDelimiter = VersionUtil.getLineDelimiter(propertiesFile);
    String contents = INTEGRATION_PROPERTY_KEY + " = " + integration + lineDelimiter + DEVIATIONS_PROPERTY_KEY + " = " + deviations + lineDelimiter
        + ROOT_PROJECTS_KEY + " = ";

    String charsetName = propertiesFile.getCharset();
    byte[] bytes = contents.getBytes(charsetName);

    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    propertiesFile.create(bais, true, new NullProgressMonitor());
    return true;
  }

  private Version getNextMicroVersion(Version releaseVersion)
  {
    return new Version(releaseVersion.getMajor(), releaseVersion.getMinor(), releaseVersion.getMicro() + (integration ? 100 : 1));
  }

  private Version getNextFeatureVersion(Version releaseVersion, Version nextImplementationVersion, ComponentReferenceType change)
  {
    switch (change)
    {
      case REMOVED:
      case MAJOR_CHANGED:
        return new Version(releaseVersion.getMajor() + 1, 0, 0);
      case ADDED:
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
    int i = 0;
    List<IElement> children = element.getChildren();
    for (IElement pluginChild : children)
    {
      if (pluginChild.getType() == IElement.Type.PLUGIN)
      {
        for (IElement featureChild : children)
        {
          if (featureChild.getType() == IElement.Type.FEATURE)
          {
            featureChild = resolveElement(featureChild);
            if (featureChild != null)
            {
              Set<IElement> allChildren = featureChild.getAllChildren(this, release);
              if (allChildren.contains(pluginChild))
              {
                try
                {
                  addRedundancyMarker(file, pluginChild, featureChild);
                }
                catch (Exception ex)
                {
                  Activator.log(ex);
                }
              }
            }
          }
        }

        if (children.indexOf(pluginChild) != i)
        {
          addRedundancyMarker(file, pluginChild, null);
        }
      }

      ++i;
    }
  }

  private ComponentReferenceType checkFeatureContentChanges(IElement element, IElement releasedElement, List<Problem> problems)
  {
    ComponentReferenceType biggestChange = ComponentReferenceType.UNCHANGED;
    Set<IElement> allChildren = element.getAllChildren(this, release);
    for (IElement child : allChildren)
    {
      ComponentReferenceType change = checkFeatureContentChanges(element, releasedElement, child, problems);
      biggestChange = ComponentReferenceType.values()[Math.max(biggestChange.ordinal(), change.ordinal())];
    }

    for (IElement releasedElementsChild : releasedElement.getAllChildren(release, this))
    {
      IElement trimmedVersion = releasedElementsChild.trimVersion();
      if (!allChildren.contains(trimmedVersion))
      {
        // IElement resolvedElement = resolveElement(trimmedVersion);
        // if (resolvedElement != null && !resolvedElement.isVersionUnresolved())
        {
          if (addProblem(releasedElementsChild, IMarker.SEVERITY_WARNING, ComponentReferenceType.REMOVED, null, problems))
          {
            biggestChange = ComponentReferenceType.REMOVED;
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
      if (resource != null && featureModel.getFeature().getId().equals(name))
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
        if (componentModel instanceof IPluginModelBase)
        {
          file = project.getFile(MANIFEST_PATH);
          regex = "Bundle-Version: *(\\d+(\\.\\d+(\\.\\d+(\\.[-_a-zA-Z0-9]+)?)?)?)";
        }
        else
        {
          file = project.getFile(FEATURE_PATH);
          regex = "feature.*?version\\s*=\\s*[\"'](\\d+(\\.\\d+(\\.\\d+(\\.[-_a-zA-Z0-9]+)?)?)?)";
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
              if (matcher.groupCount() < 4 || !".qualifier".equals(matcher.group(4)))
              {
                Version expectedVersion = new Version(version);
                addMalformedVersionMarker(file, regex, new Version(expectedVersion.getMajor(), expectedVersion.getMinor(), expectedVersion.getMicro(),
                    "qualifier"));
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
        addRequireMarker(file, requiredBundle.getName(), "dependency must specify a version range");
      }
      else
      {
        if (!range.getIncludeMinimum())
        {
          addRequireMarker(file, requiredBundle.getName(), "dependency range must include the minimum");
        }

        if (range.getIncludeMaximum())
        {
          addRequireMarker(file, requiredBundle.getName(), "dependency range must not include the maximum");
        }
      }
    }

    for (ImportPackageSpecification importPackage : description.getImportPackages())
    {
      VersionRange range = importPackage.getVersionRange();
      if (isUnspecified(getMaximum(range)))
      {
        addImportMarker(file, importPackage.getName(), "dependency must specify a version range");
      }
      else
      {
        if (!range.getIncludeMinimum())
        {
          addImportMarker(file, importPackage.getName(), "dependency range must include the minimum");
        }

        if (range.getIncludeMaximum())
        {
          addImportMarker(file, importPackage.getName(), "dependency range must not include the maximum");
        }
      }
    }
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
    String bundleName = description.getSymbolicName();
    Version bundleVersion = VersionUtil.normalize(description.getVersion());

    for (ExportPackageDescription packageExport : description.getExportPackages())
    {
      String packageName = packageExport.getName();
      if (isBundlePackage(packageName, bundleName))
      {
        Version packageVersion = packageExport.getVersion();
        if (packageVersion != null && !packageVersion.equals(Version.emptyVersion) && !packageVersion.equals(bundleVersion))
        {
          addExportMarker(file, packageName, bundleVersion);
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
      if ("org.eclipse.pde.FeatureBuilder".equals(command.getBuilderName()))
      {
        if (project.hasNature("org.eclipse.pde.FeatureNature"))
        {
          return;
        }
      }
    }

    String regex = "<buildSpec\\s*>()";
    String msg = "Feature builder is missing";
    IMarker marker = Markers.addMarker(file, msg, IMarker.SEVERITY_WARNING, regex);
    marker.setAttribute(Markers.PROBLEM_TYPE, Markers.FEATURE_NATURE_PROBLEM);
    marker.setAttribute(Markers.QUICK_FIX_NATURE, "org.eclipse.pde.FeatureNature");
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
          if ("org.eclipse.pde.SchemaBuilder".equals(command.getBuilderName()))
          {
            return;
          }
        }

        String regex = "<buildCommand\\s*>\\s*<name>\\s*org.eclipse.pde.ManifestBuilder\\s*</name>.*?</buildCommand>(\\s*)";
        String msg = "Schema builder is missing";

        IMarker marker = Markers.addMarker(file, msg, IMarker.SEVERITY_WARNING, regex);
        marker.setAttribute(Markers.PROBLEM_TYPE, Markers.SCHEMA_BUILDER_PROBLEM);
        marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
        String lineDelimiter = VersionUtil.getLineDelimiter(file);
        marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, lineDelimiter + "\t\t<buildCommand>" + lineDelimiter
            + "\t\t\t<name>org.eclipse.pde.SchemaBuilder</name>" + lineDelimiter + "\t\t\t<arguments>" + lineDelimiter + "\t\t\t</arguments>" + lineDelimiter
            + "\t\t</buildCommand>" + lineDelimiter + "\t\t");
        marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_SCHEMA_BUILDER_ARGUMENT);
        return;
      }
    }

    // Plugin has no extension point(s). Check that SchemaBuilder is not configured.
    for (ICommand command : description.getBuildSpec())
    {
      if ("org.eclipse.pde.SchemaBuilder".equals(command.getBuilderName()))
      {
        String regex = "(<buildCommand\\s*>\\s*<name>\\s*org.eclipse.pde.SchemaBuilder\\s*</name>.*?</buildCommand>)\\s*";
        String msg = "No schema builder is needed because no extension point is declared";

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
          String suffix = "/" + (matcher.group(3) + matcher.group(4)).replace(".", "\\.");
          pluginID = pluginID.replace(".", "\\.");

          String regex = prefix + "(" + pluginID + ")" + suffix;
          String msg = "Debug option should be '" + symbolicName + "/" + matcher.group(3) + "'";

          IMarker marker = Markers.addMarker(file, msg, IMarker.SEVERITY_ERROR, regex);
          marker.setAttribute(Markers.PROBLEM_TYPE, Markers.DEBUG_OPTION_PROBLEM);
          marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
          marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, symbolicName);
          marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_DEBUG_OPTIONS_ARGUMENT);
        }
      }
    }
  }

  private boolean isBundlePackage(String packageName, String bundleName)
  {
    if (packageName.startsWith(bundleName))
    {
      return true;
    }

    int lastDot = bundleName.lastIndexOf('.');
    if (lastDot != -1)
    {
      String bundleStart = bundleName.substring(0, lastDot);
      String bundleEnd = bundleName.substring(lastDot + 1);
      if (packageName.startsWith(bundleStart + ".internal." + bundleEnd))
      {
        return true;
      }

      if (packageName.startsWith(bundleStart + ".spi." + bundleEnd))
      {
        return true;
      }
    }

    return false;
  }

  private void addRequireMarker(IFile file, String name, String message)
  {
    try
    {
      String regex = name.replace(".", "\\.") + ";bundle-version=\"([^\\\"]*)\"";

      IMarker marker = Markers.addMarker(file, "'" + name + "' " + message, IMarker.SEVERITY_ERROR, regex);
      if (marker != null)
      {
        marker.setAttribute(Markers.PROBLEM_TYPE, Markers.DEPENDENCY_RANGE_PROBLEM);
        marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_DEPENDENCY_RANGES_ARGUMENT);
      }
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }
  }

  private void addImportMarker(IFile file, String name, String message)
  {
    try
    {
      String regex = name.replace(".", "\\.") + ";version=\"([^\\\"]*)\"";

      IMarker marker = Markers.addMarker(file, "'" + name + "' " + message, IMarker.SEVERITY_ERROR, regex);
      if (marker != null)
      {
        marker.setAttribute(Markers.PROBLEM_TYPE, Markers.DEPENDENCY_RANGE_PROBLEM);
        marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_DEPENDENCY_RANGES_ARGUMENT);
      }
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }
  }

  private void addExportMarker(IFile file, String name, Version bundleVersion)
  {
    String versionString = bundleVersion.toString();

    try
    {
      String message = "Export of package '" + name + "' should have the version " + versionString;
      String regex = name.replace(".", "\\.") + ";version=\"([0123456789\\.]*)\"";

      IMarker marker = Markers.addMarker(file, message, IMarker.SEVERITY_ERROR, regex);
      marker.setAttribute(Markers.PROBLEM_TYPE, Markers.EXPORT_VERSION_PROBLEM);
      marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
      marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, versionString);
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
      IMarker marker = Markers.addMarker(file, "The version should be of the form '" + versionString + "'", IMarker.SEVERITY_ERROR, regex);
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
      String type;
      if (element.getType() == IElement.Type.PLUGIN)
      {
        type = "Plug-in";
      }
      else
      {
        type = "Feature";
      }

      String message = type + " '" + element.getName() + "' is not referenced by any other feature";
      String regex;
      if (file.getFullPath().lastSegment().equals("MANIFEST.MF"))
      {
        regex = "Bundle-SymbolicName: *([^;\n\r]*)";
      }
      else
      {
        regex = "feature.*?id\\s*=\\s*[\"']([^\"']*)";
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
      String type;
      if (element.getType() == IElement.Type.PLUGIN)
      {
        type = "Plug-in";
      }
      else
      {
        type = "Feature";
      }

      Version version = element.getVersion();
      String message = type + " '" + element.getName() + "' has been changed from " + releasedVersion + " to " + version;
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
      if (file.getFullPath().lastSegment().equals("MANIFEST.MF"))
      {
        regex = "Bundle-Version: *(\\d+(\\.\\d+(\\.\\d+)?)?)";
      }
      else
      {
        regex = "feature.*?version\\s*=\\s*[\"'](\\d+(\\.\\d+(\\.\\d+)?)?)";
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

  private void addRedundancyMarker(IFile file, IElement pluginChild, IElement featureChild)
  {
    try
    {
      String name = pluginChild.getName();
      String cause = featureChild != null ? "feature '" + featureChild.getName() + "' already includes it"
          : " because it occurs more than once in this feature";
      String msg = "Plug-in reference '" + name + "' is redundant because " + cause;

      IMarker marker = addFeatureChildMarker(file, Markers.COMPONENT_VERSION_PROBLEM, "plugin", name, msg, false, true, null, IMarker.SEVERITY_WARNING);
      marker.setAttribute(Markers.QUICK_FIX_CONFIGURE_OPTION, IVersionBuilderArguments.IGNORE_CONTENT_REDUNDANCY_ARGUMENT);
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
        label = "Plug-in";
        tag = "plugin";
      }
      else
      {
        label = "Feature";
        tag = "includes";
      }

      String name = element.getName();

      if (componentReferenceType == ComponentReferenceType.REMOVED)
      {
        String msg = label + " reference '" + name + "' has been removed";
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
              msg = label + " reference '" + name + "' cannot be resolved ";
            }
            else
            {
              msg = label + " reference '" + name + "' resolves to the different version " + version;
              replacementVersion = version;
            }
            break;
          }
          case ADDED:
          {
            msg = label + " reference '" + name + "' has been added with " + element.getResolvedVersion();
            break;
          }
          case MAJOR_CHANGED:
          case MINOR_CHANGED:
          case MICRO_CHANGED:
          {
            msg = label + " reference '" + name + "' has been changed from " + element.getVersion() + " to " + version;
            break;
          }
          default:
          {
            throw new IllegalStateException("This should be unreachable code");
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
    String regex = isLicense ? "\\s+.*?license-feature-version\\s*=\\s*[\"']([^\"']*)[\"']" : version != null ? "[ \\t\\x0B\\f]*<" + tag
        + "\\s+[^<]*id\\s*=\\s*[\"']" + name.replace(".", "\\.") + "[\"'].*?version\\s*=\\s*[\"']([^\"']*)[\"'].*?/>([ \\t\\x0B\\f]*[\\n\\r])*"
        : "[ \\t\\x0B\\f]*<" + tag + "\\s+[^<]*id\\s*=\\s*[\"'](" + name.replace(".", "\\.") + ")[\"'].*?/>([ \\t\\x0B\\f]*[\\n\\r])*";
    IMarker marker = Markers.addMarker(file, msg, severity, regex);
    marker.setAttribute(Markers.PROBLEM_TYPE, problemType);
    if (version != null)
    {
      marker.setAttribute(Markers.QUICK_FIX_PATTERN, regex);
      marker.setAttribute(Markers.QUICK_FIX_REPLACEMENT, version.toString());
      marker.setAttribute(Markers.QUICK_FIX_ALTERNATIVE_REPLACEMENT, "0.0.0");
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
    UNRESOLVED, UNCHANGED, MICRO_CHANGED, MINOR_CHANGED, ADDED, MAJOR_CHANGED, REMOVED
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
