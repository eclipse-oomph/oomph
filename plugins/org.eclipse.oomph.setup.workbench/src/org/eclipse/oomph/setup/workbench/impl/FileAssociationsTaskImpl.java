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
package org.eclipse.oomph.setup.workbench.impl;

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.workbench.FileAssociationsTask;
import org.eclipse.oomph.setup.workbench.FileEditor;
import org.eclipse.oomph.setup.workbench.FileMapping;
import org.eclipse.oomph.setup.workbench.WorkbenchPackage;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IFileEditorMapping;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.registry.EditorDescriptor;
import org.eclipse.ui.internal.registry.EditorRegistry;
import org.eclipse.ui.internal.registry.FileEditorMapping;
import org.eclipse.ui.internal.util.PrefUtil;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>File Associations Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.workbench.impl.FileAssociationsTaskImpl#getMappings <em>Mappings</em>}</li>
 * </ul>
 *
 * @generated
 */
@SuppressWarnings("restriction")
public class FileAssociationsTaskImpl extends SetupTaskImpl implements FileAssociationsTask
{
  private static final Method SET_DEFAULT_EDITOR_METHOD = determineSetDefaultEditorMethod();

  /**
   * The cached value of the '{@link #getMappings() <em>Mappings</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMappings()
   * @generated
   * @ordered
   */
  protected EList<FileMapping> mappings;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FileAssociationsTaskImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return WorkbenchPackage.Literals.FILE_ASSOCIATIONS_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<FileMapping> getMappings()
  {
    if (mappings == null)
    {
      mappings = new EObjectContainmentEList<>(FileMapping.class, this, WorkbenchPackage.FILE_ASSOCIATIONS_TASK__MAPPINGS);
    }
    return mappings;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case WorkbenchPackage.FILE_ASSOCIATIONS_TASK__MAPPINGS:
        return ((InternalEList<?>)getMappings()).basicRemove(otherEnd, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case WorkbenchPackage.FILE_ASSOCIATIONS_TASK__MAPPINGS:
        return getMappings();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case WorkbenchPackage.FILE_ASSOCIATIONS_TASK__MAPPINGS:
        getMappings().clear();
        getMappings().addAll((Collection<? extends FileMapping>)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case WorkbenchPackage.FILE_ASSOCIATIONS_TASK__MAPPINGS:
        getMappings().clear();
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case WorkbenchPackage.FILE_ASSOCIATIONS_TASK__MAPPINGS:
        return mappings != null && !mappings.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  @Override
  public Object getOverrideToken()
  {
    return getClass();
  }

  @Override
  public void overrideFor(SetupTask overriddenSetupTask)
  {
    super.overrideFor(overriddenSetupTask);

    FileAssociationsTask overriddenFileAssociationsTask = (FileAssociationsTask)overriddenSetupTask;
    getMappings().addAll(overriddenFileAssociationsTask.getMappings());
  }

  @Override
  public void consolidate()
  {
    Map<String, FileMapping> existingMappings = new HashMap<>();
    for (Iterator<FileMapping> it = getMappings().iterator(); it.hasNext();)
    {
      FileMapping mapping = it.next();
      String filePattern = mapping.getFilePattern();

      FileMapping existingMapping = existingMappings.get(filePattern);
      if (existingMapping != null)
      {
        EList<FileEditor> existingEditors = existingMapping.getEditors();

        LinkedHashSet<FileEditor> editors = new LinkedHashSet<>(existingEditors);
        editors.addAll(mapping.getEditors());

        existingEditors.clear();
        existingEditors.addAll(editors);

        String defaultEditorID = mapping.getDefaultEditorID();
        if (!StringUtil.isEmpty(defaultEditorID))
        {
          existingMapping.setDefaultEditorID(defaultEditorID);
        }

        it.remove();
      }
      else
      {
        existingMappings.put(filePattern, mapping);
      }
    }
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 0;
  }

  @Override
  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    Map<String, FileEditorMapping> fileEditorMappings = getFileEditorMappings();

    for (FileMapping mapping : getMappings())
    {
      FileEditorMapping fileEditorMapping = fileEditorMappings.get(mapping.getFilePattern());
      if (fileEditorMapping == null)
      {
        return true;
      }

      EditorRegistry registry = (EditorRegistry)PlatformUI.getWorkbench().getEditorRegistry();

      String defaultEditorID = mapping.getDefaultEditorID();
      if (!StringUtil.isEmpty(defaultEditorID))
      {
        IEditorDescriptor defaultEditor = registry.findEditor(defaultEditorID);
        if (defaultEditor != null)
        {
          IEditorDescriptor mappingDefaultEditor = fileEditorMapping.getDefaultEditor();
          String mappingDefaultEditorID = mappingDefaultEditor == null ? null : mappingDefaultEditor.getId();
          if (!ObjectUtil.equals(mappingDefaultEditorID, defaultEditorID))
          {
            return true;
          }
        }
      }

      Set<String> editorIDs = getEditorIDs(fileEditorMapping);
      for (FileEditor fileEditor : mapping.getEditors())
      {
        String editorID = fileEditor.getID();
        if (registry.findEditor(editorID) != null && !editorIDs.contains(editorID))
        {
          return true;
        }
      }
    }

    return false;
  }

  @Override
  public void perform(SetupTaskContext context) throws Exception
  {
    final EditorRegistry registry = (EditorRegistry)PlatformUI.getWorkbench().getEditorRegistry();
    final Map<String, FileEditorMapping> fileEditorMappings = getFileEditorMappings();

    for (FileMapping mapping : getMappings())
    {
      String filePattern = mapping.getFilePattern();
      FileEditorMapping fileEditorMapping = fileEditorMappings.get(filePattern);
      if (fileEditorMapping == null)
      {
        int lastDot = filePattern.lastIndexOf('.');
        String name = lastDot == -1 ? filePattern : filePattern.substring(0, lastDot);
        String extension = lastDot == -1 ? null : filePattern.substring(lastDot + 1);

        if (StringUtil.isEmpty(name))
        {
          name = "." + extension; //$NON-NLS-1$
          extension = null;
        }

        fileEditorMapping = new FileEditorMapping(name, extension);
        fileEditorMappings.put(filePattern, fileEditorMapping);
      }

      Set<String> editorIDs = getEditorIDs(fileEditorMapping);
      for (FileEditor fileEditor : mapping.getEditors())
      {
        String editorID = fileEditor.getID();
        if (!editorIDs.contains(editorID))
        {
          IEditorDescriptor editor = registry.findEditor(editorID);
          if (editor instanceof EditorDescriptor)
          {
            EditorDescriptor descriptor = (EditorDescriptor)editor;
            fileEditorMapping.addEditor(descriptor);
          }
        }
      }

      if (SET_DEFAULT_EDITOR_METHOD != null)
      {
        String defaultEditorID = mapping.getDefaultEditorID();
        if (!StringUtil.isEmpty(defaultEditorID))
        {
          IEditorDescriptor defaultEditor = registry.findEditor(defaultEditorID);
          if (defaultEditor != null)
          {
            try
            {
              ReflectUtil.invokeMethod(SET_DEFAULT_EDITOR_METHOD, fileEditorMapping, defaultEditor);
            }
            catch (Throwable ex)
            {
              context.log(ex.getMessage());
            }
          }
        }
      }
    }

    performUI(context, new RunnableWithContext()
    {
      @Override
      public void run(SetupTaskContext context) throws Exception
      {
        registry.setFileEditorMappings(fileEditorMappings.values().toArray(new FileEditorMapping[fileEditorMappings.size()]));
        registry.saveAssociations();

        PrefUtil.savePrefs();
      }
    });
  }

  private static Method determineSetDefaultEditorMethod()
  {
    // Starting with Neon the setDefaultEditor() method takes an IEditorDescriptor-typed argument.
    Method method = determineSetDefaultEditorMethod(IEditorDescriptor.class);
    if (method == null)
    {
      // Before Neon the setDefaultEditor() method took an EditorDescriptor-typed argument.
      method = determineSetDefaultEditorMethod(EditorDescriptor.class);
    }

    return method;
  }

  private static Method determineSetDefaultEditorMethod(Class<?> type)
  {
    try
    {
      return ReflectUtil.getMethod(FileEditorMapping.class, "setDefaultEditor", type); //$NON-NLS-1$
    }
    catch (Throwable ex)
    {
      return null;
    }
  }

  private static Map<String, FileEditorMapping> getFileEditorMappings()
  {
    Map<String, FileEditorMapping> result = new HashMap<>();
    IFileEditorMapping[] mappings = PlatformUI.getWorkbench().getEditorRegistry().getFileEditorMappings();
    for (int i = 0; i < mappings.length; i++)
    {
      IFileEditorMapping mapping = mappings[i];
      if (mapping instanceof FileEditorMapping)
      {
        String name = mapping.getName();
        String extension = mapping.getExtension();

        String pattern = name + (StringUtil.isEmpty(extension) ? "" : "." + extension); //$NON-NLS-1$ //$NON-NLS-2$
        result.put(pattern, (FileEditorMapping)mapping);
      }
    }

    return result;
  }

  private Set<String> getEditorIDs(IFileEditorMapping mapping)
  {
    Set<String> ids = new HashSet<>();

    IEditorDescriptor[] editors = mapping.getEditors();
    for (int i = 0; i < editors.length; i++)
    {
      IEditorDescriptor editor = editors[i];
      String editorID = editor.getId();
      ids.add(editorID);
    }

    return ids;
  }

} // FileAssociationsTaskImpl
