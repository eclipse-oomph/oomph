/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.targlets.internal.ui.wizards;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.targlets.internal.ui.wizards.messages"; //$NON-NLS-1$

  public static String TargetDefinitionExportWizard_title;

  public static String TargetDefinitionExportWizard_exportTargetJob_name;

  public static String TargetDefinitionExportWizardPage_defaultMessage;

  public static String TargetDefinitionExportWizardPage_targetPlatform;

  public static String TargetDefinitionExportWizardPage_exportFolder;

  public static String TargetDefinitionExportWizardPage_browseButton_text;

  public static String TargetDefinitionExportWizardPage_exportFolderDialog_text;

  public static String TargetDefinitionExportWizardPage_exportFolderDialog_message;

  public static String TargetDefinitionExportWizardPage_determineTargetsJob_name;

  public static String TargetDefinitionExportWizardPage_selectTargetToExport;

  public static String TargetDefinitionExportWizardPage_enterFolderToExportTo;

  public static String TargetDefinitionExportWizardPage_active;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
