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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileContainer;
import org.eclipse.oomph.p2.core.ProfileCreator;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * @author Eike Stepper
 */
public abstract class ProfileCreatorImpl extends HashMap<String, String> implements ProfileCreator
{
  private static final long serialVersionUID = 1L;

  private final ProfileContainer container;

  private final String profileID;

  private final String profileType;

  public ProfileCreatorImpl(ProfileContainer container, String profileID, String profileType)
  {
    this.container = container;
    this.profileID = profileID;
    this.profileType = profileType;
  }

  public ProfileContainer getContainer()
  {
    return container;
  }

  public String getProfileID()
  {
    return profileID;
  }

  public String getProfileType()
  {
    return profileType;
  }

  public boolean is(String property)
  {
    return "true".equals(get(property));
  }

  public File getFile(String property)
  {
    String value = get(property);
    if (value != null)
    {
      return new File(value);
    }

    return null;
  }

  public ProfileCreator setFile(String property, File value)
  {
    if (value != null)
    {
      put(property, value.getAbsolutePath());
    }
    else
    {
      remove(property);
    }

    return this;
  }

  public ProfileCreator set(String property, boolean value)
  {
    return set(property, value ? "true" : null);
  }

  public ProfileCreator set(String property, String value)
  {
    if (value != null)
    {
      put(property, value);
    }
    else
    {
      remove(property);
    }

    return this;
  }

  public File getReferencer()
  {
    return getFile(Profile.PROP_PROFILE_REFERENCER);
  }

  public ProfileCreator setReferencer(File value)
  {
    return setFile(Profile.PROP_PROFILE_REFERENCER, value);
  }

  public File getConfigurationFolder()
  {
    return getFile(Profile.PROP_CONFIGURATION_FOLDER);
  }

  public ProfileCreator setConfigurationFolder(File value)
  {
    return setFile(Profile.PROP_CONFIGURATION_FOLDER, value);
  }

  public File getCacheFolder()
  {
    return getFile(Profile.PROP_CACHE);
  }

  public ProfileCreator setCacheFolder(File value)
  {
    return setFile(Profile.PROP_CACHE, value);
  }

  public File getInstallFolder()
  {
    return getFile(Profile.PROP_INSTALL_FOLDER);
  }

  public ProfileCreator setInstallFolder(File value)
  {
    return setFile(Profile.PROP_INSTALL_FOLDER, value);
  }

  public boolean isInstallFeatures()
  {
    return is(Profile.PROP_INSTALL_FEATURES);
  }

  public ProfileCreator setInstallFeatures(boolean value)
  {
    return set(Profile.PROP_INSTALL_FEATURES, value);
  }

  public boolean isRoaming()
  {
    return is(Profile.PROP_ROAMING);
  }

  public ProfileCreator setRoaming(boolean value)
  {
    return set(Profile.PROP_ROAMING, value);
  }

  public String getName()
  {
    return get(Profile.PROP_NAME);
  }

  public ProfileCreator setName(String value)
  {
    return set(Profile.PROP_NAME, value);
  }

  public String getDescription()
  {
    return get(Profile.PROP_DESCRIPTION);
  }

  public ProfileCreator setDescription(String value)
  {
    return set(Profile.PROP_DESCRIPTION, value);
  }

  public String getEnvironments()
  {
    return get(Profile.PROP_ENVIRONMENTS);
  }

  public ProfileCreator setEnvironments(String value)
  {
    return set(Profile.PROP_ENVIRONMENTS, value);
  }

  public Map<String, Set<String>> getEnvironmentsMap()
  {
    Map<String, Set<String>> map = new HashMap<String, Set<String>>();

    String value = getEnvironments();
    if (value != null)
    {
      StringTokenizer tokenizer = new StringTokenizer(value, ",");
      while (tokenizer.hasMoreTokens())
      {
        String token = tokenizer.nextToken();
        int pos = token.indexOf('=');
        String k = token.substring(0, pos).trim();
        String v = token.substring(pos + 1).trim();

        Set<String> values = map.get(k);
        if (values == null)
        {
          values = new HashSet<String>();
          map.put(k, values);
        }

        values.add(v);
      }
    }

    return map;
  }

