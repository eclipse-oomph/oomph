/*
 * Copyright (c) 2018 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.presentation.OpenDiscoveredType;
import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;

import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Ed Merks
 */
public class OpenDiscoveredTypeHandler extends AbstractDropdownItemHandler
{
  public OpenDiscoveredTypeHandler()
  {
    super("BrowseType", "Open Discovered Type...");
  }

  public void run()
  {
    try
    {
      IWorkbenchWindow workbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
      if (workbenchWindow != null)
      {
        OpenDiscoveredType.openFor(workbenchWindow);
      }
    }
    catch (Exception ex)
    {
      SetupEditorPlugin.INSTANCE.log(ex);
    }
  }
}
