/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.ui.recorder;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.ui.recorder.messages"; //$NON-NLS-1$

  public static String PreferenceInitializationDialog_title;

  public static String PreferenceInitializationDialog_dialogTitle;

  public static String PreferenceInitializationDialog_defaultMessage;

  public static String PreferenceInitializationDialog_checkBoxTree_rootLabel;

  public static String PreferenceInitializationDialog_visitingPreferencePagesTask_name;

  public static String PreferenceInitializationDialog_preferenceInvalidState_message;

  public static String RecorderManager_initializeItem_tooltip;

  public static String RecorderManager_recordItem_tooltip_recorderEnabledPushToDisable;

  public static String RecorderManager_recordItem_tooltip_recorderDisabledPushToEnabled;

  public static String RecorderManager_storePreferencesJob_name;

  public static String RecorderManager_requestDataTask_name;

  public static String RecorderPreferencePage_recordingSettingsUnavailableForSelectedFile;

  public static String RecorderPreferencePage_enableButton_text;

  public static String RecorderPreferencePage_targetCombo_tooltip;

  public static String RecorderPreferencePage_saveChangedPoliciesDialog_title;

  public static String RecorderPreferencePage_saveChangedPoliciesDialog_message;

  public static String RecorderPreferencePage_editButton_tooltip;

  public static String RecorderPreferencePage_policiesLabel;

  public static String RecorderPreferencePage_initializePreferencesButton_text;

  public static String RecorderPreferencePage_openTransactionJob_name;

  public static String RecorderTransaction_preferenceCompound_name;

  public static String RecorderTransaction_changeCommand_label;

  public static String RecorderTransaction_changeCommand_description;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
