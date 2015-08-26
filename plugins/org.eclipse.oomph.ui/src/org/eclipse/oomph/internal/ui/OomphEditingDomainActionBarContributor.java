/*
 * Copyright (c) 2015 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.emf.edit.ui.action.DeleteAction;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.texteditor.FindReplaceAction;

import java.util.ResourceBundle;

/**
 * @author Ed Merks
 */
public class OomphEditingDomainActionBarContributor extends EditingDomainActionBarContributor
{
  protected FindAction findAction;

  public OomphEditingDomainActionBarContributor()
  {
    super();
  }

  public OomphEditingDomainActionBarContributor(int style)
  {
    super(style);
  }

  @Override
  public void init(IActionBars actionBars)
  {
    super.init(actionBars);

    findAction = createFindAction();
    actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), findAction);
  }

  protected FindAction createFindAction()
  {
    return new FindAction();
  }

  @Override
  protected DeleteAction createDeleteAction()
  {
    // Specialize this so we can add the find action relative to the delete action.
    DeleteAction deleteAction = super.createDeleteAction();
    deleteAction.setId("delete");
    return deleteAction;
  }

  @Override
  public void shareGlobalActions(IPage page, IActionBars actionBars)
  {
    super.shareGlobalActions(page, actionBars);

    actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), findAction);
  }

  @Override
  public void deactivate()
  {
    super.deactivate();

    findAction.setActiveWorkbenchPart(null);
  }

  @Override
  public void activate()
  {
    super.activate();

    findAction.setActiveWorkbenchPart(activeEditor);
  }

  @Override
  public void menuAboutToShow(IMenuManager menuManager)
  {
    super.menuAboutToShow(menuManager);

    menuManager.insertAfter("delete", new ActionContributionItem(findAction));
    menuManager.insertAfter("delete", new Separator());
  }

  public static class FindAction extends Action
  {
    private IWorkbenchPart workbenchPart;

    public FindAction()
    {
      super("Find/Replace...");
    }

    @Override
    public void run()
    {
      IFindReplaceTarget adapter = workbenchPart.getAdapter(IFindReplaceTarget.class);
      if (adapter instanceof FindAndReplaceTarget)
      {
        FindAndReplaceTarget findAndReplaceTarget = (FindAndReplaceTarget)adapter;
        findAndReplaceTarget.initialize(workbenchPart);
      }

      // Reuse the platform's text editor's find and replace action.
      new FindReplaceAction(null, null, workbenchPart)
      {
        @Override
        protected void initialize(ResourceBundle bundle, String prefix)
        {
        }
      }.run();
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      this.workbenchPart = workbenchPart;
      if (workbenchPart != null)
      {
        setEnabled(workbenchPart.getAdapter(IFindReplaceTarget.class) != null);
      }
    }
  }
}
