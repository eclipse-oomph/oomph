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

import java.util.Stack;

/**
 * @author Eike Stepper
 */
public class OfflineUtil
{
  private static final ThreadLocal<Stack<Boolean>> OFFLINE = new ThreadLocal<Stack<Boolean>>();

  public OfflineUtil()
  {
  }

  public static boolean isOffline()
  {
    Stack<Boolean> stack = OFFLINE.get();
    return stack != null && !stack.isEmpty() && stack.peek();
  }

  public static void begin(boolean offline)
  {
    Stack<Boolean> stack = OFFLINE.get();
    if (stack == null)
    {
      stack = new Stack<Boolean>();
      OFFLINE.set(stack);
    }

    stack.push(offline);
  }

  public static void end()
  {
    Stack<Boolean> stack = OFFLINE.get();
    if (stack != null)
    {
      int size = stack.size();
      if (size != 0)
      {
        stack.pop();

        if (size == 1)
        {
          OFFLINE.remove();
        }
      }
    }
  }
}
