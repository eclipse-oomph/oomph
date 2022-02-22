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

import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Add desktop support according to the <a href="https://specifications.freedesktop.org/desktop-entry-spec/latest/">free desktop specification</a>.
 *
 * @author Christoph Laeubrich
 */
public class FreeDesktopSupport implements DesktopSupport
{
  private static final String IM_MODULE_XIM = "xim"; //$NON-NLS-1$

  private static final String DESKTOP_FILE_EXTENSION = ".desktop"; //$NON-NLS-1$

  private File userDir;

  public FreeDesktopSupport()
  {
    userDir = new File(System.getProperty("user.home")); //$NON-NLS-1$
    if (!userDir.isDirectory())
    {
      throw new IllegalStateException(NLS.bind(Messages.FreeDesktopSupport_NoUserHome, userDir));
    }
  }

  @Override
  public void pinToTaskBar(String location, String launcherName) throws IOException
  {
    // Not supported yet
  }

  @Override
  @SuppressWarnings("nls")
  public boolean createShortCut(ShortcutType type, String groupName, File executable, String shortcutName, String description, String id) throws IOException
  {
    StringBuilder desktopFile = new StringBuilder();
    desktopFile.append("[Desktop Entry]\n");
    desktopFile.append("Type=Application\n");
    desktopFile.append("Terminal=false\n");
    desktopFile.append("Encoding=UTF-8\n");
    desktopFile.append("Version=1.1\n");
    desktopFile.append("Name=").append(shortcutName).append("\n");
    desktopFile.append("Exec=");

    String module = System.getenv("GTK_IM_MODULE");
    if (IM_MODULE_XIM.equals(module))
    {
      try // to find a better choice see Bug 517671 xim produces flickering
      {
        Process process = Runtime.getRuntime().exec("im-config -l");
        for (String s : IOUtil.readUTF8(process.getInputStream()).trim().split("\\s+"))
        {
          if (!IM_MODULE_XIM.equals(s) && !StringUtil.isEmpty(s.trim()))
          {
            module = s;
            break;
          }
        }
      }
      catch (Exception e)
      {
        // no luck then...
      }
    }
    if (module != null && !StringUtil.isEmpty(module.trim()))
    {
      desktopFile.append("env GTK_IM_MODULE=");
      desktopFile.append(module);
      desktopFile.append(' ');
    }
    desktopFile.append(executable).append("\n");

    if (groupName != null)
    {
      desktopFile.append("Categories=").append(groupName).append("\n");
    }
    else
    {
      desktopFile.append("Categories=Development;IDE;\n");
    }

    File icon = new File(executable.getParentFile(), "icon.xpm");
    if (icon.isFile())
    {
      desktopFile.append("Icon=").append(icon.getAbsolutePath()).append("\n");
    }

    if (type == ShortcutType.START_MENU)
    {
      return writeStartMenuDesktopFile(id, desktopFile);
    }
    else if (type == ShortcutType.DESKTOP)
    {
      return writeDesktopShortCutFile(desktopFile, shortcutName);
    }
    else
    {
      return false;
    }
  }

  @SuppressWarnings("nls")
  private boolean writeDesktopShortCutFile(StringBuilder desktopFile, String shortcutName) throws IOException
  {
    File userDirsFile = new File(userDir, ".config/user-dirs.dirs");
    List<String> readLines = IOUtil.readLines(userDirsFile, "UTF-8");
    for (String line : readLines)
    {
      if (line.startsWith("XDG_DESKTOP_DIR="))
      {
        String desktopPath = line.split("=", 2)[1].trim();
        if (desktopPath.startsWith("\""))
        {
          desktopPath = desktopPath.substring(1);
        }

        if (desktopPath.endsWith("\""))
        {
          desktopPath = desktopPath.substring(0, desktopPath.length() - 1);
        }

        File outputFile = new File(desktopPath.replace("$HOME", userDir.getAbsolutePath()), shortcutName + DESKTOP_FILE_EXTENSION);
        IOUtil.writeUTF8(outputFile, "#!/usr/bin/env xdg-open\n" + desktopFile.toString());
        Runtime.getRuntime().exec(new String[] { "chmod", "+x", outputFile.getAbsolutePath() });
        return true;
      }
    }
    return false;
  }

  private boolean writeStartMenuDesktopFile(String id, StringBuilder desktopFile) throws IOException
  {
    File userApplicationsDir = new File(userDir, ".local/share/applications"); //$NON-NLS-1$
    if (!userApplicationsDir.mkdirs() && !userApplicationsDir.isDirectory())
    {
      throw new IOException(NLS.bind(Messages.FreeDesktopSupport_CannotCreateDirectory, userApplicationsDir.getAbsolutePath()));
    }

    String cleanId = id.replaceAll("[^.A-Za-z0-9-_]", "_").replaceAll("\\.+", "."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    File outputFile = new File(userApplicationsDir, cleanId + DESKTOP_FILE_EXTENSION);
    if (outputFile.exists())
    {
      if (!outputFile.renameTo(new File(userApplicationsDir, outputFile.getName() + "." + System.currentTimeMillis() + ".backup"))) //$NON-NLS-1$ //$NON-NLS-2$
      {
        return false;
      }
    }
    IOUtil.writeUTF8(outputFile, desktopFile.toString());
    try
    {
      Runtime.getRuntime().exec(new String[] { "update-desktop-database", userApplicationsDir.getAbsolutePath() }); //$NON-NLS-1$
      return true;
    }
    catch (Exception e)
    {
      // Can't update the userdatabase; the command method might not be available/installed.
      return false;
    }
  }

}
