/*
 * Copyright (c) 2016, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.mylyn;

import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.ui.TasksUi;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Eike Stepper
 */
public class ActiveMylynTaskVariableResolver implements IDynamicVariableResolver
{
  @Override
  public String resolveValue(IDynamicVariable variable, String argument) throws CoreException
  {
    ITask activeTask = TasksUi.getTaskActivityManager().getActiveTask();
    if (activeTask != null)
    {
      if (argument == null || "Id".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return activeTask.getTaskId();
      }

      if ("Key".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return activeTask.getTaskKey();
      }

      if ("Url".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return activeTask.getUrl();
      }

      if ("Kind".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return activeTask.getTaskKind();
      }

      if ("Owner".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return activeTask.getOwner();
      }

      if ("OwnerId".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        try
        {
          return ReflectUtil.invokeMethod("getOwnerId", activeTask); //$NON-NLS-1$
          // return activeTask.getOwnerId();
        }
        catch (Throwable ex)
        {
          // Ignore.
        }
      }

      if ("Priority".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return activeTask.getPriority();
      }

      if ("RepositoryUrl".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return activeTask.getRepositoryUrl();
      }

      if ("Summary".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        String summary = activeTask.getSummary();
        return summary == null ? null : StringUtil.escape(summary);
      }

      if ("ConnectorKind".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return activeTask.getConnectorKind();
      }

      if ("HandleIdentifier".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return activeTask.getHandleIdentifier();
      }

      if ("CreationDate".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return formatDate(activeTask.getCreationDate());
      }

      if ("CreationTime".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return formatTime(activeTask.getCreationDate());
      }

      if ("CompletionDate".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return formatDate(activeTask.getCompletionDate());
      }

      if ("CompletionTime".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return formatTime(activeTask.getCompletionDate());
      }

      if ("DueDate".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return formatDate(activeTask.getDueDate());
      }

      if ("DueTime".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return formatTime(activeTask.getDueDate());
      }

      if ("ModificationDate".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return formatDate(activeTask.getModificationDate());
      }

      if ("ModificationTime".equalsIgnoreCase(argument)) //$NON-NLS-1$
      {
        return formatTime(activeTask.getModificationDate());
      }

      if (argument.startsWith("@")) //$NON-NLS-1$
      {
        return activeTask.getAttribute(argument.substring(1));
      }
    }

    return null;
  }

  private static String formatDate(Date date)
  {
    return date == null ? null : new SimpleDateFormat("yyyyMMdd_HHmmss").format(date); //$NON-NLS-1$
  }

  private static String formatTime(Date date)
  {
    return date == null ? null : Long.toString(date.getTime());
  }
}
