/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.targlets.internal.core.listeners;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.targlets.internal.core.listeners.messages"; //$NON-NLS-1$

  public static String PomArtifactUpdater_Checking_task;

  public static String PomArtifactUpdater_Updating_task;

  public static String PomModulesUpdater_CheckingUpdates_task;

  public static String PomModulesUpdater_NotFile_message;

  public static String TargetDefinitionGenerator_CannotGenerateNonWorkspaceTarget;

  public static String TargetDefinitionGenerator_CheckingUpdates_task;

  public static String TargetDefinitionGenerator_GeneratedFrom_message;

  public static String TargetDefinitionGenerator_Generating_message;

  public static String TargetDefinitionGenerator_Updating_task;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
