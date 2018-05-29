/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.version.digest;

import java.util.HashMap;

/**
 * @author Eike Stepper
 */
public class ReleaseDigest extends HashMap<String, byte[]>
{
  private static final long serialVersionUID = 1L;

  private byte[] releaseSpecDigest;

  private transient long timeStamp;

  public ReleaseDigest(byte[] releaseSpecDigest)
  {
    this.releaseSpecDigest = releaseSpecDigest;
  }

  public byte[] getReleaseSpecDigest()
  {
    return releaseSpecDigest;
  }

  public long getTimeStamp()
  {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp)
  {
    this.timeStamp = timeStamp;
  }

  public byte[] getProjectDigest(String projectName)
  {
    return get(projectName);
  }
}
