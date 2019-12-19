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
package org.eclipse.oomph.gitbash.repository;

import org.eclipse.oomph.gitbash.AbstractAction;
import org.eclipse.oomph.gitbash.UpdateCopyrightsHelper;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class UpdateRepositoryCopyrightsAction extends AbstractAction<Repository>
{
  public UpdateRepositoryCopyrightsAction()
  {
    super(Repository.class);
  }

  @Override
  protected void run(final Shell shell, final Repository repository) throws Exception
  {
    UpdateCopyrightsHelper helper = new UpdateCopyrightsHelper();
    helper.run(shell, repository, null);
  }
}
