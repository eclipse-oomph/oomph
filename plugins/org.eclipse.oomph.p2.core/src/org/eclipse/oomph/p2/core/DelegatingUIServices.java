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

import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.Confirmer.Confirmation;

import org.eclipse.equinox.p2.core.UIServices;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ed Merks
 */
public abstract class DelegatingUIServices extends UIServices
{
  protected abstract UIServices getDelegate();

  protected abstract Confirmer getUnsignedContentConfirmer();

  protected abstract CertificateConfirmer getCertificateConfirmer();

  @Override
  public AuthenticationInfo getUsernamePassword(String location)
  {
    UIServices delegate = getDelegate();
    return delegate == null ? null : delegate.getUsernamePassword(location);
  }

  @Override
  public AuthenticationInfo getUsernamePassword(String location, AuthenticationInfo previousInfo)
  {
    UIServices delegate = getDelegate();
    return delegate == null ? null : delegate.getUsernamePassword(location, previousInfo);
  }

  @Override
  public TrustInfo getTrustInfo(Certificate[][] untrustedChains, String[] unsignedDetail)
  {
    if (unsignedDetail != null && unsignedDetail.length != 0)
    {
      Confirmer unsignedContentConfirmer = getUnsignedContentConfirmer();
      if (unsignedContentConfirmer != null)
      {
        Confirmation confirmation = unsignedContentConfirmer.confirm(true, unsignedDetail);
        if (!confirmation.isConfirmed())
        {
          return new TrustInfo(new Certificate[0], false, false);
        }

        // We've checked trust in unsigned content already; prevent delegate from checking it again.
        unsignedDetail = null;
      }
    }

    List<Certificate> trusted = new ArrayList<Certificate>();
    if (untrustedChains != null && untrustedChains.length > 0)
    {
      CertificateConfirmer certificateConfirmer = getCertificateConfirmer();
      if (certificateConfirmer != null)
      {
        if (certificateConfirmer.isDecline())
        {
          return new TrustInfo(new Certificate[0], false, false);
        }

        for (Certificate[] untrustedChain : untrustedChains)
        {
          trusted.add(untrustedChain[0]);
        }

        Certificate[][] untrustedCertificates = certificateConfirmer.getUntrustedCertificates(untrustedChains);
        for (Certificate[] untrustedChain : untrustedCertificates)
        {
          trusted.remove(untrustedChain[0]);
        }

        // We've checked not confirmed the trust in these remaining certificate chains, so only check these;
        untrustedChains = untrustedCertificates;
      }
    }

    UIServices delegate = getDelegate();
    TrustInfo trustInfo = delegate == null ? null : delegate.getTrustInfo(untrustedChains, unsignedDetail);
    if (trustInfo != null)
    {
      if (!trustInfo.trustUnsignedContent())
      {
        return trustInfo;
      }

      Certificate[] trustedCertificates = trustInfo.getTrustedCertificates();
      if (trustedCertificates != null && trustedCertificates.length != 0)
      {
        CertificateConfirmer certificateConfirmer = getCertificateConfirmer();
        if (certificateConfirmer != null && trustInfo instanceof CertificateConfirmer.TrustInfoWithPolicy
            && ((CertificateConfirmer.TrustInfoWithPolicy)trustInfo).isTrustAll())
        {
          // Trust all certificates from now on.
          certificateConfirmer.trust(null);

          // Don't both recording certificates.
          certificateConfirmer = null;
        }

        for (Certificate certificate : trustedCertificates)
        {
          trusted.add(certificate);
          if (certificateConfirmer != null)
          {
            for (Certificate[] untrustedChain : untrustedChains)
            {
              if (untrustedChain[0] == certificate)
              {
                certificateConfirmer.trust(untrustedChain);
              }
            }
          }
        }
      }
    }

    return new TrustInfo(trusted.toArray(new Certificate[trusted.size()]), trustInfo.persistTrust(), true);
  }
}
