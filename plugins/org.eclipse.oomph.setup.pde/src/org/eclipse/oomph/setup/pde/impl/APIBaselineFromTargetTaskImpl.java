/*
 * Copyright (c) 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.pde.impl;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.log.ProgressLog.Severity;
import org.eclipse.oomph.setup.log.ProgressLogMonitor;
import org.eclipse.oomph.setup.pde.APIBaselineFromTargetTask;
import org.eclipse.oomph.setup.pde.PDEPackage;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.api.tools.internal.model.ApiModelFactory;
import org.eclipse.pde.api.tools.internal.provisional.ApiPlugin;
import org.eclipse.pde.api.tools.internal.provisional.IApiBaselineManager;
import org.eclipse.pde.api.tools.internal.provisional.model.IApiBaseline;
import org.eclipse.pde.core.target.ITargetDefinition;

import java.lang.reflect.Method;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>API Baseline From Target Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.pde.impl.APIBaselineFromTargetTaskImpl#getTargetName <em>Target Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class APIBaselineFromTargetTaskImpl extends AbstractAPIBaselineTaskImpl implements APIBaselineFromTargetTask
{
  /**
   * The default value of the '{@link #getTargetName() <em>Target Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetName()
   * @generated
   * @ordered
   */
  protected static final String TARGET_NAME_EDEFAULT = ""; //$NON-NLS-1$

  /**
   * The cached value of the '{@link #getTargetName() <em>Target Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargetName()
   * @generated
   * @ordered
   */
  protected String targetName = TARGET_NAME_EDEFAULT;

  private transient ITargetDefinition target;

  private transient IApiBaselineManager baselineManager;

  private transient String baselineName;

  private transient IApiBaseline baseline;

  private transient boolean wasActive;

  private transient boolean backupRequired;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected APIBaselineFromTargetTaskImpl()
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
    return PDEPackage.Literals.API_BASELINE_FROM_TARGET_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getTargetName()
  {
    return targetName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setTargetName(String newTargetName)
  {
    String oldTargetName = targetName;
    targetName = newTargetName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, PDEPackage.API_BASELINE_FROM_TARGET_TASK__TARGET_NAME, oldTargetName, targetName));
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
      case PDEPackage.API_BASELINE_FROM_TARGET_TASK__TARGET_NAME:
        return getTargetName();
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
      case PDEPackage.API_BASELINE_FROM_TARGET_TASK__TARGET_NAME:
        setTargetName((String)newValue);
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
      case PDEPackage.API_BASELINE_FROM_TARGET_TASK__TARGET_NAME:
        setTargetName(TARGET_NAME_EDEFAULT);
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
      case PDEPackage.API_BASELINE_FROM_TARGET_TASK__TARGET_NAME:
        return TARGET_NAME_EDEFAULT == null ? targetName != null : !TARGET_NAME_EDEFAULT.equals(targetName);
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
    result.append(" (targetName: "); //$NON-NLS-1$
    result.append(targetName);
    result.append(')');
    return result.toString();
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    Method isDerivedFromTargetMethod;
    try
    {
      isDerivedFromTargetMethod = ApiModelFactory.class.getMethod("isDerivedFromTarget", IApiBaseline.class, ITargetDefinition.class); //$NON-NLS-1$
    }
    catch (NoSuchMethodException ex)
    {
      return false;
    }

    ApiPlugin apiPlugin = ApiPlugin.getDefault();
    if (apiPlugin == null)
    {
      // Might be deactivated
      return false;
    }

    String name = getName();
    String targetName = getTargetName();
    if (name == null || targetName == null)
    {
      return false;
    }

    target = TargetPlatformUtil.getTargetDefinition(targetName);

    boolean hasTargetDefinitionUpdates = SetupUtil.getResolvingTargetDefinitions(context).contains(targetName);
    if (target == null && !hasTargetDefinitionUpdates)
    {
      return false;
    }

    baselineManager = apiPlugin.getApiBaselineManager();
    baselineName = name;
    baseline = baselineManager.getApiBaseline(baselineName);
    if (baseline == null)
    {
      return true;
    }

    // Force a load.
    baseline.getApiComponents();

    // Work-around for PDE bug 489924:
    // API baseline from target X is not considered as derived from target X
    String location = baseline.getLocation();
    if (!StringUtil.isEmpty(location) && location.startsWith("target:")) //$NON-NLS-1$
    {
      if (location.indexOf('\\') >= 0)
      {
        location = location.replace('\\', '/');
        baseline.setLocation(location);
      }
    }

    wasActive = baselineManager.getDefaultApiBaseline() == baseline;
    backupRequired = !ReflectUtil.<Boolean> invokeMethod(isDerivedFromTargetMethod, null, baseline, target);

    return backupRequired || target == null || !wasActive && isActivate() || hasTargetDefinitionUpdates;
  }

  public void perform(SetupTaskContext context) throws Exception
  {
    if (backupRequired)
    {
      String backupName = baselineName + " " + System.currentTimeMillis(); //$NON-NLS-1$
      context.log(NLS.bind(Messages.APIBaselineFromTargetTaskImpl_Backup_message, backupName));

      baselineManager.removeApiBaseline(baselineName);
      baseline.setName(backupName);
      baselineManager.addApiBaseline(baseline);
      baseline = null;
    }

    if (baseline != null)
    {
      context.log(Messages.APIBaselineFromTargetTaskImpl_RemoveBaseline_message);
      baselineManager.removeApiBaseline(baselineName);
      baseline = null;
    }

    String targetName = getTargetName();
    if (target == null)
    {
      target = TargetPlatformUtil.getTargetDefinition(targetName);
    }

    if (target == null)
    {
      context.log(NLS.bind(Messages.APIBaselineFromTargetTaskImpl_CreateEmptyBaseline_message, targetName), Severity.WARNING);
      baseline = ApiModelFactory.newApiBaseline(baselineName);
      baselineManager.addApiBaseline(baseline);
    }

    if (baseline == null)
    {
      context.log(NLS.bind(Messages.APIBaselineFromTargetTaskImpl_CreatingBaseline_message, targetName));
      baseline = ReflectUtil.invokeMethod(
          ApiModelFactory.class.getMethod("newApiBaselineFromTarget", String.class, ITargetDefinition.class, IProgressMonitor.class), null, baselineName, //$NON-NLS-1$
          target, new ProgressLogMonitor(context));
      baselineManager.addApiBaseline(baseline);
    }

    if ((wasActive || isActivate()) && baselineManager.getDefaultApiBaseline() != baseline)
    {
      context.log(NLS.bind(Messages.APIBaselineFromTargetTaskImpl_ActivatingBaseline_message, baselineName));
      baselineManager.setDefaultApiBaseline(baselineName);
    }
  }

} // APIBaselineFromTargetTaskImpl
