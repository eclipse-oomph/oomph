/*
 * Copyright (c) 2014, 2015, 2017, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.setup.EclipseIniTask;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Eclipse Ini Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.EclipseIniTaskImpl#getOption <em>Option</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.EclipseIniTaskImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.EclipseIniTaskImpl#isVm <em>Vm</em>}</li>
 * </ul>
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
  @Override
  public String getOption()
  {
    return option;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
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
  @Override
  public String getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
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
  @Override
  public boolean isVm()
  {
    return vm;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
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

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (option: "); //$NON-NLS-1$
    result.append(option);
    result.append(", value: "); //$NON-NLS-1$
    result.append(value);
    result.append(", vm: "); //$NON-NLS-1$
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
    return getOption() + (value == null ? "" : (isVm() ? "" : " ") + value); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  @Override
  public Object getOverrideToken()
  {
    return createToken(getOption());
  }

  @Override
  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    // Return early for bootstrap because the launcher name can't be determined until after the p2 task has performed.
    if (context.getTrigger() == Trigger.BOOTSTRAP)
    {
      return true;
    }

    if (context.isSelfHosting())
    {
      return false;
    }

    file = new File(context.getProductLocation(), context.getLauncherName() + ".ini"); //$NON-NLS-1$
    boolean result = !file.exists() || createNewContent(context);

    // Ensure that the perform recomputes the contents because they could be modified by other tasks between now and when doPeform is called.
    contents = null;

    return result;
  }

  @Override
  public void perform(SetupTaskContext context) throws Exception
  {
    if (file == null)
    {
      file = new File(context.getProductLocation(), context.getLauncherName() + ".ini"); //$NON-NLS-1$
    }

    if (!file.exists())
    {
      context.log(NLS.bind(Messages.EclipseIniTaskImpl_Skipping_message, file));
      return;
    }

    if (contents != null || createNewContent(context))
    {
      context.log(NLS.bind(Messages.EclipseIniTaskImpl_Changing_message, file, getLabel(getValue())));

      // Write the ini file with the system's default encoding; the native launcher reads it so.
      IOUtil.writeLines(file, null, contents);
      context.setRestartNeeded(NLS.bind(Messages.EclipseIniTaskImpl_FileHasChanged_message, file.getName()));
    }
  }

  private boolean createNewContent(SetupTaskContext context)
  {
    // Read the existing ini file with the system's default encoding, like the native launcher does.
    List<String> oldContents = IOUtil.readLines(file, null);
    contents = new ArrayList<>(oldContents);
    int vmargsIndex = contents.indexOf("-vmargs"); //$NON-NLS-1$

    String option = getOption();
    String value = getValue();
    if (value != null && value.trim().isEmpty())
    {
      value = null;
    }

    if (isVm())
    {
      String line = option + (value == null ? "" : value); //$NON-NLS-1$
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
        contents.add("-vmargs"); //$NON-NLS-1$
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
