/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.core;

import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.Pair;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.engine.IPhaseSet;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.engine.PhaseSetFactory;
import org.eclipse.equinox.p2.engine.ProvisioningContext;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.planner.IProfileChangeRequest;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public interface ProfileTransaction
{
  public Profile getProfile();

  public ProfileDefinition getProfileDefinition();

  public String getProfileProperty(String key);

  public ProfileTransaction setProfileProperty(String key, String value);

  public ProfileTransaction removeProfileProperty(String key);

  public String getInstallableUnitProperty(IInstallableUnit iu, String key);

  public ProfileTransaction setInstallableUnitProperty(IInstallableUnit iu, String key, String value);

  public ProfileTransaction removeInstallableUnitProperty(IInstallableUnit iu, String key);

  public boolean isRemoveExistingInstallableUnits();

  public ProfileTransaction setRemoveExistingInstallableUnits(boolean removeAll);

  public boolean isMirrors();

  public ProfileTransaction setMirrors(boolean mirrors);

  public boolean isDirty();

  public boolean commit() throws CoreException;

  public boolean commit(IProgressMonitor monitor) throws CoreException;

  public boolean commit(CommitContext commitContext, IProgressMonitor monitor) throws CoreException;

  public Resolution resolve(IProgressMonitor monitor) throws CoreException;

  public Resolution resolve(CommitContext commitContext, IProgressMonitor monitor) throws CoreException;

  /**
   * Customizes the resolution and commit process of a {@link ProfileTransaction profile transaction}.
   *
   * The methods of this callback class are called in the order in which they're declared.
   *
   * @author Eike Stepper
   */
  public static class CommitContext
  {
    /**
     * Called early during {@link ProfileTransaction#resolve(CommitContext, IProgressMonitor) resolve}.
     */
    public ProvisioningContext createProvisioningContext(ProfileTransaction transaction, IProfileChangeRequest profileChangeRequest) throws CoreException
    {
      return new ProvisioningContext(transaction.getProfile().getAgent().getProvisioningAgent());
    }

    /**
     * Called late during {@link ProfileTransaction#resolve(CommitContext, IProgressMonitor) resolve}.
     */
    public boolean handleProvisioningPlan(ResolutionInfo info) throws CoreException
    {
      return true;
    }

    /**
     * Called early during {@link Resolution#commit(IProgressMonitor) commit}.
     */
    public IPhaseSet getPhaseSet(ProfileTransaction transaction) throws CoreException
    {
      return PhaseSetFactory.createDefaultPhaseSet();
    }

    /**
     * Called early during {@link Resolution#commit(IProgressMonitor) commit}.
     */
    public Confirmer getUnsignedContentConfirmer()
    {
      return null;
    }

    /**
     * Called late during {@link Resolution#commit(IProgressMonitor) commit}.
     */
    public void handleExecutionResult(IStatus status)
    {
      // Subclasses may override.
    }

    /**
     * @author Eike Stepper
     */
    public interface ResolutionInfo
    {
      public IProvisioningPlan getProvisioningPlan();

      public IInstallableUnit getArtificialRoot();

      public Map<IInstallableUnit, DeltaType> getIUDeltas();

      public Map<IInstallableUnit, Map<String, Pair<Object, Object>>> getPropertyDeltas();

      public List<IMetadataRepository> getMetadataRepositories();
    }

    /**
     * @author Ed Merks
     */
    public enum DeltaType
    {
      REMOVAL, ADDITION
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface Resolution
  {
    public ProfileTransaction getProfileTransaction();

    public IProvisioningPlan getProvisioningPlan();

    public boolean commit(IProgressMonitor monitor) throws CoreException;

    public void rollback();
  }
}
