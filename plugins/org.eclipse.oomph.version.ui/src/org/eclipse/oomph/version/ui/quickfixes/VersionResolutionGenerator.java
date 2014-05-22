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

import org.eclipse.oomph.version.Markers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class VersionResolutionGenerator implements IMarkerResolutionGenerator2
{
  public VersionResolutionGenerator()
  {
  }

  public IMarkerResolution[] getResolutions(IMarker marker)
  {
    List<IMarkerResolution> resolutions = new ArrayList<IMarkerResolution>();

    String problemType = Markers.getProblemType(marker);
    String regEx = Markers.getQuickFixPattern(marker);
    if (regEx != null)
    {
      String replacement = Markers.getQuickFixReplacement(marker);
      resolutions.add(new ReplaceResolution(marker, problemType, replacement));
      final String alternativeReplacement = Markers.getQuickFixAlternativeReplacement(marker);
      if (alternativeReplacement != null)
      {
        resolutions.add(new ReplaceResolution(marker, problemType, alternativeReplacement)
        {
          @Override
          public String getLabel()
          {
            return "Change to the omni version";
          }

          @Override
          public String getDescription()
          {
            return "Change the version to " + alternativeReplacement;
          }

          @Override
          protected String getQuickFixReplacement(IMarker marker)
          {
            return Markers.getQuickFixAlternativeReplacement(marker);
          }
        });
      }
    }

    if (Markers.UNREFERENCED_ELEMENT_PROBLEM.equals(problemType))
    {
      resolutions.add(new PropertiesResolution.RootProjects(marker));
      resolutions.add(new IgnoreRootProjectsResolution(marker));
    }
    else if (Markers.RELEASE_PATH_PROBLEM.equals(problemType))
    {
      resolutions.add(new ReleasePathResolution(marker));
    }

    String ignoreReference = Markers.getQuickFixReference(marker);
    if (ignoreReference != null)
    {
      resolutions.add(new PropertiesResolution.IgnoredReferences(marker));
    }

    String ignoreOption = Markers.getQuickFixConfigureOption(marker);
    if (ignoreOption != null)
    {
      resolutions.add(new ConfigureResolution(marker, ignoreOption));
    }

    String nature = Markers.getQuickFixNature(marker);
    if (nature != null)
    {
      resolutions.add(new AddNatureResolution(marker, nature));
    }

    return resolutions.toArray(new IMarkerResolution[resolutions.size()]);
  }

  public boolean hasResolutions(IMarker marker)
  {
    if (Markers.getQuickFixPattern(marker) != null)
    {
      return true;
    }

    if (Markers.getQuickFixConfigureOption(marker) != null)
    {
      return true;
    }

    if (Markers.getQuickFixReference(marker) != null)
    {
      return true;
    }

    if (Markers.getQuickFixNature(marker) != null)
    {
      return true;
    }

    String problemType = Markers.getProblemType(marker);
    if (Markers.UNREFERENCED_ELEMENT_PROBLEM.equals(problemType))
    {
      return true;
    }

    if (Markers.RELEASE_PATH_PROBLEM.equals(problemType))
    {
      return true;
    }

    return false;
  }
}
