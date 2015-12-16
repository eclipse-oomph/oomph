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
package org.eclipse.oomph.p2.internal.core;

import java.net.URI;
import java.util.EventObject;

/**
 * @author Ed Merks
 */
public final class DownloadArtifactEvent extends EventObject
{
  private static final long serialVersionUID = 1L;

  private final URI artifactURI;

  private final boolean completed;

  public DownloadArtifactEvent(URI artifactURI, boolean completed)
  {
    super(artifactURI);
    this.artifactURI = artifactURI;
    this.completed = completed;
  }

  public URI getArtifactURI()
  {
    return artifactURI;
  }

  public boolean isCompleted()
  {
    return completed;
  }
}
