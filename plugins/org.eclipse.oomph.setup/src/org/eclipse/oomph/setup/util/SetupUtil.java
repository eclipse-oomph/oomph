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

import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.util.PropertiesUtil;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * @author Eike Stepper
 */
public final class SetupUtil
{
  private static final String DEFAULT_INSTALLER_UPDATE_URL = "http://download.eclipse.org/oomph/products/repository";

  private static final String RESOLVING_TARGET_PLATFORM_DEFINITIONS = "oomph.setup.resolving.target.platform.definitions";

  public static final String INSTALLER_UPDATE_URL = PropertiesUtil.getProperty(SetupProperties.PROP_INSTALLER_UPDATE_URL, DEFAULT_INSTALLER_UPDATE_URL)
      .replace('\\', '/');

  public static final String INSTALLER_PRODUCT_ID = "org.eclipse.oomph.setup.installer.product";

  public static final boolean INSTALLER_PRODUCT = PropertiesUtil.getProperty("eclipse.product").equals(INSTALLER_PRODUCT_ID);

  public static final boolean SETUP_ARCHIVER_APPLICATION = "org.eclipse.oomph.setup.core.SetupArchiver"
      .equals(PropertiesUtil.getProperty("eclipse.application"));

  private SetupUtil()
  {
  }

  public static String escape(String string)
  {
    if (string == null)
    {
      return null;
    }

    Matcher matcher = StringExpander.STRING_EXPANSION_PATTERN.matcher(string);
    if (!matcher.find())
    {
      return string;
    }

    StringBuffer result = new StringBuffer();
    do
    {
      String group1 = matcher.group(1);
      if ("$".equals(group1))
      {
        matcher.appendReplacement(result, "\\$\\$\\$\\$");
      }
      else
      {
        matcher.appendReplacement(result, "\\$$0");
      }
    } while (matcher.find());

    matcher.appendTail(result);

    for (int i = 0, length = result.length(); i < length; ++i)
    {
      char c = result.charAt(i);
      if (c < StringExpander.CONTROL_CHARACTER_REPLACEMENTS.length && c != '\n' && c != '\r' && c != '\t')
      {
        String replacement = StringExpander.CONTROL_CHARACTER_REPLACEMENTS[c];
        result.replace(i, i + 1, replacement);
        length += replacement.length() - 1;
      }
    }

    return result.toString();
  }

  public static Set<String> getResolvingTargetDefinitions(SetupTaskContext context)
  {
    @SuppressWarnings("unchecked")
    Set<String> targetDefinitions = (Set<String>)context.get(RESOLVING_TARGET_PLATFORM_DEFINITIONS);
    if (targetDefinitions == null)
    {
      targetDefinitions = new LinkedHashSet<String>();
      context.put(RESOLVING_TARGET_PLATFORM_DEFINITIONS, targetDefinitions);
    }

    return targetDefinitions;
  }
}
