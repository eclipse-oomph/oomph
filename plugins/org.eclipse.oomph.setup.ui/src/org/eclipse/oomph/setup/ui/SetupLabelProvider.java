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
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.setup.SetupTask;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

/**
 * @author Eike Stepper
 */
public class SetupLabelProvider extends AdapterFactoryLabelProvider.FontAndColorProvider
{
  private final Color DARK_GRAY;

  public SetupLabelProvider(Viewer viewer)
  {
    this(BaseEditUtil.createAdapterFactory(), viewer);
  }

  public SetupLabelProvider(AdapterFactory adapterFactory, Viewer viewer)
  {
    super(adapterFactory, viewer);
    DARK_GRAY = viewer.getControl().getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
  }

  @Override
  public Color getForeground(Object object)
  {
    if (isDisabled(object))
    {
      return DARK_GRAY;
    }

    return super.getForeground(object);
  }

  public static boolean isDisabled(Object object)
  {
    if (object instanceof EObject)
    {
      EObject eObject = (EObject)object;
      if (eObject instanceof SetupTask)
      {
        SetupTask setupTask = (SetupTask)eObject;
        if (setupTask.isDisabled())
        {
          return true;
        }
      }

      EObject eContainer = eObject.eContainer();
      if (eContainer != null)
      {
        return isDisabled(eContainer);
      }
    }

    return false;
  }

  @Override
  public Font getFont(Object object)
  {
    Font font = super.getFont(object);
    return font == null ? getDefaultFont() : font;
  }
}
