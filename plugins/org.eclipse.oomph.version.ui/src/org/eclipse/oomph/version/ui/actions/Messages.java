/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.version.ui.actions;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.version.ui.actions.messages"; //$NON-NLS-1$

  public static String AbstractAction_error_title;

  public static String AbstractAction_error_message;

  public static String AddNatureAction_jobName;

  public static String ConfigureBuilderAction_title;

  public static String ConfigureBuilderAction_applyConfigMessage;

  public static String ConfigureBuildersAction_title;

  public static String RemoveNatureAction_title;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
