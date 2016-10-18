/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.Profile;

import org.eclipse.jface.viewers.ViewerComparator;

/**
 * @author Eike Stepper
 */
public class P2ViewerSorter extends ViewerComparator
{
  @Override
  public int category(Object element)
  {
    if (element instanceof AgentManager)
    {
      return 0;
    }

    if (element instanceof Agent)
    {
      return 1;
    }

    if (element instanceof BundlePool)
    {
      return 2;
    }

    if (element instanceof Profile)
    {
      return 3;
    }

    return 4;
  }
}
