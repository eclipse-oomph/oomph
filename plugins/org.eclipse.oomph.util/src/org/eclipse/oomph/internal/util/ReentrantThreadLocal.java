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
package org.eclipse.oomph.internal.util;

import java.util.Stack;
import java.util.concurrent.Callable;

/**
 * @author Eike Stepper
 */
public class ReentrantThreadLocal<T>
{
  private final ThreadLocal<Stack<T>> stacks = new ThreadLocal<Stack<T>>();

  public ReentrantThreadLocal()
  {
  }

  public T get()
  {
    Stack<T> stack = stacks.get();
    if (stack == null || stack.isEmpty())
    {
      return null;
    }

    return stack.peek();
  }

  public synchronized void begin(T value)
  {
    Stack<T> stack = stacks.get();
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
      stacks.set(stack);
    }

    stack.push(value);
  }

  public synchronized T end()
  {
    Stack<T> stack = stacks.get();
    if (stack != null)
    {
      int size = stack.size();
      if (size != 0)
      {
        T value = stack.pop();

        if (size == 1)
        {
          stacks.remove();

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
