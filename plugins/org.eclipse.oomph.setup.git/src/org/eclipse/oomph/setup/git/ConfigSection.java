/*
 * Copyright (c) 2015 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.git;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Config Section</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.git.ConfigSection#getSubsections <em>Subsections</em>}</li>
 * </ul>
 *
 * @see org.eclipse.oomph.setup.git.GitPackage#getConfigSection()
 * @model
 * @generated
 */
public interface ConfigSection extends ConfigSubsection
{
  /**
   * Returns the value of the '<em><b>Subsections</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.oomph.setup.git.ConfigSubsection}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Subsections</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Subsections</em>' containment reference list.
   * @see org.eclipse.oomph.setup.git.GitPackage#getConfigSection_Subsections()
   * @model containment="true"
   * @generated
   */
  EList<ConfigSubsection> getSubsections();

} // ConfigSection
