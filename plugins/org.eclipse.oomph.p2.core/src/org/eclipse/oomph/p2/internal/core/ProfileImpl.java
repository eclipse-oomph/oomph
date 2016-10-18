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
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.equinox.internal.p2.engine.SimpleProfileRegistry;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;

import org.xml.sax.InputSource;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class ProfileImpl extends AgentManagerElementImpl implements Profile, PersistentMap.ExtraInfoProvider
{
  private static final Method INTERNAL_GET_PROFILE_METHOD;

  private static final Map<Object, Object> XML_OPTIONS;

  static
  {
    Method method = null;

    try
    {
      method = ReflectUtil.getMethod(SimpleProfileRegistry.class, "internalGetProfile", String.class);
    }
    catch (Throwable ex)
    {
      //$FALL-THROUGH$
    }

    if (method != null)
    {
      INTERNAL_GET_PROFILE_METHOD = method;
      INTERNAL_GET_PROFILE_METHOD.setAccessible(true);
    }
    else
    {
      INTERNAL_GET_PROFILE_METHOD = null;
    }

    Map<Object, Object> options = new HashMap<Object, Object>();
    options.put(XMLResource.OPTION_DECLARE_XML, Boolean.FALSE);
    options.put(XMLResource.OPTION_EXTENDED_META_DATA, new BasicExtendedMetaData()
    {
      @Override
      public EStructuralFeature getElement(EClass eClass, String namespace, String name)
      {
        EStructuralFeature eStructuralFeature = super.getElement(eClass, namespace, name);
        if (eStructuralFeature == null)
        {
          eStructuralFeature = super.getElement(eClass, namespace, name.substring(0, name.length() - 1));
        }

        if (eStructuralFeature == null)
        {
          eStructuralFeature = eClass.getEStructuralFeature(name);
        }

        return eStructuralFeature;
      }

      @Override
      protected boolean isFeatureKindSpecific()
      {
        return false;
      }
    });

    XML_OPTIONS = Collections.unmodifiableMap(options);
  }

  private final Agent agent;

  private final BundlePool bundlePool;

  private final String id;

  private final String type;

  private File installFolder;

  private File referencer;

  private IProfile delegate;

  private ProfileDefinition definition;

  public ProfileImpl(Agent agent, BundlePool bundlePool, String id, String type, File installFolder, File referencer)
  {
    this.agent = agent;
    this.bundlePool = bundlePool;
    this.id = id;
    this.type = type;
    this.installFolder = installFolder;
    this.referencer = referencer;
  }

  @Override
  public String getElementType()
  {
    return "profile";
  }

  public AgentManager getAgentManager()
  {
    return agent.getAgentManager();
  }

  public Agent getAgent()
  {
    return agent;
  }

  public BundlePool getBundlePool()
  {
    return bundlePool;
  }

  public String getProfileId()
  {
    return id;
  }

  public boolean isSelfHosting()
  {
    return "SelfHostingProfile".equals(id);
  }

  public String getType()
  {
    return type;
  }

  public File getLocation()
  {
    File engineLocation = new File(agent.getLocation(), AgentImpl.ENGINE_PATH);
    File registryLocation = new File(engineLocation, SimpleProfileRegistry.DEFAULT_STORAGE_DIR);
    return new File(registryLocation, id + ".profile");
  }

  public File getInstallFolder()
  {
    return installFolder;
  }

  public void setInstallFolder(File installFolder)
  {
    this.installFolder = installFolder;
  }

  public File getReferencer()
  {
    return referencer;
  }

  public void setReferencer(File referencer)
  {
    this.referencer = referencer;
  }

  public String getExtraInfo()
  {
    List<String> tokens = new ArrayList<String>();
    tokens.add(type);
    tokens.add(bundlePool != null ? bundlePool.getLocation().getAbsolutePath() : "");
    tokens.add(installFolder != null ? installFolder.getAbsolutePath() : "");
    tokens.add(referencer != null ? referencer.getAbsolutePath() : "");
    return StringUtil.implode(tokens, '|');
  }

  public synchronized IProfile getDelegate()
  {
    return getDelegate(true);
  }

  public synchronized IProfile getDelegate(boolean loadOnDemand)
  {
    IProfileRegistry profileRegistry = null;

    if (delegate == null)
    {
      if (loadOnDemand)
      {
        profileRegistry = getAgent().getProfileRegistry();
        doSetDelegate(profileRegistry);
      }
    }

    if (delegate != null)
    {
      if (profileRegistry == null)
      {
        profileRegistry = getAgent().getProfileRegistry();
      }

      IProfile internalProfile = null;
      if (INTERNAL_GET_PROFILE_METHOD != null)
      {
        try
        {
          internalProfile = (IProfile)INTERNAL_GET_PROFILE_METHOD.invoke(profileRegistry, id);
        }
        catch (Throwable ex)
        {
          //$FALL-THROUGH$
        }
      }

      if (internalProfile == null)
      {
        internalProfile = getProfileSafe(profileRegistry);
      }

      if (delegate.getTimestamp() != internalProfile.getTimestamp())
      {
        doSetDelegate(profileRegistry);
      }
    }

    return delegate;
  }

  private void doSetDelegate(IProfileRegistry profileRegistry)
  {
    // Create a new snapshot from the internally registered profile.
    delegate = getProfileSafe(profileRegistry);
  }

  private IProfile getProfileSafe(IProfileRegistry profileRegistry)
  {
    IProfile profile = profileRegistry.getProfile(id);
    if (profile == null)
    {
      throw new P2Exception("Profile does not exist: " + id);
    }

    return profile;
  }

  public void setDelegate(IProfile delegate)
  {
    this.delegate = delegate;
  }

  public synchronized ProfileDefinition getDefinition()
  {
    if (definition == null)
    {
      String xml = getDelegate().getProperty(PROP_PROFILE_DEFINITION);
      if (xml == null)
      {
        // TODO This is also called via ProfileTransactionImpl() for new profiles. Should it be empty then?
        definition = definitionFromRootIUs(this, VersionSegment.MINOR);
      }
      else
      {
        definition = definitionFromXML(xml);
      }
    }

    return definition;
  }

  public void setDefinition(ProfileDefinition definition)
  {
    this.definition = definition;
  }

  public boolean isValid()
  {
    return getLocation().isDirectory();
  }

  public boolean isCurrent()
  {
    return agent.getCurrentProfile() == this;
  }

  public boolean isUsed()
  {
    File referencer = this.referencer;
    if (referencer == null && "Targlet".equals(type))
    {
      String workspace = getProperty("targlet.container.workspace");
      if (workspace != null)
      {
        referencer = new File(workspace, ".metadata/.plugins/org.eclipse.oomph.targlets.core/profiles.txt");
      }
    }

    if (referencer != null)
    {
      if (!referencer.exists())
      {
        return false;
      }

      if (referencer.isFile())
      {
        List<String> lines = IOUtil.readLines(referencer, null);
        if (!lines.contains(id))
        {
          return false;
        }
      }

      return true;
    }

    return installFolder == null || installFolder.isDirectory();
  }

  @Override
  protected void doDelete()
  {
    ((AgentImpl)agent).deleteProfile(this);
  }

  public ProfileTransaction change()
  {
    return new ProfileTransactionImpl(this);
  }

  public IQueryResult<IInstallableUnit> query(IQuery<IInstallableUnit> query, IProgressMonitor monitor)
  {
    return getDelegate().query(query, monitor);
  }

  public IProvisioningAgent getProvisioningAgent()
  {
    return getDelegate().getProvisioningAgent();
  }

  public String getProperty(String key)
  {
    return getDelegate().getProperty(key);
  }

  public String getInstallableUnitProperty(IInstallableUnit iu, String key)
  {
    return getDelegate().getInstallableUnitProperty(iu, key);
  }

  public Map<String, String> getProperties()
  {
    return getDelegate().getProperties();
  }

  public Map<String, String> getInstallableUnitProperties(IInstallableUnit iu)
  {
    return getDelegate().getInstallableUnitProperties(iu);
  }

  public long getTimestamp()
  {
    return getDelegate().getTimestamp();
  }

  public IQueryResult<IInstallableUnit> available(IQuery<IInstallableUnit> query, IProgressMonitor monitor)
  {
    return getDelegate().available(query, monitor);
  }

  @Override
  public String toString()
  {
    return id;
  }

  public static String definitionToXML(ProfileDefinition definition)
  {
    try
    {
      StringWriter writer = new StringWriter();
      XMLResource resource = new XMLResourceImpl();
      resource.getContents().add(definition);
      resource.save(writer, XML_OPTIONS);
      return writer.toString();
    }
    catch (IOException ex)
    {
      throw new P2Exception(ex);
    }
  }

  public static ProfileDefinition definitionFromXML(String xml)
  {
    try
    {
      XMLResource resource = new XMLResourceImpl();
      resource.load(new InputSource(new StringReader(xml)), XML_OPTIONS);
      return (ProfileDefinition)resource.getContents().get(0);
    }
    catch (IOException ex)
    {
      throw new P2Exception(ex);
    }
  }

  public static ProfileDefinition definitionFromRootIUs(Profile profile, VersionSegment compatibility)
  {
    ProfileDefinition definition = P2Factory.eINSTANCE.createProfileDefinition();

    EList<Requirement> requirements = definition.getRequirements();
    IQueryResult<IInstallableUnit> query = profile.query(QueryUtil.createIUAnyQuery(), null);
    for (IInstallableUnit iu : P2Util.asIterable(query))
    {
      if ("true".equals(profile.getInstallableUnitProperty(iu, Profile.PROP_PROFILE_ROOT_IU)))
      {
        VersionSegment iuCompatibility = null;

        String iuProperty = profile.getInstallableUnitProperty(iu, Profile.PROP_IU_COMPATIBILITY);
        if (iuProperty != null)
        {
          iuCompatibility = VersionSegment.get(iuProperty);
        }

        if (iuCompatibility == null)
        {
          iuProperty = iu.getProperty(Profile.PROP_IU_COMPATIBILITY);
          if (iuProperty != null)
          {
            iuCompatibility = VersionSegment.get(iuProperty);
          }
        }

        if (iuCompatibility == null)
        {
          iuCompatibility = compatibility;
        }

        VersionRange versionRange = P2Factory.eINSTANCE.createVersionRange(iu.getVersion(), iuCompatibility);

        Requirement requirement = P2Factory.eINSTANCE.createRequirement(iu.getId());
        requirement.setVersionRange(versionRange);
        requirement.setMatchExpression(iu.getFilter());
        requirements.add(requirement);
      }
    }

    IMetadataRepositoryManager metadataRepositoryManager = profile.getAgent().getMetadataRepositoryManager();
    java.net.URI[] knownRepositories = metadataRepositoryManager.getKnownRepositories(IRepositoryManager.REPOSITORIES_NON_SYSTEM);
    if (knownRepositories.length > 0)
    {
      EList<Repository> repositories = definition.getRepositories();
      for (java.net.URI knownRepository : knownRepositories)
      {
        if (metadataRepositoryManager.isEnabled(knownRepository))
        {
          if (URIUtil.isFileURI(knownRepository))
          {
            File file = URIUtil.toFile(knownRepository);
            if (!file.isDirectory())
            {
              metadataRepositoryManager.setEnabled(knownRepository, false);
              continue;
            }
          }

          Repository repository = P2Factory.eINSTANCE.createRepository(knownRepository.toString());
          repositories.add(repository);
        }
      }
    }

    return definition;
  }
}
