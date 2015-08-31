/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.sync;

/**
 * @author Eike Stepper
 */
public class SnychronizerException extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public SnychronizerException()
  {
  }

  public SnychronizerException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public SnychronizerException(String message)
  {
    super(message);
  }

  public SnychronizerException(Throwable cause)
  {
    super(cause);
  }

}