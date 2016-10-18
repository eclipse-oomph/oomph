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
package org.eclipse.oomph.version;

import org.eclipse.core.resources.IProject;
import org.eclipse.pde.core.IModel;

import java.io.Serializable;
import java.util.Map;

/**
 * Provides access to those parts of a {@link IProject project}'s build state that {@link VersionValidator version validators} need to access.
 *
 * @author Eike Stepper
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IBuildState
{
  public long getValidatorTimeStamp(IModel model);

  public void setValidatorTimeStamp(IModel model, long validatorTimeStamp);

  public Serializable getValidatorState(IModel model);

  public void setValidatorState(IModel model, Serializable validatorState);

  public void clearValidatorStates();

  public void setChangedSinceRelease(boolean changedSinceRelease);

  public Map<String, String> getArguments();

  public void setArguments(Map<String, String> arguments);
}
