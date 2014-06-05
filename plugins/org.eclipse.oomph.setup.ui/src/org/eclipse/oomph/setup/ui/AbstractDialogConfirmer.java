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
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.util.Confirmer;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;

/**
 * @author Eike Stepper
 */
public abstract class AbstractDialogConfirmer implements Confirmer
{
  public Confirmation confirm(final boolean defaultConfirmed, final Object info)
  {
    final boolean[] confirmed = { false };
    final boolean[] remember = { false };

    Display.getDefault().syncExec(new Runnable()
    {
      public void run()
      {
        AbstractConfirmDialog dialog = createDialog(defaultConfirmed, info);
        confirmed[0] = dialog.open() == Dialog.OK;
        remember[0] = dialog.isRemember();
      }
    });

    return new Confirmation(confirmed[0], remember[0]);
  }

  protected abstract AbstractConfirmDialog createDialog(boolean defaultConfirmed, Object info);
}
