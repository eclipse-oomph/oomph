/*
 * Copyright (c) 2016 Manumitting Technologies Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Manumitting Technologies Inc - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.actions;

import org.eclipse.oomph.setup.ui.Questionnaire;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * Launch the Setup Questionnaire, if found.
 *
 * @author Brian de Alwis
 */
public class LaunchQuestionnaireHandler extends AbstractHandler
{
  public Object execute(ExecutionEvent event) throws ExecutionException
  {
    if (!Questionnaire.exists())
    {
      throw new ExecutionException("No questionnaire found"); //$NON-NLS-1$
    }

    Shell parentShell = HandlerUtil.getActiveShellChecked(event);
    Questionnaire.perform(parentShell, true);

    return null;
  }
}
