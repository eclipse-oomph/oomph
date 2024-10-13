/**
 * Copyright (c) ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven;

import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.maven.util.MavenValidator;
import org.eclipse.oomph.maven.util.POMXMLUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.SegmentSequence;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DOM Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.maven.DOMElement#getElement <em>Element</em>}</li>
 *   <li>{@link org.eclipse.oomph.maven.DOMElement#getPropertyReferences <em>Property References</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.maven.MavenPackage#getDOMElement()
 * @model abstract="true"
 * @generated
 */
public interface DOMElement extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Property References</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.maven.PropertyReference}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Property References</em>' containment reference list.
   * @see org.eclipse.oomph.maven.MavenPackage#getDOMElement_PropertyReferences()
   * @model containment="true"
   * @generated
   */
  EList<PropertyReference> getPropertyReferences();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model dataType="org.eclipse.oomph.maven.Element" xpathDataType="org.eclipse.oomph.maven.XPath" xpathRequired="true"
   * @generated
   */
  Element getElement(SegmentSequence xpath);

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model kind="operation"
   * @generated
   */
  Map<Document, Map<POMXMLUtil.TextRegion, MavenValidator.ElementEdit>> getElementEdits();

  /**
   * Returns the value of the '<em><b>Element</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Element</em>' attribute.
   * @see #setElement(Element)
   * @see org.eclipse.oomph.maven.MavenPackage#getDOMElement_Element()
   * @model dataType="org.eclipse.oomph.maven.Element" required="true"
   * @generated
   */
  Element getElement();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.maven.DOMElement#getElement <em>Element</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Element</em>' attribute.
   * @see #getElement()
   * @generated
   */
  void setElement(Element value);

} // DOMElement
