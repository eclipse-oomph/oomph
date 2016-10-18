/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * Copyright (c) 2014, 2016 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Stefan Xenos - initial API and implementation
 *    Stefan Xenos - bug 174539 - add a 1-argument convert(...) method
 *    Stefan Xenos - bug 174040 - SubMonitor#convert doesn't always set task name
 *    Stefan Xenos - bug 206942 - updated javadoc to recommend better constants for infinite progress
 *    IBM Corporation - ongoing maintenance
 *    Eike Stepper - copied from org.eclipse.core.runtime.SubMonitor and enhanced as outlined in the JavaDoc below
 */
package org.eclipse.oomph.util;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitorWithBlocking;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

/**
 * A {@link IProgressMonitorWithBlocking progress monitor} that uses a given amount of work ticks from a parent monitor.
 * This is intended as a safer, easier-to-use alternative to {@link org.eclipse.core.runtime.SubProgressMonitor}.
 * <p>
 * Progress monitoring is generally quite invasive to the code that is monitored.
 * At the same time progress monitoring itself is typically very hard to implement correctly.
 * This class aims at reducing the invasiveness as much as possible while offering all the functionality needed
 * to do the job right.
 * <p>
 * The following aspects of this class help to keep the progress monitoring code short and nice and to avoid common monitoring mistakes:
 * <ul>
 * <li> It offers the full functionality of {@link org.eclipse.core.runtime.SubMonitor}, which already makes progress monitoring a lot easier.
 *      Refer to the {@link org.eclipse.core.runtime.SubMonitor} documentation or to this <a href="https://wiki.eclipse.org/Progress_Reporting">article</a> for details and examples.
 *
 * <li> In addition to the {@link SubMonitor#setWorkRemaining(int)} method it offers a {@link #skipped(int)} method, which redistributes the remaining work
 *      according to the last skipped {@link #worked(int)} or {@link #newChild(int)} call rather than on the sum of all subsequent calls.
 *
 * <li> It reduces the need to specify <code>work</code> arguments by using the default value {@link #DEFAULT_WORK 1} with the overloaded
 *      {@link #worked()}, {@link #skipped()} and {@link #newChild()} calls.
 *
 * <li> Basically all methods of this class can implicitely check for cancelation, thereby ensuring that the monitored code is always cancelable by the user
 *      without cluttering the code with repetitions of the following idiom:
 *      <pre>
if (monitor.isCanceled())
{
  throw new OperationCanceledException();
}
 *      </pre>
 *      For details about automatic cancelation detection refer to {@link #detectCancelation()}.
 *
 * <li> It is normally very challenging to find out how much time a program really spends in the different parts of the monitored methods or how often these
 *      parts get executed. Stepping through the program with a debugger obviously leads to distortion that renders the observations meaningless and adding
 *      extra code to measure a runtime scenario realisticly is not nice from a maintenance point of view.
 *      <p>
 *      As a solution to this problem this class offers the possibility to transparently instrument {@link SubMonitor} instances such that they automatically
 *      collect and report all kinds of statistics that may help to enhance the user experience. Sometimes it would even indicate to remove some progress monitoring
 *      because it turns out that almost no time is being spent in a particular part of the program. Another typical result from the analysis is the understanding of
 *      <i>one time effects</i> that might need special consideration.
 *      <p>
 *      For details about this <i>probing</i> mode refer to {@link ProbingSubMonitor}.
 * </ul>
 * <p>
 * The following example shows how to monitor progress while recursing through a tree of folders:
 * <pre>
public void recurse(IContainer container, IProgressMonitor monitor) throws Exception
{
  IResource[] members = container.members();

  SubMonitor progress = SubMonitor.convert(monitor, members.length).detectCancelation();
  progress.subTask(container.getFullPath().toString());

  for (IResource member : members)
  {
    if (member instanceof IContainer)
    {
      Thread.sleep(5);
      recurse((IContainer)member, progress.newChild());
    }
    else
    {
      progress.skipped();
    }
  }
}
 * </pre>
 *
 * @author Eike Stepper
 */
