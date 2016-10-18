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
package org.eclipse.oomph.base;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.base.BasePackage
 * @generated
 */
public interface BaseFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  BaseFactory eINSTANCE = org.eclipse.oomph.base.impl.BaseFactoryImpl.init();

  /**
   * Returns a new object of class '<em>Annotation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>Annotation</em>'.
   * @generated
   */
  Annotation createAnnotation();

  Annotation createAnnotation(String source);

  Annotation createErrorAnnotation(String diagnostic);

  Annotation createWarningAnnotation(String diagnostic);

  Annotation createInfoAnnotation(String diagnostic);

  /**
   * Returns an instance of data type '<em>URI</em>' corresponding the given literal.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal a literal of the data type.
   * @return a new instance value of the data type.
   * @generated
   */
  URI createURI(String literal);

  /**
   * Returns a literal representation of an instance of data type '<em>URI</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param instanceValue an instance value of the data type.
   * @return a literal representation of the instance value.
   * @generated
   */
  String convertURI(URI instanceValue);

  /**
   * Returns an instance of data type '<em>Exception</em>' corresponding the given literal.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal a literal of the data type.
   * @return a new instance value of the data type.
   * @generated
   */
  Exception createException(String literal);

  /**
   * Returns a literal representation of an instance of data type '<em>Exception</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param instanceValue an instance value of the data type.
   * @return a literal representation of the instance value.
   * @generated
   */
  String convertException(Exception instanceValue);

  /**
   * Returns an instance of data type '<em>Text</em>' corresponding the given literal.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal a literal of the data type.
   * @return a new instance value of the data type.
   * @generated
   */
  String createText(String literal);

  /**
   * Returns a literal representation of an instance of data type '<em>Text</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param instanceValue an instance value of the data type.
   * @return a literal representation of the instance value.
   * @generated
   */
  String convertText(String instanceValue);

  /**
   * Returns an instance of data type '<em>ID</em>' corresponding the given literal.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal a literal of the data type.
   * @return a new instance value of the data type.
   * @generated
   */
  String createID(String literal);

  /**
   * Returns a literal representation of an instance of data type '<em>ID</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param instanceValue an instance value of the data type.
   * @return a literal representation of the instance value.
   * @generated
   */
  String convertID(String instanceValue);

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  BasePackage getBasePackage();

} // BaseFactory
