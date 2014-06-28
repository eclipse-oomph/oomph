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
package org.eclipse.oomph.p2.internal.core;

import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.equinox.internal.p2.engine.Profile;
import org.eclipse.equinox.internal.p2.engine.SimpleProfileRegistry;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.osgi.util.NLS;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class LazyProfileRegistry extends SimpleProfileRegistry
{
  private static final String PROFILE_EXT = ".profile"; //$NON-NLS-1$

  private static final String PROFILE_GZ_EXT = ".profile.gz"; //$NON-NLS-1$

  // private final File store;
  //
  // private final String self;
  //
  // private boolean updateSelfProfile;
  //
  // private final Map<String, ProfileLock> profileLocks;

  private Map<String, org.eclipse.equinox.internal.p2.engine.Profile> profileMap;

  // @SuppressWarnings("unchecked")
  public LazyProfileRegistry(IProvisioningAgent provisioningAgent, File store)
  {
    super(provisioningAgent, store);
    this.store = store;

    // Field selfField = ReflectUtil.getField(SimpleProfileRegistry.class, "self");
    // self = (String)ReflectUtil.getValue(selfField, this);
    //
    // Field updateSelfProfileField = ReflectUtil.getField(SimpleProfileRegistry.class, "updateSelfProfile");
    // updateSelfProfile = (Boolean)ReflectUtil.getValue(updateSelfProfileField, this);
    //
    // Field profileLocksField = ReflectUtil.getField(SimpleProfileRegistry.class, "profileLocks");
    // profileLocks = (Map<String, ProfileLock>)ReflectUtil.getValue(profileLocksField, this);
  }

  public IProvisioningAgent getProvisioningAgent()
  {
    return agent;
  }

  @Override
  public synchronized void resetProfiles()
  {
    profileMap = null;
  }

  @Override
  public synchronized void removeProfile(String id, long timestamp) throws ProvisionException
  {
    if (SELF.equals(id))
    {
      id = self;
    }

    org.eclipse.equinox.internal.p2.engine.Profile p = profileMap.get(id);
    if (p instanceof LazyProfile)
    {
      LazyProfile lazyProfile = (LazyProfile)p;
      if (lazyProfile.getDelegate(false) != null)
      {
        IProfile profile = getProfile(id);
        if (profile != null && profile.getTimestamp() == timestamp)
        {
          throw new ProvisionException("Cannot remove the current profile timestamp");
        }
      }
    }

    super.removeProfile(id, timestamp);
  }

  @Override
  public synchronized boolean containsProfile(String id)
  {
    if (SELF.equals(id))
    {
      id = self;
    }

    // Null check done after self check, because self can be null
    if (id == null)
    {
      return false;
    }

    // Check profiles to avoid restoring the profile registry
    org.eclipse.equinox.internal.p2.engine.Profile p = profileMap.get(id);
    if (p instanceof LazyProfile)
    {
      LazyProfile lazyProfile = (LazyProfile)p;
      if (lazyProfile.getDelegate(false) != null)
      {
        if (getProfile(id) != null)
        {
          return true;
        }
      }
    }

    return super.containsProfile(id);
  }

  @Override
  protected synchronized final Map<String, org.eclipse.equinox.internal.p2.engine.Profile> getProfileMap()
  {
    if (profileMap == null)
    {
      if (store == null || !store.isDirectory())
      {
        throw new IllegalStateException(NLS.bind("Registry Directory not available: {0}.", store));
      }

      profileMap = new HashMap<String, org.eclipse.equinox.internal.p2.engine.Profile>();

      File[] profileDirectories = store.listFiles(new FileFilter()
      {
        public boolean accept(File pathname)
        {
          return pathname.getName().endsWith(PROFILE_EXT) && pathname.isDirectory();
        }
      });

      for (int i = 0; i < profileDirectories.length; i++)
      {
        String directoryName = profileDirectories[i].getName();
        String profileId = unescape(directoryName.substring(0, directoryName.lastIndexOf(PROFILE_EXT)));

        LazyProfile profile = new LazyProfile(this, profileId, profileDirectories[i]);
        profileMap.put(profileId, profile);
      }
    }

    if (updateSelfProfile)
    {
      Method updateSelfProfileMethod = ReflectUtil.getMethod(SimpleProfileRegistry.class, "updateSelfProfile", Map.class);
      ReflectUtil.invokeMethod(updateSelfProfileMethod, this, profileMap);
      updateSelfProfile = false;
    }

    return profileMap;
  }

  public org.eclipse.equinox.internal.p2.engine.Profile loadProfile(String profileId, File profileDirectory)
  {
    if (store == null || !store.isDirectory())
    {
      throw new IllegalStateException(NLS.bind("Registry Directory not available: {0}.", store));
    }

    try
    {
      Map<String, Profile> profileMap = parseProfile(profileDirectory, null);

      // Class<?> parserClass = CommonPlugin.loadClass(EngineActivator.ID, "org.eclipse.equinox.internal.p2.engine.SimpleProfileRegistry$Parser");
      // Constructor<?> constructor = ReflectUtil.getConstructor(parserClass, SimpleProfileRegistry.class, BundleContext.class, String.class);
      // Method parseMethod = ReflectUtil.getMethod(parserClass, "parse", File.class);
      // Method getProfileMapMethod = ReflectUtil.getMethod(parserClass, "getProfileMap");
      // Method addProfilePlaceHolderMethod = ReflectUtil.getMethod(parserClass, "addProfilePlaceHolder", String.class);
      //
      // Object parser = ReflectUtil.newInstance(constructor, this, EngineActivator.getContext(), EngineActivator.ID);
      //
      // ProfileLock lock = profileLocks.get(profileId);
      // if (lock == null)
      // {
      // lock = new ProfileLock(this, profileDirectory);
      // profileLocks.put(profileId, lock);
      // }
      //
      // boolean locked = false;
      // if (lock.processHoldsLock() || (locked = lock.lock()))
      // {
      // try
      // {
      // File profileFile = findLatestProfileFile(profileDirectory);
      // if (profileFile != null)
      // {
      // ReflectUtil.invokeMethod(parseMethod, parser, profileFile);
      // }
      // }
      // finally
      // {
      // if (locked)
      // {
      // lock.unlock();
      // }
      // }
      // }
      // else
      // {
      // // could not lock the profile, so add a place holder
      // ReflectUtil.invokeMethod(addProfilePlaceHolderMethod, parser, profileId);
      // }
      //
      // @SuppressWarnings("unchecked")
      // Map<String, org.eclipse.equinox.internal.p2.engine.Profile> profileMap = //
      // (Map<String, org.eclipse.equinox.internal.p2.engine.Profile>)ReflectUtil.invokeMethod(getProfileMapMethod, parser);

      return profileMap.get(profileId);
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
  }

  // private File findLatestProfileFile(File profileDirectory)
  // {
  // File latest = null;
  // long latestTimestamp = 0;
  // File[] profileFiles = profileDirectory.listFiles(new FileFilter()
  // {
  // public boolean accept(File pathname)
  // {
  // return (pathname.getName().endsWith(PROFILE_GZ_EXT) || pathname.getName().endsWith(PROFILE_EXT)) && !pathname.isDirectory();
  // }
  // });
  // // protect against NPE
  // if (profileFiles == null)
  // {
  // return null;
  // }
  // for (int i = 0; i < profileFiles.length; i++)
  // {
  // File profileFile = profileFiles[i];
  // String fileName = profileFile.getName();
  // try
  // {
  // long timestamp = Long.parseLong(fileName.substring(0, fileName.indexOf(PROFILE_EXT)));
  // if (timestamp > latestTimestamp)
  // {
  // latestTimestamp = timestamp;
  // latest = profileFile;
  // }
  // }
  // catch (NumberFormatException e)
  // {
  // // ignore
  // }
  // }
  // return latest;
  // }
}
