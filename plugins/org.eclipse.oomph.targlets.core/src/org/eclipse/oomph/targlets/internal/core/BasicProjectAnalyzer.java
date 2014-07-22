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
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.predicates.Predicate;
import org.eclipse.oomph.resources.ResourcesFactory;
import org.eclipse.oomph.resources.ResourcesUtil;

import org.eclipse.emf.common.util.EList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.SubMonitor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class BasicProjectAnalyzer<T>
{
  private MultiStatus status = new MultiStatus(TargletsCorePlugin.INSTANCE.getSymbolicName(), 0, "Project Analysis", null);

  protected void log(File file, Object object)
  {
    IStatus childStatus = TargletsCorePlugin.INSTANCE.getStatus(object);
    String message = childStatus.getMessage() + " (while processing " + file + ")";
    MultiStatus multiStatus = new MultiStatus(childStatus.getPlugin(), childStatus.getCode(), message, childStatus.getException());
    multiStatus.addAll(childStatus);
    status.add(multiStatus);
  }

  public IStatus getStatus()
  {
    return status;
  }

  public Map<T, File> analyze(File folder, EList<Predicate> predicates, boolean locateNestedProjects, ProjectVisitor<T> visitor, IProgressMonitor monitor)
  {
    Map<T, File> results = new HashMap<T, File>();
    analyze(folder, predicates, locateNestedProjects, results, visitor, monitor);
    return results;
  }

  private void analyze(File folder, EList<Predicate> predicates, boolean locateNestedProjects, Map<T, File> results, ProjectVisitor<T> visitor,
      IProgressMonitor monitor)
  {
    if (monitor != null && monitor.isCanceled())
    {
      throw new OperationCanceledException();
    }

    SubMonitor progress = SubMonitor.convert(monitor, 10);

    IProject project = ResourcesFactory.eINSTANCE.loadProject(folder);
    if (project != null && ResourcesUtil.matchesPredicates(project, predicates))
    {
      try
      {
        T result = analyzeProject(folder, visitor, monitor);
        if (result != null)
        {
          File cextFile = new File(folder, "component.ext");
          if (cextFile.exists())
          {
            try
            {
              visitor.visitComponentExtension(cextFile, result, monitor);
            }
            catch (Exception ex)
            {
              log(cextFile, ex);
            }
          }

          File cspexFile = new File(folder, "buckminster.cspex");
          if (cspexFile.exists())
          {
            try
            {
              visitor.visitCSpex(cspexFile, result, monitor);
            }
            catch (Exception ex)
            {
              log(cspexFile, ex);
            }
          }

          result = filter(result);
          if (result != null)
          {
            results.put(result, folder);
          }
        }

        if (!locateNestedProjects)
        {
          return;
        }
      }
      catch (Exception ex)
      {
        log(folder, ex);
      }

      progress.worked(1);
    }
    else if (!locateNestedProjects && project != null)
    {
      progress.worked(10);
      return;
    }

    analyzeRecursive(folder, predicates, locateNestedProjects, results, visitor, progress.newChild(9));
  }

  private void analyzeRecursive(File folder, EList<Predicate> predicates, boolean locateNestedProjects, Map<T, File> results, ProjectVisitor<T> visitor,
      IProgressMonitor monitor)
  {
    File[] listFiles = folder.listFiles();
    if (listFiles != null)
    {
      SubMonitor progress = SubMonitor.convert(monitor, listFiles.length);
      for (int i = 0; i < listFiles.length; i++)
      {
        File file = listFiles[i];
        if (file.isDirectory())
        {
          try
          {
            analyze(file, predicates, locateNestedProjects, results, visitor, progress.newChild(1));
          }
          catch (Exception ex)
          {
            log(file, ex);
          }
        }
        else
        {
          progress.worked(1);
        }
      }
    }
  }

  private T analyzeProject(File folder, ProjectVisitor<T> visitor, IProgressMonitor monitor)
  {
    File featureFile = new File(folder, "feature.xml");
    if (featureFile.exists())
    {
      try
      {
        return visitor.visitFeature(featureFile, monitor);
      }
      catch (Exception ex)
      {
        log(featureFile, ex);
      }
    }

    File manifestFile = new File(folder, "META-INF/MANIFEST.MF");
    if (manifestFile.exists())
    {
      try
      {
        return visitor.visitPlugin(manifestFile, monitor);
      }
      catch (Exception ex)
      {
        log(manifestFile, ex);
      }
    }

    File cdefFile = new File(folder, "component.def");
    if (cdefFile.exists())
    {
      try
      {
        return visitor.visitComponentDefinition(cdefFile, monitor);
      }
      catch (Exception ex)
      {
        log(cdefFile, ex);
      }
    }

    File cspecFile = new File(folder, "buckminster.cspec");
    if (cspecFile.exists())
    {
      try
      {
        return visitor.visitCSpec(cspecFile, monitor);
      }
      catch (Exception ex)
      {
        log(cspecFile, ex);
      }
    }

    return null;
  }

  protected T filter(T result)
  {
    return result;
  }
}
