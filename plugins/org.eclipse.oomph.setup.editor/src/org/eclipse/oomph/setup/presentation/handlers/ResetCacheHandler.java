/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.jface.dialogs.MessageDialog;

/**
 * @author Eike Stepper
 */
public class ResetCacheHandler extends AbstractDropdownItemHandler
{
  public ResetCacheHandler()
  {
    super("ResetCache", "Reset Remote Cache");
  }

  public void run()
  {
    try
    {
      int size = ECFURIHandlerImpl.clearExpectedETags();

      String pluralS = size == 1 ? "" : "s";
      String pluralHave = size == 1 ? "has" : "have";
      String message = "The eTag" + pluralS + " of " + size + " cache file" + pluralS + " " + pluralHave + " been reset.";
      MessageDialog.openInformation(UIUtil.getShell(), "Remote Cache", message);
    }
    catch (Exception ex)
    {
      SetupEditorPlugin.INSTANCE.log(ex);
    }
  }
}
