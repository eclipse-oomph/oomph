/*
 * Copyright (c) 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ostools;

import org.eclipse.oomph.util.OS;

import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Eike Stepper
 */
public class PromptHandler extends AbstractLocationHandler
{
  private static final String[] LINUX_TERMINALS = { "gnome-terminal", "xterm" }; //$NON-NLS-1$ //$NON-NLS-2$

  public PromptHandler()
  {
  }

  @Override
  protected void execute(File location) throws Exception
  {
    if (OS.INSTANCE.isWin())
    {
      Runtime.getRuntime().exec("cmd /c cd /d \"" + location + "\" && start cmd.exe"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    else if (OS.INSTANCE.isMac())
    {
      ProcessBuilder builder = new ProcessBuilder(new String[] { "/Applications/Utilities/Terminal.app/Contents/MacOS/Terminal", location.toString() }); //$NON-NLS-1$
      builder.start();
    }
    else if (OS.INSTANCE.isLinux())
    {
      for (String terminal : LINUX_TERMINALS)
      {
        try
        {
          ProcessBuilder builder = new ProcessBuilder(terminal);
          builder.directory(location);
          builder.start();
          return;
        }
        catch (Exception ex)
        {
          //$FALL-THROUGH$
        }
      }

      throw new IOException(NLS.bind(Messages.PromptHandler_TerminalFailure_exception, Arrays.asList(LINUX_TERMINALS)));
    }
  }
}
