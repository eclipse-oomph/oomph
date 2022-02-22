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
package org.eclipse.oomph.setup.jdt.impl;

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.jdt.JDTPackage;
import org.eclipse.oomph.setup.jdt.JRELibrary;
import org.eclipse.oomph.setup.jdt.JRETask;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.jdt.launching.VMStandin;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>JRE Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.jdt.impl.JRETaskImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.impl.JRETaskImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.impl.JRETaskImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.impl.JRETaskImpl#getVMInstallType <em>VM Install Type</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.impl.JRETaskImpl#isExecutionEnvironmentDefault <em>Execution Environment Default</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.impl.JRETaskImpl#getVMArguments <em>VM Arguments</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.impl.JRETaskImpl#getJRELibraries <em>JRE Libraries</em>}</li>
 * </ul>
 *
 * @generated
 */
public class JRETaskImpl extends SetupTaskImpl implements JRETask
{
  /**
   * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
  protected static final String VERSION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVersion()
   * @generated
   * @ordered
   */
  protected String version = VERSION_EDEFAULT;

  /**
   * The default value of the '{@link #getLocation() <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected static final String LOCATION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLocation() <em>Location</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocation()
   * @generated
   * @ordered
   */
  protected String location = LOCATION_EDEFAULT;

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
   * The default value of the '{@link #getVMInstallType() <em>VM Install Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVMInstallType()
   * @generated
   * @ordered
   */
  protected static final String VM_INSTALL_TYPE_EDEFAULT = "org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType"; //$NON-NLS-1$

  /**
   * The cached value of the '{@link #getVMInstallType() <em>VM Install Type</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVMInstallType()
   * @generated
   * @ordered
   */
  protected String vMInstallType = VM_INSTALL_TYPE_EDEFAULT;

  /**
   * The default value of the '{@link #isExecutionEnvironmentDefault() <em>Execution Environment Default</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isExecutionEnvironmentDefault()
   * @generated
   * @ordered
   */
  protected static final boolean EXECUTION_ENVIRONMENT_DEFAULT_EDEFAULT = true;

  /**
   * The cached value of the '{@link #isExecutionEnvironmentDefault() <em>Execution Environment Default</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isExecutionEnvironmentDefault()
   * @generated
   * @ordered
   */
  protected boolean executionEnvironmentDefault = EXECUTION_ENVIRONMENT_DEFAULT_EDEFAULT;

  /**
   * The default value of the '{@link #getVMArguments() <em>VM Arguments</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVMArguments()
   * @generated
   * @ordered
   */
  protected static final String VM_ARGUMENTS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getVMArguments() <em>VM Arguments</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVMArguments()
   * @generated
   * @ordered
   */
  protected String vMArguments = VM_ARGUMENTS_EDEFAULT;

  /**
   * The cached value of the '{@link #getJRELibraries() <em>JRE Libraries</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getJRELibraries()
   * @generated
   * @ordered
   */
  protected EList<JRELibrary> jRELibraries;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected JRETaskImpl()
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
    return JDTPackage.Literals.JRE_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getVersion()
  {
    return version;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setVersion(String newVersion)
  {
    String oldVersion = version;
    version = newVersion;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JDTPackage.JRE_TASK__VERSION, oldVersion, version));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getLocation()
  {
    return location;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setLocation(String newLocation)
  {
    String oldLocation = location;
    location = newLocation;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JDTPackage.JRE_TASK__LOCATION, oldLocation, location));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getNameGen()
  {
    return name;
  }

