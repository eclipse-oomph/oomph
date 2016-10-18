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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.SetupPackage
 * @generated
 */
public class SetupSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static SetupPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = SetupPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case SetupPackage.SETUP_TASK:
      {
        SetupTask setupTask = (SetupTask)theEObject;
        T result = caseSetupTask(setupTask);
        if (result == null)
        {
          result = caseModelElement(setupTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.SETUP_TASK_CONTAINER:
      {
        SetupTaskContainer setupTaskContainer = (SetupTaskContainer)theEObject;
        T result = caseSetupTaskContainer(setupTaskContainer);
        if (result == null)
        {
          result = caseModelElement(setupTaskContainer);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.SCOPE:
      {
        Scope scope = (Scope)theEObject;
        T result = caseScope(scope);
        if (result == null)
        {
          result = caseSetupTaskContainer(scope);
        }
        if (result == null)
        {
          result = caseModelElement(scope);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.INDEX:
      {
        Index index = (Index)theEObject;
        T result = caseIndex(index);
        if (result == null)
        {
          result = caseModelElement(index);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.CATALOG_SELECTION:
      {
        CatalogSelection catalogSelection = (CatalogSelection)theEObject;
        T result = caseCatalogSelection(catalogSelection);
        if (result == null)
        {
          result = caseModelElement(catalogSelection);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.PRODUCT_CATALOG:
      {
        ProductCatalog productCatalog = (ProductCatalog)theEObject;
        T result = caseProductCatalog(productCatalog);
        if (result == null)
        {
          result = caseScope(productCatalog);
        }
        if (result == null)
        {
          result = caseSetupTaskContainer(productCatalog);
        }
        if (result == null)
        {
          result = caseModelElement(productCatalog);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.PRODUCT:
      {
        Product product = (Product)theEObject;
        T result = caseProduct(product);
        if (result == null)
        {
          result = caseScope(product);
        }
        if (result == null)
        {
          result = caseSetupTaskContainer(product);
        }
        if (result == null)
        {
          result = caseModelElement(product);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.PRODUCT_VERSION:
      {
        ProductVersion productVersion = (ProductVersion)theEObject;
        T result = caseProductVersion(productVersion);
        if (result == null)
        {
          result = caseScope(productVersion);
        }
        if (result == null)
        {
          result = caseSetupTaskContainer(productVersion);
        }
        if (result == null)
        {
          result = caseModelElement(productVersion);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.PROJECT_CONTAINER:
      {
        ProjectContainer projectContainer = (ProjectContainer)theEObject;
        T result = caseProjectContainer(projectContainer);
        if (result == null)
        {
          result = caseScope(projectContainer);
        }
        if (result == null)
        {
          result = caseSetupTaskContainer(projectContainer);
        }
        if (result == null)
        {
          result = caseModelElement(projectContainer);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.PROJECT_CATALOG:
      {
        ProjectCatalog projectCatalog = (ProjectCatalog)theEObject;
        T result = caseProjectCatalog(projectCatalog);
        if (result == null)
        {
          result = caseProjectContainer(projectCatalog);
        }
        if (result == null)
        {
          result = caseScope(projectCatalog);
        }
        if (result == null)
        {
          result = caseSetupTaskContainer(projectCatalog);
        }
        if (result == null)
        {
          result = caseModelElement(projectCatalog);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.PROJECT:
      {
        Project project = (Project)theEObject;
        T result = caseProject(project);
        if (result == null)
        {
          result = caseProjectContainer(project);
        }
        if (result == null)
        {
          result = caseScope(project);
        }
        if (result == null)
        {
          result = caseSetupTaskContainer(project);
        }
        if (result == null)
        {
          result = caseModelElement(project);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.STREAM:
      {
        Stream stream = (Stream)theEObject;
        T result = caseStream(stream);
        if (result == null)
        {
          result = caseScope(stream);
        }
        if (result == null)
        {
          result = caseSetupTaskContainer(stream);
        }
        if (result == null)
        {
          result = caseModelElement(stream);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.USER:
      {
        User user = (User)theEObject;
        T result = caseUser(user);
        if (result == null)
        {
          result = caseScope(user);
        }
        if (result == null)
        {
          result = caseSetupTaskContainer(user);
        }
        if (result == null)
        {
          result = caseModelElement(user);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.ATTRIBUTE_RULE:
      {
        AttributeRule attributeRule = (AttributeRule)theEObject;
        T result = caseAttributeRule(attributeRule);
        if (result == null)
        {
          result = caseModelElement(attributeRule);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.LOCATION_CATALOG:
      {
        LocationCatalog locationCatalog = (LocationCatalog)theEObject;
        T result = caseLocationCatalog(locationCatalog);
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.INSTALLATION:
      {
        Installation installation = (Installation)theEObject;
        T result = caseInstallation(installation);
        if (result == null)
        {
          result = caseScope(installation);
        }
        if (result == null)
        {
          result = caseSetupTaskContainer(installation);
        }
        if (result == null)
        {
          result = caseModelElement(installation);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.INSTALLATION_TASK:
      {
        InstallationTask installationTask = (InstallationTask)theEObject;
        T result = caseInstallationTask(installationTask);
        if (result == null)
        {
          result = caseSetupTask(installationTask);
        }
        if (result == null)
        {
          result = caseModelElement(installationTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.WORKSPACE:
      {
        Workspace workspace = (Workspace)theEObject;
        T result = caseWorkspace(workspace);
        if (result == null)
        {
          result = caseScope(workspace);
        }
        if (result == null)
        {
          result = caseSetupTaskContainer(workspace);
        }
        if (result == null)
        {
          result = caseModelElement(workspace);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.WORKSPACE_TASK:
      {
        WorkspaceTask workspaceTask = (WorkspaceTask)theEObject;
        T result = caseWorkspaceTask(workspaceTask);
        if (result == null)
        {
          result = caseSetupTask(workspaceTask);
        }
        if (result == null)
        {
          result = caseModelElement(workspaceTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.CONFIGURATION:
      {
        Configuration configuration = (Configuration)theEObject;
        T result = caseConfiguration(configuration);
        if (result == null)
        {
          result = caseModelElement(configuration);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.COMPOUND_TASK:
      {
        CompoundTask compoundTask = (CompoundTask)theEObject;
        T result = caseCompoundTask(compoundTask);
        if (result == null)
        {
          result = caseSetupTask(compoundTask);
        }
        if (result == null)
        {
          result = caseSetupTaskContainer(compoundTask);
        }
        if (result == null)
        {
          result = caseModelElement(compoundTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.VARIABLE_TASK:
      {
        VariableTask variableTask = (VariableTask)theEObject;
        T result = caseVariableTask(variableTask);
        if (result == null)
        {
          result = caseSetupTask(variableTask);
        }
        if (result == null)
        {
          result = caseModelElement(variableTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.VARIABLE_CHOICE:
      {
        VariableChoice variableChoice = (VariableChoice)theEObject;
        T result = caseVariableChoice(variableChoice);
        if (result == null)
        {
          result = caseModelElement(variableChoice);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.STRING_SUBSTITUTION_TASK:
      {
        StringSubstitutionTask stringSubstitutionTask = (StringSubstitutionTask)theEObject;
        T result = caseStringSubstitutionTask(stringSubstitutionTask);
        if (result == null)
        {
          result = caseSetupTask(stringSubstitutionTask);
        }
        if (result == null)
        {
          result = caseModelElement(stringSubstitutionTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.REDIRECTION_TASK:
      {
        RedirectionTask redirectionTask = (RedirectionTask)theEObject;
        T result = caseRedirectionTask(redirectionTask);
        if (result == null)
        {
          result = caseSetupTask(redirectionTask);
        }
        if (result == null)
        {
          result = caseModelElement(redirectionTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.ECLIPSE_INI_TASK:
      {
        EclipseIniTask eclipseIniTask = (EclipseIniTask)theEObject;
        T result = caseEclipseIniTask(eclipseIniTask);
        if (result == null)
        {
          result = caseSetupTask(eclipseIniTask);
        }
        if (result == null)
        {
          result = caseModelElement(eclipseIniTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.LINK_LOCATION_TASK:
      {
        LinkLocationTask linkLocationTask = (LinkLocationTask)theEObject;
        T result = caseLinkLocationTask(linkLocationTask);
        if (result == null)
        {
          result = caseSetupTask(linkLocationTask);
        }
        if (result == null)
        {
          result = caseModelElement(linkLocationTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.PREFERENCE_TASK:
      {
        PreferenceTask preferenceTask = (PreferenceTask)theEObject;
        T result = casePreferenceTask(preferenceTask);
        if (result == null)
        {
          result = caseSetupTask(preferenceTask);
        }
        if (result == null)
        {
          result = caseModelElement(preferenceTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.RESOURCE_COPY_TASK:
      {
        ResourceCopyTask resourceCopyTask = (ResourceCopyTask)theEObject;
        T result = caseResourceCopyTask(resourceCopyTask);
        if (result == null)
        {
          result = caseSetupTask(resourceCopyTask);
        }
        if (result == null)
        {
          result = caseModelElement(resourceCopyTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.RESOURCE_CREATION_TASK:
      {
        ResourceCreationTask resourceCreationTask = (ResourceCreationTask)theEObject;
        T result = caseResourceCreationTask(resourceCreationTask);
        if (result == null)
        {
          result = caseSetupTask(resourceCreationTask);
        }
        if (result == null)
        {
          result = caseModelElement(resourceCreationTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.TEXT_MODIFY_TASK:
      {
        TextModifyTask textModifyTask = (TextModifyTask)theEObject;
        T result = caseTextModifyTask(textModifyTask);
        if (result == null)
        {
          result = caseSetupTask(textModifyTask);
        }
        if (result == null)
        {
          result = caseModelElement(textModifyTask);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.TEXT_MODIFICATION:
      {
        TextModification textModification = (TextModification)theEObject;
        T result = caseTextModification(textModification);
        if (result == null)
        {
          result = caseModelElement(textModification);
        }
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY:
      {
        @SuppressWarnings("unchecked")
        Map.Entry<Product, ProductVersion> productToProductVersionMapEntry = (Map.Entry<Product, ProductVersion>)theEObject;
        T result = caseProductToProductVersionMapEntry(productToProductVersionMapEntry);
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.PROJECT_TO_STREAM_MAP_ENTRY:
      {
        @SuppressWarnings("unchecked")
        Map.Entry<Project, Stream> projectToStreamMapEntry = (Map.Entry<Project, Stream>)theEObject;
        T result = caseProjectToStreamMapEntry(projectToStreamMapEntry);
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY:
      {
        @SuppressWarnings("unchecked")
        Map.Entry<Installation, EList<Workspace>> installationToWorkspacesMapEntry = (Map.Entry<Installation, EList<Workspace>>)theEObject;
        T result = caseInstallationToWorkspacesMapEntry(installationToWorkspacesMapEntry);
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      case SetupPackage.WORKSPACE_TO_INSTALLATIONS_MAP_ENTRY:
      {
        @SuppressWarnings("unchecked")
        Map.Entry<Workspace, EList<Installation>> workspaceToInstallationsMapEntry = (Map.Entry<Workspace, EList<Installation>>)theEObject;
        T result = caseWorkspaceToInstallationsMapEntry(workspaceToInstallationsMapEntry);
        if (result == null)
        {
          result = defaultCase(theEObject);
        }
        return result;
      }
      default:
        return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Index</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Index</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseIndex(Index object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Catalog Selection</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Catalog Selection</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCatalogSelection(CatalogSelection object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Workspace</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Workspace</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseWorkspace(Workspace object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Link Location Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Link Location Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLinkLocationTask(LinkLocationTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Preference Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Preference Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T casePreferenceTask(PreferenceTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Task Container</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Task Container</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSetupTaskContainer(SetupTaskContainer object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Scope</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Scope</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseScope(Scope object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Project</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Project</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProject(Project object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Stream</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Stream</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStream(Stream object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Installation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Installation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInstallation(Installation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Product Catalog</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Product Catalog</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProductCatalog(ProductCatalog object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Product</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Product</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProduct(Product object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Product Version</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Product Version</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProductVersion(ProductVersion object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Project Catalog</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Project Catalog</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProjectCatalog(ProjectCatalog object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Installation Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Installation Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInstallationTask(InstallationTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Workspace Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Workspace Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseWorkspaceTask(WorkspaceTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Configuration</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Configuration</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseConfiguration(Configuration object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Compound Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Compound Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseCompoundTask(CompoundTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Variable Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Variable Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVariableTask(VariableTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseSetupTask(SetupTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Resource Copy Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Resource Copy Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseResourceCopyTask(ResourceCopyTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Text Modify Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Text Modify Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTextModifyTask(TextModifyTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Text Modification</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Text Modification</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTextModification(TextModification object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Product To Product Version Map Entry</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Product To Product Version Map Entry</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProductToProductVersionMapEntry(Map.Entry<Product, ProductVersion> object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Project To Stream Map Entry</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Project To Stream Map Entry</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProjectToStreamMapEntry(Map.Entry<Project, Stream> object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Installation To Workspaces Map Entry</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Installation To Workspaces Map Entry</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseInstallationToWorkspacesMapEntry(Map.Entry<Installation, EList<Workspace>> object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Workspace To Installations Map Entry</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Workspace To Installations Map Entry</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseWorkspaceToInstallationsMapEntry(Map.Entry<Workspace, EList<Installation>> object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Model Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Model Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseModelElement(ModelElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>String Substitution Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>String Substitution Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseStringSubstitutionTask(StringSubstitutionTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Project Container</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Project Container</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseProjectContainer(ProjectContainer object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Attribute Rule</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Attribute Rule</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseAttributeRule(AttributeRule object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Location Catalog</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Location Catalog</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseLocationCatalog(LocationCatalog object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Redirection Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Redirection Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRedirectionTask(RedirectionTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Variable Choice</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Variable Choice</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseVariableChoice(VariableChoice object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Resource Creation Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Resource Creation Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseResourceCreationTask(ResourceCreationTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>Eclipse Ini Task</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>Eclipse Ini Task</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseEclipseIniTask(EclipseIniTask object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>User</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>User</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseUser(User object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} // SetupSwitch
