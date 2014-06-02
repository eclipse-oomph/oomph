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
package org.eclipse.oomph.ui;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Eike Stepper
 */
public class PersistentButton extends Button
{
  private final IDialogSettings settings;

  private final String key;

  private boolean loaded;

  public PersistentButton(Composite parent, int style, IDialogSettings settings, String key, boolean defaultSelection)
  {
    super(parent, style);
    this.settings = settings;
    this.key = key;

    String value = settings.get(key);
    if (value != null)
    {
      setSelection(Boolean.parseBoolean(value));
    }
    else
    {
      setSelection(defaultSelection);
    }

    loaded = true;
  }

  @Override
  public void setSelection(boolean selected)
  {
    super.setSelection(selected);
    if (loaded)
    {
      settings.put(key, selected);
    }
  }

  @Override
  protected void checkSubclass()
  {
  }
}
