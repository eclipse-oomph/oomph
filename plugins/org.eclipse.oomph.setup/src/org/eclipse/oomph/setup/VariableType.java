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
package org.eclipse.oomph.setup;

import org.eclipse.emf.common.util.Enumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Variable Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.SetupPackage#getVariableType()
 * @model
 * @generated
 */
public enum VariableType implements Enumerator
{
  /**
   * The '<em><b>STRING</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #STRING_VALUE
   * @generated
   * @ordered
   */
  STRING(0, "STRING", "STRING"),

  /**
   * The '<em><b>TEXT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #TEXT_VALUE
   * @generated
   * @ordered
   */
  TEXT(1, "TEXT", "TEXT"),

  /**
   * The '<em><b>PASSWORD</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PASSWORD_VALUE
   * @generated
   * @ordered
   */
  PASSWORD(2, "PASSWORD", "PASSWORD"),

  /**
   * The '<em><b>PATTERN</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PATTERN_VALUE
   * @generated
   * @ordered
   */
  PATTERN(3, "PATTERN", "PATTERN"),

  /**
   * The '<em><b>URI</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #URI_VALUE
   * @generated
   * @ordered
   */
  URI(4, "URI", "URI"),

  /**
   * The '<em><b>FILE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #FILE_VALUE
   * @generated
   * @ordered
   */
  FILE(5, "FILE", "FILE"),

  /**
   * The '<em><b>FOLDER</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #FOLDER_VALUE
   * @generated
   * @ordered
   */
  FOLDER(6, "FOLDER", "FOLDER"),

  /**
   * The '<em><b>RESOURCE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #RESOURCE_VALUE
   * @generated
   * @ordered
   */
  RESOURCE(7, "RESOURCE", "RESOURCE"),

  /**
   * The '<em><b>CONTAINER</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CONTAINER_VALUE
   * @generated
   * @ordered
   */
  CONTAINER(8, "CONTAINER", "CONTAINER"),

  /**
   * The '<em><b>PROJECT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PROJECT_VALUE
   * @generated
   * @ordered
   */
  PROJECT(9, "PROJECT", "PROJECT"),

  /**
   * The '<em><b>BOOLEAN</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #BOOLEAN_VALUE
   * @generated
   * @ordered
   */
  BOOLEAN(10, "BOOLEAN", "BOOLEAN"),

  /**
   * The '<em><b>INTEGER</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #INTEGER_VALUE
   * @generated
   * @ordered
   */
  INTEGER(11, "INTEGER", "INTEGER"),

  /**
   * The '<em><b>FLOAT</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #FLOAT_VALUE
   * @generated
   * @ordered
   */
  FLOAT(12, "FLOAT", "FLOAT"),
  /**
  * The '<em><b>JRE</b></em>' literal object.
  * <!-- begin-user-doc -->
  * <!-- end-user-doc -->
  * @see #JRE_VALUE
  * @generated
  * @ordered
  */
  JRE(13, "JRE", "JRE");

  /**
   * The '<em><b>STRING</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>STRING</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #STRING
   * @model
   * @generated
   * @ordered
   */
  public static final int STRING_VALUE = 0;

  /**
   * The '<em><b>TEXT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>TEXT</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #TEXT
   * @model
   * @generated
   * @ordered
   */
  public static final int TEXT_VALUE = 1;

  /**
   * The '<em><b>PASSWORD</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>PASSWORD</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #PASSWORD
   * @model
   * @generated
   * @ordered
   */
  public static final int PASSWORD_VALUE = 2;

  /**
   * The '<em><b>PATTERN</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>PATTERN</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #PATTERN
   * @model
   * @generated
   * @ordered
   */
  public static final int PATTERN_VALUE = 3;

  /**
   * The '<em><b>URI</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>URI</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #URI
   * @model
   * @generated
   * @ordered
   */
  public static final int URI_VALUE = 4;

  /**
   * The '<em><b>FILE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>FILE</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #FILE
   * @model
   * @generated
   * @ordered
   */
  public static final int FILE_VALUE = 5;

  /**
   * The '<em><b>FOLDER</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>FOLDER</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #FOLDER
   * @model
   * @generated
   * @ordered
   */
  public static final int FOLDER_VALUE = 6;

  /**
   * The '<em><b>RESOURCE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>RESOURCE</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #RESOURCE
   * @model
   * @generated
   * @ordered
   */
  public static final int RESOURCE_VALUE = 7;

  /**
   * The '<em><b>CONTAINER</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>CONTAINER</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #CONTAINER
   * @model
   * @generated
   * @ordered
   */
  public static final int CONTAINER_VALUE = 8;

  /**
   * The '<em><b>PROJECT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>PROJECT</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #PROJECT
   * @model
   * @generated
   * @ordered
   */
  public static final int PROJECT_VALUE = 9;

  /**
   * The '<em><b>BOOLEAN</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>BOOLEAN</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #BOOLEAN
   * @model
   * @generated
   * @ordered
   */
  public static final int BOOLEAN_VALUE = 10;

  /**
   * The '<em><b>INTEGER</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>INTEGER</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #INTEGER
   * @model
   * @generated
   * @ordered
   */
  public static final int INTEGER_VALUE = 11;

  /**
   * The '<em><b>FLOAT</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>FLOAT</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #FLOAT
   * @model
   * @generated
   * @ordered
   */
  public static final int FLOAT_VALUE = 12;

  /**
   * The '<em><b>JRE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>JRE</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #JRE
   * @model
   * @generated
   * @ordered
   */
  public static final int JRE_VALUE = 13;

  /**
   * An array of all the '<em><b>Variable Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final VariableType[] VALUES_ARRAY = new VariableType[] { STRING, TEXT, PASSWORD, PATTERN, URI, FILE, FOLDER, RESOURCE, CONTAINER, PROJECT,
      BOOLEAN, INTEGER, FLOAT, JRE, };

  /**
   * A public read-only list of all the '<em><b>Variable Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<VariableType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Variable Type</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static VariableType get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      VariableType result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Variable Type</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static VariableType getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      VariableType result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Variable Type</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static VariableType get(int value)
  {
    switch (value)
    {
      case STRING_VALUE:
        return STRING;
      case TEXT_VALUE:
        return TEXT;
      case PASSWORD_VALUE:
        return PASSWORD;
      case PATTERN_VALUE:
        return PATTERN;
      case URI_VALUE:
        return URI;
      case FILE_VALUE:
        return FILE;
      case FOLDER_VALUE:
        return FOLDER;
      case RESOURCE_VALUE:
        return RESOURCE;
      case CONTAINER_VALUE:
        return CONTAINER;
      case PROJECT_VALUE:
        return PROJECT;
      case BOOLEAN_VALUE:
        return BOOLEAN;
      case INTEGER_VALUE:
        return INTEGER;
      case FLOAT_VALUE:
        return FLOAT;
      case JRE_VALUE:
        return JRE;
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
  private VariableType(int value, String name, String literal)
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
  public int getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
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

} // VariableType
