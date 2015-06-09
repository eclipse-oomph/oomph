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

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.ProfileUpdateSucceededEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.WorkspaceUpdateFinishedEvent;
import org.eclipse.oomph.targlets.core.WorkspaceIUInfo;
import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;
import org.eclipse.oomph.util.XMLUtil;
import org.eclipse.oomph.util.XMLUtil.ElementUpdater;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.VersionRange;

import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Eike Stepper
 */
public class PomArtifactUpdater extends WorkspaceUpdateListener
{
  public static final String ANNOTATION = "http:/www.eclipse.org/oomph/targlets/PomArtifactUpdater";

  public static final String ANNOTATION_SKIP_ARTIFACT_IDS = "skipArtifactIDs";

  public static final String ANNOTATION_SKIP_VERSIONS = "skipVersions";

  public PomArtifactUpdater()
  {
  }

  @Override
  protected void handleTargletContainerEvent(ProfileUpdateSucceededEvent profileUpdateSucceededEvent, WorkspaceUpdateFinishedEvent workspaceUpdateFinishedEvent,
      IProgressMonitor monitor) throws Exception
  {
    ITargletContainer targletContainer = profileUpdateSucceededEvent.getSource();
    for (Targlet targlet : targletContainer.getTarglets())
    {
      Annotation annotation = targlet.getAnnotation(ANNOTATION);
      if (annotation != null)
      {
        EMap<String, String> details = annotation.getDetails();
        boolean skipArtifactIDs = "true".equalsIgnoreCase(details.get(ANNOTATION_SKIP_ARTIFACT_IDS));
        boolean skipVersions = "true".equalsIgnoreCase(details.get(ANNOTATION_SKIP_VERSIONS));

        if (!skipArtifactIDs || !skipVersions)
        {
          Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = profileUpdateSucceededEvent.getWorkspaceIUInfos();
          updatePomArtifacts(skipArtifactIDs, skipVersions, workspaceIUInfos, monitor);
        }
      }
    }
  }

  private static void updatePomArtifacts(final boolean skipArtifactIDs, final boolean skipVersions, Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos,
      final IProgressMonitor monitor) throws Exception
  {
    final DocumentBuilder documentBuilder = XMLUtil.createDocumentBuilder();
    monitor.subTask("Checking for POM artifact updates");

    for (Entry<IInstallableUnit, WorkspaceIUInfo> entry : workspaceIUInfos.entrySet())
    {
      TargletsCorePlugin.checkCancelation(monitor);

      final IInstallableUnit iu = entry.getKey();
      WorkspaceIUInfo info = entry.getValue();
      File folder = info.getLocation();

      final File pom = new File(folder, "pom.xml");
      if (pom.isFile())
      {
        new FileUpdater()
        {
          @Override
          protected String createNewContents(String oldContents, String encoding, String nl)
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
          protected void setContents(URI uri, String encoding, String contents) throws IOException
          {
            monitor.subTask("Updating " + (uri.isPlatformResource() ? uri.toPlatformString(true) : uri.toFileString()));
            super.setContents(uri, encoding, contents);
          }
        }.update(pom);
      }
    }
  }
}
