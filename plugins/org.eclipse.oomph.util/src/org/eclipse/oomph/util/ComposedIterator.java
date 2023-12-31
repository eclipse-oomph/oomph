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
package org.eclipse.oomph.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * An iterator that is composed of multiple delegate iterators.
 *
 * @author Eike Stepper
 */
public class ComposedIterator<T> extends AbstractIterator<T>
{
  private final Iterator<? extends Iterator<T>> delegates;

  private Iterator<T> currentDelegate;

  @SafeVarargs
  public ComposedIterator(Iterator<T>... delegates)
  {
    this(Arrays.asList(delegates));
  }

  public ComposedIterator(Collection<? extends Iterator<T>> delegates)
  {
    this(delegates.iterator());
  }

  public ComposedIterator(Iterator<? extends Iterator<T>> delegates)
  {
    this.delegates = delegates;
  }

  @Override
  protected Object computeNextElement()
  {
    if (currentDelegate == null)
    {
      if (!delegates.hasNext())
      {
        return END_OF_DATA;
      }

      currentDelegate = delegates.next();
    }

    if (currentDelegate.hasNext())
    {
      return currentDelegate.next();
    }

    currentDelegate = null;
    return computeNextElement();
  }

  public static <T> Iterator<T> fromIterables(Collection<? extends Iterable<T>> iterables)
  {
    List<Iterator<T>> iterators = new ArrayList<>();
    for (Iterable<T> iterable : iterables)
    {
      iterators.add(iterable.iterator());
    }

    return new ComposedIterator<>(iterators.iterator());
  }
}
