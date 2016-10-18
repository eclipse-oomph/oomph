/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.version;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import java.util.Map;

/**
 * @author Eike Stepper
 */
public interface IVersionBuilderArguments extends Map<String, String>
{
  public static final String DEFAULT_VALIDATOR_CLASS_NAME = "org.eclipse.oomph.version.digest.DigestValidator$BuildModel";

  public static final String RELEASE_PATH_ARGUMENT = "release.path";

  public static final String VALIDATOR_CLASS_ARGUMENT = "validator.class";

  public static final String IGNORE_MALFORMED_VERSIONS_ARGUMENT = "ignore.malformed.versions";

  public static final String IGNORE_SCHEMA_BUILDER_ARGUMENT = "ignore.schema.builder";

  public static final String IGNORE_FEATURE_NATURE_ARGUMENT = "ignore.feature.nature";

  public static final String IGNORE_DEBUG_OPTIONS_ARGUMENT = "ignore.debug.options";

  public static final String IGNORE_DEPENDENCY_RANGES_ARGUMENT = "ignore.missing.dependency.ranges";

  public static final String IGNORE_LAX_LOWER_BOUND_VERSIONS_ARGUMENT = "ignore.lower.bound.dependency.ranges";

  public static final String IGNORE_EXPORT_VERSIONS_ARGUMENT = "ignore.missing.export.versions";

  public static final String IGNORE_CONTENT_CHANGES_ARGUMENT = "ignore.feature.content.changes";

  public static final String IGNORE_CONTENT_REDUNDANCY_ARGUMENT = "ignore.feature.content.redundancy";

  public static final String CHECK_CLOSURE_COMPLETENESS_ARGUMENT = "check.feature.closure.completeness";

  public static final String CHECK_CLOSURE_CONTENT_ARGUMENT = "check.feature.closure.content";

  public static final String CHECK_MAVEN_POM_ARGUMENT = "check.maven.pom";

  public String getReleasePath();

  public String getValidatorClassName();

  public boolean isIgnoreMalformedVersions();

  public boolean isIgnoreFeatureNature();

  public boolean isIgnoreSchemaBuilder();

  public boolean isIgnoreDebugOptions();

  public boolean isIgnoreMissingDependencyRanges();

  public boolean isIgnoreLaxLowerBoundDependencyVersions();

  public boolean isIgnoreMissingExportVersions();

  public boolean isIgnoreFeatureContentChanges();

  public boolean isIgnoreFeatureContentRedundancy();

  public boolean isCheckFeatureClosureCompleteness();

  public boolean isCheckFeatureClosureContent();

  public boolean isCheckMavenPom();

  public void applyTo(IProject project) throws CoreException;
}
