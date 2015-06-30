/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
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

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Requirement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.p2.Requirement#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Requirement#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Requirement#getVersionRange <em>Version Range</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Requirement#isOptional <em>Optional</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Requirement#isFeature <em>Feature</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Requirement#getFilter <em>Filter</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.p2.P2Package#getRequirement()
 * @model features="iD"
 *        iDDataType="org.eclipse.emf.ecore.EString" iDRequired="true" iDTransient="true" iDVolatile="true" iDDerived="true" iDSuppressedGetVisibility="true" iDSuppressedSetVisibility="true"
 *        iDExtendedMetaData="kind='attribute' name='id'"
 * @generated
 */
public interface Requirement extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>ID</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.oomph.p2.P2Package#getRequirement_Name()
   * @model required="true"
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.Requirement#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Namespace</b></em>' attribute.
   * The default value is <code>"org.eclipse.equinox.p2.iu"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Namespace</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Namespace</em>' attribute.
   * @see #setNamespace(String)
   * @see org.eclipse.oomph.p2.P2Package#getRequirement_Namespace()
   * @model default="org.eclipse.equinox.p2.iu" required="true"
   * @generated
   */
  String getNamespace();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.Requirement#getNamespace <em>Namespace</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Namespace</em>' attribute.
   * @see #getNamespace()
   * @generated
   */
  void setNamespace(String value);

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
   * Returns the value of the '<em><b>Filter</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Filter</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Filter</em>' attribute.
   * @see #setFilter(String)
   * @see org.eclipse.oomph.p2.P2Package#getRequirement_Filter()
   * @model
   * @generated
   */
  String getFilter();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.Requirement#getFilter <em>Filter</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Filter</em>' attribute.
   * @see #getFilter()
   * @generated
   */
  void setFilter(String value);

  IMatchExpression<IInstallableUnit> getMatchExpression();

  void setMatchExpression(IMatchExpression<IInstallableUnit> matchExpression);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model versionDataType="org.eclipse.oomph.p2.Version"
   * @generated
   */
  void setVersionRange(Version version, VersionSegment segment);

} // Requirement
