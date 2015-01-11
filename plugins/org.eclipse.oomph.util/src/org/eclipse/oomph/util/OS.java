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
package org.eclipse.oomph.util;

import org.eclipse.oomph.internal.util.UtilPlugin;

import org.eclipse.emf.common.CommonPlugin;

import org.eclipse.core.runtime.Platform;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;

/**
 * @author Eike Stepper
 */
public abstract class OS
{
  public static final OS INSTANCE = create();

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

  public boolean is32BitAvailable()
  {
    return true;
  }

  public boolean isLineEndingConversionNeeded()
  {
    return false;
  }

  protected String getEncoding()
  {
    return "ISO-8859-1";
  }

  public boolean openSystemBrowser(String url)
  {
    try
    {
      // java.awt.Desktop was introduced with Java 1.6!
      Class<?> desktopClass = CommonPlugin.loadClass(UtilPlugin.INSTANCE.getSymbolicName(), "java.awt.Desktop");
      Method getDesktopMethod = ReflectUtil.getMethod(desktopClass, "getDesktop");
      Method browseMethod = ReflectUtil.getMethod(desktopClass, "browse", URI.class);

      Object desktop = getDesktopMethod.invoke(null);
      browseMethod.invoke(desktop, new URI(url));
      return true;
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    return false;
  }

  public final String getEclipseDir()
  {
    return "eclipse";
  }

  public abstract String getEclipseExecutable();

  public abstract String getEclipseIni();

  public abstract String getGitPrefix();

  public abstract String getJREsRoot();

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

    throw new IllegalStateException("Operating system not supported: " + os);
  }

  /**
   * @author Eike Stepper
   */
  private static class Win32 extends OS
  {
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
    public String getEclipseExecutable()
    {
      return "eclipse.exe";
    }

    @Override
    public String getEclipseIni()
    {
      return "eclipse.ini";
    }

    @Override
    public String getGitPrefix()
    {
      return "C:\\Program Files (x86)\\Git";
    }

    @Override
    public String getJREsRoot()
    {
      return "C:\\Program Files (x86)\\Java";
    }
  }

  /**
   * @author Eike Stepper
   */
  private static class Win64 extends Win32
  {
    public Win64(String osgiWS)
    {
      super(osgiWS, Platform.ARCH_X86_64);
    }

    @Override
    public String getJREsRoot()
    {
      return "C:\\Program Files\\Java";
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class Mac extends OS
  {
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
    public String getEclipseExecutable()
    {
      return "Eclipse.app/Contents/MacOS/eclipse";
    }

    @Override
    public String getEclipseIni()
    {
      return "Eclipse.app/Contents/MacOS/eclipse.ini";
    }

    @Override
    public String getGitPrefix()
    {
      return "/";
    }

    @Override
    public String getJREsRoot()
    {
      return "/";
    }

    @Override
    public boolean is32BitAvailable()
    {
      return false;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class Linux extends OS
  {
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
    public String getEclipseExecutable()
    {
      return "eclipse";
    }

    @Override
    public String getEclipseIni()
    {
      return "eclipse.ini";
    }

    @Override
    public String getGitPrefix()
    {
      return "";
    }

    @Override
    public String getJREsRoot()
    {
      return "";
    }
  }
}
