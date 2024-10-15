/*
 * Copyright (c) 2024 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui.internal.pde;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.TargetBundle;
import org.eclipse.pde.core.target.TargetFeature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public final class TargetSnapshot implements Comparable<TargetSnapshot>
{
  private static final TargetElement[] NO_ELEMENTS = {};

  private final Target target;

  private final int number;

  private final long timeStamp;

  private final String xml;

  private State state = State.Resolving;

  private IStatus resolutionStatus;

  private Set<TargetElement> elements = new HashSet<>();

  private TargetElement[] sortedElements = NO_ELEMENTS;

  TargetSnapshot(Target target, int number, String xml)
  {
    this.target = Objects.requireNonNull(target);
    this.number = number;
    this.xml = xml;

    timeStamp = System.currentTimeMillis();
  }

  public Target getTarget()
  {
    return target;
  }

  public int getNumber()
  {
    return number;
  }

  public long getTimeStamp()
  {
    return timeStamp;
  }

  public String getXML()
  {
    return xml;
  }

  public State getState()
  {
    return state;
  }

  public IStatus getResolutionStatus()
  {
    return resolutionStatus;
  }

  public TargetElement[] getElements()
  {
    return sortedElements;
  }

  public boolean restore()
  {
    TargetManager manager = getTarget().getManager();
    return manager.restoreSnapshot(this);
  }

  public Delta createDelta(TargetSnapshot previous)
  {
    return new Delta(this, previous);
  }

  public Delta createDelta()
  {
    TargetSnapshot previous = target.getPreviousSnapshot(this);
    if (previous == null)
    {
      return null;
    }

    return new Delta(this, previous);
  }

  @Override
  public int compareTo(TargetSnapshot o)
  {
    return -Integer.compare(number, o.number);
  }

  @Override
  public String toString()
  {
    return "TargetSnapshot[" + target + ", " + number + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  void setResolving()
  {
    resolutionStatus = null;
    state = State.fromResolutionStatus(resolutionStatus);
  }

  boolean update(ITargetDefinition definition)
  {
    if (definition.isResolved())
    {
      doUpdate(definition);
      return true;
    }

    Job.createSystem(NLS.bind(Messages.TargetSnapshot_ResolveJob_name0, target.getName()), monitor -> {
      TargetManager manager = target.getManager();
      manager.snapshotResolutionStarted(TargetSnapshot.this, definition);

      try
      {
        definition.resolve(monitor);
      }
      catch (Exception ex)
      {
        //$FALL-THROUGH$
      }

      manager.snapshotResolutionFinished(TargetSnapshot.this, definition);
      return Status.OK_STATUS;
    }).schedule();

    return false;
  }

  void doUpdate(ITargetDefinition definition)
  {
    resolutionStatus = definition.getStatus();
    state = State.fromResolutionStatus(resolutionStatus);
    elements = new HashSet<>();

    if (state == State.Resolved)
    {
      for (TargetFeature targetFeature : definition.getAllFeatures())
      {
        elements.add(TargetElement.fromFeature(this, targetFeature));
      }

      for (TargetBundle targetBundle : definition.getAllBundles())
      {
        elements.add(TargetElement.fromBundle(this, targetBundle));
      }

      sortedElements = elements.toArray(new TargetElement[elements.size()]);
      Arrays.sort(sortedElements);
    }
    else
    {
      sortedElements = new TargetElement[0];
    }
  }

  /**
   * @author Eike Stepper
   */
  public enum State
  {
    Resolving, Canceled, Resolved, Error;

    public static State fromResolutionStatus(IStatus status)
    {
      if (status == null)
      {
        return Resolving;
      }

      int severity = status.getSeverity();
      switch (severity)
      {
        case IStatus.CANCEL:
          return Canceled;

        case IStatus.OK:
          return Resolved;

        default:
          return Error;
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class Delta
  {
    private final TargetSnapshot snapshot;

    private final TargetSnapshot previous;

    private final Set<TargetElement> removedElements = new LinkedHashSet<>();

    private final Set<TargetElement> addedElements = new LinkedHashSet<>();

    private final Change[] changes;

    private Delta(TargetSnapshot snapshot, TargetSnapshot previous)
    {
      this.snapshot = Objects.requireNonNull(snapshot);
      this.previous = Objects.requireNonNull(previous);

      List<Change> list = new ArrayList<>();
      compare(snapshot, previous, removedElements, list, Change.Kind.Removal);
      compare(previous, snapshot, addedElements, list, Change.Kind.Addition);

      list.sort(null);
      changes = list.toArray(new Change[list.size()]);
    }

    public TargetSnapshot getSnapshot()
    {
      return snapshot;
    }

    public TargetSnapshot getPrevious()
    {
      return previous;
    }

    public Set<TargetElement> getRemovedElements()
    {
      return Collections.unmodifiableSet(removedElements);
    }

    public Set<TargetElement> getAddedElements()
    {
      return Collections.unmodifiableSet(addedElements);
    }

    public Change[] getChanges()
    {
      return changes;
    }

    private void compare(TargetSnapshot snapshot, TargetSnapshot previous, Set<TargetElement> elements, List<Change> changes, Change.Kind changeKind)
    {
      for (TargetElement previousElement : previous.sortedElements)
      {
        if (!snapshot.elements.contains(previousElement))
        {
          elements.add(previousElement);
          changes.add(new Change(this, previousElement, changeKind));
        }
      }
    }

    /**
     * @author Eike Stepper
     */
    public static final class Change implements Comparable<Change>
    {
      private final Delta delta;

      private final TargetElement element;

      private final Kind kind;

      private Change(Delta delta, TargetElement element, Kind kind)
      {
        this.delta = Objects.requireNonNull(delta);
        this.element = Objects.requireNonNull(element);
        this.kind = Objects.requireNonNull(kind);

        TargetSnapshot snapshot = element.getSnapshot();
        if (snapshot != delta.getSnapshot() && snapshot != delta.getPrevious())
        {
          throw new IllegalArgumentException();
        }
      }

      public Delta getDelta()
      {
        return delta;
      }

      public TargetElement getElement()
      {
        return element;
      }

      public Kind getKind()
      {
        return kind;
      }

      @Override
      public int compareTo(Change o)
      {
        return element.compareTo(o.element);
      }

      @Override
      public String toString()
      {
        return kind + "[" + element + "]"; //$NON-NLS-1$ //$NON-NLS-2$
      }

      /**
       * @author Eike Stepper
       */
      public enum Kind
      {
        Removal, Addition,
      }
    }
  }
}
