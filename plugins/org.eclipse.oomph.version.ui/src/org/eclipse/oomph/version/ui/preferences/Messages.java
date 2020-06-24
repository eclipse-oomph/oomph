/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.version.ui.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.version.ui.preferences.messages"; //$NON-NLS-1$

  public static String VersionBuilderPreferencePage_column_release;

  public static String VersionBuilderPreferencePage_column_checkMode;

  public static String VersionBuilderPreferencePage_column_lowerBoundCheckMode;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
