/*
 * Copyright (c) 2014-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.setup;

/**
 * @author Eike Stepper
 */
public class SetupProperties
{
  public static final String PROP_DO_NOT_LOAD_RESOURCES_PLUGIN = "org.eclipse.emf.ecore.plugin.EcorePlugin.doNotLoadResourcesPlugin"; //$NON-NLS-1$

  public static final String PROP_REDIRECTION_BASE = "oomph.redirection."; //$NON-NLS-1$

  public static final String PROP_UPDATE_URL = "oomph.update.url"; //$NON-NLS-1$

  public static final String PROP_SETUP_SKIP = "oomph.setup.skip"; //$NON-NLS-1$

  public static final String PROP_SETUP_STATS_SKIP = "oomph.setup.stats.skip"; //$NON-NLS-1$

  public static final String PROP_SETUP_QUESTIONNAIRE_SKIP = "oomph.setup.questionnaire.skip"; //$NON-NLS-1$

  public static final String PROP_SETUP_OFFLINE_STARTUP = "oomph.setup.offline.startup"; //$NON-NLS-1$

  public static final String PROP_SETUP_MIRRORS_STARTUP = "oomph.setup.mirrors.startup"; //$NON-NLS-1$

  public static final String PROP_SETUP_OFFLINE = "oomph.setup.offline"; //$NON-NLS-1$

  public static final String PROP_SETUP_MIRRORS = "eclipse.p2.mirrors"; //$NON-NLS-1$

  /**
   * Automatically accepts unsigned content if set to 'true'.
   */
  public static final String PROP_SETUP_UNSIGNED_POLICY = "eclipse.p2.unsignedPolicy"; //$NON-NLS-1$

  /**
   * Automatically accepts certificates content if set to 'true'.
   */
  public static final String PROP_SETUP_CERTIFICATE_POLICY = "eclipse.p2.certificatePolicy"; //$NON-NLS-1$

  public static final String PROP_SETUP_REMOTE_DEBUG = "oomph.setup.remote.debug"; //$NON-NLS-1$

  public static final String PROP_SETUP_USER_HOME_REDIRECT = "oomph.setup.user.home.redirect"; //$NON-NLS-1$

  public static final String PROP_SETUP_USER_HOME_ORIGINAL = "oomph.setup.user.home.original"; //$NON-NLS-1$

  public static final String PROP_SETUP_SYNC_SKIP = "oomph.setup.sync.skip"; //$NON-NLS-1$

  public static final String PROP_SETUP_SYNC_TIMEOUT = "oomph.setup.sync.timeout"; //$NON-NLS-1$

  public static final String PROP_SETUP_SYNC_CREDENTIAL_PROVIDER_SKIP_DEFAULT = "oomph.setup.sync.credential.provider.skip.default"; //$NON-NLS-1$

  /**
   * The value 'simple' starts the installer in simple mode, 'advanced' in advanced mode.
   * The property is not case sensitive.
   */
  public static final String PROP_SETUP_INSTALLER_MODE = "oomph.setup.installer.mode"; //$NON-NLS-1$

  public static final String PROP_SETUP_INSTALLER_MARKETPLACE = "oomph.setup.installer.marketplace"; //$NON-NLS-1$

  public static final String PROP_SETUP_INSTALLER_PROBLEM_REPORT = "oomph.setup.installer.problem.report"; //$NON-NLS-1$

  public static final String PROP_SETUP_INSTALLER_FORUM = "oomph.setup.installer.forum"; //$NON-NLS-1$

  public static final String PROP_SETUP_INSTALLER_CONTRIBUTE = "oomph.setup.installer.contribute"; //$NON-NLS-1$

  public static final String PROP_SETUP_INSTALLER_URI_SCHEME = "oomph.setup.installer.uri.scheme."; //$NON-NLS-1$

  public static final String PROP_SETUP_PRODUCT_CATALOG_FILTER = "oomph.setup.product.catalog.filter"; //$NON-NLS-1$

  public static final String PROP_SETUP_PRODUCT_FILTER = "oomph.setup.product.filter"; //$NON-NLS-1$

  public static final String PROP_SETUP_PRODUCT_VERSION_FILTER = "oomph.setup.product.version.filter"; //$NON-NLS-1$

  public static final String PROP_SETUP_JRE_CHOICE = "oomph.setup.jre.choice"; //$NON-NLS-1$

  public static final String PROP_SETUP_INSTALL_CHOICE = "oomph.setup.install.choice"; //$NON-NLS-1$

  public static final String PROP_SETUP_ECF_TRACE = "oomph.setup.ecf.trace"; //$NON-NLS-1$

  public static final String PROP_SETUP_ECF_CONNECT_TIMEOUT = "oomph.setup.ecf.connect.timeout"; //$NON-NLS-1$

  public static final String PROP_SETUP_ECF_READ_TIMEOUT = "oomph.setup.ecf.read.timeout"; //$NON-NLS-1$

  public static final String PROP_INSTALLER_UPDATE_URL = "oomph.installer.update.url"; //$NON-NLS-1$

  public static final String PROP_INSTALLER_KEEP = "oomph.installer.keep"; //$NON-NLS-1$

  public static final String PROP_INSTALLER_SWITCH_USER_HOME = "oomph.installer.switch.user.home"; //$NON-NLS-1$

  public static final String PROP_SETUP_USER_AGENT = "oomph.userAgent"; //$NON-NLS-1$

  /**
   * If set, the 'launch automatically' and the 'restart if needed' check boxes are not displayed; instead the property value (true or false) is used directly.
   */
  public static final String PROP_SETUP_LAUNCH_AUTOMATICALLY = "oomph.setup.launch.automatically"; //$NON-NLS-1$

  /**
   * Set to true when the launcher launches the product and there are workspace tasks to perform so that the perform dialog is shown automatically when that product starts performing.
   */
  public static final String PROP_SETUP_SHOW_INITIAL_PROGRESS = "oomph.setup.show.initial.progress"; //$NON-NLS-1$

  /**
   * If defined, the 'Dismiss automatically' check box is set to the value (true or false) as initial value.
   */
  public static final String PROP_SETUP_DISMISS_AUTOMATICALLY = "oomph.setup.dismiss.automatically"; //$NON-NLS-1$

  private SetupProperties()
  {
  }
}
