/*
 * Copyright (c) 2014, 2018 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.gitbash.revision;

import org.eclipse.oomph.gitbash.UpdateCopyrightsHelper;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class UpdateCommitCopyrightsAction extends AbstractRevisionAction
{
  public UpdateCommitCopyrightsAction()
  {
  }

  @Override
  protected void run(Shell shell, Repository repository, File workTree, RevObject revision) throws Exception
  {
    if (revision instanceof RevCommit)
    {
      RevCommit commit = (RevCommit)revision;
      RevCommit parent = commit.getParent(0);

      DiffFormatter diffFormatter = new DiffFormatter(System.out);
      List<File> inputFiles = new ArrayList<File>();

      try
      {
        diffFormatter.setRepository(repository);

        List<DiffEntry> entries = diffFormatter.scan(parent, commit);
        if (entries != null)
        {
          for (DiffEntry entry : entries)
          {
            String path = entry.getNewPath();
            if (path != null && path.length() != 0)
            {
              File file = new File(workTree, path);
              if (file.isFile())
              {
                inputFiles.add(file);
              }
            }
          }
        }
      }
      finally
      {
        diffFormatter.close();
      }

      if (!inputFiles.isEmpty())
      {
        UpdateCopyrightsHelper helper = new UpdateCopyrightsHelper();
        helper.run(shell, repository, inputFiles);
      }
    }
  }
}
