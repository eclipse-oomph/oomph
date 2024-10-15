/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.util.pde;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.pde.core.target.ITargetPlatformService;

/**
 * @author Eike Stepper
 */
@FunctionalInterface
public interface TargetPlatformRunnable<T>
{
  public T run(ITargetPlatformService service) throws CoreException;
}
