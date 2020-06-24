/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.ui.OomphDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class AgentManagerDialog extends OomphDialog
{
  public static final String TITLE = Messages.AgentManagerDialog_title;

  public static final String MESSAGE = Messages.AgentManagerDialog_message;

  private BundlePool selectedPool;

  private AgentManagerComposite composite;

  public AgentManagerDialog(Shell parentShell)
  {
    super(parentShell, TITLE, 700, 600, P2UIPlugin.INSTANCE, true);
    setShellStyle(SWT.TITLE | SWT.MAX | SWT.RESIZE | SWT.BORDER | SWT.APPLICATION_MODAL);
  }

  public BundlePool getSelectedBundlePool()
  {
    if (composite == null)
    {
      return selectedPool;
    }

    return composite.getSelectedBundlePool();
  }

  public void setSelectedPool(BundlePool selectedElement)
  {
    selectedPool = selectedElement;
  }

  public final AgentManagerComposite getComposite()
  {
    return composite;
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return MESSAGE + "."; //$NON-NLS-1$
  }

  @Override
  protected String getImagePath()
  {
    return "wizban/AgentManager.png"; //$NON-NLS-1$
  }

  @Override
  public String getHelpPath()
  {
    return P2UIPlugin.INSTANCE.getSymbolicName() + "/html/AgentManagerHelp.html"; //$NON-NLS-1$
  }

  @Override
  protected int getContainerMargin()
  {
    return 10;
  }

  @Override
  protected void createUI(Composite parent)
  {
    getShell().setImage(P2UIPlugin.INSTANCE.getSWTImage("obj16/bundlePool")); //$NON-NLS-1$

    composite = new AgentManagerComposite(parent, SWT.NONE, selectedPool)
    {
      @Override
      protected void elementChanged(Object element)
      {
        super.elementChanged(element);
        AgentManagerDialog.this.elementChanged(element);
      }

      @Override
      protected void profilesShown(boolean profilesShown)
      {
        super.profilesShown(profilesShown);
        String message = MESSAGE + "."; //$NON-NLS-1$
        if (profilesShown)
        {
          message += " " + Messages.AgentManagerDialog_profilesShownMessage; //$NON-NLS-1$
        }

        setMessage(message);
      }
    };

    composite.setLayoutData(new GridData(GridData.FILL_BOTH));
  }

  protected void elementChanged(Object element)
  {
  }
}
