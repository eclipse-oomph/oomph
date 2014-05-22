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
package org.eclipse.oomph.targlets.internal.ui;

import org.eclipse.oomph.targlets.internal.core.TargletContainer;

import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.internal.ui.shared.target.EditBundleContainerWizard;

/**
 * @author Eike Stepper
 */
public class EditTargletWizard extends EditBundleContainerWizard
{
  private TargletWizardPage page;

  private ITargetDefinition target;

  private TargletContainer targlet;

  public EditTargletWizard(ITargetDefinition target, TargletContainer targlet)
  {
    super(target, targlet);
    this.target = target;
    this.targlet = targlet;
    setWindowTitle("Edit Targlet");
  }

  @Override
  public void addPages()
  {
    page = new TargletWizardPage(target, targlet);
    addPage(page);
  }
}
