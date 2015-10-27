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

import org.eclipse.oomph.internal.util.UtilPlugin;
import org.eclipse.oomph.internal.util.table.Cell;
import org.eclipse.oomph.internal.util.table.Dumper;
import org.eclipse.oomph.internal.util.table.Formula;
import org.eclipse.oomph.internal.util.table.Range;
import org.eclipse.oomph.internal.util.table.Range.Alignment;
import org.eclipse.oomph.internal.util.table.Table;

import org.eclipse.core.runtime.IProgressMonitor;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An instrumented {@link SubMonitor sub monitor} that automatically collects and reports usage statistics.
 * <p>
 * It is normally very challenging to find out how much time a program really spends in the different parts of the monitored methods or how often these
 * parts get executed. Stepping through the program with a debugger obviously leads to distortion that renders the observations meaningless and adding
 * extra code to measure a runtime scenario realistically is not nice from a maintenance point of view.
 * <p>
 * As a solution to this problem this class offers the possibility to transparently instrument {@link SubMonitor} instances such that they automatically
 * collect and report all kinds of statistics that may help to enhance the user experience. Sometimes it would even indicate to remove some progress monitoring
 * because it turns out that almost no time is being spent in a particular part of the program. Another typical result from the analysis is the understanding of
 * <i>one time effects</i> that might need special consideration.
 * <p>
 * Instances of this class can be created explicitly with the {@link SubMonitor#convert(IProgressMonitor, SubMonitor.ProbingMode) Progress.progress()} factory methods
 * that take a {@link SubMonitor.ProbingMode} argument. Implicit (automatic) instrumentation can be controlled with the "<code>submonitor.probing</code>"
 * {@link System#setProperty(String, String) system property} as follows:
 * <dl>
 * <dt> <code>off</code>
 * <dd> All {@link SubMonitor monitors} that are not created with an explicit probing mode will <b>not</b> collect any usage information. No extra heap space
 *      or CPU time is allocated to these monitors.
 * <dt> <code>standard</code>
 * <dd> All {@link SubMonitor monitors} that are not created with an explicit probing mode will collect and report only statistical usage information.
 *      The amount of heap space allocated for this information is constant over time.
 * <dt> <code>full</code>
 * <dd> All {@link SubMonitor monitors} that are not created with an explicit probing mode will store and report all collected probes.
 *      The amount of heap space allocated for this information scales with the number of {@link SubMonitor monitor} instances that are created over time.
 * </dl>
 * <p>
 * This class registers a {@link Runtime#addShutdownHook(Thread) shutdown hook} that dumps the probing results to the console when the program ends.
 * By setting the "<code>submonitor.probing.trace</code>" {@link System#setProperty(String, String) system property} to the value "<code>true</code>"
 * probing results are continuously dumped as they become available.
 * <p>
 * The probing results are formatted as tables per monitored method. Each row of a table corresponds to a call to the {@link #worked()} or {@link #newChild()}
 * method. The first column of a table displays the location of that call, the content of the following columns depends on the probing mode used for a monitored method.
 * <p>
 * Example of the probing mode {@link SubMonitor.ProbingMode#STANDARD STANDARD}:
 * <p>
 * <img src="doc-files/standard.png">
 * <p>
 * Example of the probing mode {@link SubMonitor.ProbingMode#FULL FULL}:
 * <p>
 * <img src="doc-files/full.png">
 * <p>
 * The tables in the two examples above have smooth borders that can only be displayed correctly in consoles with UTF-8 encoding. The console encoding
 * can be configured on the <i>Common</i> tab of the launch configuration dialog. In addition the rendering of smooth table borders must be explicitly enabled
 * by setting the "<code>submonitor.probing.borders</code>" {@link System#setProperty(String, String) system property} to the value "<code>smooth</code>".
 * Without this setting tables are rendered with "-", "+" and "|" characters that display correctly regardless of the console encoding.
 * <p>
 * Quick navigation from the table cells to the corresponding code locations is supported by clickable links in the console tables. This way the
 * collected information can be converted into real enhancements of the monitoring code quickly and easily.
 * <p>
 * <b>Important note</b>: Avoid to load this class (i.e., to call any of its methods) unless you intend to actually use probing monitors. When this class
 * is loaded it spawns a thread to collect unused progress monitors and to free up the heap space allocated to their probes. The shutdown hook mentioned above
 * is also registered when this class is loaded.
 *
 * @author Eike Stepper
 */
public final class ProbingSubMonitor extends org.eclipse.oomph.util.SubMonitor
{
  private static final boolean TRACE = Boolean.getBoolean("submonitor.probing.trace");

  private static final Map<StackTraceElement, Statistics> STATISTICS = new HashMap<StackTraceElement, Statistics>();

  private static final Map<Integer, KeyedWeakReference> MAP = new ConcurrentHashMap<Integer, KeyedWeakReference>();

  private static final ReferenceQueue<ProbingSubMonitor> QUEUE = new ReferenceQueue<ProbingSubMonitor>();

  private static final AtomicInteger COUNTER = new AtomicInteger();

  private static final String NAME1 = SubMonitor.class.getName();

  private static final String NAME2 = ProbingSubMonitor.class.getName();

  private static final String NAME3 = RootInfo.class.getName();

  private static final int FULL_MODE = 0x0010;

  private static final int DONE_FLAG = 0x0020;

  private final ProbingSubMonitor parent;

  private final int totalTicks;

  private final int key;

  private final Map<StackTraceElement, Probe> probes = new HashMap<StackTraceElement, Probe>();

  private StackTraceElement location = determineLocation();

  private long timeStamp = determineTimeStamp();

  private StackTraceElement forkLocation;

  private StackTraceElement forkTarget;

  private int forkTicks;

  ProbingSubMonitor(ProbingSubMonitor parent, RootInfo rootInfo, int totalWork, int availableToChildren, int flags, boolean full)
  {
    super(rootInfo, totalWork, availableToChildren, flags | (full ? FULL_MODE : 0));
    this.parent = parent;
    totalTicks = totalWork;

    if (parent != null)
    {
      parent.forkTarget = location;
    }

    key = COUNTER.incrementAndGet();
    MAP.put(key, new KeyedWeakReference(key, this));
  }

  @Override
  SubMonitor createSubMonitor(RootInfo rootInfo, int totalWork, int availableToChildren, int flags)
  {
    timeStamp = determineTimeStamp();
    forkLocation = determineLocation();
    forkTicks = totalWork;

    boolean full = (flags & FULL_MODE) != 0;
    return new ProbingSubMonitor(this, rootInfo, totalWork, availableToChildren, flags, full);
  }

  @Override
  void adjustLocation()
  {
    location = determineLocation();
    if (parent != null)
    {
      parent.forkTarget = location;
    }
  }

  @Override
  public void worked(int work)
  {
    probe(work);
    super.worked(work);
  }

  @Override
  public void done()
  {
    if ((flags & DONE_FLAG) == 0)
    {
      flags |= DONE_FLAG;

      try
      {
        finishProbe(this);
        super.done();

        if (parent != null)
        {
          parent.childDone();
        }
      }
      finally
      {
        forkLocation = null;
        forkTarget = null;
        forkTicks = 0;

        MAP.remove(key);
      }
    }
  }

  @Override
  public void childDone()
  {
    if (forkLocation != null)
    {
      probe(forkLocation, forkTicks);
    }
  }

  @Override
  public String toString()
  {
    return shorten(location);
  }

  private void probe(int ticks)
  {
    StackTraceElement location = determineLocation();
    probe(location, ticks);
  }

  private void probe(StackTraceElement location, int ticks)
  {
    try
    {
      long now = determineTimeStamp();
      int time = (int)(Math.abs(now - timeStamp) / 1000000);

      Probe probe = probes.get(location);
      if (probe == null)
      {
        probe = new Probe(location);
        probes.put(location, probe);
      }

      probe.update(forkTarget, ticks, time);

      forkLocation = null;
      forkTarget = null;
      forkTicks = 0;
      timeStamp = now;
    }
    catch (Exception ex)
    {
      UtilPlugin.INSTANCE.log(ex);
    }
  }

  private static String shorten(StackTraceElement location)
  {
    String className = location.getClassName();

    boolean lastCharWasDigit = false;
    int length = className.length();
    int i = length;
    while (--i >= 0)
    {
      char c = className.charAt(i);
      if ((c == '.' || c == '$') && !lastCharWasDigit)
      {
        ++i;
        break;
      }

      lastCharWasDigit = Character.isDigit(c);
    }

    StringBuilder builder = new StringBuilder();
    builder.append(className, i, length);
    builder.append('.');
    builder.append(location.getMethodName());

    if (location.isNativeMethod())
    {
      builder.append("(Native Method)");
    }
    else
    {
      String fileName = location.getFileName();
      int lineNumber = location.getLineNumber();
      if (fileName != null && lineNumber >= 0)
      {
        builder.append('(');
        builder.append(fileName);
        builder.append(':');
        builder.append(lineNumber);
        builder.append(')');
      }
      else
      {
        if (fileName != null)
        {
          builder.append('(');
          builder.append(fileName);
          builder.append(')');
        }
        else
        {
          builder.append("(Unknown Source)");
        }
      }
    }

    return builder.toString();
  }

  private static StackTraceElement determineLocation()
  {
    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
    for (int i = 3; i < stackTrace.length; i++)
    {
      StackTraceElement stackTraceElement = stackTrace[i];

      String className = stackTraceElement.getClassName();
      if (className != NAME1 && className != NAME2 && className != NAME3)
      {
        return stackTraceElement;
      }
    }

    return null;
  }

  private static long determineTimeStamp()
  {
    return System.nanoTime();
  }

  private static void finishProbe(ProbingSubMonitor progress)
  {
    try
    {
      StackTraceElement location = progress.location;

      Statistics statistics = STATISTICS.get(location);
      if (statistics == null)
      {
        statistics = new Statistics(location);
        STATISTICS.put(location, statistics);
      }

      statistics.update(progress);
    }
    catch (Exception ex)
    {
      UtilPlugin.INSTANCE.log(ex);
    }
  }

  public static void reportStatistics()
  {
    for (Statistics statistics : STATISTICS.values())
    {
      statistics.report();
    }
  }

  public static void resetStatistics()
  {
    STATISTICS.clear();
  }

  static
  {
    final AtomicBoolean shuttingDown = new AtomicBoolean();
    Runtime.getRuntime().addShutdownHook(new Thread("Progress Probe Shutdown Hook")
    {
      @Override
      public void run()
      {
        shuttingDown.set(true);

        for (KeyedWeakReference reference : MAP.values())
        {
          ProbingSubMonitor progress = reference.get();
          if (progress != null)
          {
            finishProbe(progress);
          }
        }

        reportStatistics();
      }
    });

    Thread monitor = new Thread("Progress Probe Monitor")
    {
      @Override
      public void run()
      {
        while (!isInterrupted() && !shuttingDown.get())
        {
          KeyedWeakReference reference = (KeyedWeakReference)QUEUE.poll();
          if (reference != null)
          {
            MAP.remove(reference.key);

            ProbingSubMonitor progress = reference.get();
            if (progress != null && (progress.flags & DONE_FLAG) == 0)
            {
              finishProbe(progress);
            }
          }
        }
      }
    };

    monitor.setDaemon(true);
    monitor.start();
  }

  /**
   * @author Eike Stepper
   */
  private static final class KeyedWeakReference extends WeakReference<ProbingSubMonitor>
  {
    private final int key;

    public KeyedWeakReference(int key, ProbingSubMonitor progress)
    {
      super(progress);
      this.key = key;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class Probe
  {
    private final StackTraceElement location;

    private StackTraceElement[] forkTargets;

    private int ticks;

    private int time;

    public Probe(StackTraceElement location)
    {
      this.location = location;
    }

    public void update(StackTraceElement forkTarget, int ticks, int time)
    {
      this.ticks += ticks;
      this.time += time;

      if (forkTarget != null)
      {
        if (forkTargets == null)
        {
          forkTargets = new StackTraceElement[] { forkTarget };
        }
        else
        {
          for (int i = 0; i < forkTargets.length; i++)
          {
            StackTraceElement location = forkTargets[i];
            if (location.equals(forkTarget))
            {
              return;
            }
          }

          StackTraceElement[] newForkTargets = new StackTraceElement[forkTargets.length + 1];
          System.arraycopy(forkTargets, 0, newForkTargets, 0, forkTargets.length);
          newForkTargets[forkTargets.length] = forkTarget;

          forkTargets = newForkTargets;
        }
      }
    }

    @Override
    public String toString()
    {
      return location + " - " + time;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class Statistics
  {
    public static final Format TICKS = new DecimalFormat("0 Ticks");

    public static final Format MILLIS = new DecimalFormat("0 ms");

    public static final Format MILLIS_PRECISE = new DecimalFormat("0.00 ms");

    public static final Format PERCENT = new DecimalFormat("0 %");

    private static final int DEFAULT_COLUMNS = 3;

    private static final boolean SMOOTH_BORDERS = "smooth".equalsIgnoreCase(System.getProperty("submonitor.probing.borders"));

    private static final Dumper DUMPER = SMOOTH_BORDERS ? Dumper.UTF8 : Dumper.ASCII;

    private final StackTraceElement location;

    private long totalTicks;

    private int columns;

    private Row[] rows;

    public Statistics(StackTraceElement location)
    {
      this.location = location;
    }

    public void update(ProbingSubMonitor progress)
    {
      totalTicks += progress.totalTicks;

      List<Row> newRows;
      if (rows == null)
      {
        newRows = new ArrayList<Row>(10);
      }
      else
      {
        newRows = new ArrayList<Row>(10 + rows.length);
        for (int i = 0; i < rows.length; i++)
        {
          Row row = rows[i];
          Probe probe = progress.probes.remove(row.location);
          if (probe == null)
          {
            row.skipProbes(1);
          }
          else
          {
            row.addProbe(probe);
          }

          newRows.add(row);
        }
      }

      boolean full = (progress.flags & FULL_MODE) != 0;

      for (Probe newProbe : progress.probes.values())
      {
        Row row = Row.create(newProbe.location, full);

        if (columns > 0)
        {
          row.skipProbes(columns);
        }

        row.addProbe(newProbe);
        newRows.add(row);
      }

      rows = newRows.toArray(new Row[newRows.size()]);
      ++columns;

      if (TRACE)
      {
        report();
      }
    }

    public void report()
    {
      if (rows != null && rows.length != 0)
      {
        Arrays.sort(rows);
        int rowCount = rows.length;

        Table table = new Table(DEFAULT_COLUMNS, 1 + rowCount);
        table.cell(0, 0).value(shorten(location));
        table.cell(1, 0).value(totalTicks / columns);
        table.column(1).format(TICKS);

        int lastCol = rows[0].header(table, rowCount);
        for (int r = 0; r < rowCount; r++)
        {
          Row row = rows[r];
          table.cell(0, 1 + r).value(shorten(row.location));

          Cell tickCell = table.cell(1, 1 + r);
          tickCell.value(row.ticks / row.getRuns());

          Formula percent = new Formula.Percent(table.column(1, 1), tickCell);
          table.cell(2, 1 + r).format(PERCENT).value(percent);

          row.body(table, r + 1);

          if (row.forkTargets != null)
          {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < row.forkTargets.length; i++)
            {
              if (builder.length() != 0)
              {
                builder.append(", ");
              }

              StackTraceElement forkTarget = row.forkTargets[i];
              builder.append(shorten(forkTarget));
            }

            table.cell(lastCol + 1, r + 1).value(builder);
          }
        }

        int forkTargetCol = table.bottomRight().col;
        if (forkTargetCol > lastCol)
        {
          table.cell(forkTargetCol, 0).value("Fork Targets");
        }

        rows[0].footer(table, rowCount);

        System.out.println();
        DUMPER.dump(System.out, table, 0, rowCount);
      }
    }

    /**
     * @author Eike Stepper
     */
    private static abstract class Row implements Comparable<Row>
    {
      private final StackTraceElement location;

      private StackTraceElement[] forkTargets;

      private long ticks;

      public Row(StackTraceElement location)
      {
        this.location = location;
      }

      public void addProbe(Probe probe)
      {
        if (probe.forkTargets != null)
        {
          if (forkTargets == null)
          {
            forkTargets = probe.forkTargets;
          }
          else
          {
            if (forkTargets.length != 1 || probe.forkTargets.length != 1 || !forkTargets[0].equals(probe.forkTargets[0]))
            {
              Set<StackTraceElement> set = new HashSet<StackTraceElement>();
              set.addAll(Arrays.asList(forkTargets));
              set.addAll(Arrays.asList(probe.forkTargets));

              forkTargets = set.toArray(new StackTraceElement[set.size()]);
            }
          }
        }

        ticks += probe.ticks;
        addProbe(probe.time);
      }

      public abstract void addProbe(int time);

      public abstract void skipProbes(int count);

      public abstract int getRuns();

      public abstract int header(Table table, int rows);

      public abstract void body(Table table, int row);

      public abstract void footer(Table table, int rows);

      public int compareTo(Row o)
      {
        int result = location.getFileName().compareTo(o.location.getFileName());
        if (result == 0)
        {
          result = location.getLineNumber() - o.location.getLineNumber();
        }

        return result;
      }

      public static Row create(StackTraceElement location, boolean full)
      {
        return full ? new Full(location) : new Compressed(location);
      }

      /**
       * @author Eike Stepper
       */
      private static final class Compressed extends Row
      {
        private int skips;

        private int runs;

        private long sum;

        private int min = Integer.MAX_VALUE;

        private int max = Integer.MIN_VALUE;

        public Compressed(StackTraceElement location)
        {
          super(location);
        }

        @Override
        public void addProbe(int time)
        {
          ++runs;
          sum += time;
          min = Math.min(min, time);
          max = Math.max(max, time);
        }

        @Override
        public void skipProbes(int count)
        {
          ++skips;
        }

        @Override
        public int getRuns()
        {
          return runs;
        }

        @Override
        public int header(Table table, int rows)
        {
          int col = DEFAULT_COLUMNS - 1;
          table.cell(++col, 0).value("Skips");
          table.cell(++col, 0).value("Runs");
          table.cell(++col, 0).value("Runs %");
          table.cell(++col, 0).value("Sum");
          table.cell(++col, 0).value("Min");
          table.cell(++col, 0).value("Max");
          table.cell(++col, 0).value("Range");
          table.cell(++col, 0).value("Average/Runs");
          table.cell(++col, 0).value("Average%");
          table.row(0, 1).alignment(Alignment.RIGHT);
          return col;
        }

        @Override
        public void body(Table table, int row)
        {
          int col = DEFAULT_COLUMNS - 1;
          table.cell(++col, row).value(skips);

          Cell runsCell = table.cell(++col, row);
          runsCell.value(runs);

          Formula runsPercent = new Formula.Percent(table.range(col - 1, row, col, row), runsCell);
          table.cell(++col, row).format(PERCENT).value(runsPercent);

          table.cell(++col, row).format(MILLIS).value(sum);
          table.cell(++col, row).format(MILLIS).value(min);
          table.cell(++col, row).format(MILLIS).value(max);
          table.cell(++col, row).format(MILLIS).value(new Formula()
          {
            public Object evaluate()
            {
              return max - min;
            }
          });

          Cell avgCell = table.cell(++col, row);
          avgCell.format(MILLIS_PRECISE).value(sum / runs);

          Formula avgPercent = new Formula.Percent(table.column(avgCell.col(), 1), avgCell);
          table.cell(++col, row).format(PERCENT).value(avgPercent);
        }

        @Override
        public void footer(Table table, int rows)
        {
        }
      }

      /**
       * @author Eike Stepper
       */
      private static final class Full extends Row
      {
        private static final int SKIPPED = -1;

        private int[] times;

        public Full(StackTraceElement location)
        {
          super(location);
        }

        @Override
        public void addProbe(int time)
        {
          if (times == null)
          {
            times = new int[1];
            times[0] = time;
          }
          else
          {
            int[] newTimes = new int[times.length + 1];
            System.arraycopy(times, 0, newTimes, 0, times.length);
            newTimes[times.length] = time;
            times = newTimes;
          }
        }

        @Override
        public void skipProbes(int count)
        {
          if (times == null)
          {
            times = new int[count];
            Arrays.fill(times, SKIPPED);
          }
          else
          {
            int length = times.length;
            int newLength = length + count;

            int[] newTimes = new int[newLength];
            System.arraycopy(times, 0, newTimes, 0, length);
            Arrays.fill(newTimes, length, newLength, SKIPPED);

            times = newTimes;
          }
        }

        @Override
        public int getRuns()
        {
          if (times == null)
          {
            return 0;
          }

          int runs = 0;
          for (int i = 0; i < times.length; i++)
          {
            if (times[i] != SKIPPED)
            {
              ++runs;
            }
          }

          return runs;
        }

        @Override
        public int header(Table table, int rows)
        {
          for (int i = 0; i < times.length; i++)
          {
            Cell timeCell = table.cell(DEFAULT_COLUMNS + 2 * i, 0);
            timeCell.alignment(Alignment.RIGHT).value("Run" + (i + 1));
          }

          Cell avgTimeCell = table.cell(DEFAULT_COLUMNS + 2 * times.length, 0);
          avgTimeCell.alignment(Alignment.RIGHT).value("AVERAGE");

          return avgTimeCell.col() + 1;
        }

        @Override
        public void body(Table table, int row)
        {
          Range avgTimeRange = null;

          for (int i = 0; i < times.length; i++)
          {
            int time = times[i];
            if (time != SKIPPED)
            {
              int timeCol = DEFAULT_COLUMNS + 2 * i;

              Cell timeCell = table.cell(timeCol, row);
              timeCell.format(MILLIS).value(time);

              Cell ratioCell = timeCell.offset(1, 0);
              ratioCell.format(PERCENT).value(new Formula.Percent(table.column(timeCol, 1), timeCell));

              avgTimeRange = avgTimeRange == null ? timeCell : avgTimeRange.addRanges(timeCell);
            }
          }

          Cell avgTimeCell = table.cell(DEFAULT_COLUMNS + 2 * times.length, row);
          avgTimeCell.format(MILLIS).value(new Formula.Avg(avgTimeRange));

          Cell avgRatioCell = avgTimeCell.offset(1, 0);
          avgRatioCell.format(PERCENT).value(new Formula.Percent(table.column(avgTimeCell.col(), 1), avgTimeCell));
        }

        @Override
        public void footer(Table table, int rows)
        {
          table.cell(0, 1 + rows).value("TOTAL");
          table.cell(1, 1 + rows).format(TICKS).value(new Formula.Sum(table.column(1, 1, 1)));

          for (int i = 0; i <= times.length; i++)
          {
            int timeCol = DEFAULT_COLUMNS + 2 * i;
            Cell sumCell = table.cell(timeCol, 1 + rows);
            sumCell.format(MILLIS).value(new Formula.Sum(table.column(timeCol, 1, 1)));
          }
        }
      }
    }
  }
}
