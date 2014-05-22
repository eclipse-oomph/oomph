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
package org.eclipse.oomph.internal.util.table;

import org.eclipse.oomph.util.AbstractIterator;

import java.util.Iterator;

/**
 * @author Eike Stepper
 */
final class OffsetRange extends AbstractRange
{
  final Range range;

  final int cols;

  final int rows;

  public OffsetRange(OffsetRange source)
  {
    range = source.range;
    cols = source.cols;
    rows = source.rows;
  }

  public OffsetRange(Range range, int cols, int rows)
  {
    this.range = range;
    this.cols = cols;
    this.rows = rows;
  }

  @Override
  public Table table()
  {
    return range.table();
  }

  @Override
  public Iterator<Cell> iterator()
  {
    return new OffsetIterator();
  }

  /**
   * @author Eike Stepper
   */
  private final class OffsetIterator extends AbstractIterator<Cell>
  {
    private final Iterator<Cell> delegate = range.iterator();

    @Override
    protected Object computeNextElement()
    {
      while (delegate.hasNext())
      {
        Cell cell = delegate.next();
        return cell.offset(cols, rows);
      }

      return END_OF_DATA;
    }
  }
}
