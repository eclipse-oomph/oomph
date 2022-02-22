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

import org.eclipse.emf.common.util.Enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Repository Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.p2.P2Package#getRepositoryType()
 * @model
 * @generated
 */
public enum RepositoryType implements Enumerator
{
  /**
   * The '<em><b>Metadata</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #METADATA_VALUE
   * @generated
   * @ordered
   */
  METADATA(0, "Metadata", "Metadata"), //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * The '<em><b>Artifact</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ARTIFACT_VALUE
   * @generated
   * @ordered
   */
  ARTIFACT(1, "Artifact", "Artifact"), //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * The '<em><b>Combined</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #COMBINED_VALUE
   * @generated
   * @ordered
   */
  COMBINED(2, "Combined", "Combined"); //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * The '<em><b>Metadata</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #METADATA
   * @model name="Metadata"
   * @generated
   * @ordered
   */
  public static final int METADATA_VALUE = 0;

  /**
   * The '<em><b>Artifact</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #ARTIFACT
   * @model name="Artifact"
   * @generated
   * @ordered
   */
  public static final int ARTIFACT_VALUE = 1;

  /**
   * The '<em><b>Combined</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #COMBINED
   * @model name="Combined"
   * @generated
   * @ordered
   */
  public static final int COMBINED_VALUE = 2;

  /**
   * An array of all the '<em><b>Repository Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final RepositoryType[] VALUES_ARRAY = new RepositoryType[] { METADATA, ARTIFACT, COMBINED, };

  /**
   * A public read-only list of all the '<em><b>Repository Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<RepositoryType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Repository Type</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static RepositoryType get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      RepositoryType result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Repository Type</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static RepositoryType getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      RepositoryType result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Repository Type</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static RepositoryType get(int value)
  {
    switch (value)
    {
      case METADATA_VALUE:
        return METADATA;
      case ARTIFACT_VALUE:
        return ARTIFACT;
      case COMBINED_VALUE:
        return COMBINED;
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
  private RepositoryType(int value, String name, String literal)
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

  public boolean isMetadata()
  {
    return this == METADATA || this == COMBINED;
  }

  public boolean isArtifact()
  {
    return this == ARTIFACT || this == COMBINED;
  }

} // RepositoryType
