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
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.jface.dialogs.IDialogSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class RepositoryManager
{
  public static final RepositoryManager INSTANCE = new RepositoryManager();

  private static final String REPOSITORIES_KEY = "repositories";

  private static final String ACTIVE_REPOSITORY_KEY = "activeRepository";

  private final IDialogSettings settings;

  private final List<RepositoryManagerListener> listeners = new ArrayList<RepositoryManagerListener>();

  private final LinkedList<String> repositories = new LinkedList<String>();

  private String currentProfileLocation;

  private String activeRepository;

  private RepositoryManager()
  {
    try
    {
      Profile currentProfile = P2Util.getAgentManager().getCurrentAgent().getCurrentProfile();
      currentProfileLocation = URI.createFileURI(currentProfile.getLocation().toString()).toString();
    }
    catch (Throwable throwable)
    {
      // Ignore.
    }

    settings = P2UIPlugin.INSTANCE.getDialogSettings(getClass().getSimpleName());

    String[] array = settings.getArray(REPOSITORIES_KEY);
    if (array != null)
    {
      repositories.addAll(Arrays.asList(array));
    }

    if (currentProfileLocation != null)
    {
      repositories.add(currentProfileLocation);
    }

    activeRepository = normalize(settings.get(ACTIVE_REPOSITORY_KEY));
  }

  private String normalize(String repository)
  {
    if (repository != null && repository.length() == 0)
    {
      return null;
    }

    return repository;
  }

  private void rememberRepositories()
  {
    String[] array;

    List<String> filteredRepositories = new ArrayList<String>(repositories);
    filteredRepositories.remove(currentProfileLocation);
    int size = filteredRepositories.size();
    if (size != 0)
    {
      array = filteredRepositories.toArray(new String[size]);
    }
    else
    {
      array = null;
    }

    settings.put(REPOSITORIES_KEY, array);
  }

  private void notifyListeners(String repository)
  {
    RepositoryManagerListener[] array;
    synchronized (listeners)
    {
      array = listeners.toArray(new RepositoryManagerListener[listeners.size()]);
    }

    for (int i = 0; i < array.length; i++)
    {
      RepositoryManagerListener listener = array[i];

      try
      {
        if (repository == null)
        {
          listener.repositoriesChanged(this);
        }
        else
        {
          listener.activeRepositoryChanged(this, repository);
        }
      }
      catch (Exception ex)
      {
        P2UIPlugin.INSTANCE.log(ex);
      }
    }
  }

  public void addListener(RepositoryManagerListener listener)
  {
    synchronized (listeners)
    {
      listeners.add(listener);
    }
  }

  public void removeListener(RepositoryManagerListener listener)
  {
    synchronized (listeners)
    {
      listeners.remove(listener);
    }
  }

  public String[] getRepositories()
  {
    synchronized (repositories)
    {
      return repositories.toArray(new String[repositories.size()]);
    }
  }

  public boolean addRepository(String repository)
  {
    boolean changed = false;
    synchronized (repositories)
    {
      if (repositories.size() == 0 || !repositories.getFirst().equals(repository))
      {
        repositories.remove(repository);
        repositories.addFirst(repository);

        rememberRepositories();
        changed = true;
      }
    }

    if (changed)
    {
      notifyListeners(null);
    }

    return changed;
  }

  public boolean removeRepository(String repository)
  {
    boolean changed = false;
    synchronized (repositories)
    {
      if (repositories.remove(repository))
      {
        rememberRepositories();
        changed = true;
      }
    }

    if (changed)
    {
      notifyListeners(null);
    }

    return changed;
  }

  public String getActiveRepository()
  {
    return activeRepository;
  }

  public boolean setActiveRepository(String repository)
  {
    repository = normalize(repository);

    if (!ObjectUtil.equals(activeRepository, repository))
    {
      if (repository != null)
      {
        addRepository(repository);
      }

      synchronized (this)
      {
        activeRepository = repository;
        settings.put(ACTIVE_REPOSITORY_KEY, repository);
      }

      notifyListeners(repository);
      return true;
    }

    return false;
  }

  /**
   * @author Eike Stepper
   */
  public interface RepositoryManagerListener
  {
    public void repositoriesChanged(RepositoryManager repositoryManager);

    public void activeRepositoryChanged(RepositoryManager repositoryManager, String repository);
  }
}
