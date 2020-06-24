/*
 * Copyright (c) 2019 Ed Merks (Berlin) and others.
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
import org.eclipse.oomph.setup.Argument;
import org.eclipse.oomph.setup.Macro;
import org.eclipse.oomph.setup.MacroTask;
import org.eclipse.oomph.setup.Parameter;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Argument</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.impl.ArgumentImpl#getMacroTask <em>Macro Task</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ArgumentImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ArgumentImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.impl.ArgumentImpl#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ArgumentImpl extends ModelElementImpl implements Argument
{
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
   * The cached value of the '{@link #getParameter() <em>Parameter</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getParameter()
   * @generated
   * @ordered
   */
  protected Parameter parameter;

  /**
   * This is true if the Parameter reference has been set.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  protected boolean parameterESet;

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
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ArgumentImpl()
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
    return SetupPackage.Literals.ARGUMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MacroTask getMacroTask()
  {
    if (eContainerFeatureID() != SetupPackage.ARGUMENT__MACRO_TASK)
    {
      return null;
    }
    return (MacroTask)eInternalContainer();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String getName()
  {
    // Ensure that this processing will never demand load a resource.
    EObject resolvedParameter = resolveProxy((InternalEObject)basicGetParameter(), false);
    if (resolvedParameter instanceof Parameter)
    {
      Parameter parameter = (Parameter)resolvedParameter;
      URI parameterProxyURI = ((InternalEObject)parameter).eProxyURI();
      if (parameterProxyURI != null && parameterProxyURI.isCurrentDocumentReference())
      {
        // If it's the special proxy URI, just return its fragment.
        return parameterProxyURI.fragment();
      }

      MacroTask macroTask = getMacroTask();
      if (macroTask != null)
      {
        EObject resolvedMacro = resolveProxy((InternalEObject)macroTask.eGet(SetupPackage.Literals.MACRO_TASK__MACRO, false), false);
        if (resolvedMacro instanceof Macro)
        {
          Macro macro = (Macro)resolvedMacro;
          URI macroProxyURI = ((InternalEObject)macro).eProxyURI();

          // If they are both proxies to the same URI...
          if (macroProxyURI != null && parameterProxyURI != null && macroProxyURI.trimFragment().equals(parameterProxyURI.trimFragment()))
          {
            // Find the name in the fragment and return it.
            String fragment = parameterProxyURI.fragment();
            if (fragment != null)
            {
              int start = fragment.indexOf('\'');
              int end = fragment.lastIndexOf('\'');
              if (start != -1 && end != -1 && start != end)
              {
                String name = URI.decode(fragment.substring(start + 1, end - 1));
                return name;
              }
            }
          }

          // If the parameter is contained by the macro, just return its name.
          if (parameter.eContainer() == macro)
          {
            return parameter.getName();
          }
        }
      }
    }

    // Ensure that the name feature is not considered set.
    // This will happen when the parameter isn't set, or it's set to something that can't resolve to a parameter of the containing macro task's macro.
    return null;
  }

  private EObject resolveProxy(InternalEObject proxy, boolean demandLoad)
  {
    if (proxy != null)
    {
      URI proxyURI = proxy.eProxyURI();
      if (proxyURI != null)
      {
        if (proxyURI.isCurrentDocumentReference())
        {
          String parameterName = proxyURI.fragment();
          if (parameterName != null)
          {
            MacroTask macroTask = getMacroTask();
            if (macroTask != null)
            {
              EObject resolvedMacro = resolveProxy((InternalEObject)macroTask.eGet(SetupPackage.Literals.MACRO_TASK__MACRO, false), demandLoad);
              if (resolvedMacro instanceof Macro)
              {
                Macro macro = (Macro)resolvedMacro;
                EList<Parameter> parameters = macro.getParameters();
                for (Parameter parameter : parameters)
                {
                  if (parameterName.equals(parameter.getName()))
                  {
                    return parameter;
                  }
                }
              }
            }
          }
        }
        else if (!demandLoad)
        {
          Resource resource = eResource();
          if (resource != null)
          {
            ResourceSet resourceSet = resource.getResourceSet();
            if (resourceSet != null)
            {
              return resourceSet.getEObject(proxyURI, false);
            }
          }
        }
      }
    }

    if (demandLoad)
    {
      return super.eResolveProxy(proxy);
    }

    return proxy;
  }

  @Override
  public EObject eResolveProxy(InternalEObject proxy)
  {
    // Ensure that the special proxy is handled properly.
    return resolveProxy(proxy, true);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void setName(String newName)
  {
    if (newName == null)
    {
      unsetParameter();
    }
    else
    {
      // Create a current document reference proxy with just the name as the fragment.
      Parameter parameter = SetupFactory.eINSTANCE.createParameter();
      ((InternalEObject)parameter).eSetProxyURI(URI.createURI("#" + newName)); //$NON-NLS-1$
      setParameter(parameter);
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Parameter getParameter()
  {
    if (parameter != null && parameter.eIsProxy())
    {
      InternalEObject oldParameter = (InternalEObject)parameter;
      parameter = (Parameter)eResolveProxy(oldParameter);
      if (parameter != oldParameter)
      {
        if (eNotificationRequired())
        {
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, SetupPackage.ARGUMENT__PARAMETER, oldParameter, parameter));
        }
      }
    }
    return parameter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Parameter basicGetParameter()
  {
    return parameter;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private void setParameterGen(Parameter newParameter)
  {
    Parameter oldParameter = parameter;
    parameter = newParameter;
    boolean oldParameterESet = parameterESet;
    parameterESet = true;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.ARGUMENT__PARAMETER, oldParameter, parameter, !oldParameterESet));
    }
  }

  public void setParameter(Parameter newParameter)
  {
    if (newParameter == null)
    {
      unsetParameter();
    }
    else
    {
      setParameterGen(newParameter);
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void unsetParameter()
  {
    Parameter oldParameter = parameter;
    boolean oldParameterESet = parameterESet;
    parameter = null;
    parameterESet = false;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.UNSET, SetupPackage.ARGUMENT__PARAMETER, oldParameter, null, oldParameterESet));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isSetParameterGen()
  {
    return parameterESet;
  }

  public boolean isSetParameter()
  {
    // This ensures that a parameter reference is serialized only if the parameter is set
    // but it doesn't reference a parameter of the containing macro task's
    return isSetParameterGen() && getName() == null;
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
      eNotify(new ENotificationImpl(this, Notification.SET, SetupPackage.ARGUMENT__VALUE, oldValue, value));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case SetupPackage.ARGUMENT__MACRO_TASK:
        if (eInternalContainer() != null)
        {
          msgs = eBasicRemoveFromContainer(msgs);
        }
        return eBasicSetContainer(otherEnd, SetupPackage.ARGUMENT__MACRO_TASK, msgs);
    }
    return super.eInverseAdd(otherEnd, featureID, msgs);
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
      case SetupPackage.ARGUMENT__MACRO_TASK:
        return eBasicSetContainer(null, SetupPackage.ARGUMENT__MACRO_TASK, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs)
  {
    switch (eContainerFeatureID())
    {
      case SetupPackage.ARGUMENT__MACRO_TASK:
        return eInternalContainer().eInverseRemove(this, SetupPackage.MACRO_TASK__ARGUMENTS, MacroTask.class, msgs);
    }
    return super.eBasicRemoveFromContainerFeature(msgs);
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
      case SetupPackage.ARGUMENT__MACRO_TASK:
        return getMacroTask();
      case SetupPackage.ARGUMENT__NAME:
        return getName();
      case SetupPackage.ARGUMENT__PARAMETER:
        if (resolve)
        {
          return getParameter();
        }
        return basicGetParameter();
      case SetupPackage.ARGUMENT__VALUE:
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
      case SetupPackage.ARGUMENT__NAME:
        setName((String)newValue);
        return;
      case SetupPackage.ARGUMENT__PARAMETER:
        setParameter((Parameter)newValue);
        return;
      case SetupPackage.ARGUMENT__VALUE:
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
      case SetupPackage.ARGUMENT__NAME:
        setName(NAME_EDEFAULT);
        return;
      case SetupPackage.ARGUMENT__PARAMETER:
        unsetParameter();
        return;
      case SetupPackage.ARGUMENT__VALUE:
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
      case SetupPackage.ARGUMENT__MACRO_TASK:
        return getMacroTask() != null;
      case SetupPackage.ARGUMENT__NAME:
        return NAME_EDEFAULT == null ? getName() != null : !NAME_EDEFAULT.equals(getName());
      case SetupPackage.ARGUMENT__PARAMETER:
        return isSetParameter();
      case SetupPackage.ARGUMENT__VALUE:
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

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (value: "); //$NON-NLS-1$
    result.append(value);
    result.append(')');
    return result.toString();
  }

} // ArgumentImpl
