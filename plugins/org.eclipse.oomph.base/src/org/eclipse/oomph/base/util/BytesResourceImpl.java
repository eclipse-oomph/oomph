/*
 * Copyright (c) 2015 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.base.util;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author Ed Merks
 */
public class BytesResourceImpl extends ResourceImpl implements BytesResource
{
  public BytesResourceImpl(URI uri)
  {
    super(uri);
  }

  @Override
  protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException
  {
    ByteArrayOutputStream bytes = null;
    try
    {
      bytes = new ByteArrayOutputStream();
      IOUtil.copy(inputStream, bytes);
      String value = XMLTypeFactory.eINSTANCE.convertBase64Binary(bytes.toByteArray());
      Annotation annotation = BaseFactory.eINSTANCE.createAnnotation();
      annotation.setSource(value);
      getContents().add(annotation);
    }
    finally
    {
      IOUtil.closeSilent(bytes);
    }
  }

  @Override
  protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException
  {
    Annotation annotation = (Annotation)EcoreUtil.getObjectByType(getContents(), BasePackage.Literals.ANNOTATION);
    if (annotation != null)
    {
      String value = annotation.getSource();
      if (value != null)
      {
        outputStream.write(XMLTypeFactory.eINSTANCE.createBase64Binary(value));
      }
    }
  }
}
