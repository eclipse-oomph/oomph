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
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.ProfileUpdateSucceededEvent;
import org.eclipse.oomph.targlets.core.TargletContainerEvent.WorkspaceUpdateFinishedEvent;
import org.eclipse.oomph.targlets.core.WorkspaceIUInfo;
import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class PomModulesUpdater extends WorkspaceUpdateListener
{
  public static final String ANNOTATION = "http:/www.eclipse.org/oomph/targlets/PomModulesUpdater";

  public static final String ANNOTATION_LOCATION = "location";

  private static final Pattern MODULES_PATTERN = Pattern.compile("([ \\t]*)<modules>.*?</modules>", Pattern.DOTALL);

  public PomModulesUpdater()
  {
  }

  @Override
  protected void handleTargletContainerEvent(ProfileUpdateSucceededEvent profileUpdateSucceededEvent,
      WorkspaceUpdateFinishedEvent workspaceUpdateFinishedEvent, IProgressMonitor monitor) throws Exception
  {
    ITargletContainer targletContainer = profileUpdateSucceededEvent.getSource();
    for (Targlet targlet : targletContainer.getTarglets())
    {
      Annotation annotation = targlet.getAnnotation(ANNOTATION);
      if (annotation != null)
      {
        String location = annotation.getDetails().get(ANNOTATION_LOCATION);
        if (!StringUtil.isEmpty(location))
        {
          File mainPom = new File(location);
          if (mainPom.isFile())
          {
            Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = profileUpdateSucceededEvent.getWorkspaceIUInfos();
            updatePomModules(mainPom, workspaceIUInfos, monitor);
          }
          else
          {
            TargletsCorePlugin.INSTANCE.log("Not a file: " + mainPom, IStatus.WARNING);
          }
        }
      }
    }
  }

  private static void updatePomModules(final File mainPom, final Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos, final IProgressMonitor monitor)
      throws Exception
  {
    monitor.subTask("Checking for POM modules updates");
    new FileUpdater()
    {
      @Override
      protected String createNewContents(String oldContents, String encoding, String nl)
      {
        Matcher matcher = MODULES_PATTERN.matcher(oldContents);
        if (matcher.find())
        {
          String start = oldContents.substring(0, matcher.start(0));
          String end = oldContents.substring(matcher.end(0));
          String indent = matcher.group(1);

          StringBuilder builder = new StringBuilder();
          builder.append(start);
          builder.append(indent);
          builder.append("<modules>");
          builder.append(nl);

          List<String> modules = analyzeProjects(mainPom, workspaceIUInfos, monitor);
          for (String module : modules)
          {
            builder.append(indent);
            builder.append(indent);
            builder.append("<module>");
            builder.append(module);
            builder.append("</module>");
            builder.append(nl);
          }

          builder.append(indent);
          builder.append("</modules>");
          builder.append(end);

          return builder.toString();
        }

        return null;
      }

      @Override
      protected void setContents(URI uri, String encoding, String contents) throws IOException
      {
        monitor.subTask("Updating " + (uri.isPlatformResource() ? uri.toPlatformString(true) : uri.toFileString()));
        super.setContents(uri, encoding, contents);
      }
    }.update(mainPom);
  }

  private static List<String> analyzeProjects(File mainPom, Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos, IProgressMonitor monitor)
  {
    URI mainURI = URI.createFileURI(mainPom.getAbsolutePath());
    List<String> modules = new ArrayList<String>();

    for (WorkspaceIUInfo info : workspaceIUInfos.values())
    {
      TargletsCorePlugin.checkCancelation(monitor);

      File folder = info.getLocation();
      File pom = new File(folder, "pom.xml");
      if (pom.isFile())
      {
        URI uri = URI.createFileURI(folder.getAbsolutePath()).deresolve(mainURI);
        modules.add(uri.toString());
      }
    }

    Collections.sort(modules);
    return modules;
  }
}
