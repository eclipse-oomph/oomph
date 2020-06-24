/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.util;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.util.messages"; //$NON-NLS-1$

  public static String Confirmer_Accept_label;

  public static String Confirmer_Decline_label;

  public static String Confirmer_Remember_label;

  public static String LockFile_CannotLock_exception;

  public static String OomphPlugin_ConfigurationUnavailable_exception;

  public static String OomphPlugin_FileExists_excpetion;

  public static String OomphPlugin_InvalidDirectories_exception;

  public static String OomphPlugin_InvalidFiles_exception;

  public static String OomphPlugin_NullBundle_exception;

  public static String OS_CannotStartTerminal_exception;

  public static String OS_OS_NotSupported_exception;

  public static String ReflectUtil_NoField_exception;

  public static String ServiceUtil_MissingService_exception;

  public static String StringUtil_ShouldMatch_exception;

  public static String StringUtil_TooShort_exception;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
