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
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileCreator;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Eike Stepper
 */
@Deprecated
public class TestInstallAction implements IWorkbenchWindowActionDelegate
{
  private IWorkbenchWindow window;

  public TestInstallAction()
  {
  }

  public void init(IWorkbenchWindow window)
  {
    this.window = window;
  }

  public void dispose()
  {
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
  }

  public void run(IAction action)
  {
    File base = new File("/develop/test-install");
    for (int i = 2; i < 1000; i++)
    {
      File installFolder = new File(base, "eclipse" + i);
      if (!installFolder.exists())
      {
        install(installFolder);
        break;
      }
    }
  }

  private void install(File installFolder)
  {
    File agentFolder = new File(installFolder, "p2");
    Agent agent = P2Util.createAgent(agentFolder);
    BundlePool bundlePool = agent.addBundlePool(installFolder);

    ProfileCreator creator = bundlePool.addProfile("SDKProfile", Profile.TYPE_INSTALLATION).setInstallFeatures(true).setInstallFolder(installFolder);
    Profile profile = creator.create();

    final ProfileTransaction transaction = profile.change();
    ProfileDefinition profileDefinition = transaction.getProfileDefinition();
    profileDefinition.getRequirements().add(P2Factory.eINSTANCE.createRequirement("org.eclipse.sdk.ide"));
    profileDefinition.getRepositories().add(P2Factory.eINSTANCE.createRepository("http://download.eclipse.org/releases/luna"));

    try
    {
      UIUtil.runInProgressDialog(window.getShell(), new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          try
          {
            transaction.commit(null, monitor);
          }
          catch (CoreException ex)
          {
            throw new InvocationTargetException(ex);
          }
        }
      });
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }
}
