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
package org.eclipse.oomph.preferences.util;

import org.eclipse.oomph.preferences.PreferenceItem;
import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesPackage;
import org.eclipse.oomph.preferences.Property;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.preferences.PreferencesPackage
 * @generated
 */
public class PreferencesValidator extends EObjectValidator
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final PreferencesValidator INSTANCE = new PreferencesValidator();

  /**
   * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.common.util.Diagnostic#getSource()
   * @see org.eclipse.emf.common.util.Diagnostic#getCode()
   * @generated
   */
  public static final String DIAGNOSTIC_SOURCE = "org.eclipse.oomph.preferences";

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
  public PreferencesValidator()
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
    return PreferencesPackage.eINSTANCE;
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
      case PreferencesPackage.PREFERENCE_ITEM:
        return validatePreferenceItem((PreferenceItem)value, diagnostics, context);
      case PreferencesPackage.PREFERENCE_NODE:
        return validatePreferenceNode((PreferenceNode)value, diagnostics, context);
      case PreferencesPackage.PROPERTY:
        return validateProperty((Property)value, diagnostics, context);
      case PreferencesPackage.ESCAPED_STRING:
        return validateEscapedString((String)value, diagnostics, context);
      case PreferencesPackage.URI:
        return validateURI((URI)value, diagnostics, context);
      case PreferencesPackage.PREFERENCE_NODE_NAME:
        return validatePreferenceNodeName((String)value, diagnostics, context);
      default:
        return true;
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validatePreferenceItem(PreferenceItem preferenceItem, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(preferenceItem, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validatePreferenceNode(PreferenceNode preferenceNode, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(preferenceNode, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateProperty(Property property, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(property, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateEscapedString(String escapedString, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateURI(URI uri, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validatePreferenceNodeName(String preferenceNodeName, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    boolean result = validatePreferenceNodeName_Pattern(preferenceNodeName, diagnostics, context);
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @see #validatePreferenceNodeName_Pattern
   */
  public static final PatternMatcher[][] PREFERENCE_NODE_NAME__PATTERN__VALUES = new PatternMatcher[][] {
      new PatternMatcher[] { XMLTypeUtil.createPatternMatcher("[^/]+") } };

  /**
   * Validates the Pattern constraint of '<em>Preference Node Name</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validatePreferenceNodeName_Pattern(String preferenceNodeName, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validatePattern(PreferencesPackage.Literals.PREFERENCE_NODE_NAME, preferenceNodeName, PREFERENCE_NODE_NAME__PATTERN__VALUES, diagnostics, context);
  }

  @Override
  protected boolean validate_DataValueConforms(EObject eObject, EAttribute eAttribute, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    if (eAttribute == PreferencesPackage.Literals.PREFERENCE_ITEM__NAME && eObject instanceof PreferenceNode)
    {
      PreferenceNode preferenceNode = (PreferenceNode)eObject;
      String name = preferenceNode.getName();
      if (preferenceNode.getParent() == null && "".equals(name))
      {
        return true;
      }

      boolean result = validate(PreferencesPackage.Literals.PREFERENCE_NODE_NAME, name, null, context);
      if (!result && diagnostics != null)
      {
        DiagnosticChain diagnostic = createBadDataValueDiagnostic(eObject, eAttribute, diagnostics, context);
        validate(PreferencesPackage.Literals.PREFERENCE_NODE_NAME, name, diagnostic, context);
      }

      return result;
    }

    return super.validate_DataValueConforms(eObject, eAttribute, diagnostics, context);
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
    // TODO
    // Specialize this to return a resource locator for messages specific to this validator.
    // Ensure that you remove @generated or mark it @generated NOT
    return super.getResourceLocator();
  }

} // PreferencesValidator
