/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.core;

import org.eclipse.oomph.p2.P2Exception;

import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.net.URI;

/**
 * @author Eike Stepper
 */
public abstract class RepositoryProvider<M extends IRepositoryManager<T>, R extends IRepository<T>, T>
{
  private final M repositoryManager;

  private final URI location;

  private R repository;

  private boolean existing;

  public RepositoryProvider(M repositoryManager, URI location)
  {
    this.repositoryManager = repositoryManager;
    this.location = location;
  }

  public final M getRepositoryManager()
  {
    return repositoryManager;
  }

  public final URI getLocation()
  {
    return location;
  }

  public final synchronized R getRepository()
  {
    if (repository == null)
    {
      try
      {
        existing = repositoryManager.contains(location);
        repository = loadRepository(repositoryManager, location);
      }
      catch (ProvisionException ex)
      {
        throw new P2Exception(ex);
      }
    }

    return repository;
  }

  public final boolean isExisting()
  {
    return existing;
  }

  public final void dispose()
  {
    if (repository != null && !existing)
    {
      repositoryManager.removeRepository(location);
    }
  }

  protected abstract R loadRepository(M repositoryManager, URI location) throws ProvisionException, OperationCanceledException;

  /**
   * @author Eike Stepper
   */
  public static final class Metadata extends RepositoryProvider<IMetadataRepositoryManager, IMetadataRepository, IInstallableUnit>
  {
    public Metadata(IMetadataRepositoryManager repositoryManager, URI location)
    {
      super(repositoryManager, location);
    }

    @Override
    protected IMetadataRepository loadRepository(IMetadataRepositoryManager repositoryManager, URI location) throws ProvisionException,
        OperationCanceledException
    {
      return repositoryManager.loadRepository(location, null);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class Artifact extends RepositoryProvider<IArtifactRepositoryManager, IArtifactRepository, IArtifactKey>
  {
    public Artifact(IArtifactRepositoryManager repositoryManager, URI location)
    {
      super(repositoryManager, location);
    }

    @Override
    protected IArtifactRepository loadRepository(IArtifactRepositoryManager repositoryManager, URI location) throws ProvisionException,
        OperationCanceledException
    {
      return repositoryManager.loadRepository(location, null);
    }
  }
}
