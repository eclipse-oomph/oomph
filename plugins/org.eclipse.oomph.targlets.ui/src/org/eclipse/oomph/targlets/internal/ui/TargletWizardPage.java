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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.internal.ui.SWTFactory;
import org.eclipse.pde.internal.ui.shared.target.IEditBundleContainerPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class TargletWizardPage extends WizardPage implements IEditBundleContainerPage
{
  private ITargetDefinition target;

  private TargletContainer targletContainer;

  /**
   * Constructor for creating a new container
   */
  public TargletWizardPage(ITargetDefinition target)
  {
    super("AddTargletContainer");
    setTitle("Add Targlet Container");
    setMessage("Select content from a software site to be added to your target.");
    this.target = target;

    Set<String> ids = new HashSet<String>();
    ITargetLocation[] targetLocations = target.getTargetLocations();
    if (targetLocations != null)
    {
      for (ITargetLocation location : targetLocations)
      {
        if (location instanceof TargletContainer)
        {
          TargletContainer targletContainer = (TargletContainer)location;
          ids.add(targletContainer.getID());
        }
      }
    }

    String id = "Default";
    int i = 1;
    while (ids.contains(id))
    {
      id = "Default" + (++i);
    }

    targletContainer = new TargletContainer(id);
  }

  /**
   * Constructor for editing an existing container
   */
  public TargletWizardPage(ITargetDefinition target, TargletContainer targletContainer)
  {
    super("EditTargletContainer");
    setTitle("Edit Targlet Container");
    setMessage("Select content from a software site to be added to your target.");
    this.target = target;
    this.targletContainer = targletContainer;
  }

  public TargletContainer getBundleContainer()
  {
    return targletContainer;
  }

  public void storeSettings()
  {
  }

  public void createControl(Composite parent)
  {
    Composite composite = SWTFactory.createComposite(parent, 1, 1, GridData.FILL_BOTH, 0, 0);

    Label label = new Label(composite, SWT.NONE);
    label.setText("Targlet Info");
    label.setLayoutData(new GridData(GridData.FILL_BOTH));

    setControl(composite);
  }

  void pageChanged()
  {
    setErrorMessage(null);
    setPageComplete(true);
  }
}
