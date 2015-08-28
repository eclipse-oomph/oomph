/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.sync;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @author Eike Stepper
 */
public interface DataProvider
{
  public Location getLocation();

  public long get(long timeStamp, File file) throws IOException;

  public void post(long timeStamp, File file) throws IOException, ConflictException;

  /**
   * @author Eike Stepper
   */
  public enum Location
  {
    LOCAL, REMOTE
  }

  /**
   * @author Eike Stepper
   */
  class ConflictException extends IOException
  {
    private static final long serialVersionUID = 1L;

    public ConflictException(URI uri)
    {
      super("Conflict: " + uri);
    }
  }
}
