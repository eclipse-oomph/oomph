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
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.provider.BaseEditUtil.IconReflectiveItemProvider;
import org.eclipse.oomph.base.util.EAnnotations;
import org.eclipse.oomph.internal.ui.OomphEditingDomainActionBarContributor;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.RedirectionTask;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.WorkspaceTask;
import org.eclipse.oomph.setup.impl.DynamicSetupTaskImpl;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.presentation.SetupEditor.BrowserDialog;
import org.eclipse.oomph.setup.ui.SetupEditorSupport;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.recorder.RecorderManager;
import org.eclipse.oomph.setup.ui.recorder.RecorderTransaction;
import org.eclipse.oomph.ui.DockableDialog;
import org.eclipse.oomph.ui.DockableDialog.Factory;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.WorkingSetsPackage;
import org.eclipse.oomph.workingsets.presentation.WorkingSetsActionBarContributor;
import org.eclipse.oomph.workingsets.presentation.WorkingSetsActionBarContributor.ShowPreviewAction;

import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProvider;
import org.eclipse.emf.edit.ui.action.ControlAction;
import org.eclipse.emf.edit.ui.action.LoadResourceAction;
import org.eclipse.emf.edit.ui.action.ValidateAction;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
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
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.views.properties.PropertySheet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This is the action bar contributor for the Setup model editor.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated not
 */
public class SetupActionBarContributor extends OomphEditingDomainActionBarContributor implements ISelectionChangedListener
{
  private static final String ENABLEMENT_ITEM_PREFIX = EnablementAction.class.getName() + "-";

  private static final Comparator<? super IAction> ACTION_COMPARATOR = new Comparator<IAction>()
  {
    public int compare(IAction a1, IAction a2)
    {
      return StringUtil.safe(a1.getText()).compareTo(StringUtil.safe(a2.getText()));
    }
  };

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
   * @generated NOT
   */
  protected ShowPropertiesViewAction showPropertiesViewAction = new ShowPropertiesViewAction();

  /**
   * This action refreshes the viewer of the current editor if the editor
   * implements {@link org.eclipse.emf.common.ui.viewer.IViewerProvider}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IAction refreshViewerAction = new Action(SetupEditorPlugin.INSTANCE.getString("_UI_RefreshViewer_menu_item"))
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
   * @generated NOT
   */
  protected MenuManager createChildMenuManager;

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
   * @generated NOT
   */
  protected MenuManager createSiblingMenuManager;

  private final ToggleViewerInputAction toggleViewerInputAction = new ToggleViewerInputAction();

  private final PreferenceRecorderToolbarAction recordPreferencesAction = new PreferenceRecorderToolbarAction(true);

  private final PreferenceCaptureToolbarAction capturePreferencesAction = new PreferenceCaptureToolbarAction(false);

  private final PreferenceCaptureToolbarAction importPreferencesAction = new PreferenceCaptureToolbarAction(true);

  private final CommandTableAction commandTableAction = new CommandTableAction();

  private final EditorTableAction editorTableAction = new EditorTableAction();

  @SuppressWarnings("unused")
  private final TestInstallAction testInstallAction = new TestInstallAction();

  private final OpenInSetupEditorAction openInSetupEditorAction = new OpenInSetupEditorAction();

  private final OpenInTextEditorAction openInTextEditorAction = new OpenInTextEditorAction();

  private final ShowTooltipsAction showTooltipsAction = new ShowTooltipsAction();

  private final ShowInformationBrowserAction showInformationBrowserAction = new ShowInformationBrowserAction();

  protected final WorkingSetsActionBarContributor.ShowPreviewAction showPreviewAction = new ShowPreviewAction("Show Working Sets Preview");

  private int lastSubMenuID;

