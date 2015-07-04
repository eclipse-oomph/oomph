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
package org.eclipse.oomph.setup.ui.recorder;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.ui.SetupEditorSupport;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OomphPlugin.BundleFile;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author Eike Stepper
 */
public abstract class RecorderTransaction
{
  private static final String POLICIES_FILE_NAME = "policies.properties";

  private static final String POLICIES_EXT_POINT = "org.eclipse.oomph.setup.ui.preferencePolicies";

  private static final String POLICY_RECORD = "record";

  private static final String POLICY_IGNORE = "ignore";

  private static final String REMOVE_PREFERENCE_MARKER = "REMOVE_PREFERENCE_MARKER";

  private static RecorderTransaction instance;

  private static boolean policiesInitialized;

  private final Resource resource;

  private final Map<String, Boolean> cleanPolicies = new HashMap<String, Boolean>();

  private final Map<String, Boolean> policies = new HashMap<String, Boolean>();

  private final Set<URI> preferencesToRemove = new HashSet<URI>();

  private Map<URI, String> preferences;

  private CompoundTask preferenceCompound;

  private Annotation recorderAnnotation;

  private SetupTaskContainer rootObject;

  private boolean forceDirty;

  RecorderTransaction(Resource resource)
  {
    this.resource = resource;

    rootObject = (SetupTaskContainer)resource.getContents().get(0);
    findRecorderAnnotation(rootObject);

    if (recorderAnnotation != null)
    {
      EMap<String, String> details = recorderAnnotation.getDetails();
      for (Map.Entry<String, String> entry : details)
      {
        String value = entry.getValue();
        if (POLICY_RECORD.equals(value))
        {
          cleanPolicies.put(entry.getKey(), true);
        }
        else if (POLICY_IGNORE.equals(value))
        {
          cleanPolicies.put(entry.getKey(), false);
        }
      }
    }
  }

  public void close()
  {
    instance = null;
  }

  public Resource getResource()
  {
    return resource;
  }

  public SetupTaskContainer getRootObject()
  {
    return rootObject;
  }

  public boolean isDirty()
  {
    if (forceDirty)
    {
      return true;
    }

    if (!policies.isEmpty())
    {
      return true;
    }

    if (preferences != null && !preferences.isEmpty())
    {
      return true;
    }

    return false;
  }

  public boolean isForceDirty()
  {
    return forceDirty;
  }

  public void setForceDirty(boolean forceDirty)
  {
    this.forceDirty = forceDirty;
  }

  public Map<String, Boolean> getPolicies(boolean clean)
  {
    if (clean)
    {
      return cleanPolicies;
    }

    return policies;
  }

  public void resetPolicies()
  {
    policies.clear();
  }

  public Boolean getPolicy(String key)
  {
    Boolean policy = policies.get(key);
    if (policy != null)
    {
      return policy;
    }

    return cleanPolicies.get(key);
  }

  public void setPolicy(String key, boolean policy)
  {
    Boolean cleanPolicy = cleanPolicies.get(key);
    if (cleanPolicy != null && cleanPolicy.equals(policy))
    {
      policies.remove(key);
    }
    else
    {
      policies.put(key, policy);
    }
  }

  public boolean setPolicy(String key, String value)
  {
    if (POLICY_RECORD.equals(value))
    {
      setPolicy(key, true);
      return true;
    }

    if (POLICY_IGNORE.equals(value))
    {
      setPolicy(key, false);
      return true;
    }

    return false;
  }

  public void setPreferences(Map<URI, String> preferences)
  {
    this.preferences = preferences;
  }

  public void removePreferences(Collection<URI> keys)
  {
    preferencesToRemove.addAll(keys);
  }

  public void commit()
  {
    if (isDirty())
    {
      doCommit();
    }
  }

  protected abstract void doCommit();

  protected final List<? extends Object> applyChanges()
  {
    if (recorderAnnotation == null)
    {
      preferenceCompound = SetupFactory.eINSTANCE.createCompoundTask("User Preferences");
      rootObject.getSetupTasks().add(0, preferenceCompound);

      recorderAnnotation = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_USER_PREFERENCES);
      preferenceCompound.getAnnotations().add(recorderAnnotation);

      migrateOldTasks();
    }

    List<Object> recorderObjects = new ArrayList<Object>();

