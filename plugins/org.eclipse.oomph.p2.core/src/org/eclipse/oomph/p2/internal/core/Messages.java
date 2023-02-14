/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.p2.internal.core.messages"; //$NON-NLS-1$

  public static String AgentAnalyzer_Analyzing_task;

  public static String AgentAnalyzer_AnalyzingArtifacts_task;

  public static String AgentAnalyzer_AnalyzingBundlePool_job;

  public static String AgentAnalyzer_Deleting_task;

  public static String AgentAnalyzer_LoadingProfiles_task;

  public static String AgentAnalyzer_Repairing_task;

  public static String AgentAnalyzer_RepairingArtifacts_task;

  public static String AgentAnalyzer_Validating_task;

  public static String AgentImpl_AgentNotLoaded_exception;

  public static String AgentImpl_ConfigurationWrittenBy_message;

  public static String AgentImpl_Deleting_task;

  public static String AgentImpl_DeletingRepositoryCache_task;

  public static String AgentImpl_EngineNotLoaded_exception;

  public static String AgentImpl_PlannerNotLoaded_exception;

  public static String AgentImpl_Refreshing_task;

  public static String AgentManagerElementImpl_Used_exception;

  public static String AgentManagerImpl_AgentNotLoaded_exception;

  public static String AgentManagerImpl_Client_message;

  public static String AgentManagerImpl_Refreshing_task;

  public static String AgentManagerImpl_RefreshingAgents_task;

  public static String BundlePoolImpl_CachCannotBeChanged_exception;

  public static String BundlePoolImpl_PoolNotLoaded_exception;

  public static String BundlePoolImpl_SharedBundlePool_label;

  public static String CachingRepositoryManager_AddingRepository_task;

  public static String CachingRepositoryManager_artifact;

  public static String CachingRepositoryManager_Failure_message;

  public static String CachingRepositoryManager_LoadingFailed_message;

  public static String CachingRepositoryManager_metadata;

  public static String CachingRepositoryManager_NonRelative_message;

  public static String CachingRepositoryManager_RepeatedDownload_task;

  public static String CachingRepositoryManager_Speed_message;

  public static String CachingTransport_Chained_exception;

  public static String CachingTransport_RepeatedDownloads_exception;

  public static String CachingTransport_ServiceNotAvailable_exception;

  public static String CachingTransport_UseOfflineCache_exception;

  public static String LazyProfile_ProfileNotLoaded_exception;

  public static String LazyProfileRegistry_CannotRemoveCurrentProfile_exception;

  public static String LazyProfileRegistry_Deleting_task;

  public static String LazyProfileRegistry_LoadFailure_exception;

  public static String LazyProfileRegistry_Loading_task;

  public static String LazyProfileRegistry_P2InternalsChanged_exception;

  public static String LazyProfileRegistry_Registering_task;

  public static String LazyProfileRegistry_RegistryDirectoryNotAvailable_exception;

  public static String LazyProfileRegistryComponent_ProblemCreatingDirecgtory_exception;

  public static String P2CorePlugin_OldFolder_message;

  public static String PersistentMap_LockInterrupted_exception;

  public static String PersistentMap_LockTimeout_exception;

  public static String ProfileImpl_ProfileNotExists_exception;

  public static String ProfileReferencerImpl_NotDirectory_exception;

  public static String ProfileReferencerImpl_NotFile_exception;

  public static String ProfileTransactionImpl_AddPGPSignature;

  public static String ProfileTransactionImpl_CollectingArtifacts_task;

  public static String ProfileTransactionImpl_CollectingArtifactsFor_task;

  public static String ProfileTransactionImpl_CouldNotBeChanged_message;

  public static String ProfileTransactionImpl_Installing_task;

  public static String ProfileTransactionImpl_LoadRepositories_task;

  public static String ProfileTransactionImpl_RepositoryLoader_thread;

  public static String RepositoryFinder_Found_message;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
