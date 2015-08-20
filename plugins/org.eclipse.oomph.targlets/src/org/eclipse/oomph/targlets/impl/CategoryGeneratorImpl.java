/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.impl;

import org.eclipse.oomph.targlets.CategoryGenerator;
import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.internal.p2.repository.Transport;
import org.eclipse.equinox.internal.p2.updatesite.UpdateSite;
import org.eclipse.equinox.p2.core.ProvisionException;

import java.net.URI;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Category Generator</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class CategoryGeneratorImpl extends SiteGeneratorImpl implements CategoryGenerator
{
  private static final IPath CATEGORY_XML_PATH = new Path("category.xml");

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CategoryGeneratorImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return TargletPackage.Literals.CATEGORY_GENERATOR;
  }

  @Override
  protected IPath getPath()
  {
    return CATEGORY_XML_PATH;
  }

  @Override
  protected UpdateSite loadFile(URI location, Transport transport) throws ProvisionException
  {
    return UpdateSite.loadCategoryFile(location, transport, new NullProgressMonitor());
  }

} // CategoryGeneratorImpl
