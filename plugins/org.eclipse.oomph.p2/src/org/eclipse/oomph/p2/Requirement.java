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
package org.eclipse.oomph.p2;

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Requirement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.p2.Requirement#getID <em>ID</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Requirement#getVersionRange <em>Version Range</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Requirement#isOptional <em>Optional</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Requirement#isFeature <em>Feature</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.p2.P2Package#getRequirement()
 * @model
 * @generated
 */
public interface Requirement extends ModelElement
{
  /**
   * Returns the value of the '<em><b>ID</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>ID</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>ID</em>' attribute.
   * @see #setID(String)
   * @see org.eclipse.oomph.p2.P2Package#getRequirement_ID()
   * @model required="true"
   *        extendedMetaData="kind='attribute' name='id'"
   * @generated
   */
  String getID();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.Requirement#getID <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>ID</em>' attribute.
   * @see #getID()
   * @generated
   */
  void setID(String value);

  /**
   * Returns the value of the '<em><b>Version Range</b></em>' attribute.
   * The default value is <code>"0.0.0"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Version Range</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Version Range</em>' attribute.
   * @see #setVersionRange(VersionRange)
   * @see org.eclipse.oomph.p2.P2Package#getRequirement_VersionRange()
   * @model default="0.0.0" dataType="org.eclipse.oomph.p2.VersionRange"
   * @generated
   */
  VersionRange getVersionRange();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.Requirement#getVersionRange <em>Version Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Version Range</em>' attribute.
   * @see #getVersionRange()
   * @generated
   */
  void setVersionRange(VersionRange value);

  /**
   * Returns the value of the '<em><b>Optional</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Optional</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Optional</em>' attribute.
   * @see #setOptional(boolean)
   * @see org.eclipse.oomph.p2.P2Package#getRequirement_Optional()
   * @model
   * @generated
   */
  boolean isOptional();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.Requirement#isOptional <em>Optional</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Optional</em>' attribute.
   * @see #isOptional()
   * @generated
   */
  void setOptional(boolean value);

  /**
   * Returns the value of the '<em><b>Feature</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Feature</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Feature</em>' attribute.
   * @see org.eclipse.oomph.p2.P2Package#getRequirement_Feature()
   * @model transient="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  boolean isFeature();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model versionDataType="org.eclipse.oomph.p2.Version"
   * @generated
   */
  void setVersionRange(Version version, VersionSegment segment);

} // Requirement
