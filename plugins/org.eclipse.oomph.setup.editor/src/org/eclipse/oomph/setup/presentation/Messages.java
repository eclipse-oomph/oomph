/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.presentation;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.presentation.messages"; //$NON-NLS-1$

  public static String EnablementAction_tooltip;

  public static String EnablementDialog_title;

  public static String EnablementDialog_defaultMessage;

  public static String EnablementDialog_iconLoaderJob_name;

  public static String EnablementDialog_extensionTextLabel;

  public static String EnablementDialog_installButton_label;

  public static String OpenDiscoveredType_title;

  public static String OpenDiscoveredType_message;

  public static String OpenDiscoveredType_collapseAllButton_tooltip;

  public static String OpenDiscoveredType_action_openInExtBrowser;

  public static String OpenDiscoveredType_action_openInEditor;

  public static String OpenDiscoveredType_action_openInView;

  public static String OpenDiscoveredType_action_openInJavaEditor;

  public static String OpenDiscoveredType_action_openInImportProjectsWizard;

  public static String OpenDiscoveredType_gitIndexLoaderJob_name;

  public static String OpenDiscoveredType_detailLoaderJob_name;

  public static String OpenDiscoveredType_openJavaEditorJob_name;

  public static String OpenDiscoveredType_rootItem_name;

  public static String OpenDiscoveredType_placeHolderItem_name;

  public static String PreferenceCaptureDialog_shellText_importPrefs;

  public static String PreferenceCaptureDialog_shellText_capturePrefs;

  public static String PreferenceCaptureDialog_preferenceFileGroup_text;

  public static String PreferenceCaptureDialog_preferenceFileBrowseButton_text;

  public static String PreferenceCaptureDialog_filterGroup_text;

  public static String PreferenceCaptureDialog_availablePreferencesLabel;

  public static String PreferenceCaptureDialog_addButton_text;

  public static String PreferenceCaptureDialog_removeButton_text;

  public static String PreferenceCaptureDialog_selectedPreferencesLabel;

  public static String PreferenceCaptureDialog_defaultsToolItem_tooltip;

  public static String SetupActionBarContributor_action_showWorkingSetsPreview;

  public static String SetupActionBarContributor_action_recordPreferences;

  public static String SetupActionBarContributor_action_preferenceCapture_importPrefs;

  public static String SetupActionBarContributor_action_preferenceCapture_capturePrefs;

  public static String SetupActionBarContributor_action_testInstall;

  public static String SetupActionBarContributor_action_testInstall_tooltip;

  public static String SetupActionBarContributor_action_commandTable;

  public static String SetupActionBarContributor_action_commandTable_tooltip;

  public static String SetupActionBarContributor_html_Utf8IsUnsupported;

  public static String SetupActionBarContributor_action_editorTable;

  public static String SetupActionBarContributor_action_editorTable_tooltip;

  public static String SetupActionBarContributor_action_showResources;

  public static String SetupActionBarContributor_action_showResources_tooltip;

  public static String SetupActionBarContributor_action_openInSetupEditor;

  public static String SetupActionBarContributor_action_openInTextEditor;

  public static String SetupActionBarContributor_action_deleteUnrecognizedContent;

  public static String SetupActionBarContributor_action_showTooltips;

  public static String SetupActionBarContributor_action_showInformationBrowser;

  public static String SetupEditor_loadingResourceInput_text;

  public static String SetupEditor_loadingResourceSetInput_text;

  public static String SetupEditor_resolvedUriDescriptor_name;

  public static String SetupEditor_resolvedUriDescriptor_description;

  public static String SetupEditor_command_replaceWithMigratedContents;

  public static String SetupEditor_command_addPartiallyMigratedContents;

  public static String SetupEditor_closer_tooltip_problemsOnChildren;

  public static String SetupEditor_closer_tooltip_problems;

  public static String SetupEditor_closer_tooltip_description;

  public static String SetupEditor_closer_tooltip_properties;

  public static String SetupEditor_viewer_restricted;

  public static String SetupEditor_loadingModelJob_name;

  public static String SetupEditor_outlinePreviewPage_compositeStream_label;

  public static String SetupEditor_outlinePreviewPage_compositeStream_description;

  public static String SetupEditor_outlinePreviewPage_compositeConfiguration_label;

  public static String SetupEditor_outlinePreviewPage_compositeConfiguration_description;

  public static String SetupEditor_outlinePreviewPage_compositeMacro_label;

  public static String SetupEditor_outlinePreviewPage_compositeMacro_description;

  public static String SetupEditor_outlinePreviewPage_resolvedVariables;

  public static String SetupEditor_outlinePreviewPage_undeclaredVariables;

  public static String SetupEditor_outlinePreviewPage_unresolvedVariables;

  public static String SetupEditor_outlinePreviewPage_action_showTasksForAllTriggers;

  public static String SetupEditor_outlinePreviewPage_action_showTasksForTrigger;

  public static String SetupEditor_outlinePreviewPage_action_previewTriggeredTasks;

  public static String SetupEditor_setupLocationListener_menu_forwardMore;

  public static String SetupEditor_setupLocationListener_menu_backMore;

  public static String SetupEditor_setupLocationListener_backwardItem_tooltip;

  public static String SetupEditor_setupLocationListener_forwardItem_tooltip;

  public static String SetupEditor_setupLocationListener_showSetupItem_tooltip;

  public static String SetupEditor_setupLocationListener_editSetupItem_tooltip;

  public static String SetupEditor_setupLocationListener_showAdvancedPropertiesItem_tooltip;

  public static String SetupEditor_setupLocationListener_showPropertiesViewItem_tooltip;

  public static String SetupEditor_setupLocationListener_editTextItem_tooltip;

  public static String SetupEditor_setupLocationListener_openInfoBrowserItem_tooltip;

  public static String SetupEditor_setupLocationListener_showToolTipsItem_tooltip;

  public static String SetupEditor_setupLocationListener_liveValidationItem_tooltip;

  public static String SetupEditor_setupLocationListener_noHistory;

  public static String SetupEditor_setupLocationListener_setupInformationBrowser;

  public static String SetupModelWizard_previewButton;

  public static String SetupModelWizard_noTemplateSelected;

  public static String SetupModelWizard_template_eclipseProject;

  public static String SetupModelWizard_template_simpleProject;

  public static String SetupModelWizard_template_githubProject;

  public static String SetupModelWizard_template_simpleConfiguration;

  public static String SetupModelWizard_template_copyCurrentConfiguration;

  public static String SetupModelWizard_template_simpleIndex;

  public static String SetupModelWizard_template_simpleProductCatalog;

  public static String SetupModelWizard_template_simpleProjectCatalog;

  public static String SetupModelWizard_template_simpleUserProduct;

  public static String SetupModelWizard_template_macro;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
