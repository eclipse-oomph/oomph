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
package org.eclipse.oomph.p2.internal.core;

import java.io.File;

/**
 * @author Eike Stepper
 */
@Deprecated
public interface ProfileReferencer
{
  public File getFile();

  public boolean isDirectory();

  public boolean isReferenced(String profileID);

  public void reference(String profileID);

  public void unreference(String profileID);
}
