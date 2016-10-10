/*
 * Copyright (c) 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.internal.ide;

import org.eclipse.core.variables.IStringVariable;
import org.eclipse.debug.ui.stringsubstitution.IArgumentSelector;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * @author Eike Stepper
 */
public class ActiveMylynTaskArgumentSelector implements IArgumentSelector
{
  private static final String[] ATTRIBUTES = { "Id", "Key", "Url", "Kind", "Owner", "OwnerId", "Priority", "RepositoryUrl", "Summary", "ConnectorKind",
      "HandleIdentifier", "CreationDate", "CreationTime", "CompletionDate", "CompletionTime", "DueDate", "DueTime", "ModificationDate", "ModificationTime" };

  public ActiveMylynTaskArgumentSelector()
  {
  }

  public String selectArgument(IStringVariable variable, Shell shell)
  {
    ILabelProvider labelProvider = new LabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        String text = (String)element;
        if ("Id".equalsIgnoreCase(text))
        {
          text += "  (default)";
        }
        else
        {
          text = text.replaceAll("([a-z])([A-Z])", "$1 $2");
          text = text.replaceAll("Date", "Date  (yyyyMMdd_HHmmss)");
          text = text.replaceAll("Time", "Time  (milliseconds since Unix epoch)");
          text = text.replaceAll("Summary", "Summary  (escaped)");
        }

        return text;
      }
    };

    ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
    dialog.setElements(ATTRIBUTES);
    dialog.setTitle("Select Task Attribute");
    dialog.setMessage("Select a task attribute (? = any character, * = any String):");
    if (dialog.open() == Window.OK)
    {
      return (String)dialog.getResult()[0];
    }

    return null;
  }
}
