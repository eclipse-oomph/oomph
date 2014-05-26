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
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Eike Stepper
 */
public abstract class AbstractDropdownItemHandler extends AbstractHandler implements Runnable
{
  private final String imageKey;

  private final String text;

  public AbstractDropdownItemHandler(String imageKey, String text)
  {
    this.imageKey = imageKey;
    this.text = text;
  }

  public ImageDescriptor getImageDescriptor()
  {
    return SetupEditorPlugin.INSTANCE.getImageDescriptor(imageKey);
  }

  public String getText()
  {
    return text;
  }

  public final Object execute(ExecutionEvent event) throws ExecutionException
  {
    PerformDropdownHandler.setAction(PerformDropdownHandler.COMMAND_ID, getImageDescriptor(), text, this);

    run();
    return null;
  }
}
