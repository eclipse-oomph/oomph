/*
 * Copyright (c) 2015 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

import java.io.File;
import java.net.URI;

/**
 * @author Ed Merks
 */
public class CacheUsageConfirmer
{
  public static final String SERVICE_NAME = CacheUsageConfirmer.class.getName();

  public boolean confirmCacheUsage(URI uri, File file)
  {
    return false;
  }

  public void reset()
  {
  }
}
