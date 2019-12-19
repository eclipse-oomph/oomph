/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.gitbash.revision;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.swt.widgets.Shell;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class GitRebaseAction extends AbstractRevisionAction
{
  @Override
  protected void run(Shell shell, Repository repository, File workTree, RevObject revision) throws Exception
  {
    String id = revision.getId().name();
    System.out.println(workTree.getAbsolutePath() + " --> git rebase " + id);
    // GitBash.executeCommand(shell, workTree, "git rebase ");
  }
}
