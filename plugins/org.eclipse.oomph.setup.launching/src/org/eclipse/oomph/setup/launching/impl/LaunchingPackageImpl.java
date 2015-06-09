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
package org.eclipse.oomph.setup.launching.impl;

import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.launching.LaunchTask;
import org.eclipse.oomph.setup.launching.LaunchingFactory;
import org.eclipse.oomph.setup.launching.LaunchingPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class LaunchingPackageImpl extends EPackageImpl implements LaunchingPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass launchTaskEClass = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see org.eclipse.oomph.setup.launching.LaunchingPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private LaunchingPackageImpl()
  {
    super(eNS_URI, LaunchingFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   *
   * <p>This method is used to initialize {@link LaunchingPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static LaunchingPackage init()
  {
    if (isInited)
    {
      return (LaunchingPackage)EPackage.Registry.INSTANCE.getEPackage(LaunchingPackage.eNS_URI);
    }

    // Obtain or create and register package
    LaunchingPackageImpl theLaunchingPackage = (LaunchingPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof LaunchingPackageImpl
        ? EPackage.Registry.INSTANCE.get(eNS_URI) : new LaunchingPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    SetupPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theLaunchingPackage.createPackageContents();

    // Initialize created meta-data
    theLaunchingPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theLaunchingPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(LaunchingPackage.eNS_URI, theLaunchingPackage);
    return theLaunchingPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getLaunchTask()
  {
    return launchTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getLaunchTask_Launcher()
  {
    return (EAttribute)launchTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public LaunchingFactory getLaunchingFactory()
  {
    return (LaunchingFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated)
    {
      return;
    }
    isCreated = true;

    // Create classes and their features
    launchTaskEClass = createEClass(LAUNCH_TASK);
    createEAttribute(launchTaskEClass, LAUNCH_TASK__LAUNCHER);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized)
    {
      return;
    }
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    SetupPackage theSetupPackage = (SetupPackage)EPackage.Registry.INSTANCE.getEPackage(SetupPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    launchTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());

    // Initialize classes and features; add operations and parameters
    initEClass(launchTaskEClass, LaunchTask.class, "LaunchTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getLaunchTask_Launcher(), ecorePackage.getEString(), "launcher", null, 1, 1, LaunchTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Launching.ecore");

    // Create annotations
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
    // http://www.eclipse.org/oomph/setup/Enablement
    createEnablementAnnotations();
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
    // http://www.eclipse.org/oomph/setup/ValidTriggers
    createValidTriggersAnnotations();
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/Ecore</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createEcoreAnnotations()
  {
    String source = "http://www.eclipse.org/emf/2002/Ecore";
    addAnnotation(this, source, new String[] { "schemaLocation", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Launching.ecore" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/Enablement</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createEnablementAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/Enablement";
    addAnnotation(this, source, new String[] { "variableName", "setup.launching.p2", "repository", "${oomph.update.url}", "installableUnits",
        "org.eclipse.oomph.setup.launching.feature.group" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/base/LabelProvider</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createLabelProviderAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/base/LabelProvider";
    addAnnotation(this, source, new String[] { "imageBaseURI",
        "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.launching.edit/icons/full/obj16" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/ValidTriggers</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createValidTriggersAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/ValidTriggers";
    addAnnotation(launchTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" });
  }

} // LaunchingPackageImpl
