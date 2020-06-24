/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.presentation.handlers.messages"; //$NON-NLS-1$

  public static String AbstractDropdownHandler_error;

  public static String ImportProjectsHandler_text;

  public static String OpenDiscoveredTypeHandler_text;

  public static String OpenEditorDropdownFactory_menu_parentModels;

  public static String OpenEditorDropdownHandler_openItem;

  public static String OpenLogHandler_text;

  public static String PerformHandler_text;

  public static String RefreshCacheHandler_text;

  public static String SynchronizePreferencesHandler_text;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
