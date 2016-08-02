/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.util.internal.pde;

import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;
import org.eclipse.equinox.frameworkadmin.BundleInfo;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.TargetBundle;

import org.osgi.framework.Constants;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Eike Stepper
 */
public class TargetPlatformClasspathFile implements IDynamicVariableResolver
{
  public String resolveValue(IDynamicVariable variable, String argument) throws CoreException
  {
    try
    {
      File tempFile = File.createTempFile("pde-", ".properties");

      ITargetDefinition targetDefinition = TargetPlatformUtil.getActiveTargetDefinition();
      if (!targetDefinition.isResolved())
      {
        targetDefinition.resolve(new NullProgressMonitor());
      }

      StringBuilder builder = new StringBuilder();

      TargetBundle[] bundles = targetDefinition.getAllBundles();
      if (bundles != null)
      {
        for (TargetBundle bundle : bundles)
        {
          BundleInfo info = bundle.getBundleInfo();
          if (info != null)
          {
            if (!info.getSymbolicName().endsWith(".source"))
            {
              URI location = info.getLocation();
              if (location != null)
              {
                String scheme = location.getScheme();
                if ("file".equals(scheme))
                {
                  appendBundleClasspath(builder, new File(location.getPath()));
                }
              }
            }
          }
        }
      }

      IOUtil.writeLines(tempFile, "UTF-8", Collections.singletonList(builder.toString()));
      return tempFile.getAbsolutePath();
    }
    catch (Exception ex)
    {
      UtilPDEPlugin.INSTANCE.coreException(ex);
    }

    return null;
  }

  public static void appendBundleClasspath(StringBuilder builder, File file) throws CoreException
  {
    if (file != null)
    {
      if (file.isDirectory())
      {
        appendFolder(builder, file);
      }
      else
      {
        appendFile(builder, file);
      }
    }
  }

  @SuppressWarnings("restriction")
  private static void appendFolder(StringBuilder builder, File bundleFolder) throws CoreException
  {
    Map<String, String> manifest = org.eclipse.pde.internal.core.util.ManifestUtils.loadManifest(bundleFolder);
    String bundleClasspath = manifest.get(Constants.BUNDLE_CLASSPATH);
    if (bundleClasspath != null)
    {
      StringTokenizer tokenizer = new StringTokenizer(bundleClasspath, ",");
      while (tokenizer.hasMoreTokens())
      {
        String token = tokenizer.nextToken();
        if (".".equals(token))
        {
          appendFile(builder, bundleFolder);
        }
        else
        {
          appendFile(builder, new File(bundleFolder, token));
        }
      }
    }
  }

  private static void appendFile(StringBuilder builder, File file)
  {
    if (builder.length() != 0)
    {
      builder.append(File.pathSeparatorChar);
    }

    builder.append(file.getAbsolutePath());
  }
}
