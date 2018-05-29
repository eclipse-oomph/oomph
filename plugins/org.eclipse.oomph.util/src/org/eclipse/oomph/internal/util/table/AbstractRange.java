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

import org.eclipse.oomph.internal.util.table.Cell.Visitor;

import java.text.Format;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public abstract class AbstractRange implements Range
{
  public abstract Table table();

  public abstract Iterator<Cell> iterator();

  public Set<Cell> set()
  {
    Set<Cell> set = new HashSet<Cell>();
    fillCells(set);
    return set;
  }

  public List<Cell> list()
  {
    List<Cell> list = new ArrayList<Cell>();
    fillCells(list);
    return list;
  }

  public int accept(Visitor visitor) throws Exception
  {
    int n = 0;
    for (Cell cell : this)
    {
      if (!visitor.visit(cell, n++))
      {
        break;
      }
    }

    return n;
  }

  public boolean contains(int col, int row)
  {
    for (Cell cell : this)
    {
      if (cell.col == col && cell.row == row)
      {
        return true;
      }
    }

    return false;
  }

  public boolean contains(Coordinate coordinate)
  {
    return contains(coordinate.col, coordinate.row);
  }

  public boolean contains(Cell cell)
  {
    return contains(cell.col, cell.row);
  }

  public boolean contains(Range range)
  {
    for (Cell cell : range)
    {
      if (!contains(cell))
      {
        return false;
      }
    }

    return true;
  }

  public Range offset(int cols, int rows)
  {
    return new OffsetRange(this, cols, rows);
  }

  public Range addRange(Coordinate coordinate1, Coordinate coordinate2)
  {
    return addRanges(table().range(coordinate1, coordinate2));
  }

  public Range addRange(int col1, int row1, int col2, int row2)
  {
    return addRange(new Coordinate(col1, row1), new Coordinate(col2, row2));
  }

  public Range addRanges(Range... ranges)
  {
    Range result = new ComposedRange(table(), this);
    return result.addRanges(ranges);
  }

  public Range subtractRange(Coordinate coordinate1, Coordinate coordinate2)
  {
    return subtractRanges(table().range(coordinate1, coordinate2));
  }

  public Range subtractRange(int col1, int row1, int col2, int row2)
  {
    return subtractRange(new Coordinate(col1, row1), new Coordinate(col2, row2));
  }

  public Range subtractRanges(Range... ranges)
  {
    Range result = new ComposedRange(table(), this);
    return result.subtractRanges(ranges);
  }

  public Range value(Object value)
  {
    for (Cell cell : this)
    {
      cell.value(value);
    }

    return this;
  }

  public Range format(Format format)
  {
    for (Cell cell : this)
    {
      cell.format(format);
    }

    return this;
  }

  public Range alignment(Alignment alignment)
  {
    for (Cell cell : this)
    {
      cell.alignment(alignment);
    }

    return this;
  }

  @Override
  public int hashCode()
  {
    return set().hashCode();
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    Range other = (Range)obj;
    return set().equals(other.set());
  }

  private void fillCells(Collection<Cell> cells)
  {
    for (Cell cell : this)
    {
      cells.add(cell);
    }
  }
}
