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

import org.eclipse.oomph.setup.EclipseIniTask;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Eclipse Ini Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.EclipseIniTaskImpl#getOption <em>Option</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.EclipseIniTaskImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.EclipseIniTaskImpl#isVm <em>Vm</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EclipseIniTaskImpl extends SetupTaskImpl implements EclipseIniTask
{
  /**
   * The default value of the '{@link #getOption() <em>Option</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOption()
   * @generated
   * @ordered
   */
  protected static final String OPTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOption() <em>Option</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOption()
   * @generated
   * @ordered
   */
  protected String option = OPTION_EDEFAULT;

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

  /**
   * The default value of the '{@link #isVm() <em>Vm</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isVm()
   * @generated
   * @ordered
   */
  protected static final boolean VM_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isVm() <em>Vm</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isVm()
   * @generated
   * @ordered
   */
  protected boolean vm = VM_EDEFAULT;

  private transient File file;

  private transient List<String> contents;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EclipseIniTaskImpl()
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
    return SetupPackage.Literals.ECLIPSE_INI_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOption()
  {
    return option;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOption(String newOption)
  {
    String oldOption = option;
    option = newOption;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.ECLIPSE_INI_TASK__OPTION, oldOption, option));
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
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.ECLIPSE_INI_TASK__VALUE, oldValue, value));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isVm()
  {
    return vm;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVm(boolean newVm)
  {
    boolean oldVm = vm;
    vm = newVm;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.ECLIPSE_INI_TASK__VM, oldVm, vm));
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
      case SetupPackage.ECLIPSE_INI_TASK__OPTION:
        return getOption();
      case SetupPackage.ECLIPSE_INI_TASK__VALUE:
        return getValue();
      case SetupPackage.ECLIPSE_INI_TASK__VM:
        return isVm();
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
      case SetupPackage.ECLIPSE_INI_TASK__OPTION:
        setOption((String)newValue);
        return;
      case SetupPackage.ECLIPSE_INI_TASK__VALUE:
        setValue((String)newValue);
        return;
      case SetupPackage.ECLIPSE_INI_TASK__VM:
        setVm((Boolean)newValue);
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
      case SetupPackage.ECLIPSE_INI_TASK__OPTION:
        setOption(OPTION_EDEFAULT);
        return;
      case SetupPackage.ECLIPSE_INI_TASK__VALUE:
        setValue(VALUE_EDEFAULT);
        return;
      case SetupPackage.ECLIPSE_INI_TASK__VM:
        setVm(VM_EDEFAULT);
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
      case SetupPackage.ECLIPSE_INI_TASK__OPTION:
        return OPTION_EDEFAULT == null ? option != null : !OPTION_EDEFAULT.equals(option);
      case SetupPackage.ECLIPSE_INI_TASK__VALUE:
        return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
      case SetupPackage.ECLIPSE_INI_TASK__VM:
        return vm != VM_EDEFAULT;
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
    result.append(" (option: ");
    result.append(option);
    result.append(", value: ");
    result.append(value);
    result.append(", vm: ");
    result.append(vm);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getPriority()
  {
    return PRIORITY_INSTALLATION + 1;
  }

  public String getLabel(String value)
  {
    return getOption() + (value == null ? "" : (isVm() ? "" : " ") + value);
  }

  @Override
  public Object getOverrideToken()
  {
    return createToken(getOption());
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    file = new File(context.getProductLocation(), context.getOS().getEclipseIni());
    boolean result = !file.exists() || createNewContent(context);
    // Ensure that the perform recomputes the contents because they could be modified by other tasks between now and
    // when doPeform is called.
    contents = null;
    return result;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    if (!file.exists())
    {
      context.log("Skipping because " + file + " does not exist");
      return;
    }

    if (contents != null || createNewContent(context))
    {
      context.log("Changing " + file + " (" + getLabel(getValue()) + ")");
      IOUtil.writeLines(file, null, contents);
      context.setRestartNeeded("The eclipse.ini file has changed.");
    }
  }

  private boolean createNewContent(SetupTaskContext context)
  {
    List<String> oldContents = IOUtil.readLines(file, null);
    contents = new ArrayList<String>(oldContents);
    int vmargsIndex = contents.indexOf("-vmargs");

    String option = getOption();
    String value = getValue();

    if (isVm())
    {
      String line = option + value;
      if (vmargsIndex != -1)
      {
        for (int i = vmargsIndex + 1; i < contents.size(); i++)
        {
          String oldLine = contents.get(i);
          if (oldLine.startsWith(option))
          {
            contents.set(i, line);
            line = null;
            break;
          }
        }
      }
      else
      {
        contents.add("-vmargs");
      }

      if (line != null)
      {
        contents.add(line);
      }
    }
    else
    {
      int optionIndex = contents.indexOf(option);
      if (optionIndex != -1)
      {
        if (value != null)
        {
          contents.set(optionIndex + 1, value);
        }
      }
      else
      {
        optionIndex = vmargsIndex != -1 ? vmargsIndex : contents.size();
        contents.add(optionIndex, option);
        if (value != null)
        {
          contents.add(optionIndex + 1, value);
        }
      }
    }

    return !contents.equals(oldContents);
  }
} // EclipseIniTaskImpl
