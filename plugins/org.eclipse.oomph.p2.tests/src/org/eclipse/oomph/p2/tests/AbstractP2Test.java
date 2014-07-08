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
package org.eclipse.oomph.p2.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.P2Util.VersionedIdFilter;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.internal.core.AgentManagerImpl;
import org.eclipse.oomph.tests.AbstractTest;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.p2.metadata.IVersionedId;

import org.junit.BeforeClass;

import java.io.File;
import java.net.URI;

/**
 * @author Eike Stepper
 */
public abstract class AbstractP2Test extends AbstractTest
{
  private static final String TMP = PropertiesUtil.getProperty("java.io.tmpdir");

  private static final String CDO = "p2-test-mirror-001-cdo";

  private static final String PLATFORM = "p2-test-mirror-001-platform";

  private static final VersionedIdFilter CDO_FILTER = new VersionedIdFilter()
  {
    public boolean matches(IVersionedId versionedId)
    {
      String id = versionedId.getId();
      return id.startsWith("org.eclipse.net4j.util") || id.startsWith("org.apache");
    }
  };

  private static final VersionedIdFilter PLATFORM_FILTER = new VersionedIdFilter()
  {
    public boolean matches(IVersionedId versionedId)
    {
      String id = versionedId.getId();
      return id.startsWith("com.jcraft.jsch") || id.startsWith("org.apache") || id.startsWith("a.jre");
    }
  };

  public static final File CDO_OLD = new File(TMP, CDO + "-old");

  public static final File CDO_NEW = new File(TMP, CDO + "-new");

  public static final File PLATFORM_OLD = new File(TMP, PLATFORM + "-old");

  public static final File PLATFORM_NEW = new File(TMP, PLATFORM + "-new");

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    mirror("http://download.eclipse.org/modeling/emf/cdo/drops/R20130918-0029", CDO_OLD, CDO_FILTER);
    mirror("http://download.eclipse.org/modeling/emf/cdo/drops/R20140218-1655", CDO_NEW, CDO_FILTER);
    mirror("http://download.eclipse.org/eclipse/updates/4.3/R-4.3.1-201309111000", PLATFORM_OLD, PLATFORM_FILTER);
    mirror("http://download.eclipse.org/eclipse/updates/4.3/R-4.3.2-201402211700", PLATFORM_NEW, PLATFORM_FILTER);
  }

  private static void mirror(String repo, File local, VersionedIdFilter filter) throws Exception
  {
    if (!local.isDirectory())
    {
      LOGGER.setTaskName("Creating test mirror of " + repo + " under " + local);
      P2Util.mirrorRepository(new URI(repo), local.toURI(), filter, LOGGER);
      LOGGER.setTaskName(null);
    }
  }

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    AgentManagerImpl.instance = new AgentManagerImpl(getUserHome());
  }

  @Override
  public void tearDown() throws Exception
  {
    AgentManagerImpl.instance = null;
    super.tearDown();
  }

  protected Agent getAgent()
  {
    AgentManager agentManager = P2Util.getAgentManager();
    return agentManager.getAgents().iterator().next();
  }

  protected Agent getFreshAgent()
  {
    AgentManagerImpl.instance = new AgentManagerImpl(getUserHome());
    return getAgent();
  }

  protected void commitProfileTransaction(ProfileTransaction transaction, boolean expectedChange) throws CoreException
  {
    boolean actualChange = transaction.commit(LOGGER);
    assertThat(actualChange, is(expectedChange));
  }
}
