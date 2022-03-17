/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
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
import org.eclipse.oomph.util.ServiceUtil.MissingServiceException;

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
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.osgi.util.NLS;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

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
import java.util.Optional;
import java.util.Properties;

/**
 * @author Eike Stepper
 */
public abstract class OomphPlugin extends EMFPlugin
{
  protected OomphPlugin(ResourceLocator[] delegateResourceLocators)
  {
    super(delegateResourceLocators);
  }

  public String getStringX(String key, Object... substitutions)
  {
    return getString(key, substitutions);
  }

  public String getStringX(String key, boolean translate, Object... substitutions)
  {
    return getString(key, substitutions, translate);
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

  public final List<File> getClassPath() throws Exception
  {
    Bundle bundle = getBundle();
    return getClassPath(bundle);
  }

  public final IPath getConfigurationLocation() throws IllegalStateException
  {
    Location location = Platform.getConfigurationLocation();
    if (location != null)
    {
      try
      {
        URL dataArea = location.getDataArea(getSymbolicName());
        if (dataArea != null)
        {
          URL fileURL = FileLocator.toFileURL(dataArea);
          if (fileURL != null)
          {
            return new Path(fileURL.getFile());
          }
        }
      }
      catch (IOException ex)
      {
        //$FALL-THROUGH$
      }
    }

    throw new IllegalStateException(Messages.OomphPlugin_ConfigurationUnavailable_exception);
  }

  public final IPath getStateLocation() throws IllegalStateException
  {
    return getEclipsePlugin().getStateLocation();
  }

  public final IPath getUserLocation() throws IllegalStateException
  {
    return new Path(PropertiesUtil.getUserHome()).append(".eclipse").append(getSymbolicName()); //$NON-NLS-1$
  }

  public final Preferences getInstancePreferences()
  {
    return getPreferences("instance"); //$NON-NLS-1$
  }

  public final Preference getInstancePreference(String key)
  {
    return new Preference(getInstancePreferences(), key);
  }

  public final Preferences getConfigurationPreferences()
  {
    return getPreferences("configuration"); //$NON-NLS-1$
  }

  public final Preference getConfigurationPreference(String key)
  {
    return new Preference(getConfigurationPreferences(), key);
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

  public final <T> T getServiceOrNull(Class<T> serviceClass)
  {
    return ServiceUtil.getServiceOrNull(getBundleContext(), serviceClass);
  }

  public final <T> T getService(Class<T> serviceClass) throws MissingServiceException
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
        @Override
        public void removeLogListener(ILogListener listener)
        {
        }

        @Override
        public void log(IStatus status)
        {
          System.out.println(status);
        }

        @Override
        public Bundle getBundle()
        {
          return null;
        }

        @Override
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

  public final void log(Throwable t, int severity)
  {
    log(getStatus(t, severity));
  }

  public final String log(Throwable t)
  {
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
      return getStatus(t, IStatus.ERROR);
    }

    return new Status(IStatus.INFO, getSymbolicName(), obj.toString(), null);
  }

  public final IStatus getStatus(Throwable t, int severity)
  {
    String msg = t.getLocalizedMessage();
    if (msg == null || msg.length() == 0)
    {
      msg = t.getClass().getName();
    }

    return new Status(severity, getSymbolicName(), msg, t);
  }

  public final void coreException(IStatus status) throws CoreException
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

  public final String getBuildID()
  {
    Bundle bundle = getBundle();
    return getBuildID(bundle);
  }

  public final BundleFile getRootFile()
  {
    return new BundleFile.Root(getBundle());
  }

  public final File exportResources(String entry)
  {
    Bundle bundle = getBundle();
    File target = new File(PropertiesUtil.getProperty("java.io.tmpdir"), bundle.getSymbolicName() + "_" + bundle.getVersion()); //$NON-NLS-1$ //$NON-NLS-2$
    if (!target.exists())
    {
      exportResources(entry, target);
    }

    return target;
  }

  public final void exportResources(String entry, File target)
  {
    Bundle bundle = getBundle();
    exportResources(bundle, entry, target);
  }

  private static void exportResources(Bundle bundle, String entry, File target)
  {
    exportResources(bundle, entry.length(), entry, target.getAbsolutePath() + "/"); //$NON-NLS-1$
  }

  private static void exportResources(Bundle bundle, int sourceRootLength, String entry, String targetRoot)
  {
    File file = new File(targetRoot + entry.substring(sourceRootLength));

    if (entry.endsWith("/")) //$NON-NLS-1$
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
      PrintStream printStream = new PrintStream(out, false, "UTF-8"); //$NON-NLS-1$

      print(object, null, printStream, 0, 0);

      printStream.close();
      return new String(out.toByteArray(), "UTF-8"); //$NON-NLS-1$
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
          stream.print("OK"); //$NON-NLS-1$
          break;

        case IStatus.ERROR:
          stream.print("ERROR"); //$NON-NLS-1$
          break;

        case IStatus.WARNING:
          stream.print("WARNING"); //$NON-NLS-1$
          break;

        case IStatus.INFO:
          stream.print("INFO"); //$NON-NLS-1$
          break;

        case IStatus.CANCEL:
          stream.print("CANCEL"); //$NON-NLS-1$
          break;

        default:
          stream.print("severity="); //$NON-NLS-1$
          stream.print(severity);
      }

      stream.print(": "); //$NON-NLS-1$
      stream.print(status.getPlugin());

      stream.print(" code="); //$NON-NLS-1$
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
        stream.print("Caused by: "); //$NON-NLS-1$
      }

