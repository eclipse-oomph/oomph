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
package org.eclipse.oomph.setup.sync;

import org.eclipse.oomph.setup.sync.util.Client;
import org.eclipse.oomph.setup.sync.util.RemoteSnapshot;
import org.eclipse.oomph.util.PropertiesUtil;

import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;

import java.io.File;
import java.net.URI;

/**
 * @author Eike Stepper
 */
class Test
{
  private static final File SYNC_FOLDER = new File(PropertiesUtil.getUserHome(), ".eclipse/org.eclipse.oomph.setup.sync");

  public static void main(String[] args) throws Exception
  {
    URI serviceURI = new URI("http://localhost:8765/protected/service.php");
    Credentials credentials = new UsernamePasswordCredentials("stepper", "123");

    Client client = new Client(serviceURI, credentials);
    RemoteSnapshot remoteSnapshot = new RemoteSnapshot(client, SYNC_FOLDER);

    File workingCopy = remoteSnapshot.getWorkingCopy(true);
    edit(workingCopy);

    boolean committed = remoteSnapshot.commit();
    System.out.println(committed);
  }

  private static void edit(File file)
  {
    file.setLastModified(System.currentTimeMillis());
  }
}
