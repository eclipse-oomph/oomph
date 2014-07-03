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
package org.eclipse.oomph.setup.log;

public class ProgressLogFilter
{
  private static final String[] IGNORED_PREFIXES = { "Scanning Git", "Re-indexing", "Calculating Decorations", "Decorating", "http://",
      "The user operation is waiting", "Git repository changed", "Refreshing ", "Opening ", "Connecting project ", "Connected project ",
      "Searching for associated repositories.", "Preparing type ", "Loading project description", "Generating cspec from PDE artifacts",
      "Reporting encoding changes", "Saving", "Downloading software", "Java indexing...", "Computing Git status for ", "Configuring ", "Invoking builder on ",
      "Invoking '", "Verifying ", "Updating ...", "Reading saved build state for project ", "Reading resource change information for ",
      "Cleaning output folder for ", "Copying resources to the output folder", " adding component ", "Preparing to build", "Compiling ", "Analyzing ",
      "Comparing ", "Checking ", "Build done", "Processing API deltas...", "Create.", "Reading plug-ins", "Processing", "Querying repository",
      "Synchronizing query: ", "Receiving related tasks", "Receiving task ", "Updating repository state", "Processing Local" };

  private String lastLine;

  public String filter(String line)
  {
    if (line == null || line.length() == 0 || Character.isLowerCase(line.charAt(0)) || line.equals("Updating") || line.endsWith(" remaining.")
        || startsWithIgnoredPrefix(line))
    {
      return null;
    }

    if (line.endsWith("/s)"))
    {
      int index = line.lastIndexOf(" (");
      if (index != -1)
      {
        line = line.substring(0, index);
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
