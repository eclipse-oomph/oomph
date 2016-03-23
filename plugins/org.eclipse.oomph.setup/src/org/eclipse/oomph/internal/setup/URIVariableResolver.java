/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.setup;

import org.eclipse.emf.common.util.URI;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

/**
 * @author Ed Merks
 */
public class URIVariableResolver implements IDynamicVariableResolver
{
  public String resolveValue(IDynamicVariable variable, String file) throws CoreException
  {
    return file == null ? null : URI.createFileURI(file).toString();
  }
}
