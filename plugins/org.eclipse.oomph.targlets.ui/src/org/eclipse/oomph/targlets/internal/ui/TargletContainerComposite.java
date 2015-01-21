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
package org.eclipse.oomph.targlets.internal.ui;

import org.eclipse.oomph.targlets.presentation.TargletActionBarContributor;
import org.eclipse.oomph.targlets.presentation.TargletContainerEditorInput;
import org.eclipse.oomph.targlets.presentation.TargletEditor;
import org.eclipse.oomph.ui.PropertiesViewer;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.dynamichelpers.IExtensionTracker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IContributionManagerOverrides;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.INavigationHistory;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IReusableEditor;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISourceProvider;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.MultiPartInitException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.services.IServiceLocator;

import java.util.Collection;

/**
 * @author Eike Stepper
 */
public class TargletContainerComposite extends Composite
{
  private final WorkbenchPage page = new WorkbenchPage();

  private final EditorSite editorSite = new EditorSite();

  private final TargletEditor editor = new TargletEditor();

  public TargletContainerComposite(Composite parent, int style, String targletContainerID)
  {
    super(parent, style);
    setLayout(new FillLayout());

    SashForm sashForm = new SashForm(this, SWT.SMOOTH | SWT.VERTICAL);

    Composite editorComposite = new Composite(sashForm, SWT.BORDER);
    editorComposite.setLayout(new FillLayout());

    Composite propertySheetComposite = new Composite(sashForm, SWT.BORDER);
    propertySheetComposite.setLayout(UIUtil.createGridLayout(1));
    sashForm.setWeights(new int[] { 3, 1 });

    IEditorInput input = new TargletContainerEditorInput(targletContainerID);
    editor.init(editorSite, input);
    editor.createPartControl(editorComposite);

    editorSite.actionBarContributor.setActiveEditor(editor);

    final PropertiesViewer propertiesViewer = new PropertiesViewer(propertySheetComposite, SWT.NONE);
    GridData gridData = (GridData)propertiesViewer.getTable().getLayoutData();
    gridData.verticalAlignment = SWT.FILL;
    gridData.grabExcessVerticalSpace = true;

    editor.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        propertiesViewer.setInput(selection.getFirstElement());
      }
    });
  }

  @Override
  public void dispose()
  {
    editor.dispose();
    super.dispose();
  }

  @Override
  protected void checkSubclass()
  {
    // Disable the check that prevents subclassing of SWT components
  }

  /**
   * @author Eike Stepper
   */
  private final class WorkbenchPage implements IWorkbenchPage
  {
    public void addPartListener(IPartListener listener)
    {
    }

    public void addPartListener(IPartListener2 listener)
    {
    }

    public IWorkbenchPart getActivePart()
    {
      return editor;
    }

    public IWorkbenchPartReference getActivePartReference()
    {
      return null;
    }

    public void removePartListener(IPartListener listener)
    {
    }

    public void removePartListener(IPartListener2 listener)
    {
    }

    public void addSelectionListener(ISelectionListener listener)
    {
    }

    public void addSelectionListener(String partId, ISelectionListener listener)
    {
    }

    public void addPostSelectionListener(ISelectionListener listener)
    {
    }

    public void addPostSelectionListener(String partId, ISelectionListener listener)
    {
    }

    public ISelection getSelection()
    {
      return StructuredSelection.EMPTY;
    }

    public ISelection getSelection(String partId)
    {
      if (partId.equals(TargletEditor.EDITOR_ID))
      {
        return getSelection();
      }

      return StructuredSelection.EMPTY;
    }

    public void removeSelectionListener(ISelectionListener listener)
    {
    }

    public void removeSelectionListener(String partId, ISelectionListener listener)
    {
    }

    public void removePostSelectionListener(ISelectionListener listener)
    {
    }

    public void removePostSelectionListener(String partId, ISelectionListener listener)
    {
    }

    public void activate(IWorkbenchPart part)
    {
    }

    @SuppressWarnings("deprecation")
    public void addPropertyChangeListener(IPropertyChangeListener listener)
    {
    }

    public void bringToTop(IWorkbenchPart part)
    {
    }

    public boolean close()
    {
      return false;
    }

    public boolean closeAllEditors(boolean save)
    {
      return false;
    }

    public boolean closeEditors(IEditorReference[] editorRefs, boolean save)
    {
      return false;
    }

    public boolean closeEditor(IEditorPart editor, boolean save)
    {
      if (editor == TargletContainerComposite.this.editor)
      {
        dispose();
        return true;
      }

      return false;
    }

    public IViewPart findView(String viewId)
    {
      return null;
    }

    public IViewReference findViewReference(String viewId)
    {
      return null;
    }

    public IViewReference findViewReference(String viewId, String secondaryId)
    {
      return null;
    }

    public IEditorPart getActiveEditor()
    {
      return editor;
    }

    public IEditorPart findEditor(IEditorInput input)
    {
      return null;
    }

    public IEditorReference[] findEditors(IEditorInput input, String editorId, int matchFlags)
    {
      return null;
    }

    @Deprecated
    public IEditorPart[] getEditors()
    {
      return new IEditorPart[] { editor };
    }

    public IEditorReference[] getEditorReferences()
    {
      return null;
    }

    public IEditorPart[] getDirtyEditors()
    {
      return null;
    }

    public IAdaptable getInput()
    {
      return null;
    }

    public String getLabel()
    {
      return null;
    }

    public IPerspectiveDescriptor getPerspective()
    {
      return null;
    }

    public IViewReference[] getViewReferences()
    {
      return null;
    }

    @Deprecated
    public IViewPart[] getViews()
    {
      return null;
    }

    public IWorkbenchWindow getWorkbenchWindow()
    {
      return null;
    }

    @Deprecated
    public IWorkingSet getWorkingSet()
    {
      return null;
    }

    public void hideActionSet(String actionSetID)
    {
    }

    public void hideView(IViewPart view)
    {
    }

    public void hideView(IViewReference view)
    {
    }

    public boolean isPartVisible(IWorkbenchPart part)
    {
      return false;
    }

    public boolean isEditorAreaVisible()
    {
      return false;
    }

    public void reuseEditor(IReusableEditor editor, IEditorInput input)
    {
    }

    public IEditorPart openEditor(IEditorInput input, String editorId) throws PartInitException
    {
      return null;
    }

    public IEditorPart openEditor(IEditorInput input, String editorId, boolean activate) throws PartInitException
    {
      return null;
    }

    public IEditorPart openEditor(IEditorInput input, String editorId, boolean activate, int matchFlags) throws PartInitException
    {
      return null;
    }

    public void removePropertyChangeListener(IPropertyChangeListener listener)
    {
    }

    public void resetPerspective()
    {
    }

    public boolean saveAllEditors(boolean confirm)
    {
      return false;
    }

    public boolean saveEditor(IEditorPart editor, boolean confirm)
    {
      return false;
    }

    public void savePerspective()
    {
    }

    public void savePerspectiveAs(IPerspectiveDescriptor perspective)
    {
    }

    public void setEditorAreaVisible(boolean showEditorArea)
    {
    }

    public void setPerspective(IPerspectiveDescriptor perspective)
    {
    }

    public void showActionSet(String actionSetID)
    {
    }

    public IViewPart showView(String viewId) throws PartInitException
    {
      return null;
    }

    public IViewPart showView(String viewId, String secondaryId, int mode) throws PartInitException
    {
      return null;
    }

    public boolean isEditorPinned(IEditorPart editor)
    {
      return false;
    }

    @Deprecated
    public int getEditorReuseThreshold()
    {
      return 0;
    }

    @Deprecated
    public void setEditorReuseThreshold(int openEditors)
    {
    }

    public INavigationHistory getNavigationHistory()
    {
      return null;
    }

    public IViewPart[] getViewStack(IViewPart part)
    {
      return null;
    }

    public String[] getNewWizardShortcuts()
    {
      return null;
    }

    public String[] getPerspectiveShortcuts()
    {
      return null;
    }

    public String[] getShowViewShortcuts()
    {
      return null;
    }

    public IPerspectiveDescriptor[] getOpenPerspectives()
    {
      return null;
    }

    public IPerspectiveDescriptor[] getSortedPerspectives()
    {
      return null;
    }

    public void closePerspective(IPerspectiveDescriptor desc, boolean saveParts, boolean closePage)
    {
    }

    public void closeAllPerspectives(boolean saveEditors, boolean closePage)
    {
    }

    public IExtensionTracker getExtensionTracker()
    {
      return null;
    }

    public IWorkingSet[] getWorkingSets()
    {
      return null;
    }

    public void setWorkingSets(IWorkingSet[] sets)
    {
    }

    public IWorkingSet getAggregateWorkingSet()
    {
      return null;
    }

    public boolean isPageZoomed()
    {
      return false;
    }

    public void zoomOut()
    {
    }

    public void toggleZoom(IWorkbenchPartReference ref)
    {
    }

    public int getPartState(IWorkbenchPartReference ref)
    {
      return 0;
    }

    public void setPartState(IWorkbenchPartReference ref, int state)
    {
    }

    public IWorkbenchPartReference getReference(IWorkbenchPart part)
    {
      return null;
    }

    public void showEditor(IEditorReference ref)
    {
    }

    public void hideEditor(IEditorReference ref)
    {
    }

    public IEditorReference[] openEditors(IEditorInput[] inputs, String[] editorIDs, int matchFlags) throws MultiPartInitException
    {
      return null;
    }

    public IEditorReference[] openEditors(IEditorInput[] inputs, String[] editorIDs, IMemento[] mementos, int matchFlags, int activateIndex)
        throws MultiPartInitException
    {
      return null;
    }

    public IMemento[] getEditorState(IEditorReference[] editorRefs, boolean includeInputState)
    {
      return null;
    }

  }

  /**
   * @author Eike Stepper
   */
  private abstract class WorkbenchSite implements IWorkbenchSite
  {
    private final IPartService partService = new IPartService()
    {
      public void removePartListener(IPartListener2 listener)
      {
      }

      public void removePartListener(IPartListener listener)
      {
      }

      public IWorkbenchPartReference getActivePartReference()
      {
        return null;
      }

      public IWorkbenchPart getActivePart()
      {
        return editor;
      }

      public void addPartListener(IPartListener2 listener)
      {
      }

      public void addPartListener(IPartListener listener)
      {
      }
    };

    @SuppressWarnings("deprecation")
    private final org.eclipse.ui.IKeyBindingService keyBindingService = new org.eclipse.ui.IKeyBindingService()
    {
      public String[] getScopes()
      {
        return null;
      }

      public void registerAction(IAction action)
      {
      }

      public void setScopes(String[] scopes)
      {
      }

      public void unregisterAction(IAction action)
      {
      }
    };

    private final IHandlerService handlerService = new IHandlerService()
    {
      public void addSourceProvider(ISourceProvider provider)
      {
      }

      public void removeSourceProvider(ISourceProvider provider)
      {
      }

      public void dispose()
      {
      }

      public IHandlerActivation activateHandler(IHandlerActivation activation)
      {
        return null;
      }

      public IHandlerActivation activateHandler(String commandId, IHandler handler)
      {
        return null;
      }

      public IHandlerActivation activateHandler(String commandId, IHandler handler, Expression expression)
      {
        return null;
      }

      public IHandlerActivation activateHandler(String commandId, IHandler handler, Expression expression, boolean global)
      {
        return null;
      }

      @Deprecated
      public IHandlerActivation activateHandler(String commandId, IHandler handler, Expression expression, int sourcePriorities)
      {
        return null;
      }

      public ExecutionEvent createExecutionEvent(Command command, Event event)
      {
        return null;
      }

      public ExecutionEvent createExecutionEvent(ParameterizedCommand command, Event event)
      {
        return null;
      }

      public void deactivateHandler(IHandlerActivation activation)
      {
      }

      public void deactivateHandlers(@SuppressWarnings("rawtypes") Collection activations)
      {
      }

      public Object executeCommand(String commandId, Event event) throws ExecutionException, NotDefinedException, NotEnabledException, NotHandledException
      {
        return null;
      }

      public Object executeCommand(ParameterizedCommand command, Event event) throws ExecutionException, NotDefinedException, NotEnabledException,
          NotHandledException
      {
        return null;
      }

      public Object executeCommandInContext(ParameterizedCommand command, Event event, IEvaluationContext context) throws ExecutionException,
          NotDefinedException, NotEnabledException, NotHandledException
      {
        return null;
      }

      public IEvaluationContext createContextSnapshot(boolean includeSelection)
      {
        return null;
      }

      public IEvaluationContext getCurrentState()
      {
        return null;
      }

      public void readRegistry()
      {
      }

      public void setHelpContextId(IHandler handler, String helpContextId)
      {
      }
    };

    private ISelectionProvider selectionProvider;

    public String getPluginId()
    {
      return TargletsUIPlugin.INSTANCE.getSymbolicName();
    }

    public String getRegisteredName()
    {
      return null;
    }

    public void registerContextMenu(String menuId, MenuManager menuManager, ISelectionProvider selectionProvider)
    {
    }

    public void registerContextMenu(MenuManager menuManager, ISelectionProvider selectionProvider)
    {
    }

    @SuppressWarnings("deprecation")
    public org.eclipse.ui.IKeyBindingService getKeyBindingService()
    {
      return keyBindingService;
    }

    public IWorkbenchPage getPage()
    {
      return page;
    }

    public ISelectionProvider getSelectionProvider()
    {
      return selectionProvider;
    }

    public Shell getShell()
    {
      return TargletContainerComposite.this.getShell();
    }

    public IWorkbenchWindow getWorkbenchWindow()
    {
      return null;
    }

    public void setSelectionProvider(ISelectionProvider selectionProvider)
    {
      this.selectionProvider = selectionProvider;
    }

    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter)
    {
      return Platform.getAdapterManager().getAdapter(this, adapter);
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
    public <T> T getService(Class<T> api)
    {
      if (api == IPartService.class)
      {
        return (T)partService;
      }

      if (api == org.eclipse.ui.IKeyBindingService.class)
      {
        return (T)keyBindingService;
      }

      if (api == IHandlerService.class)
      {
        return (T)handlerService;
      }

      return null;
    }

    @SuppressWarnings("deprecation")
    public boolean hasService(Class<?> api)
    {
      if (api == IPartService.class)
      {
        return true;
      }

      if (api == org.eclipse.ui.IKeyBindingService.class)
      {
        return true;
      }

      if (api == IHandlerService.class)
      {
        return true;
      }

      return false;
    }
  }

  /**
   * @author Eike Stepper
   */
  private abstract class WorkbenchPartSite extends WorkbenchSite implements IWorkbenchPartSite
  {
    protected final IActionBars actionBars = new IActionBars()
    {
      private final IToolBarManager toolBarManager = new IToolBarManager()
      {
        public void update(boolean force)
        {
        }

        public void removeAll()
        {
        }

        public IContributionItem remove(IContributionItem item)
        {
          return null;
        }

        public IContributionItem remove(String id)
        {
          return null;
        }

        public void prependToGroup(String groupName, IContributionItem item)
        {
        }

        public void prependToGroup(String groupName, IAction action)
        {
        }

        public void markDirty()
        {
        }

        public boolean isEmpty()
        {
          return false;
        }

        public boolean isDirty()
        {
          return false;
        }

        public void insertBefore(String id, IContributionItem item)
        {
        }

        public void insertBefore(String id, IAction action)
        {
        }

        public void insertAfter(String id, IContributionItem item)
        {
        }

        public void insertAfter(String id, IAction action)
        {
        }

        public IContributionManagerOverrides getOverrides()
        {
          return null;
        }

        public IContributionItem[] getItems()
        {
          return null;
        }

        public IContributionItem find(String id)
        {
          return null;
        }

        public void appendToGroup(String groupName, IContributionItem item)
        {
        }

        public void appendToGroup(String groupName, IAction action)
        {
        }

        public void add(IContributionItem item)
        {
        }

        public void add(IAction action)
        {
        }
      };

      private final IStatusLineManager statusLineManager = new IStatusLineManager()
      {
        public void update(boolean force)
        {
        }

        public void removeAll()
        {
        }

        public IContributionItem remove(IContributionItem item)
        {
          return null;
        }

        public IContributionItem remove(String id)
        {
          return null;
        }

        public void prependToGroup(String groupName, IContributionItem item)
        {
        }

        public void prependToGroup(String groupName, IAction action)
        {
        }

        public void markDirty()
        {
        }

        public boolean isEmpty()
        {
          return false;
        }

        public boolean isDirty()
        {
          return false;
        }

        public void insertBefore(String id, IContributionItem item)
        {
        }

        public void insertBefore(String id, IAction action)
        {
        }

        public void insertAfter(String id, IContributionItem item)
        {
        }

        public void insertAfter(String id, IAction action)
        {
        }

        public IContributionManagerOverrides getOverrides()
        {
          return null;
        }

        public IContributionItem[] getItems()
        {
          return null;
        }

        public IContributionItem find(String id)
        {
          return null;
        }

        public void appendToGroup(String groupName, IContributionItem item)
        {
        }

        public void appendToGroup(String groupName, IAction action)
        {
        }

        public void add(IContributionItem item)
        {
        }

        public void add(IAction action)
        {
        }

        public void setMessage(Image image, String message)
        {
        }

        public void setMessage(String message)
        {
        }

        public void setErrorMessage(Image image, String message)
        {
        }

        public void setErrorMessage(String message)
        {
        }

        public void setCancelEnabled(boolean enabled)
        {
        }

        public boolean isCancelEnabled()
        {
          return false;
        }

        public IProgressMonitor getProgressMonitor()
        {
          return null;
        }
      };

      private final IMenuManager menuManager = new IMenuManager()
      {
        public void update(String id)
        {
        }

        public void update()
        {
        }

        public void setVisible(boolean visible)
        {
        }

        public void setParent(IContributionManager parent)
        {
        }

        public void saveWidgetState()
        {
        }

        public boolean isVisible()
        {
          return false;
        }

        public boolean isSeparator()
        {
          return false;
        }

        public boolean isGroupMarker()
        {
          return false;
        }

        public boolean isDynamic()
        {
          return false;
        }

        public String getId()
        {
          return null;
        }

        public void fill(CoolBar parent, int index)
        {
        }

        public void fill(ToolBar parent, int index)
        {
        }

        public void fill(Menu parent, int index)
        {
        }

        public void fill(Composite parent)
        {
        }

        public void dispose()
        {
        }

        public void update(boolean force)
        {
        }

        public void removeAll()
        {
        }

        public IContributionItem remove(IContributionItem item)
        {
          return null;
        }

        public IContributionItem remove(String id)
        {
          return null;
        }

        public void prependToGroup(String groupName, IContributionItem item)
        {
        }

        public void prependToGroup(String groupName, IAction action)
        {
        }

        public void markDirty()
        {
        }

        public boolean isEmpty()
        {
          return false;
        }

        public boolean isDirty()
        {
          return false;
        }

        public void insertBefore(String id, IContributionItem item)
        {
        }

        public void insertBefore(String id, IAction action)
        {
        }

        public void insertAfter(String id, IContributionItem item)
        {
        }

        public void insertAfter(String id, IAction action)
        {
        }

        public IContributionManagerOverrides getOverrides()
        {
          return null;
        }

        public IContributionItem[] getItems()
        {
          return null;
        }

        public IContributionItem find(String id)
        {
          return null;
        }

        public void appendToGroup(String groupName, IContributionItem item)
        {
        }

        public void appendToGroup(String groupName, IAction action)
        {
        }

        public void add(IContributionItem item)
        {
        }

        public void add(IAction action)
        {
        }

        public void updateAll(boolean force)
        {
        }

        public void setRemoveAllWhenShown(boolean removeAll)
        {
        }

        public void removeMenuListener(IMenuListener listener)
        {
        }

        public boolean isEnabled()
        {
          return false;
        }

        public boolean getRemoveAllWhenShown()
        {
          return false;
        }

        public IContributionItem findUsingPath(String path)
        {
          return null;
        }

        public IMenuManager findMenuUsingPath(String path)
        {
          return null;
        }

        public void addMenuListener(IMenuListener listener)
        {
        }
      };

      public void updateActionBars()
      {
      }

      public void setGlobalActionHandler(String actionId, IAction handler)
      {
      }

      public IToolBarManager getToolBarManager()
      {
        return toolBarManager;
      }

      public IStatusLineManager getStatusLineManager()
      {
        return statusLineManager;
      }

      public IServiceLocator getServiceLocator()
      {
        return null;
      }

      public IMenuManager getMenuManager()
      {
        return menuManager;
      }

      public IAction getGlobalActionHandler(String actionId)
      {
        return null;
      }

      public void clearGlobalActionHandlers()
      {
      }
    };

    public IActionBars getActionBars()
    {
      return actionBars;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class EditorSite extends WorkbenchPartSite implements IEditorSite
  {
    private final TargletActionBarContributor actionBarContributor = new TargletActionBarContributor();

    public EditorSite()
    {
      actionBarContributor.init(actionBars, page);
    }

    public String getId()
    {
      return TargletEditor.EDITOR_ID;
    }

    public IWorkbenchPart getPart()
    {
      return editor;
    }

    public IEditorActionBarContributor getActionBarContributor()
    {
      return actionBarContributor;
    }

    public void registerContextMenu(MenuManager menuManager, ISelectionProvider selectionProvider, boolean includeEditorInput)
    {
    }

    public void registerContextMenu(String menuId, MenuManager menuManager, ISelectionProvider selectionProvider, boolean includeEditorInput)
    {
    }
  }
}
