/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.projectconfig.presentation.sync;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.projectconfig.presentation.sync.messages"; //$NON-NLS-1$

  public static String ProjectConfigSynchronizerDialog_Edit_label;

  public static String ProjectConfigSynchronizerDialog_ModificationProblem_label;

  public static String ProjectConfigSynchronizerDialog_ModifiedManagedProperties_label;

  public static String ProjectConfigSynchronizerDialog_NewValue_label;

  public static String ProjectConfigSynchronizerDialog_OldValue_label;

  public static String ProjectConfigSynchronizerDialog_Overwite_message;

  public static String ProjectConfigSynchronizerDialog_Profile_label;

  public static String ProjectConfigSynchronizerDialog_ProfileValue_label;

  public static String ProjectConfigSynchronizerDialog_ProjectSpecificPreferenceModification_label;

  public static String ProjectConfigSynchronizerDialog_Propagate_label;

  public static String ProjectConfigSynchronizerDialog_Property_label;

  public static String ProjectConfigSynchronizerDialog_UnmanagedPoperties_label;

  public static String ProjectConfigSynchronizerDialog_UnmanagedPropertyModified_message;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
