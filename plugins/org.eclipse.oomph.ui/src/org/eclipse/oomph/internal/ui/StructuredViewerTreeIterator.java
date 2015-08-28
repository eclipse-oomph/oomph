/*
 * Copyright (c) 2015 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.emf.common.util.AbstractTreeIterator;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Ed Merks
 */
public class StructuredViewerTreeIterator extends AbstractTreeIterator<Object>
{
  private static final long serialVersionUID = 1L;

  private StructuredViewer viewer;

  private ITreeContentProvider treeContentProvider;

  public StructuredViewerTreeIterator(StructuredViewer viewer)
  {
    super(viewer, false);
    this.viewer = viewer;
  }

  @Override
  protected Iterator<Object> getChildren(Object object)
  {
    if (object == viewer)
    {
      IContentProvider contentProvider = viewer.getContentProvider();
      if (contentProvider instanceof IStructuredContentProvider)
      {
        IStructuredContentProvider structuredContentProvider = (IStructuredContentProvider)contentProvider;
        if (structuredContentProvider instanceof ITreeContentProvider)
        {
          treeContentProvider = (ITreeContentProvider)structuredContentProvider;
        }

        return Arrays.asList(structuredContentProvider.getElements(viewer.getInput())).iterator();
      }

      return Collections.emptyList().iterator();
    }

    if (treeContentProvider != null)
    {
      return Arrays.asList(treeContentProvider.getChildren(object)).iterator();
    }

    return Collections.emptyList().iterator();
  }

  public interface Provider
  {
    public StructuredViewerTreeIterator create();
  }
}
