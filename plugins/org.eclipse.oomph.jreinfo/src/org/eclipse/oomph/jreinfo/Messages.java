/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.jreinfo;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.jreinfo.messages"; //$NON-NLS-1$

  public static String InfoManager_InvalidJREEntry_message;

  public static String JRE_Current_message;

  public static String JREFilter_BadMicro_exception;

  public static String JREFilter_BadMinor_exception;

  public static String JREInfo_FolderDoesNotExist_exception;

  public static String JREManager_Problem_message;

  public static String JREManager_Seraching_task;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
