/*
 * Copyright (c) 2014, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.util.table;

/**
 * @author Eike Stepper
 */
public interface Formula
{
  public Object evaluate();

  /**
   * @author Eike Stepper
   */
  public static class Sum implements Formula
  {
    private final Range range;

    public Sum(Range range)
    {
      this.range = range;
    }

    public Double evaluate()
    {
      double sum = 0.0;
      for (Cell cell : range)
      {
        Number number = cell.number();
        if (number != null)
        {
          sum += number.doubleValue();
        }
      }

      return sum;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Min implements Formula
  {
    private final Range range;

    public Min(Range range)
    {
      this.range = range;
    }

    public Double evaluate()
    {
      double min = Double.MAX_VALUE;
      boolean empty = true;

      for (Cell cell : range)
      {
        Number number = cell.number();
        if (number != null)
        {
          min = Math.min(min, number.doubleValue());
          empty = false;
        }
      }

      return empty ? null : min;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Max implements Formula
  {
    private final Range range;

    public Max(Range range)
    {
      this.range = range;
    }

    public Double evaluate()
    {
      double max = Double.MIN_VALUE;
      boolean empty = true;

      for (Cell cell : range)
      {
        Number number = cell.number();
        if (number != null)
        {
          max = Math.max(max, number.doubleValue());
          empty = false;
        }
      }

      return empty ? null : max;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Avg implements Formula
  {
    private final Range range;

    public Avg(Range range)
    {
      this.range = range;
    }

    public Double evaluate()
    {
      double sum = 0.0;
      int count = 0;

      for (Cell cell : range)
      {
        Number number = cell.number();
        if (number != null)
        {
          sum += number.doubleValue();
          ++count;
        }
      }

      if (count == 0) // to avoid potential division by zero
      {
        ++count;
      }
      return sum / count;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class CountNumbers implements Formula
  {
    private final Range range;

    public CountNumbers(Range range)
    {
      this.range = range;
    }

    public Integer evaluate()
    {
      int count = 0;
      for (Cell cell : range)
      {
        Number number = cell.number();
        if (number != null)
        {
          ++count;
        }
      }

      return count;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Percent extends Sum
  {
    private final Cell cell;

    public Percent(Range range, Cell cell)
    {
      super(range);
      this.cell = cell;
    }

    @Override
    public Double evaluate()
    {
      Number number = cell.number();
      if (number == null)
      {
        return null;
      }

      double value = number.doubleValue();
      if (value == 0.0)
      {
        return value;
      }

      Double sum = super.evaluate();
      if (sum == null)
      {
        return null;
      }

      // Just to properly test equality for a double variable.
      final double EPSILON = 0.00001;
      if (sum >= -EPSILON && sum <= EPSILON)
      {
        return value < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
      }

      return value / sum;
    }
  }
}
