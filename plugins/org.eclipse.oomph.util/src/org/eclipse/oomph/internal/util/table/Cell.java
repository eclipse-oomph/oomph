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

import java.text.Format;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Eike Stepper
 */
public final class Cell extends AbstractRange
{
  final Table table;

  int col;

  int row;

  Object value;

  Format format;

  Alignment alignment;

  Cell(Table table, int col, int row)
  {
    this.table = table;
    this.col = col;
    this.row = row;
  }

  @Override
  public Table table()
  {
    return table;
  }

  public int col()
  {
    return col;
  }

  public int row()
  {
    return row;
  }

  public Coordinate coordinate()
  {
    return new Coordinate(col, row);
  }

  @Override
  public Cell offset(int cols, int rows)
  {
    return table.cell(coordinate().offset(cols, rows));
  }

  public Object value()
  {
    if (value instanceof Formula)
    {
      Formula formula = (Formula)value;
      return formula.evaluate();
    }

    return value;
  }

  @Override
  public Cell value(Object value)
  {
    if (value instanceof Generator)
    {
      Generator generator = (Generator)value;
      value = generator.nextValue();
    }

    this.value = value;
    return this;
  }

  public Object unevaluated()
  {
    return value;
  }

  public Number number()
  {
    Object value = value();
    if (value instanceof Number)
    {
      return (Number)value;
    }

    return null;
  }

  public Formula formula()
  {
    if (value instanceof Formula)
    {
      return (Formula)value;
    }

    return null;
  }

  public Cell formula(String formula)
  {

    return this;
  }

  public Format format()
  {
    return format;
  }

  @Override
  public Range format(Format format)
  {
    this.format = format;
    return this;
  }

  public Alignment alignment()
  {
    return alignment;
  }

  @Override
  public Range alignment(Alignment alignment)
  {
    this.alignment = alignment;
    return this;
  }

  // public String format()
  // {
  // Object value = value();
  // if (formatter != null)
  // {
  // return formatter.applyFormat(value);
  // }
  //
  // if (value == null)
  // {
  // return "";
  // }
  //
  // return value.toString();
  // }

  @Override
  public Iterator<Cell> iterator()
  {
    return Collections.singleton(this).iterator();
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + col;
    result = prime * result + row;
    return result;
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

    if (Cell.class != obj.getClass())
    {
      return false;
    }

    Cell other = (Cell)obj;
    return col == other.col && row == other.row;
  }

  @Override
  public String toString()
  {
    return "Cell" + coordinate() + " = " + value; //$NON-NLS-1$ //$NON-NLS-2$
  }

  String applyFormat(Object value)
  {
    try
    {
      if (format != null)
      {
        return format.format(value);
      }
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }

    if (value == null)
    {
      return ""; //$NON-NLS-1$
    }

    return value.toString();
  }

  Alignment alignmentFor(Object value)
  {
    if (alignment == null)
    {
      if (value instanceof Number)
      {
        return Alignment.RIGHT;
      }

      return Alignment.LEFT;
    }

    return alignment;
  }

  /**
   * @author Eike Stepper
   */
  public interface Visitor
  {
    public boolean visit(Cell cell, int n) throws Exception;
  }
}
