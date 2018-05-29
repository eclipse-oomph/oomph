/*
 * Copyright (c) 2014, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
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
import org.eclipse.core.runtime.NullProgressMonitor;
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
public abstract class RepositoryProviderMap<M extends IRepositoryManager<T>, R extends IRepository<T>, T, P extends RepositoryProvider<M, R, T>>
{
  private final Map<URI, P> providers = new HashMap<URI, P>();

  private final M repositoryManager;

  public RepositoryProviderMap(M repositoryManager)
  {
    this.repositoryManager = repositoryManager;
  }

  public final M getRepositoryManager()
  {
    return repositoryManager;
  }

  public final P getRepositoryProvider(URI location)
  {
    P provider = providers.get(location);
    if (provider == null)
    {
      provider = createProvider(location);
    }

    return provider;
  }

  public final synchronized R getRepository(URI location)
  {
    return getRepository(location, new NullProgressMonitor());
  }

  public final synchronized R getRepository(URI location, IProgressMonitor monitor)
  {
    P provider = getRepositoryProvider(location);
    return provider.getRepository(monitor);
  }

  public final synchronized R createRepository(URI location, String name, String type, Map<String, String> properties)
  {
    P provider = createProvider(location);
    return provider.createRepository(name, type, properties);
  }

  public final P removeAllContent(URI location, IProgressMonitor monitor)
  {
    P provider = getRepositoryProvider(location);
    provider.removeAllContent(monitor);
    return provider;
  }

  public final P dispose(URI location)
  {
    P provider = providers.remove(location);
    if (provider != null)
    {
      provider.dispose();
    }

    return provider;
  }

  public final void dispose()
  {
    for (P provider : providers.values())
    {
      provider.dispose();
    }

    providers.clear();
  }

  public abstract RepositoryType getRepositoryType();

  protected abstract P createProvider(M repositoryManager, URI location);

  private P createProvider(URI location)
  {
    dispose(location);

    P provider = createProvider(repositoryManager, location);
    providers.put(location, provider);
    return provider;
  }

  /**
   * @author Eike Stepper
   */
  public static final class Metadata
      extends RepositoryProviderMap<IMetadataRepositoryManager, IMetadataRepository, IInstallableUnit, RepositoryProvider.Metadata>
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
    protected RepositoryProvider.Metadata createProvider(IMetadataRepositoryManager repositoryManager, URI location)
    {
      return new RepositoryProvider.Metadata(repositoryManager, location);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class Artifact extends RepositoryProviderMap<IArtifactRepositoryManager, IArtifactRepository, IArtifactKey, RepositoryProvider.Artifact>
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
    protected RepositoryProvider.Artifact createProvider(IArtifactRepositoryManager repositoryManager, URI location)
    {
      return new RepositoryProvider.Artifact(repositoryManager, location);
    }
  }
}
