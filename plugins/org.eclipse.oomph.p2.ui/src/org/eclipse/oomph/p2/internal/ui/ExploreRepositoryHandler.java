/*
 * Copyright (c) 2014, 2015, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.util.ObjectUtil;

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
  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    ISelection selection = HandlerUtil.getCurrentSelection(event);
    if (selection instanceof IStructuredSelection)
    {
      for (Iterator<?> it = ((IStructuredSelection)selection).iterator(); it.hasNext();)
      {
        Object element = it.next();
        Repository repository = ObjectUtil.adapt(element, Repository.class);
        if (repository != null)
        {
          String url = repository.getURL();
          RepositoryExplorer.explore(url);
        }
      }
    }

    return null;
  }
}
