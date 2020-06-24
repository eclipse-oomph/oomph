/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.projects.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.projects.impl.messages"; //$NON-NLS-1$

  public static String PathVariableTaskImpl_SettingPathVariable_message;

  public static String ProjectsBuildTaskImpl_Build_message_part;

  public static String ProjectsBuildTaskImpl_Clean_message_part;

  public static String ProjectsBuildTaskImpl_NothingToDo_message;

  public static String ProjectsBuildTaskImpl_Refresh_message_part;

  public static String ProjectsBuildTaskImpl_Refreshing_message;

  public static String ProjectsImportTaskImpl_Analysis_message;

  public static String ProjectsImportTaskImpl_AnalysisOf_message;

  public static String ProjectsImportTaskImpl_Importing_message;

  public static String ProjectsImportTaskImpl_NoProjectsFound_message;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
