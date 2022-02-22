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

  @Override
  public ProfileContainer getContainer()
  {
    return container;
  }

  @Override
  public String getProfileID()
  {
    return profileID;
  }

  public String getProfileType()
  {
    return profileType;
  }

  @Override
  public boolean is(String property)
  {
    return "true".equals(get(property)); //$NON-NLS-1$
  }

  @Override
  public File getFile(String property)
  {
    String value = get(property);
    if (value != null)
    {
      return new File(value);
    }

    return null;
  }

  @Override
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

  @Override
  public ProfileCreator set(String property, boolean value)
  {
    return set(property, value ? "true" : null); //$NON-NLS-1$
  }

  @Override
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

  @Override
  public File getReferencer()
  {
    return getFile(Profile.PROP_PROFILE_REFERENCER);
  }

  @Override
  public ProfileCreator setReferencer(File value)
  {
    return setFile(Profile.PROP_PROFILE_REFERENCER, value);
  }

  @Override
  public File getConfigurationFolder()
  {
    return getFile(Profile.PROP_CONFIGURATION_FOLDER);
  }

  @Override
  public ProfileCreator setConfigurationFolder(File value)
  {
    return setFile(Profile.PROP_CONFIGURATION_FOLDER, value);
  }

  @Override
  public File getCacheFolder()
  {
    return getFile(Profile.PROP_CACHE);
  }

  @Override
  public ProfileCreator setCacheFolder(File value)
  {
    return setFile(Profile.PROP_CACHE, value);
  }

  @Override
  public File getInstallFolder()
  {
    return getFile(Profile.PROP_INSTALL_FOLDER);
  }

  @Override
  public ProfileCreator setInstallFolder(File value)
  {
    return setFile(Profile.PROP_INSTALL_FOLDER, value);
  }

  @Override
  public boolean isInstallFeatures()
  {
    return is(Profile.PROP_INSTALL_FEATURES);
  }

  @Override
  public ProfileCreator setInstallFeatures(boolean value)
  {
    return set(Profile.PROP_INSTALL_FEATURES, value);
  }

  @Override
  public boolean isRoaming()
  {
    return is(Profile.PROP_ROAMING);
  }

  @Override
  public ProfileCreator setRoaming(boolean value)
  {
    return set(Profile.PROP_ROAMING, value);
  }

  @Override
  public String getName()
  {
    return get(Profile.PROP_NAME);
  }

  @Override
  public ProfileCreator setName(String value)
  {
    return set(Profile.PROP_NAME, value);
  }

  @Override
  public String getDescription()
  {
    return get(Profile.PROP_DESCRIPTION);
  }

  @Override
  public ProfileCreator setDescription(String value)
  {
    return set(Profile.PROP_DESCRIPTION, value);
  }

  @Override
  public String getEnvironments()
  {
    return get(Profile.PROP_ENVIRONMENTS);
  }

  @Override
  public ProfileCreator setEnvironments(String value)
  {
    return set(Profile.PROP_ENVIRONMENTS, value);
  }

  @Override
  public Map<String, Set<String>> getEnvironmentsMap()
  {
    Map<String, Set<String>> map = new HashMap<>();

    String value = getEnvironments();
    if (value != null)
    {
      StringTokenizer tokenizer = new StringTokenizer(value, ","); //$NON-NLS-1$
      while (tokenizer.hasMoreTokens())
      {
        String token = tokenizer.nextToken();
        int pos = token.indexOf('=');
        String k = token.substring(0, pos).trim();
        String v = token.substring(pos + 1).trim();

        Set<String> values = map.get(k);
        if (values == null)
        {
          values = new HashSet<>();
          map.put(k, values);
        }

        values.add(v);
      }
    }

    return map;
  }

  @Override
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

  @Override
  public ProfileCreator addEnvironmentValue(String key, String value)
  {
    Map<String, Set<String>> map = getEnvironmentsMap();
    Set<String> values = map.get(key);
    if (values == null)
    {
      values = new HashSet<>();
      map.put(key, values);
    }

    if (values.add(value))
    {
      setEnvironmentsMap(map);
    }

    return this;
  }

  @Override
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

  @Override
  public ProfileCreator addOS(String value)
  {
    return addEnvironmentValue("osgi.os", value); //$NON-NLS-1$
  }

  @Override
  public ProfileCreator removeOS(String value)
  {
    return removeEnvironmentValue("osgi.os", value); //$NON-NLS-1$
  }

  @Override
  public ProfileCreator addWS(String value)
  {
    return addEnvironmentValue("osgi.ws", value); //$NON-NLS-1$
  }

  @Override
  public ProfileCreator removeWS(String value)
  {
    return removeEnvironmentValue("osgi.ws", value); //$NON-NLS-1$
  }

  @Override
  public ProfileCreator addArch(String value)
  {
    return addEnvironmentValue("osgi.arch", value); //$NON-NLS-1$
  }

  @Override
  public ProfileCreator removeArch(String value)
  {
    return removeEnvironmentValue("osgi.arch", value); //$NON-NLS-1$
  }

  @Override
  public String getLanguages()
  {
    return get(Profile.PROP_NL);
  }

  @Override
  public ProfileCreator setLanguages(String value)
  {
    return set(Profile.PROP_NL, value);
  }

  @Override
  public Set<String> getLanguageSet()
  {
    Set<String> set = new HashSet<>();

    String value = getLanguages();
    if (value != null)
    {
      StringTokenizer tokenizer = new StringTokenizer(value, ","); //$NON-NLS-1$
      while (tokenizer.hasMoreTokens())
      {
        String token = tokenizer.nextToken();
        set.add(token);
      }
    }

    return set;
  }

  @Override
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

  @Override
  public ProfileCreator addLanguage(String value)
  {
    Set<String> set = getLanguageSet();
    set.add(value);
    return setLanguageSet(set);
  }

  @Override
  public ProfileCreator removeLanguage(String value)
  {
    Set<String> set = getLanguageSet();
    set.remove(value);
    return setLanguageSet(set);
  }

  @Override
  public Profile create()
  {
    set(Profile.PROP_PROFILE_TYPE, profileType);
    return doCreateProfile();
  }

  protected abstract Profile doCreateProfile();
}
