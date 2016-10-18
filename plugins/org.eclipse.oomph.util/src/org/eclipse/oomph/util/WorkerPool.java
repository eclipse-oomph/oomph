/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.util;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An abstract class to maintain a pool of {@link Worker workers}.
 * @param <P> the type of the pool itself.
 * @param <K> the type of the key identifying the work that needs to be performed.
 * @param <W> the type of the worker.
 *
 * @author Eike Stepper
 */
public abstract class WorkerPool<P extends WorkerPool<P, K, W>, K, W extends WorkerPool.Worker<K, P>>
{
  /**
   * A comparator for ordering the workers in the pool.
   * Workers are ordered such that {@link Worker#secondary secondary} priority workers will be processed later.
   * Otherwise workers are processed in the {@link Worker#id identifier order},
   * i.e., first come first serve.
   */
  private static final Comparator<Worker<?, ?>> COMPARATOR = new Comparator<Worker<?, ?>>()
  {
    public int compare(Worker<?, ?> o1, Worker<?, ?> o2)
    {
      int result = (o2.secondary ? 0 : 1) - (o1.secondary ? 0 : 1);
      if (result == 0)
      {
        result = o2.id - o1.id;
      }

      return result;
    }
  };

  /**
   * The maximum number of simultaneously performer workers.
   */
  private final int maxWorker = 10;

  /**
   * The map of the worker key to the worker associated with that key.
   */
  private final Map<K, W> workers = new HashMap<K, W>();

  /**
   * The workers waiting to perform their work.
   */
  private final List<W> pendingWorkers = new ArrayList<W>();

  /**
   * Whether the workers have been canceled.
   */
  private final AtomicBoolean workCanceled = new AtomicBoolean();

  /**
   * The worker ID that will be {@link #createWorker(Object, int, boolean) allocated} to the next worker.
   */
  private int nextWorkerID;

  /**
   * The latch used to block {@link #perform(Collection) performing} the work until all work is completed.
   */
  private CountDownLatch latch;

  /**
   * Whether the work has been {@link #cancel() canceled}.
   */
  private boolean isCanceled;

  /**
   * The monitor passed when the work {@link #begin(String, IProgressMonitor) begins}.
   * This may be <code>null</code> if the work is not being monitored.
   */
  private IProgressMonitor monitor;

  /**
   * Creates an instance.
   */
  protected WorkerPool()
  {
  }

  /**
   * Creates a worker for the given key, ID, and priority.
   */
  protected abstract W createWorker(K key, int workerID, boolean secondary);

  /**
   * Begins the work as a monitored task.
   * Clients are generally expected to provide a public method that calls this method
   * and in that case must override {@link #run(String, IProgressMonitor) run} to do the actual work.
   */
  protected final void begin(String taskName, IProgressMonitor monitor)
  {
    this.monitor = monitor;
    run(taskName, monitor);
    dispose();
  }

  /**
   * Does the actual work.
   * This is essentially an abstract method and must be specialized if the work is to be monitored.
   */
  protected void run(String taskName, IProgressMonitor monitor)
  {
    throw new UnsupportedOperationException();
  }

  protected IProgressMonitor getMonitor()
  {
    return monitor;
  }

  /**
   * Cleans up the pool.
   */
  public void dispose()
  {
    monitor = null;
  }

  /**
   * Cancels the work in progress.
   */
  public synchronized void cancel()
  {
    isCanceled = true;

    for (W worker : workers.values())
    {
      worker.cancel();
    }

    workers.clear();
    pendingWorkers.clear();
  }

  /**
   * Returns whether the work should be canceled.
   */
  public boolean isCanceled()
  {
    if (monitor != null && monitor.isCanceled() && !workCanceled.getAndSet(true))
    {
      cancel();
    }

    return isCanceled;
  }

  /**
   * Performs the work for the given key,
   * blocking until the work has been performed.
   */
  public final void perform(K key)
  {
    perform(Collections.singleton(key));
  }

  /**
   * Performs the work for the given keys,
   * blocking until the work has been performed.
   */
  public final void perform(K... keys)
  {
    perform(Arrays.asList(keys));
  }

  /**
   * Performs the work for the given keys,
   * blocking until the work has been performed.
   */
  public void perform(Collection<? extends K> keys)
  {
    if (latch == null)
    {
      latch = new CountDownLatch(1);
    }

    if (schedule(keys))
    {
      try
      {
        await(-1L);
      }
      catch (InterruptedException ex)
      {
        throw new RuntimeException(ex);
      }
    }

    latch = null;
  }

  protected boolean await(long timeout) throws InterruptedException
  {
    if (timeout == -1L)
    {
      latch.await();
      return true;
    }

    return latch.await(timeout, TimeUnit.MILLISECONDS);
  }

  /**
   * Schedules work for the given key.
   * It does not block.
   * The work will be performed at some later point in time on a different thread.
   * Returns whether any work is actually scheduled (needed) for of the given key.
   */
  public final boolean schedule(K key)
  {
    return schedule(Collections.singleton(key));
  }

