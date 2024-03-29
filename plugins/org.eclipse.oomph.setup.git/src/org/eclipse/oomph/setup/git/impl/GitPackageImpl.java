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
package org.eclipse.oomph.setup.git.impl;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.git.ConfigProperty;
import org.eclipse.oomph.setup.git.ConfigSection;
import org.eclipse.oomph.setup.git.ConfigSubsection;
import org.eclipse.oomph.setup.git.GitCloneTask;
import org.eclipse.oomph.setup.git.GitConfigurationTask;
import org.eclipse.oomph.setup.git.GitFactory;
import org.eclipse.oomph.setup.git.GitPackage;

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
public class GitPackageImpl extends EPackageImpl implements GitPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass gitConfigurationTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass gitCloneTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass configSectionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass configSubsectionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass configPropertyEClass = null;

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
   * @see org.eclipse.oomph.setup.git.GitPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private GitPackageImpl()
  {
    super(eNS_URI, GitFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link GitPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static GitPackage init()
  {
    if (isInited)
    {
      return (GitPackage)EPackage.Registry.INSTANCE.getEPackage(GitPackage.eNS_URI);
    }

    // Obtain or create and register package
    Object registeredGitPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
    GitPackageImpl theGitPackage = registeredGitPackage instanceof GitPackageImpl ? (GitPackageImpl)registeredGitPackage : new GitPackageImpl();

    isInited = true;

    // Initialize simple dependencies
    BasePackage.eINSTANCE.eClass();
    SetupPackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theGitPackage.createPackageContents();

    // Initialize created meta-data
    theGitPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theGitPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(GitPackage.eNS_URI, theGitPackage);
    return theGitPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getGitConfigurationTask()
  {
    return gitConfigurationTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getGitConfigurationTask_RemoteURIPattern()
  {
    return (EAttribute)gitConfigurationTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getGitConfigurationTask_ConfigSections()
  {
    return (EReference)gitConfigurationTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getGitCloneTask()
  {
    return gitCloneTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getGitCloneTask_Location()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getGitCloneTask_LocationQualifier()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getGitCloneTask_RemoteName()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getGitCloneTask_RemoteURI()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getGitCloneTask_PushURI()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getGitCloneTask_CheckoutBranch()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getGitCloneTask_Recursive()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getGitCloneTask_ConfigSections()
  {
    return (EReference)gitCloneTaskEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getGitCloneTask_RestrictToCheckoutBranch()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getGitCloneTask_Configurations()
  {
    return (EReference)gitCloneTaskEClass.getEStructuralFeatures().get(9);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getConfigSection()
  {
    return configSectionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getConfigSection_Subsections()
  {
    return (EReference)configSectionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getConfigSubsection()
  {
    return configSubsectionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getConfigSubsection_Name()
  {
    return (EAttribute)configSubsectionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EReference getConfigSubsection_Properties()
  {
    return (EReference)configSubsectionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EClass getConfigProperty()
  {
    return configPropertyEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getConfigProperty_Key()
  {
    return (EAttribute)configPropertyEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getConfigProperty_Value()
  {
    return (EAttribute)configPropertyEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getConfigProperty_Force()
  {
    return (EAttribute)configPropertyEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EAttribute getConfigProperty_Recursive()
  {
    return (EAttribute)configPropertyEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public GitFactory getGitFactory()
  {
    return (GitFactory)getEFactoryInstance();
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
    gitCloneTaskEClass = createEClass(GIT_CLONE_TASK);
    createEAttribute(gitCloneTaskEClass, GIT_CLONE_TASK__LOCATION);
    createEAttribute(gitCloneTaskEClass, GIT_CLONE_TASK__LOCATION_QUALIFIER);
    createEAttribute(gitCloneTaskEClass, GIT_CLONE_TASK__REMOTE_NAME);
    createEAttribute(gitCloneTaskEClass, GIT_CLONE_TASK__REMOTE_URI);
    createEAttribute(gitCloneTaskEClass, GIT_CLONE_TASK__PUSH_URI);
    createEAttribute(gitCloneTaskEClass, GIT_CLONE_TASK__CHECKOUT_BRANCH);
    createEAttribute(gitCloneTaskEClass, GIT_CLONE_TASK__RECURSIVE);
    createEReference(gitCloneTaskEClass, GIT_CLONE_TASK__CONFIG_SECTIONS);
    createEAttribute(gitCloneTaskEClass, GIT_CLONE_TASK__RESTRICT_TO_CHECKOUT_BRANCH);
    createEReference(gitCloneTaskEClass, GIT_CLONE_TASK__CONFIGURATIONS);

    gitConfigurationTaskEClass = createEClass(GIT_CONFIGURATION_TASK);
    createEAttribute(gitConfigurationTaskEClass, GIT_CONFIGURATION_TASK__REMOTE_URI_PATTERN);
    createEReference(gitConfigurationTaskEClass, GIT_CONFIGURATION_TASK__CONFIG_SECTIONS);

    configSectionEClass = createEClass(CONFIG_SECTION);
    createEReference(configSectionEClass, CONFIG_SECTION__SUBSECTIONS);

    configSubsectionEClass = createEClass(CONFIG_SUBSECTION);
    createEAttribute(configSubsectionEClass, CONFIG_SUBSECTION__NAME);
    createEReference(configSubsectionEClass, CONFIG_SUBSECTION__PROPERTIES);

    configPropertyEClass = createEClass(CONFIG_PROPERTY);
    createEAttribute(configPropertyEClass, CONFIG_PROPERTY__KEY);
    createEAttribute(configPropertyEClass, CONFIG_PROPERTY__VALUE);
    createEAttribute(configPropertyEClass, CONFIG_PROPERTY__FORCE);
    createEAttribute(configPropertyEClass, CONFIG_PROPERTY__RECURSIVE);
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

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    gitCloneTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());
    gitConfigurationTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());
    configSectionEClass.getESuperTypes().add(getConfigSubsection());

    // Initialize classes and features; add operations and parameters
    initEClass(gitCloneTaskEClass, GitCloneTask.class, "GitCloneTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getGitCloneTask_Location(), ecorePackage.getEString(), "location", "", 1, 1, GitCloneTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$ //$NON-NLS-2$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGitCloneTask_LocationQualifier(), ecorePackage.getEString(), "locationQualifier", " ", 0, 1, GitCloneTask.class, !IS_TRANSIENT, //$NON-NLS-1$ //$NON-NLS-2$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGitCloneTask_RemoteName(), ecorePackage.getEString(), "remoteName", "origin", 1, 1, GitCloneTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$ //$NON-NLS-2$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGitCloneTask_RemoteURI(), ecorePackage.getEString(), "remoteURI", null, 1, 1, GitCloneTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGitCloneTask_PushURI(), ecorePackage.getEString(), "pushURI", null, 0, 1, GitCloneTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGitCloneTask_CheckoutBranch(), ecorePackage.getEString(), "checkoutBranch", "${scope.project.stream.name}", 1, 1, GitCloneTask.class, //$NON-NLS-1$ //$NON-NLS-2$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGitCloneTask_Recursive(), ecorePackage.getEBoolean(), "recursive", "false", 0, 1, GitCloneTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$ //$NON-NLS-2$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGitCloneTask_ConfigSections(), getConfigSection(), null, "configSections", null, 0, -1, GitCloneTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGitCloneTask_RestrictToCheckoutBranch(), ecorePackage.getEBoolean(), "restrictToCheckoutBranch", "false", 0, 1, GitCloneTask.class, //$NON-NLS-1$ //$NON-NLS-2$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGitCloneTask_Configurations(), getGitConfigurationTask(), null, "configurations", null, 0, -1, GitCloneTask.class, IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, !IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(gitConfigurationTaskEClass, GitConfigurationTask.class, "GitConfigurationTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getGitConfigurationTask_RemoteURIPattern(), ecorePackage.getEString(), "remoteURIPattern", "", 1, 1, GitConfigurationTask.class, //$NON-NLS-1$ //$NON-NLS-2$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getGitConfigurationTask_ConfigSections(), getConfigSection(), null, "configSections", null, 0, -1, GitConfigurationTask.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(configSectionEClass, ConfigSection.class, "ConfigSection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getConfigSection_Subsections(), getConfigSubsection(), null, "subsections", null, 0, -1, ConfigSection.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(configSubsectionEClass, ConfigSubsection.class, "ConfigSubsection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getConfigSubsection_Name(), ecorePackage.getEString(), "name", null, 1, 1, ConfigSubsection.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getConfigSubsection_Properties(), getConfigProperty(), null, "properties", null, 0, -1, ConfigSubsection.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(configPropertyEClass, ConfigProperty.class, "ConfigProperty", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getConfigProperty_Key(), ecorePackage.getEString(), "key", null, 1, 1, ConfigProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getConfigProperty_Value(), ecorePackage.getEString(), "value", null, 0, 1, ConfigProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getConfigProperty_Force(), ecorePackage.getEBoolean(), "force", null, 0, 1, ConfigProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getConfigProperty_Recursive(), ecorePackage.getEBoolean(), "recursive", "true", 0, 1, ConfigProperty.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$ //$NON-NLS-2$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource("https://raw.githubusercontent.com/eclipse-oomph/oomph/master/setups/models/Git.ecore");

    // Create annotations
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
    // http://www.eclipse.org/oomph/setup/Enablement
    createEnablementAnnotations();
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
    // http://www.eclipse.org/emf/2002/EcoreXXX
    createEcoreXXXAnnotations();
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
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/Enablement</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createEnablementAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/Enablement"; //$NON-NLS-1$
    addAnnotation(this, source, new String[] { "variableName", "setup.git.p2", //$NON-NLS-1$ //$NON-NLS-2$
        "repository", "${oomph.update.url}", //$NON-NLS-1$ //$NON-NLS-2$
        "installableUnits", "org.eclipse.oomph.setup.git.feature.group" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(this, source, new String[] { "variableName", "setup.egit.p2", //$NON-NLS-1$ //$NON-NLS-2$
        "repository", "https://download.eclipse.org/egit/updates", //$NON-NLS-1$ //$NON-NLS-2$
        "installableUnits", "org.eclipse.egit.feature.group", //$NON-NLS-1$ //$NON-NLS-2$
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
        new String[] { "imageBaseURI", "https://raw.githubusercontent.com/eclipse-oomph/oomph/master/plugins/org.eclipse.oomph.setup.git.edit/icons/full/obj16" //$NON-NLS-1$ //$NON-NLS-2$
        });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/EcoreXXX</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createEcoreXXXAnnotations()
  {
    String source = "http://www.eclipse.org/emf/2002/EcoreXXX"; //$NON-NLS-1$
    addAnnotation(gitCloneTaskEClass, source, new String[] { "constraints", "IDRequired LocationOptional" //$NON-NLS-1$ //$NON-NLS-2$
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
    addAnnotation(gitCloneTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(gitConfigurationTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" //$NON-NLS-1$ //$NON-NLS-2$
    });
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
    addAnnotation(this, source, new String[] { "schemaLocation", "https://raw.githubusercontent.com/eclipse-oomph/oomph/master/setups/models/Git.ecore" //$NON-NLS-1$ //$NON-NLS-2$
    });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/Variable</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createVariableAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/Variable"; //$NON-NLS-1$
    addAnnotation(getGitCloneTask_Location(), source, new String[] { "filter", "canonical", //$NON-NLS-1$ //$NON-NLS-2$
        "type", "STRING", //$NON-NLS-1$ //$NON-NLS-2$
        "label", "Git clone location rule", //$NON-NLS-1$ //$NON-NLS-2$
        "description", "The rule for the absolute folder location where the Git clone is located", //$NON-NLS-1$ //$NON-NLS-2$
        "explicitType", "FOLDER", //$NON-NLS-1$ //$NON-NLS-2$
        "explicitLabel", "${@id.description} Git clone location", //$NON-NLS-1$ //$NON-NLS-2$
        "explicitDescription", "The absolute folder location where the ${@id.description} Git clone is located" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getGitCloneTask_Location(), new boolean[] { true }, "Choice", //$NON-NLS-1$
        new String[] { "value", "${installation.location/git/}${@id.locationQualifier|trim}${@id.remoteURI|gitRepository}", //$NON-NLS-1$ //$NON-NLS-2$
            "label", "Located in a folder named \'git/<repo>\' within the installation folder" //$NON-NLS-1$ //$NON-NLS-2$
        });
    addAnnotation(getGitCloneTask_Location(), new boolean[] { true }, "Choice", //$NON-NLS-1$
        new String[] { "value", "${workspace.location/.git/}${@id.locationQualifier|trim}${@id.remoteURI|gitRepository}", //$NON-NLS-1$ //$NON-NLS-2$
            "label", "Located in a folder named \'.git/<repo>\' within the workspace folder" //$NON-NLS-1$ //$NON-NLS-2$
        });
    addAnnotation(getGitCloneTask_Location(), new boolean[] { true }, "Choice", //$NON-NLS-1$
        new String[] { "value", "${git.container.root/}${@id.locationQualifier|trim}${@id.remoteURI|gitRepository}", //$NON-NLS-1$ //$NON-NLS-2$
            "label", "Located in a folder named \'<repo>\' within the root Git-container folder " //$NON-NLS-1$ //$NON-NLS-2$
        });
    addAnnotation(getGitCloneTask_Location(), new boolean[] { true }, "Choice", //$NON-NLS-1$
        new String[] { "value", "${git.container.root/}${@id.locationQualifier|trim}${@id.remoteURI|gitRepository}-${@id.checkoutBranch}", //$NON-NLS-1$ //$NON-NLS-2$
            "label", "Located in a folder named \'<repo>-<branch>\' within the root Git-container folder " //$NON-NLS-1$ //$NON-NLS-2$
        });
    addAnnotation(getGitCloneTask_Location(), new boolean[] { true }, "Choice", //$NON-NLS-1$
        new String[] { "value", "${git.container.root/}${@id.locationQualifier|trim}${@id.remoteURI|gitRepository/}${@id.checkoutBranch}", //$NON-NLS-1$ //$NON-NLS-2$
            "label", "Located in a folder named \'<repo>/<branch>\' within the root Git-container folder " //$NON-NLS-1$ //$NON-NLS-2$
        });
    addAnnotation(getGitCloneTask_Location(), new boolean[] { true }, "Choice", //$NON-NLS-1$
        new String[] { "value", "${@id.location}", //$NON-NLS-1$ //$NON-NLS-2$
            "label", "Located in the specified absolute folder location" //$NON-NLS-1$ //$NON-NLS-2$
        });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/RuleVariable</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createRuleVariableAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/RuleVariable"; //$NON-NLS-1$
    addAnnotation(getGitCloneTask_Location(), source, new String[] { "name", "git.container.root", //$NON-NLS-1$ //$NON-NLS-2$
        "type", "FOLDER", //$NON-NLS-1$ //$NON-NLS-2$
        "label", "Root Git-container folder", //$NON-NLS-1$ //$NON-NLS-2$
        "defaultValue", "${user.home/git}", //$NON-NLS-1$ //$NON-NLS-2$
        "description", "The root Git-container folder where all the Git clones are located", //$NON-NLS-1$ //$NON-NLS-2$
        "storageURI", "scope://" //$NON-NLS-1$ //$NON-NLS-2$
    });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/RemoteResource</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createRemoteResourceAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/RemoteResource"; //$NON-NLS-1$
    addAnnotation(getGitCloneTask_RemoteURI(), source, new String[] {});
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/Redirect</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createRedirectAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/Redirect"; //$NON-NLS-1$
    addAnnotation(getGitCloneTask_RemoteURI(), source, new String[] {});
    addAnnotation(getGitCloneTask_PushURI(), source, new String[] {});
  }

} // GitPackageImpl
