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

/**
 * @author Eike Stepper
 */
public class Pair<T1, T2>
{
  private T1 element1;

  private T2 element2;

  public Pair()
  {
  }

  public Pair(T1 element1, T2 element2)
  {
    this.element1 = element1;
    this.element2 = element2;
  }

  public Pair(Pair<T1, T2> source)
  {
    element1 = source.element1;
    element2 = source.element2;
  }

  public final T1 getElement1()
  {
    return element1;
  }

  public void setElement1(T1 element1)
  {
    this.element1 = element1;
  }

  public final T2 getElement2()
  {
    return element2;
  }

  public void setElement2(T2 element2)
  {
    this.element2 = element2;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj instanceof Pair<?, ?>)
    {
      Pair<?, ?> that = (Pair<?, ?>)obj;
      return ObjectUtil.equals(element1, that.getElement1()) && ObjectUtil.equals(element2, that.getElement2());
    }

    return false;
  }

  @Override
  public int hashCode()
  {
    return ObjectUtil.hashCode(element1) ^ ObjectUtil.hashCode(element2);
  }

  @Override
  public String toString()
  {
    return "Pair[" + element1 + ", " + element2 + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  public Pair<T1, T2> copy()
  {
    return new Pair<>(this);
  }

  public static <T1, T2> Pair<T1, T2> create()
  {
    return new Pair<>();
  }

  public static <T1, T2> Pair<T1, T2> create(T1 element1, T2 element2)
  {
    return new Pair<>(element1, element2);
  }
}
