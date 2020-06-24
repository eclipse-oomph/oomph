/*
 * Copyright (c) 2014, 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.core.listeners;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Requirement;
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
import org.eclipse.osgi.util.NLS;

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
  public static final String ANNOTATION = "http:/www.eclipse.org/oomph/targlets/PomArtifactUpdater"; //$NON-NLS-1$

  public static final String ANNOTATION_SKIP_ARTIFACT_IDS = "skipArtifactIDs"; //$NON-NLS-1$

  public static final String ANNOTATION_SKIP_VERSIONS = "skipVersions"; //$NON-NLS-1$

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
      for (Annotation annotation : BaseUtil.getAnnotations(targlet, ANNOTATION))
      {
        EMap<String, String> details = annotation.getDetails();
        boolean skipArtifactIDs = "true".equalsIgnoreCase(details.get(ANNOTATION_SKIP_ARTIFACT_IDS)); //$NON-NLS-1$
        boolean skipVersions = "true".equalsIgnoreCase(details.get(ANNOTATION_SKIP_VERSIONS)); //$NON-NLS-1$

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
    monitor.subTask(Messages.PomArtifactUpdater_Checking_task);

    for (Entry<IInstallableUnit, WorkspaceIUInfo> entry : workspaceIUInfos.entrySet())
    {
      TargletsCorePlugin.checkCancelation(monitor);

      final IInstallableUnit iu = entry.getKey();
      WorkspaceIUInfo info = entry.getValue();
      File folder = info.getLocation();

      final File pom = new File(folder, "pom.xml"); //$NON-NLS-1$
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
                if (newID.endsWith(Requirement.FEATURE_SUFFIX))
                {
                  newID = newID.substring(0, newID.length() - Requirement.FEATURE_SUFFIX.length());
                }
                else if (newID.endsWith(Requirement.PROJECT_SUFFIX))
                {
                  newID = newID.substring(0, newID.length() - Requirement.PROJECT_SUFFIX.length());
                }

                ElementUpdater artifactIDUpdater = new ElementUpdater(rootElement, "artifactId"); //$NON-NLS-1$
                newContents = artifactIDUpdater.update(newContents, newID);
              }

              if (!skipVersions)
              {
                VersionRange versionRange = P2Factory.eINSTANCE.createVersionRange(iu.getVersion(), VersionSegment.MICRO);
                String newVersion = versionRange.getMinimum() + "-SNAPSHOT"; //$NON-NLS-1$

                ElementUpdater versionUpdater = new ElementUpdater(rootElement, "version"); //$NON-NLS-1$
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
            monitor.subTask(
                NLS.bind(Messages.PomArtifactUpdater_Updating_task, uri.isPlatformResource() ? uri.toPlatformString(true) : uri.toFileString()));
            super.setContents(uri, encoding, contents);
          }
        }.update(pom);
      }
    }
  }
}
