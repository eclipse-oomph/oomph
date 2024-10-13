/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.presentation;

import org.eclipse.oomph.maven.DOMElement;
import org.eclipse.oomph.maven.Realm;
import org.eclipse.oomph.maven.impl.RealmImpl;
import org.eclipse.oomph.maven.impl.RealmImpl.State;
import org.eclipse.oomph.maven.util.MavenValidator.ElementEdit;
import org.eclipse.oomph.maven.util.POMXMLUtil;
import org.eclipse.oomph.maven.util.POMXMLUtil.TextRegion;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.ui.action.CollapseAllAction;
import org.eclipse.emf.edit.ui.action.ControlAction;
import org.eclipse.emf.edit.ui.action.CreateChildAction;
import org.eclipse.emf.edit.ui.action.CreateSiblingAction;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;
import org.eclipse.emf.edit.ui.action.ExpandAllAction;
import org.eclipse.emf.edit.ui.action.FindAction;
import org.eclipse.emf.edit.ui.action.LoadResourceAction;
import org.eclipse.emf.edit.ui.action.RevertAction;
import org.eclipse.emf.edit.ui.action.ValidateAction;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;

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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;

import org.w3c.dom.Document;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This is the action bar contributor for the Maven model editor.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MavenActionBarContributor extends EditingDomainActionBarContributor implements ISelectionChangedListener
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
  protected IAction showPropertiesViewAction = new Action(MavenEditorPlugin.INSTANCE.getString("_UI_ShowPropertiesView_menu_item")) //$NON-NLS-1$
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
        MavenEditorPlugin.INSTANCE.log(exception);
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
  protected IAction refreshViewerAction = new Action(MavenEditorPlugin.INSTANCE.getString("_UI_RefreshViewer_menu_item")) //$NON-NLS-1$
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

  private final List<MavenEditorAction<?>> mavenEditorActions = List.of(//
      new ReconcileRealmAction(), //
      new ApplyElementEditsAction() //
  );

  /**
   * This creates an instance of the contributor.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MavenActionBarContributor()
  {
    super(ADDITIONS_LAST_STYLE);
    loadResourceAction = new LoadResourceAction();
    validateAction = new ValidateAction();
    liveValidationAction = new DiagnosticDecorator.LiveValidator.LiveValidationAction(MavenEditorPlugin.getPlugin().getDialogSettings());
    controlAction = new ControlAction();
    findAction = FindAction.create();
    revertAction = new RevertAction();
    expandAllAction = new ExpandAllAction();
    collapseAllAction = new CollapseAllAction();
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
    toolBarManager.add(new Separator("maven-settings")); //$NON-NLS-1$
    toolBarManager.add(new Separator("maven-additions")); //$NON-NLS-1$
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

    IMenuManager submenuManager = new MenuManager(MavenEditorPlugin.INSTANCE.getString("_UI_MavenEditor_menu"), "org.eclipse.oomph.mavenMenuID"); //$NON-NLS-1$ //$NON-NLS-2$
    menuManager.insertAfter("additions", submenuManager); //$NON-NLS-1$
    submenuManager.add(new Separator("settings")); //$NON-NLS-1$
    submenuManager.add(new Separator("actions")); //$NON-NLS-1$
    submenuManager.add(new Separator("additions")); //$NON-NLS-1$
    submenuManager.add(new Separator("additions-end")); //$NON-NLS-1$

    // Prepare for CreateChild item addition or removal.
    //
    createChildMenuManager = new MenuManager(MavenEditorPlugin.INSTANCE.getString("_UI_CreateChild_menu_item")); //$NON-NLS-1$
    submenuManager.insertBefore("additions", createChildMenuManager); //$NON-NLS-1$

    // Prepare for CreateSibling item addition or removal.
    //
    createSiblingMenuManager = new MenuManager(MavenEditorPlugin.INSTANCE.getString("_UI_CreateSibling_menu_item")); //$NON-NLS-1$
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

  /**
   * When the active editor changes, this remembers the change and registers with it as a selection provider.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setActiveEditorGen(IEditorPart part)
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

  @Override
  public void setActiveEditor(IEditorPart part)
  {
    setActiveEditorGen(part);
    for (MavenEditorAction<?> mavenEditorAction : mavenEditorActions)
    {
      mavenEditorAction.setActiveWorkbenchPart(part);
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

  /**
   * This generates a {@link org.eclipse.emf.edit.ui.action.CreateSiblingAction} for each object in <code>descriptors</code>,
   * and returns the collection of these actions.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<IAction> generateCreateSiblingActions(Collection<?> descriptors, ISelection selection)
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

    submenuManager = new MenuManager(MavenEditorPlugin.INSTANCE.getString("_UI_CreateChild_menu_item")); //$NON-NLS-1$
    populateManager(submenuManager, createChildActions, null);
    menuManager.insertBefore("edit", submenuManager); //$NON-NLS-1$

    submenuManager = new MenuManager(MavenEditorPlugin.INSTANCE.getString("_UI_CreateSibling_menu_item")); //$NON-NLS-1$
    populateManager(submenuManager, createSiblingActions, null);
    menuManager.insertBefore("edit", submenuManager); //$NON-NLS-1$
  }

  @Override
  public void menuAboutToShow(IMenuManager menuManager)
  {
    menuAboutToShowGen(menuManager);
    for (MavenEditorAction<?> mavenEditorAction : mavenEditorActions)
    {
      mavenEditorAction.selectionChanged();
      menuManager.insertBefore("ui-actions", mavenEditorAction); //$NON-NLS-1$
    }
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

  private static abstract class MavenEditorAction<T> extends Action
  {
    protected MavenEditor mavenEditor;

    protected List<T> elements = new ArrayList<>();

    public MavenEditorAction(String label)
    {
      super(label);
    }

    public void setActiveWorkbenchPart(IWorkbenchPart workbenchPart)
    {
      if (workbenchPart instanceof MavenEditor)
      {
        mavenEditor = (MavenEditor)workbenchPart;
        selectionChanged();
      }
      else
      {
        setEnabled(false);
        elements.clear();
        mavenEditor = null;
      }
    }

    public final void selectionChanged()
    {
      checkEnabled(mavenEditor == null ? StructuredSelection.EMPTY : (IStructuredSelection)mavenEditor.getSelection());
    }

    // public final void selectionChanged(SelectionChangedEvent event)
    // {
    // checkEnabled(event.getStructuredSelection());
    // }

    protected abstract List<? extends T> getApplicableObjects(Object selectedObject);

    protected void checkEnabled(IStructuredSelection selection)
    {
      elements.clear();
      for (Object object : selection)
      {
        elements.addAll(getApplicableObjects(object));
      }

      setEnabled(recomputeEnabled());
    }

    protected boolean recomputeEnabled()
    {
      return !elements.isEmpty();
    }
  }

  private static class ApplyElementEditsAction extends MavenEditorAction<DOMElement>
  {
    private final Map<Document, Map<TextRegion, ElementEdit>> edits = new LinkedHashMap<>();

    public ApplyElementEditsAction()
    {
      super(MavenEditorPlugin.INSTANCE.getString("_UI_ApplyEdits_label")); //$NON-NLS-1$
    }

    @Override
    protected List<? extends DOMElement> getApplicableObjects(Object selectedObject)
    {
      if (selectedObject instanceof Resource resource)
      {
        for (EObject eObject : resource.getContents())
        {
          return getApplicableObjects(eObject);
        }
      }

      if (selectedObject instanceof DOMElement domElement)
      {
        return List.of(domElement);
      }

      if (selectedObject instanceof Realm realm)
      {
        return realm.getProjects();
      }

      return List.of();
    }

    @Override
    protected boolean recomputeEnabled()
    {
      edits.clear();

      for (DOMElement domElement : elements)
      {
        Map<Document, Map<TextRegion, ElementEdit>> elementEdits = domElement.getElementEdits();
        for (Map.Entry<Document, Map<TextRegion, ElementEdit>> entry : elementEdits.entrySet())
        {
          edits.computeIfAbsent(entry.getKey(), key -> new TreeMap<>()).putAll(entry.getValue());
        }
      }

      return !edits.isEmpty();
    }

    @Override
    public void run()
    {
      ApplyEditsCommand applyEditsCommand = new ApplyEditsCommand(edits);
      applyEditsCommand.setLabel(getText());
      applyEditsCommand.setDescription(getDescription());
      mavenEditor.getEditingDomain().getCommandStack().execute(applyEditsCommand);

      Map<Document, String> appliedEdits = POMXMLUtil.applyElementEdits(edits);
      for (Map.Entry<Document, String> entry : appliedEdits.entrySet())
      {
        Document document = entry.getKey();
        try
        {
          String content = entry.getValue();
          if (Boolean.FALSE)
          {
            Files.writeString(POMXMLUtil.getLocation(document), content, POMXMLUtil.getEncoding(document));
          }
        }
        catch (IOException ex)
        {
          MavenEditorPlugin.INSTANCE.log(ex);
        }
      }
    }

    static class ApplyEditsCommand extends AbstractCommand implements AbstractCommand.NonDirtying
    {
      private Map<Document, Map<TextRegion, ElementEdit>> edits;

      private Map<Document, String> undoContents = new LinkedHashMap<>();

      private Map<Document, String> redoContents = new LinkedHashMap<>();

      public ApplyEditsCommand(Map<Document, Map<TextRegion, ElementEdit>> edits)
      {
        this.edits = edits;
      }

      @Override
      protected boolean prepare()
      {
        return true;
      }

      @Override
      public void execute()
      {
        Map<Document, String> appliedEdits = POMXMLUtil.applyElementEdits(edits);
        for (Map.Entry<Document, String> entry : appliedEdits.entrySet())
        {
          Document document = entry.getKey();
          String content = entry.getValue();
          try
          {
            Charset encoding = POMXMLUtil.getEncoding(document);
            Path location = POMXMLUtil.getLocation(document);
            undoContents.put(document, Files.readString(location, encoding));
            Files.writeString(location, content, encoding);
            redoContents.put(document, content);
          }
          catch (IOException ex)
          {
            MavenEditorPlugin.INSTANCE.log(ex);
          }
        }
      }

      @Override
      public void undo()
      {
        apply(undoContents, null, null);
      }

      @Override
      public void redo()
      {
        apply(redoContents, null, null);
      }

      private void apply(Map<Document, String> contents, Map<Document, String> undoContents, Map<Document, String> redoContents)
      {
        for (Map.Entry<Document, String> entry : contents.entrySet())
        {
          Document document = entry.getKey();
          String content = entry.getValue();
          try
          {
            Charset encoding = POMXMLUtil.getEncoding(document);
            Path location = POMXMLUtil.getLocation(document);

            if (undoContents != null)
            {
              undoContents.put(document, Files.readString(location, encoding));
            }

            Files.writeString(location, content, encoding);

            if (redoContents != null)
            {
              redoContents.put(document, content);
            }
          }
          catch (IOException ex)
          {
            MavenEditorPlugin.INSTANCE.log(ex);
          }
        }
      }
    }
  }

  static class ReconcileRealmAction extends MavenEditorAction<Realm>
  {
    public ReconcileRealmAction()
    {
      super(MavenEditorPlugin.INSTANCE.getString("_UI_Reconcile_label")); //$NON-NLS-1$
      setDescription(MavenEditorPlugin.INSTANCE.getString("_UI_Reconcile_description")); //$NON-NLS-1$
    }

    @Override
    protected List<? extends Realm> getApplicableObjects(Object selectedObject)
    {
      if (selectedObject instanceof Resource resource)
      {
        for (EObject eObject : resource.getContents())
        {
          return getApplicableObjects(eObject);
        }
      }

      if (selectedObject instanceof EObject eObject)
      {
        EObject root = EcoreUtil.getRootContainer(eObject);
        if (root instanceof Realm realm)
        {
          return List.of(realm);
        }
      }

      return List.of();
    }

    @Override
    public void run()
    {
      ReconcileCommand reconcileCommand = new ReconcileCommand(elements.get(0));
      reconcileCommand.setLabel(getText());
      reconcileCommand.setDescription(getDescription());
      mavenEditor.getEditingDomain().getCommandStack().execute(reconcileCommand);
    }

    static class ReconcileCommand extends AbstractCommand implements AbstractCommand.NonDirtying
    {
      private RealmImpl realm;

      private State state;

      public ReconcileCommand(Realm realm)
      {
        this.realm = (RealmImpl)realm;
      }

      @Override
      protected boolean prepare()
      {
        return true;
      }

      @Override
      public void execute()
      {
        state = realm.getState();
        realm.reconcile();
      }

      @Override
      public void undo()
      {
        realm.setState(state);
      }

      @Override
      public void redo()
      {
        realm.reconcile();
      }
    }
  }
}
