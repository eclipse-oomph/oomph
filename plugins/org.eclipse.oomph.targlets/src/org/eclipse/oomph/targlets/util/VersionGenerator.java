/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.util;

import org.eclipse.equinox.p2.metadata.Version;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Eike Stepper
 */
public final class VersionGenerator
{
  private static final String QUALIFIER = "qualifier";

  private static final String QUALIFIER_SUFFIX = "." + QUALIFIER;

  private static final int QUALIFIER_LENGTH = QUALIFIER.length();

  public static String generateQualifierReplacement()
  {
    return new SimpleDateFormat("'v'yyyyMMdd-HHmmss").format(new Date());
  }

  public static String replaceQualifier(String version, String qualifierReplacement)
  {
    if (version != null && qualifierReplacement != null && version.endsWith(QUALIFIER_SUFFIX))
    {
      int length = version.length();
      StringBuilder result = new StringBuilder(length - QUALIFIER_LENGTH + qualifierReplacement.length());
      result.append(version, 0, length - QUALIFIER_LENGTH);
      result.append(qualifierReplacement);
      version = result.toString();
    }

    return version;
  }

  public static Version replaceQualifier(Version version, String qualifierReplacement)
  {
    if (version != null && version.isOSGiCompatible())
    {
      version = Version.create(replaceQualifier(version.toString(), qualifierReplacement));
    }

    return version;
  }
}
