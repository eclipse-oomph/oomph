/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ostools;

import org.eclipse.oomph.util.OS;

import java.io.File;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public class PromptHandler extends AbstractLocationHandler
{
  private static final String[] LINUX_TERMINALS = { "gnome-terminal", "xterm" };

  public PromptHandler()
  {
  }

  @Override
  protected void execute(File location) throws Exception
  {
    if (OS.INSTANCE.isWin())
    {
      Runtime.getRuntime().exec("cmd /c cd /d \"" + location + "\" && start cmd.exe");
    }
    else if (OS.INSTANCE.isMac())
    {
      ProcessBuilder builder = new ProcessBuilder(new String[] { "/Applications/Utilities/Terminal.app/Contents/MacOS/Terminal", location.toString() });
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

      throw new IOException("Could not start any terminal: " + LINUX_TERMINALS);
    }
  }
}
