/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven;

import org.eclipse.oomph.maven.util.MavenValidator;
import org.eclipse.oomph.maven.util.POMXMLUtil;

import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.ecore.EFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.maven.MavenPackage
 * @generated
 */
public interface MavenFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  MavenFactory eINSTANCE = org.eclipse.oomph.maven.impl.MavenFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Realm</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Realm</em>'.
   * @generated
   */
  Realm createRealm();

  /**
   * Returns a new object of class '<em>Project</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Project</em>'.
   * @generated
   */
  Project createProject();

  /**
   * Returns a new object of class '<em>Parent</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Parent</em>'.
   * @generated
   */
  Parent createParent();

  /**
   * Returns a new object of class '<em>Dependency</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Dependency</em>'.
   * @generated
   */
  Dependency createDependency();

  /**
   * Returns a new object of class '<em>Property</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Property</em>'.
   * @generated
   */
  Property createProperty();

  /**
   * Returns a new object of class '<em>Property Reference</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Property Reference</em>'.
   * @generated
   */
  PropertyReference createPropertyReference();

  /**
   * Returns an instance of data type '<em>Constraint Type</em>' corresponding the given literal.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal a literal of the data type.
   * @return a new instance value of the data type.
   * @generated
   */
  ConstraintType createConstraintType(String literal);

  /**
   * Returns a literal representation of an instance of data type '<em>Constraint Type</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param instanceValue an instance value of the data type.
   * @return a literal representation of the instance value.
   * @generated
   */
  String convertConstraintType(ConstraintType instanceValue);

  /**
   * Returns an instance of data type '<em>Document</em>' corresponding the given literal.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal a literal of the data type.
   * @return a new instance value of the data type.
   * @generated
   */
  Document createDocument(String literal);

  /**
   * Returns a literal representation of an instance of data type '<em>Document</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param instanceValue an instance value of the data type.
   * @return a literal representation of the instance value.
   * @generated
   */
  String convertDocument(Document instanceValue);

  /**
   * Returns an instance of data type '<em>Element</em>' corresponding the given literal.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal a literal of the data type.
   * @return a new instance value of the data type.
   * @generated
   */
  Element createElement(String literal);

  /**
   * Returns a literal representation of an instance of data type '<em>Element</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param instanceValue an instance value of the data type.
   * @return a literal representation of the instance value.
   * @generated
   */
  String convertElement(Element instanceValue);

  /**
   * Returns an instance of data type '<em>Element Edit</em>' corresponding the given literal.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal a literal of the data type.
   * @return a new instance value of the data type.
   * @generated
   */
  MavenValidator.ElementEdit createElementEdit(String literal);

  /**
   * Returns a literal representation of an instance of data type '<em>Element Edit</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param instanceValue an instance value of the data type.
   * @return a literal representation of the instance value.
   * @generated
   */
  String convertElementEdit(MavenValidator.ElementEdit instanceValue);

  /**
   * Returns an instance of data type '<em>Text Region</em>' corresponding the given literal.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal a literal of the data type.
   * @return a new instance value of the data type.
   * @generated
   */
  POMXMLUtil.TextRegion createTextRegion(String literal);

  /**
   * Returns a literal representation of an instance of data type '<em>Text Region</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param instanceValue an instance value of the data type.
   * @return a literal representation of the instance value.
   * @generated
   */
  String convertTextRegion(POMXMLUtil.TextRegion instanceValue);

  /**
   * Returns an instance of data type '<em>XPath</em>' corresponding the given literal.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal a literal of the data type.
   * @return a new instance value of the data type.
   * @generated
   */
  SegmentSequence createXPath(String literal);

  /**
   * Returns a literal representation of an instance of data type '<em>XPath</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param instanceValue an instance value of the data type.
   * @return a literal representation of the instance value.
   * @generated
   */
  String convertXPath(SegmentSequence instanceValue);

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  MavenPackage getMavenPackage();

} // MavenFactory
