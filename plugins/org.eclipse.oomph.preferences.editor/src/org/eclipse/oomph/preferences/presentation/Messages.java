/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.preferences.presentation;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.preferences.presentation.messages"; //$NON-NLS-1$

  public static String AllPreferencesPreferencePage_AllPreferences_title;

  public static String AllPreferencesPreferencePage_Edit_label;

  public static String PreferencesActionBarContributor_Synchronized_label;

  public static String PreferencesActionBarContributor_Syncrhonized_description;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
