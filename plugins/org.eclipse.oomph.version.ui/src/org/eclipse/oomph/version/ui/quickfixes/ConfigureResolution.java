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

import org.eclipse.oomph.internal.version.VersionBuilderArguments;
import org.eclipse.oomph.version.Markers;
import org.eclipse.oomph.version.ui.Activator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;

/**
 * @author Eike Stepper
 */
public class ConfigureResolution extends AbstractResolution
{
  private String option;

  public ConfigureResolution(IMarker marker, String option)
  {
    super(marker, "Configure the project to ignore the problem", Activator.CORRECTION_CONFIGURE_GIF);
    this.option = option;
  }

  @Override
  public String getDescription()
  {
    IProject project = getMarker().getResource().getProject();
    return "Set " + option + " = true in '/" + project.getName() + "/.project'";
  }

  @Override
  protected boolean isApplicable(IMarker marker)
  {
    String requiredOption = Markers.getQuickFixConfigureOption(marker);
    return option.equals(requiredOption);
  }

  @Override
  protected void apply(IMarker marker) throws Exception
  {
    String option = Markers.getQuickFixConfigureOption(marker);

    IProject project = marker.getResource().getProject();
    VersionBuilderArguments arguments = new VersionBuilderArguments(project);
    arguments.put(option, "true");
    arguments.applyTo(project);
  }
}
