/*
 * Copyright (c) 2019 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Eike Stepper
 */
public class ThreadPool extends ThreadPoolExecutor
{
  private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 4;

  private static final ThreadFactory THREAD_FACTORY = new ThreadFactory()
  {
    @Override
    public Thread newThread(Runnable r)
    {
      Thread thread = new Thread(r);
      thread.setDaemon(true);
      return thread;
    }
  };

  private final Object lock = new Object();

  private int executingTasks;

  public ThreadPool()
  {
    super(THREAD_COUNT, THREAD_COUNT, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), THREAD_FACTORY);
  }

  @Override
  public void execute(Runnable command)
  {
    synchronized (lock)
    {
      ++executingTasks;
    }

    super.execute(command);
  }

  @Override
  protected void afterExecute(Runnable command, Throwable t)
  {
    synchronized (lock)
    {
      if (--executingTasks == 0)
      {
        lock.notifyAll();
      }
    }
  }

  public void awaitFinished() throws InterruptedException
  {
    synchronized (lock)
    {
      while (executingTasks != 0)
      {
        lock.wait();
      }
    }
  }
}
