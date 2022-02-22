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

    Map<String, Map<String, Map<String, Map<String, Set<String>>>>> repositoryIndices = new TreeMap<>();

    Map<String, Map<String, Map<String, Set<String>>>> gitEclipseRepositoryIndex = new TreeMap<>();
    repositoryIndices.put("https://git.eclipse.org/c/${0}/tree/${1} https://git.eclipse.org/c/${0}/plain/${1}", gitEclipseRepositoryIndex); //$NON-NLS-1$
    for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements();)
    {
      ZipEntry zipEntry = entries.nextElement();
      if (!zipEntry.isDirectory())
      {
        InputStream inputStream = zipFile.getInputStream(zipEntry);
        String name = zipEntry.getName();
        String repo = name.substring(name.indexOf('/') + 1);
        List<String> lines = readLines(inputStream, "UTF-8"); //$NON-NLS-1$
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
          "eclipse/ConfigJSR", //$NON-NLS-1$
          "eclipse/TrademarkDocs", //$NON-NLS-1$
          "eclipse/Xpect", //$NON-NLS-1$
          "eclipse/aCute", //$NON-NLS-1$
          "eclipse/andmore", //$NON-NLS-1$
          "eclipse/birt", //$NON-NLS-1$
          "eclipse/buildr4eclipse", //$NON-NLS-1$
          "eclipse/buildship", //$NON-NLS-1$
          "eclipse/californium", //$NON-NLS-1$
          "eclipse/californium.actinium", //$NON-NLS-1$
          "eclipse/californium.core", //$NON-NLS-1$
          "eclipse/californium.element-connector", //$NON-NLS-1$
          "eclipse/californium.scandium", //$NON-NLS-1$
          "eclipse/californium.tools", //$NON-NLS-1$
          "eclipse/ceylon", //$NON-NLS-1$
          "eclipse/ceylon-herd", //$NON-NLS-1$
          "eclipse/ceylon-ide-common", //$NON-NLS-1$
          "eclipse/ceylon-ide-eclipse", //$NON-NLS-1$
          "eclipse/ceylon-ide-intellij", //$NON-NLS-1$
          "eclipse/ceylon-lang.org", //$NON-NLS-1$
          "eclipse/ceylon-sdk", //$NON-NLS-1$
          "eclipse/ceylon-web-ide-backend", //$NON-NLS-1$
          "eclipse/ceylon.formatter", //$NON-NLS-1$
          "eclipse/ceylon.tool.converter.java2ceylon", //$NON-NLS-1$
          "eclipse/cft", //$NON-NLS-1$
          "eclipse/che", //$NON-NLS-1$
          "eclipse/che-archetypes", //$NON-NLS-1$
          "eclipse/che-dependencies", //$NON-NLS-1$
          "eclipse/che-dev", //$NON-NLS-1$
          "eclipse/che-dockerfiles", //$NON-NLS-1$
          "eclipse/che-docs", //$NON-NLS-1$
          "eclipse/che-lib", //$NON-NLS-1$
          "eclipse/che-ls-jdt", //$NON-NLS-1$
          "eclipse/che-parent", //$NON-NLS-1$
          "eclipse/che-plugin-svn", //$NON-NLS-1$
          "eclipse/che-theia-env-variables-plugin", //$NON-NLS-1$
          "eclipse/che-theia-github-plugin", //$NON-NLS-1$
          "eclipse/che-theia-machines-plugin", //$NON-NLS-1$
          "eclipse/che-theia-ssh-plugin", //$NON-NLS-1$
          "eclipse/che-theia-terminal-plugin", //$NON-NLS-1$
          "eclipse/che-workspace-client", //$NON-NLS-1$
          "eclipse/clatest", //$NON-NLS-1$
          "eclipse/concierge", //$NON-NLS-1$
          "eclipse/corrosion", //$NON-NLS-1$
          "eclipse/cyclonedds", //$NON-NLS-1$
          "eclipse/dawnsci", //$NON-NLS-1$
          "eclipse/dirigible", //$NON-NLS-1$
          "eclipse/dirigible-samples", //$NON-NLS-1$
          "eclipse/ditto", //$NON-NLS-1$
          "eclipse/ditto-examples", //$NON-NLS-1$
          "eclipse/dsl4eclipse", //$NON-NLS-1$
          "eclipse/eavp", //$NON-NLS-1$
          "eclipse/ebr", //$NON-NLS-1$
          "eclipse/eclemma", //$NON-NLS-1$
          "eclipse/eclipse-collections", //$NON-NLS-1$
          "eclipse/eclipse-collections-kata", //$NON-NLS-1$
          "eclipse/eclipse-webhook", //$NON-NLS-1$
          "eclipse/eclipse.github.com", //$NON-NLS-1$
          "eclipse/eclipse.jdt.ls", //$NON-NLS-1$
          "eclipse/eclipselink.runtime", //$NON-NLS-1$
          "eclipse/edje", //$NON-NLS-1$
          "eclipse/efxclipse", //$NON-NLS-1$
          "eclipse/efxclipse-eclipse", //$NON-NLS-1$
          "eclipse/efxclipse-rt", //$NON-NLS-1$
          "eclipse/elk", //$NON-NLS-1$
          "eclipse/elk-models", //$NON-NLS-1$
          "eclipse/flux", //$NON-NLS-1$
          "eclipse/gef", //$NON-NLS-1$
          "eclipse/gef-legacy", //$NON-NLS-1$
          "eclipse/gemini.blueprint", //$NON-NLS-1$
          "eclipse/gemoc-studio", //$NON-NLS-1$
          "eclipse/gemoc-studio-modeldebugging", //$NON-NLS-1$
          "eclipse/gerrit-cla-plugin", //$NON-NLS-1$
          "eclipse/golo-lang", //$NON-NLS-1$
          "eclipse/gsc-ec-converter", //$NON-NLS-1$
          "eclipse/hawkbit", //$NON-NLS-1$
          "eclipse/hawkbit-examples", //$NON-NLS-1$
          "eclipse/hawkbit-extensions", //$NON-NLS-1$
          "eclipse/hipp2jipp", //$NON-NLS-1$
          "eclipse/hono", //$NON-NLS-1$
          "eclipse/ice", //$NON-NLS-1$
          "eclipse/iottestware", //$NON-NLS-1$
          "eclipse/iottestware.coap", //$NON-NLS-1$
          "eclipse/iottestware.fuzzing", //$NON-NLS-1$
          "eclipse/iottestware.mqtt", //$NON-NLS-1$
          "eclipse/iottestware.opcua", //$NON-NLS-1$
          "eclipse/january", //$NON-NLS-1$
          "eclipse/january-forms", //$NON-NLS-1$
          "eclipse/jdtc", //$NON-NLS-1$
          "eclipse/jetty.alpn.api", //$NON-NLS-1$
          "eclipse/jetty.parent", //$NON-NLS-1$
          "eclipse/jetty.project", //$NON-NLS-1$
          "eclipse/jetty.toolchain", //$NON-NLS-1$
          "eclipse/jetty.website", //$NON-NLS-1$
          "eclipse/jnosql-artemis", //$NON-NLS-1$
          "eclipse/jnosql-artemis-extension", //$NON-NLS-1$
          "eclipse/jnosql-diana", //$NON-NLS-1$
          "eclipse/jnosql-diana-driver", //$NON-NLS-1$
          "eclipse/jnosql-parent", //$NON-NLS-1$
          "eclipse/kapua", //$NON-NLS-1$
          "eclipse/keti", //$NON-NLS-1$
          "eclipse/kura", //$NON-NLS-1$
          "eclipse/kura-apps", //$NON-NLS-1$
          "eclipse/leshan", //$NON-NLS-1$
          "eclipse/leshan.osgi", //$NON-NLS-1$
          "eclipse/lsp4j", //$NON-NLS-1$
          "eclipse/lyo-store", //$NON-NLS-1$
          "eclipse/lyo-trs-client", //$NON-NLS-1$
          "eclipse/lyo-trs-server", //$NON-NLS-1$
          "eclipse/lyo-validation", //$NON-NLS-1$
          "eclipse/lyo.rio", //$NON-NLS-1$
          "eclipse/manifest", //$NON-NLS-1$
          "eclipse/microprofile", //$NON-NLS-1$
          "eclipse/microprofile-bom", //$NON-NLS-1$
          "eclipse/microprofile-conference", //$NON-NLS-1$
          "eclipse/microprofile-config", //$NON-NLS-1$
          "eclipse/microprofile-evolution-process", //$NON-NLS-1$
          "eclipse/microprofile-fault-tolerance", //$NON-NLS-1$
          "eclipse/microprofile-health", //$NON-NLS-1$
          "eclipse/microprofile-jwt-auth", //$NON-NLS-1$
          "eclipse/microprofile-lra", //$NON-NLS-1$
          "eclipse/microprofile-metrics", //$NON-NLS-1$
          "eclipse/microprofile-open-api", //$NON-NLS-1$
          "eclipse/microprofile-opentracing", //$NON-NLS-1$
          "eclipse/microprofile-parent", //$NON-NLS-1$
          "eclipse/microprofile-rest-client", //$NON-NLS-1$
          "eclipse/microprofile-samples", //$NON-NLS-1$
          "eclipse/microprofile-sandbox", //$NON-NLS-1$
          "eclipse/microprofile-service-mesh", //$NON-NLS-1$
          "eclipse/milo", //$NON-NLS-1$
          "eclipse/mita", //$NON-NLS-1$
          "eclipse/mosquitto", //$NON-NLS-1$
          "eclipse/mosquitto.rsmb", //$NON-NLS-1$
          "eclipse/n4js", //$NON-NLS-1$
          "eclipse/neoscada", //$NON-NLS-1$
          "eclipse/ocl", //$NON-NLS-1$
          "eclipse/omr", //$NON-NLS-1$
          "eclipse/omr.website", //$NON-NLS-1$
          "eclipse/openj9", //$NON-NLS-1$
          "eclipse/openj9-docs", //$NON-NLS-1$
          "eclipse/openj9-omr", //$NON-NLS-1$
          "eclipse/openj9-systemtest", //$NON-NLS-1$
          "eclipse/openj9-website", //$NON-NLS-1$
          "eclipse/org.eclipse.scout.docs", //$NON-NLS-1$
          "eclipse/orion", //$NON-NLS-1$
          "eclipse/orion.client", //$NON-NLS-1$
          "eclipse/orion.electron", //$NON-NLS-1$
          "eclipse/orion.server", //$NON-NLS-1$
          "eclipse/orion.server.node", //$NON-NLS-1$
          "eclipse/packagedrone", //$NON-NLS-1$
          "eclipse/paho.mqtt-sn.embedded-c", //$NON-NLS-1$
          "eclipse/paho.mqtt-spy", //$NON-NLS-1$
          "eclipse/paho.mqtt.android", //$NON-NLS-1$
          "eclipse/paho.mqtt.c", //$NON-NLS-1$
          "eclipse/paho.mqtt.cpp", //$NON-NLS-1$
          "eclipse/paho.mqtt.d", //$NON-NLS-1$
          "eclipse/paho.mqtt.embedded-c", //$NON-NLS-1$
          "eclipse/paho.mqtt.golang", //$NON-NLS-1$
          "eclipse/paho.mqtt.java", //$NON-NLS-1$
          "eclipse/paho.mqtt.javascript", //$NON-NLS-1$
          "eclipse/paho.mqtt.m2mqtt", //$NON-NLS-1$
          "eclipse/paho.mqtt.python", //$NON-NLS-1$
          "eclipse/paho.mqtt.ruby", //$NON-NLS-1$
          "eclipse/paho.mqtt.rust", //$NON-NLS-1$
          "eclipse/paho.mqtt.testing", //$NON-NLS-1$
          "eclipse/ponte", //$NON-NLS-1$
          "eclipse/rdf4j", //$NON-NLS-1$
          "eclipse/rdf4j-doc", //$NON-NLS-1$
          "eclipse/rdf4j-storage", //$NON-NLS-1$
          "eclipse/rdf4j-testsuite", //$NON-NLS-1$
          "eclipse/rdf4j-tools", //$NON-NLS-1$
          "eclipse/reddeer", //$NON-NLS-1$
          "eclipse/richbeans", //$NON-NLS-1$
          "eclipse/risev2g", //$NON-NLS-1$
          "eclipse/scanning", //$NON-NLS-1$
          "eclipse/score", //$NON-NLS-1$
          "eclipse/sequoyah", //$NON-NLS-1$
          "eclipse/sirius-components", //$NON-NLS-1$
          "eclipse/smarthome", //$NON-NLS-1$
          "eclipse/smarthome-designer", //$NON-NLS-1$
          "eclipse/smarthome-packaging-sample", //$NON-NLS-1$
          "eclipse/smarthome.osgi-ri.enocean", //$NON-NLS-1$
          "eclipse/sumo", //$NON-NLS-1$
          "eclipse/texlipse", //$NON-NLS-1$
          "eclipse/thym", //$NON-NLS-1$
          "eclipse/tiaki-c", //$NON-NLS-1$
          "eclipse/tiaki-java", //$NON-NLS-1$
          "eclipse/titan.EclipsePlug-ins", //$NON-NLS-1$
          "eclipse/titan.Libraries.TCCUsefulFunctions", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.COMMON", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.DHCP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.DHCPv6", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.DIAMETER_ProtocolModule_Generator", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.DNS", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.FrameRelay", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.FrameRelay-", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.H248_v2", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.HTTP2.0", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.ICAP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.ICMP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.ICMPv6", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.IKEv2", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.IMAP_4rev1", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.IP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.IPsec", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.IUA", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.JSON_v07_2006", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.L2TP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.M3UA", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.MIME", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.MSRP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.PPP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.ProtoBuff", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.RADIUS_ProtocolModule_Generator", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.RTP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.RTSP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.SMPP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.SMTP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.SNMP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.SRTP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.TCP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.UDP", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.WebSocket", //$NON-NLS-1$
          "eclipse/titan.ProtocolModules.XMPP", //$NON-NLS-1$
          "eclipse/titan.TestPorts.Common_Components.Abstract_Socket", //$NON-NLS-1$
          "eclipse/titan.TestPorts.Common_Components.Socket-API", //$NON-NLS-1$
          "eclipse/titan.TestPorts.HTTPmsg", //$NON-NLS-1$
          "eclipse/titan.TestPorts.IPL4asp", //$NON-NLS-1$
          "eclipse/titan.TestPorts.LANL2asp", //$NON-NLS-1$
          "eclipse/titan.TestPorts.LDAPasp_RFC4511", //$NON-NLS-1$
          "eclipse/titan.TestPorts.LDAPasp_RFC4511-", //$NON-NLS-1$
          "eclipse/titan.TestPorts.LDAPmsg", //$NON-NLS-1$
          "eclipse/titan.TestPorts.LDAPmsg-", //$NON-NLS-1$
          "eclipse/titan.TestPorts.PCAPasp", //$NON-NLS-1$
          "eclipse/titan.TestPorts.PIPEasp", //$NON-NLS-1$
          "eclipse/titan.TestPorts.SCTPasp", //$NON-NLS-1$
          "eclipse/titan.TestPorts.SIPmsg", //$NON-NLS-1$
          "eclipse/titan.TestPorts.SQLasp", //$NON-NLS-1$
          "eclipse/titan.TestPorts.SSHCLIENTasp", //$NON-NLS-1$
          "eclipse/titan.TestPorts.STDINOUTmsg", //$NON-NLS-1$
          "eclipse/titan.TestPorts.SUNRPCasp", //$NON-NLS-1$
          "eclipse/titan.TestPorts.SUNRPCasp-", //$NON-NLS-1$
          "eclipse/titan.TestPorts.TCPasp", //$NON-NLS-1$
          "eclipse/titan.TestPorts.TELNETasp", //$NON-NLS-1$
          "eclipse/titan.TestPorts.UDPasp", //$NON-NLS-1$
          "eclipse/titan.TestPorts.UNIX_DOMAIN_SOCKETasp", //$NON-NLS-1$
          "eclipse/titan.core", //$NON-NLS-1$
          "eclipse/titan.misc", //$NON-NLS-1$
          "eclipse/tm", //$NON-NLS-1$
          "eclipse/tm4e", //$NON-NLS-1$
          "eclipse/triquetrum", //$NON-NLS-1$
          "eclipse/unide", //$NON-NLS-1$
          "eclipse/unide.java", //$NON-NLS-1$
          "eclipse/unide.python", //$NON-NLS-1$
          "eclipse/vert.x", //$NON-NLS-1$
          "eclipse/vorto", //$NON-NLS-1$
          "eclipse/wakaama", //$NON-NLS-1$
          "eclipse/webtools.jsdt", //$NON-NLS-1$
          "eclipse/whiskers.arduino", //$NON-NLS-1$
          "eclipse/whiskers.js", //$NON-NLS-1$
          "eclipse/winery", //$NON-NLS-1$
          "eclipse/www.eclipse.org-collections", //$NON-NLS-1$
          "eclipse/xacc", //$NON-NLS-1$
          "eclipse/xsemantics", //$NON-NLS-1$
          "eclipse/xtext", //$NON-NLS-1$
          "eclipse/xtext-core", //$NON-NLS-1$
          "eclipse/xtext-eclipse", //$NON-NLS-1$
          "eclipse/xtext-extras", //$NON-NLS-1$
          "eclipse/xtext-gradle", //$NON-NLS-1$
          "eclipse/xtext-idea", //$NON-NLS-1$
          "eclipse/xtext-lib", //$NON-NLS-1$
          "eclipse/xtext-maven", //$NON-NLS-1$
          "eclipse/xtext-umbrella", //$NON-NLS-1$
          "eclipse/xtext-web", //$NON-NLS-1$
          "eclipse/xtext-xtend", //$NON-NLS-1$
      });
    }

    if (Boolean.TRUE)
    {
      Map<String, Map<String, Map<String, Set<String>>>> githubEclipseRepositoryIndex = new TreeMap<>();
      repositoryIndices.put("https://github.com/${0}/tree/master/${1} https://raw.githubusercontent.com/${0}/master/${1}", githubEclipseRepositoryIndex); //$NON-NLS-1$
      for (String githubEclipseRepository : githubEclipseRepositories)
      {
        try
        {
          githubRepoIndex(githubEclipseRepositoryIndex, githubEclipseRepository, ""); //$NON-NLS-1$
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
    zipOutputStream.putNextEntry(new ZipEntry("index.txt")); //$NON-NLS-1$

    PrintStream out = new PrintStream(zipOutputStream, false, "UTF-8"); //$NON-NLS-1$

    for (Map.Entry<String, Map<String, Map<String, Map<String, Set<String>>>>> entry : repositoryIndices.entrySet())
    {
      String link = entry.getKey();
      out.println(link);
      Map<String, Map<String, Map<String, Set<String>>>> repositoryIndex = entry.getValue();
      for (Map.Entry<String, Map<String, Map<String, Set<String>>>> repoEntry : repositoryIndex.entrySet())
      {
        String repo = repoEntry.getKey();
        out.print(" "); //$NON-NLS-1$
        out.println(repo);
        Map<String, Map<String, Set<String>>> sourceFolders = repoEntry.getValue();
        for (Map.Entry<String, Map<String, Set<String>>> sourceFolderEntry : sourceFolders.entrySet())
        {
          String sourceFolder = sourceFolderEntry.getKey();
          out.print("  "); //$NON-NLS-1$
          out.println(sourceFolder);
          Map<String, Set<String>> packages = sourceFolderEntry.getValue();
          for (Entry<String, Set<String>> packageEntry : packages.entrySet())
          {
            String packageName = packageEntry.getKey();
            out.print("   "); //$NON-NLS-1$
            out.println(packageName);
            Set<String> classes = packageEntry.getValue();
            for (String className : classes)
            {
              out.print("    "); //$NON-NLS-1$
              out.println(className);
              if (Boolean.FALSE)
              {
                System.out.print(packageName);
                System.out.print("."); //$NON-NLS-1$
                System.out.print(className);
                System.out.print(" ->"); //$NON-NLS-1$
                System.out.print(" https://git.eclipse.org/c/"); //$NON-NLS-1$
                System.out.print(repo);
                System.out.print("/tree/"); //$NON-NLS-1$
                System.out.print(sourceFolder);
                System.out.print("/"); //$NON-NLS-1$
                System.out.print(packageName.replace('.', '/'));
                System.out.print("/"); //$NON-NLS-1$
                System.out.print(className);
                System.out.print(".java"); //$NON-NLS-1$
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
    if (!line.startsWith("test/") && !line.startsWith("tests/") && !line.endsWith("package-info.java")) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    {
      rootPackageIndex = line.indexOf("/org/"); //$NON-NLS-1$
      if (rootPackageIndex == -1)
      {
        rootPackageIndex = line.indexOf("/com/"); //$NON-NLS-1$
        if (rootPackageIndex == -1)
        {
          rootPackageIndex = line.indexOf("/javax/"); //$NON-NLS-1$
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
        sourceFolders = new TreeMap<>();
        repositoryIndex.put(repo, sourceFolders);
      }

      Map<String, Set<String>> packages = sourceFolders.get(sourceFolder);
      if (packages == null)
      {
        packages = new TreeMap<>();
        sourceFolders.put(sourceFolder, packages);
      }

      Set<String> classes = packages.get(packageName);
      if (classes == null)
      {
        classes = new TreeSet<>();
        packages.put(packageName, classes);
      }

      classes.add(className);
    }
  }

  private static final Pattern REPO_FULL_NAME_PATTERN = Pattern.compile("\"full_name\":\"([^\"]+)\""); //$NON-NLS-1$

  private static final Pattern MIRROR_URL_PATTERN = Pattern.compile("\"mirror_url\":(\"(|[^\"]+)\"|null)"); //$NON-NLS-1$

  private static Set<String> getGithubEclipseRepositoryIndex() throws Exception
  {
    Map<String, String> repos = new TreeMap<>();
    for (int i = 1; i < 500; ++i)
    {
      URL url = new URL("https://api.github.com/users/eclipse/repos?page=" + i); //$NON-NLS-1$
      List<String> lines = readLines(url, "UTF-8"); //$NON-NLS-1$
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
        System.out.println("\"" + entry.getKey() + "\", //"); //$NON-NLS-1$ //$NON-NLS-2$
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
    URL url = new URL("https://github.com/" + repo + "/tree/master/" + path); //$NON-NLS-1$ //$NON-NLS-2$
    List<String> lines = readLines(url, "UTF-8"); //$NON-NLS-1$
    Pattern folderPattern = Pattern.compile("href=\"/" + repo + "/tree/master/([^\"]+)\""); //$NON-NLS-1$ //$NON-NLS-2$
    Pattern filePattern = Pattern.compile("href=\"/" + repo + "/blob/master/([^\"]+)\""); //$NON-NLS-1$ //$NON-NLS-2$
    for (String line : lines)
    {
      Matcher matcher = folderPattern.matcher(line);
      if (matcher.find())
      {
        String folder = matcher.group(1);
        int index = folder.lastIndexOf('/');
        String folderName = index == -1 ? folder : folder.substring(index + 1);
        if (line.indexOf(">" + folderName + "</a>") != -1) //$NON-NLS-1$ //$NON-NLS-2$
        {
          System.out.println(repo + "/" + folder); //$NON-NLS-1$
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
          if (file.endsWith(".java")) //$NON-NLS-1$
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
        List<String> lines = readLines(inputStream, "UTF-8"); //$NON-NLS-1$
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
    List<String> lines = new ArrayList<>();

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
