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

import org.eclipse.oomph.targlets.internal.core.TargletContainerDescriptorManager;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.internal.ui.SWTFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.util.Set;

/**
 * @author Eike Stepper
 */
public class NewTargletContainerWizardPage extends WizardPage
{
  private static final String DEFAULT_ID_PREFIX = "Default"; //$NON-NLS-1$

  private String containerID = DEFAULT_ID_PREFIX;

  public NewTargletContainerWizardPage()
  {
    super("AddTargletContainer"); //$NON-NLS-1$
    setTitle(Messages.NewTargletContainerWizardPage_title);
    setMessage(Messages.NewTargletContainerWizardPage_message);
  }

  public String getContainerID()
  {
    return containerID;
  }

  @Override
  public void createControl(Composite parent)
  {
    final Set<String> ids = TargletContainerDescriptorManager.getContainerIDs(new NullProgressMonitor());

    int i = 1;
    while (ids.contains(containerID))
    {
      containerID = DEFAULT_ID_PREFIX + (++i);
    }

    Composite composite = SWTFactory.createComposite(parent, 2, 1, GridData.FILL_BOTH, 0, 0);

    Label label = new Label(composite, SWT.NONE);
    label.setText(Messages.NewTargletContainerWizardPage_targletContainerId);
    label.setLayoutData(new GridData());

    final Text text = new Text(composite, SWT.BORDER);
    text.setText(containerID);
    text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    text.addModifyListener(new ModifyListener()
    {
      @Override
      public void modifyText(ModifyEvent e)
      {
        containerID = text.getText();
        if (containerID.trim().length() == 0)
        {
          setErrorMessage(Messages.NewTargletContainerWizardPage_error_containerIdNotPresent);
          setPageComplete(false);
          return;
        }

        if (ids.contains(containerID))
        {
          setErrorMessage(Messages.NewTargletContainerWizardPage_error_containerIdNotUnique);
          setPageComplete(false);
          return;
        }

        setErrorMessage(null);
        setPageComplete(true);
      }
    });

    setControl(composite);
  }

  public void storeSettings()
  {
  }
}
