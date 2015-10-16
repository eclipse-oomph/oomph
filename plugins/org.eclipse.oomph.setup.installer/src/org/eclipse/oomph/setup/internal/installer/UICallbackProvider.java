/*
 * Copyright (c) 2015 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.equinox.internal.security.storage.friends.IStorageTask;
import org.eclipse.equinox.internal.security.storage.friends.IUICallbacks;
import org.eclipse.equinox.internal.security.storage.friends.InternalExchangeUtils;
import org.eclipse.equinox.internal.security.ui.nls.SecUIMessages;
import org.eclipse.equinox.internal.security.ui.storage.ChallengeResponseDialog;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.equinox.security.storage.provider.IPreferencesContainer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Ed Merks
 */
@SuppressWarnings("restriction")
public class UICallbackProvider implements IUICallbacks
{
  public void setupPasswordRecovery(final int size, final String moduleID, final IPreferencesContainer container)
  {
    UIUtil.syncExec(new Runnable()
    {
      public void run()
      {
        Shell shell = UIUtil.getShell();
        boolean reply = MessageDialog.openQuestion(shell, SecUIMessages.pswdRecoveryOptionTitle, SecUIMessages.pswdRecoveryOptionMsg);
        if (reply)
        {
          ChallengeResponseDialog dialog = new ChallengeResponseDialog(size, shell)
          {
            @Override
            protected void configureShell(Shell shell)
            {
              try
              {
                super.configureShell(shell);
              }
              catch (IllegalStateException ex)
              {
                // This happens because there is no workbench help system.
              }
            }
          };

          dialog.open();
          String[][] result = dialog.getResult();
          if (result != null)
          {
            InternalExchangeUtils.setupRecovery(result, moduleID, container);
          }
        }
      }
    });
  }

  public void execute(final IStorageTask callback) throws StorageException
  {
    callback.execute();
  }

  public Boolean ask(final String msg)
  {
    final Boolean[] result = new Boolean[1];
    UIUtil.getDisplay().syncExec(new Runnable()
    {
      public void run()
      {
        boolean reply = MessageDialog.openConfirm(UIUtil.getShell(), SecUIMessages.generalDialogTitle, msg);
        result[0] = Boolean.valueOf(reply);
      }
    });

    return result[0];
  }

  public boolean runningUI()
  {
    return true;
  }
}
