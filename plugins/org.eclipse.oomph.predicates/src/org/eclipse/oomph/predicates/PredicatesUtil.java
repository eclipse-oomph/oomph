/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.predicates;

import org.eclipse.emf.common.util.EList;

import org.eclipse.core.resources.IResource;

/**
 * @author Eike Stepper
 */
public final class PredicatesUtil
{
  private PredicatesUtil()
  {
  }

  public static boolean matchesPredicates(IResource resource, EList<Predicate> predicates)
  {
    if (resource == null)
    {
      return false;
    }

    if (predicates == null || predicates.isEmpty())
    {
      return true;
    }

    for (Predicate predicate : predicates)
    {
      if (predicate.matches(resource))
      {
        return true;
      }
    }

    return false;
  }
}
