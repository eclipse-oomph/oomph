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
package org.eclipse.oomph.resources.backend;

import org.eclipse.oomph.internal.resources.ResourcesPlugin;
import org.eclipse.oomph.resources.ResourcesUtil.ImportResult;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.SynchronizedCounter;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Eike Stepper
 */
public abstract class BackendSystem extends BackendContainer
{
  private static final BackendResource[] NO_MEMBERS = new BackendResource[0];

  private static final String[] EMPTY_SEGMENTS = {};

  private static final URI EMPTY_URI = URI.createHierarchicalURI(EMPTY_SEGMENTS, null, null);

  private final String systemURI; // Store as string to not lock this system in the system registry's weak map.

  private final AtomicInteger visitorCounter = new AtomicInteger();

  private VisitorThreadPool visitorThreadPool;

  protected BackendSystem(URI systemURI) throws BackendException
  {
    super(null, EMPTY_URI);
    this.systemURI = systemURI.toString();
  }

  public final URI getSystemURI()
  {
    return URI.createURI(systemURI);
  }

  @Override
  public final Type getType()
  {
    return Type.SYSTEM;
  }

  protected void beginVisitor()
  {
    visitorThreadPool = new VisitorThreadPool(BackendSystem.this);
  }

  protected void endVisitor()
  {
    visitorThreadPool.dispose();
    visitorThreadPool = null;
  }

  protected Object beginConnected()
  {
    return null;
  }

  protected void endConnected()
  {
  }

  protected int getMaxThreads()
  {
    return Integer.MAX_VALUE;
  }

  protected abstract Object getDelegate(BackendResource backendResource) throws Exception;

  protected abstract Object[] getDelegateMembers(Object containerDelegate, IProgressMonitor monitor) throws Exception;

  protected abstract Object getDelegateMember(Object containerDelegate, String relativePath, IProgressMonitor monitor) throws Exception;

  protected abstract String getDelegateName(Object resourceDelegate) throws Exception;

  protected abstract Type getDelegateType(Object resourceDelegate, boolean checkExists) throws Exception;

  protected abstract IPath getLocation(BackendResource backendResource) throws Exception;

  protected abstract boolean exists(BackendResource backendResource, IProgressMonitor monitor) throws Exception;

  protected abstract long getLastModified(BackendResource backendResource, IProgressMonitor monitor) throws Exception;

  protected abstract InputStream getContents(BackendFile backendFile, IProgressMonitor monitor) throws Exception;

  protected abstract ImportResult importIntoWorkspace(BackendContainer backendContainer, IProject project, IProgressMonitor monitor) throws Exception;

  protected final BackendFolder createBackendFolder(URI systemRelativeURI)
  {
    return new BackendFolder(this, systemRelativeURI);
  }

  protected final BackendFile createBackendFile(URI systemRelativeURI)
  {
    return new BackendFile(this, systemRelativeURI);
  }

  protected BackendResource[] getMembers(BackendContainer backendContainer, IProgressMonitor monitor) throws Exception
  {
    Object delegate = getDelegate(backendContainer);

    Object[] delegateMembers = getDelegateMembers(delegate, monitor);
    if (delegateMembers == null)
    {
      return NO_MEMBERS;
    }

    BackendResource[] result = new BackendResource[delegateMembers.length];
    for (int i = 0; i < delegateMembers.length; i++)
    {
      ResourcesPlugin.checkCancelation(monitor);

      Object delegateMember = delegateMembers[i];
      String name = getDelegateName(delegateMember);
      URI systemRelativeURI = backendContainer.getSystemRelativeURI().appendSegment(name);

      result[i] = createMember(delegateMember, systemRelativeURI, false);
    }

    Arrays.sort(result, new Comparator<BackendResource>()
    {
      public int compare(BackendResource r1, BackendResource r2)
      {
        int t1 = r1 instanceof BackendContainer ? 0 : 1;
        int t2 = r2 instanceof BackendContainer ? 0 : 1;

        int result = t2 - t1;
        if (result == 0)
        {
          result = r1.getName().compareTo(r2.getName());
        }

        return result;
      }
    });

    return result;
  }

  protected BackendResource findMember(BackendContainer backendContainer, URI relativeURI, IProgressMonitor monitor) throws Exception
  {
    Object delegate = getDelegate(backendContainer);

    Object delegateMember = getDelegateMember(delegate, relativeURI.toString(), monitor);
    URI systemRelativeURI = backendContainer.getSystemRelativeURI().appendSegments(relativeURI.segments());

    return createMember(delegateMember, systemRelativeURI, true);
  }

  private BackendResource createMember(Object delegateMember, URI systemRelativeURI, boolean checkExists) throws Exception
  {
    BackendSystem system = getSystem();
    Type type = system.getDelegateType(delegateMember, checkExists);
    if (type == null)
    {
      return null;
    }

    switch (type)
    {
      case FOLDER:
        return system.createBackendFolder(systemRelativeURI);

      case FILE:
        return system.createBackendFile(systemRelativeURI);

      default:
        throw new BackendException("The " + type.toString().toLowerCase() + " can't be a member: " + delegateMember);
    }
  }

