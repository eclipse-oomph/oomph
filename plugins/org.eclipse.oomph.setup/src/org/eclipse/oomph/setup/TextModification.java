/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup;

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Text Modification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.TextModification#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.TextModification#getSubstitutions <em>Substitutions</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.SetupPackage#getTextModification()
 * @model
 * @generated
 */
public interface TextModification extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Pattern</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Pattern</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Pattern</em>' attribute.
   * @see #setPattern(String)
   * @see org.eclipse.oomph.setup.SetupPackage#getTextModification_Pattern()
   * @model required="true"
   * @generated
   */
  String getPattern();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.TextModification#getPattern <em>Pattern</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Pattern</em>' attribute.
   * @see #getPattern()
   * @generated
   */
  void setPattern(String value);

  /**
   * Returns the value of the '<em><b>Substitutions</b></em>' attribute list.
   * The list contents are of type {@link java.lang.String}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Substitutions</em>' attribute list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Substitutions</em>' attribute list.
   * @see org.eclipse.oomph.setup.SetupPackage#getTextModification_Substitutions()
   * @model extendedMetaData="kind='element' name='substitution'"
   * @generated
   */
  EList<String> getSubstitutions();

} // TextModification
