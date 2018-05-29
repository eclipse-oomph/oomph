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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.P2Exception;
import org.eclipse.oomph.p2.core.AgentManagerElement;
import org.eclipse.oomph.util.StringUtil;

/**
 * @author Eike Stepper
 */
public abstract class AgentManagerElementImpl implements AgentManagerElement
{
  public AgentManagerElementImpl()
  {
  }

  public abstract String getElementType();

  public final void delete()
  {
    delete(false);
  }

  public final void delete(boolean force)
  {
    if (isUsed() && !force)
    {
      throw new P2Exception(StringUtil.cap(getElementType()) + " is used: " + this);
    }

    doDelete();
  }

  protected abstract void doDelete();
}
