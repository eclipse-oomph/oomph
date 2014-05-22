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
package org.eclipse.oomph.util;

import org.eclipse.oomph.internal.util.bundle.UtilPlugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Eike Stepper
 */
public abstract class JobQueue<K>
{
  public static final int DEFAULT_MAX_JOBS = 10;

  private final Comparator<KeyedJob> COMPARATOR = new Comparator<KeyedJob>()
  {
    public int compare(KeyedJob o1, KeyedJob o2)
    {
      int result = (o2.secondary ? 0 : 1) - (o1.secondary ? 0 : 1);
      if (result == 0)
      {
        result = o2.sequenceNumber - o1.sequenceNumber;
      }

      return result;
    }
  };

  private final Object latchLock = new Object();

  private final Map<K, KeyedJob> jobs = new HashMap<K, KeyedJob>();

  private final List<KeyedJob> pendingJobs = new ArrayList<KeyedJob>();

  private final int maxJobs;

  private int nextSequenceNumber;

  private CountDownLatch latch;

  private boolean isCanceled;

  public JobQueue(int maxJobs)
  {
    this.maxJobs = maxJobs;
  }

  public JobQueue()
  {
    this(DEFAULT_MAX_JOBS);
  }

  public final synchronized void cancel()
  {
    isCanceled = true;

    for (KeyedJob job : jobs.values())
    {
      job.cancel();
    }

    jobs.clear();
    pendingJobs.clear();
  }

  public final void enqueueAndWait(Collection<? extends K> keys)
  {
    CountDownLatch localLatch;
    synchronized (latchLock)
    {
      if (latch == null)
      {
        latch = new CountDownLatch(1);
      }

      localLatch = latch;
    }

    enqueue(keys);

    try
    {
      localLatch.await();
    }
    catch (InterruptedException ex)
    {
      throw new RuntimeException(ex);
    }

    int xxx;
    latch = null;
  }

  public final void enqueueAndWait(K... keys)
  {
    enqueueAndWait(Arrays.asList(keys));
  }

  public final void enqueueAndWait(K key)
  {
    enqueueAndWait(Collections.singleton(key));
  }

  public final void enqueue(Collection<? extends K> keys)
  {
    for (K key : keys)
    {
      enqueue(key);
    }
  }

  public final void enqueue(K... keys)
  {
    for (K key : keys)
    {
      enqueue(key);
    }
  }

  public final void enqueue(K key)
  {
    enqueue(key, false);
  }

  private synchronized void enqueue(K key, boolean secondary)
  {
    if (!isCanceled)
    {
      if (!shouldEnqueue(key))
      {
        return;
      }

      KeyedJob job = jobs.get(key);
      if (job != null)
      {
        if (!secondary && job.secondary && pendingJobs.contains(job))
        {
          job.secondary = false;
          Collections.sort(pendingJobs, COMPARATOR);
        }
      }
      else
      {
        job = new KeyedJob(key, ++nextSequenceNumber, secondary);
        jobs.put(job.key, job);

        if (isSchedulePossible())
        {
          job.schedule();
        }
        else
        {
          pendingJobs.add(job);
          Collections.sort(pendingJobs, COMPARATOR);
        }
      }
    }
  }

  private synchronized void dequeue(K key)
  {
    jobs.remove(key);

    if (!isCanceled && !pendingJobs.isEmpty())
    {
      KeyedJob job = pendingJobs.remove(0);
      job.schedule();
    }

    int xxx;
    if (latch != null && jobs.isEmpty())
    {
      latch.countDown();
    }
  }

  private boolean isSchedulePossible()
  {
    int all = jobs.size() - 1;
    int pending = pendingJobs.size();
    int running = all - pending;
    return running < maxJobs;
  }

  protected boolean shouldEnqueue(K key)
  {
    return true;
  }

  protected abstract String getJobName(K key);

  protected abstract void runInJob(K key, IProgressMonitor monitor) throws Exception;

  /**
   * @author Eike Stepper
   */
  private class KeyedJob extends Job
  {
    private final K key;

    private final int sequenceNumber;

    private boolean secondary;

    private KeyedJob(K key, int sequenceNumber, boolean secondary)
    {
      super(getJobName(key));
      this.key = key;
      this.sequenceNumber = sequenceNumber;
      this.secondary = secondary;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
      try
      {
        runInJob(key, monitor);
      }
      catch (Throwable t)
      {
        UtilPlugin.INSTANCE.log(t);
      }
      finally
      {
        dequeue(key);
      }

      return Status.OK_STATUS;
    }

    @Override
    public String toString()
    {
      return "key=" + key + ", sequenceNumber=" + sequenceNumber + ", secondary=" + secondary;
    }
  }
}
