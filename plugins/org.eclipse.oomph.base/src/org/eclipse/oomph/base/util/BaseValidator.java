/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.base.util;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.internal.base.BasePlugin;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecore.xml.type.util.XMLTypeUtil;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.base.BasePackage
 * @generated
 */
public class BaseValidator extends EObjectValidator
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final BaseValidator INSTANCE = new BaseValidator();

  /**
   * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.common.util.Diagnostic#getSource()
   * @see org.eclipse.emf.common.util.Diagnostic#getCode()
   * @generated
   */
  public static final String DIAGNOSTIC_SOURCE = "org.eclipse.oomph.base";

  /**
   * A constant with a fixed name that can be used as the base value for additional hand written constants.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final int GENERATED_DIAGNOSTIC_CODE_COUNT = 0;

  /**
   * @see #validateAnnotation_WellFormedSourceURI(Annotation, DiagnosticChain, Map)
   */
  public static final int WELL_FORMED_SOURCE_URI = GENERATED_DIAGNOSTIC_CODE_COUNT + 1;

  /**
   * A constant with a fixed name that can be used as the base value for additional hand written constants in a derived class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected static final int DIAGNOSTIC_CODE_COUNT = GENERATED_DIAGNOSTIC_CODE_COUNT;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BaseValidator()
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
    return BasePackage.eINSTANCE;
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
      case BasePackage.MODEL_ELEMENT:
        return validateModelElement((ModelElement)value, diagnostics, context);
      case BasePackage.ANNOTATION:
        return validateAnnotation((Annotation)value, diagnostics, context);
      case BasePackage.STRING_TO_STRING_MAP_ENTRY:
        return validateStringToStringMapEntry((Map.Entry<?, ?>)value, diagnostics, context);
      case BasePackage.URI:
        return validateURI((URI)value, diagnostics, context);
      case BasePackage.EXCEPTION:
        return validateException((Exception)value, diagnostics, context);
      case BasePackage.TEXT:
        return validateText((String)value, diagnostics, context);
      case BasePackage.ID:
        return validateID((String)value, diagnostics, context);
      default:
        return true;
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateModelElement(ModelElement modelElement, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(modelElement, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateAnnotation(Annotation annotation, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    if (!validate_NoCircularContainment(annotation, diagnostics, context))
    {
      return false;
    }
    boolean result = validate_EveryMultiplicityConforms(annotation, diagnostics, context);
    if (result || diagnostics != null)
    {
      result &= validate_EveryDataValueConforms(annotation, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryReferenceIsContained(annotation, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryBidirectionalReferenceIsPaired(annotation, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryProxyResolves(annotation, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_UniqueID(annotation, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryKeyUnique(annotation, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryMapEntryUnique(annotation, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validateAnnotation_WellFormedSourceURI(annotation, diagnostics, context);
    }
    return result;
  }

  /**
   * Validates the WellFormedSourceURI constraint of '<em>Annotation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean validateAnnotation_WellFormedSourceURI(Annotation annotation, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    String source = annotation.getSource();
    boolean result = source == null || isWellFormedURI(source);
    if (!result && diagnostics != null)
    {
      diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, WELL_FORMED_SOURCE_URI, "_UI_AnnotationSourceURINotWellFormed_diagnostic", //
          new Object[] { source }, new Object[] { annotation, BasePackage.Literals.ANNOTATION__SOURCE }, context));
    }
    return result;
  }

  /**
   * A well formed URI string must have a non-zero length,
   * and must encode any special characters such as the space character.
   * As such, creating a {@link URI#createURI(String, boolean) URI},
   * ignoring the properly encoded characters,
   * and converting that to a {@link URI#toString() string},
   * must yield this URI string itself.
   * @param uri the URI string in question.
   * @return whether the URI is well formed.
   */
  protected static boolean isWellFormedURI(String uri)
  {
    try
    {
      return uri != null && uri.length() != 0 && uri.equals(URI.createURI(uri, true).toString());
    }
    catch (Throwable exception)
    {
      return false;
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateStringToStringMapEntry(Map.Entry<?, ?> stringToStringMapEntry, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint((EObject)stringToStringMapEntry, diagnostics, context);
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
  public boolean validateException(Exception exception, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateText(String text, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateID(String id, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    boolean result = validateID_Pattern(id, diagnostics, context);
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @see #validateID_Pattern
   */
  public static final PatternMatcher[][] ID__PATTERN__VALUES = new PatternMatcher[][] {
      new PatternMatcher[] { XMLTypeUtil.createPatternMatcher("[\\i-[:]][\\c-[:]]*") } };

  /**
   * Validates the Pattern constraint of '<em>ID</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateID_Pattern(String id, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validatePattern(BasePackage.Literals.ID, id, ID__PATTERN__VALUES, diagnostics, context);
  }

  @Override
  protected void reportDataValuePatternViolation(EDataType eDataType, Object value, PatternMatcher[] patterns, DiagnosticChain diagnostics,
      Map<Object, Object> context)
  {
    diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, DATA_VALUE__MATCHES_PATTERN, "_UI_IDConstraint_diagnostic",
        new Object[] { getValueLabel(eDataType, value, context) }, new Object[] { value, eDataType, patterns }, context));
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
    return BasePlugin.INSTANCE;
  }

} // BaseValidator
