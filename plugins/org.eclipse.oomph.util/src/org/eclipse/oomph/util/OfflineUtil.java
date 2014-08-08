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
package org.eclipse.oomph.util;


/**
 * @author Eike Stepper
 */
public class OfflineUtil
{
  private static final ReentrantThreadLocal<Boolean> OFFLINE = new ReentrantThreadLocal<Boolean>();

  public OfflineUtil()
  {
  }

  public static boolean isOffline()
  {
    return OFFLINE.get() == Boolean.TRUE;
  }

  public static void begin(boolean offline)
  {
    OFFLINE.begin(offline ? Boolean.TRUE : null);
  }

  public static void end()
  {
    OFFLINE.end();
  }
}
