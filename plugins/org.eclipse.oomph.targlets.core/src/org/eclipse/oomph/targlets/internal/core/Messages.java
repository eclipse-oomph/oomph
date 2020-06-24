/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.targlets.internal.core.messages"; //$NON-NLS-1$

  public static String TargletContainer_CannotInstall_message;

  public static String TargletContainer_UpdateComplete_task;

  public static String TargletContainerDescriptor_ConflictingTransaction_exception;

  public static String TargletContainerDescriptor_NoTransaction_exception;

  public static String TargletContainerListenerRegistry_Sending_task;

  public static String TargletContainerResourceFactory_Resolve_job;

  public static String WorkspaceIUAnalyzer_Analysis_message;

  public static String WorkspaceIUImporter_Imports_job;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
