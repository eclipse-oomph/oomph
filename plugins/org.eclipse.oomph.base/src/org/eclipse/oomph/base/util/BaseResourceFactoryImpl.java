/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.base.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;

import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource Factory</b> associated with the package.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.base.util.BaseResourceImpl
 * @generated
 */
public class BaseResourceFactoryImpl extends ResourceFactoryImpl
{
  /**
   * Creates an instance of the resource factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public BaseResourceFactoryImpl()
  {
    super();
  }

  /**
   * Creates an instance of the resource.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public Resource createResource(URI uri)
  {
    BaseResourceImpl result = new BaseResourceImpl(uri);
    result.setEncoding("UTF-8");

    Map<Object, Object> defaultLoadOptions = result.getDefaultLoadOptions();
    defaultLoadOptions.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE, Boolean.TRUE);
    defaultLoadOptions.put(XMLResource.OPTION_RECORD_ANY_TYPE_NAMESPACE_DECLARATIONS, Boolean.TRUE);
    defaultLoadOptions.put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);
    defaultLoadOptions.put(XMLResource.OPTION_LAX_FEATURE_PROCESSING, Boolean.TRUE);
    defaultLoadOptions.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
    defaultLoadOptions.put(XMLResource.OPTION_USE_PACKAGE_NS_URI_AS_LOCATION, Boolean.FALSE);

    Map<Object, Object> defaultSaveOptions = result.getDefaultSaveOptions();
    defaultSaveOptions.put(XMLResource.OPTION_SKIP_ESCAPE_URI, Boolean.FALSE);
    defaultSaveOptions.put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.TRUE);
    defaultSaveOptions.put(XMLResource.OPTION_LINE_WIDTH, 10);
    defaultSaveOptions.put(XMLResource.OPTION_SCHEMA_LOCATION, Boolean.TRUE);

    return result;
  }

} // SetupResourceFactoryImpl
