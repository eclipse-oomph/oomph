/*
 * Copyright (c) 2015, 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.internal.ui;

import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.edit.ui.action.DeleteAction;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
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

  protected CollapseAllAction collapseAllAction;

  private RevertAction revertAction;

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
    findAction = createFindAction();
    if (findAction != null)
    {
      actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), findAction);
    }

    collapseAllAction = createCollapseAllAction();

    revertAction = createRevertAction();
    if (revertAction != null)
    {
      actionBars.setGlobalActionHandler(ActionFactory.REVERT.getId(), revertAction);
    }

    super.init(actionBars);
  }

  protected FindAction createFindAction()
  {
    return new FindAction();
  }

  private CollapseAllAction createCollapseAllAction()
  {
    return new CollapseAllAction();
  }

  private RevertAction createRevertAction()
  {
    return new RevertAction();
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
  public void contributeToToolBar(IToolBarManager toolBarManager)
  {
    super.contributeToToolBar(toolBarManager);

    if (collapseAllAction != null)
    {
      toolBarManager.add(collapseAllAction);
    }
  }

  @Override
  public void shareGlobalActions(IPage page, IActionBars actionBars)
  {
    super.shareGlobalActions(page, actionBars);

    if (findAction != null)
    {
      actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), findAction);
    }
  }

  @Override
  public void deactivate()
  {
    super.deactivate();

    if (findAction != null)
    {
      findAction.setActiveWorkbenchPart(null);
    }

    if (collapseAllAction != null)
    {
      collapseAllAction.setActiveWorkbenchPart(null);
    }

    if (revertAction != null)
    {
      revertAction.setActiveWorkbenchPart(null);
    }
  }

  @Override
  public void activate()
  {
    super.activate();

    if (findAction != null)
    {
      findAction.setActiveWorkbenchPart(activeEditor);
    }

    if (collapseAllAction != null)
    {
      collapseAllAction.setActiveWorkbenchPart(activeEditor);
    }

    if (revertAction != null)
    {
      revertAction.setActiveWorkbenchPart(activeEditor);
    }
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
      super("Find/Replace...", UIPlugin.INSTANCE.getImageDescriptor("search"));
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

  public static final class CollapseAllAction extends Action
  {
    private IViewerProvider viewerProvider;

    public CollapseAllAction()
    {
      super("Collapse All", IAction.AS_PUSH_BUTTON);
      setImageDescriptor(UIPlugin.INSTANCE.getImageDescriptor("collapse-all"));
      setToolTipText("Collapse all expanded elements");
    }

    @Override
    public void run()
    {
      Viewer viewer = viewerProvider.getViewer();
      if (viewer instanceof TreeViewer)
      {
        TreeViewer treeViewer = (TreeViewer)viewer;
        treeViewer.collapseAll();
      }
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      if (workbenchPart instanceof IViewerProvider)
      {
        viewerProvider = (IViewerProvider)workbenchPart;
        setEnabled(true);
      }
      else
      {
        setEnabled(false);
        viewerProvider = null;
      }
    }
  }

  private static final class RevertAction extends Action
  {
    private IRevertablePart revertableEditor;

    @Override
    public void run()
    {
      revertableEditor.doRevert();
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      if (workbenchPart instanceof IRevertablePart)
      {
        revertableEditor = (IRevertablePart)workbenchPart;
        setEnabled(true);
      }
      else
      {
        setEnabled(false);
        revertableEditor = null;
      }
    }
  }
}
