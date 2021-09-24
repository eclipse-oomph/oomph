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

import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.util.ThreadPool;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectOutputStream;
import org.eclipse.emf.ecore.resource.impl.URIMappingRegistryImpl;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("nls")
public final class P2Indexer implements IApplication
{
  private static final String CHARSET = "UTF-8";

  private final Map<URI, Repository> repositories = new ConcurrentHashMap<URI, Repository>();

  /**
   * The map from repository URL to the list of capabilities in that repository.
   */
  private final Map<String, List<Capability>> capabilities = new HashMap<String, List<Capability>>();

  /**
   * The map from capability namespace to the set of capability names in that namespace.
   */
  private final Map<String, Set<String>> capabilityIndex = new HashMap<String, Set<String>>();

  private final SAXParserFactory parserFactory = SAXParserFactory.newInstance();

  private final Queue<SAXParser> parserPool = new ConcurrentLinkedQueue<SAXParser>();

  private final ThreadPool threadPool = new ThreadPool();

  private final long timeStamp = System.currentTimeMillis();

  private int refreshHours = 24;

  private URI baseURI;

  private int maxRepos = Integer.MAX_VALUE;

  private boolean checkTimeStamps;

  private boolean verbose;

  private Reporter reporter;

  public Object start(IApplicationContext context) throws Exception
  {
    long start = System.currentTimeMillis();
    String[] args = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
    LinkedList<String> arguments = new LinkedList<String>(Arrays.asList(args));

    try
    {
      File scanFolder = new File(arguments.removeFirst()).getCanonicalFile();
      refreshHours = Integer.parseInt(arguments.removeFirst());
      baseURI = URI.createURI(arguments.removeFirst());
      File outputFolder = new File(arguments.removeFirst()).getCanonicalFile();

      while (!arguments.isEmpty())
      {
        String arg = arguments.removeFirst();
        if ("-maxRepos".equalsIgnoreCase(arg) || "-m".equals(arg))
        {
          maxRepos = Integer.parseInt(arguments.removeFirst());
        }
        else if ("-verbose".equalsIgnoreCase(arg) || "-v".equals(arg))
        {
          verbose = true;
        }
        else if ("-checkTimeStamps".equalsIgnoreCase(arg))
        {
          checkTimeStamps = true;
        }
        else if ("-report".equalsIgnoreCase(arg) || "-r".equals(arg))
        {
          File reportFolder = new File(arguments.removeFirst());
          reporter = new Reporter(baseURI.toString(), reportFolder);
        }
      }

      if (baseURI.hasTrailingPathSeparator())
      {
        baseURI = baseURI.trimSegments(1);
      }

      System.out.println();
      System.out.println("Starting analysis: " + new Date(start));
      scanFolder(scanFolder, baseURI);
      threadPool.awaitFinished();

      generateRepositoryMetadata();
      threadPool.awaitFinished();

      long end = System.currentTimeMillis();

      System.out.println("Analysis finished after " + (end - start) / 1000 + " seconds.");
      System.out.println();
      System.out.println("Generating index to " + outputFolder);
      generateIndex(outputFolder);
      System.out.println("Finished after " + (System.currentTimeMillis() - end) / 1000 + " seconds.");

      if (reporter != null)
      {
        System.out.println();
        System.out.println("Writing report to " + reporter.getReportFolder());
        reporter.writeReport(this, start, end - start);
      }
    }
    finally
    {
      System.out.println();
      System.out.println("Overall duration: " + (System.currentTimeMillis() - start) / 1000 + " seconds.");
      System.out.println();

      threadPool.shutdown();
    }

    return null;
  }

  public void stop()
  {
  }

