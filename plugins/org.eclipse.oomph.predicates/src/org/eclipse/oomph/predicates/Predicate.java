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
package org.eclipse.oomph.predicates;

import org.eclipse.oomph.base.ModelElement;

import org.eclipse.core.resources.IResource;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Predicate</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.oomph.predicates.PredicatesPackage#getPredicate()
 * @model abstract="true"
 * @generated
 */
public interface Predicate extends ModelElement
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @model resourceDataType="org.eclipse.oomph.predicates.Resource"
   * @generated
   */
  boolean matches(IResource resource);

} // Predicate
