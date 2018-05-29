/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.version.ui.quickfixes;

import org.eclipse.oomph.internal.version.VersionBuilder;
import org.eclipse.oomph.internal.version.VersionBuilderArguments;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.version.Markers;
import org.eclipse.oomph.version.VersionUtil;
import org.eclipse.oomph.version.ui.Activator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Path;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Eike Stepper
 */
public class PropertiesResolution extends AbstractResolution
{
  private String propertyKey;

  private String propertyValue;

  public PropertiesResolution(IMarker marker, String label, String propertyKey, String propertyValue)
  {
    super(marker, label, Activator.CORRECTION_CONFIGURE_GIF);
    this.propertyKey = propertyKey;
    this.propertyValue = propertyValue;
  }

  @Override
  protected boolean isApplicable(IMarker marker)
  {
    return Markers.UNREFERENCED_ELEMENT_PROBLEM.equals(Markers.getProblemType(marker));
  }

  @Override
  public String getDescription()
  {
    IProject project = getMarker().getResource().getProject();
    VersionBuilderArguments arguments = new VersionBuilderArguments(project);
    IFile propertiesFile = VersionUtil.getFile(new Path(arguments.getReleasePath()), "properties");
    return "Add '" + propertyValue + "' to the " + propertyKey + " property of " + propertiesFile.getFullPath();
  }

  @Override
  protected void apply(IMarker marker) throws Exception
  {
    IProject project = marker.getResource().getProject();
    VersionBuilderArguments arguments = new VersionBuilderArguments(project);
    IFile propertiesFile = VersionUtil.getFile(new Path(arguments.getReleasePath()), "properties");
    if (propertiesFile.exists())
    {
      Properties properties = new Properties();
      InputStream contents = null;
      try
      {
        contents = propertiesFile.getContents();

        properties = new Properties();
        properties.load(contents);

        String oldValue = properties.getProperty(propertyKey, "");
        properties.setProperty(propertyKey, (oldValue + " " + propertyValue.replace("\\", "\\\\").replace(" ", "\\ ")).trim());
      }
      finally
      {
        IOUtil.closeSilent(contents);
      }

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      properties.store(out, "");
      contents = new ByteArrayInputStream(out.toByteArray());
      propertiesFile.setContents(contents, true, true, null);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class RootProjects extends PropertiesResolution
  {
    private static final String LABEL = "Mark as root project";

    public RootProjects(IMarker marker)
    {
      super(marker, LABEL, VersionBuilder.ROOT_PROJECTS_KEY, marker.getResource().getProject().getName());
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class IgnoredReferences extends PropertiesResolution
  {
    private static final String LABEL = "Mark as ignored reference";

    public IgnoredReferences(IMarker marker)
    {
      super(marker, LABEL, VersionBuilder.IGNORED_REFERENCES_KEY, Markers.getQuickFixReference(marker));
    }
  }
}
