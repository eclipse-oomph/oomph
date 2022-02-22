/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.projectconfig.util;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.projectconfig.PreferenceFilter;
import org.eclipse.oomph.projectconfig.PreferenceProfile;
import org.eclipse.oomph.projectconfig.Project;
import org.eclipse.oomph.projectconfig.ProjectConfigFactory;
import org.eclipse.oomph.projectconfig.WorkspaceConfiguration;
import org.eclipse.oomph.projectconfig.impl.ProjectConfigPlugin;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public final class ProjectConfigUtil
{
  private static final Pattern JDT_CORE_BREE_PATTERN = Pattern.compile("org\\.eclipse\\.jdt\\.core\\.compiler\\.codegen\\.targetPlatform" + "|" //$NON-NLS-1$ //$NON-NLS-2$
      + "org.eclipse\\.jdt\\.core\\.compiler\\.compliance" + "|" + "org\\.eclipse\\.jdt\\.core\\.compiler\\.source"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

  private static final Pattern RESOURCE_ENCODING_PROJECT_PATTERN = Pattern.compile("<project>"); //$NON-NLS-1$

  private static final IWorkspaceRoot WORKSPACE_ROOT = ResourcesPlugin.getWorkspace().getRoot();

  public static final String PROJECT_CONF_NODE_NAME = "org.eclipse.oomph.projectconfig"; //$NON-NLS-1$

  public static final String PROJECT_CONF_PROJECT_KEY = "project"; //$NON-NLS-1$

  public static final String PROJECT_CONFIG_SCHEME = "configuration"; //$NON-NLS-1$

  public static final URI PROJECT_CONFIG_URI = URI.createURI(PROJECT_CONFIG_SCHEME + ":/"); //$NON-NLS-1$

  public static WorkspaceConfiguration getWorkspaceConfiguration()
  {
    return getWorkspaceConfiguration(null);
  }

  public static abstract class CompletenessChecker
  {
    public CompletenessChecker()
    {
      this(getWorkspaceConfiguration());
    }

    public CompletenessChecker(WorkspaceConfiguration workspaceConfiguration)
    {
      final Set<String> projectNames = new HashSet<>();
      for (IProject iProject : WORKSPACE_ROOT.getProjects())
      {
        if (iProject.isAccessible())
        {
          projectNames.add(iProject.getName());
        }
      }

      if (getProjectNames(workspaceConfiguration.getInstancePreferenceNode().getParent().getNode(PreferencesUtil.PROJECT_NODE)).containsAll(projectNames))
      {
        complete(workspaceConfiguration);
      }
      else
      {
        final PreferenceNode projectPreferenceNode = PreferencesUtil.getRootPreferenceNode(Collections.singleton(PreferencesUtil.PROJECT_NODE), true)
            .getNode(PreferencesUtil.PROJECT_NODE);
        Adapter projectNodeListener = new AdapterImpl()
        {
          @Override
          public synchronized void notifyChanged(Notification msg)
          {
            Set<String> projectPreferenceNodeNames = getProjectNames(projectPreferenceNode);
            if (projectPreferenceNodeNames.containsAll(projectNames) && projectPreferenceNode.eAdapters().remove(this))
            {
              projectPreferenceNode.eResource().unload();
              complete();
            }
          }
        };

        projectPreferenceNode.eAdapters().add(projectNodeListener);
      }
    }

    private Set<String> getProjectNames(final PreferenceNode projectPreferenceNode)
    {
      Set<String> projectPreferenceNodeNames = new HashSet<>();
      for (PreferenceNode preferenceNode : projectPreferenceNode.getChildren())
      {
        projectPreferenceNodeNames.add(preferenceNode.getName());
      }
      return projectPreferenceNodeNames;
    }

    protected void complete()
    {
      complete(getWorkspaceConfiguration());
    }

    protected void complete(WorkspaceConfiguration workspaceConfiguration)
    {
    }
  }

  public static EList<PreferenceProfile> getDefaultPreferenceProfiles(IProject project, PreferenceNode projectPreferenceNode)
  {
    EList<PreferenceProfile> result = new BasicEList<>();

    PreferenceNode encodingPreferenceNode = projectPreferenceNode.getNode(URI.createURI("org.eclipse.core.resources/encoding")); //$NON-NLS-1$
    if (encodingPreferenceNode != null)
    {
      PreferenceProfile encodingPreferenceProfile = ProjectConfigFactory.eINSTANCE.createPreferenceProfile();
      encodingPreferenceProfile.setName(Messages.ProjectConfigUtil_CoreResourcesEncoding_label);

      PreferenceFilter encodingPreferenceFilter = ProjectConfigFactory.eINSTANCE.createPreferenceFilter();
      encodingPreferenceFilter.setExclusions(RESOURCE_ENCODING_PROJECT_PATTERN);
      encodingPreferenceFilter.setPreferenceNode(encodingPreferenceNode);
      encodingPreferenceProfile.getPreferenceFilters().add(encodingPreferenceFilter);

      result.add(encodingPreferenceProfile);
    }

    try
    {
      if (project.hasNature("org.eclipse.jdt.core.javanature")) //$NON-NLS-1$
      {
        PreferenceNode jdtCorePreferenceNode = projectPreferenceNode.getNode("org.eclipse.jdt.core"); //$NON-NLS-1$
        if (jdtCorePreferenceNode != null)
        {
          PreferenceProfile jdtCompilerCompliancePreferenceProfile = ProjectConfigFactory.eINSTANCE.createPreferenceProfile();

          jdtCompilerCompliancePreferenceProfile.setName(Messages.ProjectConfigUtil_JDTCoreManaged_label);

          PreferenceFilter jdtCompilerCompliancePreferenceFilter = ProjectConfigFactory.eINSTANCE.createPreferenceFilter();
          jdtCompilerCompliancePreferenceFilter.setInclusions(JDT_CORE_BREE_PATTERN);
          jdtCompilerCompliancePreferenceFilter.setPreferenceNode(jdtCorePreferenceNode);
          jdtCompilerCompliancePreferenceProfile.getPreferenceFilters().add(jdtCompilerCompliancePreferenceFilter);

          result.add(jdtCompilerCompliancePreferenceProfile);
        }
      }
    }
    catch (CoreException ex)
    {
      // Ignore
    }

    return result;
  }

  public static WorkspaceConfiguration getWorkspaceConfiguration(PreferenceNode cachedProjectsPreferenceNode)
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    Resource resource = resourceSet.createResource(URI.createURI("*.projectconfig")); //$NON-NLS-1$
    resource.setURI(PROJECT_CONFIG_URI);

    PreferenceNode rootPreferenceNode = PreferencesUtil.getRootPreferenceNode();

    WorkspaceConfiguration workspaceConfiguration = ProjectConfigFactory.eINSTANCE.createWorkspaceConfiguration();
    PreferenceNode instancePreferenceNode = rootPreferenceNode.getNode(PreferencesUtil.INSTANCE_NODE);
    workspaceConfiguration.setInstancePreferenceNode(instancePreferenceNode);
    workspaceConfiguration.setDefaultPreferenceNode(rootPreferenceNode.getNode(PreferencesUtil.DEFAULT_NODE));

    PreferenceNode projectsPreferenceNode = rootPreferenceNode.getNode(PreferencesUtil.PROJECT_NODE);
    EList<Project> projects = workspaceConfiguration.getProjects();

    for (IProject iProject : WORKSPACE_ROOT.getProjects())
    {
      if (iProject.isAccessible())
      {
        String name = iProject.getName();
        PreferenceNode projectPreferenceNode = projectsPreferenceNode.getNode(name);
        PreferenceNode cachedProjectPreferenceNode = cachedProjectsPreferenceNode == null ? null : cachedProjectsPreferenceNode.getNode(name);
        if (projectPreferenceNode != null && (cachedProjectsPreferenceNode == null || cachedProjectPreferenceNode != null))
        {
          Project project = ProjectConfigFactory.eINSTANCE.createProject();

          PreferenceNode projectConfNode = (cachedProjectPreferenceNode == null ? projectPreferenceNode : cachedProjectPreferenceNode)
              .getNode(PROJECT_CONF_NODE_NAME);
          if (projectConfNode == null)
          {
            project.getPreferenceProfiles().addAll(getDefaultPreferenceProfiles(iProject, projectPreferenceNode));
          }
          else
          {
            Property projectProperty = projectConfNode.getProperty(PROJECT_CONF_PROJECT_KEY);
            if (projectProperty != null)
            {
              String value = projectProperty.getValue();
              if (value != null)
              {
                XMLResourceImpl projectResource = new XMLResourceImpl(PROJECT_CONFIG_URI);
                InputStream in = new URIConverter.ReadableInputStream(value);
                try
                {
                  projectResource.load(in, null);
                }
                catch (IOException ex)
                {
                  // Ignore.
                }

                EList<EObject> contents = projectResource.getContents();
                if (!contents.isEmpty())
                {
                  project = (Project)contents.get(0);
                }
              }
            }
          }

          project.setPreferenceNode(projectPreferenceNode);

          projects.add(project);
        }
      }
    }

    EList<EObject> contents = resource.getContents();
    contents.add(workspaceConfiguration);
    contents.add(rootPreferenceNode);

    for (Project project : projects)
    {
      for (PreferenceProfile preferenceProfile : project.getPreferenceProfiles())
      {
        for (PreferenceFilter preferenceFilter : preferenceProfile.getPreferenceFilters())
        {
          preferenceFilter.getPreferenceNode();
        }
      }

      EList<PreferenceProfile> profileReferences = project.getPreferenceProfileReferences();
      ArrayList<PreferenceProfile> copy = new ArrayList<>(profileReferences);
      profileReferences.clear();
      profileReferences.addAll(copy);
    }

    workspaceConfiguration.updatePreferenceProfileReferences();

    return workspaceConfiguration;
  }

  public static final void saveWorkspaceConfiguration(WorkspaceConfiguration workspaceConfiguration) throws BackingStoreException
  {
    saveWorkspaceConfiguration(workspaceConfiguration, false);
  }

  public static final PreferenceNode cacheWorkspaceConfiguration(WorkspaceConfiguration workspaceConfiguration)
  {
    try
    {
      return saveWorkspaceConfiguration(workspaceConfiguration, true);
    }
    catch (BackingStoreException ex)
    {
      // Can't occur when only caching.
      return null;
    }
  }

  private static final PreferenceNode saveWorkspaceConfiguration(WorkspaceConfiguration workspaceConfiguration, boolean cache) throws BackingStoreException
  {
    PreferenceNode projectsPreferenceNode = null;

    for (Project project : workspaceConfiguration.getProjects())
    {
      PreferenceNode projectPreferenceNode = project.getPreferenceNode();
      Preferences projectPreferences = cache ? null : PreferencesUtil.getPreferences(projectPreferenceNode, true);
      EList<PreferenceProfile> preferenceProfiles = project.getPreferenceProfiles();
      List<PreferenceProfile> preferenceProfileReferences = project.getPreferenceProfileReferences();
      String projectPropertyValue = null;

      if (!preferenceProfileReferences.isEmpty() || !preferenceProfiles.isEmpty())
      {
        boolean hasOnlyDefaultPreferenceProfiles = false;
        if (!preferenceProfiles.isEmpty())
        {
          IProject iProject = WORKSPACE_ROOT.getProject(projectPreferenceNode.getName());
          List<PreferenceProfile> defaultPreferenceProfiles = getDefaultPreferenceProfiles(iProject, projectPreferenceNode);
          List<PreferenceProfile> copiedPreferenceProfiles = new ArrayList<>(EcoreUtil.copyAll(preferenceProfiles));
          hasOnlyDefaultPreferenceProfiles = EcoreUtil.equals(copiedPreferenceProfiles, defaultPreferenceProfiles);
        }

        if (!hasOnlyDefaultPreferenceProfiles)
        {
          Project copy = EcoreUtil.copy(project);

          copy.setPreferenceNode(null);
          EList<PreferenceProfile> copyPreferenceProfileReferences = copy.getPreferenceProfileReferences();
          copyPreferenceProfileReferences.clear();

          for (PreferenceProfile preferenceProfileReference : preferenceProfileReferences)
          {
            if (preferenceProfileReference.getPredicates().isEmpty())
            {
              PreferenceProfile proxy = ProjectConfigFactory.eINSTANCE.createPreferenceProfile();
              ((InternalEObject)proxy).eSetProxyURI(URI.createURI(".#" + preferenceProfileReference.eResource().getURIFragment(preferenceProfileReference))); //$NON-NLS-1$
              copyPreferenceProfileReferences.add(proxy);
            }
          }

          for (Iterator<EObject> it = EcoreUtil.getAllContents(copy.getPreferenceProfiles()); it.hasNext();)
          {
            EObject eObject = it.next();
            proxifyCrossReferences(eObject);
          }

          if (!copy.getPreferenceProfiles().isEmpty() || !copy.getPreferenceProfileReferences().isEmpty())
          {
            Resource resource = new XMLResourceImpl(workspaceConfiguration.eResource().getURI());
            resource.getContents().add(copy);

            Map<Object, Object> options = new HashMap<>();
            options.put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
            options.put(XMLResource.OPTION_LINE_WIDTH, 10);
            options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
            try
            {
              StringWriter writer = new StringWriter();
              OutputStream out = new URIConverter.WriteableOutputStream(writer, "UTF-8"); //$NON-NLS-1$
              resource.save(out, options);
              projectPropertyValue = writer.toString();
            }
            catch (IOException ex)
            {
              ProjectConfigPlugin.INSTANCE.log(ex);
            }
          }
        }
      }

      if (projectPropertyValue == null)
      {
        if (cache)
        {
          projectsPreferenceNode = projectPreferenceNode.getParent();
          PreferenceNode projectConfPreferenceNode = projectPreferenceNode.getNode(PROJECT_CONF_NODE_NAME);
          if (projectConfPreferenceNode != null)
          {
            projectConfPreferenceNode.getChildren().remove(projectConfPreferenceNode);
          }
        }
        else if (projectPreferences.nodeExists(PROJECT_CONF_NODE_NAME))
        {
          Preferences projectConfPreferenceNode = projectPreferences.node(PROJECT_CONF_NODE_NAME);
          projectConfPreferenceNode.remove(PROJECT_CONF_PROJECT_KEY);
          projectConfPreferenceNode.flush();
          projectConfPreferenceNode.removeNode();
          projectPreferences.flush();
        }
      }
      else
      {
        if (cache)
        {
          projectsPreferenceNode = projectPreferenceNode.getParent();

          PreferenceNode projectConfPreferenceNode = projectPreferenceNode.getNode(PROJECT_CONF_NODE_NAME);
          if (projectConfPreferenceNode == null)
          {
            projectConfPreferenceNode = PreferencesFactory.eINSTANCE.createPreferenceNode();
            projectConfPreferenceNode.setName(PROJECT_CONF_NODE_NAME);
            projectPreferenceNode.getChildren().add(projectConfPreferenceNode);
          }

          Property projectConfProperty = projectConfPreferenceNode.getProperty(PROJECT_CONF_PROJECT_KEY);
          if (projectConfProperty == null)
          {
            projectConfProperty = PreferencesFactory.eINSTANCE.createProperty();
            projectConfProperty.setName(PROJECT_CONF_PROJECT_KEY);
            projectConfPreferenceNode.getProperties().add(projectConfProperty);
          }

          projectConfProperty.setValue(projectPropertyValue);
        }
        else
        {
          Preferences projectConfPreferences = projectPreferences.node(PROJECT_CONF_NODE_NAME);
          projectConfPreferences.put(PROJECT_CONF_PROJECT_KEY, projectPropertyValue);
          projectConfPreferences.flush();
        }
      }
    }

    return projectsPreferenceNode;
  }

  private static void proxifyCrossReferences(EObject eObject)
  {
    for (EReference eReference : eObject.eClass().getEAllReferences())
    {
      if (!eReference.isTransient() && !eReference.isContainer() && !eReference.isContainment())
      {
        if (eReference.isMany())
        {
          @SuppressWarnings("unchecked")
          EList<EObject> eObjects = (EList<EObject>)eObject.eGet(eReference);
          List<EObject> eObjectsCopy = new ArrayList<>(eObjects);
          eObjects.clear();
          for (EObject referencedEObject : eObjectsCopy)
          {
            Resource eResource = referencedEObject.eResource();
            if (eResource == null)
            {
              eObjects.add(referencedEObject);
            }
            else
            {
              EObject proxy = EcoreUtil.create(referencedEObject.eClass());
              ((InternalEObject)proxy).eSetProxyURI(URI.createURI(".#" + eResource.getURIFragment(referencedEObject))); //$NON-NLS-1$
              eObjects.add(proxy);
            }
          }
        }
        else
        {
          EObject referencedEObject = (EObject)eObject.eGet(eReference);
          Resource eResource = referencedEObject.eResource();
          if (eResource != null)
          {
            EObject proxy = EcoreUtil.create(referencedEObject.eClass());
            ((InternalEObject)proxy).eSetProxyURI(URI.createURI(".#" + eResource.getURIFragment(referencedEObject))); //$NON-NLS-1$
            eObject.eSet(eReference, proxy);
          }
        }
      }
    }
  }

  public static IProject getProject(Project project)
  {
    PreferenceNode preferenceNode = project.getPreferenceNode();
    if (preferenceNode != null)
    {
      String name = preferenceNode.getName();
      if (name != null)
      {
        return WORKSPACE_ROOT.getProject(name);
      }
    }

    return null;
  }

  public static Map<PreferenceNode, Set<Property>> getUnmanagedPreferenceNodes(Project project)
  {
    return ProjectConfigValidator.collectUnmanagedPreferences(project);
  }

  public static Map<PreferenceNode, Map<Property, Property>> getInconsistentPreferenceNodes(Project project)
  {
    return ProjectConfigValidator.collectInconsistentPreferences(project);
  }
}
