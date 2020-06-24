/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.doc.guide;

import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.core.ITargletContainerListener;
import org.eclipse.oomph.targlets.core.TargletContainerEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.IDChangedEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.ProfileUpdateFailedEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.ProfileUpdateSucceededEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.TargletsChangedEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.WorkspaceUpdateFinishedEvent;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Listening To Targlet Container Events
 * <p>
 * When the state of a {@link ITargletContainer targlet container} changes registered {@link ITargletContainerListener listeners} are sent one of the following {@link TargletContainerEvent events}:
 * <p>
 * <ul>
 * <li> {@link IDChangedEvent}
 * <li> {@link TargletsChangedEvent}
 * <li> {@link ProfileUpdateSucceededEvent}
 * <li> {@link ProfileUpdateFailedEvent}
 * <li> {@link WorkspaceUpdateFinishedEvent}
 * </ul>
 * <p>
 * Listeners can be registered programmatically or declaratively:
 * <p>
 * <ul>
 * <li> The {@link org.eclipse.oomph.targlets.core.ITargletContainerListener.Registry listener registry} provides methods to add or remove listeners.
 * <li> The <a href="../../schemadoc/org_eclipse_oomph_targlets_core_targletContainerListeners.html"><code>org.eclipse.oomph.targlets.core.targletContainerListeners</code></a> extension point accepts declarative listener contributions.
 * </ul>
 * <p>
 * The following example shows a listener that prints information about all received events to the console:
 * <p>
 * {@link TargletContainerEventLogger}.
 * <p>
 *
 * @number 200
 * @author Eike Stepper
 * @see UnderstandingTargletContainers
 */
public class ListeningToEvents
{
}

/**
 * @snippet
 */
class TargletContainerEventLogger implements ITargletContainerListener
{
  public TargletContainerEventLogger()
  {
  }

  @SuppressWarnings("nls")
  public void handleTargletContainerEvent(TargletContainerEvent event, IProgressMonitor monitor) throws Exception
  {
    ITargletContainer container = event.getSource();

    if (event instanceof IDChangedEvent)
    {
      System.out.println("IDChangedEvent: " + container);
      System.out.println("  oldID: " + ((IDChangedEvent)event).getOldID());
    }
    else if (event instanceof TargletsChangedEvent)
    {
      System.out.println("TargletsChangedEvent: " + container);
    }
    else if (event instanceof ProfileUpdateSucceededEvent)
    {
      System.out.println("ProfileUpdateSucceededEvent: " + container);
      System.out.println("  metadataRepositories: " + ((ProfileUpdateSucceededEvent)event).getMetadataRepositories());
      System.out.println("  workspaceIUInfos: " + ((ProfileUpdateSucceededEvent)event).getWorkspaceIUInfos());
      System.out.println("  provisioningPlan: " + ((ProfileUpdateSucceededEvent)event).getProvisioningPlan());
      System.out.println("  profile: " + ((ProfileUpdateSucceededEvent)event).getProfile());
    }
    else if (event instanceof ProfileUpdateFailedEvent)
    {
      System.out.println("ProfileUpdateFailedEvent: " + container);
      System.out.println("  updateProblem: " + ((ProfileUpdateFailedEvent)event).getUpdateProblem());
    }
    else if (event instanceof WorkspaceUpdateFinishedEvent)
    {
      System.out.println("WorkspaceUpdateFinishedEvent: " + container);
      System.out.println("  importResults: " + ((WorkspaceUpdateFinishedEvent)event).getImportResults());
    }
    else
    {
      System.out.println(event);
    }
  }
}
