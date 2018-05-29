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

import java.text.Format;
import java.util.Iterator;

/**
 * @author Eike Stepper
 */
public class RectangularRange extends AbstractRange
{
  final Table table;

  Coordinate topLeft;

  Coordinate bottomRight;

  RectangularRange(Table table, Coordinate coordinate1, Coordinate coordinate2)
  {
    this.table = table;

    int row1, row2;
    if (coordinate1.row <= coordinate2.row)
    {
      row1 = coordinate1.row;
      row2 = coordinate2.row;
    }
    else
    {
      row1 = coordinate2.row;
      row2 = coordinate1.row;
    }

    int col1, col2;
    if (coordinate1.col <= coordinate2.col)
    {
      col1 = coordinate1.col;
      col2 = coordinate2.col;
    }
    else
    {
      col1 = coordinate2.col;
      col2 = coordinate1.col;
    }

    topLeft = new Coordinate(col1, row1);
    bottomRight = new Coordinate(col2, row2);

    if (table != null)
    {
      // Make sure that the table is large enough
      table.cell(bottomRight);
    }
  }

  @Override
  public Table table()
  {
    return table;
  }

  public Coordinate topLeft()
  {
    return topLeft;
  }

  public Coordinate bottomRight()
  {
    return bottomRight;
  }

  public int cols()
  {
    return bottomRight.col - topLeft.col + 1;
  }

  public int rows()
  {
    return bottomRight.row - topLeft.row + 1;
  }

  @Override
  public boolean contains(int col, int row)
  {
    return topLeft.col <= col && col <= bottomRight.col && topLeft.row <= row && row <= bottomRight.row;
  }

  @Override
  public boolean contains(Range range)
  {
    if (range instanceof RectangularRange)
    {
      RectangularRange rect = (RectangularRange)range;
      return topLeft.col <= rect.topLeft.col && rect.bottomRight.col <= bottomRight.col && topLeft.row <= rect.topLeft.row
          && rect.bottomRight.row <= bottomRight.row;
    }

    return super.contains(range);
  }

  @Override
  public Iterator<Cell> iterator()
  {
    return new CellIterator();
  }

  @Override
  public RectangularRange value(Object value)
  {
    return (RectangularRange)super.value(value);
  }

  @Override
  public RectangularRange format(Format format)
  {
    return (RectangularRange)super.format(format);
  }

  @Override
  public RectangularRange alignment(Alignment alignment)
  {
    return (RectangularRange)super.alignment(alignment);
  }

  /**
   * @author Eike Stepper
   */
  private final class CellIterator extends AbstractIterator<Cell>
  {
    private int col = topLeft.col;

    private int row = topLeft.row;

    @Override
    protected Object computeNextElement()
    {
      try
      {
        if (col > bottomRight.col)
        {
          if (++row > bottomRight.row)
          {
            return END_OF_DATA;
          }

          col = topLeft.col;
        }

        return table().cell(col, row);
      }
      finally
      {
        ++col;
      }
    }
  }
}
