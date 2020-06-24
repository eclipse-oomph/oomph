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
import org.eclipse.osgi.util.NLS;

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
    IFile propertiesFile = VersionUtil.getFile(new Path(arguments.getReleasePath()), "properties"); //$NON-NLS-1$
    return NLS.bind(Messages.PropertiesResolution_description_addValueToKeyOfPropertiesFile,
        new Object[] { propertyValue, propertyKey, propertiesFile.getFullPath() });
  }

  @Override
  protected void apply(IMarker marker) throws Exception
  {
    IProject project = marker.getResource().getProject();
    VersionBuilderArguments arguments = new VersionBuilderArguments(project);
    IFile propertiesFile = VersionUtil.getFile(new Path(arguments.getReleasePath()), "properties"); //$NON-NLS-1$
    if (propertiesFile.exists())
    {
      Properties properties = new Properties();
      InputStream contents = null;
      try
      {
        contents = propertiesFile.getContents();

        properties = new Properties();
        properties.load(contents);

        String oldValue = properties.getProperty(propertyKey, ""); //$NON-NLS-1$
        properties.setProperty(propertyKey, (oldValue + " " + propertyValue.replace("\\", "\\\\").replace(" ", "\\ ")).trim()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
      }
      finally
      {
        IOUtil.closeSilent(contents);
      }

      ByteArrayOutputStream out = new ByteArrayOutputStream();
      properties.store(out, ""); //$NON-NLS-1$
      contents = new ByteArrayInputStream(out.toByteArray());
      propertiesFile.setContents(contents, true, true, null);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class RootProjects extends PropertiesResolution
  {
    public RootProjects(IMarker marker)
    {
      super(marker, Messages.PropertiesResolution_rootProjects_label, VersionBuilder.ROOT_PROJECTS_KEY, marker.getResource().getProject().getName());
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class IgnoredReferences extends PropertiesResolution
  {
    public IgnoredReferences(IMarker marker)
    {
      super(marker, Messages.PropertiesResolution_ignoredReferences_label, VersionBuilder.IGNORED_REFERENCES_KEY, Markers.getQuickFixReference(marker));
    }
  }
}
