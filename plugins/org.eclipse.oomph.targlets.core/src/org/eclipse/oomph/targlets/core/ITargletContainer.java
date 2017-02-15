/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.core;

import org.eclipse.oomph.targlets.Targlet;

import org.eclipse.emf.common.util.EList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetLocation;

import java.util.Collection;

/**
 * @author Eike Stepper
 */
public interface ITargletContainer extends ITargetLocation
{
  public static final String TYPE = "Targlet";

  public static final String IU_PROPERTY_SOURCE = "org.eclipse.oomph.targlet.source";

  public String getID();

  public void setID(String newID) throws CoreException;

  public ITargletContainerDescriptor getDescriptor();

  public ITargetDefinition getTargetDefinition();

  /**
   * Returns a copy of the targlet with the given name in this targlet container. This copy can be freely modified but the modifications won't have an impact
   * on a targlet container unless the copy is set back into a container via {@link #setTarglets(Collection)}.
   */
  public Targlet getTarglet(String name);

  public int getTargletIndex(String name);

  public boolean hasTarglet(String name);

  /**
   * Returns a copy of the targlets in this targlet container. This copy can be freely modified but the modifications won't have an impact
   * on a targlet container unless the copy is set back into a container via {@link #setTarglets(Collection)}.
   */
  public EList<Targlet> getTarglets();

  /**
   * Copies the passed targlets into this targlet container. Modifications of the passed targlets after the call
   * to this method won't have an impact on this targlet container.
   */
  public void setTarglets(Collection<? extends Targlet> targlets) throws CoreException;

  public boolean isIncludeSources();

  public boolean isIncludeAllPlatforms();

  public boolean isIncludeAllRequirements();

  public boolean isIncludeBinaryEquivalents();

  public String getEnvironmentProperties();

  public String getNLProperty();

  public String getDigest();

  public void forceUpdate(boolean activateTargetDefinition, boolean mirrors, IProgressMonitor monitor) throws CoreException;

  public IStatus updateProfile(IProgressMonitor monitor);
}
