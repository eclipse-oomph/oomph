/*
 * Copyright (c) 2014-2016, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.impl;

import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.setup.Argument;
import org.eclipse.oomph.setup.AttributeRule;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.CertificateInfo;
import org.eclipse.oomph.setup.CertificatePolicy;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Configuration;
import org.eclipse.oomph.setup.EclipseIniTask;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.LicenseInfo;
import org.eclipse.oomph.setup.LinkLocationTask;
import org.eclipse.oomph.setup.LocationCatalog;
import org.eclipse.oomph.setup.Macro;
import org.eclipse.oomph.setup.MacroTask;
import org.eclipse.oomph.setup.Parameter;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.ProjectContainer;
import org.eclipse.oomph.setup.RedirectionTask;
import org.eclipse.oomph.setup.ResourceCopyTask;
import org.eclipse.oomph.setup.ResourceCreationTask;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.ScopeType;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.StringSubstitutionTask;
import org.eclipse.oomph.setup.TextModification;
import org.eclipse.oomph.setup.TextModifyTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.UnsignedPolicy;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableChoice;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.VariableType;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.WorkspaceTask;
import org.eclipse.oomph.setup.util.SetupValidator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import java.util.Map;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SetupPackageImpl extends EPackageImpl implements SetupPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass indexEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass catalogSelectionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass projectEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass streamEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass installationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass productCatalogEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass productEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass productVersionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass projectCatalogEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass installationTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass workspaceTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass configurationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass compoundTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass variableTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass setupTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass resourceCopyTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass textModifyTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass textModificationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass productToProductVersionMapEntryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass projectToStreamMapEntryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass installationToWorkspacesMapEntryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass workspaceToInstallationsMapEntryEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass macroEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass parameterEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass macroTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass argumentEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass stringSubstitutionTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass projectContainerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum scopeTypeEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass attributeRuleEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass locationCatalogEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass redirectionTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass variableChoiceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass resourceCreationTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass eclipseIniTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum triggerEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum variableTypeEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum unsignedPolicyEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum certificatePolicyEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass userEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass workspaceEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass linkLocationTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass preferenceTaskEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass setupTaskContainerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass scopeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType licenseInfoEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType filterEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType certificateInfoEDataType = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EDataType triggerSetEDataType = null;

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
   * @see org.eclipse.oomph.setup.SetupPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private SetupPackageImpl()
  {
    super(eNS_URI, SetupFactory.eINSTANCE);
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
   * <p>This method is used to initialize {@link SetupPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static SetupPackage init()
  {
    if (isInited)
    {
      return (SetupPackage)EPackage.Registry.INSTANCE.getEPackage(SetupPackage.eNS_URI);
    }

    // Obtain or create and register package
    Object registeredSetupPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
    SetupPackageImpl theSetupPackage = registeredSetupPackage instanceof SetupPackageImpl ? (SetupPackageImpl)registeredSetupPackage : new SetupPackageImpl();

    isInited = true;

    // Initialize simple dependencies
    BasePackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theSetupPackage.createPackageContents();

    // Initialize created meta-data
    theSetupPackage.initializePackageContents();

    // Register package validator
    EValidator.Registry.INSTANCE.put(theSetupPackage, new EValidator.Descriptor()
    {
      public EValidator getEValidator()
      {
        return SetupValidator.INSTANCE;
      }
    });

    // Mark meta-data to indicate it can't be changed
    theSetupPackage.freeze();

    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(SetupPackage.eNS_URI, theSetupPackage);
    return theSetupPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getIndex()
  {
    return indexEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getIndex_Name()
  {
    return (EAttribute)indexEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIndex_DiscoverablePackages()
  {
    return (EReference)indexEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIndex_ProjectCatalogs()
  {
    return (EReference)indexEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCatalogSelection()
  {
    return catalogSelectionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCatalogSelection_ProductCatalogs()
  {
    return (EReference)catalogSelectionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCatalogSelection_ProjectCatalogs()
  {
    return (EReference)catalogSelectionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCatalogSelection_DefaultProductVersions()
  {
    return (EReference)catalogSelectionEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCatalogSelection_DefaultStreams()
  {
    return (EReference)catalogSelectionEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getCatalogSelection_SelectedStreams()
  {
    return (EReference)catalogSelectionEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getIndex_ProductCatalogs()
  {
    return (EReference)indexEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProject()
  {
    return projectEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProject_Streams()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProject_ProjectContainer()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProject_LogicalProjectContainer()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProject_ParentProject()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProject_ProjectCatalog()
  {
    return (EReference)projectEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getStream()
  {
    return streamEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getStream_Project()
  {
    return (EReference)streamEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getInstallation()
  {
    return installationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getInstallation_ProductVersion()
  {
    return (EReference)installationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProductCatalog()
  {
    return productCatalogEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProductCatalog_Index()
  {
    return (EReference)productCatalogEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProductCatalog_Products()
  {
    return (EReference)productCatalogEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProduct()
  {
    return productEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProduct_ProductCatalog()
  {
    return (EReference)productEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProduct_Versions()
  {
    return (EReference)productEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProductVersion()
  {
    return productVersionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProductVersion_Product()
  {
    return (EReference)productVersionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getProductVersion_RequiredJavaVersion()
  {
    return (EAttribute)productVersionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProjectCatalog()
  {
    return projectCatalogEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProjectCatalog_Index()
  {
    return (EReference)projectCatalogEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getInstallationTask()
  {
    return installationTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getInstallationTask_Location()
  {
    return (EAttribute)installationTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getInstallationTask_RelativeProductFolder()
  {
    return (EAttribute)installationTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getWorkspaceTask()
  {
    return workspaceTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getWorkspaceTask_Location()
  {
    return (EAttribute)workspaceTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getConfiguration()
  {
    return configurationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConfiguration_Label()
  {
    return (EAttribute)configurationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getConfiguration_Description()
  {
    return (EAttribute)configurationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getConfiguration_Installation()
  {
    return (EReference)configurationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getConfiguration_Workspace()
  {
    return (EReference)configurationEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getCompoundTask()
  {
    return compoundTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getCompoundTask_Name()
  {
    return (EAttribute)compoundTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getVariableTask()
  {
    return variableTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariableTask_Type()
  {
    return (EAttribute)variableTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariableTask_Name()
  {
    return (EAttribute)variableTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariableTask_Value()
  {
    return (EAttribute)variableTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariableTask_DefaultValue()
  {
    return (EAttribute)variableTaskEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariableTask_StorePromptedValue()
  {
    return (EAttribute)variableTaskEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariableTask_StorageURI()
  {
    return (EAttribute)variableTaskEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariableTask_Label()
  {
    return (EAttribute)variableTaskEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getVariableTask_Choices()
  {
    return (EReference)variableTaskEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSetupTask()
  {
    return setupTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSetupTask_Predecessors()
  {
    return (EReference)setupTaskEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSetupTask_Successors()
  {
    return (EReference)setupTaskEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSetupTask_ID()
  {
    return (EAttribute)setupTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSetupTask_ScopeType()
  {
    return (EAttribute)setupTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSetupTask_Restrictions()
  {
    return (EReference)setupTaskEClass.getEStructuralFeatures().get(7);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSetupTask_Filter()
  {
    return (EAttribute)setupTaskEClass.getEStructuralFeatures().get(8);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSetupTask_ExcludedTriggers()
  {
    return (EAttribute)setupTaskEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSetupTask_Description()
  {
    return (EAttribute)setupTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getSetupTask_Disabled()
  {
    return (EAttribute)setupTaskEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getResourceCopyTask()
  {
    return resourceCopyTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getResourceCopyTask_Force()
  {
    return (EAttribute)resourceCopyTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getResourceCopyTask_SourceURL()
  {
    return (EAttribute)resourceCopyTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getResourceCopyTask_TargetURL()
  {
    return (EAttribute)resourceCopyTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTextModifyTask()
  {
    return textModifyTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTextModifyTask_URL()
  {
    return (EAttribute)textModifyTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTextModifyTask_Modifications()
  {
    return (EReference)textModifyTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTextModifyTask_Encoding()
  {
    return (EAttribute)textModifyTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTextModification()
  {
    return textModificationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTextModification_Pattern()
  {
    return (EAttribute)textModificationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTextModification_Substitutions()
  {
    return (EAttribute)textModificationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProductToProductVersionMapEntry()
  {
    return productToProductVersionMapEntryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProductToProductVersionMapEntry_Key()
  {
    return (EReference)productToProductVersionMapEntryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProductToProductVersionMapEntry_Value()
  {
    return (EReference)productToProductVersionMapEntryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProjectToStreamMapEntry()
  {
    return projectToStreamMapEntryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProjectToStreamMapEntry_Key()
  {
    return (EReference)projectToStreamMapEntryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProjectToStreamMapEntry_Value()
  {
    return (EReference)projectToStreamMapEntryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getProjectToStreamMapEntry_Selection()
  {
    return (EAttribute)projectToStreamMapEntryEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getInstallationToWorkspacesMapEntry()
  {
    return installationToWorkspacesMapEntryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getInstallationToWorkspacesMapEntry_Key()
  {
    return (EReference)installationToWorkspacesMapEntryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getInstallationToWorkspacesMapEntry_Value()
  {
    return (EReference)installationToWorkspacesMapEntryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getWorkspaceToInstallationsMapEntry()
  {
    return workspaceToInstallationsMapEntryEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getWorkspaceToInstallationsMapEntry_Value()
  {
    return (EReference)workspaceToInstallationsMapEntryEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMacro()
  {
    return macroEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMacro_LogicalContainer()
  {
    return (EReference)macroEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMacro_Parameters()
  {
    return (EReference)macroEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getParameter()
  {
    return parameterEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getParameter_Name()
  {
    return (EAttribute)parameterEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getParameter_Description()
  {
    return (EAttribute)parameterEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getParameter_DefaultValue()
  {
    return (EAttribute)parameterEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getMacroTask()
  {
    return macroTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMacroTask_Arguments()
  {
    return (EReference)macroTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getArgument()
  {
    return argumentEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getArgument_MacroTask()
  {
    return (EReference)argumentEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getArgument_Name()
  {
    return (EAttribute)argumentEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getArgument_Parameter()
  {
    return (EReference)argumentEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getArgument_Value()
  {
    return (EAttribute)argumentEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getMacroTask_Macro()
  {
    return (EReference)macroTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getWorkspaceToInstallationsMapEntry_Key()
  {
    return (EReference)workspaceToInstallationsMapEntryEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getStringSubstitutionTask()
  {
    return stringSubstitutionTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStringSubstitutionTask_Name()
  {
    return (EAttribute)stringSubstitutionTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getStringSubstitutionTask_Value()
  {
    return (EAttribute)stringSubstitutionTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getProjectContainer()
  {
    return projectContainerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getProjectContainer_Projects()
  {
    return (EReference)projectContainerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getScopeType()
  {
    return scopeTypeEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getAttributeRule()
  {
    return attributeRuleEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAttributeRule_AttributeURI()
  {
    return (EAttribute)attributeRuleEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getAttributeRule_Value()
  {
    return (EAttribute)attributeRuleEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getLocationCatalog()
  {
    return locationCatalogEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getLocationCatalog_Installations()
  {
    return (EReference)locationCatalogEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getLocationCatalog_Workspaces()
  {
    return (EReference)locationCatalogEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRedirectionTask()
  {
    return redirectionTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRedirectionTask_SourceURL()
  {
    return (EAttribute)redirectionTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getRedirectionTask_TargetURL()
  {
    return (EAttribute)redirectionTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getVariableChoice()
  {
    return variableChoiceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariableChoice_Value()
  {
    return (EAttribute)variableChoiceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getVariableChoice_Label()
  {
    return (EAttribute)variableChoiceEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getResourceCreationTask()
  {
    return resourceCreationTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getResourceCreationTask_Force()
  {
    return (EAttribute)resourceCreationTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getResourceCreationTask_Content()
  {
    return (EAttribute)resourceCreationTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getResourceCreationTask_TargetURL()
  {
    return (EAttribute)resourceCreationTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getResourceCreationTask_Encoding()
  {
    return (EAttribute)resourceCreationTaskEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getEclipseIniTask()
  {
    return eclipseIniTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getEclipseIniTask_Option()
  {
    return (EAttribute)eclipseIniTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getEclipseIniTask_Value()
  {
    return (EAttribute)eclipseIniTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getEclipseIniTask_Vm()
  {
    return (EAttribute)eclipseIniTaskEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getTrigger()
  {
    return triggerEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getVariableType()
  {
    return variableTypeEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getUnsignedPolicy()
  {
    return unsignedPolicyEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getCertificatePolicy()
  {
    return certificatePolicyEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getUser()
  {
    return userEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getUser_AttributeRules()
  {
    return (EReference)userEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getUser_AcceptedLicenses()
  {
    return (EAttribute)userEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getUser_AcceptedCertificates()
  {
    return (EAttribute)userEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getUser_UnsignedPolicy()
  {
    return (EAttribute)userEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getUser_CertificatePolicy()
  {
    return (EAttribute)userEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getUser_QuestionnaireDate()
  {
    return (EAttribute)userEClass.getEStructuralFeatures().get(5);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getUser_PreferenceRecorderDefault()
  {
    return (EAttribute)userEClass.getEStructuralFeatures().get(6);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getWorkspace()
  {
    return workspaceEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getWorkspace_Streams()
  {
    return (EReference)workspaceEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getLinkLocationTask()
  {
    return linkLocationTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getLinkLocationTask_Path()
  {
    return (EAttribute)linkLocationTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getLinkLocationTask_Name()
  {
    return (EAttribute)linkLocationTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getPreferenceTask()
  {
    return preferenceTaskEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPreferenceTask_Key()
  {
    return (EAttribute)preferenceTaskEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getPreferenceTask_Value()
  {
    return (EAttribute)preferenceTaskEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getSetupTaskContainer()
  {
    return setupTaskContainerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getSetupTaskContainer_SetupTasks()
  {
    return (EReference)setupTaskContainerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getScope()
  {
    return scopeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getScope_Name()
  {
    return (EAttribute)scopeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getScope_Label()
  {
    return (EAttribute)scopeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getScope_Description()
  {
    return (EAttribute)scopeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getScope_QualifiedName()
  {
    return (EAttribute)scopeEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getLicenseInfo()
  {
    return licenseInfoEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getFilter()
  {
    return filterEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getCertificateInfo()
  {
    return certificateInfoEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EDataType getTriggerSet()
  {
    return triggerSetEDataType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupFactory getSetupFactory()
  {
    return (SetupFactory)getEFactoryInstance();
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
    setupTaskEClass = createEClass(SETUP_TASK);
    createEAttribute(setupTaskEClass, SETUP_TASK__ID);
    createEAttribute(setupTaskEClass, SETUP_TASK__DESCRIPTION);
    createEAttribute(setupTaskEClass, SETUP_TASK__SCOPE_TYPE);
    createEAttribute(setupTaskEClass, SETUP_TASK__EXCLUDED_TRIGGERS);
    createEAttribute(setupTaskEClass, SETUP_TASK__DISABLED);
    createEReference(setupTaskEClass, SETUP_TASK__PREDECESSORS);
    createEReference(setupTaskEClass, SETUP_TASK__SUCCESSORS);
    createEReference(setupTaskEClass, SETUP_TASK__RESTRICTIONS);
    createEAttribute(setupTaskEClass, SETUP_TASK__FILTER);

    setupTaskContainerEClass = createEClass(SETUP_TASK_CONTAINER);
    createEReference(setupTaskContainerEClass, SETUP_TASK_CONTAINER__SETUP_TASKS);

    scopeEClass = createEClass(SCOPE);
    createEAttribute(scopeEClass, SCOPE__NAME);
    createEAttribute(scopeEClass, SCOPE__LABEL);
    createEAttribute(scopeEClass, SCOPE__DESCRIPTION);
    createEAttribute(scopeEClass, SCOPE__QUALIFIED_NAME);

    indexEClass = createEClass(INDEX);
    createEAttribute(indexEClass, INDEX__NAME);
    createEReference(indexEClass, INDEX__DISCOVERABLE_PACKAGES);
    createEReference(indexEClass, INDEX__PRODUCT_CATALOGS);
    createEReference(indexEClass, INDEX__PROJECT_CATALOGS);

    catalogSelectionEClass = createEClass(CATALOG_SELECTION);
    createEReference(catalogSelectionEClass, CATALOG_SELECTION__PRODUCT_CATALOGS);
    createEReference(catalogSelectionEClass, CATALOG_SELECTION__PROJECT_CATALOGS);
    createEReference(catalogSelectionEClass, CATALOG_SELECTION__DEFAULT_PRODUCT_VERSIONS);
    createEReference(catalogSelectionEClass, CATALOG_SELECTION__DEFAULT_STREAMS);
    createEReference(catalogSelectionEClass, CATALOG_SELECTION__SELECTED_STREAMS);

    productCatalogEClass = createEClass(PRODUCT_CATALOG);
    createEReference(productCatalogEClass, PRODUCT_CATALOG__INDEX);
    createEReference(productCatalogEClass, PRODUCT_CATALOG__PRODUCTS);

    productEClass = createEClass(PRODUCT);
    createEReference(productEClass, PRODUCT__PRODUCT_CATALOG);
    createEReference(productEClass, PRODUCT__VERSIONS);

    productVersionEClass = createEClass(PRODUCT_VERSION);
    createEReference(productVersionEClass, PRODUCT_VERSION__PRODUCT);
    createEAttribute(productVersionEClass, PRODUCT_VERSION__REQUIRED_JAVA_VERSION);

    projectContainerEClass = createEClass(PROJECT_CONTAINER);
    createEReference(projectContainerEClass, PROJECT_CONTAINER__PROJECTS);

    projectCatalogEClass = createEClass(PROJECT_CATALOG);
    createEReference(projectCatalogEClass, PROJECT_CATALOG__INDEX);

    projectEClass = createEClass(PROJECT);
    createEReference(projectEClass, PROJECT__STREAMS);
    createEReference(projectEClass, PROJECT__PROJECT_CONTAINER);
    createEReference(projectEClass, PROJECT__LOGICAL_PROJECT_CONTAINER);
    createEReference(projectEClass, PROJECT__PARENT_PROJECT);
    createEReference(projectEClass, PROJECT__PROJECT_CATALOG);

    streamEClass = createEClass(STREAM);
    createEReference(streamEClass, STREAM__PROJECT);

    userEClass = createEClass(USER);
    createEReference(userEClass, USER__ATTRIBUTE_RULES);
    createEAttribute(userEClass, USER__ACCEPTED_LICENSES);
    createEAttribute(userEClass, USER__ACCEPTED_CERTIFICATES);
    createEAttribute(userEClass, USER__UNSIGNED_POLICY);
    createEAttribute(userEClass, USER__CERTIFICATE_POLICY);
    createEAttribute(userEClass, USER__QUESTIONNAIRE_DATE);
    createEAttribute(userEClass, USER__PREFERENCE_RECORDER_DEFAULT);

    attributeRuleEClass = createEClass(ATTRIBUTE_RULE);
    createEAttribute(attributeRuleEClass, ATTRIBUTE_RULE__ATTRIBUTE_URI);
    createEAttribute(attributeRuleEClass, ATTRIBUTE_RULE__VALUE);

    locationCatalogEClass = createEClass(LOCATION_CATALOG);
    createEReference(locationCatalogEClass, LOCATION_CATALOG__INSTALLATIONS);
    createEReference(locationCatalogEClass, LOCATION_CATALOG__WORKSPACES);

    installationEClass = createEClass(INSTALLATION);
    createEReference(installationEClass, INSTALLATION__PRODUCT_VERSION);

    installationTaskEClass = createEClass(INSTALLATION_TASK);
    createEAttribute(installationTaskEClass, INSTALLATION_TASK__LOCATION);
    createEAttribute(installationTaskEClass, INSTALLATION_TASK__RELATIVE_PRODUCT_FOLDER);

    workspaceEClass = createEClass(WORKSPACE);
    createEReference(workspaceEClass, WORKSPACE__STREAMS);

    workspaceTaskEClass = createEClass(WORKSPACE_TASK);
    createEAttribute(workspaceTaskEClass, WORKSPACE_TASK__LOCATION);

    configurationEClass = createEClass(CONFIGURATION);
    createEAttribute(configurationEClass, CONFIGURATION__LABEL);
    createEAttribute(configurationEClass, CONFIGURATION__DESCRIPTION);
    createEReference(configurationEClass, CONFIGURATION__INSTALLATION);
    createEReference(configurationEClass, CONFIGURATION__WORKSPACE);

    compoundTaskEClass = createEClass(COMPOUND_TASK);
    createEAttribute(compoundTaskEClass, COMPOUND_TASK__NAME);

    variableTaskEClass = createEClass(VARIABLE_TASK);
    createEAttribute(variableTaskEClass, VARIABLE_TASK__TYPE);
    createEAttribute(variableTaskEClass, VARIABLE_TASK__NAME);
    createEAttribute(variableTaskEClass, VARIABLE_TASK__VALUE);
    createEAttribute(variableTaskEClass, VARIABLE_TASK__DEFAULT_VALUE);
    createEAttribute(variableTaskEClass, VARIABLE_TASK__STORE_PROMPTED_VALUE);
    createEAttribute(variableTaskEClass, VARIABLE_TASK__STORAGE_URI);
    createEAttribute(variableTaskEClass, VARIABLE_TASK__LABEL);
    createEReference(variableTaskEClass, VARIABLE_TASK__CHOICES);

    variableChoiceEClass = createEClass(VARIABLE_CHOICE);
    createEAttribute(variableChoiceEClass, VARIABLE_CHOICE__VALUE);
    createEAttribute(variableChoiceEClass, VARIABLE_CHOICE__LABEL);

    stringSubstitutionTaskEClass = createEClass(STRING_SUBSTITUTION_TASK);
    createEAttribute(stringSubstitutionTaskEClass, STRING_SUBSTITUTION_TASK__NAME);
    createEAttribute(stringSubstitutionTaskEClass, STRING_SUBSTITUTION_TASK__VALUE);

    redirectionTaskEClass = createEClass(REDIRECTION_TASK);
    createEAttribute(redirectionTaskEClass, REDIRECTION_TASK__SOURCE_URL);
    createEAttribute(redirectionTaskEClass, REDIRECTION_TASK__TARGET_URL);

    eclipseIniTaskEClass = createEClass(ECLIPSE_INI_TASK);
    createEAttribute(eclipseIniTaskEClass, ECLIPSE_INI_TASK__OPTION);
    createEAttribute(eclipseIniTaskEClass, ECLIPSE_INI_TASK__VALUE);
    createEAttribute(eclipseIniTaskEClass, ECLIPSE_INI_TASK__VM);

    linkLocationTaskEClass = createEClass(LINK_LOCATION_TASK);
    createEAttribute(linkLocationTaskEClass, LINK_LOCATION_TASK__PATH);
    createEAttribute(linkLocationTaskEClass, LINK_LOCATION_TASK__NAME);

    preferenceTaskEClass = createEClass(PREFERENCE_TASK);
    createEAttribute(preferenceTaskEClass, PREFERENCE_TASK__KEY);
    createEAttribute(preferenceTaskEClass, PREFERENCE_TASK__VALUE);

    resourceCopyTaskEClass = createEClass(RESOURCE_COPY_TASK);
    createEAttribute(resourceCopyTaskEClass, RESOURCE_COPY_TASK__FORCE);
    createEAttribute(resourceCopyTaskEClass, RESOURCE_COPY_TASK__SOURCE_URL);
    createEAttribute(resourceCopyTaskEClass, RESOURCE_COPY_TASK__TARGET_URL);

    resourceCreationTaskEClass = createEClass(RESOURCE_CREATION_TASK);
    createEAttribute(resourceCreationTaskEClass, RESOURCE_CREATION_TASK__FORCE);
    createEAttribute(resourceCreationTaskEClass, RESOURCE_CREATION_TASK__CONTENT);
    createEAttribute(resourceCreationTaskEClass, RESOURCE_CREATION_TASK__TARGET_URL);
    createEAttribute(resourceCreationTaskEClass, RESOURCE_CREATION_TASK__ENCODING);

    textModifyTaskEClass = createEClass(TEXT_MODIFY_TASK);
    createEAttribute(textModifyTaskEClass, TEXT_MODIFY_TASK__URL);
    createEReference(textModifyTaskEClass, TEXT_MODIFY_TASK__MODIFICATIONS);
    createEAttribute(textModifyTaskEClass, TEXT_MODIFY_TASK__ENCODING);

    textModificationEClass = createEClass(TEXT_MODIFICATION);
    createEAttribute(textModificationEClass, TEXT_MODIFICATION__PATTERN);
    createEAttribute(textModificationEClass, TEXT_MODIFICATION__SUBSTITUTIONS);

    productToProductVersionMapEntryEClass = createEClass(PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY);
    createEReference(productToProductVersionMapEntryEClass, PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY__KEY);
    createEReference(productToProductVersionMapEntryEClass, PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY__VALUE);

    projectToStreamMapEntryEClass = createEClass(PROJECT_TO_STREAM_MAP_ENTRY);
    createEReference(projectToStreamMapEntryEClass, PROJECT_TO_STREAM_MAP_ENTRY__KEY);
    createEReference(projectToStreamMapEntryEClass, PROJECT_TO_STREAM_MAP_ENTRY__VALUE);
    createEAttribute(projectToStreamMapEntryEClass, PROJECT_TO_STREAM_MAP_ENTRY__SELECTION);

    installationToWorkspacesMapEntryEClass = createEClass(INSTALLATION_TO_WORKSPACES_MAP_ENTRY);
    createEReference(installationToWorkspacesMapEntryEClass, INSTALLATION_TO_WORKSPACES_MAP_ENTRY__KEY);
    createEReference(installationToWorkspacesMapEntryEClass, INSTALLATION_TO_WORKSPACES_MAP_ENTRY__VALUE);

    workspaceToInstallationsMapEntryEClass = createEClass(WORKSPACE_TO_INSTALLATIONS_MAP_ENTRY);
    createEReference(workspaceToInstallationsMapEntryEClass, WORKSPACE_TO_INSTALLATIONS_MAP_ENTRY__KEY);
    createEReference(workspaceToInstallationsMapEntryEClass, WORKSPACE_TO_INSTALLATIONS_MAP_ENTRY__VALUE);

    macroEClass = createEClass(MACRO);
    createEReference(macroEClass, MACRO__LOGICAL_CONTAINER);
    createEReference(macroEClass, MACRO__PARAMETERS);

    parameterEClass = createEClass(PARAMETER);
    createEAttribute(parameterEClass, PARAMETER__NAME);
    createEAttribute(parameterEClass, PARAMETER__DESCRIPTION);
    createEAttribute(parameterEClass, PARAMETER__DEFAULT_VALUE);

    macroTaskEClass = createEClass(MACRO_TASK);
    createEReference(macroTaskEClass, MACRO_TASK__ARGUMENTS);
    createEReference(macroTaskEClass, MACRO_TASK__MACRO);

    argumentEClass = createEClass(ARGUMENT);
    createEReference(argumentEClass, ARGUMENT__MACRO_TASK);
    createEAttribute(argumentEClass, ARGUMENT__NAME);
    createEReference(argumentEClass, ARGUMENT__PARAMETER);
    createEAttribute(argumentEClass, ARGUMENT__VALUE);

    // Create enums
    scopeTypeEEnum = createEEnum(SCOPE_TYPE);
    triggerEEnum = createEEnum(TRIGGER);
    variableTypeEEnum = createEEnum(VARIABLE_TYPE);
    unsignedPolicyEEnum = createEEnum(UNSIGNED_POLICY);
    certificatePolicyEEnum = createEEnum(CERTIFICATE_POLICY);

    // Create data types
    triggerSetEDataType = createEDataType(TRIGGER_SET);
    licenseInfoEDataType = createEDataType(LICENSE_INFO);
    filterEDataType = createEDataType(FILTER);
    certificateInfoEDataType = createEDataType(CERTIFICATE_INFO);
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
    BasePackage theBasePackage = (BasePackage)EPackage.Registry.INSTANCE.getEPackage(BasePackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    setupTaskEClass.getESuperTypes().add(theBasePackage.getModelElement());
    setupTaskContainerEClass.getESuperTypes().add(theBasePackage.getModelElement());
    scopeEClass.getESuperTypes().add(getSetupTaskContainer());
    indexEClass.getESuperTypes().add(theBasePackage.getModelElement());
    catalogSelectionEClass.getESuperTypes().add(theBasePackage.getModelElement());
    productCatalogEClass.getESuperTypes().add(getScope());
    productEClass.getESuperTypes().add(getScope());
    productVersionEClass.getESuperTypes().add(getScope());
    projectContainerEClass.getESuperTypes().add(getScope());
    projectCatalogEClass.getESuperTypes().add(getProjectContainer());
    projectEClass.getESuperTypes().add(getProjectContainer());
    streamEClass.getESuperTypes().add(getScope());
    userEClass.getESuperTypes().add(getScope());
    attributeRuleEClass.getESuperTypes().add(theBasePackage.getModelElement());
    installationEClass.getESuperTypes().add(getScope());
    installationTaskEClass.getESuperTypes().add(getSetupTask());
    workspaceEClass.getESuperTypes().add(getScope());
    workspaceTaskEClass.getESuperTypes().add(getSetupTask());
    configurationEClass.getESuperTypes().add(theBasePackage.getModelElement());
    compoundTaskEClass.getESuperTypes().add(getSetupTask());
    compoundTaskEClass.getESuperTypes().add(getSetupTaskContainer());
    variableTaskEClass.getESuperTypes().add(getSetupTask());
    variableChoiceEClass.getESuperTypes().add(theBasePackage.getModelElement());
    stringSubstitutionTaskEClass.getESuperTypes().add(getSetupTask());
    redirectionTaskEClass.getESuperTypes().add(getSetupTask());
    eclipseIniTaskEClass.getESuperTypes().add(getSetupTask());
    linkLocationTaskEClass.getESuperTypes().add(getSetupTask());
    preferenceTaskEClass.getESuperTypes().add(getSetupTask());
    resourceCopyTaskEClass.getESuperTypes().add(getSetupTask());
    resourceCreationTaskEClass.getESuperTypes().add(getSetupTask());
    textModifyTaskEClass.getESuperTypes().add(getSetupTask());
    textModificationEClass.getESuperTypes().add(theBasePackage.getModelElement());
    macroEClass.getESuperTypes().add(getScope());
    parameterEClass.getESuperTypes().add(theBasePackage.getModelElement());
    macroTaskEClass.getESuperTypes().add(getSetupTask());
    argumentEClass.getESuperTypes().add(theBasePackage.getModelElement());

    // Initialize classes and features; add operations and parameters
    initEClass(setupTaskEClass, SetupTask.class, "SetupTask", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getSetupTask_ID(), theBasePackage.getID(), "iD", null, 0, 1, SetupTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, //$NON-NLS-1$
        IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSetupTask_Description(), theBasePackage.getText(), "description", null, 0, 1, SetupTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSetupTask_ScopeType(), getScopeType(), "scopeType", null, 0, 1, SetupTask.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getSetupTask_ExcludedTriggers(), getTriggerSet(), "excludedTriggers", "", 1, 1, SetupTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$ //$NON-NLS-2$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSetupTask_Disabled(), ecorePackage.getEBoolean(), "disabled", null, 0, 1, SetupTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSetupTask_Predecessors(), getSetupTask(), null, "predecessors", null, 0, -1, SetupTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSetupTask_Successors(), getSetupTask(), null, "successors", null, 0, -1, SetupTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getSetupTask_Restrictions(), getScope(), null, "restrictions", null, 0, -1, SetupTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getSetupTask_Filter(), getFilter(), "filter", null, 0, 1, SetupTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, //$NON-NLS-1$
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    addEOperation(setupTaskEClass, getScope(), "getScope", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    EOperation op = addEOperation(setupTaskEClass, ecorePackage.getEBoolean(), "requires", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$
    addEParameter(op, getSetupTask(), "setupTask", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    addEOperation(setupTaskEClass, getTriggerSet(), "getValidTriggers", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    addEOperation(setupTaskEClass, getTriggerSet(), "getTriggers", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    initEClass(setupTaskContainerEClass, SetupTaskContainer.class, "SetupTaskContainer", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getSetupTaskContainer_SetupTasks(), getSetupTask(), null, "setupTasks", null, 0, -1, SetupTaskContainer.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(scopeEClass, Scope.class, "Scope", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getScope_Name(), ecorePackage.getEString(), "name", null, 1, 1, Scope.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, //$NON-NLS-1$
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getScope_Label(), ecorePackage.getEString(), "label", null, 0, 1, Scope.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, //$NON-NLS-1$
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getScope_Description(), theBasePackage.getText(), "description", null, 0, 1, Scope.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getScope_QualifiedName(), ecorePackage.getEString(), "qualifiedName", null, 1, 1, Scope.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    addEOperation(scopeEClass, getScope(), "getParentScope", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    addEOperation(scopeEClass, getScopeType(), "getType", 1, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    addEOperation(scopeEClass, ecorePackage.getEString(), "getQualifiedLabel", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    initEClass(indexEClass, Index.class, "Index", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getIndex_Name(), ecorePackage.getEString(), "name", null, 1, 1, Index.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, //$NON-NLS-1$
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIndex_DiscoverablePackages(), ecorePackage.getEPackage(), null, "discoverablePackages", null, 0, -1, Index.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getIndex_ProductCatalogs(), getProductCatalog(), getProductCatalog_Index(), "productCatalogs", null, 0, -1, Index.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getIndex_ProductCatalogs().getEKeys().add(getScope_Name());
    initEReference(getIndex_ProjectCatalogs(), getProjectCatalog(), getProjectCatalog_Index(), "projectCatalogs", null, 0, -1, Index.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getIndex_ProjectCatalogs().getEKeys().add(getScope_Name());

    initEClass(catalogSelectionEClass, CatalogSelection.class, "CatalogSelection", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getCatalogSelection_ProductCatalogs(), getProductCatalog(), null, "productCatalogs", null, 0, -1, CatalogSelection.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getCatalogSelection_ProductCatalogs().getEKeys().add(getScope_Name());
    initEReference(getCatalogSelection_ProjectCatalogs(), getProjectCatalog(), null, "projectCatalogs", null, 0, -1, CatalogSelection.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getCatalogSelection_ProjectCatalogs().getEKeys().add(getScope_Name());
    initEReference(getCatalogSelection_DefaultProductVersions(), getProductToProductVersionMapEntry(), null, "defaultProductVersions", null, 0, -1, //$NON-NLS-1$
        CatalogSelection.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED,
        IS_ORDERED);
    initEReference(getCatalogSelection_DefaultStreams(), getProjectToStreamMapEntry(), null, "defaultStreams", null, 0, -1, CatalogSelection.class, //$NON-NLS-1$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getCatalogSelection_SelectedStreams(), getStream(), null, "selectedStreams", null, 0, -1, CatalogSelection.class, IS_TRANSIENT, IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    initEClass(productCatalogEClass, ProductCatalog.class, "ProductCatalog", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getProductCatalog_Index(), getIndex(), getIndex_ProductCatalogs(), "index", null, 0, 1, ProductCatalog.class, IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProductCatalog_Products(), getProduct(), getProduct_ProductCatalog(), "products", null, 0, -1, ProductCatalog.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getProductCatalog_Products().getEKeys().add(getScope_Name());

    initEClass(productEClass, Product.class, "Product", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getProduct_ProductCatalog(), getProductCatalog(), getProductCatalog_Products(), "productCatalog", null, 0, 1, Product.class, IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getProduct_ProductCatalog().getEKeys().add(getScope_Name());
    initEReference(getProduct_Versions(), getProductVersion(), getProductVersion_Product(), "versions", null, 1, -1, Product.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getProduct_Versions().getEKeys().add(getScope_Name());

    initEClass(productVersionEClass, ProductVersion.class, "ProductVersion", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getProductVersion_Product(), getProduct(), getProduct_Versions(), "product", null, 1, 1, ProductVersion.class, IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getProductVersion_RequiredJavaVersion(), ecorePackage.getEString(), "requiredJavaVersion", null, 0, 1, ProductVersion.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(projectContainerEClass, ProjectContainer.class, "ProjectContainer", IS_ABSTRACT, IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getProjectContainer_Projects(), getProject(), getProject_ProjectContainer(), "projects", null, 0, -1, ProjectContainer.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getProjectContainer_Projects().getEKeys().add(getScope_Name());

    addEOperation(projectContainerEClass, getProjectContainer(), "getProjectContainer", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    addEOperation(projectContainerEClass, getProjectCatalog(), "getProjectCatalog", 0, 1, IS_UNIQUE, IS_ORDERED); //$NON-NLS-1$

    initEClass(projectCatalogEClass, ProjectCatalog.class, "ProjectCatalog", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getProjectCatalog_Index(), getIndex(), getIndex_ProjectCatalogs(), "index", null, 0, 1, ProjectCatalog.class, IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(projectEClass, Project.class, "Project", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getProject_Streams(), getStream(), getStream_Project(), "streams", null, 0, -1, Project.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getProject_Streams().getEKeys().add(getScope_Name());
    initEReference(getProject_ProjectContainer(), getProjectContainer(), getProjectContainer_Projects(), "projectContainer", null, 0, 1, Project.class, //$NON-NLS-1$
        IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProject_LogicalProjectContainer(), getProjectContainer(), null, "logicalProjectContainer", null, 0, 1, Project.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProject_ParentProject(), getProject(), null, "parentProject", null, 0, 1, Project.class, IS_TRANSIENT, IS_VOLATILE, !IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEReference(getProject_ProjectCatalog(), getProjectCatalog(), null, "projectCatalog", null, 0, 1, Project.class, IS_TRANSIENT, IS_VOLATILE, //$NON-NLS-1$
        !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

    initEClass(streamEClass, Stream.class, "Stream", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getStream_Project(), getProject(), getProject_Streams(), "project", null, 0, 1, Stream.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(userEClass, User.class, "User", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getUser_AttributeRules(), getAttributeRule(), null, "attributeRules", null, 0, -1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getUser_AttributeRules().getEKeys().add(getAttributeRule_AttributeURI());
    initEAttribute(getUser_AcceptedLicenses(), getLicenseInfo(), "acceptedLicenses", null, 0, -1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getUser_AcceptedCertificates(), getCertificateInfo(), "acceptedCertificates", null, 0, -1, User.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getUser_UnsignedPolicy(), getUnsignedPolicy(), "unsignedPolicy", "PROMPT", 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$ //$NON-NLS-2$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getUser_CertificatePolicy(), getCertificatePolicy(), "certificatePolicy", "PROMPT", 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$ //$NON-NLS-2$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getUser_QuestionnaireDate(), ecorePackage.getEDate(), "questionnaireDate", null, 0, 1, User.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getUser_PreferenceRecorderDefault(), ecorePackage.getEBoolean(), "preferenceRecorderDefault", "true", 0, 1, User.class, !IS_TRANSIENT, //$NON-NLS-1$ //$NON-NLS-2$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(attributeRuleEClass, AttributeRule.class, "AttributeRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getAttributeRule_AttributeURI(), theBasePackage.getURI(), "attributeURI", null, 0, 1, AttributeRule.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getAttributeRule_Value(), ecorePackage.getEString(), "value", null, 1, 1, AttributeRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(locationCatalogEClass, LocationCatalog.class, "LocationCatalog", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getLocationCatalog_Installations(), getInstallationToWorkspacesMapEntry(), null, "installations", null, 0, -1, LocationCatalog.class, //$NON-NLS-1$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getLocationCatalog_Workspaces(), getWorkspaceToInstallationsMapEntry(), null, "workspaces", null, 0, -1, LocationCatalog.class, //$NON-NLS-1$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(installationEClass, Installation.class, "Installation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getInstallation_ProductVersion(), getProductVersion(), null, "productVersion", null, 1, 1, Installation.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(installationTaskEClass, InstallationTask.class, "InstallationTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getInstallationTask_Location(), ecorePackage.getEString(), "location", "", 1, 1, InstallationTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$ //$NON-NLS-2$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getInstallationTask_RelativeProductFolder(), ecorePackage.getEString(), "relativeProductFolder", "", 1, 1, InstallationTask.class, //$NON-NLS-1$ //$NON-NLS-2$
        !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(workspaceEClass, Workspace.class, "Workspace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getWorkspace_Streams(), getStream(), null, "streams", null, 0, -1, Workspace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(workspaceTaskEClass, WorkspaceTask.class, "WorkspaceTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getWorkspaceTask_Location(), ecorePackage.getEString(), "location", "", 1, 1, WorkspaceTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$ //$NON-NLS-2$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(configurationEClass, Configuration.class, "Configuration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getConfiguration_Label(), ecorePackage.getEString(), "label", null, 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getConfiguration_Description(), theBasePackage.getText(), "description", null, 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getConfiguration_Installation(), getInstallation(), null, "installation", null, 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getConfiguration_Workspace(), getWorkspace(), null, "workspace", null, 0, 1, Configuration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(compoundTaskEClass, CompoundTask.class, "CompoundTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getCompoundTask_Name(), ecorePackage.getEString(), "name", null, 1, 1, CompoundTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(variableTaskEClass, VariableTask.class, "VariableTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getVariableTask_Type(), getVariableType(), "type", "STRING", 1, 1, VariableTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$ //$NON-NLS-2$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVariableTask_Name(), ecorePackage.getEString(), "name", null, 1, 1, VariableTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVariableTask_Value(), ecorePackage.getEString(), "value", null, 0, 1, VariableTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVariableTask_DefaultValue(), ecorePackage.getEString(), "defaultValue", null, 0, 1, VariableTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVariableTask_StorePromptedValue(), ecorePackage.getEBoolean(), "storePromptedValue", "true", 0, 1, VariableTask.class, IS_TRANSIENT, //$NON-NLS-1$ //$NON-NLS-2$
        IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, IS_DERIVED, IS_ORDERED);
    initEAttribute(getVariableTask_StorageURI(), theBasePackage.getURI(), "storageURI", "scope://", 0, 1, VariableTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$ //$NON-NLS-2$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVariableTask_Label(), ecorePackage.getEString(), "label", null, 0, 1, VariableTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getVariableTask_Choices(), getVariableChoice(), null, "choices", null, 0, -1, VariableTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(variableChoiceEClass, VariableChoice.class, "VariableChoice", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getVariableChoice_Value(), ecorePackage.getEString(), "value", null, 1, 1, VariableChoice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getVariableChoice_Label(), ecorePackage.getEString(), "label", null, 0, 1, VariableChoice.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(stringSubstitutionTaskEClass, StringSubstitutionTask.class, "StringSubstitutionTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getStringSubstitutionTask_Name(), ecorePackage.getEString(), "name", null, 1, 1, StringSubstitutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getStringSubstitutionTask_Value(), ecorePackage.getEString(), "value", null, 1, 1, StringSubstitutionTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(redirectionTaskEClass, RedirectionTask.class, "RedirectionTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getRedirectionTask_SourceURL(), ecorePackage.getEString(), "sourceURL", null, 1, 1, RedirectionTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getRedirectionTask_TargetURL(), ecorePackage.getEString(), "targetURL", null, 1, 1, RedirectionTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(eclipseIniTaskEClass, EclipseIniTask.class, "EclipseIniTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getEclipseIniTask_Option(), ecorePackage.getEString(), "option", null, 1, 1, EclipseIniTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getEclipseIniTask_Value(), ecorePackage.getEString(), "value", null, 0, 1, EclipseIniTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getEclipseIniTask_Vm(), ecorePackage.getEBoolean(), "vm", null, 0, 1, EclipseIniTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(linkLocationTaskEClass, LinkLocationTask.class, "LinkLocationTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getLinkLocationTask_Path(), ecorePackage.getEString(), "path", null, 1, 1, LinkLocationTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getLinkLocationTask_Name(), ecorePackage.getEString(), "name", null, 0, 1, LinkLocationTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(preferenceTaskEClass, PreferenceTask.class, "PreferenceTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getPreferenceTask_Key(), ecorePackage.getEString(), "key", null, 1, 1, PreferenceTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getPreferenceTask_Value(), ecorePackage.getEString(), "value", null, 0, 1, PreferenceTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(resourceCopyTaskEClass, ResourceCopyTask.class, "ResourceCopyTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getResourceCopyTask_Force(), ecorePackage.getEBoolean(), "force", null, 0, 1, ResourceCopyTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getResourceCopyTask_SourceURL(), ecorePackage.getEString(), "sourceURL", null, 1, 1, ResourceCopyTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getResourceCopyTask_TargetURL(), ecorePackage.getEString(), "targetURL", null, 1, 1, ResourceCopyTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(resourceCreationTaskEClass, ResourceCreationTask.class, "ResourceCreationTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getResourceCreationTask_Force(), ecorePackage.getEBoolean(), "force", null, 0, 1, ResourceCreationTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getResourceCreationTask_Content(), theBasePackage.getText(), "content", null, 1, 1, ResourceCreationTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getResourceCreationTask_TargetURL(), ecorePackage.getEString(), "targetURL", null, 1, 1, ResourceCreationTask.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getResourceCreationTask_Encoding(), ecorePackage.getEString(), "encoding", null, 0, 1, ResourceCreationTask.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(textModifyTaskEClass, TextModifyTask.class, "TextModifyTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getTextModifyTask_URL(), ecorePackage.getEString(), "uRL", null, 1, 1, TextModifyTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTextModifyTask_Modifications(), getTextModification(), null, "modifications", null, 0, -1, TextModifyTask.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTextModifyTask_Encoding(), ecorePackage.getEString(), "encoding", null, 0, 1, TextModifyTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(textModificationEClass, TextModification.class, "TextModification", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getTextModification_Pattern(), ecorePackage.getEString(), "pattern", null, 1, 1, TextModification.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTextModification_Substitutions(), ecorePackage.getEString(), "substitutions", null, 0, -1, TextModification.class, !IS_TRANSIENT, //$NON-NLS-1$
        !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(productToProductVersionMapEntryEClass, Map.Entry.class, "ProductToProductVersionMapEntry", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
        !IS_GENERATED_INSTANCE_CLASS);
    initEReference(getProductToProductVersionMapEntry_Key(), getProduct(), null, "key", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProductToProductVersionMapEntry_Value(), getProductVersion(), null, "value", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(projectToStreamMapEntryEClass, Map.Entry.class, "ProjectToStreamMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getProjectToStreamMapEntry_Key(), getProject(), null, "key", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getProjectToStreamMapEntry_Value(), getStream(), null, "value", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getProjectToStreamMapEntry_Selection(), ecorePackage.getEBoolean(), "selection", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(installationToWorkspacesMapEntryEClass, Map.Entry.class, "InstallationToWorkspacesMapEntry", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
        !IS_GENERATED_INSTANCE_CLASS);
    initEReference(getInstallationToWorkspacesMapEntry_Key(), getInstallation(), null, "key", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getInstallationToWorkspacesMapEntry_Value(), getWorkspace(), null, "value", null, 0, -1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(workspaceToInstallationsMapEntryEClass, Map.Entry.class, "WorkspaceToInstallationsMapEntry", !IS_ABSTRACT, !IS_INTERFACE, //$NON-NLS-1$
        !IS_GENERATED_INSTANCE_CLASS);
    initEReference(getWorkspaceToInstallationsMapEntry_Key(), getWorkspace(), null, "key", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getWorkspaceToInstallationsMapEntry_Value(), getInstallation(), null, "value", null, 0, -1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(macroEClass, Macro.class, "Macro", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getMacro_LogicalContainer(), getMacroTask(), null, "logicalContainer", null, 0, 1, Macro.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getMacro_Parameters(), getParameter(), null, "parameters", null, 0, -1, Macro.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    getMacro_Parameters().getEKeys().add(getParameter_Name());

    initEClass(parameterEClass, Parameter.class, "Parameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEAttribute(getParameter_Name(), ecorePackage.getEString(), "name", null, 1, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getParameter_Description(), theBasePackage.getText(), "description", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getParameter_DefaultValue(), ecorePackage.getEString(), "defaultValue", null, 0, 1, Parameter.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(macroTaskEClass, MacroTask.class, "MacroTask", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getMacroTask_Arguments(), getArgument(), getArgument_MacroTask(), "arguments", null, 0, -1, MacroTask.class, !IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getMacroTask_Macro(), getMacro(), null, "macro", null, 1, 1, MacroTask.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, //$NON-NLS-1$
        IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(argumentEClass, Argument.class, "Argument", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEReference(getArgument_MacroTask(), getMacroTask(), getMacroTask_Arguments(), "macroTask", null, 0, 1, Argument.class, IS_TRANSIENT, !IS_VOLATILE, //$NON-NLS-1$
        !IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getArgument_Name(), ecorePackage.getEString(), "name", null, 0, 1, Argument.class, !IS_TRANSIENT, IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, //$NON-NLS-1$
        !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getArgument_Parameter(), getParameter(), null, "parameter", null, 1, 1, Argument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_COMPOSITE, IS_RESOLVE_PROXIES, IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getArgument_Value(), ecorePackage.getEString(), "value", null, 0, 1, Argument.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, //$NON-NLS-1$
        !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(scopeTypeEEnum, ScopeType.class, "ScopeType"); //$NON-NLS-1$
    addEEnumLiteral(scopeTypeEEnum, ScopeType.NONE);
    addEEnumLiteral(scopeTypeEEnum, ScopeType.PRODUCT_CATALOG);
    addEEnumLiteral(scopeTypeEEnum, ScopeType.PRODUCT);
    addEEnumLiteral(scopeTypeEEnum, ScopeType.PRODUCT_VERSION);
    addEEnumLiteral(scopeTypeEEnum, ScopeType.PROJECT_CATALOG);
    addEEnumLiteral(scopeTypeEEnum, ScopeType.PROJECT);
    addEEnumLiteral(scopeTypeEEnum, ScopeType.STREAM);
    addEEnumLiteral(scopeTypeEEnum, ScopeType.INSTALLATION);
    addEEnumLiteral(scopeTypeEEnum, ScopeType.WORKSPACE);
    addEEnumLiteral(scopeTypeEEnum, ScopeType.USER);
    addEEnumLiteral(scopeTypeEEnum, ScopeType.MACRO);

    initEEnum(triggerEEnum, Trigger.class, "Trigger"); //$NON-NLS-1$
    addEEnumLiteral(triggerEEnum, Trigger.BOOTSTRAP);
    addEEnumLiteral(triggerEEnum, Trigger.STARTUP);
    addEEnumLiteral(triggerEEnum, Trigger.MANUAL);

    initEEnum(variableTypeEEnum, VariableType.class, "VariableType"); //$NON-NLS-1$
    addEEnumLiteral(variableTypeEEnum, VariableType.STRING);
    addEEnumLiteral(variableTypeEEnum, VariableType.TEXT);
    addEEnumLiteral(variableTypeEEnum, VariableType.PASSWORD);
    addEEnumLiteral(variableTypeEEnum, VariableType.PATTERN);
    addEEnumLiteral(variableTypeEEnum, VariableType.URI);
    addEEnumLiteral(variableTypeEEnum, VariableType.FILE);
    addEEnumLiteral(variableTypeEEnum, VariableType.FOLDER);
    addEEnumLiteral(variableTypeEEnum, VariableType.RESOURCE);
    addEEnumLiteral(variableTypeEEnum, VariableType.CONTAINER);
    addEEnumLiteral(variableTypeEEnum, VariableType.PROJECT);
    addEEnumLiteral(variableTypeEEnum, VariableType.BOOLEAN);
    addEEnumLiteral(variableTypeEEnum, VariableType.INTEGER);
    addEEnumLiteral(variableTypeEEnum, VariableType.FLOAT);
    addEEnumLiteral(variableTypeEEnum, VariableType.JRE);

    initEEnum(unsignedPolicyEEnum, UnsignedPolicy.class, "UnsignedPolicy"); //$NON-NLS-1$
    addEEnumLiteral(unsignedPolicyEEnum, UnsignedPolicy.PROMPT);
    addEEnumLiteral(unsignedPolicyEEnum, UnsignedPolicy.ACCEPT);
    addEEnumLiteral(unsignedPolicyEEnum, UnsignedPolicy.DECLINE);

    initEEnum(certificatePolicyEEnum, CertificatePolicy.class, "CertificatePolicy"); //$NON-NLS-1$
    addEEnumLiteral(certificatePolicyEEnum, CertificatePolicy.PROMPT);
    addEEnumLiteral(certificatePolicyEEnum, CertificatePolicy.ACCEPT);
    addEEnumLiteral(certificatePolicyEEnum, CertificatePolicy.DECLINE);

    // Initialize data types
    initEDataType(triggerSetEDataType, Set.class, "TriggerSet", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS, //$NON-NLS-1$
        "java.util.Set<org.eclipse.oomph.setup.Trigger>"); //$NON-NLS-1$
    initEDataType(licenseInfoEDataType, LicenseInfo.class, "LicenseInfo", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(filterEDataType, String.class, "Filter", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$
    initEDataType(certificateInfoEDataType, CertificateInfo.class, "CertificateInfo", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS); //$NON-NLS-1$

    // Create resource
    createResource(eNS_URI);

    // Create annotations
    // http://www.eclipse.org/oomph/base/LabelProvider
    createLabelProviderAnnotations();
    // http:///org/eclipse/emf/ecore/util/ExtendedMetaData
    createExtendedMetaDataAnnotations();
    // http://www.eclipse.org/oomph/setup/NoExpand
    createNoExpandAnnotations();
    // http://www.eclipse.org/oomph/setup/Variable
    createVariableAnnotations();
    // http://www.eclipse.org/oomph/setup/RuleVariable
    createRuleVariableAnnotations();
    // http://www.eclipse.org/oomph/setup/ValidTriggers
    createValidTriggersAnnotations();
    // http://www.eclipse.org/emf/2002/Ecore
    createEcoreAnnotations();
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
        new String[] { "imageBaseURI", "http://git.eclipse.org/c/oomph/org.eclipse.oomph.git/plain/plugins/org.eclipse.oomph.setup.edit/icons/full/obj16" //$NON-NLS-1$ //$NON-NLS-2$
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
    addAnnotation(getSetupTask_ID(), source, new String[] { "kind", "attribute", //$NON-NLS-1$ //$NON-NLS-2$
        "name", "id" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getSetupTask_Description(), source, new String[] { "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getSetupTask_Predecessors(), source, new String[] { "name", "predecessor" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getSetupTask_Successors(), source, new String[] { "name", "successor" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getSetupTask_Restrictions(), source, new String[] { "name", "restriction" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getSetupTaskContainer_SetupTasks(), source, new String[] { "name", "setupTask" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getScope_Description(), source, new String[] { "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getIndex_DiscoverablePackages(), source, new String[] { "name", "discoverablePackage" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getIndex_ProductCatalogs(), source, new String[] { "name", "productCatalog" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getIndex_ProjectCatalogs(), source, new String[] { "name", "projectCatalog" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getCatalogSelection_ProductCatalogs(), source, new String[] { "name", "productCatalog" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getCatalogSelection_ProjectCatalogs(), source, new String[] { "name", "projectCatalog" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getCatalogSelection_DefaultProductVersions(), source, new String[] { "name", "defaultProductVersion" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getCatalogSelection_DefaultStreams(), source, new String[] { "name", "defaultStream" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getProductCatalog_Products(), source, new String[] { "name", "product" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getProduct_Versions(), source, new String[] { "name", "version" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getProjectContainer_Projects(), source, new String[] { "name", "project" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getProject_Streams(), source, new String[] { "name", "stream" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getUser_AttributeRules(), source, new String[] { "name", "attributeRule" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getUser_AcceptedLicenses(), source, new String[] { "name", "acceptedLicense" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getUser_AcceptedCertificates(), source, new String[] { "name", "acceptedCertificate" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getLocationCatalog_Installations(), source, new String[] { "name", "installation" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getLocationCatalog_Workspaces(), source, new String[] { "name", "workspace" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getWorkspace_Streams(), source, new String[] { "name", "stream" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getConfiguration_Description(), source, new String[] { "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getVariableTask_Choices(), source, new String[] { "name", "choice" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getResourceCreationTask_Content(), source, new String[] { "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getTextModifyTask_URL(), source, new String[] { "kind", "attribute", //$NON-NLS-1$ //$NON-NLS-2$
        "name", "url" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getTextModifyTask_Modifications(), source, new String[] { "name", "modification" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getTextModification_Substitutions(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
        "name", "substitution" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getProductToProductVersionMapEntry_Key(), source, new String[] { "kind", "attribute" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getProductToProductVersionMapEntry_Value(), source, new String[] { "kind", "attribute" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getProjectToStreamMapEntry_Key(), source, new String[] { "kind", "attribute" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getProjectToStreamMapEntry_Value(), source, new String[] { "kind", "attribute" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getMacro_Parameters(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
        "name", "parameter" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getParameter_Description(), source, new String[] { "kind", "element" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getMacroTask_Arguments(), source, new String[] { "kind", "element", //$NON-NLS-1$ //$NON-NLS-2$
        "name", "argument" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getMacroTask_Macro(), source, new String[] { "kind", "attribute" //$NON-NLS-1$ //$NON-NLS-2$
    });
  }

  /**
   * Initializes the annotations for <b>http://www.eclipse.org/oomph/setup/NoExpand</b>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createNoExpandAnnotations()
  {
    String source = "http://www.eclipse.org/oomph/setup/NoExpand"; //$NON-NLS-1$
    addAnnotation(getSetupTask_ID(), source, new String[] {});
    addAnnotation(getSetupTask_Disabled(), source, new String[] {});
    addAnnotation(getSetupTask_Filter(), source, new String[] {});
    addAnnotation(getVariableTask_Type(), source, new String[] {});
    addAnnotation(getVariableTask_StorePromptedValue(), source, new String[] {});
    addAnnotation(getVariableTask_StorageURI(), source, new String[] {});
    addAnnotation(getVariableChoice_Value(), source, new String[] {});
    addAnnotation(getParameter_Name(), source, new String[] {});
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
    addAnnotation(getInstallationTask_Location(), source, new String[] { "name", "install.root", //$NON-NLS-1$ //$NON-NLS-2$
        "type", "FOLDER", //$NON-NLS-1$ //$NON-NLS-2$
        "label", "Root install folder", //$NON-NLS-1$ //$NON-NLS-2$
        "defaultValue", "${user.home}", //$NON-NLS-1$ //$NON-NLS-2$
        "description", "The root install folder where all the products are installed", //$NON-NLS-1$ //$NON-NLS-2$
        "storageURI", "scope://" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getInstallationTask_Location(), source, new String[] { "name", "installation.id", //$NON-NLS-1$ //$NON-NLS-2$
        "type", "STRING", //$NON-NLS-1$ //$NON-NLS-2$
        "label", "Installation folder name", //$NON-NLS-1$ //$NON-NLS-2$
        "defaultValue", "${scope.product.name|installationID}", //$NON-NLS-1$ //$NON-NLS-2$
        "description", "The name of the folder within the root install folder where the product is installed" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getWorkspaceTask_Location(), source, new String[] { "name", "workspace.container.root", //$NON-NLS-1$ //$NON-NLS-2$
        "type", "FOLDER", //$NON-NLS-1$ //$NON-NLS-2$
        "label", "Root workspace-container folder", //$NON-NLS-1$ //$NON-NLS-2$
        "defaultValue", "${user.home}", //$NON-NLS-1$ //$NON-NLS-2$
        "description", "The root workspace-container folder where all the workspaces are located", //$NON-NLS-1$ //$NON-NLS-2$
        "storageURI", "scope://" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getWorkspaceTask_Location(), source, new String[] { "name", "workspace.id", //$NON-NLS-1$ //$NON-NLS-2$
        "type", "STRING", //$NON-NLS-1$ //$NON-NLS-2$
        "label", "Workspace folder name", //$NON-NLS-1$ //$NON-NLS-2$
        "defaultValue", "${scope.project.name|workspaceID}", //$NON-NLS-1$ //$NON-NLS-2$
        "description", "The name of the workspace folder within the root workspace-container folder where the workspaces are located" //$NON-NLS-1$ //$NON-NLS-2$
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
    addAnnotation(stringSubstitutionTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(preferenceTaskEClass, source, new String[] { "triggers", "STARTUP MANUAL" //$NON-NLS-1$ //$NON-NLS-2$
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
    addAnnotation(macroEClass, source, new String[] { "constraints", "NoRecursion" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(macroTaskEClass, source, new String[] { "constraints", "IDRequired ArgumentsCorrespondToParameters" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(argumentEClass, source, new String[] { "constraints", "ConsistentParameterBinding" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(filterEDataType, source, new String[] { "constraints", "WellformedFilterExpression" //$NON-NLS-1$ //$NON-NLS-2$
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
    addAnnotation(getInstallationTask_Location(), source, new String[] { "filter", "canonical", //$NON-NLS-1$ //$NON-NLS-2$
        "type", "STRING", //$NON-NLS-1$ //$NON-NLS-2$
        "label", "Installation location rule", //$NON-NLS-1$ //$NON-NLS-2$
        "description", "The rule for the absolute folder location where the product is installed", //$NON-NLS-1$ //$NON-NLS-2$
        "storageURI", null, //$NON-NLS-1$
        "explicitType", "FOLDER", //$NON-NLS-1$ //$NON-NLS-2$
        "explicitLabel", "Installation location", //$NON-NLS-1$ //$NON-NLS-2$
        "explicitDescription", "The absolute folder location where the product is installed" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getInstallationTask_Location(), new boolean[] { true }, "Choice", //$NON-NLS-1$
        new String[] { "value", "${install.root/}${installation.id}", //$NON-NLS-1$ //$NON-NLS-2$
            "label", "Installed in a uniquely-named folder within the root install folder" //$NON-NLS-1$ //$NON-NLS-2$
        });
    addAnnotation(getInstallationTask_Location(), new boolean[] { true }, "Choice", //$NON-NLS-1$
        new String[] { "value", "${@id.location}", //$NON-NLS-1$ //$NON-NLS-2$
            "label", "Installed in the specified absolute folder location" //$NON-NLS-1$ //$NON-NLS-2$
        });
    addAnnotation(getWorkspaceTask_Location(), source, new String[] { "filter", "canonical", //$NON-NLS-1$ //$NON-NLS-2$
        "type", "STRING", //$NON-NLS-1$ //$NON-NLS-2$
        "label", "Workspace location rule", //$NON-NLS-1$ //$NON-NLS-2$
        "description", "The rule for the absolute folder location where the workspace is located", //$NON-NLS-1$ //$NON-NLS-2$
        "storageURI", null, //$NON-NLS-1$
        "explicitType", "FOLDER", //$NON-NLS-1$ //$NON-NLS-2$
        "explicitLabel", "Workspace location", //$NON-NLS-1$ //$NON-NLS-2$
        "explicitDescription", "The absolute folder location where the workspace is located" //$NON-NLS-1$ //$NON-NLS-2$
    });
    addAnnotation(getWorkspaceTask_Location(), new boolean[] { true }, "Choice", //$NON-NLS-1$
        new String[] { "value", "${installation.location/ws}", //$NON-NLS-1$ //$NON-NLS-2$
            "label", "Located in a folder named \'ws\' within the installation folder" //$NON-NLS-1$ //$NON-NLS-2$
        });
    addAnnotation(getWorkspaceTask_Location(), new boolean[] { true }, "Choice", //$NON-NLS-1$
        new String[] { "value", "${workspace.container.root/}${workspace.id}", //$NON-NLS-1$ //$NON-NLS-2$
            "label", "Located in a uniquely-named folder within the root workspace-container folder" //$NON-NLS-1$ //$NON-NLS-2$
        });
    addAnnotation(getWorkspaceTask_Location(), new boolean[] { true }, "Choice", //$NON-NLS-1$
        new String[] { "value", "${@id.location}", //$NON-NLS-1$ //$NON-NLS-2$
            "label", "Located in the specified absolute folder location" //$NON-NLS-1$ //$NON-NLS-2$
        });
  }

} // SetupPackageImpl