  /**
   * This creates an instance of the contributor.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupActionBarContributor()
  {
    super(ADDITIONS_LAST_STYLE);
    loadResourceAction = new LoadResourceAction();
    validateAction = new ValidateAction();
    liveValidationAction = new DiagnosticDecorator.LiveValidator.LiveValidationAction(SetupEditorPlugin.getPlugin().getDialogSettings());
    controlAction = new ControlAction();
  }

  @Override
  public void init(IActionBars actionBars)
  {
    validateAction = null;

    showPropertiesViewAction.setPage(getPage());
    liveValidationAction.setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("live_validation"));
    refreshViewerAction.setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("refresh_view"));
    controlAction.setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("control"));

    loadResourceAction = new LoadResourceAction()
    {
      @Override
      public void run()
      {
        ResourceSet resourceSet = domain.getResourceSet();
        EList<Resource> resources = resourceSet.getResources();
        List<Resource> originalResources = new ArrayList<Resource>(resources);
        super.run();
        synchronized (resourceSet)
        {
          List<Resource> finalResources = new ArrayList<Resource>(resources);
          finalResources.removeAll(originalResources);
          if (!finalResources.isEmpty())
          {
            int index = 0;
            for (Resource resource : finalResources)
            {
              resources.move(++index, resource);
            }

            if (!toggleViewerInputAction.isChecked())
            {
              toggleViewerInputAction.run();
            }
          }
        }
      }
    };

    loadResourceAction.setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("load"));
    loadResourceAction.setId("load");

    controlAction.setId("control");
    liveValidationAction.setId("live");

    super.init(actionBars);
  }

  public final ToggleViewerInputAction getToggleViewerInputAction()
  {
    return toggleViewerInputAction;
  }

  public void scheduleValidation()
  {
    liveValidationAction.run();
  }

  /**
   * This adds Separators for editor additions to the tool bar.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public void contributeToToolBar(IToolBarManager toolBarManager)
  {
    toolBarManager.add(new Separator("setup-settings"));
    toolBarManager.add(recordPreferencesAction);
    toolBarManager.add(capturePreferencesAction);
    toolBarManager.add(importPreferencesAction);
    toolBarManager.add(commandTableAction);
    toolBarManager.add(editorTableAction);
    // toolBarManager.add(testInstallAction);
    toolBarManager.add(showInformationBrowserAction);
    toolBarManager.add(showPreviewAction);
    toolBarManager.add(toggleViewerInputAction);
    super.contributeToToolBar(toolBarManager);
    toolBarManager.add(new Separator("setup-additions"));
  }

  /**
   * This adds to the menu bar a menu and some separators for editor additions,
   * as well as the sub-menus for object creation items.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void contributeToMenuGen(IMenuManager menuManager)
  {
    super.contributeToMenu(menuManager);

    IMenuManager submenuManager = new MenuManager(SetupEditorPlugin.INSTANCE.getString("_UI_SetupEditor_menu"), "org.eclipse.oomph.setupMenuID");
    menuManager.insertAfter("additions", submenuManager);
    submenuManager.add(new Separator("settings"));
    submenuManager.add(new Separator("actions"));
    submenuManager.add(new Separator("additions"));
    submenuManager.add(new Separator("additions-end"));

    // Prepare for CreateChild item addition or removal.
    //
    createChildMenuManager = new MenuManager(SetupEditorPlugin.INSTANCE.getString("_UI_CreateChild_menu_item"));
    submenuManager.insertBefore("additions", createChildMenuManager);

    // Prepare for CreateSibling item addition or removal.
    //
    createSiblingMenuManager = new MenuManager(SetupEditorPlugin.INSTANCE.getString("_UI_CreateSibling_menu_item"));
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

  @Override
  public void contributeToMenu(IMenuManager menuManager)
  {
    contributeToMenuGen(menuManager);
    createChildMenuManager.setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("create_child"));
    createSiblingMenuManager.setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("create_sibling"));
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

    toggleViewerInputAction.setActiveWorkbenchPart(part);
    commandTableAction.setActivePart(part);
    editorTableAction.setActivePart(part);
    openInSetupEditorAction.setActiveWorkbenchPart(part);
    openInTextEditorAction.setActiveWorkbenchPart(part);
    capturePreferencesAction.setActiveWorkbenchPart(part);
    importPreferencesAction.setActiveWorkbenchPart(part);
    showPreviewAction.setActiveWorkbenchPart(part);
    showInformationBrowserAction.setActiveWorkbenchPart(part);

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
  public void selectionChangedGen(SelectionChangedEvent event)
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

  public void selectionChanged(SelectionChangedEvent event)
  {
    selectionChangedGen(event);
    // recordPreferencesAction.selectionChanged(event);
    openInSetupEditorAction.selectionChanged(event);
    openInTextEditorAction.selectionChanged(event);
    // testInstallAction.selectionChanged(event);
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

  protected Collection<IAction> generateCreateChildActions(Collection<?> descriptors, ISelection selection)
  {
    Collection<IAction> actions = generateCreateChildActionsGen(descriptors, selection);
    return addEnablementActions(descriptors, selection, false, actions);
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

  protected Collection<IAction> generateCreateSiblingActions(Collection<?> descriptors, ISelection selection)
  {
    Collection<IAction> actions = generateCreateSiblingActionsGen(descriptors, selection);
    return addEnablementActions(descriptors, selection, true, actions);
  }

  private Collection<IAction> addEnablementActions(Collection<?> descriptors, ISelection selection, boolean sibling, Collection<IAction> actions)
  {
    if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1)
    {
      Object object = ((IStructuredSelection)selection).getFirstElement();
      if (object instanceof EObject)
      {
        actions = new ArrayList<IAction>(actions);
        addEnablementActions(descriptors, object, sibling, actions);
      }
    }

    return actions;
  }

  private void addEnablementActions(Collection<?> descriptors, Object object, boolean sibling, Collection<IAction> actions)
  {
    EditingDomain domain = ((IEditingDomainProvider)activeEditorPart).getEditingDomain();
    ResourceSet resourceSet = domain.getResourceSet();

    Object siblingObject = null;
    if (sibling)
    {
      siblingObject = object;
      object = domain.getParent(object);

      if (!(object instanceof EObject))
      {
        return;
      }
    }

    ReflectiveItemProvider itemProvider = new ReflectiveItemProvider(null)
    {
      @Override
      protected void gatherAllMetaData(EObject eObject)
      {
        Resource mainResource = eObject.eResource();
        if (mainResource != null)
        {
          ResourceSet resourceSet = mainResource.getResourceSet();
          if (resourceSet == null)
          {
            gatherAllMetaData(mainResource);
          }
          else
          {
            // Allow for the resource set to grow as a result of proxy resolution.
            EList<Resource> resources = resourceSet.getResources();
            for (int i = 0; i < resources.size(); ++i)
            {
              gatherAllMetaData(resources.get(i));
            }
          }
        }
      }

      protected void gatherAllMetaData(Resource resource)
      {
        for (EObject root : resource.getContents())
        {
          if (root instanceof EPackage)
          {
            gatherMetaData((EPackage)root);
          }
        }
      }
    };

    IconReflectiveItemProvider iconItemProvider = ((SetupEditor)activeEditor).getReflectiveItemProvider();
    Shell shell = activeEditorPart.getSite().getShell();

    Collection<?> newChildDescriptors = itemProvider.getNewChildDescriptors(object, domain, siblingObject);
    for (Iterator<?> it = newChildDescriptors.iterator(); it.hasNext();)
    {
      Object descriptor = it.next();
      if (descriptor instanceof CommandParameter)
      {
        Object value = ((CommandParameter)descriptor).getValue();
        if (value instanceof EObject)
        {
          EObject eObject = (EObject)value;
          if (eObject instanceof DynamicSetupTaskImpl)
          {
            EClass eClass = eObject.eClass();
            Resource eResource = eClass.eResource();
            if (eResource != null && eResource.getResourceSet() == resourceSet)
            {
              EList<SetupTask> enablementTasks = SetupTaskPerformer.createEnablementTasks(eClass, true);
              if (enablementTasks != null)
              {
                String typeText = EAnnotations.getText(eClass);
                if (typeText == null)
                {
                  typeText = iconItemProvider.getTypeText(eObject);
                }

                EnablementAction action = new EnablementAction(shell, eClass, typeText, enablementTasks);
                actions.add(action);
              }
            }
          }
        }
      }
    }
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
  protected void populateManagerGen(IContributionManager manager, Collection<? extends IAction> actions, String contributionID)
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

  protected void populateManager(IContributionManager manager, Collection<? extends IAction> actions, String contributionID)
  {
    manager.add(new Separator("elements"));
    manager.add(new Separator("elements-end"));
    manager.add(new Separator("scopes"));
    manager.add(new Separator("scopes-end"));
    manager.add(new Separator("defaults"));
    manager.add(new Separator("defaults-end"));
    manager.add(new Separator("installations"));
    manager.add(new Separator("installations-end"));
    manager.add(new Separator("tasks"));
    manager.add(new Separator("tasks-end"));
    manager.add(new Separator("annotations"));
    manager.add(new Separator("annotations-end"));
    manager.add(new Separator("additions"));
    manager.add(new Separator("additions-end"));

    List<IAction> elements = new ArrayList<IAction>();
    List<IAction> scopes = new ArrayList<IAction>();
    List<IAction> defaults = new ArrayList<IAction>();
    List<IAction> installations = new ArrayList<IAction>();
    List<IAction> tasks = new ArrayList<IAction>();
    List<IAction> annotations = new ArrayList<IAction>();
    List<IAction> additions = new ArrayList<IAction>();
    List<EnablementAction> additionalTasks = new ArrayList<EnablementAction>();
    List<EnablementAction> additionalElements = new ArrayList<EnablementAction>();

    Set<String> installedClasses = new HashSet<String>();

    for (IAction action : actions)
    {
      if (action instanceof EnablementAction)
      {
        EnablementAction additionalTaskAction = (EnablementAction)action;
        EClass eClass = additionalTaskAction.getEClass();
        if (SetupPackage.Literals.SETUP_TASK.isSuperTypeOf(eClass))
        {
          additionalTasks.add(additionalTaskAction);
        }
        else
        {
          additionalElements.add(additionalTaskAction);
        }

        continue;
      }

      Object descriptor;
      if (action instanceof CreateChildAction)
      {
        descriptor = ((CreateChildAction)action).getDescriptor();
      }
      else if (action instanceof CreateSiblingAction)
      {
        descriptor = ((CreateSiblingAction)action).getDescriptor();
      }
      else
      {
        additions.add(action);
        continue;
      }

      if (descriptor instanceof CommandParameter)
      {
        CommandParameter parameter = (CommandParameter)descriptor;
        Object value = parameter.getValue();
        if (value instanceof Scope)
        {
          scopes.add(action);
        }
        else if (value instanceof CompoundTask || value instanceof VariableTask || value instanceof RedirectionTask)
        {
          defaults.add(action);
        }
        else if (value instanceof InstallationTask || value instanceof WorkspaceTask)
        {
          installations.add(action);
        }
        else if (value instanceof SetupTask)
        {
          tasks.add(action);
        }
        else if (value instanceof Annotation)
        {
          annotations.add(action);
        }
        else
        {
          elements.add(action);
        }

        if (value instanceof EObject)
        {
          EClass eClass = ((EObject)value).eClass();
          String installedClass = getInstalledClass(eClass);
          installedClasses.add(installedClass);
        }
      }
      else
      {
        additions.add(action);
      }
    }

    removeInstalledClasses(installedClasses, additionalElements);
    Collections.sort(elements, ACTION_COMPARATOR);

    removeInstalledClasses(installedClasses, additionalTasks);
    Collections.sort(tasks, ACTION_COMPARATOR);

    populateManagerGen(manager, elements, "elements-end");

    if (!additionalElements.isEmpty())
    {
      populateManagerEnablements(manager, SetupEditorPlugin.INSTANCE.getString("_UI_AdditionalElements_menu_item"), "elements-end", additionalElements);
    }

    populateManagerGen(manager, scopes, "scopes-end");
    populateManagerGen(manager, defaults, "defaults-end");
    populateManagerGen(manager, installations, "installations-end");
    populateManagerGen(manager, tasks, "tasks-end");

    if (!additionalTasks.isEmpty())
    {
      populateManagerEnablements(manager, SetupEditorPlugin.INSTANCE.getString("_UI_AdditionalTasks_menu_item"), "tasks-end", additionalTasks);
    }

    populateManagerGen(manager, annotations, "annotations-end");
    populateManagerGen(manager, additions, "additions-end");
  }

  private String getInstalledClass(EClass eClass)
  {
    return eClass.getEPackage().getNsURI() + "#" + eClass.getName();
  }

  private void removeInstalledClasses(Set<String> installedClasses, List<EnablementAction> actions)
  {
    for (Iterator<EnablementAction> it = actions.iterator(); it.hasNext();)
    {
      EnablementAction action = it.next();
      EClass eClass = action.getEClass();
      String installedClass = getInstalledClass(eClass);
      if (installedClasses.contains(installedClass))
      {
        it.remove();
      }
    }
  }

  private void populateManagerEnablements(IContributionManager manager, String subMenuText, String insertBeforeID, final List<EnablementAction> additionalTasks)
  {
    int id = ++lastSubMenuID;
    String subMenuID = ENABLEMENT_ITEM_PREFIX + id;

    final MenuManager submenuManager = new MenuManager(subMenuText, subMenuID);
    submenuManager.addMenuListener(new IMenuListener()
    {
      public void menuAboutToShow(IMenuManager manager)
      {
        // Execute later in event loop to make sure the menu is visible when the first image is loaded.
        UIUtil.asyncExec(new Runnable()
        {
          public void run()
          {
            final Queue<EnablementAction> queue = new ConcurrentLinkedQueue<EnablementAction>(additionalTasks);
            int jobs = Math.max(queue.size(), 10);

            for (int i = 0; i < jobs; i++)
            {
              Job iconLoader = new Job("IconLoader-" + i)
              {
                @Override
                protected IStatus run(IProgressMonitor monitor)
                {
                  EnablementAction action;
                  while ((action = queue.poll()) != null && submenuManager.isVisible() && !monitor.isCanceled())
                  {
                    action.loadImage();
                  }

                  return Status.OK_STATUS;
                }
              };

              iconLoader.setSystem(true);
              iconLoader.schedule();
            }
          }
        });
      }
    });

    Collections.sort(additionalTasks, ACTION_COMPARATOR);
    populateManagerGen(submenuManager, additionalTasks, null);

    manager.insertBefore(insertBeforeID, submenuManager);
  }

  /**
   * This removes from the specified <code>manager</code> all {@link org.eclipse.jface.action.ActionContributionItem}s
   * based on the {@link org.eclipse.jface.action.IAction}s contained in the <code>actions</code> collection.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   */
  protected void depopulateManagerGen(IContributionManager manager, Collection<? extends IAction> actions)
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

