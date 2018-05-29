/*
 * Copyright (c) 2014, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.jdt.impl;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.jdt.JDTFactory;
import org.eclipse.oomph.setup.jdt.JDTPackage;
import org.eclipse.oomph.setup.jdt.JRELibrary;
import org.eclipse.oomph.setup.jdt.JRETask;

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
public class JDTPackageImpl extends EPackageImpl implements JDTPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jreTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass jreLibraryEClass = null;

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
   * @see org.eclipse.oomph.setup.jdt.JDTPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private JDTPackageImpl()
  {
    super(eNS_URI, JDTFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link JDTPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static JDTPackage init()
  {
    if (isInited)
    {
      return (JDTPackage)EPackage.Registry.INSTANCE.getEPackage(JDTPackage.eNS_URI);
    }

    // Obtain or create and register package
    Object registeredJDTPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
    JDTPackageImpl theJDTPackage = registeredJDTPackage instanceof JDTPackageImpl ? (JDTPackageImpl)registeredJDTPackage : new JDTPackageImpl();

    isInited = true;

    // Initialize simple dependencies
    BasePackage.eINSTANCE.eClass();
    SetupPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theJDTPackage.createPackageContents();

    // Initialize created meta-data
    theJDTPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theJDTPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(JDTPackage.eNS_URI, theJDTPackage);
    return theJDTPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJRETask()
  {
    return jreTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRETask_Version()
  {
    return (EAttribute)jreTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRETask_Location()
  {
    return (EAttribute)jreTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRETask_Name()
  {
    return (EAttribute)jreTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRETask_VMInstallType()
  {
    return (EAttribute)jreTaskEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRETask_ExecutionEnvironmentDefault()
  {
    return (EAttribute)jreTaskEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRETask_VMArguments()
  {
    return (EAttribute)jreTaskEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getJRETask_JRELibraries()
  {
    return (EReference)jreTaskEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getJRELibrary()
  {
    return jreLibraryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRELibrary_LibraryPath()
  {
    return (EAttribute)jreLibraryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getJRELibrary_ExternalAnnotationsPath()
  {
    return (EAttribute)jreLibraryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JDTFactory getJDTFactory()
  {
    return (JDTFactory)getEFactoryInstance();
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
    jreTaskEClass = createEClass(JRE_TASK);
    createEAttribute(jreTaskEClass, JRE_TASK__VERSION);
    createEAttribute(jreTaskEClass, JRE_TASK__LOCATION);
    createEAttribute(jreTaskEClass, JRE_TASK__NAME);
    createEAttribute(jreTaskEClass, JRE_TASK__VM_INSTALL_TYPE);
    createEAttribute(jreTaskEClass, JRE_TASK__EXECUTION_ENVIRONMENT_DEFAULT);
    createEAttribute(jreTaskEClass, JRE_TASK__VM_ARGUMENTS);
    createEReference(jreTaskEClass, JRE_TASK__JRE_LIBRARIES);

    jreLibraryEClass = createEClass(JRE_LIBRARY);
    createEAttribute(jreLibraryEClass, JRE_LIBRARY__LIBRARY_PATH);
    createEAttribute(jreLibraryEClass, JRE_LIBRARY__EXTERNAL_ANNOTATIONS_PATH);
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
    jreTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());

    // Initialize classes and features; add operations and parameters
    initEClass(jreTaskEClass, JRETask.class, "JRETask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getJRETask_Version(), ecorePackage.getEString(), "version", null, 1, 1, JRETask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getJRETask_Location(), ecorePackage.getEString(), "location", null, 1, 1, JRETask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getJRETask_Name(), ecorePackage.getEString(), "name", null, 0, 1, JRETask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getJRETask_VMInstallType(), ecorePackage.getEString(), "vMInstallType", "org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType", 1, 1,
        JRETask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getJRETask_ExecutionEnvironmentDefault(), ecorePackage.getEBoolean(), "executionEnvironmentDefault", "true", 0, 1, JRETask.class,
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getJRETask_VMArguments(), ecorePackage.getEString(), "vMArguments", null, 0, 1, JRETask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getJRETask_JRELibraries(), getJRELibrary(), null, "jRELibraries", null, 0, -1, JRETask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(jreLibraryEClass, JRELibrary.class, "JRELibrary", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getJRELibrary_LibraryPath(), ecorePackage.getEString(), "libraryPath", null, 1, 1, JRELibrary.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getJRELibrary_ExternalAnnotationsPath(), ecorePackage.getEString(), "externalAnnotationsPath", null, 1, 1, JRELibrary.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/JDT.ecore");

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
    // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
    createExtendedMetaDataAnnotations();
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
    addAnnotation(this, source, new String[] { "schemaLocation", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/JDT.ecore" });
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
    addAnnotation(jreTaskEClass, source, new String[] { "name", "jre.location-1.1", "type", "JRE", "label", "JRE 1.1 Location", "description",
        "The location of a JDK or JRE compatible with Java 1.1." });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.2}", "label", "JRE 1.2 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.3}", "label", "JRE 1.3 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.4}", "label", "JRE 1.4 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.5}", "label", "JRE 1.5 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.6}", "label", "JRE 1.6 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.7}", "label", "JRE 1.7 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.8}", "label", "JRE 1.8 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-9}", "label", "JRE 9 Location" });
    addAnnotation(jreTaskEClass, source, new String[] { "name", "jre.location-1.2", "type", "JRE", "label", "JRE 1.2 Location", "description",
        "The location of a JDK or JRE compatible with Java 1.2." });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.3}", "label", "JRE 1.3 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.4}", "label", "JRE 1.4 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.5}", "label", "JRE 1.5 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.6}", "label", "JRE 1.6 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.7}", "label", "JRE 1.7 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.8}", "label", "JRE 1.8 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-9}", "label", "JRE 9 Location" });
    addAnnotation(jreTaskEClass, source, new String[] { "name", "jre.location-1.3", "type", "JRE", "label", "JRE 1.3 Location", "description",
        "The location of a JDK or JRE compatible with Java 1.3." });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.4}", "label", "JRE 1.4 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.5}", "label", "JRE 1.5 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.6}", "label", "JRE 1.6 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.7}", "label", "JRE 1.7 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.8}", "label", "JRE 1.8 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-9}", "label", "JRE 9 Location" });
    addAnnotation(jreTaskEClass, source, new String[] { "name", "jre.location-1.4", "type", "JRE", "label", "JRE 1.4 Location", "description",
        "The location of a JDK or JRE compatible with Java 1.4." });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.5}", "label", "JRE 1.5 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.6}", "label", "JRE 1.6 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.7}", "label", "JRE 1.7 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.8}", "label", "JRE 1.8 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-9}", "label", "JRE 9 Location" });
    addAnnotation(jreTaskEClass, source, new String[] { "name", "jre.location-1.5", "type", "JRE", "label", "JRE 1.5 Location", "description",
        "The location of a JDK or JRE compatible with Java 1.5." });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.6}", "label", "JRE 1.6 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.7}", "label", "JRE 1.7 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.8}", "label", "JRE 1.8 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-9}", "label", "JRE 9 Location" });
    addAnnotation(jreTaskEClass, source, new String[] { "name", "jre.location-1.6", "type", "JRE", "label", "JRE 1.6 Location", "description",
        "The location of a JDK or JRE compatible with Java 1.6." });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.7}", "label", "JRE 1.7 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.8}", "label", "JRE 1.8 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-9}", "label", "JRE 9 Location" });
    addAnnotation(jreTaskEClass, source, new String[] { "name", "jre.location-1.7", "type", "JRE", "label", "JRE 1.7 Location", "description",
        "The location of a JDK or JRE compatible with Java 1.7." });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-1.8}", "label", "JRE 1.8 Location" });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-9}", "label", "JRE 9 Location" });
    addAnnotation(jreTaskEClass, source, new String[] { "name", "jre.location-1.8", "type", "JRE", "label", "JRE 1.8 Location", "description",
        "The location of a JDK or JRE compatible with Java 1.8." });
    addAnnotation(jreTaskEClass, new boolean[] { true }, "Choice", new String[] { "value", "${jre.location-9}", "label", "JRE 9 Location" });
    addAnnotation(jreTaskEClass, source, new String[] { "name", "jre.location-9", "type", "JRE", "label", "JRE 9 Location", "description",
        "The location of a JDK or JRE compatible with Java 9." });
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
    addAnnotation(getJRETask_VMInstallType(), source, new String[] { "kind", "attribute", "name", "vmInstallType" });
    addAnnotation(getJRETask_VMArguments(), source, new String[] { "kind", "attribute", "name", "vmArguments" });
    addAnnotation(getJRETask_JRELibraries(), source, new String[] { "name", "jreLibrary" });
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
        new String[] { "variableName", "setup.jdt.p2", "repository", "${oomph.update.url}", "installableUnits", "org.eclipse.oomph.setup.jdt.feature.group" });
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
        "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.jdt.edit/icons/full/obj16" });
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
    addAnnotation(jreTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" });
  }

} // JDTPackageImpl
