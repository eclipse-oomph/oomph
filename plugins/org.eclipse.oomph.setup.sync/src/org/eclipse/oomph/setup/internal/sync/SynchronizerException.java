/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.sync;

/**
 * @author Eike Stepper
 */
public class SynchronizerException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public SynchronizerException()
  {
  }

  public SynchronizerException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public SynchronizerException(String message)
  {
    super(message);
  }

  public SynchronizerException(Throwable cause)
  {
    super(cause);
  }
}
