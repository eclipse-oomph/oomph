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
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Workspace;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import java.util.Collection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Installation To Workspaces Map Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.InstallationToWorkspacesMapEntryImpl#getTypedKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.InstallationToWorkspacesMapEntryImpl#getTypedValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InstallationToWorkspacesMapEntryImpl extends MinimalEObjectImpl.Container implements BasicEMap.Entry<Installation, EList<Workspace>>
{
  /**
   * The cached value of the '{@link #getTypedKey() <em>Key</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTypedKey()
   * @generated
   * @ordered
   */
  protected Installation key;

  /**
   * The cached value of the '{@link #getTypedValue() <em>Value</em>}' reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTypedValue()
   * @generated
   * @ordered
   */
  protected EList<Workspace> value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected InstallationToWorkspacesMapEntryImpl()
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
    return SetupPackage.Literals.INSTALLATION_TO_WORKSPACES_MAP_ENTRY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Installation getTypedKey()
  {
    if (key != null && key.eIsProxy())
    {
      InternalEObject oldKey = (InternalEObject)key;
      key = (Installation)eResolveProxy(oldKey);
      if (key != oldKey)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY__KEY, oldKey, key));
        }
      }
    }
    return key;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Installation basicGetTypedKey()
  {
    return key;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTypedKey(Installation newKey)
  {
    Installation oldKey = key;
    key = newKey;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY__KEY, oldKey, key));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Workspace> getTypedValue()
  {
    if (value == null)
    {
      value = new EObjectResolvingEList<Workspace>(Workspace.class, this, SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY__VALUE);
    }
    return value;
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
      case SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY__KEY:
        if (resolve)
        {
          return getTypedKey();
        }
        return basicGetTypedKey();
      case SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY__VALUE:
        return getTypedValue();
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
      case SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY__KEY:
        setTypedKey((Installation)newValue);
        return;
      case SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY__VALUE:
        getTypedValue().clear();
        getTypedValue().addAll((Collection<? extends Workspace>)newValue);
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
      case SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY__KEY:
        setTypedKey((Installation)null);
        return;
      case SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY__VALUE:
        getTypedValue().clear();
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
      case SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY__KEY:
        return key != null;
      case SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY__VALUE:
        return value != null && !value.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected int hash = -1;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getHash()
  {
    if (hash == -1)
    {
      Object theKey = getKey();
      hash = theKey == null ? 0 : theKey.hashCode();
    }
    return hash;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setHash(int hash)
  {
    this.hash = hash;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Installation getKey()
  {
    return getTypedKey();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setKey(Installation key)
  {
    setTypedKey(key);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Workspace> getValue()
  {
    return getTypedValue();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Workspace> setValue(EList<Workspace> value)
  {
    EList<Workspace> oldValue = getValue();
    getTypedValue().clear();
    getTypedValue().addAll(value);
    return oldValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  public EMap<Installation, EList<Workspace>> getEMap()
  {
    EObject container = eContainer();
    return container == null ? null : (EMap<Installation, EList<Workspace>>)container.eGet(eContainmentFeature());
  }

} // InstallationToWorkspacesMapEntryImpl
