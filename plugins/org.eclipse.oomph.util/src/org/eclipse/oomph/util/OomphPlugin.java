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

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.Preferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Eike Stepper
 */
public abstract class OomphPlugin extends EMFPlugin
{
  protected OomphPlugin(ResourceLocator[] delegateResourceLocators)
  {
    super(delegateResourceLocators);
  }

  public final boolean isOSGiRunning()
  {
    return getEclipsePlugin() != null;
  }

  protected Plugin getEclipsePlugin()
  {
    return (Plugin)getPluginResourceLocator();
  }

  public final Bundle getBundle()
  {
    return getEclipsePlugin().getBundle();
  }

  public final BundleContext getBundleContext()
  {
    return getBundle().getBundleContext();
  }

  public final IPath getStateLocation() throws IllegalStateException
  {
    return getEclipsePlugin().getStateLocation();
  }

  public final IPath getUserLocation() throws IllegalStateException
  {
    return new Path(PropertiesUtil.USER_HOME).append(".eclipse").append(getSymbolicName());
  }

  public final Preferences getInstancePreferences()
  {
    return getPreferences("instance");
  }

  public final Preferences getConfigurationPreferences()
  {
    return getPreferences("configuration");
  }

  private Preferences getPreferences(String scope)
  {
    IEclipsePreferences rootNode = Platform.getPreferencesService().getRootNode();
    Preferences instanceScope = rootNode.node(scope);
    return instanceScope.node(getSymbolicName());
  }

  public final boolean isDebugging()
  {
    return getEclipsePlugin().isDebugging();
  }

  public final void setDebugging(boolean value)
  {
    getEclipsePlugin().setDebugging(value);
  }

  public final <T> T getService(Class<T> serviceClass)
  {
    return ServiceUtil.getService(getBundleContext(), serviceClass);
  }

  public final void ungetService(Object service)
  {
    ServiceUtil.ungetService(getBundleContext(), service);
  }

  public final ILog getLog()
  {
    Plugin eclipsePlugin = getEclipsePlugin();
    if (eclipsePlugin == null)
    {
      return new ILog()
      {
        public void removeLogListener(ILogListener listener)
        {
        }

        public void log(IStatus status)
        {
          System.out.println(status);
        }

        public Bundle getBundle()
        {
          return null;
        }

        public void addLogListener(ILogListener listener)
        {
        }
      };
    }

    return eclipsePlugin.getLog();
  }

  public final void log(String message, int severity)
  {
    log(new Status(severity, getSymbolicName(), message));
  }

  public final void log(String message)
  {
    log(message, IStatus.INFO);
  }

  public final void log(IStatus status)
  {
    getLog().log(status);
  }

  public final String log(Throwable t)
  {
    t.printStackTrace();

    IStatus status = getStatus(t);
    log(status);
    return status.getMessage();
  }

  public final IStatus getStatus(Object obj)
  {
    if (obj instanceof CoreException)
    {
      CoreException coreException = (CoreException)obj;
      return coreException.getStatus();
    }

    if (obj instanceof Throwable)
    {
      Throwable t = (Throwable)obj;
      String msg = t.getLocalizedMessage();
      if (msg == null || msg.length() == 0)
      {
        msg = t.getClass().getName();
      }

      return new Status(IStatus.ERROR, getSymbolicName(), msg, t);
    }

    return new Status(IStatus.INFO, getSymbolicName(), obj.toString(), null);
  }

  public void coreException(IStatus status) throws CoreException
  {
    if (status != null)
    {
      int severity = status.getSeverity();
      if (severity == IStatus.CANCEL)
      {
        throw new OperationCanceledException();
      }

      if (severity == IStatus.ERROR)
      {
        throw new CoreException(status);
      }

      if (!status.isOK())
      {
        log(status);
      }
    }
  }

  public final void coreException(Throwable t) throws CoreException
  {
    if (t instanceof CoreException)
    {
      CoreException ex = (CoreException)t;
      IStatus status = ex.getStatus();
      if (status != null && status.getSeverity() == IStatus.CANCEL)
      {
        throw new OperationCanceledException();
      }

      throw ex;
    }

    if (t instanceof OperationCanceledException)
    {
      throw (OperationCanceledException)t;
    }

    if (t instanceof Error)
    {
      throw (Error)t;
    }

    IStatus status = getStatus(t);
    throw new CoreException(status);
  }

  public BundleFile getRootFile()
  {
    return new BundleFile.Root(getBundle());
  }

  public File exportResources(String entry)
  {
    Bundle bundle = getBundle();
    File target = new File(PropertiesUtil.getProperty("java.io.tmpdir"), bundle.getSymbolicName() + "_" + bundle.getVersion());
    if (!target.exists())
    {
      exportResources(entry, target);
    }

    return target;
  }

