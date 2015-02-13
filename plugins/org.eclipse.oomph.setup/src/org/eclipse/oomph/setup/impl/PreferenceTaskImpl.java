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
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Eclipse Preference Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.PreferenceTaskImpl#getKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.PreferenceTaskImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PreferenceTaskImpl extends SetupTaskImpl implements PreferenceTask
{
  /**
   * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKey()
   * @generated
   * @ordered
   */
  protected static final String KEY_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getKey()
   * @generated
   * @ordered
   */
  protected String key = KEY_EDEFAULT;

  /**
   * The default value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
  protected static final String VALUE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getValue() <em>Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getValue()
   * @generated
   * @ordered
   */
  protected String value = VALUE_EDEFAULT;

  private transient PreferencesUtil.PreferenceProperty preferenceProperty;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected PreferenceTaskImpl()
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
    return SetupPackage.Literals.PREFERENCE_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getKey()
  {
    return key;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setKey(String newKey)
  {
    String oldKey = key;
    key = newKey;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.PREFERENCE_TASK__KEY, oldKey, key));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setValue(String newValue)
  {
    String oldValue = value;
    value = newValue;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.PREFERENCE_TASK__VALUE, oldValue, value));
    }
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
      case SetupPackage.PREFERENCE_TASK__KEY:
        return getKey();
      case SetupPackage.PREFERENCE_TASK__VALUE:
        return getValue();
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
      case SetupPackage.PREFERENCE_TASK__KEY:
        setKey((String)newValue);
        return;
      case SetupPackage.PREFERENCE_TASK__VALUE:
        setValue((String)newValue);
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
      case SetupPackage.PREFERENCE_TASK__KEY:
        setKey(KEY_EDEFAULT);
        return;
      case SetupPackage.PREFERENCE_TASK__VALUE:
        setValue(VALUE_EDEFAULT);
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
      case SetupPackage.PREFERENCE_TASK__KEY:
        return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
      case SetupPackage.PREFERENCE_TASK__VALUE:
        return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
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
    result.append(" (key: ");
    result.append(key);
    result.append(", value: ");
    result.append(value);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getPriority()
  {
    String key = getKey();
    return key != null && key.startsWith("/project") ? PRIORITY_LATE : PRIORITY_EARLY;
  }

  @Override
  public Object getOverrideToken()
  {
    String key = getKey();
    if (key == null)
    {
      return super.getOverrideToken();
    }

    return createToken(new Path(key).makeAbsolute().toString());
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 0;
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    preferenceProperty = new PreferencesUtil.PreferenceProperty(key);

    String oldValue = preferenceProperty.get(null);
    if (ObjectUtil.equals(getValue(), oldValue))
    {
      return false;
    }

    return true;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    String key = getKey();

    // Ignore project-specific preferences for projects that don't exist in the workspace.
    URI uri = PreferencesFactory.eINSTANCE.createURI(key);
    if ("project".equals(uri.authority()))
    {
      if (!ResourcesPlugin.getWorkspace().getRoot().getProject(URI.decode(uri.segment(0))).isAccessible())
      {
        context.log("Ignoring preference " + key + " = " + value);
        return;
      }
    }

    final String value = getValue();
    if (value != null && value.indexOf('\n') != -1)
    {
      context.log(value);
    }

    performUI(context, new RunnableWithContext()
    {
      public void run(SetupTaskContext context) throws Exception
      {
        preferenceProperty.set(value);
        preferenceProperty.getNode().flush();
      }
    });
  }
} // PreferenceTaskImpl
