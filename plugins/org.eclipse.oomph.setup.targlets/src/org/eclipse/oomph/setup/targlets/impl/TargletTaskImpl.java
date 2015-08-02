/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.targlets.impl;

import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.internal.core.CacheUsageConfirmer;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.impl.SetupTaskImpl;
import org.eclipse.oomph.setup.targlets.ImplicitDependency;
import org.eclipse.oomph.setup.targlets.SetupTargletsFactory;
import org.eclipse.oomph.setup.targlets.SetupTargletsPackage;
import org.eclipse.oomph.setup.targlets.TargletTask;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.TargletFactory;
import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.internal.core.TargletContainer;
import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;
import org.eclipse.oomph.targlets.internal.core.WorkspaceIUImporter;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.pde.TargetPlatformRunnable;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetHandle;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.core.target.ITargetPlatformService;
import org.eclipse.pde.core.target.NameVersionDescriptor;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Targlet Task</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl#getTarglets <em>Targlets</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl#getTargletURIs <em>Targlet UR Is</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl#getOperatingSystem <em>Operating System</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl#getWindowingSystem <em>Windowing System</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl#getArchitecture <em>Architecture</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl#getLocale <em>Locale</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl#getProgramArguments <em>Program Arguments</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl#getVMArguments <em>VM Arguments</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.targlets.impl.TargletTaskImpl#getImplicitDependencies <em>Implicit Dependencies</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TargletTaskImpl extends SetupTaskImpl implements TargletTask
{
  private static final String TARGET_DEFINITION_NAME = "Modular Target";

  private static final String TARGLET_CONTAINER_ID = "Oomph";

  /**
   * The cached value of the '{@link #getTarglets() <em>Targlets</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTarglets()
   * @generated
   * @ordered
   */
  protected EList<Targlet> targlets;

  /**
   * The cached value of the '{@link #getTargletURIs() <em>Targlet UR Is</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTargletURIs()
   * @generated
   * @ordered
   */
  protected EList<String> targletURIs;

  /**
   * The default value of the '{@link #getOperatingSystem() <em>Operating System</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperatingSystem()
   * @generated
   * @ordered
   */
  protected static final String OPERATING_SYSTEM_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOperatingSystem() <em>Operating System</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperatingSystem()
   * @generated
   * @ordered
   */
  protected String operatingSystem = OPERATING_SYSTEM_EDEFAULT;

  /**
   * The default value of the '{@link #getWindowingSystem() <em>Windowing System</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWindowingSystem()
   * @generated
   * @ordered
   */
  protected static final String WINDOWING_SYSTEM_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getWindowingSystem() <em>Windowing System</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWindowingSystem()
   * @generated
   * @ordered
   */
  protected String windowingSystem = WINDOWING_SYSTEM_EDEFAULT;

  /**
   * The default value of the '{@link #getArchitecture() <em>Architecture</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArchitecture()
   * @generated
   * @ordered
   */
  protected static final String ARCHITECTURE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getArchitecture() <em>Architecture</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArchitecture()
   * @generated
   * @ordered
   */
  protected String architecture = ARCHITECTURE_EDEFAULT;

  /**
   * The default value of the '{@link #getLocale() <em>Locale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocale()
   * @generated
   * @ordered
   */
  protected static final String LOCALE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getLocale() <em>Locale</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLocale()
   * @generated
   * @ordered
   */
  protected String locale = LOCALE_EDEFAULT;

  /**
   * The default value of the '{@link #getProgramArguments() <em>Program Arguments</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProgramArguments()
   * @generated
   * @ordered
   */
  protected static final String PROGRAM_ARGUMENTS_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getProgramArguments() <em>Program Arguments</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProgramArguments()
   * @generated
   * @ordered
   */
  protected String programArguments = PROGRAM_ARGUMENTS_EDEFAULT;

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
   * The cached value of the '{@link #getImplicitDependencies() <em>Implicit Dependencies</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getImplicitDependencies()
   * @generated
   * @ordered
   */
  protected EList<ImplicitDependency> implicitDependencies;

  private ITargletContainer targletContainer;

  private ITargetDefinition targetDefinition;

  private EList<Targlet> copyTarglets;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TargletTaskImpl()
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
    return SetupTargletsPackage.Literals.TARGLET_TASK;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Targlet> getTarglets()
  {
    if (targlets == null)
    {
      targlets = new EObjectContainmentEList<Targlet>(Targlet.class, this, SetupTargletsPackage.TARGLET_TASK__TARGLETS);
    }
    return targlets;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<String> getTargletURIs()
  {
    if (targletURIs == null)
    {
      targletURIs = new EDataTypeUniqueEList<String>(String.class, this, SetupTargletsPackage.TARGLET_TASK__TARGLET_UR_IS);
    }
    return targletURIs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getOperatingSystem()
  {
    return operatingSystem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOperatingSystem(String newOperatingSystem)
  {
    String oldOperatingSystem = operatingSystem;
    operatingSystem = newOperatingSystem;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupTargletsPackage.TARGLET_TASK__OPERATING_SYSTEM, oldOperatingSystem, operatingSystem));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getWindowingSystem()
  {
    return windowingSystem;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setWindowingSystem(String newWindowingSystem)
  {
    String oldWindowingSystem = windowingSystem;
    windowingSystem = newWindowingSystem;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupTargletsPackage.TARGLET_TASK__WINDOWING_SYSTEM, oldWindowingSystem, windowingSystem));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getArchitecture()
  {
    return architecture;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setArchitecture(String newArchitecture)
  {
    String oldArchitecture = architecture;
    architecture = newArchitecture;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupTargletsPackage.TARGLET_TASK__ARCHITECTURE, oldArchitecture, architecture));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLocale()
  {
    return locale;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLocale(String newLocale)
  {
    String oldLocale = locale;
    locale = newLocale;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupTargletsPackage.TARGLET_TASK__LOCALE, oldLocale, locale));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getProgramArguments()
  {
    return programArguments;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setProgramArguments(String newProgramArguments)
  {
    String oldProgramArguments = programArguments;
    programArguments = newProgramArguments;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupTargletsPackage.TARGLET_TASK__PROGRAM_ARGUMENTS, oldProgramArguments, programArguments));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getVMArguments()
  {
    return vMArguments;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVMArguments(String newVMArguments)
  {
    String oldVMArguments = vMArguments;
    vMArguments = newVMArguments;
    if (eNotificationRequired())
    {
      eNotify(new ENotificationImpl(this, Notification.SET, SetupTargletsPackage.TARGLET_TASK__VM_ARGUMENTS, oldVMArguments, vMArguments));
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<ImplicitDependency> getImplicitDependencies()
  {
    if (implicitDependencies == null)
    {
      implicitDependencies = new EObjectContainmentEList<ImplicitDependency>(ImplicitDependency.class, this,
          SetupTargletsPackage.TARGLET_TASK__IMPLICIT_DEPENDENCIES);
    }
    return implicitDependencies;
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
      case SetupTargletsPackage.TARGLET_TASK__TARGLETS:
        return ((InternalEList<?>)getTarglets()).basicRemove(otherEnd, msgs);
      case SetupTargletsPackage.TARGLET_TASK__IMPLICIT_DEPENDENCIES:
        return ((InternalEList<?>)getImplicitDependencies()).basicRemove(otherEnd, msgs);
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
      case SetupTargletsPackage.TARGLET_TASK__TARGLETS:
        return getTarglets();
      case SetupTargletsPackage.TARGLET_TASK__TARGLET_UR_IS:
        return getTargletURIs();
      case SetupTargletsPackage.TARGLET_TASK__OPERATING_SYSTEM:
        return getOperatingSystem();
      case SetupTargletsPackage.TARGLET_TASK__WINDOWING_SYSTEM:
        return getWindowingSystem();
      case SetupTargletsPackage.TARGLET_TASK__ARCHITECTURE:
        return getArchitecture();
      case SetupTargletsPackage.TARGLET_TASK__LOCALE:
        return getLocale();
      case SetupTargletsPackage.TARGLET_TASK__PROGRAM_ARGUMENTS:
        return getProgramArguments();
      case SetupTargletsPackage.TARGLET_TASK__VM_ARGUMENTS:
        return getVMArguments();
      case SetupTargletsPackage.TARGLET_TASK__IMPLICIT_DEPENDENCIES:
        return getImplicitDependencies();
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
      case SetupTargletsPackage.TARGLET_TASK__TARGLETS:
        getTarglets().clear();
        getTarglets().addAll((Collection<? extends Targlet>)newValue);
        return;
      case SetupTargletsPackage.TARGLET_TASK__TARGLET_UR_IS:
        getTargletURIs().clear();
        getTargletURIs().addAll((Collection<? extends String>)newValue);
        return;
      case SetupTargletsPackage.TARGLET_TASK__OPERATING_SYSTEM:
        setOperatingSystem((String)newValue);
        return;
      case SetupTargletsPackage.TARGLET_TASK__WINDOWING_SYSTEM:
        setWindowingSystem((String)newValue);
        return;
      case SetupTargletsPackage.TARGLET_TASK__ARCHITECTURE:
        setArchitecture((String)newValue);
        return;
      case SetupTargletsPackage.TARGLET_TASK__LOCALE:
        setLocale((String)newValue);
        return;
      case SetupTargletsPackage.TARGLET_TASK__PROGRAM_ARGUMENTS:
        setProgramArguments((String)newValue);
        return;
      case SetupTargletsPackage.TARGLET_TASK__VM_ARGUMENTS:
        setVMArguments((String)newValue);
        return;
      case SetupTargletsPackage.TARGLET_TASK__IMPLICIT_DEPENDENCIES:
        getImplicitDependencies().clear();
        getImplicitDependencies().addAll((Collection<? extends ImplicitDependency>)newValue);
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
      case SetupTargletsPackage.TARGLET_TASK__TARGLETS:
        getTarglets().clear();
        return;
      case SetupTargletsPackage.TARGLET_TASK__TARGLET_UR_IS:
        getTargletURIs().clear();
        return;
      case SetupTargletsPackage.TARGLET_TASK__OPERATING_SYSTEM:
        setOperatingSystem(OPERATING_SYSTEM_EDEFAULT);
        return;
      case SetupTargletsPackage.TARGLET_TASK__WINDOWING_SYSTEM:
        setWindowingSystem(WINDOWING_SYSTEM_EDEFAULT);
        return;
      case SetupTargletsPackage.TARGLET_TASK__ARCHITECTURE:
        setArchitecture(ARCHITECTURE_EDEFAULT);
        return;
      case SetupTargletsPackage.TARGLET_TASK__LOCALE:
        setLocale(LOCALE_EDEFAULT);
        return;
      case SetupTargletsPackage.TARGLET_TASK__PROGRAM_ARGUMENTS:
        setProgramArguments(PROGRAM_ARGUMENTS_EDEFAULT);
        return;
      case SetupTargletsPackage.TARGLET_TASK__VM_ARGUMENTS:
        setVMArguments(VM_ARGUMENTS_EDEFAULT);
        return;
      case SetupTargletsPackage.TARGLET_TASK__IMPLICIT_DEPENDENCIES:
        getImplicitDependencies().clear();
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
      case SetupTargletsPackage.TARGLET_TASK__TARGLETS:
        return targlets != null && !targlets.isEmpty();
      case SetupTargletsPackage.TARGLET_TASK__TARGLET_UR_IS:
        return targletURIs != null && !targletURIs.isEmpty();
      case SetupTargletsPackage.TARGLET_TASK__OPERATING_SYSTEM:
        return OPERATING_SYSTEM_EDEFAULT == null ? operatingSystem != null : !OPERATING_SYSTEM_EDEFAULT.equals(operatingSystem);
      case SetupTargletsPackage.TARGLET_TASK__WINDOWING_SYSTEM:
        return WINDOWING_SYSTEM_EDEFAULT == null ? windowingSystem != null : !WINDOWING_SYSTEM_EDEFAULT.equals(windowingSystem);
      case SetupTargletsPackage.TARGLET_TASK__ARCHITECTURE:
        return ARCHITECTURE_EDEFAULT == null ? architecture != null : !ARCHITECTURE_EDEFAULT.equals(architecture);
      case SetupTargletsPackage.TARGLET_TASK__LOCALE:
        return LOCALE_EDEFAULT == null ? locale != null : !LOCALE_EDEFAULT.equals(locale);
      case SetupTargletsPackage.TARGLET_TASK__PROGRAM_ARGUMENTS:
        return PROGRAM_ARGUMENTS_EDEFAULT == null ? programArguments != null : !PROGRAM_ARGUMENTS_EDEFAULT.equals(programArguments);
      case SetupTargletsPackage.TARGLET_TASK__VM_ARGUMENTS:
        return VM_ARGUMENTS_EDEFAULT == null ? vMArguments != null : !VM_ARGUMENTS_EDEFAULT.equals(vMArguments);
      case SetupTargletsPackage.TARGLET_TASK__IMPLICIT_DEPENDENCIES:
        return implicitDependencies != null && !implicitDependencies.isEmpty();
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
    result.append(" (targletURIs: ");
    result.append(targletURIs);
    result.append(", operatingSystem: ");
    result.append(operatingSystem);
    result.append(", windowingSystem: ");
    result.append(windowingSystem);
    result.append(", architecture: ");
    result.append(architecture);
    result.append(", locale: ");
    result.append(locale);
    result.append(", programArguments: ");
    result.append(programArguments);
    result.append(", vMArguments: ");
    result.append(vMArguments);
    result.append(')');
    return result.toString();
  }

  @Override
  public Object getOverrideToken()
  {
    return createToken(TARGLET_CONTAINER_ID);
  }

  @Override
  public void overrideFor(SetupTask overriddenSetupTask)
  {
    super.overrideFor(overriddenSetupTask);

    TargletTask targletTask = (TargletTask)overriddenSetupTask;
    getTarglets().addAll(targletTask.getTarglets());

    mergeSetting(targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__OPERATING_SYSTEM, "operating systems");
    mergeSetting(targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__WINDOWING_SYSTEM, "windowing systems");
    mergeSetting(targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__ARCHITECTURE, "architectures");
    mergeSetting(targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__LOCALE, "locales");

    mergeArguments(targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__PROGRAM_ARGUMENTS);
    mergeArguments(targletTask, SetupTargletsPackage.Literals.TARGLET_TASK__VM_ARGUMENTS);

    getImplicitDependencies().addAll(targletTask.getImplicitDependencies());
  }

  private void mergeSetting(TargletTask overriddenTargletTask, EAttribute attribute, String errorLabel)
  {
    String overridingValue = (String)eGet(attribute);
    String overriddenValue = (String)overriddenTargletTask.eGet(attribute);

    if (!ObjectUtil.equals(overridingValue, overriddenValue))
    {
      if (overridingValue == null)
      {
        eSet(attribute, overriddenValue);
      }
      else
      {
        if (overriddenValue != null)
        {
          getAnnotations()
              .add(BaseFactory.eINSTANCE.createErrorAnnotation("The " + errorLabel + " '" + overriddenValue + "' and '" + overridingValue + "' collide."));
        }
      }
    }
  }

  private void mergeArguments(TargletTask overriddenTargletTask, EAttribute attribute)
  {
    String overridingValue = sanitizeArguments((String)eGet(attribute));
    String overriddenValue = sanitizeArguments((String)overriddenTargletTask.eGet(attribute));

    if (overridingValue != null)
    {
      if (overriddenValue != null)
      {
        eSet(attribute, overriddenValue + "\n" + overridingValue);
      }
      else
      {
        eSet(attribute, overridingValue);
      }
    }
  }

  private String sanitizeArguments(String arguments)
  {
    if (StringUtil.isEmpty(arguments))
    {
      return null;
    }

    arguments = arguments.trim();

    while (arguments.endsWith("\n") || arguments.endsWith("\r"))
    {
      arguments = arguments.substring(0, arguments.length() - 1).trim();
    }

    return arguments;
  }

  @Override
  public void consolidate()
  {
    super.consolidate();

    Set<String> targletNames = new HashSet<String>();
    EList<Targlet> targlets = getTarglets();

    LOOP: for (Iterator<Targlet> it = targlets.iterator(); it.hasNext();)
    {
      Targlet targlet = it.next();
      String name = targlet.getName();
      if (StringUtil.isEmpty(name) || !targletNames.add(name))
      {
        it.remove();
      }
      else if (targlet.getRequirements().isEmpty() && targlet.getSourceLocators().isEmpty())
      {
        // Eliminate targlets that are effectively empty, i.e., no requirements, no source locators, and the active repository list is empty.
        String activeRepositoryList = targlet.getActiveRepositoryListName();
        for (RepositoryList repositoryList : targlet.getRepositoryLists())
        {
          if (ObjectUtil.equals(activeRepositoryList, repositoryList.getName()))
          {
            if (repositoryList.getRepositories().isEmpty())
            {
              it.remove();
              continue LOOP;
            }
          }
        }
      }
    }

    ECollections.sort(targlets, new Comparator<Targlet>()
    {
      public int compare(Targlet o1, Targlet o2)
      {
        return StringUtil.safe(o1.getName()).compareTo(StringUtil.safe(o2.getName()));
      }
    });

    // Use a set to eliminate duplicates from the implicit dependencies.
    EList<ImplicitDependency> implicitDependencies = getImplicitDependencies();
    Set<NameVersionDescriptor> descriptors = createNameVersionDescriptors(implicitDependencies);
    implicitDependencies.clear();

    for (NameVersionDescriptor descriptor : descriptors)
    {
      String id = descriptor.getId();
      String version = descriptor.getVersion();

      ImplicitDependency implicitDependency = SetupTargletsFactory.eINSTANCE.createImplicitDependency(id, version);
      implicitDependencies.add(implicitDependency);
    }

    ECollections.sort(implicitDependencies, new Comparator<ImplicitDependency>()
    {
      public int compare(ImplicitDependency o1, ImplicitDependency o2)
      {
        int result = StringUtil.safe(o1.getID()).compareTo(StringUtil.safe(o2.getID()));
        if (result == 0)
        {
          Version v1 = o1.getVersion();
          if (v1 == null)
          {
            v1 = Version.emptyVersion;
          }

          Version v2 = o2.getVersion();
          if (v2 == null)
          {
            v2 = Version.emptyVersion;
          }

          result = v1.compareTo(v2);
        }

        return result;
      }
    });
  }

  @Override
  public int getProgressMonitorWork()
  {
    return 100;
  }

  public boolean isNeeded(final SetupTaskContext context) throws Exception
  {
    copyTarglets = TargletFactory.eINSTANCE.copyTarglets(getTarglets());

    return TargetPlatformUtil.runWithTargetPlatformService(new TargetPlatformRunnable<Boolean>()
    {
      public Boolean run(ITargetPlatformService service) throws CoreException
      {
        ITargetHandle activeTargetHandle = service.getWorkspaceTargetHandle();

        targetDefinition = getTargetDefinition(service, context.getProgressMonitor(true));
        if (targetDefinition == null)
        {
          return hasRequirements(copyTarglets);
        }

        targletContainer = getTargletContainer();
        if (targletContainer == null)
        {
          return hasRequirements(copyTarglets);
        }
        else if (!hasRequirements(copyTarglets) && !hasRequirements(targletContainer.getTarglets()))
        {
          return false;
        }

        for (Targlet targlet : copyTarglets)
        {
          Targlet existingTarglet = targletContainer.getTarglet(targlet.getName());
          if (existingTarglet == null || !EcoreUtil.equals(existingTarglet, targlet))
          {
            return true;
          }
        }

        if (context.getTrigger() == Trigger.MANUAL)
        {
          return true;
        }

        if (!targetDefinition.getHandle().equals(activeTargetHandle))
        {
          return true;
        }

        if (!ObjectUtil.equals(targetDefinition.getOS(), getOperatingSystem()) //
            || !ObjectUtil.equals(targetDefinition.getWS(), getWindowingSystem()) //
            || !ObjectUtil.equals(targetDefinition.getArch(), getArchitecture()) //
            || !ObjectUtil.equals(targetDefinition.getNL(), getLocale()) //
            || !ObjectUtil.equals(targetDefinition.getProgramArguments(), getProgramArguments()) //
            || !ObjectUtil.equals(targetDefinition.getVMArguments(), getVMArguments()) //
            || !equalNameVersionDescriptors(targetDefinition.getImplicitDependencies(), getImplicitDependencies()))
        {
          return true;
        }

        return false;
      }
    });
  }

  public void perform(final SetupTaskContext context) throws Exception
  {
    for (Targlet targlet : copyTarglets)
    {
      for (Repository p2Repository : targlet.getActiveRepositories())
      {
        context.log("Using " + p2Repository.getURL());
      }
    }

    boolean offline = context.isOffline();
    context.log("Offline = " + offline);

    boolean mirrors = context.isMirrors();
    context.log("Mirrors = " + mirrors);

    TargetPlatformUtil.runWithTargetPlatformService(new TargetPlatformRunnable<Object>()
    {
      public Object run(ITargetPlatformService service) throws CoreException
      {
        IProgressMonitor monitor = context.getProgressMonitor(true);
        monitor.beginTask("", 100 + (targetDefinition == null ? 1 : 0));

        try
        {
          if (targetDefinition == null)
          {
            targetDefinition = getTargetDefinition(service, new SubProgressMonitor(monitor, 1));
          }

          if (targetDefinition == null)
          {
            targetDefinition = service.newTarget();
            targetDefinition.setName(TARGET_DEFINITION_NAME);
          }

          targetDefinition.setOS(getOperatingSystem());
          targetDefinition.setWS(getWindowingSystem());
          targetDefinition.setArch(getArchitecture());
          targetDefinition.setNL(getLocale());
          targetDefinition.setProgramArguments(getProgramArguments());
          targetDefinition.setVMArguments(getVMArguments());
          targetDefinition.setImplicitDependencies(getNameVersionDescriptors());

          if (targletContainer == null)
          {
            targletContainer = getTargletContainer();
          }

          EList<Targlet> targlets = copyTarglets;
          if (targletContainer == null)
          {
            targletContainer = new TargletContainer(TARGLET_CONTAINER_ID);

            ITargetLocation[] newLocations;
            ITargetLocation[] oldLocations = targetDefinition.getTargetLocations();
            if (oldLocations != null && oldLocations.length != 0)
            {
              newLocations = new ITargetLocation[oldLocations.length + 1];
              System.arraycopy(oldLocations, 0, newLocations, 0, oldLocations.length);
              newLocations[oldLocations.length] = targletContainer;
            }
            else
            {
              newLocations = new ITargetLocation[] { targletContainer };
            }

            targetDefinition.setTargetLocations(newLocations);
          }

          boolean mirrors = context.isMirrors();

          CacheUsageConfirmer cacheUsageConfirmer = (CacheUsageConfirmer)context.get(CacheUsageConfirmer.class);
          CacheUsageConfirmer oldCacheUsageConfirmer = TargletsCorePlugin.INSTANCE.getCacheUsageConfirmer();

          try
          {
            TargletsCorePlugin.INSTANCE.setCacheUsageConfirmer(cacheUsageConfirmer);

            targletContainer.setTarglets(targlets);
            targletContainer.forceUpdate(true, mirrors, new SubProgressMonitor(monitor, 90));

            try
            {
              Job.getJobManager().join(WorkspaceIUImporter.WORKSPACE_IU_IMPORT_FAMILY, new SubProgressMonitor(monitor, 10));
            }
            catch (InterruptedException ex)
            {
              TargletsCorePlugin.INSTANCE.coreException(ex);
            }
          }
          finally
          {
            TargletsCorePlugin.INSTANCE.setCacheUsageConfirmer(oldCacheUsageConfirmer);
          }

          return null;
        }
        finally
        {
          monitor.done();
        }
      }
    });
  }

  private ITargetDefinition getTargetDefinition(ITargetPlatformService service, IProgressMonitor monitor) throws CoreException
  {
    for (ITargetHandle targetHandle : service.getTargets(monitor))
    {
      ITargetDefinition targetDefinition = targetHandle.getTargetDefinition();
      if (TARGET_DEFINITION_NAME.equals(targetDefinition.getName()))
      {
        return targetDefinition;
      }
    }

    return null;
  }

  private ITargletContainer getTargletContainer()
  {
    ITargetLocation[] locations = targetDefinition.getTargetLocations();
    if (locations != null)
    {
      for (ITargetLocation location : locations)
      {
        if (location instanceof ITargletContainer)
        {
          ITargletContainer targletContainer = (ITargletContainer)location;
          if (TARGLET_CONTAINER_ID.equals(targletContainer.getID()))
          {
            return targletContainer;
          }
        }
      }
    }

    return null;
  }

  private NameVersionDescriptor[] getNameVersionDescriptors()
  {
    EList<ImplicitDependency> implicitDependencies = getImplicitDependencies();
    if (implicitDependencies.isEmpty())
    {
      return null;
    }

    Set<NameVersionDescriptor> descriptors = createNameVersionDescriptors(implicitDependencies);
    return descriptors.toArray(new NameVersionDescriptor[descriptors.size()]);
  }

  private static Set<NameVersionDescriptor> createNameVersionDescriptors(Collection<ImplicitDependency> implicitDependencies)
  {
    Set<NameVersionDescriptor> result = new LinkedHashSet<NameVersionDescriptor>();
    for (ImplicitDependency implicitDependency : implicitDependencies)
    {
      Version version = implicitDependency.getVersion();
      result.add(new NameVersionDescriptor(implicitDependency.getID(), version == null ? null : version.toString()));
    }

    return result;
  }

  private static boolean equalNameVersionDescriptors(NameVersionDescriptor[] targetImplicitDependencies, EList<ImplicitDependency> targletImplicitDependencies)
  {
    Set<NameVersionDescriptor> targetSet = new HashSet<NameVersionDescriptor>();
    if (targetImplicitDependencies != null)
    {
      for (int i = 0; i < targetImplicitDependencies.length; i++)
      {
        targetSet.add(targetImplicitDependencies[i]);
      }
    }

    Set<NameVersionDescriptor> targletSet = createNameVersionDescriptors(targletImplicitDependencies);
    return targetSet.equals(targletSet);
  }

  private static boolean hasRequirements(EList<Targlet> targlets)
  {
    for (Targlet targlet : targlets)
    {
      if (!targlet.getRequirements().isEmpty())
      {
        return true;
      }
    }

    return false;
  }

} // TargletTaskImpl
