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
package org.eclipse.oomph.version.tests;

import org.eclipse.oomph.util.IOUtil;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import org.osgi.framework.Bundle;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class BundleFile implements Comparable<BundleFile>
{
  private static final List<BundleFile> NO_CHILDREN = Collections.emptyList();

  private String name;

  private boolean directory;

  private BundleFile parent;

  private List<BundleFile> children;

  private BundleFile(String name, boolean directory, BundleFile parent)
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

  public void copyTo(File targetFile) throws IOException
  {
    checkFile();
    String path = getPath();
    URL url = getBundle().getEntry(path);

    InputStream in = null;
    OutputStream out = null;

    try
    {
      in = url.openStream();
      out = new FileOutputStream(targetFile);
      IOUtil.copy(in, out);
    }
    finally
    {
      IOUtil.closeSilent(out);
      IOUtil.closeSilent(in);
    }
  }

  public String getContents()
  {
    checkFile();
    String path = getPath();
    URL url = getBundle().getEntry(path);

    InputStream in = null;

    try
    {
      in = url.openStream();
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      IOUtil.copy(in, out);

      return out.toString("UTF-8");
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

  public static BundleFile createRoot(Bundle bundle)
  {
    return new Root(bundle);
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
