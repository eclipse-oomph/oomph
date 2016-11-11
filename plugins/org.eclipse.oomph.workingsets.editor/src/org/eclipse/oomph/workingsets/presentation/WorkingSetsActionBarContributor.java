/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.workingsets.presentation;

import org.eclipse.oomph.ui.DockableDialog;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.WorkingSetGroup;
import org.eclipse.oomph.workingsets.WorkingSetsFactory;
import org.eclipse.oomph.workingsets.presentation.WorkingSetsActionBarContributor.PreviewDialog.Previewable;
import org.eclipse.oomph.workingsets.provider.WorkingSetsEditPlugin;

import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.action.ControlAction;
import org.eclipse.emf.edit.ui.action.CreateChildAction;
import org.eclipse.emf.edit.ui.action.CreateSiblingAction;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;
import org.eclipse.emf.edit.ui.action.LoadResourceAction;
import org.eclipse.emf.edit.ui.action.ValidateAction;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.contentoutline.ContentOutline;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This is the action bar contributor for the WorkingSets model editor.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class WorkingSetsActionBarContributor extends EditingDomainActionBarContributor implements ISelectionChangedListener
{
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
  protected IAction showPropertiesViewAction = new Action(WorkingSetsEditorPlugin.INSTANCE.getString("_UI_ShowPropertiesView_menu_item"))
  {
    @Override
    public void run()
    {
      try
      {
        getPage().showView("org.eclipse.ui.views.PropertySheet");
      }
      catch (PartInitException exception)
      {
        WorkingSetsEditorPlugin.INSTANCE.log(exception);
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
  protected IAction refreshViewerAction = new Action(WorkingSetsEditorPlugin.INSTANCE.getString("_UI_RefreshViewer_menu_item"))
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

  protected final ShowPreviewAction showPreviewAction = new ShowPreviewAction("Preview...");

  /**
   * This creates an instance of the contributor.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public WorkingSetsActionBarContributor()
  {
    super(ADDITIONS_LAST_STYLE);
    loadResourceAction = new LoadResourceAction();
    validateAction = new ValidateAction();
    liveValidationAction = new DiagnosticDecorator.LiveValidator.LiveValidationAction(WorkingSetsEditorPlugin.getPlugin().getDialogSettings());
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
    toolBarManager.add(new Separator("workingsets-settings"));
    toolBarManager.add(new Separator("workingsets-additions"));
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

    IMenuManager submenuManager = new MenuManager(WorkingSetsEditorPlugin.INSTANCE.getString("_UI_WorkingSetsEditor_menu"),
        "org.eclipse.oomph.workingsetsMenuID");
    menuManager.insertAfter("additions", submenuManager);
    submenuManager.add(new Separator("settings"));
    submenuManager.add(new Separator("actions"));
    submenuManager.add(new Separator("additions"));
    submenuManager.add(new Separator("additions-end"));

    // Prepare for CreateChild item addition or removal.
    //
    createChildMenuManager = new MenuManager(WorkingSetsEditorPlugin.INSTANCE.getString("_UI_CreateChild_menu_item"));
    submenuManager.insertBefore("additions", createChildMenuManager);

    // Prepare for CreateSibling item addition or removal.
    //
    createSiblingMenuManager = new MenuManager(WorkingSetsEditorPlugin.INSTANCE.getString("_UI_CreateSibling_menu_item"));
    submenuManager.insertBefore("additions", createSiblingMenuManager);

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
   * @generated NOT
   */
  @Override
  public void setActiveEditor(IEditorPart part)
  {
    super.setActiveEditor(part);
    activeEditorPart = part;

    showPreviewAction.setActiveWorkbenchPart(part);

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

  /**
   * This generates a {@link org.eclipse.emf.edit.ui.action.CreateChildAction} for each object in <code>descriptors</code>,
   * and returns the collection of these actions.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<IAction> generateCreateChildActions(Collection<?> descriptors, ISelection selection)
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

  /**
   * This generates a {@link org.eclipse.emf.edit.ui.action.CreateSiblingAction} for each object in <code>descriptors</code>,
   * and returns the collection of these actions.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<IAction> generateCreateSiblingActions(Collection<?> descriptors, ISelection selection)
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
   * @generated
   */
  public void menuAboutToShowGen(IMenuManager menuManager)
  {
    super.menuAboutToShow(menuManager);
    MenuManager submenuManager = null;

    submenuManager = new MenuManager(WorkingSetsEditorPlugin.INSTANCE.getString("_UI_CreateChild_menu_item"));
    populateManager(submenuManager, createChildActions, null);
    menuManager.insertBefore("edit", submenuManager);

    submenuManager = new MenuManager(WorkingSetsEditorPlugin.INSTANCE.getString("_UI_CreateSibling_menu_item"));
    populateManager(submenuManager, createSiblingActions, null);
    menuManager.insertBefore("edit", submenuManager);
  }

  @Override
  public void menuAboutToShow(IMenuManager menuManager)
  {
    menuAboutToShowGen(menuManager);
    menuManager.insertBefore("ui-actions", showPreviewAction);
  }

  /**
   * This inserts global actions before the "additions-end" separator.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected void addGlobalActions(IMenuManager menuManager)
  {
    menuManager.insertAfter("additions-end", new Separator("ui-actions"));
    menuManager.insertAfter("ui-actions", showPropertiesViewAction);

    refreshViewerAction.setEnabled(refreshViewerAction.isEnabled());
    menuManager.insertAfter("ui-actions", refreshViewerAction);

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

  /**
   * @author Ed Merks
   */
  public static class PreviewDialog extends DockableDialog
  {
    public interface Previewable
    {
      public WorkingSetsProvider getWorkingSetsProvider();
    }

    protected static class Input extends ItemProvider
    {
      public EList<WorkingSetPresentation> getWorkingSets()
      {
        @SuppressWarnings("unchecked")
        EList<WorkingSetPresentation> result = (EList<WorkingSetPresentation>)(EList<?>)getChildren();
        return result;
      }
    }

    protected static class WorkingSetPresentation extends ItemProvider
    {
      private WorkingSet workingSet;

      public WorkingSetPresentation(WorkingSet workingSet)
      {
        super(workingSet.getName(), WorkingSetsEditPlugin.INSTANCE.getImage("full/obj16/WorkingSet"));

        this.workingSet = workingSet;
      }

      public EList<ProjectPresentation> getProjects()
      {
        @SuppressWarnings("unchecked")
        EList<ProjectPresentation> result = (EList<ProjectPresentation>)(EList<?>)getChildren();
        return result;
      }

      public WorkingSet getWorkingSet()
      {
        return workingSet;
      }
    }

    protected static class ProjectPresentation extends ItemProvider
    {
      private IProject project;

      public ProjectPresentation(IProject project)
      {
        super(project.getName(), ExtendedImageRegistry.INSTANCE.getImage(project.getAdapter(IWorkbenchAdapter.class).getImageDescriptor(project)));

        this.project = project;
      }

      public IProject getProject()
      {
        return project;
      }
    }

    public static class WorkingSetsProvider
    {
      protected PreviewDialog previewDialog;

      private List<WorkingSet> workingSets;

      public WorkingSetsProvider()
      {
      }

      public void setPreviewDialog(PreviewDialog previewDialog)
      {
        this.previewDialog = previewDialog;
      }

      protected void selectionChanged(IWorkbenchPart part, ISelection selection)
      {
        List<WorkingSet> oldWorkingSets = workingSets;
        previewDialog.getDockable().setWorkbenchPart(part);

        if (selection instanceof IStructuredSelection)
        {
          IStructuredSelection structuredSelection = (IStructuredSelection)selection;
          workingSets = getWorkingSets(structuredSelection);
          if (workingSets != oldWorkingSets)
          {
            previewDialog.getTree().setInput(null);
            previewDialog.reconcile();
            previewDialog.getTree().expandAll();
          }

          Set<Object> selectedObjects = new HashSet<Object>();
          for (Object value : structuredSelection.toArray())
          {
            if (value instanceof EObject)
            {
              for (EObject eObject = (EObject)value; eObject != null; eObject = eObject.eContainer())
              {
                for (WorkingSetPresentation workingSet : previewDialog.input.getWorkingSets())
                {
                  if (eObject == workingSet.getWorkingSet())
                  {
                    selectedObjects.add(workingSet);
                  }
                }
              }
            }

            if (value instanceof IAdaptable)
            {
              IProject project = ((IAdaptable)value).getAdapter(IProject.class);
              if (project != null)
              {
                for (WorkingSetPresentation workingSet : previewDialog.input.getWorkingSets())
                {
                  for (ProjectPresentation p : workingSet.getProjects())
                  {
                    if (project.equals(p.getProject()))
                    {
                      selectedObjects.add(p);
                    }
                  }
                }
              }
            }
          }

          if (!selectedObjects.isEmpty())
          {
            previewDialog.tree.setSelection(new StructuredSelection(new ArrayList<Object>(selectedObjects)));
          }
        }
      }

      protected List<WorkingSet> getWorkingSets()
      {
        return workingSets;
      }

      protected List<WorkingSet> getWorkingSets(IStructuredSelection structuredSelection)
      {
        Resource resource = previewDialog.editingDomain.getResourceSet().getResources().get(0);
        WorkingSetGroup workingSetGroup = (WorkingSetGroup)resource.getContents().get(0);
        return workingSetGroup.getWorkingSets();
      }
    }

    protected TreeViewer tree;

    private AdapterFactoryEditingDomain editingDomain;

    protected ISelectionProvider activePart;

    protected Input input = new Input();

    protected WorkingSetsProvider workingSetsProvider;

    private ISelectionListener selectionListener = new ISelectionListener()
    {
      public void selectionChanged(IWorkbenchPart part, ISelection selection)
      {
        getDockable().setWorkbenchPart(part);
        if (workingSetsProvider != null)
        {
          workingSetsProvider.selectionChanged(part, selection);
        }
      }
    };

    private CommandStackListener commandStackListener = new CommandStackListener()
    {
      public void commandStackChanged(EventObject event)
      {
        PreviewDialog.this.commandStackChanged(event);
      }
    };

    public PreviewDialog(IWorkbenchWindow workbenchWindow)
    {
      super(workbenchWindow);

      ISelectionService selectionService = workbenchWindow.getSelectionService();
      selectionService.addPostSelectionListener(selectionListener);
    }

    public TreeViewer getTree()
    {
      return tree;
    }

    @Override
    protected IDialogSettings getDialogBoundsSettings()
    {
      return WorkingSetsEditorPlugin.INSTANCE.getDialogSettings("Preview");
    }

    public void setWorkingSetsProvider(WorkingSetsProvider workingSetsProvider)
    {
      if (this.workingSetsProvider != null)
      {
        this.workingSetsProvider.setPreviewDialog(null);
      }

      this.workingSetsProvider = workingSetsProvider;

      if (workingSetsProvider != null)
      {
        workingSetsProvider.setPreviewDialog(this);
      }
    }

    protected void commandStackChanged(EventObject event)
    {
      reconcile();
      tree.expandAll();
    }

    @Override
    public boolean close()
    {
      handleWorkbenchPart(null);

      ISelectionService selectionService = getWorkbenchWindow().getSelectionService();
      selectionService.removePostSelectionListener(selectionListener);

      return super.close();
    }

    @Override
    public boolean handleWorkbenchPart(IWorkbenchPart part)
    {
      if (part instanceof ContentOutline)
      {
        ContentOutline contentOutline = (ContentOutline)part;
        IPage page = contentOutline.getCurrentPage();
        try
        {
          // It would be nice if we could generally figure out the editor that contributed the outline page.
          Method method = ReflectUtil.getMethod(page, "getSetupEditor");
          part = (IWorkbenchPart)method.invoke(page);
        }
        catch (Exception ex)
        {
          // Ignore.
        }
      }

      if (part == activePart)
      {
        return true;
      }

      if (editingDomain != null)
      {
        editingDomain.getCommandStack().removeCommandStackListener(commandStackListener);
      }

      if (part instanceof Previewable)
      {
        setWorkingSetsProvider(((Previewable)part).getWorkingSetsProvider());
        editingDomain = (AdapterFactoryEditingDomain)((IEditingDomainProvider)part).getEditingDomain();
        activePart = (ISelectionProvider)part;
        workingSetsProvider.workingSets = null;
        if (tree.getInput() != null)
        {
          tree.setInput(null);
        }
      }
      else
      {
        setWorkingSetsProvider(null);
        editingDomain = null;
        activePart = null;
      }

      if (editingDomain != null)
      {
        editingDomain.getCommandStack().addCommandStackListener(commandStackListener);

        if (tree.getInput() == null)
        {
          if (workingSetsProvider.getWorkingSets() == null)
          {
            workingSetsProvider.workingSets = workingSetsProvider.getWorkingSets((IStructuredSelection)activePart.getSelection());
          }

          reconcile();
          tree.expandAll();
        }

        return true;
      }

      return false;
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
      getShell().setText("Working Sets Preview");

      tree = new TreeViewer(parent);
      tree.expandAll();

      GridLayout layout = new GridLayout();
      layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
      layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
      layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
      layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);

      Tree treeControl = tree.getTree();
      treeControl.setLayout(layout);

      GridData layoutData = new GridData(GridData.FILL_BOTH);
      layoutData.heightHint = 800;
      layoutData.widthHint = 400;
      treeControl.setLayoutData(layoutData);

      applyDialogFont(treeControl);

      return treeControl;
    }

    public void reconcile()
    {
      EList<Object> children = input.getChildren();
      children.clear();

      tree.setContentProvider(new AdapterFactoryContentProvider(editingDomain.getAdapterFactory()));
      DecoratingColumLabelProvider labelProvider = new DecoratingColumLabelProvider(new AdapterFactoryLabelProvider(editingDomain.getAdapterFactory()),
          new DiagnosticDecorator(editingDomain, tree, WorkingSetsEditorPlugin.getPlugin().getDialogSettings()));
      tree.setLabelProvider(labelProvider);

      WorkingSet otherProjectsWorkingSet = WorkingSetsFactory.eINSTANCE.createWorkingSet();
      otherProjectsWorkingSet.setName("Other Projects");
      ItemProvider otherProjects = new WorkingSetPresentation(otherProjectsWorkingSet);
      children.add(otherProjects);

      Set<IProject> projects = new LinkedHashSet<IProject>(Arrays.asList(ResourcesPlugin.getWorkspace().getRoot().getProjects()));
      Set<IProject> unmatchedProjects = new LinkedHashSet<IProject>(projects);
      for (WorkingSet workingSet : workingSetsProvider.getWorkingSets())
      {
        ItemProvider child = new WorkingSetPresentation(workingSet);
        EList<Object> contents = child.getChildren();

        for (IProject project : projects)
        {
          if (project.isHidden())
          {
            unmatchedProjects.remove(project);
          }
          else if (workingSet.matches(project))
          {
            ItemProvider childProject = new ProjectPresentation(project);
            contents.add(childProject);
            unmatchedProjects.remove(project);
          }
        }

        children.add(child);
      }

      if (!unmatchedProjects.isEmpty())
      {
        EList<Object> contents = otherProjects.getChildren();
        for (IProject project : unmatchedProjects)
        {
          ItemProvider childProject = new ProjectPresentation(project);
          contents.add(childProject);
        }
      }

      if (tree.getInput() == null)
      {
        tree.setInput(input);
      }
    }

    @Override
    protected Control createButtonBar(Composite parent)
    {
      return null;
    }

    /**
     * Return the instance for this workbench window, if there is one.
     */
    public static PreviewDialog getFor(IWorkbenchWindow workbenchWindow)
    {
      return DockableDialog.getFor(PreviewDialog.class, workbenchWindow);
    }

    /**
     * Close the instance for this workbench window, if there is one.
     */
    public static void closeFor(IWorkbenchWindow workbenchWindow)
    {
      DockableDialog.closeFor(PreviewDialog.class, workbenchWindow);
    }

    /**
     * Reopen or create the instance for this workbench window.
     */
    public static PreviewDialog openFor(final IWorkbenchWindow workbenchWindow)
    {
      Factory<PreviewDialog> factory = new Factory<PreviewDialog>()
      {
        public PreviewDialog create(IWorkbenchWindow workbenchWindow)
        {
          return new PreviewDialog(workbenchWindow);
        }
      };

      return DockableDialog.openFor(PreviewDialog.class, factory, workbenchWindow);
    }
  }

  /**
   * @author Ed Merks
   */
  public static class ShowPreviewAction extends Action
  {
    private IWorkbenchPart activePart;

    public ShowPreviewAction(String text)
    {
      super(text, IAction.AS_CHECK_BOX);
      setImageDescriptor(WorkingSetsEditorPlugin.INSTANCE.getImageDescriptor("preview"));
    }

    @Override
    public void run()
    {
      if (isChecked())
      {
        PreviewDialog previewDialog = PreviewDialog.openFor(activePart.getSite().getWorkbenchWindow());
        DockableDialog.Dockable dockable = previewDialog.getDockable();
        dockable.setWorkbenchPart(activePart);
        dockable.associate(this);
      }
      else
      {
        PreviewDialog.closeFor(activePart.getSite().getWorkbenchWindow());
      }
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      if (workbenchPart instanceof Previewable)
      {
        activePart = workbenchPart;
        setEnabled(true);
        PreviewDialog previewDialog = PreviewDialog.getFor(workbenchPart.getSite().getWorkbenchWindow());
        if (previewDialog != null)
        {
          previewDialog.getDockable().associate(this);
        }
      }
      else
      {
        setEnabled(false);
        activePart = null;
      }
    }
  }
}
