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

import java.io.File;
import java.io.IOException;

/**
 * An interface for performing OS-dependent desktop tasks.
 *
 * @author Christoph Laeubrich
 */
public interface DesktopSupport
{
  /**
   * The types of supported shortcuts.
   *
   * @author Christoph Laeubrich
   */
  enum ShortcutType
  {
    DESKTOP, START_MENU, TASKBAR;
  }

  /**
   * Pin the given launcher location to the task bar using the given launcher name.
   */
  void pinToTaskBar(String location, String launcherName) throws IOException;

  /**
   * Creates a shortcut in the system menu for the given folder, group target, and shortcut name.
   *
   * @return <code>true</code> if creation was performed, <code>false</code> otherwise.
   * @throws IOException if an unrecoverable error occurred that prevents the creation of the shortcut.
   */
  boolean createShortCut(ShortcutType type, String groupName, File executable, String shortcutName, String description, String id) throws IOException;
}
