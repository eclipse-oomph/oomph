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
package org.eclipse.oomph.targlets.internal.core;

import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileCreator;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public final class TargletContainerDescriptor implements Serializable, Comparable<TargletContainerDescriptor>
{
  public static final String PROP_TARGLET_CONTAINER_WORKSPACE = "targlet.container.workspace"; //$NON-NLS-1$

  public static final String PROP_TARGLET_CONTAINER_ID = "targlet.container.id"; //$NON-NLS-1$

  public static final String PROP_TARGLET_CONTAINER_DIGEST = "targlet.container.digest"; //$NON-NLS-1$

  @SuppressWarnings("restriction")
  private static final IPath INSTALL_FOLDERS = org.eclipse.pde.internal.core.PDECore.getDefault().getStateLocation().append(".install_folders");

  private static final long serialVersionUID = 1L;

  private static long lastStamp;

  private String id;

  private File poolLocation;

  private String workingDigest;

  private Set<File> workingProjects;

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

  public String getID()
  {
    return id;
  }

  public File getPoolLocation()
  {
    return poolLocation;
  }

  public String getWorkingDigest()
  {
    return workingDigest;
  }

  public Set<File> getWorkingProjects()
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

  void commitUpdateTransaction(String digest, Set<File> projectLocations, IProgressMonitor monitor) throws CoreException
  {
    if (transactionProfile == null)
    {
      throw new ProvisionException("No update transaction is ongoing");
    }

    transactionProfile = null;
    workingDigest = digest;
    workingProjects = projectLocations;
    resetUpdateProblem();

    saveDescriptors(monitor);
  }

  void rollbackUpdateTransaction(Throwable t, IProgressMonitor monitor) throws CoreException
  {
    transactionProfile = null;
    updateProblem = new UpdateProblem(t);

    saveDescriptors(monitor);
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

    return getBundlePool().getProfile(profileID);
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
      ProfileCreator creator = bundlePool.addProfile(profileID, "Targlet");
      creator.set(PROP_TARGLET_CONTAINER_WORKSPACE, TargletContainerManager.WORKSPACE_LOCATION);
      creator.set(PROP_TARGLET_CONTAINER_ID, id);
      creator.set(PROP_TARGLET_CONTAINER_DIGEST, digest);
      creator.set(IProfile.PROP_INSTALL_FOLDER, INSTALL_FOLDERS.append(Long.toString(nextTimeStamp())).toOSString());
      creator.setEnvironments(environmentProperties);
      creator.setLanguages(nlProperty);
      creator.setInstallFeatures(true);
      creator.setReferencer(TargletContainerManager.WORKSPACE_REFERENCER_FILE);
      profile = creator.create();
    }

    return profile;
  }

  private static String getProfileID(String suffix)
  {
    return StringUtil.encodePath(TargletContainerManager.WORKSPACE_LOCATION) + "-" + suffix;
  }

  private static void saveDescriptors(IProgressMonitor monitor) throws CoreException
  {
    TargletContainerManager manager = TargletContainerManager.getInstance();
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

  /**
   * @author Eike Stepper
   */
  public static final class UpdateProblem implements Serializable, IStatus
  {
    private static final long serialVersionUID = 3L;

    private static final UpdateProblem[] NO_CHILDREN = {};

    private String plugin;

    private String message;

    private int severity;

    private int code;

    private Throwable exception;

    private UpdateProblem[] children;

    public UpdateProblem()
    {
    }

    public UpdateProblem(Throwable t)
    {
      this(TargletsCorePlugin.INSTANCE.getStatus(t));
    }

    public UpdateProblem(IStatus status)
    {
      plugin = status.getPlugin();
      message = status.getMessage();
      severity = status.getSeverity();
      code = status.getCode();
      exception = toSerializeableException(status.getException());

      IStatus[] statusChildren = status.getChildren();
      if (statusChildren != null && statusChildren.length != 0)
      {
        children = new UpdateProblem[statusChildren.length];
        for (int i = 0; i < statusChildren.length; i++)
        {
          IStatus statusChild = statusChildren[i];
          children[i] = new UpdateProblem(statusChild);
        }
      }
    }

    public String getPlugin()
    {
      return plugin;
    }

    public String getMessage()
    {
      return message;
    }

    public int getSeverity()
    {
      return severity;
    }

    public int getCode()
    {
      return code;
    }

    public Throwable getException()
    {
      return exception;
    }

    public UpdateProblem[] getChildren()
    {
      return isMultiStatus() ? children : NO_CHILDREN;
    }

    public boolean isMultiStatus()
    {
      return children != null;
    }

    public boolean isOK()
    {
      return severity == OK;
    }

    public boolean matches(int severityMask)
    {
      return (severity & severityMask) != 0;
    }

    public IStatus toStatus()
    {
      if (isMultiStatus())
      {
        IStatus[] statusChildren = new IStatus[children.length];
        for (int i = 0; i < children.length; i++)
        {
          statusChildren[i] = children[i].toStatus();
        }

        return new MultiStatus(plugin, code, statusChildren, message, exception);
      }

      return new Status(severity, plugin, code, message, exception);
    }

    @Override
    public String toString()
    {
      StringBuilder builder = new StringBuilder();
      toString(builder, 0);
      return builder.toString();
    }

    private void toString(StringBuilder builder, int level)
    {
      for (int i = 0; i < level; i++)
      {
        builder.append(' ');
      }

      switch (severity)
      {
        case IStatus.OK:
          builder.append("OK ");
          break;

        case IStatus.INFO:
          builder.append("INFO ");
          break;

        case IStatus.WARNING:
          builder.append("WARNING ");
          break;

        case IStatus.ERROR:
          builder.append("ERROR ");
          break;

        case IStatus.CANCEL:
          builder.append("CANCEL ");
          break;

        default:
      }

      builder.append(message);
      builder.append(StringUtil.NL);
    }

    private Throwable toSerializeableException(Throwable exception)
    {
      if (exception != null && !IOUtil.isSerializeable(exception))
      {
        if (exception instanceof CoreException)
        {
          CoreException coreException = (CoreException)exception;
          exception = new CoreException(new UpdateProblem(coreException.getStatus()));
        }
        else
        {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintWriter writer = new PrintWriter(baos);
          exception.printStackTrace(writer);
          writer.close();

          exception = new RuntimeException(baos.toString());
        }
      }

      return exception;
    }
  }
}
