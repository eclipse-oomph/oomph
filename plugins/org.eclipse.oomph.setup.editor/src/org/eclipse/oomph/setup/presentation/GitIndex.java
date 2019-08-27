/*
 * Copyright (c) 2018 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class GitIndex
{
  public static void main(String[] args) throws Exception
  {
    ZipFile zipFile = new ZipFile(args[0]);

    File target = new File(args[1]);

    Map<String, Map<String, Map<String, Map<String, Set<String>>>>> repositoryIndices = new TreeMap<String, Map<String, Map<String, Map<String, Set<String>>>>>();

    Map<String, Map<String, Map<String, Set<String>>>> gitEclipseRepositoryIndex = new TreeMap<String, Map<String, Map<String, Set<String>>>>();
    repositoryIndices.put("https://git.eclipse.org/c/${0}/tree/${1} https://git.eclipse.org/c/${0}/plain/${1}", gitEclipseRepositoryIndex);
    for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();)
    {
      ZipEntry zipEntry = entries.nextElement();
      if (!zipEntry.isDirectory())
      {
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        String name = zipEntry.getName();
        String repo = name.substring(name.indexOf('/') + 1);
        List<String> lines = readLines(inputStream, "UTF-8");
        for (String line : lines)
        {
          handleJavaPath(gitEclipseRepositoryIndex, repo, line);
        }
        inputStream.close();
      }
    }
    zipFile.close();

    Collection<String> githubEclipseRepositories;
    if (Boolean.TRUE)
    {
      githubEclipseRepositories = getGithubEclipseRepositoryIndex();
    }
    else
    {
      githubEclipseRepositories = Arrays.asList(new String[] { //
          "eclipse/ConfigJSR", //
          "eclipse/TrademarkDocs", //
          "eclipse/Xpect", //
          "eclipse/aCute", //
          "eclipse/andmore", //
          "eclipse/birt", //
          "eclipse/buildr4eclipse", //
          "eclipse/buildship", //
          "eclipse/californium", //
          "eclipse/californium.actinium", //
          "eclipse/californium.core", //
          "eclipse/californium.element-connector", //
          "eclipse/californium.scandium", //
          "eclipse/californium.tools", //
          "eclipse/ceylon", //
          "eclipse/ceylon-herd", //
          "eclipse/ceylon-ide-common", //
          "eclipse/ceylon-ide-eclipse", //
          "eclipse/ceylon-ide-intellij", //
          "eclipse/ceylon-lang.org", //
          "eclipse/ceylon-sdk", //
          "eclipse/ceylon-web-ide-backend", //
          "eclipse/ceylon.formatter", //
          "eclipse/ceylon.tool.converter.java2ceylon", //
          "eclipse/cft", //
          "eclipse/che", //
          "eclipse/che-archetypes", //
          "eclipse/che-dependencies", //
          "eclipse/che-dev", //
          "eclipse/che-dockerfiles", //
          "eclipse/che-docs", //
          "eclipse/che-lib", //
          "eclipse/che-ls-jdt", //
          "eclipse/che-parent", //
          "eclipse/che-plugin-svn", //
          "eclipse/che-theia-env-variables-plugin", //
          "eclipse/che-theia-github-plugin", //
          "eclipse/che-theia-machines-plugin", //
          "eclipse/che-theia-ssh-plugin", //
          "eclipse/che-theia-terminal-plugin", //
          "eclipse/che-workspace-client", //
          "eclipse/clatest", //
          "eclipse/concierge", //
          "eclipse/corrosion", //
          "eclipse/cyclonedds", //
          "eclipse/dawnsci", //
          "eclipse/dirigible", //
          "eclipse/dirigible-samples", //
          "eclipse/ditto", //
          "eclipse/ditto-examples", //
          "eclipse/dsl4eclipse", //
          "eclipse/eavp", //
          "eclipse/ebr", //
          "eclipse/eclemma", //
          "eclipse/eclipse-collections", //
          "eclipse/eclipse-collections-kata", //
          "eclipse/eclipse-webhook", //
          "eclipse/eclipse.github.com", //
          "eclipse/eclipse.jdt.ls", //
          "eclipse/eclipselink.runtime", //
          "eclipse/edje", //
          "eclipse/efxclipse", //
          "eclipse/efxclipse-eclipse", //
          "eclipse/efxclipse-rt", //
          "eclipse/elk", //
          "eclipse/elk-models", //
          "eclipse/flux", //
          "eclipse/gef", //
          "eclipse/gef-legacy", //
          "eclipse/gemini.blueprint", //
          "eclipse/gemoc-studio", //
          "eclipse/gemoc-studio-modeldebugging", //
          "eclipse/gerrit-cla-plugin", //
          "eclipse/golo-lang", //
          "eclipse/gsc-ec-converter", //
          "eclipse/hawkbit", //
          "eclipse/hawkbit-examples", //
          "eclipse/hawkbit-extensions", //
          "eclipse/hipp2jipp", //
          "eclipse/hono", //
          "eclipse/ice", //
          "eclipse/iottestware", //
          "eclipse/iottestware.coap", //
          "eclipse/iottestware.fuzzing", //
          "eclipse/iottestware.mqtt", //
          "eclipse/iottestware.opcua", //
          "eclipse/january", //
          "eclipse/january-forms", //
          "eclipse/jdtc", //
          "eclipse/jetty.alpn.api", //
          "eclipse/jetty.parent", //
          "eclipse/jetty.project", //
          "eclipse/jetty.toolchain", //
          "eclipse/jetty.website", //
          "eclipse/jnosql-artemis", //
          "eclipse/jnosql-artemis-extension", //
          "eclipse/jnosql-diana", //
          "eclipse/jnosql-diana-driver", //
          "eclipse/jnosql-parent", //
          "eclipse/kapua", //
          "eclipse/keti", //
          "eclipse/kura", //
          "eclipse/kura-apps", //
          "eclipse/leshan", //
          "eclipse/leshan.osgi", //
          "eclipse/lsp4j", //
          "eclipse/lyo-store", //
          "eclipse/lyo-trs-client", //
          "eclipse/lyo-trs-server", //
          "eclipse/lyo-validation", //
          "eclipse/lyo.rio", //
          "eclipse/manifest", //
          "eclipse/microprofile", //
          "eclipse/microprofile-bom", //
          "eclipse/microprofile-conference", //
          "eclipse/microprofile-config", //
          "eclipse/microprofile-evolution-process", //
          "eclipse/microprofile-fault-tolerance", //
          "eclipse/microprofile-health", //
          "eclipse/microprofile-jwt-auth", //
          "eclipse/microprofile-lra", //
          "eclipse/microprofile-metrics", //
          "eclipse/microprofile-open-api", //
          "eclipse/microprofile-opentracing", //
          "eclipse/microprofile-parent", //
          "eclipse/microprofile-rest-client", //
          "eclipse/microprofile-samples", //
          "eclipse/microprofile-sandbox", //
          "eclipse/microprofile-service-mesh", //
          "eclipse/milo", //
          "eclipse/mita", //
          "eclipse/mosquitto", //
          "eclipse/mosquitto.rsmb", //
          "eclipse/n4js", //
          "eclipse/neoscada", //
          "eclipse/ocl", //
          "eclipse/omr", //
          "eclipse/omr.website", //
          "eclipse/openj9", //
          "eclipse/openj9-docs", //
          "eclipse/openj9-omr", //
          "eclipse/openj9-systemtest", //
          "eclipse/openj9-website", //
          "eclipse/org.eclipse.scout.docs", //
          "eclipse/orion", //
          "eclipse/orion.client", //
          "eclipse/orion.electron", //
          "eclipse/orion.server", //
          "eclipse/orion.server.node", //
          "eclipse/packagedrone", //
          "eclipse/paho.mqtt-sn.embedded-c", //
          "eclipse/paho.mqtt-spy", //
          "eclipse/paho.mqtt.android", //
          "eclipse/paho.mqtt.c", //
          "eclipse/paho.mqtt.cpp", //
          "eclipse/paho.mqtt.d", //
          "eclipse/paho.mqtt.embedded-c", //
          "eclipse/paho.mqtt.golang", //
          "eclipse/paho.mqtt.java", //
          "eclipse/paho.mqtt.javascript", //
          "eclipse/paho.mqtt.m2mqtt", //
          "eclipse/paho.mqtt.python", //
          "eclipse/paho.mqtt.ruby", //
          "eclipse/paho.mqtt.rust", //
          "eclipse/paho.mqtt.testing", //
          "eclipse/ponte", //
          "eclipse/rdf4j", //
          "eclipse/rdf4j-doc", //
          "eclipse/rdf4j-storage", //
          "eclipse/rdf4j-testsuite", //
          "eclipse/rdf4j-tools", //
          "eclipse/reddeer", //
          "eclipse/richbeans", //
          "eclipse/risev2g", //
          "eclipse/scanning", //
          "eclipse/score", //
          "eclipse/sequoyah", //
          "eclipse/sirius-components", //
          "eclipse/smarthome", //
          "eclipse/smarthome-designer", //
          "eclipse/smarthome-packaging-sample", //
          "eclipse/smarthome.osgi-ri.enocean", //
          "eclipse/sumo", //
          "eclipse/texlipse", //
          "eclipse/thym", //
          "eclipse/tiaki-c", //
          "eclipse/tiaki-java", //
          "eclipse/titan.EclipsePlug-ins", //
          "eclipse/titan.Libraries.TCCUsefulFunctions", //
          "eclipse/titan.ProtocolModules.COMMON", //
          "eclipse/titan.ProtocolModules.DHCP", //
          "eclipse/titan.ProtocolModules.DHCPv6", //
          "eclipse/titan.ProtocolModules.DIAMETER_ProtocolModule_Generator", //
          "eclipse/titan.ProtocolModules.DNS", //
          "eclipse/titan.ProtocolModules.FrameRelay", //
          "eclipse/titan.ProtocolModules.FrameRelay-", //
          "eclipse/titan.ProtocolModules.H248_v2", //
          "eclipse/titan.ProtocolModules.HTTP2.0", //
          "eclipse/titan.ProtocolModules.ICAP", //
          "eclipse/titan.ProtocolModules.ICMP", //
          "eclipse/titan.ProtocolModules.ICMPv6", //
          "eclipse/titan.ProtocolModules.IKEv2", //
          "eclipse/titan.ProtocolModules.IMAP_4rev1", //
          "eclipse/titan.ProtocolModules.IP", //
          "eclipse/titan.ProtocolModules.IPsec", //
          "eclipse/titan.ProtocolModules.IUA", //
          "eclipse/titan.ProtocolModules.JSON_v07_2006", //
          "eclipse/titan.ProtocolModules.L2TP", //
          "eclipse/titan.ProtocolModules.M3UA", //
          "eclipse/titan.ProtocolModules.MIME", //
          "eclipse/titan.ProtocolModules.MSRP", //
          "eclipse/titan.ProtocolModules.PPP", //
          "eclipse/titan.ProtocolModules.ProtoBuff", //
          "eclipse/titan.ProtocolModules.RADIUS_ProtocolModule_Generator", //
          "eclipse/titan.ProtocolModules.RTP", //
          "eclipse/titan.ProtocolModules.RTSP", //
          "eclipse/titan.ProtocolModules.SMPP", //
          "eclipse/titan.ProtocolModules.SMTP", //
          "eclipse/titan.ProtocolModules.SNMP", //
          "eclipse/titan.ProtocolModules.SRTP", //
          "eclipse/titan.ProtocolModules.TCP", //
          "eclipse/titan.ProtocolModules.UDP", //
          "eclipse/titan.ProtocolModules.WebSocket", //
          "eclipse/titan.ProtocolModules.XMPP", //
          "eclipse/titan.TestPorts.Common_Components.Abstract_Socket", //
          "eclipse/titan.TestPorts.Common_Components.Socket-API", //
          "eclipse/titan.TestPorts.HTTPmsg", //
          "eclipse/titan.TestPorts.IPL4asp", //
          "eclipse/titan.TestPorts.LANL2asp", //
          "eclipse/titan.TestPorts.LDAPasp_RFC4511", //
          "eclipse/titan.TestPorts.LDAPasp_RFC4511-", //
          "eclipse/titan.TestPorts.LDAPmsg", //
          "eclipse/titan.TestPorts.LDAPmsg-", //
          "eclipse/titan.TestPorts.PCAPasp", //
          "eclipse/titan.TestPorts.PIPEasp", //
          "eclipse/titan.TestPorts.SCTPasp", //
          "eclipse/titan.TestPorts.SIPmsg", //
          "eclipse/titan.TestPorts.SQLasp", //
          "eclipse/titan.TestPorts.SSHCLIENTasp", //
          "eclipse/titan.TestPorts.STDINOUTmsg", //
          "eclipse/titan.TestPorts.SUNRPCasp", //
          "eclipse/titan.TestPorts.SUNRPCasp-", //
          "eclipse/titan.TestPorts.TCPasp", //
          "eclipse/titan.TestPorts.TELNETasp", //
          "eclipse/titan.TestPorts.UDPasp", //
          "eclipse/titan.TestPorts.UNIX_DOMAIN_SOCKETasp", //
          "eclipse/titan.core", //
          "eclipse/titan.misc", //
          "eclipse/tm", //
          "eclipse/tm4e", //
          "eclipse/triquetrum", //
          "eclipse/unide", //
          "eclipse/unide.java", //
          "eclipse/unide.python", //
          "eclipse/vert.x", //
          "eclipse/vorto", //
          "eclipse/wakaama", //
          "eclipse/webtools.jsdt", //
          "eclipse/whiskers.arduino", //
          "eclipse/whiskers.js", //
          "eclipse/winery", //
          "eclipse/www.eclipse.org-collections", //
          "eclipse/xacc", //
          "eclipse/xsemantics", //
          "eclipse/xtext", //
          "eclipse/xtext-core", //
          "eclipse/xtext-eclipse", //
          "eclipse/xtext-extras", //
          "eclipse/xtext-gradle", //
          "eclipse/xtext-idea", //
          "eclipse/xtext-lib", //
          "eclipse/xtext-maven", //
          "eclipse/xtext-umbrella", //
          "eclipse/xtext-web", //
          "eclipse/xtext-xtend", //
      });
    }

    if (Boolean.TRUE)
    {
      Map<String, Map<String, Map<String, Set<String>>>> githubEclipseRepositoryIndex = new TreeMap<String, Map<String, Map<String, Set<String>>>>();
      repositoryIndices.put("https://github.com/${0}/tree/master/${1} https://raw.githubusercontent.com/${0}/master/${1}", githubEclipseRepositoryIndex);
      for (String githubEclipseRepository : githubEclipseRepositories)
      {
        try
        {
          githubRepoIndex(githubEclipseRepositoryIndex, githubEclipseRepository, "");
        }
        catch (Exception ex)
        {
          ex.printStackTrace();
        }
      }
    }

    saveIndex(repositoryIndices, target);
  }

  private static void saveIndex(Map<String, Map<String, Map<String, Map<String, Set<String>>>>> repositoryIndices, File target) throws IOException
  {
    ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(target)));
    zipOutputStream.putNextEntry(new ZipEntry("index.txt"));

    PrintStream out = new PrintStream(zipOutputStream, false, "UTF-8");

    for (Map.Entry<String, Map<String, Map<String, Map<String, Set<String>>>>> entry : repositoryIndices.entrySet())
    {
      String link = entry.getKey();
      out.println(link);
      Map<String, Map<String, Map<String, Set<String>>>> repositoryIndex = entry.getValue();
      for (Map.Entry<String, Map<String, Map<String, Set<String>>>> repoEntry : repositoryIndex.entrySet())
      {
        String repo = repoEntry.getKey();
        out.print(" ");
        out.println(repo);
        Map<String, Map<String, Set<String>>> sourceFolders = repoEntry.getValue();
        for (Map.Entry<String, Map<String, Set<String>>> sourceFolderEntry : sourceFolders.entrySet())
        {
          String sourceFolder = sourceFolderEntry.getKey();
          out.print("  ");
          out.println(sourceFolder);
          Map<String, Set<String>> packages = sourceFolderEntry.getValue();
          for (Entry<String, Set<String>> packageEntry : packages.entrySet())
          {
            String packageName = packageEntry.getKey();
            out.print("   ");
            out.println(packageName);
            Set<String> classes = packageEntry.getValue();
            for (String className : classes)
            {
              out.print("    ");
              out.println(className);
              if (Boolean.FALSE)
              {
                System.out.print(packageName);
                System.out.print(".");
                System.out.print(className);
                System.out.print(" ->");
                System.out.print(" https://git.eclipse.org/c/");
                System.out.print(repo);
                System.out.print("/tree/");
                System.out.print(sourceFolder);
                System.out.print("/");
                System.out.print(packageName.replace('.', '/'));
                System.out.print("/");
                System.out.print(className);
                System.out.print(".java");
                System.out.println();
              }
            }
          }
        }
      }
    }

    out.close();
    zipOutputStream.close();
  }

  private static void handleJavaPath(Map<String, Map<String, Map<String, Set<String>>>> repositoryIndex, String repo, String line)
  {
    int rootPackageIndex = -1;
    if (!line.startsWith("test/") && !line.startsWith("tests/") && !line.endsWith("package-info.java"))
    {
      rootPackageIndex = line.indexOf("/org/");
      if (rootPackageIndex == -1)
      {
        rootPackageIndex = line.indexOf("/com/");
        if (rootPackageIndex == -1)
        {
          rootPackageIndex = line.indexOf("/javax/");
        }
      }
    }

    if (rootPackageIndex != -1)
    {
      String sourceFolder = line.substring(0, rootPackageIndex);
      String qualifiedName = line.substring(rootPackageIndex + 1, line.length() - 5).replace('/', '.');
      int index = qualifiedName.lastIndexOf('.');
      String packageName = qualifiedName.substring(0, index);
      String className = qualifiedName.substring(index + 1);

      Map<String, Map<String, Set<String>>> sourceFolders = repositoryIndex.get(repo);
      if (sourceFolders == null)
      {
        sourceFolders = new TreeMap<String, Map<String, Set<String>>>();
        repositoryIndex.put(repo, sourceFolders);
      }

      Map<String, Set<String>> packages = sourceFolders.get(sourceFolder);
      if (packages == null)
      {
        packages = new TreeMap<String, Set<String>>();
        sourceFolders.put(sourceFolder, packages);
      }

      Set<String> classes = packages.get(packageName);
      if (classes == null)
      {
        classes = new TreeSet<String>();
        packages.put(packageName, classes);
      }

      classes.add(className);
    }
  }

  private static final Pattern REPO_FULL_NAME_PATTERN = Pattern.compile("\"full_name\":\"([^\"]+)\"");

  private static final Pattern MIRROR_URL_PATTERN = Pattern.compile("\"mirror_url\":(\"(|[^\"]+)\"|null)");

  private static Set<String> getGithubEclipseRepositoryIndex() throws Exception
  {
    Map<String, String> repos = new TreeMap<String, String>();
    for (int i = 1; i < 500; ++i)
    {
      URL url = new URL("https://api.github.com/users/eclipse/repos?page=" + i);
      List<String> lines = readLines(url, "UTF-8");
      int count = 0;
      for (String line : lines)
      {
        for (Matcher matcher = REPO_FULL_NAME_PATTERN.matcher(line); matcher.find();)
        {
          String repo = matcher.group(1);
          Matcher mirrorMatcher = MIRROR_URL_PATTERN.matcher(line);
          if (!mirrorMatcher.find(matcher.end()))
          {
            System.err.println(line.substring(matcher.end()));
          }

          ++count;
          repos.put(repo, mirrorMatcher.group(2));
        }
      }
      if (count == 0)
      {
        break;
      }
    }

    for (Iterator<Map.Entry<String, String>> it = repos.entrySet().iterator(); it.hasNext();)
    {
      Map.Entry<String, String> entry = it.next();
      if (entry.getValue() == null)
      {
        System.out.println("\"" + entry.getKey() + "\", //");
      }
      else
      {
        it.remove();
      }
    }

    return repos.keySet();
  }

  public static void githubRepoIndex(Map<String, Map<String, Map<String, Set<String>>>> repositoryIndex, String repo, String path) throws Exception
  {
    URL url = new URL("https://github.com/" + repo + "/tree/master/" + path);
    List<String> lines = readLines(url, "UTF-8");
    Pattern folderPattern = Pattern.compile("href=\"/" + repo + "/tree/master/([^\"]+)\"");
    Pattern filePattern = Pattern.compile("href=\"/" + repo + "/blob/master/([^\"]+)\"");
    for (String line : lines)
    {
      Matcher matcher = folderPattern.matcher(line);
      if (matcher.find())
      {
        String folder = matcher.group(1);
        int index = folder.lastIndexOf('/');
        String folderName = index == -1 ? folder : folder.substring(index + 1);
        if (line.indexOf(">" + folderName + "</a>") != -1)
        {
          System.out.println(repo + "/" + folder);
          githubRepoIndex(repositoryIndex, repo, folder);
        }
      }
      else
      {
        matcher = filePattern.matcher(line);
        if (matcher.find())
        {
          String file = matcher.group(1);
          // System.out.println(">" + file);
          if (file.endsWith(".java"))
          {
            handleJavaPath(repositoryIndex, repo, file);
          }
        }
      }
    }
  }

  private static List<String> readLines(URL url, String charsetName) throws Exception
  {
    IOException exception = null;
    for (int i = 0; i < 5; ++i)
    {
      try
      {
        InputStream inputStream = url.openStream();
        List<String> lines = readLines(inputStream, "UTF-8");
        return lines;
      }
      catch (IOException ex)
      {
        exception = ex;
        System.err.println(ex.getLocalizedMessage());
        Thread.sleep(60000);
      }
    }
    throw exception;
  }

  private static List<String> readLines(InputStream in, String charsetName) throws IOException
  {
    List<String> lines = new ArrayList<String>();

    Reader reader = null;
    BufferedReader bufferedReader = null;

    try
    {
      reader = new InputStreamReader(in, charsetName);
      bufferedReader = new BufferedReader(reader);

      String line;
      while ((line = bufferedReader.readLine()) != null)
      {
        lines.add(line);
      }
    }
    finally
    {
      if (bufferedReader != null)
      {
        bufferedReader.close();
      }
      if (reader != null)
      {
        reader.close();
      }
    }

    return lines;
  }
}
