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

import org.eclipse.oomph.internal.version.VersionBuilderArguments;
import org.eclipse.oomph.internal.version.VersionNature;
import org.eclipse.oomph.version.Markers;
import org.eclipse.oomph.version.ui.Activator;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.osgi.util.NLS;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Eike Stepper
 */
public class AddNatureResolution extends AbstractResolution
{
  private String nature;

  public AddNatureResolution(IMarker marker, String nature)
  {
    super(marker, VersionNature.NATURE_ID.equals(nature) ? Messages.AddNatureResolution_label_addVersionManagementBuilder
        : NLS.bind(Messages.AddNatureResolution_label_addNature, nature), Activator.CORRECTION_CONFIGURE_GIF);
    this.nature = nature;
  }

  @Override
  protected boolean isApplicable(IMarker marker)
  {
    String requiredNature = Markers.getQuickFixNature(marker);
    return nature.equals(requiredNature);
  }

  @Override
  protected void apply(IMarker marker) throws Exception
  {
    String nature = Markers.getQuickFixNature(marker);
    IProject project = marker.getResource().getProject();
    String projectName = Markers.getQuickFixProject(marker);
    if (projectName != null)
    {
      VersionBuilderArguments arguments = new VersionBuilderArguments(project);
      IProject targetProject = project.getWorkspace().getRoot().getProject(projectName);
      arguments.applyTo(targetProject);
    }
    else
    {
      IProjectDescription description = project.getDescription();
      ArrayList<String> natures = new ArrayList<>(Arrays.asList(description.getNatureIds()));
      if (natures.contains(nature))
      {
        natures.remove(nature);
        description.setNatureIds(natures.toArray(new String[natures.size()]));
        project.setDescription(description, null);
      }
      natures.add(nature);
      description.setNatureIds(natures.toArray(new String[natures.size()]));
      project.setDescription(description, null);
    }
  }
}