  protected final void accept(final BackendResource backendResource, final Visitor visitor, final IProgressMonitor monitor) throws Exception
  {
    synchronized (visitorCounter)
    {
      if (visitorCounter.incrementAndGet() == 1)
      {
        beginVisitor();
      }
    }

    doAccept(backendResource, visitor, monitor);

    synchronized (visitorCounter)
    {
      if (visitorCounter.decrementAndGet() == 0)
      {
        endVisitor();
      }
    }
  }

  protected void doAccept(BackendResource backendResource, Visitor visitor, IProgressMonitor monitor) throws Exception
  {
    SynchronizedCounter counter = new SynchronizedCounter();

    Queue<BackendResource> queue = new ConcurrentLinkedQueue<BackendResource>();
    queue.offer(backendResource);

    for (;;)
    {
      BackendResource polledResource = queue.poll();
      if (polledResource != null)
      {
        VisitorThread thread = visitorThreadPool.checkout();
        if (thread != null)
        {
          thread.scheduleVisit(polledResource, queue, counter, visitor, monitor);
        }
        else
        {
          polledResource.visit(queue, visitor, monitor);
        }
      }
      else
      {
        synchronized (counter)
        {
          if (counter.isZero())
          {
            break;
          }

          counter.awaitChange();
        }
      }
    }
  }

  @Override
  protected boolean doVisit(BackendContainer backendContainer, Visitor visitor, IProgressMonitor monitor) throws BackendException, OperationCanceledException
  {
    return visitor.visit(this, monitor);
  }

  /**
   * @author Eike Stepper
   */
  private static final class VisitorThread extends Thread
  {
    private static int lastID;

    private final Object mutex = new Object();

    private final VisitorThreadPool pool;

    private BackendResource backendResource;

    private Queue<BackendResource> queue;

    private SynchronizedCounter counter;

    private Visitor visitor;

    private IProgressMonitor monitor;

    public VisitorThread(VisitorThreadPool pool)
    {
      super("VisitorThread-" + (++lastID));
      this.pool = pool;
      setDaemon(true);
    }

    public void scheduleVisit(BackendResource backendResource, Queue<BackendResource> queue, SynchronizedCounter counter, Visitor visitor,
        IProgressMonitor monitor)
    {
      counter.countUp();

      synchronized (mutex)
      {
        this.backendResource = backendResource;
        this.queue = queue;
        this.counter = counter;
        this.visitor = visitor;
        this.monitor = monitor;
        mutex.notifyAll();
      }
    }

    @Override
    public void run()
    {
      BackendSystem backendSystem = pool.getBackendSystem();
      backendSystem.beginConnected();

      try
      {
        while (!isInterrupted())
        {
          try
          {
            doVisit();
          }
          catch (OperationCanceledException ex)
          {
            return;
          }
          catch (InterruptedException ex)
          {
            return;
          }
        }
      }
      finally
      {
        backendSystem.endConnected();
      }
    }

    private void doVisit() throws InterruptedException
    {
      synchronized (mutex)
      {
        while (backendResource == null)
        {
          mutex.wait();
        }
      }

      if (isInterrupted())
      {
        throw new InterruptedException();
      }

      try
      {
        backendResource.visit(queue, visitor, monitor);
      }
      catch (OperationCanceledException ex)
      {
        throw ex;
      }
      catch (Exception ex)
      {
        if (ex instanceof InterruptedException)
        {
          throw (InterruptedException)ex;
        }

        ResourcesPlugin.INSTANCE.log(ex);
      }
      finally
      {
        synchronized (mutex)
        {
          // Must happen before countDown() because that can dispose of the pool.
          pool.checkin(this);

          // Can dispose of the pool.
          counter.countDown();

          backendResource = null;
          queue = null;
          counter = null;
          visitor = null;
          monitor = null;
        }
      }
    }

    @Override
    public void interrupt()
    {
      if (monitor != null)
      {
        monitor.setCanceled(true);
      }

      synchronized (mutex)
      {
        mutex.notifyAll();
      }

      super.interrupt();
    }

