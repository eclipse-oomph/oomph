/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.version;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.version.messages"; //$NON-NLS-1$

  public static String VersionUtil_BadBuildModel_exception;

  public static String VersionUtil_BadModelType_exception;

  public static String VersionUtil_BadProjectType_exception;

  public static String VersionUtil_CleaningWorkspace_job;

  public static String VersionUtil_DependencyRangeMin_message;

  public static String VersionUtil_RebuildingWorkspace_job;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
