/*
 * Copyright (c) 2023 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.version.ui.quickfixes;

import org.eclipse.oomph.internal.version.VersionBuilderArguments;
import org.eclipse.oomph.internal.version.VersionNature;
import org.eclipse.oomph.version.Markers;
import org.eclipse.oomph.version.ui.Activator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;

/**
 * @author Ed Merks
 */
public class IgnoreMissingVersionNatureResolution extends AbstractResolution
{
  public IgnoreMissingVersionNatureResolution(IMarker marker)
  {
    super(marker, Messages.IgnoreMissingVersionNatureResolution_IgnoreMissingVersionNature_label, Activator.CORRECTION_CONFIGURE_GIF);
  }

  @Override
  protected boolean isApplicable(IMarker marker)
  {
    String requiredNature = Markers.getQuickFixNature(marker);
    return VersionNature.NATURE_ID.equals(requiredNature);
  }

  @Override
  public String getDescription()
  {
    return Messages.IgnoreMissingVersionNatureResolution_IgnoreMissingVersionNature_description;
  }

  @Override
  protected void apply(IMarker marker) throws Exception
  {
    IProject project = marker.getResource().getProject();
    VersionBuilderArguments arguments = new VersionBuilderArguments(project);
    arguments.setIgnoreMissingVersionNature(true);
    arguments.applyTo(project);
  }
}
