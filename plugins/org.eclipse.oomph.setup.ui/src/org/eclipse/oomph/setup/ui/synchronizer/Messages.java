/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.ui.synchronizer.messages"; //$NON-NLS-1$

  public static String AbstractServiceDialog_title;

  public static String OptInDialog_message;

  public static String OptInDialog_yesButton_text;

  public static String OptInDialog_noButton_text;

  public static String OptInDialog_askMeLaterButton_text;

  public static String OptOutDialog_message;

  public static String OptOutDialog_yesButton_text;

  public static String OptOutDialog_noButton_text;

  public static String OptOutDialog_hint;

  public static String SynchronizerDialog_policyHelpMessage;

  public static String SynchronizerDialog_defaultMessage_selectWhatToRecord_allWorkspaces;

  public static String SynchronizerDialog_defaultMessage_selectWhatToRecord_installationWorkspaces;

  public static String SynchronizerDialog_defaultMessage_selectWhatToRecord_workspace;

  public static String SynchronizerDialog_defaultMessage_selectWhatToRecord_intoRecorderTarget;

  public static String SynchronizerDialog_defaultMessage_selectWhatToSyncWithRemote;

  public static String SynchronizerDialog_defaultMessage_selectWhatToRecordAndSync;

  public static String SynchronizerDialog_column_label;

  public static String SynchronizerDialog_column_local;

  public static String SynchronizerDialog_column_remote;

  public static String SynchronizerDialog_enableRecorderButton_text;

  public static String SynchronizerDialog_enableRecorderButton_tooltip;

  public static String SynchronizerDialog_serviceLabel;

  public static String SynchronizerDialog_title_recorder;

  public static String SynchronizerDialog_title_synchronizer;

  public static String SynchronizerDialog_choice_recordAlways;

  public static String SynchronizerDialog_choice_recordNever;

  public static String SynchronizerDialog_choice_recordSkip;

  public static String SynchronizerDialog_choice_syncConflict;

  public static String SynchronizerDialog_choice_syncLocal;

  public static String SynchronizerDialog_choice_syncRemote;

  public static String SynchronizerDialog_choice_syncAlways;

  public static String SynchronizerDialog_choice_syncNever;

  public static String SynchronizerDialog_choice_syncSkip;

  public static String SynchronizerManager_watchDogJob_name;

  public static String SynchronizerManager_requestDataTask_name;

  public static String SynchronizerPreferencePage_disabledHandler_text;

  public static String SynchronizerPreferencePage_noServiceAvailable;

  public static String SynchronizerPreferencePage_enableButton_syncWith;

  public static String SynchronizerPreferencePage_enableButton_syncWithService;

  public static String SynchronizerPreferencePage_tableCombo_tooltip;

  public static String SynchronizerPreferencePage_syncButton_text;

  public static String SynchronizerPreferencePage_viewButton_text;

  public static String SynchronizerPreferencePage_shellText;

  public static String SynchronizerPreferencePage_seeLinkforCredentials;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
