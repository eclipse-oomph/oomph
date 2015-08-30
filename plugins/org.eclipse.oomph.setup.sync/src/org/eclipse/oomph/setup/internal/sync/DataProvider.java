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

  public boolean update(File file) throws IOException, NotFoundException;

  public void post(File file, String baseVersion) throws IOException, ConflictException;

  public boolean delete() throws IOException;

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
  public static class NotFoundException extends IOException
  {
    private static final long serialVersionUID = 1L;

    public NotFoundException(URI uri)
    {
      super("Not found: " + uri);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class ConflictException extends IOException
  {
    private static final long serialVersionUID = 1L;

    public ConflictException(URI uri)
    {
      super("Conflict: " + uri);
    }
  }
}
