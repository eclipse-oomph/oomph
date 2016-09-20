/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.SubMonitor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.ArtifactKeyQuery;
import org.eclipse.equinox.p2.repository.artifact.IArtifactDescriptor;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.artifact.IFileArtifactRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Eike Stepper
 */
public final class AgentAnalyzer
{
  private final Agent agent;

  private final Map<File, AnalyzedBundlePool> bundlePools = new HashMap<File, AnalyzedBundlePool>();

  private final CountDownLatch analyzeLatch;

  private final List<Job> analyzeProfileJobs = new ArrayList<Job>();

  private Set<URI> repositoryURIs;

  private Handler handler;

  public AgentAnalyzer(Agent agent, boolean analyzeDamage, Handler handler, IProgressMonitor monitor)
  {
    this.agent = agent;
    this.handler = handler;

    Collection<Profile> allProfiles = agent.getAllProfiles();
    monitor.beginTask("Loading profiles...", allProfiles.size());

    try
    {
      for (Profile p2Profile : allProfiles)
      {
        P2CorePlugin.checkCancelation(monitor);
        if (p2Profile.isValid())
        {
          monitor.subTask(p2Profile.getProfileId());

          BundlePool p2BundlePool = p2Profile.getBundlePool();
          if (p2BundlePool != null)
          {
            File installFolder = p2Profile.getInstallFolder();
            File location = p2BundlePool.getLocation();
            if (!location.equals(installFolder))
            {
              AnalyzedBundlePool bundlePool = bundlePools.get(location);
              if (bundlePool == null)
              {
                bundlePool = new AnalyzedBundlePool(this, location);
                bundlePools.put(location, bundlePool);
              }

              bundlePool.addProfile(p2Profile, installFolder);
            }
          }
        }

        monitor.worked(1);
      }
    }
    finally
    {
      monitor.done();
    }

    if (handler != null)
    {
      handler.analyzerChanged(this);
    }

    analyzeLatch = new CountDownLatch(bundlePools.size());

    for (AnalyzedBundlePool bundlePool : bundlePools.values())
    {
      Job job = bundlePool.analyze(analyzeLatch, analyzeDamage);
      analyzeProfileJobs.add(job);
    }
  }

