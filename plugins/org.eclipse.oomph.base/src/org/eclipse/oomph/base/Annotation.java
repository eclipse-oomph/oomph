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
package org.eclipse.oomph.base;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Annotation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.base.Annotation#getModelElement <em>Model Element</em>}</li>
 *   <li>{@link org.eclipse.oomph.base.Annotation#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.oomph.base.Annotation#getDetails <em>Details</em>}</li>
 *   <li>{@link org.eclipse.oomph.base.Annotation#getContents <em>Contents</em>}</li>
 *   <li>{@link org.eclipse.oomph.base.Annotation#getReferences <em>References</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.base.BasePackage#getAnnotation()
 * @model annotation="http://www.eclipse.org/emf/2002/Ecore constraints='WellFormedSourceURI'"
 * @generated
 */
public interface Annotation extends ModelElement
{
  /**
   * Returns the value of the '<em><b>Model Element</b></em>' container reference.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.base.ModelElement#getAnnotations <em>Annotations</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Model Element</em>' container reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Model Element</em>' container reference.
   * @see #setModelElement(ModelElement)
   * @see org.eclipse.oomph.base.BasePackage#getAnnotation_ModelElement()
   * @see org.eclipse.oomph.base.ModelElement#getAnnotations
   * @model opposite="annotations" resolveProxies="false"
   * @generated
   */
  ModelElement getModelElement();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.base.Annotation#getModelElement <em>Model Element</em>}' container reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Model Element</em>' container reference.
   * @see #getModelElement()
   * @generated
   */
  void setModelElement(ModelElement value);

  /**
   * Returns the value of the '<em><b>Source</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Source</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Source</em>' attribute.
   * @see #setSource(String)
   * @see org.eclipse.oomph.base.BasePackage#getAnnotation_Source()
   * @model
   * @generated
   */
  String getSource();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.base.Annotation#getSource <em>Source</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Source</em>' attribute.
   * @see #getSource()
   * @generated
   */
  void setSource(String value);

  /**
   * Returns the value of the '<em><b>Details</b></em>' map.
   * The key is of type {@link java.lang.String},
   * and the value is of type {@link java.lang.String},
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Details</em>' map isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Details</em>' map.
   * @see org.eclipse.oomph.base.BasePackage#getAnnotation_Details()
   * @model mapType="org.eclipse.oomph.base.StringToStringMapEntry&lt;org.eclipse.emf.ecore.EString, org.eclipse.oomph.base.Text&gt;"
   *        extendedMetaData="name='detail'"
   * @generated
   */
  EMap<String, String> getDetails();

  /**
   * Returns the value of the '<em><b>Contents</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Contents</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Contents</em>' containment reference list.
   * @see org.eclipse.oomph.base.BasePackage#getAnnotation_Contents()
   * @model containment="true"
   *        extendedMetaData="name='content'"
   * @generated
   */
  EList<EObject> getContents();

  /**
   * Returns the value of the '<em><b>References</b></em>' reference list.
   * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>References</em>' reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>References</em>' reference list.
   * @see org.eclipse.oomph.base.BasePackage#getAnnotation_References()
   * @model extendedMetaData="name='reference'"
   * @generated
   */
  EList<EObject> getReferences();

} // Annotation
