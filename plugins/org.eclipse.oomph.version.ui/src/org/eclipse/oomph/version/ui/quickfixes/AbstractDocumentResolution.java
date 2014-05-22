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
package org.eclipse.oomph.version.ui.quickfixes;

import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;

/**
 * @author Eike Stepper
 */
public abstract class AbstractDocumentResolution extends AbstractResolution
{
  public AbstractDocumentResolution(IMarker marker, String label, String imageKey)
  {
    super(marker, label, imageKey);
  }

  @Override
  protected final void apply(IMarker marker) throws Exception
  {
    IPath fullPath = ((IFile)marker.getResource()).getFullPath();
    ITextFileBufferManager.DEFAULT.connect(fullPath, LocationKind.IFILE, new NullProgressMonitor());

    try
    {
      ITextFileBuffer buffer = ITextFileBufferManager.DEFAULT.getTextFileBuffer(fullPath, LocationKind.IFILE);
      boolean wasDirty = buffer.isDirty();

      IDocument document = buffer.getDocument();
      if (apply(marker, document))
      {
        if (!wasDirty && !buffer.isShared())
        {
          buffer.commit(new NullProgressMonitor(), true);
        }
      }
    }
    finally
    {
      ITextFileBufferManager.DEFAULT.disconnect(fullPath, LocationKind.IFILE, new NullProgressMonitor());
    }
  }

  protected abstract boolean apply(IMarker marker, IDocument document) throws Exception;
}
