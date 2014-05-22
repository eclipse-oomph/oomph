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

import org.eclipse.swt.widgets.Shell;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class GitRebaseAction extends AbstractRevisionAction
{
  @Override
  protected void run(Shell shell, File workTree, String revision) throws Exception
  {
    System.out.println(workTree.getAbsolutePath() + " --> git rebase " + revision);
    // GitBash.executeCommand(shell, workTree, "git rebase ");
  }
}
