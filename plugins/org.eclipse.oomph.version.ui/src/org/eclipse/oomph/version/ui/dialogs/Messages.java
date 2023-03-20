/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.version.ui.dialogs;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.version.ui.dialogs.messages"; //$NON-NLS-1$

  public static String ConfigurationDialog_title;

  public static String ConfigurationDialog_pathLabel;

  public static String ConfigurationDialog_emptyPathToSpecFile;

  public static String ConfigurationDialog_message;

  public static String ConfigurationDialog_ignoreMalformedVersionsButton_text;

  public static String ConfigurationDialog_ignoreFeatureNatureButton_text;

  public static String ConfigurationDialog_ignoreSchemaBuilderButton_text;

  public static String ConfigurationDialog_ignoreDebugOptionsButton_text;

  public static String ConfigurationDialog_ignoreAutomaticModuleNameButton_text;

  public static String ConfigurationDialog_ignoreMissingDependencyRangesButton_text;

  public static String ConfigurationDialog_ignoreLaxLowerBoundDependencyButton_text;

  public static String ConfigurationDialog_ignoreMissingExportVersionsButton_text;

  public static String ConfigurationDialog_ignoreFeatureContentChangesButton_text;

  public static String ConfigurationDialog_ignoreFeatureContentRedundancyButton_text;

  public static String ConfigurationDialog_checkFeatureClosureCompletenessButton_text;

  public static String ConfigurationDialog_checkFeatureClosureContentButton_text;

  public static String ConfigurationDialog_checkMavenPomButton_text;

  public static String ConfigurationDialog_error_cantDoRedundancyAndCompletenessChecksAtSameTime;

  public static String ConfigurationDialog_IgnoreMissingNature;

  public static String ExtendedConfigurationDialog_title;

  public static String ExtendedConfigurationDialog_message;

  public static String ExtendedConfigurationDialog_column_ignoreMalformedVersions;

  public static String ExtendedConfigurationDialog_column_ignoreFeatureNature;

  public static String ExtendedConfigurationDialog_column_ignoreSchemaBuilder;

  public static String ExtendedConfigurationDialog_column_ignoreDebugOptions;

  public static String ExtendedConfigurationDialog_column_ignoreMissingVersionRanges;

  public static String ExtendedConfigurationDialog_column_ignoreLaxLowerVersionBound;

  public static String ExtendedConfigurationDialog_column_ignoreMissingPackageExportVersions;

  public static String ExtendedConfigurationDialog_column_ignoreFeatureChanges;

  public static String ExtendedConfigurationDialog_column_ignoreFeatureRedundancy;

  public static String ExtendedConfigurationDialog_column_checkFeatureClosureCompleteness;

  public static String ExtendedConfigurationDialog_column_checkFeatureClosureContent;

  public static String ExtendedConfigurationDialog_column_checkMavenPom;

  public static String ExtendedConfigurationDialog_column_project;

  public static String ExtendedConfigurationDialog_IgnoreMissingVersionNature;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
