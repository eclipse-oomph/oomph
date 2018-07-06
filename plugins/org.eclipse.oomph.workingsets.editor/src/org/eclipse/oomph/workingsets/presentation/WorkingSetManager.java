/*
 * Copyright (c) 2014, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.workingsets.presentation;

import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.WorkingSetGroup;
import org.eclipse.oomph.workingsets.util.WorkingSetsUtil;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IAggregateWorkingSet;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class WorkingSetManager
{
  private static final String WORKING_SET_PAGE = "org.eclipse.jdt.ui.JavaWorkingSetPage";

  private static final String PACKAGE_EXPLORER_ID = "org.eclipse.jdt.ui.PackageExplorer";

  private static final IWorkingSetManager MANAGER = PlatformUI.getWorkbench().getWorkingSetManager();

  private static final IWorkspace WORKSPACE = ResourcesPlugin.getWorkspace();

  public static final WorkingSetManager INSTANCE = new WorkingSetManager();

  private final IEclipsePreferences.IPreferenceChangeListener preferencesListener = new IEclipsePreferences.IPreferenceChangeListener()
  {
    public void preferenceChange(PreferenceChangeEvent event)
    {
      if (WorkingSetsUtil.WORKING_SET_GROUP_PREFERENCE_KEY.equals(event.getKey()))
      {
        apply();
      }
    }
  };

  private final IResourceChangeListener resourceChangeListener = new IResourceChangeListener()
  {
    public void resourceChanged(IResourceChangeEvent event)
    {
      if (!workingSetGroup.getWorkingSets().isEmpty())
      {
        IResourceDelta delta = event.getDelta();
        if (delta != null)
        {
          try
          {
            // Compute the workings sets and update them relative to our workspace delta, i.e., relative to added and
            // removed projects.
            class ResourceDeltaVisitor implements IResourceDeltaVisitor
            {
              List<IProject> addedProjects = new ArrayList<IProject>();

              List<IProject> removedProjects = new ArrayList<IProject>();

              public boolean visit(IResourceDelta delta) throws CoreException
              {
                IResource resource = delta.getResource();
                if (resource instanceof IWorkspaceRoot)
                {
                  return true;
                }

                if (resource instanceof IProject)
                {
                  int kind = delta.getKind();
                  if (kind == IResourceDelta.ADDED)
                  {
                    IProject project = (IProject)resource;
                    if (!project.isHidden())
                    {
                      addedProjects.add(project);
                    }
                  }
                  else if (kind == IResourceDelta.REMOVED)
                  {
                    IProject project = (IProject)resource;
                    if (!project.isHidden())
                    {
                      removedProjects.remove(project);
                    }
                  }
                }

                return false;
              }
            }

            ResourceDeltaVisitor resourceDeltaVisitor = new ResourceDeltaVisitor();

            delta.accept(resourceDeltaVisitor);
            updateProjects(resourceDeltaVisitor.addedProjects, resourceDeltaVisitor.removedProjects);
          }
          catch (CoreException ex)
          {
            // Ignore
          }
        }
      }
    }
  };

  private WorkingSetGroup workingSetGroup;

  public WorkingSetManager()
  {
    workingSetGroup = WorkingSetsUtil.getWorkingSetGroup();

    // Listen for projects being added or removed from the workspace and for preferences changing.
    WORKSPACE.addResourceChangeListener(resourceChangeListener);
    WorkingSetsUtil.WORKING_SET_GROUP_PREFERENCES.addPreferenceChangeListener(preferencesListener);
  }

  /**
   * Clean up the listeners.
   */
  public void dispose()
  {
    WORKSPACE.removeResourceChangeListener(resourceChangeListener);

    WorkingSetsUtil.WORKING_SET_GROUP_PREFERENCES.removePreferenceChangeListener(preferencesListener);
  }

  /**
   * Returns a map with an entry for every working set defined in the working set group from the working set name to either the elements in the real working set or to null, if there isn't one.
   */
  private EMap<String, Set<IAdaptable>> getWorkingSets()
  {
    EMap<String, Set<IAdaptable>> workingSets = new BasicEMap<String, Set<IAdaptable>>();
    for (WorkingSet workingSet : workingSetGroup.getWorkingSets())
    {
      String name = workingSet.getName();
      IWorkingSet iWorkingSet = MANAGER.getWorkingSet(name);
      workingSets.put(name, iWorkingSet == null ? new LinkedHashSet<IAdaptable>() : new LinkedHashSet<IAdaptable>(Arrays.asList(iWorkingSet.getElements())));
    }

    return workingSets;
  }

  /**
   * Update the real workings sets based on the map.
   * This deletes the real working set for any map entry with a null value and creates or updates the real working set for every other entry.
   */
  private void apply(final EMap<String, Set<IAdaptable>> workingSets)
  {
    for (Map.Entry<String, Set<IAdaptable>> entry : workingSets)
    {
      String key = entry.getKey();
      Set<IAdaptable> value = entry.getValue();
      IWorkingSet workingSet = MANAGER.getWorkingSet(key);
      if (workingSet == null)
      {
        if (key != null && value != null)
        {
          workingSet = MANAGER.createWorkingSet(key, value.toArray(new IAdaptable[value.size()]));
          workingSet.setLabel(key);
          workingSet.setId(WORKING_SET_PAGE);
          MANAGER.addWorkingSet(workingSet);
        }
      }
      else
      {
        if (value == null)
        {
          MANAGER.removeWorkingSet(workingSet);
        }
        else
        {
          workingSet.setElements(value.toArray(new IAdaptable[value.size()]));
        }
      }
    }

    managePackageExplorer(true);
  }

  private void managePackageExplorer(boolean addListener)
  {
    if (WORKSPACE.getRoot().getProjects().length != 0)
    {
      try
      {
        boolean handledNavigator = false;
        boolean handledPackageExplorer = false;

        for (IWorkbenchWindow workbenchWindow : PlatformUI.getWorkbench().getWorkbenchWindows())
        {
          for (IWorkbenchPage workbenchPage : workbenchWindow.getPages())
          {
            if (!handledNavigator)
            {
              Object commonNavigator = workbenchPage.findView(IPageLayout.ID_PROJECT_EXPLORER);
              if (commonNavigator != null)
              {
                handledNavigator = true;

                Method getNavigatorActionServiceMethod = commonNavigator.getClass().getMethod("getNavigatorActionService");
                Object navigatorActionService = getNavigatorActionServiceMethod.invoke(commonNavigator);
                Field actionProviderInstancesField = navigatorActionService.getClass().getDeclaredField("actionProviderInstances");
                actionProviderInstancesField.setAccessible(true);
                HashMap<?, ?> object = (HashMap<?, ?>)actionProviderInstancesField.get(navigatorActionService);
                if (object != null)
                {
                  for (Object value : object.values())
                  {
                    Class<? extends Object> theClass = value.getClass();
                    if ("org.eclipse.ui.internal.navigator.resources.actions.WorkingSetActionProvider".equals(theClass.getName()))
                    {

                      Field workingSetField = theClass.getDeclaredField("workingSet");
                      workingSetField.setAccessible(true);
                      IWorkingSet oldWorkingSet = (IWorkingSet)workingSetField.get(value);

                      IWorkingSet[] activeWorkingSets = oldWorkingSet == null ? new IWorkingSet[0]
                          : oldWorkingSet.isAggregateWorkingSet() ? ((IAggregateWorkingSet)oldWorkingSet).getComponents() : new IWorkingSet[] { oldWorkingSet };

                      Set<IWorkingSet> allWorkingSets = new LinkedHashSet<IWorkingSet>(Arrays.asList(activeWorkingSets));
                      allWorkingSets.addAll(Arrays.asList(MANAGER.getAllWorkingSets()));

                      List<IWorkingSet> newActiveWorkingSets = getActiveWorkingSets(allWorkingSets.toArray(new IWorkingSet[allWorkingSets.size()]),
                          activeWorkingSets);
                      StringBuilder id = new StringBuilder("Aggregate:");
                      for (Iterator<IWorkingSet> it = newActiveWorkingSets.iterator(); it.hasNext();)
                      {
                        IWorkingSet iWorkingSet = it.next();
                        if (iWorkingSet == null)
                        {
                          it.remove();
                        }
                        else
                        {
                          id.append(iWorkingSet.getName());
                          id.append(":");
                        }
                      }

                      IWorkingSet aggregateWorkingSet = MANAGER.getWorkingSet(id.toString());
                      if (aggregateWorkingSet == null)
                      {
                        aggregateWorkingSet = MANAGER.createAggregateWorkingSet(id.toString(), "Multiple Working Sets",
                            newActiveWorkingSets.toArray(new IWorkingSet[newActiveWorkingSets.size()]));
                        MANAGER.addWorkingSet(aggregateWorkingSet);
                      }
                      MANAGER.addRecentWorkingSet(aggregateWorkingSet);

                      Method setWorkingSetMethod = theClass.getDeclaredMethod("setWorkingSet", IWorkingSet.class);
                      setWorkingSetMethod.setAccessible(true);
                      setWorkingSetMethod.invoke(value, aggregateWorkingSet);
                    }
                  }
                }
              }
            }

            if (!handledPackageExplorer)
            {
              IViewPart packageExplorer = workbenchPage.findView(PACKAGE_EXPLORER_ID);
              if (packageExplorer != null)
              {
                handledPackageExplorer = true;

                Class<? extends IViewPart> packageExplorerClass = packageExplorer.getClass();
                Method getWorkingSetModelMethod = packageExplorerClass.getMethod("getWorkingSetModel");
                Object workingSetModel = getWorkingSetModelMethod.invoke(packageExplorer);
                if (workingSetModel != null)
                {
                  Class<?> workingSetModelClass = workingSetModel.getClass();
                  Method getAllWorkingSetsMethod = workingSetModelClass.getMethod("getAllWorkingSets");
                  IWorkingSet[] allWorkingSets = (IWorkingSet[])getAllWorkingSetsMethod.invoke(workingSetModel);

                  Method getActiveWorkingSetsMethod = workingSetModelClass.getMethod("getActiveWorkingSets");
                  IWorkingSet[] activeWorkingSets = (IWorkingSet[])getActiveWorkingSetsMethod.invoke(workingSetModel);

                  List<IWorkingSet> newActiveWorkingSets = getActiveWorkingSets(allWorkingSets, activeWorkingSets);

                  IWorkingSet[] orderedActiveWorkingSetsArray = newActiveWorkingSets.toArray(new IWorkingSet[newActiveWorkingSets.size()]);

                  Method setWorkingSetsMethod = workingSetModelClass.getMethod("setActiveWorkingSets", IWorkingSet[].class);

                  setWorkingSetsMethod.invoke(workingSetModel, new Object[] { orderedActiveWorkingSetsArray });
                }
              }
            }
          }
        }
      }
      catch (NoSuchMethodException ex)
      {
        WorkingSetsEditorPlugin.INSTANCE.log(ex);
      }
      catch (SecurityException ex)
      {
        WorkingSetsEditorPlugin.INSTANCE.log(ex);
      }
      catch (IllegalAccessException ex)
      {
        WorkingSetsEditorPlugin.INSTANCE.log(ex);
      }
      catch (InvocationTargetException ex)
      {
        WorkingSetsEditorPlugin.INSTANCE.log(ex);
      }
      catch (NoSuchFieldException ex)
      {
        WorkingSetsEditorPlugin.INSTANCE.log(ex);
      }
    }
  }

  private List<IWorkingSet> getActiveWorkingSets(IWorkingSet[] allWorkingSets, IWorkingSet[] activeWorkingSets)
  {
    Map<WorkingSet, IWorkingSet> managedWorkingSets = new HashMap<WorkingSet, IWorkingSet>();
    for (int i = 0; i < allWorkingSets.length; ++i)
    {
      IWorkingSet iWorkingSet = allWorkingSets[i];
      WorkingSet workingSet = workingSetGroup.getWorkingSet(iWorkingSet.getName());
      if (workingSet != null)
      {
        managedWorkingSets.put(workingSet, iWorkingSet);
      }
    }

    Map<IWorkingSet, List<IWorkingSet>> orderedWorkingSetGroups = new LinkedHashMap<IWorkingSet, List<IWorkingSet>>();
    for (WorkingSet workingSet : workingSetGroup.getWorkingSets())
    {
      IWorkingSet iWorkingSet = managedWorkingSets.get(workingSet);
      List<IWorkingSet> group = new ArrayList<IWorkingSet>();
      group.add(iWorkingSet);
      orderedWorkingSetGroups.put(iWorkingSet, group);
    }

    List<IWorkingSet> newActiveWorkingSets = new ArrayList<IWorkingSet>();
    List<IWorkingSet> group = newActiveWorkingSets;
    for (IWorkingSet iWorkingSet : activeWorkingSets)
    {
      List<IWorkingSet> targetGroup = orderedWorkingSetGroups.get(iWorkingSet);
      if (targetGroup == null)
      {
        group.add(iWorkingSet);
      }
      else
      {
        group = targetGroup;
      }
    }

    for (List<IWorkingSet> workingSets : orderedWorkingSetGroups.values())
    {
      newActiveWorkingSets.addAll(workingSets);
    }
    return newActiveWorkingSets;
  }

  private void updateProjects(final List<IProject> addedProjects, final List<IProject> removedProjects)
  {
    // Do this on the UI thread to avoid problems with JDT's getting out of sync with respect to our updates.
    Display.getDefault().asyncExec(new Runnable()
    {
      public void run()
      {
        EMap<String, Set<IAdaptable>> workingSets = getWorkingSets();
        boolean isChanged = false;
        for (IProject project : addedProjects)
        {
          if (addProject(project, workingSets))
          {
            isChanged = true;
          }
        }
        for (IProject project : removedProjects)
        {
          if (removeProject(project, workingSets))
          {
            isChanged = true;
          }
        }

        if (isChanged)
        {
          apply(workingSets);
        }
      }
    });
  }

  /**
   * Compute the elements for the working sets based on the projects in the workspace.
   * Returns <code>true</code> only if a project was added to some working set.
   */
  private boolean updateProjects(EMap<String, Set<IAdaptable>> workingSets)
  {
    boolean result = false;
    for (IProject project : WORKSPACE.getRoot().getProjects())
    {
      if (addProject(project, workingSets))
      {
        result = true;
      }
    }

    return result;
  }

  /**
   * Adds the project to the appropriate working set entry, if applicable.
   * Returns <code>true</code> only if the project was added to some working set.
   */
  private boolean addProject(IProject project, EMap<String, Set<IAdaptable>> workingSets)
  {
    boolean result = false;
    for (WorkingSet workingSet : workingSetGroup.getWorkingSets())
    {
      if (workingSet.matches(project))
      {
        String name = workingSet.getName();
        Set<IAdaptable> elements = workingSets.get(name);
        if (elements == null)
        {
          elements = new LinkedHashSet<IAdaptable>();
          workingSets.put(name, elements);
        }

        elements.add(project);
        result = true;
      }
    }
    return result;
  }

  /**
   * Removes the project from the appropriate working set entry.
   * Returns <code>true</code> only if the project was removed to some working set.
   */
  private boolean removeProject(IProject project, EMap<String, Set<IAdaptable>> workingSets)
  {
    boolean result = false;
    for (WorkingSet workingSet : workingSetGroup.getWorkingSets())
    {
      if (workingSet.matches(project))
      {
        String name = workingSet.getName();
        Set<IAdaptable> elements = workingSets.get(name);
        if (elements != null)
        {
          elements.remove(project);
          result = true;
        }
      }
    }
    return result;
  }

  public void apply()
  {
    WorkingSetGroup oldWorkingSetGroup = workingSetGroup;

    // Compute the working sets for the new working group.
    workingSetGroup = WorkingSetsUtil.getWorkingSetGroup();

    final EMap<String, Set<IAdaptable>> workingSets = new BasicEMap<String, Set<IAdaptable>>();

    // Update the map to include null (to cause an delete) for any old working set not present in the new ones
    for (WorkingSet workingSet : oldWorkingSetGroup.getWorkingSets())
    {
      String name = workingSet.getName();
      workingSets.put(name, null);
    }

    // Update the map to include empty sets (to cause an add) for any new working set not already present.
    for (WorkingSet workingSet : workingSetGroup.getWorkingSets())
    {
      String name = workingSet.getName();
      workingSets.put(name, new LinkedHashSet<IAdaptable>());
    }

    // Update the working sets for all the projects in the workspace and apply the result to the real working sets.
    if (!workingSets.isEmpty())
    {
      Display.getDefault().asyncExec(new Runnable()
      {
        public void run()
        {
          updateProjects(workingSets);
          apply(workingSets);
        }
      });
    }
  }
}
