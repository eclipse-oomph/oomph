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
import org.eclipse.oomph.targlets.core.TargletEvent;
import org.eclipse.oomph.targlets.core.TargletEvent.ProfileUpdate;
import org.eclipse.oomph.targlets.core.TargletListener;
import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class PomModulesUpdater implements TargletListener
{
  public static final String ANNOTATION = "http:/www.eclipse.org/oomph/targlets/PomModulesUpdater";

  public static final String ANNOTATION_LOCATION = "location";

  private static final Pattern MODULES_PATTERN = Pattern.compile("([ \\t]*)<modules>.*?</modules>", Pattern.DOTALL);

  public PomModulesUpdater()
  {
  }

  public void handleTargletEvent(TargletEvent event) throws Exception
  {
    if (event instanceof ProfileUpdate)
    {
      ProfileUpdate profileUpdate = (ProfileUpdate)event;
      Targlet targlet = profileUpdate.getSource();

      Annotation annotation = targlet.getAnnotation(ANNOTATION);
      if (annotation != null)
      {
        String location = annotation.getDetails().get(ANNOTATION_LOCATION);
        if (!StringUtil.isEmpty(location))
        {
          File mainPom = new File(location);
          if (mainPom.isFile())
          {
            Map<IInstallableUnit, File> projectLocations = profileUpdate.getProjectLocations();
            updatePomModules(mainPom, projectLocations);
          }
          else
          {
            TargletsCorePlugin.INSTANCE.log("Not a file: " + mainPom, IStatus.WARNING);
          }
        }
      }
    }
  }

  private void updatePomModules(final File mainPom, final Map<IInstallableUnit, File> projectLocations) throws Exception
  {
    new FileUpdater()
    {
      @Override
      protected String createNewContents(String oldContents, String nl)
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

          List<String> modules = analyzeProjects(mainPom, projectLocations);
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
    }.update(mainPom);
  }

  private List<String> analyzeProjects(File mainPom, Map<IInstallableUnit, File> projectLocations)
  {
    URI mainURI = URI.createFileURI(mainPom.getAbsolutePath());
    List<String> modules = new ArrayList<String>();

    for (File folder : projectLocations.values())
    {
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
