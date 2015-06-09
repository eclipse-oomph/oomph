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
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.base.ModelElement#getAnnotations <em>Annotations</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.base.BasePackage#getModelElement()
 * @model abstract="true"
 * @generated
 */
public interface ModelElement extends EObject
{
  /**
   * Returns the value of the '<em><b>Annotations</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.base.Annotation}.
   * It is bidirectional and its opposite is '{@link org.eclipse.oomph.base.Annotation#getModelElement <em>Model Element</em>}'.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Annotations</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Annotations</em>' containment reference list.
   * @see org.eclipse.oomph.base.BasePackage#getModelElement_Annotations()
   * @see org.eclipse.oomph.base.Annotation#getModelElement
   * @model opposite="modelElement" containment="true"
   *        extendedMetaData="name='annotation'"
   * @generated
   */
  EList<Annotation> getAnnotations();

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model
   * @generated
   */
  Annotation getAnnotation(String source);

} // ModelElement
