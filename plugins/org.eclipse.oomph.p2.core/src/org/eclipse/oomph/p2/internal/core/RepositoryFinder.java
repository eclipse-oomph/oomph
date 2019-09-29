/*
 * Copyright (c) 2019 Eike Stepper and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.internal.core.P2Index.Repository;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class RepositoryFinder implements IApplication
{
  private static final boolean SUPPRESS_STATS = PropertiesUtil.isProperty("suppress.stats");

  public Object start(IApplicationContext context) throws Exception
  {
    String[] arguments = (String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
    Pattern pattern = Pattern.compile(arguments[0]);
    List<String> urls = new ArrayList<String>();

    Repository[] repositories = P2Index.INSTANCE.getRepositories();
    if (repositories != null)
    {
      for (Repository repository : repositories)
      {
        String url = repository.getLocation().toString();
        if (pattern.matcher(url).find())
        {
          urls.add(url);
        }
      }
    }

    if (!SUPPRESS_STATS)
    {
      System.out.println("Found " + urls.size() + " of " + repositories.length + " repositories:");
    }

    Collections.sort(urls);
    for (String url : urls)
    {
      System.out.println(url);
    }

    return null;
  }

  public void stop()
  {
  }
}
