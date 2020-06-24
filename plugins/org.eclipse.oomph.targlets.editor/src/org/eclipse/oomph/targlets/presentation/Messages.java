/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.targlets.presentation;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.targlets.presentation.messages"; //$NON-NLS-1$

  public static String ComponentModelWizard_AlreadyExists_message;

  public static String ComponentModelWizard_ComponentDefinitionExists_message;

  public static String ComponentModelWizard_ComponentID_label;

  public static String ComponentModelWizard_ComponentVersion_label;

  public static String ComponentModelWizard_CreateFile_message;

  public static String ComponentModelWizard_Error_label;

  public static String ComponentModelWizard_FeatureComponent_message;

  public static String ComponentModelWizard_Finish_message;

  public static String ComponentModelWizard_PluginComponent_message;

  public static String ComponentModelWizard_ProjectAlreadyComponent_message;

  public static String ComponentModelWizard_ProjectNotAccessible_message;

  public static String ComponentModelWizard_SelectOrCreate_message;

  public static String ComponentModelWizard_SelectProject_message;

  public static String TargletModelWizard_Whatever_label;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
