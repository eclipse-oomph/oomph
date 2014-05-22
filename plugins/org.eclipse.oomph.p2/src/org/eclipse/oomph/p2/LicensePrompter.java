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
package org.eclipse.oomph.p2;

import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.ILicense;

import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public interface LicensePrompter
{
  public static final LicensePrompter DECLINE = new LicensePrompter()
  {
    public LicenseConfirmation promptLicenses(Map<ILicense, List<IInstallableUnit>> licensesToIUs)
    {
      return LicenseConfirmation.DECLINE;
    }
  };

  public LicenseConfirmation promptLicenses(Map<ILicense, List<IInstallableUnit>> licensesToIUs);
}
