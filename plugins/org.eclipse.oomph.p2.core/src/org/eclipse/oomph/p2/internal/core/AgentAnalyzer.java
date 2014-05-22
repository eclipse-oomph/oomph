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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.SubMonitor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.ArtifactKeyQuery;
import org.eclipse.equinox.p2.repository.artifact.IArtifactDescriptor;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IFileArtifactRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Eike Stepper
 */
public final class AgentAnalyzer
{
  private final Agent agent;

  private final Map<File, BundlePool> bundlePools = new HashMap<File, BundlePool>();

  private final List<Job> analyzeProfileJobs = new ArrayList<Job>();

  private Set<URI> repositoryURIs;

  private Handler handler;

  public AgentAnalyzer(Agent agent, Handler handler)
  {
    this.agent = agent;
    this.handler = handler;

    IProfileRegistry profileRegistry = agent.getProfileRegistry();
    for (IProfile p2Profile : profileRegistry.getProfiles())
    {
      String installFolder = p2Profile.getProperty(IProfile.PROP_INSTALL_FOLDER);
      String cache = p2Profile.getProperty(IProfile.PROP_CACHE);
      if (cache != null && !cache.equals(installFolder))
      {
        File location = new File(cache);

        BundlePool bundlePool = bundlePools.get(location);
        if (bundlePool == null)
        {
          bundlePool = new BundlePool(this, location);
          bundlePools.put(location, bundlePool);
        }

        bundlePool.addProfile(p2Profile, installFolder);
      }
    }

    handler.analyzerChanged(this);

    for (BundlePool bundlePool : bundlePools.values())
    {
      Job job = bundlePool.analyze();
      analyzeProfileJobs.add(job);
    }
  }

  public void dispose()
  {
    handler = null;

    for (Job job : analyzeProfileJobs)
    {
      job.cancel();
    }

    analyzeProfileJobs.clear();
    bundlePools.clear();
  }

  public Map<File, BundlePool> getBundlePools()
  {
    return bundlePools;
  }

  public Set<URI> getRepositoryURIs()
  {
    if (repositoryURIs == null)
    {
      repositoryURIs = new HashSet<URI>();

      IArtifactRepositoryManager repositoryManager = agent.getArtifactRepositoryManager();
      // addURIs(repositoryURIs, repositoryManager, IRepositoryManager.REPOSITORIES_ALL);
      // addURIs(repositoryURIs, repositoryManager, IRepositoryManager.REPOSITORIES_DISABLED);
      // addURIs(repositoryURIs, repositoryManager, IRepositoryManager.REPOSITORIES_LOCAL);
      // addURIs(repositoryURIs, repositoryManager, IRepositoryManager.REPOSITORIES_NON_LOCAL);
      // addURIs(repositoryURIs, repositoryManager, IRepositoryManager.REPOSITORIES_SYSTEM);
      addURIs(repositoryURIs, repositoryManager, IRepositoryManager.REPOSITORIES_NON_SYSTEM);

      for (BundlePool bundlePool : bundlePools.values())
      {
        // Don't use possibly damaged local bundle pools for damage repair
        repositoryURIs.remove(bundlePool.getLocation().toURI());
      }
    }

    return repositoryURIs;
  }

  private void analyzerChanged(AgentAnalyzer analyzer)
  {
    if (handler != null)
    {
      handler.analyzerChanged(analyzer);
    }
  }

  private void bundlePoolChanged(BundlePool bundlePool, boolean artifacts, boolean profiles)
  {
    if (handler != null)
    {
      handler.bundlePoolChanged(bundlePool, artifacts, profiles);
    }
  }

  private void profileChanged(Profile profile)
  {
    if (handler != null)
    {
      handler.profileChanged(profile);
    }
  }

  private void artifactChanged(Artifact artifact)
  {
    if (handler != null)
    {
      handler.artifactChanged(artifact);
    }
  }

