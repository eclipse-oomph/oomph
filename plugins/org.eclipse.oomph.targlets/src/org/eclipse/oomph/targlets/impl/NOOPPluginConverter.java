/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.impl;

import org.eclipse.osgi.service.pluginconversion.PluginConversionException;
import org.eclipse.osgi.service.pluginconversion.PluginConverter;

import java.io.File;
import java.util.Dictionary;

/**
 * This is a NOOP implementation of {@link PluginConverter} that's registered as a service with very low ranking.
 * <p>
 * The purpose is to prevent errors from being logged in BundlesAction.convertPluginManifest() if no PluginConverter service is registered.
 * That method is indirectly called from {@link PluginGeneratorImpl#generateIUs(org.eclipse.core.resources.IProject, String, java.util.Map, org.eclipse.emf.common.util.EList) PluginGeneratorImpl#generateIUs()}
 * if a project contains a JAR manifest that is <b>not</b> an OSGi bundle manifest.
 *
 * @author Eike Stepper
 */
public final class NOOPPluginConverter implements PluginConverter
{
  public File convertManifest(File pluginBaseLocation, File bundleManifestLocation, boolean compatibilityManifest, String target, boolean analyseJars,
      Dictionary<String, String> devProperties) throws PluginConversionException
  {
    return null;
  }

  public Dictionary<String, String> convertManifest(File pluginBaseLocation, boolean compatibility, String target, boolean analyseJars,
      Dictionary<String, String> devProperties) throws PluginConversionException
  {
    return null;
  }

  public void writeManifest(File generationLocation, Dictionary<String, String> manifestToWrite, boolean compatibilityManifest) throws PluginConversionException
  {
    // Do nothing.
  }
}