  @Override
  public String getName()
  {
    String name = getNameGen();
    if (StringUtil.isEmpty(name))
    {
      String version = getVersion();
      if (!StringUtil.isEmpty(version))
      {
        return "JRE for " + version; //$NON-NLS-1$
      }

      return null;
    }

    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JDTPackage.JRE_TASK__NAME, oldName, name));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getVMInstallType()
  {
    return vMInstallType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setVMInstallType(String newVMInstallType)
  {
    String oldVMInstallType = vMInstallType;
    vMInstallType = newVMInstallType;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JDTPackage.JRE_TASK__VM_INSTALL_TYPE, oldVMInstallType, vMInstallType));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isExecutionEnvironmentDefault()
  {
    return executionEnvironmentDefault;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setExecutionEnvironmentDefault(boolean newExecutionEnvironmentDefault)
  {
    boolean oldExecutionEnvironmentDefault = executionEnvironmentDefault;
    executionEnvironmentDefault = newExecutionEnvironmentDefault;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JDTPackage.JRE_TASK__EXECUTION_ENVIRONMENT_DEFAULT, oldExecutionEnvironmentDefault,
          executionEnvironmentDefault));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getVMArguments()
  {
    return vMArguments;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setVMArguments(String newVMArguments)
  {
    String oldVMArguments = vMArguments;
    vMArguments = newVMArguments;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, JDTPackage.JRE_TASK__VM_ARGUMENTS, oldVMArguments, vMArguments));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<JRELibrary> getJRELibraries()
  {
    if (jRELibraries == null)
    {
      jRELibraries = new EObjectContainmentEList<>(JRELibrary.class, this, JDTPackage.JRE_TASK__JRE_LIBRARIES);
    }
    return jRELibraries;
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
      case JDTPackage.JRE_TASK__JRE_LIBRARIES:
        return ((InternalEList<?>)getJRELibraries()).basicRemove(otherEnd, msgs);
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
      case JDTPackage.JRE_TASK__VERSION:
        return getVersion();
      case JDTPackage.JRE_TASK__LOCATION:
        return getLocation();
      case JDTPackage.JRE_TASK__NAME:
        return getName();
      case JDTPackage.JRE_TASK__VM_INSTALL_TYPE:
        return getVMInstallType();
      case JDTPackage.JRE_TASK__EXECUTION_ENVIRONMENT_DEFAULT:
        return isExecutionEnvironmentDefault();
      case JDTPackage.JRE_TASK__VM_ARGUMENTS:
        return getVMArguments();
      case JDTPackage.JRE_TASK__JRE_LIBRARIES:
        return getJRELibraries();
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
      case JDTPackage.JRE_TASK__VERSION:
        setVersion((String)newValue);
        return;
      case JDTPackage.JRE_TASK__LOCATION:
        setLocation((String)newValue);
        return;
      case JDTPackage.JRE_TASK__NAME:
        setName((String)newValue);
        return;
      case JDTPackage.JRE_TASK__VM_INSTALL_TYPE:
        setVMInstallType((String)newValue);
        return;
      case JDTPackage.JRE_TASK__EXECUTION_ENVIRONMENT_DEFAULT:
        setExecutionEnvironmentDefault((Boolean)newValue);
        return;
      case JDTPackage.JRE_TASK__VM_ARGUMENTS:
        setVMArguments((String)newValue);
        return;
      case JDTPackage.JRE_TASK__JRE_LIBRARIES:
        getJRELibraries().clear();
        getJRELibraries().addAll((Collection<? extends JRELibrary>)newValue);
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
      case JDTPackage.JRE_TASK__VERSION:
        setVersion(VERSION_EDEFAULT);
        return;
      case JDTPackage.JRE_TASK__LOCATION:
        setLocation(LOCATION_EDEFAULT);
        return;
      case JDTPackage.JRE_TASK__NAME:
        setName(NAME_EDEFAULT);
        return;
      case JDTPackage.JRE_TASK__VM_INSTALL_TYPE:
        setVMInstallType(VM_INSTALL_TYPE_EDEFAULT);
        return;
      case JDTPackage.JRE_TASK__EXECUTION_ENVIRONMENT_DEFAULT:
        setExecutionEnvironmentDefault(EXECUTION_ENVIRONMENT_DEFAULT_EDEFAULT);
        return;
      case JDTPackage.JRE_TASK__VM_ARGUMENTS:
        setVMArguments(VM_ARGUMENTS_EDEFAULT);
        return;
      case JDTPackage.JRE_TASK__JRE_LIBRARIES:
        getJRELibraries().clear();
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
      case JDTPackage.JRE_TASK__VERSION:
        return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
      case JDTPackage.JRE_TASK__LOCATION:
        return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
      case JDTPackage.JRE_TASK__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case JDTPackage.JRE_TASK__VM_INSTALL_TYPE:
        return VM_INSTALL_TYPE_EDEFAULT == null ? vMInstallType != null : !VM_INSTALL_TYPE_EDEFAULT.equals(vMInstallType);
      case JDTPackage.JRE_TASK__EXECUTION_ENVIRONMENT_DEFAULT:
        return executionEnvironmentDefault != EXECUTION_ENVIRONMENT_DEFAULT_EDEFAULT;
      case JDTPackage.JRE_TASK__VM_ARGUMENTS:
        return VM_ARGUMENTS_EDEFAULT == null ? vMArguments != null : !VM_ARGUMENTS_EDEFAULT.equals(vMArguments);
      case JDTPackage.JRE_TASK__JRE_LIBRARIES:
        return jRELibraries != null && !jRELibraries.isEmpty();
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
    result.append(" (version: "); //$NON-NLS-1$
    result.append(version);
    result.append(", location: "); //$NON-NLS-1$
    result.append(location);
    result.append(", name: "); //$NON-NLS-1$
    result.append(name);
    result.append(", vMInstallType: "); //$NON-NLS-1$
    result.append(vMInstallType);
    result.append(", executionEnvironmentDefault: "); //$NON-NLS-1$
    result.append(executionEnvironmentDefault);
    result.append(", vMArguments: "); //$NON-NLS-1$
    result.append(vMArguments);
    result.append(')');
    return result.toString();
  }

  @Override
  public Object getOverrideToken()
  {
    return createToken(getName());
  }

  @Override
  public void overrideFor(SetupTask overriddenSetupTask)
  {
    super.overrideFor(overriddenSetupTask);
    JRETask overriddenJRETask = (JRETask)overriddenSetupTask;
    mergeArguments(overriddenJRETask);
  }

  private void mergeArguments(JRETask overriddenJRETask)
  {
    String overridingValue = sanitizeArguments(getVMArguments());
    String overriddenValue = sanitizeArguments(overriddenJRETask.getVMArguments());

    if (overridingValue != null)
    {
      if (overriddenValue != null)
      {
        setVMArguments(overriddenValue + StringUtil.NL + overridingValue);
      }
      else
      {
        setVMArguments(overridingValue);
      }
    }
  }

  private String sanitizeArguments(String arguments)
  {
    if (StringUtil.isEmpty(arguments))
    {
      return null;
    }

    return arguments.trim().replaceAll("(\n\r?|\r\n?)", StringUtil.NL); //$NON-NLS-1$
  }

  @Override
  public boolean isNeeded(SetupTaskContext context) throws Exception
  {
    return JREHelper.isNeeded(context, getName(), getVersion(), getLocation(), getVMInstallType(), isExecutionEnvironmentDefault(),
        sanitizeArguments(getVMArguments()), getJRELibraries());
  }

  @Override
  public void perform(SetupTaskContext context) throws Exception
  {
    JREHelper.perform(context, getName(), getVersion(), getLocation(), getVMInstallType(), isExecutionEnvironmentDefault(), sanitizeArguments(getVMArguments()),
        getJRELibraries());
  }

  private static class JREHelper
  {
    public static void perform(SetupTaskContext context, String name, String version, String location, String vmInstallTypeID,
        boolean executionEnvironmentDefault, String vmArguments, Collection<JRELibrary> jreLibraries) throws Exception
    {
      IVMInstallType[] types = JavaRuntime.getVMInstallTypes();
      for (IVMInstallType type : types)
      {
        if (vmInstallTypeID.equals(type.getId()))
        {
          IVMInstall realVM = getVMInstall(name, vmInstallTypeID);
          boolean setAsDefault = false;
          VMStandin vmStandin;
          if (realVM == null)
          {
            setAsDefault = true;
            context.log(NLS.bind(Messages.JRETaskImpl_CreatingJRE_message, name, location));

            vmStandin = new VMStandin(type, EcoreUtil.generateUUID());
            vmStandin.setName(name);
          }
          else
          {
            context.log(NLS.bind(Messages.JRETaskImpl_UpdatingJRE_message, name, location));
            vmStandin = new VMStandin(realVM);
          }

          File installLocation = new File(location);
          IStatus validationStatus = type.validateInstallLocation(installLocation);
          if (!validationStatus.isOK())
          {
            context.log(NLS.bind(Messages.JRETaskImpl_InvalidLocation_message, location));
            context.log(Messages.JRETaskImpl_GoBack_message);
            throw new CoreException(validationStatus);
          }

          vmStandin.setInstallLocation(installLocation);

          List<String> mergedVMArguments = new ArrayList<>();

          if (vmArguments != null)
          {
            String[] vmArgumentsArray = new ExecutionArguments(vmArguments, "").getVMArgumentsArray(); //$NON-NLS-1$
            if (vmArgumentsArray != null)
            {
              for (String vmArgument : vmArgumentsArray)
              {
                mergedVMArguments.add(vmArgument);
              }
            }
          }

          String[] vmStandinVMArguments = vmStandin.getVMArguments();
          if (vmStandinVMArguments != null)
          {
            for (String vmArgument : vmStandinVMArguments)
            {
              if (!mergedVMArguments.contains(vmArgument))
              {
                mergedVMArguments.add(vmArgument);
              }
            }
          }

          vmStandin.setVMArguments(mergedVMArguments.toArray(new String[mergedVMArguments.size()]));
          if (!mergedVMArguments.isEmpty())
          {
            context.log(NLS.bind(Messages.JRETaskImpl_SetVMArgs_message, name, StringUtil.implode(mergedVMArguments, '\0')));
          }

          if (!jreLibraries.isEmpty())
          {
            if (vmStandin.getLibraryLocations() == null)
            {
              // We have to set this to an empty array first in order for the real setLibraryLocations() call to have any effect.
              vmStandin.setLibraryLocations(new LibraryLocation[0]);
              vmStandin.setLibraryLocations(type.getDefaultLibraryLocations(installLocation));
            }

            IPath installPath = new Path(installLocation.getCanonicalPath());
            for (JRELibrary jreLibrary : jreLibraries)
            {
              String libraryPath = jreLibrary.getLibraryPath();
              String externalAnnotationsPathLiteral = jreLibrary.getExternalAnnotationsPath();
              if (libraryPath != null && externalAnnotationsPathLiteral != null)
              {
                IPath jreLibraryPath = installPath.append(libraryPath);
                LibraryLocation[] libraryLocations = vmStandin.getLibraryLocations();
                for (int i = 0; i < libraryLocations.length; i++)
                {
                  LibraryLocation libraryLocation = libraryLocations[i];
                  IPath systemLibraryPath = libraryLocation.getSystemLibraryPath();
                  if (jreLibraryPath.equals(systemLibraryPath))
                  {
                    Path externalAnnotationsPath = new Path(externalAnnotationsPathLiteral);
                    try
                    {
                      if (!externalAnnotationsPath.equals(libraryLocation.getExternalAnnotationsPath()))
                      {
                        LibraryLocation newLibraryLocation = new LibraryLocation(systemLibraryPath, libraryLocation.getSystemLibrarySourcePath(),
                            libraryLocation.getPackageRootPath(), libraryLocation.getJavadocLocation(), libraryLocation.getIndexLocation(),
                            externalAnnotationsPath);
                        libraryLocations[i] = newLibraryLocation;
                      }
                    }
                    catch (NoSuchMethodError ex)
                    {
                      //$FALL-THROUGH$
                    }
                    break;
                  }
                }
              }
            }
          }

          realVM = vmStandin.convertToRealVM();
          if (setAsDefault)
          {
            JavaRuntime.setDefaultVMInstall(realVM, new NullProgressMonitor());
          }

          if (executionEnvironmentDefault)
          {
            IExecutionEnvironment[] executionEnvironments = JavaRuntime.getExecutionEnvironmentsManager().getExecutionEnvironments();
            for (IExecutionEnvironment executionEnvironment : executionEnvironments)
            {
              String id = executionEnvironment.getId();
              if (id.equals(version) || "CDC-1.1/Foundation-1.1".equals(id) && "J2SE-1.4".equals(version)) //$NON-NLS-1$ //$NON-NLS-2$
              {
                if (executionEnvironment.getDefaultVM() == null)
                {
                  context.log(NLS.bind(Messages.JRETaskImpl_SettingEE_message, name));
                  executionEnvironment.setDefaultVM(realVM);
                }
              }
            }
          }

          return;
        }
      }
    }

    public static boolean isNeeded(SetupTaskContext context, String name, String version, String location, String vmInstallTypeID,
        boolean executionEnvironmentDefault, String vmArguments, Collection<JRELibrary> jreLibraries) throws Exception
    {
      // If there is already a VM install for this name...
      IVMInstall vmInstall = getVMInstall(name, vmInstallTypeID);
      if (vmInstall != null)
      {
        // If the JRE for that name doesn't have the expected location...
        if (!vmInstall.getInstallLocation().equals(new File(location)))
        {
          return true;
        }

        // If the JRE for that name doesn't have VM arguments...
        String[] vmInstallVMArguments = vmInstall.getVMArguments();
        if (vmInstallVMArguments == null)
        {
          // But there should be VM arguments...
          if (vmArguments != null)
          {
            return true;
          }
        }
        else if (vmArguments != null)
        {
          // If the argument are not all already present...
          String[] vmArgumentsArray = new ExecutionArguments(vmArguments, "").getVMArgumentsArray(); //$NON-NLS-1$
          if (vmArgumentsArray != null && !Arrays.asList(vmInstallVMArguments).containsAll(Arrays.asList(vmArgumentsArray)))
          {
            return true;
          }
        }

        // If this JRE should be the execution environment default.
        if (executionEnvironmentDefault)
        {
          IExecutionEnvironment[] executionEnvironments = JavaRuntime.getExecutionEnvironmentsManager().getExecutionEnvironments();
          for (IExecutionEnvironment executionEnvironment : executionEnvironments)
          {
            String id = executionEnvironment.getId();
            if (id.equals(version) || "CDC-1.1/Foundation-1.1".equals(id) && "J2SE-1.4".equals(version)) //$NON-NLS-1$ //$NON-NLS-2$
            {
              // If the corresponding execution environment has no default, then the task is still needed, even though the JRE already exists.
              if (executionEnvironment.getDefaultVM() == null)
              {
                return true;
              }
            }
          }
        }

        if (!jreLibraries.isEmpty())
        {
          File installLocation = new File(location);
          IPath installPath = new Path(installLocation.getCanonicalPath());
          for (JRELibrary jreLibrary : jreLibraries)
          {
            String libraryPath = jreLibrary.getLibraryPath();
            String externalAnnotationsPathLiteral = jreLibrary.getExternalAnnotationsPath();
            if (libraryPath != null && externalAnnotationsPathLiteral != null)
            {
              IPath jreLibraryPath = installPath.append(libraryPath);
              LibraryLocation[] libraryLocations = vmInstall.getLibraryLocations();
              if (libraryLocations == null)
              {
                // If it's still using the defaults, indicated by a null result, then the perform needs to initialize this to be non-null.
                return true;
              }

              for (int i = 0; i < libraryLocations.length; i++)
              {
                LibraryLocation libraryLocation = libraryLocations[i];
                if (jreLibraryPath.equals(libraryLocation.getSystemLibraryPath()))
                {
                  Path externalAnnotationsPath = new Path(externalAnnotationsPathLiteral);
                  try
                  {
                    if (!externalAnnotationsPath.equals(libraryLocation.getExternalAnnotationsPath()))
                    {
                      return true;
                    }
                  }
                  catch (NoSuchMethodError ex)
                  {
                    //$FALL-THROUGH$
                  }
                  break;
                }
              }
            }
            // NOTE: if we couldn't find the library we wouldn't be able to update it, so DON'T return true.
          }
        }

        // The JRE already exists so the task isn't needed.
        return false;
      }

      // The JRE must be created.
      return true;
    }

    private static IVMInstall getVMInstall(String name, String vmInstallTypeID) throws Exception
    {
      for (IVMInstallType vmInstallType : JavaRuntime.getVMInstallTypes())
      {
        if (vmInstallType.getId().equals(vmInstallTypeID))
        {
          for (IVMInstall vmInstall : vmInstallType.getVMInstalls())
          {
            if (vmInstall.getName().equals(name))
            {
              return vmInstall;
            }
          }

          break;
        }
      }

      return null;
    }
  }
} // JRETaskImpl