  protected void depopulateManager(IContributionManager manager, Collection<? extends IAction> actions)
  {
    IContributionItem[] items = manager.getItems();
    if (items != null)
    {
      for (int i = 0; i < items.length; i++)
      {
        IContributionItem item = items[i];
        String id = item.getId();
        if (id != null && id.startsWith(ENABLEMENT_ITEM_PREFIX))
        {
          manager.remove(item);
        }
      }
    }

    depopulateManagerGen(manager, actions);
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

    submenuManager = new MenuManager(SetupEditorPlugin.INSTANCE.getString("_UI_CreateChild_menu_item"),
        SetupEditorPlugin.INSTANCE.getImageDescriptor("create_child"), "CreateChild");
    populateManager(submenuManager, createChildActions, null);
    menuManager.insertBefore("edit", submenuManager);

    submenuManager = new MenuManager(SetupEditorPlugin.INSTANCE.getString("_UI_CreateSibling_menu_item"),
        SetupEditorPlugin.INSTANCE.getImageDescriptor("create_sibling"), "CreateSibling");
    populateManager(submenuManager, createSiblingActions, null);
    menuManager.insertBefore("edit", submenuManager);

    menuManager.insertBefore("ui-actions", openInSetupEditorAction);
    menuManager.insertBefore("ui-actions", openInTextEditorAction);
  }

