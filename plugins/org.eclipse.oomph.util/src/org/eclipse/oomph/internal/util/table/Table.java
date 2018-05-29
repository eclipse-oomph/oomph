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

import org.eclipse.oomph.internal.util.table.Formula.Percent;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * @author Eike Stepper
 */
public class Table extends RectangularRange
{
  Cell[][] cells;

  public Table(int cols, int rows)
  {
    super(null, Coordinate.ZERO, new Coordinate(cols - 1, rows - 1));
    cells = new Cell[cols][];
    for (int col = 0; col < cols; col++)
    {
      cells[col] = new Cell[rows];
      for (int row = 0; row < rows; row++)
      {
        cells[col][row] = new Cell(this, col, row);
      }
    }
  }

  public Table(int cols)
  {
    this(cols, 1);
  }

  public Table()
  {
    this(1);
  }

  @Override
  public Table table()
  {
    return this;
  }

  public Cell cell(Coordinate coordinate)
  {
    return cell(coordinate.col, coordinate.row);
  }

  public Cell cell(int col, int row)
  {
    for (int c = cols(); c <= col; c++)
    {
      addColumn();
    }

    for (int r = rows(); r <= row; r++)
    {
      addRow();
    }

    return cells[col][row];
  }

  public RectangularRange range(Coordinate coordinate1, Coordinate coordinate2)
  {
    return new RectangularRange(this, coordinate1, coordinate2);
  }

  public RectangularRange range(int col1, int row1, int col2, int row2)
  {
    return range(new Coordinate(col1, row1), new Coordinate(col2, row2));
  }

  public RectangularRange column(int col)
  {
    return column(col, 0);
  }

  public RectangularRange column(int col, int skipTopRows)
  {
    return column(col, skipTopRows, 0);
  }

  public RectangularRange column(int col, int skipTopRows, int skipBottomRows)
  {
    return range(new Coordinate(col, skipTopRows), new Coordinate(col, bottomRight.row - skipBottomRows));
  }

  public Table addColumn(int index)
  {
    int cols = cells.length;
    addColumn();

    if (index < cols)
    {
      moveColumn(cols, index);
    }

    return this;
  }

  public Table addColumn()
  {
    int cols = cells.length;
    int rows = cells[0].length;

    Cell[][] newCells = new Cell[cols + 1][];
    System.arraycopy(cells, 0, newCells, 0, cols);

    newCells[cols] = new Cell[rows];
    for (int row = 0; row < rows; row++)
    {
      newCells[cols][row] = new Cell(this, cols, row);
    }

    cells = newCells;
    bottomRight = bottomRight.offset(1, 0);
    return this;
  }

  public Table moveColumn(int from, int to)
  {
    int rows = cells[0].length;

    if (from != to)
    {
      for (int row = 0; row < rows; row++)
      {
        Cell cell = cells[from][row];
        if (from < to)
        {
          for (int col = from; col < to; col++)
          {
            setCell(col, row, cells[col + 1][row]);
          }
        }
        else
        {
          for (int col = from; col > to; col--)
          {
            setCell(col, row, cells[col - 1][row]);
          }
        }

        setCell(to, row, cell);
      }
    }

    return this;
  }

  public RectangularRange row(int row)
  {
    return row(row, 0);
  }

  public RectangularRange row(int row, int skipLeftColumns)
  {
    return row(row, skipLeftColumns, 0);
  }

  public RectangularRange row(int row, int skipLeftColumns, int skipRightColumns)
  {
    return range(new Coordinate(skipLeftColumns, row), new Coordinate(bottomRight.col - skipRightColumns, row));
  }

  public Table addRow(int index)
  {
    int rows = cells[0].length;
    addRow();

    if (index < rows)
    {
      moveRow(rows, index);
    }

    return this;
  }

  public Table addRow()
  {
    int cols = cells.length;
    int rows = cells[0].length;

    for (int col = 0; col < cols; col++)
    {
      Cell[] newColumn = new Cell[rows + 1];
      System.arraycopy(cells[col], 0, newColumn, 0, rows);
      newColumn[rows] = new Cell(this, col, rows);
      cells[col] = newColumn;
    }

    bottomRight = bottomRight.offset(0, 1);
    return this;
  }

  public Table moveRow(int from, int to)
  {
    int cols = cells.length;

    if (from != to)
    {
      for (int col = 0; col < cols; col++)
      {
        Cell cell = cells[col][from];
        if (from < to)
        {
          for (int row = from; row < to; row++)
          {
            setCell(col, row, cells[col][row + 1]);
          }
        }
        else
        {
          for (int row = from; row > to; row--)
          {
            setCell(col, row, cells[col][row - 1]);
          }
        }

        setCell(col, to, cell);
      }
    }

    return this;
  }

  @Override
  public Table value(Object value)
  {
    return (Table)super.value(value);
  }

  @Override
  public Table format(Format format)
  {
    return (Table)super.format(format);
  }

  @Override
  public Table alignment(Alignment alignment)
  {
    return (Table)super.alignment(alignment);
  }

  private void setCell(int col, int row, Cell cell)
  {
    cells[col][row] = cell;
    cell.col = col;
    cell.row = row;
  }

  /**
   * Example
   */
  public static void main(String[] args) throws Exception
  {
    Table table1 = new Table();
    table1.range(0, 0, 5, 5).subtractRange(0, 0, 7, 0).value(new Generator.Incrementer());
    table1.row(0).format(new DecimalFormat("Series 0")).value(new Generator.Incrementer(1));
    Dumper.UTF8.dump(System.out, table1, 0);

    Table table2 = new Table(2, 11);
    table2.row(0).alignment(Alignment.RIGHT);
    table2.cell(0, 0).value("Probe");
    table2.cell(1, 0).value("Percent");

    Range probes = table2.column(0, 1).value(new Generator.Incrementer(1));
    for (Cell cell : probes.offset(1, 0).format(new DecimalFormat("0.00 %")))
    {
      cell.value(new Percent(probes, cell.offset(-1, 0)));
    }

    Dumper.UTF8.dump(System.out, table2, 0);
  }
}
