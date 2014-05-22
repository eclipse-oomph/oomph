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

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.ui.target.ITargetLocationWizard;

/**
 * @author Eike Stepper
 */
public class NewTargletWizard extends Wizard implements ITargetLocationWizard
{
  private ITargetDefinition target;

  private TargletContainer targlet;

  private TargletWizardPage page;

  public NewTargletWizard()
  {
    setWindowTitle("Add Targlet");
  }

  public void setTarget(ITargetDefinition target)
  {
    this.target = target;
  }

  @Override
  public void addPages()
  {
    page = new TargletWizardPage(target);
    addPage(page);
  }

  @Override
  public boolean performFinish()
  {
    targlet = page.getBundleContainer();
    return true;
  }

  public ITargetLocation[] getLocations()
  {
    return new ITargetLocation[] { targlet };
  }
}
