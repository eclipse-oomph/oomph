/*
 * Copyright (c) 2019 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.oomph.internal.ui.UIPlugin;

import org.eclipse.emf.common.ui.CommonUIPlugin;
import org.eclipse.emf.common.ui.DiagnosticComposite;
import org.eclipse.emf.common.ui.dialogs.DiagnosticDialog;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Ed Merks
 */
public class StatusDialog extends DiagnosticDialog
{
  private final Diagnostic diagnostic;

  public StatusDialog(Shell parentShell, String dialogTitle, String message, Diagnostic diagnostic, int severityMask)
  {
    super(parentShell, dialogTitle, message, diagnostic, severityMask);
    this.diagnostic = diagnostic;
  }

  public StatusDialog(Shell parentShell, String dialogTitle, String message, IStatus status, int severityMask)
  {
    this(parentShell, dialogTitle, message, BasicDiagnostic.toDiagnostic(status), severityMask);
  }

  @Override
  protected DiagnosticComposite createDiagnosticComposite(Composite parent)
  {
    DiagnosticComposite diagnosticComposite = new DiagnosticComposite(parent, SWT.NONE)
    {
      @Override
      protected ILabelProvider createLabelProvider()
      {
        return new LabelProvider()
        {
          @Override
          public String getText(Object element)
          {
            Diagnostic diagnostic = (Diagnostic)element;
            String message = diagnostic.getMessage();
            if (message == null)
            {
              switch (diagnostic.getSeverity())
              {
                case Diagnostic.ERROR:
                  message = CommonUIPlugin.getPlugin().getString("_UI_DiagnosticError_label"); //$NON-NLS-1$
                  break;
                case Diagnostic.WARNING:
                  message = CommonUIPlugin.getPlugin().getString("_UI_DiagnosticWarning_label"); //$NON-NLS-1$
                  break;
                default:
                  message = CommonUIPlugin.getPlugin().getString("_UI_Diagnostic_label"); //$NON-NLS-1$
                  break;
              }
            }
            return message;
          }

          @Override
          public Image getImage(Object element)
          {
            Diagnostic diagnostic = (Diagnostic)element;
            switch (diagnostic.getSeverity())
            {
              case Diagnostic.ERROR:
                return UIPlugin.INSTANCE.getSWTImage("error"); //$NON-NLS-1$
              case Diagnostic.CANCEL:
              case Diagnostic.WARNING:
                return UIPlugin.INSTANCE.getSWTImage("warning"); //$NON-NLS-1$
              case Diagnostic.OK:
              case Diagnostic.INFO:
                return UIPlugin.INSTANCE.getSWTImage("info"); //$NON-NLS-1$
            }
            return null;
          }
        };
      }
    };

    GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL | GridData.GRAB_VERTICAL);
    data.horizontalSpan = 2;
    data.heightHint = 200;
    diagnosticComposite.setLayoutData(data);
    if (getTextProvider() != null)
    {
      diagnosticComposite.setTextProvider(getTextProvider());
    }
    diagnosticComposite.initialize(null);

    UIUtil.asyncExec(diagnosticComposite, new Runnable()
    {
      public void run()
      {
        setDiagnostic(diagnostic);
      }
    });

    return diagnosticComposite;
  }
}
