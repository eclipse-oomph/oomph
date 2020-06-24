/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.impl.messages"; //$NON-NLS-1$

  public static String EclipseIniTaskImpl_Changing_message;

  public static String EclipseIniTaskImpl_FileHasChanged_message;

  public static String EclipseIniTaskImpl_Skipping_message;

  public static String LinkLocationTaskImpl_ProductExtensionLocationsAdded_message;

  public static String PreferenceTaskImpl_IgnoringPreference_message;

  public static String PreferenceTaskImpl_NoAuthorityKey_excpetion;

  public static String PreferenceTaskImpl_NonHierarchicalKey_exception;

  public static String PreferenceTaskImpl_NoSchemeKey_exception;

  public static String PreferenceTaskImpl_NullKey_exception;

  public static String PreferenceTaskImpl_NullValue_exception;

  public static String RedirectionTaskImpl_NoExecutable_exception;

  public static String ResourceCopyTaskImpl_CannotCopy_message;

  public static String ResourceCopyTaskImpl_CopyingFolder_message;

  public static String ResourceCopyTaskImpl_CopyingResource_message;

  public static String ResourceCopyTaskImpl_DownloadingResource_message;

  public static String ResourceCopyTaskImpl_UnsupportedCopyingFolder_message;

  public static String ResourceCopyTaskImpl_UnzippingResource_message;

  public static String ResourceCopyTaskImpl_UnzippingTempFile_message;

  public static String ResourceCreationTaskImpl_Creating_message;

  public static String StringSubstitutionTaskImpl_SettingVariable_message;

  public static String TextModifyTaskImpl_Modifying_message;

  public static String VariableTaskImpl_NotExecutable_exception;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