  private void addURIs(Set<URI> repos, IArtifactRepositoryManager repositoryManager, int flag)
  {
    for (URI uri : repositoryManager.getKnownRepositories(flag))
    {
      repos.add(uri);
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface Handler
  {
    public void analyzerChanged(AgentAnalyzer analyzer);

    public void bundlePoolChanged(BundlePool bundlePool, boolean artifacts, boolean profiles);

    public void profileChanged(Profile profile);

    public void artifactChanged(Artifact artifact);
  }

  /**
   * @author Eike Stepper
   */
  public static final class BundlePool implements Comparable<BundlePool>
  {
    private final AgentAnalyzer analyzer;

    private final File location;

    private final Set<URI> repositoryURIs = new LinkedHashSet<URI>();

    private final List<Profile> profiles = new ArrayList<Profile>();

    private final Map<IArtifactKey, Artifact> artifacts = new HashMap<IArtifactKey, Artifact>();

    private final Set<Artifact> unusedArtifacts = new HashSet<Artifact>();

    private final Set<Artifact> damagedArtifacts = new HashSet<Artifact>();

    private int damagedArtifactsPercent;

    private Artifact[] artifactsArray;

    private Artifact[] unusedArtifactsArray;

    private Artifact[] damagedArtifactsArray;

    private IFileArtifactRepository p2BundlePool;

    public BundlePool(AgentAnalyzer analyzer, File location)
    {
      this.analyzer = analyzer;
      this.location = location;
    }

    public AgentAnalyzer getAnalyzer()
    {
      return analyzer;
    }

    public File getLocation()
    {
      return location;
    }

    public Set<URI> getRepositoryURIs()
    {
      return repositoryURIs;
    }

    public int getProfilesCount()
    {
      synchronized (this)
      {
        return profiles.size();
      }
    }

    public Profile[] getProfiles()
    {
      synchronized (profiles)
      {
        return profiles.toArray(new Profile[profiles.size()]);
      }
    }

    public int getArtifactCount()
    {
      synchronized (this)
      {
        return artifacts.size();
      }
    }

    public Artifact[] getArtifacts()
    {
      synchronized (this)
      {
        if (artifactsArray == null)
        {
          artifactsArray = artifacts.values().toArray(new Artifact[artifacts.size()]);
          Arrays.sort(artifactsArray);
        }

        return artifactsArray;
      }
    }

    public Artifact getArtifact(IArtifactKey key)
    {
      synchronized (this)
      {
        return artifacts.get(key);
      }
    }

    public int getUnusedArtifactsCount()
    {
      synchronized (this)
      {
        return unusedArtifacts.size();
      }
    }

    public Artifact[] getUnusedArtifacts()
    {
      synchronized (this)
      {
        if (unusedArtifactsArray == null)
        {
          unusedArtifactsArray = unusedArtifacts.toArray(new Artifact[unusedArtifacts.size()]);
          Arrays.sort(unusedArtifactsArray);
        }

        return unusedArtifactsArray;
      }
    }

    public int getDamagedArtifactsPercent()
    {
      return damagedArtifactsPercent;
    }

    public int getDamagedArtifactsCount()
    {
      synchronized (damagedArtifacts)
      {
        return damagedArtifacts.size();
      }
    }

    public Artifact[] getDamagedArtifacts()
    {
      synchronized (this)
      {
        if (damagedArtifactsArray == null)
        {
          damagedArtifactsArray = damagedArtifacts.toArray(new Artifact[damagedArtifacts.size()]);
          Arrays.sort(damagedArtifactsArray);
        }

        return damagedArtifactsArray;
      }
    }

    public int compareTo(BundlePool o)
    {
      return location.getAbsolutePath().compareTo(o.getLocation().getAbsolutePath());
    }

    @Override
    public String toString()
    {
      return location.toString();
    }

    synchronized IFileArtifactRepository getP2BundlePool(IProgressMonitor monitor)
    {
      if (p2BundlePool == null)
      {
        try
        {
          IArtifactRepositoryManager repositoryManager = analyzer.agent.getArtifactRepositoryManager();
          p2BundlePool = (IFileArtifactRepository)repositoryManager.loadRepository(location.toURI(), monitor);
        }
        catch (ProvisionException ex)
        {
          throw new IllegalStateException(ex);
        }
      }

      return p2BundlePool;
    }

    Profile addProfile(IProfile p2Profile, String installFolder)
    {
      Profile profile = new Profile(this, p2Profile, installFolder == null ? null : new File(installFolder));
      repositoryURIs.addAll(profile.getRepositoryURIs());

      synchronized (this)
      {
        profiles.add(profile);
        Collections.sort(profiles);
      }

      return profile;
    }

    Job analyze()
    {
      Job job = new Job("Analyzing bundle pool " + location)
      {
        @Override
        protected IStatus run(IProgressMonitor monitor)
        {
          analyze(monitor);
          return Status.OK_STATUS;
        }
      };

      job.schedule();
      return job;
    }

    private void analyze(IProgressMonitor monitor)
    {
      Random random = new Random(System.currentTimeMillis());

      IFileArtifactRepository p2BundlePool = getP2BundlePool(monitor);
      for (IArtifactKey key : p2BundlePool.query(ArtifactKeyQuery.ALL_KEYS, monitor))
      {
        checkCancelation(monitor);

        File file = p2BundlePool.getArtifactFile(key);
        Artifact artifact = new Artifact(this, key, file);

        synchronized (this)
        {
          artifacts.put(key, artifact);
          artifactsArray = null;
        }

        if (random.nextInt(100) < 2)
        {
          analyzer.bundlePoolChanged(this, false, false);
        }
      }

      analyzer.bundlePoolChanged(this, true, false);

      for (Profile profile : getProfiles())
      {
        checkCancelation(monitor);
        profile.analyze(monitor);
      }

      analyzer.analyzerChanged(analyzer);

      analyzeUnusedArtifacts(monitor);
      analyzeDamagedArtifacts(monitor);
    }

    private void analyzeUnusedArtifacts(IProgressMonitor monitor)
    {
      for (Artifact artifact : getArtifacts())
      {
        checkCancelation(monitor);
        if (analyzeUnusedArtifact(artifact, monitor))
        {
          synchronized (this)
          {
            unusedArtifacts.add(artifact);
            unusedArtifactsArray = null;
          }

          analyzer.bundlePoolChanged(this, false, false);
        }
      }

      analyzer.bundlePoolChanged(this, true, false);
    }

    private boolean analyzeUnusedArtifact(Artifact artifact, IProgressMonitor monitor)
    {
      for (Profile profile : getProfiles())
      {
        checkCancelation(monitor);
        if (profile.getArtifacts().contains(artifact))
        {
          return false;
        }
      }

      return true;
    }

    private void analyzeDamagedArtifacts(IProgressMonitor monitor)
    {
      Artifact[] artifacts = getArtifacts();
      int total = artifacts.length;
      int i = 0;

      for (Artifact artifact : artifacts)
      {
        checkCancelation(monitor);

        int percent = ++i * 100 / total;
        if (percent != damagedArtifactsPercent)
        {
          damagedArtifactsPercent = percent;
          analyzer.bundlePoolChanged(this, false, false);
        }

        synchronized (artifact)
        {
          IArtifactKey key = artifact.getKey();
          if (getArtifact(key) == null)
          {
            // Continue with next artifact if this artifact was deleted meanwhile
            continue;
          }
        }

        monitor.subTask("Validating " + artifact);
        if (isDamaged(artifact))
        {
          synchronized (this)
          {
            damagedArtifacts.add(artifact);
            damagedArtifactsArray = null;
          }

          analyzer.bundlePoolChanged(this, false, false);

          artifact.setDamaged();
          analyzer.artifactChanged(artifact);
        }
      }

      analyzer.bundlePoolChanged(this, false, false);
    }

    private static boolean isDamaged(Artifact artifact)
    {
      File file = artifact.getFile();
      if (file == null || !file.exists())
      {
        return true;
      }

      String name = file.getName();
      if (name.endsWith(".jar") || name.endsWith(".zip"))
      {
        ZipInputStream in = null;

        try
        {
          in = new ZipInputStream(new FileInputStream(file));

          ZipEntry entry = in.getNextEntry();
          if (entry == null)
          {
            return true;
          }

          while (entry != null)
          {
            entry.getName();
            entry.getCompressedSize();
            entry.getCrc();

            entry = in.getNextEntry();
          }
        }
        catch (Exception ex)
        {
          return true;
        }
        finally
        {
          IOUtil.close(in);
        }
      }

      return false;
    }

    private static void checkCancelation(IProgressMonitor monitor)
    {
      if (monitor.isCanceled())
      {
        throw new OperationCanceledException();
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class Profile implements Comparable<Profile>
  {
    public static final String ECLIPSE = "Eclipse";

    public static final String TARGLET = "Targlet";

    public static final String UNKNOWN = "Unknown";

    @Deprecated
    private static final String PROP_TARGLET_CONTAINER_ID = "targlet.container.id";

    @Deprecated
    private static final String PROP_REPO_LIST = "setup.repo.list";

    private final BundlePool bundlePool;

    private final IProfile p2Profile;

    private final File installFolder;

    private final String type;

    private final Set<URI> repositoryURIs = new LinkedHashSet<URI>();

    private final Set<Artifact> artifacts = new HashSet<Artifact>();

    private final Set<Artifact> damagedArtifacts = new HashSet<Artifact>();

    private Artifact[] damagedArtifactsArray;

    public Profile(BundlePool bundlePool, IProfile p2Profile, File installFolder)
    {
      this.bundlePool = bundlePool;
      this.p2Profile = p2Profile;
      this.installFolder = installFolder;

      if (p2Profile.getProperty(PROP_TARGLET_CONTAINER_ID) != null)
      {
        type = TARGLET;
      }
      else if (installFolder != null)
      {
        type = ECLIPSE;
      }
      else
      {
        type = UNKNOWN;
      }

      String repoList = p2Profile.getProperty(PROP_REPO_LIST);
      if (repoList != null)
      {
        StringTokenizer tokenizer = new StringTokenizer(repoList, ",");
        while (tokenizer.hasMoreTokens())
        {
          String uri = tokenizer.nextToken();

          try
          {
            repositoryURIs.add(new URI(uri));
          }
          catch (URISyntaxException ex)
          {
            P2CorePlugin.INSTANCE.log(ex);
          }
        }
      }
    }

    public BundlePool getBundlePool()
    {
      return bundlePool;
    }

    public String getID()
    {
      return p2Profile.getProfileId();
    }

    public File getInstallFolder()
    {
      return installFolder;
    }

    public String getType()
    {
      return type;
    }

    public Set<URI> getRepositoryURIs()
    {
      return repositoryURIs;
    }

    public Set<Artifact> getArtifacts()
    {
      return artifacts;
    }

    public boolean isDamaged()
    {
      synchronized (bundlePool)
      {
        return !damagedArtifacts.isEmpty();
      }
    }

    public int getDamagedArtifactsCount()
    {
      synchronized (bundlePool)
      {
        return damagedArtifacts.size();
      }
    }

    public Artifact[] getDamagedArtifacts()
    {
      synchronized (bundlePool)
      {
        if (damagedArtifactsArray == null)
        {
          damagedArtifactsArray = damagedArtifacts.toArray(new Artifact[damagedArtifacts.size()]);
          Arrays.sort(damagedArtifactsArray);
        }

        return damagedArtifactsArray;
      }
    }

    public int compareTo(Profile o)
    {
      return getID().compareTo(o.getID());
    }

    @Override
    public String toString()
    {
      return getID();
    }

    void analyze(IProgressMonitor monitor)
    {
      for (IInstallableUnit iu : p2Profile.query(QueryUtil.createIUAnyQuery(), monitor))
      {
        for (IArtifactKey key : iu.getArtifacts())
        {
          Artifact artifact = bundlePool.getArtifact(key);
          if (artifact != null)
          {
            synchronized (bundlePool)
            {
              artifacts.add(artifact);
              artifact.addProfile(this);
            }

            bundlePool.analyzer.profileChanged(this);
          }
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class Artifact implements Comparable<Artifact>
  {
    public static final String REPAIR_TASK_NAME = "Repairing artifacts";

    public static final String TYPE_FEATURE = "Feature";

    public static final String TYPE_PLUGIN = "Plugin";

    public static final String TYPE_BINARY = "Binary";

    private final BundlePool bundlePool;

    private final IArtifactKey key;

    private final String type;

    private final File file;

    private final List<Profile> profiles = new ArrayList<Profile>();

    private boolean damaged;

    public Artifact(BundlePool bundlePool, IArtifactKey key, File file)
    {
      this.bundlePool = bundlePool;
      this.key = key;
      this.file = file;

      String classifier = key.getClassifier();
      if ("org.eclipse.update.feature".equals(classifier))
      {
        type = TYPE_FEATURE;
      }
      else if ("osgi.bundle".equals(classifier))
      {
        type = TYPE_PLUGIN;
      }
      else
      {
        type = TYPE_BINARY;
      }
    }

    public boolean isUnused()
    {
      return profiles.isEmpty();
    }

    public boolean isDamaged()
    {
      return damaged;
    }

    public BundlePool getBundlePool()
    {
      return bundlePool;
    }

    public IArtifactKey getKey()
    {
      return key;
    }

    public String getType()
    {
      return type;
    }

    public String getID()
    {
      return key.getId();
    }

    public String getVersion()
    {
      return key.getVersion().toString();
    }

    public File getFile()
    {
      return file;
    }

    public List<Profile> getProfiles()
    {
      return profiles;
    }

    public int compareTo(Artifact o)
    {
      int result = key.getId().compareTo(o.key.getId());
      if (result == 0)
      {
        result = key.getVersion().compareTo(o.key.getVersion());
        if (result == 0)
        {
          result = type.compareTo(o.type);
        }
      }

      return result;
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + (key == null ? 0 : key.hashCode());
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

      Artifact other = (Artifact)obj;
      if (key == null)
      {
        if (other.key != null)
        {
          return false;
        }
      }
      else if (!key.equals(other.key))
      {
        return false;
      }

      return true;
    }

    @Override
    public String toString()
    {
      return key.getId() + " " + key.getVersion();
    }

    void addProfile(Profile profile)
    {
      profiles.add(profile);
    }

    void setDamaged()
    {
      damaged = true;
      for (Profile profile : profiles)
      {
        synchronized (bundlePool)
        {
          profile.damagedArtifacts.add(this);
          profile.damagedArtifactsArray = null;
        }

        bundlePool.analyzer.handler.profileChanged(profile);
      }
    }

    public synchronized void delete(IProgressMonitor monitor)
    {
      if (isUnused())
      {
        deleteUnused(monitor);
      }
      else
      {
        monitor.subTask("Deleting " + this);
        IOUtil.deleteBestEffort(file);
        damaged = true;

        synchronized (bundlePool)
        {
          bundlePool.damagedArtifacts.add(this);
          bundlePool.damagedArtifactsArray = null;
        }

        bundlePool.analyzer.bundlePoolChanged(bundlePool, false, false);
        bundlePool.analyzer.artifactChanged(this);

        for (Profile profile : profiles)
        {
          synchronized (bundlePool)
          {
            profile.damagedArtifacts.add(this);
            profile.damagedArtifactsArray = null;
          }

          bundlePool.analyzer.profileChanged(profile);
        }
      }
    }

    private void deleteUnused(IProgressMonitor monitor)
    {
      monitor.subTask("Deleting " + this);
      IFileArtifactRepository p2BundlePool = bundlePool.getP2BundlePool(monitor);
      p2BundlePool.removeDescriptor(key, monitor);
      damaged = false;

      synchronized (bundlePool)
      {
        bundlePool.artifacts.remove(key);
        bundlePool.artifactsArray = null;

        bundlePool.unusedArtifacts.remove(this);
        bundlePool.unusedArtifactsArray = null;

        bundlePool.damagedArtifacts.remove(this);
        bundlePool.damagedArtifactsArray = null;
      }

      bundlePool.analyzer.bundlePoolChanged(bundlePool, true, false);
    }

    public synchronized boolean repair(Set<URI> repositoryURIs, IProgressMonitor monitor)
    {
      if (!damaged)
      {
        return false;
      }

      if (isUnused())
      {
        deleteUnused(monitor);
        return true;
      }

      monitor.subTask("Repairing " + this);
      if (repositoryURIs == null ? doRepair(monitor) : doRepair(repositoryURIs, monitor))
      {
        damaged = false;
        bundlePool.analyzer.artifactChanged(this);

        synchronized (bundlePool)
        {
          bundlePool.damagedArtifacts.remove(this);
          bundlePool.damagedArtifactsArray = null;
        }

        for (Profile profile : profiles)
        {
          synchronized (bundlePool)
          {
            profile.damagedArtifacts.remove(this);
            profile.damagedArtifactsArray = null;
          }

          bundlePool.analyzer.profileChanged(profile);
        }

        bundlePool.analyzer.bundlePoolChanged(bundlePool, false, false);
        return true;
      }

      return false;
    }

    private boolean doRepair(IProgressMonitor monitor)
    {
      Set<URI> repositoryURIs = bundlePool.getRepositoryURIs();
      SubMonitor progress = SubMonitor.convert(monitor, 1 + repositoryURIs.size()).detectCancelation();

      Set<URI> poolURIs = new HashSet<URI>();
      for (BundlePool pool : bundlePool.analyzer.getBundlePools().values())
      {
        if (pool != bundlePool)
        {
          Artifact otherArtifact = pool.getArtifact(key);
          if (otherArtifact != null && !otherArtifact.isDamaged())
          {
            URI uri = pool.getLocation().toURI();
            poolURIs.add(uri);
          }
        }
      }

      if (!poolURIs.isEmpty())
      {
        if (doRepair(poolURIs, progress))
        {
          return true;
        }
      }

      if (!repositoryURIs.isEmpty())
      {
        if (doRepair(repositoryURIs, progress))
        {
          return true;
        }
      }

      return false;
    }

    private boolean doRepair(Set<URI> repositoryURIs, IProgressMonitor monitor)
    {
      SubMonitor progress = SubMonitor.convert(monitor, repositoryURIs.size()).detectCancelation();
      for (URI uri : repositoryURIs)
      {
        if (doRepair(uri, progress.newChild()))
        {
          return true;
        }
      }

      return false;
    }

    private boolean doRepair(URI repositoryURI, SubMonitor progress)
    {
      IFileArtifactRepository p2BundlePool = bundlePool.getP2BundlePool(progress.newChild());
      IArtifactDescriptor[] localDescriptors = null;

      try
      {
        localDescriptors = p2BundlePool.getArtifactDescriptors(key);
        if (localDescriptors == null || localDescriptors.length == 0)
        {
          return false;
        }

        p2BundlePool.removeDescriptors(localDescriptors, progress.newChild());

        IArtifactRepositoryManager repositoryManager = bundlePool.analyzer.agent.getArtifactRepositoryManager();
        IArtifactRepository repository = repositoryManager.loadRepository(repositoryURI, progress.newChild());
        progress.setTaskName(REPAIR_TASK_NAME);

        IArtifactDescriptor[] remoteDescriptors = repository.getArtifactDescriptors(key);
        for (IArtifactDescriptor remoteDescriptor : remoteDescriptors)
        {
          OutputStream destination = null;

          try
          {
            destination = p2BundlePool.getOutputStream(localDescriptors[0]);

            IStatus status = repository.getArtifact(remoteDescriptor, destination, progress.newChild());
            if (status.getSeverity() == IStatus.OK)
            {
              return true;
            }
          }
          finally
          {
            IOUtil.close(destination);
          }
        }
      }
      catch (OperationCanceledException ex)
      {
        restoreDescriptors(p2BundlePool, localDescriptors);
        throw ex;
      }
      catch (Error err)
      {
        restoreDescriptors(p2BundlePool, localDescriptors);
        throw err;
      }
      catch (Exception ex)
      {
        restoreDescriptors(p2BundlePool, localDescriptors);
        P2CorePlugin.INSTANCE.log(ex);
      }

      return false;
    }

    private void restoreDescriptors(IFileArtifactRepository p2BundlePool, IArtifactDescriptor[] oldDescriptors)
    {
      if (oldDescriptors != null && oldDescriptors.length != 0)
      {
        try
        {
          p2BundlePool.addDescriptors(oldDescriptors, new NullProgressMonitor());
        }
        catch (Exception ex)
        {
          P2CorePlugin.INSTANCE.log(ex);
        }
      }
    }
  }
}
