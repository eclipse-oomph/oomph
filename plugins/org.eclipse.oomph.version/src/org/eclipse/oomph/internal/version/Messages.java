/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.internal.version;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.internal.version.messages"; //$NON-NLS-1$

  public static String ReleaseManager_NoDescription_exception;

  public static String ReleaseManager_UnknownElement_exception;

  public static String VersionBuilder_Added_message;

  public static String VersionBuilder_AlredyIncluded_message;

  public static String VersionBuilder_AutomaticModule_message;

  public static String VersionBuilder_BadVersion_message;

  public static String VersionBuilder_Changed_message;

  public static String VersionBuilder_Checking_task;

  public static String VersionBuilder_Clean_task;

  public static String VersionBuilder_DebugOption_message;

  public static String VersionBuilder_DependencyRangeMin_message;

  public static String VersionBuilder_DependencyRangeMustIncludeMinimum_message;

  public static String VersionBuilder_DependencyRangeMustNotIncludeMaximum_message;

  public static String VersionBuilder_Feature_label;

  public static String VersionBuilder_FeatureBuilderMissing_message;

  public static String VersionBuilder_FeatureLabel;

  public static String VersionBuilder_HasBeenChanged_message;

  public static String VersionBuilder_MavenBadArtifactID_message;

  public static String VersionBuilder_MavenBadVersion_message;

  public static String VersionBuilder_MissingPlugin_message;

  public static String VersionBuilder_MissingVersionBuilder_message;

  public static String VersionBuilder_NoReleaseSpecFile_message;

  public static String VersionBuilder_NoSchemaBuilderNeed_message;

  public static String VersionBuilder_NotReferenceByFeature_message;

  public static String VersionBuilder_PackageRequiresVersion_message;

  public static String VersionBuilder_Plugin_label;

  public static String VersionBuilder_RedundantInclude_message;

  public static String VersionBuilder_RedundantReference_message;

  public static String VersionBuilder_ReleaseSpecProblem_message;

  public static String VersionBuilder_Remove_message;

  public static String VersionBuilder_ResolvesDifferently_message;

  public static String VersionBuilder_SchemaBuilderMissing_message;

  public static String VersionBuilder_Unreachable_message;

  public static String VersionBuilder_Unresolved_message;

  public static String VersionBuilder_UnresolveProject_message;

  public static String VersionBuilder_VersionDecreased_message;

  public static String VersionBuilder_VersionMustBeIncreasedManifestMustChange_message;

  public static String VersionBuilder_VersionMustBeIncreasedProjectContentsChanged_message;

  public static String VersionBuilder_VersionMustBeIncreaseReferencesChanged_message;

  public static String VersionBuilder_VersionRangeRequired_message;

  public static String VersionBuilder_VersionShouldBe_message;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
