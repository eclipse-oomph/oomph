/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.targlets.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.targlets.impl.messages"; //$NON-NLS-1$

  public static String TargletTaskImpl_AllPlatforms_message;

  public static String TargletTaskImpl_AllRequirements_message;

  public static String TargletTaskImpl_BinaryEquivalents_message;

  public static String TargletTaskImpl_CollisionError_message;

  public static String TargletTaskImpl_Mirrors_message;

  public static String TargletTaskImpl_NegativeRequirements_message;

  public static String TargletTaskImpl_Offline_message;

  public static String TargletTaskImpl_ProfileProperties_message;

  public static String TargletTaskImpl_Repository_message;

  public static String TargletTaskImpl_Sources_message;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
