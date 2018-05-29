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
package org.eclipse.oomph.internal.util.table;

import org.eclipse.oomph.util.AbstractIterator;
import org.eclipse.oomph.util.ComposedIterator;
import org.eclipse.oomph.util.PredicateIterator;
import org.eclipse.oomph.util.Predicates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
final class ComposedRange extends AbstractRange
{
  final Table table;

  final List<Range> inclusions = new ArrayList<Range>();

  final List<Range> exclusions = new ArrayList<Range>();

  public ComposedRange(ComposedRange source)
  {
    table = source.table;
    inclusions.addAll(source.inclusions);
    exclusions.addAll(source.exclusions);
  }

  public ComposedRange(Table table, Range... ranges)
  {
    this.table = table;
    addRanges(ranges);
  }

  @Override
  public Range addRanges(Range... ranges)
  {
    addRanges(inclusions, ranges);
    return this;
  }

  @Override
  public Range subtractRanges(Range... ranges)
  {
    addRanges(exclusions, ranges);
    return this;
  }

  @Override
  public Table table()
  {
    return table;
  }

  @Override
  public Iterator<Cell> iterator()
  {
    if (inclusions.isEmpty())
    {
      return AbstractIterator.empty();
    }

    Iterator<Cell> iterator = ComposedIterator.fromIterables(inclusions);
    if (!exclusions.isEmpty())
    {
      Set<Cell> excludedCells = new HashSet<Cell>();
      for (Range range : exclusions)
      {
        for (Cell cell : range)
        {
          excludedCells.add(cell);
        }
      }

      iterator = new PredicateIterator<Cell>(Predicates.excluded(excludedCells), iterator);
    }

    return new PredicateIterator<Cell>(Predicates.unique(), iterator);
  }

  private static void addRanges(List<Range> list, Range... ranges)
  {
    for (int i = 0; i < ranges.length; i++)
    {
      Range range = ranges[i];
      list.add(range);
    }
  }
}