public class SubMonitor implements IProgressMonitorWithBlocking
{
  /**
   * Minimum number of ticks to allocate when calling beginTask on an unknown IProgressMonitor.
   * Pick a number that is big enough such that, no matter where progress is being displayed,
   * the user would be unlikely to notice if progress were to be reported with higher accuracy.
   */
  private static final int MINIMUM_RESOLUTION = 1000;

  /**
   * The RootInfo holds information about the root progress monitor. A SubMonitor and
   * its active descendents share the same RootInfo.
   */
  // Can't be private because ProbingSubMonitor uses it.
  static final class RootInfo
  {
    private final IProgressMonitor root;

    /**
     * Remembers the last task name. Prevents us from setting the same task name multiple
     * times in a row.
     */
    private String taskName = null;

    /**
     * Remembers the last subtask name. Prevents the SubMonitor from setting the same
     * subtask string more than once in a row.
     */
    private String subTask = null;

    /**
     * Creates a RootInfo struct that delegates to the given progress
     * monitor.
     *
     * @param root progress monitor to delegate to
     */
    public RootInfo(IProgressMonitor root)
    {
      this.root = root;
    }

    public IProgressMonitor getRoot()
    {
      return root;
    }

    public boolean isCanceled()
    {
      return root.isCanceled();
    }

    public void setCanceled(boolean value)
    {
      root.setCanceled(value);
    }

    public void setTaskName(String taskName)
    {
      if (eq(taskName, this.taskName))
      {
        return;
      }
      this.taskName = taskName;
      root.setTaskName(taskName);
    }

    public void subTask(String name)
    {
      if (eq(subTask, name))
      {
        return;
      }

      subTask = name;
      root.subTask(name);
    }

    public void worked(int i)
    {
      root.worked(i);
    }

    public void clearBlocked()
    {
      if (root instanceof IProgressMonitorWithBlocking)
      {
        ((IProgressMonitorWithBlocking)root).clearBlocked();
      }
    }

    public void setBlocked(IStatus reason)
    {
      if (root instanceof IProgressMonitorWithBlocking)
      {
        ((IProgressMonitorWithBlocking)root).setBlocked(reason);
      }
    }

  }

  /**
   * Total number of ticks that this progress monitor is permitted to consume
   * from the root.
   */
  private int totalParent;

  /**
   * Number of ticks that this progress monitor has already reported in the root.
   */
  private int usedForParent = 0;

  /**
   * Number of ticks that have been consumed by this instance's children.
   */
  private double usedForChildren = 0.0;

  /**
   * Number of ticks allocated for this instance's children. This is the total number
   * of ticks that may be passed into worked(int) or newChild(int).
   */
  private int totalForChildren;

  /**
   * Children created by newChild will be completed automatically the next time
   * the parent progress monitor is touched. This points to the last incomplete child
   * created with newChild.
   */
  private IProgressMonitor lastSubMonitor = null;

  /**
   * Used to communicate with the root of this progress monitor tree
   */
  // Can't be private because ProbingSubMonitor reads from it.
  final RootInfo root;

  /**
   * A bitwise combination of the SUPPRESS_* flags.
   */
  // Can't be final because detectCancelation() writes to it.
  // Can't be private because ProbingSubMonitor reads from it.
  int flags;

  public static final int DEFAULT_WORK = 1;

  /**
   * May be passed as a flag to newChild. Indicates that the calls
   * to subTask on the child should be ignored. Without this flag,
   * calling subTask on the child will result in a call to subTask
   * on its parent.
   */
  public static final int SUPPRESS_SUBTASK = 0x0001;

