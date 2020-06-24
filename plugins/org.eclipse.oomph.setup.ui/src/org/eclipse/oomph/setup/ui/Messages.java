/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.ui.messages"; //$NON-NLS-1$

  public static String AbstractConfirmDialog_accept;

  public static String AbstractConfirmDialog_decline;

  public static String EnablementComposite_offlineButton_text;

  public static String EnablementComposite_offlineButton_tooltip;

  public static String EnablementComposite_mirrorsButton_text;

  public static String EnablementComposite_mirrorsButton_tooltip;

  public static String EnablementComposite_iconLoaderJob_name;

  public static String EnablementComposite_installExtensionsJob_name;

  public static String EnablementDialog_title;

  public static String EnablementDialog_buttonLabel;

  public static String EnablementDialog_description;

  public static String IndexManagerDialog_title;

  public static String IndexManagerDialog_description;

  public static String IndexManagerDialog_dialogTitle;

  public static String IndexManagerDialog_availabilityCheckerJob_name;

  public static String IndexManagerDialog_tooltip_name;

  public static String IndexManagerDialog_tooltip_location;

  public static String IndexManagerDialog_tooltip_availability;

  public static String IndexManagerDialog_tooltip_availability_unknown;

  public static String IndexManagerDialog_tooltip_availability_available;

  public static String IndexManagerDialog_tooltip_availability_unavailable;

  public static String IndexManagerDialog_scopeLabel;

  public static String IndexManagerDialog_indexCombo_local;

  public static String IndexManagerDialog_indexCombo_localAndGlobal;

  public static String IndexManagerDialog_indexCombo_global;

  public static String IndexManagerDialog_removeButton_text;

  public static String LicenseDialog_title;

  public static String LicenseDialog_rememberButton_text;

  public static String LicenseDialog_shellText;

  public static String LicenseDialog_defaultMessage;

  public static String LicensePrePrompter_defaultLicense_name;

  public static String LicensePrePrompter_defaultLicense_info;

  public static String LicensePrePrompter_defaultMessage;

  public static String LicensePrePrompter_acceptButton_text;

  public static String LicensePrePrompter_decideLaterButton_text;

  public static String P2TaskUIServicesPrompter_title;

  public static String P2TaskUIServicesPrompter_rememberButton_text;

  public static String P2TaskUIServicesPrompter_defaultMessage;

  public static String P2TaskUIServicesPrompter_shellText;

  public static String P2TaskUIServicesPrompter_installButton_text;

  public static String P2TaskUIServicesPrompter_skipButton_text;

  public static String PropertyField_folderField_text;

  public static String PropertyField_folderField_message;

  public static String PropertyField_fileField_text;

  public static String PropertyField_containerField_text;

  public static String PropertyField_containerField_message;

  public static String PropertyField_linkButton_tooltip;

  public static String PropertyField_folderField_buttonText;

  public static String PropertyField_fileField_buttonText;

  public static String PropertyField_containerField_buttonText;

  public static String PropertyField_jreField_buttonText;

  public static String PropertyField_jreField_bitness;

  public static String PropertyField_authenticatedField_buttonText;

  public static String PropertyField_authenticatedField_authenticationStatus;

  public static String PropertyField_authenticatedField_status_authenticated;

  public static String PropertyField_authenticatedField_status_cannotAuthenticate;

  public static String PropertyField_authenticatedField_status_invalidPassword;

  public static String SetupEditorSupport_loadJob_name;

  public static String SetupLabelProvider_catalogIndex;

  public static String SetupPreferencePage_skipStartupTasks_label;

  public static String SetupPreferencePage_skipStartupTasks_tooltip;

  public static String SetupPreferencePage_p2StartupTasks_label;

  public static String SetupPreferencePage_p2StartupTasks_prompt;

  public static String SetupPreferencePage_p2StartupTasks_always;

  public static String SetupPreferencePage_p2StartupTasks_never;

  public static String SetupPreferencePage_p2StartupTasks_labelTooltip;

  public static String SetupPreferencePage_showToolbarContributions_label;

  public static String SetupPreferencePage_showToolbarContributions_tooltip;

  public static String SetupPreferencePage_showProgressInWizard_label;

  public static String SetupPreferencePage_showProgressInWizard_tooltip;

  public static String SetupPreferencePage_preferredTextEditor_text;

  public static String SetupPreferencePage_preferredTextEditor_dialogMessage;

  public static String SetupPreferencePage_preferredTextEditor_labelTooltip;

  public static String SetupPreferencePage_questionnaireButton_text;

  public static String SetupUIPlugin_remoteDebugPauseDialog_title;

  public static String SetupUIPlugin_remoteDebugPauseDialog_message;

  public static String SetupUIPlugin_setupCheckJob_name;

  public static String SetupUIPlugin_initSetupModelsJob_name;

  public static String SetupUIPlugin_initSetupModelsJob_taskName;

  public static String SetupUIPlugin_locationVariableResolver_description;

  public static String SetupUIPlugin_loadRestartTasks_taskName;

  public static String SetupUIPlugin_createPerformerTask_name;

  public static String SetupUIPlugin_initPerformerTask_name;

  public static String SetupUIPlugin_launchSetupWizardTask_name;

  public static String UnsignedContentDialog_title;

  public static String UnsignedContentDialog_rememberButton_text;

  public static String UnsignedContentDialog_shellText;

  public static String UnsignedContentDialog_defaultMessage;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
