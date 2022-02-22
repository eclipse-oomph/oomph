/*
 * Copyright (c) 2022 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.p2.core.P2Util;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Ed Merks
 */
public class TrustDialog extends AbstractPreferenceDialog
{
  public static final String TITLE = Messages.TrustDialog_title;

  public static final String DESCRIPTION = Messages.TrustDialog_description;

  public TrustDialog(Shell parentShell)
  {
    super(parentShell, TITLE);
  }

  @SuppressWarnings("restriction")
  @Override
  protected PreferencePage createPreferencePage()
  {
    return new org.eclipse.equinox.internal.p2.ui.sdk.TrustPreferencePage()
    {
      {
        noDefaultAndApplyButton();
      }

      @Override
      public boolean performOk()
      {
        super.performOk();
        P2Util.saveGlobalTrustPreferences(P2Util.getAgentManager().getCurrentAgent().getCurrentProfile(), true);
        return true;
      }
    };
  }

  @Override
  protected String getDefaultMessage()
  {
    return DESCRIPTION + "."; //$NON-NLS-1$
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

}
