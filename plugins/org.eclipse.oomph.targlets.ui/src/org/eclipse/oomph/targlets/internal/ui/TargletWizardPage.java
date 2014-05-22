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

import org.eclipse.equinox.p2.metadata.IVersionedId;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionedId;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.internal.ui.SWTFactory;
import org.eclipse.pde.internal.ui.shared.target.IEditBundleContainerPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.io.File;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("unused")
public class TargletWizardPage extends WizardPage implements IEditBundleContainerPage
{
  private ITargetDefinition target;

  private TargletContainer targlet;

  /**
   * Constructor for creating a new container
   */
  public TargletWizardPage(ITargetDefinition target)
  {
    super("AddP2Container");
    setTitle("Add Targlet");
    setMessage("Select content from a software site to be added to your target.");
    this.target = target;
  }

  /**
   * Constructor for editing an existing container
   */
  public TargletWizardPage(ITargetDefinition target, TargletContainer targlet)
  {
    this(target);
    this.targlet = targlet;
    setTitle("Edit Targlet");
  }

  public TargletContainer getBundleContainer()
  {
    try
    {
      File p2PoolDir = new File("C:/develop/.p2pool-ide");
      File p2AgentDir = new File("C:/develop/.p2pool-ide/p2");
      String profileID = "C__develop_aaa_cdo.releng_master_tpX";
      String destination = "C:/develop/aaa/cdo.releng/master/tpX"; // XXX

      java.net.URI[] p2Repositories = { new java.net.URI("http://download.eclipse.org/releases/luna") };
      IVersionedId[] rootComponents = { new VersionedId("org.eclipse.emf.ecore.feature.group", Version.emptyVersion) };

      return null;
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
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

  /**
   * Checks if the page is complete, updating messages and finish button.
   */
  void pageChanged()
  {
    setErrorMessage(null);
    setPageComplete(true);
  }

  /**
   * Restores the state of the wizard from previous invocations
   */
  private void restoreWidgetState()
  {
  }
}
