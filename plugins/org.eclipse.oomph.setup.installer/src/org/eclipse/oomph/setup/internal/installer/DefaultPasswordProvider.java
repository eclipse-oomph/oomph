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

import org.eclipse.equinox.internal.security.storage.friends.InternalExchangeUtils;
import org.eclipse.equinox.internal.security.ui.nls.SecUIMessages;
import org.eclipse.equinox.internal.security.ui.storage.StorageLoginDialog;
import org.eclipse.equinox.security.storage.provider.IPreferencesContainer;
import org.eclipse.equinox.security.storage.provider.PasswordProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import javax.crypto.spec.PBEKeySpec;

import java.net.URL;

/**
 * @author Ed Merks
 */
@SuppressWarnings("restriction")
public class DefaultPasswordProvider extends PasswordProvider
{
  @Override
  public PBEKeySpec getPassword(IPreferencesContainer container, int passwordType)
  {
    boolean newPassword = (passwordType & CREATE_NEW_PASSWORD) != 0;
    boolean passwordChange = (passwordType & PASSWORD_CHANGE) != 0;

    String location = container.getLocation().getFile();
    URL defaultURL = InternalExchangeUtils.defaultStorageLocation();
    if (defaultURL != null)
    {
      String defaultFile = defaultURL.getFile();
      if (defaultFile != null && defaultFile.equals(location))
      {
        location = null;
      }
    }

    final StorageLoginDialog loginDialog = new StorageLoginDialog(newPassword, passwordChange, location)
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
          // THere is no workbench so hooking up help always throws this exception.
        }
      }
    };

    final PBEKeySpec[] result = new PBEKeySpec[1];
    UIUtil.syncExec(new Runnable()
    {
      public void run()
      {
        if (loginDialog.open() == Window.OK)
        {
          result[0] = loginDialog.getGeneratedPassword();
        }
        else
        {
          result[0] = null;
        }
      }
    });

    return result[0];
  }

  @Override
  public boolean retryOnError(Exception e, IPreferencesContainer container)
  {
    final boolean[] result = new boolean[1];
    UIUtil.getDisplay().syncExec(new Runnable()
    {
      public void run()
      {
        boolean reply = MessageDialog.openConfirm(UIUtil.getShell(), SecUIMessages.exceptionTitle, SecUIMessages.exceptionDecode);
        result[0] = reply;
      }
    });

    return result[0];
  }
}
