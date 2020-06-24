/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.projectconfig.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.projectconfig.impl.messages"; //$NON-NLS-1$

  public static String ProjectConfigURIHandlerImpl_DeleteNotSupported_exception;

  public static String ProjectConfigURIHandlerImpl_OutputNotSupported_exception;

  public static String ProjectConfigURIHandlerImpl_Unavailable_exception;

  public static String ProjectConfigURIHandlerImpl_WriteNotSupported_exception;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
