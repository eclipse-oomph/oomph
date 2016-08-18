/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.setup;

/**
 * @author Eike Stepper
 */
public interface SetupProperties
{
  public static final String PROP_DO_NOT_LOAD_RESOURCES_PLUGIN = "org.eclipse.emf.ecore.plugin.EcorePlugin.doNotLoadResourcesPlugin";

  public static final String PROP_REDIRECTION_BASE = "oomph.redirection.";

  public static final String PROP_UPDATE_URL = "oomph.update.url";

  public static final String PROP_SETUP_SKIP = "oomph.setup.skip";

  public static final String PROP_SETUP_STATS_SKIP = "oomph.setup.stats.skip";

  public static final String PROP_SETUP_QUESTIONNAIRE_SKIP = "oomph.setup.questionnaire.skip";

  public static final String PROP_SETUP_OFFLINE_STARTUP = "oomph.setup.offline.startup";

  public static final String PROP_SETUP_MIRRORS_STARTUP = "oomph.setup.mirrors.startup";

  public static final String PROP_SETUP_OFFLINE = "oomph.setup.offline";

  public static final String PROP_SETUP_MIRRORS = "eclipse.p2.mirrors";

  public static final String PROP_SETUP_UNSIGNED_POLICY = "eclipse.p2.unsignedPolicy";

  public static final String PROP_SETUP_REMOTE_DEBUG = "oomph.setup.remote.debug";

  public static final String PROP_SETUP_USER_HOME_REDIRECT = "oomph.setup.user.home.redirect";

  public static final String PROP_SETUP_SYNC_SKIP = "oomph.setup.sync.skip";

  public static final String PROP_SETUP_SYNC_TIMEOUT = "oomph.setup.sync.timeout";

  public static final String PROP_SETUP_INSTALLER_MODE = "oomph.setup.installer.mode";

  public static final String PROP_SETUP_PRODUCT_CATALOG_FILTER = "oomph.setup.product.catalog.filter";

  public static final String PROP_SETUP_PRODUCT_FILTER = "oomph.setup.product.filter";

  public static final String PROP_SETUP_PRODUCT_VERSION_FILTER = "oomph.setup.product.version.filter";

  public static final String PROP_SETUP_JRE_CHOICE = "oomph.setup.jre.choice";

  public static final String PROP_SETUP_ECF_TRACE = "oomph.setup.ecf.trace";

  public static final String PROP_SETUP_ECF_CONNECT_TIMEOUT = "oomph.setup.ecf.connect.timeout";

  public static final String PROP_SETUP_ECF_READ_TIMEOUT = "oomph.setup.ecf.read.timeout";

  public static final String PROP_INSTALLER_UPDATE_URL = "oomph.installer.update.url";

  public static final String PROP_SETUP_USER_AGENT = "oomph.userAgent";
}
