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

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.StringSubstitutionTask;
import org.eclipse.oomph.setup.VariableChoice;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.VariableType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.Collection;
import java.util.List;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Context Variable Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.VariableTaskImpl#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.VariableTaskImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.VariableTaskImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.VariableTaskImpl#isStorePromptedValue <em>Store Prompted Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.VariableTaskImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.VariableTaskImpl#getChoices <em>Choices</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VariableTaskImpl extends SetupTaskImpl implements VariableTask
{
  /**
   * The default value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected static final VariableType TYPE_EDEFAULT = VariableType.STRING;

  /**
   * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected VariableType type = TYPE_EDEFAULT;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

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
   * The default value of the '{@link #isStorePromptedValue() <em>Store Prompted Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isStorePromptedValue()
   * @generated
   * @ordered
   */
  protected static final boolean STORE_PROMPTED_VALUE_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isStorePromptedValue() <em>Store Prompted Value</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isStorePromptedValue()
   * @generated
   * @ordered
   */
  protected boolean storePromptedValue = STORE_PROMPTED_VALUE_EDEFAULT;

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
   * The cached value of the '{@link #getChoices() <em>Choices</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getChoices()
   * @generated
   * @ordered
   */
  protected EList<VariableChoice> choices;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected VariableTaskImpl()
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
    return SetupPackage.Literals.VARIABLE_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public VariableType getType()
  {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setType(VariableType newType)
  {
    VariableType oldType = type;
    type = newType == null ? TYPE_EDEFAULT : newType;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.VARIABLE_TASK__TYPE, oldType, type));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.VARIABLE_TASK__NAME, oldName, name));
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
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.VARIABLE_TASK__VALUE, oldValue, value));
    }
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
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.VARIABLE_TASK__LABEL, oldLabel, label));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<VariableChoice> getChoices()
  {
    if (choices == null)
    {
      choices = new EObjectContainmentEList.Resolving<VariableChoice>(VariableChoice.class, this, SetupPackage.VARIABLE_TASK__CHOICES);
    }
    return choices;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isStorePromptedValue()
  {
    return storePromptedValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setStorePromptedValue(boolean newStorePromptedValue)
  {
    boolean oldStorePromptedValue = storePromptedValue;
    storePromptedValue = newStorePromptedValue;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.VARIABLE_TASK__STORE_PROMPTED_VALUE, oldStorePromptedValue, storePromptedValue));
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
      case SetupPackage.VARIABLE_TASK__CHOICES:
        return ((InternalEList<?>)getChoices()).basicRemove(otherEnd, msgs);
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
      case SetupPackage.VARIABLE_TASK__TYPE:
        return getType();
      case SetupPackage.VARIABLE_TASK__NAME:
        return getName();
      case SetupPackage.VARIABLE_TASK__VALUE:
        return getValue();
      case SetupPackage.VARIABLE_TASK__STORE_PROMPTED_VALUE:
        return isStorePromptedValue();
      case SetupPackage.VARIABLE_TASK__LABEL:
        return getLabel();
      case SetupPackage.VARIABLE_TASK__CHOICES:
        return getChoices();
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
      case SetupPackage.VARIABLE_TASK__TYPE:
        setType((VariableType)newValue);
        return;
      case SetupPackage.VARIABLE_TASK__NAME:
        setName((String)newValue);
        return;
      case SetupPackage.VARIABLE_TASK__VALUE:
        setValue((String)newValue);
        return;
      case SetupPackage.VARIABLE_TASK__STORE_PROMPTED_VALUE:
        setStorePromptedValue((Boolean)newValue);
        return;
      case SetupPackage.VARIABLE_TASK__LABEL:
        setLabel((String)newValue);
        return;
      case SetupPackage.VARIABLE_TASK__CHOICES:
        getChoices().clear();
        getChoices().addAll((Collection<? extends VariableChoice>)newValue);
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
      case SetupPackage.VARIABLE_TASK__TYPE:
        setType(TYPE_EDEFAULT);
        return;
      case SetupPackage.VARIABLE_TASK__NAME:
        setName(NAME_EDEFAULT);
        return;
      case SetupPackage.VARIABLE_TASK__VALUE:
        setValue(VALUE_EDEFAULT);
        return;
      case SetupPackage.VARIABLE_TASK__STORE_PROMPTED_VALUE:
        setStorePromptedValue(STORE_PROMPTED_VALUE_EDEFAULT);
        return;
      case SetupPackage.VARIABLE_TASK__LABEL:
        setLabel(LABEL_EDEFAULT);
        return;
      case SetupPackage.VARIABLE_TASK__CHOICES:
        getChoices().clear();
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
      case SetupPackage.VARIABLE_TASK__TYPE:
        return type != TYPE_EDEFAULT;
      case SetupPackage.VARIABLE_TASK__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case SetupPackage.VARIABLE_TASK__VALUE:
        return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
      case SetupPackage.VARIABLE_TASK__STORE_PROMPTED_VALUE:
        return storePromptedValue != STORE_PROMPTED_VALUE_EDEFAULT;
      case SetupPackage.VARIABLE_TASK__LABEL:
        return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
      case SetupPackage.VARIABLE_TASK__CHOICES:
        return choices != null && !choices.isEmpty();
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String toString()
  {
    if (eIsProxy())
    {
      return super.toString();
    }

    StringBuffer result = new StringBuffer();
    result.append(name);
    if (value != null)
    {
      result.append("=\"");
      result.append(value);
      result.append('"');
    }

    return result.toString();
  }

  @Override
  public Object getOverrideToken()
  {
    return createToken(getName());
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    return false;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    throw new UnsupportedOperationException("Context variable task can't be executed");
  }

  private static final String ANNOTATION_SOURCE = "http://www.eclipse.org/oomph/Migrator";

  private static final String ANNOTATION_KEY = "platform:/plugin/org.eclipse.oomph.base/model/legacy/setup.ecore#//ContextVariableTask/stringSubstitution";

  protected void eMigrate()
  {
    int xxx; // The above ANNOTATION_SOURCE is not visible here.
    Annotation annotation = getAnnotation(ANNOTATION_SOURCE);
    if (annotation != null)
    {
      EMap<String, String> details = annotation.getDetails();
      String value = details.get(ANNOTATION_KEY);
      if (value != null)
      {
        String name = getName();
        if (name != null && "true".equals(value))
        {
          StringSubstitutionTask stringSubstitutionTask = SetupFactory.eINSTANCE.createStringSubstitutionTask();
          stringSubstitutionTask.setName(name);
          stringSubstitutionTask.setValue("${" + name + "}");
          EObject eContainer = eContainer();
          @SuppressWarnings("unchecked")
          List<EObject> list = (List<EObject>)eContainer.eGet(eContainmentFeature());
          list.add(list.indexOf(this) + 1, stringSubstitutionTask);
        }

        details.remove(ANNOTATION_KEY);
        if (annotation.getDetails().isEmpty())
        {
          getAnnotations().remove(annotation);
        }
      }
    }
  }

} // VariableTaskImpl
