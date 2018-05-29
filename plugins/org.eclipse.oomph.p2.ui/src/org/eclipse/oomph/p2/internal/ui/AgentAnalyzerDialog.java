/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.OomphDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Eike Stepper
 */
public class AgentAnalyzerDialog extends OomphDialog
{
  public static final String TITLE = "Bundle Pool Analysis";

  private final Agent agent;

  public AgentAnalyzerDialog(Shell parentShell, Agent agent)
  {
    super(parentShell, TITLE, 750, 750, P2UIPlugin.INSTANCE, false);
    this.agent = agent;

    setShellStyle(SWT.TITLE | SWT.MAX | SWT.RESIZE | SWT.BORDER | SWT.APPLICATION_MODAL);
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Analyze your bundle pools, collect garbage and repair damaged artifacts.";
  }

  @Override
  protected String getImagePath()
  {
    return "wizban/AgentAnalyzer.png";
  }

  @Override
  protected void createUI(Composite parent)
  {
    getShell().setImage(P2UIPlugin.INSTANCE.getSWTImage("obj16/bundlePool"));

    try
    {
      AgentAnalyzerComposite composite = new AgentAnalyzerComposite(parent, 10, SWT.NONE, agent);
      composite.setLayoutData(new GridData(GridData.FILL_BOTH));
    }
    catch (InvocationTargetException ex)
    {
      ErrorDialog.open(ex);
      close();
    }
    catch (InterruptedException ex)
    {
      close();
    }
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, true);
  }
}
