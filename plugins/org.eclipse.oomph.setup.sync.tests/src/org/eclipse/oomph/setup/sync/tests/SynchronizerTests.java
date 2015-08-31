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

import org.eclipse.oomph.setup.internal.sync.DataProvider.NotCurrentException;
import org.eclipse.oomph.setup.internal.sync.Synchronization.ConflictException;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.tests.TestWorkstation.FailureHandler;
import org.eclipse.oomph.setup.sync.tests.TestWorkstation.FailureHandler.Expect;
import org.eclipse.oomph.setup.sync.tests.TestWorkstation.TestSynchronization;
import org.eclipse.oomph.tests.AbstractTest;

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
  private final Map<Integer, TestWorkstation> workstations = new HashMap<Integer, TestWorkstation>();

  private TestWorkstation WS(int id) throws Exception
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
    TestServer.getRemoteDataProvider().delete();
  }

  @Test
  public void test000() throws Exception
  {
    WS(1).synchronize().commitAnd().assertCount(0);
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
    TestSynchronization sync1 = WS(1).set("line.numbers", "true").save().synchronize();

    WS(2).set("line.numbers", "true").save().commit().assertCount(1).assertSet("line.numbers", "true");

    sync1.commitFail(new FailureHandler()
    {
      public void handleFailure(Exception t) throws Exception
      {
        WS(1).commit().assertCount(1).assertSet("line.numbers", "true");
      }
    });

    WS(2).commit().assertCount(1).assertSet("line.numbers", "true");
  }

  @Test
  public void test007_SameKey_ConflictException() throws Exception
  {
    TestSynchronization sync1 = WS(1).set("line.numbers", "true").save().synchronize();

    WS(2).set("line.numbers", "false").save().commit().assertCount(1).assertSet("line.numbers", "false");

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
  public void test008_SameKey_ConflictPick1() throws Exception
  {
    WS(1).set("line.numbers", "true").save();

    WS(2).set("line.numbers", "false").save().commit().assertCount(1).assertSet("line.numbers", "false");

    WS(1).synchronize().resolvePreference("line.numbers", SyncActionType.SET_LOCAL).commitAnd().assertCount(1).assertSet("line.numbers", "true");

    WS(2).commit().assertCount(1).assertSet("line.numbers", "true");
  }

  @Test
  public void test009_SameKey_ConflictPick2() throws Exception
  {
    WS(1).set("line.numbers", "true").save();

    WS(2).set("line.numbers", "false").save().commit().assertCount(1).assertSet("line.numbers", "false");

    WS(1).synchronize().resolvePreference("line.numbers", SyncActionType.SET_REMOTE).commitAnd().assertCount(1).assertSet("line.numbers", "false");

    WS(2).commit().assertCount(1).assertSet("line.numbers", "false");
  }
}
