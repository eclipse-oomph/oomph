/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.version;

import org.eclipse.core.resources.IFile;

import java.util.Map;

/**
 * Represents the released state of a set of {@link IElement components}.
 *
 * @author Eike Stepper
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IRelease extends IElementResolver
{
  public IFile getFile();

  public byte[] getDigest();

  public Map<IElement, IElement> getElements();

  public int getSize();
}
