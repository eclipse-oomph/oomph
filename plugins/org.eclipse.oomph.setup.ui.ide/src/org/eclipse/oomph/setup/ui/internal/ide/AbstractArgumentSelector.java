/*
 * Copyright (c) 2024 Eclipse contributors and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.setup.ui.internal.ide;

import org.eclipse.emf.common.CommonPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.swt.widgets.Shell;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class AbstractArgumentSelector
{
  public abstract String selectArgument(IStringVariable variable, Shell shell);

  public abstract static class AbstractExtensionFactory implements IExecutableExtensionFactory
  {

    protected abstract AbstractArgumentSelector createSelector();

    @Override
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
        private final AbstractArgumentSelector selector = createSelector();

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
          return selector.selectArgument((IStringVariable)args[0], (Shell)args[1]);
        }
      };

      return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { argumentSelectorInterface }, invocationHandler);
    }
  }
}
