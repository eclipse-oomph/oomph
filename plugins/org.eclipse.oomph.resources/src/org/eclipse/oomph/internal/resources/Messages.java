/*
 * Copyright (c) 2024 merks and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    merks - initial API and implementation
 */
package org.eclipse.oomph.internal.resources;

import org.eclipse.osgi.util.NLS;

/**
 * @author merks
 */
public class Messages extends NLS
{
  private static final String BUNDLE_NAME = Messages.class.getPackageName() + ".messages"; //$NON-NLS-1$

  public static String FileExtractMatchingPatternResolver_InvalidBlankPattern_exception;

  public static String FileExtractMatchingPatternResolver_InvalidCharset_exception;

  public static String FileExtractMatchingPatternResolver_InvalidPath_exception;

  public static String FileExtractMatchingPatternResolver_InvalidPattern_exception;

  public static String FileExtractMatchingPatternResolver_InvalidRead_excpetion;

  public static String FileExtractMatchingPatternResolver_MissingArgument_exception;

  public static String FileExtractMatchingPatternResolver_UnspecifedArguments_exception;
  static
  {
    // initialize resource bundle
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
