/*
 * Copyright (c) 2019 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup;

import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;

import java.io.ByteArrayInputStream;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 * @author Ed Merks
 */
public final class CertificateInfo extends Info
{
  public CertificateInfo(String string)
  {
    super(string);
  }

  public CertificateInfo(String uuid, String name)
  {
    super(uuid, name);
  }

  public CertificateInfo(Certificate certificate)
  {
    super(getUUID(certificate), getName(certificate));
  }

  private static String getUUID(Certificate certificate)
  {
    try
    {
      return XMLTypeFactory.eINSTANCE.convertBase64Binary(IOUtil.getSHA1(new ByteArrayInputStream(certificate.getEncoded())));
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

  private static String getName(Certificate certificate)
  {
    return ((X509Certificate)certificate).getSubjectDN().getName();
  }
}
