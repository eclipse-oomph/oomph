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
package org.eclipse.oomph.version.ui.quickfixes;

import org.eclipse.oomph.internal.version.Activator.ReleaseCheckMode;
import org.eclipse.oomph.internal.version.VersionBuilderArguments;
import org.eclipse.oomph.version.Markers;
import org.eclipse.oomph.version.VersionUtil;
import org.eclipse.oomph.version.ui.Activator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;

/**
 * @author Eike Stepper
 */
public class IgnoreRootProjectsResolution extends AbstractResolution
{
  public IgnoreRootProjectsResolution(IMarker marker)
  {
    super(marker, "Ignore root components for release", Activator.CORRECTION_CONFIGURE_GIF);
  }

  @Override
  protected boolean isApplicable(IMarker marker)
  {
    return Markers.UNREFERENCED_ELEMENT_PROBLEM.equals(Markers.getProblemType(marker));
  }

  @Override
  public String getDescription()
  {
    IMarker marker = getMarker();
    IProject project = marker.getResource().getProject();
    VersionBuilderArguments arguments = new VersionBuilderArguments(project);

    return "Configure '" + arguments.getReleasePath()
        + "' to stop checking that non-root elements are referenced by a root element. To re-enable this checking, use the Preferences dialog to manage the release check mode.";
  }

  @Override
  protected void apply(IMarker marker) throws Exception
  {
    IProject project = marker.getResource().getProject();
    VersionBuilderArguments arguments = new VersionBuilderArguments(project);
    String releasePath = arguments.getReleasePath();

    org.eclipse.oomph.internal.version.Activator.setReleaseCheckMode(releasePath, ReleaseCheckMode.PARTIAL);
    VersionUtil.cleanReleaseProjects(releasePath);
  }
}
