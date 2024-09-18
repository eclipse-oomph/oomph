/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.p2.internal.ui.messages"; //$NON-NLS-1$

  public static String AdditionalURIPrompterDialog_artifactsDownloadError_firstTime;

  public static String AdditionalURIPrompterDialog_artifactsDownloadError;

  public static String AdditionalURIPrompterDialog_artifactsDownloadError_tryAgain;

  public static String AdditionalURIPrompterDialog_artifactLabel;

  public static String AdditionalURIPrompterDialog_repositoryLabel;

  public static String AdditionalURIPrompterDialog_checkAll_tooltip;

  public static String AdditionalURIPrompterDialog_uncheckAll_tooltip;

  public static String AdditionalURIPrompterDialog_uriLabel;

  public static String AdditionalURIPrompterDialog_uriButton_text;

  public static String AdditionalURIPrompterDialog_uriButton_tooltip;

  public static String AgentAnalyzerComposite_showAll;

  public static String AgentAnalyzerComposite_showUnused;

  public static String AgentAnalyzerComposite_showDamaged;

  public static String AgentAnalyzerComposite_showByArtifact;

  public static String AgentAnalyzerComposite_bundlePool_bundlePool;

  public static String AgentAnalyzerComposite_bundlePool_profiles;

  public static String AgentAnalyzerComposite_bundlePool_unusedProfiles;

  public static String AgentAnalyzerComposite_bundlePool_artifacts;

  public static String AgentAnalyzerComposite_bundlePool_unusedArtifacts;

  public static String AgentAnalyzerComposite_bundlePool_damangedArtifacts;

  public static String AgentAnalyzerComposite_bundlePool_deleteUnusedProfiles;

  public static String AgentAnalyzerComposite_bundlePool_deleteUnusedProfiles_withCount;

  public static String AgentAnalyzerComposite_bundlePool_deleteUnusedArtifacts;

  public static String AgentAnalyzerComposite_bundlePool_deleteUnusedArtifacts_withCount;

  public static String AgentAnalyzerComposite_bundlePool_repairDamagedArtifacts;

  public static String AgentAnalyzerComposite_bundlePool_repairDamagedArtifacts_withCount;

  public static String AgentAnalyzerComposite_profile_profile;

  public static String AgentAnalyzerComposite_profile_artifacts;

  public static String AgentAnalyzerComposite_profile_damagedArtifacts;

  public static String AgentAnalyzerComposite_profile_roots;

  public static String AgentAnalyzerComposite_profile_repositories;

  public static String AgentAnalyzerComposite_profile_selectAll;

  public static String AgentAnalyzerComposite_profile_selectAll_withCount;

  public static String AgentAnalyzerComposite_profile_deleteSelected;

  public static String AgentAnalyzerComposite_profile_deleteSelected_withCount;

  public static String AgentAnalyzerComposite_profile_details;

  public static String AgentAnalyzerComposite_profile_doYouWantToDeleteProfile;

  public static String AgentAnalyzerComposite_profile_doYouWantToDeleteProfiles;

  public static String AgentAnalyzerComposite_profile_deletingProfiles;

  public static String AgentAnalyzerComposite_artifact_artifact;

  public static String AgentAnalyzerComposite_artifact_version;

  public static String AgentAnalyzerComposite_artifact_profiles;

  public static String AgentAnalyzerComposite_artifact_selectAll;

  public static String AgentAnalyzerComposite_artifact_selectAll_withCount;

  public static String AgentAnalyzerComposite_artifact_deleteSelected;

  public static String AgentAnalyzerComposite_artifact_deleteSelected_withCount;

  public static String AgentAnalyzerComposite_artifact_repairSelected;

  public static String AgentAnalyzerComposite_artifact_repairSelected_withCount;

  public static String AgentAnalyzerComposite_artifact_doYouWantToDeleteArtifact;

  public static String AgentAnalyzerComposite_artifact_doYouWantToDeleteArtifacts;

  public static String AgentAnalyzerComposite_artifact_deleteArtifacts_note1;

  public static String AgentAnalyzerComposite_artifact_deleteArtifacts_note2;

  public static String AgentAnalyzerComposite_artifact_deletingArtifacts;

  public static String AgentAnalyzerDialog_title;

  public static String AgentAnalyzerDialog_defaultMessage;

  public static String AgentManagerComposite_newAgent_button_text;

  public static String AgentManagerComposite_newAgent_button_tooltip;

  public static String AgentManagerComposite_newAgent_selectLocation;

  public static String AgentManagerComposite_clearCache_text;

  public static String AgentManagerComposite_clearCache_tooltip;

  public static String AgentManagerComposite_cleanupAgent_text;

  public static String AgentManagerComposite_cleanupAgent_tooltip;

  public static String AgentManagerComposite_analyzeAgent_text;

  public static String AgentManagerComposite_analyzeAgent_tooltip;

  public static String AgentManagerComposite_newPool_button_text;

  public static String AgentManagerComposite_newPool_button_tooltip;

  public static String AgentManagerComposite_newPool_selectLocation;

  public static String AgentManagerComposite_selectPool_text;

  public static String AgentManagerComposite_selectPool_tooltip;

  public static String AgentManagerComposite_delete_text;

  public static String AgentManagerComposite_delete_tooltip;

  public static String AgentManagerComposite_refresh_text;

  public static String AgentManagerComposite_refresh_tooltip;

  public static String AgentManagerComposite_showProfiles_text;

  public static String AgentManagerComposite_showProfiles_tooltip;

  public static String AgentManagerComposite_profileDetails_text;

  public static String AgentManagerComposite_profilesDetails_tooltip;

  public static String AgentManagerComposite_deleteAgent_confirmation;

  public static String AgentManagerComposite_deleteAgent_filesWillRemainOnDisk;

  public static String AgentManagerComposite_deleteBundlePool_confirmation;

  public static String AgentManagerComposite_deleteBundlePool_filesWillRemainOnDisk;

  public static String AgentManagerComposite_deleteProfile_confirmation;

  public static String AgentManagerComposite_cleanup_title;

  public static String AgentManagerComposite_cleanup_analyzing;

  public static String AgentManagerComposite_cleanup_nothingToCleanUp;

  public static String AgentManagerComposite_cleanup_deleteConfirm_profile;

  public static String AgentManagerComposite_cleanup_deleteConfirm_profiles;

  public static String AgentManagerComposite_cleanup_deleteConfirm_artifact;

  public static String AgentManagerComposite_cleanup_deleteConfirm_artifacts;

  public static String AgentManagerComposite_cleanup_deleteConfirm_profileAndArtifact;

  public static String AgentManagerComposite_cleanup_deleteConfirm_profileAndArtifacts;

  public static String AgentManagerComposite_cleanup_deleteConfirm_profilesAndArtifact;

  public static String AgentManagerComposite_cleanup_deleteConfirm_profilesAndArtifacts;

  public static String AgentManagerComposite_cleanup_deleteConfirm_artifactDeleteNote;

  public static String AgentManagerComposite_cleanup_deleting;

  public static String AgentManagerComposite_cleanup_deletingUnusedProfiles;

  public static String AgentManagerComposite_cleanup_deletingUnusedArtifacts;

  public static String AgentManagerDialog_title;

  public static String AgentManagerDialog_message;

  public static String AgentManagerDialog_profilesShownMessage;

  public static String CacheUsageConfirmerUI_cacheAge_days;

  public static String CacheUsageConfirmerUI_cacheAge_hours;

  public static String CacheUsageConfirmerUI_cacheAge_minutes;

  public static String CacheUsageConfirmerUI_cacheAge_fewSeconds;

  public static String CacheUsageConfirmerUI_cacheAge_useCachedVersionsSuggestion;

  public static String CacheUsageConfirmerUI_downloadFailure;

  public static String CacheUsageConfirmerUI_uriCouldNotBeDownloaded;

  public static String P2ServiceUI_certs_rememberAccepted;

  public static String P2ServiceUI_certs_alwaysAccept;

  public static String ProfileDetailsComposite_headerRow_profile;

  public static String ProfileDetailsComposite_headerRow_agent;

  public static String ProfileDetailsComposite_headerRow_bundlePool;

  public static String ProfileDetailsComposite_headerRow_installation;

  public static String ProfileDetailsComposite_openFolder;

  public static String ProfileDetailsComposite_tab_definition;

  public static String ProfileDetailsComposite_tab_definition_requirements;

  public static String ProfileDetailsComposite_tab_definition_repositories;

  public static String ProfileDetailsComposite_tab_installedUnits;

  public static String ProfileDetailsComposite_tab_properties;

  public static String ProfileDetailsComposite_tab_properties_key;

  public static String ProfileDetailsComposite_tab_properties_value;

  public static String ProfileDetailsDialog_title;

  public static String ProfileDetailsDialog_defaultMessage;

  public static String RepositoryExplorer_repositoryCombo_tooltip;

  public static String RepositoryExplorer_action_generalCopy;

  public static String RepositoryExplorer_action_cut;

  public static String RepositoryExplorer_action_paste;

  public static String RepositoryExplorer_action_generalPaste;

  public static String RepositoryExplorer_searchField_tooltip;

  public static String RepositoryExplorer_versionSegmentButton_compatibleButton_text;

  public static String RepositoryExplorer_versionSegmentButton_compatibleButton_tooltip;

  public static String RepositoryExplorer_versionSegmentButton_major_text;

  public static String RepositoryExplorer_versionSegmentButton_major_tooltip;

  public static String RepositoryExplorer_versionSegmentButton_minor_text;

  public static String RepositoryExplorer_versionSegmentButton_minor_tooltip;

  public static String RepositoryExplorer_versionSegmentButton_micro_text;

  public static String RepositoryExplorer_versionSegmentButton_micro_tooltip;

  public static String RepositoryExplorer_versionSegmentButton_qualifier_text;

  public static String RepositoryExplorer_versionSegmentButton_qualifier_tooltip;

  public static String RepositoryExplorer_action_refresh;

  public static String RepositoryExplorer_action_refresh_tooltip;

  public static String RepositoryExplorer_action_expertMode;

  public static String RepositoryExplorer_action_showVersions;

  public static String RepositoryExplorer_iu_more;

  public static String RepositoryExplorer_action_copy;

  public static String RepositoryExplorer_action_showDetails;

  public static String RepositoryExplorer_iuDialog_title;

  public static String RepositoryExplorer_iuDialog_defaultMessage;

  public static String RepositoryExplorer_action_collapseAll_text;

  public static String RepositoryExplorer_action_collapseAll_tooltip;

  public static String RepositoryExplorer_action_searchRepositories_text;

  public static String RepositoryExplorer_action_searchRepositories_tooltip;

  public static String RepositoryExplorer_action_searchRequirements_text;

  public static String RepositoryExplorer_action_searchRequirements_tooltip;

  public static String RepositoryExplorer_action_findRepositories_text;

  public static String RepositoryExplorer_action_findRepositories_tooltip;

  public static String RepositoryExplorer_loadJob_name;

  public static String RepositoryExplorer_analyzeJob_name;

  public static String RepositoryExplorer_groupButton_text;

  public static String RepositoryExplorer_groupButton_tooltip;

  public static String RepositoryExplorer_analyzeIus_noItems;

  public static String RepositoryExplorer_analyzeIus_noItemsMatchingFilter;

  public static String RepositoryExplorer_analyzeIus_disableGroupItemsToSeeMore;

  public static String RepositoryExplorer_analyzeIus_noFeatureItems;

  public static String RepositoryExplorer_analyzeIus_noFeatureItemsMatchingFilter;

  public static String RepositoryExplorer_analyzeIus_enableExpertModeToSeeMore;

  public static String RepositoryExplorer_analyzeCategory_featureSource;

  public static String RepositoryExplorer_analyzeCategory_otherIuSource;

  public static String RepositoryExplorer_capabilitiesMode_namespaceCombo_tooltip;

  public static String RepositoryExplorer_capabilitiesMode_noItems;

  public static String RepositoryExplorer_capabilitiesMode_noItemsMatchingFilter;

  public static String RepositoryExplorer_repositoryCombo_initialText;

  public static String RepositoryExplorer_loadingItem_loadingLocation;

  public static String RepositoryFinderDialog_stringRepresentation;

  public static String RepositoryFinderDialog_title;

  public static String RepositoryFinderDialog_filterLabel_text;

  public static String RepositoryFinderDialog_searchField_tooltip;

  public static String RepositoryFinderDialog_statsLabel_repositories;

  public static String RepositoryFinderDialog_loadRepositoriesJob_name;

  public static String RepositoryFinderDialog_tooltip_composite;

  public static String RepositoryFinderDialog_tooltip_simple;

  public static String RepositoryFinderDialog_tooltip_repository;

  public static String RepositoryFinderDialog_tooltip_capability;

  public static String RepositoryFinderDialog_tooltip_capabilities;

  public static String RepositoryFinderDialog_tooltip_unresolvedChild;

  public static String RepositoryFinderDialog_tooltip_unresolvedChildren;

  public static String RepositoryFinderDialog_tooltip_compressed;

  public static String RepositoryFinderDialog_tooltip_children;

  public static String RepositoryFinderDialog_tooltip_composites;

  public static String RepositoryFinderDialog_exploreAction_text;

  public static String SearchEclipseDialog_collapseAllButton_tooltip;

  public static String SearchEclipseDialog_applyButton_text;

  public static String SearchEclipseDialog_okButton_text;

  public static String SearchEclipseDialog_loadingItem_namespace;

  public static String SearchEclipseDialog_capabilityLoaderJob_name;

  public static String SearchEclipseDialog_indexUnavailableItem_namespace;

  public static String SearchEclipseDialog_detailsLoadJob_name;

  public static String SearchEclipseDialog_applyHandler_tooltipPrefix;

  public static String SearchEclipseDialog_repositoryApplyHandler_tooltipPrefix;

  public static String SearchEclipseDialog_eObjectApplyHandler_tooltipPrefix;

  public static String SearchEclipseDialog_requirementApplyHandler_tooltipPrefix;

  public static String SearchEclipseDialog_capability;

  public static String SearchEclipseDialog_capabilities;

  public static String SearchEclipseDialog_repositoriesDialog_title;

  public static String SearchEclipseDialog_repositoriesDialog_message;

  public static String SearchEclipseDialog_requirementsDialog_title;

  public static String SearchEclipseDialog_requirementsDialog_message;

  public static String SearchEclipseDialog_workbenchPathApplyHandler_tooltip;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
