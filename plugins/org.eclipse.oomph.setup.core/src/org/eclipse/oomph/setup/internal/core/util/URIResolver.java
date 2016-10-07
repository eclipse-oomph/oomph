/*
 * Copyright (c) 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.core.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIHandler;

/**
 * An interface implements by {@link URIHandler uri handlers} that can resolve the URIs they handle.
 *
 * @author Ed Merks
 */
public interface URIResolver
{
  public URI resolve(URI uri);
}
