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
import org.eclipse.osgi.util.NLS;

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
  public static final String ANNOTATION = "http:/www.eclipse.org/oomph/targlets/PomModulesUpdater"; //$NON-NLS-1$

  public static final String ANNOTATION_LOCATION = "location"; //$NON-NLS-1$

  public static final String ANNOTATION_MODULE_ROOTS = "moduleRoots"; //$NON-NLS-1$

  private static final Pattern MODULES_PATTERN = Pattern.compile("([ \\t]*)<modules>.*?</modules>", Pattern.DOTALL); //$NON-NLS-1$

  public PomModulesUpdater()
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
        String location = annotation.getDetails().get(ANNOTATION_LOCATION);
        if (!StringUtil.isEmpty(location))
        {
          File mainPom = new File(location);
          if (mainPom.isFile())
          {
            String[] moduleRoots = null;

            String roots = annotation.getDetails().get(ANNOTATION_MODULE_ROOTS);
            if (!StringUtil.isEmpty(roots))
            {
              moduleRoots = roots.split(","); //$NON-NLS-1$
              for (int i = 0; i < moduleRoots.length; i++)
              {
                moduleRoots[i] = moduleRoots[i].replace('\\', '/');
              }
            }

            Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos = profileUpdateSucceededEvent.getWorkspaceIUInfos();
            updatePomModules(mainPom, moduleRoots, workspaceIUInfos, monitor);
          }
          else
          {
            TargletsCorePlugin.INSTANCE.log(NLS.bind(Messages.PomModulesUpdater_NotFile_message, mainPom), IStatus.WARNING);
          }
        }
      }
    }
  }

  private static void updatePomModules(final File mainPom, final String[] moduleRoots, final Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos,
      final IProgressMonitor monitor) throws Exception
  {
    monitor.subTask(Messages.PomModulesUpdater_CheckingUpdates_task);

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
          builder.append("<modules>"); //$NON-NLS-1$
          builder.append(nl);

          List<String> modules = analyzeProjects(mainPom, moduleRoots, workspaceIUInfos, monitor);
          for (String module : modules)
          {
            builder.append(indent);
            builder.append(indent);
            builder.append("<module>"); //$NON-NLS-1$
            builder.append(module);
            builder.append("</module>"); //$NON-NLS-1$
            builder.append(nl);
          }

          builder.append(indent);
          builder.append("</modules>"); //$NON-NLS-1$
          builder.append(end);

          return builder.toString();
        }

        return null;
      }

      @Override
      protected void setContents(URI uri, String encoding, String contents) throws IOException
      {
        monitor.subTask(NLS.bind("Updating {0}", uri.isPlatformResource() ? uri.toPlatformString(true) : uri.toFileString())); //$NON-NLS-1$
        super.setContents(uri, encoding, contents);
      }
    }.update(mainPom);
  }

  private static List<String> analyzeProjects(File mainPom, String[] moduleRoots, Map<IInstallableUnit, WorkspaceIUInfo> workspaceIUInfos,
      IProgressMonitor monitor)
  {
    URI mainURI = URI.createFileURI(mainPom.getAbsolutePath());
    List<String> modules = new ArrayList<String>();

    for (WorkspaceIUInfo info : workspaceIUInfos.values())
    {
      TargletsCorePlugin.checkCancelation(monitor);

      File folder = info.getLocation();
      String modulePath = folder.getAbsolutePath().replace('\\', '/');

      boolean inRoot = true;
      if (moduleRoots != null)
      {
        inRoot = false;
        for (String moduleRoot : moduleRoots)
        {
          if (modulePath.startsWith(moduleRoot))
          {
            inRoot = true;
            break;
          }
        }
      }

      if (inRoot)
      {
        File pom = new File(folder, "pom.xml"); //$NON-NLS-1$
        if (pom.isFile())
        {
          URI uri = URI.createFileURI(modulePath).deresolve(mainURI);
          modules.add(uri.toString());
        }
      }
    }

    Collections.sort(modules);
    return modules;
  }
}
