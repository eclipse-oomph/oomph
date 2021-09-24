/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.maven.impl;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.predicates.PredicatesPackage;
import org.eclipse.oomph.resources.ResourcesPackage;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.maven.MavenFactory;
import org.eclipse.oomph.setup.maven.MavenImportTask;
import org.eclipse.oomph.setup.maven.MavenPackage;
import org.eclipse.oomph.setup.maven.MavenUpdateTask;

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
public class MavenPackageImpl extends EPackageImpl implements MavenPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mavenImportTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mavenUpdateTaskEClass = null;

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
   * @see org.eclipse.oomph.setup.maven.MavenPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private MavenPackageImpl()
  {
    super(eNS_URI, MavenFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link MavenPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static MavenPackage init()
  {
    if (isInited)
    {
      return (MavenPackage)EPackage.Registry.INSTANCE.getEPackage(MavenPackage.eNS_URI);
    }

    // Obtain or create and register package
    Object registeredMavenPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
    MavenPackageImpl theMavenPackage = registeredMavenPackage instanceof MavenPackageImpl ? (MavenPackageImpl)registeredMavenPackage : new MavenPackageImpl();

    isInited = true;

    // Initialize simple dependencies
    BasePackage.eINSTANCE.eClass();
    PredicatesPackage.eINSTANCE.eClass();
    ResourcesPackage.eINSTANCE.eClass();
    SetupPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theMavenPackage.createPackageContents();

    // Initialize created meta-data
    theMavenPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theMavenPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(MavenPackage.eNS_URI, theMavenPackage);
    return theMavenPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMavenImportTask()
  {
    return mavenImportTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMavenImportTask_SourceLocators()
  {
    return (EReference)mavenImportTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMavenImportTask_ProjectNameTemplate()
  {
    return (EAttribute)mavenImportTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMavenImportTask_Profiles()
  {
    return (EAttribute)mavenImportTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMavenUpdateTask()
  {
    return mavenUpdateTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMavenUpdateTask_ProjectNamePatterns()
  {
    return (EAttribute)mavenUpdateTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMavenUpdateTask_Offline()
  {
    return (EAttribute)mavenUpdateTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMavenUpdateTask_UpdateSnapshots()
  {
    return (EAttribute)mavenUpdateTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MavenFactory getMavenFactory()
  {
    return (MavenFactory)getEFactoryInstance();
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
    mavenImportTaskEClass = createEClass(MAVEN_IMPORT_TASK);
    createEReference(mavenImportTaskEClass, MAVEN_IMPORT_TASK__SOURCE_LOCATORS);
    createEAttribute(mavenImportTaskEClass, MAVEN_IMPORT_TASK__PROJECT_NAME_TEMPLATE);
    createEAttribute(mavenImportTaskEClass, MAVEN_IMPORT_TASK__PROFILES);

    mavenUpdateTaskEClass = createEClass(MAVEN_UPDATE_TASK);
    createEAttribute(mavenUpdateTaskEClass, MAVEN_UPDATE_TASK__PROJECT_NAME_PATTERNS);
    createEAttribute(mavenUpdateTaskEClass, MAVEN_UPDATE_TASK__OFFLINE);
    createEAttribute(mavenUpdateTaskEClass, MAVEN_UPDATE_TASK__UPDATE_SNAPSHOTS);
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
  @SuppressWarnings("nls")
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
    ResourcesPackage theResourcesPackage = (ResourcesPackage)EPackage.Registry.INSTANCE.getEPackage(ResourcesPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    mavenImportTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());
    mavenUpdateTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());

    // Initialize classes and features; add operations and parameters
    initEClass(mavenImportTaskEClass, MavenImportTask.class, "MavenImportTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getMavenImportTask_SourceLocators(), theResourcesPackage.getSourceLocator(), null, "sourceLocators", null, 1, -1, MavenImportTask.class, //$NON-NLS-1$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMavenImportTask_ProjectNameTemplate(), ecorePackage.getEString(), "projectNameTemplate", null, 0, 1, MavenImportTask.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMavenImportTask_Profiles(), ecorePackage.getEString(), "profiles", null, 0, -1, MavenImportTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mavenUpdateTaskEClass, MavenUpdateTask.class, "MavenUpdateTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getMavenUpdateTask_ProjectNamePatterns(), ecorePackage.getEString(), "projectNamePatterns", null, 0, -1, MavenUpdateTask.class, //$NON-NLS-1$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMavenUpdateTask_Offline(), ecorePackage.getEBoolean(), "offline", null, 0, 1, MavenUpdateTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMavenUpdateTask_UpdateSnapshots(), ecorePackage.getEBoolean(), "updateSnapshots", null, 0, 1, MavenUpdateTask.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Maven.ecore");

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
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/Ecore</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createEcoreAnnotations()
  {
    String source = "http://www.eclipse.org/emf/2002/Ecore"; //$NON-NLS-1$
    addAnnotation(this, source, new String[] { "schemaLocation", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Maven.ecore" //$NON-NLS-1$ //$NON-NLS-2$
    });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/Enablement</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createEnablementAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/Enablement"; //$NON-NLS-1$
    addAnnotation(this, source, new String[] { "variableName", "setup.maven.p2", //$NON-NLS-1$ //$NON-NLS-2$
        "repository", "${oomph.update.url}", //$NON-NLS-1$ //$NON-NLS-2$
        "installableUnits", "org.eclipse.oomph.setup.maven.feature.group" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(this, source, new String[] { "variableName", "setup.m2e.p2", //$NON-NLS-1$ //$NON-NLS-2$
        "repository", "https://download.eclipse.org/technology/m2e/releases/latest", //$NON-NLS-1$ //$NON-NLS-2$
        "installableUnits", "org.eclipse.m2e.feature.feature.group", //$NON-NLS-1$ //$NON-NLS-2$
        "releaseTrainAlternate", "true" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(this, source, new String[] { "variableName", "setup.webtools.p2", //$NON-NLS-1$ //$NON-NLS-2$
        "repository", "https://download.eclipse.org/webtools/repository/latest", //$NON-NLS-1$ //$NON-NLS-2$
        "releaseTrainAlternate", "true" //$NON-NLS-1$ //$NON-NLS-2$
    });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/base/LabelProvider</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createLabelProviderAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/base/LabelProvider"; //$NON-NLS-1$
    addAnnotation(this, source,
        new String[] { "imageBaseURI", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.maven.edit/icons/full/obj16" //$NON-NLS-1$ //$NON-NLS-2$
        });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/ValidTriggers</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createValidTriggersAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/ValidTriggers"; //$NON-NLS-1$
    addAnnotation(mavenImportTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(mavenUpdateTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" //$NON-NLS-1$ //$NON-NLS-2$
    });
  }

  /**
   * Initializes the annotations for <b>http:///org/eclipse/emf/ecore/util/ExtendedMetaData</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createExtendedMetaDataAnnotations()
  {
    String source = "http:///org/eclipse/emf/ecore/util/ExtendedMetaData"; //$NON-NLS-1$
    addAnnotation(getMavenImportTask_SourceLocators(), source, new String[] { "name", "sourceLocator" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getMavenImportTask_Profiles(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
        "name", "profile" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getMavenUpdateTask_ProjectNamePatterns(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
        "name", "projectNamePattern" //$NON-NLS-1$ //$NON-NLS-2$
    });
  }

} // MavenPackageImpl
