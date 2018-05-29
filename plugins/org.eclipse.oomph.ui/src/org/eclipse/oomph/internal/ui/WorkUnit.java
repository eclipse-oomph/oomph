/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.swt.widgets.Display;

public abstract class WorkUnit<R, E extends Throwable>
{
  private final Class<E> exceptionType;

  private R result;

  private Throwable throwable;

  protected WorkUnit()
  {
    this(null);
  }

  protected WorkUnit(Class<E> exceptionType)
  {
    this.exceptionType = exceptionType;
  }

  protected abstract R doExecute() throws E;

  public R execute() throws E
  {
    return execute(UIUtil.getDisplay());
  }

  public R execute(final Display display) throws E
  {
    if (display.isDisposed())
    {
      return null;
    }

    display.syncExec(new Runnable()
    {
      public void run()
      {
        if (display.isDisposed())
        {
          return;
        }

        try
        {
          result = doExecute();
        }
        catch (Throwable ex)
        {
          throwable = ex;
        }
      }
    });

    rethrow();
    return result;
  }

  protected void rethrow() throws E
  {
    if (throwable != null)
    {
      if (exceptionType == null)
      {
        if (throwable instanceof RuntimeException)
        {
          throw (RuntimeException)throwable;
        }

        throw new RuntimeException(throwable);
      }

      throw exceptionType.cast(throwable);
    }
  }

  public static abstract class Void<E extends Throwable> extends WorkUnit<Object, E>
  {
    public Void()
    {
      this(null);
    }

    public Void(Class<E> exceptionType)
    {
      super(exceptionType);
    }

    @Override
    protected final Object doExecute() throws E
    {
      doProcess();
      return null;
    }

    protected abstract void doProcess() throws E;

    public void process() throws E
    {
      execute();
    }
  }
}
