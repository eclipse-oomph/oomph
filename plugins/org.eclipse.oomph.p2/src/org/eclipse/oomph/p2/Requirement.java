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
package org.eclipse.oomph.p2;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.metadata.expression.IMatchExpression;

import java.util.Comparator;

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
 *   <li>{@link org.eclipse.oomph.p2.Requirement#isGreedy <em>Greedy</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Requirement#getFilter <em>Filter</em>}</li>
 *   <li>{@link org.eclipse.oomph.p2.Requirement#getType <em>Type</em>}</li>
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
  public static final String FEATURE_SUFFIX = ".feature.group";

  public static final String PROJECT_SUFFIX = ".plain.project";

  public static final Comparator<Requirement> COMPARATOR = new Comparator<Requirement>()
  {
    public int compare(Requirement o1, Requirement o2)
    {
      String ns1 = StringUtil.safe(o1.getNamespace());
      String ns2 = StringUtil.safe(o2.getNamespace());

      int result = ns1.compareTo(ns2);
      if (result == 0)
      {
        String n1 = StringUtil.safe(o1.getName());
        String n2 = StringUtil.safe(o2.getName());

        result = n1.compareTo(n2);
        if (result == 0)
        {
          VersionRange range1 = o1.getVersionRange();
          VersionRange range2 = o2.getVersionRange();

          result = range1.getMinimum().compareTo(range2.getMinimum());
          if (result == 0)
          {
            result = range1.getMaximum().compareTo(range2.getMaximum());
          }
        }
      }

      return result;
    }
  };

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

  /**
   * Returns the value of the '<em><b>Type</b></em>' attribute.
   * The literals are from the enumeration {@link org.eclipse.oomph.p2.RequirementType}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Type</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Type</em>' attribute.
   * @see org.eclipse.oomph.p2.RequirementType
   * @see org.eclipse.oomph.p2.P2Package#getRequirement_Type()
   * @model transient="true" changeable="false" volatile="true" derived="true"
   * @generated
   */
  RequirementType getType();

  /**
   * Returns the value of the '<em><b>Greedy</b></em>' attribute.
   * The default value is <code>"true"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Greedy</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Greedy</em>' attribute.
   * @see #setGreedy(boolean)
   * @see org.eclipse.oomph.p2.P2Package#getRequirement_Greedy()
   * @model default="true"
   * @generated
   */
  boolean isGreedy();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.p2.Requirement#isGreedy <em>Greedy</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Greedy</em>' attribute.
   * @see #isGreedy()
   * @generated
   */
  void setGreedy(boolean value);

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