  /**
   * May be passed as a flag to newChild. Indicates that strings
   * passed into beginTask should be ignored. If this flag is
   * specified, then the progress monitor instance will accept null
   * as the first argument to beginTask. Without this flag, any
   * string passed to beginTask will result in a call to
   * setTaskName on the parent.
   */
  public static final int SUPPRESS_BEGINTASK = 0x0002;

  /**
   * May be passed as a flag to newChild. Indicates that strings
   * passed into setTaskName should be ignored. If this string
   * is omitted, then a call to setTaskName on the child will
   * result in a call to setTaskName on the parent.
   */
  public static final int SUPPRESS_SETTASKNAME = 0x0004;

  /**
   * May be passed as a flag to newChild. Indicates that strings
   * passed to setTaskName, subTask, and beginTask should all be ignored.
   */
  public static final int SUPPRESS_ALL_LABELS = SUPPRESS_SETTASKNAME | SUPPRESS_BEGINTASK | SUPPRESS_SUBTASK;

  /**
   * May be passed as a flag to newChild. Indicates that strings
   * passed to setTaskName, subTask, and beginTask should all be propagated
   * to the parent.
   */
  public static final int SUPPRESS_NONE = 0;

  private static final int DETECT_CANCELATION = 0x0008;

  /**
   * Creates a new SubMonitor that will report its progress via
   * the given RootInfo.
   * @param rootInfo the root of this progress monitor tree
   * @param totalWork total work to perform on the given progress monitor
   * @param availableToChildren number of ticks allocated for this instance's children
   * @param flags a bitwise combination of the SUPPRESS_* constants
   */
  SubMonitor(RootInfo rootInfo, int totalWork, int availableToChildren, int flags)
  {
    root = rootInfo;
    totalParent = totalWork > 0 ? totalWork : 0;
    totalForChildren = availableToChildren;
    this.flags = flags;
  }

  SubMonitor createSubMonitor(RootInfo rootInfo, int totalWork, int availableToChildren, int flags)
  {
    // ProbingSubMonitor overwrites to create special sub monitors for probing and reporting purposes.
    return new SubMonitor(rootInfo, totalWork, availableToChildren, flags);
  }

  void adjustLocation()
  {
    // Do nothing here.
    // ProbingSubMonitor overwrites for probing and reporting purposes.
  }

  /**
   * <p>Converts an unknown (possibly null) IProgressMonitor into a SubMonitor. It is
   * not necessary to call done() on the result, but the caller is responsible for calling
   * done() on the argument. Calls beginTask on the argument.</p>
   *
   * <p>This method should generally be called at the beginning of a method that accepts
   * an IProgressMonitor in order to convert the IProgressMonitor into a SubMonitor.</p>
   *
   * @param monitor monitor to convert to a SubMonitor instance or null. Treats null
   * as a new instance of <code>NullProgressMonitor</code>.
   * @return a SubMonitor instance that adapts the argument
   */
  public static SubMonitor convert(IProgressMonitor monitor)
  {
    return convert(monitor, "", 0, ProbingMode.DEFAULT); //$NON-NLS-1$
  }

  /**
   * <p>Converts an unknown (possibly null) IProgressMonitor into a SubMonitor allocated
   * with the given number of ticks. It is not necessary to call done() on the result,
   * but the caller is responsible for calling done() on the argument. Calls beginTask
   * on the argument.</p>
   *
   * <p>This method should generally be called at the beginning of a method that accepts
   * an IProgressMonitor in order to convert the IProgressMonitor into a SubMonitor.</p>
   *
   * @param monitor monitor to convert to a SubMonitor instance or null. Treats null
   * as a new instance of <code>NullProgressMonitor</code>.
   * @param work number of ticks that will be available in the resulting monitor
   * @return a SubMonitor instance that adapts the argument
   */
  public static SubMonitor convert(IProgressMonitor monitor, int work)
  {
    return convert(monitor, "", work, ProbingMode.DEFAULT); //$NON-NLS-1$
  }

