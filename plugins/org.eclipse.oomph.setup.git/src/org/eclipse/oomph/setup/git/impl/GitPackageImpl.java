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
package org.eclipse.oomph.setup.git.impl;

import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.git.GitCloneTask;
import org.eclipse.oomph.setup.git.GitFactory;
import org.eclipse.oomph.setup.git.GitPackage;

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
public class GitPackageImpl extends EPackageImpl implements GitPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass gitCloneTaskEClass = null;

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
    GitPackageImpl theGitPackage = (GitPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof GitPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
        : new GitPackageImpl());

    isInited = true;

    // Initialize simple dependencies
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
  public EClass getGitCloneTask()
  {
    return gitCloneTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getGitCloneTask_Location()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getGitCloneTask_RemoteName()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getGitCloneTask_RemoteURI()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getGitCloneTask_PushURI()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getGitCloneTask_CheckoutBranch()
  {
    return (EAttribute)gitCloneTaskEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
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
    createEAttribute(gitCloneTaskEClass, GIT_CLONE_TASK__REMOTE_NAME);
    createEAttribute(gitCloneTaskEClass, GIT_CLONE_TASK__REMOTE_URI);
    createEAttribute(gitCloneTaskEClass, GIT_CLONE_TASK__PUSH_URI);
    createEAttribute(gitCloneTaskEClass, GIT_CLONE_TASK__CHECKOUT_BRANCH);
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
    gitCloneTaskEClass.getESuperTypes().add(theSetupPackage.getSetupTask());

    // Initialize classes and features; add operations and parameters
    initEClass(gitCloneTaskEClass, GitCloneTask.class, "GitCloneTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getGitCloneTask_Location(), ecorePackage.getEString(), "location", "", 1, 1, GitCloneTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGitCloneTask_RemoteName(), ecorePackage.getEString(), "remoteName", "origin", 1, 1, GitCloneTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGitCloneTask_RemoteURI(), ecorePackage.getEString(), "remoteURI", null, 1, 1, GitCloneTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGitCloneTask_PushURI(), ecorePackage.getEString(), "pushURI", null, 0, 1, GitCloneTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getGitCloneTask_CheckoutBranch(), ecorePackage.getEString(), "checkoutBranch", null, 1, 1, GitCloneTask.class, !IS_TRANSIENT, !IS_VOLATILE,
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Create resource
    createResource("http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Git.ecore");

    // Create annotations
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
    // http://www.eclipse.org/oomph/setup/Enablement
    createEnablementAnnotations();
    // http://www.eclipse.org/emf/2002/EcoreXXX
    createEcoreXXXAnnotations();
    // http://www.eclipse.org/oomph/setup/ValidTriggers
    createValidTriggersAnnotations();
    // http://www.eclipse.org/oomph/setup/Variable
    createVariableAnnotations();
    // http://www.eclipse.org/oomph/setup/RemoteResource
    createRemoteResourceAnnotations();
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
    addAnnotation(gitCloneTaskEClass, source, new String[] { "variableName", "setup.git.p2", "repository", "${oomph.update.url}", "installableUnits",
        "org.eclipse.oomph.setup.git.feature.group" });
    addAnnotation(gitCloneTaskEClass, source, new String[] { "variableName", "setup.egit.p2", "repository", "http://download.eclipse.org/egit/updates",
        "installableUnits", "org.eclipse.egit.feature.group" });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/EcoreXXX</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createEcoreXXXAnnotations()
  {
    String source = "http://www.eclipse.org/emf/2002/EcoreXXX";
    addAnnotation(gitCloneTaskEClass, source, new String[] { "constraints", "IDRequired LocationOptional" });
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
    addAnnotation(gitCloneTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" });
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
    addAnnotation(this, source, new String[] { "schemaLocation", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/setups/models/Git.ecore" });
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
    addAnnotation(getGitCloneTask_Location(), source, new String[] { "type", "STRING", "label", "Git clone location", "description", "Git clone location" });
    addAnnotation(getGitCloneTask_Location(), new boolean[] { true }, "Choice", new String[] { "value",
        "${user.home/git/}${@id.remoteURI|uriLastSegment}-${@id.checkoutBranch}", "label", "Located in the user home folder named git/<repo>-<branch>" });
    addAnnotation(getGitCloneTask_Location(), new boolean[] { true }, "Choice", new String[] { "value",
        "${user.home/git/}${@id.remoteURI|uriLastSegment/}${@id.checkoutBranch}", "label", "Located in the user home folder named git/<repo>/<branch>" });
    addAnnotation(getGitCloneTask_Location(), new boolean[] { true }, "Choice", new String[] { "value",
        "${installation.location/git/}${@id.remoteURI|uriLastSegment}", "label", "Located in installation location folder named git/<repo>" });
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
    addAnnotation(getGitCloneTask_RemoteURI(), source, new String[] {});
  }

} // GitPackageImpl