  public void exportResources(String entry, File target)
  {
    Bundle bundle = getBundle();
    exportResources(bundle, entry, target);
  }

  private static void exportResources(Bundle bundle, String entry, File target)
  {
    exportResources(bundle, entry.length(), entry, target.getAbsolutePath() + "/");
  }

  private static void exportResources(Bundle bundle, int sourceRootLength, String entry, String targetRoot)
  {
    File file = new File(targetRoot + entry.substring(sourceRootLength));

    if (entry.endsWith("/"))
    {
      file.mkdirs();

      String path = entry.substring(0, entry.length() - 1);
      Enumeration<String> entries = bundle.getEntryPaths(path);
      if (entries != null)
      {
        while (entries.hasMoreElements())
        {
          String childEntry = entries.nextElement();
          exportResources(bundle, sourceRootLength, childEntry, targetRoot);
        }
      }
    }
    else
    {
      InputStream source = null;
      OutputStream target = null;

      try
      {
        URL url = bundle.getResource(entry);
        source = url.openStream();
        target = new FileOutputStream(file);

        IOUtil.copy(source, target);
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
      finally
      {
        IOUtil.closeSilent(source);
        IOUtil.closeSilent(target);
      }
    }
  }

  public static void checkCancelation(IProgressMonitor monitor)
  {
    if (monitor != null && monitor.isCanceled())
    {
      throw new OperationCanceledException();
    }
  }

  public static String toString(Throwable t)
  {
    return print(t);
  }

  public static String toString(IStatus status)
  {
    return print(status);
  }

  private static String print(Object object)
  {
    try
    {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      PrintStream printStream = new PrintStream(out, false, "UTF-8");

      print(object, null, printStream, 0, 0);

      printStream.close();
      return new String(out.toByteArray(), "UTF-8");
    }
    catch (UnsupportedEncodingException ex)
    {
      return object.toString();
    }
  }

  private static void print(Object object, StackTraceElement[] extra, PrintStream stream, int level, int more)
  {
    if (object instanceof IStatus)
    {
      IStatus status = (IStatus)object;
      indent(stream, level);

      int severity = status.getSeverity();
      switch (severity)
      {
        case IStatus.OK:
          stream.print("OK");
          break;

        case IStatus.ERROR:
          stream.print("ERROR");
          break;

        case IStatus.WARNING:
          stream.print("WARNING");
          break;

        case IStatus.INFO:
          stream.print("INFO");
          break;

        case IStatus.CANCEL:
          stream.print("CANCEL");
          break;

        default:
          stream.print("severity=");
          stream.print(severity);
      }

      stream.print(": ");
      stream.print(status.getPlugin());

      stream.print(" code=");
      stream.print(status.getCode());

      stream.print(' ');
      stream.println(status.getMessage());

      Throwable t = status.getException();
      if (t != null)
      {
        print(t, null, stream, level, more);
      }
      else if (extra != null)
      {
        print(extra, null, stream, level, more);
      }

      for (IStatus child : status.getChildren())
      {
        print(child, null, stream, level + 1, more);
      }
    }
    else if (object instanceof CoreException)
    {
      CoreException ex = (CoreException)object;

      IStatus status = ex.getStatus();
      if (status.getException() == null)
      {
        extra = ex.getStackTrace();
      }

      print(status, extra, stream, level, more);
    }
    else if (object instanceof Throwable)
    {
      Throwable t = (Throwable)object;

      indent(stream, level);
      if (more != 0)
      {
        stream.print("Caused by: ");
      }

      stream.print(t.getClass().getName());

      String msg = t.getLocalizedMessage();
      if (msg != null && msg.length() != 0)
      {
        stream.print(": ");
        stream.print(msg);
      }

      stream.println();

      print(t.getStackTrace(), null, stream, level, more);

      Throwable cause = t.getCause();
      if (cause != null)
      {
        print(cause, null, stream, level, more + 1);
      }

      if (t instanceof InvocationTargetException)
      {
        Throwable targetException = ((InvocationTargetException)t).getTargetException();
        print(targetException, null, stream, level + 1, more);
      }
    }
    else if (object instanceof StackTraceElement[])
    {
      StackTraceElement[] stackTrace = (StackTraceElement[])object;
      for (int i = 0; i < stackTrace.length - more; i++)
      {
        indent(stream, level + 1);
        stream.print("at ");
        stream.println(stackTrace[i].toString());
      }

      if (more != 0)
      {
        indent(stream, level + 1);
        stream.print("... ");
        stream.print(more);
        stream.println(" more");
      }
    }
    else if (extra != null)
    {
      print(extra, null, stream, level, more);
    }
  }

  private static void indent(PrintStream stream, int level)
  {
    for (int i = 0; i < level; ++i)
    {
      stream.print("  ");
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class BundleFile implements Comparable<BundleFile>
  {
    private static final List<BundleFile> NO_CHILDREN = Collections.emptyList();

    private String name;

    private boolean directory;

    private BundleFile parent;

    private List<BundleFile> children;

    protected BundleFile(String name, boolean directory, BundleFile parent)
    {
      this.name = name;
      this.directory = directory;
      this.parent = parent;
    }

    public Bundle getBundle()
    {
      return parent.getBundle();
    }

    public String getName()
    {
      return name;
    }

    public boolean isDirectory()
    {
      return directory;
    }

    public BundleFile getParent()
    {
      return parent;
    }

    public String getPath()
    {
      String path = parent.getPath();
      if (path.length() != 0)
      {
        path += "/";
      }

      return path + name;
    }

    public List<BundleFile> getChildren()
    {
      if (children == null)
      {
        children = new ArrayList<BundleFile>();

        Bundle bundle = getBundle();
        String path = "/" + getPath();
        Enumeration<String> paths = bundle.getEntryPaths(path);
        while (paths.hasMoreElements())
        {
          String childPath = paths.nextElement();

          boolean directory = childPath.endsWith("/");

          String childName = new Path(childPath).removeTrailingSeparator().lastSegment();
          BundleFile child = new BundleFile(childName, directory, this);
          children.add(child);
        }

        if (children.isEmpty())
        {
          children = NO_CHILDREN;
        }
        else
        {
          Collections.sort(children);
        }
      }

      return children;
    }

    public BundleFile getChild(String name)
    {
      for (BundleFile child : getChildren())
      {
        if (child.getName().equals(name))
        {
          return child;
        }
      }

      return null;
    }

    public BundleFile addChild(String name, boolean directory) throws IOException
    {
      checkDirectory();

      BundleFile child = getChild(name);
      if (child != null)
      {
        throw new IllegalStateException("File already exists: " + child);
      }

      child = new BundleFile(name, directory, this);

      File file = new File(getFile(), name);
      if (directory)
      {
        file.mkdir();
      }
      else
      {
        file.createNewFile();
      }

      List<BundleFile> children = getChildren();
      children.add(child);
      Collections.sort(children);

      return child;
    }

    public void export(File target)
    {
      Bundle bundle = getBundle();
      String path = getPath();
      if (isDirectory())
      {
        path += "/";
      }

      exportResources(bundle, path, target);
    }

    public InputStream getContents()
    {
      checkFile();
      String path = getPath();
      URL url = getBundle().getEntry(path);

      InputStream in = null;

      try
      {
        return url.openStream();
      }
      catch (RuntimeException ex)
      {
        throw ex;
      }
      catch (Exception ex)
      {
        throw new RuntimeException(ex);
      }
      finally
      {
        IOUtil.closeSilent(in);
      }
    }

    public String getContentsString()
    {
      InputStream contents = getContents();
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      IOUtil.copy(contents, out);

      try
      {
        return out.toString("UTF-8");
      }
      catch (UnsupportedEncodingException ex)
      {
        throw new RuntimeException(ex);
      }
    }

    public void setContents(String contents)
    {
      checkFile();
      File file = getFile();

      OutputStream out = null;

      try
      {
        InputStream in = new ByteArrayInputStream(contents.getBytes("UTF-8"));
        out = new FileOutputStream(file);
        IOUtil.copy(in, out);
      }
      catch (RuntimeException ex)
      {
        throw ex;
      }
      catch (Exception ex)
      {
        throw new RuntimeException(ex);
      }
      finally
      {
        IOUtil.closeSilent(out);
      }
    }

    public int compareTo(BundleFile o)
    {
      return name.compareTo(o.getName());
    }

    @Override
    public String toString()
    {
      return getPath();
    }

    private void checkDirectory()
    {
      if (!directory)
      {
        throw new IllegalStateException("Should not be called on files");
      }
    }

    private void checkFile()
    {
      if (directory)
      {
        throw new IllegalStateException("Should not be called on directories");
      }
    }

    private File getFile()
    {
      String path = getPath();
      URL url = getBundle().getEntry(path);

      try
      {
        return new File(FileLocator.toFileURL(url).getFile());
      }
      catch (IOException ex)
      {
        throw new RuntimeException(ex);
      }
    }

    /**
     * @author Eike Stepper
     */
    private static class Root extends BundleFile
    {
      private Bundle bundle;

      public Root(Bundle bundle)
      {
        super("", true, null);
        this.bundle = bundle;
      }

      @Override
      public Bundle getBundle()
      {
        return bundle;
      }

      @Override
      public String getPath()
      {
        return "";
      }
    }
  }
}
