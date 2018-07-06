/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ostools;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.ui.handlers.HandlerUtil;

import java.io.File;

/**
 * @author Eike Stepper
 */
public abstract class AbstractLocationHandler extends AbstractHandler
{
  public AbstractLocationHandler()
  {
  }

  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    ISelection selection = HandlerUtil.getActiveMenuSelection(event);
    File location = getLocation(selection);

    try
    {
      execute(location);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return null;
  }

  protected abstract void execute(File location) throws Exception;

  public static File getLocation(ISelection selection)
  {
    if (selection instanceof IStructuredSelection)
    {
      IStructuredSelection ssel = (IStructuredSelection)selection;
      Object element = ssel.getFirstElement();
      return getLocation(element);
    }

    return null;
  }

  private static File getLocation(Object element)
  {
    IContainer container = getContainer(element);
    if (container != null)
    {
      return new File(container.getLocation().toOSString());
    }

    File directory = getDirectory(element);
    if (directory != null)
    {
      return directory;
    }

    File workTree = getWorkTree(element);
    if (workTree != null)
    {
      return workTree;
    }

    return null;
  }

  private static IContainer getContainer(Object element)
  {
    if (element instanceof IContainer)
    {
      return (IContainer)element;
    }

    if (element instanceof IResource)
    {
      return ((IResource)element).getParent();
    }

    if (element instanceof IAdaptable)
    {
      Object adapter = ((IAdaptable)element).getAdapter(IResource.class);
      return getContainer(adapter);
    }

    return null;
  }

  private static File getDirectory(Object element)
  {
    if (element instanceof File)
    {
      File file = (File)element;
      if (file.isDirectory())
      {
        return file;
      }

      return file.getParentFile();
    }

    if (element instanceof IAdaptable)
    {
      Object adapter = ((IAdaptable)element).getAdapter(File.class);
      return getDirectory(adapter);
    }

    return null;
  }

  private static File getWorkTree(Object element)
  {
    try
    {
      return GitHelper.getWorkTree(element);
    }
    catch (Throwable ex)
    {
      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class GitHelper
  {
    public static File getWorkTree(Object element)
    {
      if (element instanceof Repository)
      {
        return ((Repository)element).getWorkTree();
      }

      if (element instanceof IAdaptable)
      {
        Object adapter = ((IAdaptable)element).getAdapter(Repository.class);
        return getWorkTree(adapter);
      }

      return null;
    }
  }
}
