/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation.handlers;

import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.sync.DataProvider.NotCurrentException;
import org.eclipse.oomph.setup.internal.sync.Synchronization;
import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;
import org.eclipse.oomph.setup.sync.SyncAction;
import org.eclipse.oomph.setup.sync.SyncActionType;
import org.eclipse.oomph.setup.sync.SyncPolicy;
import org.eclipse.oomph.setup.ui.SetupEditorSupport;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class PerformHandler extends AbstractDropdownItemHandler
{
  protected boolean manual;

  public PerformHandler()
  {
    super("update", "Perform Setup Tasks...");
    manual = true;
  }

  public void run()
  {
    final Synchronization synchronization = SynchronizerManager.INSTANCE.synchronize();
    if (synchronization != null)
    {
      try
      {
        EMap<String, SyncPolicy> policies = synchronization.getRemotePolicies();
        Map<String, SyncAction> actions = synchronization.getActions();

        for (Iterator<Map.Entry<String, SyncAction>> it = actions.entrySet().iterator(); it.hasNext();)
        {
          Map.Entry<String, SyncAction> entry = it.next();
          String syncID = entry.getKey();
          SyncAction syncAction = entry.getValue();

          SyncPolicy policy = policies.get(syncID);
          if (policy == null || policy == SyncPolicy.EXCLUDE)
          {
            it.remove();
            continue;
          }

          SyncActionType type = syncAction.getComputedType();
          switch (type)
          {
            case SET_LOCAL: // Ignore LOCAL -> REMOTE actions.
            case REMOVE_LOCAL: // Ignore LOCAL -> REMOTE actions.
            case CONFLICT: // Ignore interactive actions.
            case EXCLUDE: // Should not occur.
            case NONE: // Should not occur.
              it.remove();
              continue;
          }
        }

        if (!actions.isEmpty())
        {
          try
          {
            synchronization.commit();
          }
          catch (NotCurrentException ex)
          {
            SetupEditorPlugin.INSTANCE.log(ex, IStatus.WARNING);
          }
          catch (IOException ex)
          {
            SetupEditorPlugin.INSTANCE.log(ex, IStatus.WARNING);
          }

          IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
          if (page != null)
          {
            IEditorPart editor = SetupEditorSupport.getEditor(page, SetupContext.USER_SETUP_URI, false);
            if (editor != null)
            {
              // editor.
            }
          }

        }
      }
      finally
      {
        synchronization.dispose();
      }
    }

    final SetupWizard updater = new SetupWizard.Updater(manual)
    {
      @Override
      public void createPageControls(Composite pageContainer)
      {
        loadIndex();
        super.createPageControls(pageContainer);
      }
    };

    Shell shell = UIUtil.getShell();
    updater.openDialog(shell);
  }

  /**
   * @author Eike Stepper
   */
  public static final class StartupPerformHandler extends PerformHandler
  {
    public StartupPerformHandler()
    {
      manual = false;
    }
  }
}
