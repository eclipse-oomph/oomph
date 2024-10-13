/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.PropertyReference#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.PropertyReference#getResolvedProperty <em>Resolved Property</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.maven.MavenPackage#getPropertyReference()
 * @model
 * @generated
 */
public interface PropertyReference extends DOMElement
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see org.eclipse.oomph.maven.MavenPackage#getPropertyReference_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.maven.PropertyReference#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Resolved Property</b></em>' reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.maven.Property#getIncomingResolvedPropertyReferences <em>Incoming Resolved Property References</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Resolved Property</em>' reference.
   * @see #setResolvedProperty(Property)
   * @see org.eclipse.oomph.maven.MavenPackage#getPropertyReference_ResolvedProperty()
   * @see org.eclipse.oomph.maven.Property#getIncomingResolvedPropertyReferences
   * @model opposite="incomingResolvedPropertyReferences" required="true"
   * @generated
   */
  Property getResolvedProperty();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.maven.PropertyReference#getResolvedProperty <em>Resolved Property</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Resolved Property</em>' reference.
   * @see #getResolvedProperty()
   * @generated
   */
  void setResolvedProperty(Property value);

} // PropertyReference
