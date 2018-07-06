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
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.util.Request;

/**
 * @author Eike Stepper
 */
public abstract class JREDownloadHandler extends Request.Handler.Modifier
{
  public JREDownloadHandler()
  {
    super(Request.Handler.SYSTEM_BROWSER);
  }

  @Override
  protected void modify(Request request)
  {
    Product product = getProduct();
    request.put("pn", product.getLabel());

    Annotation annotation = product.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
    if (annotation != null)
    {
      String uri = annotation.getDetails().get(AnnotationConstants.KEY_URI);
      if (uri != null)
      {
        request.put("pu", uri);
      }

      String imageURI = annotation.getDetails().get(AnnotationConstants.KEY_IMAGE_URI);
      if (imageURI != null)
      {
        request.put("pi", imageURI);
      }
    }
  }

  protected abstract Product getProduct();
}
