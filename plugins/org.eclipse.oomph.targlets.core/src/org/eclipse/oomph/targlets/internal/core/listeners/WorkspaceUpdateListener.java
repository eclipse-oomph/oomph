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
package org.eclipse.oomph.targlets.internal.core.listeners;

import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.core.ITargletContainerListener;
import org.eclipse.oomph.targlets.core.TargletContainerEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.ProfileUpdateSucceededEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.WorkspaceUpdateFinishedEvent;

import org.eclipse.core.runtime.IProgressMonitor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public abstract class WorkspaceUpdateListener implements ITargletContainerListener
{
  private final Map<String, ProfileUpdateSucceededEvent> profileUpdateSucceededEvents = new HashMap<String, ProfileUpdateSucceededEvent>();

  public WorkspaceUpdateListener()
  {
  }

  public void handleTargletContainerEvent(TargletContainerEvent event, IProgressMonitor monitor) throws Exception
  {
    if (event instanceof ProfileUpdateSucceededEvent)
    {
      ProfileUpdateSucceededEvent profileUpdateSucceededEvent = (ProfileUpdateSucceededEvent)event;

      ITargletContainer targletContainer = profileUpdateSucceededEvent.getSource();
      profileUpdateSucceededEvents.put(targletContainer.getID(), profileUpdateSucceededEvent);
    }
    else if (event instanceof WorkspaceUpdateFinishedEvent)
    {
      WorkspaceUpdateFinishedEvent workspaceUpdateFinishedEvent = (WorkspaceUpdateFinishedEvent)event;

      ITargletContainer targletContainer = workspaceUpdateFinishedEvent.getSource();
      ProfileUpdateSucceededEvent profileUpdateSucceededEvent = profileUpdateSucceededEvents.remove(targletContainer.getID());

      if (profileUpdateSucceededEvent != null)
      {
        handleTargletContainerEvent(profileUpdateSucceededEvent, workspaceUpdateFinishedEvent, monitor);
      }
    }
  }

  protected abstract void handleTargletContainerEvent(ProfileUpdateSucceededEvent profileUpdateSucceededEvent,
      WorkspaceUpdateFinishedEvent workspaceUpdateFinishedEvent, IProgressMonitor monitor) throws Exception;
}
