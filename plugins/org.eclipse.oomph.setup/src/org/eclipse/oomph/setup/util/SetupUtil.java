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
package org.eclipse.oomph.setup.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public final class SetupUtil
{
  private static final Pattern STRING_EXPANSION_PATTERN = Pattern.compile("\\$(\\{([^${}|/]+)(\\|([^{}/]+))?([^{}]*)}|\\$)");

  private SetupUtil()
  {
  }

  public static String escape(String string)
  {
    if (string == null)
    {
      return null;
    }

    StringBuffer result = new StringBuffer();
    Matcher matcher = STRING_EXPANSION_PATTERN.matcher(string);
    while (matcher.find())
    {
      matcher.appendReplacement(result, "\\$$0");
    }

    matcher.appendTail(result);
    return result.toString();
  }
}
