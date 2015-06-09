/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public abstract class StringExpander
{
  public static final Pattern STRING_EXPANSION_PATTERN = Pattern.compile("\\$(\\{([^${}|/]+)(\\|([^{}/]+))?([^{}]*)}|\\$)");

  static final String[] CONTROL_CHARACTER_REPLACEMENTS = { "${0x0}", "${0x1}", "${0x2}", "${0x3}", "${0x4}", "${0x5}", "${0x6}", "${0x7}", "${0x8}", "${0x9}",
      "${0xA}", "${0xB}", "${0xC}", "${0xD}", "${0xE}", "${0xF}", "${0x10}", "${0x11}", "${0x12}", "${0x13}", "${0x14}", "${0x15}", "${0x16}", "${0x17}",
      "${0x18}", "${0x19}", "${0x1A}", "${0x1B}", "${0x1C}", "${0x1D}", "${0x1E}", "${0x1F}" };

  private static boolean NEEDS_PATH_SEPARATOR_CONVERSION = File.separatorChar == '\\';

  protected static final Map<String, String> CONTROL_CHARACTER_VALUES = new HashMap<String, String>();

  static
  {
    for (int i = 0, length = CONTROL_CHARACTER_REPLACEMENTS.length; i < length; ++i)
    {
      CONTROL_CHARACTER_VALUES.put(CONTROL_CHARACTER_REPLACEMENTS[i].substring(2, 5), Character.toString((char)i));
    }
  }

  protected static String resolve(StringExpander stringExpander, String key)
  {
    return stringExpander.resolve(key);
  }

  protected static boolean isUnexpanded(StringExpander stringExpander, String key)
  {
    return stringExpander.isUnexpanded(key);
  }

  protected static String filter(StringExpander stringExpander, String value, String filterName)
  {
    return stringExpander.filter(value, filterName);
  }

  protected abstract String resolve(String key);

  protected abstract boolean isUnexpanded(String key);

  protected abstract String filter(String value, String filterName);

  public String expandString(String string)
  {
    return expandString(string, null);
  }

  public String expandString(String string, Set<String> keys)
  {
    if (string == null)
    {
      return null;
    }

    StringBuilder result = new StringBuilder();
    int previous = 0;
    boolean unresolved = false;
    for (Matcher matcher = StringExpander.STRING_EXPANSION_PATTERN.matcher(string); matcher.find();)
    {
      result.append(string.substring(previous, matcher.start()));
      String key = matcher.group(1);
      if ("$".equals(key))
      {
        result.append('$');
      }
      else
      {
        key = matcher.group(2);
        String suffix = matcher.group(5);
        if (NEEDS_PATH_SEPARATOR_CONVERSION)
        {
          suffix = suffix.replace('/', File.separatorChar);
        }

        boolean isUnexpanded = isUnexpanded(key);
        String value = isUnexpanded ? null : resolve(key);
        if (value == null)
        {
          if (keys != null)
          {
            unresolved = true;

            if (!isUnexpanded)
            {
              keys.add(key);
            }
          }
          else if (!unresolved)
          {
            result.append(matcher.group());
          }
        }
        else
        {
          String filters = matcher.group(4);
          if (filters != null)
          {
            for (String filterName : filters.split("\\|"))
            {
              value = filter(value, filterName);
              if (value == null)
              {
                if (keys != null)
                {
                  unresolved = true;

                  if (!isUnexpanded)
                  {
                    keys.add(key);
                  }
                }
                else if (!unresolved)
                {
                  result.append(matcher.group());
                }

                continue;
              }
            }
          }

          if (!unresolved)
          {
            result.append(value);
            result.append(suffix);
          }
        }
      }

      previous = matcher.end();
    }

    if (unresolved)
    {
      return null;
    }

    result.append(string.substring(previous));
    return result.toString();
  }

}
