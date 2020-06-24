/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.util;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.util.messages"; //$NON-NLS-1$

  public static String DownloadUtil_ConnectionTimeout_message;

  public static String DownloadUtil_Downloading_message;

  public static String DownloadUtil_HTTPError_exception;

  public static String DownloadUtil_ProblemDownloading_message;

  public static String DownloadUtil_TimeoutDuringRead_message;

  public static String FileUtil_CouldNotDelete_exception;

  public static String FileUtil_CouldNotRename_exception;

  public static String FileUtil_DeletingFile_task;

  public static String FileUtil_DeletingFilesIn_task;

  public static String FileUtil_DeletingOldFilesJob_name;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
