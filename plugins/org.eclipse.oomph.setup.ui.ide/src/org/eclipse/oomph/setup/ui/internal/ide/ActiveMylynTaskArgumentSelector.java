/*
 * Copyright (c) 2016, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.internal.ide;

import org.eclipse.core.variables.IStringVariable;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * @author Eike Stepper
 */
public class ActiveMylynTaskArgumentSelector extends AbstractArgumentSelector
{
  @SuppressWarnings("nls")
  private static final String[] ATTRIBUTES = { "Id", "Key", "Url", "Kind", "Owner", "OwnerId", "Priority", "RepositoryUrl", "Summary", "ConnectorKind",
      "HandleIdentifier", "CreationDate", "CreationTime", "CompletionDate", "CompletionTime", "DueDate", "DueTime", "ModificationDate", "ModificationTime" };

  @Override
  public String selectArgument(IStringVariable variable, Shell shell)
  {
    ILabelProvider labelProvider = new LabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        String text = (String)element;
        if ("Id".equalsIgnoreCase(text)) //$NON-NLS-1$
        {
          text += "  " + Messages.ActiveMylynTaskArgumentSelector_elementDialog_defaultElement; //$NON-NLS-1$
        }
        else
        {
          text = text.replaceAll("([a-z])([A-Z])", "$1 $2"); //$NON-NLS-1$ //$NON-NLS-2$
          text = text.replaceAll("Date", Messages.ActiveMylynTaskArgumentSelector_elementDialog_date); //$NON-NLS-1$
          text = text.replaceAll("Time", Messages.ActiveMylynTaskArgumentSelector_elementDialog_time); //$NON-NLS-1$
          text = text.replaceAll("Summary", Messages.ActiveMylynTaskArgumentSelector_elementDialog_summary); //$NON-NLS-1$
        }

        return text;
      }
    };

    ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
    dialog.setElements(ATTRIBUTES);
    dialog.setTitle(Messages.ActiveMylynTaskArgumentSelector_elementDialog_title);
    dialog.setMessage(Messages.ActiveMylynTaskArgumentSelector_elementDialog_message);
    if (dialog.open() == Window.OK)
    {
      return (String)dialog.getResult()[0];
    }

    return null;
  }

  /**
   * @author Ed Merks
   */
  public static class ExtensionFactory extends AbstractExtensionFactory
  {
    @Override
    protected AbstractArgumentSelector createSelector()
    {
      return new ActiveMylynTaskArgumentSelector();
    }
  }
}
