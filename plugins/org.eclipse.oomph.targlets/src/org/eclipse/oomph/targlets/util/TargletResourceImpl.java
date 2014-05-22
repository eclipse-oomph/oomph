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
package org.eclipse.oomph.targlets.util;

import org.eclipse.oomph.targlets.TargletPackage;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * <!-- begin-user-doc -->
 * The <b>Resource </b> associated with the package.
 * <!-- end-user-doc -->
 * @see org.eclipse.oomph.targlets.util.TargletResourceFactoryImpl
 * @generated
 */
public class TargletResourceImpl extends XMIResourceImpl
{
  /**
   * Creates an instance of the resource.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param uri the URI of the new resource.
   * @generated
   */
  public TargletResourceImpl(URI uri)
  {
    super(uri);
  }

  private Map<?, ?> augmentOptions(Map<?, ?> options)
  {
    if (options != null && Boolean.TRUE.equals(options.get(OPTION_EXTENDED_META_DATA)))
    {
      HashMap<Object, Object> specializedOptions = new HashMap<Object, Object>(options);
      ResourceSet resourceSet = getResourceSet();

      specializedOptions.put(OPTION_EXTENDED_META_DATA,
          new BasicExtendedMetaData(resourceSet == null ? EPackage.Registry.INSTANCE : resourceSet.getPackageRegistry())
          {
            @Override
            public EPackage getPackage(String namespace)
            {
              if ("http://www.eclipse.org/CDO/releng/setup/1.0".equals(namespace))
              {
                return TargletPackage.eINSTANCE;
              }

              return super.getPackage(namespace);
            }
          });
      return specializedOptions;
    }

    return options;
  }

  @Override
  public void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException
  {
    super.doLoad(inputStream, augmentOptions(options));
  }

  @Override
  public void doLoad(InputSource inputSource, Map<?, ?> options) throws IOException
  {
    super.doLoad(inputSource, augmentOptions(options));
  }

  @Override
  public void doLoad(Node node, Map<?, ?> options) throws IOException
  {
    super.doLoad(node, augmentOptions(options));
  }

} // TargletResourceImpl
