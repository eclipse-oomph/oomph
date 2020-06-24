/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.core.variables;

import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Eike Stepper
 */
public class TargletContainerClasspathFile implements IDynamicVariableResolver
{
  public String resolveValue(IDynamicVariable variable, String containerID) throws CoreException
  {
    try
    {
      File tempFile = File.createTempFile("tcc-", ".properties"); //$NON-NLS-1$ //$NON-NLS-2$

      String classpath = TargletContainerClasspath.getClasspath(containerID);
      IOUtil.writeLines(tempFile, "UTF-8", Collections.singletonList(classpath)); //$NON-NLS-1$

      return tempFile.getAbsolutePath();
    }
    catch (IOException ex)
    {
      TargletsCorePlugin.INSTANCE.coreException(ex);
    }

    return null;
  }
}
