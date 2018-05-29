/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.launches;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.swt.graphics.Image;

/**
 * @author Eike Stepper
 */
public class LaunchConfigLabelDecorator extends BaseLabelProvider implements ILabelDecorator // , IColorDecorator
{
  // private static final Color RED = Display.getDefault().getSystemColor(SWT.COLOR_RED);
  //
  // private static final Color GRAY = Display.getDefault().getSystemColor(SWT.COLOR_GRAY);

  private static final ImageDescriptor LOCAL_OVERLAY = Activator.getImageDescriptor("local_ovr");

  private static final ImageDescriptor EXAMPLE_OVERLAY = Activator.getImageDescriptor("example_ovr");

  private final ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());

  public LaunchConfigLabelDecorator()
  {
  }

  @Override
  public void dispose()
  {
    resourceManager.dispose();
    super.dispose();
  }

  public Image decorateImage(Image image, Object element)
  {
    if (image != null)
    {
      if (isLocal(element))
      {
        DecorationOverlayIcon icon = new DecorationOverlayIcon(image, LOCAL_OVERLAY, IDecoration.TOP_LEFT);
        return (Image)resourceManager.get(icon);
      }

      if (isExampleCopy(element))
      {
        DecorationOverlayIcon icon = new DecorationOverlayIcon(image, EXAMPLE_OVERLAY, IDecoration.TOP_LEFT);
        return (Image)resourceManager.get(icon);
      }
    }

    return null;
  }

  public String decorateText(String text, Object element)
  {
    if (isLocal(element))
    {
      return "* " + text + " [local]";
    }

    if (isExampleCopy(element))
    {
      return "~ " + text + " [example]";
    }

    return null;
  }

  // public Color decorateForeground(Object element)
  // {
  // if (isLocal(element))
  // {
  // return RED;
  // }
  //
  // if (isExampleCopy(element))
  // {
  // return GRAY;
  // }
  //
  // return null;
  // }
  //
  // public Color decorateBackground(Object element)
  // {
  // return null;
  // }

  private boolean isLocal(Object element)
  {
    if (element instanceof ILaunchConfiguration)
    {
      ILaunchConfiguration configuration = (ILaunchConfiguration)element;
      return configuration.isLocal();
    }

    return false;
  }

  private boolean isExampleCopy(Object element)
  {
    if (element instanceof ILaunchConfiguration)
    {
      ILaunchConfiguration configuration = (ILaunchConfiguration)element;
      IFile file = configuration.getFile();
      if (file != null)
      {
        IPath path = file.getFullPath();
        String[] segments = path.segments();
        return segments.length == 4 && segments[1].equals("examples");
      }
    }

    return false;
  }
}
