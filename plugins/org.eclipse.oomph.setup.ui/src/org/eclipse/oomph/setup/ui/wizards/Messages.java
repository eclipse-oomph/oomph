/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.ui.wizards.messages"; //$NON-NLS-1$

  public static String CatalogSelector_menuItem_text;

  public static String CatalogSelector_manageItem_text;

  public static String ConfigurationProcessor_configurationStatus_error;

  public static String ConfigurationProcessor_installationStatus_error;

  public static String ConfigurationProcessor_installationStatus_configEmpty;

  public static String ConfigurationProcessor_workspaceStatus_error;

  public static String ConfigurationProcessor_workspaceStatus_configEmpty;

  public static String ConfigurationProcessor_status_noProductVersion;

  public static String ConfigurationProcessor_status_productNotInIndex;

  public static String ConfigurationProcessor_status_unresolvableVersion;

  public static String ConfigurationProcessor_status_versionNotInProduct;

  public static String ConfigurationProcessor_status_emptyResource;

  public static String ConfigurationProcessor_status_resourceContains;

  public static String ConfigurationProcessor_status_couldNotLoadFromUris;

  public static String ConfigurationProcessor_configHandlingDialog_title;

  public static String ConfigurationProcessor_configHandlingDialog_message;

  public static String ConfigurationProcessor_configHandlingDialog_button_advancedMode;

  public static String ConfigurationProcessor_configHandlingDialog_button_cancel;

  public static String ConfigurationProcessor_configHandlingDialog_button_applyOnlyInstallation;

  public static String ConfigurationProcessor_status_unresolvableStream;

  public static String ConfigurationProcessor_status_streamNotInProject;

  public static String ConfigurationProcessor_status_rootProjectNotInCatalog_noContainer;

  public static String ConfigurationProcessor_status_rootProjectNotInCatalog_unresolvableContainer;

  public static String ConfigurationProcessor_status_rootProjectNotInCatalog_notInContainer;

  public static String ConfigurationProcessor_status_cannotAddRootProject;

  public static String ConfigurationProcessor_status_noExtensibleProjectInCatalog;

  public static String ConfigurationProcessor_status_cannotAddCatalog;

  public static String ConfigurationProcessor_status_noRedirectableProductCatalog;

  public static String ConfigurationProcessor_status_noRedirectableProjectCatalog;

  public static String ConfigurationProcessor_status_catalogAlreadyRedirected;

  public static String ConfigurationProcessor_scopeStatus_message;

  public static String ConfirmationPage_title;

  public static String ConfirmationPage_description;

  public static String ConfirmationPage_showAllButton_text;

  public static String ConfirmationPage_showAllButton_tooltip;

  public static String ConfirmationPage_offlineButton_text;

  public static String ConfirmationPage_offlineButton_tooltip;

  public static String ConfirmationPage_mirrorsButton_text;

  public static String ConfirmationPage_mirrorsButton_tooltip;

  public static String ConfirmationPage_overwriteButton_text;

  public static String ConfirmationPage_overwriteButton_tooltip;

  public static String ConfirmationPage_switchWorkspaceButton_text;

  public static String ConfirmationPage_switchWorkspaceButton_tooltip;

  public static String ConfirmationPage_labelProvider_tasks;

  public static String ConfirmationPage_labelProvider_tasks_for;

  public static String ConfirmationPage_copyAction_text;

  public static String ConfirmationPage_column_nestedElements;

  public static String ConfirmationPage_ideWillBeRestartedWithNewWorkspace;

  public static String ConfirmationPage_noTasksToPerform;

  public static String ConfirmationPage_checkOneOrMoreTasksToContinue;

  public static String ConfirmationPage_error_folderExists;

  public static String ConfirmationPage_error_workspaceLocationChanged;

  public static String ExtensionsDialog_title;

  public static String ExtensionsDialog_header;

  public static String ExtensionsDialog_description;

  public static String ExtensionsDialog_selectAllButton_text;

  public static String ExtensionsDialog_deselectAllButton_text;

  public static String MarketPlaceListingProcessor_status_problemsEncountered;

  public static String MarketPlaceListingProcessor_status_noUpdateSiteInListing;

  public static String MarketPlaceListingProcessor_status_noIusInListing;

  public static String MarketPlaceListingProcessor_shellText;

  public static String MarketPlaceListingProcessor_defaultMessage;

  public static String MarketPlaceListingProcessor_tooltip_loading;

  public static String MarketPlaceListingProcessor_repositoryLoaderJob_name;

  public static String MarketPlaceListingProcessor_problemDialog_title;

  public static String MarketPlaceListingProcessor_problemDialog_message;

  public static String MarketPlaceListingProcessor_selectAllButton_text;

  public static String MarketPlaceListingProcessor_deselectAllButton_text;

  public static String MarketPlaceListingProcessor_status_emptyResource;

  public static String MarketPlaceListingProcessor_status_resourceContains;

  public static String MarketPlaceListingProcessor_status_couldNotLoadFromUris;

  public static String MarketPlaceListingProcessor_MarketPlaceListingStatus_message;

  public static String ProgressPage_title;

  public static String ProgressPage_description;

  public static String ProgressPage_desktopShortcut_description;

  public static String ProgressPage_desktopShortcut_text;

  public static String ProgressPage_scrollLockButton_text;

  public static String ProgressPage_scrollLockButton_tooltip;

  public static String ProgressPage_dismissButton_text;

  public static String ProgressPage_dismissButton_tooltip;

  public static String ProgressPage_launchButton_text;

  public static String ProgressPage_launchButton_tooltip;

  public static String ProgressPage_restartButton_text;

  public static String ProgressPage_restartButton_tooltip;

  public static String ProgressPage_minimizeButton_tooltip;

  public static String ProgressPage_error_couldNotRenameInstallation;

  public static String ProgressPage_executeTasksJob_name;

  public static String ProgressPage_log_renamedFolder;

  public static String ProgressPage_log_tookSeconds;

  public static String ProgressPage_log_startup;

  public static String ProgressPage_log_startup_updatingPreference;

  public static String ProgressPage_log_startup_updatingPreferences;

  public static String ProgressPage_log_startup_seeLogForDetails;

  public static String ProgressPage_log_restartIsNeeded;

  public static String ProgressPage_log_pressFinishOrCancel;

  public static String ProgressPage_log_pressFinishToClose;

  public static String ProgressPage_log_taskWasCanceled;

  public static String ProgressPage_log_failedTasks;

  public static String ProgressPage_log_pressBackOrCancel;

  public static String ProgressPage_taskSuccessfulAndRequiresRestart;

  public static String ProgressPage_pressFinishOrCancel;

  public static String ProgressPage_taskSuccessful;

  public static String ProgressPage_pressBackOrFinish;

  public static String ProgressPage_taskCanceled;

  public static String ProgressPage_failedTasks;

  public static String ProgressPage_pressBackOrCancel;

  public static String ProgressPage_taskFailed;

  public static String ProgressPage_log_launchingProduct;

  public static String ProgressPage_log_productLaunchFailed;

  public static String ProgressPage_log_crossPlatformInstallLaunchNotPossible;

  public static String ProjectPage_title;

  public static String ProjectPage_description;

  public static String ProjectPage_addProjectButton_tooltip;

  public static String ProjectPage_removeProjectButton_tooltip;

  public static String ProjectPage_collapseAllButton_tooltip;

  public static String ProjectPage_catalogsButton_tooltip;

  public static String ProjectPage_projectViewer_addButton_tooltip;

  public static String ProjectPage_projectViewer_removeButton_tooltip;

  public static String ProjectPage_column_catalog;

  public static String ProjectPage_column_project;

  public static String ProjectPage_column_stream;

  public static String ProjectPage_status_workspaceProblems;

  public static String ProjectPage_error_anotherWizardAlreadyOpen;

  public static String ProjectPage_addUserProjectDialog_title;

  public static String ProjectPage_addUserProjectDialog_catalog;

  public static String ProjectPage_uriDoesNotContainValidProject;

  public static String ProjectPage_urisDoNotContainValidProjects;

  public static String ProjectPage_and;

  public static String ProjectPage_projectAlreadyInIndex;

  public static String ProjectPage_projectsAlreadyInIndex;

  public static String ProjectPage_noUrisSpecified;

  public static String ProjectPage_errorDialog_title;

  public static String ProjectPage_applyConfigurationButton_tooltip_switchToCatalogIndex;

  public static String ProjectPage_applyConfigurationButton_tooltip_applyConfig;

  public static String ProjectPage_ShowLocationDetails_message;

  public static String SetupWizard_importer_title;

  public static String SetupWizard_ExistingProcessPage_errorMessage;

  public static String SetupWizard_ExistingProcessPage_anotherSetupProcessAlreadyActive;

  public static String SetupWizard_ExistingProcessPage_completeOtherProcessBeforeImporting;

  public static String SetupWizard_ExistingProcessPage_openExistingSetupButton_text;

  public static String SetupWizard_ExistingProcessPage_existingProcessFinished;

  public static String SetupWizard_ExistingProcessPage_doYouWantToOpenAgain;

  public static String SetupWizard_updater_title;

  public static String VariablePage_description_basic;

  public static String VariablePage_description_addition;

  public static String VariablePage_setupTaskAnalysis_title;

  public static String VariablePage_setupTaskAnalysis_message;

  public static String VariablePage_title;

  public static String VariablePage_fullPromptButton_text;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
