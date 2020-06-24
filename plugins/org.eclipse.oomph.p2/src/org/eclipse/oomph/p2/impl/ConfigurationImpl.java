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
package org.eclipse.oomph.p2.impl;

import org.eclipse.oomph.base.impl.ModelElementImpl;
import org.eclipse.oomph.p2.Configuration;
import org.eclipse.oomph.p2.P2Package;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.p2.impl.ConfigurationImpl#getWS <em>WS</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.ConfigurationImpl#getOS <em>OS</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.impl.ConfigurationImpl#getArch <em>Arch</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConfigurationImpl extends ModelElementImpl implements Configuration
{
  /**
   * The default value of the '{@link #getWS() <em>WS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWS()
   * @generated
   * @ordered
   */
  protected static final String WS_EDEFAULT = "ANY"; //$NON-NLS-1$

  /**
   * The cached value of the '{@link #getWS() <em>WS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWS()
   * @generated
   * @ordered
   */
  protected String wS = WS_EDEFAULT;

  /**
   * The default value of the '{@link #getOS() <em>OS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOS()
   * @generated
   * @ordered
   */
  protected static final String OS_EDEFAULT = "ANY"; //$NON-NLS-1$

  /**
   * The cached value of the '{@link #getOS() <em>OS</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOS()
   * @generated
   * @ordered
   */
  protected String oS = OS_EDEFAULT;

  /**
   * The default value of the '{@link #getArch() <em>Arch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArch()
   * @generated
   * @ordered
   */
  protected static final String ARCH_EDEFAULT = "ANY"; //$NON-NLS-1$

  /**
   * The cached value of the '{@link #getArch() <em>Arch</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArch()
   * @generated
   * @ordered
   */
  protected String arch = ARCH_EDEFAULT;

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
    return P2Package.Literals.CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getWS()
  {
    return wS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setWS(String newWS)
  {
    String oldWS = wS;
    wS = newWS;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.CONFIGURATION__WS, oldWS, wS));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOS()
  {
    return oS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOS(String newOS)
  {
    String oldOS = oS;
    oS = newOS;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.CONFIGURATION__OS, oldOS, oS));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getArch()
  {
    return arch;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setArch(String newArch)
  {
    String oldArch = arch;
    arch = newArch;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, P2Package.CONFIGURATION__ARCH, oldArch, arch));
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
      case P2Package.CONFIGURATION__WS:
        return getWS();
      case P2Package.CONFIGURATION__OS:
        return getOS();
      case P2Package.CONFIGURATION__ARCH:
        return getArch();
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
      case P2Package.CONFIGURATION__WS:
        setWS((String)newValue);
        return;
      case P2Package.CONFIGURATION__OS:
        setOS((String)newValue);
        return;
      case P2Package.CONFIGURATION__ARCH:
        setArch((String)newValue);
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
      case P2Package.CONFIGURATION__WS:
        setWS(WS_EDEFAULT);
        return;
      case P2Package.CONFIGURATION__OS:
        setOS(OS_EDEFAULT);
        return;
      case P2Package.CONFIGURATION__ARCH:
        setArch(ARCH_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("null")
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case P2Package.CONFIGURATION__WS:
        return WS_EDEFAULT == null ? wS != null : !WS_EDEFAULT.equals(wS);
      case P2Package.CONFIGURATION__OS:
        return OS_EDEFAULT == null ? oS != null : !OS_EDEFAULT.equals(oS);
      case P2Package.CONFIGURATION__ARCH:
        return ARCH_EDEFAULT == null ? arch != null : !ARCH_EDEFAULT.equals(arch);
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
    result.append(" (wS: "); //$NON-NLS-1$
    result.append(wS);
    result.append(", oS: "); //$NON-NLS-1$
    result.append(oS);
    result.append(", arch: "); //$NON-NLS-1$
    result.append(arch);
    result.append(')');
    return result.toString();
  }

} // ConfigurationImpl
