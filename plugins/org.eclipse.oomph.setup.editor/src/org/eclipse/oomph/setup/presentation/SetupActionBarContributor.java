/*
 * Copyright (c) 2014 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ui.actions.PreferenceRecorderAction;
import org.eclipse.oomph.setup.workingsets.WorkingSetTask;
import org.eclipse.oomph.workingsets.WorkingSet;
import org.eclipse.oomph.workingsets.presentation.WorkingSetsActionBarContributor.PreviewDialog;

import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.ui.action.ControlAction;
import org.eclipse.emf.edit.ui.action.CreateChildAction;
import org.eclipse.emf.edit.ui.action.CreateSiblingAction;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;
import org.eclipse.emf.edit.ui.action.LoadResourceAction;
import org.eclipse.emf.edit.ui.action.ValidateAction;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.keys.IBindingService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the action bar contributor for the Setup model editor.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SetupActionBarContributor extends EditingDomainActionBarContributor implements ISelectionChangedListener
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
  protected IAction showPropertiesViewAction = new Action(SetupEditorPlugin.INSTANCE.getString("_UI_ShowPropertiesView_menu_item"))
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
        SetupEditorPlugin.INSTANCE.log(exception);
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

  public ToggleViewerInputAction toggleViewerInputAction = new ToggleViewerInputAction();

  private PreferenceRecorderToolbarAction recordPreferencesAction = new PreferenceRecorderToolbarAction(true);

  private SniffAction sniffAction = new SniffAction(true);

  private CommandTableAction commandTableAction = new CommandTableAction();

  private EditorTableAction editorTableAction = new EditorTableAction();

  private TestInstallAction testInstallAction = new TestInstallAction();

  private RevertAction revertAction;

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
    super.init(actionBars);

    revertAction = new RevertAction();
    actionBars.setGlobalActionHandler(ActionFactory.REVERT.getId(), revertAction);
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
    toolBarManager.add(sniffAction);
    toolBarManager.add(commandTableAction);
    toolBarManager.add(editorTableAction);
    toolBarManager.add(testInstallAction);
    toolBarManager.add(toggleViewerInputAction);
    toolBarManager.add(new Separator("setup-additions"));
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
    revertAction.setActiveWorkbenchPart(part);

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
    recordPreferencesAction.selectionChanged(event);
    sniffAction.selectionChanged(event);
    testInstallAction.selectionChanged(event);
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

    submenuManager = new MenuManager(SetupEditorPlugin.INSTANCE.getString("_UI_CreateChild_menu_item"));
    populateManager(submenuManager, createChildActions, null);
    menuManager.insertBefore("edit", submenuManager);

    submenuManager = new MenuManager(SetupEditorPlugin.INSTANCE.getString("_UI_CreateSibling_menu_item"));
    populateManager(submenuManager, createSiblingActions, null);
    menuManager.insertBefore("edit", submenuManager);
  }

  @Override
  public void menuAboutToShow(IMenuManager menuManager)
  {
    menuAboutToShowGen(menuManager);
    menuManager.insertBefore("ui-actions", new Action()
    {
      @Override
      public String getText()
      {
        return "Working Sets Preview...";
      }

      @Override
      public void run()
      {
        Dialog dialog = new PreviewDialog(activeEditorPart.getSite().getShell(), activeEditorPart)
        {
          private List<WorkingSet> workingSets = new ArrayList<WorkingSet>();

          @Override
          protected void selectionChanged(IWorkbenchPart part, ISelection selection)
          {
            if (part == activeEditorPart)
            {
              List<WorkingSet> oldWorkingSets = workingSets;
              workingSets = getWorkingSets();
              if (workingSets != oldWorkingSets)
              {
                reconcile();
                tree.setInput(input);
                tree.expandAll();
              }
            }

            super.selectionChanged(part, selection);
          }

          @Override
          protected List<WorkingSet> getWorkingSets()
          {
            IStructuredSelection selection = (IStructuredSelection)((ISelectionProvider)activeEditorPart).getSelection();
            LOOP: for (Object object : selection.toArray())
            {
              if (object instanceof EObject)
              {
                for (EObject eObject = (EObject)object; eObject != null; eObject = eObject.eContainer())
                {
                  if (eObject instanceof WorkingSetTask)
                  {
                    workingSets = ((WorkingSetTask)eObject).getWorkingSets();
                    break LOOP;
                  }
                }
              }
            }

            return workingSets;
          }
        };

        dialog.open();
      }
    });
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
   * @author Eike Stepper
   */
  private static class PreferenceRecorderToolbarAction extends PreferenceRecorderAction
  {
    public PreferenceRecorderToolbarAction(boolean withDialog)
    {
      super(withDialog);
      setImageDescriptor(SetupEditorPlugin.INSTANCE.getImageDescriptor("recorder"));
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
      IBindingService bindingService = (IBindingService)PlatformUI.getWorkbench().getService(IBindingService.class);
      Binding[] bindings = bindingService.getBindings();
      Map<String, List<Command>> map = new HashMap<String, List<Command>>();

      ICommandService commandService = (ICommandService)PlatformUI.getWorkbench().getService(ICommandService.class);
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

  private static final class RevertAction extends Action
  {
    private SetupEditor setupEditor;

    @Override
    public void run()
    {
      setupEditor.doRevert();
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
}
