/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.presentation.templates;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.presentation.templates.messages"; //$NON-NLS-1$

  public static String GenericSetupTemplate_message_locationIsNotValidProjectPath;

  public static String GenericSetupTemplate_message_projectNameIsNotValid;

  public static String GenericSetupTemplate_message_projectIsNotAccessible;

  public static String GenericSetupTemplate_message_folderNameIsNotValid;

  public static String GenericSetupTemplate_message_fileExistsAt;

  public static String GenericSetupTemplate_message_fileNameIsNotValid;

  public static String GenericSetupTemplate_message_fileNameMissingSetupExtension;

  public static String GenericSetupTemplate_message_fileAlreadyExists;

  public static String GenericSetupTemplate_beforeEnablingTaskReplacePlaceholderWithRepoPath;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
