/*
 * Copyright (c) 2024 Eclipse contributors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.provider;

import org.eclipse.emf.ecore.EReference;

public interface ReferenceGroup
{
  EReference getFeature();
}
