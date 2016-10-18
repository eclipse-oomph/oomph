/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.core.ProfileTransaction.CommitContext;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.p2.impl.P2TaskImpl;
import org.eclipse.oomph.setup.ui.wizards.ProgressPage;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.VersionRange;

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
    boolean installer = false;

    // TODO Remove this temporary range conversion when all users can be expected to have a major range.
    String productID = PropertiesUtil.getProductID();
    VersionRange deprecatedVersionRange = new VersionRange("[1.0.0,1.1.0)");
    ProfileDefinition profileDefinition = transaction.getProfileDefinition();
    for (Requirement requirement : profileDefinition.getRequirements())
    {
      if (IInstallableUnit.NAMESPACE_IU_ID.equals(requirement.getNamespace()) && productID.equals(requirement.getName()))
      {
        installer = true;
        if (deprecatedVersionRange.equals(requirement.getVersionRange()))
        {
          requirement.setVersionRange(new VersionRange("[1.0.0,2.0.0)"));
          transaction.commit(new NullProgressMonitor());

          Profile profile = transaction.getProfile();
          transaction = profile.change();
        }

        break;
      }
    }

    repositoryChanged = installer ? changeRepositoryIfNeeded(profileDefinition) : false;
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
