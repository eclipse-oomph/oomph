/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.targlets.internal.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.targlets.internal.ui.messages"; //$NON-NLS-1$

  public static String ManifestDiscovery_discoverButton_text;

  public static String ManifestDiscoveryDialog_title_featureDiscovery;

  public static String ManifestDiscoveryDialog_title_packageDiscovery;

  public static String ManifestDiscoveryDialog_title_pluginDiscovery;

  public static String NewTargletContainerWizard_title;

  public static String NewTargletContainerWizardPage_title;

  public static String NewTargletContainerWizardPage_message;

  public static String NewTargletContainerWizardPage_targletContainerId;

  public static String NewTargletContainerWizardPage_error_containerIdNotPresent;

  public static String NewTargletContainerWizardPage_error_containerIdNotUnique;

  public static String TargletContainerDialog_title;

  public static String TargletContainerDialog_defaultMessage;

  public static String TargletContainerUI_status_contentAvailableFromLastWorkingProfile;

  public static String TargletContainerUI_status_foundRepository;

  public static String TargletContainerUI_status_foundRepositories;

  public static String TargletContainerUI_status_composed;

  public static String TargletContainerUI_status_simple;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
