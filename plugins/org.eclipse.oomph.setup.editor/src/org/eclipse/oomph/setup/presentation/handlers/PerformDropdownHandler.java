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

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Eike Stepper
 */
public class PerformDropdownHandler extends AbstractDropdownHandler
{
  public static final String COMMAND_ID = "org.eclipse.oomph.setup.editor.performDropdown";

  public PerformDropdownHandler()
  {
    super(COMMAND_ID);
  }

  @Override
  protected ActionDescriptor createActionDescriptor() throws Exception
  {
    PerformHandler performHandler = new PerformHandler();
    ImageDescriptor imageDescriptor = performHandler.getImageDescriptor();
    String text = performHandler.getText();
    return new ActionDescriptor(imageDescriptor, text, performHandler);
  }
}
