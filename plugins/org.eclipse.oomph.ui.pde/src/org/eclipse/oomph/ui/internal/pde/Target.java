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

import org.eclipse.core.resources.IFile;
import org.eclipse.pde.core.target.ITargetHandle;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public final class Target implements Comparable<Target>
{
  private static final TargetSnapshot[] NO_SNAPSHOTS = {};

  private final TargetManager manager;

  private final ITargetHandle handle;

  private String name;

  private Set<TargetSnapshot> snapshots;

  private TargetSnapshot[] sortedSnapshots;

  private int lastSnapshotNumber;

  Target(TargetManager manager, ITargetHandle handle, String name)
  {
    this.manager = Objects.requireNonNull(manager);
    this.handle = Objects.requireNonNull(handle);
    this.name = name;

    clearSnapshots();
  }

  public TargetManager getManager()
  {
    return manager;
  }

  public ITargetHandle getHandle()
  {
    return handle;
  }

  public String getName()
  {
    return name;
  }

  @SuppressWarnings("restriction")
  public String getLocation()
  {
    if (handle instanceof org.eclipse.pde.internal.core.target.WorkspaceFileTargetHandle)
    {
      IFile file = ((org.eclipse.pde.internal.core.target.WorkspaceFileTargetHandle)handle).getTargetFile();
      return file.getFullPath().toString();
    }

    if (handle instanceof org.eclipse.pde.internal.core.target.ExternalFileTargetHandle)
    {
      URI uri = ((org.eclipse.pde.internal.core.target.ExternalFileTargetHandle)handle).getLocation();
      return uri.toString();
    }

    return null;
  }

  @SuppressWarnings("restriction")
  public IFile getWorkspaceFile()
  {
    if (handle instanceof org.eclipse.pde.internal.core.target.WorkspaceFileTargetHandle)
    {
      return ((org.eclipse.pde.internal.core.target.WorkspaceFileTargetHandle)handle).getTargetFile();
    }

    return null;
  }

  public boolean isActive()
  {
    return manager.getActiveTarget() == this;
  }

  public boolean isDefault()
  {
    return Objects.equals(name, manager.getDefaultTargetName());
  }

  public TargetSnapshot[] getSnapshots()
  {
    return sortedSnapshots;
  }

  public List<TargetSnapshot> getOldSnapshots()
  {
    TargetSnapshot[] tmp = sortedSnapshots;
    if (tmp.length <= 1)
    {
      return Collections.emptyList();
    }

    List<TargetSnapshot> result = new ArrayList<>();

    for (int i = 1; i < tmp.length; i++)
    {
      result.add(tmp[i]);
    }

    return result;
  }

  public TargetSnapshot getCurrentSnapshot()
  {
    TargetSnapshot[] tmp = sortedSnapshots;
    return tmp.length == 0 ? null : tmp[0];
  }

  public TargetSnapshot getPreviousSnapshot(TargetSnapshot snapshot)
  {
    TargetSnapshot[] tmp = sortedSnapshots;
    int index = indexOfSnapshot(tmp, snapshot);
    return index != -1 && index < tmp.length - 1 ? tmp[index + 1] : null;
  }

  public TargetSnapshot getNextSnapshot(TargetSnapshot snapshot)
  {
    TargetSnapshot[] tmp = sortedSnapshots;
    int index = indexOfSnapshot(tmp, snapshot);
    return index != -1 && index > 0 ? tmp[index - 1] : null;
  }

  public boolean isMonitored()
  {
    return manager.getMonitoredTarget() == this;
  }

  public boolean monitor()
  {
    return manager.monitorTarget(this);
  }

  public TargetSnapshot takeSnapshot()
  {
    return manager.targetSaved(handle, true);
  }

  public void deleteSnapshots(Collection<TargetSnapshot> snapshots)
  {
    if (!snapshots.isEmpty())
    {
      manager.deleteSnapshots(this, snapshots);
    }
  }

  public boolean isResolved()
  {
    return snapshots != null;
  }

  @Override
  public int compareTo(Target o)
  {
    int result = name == null ? -1 : name.compareTo(o.name);
    if (result == 0)
    {
      result = handle.toString().compareTo(o.handle.toString());
    }

    return result;
  }

  @Override
  public String toString()
  {
    return "Target[" + name + " --> " + handle + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  void setName(String name)
  {
    this.name = name;
  }

  TargetSnapshot internalTakeSnapshot(String xml)
  {
    TargetSnapshot snapshot = new TargetSnapshot(this, ++lastSnapshotNumber, xml);
    snapshots.add(snapshot);

    updateSortedSnapshots();

    return snapshot;
  }

  void internalDeleteSnapshots(Collection<TargetSnapshot> snapshots)
  {
    this.snapshots.removeAll(snapshots);
    updateSortedSnapshots();
  }

  void clearSnapshots()
  {
    snapshots = new HashSet<>();
    sortedSnapshots = NO_SNAPSHOTS;
  }

  private void updateSortedSnapshots()
  {
    sortedSnapshots = snapshots.toArray(new TargetSnapshot[snapshots.size()]);
    Arrays.sort(sortedSnapshots);
  }

  private static int indexOfSnapshot(TargetSnapshot[] tmp, TargetSnapshot snapshot)
  {
    for (int i = 0; i < tmp.length; i++)
    {
      if (tmp[i] == snapshot)
      {
        return i;
      }
    }

    return -1;
  }
}
