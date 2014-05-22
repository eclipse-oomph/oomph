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
package org.eclipse.oomph.p2.core;

import java.io.File;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public interface ProfileCreator extends Map<String, String>
{
  public ProfileContainer getContainer();

  public String getProfileID();

  public boolean is(String property);

  public File getFile(String property);

  public ProfileCreator setFile(String property, File value);

  public ProfileCreator set(String property, boolean value);

  public ProfileCreator set(String property, String value);

  public File getConfigurationFolder();

  public ProfileCreator setConfigurationFolder(File value);

  public File getCacheFolder();

  public ProfileCreator setCacheFolder(File value);

  public File getInstallFolder();

  public ProfileCreator setInstallFolder(File value);

  public boolean isInstallFeatures();

  public ProfileCreator setInstallFeatures(boolean value);

  public boolean isRoaming();

  public ProfileCreator setRoaming(boolean value);

  public String getName();

  public ProfileCreator setName(String value);

  public String getDescription();

  public ProfileCreator setDescription(String value);

  public String getEnvironments();

  public ProfileCreator setEnvironments(String value);

  public Map<String, Set<String>> getEnvironmentsMap();

  public ProfileCreator setEnvironmentsMap(Map<String, Set<String>> map);

  public ProfileCreator addEnvironmentValue(String key, String value);

  public ProfileCreator removeEnvironmentValue(String key, String value);

  public ProfileCreator addOS(String value);

  public ProfileCreator removeOS(String value);

  public ProfileCreator addWS(String value);

  public ProfileCreator removeWS(String value);

  public ProfileCreator addArch(String value);

  public ProfileCreator removeArch(String value);

  public String getLanguages();

  public ProfileCreator setLanguages(String value);

  public Set<String> getLanguageSet();

  public ProfileCreator setLanguageSet(Set<String> set);

  public ProfileCreator addLanguage(String value);

  public ProfileCreator removeLanguage(String value);

  public Profile create();
}
