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

import org.eclipse.oomph.base.provider.BaseItemProviderAdapterFactory;
import org.eclipse.oomph.internal.base.bundle.BasePlugin;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.provider.SetupItemProviderAdapterFactory;
import org.eclipse.oomph.setup.ui.SetupLabelProvider;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.ui.MarkerHelper;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.ui.editor.ProblemEditorPart;
import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProvider;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.dnd.ViewerDragAdapter;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.emf.edit.ui.provider.UnwrappingSelectionProvider;
import org.eclipse.emf.edit.ui.util.EditUIMarkerHelper;
import org.eclipse.emf.edit.ui.util.EditUIUtil;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetSorter;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is an example of a Setup model editor.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SetupEditor extends MultiPageEditorPart implements IEditingDomainProvider, ISelectionProvider, IMenuListener, IViewerProvider, IGotoMarker
{
  private static final URI LEGACY_MODELS = URI.createURI("platform:/plugin/" + BasePlugin.INSTANCE.getSymbolicName() + "/model/legacy");

  private static final URI LEGACY_EXAMPLE_URI = URI.createURI("file:/example.setup");

  private static final Object VARIABLE_GROUP_IMAGE = SetupEditorPlugin.INSTANCE.getImage("full/obj16/VariableGroup");

  private static final Object UNDECLARED_VARIABLE_GROUP_IMAGE;

  static
  {
    List<Object> images = new ArrayList<Object>(2);
    images.add(VARIABLE_GROUP_IMAGE);
    images.add(EMFEditUIPlugin.INSTANCE.getImage("full/ovr16/error_ovr.gif"));
    ComposedImage composedImage = new ComposedImage(images)
    {
      @Override
      public List<Point> getDrawPoints(Size size)
      {
        List<Point> result = new ArrayList<Point>();
        result.add(new Point());
        Point overlay = new Point();
        overlay.y = 7;
        result.add(overlay);
        return result;
      }
    };

    UNDECLARED_VARIABLE_GROUP_IMAGE = ExtendedImageRegistry.INSTANCE.getImage(composedImage);
  }

  public static final String EDITOR_ID = "org.eclipse.oomph.setup.presentation.SetupEditorID";

  /**
   * This keeps track of the editing domain that is used to track all changes to the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected AdapterFactoryEditingDomain editingDomain;

  /**
   * This is the one adapter factory used for providing views of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ComposedAdapterFactory adapterFactory;

  /**
   * This is the content outline page.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected OutlinePreviewPage contentOutlinePage;

  /**
   * This is a kludge...
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IStatusLineManager contentOutlineStatusLineManager;

  /**
   * This is the content outline page's viewer.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TreeViewer contentOutlineViewer;

  /**
   * This is the property sheet page.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected List<PropertySheetPage> propertySheetPages = new ArrayList<PropertySheetPage>();

  /**
   * This is the viewer that shadows the selection in the content outline.
   * The parent relation must be correctly defined for this to work.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TreeViewer selectionViewer;

  /**
   * This keeps track of the active content viewer, which may be either one of the viewers in the pages or the content outline viewer.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Viewer currentViewer;

  /**
   * This listens to which ever viewer is active.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ISelectionChangedListener selectionChangedListener;

  /**
   * This keeps track of all the {@link org.eclipse.jface.viewers.ISelectionChangedListener}s that are listening to this editor.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<ISelectionChangedListener> selectionChangedListeners = new ArrayList<ISelectionChangedListener>();

  /**
   * This keeps track of the selection of the editor as a whole.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ISelection editorSelection = StructuredSelection.EMPTY;

  /**
   * The MarkerHelper is responsible for creating workspace resource markers presented
   * in Eclipse's Problems View.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MarkerHelper markerHelper = new EditUIMarkerHelper();

  /**
   * This listens for when the outline becomes active
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected IPartListener partListener = new IPartListener()
  {
    public void partActivated(IWorkbenchPart p)
    {
      if (p instanceof ContentOutline)
      {
        if (((ContentOutline)p).getCurrentPage() == contentOutlinePage)
        {
          getActionBarContributor().setActiveEditor(SetupEditor.this);

          setCurrentViewer(contentOutlineViewer);
        }
      }
      else if (p instanceof PropertySheet)
      {
        if (propertySheetPages.contains(((PropertySheet)p).getCurrentPage()))
        {
          getActionBarContributor().setActiveEditor(SetupEditor.this);
          handleActivate();
        }
      }
      else if (p == SetupEditor.this)
      {
        handleActivate();
        setCurrentViewer(selectionViewer);
      }
    }

    public void partBroughtToTop(IWorkbenchPart p)
    {
      // Ignore.
    }

    public void partClosed(IWorkbenchPart p)
    {
      // Ignore.
    }

    public void partDeactivated(IWorkbenchPart p)
    {
      // Ignore.
    }

    public void partOpened(IWorkbenchPart p)
    {
      // Ignore.
    }
  };

  /**
   * Resources that have been removed since last activation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<Resource> removedResources = new ArrayList<Resource>();

  /**
   * Resources that have been changed since last activation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<Resource> changedResources = new ArrayList<Resource>();

  /**
   * Resources that have been saved.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<Resource> savedResources = new ArrayList<Resource>();

  /**
   * Map to store the diagnostic associated with a resource.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Map<Resource, Diagnostic> resourceToDiagnosticMap = new LinkedHashMap<Resource, Diagnostic>();

  /**
   * Controls whether the problem indication should be updated.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected boolean updateProblemIndication = true;

  /**
   * Adapter used to update the problem indication when resources are demanded loaded.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EContentAdapter problemIndicationAdapter = new EContentAdapter()
  {
    @Override
    public void notifyChanged(Notification notification)
    {
      if (notification.getNotifier() instanceof Resource)
      {
        switch (notification.getFeatureID(Resource.class))
        {
          case Resource.RESOURCE__IS_LOADED:
          case Resource.RESOURCE__ERRORS:
          case Resource.RESOURCE__WARNINGS:
          {
            Resource resource = (Resource)notification.getNotifier();
            Diagnostic diagnostic = analyzeResourceProblems(resource, null);
            if (diagnostic.getSeverity() != Diagnostic.OK)
            {
              resourceToDiagnosticMap.put(resource, diagnostic);
            }
            else
            {
              resourceToDiagnosticMap.remove(resource);
            }

            if (updateProblemIndication)
            {
              getSite().getShell().getDisplay().asyncExec(new Runnable()
              {
                public void run()
                {
                  updateProblemIndication();
                }
              });
            }
            break;
          }
        }
      }
      else
      {
        super.notifyChanged(notification);
      }
    }

    @Override
    protected void setTarget(Resource target)
    {
      basicSetTarget(target);
    }

    @Override
    protected void unsetTarget(Resource target)
    {
      basicUnsetTarget(target);
      resourceToDiagnosticMap.remove(target);
      if (updateProblemIndication)
      {
        getSite().getShell().getDisplay().asyncExec(new Runnable()
        {
          public void run()
          {
            updateProblemIndication();
          }
        });
      }
    }
  };

  /**
   * This listens for workspace changes.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IResourceChangeListener resourceChangeListener = new IResourceChangeListener()
  {
    public void resourceChanged(IResourceChangeEvent event)
    {
      IResourceDelta delta = event.getDelta();
      try
      {
        class ResourceDeltaVisitor implements IResourceDeltaVisitor
        {
          protected ResourceSet resourceSet = editingDomain.getResourceSet();

          protected Collection<Resource> changedResources = new ArrayList<Resource>();

          protected Collection<Resource> removedResources = new ArrayList<Resource>();

          public boolean visit(final IResourceDelta delta)
          {
            if (delta.getResource().getType() == IResource.FILE)
            {
              if (delta.getKind() == IResourceDelta.REMOVED || delta.getKind() == IResourceDelta.CHANGED)
              {
                final Resource resource = resourceSet.getResource(URI.createPlatformResourceURI(delta.getFullPath().toString(), true), false);
                if (resource != null)
                {
                  if (delta.getKind() == IResourceDelta.REMOVED)
                  {
                    removedResources.add(resource);
                  }
                  else
                  {
                    if ((delta.getFlags() & IResourceDelta.MARKERS) != 0)
                    {
                      DiagnosticDecorator.DiagnosticAdapter.update(resource, markerHelper.getMarkerDiagnostics(resource, (IFile)delta.getResource()));
                    }
                    if ((delta.getFlags() & IResourceDelta.CONTENT) != 0)
                    {
                      if (!savedResources.remove(resource))
                      {
                        changedResources.add(resource);
                      }
                    }
                  }
                }
              }
              return false;
            }

            return true;
          }

          public Collection<Resource> getChangedResources()
          {
            return changedResources;
          }

          public Collection<Resource> getRemovedResources()
          {
            return removedResources;
          }
        }

        final ResourceDeltaVisitor visitor = new ResourceDeltaVisitor();
        delta.accept(visitor);

        if (!visitor.getRemovedResources().isEmpty())
        {
          getSite().getShell().getDisplay().asyncExec(new Runnable()
          {
            public void run()
            {
              removedResources.addAll(visitor.getRemovedResources());
              if (!isDirty())
              {
                getSite().getPage().closeEditor(SetupEditor.this, false);
              }
            }
          });
        }

        if (!visitor.getChangedResources().isEmpty())
        {
          getSite().getShell().getDisplay().asyncExec(new Runnable()
          {
            public void run()
            {
              changedResources.addAll(visitor.getChangedResources());
              if (getSite().getPage().getActiveEditor() == SetupEditor.this)
              {
                handleActivate();
              }
            }
          });
        }
      }
      catch (CoreException exception)
      {
        SetupEditorPlugin.INSTANCE.log(exception);
      }
    }
  };

  /**
   * Handles activation of the editor or it's associated views.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void handleActivate()
  {
    // Recompute the read only state.
    //
    if (editingDomain.getResourceToReadOnlyMap() != null)
    {
      editingDomain.getResourceToReadOnlyMap().clear();

      // Refresh any actions that may become enabled or disabled.
      //
      setSelection(getSelection());
    }

    if (!removedResources.isEmpty())
    {
      if (handleDirtyConflict())
      {
        getSite().getPage().closeEditor(SetupEditor.this, false);
      }
      else
      {
        removedResources.clear();
        changedResources.clear();
        savedResources.clear();
      }
    }
    else if (!changedResources.isEmpty())
    {
      changedResources.removeAll(savedResources);
      handleChangedResources();
      changedResources.clear();
      savedResources.clear();
    }
  }

  /**
   * Handles what to do with changed resources on activation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void handleChangedResources()
  {
    if (!changedResources.isEmpty() && (!isDirty() || handleDirtyConflict()))
    {
      if (isDirty())
      {
        changedResources.addAll(editingDomain.getResourceSet().getResources());
      }
      editingDomain.getCommandStack().flush();

      updateProblemIndication = false;
      for (Resource resource : changedResources)
      {
        if (resource.isLoaded())
        {
          resource.unload();
          try
          {
            resource.load(Collections.EMPTY_MAP);
          }
          catch (IOException exception)
          {
            if (!resourceToDiagnosticMap.containsKey(resource))
            {
              resourceToDiagnosticMap.put(resource, analyzeResourceProblems(resource, exception));
            }
          }
        }
      }

      if (AdapterFactoryEditingDomain.isStale(editorSelection))
      {
        setSelection(StructuredSelection.EMPTY);
      }

      updateProblemIndication = true;
      updateProblemIndication();
    }
  }

  /**
   * Updates the problems indication with the information described in the specified diagnostic.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void updateProblemIndication()
  {
    if (updateProblemIndication)
    {
      BasicDiagnostic diagnostic = new BasicDiagnostic(Diagnostic.OK, "org.eclipse.oomph.setup.editor", 0, null,
          new Object[] { editingDomain.getResourceSet() });
      for (Diagnostic childDiagnostic : resourceToDiagnosticMap.values())
      {
        if (childDiagnostic.getSeverity() != Diagnostic.OK)
        {
          diagnostic.add(childDiagnostic);
        }
      }

      int lastEditorPage = getPageCount() - 1;
      if (lastEditorPage >= 0 && getEditor(lastEditorPage) instanceof ProblemEditorPart)
      {
        ((ProblemEditorPart)getEditor(lastEditorPage)).setDiagnostic(diagnostic);
        if (diagnostic.getSeverity() != Diagnostic.OK)
        {
          setActivePage(lastEditorPage);
        }
      }
      else if (diagnostic.getSeverity() != Diagnostic.OK)
      {
        ProblemEditorPart problemEditorPart = new ProblemEditorPart();
        problemEditorPart.setDiagnostic(diagnostic);
        problemEditorPart.setMarkerHelper(markerHelper);
        try
        {
          addPage(++lastEditorPage, problemEditorPart, getEditorInput());
          setPageText(lastEditorPage, problemEditorPart.getPartName());
          setActivePage(lastEditorPage);
          showTabs();
        }
        catch (PartInitException exception)
        {
          SetupEditorPlugin.INSTANCE.log(exception);
        }
      }

      if (markerHelper.hasMarkers(editingDomain.getResourceSet()))
      {
        markerHelper.deleteMarkers(editingDomain.getResourceSet());
        if (diagnostic.getSeverity() != Diagnostic.OK)
        {
          try
          {
            markerHelper.createMarkers(diagnostic);
          }
          catch (CoreException exception)
          {
            SetupEditorPlugin.INSTANCE.log(exception);
          }
        }
      }
    }
  }

  /**
   * Shows a dialog that asks if conflicting changes should be discarded.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected boolean handleDirtyConflict()
  {
    return MessageDialog.openQuestion(getSite().getShell(), getString("_UI_FileConflict_label"), getString("_WARN_FileConflict"));
  }

  /**
   * This creates a model editor.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public SetupEditor()
  {
    super();
    initializeEditingDomain();
  }

  /**
   * This sets up the editing domain for the model editor.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void initializeEditingDomainGen()
  {
    // Create an adapter factory that yields item providers.
    //
    adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

    adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new SetupItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new BaseItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

    // Create the command stack that will notify this editor as commands are executed.
    //
    BasicCommandStack commandStack = new BasicCommandStack();

    // Add a listener to set the most recent command's affected objects to be the selection of the viewer with focus.
    //
    commandStack.addCommandStackListener(new CommandStackListener()
    {
      public void commandStackChanged(final EventObject event)
      {
        getContainer().getDisplay().asyncExec(new Runnable()
        {
          public void run()
          {
            firePropertyChange(IEditorPart.PROP_DIRTY);

            // Try to select the affected objects.
            //
            Command mostRecentCommand = ((CommandStack)event.getSource()).getMostRecentCommand();
            if (mostRecentCommand != null)
            {
              setSelectionToViewer(mostRecentCommand.getAffectedObjects());
            }
            for (Iterator<PropertySheetPage> i = propertySheetPages.iterator(); i.hasNext();)
            {
              PropertySheetPage propertySheetPage = i.next();
              if (propertySheetPage.getControl().isDisposed())
              {
                i.remove();
              }
              else
              {
                propertySheetPage.refresh();
              }
            }
          }
        });
      }
    });

    // Create the editing domain with a special command stack.
    //
    editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<Resource, Boolean>());
  }

  protected void initializeEditingDomain()
  {
    initializeEditingDomainGen();

    adapterFactory.insertAdapterFactory(new ResourceItemProviderAdapterFactory()
    {
      @Override
      public Adapter createResourceAdapter()
      {
        return new ResourceItemProvider(this)
        {
          @Override
          public Collection<?> getChildren(Object object)
          {
            Resource resource = (Resource)object;
            Object[] contents = getContents(resource);
            return Arrays.asList(contents);
          }
        };
      }
    });

    // Create the editing domain with a special command stack.
    //
    editingDomain = new AdapterFactoryEditingDomain(adapterFactory, editingDomain.getCommandStack(), new HashMap<Resource, Boolean>()
    {
      private static final long serialVersionUID = 1L;

      @Override
      public Boolean get(Object key)
      {
        return !editingDomain.getResourceSet().getResources().contains(key) ? Boolean.TRUE : super.get(key);
      }
    });

    EMFUtil.configureResourceSet(editingDomain.getResourceSet());

    // Add a listener to set the most recent command's affected objects to be the selection of the viewer with focus.
    //
    editingDomain.getCommandStack().addCommandStackListener(new CommandStackListener()
    {
      public void commandStackChanged(final EventObject event)
      {
        if (contentOutlinePage != null)
        {
          contentOutlinePage.update();
        }
      }
    });
  }

  /**
   * This is here for the listener to be able to call it.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected void firePropertyChange(int action)
  {
    super.firePropertyChange(action);
  }

  /**
   * This sets the selection into whichever viewer is active.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSelectionToViewer(Collection<?> collection)
  {
    final Collection<?> theSelection = collection;
    // Make sure it's okay.
    //
    if (theSelection != null && !theSelection.isEmpty())
    {
      Runnable runnable = new Runnable()
      {
        public void run()
        {
          // Try to select the items in the current content viewer of the editor.
          //
          if (currentViewer != null)
          {
            currentViewer.setSelection(new StructuredSelection(theSelection.toArray()), true);
          }
        }
      };
      getSite().getShell().getDisplay().asyncExec(runnable);
    }
  }

  /**
   * This returns the editing domain as required by the {@link IEditingDomainProvider} interface.
   * This is important for implementing the static methods of {@link AdapterFactoryEditingDomain}
   * and for supporting {@link org.eclipse.emf.edit.ui.action.CommandAction}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EditingDomain getEditingDomain()
  {
    return editingDomain;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public class ReverseAdapterFactoryContentProvider extends AdapterFactoryContentProvider
  {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ReverseAdapterFactoryContentProvider(AdapterFactory adapterFactory)
    {
      super(adapterFactory);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object[] getElements(Object object)
    {
      Object parent = super.getParent(object);
      return (parent == null ? Collections.EMPTY_SET : Collections.singleton(parent)).toArray();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object[] getChildren(Object object)
    {
      Object parent = super.getParent(object);
      return (parent == null ? Collections.EMPTY_SET : Collections.singleton(parent)).toArray();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean hasChildren(Object object)
    {
      Object parent = super.getParent(object);
      return parent != null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object getParent(Object object)
    {
      return null;
    }
  }

  /**
   * This makes sure that one content viewer, either for the current page or the outline view, if it has focus,
   * is the current one.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCurrentViewer(Viewer viewer)
  {
    // If it is changing...
    //
    if (currentViewer != viewer)
    {
      if (selectionChangedListener == null)
      {
        // Create the listener on demand.
        //
        selectionChangedListener = new ISelectionChangedListener()
        {
          // This just notifies those things that are affected by the section.
          //
          public void selectionChanged(SelectionChangedEvent selectionChangedEvent)
          {
            setSelection(selectionChangedEvent.getSelection());
          }
        };
      }

      // Stop listening to the old one.
      //
      if (currentViewer != null)
      {
        currentViewer.removeSelectionChangedListener(selectionChangedListener);
      }

      // Start listening to the new one.
      //
      if (viewer != null)
      {
        viewer.addSelectionChangedListener(selectionChangedListener);
      }

      // Remember it.
      //
      currentViewer = viewer;

      // Set the editors selection based on the current viewer's selection.
      //
      setSelection(currentViewer == null ? StructuredSelection.EMPTY : currentViewer.getSelection());
    }
  }

  /**
   * This returns the viewer as required by the {@link IViewerProvider} interface.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Viewer getViewer()
  {
    return currentViewer;
  }

  /**
   * This creates a context menu for the viewer and adds a listener as well registering the menu for extension.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void createContextMenuFor(StructuredViewer viewer)
  {
    MenuManager contextMenu = new MenuManager("#PopUp");
    contextMenu.add(new Separator("additions"));
    contextMenu.setRemoveAllWhenShown(true);
    contextMenu.addMenuListener(this);
    Menu menu = contextMenu.createContextMenu(viewer.getControl());
    viewer.getControl().setMenu(menu);
    getSite().registerContextMenu(contextMenu, new UnwrappingSelectionProvider(viewer));

    int dndOperations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
    Transfer[] transfers = new Transfer[] { LocalTransfer.getInstance(), LocalSelectionTransfer.getTransfer(), FileTransfer.getInstance() };
    viewer.addDragSupport(dndOperations, transfers, new ViewerDragAdapter(viewer));
    viewer.addDropSupport(dndOperations, transfers, new EditingDomainViewerDropAdapter(editingDomain, viewer));
  }

  /**
   * This is the method called to load a resource into the editing domain's resource set based on the editor's input.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createModelGen()
  {
    URI resourceURI = EditUIUtil.getURI(getEditorInput());
    Exception exception = null;
    Resource resource = null;
    try
    {
      // Load the resource through the editing domain.
      //
      resource = editingDomain.getResourceSet().getResource(resourceURI, true);
    }
    catch (Exception e)
    {
      exception = e;
      resource = editingDomain.getResourceSet().getResource(resourceURI, false);
    }

    Diagnostic diagnostic = analyzeResourceProblems(resource, exception);
    if (diagnostic.getSeverity() != Diagnostic.OK)
    {
      resourceToDiagnosticMap.put(resource, analyzeResourceProblems(resource, exception));
    }
    editingDomain.getResourceSet().eAdapters().add(problemIndicationAdapter);
  }

  public void createModel()
  {
    createModelGen();

    ResourceSet resourceSet = editingDomain.getResourceSet();
    Resource mainResource = resourceSet.getResources().get(0);
    EObject rootObject = mainResource.getContents().get(0);
    if (!(rootObject instanceof Index))
    {
      EMFUtil.loadResourceSafely(resourceSet, SetupContext.INDEX_SETUP_URI);
    }

    EPackage ePackage = rootObject.eClass().getEPackage();
    URI ePackageResourceURI = ePackage.eResource().getURI();
    if (ePackageResourceURI.isHierarchical() && ePackageResourceURI.trimSegments(1).equals(LEGACY_MODELS))
    {
      List<EObject> migratedContents = new ArrayList<EObject>();
      try
      {
        EMFUtil.migrate(mainResource, migratedContents);
        CompoundCommand command = new CompoundCommand(1, "Replace with Migrated Contents");
        command.append(new RemoveCommand(editingDomain, mainResource.getContents(), new ArrayList<EObject>(mainResource.getContents())));
        command.append(new AddCommand(editingDomain, mainResource.getContents(), migratedContents));
        editingDomain.getCommandStack().execute(command);
      }
      catch (RuntimeException ex)
      {
        CompoundCommand command = new CompoundCommand(1, "Add Partially Migrated Contents");
        command.append(new AddCommand(editingDomain, mainResource.getContents(), migratedContents));
        editingDomain.getCommandStack().execute(command);

        SetupEditorPlugin.INSTANCE.log(ex);
      }

      EcoreUtil.resolveAll(mainResource);
      for (Resource resource : resourceSet.getResources())
      {
        URI uri = resource.getURI();
        if ("bogus".equals(uri.scheme()) || LEGACY_EXAMPLE_URI.equals(uri))
        {
          resource.getErrors().clear();
          resource.getWarnings().clear();
        }
      }
    }
  }

  /**
   * Returns a diagnostic describing the errors and warnings listed in the resource
   * and the specified exception (if any).
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Diagnostic analyzeResourceProblems(Resource resource, Exception exception)
  {
    if (!resource.getErrors().isEmpty() || !resource.getWarnings().isEmpty())
    {
      BasicDiagnostic basicDiagnostic = new BasicDiagnostic(Diagnostic.ERROR, "org.eclipse.oomph.setup.editor", 0, getString("_UI_CreateModelError_message",
          resource.getURI()), new Object[] { exception == null ? (Object)resource : exception });
      basicDiagnostic.merge(EcoreUtil.computeDiagnostic(resource, true));
      return basicDiagnostic;
    }
    else if (exception != null)
    {
      return new BasicDiagnostic(Diagnostic.ERROR, "org.eclipse.oomph.setup.editor", 0, getString("_UI_CreateModelError_message", resource.getURI()),
          new Object[] { exception });
    }
    else
    {
      return Diagnostic.OK_INSTANCE;
    }
  }

  /**
   * This is the method used by the framework to install your own controls.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public void createPages()
  {
    // Creates the model from the editor input
    //
    createModel();

    // Only creates the other pages if there is something that can be edited
    //
    if (!getEditingDomain().getResourceSet().getResources().isEmpty())
    {
      // Create a page for the selection tree view.
      //
      Tree tree = new Tree(getContainer(), SWT.MULTI);
      selectionViewer = new TreeViewer(tree);
      setCurrentViewer(selectionViewer);

      selectionViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
      selectionViewer.setLabelProvider(new DecoratingColumLabelProvider(new SetupLabelProvider(adapterFactory, selectionViewer), new DiagnosticDecorator(
          editingDomain, selectionViewer, SetupEditorPlugin.getPlugin().getDialogSettings())));

      Resource resource = editingDomain.getResourceSet().getResources().get(0);
      selectionViewer.setInput(resource);

      EObject rootObject = resource.getContents().get(0);
      if (rootObject instanceof Project)
      {
        for (Stream branch : ((Project)rootObject).getStreams())
        {
          selectionViewer.expandToLevel(branch, 1);
        }
      }
      else
      {
        selectionViewer.expandToLevel(2);
      }

      selectionViewer.setSelection(new StructuredSelection(rootObject), true);

      getViewer().getControl().addMouseListener(new MouseListener()
      {
        public void mouseDoubleClick(MouseEvent e)
        {
          try
          {
            getSite().getPage().showView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
          }
          catch (PartInitException ex)
          {
            SetupEditorPlugin.INSTANCE.log(ex);
          }
        }

        public void mouseDown(MouseEvent e)
        {
          // Do nothing
        }

        public void mouseUp(MouseEvent e)
        {
          // Do nothing
        }
      });

      new AdapterFactoryTreeEditor(selectionViewer.getTree(), adapterFactory);
      new ColumnViewerInformationControlToolTipSupport(selectionViewer, new DiagnosticDecorator.EditingDomainLocationListener(editingDomain, selectionViewer));

      createContextMenuFor(selectionViewer);
      int pageIndex = addPage(tree);
      setPageText(pageIndex, getString("_UI_SelectionPage_label"));

      getSite().getShell().getDisplay().asyncExec(new Runnable()
      {
        public void run()
        {
          setActivePage(0);
        }
      });
    }

    // Ensures that this editor will only display the page's tab
    // area if there are more than one page
    //
    getContainer().addControlListener(new ControlAdapter()
    {
      boolean guard = false;

      @Override
      public void controlResized(ControlEvent event)
      {
        if (!guard)
        {
          guard = true;
          hideTabs();
          guard = false;
        }
      }
    });

    getSite().getShell().getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        updateProblemIndication();
      }
    });
  }

  /**
   * If there is just one page in the multi-page editor part,
   * this hides the single tab at the bottom.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void hideTabs()
  {
    if (getPageCount() <= 1)
    {
      setPageText(0, "");
      if (getContainer() instanceof CTabFolder)
      {
        ((CTabFolder)getContainer()).setTabHeight(1);
        Point point = getContainer().getSize();
        getContainer().setSize(point.x, point.y + 6);
      }
    }
  }

  /**
   * If there is more than one page in the multi-page editor part,
   * this shows the tabs at the bottom.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void showTabs()
  {
    if (getPageCount() > 1)
    {
      setPageText(0, getString("_UI_SelectionPage_label"));
      if (getContainer() instanceof CTabFolder)
      {
        ((CTabFolder)getContainer()).setTabHeight(SWT.DEFAULT);
        Point point = getContainer().getSize();
        getContainer().setSize(point.x, point.y - 6);
      }
    }
  }

  /**
   * This is used to track the active viewer.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected void pageChange(int pageIndex)
  {
    super.pageChange(pageIndex);

    if (contentOutlinePage != null)
    {
      handleContentOutlineSelection(contentOutlinePage.getSelection());
    }
  }

  /**
   * This is how the framework determines which interfaces we implement.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("rawtypes")
  @Override
  public Object getAdapter(Class key)
  {
    if (key.equals(IContentOutlinePage.class))
    {
      return showOutlineView() ? getContentOutlinePage() : null;
    }
    else if (key.equals(IPropertySheetPage.class))
    {
      return getPropertySheetPage();
    }
    else if (key.equals(IGotoMarker.class))
    {
      return this;
    }
    else
    {
      return super.getAdapter(key);
    }
  }

  /**
   * @author Ed Merks
   */
  class OutlinePreviewPage extends ContentOutlinePage
  {
    private ILabelProvider labelProvider;

    private Trigger trigger;

    private int xxx;// TODO Remove productVersion

    private ProductVersion productVersion;

    private Map<Object, Object> copyMap = new HashMap<Object, Object>();

    private Map<Object, Set<Object>> inverseCopyMap = new HashMap<Object, Set<Object>>();

    private Map<Object, Object> parents = new HashMap<Object, Object>();

    private AdapterFactoryEditingDomain.EditingDomainProvider editingDomainProvider = new AdapterFactoryEditingDomain.EditingDomainProvider(editingDomain);

    private class VariableContainer extends ItemProvider
    {
      private SetupTaskPerformer setupTaskPerformer;

      public VariableContainer(SetupTaskPerformer setupTaskPerformer, String text, Object image)
      {
        super(text, image);
        this.setupTaskPerformer = setupTaskPerformer;
      }

      public SetupTaskPerformer getSetupTaskPerformer()
      {
        return setupTaskPerformer;
      }
    }

    @Override
    public void createControl(Composite parent)
    {
      super.createControl(parent);
      contentOutlineViewer = getTreeViewer();
      contentOutlineViewer.addSelectionChangedListener(this);

      contentOutlineViewer.addDoubleClickListener(new IDoubleClickListener()
      {
        public void doubleClick(DoubleClickEvent event)
        {
          if (selectionViewer != null)
          {
            selectionViewer.getControl().setFocus();
            SetupEditor.this.setSelection(selectionViewer.getSelection());
          }
        }
      });

      // Set up the tree viewer.
      //
      contentOutlineViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory)
      {
        @Override
        public boolean hasChildren(Object object)
        {
          return object instanceof VariableTask && parents.get(object) instanceof VariableContainer || super.hasChildren(object);
        }

        @Override
        public Object[] getChildren(Object object)
        {
          if (object instanceof VariableTask && parents.get(object) instanceof VariableContainer)
          {
            Resource resource = editingDomain.getResourceSet().getResources().get(0);
            SetupTaskPerformer setupTaskPerformer = ((VariableContainer)parents.get(object)).getSetupTaskPerformer();
            VariableTask contextVariableTask = (VariableTask)object;
            String name = contextVariableTask.getName();
            List<EObject> variableUsages = new ArrayList<EObject>();
            for (Object o : copyMap.keySet())
            {
              if (o instanceof EObject)
              {
                EObject eObject = (EObject)o;
                if (eObject.eResource() == resource)
                {
                  for (EAttribute attribute : eObject.eClass().getEAllAttributes())
                  {
                    if (attribute.isChangeable() && attribute.getEAttributeType().getInstanceClassName() == "java.lang.String"
                        && attribute != SetupPackage.Literals.VARIABLE_TASK__NAME)
                    {
                      if (attribute.isMany())
                      {
                        @SuppressWarnings("unchecked")
                        List<String> values = (List<String>)eObject.eGet(attribute);
                        for (String value : values)
                        {
                          Set<String> variables = setupTaskPerformer.getVariables(value);
                          if (variables.contains(name))
                          {
                            variableUsages.add(eObject);
                            break;
                          }
                        }
                      }
                      else
                      {
                        String value = (String)eObject.eGet(attribute);
                        if (value != null)
                        {
                          Set<String> variables = setupTaskPerformer.getVariables(value);
                          if (variables.contains(name))
                          {
                            variableUsages.add(eObject);
                            break;
                          }
                        }
                      }
                    }
                  }
                }
              }
            }

            for (EObject eObject : variableUsages)
            {
              parents.put(eObject, object);
            }

            return variableUsages.toArray();
          }

          return super.getChildren(object);
        }

        @Override
        public Object getParent(Object object)
        {
          Object parent = parents.get(object);
          return parent != null ? parent : super.getParent(object);
        }
      });

      final Font font = contentOutlineViewer.getControl().getFont();
      labelProvider = new DecoratingColumLabelProvider(new SetupLabelProvider(adapterFactory, contentOutlineViewer), new DiagnosticDecorator(editingDomain,
          contentOutlineViewer, SetupEditorPlugin.getPlugin().getDialogSettings()))
      {
        @Override
        public Font getFont(Object object)
        {
          Font result = super.getFont(object);
          if (object instanceof SetupTask)
          {
            SetupTask setupTask = (SetupTask)object;
            Resource resource = setupTask.eResource();
            if (resource == null || !editingDomain.getResourceSet().getResources().get(0).getURI().equals(resource.getURI()))
            {
              result = ExtendedFontRegistry.INSTANCE.getFont(result != null ? result : font, IItemFontProvider.ITALIC_FONT);
            }
          }

          return result;
        }
      };
      contentOutlineViewer.setLabelProvider(labelProvider);

      new ColumnViewerInformationControlToolTipSupport(contentOutlineViewer, new DiagnosticDecorator.EditingDomainLocationListener(editingDomain,
          contentOutlineViewer));

      // Make sure our popups work.
      //
      createContextMenuFor(contentOutlineViewer);

      selectionViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        public void selectionChanged(SelectionChangedEvent event)
        {
          IStructuredSelection selection = (IStructuredSelection)event.getSelection();
          if (selectionViewer != null && !selection.isEmpty())
          {
            ArrayList<Object> selectionList = new ArrayList<Object>();
            for (Object object : selection.toArray())
            {
              collectSelection(selectionList, object);
            }

            if (!selectionList.isEmpty())
            {
              contentOutlinePage = null;
              getTreeViewer().setSelection(new StructuredSelection(selectionList));
              contentOutlinePage = OutlinePreviewPage.this;
            }
          }
        }

        private void collectSelection(List<Object> selection, Object object)
        {
          if (object instanceof CompoundTask)
          {
            for (SetupTask setupTask : ((CompoundTask)object).getSetupTasks())
            {
              collectSelection(selection, setupTask);
            }
          }
          else
          {
            Object copy = contentOutlinePage.getCopy(object);
            if (copy != null)
            {
              selection.add(copy);
            }
          }
        }
      });

      update();

      contentOutlineViewer.expandToLevel(2);
    }

    public Set<Object> getOriginals(Object object)
    {
      Set<Object> result = inverseCopyMap.get(object);
      return result == null ? Collections.singleton(object) : result;
    }

    public Object getCopy(Object object)
    {
      return copyMap.get(object);
    }

    public void update()
    {
      copyMap.clear();

      try
      {
        ResourceSet resourceSet = editingDomain.getResourceSet();
        Resource resource = resourceSet.getResources().get(0);
        EObject eObject = resource.getContents().get(0);
        if (productVersion == null)
        {
          if (eObject instanceof Project)
          {
            Project project = (Project)eObject;
            ProjectCatalog projectCatalog = project.getProjectCatalog();
            if (projectCatalog != null)
            {
              EObject rootContainer = EcoreUtil.getRootContainer(projectCatalog);
              if (rootContainer instanceof Index)
              {
                Index index = (Index)rootContainer;
                LOOP: for (ProductCatalog productCatalog : index.getProductCatalogs())
                {
                  for (Product product : productCatalog.getProducts())
                  {
                    for (ProductVersion productVersion : product.getVersions())
                    {
                      this.productVersion = productVersion;
                      break LOOP;
                    }
                  }
                }
              }
            }
          }
        }

        if (productVersion != null)
        {
          Project project = (Project)eObject;
          ItemProvider input = getTriggeredTasks(project);
          getTreeViewer().setInput(input);
        }
      }
      catch (Exception ex)
      {
        SetupEditorPlugin.INSTANCE.log(ex);
      }

      for (Map.Entry<Object, Object> entry : copyMap.entrySet())
      {
        Object value = entry.getValue();
        Set<Object> eObjects = inverseCopyMap.get(value);
        if (eObjects == null)
        {
          eObjects = new HashSet<Object>();
          inverseCopyMap.put(value, eObjects);
        }
        eObjects.add(entry.getKey());
      }
    }

    private ItemProvider getTriggeredTasks(Project project)
    {
      final ItemProvider projectItem = new ItemProvider(labelProvider.getText(project), labelProvider.getImage(project));

      EList<Object> projectItemChildren = projectItem.getChildren();
      EList<Stream> streams = project.getStreams();
      for (Stream stream : streams)
      {
        final ItemProvider branchItem = new ItemProvider(labelProvider.getText(stream), labelProvider.getImage(stream));
        projectItemChildren.add(branchItem);

        // String installFolder = "/${" + "FIXME" + '}';
        SetupTaskPerformer setupTaskPerformer = new SetupTaskPerformer(getEditingDomain().getResourceSet().getURIConverter(), SetupPrompter.CANCEL, trigger,
            SetupContext.create(productVersion, stream), stream);
        List<SetupTask> triggeredSetupTasks = new ArrayList<SetupTask>(setupTaskPerformer.getTriggeredSetupTasks());

        if (!triggeredSetupTasks.isEmpty())
        {
          for (EObject eObject : setupTaskPerformer.getCopyMap().values())
          {
            Resource resource = ((InternalEObject)eObject).eDirectResource();
            if (resource != null && !resource.eAdapters().contains(editingDomainProvider))
            {
              resource.eAdapters().add(editingDomainProvider);
            }
          }

          ItemProvider undeclaredVariablesItem = new VariableContainer(setupTaskPerformer, "Undeclared Variables", UNDECLARED_VARIABLE_GROUP_IMAGE);
          EList<Object> undeclaredVariablesItemChildren = undeclaredVariablesItem.getChildren();
          Set<String> undeclaredVariables = setupTaskPerformer.getUndeclaredVariables();
          for (String key : undeclaredVariables)
          {
            VariableTask contextVariableTask = SetupFactory.eINSTANCE.createVariableTask();
            contextVariableTask.setName(key);
            undeclaredVariablesItemChildren.add(contextVariableTask);
            parents.put(contextVariableTask, undeclaredVariablesItem);
          }

          if (!undeclaredVariablesItemChildren.isEmpty())
          {
            branchItem.getChildren().add(undeclaredVariablesItem);
          }

          ItemProvider unresolvedVariablesItem = new VariableContainer(setupTaskPerformer, "Unresolved Variables", VARIABLE_GROUP_IMAGE);
          EList<Object> unresolvedVariablesItemChildren = unresolvedVariablesItem.getChildren();
          List<VariableTask> unresolvedVariables = setupTaskPerformer.getUnresolvedVariables();
          for (VariableTask contextVariableTask : unresolvedVariables)
          {
            unresolvedVariablesItemChildren.add(contextVariableTask);
            parents.put(contextVariableTask, unresolvedVariablesItem);
          }

          if (!unresolvedVariablesItemChildren.isEmpty())
          {
            branchItem.getChildren().add(unresolvedVariablesItem);
          }

          ItemProvider resolvedVariablesItem = new VariableContainer(setupTaskPerformer, "Resolved Variables", VARIABLE_GROUP_IMAGE);
          EList<Object> resolvedVariablesItemChildren = resolvedVariablesItem.getChildren();
          List<VariableTask> resolvedVariables = setupTaskPerformer.getResolvedVariables();
          for (VariableTask contextVariableTask : resolvedVariables)
          {
            resolvedVariablesItemChildren.add(contextVariableTask);
            parents.put(contextVariableTask, resolvedVariablesItem);
          }

          if (!resolvedVariablesItemChildren.isEmpty())
          {
            branchItem.getChildren().add(resolvedVariablesItem);
          }

          branchItem.getChildren().addAll(triggeredSetupTasks);

          for (SetupTask setupTask : triggeredSetupTasks)
          {
            parents.put(setupTask, branchItem);
          }

          copyMap.putAll(setupTaskPerformer.getCopyMap());
          copyMap.put(stream, branchItem);
        }

        copyMap.put(project, projectItem);
      }

      return projectItem;
    }

    @Override
    public void makeContributions(IMenuManager menuManager, IToolBarManager toolBarManager, IStatusLineManager statusLineManager)
    {
      super.makeContributions(menuManager, toolBarManager, statusLineManager);
      contentOutlineStatusLineManager = statusLineManager;
    }

    @Override
    public void setActionBars(IActionBars actionBars)
    {
      super.setActionBars(actionBars);

      actionBars.getToolBarManager().add(new Action("Show tasks for all triggers", IAction.AS_RADIO_BUTTON)
      {
        {
          setChecked(true);
          setImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(SetupEditorPlugin.INSTANCE.getImage("AllTrigger")));
        }

        @Override
        public void run()
        {
          trigger = null;
          update();
          getTreeViewer().expandToLevel(2);
        }
      });

      for (final Trigger trigger : Trigger.VALUES)
      {
        final String label = trigger.getLiteral().toLowerCase();
        actionBars.getToolBarManager().add(new Action("Show tasks for the " + label + " trigger", IAction.AS_RADIO_BUTTON)
        {
          {
            setImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(SetupEditorPlugin.INSTANCE.getImage(StringUtil.cap(label) + "Trigger")));
          }

          @Override
          public void run()
          {
            OutlinePreviewPage.this.trigger = trigger;
            super.run();
            update();
            getTreeViewer().expandToLevel(2);
          }
        });
      }

      getActionBarContributor().shareGlobalActions(this, actionBars);
    }
  }

  /**
   * This accesses a cached version of the content outliner.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public IContentOutlinePage getContentOutlinePage()
  {
    if (contentOutlinePage == null)
    {
      // The content outline is just a tree.
      //
      contentOutlinePage = new OutlinePreviewPage();

      // Listen to selection so that we can handle it is a special way.
      //
      contentOutlinePage.addSelectionChangedListener(new ISelectionChangedListener()
      {
        // This ensures that we handle selections correctly.
        //
        public void selectionChanged(SelectionChangedEvent event)
        {
          handleContentOutlineSelection(event.getSelection());
        }
      });
    }

    return contentOutlinePage;
  }

  /**
   * This accesses a cached version of the property sheet.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public IPropertySheetPage getPropertySheetPage()
  {
    PropertySheetPage propertySheetPage = new ExtendedPropertySheetPage(editingDomain)
    {
      {
        setSorter(new PropertySheetSorter()
        {
          @Override
          public void sort(IPropertySheetEntry[] entries)
          {
            // Intentionally left empty
          }
        });
      }

      @Override
      public void setSelectionToViewer(List<?> selection)
      {
        SetupEditor.this.setSelectionToViewer(selection);
        SetupEditor.this.setFocus();
      }

      @Override
      public void setActionBars(IActionBars actionBars)
      {
        super.setActionBars(actionBars);
        getActionBarContributor().shareGlobalActions(this, actionBars);
      }
    };
    propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));
    propertySheetPages.add(propertySheetPage);

    return propertySheetPage;
  }

  /**
   * This deals with how we want selection in the outliner to affect the other views.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void handleContentOutlineSelection(ISelection selection)
  {
    if (contentOutlinePage != null && selectionViewer != null && !selection.isEmpty() && selection instanceof IStructuredSelection)
    {
      Iterator<?> selectedElements = ((IStructuredSelection)selection).iterator();
      if (selectedElements.hasNext())
      {
        Object selectedElement = selectedElements.next();

        ArrayList<Object> selectionList = new ArrayList<Object>();
        selectionList.addAll(contentOutlinePage.getOriginals(selectedElement));
        while (selectedElements.hasNext())
        {
          selectionList.addAll(contentOutlinePage.getOriginals(selectedElements.next()));
        }

        TreeViewer oldSectionViewer = selectionViewer;
        selectionViewer = null;
        oldSectionViewer.setSelection(new StructuredSelection(selectionList));
        selectionViewer = oldSectionViewer;
      }
    }
  }

  /**
   * This is for implementing {@link IEditorPart} and simply tests the command stack.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isDirty()
  {
    return ((BasicCommandStack)editingDomain.getCommandStack()).isSaveNeeded();
  }

  /**
   * This is for implementing {@link IEditorPart} and simply saves the model file.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void doSaveGen(IProgressMonitor progressMonitor)
  {
    // Save only resources that have actually changed.
    //
    final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
    saveOptions.put(Resource.OPTION_SAVE_ONLY_IF_CHANGED, Resource.OPTION_SAVE_ONLY_IF_CHANGED_MEMORY_BUFFER);
    saveOptions.put(Resource.OPTION_LINE_DELIMITER, Resource.OPTION_LINE_DELIMITER_UNSPECIFIED);

    // Do the work within an operation because this is a long running activity that modifies the workbench.
    //
    WorkspaceModifyOperation operation = new WorkspaceModifyOperation()
    {
      // This is the method that gets invoked when the operation runs.
      //
      @Override
      public void execute(IProgressMonitor monitor)
      {
        // Save the resources to the file system.
        //
        boolean first = true;
        for (Resource resource : editingDomain.getResourceSet().getResources())
        {
          if ((first || !resource.getContents().isEmpty() || isPersisted(resource)) && !editingDomain.isReadOnly(resource))
          {
            try
            {
              long timeStamp = resource.getTimeStamp();
              resource.save(saveOptions);
              if (resource.getTimeStamp() != timeStamp)
              {
                savedResources.add(resource);
              }
            }
            catch (Exception exception)
            {
              resourceToDiagnosticMap.put(resource, analyzeResourceProblems(resource, exception));
            }
            first = false;
          }
        }
      }
    };

    updateProblemIndication = false;
    try
    {
      // This runs the options, and shows progress.
      //
      new ProgressMonitorDialog(getSite().getShell()).run(true, false, operation);

      // Refresh the necessary state.
      //
      ((BasicCommandStack)editingDomain.getCommandStack()).saveIsDone();
      firePropertyChange(IEditorPart.PROP_DIRTY);
    }
    catch (Exception exception)
    {
      // Something went wrong that shouldn't.
      //
      SetupEditorPlugin.INSTANCE.log(exception);
    }
    updateProblemIndication = true;
    updateProblemIndication();
  }

  @Override
  public void doSave(IProgressMonitor monitor)
  {
    EList<Resource> resources = editingDomain.getResourceSet().getResources();
    Map<Resource, Boolean> resourceToReadOnlyMap = editingDomain.getResourceToReadOnlyMap();
    resourceToReadOnlyMap.remove(resources.get(0));
    for (int i = 1, size = resources.size(); i < size; ++i)
    {
      resourceToReadOnlyMap.put(resources.get(i), Boolean.TRUE);
    }

    doSaveGen(monitor);
  }

  /**
   * This returns whether something has been persisted to the URI of the specified resource.
   * The implementation uses the URI converter from the editor's resource set to try to open an input stream.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected boolean isPersisted(Resource resource)
  {
    boolean result = false;
    try
    {
      InputStream stream = editingDomain.getResourceSet().getURIConverter().createInputStream(resource.getURI());
      if (stream != null)
      {
        result = true;
        stream.close();
      }
    }
    catch (IOException e)
    {
      // Ignore
    }
    return result;
  }

  /**
   * This always returns true because it is not currently supported.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isSaveAsAllowed()
  {
    return true;
  }

  /**
   * This also changes the editor's input.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void doSaveAs()
  {
    SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
    saveAsDialog.open();
    IPath path = saveAsDialog.getResult();
    if (path != null)
    {
      IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
      if (file != null)
      {
        doSaveAs(URI.createPlatformResourceURI(file.getFullPath().toString(), true), new FileEditorInput(file));
      }
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void doSaveAs(URI uri, IEditorInput editorInput)
  {
    editingDomain.getResourceSet().getResources().get(0).setURI(uri);
    setInputWithNotify(editorInput);
    setPartName(editorInput.getName());
    IProgressMonitor progressMonitor = getActionBars().getStatusLineManager() != null ? getActionBars().getStatusLineManager().getProgressMonitor()
        : new NullProgressMonitor();
    doSave(progressMonitor);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void gotoMarker(IMarker marker)
  {
    List<?> targetObjects = markerHelper.getTargetObjects(editingDomain, marker);
    if (!targetObjects.isEmpty())
    {
      setSelectionToViewer(targetObjects);
    }
  }

  /**
   * This is called during startup.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void init(IEditorSite site, IEditorInput editorInput)
  {
    setSite(site);
    setInputWithNotify(editorInput);
    setPartName(editorInput.getName());
    site.setSelectionProvider(this);
    site.getPage().addPartListener(partListener);
    ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setFocus()
  {
    getControl(getActivePage()).setFocus();
  }

  /**
   * This implements {@link org.eclipse.jface.viewers.ISelectionProvider}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void addSelectionChangedListener(ISelectionChangedListener listener)
  {
    selectionChangedListeners.add(listener);
  }

  /**
   * This implements {@link org.eclipse.jface.viewers.ISelectionProvider}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void removeSelectionChangedListener(ISelectionChangedListener listener)
  {
    selectionChangedListeners.remove(listener);
  }

  /**
   * This implements {@link org.eclipse.jface.viewers.ISelectionProvider} to return this editor's overall selection.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ISelection getSelection()
  {
    return editorSelection;
  }

  /**
   * This implements {@link org.eclipse.jface.viewers.ISelectionProvider} to set this editor's overall selection.
   * Calling this result will notify the listeners.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSelection(ISelection selection)
  {
    editorSelection = selection;

    for (ISelectionChangedListener listener : selectionChangedListeners)
    {
      listener.selectionChanged(new SelectionChangedEvent(this, selection));
    }
    setStatusLineManager(selection);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setStatusLineManager(ISelection selection)
  {
    IStatusLineManager statusLineManager = currentViewer != null && currentViewer == contentOutlineViewer ? contentOutlineStatusLineManager : getActionBars()
        .getStatusLineManager();

    if (statusLineManager != null)
    {
      if (selection instanceof IStructuredSelection)
      {
        Collection<?> collection = ((IStructuredSelection)selection).toList();
        switch (collection.size())
        {
          case 0:
          {
            statusLineManager.setMessage(getString("_UI_NoObjectSelected"));
            break;
          }
          case 1:
          {
            String text = new AdapterFactoryItemDelegator(adapterFactory).getText(collection.iterator().next());
            statusLineManager.setMessage(getString("_UI_SingleObjectSelected", text));
            break;
          }
          default:
          {
            statusLineManager.setMessage(getString("_UI_MultiObjectSelected", Integer.toString(collection.size())));
            break;
          }
        }
      }
      else
      {
        statusLineManager.setMessage("");
      }
    }
  }

  /**
   * This looks up a string in the plugin's plugin.properties file.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static String getString(String key)
  {
    return SetupEditorPlugin.INSTANCE.getString(key);
  }

  /**
   * This looks up a string in plugin.properties, making a substitution.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static String getString(String key, Object s1)
  {
    return SetupEditorPlugin.INSTANCE.getString(key, new Object[] { s1 });
  }

  /**
   * This implements {@link org.eclipse.jface.action.IMenuListener} to help fill the context menus with contributions from the Edit menu.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void menuAboutToShow(IMenuManager menuManager)
  {
    ((IMenuListener)getEditorSite().getActionBarContributor()).menuAboutToShow(menuManager);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EditingDomainActionBarContributor getActionBarContributor()
  {
    return (EditingDomainActionBarContributor)getEditorSite().getActionBarContributor();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IActionBars getActionBars()
  {
    return getActionBarContributor().getActionBars();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public AdapterFactory getAdapterFactory()
  {
    return adapterFactory;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void dispose()
  {
    updateProblemIndication = false;

    ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceChangeListener);

    getSite().getPage().removePartListener(partListener);

    adapterFactory.dispose();

    if (getActionBarContributor().getActiveEditor() == this)
    {
      getActionBarContributor().setActiveEditor(null);
    }

    for (PropertySheetPage propertySheetPage : propertySheetPages)
    {
      propertySheetPage.dispose();
    }

    if (contentOutlinePage != null)
    {
      contentOutlinePage.dispose();
    }

    super.dispose();
  }

  /**
   * Returns whether the outline view should be presented to the user.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected boolean showOutlineView()
  {
    EObject eObject = getEditingDomain().getResourceSet().getResources().get(0).getContents().get(0);

    return eObject instanceof ProjectCatalog || eObject instanceof Project;
  }

  public static void open(final IWorkbenchPage page, URI uri)
  {
    final URI normalizedURI = EMFUtil.createResourceSet().getURIConverter().normalize(uri);
    final Display display = page.getWorkbenchWindow().getShell().getDisplay();

    display.asyncExec(new Runnable()
    {
      public void run()
      {
        try
        {
          IEditorInput editorInput = new URIEditorInput(normalizedURI, normalizedURI.lastSegment());
          if (normalizedURI.isFile())
          {
            IFile[] files = ResourcesPlugin.getWorkspace().getRoot().findFilesForLocationURI(new java.net.URI(normalizedURI.toString()));
            for (IFile file : files)
            {
              if (file.isAccessible())
              {
                editorInput = new FileEditorInput(files[0]);
                break;
              }
            }
          }

          IEditorPart editor = page.openEditor(editorInput, EDITOR_ID);
          if (editor instanceof SetupEditor && normalizedURI.fragment() != null)
          {
            final SetupEditor setupEditor = (SetupEditor)editor;

            ResourceSet resourceSet = setupEditor.getEditingDomain().getResourceSet();
            final EObject object = resourceSet.getEObject(normalizedURI, true);
            if (object != null)
            {
              setupEditor.selectionViewer.setSelection(new StructuredSelection(object));

              ScrollBar scrollBar = setupEditor.selectionViewer.getTree().getHorizontalBar();
              if (scrollBar != null && !scrollBar.isDisposed() && scrollBar.isVisible())
              {
                scrollBar.setSelection(0);
              }
            }
          }
        }
        catch (Exception ex)
        {
          SetupEditorPlugin.INSTANCE.log(ex);
        }
      }
    });
  }
}
