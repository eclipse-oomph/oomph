/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.osgi.util.NLS;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Launch Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.launching.impl.LaunchTaskImpl#getLauncher <em>Launcher</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.launching.impl.LaunchTaskImpl#isRunEveryStartup <em>Run Every Startup</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LaunchTaskImpl extends SetupTaskImpl implements LaunchTask
{
  private static final PropertyFile HISTORY = new PropertyFile(LaunchingPlugin.INSTANCE.getStateLocation().append("launch-history.properties").toFile()); //$NON-NLS-1$

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
   * The default value of the '{@link #isRunEveryStartup() <em>Run Every Startup</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isRunEveryStartup()
   * @generated
   * @ordered
   */
  protected static final boolean RUN_EVERY_STARTUP_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isRunEveryStartup() <em>Run Every Startup</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isRunEveryStartup()
   * @generated
   * @ordered
   */
  protected boolean runEveryStartup = RUN_EVERY_STARTUP_EDEFAULT;

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
  public boolean isRunEveryStartup()
  {
    return runEveryStartup;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setRunEveryStartup(boolean newRunEveryStartup)
  {
    boolean oldRunEveryStartup = runEveryStartup;
    runEveryStartup = newRunEveryStartup;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, LaunchingPackage.LAUNCH_TASK__RUN_EVERY_STARTUP, oldRunEveryStartup, runEveryStartup));
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
      case LaunchingPackage.LAUNCH_TASK__RUN_EVERY_STARTUP:
        return isRunEveryStartup();
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
      case LaunchingPackage.LAUNCH_TASK__RUN_EVERY_STARTUP:
        setRunEveryStartup((Boolean)newValue);
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
      case LaunchingPackage.LAUNCH_TASK__RUN_EVERY_STARTUP:
        setRunEveryStartup(RUN_EVERY_STARTUP_EDEFAULT);
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
      case LaunchingPackage.LAUNCH_TASK__RUN_EVERY_STARTUP:
        return runEveryStartup != RUN_EVERY_STARTUP_EDEFAULT;
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
    result.append(" (launcher: "); //$NON-NLS-1$
    result.append(launcher);
    result.append(", runEveryStartup: "); //$NON-NLS-1$
    result.append(runEveryStartup);
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
    if (trigger == Trigger.STARTUP && !isRunEveryStartup())
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

  @SuppressWarnings("restriction")
  public void perform(final SetupTaskContext context) throws Exception
  {
    String launcher = getLauncher();
    ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
    ILaunchConfiguration targetLaunchConfiguration = getLaunchConfiguration(launcher, launchManager);

    if (targetLaunchConfiguration == null)
    {
      context.log(Messages.LaunchTaskImpl_LauncherNotFoundWaiting_message);
      IJobManager jobManager = Job.getJobManager();
      jobManager.join(org.eclipse.core.internal.events.NotificationManager.class, context.getProgressMonitor(true));
      context.log(Messages.LaunchTaskImpl_RefreshFinished_message);
      targetLaunchConfiguration = getLaunchConfiguration(launcher, launchManager);
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
      throw new Exception(NLS.bind(Messages.LaunchTaskImpl_LauncherNotFound_exception, launcher));
    }

    PreferenceProperty launchPromptForErrorPreference = new PreferencesUtil.PreferenceProperty(
        "/instance/org.eclipse.debug.ui/org.eclipse.debug.ui.cancel_launch_with_compile_errors"); //$NON-NLS-1$
    String oldValue = launchPromptForErrorPreference.get(null);
    try
    {
      launchPromptForErrorPreference.set("always"); //$NON-NLS-1$

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
            HISTORY.setProperty(launcher, processes.length > 0 ? Integer.toString(processes[0].getExitValue()) : "-1"); //$NON-NLS-1$
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

  private static ILaunchConfiguration getLaunchConfiguration(String launcher, ILaunchManager launchManager) throws CoreException
  {
    for (ILaunchConfiguration launchConfiguration : launchManager.getLaunchConfigurations())
    {
      String name = launchConfiguration.getName();
      if (name.equals(launcher))
      {
        return launchConfiguration;
      }
    }

    return null;
  }
} // LaunchTaskImpl
