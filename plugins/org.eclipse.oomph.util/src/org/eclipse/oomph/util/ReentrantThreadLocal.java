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
package org.eclipse.oomph.util;

import org.eclipse.oomph.internal.util.UtilPlugin;

import java.util.Stack;
import java.util.concurrent.Callable;

/**
 * @author Eike Stepper
 */
public class ReentrantThreadLocal<T>
{
  private final ThreadLocal<Stack<T>> OFFLINE = new ThreadLocal<Stack<T>>();

  public ReentrantThreadLocal()
  {
  }

  public T get()
  {
    Stack<T> stack = OFFLINE.get();
    if (stack == null || stack.isEmpty())
    {
      return null;
    }

    return stack.peek();
  }

  public void begin(T value)
  {
    Stack<T> stack = OFFLINE.get();
    if (stack == null)
    {
      try
      {
        init();
      }
      catch (Exception ex)
      {
        UtilPlugin.INSTANCE.log(ex);
      }

      stack = new Stack<T>();
      OFFLINE.set(stack);
    }

    stack.push(value);
  }

  public T end()
  {
    Stack<T> stack = OFFLINE.get();
    if (stack != null)
    {
      int size = stack.size();
      if (size != 0)
      {
        T value = stack.pop();

        if (size == 1)
        {
          OFFLINE.remove();

          try
          {
            done();
          }
          catch (Exception ex)
          {
            UtilPlugin.INSTANCE.log(ex);
          }
        }

        return value;
      }
    }

    return null;
  }

  public <R> R call(T value, Callable<R> callable) throws Exception
  {
    begin(value);

    try
    {
      return callable.call();
    }
    finally
    {
      end();
    }
  }

  protected void init() throws Exception
  {
  }

  protected void done() throws Exception
  {
  }
}
