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
package org.eclipse.oomph.version.ui.actions;

import org.eclipse.oomph.version.ui.Activator;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import org.osgi.framework.Bundle;

import java.lang.reflect.Field;

/**
 * @author Eike Stepper
 */
public class ToggleApiBuilderAction implements IWorkbenchWindowActionDelegate
{
  private Field buildDisabledField;

  public ToggleApiBuilderAction()
  {
    try
    {
      Bundle bundle = Activator.getPlugin().getBundle();
      Class<?> c = bundle.loadClass("org.eclipse.pde.api.tools.internal.builder.ApiAnalysisBuilder");
      buildDisabledField = c.getDeclaredField("buildDisabled");
      buildDisabledField.setAccessible(true);
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }
  }

  public void init(IWorkbenchWindow window)
  {
  }

  public void dispose()
  {
  }

  public void selectionChanged(IAction action, ISelection selection)
  {
    updateCheckState(action);
  }

  public void run(IAction action)
  {
    boolean disabled = isDisabled();
    setDisabled(!disabled);

    updateCheckState(action);
  }

  private void updateCheckState(IAction action)
  {
    boolean disabled = isDisabled();
    boolean checked = action.isChecked();
    if (checked != disabled)
    {
      action.setChecked(disabled);
    }
  }

  private boolean isDisabled()
  {
    if (buildDisabledField != null)
    {
      try
      {
        return (Boolean)buildDisabledField.get(null);
      }
      catch (Throwable ex)
      {
        Activator.log(ex);
      }
    }

    return false;
  }

  private void setDisabled(boolean disabled)
  {
    if (buildDisabledField != null)
    {
      try
      {
        buildDisabledField.set(null, disabled);
      }
      catch (Throwable ex)
      {
        Activator.log(ex);
      }
    }
  }
}
