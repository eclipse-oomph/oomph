/*
 * Copyright (c) 2018 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Ed Merks
 */
public class SearchRequirementsHandler extends AbstractHandler
{
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    SearchEclipseDialog.Requirements searchEclipseRequirementsDialog = RepositoryExplorer.getSearchEclipseRequirementDialog();
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    if (selection instanceof IStructuredSelection)
    {
      for (Object object : ((IStructuredSelection)selection).toArray())
      {
        Requirement requirement = ObjectUtil.adapt(object, Requirement.class);
        if (requirement != null)
        {
          searchEclipseRequirementsDialog.setInitialFilterString(requirement);
          break;
        }
      }
    }

    return null;
  }
}
