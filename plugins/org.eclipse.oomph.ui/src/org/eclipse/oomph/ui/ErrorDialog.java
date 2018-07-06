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
package org.eclipse.oomph.ui;

import org.eclipse.oomph.util.StringUtil;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Eike Stepper
 */
public class ErrorDialog extends MessageDialog
{
  private static final int TEXT_LINE_COUNT = 15;

  private final Throwable throwable;

  private final int detailsButtonID;

  private Text text;

  private ErrorDialog(String title, Throwable throwable)
  {
    this(title, throwable, 0, 1, IDialogConstants.OK_LABEL, IDialogConstants.SHOW_DETAILS_LABEL);
  }

  protected ErrorDialog(String title, Throwable throwable, int defaultButtonID, int detailsButtonID, String... dialogButtonLabels)
  {
    super(null, title, null, "Internal error:" + StringUtil.NL + getMessage(throwable), MessageDialog.ERROR, dialogButtonLabels, defaultButtonID);
    this.throwable = throwable;
    this.detailsButtonID = detailsButtonID;
    setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.APPLICATION_MODAL);
  }

  protected void createDropDownText(Composite parent)
  {
    text = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    text.setFont(parent.getFont());

    StringWriter stringWriter = new StringWriter();
    PrintWriter out = new PrintWriter(stringWriter);
    throwable.printStackTrace(out);
    out.close();
    text.setText(stringWriter.toString());

    GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
    data.heightHint = text.getLineHeight() * TEXT_LINE_COUNT;
    data.horizontalSpan = 2;
    text.setLayoutData(data);
  }

  @Override
  protected void buttonPressed(int buttonId)
  {
    if (buttonId == detailsButtonID)
    {
      toggleDetailsArea();
    }
    else
    {
      super.buttonPressed(buttonId);
    }
  }

  private void toggleDetailsArea()
  {
    Point windowSize = getShell().getSize();
    Point oldSize = getContents().computeSize(SWT.DEFAULT, SWT.DEFAULT);

    if (text != null)
    {
      text.dispose();
      text = null;
      getButton(detailsButtonID).setText(IDialogConstants.SHOW_DETAILS_LABEL);
    }
    else
    {
      createDropDownText((Composite)getContents());
      getButton(detailsButtonID).setText(IDialogConstants.HIDE_DETAILS_LABEL);
    }

    Point newSize = getContents().computeSize(SWT.DEFAULT, SWT.DEFAULT);
    getShell().setSize(new Point(windowSize.x, windowSize.y + newSize.y - oldSize.y));
  }

  private static String getMessage(Throwable t)
  {
    try
    {
      String message = getMessageRecursively(t);
      if (!StringUtil.isEmpty(message))
      {
        return message;
      }
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    return t.getClass().getSimpleName();
  }

  private static String getMessageRecursively(Throwable t)
  {
    String message = t.getMessage();
    if (StringUtil.isEmpty(message))
    {
      Throwable cause = t.getCause();
      if (cause != null)
      {
        return getMessageRecursively(cause);
      }
    }

    return message;
  }

  private static void openWithDetail(Throwable detail)
  {
    ErrorDialog dialog = new ErrorDialog("Error", detail);
    dialog.open();
  }

  public static void open(final Throwable detail)
  {
    if (Display.getCurrent() == Display.getDefault())
    {
      openWithDetail(detail);
    }
    else
    {
      Display.getDefault().syncExec(new Runnable()
      {
        public void run()
        {
          openWithDetail(detail);
        }
      });
    }
  }

}
