/*
 * Copyright (c) 2014, 2019 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

/*
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */

import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectOutputStream;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public final class P2Indexer implements IApplication
{
  private static final String CHARSET = "UTF-8";

  private final Map<URI, Repository> repositories = new HashMap<URI, Repository>();

  /**
   * The map from repository URL to the list of capabilities in that repository.
   */
  private final Map<String, List<Capability>> capabilities = new HashMap<String, List<Capability>>();

  /**
   * The map from capability namespace to the set of capability names in that namespace.
   */
  private final Map<String, Set<String>> capabilityIndex = new HashMap<String, Set<String>>();

  private final long timeStamp = System.currentTimeMillis();

  private int refreshHours = 24;

  private int maxRepos = Integer.MAX_VALUE;

  private boolean verbose;

  public Object start(IApplicationContext context) throws Exception
  {
    long start = System.currentTimeMillis();
    String[] args = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
    LinkedList<String> arguments = new LinkedList<String>(Arrays.asList(args));

    try
    {
      File scanFolder = new File(arguments.removeFirst());
      refreshHours = Integer.parseInt(arguments.removeFirst());
      URI baseURI = URI.createURI(arguments.removeFirst());
      File outputFolder = new File(arguments.removeFirst());

      while (!arguments.isEmpty())
      {
        String arg = arguments.removeFirst();
        if ("-maxrepos".equals(arg) || "-m".equals(arg))
        {
          maxRepos = Integer.parseInt(arguments.removeFirst());
        }
        else if ("-verbose".equals(arg) || "-v".equals(arg))
        {
          verbose = true;
        }
      }

      scanFolder(scanFolder, baseURI);
      generateIndex(outputFolder);
    }
    finally
    {
      System.out.println("Took " + (System.currentTimeMillis() - start) / 1000 + " seconds.");
    }

    return null;
  }

  public void stop()
  {
  }

  private void scanFolder(File folder, URI uri)
  {
    if (repositories.size() >= maxRepos)
    {
      return;
    }

    File metadataFile = getMetadataFile(folder);
    if (metadataFile != null)
    {
      if (verbose)
      {
        System.out.println("Found " + metadataFile);
      }

      if (metadataFile.getName().startsWith("composite"))
      {
        repositories.put(uri, new Repository.Composite(uri, metadataFile));
      }
      else
      {
        repositories.put(uri, new Repository.Simple(uri, metadataFile));
      }
    }

    File[] children = folder.listFiles(new FileFilter()
    {
      public boolean accept(File file)
      {
        return isValidFolder(file);
      }
    });

    if (children != null)
    {
      Arrays.sort(children);

      for (File child : children)
      {
        String name = child.getName();
        String encodedName = URI.encodeSegment(name, false);
        if (name.equals(URI.decode(encodedName)))
        {
          scanFolder(child, uri.appendSegment(encodedName));
        }
        else
        {
          System.err.println("Can't encode " + child);
        }
      }
    }
  }

  private File getMetadataFile(File folder)
  {
    File p2IndexFile = new File(folder, "p2.index");
    if (p2IndexFile.canRead())
    {
      Map<String, String> properties = PropertiesUtil.loadProperties(p2IndexFile);
      String factoryOrder = properties.get("metadata.repository.factory.order");
      if (factoryOrder != null)
      {
        for (StringTokenizer tokenizer = new StringTokenizer(factoryOrder, ","); tokenizer.hasMoreTokens();)
        {
          String factory = tokenizer.nextToken();
          if ("!".equals(factory))
          {
            break;
          }

          if ("content.xml".equals(factory))
          {
            File file = new File(folder, "content.jar");
            if (file.isFile())
            {
              return file;
            }

            file = new File(folder, "content.xml");
            if (file.isFile())
            {
              return file;
            }
          }

          if ("compositeContent.xml".equals(factory))
          {
            File file = new File(folder, "compositeContent.jar");
            if (file.isFile())
            {
              return file;
            }

            file = new File(folder, "compositeContent.xml");
            if (file.isFile())
            {
              return file;
            }
          }
        }
      }
    }

    File file = new File(folder, "content.jar");
    if (file.isFile())
    {
      return file;
    }

    file = new File(folder, "content.xml");
    if (file.isFile())
    {
      return file;
    }

    file = new File(folder, "compositeContent.jar");
    if (file.isFile())
    {
      return file;
    }

    file = new File(folder, "compositeContent.xml");
    if (file.isFile())
    {
      return file;
    }

    return null;
  }

  private void generateIndex(File outputFolder) throws Exception
  {
    for (Iterator<Repository> it = repositories.values().iterator(); it.hasNext();)
    {
      Repository repository = it.next();
      if (verbose)
      {
        System.out.println("Processing " + repository.getMetadataFile());
      }

      try
      {
        repository.processsMetadata(this, verbose);
      }
      catch (Exception ex)
      {
        it.remove();
        System.err.println("Processing " + repository.getMetadataFile());
        ex.printStackTrace();
      }
    }

    int id = 0;
    for (Repository repository : repositories.values())
    {
      repository.setID(++id);
    }

    outputFolder.mkdirs();
    int count = writeCapabilities(outputFolder);
    writeRepositories(outputFolder);
    writeCapabilityIndex(outputFolder);

    if (verbose)
    {
      System.out.println(repositories.size() + " repositories");
      System.out.println(count + " capabilities");
    }
  }

  private void writeCapabilityIndex(File outputFolder) throws FileNotFoundException, UnsupportedEncodingException, IOException
  {
    OutputStream outputStream = null;

    try
    {
      File capabilitiesFile = new File(outputFolder, "capabilities");
      outputStream = new FileOutputStream(capabilitiesFile);

      Map<Object, Object> options = new HashMap<Object, Object>();
      options.put(BinaryResourceImpl.OPTION_VERSION, BinaryResourceImpl.BinaryIO.Version.VERSION_1_1);
      options.put(BinaryResourceImpl.OPTION_STYLE_DATA_CONVERTER, Boolean.TRUE);
      options.put(BinaryResourceImpl.OPTION_BUFFER_CAPACITY, 8192);

      EObjectOutputStream stream = new BinaryResourceImpl.EObjectOutputStream(outputStream, options);

      stream.writeInt(refreshHours);
      stream.writeCompressedInt(capabilityIndex.size());
      for (Map.Entry<String, Set<String>> entry : capabilityIndex.entrySet())
      {
        Set<String> values = entry.getValue();
        stream.writeSegmentedString(entry.getKey());
        stream.writeCompressedInt(values.size());
        for (String value : values)
        {
          stream.writeSegmentedString(value);
        }
      }

      stream.flush();
    }
    finally
    {
      IOUtil.close(outputStream);
    }
  }

  private void writeRepositories(File outputFolder) throws FileNotFoundException, UnsupportedEncodingException, IOException
  {
    OutputStream outputStream = null;

    try
    {
      File repositoriesFile = new File(outputFolder, "repositories");
      outputStream = new FileOutputStream(repositoriesFile);

      Map<Object, Object> options = new HashMap<Object, Object>();
      options.put(BinaryResourceImpl.OPTION_VERSION, BinaryResourceImpl.BinaryIO.Version.VERSION_1_1);
      options.put(BinaryResourceImpl.OPTION_STYLE_DATA_CONVERTER, Boolean.TRUE);
      options.put(BinaryResourceImpl.OPTION_BUFFER_CAPACITY, 8192);

      EObjectOutputStream stream = new BinaryResourceImpl.EObjectOutputStream(outputStream, options);
      stream.writeLong(timeStamp);
      stream.writeInt(refreshHours);
      stream.writeInt(repositories.size());

      for (Repository repository : repositories.values())
      {
        repository.write(this, stream);
      }

      stream.flush();
    }
    finally
    {
      IOUtil.close(outputStream);
    }
  }

  private int writeCapabilities(File outputFolder)
  {
    int count = 0;
    for (Map.Entry<String, List<Capability>> entry : capabilities.entrySet())
    {
      ++count;
      String name = entry.getKey();
      if (verbose)
      {
        System.out.println("Capability " + name);
      }

      Map<Repository, Set<String>> versions = new HashMap<Repository, Set<String>>();
      for (Capability capability : entry.getValue())
      {
        Repository repository = capability.getRepository();
        if (repositories.containsKey(repository.getURI()))
        {
          Set<String> set = versions.get(repository);
          if (set == null)
          {
            set = new HashSet<String>();
            versions.put(repository, set);
          }

          set.add(capability.getVersion());
        }
      }

      List<String> lines = new ArrayList<String>();
      lines.add(Long.toString(timeStamp));

      for (Map.Entry<Repository, Set<String>> versionEntry : versions.entrySet())
      {
        Repository repository = versionEntry.getKey();
        StringBuilder builder = new StringBuilder();
        builder.append(repository.getID());

        for (String version : versionEntry.getValue())
        {
          builder.append(",");
          builder.append(version);
        }

        lines.add(builder.toString());
      }

      try
      {
        File file = new File(outputFolder, name);
        file.getParentFile().mkdirs();
        IOUtil.writeLines(file, CHARSET, lines);
      }
      catch (Exception ex)
      {
        System.err.println("Capability " + name);
        ex.printStackTrace();
      }
    }

    return count;
  }

  private static Pattern pattern(String pattern)
  {
    pattern = pattern.replaceAll(" ", "\\\\s+");
    pattern = pattern.replaceAll("=", "\\\\s*=\\\\s*");
    pattern = pattern.replaceAll("/>", "\\\\s*/>");
    pattern = pattern.replaceAll("'", " ");
    pattern = pattern.replaceAll("\"", " ");
    pattern = pattern.replaceAll(" ", "['\"]");
    pattern = pattern.replaceAll("#", "([^'\"]+)");
    return Pattern.compile(pattern);
  }

  /**
   * TODO Use {@link IOUtil#isValidFolder(File)}.
   */
  private static boolean isValidFolder(File folder)
  {
    try
    {
      return folder.isDirectory() && folder.getAbsoluteFile().equals(folder.getCanonicalFile());
    }
    catch (IOException ex)
    {
      return false;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class Repository
  {
    private static final String XML_SUFFIX = ".xml";

    private static final String JAR_SUFFIX = ".jar";

    private static final Pattern TIMESTAMP_PATTERN = pattern("<property name='p2.timestamp' value='#'/>");

    private static final long NO_TIMESTAMP = 0;

    protected final List<Composite> composites = new ArrayList<Composite>();

    protected final URI uri;

    protected final File metadataFile;

    protected int id;

    protected long timestamp = NO_TIMESTAMP;

    public Repository(URI uri, File metadataFile)
    {
      this.uri = uri;
      this.metadataFile = metadataFile;
    }

    public File getMetadataFile()
    {
      return metadataFile;
    }

    public URI getURI()
    {
      return uri;
    }

    public int getID()
    {
      return id;
    }

    public void setID(int id)
    {
      this.id = id;
    }

    public abstract boolean isComposed();

    public boolean isCompressed()
    {
      return metadataFile.getName().endsWith(JAR_SUFFIX);
    }

    public int getCapabilityCount()
    {
      return 0;
    }

    public void processsMetadata(P2Indexer indexer, boolean verbose) throws IOException
    {
      JarFile jarFile = null;
      BufferedReader reader = null;

      try
      {
        InputStream inputStream;

        if (isCompressed())
        {
          String name = metadataFile.getName();
          name = name.substring(0, name.length() - JAR_SUFFIX.length()) + XML_SUFFIX;

          jarFile = new JarFile(metadataFile);
          JarEntry jarEntry = jarFile.getJarEntry(name);
          inputStream = jarFile.getInputStream(jarEntry);
        }
        else
        {
          inputStream = new FileInputStream(metadataFile);
        }

        reader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));

        String line;
        while ((line = reader.readLine()) != null)
        {
          if (timestamp == NO_TIMESTAMP)
          {
            Matcher matcher = TIMESTAMP_PATTERN.matcher(line);
            if (matcher.find())
            {
              try
              {
                timestamp = Long.parseLong(matcher.group(1));
              }
              catch (NumberFormatException ex)
              {
                System.err.println("Bad timestamp value '" + matcher.group(1) + "' for:" + metadataFile);
              }
            }
          }

          processsMetadata(indexer, line, verbose);
        }
      }
      finally
      {
        IOUtil.close(reader);

        if (jarFile != null)
        {
          jarFile.close();
        }
      }
    }

    protected abstract void processsMetadata(P2Indexer indexer, String line, boolean verbose);

    public void write(P2Indexer indexer, EObjectOutputStream stream) throws IOException
    {
      stream.writeURI(uri);
      stream.writeBoolean(isComposed());
      stream.writeBoolean(isCompressed());
      stream.writeLong(timestamp);

      if (!isComposed())
      {
        stream.writeInt(getCapabilityCount());
      }

      for (Composite composite : composites)
      {
        stream.writeBoolean(true);
        stream.writeInt(composite.getID());
      }

      stream.writeBoolean(false);
    }

    @Override
    public int hashCode()
    {
      final int prime = 31;
      int result = 1;
      result = prime * result + (uri == null ? 0 : uri.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj)
    {
      if (this == obj)
      {
        return true;
      }

      if (obj == null)
      {
        return false;
      }

      if (getClass() != obj.getClass())
      {
        return false;
      }

      Repository other = (Repository)obj;
      if (uri == null)
      {
        if (other.uri != null)
        {
          return false;
        }
      }
      else if (!uri.equals(other.uri))
      {
        return false;
      }

      return true;
    }

    @Override
    public String toString()
    {
      return uri.toString();
    }

    /**
     * @author Eike Stepper
     */
    private static final class Simple extends Repository
    {
      private static final Pattern CAPABILITY_PATTERN = pattern("<provided namespace='#' name='#' version='#'/>");

      private int capabilityCount;

      public Simple(URI uri, File metadataFile)
      {
        super(uri, metadataFile);
      }

      @Override
      public boolean isComposed()
      {
        return false;
      }

      @Override
      public int getCapabilityCount()
      {
        return capabilityCount;
      }

      @Override
      protected void processsMetadata(P2Indexer indexer, String line, boolean verbose)
      {
        Matcher matcher = CAPABILITY_PATTERN.matcher(line);
        if (matcher.find())
        {
          String namespace = URI.encodeSegment(matcher.group(1), false);
          String name = URI.encodeSegment(matcher.group(2), false);
          String qualifiedName = namespace + "/" + name;

          if (name.equals(".") || name.equals("..") || name.startsWith("file:"))
          {
            if (verbose)
            {
              System.err.println("Skipping " + qualifiedName);
            }

            return;
          }

          CollectionUtil.add(indexer.capabilityIndex, namespace, name);

          String version = matcher.group(3);

          List<Capability> list = indexer.capabilities.get(qualifiedName);
          if (list == null)
          {
            list = new ArrayList<Capability>();
            indexer.capabilities.put(qualifiedName, list);
          }

          list.add(new Capability(this, version));
          ++capabilityCount;
        }
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class Composite extends Repository
    {
      private static final Pattern CHILD_PATTERN = pattern("<child location='#'/>");

      public Composite(URI uri, File metadataFile)
      {
        super(uri, metadataFile);
      }

      @Override
      public boolean isComposed()
      {
        return true;
      }

      @Override
      protected void processsMetadata(P2Indexer indexer, String line, boolean verbose)
      {
        Matcher matcher = CHILD_PATTERN.matcher(line);
        if (matcher.find())
        {
          String child = matcher.group(1);
          if (!StringUtil.isEmpty(child))
          {
            URI childURI = URI.createURI(child);
            if (childURI.hasTrailingPathSeparator())
            {
              childURI = childURI.trimSegments(1);
            }

            if (childURI.isRelative())
            {
              childURI = childURI.resolve(uri.hasTrailingPathSeparator() ? uri : uri.appendSegment(""));
            }

            Repository childRepository = indexer.repositories.get(childURI);
            if (childRepository != null)
            {
              childRepository.composites.add(this);
            }
            else
            {
              if (verbose)
              {
                System.err.println("Child repository of " + getURI() + " not found: " + childURI);
              }
            }
          }
        }
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class Capability
  {
    private final Repository repository;

    private final String version;

    public Capability(Repository repository, String version)
    {
      this.repository = repository;
      this.version = version;
    }

    public Repository getRepository()
    {
      return repository;
    }

    public String getVersion()
    {
      return version;
    }

  }
}
