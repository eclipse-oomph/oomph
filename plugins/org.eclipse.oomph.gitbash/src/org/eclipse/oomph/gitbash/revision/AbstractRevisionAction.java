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
package org.eclipse.oomph.gitbash.revision;

import org.eclipse.oomph.gitbash.AbstractAction;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.team.ui.history.IHistoryView;
import org.eclipse.ui.IWorkbenchPart;

import java.io.File;

/**
 * @author Eike Stepper
 */
public abstract class AbstractRevisionAction extends AbstractAction<RevObject>
{
  public AbstractRevisionAction()
  {
    super(RevObject.class);
  }

  @Override
  protected void run(Shell shell, RevObject revision) throws Exception
  {
    Repository repository = getRepository();
    if (repository != null)
    {
      File workTree = repository.getWorkTree();
      String id = revision.getId().name();
      run(shell, workTree, id);
    }
  }

  protected abstract void run(Shell shell, File workTree, String revision) throws Exception;

  @SuppressWarnings({ "restriction", "rawtypes" })
  private Repository getRepository()
  {
    Object input = getInput();
    if (input == null)
    {
      return null;
    }

    if (input instanceof org.eclipse.egit.ui.internal.history.HistoryPageInput)
    {
      return ((org.eclipse.egit.ui.internal.history.HistoryPageInput)input).getRepository();
    }

    if (input instanceof org.eclipse.egit.ui.internal.repository.tree.RepositoryTreeNode)
    {
      return ((org.eclipse.egit.ui.internal.repository.tree.RepositoryTreeNode)input).getRepository();
    }

    if (input instanceof IResource)
    {
      org.eclipse.egit.core.project.RepositoryMapping mapping = org.eclipse.egit.core.project.RepositoryMapping.getMapping((IResource)input);
      if (mapping != null)
      {
        return mapping.getRepository();
      }
    }

    if (input instanceof IAdaptable)
    {
      IResource resource = (IResource)((IAdaptable)input).getAdapter(IResource.class);
      if (resource != null)
      {
        org.eclipse.egit.core.project.RepositoryMapping mapping = org.eclipse.egit.core.project.RepositoryMapping.getMapping(resource);
        if (mapping != null)
        {
          return mapping.getRepository();
        }
      }
    }

    Repository repo = org.eclipse.egit.core.AdapterUtils.adapt(input, Repository.class);
    if (repo != null)
    {
      return repo;
    }

    return null;
  }

  private Object getInput()
  {
    IWorkbenchPart part = getTargetPart();
    if (part instanceof IHistoryView)
    {
      return ((IHistoryView)part).getHistoryPage().getInput();
    }

    return null;
  }
}
