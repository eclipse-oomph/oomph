/*
 * Copyright (c) 2017 Adrian Price and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Adrian Price <aprice@tibco.com> - initial API and implementation
 */
package org.eclipse.oomph.setup.jdt;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>JRE Library</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.jdt.JRELibrary#getLibraryPath <em>Library Path</em>}</li>
 *   <li>{@link org.eclipse.oomph.setup.jdt.JRELibrary#getExternalAnnotationsPath <em>External Annotations Path</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.jdt.JDTPackage#getJRELibrary()
 * @model
 * @generated
 */
public interface JRELibrary extends EObject
{
  /**
   * Returns the value of the '<em><b>Library Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Library Path</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * The path, relative to the JRE location, of the JRE library JAR.
   * <!-- end-model-doc -->
   * @return the value of the '<em>Library Path</em>' attribute.
   * @see #setLibraryPath(String)
   * @see org.eclipse.oomph.setup.jdt.JDTPackage#getJRELibrary_LibraryPath()
   * @model required="true"
   * @generated
   */
  String getLibraryPath();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.jdt.JRELibrary#getLibraryPath <em>Library Path</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Library Path</em>' attribute.
   * @see #getLibraryPath()
   * @generated
   */
  void setLibraryPath(String value);

  /**
   * Returns the value of the '<em><b>External Annotations Path</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>External Annotations Path</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * <!-- begin-model-doc -->
   * The absolute workspace path to a folder or ZIP file containing external nullability annotations for classes in the JRE library JAR.
   * <!-- end-model-doc -->
   * @return the value of the '<em>External Annotations Path</em>' attribute.
   * @see #setExternalAnnotationsPath(String)
   * @see org.eclipse.oomph.setup.jdt.JDTPackage#getJRELibrary_ExternalAnnotationsPath()
   * @model required="true"
   * @generated
   */
  String getExternalAnnotationsPath();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.jdt.JRELibrary#getExternalAnnotationsPath <em>External Annotations Path</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>External Annotations Path</em>' attribute.
   * @see #getExternalAnnotationsPath()
   * @generated
   */
  void setExternalAnnotationsPath(String value);

} // JRELibrary
