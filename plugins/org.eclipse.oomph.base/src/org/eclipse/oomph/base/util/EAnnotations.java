/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.base.util;

import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * @author Eike Stepper
 */
public final class EAnnotations
{
  private static final String ANNOTATION_LABEL_PROVIDER = "http://www.eclipse.org/oomph/base/LabelProvider"; //$NON-NLS-1$

  private static final String KEY_IMAGE_BASE_URI = "imageBaseURI"; //$NON-NLS-1$

  private static final String KEY_IMAGE_URI = "imageURI"; //$NON-NLS-1$

  private static final String KEY_TEXT = "text"; //$NON-NLS-1$

  private EAnnotations()
  {
  }

  private static URI getImageBaseURI(EClass eClass)
  {
    EPackage ePackage = eClass.getEPackage();
    String uri = EcoreUtil.getAnnotation(ePackage, ANNOTATION_LABEL_PROVIDER, KEY_IMAGE_BASE_URI);
    if (!StringUtil.isEmpty(uri))
    {
      return URI.createURI(uri);
    }

    return null;
  }

  public static URI getImageURI(EClass eClass)
  {
    String uri = EcoreUtil.getAnnotation(eClass, ANNOTATION_LABEL_PROVIDER, KEY_IMAGE_URI);
    if (!StringUtil.isEmpty(uri))
    {
      URI imageURI = URI.createURI(uri);
      if (imageURI.isRelative())
      {
        URI imageBaseURI = getImageBaseURI(eClass);
        if (imageBaseURI != null)
        {
          return imageURI.resolve(imageBaseURI);
        }
      }

      return imageURI;
    }

    URI imageBaseURI = getImageBaseURI(eClass);
    if (imageBaseURI != null)
    {
      return imageBaseURI.appendSegment(eClass.getName() + ".gif"); //$NON-NLS-1$
    }

    return null;
  }

  public static String getText(EClass eClass)
  {
    return EcoreUtil.getAnnotation(eClass, ANNOTATION_LABEL_PROVIDER, KEY_TEXT);
  }
}
