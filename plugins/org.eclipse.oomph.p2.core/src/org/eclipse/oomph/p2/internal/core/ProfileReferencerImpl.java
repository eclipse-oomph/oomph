/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.P2Exception;

import org.eclipse.osgi.util.NLS;

import java.io.File;

/**
 * @author Eike Stepper
 */
@Deprecated
public class ProfileReferencerImpl extends PersistentMap<Boolean> implements ProfileReferencer
{
  private final boolean directory;

  public ProfileReferencerImpl(File file, boolean directory)
  {
    super(directory ? null : file);
    this.directory = directory;

    if (file.exists())
    {
      if (directory && !file.isDirectory())
      {
        throw new P2Exception(NLS.bind(Messages.ProfileReferencerImpl_NotDirectory_exception, file));
      }

      if (!directory && file.isDirectory())
      {
        throw new P2Exception(NLS.bind(Messages.ProfileReferencerImpl_NotFile_exception, file));
      }
    }

    load();
  }

  public boolean isDirectory()
  {
    return directory;
  }

  public boolean isReferenced(String profileID)
  {
    if (!getFile().exists())
    {
      return false;
    }

    if (!directory)
    {
      if (getElement(profileID) == null)
      {
        return false;
      }
    }

    return true;
  }

  public void reference(String profileID)
  {
    File file = getFile();
    if (!file.exists())
    {
      if (directory)
      {
        file.mkdirs();
      }
      else
      {
        addElement(profileID, null);
      }
    }
  }

  public void unreference(String profileID)
  {
    if (!directory)
    {
      removeElement(profileID);
    }
  }

  @Override
  protected Boolean createElement(String key, String extraInfo)
  {
    return Boolean.TRUE;
  }
}
