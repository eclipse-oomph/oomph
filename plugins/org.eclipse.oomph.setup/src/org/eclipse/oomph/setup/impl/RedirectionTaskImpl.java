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
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.setup.RedirectionTask;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTaskContext;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Redirection Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.RedirectionTaskImpl#getSourceURL <em>Source URL</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.RedirectionTaskImpl#getTargetURL <em>Target URL</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RedirectionTaskImpl extends SetupTaskImpl implements RedirectionTask
{
  /**
   * The default value of the '{@link #getSourceURL() <em>Source URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSourceURL()
   * @generated
   * @ordered
   */
  protected static final String SOURCE_URL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSourceURL() <em>Source URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSourceURL()
   * @generated
   * @ordered
   */
  protected String sourceURL = SOURCE_URL_EDEFAULT;

  /**
   * The default value of the '{@link #getTargetURL() <em>Target URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetURL()
   * @generated
   * @ordered
   */
  protected static final String TARGET_URL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getTargetURL() <em>Target URL</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetURL()
   * @generated
   * @ordered
   */
  protected String targetURL = TARGET_URL_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RedirectionTaskImpl()
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
    return SetupPackage.Literals.REDIRECTION_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSourceURL()
  {
    return sourceURL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSourceURL(String newSourceURL)
  {
    String oldSourceURL = sourceURL;
    sourceURL = newSourceURL;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.REDIRECTION_TASK__SOURCE_URL, oldSourceURL, sourceURL));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getTargetURL()
  {
    return targetURL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTargetURL(String newTargetURL)
  {
    String oldTargetURL = targetURL;
    targetURL = newTargetURL;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.REDIRECTION_TASK__TARGET_URL, oldTargetURL, targetURL));
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
      case SetupPackage.REDIRECTION_TASK__SOURCE_URL:
        return getSourceURL();
      case SetupPackage.REDIRECTION_TASK__TARGET_URL:
        return getTargetURL();
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
      case SetupPackage.REDIRECTION_TASK__SOURCE_URL:
        setSourceURL((String)newValue);
        return;
      case SetupPackage.REDIRECTION_TASK__TARGET_URL:
        setTargetURL((String)newValue);
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
      case SetupPackage.REDIRECTION_TASK__SOURCE_URL:
        setSourceURL(SOURCE_URL_EDEFAULT);
        return;
      case SetupPackage.REDIRECTION_TASK__TARGET_URL:
        setTargetURL(TARGET_URL_EDEFAULT);
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
      case SetupPackage.REDIRECTION_TASK__SOURCE_URL:
        return SOURCE_URL_EDEFAULT == null ? sourceURL != null : !SOURCE_URL_EDEFAULT.equals(sourceURL);
      case SetupPackage.REDIRECTION_TASK__TARGET_URL:
        return TARGET_URL_EDEFAULT == null ? targetURL != null : !TARGET_URL_EDEFAULT.equals(targetURL);
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
    result.append(" (sourceURL: ");
    result.append(sourceURL);
    result.append(", targetURL: ");
    result.append(targetURL);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getPriority()
  {
    return PRIORITY_REDIRECTION;
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 0;
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    return false;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    throw new UnsupportedOperationException("Redirection task can't be executed");
  }

} // RedirectionTaskImpl
