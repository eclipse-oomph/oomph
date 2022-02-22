/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.sync;

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.internal.sync.DataProvider.Location;
import org.eclipse.oomph.setup.internal.sync.Snapshot.WorkingCopy;
import org.eclipse.oomph.setup.sync.SyncAction;

import java.util.Map;

/**
 * @author Eike Stepper
 */
public class SynchronizerAdapter implements SynchronizerListener
{
  public SynchronizerAdapter()
  {
  }

  @Override
  public void syncStarted(Synchronization synchronization)
  {
  }

  @Override
  public void workingCopyCreated(Synchronization synchronization, WorkingCopy workingCopy)
  {
  }

  @Override
  public void tasksCollected(Synchronization synchronization, Location location, Map<String, SetupTask> oldTasks, Map<String, SetupTask> newTasks)
  {
  }

  @Override
  public void actionsComputed(Synchronization synchronization, Map<String, SyncAction> actions)
  {
  }

  @Override
  public void actionResolved(Synchronization synchronization, SyncAction action, String id)
  {
  }

  @Override
  public void commitStarted(Synchronization synchronization)
  {
  }

  @Override
  public void commitFinished(Synchronization synchronization, Throwable t)
  {
  }

  @Override
  public void lockReleased(Synchronization synchronization)
  {
  }
}
