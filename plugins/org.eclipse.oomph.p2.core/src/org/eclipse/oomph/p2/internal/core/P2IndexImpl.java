/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectInputStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.equinox.p2.metadata.Version;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Eike Stepper
 */
public class P2IndexImpl implements P2Index
{
  public static final P2IndexImpl INSTANCE = new P2IndexImpl();

  private static final String INDEX_BASE = "http://download.eclipse.org/oomph/index/";

  private long timeStamp;

  private Map<Integer, RepositoryImpl> repositories;

  private Repository[] repositoriesArray;

  private Map<String, Set<String>> capabilitiesMap;

  private File repositoriesCacheFile;

  private File capabilitiesCacheFile;

  private P2IndexImpl()
  {
  }

  private synchronized void initCapabilities()
  {
    if (capabilitiesMap == null)
    {
      capabilitiesMap = new LinkedHashMap<String, Set<String>>();

      ZipFile zipFile = null;
      InputStream inputStream = null;

      try
      {
        boolean refreshed = initCapabilitiesCacheFile();

        zipFile = new ZipFile(capabilitiesCacheFile);
        ZipEntry zipEntry = zipFile.getEntry("capabilities");

        inputStream = zipFile.getInputStream(zipEntry);

        Map<Object, Object> options = new HashMap<Object, Object>();
        options.put(BinaryResourceImpl.OPTION_VERSION, BinaryResourceImpl.BinaryIO.Version.VERSION_1_1);
        options.put(BinaryResourceImpl.OPTION_STYLE_DATA_CONVERTER, Boolean.TRUE);
        options.put(BinaryResourceImpl.OPTION_BUFFER_CAPACITY, 8192);

        EObjectInputStream stream = new BinaryResourceImpl.EObjectInputStream(inputStream, options);
        int refreshHours = stream.readInt();
        int mapSize = stream.readCompressedInt();
        for (int i = 0; i < mapSize; ++i)
        {
          String key = stream.readSegmentedString();
          int valuesSize = stream.readCompressedInt();
          for (int j = 0; j < valuesSize; ++j)
          {
            String value = stream.readSegmentedString();
            CollectionUtil.add(capabilitiesMap, key, value);
          }
        }

        if (refreshed)
        {
          File validityFile = getCapabilitiesValidityFile();
          IOUtil.writeLines(validityFile, "UTF-8", Collections.singletonList("" + (System.currentTimeMillis() + refreshHours * 60 * 60 * 1000)));
        }
      }
      catch (Exception ex)
      {
        P2CorePlugin.INSTANCE.log(ex, IStatus.WARNING);
      }
      finally
      {
        IOUtil.closeSilent(inputStream);
        if (zipFile != null)
        {
          try
          {
            zipFile.close();
          }
          catch (IOException ex)
          {
            P2CorePlugin.INSTANCE.log(ex, IStatus.WARNING);
          }
        }
      }
    }
  }

  private synchronized void initRepositories(boolean force)
  {
    if (repositories == null || force)
    {
      repositories = new HashMap<Integer, RepositoryImpl>();

      ZipFile zipFile = null;
      InputStream inputStream = null;

      try
      {
        boolean refreshed = initRepositoriesCacheFile();

        zipFile = new ZipFile(repositoriesCacheFile);
        ZipEntry zipEntry = zipFile.getEntry("repositories");

        inputStream = zipFile.getInputStream(zipEntry);

        Map<Object, Object> options = new HashMap<Object, Object>();
        options.put(BinaryResourceImpl.OPTION_VERSION, BinaryResourceImpl.BinaryIO.Version.VERSION_1_1);
        options.put(BinaryResourceImpl.OPTION_STYLE_DATA_CONVERTER, Boolean.TRUE);
        options.put(BinaryResourceImpl.OPTION_BUFFER_CAPACITY, 8192);

        EObjectInputStream stream = new BinaryResourceImpl.EObjectInputStream(inputStream, options);

        timeStamp = stream.readLong();
        int refreshHours = stream.readInt();
        int repositoryCount = stream.readInt();

        if (refreshed)
        {
          File validityFile = getRepositoriesValidityFile();
          IOUtil.writeLines(validityFile, "UTF-8", Collections.singletonList("" + (System.currentTimeMillis() + refreshHours * 60 * 60 * 1000)));
        }

        Map<RepositoryImpl, List<Integer>> composedRepositories = new HashMap<RepositoryImpl, List<Integer>>();
        for (int id = 1; id <= repositoryCount; id++)
        {
          RepositoryImpl repository = new RepositoryImpl(stream, id, composedRepositories);
          repositories.put(id, repository);
        }

        for (Map.Entry<RepositoryImpl, List<Integer>> entry : composedRepositories.entrySet())
        {
          RepositoryImpl repository = entry.getKey();
          for (int compositeID : entry.getValue())
          {
            RepositoryImpl composite = repositories.get(compositeID);
            if (composite != null)
            {
              composite.addChild(repository);
              repository.addComposite(composite);
            }
          }
        }

        repositoriesArray = repositories.values().toArray(new Repository[repositories.size()]);
      }
      catch (Exception ex)
      {
        P2CorePlugin.INSTANCE.log(ex, IStatus.WARNING);
      }
      finally
      {
        IOUtil.close(inputStream);
        if (zipFile != null)
        {
          try
          {
            zipFile.close();
          }
          catch (IOException ex)
          {
            P2CorePlugin.INSTANCE.log(ex, IStatus.WARNING);
          }
        }
      }
    }
  }

