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
package org.eclipse.oomph.setup.pde.impl;

import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.pde.APIBaselineFromTargetTask;
import org.eclipse.oomph.setup.pde.APIBaselineTask;
import org.eclipse.oomph.setup.pde.AbstractAPIBaselineTask;
import org.eclipse.oomph.setup.pde.PDEFactory;
import org.eclipse.oomph.setup.pde.PDEPackage;
import org.eclipse.oomph.setup.pde.TargetPlatformTask;

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
public class PDEPackageImpl extends EPackageImpl implements PDEPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass targetPlatformTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass abstractAPIBaselineTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass apiBaselineTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass apiBaselineFromTargetTaskEClass = null;

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
   * @see org.eclipse.oomph.setup.pde.PDEPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private PDEPackageImpl()
  {
    super(eNS_URI, PDEFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link PDEPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static PDEPackage init()
  {
    if (isInited)
    {
      return (PDEPackage)EPackage.Registry.INSTANCE.getEPackage(PDEPackage.eNS_URI);
    }

    // Obtain or create and register package
    PDEPackageImpl thePDEPackage = (PDEPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof PDEPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
        : new PDEPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    SetupPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    thePDEPackage.createPackageContents();

    // Initialize created meta-data
    thePDEPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    thePDEPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(PDEPackage.eNS_URI, thePDEPackage);
    return thePDEPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTargetPlatformTask()
  {
    return targetPlatformTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTargetPlatformTask_Name()
  {
    return (EAttribute)targetPlatformTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getAbstractAPIBaselineTask()
  {
    return abstractAPIBaselineTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAbstractAPIBaselineTask_Name()
  {
    return (EAttribute)abstractAPIBaselineTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAbstractAPIBaselineTask_Version()
  {
    return (EAttribute)abstractAPIBaselineTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAbstractAPIBaselineTask_Activate()
  {
    return (EAttribute)abstractAPIBaselineTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getAPIBaselineTask()
  {
    return apiBaselineTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAPIBaselineTask_Location()
  {
    return (EAttribute)apiBaselineTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAPIBaselineTask_RemoteURI()
  {
    return (EAttribute)apiBaselineTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getAPIBaselineFromTargetTask()
  {
    return apiBaselineFromTargetTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAPIBaselineFromTargetTask_TargetName()
  {
    return (EAttribute)apiBaselineFromTargetTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public PDEFactory getPDEFactory()
  {
    return (PDEFactory)getEFactoryInstance();
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
    targetPlatformTaskEClass = createEClass(TARGET_PLATFORM_TASK);
    createEAttribute(targetPlatformTaskEClass, TARGET_PLATFORM_TASK__NAME);

    abstractAPIBaselineTaskEClass = createEClass(ABSTRACT_API_BASELINE_TASK);
    createEAttribute(abstractAPIBaselineTaskEClass, ABSTRACT_API_BASELINE_TASK__NAME);
    createEAttribute(abstractAPIBaselineTaskEClass, ABSTRACT_API_BASELINE_TASK__VERSION);
    createEAttribute(abstractAPIBaselineTaskEClass, ABSTRACT_API_BASELINE_TASK__ACTIVATE);

    apiBaselineTaskEClass = createEClass(API_BASELINE_TASK);
    createEAttribute(apiBaselineTaskEClass, API_BASELINE_TASK__LOCATION);
    createEAttribute(apiBaselineTaskEClass, API_BASELINE_TASK__REMOTE_URI);

    apiBaselineFromTargetTaskEClass = createEClass(API_BASELINE_FROM_TARGET_TASK);
    createEAttribute(apiBaselineFromTargetTaskEClass, API_BASELINE_FROM_TARGET_TASK__TARGET_NAME);
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
    targetPlatformTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());
    abstractAPIBaselineTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());
    apiBaselineTaskEClass.getESuperTypes().add(getAbstractAPIBaselineTask());
    apiBaselineFromTargetTaskEClass.getESuperTypes().add(getAbstractAPIBaselineTask());

    // Initialize classes and features; add operations and parameters
    initEClass(targetPlatformTaskEClass, TargetPlatformTask.class, "TargetPlatformTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTargetPlatformTask_Name(), ecorePackage.getEString(), "name", null, 1, 1, TargetPlatformTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(abstractAPIBaselineTaskEClass, AbstractAPIBaselineTask.class, "AbstractAPIBaselineTask", IS_ABSTRACT, !IS_INTERFACE,
        IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getAbstractAPIBaselineTask_Name(), ecorePackage.getEString(), "name", null, 1, 1, AbstractAPIBaselineTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAbstractAPIBaselineTask_Version(), ecorePackage.getEString(), "version", null, 1, 1, AbstractAPIBaselineTask.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAbstractAPIBaselineTask_Activate(), ecorePackage.getEBoolean(), "activate", "true", 0, 1, AbstractAPIBaselineTask.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(apiBaselineTaskEClass, APIBaselineTask.class, "APIBaselineTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getAPIBaselineTask_Location(), ecorePackage.getEString(), "location", "", 1, 1, APIBaselineTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAPIBaselineTask_RemoteURI(), ecorePackage.getEString(), "remoteURI", null, 1, 1, APIBaselineTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(apiBaselineFromTargetTaskEClass, APIBaselineFromTargetTask.class, "APIBaselineFromTargetTask", !IS_ABSTRACT, !IS_INTERFACE,
        IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getAPIBaselineFromTargetTask_TargetName(), ecorePackage.getEString(), "targetName", "", 1, 1, APIBaselineFromTargetTask.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/PDE.ecore");

    // Create annotations
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
    // http://www.eclipse.org/oomph/setup/Enablement
    createEnablementAnnotations();
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
    // http://www.eclipse.org/oomph/setup/ValidTriggers
    createValidTriggersAnnotations();
    // http://www.eclipse.org/oomph/setup/Variable
    createVariableAnnotations();
    // http://www.eclipse.org/oomph/setup/RuleVariable
    createRuleVariableAnnotations();
    // http://www.eclipse.org/oomph/setup/RemoteResource
    createRemoteResourceAnnotations();
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
    addAnnotation(this, source, new String[] { "schemaLocation", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/PDE.ecore" });
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
    addAnnotation(this, source,
        new String[] { "variableName", "setup.pde.p2", "repository", "${oomph.update.url}", "installableUnits", "org.eclipse.oomph.setup.pde.feature.group" });
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
        "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.pde.edit/icons/full/obj16" });
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
    addAnnotation(targetPlatformTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" });
    addAnnotation(apiBaselineTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" });
    addAnnotation(apiBaselineFromTargetTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/Variable</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createVariableAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/Variable";
    addAnnotation(getAPIBaselineTask_Location(), source,
        new String[] { "type", "STRING", "label", "API baseline location rule", "description",
            "The rule for the absolute folder location where the API baseline is located", "explicitType", "FOLDER", "explicitLabel",
            "${@id.name}-${@id.version} API baseline location", "explicitDescription",
            "The absolute folder location where the ${@id.name}-${@id.version} API baseline is located" });
    addAnnotation(getAPIBaselineTask_Location(), new boolean[] { true }, "Choice",
        new String[] { "value", "${api.baselines.root/}${@id.name|lower}-${@id.version}", "label",
            "Located in a folder named \'<name>-<version>\' within the root API baselines folder" });
    addAnnotation(getAPIBaselineTask_Location(), new boolean[] { true }, "Choice",
        new String[] { "value", "${installation.location/baselines/}${@id.name|lower}-${@id.version}", "label",
            "Located in a folder named \'baselines/<name>-<version>\' within the installation folder" });
    addAnnotation(getAPIBaselineTask_Location(), new boolean[] { true }, "Choice",
        new String[] { "value", "${workspace.location/.baselines/}${@id.name|lower}-${@id.version}", "label",
            "Located in a folder named \'.baselines/<name>-<version>\' within the workspace folder" });
    addAnnotation(getAPIBaselineTask_Location(), new boolean[] { true }, "Choice",
        new String[] { "value", "${@id.location}", "label", "Located in the specified absolute folder location" });
    addAnnotation(getAPIBaselineFromTargetTask_TargetName(), source,
        new String[] { "type", "STRING", "label", "API baseline location rule", "description",
            "The rule for the absolute folder location where the API baseline is located", "explicitType", "FOLDER", "explicitLabel",
            "${@id.name}-${@id.version} API baseline location", "explicitDescription",
            "The absolute folder location where the ${@id.name}-${@id.version} API baseline is located" });
    addAnnotation(getAPIBaselineFromTargetTask_TargetName(), new boolean[] { true }, "Choice",
        new String[] { "value", "${api.baselines.root/}${@id.name|lower}-${@id.version}", "label",
            "Located in a folder named \'<name>-<version>\' within the root API baselines folder" });
    addAnnotation(getAPIBaselineFromTargetTask_TargetName(), new boolean[] { true }, "Choice",
        new String[] { "value", "${installation.location/baselines/}${@id.name|lower}-${@id.version}", "label",
            "Located in a folder named \'baselines/<name>-<version>\' within the installation folder" });
    addAnnotation(getAPIBaselineFromTargetTask_TargetName(), new boolean[] { true }, "Choice",
        new String[] { "value", "${workspace.location/.baselines/}${@id.name|lower}-${@id.version}", "label",
            "Located in a folder named \'.baselines/<name>-<version>\' within the workspace folder" });
    addAnnotation(getAPIBaselineFromTargetTask_TargetName(), new boolean[] { true }, "Choice",
        new String[] { "value", "${@id.location}", "label", "Located in the specified absolute folder location" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/RuleVariable</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createRuleVariableAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/RuleVariable";
    addAnnotation(getAPIBaselineTask_Location(), source, new String[] { "name", "api.baselines.root", "type", "FOLDER", "label", "Root API baselines folder",
        "description", "The root API baselines folder where all the API baselines are located", "storageURI", "scope://" });
    addAnnotation(getAPIBaselineFromTargetTask_TargetName(), source, new String[] { "name", "api.baselines.root", "type", "FOLDER", "label",
        "Root API baselines folder", "description", "The root API baselines folder where all the API baselines are located", "storageURI", "scope://" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/RemoteResource</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createRemoteResourceAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/RemoteResource";
    addAnnotation(getAPIBaselineTask_RemoteURI(), source, new String[] {});
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
    addAnnotation(getAPIBaselineTask_RemoteURI(), source, new String[] {});
  }

} // PDEPackageImpl
