/*
 * Copyright (c) 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.userstorage.oauth.OAuthParameters;

/**
 * @author Eike Stepper
 */
public final class OAuthConstants extends OAuthParameters
{
  private static final String CLIENT_ID = "4e890e9b113d2470e7a206ef59414cb38a98fbfaaf79760fe8374e96";

  private static final String CLIENT_SECRET = "870a5352c5046346e43d9c19601b74eca332ea62537d80df84d0f28f";

  private static final String CLIENT_KEY = "0c49048fc87fdf2e1207e5edeaa39cc63b233ba346341fb05ad976";

  @Override
  protected String getServiceName()
  {
    return "oomph";
  }

  @Override
  protected String getDefaultClientId()
  {
    return CLIENT_ID;
  }

  @Override
  protected String getDefaultClientSecret()
  {
    return CLIENT_SECRET;
  }

  @Override
  protected String getDefaultClientKey()
  {
    return CLIENT_KEY;
  }

  @Override
  protected String getDefaultExpectedCallback()
  {
    return "http://localhost";
  }

}
