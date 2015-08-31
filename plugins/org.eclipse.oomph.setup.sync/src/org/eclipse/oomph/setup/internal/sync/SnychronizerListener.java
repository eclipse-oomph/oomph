/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.sync;

import org.eclipse.oomph.setup.sync.SyncAction;

/**
 * @author Eike Stepper
 */
public interface SnychronizerListener
{
  public void syncStarted(Synchronization synchronization);

  public void actionResolved(Synchronization synchronization, SyncAction action, String id);

  public void commitStarted(Synchronization synchronization);

  public void commitFinished(Synchronization synchronization, Throwable t);

  public void lockReleased(Synchronization synchronization);
}
