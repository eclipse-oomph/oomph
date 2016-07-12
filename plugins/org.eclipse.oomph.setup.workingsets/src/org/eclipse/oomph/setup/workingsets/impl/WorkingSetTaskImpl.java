/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.workingsets.impl;

import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.workingsets.SetupWorkingSetsPackage;
import org.eclipse.oomph.setup.workingsets.WorkingSetTask;
import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.WorkingSetGroup;
import org.eclipse.oomph.workingsets.util.WorkingSetsUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Set Working Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.workingsets.impl.WorkingSetTaskImpl#getPrefix <em>Prefix</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.workingsets.impl.WorkingSetTaskImpl#getWorkingSets <em>Working Sets</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WorkingSetTaskImpl extends SetupTaskImpl implements WorkingSetTask
{
  /**
   * The default value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPrefix()
   * @generated
   * @ordered
   */
  protected static final String PREFIX_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPrefix()
   * @generated
   * @ordered
   */
  protected String prefix = PREFIX_EDEFAULT;

  /**
   * The cached value of the '{@link #getWorkingSets() <em>Working Sets</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWorkingSets()
   * @generated
   * @ordered
   */
  protected EList<WorkingSet> workingSets;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected WorkingSetTaskImpl()
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
    return SetupWorkingSetsPackage.Literals.WORKING_SET_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public String getPrefix()
  {
    return prefix == null ? getWorkingSetPrefix() : prefix;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPrefix(String newPrefix)
  {
    String oldPrefix = prefix;
    prefix = newPrefix;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupWorkingSetsPackage.WORKING_SET_TASK__PREFIX, oldPrefix, prefix));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<WorkingSet> getWorkingSets()
  {
    if (workingSets == null)
    {
      workingSets = new EObjectContainmentEList<WorkingSet>(WorkingSet.class, this, SetupWorkingSetsPackage.WORKING_SET_TASK__WORKING_SETS);
    }
    return workingSets;
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
      case SetupWorkingSetsPackage.WORKING_SET_TASK__WORKING_SETS:
        return ((InternalEList<?>)getWorkingSets()).basicRemove(otherEnd, msgs);
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
      case SetupWorkingSetsPackage.WORKING_SET_TASK__PREFIX:
        return getPrefix();
      case SetupWorkingSetsPackage.WORKING_SET_TASK__WORKING_SETS:
        return getWorkingSets();
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
      case SetupWorkingSetsPackage.WORKING_SET_TASK__PREFIX:
        setPrefix((String)newValue);
        return;
      case SetupWorkingSetsPackage.WORKING_SET_TASK__WORKING_SETS:
        getWorkingSets().clear();
        getWorkingSets().addAll((Collection<? extends WorkingSet>)newValue);
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
      case SetupWorkingSetsPackage.WORKING_SET_TASK__PREFIX:
        setPrefix(PREFIX_EDEFAULT);
        return;
      case SetupWorkingSetsPackage.WORKING_SET_TASK__WORKING_SETS:
        getWorkingSets().clear();
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
      case SetupWorkingSetsPackage.WORKING_SET_TASK__PREFIX:
        return PREFIX_EDEFAULT == null ? prefix != null : !PREFIX_EDEFAULT.equals(prefix);
      case SetupWorkingSetsPackage.WORKING_SET_TASK__WORKING_SETS:
        return workingSets != null && !workingSets.isEmpty();
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
    result.append(" (prefix: ");
    result.append(prefix);
    result.append(')');
    return result.toString();
  }

  private String getWorkingSetPrefix()
  {
    for (EObject eContainer = eContainer(); eContainer != null; eContainer = eContainer.eContainer())
    {
      if (eContainer instanceof Scope)
      {
        Scope scope = (Scope)eContainer;
        return scope.getQualifiedName().replace(' ', '.') + '-';
      }
    }

    throw new IllegalStateException("The working set task must be indirectly contained by a scope");
  }

  @Override
  public Object getOverrideToken()
  {
    return getPrefix();
  }

  @Override
  public void overrideFor(SetupTask overriddenSetupTask)
  {
    super.overrideFor(overriddenSetupTask);

    WorkingSetTask workingSetTask = (WorkingSetTask)overriddenSetupTask;
    EList<WorkingSet> workingSets = getWorkingSets();
    LOOP: for (WorkingSet overriddenWorkingSet : new ArrayList<WorkingSet>(workingSetTask.getWorkingSets()))
    {
      for (WorkingSet workingSet : workingSets)
      {
        if (EcoreUtil.equals(workingSet, overriddenWorkingSet))
        {
          continue LOOP;
        }
      }

      workingSets.add(0, overriddenWorkingSet);
    }
  }

  private Map<String, WorkingSet> getExistingWorkingSets(String prefix, EList<WorkingSet> workingSets)
  {
    Map<String, WorkingSet> existingWorkingSets = new HashMap<String, WorkingSet>();
    for (WorkingSet workingSet : workingSets)
    {
      String id = workingSet.getID();
      if (id != null && id.startsWith(prefix))
      {
        existingWorkingSets.put(id, workingSet);
      }
    }

    return existingWorkingSets;
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    String prefix = getPrefix();
    WorkingSetGroup defaultWorkingSetGroup = WorkingSetsUtil.getWorkingSetGroup();
    Map<String, WorkingSet> existingWorkingSets = getExistingWorkingSets(prefix, defaultWorkingSetGroup.getWorkingSets());

    for (WorkingSet workingSet : getWorkingSets())
    {
      String id = prefix + workingSet.getName();
      workingSet.setID(id);
    }

    for (WorkingSet workingSet : getWorkingSets())
    {
      context.checkCancelation();

      WorkingSet existingWorkingSet = existingWorkingSets.remove(workingSet.getID());
      if (existingWorkingSet == null || !EcoreUtil.equals(workingSet.getPredicates(), existingWorkingSet.getPredicates()))
      {
        return true;
      }
    }

    return !existingWorkingSets.isEmpty();
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    String prefix = getPrefix();
    WorkingSetGroup defaultWorkingSetGroup = WorkingSetsUtil.getWorkingSetGroup();
    EList<WorkingSet> workingSets = defaultWorkingSetGroup.getWorkingSets();
    Map<String, WorkingSet> existingWorkingSets = getExistingWorkingSets(prefix, workingSets);

    EList<WorkingSet> newWorkingSetGroups = getWorkingSets();
    int index = 0;
    for (WorkingSet workingSet : new ArrayList<WorkingSet>(newWorkingSetGroups))
    {
      context.checkCancelation();

      String id = workingSet.getID();
      WorkingSet existingWorkingSet = existingWorkingSets.remove(id);
      if (existingWorkingSet == null)
      {
        workingSets.add(index++, workingSet);
      }
      else
      {
        index = workingSets.indexOf(existingWorkingSet);
        workingSets.set(index, workingSet);
        ++index;
      }
    }

    workingSets.removeAll(existingWorkingSets.values());

    Resource resource = defaultWorkingSetGroup.eResource();
    resource.save(Collections.singletonMap(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER));
  }

  @Override
  public int getPriority()
  {
    return PRIORITY_LATE;
  }
} // SetWorkingTaskImpl
