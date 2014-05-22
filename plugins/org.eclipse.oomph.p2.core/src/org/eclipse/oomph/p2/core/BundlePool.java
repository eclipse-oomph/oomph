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
package org.eclipse.oomph.p2.core;

import org.eclipse.equinox.p2.repository.artifact.IFileArtifactRepository;

import java.util.Set;

/**
 * @author Eike Stepper
 */
public interface BundlePool extends AgentElement, ProfileContainer
{
  public IFileArtifactRepository getFileArtifactRepository();

  public Set<String> getClients();
}
