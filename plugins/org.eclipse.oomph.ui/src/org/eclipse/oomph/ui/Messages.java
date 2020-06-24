/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.ui;

import org.eclipse.osgi.util.NLS;

public class Messages
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.ui.messages"; //$NON-NLS-1$

  public static String ErrorDialog_message_internalError;

  public static String ErrorDialog_title;

  public static String FilteredTreeWithoutWorkbench_refreshJob_name;

  public static String HelpSupport_contentNotFound;

  public static String PropertiesViewer_propertyColumn;

  public static String PropertiesViewer_valueColumn;

  public static String SearchField_refreshJob_name;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
