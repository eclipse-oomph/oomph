/*
 * Copyright (c) 2020 Christoph Laeubrich and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Christoph Laeubrich - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import java.io.File;
import java.io.IOException;

/**
 * @author Christoph Laeubrich
 */
@SuppressWarnings("nls")
public class WindowsPowerShell implements DesktopSupport
{
  private String executable;

  public WindowsPowerShell(String executable)
  {
    this.executable = executable;
  }

  @Override
  public void pinToTaskBar(String location, String launcherName) throws IOException
  {
    Runtime.getRuntime().exec(new String[] { executable, "-command",
        "& { (new-object -c shell.application).namespace('" + location + "').parsename('" + launcherName + "').invokeverb('taskbarpin') }" });
  }

  @Override
  public boolean createShortCut(ShortcutType type, String groupName, File target, String shortcutName, String description, String id) throws IOException
  {
    String specialFolder;
    switch (type)
    {
      case DESKTOP:
        specialFolder = "Desktop";
        break;
      case START_MENU:
        specialFolder = "Programs";
        break;
      default:
        // unsupported type
        return false;
    }
    if (groupName != null)
    {
      Runtime.getRuntime().exec(new String[] { executable, "-command",
          "& { " + "$folderPath = Join-Path ([Environment]::GetFolderPath('" + specialFolder + "')) '" + groupName + "';" + //
              "[system.io.directory]::CreateDirectory($folderPath); " + //
              "$linkPath = Join-Path $folderPath '" + shortcutName + ".lnk'; $targetPath = '" + target.getAbsolutePath()
              + "'; $link = (New-Object -ComObject WScript.Shell).CreateShortcut( $linkpath ); $link.TargetPath = $targetPath; $link.Save()}" });

    }
    else
    {
      Runtime.getRuntime()
          .exec(new String[] { executable, "-command",
              "& {$linkPath = Join-Path ([Environment]::GetFolderPath('" + specialFolder + "')) '" + shortcutName + ".lnk'; $targetPath = '"
                  + target.getAbsolutePath()
                  + "'; $link = (New-Object -ComObject WScript.Shell).CreateShortcut( $linkpath ); $link.TargetPath = $targetPath; $link.Save()}" });

    }
    return true;
  }
}