      stream.print(t.getClass().getName());

      String msg = t.getLocalizedMessage();
      if (msg != null && msg.length() != 0)
      {
        stream.print(": "); //$NON-NLS-1$
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
        stream.print("at "); //$NON-NLS-1$
        stream.println(stackTrace[i].toString());
      }

      if (more != 0)
      {
        indent(stream, level + 1);
        stream.print("... "); //$NON-NLS-1$
        stream.print(more);
        stream.println(" more"); //$NON-NLS-1$
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
      stream.print("  "); //$NON-NLS-1$
    }
  }

  public static List<File> getClassPath(Bundle bundle) throws Exception
  {
    List<File> cp = new ArrayList<>();
    Optional<File> bundleLocation = FileLocator.getBundleFileLocation(bundle);
    if (bundleLocation.isPresent())
    {
      File file = bundleLocation.get();
      if (file.isFile())
      {
        if (file.getName().endsWith(".jar")) //$NON-NLS-1$
        {
          cp.add(file);
        }
      }
      else if (file.isDirectory())
      {
        File classpathFile = new File(file, ".classpath"); //$NON-NLS-1$
        if (classpathFile.isFile())
        {
          DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
          Element rootElement = XMLUtil.loadRootElement(documentBuilder, classpathFile);
          XMLUtil.handleElementsByTagName(rootElement, "classpathentry", new XMLUtil.ElementHandler() //$NON-NLS-1$
          {
            @Override
            public void handleElement(Element element) throws Exception
            {
              if ("output".equals(element.getAttribute("kind"))) //$NON-NLS-1$ //$NON-NLS-2$
              {
                String path = element.getAttribute("path"); //$NON-NLS-1$
                cp.add(new File(file, path));
              }
            }
          });
        }
        else
        {
          cp.add(file);
        }
      }
    }

    return cp;
  }

  public static String getBuildID(Bundle bundle)
  {
    URL url = bundle.getEntry("about.mappings"); //$NON-NLS-1$
    if (url != null)
    {
      InputStream source = null;

      try
      {
        source = url.openStream();

        Properties properties = new Properties();
        properties.load(source);

        String buildID = (String)properties.get("0"); //$NON-NLS-1$
        if (buildID != null && !buildID.startsWith("$")) //$NON-NLS-1$
        {
          return buildID;
        }
      }
      catch (IOException ex)
      {
        //$FALL-THROUGH$
      }
      finally
      {
        IOUtil.closeSilent(source);
      }
    }

    return null;
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
        path += "/"; //$NON-NLS-1$
      }

      return path + name;
    }

    public List<BundleFile> getChildren()
    {
      if (children == null)
      {
        children = new ArrayList<>();

        Bundle bundle = getBundle();
        String path = "/" + getPath(); //$NON-NLS-1$
        Enumeration<String> paths = bundle.getEntryPaths(path);
        while (paths.hasMoreElements())
        {
          String childPath = paths.nextElement();

          boolean directory = childPath.endsWith("/"); //$NON-NLS-1$

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

    public BundleFile getChild(String path)
    {
      String name;
      String remainder;

      int slash = path.indexOf('/');
      if (slash != -1)
      {
        name = path.substring(0, slash);
        remainder = path.substring(slash + 1);
      }
      else
      {
        name = path;
        remainder = null;
      }

      BundleFile child = null;
      for (BundleFile c : getChildren())
      {
        if (c.getName().equals(name))
        {
          child = c;
          break;
        }
      }

      if (child != null && remainder != null)
      {
        child = child.getChild(remainder);
      }

      return child;
    }

    public BundleFile addChild(String name, boolean directory) throws IOException
    {
      checkDirectory();

      BundleFile child = getChild(name);
      if (child != null)
      {
        throw new IllegalStateException(NLS.bind(Messages.OomphPlugin_FileExists_excpetion, child));
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
        path += "/"; //$NON-NLS-1$
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
        return out.toString("UTF-8"); //$NON-NLS-1$
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
        InputStream in = new ByteArrayInputStream(contents.getBytes("UTF-8")); //$NON-NLS-1$
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

    @Override
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
        throw new IllegalStateException(Messages.OomphPlugin_InvalidFiles_exception);
      }
    }

    private void checkFile()
    {
      if (directory)
      {
        throw new IllegalStateException(Messages.OomphPlugin_InvalidDirectories_exception);
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
        super("", true, null); //$NON-NLS-1$
        if (bundle == null)
        {
          throw new IllegalArgumentException(Messages.OomphPlugin_NullBundle_exception);
        }

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
        return ""; //$NON-NLS-1$
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Preference
  {
    private final Preferences preferences;

    private final String key;

    public Preference(Preferences preferences, String key)
    {
      this.preferences = preferences;
      this.key = key;
    }

    public final Preferences preferences()
    {
      return preferences;
    }

    public final String key()
    {
      return key;
    }

    public void remove()
    {
      preferences.remove(key);
      flush();
    }

    public String get(String def)
    {
      return preferences.get(key, def);
    }

    public void set(String value)
    {
      preferences.put(key, value);
      flush();
    }

    public int get(int def)
    {
      return preferences.getInt(key, def);
    }

    public void set(int value)
    {
      preferences.putInt(key, value);
      flush();
    }

    public long get(long def)
    {
      return preferences.getLong(key, def);
    }

    public void set(long value)
    {
      preferences.putLong(key, value);
      flush();
    }

    public boolean get(boolean def)
    {
      return preferences.getBoolean(key, def);
    }

    public void set(boolean value)
    {
      preferences.putBoolean(key, value);
      flush();
    }

    public float get(float def)
    {
      return preferences.getFloat(key, def);
    }

    public void set(float value)
    {
      preferences.putFloat(key, value);
      flush();
    }

    public double get(double def)
    {
      return preferences.getDouble(key, def);
    }

    public void set(double value)
    {
      preferences.putDouble(key, value);
      flush();
    }

    public byte[] get(byte[] def)
    {
      return preferences.getByteArray(key, def);
    }

    public void set(byte[] value)
    {
      preferences.putByteArray(key, value);
      flush();
    }

    @Override
    public String toString()
    {
      return getClass().getSimpleName() + "[" + key + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    private void flush()
    {
      try
      {
        preferences.flush();
      }
      catch (BackingStoreException ex)
      {
        UtilPlugin.INSTANCE.log(ex);
      }
    }
  }
}
