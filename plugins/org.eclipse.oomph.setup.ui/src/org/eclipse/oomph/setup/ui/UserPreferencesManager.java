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
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.util.PreferencesRecorder;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class UserPreferencesManager
{
  public static final UserPreferencesManager INSTANCE = new UserPreferencesManager();

  public static final String[][] CHOICES = { { "Always prompt", "" }, { "Don't store", Storage.NONE.toString() },
      { "Store into model", Storage.MODEL.toString() }, { "Store into editor", Storage.EDITOR.toString() } };

  private UserPreferencesManager()
  {
  }

  public void register(Display display)
  {
    display.addListener(SWT.Skin, new Listener()
    {
      public void handleEvent(Event event)
      {
        if (event.widget instanceof Shell)
        {
          final Shell shell = (Shell)event.widget;
          if (isPreferenceDialog(shell))
          {
            final PreferencesRecorder recorder = new PreferencesRecorder();

            shell.addDisposeListener(new DisposeListener()
            {
              public void widgetDisposed(DisposeEvent e)
              {
                Map<URI, String> values = recorder.done();

                for (Iterator<URI> it = values.keySet().iterator(); it.hasNext();)
                {
                  URI uri = it.next();
                  String pluginID = uri.segment(0);

                  if (SetupUIPlugin.INSTANCE.getSymbolicName().equals(pluginID))
                  {
                    it.remove();
                  }
                }

                if (!values.isEmpty())
                {
                  storePreferences(shell, values);
                }
              }
            });
          }
        }
      }

      @SuppressWarnings("restriction")
      private boolean isPreferenceDialog(Shell shell)
      {
        Object data = shell.getData();
        return data instanceof org.eclipse.ui.internal.dialogs.WorkbenchPreferenceDialog;
      }
    });
  }

  private void storePreferences(Shell shell, final Map<URI, String> values)
  {
    Storage storage = getStorage();
    if (storage == null)
    {
      UserPreferencesStorageDialog dialog = new UserPreferencesStorageDialog(shell);
      if (dialog.open() != UserPreferencesStorageDialog.OK)
      {
        return;
      }

      storage = dialog.getStorage();

      if (dialog.isRemember())
      {
        setStorage(storage);
      }
    }

    if (storage == Storage.NONE)
    {
      return;
    }

    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    final URI userSetupURI = getUserSetupURI();

    if (storage == Storage.EDITOR)
    {
      SetupEditorSupport.getEditor(page, userSetupURI, true, new SetupEditorSupport.LoadHandler()
      {
        @Override
        protected void loaded(IEditorPart editor, EditingDomain domain, Resource resource)
        {
          storePreferences(editor, domain, resource, false, values);
        }
      });

      return;
    }

    IEditorPart editor = SetupEditorSupport.getEditor(page, userSetupURI, false, new SetupEditorSupport.LoadHandler()
    {
      @Override
      protected void loaded(IEditorPart editor, EditingDomain domain, Resource resource)
      {
        storePreferences(editor, domain, resource, true, values);
      }
    });

    if (editor != null)
    {
      return;
    }

    ResourceSet resourceSet = org.eclipse.oomph.setup.internal.core.util.SetupUtil.createResourceSet();
    Resource resource = resourceSet.getResource(userSetupURI, true);
    List<PreferenceTask> preferenceTasks = storePreferences(resource, values);
    if (preferenceTasks != null)
    {
      try
      {
        resource.save(null);
      }
      catch (IOException ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
      }
    }
  }

  private void storePreferences(IEditorPart editor, EditingDomain domain, final Resource resource, boolean tryToSave, final Map<URI, String> values)
  {
    boolean wasDirty = editor.isDirty();

    ISelection selection = ((ISelectionProvider)editor).getSelection();
    final List<?> oldSelection = selection instanceof IStructuredSelection ? ((IStructuredSelection)selection).toList() : Collections.emptyList();

    ChangeCommand command = new ChangeCommand(domain.getResourceSet())
    {
      List<PreferenceTask> preferenceTasks = Collections.emptyList();

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
        preferenceTasks = storePreferences(resource, values);
        affectedObjects = preferenceTasks;
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
        affectedObjects = preferenceTasks;
      }
    };

    CommandStack commandStack = domain.getCommandStack();
    commandStack.execute(command);

    if (tryToSave && !wasDirty)
    {
      editor.doSave(new NullProgressMonitor());
    }
  }

  private List<PreferenceTask> storePreferences(Resource resource, Map<URI, String> values)
  {
    EObject rootObject = resource.getContents().get(0);
    if (rootObject instanceof SetupTaskContainer)
    {
      SetupTaskContainer container = (SetupTaskContainer)rootObject;

      CompoundTask preferencesCompound = getPreferencesCompound(container);
      if (preferencesCompound == null)
      {
        preferencesCompound = SetupFactory.eINSTANCE.createCompoundTask("User Preferences");
        preferencesCompound.getAnnotations().add(BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_USER_PREFERENCES));
        container.getSetupTasks().add(0, preferencesCompound);

        migrateOldTasks(preferencesCompound, container);
      }

      return storePreferences(preferencesCompound, values);
    }

    return null;
  }

  private List<PreferenceTask> storePreferences(CompoundTask preferencesCompound, Map<URI, String> values)
  {
    List<PreferenceTask> preferenceTasks = new ArrayList<PreferenceTask>();
    for (Map.Entry<URI, String> entry : values.entrySet())
    {
      URI key = entry.getKey();
      String value = entry.getValue();

      String pluginID = key.segment(0).toString();
      String path = PreferencesFactory.eINSTANCE.convertURI(key);

      CompoundTask pluginCompound = (CompoundTask)findOrCreateTask(preferencesCompound.getSetupTasks(), SetupPackage.Literals.COMPOUND_TASK__NAME, pluginID);

      PreferenceTask preferenceTask = (PreferenceTask)findOrCreateTask(pluginCompound.getSetupTasks(), SetupPackage.Literals.PREFERENCE_TASK__KEY, path);
      preferenceTask.setValue(SetupUtil.escape(value));

      preferenceTasks.add(preferenceTask);
    }

    return preferenceTasks;
  }

  private void migrateOldTasks(CompoundTask preferencesCompound, SetupTaskContainer container)
  {
    migrateOldTasksRecursively(preferencesCompound, container);

    EList<SetupTask> pluginCompounds = preferencesCompound.getSetupTasks();
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

  private void migrateOldTasksRecursively(CompoundTask preferencesCompound, SetupTaskContainer container)
  {
    if (container == preferencesCompound)
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
        CompoundTask pluginCompound = (CompoundTask)findOrCreateTask(preferencesCompound.getSetupTasks(), SetupPackage.Literals.COMPOUND_TASK__NAME, pluginID);
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
        migrateOldTasksRecursively(preferencesCompound, (SetupTaskContainer)object);
      }
    }
  }

  private URI getUserSetupURI()
  {
    return SetupContext.USER_SETUP_URI;
  }

  private static CompoundTask getPreferencesCompound(SetupTaskContainer container)
  {
    if (container instanceof CompoundTask)
    {
      CompoundTask compound = (CompoundTask)container;
      if (compound.getAnnotation(AnnotationConstants.ANNOTATION_USER_PREFERENCES) != null)
      {
        return compound;
      }
    }

    for (SetupTask setupTask : container.getSetupTasks())
    {
      if (setupTask instanceof SetupTaskContainer)
      {
        CompoundTask compound = getPreferencesCompound((SetupTaskContainer)setupTask);
        if (compound != null)
        {
          return compound;
        }
      }
    }

    return null;
  }

  private static SetupTask findOrCreateTask(EList<SetupTask> tasks, EAttribute key, String value)
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

    EClass eClass = key.getEContainingClass();
    SetupTask task = (SetupTask)EcoreUtil.create(eClass);
    task.eSet(key, value);
    tasks.add(position, task);
    return task;
  }

  private static Storage getStorage()
  {
    try
    {
      IPreferenceStore preferenceStore = SetupUIPlugin.INSTANCE.getPreferenceStore();
      String value = preferenceStore.getString(SetupUIPlugin.PREF_USER_PREFERENCES_STORAGE);
      return Storage.valueOf(value);
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  private static void setStorage(Storage storage)
  {
    IPreferenceStore preferenceStore = SetupUIPlugin.INSTANCE.getPreferenceStore();
    preferenceStore.putValue(SetupUIPlugin.PREF_USER_PREFERENCES_STORAGE, storage.toString());
  }

  /**
   * @author Eike Stepper
   */
  public static enum Storage
  {
    NONE, EDITOR, MODEL
  }
}
