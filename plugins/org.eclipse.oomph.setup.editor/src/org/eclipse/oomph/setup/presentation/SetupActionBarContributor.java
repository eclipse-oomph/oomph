/*
 * Copyright (c) 2014-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.base.provider.BaseEditUtil.IconReflectiveItemProvider;
import org.eclipse.oomph.base.provider.ModelElementItemProvider;
import org.eclipse.oomph.base.util.EAnnotations;
import org.eclipse.oomph.internal.ui.GeneralDragAdapter;
import org.eclipse.oomph.internal.ui.OomphEditingDomainActionBarContributor;
import org.eclipse.oomph.internal.ui.OomphTransferDelegate;
import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.EAnnotationConstants;
import org.eclipse.oomph.setup.InstallationTask;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.RedirectionTask;
import org.eclipse.oomph.setup.ResourceCreationTask;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.VariableChoice;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.WorkspaceTask;
import org.eclipse.oomph.setup.impl.DynamicSetupTaskImpl;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.p2.util.MarketPlaceListing;
import org.eclipse.oomph.setup.presentation.SetupEditor.BrowserDialog;
import org.eclipse.oomph.setup.ui.SetupEditorSupport;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.recorder.RecorderManager;
import org.eclipse.oomph.setup.ui.recorder.RecorderTransaction;
import org.eclipse.oomph.setup.util.SetupUtil;
import org.eclipse.oomph.ui.DockableDialog;
import org.eclipse.oomph.ui.DockableDialog.Factory;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.WorkingSetsPackage;
import org.eclipse.oomph.workingsets.presentation.WorkingSetsActionBarContributor;
import org.eclipse.oomph.workingsets.presentation.WorkingSetsActionBarContributor.PreviewDialog;
import org.eclipse.oomph.workingsets.presentation.WorkingSetsActionBarContributor.PreviewDialog.Previewable;
import org.eclipse.oomph.workingsets.presentation.WorkingSetsActionBarContributor.ShowPreviewAction;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xml.type.AnyType;
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
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IStorage;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
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
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
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
  private static final String ENABLEMENT_ITEM_PREFIX = EnablementAction.class.getName() + "-"; //$NON-NLS-1$

  private static final Comparator<? super IAction> ACTION_COMPARATOR = new Comparator<>()
  {
    @Override
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
  protected IAction refreshViewerAction = new Action(SetupEditorPlugin.INSTANCE.getString("_UI_RefreshViewer_menu_item")) //$NON-NLS-1$
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

  protected final WorkingSetsActionBarContributor.ShowPreviewAction showPreviewAction = new ShowPreviewAction(
      Messages.SetupActionBarContributor_action_showWorkingSetsPreview);

  protected final DeleteUnrecognizedContentAction deleteUnrecognizedContentAction = new DeleteUnrecognizedContentAction();

  protected final ShowLaunchConfigurationsAction showLaunchConfigurationAction = new ShowLaunchConfigurationsAction();

  private int lastSubMenuID;

  /**
   * This creates an instance of the contributor.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public SetupActionBarContributor()
  {
    super(ADDITIONS_LAST_STYLE);
    loadResourceAction = new LoadResourceAction();
    validateAction = new ValidateAction();
    liveValidationAction = new DiagnosticDecorator.LiveValidator.LiveValidationAction(SetupEditorPlugin.getPlugin().getDialogSettings());
    controlAction = new ControlAction()
    {
      @Override
      public void run()
      {
        super.run();

        if (command != null && domain instanceof AdapterFactoryEditingDomain)
        {
          for (Object object : command.getResult())
          {
            if (object instanceof EObject)
            {
              Resource resource = ((EObject)object).eResource();
              if (resource != null)
              {
                // Ensure that this resource can be saved.
                ((AdapterFactoryEditingDomain)domain).getResourceToReadOnlyMap().put(resource, Boolean.FALSE);
              }
            }
          }
        }
      }
    };
  }

  @Override
  public void init(IActionBars actionBars)
  {
    validateAction = null;

    showPropertiesViewAction.setPage(getPage());
    liveValidationAction.setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("live_validation")); //$NON-NLS-1$
    refreshViewerAction.setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("refresh_view")); //$NON-NLS-1$
    controlAction.setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("control")); //$NON-NLS-1$

    loadResourceAction = new LoadResourceAction()
    {
      @Override
      public void run()
      {
        ResourceSet resourceSet = domain.getResourceSet();
        EList<Resource> resources = resourceSet.getResources();
        final Set<URI> loadedURIs = new LinkedHashSet<>();
        LoadResourceDialog loadResourceDialog = new LoadResourceDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), domain)
        {
          @Override
          public List<URI> getURIs()
          {
            List<URI> uris = super.getURIs();
            for (ListIterator<URI> it = uris.listIterator(); it.hasNext();)
            {
              URI uri = it.next();
              MarketPlaceListing marketPlaceListing = MarketPlaceListing.getMarketPlaceListing(uri, domain.getResourceSet().getURIConverter());
              if (marketPlaceListing != null)
              {
                it.set(marketPlaceListing.getListing());
              }
            }

            loadedURIs.clear();
            loadedURIs.addAll(uris);

            return uris;
          }
        };

        if (loadResourceDialog.open() == IDialogConstants.OK_ID)
        {
          List<Resource> loadedResources = new ArrayList<>();
          synchronized (resourceSet)
          {
            int index = 0;
            for (URI uri : loadedURIs)
            {
              Resource resource = resourceSet.getResource(uri, false);
              if (resource != null)
              {
                loadedResources.add(resource);
                resources.move(++index, resource);
              }
            }
          }

          if (!loadedResources.isEmpty())
          {
            if (!toggleViewerInputAction.isChecked())
            {
              toggleViewerInputAction.run();
            }

            toggleViewerInputAction.select(new StructuredSelection(loadedResources));
          }
        }
      }
    };

    loadResourceAction.setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("load")); //$NON-NLS-1$
    loadResourceAction.setId("load"); //$NON-NLS-1$

    controlAction.setId("control"); //$NON-NLS-1$
    liveValidationAction.setId("live"); //$NON-NLS-1$

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
    toolBarManager.add(new Separator("setup-settings")); //$NON-NLS-1$
    toolBarManager.add(recordPreferencesAction);
    toolBarManager.add(capturePreferencesAction);
    toolBarManager.add(importPreferencesAction);
    toolBarManager.add(commandTableAction);
    toolBarManager.add(editorTableAction);
    // toolBarManager.add(testInstallAction);
    toolBarManager.add(showInformationBrowserAction);
    toolBarManager.add(showPreviewAction);
    toolBarManager.add(showLaunchConfigurationAction);
    toolBarManager.add(toggleViewerInputAction);
    super.contributeToToolBar(toolBarManager);
    toolBarManager.add(new Separator("setup-additions")); //$NON-NLS-1$
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

    IMenuManager submenuManager = new MenuManager(SetupEditorPlugin.INSTANCE.getString("_UI_SetupEditor_menu"), "org.eclipse.oomph.setupMenuID"); //$NON-NLS-1$ //$NON-NLS-2$
    menuManager.insertAfter("additions", submenuManager); //$NON-NLS-1$
    submenuManager.add(new Separator("settings")); //$NON-NLS-1$
    submenuManager.add(new Separator("actions")); //$NON-NLS-1$
    submenuManager.add(new Separator("additions")); //$NON-NLS-1$
    submenuManager.add(new Separator("additions-end")); //$NON-NLS-1$

    // Prepare for CreateChild item addition or removal.
    //
    createChildMenuManager = new MenuManager(SetupEditorPlugin.INSTANCE.getString("_UI_CreateChild_menu_item")); //$NON-NLS-1$
    submenuManager.insertBefore("additions", createChildMenuManager); //$NON-NLS-1$

    // Prepare for CreateSibling item addition or removal.
    //
    createSiblingMenuManager = new MenuManager(SetupEditorPlugin.INSTANCE.getString("_UI_CreateSibling_menu_item")); //$NON-NLS-1$
    submenuManager.insertBefore("additions", createSiblingMenuManager); //$NON-NLS-1$

    // Force an update because Eclipse hides empty menus now.
    //
    submenuManager.addMenuListener(new IMenuListener()
    {
      @Override
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

    ReflectUtil.setValue("image", createChildMenuManager, SetupEditorPlugin.INSTANCE.getImageDescriptor("create_child")); //$NON-NLS-1$ //$NON-NLS-2$
    ReflectUtil.setValue("image", createSiblingMenuManager, SetupEditorPlugin.INSTANCE.getImageDescriptor("create_sibling")); //$NON-NLS-1$ //$NON-NLS-2$
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
    showLaunchConfigurationAction.setActiveWorkbenchPart(part);
    showInformationBrowserAction.setActiveWorkbenchPart(part);
    deleteUnrecognizedContentAction.setActiveWorkbenchPart(part);

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

  @Override
  public void selectionChanged(SelectionChangedEvent event)
  {
    selectionChangedGen(event);
    // recordPreferencesAction.selectionChanged(event);
    openInSetupEditorAction.selectionChanged(event);
    openInTextEditorAction.selectionChanged(event);
    deleteUnrecognizedContentAction.selectionChanged(event);
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
    Collection<IAction> actions = new ArrayList<>();
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
    return addSpecializedAnnotationCreationActions(descriptors, selection, false, addEnablementActions(descriptors, selection, false, actions));
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
    Collection<IAction> actions = new ArrayList<>();
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
    return addSpecializedAnnotationCreationActions(descriptors, selection, true, addEnablementActions(descriptors, selection, true, actions));
  }

  private Collection<IAction> addSpecializedAnnotationCreationActions(Collection<?> descriptors, ISelection selection, boolean sibling,
      Collection<IAction> actions)
  {
    actions = new ArrayList<>(actions);
    if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1)
    {
      EditingDomain domain = ((IEditingDomainProvider)activeEditorPart).getEditingDomain();
      Object object = ((IStructuredSelection)selection).getFirstElement();
      if (sibling)
      {
        object = domain.getParent(object);
      }

      if (object instanceof VariableTask)
      {
        VariableTask variable = (VariableTask)object;
        if (variable.getAnnotation(AnnotationConstants.ANNOTATION_GLOBAL_VARIABLE) == null)
        {
          String name = variable.getName();
          if (name != null && !name.startsWith("*")) //$NON-NLS-1$
          {
            Annotation annotation = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_GLOBAL_VARIABLE);
            CommandParameter descriptor = new CommandParameter(null, BasePackage.Literals.MODEL_ELEMENT__ANNOTATIONS, annotation);
            Action action = sibling ? new CreateSiblingAction(domain, selection, descriptor) : new CreateChildAction(domain, selection, descriptor);
            action.setText(action.getText() + " - " + AnnotationConstants.ANNOTATION_GLOBAL_VARIABLE); //$NON-NLS-1$
            actions.add(action);
          }
        }

        if (variable.getAnnotation(AnnotationConstants.ANNOTATION_SIMPLE_MODE_DEFAULT_VARIABLE) == null)
        {
          String name = variable.getName();
          if (name != null && !name.startsWith("*") && (variable.getDefaultValue() != null || !variable.getChoices().isEmpty())) //$NON-NLS-1$
          {
            Annotation annotation = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_SIMPLE_MODE_DEFAULT_VARIABLE);
            CommandParameter descriptor = new CommandParameter(null, BasePackage.Literals.MODEL_ELEMENT__ANNOTATIONS, annotation);
            Action action = sibling ? new CreateSiblingAction(domain, selection, descriptor) : new CreateChildAction(domain, selection, descriptor);
            action.setText(action.getText() + " - " + AnnotationConstants.ANNOTATION_SIMPLE_MODE_DEFAULT_VARIABLE); //$NON-NLS-1$
            actions.add(action);
          }
        }

        if (variable.getAnnotation(AnnotationConstants.ANNOTATION_INHERITED_CHOICES) == null)
        {
          Annotation annotation = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_INHERITED_CHOICES);
          annotation.getDetails().put(AnnotationConstants.KEY_INHERIT, ""); //$NON-NLS-1$
          CommandParameter descriptor = new CommandParameter(null, BasePackage.Literals.MODEL_ELEMENT__ANNOTATIONS, annotation);
          Action action = sibling ? new CreateSiblingAction(domain, selection, descriptor) : new CreateChildAction(domain, selection, descriptor);
          action.setText(action.getText() + " - " + AnnotationConstants.ANNOTATION_INHERITED_CHOICES); //$NON-NLS-1$
          actions.add(action);
        }
      }
      else if (object instanceof ModelElement)
      {
        ModelElement modelElement = (ModelElement)object;
        if (modelElement.getAnnotation(AnnotationConstants.ANNOTATION_FEATURE_SUBSTITUTION) == null
            && !getFeatureSubstitutionAttributes(modelElement).isEmpty())
        {
          Annotation annotation = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_FEATURE_SUBSTITUTION);
          CommandParameter descriptor = new CommandParameter(null, BasePackage.Literals.MODEL_ELEMENT__ANNOTATIONS, annotation);
          Action action = sibling ? new CreateSiblingAction(domain, selection, descriptor) : new CreateChildAction(domain, selection, descriptor);
          action.setText(action.getText() + " - " + AnnotationConstants.ANNOTATION_FEATURE_SUBSTITUTION); //$NON-NLS-1$
          actions.add(action);
        }

        if (modelElement instanceof Annotation)
        {
          Annotation annotation = (Annotation)modelElement;
          if (AnnotationConstants.ANNOTATION_FEATURE_SUBSTITUTION.equals(annotation.getSource()))
          {
            EMap<String, String> details = annotation.getDetails();
            Set<EAttribute> featureSubstitutionAttributes = getFeatureSubstitutionAttributes(annotation.getModelElement());
            for (EAttribute eAttribute : featureSubstitutionAttributes)
            {
              String name = eAttribute.getName();
              if (!details.containsKey(name))
              {
                Map.Entry<String, String> detail = BaseFactory.eINSTANCE.createStringToStringMapEntry();
                ((EObject)detail).eSet(BasePackage.Literals.STRING_TO_STRING_MAP_ENTRY__KEY, name);
                CommandParameter descriptor = new CommandParameter(null, BasePackage.Literals.ANNOTATION__DETAILS, detail);
                Action action = sibling ? new CreateSiblingAction(domain, selection, descriptor) : new CreateChildAction(domain, selection, descriptor);
                action.setText(action.getText() + " - " + name); //$NON-NLS-1$
                actions.add(action);
              }
            }
          }
        }
        else if (modelElement instanceof SetupTask)
        {
          if (modelElement.getAnnotation(AnnotationConstants.ANNOTATION_INDUCED_CHOICES) == null)
          {
            Annotation annotation = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_INDUCED_CHOICES);
            annotation.getDetails().put(AnnotationConstants.KEY_INHERIT, ""); //$NON-NLS-1$
            annotation.getDetails().put(AnnotationConstants.KEY_LABEL, ""); //$NON-NLS-1$
            annotation.getDetails().put(AnnotationConstants.KEY_TARGET, ""); //$NON-NLS-1$
            annotation.getDetails().put(AnnotationConstants.KEY_DESCRIPTION, ""); //$NON-NLS-1$
            CommandParameter descriptor = new CommandParameter(null, BasePackage.Literals.MODEL_ELEMENT__ANNOTATIONS, annotation);
            Action action = sibling ? new CreateSiblingAction(domain, selection, descriptor) : new CreateChildAction(domain, selection, descriptor);
            action.setText(action.getText() + " - " + AnnotationConstants.ANNOTATION_INDUCED_CHOICES); //$NON-NLS-1$
            actions.add(action);
          }
        }
        else if (modelElement instanceof VariableChoice)
        {
          if (modelElement.getAnnotation(AnnotationConstants.ANNOTATION_MATCH_CHOICE) == null)
          {
            Annotation annotation = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_MATCH_CHOICE);
            CommandParameter descriptor = new CommandParameter(null, BasePackage.Literals.MODEL_ELEMENT__ANNOTATIONS, annotation);
            Action action = sibling ? new CreateSiblingAction(domain, selection, descriptor) : new CreateChildAction(domain, selection, descriptor);
            action.setText(action.getText() + " - " + AnnotationConstants.ANNOTATION_MATCH_CHOICE); //$NON-NLS-1$
            actions.add(action);
          }
        }
        else if (modelElement instanceof ProjectCatalog || modelElement instanceof Project || modelElement instanceof ProductCatalog
            || modelElement instanceof Product || modelElement instanceof ProductVersion)
        {
          if (modelElement.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO) == null)
          {
            Annotation annotation = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);

            EMap<String, String> details = annotation.getDetails();
            if (modelElement instanceof ProductCatalog || modelElement instanceof Product || modelElement instanceof ProductVersion)
            {
              details.put(AnnotationConstants.KEY_README_PATH, null);
              details.put(AnnotationConstants.KEY_FOLDER_NAME, null);
            }

            details.put(AnnotationConstants.KEY_IMAGE_URI, null);
            details.put(AnnotationConstants.KEY_SITE_URI, null);

            CommandParameter descriptor = new CommandParameter(null, BasePackage.Literals.MODEL_ELEMENT__ANNOTATIONS, annotation);
            Action action = sibling ? new CreateSiblingAction(domain, selection, descriptor) : new CreateChildAction(domain, selection, descriptor);
            action.setText(action.getText() + " - " + AnnotationConstants.ANNOTATION_BRANDING_INFO); //$NON-NLS-1$
            actions.add(action);
          }

          if (modelElement instanceof Project || modelElement instanceof Product)
          {
            if (modelElement.getAnnotation(AnnotationConstants.ANNOTATION_CONFIGURATION_REFERENCE) == null)
            {
              Annotation annotation = BaseFactory.eINSTANCE.createAnnotation(AnnotationConstants.ANNOTATION_CONFIGURATION_REFERENCE);
              CommandParameter descriptor = new CommandParameter(null, BasePackage.Literals.MODEL_ELEMENT__ANNOTATIONS, annotation);
              Action action = sibling ? new CreateSiblingAction(domain, selection, descriptor) : new CreateChildAction(domain, selection, descriptor);
              action.setText(action.getText() + " - " + AnnotationConstants.ANNOTATION_CONFIGURATION_REFERENCE); //$NON-NLS-1$
              actions.add(action);
            }
          }
        }
      }
    }

    return actions;
  }

  private Set<EAttribute> getFeatureSubstitutionAttributes(ModelElement modelElement)
  {
    Set<EAttribute> result = new LinkedHashSet<>();

    for (EAttribute eAttribute : modelElement.eClass().getEAllAttributes())
    {
      if (eAttribute.isChangeable() && eAttribute.getEAnnotation(EAnnotationConstants.ANNOTATION_NO_EXPAND) == null)
      {
        String instanceClassName = eAttribute.getEAttributeType().getInstanceClassName();
        if (!"java.lang.String".equals(instanceClassName) && !"org.eclipse.emf.common.util.URI".equals(instanceClassName)) //$NON-NLS-1$ //$NON-NLS-2$
        {
          result.add(eAttribute);
        }
      }
    }

    return result;
  }

  private Collection<IAction> addEnablementActions(Collection<?> descriptors, ISelection selection, boolean sibling, Collection<IAction> actions)
  {
    if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1)
    {
      Object object = ((IStructuredSelection)selection).getFirstElement();
      if (object instanceof EObject)
      {
        actions = new ArrayList<>(actions);
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
    manager.add(new Separator("elements")); //$NON-NLS-1$
    manager.add(new Separator("elements-end")); //$NON-NLS-1$
    manager.add(new Separator("groups")); //$NON-NLS-1$
    manager.add(new Separator("groups-end")); //$NON-NLS-1$
    manager.add(new Separator("scopes")); //$NON-NLS-1$
    manager.add(new Separator("scopes")); //$NON-NLS-1$
    manager.add(new Separator("scopes-end")); //$NON-NLS-1$
    manager.add(new Separator("defaults")); //$NON-NLS-1$
    manager.add(new Separator("defaults-end")); //$NON-NLS-1$
    manager.add(new Separator("installations")); //$NON-NLS-1$
    manager.add(new Separator("installations-end")); //$NON-NLS-1$
    manager.add(new Separator("tasks")); //$NON-NLS-1$
    manager.add(new Separator("tasks-end")); //$NON-NLS-1$
    manager.add(new Separator("annotations")); //$NON-NLS-1$
    manager.add(new Separator("annotations-end")); //$NON-NLS-1$
    manager.add(new Separator("additions")); //$NON-NLS-1$
    manager.add(new Separator("additions-end")); //$NON-NLS-1$

    List<IAction> elements = new ArrayList<>();
    List<IAction> scopes = new ArrayList<>();
    List<IAction> defaults = new ArrayList<>();
    List<IAction> installations = new ArrayList<>();
    List<IAction> tasks = new ArrayList<>();
    List<IAction> annotations = new ArrayList<>();
    List<IAction> additions = new ArrayList<>();
    List<EnablementAction> additionalTasks = new ArrayList<>();
    List<EnablementAction> additionalElements = new ArrayList<>();
    Map<String, MenuManager> submenuManagers = new LinkedHashMap<>();

    Set<String> installedClasses = new HashSet<>();

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

      if (descriptor instanceof ModelElementItemProvider.GroupingChildCommandParameter)
      {
        String group = ((ModelElementItemProvider.GroupingChildCommandParameter)descriptor).getGroup();
        MenuManager submenuManager = submenuManagers.computeIfAbsent(contributionID, it -> new MenuManager(group, action.getImageDescriptor(), null));

        submenuManager.add(action);
      }
      else if (descriptor instanceof CommandParameter)
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

    populateManagerGen(manager, elements, "elements-end"); //$NON-NLS-1$

    for (MenuManager submenuManager : submenuManagers.values())
    {
      manager.insertBefore("groups-end", submenuManager); //$NON-NLS-1$
    }

    if (!additionalElements.isEmpty())
    {
      populateManagerEnablements(manager, SetupEditorPlugin.INSTANCE.getString("_UI_AdditionalElements_menu_item"), "elements-end", additionalElements); //$NON-NLS-1$ //$NON-NLS-2$
    }

    populateManagerGen(manager, scopes, "scopes-end"); //$NON-NLS-1$
    populateManagerGen(manager, defaults, "defaults-end"); //$NON-NLS-1$
    populateManagerGen(manager, installations, "installations-end"); //$NON-NLS-1$
    populateManagerGen(manager, tasks, "tasks-end"); //$NON-NLS-1$

    if (!additionalTasks.isEmpty())
    {
      populateManagerEnablements(manager, SetupEditorPlugin.INSTANCE.getString("_UI_AdditionalTasks_menu_item"), "tasks-end", additionalTasks); //$NON-NLS-1$ //$NON-NLS-2$
    }

    populateManagerGen(manager, annotations, "annotations-end"); //$NON-NLS-1$
    populateManagerGen(manager, additions, "additions-end"); //$NON-NLS-1$
  }

  private String getInstalledClass(EClass eClass)
  {
    return eClass.getEPackage().getNsURI() + "#" + eClass.getName(); //$NON-NLS-1$
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
      @Override
      public void menuAboutToShow(IMenuManager manager)
      {
        // Execute later in event loop to make sure the menu is visible when the first image is loaded.
        UIUtil.asyncExec(new Runnable()
        {
          @Override
          public void run()
          {
            final Queue<EnablementAction> queue = new ConcurrentLinkedQueue<>(additionalTasks);
            int jobs = Math.max(queue.size(), 10);

            for (int i = 0; i < jobs; i++)
            {
              Job iconLoader = new Job("IconLoader-" + i) //$NON-NLS-1$
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
        if (item instanceof MenuManager)
        {
          manager.remove(item);
        }
        else
        {
          String id = item.getId();
          if (id != null && id.startsWith(ENABLEMENT_ITEM_PREFIX))
          {
            manager.remove(item);
          }
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

    submenuManager = new MenuManager(SetupEditorPlugin.INSTANCE.getString("_UI_CreateChild_menu_item"), //$NON-NLS-1$
        SetupEditorPlugin.INSTANCE.getImageDescriptor("create_child"), "CreateChild"); //$NON-NLS-1$ //$NON-NLS-2$
    populateManager(submenuManager, createChildActions, null);
    menuManager.insertBefore("edit", submenuManager); //$NON-NLS-1$

    submenuManager = new MenuManager(SetupEditorPlugin.INSTANCE.getString("_UI_CreateSibling_menu_item"), //$NON-NLS-1$
        SetupEditorPlugin.INSTANCE.getImageDescriptor("create_sibling"), "CreateSibling"); //$NON-NLS-1$ //$NON-NLS-2$
    populateManager(submenuManager, createSiblingActions, null);
    menuManager.insertBefore("edit", submenuManager); //$NON-NLS-1$

    if (deleteUnrecognizedContentAction.isApplicable())
    {
      menuManager.insertBefore("edit", deleteUnrecognizedContentAction); //$NON-NLS-1$
    }

    menuManager.insertBefore("ui-actions", openInSetupEditorAction); //$NON-NLS-1$
    menuManager.insertBefore("ui-actions", openInTextEditorAction); //$NON-NLS-1$
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

  @Override
  protected void addGlobalActions(IMenuManager menuManager)
  {
    addGlobalActionsGen(menuManager);
    showTooltipsAction.setChecked(isShowTooltips());
    menuManager.insertAfter("live", new ActionContributionItem(showTooltipsAction)); //$NON-NLS-1$
    menuManager.insertBefore("properties", new ActionContributionItem(showInformationBrowserAction)); //$NON-NLS-1$
    menuManager.insertBefore("properties", showPreviewAction); //$NON-NLS-1$
    menuManager.insertBefore("properties", showLaunchConfigurationAction); //$NON-NLS-1$
    menuManager.insertBefore("load", menuManager.remove("control")); //$NON-NLS-1$ //$NON-NLS-2$
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

  @Override
  public void dispose()
  {
    showLaunchConfigurationAction.dispose();
    super.dispose();
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
      super(Messages.SetupActionBarContributor_action_recordPreferences, SetupEditorPlugin.INSTANCE.getImageDescriptor("preference_recorder")); //$NON-NLS-1$
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
    ISelection selection = editor.getSelection();
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
      super(
          fromEclipsePreferenceFile ? Messages.SetupActionBarContributor_action_preferenceCapture_importPrefs
              : Messages.SetupActionBarContributor_action_preferenceCapture_capturePrefs,
          SetupEditorPlugin.INSTANCE.getImageDescriptor(fromEclipsePreferenceFile ? "preference_importer" : "preference_picker")); //$NON-NLS-1$ //$NON-NLS-2$
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
          RecorderManager.INSTANCE.setTemporaryRecorderTarget(null);
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
      super(Messages.SetupActionBarContributor_action_testInstall, AS_PUSH_BUTTON);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("run")); //$NON-NLS-1$
      setToolTipText(Messages.SetupActionBarContributor_action_testInstall_tooltip);
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
      super(Messages.SetupActionBarContributor_action_commandTable);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("commands")); //$NON-NLS-1$
      setToolTipText(Messages.SetupActionBarContributor_action_commandTable_tooltip);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String renderHTML()
    {
      IBindingService bindingService = UIUtil.getService(PlatformUI.getWorkbench(), IBindingService.class);
      Binding[] bindings = bindingService.getBindings();
      Map<String, List<Command>> map = new HashMap<>();

      ICommandService commandService = UIUtil.getService(PlatformUI.getWorkbench(), ICommandService.class);
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
            commands = new ArrayList<>();
            map.put(category, commands);
          }

          commands.add(command);
        }
        catch (NotDefinedException ex)
        {
          SetupEditorPlugin.getPlugin().log(ex);
        }
      }

      List<String> categories = new ArrayList<>(map.keySet());
      Collections.sort(categories);

      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(baos);
      out.println("<table border=\"1\">"); //$NON-NLS-1$

      for (String category : categories)
      {
        out.println("<tr><td colspan=\"3\" bgcolor=\"eae6ff\"><br><h2>" + category + "</h2></td></tr>"); //$NON-NLS-1$ //$NON-NLS-2$

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
                  keys.append("<br>"); //$NON-NLS-1$
                }

                keys.append(binding.getTriggerSequence());
              }
            }
          }

          if (keys.length() == 0)
          {
            keys.append("&nbsp;"); //$NON-NLS-1$
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

          out.println("<tr><td valign=\"top\" width=\"200\">" + name + "</td><td valign=\"top\" width=\"400\">" + command.getId() //$NON-NLS-1$ //$NON-NLS-2$
              + "</td><td valign=\"top\" width=\"100\">" + keys + "</td></tr>"); //$NON-NLS-1$ //$NON-NLS-2$
        }
      }

      out.println("</table>"); //$NON-NLS-1$

      try
      {
        out.flush();
        return baos.toString("UTF-8"); //$NON-NLS-1$
      }
      catch (UnsupportedEncodingException ex)
      {
        return Messages.SetupActionBarContributor_html_Utf8IsUnsupported;
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
      return new Factory<>()
      {
        @Override
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
        return SetupEditorPlugin.INSTANCE.getDialogSettings("CommandTable"); //$NON-NLS-1$
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
      super(Messages.SetupActionBarContributor_action_editorTable);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("FileEditor")); //$NON-NLS-1$
      setToolTipText(Messages.SetupActionBarContributor_action_editorTable_tooltip);
    }

    @SuppressWarnings("restriction")
    @Override
    protected String renderHTML()
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream out = new PrintStream(baos);
      out.println("<table border=\"1\">"); //$NON-NLS-1$
      out.println("<tr><td bgcolor=\"eae6ff\">ID</td><td bgcolor=\"eae6ff\">Label</td></tr>"); //$NON-NLS-1$

      org.eclipse.ui.internal.registry.EditorRegistry registry = (org.eclipse.ui.internal.registry.EditorRegistry)PlatformUI.getWorkbench().getEditorRegistry();
      IEditorDescriptor[] editors = registry.getSortedEditorsFromPlugins();
      for (IEditorDescriptor editor : editors)
      {
        out.println("<tr><td>" + editor.getId() + "</td><td>" + editor.getLabel() + "</td></tr>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      }

      out.println("</table>"); //$NON-NLS-1$

      try
      {
        out.flush();
        return baos.toString("UTF-8"); //$NON-NLS-1$
      }
      catch (UnsupportedEncodingException ex)
      {
        return Messages.SetupActionBarContributor_html_Utf8IsUnsupported;
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
      return new Factory<>()
      {
        @Override
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
        return SetupEditorPlugin.INSTANCE.getDialogSettings("EditorTable"); //$NON-NLS-1$
      }
    }
  }

  protected static final class ToggleViewerInputAction extends Action
  {
    private SetupEditor setupEditor;

    public ToggleViewerInputAction()
    {
      super(Messages.SetupActionBarContributor_action_showResources, IAction.AS_CHECK_BOX);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("ToggleInput")); //$NON-NLS-1$
      setToolTipText(Messages.SetupActionBarContributor_action_showResources_tooltip);
    }

    @Override
    public void run()
    {
      setupEditor.toggleInput();
    }

    public void select(ISelection selection)
    {
      setupEditor.selectionViewer.setSelection(selection, true);
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
      setText(Messages.SetupActionBarContributor_action_openInSetupEditor);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("edit_setup")); //$NON-NLS-1$
    }

    @Override
    public void run()
    {
      open(uri);
    }

    public void open(Object object)
    {
      final URI uri = getEditURI(object, true);
      if ("performer".equals(uri.scheme())) //$NON-NLS-1$
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
      setText(Messages.SetupActionBarContributor_action_openInTextEditor);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("edit_text")); //$NON-NLS-1$
    }

    @Override
    public void run()
    {
      if ("performer".equals(uri.scheme())) //$NON-NLS-1$
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
        @Override
        @SuppressWarnings("all")
        public Object getAdapter(Class adapter)
        {
          if (adapter == IStorage.class)
          {
            try
            {
              return getStorage();
            }
            catch (CoreException ex)
            {
            }
          }

          return null;
        }

        @Override
        public String getToolTipText()
        {
          return uri.toString();
        }

        @Override
        public IPersistableElement getPersistable()
        {
          return null;
        }

        @Override
        public String getName()
        {
          return uri.lastSegment();
        }

        @Override
        public ImageDescriptor getImageDescriptor()
        {
          return null;
        }

        @Override
        public boolean exists()
        {
          return true;
        }

        @Override
        public IStorage getStorage() throws CoreException
        {
          return new IStorage()
          {
            @Override
            @SuppressWarnings("all")
            public Object getAdapter(Class adapter)
            {
              return null;
            }

            @Override
            public boolean isReadOnly()
            {
              return true;
            }

            @Override
            public String getName()
            {
              return uri.lastSegment();
            }

            @Override
            public IPath getFullPath()
            {
              return new Path(uri.toString());
            }

            @Override
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
      if ("performer".equals(uri.scheme())) //$NON-NLS-1$
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

  /**
   * @author Ed Merks
   */
  private static class DeleteUnrecognizedContentAction extends Action
  {
    private XMLResource resource;

    private SetupEditor setupEditor;

    public DeleteUnrecognizedContentAction()
    {
      setText(Messages.SetupActionBarContributor_action_deleteUnrecognizedContent);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("remove_unrecognized_content")); //$NON-NLS-1$
    }

    @Override
    public void run()
    {
      EditingDomain editingDomain = setupEditor.getEditingDomain();
      AbstractCommand command = new AbstractCommand(getText())
      {
        private Map<EObject, AnyType> eObjectToExtensionMap;

        @Override
        protected boolean prepare()
        {
          eObjectToExtensionMap = new LinkedHashMap<>(resource.getEObjectToExtensionMap());
          return true;
        }

        @Override
        public void execute()
        {
          resource.getEObjectToExtensionMap().clear();
        }

        @Override
        public void undo()
        {
          resource.getEObjectToExtensionMap().putAll(eObjectToExtensionMap);
        }

        @Override
        public void redo()
        {
          execute();
        }
      };

      editingDomain.getCommandStack().execute(command);
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

    public boolean isApplicable()
    {
      return resource != null;
    }

    public final void selectionChanged(SelectionChangedEvent event)
    {
      resource = null;

      IStructuredSelection selection = (IStructuredSelection)event.getSelection();
      if (selection.size() == 1)
      {
        Object element = selection.getFirstElement();
        if (element instanceof XMLResource)
        {
          resource = (XMLResource)element;

        }
      }

      setEnabled(resource != null && !resource.getEObjectToExtensionMap().isEmpty());
    }
  }

  public static boolean isShowTooltips()
  {
    return SetupUIPlugin.INSTANCE.getInstancePreference("showTooltips").get(true); //$NON-NLS-1$
  }

  public static void setShowTooltips(boolean showTooltips)
  {
    SetupUIPlugin.INSTANCE.getInstancePreference("showTooltips").set(showTooltips); //$NON-NLS-1$
  }

  /**
   * @author Ed Merks
   */
  private static final class ShowTooltipsAction extends Action
  {
    public ShowTooltipsAction()
    {
      setText(Messages.SetupActionBarContributor_action_showTooltips);
      setChecked(isShowTooltips());
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("show_tooltips")); //$NON-NLS-1$
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
      super(Messages.SetupActionBarContributor_action_showInformationBrowser, Action.AS_CHECK_BOX);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("open_browser")); //$NON-NLS-1$
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
      super(SetupEditorPlugin.INSTANCE.getString("_UI_ShowPropertiesView_menu_item"), SetupEditorPlugin.INSTANCE.getImageDescriptor("show_properties_view")); //$NON-NLS-1$ //$NON-NLS-2$
      setId("properties"); //$NON-NLS-1$
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
        page.showView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
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
        IViewPart propertiesView = page.showView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
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

  public static class LaunchConfigurationsDialog extends Dialog
  {
    private static final Class<?> LAUNCH_VIEW_MODEL_CLASS;

    private static final Constructor<IContentProvider> LAUNCH_VIEW_CONTENT_PROVIDER_CONSTRUCTOR;

    private static final Constructor<IStyledLabelProvider> LAUNCH_VIEW_LABEL_PROVIDER_CONSTRUCTOR;

    static
    {
      Class<?> launchViewModelClass = null;
      Constructor<IContentProvider> launchViewContentProviderConstructor = null;
      Constructor<IStyledLabelProvider> launchViewLabelProviderConstructor = null;

      try
      {
        @SuppressWarnings({ "unchecked", "nls", "unused" })
        Object it = new Object[] {
            launchViewModelClass = CommonPlugin.loadClass("org.eclipse.debug.ui.launchview", "org.eclipse.debug.ui.launchview.internal.model.LaunchViewModel"),
            launchViewContentProviderConstructor = (Constructor<IContentProvider>)CommonPlugin
                .loadClass("org.eclipse.debug.ui.launchview", "org.eclipse.debug.ui.launchview.internal.view.LaunchViewContentProvider").getConstructor(),
            launchViewLabelProviderConstructor = (Constructor<IStyledLabelProvider>)CommonPlugin
                .loadClass("org.eclipse.debug.ui.launchview", "org.eclipse.debug.ui.launchview.internal.view.LaunchViewLabelProvider").getConstructor() //
        };
      }
      catch (ClassNotFoundException | NoSuchMethodException | SecurityException ex)
      {
        //$FALL-THROUGH$
      }

      LAUNCH_VIEW_MODEL_CLASS = launchViewModelClass;
      LAUNCH_VIEW_CONTENT_PROVIDER_CONSTRUCTOR = launchViewContentProviderConstructor;
      LAUNCH_VIEW_LABEL_PROVIDER_CONSTRUCTOR = launchViewLabelProviderConstructor;
    }

    public static boolean IS_AVAILBLE = LAUNCH_VIEW_MODEL_CLASS != null && LAUNCH_VIEW_CONTENT_PROVIDER_CONSTRUCTOR != null
        && LAUNCH_VIEW_LABEL_PROVIDER_CONSTRUCTOR != null;

    protected TreeViewer tree;

    protected ISelectionProvider activePart;

    public LaunchConfigurationsDialog(IWorkbenchWindow workbenchWindow)
    {
      super(workbenchWindow);
      setShellStyle(SWT.MODELESS | SWT.DIALOG_TRIM | SWT.RESIZE);
    }

    @Override
    protected IDialogSettings getDialogBoundsSettings()
    {
      return SetupEditorPlugin.INSTANCE.getDialogSettings("LaunchConfigurations"); //$NON-NLS-1$
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
      getShell().setText(Messages.SetupActionBarContributor_LaunchConfiguration_title);

      tree = new TreeViewer(parent);
      tree.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);

      List<? extends OomphTransferDelegate> dndDelegates = Arrays
          .asList(new OomphTransferDelegate[] { new OomphTransferDelegate.URLTransferDelegate(), new OomphTransferDelegate.TextTransferDelegate() });
      Transfer[] dndTransfers = new Transfer[] { dndDelegates.get(0).getTransfer(), dndDelegates.get(1).getTransfer() };
      tree.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK, dndTransfers,
          new GeneralDragAdapter(tree, new GeneralDragAdapter.DraggedObjectsFactory()
          {
            @Override
            public List<Object> createDraggedObjects(ISelection selection) throws Exception
            {
              List<Object> result = new ArrayList<>();
              for (Object object : ((IStructuredSelection)selection).toArray())
              {
                Object convertedObject = convertLaunchViewObject(object);
                if (convertedObject != null)
                {
                  result.add(convertedObject);
                }
              }

              return result;
            }
          }, dndDelegates));

      init();

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

    @SuppressWarnings("nls")
    protected Object convertLaunchViewObject(Object object)
    {
      try
      {
        Object launchObject = ReflectUtil.invokeMethod("getObject", object);
        if (launchObject != null)
        {
          Object value = ReflectUtil.getValue("config", launchObject);
          IFileStore fileStore = ReflectUtil.invokeMethod("getFileStore", value);

          // Load the contents and escape and string substitutions.
          String content = new String(fileStore.openInputStream(EFS.NONE, null).readAllBytes(), StandardCharsets.UTF_8);
          String escapedContent = SetupUtil.escape(content);
          ResourceCreationTask resourceCreationTask = SetupFactory.eINSTANCE.createResourceCreationTask();
          resourceCreationTask.setContent(escapedContent);

          java.net.URI uri = fileStore.toURI();
          IFile[] filesForLocationURI = ResourcesUtil.findFilesForLocationURI(uri);
          if (filesForLocationURI.length != 0)
          {
            resourceCreationTask.setTargetURL(URI.createPlatformResourceURI(filesForLocationURI[0].getFullPath().toString(), true).toString());
          }
          else
          {
            java.net.URI workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getLocationURI();
            java.net.URI workspaceRelativeLocation = workspaceLocation.relativize(uri);
            if (workspaceRelativeLocation.toString().startsWith(".metadata"))
            {
              resourceCreationTask.setTargetURL("${workspace.location|uri}/" + workspaceRelativeLocation);
            }
            else
            {
              resourceCreationTask.setTargetURL(uri.toString());
            }
          }

          return resourceCreationTask;
        }
      }
      catch (Exception ex)
      {
        //$FALL-THROUGH$
      }

      return null;
    }

    @SuppressWarnings("nls")
    protected void init()
    {
      try
      {
        IContentProvider contentProvider = LAUNCH_VIEW_CONTENT_PROVIDER_CONSTRUCTOR.newInstance();
        IStyledLabelProvider labelProvider = LAUNCH_VIEW_LABEL_PROVIDER_CONSTRUCTOR.newInstance();

        tree.setContentProvider(contentProvider);
        tree.setLabelProvider(new DelegatingStyledCellLabelProvider(labelProvider));
        tree.setInput(ReflectUtil.invokeMethod("getModel", ReflectUtil.invokeMethod(ReflectUtil.getMethod(LAUNCH_VIEW_MODEL_CLASS, "getService"), null)));
      }
      catch (Exception ex)
      {
        SetupEditorPlugin.getPlugin().log(ex);
      }
    }

    @Override
    protected Control createButtonBar(Composite parent)
    {
      return null;
    }
  }

  public static class ShowLaunchConfigurationsAction extends Action
  {
    private IWorkbenchPart activePart;

    private LaunchConfigurationsDialog launchConfigurationsDialog;

    public ShowLaunchConfigurationsAction()
    {
      super(Messages.SetupActionBarContributor_LaunchConfigurations_label, IAction.AS_PUSH_BUTTON);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("launch_ configurations")); //$NON-NLS-1$
      setEnabled(LaunchConfigurationsDialog.IS_AVAILBLE);
    }

    @Override
    public void run()
    {
      if (launchConfigurationsDialog != null)
      {
        Shell shell = launchConfigurationsDialog.getShell();
        if (shell != null && !shell.isDisposed())
        {
          shell.setActive();
          return;
        }
      }

      launchConfigurationsDialog = new LaunchConfigurationsDialog(activePart.getSite().getWorkbenchWindow());
      launchConfigurationsDialog.setBlockOnOpen(false);
      launchConfigurationsDialog.open();
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

    public void dispose()
    {
      if (launchConfigurationsDialog != null)
      {
        Shell shell = launchConfigurationsDialog.getShell();
        if (shell != null && !shell.isDisposed())
        {
          shell.dispose();
        }
      }
    }
  }
}
