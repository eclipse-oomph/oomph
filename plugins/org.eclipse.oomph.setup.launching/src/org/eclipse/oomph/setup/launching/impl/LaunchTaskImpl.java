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
package org.eclipse.oomph.setup.launching.impl;

import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.preferences.util.PreferencesUtil.PreferenceProperty;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.launching.LaunchTask;
import org.eclipse.oomph.setup.launching.LaunchingPackage;
import org.eclipse.oomph.setup.launching.LaunchingPlugin;
import org.eclipse.oomph.setup.log.ProgressLog.Severity;
import org.eclipse.oomph.util.PropertyFile;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Launch Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.launching.impl.LaunchTaskImpl#getLauncher <em>Launcher</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LaunchTaskImpl extends SetupTaskImpl implements LaunchTask
{
  private static final PropertyFile HISTORY = new PropertyFile(LaunchingPlugin.INSTANCE.getStateLocation().append("launch-history.properties").toFile());

  /**
   * The default value of the '{@link #getLauncher() <em>Launcher</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLauncher()
   * @generated
   * @ordered
   */
  protected static final String LAUNCHER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLauncher() <em>Launcher</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLauncher()
   * @generated
   * @ordered
   */
  protected String launcher = LAUNCHER_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected LaunchTaskImpl()
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
    return LaunchingPackage.Literals.LAUNCH_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLauncher()
  {
    return launcher;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLauncher(String newLauncher)
  {
    String oldLauncher = launcher;
    launcher = newLauncher;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, LaunchingPackage.LAUNCH_TASK__LAUNCHER, oldLauncher, launcher));
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
      case LaunchingPackage.LAUNCH_TASK__LAUNCHER:
        return getLauncher();
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
      case LaunchingPackage.LAUNCH_TASK__LAUNCHER:
        setLauncher((String)newValue);
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
      case LaunchingPackage.LAUNCH_TASK__LAUNCHER:
        setLauncher(LAUNCHER_EDEFAULT);
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
      case LaunchingPackage.LAUNCH_TASK__LAUNCHER:
        return LAUNCHER_EDEFAULT == null ? launcher != null : !LAUNCHER_EDEFAULT.equals(launcher);
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
    result.append(" (launcher: ");
    result.append(launcher);
    result.append(')');
    return result.toString();
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 10;
  }

  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    String launcher = getLauncher();
    Trigger trigger = context.getTrigger();
    if (trigger == Trigger.STARTUP)
    {
      // If we've performed this task at least once, don't do it automatically again.
      String property = HISTORY.getProperty(launcher, null);
      if (property != null)
      {
        return false;
      }
    }

    return true;
  }

  public void perform(final SetupTaskContext context) throws Exception
  {
    String launcher = getLauncher();
    ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
    ILaunchConfiguration targetLaunchConfiguration = null;
    for (ILaunchConfiguration launchConfiguration : launchManager.getLaunchConfigurations())
    {
      String name = launchConfiguration.getName();
      if (name.equals(launcher))
      {
        targetLaunchConfiguration = launchConfiguration;
        break;
      }
    }

    if (targetLaunchConfiguration == null)
    {
      Path path = new Path(launcher);
      if (path.segmentCount() >= 2)
      {
        IFile file = EcorePlugin.getWorkspaceRoot().getFile(path);
        if (file.isAccessible())
        {
          targetLaunchConfiguration = launchManager.getLaunchConfiguration(file);
        }
      }
    }

    if (targetLaunchConfiguration == null)
    {
      throw new Exception("No launcher found for " + launcher);
    }

    PreferenceProperty launchPromptForErrorPreference = new PreferencesUtil.PreferenceProperty(
        "/instance/org.eclipse.debug.ui/org.eclipse.debug.ui.cancel_launch_with_compile_errors");
    String oldValue = launchPromptForErrorPreference.get(null);
    try
    {
      launchPromptForErrorPreference.set("always");

      ILaunch launch = targetLaunchConfiguration.launch(ILaunchManager.RUN_MODE, null);
      IProcess[] processes = launch.getProcesses();
      if (processes.length > 0)
      {
        for (IProcess process : processes)
        {
          IStreamsProxy streamsProxy = process.getStreamsProxy();
          if (streamsProxy != null)
          {
            IStreamMonitor outputStreamMonitor = streamsProxy.getOutputStreamMonitor();
            if (outputStreamMonitor != null)
            {
              outputStreamMonitor.addListener(new IStreamListener()
              {
                public void streamAppended(String text, IStreamMonitor monitor)
                {
                  context.log(text.replace('\r', ' '));
                }
              });
            }

            IStreamMonitor errorStreamMonitor = streamsProxy.getErrorStreamMonitor();
            if (errorStreamMonitor != null)
            {
              errorStreamMonitor.addListener(new IStreamListener()
              {
                public void streamAppended(String text, IStreamMonitor monitor)
                {
                  context.log(text.replace('\r', ' '), Severity.ERROR);
                }
              });
            }
          }
        }

        for (;;)
        {
          Thread.sleep(1000);
          if (context.isCanceled())
          {
            launch.terminate();
            return;
          }

          if (launch.isTerminated())
          {
            HISTORY.setProperty(launcher, processes.length > 0 ? Integer.toString(processes[0].getExitValue()) : "-1");
            return;
          }
        }
      }
    }
    finally
    {
      launchPromptForErrorPreference.set(oldValue);
    }
  }
} // LaunchTaskImpl
