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
import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Base64;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ed Merks
 */
public class X509CertificateResourceImpl extends ResourceImpl
{
  private static Pattern PEM_PATTERN = Pattern.compile("-----BEGIN CERTIFICATE-----\r?\n(.*)\r?\n-----END CERTIFICATE-----\r?\n?", Pattern.DOTALL); //$NON-NLS-1$

  private Certificate certificate;

  public X509CertificateResourceImpl(URI uri)
  {
    super(uri);
  }

  public Certificate getCertificate()
  {
    return certificate;
  }

  @Override
  protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException
  {
    try
    {
      byte[] bytes = inputStream.readAllBytes();
      String value = new String(bytes, StandardCharsets.US_ASCII);
      Matcher matcher = PEM_PATTERN.matcher(value);
      CertificateFactory certificateFactory;
      certificateFactory = CertificateFactory.getInstance("X.509"); //$NON-NLS-1$

      Annotation annotation = BaseFactory.eINSTANCE.createAnnotation();
      if (matcher.matches())
      {
        String base64 = matcher.group(1);
        byte[] certificate = Base64.getMimeDecoder().decode(base64);
        this.certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(certificate));
        annotation.setSource(value);
      }
      else
      {
        certificate = certificateFactory.generateCertificate(new ByteArrayInputStream(bytes));
        annotation.setSource(XMLTypeFactory.eINSTANCE.convertBase64Binary(bytes));
      }

      getContents().add(annotation);
    }
    catch (CertificateException ex)
    {
      throw new IOException(ex);
    }
  }

  @Override
  protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException
  {
    String value = getValue();
    if (value != null)
    {
      if (PEM_PATTERN.matcher(value).matches())
      {
        outputStream.write(value.getBytes(StandardCharsets.US_ASCII));
      }
      else
      {
        outputStream.write(XMLTypeFactory.eINSTANCE.createBase64Binary(value));
      }
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
