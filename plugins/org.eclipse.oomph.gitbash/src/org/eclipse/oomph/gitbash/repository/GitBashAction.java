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

import org.eclipse.oomph.gitbash.GitBash;

import org.eclipse.swt.widgets.Shell;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class GitBashAction extends AbstractRepositoryAction
{
  @Override
  protected void run(Shell shell, File workTree) throws Exception
  {
    String gitBash = GitBash.getExecutable(shell);
    Runtime.getRuntime().exec("cmd /c cd \"" + workTree.getAbsolutePath() + "\" && start cmd.exe /c \"" + gitBash + "\" --login -i");
  }
}
