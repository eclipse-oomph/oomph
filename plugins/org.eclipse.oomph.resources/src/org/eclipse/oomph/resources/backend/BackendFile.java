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
package org.eclipse.oomph.resources.backend;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Eike Stepper
 */
public final class BackendFile extends BackendResource
{
  BackendFile(BackendSystem system, URI systemRelativeURI)
  {
    super(system, systemRelativeURI);
  }

  @Override
  public Type getResourceType()
  {
    return Type.FILE;
  }

  public InputStream getContents(IProgressMonitor monitor) throws IOException, BackendException, OperationCanceledException
  {
    if (monitor == null)
    {
      monitor = new NullProgressMonitor();
    }

    try
    {
      BackendSystem system = getSystem();
      return system.getContents(this, monitor);
    }
    catch (IOException ex)
    {
      throw ex;
    }
    catch (OperationCanceledException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new BackendException(ex);
    }
  }

  @Override
  void doAccept(Visitor visitor, IProgressMonitor monitor) throws BackendException, OperationCanceledException
  {
    visitor.visit(this);
  }
}
