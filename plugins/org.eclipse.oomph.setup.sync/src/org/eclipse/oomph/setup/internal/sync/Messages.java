/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.internal.sync;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.internal.sync.messages"; //$NON-NLS-1$

  public static String DataProvider_NotCurrent_exception;

  public static String DataProvider_NotFound_exception;

  public static String Snapshot_CouldNotRename_exception;

  public static String Synchronization_Conflict_exception;

  public static String Synchronization_DuplicateID_exception;

  public static String Synchronization_TooManyIDs_exception;

  public static String Synchronizer_OtherSyncLock_exception;

  public static String SynchronizerJob_job;

  public static String SyncUtil_CouldNotDelete_exception;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
