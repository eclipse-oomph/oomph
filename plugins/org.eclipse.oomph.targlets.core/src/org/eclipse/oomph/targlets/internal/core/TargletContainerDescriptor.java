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
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectInputStream;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl.EObjectOutputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.director.Explanation;
import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.metadata.IRequirement;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;

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

  private static final long serialVersionUID = 2L;

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

  /**
   * @author Eike Stepper
   */
  public static class UpdateProblem implements Serializable, IStatus
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

    protected UpdateProblem(IStatus status)
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
          children[i] = UpdateProblem.create(statusChild);
        }
      }
    }

    public static UpdateProblem create(Throwable t)
    {
      return create(TargletsCorePlugin.INSTANCE.getStatus(t));
    }

    @SuppressWarnings("restriction")
    public static UpdateProblem create(IStatus status)
    {
      if (status instanceof org.eclipse.equinox.internal.provisional.p2.director.PlannerStatus)
      {
        org.eclipse.equinox.internal.provisional.p2.director.PlannerStatus plannerStatus = (org.eclipse.equinox.internal.provisional.p2.director.PlannerStatus)status;
        org.eclipse.equinox.internal.provisional.p2.director.RequestStatus requestStatus = plannerStatus.getRequestStatus();
        if (requestStatus != null)
        {
          Explanation explanation = requestStatus.getExplanationDetails();
          if (explanation instanceof Explanation.MissingIU)
          {
            Explanation.MissingIU detailedExplanation = (Explanation.MissingIU)explanation;
            IRequirement req = detailedExplanation.req;
            if (req instanceof IRequiredCapability)
            {
              return new MissingIU(status, (IRequiredCapability)req);
            }
          }
        }
      }

      return new UpdateProblem(status);
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
          exception = new CoreException(UpdateProblem.create(coreException.getStatus()));
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

    /**
     * @author Eike Stepper
     */
    public static final class MissingIU extends UpdateProblem
    {
      private static final long serialVersionUID = 1L;

      private String namespace;

      private String name;

      private String range;

      public MissingIU()
      {
      }

      protected MissingIU(IStatus status, IRequiredCapability requirement)
      {
        super(status);
        namespace = requirement.getNamespace();
        name = requirement.getName();
        range = requirement.getRange().toString();
      }

      public String getNamespace()
      {
        return namespace;
      }

      public String getName()
      {
        return name;
      }

      public String getRange()
      {
        return range;
      }
    }
  }
}
