/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.workingsets.presentation;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.workingsets.presentation.messages"; //$NON-NLS-1$

  public static String WorkingSetsActionBarContributor_OtherProjects_label;

  public static String WorkingSetsActionBarContributor_ShowPreference_label;

  public static String WorkingSetsActionBarContributor_WorkingSetsPreview_title;

  public static String WorkingSetsModelWizard_WorkingSetGroup_label;

  public static String WorkingSetsPreferencePage_Apply_label;

  public static String WorkingSetsPreferencePage_DynamiWorkingSetPreferences_title;

  public static String WorkingSetsPreferencePage_Edit_label;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
