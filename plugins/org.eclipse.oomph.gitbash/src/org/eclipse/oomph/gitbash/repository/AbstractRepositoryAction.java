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
package org.eclipse.oomph.gitbash.repository;

import org.eclipse.oomph.gitbash.AbstractAction;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.swt.widgets.Shell;

import java.io.File;

/**
 * @author Eike Stepper
 */
public abstract class AbstractRepositoryAction extends AbstractAction<Repository>
{
  public AbstractRepositoryAction()
  {
    super(Repository.class);
  }

  @Override
  protected void run(Shell shell, Repository repository) throws Exception
  {
    run(shell, repository.getWorkTree());
  }

  protected abstract void run(Shell shell, File workTree) throws Exception;
}
