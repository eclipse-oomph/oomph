/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.extractor.lib;

import java.io.UnsupportedEncodingException;

/**
 * @author Eike Stepper
 */
public final class BINMarker
{
  // The marker string must start and end with a space character.
  public static final String MARKER = " THIS IS THE MARKER STRING FOR THE ECLIPSE OOMPH EXTRACTOR 1970-12-03 ";

  public static byte[] getBytes() throws UnsupportedEncodingException
  {
    if (!MARKER.startsWith(" ") || !MARKER.endsWith(" "))
    {
      throw new IllegalStateException("Bad marker format");
    }

    return MARKER.getBytes("UTF-8");
  }
}
