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
package org.eclipse.oomph.version;

import org.eclipse.oomph.internal.version.ReleaseManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.pde.core.IModel;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Manages the {@link IRelease releases} that are specified by the version managed {@link IElement components} in the {@link IWorkspace workspace}.
 *
 * @author Eike Stepper
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IReleaseManager
{
  public static final IReleaseManager INSTANCE = new ReleaseManager();

  public Map<IElement, IElement> createElements(String path, boolean resolve) throws CoreException, IOException, NoSuchAlgorithmException;

  public IRelease getRelease(IFile file) throws CoreException;

  public IRelease createRelease(IFile file) throws CoreException, IOException, NoSuchAlgorithmException;

  public IElement createElement(IModel componentModel, boolean withFeatureContent, boolean resolve);

  public IModel getComponentModel(IElement element);
}