    @Override
    public String toString()
    {
      return super.toString();
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class VisitorThreadPool
  {
    private static final String PROP_MAX_THREADS = "oomph.resources.VisitorThreadPool.MAX_THREADS";

    private static final int DEFAULT_MAX_THREADS = 10;

    private static final int MAX_THREADS = Integer.parseInt(PropertiesUtil.getProperty(PROP_MAX_THREADS, "" + DEFAULT_MAX_THREADS));

    private static final String PROP_SKIP_THRESHOLD = "oomph.resources.VisitorThreadPool.SKIP_THRESHOLD";

    private static final int DEFAULT_SKIP_THRESHOLD = MAX_THREADS / 2;

    private static final int SKIP_THRESHOLD = Integer.parseInt(PropertiesUtil.getProperty(PROP_SKIP_THRESHOLD, "" + DEFAULT_SKIP_THRESHOLD));

    private final LinkedList<VisitorThread> threads = new LinkedList<VisitorThread>();

    private final Set<VisitorThread> checkouts = new HashSet<VisitorThread>();

    private final BackendSystem backendSystem;

    private final int maxThreads;

    private int skippedThreadCreations;

    private boolean disposed;

    public VisitorThreadPool(BackendSystem backendSystem)
    {
      this.backendSystem = backendSystem;
      maxThreads = Math.min(MAX_THREADS, backendSystem.getMaxThreads());
    }

    public BackendSystem getBackendSystem()
    {
      return backendSystem;
    }

    public synchronized VisitorThread checkout()
    {
      if (!disposed && maxThreads > 0)
      {
        if (!threads.isEmpty())
        {
          VisitorThread thread = threads.removeFirst();
          checkouts.add(thread);
          return thread;
        }

        int currentNumberOfThreads = checkouts.size(); // Here we know that the pool is empty.
        if (currentNumberOfThreads < maxThreads)
        {
          int threadCreationsToSkip = currentNumberOfThreads / SKIP_THRESHOLD;
          if (++skippedThreadCreations >= threadCreationsToSkip)
          {
            skippedThreadCreations = 0;

            VisitorThread thread = new VisitorThread(this);
            thread.start();
            checkouts.add(thread);
            return thread;
          }
        }
      }

      // Let the calling thread do the work.
      return null;
    }

    public synchronized void checkin(VisitorThread thread)
    {
      if (!disposed)
      {
        checkouts.remove(thread);
        threads.addLast(thread);
      }
    }

    public synchronized void dispose()
    {
      for (VisitorThread thread : threads)
      {
        thread.interrupt();
      }

      for (VisitorThread thread : checkouts)
      {
        thread.interrupt();
      }

      threads.clear();
      checkouts.clear();
      disposed = true;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class Registry
  {
    public static final Registry INSTANCE = new Registry();

    private final Map<URI, BackendSystem> backendSystems = new WeakHashMap<URI, BackendSystem>();

    private Registry()
    {
    }

    public synchronized BackendSystem getBackendSystem(URI systemURI) throws BackendException
    {
      if (systemURI.hasTrailingPathSeparator())
      {
        systemURI = systemURI.trimSegments(1);
      }

      BackendSystem backendSystem = backendSystems.get(systemURI);
      if (backendSystem == null)
      {
        IFactory factory = IFactory.Registry.INSTANCE.getFactory(systemURI.scheme());
        backendSystem = factory.createBackendSystem(systemURI);
        backendSystems.put(systemURI, backendSystem);
      }

      return backendSystem;
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface IFactory
  {
    public BackendSystem createBackendSystem(URI systemURI) throws BackendException;

    /**
     * @author Eike Stepper
     */
    public static final class Registry
    {
      public static final Registry INSTANCE = new Registry();

      private final Map<String, IFactory> factories = new HashMap<String, IFactory>();

      private Registry()
      {
        addFactory("file", new LocalBackendSystem.Factory());
      }

      private IFactory loadFactory(String scheme) throws BackendException
      {
        if (ResourcesPlugin.INSTANCE.isOSGiRunning())
        {
          try
          {
            IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();

            for (IConfigurationElement configurationElement : extensionRegistry
                .getConfigurationElementsFor("org.eclipse.oomph.resources.backendSystemFactories"))
            {
              String factoryScheme = configurationElement.getAttribute("scheme");
              if (ObjectUtil.equals(factoryScheme, scheme))
              {
                try
                {
                  return (IFactory)configurationElement.createExecutableExtension("class");
                }
                catch (Exception ex)
                {
                  ResourcesPlugin.INSTANCE.log(ex);
                }
              }
            }
          }
          catch (Exception ex)
          {
            ResourcesPlugin.INSTANCE.log(ex);
          }
        }

        throw new BackendException("Backend system factory with scheme '" + scheme + "' not found");
      }

      public synchronized IFactory getFactory(String scheme) throws BackendException
      {
        IFactory factory = factories.get(scheme);
        if (factory == null)
        {
          factory = loadFactory(scheme);
          factories.put(scheme, factory);
        }

        return factory;
      }

      public synchronized IFactory addFactory(String scheme, IFactory factory) throws BackendException
      {
        return factories.put(scheme, factory);
      }

      public synchronized IFactory removeFactory(String scheme) throws BackendException
      {
        return factories.remove(scheme);
      }

      public synchronized Set<String> removeFactory(IFactory factory) throws BackendException
      {
        Set<String> schemes = new HashSet<String>();
        for (Iterator<Map.Entry<String, IFactory>> it = factories.entrySet().iterator(); it.hasNext();)
        {
          Map.Entry<String, IFactory> entry = it.next();
          if (entry.getValue() == factory)
          {
            schemes.add(entry.getKey());
            it.remove();
          }
        }

        return schemes;
      }
    }
  }
}
