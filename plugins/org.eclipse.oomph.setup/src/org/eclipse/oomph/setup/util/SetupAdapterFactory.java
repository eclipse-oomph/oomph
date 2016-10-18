/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.util;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.setup.AttributeRule;
import org.eclipse.oomph.setup.CatalogSelection;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Configuration;
import org.eclipse.oomph.setup.EclipseIniTask;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.LinkLocationTask;
import org.eclipse.oomph.setup.LocationCatalog;
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
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.StringSubstitutionTask;
import org.eclipse.oomph.setup.TextModification;
import org.eclipse.oomph.setup.TextModifyTask;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableChoice;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.WorkspaceTask;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.SetupPackage
 * @generated
 */
public class SetupAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static SetupPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = SetupPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SetupSwitch<Adapter> modelSwitch = new SetupSwitch<Adapter>()
  {
    @Override
    public Adapter caseSetupTask(SetupTask object)
    {
      return createSetupTaskAdapter();
    }

    @Override
    public Adapter caseSetupTaskContainer(SetupTaskContainer object)
    {
      return createSetupTaskContainerAdapter();
    }

    @Override
    public Adapter caseScope(Scope object)
    {
      return createScopeAdapter();
    }

    @Override
    public Adapter caseIndex(Index object)
    {
      return createIndexAdapter();
    }

    @Override
    public Adapter caseCatalogSelection(CatalogSelection object)
    {
      return createCatalogSelectionAdapter();
    }

    @Override
    public Adapter caseProductCatalog(ProductCatalog object)
    {
      return createProductCatalogAdapter();
    }

    @Override
    public Adapter caseProduct(Product object)
    {
      return createProductAdapter();
    }

    @Override
    public Adapter caseProductVersion(ProductVersion object)
    {
      return createProductVersionAdapter();
    }

    @Override
    public Adapter caseProjectContainer(ProjectContainer object)
    {
      return createProjectContainerAdapter();
    }

    @Override
    public Adapter caseProjectCatalog(ProjectCatalog object)
    {
      return createProjectCatalogAdapter();
    }

    @Override
    public Adapter caseProject(Project object)
    {
      return createProjectAdapter();
    }

    @Override
    public Adapter caseStream(Stream object)
    {
      return createStreamAdapter();
    }

    @Override
    public Adapter caseUser(User object)
    {
      return createUserAdapter();
    }

    @Override
    public Adapter caseAttributeRule(AttributeRule object)
    {
      return createAttributeRuleAdapter();
    }

    @Override
    public Adapter caseLocationCatalog(LocationCatalog object)
    {
      return createLocationCatalogAdapter();
    }

    @Override
    public Adapter caseInstallation(Installation object)
    {
      return createInstallationAdapter();
    }

    @Override
    public Adapter caseInstallationTask(InstallationTask object)
    {
      return createInstallationTaskAdapter();
    }

    @Override
    public Adapter caseWorkspace(Workspace object)
    {
      return createWorkspaceAdapter();
    }

    @Override
    public Adapter caseWorkspaceTask(WorkspaceTask object)
    {
      return createWorkspaceTaskAdapter();
    }

    @Override
    public Adapter caseConfiguration(Configuration object)
    {
      return createConfigurationAdapter();
    }

    @Override
    public Adapter caseCompoundTask(CompoundTask object)
    {
      return createCompoundTaskAdapter();
    }

    @Override
    public Adapter caseVariableTask(VariableTask object)
    {
      return createVariableTaskAdapter();
    }

    @Override
    public Adapter caseVariableChoice(VariableChoice object)
    {
      return createVariableChoiceAdapter();
    }

    @Override
    public Adapter caseStringSubstitutionTask(StringSubstitutionTask object)
    {
      return createStringSubstitutionTaskAdapter();
    }

    @Override
    public Adapter caseRedirectionTask(RedirectionTask object)
    {
      return createRedirectionTaskAdapter();
    }

    @Override
    public Adapter caseEclipseIniTask(EclipseIniTask object)
    {
      return createEclipseIniTaskAdapter();
    }

    @Override
    public Adapter caseLinkLocationTask(LinkLocationTask object)
    {
      return createLinkLocationTaskAdapter();
    }

    @Override
    public Adapter casePreferenceTask(PreferenceTask object)
    {
      return createPreferenceTaskAdapter();
    }

    @Override
    public Adapter caseResourceCopyTask(ResourceCopyTask object)
    {
      return createResourceCopyTaskAdapter();
    }

    @Override
    public Adapter caseResourceCreationTask(ResourceCreationTask object)
    {
      return createResourceCreationTaskAdapter();
    }

    @Override
    public Adapter caseTextModifyTask(TextModifyTask object)
    {
      return createTextModifyTaskAdapter();
    }

    @Override
    public Adapter caseTextModification(TextModification object)
    {
      return createTextModificationAdapter();
    }

    @Override
    public Adapter caseProductToProductVersionMapEntry(Map.Entry<Product, ProductVersion> object)
    {
      return createProductToProductVersionMapEntryAdapter();
    }

    @Override
    public Adapter caseProjectToStreamMapEntry(Map.Entry<Project, Stream> object)
    {
      return createProjectToStreamMapEntryAdapter();
    }

    @Override
    public Adapter caseInstallationToWorkspacesMapEntry(Map.Entry<Installation, EList<Workspace>> object)
    {
      return createInstallationToWorkspacesMapEntryAdapter();
    }

    @Override
    public Adapter caseWorkspaceToInstallationsMapEntry(Map.Entry<Workspace, EList<Installation>> object)
    {
      return createWorkspaceToInstallationsMapEntryAdapter();
    }

    @Override
    public Adapter caseModelElement(ModelElement object)
    {
      return createModelElementAdapter();
    }

    @Override
    public Adapter defaultCase(EObject object)
    {
      return createEObjectAdapter();
    }
  };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.Index <em>Index</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.Index
   * @generated
   */
  public Adapter createIndexAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.CatalogSelection <em>Catalog Selection</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.CatalogSelection
   * @generated
   */
  public Adapter createCatalogSelectionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.Project <em>Project</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.Project
   * @generated
   */
  public Adapter createProjectAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.Stream <em>Stream</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.Stream
   * @generated
   */
  public Adapter createStreamAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.Installation <em>Installation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.Installation
   * @generated
   */
  public Adapter createInstallationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.ProductCatalog <em>Product Catalog</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.ProductCatalog
   * @generated
   */
  public Adapter createProductCatalogAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.Product <em>Product</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.Product
   * @generated
   */
  public Adapter createProductAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.ProductVersion <em>Product Version</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.ProductVersion
   * @generated
   */
  public Adapter createProductVersionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.ProjectCatalog <em>Project Catalog</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.ProjectCatalog
   * @generated
   */
  public Adapter createProjectCatalogAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.InstallationTask <em>Installation Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.InstallationTask
   * @generated
   */
  public Adapter createInstallationTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.WorkspaceTask <em>Workspace Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.WorkspaceTask
   * @generated
   */
  public Adapter createWorkspaceTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.Configuration <em>Configuration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.Configuration
   * @generated
   */
  public Adapter createConfigurationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.CompoundTask <em>Compound Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.CompoundTask
   * @generated
   */
  public Adapter createCompoundTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.VariableTask <em>Variable Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.VariableTask
   * @generated
   */
  public Adapter createVariableTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.SetupTask <em>Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.SetupTask
   * @generated
   */
  public Adapter createSetupTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.ResourceCopyTask <em>Resource Copy Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.ResourceCopyTask
   * @generated
   */
  public Adapter createResourceCopyTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.TextModifyTask <em>Text Modify Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.TextModifyTask
   * @generated
   */
  public Adapter createTextModifyTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.TextModification <em>Text Modification</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.TextModification
   * @generated
   */
  public Adapter createTextModificationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Product To Product Version Map Entry</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see java.util.Map.Entry
   * @generated
   */
  public Adapter createProductToProductVersionMapEntryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Project To Stream Map Entry</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see java.util.Map.Entry
   * @generated
   */
  public Adapter createProjectToStreamMapEntryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Installation To Workspaces Map Entry</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see java.util.Map.Entry
   * @generated
   */
  public Adapter createInstallationToWorkspacesMapEntryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Workspace To Installations Map Entry</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see java.util.Map.Entry
   * @generated
   */
  public Adapter createWorkspaceToInstallationsMapEntryAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.base.ModelElement <em>Model Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.base.ModelElement
   * @generated
   */
  public Adapter createModelElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.StringSubstitutionTask <em>String Substitution Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.StringSubstitutionTask
   * @generated
   */
  public Adapter createStringSubstitutionTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.ProjectContainer <em>Project Container</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.ProjectContainer
   * @generated
   */
  public Adapter createProjectContainerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.AttributeRule <em>Attribute Rule</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.AttributeRule
   * @generated
   */
  public Adapter createAttributeRuleAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.LocationCatalog <em>Location Catalog</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.LocationCatalog
   * @generated
   */
  public Adapter createLocationCatalogAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.RedirectionTask <em>Redirection Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.RedirectionTask
   * @generated
   */
  public Adapter createRedirectionTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.VariableChoice <em>Variable Choice</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.VariableChoice
   * @generated
   */
  public Adapter createVariableChoiceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.ResourceCreationTask <em>Resource Creation Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.ResourceCreationTask
   * @generated
   */
  public Adapter createResourceCreationTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.EclipseIniTask <em>Eclipse Ini Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.EclipseIniTask
   * @generated
   */
  public Adapter createEclipseIniTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.User <em>User</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.User
   * @generated
   */
  public Adapter createUserAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.Workspace <em>Workspace</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.Workspace
   * @generated
   */
  public Adapter createWorkspaceAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.LinkLocationTask <em>Link Location Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.LinkLocationTask
   * @generated
   */
  public Adapter createLinkLocationTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.PreferenceTask <em>Preference Task</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.PreferenceTask
   * @generated
   */
  public Adapter createPreferenceTaskAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.SetupTaskContainer <em>Task Container</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.SetupTaskContainer
   * @generated
   */
  public Adapter createSetupTaskContainerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.oomph.setup.Scope <em>Scope</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.oomph.setup.Scope
   * @generated
   */
  public Adapter createScopeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} // SetupAdapterFactory
