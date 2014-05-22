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
  public static final String PROP_SETUP_IDE = "org.eclipse.oomph.setup.ide";

  public static final String PROP_SETUP_SKIP = "org.eclipse.oomph.setup.skip";

  public static final String PROP_SKIP_CONFIRM = "org.eclipse.oomph.setup.skip.confirm";

  public static final String PROP_SETUP_REMOTE_DEBUG = "org.eclipse.oomph.setup.remote.debug";

  public static final String PROP_REDIRECTION_BASE = "setup.redirection.";

  public static final String PROP_RELENG_URL = "releng.url";

  public static final String PROP_OS = "os";

  public static final String PROP_OS_ARCH = "os.arch";

  public static final String PROP_OS_WS = "ws";

  public static final String PROP_ECLIPSE_PRODUCT = "eclipse.product";
}
