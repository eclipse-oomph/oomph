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

import org.eclipse.oomph.ui.AbstractDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class AgentManagerDialog extends AbstractDialog
{
  public static final String TITLE = "Bundle Pool Management";

  private Object selectedElement;

  private AgentManagerComposite composite;

  public AgentManagerDialog(Shell parentShell)
  {
    super(parentShell, TITLE, 500, 350, P2UIPlugin.INSTANCE, null);
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

    GridLayout layout = (GridLayout)composite.getLayout();
    layout.marginWidth = 10;
    layout.marginHeight = 10;
  }

  protected void elementChanged(Object element)
  {
  }
}