  /**
   * Schedules work for the given keys.
   * It does not block.
   * The work will be performed at some later point in time on a different thread.
   * Returns whether any work is actually scheduled (needed) for any of the given keys.
   */
  public final boolean schedule(K... keys)
  {
    return schedule(Arrays.asList(keys));
  }

  /**
   * Schedules work for the given keys.
   * It does not block.
   * The work will be performed at some later point in time on a different thread.
   * Returns whether any work is actually scheduled (needed) for any of the given keys.
   */
  public synchronized boolean schedule(Collection<? extends K> keys)
  {
    boolean result = false;
    for (K key : keys)
    {
      if (schedule(key, false))
      {
        result = true;
      }
    }

    return result;
  }

  /**
   * Schedules work for the given key with the specified priority.
   * It does not block.
   * The work will be performed at some later point in time on a different thread.
   * Returns whether any work is actually scheduled (needed) for any of the give key.
   */
  protected synchronized boolean schedule(K key, boolean secondary)
  {
    if (isCanceled() || isCompleted(key))
    {
      return false;
    }

    W worker = workers.get(key);
    if (worker != null)
    {
      if (!secondary && worker.secondary && pendingWorkers.contains(worker))
      {
        worker.secondary = false;
        Collections.sort(pendingWorkers, COMPARATOR);
      }
    }
    else
    {
      worker = createWorker(key, ++nextWorkerID, secondary);
      workers.put(key, worker);

      if (isWorkPossible())
      {
        worker.schedule();
      }
      else
      {
        pendingWorkers.add(worker);
        Collections.sort(pendingWorkers, COMPARATOR);
      }
    }

    return true;
  }

  /**
   * Deschedules the work for the given key when that work has been completed.
   * If there are {@link #pendingWorkers pending workings},
   * the next one is {@link Worker#schedule() scheduled} to perform its work.
   * If there are no remaining {@link #workers},
   * the {@link #latch latch} is unlatched.
   */
  private synchronized void deschedule(K key)
  {
    workers.remove(key);

    if (!isCanceled() && !pendingWorkers.isEmpty())
    {
      W worker = pendingWorkers.remove(0);
      worker.schedule();
    }

    if (latch != null && workers.isEmpty())
    {
      latch.countDown();
    }
  }

  /**
   * Returns whether another worker can be {@link Worker#schedule() scheduled} without exceeding the {@link #maxWorker maximum} number of currently performing workers.
   */
  private boolean isWorkPossible()
  {
    int all = workers.size() - 1;
    int pending = pendingWorkers.size();
    int running = all - pending;
    return running < maxWorker;
  }

  /**
   * Returns whether work has been completed for the given key.
   * It returns <code>false</code> by default.
   */
  protected boolean isCompleted(K key)
  {
    return false;
  }

  /**
   * An abstract worker, which is a specialized type of {@link Job Job}.
   * @param <K> the type of key identifying the work that needs to be performed.
   * @param <P> the type of {@link WorkerPool pool} managing the worker.
   * @author Eike Stepper
   */
  public static abstract class Worker<K, P extends WorkerPool<? extends P, K, ? extends Worker<K, P>>> extends Job
  {
    /**
     * The work pool managing this worker.
     */
    private P workPool;

    /**
     * The key identifying the type of work that needs to be performed.
     */
    private final K key;

    /**
     * The ID {@link WorkerPool#createWorker(Object, int, boolean) allocated} by the pool to this worker.
     */
    private final int id;

    /**
     * The priority {@link WorkerPool#createWorker(Object, int, boolean) allocated} by the pool to this worker.
     */
    private boolean secondary;

    /**
     * Creates an instance with the given Job name,
     * managed by the given work pool,
     * for the given key,
     * with the given ID and priority.
     */
    protected Worker(String name, P workPool, K key, int id, boolean secondary)
    {
      super(name);
      this.workPool = workPool;
      this.key = key;
      this.id = id;
      this.secondary = secondary;
    }

    /**
     * performs the actual work.
     */
    protected abstract IStatus perform(IProgressMonitor monitor);

    /**
     * Returns the key identifying the work to be performed by this worker.
     */
    public K getKey()
    {
      return key;
    }

    /**
     * Returns the work pool managing this worker.
     */
    public P getWorkPool()
    {
      return workPool;
    }

    /**
     * {@link #perform(IProgressMonitor) performs the work} and {@link WorkerPool#deschedule(Object) deschedules} the worker upon completion.
     */
    @Override
    protected IStatus run(IProgressMonitor monitor)
    {
      try
      {
        return perform(monitor);
      }
      finally
      {
        workPool.deschedule(key);
      }
    }

    @Override
    public String toString()
    {
      return "key=" + key + ", secondary=" + secondary + ", id=" + id;
    }
  }
}
