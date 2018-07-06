/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileCreator;
import org.eclipse.oomph.targlets.core.ITargletContainerDescriptor;
import org.eclipse.oomph.targlets.core.WorkspaceIUInfo;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectInputStream;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectOutputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Eike Stepper
 */
public final class TargletContainerDescriptor implements ITargletContainerDescriptor, Comparable<TargletContainerDescriptor>
{
  public static final String PROP_TARGLET_CONTAINER_WORKSPACE = "targlet.container.workspace"; //$NON-NLS-1$

  public static final String PROP_TARGLET_CONTAINER_ID = "targlet.container.id"; //$NON-NLS-1$

  public static final String PROP_TARGLET_CONTAINER_DIGEST = "targlet.container.digest"; //$NON-NLS-1$

  @SuppressWarnings("restriction")
  private static final IPath INSTALL_FOLDERS = org.eclipse.pde.internal.core.PDECore.getDefault().getStateLocation().append(".install_folders");

  private static final File DEFAULT_INSTALL_FOLDER = INSTALL_FOLDERS.append("default").toFile();

  private static long lastStamp;

  private String id;

  private File poolLocation;

  private String workingDigest;

  private Collection<WorkspaceIUInfo> workingProjects;

  private UpdateProblem updateProblem;

  private transient BundlePool bundlePool;

  private transient Profile transactionProfile;

  public TargletContainerDescriptor()
  {
  }

  public TargletContainerDescriptor(String id, File poolLocation)
  {
    this.id = id;
    this.poolLocation = poolLocation;
  }

  public TargletContainerDescriptor(EObjectInputStream stream) throws IOException
  {
    id = stream.readString();

    String path = stream.readString();
    if (path != null)
    {
      poolLocation = new File(path);
    }

    workingDigest = stream.readString();

    int size = stream.readInt();
    if (size != 0)
    {
      workingProjects = new ArrayList<WorkspaceIUInfo>();
      for (int i = 0; i < size; i++)
      {
        WorkspaceIUInfo workspaceIUInfo = new WorkspaceIUInfo(stream);
        workingProjects.add(workspaceIUInfo);
      }
    }

    size = stream.readInt();
    if (size != 0)
    {
      byte[] bytes = new byte[size];
      for (int i = 0; i < size; i++)
      {
        bytes[i] = stream.readByte();
      }

      try
      {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        updateProblem = (UpdateProblem)new ObjectInputStream(bais).readObject();
      }
      catch (ClassNotFoundException ex)
      {
        throw new IORuntimeException(ex);
      }
    }
  }