  public void awaitAnalyzing(IProgressMonitor monitor)
  {
    int totalWork = bundlePools.size();
    SubMonitor progress = SubMonitor.convert(monitor, "Analyzing...", totalWork).detectCancelation();
    progress.subTask("Analyzing artifacts...");

    int work = totalWork - (int)analyzeLatch.getCount();
    if (work != 0)
    {
      progress.worked(work);
    }

    try
    {
      while (!analyzeLatch.await(100, TimeUnit.MILLISECONDS))
      {
        P2CorePlugin.checkCancelation(monitor);

        int newWork = totalWork - (int)analyzeLatch.getCount();
        if (newWork != work)
        {
          progress.worked(newWork - work);
          work = newWork;
        }
      }
    }
    catch (InterruptedException ex)
    {
      //$FALL-THROUGH$
    }
    finally
    {
      progress.done();
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

  public Map<File, AnalyzedBundlePool> getBundlePools()
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

      for (AnalyzedBundlePool bundlePool : bundlePools.values())
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

  private void bundlePoolChanged(AnalyzedBundlePool bundlePool, boolean artifacts, boolean profiles)
  {
    if (handler != null)
    {
      handler.bundlePoolChanged(bundlePool, artifacts, profiles);
    }
  }

  private void profileChanged(AnalyzedProfile profile)
  {
    if (handler != null)
    {
      handler.profileChanged(profile);
    }
  }

  private void artifactChanged(AnalyzedArtifact artifact)
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

    public void bundlePoolChanged(AnalyzedBundlePool bundlePool, boolean artifacts, boolean profiles);

    public void profileChanged(AnalyzedProfile profile);

    public void artifactChanged(AnalyzedArtifact artifact);
  }

  /**
   * @author Eike Stepper
   */
  public static final class AnalyzedBundlePool implements Comparable<AnalyzedBundlePool>
  {
    private final AgentAnalyzer analyzer;

    private final File location;

    private final Set<URI> repositoryURIs = new LinkedHashSet<URI>();

    private final List<AnalyzedProfile> profiles = new ArrayList<AnalyzedProfile>();

    private final Map<IArtifactKey, AnalyzedArtifact> artifacts = new HashMap<IArtifactKey, AnalyzedArtifact>();

    private final Set<AnalyzedArtifact> unusedArtifacts = new HashSet<AnalyzedArtifact>();

    private final Set<AnalyzedArtifact> damagedArtifacts = new HashSet<AnalyzedArtifact>();

    private int damagedArtifactsPercent;

    private AnalyzedArtifact[] artifactsArray;

    private AnalyzedArtifact[] unusedArtifactsArray;

    private AnalyzedArtifact[] damagedArtifactsArray;

    private IFileArtifactRepository p2BundlePool;

    private boolean analyzing = true;

    private boolean analyzingDamage = true;

    public AnalyzedBundlePool(AgentAnalyzer analyzer, File location)
    {
      this.analyzer = analyzer;
      this.location = location;
    }

    public boolean isAnalyzing()
    {
      return analyzing;
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

    public AnalyzedProfile[] getProfiles()
    {
      synchronized (this)
      {
        return profiles.toArray(new AnalyzedProfile[profiles.size()]);
      }
    }

    public AnalyzedProfile[] getUnusedProfiles()
    {
      List<AnalyzedProfile> unusedProfiles = new ArrayList<AnalyzedProfile>();

      synchronized (this)
      {
        for (AnalyzedProfile profile : profiles)
        {
          if (profile.isUnused())
          {
            unusedProfiles.add(profile);
          }
        }
      }

      return unusedProfiles.toArray(new AnalyzedProfile[unusedProfiles.size()]);
    }

    public int getUnusedProfilesCount()
    {
      int count = 0;

      synchronized (this)
      {
        for (AnalyzedProfile profile : profiles)
        {
          if (profile.isUnused())
          {
            ++count;
          }
        }
      }

      return count;
    }

    public int getArtifactCount()
    {
      synchronized (this)
      {
        return artifacts.size();
      }
    }

    public AnalyzedArtifact[] getArtifacts()
    {
      synchronized (this)
      {
        if (artifactsArray == null)
        {
          artifactsArray = artifacts.values().toArray(new AnalyzedArtifact[artifacts.size()]);
          Arrays.sort(artifactsArray);
        }

        return artifactsArray;
      }
    }

    public AnalyzedArtifact getArtifact(IArtifactKey key)
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

    public AnalyzedArtifact[] getUnusedArtifacts()
    {
      synchronized (this)
      {
        if (unusedArtifactsArray == null)
        {
          unusedArtifactsArray = unusedArtifacts.toArray(new AnalyzedArtifact[unusedArtifacts.size()]);
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

    public AnalyzedArtifact[] getDamagedArtifacts()
    {
      synchronized (this)
      {
        if (damagedArtifactsArray == null)
        {
          damagedArtifactsArray = damagedArtifacts.toArray(new AnalyzedArtifact[damagedArtifacts.size()]);
          Arrays.sort(damagedArtifactsArray);
        }

        return damagedArtifactsArray;
      }
    }

    public boolean isAnalyzingDamage()
    {
      return analyzingDamage;
    }

    public int compareTo(AnalyzedBundlePool o)
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

    AnalyzedProfile addProfile(Profile p2Profile, File installFolder)
    {
      AnalyzedProfile profile = new AnalyzedProfile(this, p2Profile, installFolder);
      repositoryURIs.addAll(profile.getRepositoryURIs());

      synchronized (this)
      {
        profiles.add(profile);
        Collections.sort(profiles);
      }

      return profile;
    }

    Job analyze(final CountDownLatch analyzeLatch, final boolean analyzeDamage)
    {
      Job job = new Job("Analyzing bundle pool " + location)
      {
        @Override
        protected IStatus run(IProgressMonitor monitor)
        {
          analyze(analyzeDamage, monitor);
          analyzeLatch.countDown();
          return Status.OK_STATUS;
        }
      };

      job.schedule();
      return job;
    }

    private void analyze(boolean analyzeDamage, IProgressMonitor monitor)
    {
      Random random = new Random(System.currentTimeMillis());

      IFileArtifactRepository p2BundlePool = getP2BundlePool(monitor);
      for (IArtifactKey key : P2Util.asIterable(p2BundlePool.query(ArtifactKeyQuery.ALL_KEYS, monitor)))
      {
        P2CorePlugin.checkCancelation(monitor);

        File file = p2BundlePool.getArtifactFile(key);
        AnalyzedArtifact artifact = new AnalyzedArtifact(this, key, file);

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

      for (AnalyzedProfile profile : getProfiles())
      {
        P2CorePlugin.checkCancelation(monitor);
        profile.analyze(monitor);
      }

      analyzer.analyzerChanged(analyzer);

      analyzeUnusedArtifacts(monitor);
      analyzing = false;

      if (analyzeDamage)
      {
        analyzeDamagedArtifacts(monitor);
      }
    }

    private void analyzeUnusedArtifacts(IProgressMonitor monitor)
    {
      for (AnalyzedArtifact artifact : getArtifacts())
      {
        P2CorePlugin.checkCancelation(monitor);
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

    private boolean analyzeUnusedArtifact(AnalyzedArtifact artifact, IProgressMonitor monitor)
    {
      for (AnalyzedProfile profile : getProfiles())
      {
        P2CorePlugin.checkCancelation(monitor);
        if (profile.getArtifacts().contains(artifact))
        {
          return false;
        }
      }

      return true;
    }

    private void analyzeDamagedArtifacts(IProgressMonitor monitor)
    {
      AnalyzedArtifact[] artifacts = getArtifacts();
      int total = artifacts.length;
      int i = 0;

      for (AnalyzedArtifact artifact : artifacts)
      {
        P2CorePlugin.checkCancelation(monitor);

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
      analyzingDamage = false;
    }

    @SuppressWarnings("restriction")
    private static boolean isDamaged(AnalyzedArtifact artifact)
    {
      File file = artifact.getFile();
      if (file == null || !file.exists())
      {
        return true;
      }

      if (file.isFile())
      {
        ZipFile zipFile = null;

        try
        {
          zipFile = new ZipFile(file);
          Enumeration<? extends ZipEntry> entries = zipFile.entries();
          if (!entries.hasMoreElements())
          {
            return true;
          }

          do
          {
            ZipEntry entry = entries.nextElement();

            entry.getName();
            entry.getCompressedSize();
            entry.getCrc();

            InputStream inputStream = null;

            try
            {
              inputStream = zipFile.getInputStream(entry);
              if (inputStream == null)
              {
                return true;
              }
            }
            finally
            {
              IOUtil.close(inputStream);
            }
          } while (entries.hasMoreElements());
        }
        catch (Exception ex)
        {
          return true;
        }
        finally
        {
          try
          {
            if (zipFile != null)
            {
              zipFile.close();
            }
          }
          catch (IOException ex)
          {
            throw new IORuntimeException(ex);
          }
        }
      }

      try
      {
        String type = artifact.getType();
        org.eclipse.equinox.p2.publisher.AbstractPublisherAction action;
        String namespace;
        if (AnalyzedArtifact.TYPE_FEATURE.equals(type))
        {
          action = new org.eclipse.equinox.p2.publisher.eclipse.FeaturesAction(new File[] { file });
          namespace = "org.eclipse.update.feature";
        }
        else if (AnalyzedArtifact.TYPE_PLUGIN.equals(type))
        {
          action = new org.eclipse.equinox.p2.publisher.eclipse.BundlesAction(new File[] { file });
          namespace = "osgi.bundle";
        }
        else
        {
          return false;
        }

        org.eclipse.equinox.p2.publisher.PublisherInfo info = new org.eclipse.equinox.p2.publisher.PublisherInfo();
        org.eclipse.equinox.p2.publisher.PublisherResult result = new org.eclipse.equinox.p2.publisher.PublisherResult();
        action.perform(info, result, new NullProgressMonitor());
        IArtifactKey key = artifact.getKey();
        String id = key.getId();
        Version version = key.getVersion();
        for (Iterator<IInstallableUnit> it = result.everything(); it.hasNext();)
        {
          IInstallableUnit iu = it.next();
          for (IProvidedCapability capability : iu.getProvidedCapabilities())
          {
            String name = capability.getName();
            String capabilityNamespace = capability.getNamespace();
            Version capabilityVersion = capability.getVersion();
            if (namespace.equals(capabilityNamespace) && id.equals(name) && version.equals(capabilityVersion))
            {
              return false;
            }
          }
        }
      }
      catch (Exception exception)
      {
        return true;
      }

      return true;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class AnalyzedProfile implements Comparable<AnalyzedProfile>
  {
    public static final String ECLIPSE = "Eclipse";

    public static final String TARGLET = "Targlet";

    public static final String UNKNOWN = "Unknown";

    @Deprecated
    private static final String PROP_TARGLET_CONTAINER_ID = "targlet.container.id";

    private final AnalyzedBundlePool bundlePool;

    private final Profile p2Profile;

    private final File installFolder;

    private final String type;

    private final int roots;

    private final Set<URI> repositoryURIs = new LinkedHashSet<URI>();

    private final Set<AnalyzedArtifact> artifacts = new HashSet<AnalyzedArtifact>();

    private final Set<AnalyzedArtifact> damagedArtifacts = new HashSet<AnalyzedArtifact>();

    private AnalyzedArtifact[] damagedArtifactsArray;

    public AnalyzedProfile(AnalyzedBundlePool bundlePool, Profile p2Profile, File installFolder)
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

      ProfileDefinition profileDefinition = p2Profile.getDefinition();
      roots = profileDefinition.getRequirements().size();

      for (Repository repository : profileDefinition.getRepositories())
      {
        try
        {
          repositoryURIs.add(new URI(repository.getURL()));
        }
        catch (URISyntaxException ex)
        {
          P2CorePlugin.INSTANCE.log(ex);
        }
      }
    }

    public AnalyzedBundlePool getBundlePool()
    {
      return bundlePool;
    }

    public Profile getP2Profile()
    {
      return p2Profile;
    }

    public boolean isUnused()
    {
      return !p2Profile.isUsed();
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

    public final int getRoots()
    {
      return roots;
    }

    public Set<URI> getRepositoryURIs()
    {
      return repositoryURIs;
    }

    public Set<AnalyzedArtifact> getArtifacts()
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

    public AnalyzedArtifact[] getDamagedArtifacts()
    {
      synchronized (bundlePool)
      {
        if (damagedArtifactsArray == null)
        {
          damagedArtifactsArray = damagedArtifacts.toArray(new AnalyzedArtifact[damagedArtifacts.size()]);
          Arrays.sort(damagedArtifactsArray);
        }

        return damagedArtifactsArray;
      }
    }

    public int compareTo(AnalyzedProfile o)
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
      for (IInstallableUnit iu : P2Util.asIterable(p2Profile.query(QueryUtil.createIUAnyQuery(), monitor)))
      {
        for (IArtifactKey key : iu.getArtifacts())
        {
          AnalyzedArtifact artifact = bundlePool.getArtifact(key);
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

    public synchronized void delete(IProgressMonitor monitor)
    {
      if (isUnused())
      {
        monitor.subTask("Deleting " + this);
        p2Profile.delete();

        boolean artifactsChanged = false;
        synchronized (bundlePool)
        {
          for (AnalyzedArtifact artifact : artifacts)
          {
            if (artifact.profiles.remove(this))
            {
              artifactsChanged = true;

              if (artifact.profiles.isEmpty())
              {
                bundlePool.unusedArtifacts.add(artifact);
                bundlePool.unusedArtifactsArray = null;
              }
            }
          }

          bundlePool.profiles.remove(this);
        }

        bundlePool.analyzer.bundlePoolChanged(bundlePool, true, true);
        bundlePool.analyzer.profileChanged(this);

        if (artifactsChanged)
        {
          bundlePool.analyzer.artifactChanged(null);
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class AnalyzedArtifact implements Comparable<AnalyzedArtifact>
  {
    public static final String REPAIR_TASK_NAME = "Repairing artifacts";

    public static final String TYPE_FEATURE = "Feature";

    public static final String TYPE_PLUGIN = "Plugin";

    public static final String TYPE_BINARY = "Binary";

    private final AnalyzedBundlePool bundlePool;

    private final IArtifactKey key;

    private final String type;

    private final File file;

    private final List<AnalyzedProfile> profiles = new ArrayList<AnalyzedProfile>();

    private boolean damaged;

    public AnalyzedArtifact(AnalyzedBundlePool bundlePool, IArtifactKey key, File file)
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

    public AnalyzedBundlePool getBundlePool()
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

    public List<AnalyzedProfile> getProfiles()
    {
      return profiles;
    }

    public int compareTo(AnalyzedArtifact o)
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

      AnalyzedArtifact other = (AnalyzedArtifact)obj;
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

    void addProfile(AnalyzedProfile profile)
    {
      profiles.add(profile);
    }

    void setDamaged()
    {
      damaged = true;
      for (AnalyzedProfile profile : profiles)
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

        for (AnalyzedProfile profile : profiles)
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

        for (AnalyzedProfile profile : profiles)
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
      for (AnalyzedBundlePool pool : bundlePool.analyzer.getBundlePools().values())
      {
        if (pool != bundlePool)
        {
          AnalyzedArtifact otherArtifact = pool.getArtifact(key);
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
              localDescriptors = null;
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
        throw ex;
      }
      catch (Error err)
      {
        throw err;
      }
      catch (Exception ex)
      {
        P2CorePlugin.INSTANCE.log(ex);
      }
      finally
      {
        restoreDescriptors(p2BundlePool, localDescriptors);
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
