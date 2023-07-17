/*
 * Copyright (c) 2023 Ed Merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Edt Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.git.util;

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.log.ProgressLog;
import org.eclipse.oomph.setup.log.ProgressLogMonitor;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.egit.core.EclipseGitProgressTransformer;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jgit.api.Git;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Ed Merks
 */
@SuppressWarnings("nls")
public class GitIndexApplication implements IApplication
{
  private static final Pattern GITHUB_REPO_PATTERN = Pattern.compile("https://github.com/(([^/]+)/(.*))");

  private static final Pattern GIT_ECLIPSE_REPO_PATTERN = Pattern.compile("https://git.eclipse.org/r/(([^/]+)/(.*))");

  private static final Pattern GITLAB_ECLIPSE_REPO_PATTERN = Pattern.compile("https://gitlab.eclipse.org/(([^/]+)/(.*))");

  private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([^;]+)\\s*;");

  private final Map<String, Map<String, Map<String, Map<String, Set<String>>>>> repositoryIndices = new TreeMap<>();

  private ContentHandler contentHandler;

  private File target;

  @Override
  public Object start(IApplicationContext context) throws Exception
  {
    var args = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);

    contentHandler = new ContentHandler(args[0]);
    target = new File(args[1]);
    index();

    return null;
  }

  @Override
  public void stop()
  {
  }

  public void index() throws Exception
  {
    var projectsBaseURL = "https://projects.eclipse.org/api/projects/?pagesize=50&page=";
    var projectIDs = new TreeSet<String>();
    for (var i = 0; i < 50; ++i)
    {
      var pageURL = projectsBaseURL + i;
      System.out.println("Processing " + pageURL);
      var body = contentHandler.getContent(pageURL);
      if (body.length() < 10)
      {
        break;
      }

      var ids = getValues("project_id", body);
      projectIDs.addAll(ids);
    }

    projectIDs.removeIf(id -> {
      if (id.equals("automotive.sphinx"))
      {
        return false;
      }

      if (!id.contains("."))
      {
        return true;
      }

      if (id.startsWith("adoptium") || id.startsWith("automotive") || id.startsWith("dt") || id.startsWith("ecd") || id.startsWith("ee4j")
          || id.startsWith("iot") || id.startsWith("locationtech") || id.startsWith("polarsys") || id.startsWith("oniro") || id.startsWith("technology.openj9")
          || id.startsWith("tools.titan") || id.startsWith("technology.microprofile") || id.startsWith("technology.edc") //
          || id.startsWith("technology.openk")//
          || id.startsWith("eclipse.e4")//
          || id.startsWith("technology.osbp")//
          || id.startsWith("technology.graphene")//
          || id.startsWith("technology.pass")//
          || id.startsWith("openhw")//
          || id.startsWith("technology.recommenders"))
      {
        return true;
      }
      return false;
    });

    var repositories = Collections.synchronizedSet(new TreeSet<String>());
    projectIDs.parallelStream().forEach(projectID -> {
      try
      {
        repositories.addAll(getRepositories(projectID));
      }
      catch (IOException ex)
      {
        throw new IORuntimeException(ex);
      }
    });

    var count = 0;
    var size = repositories.size();
    for (var repo : repositories)
    {
      if ("true".equals(System.getProperty("org.eclipse.oomph.setup.git.util.GitIndexApplication.test")))
      {
        if (repo.startsWith("https://github.com") && repo.endsWith("/org.eclipse.mylyn") || "https://git.eclipse.org/r/jgit/jgit".equals(repo)
            || "https://gitlab.eclipse.org/eclipse/xpand/org.eclipse.xpand".equals(repo))
        {
          System.out.println("----------------" + ++count + " of " + size + " -------------------------");
          index(repo);
        }
      }
      else
      {
        System.out.println("----------------" + ++count + " of " + size + " -------------------------");
        index(repo);
      }
    }

    saveIndex(target);
  }

  public String getKey(String repo, String branch, StringBuilder baseRepo)
  {
    var githubMatcher = GITHUB_REPO_PATTERN.matcher(repo);
    if (githubMatcher.matches())
    {
      baseRepo.append(githubMatcher.group(1));
      return "https://github.com/${0}/tree/" + branch + "/${1} https://raw.githubusercontent.com/${0}/" + branch + "/${1}";
    }

    var gitEclipseMatcher = GIT_ECLIPSE_REPO_PATTERN.matcher(repo);
    if (gitEclipseMatcher.matches())
    {
      baseRepo.append(gitEclipseMatcher.group(1) + ".git");
      return "https://git.eclipse.org/c/${0}/tree/${1} https://git.eclipse.org/c/${0}/plain/${1}";
    }

    var gitlablEclipseMatcher = GITLAB_ECLIPSE_REPO_PATTERN.matcher(repo);
    if (gitlablEclipseMatcher.matches())
    {
      baseRepo.append(gitlablEclipseMatcher.group(1));
      return "https://gitlab.eclipse.org/${0}/-/blob/" + branch + "/${1} https://gitlab.eclipse.org/${0}/-/raw/" + branch + "/${1}";
    }

    throw new IllegalArgumentException(repo);
  }

  public void index(String repo) throws Exception
  {
    System.out.println("Cloning: " + repo);
    long start = System.currentTimeMillis();
    var cloneRepository = Git.cloneRepository();
    cloneRepository.setCloneSubmodules(false);
    cloneRepository.setURI(repo);
    cloneRepository.setDepth(1);
    var cloneFolder = Files.createTempDirectory("git-clone");
    cloneRepository.setDirectory(cloneFolder.toFile());
    cloneRepository.setProgressMonitor(new EclipseGitProgressTransformer(new ProgressLogMonitor(new ProgressLog()
    {
      @Override
      public boolean isCanceled()
      {
        return false;
      }

      @Override
      public void log(String line)
      {
        if (line.contains("%"))
        {
          if (line.contains("0%"))
          {
            System.out.println(line);
          }
        }
        else
        {
          System.out.println(line);
        }
      }

      @Override
      public void log(String line, Severity severity)
      {
      }

      @Override
      public void log(String line, boolean filter)
      {
      }

      @Override
      public void log(String line, boolean filter, Severity severity)
      {
      }

      @Override
      public void log(IStatus status)
      {
      }

      @Override
      public void log(Throwable t)
      {
      }

      @Override
      public void task(SetupTask setupTask)
      {
      }

      @Override
      public void setTerminating()
      {
      }
    })));

    var clone = cloneRepository.call();

    long finish = System.currentTimeMillis();

    System.out.println("Cloned: " + repo + " elapsed=" + (finish - start) / 1000);

    var repository = clone.getRepository();
    var branch = repository.getBranch();
    var baseRepo = new StringBuilder();
    var repositoryIndex = repositoryIndices.computeIfAbsent(getKey(repo, branch, baseRepo), key -> new TreeMap<>());
    var base = baseRepo.toString();
    var javaCount = new AtomicInteger();

    Files.walkFileTree(cloneFolder, new SimpleFileVisitor<Path>()
    {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException
      {
        var fileName = file.getFileName().toString();
        if (fileName.endsWith(".java"))
        {
          for (var cs : new Charset[] { StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1, StandardCharsets.UTF_16 })
          {
            try (var in = Files.newBufferedReader(file, cs))
            {
              for (var line = in.readLine(); line != null; line = in.readLine())
              {
                var matcher = PACKAGE_PATTERN.matcher(line);
                if (matcher.find())
                {
                  var relativePath = cloneFolder.relativize(file.getParent()).toString().replace('\\', '/');
                  var packageName = matcher.group(1);
                  var className = fileName.substring(0, fileName.length() - ".java".length());
                  var packagePath = "/" + packageName.replace('.', '/');
                  if (relativePath.endsWith(packagePath))
                  {
                    var relativeBasePath = relativePath.substring(0, relativePath.length() - packagePath.length());
                    javaCount.incrementAndGet();
                    repositoryIndex.computeIfAbsent(base, key -> new TreeMap<>()).computeIfAbsent(relativeBasePath, key -> new TreeMap<>())
                        .computeIfAbsent(packageName, key -> new TreeSet<>()).add(className);
                  }

                  return FileVisitResult.CONTINUE;
                }
              }
            }
            catch (MalformedInputException ex)
            {
              // Some files are not UTF-8.
              //
              continue;
            }

            break;
          }
        }
        return FileVisitResult.CONTINUE;
      }

      @Override
      public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
      {
        var name = dir.getFileName().toString();
        if (name.startsWith("."))
        {
          return FileVisitResult.SKIP_SUBTREE;
        }

        return super.preVisitDirectory(dir, attrs);
      }
    });

    System.out.println("Indexed: " + repo + " count=" + javaCount);

    clone.close();

    IOUtil.deleteBestEffort(cloneFolder.toFile());
  }

  public Set<String> getRepositories(String projectID) throws IOException
  {
    var result = new TreeSet<String>();
    var content = getPMIContent(projectID);

    if (content.contains("\"state\":\"Archived\""))
    {
      return result;
    }

    var github = getGroup("github", content);
    if (github != null)
    {
      var orgs = getValues("org", github);
      for (var org : orgs)
      {
        for (var i = 1; i < 10; i++)
        {
          var repos = contentHandler.getContent("https://api.github.com/orgs/" + org + "/repos?page=" + i);
          var urls = getValues("html_url", repos);
          urls.remove("https://github.com/" + org);
          if (urls.isEmpty())
          {
            break;
          }

          result.addAll(urls);
        }
      }
    }

    var githubRepos = getSection("github_repos", content);
    if (githubRepos != null)
    {
      result.addAll(getValues("url", githubRepos));
    }

    cleanupRepos(result);

    if (result.isEmpty())
    {
      var gerritRepos = getSection("gerrit_repos", content);
      if (gerritRepos != null)
      {
        result.addAll(getValues("url", gerritRepos));
        cleanupRepos(result);
      }
    }

    var gitlab = getGroup("gitlab", content);
    if (gitlab != null)
    {
      var groups = getValues("project_group", gitlab);
      for (var group : groups)
      {
        for (var i = 1; i < 10; i++)
        {
          var repos = contentHandler.getContent("https://gitlab.eclipse.org/api/v4/groups/" + group.replace("/", "%2f") + "?page=" + i);
          var urls = getValues("http_url_to_repo", repos);
          if (!result.addAll(urls.stream().map(it -> it.replaceAll("\\.git$", "")).collect(Collectors.toList())))
          {
            break;
          }
        }
      }
    }

    var gitlabRepos = getSection("gitlab_repos", content);
    if (githubRepos != null)
    {
      result.addAll(getValues("url", gitlabRepos));
      cleanupRepos(result);
    }

    return result;
  }

  private void cleanupRepos(Set<String> repos)
  {
    repos.removeIf(it -> {
      if (it.contains("/org.eclipse.mylyn.") && !it.endsWith("/org.eclipse.mylyn.docs"))
      {
        return true;
      }

      return it.endsWith("/.github") || it.endsWith("/ui-best-practices") || it.endsWith("/.eclipsefdn") || it.contains("www.eclipse.org")
          || it.endsWith(".incubator") || it.contains("website") || it.endsWith(".github.io") || it.endsWith(".binaries");
    });
  }

  public String getPMIContent(String projectID) throws IOException
  {
    var pmiID = projectID.replace('.', '_');
    return contentHandler.getContent("https://projects.eclipse.org/api/projects/" + pmiID);
  }

  private String getGroup(String key, String content)
  {
    // {{
    var pattern = Pattern.compile('"' + key + "\":\\{([^}]*)\\}"); // }
    var matcher = pattern.matcher(content);
    if (matcher.find())
    {
      return matcher.group(1);
    }

    return null;
  }

  private String getSection(String key, String content)
  {
    var pattern = Pattern.compile('"' + key + "\":\\[([^]]*)\\]");
    var matcher = pattern.matcher(content);
    if (matcher.find())
    {
      return matcher.group(1);
    }

    return null;
  }

  private Set<String> getValues(String key, String content)
  {
    var result = new LinkedHashSet<String>();
    var matcher = Pattern.compile('"' + key + "\":\"([^\"]+)\"").matcher(content);
    while (matcher.find())
    {
      result.add(matcher.group(1));
    }

    return result;
  }

  private void saveIndex(File target) throws IOException
  {
    System.out.println("Saving: " + target.getAbsolutePath());

    var zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)));
    zipOutputStream.putNextEntry(new ZipEntry("index.txt"));

    var out = new PrintStream(zipOutputStream, false, "UTF-8");
    for (var entry : repositoryIndices.entrySet())
    {
      var link = entry.getKey();
      out.println(link);
      var repositoryIndex = entry.getValue();
      for (var repoEntry : repositoryIndex.entrySet())
      {
        var repo = repoEntry.getKey();
        out.print(" ");
        out.println(repo);
        var sourceFolders = repoEntry.getValue();
        for (var sourceFolderEntry : sourceFolders.entrySet())
        {
          var sourceFolder = sourceFolderEntry.getKey();
          out.print("  ");
          out.println(sourceFolder);
          var packages = sourceFolderEntry.getValue();
          for (Entry<String, Set<String>> packageEntry : packages.entrySet())
          {
            var packageName = packageEntry.getKey();
            out.print("   ");
            out.println(packageName);
            var classes = packageEntry.getValue();
            for (var className : classes)
            {
              out.print("    ");
              out.println(className);
            }
          }
        }
      }
    }

    out.close();
    zipOutputStream.close();
  }

  private static class ContentHandler
  {
    private Path cache;

    public ContentHandler(String cache)
    {
      try
      {
        if (cache != null)
        {
          this.cache = Path.of(cache);
        }
        else
        {
          this.cache = Files.createTempDirectory("org.eclipse.oomph.git.cache");
        }
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
    }

    protected String basicGetContent(URI uri) throws IOException, InterruptedException
    {
      var httpClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();
      var requestBuilder = HttpRequest.newBuilder(uri).GET();
      var request = requestBuilder.build();
      var response = httpClient.send(request, BodyHandlers.ofString());
      var statusCode = response.statusCode();
      if (statusCode != 200)
      {
        throw new IOException("status code " + statusCode + " -> " + uri);
      }
      return response.body();
    }

    protected Path getCachePath(URI uri)
    {
      var decodedURI = URLDecoder.decode(uri.toString(), StandardCharsets.UTF_8);
      var isFolder = decodedURI.endsWith("/");
      var uriSegments = decodedURI.split("[:/?#&;]+");
      var relativePath = String.join("/", uriSegments).replace('=', '-');
      if (isFolder)
      {
        relativePath += "_._";
      }

      var result = cache.resolve(relativePath);
      return result;
    }

    public String getContent(String uri) throws IOException
    {
      return getContent(URI.create(uri));
    }

    public String getContent(URI uri) throws IOException
    {
      if ("file".equals(uri.getScheme()))
      {
        return Files.readString(Path.of(uri));
      }

      var path = getCachePath(uri);
      if (Files.isRegularFile(path))
      {
        var lastModifiedTime = Files.getLastModifiedTime(path);
        var now = System.currentTimeMillis();
        var age = now - lastModifiedTime.toMillis();
        var ageInHours = age / 1000 / 60 / 60;
        if (ageInHours < 8)
        {
          return Files.readString(path);
        }
      }

      try
      {
        var content = basicGetContent(uri);
        Files.createDirectories(path.getParent());
        writeString(path, content);
        return content;
      }
      catch (InterruptedException e)
      {
        throw new IOException(e);
      }
    }

    private static void writeString(Path path, CharSequence string) throws IOException
    {
      Files.writeString(path, string);
    }
  }
}
