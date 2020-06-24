/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.ui.questionnaire;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.ui.questionnaire.messages"; //$NON-NLS-1$

  public static String ExitShell_lines_youCanTakeQuestionnaireLater;

  public static String ExitShell_exitConfirmation;

  public static String ExitShell_exitNow;

  public static String ExitShell_goBack;

  public static String GearAnimator_titles_welcome;

  public static String GearAnimator_titles_refreshResources;

  public static String GearAnimator_titles_showLineNumbers;

  public static String GearAnimator_titles_checkSpelling;

  public static String GearAnimator_titles_executeJobsInBackground;

  public static String GearAnimator_titles_encodeTextFiles;

  public static String GearAnimator_titles_enablePreferenceRecorder;

  public static String GearShell_finish;

  public static String GearShell_text;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
