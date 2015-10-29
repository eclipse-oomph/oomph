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
package org.eclipse.oomph.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.EList;

import java.util.Iterator;

/**
 * An instance of this {@link Adapter} attached to a {@link Notifier} serves as a hint that the notifier should be suppressed from the UI.
 *
 * @author Eike Stepper
 */
public class SuppressHint extends AdapterImpl
{
  public static final SuppressHint INSTANCE = new SuppressHint();

  protected SuppressHint()
  {
  }

  public static boolean isSuppressed(Object object)
  {
    if (object instanceof Notifier)
    {
      Notifier notifier = (Notifier)object;
      EList<Adapter> adapters = notifier.eAdapters();

      for (Adapter adapter : adapters)
      {
        if (adapter instanceof SuppressHint)
        {
          return true;
        }
      }
    }

    return false;
  }

  public static void setSuppressed(Object object, boolean suppressed)
  {
    if (object instanceof Notifier)
    {
      Notifier notifier = (Notifier)object;
      EList<Adapter> adapters = notifier.eAdapters();

      if (suppressed)
      {
        for (Adapter adapter : notifier.eAdapters())
        {
          if (adapter instanceof SuppressHint)
          {
            return;
          }
        }

        adapters.add(INSTANCE);
      }
      else
      {
        for (Iterator<Adapter> it = adapters.iterator(); it.hasNext();)
        {
          Adapter adapter = it.next();
          if (adapter instanceof SuppressHint)
          {
            it.remove();
          }
        }
      }
    }
  }
}
