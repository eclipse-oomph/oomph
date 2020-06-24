/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.git.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.git.impl.messages"; //$NON-NLS-1$

  public static String GitCloneTaskImpl_AddingFetchRefSpec_message;

  public static String GitCloneTaskImpl_AddingPushRefSpec_message;

  public static String GitCloneTaskImpl_AddingPushURI_message;

  public static String GitCloneTaskImpl_AddingSubmodules_message;

  public static String GitCloneTaskImpl_CheckingOutLocalBranch_message;

  public static String GitCloneTaskImpl_CloneCollision_message;

  public static String GitCloneTaskImpl_CloningRepo_message;

  public static String GitCloneTaskImpl_ConfigureSubmodule_message;

  public static String GitCloneTaskImpl_CreatingLocalBranch_message;

  public static String GitCloneTaskImpl_CreatingLocalTab_message;

  public static String GitCloneTaskImpl_DeletingClone_message;

  public static String GitCloneTaskImpl_FetchingRefSpec_message;

  public static String GitCloneTaskImpl_OpeningClone_message;

  public static String GitCloneTaskImpl_ResettingHard_message;

  public static String GitCloneTaskImpl_SettingConfig_message;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
