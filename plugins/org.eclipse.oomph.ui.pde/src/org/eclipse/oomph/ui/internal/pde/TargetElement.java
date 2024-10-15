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

import org.eclipse.equinox.frameworkadmin.BundleInfo;
import org.eclipse.pde.core.target.TargetBundle;
import org.eclipse.pde.core.target.TargetFeature;

import org.osgi.framework.Version;

import java.io.File;
import java.net.URI;
import java.util.Objects;

/**
 * @author Eike Stepper
 */
public final class TargetElement implements Comparable<TargetElement>
{
  private static final String SOURCE = "Source"; //$NON-NLS-1$

  private final TargetSnapshot snapshot;

  private final Type type;

  private final String name;

  private final Version version;

  private final boolean source;

  private final String location;

  private TargetElement(TargetSnapshot snapshot, Type type, String name, String version, boolean source, String location)
  {
    this.snapshot = Objects.requireNonNull(snapshot);
    this.type = Objects.requireNonNull(type);
    this.name = Objects.requireNonNull(name);
    this.version = Version.parseVersion(version);
    this.source = source;
    this.location = location;
  }

  public TargetSnapshot getSnapshot()
  {
    return snapshot;
  }

  public Type getType()
  {
    return type;
  }

  public String getName()
  {
    return name;
  }

  public Version getVersion()
  {
    return version;
  }

  public boolean isSource()
  {
    return source;
  }

  public String getLocation()
  {
    return location;
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(name, type, version);
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

    if (getClass() != obj.getClass())
    {
      return false;
    }

    TargetElement other = (TargetElement)obj;
    return name.equals(other.name) && type == other.type && version.equals(other.version);
  }

  @Override
  public int compareTo(TargetElement o)
  {
    int result = name.compareTo(o.name);
    if (result == 0)
    {
      result = Integer.compare(type.ordinal(), o.type.ordinal());
      if (result == 0)
      {
        result = version.compareTo(o.version);
      }
    }

    return result;
  }

  @Override
  public String toString()
  {
    return typeString() + '[' + name + '@' + version + ']';
  }

  private String typeString()
  {
    return (source ? SOURCE : "") + type; //$NON-NLS-1$
  }

  public static TargetElement fromFeature(TargetSnapshot snapshot, TargetFeature targetFeature)
  {
    String location = targetFeature.getLocation();
    return new TargetElement(snapshot, Type.Feature, targetFeature.getId(), targetFeature.getVersion(), false, location);
  }

  public static TargetElement fromBundle(TargetSnapshot snapshot, TargetBundle targetBundle)
  {
    BundleInfo bundleInfo = targetBundle.getBundleInfo();
    Type type = targetBundle.isFragment() ? Type.Fragment : Type.Bundle;
    String name = bundleInfo.getSymbolicName();
    String version = bundleInfo.getVersion();
    boolean source = targetBundle.isSourceBundle();

    URI uri = targetBundle.getBundleInfo().getLocation();
    String location = "file".equals(uri.getScheme()) ? new File(uri.getPath()).getAbsolutePath() : uri.toString(); //$NON-NLS-1$

    return new TargetElement(snapshot, type, name, version, source, location);
  }

  /**
   * @author Eike Stepper
   */
  public enum Type
  {
    Feature, Bundle, Fragment;
  }
}
