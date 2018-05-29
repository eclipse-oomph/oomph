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
package org.eclipse.oomph.resources.backend;

/**
 * @author Eike Stepper
 */
public class BackendException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public BackendException()
  {
  }

  public BackendException(String message)
  {
    super(message);
  }

  public BackendException(Throwable cause)
  {
    super(cause);
  }

  public BackendException(String message, Throwable cause)
  {
    super(message, cause);
  }
}