  private boolean initRepositoriesCacheFile() throws Exception
  {
    if (repositoriesCacheFile == null)
    {
      IPath stateLocation = P2CorePlugin.INSTANCE.isOSGiRunning() ? P2CorePlugin.INSTANCE.getStateLocation() : new Path(".");
      repositoriesCacheFile = new File(stateLocation.toOSString(), "repositories");
    }

    if (repositoriesCacheFile.exists())
    {
      File validityFile = getRepositoriesValidityFile();
      List<String> lines = IOUtil.readLines(validityFile, "UTF-8");
      long validUntil = Long.parseLong(lines.get(0));
      if (System.currentTimeMillis() <= validUntil)
      {
        return false;
      }
    }

    InputStream inputStream = null;
    OutputStream outputStream = null;

    try
    {
      inputStream = new URL(INDEX_BASE + "repositories").openStream();
      outputStream = new FileOutputStream(repositoriesCacheFile);
      IOUtil.copy(inputStream, outputStream);
    }
    finally
    {
      IOUtil.close(outputStream);
      IOUtil.close(inputStream);
    }

    return true;
  }

  private boolean initCapabilitiesCacheFile() throws Exception
  {
    if (capabilitiesCacheFile == null)
    {
      IPath stateLocation = P2CorePlugin.INSTANCE.isOSGiRunning() ? P2CorePlugin.INSTANCE.getStateLocation() : new Path(".");
      capabilitiesCacheFile = new File(stateLocation.toOSString(), "capabilities");
    }

    if (capabilitiesCacheFile.exists())
    {
      File validityFile = getCapabilitiesValidityFile();
      List<String> lines = IOUtil.readLines(validityFile, "UTF-8");
      long validUntil = Long.parseLong(lines.get(0));
      if (System.currentTimeMillis() <= validUntil)
      {
        return false;
      }
    }

    InputStream inputStream = null;
    OutputStream outputStream = null;

    try
    {
      inputStream = new URL(INDEX_BASE + "capabilities").openStream();
      outputStream = new FileOutputStream(capabilitiesCacheFile);
      IOUtil.copy(inputStream, outputStream);
    }
    finally
    {
      IOUtil.close(outputStream);
      IOUtil.close(inputStream);
    }

    return true;
  }

  private File getRepositoriesValidityFile()
  {
    return new File(repositoriesCacheFile.getParentFile(), "repositories.txt");
  }

  private File getCapabilitiesValidityFile()
  {
    return new File(capabilitiesCacheFile.getParentFile(), "capabilities.txt");
  }

  public Repository[] getRepositories()
  {
    initRepositories(false);
    return repositoriesArray;
  }

  public Map<String, Set<String>> getCapabilities()
  {
    initCapabilities();
    return Collections.unmodifiableMap(capabilitiesMap);
  }

  public Map<Repository, Set<Version>> lookupCapabilities(String namespace, String name)
  {
    Map<Repository, Set<Version>> capabilities = new HashMap<Repository, Set<Version>>();
    if (!StringUtil.isEmpty(namespace) && !StringUtil.isEmpty(name))
    {
      namespace = URI.encodeSegment(namespace, false);
      name = URI.encodeSegment(name, false);

      BufferedReader reader = null;

      try
      {
        InputStream inputStream = new URL(INDEX_BASE + namespace + "/" + name).openStream();
        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line = reader.readLine();
        if (line == null)
        {
          return capabilities;
        }

        long timeStamp = Long.parseLong(line);
        initRepositories(timeStamp != this.timeStamp);

        while ((line = reader.readLine()) != null)
        {
          String[] tokens = line.split(",");
          int repositoryID = Integer.parseInt(tokens[0]);
          Repository repository = repositories.get(repositoryID);
          if (repository != null)
          {
            Set<Version> versions = new HashSet<Version>();
            for (int i = 1; i < tokens.length; i++)
            {
              versions.add(Version.parseVersion(tokens[i]));
            }

            capabilities.put(repository, versions);
          }
        }
      }
      catch (FileNotFoundException ex)
      {
        // Ignore.
      }
      catch (Exception ex)
      {
        P2CorePlugin.INSTANCE.log(ex, IStatus.WARNING);
      }
      finally
      {
        IOUtil.close(reader);
      }
    }

    return capabilities;
  }

