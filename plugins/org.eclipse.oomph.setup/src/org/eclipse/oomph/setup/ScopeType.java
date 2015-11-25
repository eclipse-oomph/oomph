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
 * A representation of the literals of the enumeration '<em><b>Task Scope</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.setup.SetupPackage#getScopeType()
 * @model
 * @generated
 */
public enum ScopeType implements Enumerator
{
  /**
   * The '<em><b>None</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #NONE_VALUE
   * @generated
   * @ordered
   */
  NONE(0, "None", "None"),
  /**
  * The '<em><b>Product Catalog</b></em>' literal object.
  * <!-- begin-user-doc -->
  * <!-- end-user-doc -->
  * @see #PRODUCT_CATALOG_VALUE
  * @generated
  * @ordered
  */
  PRODUCT_CATALOG(1, "ProductCatalog", "ProductCatalog"),
  /**
  * The '<em><b>Product</b></em>' literal object.
  * <!-- begin-user-doc -->
  * <!-- end-user-doc -->
  * @see #PRODUCT_VALUE
  * @generated
  * @ordered
  */
  PRODUCT(2, "Product", "Eclipse"),
  /**
  * The '<em><b>Product Version</b></em>' literal object.
  * <!-- begin-user-doc -->
  * <!-- end-user-doc -->
  * @see #PRODUCT_VERSION_VALUE
  * @generated
  * @ordered
  */
  PRODUCT_VERSION(3, "ProductVersion", "ProductVersion"),
  /**
  * The '<em><b>Project Catalog</b></em>' literal object.
  * <!-- begin-user-doc -->
  * <!-- end-user-doc -->
  * @see #PROJECT_CATALOG_VALUE
  * @generated
  * @ordered
  */
  PROJECT_CATALOG(4, "ProjectCatalog", "ProjectCatalog"),
  /**
  * The '<em><b>Project</b></em>' literal object.
  * <!-- begin-user-doc -->
  * <!-- end-user-doc -->
  * @see #PROJECT_VALUE
  * @generated
  * @ordered
  */
  PROJECT(5, "Project", "Project"),
  /**
  * The '<em><b>Stream</b></em>' literal object.
  * <!-- begin-user-doc -->
  * <!-- end-user-doc -->
  * @see #STREAM_VALUE
  * @generated
  * @ordered
  */
  STREAM(6, "Stream", "Stream"),
  /**
  * The '<em><b>Installation</b></em>' literal object.
  * <!-- begin-user-doc -->
  * <!-- end-user-doc -->
  * @see #INSTALLATION_VALUE
  * @generated
  * @ordered
  */
  INSTALLATION(7, "Installation", "Installation"),
  /**
  * The '<em><b>Workspace</b></em>' literal object.
  * <!-- begin-user-doc -->
  * <!-- end-user-doc -->
  * @see #WORKSPACE_VALUE
  * @generated
  * @ordered
  */
  WORKSPACE(8, "Workspace", "Workspace"),
  /**
  * The '<em><b>User</b></em>' literal object.
  * <!-- begin-user-doc -->
  * <!-- end-user-doc -->
  * @see #USER_VALUE
  * @generated
  * @ordered
  */
  USER(9, "User", "User");

  /**
   * The '<em><b>None</b></em>' literal value.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>None</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @see #NONE
   * @model name="None"
   * @generated
   * @ordered
   */
  public static final int NONE_VALUE = 0;

  /**
   * The '<em><b>Product Catalog</b></em>' literal value.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Product Catalog</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @see #PRODUCT_CATALOG
   * @model name="ProductCatalog"
   * @generated
   * @ordered
   */
  public static final int PRODUCT_CATALOG_VALUE = 1;

  /**
   * The '<em><b>Product</b></em>' literal value.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Product</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @see #PRODUCT
   * @model name="Product" literal="Eclipse"
   * @generated
   * @ordered
   */
  public static final int PRODUCT_VALUE = 2;

  /**
   * The '<em><b>Product Version</b></em>' literal value.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Product Version</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @see #PRODUCT_VERSION
   * @model name="ProductVersion"
   * @generated
   * @ordered
   */
  public static final int PRODUCT_VERSION_VALUE = 3;

  /**
   * The '<em><b>Project Catalog</b></em>' literal value.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Project Catalog</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @see #PROJECT_CATALOG
   * @model name="ProjectCatalog"
   * @generated
   * @ordered
   */
  public static final int PROJECT_CATALOG_VALUE = 4;

  /**
   * The '<em><b>Project</b></em>' literal value.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Project</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @see #PROJECT
   * @model name="Project"
   * @generated
   * @ordered
   */
  public static final int PROJECT_VALUE = 5;

  /**
   * The '<em><b>Stream</b></em>' literal value.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Stream</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @see #STREAM
   * @model name="Stream"
   * @generated
   * @ordered
   */
  public static final int STREAM_VALUE = 6;

  /**
   * The '<em><b>Installation</b></em>' literal value.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Installation</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @see #INSTALLATION
   * @model name="Installation"
   * @generated
   * @ordered
   */
  public static final int INSTALLATION_VALUE = 7;

  /**
   * The '<em><b>Workspace</b></em>' literal value.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Workspace</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @see #WORKSPACE
   * @model name="Workspace"
   * @generated
   * @ordered
   */
  public static final int WORKSPACE_VALUE = 8;

  /**
   * The '<em><b>User</b></em>' literal value.
   * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>User</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
   * @see #USER
   * @model name="User"
   * @generated
   * @ordered
   */
  public static final int USER_VALUE = 9;

  /**
   * An array of all the '<em><b>Scope Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
  private static final ScopeType[] VALUES_ARRAY = new ScopeType[] { NONE, PRODUCT_CATALOG, PRODUCT, PRODUCT_VERSION, PROJECT_CATALOG, PROJECT, STREAM,
      INSTALLATION, WORKSPACE, USER, };

  /**
   * A public read-only list of all the '<em><b>Scope Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @generated
   */
  public static final List<ScopeType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>Scope Type</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static ScopeType get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ScopeType result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Scope Type</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static ScopeType getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      ScopeType result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>Scope Type</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static ScopeType get(int value)
  {
    switch (value)
    {
      case NONE_VALUE:
        return NONE;
      case PRODUCT_CATALOG_VALUE:
        return PRODUCT_CATALOG;
      case PRODUCT_VALUE:
        return PRODUCT;
      case PRODUCT_VERSION_VALUE:
        return PRODUCT_VERSION;
      case PROJECT_CATALOG_VALUE:
        return PROJECT_CATALOG;
      case PROJECT_VALUE:
        return PROJECT;
      case STREAM_VALUE:
        return STREAM;
      case INSTALLATION_VALUE:
        return INSTALLATION;
      case WORKSPACE_VALUE:
        return WORKSPACE;
      case USER_VALUE:
        return USER;
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
  private ScopeType(int value, String name, String literal)
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

} // SetupTaskScope