    if (!policies.isEmpty())
    {
      recorderObjects.add(recorderAnnotation);
      EMap<String, String> details = recorderAnnotation.getDetails();

      Map<String, String> workspacePolicies = null;
      boolean workspacePoliciesChanged = false;
      IFile workspacePoliciesFile = getWorkspacePropertiesFile();

      for (Map.Entry<String, Boolean> entry : policies.entrySet())
      {
        String path = entry.getKey();
        boolean policy = entry.getValue();

        details.put(path, policy ? POLICY_RECORD : POLICY_IGNORE);
        cleanPolicies.put(path, policy);

        if (!policy)
        {
          URI key = PreferencesFactory.eINSTANCE.createURI(path);
          preferencesToRemove.add(key);

          if (workspacePoliciesFile != null)
          {
            if (workspacePolicies == null && workspacePoliciesFile.isAccessible())
            {
              workspacePolicies = PropertiesUtil.loadProperties(workspacePoliciesFile.getLocation().toFile());
            }

            if (workspacePolicies != null && !POLICY_IGNORE.equals(workspacePolicies.get(path)))
            {
              workspacePolicies.put(path, POLICY_IGNORE);
              workspacePoliciesChanged = true;
            }
          }
        }
      }

      if (workspacePoliciesChanged)
      {
        try
        {
          PropertiesUtil.saveProperties(workspacePoliciesFile.getLocation().toFile(), workspacePolicies, true);
          workspacePoliciesFile.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
        }
        catch (Exception ex)
        {
          SetupUIPlugin.INSTANCE.log(ex);
        }
      }

      policies.clear();
    }

    if (!preferencesToRemove.isEmpty())
    {
      if (preferences == null)
      {
        preferences = new HashMap<URI, String>();
      }

      for (URI key : preferencesToRemove)
      {
        preferences.put(key, REMOVE_PREFERENCE_MARKER);
      }
    }

    if (preferences != null)
    {
      for (Map.Entry<URI, String> entry : preferences.entrySet())
      {
        URI key = entry.getKey();
        String value = entry.getValue();

        String pluginID = key.segment(0).toString();
        String path = PreferencesFactory.eINSTANCE.convertURI(key);

        boolean remove = value == REMOVE_PREFERENCE_MARKER;

        CompoundTask pluginCompound = (CompoundTask)getPreferenceTask(preferenceCompound.getSetupTasks(), SetupPackage.Literals.COMPOUND_TASK__NAME, pluginID,
            !remove);
        if (pluginCompound != null)
        {
          PreferenceTask preferenceTask = (PreferenceTask)getPreferenceTask(pluginCompound.getSetupTasks(), SetupPackage.Literals.PREFERENCE_TASK__KEY, path,
              !remove);
          if (preferenceTask != null)
          {
            if (remove)
            {
              EcoreUtil.remove(preferenceTask);
              if (pluginCompound.getSetupTasks().isEmpty())
              {
                recorderObjects.add(pluginCompound.eContainer());
                EcoreUtil.remove(pluginCompound);
              }
              else
              {
                recorderObjects.add(pluginCompound);
              }
            }
            else
            {
              preferenceTask.setValue(SetupUtil.escape(value));
              recorderObjects.add(preferenceTask);
            }
          }
        }
      }

      preferences = null;
    }

