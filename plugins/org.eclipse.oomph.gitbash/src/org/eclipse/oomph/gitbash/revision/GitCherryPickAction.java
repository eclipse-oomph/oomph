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

import org.eclipse.oomph.gitbash.GitBash;

import org.eclipse.swt.widgets.Shell;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class GitCherryPickAction extends AbstractRevisionAction
{
  @Override
  protected void run(Shell shell, File workTree, String revision) throws Exception
  {
    GitBash.executeCommand(shell, workTree, "git cherry-pick -n " + revision);
  }
}
