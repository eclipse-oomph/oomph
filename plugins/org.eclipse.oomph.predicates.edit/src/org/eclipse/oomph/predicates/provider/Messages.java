/*
 * Copyright (c) 2020 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.predicates.provider;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS
{
  private static final String BUNDLE_NAME = "org.eclipse.oomph.predicates.provider.messages"; //$NON-NLS-1$

  public static String AndPredicateItemProvider_And_label;

  public static String BuilderPredicateItemProvider_HasBuild_label;

  public static String CommentPredicateItemProvider_CommentLike_label;

  public static String FilePredicateItemProvider_ContentLike_label;

  public static String FilePredicateItemProvider_FileAt_label;

  public static String ImportedPredicateItemProvider_AndAccessible_label;

  public static String ImportedPredicateItemProvider_Imported_label;

  public static String LocationPredicateItemProvider_LocationLike_label;

  public static String NamePredicateItemProvider_NameMatches_label;

  public static String NaturePredicateItemProvider_HasNature_label;

  public static String NotPredicateItemProvider_Not_label;

  public static String OrPredicateItemProvider_Or_label;

  public static String RepositoryPredicateItemProvider_relativePathLike;

  public static String RepositoryPredicateItemProvider_SameRepository_label;

  static
  {
    NLS.initializeMessages(BUNDLE_NAME, Messages.class);
  }

  private Messages()
  {
  }
}
