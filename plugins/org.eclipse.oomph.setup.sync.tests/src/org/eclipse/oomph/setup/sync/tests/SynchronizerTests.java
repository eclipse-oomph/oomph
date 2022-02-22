/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.sync.tests;

import org.eclipse.oomph.setup.internal.sync.DataProvider.NotCurrentException;
import org.eclipse.oomph.setup.internal.sync.RemoteDataProvider;
import org.eclipse.oomph.setup.internal.sync.Synchronization.ConflictException;
import org.eclipse.oomph.setup.sync.tests.TestWorkstation.FailureHandler;
import org.eclipse.oomph.setup.sync.tests.TestWorkstation.FailureHandler.Expect;
import org.eclipse.oomph.setup.sync.tests.TestWorkstation.TestSynchronization;
import org.eclipse.oomph.tests.AbstractTest;

import org.eclipse.userstorage.tests.util.ServerFixture;

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
  private final Map<Integer, TestWorkstation> workstations = new HashMap<>();

  private ServerFixture serverFixture;

  private TestWorkstation WS(int id) throws Exception
  {
    TestWorkstation workstation = workstations.get(id);
    if (workstation == null)
    {
      workstation = new TestWorkstation(serverFixture, workstations, id);
      workstations.put(id, workstation);
    }

    return workstation;
  }

  @Override
  public void setUp() throws Exception
  {
    super.setUp();
    workstations.clear();
    serverFixture = new ServerFixture(RemoteDataProvider.APPLICATION_TOKEN);
  }

  @Override
  public void tearDown() throws Exception
  {
    serverFixture.dispose();
    super.tearDown();
  }

  @Test
  public void test000() throws Exception
  {
    WS(1).commit().assertCount(0);
  }

  @Test
  public void test001_Set1_Sync1() throws Exception
  {
    WS(1).set("line.numbers", "true").save().commit().assertCount(1).assertSet("line.numbers", "true");
  }

  @Test
  public void test002_Set1_Sync1_Sync2() throws Exception
  {
    WS(1).set("line.numbers", "true").save().commit();

    WS(2).commit().assertCount(1).assertSet("line.numbers", "true");
  }

  @Test
  public void test003_Set1_Sync1_Sync2_Sync1() throws Exception
  {
    WS(1).set("line.numbers", "true").save().commit();

    WS(2).commit().assertCount(1).assertSet("line.numbers", "true");

    WS(1).commit().assertCount(1).assertSet("line.numbers", "true");
  }

  @Test
  public void test004_Set1_Sync1_Set2_Sync2() throws Exception
  {
    WS(1).set("line.numbers", "true").save().commit();

    WS(2).set("refresh.resources", "true").save().commit().assertCount(2).assertSet("line.numbers", "true").assertSet("refresh.resources", "true");
  }

  @Test
  public void test005_Set1_Sync1_Set2_Sync2_Sync1() throws Exception
  {
    WS(1).set("line.numbers", "true").save().commit();

    WS(2).set("refresh.resources", "true").save().commit().assertCount(2).assertSet("line.numbers", "true").assertSet("refresh.resources", "true");

    WS(1).commit().assertCount(2).assertSet("line.numbers", "true").assertSet("refresh.resources", "true");
  }

  @Test
  public void test006_SameKey_SameValue() throws Exception
  {
    TestSynchronization sync1 = WS(1).set("property", "value").save().synchronize();

    WS(2).set("property", "value").save().commit().assertCount(1).assertSet("property", "value");

    sync1.commitFail(new FailureHandler()
    {
      @Override
      public void handleFailure(Exception t) throws Exception
      {
        WS(1).commit().assertCount(1).assertSet("property", "value");
      }
    });

    WS(2).commit().assertCount(1).assertSet("property", "value");
  }

  @Test
  public void test007_SameKey_Conflict_Sync() throws Exception
  {
    TestSynchronization sync1 = WS(1).set("property", "value1").save().synchronize();

    WS(2).set("property", "value2").save().commit().assertCount(1).assertSet("property", "value2");

    sync1.commitFail(new Expect(NotCurrentException.class)
    {
      @Override
      protected void handleFailure() throws Exception
      {
        WS(1).commitFail(new Expect(ConflictException.class));
      }
    });
  }

  @Test
  public void test008_SameKey_Conflict_Pick1() throws Exception
  {
    WS(1).set("property", "value1").save();

    WS(2).set("property", "value2").save().commit().assertCount(1).assertSet("property", "value2");

    WS(1).synchronize().pickLocal("property").commitAnd().assertCount(1).assertSet("property", "value1");

    WS(2).commit().assertCount(1).assertSet("property", "value1");
  }

  @Test
  public void test009_SameKey_Conflict_Pick2() throws Exception
  {
    WS(1).set("property", "value1").save();

    WS(2).set("property", "value2").save().commit().assertCount(1).assertSet("property", "value2");

    WS(1).synchronize().pickRemote("property").commitAnd().assertCount(1).assertSet("property", "value2");

    WS(2).commit().assertCount(1).assertSet("property", "value2");
  }

  @Test
  public void test010_Remove1_Sync1() throws Exception
  {
    WS(1).set("line.numbers", "true").set("refresh.resources", "true").save().commit() //
        .assertCount(2).assertSet("line.numbers", "true").assertSet("refresh.resources", "true");

    WS(1).remove("line.numbers").save().commit().assertCount(1).assertSet("refresh.resources", "true");
    WS(1).remove("refresh.resources").save().commit().assertCount(0);
  }

  @Test
  public void test011_Remove1_Sync1_Sync2() throws Exception
  {
    WS(1).set("line.numbers", "true").set("refresh.resources", "true").save().commit();

    WS(2).commit().assertCount(2).assertSet("line.numbers", "true").assertSet("refresh.resources", "true");

    WS(1).remove("line.numbers").save().commit().assertCount(1).assertSet("refresh.resources", "true");

    WS(2).commit().assertCount(1).assertSet("refresh.resources", "true");

    WS(1).remove("refresh.resources").save().commit().assertCount(0);

    WS(2).commit().assertCount(0);
  }

  @Test
  public void test012_Remove1_Remove2_Sync1_Sync2() throws Exception
  {
    WS(1).set("line.numbers", "true").set("refresh.resources", "true").save().commit();

    WS(2).commit().assertCount(2).assertSet("line.numbers", "true").assertSet("refresh.resources", "true");

    WS(1).remove("line.numbers").save().commit().assertCount(1).assertSet("refresh.resources", "true");

    WS(2).remove("line.numbers").save().commit().assertCount(1).assertSet("refresh.resources", "true");
  }

  @Test
  public void test013_Exclude() throws Exception
  {
    WS(1).set("line.numbers", "true").save().commit();

    WS(2).commit().assertCount(1);
    WS(2).set("refresh.resources", "true").save().synchronize().exclude("refresh.resources").commitAnd() //
        .assertCount(2).assertSet("line.numbers", "true").assertSet("refresh.resources", "true"); //
    WS(2).assertExcluded("refresh.resources");

    WS(1).commit().assertCount(1).assertSet("line.numbers", "true").assertExcluded("refresh.resources");
    WS(1).set("refresh.resources", "true").save().commit() //
        .assertCount(2).assertSet("line.numbers", "true").assertSet("refresh.resources", "true") //
        .assertExcluded("refresh.resources");
  }
}
