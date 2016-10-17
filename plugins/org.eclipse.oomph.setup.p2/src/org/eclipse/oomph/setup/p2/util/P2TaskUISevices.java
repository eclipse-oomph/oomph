/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.p2.util;

import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.setup.p2.P2Task;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;

import java.util.Set;

/**
 * An instance of this service is used to prompt the user for what action to take when the current profile doesn't satisfy the requirements of a {@link P2Task}.
 *
 * @author Ed Merks
 */
public abstract class P2TaskUISevices
{
  /**
   * This will prompt whether to proceed with installing the unsatisfied requirement.
   */
  public abstract boolean handleUnsatisfiedRequirements(Set<Requirement> unsatisifiedRequirements, Set<IInstallableUnit> availableIUs);
}
