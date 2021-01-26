/*
 * Copyright (c) 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.core.CertificateConfirmer;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.core.ProfileTransaction.CommitContext;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.p2.impl.P2TaskImpl;
import org.eclipse.oomph.setup.ui.wizards.ProgressPage;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.util.Confirmer;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;

/**
 * @author Eike Stepper
 */
public class SelfCommitContext extends CommitContext
{
  private final User user;

  private boolean repositoryChanged;

  private IProvisioningPlan provisioningPlan;

  public SelfCommitContext(User user) throws CoreException
  {
    this.user = user;
  }

  public ProfileTransaction migrateProfile(ProfileTransaction transaction) throws CoreException
  {
    repositoryChanged = SetupUtil.INSTALLER_APPLICATION && changeRepositoryIfNeeded(transaction.getProfileDefinition());
    return transaction;
  }

  @Override
  public boolean handleProvisioningPlan(ResolutionInfo info) throws CoreException
  {
    if (repositoryChanged && info.getIUDeltas().isEmpty() && info.getPropertyDeltas().size() <= 1)
    {
      // Cancel if only the repository addition would be committed.
      return false;
    }

    provisioningPlan = info.getProvisioningPlan();
    return true;
  }

  @Override
  public Confirmer getUnsignedContentConfirmer()
  {
    if (user == null)
    {
      return Confirmer.ACCEPT;
    }

    P2TaskImpl.processLicenses(provisioningPlan, ProgressPage.LICENSE_CONFIRMER, user, true, new NullProgressMonitor());

    return UnsignedContentDialog.createUnsignedContentConfirmer(user, true);
  }

  @Override
  public CertificateConfirmer getCertficateConfirmer()
  {
    if (user == null)
    {
      return CertificateConfirmer.ACCEPT;
    }

    return SetupCoreUtil.createCertificateConfirmer(user, true);
  }

  private boolean changeRepositoryIfNeeded(ProfileDefinition profileDefinition)
  {
    EList<Repository> repositories = profileDefinition.getRepositories();
    URIConverter uriConverter = SetupCoreUtil.createResourceSet().getURIConverter();

    String installerUpdateURL = uriConverter.normalize(URI.createURI(SetupUtil.INSTALLER_UPDATE_URL)).toString();
    Repository repository = P2Factory.eINSTANCE.createRepository(installerUpdateURL);

    if (repositories.isEmpty())
    {
      repositories.add(repository);
      return true;
    }

    if (!installerUpdateURL.equals(repositories.get(0).getURL()))
    {
      repositories.set(0, repository);
      return true;
    }

    return false;
  }
}
