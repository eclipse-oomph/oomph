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
package org.eclipse.oomph.setup.mylyn.impl;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.mylyn.BuildPlan;
import org.eclipse.oomph.setup.mylyn.MylynBuildsTask;
import org.eclipse.oomph.setup.mylyn.MylynFactory;
import org.eclipse.oomph.setup.mylyn.MylynPackage;
import org.eclipse.oomph.setup.mylyn.MylynQueriesTask;
import org.eclipse.oomph.setup.mylyn.Query;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MylynPackageImpl extends EPackageImpl implements MylynPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mylynQueriesTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass mylynBuildsTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass buildPlanEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass queryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass queryAttributeEClass = null;

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
   * @see org.eclipse.oomph.setup.mylyn.MylynPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private MylynPackageImpl()
  {
    super(eNS_URI, MylynFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link MylynPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static MylynPackage init()
  {
    if (isInited)
    {
      return (MylynPackage)EPackage.Registry.INSTANCE.getEPackage(MylynPackage.eNS_URI);
    }

    // Obtain or create and register package
    MylynPackageImpl theMylynPackage = (MylynPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MylynPackageImpl
        ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MylynPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    SetupPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theMylynPackage.createPackageContents();

    // Initialize created meta-data
    theMylynPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theMylynPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(MylynPackage.eNS_URI, theMylynPackage);
    return theMylynPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMylynQueriesTask()
  {
    return mylynQueriesTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMylynQueriesTask_ConnectorKind()
  {
    return (EAttribute)mylynQueriesTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMylynQueriesTask_RepositoryURL()
  {
    return (EAttribute)mylynQueriesTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMylynQueriesTask_UserID()
  {
    return (EAttribute)mylynQueriesTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMylynQueriesTask_Password()
  {
    return (EAttribute)mylynQueriesTaskEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMylynQueriesTask_Queries()
  {
    return (EReference)mylynQueriesTaskEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMylynBuildsTask()
  {
    return mylynBuildsTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMylynBuildsTask_ConnectorKind()
  {
    return (EAttribute)mylynBuildsTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMylynBuildsTask_ServerURL()
  {
    return (EAttribute)mylynBuildsTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMylynBuildsTask_UserID()
  {
    return (EAttribute)mylynBuildsTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getMylynBuildsTask_Password()
  {
    return (EAttribute)mylynBuildsTaskEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMylynBuildsTask_BuildPlans()
  {
    return (EReference)mylynBuildsTaskEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getBuildPlan()
  {
    return buildPlanEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getBuildPlan_Name()
  {
    return (EAttribute)buildPlanEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getQuery()
  {
    return queryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getQuery_Task()
  {
    return (EReference)queryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getQuery_Summary()
  {
    return (EAttribute)queryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getQuery_URL()
  {
    return (EAttribute)queryEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getQuery_Attributes()
  {
    return (EReference)queryEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getQueryAttribute()
  {
    return queryAttributeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getQueryAttribute_Key()
  {
    return (EAttribute)queryAttributeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getQueryAttribute_Value()
  {
    return (EAttribute)queryAttributeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MylynFactory getMylynFactory()
  {
    return (MylynFactory)getEFactoryInstance();
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
    mylynQueriesTaskEClass = createEClass(MYLYN_QUERIES_TASK);
    createEAttribute(mylynQueriesTaskEClass, MYLYN_QUERIES_TASK__CONNECTOR_KIND);
    createEAttribute(mylynQueriesTaskEClass, MYLYN_QUERIES_TASK__REPOSITORY_URL);
    createEAttribute(mylynQueriesTaskEClass, MYLYN_QUERIES_TASK__USER_ID);
    createEAttribute(mylynQueriesTaskEClass, MYLYN_QUERIES_TASK__PASSWORD);
    createEReference(mylynQueriesTaskEClass, MYLYN_QUERIES_TASK__QUERIES);

    mylynBuildsTaskEClass = createEClass(MYLYN_BUILDS_TASK);
    createEAttribute(mylynBuildsTaskEClass, MYLYN_BUILDS_TASK__CONNECTOR_KIND);
    createEAttribute(mylynBuildsTaskEClass, MYLYN_BUILDS_TASK__SERVER_URL);
    createEAttribute(mylynBuildsTaskEClass, MYLYN_BUILDS_TASK__USER_ID);
    createEAttribute(mylynBuildsTaskEClass, MYLYN_BUILDS_TASK__PASSWORD);
    createEReference(mylynBuildsTaskEClass, MYLYN_BUILDS_TASK__BUILD_PLANS);

    buildPlanEClass = createEClass(BUILD_PLAN);
    createEAttribute(buildPlanEClass, BUILD_PLAN__NAME);

    queryEClass = createEClass(QUERY);
    createEReference(queryEClass, QUERY__TASK);
    createEAttribute(queryEClass, QUERY__SUMMARY);
    createEAttribute(queryEClass, QUERY__URL);
    createEReference(queryEClass, QUERY__ATTRIBUTES);

    queryAttributeEClass = createEClass(QUERY_ATTRIBUTE);
    createEAttribute(queryAttributeEClass, QUERY_ATTRIBUTE__KEY);
    createEAttribute(queryAttributeEClass, QUERY_ATTRIBUTE__VALUE);
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
    BasePackage theBasePackage = (BasePackage)EPackage.Registry.INSTANCE.getEPackage(BasePackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    mylynQueriesTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());
    mylynBuildsTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());
    buildPlanEClass.getESuperTypes().add(theBasePackage.getModelElement());
    queryEClass.getESuperTypes().add(theBasePackage.getModelElement());

    // Initialize classes and features; add operations and parameters
    initEClass(mylynQueriesTaskEClass, MylynQueriesTask.class, "MylynQueriesTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMylynQueriesTask_ConnectorKind(), ecorePackage.getEString(), "connectorKind", "bugzilla", 1, 1, MylynQueriesTask.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMylynQueriesTask_RepositoryURL(), ecorePackage.getEString(), "repositoryURL", null, 1, 1, MylynQueriesTask.class, !IS_TRANSIENT,
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMylynQueriesTask_UserID(), ecorePackage.getEString(), "userID", null, 0, 1, MylynQueriesTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMylynQueriesTask_Password(), ecorePackage.getEString(), "password", null, 0, 1, MylynQueriesTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getMylynQueriesTask_Queries(), getQuery(), getQuery_Task(), "queries", null, 0, -1, MylynQueriesTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(mylynBuildsTaskEClass, MylynBuildsTask.class, "MylynBuildsTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getMylynBuildsTask_ConnectorKind(), ecorePackage.getEString(), "connectorKind", "org.eclipse.mylyn.hudson", 1, 1, MylynBuildsTask.class,
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMylynBuildsTask_ServerURL(), ecorePackage.getEString(), "serverURL", null, 1, 1, MylynBuildsTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMylynBuildsTask_UserID(), ecorePackage.getEString(), "userID", null, 0, 1, MylynBuildsTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getMylynBuildsTask_Password(), ecorePackage.getEString(), "password", null, 0, 1, MylynBuildsTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getMylynBuildsTask_BuildPlans(), getBuildPlan(), null, "buildPlans", null, 1, -1, MylynBuildsTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(buildPlanEClass, BuildPlan.class, "BuildPlan", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getBuildPlan_Name(), ecorePackage.getEString(), "name", null, 1, 1, BuildPlan.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(queryEClass, Query.class, "Query", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getQuery_Task(), getMylynQueriesTask(), getMylynQueriesTask_Queries(), "task", null, 0, 1, Query.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getQuery_Summary(), ecorePackage.getEString(), "summary", null, 1, 1, Query.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getQuery_URL(), ecorePackage.getEString(), "uRL", null, 0, 1, Query.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE,
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getQuery_Attributes(), getQueryAttribute(), null, "attributes", null, 0, -1, Query.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(queryAttributeEClass, Map.Entry.class, "QueryAttribute", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getQueryAttribute_Key(), ecorePackage.getEString(), "key", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getQueryAttribute_Value(), ecorePackage.getEString(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Mylyn.ecore");

    // Create annotations
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
    // http://www.eclipse.org/oomph/setup/Enablement
    createEnablementAnnotations();
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
    // http://www.eclipse.org/oomph/setup/ValidTriggers
    createValidTriggersAnnotations();
    // http://www.eclipse.org/oomph/setup/Redirect
    createRedirectAnnotations();
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
    addAnnotation(this, source, new String[] { "schemaLocation", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Mylyn.ecore" });
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
    addAnnotation(this, source, new String[] { "variableName", "setup.mylyn.p2", "repository", "${oomph.update.url}", "installableUnits",
        "org.eclipse.oomph.setup.mylyn.feature.group" });
    addAnnotation(this, source, new String[] { "variableName", "setup.mylyn.p2", "repository", "http://download.eclipse.org/mylyn/releases/latest" });
    addAnnotation(mylynQueriesTaskEClass, source, new String[] { "installableUnits", "org.eclipse.mylyn.tasks.core org.eclipse.mylyn.tasks.ui" });
    addAnnotation(mylynBuildsTaskEClass, source, new String[] { "installableUnits", "org.eclipse.mylyn.tasks.core org.eclipse.mylyn.tasks.ui" });
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
        "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.mylyn.edit/icons/full/obj16" });
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
    addAnnotation(mylynQueriesTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" });
    addAnnotation(mylynBuildsTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" });
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
    addAnnotation(getMylynQueriesTask_Queries(), source, new String[] { "name", "query" });
    addAnnotation(getMylynBuildsTask_BuildPlans(), source, new String[] { "name", "buildPlan" });
    addAnnotation(getQuery_URL(), source, new String[] { "kind", "attribute", "name", "url" });
    addAnnotation(getQuery_Attributes(), source, new String[] { "name", "attribute" });
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
    addAnnotation(getMylynQueriesTask_RepositoryURL(), source, new String[] {});
    addAnnotation(getMylynBuildsTask_ServerURL(), source, new String[] {});
    addAnnotation(getQuery_URL(), source, new String[] {});
  }

} // MylynPackageImpl
