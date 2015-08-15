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
  public EList<FileMapping> getMappings()
  {
    if (mappings == null)
    {
      mappings = new EObjectContainmentEList<FileMapping>(FileMapping.class, this, WorkbenchPackage.FILE_ASSOCIATIONS_TASK__MAPPINGS);
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
    Map<String, FileMapping> existingMappings = new HashMap<String, FileMapping>();
    for (Iterator<FileMapping> it = getMappings().iterator(); it.hasNext();)
    {
      FileMapping mapping = it.next();
      String filePattern = mapping.getFilePattern();

      FileMapping existingMapping = existingMappings.get(filePattern);
      if (existingMapping != null)
      {
        EList<FileEditor> existingEditors = existingMapping.getEditors();

        LinkedHashSet<FileEditor> editors = new LinkedHashSet<FileEditor>(existingEditors);
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
          name = "." + extension;
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

      String defaultEditorID = mapping.getDefaultEditorID();
      if (!StringUtil.isEmpty(defaultEditorID))
      {
        IEditorDescriptor defaultEditor = registry.findEditor(defaultEditorID);
        if (defaultEditor != null)
        {
          try
          {
            fileEditorMapping.setDefaultEditor(defaultEditor);
          }
          catch (NoSuchMethodError ex)
          {
            // Before Neon the setDefaultEditor() method took an EditorDescriptor-typed argument.
            if (defaultEditor instanceof EditorDescriptor)
            {
              EditorDescriptor descriptor = (EditorDescriptor)defaultEditor;
              Method method = ReflectUtil.getMethod(FileEditorMapping.class, "setDefaultEditor", EditorDescriptor.class);
              ReflectUtil.invokeMethod(method, fileEditorMapping, descriptor);
            }
          }
        }
      }
    }

    performUI(context, new RunnableWithContext()
    {
      public void run(SetupTaskContext context) throws Exception
      {
        registry.setFileEditorMappings(fileEditorMappings.values().toArray(new FileEditorMapping[fileEditorMappings.size()]));
        registry.saveAssociations();

        PrefUtil.savePrefs();
      }
    });
  }

  private static Map<String, FileEditorMapping> getFileEditorMappings()
  {
    Map<String, FileEditorMapping> result = new HashMap<String, FileEditorMapping>();
    IFileEditorMapping[] mappings = PlatformUI.getWorkbench().getEditorRegistry().getFileEditorMappings();
    for (int i = 0; i < mappings.length; i++)
    {
      IFileEditorMapping mapping = mappings[i];
      if (mapping instanceof FileEditorMapping)
      {
        String name = mapping.getName();
        String extension = mapping.getExtension();

        String pattern = name + (StringUtil.isEmpty(extension) ? "" : "." + extension);
        result.put(pattern, (FileEditorMapping)mapping);
      }
    }

    return result;
  }

  private Set<String> getEditorIDs(IFileEditorMapping mapping)
  {
    Set<String> ids = new HashSet<String>();

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
