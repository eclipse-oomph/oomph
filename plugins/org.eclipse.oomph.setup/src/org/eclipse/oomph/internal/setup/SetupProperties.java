/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
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
  public static final String PROP_SETUP_SKIP = "oomph.setup.skip";

  public static final String PROP_SETUP_CONFIRM_SKIP = "oomph.setup.confirm.skip";

  public static final String PROP_SETUP_OFFLINE_STARTUP = "oomph.setup.offline.startup";

  public static final String PROP_SETUP_MIRRORS_STARTUP = "oomph.setup.mirrors.startup";

  public static final String PROP_SETUP_OFFLINE = "oomph.setup.offline";

  public static final String PROP_SETUP_MIRRORS = "eclipse.p2.mirrors";

  public static final String PROP_SETUP_UNSIGNED_POLICY = "eclipse.p2.unsignedPolicy";

  public static final String PROP_SETUP_REMOTE_DEBUG = "oomph.setup.remote.debug";

  public static final String PROP_REDIRECTION_BASE = "oomph.redirection.";

  public static final String PROP_UPDATE_URL = "oomph.update.url";
}
