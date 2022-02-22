/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.setup;

import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.UserCallback;

import java.util.List;

/**
 * @author Eike Stepper
 */
public interface SetupPrompter
{
  public static final SetupPrompter OK = new Default(true);

  public static final SetupPrompter CANCEL = new Default(false);

  public OS getOS();

  public String getVMPath();

  public UserCallback getUserCallback();

  public String getValue(VariableTask variable);

  public boolean promptVariables(List<? extends SetupTaskContext> performers);

  /**
   * @author Eike Stepper
   */
  public static final class Default implements SetupPrompter
  {
    private final boolean ok;

    public Default(boolean ok)
    {
      this.ok = ok;
    }

    @Override
    public OS getOS()
    {
      return OS.INSTANCE;
    }

    @Override
    public String getVMPath()
    {
      return null;
    }

    @Override
    public UserCallback getUserCallback()
    {
      return null;
    }

    @Override
    public String getValue(VariableTask variable)
    {
      return null;
    }

    @Override
    public boolean promptVariables(List<? extends SetupTaskContext> performers)
    {
      return ok;
    }
  }
}
