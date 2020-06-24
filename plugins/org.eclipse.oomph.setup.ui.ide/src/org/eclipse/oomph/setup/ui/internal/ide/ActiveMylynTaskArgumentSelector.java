/*
 * Copyright (c) 2016, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.internal.ide;

import org.eclipse.emf.common.CommonPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Eike Stepper
 */
public class ActiveMylynTaskArgumentSelector
{
  @SuppressWarnings("nls")
  private static final String[] ATTRIBUTES = { "Id", "Key", "Url", "Kind", "Owner", "OwnerId", "Priority", "RepositoryUrl", "Summary", "ConnectorKind",
      "HandleIdentifier", "CreationDate", "CreationTime", "CompletionDate", "CompletionTime", "DueDate", "DueTime", "ModificationDate", "ModificationTime" };

  public String selectArgument(IStringVariable variable, Shell shell)
  {
    ILabelProvider labelProvider = new LabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        String text = (String)element;
        if ("Id".equalsIgnoreCase(text)) //$NON-NLS-1$
        {
          text += "  " + Messages.ActiveMylynTaskArgumentSelector_elementDialog_defaultElement; //$NON-NLS-1$
        }
        else
        {
          text = text.replaceAll("([a-z])([A-Z])", "$1 $2"); //$NON-NLS-1$ //$NON-NLS-2$
          text = text.replaceAll("Date", Messages.ActiveMylynTaskArgumentSelector_elementDialog_date); //$NON-NLS-1$
          text = text.replaceAll("Time", Messages.ActiveMylynTaskArgumentSelector_elementDialog_time); //$NON-NLS-1$
          text = text.replaceAll("Summary", Messages.ActiveMylynTaskArgumentSelector_elementDialog_summary); //$NON-NLS-1$
        }

        return text;
      }
    };

    ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
    dialog.setElements(ATTRIBUTES);
    dialog.setTitle(Messages.ActiveMylynTaskArgumentSelector_elementDialog_title);
    dialog.setMessage(Messages.ActiveMylynTaskArgumentSelector_elementDialog_message);
    if (dialog.open() == Window.OK)
    {
      return (String)dialog.getResult()[0];
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
        argumentSelectorInterface = CommonPlugin.loadClass("org.eclipse.debug.ui", "org.eclipse.debug.internal.ui.stringsubstitution.IArgumentSelector"); //$NON-NLS-1$ //$NON-NLS-2$
      }
      catch (ClassNotFoundException ex)
      {
        try
        {
          argumentSelectorInterface = CommonPlugin.loadClass("org.eclipse.debug.ui", "org.eclipse.debug.ui.stringsubstitution.IArgumentSelector"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        catch (ClassNotFoundException ex1)
        {
          SetupUIIDEPlugin.INSTANCE.coreException(ex);
        }
      }

      InvocationHandler invocationHandler = new InvocationHandler()
      {
        private final ActiveMylynTaskArgumentSelector activeMylynTaskArgumentSelector = new ActiveMylynTaskArgumentSelector();

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
          return activeMylynTaskArgumentSelector.selectArgument((IStringVariable)args[0], (Shell)args[1]);
        }
      };

      return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { argumentSelectorInterface }, invocationHandler);
    }
  }
}
