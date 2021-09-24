/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.util;

import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.setup.EAnnotationConstants;
import org.eclipse.oomph.setup.SetupTaskContext;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * @author Eike Stepper
 */
public final class SetupUtil
{
  private static final String DEFAULT_INSTALLER_UPDATE_URL = "https://download.eclipse.org/oomph/products/repository"; //$NON-NLS-1$

  private static final String RESOLVING_TARGET_PLATFORM_DEFINITIONS = "oomph.setup.resolving.target.platform.definitions"; //$NON-NLS-1$

  private static final Map<EClass, Set<Trigger>> TRIGGERS = Collections.synchronizedMap(new HashMap<EClass, Set<Trigger>>());

  private static final StringExpander UNESCAPE_STRING_EXPANDER = new StringExpander()
  {
    @Override
    protected String resolve(String key)
    {
      return CONTROL_CHARACTER_VALUES.containsKey(key) ? CONTROL_CHARACTER_VALUES.get(key) : key;
    }

    @Override
    protected boolean isUnexpanded(String key)
    {
      return !CONTROL_CHARACTER_VALUES.containsKey(key);
    }

    @Override
    protected String filter(String value, String filterName)
    {
      return value;
    }
  };

  public static final String INSTALLER_UPDATE_URL = PropertiesUtil.getProperty(SetupProperties.PROP_INSTALLER_UPDATE_URL, DEFAULT_INSTALLER_UPDATE_URL)
      .replace('\\', '/');

  public static final boolean INSTALLER_APPLICATION = "org.eclipse.oomph.setup.installer.application".equals(PropertiesUtil.getApplicationID()); //$NON-NLS-1$

  public static final boolean SETUP_ARCHIVER_APPLICATION = "org.eclipse.oomph.setup.core.SetupArchiver".equals(PropertiesUtil.getApplicationID()); //$NON-NLS-1$

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

    StringBuffer result = new StringBuffer();
    while (matcher.find())
    {
      String group1 = matcher.group(1);
      if ("$".equals(group1)) //$NON-NLS-1$
      {
        matcher.appendReplacement(result, "\\$\\$\\$\\$"); //$NON-NLS-1$
      }
      else
      {
        matcher.appendReplacement(result, "\\$$0"); //$NON-NLS-1$
      }
    }

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

  public static String unescape(String string)
  {
    return UNESCAPE_STRING_EXPANDER.expandString(string);
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

  public static Set<Trigger> getTriggers(EClass eClass)
  {
    Set<Trigger> result = TRIGGERS.get(eClass);
    if (result == null)
    {
      String triggers = EcoreUtil.getAnnotation(eClass, EAnnotationConstants.ANNOTATION_VALID_TRIGGERS, EAnnotationConstants.KEY_TRIGGERS);
      if (triggers != null)
      {
        String[] triggerValueLiterals = triggers.split("\\s"); //$NON-NLS-1$
        Trigger[] triggerValues = new Trigger[triggerValueLiterals.length];
        for (int i = 0; i < triggerValueLiterals.length; ++i)
        {
          triggerValues[i] = Trigger.get(triggerValueLiterals[i]);
        }

        result = Trigger.toSet(triggerValues);
      }
      else
      {
        result = Trigger.ALL_TRIGGERS;
      }
      TRIGGERS.put(eClass, result);
    }

    return result;
  }

}
