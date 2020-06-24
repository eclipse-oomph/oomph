/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.jreinfo.ui;

import org.eclipse.osgi.util.NLS;

public class Messages
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.jreinfo.ui.messages"; //$NON-NLS-1$

  public static String JREComposite_group_system;

  public static String JREComposite_group_user;

  public static String JREComposite_column_location;

  public static String JREComposite_column_version;

  public static String JREComposite_column_bitness;

  public static String JREComposite_column_type;

  public static String JREComposite_button_browse;

  public static String JREComposite_button_download;

  public static String JREComposite_button_remove;

  public static String JREComposite_button_refresh;

  public static String JREComposite_browseDialog_pickRootFolder;

  public static String JREComposite_browseDialog_noNewVmsFound;

  public static String JREComposite_browseDialog_oneNewVmFound;

  public static String JREComposite_browseDialog_newVmsFound;

  public static String JREComposite_bit;

  public static String JREController_noJreFound;

  public static String JREController_chooseJvm_tooltip;

  public static String JREDialog_title;

  public static String JREDialog_selectJvm;

  public static String JREDialog_selectJvm_withFilter;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
