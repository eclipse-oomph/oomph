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
package org.eclipse.oomph.ostools;

import org.eclipse.oomph.util.OS;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class PromptHandler extends AbstractLocationHandler
{
  public PromptHandler()
  {
  }

  @Override
  protected void execute(File location) throws Exception
  {
    if (OS.INSTANCE.isWin())
    {
      Runtime.getRuntime().exec("cmd /c cd \"" + location + "\" && start cmd.exe");
    }
    else
    {
      Runtime.getRuntime().exec("bash \"" + location + "\"");
    }
  }
}