  /**
   * <p>Converts an unknown (possibly null) IProgressMonitor into a SubMonitor allocated
   * with the given number of ticks. It is not necessary to call done() on the result,
   * but the caller is responsible for calling done() on the argument. Calls beginTask
   * on the argument.</p>
   *
   * <p>This method should generally be called at the beginning of a method that accepts
   * an IProgressMonitor in order to convert the IProgressMonitor into a SubMonitor.</p>
   *
   * @param monitor to convert into a SubMonitor instance or null. If given a null argument,
   * the resulting SubMonitor will not report its progress anywhere.
   * @param taskName user readable name to pass to monitor.beginTask. Never null.
   * @param work initial number of ticks to allocate for children of the SubMonitor
   * @return a new SubMonitor instance that is a child of the given monitor
   */
  public static SubMonitor convert(IProgressMonitor monitor, String taskName, int work)
  {
    return convert(monitor, taskName, work, ProbingMode.DEFAULT);
  }

  /**
   * <p>Converts an unknown (possibly null) IProgressMonitor into a SubMonitor. It is
   * not necessary to call done() on the result, but the caller is responsible for calling
   * done() on the argument. Calls beginTask on the argument.</p>
   *
   * <p>This method should generally be called at the beginning of a method that accepts
   * an IProgressMonitor in order to convert the IProgressMonitor into a SubMonitor.</p>
   *
   * @param monitor monitor to convert to a SubMonitor instance or null. Treats null
   * as a new instance of <code>NullProgressMonitor</code>.
   * @return a SubMonitor instance that adapts the argument
   */
  public static SubMonitor convert(IProgressMonitor monitor, ProbingMode probingMode)
  {
    return convert(monitor, "", 0, probingMode); //$NON-NLS-1$
  }

  /**
   * <p>Converts an unknown (possibly null) IProgressMonitor into a SubMonitor allocated
   * with the given number of ticks. It is not necessary to call done() on the result,
   * but the caller is responsible for calling done() on the argument. Calls beginTask
   * on the argument.</p>
   *
   * <p>This method should generally be called at the beginning of a method that accepts
   * an IProgressMonitor in order to convert the IProgressMonitor into a SubMonitor.</p>
   *
   * @param monitor monitor to convert to a SubMonitor instance or null. Treats null
   * as a new instance of <code>NullProgressMonitor</code>.
   * @param work number of ticks that will be available in the resulting monitor
   * @return a SubMonitor instance that adapts the argument
   */
  public static SubMonitor convert(IProgressMonitor monitor, int work, ProbingMode probingMode)
  {
    return convert(monitor, "", work, probingMode); //$NON-NLS-1$
  }

  /**
   * <p>Converts an unknown (possibly null) IProgressMonitor into a SubMonitor allocated
   * with the given number of ticks. It is not necessary to call done() on the result,
   * but the caller is responsible for calling done() on the argument. Calls beginTask
   * on the argument.</p>
   *
   * <p>This method should generally be called at the beginning of a method that accepts
   * an IProgressMonitor in order to convert the IProgressMonitor into a SubMonitor.</p>
   *
   * @param monitor to convert into a SubMonitor instance or null. If given a null argument,
   * the resulting SubMonitor will not report its progress anywhere.
   * @param taskName user readable name to pass to monitor.beginTask. Never null.
   * @param work initial number of ticks to allocate for children of the SubMonitor
   * @return a new SubMonitor instance that is a child of the given monitor
   */
  public static SubMonitor convert(IProgressMonitor monitor, String taskName, int work, ProbingMode probingMode)
  {
    if (monitor == null)
    {
      monitor = new NullProgressMonitor();
    }

    // Optimization: if the given monitor already a SubMonitor, no conversion is necessary
    if (monitor instanceof SubMonitor)
    {
      SubMonitor subMonitor = (SubMonitor)monitor;
      subMonitor.beginTask(taskName, work);
      subMonitor.adjustLocation();
      return subMonitor;
    }

    monitor.beginTask(taskName, MINIMUM_RESOLUTION);
    if (probingMode == ProbingMode.OFF)
    {
      return new SubMonitor(new RootInfo(monitor), MINIMUM_RESOLUTION, work, SUPPRESS_NONE);
    }

    return createProbingSubMonitor(monitor, work, probingMode == ProbingMode.FULL);
  }

