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
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import java.util.Iterator;

/**
 * @author Eike Stepper
 */
public class ExploreRepositoryHandler extends AbstractHandler
{
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    if (selection instanceof IStructuredSelection)
    {
      for (Iterator<?> it = ((IStructuredSelection)selection).iterator(); it.hasNext();)
      {
        Object element = it.next();
        if (element instanceof Repository)
        {
          Repository repository = (Repository)element;
          String url = repository.getURL();
          if (!StringUtil.isEmpty(url))
          {
            RepositoryExplorer.explore(url);
          }
        }
      }
    }

    return null;
  }
}
