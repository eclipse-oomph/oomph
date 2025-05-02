/*
 * Copyright (c) 2025 Eclipse contributor sand others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.internal.util;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

/**
 * A simple wrapper around {@link HTTPServer#main(String[])}.
 */
public class HTTPServerApplication implements IApplication
{
  @Override
  public Object start(IApplicationContext context) throws Exception
  {
    String[] args = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
    HTTPServer.main(args);
    return null;
  }

  @Override
  public void stop()
  {
  }

}
