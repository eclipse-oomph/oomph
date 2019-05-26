/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.util;

import org.eclipse.oomph.internal.setup.SetupPlugin;
import org.eclipse.oomph.setup.Argument;
import org.eclipse.oomph.setup.AttributeRule;
import org.eclipse.oomph.setup.CatalogSelection;
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
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EObjectValidator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.SetupPackage
 * @generated
 */
public class SetupValidator extends EObjectValidator
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final SetupValidator INSTANCE = new SetupValidator();

  /**
   * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.common.util.Diagnostic#getSource()
   * @see org.eclipse.emf.common.util.Diagnostic#getCode()
   * @generated
   */
  public static final String DIAGNOSTIC_SOURCE = "org.eclipse.oomph.setup";

  /**
   * A constant with a fixed name that can be used as the base value for additional hand written constants.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

  /**
   * @see #validateMacroTask_IDRequired(MacroTask, DiagnosticChain, Map)
   */
  public static final int MACRO_TASK_ID_REQUIRED = GENERATED_DIAGNOSTIC_CODE_COUNT + 1;

  /**
   * @see #validateMacroTask_ArgumentsCorrespondToParameters(MacroTask, DiagnosticChain, Map)
   */
  public static final int MACRO_TASK_ARGUMENTS_CORRESPOND_TO_PARAMETERS = GENERATED_DIAGNOSTIC_CODE_COUNT + 2;

  /**
   * @see #validateArgument_ConsistentParameterBinding(Argument, DiagnosticChain, Map)
   */
  public static final int ARGUMENT_CONSISTENT_PARAMETER_BINDING = GENERATED_DIAGNOSTIC_CODE_COUNT + 3;

  /**
   * @see #validateMacro_NoRecursion(Macro, DiagnosticChain, Map)
   */
  public static final int MACRO_NO_RECURSION = GENERATED_DIAGNOSTIC_CODE_COUNT + 4;

  /**
   * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected static final int DIAGNOSTIC_CODE_COUNT = MACRO_NO_RECURSION;

  private static boolean parseFilterMethodInitialized;

  private static Method parseFilterMethod;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupValidator()
  {
    super();
  }

  /**
   * Returns the package of this validator switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EPackage getEPackage()
  {
    return SetupPackage.eINSTANCE;
  }

  /**
   * Calls <code>validateXXX</code> for the corresponding classifier of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  @SuppressWarnings("unchecked")
  protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    switch (classifierID)
    {
      case SetupPackage.SETUP_TASK:
        return validateSetupTask((SetupTask)value, diagnostics, context);
      case SetupPackage.SETUP_TASK_CONTAINER:
        return validateSetupTaskContainer((SetupTaskContainer)value, diagnostics, context);
      case SetupPackage.SCOPE:
        return validateScope((Scope)value, diagnostics, context);
      case SetupPackage.INDEX:
        return validateIndex((Index)value, diagnostics, context);
      case SetupPackage.CATALOG_SELECTION:
        return validateCatalogSelection((CatalogSelection)value, diagnostics, context);
      case SetupPackage.PRODUCT_CATALOG:
        return validateProductCatalog((ProductCatalog)value, diagnostics, context);
      case SetupPackage.PRODUCT:
        return validateProduct((Product)value, diagnostics, context);
      case SetupPackage.PRODUCT_VERSION:
        return validateProductVersion((ProductVersion)value, diagnostics, context);
      case SetupPackage.PROJECT_CONTAINER:
        return validateProjectContainer((ProjectContainer)value, diagnostics, context);
      case SetupPackage.PROJECT_CATALOG:
        return validateProjectCatalog((ProjectCatalog)value, diagnostics, context);
      case SetupPackage.PROJECT:
        return validateProject((Project)value, diagnostics, context);
      case SetupPackage.STREAM:
        return validateStream((Stream)value, diagnostics, context);
      case SetupPackage.USER:
        return validateUser((User)value, diagnostics, context);
      case SetupPackage.ATTRIBUTE_RULE:
        return validateAttributeRule((AttributeRule)value, diagnostics, context);
      case SetupPackage.LOCATION_CATALOG:
        return validateLocationCatalog((LocationCatalog)value, diagnostics, context);
      case SetupPackage.INSTALLATION:
        return validateInstallation((Installation)value, diagnostics, context);
      case SetupPackage.INSTALLATION_TASK:
        return validateInstallationTask((InstallationTask)value, diagnostics, context);
      case SetupPackage.WORKSPACE:
        return validateWorkspace((Workspace)value, diagnostics, context);
      case SetupPackage.WORKSPACE_TASK:
        return validateWorkspaceTask((WorkspaceTask)value, diagnostics, context);
      case SetupPackage.CONFIGURATION:
        return validateConfiguration((Configuration)value, diagnostics, context);
      case SetupPackage.COMPOUND_TASK:
        return validateCompoundTask((CompoundTask)value, diagnostics, context);
      case SetupPackage.VARIABLE_TASK:
        return validateVariableTask((VariableTask)value, diagnostics, context);
      case SetupPackage.VARIABLE_CHOICE:
        return validateVariableChoice((VariableChoice)value, diagnostics, context);
      case SetupPackage.STRING_SUBSTITUTION_TASK:
        return validateStringSubstitutionTask((StringSubstitutionTask)value, diagnostics, context);
      case SetupPackage.REDIRECTION_TASK:
        return validateRedirectionTask((RedirectionTask)value, diagnostics, context);
      case SetupPackage.ECLIPSE_INI_TASK:
        return validateEclipseIniTask((EclipseIniTask)value, diagnostics, context);
      case SetupPackage.LINK_LOCATION_TASK:
        return validateLinkLocationTask((LinkLocationTask)value, diagnostics, context);
      case SetupPackage.PREFERENCE_TASK:
        return validatePreferenceTask((PreferenceTask)value, diagnostics, context);
      case SetupPackage.RESOURCE_COPY_TASK:
        return validateResourceCopyTask((ResourceCopyTask)value, diagnostics, context);
      case SetupPackage.RESOURCE_CREATION_TASK:
        return validateResourceCreationTask((ResourceCreationTask)value, diagnostics, context);
      case SetupPackage.TEXT_MODIFY_TASK:
        return validateTextModifyTask((TextModifyTask)value, diagnostics, context);
      case SetupPackage.TEXT_MODIFICATION:
        return validateTextModification((TextModification)value, diagnostics, context);
      case SetupPackage.PRODUCT_TO_PRODUCT_VERSION_MAP_ENTRY:
        return validateProductToProductVersionMapEntry((Map.Entry<?, ?>)value, diagnostics, context);
      case SetupPackage.PROJECT_TO_STREAM_MAP_ENTRY:
        return validateProjectToStreamMapEntry((Map.Entry<?, ?>)value, diagnostics, context);
      case SetupPackage.INSTALLATION_TO_WORKSPACES_MAP_ENTRY:
        return validateInstallationToWorkspacesMapEntry((Map.Entry<?, ?>)value, diagnostics, context);
      case SetupPackage.WORKSPACE_TO_INSTALLATIONS_MAP_ENTRY:
        return validateWorkspaceToInstallationsMapEntry((Map.Entry<?, ?>)value, diagnostics, context);
      case SetupPackage.MACRO:
        return validateMacro((Macro)value, diagnostics, context);
      case SetupPackage.PARAMETER:
        return validateParameter((Parameter)value, diagnostics, context);
      case SetupPackage.MACRO_TASK:
        return validateMacroTask((MacroTask)value, diagnostics, context);
      case SetupPackage.ARGUMENT:
        return validateArgument((Argument)value, diagnostics, context);
      case SetupPackage.SCOPE_TYPE:
        return validateScopeType((ScopeType)value, diagnostics, context);
      case SetupPackage.TRIGGER:
        return validateTrigger((Trigger)value, diagnostics, context);
      case SetupPackage.VARIABLE_TYPE:
        return validateVariableType((VariableType)value, diagnostics, context);
      case SetupPackage.UNSIGNED_POLICY:
        return validateUnsignedPolicy((UnsignedPolicy)value, diagnostics, context);
      case SetupPackage.TRIGGER_SET:
        return validateTriggerSet((Set<Trigger>)value, diagnostics, context);
      case SetupPackage.LICENSE_INFO:
        return validateLicenseInfo((LicenseInfo)value, diagnostics, context);
      case SetupPackage.FILTER:
        return validateFilter((String)value, diagnostics, context);
      default:
        return true;
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateSetupTask(SetupTask setupTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(setupTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateSetupTaskContainer(SetupTaskContainer setupTaskContainer, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(setupTaskContainer, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateScope(Scope scope, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(scope, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateIndex(Index index, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(index, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateCatalogSelection(CatalogSelection catalogSelection, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(catalogSelection, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateProductCatalog(ProductCatalog productCatalog, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(productCatalog, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateProduct(Product product, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(product, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateProductVersion(ProductVersion productVersion, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(productVersion, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateProjectContainer(ProjectContainer projectContainer, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(projectContainer, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateProjectCatalog(ProjectCatalog projectCatalog, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(projectCatalog, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateProject(Project project, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(project, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateStream(Stream stream, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(stream, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateUser(User user, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(user, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateAttributeRule(AttributeRule attributeRule, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(attributeRule, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateLocationCatalog(LocationCatalog locationCatalog, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(locationCatalog, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateInstallation(Installation installation, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(installation, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateInstallationTask(InstallationTask installationTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(installationTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateWorkspace(Workspace workspace, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(workspace, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateWorkspaceTask(WorkspaceTask workspaceTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(workspaceTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateConfiguration(Configuration configuration, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(configuration, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateCompoundTask(CompoundTask compoundTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(compoundTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateVariableTask(VariableTask variableTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(variableTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateVariableChoice(VariableChoice variableChoice, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(variableChoice, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateStringSubstitutionTask(StringSubstitutionTask stringSubstitutionTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(stringSubstitutionTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateRedirectionTask(RedirectionTask redirectionTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(redirectionTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateEclipseIniTask(EclipseIniTask eclipseIniTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(eclipseIniTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateLinkLocationTask(LinkLocationTask linkLocationTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(linkLocationTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validatePreferenceTask(PreferenceTask preferenceTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(preferenceTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateResourceCopyTask(ResourceCopyTask resourceCopyTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(resourceCopyTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateResourceCreationTask(ResourceCreationTask resourceCreationTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(resourceCreationTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateTextModifyTask(TextModifyTask textModifyTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(textModifyTask, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateTextModification(TextModification textModification, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(textModification, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateProductToProductVersionMapEntry(Map.Entry<?, ?> productToProductVersionMapEntry, DiagnosticChain diagnostics,
      Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint((EObject)productToProductVersionMapEntry, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateProjectToStreamMapEntry(Map.Entry<?, ?> projectToStreamMapEntry, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint((EObject)projectToStreamMapEntry, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateInstallationToWorkspacesMapEntry(Map.Entry<?, ?> installationToWorkspacesMapEntry, DiagnosticChain diagnostics,
      Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint((EObject)installationToWorkspacesMapEntry, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateWorkspaceToInstallationsMapEntry(Map.Entry<?, ?> workspaceToInstallationsMapEntry, DiagnosticChain diagnostics,
      Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint((EObject)workspaceToInstallationsMapEntry, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateMacro(Macro macro, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    if (!validate_NoCircularContainment(macro, diagnostics, context))
    {
      return false;
    }
    boolean result = validate_EveryMultiplicityConforms(macro, diagnostics, context);
    if (result || diagnostics != null)
    {
      result &= validate_EveryDataValueConforms(macro, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryReferenceIsContained(macro, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryBidirectionalReferenceIsPaired(macro, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryProxyResolves(macro, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_UniqueID(macro, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryKeyUnique(macro, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryMapEntryUnique(macro, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validateMacro_NoRecursion(macro, diagnostics, context);
    }
    return result;
  }

  /**
   * Validates the NoRecursion constraint of '<em>Macro</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean validateMacro_NoRecursion(Macro macro, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    Map<Macro, MacroTask> visitedMacros = new LinkedHashMap<Macro, MacroTask>();
    MacroTask fakeRootMacroTask = SetupFactory.eINSTANCE.createMacroTask();
    if (isRecursive(visitedMacros, macro, fakeRootMacroTask))
    {
      if (diagnostics != null)
      {
        List<String> choices = new ArrayList<String>();
        List<Object> data = new ArrayList<Object>();
        data.add(macro);
        data.add(SetupPackage.Literals.SETUP_TASK_CONTAINER__SETUP_TASKS);

        // Gather the chain of objects that lead to the recursion.
        // The fake root macro task will have been replaced with an actual macro task that finally resulted in the recursion.
        MacroTask finalRecursiveMacroTask = null;
        for (Map.Entry<Macro, MacroTask> entry : visitedMacros.entrySet())
        {
          Macro otherMacro = entry.getKey();
          choices.add(getObjectLabel(otherMacro, context));

          MacroTask macroTask = entry.getValue();
          if (finalRecursiveMacroTask == null)
          {
            // The initial replaced macro task should be last in the data list.
            finalRecursiveMacroTask = macroTask;
          }
          else
          {
            data.add(macroTask);
          }

          if (otherMacro != macro)
          {
            data.add(otherMacro);
          }
        }

        data.add(finalRecursiveMacroTask);

        diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, MACRO_NO_RECURSION, "_UI_MacroNoRecursion_diagnostic",
            new Object[] { getAvailableChoices(choices, true, "'", Integer.MAX_VALUE) }, data.toArray(), context));
      }

      return false;
    }

    return true;
  }

  private boolean isRecursive(Map<Macro, MacroTask> visitedMacros, Macro macro, MacroTask macroTask)
  {
    MacroTask put = visitedMacros.put(macro, macroTask);
    if (put == null)
    {
      for (Iterator<EObject> it = macro.eAllContents(); it.hasNext();)
      {
        EObject eObject = it.next();
        if (eObject instanceof MacroTask)
        {
          MacroTask reachableMacroTask = (MacroTask)eObject;
          Macro reachableMacro = reachableMacroTask.getMacro();
          if (reachableMacro != null)
          {
            if (isRecursive(visitedMacros, reachableMacro, reachableMacroTask))
            {
              return true;
            }
          }
        }
      }

      visitedMacros.remove(macro);
      return false;
    }
    else
    {
      return visitedMacros.keySet().iterator().next() == macro;
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateParameter(Parameter parameter, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(parameter, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateMacroTask(MacroTask macroTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    if (!validate_NoCircularContainment(macroTask, diagnostics, context))
    {
      return false;
    }
    boolean result = validate_EveryMultiplicityConforms(macroTask, diagnostics, context);
    if (result || diagnostics != null)
    {
      result &= validate_EveryDataValueConforms(macroTask, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryReferenceIsContained(macroTask, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryBidirectionalReferenceIsPaired(macroTask, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryProxyResolves(macroTask, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_UniqueID(macroTask, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryKeyUnique(macroTask, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryMapEntryUnique(macroTask, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validateMacroTask_IDRequired(macroTask, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validateMacroTask_ArgumentsCorrespondToParameters(macroTask, diagnostics, context);
    }
    return result;
  }

  /**
   * Validates the IDRequired constraint of '<em>Macro Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean validateMacroTask_IDRequired(MacroTask macroTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    if (StringUtil.isEmpty(macroTask.getID()))
    {
      if (diagnostics != null)
      {
        diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, MACRO_TASK_ID_REQUIRED, "_UI_MacroTaskDRequired_diagnostic", null,
            new Object[] { macroTask, SetupPackage.Literals.SETUP_TASK__ID }, context));
      }

      return false;
    }

    return true;
  }

  /**
   * Validates the ArgumentsCorrespondToParameters constraint of '<em>Macro Task</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean validateMacroTask_ArgumentsCorrespondToParameters(MacroTask macroTask, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    Macro macro = macroTask.getMacro();
    if (macro != null && !macro.eIsProxy())
    {
      EList<Parameter> parameters = macro.getParameters();
      EList<Argument> arguments = macroTask.getArguments();
      Set<Parameter> boundParameters = new LinkedHashSet<Parameter>();
      for (Argument argument : arguments)
      {
        boundParameters.add(argument.getParameter());
      }

      Set<String> unboundParameters = new LinkedHashSet<String>();
      for (Parameter parameter : parameters)
      {
        if (!boundParameters.contains(parameter) && parameter.getDefaultValue() == null)
        {
          unboundParameters.add(parameter.getName());
        }
      }

      if (!unboundParameters.isEmpty())
      {
        if (diagnostics != null)
        {
          diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, MACRO_TASK_ARGUMENTS_CORRESPOND_TO_PARAMETERS,
              "_UI_MacroTaskMissingArgument_diagnostic", new Object[] { getAvailableChoices(unboundParameters, true, "'", Integer.MAX_VALUE) },
              new Object[] { macroTask, SetupPackage.Literals.MACRO_TASK__ARGUMENTS }, context));
        }

        return false;
      }
    }

    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateArgument(Argument argument, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    if (!validate_NoCircularContainment(argument, diagnostics, context))
    {
      return false;
    }
    boolean result = validate_EveryMultiplicityConforms(argument, diagnostics, context);
    if (result || diagnostics != null)
    {
      result &= validate_EveryDataValueConforms(argument, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryReferenceIsContained(argument, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryBidirectionalReferenceIsPaired(argument, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryProxyResolves(argument, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_UniqueID(argument, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryKeyUnique(argument, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryMapEntryUnique(argument, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validateArgument_ConsistentParameterBinding(argument, diagnostics, context);
    }
    return result;
  }

  @Override
  protected boolean validate_MultiplicityConforms(EObject eObject, EStructuralFeature eStructuralFeature, DiagnosticChain diagnostics,
      Map<Object, Object> context)
  {
    if (eStructuralFeature == SetupPackage.Literals.ARGUMENT__PARAMETER && ((Argument)eObject).getParameter() != null)
    {
      // Because the parameter, even when present, will be considered not set when it's properly a parameter of the containing macro task's macro,
      // we will get a validation error, because it's a required unsettable feature, and we don't want that.
      return true;
    }

    return super.validate_MultiplicityConforms(eObject, eStructuralFeature, diagnostics, context);
  }

  /**
   * Validates the ConsistentParameterBinding constraint of '<em>Argument</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean validateArgument_ConsistentParameterBinding(Argument argument, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    boolean result = true;
    Parameter parameter = argument.getParameter();
    if (parameter != null && !parameter.eIsProxy())
    {
      if (argument.getValue() == null && parameter.getDefaultValue() == null)
      {
        if (diagnostics != null)
        {
          diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, ARGUMENT_CONSISTENT_PARAMETER_BINDING,
              "_UI_ArgumentValueMustBeSpecified_diagnostic", null, new Object[] { argument, SetupPackage.Literals.ARGUMENT__VALUE }, context));
          result = false;
        }
        else
        {
          return false;
        }
      }

      MacroTask macroTask = argument.getMacroTask();
      if (macroTask != null)
      {
        EList<Argument> arguments = macroTask.getArguments();
        for (int i = 0, index = arguments.indexOf(argument); i < index; ++i)
        {
          if (arguments.get(i).getParameter() == parameter)
          {
            if (diagnostics != null)
            {
              diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, ARGUMENT_CONSISTENT_PARAMETER_BINDING,
                  "_UI_ArgumentParameterAlreadyBound_diagnostic", null, new Object[] { argument, SetupPackage.Literals.ARGUMENT__PARAMETER }, context));
              result = false;
            }
            else
            {
              return false;
            }
          }
        }

        Macro macro = macroTask.getMacro();
        if (macro != null && !macro.eIsProxy())
        {
          EList<Parameter> parameters = macro.getParameters();
          if (!parameters.contains(parameter))
          {
            if (diagnostics != null)
            {
              diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, ARGUMENT_CONSISTENT_PARAMETER_BINDING,
                  "_UI_ArgumentParameterOutOfScope_diagnostic", null, new Object[] { argument, SetupPackage.Literals.ARGUMENT__PARAMETER }, context));
              result = false;
            }
            else
            {
              return false;
            }
          }
        }
      }
    }

    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateScopeType(ScopeType scopeType, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateTrigger(Trigger trigger, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateVariableType(VariableType variableType, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateUnsignedPolicy(UnsignedPolicy unsignedPolicy, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateTriggerSet(Set<Trigger> triggerSet, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateLicenseInfo(LicenseInfo licenseInfo, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateFilter(String filter, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    boolean result = validateFilter_WellformedFilterExpression(filter, diagnostics, context);
    return result;
  }

  /**
   * Validates the WellformedFilterExpression constraint of '<em>Filter</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean validateFilter_WellformedFilterExpression(String filter, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    if (StringUtil.isEmpty(filter))
    {
      return true;
    }

    if (!parseFilterMethodInitialized)
    {
      try
      {
        Class<?> installableUnitClass = CommonPlugin.loadClass("org.eclipse.equinox.p2.metadata", "org.eclipse.equinox.internal.p2.metadata.InstallableUnit");
        parseFilterMethod = installableUnitClass.getMethod("parseFilter", String.class);
      }
      catch (Throwable throwable)
      {
        // If p2 isn't available, we simply won't be able to validate.
      }

      parseFilterMethodInitialized = true;
    }

    if (parseFilterMethod == null)
    {
      // Validation isn't available so assume the value is valid.
      return true;
    }

    Throwable throwable = null;
    try
    {
      parseFilterMethod.invoke(null, filter);
    }
    catch (IllegalAccessException ex)
    {
      // It's a public method, so this can't happen.
    }
    catch (IllegalArgumentException ex)
    {
      // The argument is definitely of type string, so this can't happen.
    }
    catch (InvocationTargetException ex)
    {
      // Record the wrapped exception.
      throwable = ex.getTargetException();
    }

    if (throwable != null)
    {
      if (diagnostics != null)
      {
        diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0, throwable.getLocalizedMessage(), new Object[] { filter }));
      }

      return false;
    }

    return true;
  }

  /**
   * Returns the resource locator that will be used to fetch messages for this validator's diagnostics.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ResourceLocator getResourceLocator()
  {
    return SetupPlugin.INSTANCE;
  }

} // SetupValidator