  private void scanFolder(final File folder, final URI uri)
  {
    if (repositories.size() >= maxRepos)
    {
      return;
    }

    threadPool.submit(new Runnable()
    {
      public void run()
      {
        File metadataFile = getMetadataFile(folder);
        if (metadataFile != null)
        {
          if (verbose)
          {
            System.out.println("Found " + metadataFile);
          }

          if (metadataFile.getName().startsWith("composite"))
          {
            repositories.put(uri, new Repository.Composite(P2Indexer.this, uri, metadataFile));
          }
          else
          {
            repositories.put(uri, new Repository.Simple(P2Indexer.this, uri, metadataFile));
          }
        }
      }
    });

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

      for (final File child : children)
      {
        String name = child.getName();
        final String encodedName = URI.encodeSegment(name, false);
        if (name.equals(URI.decode(encodedName)))
        {
          threadPool.submit(new Runnable()
          {
            public void run()
            {
              scanFolder(child, uri.appendSegment(encodedName));
            }
          });
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

  private void generateRepositoryMetadata()
  {
    for (final Repository repository : repositories.values())
    {
      threadPool.submit(new Runnable()
      {
        public void run()
        {
          if (verbose)
          {
            System.out.println("Processing " + repository.getMetadataFile());
          }

          SAXParser parser = null;

          try
          {
            parser = acquireParser();
            repository.processsMetadata(parser);
          }
          catch (FileNotFoundException ex)
          {
            URI uri = repository.getURI();
            repositories.remove(uri);

            if (verbose)
            {
              System.out.println(uri + " --> Metadata file has been deleted");
            }
          }
          catch (Exception ex)
          {
            String message = getStackTrace(ex);
            if (reporter != null)
            {
              reporter.reportError(repository, message);
            }

            System.err.println(repository.getURI() + " --> " + message);
          }
          finally
          {
            if (parser != null)
            {
              releaseParser(parser);
            }
          }
        }
      });
    }
  }

  private SAXParser acquireParser() throws ParserConfigurationException, SAXException
  {
    SAXParser parser = parserPool.poll();
    if (parser == null)
    {
      parser = parserFactory.newSAXParser();
    }

    return parser;
  }

  private void releaseParser(SAXParser parser)
  {
    parserPool.add(parser);
  }

  private void generateIndex(File outputFolder) throws Exception
  {
    int id = 0;
    for (Repository repository : repositories.values())
    {
      repository.setID(++id);
    }

    outputFolder.mkdirs();
    int count = writeCapabilities(outputFolder);
    writeRepositories(outputFolder);
    writeCapabilityIndex(outputFolder);

    System.out.println(repositories.size() + " repositories");
    System.out.println(count + " capabilities");
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

      List<Repository> problematicRepositories = new ArrayList<Repository>();
      for (Repository repository : repositories.values())
      {
        repository.write(stream);

        if (repository.unresolvedChildren > 0)
        {
          problematicRepositories.add(repository);
        }
      }

      stream.writeInt(problematicRepositories.size());
      for (Repository repository : problematicRepositories)
      {
        stream.writeInt(repository.getID());
        stream.writeInt(repository.unresolvedChildren);
      }

      stream.flush();
    }
    finally
    {
      IOUtil.close(outputStream);
    }
  }

  private int writeCapabilities(final File outputFolder) throws InterruptedException
  {
    for (final Map.Entry<String, List<Capability>> entry : capabilities.entrySet())
    {
      threadPool.submit(new Runnable()
      {
        public void run()
        {
          writeCapability(outputFolder, entry.getKey(), entry.getValue());
        }
      });
    }

    threadPool.awaitFinished();
    return capabilities.size();
  }

  private void writeCapability(File outputFolder, String name, List<Capability> capabilities)
  {
    if (verbose)
    {
      System.out.println("Capability " + name);
    }

    Map<Repository, Set<String>> versions = new HashMap<Repository, Set<String>>();
    for (Capability capability : capabilities)
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
      System.err.println("Capability " + name + " --> " + getStackTrace(ex));
    }
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

  private static String getStackTrace(Exception exception)
  {
    try
    {
      ByteArrayOutputStream bytes = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(bytes);
      exception.printStackTrace(out);
      return bytes.toString();
    }
    catch (Exception ex)
    {
      return ex.getMessage();
    }
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class Repository extends DefaultHandler
  {
    private static final String XML_SUFFIX = ".xml";

    private static final String JAR_SUFFIX = ".jar";

    private static final long NO_TIMESTAMP = 0;

    protected final List<Composite> composites = new ArrayList<Composite>();

    protected final P2Indexer indexer;

    protected final URI uri;

    protected final File metadataFile;

    protected final long timestamp;

    protected String elementPath;

    protected int id;

    protected int unresolvedChildren;

    public Repository(P2Indexer indexer, URI uri, File metadataFile)
    {
      this.indexer = indexer;
      this.uri = uri;
      this.metadataFile = metadataFile;
      timestamp = metadataFile.lastModified();

      if (indexer.reporter != null)
      {
        indexer.reporter.reportRepository(this);
      }
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

    public long getTimestamp()
    {
      return timestamp;
    }

    public List<Composite> getComposites()
    {
      return composites;
    }

    public abstract boolean isComposed();

    public boolean isCompressed()
    {
      return metadataFile.getName().endsWith(JAR_SUFFIX);
    }

    public int getIUCount()
    {
      return 0;
    }

    public int getCapabilityCount()
    {
      return 0;
    }

    public void processsMetadata(SAXParser parser) throws IOException, SAXException
    {
      JarFile jarFile = null;
      InputStream inputStream = null;

      try
      {
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

        inputStream = new BufferedInputStream(inputStream);
        parser.parse(inputStream, this);
      }
      finally
      {
        IOUtil.close(inputStream);

        if (jarFile != null)
        {
          jarFile.close();
        }
      }
    }

    protected boolean startElement(String elementPath, Attributes attributes)
    {
      if (indexer.checkTimeStamps && "repository>properties>property".equals(elementPath))
      {
        if (timestamp == NO_TIMESTAMP)
        {
          String name = attributes.getValue("name");
          if ("p2.timestamp".equals(name))
          {
            String value = attributes.getValue("value");
            if (value != null)
            {
              try
              {
                Long.parseLong(value);
              }
              catch (NumberFormatException ex)
              {
                String message = "Bad timestamp value '" + value + "'";
                if (indexer.reporter != null)
                {
                  message = indexer.reporter.reportError(this, message);
                }

                System.err.println(uri + " --> " + message);
              }
            }
            else
            {
              String message = "No timestamp value";
              if (indexer.reporter != null)
              {
                message = indexer.reporter.reportError(this, message);
              }

              System.err.println(uri + " --> " + message);
            }
          }
        }

        return true;
      }

      return false;
    }

    @Override
    public final void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
      if (elementPath == null)
      {
        elementPath = qName;
      }
      else
      {
        elementPath += ">" + qName;
      }

      startElement(elementPath, attributes);
    }

    @Override
    public final void endElement(String uri, String localName, String qName) throws SAXException
    {
      int pos = elementPath.lastIndexOf('>');
      if (pos >= 0)
      {
        elementPath = elementPath.substring(0, pos);
      }
      else
      {
        elementPath = null;
      }
    }

    public void write(EObjectOutputStream stream) throws IOException
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
      private int iuCount;

      private int capabilityCount;

      public Simple(P2Indexer indexer, URI uri, File metadataFile)
      {
        super(indexer, uri, metadataFile);
      }

      @Override
      public boolean isComposed()
      {
        return false;
      }

      @Override
      public int getIUCount()
      {
        return iuCount;
      }

      @Override
      public int getCapabilityCount()
      {
        return capabilityCount;
      }

      @Override
      protected boolean startElement(String elementPath, Attributes attributes)
      {
        if (super.startElement(elementPath, attributes))
        {
          return true;
        }

        if ("repository>units>unit".equals(elementPath))
        {
          ++iuCount;
        }
        else if ("repository>units>unit>provides>provided".equals(elementPath))
        {
          String namespace = URI.encodeSegment(attributes.getValue("namespace"), false);
          String name = URI.encodeSegment(attributes.getValue("name"), false);
          String version = attributes.getValue("version");

          String qualifiedName = namespace + "/" + name;
          if (name.equals(".") || name.equals("..") || name.startsWith("file:"))
          {
            if (indexer.verbose)
            {
              System.err.println("Skipping " + qualifiedName);
            }

            return true;
          }

          synchronized (indexer.capabilityIndex)
          {
            CollectionUtil.add(indexer.capabilityIndex, namespace, name);

            List<Capability> list = indexer.capabilities.get(qualifiedName);
            if (list == null)
            {
              list = new ArrayList<Capability>();
              indexer.capabilities.put(qualifiedName, list);
            }

            list.add(new Capability(this, version));
            ++capabilityCount;
          }

          return true;
        }

        return false;
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class Composite extends Repository
    {
      public Composite(P2Indexer indexer, URI uri, File metadataFile)
      {
        super(indexer, uri, metadataFile);
      }

      @Override
      public boolean isComposed()
      {
        return true;
      }

      @Override
      protected boolean startElement(String elementPath, Attributes attributes)
      {
        if (super.startElement(elementPath, attributes))
        {
          return true;
        }

        if ("repository>children>child".equals(elementPath))
        {
          String child = attributes.getValue("location");
          if (!StringUtil.isEmpty(child))
          {
            URI childURI = URI.createURI(child);
            if (childURI.hasTrailingPathSeparator())
            {
              childURI = childURI.trimSegments(1);
            }

            if (childURI.scheme() != null && !"https".equals(childURI.scheme()))
            {
              String message = "Child repository URI " + childURI + " is absolute and is not secure";
              if (indexer.reporter != null)
              {
                message = indexer.reporter.reportError(this, message);
              }

              System.err.println(uri + " --> " + message);
            }

            if (childURI.isRelative())
            {
              childURI = childURI.resolve(uri.hasTrailingPathSeparator() ? uri : uri.appendSegment(""));

              // If the child is ".." the resolve will return a URI with a trailing separator.
              if (childURI.hasTrailingPathSeparator())
              {
                childURI = childURI.trimSegments(1);
              }
            }
            else if (!indexer.baseURI.scheme().equals(childURI.scheme()) && indexer.baseURI.authority().equals(childURI.authority()))
            {
              childURI = indexer.baseURI.appendSegments(childURI.segments());
            }

            Repository childRepository = indexer.repositories.get(childURI);
            if (childRepository != null)
            {
              childRepository.composites.add(this);
            }
            else if (indexer.baseURI.scheme().equals(childURI.scheme()) && indexer.baseURI.authority().equals(childURI.authority()))
            {
              ++unresolvedChildren;

              String message = "Child repository " + childURI + " not found";
              if (indexer.reporter != null)
              {
                message = indexer.reporter.reportError(this, message);
              }

              System.err.println(uri + " --> " + message);
            }
          }
        }

        return false;
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

  /**
   * @author Eike Stepper
   */
  private static final class Reporter
  {
    private final File reportFolder;

    private final ProjectRegistry projectRegistry;

    private final ProjectMapper projectMapper;

    private final Map<String, Project> projectsByID = new HashMap<String, Project>();

    private final Map<Repository, Project> projectsByRepository = new HashMap<Repository, Project>();

    private final Project unassigned = new Project("_unassigned", "Unassigned Repositories");

    public Reporter(String baseURI, File reportFolder)
    {
      this.reportFolder = reportFolder;
      projectRegistry = new ProjectRegistry();
      projectMapper = new ProjectMapper(baseURI.toString());
    }

    public File getReportFolder()
    {
      return reportFolder;
    }

    public synchronized void reportRepository(Repository repository)
    {
      String projectID = projectMapper.getProjectID(repository.getURI());
      if (projectID != null)
      {
        Project project = projectsByID.get(projectID);
        if (project == null)
        {
          String projectName = projectRegistry.getProjectName(projectID);
          project = new Project(projectID, projectName);
          projectsByID.put(projectID, project);
        }

        projectsByRepository.put(repository, project);
        project.addRepository(repository);
      }
      else
      {
        unassigned.addRepository(repository);
      }
    }

    public synchronized String reportError(Repository repository, String message)
    {
      Project project = projectsByRepository.get(repository);
      if (project != null)
      {
        project.addError(repository, message);
        message += " (" + project.getID() + ")";
      }
      else
      {
        unassigned.addError(repository, message);
      }

      return message;
    }

    public void writeReport(P2Indexer indexer, long start, long duration) throws IOException
    {
      Map<Repository, Set<Repository>> childrenMap = new HashMap<Repository, Set<Repository>>();
      Map<Repository, Pair<Project, Integer>> ids = new HashMap<Repository, Pair<Project, Integer>>();
      int nextID = 0;

      for (Repository repository : indexer.repositories.values())
      {
        Project project = projectsByRepository.get(repository);
        ids.put(repository, Pair.create(project, ++nextID));

        for (Repository.Composite composite : repository.getComposites())
        {
          CollectionUtil.add(childrenMap, composite, repository);
        }
      }

      List<Project> projects = new ArrayList<Project>(projectsByID.values());
      Collections.sort(projects, new Comparator<Project>()
      {
        public int compare(Project o1, Project o2)
        {
          return o1.getLabel().toLowerCase().compareTo(o2.getLabel().toLowerCase());
        }
      });

      int projectRepos = 0;
      int erroneousProjects = 0;
      int erroneousRepos = 0;
      int totalErrors = 0;
      for (Project project : projects)
      {
        projectRepos += project.getTotalRepos();

        int erroneousProjectRepos = project.getErroneousRepos();
        erroneousRepos += erroneousProjectRepos;
        totalErrors += project.getTotalErrors();

        if (erroneousProjectRepos > 0)
        {
          ++erroneousProjects;
        }
      }

      Writer writer = null;

      try
      {
        reportFolder.mkdirs();
        writeCSS();

        writer = new BufferedWriter(new FileWriter(new File(reportFolder, "index.html")));
        writer.write("<html>\n");
        writer.write("<head>\n");
        writer.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"report.css\">\n");
        writer.write("</head>\n");
        writer.write("<body>\n");
        writer.write("<h1>Project Repository Reports</h1>\n");
        writer.write("<p class=\"meta\">Generated " + new Date(start) + ", took " + duration / 1000 + " seconds.</p>\n");

        writer.write("<table border=\"0\">\n");
        writer.write("<tr><td>Projects with repositories:</td><td align=\"right\">" + projects.size()
            + "</td><td rowspan=\"2\">&nbsp;&nbsp;&nbsp;<a class=\"button\" href=\"mailto:stepper@esc-net.de?cc=ed.merks@gmail.com&amp;subject=My%20project%20is%20missing\">Report missing project</a></td></tr>\n");

        if (erroneousProjects > 0)
        {
          writer.write("<tr><td class=\"error\">Projects with erroneous repositories:</td><td class=\"error\" align=\"right\">" + erroneousProjects
              + "</td><td></td></tr>\n");
        }

        if (unassigned.getTotalRepos() > 0)
        {
          writer.write("<tr><td><a href=\"" + unassigned.getFileName() + "\">Unassigned repositories</a>:</td><td align=\"right\">" + unassigned.getTotalRepos()
              + "</td><td></td></tr>\n");
        }

        writer.write("<tr><td>Total repositories:</td><td align=\"right\">" + projectRepos + "</td><td></td></tr>\n");

        if (erroneousRepos > 0)
        {
          writer.write("<tr><td class=\"error\">Erroneous repositories:</td><td class=\"error\" align=\"right\">" + erroneousRepos + "</td><td></td></tr>\n");
        }

        if (totalErrors > 0)
        {
          writer.write("<tr><td class=\"error\">Total errors:</td><td class=\"error\" align=\"right\">" + totalErrors + "</td><td></td></tr>\n");
        }

        writer.write("</table>\n");
        writer.write("<hr>\n");
        writer.write("<ul>\n");
      }
      catch (Exception ex)
      {
        ex.printStackTrace();
      }

      unassigned.writeReport(reportFolder, childrenMap, ids);

      for (Project project : projects)
      {
        if (project.writeReport(reportFolder, childrenMap, ids) && writer != null)
        {
          int errors = project.getErroneousRepos();
          String suffix = errors > 0
              ? "<span class=\"error\">&nbsp;&nbsp;&nbsp;" + errors + "&nbsp;erroneous&nbsp;" + (errors == 1 ? "repository" : "repositories") + "</span>"
              : "";

          writer.write("<li><a href=\"" + project.getID() + ".html\">" + project.getLabel() + "</a>" + suffix + "\n");
        }
      }

      if (writer != null)
      {
        writer.write("</ul>\n");
        writer.write("</body>\n");
        writer.write("</html>\n");
        IOUtil.close(writer);
      }
    }

    private void writeCSS() throws IOException
    {
      Writer writer = null;

      try
      {
        writer = new BufferedWriter(new FileWriter(new File(reportFolder, "report.css")));

        writer.write("@import url('https://fonts.googleapis.com/css?family=Roboto:400,700');\n");
        writer.write("\n");
        writer.write("body {\n");
        writer.write("  font-family: 'Roboto', sans-serif;\n");
        writer.write("}\n");
        writer.write("\n");
        writer.write("h4 {\n");
        writer.write("  margin-left: 24px;\n");
        writer.write("}\n");
        writer.write("\n");
        writer.write(".button {\n");
        writer.write("  border: 1px solid gray;\n");
        writer.write("  border-radius: 3px;\n");
        writer.write("  box-shadow: 1px 1px 1px darkgray;\n");
        writer.write("  padding: 0.25ex 0.25em;\n");
        writer.write("  background-color: lightgray;\n");
        writer.write("  color: black;\n");
        writer.write("  font-weight: bold;\n");
        writer.write("  text-align: center;\n");
        writer.write("  text-decoration: none;\n");
        writer.write("  display: inline-block;\n");
        writer.write("}\n");
        writer.write("\n");
        writer.write(".error {\n");
        writer.write("  color: red;\n");
        writer.write("  font-weight: bold;\n");
        writer.write("}\n");
        writer.write("\n");
        writer.write(".meta {\n");
        writer.write("  color: gray;\n");
        writer.write("  font-size: 0.75em;\n");
        writer.write("  margin-top: -12px;\n");
        writer.write("  margin-bottom: 12px;\n");
        writer.write("}\n");
        writer.write("\n");
        writer.write("a:link {\n");
        writer.write("  text-decoration: none;\n");
        writer.write("}\n");
        writer.write("\n");
        writer.write("a:visited {\n");
        writer.write("  text-decoration: none;\n");
        writer.write("}\n");
        writer.write("\n");
        writer.write("a:hover {\n");
        writer.write("  text-decoration: underline;\n");
        writer.write("}\n");
        writer.write("\n");
        writer.write("a:active {\n");
        writer.write("  text-decoration: underline;\n");
        writer.write("}\n");
      }
      finally
      {
        IOUtil.close(writer);
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class ProjectRegistry
    {
      private static final String PROJECTS_TXT = System.getProperty("projects.txt",
          "/home/data/httpd/download.eclipse.org/oomph/archive/projects/projects.txt");

      private final Map<String, String> names = new HashMap<String, String>();

      public ProjectRegistry()
      {
        File file = new File(PROJECTS_TXT);
        if (file.exists())
        {
          for (String line : IOUtil.readLines(file, "UTF-8"))
          {
            int tab = line.indexOf('\t');
            String id = line.substring(0, tab);
            String name = line.substring(tab + 1);

            names.put(id, name);
          }
        }
        else
        {
          System.out.println(ProjectRegistry.class.getSimpleName() + ": " + PROJECTS_TXT + " not found.");
        }
      }

      public String getProjectName(String id)
      {
        return names.get(id);
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class ProjectMapper
    {
      private static final String MAPPINGS_TXT = System.getProperty("mappings.txt",
          "/home/data/httpd/download.eclipse.org/oomph/archive/projects/mappings.txt");

      private final URIMappingRegistryImpl registry = new URIMappingRegistryImpl();

      private final String baseURI;

      public ProjectMapper(String baseURI)
      {
        this.baseURI = baseURI;

        File file = new File(MAPPINGS_TXT);
        if (file.exists())
        {
          for (String line : IOUtil.readLines(file, "UTF-8"))
          {
            int tab = line.indexOf('\t');
            String path = line.substring(0, tab);
            String project = line.substring(tab + 1);

            registry.put(createURI(path), URI.createURI("project://" + project + "/"));
          }
        }
        else
        {
          System.out.println(ProjectMapper.class.getSimpleName() + ": " + MAPPINGS_TXT + " not found.");
        }
      }

      public String getProjectID(URI uri)
      {
        URI projectURI = registry.getURI(uri);
        if (projectURI != null && "project".equals(projectURI.scheme()))
        {
          return projectURI.authority();
        }

        return null;
      }

      private URI createURI(String path)
      {
        if (!path.endsWith("/"))
        {
          path += "/";
        }

        return URI.createURI(baseURI + "/" + path);
      }
    }

    /**
     * @author Eike Stepper
     */
    private static final class Project
    {
      private final String id;

      private final String name;

      private final Map<Repository, List<String>> repositories = Collections.synchronizedMap(new HashMap<Repository, List<String>>());

      public Project(String id, String name)
      {
        this.id = id;
        this.name = name;
      }

      public String getID()
      {
        return id;
      }

      public String getLabel()
      {
        return name != null && name.length() != 0 ? name : id;
      }

      public String getFileName()
      {
        return id + ".html";
      }

      public int getTotalRepos()
      {
        return repositories.size();
      }

      public int getComposedRepos()
      {
        int result = 0;
        for (Repository repository : repositories.keySet())
        {
          if (repository.isComposed())
          {
            ++result;
          }
        }

        return result;
      }

      public int getErroneousRepos()
      {
        int result = 0;
        for (List<String> errors : repositories.values())
        {
          if (!errors.isEmpty())
          {
            ++result;
          }
        }

        return result;
      }

      public int getTotalErrors()
      {
        int result = 0;
        for (List<String> errors : repositories.values())
        {
          result += errors.size();
        }

        return result;
      }

      public void addRepository(Repository repository)
      {
        repositories.put(repository, new ArrayList<String>());
      }

      public void addError(Repository repository, String message)
      {
        List<String> errors = repositories.get(repository);
        errors.add(message);
      }

      public boolean writeReport(File folder, Map<Repository, Set<Repository>> childrenMap, Map<Repository, Pair<Project, Integer>> ids)
      {
        if (repositories.isEmpty())
        {
          return false;
        }

        BufferedWriter writer = null;

        try
        {
          List<Map.Entry<Repository, List<String>>> entries = new ArrayList<Map.Entry<Repository, List<String>>>(repositories.entrySet());
          Collections.sort(entries, new Comparator<Map.Entry<Repository, List<String>>>()
          {
            public int compare(Map.Entry<Repository, List<String>> o1, Map.Entry<Repository, List<String>> o2)
            {
              return o1.getKey().getURI().toString().compareTo(o2.getKey().getURI().toString());
            }
          });

          int totalRepos = getTotalRepos();
          int compositeRepos = getComposedRepos();
          int simpleRepos = totalRepos - compositeRepos;
          int erroneousRepos = getErroneousRepos();
          int totalErrors = getTotalErrors();

          folder.mkdirs();

          writer = new BufferedWriter(new FileWriter(new File(folder, getFileName())));
          writer.write("<html>\n");
          writer.write("<head>\n");
          writer.write("<link rel=\"stylesheet\" type=\"text/css\" href=\"report.css\">\n");
          writer.write("</head>\n");
          writer.write("<body>\n");
          writer.write("<h1><a href=\"https://projects.eclipse.org/projects/" + id + "\">" + (name != null && name.length() != 0 ? name : id) + "</a></h1>\n");

          writer.write("<table border=\"0\">\n");
          writer.write("<tr><td>Simple repositories:</td><td align=\"right\">" + simpleRepos + "</td><td></td></tr>\n");
          writer.write("<tr><td>Composite repositories:</td><td align=\"right\">" + compositeRepos + "</td><td></td></tr>\n");
          writer.write("<tr><td>Total repositories:</td><td align=\"right\">" + (simpleRepos + compositeRepos) + "</td><td></td></tr>\n");

          if (totalErrors > 0)
          {
            writer.write("<tr><td class=\"error\">Erroneous repositories:</td><td class=\"error\" align=\"right\">" + erroneousRepos + "</td><td rowspan=\"2\">"
                + (erroneousRepos > 0 ? "&nbsp;<a class=\"button\" href=\"#err0\">Goto first error</a>" : "") + "</td></tr>\n");
            writer.write("<tr><td class=\"error\">Total errors:</td><td class=\"error\" align=\"right\">" + totalErrors + "</td><td></td></tr>\n");
          }

          writer.write("</table>\n");
          writer.write("<hr>\n");

          int erroneousRepo = 0;
          for (Map.Entry<Repository, List<String>> entry : entries)
          {
            Repository repository = entry.getKey();
            List<String> errors = entry.getValue();

            Pair<Project, Integer> pair = ids.get(repository);
            Integer idWrapper = pair.getElement2();
            int id = idWrapper;

            writer.write("<h3><a name=\"repo" + id + "\">" + (errors.isEmpty() ? "" : "<a name=\"err" + erroneousRepo + "\">")
                + (repository.isComposed() ? "Composite" : "Simple") + " Repository <a href=\"" + repository.getURI() + "\">" + repository.getURI()
                + "</a></h3>\n");

            writer.write("<ul>\n");

            writer.write("<li><span style=\"white-space:nowrap;\">");
            writer.write(new Date(repository.getTimestamp()).toString());
            writer.write("</span>&nbsp;(");
            writer.write(String.valueOf(repository.getTimestamp()));
            writer.write(")\n");

            if (repository.isCompressed())
            {
              writer.write("<li>Compressed\n");
            }

            writer.write("<li>");
            int iuCount = getIUCount(repository, childrenMap);
            writer.write(String.valueOf(iuCount));
            writer.write("&nbsp;installable&nbsp;");
            writer.write(iuCount == 1 ? "unit" : "units");
            writer.write("\n");

            writer.write("<li>");
            int capabilityCount = getCapabilityCount(repository, childrenMap);
            writer.write(String.valueOf(capabilityCount));
            writer.write("&nbsp;");
            writer.write(capabilityCount == 1 ? "capability" : "capabilities");
            writer.write("\n");

            if (!errors.isEmpty())
            {
              writer.write("<li class=\"error\">");
              writer.write(String.valueOf(errors.size()));
              writer.write("&nbsp;");
              writer.write(errors.size() == 1 ? "error" : "errors");
              writer.write("\n");
            }

            writer.write("</ul>\n");

            if (!errors.isEmpty())
            {
              writer.write("<h4>Errors</h4>\n");
              writer.write("<ul>\n");

              for (String error : errors)
              {
                writer.write("<li class=\"error\">" + error.replace("\n", "<br>") + "\n");
              }

              if (++erroneousRepo < erroneousRepos)
              {
                writer.write("<li><a class=\"button\" href=\"#err" + erroneousRepo + "\">Goto next error</a>\n");
              }

              writer.write("</ul>\n");
            }

            Set<Repository> children = childrenMap.get(repository);
            if (children != null && !children.isEmpty())
            {
              writer.write("<h4>Children</h4>\n");
              writer.write("<ul>\n");

              for (Repository child : children)
              {
                writer.write("<li>");
                writeRepositoryLink(writer, child, ids);
                writer.write("\n");
              }

              writer.write("</ul>\n");
            }

            List<Repository.Composite> composites = repository.getComposites();
            if (composites != null && !composites.isEmpty())
            {
              writer.write("<h4>Composites</h4>\n");
              writer.write("<ul>\n");

              for (Repository.Composite composite : composites)
              {
                writer.write("<li>");
                writeRepositoryLink(writer, composite, ids);
                writer.write("\n");
              }

              writer.write("</ul>\n");
            }
          }

          writer.write("</body>\n");
          writer.write("</html>\n");
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
        finally
        {
          IOUtil.close(writer);
        }

        return true;
      }

      private void writeRepositoryLink(BufferedWriter writer, Repository repository, Map<Repository, Pair<Project, Integer>> ids) throws IOException
      {
        Pair<Project, Integer> pair = ids.get(repository);
        Project project = pair.getElement1();
        int id = pair.getElement2();

        String href = project != null ? (project == this ? "" : project.getFileName()) + "#repo" + id : repository.getURI().toString();
        writer.write("<a href=\"" + href + "\">" + repository.getURI() + "</a>");
      }

      private int getIUCount(Repository repository, Map<Repository, Set<Repository>> childrenMap)
      {
        if (repository instanceof Repository.Composite)
        {
          int sum = 0;

          Set<Repository> children = childrenMap.get(repository);
          if (children != null && !children.isEmpty())
          {
            for (Repository child : children)
            {
              sum += getIUCount(child, childrenMap);
            }
          }

          return sum;
        }

        return repository.getIUCount();
      }

      private int getCapabilityCount(Repository repository, Map<Repository, Set<Repository>> childrenMap)
      {
        if (repository instanceof Repository.Composite)
        {
          int sum = 0;

          Set<Repository> children = childrenMap.get(repository);
          if (children != null && !children.isEmpty())
          {
            for (Repository child : children)
            {
              sum += getCapabilityCount(child, childrenMap);
            }
          }

          return sum;
        }

        return repository.getCapabilityCount();
      }
    }
  }
}