  public ProfileCreator setEnvironmentsMap(Map<String, Set<String>> map)
  {
    StringBuilder builder = new StringBuilder();
    if (map != null)
    {
      for (Map.Entry<String, Set<String>> entry : map.entrySet())
      {
        String key = entry.getKey();
        // TODO Should the keys be validated?

        Set<String> values = entry.getValue();
        if (values != null)
        {
          for (String value : values)
          {
            if (builder.length() != 0)
            {
              builder.append(',');
            }

            builder.append(key);
            builder.append('=');
            builder.append(value);
          }
        }
      }
    }

    if (builder.length() != 0)
    {
      setEnvironments(builder.toString());
    }
    else
    {
      setEnvironments(null);
    }

    return this;
  }

  public ProfileCreator addEnvironmentValue(String key, String value)
  {
    Map<String, Set<String>> map = getEnvironmentsMap();
    Set<String> values = map.get(key);
    if (values == null)
    {
      values = new HashSet<String>();
      map.put(key, values);
    }

    if (values.add(value))
    {
      setEnvironmentsMap(map);
    }

    return this;
  }

  public ProfileCreator removeEnvironmentValue(String key, String value)
  {
    Map<String, Set<String>> map = getEnvironmentsMap();
    Set<String> values = map.get(key);
    if (values != null)
    {
      if (values.remove(value))
      {
        setEnvironmentsMap(map);
      }
    }

    return this;
  }

  public ProfileCreator addOS(String value)
  {
    return addEnvironmentValue("osgi.os", value);
  }

  public ProfileCreator removeOS(String value)
  {
    return removeEnvironmentValue("osgi.os", value);
  }

  public ProfileCreator addWS(String value)
  {
    return addEnvironmentValue("osgi.ws", value);
  }

  public ProfileCreator removeWS(String value)
  {
    return removeEnvironmentValue("osgi.ws", value);
  }

  public ProfileCreator addArch(String value)
  {
    return addEnvironmentValue("osgi.arch", value);
  }

  public ProfileCreator removeArch(String value)
  {
    return removeEnvironmentValue("osgi.arch", value);
  }

  public String getLanguages()
  {
    return get(Profile.PROP_NL);
  }

  public ProfileCreator setLanguages(String value)
  {
    return set(Profile.PROP_NL, value);
  }

  public Set<String> getLanguageSet()
  {
    Set<String> set = new HashSet<String>();

    String value = getLanguages();
    if (value != null)
    {
      StringTokenizer tokenizer = new StringTokenizer(value, ",");
      while (tokenizer.hasMoreTokens())
      {
        String token = tokenizer.nextToken();
        set.add(token);
      }
    }

    return set;
  }

  public ProfileCreator setLanguageSet(Set<String> set)
  {
    StringBuilder builder = new StringBuilder();
    if (set != null)
    {
      for (String value : set)
      {
        if (builder.length() != 0)
        {
          builder.append(',');
        }

        builder.append(value);
      }
    }

    if (builder.length() != 0)
    {
      setLanguages(builder.toString());
    }
    else
    {
      setLanguages(null);
    }

    return this;
  }

  public ProfileCreator addLanguage(String value)
  {
    Set<String> set = getLanguageSet();
    set.add(value);
    return setLanguageSet(set);
  }

  public ProfileCreator removeLanguage(String value)
  {
    Set<String> set = getLanguageSet();
    set.remove(value);
    return setLanguageSet(set);
  }

  public Profile create()
  {
    set(Profile.PROP_PROFILE_TYPE, profileType);
    return doCreateProfile();
  }

  protected abstract Profile doCreateProfile();
}
