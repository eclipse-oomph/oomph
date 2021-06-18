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
package org.eclipse.oomph.p2.core;

import org.eclipse.equinox.p2.core.UIServices.TrustInfo;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * A no-op implementation of a certificate store that can be used to determine if a certificate chain is trusted and to remember trusted certificates.
 *
 * @author Ed Merks
 */
public class CertificateConfirmer
{
  /**
   * @author Ed Merks
   */
  public static class TrustInfoWithPolicy extends TrustInfo
  {
    private final boolean trustAll;

    @SuppressWarnings("deprecation")
    public TrustInfoWithPolicy(Certificate[] trusted, boolean save, boolean trustUnsigned, boolean trustAll)
    {
      super(trusted, save, trustUnsigned);
      this.trustAll = trustAll;
    }

    public boolean isTrustAll()
    {
      return trustAll;
    }
  }

  public static final CertificateConfirmer ACCEPT = new CertificateConfirmer()
  {
    @Override
    public boolean isTrusted(Certificate[] certificateChain)
    {
      return true;
    }
  };

  public static final CertificateConfirmer DECLINE = new CertificateConfirmer()
  {
    @Override
    public boolean isDecline()
    {
      return true;
    }
  };

  public static final CertificateConfirmer ALWAYS_PROMPT = new CertificateConfirmer();

  /**
   * Returns {@code true} if and only if any certificate in the chain is already trusted.
   */
  public boolean isTrusted(Certificate[] certificateChain)
  {
    return false;
  }

  /**
   * Remembers all the certificates in the chain as trusted such that any subsequent call to {@link #isTrusted(Certificate[])}
   * with a chain that contains any one of the certificates in this chain returns {@code true}.
   * If called with {@code null}, all certificates will be trusted in the future.
   */
  public void trust(Certificate[] certificateChain)
  {
  }

  /**
   * Returns {@code true} if this confirmer will never accept an untrusted certificate chain.
   */
  public boolean isDecline()
  {
    return false;
  }

  /**
   * Returns the certificate chains not already trusted by the certificate confirmer.
   */
  public final Certificate[][] getUntrustedCertificates(Certificate[][] untrustedChains)
  {
    if (untrustedChains == null || untrustedChains.length == 0 || isDecline())
    {
      return untrustedChains;
    }

    List<Certificate[]> result = new ArrayList<Certificate[]>();
    for (Certificate[] untrustedChain : untrustedChains)
    {
      if (!isTrusted(untrustedChain))
      {
        result.add(untrustedChain);
      }
    }

    return result.toArray(new Certificate[result.size()][]);
  }
}
