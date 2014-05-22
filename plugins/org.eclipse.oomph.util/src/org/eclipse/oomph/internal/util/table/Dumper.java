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

import org.eclipse.oomph.internal.util.table.Range.Alignment;

import java.io.PrintStream;

/**
 * @author Eike Stepper
 */
public class Dumper
{
  public static final Dumper ASCII = new Dumper();

  public static final Dumper UTF8 = new Dumper('\u2500', '\u2534', '\u252c', '\u253c', '\u2518', '\u2510', '\u2524', '\u2502', '\u251c', '\u2514', '\u250c',
      "\n", " ");

  public final char borderLeftRight;

  public final char borderLeftRightUp;

  public final char borderLeftRightDown;

  public final char borderLeftRightUpDown;

  public final char borderLeftUp;

  public final char borderLeftDown;

  public final char borderLeftUpDown;

  public final char borderUpDown;

  public final char borderUpDownRight;

  public final char borderUpRight;

  public final char borderDownRight;

  public final String newLine;

  public final String padding;

  public final String paddingBorder;

  public Dumper(char borderLeftRight, char borderLeftRightUp, char borderLeftRightDown, char borderLeftRightUpDown, char borderLeftUp, char borderLeftDown,
      char borderLeftUpDown, char borderUpDown, char borderUpDownRight, char borderUpRight, char borderDownRight, String newLine, String padding)
  {
    this.borderLeftRight = borderLeftRight;
    this.borderLeftRightUp = borderLeftRightUp;
    this.borderLeftRightDown = borderLeftRightDown;
    this.borderLeftRightUpDown = borderLeftRightUpDown;
    this.borderLeftUp = borderLeftUp;
    this.borderLeftDown = borderLeftDown;
    this.borderLeftUpDown = borderLeftUpDown;
    this.borderUpDown = borderUpDown;
    this.borderUpDownRight = borderUpDownRight;
    this.borderUpRight = borderUpRight;
    this.borderDownRight = borderDownRight;

    this.newLine = newLine == null ? "" : newLine;
    this.padding = padding == null ? "" : padding;

    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < padding.length(); i++)
    {
      builder.append(borderLeftRight);
    }

    paddingBorder = builder.toString();
  }

  public Dumper()
  {
    this('-', '+', '+', '+', '+', '+', '+', '|', '+', '+', '+', "\n", " ");
  }

  public String dump(RectangularRange range, int... rowSeparators)
  {
    StringBuilder builder = new StringBuilder();
    dump(builder, range, rowSeparators);
    return builder.toString();
  }

  public void dump(PrintStream stream, RectangularRange range, int... rowSeparators)
  {
    String string = dump(range, rowSeparators);
    stream.print(string);
    stream.flush();
  }

  public void dump(StringBuilder builder, RectangularRange range, int... rowSeparators)
  {
    Table table = range.table();
    Coordinate topLeft = range.topLeft();
    int cols = range.cols();
    int rows = range.rows();

    String[][] strings = new String[cols][];
    Alignment[][] alignments = new Alignment[cols][];
    int[] widths = new int[cols];

    for (int col = 0; col < cols; col++)
    {
      strings[col] = new String[rows];
      alignments[col] = new Alignment[rows];

      for (int row = 0; row < rows; row++)
      {
        Cell cell = table.cell(topLeft.col + col, range.topLeft().row + row);
        Object value = cell.value();
        String string = cell.applyFormat(value);

        strings[col][row] = string;
        alignments[col][row] = cell.alignmentFor(value);

        widths[col] = Math.max(widths[col], string.length());
      }
    }

    dumpSeparator(builder, Position.BEGIN, widths);
    for (int row = 0; row < rows; row++)
    {
      // Position vertical = row == 0 ? Position.BEGIN : row == rows - 1 ? Position.END : Position.MIDDLE;

      builder.append(borderUpDown);
      builder.append(padding);

      for (int col = 0; col < cols; col++)
      {
        if (col != 0)
        {
          builder.append(padding);
          builder.append(borderUpDown);
          builder.append(padding);
        }

        String value = strings[col][row];
        Alignment alignment = alignments[col][row];
        int width = widths[col];

        builder.append(alignment.apply(value, width));
      }

      builder.append(padding);
      builder.append(borderUpDown);
      builder.append(newLine);

      if (row < rows - 1 && needsSeparator(row, rowSeparators))
      {
        dumpSeparator(builder, Position.MIDDLE, widths);
      }
    }

    dumpSeparator(builder, Position.END, widths);
  }

  private void dumpSeparator(StringBuilder builder, Position vertical, int[] widths)
  {
    builder.append(border(Position.BEGIN, vertical));
    builder.append(paddingBorder);

    for (int c = 0; c < widths.length; c++)
    {
      if (c > 0)
      {
        builder.append(paddingBorder);
        builder.append(border(Position.MIDDLE, vertical));
        builder.append(paddingBorder);
      }

      for (int i = 0; i < widths[c]; i++)
      {
        builder.append(borderLeftRight);
      }
    }

    builder.append(paddingBorder);
    builder.append(border(Position.END, vertical));
    builder.append(newLine);
  }

  private char border(Position horizontal, Position vertical)
  {
    switch (horizontal)
    {
      case BEGIN:
        switch (vertical)
        {
          case BEGIN:
            return borderDownRight;
          case MIDDLE:
            return borderUpDownRight;
          case END:
            return borderUpRight;
        }

        break;

      case MIDDLE:
        switch (vertical)
        {
          case BEGIN:
            return borderLeftRightDown;
          case MIDDLE:
            return borderLeftRightUpDown;
          case END:
            return borderLeftRightUp;
        }

        break;

      case END:
        switch (vertical)
        {
          case BEGIN:
            return borderLeftDown;
          case MIDDLE:
            return borderLeftUpDown;
          case END:
            return borderLeftUp;
        }

        break;
    }

    throw new IllegalArgumentException();
  }

  private static boolean needsSeparator(int row, int[] rowSeparators)
  {
    for (int i = 0; i < rowSeparators.length; i++)
    {
      if (rowSeparators[i] == row)
      {
        return true;
      }
    }

    return false;
  }

  /**
   * @author Eike Stepper
   */
  private enum Position
  {
    BEGIN, MIDDLE, END
  }
}