  public Map<Repository, Set<Version>> generateCapabilitiesFromComposedRepositories(Map<Repository, Set<Version>> capabilitiesFromSimpleRepositories)
  {
    Map<Repository, Set<Version>> capabilities = new HashMap<Repository, Set<Version>>();
    for (Map.Entry<Repository, Set<Version>> entry : capabilitiesFromSimpleRepositories.entrySet())
    {
      Repository repository = entry.getKey();
      Set<Version> versions = entry.getValue();
      recurseComposedRepositories(capabilities, repository, versions);
    }

    return capabilities;
  }

  private void recurseComposedRepositories(Map<Repository, Set<Version>> capabilities, Repository repository, Set<Version> versions)
  {
    for (Repository composite : repository.getComposites())
    {
      Set<Version> set = capabilities.get(composite);
      if (set == null)
      {
        set = new HashSet<Version>();
        capabilities.put(composite, set);
      }

      set.addAll(versions);
      recurseComposedRepositories(capabilities, composite, versions);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class RepositoryImpl implements Repository
  {
    public static final int UNINITIALIZED = -1;

    private static final Repository[] NO_REPOSITORIES = {};

    private final URI location;

    private final int id;

    private final boolean composed;

    private final boolean compressed;

    private final long timestamp;

    private int capabilityCount;

    private Repository[] children;

    private Repository[] composites;

    public RepositoryImpl(EObjectInputStream stream, int id, Map<RepositoryImpl, List<Integer>> composedRepositories) throws IOException
    {
      this.id = id;
      location = stream.readURI();
      composed = stream.readBoolean();
      compressed = stream.readBoolean();
      timestamp = stream.readLong();

      if (composed)
      {
        capabilityCount = UNINITIALIZED;
      }
      else
      {
        capabilityCount = stream.readInt();
      }

      List<Integer> composites = null;
      while (stream.readBoolean())
      {
        if (composites == null)
        {
          composites = new ArrayList<Integer>();
          composedRepositories.put(this, composites);
        }

        int composite = stream.readInt();
        composites.add(composite);
      }
    }

    public URI getLocation()
    {
      return location;
    }

    public int getID()
    {
      return id;
    }

    public boolean isComposed()
    {
      return composed;
    }

    public boolean isCompressed()
    {
      return compressed;
    }

    public long getTimestamp()
    {
      return timestamp;
    }

    public int getCapabilityCount()
    {
      if (composed && capabilityCount == UNINITIALIZED)
      {
        capabilityCount = 0;
        for (Repository child : getChildren())
        {
          capabilityCount += child.getCapabilityCount();
        }
      }

      return capabilityCount;
    }

    public Repository[] getChildren()
    {
      if (children == null)
      {
        return NO_REPOSITORIES;
      }

      return children;
    }

    public Repository[] getComposites()
    {
      if (composites == null)
      {
        return NO_REPOSITORIES;
      }

      return composites;
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + id;
      return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (this == obj)
      {
        return true;
      }

      if (obj == null)
      {
        return false;
      }

      if (getClass() != obj.getClass())
      {
        return false;
      }

      RepositoryImpl other = (RepositoryImpl)obj;
      if (id != other.id)
      {
        return false;
      }

      return true;
    }

    public int compareTo(Repository o)
    {
      return location.toString().compareTo(o.getLocation().toString());
    }

    @Override
    public String toString()
    {
      return location.toString();
    }

    public void addChild(Repository child)
    {
      children = addRepository(children, child);
    }

    public void addComposite(Repository composite)
    {
      composites = addRepository(composites, composite);
    }

    private Repository[] addRepository(Repository[] repositories, Repository repository)
    {
      if (repositories == null)
      {
        return new Repository[] { repository };
      }

      int length = repositories.length;
      Repository[] newRepositories = new Repository[length + 1];
      System.arraycopy(repositories, 0, newRepositories, 0, length);
      newRepositories[length] = repository;
      return newRepositories;
    }
  }
}
