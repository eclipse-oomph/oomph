/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.projectconfig.presentation;

import org.eclipse.oomph.predicates.RepositoryPredicate;
import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.presentation.actions.ShowInExplorerAction;
import org.eclipse.oomph.projectconfig.Project;
import org.eclipse.oomph.projectconfig.WorkspaceConfiguration;

import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.ui.action.ControlAction;
import org.eclipse.emf.edit.ui.action.CreateChildAction;
import org.eclipse.emf.edit.ui.action.CreateSiblingAction;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;
import org.eclipse.emf.edit.ui.action.LoadResourceAction;
import org.eclipse.emf.edit.ui.action.ValidateAction;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This is the action bar contributor for the ProjectConfig model editor.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ProjectConfigActionBarContributor extends EditingDomainActionBarContributor implements ISelectionChangedListener
{
  private static final IWorkspaceRoot WORKSPACE_ROOT = ResourcesPlugin.getWorkspace().getRoot();

  /**
   * This keeps track of the active editor.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IEditorPart activeEditorPart;

  /**
   * This keeps track of the current selection provider.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ISelectionProvider selectionProvider;

  /**
   * This action opens the Properties view.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IAction showPropertiesViewAction = new Action(ProjectConfigEditorPlugin.INSTANCE.getString("_UI_ShowPropertiesView_menu_item")) //$NON-NLS-1$
  {
    @Override
    public void run()
    {
      try
      {
        getPage().showView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
      }
      catch (PartInitException exception)
      {
        ProjectConfigEditorPlugin.INSTANCE.log(exception);
      }
    }
  };

  /**
   * This action refreshes the viewer of the current editor if the editor
   * implements {@link org.eclipse.emf.common.ui.viewer.IViewerProvider}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IAction refreshViewerAction = new Action(ProjectConfigEditorPlugin.INSTANCE.getString("_UI_RefreshViewer_menu_item")) //$NON-NLS-1$
  {
    @Override
    public boolean isEnabled()
    {
      return activeEditorPart instanceof IViewerProvider;
    }

    @Override
    public void run()
    {
      if (activeEditorPart instanceof IViewerProvider)
      {
        Viewer viewer = ((IViewerProvider)activeEditorPart).getViewer();
        if (viewer != null)
        {
          viewer.refresh();
        }
      }
    }
  };

  protected IAction updatePreferenceProfileReferencesAction = new Action(Messages.ProjectConfigActionBarContributor_UpdateReferences_label)
  {
    @Override
    public boolean isEnabled()
    {
      return activeEditorPart instanceof IEditingDomainProvider;
    }

    @Override
    public void run()
    {
      if (activeEditorPart instanceof IEditingDomainProvider)
      {
        EditingDomain editingDomain = ((IEditingDomainProvider)activeEditorPart).getEditingDomain();
        EList<Resource> resources = editingDomain.getResourceSet().getResources();
        if (!resources.isEmpty())
        {
          final WorkspaceConfiguration workspaceConfiguration = (WorkspaceConfiguration)resources.get(0).getContents().get(0);

          editingDomain.getCommandStack().execute(new ChangeCommand(workspaceConfiguration)
          {
            @Override
            public String getDescription()
            {
              return Messages.ProjectConfigActionBarContributor_UpdateReferences_description;
            }

            @Override
            public String getLabel()
            {
              return Messages.ProjectConfigActionBarContributor_UpdateReferencesShort_label;
            }

            @Override
            protected void doExecute()
            {
              workspaceConfiguration.updatePreferenceProfileReferences();
            }
          });
        }
      }
    }
  };

  protected IAction applyPreferenceProfilesAction = new Action(Messages.ProjectConfigActionBarContributor_ApplyPReferenceProfile_label)
  {
    @Override
    public boolean isEnabled()
    {
      return activeEditorPart instanceof IEditingDomainProvider;
    }

    @Override
    public void run()
    {
      if (activeEditorPart instanceof IEditingDomainProvider)
      {
        EditingDomain editingDomain = ((IEditingDomainProvider)activeEditorPart).getEditingDomain();
        EList<Resource> resources = editingDomain.getResourceSet().getResources();
        if (!resources.isEmpty())
        {
          if (activeEditorPart.isDirty())
          {
            boolean confirmation = MessageDialog.openQuestion(null, Messages.ProjectConfigActionBarContributor_Save_title,
                Messages.ProjectConfigActionBarContributor_SaveConfiguration_description);
            if (!confirmation)
            {
              return;
            }
          }
          else
          {
            boolean confirmation = MessageDialog.openQuestion(null, Messages.ProjectConfigActionBarContributor_Save_title,
                Messages.ProjectConfigActionBarContributor_SaveReferences_description);
            if (!confirmation)
            {
              return;
            }
          }

          final WorkspaceConfiguration workspaceConfiguration = (WorkspaceConfiguration)resources.get(0).getContents().get(0);
          editingDomain.getCommandStack().execute(new ChangeCommand(workspaceConfiguration)
          {
            @Override
            public String getDescription()
            {
              return Messages.ProjectConfigActionBarContributor_ApplyReferenceProfiles_description;
            }

            @Override
            public String getLabel()
            {
              return Messages.ProjectConfigActionBarContributor_UpdatePreferenceProfiles_label;
            }

            @Override
            protected void doExecute()
            {
              workspaceConfiguration.updatePreferenceProfileReferences();
            }
          });

          activeEditor.doSave(new NullProgressMonitor());
          workspaceConfiguration.applyPreferenceProfiles();
        }
      }
    }
  };

  protected IAction reloadEditorAction = new Action(Messages.ProjectConfigActionBarContributor_ReloadEditor_label)
  {
    @Override
    public boolean isEnabled()
    {
      return activeEditorPart instanceof ProjectConfigEditor;
    }

    @Override
    public void run()
    {
      if (activeEditorPart instanceof ProjectConfigEditor)
      {
        ProjectConfigEditor projectConfigEditor = (ProjectConfigEditor)activeEditorPart;
        projectConfigEditor.reload();
      }
    }
  };

  /**
   * This will contain one {@link org.eclipse.emf.edit.ui.action.CreateChildAction} corresponding to each descriptor
   * generated for the current selection by the item provider.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<IAction> createChildActions;

  /**
   * This is the menu manager into which menu contribution items should be added for CreateChild actions.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IMenuManager createChildMenuManager;

  /**
   * This will contain one {@link org.eclipse.emf.edit.ui.action.CreateSiblingAction} corresponding to each descriptor
   * generated for the current selection by the item provider.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<IAction> createSiblingActions;

  /**
   * This is the menu manager into which menu contribution items should be added for CreateSibling actions.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IMenuManager createSiblingMenuManager;

  /**
   * This creates an instance of the contributor.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProjectConfigActionBarContributor()
  {
    super(ADDITIONS_LAST_STYLE);
    loadResourceAction = new LoadResourceAction();
    validateAction = new ValidateAction();
    liveValidationAction = new DiagnosticDecorator.LiveValidator.LiveValidationAction(ProjectConfigEditorPlugin.getPlugin().getDialogSettings());
    controlAction = new ControlAction();
  }

  /**
   * This adds Separators for editor additions to the tool bar.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void contributeToToolBar(IToolBarManager toolBarManager)
  {
    super.contributeToToolBar(toolBarManager);
    toolBarManager.add(new Separator("projectconfig-settings")); //$NON-NLS-1$
    toolBarManager.add(new Separator("projectconfig-additions")); //$NON-NLS-1$
  }

  /**
   * This adds to the menu bar a menu and some separators for editor additions,
   * as well as the sub-menus for object creation items.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void contributeToMenu(IMenuManager menuManager)
  {
    super.contributeToMenu(menuManager);

    IMenuManager submenuManager = new MenuManager(ProjectConfigEditorPlugin.INSTANCE.getString("_UI_ProjectConfigEditor_menu"), //$NON-NLS-1$
        "org.eclipse.oomph.projectconfigMenuID"); //$NON-NLS-1$
    menuManager.insertAfter("additions", submenuManager); //$NON-NLS-1$
    submenuManager.add(new Separator("settings")); //$NON-NLS-1$
    submenuManager.add(new Separator("actions")); //$NON-NLS-1$
    submenuManager.add(new Separator("additions")); //$NON-NLS-1$
    submenuManager.add(new Separator("additions-end")); //$NON-NLS-1$

    // Prepare for CreateChild item addition or removal.
    //
    createChildMenuManager = new MenuManager(ProjectConfigEditorPlugin.INSTANCE.getString("_UI_CreateChild_menu_item")); //$NON-NLS-1$
    submenuManager.insertBefore("additions", createChildMenuManager); //$NON-NLS-1$

    // Prepare for CreateSibling item addition or removal.
    //
    createSiblingMenuManager = new MenuManager(ProjectConfigEditorPlugin.INSTANCE.getString("_UI_CreateSibling_menu_item")); //$NON-NLS-1$
    submenuManager.insertBefore("additions", createSiblingMenuManager); //$NON-NLS-1$

    // Force an update because Eclipse hides empty menus now.
    //
    submenuManager.addMenuListener(new IMenuListener()
    {
      public void menuAboutToShow(IMenuManager menuManager)
      {
        menuManager.updateAll(true);
      }
    });

    addGlobalActions(submenuManager);
  }

  /**
   * When the active editor changes, this remembers the change and registers with it as a selection provider.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setActiveEditor(IEditorPart part)
  {
    super.setActiveEditor(part);
    activeEditorPart = part;

    // Switch to the new selection provider.
    //
    if (selectionProvider != null)
    {
      selectionProvider.removeSelectionChangedListener(this);
    }
    if (part == null)
    {
      selectionProvider = null;
    }
    else
    {
      selectionProvider = part.getSite().getSelectionProvider();
      selectionProvider.addSelectionChangedListener(this);

      // Fake a selection changed event to update the menus.
      //
      if (selectionProvider.getSelection() != null)
      {
        selectionChanged(new SelectionChangedEvent(selectionProvider, selectionProvider.getSelection()));
      }
    }
  }

  /**
   * This implements {@link org.eclipse.jface.viewers.ISelectionChangedListener},
   * handling {@link org.eclipse.jface.viewers.SelectionChangedEvent}s by querying for the children and siblings
   * that can be added to the selected object and updating the menus accordingly.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void selectionChanged(SelectionChangedEvent event)
  {
    // Remove any menu items for old selection.
    //
    if (createChildMenuManager != null)
    {
      depopulateManager(createChildMenuManager, createChildActions);
    }
    if (createSiblingMenuManager != null)
    {
      depopulateManager(createSiblingMenuManager, createSiblingActions);
    }

    // Query the new selection for appropriate new child/sibling descriptors
    //
    Collection<?> newChildDescriptors = null;
    Collection<?> newSiblingDescriptors = null;

    ISelection selection = event.getSelection();
    if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1)
    {
      Object object = ((IStructuredSelection)selection).getFirstElement();

      EditingDomain domain = ((IEditingDomainProvider)activeEditorPart).getEditingDomain();

      newChildDescriptors = domain.getNewChildDescriptors(object, null);
      newSiblingDescriptors = domain.getNewChildDescriptors(null, object);
    }

    // Generate actions for selection; populate and redraw the menus.
    //
    createChildActions = generateCreateChildActions(newChildDescriptors, selection);
    createSiblingActions = generateCreateSiblingActions(newSiblingDescriptors, selection);

    if (createChildMenuManager != null)
    {
      populateManager(createChildMenuManager, createChildActions, null);
      createChildMenuManager.update(true);
    }
    if (createSiblingMenuManager != null)
    {
      populateManager(createSiblingMenuManager, createSiblingActions, null);
      createSiblingMenuManager.update(true);
    }
  }

  protected void augment(Collection<?> descriptors, ISelection selection)
  {
    if (selection instanceof IStructuredSelection && descriptors != null)
    {
      IStructuredSelection structuredSelection = (IStructuredSelection)selection;
      Object object = structuredSelection.getFirstElement();
      if (object instanceof EObject)
      {
        for (EObject eObject = (EObject)object; eObject != null; eObject = eObject.eContainer())
        {
          if (eObject instanceof Project)
          {
            for (Object descriptor : descriptors)
            {
              if (descriptor instanceof CommandParameter)
              {
                CommandParameter commandParameter = (CommandParameter)descriptor;
                Object value = commandParameter.getValue();
                if (value instanceof RepositoryPredicate)
                {
                  Project project = (Project)eObject;
                  PreferenceNode preferenceNode = project.getPreferenceNode();
                  if (preferenceNode != null)
                  {
                    String projectName = preferenceNode.getName();
                    IProject iProject = WORKSPACE_ROOT.getProject(projectName);
                    RepositoryPredicate repositoryPredicate = (RepositoryPredicate)value;
                    repositoryPredicate.setProject(iProject);
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  protected Collection<IAction> generateCreateChildActions(Collection<?> descriptors, ISelection selection)
  {
    augment(descriptors, selection);
    return generateCreateChildActionsGen(descriptors, selection);
  }

  /**
   * This generates a {@link org.eclipse.emf.edit.ui.action.CreateChildAction} for each object in <code>descriptors</code>,
   * and returns the collection of these actions.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<IAction> generateCreateChildActionsGen(Collection<?> descriptors, ISelection selection)
  {
    Collection<IAction> actions = new ArrayList<IAction>();
    if (descriptors != null)
    {
      for (Object descriptor : descriptors)
      {
        actions.add(new CreateChildAction(activeEditorPart, selection, descriptor));
      }
    }
    return actions;
  }

  protected Collection<IAction> generateCreateSiblingActions(Collection<?> descriptors, ISelection selection)
  {
    augment(descriptors, selection);
    return generateCreateSiblingActionsGen(descriptors, selection);
  }

  /**
   * This generates a {@link org.eclipse.emf.edit.ui.action.CreateSiblingAction} for each object in <code>descriptors</code>,
   * and returns the collection of these actions.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<IAction> generateCreateSiblingActionsGen(Collection<?> descriptors, ISelection selection)
  {
    Collection<IAction> actions = new ArrayList<IAction>();
    if (descriptors != null)
    {
      for (Object descriptor : descriptors)
      {
        actions.add(new CreateSiblingAction(activeEditorPart, selection, descriptor));
      }
    }
    return actions;
  }

  /**
   * This populates the specified <code>manager</code> with {@link org.eclipse.jface.action.ActionContributionItem}s
   * based on the {@link org.eclipse.jface.action.IAction}s contained in the <code>actions</code> collection,
   * by inserting them before the specified contribution item <code>contributionID</code>.
   * If <code>contributionID</code> is <code>null</code>, they are simply added.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void populateManager(IContributionManager manager, Collection<? extends IAction> actions, String contributionID)
  {
    if (actions != null)
    {
      for (IAction action : actions)
      {
        if (contributionID != null)
        {
          manager.insertBefore(contributionID, action);
        }
        else
        {
          manager.add(action);
        }
      }
    }
  }

  /**
   * This removes from the specified <code>manager</code> all {@link org.eclipse.jface.action.ActionContributionItem}s
   * based on the {@link org.eclipse.jface.action.IAction}s contained in the <code>actions</code> collection.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void depopulateManager(IContributionManager manager, Collection<? extends IAction> actions)
  {
    if (actions != null)
    {
      IContributionItem[] items = manager.getItems();
      for (int i = 0; i < items.length; i++)
      {
        // Look into SubContributionItems
        //
        IContributionItem contributionItem = items[i];
        while (contributionItem instanceof SubContributionItem)
        {
          contributionItem = ((SubContributionItem)contributionItem).getInnerItem();
        }

        // Delete the ActionContributionItems with matching action.
        //
        if (contributionItem instanceof ActionContributionItem)
        {
          IAction action = ((ActionContributionItem)contributionItem).getAction();
          if (actions.contains(action))
          {
            manager.remove(contributionItem);
          }
        }
      }
    }
  }

  /**
   * This populates the pop-up menu before it appears.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public void menuAboutToShow(IMenuManager menuManager)
  {
    super.menuAboutToShow(menuManager);
    MenuManager submenuManager = null;

    submenuManager = new MenuManager(ProjectConfigEditorPlugin.INSTANCE.getString("_UI_CreateChild_menu_item")); //$NON-NLS-1$
    populateManager(submenuManager, createChildActions, null);
    menuManager.insertBefore("edit", submenuManager); //$NON-NLS-1$

    submenuManager = new MenuManager(ProjectConfigEditorPlugin.INSTANCE.getString("_UI_CreateSibling_menu_item")); //$NON-NLS-1$
    populateManager(submenuManager, createSiblingActions, null);
    menuManager.insertBefore("edit", submenuManager); //$NON-NLS-1$

    ISelectionProvider selectionProvider = activeEditor instanceof ISelectionProvider ? (ISelectionProvider)activeEditor
        : activeEditor.getEditorSite().getSelectionProvider();

    if (selectionProvider != null)
    {
      ISelection selection = selectionProvider.getSelection();
      IStructuredSelection structuredSelection = selection instanceof IStructuredSelection ? (IStructuredSelection)selection : StructuredSelection.EMPTY;

      final List<PreferenceNode> preferenceNodes = new ArrayList<PreferenceNode>();
      for (Object object : structuredSelection.toArray())
      {
        if (object instanceof Project)
        {
          preferenceNodes.add(((Project)object).getPreferenceNode());
        }
      }

      final Action action = new Action()
      {
        ShowInExplorerAction showInExplorerAction = new ShowInExplorerAction();

        {
          showInExplorerAction.selectionChanged(this, new StructuredSelection(preferenceNodes));
        }

        @Override
        public String getText()
        {
          return Messages.ProjectConfigActionBarContributor_ShowPackageExplorer_label;
        }

        @Override
        public void run()
        {
          showInExplorerAction.run(this);
        }
      };

      if (action.isEnabled())
      {
        menuManager.insertAfter("additions", action); //$NON-NLS-1$
      }
    }

    menuManager.insertAfter("ui-actions", new CommandContributionItem(new CommandContributionItemParameter(activeEditorPart.getEditorSite(), //$NON-NLS-1$
        "org.eclipse.oomph.projectconfig.editor.commands.Navigate", "org.eclipse.oomph.projectconfig.editor.commands.Navigate", 0))); //$NON-NLS-1$ //$NON-NLS-2$

  }

  @Override
  protected void addGlobalActions(IMenuManager menuManager)
  {
    addGlobalActionsGen(menuManager);

    updatePreferenceProfileReferencesAction.setEnabled(updatePreferenceProfileReferencesAction.isEnabled());
    menuManager.insertAfter("ui-actions", updatePreferenceProfileReferencesAction); //$NON-NLS-1$

    updatePreferenceProfileReferencesAction.setEnabled(applyPreferenceProfilesAction.isEnabled());
    menuManager.insertAfter("ui-actions", applyPreferenceProfilesAction); //$NON-NLS-1$

    menuManager.insertAfter("ui-actions", reloadEditorAction); //$NON-NLS-1$
  }

  /**
   * This inserts global actions before the "additions-end" separator.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addGlobalActionsGen(IMenuManager menuManager)
  {
    menuManager.insertAfter("additions-end", new Separator("ui-actions")); //$NON-NLS-1$ //$NON-NLS-2$
    menuManager.insertAfter("ui-actions", showPropertiesViewAction); //$NON-NLS-1$

    refreshViewerAction.setEnabled(refreshViewerAction.isEnabled());
    menuManager.insertAfter("ui-actions", refreshViewerAction); //$NON-NLS-1$

    super.addGlobalActions(menuManager);
  }

  /**
   * This ensures that a delete action will clean up all references to deleted objects.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected boolean removeAllReferencesOnDelete()
  {
    return true;
  }
}
