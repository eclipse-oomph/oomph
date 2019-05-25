/*
 * Copyright (c) 2019 Ed Merks (Berlin) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.edit;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Ed Merks
 */
public class NoChildrenDelegatingWrapperItemProvider extends DelegatingWrapperItemProvider
{
  public NoChildrenDelegatingWrapperItemProvider(Object value, Object owner, EStructuralFeature feature, int index, AdapterFactory adapterFactory)
  {
    super(value, owner, feature, index, adapterFactory);
  }

  public NoChildrenDelegatingWrapperItemProvider(Object value, Object owner, AdapterFactory adapterFactory)
  {
    this(value, owner, null, CommandParameter.NO_INDEX, adapterFactory);
  }

  @Override
  public boolean hasChildren(Object object)
  {
    return false;
  }

  @Override
  protected void updateChildren()
  {
    // Don't show children.
  }

  @Override
  public Collection<?> getChildren(Object object)
  {
    return Collections.emptyList();
  }
}
