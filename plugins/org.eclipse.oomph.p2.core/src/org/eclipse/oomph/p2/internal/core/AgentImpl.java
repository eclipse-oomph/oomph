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

import org.eclipse.oomph.p2.P2Exception;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileCreator;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.internal.p2.engine.CommitOperationEvent;
import org.eclipse.equinox.internal.p2.repository.Transport;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.EclipseTouchpoint;
import org.eclipse.equinox.internal.p2.touchpoint.eclipse.Util;
import org.eclipse.equinox.internal.p2.update.Configuration;
import org.eclipse.equinox.internal.p2.update.Site;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.IProvisioningEventBus;
import org.eclipse.equinox.internal.provisional.p2.core.eventbus.SynchronousProvisioningListener;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.core.spi.IAgentService;
import org.eclipse.equinox.p2.engine.IEngine;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IArtifactKey;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.planner.IPlanner;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class AgentImpl extends AgentManagerElementImpl implements Agent
{
  public static final String ENGINE_PATH = "org.eclipse.equinox.p2.engine";

  private static final Pattern ECLIPSE_INI_SECTION_PATTERN = Pattern.compile("^(-vmargs)([\n\r]+.*)\\z|^(-[^\\n\\r]*[\\n\\r]*)((?:^[^-][^\\n\\r]*)*[\\n\\r]*)",
      Pattern.MULTILINE | Pattern.DOTALL);

  private final AgentManagerImpl agentManager;

  private final File location;

  private final PersistentMap<BundlePool> bundlePoolMap;

  private final PersistentMap<Profile> profileMap;

  private IProvisioningAgent provisioningAgent;

  private IProfileRegistry profileRegistry;

  private IMetadataRepositoryManager metadataRepositoryManager;

  private IArtifactRepositoryManager artifactRepositoryManager;

  private IEngine engine;

  private IPlanner planner;

  private Transport originalTransport;

  private IMetadataRepositoryManager originalMetadataRepositoryManager;

  private IArtifactRepositoryManager originalArtifactRepositoryManager;

  public AgentImpl(AgentManagerImpl agentManager, File location)
  {
    this.agentManager = agentManager;
    this.location = location;

    bundlePoolMap = new PersistentMap<BundlePool>(new File(location, "pools.info"))
    {
      @Override
      protected BundlePool createElement(String key, String extraInfo)
      {
        return new BundlePoolImpl(AgentImpl.this, new File(key));
      }

      @Override
      protected BundlePool loadElement(String key, String extraInfo)
      {
        // TODO
        return super.loadElement(key, extraInfo);
      }

      @Override
      protected void initializeFirstTime()
      {
        IProfileRegistry profileRegistry = getProfileRegistry();
        for (IProfile delegate : profileRegistry.getProfiles())
        {
          String cache = delegate.getProperty(Profile.PROP_CACHE);
          if (cache != null)
          {
            addElement(cache, null);
          }
        }
      }
    };

    profileMap = new PersistentMap<Profile>(new File(location, "profiles.info"))
    {
      @Override
      protected Profile createElement(String profileID, String extraInfo)
      {
        if (extraInfo == null)
        {
          return new ProfileImpl(AgentImpl.this, null, profileID, null, null, null);
        }

        List<String> tokens = StringUtil.explode(extraInfo, "|");
        int size = tokens.size();

        String type = size > 0 ? tokens.get(0) : "";
        BundlePool bundlePool = size > 1 ? getBundlePool(tokens.get(1)) : null;
        File installFolder = size > 2 ? AgentImpl.getFile(tokens.get(2)) : null;
        File referencer = size > 3 ? AgentImpl.getFile(tokens.get(3)) : null;

        return new ProfileImpl(AgentImpl.this, bundlePool, profileID, type, installFolder, referencer);
      }

      @Override
      protected Profile loadElement(String key, String extraInfo)
      {
        // TODO
        return super.loadElement(key, extraInfo);
      }

      @Override
      protected void initializeFirstTime()
      {
        fillProfileMap(this);
      }
    };

    bundlePoolMap.load();
    profileMap.load();
  }

  @Override
  public String getElementType()
  {
    return "agent";
  }

  public void dispose()
  {
    if (provisioningAgent != null)
    {
      if (originalTransport != null)
      {
        provisioningAgent.registerService(Transport.SERVICE_NAME, originalTransport);
      }

      if (originalMetadataRepositoryManager != null)
      {
        if (metadataRepositoryManager instanceof IAgentService)
        {
          ((IAgentService)metadataRepositoryManager).stop();
        }

        provisioningAgent.registerService(IMetadataRepositoryManager.SERVICE_NAME, originalMetadataRepositoryManager);
      }

      if (originalArtifactRepositoryManager != null)
      {
        if (artifactRepositoryManager instanceof IAgentService)
        {
          ((IAgentService)artifactRepositoryManager).stop();
        }

        provisioningAgent.registerService(IArtifactRepositoryManager.SERVICE_NAME, originalArtifactRepositoryManager);
      }
    }
  }

  public AgentManager getAgentManager()
  {
    return agentManager;
  }

  public File getLocation()
  {
    return location;
  }

  public boolean isValid()
  {
    return isValid(location);
  }

  public boolean isCurrent()
  {
    return agentManager.getCurrentAgent() == this;
  }

  public boolean isUsed()
  {
    for (BundlePool bundlePool : getBundlePools())
    {
      if (bundlePool.isUsed())
      {
        return true;
      }
    }

    for (Profile profile : getProfiles())
    {
      if (profile.isUsed())
      {
        return true;
      }
    }

    return false;
  }

  @Override
  protected void doDelete()
  {
    agentManager.deleteAgent(this);
  }

  public PersistentMap<BundlePool> getBundlePoolMap()
  {
    return bundlePoolMap;
  }

  public Set<File> getBundlePoolLocations()
  {
    return AgentManagerImpl.getLocations(bundlePoolMap.getElementKeys());
  }

  public Collection<BundlePool> getBundlePools()
  {
    return bundlePoolMap.getElements();
  }

  public BundlePool getBundlePool(String path)
  {
    return bundlePoolMap.getElement(path);
  }

  public BundlePool getBundlePool(File location)
  {
    return bundlePoolMap.getElement(location.getAbsolutePath());
  }

  public BundlePool addBundlePool(File location)
  {
    return bundlePoolMap.addElement(location.getAbsolutePath(), null);
  }

  public void deleteBundlePool(BundlePool bundlePool)
  {
    bundlePoolMap.removeElement(bundlePool.getLocation().getAbsolutePath());

    // TODO Delete artifacts from disk
  }

  public void refreshBundlePools(IProgressMonitor monitor)
  {
    monitor.subTask("Refreshing " + getLocation() + " bundle pools");
    bundlePoolMap.refresh();
    monitor.done();
  }

  public synchronized IProfileRegistry getProfileRegistry()
  {
    getProvisioningAgent();
    return profileRegistry;
  }

  public PersistentMap<Profile> getProfileMap()
  {
    return profileMap;
  }

  public Set<String> getAllProfileIDs()
  {
    return profileMap.getElementKeys();
  }

  public Collection<Profile> getAllProfiles()
  {
    return profileMap.getElements();
  }

  public Set<String> getProfileIDs()
  {
    return getProfileIDs(null);
  }

  public Set<String> getProfileIDs(BundlePool bundlePool)
  {
    Set<String> ids = new HashSet<String>();
    for (Profile profile : getAllProfiles())
    {
      if (profile.getBundlePool() == bundlePool)
      {
        ids.add(profile.getProfileId());
      }
    }

    return ids;
  }

  public Collection<Profile> getProfiles()
  {
    return getProfiles(null);
  }

  public Collection<Profile> getProfiles(BundlePool bundlePool)
  {
    Collection<Profile> profiles = getAllProfiles();
    for (Iterator<Profile> it = profiles.iterator(); it.hasNext();)
    {
      Profile profile = it.next();
      if (profile.getBundlePool() != bundlePool)
      {
        it.remove();
      }
    }

    return profiles;
  }

  public Profile getCurrentProfile()
  {
    IProfile delegate = getProfileRegistry().getProfile(IProfileRegistry.SELF);
    if (delegate != null)
    {
      String profileID = delegate.getProfileId();
      return getProfile(profileID);
    }

    return null;
  }

  public Profile getProfile(String id)
  {
    Profile profile = profileMap.getElement(id);
    if (profile == null)
    {
      if (provisioningAgent == null)
      {
        // Trigger creation of the LazyProfileRegistry and, hence, refreshing of the profileMap
        getProvisioningAgent();

        // Then try again
        profile = profileMap.getElement(id);
      }
    }
    else
    {
      if (!profile.isValid())
      {
        profileMap.removeElement(id);
        getProfileRegistry().removeProfile(id);
        profile = null;
      }
    }

    return profile;
  }

  public ProfileCreator addProfile(String id, String type)
  {
    return new ProfileCreatorImpl(this, id, type)
    {
      private static final long serialVersionUID = 1L;

      @Override
      protected Profile doCreateProfile()
      {
        return AgentImpl.this.createProfile(this);
      }
    };
  }

  public Profile createProfile(ProfileCreator creator)
  {
    if (creator.getEnvironments() == null)
    {
      creator.addOS(Platform.getOS());
      creator.addWS(Platform.getWS());
      creator.addArch(Platform.getOSArch());
      creator.addLanguage(Platform.getNL());
    }

    try
    {
      String profileID = creator.getProfileID();

      IProfile delegate = getProfileRegistry().addProfile(profileID, creator);
      String extraInfo = getProfileExtraInfo(delegate);

      ProfileImpl profile = (ProfileImpl)profileMap.addElement(profileID, extraInfo);
      profile.setDelegate(delegate);
      return profile;
    }
    catch (ProvisionException ex)
    {
      throw new P2Exception(ex);
    }
  }

  public void deleteProfile(Profile profile)
  {
    String profileID = profile.getProfileId();
    profileMap.removeElement(profileID);
    getProfileRegistry().removeProfile(profileID);
  }

  public void refreshProfiles(IProgressMonitor monitor)
  {
    // monitor.subTask("Refreshing " + getLocation() + " profiles");
    getProvisioningAgent();

    if (profileRegistry instanceof LazyProfileRegistry)
    {
      LazyProfileRegistry lazyProfileRegistry = (LazyProfileRegistry)profileRegistry;
      lazyProfileRegistry.resetProfiles();
      lazyProfileRegistry.getProfileMap(monitor);
    }

    fillProfileMap(profileMap);
    profileMap.refresh();

    // monitor.done();
  }

  private void fillProfileMap(PersistentMap<Profile> profileMap)
  {
    IProfileRegistry profileRegistry = getProfileRegistry();
    for (IProfile delegate : profileRegistry.getProfiles())
    {
      String key = delegate.getProfileId();
      String extraInfo = AgentImpl.getProfileExtraInfo(delegate);
      profileMap.addElement(key, extraInfo);
    }
  }

  public void initializeProvisioningAgent(IProvisioningAgent provisioningAgent)
  {
    if (this.provisioningAgent == null)
    {
      profileRegistry = (IProfileRegistry)provisioningAgent.getService(IProfileRegistry.SERVICE_NAME);
      metadataRepositoryManager = (IMetadataRepositoryManager)provisioningAgent.getService(IMetadataRepositoryManager.SERVICE_NAME);
      artifactRepositoryManager = (IArtifactRepositoryManager)provisioningAgent.getService(IArtifactRepositoryManager.SERVICE_NAME);

      Transport transport = (Transport)provisioningAgent.getService(Transport.SERVICE_NAME);
      if (cacheTransport(transport))
      {
        originalTransport = transport;
        CachingTransport cachingTransport = new CachingTransport(transport, provisioningAgent);
        provisioningAgent.registerService(Transport.SERVICE_NAME, cachingTransport);

        originalMetadataRepositoryManager = metadataRepositoryManager;
        metadataRepositoryManager = new CachingRepositoryManager.Metadata(provisioningAgent, cachingTransport);
        provisioningAgent.registerService(IMetadataRepositoryManager.SERVICE_NAME, metadataRepositoryManager);

        originalArtifactRepositoryManager = artifactRepositoryManager;
        artifactRepositoryManager = new CachingRepositoryManager.Artifact(provisioningAgent, cachingTransport);
        provisioningAgent.registerService(IArtifactRepositoryManager.SERVICE_NAME, artifactRepositoryManager);
      }

      IProvisioningEventBus eventBus = (IProvisioningEventBus)provisioningAgent.getService(IProvisioningEventBus.SERVICE_NAME);
      if (eventBus != null)
      {
        eventBus.addListener(new SynchronousProvisioningListener()
        {
          public void notify(EventObject event)
          {
            if (event instanceof CommitOperationEvent)
            {
              CommitOperationEvent commitOperationEvent = (CommitOperationEvent)event;
              IProfile profile = commitOperationEvent.getProfile();
              if (Profile.TYPE_INSTALLATION.equals(profile.getProperty(Profile.PROP_PROFILE_TYPE)))
              {
                // If this is a commit of an Oomph-created installation profile, then adjust the installation details.
                adjustInstallation(profile);
              }
            }
          }
        });
      }

      this.provisioningAgent = provisioningAgent;

      Profile currentProfile = getCurrentProfile();
      if (currentProfile instanceof ProfileImpl)
      {
        File cachedInstallFolder = currentProfile.getInstallFolder();
        if (cachedInstallFolder != null)
        {
          String actualInstallFolderLocation = currentProfile.getProperty(IProfile.PROP_INSTALL_FOLDER);
          if (actualInstallFolderLocation != null)
          {
            File actualInstallFolder = new File(actualInstallFolderLocation);
            if (!actualInstallFolder.equals(cachedInstallFolder))
            {
              ((ProfileImpl)currentProfile).setInstallFolder(actualInstallFolder);

              BundlePool bundlePool = currentProfile.getBundlePool();
              if (bundlePool instanceof BundlePoolImpl)
              {
                File bundlePoolLocation = bundlePool.getLocation();
                if (bundlePoolLocation.equals(cachedInstallFolder))
                {
                  ((BundlePoolImpl)bundlePool).setLocation(actualInstallFolder);
                  bundlePoolMap.remap(cachedInstallFolder.toString(), actualInstallFolderLocation);
                }
              }

              String profileID = currentProfile.getProfileId();
              profileMap.save(profileID, profileID);
            }
          }
        }
      }
    }
  }

  public synchronized IProvisioningAgent getProvisioningAgent()
  {
    if (provisioningAgent == null)
    {
      File location = getLocation();
      initializeProvisioningAgent(getOrCreateProvisioningAgent(location));
    }

    return provisioningAgent;
  }

  public synchronized IMetadataRepositoryManager getMetadataRepositoryManager()
  {
    getProvisioningAgent();
    return metadataRepositoryManager;
  }

  public synchronized IArtifactRepositoryManager getArtifactRepositoryManager()
  {
    getProvisioningAgent();
    return artifactRepositoryManager;
  }

  public synchronized IEngine getEngine()
  {
    if (engine == null)
    {
      engine = (IEngine)getProvisioningAgent().getService(IEngine.SERVICE_NAME);
      if (engine == null)
      {
        throw new IllegalStateException("Engine could not be loaded");
      }
    }

    return engine;
  }

  public synchronized IPlanner getPlanner()
  {
    if (planner == null)
    {
      planner = (IPlanner)getProvisioningAgent().getService(IPlanner.SERVICE_NAME);
      if (planner == null)
      {
        throw new IllegalStateException("Planner could not be loaded");
      }
    }

    return planner;
  }

  public void flushRepositoryCaches()
  {
    IMetadataRepositoryManager metadataRepositoryManager = getMetadataRepositoryManager();
    if (metadataRepositoryManager instanceof CachingRepositoryManager.Metadata)
    {
      CachingRepositoryManager.Metadata manager = (CachingRepositoryManager.Metadata)metadataRepositoryManager;
      manager.flushCache();
    }

    IArtifactRepositoryManager artifactRepositoryManager = getArtifactRepositoryManager();
    if (artifactRepositoryManager instanceof CachingRepositoryManager.Artifact)
    {
      CachingRepositoryManager.Artifact manager = (CachingRepositoryManager.Artifact)artifactRepositoryManager;
      manager.flushCache();
    }
  }

  @Override
  public String toString()
  {
    return getLocation().getAbsolutePath();
  }

  private void adjustInstallation(IProfile profile)
  {
    // This property should always be in a well-formed profile.
    String bundlePoolLocation = profile.getProperty(IProfile.PROP_CACHE);
    if (bundlePoolLocation != null)
    {
      // There should always be a bundle pool for this location.
      BundlePool bundlePool = getBundlePool(new File(bundlePoolLocation));
      if (bundlePool != null)
      {
        // And there should always be an install folder in a well-formed profile.
        // On the Mac, this should point at the Contents/Eclipse subfolder.
        String installFolderLocation = profile.getProperty(IProfile.PROP_INSTALL_FOLDER);
        if (installFolderLocation != null)
        {
          // The configuration folder should be nested in this install folder.
          File installFolder = new File(installFolderLocation);
          File configurationFolder = new File(installFolder, "configuration");

          // If this is an installation based on a shared pool...
          if ("true".equals(profile.getProperty(Profile.PROP_PROFILE_SHARED_POOL)))
          {
            try
            {
              adjustConfigIni(profile, bundlePool, configurationFolder);
            }
            catch (Exception ex)
            {
              // Ignore.
            }

            try
            {
              adjustLauncherIni(profile, installFolder, true);
            }
            catch (Exception ex)
            {
              // Ignore.
            }

            try
            {
              adjustBundlesInfo(configurationFolder);
            }
            catch (Exception ex)
            {
              // Ignore.
            }

            try
            {
              adjustPlatformXML(bundlePoolLocation, configurationFolder);
            }
            catch (Exception ex)
            {
              // Ignore.
            }
          }
          else
          {
            try
            {
              adjustLauncherIni(profile, installFolder, false);
            }
            catch (Exception ex)
            {
              // Ignore.
            }
          }
        }
      }
    }
  }

  private void adjustConfigIni(IProfile profile, BundlePool bundlePool, File configurationFolder)
  {
    // It should have a config.ini file to set various system properties.
    // If it doesn't exist, it's not a well-formed installation so an exception will be thrown loading it and we'll exit this method as a result.
    File configIniFile = new File(configurationFolder, "config.ini");
    Map<String, String> configProperties = PropertiesUtil.loadProperties(configIniFile);

    // The OSGi slash path might be specified using platform:/base/<bundle-id>
    // but that doesn't work for a shared installation because the referenced bundle doesn't physically exist in the installation.
    boolean changed = false;
    String splashPath = configProperties.get("osgi.splashPath");
    if (splashPath != null)
    {
      // If there is a splash path with a URI of this form...
      org.eclipse.emf.common.util.URI uri = org.eclipse.emf.common.util.URI.createURI(splashPath);
      if ("platform".equals(uri.scheme()) && uri.segmentCount() >= 2 && "base".equals(uri.segment(0)))
      {
        // Determine which IU resolves for bundle-id...
        LOOP: for (IInstallableUnit installableUnit : P2Util.asIterable(profile.query(QueryUtil.createIUQuery(uri.lastSegment()), null)))
        {
          // Look at all that IU's artifacts.
          for (IArtifactKey artifactKey : installableUnit.getArtifacts())
          {
            // If the file slash.bmp exists in that artifact jar or folder...
            File artifactFile = bundlePool.getFileArtifactRepository().getArtifactFile(artifactKey);
            if (artifactFile.isDirectory() ? new File(artifactFile, "splash.bmp").exists()
                : URIConverter.INSTANCE.exists(URI.createURI("archive:" + URI.createFileURI(artifactFile.getAbsolutePath() + "!/splash.bmp")), null))
            {
              // Replace the value with the absolute URI of the artifact in the shared bundle pool.
              // The launcher and p2 have a bad habit of using URL.getPath which does not decode encoded character, e.g., %20 is not decode to the space
              // character.
              // So better we not produce an encoded URI.
              configProperties.put("osgi.splashPath", createFileURI(artifactFile));
              changed = true;
              break LOOP;
            }
          }
        }
      }
    }

    // The OSGi framework property might be a relative path, e.g., file:../<path-to-shared-pool>.
    String osgiFramework = configProperties.get("osgi.framework");
    if (osgiFramework != null)
    {
      // If file: is not followed by a '/', the URI won't be considered hierarchical even though it has file scheme.
      URI uri = URI.createURI(osgiFramework);
      if (uri.hasOpaquePart() && "file".equals(uri.scheme()))
      {
        // Resolve the relative path in the URI against the configuration folder's URI.
        // If that file exists, as expected will be the case.
        URI configurationFolderURI = URI.createFileURI(configurationFolder.toString());
        URI absoluteOSGiFrameworkLocation = URI.createURI(uri.opaquePart()).resolve(configurationFolderURI);
        if (new File(absoluteOSGiFrameworkLocation.toFileString()).exists())
        {
          // Replace the value with the absolute URI of the OSGi framework bundle.
          // The launcher and p2 have a bad habit of using URL.getPath which does not decode encoded character, e.g., %20 is not decode to the space character.
          // So better we not produce an encoded URI.
          configProperties.put("osgi.framework", createFileURI(absoluteOSGiFrameworkLocation.toFileString()));
          changed = true;
        }
      }
    }

    // If there were changes made, save the configuration.
    if (changed)
    {
      PropertiesUtil.saveProperties(configIniFile, configProperties, false, true, "This configuration file was written by: " + AgentImpl.class.getName());
    }
  }

  private void adjustLauncherIni(IProfile profile, File installFolder, boolean sharedPool)
  {
    // We need to modify the eclipse.ini.
    // For generality we'll check the launcher name property to compute the name of the launcher ini.
    String launcherName = profile.getProperty(EclipseTouchpoint.PROFILE_PROP_LAUNCHER_NAME);
    File iniFile = new File(installFolder, launcherName == null ? "eclipse.ini" : launcherName + ".ini");
    String contents = new String(IOUtil.readFile(iniFile));

    // We will process all the sections, keeping them in a map from which we'll compute the modified contents.
    Map<String, String> map = new LinkedHashMap<String, String>();
    for (Matcher matcher = ECLIPSE_INI_SECTION_PATTERN.matcher(contents); matcher.find();)
    {
      // Depending on which part of the pattern matched, we'll need to select the argument and extension from the constituent parts.
      String argument = matcher.group(3);
      String extension;
      if (argument == null)
      {
        // If there is no third group, then the first part of the pattern matched, i.e., the -vmargs and the associated args.
        argument = matcher.group(1);
        extension = matcher.group(2);
      }
      else
      {
        // Otherwise the fourth group is the value part of the argument; it could be an empty string.
        extension = matcher.group(4);
      }

      // This will remove duplicates as well.
      if (!argument.startsWith("--launcher.XXMaxPermSize") && !argument.startsWith("-startup") || !map.containsKey(argument))
      {
        map.put(argument, extension);
      }
    }

    // If there are relative paths, that needs to be made absolute, this is used as the base URI against which to resolve them.
    // We will build the new contents from the entries in the map.
    // And will preserve the existing line feed convention if we need to add new lines.
    URI baseURI = URI.createFileURI(installFolder.toString()).appendSegment("");
    StringBuilder newContents = new StringBuilder();
    String nl = null;
    for (Map.Entry<String, String> entry : map.entrySet())
    {
      // The keys will generally include the line feed character, so we trim that off when inspecting the key.
      String key = entry.getKey();
      String trimmedKey = key.trim();
      String value = entry.getValue();
      if (sharedPool)
      {
        if ("-startup".equals(trimmedKey))
        {
          // Create the URI for the value, and resolve it against the base URI (in case it's relative) and also check that this library file actually exists.
          URI absoluteLauncherLibraryLocation = URI.createFileURI(value.trim()).resolve(baseURI);
          File absoluteLauncherLibraryFile = new File(absoluteLauncherLibraryLocation.toFileString());
          if (absoluteLauncherLibraryFile.isFile())
          {
            // We'll copy this to the installation folder.
            // We do this because the Equinox launcher org.eclipse.equinox.launcher.Main.getInstallLocation()
            // computes the installation location from the location of this bundle.
            // If we leave it as a relative path that references something outside the installation,
            // the installation can't roam.
            // Note that on the Mac we're replacing it with a relative path that starts with ..
            // so we'd better be sure we don't try to copy the file to itself.
            File localLauncherLibraryFile = new File(new File(installFolder, "plugins"), absoluteLauncherLibraryLocation.lastSegment());
            if (!localLauncherLibraryFile.equals(absoluteLauncherLibraryFile))
            {
              IOUtil.copyFile(absoluteLauncherLibraryFile, localLauncherLibraryFile);

              // Remember the line feed convention used for this section.
              nl = key.substring(trimmedKey.length());

              // The value is therefore the relative path to this copied target within the installation.
              value = (Platform.OS_MACOSX.equals(Util.getOSFromProfile(profile)) ? "../Eclipse/plugins/" : "plugins/")
                  + absoluteLauncherLibraryLocation.lastSegment() + nl;
            }
          }
        }
        else if ("--launcher.library".equals(trimmedKey))
        {
          // If the launcher library, where the companion shared library is located, is a relative path.
          if (value.startsWith("../"))
          {
            // Resolve it against against the base to determine the absolute location, checking that actually exists.
            URI absoluteBundleLocation = URI.createURI(value.trim()).resolve(baseURI);
            if (new File(absoluteBundleLocation.toFileString()).exists())
            {
              // Replace the value with this absolute path, preserving the existing line feed convention.
              nl = key.substring(trimmedKey.length());
              value = absoluteBundleLocation.toFileString() + nl;
            }
          }
        }
        else if ("-configuration".equals(trimmedKey))
        {
          // On the Mac, a -configuration ../Eclipse/configuration is produced, but this breaks the ability to run a read-only installation.
          // Moreover, it isn't needed so best to omit it.
          continue;
        }
        else if ("-install".equals(trimmedKey))
        {
          // Omit the install argument.
          // This is generally an absolute path that effectively points at the folder in which the launcher ini is located.
          // This breaks the ability for this installation to roam.
          // Instead of this argument, we've copied the startup bundle into the installation, eliminating the need for this value.
          continue;
        }
      }

      newContents.append(key);
      newContents.append(value);
    }

    if (!contents.contentEquals(newContents))
    {
      IOUtil.writeFile(iniFile, newContents.toString().getBytes());
    }
  }

  private void adjustBundlesInfo(File configurationFolder)
  {
    // For an installation with a shared bundle pool, this will be a relative path that navigates outside of the installation.
    // In that case, we want to make those references be absolute.
    File bundlesInfoFile = new File(configurationFolder, "org.eclipse.equinox.simpleconfigurator/bundles.info");

    // Read all the lines as UTF-8 as documented in a comment in that file.
    List<String> lines = IOUtil.readLines(bundlesInfoFile, "UTF-8");
    List<String> result = new ArrayList<String>();
    boolean changed = false;
    URI configurationFolderURI = URI.createFileURI(configurationFolder.toString());

    for (String line : lines)
    {
      // Ignore the comment lines.
      if (!line.startsWith("#"))
      {
        // Lines of of this form:
        // <bundle-id>,<version>,<location-URI>,<start-level>,<true|false>
        // As such, we can split the lines on ',' and generally expect 5 elements in the list.
        List<String> elements = StringUtil.explode(line, ",");
        if (elements.size() > 2)
        {
          // If the third element that needs to be modified it it's a relative path.
          String bundleReference = elements.get(2);
          if (bundleReference.startsWith("../"))
          {
            // Resolve it against the location of the configuration folder, and if that bundle exists, as expected...
            URI absoluteBundleLocation = URI.createURI(bundleReference).resolve(configurationFolderURI);
            if (new File(absoluteBundleLocation.toFileString()).exists())
            {
              // Replace the element, with that absolute URI,
              // add that replacement to the result,
              // and continue with the next line.
              elements.set(2, createFileURI(absoluteBundleLocation.toFileString()));
              result.add(StringUtil.implode(elements, ',', (char)0));
              changed = true;
              continue;
            }
          }
        }
      }

      // Otherwise add the line as-is to the result.
      result.add(line);
    }

    // If there are any changes...
    if (changed)
    {
      // Write the modified result back out to the bundles.info file.
      IOUtil.writeLines(bundlesInfoFile, "UTF-8", result);
    }
  }

  private void adjustPlatformXML(String bundlePoolLocation, File configurationFolder) throws ProvisionException
  {
    // The platform.xml, initially will have the wrong site policy.
    // Also, regular p2 updates will mangle the site URL to be an poorly-formed relative file URI,
    // which again breaks the ability for the installation to roam.
    File platformXML = new File(configurationFolder, "org.eclipse.update/platform.xml");
    if (platformXML.isFile())
    {
      URI configurationFolderURI = URI.createFileURI(configurationFolder.toString());
      Configuration configuration = Configuration.load(platformXML, null);
      boolean changed = true;
      for (Site site : configuration.getSites())
      {
        // If the site URI is of the form file:../<path> then we need to make it absolute.
        URI siteURI = URI.createURI(site.getUrl());
        if (siteURI.hasOpaquePart() && "file".equals(siteURI.scheme()))
        {
          // Resolve it against the location of the configuration folder, checking if the folder really exists.
          URI absoluteSiteURI = URI.createURI(siteURI.opaquePart()).resolve(configurationFolderURI);
          if (new File(absoluteSiteURI.toFileString()).exists())
          {
            // If so, we use this absolute URI instead and change the configuration to specify it.
            siteURI = absoluteSiteURI;
            site.setUrl(absoluteSiteURI.toFileString());
            changed = true;
          }
        }

        // If the policy isn't managed all the bundles in the pool will be visible, which is very bad.
        if (!Site.POLICY_MANAGED_ONLY.equals(site.getPolicy()))
        {
          // If this site refers to the shared bundle pool, change the policy to be managed.
          File siteLocation = new File(siteURI.toFileString());
          if (siteLocation.equals(new File(bundlePoolLocation)))
          {
            site.setPolicy(Site.POLICY_MANAGED_ONLY);
            changed = true;
            break;
          }
        }
      }

      // If something has changed, save the configuration.
      if (changed)
      {
        configuration.save(platformXML, null);
      }
    }
  }

  public static boolean isValid(File location)
  {
    return new File(location, ENGINE_PATH).isDirectory();
  }

  public static String getProfileType(IProfile delegate)
  {
    String type = delegate.getProperty(Profile.PROP_PROFILE_TYPE);
    if (type == null)
    {
      // This check is done for legacy reasons. The current targlet.ui plugin contributes a profileType extension.
      if (delegate.getProperty("targlet.container.id") != null)
      {
        return "Targlet";
      }

      if (delegate.getProperty(Profile.PROP_INSTALL_FOLDER) != null)
      {
        return "Installation";
      }
    }

    return type;
  }

  public static String getProfileExtraInfo(IProfile delegate)
  {
    List<String> tokens = new ArrayList<String>();
    tokens.add(getProfileType(delegate));
    tokens.add(delegate.getProperty(Profile.PROP_CACHE));
    tokens.add(delegate.getProperty(Profile.PROP_INSTALL_FOLDER));
    tokens.add(delegate.getProperty(Profile.PROP_PROFILE_REFERENCER));
    return StringUtil.implode(tokens, '|');
  }

  public static IProvisioningAgent getOrCreateProvisioningAgent(File location)
  {
    BundleContext context = P2CorePlugin.INSTANCE.getBundleContext();
    ServiceReference<IProvisioningAgent> agentRef = null;
    IProvisioningAgent provisioningAgent = null;

    try
    {
      Collection<ServiceReference<IProvisioningAgent>> agentRefs = null;

      try
      {
        agentRefs = context.getServiceReferences(IProvisioningAgent.class, "(locationURI=" + location.toURI() + ")");
      }
      catch (InvalidSyntaxException ex)
      {
        // Can't happen because we write the filter ourselves
      }

      if (agentRefs != null && !agentRefs.isEmpty())
      {
        agentRef = agentRefs.iterator().next();
        provisioningAgent = context.getService(agentRef);
      }
    }
    catch (Exception ex)
    {
      provisioningAgent = null;
    }
    finally
    {
      if (agentRef != null)
      {
        context.ungetService(agentRef);
      }
    }

    if (provisioningAgent == null)
    {
      location.mkdirs();
      ServiceReference<IProvisioningAgentProvider> providerRef = null;

      try
      {
        providerRef = context.getServiceReference(IProvisioningAgentProvider.class);
        if (providerRef != null)
        {
          IProvisioningAgentProvider provider = context.getService(providerRef);
          provisioningAgent = provider.createAgent(location.toURI());
        }
      }
      catch (ProvisionException ex)
      {
        throw new P2Exception(ex);
      }
      finally
      {
        if (providerRef != null)
        {
          context.ungetService(providerRef);
        }
      }
    }

    if (provisioningAgent == null)
    {
      throw new P2Exception("Provisioning agent could not be loaded from " + location);
    }

    return provisioningAgent;
  }

  private static boolean cacheTransport(Transport transport)
  {
    if (PropertiesUtil.isProperty("oomph.p2.disable.offline"))
    {
      return false;
    }

    // If a location is listed twice or the location to be installed in is also listed in the agents.info we may already have a CachingTransport.
    if (transport instanceof CachingTransport)
    {
      return false;
    }

    // We can only cache if we can delegate to an existing transport.
    return transport != null;
  }

  private static File getFile(String path)
  {
    if (StringUtil.isEmpty(path))
    {
      return null;
    }

    return new File(path);
  }

  private static String createFileURI(String path)
  {
    return createFileURI(new File(path));
  }

  static String createFileURI(File file)
  {
    String path = file.getPath();
    if (File.separatorChar != '/')
    {
      path = path.replace(File.separatorChar, '/');
    }

    if (file.isAbsolute() && !path.startsWith(File.separator))
    {
      return "file:/" + path;
    }

    return "file:" + path;
  }
}
