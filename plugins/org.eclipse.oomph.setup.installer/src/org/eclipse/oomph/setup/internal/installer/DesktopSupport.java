/*
 * Copyright (c) 2020 Christoph Laeubrich and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Christoph Laeubrich - initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import java.io.IOException;

/**
 * An interface for performing OS-dependent desktop tasks.
 *
 * @author Christoph Laeubrich
 */
public interface DesktopSupport
{
  /**
   * Pin the given launcher location to the task bar using the given launcher name
   */
  void pinToTaskBar(String location, String launcherName) throws IOException;

  /**
   * Creates a shortcut in the system menu for the given folder, group target, and shortcut name
   */
  void createShortCut(String specialFolder, String groupName, String target, String shortcutName) throws IOException;
}
