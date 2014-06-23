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
package org.eclipse.oomph.targlets.internal.core.listeners;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.core.TargletEvent;
import org.eclipse.oomph.targlets.core.TargletEvent.ProfileUpdate;
import org.eclipse.oomph.targlets.core.TargletListener;
import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;
import org.eclipse.oomph.util.XMLUtil;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.VersionRange;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

import java.io.File;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class PomArtifactUpdater implements TargletListener
{
  public static final String ANNOTATION = "http:/www.eclipse.org/oomph/targlets/PomArtifactUpdater";

  public static final String ANNOTATION_SKIP_ARTIFACT_IDS = "skipArtifactIDs";

  public static final String ANNOTATION_SKIP_VERSIONS = "skipVersions";

  public PomArtifactUpdater()
  {
  }

  public void handleTargletEvent(TargletEvent event, IProgressMonitor monitor) throws Exception
  {
    if (event instanceof ProfileUpdate)
    {
      ProfileUpdate profileUpdate = (ProfileUpdate)event;
      Targlet targlet = profileUpdate.getSource();

      Annotation annotation = targlet.getAnnotation(ANNOTATION);
      if (annotation != null)
      {
        EMap<String, String> details = annotation.getDetails();
        boolean skipArtifactIDs = "true".equalsIgnoreCase(details.get(ANNOTATION_SKIP_ARTIFACT_IDS));
        boolean skipVersions = "true".equalsIgnoreCase(details.get(ANNOTATION_SKIP_VERSIONS));

        if (!skipArtifactIDs || !skipVersions)
        {
          Map<IInstallableUnit, File> projectLocations = profileUpdate.getProjectLocations();
          updatePomArtifacts(skipArtifactIDs, skipVersions, projectLocations, monitor);
        }
      }
    }
  }

  private static void updatePomArtifacts(final boolean skipArtifactIDs, final boolean skipVersions, Map<IInstallableUnit, File> projectLocations,
      final IProgressMonitor monitor) throws Exception
  {
    final DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
    monitor.subTask("Checking for POM artifact updates");

    for (Map.Entry<IInstallableUnit, File> entry : projectLocations.entrySet())
    {
      TargletsCorePlugin.checkCancelation(monitor);

      final IInstallableUnit iu = entry.getKey();
      File folder = entry.getValue();

      final File pom = new File(folder, "pom.xml");
      if (pom.isFile())
      {
        new FileUpdater()
        {
          @Override
          protected String createNewContents(String oldContents, String nl)
          {
            try
            {
              String newContents = oldContents;
              final Element rootElement = XMLUtil.loadRootElement(documentBuilder, pom);

              if (!skipArtifactIDs)
              {
                String newID = iu.getId();
                if (newID.endsWith(".feature.group"))
                {
                  newID = newID.substring(0, newID.length() - ".feature.group".length());
                }

                ElementUpdater artifactIDUpdater = new ElementUpdater(rootElement, "artifactId");
                newContents = artifactIDUpdater.update(newContents, newID);
              }

              if (!skipVersions)
              {
                VersionRange versionRange = P2Factory.eINSTANCE.createVersionRange(iu.getVersion(), VersionSegment.MICRO);
                String newVersion = versionRange.getMinimum() + "-SNAPSHOT";

                ElementUpdater versionUpdater = new ElementUpdater(rootElement, "version");
                newContents = versionUpdater.update(newContents, newVersion);
              }

              return newContents;
            }
            catch (RuntimeException ex)
            {
              throw ex;
            }
            catch (Exception ex)
            {
              throw new RuntimeException(ex);
            }
          }

          @Override
          protected void setContents(File file, IFile iFile, String contents) throws Exception
          {
            monitor.subTask("Updating " + (iFile != null ? iFile.getFullPath() : file));
            super.setContents(file, iFile, contents);
          }

        }.update(pom);
      }
    }
  }
}
