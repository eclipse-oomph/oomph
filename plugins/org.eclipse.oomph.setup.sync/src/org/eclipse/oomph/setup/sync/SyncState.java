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
package org.eclipse.oomph.setup.sync;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @author Eike Stepper
 */
public interface SyncState
{
  public File getWorkingCopy(boolean forceRefresh) throws IOException;

  public boolean commit() throws IOException, ConflictException;

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
