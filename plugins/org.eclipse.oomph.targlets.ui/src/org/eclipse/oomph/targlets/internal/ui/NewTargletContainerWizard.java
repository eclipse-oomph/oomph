/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.ui;

import org.eclipse.oomph.targlets.core.ITargletContainer;
import org.eclipse.oomph.targlets.internal.core.TargletContainer;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.ui.target.ITargetLocationWizard;

/**
 * @author Eike Stepper
 */
public class NewTargletContainerWizard extends Wizard implements ITargetLocationWizard
{
  private ITargletContainer targletContainer;

  private NewTargletContainerWizardPage page;

  public NewTargletContainerWizard()
  {
    setWindowTitle("Add Targlet Container");
  }

  public void setTarget(ITargetDefinition target)
  {
  }

  @Override
  public void addPages()
  {
    page = new NewTargletContainerWizardPage();
    addPage(page);
  }

  @Override
  public boolean performFinish()
  {
    String containerID = page.getContainerID();
    targletContainer = new TargletContainer(containerID);
    return true;
  }

  public ITargetLocation[] getLocations()
  {
    return new ITargetLocation[] { targletContainer };
  }
}
