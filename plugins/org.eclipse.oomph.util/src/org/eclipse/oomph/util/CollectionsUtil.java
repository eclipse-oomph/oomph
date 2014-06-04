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

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public final class CollectionsUtil
{
  private CollectionsUtil()
  {
  }

  public static <K, V> void add(Map<K, Set<V>> map, K key, V value)
  {
    Set<V> set = map.get(key);
    if (set == null)
    {
      set = new LinkedHashSet<V>();
      map.put(key, set);
    }

    set.add(value);
  }

  public static <K, V> void addAll(Map<K, Set<V>> map, K key, Collection<? extends V> values)
  {
    Set<V> set = map.get(key);
    if (set == null)
    {
      set = new LinkedHashSet<V>();
      map.put(key, set);
    }

    set.addAll(values);
  }

  public static <K, V> void addAll(Map<K, Set<V>> map, Map<? extends K, ? extends Collection<? extends V>> map2)
  {
    for (Map.Entry<? extends K, ? extends Collection<? extends V>> entry : map2.entrySet())
    {
      addAll(map, entry.getKey(), entry.getValue());
    }
  }

}