  /**
   * This inserts global actions before the "additions-end" separator.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void addGlobalActionsGen(IMenuManager menuManager)
  {
    menuManager.insertAfter("additions-end", new Separator("ui-actions"));
    menuManager.insertAfter("ui-actions", showPropertiesViewAction);

    refreshViewerAction.setEnabled(refreshViewerAction.isEnabled());
    menuManager.insertAfter("ui-actions", refreshViewerAction);

    super.addGlobalActions(menuManager);
  }

  @Override
  protected void addGlobalActions(IMenuManager menuManager)
  {
    addGlobalActionsGen(menuManager);
    showTooltipsAction.setChecked(isShowTooltips());
    menuManager.insertAfter("live", new ActionContributionItem(showTooltipsAction));
    menuManager.insertBefore("properties", new ActionContributionItem(showInformationBrowserAction));
    menuManager.insertBefore("properties", showPreviewAction);
    menuManager.insertBefore("load", menuManager.remove("control"));
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

  public boolean isLiveValidation()
  {
    return liveValidationAction.isChecked();
  }

  public void setLiveValidation(boolean liveValidation)
  {
    liveValidationAction.setChecked(liveValidation);
    liveValidationAction.run();
  }

  public void showInformationBrowser(Object object)
  {
    showInformationBrowserAction.open(new StructuredSelection(object));
  }

  public boolean isInformationBrowserShowing()
  {
    return showInformationBrowserAction.isChecked();
  }

  public void openInTextEditor(Object object)
  {
    openInTextEditorAction.open(object);
  }

  public void openInSetupEditor(Object object)
  {
    openInSetupEditorAction.open(object);
  }

  public void openInPropertiesView(SetupEditor setupEditor, Object object, String property)
  {
    showPropertiesViewAction.open(setupEditor, object, property);
  }

  /**
   * @author Eike Stepper
   */
  private static final class CreateChildAction extends org.eclipse.emf.edit.ui.action.CreateChildAction
  {
    public CreateChildAction(EditingDomain editingDomain, ISelection selection, Object descriptor)
    {
      super(editingDomain, selection, descriptor);
    }

