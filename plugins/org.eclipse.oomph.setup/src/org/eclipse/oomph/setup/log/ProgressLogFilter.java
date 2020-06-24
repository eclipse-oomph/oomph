/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.log;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ProgressLogFilter
{
  private static final String[] IGNORED_PREFIXES = { "Scanning Git", //$NON-NLS-1$
      "Re-indexing", //$NON-NLS-1$
      "Calculating Decorations", //$NON-NLS-1$
      "Decorating", //$NON-NLS-1$
      "http://", //$NON-NLS-1$
      "The user operation is waiting", //$NON-NLS-1$
      "Git repository changed", //$NON-NLS-1$
      "Refreshing ", //$NON-NLS-1$
      "Opening ", //$NON-NLS-1$
      "Connecting project ", //$NON-NLS-1$
      "Connected project ", //$NON-NLS-1$
      "Searching for associated repositories.", //$NON-NLS-1$
      "Preparing type ", //$NON-NLS-1$
      "Loading project description", //$NON-NLS-1$
      "Generating cspec from PDE artifacts", //$NON-NLS-1$
      "Reporting encoding changes", //$NON-NLS-1$
      "Saving", //$NON-NLS-1$
      "Downloading software", //$NON-NLS-1$
      "Java indexing...", //$NON-NLS-1$
      "Computing Git status for ", //$NON-NLS-1$
      "Configuring ", //$NON-NLS-1$
      "Invoking builder on ", //$NON-NLS-1$
      "Invoking '", //$NON-NLS-1$
      "Verifying ", //$NON-NLS-1$
      "Updating ...", //$NON-NLS-1$
      "Reading saved build state for project ", //$NON-NLS-1$
      "Reading resource change information for ", //$NON-NLS-1$
      "Cleaning output folder for ", //$NON-NLS-1$
      "Copying resources to the output folder", //$NON-NLS-1$
      " adding component ", //$NON-NLS-1$
      "Preparing to build", //$NON-NLS-1$
      "Compiling ", //$NON-NLS-1$
      "Analyzing ", //$NON-NLS-1$
      "Comparing ", //$NON-NLS-1$
      "Checking ", //$NON-NLS-1$
      "Build done", //$NON-NLS-1$
      "Processing API deltas...", //$NON-NLS-1$
      "Create.", //$NON-NLS-1$
      "Reading plug-ins", //$NON-NLS-1$
      "Processing", //$NON-NLS-1$
      "Querying repository", //$NON-NLS-1$
      "Synchronizing query: ", //$NON-NLS-1$
      "Receiving related tasks", //$NON-NLS-1$
      "Receiving task ", //$NON-NLS-1$
      "Updating repository state", //$NON-NLS-1$
      "Processing Local", //$NON-NLS-1$
      "Resources to refresh:" //$NON-NLS-1$
  };

  private String lastLine;

  private Set<String> downloadProgressLines = Collections.synchronizedSet(new HashSet<String>());

  public String filter(String line)
  {
    if (line == null || line.length() == 0 || Character.isLowerCase(line.charAt(0)) || line.equals("Updating") || line.endsWith(" remaining.") //$NON-NLS-1$ //$NON-NLS-2$
        || startsWithIgnoredPrefix(line))
    {
      return null;
    }

    if (line.endsWith("/s)")) //$NON-NLS-1$
    {
      int index = line.lastIndexOf(" ("); //$NON-NLS-1$
      if (index != -1)
      {
        line = line.substring(0, index);
        if (!downloadProgressLines.add(line))
        {
          return null;
        }
      }
    }

    if (line.equals(lastLine))
    {
      return null;
    }

    lastLine = line;
    return line;
  }

  private static boolean startsWithIgnoredPrefix(String line)
  {
    for (int i = 0; i < IGNORED_PREFIXES.length; i++)
    {
      String prefix = IGNORED_PREFIXES[i];
      if (line.startsWith(prefix))
      {
        return true;
      }
    }

    return false;
  }
}
