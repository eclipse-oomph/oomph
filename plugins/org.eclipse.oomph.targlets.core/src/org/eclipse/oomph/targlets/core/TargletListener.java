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
package org.eclipse.oomph.targlets.core;

import org.eclipse.oomph.targlets.internal.core.TargletListenerRegistryImpl;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Eike Stepper
 */
public interface TargletListener
{
  public void handleTargletEvent(TargletEvent event, IProgressMonitor monitor) throws Exception;

  /**
   * @author Eike Stepper
   */
  public interface Registry
  {
    public static final Registry INSTANCE = TargletListenerRegistryImpl.INSTANCE;

    public void addListener(TargletListener listener);

    public void removeListener(TargletListener listener);
  }
}
