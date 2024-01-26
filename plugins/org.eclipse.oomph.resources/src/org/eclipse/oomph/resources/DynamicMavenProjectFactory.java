/*
 * Copyright (c) 2024 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.resources;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dynamic Maven Project Factory</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.resources.DynamicMavenProjectFactory#getXMLFileName <em>XML File Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.resources.ResourcesPackage#getDynamicMavenProjectFactory()
 * @model
 * @generated
 */
public interface DynamicMavenProjectFactory extends MavenProjectFactory
{
  /**
   * Returns the value of the '<em><b>XML File Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>XML File Name</em>' attribute.
   * @see #setXMLFileName(String)
   * @see org.eclipse.oomph.resources.ResourcesPackage#getDynamicMavenProjectFactory_XMLFileName()
   * @model extendedMetaData="name='xmlFileName'"
   * @generated
   */
  String getXMLFileName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.resources.DynamicMavenProjectFactory#getXMLFileName <em>XML File Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>XML File Name</em>' attribute.
   * @see #getXMLFileName()
   * @generated
   */
  void setXMLFileName(String value);

} // DynamicMavenProjectFactory
