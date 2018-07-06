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
package org.eclipse.oomph.internal.util.table;

import org.eclipse.oomph.internal.util.table.Cell.Visitor;

import java.text.Format;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public interface Range extends Iterable<Cell>
{
  public Table table();

  public Set<Cell> set();

  public List<Cell> list();

  public int accept(Visitor visitor) throws Exception;

  public boolean contains(int col, int row);

  public boolean contains(Coordinate coordinate);

  public boolean contains(Cell cell);

  public boolean contains(Range range);

  public Range offset(int cols, int rows);

  public Range addRange(Coordinate coordinate1, Coordinate coordinate2);

  public Range addRange(int col1, int row1, int col2, int row2);

  public Range addRanges(Range... ranges);

  public Range subtractRange(Coordinate coordinate1, Coordinate coordinate2);

  public Range subtractRange(int col1, int row1, int col2, int row2);

  public Range subtractRanges(Range... ranges);

  public Range value(Object value);

  public Range format(Format format);

  public Range alignment(Alignment alignment);

  /**
   * @author Eike Stepper
   */
  public enum Alignment
  {
    LEFT, CENTER, RIGHT;

    public String apply(String value, int width)
    {
      int length = value.length();
      int pad = width - length;
      if (pad == 0)
      {
        return value;
      }

      int padLeft = 0;
      int padRight = 0;

      switch (this)
      {
        case LEFT:
          padRight = pad;
          break;

        case CENTER:
          padLeft = pad / 2;
          padRight = pad - padLeft;
          break;

        case RIGHT:
          padLeft = pad;
          break;
      }

      StringBuilder builder = new StringBuilder(width);

      if (padLeft > 0)
      {
        pad(builder, padLeft);
      }

      builder.append(value);

      if (padRight > 0)
      {
        pad(builder, padRight);
      }

      return builder.toString();
    }

    private static void pad(StringBuilder builder, int spaces)
    {
      for (int i = 0; i < spaces; i++)
      {
        builder.append(' ');
      }
    }
  }
}
