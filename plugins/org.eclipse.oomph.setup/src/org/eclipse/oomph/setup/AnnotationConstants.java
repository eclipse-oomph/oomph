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

  public static final String ANNOTATION_INHERITED_CHOICES = "http://www.eclipse.org/oomph/setup/InheritedChoices";

  public static final String ANNOTATION_INDUCED_CHOICES = "http://www.eclipse.org/oomph/setup/InducedChoices";

  public static final String ANNOTATION_FEATURE_SUBSTITUTION = "http://www.eclipse.org/oomph/setup/FeatureSubstitution";

  public static final String ANNOTATION_PASSWORD_VERIFICATION = "http://www.eclipse.org/oomph/setup/PasswordVerification";

  public static final String KEY_INHERIT = "inherit";

  public static final String KEY_TARGET = "target";

  public static final String KEY_LABEL = "label";

  public static final String KEY_DESCRIPTION = "description";
}
