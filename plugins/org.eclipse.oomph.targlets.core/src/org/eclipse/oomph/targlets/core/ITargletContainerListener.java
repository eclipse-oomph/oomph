/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.core;

import org.eclipse.oomph.targlets.internal.core.TargletContainerListenerRegistry;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Eike Stepper
 */
public interface ITargletContainerListener
{
  public void handleTargletContainerEvent(TargletContainerEvent event, IProgressMonitor monitor) throws Exception;

  /**
   * @author Eike Stepper
   */
  public interface Registry
  {
    public static final Registry INSTANCE = TargletContainerListenerRegistry.INSTANCE;

    public void addListener(ITargletContainerListener listener);

    public void removeListener(ITargletContainerListener listener);
  }
}
