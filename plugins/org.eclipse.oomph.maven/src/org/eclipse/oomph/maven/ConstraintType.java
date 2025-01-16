/**
 * Copyright (c) 2025 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven;

import org.eclipse.emf.common.util.Enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Diagnostic</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.maven.MavenPackage#getConstraintType()
 * @model
 * @generated
 */
public enum ConstraintType implements Enumerator
{
  /**
   * The '<em><b>Valid Relative Parent</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #VALID_RELATIVE_PARENT_VALUE
   * @generated
   * @ordered
   */
  VALID_RELATIVE_PARENT(0, "ValidRelativeParent", "ValidRelativeParent"), //$NON-NLS-1$ //$NON-NLS-2$
  /**
   * The '<em><b>Resolves In Realm</b></em>' literal object.
   * <!-- begin-user-doc -->
  * <!-- end-user-doc -->
   * @see #RESOLVES_IN_REALM_VALUE
   * @generated
   * @ordered
   */
  RESOLVES_IN_REALM(1, "ResolvesInRealm", "ResolvesInRealm"); //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * The '<em><b>Valid Relative Parent</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #VALID_RELATIVE_PARENT
   * @model name="ValidRelativeParent"
   * @generated
   * @ordered
   */
  public static final int VALID_RELATIVE_PARENT_VALUE = 0;

  /**
   * The '<em><b>Resolves In Realm</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RESOLVES_IN_REALM
   * @model name="ResolvesInRealm"
   * @generated
   * @ordered
   */
  public static final int RESOLVES_IN_REALM_VALUE = 1;

  /**
   * An array of all the '<em><b>Constraint Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final ConstraintType[] VALUES_ARRAY = new ConstraintType[] { VALID_RELATIVE_PARENT, RESOLVES_IN_REALM, };

  /**
   * A public read-only list of all the '<em><b>Constraint Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<ConstraintType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Constraint Type</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static ConstraintType get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ConstraintType result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Constraint Type</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static ConstraintType getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ConstraintType result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Constraint Type</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static ConstraintType get(int value)
  {
    switch (value)
    {
      case VALID_RELATIVE_PARENT_VALUE:
        return VALID_RELATIVE_PARENT;
      case RESOLVES_IN_REALM_VALUE:
        return RESOLVES_IN_REALM;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final int value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String name;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String literal;

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private ConstraintType(int value, String name, String literal)
  {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public int getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getLiteral()
  {
    return literal;
  }

  /**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    return literal;
  }

} // Diagnostic
