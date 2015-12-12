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

import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.sync.SyncPackage;

import org.eclipse.emf.ecore.EClass;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * @author Eike Stepper
 */
public interface DataProvider
{
  public static final File[] NO_FILES = {};

  public Location getLocation();

  public File[] getExtraFiles();

  public boolean retrieve(File file) throws IOException, NotFoundException;

  public void update(File file, File baseFile) throws IOException, NotCurrentException;

  public boolean delete() throws IOException;

  /**
   * @author Eike Stepper
   */
  public enum Location
  {
    LOCAL
    {
      @Override
      public <T> T pick(T local, T remote)
      {
        return local;
      }
    },

    REMOTE
    {
      @Override
      public <T> T pick(T local, T remote)
      {
        return remote;
      }
    };

    public EClass getDataType()
    {
      return pick(SetupPackage.Literals.USER, SyncPackage.Literals.REMOTE_DATA);
    }

    public abstract <T> T pick(T local, T remote);
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
  public static class NotCurrentException extends IOException
  {
    private static final long serialVersionUID = 1L;

    public NotCurrentException(URI uri)
    {
      super("Not current: " + uri);
    }
  }
}
