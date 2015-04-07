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

/**
 * @author Eike Stepper
 */
public final class SetupUtil
{

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
    Matcher matcher = StringExpander.STRING_EXPANSION_PATTERN.matcher(string);
    while (matcher.find())
    {
      matcher.appendReplacement(result, "\\$$0");
    }

    matcher.appendTail(result);

    for (int i = 0, length = result.length(); i < length; ++i)
    {
      char c = result.charAt(i);
      if (c < StringExpander.CONTROL_CHARACTER_REPLACEMENTS.length)
      {
        result.replace(i, i + 1, StringExpander.CONTROL_CHARACTER_REPLACEMENTS[c]);
      }
    }

    return result.toString();
  }
}
