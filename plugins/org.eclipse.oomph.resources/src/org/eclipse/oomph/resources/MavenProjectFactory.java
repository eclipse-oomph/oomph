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
 * A representation of the model object '<em><b>Maven Project Factory</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see org.eclipse.oomph.resources.ResourcesPackage#getMavenProjectFactory()
 * @model
 * @generated
 */
public interface MavenProjectFactory extends XMLProjectFactory
{
  public static final EList<ProjectFactory> LIST = ECollections.singletonEList(ResourcesFactory.eINSTANCE.createMavenProjectFactory());

  public static final String POM_XML = "pom.xml"; //$NON-NLS-1$

} // MavenProjectFactory
