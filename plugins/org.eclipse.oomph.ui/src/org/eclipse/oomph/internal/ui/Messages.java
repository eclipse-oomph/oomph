/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.internal.ui.messages"; //$NON-NLS-1$

  public static String EarlyStart_valueMustBeOneOf;

  public static String EarlyStart_valueMustBeNonNegative;

  public static String EarlyStart_jobName;

  public static String FindAndReplaceTarget_selectedElements;

  public static String FindAndReplaceTarget_replaceAllCommand;

  public static String FindAndReplaceTarget_labelsAndAttributes;

  public static String FindAndReplaceTarget_labels;

  public static String FindAndReplaceTarget_attributes;

  public static String FindAndReplaceTarget_modifiableAttributes;

  public static String OomphEditingDomainActionBarContributor_findOrReplace;

  public static String OomphEditingDomainActionBarContributor_collapseAll_action;

  public static String OomphEditingDomainActionBarContributor_collapseAll_tooltip;

  public static String OomphPropertySheetPage_copyValue;

  public static String RemoteResourcesPreferencePage_useOfflineCache_label;

  public static String RemoteResourcesPreferencePage_useOfflineCache_tooltip;

  public static String RemoteResourcesPreferencePage_showOfflineToolbar_label;

  public static String RemoteResourcesPreferencePage_showOfflineToolbar_tooltip;

  public static String RemoteResourcesPreferencePage_refreshRemoteCache;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
    // Do not instantiate
  }
}
