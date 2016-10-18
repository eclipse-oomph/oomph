/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.core;

import org.eclipse.oomph.p2.ProfileDefinition;

import org.eclipse.equinox.p2.engine.IProfile;

import java.io.File;

/**
 * @author Eike Stepper
 */
public interface Profile extends IProfile, AgentElement
{
  public static final String PROP_PROFILE_TYPE = "org.eclipse.oomph.p2.profile.type"; //$NON-NLS-1$

  public static final String PROP_PROFILE_DEFINITION = "org.eclipse.oomph.p2.profile.definition"; //$NON-NLS-1$

  public static final String PROP_PROFILE_REFERENCER = "org.eclipse.oomph.p2.profile.referencer"; //$NON-NLS-1$

  public static final String PROP_PROFILE_SHARED_POOL = "org.eclipse.oomph.p2.profile.shared.pool"; //$NON-NLS-1$

  public static final String PROP_IU_COMPATIBILITY = "org.eclipse.oomph.p2.iu.compatibility"; //$NON-NLS-1$

  public static final String TYPE_INSTALLATION = "Installation";

  public static final String TYPE_TARGLET = "Targlet";

  public boolean isSelfHosting();

  public String getType();

  public File getInstallFolder();

  public BundlePool getBundlePool();

  public ProfileDefinition getDefinition();

  public ProfileTransaction change();
}
