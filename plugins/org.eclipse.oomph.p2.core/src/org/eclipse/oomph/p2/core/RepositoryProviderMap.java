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

import org.eclipse.oomph.p2.RepositoryType;

import org.eclipse.core.runtime.IProgressMonitor;
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
    RepositoryProvider<M, R, T> provider = getProvider(location);
    return provider.getRepository();
  }

  public final synchronized R createRepository(URI location, String name, String type, Map<String, String> properties)
  {
    RepositoryProvider<M, R, T> provider = createProvider(location);
    return provider.createRepository(name, type, properties);
  }

  public final void removeAllContent(URI location, IProgressMonitor monitor)
  {
    RepositoryProvider<M, R, T> provider = getProvider(location);
    provider.removeAllContent(monitor);
  }

  public final void dispose(URI location)
  {
    RepositoryProvider<M, R, T> provider = providers.remove(location);
    if (provider != null)
    {
      provider.dispose();
    }
  }

  public final void dispose()
  {
    for (RepositoryProvider<M, R, T> provider : providers.values())
    {
      provider.dispose();
    }

    providers.clear();
  }

  public abstract RepositoryType getRepositoryType();

  protected abstract RepositoryProvider<M, R, T> createProvider(M repositoryManager, URI location);

  private RepositoryProvider<M, R, T> createProvider(URI location)
  {
    dispose(location);

    RepositoryProvider<M, R, T> provider = createProvider(repositoryManager, location);
    providers.put(location, provider);
    return provider;
  }

  private RepositoryProvider<M, R, T> getProvider(URI location)
  {
    RepositoryProvider<M, R, T> provider = providers.get(location);
    if (provider == null)
    {
      provider = createProvider(location);
    }

    return provider;
  }

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
    public RepositoryType getRepositoryType()
    {
      return RepositoryType.METADATA;
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
    public RepositoryType getRepositoryType()
    {
      return RepositoryType.ARTIFACT;
    }

    @Override
    protected RepositoryProvider<IArtifactRepositoryManager, IArtifactRepository, IArtifactKey> createProvider(IArtifactRepositoryManager repositoryManager,
        URI location)
    {
      return new RepositoryProvider.Artifact(repositoryManager, location);
    }
  }
}
