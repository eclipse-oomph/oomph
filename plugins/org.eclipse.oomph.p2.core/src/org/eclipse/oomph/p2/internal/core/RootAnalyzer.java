/*
 * Copyright (c) 2015, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.core.P2Util;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IRequirement;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public final class RootAnalyzer
{
  private RootAnalyzer()
  {
  }

  public static Set<IInstallableUnit> getRootUnits(IQueryable<IInstallableUnit> queryable, IProgressMonitor monitor)
  {
    Set<IInstallableUnit> rootIUs = new HashSet<IInstallableUnit>();

    for (IInstallableUnit iu : P2Util.asIterable(queryable.query(QueryUtil.createIUAnyQuery(), null)))
    {
      P2CorePlugin.checkCancelation(monitor);

      String id = iu.getId();
      if (id.endsWith(".source") || id.endsWith(".source.feature.group"))
      {
        continue;
      }

      if ("true".equalsIgnoreCase(iu.getProperty("org.eclipse.equinox.p2.type.category")))
      {
        continue;
      }

      if ("true".equalsIgnoreCase(iu.getProperty("org.eclipse.equinox.p2.type.product")))
      {
        continue;
      }

      rootIUs.add(iu);
    }

    removeImplicitUnits(rootIUs, queryable, monitor);
    return rootIUs;
  }

  public static void removeImplicitUnits(Map<IMetadataRepository, Set<IInstallableUnit>> result, IProgressMonitor monitor)
  {
    for (Map.Entry<IMetadataRepository, Set<IInstallableUnit>> entry : result.entrySet())
    {
      IMetadataRepository metadataRepository = entry.getKey();
      Set<IInstallableUnit> ius = entry.getValue();

      removeImplicitUnits(ius, metadataRepository, monitor);
    }
  }

  public static void removeImplicitUnits(Set<IInstallableUnit> ius, IQueryable<IInstallableUnit> queryable, IProgressMonitor monitor)
  {
    Set<IInstallableUnit> rootIUs = new HashSet<IInstallableUnit>(ius);
    Set<IInstallableUnit> currentlyVisitingIUs = new HashSet<IInstallableUnit>();
    Set<IInstallableUnit> visitedIUs = new HashSet<IInstallableUnit>();

    for (IInstallableUnit iu : ius)
    {
      removeImplicitUnits(iu, rootIUs, currentlyVisitingIUs, visitedIUs, queryable, monitor);
    }

    if (rootIUs.size() < ius.size())
    {
      ius.retainAll(rootIUs);
    }
  }

  private static void removeImplicitUnits(IInstallableUnit iu, Set<IInstallableUnit> rootIUs, Set<IInstallableUnit> currentlyVisitingIUs,
      Set<IInstallableUnit> visitedIUs, IQueryable<IInstallableUnit> queryable, IProgressMonitor monitor)
  {
    if (visitedIUs.add(iu))
    {
      currentlyVisitingIUs.add(iu);
      for (IRequirement requirement : iu.getRequirements())
      {
        for (IInstallableUnit requiredIU : P2Util.asIterable(queryable.query(QueryUtil.createMatchQuery(requirement.getMatches()), null)))
        {
          P2CorePlugin.checkCancelation(monitor);

          if (!currentlyVisitingIUs.contains(requiredIU))
          {
            rootIUs.remove(requiredIU);
            removeImplicitUnits(requiredIU, rootIUs, currentlyVisitingIUs, visitedIUs, queryable, monitor);
          }
        }
      }

      currentlyVisitingIUs.remove(iu);
    }
  }
}
