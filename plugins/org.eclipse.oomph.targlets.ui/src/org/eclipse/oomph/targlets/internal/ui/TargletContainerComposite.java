/*
 * Copyright (c) 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
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
    UIUtil.setTransparentBackgroundColor(this);
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
      @Override
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

  /**
   * @author Eike Stepper
   */
  private final class WorkbenchPage implements IWorkbenchPage
  {
    @Override
    public void addPartListener(IPartListener listener)
    {
    }

    @Override
    public void addPartListener(IPartListener2 listener)
    {
    }

    @Override
    public IWorkbenchPart getActivePart()
    {
      return editor;
    }

    @Override
    public IWorkbenchPartReference getActivePartReference()
    {
      return null;
    }

    @Override
    public void removePartListener(IPartListener listener)
    {
    }

    @Override
    public void removePartListener(IPartListener2 listener)
    {
    }

    @Override
    public void addSelectionListener(ISelectionListener listener)
    {
    }

    @Override
    public void addSelectionListener(String partId, ISelectionListener listener)
    {
    }

    @Override
    public void addPostSelectionListener(ISelectionListener listener)
    {
    }

    @Override
    public void addPostSelectionListener(String partId, ISelectionListener listener)
    {
    }

    @Override
    public ISelection getSelection()
    {
      return StructuredSelection.EMPTY;
    }

    @Override
    public ISelection getSelection(String partId)
    {
      if (partId.equals(TargletEditor.EDITOR_ID))
      {
        return getSelection();
      }

      return StructuredSelection.EMPTY;
    }

    @Override
    public void removeSelectionListener(ISelectionListener listener)
    {
    }

    @Override
    public void removeSelectionListener(String partId, ISelectionListener listener)
    {
    }

    @Override
    public void removePostSelectionListener(ISelectionListener listener)
    {
    }

    @Override
    public void removePostSelectionListener(String partId, ISelectionListener listener)
    {
    }

    @Override
    public void activate(IWorkbenchPart part)
    {
    }

    @Override
    @SuppressWarnings("deprecation")
    public void addPropertyChangeListener(IPropertyChangeListener listener)
    {
    }

    @Override
    public void bringToTop(IWorkbenchPart part)
    {
    }

    @Override
    public boolean close()
    {
      return false;
    }

    @Override
    public boolean closeAllEditors(boolean save)
    {
      return false;
    }

    @Override
    public boolean closeEditors(IEditorReference[] editorRefs, boolean save)
    {
      return false;
    }

    @Override
    public boolean closeEditor(IEditorPart editor, boolean save)
    {
      if (editor == TargletContainerComposite.this.editor)
      {
        dispose();
        return true;
      }

      return false;
    }

    @Override
    public IViewPart findView(String viewId)
    {
      return null;
    }

    @Override
    public IViewReference findViewReference(String viewId)
    {
      return null;
    }

    @Override
    public IViewReference findViewReference(String viewId, String secondaryId)
    {
      return null;
    }

    @Override
    public IEditorPart getActiveEditor()
    {
      return editor;
    }

    @Override
    public IEditorPart findEditor(IEditorInput input)
    {
      return null;
    }

    @Override
    public IEditorReference[] findEditors(IEditorInput input, String editorId, int matchFlags)
    {
      return null;
    }

    @Override
    @Deprecated
    public IEditorPart[] getEditors()
    {
      return new IEditorPart[] { editor };
    }

    @Override
    public IEditorReference[] getEditorReferences()
    {
      return null;
    }

    @Override
    public IEditorPart[] getDirtyEditors()
    {
      return null;
    }

    @Override
    public IAdaptable getInput()
    {
      return null;
    }

    @Override
    public String getLabel()
    {
      return null;
    }

    @Override
    public IPerspectiveDescriptor getPerspective()
    {
      return null;
    }

    @Override
    public IViewReference[] getViewReferences()
    {
      return null;
    }

    @Override
    @Deprecated
    public IViewPart[] getViews()
    {
      return null;
    }

    @Override
    public IWorkbenchWindow getWorkbenchWindow()
    {
      return null;
    }

    @Override
    @Deprecated
    public IWorkingSet getWorkingSet()
    {
      return null;
    }

    @Override
    public void hideActionSet(String actionSetID)
    {
    }

    @Override
    public void hideView(IViewPart view)
    {
    }

    @Override
    public void hideView(IViewReference view)
    {
    }

    @Override
    public boolean isPartVisible(IWorkbenchPart part)
    {
      return false;
    }

    @Override
    public boolean isEditorAreaVisible()
    {
      return false;
    }

    @Override
    public void reuseEditor(IReusableEditor editor, IEditorInput input)
    {
    }

    @Override
    public IEditorPart openEditor(IEditorInput input, String editorId) throws PartInitException
    {
      return null;
    }

    @Override
    public IEditorPart openEditor(IEditorInput input, String editorId, boolean activate) throws PartInitException
    {
      return null;
    }

    @Override
    public IEditorPart openEditor(IEditorInput input, String editorId, boolean activate, int matchFlags) throws PartInitException
    {
      return null;
    }

    @Override
    public void removePropertyChangeListener(IPropertyChangeListener listener)
    {
    }

    @Override
    public void resetPerspective()
    {
    }

    @Override
    public boolean saveAllEditors(boolean confirm)
    {
      return false;
    }

    @Override
    public boolean saveEditor(IEditorPart editor, boolean confirm)
    {
      return false;
    }

    @Override
    public void savePerspective()
    {
    }

    @Override
    public void savePerspectiveAs(IPerspectiveDescriptor perspective)
    {
    }

    @Override
    public void setEditorAreaVisible(boolean showEditorArea)
    {
    }

    @Override
    public void setPerspective(IPerspectiveDescriptor perspective)
    {
    }

    @Override
    public void showActionSet(String actionSetID)
    {
    }

    @Override
    public IViewPart showView(String viewId) throws PartInitException
    {
      return null;
    }

    @Override
    public IViewPart showView(String viewId, String secondaryId, int mode) throws PartInitException
    {
      return null;
    }

    @Override
    public boolean isEditorPinned(IEditorPart editor)
    {
      return false;
    }

    @Override
    @Deprecated
    public int getEditorReuseThreshold()
    {
      return 0;
    }

    @Override
    @Deprecated
    public void setEditorReuseThreshold(int openEditors)
    {
    }

    @Override
    public INavigationHistory getNavigationHistory()
    {
      return null;
    }

    @Override
    public IViewPart[] getViewStack(IViewPart part)
    {
      return null;
    }

    @Override
    public String[] getNewWizardShortcuts()
    {
      return null;
    }

    @Override
    public String[] getPerspectiveShortcuts()
    {
      return null;
    }

    @Override
    public String[] getShowViewShortcuts()
    {
      return null;
    }

    @Override
    public IPerspectiveDescriptor[] getOpenPerspectives()
    {
      return null;
    }

    @Override
    public IPerspectiveDescriptor[] getSortedPerspectives()
    {
      return null;
    }

    @Override
    public void closePerspective(IPerspectiveDescriptor desc, boolean saveParts, boolean closePage)
    {
    }

    @Override
    public void closeAllPerspectives(boolean saveEditors, boolean closePage)
    {
    }

    @Override
    public IExtensionTracker getExtensionTracker()
    {
      return null;
    }

    @Override
    public IWorkingSet[] getWorkingSets()
    {
      return null;
    }

    @Override
    public void setWorkingSets(IWorkingSet[] sets)
    {
    }

    @Override
    public IWorkingSet getAggregateWorkingSet()
    {
      return null;
    }

    @Override
    public boolean isPageZoomed()
    {
      return false;
    }

    @SuppressWarnings("all")
    public void zoomOut()
    {
    }

    @Override
    public void toggleZoom(IWorkbenchPartReference ref)
    {
    }

    @Override
    public int getPartState(IWorkbenchPartReference ref)
    {
      return 0;
    }

    @Override
    public void setPartState(IWorkbenchPartReference ref, int state)
    {
    }

    @Override
    public IWorkbenchPartReference getReference(IWorkbenchPart part)
    {
      return null;
    }

    @Override
    public void showEditor(IEditorReference ref)
    {
    }

    @Override
    public void hideEditor(IEditorReference ref)
    {
    }

    @Override
    public IEditorReference[] openEditors(IEditorInput[] inputs, String[] editorIDs, int matchFlags) throws MultiPartInitException
    {
      return null;
    }

    @Override
    public IEditorReference[] openEditors(IEditorInput[] inputs, String[] editorIDs, IMemento[] mementos, int matchFlags, int activateIndex)
        throws MultiPartInitException
    {
      return null;
    }

    @Override
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
      @Override
      public void removePartListener(IPartListener2 listener)
      {
      }

      @Override
      public void removePartListener(IPartListener listener)
      {
      }

      @Override
      public IWorkbenchPartReference getActivePartReference()
      {
        return null;
      }

      @Override
      public IWorkbenchPart getActivePart()
      {
        return editor;
      }

      @Override
      public void addPartListener(IPartListener2 listener)
      {
      }

      @Override
      public void addPartListener(IPartListener listener)
      {
      }
    };

    @SuppressWarnings("deprecation")
    private final org.eclipse.ui.IKeyBindingService keyBindingService = new org.eclipse.ui.IKeyBindingService()
    {
      @Override
      public String[] getScopes()
      {
        return null;
      }

      @Override
      public void registerAction(IAction action)
      {
      }

      @Override
      public void setScopes(String[] scopes)
      {
      }

      @Override
      public void unregisterAction(IAction action)
      {
      }
    };

    private final IHandlerService handlerService = new IHandlerService()
    {
      @Override
      public void addSourceProvider(ISourceProvider provider)
      {
      }

      @Override
      public void removeSourceProvider(ISourceProvider provider)
      {
      }

      @Override
      public void dispose()
      {
      }

      @Override
      public IHandlerActivation activateHandler(IHandlerActivation activation)
      {
        return null;
      }

      @Override
      public IHandlerActivation activateHandler(String commandId, IHandler handler)
      {
        return null;
      }

      @Override
      public IHandlerActivation activateHandler(String commandId, IHandler handler, Expression expression)
      {
        return null;
      }

      @Override
      public IHandlerActivation activateHandler(String commandId, IHandler handler, Expression expression, boolean global)
      {
        return null;
      }

      @Override
      @Deprecated
      public IHandlerActivation activateHandler(String commandId, IHandler handler, Expression expression, int sourcePriorities)
      {
        return null;
      }

      @Override
      public ExecutionEvent createExecutionEvent(Command command, Event event)
      {
        return null;
      }

      @Override
      public ExecutionEvent createExecutionEvent(ParameterizedCommand command, Event event)
      {
        return null;
      }

      @Override
      public void deactivateHandler(IHandlerActivation activation)
      {
      }

      @Override
      public void deactivateHandlers(@SuppressWarnings("rawtypes") Collection activations)
      {
      }

      @Override
      public Object executeCommand(String commandId, Event event) throws ExecutionException, NotDefinedException, NotEnabledException, NotHandledException
      {
        return null;
      }

      @Override
      public Object executeCommand(ParameterizedCommand command, Event event)
          throws ExecutionException, NotDefinedException, NotEnabledException, NotHandledException
      {
        return null;
      }

      @Override
      public Object executeCommandInContext(ParameterizedCommand command, Event event, IEvaluationContext context)
          throws ExecutionException, NotDefinedException, NotEnabledException, NotHandledException
      {
        return null;
      }

      @Override
      public IEvaluationContext createContextSnapshot(boolean includeSelection)
      {
        return null;
      }

      @Override
      public IEvaluationContext getCurrentState()
      {
        return null;
      }

      @Override
      public void readRegistry()
      {
      }

      @Override
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

    @Override
    public IWorkbenchPage getPage()
    {
      return page;
    }

    @Override
    public ISelectionProvider getSelectionProvider()
    {
      return selectionProvider;
    }

    @Override
    public Shell getShell()
    {
      return TargletContainerComposite.this.getShell();
    }

    @Override
    public IWorkbenchWindow getWorkbenchWindow()
    {
      return null;
    }

    @Override
    public void setSelectionProvider(ISelectionProvider selectionProvider)
    {
      this.selectionProvider = selectionProvider;
    }

    @Override
    @SuppressWarnings("all")
    public Object getAdapter(Class adapter)
    {
      return Platform.getAdapterManager().getAdapter(this, adapter);
    }

    @Override
    @SuppressWarnings("all")
    public Object getService(Class api)
    {
      if (api == IPartService.class)
      {
        return partService;
      }

      if (api == org.eclipse.ui.IKeyBindingService.class)
      {
        return keyBindingService;
      }

      if (api == IHandlerService.class)
      {
        return handlerService;
      }

      return null;
    }

    @Override
    @SuppressWarnings("all")
    public boolean hasService(Class api)
    {
      if (api == IPartService.class || api == org.eclipse.ui.IKeyBindingService.class || api == IHandlerService.class)
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
        @Override
        public void update(boolean force)
        {
        }

        @Override
        public void removeAll()
        {
        }

        @Override
        public IContributionItem remove(IContributionItem item)
        {
          return null;
        }

        @Override
        public IContributionItem remove(String id)
        {
          return null;
        }

        @Override
        public void prependToGroup(String groupName, IContributionItem item)
        {
        }

        @Override
        public void prependToGroup(String groupName, IAction action)
        {
        }

        @Override
        public void markDirty()
        {
        }

        @Override
        public boolean isEmpty()
        {
          return false;
        }

        @Override
        public boolean isDirty()
        {
          return false;
        }

        @Override
        public void insertBefore(String id, IContributionItem item)
        {
        }

        @Override
        public void insertBefore(String id, IAction action)
        {
        }

        @Override
        public void insertAfter(String id, IContributionItem item)
        {
        }

        @Override
        public void insertAfter(String id, IAction action)
        {
        }

        @Override
        public IContributionManagerOverrides getOverrides()
        {
          return null;
        }

        @Override
        public IContributionItem[] getItems()
        {
          return null;
        }

        @Override
        public IContributionItem find(String id)
        {
          return null;
        }

        @Override
        public void appendToGroup(String groupName, IContributionItem item)
        {
        }

        @Override
        public void appendToGroup(String groupName, IAction action)
        {
        }

        @Override
        public void add(IContributionItem item)
        {
        }

        @Override
        public void add(IAction action)
        {
        }
      };

      private final IStatusLineManager statusLineManager = new IStatusLineManager()
      {
        @Override
        public void update(boolean force)
        {
        }

        @Override
        public void removeAll()
        {
        }

        @Override
        public IContributionItem remove(IContributionItem item)
        {
          return null;
        }

        @Override
        public IContributionItem remove(String id)
        {
          return null;
        }

        @Override
        public void prependToGroup(String groupName, IContributionItem item)
        {
        }

        @Override
        public void prependToGroup(String groupName, IAction action)
        {
        }

        @Override
        public void markDirty()
        {
        }

        @Override
        public boolean isEmpty()
        {
          return false;
        }

        @Override
        public boolean isDirty()
        {
          return false;
        }

        @Override
        public void insertBefore(String id, IContributionItem item)
        {
        }

        @Override
        public void insertBefore(String id, IAction action)
        {
        }

        @Override
        public void insertAfter(String id, IContributionItem item)
        {
        }

        @Override
        public void insertAfter(String id, IAction action)
        {
        }

        @Override
        public IContributionManagerOverrides getOverrides()
        {
          return null;
        }

        @Override
        public IContributionItem[] getItems()
        {
          return null;
        }

        @Override
        public IContributionItem find(String id)
        {
          return null;
        }

        @Override
        public void appendToGroup(String groupName, IContributionItem item)
        {
        }

        @Override
        public void appendToGroup(String groupName, IAction action)
        {
        }

        @Override
        public void add(IContributionItem item)
        {
        }

        @Override
        public void add(IAction action)
        {
        }

        @Override
        public void setMessage(Image image, String message)
        {
        }

        @Override
        public void setMessage(String message)
        {
        }

        @Override
        public void setErrorMessage(Image image, String message)
        {
        }

        @Override
        public void setErrorMessage(String message)
        {
        }

        @Override
        public void setCancelEnabled(boolean enabled)
        {
        }

        @Override
        public boolean isCancelEnabled()
        {
          return false;
        }

        @Override
        public IProgressMonitor getProgressMonitor()
        {
          return null;
        }
      };

      private final IMenuManager menuManager = new IMenuManager()
      {
        @Override
        public void update(String id)
        {
        }

        @Override
        public void update()
        {
        }

        @Override
        public void setVisible(boolean visible)
        {
        }

        @Override
        public void setParent(IContributionManager parent)
        {
        }

        @Override
        public void saveWidgetState()
        {
        }

        @Override
        public boolean isVisible()
        {
          return false;
        }

        @Override
        public boolean isSeparator()
        {
          return false;
        }

        @Override
        public boolean isGroupMarker()
        {
          return false;
        }

        @Override
        public boolean isDynamic()
        {
          return false;
        }

        @Override
        public String getId()
        {
          return null;
        }

        @Override
        public void fill(CoolBar parent, int index)
        {
        }

        @Override
        public void fill(ToolBar parent, int index)
        {
        }

        @Override
        public void fill(Menu parent, int index)
        {
        }

        @Override
        public void fill(Composite parent)
        {
        }

        @Override
        public void dispose()
        {
        }

        @Override
        public void update(boolean force)
        {
        }

        @Override
        public void removeAll()
        {
        }

        @Override
        public IContributionItem remove(IContributionItem item)
        {
          return null;
        }

        @Override
        public IContributionItem remove(String id)
        {
          return null;
        }

        @Override
        public void prependToGroup(String groupName, IContributionItem item)
        {
        }

        @Override
        public void prependToGroup(String groupName, IAction action)
        {
        }

        @Override
        public void markDirty()
        {
        }

        @Override
        public boolean isEmpty()
        {
          return false;
        }

        @Override
        public boolean isDirty()
        {
          return false;
        }

        @Override
        public void insertBefore(String id, IContributionItem item)
        {
        }

        @Override
        public void insertBefore(String id, IAction action)
        {
        }

        @Override
        public void insertAfter(String id, IContributionItem item)
        {
        }

        @Override
        public void insertAfter(String id, IAction action)
        {
        }

        @Override
        public IContributionManagerOverrides getOverrides()
        {
          return null;
        }

        @Override
        public IContributionItem[] getItems()
        {
          return null;
        }

        @Override
        public IContributionItem find(String id)
        {
          return null;
        }

        @Override
        public void appendToGroup(String groupName, IContributionItem item)
        {
        }

        @Override
        public void appendToGroup(String groupName, IAction action)
        {
        }

        @Override
        public void add(IContributionItem item)
        {
        }

        @Override
        public void add(IAction action)
        {
        }

        @Override
        public void updateAll(boolean force)
        {
        }

        @Override
        public void setRemoveAllWhenShown(boolean removeAll)
        {
        }

        @Override
        public void removeMenuListener(IMenuListener listener)
        {
        }

        @Override
        public boolean isEnabled()
        {
          return false;
        }

        @Override
        public boolean getRemoveAllWhenShown()
        {
          return false;
        }

        @Override
        public IContributionItem findUsingPath(String path)
        {
          return null;
        }

        @Override
        public IMenuManager findMenuUsingPath(String path)
        {
          return null;
        }

        @Override
        public void addMenuListener(IMenuListener listener)
        {
        }
      };

      @Override
      public void updateActionBars()
      {
      }

      @Override
      public void setGlobalActionHandler(String actionId, IAction handler)
      {
      }

      @Override
      public IToolBarManager getToolBarManager()
      {
        return toolBarManager;
      }

      @Override
      public IStatusLineManager getStatusLineManager()
      {
        return statusLineManager;
      }

      @Override
      public IServiceLocator getServiceLocator()
      {
        return null;
      }

      @Override
      public IMenuManager getMenuManager()
      {
        return menuManager;
      }

      @Override
      public IAction getGlobalActionHandler(String actionId)
      {
        return null;
      }

      @Override
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

    @Override
    public String getId()
    {
      return TargletEditor.EDITOR_ID;
    }

    @Override
    public IWorkbenchPart getPart()
    {
      return editor;
    }

    @Override
    public IEditorActionBarContributor getActionBarContributor()
    {
      return actionBarContributor;
    }

    @Override
    public void registerContextMenu(MenuManager menuManager, ISelectionProvider selectionProvider, boolean includeEditorInput)
    {
    }

    @Override
    public void registerContextMenu(String menuId, MenuManager menuManager, ISelectionProvider selectionProvider, boolean includeEditorInput)
    {
    }
  }
}