  /**
   * Helps to avoid unnecessary loading of the {@link ProbingProgress} class, which forks a monitoring thread.
   */
  private static SubMonitor createProbingSubMonitor(IProgressMonitor monitor, int availableToChildren, boolean full)
  {
    ProbingSubMonitor parent = monitor instanceof ProbingSubMonitor ? (ProbingSubMonitor)monitor : null;
    return new ProbingSubMonitor(parent, new RootInfo(monitor), MINIMUM_RESOLUTION, availableToChildren, SUPPRESS_NONE, full);
  }

  public final SubMonitor detectCancelation()
  {
    return detectCancelation(true);
  }

  public final SubMonitor detectCancelation(boolean on)
  {
    if (on)
    {
      flags |= DETECT_CANCELATION;
    }
    else
    {
      flags &= ~DETECT_CANCELATION;
    }

    return this;
  }

  private void checkCancelation()
  {
    if ((flags & DETECT_CANCELATION) != 0 && isCanceled())
    {
      throw new OperationCanceledException();
    }
  }

  /**
   * <p>Sets the work remaining for this SubMonitor instance. This is the total number
   * of ticks that may be reported by all subsequent calls to worked(int), newChild(int), etc.
   * This may be called many times for the same SubMonitor instance. When this method
   * is called, the remaining space on the progress monitor is redistributed into the given
   * number of ticks.</p>
   *
   * <p>It doesn't matter how much progress has already been reported with this SubMonitor
   * instance. If you call setWorkRemaining(100), you will be able to report 100 more ticks of
   * work before the progress meter reaches 100%.</p>
   *
   * @param workRemaining total number of remaining ticks
   * @return the receiver
   */
  public final SubMonitor setWorkRemaining(int workRemaining)
  {
    checkCancelation();

    // Ensure we don't try to allocate negative ticks
    workRemaining = Math.max(0, workRemaining);

    // Ensure we don't cause division by zero
    if (totalForChildren > 0 && totalParent > usedForParent)
    {
      // Note: We want the following value to remain invariant after this method returns
      double remainForParent = totalParent * (1.0d - usedForChildren / totalForChildren);
      usedForChildren = workRemaining * (1.0d - remainForParent / (totalParent - usedForParent));
    }
    else
    {
      usedForChildren = 0.0d;
    }

    totalParent = totalParent - usedForParent;
    usedForParent = 0;
    totalForChildren = workRemaining;
    return this;
  }

