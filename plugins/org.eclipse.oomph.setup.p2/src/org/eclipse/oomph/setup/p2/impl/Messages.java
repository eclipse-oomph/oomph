/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.p2.impl;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.setup.p2.impl.messages"; //$NON-NLS-1$

  public static String P2TaskImpl_offline;

  public static String P2TaskImpl_mirrors;

  public static String P2TaskImpl_profileProperties;

  public static String P2TaskImpl_resolvingRequirement;

  public static String P2TaskImpl_resolvingRequirements;

  public static String P2TaskImpl_fromRepository;

  public static String P2TaskImpl_fromRepositories;

  public static String P2TaskImpl_resolvingRequirements_to;

  public static String P2TaskImpl_requirement;

  public static String P2TaskImpl_repository;

  public static String P2TaskImpl_newSoftwareInstalled;

  public static String P2TaskImpl_noUpdatesAvailable;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
