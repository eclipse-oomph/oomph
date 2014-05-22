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
package org.eclipse.oomph.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Various static helper methods for dealing with strings.
 *
 * @author Eike Stepper
 */
public final class StringUtil
{
  public static final String EMPTY = ""; //$NON-NLS-1$

  public static final String NL = PropertiesUtil.getProperty("line.separator"); //$NON-NLS-1$

  private StringUtil()
  {
  }

  public static boolean isEmpty(String str)
  {
    return str == null || str.length() == 0;
  }

  public static String safe(String str)
  {
    return safe(str, EMPTY);
  }

  private static String safe(String str, String def)
  {
    if (str == null)
    {
      return def;
    }

    return str;
  }

  public static String cap(String str)
  {
    if (str == null || str.length() == 0)
    {
      return str;
    }

    char first = str.charAt(0);
    if (Character.isUpperCase(first))
    {
      return str;
    }

    if (str.length() == 1)
    {
      return str.toUpperCase();
    }

    StringBuilder builder = new StringBuilder(str);
    builder.setCharAt(0, Character.toUpperCase(first));
    return builder.toString();
  }

  public static String capAll(String str)
  {
    if (str == null || str.length() == 0)
    {
      return str;
    }

    boolean inWhiteSpace = true;
    StringBuilder builder = new StringBuilder(str);
    for (int i = 0; i < builder.length(); i++)
    {
      char c = builder.charAt(i);
      boolean isWhiteSpace = Character.isWhitespace(c);
      if (!isWhiteSpace && inWhiteSpace)
      {
        builder.setCharAt(i, Character.toUpperCase(c));
      }

      inWhiteSpace = isWhiteSpace;
    }

    return builder.toString();
  }

  public static String uncap(String str)
  {
    if (str == null || str.length() == 0)
    {
      return str;
    }

    char first = str.charAt(0);
    if (Character.isLowerCase(first))
    {
      return str;
    }

    if (str.length() == 1)
    {
      return str.toLowerCase();
    }

    StringBuilder builder = new StringBuilder(str);
    builder.setCharAt(0, Character.toLowerCase(first));
    return builder.toString();
  }

  public static String uncapAll(String str)
  {
    if (str == null || str.length() == 0)
    {
      return str;
    }

    boolean inWhiteSpace = true;
    StringBuilder builder = new StringBuilder(str);
    for (int i = 0; i < builder.length(); i++)
    {
      char c = builder.charAt(i);
      boolean isWhiteSpace = Character.isWhitespace(c);
      if (!isWhiteSpace && inWhiteSpace)
      {
        builder.setCharAt(i, Character.toLowerCase(c));
      }

      inWhiteSpace = isWhiteSpace;
    }

    return builder.toString();
  }

  public static String encodePath(String path)
  {
    return path.replace(':', '_').replace('/', '_').replace('\\', '_');
  }

  public static List<String> explode(String string, String separators)
  {
    return explode(string, separators, '\\');
  }

  public static List<String> explode(String string, String separators, char escapeCharacter)
  {
    List<String> tokens = new ArrayList<String>();

    StringBuilder builder = new StringBuilder();
    boolean separator = false;
    boolean escape = false;

    for (int i = 0; i < string.length(); i++)
    {
      separator = false;
      char c = string.charAt(i);
      if (!escape && c == escapeCharacter)
      {
        escape = true;
      }
      else
      {
        if (!escape && separators.indexOf(c) != -1)
        {
          tokens.add(builder.toString());
          builder.setLength(0);
          separator = true;
        }
        else
        {
          builder.append(c);
        }

        escape = false;
      }
    }

    if (separator || builder.length() != 0)
    {
      tokens.add(builder.toString());
    }

    return tokens;
  }

  public static String implode(List<String> tokens, char separator)
  {
    return implode(tokens, separator, '\\');
  }

  public static String implode(List<String> tokens, char separator, char escapeCharacter)
  {
    String escapeString = Character.toString(escapeCharacter);
    String escapeString2 = escapeString + escapeString;

    String separatorString = Character.toString(separator);
    String separatorString2 = escapeString + separatorString;

    StringBuilder builder = new StringBuilder();
    boolean firstTime = true;

    for (String token : tokens)
    {
      if (firstTime)
      {
        firstTime = false;
      }
      else
      {
        builder.append(separator);
      }

      if (token != null)
      {
        token = token.replace(escapeString, escapeString2);
        token = token.replace(separatorString, separatorString2);
        builder.append(token);
      }
    }

    return builder.toString();
  }
}
