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
package org.eclipse.oomph.projectconfig.presentation.sync;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.projectconfig.Project;
import org.eclipse.oomph.projectconfig.WorkspaceConfiguration;
import org.eclipse.oomph.projectconfig.impl.ProjectConfigPlugin;
import org.eclipse.oomph.projectconfig.presentation.ProjectConfigEditorPlugin;
import org.eclipse.oomph.projectconfig.presentation.sync.ProjectConfigSynchronizerPreferences.PropertyModificationHandling;
import org.eclipse.oomph.projectconfig.util.ProjectConfigUtil;

import org.eclipse.emf.ecore.resource.Resource;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ProjectConfigSynchronizer implements IStartup
{
  private static final IWorkspace WORKSPACE = ResourcesPlugin.getWorkspace();

  private static final IWorkspaceRoot WORKSPACE_ROOT = WORKSPACE.getRoot();

  private WorkspaceConfiguration workspaceConfiguration;

  private ProjectConfigSynchronizerDialog projectConfigSynchronizerDialog;

  protected IResourceChangeListener resourceChangeListener = new IResourceChangeListener()
  {
    public void resourceChanged(IResourceChangeEvent event)
    {
      IResourceDelta delta = event.getDelta();
      if (delta != null)
      {
        try
        {
          class ResourceDeltaVisitor implements IResourceDeltaVisitor
          {
            private Collection<IPath> changedPaths = new HashSet<IPath>();

            public Collection<IPath> getChangedPaths()
            {
              return changedPaths;
            }

            public boolean visit(final IResourceDelta delta)
            {
              int type = delta.getResource().getType();
              if (type == IResource.FOLDER)
              {
                // Visit the folder's children only if its the .settings folder.
                IPath fullPath = delta.getFullPath();
                if (!".settings".equals(fullPath.lastSegment())) //$NON-NLS-1$
                {
                  return false;
                }
              }
              else if (type == IResource.FILE)
              {
                // Include only the *.prefs resources in the .settings folder.
                IPath fullPath = delta.getFullPath();
                if (fullPath.segmentCount() > 2 && "prefs".equals(fullPath.getFileExtension()) //$NON-NLS-1$
                    && !ProjectConfigUtil.PROJECT_CONF_NODE_NAME.equals(fullPath.removeFileExtension().lastSegment()))
                {
                  changedPaths.add(fullPath);
                }

                return false;
              }

              // Recursively visit only the workspace root, the projects, and the .settings folders in projects.
              return true;
            }
          }

          ResourceDeltaVisitor visitor = new ResourceDeltaVisitor();
          delta.accept(visitor);
          final Collection<IPath> changedPaths = visitor.getChangedPaths();

          if (!changedPaths.isEmpty())
          {
            PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable()
            {
              public void run()
              {
                final WorkspaceConfiguration newWorkspaceConfiguration = ProjectConfigUtil.getWorkspaceConfiguration();
                try
                {
                  newWorkspaceConfiguration.updatePreferenceProfileReferences();
                  Resource newWorkspaceConfigurationResource = newWorkspaceConfiguration.eResource();
                  Map<Property, Property> propertyMap = new LinkedHashMap<Property, Property>();
                  for (IPath preferenceNodePath : changedPaths)
                  {
                    String projectName = preferenceNodePath.segment(0);
                    Project oldProject = workspaceConfiguration.getProject(projectName);
                    Project newProject = newWorkspaceConfiguration.getProject(projectName);
                    if (newProject != null)
                    {
                      String preferenceNodeName = preferenceNodePath.removeFileExtension().lastSegment();
                      PreferenceNode oldPreferenceNode = oldProject == null ? null : oldProject.getPreferenceNode().getNode(preferenceNodeName);
                      PreferenceNode newPreferenceNode = newProject.getPreferenceNode().getNode(preferenceNodeName);
                      Map<Property, Property> modifiedProperties = collectModifiedProperties(workspaceConfiguration, oldPreferenceNode, newPreferenceNode);
                      if (!modifiedProperties.isEmpty())
                      {
                        for (Map.Entry<Property, Property> entry : modifiedProperties.entrySet())
                        {
                          Property property = entry.getKey();
                          String value = property.getValue();

                          Property preferenceProfileProperty = newProject.getProperty(property.getRelativePath());
                          if (preferenceProfileProperty != null)
                          {
                            String preferenceProfilePropertyValue = preferenceProfileProperty.getValue();
                            if (value == null ? preferenceProfilePropertyValue != null : !value.equals(preferenceProfilePropertyValue))
                            {
                              propertyMap.put(property, preferenceProfileProperty);
                            }
                          }
                          else
                          {
                            propertyMap.put(property, entry.getValue());
                          }
                        }
                      }
                    }
                  }

                  if (!propertyMap.isEmpty())
                  {
                    boolean dialogCreator = false;
                    IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                    Shell shell = activeWorkbenchWindow.getShell();
                    if (projectConfigSynchronizerDialog == null
                        && (configurationValidationPrompt || propertyModificationHandling == PropertyModificationHandling.PROMPT))
                    {
                      projectConfigSynchronizerDialog = new ProjectConfigSynchronizerDialog(shell);
                      projectConfigSynchronizerDialog.setWorkspaceConfiguration(newWorkspaceConfiguration);
                      dialogCreator = true;
                    }

                    Map<Property, Property> managedProperties = new HashMap<Property, Property>();
                    for (Map.Entry<Property, Property> propertyEntry : propertyMap.entrySet())
                    {
                      Property property = propertyEntry.getKey();
                      Property otherProperty = propertyEntry.getValue();
                      if (otherProperty != null && otherProperty.eResource() == newWorkspaceConfigurationResource)
                      {
                        if (propertyModificationHandling == PropertyModificationHandling.PROMPT)
                        {
                          projectConfigSynchronizerDialog.managedProperty(property, otherProperty);
                        }

                        managedProperties.put(property, otherProperty);
                      }
                      else
                      {
                        if (configurationValidationPrompt)
                        {
                          projectConfigSynchronizerDialog.unmanagedProperty(property, otherProperty);
                        }
                      }
                    }

                    if (projectConfigSynchronizerDialog != null)
                    {
                      if (dialogCreator)
                      {
                        ProjectConfigSynchronizerDialog dialog = projectConfigSynchronizerDialog;
                        projectConfigSynchronizerDialog.open();
                        if (dialog.hasUnmanagedProperties() && ProjectConfigSynchronizerPreferences.isEdit())
                        {
                          PreferenceDialog preferenceDialog = org.eclipse.ui.dialogs.PreferencesUtil.createPreferenceDialogOn(shell,
                              "org.eclipse.oomph.projectconfig.presentation.ProjectConfigPreferencePage", null, null); //$NON-NLS-1$
                          preferenceDialog.open();
                        }
                      }
                      else
                      {
                        Shell dialogShell = projectConfigSynchronizerDialog.getShell();
                        Display display = dialogShell.getDisplay();
                        while (!dialogShell.isDisposed())
                        {
                          if (!display.readAndDispatch())
                          {
                            display.sleep();
                          }
                        }
                      }
                    }

                    if (!managedProperties.isEmpty() && (propertyModificationHandling == PropertyModificationHandling.PROPAGATE
                        || propertyModificationHandling == PropertyModificationHandling.PROMPT && ProjectConfigSynchronizerPreferences.isPropagate()))
                    {
                      for (Map.Entry<Property, Property> entry : managedProperties.entrySet())
                      {
                        Property managedProperty = entry.getKey();
                        Property managingProperty = entry.getValue();
                        try
                        {
                          Preferences preferences = PreferencesUtil.getPreferences(managingProperty.getParent(), false);
                          preferences.put(managingProperty.getName(), managedProperty.getValue());
                        }
                        catch (BackingStoreException ex)
                        {
                          ProjectConfigEditorPlugin.INSTANCE.log(ex);
                        }
                      }

                      try
                      {
                        WORKSPACE_ROOT.getWorkspace().run(new IWorkspaceRunnable()
                        {
                          public void run(IProgressMonitor monitor) throws CoreException
                          {
                            try
                            {
                              PreferencesUtil.getPreferences(newWorkspaceConfiguration.getInstancePreferenceNode().getParent().getNode("project"), false) //$NON-NLS-1$
                                  .flush();
                            }
                            catch (BackingStoreException ex)
                            {
                              ProjectConfigPlugin.INSTANCE.log(ex);
                            }
                          }
                        }, new NullProgressMonitor());
                      }
                      catch (CoreException ex)
                      {
                        ProjectConfigPlugin.INSTANCE.log(ex);
                      }
                    }
                  }

                  // We expect this processing of the managed property to cause another delta which will apply the
                  // preference profiles anyway, so
                  // don't do it now when we might overwrite the properties being propagated.
                  if (projectConfigSynchronizerDialog == null || !projectConfigSynchronizerDialog.hasManagedProperties())
                  {
                    newWorkspaceConfiguration.applyPreferenceProfiles();
                  }
                }
                finally
                {
                  workspaceConfiguration = newWorkspaceConfiguration;
                  projectConfigSynchronizerDialog = null;
                }
              }

              private Map<Property, Property> collectModifiedProperties(WorkspaceConfiguration workspaceConfiguration, PreferenceNode oldPreferenceNode,
                  PreferenceNode newPreferenceNode)
              {
                LinkedHashMap<Property, Property> result = new LinkedHashMap<Property, Property>();
                collectModifiedProperties(result, workspaceConfiguration, oldPreferenceNode, newPreferenceNode);
                return result;
              }

              private void collectModifiedProperties(Map<Property, Property> result, WorkspaceConfiguration workspaceConfiguration,
                  PreferenceNode oldPreferenceNode, PreferenceNode newPreferenceNode)
              {
                if (newPreferenceNode != null)
                {
                  for (PreferenceNode newChild : newPreferenceNode.getChildren())
                  {
                    PreferenceNode oldChild = oldPreferenceNode == null ? null : oldPreferenceNode.getNode(newChild.getName());
                    if (oldChild == null)
                    {
                      for (Property newProperty : newChild.getProperties())
                      {
                        result.put(newProperty, null);
                      }
                    }
                    else
                    {
                      collectModifiedProperties(result, workspaceConfiguration, oldChild, newChild);
                    }
                  }

                  for (Property newProperty : newPreferenceNode.getProperties())
                  {
                    Property oldProperty = oldPreferenceNode == null ? null : oldPreferenceNode.getProperty(newProperty.getName());
                    if (oldProperty == null)
                    {
                      result.put(newProperty, null);
                    }
                    else
                    {
                      String newValue = newProperty.getValue();
                      String oldValue = oldProperty.getValue();
                      if (newValue == null ? oldValue != null : !newValue.equals(oldValue))
                      {
                        result.put(newProperty, oldProperty);
                      }
                    }
                  }
                }
              }
            });
          }
        }
        catch (CoreException exception)
        {
          ProjectConfigEditorPlugin.INSTANCE.log(exception);
        }
      }
    }
  };

  private boolean configurationValidationPrompt = ProjectConfigSynchronizerPreferences.isConfigurationValidationPrompt();

  private PropertyModificationHandling propertyModificationHandling = ProjectConfigSynchronizerPreferences.getPropertyModificationHandling();

  public ProjectConfigSynchronizer()
  {
    ProjectConfigEditorPlugin.Implementation.setProjectConfigSynchronizer(this);
  }

  public void earlyStartup()
  {
    update();
  }

  public void stop()
  {
    WORKSPACE.removeResourceChangeListener(resourceChangeListener);
  }

  public void update()
  {
    if (ProjectConfigSynchronizerPreferences.isConfigurationManagementAutomatic())
    {
      new ProjectConfigUtil.CompletenessChecker()
      {
        @Override
        protected void complete(WorkspaceConfiguration workspaceConfiguration)
        {
          ProjectConfigSynchronizer.this.workspaceConfiguration = workspaceConfiguration;
          workspaceConfiguration.updatePreferenceProfileReferences();
        }
      };

      WORKSPACE.addResourceChangeListener(resourceChangeListener);
    }
    else
    {
      WORKSPACE.removeResourceChangeListener(resourceChangeListener);
    }

    configurationValidationPrompt = ProjectConfigSynchronizerPreferences.isConfigurationValidationPrompt();
    propertyModificationHandling = ProjectConfigSynchronizerPreferences.getPropertyModificationHandling();
  }
}