    public CreateChildAction(IEditorPart editorPart, ISelection selection, Object descriptor)
    {
      super(editorPart, selection, descriptor);
    }

    public CreateChildAction(IWorkbenchPart workbenchPart, ISelection selection, Object descriptor)
    {
      super(workbenchPart, selection, descriptor);
    }

    public Object getDescriptor()
    {
      return descriptor;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class CreateSiblingAction extends org.eclipse.emf.edit.ui.action.CreateSiblingAction
  {
    public CreateSiblingAction(EditingDomain editingDomain, ISelection selection, Object descriptor)
    {
      super(editingDomain, selection, descriptor);
    }

    public CreateSiblingAction(IEditorPart editorPart, ISelection selection, Object descriptor)
    {
      super(editorPart, selection, descriptor);
    }

    public CreateSiblingAction(IWorkbenchPart workbenchPart, ISelection selection, Object descriptor)
    {
      super(workbenchPart, selection, descriptor);
    }

    public Object getDescriptor()
    {
      return descriptor;
    }
  }

  /**
   * @author Eike Stepper
   */
  private class PreferenceRecorderToolbarAction extends Action
  {
    public PreferenceRecorderToolbarAction(boolean withDialog)
    {
      super("Record Preferences", SetupEditorPlugin.INSTANCE.getImageDescriptor("preference_recorder"));
    }

    @Override
    public void run()
    {
      RecorderManager.INSTANCE.setTemporaryRecorderTarget(EcoreUtil.getURI(getScope()));
      RecorderManager.INSTANCE.record(getActiveEditor());
    }
  }

  private EObject getScope()
  {
    SetupEditor editor = (SetupEditor)getActiveEditor();
    EObject rootObject = editor.getEditingDomain().getResourceSet().getResources().get(0).getContents().get(0);
    ISelection selection = ((ISelectionProvider)editor).getSelection();
    if (selection instanceof IStructuredSelection)
    {
      IStructuredSelection structuredSelection = (IStructuredSelection)selection;
      Object element = structuredSelection.getFirstElement();
      if (element instanceof EObject)
      {
        EObject eObject = (EObject)element;
        if (EcoreUtil.isAncestor(rootObject, eObject))
        {
          for (EObject eContainer = eObject; eContainer != null; eContainer = eContainer.eContainer())
          {
            if (eContainer instanceof Scope)
            {
              return eContainer;
            }
          }
        }
      }
    }

    return rootObject;
  }

  /**
   * @author Ed Merks
   */
  private class PreferenceCaptureToolbarAction extends Action
  {
    private SetupEditor setupEditor;

    private boolean fromEclipsePreferenceFile;

    public PreferenceCaptureToolbarAction(boolean fromEclipsePreferenceFile)
    {
      super(fromEclipsePreferenceFile ? "Import Preferences" : "Capture Preferences",
          SetupEditorPlugin.INSTANCE.getImageDescriptor(fromEclipsePreferenceFile ? "preference_importer" : "preference_picker"));
      this.fromEclipsePreferenceFile = fromEclipsePreferenceFile;
    }

    @Override
    public void run()
    {
      PreferenceCaptureDialog preferenceCaptureDialog = new PreferenceCaptureDialog(null, setupEditor.getAdapterFactory(), fromEclipsePreferenceFile);
      if (preferenceCaptureDialog.open() == Dialog.OK)
      {
        RecorderManager.INSTANCE.setTemporaryRecorderTarget(EcoreUtil.getURI(getScope()));
        final RecorderTransaction transaction = RecorderTransaction.open(setupEditor);

        try
        {
          transaction.setPreferences(preferenceCaptureDialog.getResult());
          transaction.commit();
        }
        finally
        {
          transaction.close();
        }
      }
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      if (workbenchPart instanceof SetupEditor)
      {
        setupEditor = (SetupEditor)workbenchPart;
        setEnabled(true);
      }
      else
      {
        setEnabled(false);
        setupEditor = null;
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  @Deprecated
  private class TestInstallAction extends Action
  {
    private Project project;

    public TestInstallAction()
    {
      super("Test Install", AS_PUSH_BUTTON);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("run"));
      setToolTipText("Launch the installer with the current project");
    }

    @SuppressWarnings("unused")
    public void selectionChanged(SelectionChangedEvent event)
    {
      ISelection selection = event.getSelection();
      if (selection instanceof IStructuredSelection)
      {
        IStructuredSelection structuredSelection = (IStructuredSelection)selection;
        if (structuredSelection.size() == 1)
        {
          Object element = structuredSelection.getFirstElement();
          if (element instanceof EObject)
          {
            project = getProject((EObject)element);
            if (project != null)
            {
              setEnabled(true);
              return;
            }
          }
        }
      }

      project = null;
      setEnabled(false);
    }

    @Override
    public void run()
    {
      // Shell shell = activeEditorPart.getSite().getShell();
      // new org.eclipse.oomph.internal.setup.ui.InstallerDialog(shell, project).open();
    }

    private Project getProject(EObject object)
    {
      while (object != null && !(object instanceof Project))
      {
        object = object.eContainer();
      }

      return (Project)object;
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class CommandTableAction extends AbstractTableAction
  {
    public CommandTableAction()
    {
      super("Command Table");
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("commands"));
      setToolTipText("Show a table of all available commands");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String renderHTML()
    {
      IBindingService bindingService = PlatformUI.getWorkbench().getService(IBindingService.class);
      Binding[] bindings = bindingService.getBindings();
      Map<String, List<Command>> map = new HashMap<String, List<Command>>();

      ICommandService commandService = PlatformUI.getWorkbench().getService(ICommandService.class);
      for (Command command : commandService.getDefinedCommands())
      {
        try
        {
          String category = command.getCategory().getName();
          if (category == null || category.length() == 0)
          {
            category = command.getCategory().getId();
          }

          List<Command> commands = map.get(category);
          if (commands == null)
          {
            commands = new ArrayList<Command>();
            map.put(category, commands);
          }

          commands.add(command);
        }
        catch (NotDefinedException ex)
        {
          SetupEditorPlugin.getPlugin().log(ex);
        }
      }

      List<String> categories = new ArrayList<String>(map.keySet());
      Collections.sort(categories);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(baos);
      out.println("<table border=\"1\">");

      for (String category : categories)
      {
        out.println("<tr><td colspan=\"3\" bgcolor=\"eae6ff\"><br><h2>" + category + "</h2></td></tr>");

        List<Command> commands = map.get(category);
        Collections.sort(commands);

        for (Command command : commands)
        {
          StringBuilder keys = new StringBuilder();
          for (Binding binding : bindings)
          {
            ParameterizedCommand parameterizedCommand = binding.getParameterizedCommand();
            if (parameterizedCommand != null)
            {
              if (parameterizedCommand.getId().equals(command.getId()))
              {
                if (keys.length() != 0)
                {
                  keys.append("<br>");
                }

                keys.append(binding.getTriggerSequence());
              }
            }
          }

          if (keys.length() == 0)
          {
            keys.append("&nbsp;");
          }

          String name;
          try
          {
            name = command.getName();
          }
          catch (NotDefinedException ex)
          {
            name = command.getId();
          }

          out.println("<tr><td valign=\"top\" width=\"200\">" + name + "</td><td valign=\"top\" width=\"400\">" + command.getId()
              + "</td><td valign=\"top\" width=\"100\">" + keys + "</td></tr>");
        }
      }

      out.println("</table>");

      try
      {
        out.flush();
        return baos.toString("UTF-8");
      }
      catch (UnsupportedEncodingException ex)
      {
        return "UTF-8 is unsupported";
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Class<CommandTableDialog> getDialogClass()
    {
      return CommandTableDialog.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected DockableDialog.Factory<CommandTableDialog> getDialogFactory()
    {
      return new Factory<CommandTableDialog>()
      {
        public CommandTableDialog create(IWorkbenchWindow workbenchWindow)
        {
          return new CommandTableDialog(workbenchWindow, CommandTableAction.this);
        }
      };
    }

    private static final class CommandTableDialog extends TableDialog
    {
      public CommandTableDialog(IWorkbenchWindow workbenchWindow, AbstractTableAction tableAction)
      {
        super(workbenchWindow, tableAction);
      }

      @Override
      protected IDialogSettings getDialogBoundsSettings()
      {
        return SetupEditorPlugin.INSTANCE.getDialogSettings("CommandTable");
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class EditorTableAction extends AbstractTableAction
  {
    public EditorTableAction()
    {
      super("Editor Table");
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("FileEditor"));
      setToolTipText("Show a table of all available editors");
    }

    @SuppressWarnings("restriction")
    @Override
    protected String renderHTML()
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(baos);
      out.println("<table border=\"1\">");
      out.println("<tr><td bgcolor=\"eae6ff\">ID</td><td bgcolor=\"eae6ff\">Label</td></tr>");

      org.eclipse.ui.internal.registry.EditorRegistry registry = (org.eclipse.ui.internal.registry.EditorRegistry)PlatformUI.getWorkbench().getEditorRegistry();
      IEditorDescriptor[] editors = registry.getSortedEditorsFromPlugins();
      for (IEditorDescriptor editor : editors)
      {
        out.println("<tr><td>" + editor.getId() + "</td><td>" + editor.getLabel() + "</td></tr>");
      }

      out.println("</table>");

      try
      {
        out.flush();
        return baos.toString("UTF-8");
      }
      catch (UnsupportedEncodingException ex)
      {
        return "UTF-8 is unsupported";
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Class<EditorTableDialog> getDialogClass()
    {
      return EditorTableDialog.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected DockableDialog.Factory<EditorTableDialog> getDialogFactory()
    {
      return new Factory<EditorTableDialog>()
      {
        public EditorTableDialog create(IWorkbenchWindow workbenchWindow)
        {
          return new EditorTableDialog(workbenchWindow, EditorTableAction.this);
        }
      };
    }

    private static final class EditorTableDialog extends TableDialog
    {
      public EditorTableDialog(IWorkbenchWindow workbenchWindow, AbstractTableAction tableAction)
      {
        super(workbenchWindow, tableAction);
      }

      @Override
      protected IDialogSettings getDialogBoundsSettings()
      {
        return SetupEditorPlugin.INSTANCE.getDialogSettings("EditorTable");
      }
    }
  }

  protected static final class ToggleViewerInputAction extends Action
  {
    private SetupEditor setupEditor;

    public ToggleViewerInputAction()
    {
      super("Show Resources", IAction.AS_CHECK_BOX);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("ToggleInput"));
      setToolTipText("Show all resources");
    }

    @Override
    public void run()
    {
      setupEditor.toggleInput();
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      if (workbenchPart instanceof SetupEditor)
      {
        setupEditor = (SetupEditor)workbenchPart;
        setEnabled(true);
        setChecked(setupEditor.selectionViewer.getInput() instanceof ResourceSet);
      }
      else
      {
        setEnabled(false);
        setupEditor = null;
      }
    }
  }

  static URI getEditURI(Object object, boolean text)
  {
    object = AdapterFactoryEditingDomain.unwrap(object);
    if (object instanceof EObject)
    {
      EObject eObject = (EObject)object;
      Resource resource = eObject.eResource();
      if (resource != null)
      {
        ResourceSet resourceSet = resource.getResourceSet();
        if (text || resourceSet == null || resourceSet.getResources().indexOf(resource) != 0)
        {
          return EcoreUtil.getURI(eObject);
        }
      }
    }
    else if (object instanceof Resource)
    {
      Resource resource = (Resource)object;
      return resource.getURI();
    }

    return null;
  }

  /**
   * @author Ed Merks
   */
  private static final class OpenInSetupEditorAction extends Action
  {
    private URI uri;

    private SetupEditor setupEditor;

    public OpenInSetupEditorAction()
    {
      setText("Open in Setup Editor");
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("edit_setup"));
    }

    @Override
    public void run()
    {
      open(uri);
    }

    public void open(Object object)
    {
      final URI uri = getEditURI(object, true);
      if ("performer".equals(uri.scheme()))
      {
        SetupEditorSupport.getEditor(setupEditor.getSite().getWorkbenchWindow().getActivePage(),
            OpenInTextEditorAction.createEditorInput(uri.trimFragment(), object), uri, true);
      }
      else
      {
        open(uri);
      }
    }

    public void open(URI uri)
    {
      SetupEditorSupport.getEditor(setupEditor.getSite().getWorkbenchWindow().getActivePage(), uri, true);
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      if (workbenchPart instanceof SetupEditor)
      {
        setupEditor = (SetupEditor)workbenchPart;
        setEnabled(true);
      }
      else
      {
        setEnabled(false);
        setupEditor = null;
      }
    }

    public final void selectionChanged(SelectionChangedEvent event)
    {
      uri = null;

      IStructuredSelection selection = (IStructuredSelection)event.getSelection();
      if (selection.size() == 1)
      {
        Object object = selection.getFirstElement();
        uri = getEditURI(object, false);
      }

      setEnabled(uri != null);
    }
  }

  /**
   * @author Ed Merks
   */
  private static final class OpenInTextEditorAction extends Action
  {
    private Object object;

    private URI uri;

    private SetupEditor setupEditor;

    public OpenInTextEditorAction()
    {
      setText("Open in Text Editor");
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("edit_text"));
    }

    @Override
    public void run()
    {
      if ("performer".equals(uri.scheme()))
      {
        SetupEditorSupport.getTextEditor(setupEditor.getSite().getWorkbenchWindow().getActivePage(), createEditorInput(uri.trimFragment(), object));
      }
      else
      {
        open(uri);
      }
    }

    public static IEditorInput createEditorInput(final URI uri, Object object)
    {
      final Resource resource = object instanceof Resource ? (Resource)object : ((EObject)object).eResource();
      IStorageEditorInput editorInput = new IStorageEditorInput()
      {
        public <T> T getAdapter(Class<T> adapter)
        {
          if (adapter == IStorage.class)
          {
            try
            {
              @SuppressWarnings("unchecked")
              T storage = (T)getStorage();
              return storage;
            }
            catch (CoreException ex)
            {
            }
          }

          return null;
        }

        public String getToolTipText()
        {
          return uri.toString();
        }

        public IPersistableElement getPersistable()
        {
          return null;
        }

        public String getName()
        {
          return uri.lastSegment();
        }

        public ImageDescriptor getImageDescriptor()
        {
          return null;
        }

        public boolean exists()
        {
          return true;
        }

        public IStorage getStorage() throws CoreException
        {
          return new IStorage()
          {
            public <T> T getAdapter(Class<T> adapter)
            {
              return null;
            }

            public boolean isReadOnly()
            {
              return true;
            }

            public String getName()
            {
              return uri.lastSegment();
            }

            public IPath getFullPath()
            {
              return new Path(uri.toString());
            }

            public InputStream getContents() throws CoreException
            {
              try
              {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                resource.save(out, null);
                return new ByteArrayInputStream(out.toByteArray());
              }
              catch (IOException ex)
              {
                SetupEditorPlugin.INSTANCE.coreException(ex);
                return new ByteArrayInputStream(new byte[0]);
              }
            }
          };
        }
      };

      return editorInput;
    }

    public void open(Object object)
    {
      final URI uri = getEditURI(object, true);
      if ("performer".equals(uri.scheme()))
      {
        SetupEditorSupport.getTextEditor(setupEditor.getSite().getWorkbenchWindow().getActivePage(), createEditorInput(uri.trimFragment(), object));
      }
      else
      {
        open(uri);
      }
    }

    public void open(URI uri)
    {
      SetupEditorSupport.getTextEditor(setupEditor.getSite().getWorkbenchWindow().getActivePage(), uri);
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      if (workbenchPart instanceof SetupEditor)
      {
        setupEditor = (SetupEditor)workbenchPart;
        setEnabled(true);
      }
      else
      {
        setEnabled(false);
        setupEditor = null;
      }
    }

    public final void selectionChanged(SelectionChangedEvent event)
    {
      uri = null;

      IStructuredSelection selection = (IStructuredSelection)event.getSelection();
      if (selection.size() == 1)
      {
        object = selection.getFirstElement();
        uri = getEditURI(object, true);
      }

      setEnabled(uri != null);
    }
  }

  public static boolean isShowTooltips()
  {
    return SetupUIPlugin.INSTANCE.getInstancePreference("showTooltips").get(true);
  }

  public static void setShowTooltips(boolean showTooltips)
  {
    SetupUIPlugin.INSTANCE.getInstancePreference("showTooltips").set(showTooltips);
  }

  /**
   * @author Ed Merks
   */
  private static final class ShowTooltipsAction extends Action
  {
    public ShowTooltipsAction()
    {
      setText("Show Tooltips");
      setChecked(isShowTooltips());
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("show_tooltips"));
    }

    @Override
    public void run()
    {
      boolean show = isChecked();
      setShowTooltips(show);
    }
  }

  /**
   * @author Ed Merks
   */
  private static final class ShowInformationBrowserAction extends Action
  {
    private SetupEditor setupEditor;

    public ShowInformationBrowserAction()
    {
      super("Show Information Browser", Action.AS_CHECK_BOX);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("open_browser"));
    }

    @Override
    public void run()
    {
      if (isChecked())
      {
        IStructuredSelection selection = (IStructuredSelection)setupEditor.getSelection();
        open(selection);
      }
      else
      {
        BrowserDialog.closeFor(setupEditor.getSite().getWorkbenchWindow());
      }
    }

    public void open(IStructuredSelection selection)
    {
      BrowserDialog browserDialog = BrowserDialog.openFor(setupEditor.getSite().getWorkbenchWindow());
      Object object = selection.getFirstElement();
      if (object == null)
      {
        EList<Resource> resources = setupEditor.getEditingDomain().getResourceSet().getResources();
        if (!resources.isEmpty())
        {
          object = resources.get(0);
        }
      }

      browserDialog.setInput(setupEditor, object);
      browserDialog.getDockable().associate(this);
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      if (workbenchPart instanceof SetupEditor)
      {
        setupEditor = (SetupEditor)workbenchPart;
        setEnabled(true);
        BrowserDialog browserDialog = BrowserDialog.getFor(workbenchPart.getSite().getWorkbenchWindow());
        if (browserDialog != null)
        {
          browserDialog.getDockable().associate(this);
        }
      }
      else
      {
        setEnabled(false);
        setupEditor = null;
      }
    }
  }

  /**
   * @author Ed Merks
   */
  private static class ShowPropertiesViewAction extends Action
  {
    private IWorkbenchPage page;

    private ShowPropertiesViewAction()
    {
      super(SetupEditorPlugin.INSTANCE.getString("_UI_ShowPropertiesView_menu_item"), SetupEditorPlugin.INSTANCE.getImageDescriptor("show_properties_view"));
      setId("properties");
    }

    public void setPage(IWorkbenchPage page)
    {
      this.page = page;
    }

    @Override
    public void run()
    {
      try
      {
        page.showView("org.eclipse.ui.views.PropertySheet");
      }
      catch (PartInitException exception)
      {
        SetupEditorPlugin.INSTANCE.log(exception);
      }
    }

    public void open(SetupEditor setupEditor, Object object, String property)
    {
      try
      {
        IViewPart propertiesView = page.showView("org.eclipse.ui.views.PropertySheet");
        if (propertiesView instanceof PropertySheet)
        {
          PropertySheet propertySheet = (PropertySheet)propertiesView;
          IPage page = propertySheet.getCurrentPage();
          if (page instanceof ISelectionListener)
          {
            ((ISelectionListener)page).selectionChanged(setupEditor, new StructuredSelection(object));
            Control control = page.getControl();
            if (control instanceof Tree)
            {
              Tree tree = (Tree)control;
              for (TreeItem treeItem : tree.getItems())
              {
                if (property.equals(treeItem.getText()))
                {
                  tree.select(treeItem);
                  break;
                }
              }
            }
          }
        }
      }
      catch (PartInitException exception)
      {
        SetupEditorPlugin.INSTANCE.log(exception);
      }
    }
  }

  protected static class SetupWorkingSetsProvider extends WorkingSetsActionBarContributor.PreviewDialog.WorkingSetsProvider
  {
    @Override
    protected List<WorkingSet> getWorkingSets(IStructuredSelection selection)
    {
      for (Object object : selection.toArray())
      {
        if (object instanceof EObject)
        {
          for (EObject eObject = (EObject)object; eObject != null; eObject = eObject.eContainer())
          {
            List<WorkingSet> workingSets = getWorkingSets(eObject);
            if (workingSets != null)
            {
              return workingSets;
            }
          }
        }
      }

      return Collections.emptyList();
    }

    private List<WorkingSet> getWorkingSets(EObject eObject)
    {
      for (EReference eReference : eObject.eClass().getEAllReferences())
      {
        if (eReference.isMany() && eReference.getEType() == WorkingSetsPackage.Literals.WORKING_SET)
        {
          @SuppressWarnings("unchecked")
          EList<WorkingSet> value = (EList<WorkingSet>)eObject.eGet(eReference);
          return value;
        }
      }

      return null;
    }
  }
}
