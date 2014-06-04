/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.workbench;

import org.eclipse.oomph.base.ModelElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Key Binding Context</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.oomph.setup.workbench.KeyBindingContext#getID <em>ID</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getKeyBindingContext()
 * @model
 * @generated
 */
public interface KeyBindingContext extends ModelElement
{
  /**
   * Returns the value of the '<em><b>ID</b></em>' attribute.
   * The default value is <code>"org.eclipse.ui.contexts.window"</code>.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>ID</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>ID</em>' attribute.
   * @see #setID(String)
   * @see org.eclipse.oomph.setup.workbench.WorkbenchPackage#getKeyBindingContext_ID()
   * @model default="org.eclipse.ui.contexts.window"
   *        extendedMetaData="kind='attribute' name='id'"
   * @generated
   */
  String getID();

  /**
   * Sets the value of the '{@link org.eclipse.oomph.setup.workbench.KeyBindingContext#getID <em>ID</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>ID</em>' attribute.
   * @see #getID()
   * @generated
   */
  void setID(String value);

} // KeyBindingContext
