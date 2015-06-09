/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.jreinfo.ui;

import org.eclipse.oomph.jreinfo.JRE;
import org.eclipse.oomph.jreinfo.JREFilter;
import org.eclipse.oomph.ui.OomphDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.Request;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Eike Stepper
 */
public class JREDialog extends OomphDialog
{
  public static final String TITLE = "Java Virtual Machines";

  private final Request.Handler downloadHandler;

  private JREFilter filter;

  private Object selectedElement;

  private JREComposite composite;

  public JREDialog(Shell parentShell, Request.Handler downloadHandler)
  {
    super(parentShell, TITLE, 600, 500, JREInfoUIPlugin.INSTANCE, false);
    this.downloadHandler = downloadHandler;
    setShellStyle(SWT.TITLE | SWT.MAX | SWT.RESIZE | SWT.BORDER | SWT.APPLICATION_MODAL);
  }

  public JREFilter getJREFilter()
  {
    return filter;
  }

  public void setJREFilter(JREFilter filter)
  {
    this.filter = filter;

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        setMessage(getDefaultMessage());
      }
    });
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

  public final JREComposite getComposite()
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
    String message = "Select a Java ";

    if (filter != null)
    {
      message += filter.toString() + ".";
    }
    else
    {
      message += "VM.";
    }

    return message;
  }

  @Override
  protected String getImagePath()
  {
    return "library_wiz.png";
  }

  @Override
  protected int getContainerMargin()
  {
    return 5;
  }

  @Override
  protected void createUI(Composite parent)
  {
    getShell().setImage(JREInfoUIPlugin.INSTANCE.getSWTImage("jre"));

    composite = new JREComposite(parent, SWT.NONE, downloadHandler, filter, selectedElement)
    {
      @Override
      protected void elementChanged(Object element)
      {
        super.elementChanged(element);
        JREDialog.this.elementChanged(element);
      }

      @Override
      protected void doubleClicked(JRE jre)
      {
        close();
      }
    };

    composite.setLayoutData(new GridData(GridData.FILL_BOTH));
  }

  protected void elementChanged(Object element)
  {
  }
}
