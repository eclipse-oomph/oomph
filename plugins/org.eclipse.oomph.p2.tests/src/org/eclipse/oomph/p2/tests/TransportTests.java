/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.oomph.internal.util.HTTPServer;
import org.eclipse.oomph.internal.util.HTTPServer.Context;
import org.eclipse.oomph.internal.util.HTTPServer.FileContext;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.internal.core.CachingTransport;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
@FixMethodOrder(MethodSorters.JVM)
public class TransportTests extends AbstractP2Test
{
  private static final boolean ASSERT_REQUESTS = true;

  private static HTTPServer httpServer;

  private List<Context> contexts = new ArrayList<Context>();

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    httpServer = new HTTPServer(2345, 5000);
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    if (httpServer != null)
    {
      httpServer.stop();
      httpServer = null;
    }
  }

  @Override
  public void tearDown() throws Exception
  {
    for (Context context : contexts)
    {
      httpServer.removeContext(context);
    }

    super.tearDown();
  }

  private FileContext addRepo(String path, File folder, boolean composite, boolean p2Index) throws Exception
  {
    if (composite)
    {
      IOUtil.copyTree(CDO_NEW, new File(folder, "child"));

      IOUtil.writeUTF8(new File(folder, "compositeContent.xml"), "<?xml version='1.0' encoding='UTF-8'?>\n" //
          + "<?compositeMetadataRepository version='1.0.0'?>\n" //
          + "<repository name='Eclipse Mars repository' type='org.eclipse.equinox.internal.p2.metadata.repository.CompositeMetadataRepository' version='1.0.0'>\n" //
          + "    <properties size='3'>\n" //
          + "        <property name='p2.timestamp' value='1313779613760'/>\n" //
          + "        <property name='p2.atomic.composite.loading' value='true'/>\n" //
          + "    </properties>\n" //
          + "    <children size='1'>\n" //
          + "        <child location='child' />\n" //
          + "    </children>    \n" //
          + "</repository>\n");

      IOUtil.writeUTF8(new File(folder, "compositeArtifacts.xml"), "<?xml version='1.0' encoding='UTF-8'?>\n" //
          + "<?compositeArtifactRepository version='1.0.0'?>\n" //
          + "<repository name='Eclipse Mars repository' type='org.eclipse.equinox.internal.p2.artifact.repository.CompositeArtifactRepository' version='1.0.0'>\n" //
          + "    <properties size='3'>\n" //
          + "        <property name='p2.timestamp' value='1313779613118'/>\n" //
          + "        <property name='p2.atomic.composite.loading' value='true'/>\n" //
          + "    </properties>\n" //
          + "    <children size='1'>\n" //
          + "        <child location='child' />\n" //
          + "    </children>\n" //
          + "</repository>\n");

      if (p2Index)
      {
        IOUtil.writeUTF8(new File(folder, "p2.index"),
            "version=1\n" //
                + "metadata.repository.factory.order=compositeContent.xml,\\!\n" //
                + "artifact.repository.factory.order=compositeArtifacts.xml,\\!\n");
      }
    }
    else
    {
      IOUtil.copyTree(CDO_NEW, folder);

      if (p2Index)
      {
        IOUtil.writeUTF8(new File(folder, "p2.index"),
            "version=1\n" //
                + "metadata.repository.factory.order=content.xml,\\!\n" //
                + "artifact.repository.factory.order=artifacts.xml,\\!\n");
      }
    }

    FileContext context = new FileContext(path, false, folder);
    httpServer.addContext(context);
    contexts.add(context);
    return context;
  }

  private void removeRepo(FileContext context)
  {
    httpServer.removeContext(context);
    IOUtil.deleteBestEffort(context.getRoot());
  }

  private String removePort(String request)
  {
    return request.replace(":" + httpServer.getPort() + "/", "/");
  }

  private List<String> install(int installation) throws Exception
  {
    String url = httpServer + "/cdo";
    File installFolder = new File(getUserHome(), "app" + installation);

    AgentManager agentManager = P2Util.getAgentManager();
    Agent agent = agentManager.addAgent(new File(installFolder, "p2"));

    CachingTransport cachingTransport = (CachingTransport)agent.getProvisioningAgent().getService(CachingTransport.SERVICE_NAME);

    CountingTransport countingTransport = new CountingTransport(cachingTransport.getDelegate());
    cachingTransport.setDelegate(countingTransport);

    Profile profile = agent.addProfile("profile-app" + installation, "Installation").setCacheFolder(installFolder).setInstallFolder(installFolder).create();
    ProfileTransaction transaction = profile.change();
    transaction.setMirrors(false);

    ProfileDefinition profileDefinition = transaction.getProfileDefinition();
    profileDefinition.getRequirements().add(P2Factory.eINSTANCE.createRequirement("org.eclipse.net4j.util"));
    profileDefinition.getRepositories().add(P2Factory.eINSTANCE.createRepository(url));

    commitProfileTransaction(transaction, true, new NullProgressMonitor());
    assertThat(installFolder.isDirectory(), is(true));
    assertThat(installFolder.list().length, is(3));
    assertThat(new File(installFolder, "p2").isDirectory(), is(true));
    assertThat(new File(installFolder, "artifacts.xml").isFile(), is(true));

    File plugins = new File(installFolder, "plugins");
    assertThat(plugins.isDirectory(), is(true));
    assertThat(plugins.list().length, is(1));

    List<String> results = new ArrayList<String>();
    results.addAll(countingTransport.getRequests());

    File cacheFile = cachingTransport.getCacheFile(new URI(url + "/p2.index"));
    Map<String, String> p2Index = PropertiesUtil.loadProperties(cacheFile);
    for (Map.Entry<String, String> entry : p2Index.entrySet())
    {
      results.add("     " + entry.getKey() + "=" + entry.getValue());
    }

    return results;
  }

  private List<String> test(boolean startWithComposite, boolean startWithP2Index, boolean endWithP2Index) throws Exception
  {
    List<String> results = new ArrayList<String>();

    File root = new File(getUserHome(), "cdo");
    System.out.println(root);
    System.out.println();

    FileContext context1 = addRepo("/cdo", root, startWithComposite, startWithP2Index);

    try
    {
      results.add("INSTALL 1");
      results.addAll(install(1));
    }
    finally
    {
      removeRepo(context1);
    }

    FileContext context2 = addRepo("/cdo", root, !startWithComposite, endWithP2Index);

    try
    {
      results.add("INSTALL 2");
      results.addAll(install(2));
    }
    finally
    {
      removeRepo(context2);
    }

    return results;
  }

  private void assertResults(List<String> results, String... expectedResults)
  {
    System.out.println("assertResults(results //");
    int i = 0;
    boolean failed = false;

    for (String result : results)
    {
      result = removePort(result);
      System.out.print(", \"" + result + "\" //");

      if (!ASSERT_REQUESTS)
      {
        System.out.println();
        continue;
      }

      int index = i++;
      if (index >= expectedResults.length)
      {
        System.out.println(" -------------------> UNEXPECTED");
        failed = true;
      }
      else
      {
        String expectedResult = removePort(expectedResults[index]);
        if (result.equals(expectedResult))
        {
          System.out.println();
        }
        else
        {
          System.out.println(" ------------------->  " + expectedResult);
          failed = true;
        }
      }
    }

    System.out.println(");\n\n");

    if (ASSERT_REQUESTS)
    {
      for (int j = i; j < expectedResults.length; j++)
      {
        System.out.println("MISSING: " + expectedResults[j]);
        failed = true;
      }

      if (failed)
      {
        throw new AssertionError("Unexpected Results");
      }
    }
  }

  @Test
  public void test_1_CompositeToSimpleWithoutIndex() throws Exception
  {
    List<String> results = test(true, false, false);
    assertResults(results //
        , "INSTALL 1" //
        , "GET  http://127.0.0.1/cdo/p2.index" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/content.xml" //
        , "HEAD http://127.0.0.1/cdo/content.xml.xz" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/child/p2.index" //
        , "HEAD http://127.0.0.1/cdo/child/content.jar" //
        , "GET  http://127.0.0.1/cdo/child/content.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/artifacts.xml.xz" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "GET  http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=compositeContent.xml" //
        , "     artifact.repository.factory.order=compositeArtifacts.xml" //
        , "INSTALL 2" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.xml" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "GET  http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=content.xml" //
        , "     artifact.repository.factory.order=artifacts.xml" //
    );
  }

  @Test
  public void test_2_CompositeToSimpleRemoveIndex() throws Exception
  {
    List<String> results = test(true, true, false);
    assertResults(results //
        , "INSTALL 1" //
        , "GET  http://127.0.0.1/cdo/p2.index" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/child/p2.index" //
        , "HEAD http://127.0.0.1/cdo/child/content.jar" //
        , "GET  http://127.0.0.1/cdo/child/content.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "GET  http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=compositeContent.xml" //
        , "     artifact.repository.factory.order=compositeArtifacts.xml" //
        , "INSTALL 2" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.xml" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "GET  http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=content.xml" //
        , "     artifact.repository.factory.order=artifacts.xml" //
    );
  }

  @Test
  public void test_3_CompositeToSimpleAddIndex() throws Exception
  {
    List<String> results = test(true, false, true);
    assertResults(results //
        , "INSTALL 1" //
        , "GET  http://127.0.0.1/cdo/p2.index" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/content.xml" //
        , "HEAD http://127.0.0.1/cdo/content.xml.xz" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/child/p2.index" //
        , "HEAD http://127.0.0.1/cdo/child/content.jar" //
        , "GET  http://127.0.0.1/cdo/child/content.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/artifacts.xml.xz" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "GET  http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=compositeContent.xml" //
        , "     artifact.repository.factory.order=compositeArtifacts.xml" //
        , "INSTALL 2" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.xml" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "GET  http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=content.xml" //
        , "     artifact.repository.factory.order=artifacts.xml" //
    );
  }

  @Test
  public void test_4_CompositeToSimpleWithIndex() throws Exception
  {
    List<String> results = test(true, true, true);
    assertResults(results //
        , "INSTALL 1" //
        , "GET  http://127.0.0.1/cdo/p2.index" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/child/p2.index" //
        , "HEAD http://127.0.0.1/cdo/child/content.jar" //
        , "GET  http://127.0.0.1/cdo/child/content.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "GET  http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=compositeContent.xml" //
        , "     artifact.repository.factory.order=compositeArtifacts.xml" //
        , "INSTALL 2" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.xml" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "GET  http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=content.xml" //
        , "     artifact.repository.factory.order=artifacts.xml" //
    );
  }

  @Test
  public void test_5_SimpleToCompositeWithoutIndex() throws Exception
  {
    List<String> results = test(false, false, false);
    assertResults(results //
        , "INSTALL 1" //
        , "GET  http://127.0.0.1/cdo/p2.index" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "GET  http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=content.xml" //
        , "     artifact.repository.factory.order=artifacts.xml" //
        , "INSTALL 2" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/content.xml" //
        , "HEAD http://127.0.0.1/cdo/content.xml.xz" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/child/p2.index" //
        , "HEAD http://127.0.0.1/cdo/child/content.jar" //
        , "GET  http://127.0.0.1/cdo/child/content.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/artifacts.xml.xz" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "GET  http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=compositeContent.xml" //
        , "     artifact.repository.factory.order=compositeArtifacts.xml" //
    );
  }

  @Test
  public void test_6_SimpleToCompositeRemoveIndex() throws Exception
  {
    List<String> results = test(false, true, false);
    assertResults(results //
        , "INSTALL 1" //
        , "GET  http://127.0.0.1/cdo/p2.index" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "GET  http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=content.xml" //
        , "     artifact.repository.factory.order=artifacts.xml" //
        , "INSTALL 2" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/content.xml" //
        , "HEAD http://127.0.0.1/cdo/content.xml.xz" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/child/p2.index" //
        , "HEAD http://127.0.0.1/cdo/child/content.jar" //
        , "GET  http://127.0.0.1/cdo/child/content.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/artifacts.xml.xz" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "GET  http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=compositeContent.xml" //
        , "     artifact.repository.factory.order=compositeArtifacts.xml" //
    );
  }

  @Test
  public void test_7_SimpleToCompositeAddIndex() throws Exception
  {
    List<String> results = test(false, false, true);
    assertResults(results //
        , "INSTALL 1" //
        , "GET  http://127.0.0.1/cdo/p2.index" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "GET  http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=content.xml" //
        , "     artifact.repository.factory.order=artifacts.xml" //
        , "INSTALL 2" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/content.xml" //
        , "HEAD http://127.0.0.1/cdo/content.xml.xz" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/child/p2.index" //
        , "HEAD http://127.0.0.1/cdo/child/content.jar" //
        , "GET  http://127.0.0.1/cdo/child/content.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/artifacts.xml.xz" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "GET  http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=compositeContent.xml" //
        , "     artifact.repository.factory.order=compositeArtifacts.xml" //
    );
  }

  @Test
  public void test_8_SimpleToCompositeWithIndex() throws Exception
  {
    List<String> results = test(false, true, true);
    assertResults(results //
        , "INSTALL 1" //
        , "GET  http://127.0.0.1/cdo/p2.index" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "GET  http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=content.xml" //
        , "     artifact.repository.factory.order=artifacts.xml" //
        , "INSTALL 2" //
        , "HEAD http://127.0.0.1/cdo/content.jar" //
        , "HEAD http://127.0.0.1/cdo/content.xml" //
        , "HEAD http://127.0.0.1/cdo/content.xml.xz" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/compositeContent.xml" //
        , "GET  http://127.0.0.1/cdo/child/p2.index" //
        , "HEAD http://127.0.0.1/cdo/child/content.jar" //
        , "GET  http://127.0.0.1/cdo/child/content.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/artifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/artifacts.xml.xz" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.jar" //
        , "HEAD http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "GET  http://127.0.0.1/cdo/compositeArtifacts.xml" //
        , "HEAD http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/artifacts.jar" //
        , "GET  http://127.0.0.1/cdo/child/plugins/org.eclipse.net4j.util_3.3.1.v20140218-1709.jar" //
        , "     version=1" //
        , "     metadata.repository.factory.order=compositeContent.xml" //
        , "     artifact.repository.factory.order=compositeArtifacts.xml" //
    );
  }

  /**
   * @author Eike Stepper
   */
  @SuppressWarnings("restriction")
  private static final class CountingTransport extends org.eclipse.equinox.internal.p2.repository.Transport
  {
    private final org.eclipse.equinox.internal.p2.repository.Transport delegate;

    private final List<String> requests = new ArrayList<String>();

    private CountingTransport(org.eclipse.equinox.internal.p2.repository.Transport delegate)
    {
      this.delegate = delegate;
    }

    public List<String> getRequests()
    {
      return requests;
    }

    @Override
    public IStatus download(URI uri, OutputStream target, long startPos, IProgressMonitor monitor)
    {
      requests.add("GET  " + uri);
      return delegate.download(uri, target, startPos, monitor);
    }

    @Override
    public IStatus download(URI uri, OutputStream target, IProgressMonitor monitor)
    {
      requests.add("GET  " + uri);
      return delegate.download(uri, target, monitor);
    }

    @Override
    public InputStream stream(URI uri, IProgressMonitor monitor)
        throws FileNotFoundException, CoreException, org.eclipse.equinox.internal.p2.repository.AuthenticationFailedException
    {
      requests.add("GET  " + uri);
      return delegate.stream(uri, monitor);
    }

    @Override
    public long getLastModified(URI uri, IProgressMonitor monitor)
        throws CoreException, FileNotFoundException, org.eclipse.equinox.internal.p2.repository.AuthenticationFailedException
    {
      requests.add("HEAD " + uri);
      return delegate.getLastModified(uri, monitor);
    }

    @Override
    public boolean equals(Object obj)
    {
      return delegate.equals(obj);
    }

    @Override
    public int hashCode()
    {
      return delegate.hashCode();
    }

    @Override
    public String toString()
    {
      return requests.toString();
    }
  }
}
