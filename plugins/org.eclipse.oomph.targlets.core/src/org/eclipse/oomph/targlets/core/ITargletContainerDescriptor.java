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
package org.eclipse.oomph.targlets.core;

import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.targlets.internal.core.TargletsCorePlugin;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.director.Explanation;
import org.eclipse.equinox.internal.p2.metadata.IRequiredCapability;
import org.eclipse.equinox.p2.metadata.IRequirement;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author Eike Stepper
 */
public interface ITargletContainerDescriptor
{
  public static final String PROP_TARGLET_CONTAINER_WORKSPACE = "targlet.container.workspace"; //$NON-NLS-1$

  public static final String PROP_TARGLET_CONTAINER_ID = "targlet.container.id"; //$NON-NLS-1$

  public static final String PROP_TARGLET_CONTAINER_DIGEST = "targlet.container.digest"; //$NON-NLS-1$

  public String getID();

  public File getPoolLocation();

  public String getWorkingDigest();

  public Collection<WorkspaceIUInfo> getWorkingProjects();

  public UpdateProblem getUpdateProblem();

  public BundlePool getBundlePool();

  public String getWorkingProfileID();

  public Profile getWorkingProfile();

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
