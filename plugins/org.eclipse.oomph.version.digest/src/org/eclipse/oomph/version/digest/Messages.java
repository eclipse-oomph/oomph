/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.version.digest;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.version.digest.messages"; //$NON-NLS-1$

  public static String DigestValidator_ComponentNotFound_message;

  public static String DigestValidator_Feature_message_part;

  public static String DigestValidator_NotInWorkspace_message;

  public static String DigestValidator_NoValidationState_exception;

  public static String DigestValidator_Plugin_message_part;

  public static String DigestValidator_VersionIsNot_message;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