  /**
   * Consumes the given number of child ticks, given as a double. Must only
   * be called if the monitor is in floating-point mode.
   *
   * @param ticks the number of ticks to consume
   * @return ticks the number of ticks to be consumed from parent
   */
  private int consume(double ticks)
  {
    if (totalParent == 0 || totalForChildren == 0)
    {
      return 0;
    }

    usedForChildren += ticks;

    if (usedForChildren > totalForChildren)
    {
      usedForChildren = totalForChildren;
    }
    else if (usedForChildren < 0.0)
    {
      usedForChildren = 0.0;
    }

    int parentPosition = (int)(totalParent * usedForChildren / totalForChildren);
    int delta = parentPosition - usedForParent;

    usedForParent = parentPosition;
    return delta;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.runtime.IProgressMonitor#isCanceled()
   */
  public final boolean isCanceled()
  {
    return root.isCanceled();
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.runtime.IProgressMonitor#setTaskName(java.lang.String)
   */
  public final void setTaskName(String name)
  {
    checkCancelation();

    if ((flags & SUPPRESS_SETTASKNAME) == 0)
    {
      root.setTaskName(name);
    }
  }

  /**
   * Starts a new main task. The string argument is ignored
   * if and only if the SUPPRESS_BEGINTASK flag has been set on this SubMonitor
   * instance.
   *
   * <p>This method is equivalent calling setWorkRemaining(...) on the receiver. Unless
   * the SUPPRESS_BEGINTASK flag is set, this will also be equivalent to calling
   * setTaskName(...) on the parent.</p>
   *
   * @param name new main task name
   * @param totalWork number of ticks to allocate
   *
   * @see org.eclipse.core.runtime.IProgressMonitor#beginTask(java.lang.String, int)
   */
  public final void beginTask(String name, int totalWork)
  {
    if ((flags & SUPPRESS_BEGINTASK) == 0 && name != null)
    {
      root.setTaskName(name);
    }
    setWorkRemaining(totalWork);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.runtime.IProgressMonitor#done()
   */
  // Can't be final because ProbingSubMonitor overrides it.
  public void done()
  {
    cleanupActiveChild();
    int delta = totalParent - usedForParent;
    if (delta > 0)
    {
      root.worked(delta);
    }

    totalParent = 0;
    usedForParent = 0;
    totalForChildren = 0;
    usedForChildren = 0.0d;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.runtime.IProgressMonitor#internalWorked(double)
   */
  public final void internalWorked(double work)
  {
    checkCancelation();
    cleanupActiveChild();

    int delta = consume(work > 0.0d ? work : 0.0d);
    if (delta != 0)
    {
      root.worked(delta);
    }
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.runtime.IProgressMonitor#subTask(java.lang.String)
   */
  public final void subTask(String name)
  {
    checkCancelation();

    if ((flags & SUPPRESS_SUBTASK) == 0)
    {
      root.subTask(name);
    }
  }

  /**
   * Same as {@link #worked(int) worked(1)}.
   */
  public final void worked()
  {
    worked(DEFAULT_WORK);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.runtime.IProgressMonitor#worked(int)
   */
  // Can't be final because ProbingSubMonitor overrides it.
  public void worked(int work)
  {
    internalWorked(work);
  }

  /**
   * Same as {@link #setWorkRemaining(int) setWorkRemaining(totalParent - usedForParent)}.
   */
  public final void skipped(int ticks)
  {
    checkCancelation();
    setWorkRemaining(totalParent - usedForParent);
  }

  /**
   * Same as {@link #skipped(int) skipped(1)}.
   */
  public final void skipped()
  {
    skipped(DEFAULT_WORK);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.runtime.IProgressMonitor#setCanceled(boolean)
   */
  public final void setCanceled(boolean b)
  {
    root.setCanceled(b);
  }

  /**
   * Same as {@link #newChild(int) newChild(1)}.
   */
  public final SubMonitor newChild()
  {
    return newChild(DEFAULT_WORK, SUPPRESS_BEGINTASK);
  }

  /**
   * <p>Creates a sub progress monitor that will consume the given number of ticks from the
   * receiver. It is not necessary to call <code>beginTask</code> or <code>done</code> on the
   * result. However, the resulting progress monitor will not report any work after the first
   * call to done() or before ticks are allocated. Ticks may be allocated by calling beginTask
   * or setWorkRemaining.</p>
   *
   * <p>Each SubMonitor only has one active child at a time. Each time newChild() is called, the
   * result becomes the new active child and any unused progress from the previously-active child is
   * consumed.</p>
   *
   * <p>This is property makes it unnecessary to call done() on a SubMonitor instance, since child
   * monitors are automatically cleaned up the next time the parent is touched.</p>
   *
   * <code><pre>
   *      ////////////////////////////////////////////////////////////////////////////
   *      // Example 1: Typical usage of newChild
   *      void myMethod(IProgressMonitor parent) {
   *          SubMonitor progress = SubMonitor.convert(parent, 100);
   *          doSomething(progress.newChild(50));
   *          doSomethingElse(progress.newChild(50));
   *      }
   *
   *      ////////////////////////////////////////////////////////////////////////////
   *      // Example 2: Demonstrates the function of active children. Creating children
   *      // is sufficient to smoothly report progress, even if worked(...) and done()
   *      // are never called.
   *      void myMethod(IProgressMonitor parent) {
   *          SubMonitor progress = SubMonitor.convert(parent, 100);
   *
   *          for (int i = 0; i < 100; i++) {
   *              // Creating the next child monitor will clean up the previous one,
   *              // causing progress to be reported smoothly even if we don't do anything
   *              // with the monitors we create
   *            progress.newChild(1);
   *          }
   *      }
   *
   *      ////////////////////////////////////////////////////////////////////////////
   *      // Example 3: Demonstrates a common anti-pattern
   *      void wrongMethod(IProgressMonitor parent) {
   *          SubMonitor progress = SubMonitor.convert(parent, 100);
   *
   *          // WRONG WAY: Won't have the intended effect, as only one of these progress
   *          // monitors may be active at a time and the other will report no progress.
   *          callMethod(progress.newChild(50), computeValue(progress.newChild(50)));
   *      }
   *
   *      void rightMethod(IProgressMonitor parent) {
   *          SubMonitor progress = SubMonitor.convert(parent, 100);
   *
   *          // RIGHT WAY: Break up method calls so that only one SubMonitor is in use at a time.
   *          Object someValue = computeValue(progress.newChild(50));
   *          callMethod(progress.newChild(50), someValue);
   *      }
   * </pre></code>
   *
   * @param totalWork number of ticks to consume from the receiver
   * @return new sub progress monitor that may be used in place of a new SubMonitor
   */
  public final SubMonitor newChild(int totalWork)
  {
    return newChild(totalWork, SUPPRESS_BEGINTASK);
  }

  /**
   * <p>Creates a sub progress monitor that will consume the given number of ticks from the
   * receiver. It is not necessary to call <code>beginTask</code> or <code>done</code> on the
   * result. However, the resulting progress monitor will not report any work after the first
   * call to done() or before ticks are allocated. Ticks may be allocated by calling beginTask
   * or setWorkRemaining.</p>
   *
   * <p>Each SubMonitor only has one active child at a time. Each time newChild() is called, the
   * result becomes the new active child and any unused progress from the previously-active child is
   * consumed.</p>
   *
   * <p>This is property makes it unnecessary to call done() on a SubMonitor instance, since child
   * monitors are automatically cleaned up the next time the parent is touched.</p>
   *
   * <code><pre>
   *      ////////////////////////////////////////////////////////////////////////////
   *      // Example 1: Typical usage of newChild
   *      void myMethod(IProgressMonitor parent) {
   *          SubMonitor progress = SubMonitor.convert(parent, 100);
   *          doSomething(progress.newChild(50));
   *          doSomethingElse(progress.newChild(50));
   *      }
   *
   *      ////////////////////////////////////////////////////////////////////////////
   *      // Example 2: Demonstrates the function of active children. Creating children
   *      // is sufficient to smoothly report progress, even if worked(...) and done()
   *      // are never called.
   *      void myMethod(IProgressMonitor parent) {
   *          SubMonitor progress = SubMonitor.convert(parent, 100);
   *
   *          for (int i = 0; i < 100; i++) {
   *              // Creating the next child monitor will clean up the previous one,
   *              // causing progress to be reported smoothly even if we don't do anything
   *              // with the monitors we create
   *            progress.newChild(1);
   *          }
   *      }
   *
   *      ////////////////////////////////////////////////////////////////////////////
   *      // Example 3: Demonstrates a common anti-pattern
   *      void wrongMethod(IProgressMonitor parent) {
   *          SubMonitor progress = SubMonitor.convert(parent, 100);
   *
   *          // WRONG WAY: Won't have the intended effect, as only one of these progress
   *          // monitors may be active at a time and the other will report no progress.
   *          callMethod(progress.newChild(50), computeValue(progress.newChild(50)));
   *      }
   *
   *      void rightMethod(IProgressMonitor parent) {
   *          SubMonitor progress = SubMonitor.convert(parent, 100);
   *
   *          // RIGHT WAY: Break up method calls so that only one SubMonitor is in use at a time.
   *          Object someValue = computeValue(progress.newChild(50));
   *          callMethod(progress.newChild(50), someValue);
   *      }
   * </pre></code>
   *
   * @param totalWork number of ticks to consume from the receiver
   * @return new sub progress monitor that may be used in place of a new SubMonitor
   */
  public final SubMonitor newChild(int totalWork, int suppressFlags)
  {
    checkCancelation();

    double totalWorkDouble = totalWork > 0 ? totalWork : 0.0d;
    totalWorkDouble = Math.min(totalWorkDouble, totalForChildren - usedForChildren);
    cleanupActiveChild();

    // Compute the flags for the child. We want the net effect to be as though the child is
    // delegating to its parent, even though it is actually talking directly to the root.
    // This means that we need to compute the flags such that - even if a label isn't
    // suppressed by the child - if that same label would have been suppressed when the
    // child delegated to its parent, the child must explicitly suppress the label.
    int childFlags = SUPPRESS_NONE;

    if ((flags & SUPPRESS_SETTASKNAME) != 0)
    {
      // If the parent was ignoring labels passed to setTaskName, then the child will ignore
      // labels passed to either beginTask or setTaskName - since both delegate to setTaskName
      // on the parent
      childFlags |= SUPPRESS_SETTASKNAME | SUPPRESS_BEGINTASK;
    }

    if ((flags & SUPPRESS_SUBTASK) != 0)
    {
      // If the parent was suppressing labels passed to subTask, so will the child.
      childFlags |= SUPPRESS_SUBTASK;
    }

    // Note: the SUPPRESS_BEGINTASK flag does not affect the child since there
    // is no method on the child that would delegate to beginTask on the parent.
    childFlags |= suppressFlags;

    SubMonitor result = createSubMonitor(root, consume(totalWorkDouble), (int)totalWorkDouble, childFlags);
    lastSubMonitor = result;
    return result;
  }

  public void childDone()
  {
    // Do nothing
  }

  private void cleanupActiveChild()
  {
    if (lastSubMonitor == null)
    {
      return;
    }

    IProgressMonitor child = lastSubMonitor;
    lastSubMonitor = null;
    child.done();
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.runtime.IProgressMonitorWithBlocking#clearBlocked()
   */
  public final void clearBlocked()
  {
    checkCancelation();
    root.clearBlocked();
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.runtime.IProgressMonitorWithBlocking#setBlocked(org.eclipse.core.runtime.IStatus)
   */
  public final void setBlocked(IStatus reason)
  {
    checkCancelation();
    root.setBlocked(reason);
  }

  protected static boolean eq(Object o1, Object o2)
  {
    if (o1 == null)
    {
      return o2 == null;
    }
    if (o2 == null)
    {
      return false;
    }
    return o1.equals(o2);
  }

  /**
   * Enumerates the possible probing mode values {@link #OFF}, {@link #STANDARD} and {@link #FULL}.
   *
   * @author Eike Stepper
   */
  public enum ProbingMode
  {
    OFF, STANDARD, FULL;

    public static final ProbingMode DEFAULT = getDefault();

    private static ProbingMode getDefault()
    {
      String mode = System.getProperty("submonitor.probing");
      if (FULL.toString().equalsIgnoreCase(mode))
      {
        return FULL;
      }

      if (STANDARD.toString().equalsIgnoreCase(mode) || Boolean.TRUE.toString().equalsIgnoreCase(mode))
      {
        return STANDARD;
      }

      return OFF;
    }
  }
}
