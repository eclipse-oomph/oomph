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
package org.eclipse.oomph.version.ui.quickfixes;

import org.eclipse.oomph.version.ui.Activator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.views.markers.WorkbenchMarkerResolution;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public abstract class AbstractResolution extends WorkbenchMarkerResolution
{
  private IMarker marker;

  private String label;

  private String imageKey;

  public AbstractResolution(IMarker marker, String label, String imageKey)
  {
    this.marker = marker;
    this.label = label;
    this.imageKey = imageKey;
  }

  public IMarker getMarker()
  {
    return marker;
  }

  public String getLabel()
  {
    return label;
  }

  public String getDescription()
  {
    return "";
  }

  public final Image getImage()
  {
    ImageRegistry imageRegistry = Activator.getPlugin().getImageRegistry();
    return imageRegistry.get(imageKey);
  }

  @Override
  public IMarker[] findOtherMarkers(IMarker[] markers)
  {
    List<IMarker> result = new ArrayList<IMarker>();
    for (IMarker marker : markers)
    {
      try
      {
        if (marker != this.marker && isApplicable(marker))
        {
          result.add(marker);
        }
      }
      catch (Exception ex)
      {
        Activator.log(ex);
      }
    }

    return result.toArray(new IMarker[result.size()]);
  }

  protected abstract boolean isApplicable(IMarker marker) throws Exception;

  @Override
  public void run(final IMarker[] markers, IProgressMonitor monitor)
  {
    new UIJob("Applying Fixes")
    {
      @Override
      public IStatus runInUIThread(IProgressMonitor monitor)
      {
        try
        {
          ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable()
          {
            public void run(IProgressMonitor monitor) throws CoreException
            {
              AbstractResolution.super.run(markers, monitor);
            }
          }, monitor);

          return Status.OK_STATUS;
        }
        catch (CoreException ex)
        {
          return ex.getStatus();
        }
      }
    }.schedule();
  }

  public final void run(IMarker marker)
  {
    try
    {
      apply(marker);
    }
    catch (Exception ex)
    {
      Activator.log(ex);
    }
  }

  protected abstract void apply(IMarker marker) throws Exception;
}
