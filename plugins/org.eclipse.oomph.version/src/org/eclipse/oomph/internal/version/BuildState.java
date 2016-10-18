/*
 * Copyright (c) 2014, 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.internal.version;

import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.version.IBuildState;

import org.eclipse.pde.core.IModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class BuildState implements IBuildState, Serializable
{
  private static final long serialVersionUID = 2L;

  private Map<String, String> arguments;

  private byte[] releaseSpecDigest;

  private long propertiesTimeStamp;

  private boolean deviations;

  private boolean integration;

  private boolean service;

  private Set<String> rootProjects;

  private Set<String> ignoredReferences;

  private Map<String, Long> validatorTimeStamps = new LinkedHashMap<String, Long>();

  private String validatorClass;

  private String validatorVersion;

  private byte[] validatorBytes;

  private transient Map<String, Serializable> validatorStates;

  private transient boolean changedSinceRelease;

  BuildState()
  {
  }

  public Map<String, String> getArguments()
  {
    return arguments == null ? new HashMap<String, String>() : arguments;
  }

  public void setArguments(Map<String, String> arguments)
  {
    this.arguments = arguments;
  }

  public byte[] getReleaseSpecDigest()
  {
    return releaseSpecDigest;
  }

  public void setReleaseSpecDigest(byte[] releaseSpecDigest)
  {
    this.releaseSpecDigest = releaseSpecDigest;
  }

  public long getPropertiesTimeStamp()
  {
    return propertiesTimeStamp;
  }

  public void setPropertiesTimeStamp(long propertiesTimeStamp)
  {
    this.propertiesTimeStamp = propertiesTimeStamp;
  }

  public boolean isDeviations()
  {
    return deviations;
  }

  public void setDeviations(boolean deviations)
  {
    this.deviations = deviations;
  }

  public boolean isIntegration()
  {
    return integration;
  }

  public void setIntegration(boolean integration)
  {
    this.integration = integration;
  }

  public boolean isService()
  {
    return service;
  }

  public void setService(boolean service)
  {
    this.service = service;
  }

  public Set<String> getRootProjects()
  {
    return rootProjects;
  }

  public void setRootProjects(Set<String> rootProjects)
  {
    this.rootProjects = rootProjects;
  }

  public Set<String> getIgnoredReferences()
  {
    return ignoredReferences;
  }

  public void setIgnoredReferences(Set<String> ignoredReferences)
  {
    this.ignoredReferences = ignoredReferences;
  }

  public long getValidatorTimeStamp(IModel model)
  {
    Long result = validatorTimeStamps.get(model.getUnderlyingResource().getProjectRelativePath().toString());
    return result == null ? 0 : result;
  }

  public void setValidatorTimeStamp(IModel model, long validatorTimeStamp)
  {
    validatorTimeStamps.put(getKey(model), validatorTimeStamp);
  }

  public String getValidatorClass()
  {
    return validatorClass;
  }

  public void setValidatorClass(String validatorClass)
  {
    this.validatorClass = validatorClass;
  }

  public String getValidatorVersion()
  {
    return validatorVersion;
  }

  public void setValidatorVersion(String validatorVersion)
  {
    this.validatorVersion = validatorVersion;
  }

  public boolean isChangedSinceRelease()
  {
    return changedSinceRelease;
  }

  public void setChangedSinceRelease(boolean changedSinceRelease)
  {
    this.changedSinceRelease = changedSinceRelease;
  }

  @SuppressWarnings("unchecked")
  public Serializable getValidatorState(IModel model)
  {
    if (validatorStates == null)
    {
      if (validatorBytes != null)
      {
        validatorStates = (Map<String, Serializable>)IOUtil.deserialize(validatorBytes);
      }
      else
      {
        validatorStates = new LinkedHashMap<String, Serializable>();
      }
    }

    return validatorStates.get(getKey(model));
  }

  public void setValidatorState(IModel model, Serializable validatorState)
  {
    if (validatorStates == null)
    {
      validatorStates = new LinkedHashMap<String, Serializable>();
    }

    validatorStates.put(getKey(model), validatorState);
    validatorBytes = null;
  }

  public void clearValidatorStates()
  {
    validatorStates = null;
    validatorBytes = null;
  }

  void serializeValidatorState()
  {
    if (validatorBytes == null && validatorStates != null)
    {
      validatorBytes = IOUtil.serialize((Serializable)validatorStates);
    }
  }

  private String getKey(IModel model)
  {
    return model.getUnderlyingResource().getProjectRelativePath().toString();
  }
}