    preferencesToRemove.clear();
    return recorderObjects;
  }

  protected void initializePolicies()
  {
    if (!policiesInitialized)
    {
      boolean changed = false;

      try
      {
        changed |= initializePoliciesFromProperties();
      }
      catch (Exception ex)
      {
        SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
      }

      try
      {
        changed |= initializePoliciesFromExtensions();
      }
      catch (Exception ex)
      {
        SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
      }

      if (changed)
      {
        commit();
      }

      policiesInitialized = true;
    }
  }

  private boolean initializePoliciesFromProperties()
  {
    boolean changed = false;

    BundleFile policiesFile = SetupUIPlugin.INSTANCE.getRootFile().getChild(POLICIES_FILE_NAME);
    if (policiesFile != null)
    {
      String contents = policiesFile.getContentsString();
      String[] lines = contents.split("[\n\r]");
      for (int i = 0; i < lines.length; i++)
      {
        try
        {
          String line = lines[i].trim();
          if (line.length() != 0)
          {
            int pos = line.lastIndexOf('=');
            if (pos != -1)
            {
              String key = line.substring(0, pos).trim();
              if (!cleanPolicies.containsKey(key))
              {
                String value = line.substring(pos + 1).trim();
                changed |= setPolicy(key, value);
              }
            }
          }
        }
        catch (Exception ex)
        {
          SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
        }
      }
    }

    return changed;
  }

  private boolean initializePoliciesFromExtensions()
  {
    boolean changed = false;

    IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
    for (IConfigurationElement configurationElement : extensionRegistry.getConfigurationElementsFor(POLICIES_EXT_POINT))
    {
      try
      {
        String pluginRelativePath = configurationElement.getAttribute("pluginRelativePath");
        while (pluginRelativePath != null && pluginRelativePath.startsWith("/"))
        {
          pluginRelativePath = pluginRelativePath.substring(1);
        }

        if (StringUtil.isEmpty(pluginRelativePath))
        {
          continue;
        }

        String policy = configurationElement.getAttribute("policy");

        IContributor contributor = configurationElement.getContributor();
        String contributorName = contributor.getName();

        String key = "/instance/" + contributorName + "/" + pluginRelativePath;
        changed |= setPolicy(key, policy);
      }
      catch (Exception ex)
      {
        SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
      }
    }

    return changed;
  }

  private void findRecorderAnnotation(SetupTaskContainer container)
  {
    if (container instanceof CompoundTask)
    {
      CompoundTask compound = (CompoundTask)container;
      recorderAnnotation = compound.getAnnotation(AnnotationConstants.ANNOTATION_USER_PREFERENCES);
      if (recorderAnnotation != null)
      {
        preferenceCompound = compound;
        return;
      }
    }

    for (SetupTask setupTask : container.getSetupTasks())
    {
      if (setupTask instanceof SetupTaskContainer)
      {
        findRecorderAnnotation((SetupTaskContainer)setupTask);
        if (recorderAnnotation != null)
        {
          return;
        }
      }
    }
  }

  private void migrateOldTasks()
  {
    migrateOldTasksRecursively(rootObject);

    EList<SetupTask> pluginCompounds = preferenceCompound.getSetupTasks();
    ECollections.sort(pluginCompounds, new Comparator<SetupTask>()
    {
      public int compare(SetupTask o1, SetupTask o2)
      {
        String n1 = StringUtil.safe(((CompoundTask)o1).getName()).toLowerCase();
        String n2 = StringUtil.safe(((CompoundTask)o2).getName()).toLowerCase();
        return n1.compareTo(n2);
      }
    });

    for (SetupTask pluginCompound : pluginCompounds)
    {
      EList<SetupTask> preferenceTasks = ((CompoundTask)pluginCompound).getSetupTasks();
      ECollections.sort(preferenceTasks, new Comparator<SetupTask>()
      {
        public int compare(SetupTask o1, SetupTask o2)
        {
          String n1 = StringUtil.safe(((PreferenceTask)o1).getKey()).toLowerCase();
          String n2 = StringUtil.safe(((PreferenceTask)o2).getKey()).toLowerCase();
          return n1.compareTo(n2);
        }
      });
    }
  }

  private void migrateOldTasksRecursively(SetupTaskContainer container)
  {
    if (container == preferenceCompound)
    {
      return;
    }

    for (Object object : container.getSetupTasks().toArray())
    {
      if (object instanceof PreferenceTask)
      {
        PreferenceTask preferenceTask = (PreferenceTask)object;
        EObject eContainer = preferenceTask.eContainer();

        String pluginID = URI.createURI(preferenceTask.getKey()).segment(1).toString();
        CompoundTask pluginCompound = (CompoundTask)getPreferenceTask(preferenceCompound.getSetupTasks(), SetupPackage.Literals.COMPOUND_TASK__NAME, pluginID,
            true);
        pluginCompound.getSetupTasks().add(preferenceTask);

        while (eContainer instanceof CompoundTask)
        {
          CompoundTask oldCompound = (CompoundTask)eContainer;
          if (oldCompound.getSetupTasks().isEmpty())
          {
            eContainer = oldCompound.eContainer();
            EcoreUtil.remove(oldCompound);
          }
          else
          {
            break;
          }
        }
      }
      else if (object instanceof SetupTaskContainer)
      {
        migrateOldTasksRecursively((SetupTaskContainer)object);
      }
    }
  }

  private static SetupTask getPreferenceTask(EList<SetupTask> tasks, EAttribute key, String value, boolean createOnDemand)
  {
    int position = 0;
    String value1 = StringUtil.safe(value).toLowerCase();

    for (SetupTask task : tasks)
    {
      String value2 = StringUtil.safe((String)task.eGet(key)).toLowerCase();
      int compare = value2.compareTo(value1);
      if (compare == 0)
      {
        return task;
      }

      if (compare < 0)
      {
        ++position;
      }
    }

    if (!createOnDemand)
    {
      return null;
    }

    EClass eClass = key.getEContainingClass();
    SetupTask task = (SetupTask)EcoreUtil.create(eClass);
    task.eSet(key, value);
    tasks.add(position, task);
    return task;
  }

  private static IFile getWorkspacePropertiesFile()
  {
    try
    {
      return ResourcesPlugin.getWorkspace().getRoot().getProject("org.eclipse.oomph.setup.ui").getFile(POLICIES_FILE_NAME);
    }
    catch (Throwable ex)
    {
      return null;
    }
  }

  public static RecorderTransaction open()
  {
    if (instance == null)
    {
      final IEditorPart[] editor = { null };
      final CountDownLatch editorLoadedLatch = new CountDownLatch(1);

      IWorkbench workbench = null;

      try
      {
        workbench = PlatformUI.getWorkbench();
      }
      catch (Exception ex)
      {
        //$FALL-THROUGH$
      }

      if (workbench != null)
      {
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        if (window == null)
        {
          IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
          if (windows.length != 0)
          {
            window = windows[0];
          }
        }

        final IWorkbenchPage page = window != null ? window.getActivePage() : null;
        if (page != null)
        {
          UIUtil.syncExec(new Runnable()
          {
            public void run()
            {
              editor[0] = SetupEditorSupport.getEditor(page, SetupContext.USER_SETUP_URI, false, new SetupEditorSupport.LoadHandler()
              {
                @Override
                protected void loaded(IEditorPart editor, EditingDomain domain, Resource resource)
                {
                  instance = new RecorderTransaction.EditorTransaction(editor, domain, resource);
                  editorLoadedLatch.countDown();
                }
              });
            }
          });
        }
      }

      if (editor[0] != null)
      {
        try
        {
          // This waiting must not be done on the UI thread.
          editorLoadedLatch.await();
        }
        catch (InterruptedException ex)
        {
          throw new Error(ex);
        }
      }
      else
      {
        ResourceSet resourceSet = org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil.createResourceSet();
        Resource resource = resourceSet.getResource(SetupContext.USER_SETUP_URI, true);
        instance = new ResourceTransaction(resource);
      }
    }

    return instance;
  }

  static RecorderTransaction getInstance()
  {
    return instance;
  }

  /**
   * @author Eike Stepper
   */
  public static final class EditorTransaction extends RecorderTransaction
  {
    private final IEditorPart editor;

    private final EditingDomain domain;

    private final boolean editorWasClean;

    private EditorTransaction(IEditorPart editor, EditingDomain domain, Resource resource)
    {
      super(resource);
      this.editor = editor;
      this.domain = domain;
      editorWasClean = !editor.isDirty();
      initializePolicies();
    }

    @Override
    protected void doCommit()
    {
      ISelection selection = ((ISelectionProvider)editor).getSelection();
      final List<?> oldSelection = selection instanceof IStructuredSelection ? ((IStructuredSelection)selection).toList() : Collections.emptyList();

      ChangeCommand command = new ChangeCommand(domain.getResourceSet())
      {
        List<? extends Object> recorderObjects = Collections.emptyList();

        List<? extends Object> affectedObjects = Collections.emptyList();

        @Override
        public String getLabel()
        {
          return "Record Preferences";
        }

        @Override
        public String getDescription()
        {
          return "Records the preferences changes as preference tasks";
        }

        @Override
        public Collection<?> getAffectedObjects()
        {
          return affectedObjects;
        }

        @Override
        protected void doExecute()
        {
          recorderObjects = applyChanges();
          affectedObjects = recorderObjects;
        }

        @Override
        public void undo()
        {
          super.undo();
          affectedObjects = oldSelection;
        }

        @Override
        public void redo()
        {
          super.redo();
          affectedObjects = recorderObjects;
        }
      };

      CommandStack commandStack = domain.getCommandStack();
      commandStack.execute(command);

      if (editorWasClean && editor.isDirty())
      {
        UIUtil.syncExec(new Runnable()
        {
          public void run()
          {
            editor.doSave(new NullProgressMonitor());
          }
        });
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class ResourceTransaction extends RecorderTransaction
  {
    private ResourceTransaction(Resource resource)
    {
      super(resource);
      initializePolicies();
    }

    @Override
    protected void doCommit()
    {
      List<? extends Object> recorderObjects = applyChanges();
      if (recorderObjects != null)
      {
        try
        {
          getResource().save(null);
        }
        catch (IOException ex)
        {
          SetupUIPlugin.INSTANCE.log(ex);
        }
      }
    }
  }
}
