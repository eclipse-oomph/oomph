/*
 * Copyright (c) 2016, 2018 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.setup.Configuration;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Workspace;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.ConfigurationImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ConfigurationImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ConfigurationImpl#getInstallation <em>Installation</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ConfigurationImpl#getWorkspace <em>Workspace</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConfigurationImpl extends ModelElementImpl implements Configuration
{
  /**
   * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLabel()
   * @generated
   * @ordered
   */
  protected static final String LABEL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLabel()
   * @generated
   * @ordered
   */
  protected String label = LABEL_EDEFAULT;

  /**
   * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDescription()
   * @generated
   * @ordered
   */
  protected static final String DESCRIPTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDescription()
   * @generated
   * @ordered
   */
  protected String description = DESCRIPTION_EDEFAULT;

  /**
   * The cached value of the '{@link #getInstallation() <em>Installation</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInstallation()
   * @generated
   * @ordered
   */
  protected Installation installation;

  /**
   * The cached value of the '{@link #getWorkspace() <em>Workspace</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWorkspace()
   * @generated
   * @ordered
   */
  protected Workspace workspace;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ConfigurationImpl()
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
    return SetupPackage.Literals.CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLabel()
  {
    return label;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLabel(String newLabel)
  {
    String oldLabel = label;
    label = newLabel;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.CONFIGURATION__LABEL, oldLabel, label));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setDescription(String newDescription)
  {
    String oldDescription = description;
    description = newDescription;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.CONFIGURATION__DESCRIPTION, oldDescription, description));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Installation getInstallation()
  {
    if (installation != null && installation.eIsProxy())
    {
      InternalEObject oldInstallation = (InternalEObject)installation;
      installation = (Installation)eResolveProxy(oldInstallation);
      if (installation != oldInstallation)
      {
        InternalEObject newInstallation = (InternalEObject)installation;
        NotificationChain msgs = oldInstallation.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SetupPackage.CONFIGURATION__INSTALLATION, null, null);
        if (newInstallation.eInternalContainer() == null)
        {
          msgs = newInstallation.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SetupPackage.CONFIGURATION__INSTALLATION, null, msgs);
        }
        if (msgs != null)
        {
          msgs.dispatch();
        }
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SetupPackage.CONFIGURATION__INSTALLATION, oldInstallation, installation));
        }
      }
    }
    return installation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Installation basicGetInstallation()
  {
    return installation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetInstallation(Installation newInstallation, NotificationChain msgs)
  {
    Installation oldInstallation = installation;
    installation = newInstallation;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SetupPackage.CONFIGURATION__INSTALLATION, oldInstallation,
          newInstallation);
      if (msgs == null)
      {
        msgs = notification;
      }
      else
      {
        msgs.add(notification);
      }
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setInstallation(Installation newInstallation)
  {
    if (newInstallation != installation)
    {
      NotificationChain msgs = null;
      if (installation != null)
      {
        msgs = ((InternalEObject)installation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SetupPackage.CONFIGURATION__INSTALLATION, null, msgs);
      }
      if (newInstallation != null)
      {
        msgs = ((InternalEObject)newInstallation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SetupPackage.CONFIGURATION__INSTALLATION, null, msgs);
      }
      msgs = basicSetInstallation(newInstallation, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.CONFIGURATION__INSTALLATION, newInstallation, newInstallation));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Workspace getWorkspace()
  {
    if (workspace != null && workspace.eIsProxy())
    {
      InternalEObject oldWorkspace = (InternalEObject)workspace;
      workspace = (Workspace)eResolveProxy(oldWorkspace);
      if (workspace != oldWorkspace)
      {
        InternalEObject newWorkspace = (InternalEObject)workspace;
        NotificationChain msgs = oldWorkspace.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SetupPackage.CONFIGURATION__WORKSPACE, null, null);
        if (newWorkspace.eInternalContainer() == null)
        {
          msgs = newWorkspace.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SetupPackage.CONFIGURATION__WORKSPACE, null, msgs);
        }
        if (msgs != null)
        {
          msgs.dispatch();
        }
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SetupPackage.CONFIGURATION__WORKSPACE, oldWorkspace, workspace));
        }
      }
    }
    return workspace;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Workspace basicGetWorkspace()
  {
    return workspace;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetWorkspace(Workspace newWorkspace, NotificationChain msgs)
  {
    Workspace oldWorkspace = workspace;
    workspace = newWorkspace;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SetupPackage.CONFIGURATION__WORKSPACE, oldWorkspace, newWorkspace);
      if (msgs == null)
      {
        msgs = notification;
      }
      else
      {
        msgs.add(notification);
      }
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setWorkspace(Workspace newWorkspace)
  {
    if (newWorkspace != workspace)
    {
      NotificationChain msgs = null;
      if (workspace != null)
      {
        msgs = ((InternalEObject)workspace).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SetupPackage.CONFIGURATION__WORKSPACE, null, msgs);
      }
      if (newWorkspace != null)
      {
        msgs = ((InternalEObject)newWorkspace).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SetupPackage.CONFIGURATION__WORKSPACE, null, msgs);
      }
      msgs = basicSetWorkspace(newWorkspace, msgs);
      if (msgs != null)
      {
        msgs.dispatch();
      }
    }
    else if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.CONFIGURATION__WORKSPACE, newWorkspace, newWorkspace));
    }
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
      case SetupPackage.CONFIGURATION__INSTALLATION:
        return basicSetInstallation(null, msgs);
      case SetupPackage.CONFIGURATION__WORKSPACE:
        return basicSetWorkspace(null, msgs);
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
      case SetupPackage.CONFIGURATION__LABEL:
        return getLabel();
      case SetupPackage.CONFIGURATION__DESCRIPTION:
        return getDescription();
      case SetupPackage.CONFIGURATION__INSTALLATION:
        if (resolve)
        {
          return getInstallation();
        }
        return basicGetInstallation();
      case SetupPackage.CONFIGURATION__WORKSPACE:
        if (resolve)
        {
          return getWorkspace();
        }
        return basicGetWorkspace();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SetupPackage.CONFIGURATION__LABEL:
        setLabel((String)newValue);
        return;
      case SetupPackage.CONFIGURATION__DESCRIPTION:
        setDescription((String)newValue);
        return;
      case SetupPackage.CONFIGURATION__INSTALLATION:
        setInstallation((Installation)newValue);
        return;
      case SetupPackage.CONFIGURATION__WORKSPACE:
        setWorkspace((Workspace)newValue);
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
      case SetupPackage.CONFIGURATION__LABEL:
        setLabel(LABEL_EDEFAULT);
        return;
      case SetupPackage.CONFIGURATION__DESCRIPTION:
        setDescription(DESCRIPTION_EDEFAULT);
        return;
      case SetupPackage.CONFIGURATION__INSTALLATION:
        setInstallation((Installation)null);
        return;
      case SetupPackage.CONFIGURATION__WORKSPACE:
        setWorkspace((Workspace)null);
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
      case SetupPackage.CONFIGURATION__LABEL:
        return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
      case SetupPackage.CONFIGURATION__DESCRIPTION:
        return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
      case SetupPackage.CONFIGURATION__INSTALLATION:
        return installation != null;
      case SetupPackage.CONFIGURATION__WORKSPACE:
        return workspace != null;
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

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (label: "); //$NON-NLS-1$
    result.append(label);
    result.append(", description: "); //$NON-NLS-1$
    result.append(description);
    result.append(')');
    return result.toString();
  }

} // ConfigurationImpl
