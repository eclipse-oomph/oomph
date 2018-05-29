/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.projectconfig.util;

import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.projectconfig.ExclusionPredicate;
import org.eclipse.oomph.projectconfig.InclusionPredicate;
import org.eclipse.oomph.projectconfig.PreferenceFilter;
import org.eclipse.oomph.projectconfig.PreferenceProfile;
import org.eclipse.oomph.projectconfig.Project;
import org.eclipse.oomph.projectconfig.ProjectConfigPackage;
import org.eclipse.oomph.projectconfig.WorkspaceConfiguration;
import org.eclipse.oomph.projectconfig.impl.ProjectConfigPlugin;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.projectconfig.ProjectConfigPackage
 * @generated
 */
public class ProjectConfigValidator extends EObjectValidator
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final ProjectConfigValidator INSTANCE = new ProjectConfigValidator();

  /**
   * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.common.util.Diagnostic#getSource()
   * @see org.eclipse.emf.common.util.Diagnostic#getCode()
   * @generated
   */
  public static final String DIAGNOSTIC_SOURCE = "org.eclipse.oomph.projectconfig";

  /**
   * A constant with a fixed name that can be used as the base value for additional hand written constants.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

  /**
   * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectConfigValidator()
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
    return ProjectConfigPackage.eINSTANCE;
  }

  /**
   * Calls <code>validateXXX</code> for the corresponding classifier of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected boolean validate(int classifierID, Object value, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    switch (classifierID)
    {
      case ProjectConfigPackage.WORKSPACE_CONFIGURATION:
        return validateWorkspaceConfiguration((WorkspaceConfiguration)value, diagnostics, context);
      case ProjectConfigPackage.PROJECT:
        return validateProject((Project)value, diagnostics, context);
      case ProjectConfigPackage.PREFERENCE_PROFILE:
        return validatePreferenceProfile((PreferenceProfile)value, diagnostics, context);
      case ProjectConfigPackage.PREFERENCE_FILTER:
        return validatePreferenceFilter((PreferenceFilter)value, diagnostics, context);
      case ProjectConfigPackage.INCLUSION_PREDICATE:
        return validateInclusionPredicate((InclusionPredicate)value, diagnostics, context);
      case ProjectConfigPackage.EXCLUSION_PREDICATE:
        return validateExclusionPredicate((ExclusionPredicate)value, diagnostics, context);
      case ProjectConfigPackage.PATTERN:
        return validatePattern((Pattern)value, diagnostics, context);
      default:
        return true;
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateWorkspaceConfiguration(WorkspaceConfiguration workspaceConfiguration, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(workspaceConfiguration, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateProject(Project project, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    if (!validate_NoCircularContainment(project, diagnostics, context))
    {
      return false;
    }
    boolean result = validate_EveryMultiplicityConforms(project, diagnostics, context);
    if (result || diagnostics != null)
    {
      result &= validate_EveryDataValueConforms(project, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryReferenceIsContained(project, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryBidirectionalReferenceIsPaired(project, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryProxyResolves(project, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_UniqueID(project, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryKeyUnique(project, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryMapEntryUnique(project, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validateProject_AllPreferencesManaged(project, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validateProject_PreferenceProfileReferencesSpecifyUniqueProperties(project, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validateProject_AllPropertiesHaveManagedValue(project, diagnostics, context);
    }
    return result;
  }

  static Map<PreferenceNode, Set<Property>> collectUnmanagedPreferences(Project project)
  {
    Map<PreferenceNode, Set<Property>> result = new LinkedHashMap<PreferenceNode, Set<Property>>();
    PreferenceNode projectPreferenceNode = project.getPreferenceNode();
    if (projectPreferenceNode != null)
    {
      collectPreferenceNodes(project.getConfiguration(), result, projectPreferenceNode.getChildren());

      result.remove(projectPreferenceNode.getNode(ProjectConfigUtil.PROJECT_CONF_NODE_NAME));

      for (PreferenceProfile preferenceProfile : project.getPreferenceProfiles())
      {
        for (PreferenceFilter preferenceFilter : preferenceProfile.getPreferenceFilters())
        {
          PreferenceNode preferenceNode = preferenceFilter.getPreferenceNode();
          if (preferenceNode != null)
          {
            Set<Property> properties = result.get(preferenceNode);
            if (properties != null)
            {
              for (Iterator<Property> it = properties.iterator(); it.hasNext();)
              {
                if (preferenceFilter.matches(it.next().getName()))
                {
                  it.remove();
                }
              }

              if (properties.isEmpty())
              {
                result.remove(preferenceNode);
              }
            }
          }
        }
      }

      for (PreferenceProfile preferenceProfile : project.getPreferenceProfileReferences())
      {
        PreferenceNode otherProjectPreferenceNode = preferenceProfile.getProject().getPreferenceNode();
        for (PreferenceFilter preferenceFilter : preferenceProfile.getPreferenceFilters())
        {
          final PreferenceNode referencedPreferenceNode = preferenceFilter.getPreferenceNode();
          if (referencedPreferenceNode != null)
          {
            Set<Property> properties = null;
            PreferenceNode preferenceNode = null;

            LOOP: for (Map.Entry<PreferenceNode, Set<Property>> entry : result.entrySet())
            {
              for (PreferenceNode targetPreferenceNode = entry.getKey(), otherPreferenceNode = referencedPreferenceNode; targetPreferenceNode.getName()
                  .equals(otherPreferenceNode.getName()) && targetPreferenceNode != projectPreferenceNode && otherPreferenceNode != otherProjectPreferenceNode;)
              {
                targetPreferenceNode = targetPreferenceNode.getParent();
                otherPreferenceNode = otherPreferenceNode.getParent();

                if (targetPreferenceNode == projectPreferenceNode && otherPreferenceNode == otherProjectPreferenceNode)
                {
                  preferenceNode = entry.getKey();
                  properties = entry.getValue();
                  break LOOP;
                }
              }
            }

            if (properties != null)
            {
              for (Iterator<Property> it = properties.iterator(); it.hasNext();)
              {
                String name = it.next().getName();
                if (preferenceFilter.matches(name) && referencedPreferenceNode.getProperty(name) != null)
                {
                  it.remove();
                }
              }

              if (properties.isEmpty())
              {
                result.remove(preferenceNode);
              }
            }
          }
        }
      }
    }

    return result;
  }

  private static void collectPreferenceNodes(WorkspaceConfiguration workspaceConfiguration, Map<PreferenceNode, Set<Property>> result,
      List<PreferenceNode> preferenceNodes)
  {
    for (PreferenceNode child : preferenceNodes)
    {
      EList<Property> properties = child.getProperties();
      if (!properties.isEmpty())
      {
        Set<Property> propertySet = new LinkedHashSet<Property>(properties);
        result.put(child, propertySet);
      }

      collectPreferenceNodes(workspaceConfiguration, result, child.getChildren());
    }
  }

  private static String getObjectLabel(Collection<? extends EObject> eObjects, Map<Object, Object> context)
  {
    StringBuilder result = new StringBuilder();
    List<EObject> values = new ArrayList<EObject>(eObjects);
    for (int i = 0, size = values.size(); i < size; ++i)
    {
      if (i == size - 1 && size > 1)
      {
        result.append(" and ");
      }
      else if (i != 0)
      {
        result.append(", ");
      }
      result.append(getObjectLabel(values.get(i), context));
    }
    return result.toString();
  }

  /**
   * Validates the AllPreferencesManaged constraint of '<em>Project</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean validateProject_AllPreferencesManaged(Project project, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    Map<PreferenceNode, Set<Property>> unmanagedPreferences = collectUnmanagedPreferences(project);

    if (!unmanagedPreferences.isEmpty())
    {
      if (diagnostics != null)
      {
        PreferenceNode projectPreferenceNode = project.getPreferenceNode();
        StringBuilder substitution = new StringBuilder();
        List<PreferenceNode> preferenceNodes = new ArrayList<PreferenceNode>(unmanagedPreferences.keySet());
        for (int i = 0, size = preferenceNodes.size(); i < size; ++i)
        {
          if (i == size - 1 && size > 1)
          {
            substitution.append(" and ");
          }
          else if (i != 0)
          {
            substitution.append(", ");
          }

          int index = substitution.length();
          int count = 0;
          for (PreferenceNode preferenceNode = preferenceNodes.get(i); preferenceNode != projectPreferenceNode; preferenceNode = preferenceNode.getParent())
          {
            if (count++ > 0)
            {
              substitution.insert(index, "/");
            }

            substitution.insert(index, getObjectLabel(preferenceNode, context));
          }
        }

        List<Object> data = new ArrayList<Object>();
        data.add(project);
        data.add(ProjectConfigPackage.Literals.PROJECT__PREFERENCE_PROFILE_REFERENCES);
        data.addAll(preferenceNodes);
        diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0, "_UI_AllPreferencesManaged_diagnostic", new Object[] { substitution },
            data.toArray(), context));

        for (Map.Entry<PreferenceNode, Set<Property>> entry : unmanagedPreferences.entrySet())
        {
          for (Property property : entry.getValue())
          {
            diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0, "_UI_UnmanagedProperty_diagnostic",
                new Object[] { getObjectLabel(property, context) }, new Object[] { property, project }, context));

          }
        }
      }
      return false;
    }

    return true;
  }

  private static final Object OVERLAPPING_PROFILES_KEY = new Object();

  // private static Map<PreferenceProfile, Map<PreferenceProfile, Set<Property>>> addOverlap(Map<Object, Object>
  // context, PreferenceProfile preferenceProfile, PreferenceProfile overlappingPreferencProfile, Property property)
  // {
  //
  // }
  //
  private static final Map<PreferenceProfile, Map<PreferenceProfile, Set<Property>>> getOverlaps(Map<Object, Object> context, boolean demandCreate)
  {
    @SuppressWarnings("unchecked")
    Map<PreferenceProfile, Map<PreferenceProfile, Set<Property>>> overlappingProfiles = (Map<PreferenceProfile, Map<PreferenceProfile, Set<Property>>>)context
        .get(OVERLAPPING_PROFILES_KEY);
    if (overlappingProfiles == null)
    {
      overlappingProfiles = new HashMap<PreferenceProfile, Map<PreferenceProfile, Set<Property>>>();
      context.put(OVERLAPPING_PROFILES_KEY, overlappingProfiles);
    }
    return overlappingProfiles;

  }

  /**
   * Validates the PreferenceProfileReferencesSpecifyUniqueProperties constraint of '<em>Project</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean validateProject_PreferenceProfileReferencesSpecifyUniqueProperties(Project project, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    try
    {
      Map<URI, Set<Property>> keyToPropertiesMap = new HashMap<URI, Set<Property>>();
      Map<Property, Set<PreferenceProfile>> propertyToPreferenceProfileMap = new HashMap<Property, Set<PreferenceProfile>>();
      Map<PreferenceProfile, Set<Property>> preferenceProfileToPropertyMap = new LinkedHashMap<PreferenceProfile, Set<Property>>();
      Set<PreferenceProfile> preferenceProfileReferences = new LinkedHashSet<PreferenceProfile>(project.getPreferenceProfileReferences());
      preferenceProfileReferences.addAll(project.getPreferenceProfiles());
      for (PreferenceProfile preferenceProfile : preferenceProfileReferences)
      {
        Set<Property> preferenceProfileProperties = preferenceProfileToPropertyMap.get(preferenceProfile);
        if (preferenceProfileProperties == null)
        {
          preferenceProfileProperties = new LinkedHashSet<Property>();
          preferenceProfileToPropertyMap.put(preferenceProfile, preferenceProfileProperties);
        }

        for (PreferenceFilter preferenceFilter : preferenceProfile.getPreferenceFilters())
        {
          for (Property property : preferenceFilter.getProperties())
          {
            preferenceProfileProperties.add(property);

            Set<PreferenceProfile> preferenceProfiles = propertyToPreferenceProfileMap.get(property);
            if (preferenceProfiles == null)
            {
              preferenceProfiles = new LinkedHashSet<PreferenceProfile>();
              propertyToPreferenceProfileMap.put(property, preferenceProfiles);
            }

            preferenceProfiles.add(preferenceProfile);

            URI relativePath = property.getRelativePath();
            Set<Property> properties = keyToPropertiesMap.get(relativePath);
            if (properties == null)
            {
              properties = new HashSet<Property>();
              keyToPropertiesMap.put(relativePath, properties);
            }

            properties.add(property);
          }
        }
      }

      for (Iterator<Map.Entry<URI, Set<Property>>> it = keyToPropertiesMap.entrySet().iterator(); it.hasNext();)
      {
        Set<Property> properties = it.next().getValue();
        if (properties.size() <= 1)
        {
          it.remove();
        }
        else
        {
          Set<PreferenceProfile> preferencesProfiles = new LinkedHashSet<PreferenceProfile>();
          for (Property property : properties)
          {
            preferencesProfiles.addAll(propertyToPreferenceProfileMap.get(property));
          }

          Map<PreferenceProfile, Map<PreferenceProfile, Set<Property>>> overlaps = getOverlaps(context, true);
          for (PreferenceProfile preferenceProfile : preferencesProfiles)
          {
            Map<PreferenceProfile, Set<Property>> profileProperties = overlaps.get(preferenceProfile);
            if (profileProperties == null)
            {
              profileProperties = new LinkedHashMap<PreferenceProfile, Set<Property>>();
              overlaps.put(preferenceProfile, profileProperties);
            }

            for (PreferenceProfile otherPreferenceProfile : preferencesProfiles)
            {
              if (otherPreferenceProfile != preferenceProfile)
              {
                Set<Property> otherProperties = profileProperties.get(otherPreferenceProfile);
                if (otherProperties == null)
                {
                  otherProperties = new LinkedHashSet<Property>();
                  profileProperties.put(otherPreferenceProfile, otherProperties);
                }

                otherProperties.addAll(properties);
                otherProperties.retainAll(preferenceProfileToPropertyMap.get(otherPreferenceProfile));
              }
            }
          }

          for (Property property : properties)
          {
            LinkedHashSet<Property> otherProperties = new LinkedHashSet<Property>(properties);
            otherProperties.remove(property);
            List<Object> data = new ArrayList<Object>(otherProperties);

            data.add(0, property);
            data.add(1, project);
            data.addAll(2, preferencesProfiles);

            diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE,
                0, "_UI_NonUniquePropertyApplication_diagnostic", new Object[] { getObjectLabel(property, context),
                    getObjectLabel(preferencesProfiles, context), getObjectLabel(project, context), getObjectLabel(otherProperties, context) },
                data.toArray(), context));
          }
        }
      }

      return keyToPropertiesMap.isEmpty();
    }
    finally
    {
      EList<Project> projects = project.getConfiguration().getProjects();
      if (projects.indexOf(project) == projects.size() - 1)
      {
        Map<PreferenceProfile, Map<PreferenceProfile, Set<Property>>> overlaps = getOverlaps(context, false);
        if (overlaps != null)
        {
          for (Entry<PreferenceProfile, Map<PreferenceProfile, Set<Property>>> entry : overlaps.entrySet())
          {
            PreferenceProfile key = entry.getKey();
            Map<PreferenceProfile, Set<Property>> value = entry.getValue();
            Set<Object> data = new LinkedHashSet<Object>();
            data.add(key);
            data.addAll(value.keySet());
            BasicDiagnostic diagnostic = createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0, "_UI_OverlappingPreferenceProfile_diagnostic",
                new Object[] { getObjectLabel(key, context), getObjectLabel(value.keySet(), context) }, data.toArray(), context);

            List<Project> referentProjects = new ArrayList<Project>(key.getReferentProjects());
            referentProjects.add(key.getProject());
            for (Map.Entry<PreferenceProfile, Set<Property>> propertyEntry : value.entrySet())
            {
              Set<Object> data2 = new LinkedHashSet<Object>();
              data2.add(key);
              PreferenceProfile otherPreferenceProfile = propertyEntry.getKey();
              Set<Project> otherReferentProjects = new LinkedHashSet<Project>(otherPreferenceProfile.getReferentProjects());
              otherReferentProjects.add(otherPreferenceProfile.getProject());
              otherReferentProjects.retainAll(referentProjects);
              data2.add(otherPreferenceProfile);
              data2.addAll(otherReferentProjects);
              data2.addAll(propertyEntry.getValue());
              BasicDiagnostic diagnostic2 = createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0, "_UI_OverlappingPreferenceProfileProperty_diagnostic",
                  new Object[] { getObjectLabel(otherPreferenceProfile, context), getObjectLabel(otherReferentProjects, context) }, data2.toArray(), context);
              diagnostic.add(diagnostic2);
            }

            diagnostics.add(diagnostic);
          }
        }
      }
    }
  }

  static Map<PreferenceNode, Map<Property, Property>> collectInconsistentPreferences(Project project)
  {
    Map<PreferenceNode, Map<Property, Property>> result = new LinkedHashMap<PreferenceNode, Map<Property, Property>>();
    collectInconsistentPreferences(result, project.getConfiguration(), project, project.getPreferenceNode());
    return result;
  }

  static void collectInconsistentPreferences(Map<PreferenceNode, Map<Property, Property>> result, WorkspaceConfiguration workspaceConfiguration,
      Project project, PreferenceNode preferenceNode)
  {
    Map<Property, Property> propertyMap = null;
    for (Property property : preferenceNode.getProperties())
    {
      String value = property.getValue();
      Property managingProperty = project.getProperty(property.getRelativePath());
      if (managingProperty != null && managingProperty != property)
      {
        String managingValue = managingProperty.getValue();
        if (value == null ? managingValue != null : !value.equals(managingValue))
        {
          if (propertyMap == null)
          {
            propertyMap = new LinkedHashMap<Property, Property>();
            result.put(preferenceNode, propertyMap);
          }

          propertyMap.put(property, managingProperty);
        }
      }
    }

    for (PreferenceNode childPreferenceNode : preferenceNode.getChildren())
    {
      collectInconsistentPreferences(result, workspaceConfiguration, project, childPreferenceNode);
    }
  }

  /**
   * Validates the AllPropertiesHaveManagedValue constraint of '<em>Project</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean validateProject_AllPropertiesHaveManagedValue(Project project, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    Map<PreferenceNode, Map<Property, Property>> inconsistentPreferences = collectInconsistentPreferences(project);
    if (!inconsistentPreferences.isEmpty())
    {
      for (Map.Entry<PreferenceNode, Map<Property, Property>> entry : inconsistentPreferences.entrySet())
      {
        for (Map.Entry<Property, Property> propertyEntry : entry.getValue().entrySet())
        {
          Property managedProperty = propertyEntry.getKey();
          Property managingProperty = propertyEntry.getValue();

          diagnostics.add(createDiagnostic(Diagnostic.WARNING, DIAGNOSTIC_SOURCE, 0, "_UI_InconsistentPropertyValue_diagnostic",
              new Object[] { getObjectLabel(managedProperty, context), PreferencesFactory.eINSTANCE.createEscapedString(managedProperty.getValue()),
                  getObjectLabel(managingProperty, context), PreferencesFactory.eINSTANCE.createEscapedString(managingProperty.getValue()) },
              new Object[] { managedProperty, managingProperty }, context));

          diagnostics.add(createDiagnostic(Diagnostic.WARNING, DIAGNOSTIC_SOURCE, 0, "_UI_InconsistentPropertyValue_diagnostic",
              new Object[] { getObjectLabel(managedProperty, context), PreferencesFactory.eINSTANCE.createEscapedString(managedProperty.getValue()),
                  getObjectLabel(managingProperty, context), PreferencesFactory.eINSTANCE.createEscapedString(managingProperty.getValue()) },
              new Object[] { project, managedProperty, managingProperty }, context));
        }
      }

      return false;
    }

    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validatePreferenceProfile(PreferenceProfile preferenceProfile, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(preferenceProfile, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validatePreferenceFilter(PreferenceFilter preferenceFilter, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(preferenceFilter, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateInclusionPredicate(InclusionPredicate inclusionPredicate, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(inclusionPredicate, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateExclusionPredicate(ExclusionPredicate exclusionPredicate, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(exclusionPredicate, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validatePattern(Pattern pattern, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
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
    return ProjectConfigPlugin.INSTANCE;
  }

} // ProjectConfigValidator
