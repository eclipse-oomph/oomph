/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.version.ui.quickfixes;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.version.ui.quickfixes.messages"; //$NON-NLS-1$

  public static String AbstractResolution_jobName;

  public static String AddNatureResolution_label_addVersionManagementBuilder;

  public static String AddNatureResolution_label_addNature;

  public static String ConfigureResolution_label;

  public static String IgnoreRootProjectsResolution_label;

  public static String IgnoreRootProjectsResolution_description;

  public static String PropertiesResolution_description_addValueToKeyOfPropertiesFile;

  public static String PropertiesResolution_rootProjects_label;

  public static String PropertiesResolution_ignoredReferences_label;

  public static String ReleasePathResolution_label;

  public static String ReleasePathResolution_description;

  public static String ReplaceResolution_label_removeSchemaBuilder;

  public static String ReplaceResolution_label_addSchemaBuilder;

  public static String ReplaceResolution_label_addFeatureBuilder;

  public static String ReplaceResolution_label_addPluginReference;

  public static String ReplaceResolution_label_changeDebugOption;

  public static String ReplaceResolution_label_changeAutomaticModuleName;

  public static String ReplaceResolution_label_changeMavenArtifactId;

  public static String ReplaceResolution_label_changeMavenVersion;

  public static String ReplaceResolution_label_removeReference;

  public static String ReplaceResolution_label_changeVersion;

  public static String ReplaceResolution_label_changeToExactVersion;

  public static String ReplaceResolution_label_to;

  public static String VersionResolutionGenerator_changeToOmniVersion;

  public static String VersionResolutionGenerator_changeVersionTo;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
