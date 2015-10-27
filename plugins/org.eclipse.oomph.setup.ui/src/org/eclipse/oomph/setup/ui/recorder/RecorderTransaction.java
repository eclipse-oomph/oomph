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
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.SetupEditorSupport;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OomphPlugin.BundleFile;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.BasicEList;
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
import org.eclipse.emf.edit.domain.IEditingDomainProvider;

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

  private final Resource resource;

  private final Map<String, Boolean> cleanPolicies = new HashMap<String, Boolean>();

  private final Map<String, Boolean> policies = new HashMap<String, Boolean>();

  private final Set<URI> preferencesToRemove = new HashSet<URI>();

  private Map<URI, String> preferences;

  private SetupTaskContainer preferenceContainer;

  private Annotation recorderAnnotation;

  private final boolean user;

  private SetupTaskContainer rootObject;

  private boolean forceDirty;

  private CommitHandler commitHandler;

  RecorderTransaction(boolean user, SetupTaskContainer rootObject)
  {
    this.user = user;
    this.rootObject = rootObject;
    resource = rootObject.eResource();

    if (user)
    {
      SetupTaskContainer resourceRoot = (SetupTaskContainer)resource.getContents().get(0);
      findRecorderAnnotation(resourceRoot);

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
  }

  public boolean isUser()
  {
    return user;
  }

  public IEditorPart getEditor()
  {
    return null;
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

  public void setPolicies(Map<String, Boolean> policies)
  {
    resetPolicies();
    if (policies != null)
    {
      this.policies.putAll(policies);
    }
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

  public void removePolicy(String key)
  {
    policies.remove(key);
  }

  public Map<URI, String> getPreferences()
  {
    return preferences;
  }

  public void setPreferences(Map<URI, String> preferences)
  {
    this.preferences = preferences;
  }

  public void removePreferences(Collection<URI> keys)
  {
    preferencesToRemove.addAll(keys);
  }

  public CommitHandler getCommitHandler()
  {
    return commitHandler;
  }

  public void setCommitHandler(CommitHandler commitHandler)
  {
    this.commitHandler = commitHandler;
  }

  public Map<String, PreferenceTask> commit()
  {
    Map<String, PreferenceTask> preferenceTasks = new HashMap<String, PreferenceTask>();

    if (isDirty())
    {
      doCommit(preferenceTasks);
    }

    return preferenceTasks;
  }

  protected abstract void doCommit(Map<String, PreferenceTask> preferenceTasks);

  protected final List<? extends Object> applyChanges(Map<String, PreferenceTask> preferenceTasks)
  {
    List<Object> recorderObjects = new ArrayList<Object>();

    if (user)
    {
      if (recorderAnnotation == null)
      {
        CompoundTask preferenceCompound = SetupFactory.eINSTANCE.createCompoundTask("User Preferences");
        preferenceContainer = preferenceCompound;
        rootObject.getSetupTasks().add(0, preferenceCompound);

        recorderAnnotation = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_USER_PREFERENCES);
        preferenceContainer.getAnnotations().add(recorderAnnotation);
      }

      migrateOldTasks();

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
    }
    else
    {
      preferenceContainer = rootObject;
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
      record(preferences, preferenceContainer.getSetupTasks(), recorderObjects, preferenceTasks);
      preferences = null;

      if (commitHandler != null)
      {
        for (PreferenceTask preferenceTask : preferenceTasks.values())
        {
          commitHandler.handlePreferenceTask(preferenceTask);
        }
      }
    }

    preferencesToRemove.clear();
    return recorderObjects;
  }

  protected void initializePolicies()
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
        preferenceContainer = compound;
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

    EList<SetupTask> pluginCompounds = preferenceContainer.getSetupTasks();
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
    if (container == preferenceContainer)
    {
      return;
    }

    EList<SetupTask> setupTasks = container.getSetupTasks();
    for (SetupTask task : setupTasks.toArray(new SetupTask[setupTasks.size()]))
    {
      if (task.getRestrictions().isEmpty())
      {
        if (task instanceof PreferenceTask)
        {
          PreferenceTask preferenceTask = (PreferenceTask)task;
          EObject eContainer = preferenceTask.eContainer();

          String pluginID = URI.createURI(preferenceTask.getKey()).segment(1).toString();
          CompoundTask pluginCompound = (CompoundTask)getPreferenceTask(preferenceContainer.getSetupTasks(), SetupPackage.Literals.COMPOUND_TASK__NAME,
              pluginID, true);
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
        else if (task instanceof SetupTaskContainer)
        {
          migrateOldTasksRecursively((SetupTaskContainer)task);
        }
      }
    }
  }

  private static SetupTask getPreferenceTask(EList<SetupTask> tasks, EAttribute key, String value, boolean createOnDemand)
  {
    int position = 0;
    String value1 = StringUtil.safe(value).toLowerCase();

    for (SetupTask task : tasks)
    {
      if (task.eClass().getEAllStructuralFeatures().contains(key))
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
      else
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

  private static SetupTaskContainer getRootObject(Resource resource, String fragment)
  {
    if (!StringUtil.isEmpty(fragment))
    {
      EObject eObject = resource.getEObject(fragment);
      if (eObject instanceof SetupTaskContainer)
      {
        return (SetupTaskContainer)eObject;
      }
    }

    return (SetupTaskContainer)resource.getContents().get(0);
  }

  private static void record(Map<URI, String> preferences, EList<SetupTask> setupTasks, List<Object> recorderObjects,
      Map<String, PreferenceTask> preferenceTasks)
  {
    for (Map.Entry<URI, String> entry : preferences.entrySet())
    {
      URI key = entry.getKey();
      String value = entry.getValue();

      String pluginID = key.segment(0).toString();
      String path = PreferencesFactory.eINSTANCE.convertURI(key);

      boolean remove = value == REMOVE_PREFERENCE_MARKER;

      CompoundTask pluginCompound = (CompoundTask)getPreferenceTask(setupTasks, SetupPackage.Literals.COMPOUND_TASK__NAME, pluginID, !remove);
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

          if (preferenceTasks != null)
          {
            preferenceTasks.put(path, preferenceTask);
          }
        }
      }
    }
  }

  public static List<SetupTask> record(Map<URI, String> preferences)
  {
    EList<SetupTask> setupTasks = new BasicEList<SetupTask>();
    List<Object> recorderObjects = new ArrayList<Object>();
    record(preferences, setupTasks, recorderObjects, null);
    return setupTasks;
  }

  public static RecorderTransaction open()
  {
    if (instance == null)
    {
      URI targetURI = RecorderManager.INSTANCE.getRecorderTarget();
      final String targetFragment = targetURI.fragment();
      final URI targetResourceURI = targetURI.trimFragment();

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
              editor[0] = SetupEditorSupport.getEditor(page, targetResourceURI, false, new SetupEditorSupport.LoadHandler()
              {
                @Override
                protected void loaded(IEditorPart editor, EditingDomain domain, Resource resource)
                {
                  SetupTaskContainer rootObject = getRootObject(resource, targetFragment);

                  instance = new RecorderTransaction.EditorTransaction(true, editor, domain, rootObject);
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
        ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
        Resource resource = resourceSet.getResource(targetResourceURI, true);

        SetupTaskContainer rootObject = getRootObject(resource, targetFragment);
        instance = new ResourceTransaction(rootObject);
      }
    }

    return instance;
  }

  public static RecorderTransaction open(boolean user, IEditorPart editor)
  {
    if (instance != null)
    {
      throw new IllegalStateException("A recorder transaction is already open");
    }

    EditingDomain domain = ((IEditingDomainProvider)editor).getEditingDomain();
    instance = new RecorderTransaction.EditorTransaction(user, editor, domain, domain.getResourceSet().getResources().get(0));

    return instance;
  }

  public static RecorderTransaction openTmp(Resource resource)
  {
    SetupTaskContainer rootObject = getRootObject(resource, null);
    return new TmpTransaction(rootObject);
  }

  static RecorderTransaction getInstance()
  {
    return instance;
  }

  /**
   * @author Eike Stepper
   */
  public interface CommitHandler
  {
    public void handlePreferenceTask(PreferenceTask preferenceTask);
  }

  /**
   * @author Eike Stepper
   */
  private static final class EditorTransaction extends RecorderTransaction
  {
    private final IEditorPart editor;

    private final EditingDomain domain;

    private final boolean editorWasClean;

    public EditorTransaction(boolean user, IEditorPart editor, EditingDomain domain, Resource resource)
    {
      this(user, editor, domain, getRootObject(editor, resource));
    }

    public EditorTransaction(boolean user, IEditorPart editor, EditingDomain domain, SetupTaskContainer rootObject)
    {
      super(user, rootObject);
      this.editor = editor;
      this.domain = domain;
      editorWasClean = !editor.isDirty();

      // Only add policies to the user.
      if (user)
      {
        initializePolicies();
      }
    }

    @Override
    public IEditorPart getEditor()
    {
      return editor;
    }

    @Override
    public Boolean getPolicy(String key)
    {
      if (isUser())
      {
        return super.getPolicy(key);
      }

      return Boolean.TRUE;
    }

    @Override
    protected void doCommit(final Map<String, PreferenceTask> preferenceTasks)
    {
      ISelection selection = ((ISelectionProvider)editor).getSelection();
      final List<?> oldSelection = selection instanceof IStructuredSelection ? ((IStructuredSelection)selection).toList() : Collections.emptyList();

      final ChangeCommand command = new ChangeCommand(domain.getResourceSet())
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
          recorderObjects = applyChanges(preferenceTasks);
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

      UIUtil.syncExec(new Runnable()
      {
        public void run()
        {
          CommandStack commandStack = domain.getCommandStack();
          commandStack.execute(command);
          if (isUser() && editorWasClean && editor.isDirty())
          {
            editor.doSave(new NullProgressMonitor());
          }
        }
      });
    }

    private static SetupTaskContainer getRootObject(IEditorPart editor, Resource resource)
    {
      SetupTaskContainer rootObject = (SetupTaskContainer)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.SCOPE);
      if (!(rootObject instanceof User))
      {
        ISelection selection = ((ISelectionProvider)editor).getSelection();
        if (selection instanceof IStructuredSelection)
        {
          IStructuredSelection structuredSelection = (IStructuredSelection)selection;
          Object element = structuredSelection.getFirstElement();
          if (element instanceof EObject)
          {
            EObject eObject = (EObject)element;
            if (EcoreUtil.isAncestor(rootObject, eObject))
            {
              for (EObject eContainer = eObject; eContainer != null; eContainer = eContainer.eContainer())
              {
                if (eContainer instanceof SetupTaskContainer)
                {
                  return (SetupTaskContainer)eContainer;
                }
              }
            }
          }
        }
      }

      return rootObject;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static class ResourceTransaction extends RecorderTransaction
  {
    public ResourceTransaction(SetupTaskContainer rootObject)
    {
      super(true, rootObject);
      initializePolicies();
    }

    @Override
    protected void doCommit(Map<String, PreferenceTask> preferenceTasks)
    {
      List<? extends Object> recorderObjects = applyChanges(preferenceTasks);
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

  /**
   * @author Eike Stepper
   */
  private static final class TmpTransaction extends ResourceTransaction
  {
    public TmpTransaction(SetupTaskContainer rootObject)
    {
      super(rootObject);
    }

    @Override
    public void close()
    {
      // Do nothing.
    }
  }
}
