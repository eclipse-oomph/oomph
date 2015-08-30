/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.sync.tests;

import static org.hamcrest.MatcherAssert.assertThat;

import org.eclipse.oomph.setup.internal.sync.SyncUtil;
import org.eclipse.oomph.tests.AbstractTest;

import org.eclipse.emf.ecore.resource.ResourceSet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SynchronizerTests extends AbstractTest
{
  public static final ResourceSet RESOURCE_SET = SyncUtil.createResourceSet();

  private final Map<Integer, TestWorkstation> workstations = new HashMap<Integer, TestWorkstation>();

  private TestWorkstation getWorkstation(int id)
  {
    TestWorkstation workstation = workstations.get(id);
    if (workstation == null)
    {
      workstation = new TestWorkstation(id);
      workstations.put(id, workstation);
    }

    return workstation;
  }

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    RESOURCE_SET.getResources().clear();
    TestServer.getRemoteDataProvider().delete();
  }

  @Test
  public void test000() throws Exception
  {
    TestWorkstation workstation1 = getWorkstation(1);
    workstation1.synchronize().commit();
    assertThat(workstation1.getPreferenceTasks().size(), is(0));
  }

  @Test
  public void test001_Set1_Sync1() throws Exception
  {
    TestWorkstation workstation1 = getWorkstation(1);
    workstation1.setPreference("line.numbers", "true");
    workstation1.saveUser();
    workstation1.synchronize().commit();
    assertThat(workstation1.getPreferenceTasks().size(), is(1));
    assertThat(workstation1.getPreference("line.numbers"), is("true"));
  }

  @Test
  public void test002_Set1_Sync1_Sync2() throws Exception
  {
    test001_Set1_Sync1();

    TestWorkstation workstation2 = getWorkstation(2);
    workstation2.synchronize().commit();
    assertThat(workstation2.getPreferenceTasks().size(), is(1));
    assertThat(workstation2.getPreference("line.numbers"), is("true"));

    TestWorkstation workstation1 = getWorkstation(1);
    assertThat(workstation1.getPreferenceTasks().size(), is(1));
    assertThat(workstation1.getPreference("line.numbers"), is("true"));
  }

  @Test
  public void test002_Set1_Sync1_Sync2_Sync1() throws Exception
  {
    test002_Set1_Sync1_Sync2();

    TestWorkstation workstation1 = getWorkstation(1);
    workstation1.synchronize().commit();
    assertThat(workstation1.getPreferenceTasks().size(), is(1));
    assertThat(workstation1.getPreference("line.numbers"), is("true"));
  }

  @Test
  public void test003_Set1_Sync1_Set2_Sync2() throws Exception
  {
    test001_Set1_Sync1();

    TestWorkstation workstation2 = getWorkstation(2);
    workstation2.setPreference("refresh.resources", "true");
    workstation2.saveUser();
    workstation2.synchronize().commit();
    assertThat(workstation2.getPreferenceTasks().size(), is(2));
    assertThat(workstation2.getPreference("line.numbers"), is("true"));
    assertThat(workstation2.getPreference("refresh.resources"), is("true"));

    TestWorkstation workstation1 = getWorkstation(1);
    assertThat(workstation1.getPreferenceTasks().size(), is(1));
    assertThat(workstation1.getPreference("line.numbers"), is("true"));
  }

  @Test
  public void test003_Set1_Sync1_Set2_Sync2_Sync1() throws Exception
  {
    test003_Set1_Sync1_Set2_Sync2();

    TestWorkstation workstation1 = getWorkstation(1);
    workstation1.synchronize().commit();
    assertThat(workstation1.getPreferenceTasks().size(), is(2));
    assertThat(workstation1.getPreference("line.numbers"), is("true"));
    assertThat(workstation1.getPreference("refresh.resources"), is("true"));
  }
}
