/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.resources;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Eclipse Project Factory</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.oomph.resources.ResourcesPackage#getEclipseProjectFactory()
 * @model
 * @generated
 */
public interface EclipseProjectFactory extends XMLProjectFactory
{
  public static final EList<ProjectFactory> LIST = ECollections.singletonEList((ProjectFactory)ResourcesFactory.eINSTANCE.createEclipseProjectFactory());

} // EclipseProjectFactory
