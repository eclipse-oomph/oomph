/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.core;

import java.util.Collection;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public interface ProfileContainer
{
  public Set<String> getProfileIDs();

  public Collection<Profile> getProfiles();

  public Profile getProfile(String id);

  public ProfileCreator addProfile(String id, String type);
}
