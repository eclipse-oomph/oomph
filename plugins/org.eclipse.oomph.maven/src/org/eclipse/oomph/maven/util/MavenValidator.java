/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.util;

import static org.eclipse.oomph.maven.ConstraintType.RESOLVES_IN_REALM;
import static org.eclipse.oomph.maven.ConstraintType.VALID_RELATIVE_PARENT;

import org.eclipse.oomph.maven.ConstraintType;
import org.eclipse.oomph.maven.Coordinate;
import org.eclipse.oomph.maven.DOMElement;
import org.eclipse.oomph.maven.Dependency;
import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.MavenPlugin;
import org.eclipse.oomph.maven.Parent;
import org.eclipse.oomph.maven.Project;
import org.eclipse.oomph.maven.Property;
import org.eclipse.oomph.maven.PropertyReference;
import org.eclipse.oomph.maven.Realm;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EObjectValidator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <!-- begin-user-doc -->
 * The <b>Validator</b> for the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.maven.MavenPackage
 * @generated
 */
public class MavenValidator extends EObjectValidator
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final MavenValidator INSTANCE = new MavenValidator();

  /**
   * A constant for the {@link org.eclipse.emf.common.util.Diagnostic#getSource() source} of diagnostic {@link org.eclipse.emf.common.util.Diagnostic#getCode() codes} from this package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.common.util.Diagnostic#getSource()
   * @see org.eclipse.emf.common.util.Diagnostic#getCode()
   * @generated
   */
  public static final String DIAGNOSTIC_SOURCE = "org.eclipse.oomph.maven"; //$NON-NLS-1$

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
  public MavenValidator()
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
    return MavenPackage.eINSTANCE;
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
      case MavenPackage.REALM:
        return validateRealm((Realm)value, diagnostics, context);
      case MavenPackage.DOM_ELEMENT:
        return validateDOMElement((DOMElement)value, diagnostics, context);
      case MavenPackage.COORDINATE:
        return validateCoordinate((Coordinate)value, diagnostics, context);
      case MavenPackage.PROJECT:
        return validateProject((Project)value, diagnostics, context);
      case MavenPackage.PARENT:
        return validateParent((Parent)value, diagnostics, context);
      case MavenPackage.DEPENDENCY:
        return validateDependency((Dependency)value, diagnostics, context);
      case MavenPackage.PROPERTY:
        return validateProperty((Property)value, diagnostics, context);
      case MavenPackage.PROPERTY_REFERENCE:
        return validatePropertyReference((PropertyReference)value, diagnostics, context);
      case MavenPackage.CONSTRAINT_TYPE:
        return validateConstraintType((ConstraintType)value, diagnostics, context);
      case MavenPackage.DOCUMENT:
        return validateDocument((Document)value, diagnostics, context);
      case MavenPackage.ELEMENT:
        return validateElement((Element)value, diagnostics, context);
      case MavenPackage.ELEMENT_EDIT:
        return validateElementEdit((MavenValidator.ElementEdit)value, diagnostics, context);
      case MavenPackage.TEXT_REGION:
        return validateTextRegion((POMXMLUtil.TextRegion)value, diagnostics, context);
      case MavenPackage.XPATH:
        return validateXPath((SegmentSequence)value, diagnostics, context);
      default:
        return true;
    }
  }

  @Override
  protected boolean validate_MultiplicityConforms(EObject eObject, EStructuralFeature eStructuralFeature, DiagnosticChain diagnostics,
      Map<Object, Object> context)
  {
    if (eStructuralFeature == MavenPackage.Literals.PARENT__RESOLVED_PROJECT)
    {
      Parent parent = (Parent)eObject;
      Project project = parent.getProject();
      if (project != null)
      {
        Realm realm = project.getRealm();
        if (realm != null && realm.getSuppressedConstraints().contains(RESOLVES_IN_REALM))
        {
          return true;
        }
      }
    }
    return super.validate_MultiplicityConforms(eObject, eStructuralFeature, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateRealm(Realm realm, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(realm, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateDOMElement(DOMElement domElement, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(domElement, diagnostics, context);
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
  public boolean validateCoordinate(Coordinate coordinate, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(coordinate, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateParent(Parent parent, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    if (!validate_NoCircularContainment(parent, diagnostics, context))
    {
      return false;
    }
    boolean result = validate_EveryMultiplicityConforms(parent, diagnostics, context);
    if (result || diagnostics != null)
    {
      result &= validate_EveryDataValueConforms(parent, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryReferenceIsContained(parent, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryBidirectionalReferenceIsPaired(parent, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryProxyResolves(parent, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_UniqueID(parent, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryKeyUnique(parent, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryMapEntryUnique(parent, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validateParent_ResolvesInRealm(parent, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validateParent_ValidRelativePath(parent, diagnostics, context);
    }
    return result;
  }

  /**
   * Validates the ResolvesInRealm constraint of '<em>Parent</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean validateParent_ResolvesInRealm(Parent parent, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    Project resolvedProject = parent.getResolvedProject();
    if (resolvedProject != null)
    {
      Realm realm = resolvedProject.getRealm();
      if (realm != null && realm.getSuppressedConstraints().contains(RESOLVES_IN_REALM))
      {
        return true;
      }

      String version = parent.getExpandedVersion();
      String resolvedVersion = resolvedProject.getVersion();

      if (!Objects.equals(version, resolvedVersion))
      {
        if (diagnostics != null)
        {
          diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0, "BadParentVersion_diagnostic", //$NON-NLS-1$
              new Object[] { version, resolvedVersion }, new Object[] { parent, MavenPackage.Literals.COORDINATE__VERSION,
                  new ElementEdit(parent.getElement(POMXMLUtil.xpath(Coordinate.VERSION)), resolvedVersion) },
              context));
        }

        return false;
      }
    }

    return true;
  }

  /**
   * Validates the ValidRelativePath constraint of '<em>Parent</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean validateParent_ValidRelativePath(Parent parent, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    Project resolvedProject = parent.getResolvedProject();
    if (resolvedProject != null)
    {
      Realm realm = resolvedProject.getRealm();
      if (realm != null && realm.getSuppressedConstraints().contains(VALID_RELATIVE_PARENT))
      {
        return true;
      }

      Element relativePathElement = parent.getElement(POMXMLUtil.xpath(Parent.RELATIVE_PATH));
      if (relativePathElement == null)
      {
        Element parentElement = parent.getElement();
        relativePathElement = POMXMLUtil.createChildElement(parentElement, Parent.RELATIVE_PATH);
      }

      String relativePath = parent.getRelativePath();
      Path parentLocation = POMXMLUtil.getLocation(resolvedProject.getElement()).getParent();
      Path projectLocation = POMXMLUtil.getLocation(parent.getProject().getElement()).getParent();
      Path relativePathLocation = projectLocation.relativize(parentLocation);
      String normalizedRelativePath = relativePathLocation.toString().replace('\\', '/');
      if (!normalizedRelativePath.equals(relativePath) && !(normalizedRelativePath + "/").equals(relativePath) //$NON-NLS-1$
          && !(normalizedRelativePath + "/pom.xml").equals(relativePath)) //$NON-NLS-1$
      {
        if (diagnostics != null)
        {
          diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0, "BadParentRelativePath_diagnostic", //$NON-NLS-1$
              new Object[] { relativePath, normalizedRelativePath },
              new Object[] { parent, MavenPackage.Literals.PARENT__RELATIVE_PATH, new ElementEdit(relativePathElement, normalizedRelativePath) }, context));
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
  public boolean validateDependency(Dependency dependency, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    if (!validate_NoCircularContainment(dependency, diagnostics, context))
    {
      return false;
    }
    boolean result = validate_EveryMultiplicityConforms(dependency, diagnostics, context);
    if (result || diagnostics != null)
    {
      result &= validate_EveryDataValueConforms(dependency, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryReferenceIsContained(dependency, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryBidirectionalReferenceIsPaired(dependency, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryProxyResolves(dependency, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_UniqueID(dependency, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryKeyUnique(dependency, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validate_EveryMapEntryUnique(dependency, diagnostics, context);
    }
    if (result || diagnostics != null)
    {
      result &= validateDependency_ResolvesInRealm(dependency, diagnostics, context);
    }
    return result;
  }

  /**
   * Validates the ResolvesInRealm constraint of '<em>Dependency</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public boolean validateDependency_ResolvesInRealm(Dependency dependency, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    Project resolvedProject = dependency.getResolvedProject();
    if (resolvedProject != null)
    {
      Realm realm = resolvedProject.getRealm();
      if (realm != null && realm.getSuppressedConstraints().contains(RESOLVES_IN_REALM))
      {
        return true;
      }

      String version = dependency.getExpandedVersion();
      if (!version.isBlank())
      {
        String resolvedVersion = resolvedProject.getVersion();
        if (!Objects.equals(version, resolvedVersion))
        {
          if (diagnostics != null)
          {
            boolean setViaPropertyExpansion = false;
            for (PropertyReference propertyReference : dependency.getPropertyReferences())
            {
              Element element = propertyReference.getElement();
              if (Coordinate.VERSION.equals(element.getLocalName()))
              {
                Property resolvedProperty = propertyReference.getResolvedProperty();
                if (resolvedProperty != null)
                {
                  setViaPropertyExpansion = true;

                  // Avoid creating the same message multiple times.
                  @SuppressWarnings("unchecked")
                  Set<String> messages = (Set<String>)context.computeIfAbsent(List.of(resolvedProperty, Coordinate.VERSION), it -> new LinkedHashSet<String>());
                  String messageKey = "BadPropertyVersion_diagnostic"; //$NON-NLS-1$
                  if (messages.add(messageKey + "resolvedVersion")) //$NON-NLS-1$
                  {
                    diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0, messageKey, // $NON-NLS-1$
                        new Object[] { resolvedVersion }, new Object[] { resolvedProperty, MavenPackage.Literals.COORDINATE__VERSION,
                            new ElementEdit(resolvedProperty.getElement(), resolvedVersion) },
                        context));
                  }
                }
              }
            }

            diagnostics.add(createDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0, "BadDependencyVersion_diagnostic", //$NON-NLS-1$
                new Object[] { version, resolvedVersion },
                setViaPropertyExpansion ? new Object[] { dependency, MavenPackage.Literals.COORDINATE__VERSION }
                    : new Object[] { dependency, MavenPackage.Literals.COORDINATE__VERSION,
                        new ElementEdit(dependency.getElement(POMXMLUtil.xpath(Coordinate.VERSION)), resolvedVersion) },
                context));
          }

          return false;
        }
      }
    }

    return true;
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
  public boolean validatePropertyReference(PropertyReference propertyReference, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return validate_EveryDefaultConstraint(propertyReference, diagnostics, context);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateConstraintType(ConstraintType constraintType, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateDocument(Document document, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateElement(Element element, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateElementEdit(MavenValidator.ElementEdit elementEdit, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateTextRegion(POMXMLUtil.TextRegion textRegion, DiagnosticChain diagnostics, Map<Object, Object> context)
  {
    return true;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean validateXPath(SegmentSequence xPath, DiagnosticChain diagnostics, Map<Object, Object> context)
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
    return MavenPlugin.INSTANCE;
  }

  public static record ElementEdit(Element element, String value)
  {
    public static ElementEdit of(Diagnostic diagnostic)
    {
      return diagnostic.getData().stream().filter(ElementEdit.class::isInstance).map(ElementEdit.class::cast).findFirst().orElse(null);
    }
  }

} // MavenValidator
