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
package org.eclipse.oomph.base;

/**
 * @author Ed Merks
 */
public final class BaseAnnotationConstants
{
  public static final String ANNOTATION_SOURCE = "http://www.eclipse.org/oomph/Migrator";

  private BaseAnnotationConstants()
  {
  }

  public static final String ANNOTATION_ERROR = "http://www.eclipse.org/oomph/base/Error";

  public static final String ANNOTATION_WARNING = "http://www.eclipse.org/oomph/base/Warning";

  public static final String ANNOTATION_INFO = "http://www.eclipse.org/oomph/base/Info";

  public static final String KEY_DIAGNOSTIC = "diagnostic";

  public static final String ANNOTATION_CONVERSION = "http://www.eclipse.org/oomph/base/Conversion";

  public static final String KEY_ECLASS = "eClass";
}
