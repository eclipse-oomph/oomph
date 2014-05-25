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
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.internal.setup.core.SetupCorePlugin;
import org.eclipse.oomph.util.IRunnable;
import org.eclipse.oomph.util.UserCallback;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Eike Stepper
 */
public final class UpdateUtil extends Plugin
{
  public static final IStatus UPDATE_FOUND_STATUS = new Status(IStatus.OK, SetupCorePlugin.INSTANCE.getSymbolicName(), "Updates found");

  private UpdateUtil()
  {
  }

  public static boolean update(final UserCallback callback, final boolean needsEarlyConfirmation, final boolean async, final Runnable postInstall,
      final Runnable restartHandler)
  {
    if (needsEarlyConfirmation)
    {
      if (!callback.question("Updates are needed to process the setup configuration, and then a restart is required. "
          + "It might be possible for this older version of the tool to process the configuration, but that's not recommended.\n\n"
          + "Do you wish to update and restart?"))
      {
        return false;
      }
    }

    callback.runInProgressDialog(async, new IRunnable()
    {
      public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
      {
        IProvisioningAgent agent = SetupCorePlugin.INSTANCE.getService(IProvisioningAgent.class);

        try
        {
          SubMonitor sub = SubMonitor.convert(monitor, needsEarlyConfirmation ? "Updating..." : "Checking for updates...", 1000);

          IStatus updateStatus = checkForUpdates(agent, false, postInstall, sub);
          if (updateStatus.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE)
          {
            callback.information(async, "No updates were found.");
          }
          else if (updateStatus.getSeverity() != IStatus.ERROR)
          {
            callback.execInUI(async, new Runnable()
            {
              public void run()
              {
                if (!needsEarlyConfirmation)
                {
                  callback.information(false, "Updates were installed. Press OK to restart.");
                }

                if (restartHandler != null)
                {
                  restartHandler.run();
                }
              }
            });
          }
          else
          {
            throw new InvocationTargetException(new CoreException(updateStatus));
          }
        }
        finally
        {
          SetupCorePlugin.INSTANCE.ungetService(agent);
          monitor.done();
        }
      }
    });

    return true;
  }

  public static IStatus checkForUpdates(IProvisioningAgent agent, boolean resolveOnly, Runnable postInstall, SubMonitor sub)
  {
    try
    {
      try
      {
        addRepositories(agent, true, sub);
      }
      catch (CoreException ex)
      {
        return ex.getStatus();
      }

      ProvisioningSession session = new ProvisioningSession(agent);
      List<IInstallableUnit> ius = getInstalledUnits(session);

      UpdateOperation operation = new UpdateOperation(session, ius);
      IStatus status = operation.resolveModal(sub.newChild(300));
      if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE)
      {
        return status;
      }

      if (status.getSeverity() == IStatus.CANCEL)
      {
        throw new OperationCanceledException();
      }

      if (status.getSeverity() != IStatus.ERROR)
      {
        ProvisioningJob job = operation.getProvisioningJob(null);
        if (job == null)
        {
          String resolutionDetails = operation.getResolutionDetails();
          throw new IllegalStateException(resolutionDetails);
        }

        if (resolveOnly)
        {
          return UPDATE_FOUND_STATUS;
        }

        sub.setTaskName("Installing updates...");

        try
        {
          addRepositories(agent, false, sub);
        }
        catch (CoreException ex)
        {
          return ex.getStatus();
        }

        status = job.runModal(sub.newChild(300));
        if (status.getSeverity() == IStatus.CANCEL)
        {
          throw new OperationCanceledException();
        }
      }

      return status;
    }
    finally
    {
      if (!resolveOnly && postInstall != null)
      {
        postInstall.run();
      }
    }
  }

  private static List<IInstallableUnit> getInstalledUnits(ProvisioningSession session)
  {
    IProvisioningAgent agent = session.getProvisioningAgent();
    IProfileRegistry profileRegistry = (IProfileRegistry)agent.getService(IProfileRegistry.class.getName());
    IProfile profile = profileRegistry.getProfile(IProfileRegistry.SELF);
    if (profile == null)
    {
      return Collections.emptyList();
    }

    IQueryResult<IInstallableUnit> queryResult = profile.query(QueryUtil.createIUAnyQuery(), null);

    List<IInstallableUnit> ius = new ArrayList<IInstallableUnit>();
    for (IInstallableUnit installableUnit : queryResult)
    {
      String id = installableUnit.getId();

      if (id.startsWith("org.eclipse.oomph"))
      {
        ius.add(installableUnit);
      }
    }

    return ius;
  }

  private static void addRepositories(IProvisioningAgent agent, boolean metadata, SubMonitor sub) throws CoreException
  {
    int xxx;

    addRepository(agent, "http://download.eclipse.org/releases/luna", metadata, sub.newChild(200));
    addRepository(agent, SetupCorePlugin.UPDATE_URL, metadata, sub.newChild(200));
  }

  private static void addRepository(IProvisioningAgent agent, String location, boolean metadata, IProgressMonitor monitor) throws CoreException
  {
    SubMonitor sub = SubMonitor.convert(monitor, "Loading " + location, 500);

    try
    {
      URI uri = new URI(location);

      if (metadata)
      {
        addMetadataRepository(agent, uri, sub);
      }
      else
      {
        addArtifactRepository(agent, uri, sub);
      }
    }
    catch (URISyntaxException ex)
    {
      throw new IllegalArgumentException(ex);
    }
  }

  private static void addMetadataRepository(IProvisioningAgent agent, URI location, IProgressMonitor monitor) throws CoreException
  {
    IMetadataRepositoryManager manager = (IMetadataRepositoryManager)agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
    if (manager == null)
    {
      throw new IllegalStateException("No metadata repository manager found");
    }

    manager.loadRepository(location, monitor);
  }

  private static void addArtifactRepository(IProvisioningAgent agent, URI location, IProgressMonitor monitor) throws CoreException
  {
    IArtifactRepositoryManager manager = (IArtifactRepositoryManager)agent.getService(IArtifactRepositoryManager.SERVICE_NAME);
    if (manager == null)
    {
      throw new IllegalStateException("No artifact repository manager found");
    }

    manager.loadRepository(location, monitor);
  }
}
