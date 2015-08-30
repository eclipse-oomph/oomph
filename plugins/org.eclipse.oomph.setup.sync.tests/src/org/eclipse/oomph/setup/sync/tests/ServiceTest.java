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

import org.eclipse.oomph.setup.internal.sync.RemoteDataProvider;
import org.eclipse.oomph.util.IOUtil;

import java.io.File;

/**
 * @author Eike Stepper
 */
class ServiceTest
{
  private static final File FILE = new File("C:/Users/Stepper/AppData/Local/Temp/test-user-1/local.txt");

  public static void main(String[] args) throws Exception
  {
    IOUtil.writeUTF8(FILE, "hello 1");

    RemoteDataProvider dataProvider = TestServer.getRemoteDataProvider();
    dataProvider.post(FILE, null);

    // dataProvider.update(FILE);

    // Snapshot remoteSnapshot = new Snapshot(dataProvider, FILE);
    //
    // WorkingCopy workingCopy = remoteSnapshot.createWorkingCopy();
    // edit(workingCopy.getTmpFile());
    // workingCopy.commit();
  }
}
