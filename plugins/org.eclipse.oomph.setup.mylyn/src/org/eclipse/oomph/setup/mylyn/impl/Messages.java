/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.mylyn.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.mylyn.impl.messages"; //$NON-NLS-1$

  public static String MylynBuildsTaskImpl_AddingBuildPlan_message;

  public static String MylynBuildsTaskImpl_AddingServer_message;

  public static String MylynQueriesTaskImpl_AddingQuery_message;

  public static String MylynQueriesTaskImpl_AddingRepository_message;

  public static String MylynQueriesTaskImpl_ChaningQuery_message;

  public static String MylynQueriesTaskImpl_RemovingQueryAttribute_message;

  public static String MylynQueriesTaskImpl_SettingQuery_message;

  public static String MylynQueriesTaskImpl_SettingQueryAttribute_message;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
