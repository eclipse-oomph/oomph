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

import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public abstract class RepositoryProviderMap<M extends IRepositoryManager<T>, R extends IRepository<T>, T>
{
  private final Map<URI, RepositoryProvider<M, R, T>> providers = new HashMap<URI, RepositoryProvider<M, R, T>>();

  private final M repositoryManager;

  public RepositoryProviderMap(M repositoryManager)
  {
    this.repositoryManager = repositoryManager;
  }

  public final M getRepositoryManager()
  {
    return repositoryManager;
  }

  public final synchronized R getRepository(URI location)
  {
    RepositoryProvider<M, R, T> provider = providers.get(location);
    if (provider == null)
    {
      provider = createProvider(repositoryManager, location);
      providers.put(location, provider);
    }

    return provider.getRepository();
  }

  public final void dispose()
  {
    for (RepositoryProvider<M, R, T> provider : providers.values())
    {
      provider.dispose();
    }

    providers.clear();
  }

  protected abstract RepositoryProvider<M, R, T> createProvider(M repositoryManager, URI location);

  /**
   * @author Eike Stepper
   */
  public static final class Metadata extends RepositoryProviderMap<IMetadataRepositoryManager, IMetadataRepository, IInstallableUnit>
  {
    public Metadata(IMetadataRepositoryManager repositoryManager)
    {
      super(repositoryManager);
    }

    @Override
    protected RepositoryProvider<IMetadataRepositoryManager, IMetadataRepository, IInstallableUnit> createProvider(
        IMetadataRepositoryManager repositoryManager, URI location)
    {
      return new RepositoryProvider.Metadata(repositoryManager, location);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class Artifact extends RepositoryProviderMap<IArtifactRepositoryManager, IArtifactRepository, IArtifactKey>
  {
    public Artifact(IArtifactRepositoryManager repositoryManager)
    {
      super(repositoryManager);
    }

    @Override
    protected RepositoryProvider<IArtifactRepositoryManager, IArtifactRepository, IArtifactKey> createProvider(IArtifactRepositoryManager repositoryManager,
        URI location)
    {
      return new RepositoryProvider.Artifact(repositoryManager, location);
    }
  }
}
