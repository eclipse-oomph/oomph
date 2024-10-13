/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.EList;

import java.util.Comparator;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.Property#getKey <em>Key</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Property#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Property#getExpandedValue <em>Expanded Value</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.Property#getIncomingResolvedPropertyReferences <em>Incoming Resolved Property References</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.maven.MavenPackage#getProperty()
 * @model
 * @generated
 */
public interface Property extends DOMElement
{
  String PROPERTIES = "properties"; //$NON-NLS-1$

  Comparator<Property> COMPARATOR = new Comparator<>()
  {
    private final Comparator<String> stringComparator = CommonPlugin.INSTANCE.getComparator();

    @Override
    public int compare(Property o1, Property o2)
    {
      return stringComparator.compare(o1.getKey(), o2.getKey());
    }
  };

  /**
   * Returns the value of the '<em><b>Key</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Key</em>' attribute.
   * @see org.eclipse.oomph.maven.MavenPackage#getProperty_Key()
   * @model required="true" changeable="false" derived="true"
   * @generated
   */
  String getKey();

  /**
   * Returns the value of the '<em><b>Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Value</em>' attribute.
   * @see org.eclipse.oomph.maven.MavenPackage#getProperty_Value()
   * @model required="true" changeable="false" derived="true"
   * @generated
   */
  String getValue();

  /**
   * Returns the value of the '<em><b>Expanded Value</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expanded Value</em>' attribute.
   * @see org.eclipse.oomph.maven.MavenPackage#getProperty_ExpandedValue()
   * @model required="true" changeable="false" derived="true"
   * @generated
   */
  String getExpandedValue();

  /**
   * Returns the value of the '<em><b>Incoming Resolved Property References</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.oomph.maven.PropertyReference}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.maven.PropertyReference#getResolvedProperty <em>Resolved Property</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Incoming Resolved Property References</em>' reference list.
   * @see org.eclipse.oomph.maven.MavenPackage#getProperty_IncomingResolvedPropertyReferences()
   * @see org.eclipse.oomph.maven.PropertyReference#getResolvedProperty
   * @model opposite="resolvedProperty"
   * @generated
   */
  EList<PropertyReference> getIncomingResolvedPropertyReferences();

} // Property
