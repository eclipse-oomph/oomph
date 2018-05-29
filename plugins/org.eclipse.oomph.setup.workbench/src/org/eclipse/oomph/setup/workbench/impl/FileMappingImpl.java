/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.workbench.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.setup.workbench.FileEditor;
import org.eclipse.oomph.setup.workbench.FileMapping;
import org.eclipse.oomph.setup.workbench.WorkbenchPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>File Mapping</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.workbench.impl.FileMappingImpl#getFilePattern <em>File Pattern</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.impl.FileMappingImpl#getDefaultEditorID <em>Default Editor ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workbench.impl.FileMappingImpl#getEditors <em>Editors</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FileMappingImpl extends ModelElementImpl implements FileMapping
{
  /**
   * The default value of the '{@link #getFilePattern() <em>File Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFilePattern()
   * @generated
   * @ordered
   */
  protected static final String FILE_PATTERN_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getFilePattern() <em>File Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFilePattern()
   * @generated
   * @ordered
   */
  protected String filePattern = FILE_PATTERN_EDEFAULT;

  /**
   * The default value of the '{@link #getDefaultEditorID() <em>Default Editor ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultEditorID()
   * @generated
   * @ordered
   */
  protected static final String DEFAULT_EDITOR_ID_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDefaultEditorID() <em>Default Editor ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDefaultEditorID()
   * @generated
   * @ordered
   */
  protected String defaultEditorID = DEFAULT_EDITOR_ID_EDEFAULT;

  /**
   * The cached value of the '{@link #getEditors() <em>Editors</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEditors()
   * @generated
   * @ordered
   */
  protected EList<FileEditor> editors;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FileMappingImpl()
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
    return WorkbenchPackage.Literals.FILE_MAPPING;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getFilePattern()
  {
    return filePattern;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFilePattern(String newFilePattern)
  {
    String oldFilePattern = filePattern;
    filePattern = newFilePattern;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, WorkbenchPackage.FILE_MAPPING__FILE_PATTERN, oldFilePattern, filePattern));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDefaultEditorID()
  {
    return defaultEditorID;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDefaultEditorID(String newDefaultEditorID)
  {
    String oldDefaultEditorID = defaultEditorID;
    defaultEditorID = newDefaultEditorID;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, WorkbenchPackage.FILE_MAPPING__DEFAULT_EDITOR_ID, oldDefaultEditorID, defaultEditorID));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<FileEditor> getEditors()
  {
    if (editors == null)
    {
      editors = new EObjectContainmentEList<FileEditor>(FileEditor.class, this, WorkbenchPackage.FILE_MAPPING__EDITORS);
    }
    return editors;
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
      case WorkbenchPackage.FILE_MAPPING__EDITORS:
        return ((InternalEList<?>)getEditors()).basicRemove(otherEnd, msgs);
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
      case WorkbenchPackage.FILE_MAPPING__FILE_PATTERN:
        return getFilePattern();
      case WorkbenchPackage.FILE_MAPPING__DEFAULT_EDITOR_ID:
        return getDefaultEditorID();
      case WorkbenchPackage.FILE_MAPPING__EDITORS:
        return getEditors();
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
      case WorkbenchPackage.FILE_MAPPING__FILE_PATTERN:
        setFilePattern((String)newValue);
        return;
      case WorkbenchPackage.FILE_MAPPING__DEFAULT_EDITOR_ID:
        setDefaultEditorID((String)newValue);
        return;
      case WorkbenchPackage.FILE_MAPPING__EDITORS:
        getEditors().clear();
        getEditors().addAll((Collection<? extends FileEditor>)newValue);
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
      case WorkbenchPackage.FILE_MAPPING__FILE_PATTERN:
        setFilePattern(FILE_PATTERN_EDEFAULT);
        return;
      case WorkbenchPackage.FILE_MAPPING__DEFAULT_EDITOR_ID:
        setDefaultEditorID(DEFAULT_EDITOR_ID_EDEFAULT);
        return;
      case WorkbenchPackage.FILE_MAPPING__EDITORS:
        getEditors().clear();
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
      case WorkbenchPackage.FILE_MAPPING__FILE_PATTERN:
        return FILE_PATTERN_EDEFAULT == null ? filePattern != null : !FILE_PATTERN_EDEFAULT.equals(filePattern);
      case WorkbenchPackage.FILE_MAPPING__DEFAULT_EDITOR_ID:
        return DEFAULT_EDITOR_ID_EDEFAULT == null ? defaultEditorID != null : !DEFAULT_EDITOR_ID_EDEFAULT.equals(defaultEditorID);
      case WorkbenchPackage.FILE_MAPPING__EDITORS:
        return editors != null && !editors.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (filePattern: ");
    result.append(filePattern);
    result.append(", defaultEditorID: ");
    result.append(defaultEditorID);
    result.append(')');
    return result.toString();
  }

} // FileMappingImpl
