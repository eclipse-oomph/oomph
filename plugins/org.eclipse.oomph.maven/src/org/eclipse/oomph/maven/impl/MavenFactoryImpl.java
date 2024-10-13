/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.impl;

import org.eclipse.oomph.maven.Dependency;
import org.eclipse.oomph.maven.MavenFactory;
import org.eclipse.oomph.maven.MavenPackage;
import org.eclipse.oomph.maven.Parent;
import org.eclipse.oomph.maven.Project;
import org.eclipse.oomph.maven.Property;
import org.eclipse.oomph.maven.PropertyReference;
import org.eclipse.oomph.maven.Realm;
import org.eclipse.oomph.maven.util.MavenValidator;
import org.eclipse.oomph.maven.util.POMXMLUtil;

import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MavenFactoryImpl extends EFactoryImpl implements MavenFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static MavenFactory init()
  {
    try
    {
      MavenFactory theMavenFactory = (MavenFactory)EPackage.Registry.INSTANCE.getEFactory(MavenPackage.eNS_URI);
      if (theMavenFactory != null)
      {
        return theMavenFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new MavenFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MavenFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case MavenPackage.REALM:
        return createRealm();
      case MavenPackage.PROJECT:
        return createProject();
      case MavenPackage.PARENT:
        return createParent();
      case MavenPackage.DEPENDENCY:
        return createDependency();
      case MavenPackage.PROPERTY:
        return createProperty();
      case MavenPackage.PROPERTY_REFERENCE:
        return createPropertyReference();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case MavenPackage.DOCUMENT:
        return createDocumentFromString(eDataType, initialValue);
      case MavenPackage.ELEMENT:
        return createElementFromString(eDataType, initialValue);
      case MavenPackage.ELEMENT_EDIT:
        return createElementEditFromString(eDataType, initialValue);
      case MavenPackage.TEXT_REGION:
        return createTextRegionFromString(eDataType, initialValue);
      case MavenPackage.XPATH:
        return createXPathFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case MavenPackage.DOCUMENT:
        return convertDocumentToString(eDataType, instanceValue);
      case MavenPackage.ELEMENT:
        return convertElementToString(eDataType, instanceValue);
      case MavenPackage.ELEMENT_EDIT:
        return convertElementEditToString(eDataType, instanceValue);
      case MavenPackage.TEXT_REGION:
        return convertTextRegionToString(eDataType, instanceValue);
      case MavenPackage.XPATH:
        return convertXPathToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Realm createRealm()
  {
    RealmImpl realm = new RealmImpl();
    return realm;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Project createProject()
  {
    ProjectImpl project = new ProjectImpl();
    return project;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Parent createParent()
  {
    ParentImpl parent = new ParentImpl();
    return parent;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Dependency createDependency()
  {
    DependencyImpl dependency = new DependencyImpl();
    return dependency;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Property createProperty()
  {
    PropertyImpl property = new PropertyImpl();
    return property;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public PropertyReference createPropertyReference()
  {
    PropertyReferenceImpl propertyReference = new PropertyReferenceImpl();
    return propertyReference;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Document createDocument(String literal)
  {
    return (Document)super.createFromString(MavenPackage.Literals.DOCUMENT, literal);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Document createDocumentFromString(EDataType eDataType, String initialValue)
  {
    return createDocument(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertDocument(Document instanceValue)
  {
    return super.convertToString(MavenPackage.Literals.DOCUMENT, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertDocumentToString(EDataType eDataType, Object instanceValue)
  {
    return convertDocument((Document)instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Element createElement(String literal)
  {
    return (Element)super.createFromString(MavenPackage.Literals.ELEMENT, literal);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Element createElementFromString(EDataType eDataType, String initialValue)
  {
    return createElement(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertElement(Element instanceValue)
  {
    return super.convertToString(MavenPackage.Literals.ELEMENT, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertElementToString(EDataType eDataType, Object instanceValue)
  {
    return convertElement((Element)instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public MavenValidator.ElementEdit createElementEdit(String literal)
  {
    return (MavenValidator.ElementEdit)super.createFromString(MavenPackage.Literals.ELEMENT_EDIT, literal);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MavenValidator.ElementEdit createElementEditFromString(EDataType eDataType, String initialValue)
  {
    return createElementEdit(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertElementEdit(MavenValidator.ElementEdit instanceValue)
  {
    return super.convertToString(MavenPackage.Literals.ELEMENT_EDIT, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertElementEditToString(EDataType eDataType, Object instanceValue)
  {
    return convertElementEdit((MavenValidator.ElementEdit)instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public POMXMLUtil.TextRegion createTextRegion(String literal)
  {
    return (POMXMLUtil.TextRegion)super.createFromString(MavenPackage.Literals.TEXT_REGION, literal);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public POMXMLUtil.TextRegion createTextRegionFromString(EDataType eDataType, String initialValue)
  {
    return createTextRegion(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertTextRegion(POMXMLUtil.TextRegion instanceValue)
  {
    return super.convertToString(MavenPackage.Literals.TEXT_REGION, instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTextRegionToString(EDataType eDataType, Object instanceValue)
  {
    return convertTextRegion((POMXMLUtil.TextRegion)instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public SegmentSequence createXPath(String literal)
  {
    return literal == null ? null : SegmentSequence.create("/"); //$NON-NLS-1$
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SegmentSequence createXPathFromString(EDataType eDataType, String initialValue)
  {
    return createXPath(initialValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public String convertXPath(SegmentSequence instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertXPathToString(EDataType eDataType, Object instanceValue)
  {
    return convertXPath((SegmentSequence)instanceValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public MavenPackage getMavenPackage()
  {
    return (MavenPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static MavenPackage getPackage()
  {
    return MavenPackage.eINSTANCE;
  }

} // MavenFactoryImpl
