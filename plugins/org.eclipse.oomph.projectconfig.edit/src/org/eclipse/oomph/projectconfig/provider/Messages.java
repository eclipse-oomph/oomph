/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.projectconfig.provider;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.projectconfig.provider.messages"; //$NON-NLS-1$

  public static String PreferenceFilterItemProvider_Update_command;

  public static String PreferenceProfileItemProvider_Referents_label;

  public static String ProjectItemProvider_Exceptional_label;

  public static String ProjectItemProvider_For_message_part;

  public static String ProjectItemProvider_Invalid_label;

  public static String ProjectItemProvider_Partial_label;

  public static String ProjectItemProvider_References_label;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