  public void write(EObjectOutputStream stream) throws IOException
  {
    stream.writeString(id);
    stream.writeString(poolLocation == null ? null : poolLocation.getAbsolutePath());
    stream.writeString(workingDigest);

    int size = workingProjects == null ? 0 : workingProjects.size();
    stream.writeInt(size);
    if (size != 0)
    {
      for (WorkspaceIUInfo workspaceIUInfo : workingProjects)
      {
        workspaceIUInfo.write(stream);
      }
    }

    if (updateProblem != null)
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(updateProblem);
      oos.close();

      byte[] bytes = baos.toByteArray();
      stream.writeInt(bytes.length);

      for (int i = 0; i < bytes.length; i++)
      {
        byte b = bytes[i];
        stream.writeByte(b);
      }
    }
    else
    {
      stream.writeInt(0);
    }
  }

  public String getID()
  {
    return id;
  }

  public File getPoolLocation()
  {
    return poolLocation;
  }

  public File getInstallLocation()
  {
    Profile workingProfile = getWorkingProfile();
    if (workingProfile != null)
    {
      String installFolder = workingProfile.getProperty(IProfile.PROP_INSTALL_FOLDER);
      if (installFolder != null)
      {
        return new File(installFolder);
      }
    }

    return DEFAULT_INSTALL_FOLDER;
  }

  public String getWorkingDigest()
  {
    return workingDigest;
  }

  public Collection<WorkspaceIUInfo> getWorkingProjects()
  {
    return workingProjects;
  }

  public UpdateProblem getUpdateProblem()
  {
    return updateProblem;
  }

  public synchronized BundlePool getBundlePool()
  {
    if (bundlePool == null)
    {
      AgentManager agentManager = P2Util.getAgentManager();
      bundlePool = agentManager.getBundlePool(poolLocation);
    }

    return bundlePool;
  }

  public int compareTo(TargletContainerDescriptor o)
  {
    return id.compareTo(o.getID());
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (id == null ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    TargletContainerDescriptor other = (TargletContainerDescriptor)obj;
    if (id == null)
    {
      if (other.id != null)
      {
        return false;
      }
    }
    else if (!id.equals(other.id))
    {
      return false;
    }

    return true;
  }

  @Override
  public String toString()
  {
    return MessageFormat.format("TargletContainerDescriptor[id={0}, workingDigest={1}]", id, workingDigest);
  }

  Profile startUpdateTransaction(String environmentProperties, String nlProperty, String digest, IProgressMonitor monitor) throws CoreException
  {
    if (transactionProfile != null)
    {
      throw new ProvisionException("An update transaction is already ongoing");
    }

    transactionProfile = getOrCreateProfile(id, poolLocation, environmentProperties, nlProperty, digest, monitor);
    return transactionProfile;
  }

  void commitUpdateTransaction(String digest, Collection<WorkspaceIUInfo> workspaceIUInfos, IProgressMonitor monitor) throws CoreException
  {
    if (transactionProfile == null)
    {
      throw new ProvisionException("No update transaction is ongoing");
    }

    transactionProfile = null;
    workingDigest = digest;
    workingProjects = workspaceIUInfos;
    resetUpdateProblem();

    saveDescriptors(monitor);
  }

  void rollbackUpdateTransaction(Throwable t, IProgressMonitor monitor) throws CoreException
  {
    transactionProfile = null;

    if (t != null)
    {
      updateProblem = UpdateProblem.create(t);

      saveDescriptors(monitor);
    }
  }

  void resetUpdateProblem()
  {
    updateProblem = null;
  }

  public String getWorkingProfileID()
  {
    if (workingDigest == null)
    {
      return null;
    }

    return getProfileID(workingDigest);
  }

  public Profile getWorkingProfile()
  {
    String profileID = getWorkingProfileID();
    if (profileID == null)
    {
      return null;
    }

    BundlePool bundlePool = getBundlePool();
    if (bundlePool == null)
    {
      return null;
    }

    return bundlePool.getProfile(profileID);
  }

  private Profile getOrCreateProfile(String id, File poolLocation, String environmentProperties, String nlProperty, String digest, IProgressMonitor monitor)
      throws CoreException
  {
    AgentManager agentManager = P2Util.getAgentManager();
    BundlePool bundlePool = agentManager.getBundlePool(poolLocation);

    String profileID = getProfileID(digest);

    Profile profile = bundlePool.getProfile(profileID);
    if (profile != null)
    {
      File installFolder = profile.getInstallFolder();
      if (installFolder == null)
      {
        // TODO Remove this code after a while (install folder property is set for all profiles below).
        profile.delete(true);
        profile = null;
      }
    }

    if (profile == null)
    {
      ProfileCreator creator = bundlePool.addProfile(profileID, Profile.TYPE_TARGLET);
      creator.set(PROP_TARGLET_CONTAINER_WORKSPACE, TargletContainerDescriptorManager.WORKSPACE_LOCATION);
      creator.set(PROP_TARGLET_CONTAINER_ID, id);
      creator.set(PROP_TARGLET_CONTAINER_DIGEST, digest);
      creator.set(IProfile.PROP_INSTALL_FOLDER, INSTALL_FOLDERS.append(Long.toString(nextTimeStamp())).toOSString());
      creator.setEnvironments(environmentProperties);
      creator.setLanguages(nlProperty);
      creator.setInstallFeatures(true);
      creator.setReferencer(TargletContainerDescriptorManager.WORKSPACE_REFERENCER_FILE);
      profile = creator.create();
    }

    return profile;
  }

  private static String getProfileID(String suffix)
  {
    return IOUtil.encodeFileName(TargletContainerDescriptorManager.WORKSPACE_LOCATION + "-" + suffix);
  }

  private static void saveDescriptors(IProgressMonitor monitor) throws CoreException
  {
    TargletContainerDescriptorManager manager = TargletContainerDescriptorManager.getInstance();
    manager.saveDescriptors(monitor);
  }

  private static synchronized long nextTimeStamp()
  {
    long stamp = System.currentTimeMillis();
    if (stamp == lastStamp)
    {
      stamp++;
    }
    lastStamp = stamp;
    return stamp;
  }
}
