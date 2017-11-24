/*
 * Copyright (c) 2017 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.core;

/**
 * @author Ed Merks
 */
public interface ParameterizedStringFilter extends StringFilter
{
  public String filter(String value, String argument);
}
