/*
 * Copyright (c) 2022 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.BasePackage;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.bouncycastle.openpgp.PGPPublicKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

/**
 * @author Ed Merks
 */
public class PGPKeyResourceImpl extends ResourceImpl
{
  private Set<PGPPublicKey> publicKeys;

  public PGPKeyResourceImpl(URI uri)
  {
    super(uri);
  }

  public Set<PGPPublicKey> getPublicKeys()
  {
    return publicKeys;
  }

  @SuppressWarnings("restriction")
  @Override
  protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException
  {
    String value = new String(inputStream.readAllBytes(), StandardCharsets.US_ASCII);
    publicKeys = org.eclipse.equinox.internal.p2.artifact.processors.pgp.PGPPublicKeyStore.readPublicKeys(value);
    Annotation annotation = BaseFactory.eINSTANCE.createAnnotation();
    annotation.setSource(value);
    getContents().add(annotation);
  }

  @Override
  protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException
  {
    String value = getValue();
    if (value != null)
    {
      outputStream.write(value.getBytes(StandardCharsets.US_ASCII));
    }
  }

  protected String getValue()
  {
    Annotation annotation = (Annotation)EcoreUtil.getObjectByType(getContents(), BasePackage.Literals.ANNOTATION);
    if (annotation != null)
    {
      return annotation.getSource();
    }
    return null;
  }
}
