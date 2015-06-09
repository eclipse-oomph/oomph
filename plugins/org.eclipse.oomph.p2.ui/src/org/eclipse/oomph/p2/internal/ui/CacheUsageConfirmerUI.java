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
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.internal.core.CacheUsageConfirmer;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.net.URI;

/**
 * @author Ed Merks
 */
public class CacheUsageConfirmerUI extends CacheUsageConfirmer
{
  private Boolean cacheUsageConfirmed;

  @Override
  public boolean confirmCacheUsage(final URI uri, File file)
  {
    if (cacheUsageConfirmed == null)
    {
      cacheUsageConfirmed = Boolean.FALSE;

      final long lastModified = file.lastModified();
      if (lastModified != 0)
      {
        final Shell shell = getShell();
        if (shell != null)
        {
          UIUtil.getDisplay().syncExec(new Runnable()
          {
            public void run()
            {
              String message;
              long age = System.currentTimeMillis() - lastModified;
              long days = age / (24 * 60 * 60 * 1000);
              if (days > 0)
              {
                message = days + " days";
              }
              else
              {
                long hours = age / (60 * 60 * 1000);
                if (hours > 0)
                {
                  message = hours + " hours";
                }
                else
                {
                  long minutes = age / (60 * 1000);
                  if (minutes > 0)
                  {
                    message = minutes + " minutes";
                  }
                  else
                  {
                    message = "a few seconds ";
                  }
                }
              }

              cacheUsageConfirmed = MessageDialog.openQuestion(shell, "Download failure",
                  "The following URI could not be downloaded:\n\n  " + uri + "\n\nA locally cached version that is " + message
                      + " old is available.  Would you like to use locally cached versions, when available, to continue provisioning?");
            }
          });
        }
      }
    }

    return cacheUsageConfirmed;
  }

  @Override
  public void reset()
  {
    cacheUsageConfirmed = null;
  }

  protected Shell getShell()
  {
    try
    {
      return UIUtil.getShell();
    }
    catch (Throwable ex)
    {
      return null;
    }
  }
}
