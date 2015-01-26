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
package org.eclipse.oomph.setup.targlets.impl;

import org.eclipse.oomph.p2.P2Package;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.targlets.ImplicitDependency;
import org.eclipse.oomph.setup.targlets.SetupTargletsFactory;
import org.eclipse.oomph.setup.targlets.SetupTargletsPackage;
import org.eclipse.oomph.setup.targlets.TargletTask;
import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SetupTargletsPackageImpl extends EPackageImpl implements SetupTargletsPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass targletTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass implicitDependencyEClass = null;

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
   * @see org.eclipse.oomph.setup.targlets.SetupTargletsPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private SetupTargletsPackageImpl()
  {
    super(eNS_URI, SetupTargletsFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link SetupTargletsPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static SetupTargletsPackage init()
  {
    if (isInited)
    {
      return (SetupTargletsPackage)EPackage.Registry.INSTANCE.getEPackage(SetupTargletsPackage.eNS_URI);
    }

    // Obtain or create and register package
    SetupTargletsPackageImpl theSetupTargletsPackage = (SetupTargletsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof SetupTargletsPackageImpl ? EPackage.Registry.INSTANCE
        .get(eNS_URI) : new SetupTargletsPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    SetupPackage.eINSTANCE.eClass();
    TargletPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theSetupTargletsPackage.createPackageContents();

    // Initialize created meta-data
    theSetupTargletsPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theSetupTargletsPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(SetupTargletsPackage.eNS_URI, theSetupTargletsPackage);
    return theSetupTargletsPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTargletTask()
  {
    return targletTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTargletTask_Targlets()
  {
    return (EReference)targletTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTargletTask_TargletURIs()
  {
    return (EAttribute)targletTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTargletTask_OperatingSystem()
  {
    return (EAttribute)targletTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTargletTask_WindowingSystem()
  {
    return (EAttribute)targletTaskEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTargletTask_Architecture()
  {
    return (EAttribute)targletTaskEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTargletTask_Locale()
  {
    return (EAttribute)targletTaskEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTargletTask_ProgramArguments()
  {
    return (EAttribute)targletTaskEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTargletTask_VMArguments()
  {
    return (EAttribute)targletTaskEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTargletTask_ImplicitDependencies()
  {
    return (EReference)targletTaskEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getImplicitDependency()
  {
    return implicitDependencyEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getImplicitDependency_ID()
  {
    return (EAttribute)implicitDependencyEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getImplicitDependency_Version()
  {
    return (EAttribute)implicitDependencyEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupTargletsFactory getSetupTargletsFactory()
  {
    return (SetupTargletsFactory)getEFactoryInstance();
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
    targletTaskEClass = createEClass(TARGLET_TASK);
    createEReference(targletTaskEClass, TARGLET_TASK__TARGLETS);
    createEAttribute(targletTaskEClass, TARGLET_TASK__TARGLET_UR_IS);
    createEAttribute(targletTaskEClass, TARGLET_TASK__OPERATING_SYSTEM);
    createEAttribute(targletTaskEClass, TARGLET_TASK__WINDOWING_SYSTEM);
    createEAttribute(targletTaskEClass, TARGLET_TASK__ARCHITECTURE);
    createEAttribute(targletTaskEClass, TARGLET_TASK__LOCALE);
    createEAttribute(targletTaskEClass, TARGLET_TASK__PROGRAM_ARGUMENTS);
    createEAttribute(targletTaskEClass, TARGLET_TASK__VM_ARGUMENTS);
    createEReference(targletTaskEClass, TARGLET_TASK__IMPLICIT_DEPENDENCIES);

    implicitDependencyEClass = createEClass(IMPLICIT_DEPENDENCY);
    createEAttribute(implicitDependencyEClass, IMPLICIT_DEPENDENCY__ID);
    createEAttribute(implicitDependencyEClass, IMPLICIT_DEPENDENCY__VERSION);
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
    TargletPackage theTargletPackage = (TargletPackage)EPackage.Registry.INSTANCE.getEPackage(TargletPackage.eNS_URI);
    P2Package theP2Package = (P2Package)EPackage.Registry.INSTANCE.getEPackage(P2Package.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    targletTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());

    // Initialize classes and features; add operations and parameters
    initEClass(targletTaskEClass, TargletTask.class, "TargletTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTargletTask_Targlets(), theTargletPackage.getTarglet(), null, "targlets", null, 0, -1, TargletTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTargletTask_TargletURIs(), ecorePackage.getEString(), "targletURIs", null, 0, -1, TargletTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTargletTask_OperatingSystem(), ecorePackage.getEString(), "operatingSystem", null, 0, 1, TargletTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTargletTask_WindowingSystem(), ecorePackage.getEString(), "windowingSystem", null, 0, 1, TargletTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTargletTask_Architecture(), ecorePackage.getEString(), "architecture", null, 0, 1, TargletTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTargletTask_Locale(), ecorePackage.getEString(), "locale", null, 0, 1, TargletTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTargletTask_ProgramArguments(), ecorePackage.getEString(), "programArguments", null, 0, 1, TargletTask.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTargletTask_VMArguments(), ecorePackage.getEString(), "vMArguments", null, 0, 1, TargletTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTargletTask_ImplicitDependencies(), getImplicitDependency(), null, "implicitDependencies", null, 0, -1, TargletTask.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(implicitDependencyEClass, ImplicitDependency.class, "ImplicitDependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getImplicitDependency_ID(), ecorePackage.getEString(), "iD", null, 1, 1, ImplicitDependency.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getImplicitDependency_Version(), theP2Package.getVersion(), "version", "0.0.0", 0, 1, ImplicitDependency.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/SetupTarglets.ecore");

    // Create annotations
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
    // http://www.eclipse.org/oomph/setup/Enablement
    createEnablementAnnotations();
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
    // http://www.eclipse.org/oomph/setup/ValidTriggers
    createValidTriggersAnnotations();
    // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
    createExtendedMetaDataAnnotations();
    // http://www.eclipse.org/oomph/setup/Redirect
    createRedirectAnnotations();
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
    addAnnotation(this, source,
        new String[] { "schemaLocation", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/SetupTarglets.ecore" });
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
    addAnnotation(this, source, new String[] { "variableName", "setup.targlets.p2", "repository", "${oomph.update.url}", "installableUnits",
        "org.eclipse.oomph.targlets.feature.group org.eclipse.oomph.setup.targlets.feature.group" });
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
        "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.targlets.edit/icons/full/obj16" });
    addAnnotation(targletTaskEClass, source, new String[] { "text", "Targlets" });
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
    addAnnotation(targletTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" });
  }

  /**
   * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createExtendedMetaDataAnnotations()
  {
    String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData";
    addAnnotation(getTargletTask_Targlets(), source, new String[] { "name", "targlet" });
    addAnnotation(getTargletTask_TargletURIs(), source, new String[] { "name", "targletURI" });
    addAnnotation(getTargletTask_VMArguments(), source, new String[] { "kind", "attribute", "name", "vmArguments" });
    addAnnotation(getTargletTask_ImplicitDependencies(), source, new String[] { "name", "implicitDependency" });
    addAnnotation(getImplicitDependency_ID(), source, new String[] { "kind", "attribute", "name", "id" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/Redirect</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createRedirectAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/Redirect";
    addAnnotation(getTargletTask_TargletURIs(), source, new String[] {});
  }

} // SetupTargletsPackageImpl
