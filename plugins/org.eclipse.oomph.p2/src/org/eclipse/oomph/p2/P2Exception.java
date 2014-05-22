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
package org.eclipse.oomph.p2;

/**
 * @author Eike Stepper
 */
public class P2Exception extends RuntimeException
{
  private static final long serialVersionUID = 1L;

  public P2Exception()
  {
  }

  public P2Exception(String message)
  {
    super(message);
  }

  public P2Exception(Throwable cause)
  {
    super(cause);
  }

  public P2Exception(String message, Throwable cause)
  {
    super(message, cause);
  }

  public P2Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
