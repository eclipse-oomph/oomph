/*
 * Copyright (c) 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.mylyn;

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
  public String resolveValue(IDynamicVariable variable, String argument) throws CoreException
  {
    ITask activeTask = TasksUi.getTaskActivityManager().getActiveTask();
    if (activeTask != null)
    {
      if (argument == null || "Id".equalsIgnoreCase(argument))
      {
        return activeTask.getTaskId();
      }

      if ("Key".equalsIgnoreCase(argument))
      {
        return activeTask.getTaskKey();
      }

      if ("Url".equalsIgnoreCase(argument))
      {
        return activeTask.getUrl();
      }

      if ("Kind".equalsIgnoreCase(argument))
      {
        return activeTask.getTaskKind();
      }

      if ("Owner".equalsIgnoreCase(argument))
      {
        return activeTask.getOwner();
      }

      if ("OwnerId".equalsIgnoreCase(argument))
      {
        return activeTask.getOwnerId();
      }

      if ("Priority".equalsIgnoreCase(argument))
      {
        return activeTask.getPriority();
      }

      if ("RepositoryUrl".equalsIgnoreCase(argument))
      {
        return activeTask.getRepositoryUrl();
      }

      if ("Summary".equalsIgnoreCase(argument))
      {
        String summary = activeTask.getSummary();
        return summary == null ? null : StringUtil.escape(summary);
      }

      if ("ConnectorKind".equalsIgnoreCase(argument))
      {
        return activeTask.getConnectorKind();
      }

      if ("HandleIdentifier".equalsIgnoreCase(argument))
      {
        return activeTask.getHandleIdentifier();
      }

      if ("CreationDate".equalsIgnoreCase(argument))
      {
        return formatDate(activeTask.getCreationDate());
      }

      if ("CreationTime".equalsIgnoreCase(argument))
      {
        return formatTime(activeTask.getCreationDate());
      }

      if ("CompletionDate".equalsIgnoreCase(argument))
      {
        return formatDate(activeTask.getCompletionDate());
      }

      if ("CompletionTime".equalsIgnoreCase(argument))
      {
        return formatTime(activeTask.getCompletionDate());
      }

      if ("DueDate".equalsIgnoreCase(argument))
      {
        return formatDate(activeTask.getDueDate());
      }

      if ("DueTime".equalsIgnoreCase(argument))
      {
        return formatTime(activeTask.getDueDate());
      }

      if ("ModificationDate".equalsIgnoreCase(argument))
      {
        return formatDate(activeTask.getModificationDate());
      }

      if ("ModificationTime".equalsIgnoreCase(argument))
      {
        return formatTime(activeTask.getModificationDate());
      }

      if (argument.startsWith("@"))
      {
        return activeTask.getAttribute(argument.substring(1));
      }
    }

    return null;
  }

  private static String formatDate(Date date)
  {
    return date == null ? null : new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
  }

  private static String formatTime(Date date)
  {
    return date == null ? null : Long.toString(date.getTime());
  }
}
