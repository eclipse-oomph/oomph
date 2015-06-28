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
package org.eclipse.oomph.gitbash.repository;

import org.eclipse.oomph.gitbash.AbstractAction;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class ListFilesAction extends AbstractAction<Repository>
{
  private static final IWorkbench WORKBENCH = PlatformUI.getWorkbench();

  public ListFilesAction()
  {
    super(Repository.class);
  }

  @Override
  protected void run(final Shell shell, final Repository repository) throws Exception
  {
    IRunnableWithProgress runnable = new IRunnableWithProgress()
    {
      public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
      {
        long start = System.currentTimeMillis();

        try
        {
          final File file = checkHistory(repository, monitor);

          shell.getDisplay().asyncExec(new Runnable()
          {
            public void run()
            {
              try
              {
                IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.toURI());
                IDE.openEditorOnFileStore(WORKBENCH.getActiveWorkbenchWindow().getActivePage(), fileStore);
              }
              catch (PartInitException ex)
              {
                ex.printStackTrace();
              }
            }
          });
        }
        catch (OperationCanceledException ex)
        {
          // Do nothing
        }
        catch (Exception ex)
        {
          throw new InvocationTargetException(ex);
        }
        finally
        {
          System.out.println();
          System.out.println("Took " + (System.currentTimeMillis() - start) + " millis");
        }
      }
    };

    WORKBENCH.getProgressService().run(true, true, runnable);
  }

  private File checkHistory(Repository repository, IProgressMonitor monitor) throws Exception
  {
    Git git = new Git(repository);

    int commitCount = getCommitCount(git);
    monitor.beginTask("Listing all files", commitCount);

    Map<String, Set<String>> namesByExtension = new HashMap<String, Set<String>>();

    for (RevCommit commit : git.log().call())
    {
      TreeWalk walk = new TreeWalk(repository);
      walk.setRecursive(true);
      walk.addTree(commit.getTree());

      while (walk.next())
      {
        checkCancelation(monitor);

        String name = walk.getNameString();
        String extension;

        int lastDot = name.lastIndexOf('.');
        if (lastDot == -1)
        {
          extension = "";
        }
        else
        {
          extension = name.substring(lastDot + 1);
          name = name.substring(0, lastDot);
        }

        Set<String> names = namesByExtension.get(extension);
        if (names == null)
        {
          names = new HashSet<String>();
          namesByExtension.put(extension, names);
        }

        names.add(name);
      }

      try
      {
        walk.close();
      }
      catch (Throwable ex)
      {
        try
        {
          Method method = walk.getClass().getMethod("release");
          method.invoke(walk);
        }
        catch (Throwable ignore)
        {
          //$FALL-THROUGH$
        }
      }

      monitor.worked(1);
    }

    final File file = new File("files-in-" + repository.getWorkTree().getName() + ".txt");
    PrintStream stream = new PrintStream(file);

    try
    {
      for (String extension : sort(namesByExtension.keySet()))
      {
        List<String> names = sort(namesByExtension.get(extension));
        System.out.println(extension + "\t" + names.size());

        stream.println(extension + "\t" + names.size());
        for (String name : names)
        {
          stream.println("\t" + name);
        }
      }
    }
    finally
    {
      stream.close();
    }

    return file;
  }

  private List<String> sort(Collection<String> c)
  {
    List<String> list = new ArrayList<String>(c);
    Collections.sort(list);
    return list;
  }

  private int getCommitCount(Git git) throws GitAPIException, NoHeadException
  {
    int commitCount = 0;
    for (@SuppressWarnings("unused")
    RevCommit commit : git.log().call())
    {
      ++commitCount;
    }

    return commitCount;
  }

  private void checkCancelation(IProgressMonitor monitor)
  {
    if (monitor.isCanceled())
    {
      throw new OperationCanceledException();
    }
  }
}
