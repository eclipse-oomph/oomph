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
package org.eclipse.oomph.setup;

/**
 * @author Eike Stepper
 */
public final class AnnotationConstants
{
  private AnnotationConstants()
  {
  }

  public static final String ANNOTATION_VALID_TRIGGERS = "http://www.eclipse.org/oomph/setup/ValidTriggers";

  public static final String KEY_TRIGGERS = "triggers";

  public static final String ANNOTATION_ENABLEMENT = "http://www.eclipse.org/oomph/setup/Enablement";

  public static final String KEY_VARIABLE_NAME = "variableName";

  public static final String KEY_REPOSITORY = "repository";

  public static final String KEY_INSTALLABLE_UNITS = "installableUnits";
}
