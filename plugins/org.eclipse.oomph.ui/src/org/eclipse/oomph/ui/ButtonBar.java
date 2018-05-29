/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.oomph.util.StringUtil;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Eike Stepper
 */
public class ButtonBar
{
  private static final String TOGGLE_COMMAND_PREFIX = "toggleCommand:";

  private final Composite parent;

  private final Control previousChild;

  private Composite composite;

  public ButtonBar(Composite parent)
  {
    this.parent = parent;

    Control[] children = parent.getChildren();
    if (children.length == 0)
    {
      previousChild = null;
    }
    else
    {
      previousChild = children[children.length - 1];
    }
  }

  public final Composite getComposite()
  {
    return composite;
  }

  public final Button addCheckButton(String text, String toolTip, boolean defaultSelection, String persistenceKey)
  {
    if (composite == null)
    {
      composite = new Composite(parent, SWT.NONE);
      composite.setLayout(UIUtil.createGridLayout(1));
      composite.setLayoutData(createLayoutData());

      if (previousChild != null && !previousChild.isDisposed())
      {
        composite.moveBelow(previousChild);
      }
    }
    else
    {
      GridLayout checkLayout = (GridLayout)composite.getLayout();
      ++checkLayout.numColumns;
    }

    Button button;
    if (persistenceKey != null)
    {
      boolean toggleCommand = false;
      if (persistenceKey.startsWith(TOGGLE_COMMAND_PREFIX))
      {
        persistenceKey = persistenceKey.substring(TOGGLE_COMMAND_PREFIX.length());
        toggleCommand = UIUtil.WORKBENCH != null;
      }

      PersistentButton.Persistence persistence = null;

      if (toggleCommand)
      {
        persistence = new PersistentButton.ToggleCommandPersistence(persistenceKey);
      }
      else
      {
        IDialogSettings dialogSettings = getDialogSettings();
        if (dialogSettings != null)
        {
          persistence = new PersistentButton.DialogSettingsPersistence(dialogSettings, persistenceKey);
        }
      }

      button = PersistentButton.create(composite, SWT.CHECK, defaultSelection, persistence);
    }
    else
    {
      button = new Button(composite, SWT.CHECK);
      button.setSelection(defaultSelection);
    }

    button.setLayoutData(createLayoutData());
    button.setText(text);
    if (!StringUtil.isEmpty(toolTip))
    {
      button.setToolTipText(toolTip);
    }

    return button;
  }

  protected GridData createLayoutData()
  {
    return new GridData();
  }

  protected IDialogSettings getDialogSettings()
  {
    return null;
  }
}
