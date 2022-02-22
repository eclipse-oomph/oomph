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
package org.eclipse.oomph.util;

/**
 * @author Eike Stepper
 */
public interface Confirmer
{
  public static final Confirmer ACCEPT = new Confirmer()
  {
    @Override
    public Confirmation confirm(boolean defaultConfirmed, Object info)
    {
      return new Confirmation(true, false);
    }
  };

  public static final Confirmer DECLINE = new Confirmer()
  {
    @Override
    public Confirmation confirm(boolean defaultConfirmed, Object info)
    {
      return new Confirmation(false, false);
    }
  };

  public static final Confirmer DEFAULT = new Confirmer()
  {
    @Override
    public Confirmation confirm(boolean defaultConfirmed, Object info)
    {
      return new Confirmation(defaultConfirmed, false);
    }
  };

  public Confirmation confirm(boolean defaultConfirmed, Object info);

  /**
   * @author Eike Stepper
   */
  public final class Confirmation
  {
    private final boolean confirmed;

    private final boolean remember;

    public Confirmation(boolean confirmed, boolean remember)
    {
      this.confirmed = confirmed;
      this.remember = remember;
    }

    public boolean isConfirmed()
    {
      return confirmed;
    }

    public boolean isRemember()
    {
      return remember;
    }

    @Override
    public String toString()
    {
      return (confirmed ? Messages.Confirmer_Accept_label : Messages.Confirmer_Decline_label) + (remember ? " " + Messages.Confirmer_Remember_label : ""); //$NON-NLS-1$ //$NON-NLS-2$
    }
  }
}
