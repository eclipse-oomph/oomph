/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.internal.ide;

import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.CommonPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.debug.ui.StringVariableSelectionDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Ed Merks
 */
public class URIArgumentSelector
{
  public String selectArgument(IStringVariable variable, Shell shell)
  {
    URIArgumentDialog uriArgumentDialog = new URIArgumentDialog(shell);
    if (uriArgumentDialog.open() == Window.OK)
    {
      return uriArgumentDialog.getValue();
    }

    return null;
  }

  /**
   * @author Ed Merks
   */
  public static class ExtensionFactory implements IExecutableExtensionFactory
  {
    public Object create() throws CoreException
    {
      Class<?> argumentSelectorInterface = null;

      try
      {
        argumentSelectorInterface = CommonPlugin.loadClass("org.eclipse.debug.ui", "org.eclipse.debug.internal.ui.stringsubstitution.IArgumentSelector");
      }
      catch (ClassNotFoundException ex)
      {
        try
        {
          argumentSelectorInterface = CommonPlugin.loadClass("org.eclipse.debug.ui", "org.eclipse.debug.ui.stringsubstitution.IArgumentSelector");
        }
        catch (ClassNotFoundException ex1)
        {
          SetupUIIDEPlugin.INSTANCE.coreException(ex);
        }
      }

      InvocationHandler invocationHandler = new InvocationHandler()
      {
        private final URIArgumentSelector uriArgumentSelector = new URIArgumentSelector();

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
          return uriArgumentSelector.selectArgument((IStringVariable)args[0], (Shell)args[1]);
        }
      };

      return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { argumentSelectorInterface }, invocationHandler);
    }
  }

  /**
   * @author Ed Merks
   */
  private static class URIArgumentDialog extends TrayDialog
  {
    private Text text;

    private String value;

    protected URIArgumentDialog(Shell parentShell)
    {
      super(parentShell);
      setHelpAvailable(false);
    }

    public String getValue()
    {
      return value;
    }

    @Override
    protected void configureShell(Shell newShell)
    {
      super.configureShell(newShell);
      newShell.setText("Variable Selection");
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
      Composite composite = (Composite)super.createDialogArea(parent);
      composite.setLayout(new GridLayout(2, false));

      text = new Text(composite, SWT.BORDER);
      GridData gridData = UIUtil.applyGridData(text);
      gridData.grabExcessHorizontalSpace = true;
      gridData.widthHint = 400;

      createButton(composite, IDialogConstants.OPEN_ID, "Variables...", false);

      return composite;
    }

    @Override
    protected void buttonPressed(int buttonId)
    {
      if (buttonId == IDialogConstants.OPEN_ID)
      {
        StringVariableSelectionDialog stringVariableSelectionDialog = new StringVariableSelectionDialog(getShell());
        if (stringVariableSelectionDialog.open() == Window.OK)
        {
          text.insert(stringVariableSelectionDialog.getVariableExpression());
        }
      }
      else
      {
        value = text.getText();
        super.buttonPressed(buttonId);
      }
    }
  }
}
