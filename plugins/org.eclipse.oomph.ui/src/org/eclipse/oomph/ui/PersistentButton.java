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
  private final Persistence persistence;

  public PersistentButton(Composite parent, int style, boolean defaultSelection, Persistence persistence)
  {
    super(parent, style);
    this.persistence = persistence;
    super.setSelection(persistence != null ? persistence.load(defaultSelection) : defaultSelection);
  }

  @Override
  public void setSelection(boolean selection)
  {
    super.setSelection(selection);

    if (persistence != null)
    {
      persistence.save(selection);
    }
  }

  @Override
  protected void checkSubclass()
  {
  }

  /**
   * @author Eike Stepper
   */
  public static abstract class Persistence
  {
    protected abstract boolean load(boolean defaultSelection);

    protected abstract void save(boolean selection);
  }

  /**
   * @author Eike Stepper
   */
  public static final class DialogSettingsPersistence extends Persistence
  {
    private final IDialogSettings dialogSettings;

    private final String key;

    public DialogSettingsPersistence(IDialogSettings dialogSettings, String key)
    {
      this.dialogSettings = dialogSettings;
      this.key = key;
    }

    public IDialogSettings getDialogSettings()
    {
      return dialogSettings;
    }

    public String getKey()
    {
      return key;
    }

    @Override
    protected boolean load(boolean defaultSelection)
    {
      String value = dialogSettings.get(key);
      if (value != null)
      {
        return Boolean.parseBoolean(value);
      }

      return defaultSelection;
    }

    @Override
    protected void save(boolean selection)
    {
      dialogSettings.put(key, selection);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class ToggleCommandPersistence extends Persistence
  {
    private final String commandID;

    public ToggleCommandPersistence(String commandID)
    {
      this.commandID = commandID;
    }

    public String getCommandID()
    {
      return commandID;
    }

    @Override
    protected boolean load(boolean defaultSelection)
    {
      return ToggleCommandHandler.getToggleState(commandID);
    }

    @Override
    protected void save(boolean selection)
    {
      ToggleCommandHandler.setToggleState(commandID, selection);
    }
  }
}
