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
package org.eclipse.oomph.p2.internal.ui;

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
  public static final String TITLE = "Bundle Pool Management";

  private Object selectedElement;

  private AgentManagerComposite composite;

  public AgentManagerDialog(Shell parentShell)
  {
    super(parentShell, TITLE, 500, 350, P2UIPlugin.INSTANCE, false);
    setShellStyle(SWT.TITLE | SWT.MAX | SWT.RESIZE | SWT.BORDER | SWT.APPLICATION_MODAL);
  }

  public Object getSelectedElement()
  {
    if (composite == null)
    {
      return selectedElement;
    }

    return composite.getSelectedElement();
  }

  public void setSelectedElement(Object selectedElement)
  {
    this.selectedElement = selectedElement;
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
    return "Manage your p2 agents and bundle pools.";
  }

  @Override
  protected String getImagePath()
  {
    return "wizban/AgentManager";
  }

  @Override
  protected int getContainerMargin()
  {
    return 10;
  }

  @Override
  protected void createUI(Composite parent)
  {
    getShell().setImage(P2UIPlugin.INSTANCE.getSWTImage("obj16/agent"));

    composite = new AgentManagerComposite(parent, SWT.NONE, selectedElement)
    {
      @Override
      protected void elementChanged(Object element)
      {
        super.elementChanged(element);
        AgentManagerDialog.this.elementChanged(element);
      }
    };

    composite.setLayoutData(new GridData(GridData.FILL_BOTH));
  }

  protected void elementChanged(Object element)
  {
  }
}
