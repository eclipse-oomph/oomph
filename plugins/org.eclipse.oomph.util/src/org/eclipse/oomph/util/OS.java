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
package org.eclipse.oomph.util;

import org.eclipse.oomph.internal.util.UtilPlugin;

import org.eclipse.emf.common.CommonPlugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.util.NLS;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public abstract class OS
{
  public static final OS INSTANCE = create();

  public static final List<OS> INSTANCES = createAll();

  private final String osgiOS;

  private final String osgiWS;

  private final String osgiArch;

  protected OS(String osgiOS, String osgiWS, String osgiArch)
  {
    this.osgiOS = osgiOS;
    this.osgiWS = osgiWS;
    this.osgiArch = osgiArch;
  }

  public String getOsgiOS()
  {
    return osgiOS;
  }

  public String getOsgiWS()
  {
    return osgiWS;
  }

  public String getOsgiArch()
  {
    return osgiArch;
  }

  public boolean isWin()
  {
    return false;
  }

  public boolean isMac()
  {
    return false;
  }

  public boolean isLinux()
  {
    return false;
  }

  public boolean isCurrent()
  {
    return Platform.getOS().equals(osgiOS) && Platform.getWS().equals(osgiWS) && Platform.getOSArch().equals(osgiArch);
  }

  public boolean isCurrentOS()
  {
    return Platform.getOS().equals(osgiOS);
  }

  public boolean is32BitAvailable()
  {
    return true;
  }

  public int getBitness()
  {
    String osgiArch = getOsgiArch();
    if (Platform.ARCH_X86.equals(osgiArch))
    {
      return 32;
    }

    return 64;
  }

  public boolean isLineEndingConversionNeeded()
  {
    return false;
  }

  protected String getEncoding()
  {
    return "ISO-8859-1"; //$NON-NLS-1$
  }

  protected abstract String[] getOpenCommands();

  public boolean openSystemBrowser(String url)
  {
    try
    {
      for (String command : getOpenCommands())
      {
        if (openSystemBrowser(command, url))
        {
          return true;
        }
      }
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    try
    {
      // java.awt.Desktop was introduced with Java 1.6!
      Class<?> desktopClass = CommonPlugin.loadClass(UtilPlugin.INSTANCE.getSymbolicName(), "java.awt.Desktop"); //$NON-NLS-1$
      Method getDesktopMethod = ReflectUtil.getMethod(desktopClass, "getDesktop"); //$NON-NLS-1$
      Method browseMethod = ReflectUtil.getMethod(desktopClass, "browse", URI.class); //$NON-NLS-1$

      Object desktop = getDesktopMethod.invoke(null);
      browseMethod.invoke(desktop, new URI(url));
      return true;
    }
    catch (Throwable ex)
    {
      UtilPlugin.INSTANCE.log(ex, IStatus.WARNING);
    }

    return false;
  }

  private boolean openSystemBrowser(String command, String url)
  {
    if (IOUtil.getFromPath(command) != null)
    {
      String[] cmdarray = { command, url };

      try
      {
        Process process = Runtime.getRuntime().exec(cmdarray);
        if (process != null)
        {
          // Don't check whether the process is still running; some commands just delegate to others and terminate.
          return true;
        }
      }
      catch (IOException ex)
      {
        //$FALL-THROUGH$
      }
    }

    return false;
  }

  public Process execute(List<String> command, boolean terminal) throws Exception
  {
    ProcessBuilder processBuilder = new ProcessBuilder(command);
    try
    {
      Map<String, String> environment = processBuilder.environment();
      environment.remove("SWT_GTK3"); //$NON-NLS-1$
    }
    catch (UnsupportedOperationException ex)
    {
      // Ignore.
    }

    Process process = processBuilder.start();
    process.getInputStream().close();
    process.getOutputStream().close();
    Thread.sleep(1000);
    process.getErrorStream().close();
    return process;
  }

  protected String getCommandLine(List<String> command)
  {
    StringBuilder builder = new StringBuilder();
    for (String token : command)
    {
      if (builder.length() != 0)
      {
        builder.append(' ');
      }

      builder.append('"');
      builder.append(token);
      builder.append('"');
    }

    return builder.toString();
  }

  public String getRelativeProductFolder(String folderName)
  {
    if (StringUtil.isEmpty(folderName))
    {
      return "eclipse"; //$NON-NLS-1$
    }

    return folderName;
  }

  public abstract String getRelativeExecutableFolder();

  public String getExecutableName(String launcherName)
  {
    return launcherName;
  }

  public static String getCurrentLauncher(boolean console)
  {
    try
    {
      String launcher = PropertiesUtil.getProperty("eclipse.launcher"); //$NON-NLS-1$
      if (launcher != null)
      {
        File launcherFile = new File(launcher);
        if (launcherFile.isFile())
        {
          File result = IOUtil.getCanonicalFile(launcherFile);
          if (INSTANCE.isWin())
          {
            // If we don't need a console, but actually ended up here with eclipsec.exe, we don't try to find the product-specifically named executable.
            if (console)
            {
              File parentFolder = result.getParentFile();
              if (parentFolder != null)
              {
                File consoleLauncher = new File(parentFolder, "eclipsec.exe"); //$NON-NLS-1$
                if (consoleLauncher.isFile())
                {
                  return consoleLauncher.getPath();
                }
              }
            }
          }

          return result.getPath();
        }
      }
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    return null;
  }

  public abstract String getGitPrefix();

  public abstract String getJREsRoot();

  public abstract OS getForBitness(int bitness);

  public static void close(Closeable closeable)
  {
    if (closeable != null)
    {
      try
      {
        closeable.close();
      }
      catch (IOException ex)
      {
        throw new RuntimeException(ex);
      }
    }
  }

  private static OS create()
  {
    String os = Platform.getOS();
    String ws = Platform.getWS();
    String arch = Platform.getOSArch();

    if (Platform.OS_WIN32.equals(os))
    {
      if (Platform.ARCH_X86_64.equals(arch))
      {
        return new Win64(ws);
      }

      return new Win32(ws, arch);
    }

    if (Platform.OS_MACOSX.equals(os))
    {
      return new Mac(ws, arch);
    }

    if (Platform.OS_LINUX.equals(os))
    {
      return new Linux(ws, arch);
    }

    throw new IllegalStateException(NLS.bind(Messages.OS_OS_NotSupported_exception, os));
  }

  private static List<OS> createAll()
  {
    List<OS> result = new ArrayList<>();

    result.add(Win64.INSTANCE);
    result.add(Win32.INSTANCE);
    result.add(new Mac(Platform.WS_COCOA, Platform.ARCH_X86_64));
    result.add(new Mac(Platform.WS_COCOA, Platform.ARCH_AARCH64));
    result.add(new Linux(Platform.WS_GTK, Platform.ARCH_X86_64));
    result.add(new Linux(Platform.WS_GTK, Platform.ARCH_AARCH64));

    return Collections.unmodifiableList(result);
  }

  @Override
  public String toString()
  {
    return getOsgiOS() + " " + getOsgiWS() + " " + getOsgiArch(); //$NON-NLS-1$ //$NON-NLS-2$
  }

  /**
   * @author Eike Stepper
   */
  private static class Win32 extends OS
  {
    private static final Win32 INSTANCE = new Win32(Platform.WS_WIN32, Platform.ARCH_X86);

    // Don't use "explorer" as it forks another process and returns a confusing exit value.
    private static final String[] OPEN_COMMANDS = {};

    public Win32(String osgiWS, String osgiArch)
    {
      super(Platform.OS_WIN32, osgiWS, osgiArch);
    }

    @Override
    public boolean isWin()
    {
      return true;
    }

    @Override
    public boolean isLineEndingConversionNeeded()
    {
      return true;
    }

    @Override
    public String getRelativeExecutableFolder()
    {
      return ""; //$NON-NLS-1$
    }

    @Override
    public String getExecutableName(String launcherName)
    {
      return super.getExecutableName(launcherName) + ".exe"; //$NON-NLS-1$
    }

    @Override
    public String getGitPrefix()
    {
      return "C:\\Program Files (x86)\\Git"; //$NON-NLS-1$
    }

    @Override
    public String getJREsRoot()
    {
      return "C:\\Program Files (x86)\\Java"; //$NON-NLS-1$
    }

    @Override
    public OS getForBitness(int bitness)
    {
      if (bitness == 64)
      {
        return Win64.INSTANCE;
      }

      return this;
    }

    @Override
    public Process execute(List<String> command, boolean terminal) throws Exception
    {
      if (terminal)
      {
        List<String> terminalCommand = new ArrayList<>();
        terminalCommand.add("cmd"); //$NON-NLS-1$
        terminalCommand.add("/C"); //$NON-NLS-1$
        terminalCommand.add("start"); //$NON-NLS-1$
        terminalCommand.addAll(command);
        command = terminalCommand;
      }

      return super.execute(command, terminal);
    }

    @Override
    protected String[] getOpenCommands()
    {
      return OPEN_COMMANDS;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static class Win64 extends Win32
  {
    private static final Win64 INSTANCE = new Win64(Platform.WS_WIN32);

    public Win64(String osgiWS)
    {
      super(osgiWS, Platform.ARCH_X86_64);
    }

    @Override
    public String getJREsRoot()
    {
      return "C:\\Program Files\\Java"; //$NON-NLS-1$
    }

    @Override
    public OS getForBitness(int bitness)
    {
      if (bitness == 32)
      {
        return Win32.INSTANCE;
      }

      return this;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class Mac extends OS
  {
    private static final String[] OPEN_COMMANDS = { "open" }; //$NON-NLS-1$

    public Mac(String osgiWS, String osgiArch)
    {
      super(Platform.OS_MACOSX, osgiWS, osgiArch);
    }

    @Override
    public boolean isMac()
    {
      return true;
    }

    @Override
    public String getRelativeProductFolder(String folderName)
    {
      if (StringUtil.isEmpty(folderName))
      {
        folderName = "Eclipse.app"; //$NON-NLS-1$
      }
      else if (!folderName.endsWith(".app")) //$NON-NLS-1$
      {
        folderName += ".app"; //$NON-NLS-1$
      }

      return folderName + "/Contents/Eclipse"; //$NON-NLS-1$
    }

    @Override
    public String getRelativeExecutableFolder()
    {
      return "../MacOS"; //$NON-NLS-1$
    }

    @Override
    public String getGitPrefix()
    {
      return "/"; //$NON-NLS-1$
    }

    @Override
    public String getJREsRoot()
    {
      return "/"; //$NON-NLS-1$
    }

    @Override
    public boolean is32BitAvailable()
    {
      return false;
    }

    @Override
    public OS getForBitness(int bitness)
    {
      return this;
    }

    @Override
    public Process execute(List<String> command, boolean terminal) throws Exception
    {
      if (terminal)
      {
        File commandFile = File.createTempFile("cmd-", ""); //$NON-NLS-1$ //$NON-NLS-2$
        commandFile.deleteOnExit();

        String commandPath = commandFile.toString();
        String commandLine = getCommandLine(command);
        IOUtil.writeUTF8(commandFile, commandLine);

        List<String> chmodCommand = new ArrayList<>();
        chmodCommand.add("chmod"); //$NON-NLS-1$
        chmodCommand.add("a+x"); //$NON-NLS-1$
        chmodCommand.add(commandPath);

        ProcessBuilder chmodProcessBuilder = new ProcessBuilder(chmodCommand);
        Process chmodProcess = chmodProcessBuilder.start();
        chmodProcess.waitFor();

        List<String> terminalCommand = new ArrayList<>();
        terminalCommand.add("open"); //$NON-NLS-1$
        terminalCommand.add("-b"); //$NON-NLS-1$
        terminalCommand.add("com.apple.terminal"); //$NON-NLS-1$
        terminalCommand.add(commandPath);
        command = terminalCommand;
      }

      return super.execute(command, terminal);
    }

    @Override
    protected String[] getOpenCommands()
    {
      return OPEN_COMMANDS;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class Linux extends OS
  {
    private static final String[] OPEN_COMMANDS = { "kde-open", "gnome-open", "xdg-open", "sensible-browser" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

    private static final String[] TERMINAL_COMMANDS = { "gnome-terminal", "xterm" }; //$NON-NLS-1$ //$NON-NLS-2$

    public Linux(String osgiWS, String osgiArch)
    {
      super(Platform.OS_LINUX, osgiWS, osgiArch);
    }

    @Override
    public boolean isLinux()
    {
      return true;
    }

    @Override
    public String getRelativeExecutableFolder()
    {
      return ""; //$NON-NLS-1$
    }

    @Override
    public String getGitPrefix()
    {
      return ""; //$NON-NLS-1$
    }

    @Override
    public String getJREsRoot()
    {
      return ""; //$NON-NLS-1$
    }

    @Override
    public OS getForBitness(int bitness)
    {
      String osgiArch = getOsgiArch();
      if (bitness == 32)
      {
        if (Platform.ARCH_X86_64.equals(osgiArch))
        {
          return createLinux(Platform.ARCH_X86);
        }
      }
      else
      {
        if (Platform.ARCH_X86.equals(osgiArch))
        {
          return createLinux(Platform.ARCH_X86_64);
        }
      }

      return this;
    }

    @Override
    public Process execute(List<String> command, boolean terminal) throws Exception
    {
      if (terminal)
      {
        String commandLine = getCommandLine(command);

        for (String xterm : TERMINAL_COMMANDS)
        {
          try
          {
            List<String> terminalCommand = new ArrayList<>();
            terminalCommand.add(xterm);
            terminalCommand.add("-e"); //$NON-NLS-1$
            terminalCommand.add(commandLine);
            return super.execute(terminalCommand, true);
          }
          catch (Exception ex)
          {
            //$FALL-THROUGH$
          }
        }

        throw new IOException(NLS.bind(Messages.OS_CannotStartTerminal_exception, new Object[] { TERMINAL_COMMANDS }));
      }

      return super.execute(command, false);
    }

    @Override
    protected String[] getOpenCommands()
    {
      return OPEN_COMMANDS;
    }

    private Linux createLinux(String arch)
    {
      return new Linux(getOsgiWS(), arch);
    }
  }
}
