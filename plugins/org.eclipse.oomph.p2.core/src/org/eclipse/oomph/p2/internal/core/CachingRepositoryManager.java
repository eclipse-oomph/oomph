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

import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OfflineMode;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.ReflectUtil.ReflectionException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.internal.p2.artifact.repository.Activator;
import org.eclipse.equinox.internal.p2.artifact.repository.ArtifactRepositoryManager;
import org.eclipse.equinox.internal.p2.artifact.repository.MirrorSelector;
import org.eclipse.equinox.internal.p2.artifact.repository.simple.SimpleArtifactRepository;
import org.eclipse.equinox.internal.p2.core.helpers.LogHelper;
import org.eclipse.equinox.internal.p2.core.helpers.OrderedProperties;
import org.eclipse.equinox.internal.p2.engine.CommitOperationEvent;
import org.eclipse.equinox.internal.p2.engine.RollbackOperationEvent;
import org.eclipse.equinox.internal.p2.metadata.repository.MetadataRepositoryManager;
import org.eclipse.equinox.internal.p2.repository.DownloadStatus;
import org.eclipse.equinox.internal.p2.repository.Transport;
import org.eclipse.equinox.internal.p2.repository.helpers.AbstractRepositoryManager;
import org.eclipse.equinox.internal.p2.repository.helpers.LocationProperties;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.IProvisioningEventBus;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.SynchronousProvisioningListener;
import org.eclipse.equinox.internal.provisional.p2.repository.RepositoryEvent;
import org.eclipse.equinox.p2.core.IAgentLocation;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class CachingRepositoryManager<T>
{
  public static final String BOGUS_SCHEME = "bogus";

  private static final Method METHOD_checkValidLocation = ReflectUtil.getMethod(AbstractRepositoryManager.class, "checkValidLocation", URI.class);

  private static final Method METHOD_enterLoad = ReflectUtil.getMethod(AbstractRepositoryManager.class, "enterLoad", URI.class, IProgressMonitor.class);

  private static final Method METHOD_basicGetRepository = ReflectUtil.getMethod(AbstractRepositoryManager.class, "basicGetRepository", URI.class);

  private static final Method METHOD_fail = ReflectUtil.getMethod(AbstractRepositoryManager.class, "fail", URI.class, int.class);

  private static final Method METHOD_addRepository1 = ReflectUtil.getMethod(AbstractRepositoryManager.class, "addRepository", URI.class, boolean.class,
      boolean.class);

  private static final Method METHOD_loadIndexFile = ReflectUtil.getMethod(AbstractRepositoryManager.class, "loadIndexFile", URI.class, IProgressMonitor.class);

  private static final Method METHOD_getPreferredRepositorySearchOrder = ReflectUtil.getMethod(AbstractRepositoryManager.class,
      "getPreferredRepositorySearchOrder", LocationProperties.class);

  private static final Method METHOD_getAllSuffixes = ReflectUtil.getMethod(AbstractRepositoryManager.class, "getAllSuffixes");

  private static final Method METHOD_loadRepository = ReflectUtil.getMethod(AbstractRepositoryManager.class, "loadRepository", URI.class, String.class,
      String.class, int.class, SubMonitor.class);

  private static final Method METHOD_addRepository2 = ReflectUtil.getMethod(AbstractRepositoryManager.class, "addRepository", IRepository.class, boolean.class,
      String.class);

  private static final Method METHOD_removeRepository = ReflectUtil.getMethod(AbstractRepositoryManager.class, "removeRepository", URI.class, boolean.class);

  private static final Method METHOD_exitLoad = ReflectUtil.getMethod(AbstractRepositoryManager.class, "exitLoad", URI.class);

  private static final Method METHOD_broadcastChangeEvent = ReflectUtil.getMethod(AbstractRepositoryManager.class, "broadcastChangeEvent", URI.class, int.class,
      int.class, boolean.class);

  private static final String PROPERTY_VERSION = "version";

  private static final String PROP_BETTER_MIRROR_SELECTION = "oomph.p2.mirror";

  private static boolean betterMirrorSelection;

  private final AbstractRepositoryManager<T> delegate;

  private final int repositoryType;

  private final CachingTransport transport;

  public CachingRepositoryManager(AbstractRepositoryManager<T> delegate, int repositoryType, CachingTransport transport)
  {
    this.delegate = delegate;

    IAgentLocation agentLocation = ReflectUtil.getValue("agentLocation", delegate);
    if (agentLocation != null)
    {
      URI rootLocation = agentLocation.getRootLocation();
      if (rootLocation != null)
      {
        if (!IOUtil.canWriteFolder(new File(rootLocation.getPath())))
        {
          ReflectUtil.setValue("agentLocation", delegate, null);
        }
      }
    }

    this.repositoryType = repositoryType;

    if (transport == null)
    {
      Object t = delegate.getAgent().getService(Transport.SERVICE_NAME);
      if (t instanceof CachingTransport)
      {
        transport = (CachingTransport)t;
      }
    }

    this.transport = transport;
  }

  public CachingTransport getTransport()
  {
    return transport;
  }

  public IRepository<T> loadRepository(URI location, IProgressMonitor monitor, String type, int flags) throws ProvisionException
  {
    checkValidLocation(location);
    SubMonitor sub = SubMonitor.convert(monitor, 100);
    boolean added = false;
    IRepository<T> result = null;

    try
    {
      CachingTransport.startLoadingRepository(location);
      enterLoad(location, sub.newChild(5));

      result = basicGetRepository(location);
      if (result != null)
      {
        return result;
      }

      // Add the repository first so that it will be enabled, but don't send add event until after the load.
      added = addRepository(location, true, false);

      LocationProperties indexFile = loadIndexFile(location, sub.newChild(15));
      String[] preferredOrder = getPreferredRepositorySearchOrder(indexFile);
      String[] allSuffixes = getAllSuffixes();
      String[] suffixes = sortSuffixes(allSuffixes, preferredOrder);

      sub = SubMonitor.convert(sub, NLS.bind("Adding repository {0}", location), suffixes.length * 100);
      ProvisionException failure = null;

      try
      {
        for (int i = 0; i < suffixes.length; i++)
        {
          if (sub.isCanceled())
          {
            throw new OperationCanceledException();
          }

          try
          {
            result = loadRepository(location, suffixes[i], type, flags, sub.newChild(100));
          }
          catch (ProvisionException e)
          {
            failure = e;
            break;
          }

          if (result != null)
          {
            addRepository(result, false, suffixes[i]);
            cacheIndexFile(location, suffixes[i]);
            break;
          }
        }
      }
      finally
      {
        sub.done();
      }

      if (result == null)
      {
        // If we just added the repository, remove it because it cannot be loaded.
        if (added)
        {
          removeRepository(location, false);
        }

        // Eagerly cleanup missing system repositories.
        if (Boolean.valueOf(delegate.getRepositoryProperty(location, IRepository.PROP_SYSTEM)).booleanValue())
        {
          delegate.removeRepository(location);
        }

        if (failure != null)
        {
          throw failure;
        }

        fail(location, ProvisionException.REPOSITORY_NOT_FOUND);
      }
    }
    finally
    {
      CachingTransport.stopLoadingRepository();
      exitLoad(location);
    }

    // Broadcast the add event after releasing lock.
    if (added)
    {
      broadcastChangeEvent(location, repositoryType, RepositoryEvent.ADDED, true);
    }

    return result;
  }

  private File getCachedIndexFile(URI location)
  {
    try
    {
      String path = location.toString();
      if (!path.endsWith("/"))
      {
        path += "/";
      }

      return transport.getCacheFile(new URI(path + "p2.index"));
    }
    catch (URISyntaxException ex)
    {
      // Can't happen.
      throw new RuntimeException(ex);
    }
  }

  private void cacheIndexFile(URI location, String suffix)
  {
    if ("file".equals(location.getScheme()))
    {
      return;
    }

    File cachedIndexFile = getCachedIndexFile(location);

    Map<String, String> properties = PropertiesUtil.getProperties(cachedIndexFile);
    if (!properties.containsKey(PROPERTY_VERSION))
    {
      properties.put(PROPERTY_VERSION, "1");
    }

    if (repositoryType == IRepository.TYPE_METADATA)
    {
      properties.put("metadata.repository.factory.order", suffix);
    }
    else
    {
      properties.put("artifact.repository.factory.order", suffix);
    }

    // Cleanup; can be removed at some point in the future...
    properties.remove("generated");

    PropertiesUtil.saveProperties(cachedIndexFile, properties, false);
  }

  private URI checkValidLocation(URI location)
  {
    return (URI)ReflectUtil.invokeMethod(METHOD_checkValidLocation, delegate, location);
  }

  private void enterLoad(URI location, IProgressMonitor monitor)
  {
    ReflectUtil.invokeMethod(METHOD_enterLoad, delegate, location, monitor);
  }

  @SuppressWarnings("unchecked")
  protected IRepository<T> basicGetRepository(URI location)
  {
    return (IRepository<T>)ReflectUtil.invokeMethod(METHOD_basicGetRepository, delegate, location);
  }

  // private boolean checkNotFound(URI location)
  // {
  // return (Boolean)ReflectUtil.invokeMethod(METHOD_checkNotFound, delegate, location);
  // }
  //
  // private void rememberNotFound(URI location)
  // {
  // ReflectUtil.invokeMethod(METHOD_rememberNotFound, delegate, location);
  // }

  private void fail(URI location, int code) throws ProvisionException
  {
    try
    {
      ReflectUtil.invokeMethod(METHOD_fail, delegate, location, code);
    }
    catch (ReflectionException ex)
    {
      Throwable cause = ex.getCause();
      if (cause instanceof ProvisionException)
      {
        throw (ProvisionException)cause;
      }

      throw ex;
    }
  }

  private boolean addRepository(URI location, boolean isEnabled, boolean signalAdd)
  {
    return (Boolean)ReflectUtil.invokeMethod(METHOD_addRepository1, delegate, location, isEnabled, signalAdd);
  }

  private LocationProperties loadIndexFile(URI location, IProgressMonitor monitor)
  {
    return (LocationProperties)ReflectUtil.invokeMethod(METHOD_loadIndexFile, delegate, location, monitor);
  }

  protected String[] getPreferredRepositorySearchOrder(LocationProperties properties)
  {
    return (String[])ReflectUtil.invokeMethod(METHOD_getPreferredRepositorySearchOrder, delegate, properties);
  }

  protected String[] getAllSuffixes()
  {
    return (String[])ReflectUtil.invokeMethod(METHOD_getAllSuffixes, delegate);
  }

  private String[] sortSuffixes(String[] allSuffixes, String[] preferredOrder)
  {
    List<String> suffixes = new ArrayList<String>(Arrays.asList(allSuffixes));

    for (int i = preferredOrder.length - 1; i >= 0; --i)
    {
      String suffix = preferredOrder[i].trim();
      if (!LocationProperties.END.equals(suffix))
      {
        suffixes.remove(suffix);
        suffixes.add(0, suffix);
      }
    }

    return suffixes.toArray(new String[suffixes.size()]);
  }

  @SuppressWarnings("unchecked")
  private IRepository<T> loadRepository(URI location, String suffix, String type, int flags, SubMonitor monitor) throws ProvisionException
  {
    try
    {
      return (IRepository<T>)ReflectUtil.invokeMethod(METHOD_loadRepository, delegate, location, suffix, type, flags, monitor);
    }
    catch (ReflectionException ex)
    {
      Throwable cause = ex.getCause();
      if (cause instanceof ProvisionException)
      {
        throw (ProvisionException)cause;
      }

      throw ex;
    }
  }

  protected void addRepository(IRepository<T> repository, boolean signalAdd, String suffix)
  {
    ReflectUtil.invokeMethod(METHOD_addRepository2, delegate, repository, signalAdd, suffix);
  }

  private boolean removeRepository(URI toRemove, boolean signalRemove)
  {
    return (Boolean)ReflectUtil.invokeMethod(METHOD_removeRepository, delegate, toRemove, signalRemove);
  }

  private void exitLoad(URI location)
  {
    ReflectUtil.invokeMethod(METHOD_exitLoad, delegate, location);
  }

  private void broadcastChangeEvent(URI location, int repositoryType, int kind, boolean isEnabled)
  {
    ReflectUtil.invokeMethod(METHOD_broadcastChangeEvent, delegate, location, repositoryType, kind, isEnabled);
  }

  public static boolean isBetterMirrorSelection()
  {
    return betterMirrorSelection;
  }

  public static boolean enableBetterMirrorSelection()
  {
    boolean originalBetterMirrorSelection = betterMirrorSelection;
    setBetterMirrorSelection(!"false".equals(PropertiesUtil.getProperty(PROP_BETTER_MIRROR_SELECTION)));
    return originalBetterMirrorSelection;
  }

  public static void setBetterMirrorSelection(boolean betterMirrorSelection)
  {
    CachingRepositoryManager.betterMirrorSelection = betterMirrorSelection;
  }

  /**
   * @author Eike Stepper
   */
  public static class Metadata extends MetadataRepositoryManager
  {
    private final CachingRepositoryManager<IInstallableUnit> loader;

    public Metadata(IProvisioningAgent agent, CachingTransport transport)
    {
      super(agent);
      loader = new CachingRepositoryManager<IInstallableUnit>(this, IRepository.TYPE_METADATA, transport);
    }

    @Override
    protected IRepository<IInstallableUnit> loadRepository(URI location, IProgressMonitor monitor, String type, int flags) throws ProvisionException
    {
      return loader.loadRepository(location, monitor, type, flags);
    }

    @Override
    public URI[] getKnownRepositories(int flags)
    {
      return filter(super.getKnownRepositories(flags));
    }

    /**
     * Work-around for bug 483286.
     */
    @Override
    public void flushCache()
    {
      synchronized (repositoryLock)
      {
        if (repositories != null)
        {
          super.flushCache();
        }
      }
    }

    static URI[] filter(URI[] uris)
    {
      List<URI> result = new ArrayList<URI>(uris.length);
      for (URI uri : uris)
      {
        try
        {
          if (!"file".equalsIgnoreCase(uri.getScheme()) || new File(uri).isDirectory())
          {
            result.add(uri);
          }
        }
        catch (IllegalArgumentException ex)
        {
          //$FALL-THROUGH$
        }
      }

      return result.toArray(new URI[result.size()]);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Artifact extends ArtifactRepositoryManager
  {
    private static final String GLOBAL_MAX_THREADS = Activator.getContext().getProperty(SimpleArtifactRepository.PROP_MAX_THREADS);

    private final CachingRepositoryManager<IArtifactKey> loader;

    public Artifact(IProvisioningAgent agent, CachingTransport transport)
    {
      super(agent);
      loader = new CachingRepositoryManager<IArtifactKey>(this, IRepository.TYPE_ARTIFACT, transport);
    }

    @Override
    protected IRepository<IArtifactKey> loadRepository(URI location, IProgressMonitor monitor, String type, int flags) throws ProvisionException
    {
      // Inspect the repository if better mirror selection is enabled,
      // the repository is simple
      // and the repository not modifiable, i.e., if it's not a local file-based repository.
      IRepository<IArtifactKey> result = loader.loadRepository(location, monitor, type, flags);
      if (isBetterMirrorSelection() && result instanceof SimpleArtifactRepository && !result.isModifiable())
      {
        // There should always be an event bus.
        IProvisioningEventBus eventBus = (IProvisioningEventBus)getAgent().getService(IProvisioningEventBus.SERVICE_NAME);
        if (eventBus != null)
        {
          // Create our improved mirror selector and reflectively set it to be the one used by this repository.
          final BetterMirrorSelector mirrorSelector = new BetterMirrorSelector(result, loader.getTransport(), eventBus);
          ReflectUtil.setValue("mirrors", result, mirrorSelector);

          // Because of the poor implementation in org.eclipse.equinox.internal.p2.artifact.repository.simple.SimpleArtifactRepository.getMaximumThreads()
          // which was reported in https://bugs.eclipse.org/bugs/show_bug.cgi?id=471861 but is not yet fixed,
          // we work the around the problem by setting the global PROP_MAX_THREADS into the repository properties.
          if (GLOBAL_MAX_THREADS != null)
          {
            Map<String, String> properties = ReflectUtil.getValue("properties", result);
            Map<String, String> specializedProperties = new OrderedProperties()
            {
              @Override
              public Set<java.util.Map.Entry<String, String>> entrySet()
              {
                if (!OfflineMode.isEnabled())
                {
                  // If the request for the entry set occurs when p2 is determining the maximum number of threads allowed by the repository...
                  StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                  if (stackTrace.length > 5 && "getMaximumThreads".equals(stackTrace[5].getMethodName()))
                  {
                    // If the repository itself doesn't restrict the maximum number of threads...
                    String respositoryMaxThreads = get(SimpleArtifactRepository.PROP_MAX_THREADS);
                    if (respositoryMaxThreads == null)
                    {
                      // Initialize our specialized mirror selector.
                      mirrorSelector.initMirrorActivities(new NullProgressMonitor());

                      // If there is no list of mirrors, allow 10 threads, otherwise, allow 10 threads per mirror or the global maximum, whichever is less.
                      // Save that value as if it were a property in the repository.
                      BetterMirrorSelector.MirrorActivity[] mirrorActivities = mirrorSelector.mirrorActivities;
                      int maxThreads = mirrorActivities.length <= 1 ? 10 : Math.min(mirrorActivities.length * 10, Integer.parseInt(GLOBAL_MAX_THREADS));
                      put(SimpleArtifactRepository.PROP_MAX_THREADS, Integer.toString(maxThreads));
                    }
                  }
                }

                return super.entrySet();
              }
            };

            // Copy over the properties and insert our specialized map reflective into the field of the base class.
            specializedProperties.putAll(properties);
            ReflectUtil.setValue("properties", result, specializedProperties);
          }
        }
      }

      return result;
    }

    @Override
    public URI[] getKnownRepositories(int flags)
    {
      return Metadata.filter(super.getKnownRepositories(flags));
    }

    /**
     * Work-around for bug 483286.
     */
    @Override
    public void flushCache()
    {
      synchronized (repositoryLock)
      {
        if (repositories != null)
        {
          super.flushCache();
        }
      }
    }

    /**
     * @author Eike Stepper
     * @author Ed Merks
     */
    static final class BetterMirrorSelector extends MirrorSelector implements SynchronousProvisioningListener
    {
      /**
       * The main artifact repository.
       */
      private final URI repositoryURI;

      /**
       * The event bus to which this selector listens.
       */
      private final IProvisioningEventBus eventBus;

      /**
       * A map from mirror URI to the artifact activity for that mirror.
       */
      private final Map<URI, ArtifactActivity> artifactActivities = Collections.synchronizedMap(new HashMap<URI, ArtifactActivity>());

      /**
       * The phase in which we are using mirrors.
       * During the initial phase, each mirror is probed to measure performance.
       * Subsequently only the faster mirrors are used.
       */
      private int phase = 1;

      /**
       * The per-mirror activities being managed.
       */
      private MirrorActivity[] mirrorActivities;

      public BetterMirrorSelector(IRepository<?> repository, Transport transport, IProvisioningEventBus eventBus)
      {
        super(repository, transport);
        this.eventBus = eventBus;
        repositoryURI = getBaseURI();
      }

      public void notify(EventObject event)
      {
        // If we have mirror activities.
        if (mirrorActivities != null)
        {
          // If it's a download event.
          if (event instanceof DownloadArtifactEvent)
          {
            // Get the corresponding artifact activity for this event, if there is one..
            DownloadArtifactEvent downloadArtifactEvent = (DownloadArtifactEvent)event;
            URI artifactURI = downloadArtifactEvent.getArtifactURI();
            ArtifactActivity artifactActivity = artifactActivities.get(artifactURI);
            if (artifactActivity != null)
            {
              // Get the corresponding overall mirror activity.
              MirrorActivity mirrorActivity = artifactActivity.getMirrorActivity();
              if (downloadArtifactEvent.isCompleted())
              {
                // If the download is completed, reduce the usage count of this mirror.
                mirrorActivity.decrementUsage();

                // Determine if it was successful or not.
                IStatus status = downloadArtifactEvent.getStatus();
                if (status.isOK())
                {
                  // Increment the success count of this mirror.
                  mirrorActivity.incrementSuccess();

                  // Record the overall end-to-end speed based or course on the overall time for the download the start and complete along with the number of
                  // bytes.
                  DownloadStatus downloadStatus = (DownloadStatus)status;
                  long fileSize = downloadStatus.getFileSize();
                  artifactActivity.setSize(fileSize);
                  mirrorActivity.setSpeed(artifactActivity.getSpeed());
                }
                else
                {
                  // There was a failure.
                  mirrorActivity.incrementFailure();

                  // So no bytes were successfully downloaded.
                  artifactActivity.setSize(0);

                  // This will reduce the average speed of the mirror in half, quickly dropping it from consideration for use.
                  mirrorActivity.setSpeed(0);
                }
              }
              else
              {
                // If the download is started, increment the usage count of the mirror.
                mirrorActivity.incrementUsage();

                // Record the point in time at which this activity started.
                artifactActivity.setStart();
              }
            }
          }
          else if (event instanceof CommitOperationEvent || event instanceof RollbackOperationEvent)
          {
            // If we complete or we roll back, we remove this as a listener.
            if (eventBus != null)
            {
              eventBus.removeListener(this);
            }

            // And reset our state to the intial state for subsequent reuse.
            mirrorActivities = null;
            artifactActivities.clear();
            phase = 1;
          }
        }
      }

      @Override
      public synchronized URI getMirrorLocation(URI inputLocation, IProgressMonitor monitor)
      {
        // If we don't know the repository URI, we can't request mirrors.
        Assert.isNotNull(inputLocation);
        if (repositoryURI == null)
        {
          return inputLocation;
        }

        // Determine the relative location of the download with respect to the repository.
        URI relativeLocation = repositoryURI.relativize(inputLocation);
        if (relativeLocation == null || relativeLocation.isAbsolute())
        {
          // If it's not relative, we just use the given URI.
          return inputLocation;
        }

        // Initialize the mirrors with a mirror request to populate the list.
        initMirrorActivities(monitor);

        // Select the mirror to be used for the artifact activity.
        MirrorActivity selectedMirror = selectMirror();
        if (selectedMirror == null)
        {
          // If there isn't one, we must use the given input.
          return inputLocation;
        }

        String location = selectedMirror.getLocation();
        try
        {
          // Determine the location within the mirror, create a new artifact activity for it, and see if there was previous such an activity.
          URI artifactURI = new URI(location + relativeLocation.getPath());
          ArtifactActivity newActivity = new ArtifactActivity(selectedMirror);
          ArtifactActivity previousActivity = artifactActivities.put(artifactURI, newActivity);
          if (previousActivity != null)
          {
            if (!newActivity.retry(previousActivity))
            {
              // Return a bogus URI that will refuse to try this artifactURI from this mirror again.
              return new URI(BOGUS_SCHEME + ":" + artifactURI);
            }

            // We don't expect to have repeated requests for the same artifact.
            // But this does happen if it's a .pack.gz produced by a version of Java newer than the version of Java being used by this process.
            monitor.subTask("Repeated attempts to download " + artifactURI + " probably because it can't be processed");
          }

          // Use this location in the mirror instead of the original URI.
          return artifactURI;
        }
        catch (URISyntaxException e)
        {
          log("Unable to make location " + inputLocation + " relative to mirror " + location, e);
        }

        // If all else fails, we must use the original URI.
        return inputLocation;
      }

      private MirrorActivity selectMirror()
      {
        // If we don't have mirrors, we can't select one.
        if (mirrorActivities.length == 0)
        {
          return null;
        }

        if (phase == 1)
        {
          // During phase one, we try to probe each mirror once to measure speed.
          for (MirrorActivity mirrorActivity : mirrorActivities)
          {
            if (!mirrorActivity.isProbed())
            {
              mirrorActivity.setProbed();
              return mirrorActivity;
            }
          }

          // Once all mirrors are probed, we enter phase two.
          phase = 2;
        }

        // We sort the mirrors based on performance, favoring faster mirrors of course.
        sort();

        for (MirrorActivity mirrorActivity : mirrorActivities)
        {
          // We try to limit the number of simultaneous activities per mirror to avoid overloading any one mirror.
          int MAX_ACTIVITY_PER_HOST = 4;
          int usages = mirrorActivity.getUsages();
          if (usages < MAX_ACTIVITY_PER_HOST)
          {
            // Return the fasted one that's not so busy.
            return mirrorActivity;
          }
        }

        // Find the least used mirror in the first half of the available mirrors.
        MirrorActivity leastUsedMirror = null;
        for (MirrorActivity mirrorActivity : mirrorActivities)
        {
          int usages = mirrorActivity.getUsages();
          if (leastUsedMirror == null || leastUsedMirror.getUsages() > usages)
          {
            leastUsedMirror = mirrorActivity;
          }
        }

        return leastUsedMirror;
      }

      public List<String> getStats()
      {
        // Compute statistics for the mirror speeds.
        List<String> result = new ArrayList<String>();
        if (mirrorActivities != null)
        {
          // We'll create new mirror activities to summarize the result.
          MirrorActivity[] summaryMirrorActivities = new MirrorActivity[mirrorActivities.length];

          for (int i = 0; i < mirrorActivities.length; ++i)
          {
            MirrorActivity mirrorActivity = mirrorActivities[i];
            MirrorActivity summaryMirrorActivity = summaryMirrorActivities[i] = new MirrorActivity(mirrorActivity.location);
            for (int j = 0; j < mirrorActivity.getFailures(); ++j)
            {
              // Record the number of failures.
              summaryMirrorActivity.incrementFailure();
            }

            // Compute the total number of bytes and the total time taken to download them for all the artifact activities.
            long totalSize = 0;
            long totalDuration = 0;
            for (ArtifactActivity artifactActivity : artifactActivities.values())
            {
              if (artifactActivity.getMirrorActivity() == mirrorActivity)
              {
                summaryMirrorActivity.incrementSuccess();
                totalSize += artifactActivity.getSize();
                totalDuration += artifactActivity.getDuration();
              }
            }

            if (totalSize != 0)
            {
              // Record this as the overall result of the mirror activity.
              summaryMirrorActivity.setSpeed(1000 * totalSize / totalDuration);
            }
          }

          // Replace with the summary.
          mirrorActivities = summaryMirrorActivities;

          // Sort them again.
          sort();

          // Add only the ones that actually were used to the final result.
          for (MirrorActivity mirrorActivity : mirrorActivities)
          {
            if (mirrorActivity.getSuccesses() + mirrorActivity.getFailures() > 0)
            {
              result.add(getStats(mirrorActivity));
            }
          }
        }

        return result;
      }

      private String getStats(MirrorActivity mirrorActivity)
      {
        // Compute a nice string representation for the mirror activity.
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        int successes = mirrorActivity.getSuccesses();
        int failures = mirrorActivity.getFailures();
        String message = "Mirrored " + successes + " artifacts from " + mirrorActivity.getLocation() + " at "
            + numberFormat.format(mirrorActivity.getSpeed() / 1000) + "kb/s";
        if (failures > 0)
        {
          message += " with " + failures + " failures";
        }

        return message;
      }

      private void sort()
      {
        // Compute a sorted map from the speed to the mirror activity with that speed.
        Map<Long, Set<MirrorActivity>> sortedMap = new TreeMap<Long, Set<MirrorActivity>>();
        for (MirrorActivity mirrorActivity : mirrorActivities)
        {
          long value = mirrorActivity.getSpeed();
          CollectionUtil.add(sortedMap, value, mirrorActivity);
        }

        // Flatten out the values of the map back into the array.
        int index = mirrorActivities.length;
        for (Set<MirrorActivity> acitivities : sortedMap.values())
        {
          for (MirrorActivity mirrorActivity : acitivities)
          {
            mirrorActivities[--index] = mirrorActivity;
          }
        }
      }

      private void initMirrorActivities(IProgressMonitor monitor)
      {
        // Do this only once.
        if (mirrorActivities == null)
        {
          // Delete to p2's implementation.
          Method method = ReflectUtil.getMethod(this, "initMirrors", IProgressMonitor.class);
          MirrorInfo[] mirrors = (MirrorInfo[])ReflectUtil.invokeMethod(method, this, monitor);
          if (mirrors == null)
          {
            mirrors = new MirrorInfo[0];
          }

          // Use only the first two thirds of the list.
          int limit = (mirrors.length + 2) / 3;
          if (limit == 0 && repositoryURI != null)
          {
            // Even if there are no mirrors treat it as a single mirror so that activities and performance can always be monitored.
            String locationString = repositoryURI.toString();
            if (!locationString.endsWith("/"))
            {
              locationString += '/';
            }

            mirrorActivities = new MirrorActivity[] { new MirrorActivity(locationString) };
          }
          else
          {
            mirrorActivities = new MirrorActivity[limit];
            for (int i = 0; i < limit; i++)
            {
              // Convert the mirror info to our mirror activity representation.
              MirrorInfo mirrorInfo = mirrors[i];
              String locationString = getLocationString(mirrorInfo);
              MirrorActivity mirrorActivity = new MirrorActivity(locationString);
              mirrorActivities[i] = mirrorActivity;
            }
          }

          // Do ourselves as a listener.
          if (eventBus != null)
          {
            eventBus.addListener(this);
          }
        }
      }

      private URI getBaseURI()
      {
        return ReflectUtil.getValue("baseURI", this);
      }

      private static String getLocationString(MirrorInfo mirrorInfo)
      {
        return ReflectUtil.getValue("locationString", mirrorInfo);
      }

      private static void log(String message, Throwable exception)
      {
        LogHelper.log(new Status(IStatus.ERROR, Activator.ID, message, exception));
      }

      /**
       * @author Ed Merks
       */
      private static class ArtifactActivity
      {
        private final MirrorActivity mirrorActivity;

        private long start;

        private long end;

        private long size;

        private int retryCount;

        public ArtifactActivity(MirrorActivity mirrorActivity)
        {
          this.mirrorActivity = mirrorActivity;
        }

        public MirrorActivity getMirrorActivity()
        {
          return mirrorActivity;
        }

        public void setStart()
        {
          this.start = System.currentTimeMillis();
        }

        public void setSize(long size)
        {
          end = System.currentTimeMillis();
          this.size = size;
        }

        public long getDuration()
        {
          return end - start;
        }

        public long getSpeed()
        {
          return size * 1000 / (end - start);
        }

        public long getSize()
        {
          return size;
        }

        public boolean retry(ArtifactActivity previousArtifactActivity)
        {
          if (previousArtifactActivity != null)
          {
            retryCount = previousArtifactActivity.retryCount + 1;
          }

          return retryCount < 3;
        }

        @Override
        public String toString()
        {
          return "ArtifactActivity [start=" + start + ", end=" + end + ", size=" + size + ", duration=" + getDuration() + ", speed=" + getSpeed() + "]";
        }
      }

      /**
       * @author Ed Merks
       */
      private static class MirrorActivity
      {
        private final String location;

        private final AtomicInteger usages = new AtomicInteger();

        private final AtomicInteger successes = new AtomicInteger();

        private final AtomicInteger failures = new AtomicInteger();

        private final AtomicLong speed = new AtomicLong();

        private boolean probed;

        public MirrorActivity(String location)
        {
          this.location = location;
        }

        public String getLocation()
        {
          return location;
        }

        public boolean isProbed()
        {
          return probed;
        }

        public void setProbed()
        {
          probed = true;
        }

        public int getUsages()
        {
          return usages.get();
        }

        public void incrementUsage()
        {
          usages.incrementAndGet();
        }

        public void decrementUsage()
        {
          usages.decrementAndGet();
        }

        public int getSuccesses()
        {
          return successes.get();
        }

        public void incrementSuccess()
        {
          successes.incrementAndGet();
        }

        public int getFailures()
        {
          return failures.get();
        }

        public void incrementFailure()
        {
          failures.incrementAndGet();
        }

        public long getSpeed()
        {
          return speed.get();
        }

        public synchronized void setSpeed(long speed)
        {
          long currentSpeed = this.speed.get();
          if (currentSpeed == 0)
          {
            currentSpeed = speed;
          }
          else
          {
            currentSpeed = (currentSpeed + speed) / 2;
          }

          this.speed.set(currentSpeed);
        }

        @Override
        public String toString()
        {
          return "MirrorActivity [location=" + location + ", usages=" + usages.get() + ", successes=" + successes.get() + ", failures=" + failures.get()
              + ", speed=" + speed.get() + ", probed=" + probed + "]";
        }
      }
    }
  }
}
