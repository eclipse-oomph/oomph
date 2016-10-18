/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
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
  public static final Pattern STRING_EXPANSION_PATTERN = Pattern.compile("\\$(\\{([^${}|/]+)(([\\|/][^{}|/]*)*)}|\\$)");

  static final String[] CONTROL_CHARACTER_REPLACEMENTS = { "${0x0}", "${0x1}", "${0x2}", "${0x3}", "${0x4}", "${0x5}", "${0x6}", "${0x7}", "${0x8}", "${0x9}",
      "${0xA}", "${0xB}", "${0xC}", "${0xD}", "${0xE}", "${0xF}", "${0x10}", "${0x11}", "${0x12}", "${0x13}", "${0x14}", "${0x15}", "${0x16}", "${0x17}",
      "${0x18}", "${0x19}", "${0x1A}", "${0x1B}", "${0x1C}", "${0x1D}", "${0x1E}", "${0x1F}" };

  protected static final Map<String, String> CONTROL_CHARACTER_VALUES = new HashMap<String, String>();

  static
  {
    for (int i = 0, length = CONTROL_CHARACTER_REPLACEMENTS.length; i < length; ++i)
    {
      String controlCharacterReplacement = CONTROL_CHARACTER_REPLACEMENTS[i];
      CONTROL_CHARACTER_VALUES.put(controlCharacterReplacement.substring(2, controlCharacterReplacement.length() - 1), Character.toString((char)i));
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

  protected String getFileSeparator()
  {
    return File.separator;
  }

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
          String extensions = matcher.group(3);
          if (extensions != null)
          {
            ExtensionParser parser = new ExtensionParser(extensions);
            Extension extension;

            while ((extension = parser.parseNext()) != null)
            {
              if (extension.filter)
              {
                if (extension.name.length() != 0)
                {
                  value = filter(value, extension.name);
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
              else
              {
                if (!value.endsWith("/") && !value.endsWith("\\"))
                {
                  value += getFileSeparator();
                }

                value += extension.name;
              }
            }
          }

          if (!unresolved)
          {
            result.append(value);
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

  /**
   * @author Eike Stepper
   */
  private static final class Extension
  {
    public final boolean filter;

    public final String name;

    public Extension(boolean filter, String name)
    {
      this.filter = filter;
      this.name = name;
    }
  }

  private static final class ExtensionParser
  {
    private final String input;

    private final int length;

    private int next;

    public ExtensionParser(String input)
    {
      this.input = input;
      length = input.length();
    }

    public Extension parseNext()
    {
      if (next < input.length())
      {
        boolean filter = input.charAt(next) == '|';
        StringBuilder name = new StringBuilder();

        while (++next < length)
        {
          char c = input.charAt(next);
          if (c == '/' || c == '|')
          {
            return new Extension(filter, name.toString());
          }
          else
          {
            name.append(c);
          }
        }

        return new Extension(filter, name.toString());
      }

      return null;
    }
  }
}
