/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.base.provider.BaseEditUtil.IconReflectiveItemProvider;
import org.eclipse.oomph.base.provider.BaseItemProviderAdapterFactory;
import org.eclipse.oomph.internal.base.BasePlugin;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.ui.FindAndReplaceTarget;
import org.eclipse.oomph.internal.ui.OomphEditingDomain;
import org.eclipse.oomph.internal.ui.OomphPropertySheetPage;
import org.eclipse.oomph.internal.ui.OomphTransferDelegate;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Product;
import org.eclipse.oomph.setup.ProductCatalog;
import org.eclipse.oomph.setup.ProductVersion;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.ProjectCatalog;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.ResourceMirror;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.presentation.SetupActionBarContributor.ToggleViewerInputAction;
import org.eclipse.oomph.setup.provider.PreferenceTaskItemProvider;
import org.eclipse.oomph.setup.provider.SetupItemProviderAdapterFactory;
import org.eclipse.oomph.setup.ui.SetupEditorSupport;
import org.eclipse.oomph.setup.ui.SetupLabelProvider;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.ui.MarkerHelper;
import org.eclipse.emf.common.ui.editor.ProblemEditorPart;
import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CopyCommand.Helper;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProvider;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.emf.edit.ui.provider.UnwrappingSelectionProvider;
import org.eclipse.emf.edit.ui.util.EditUIMarkerHelper;
import org.eclipse.emf.edit.ui.util.EditUIUtil;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
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
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

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

  protected IWindowListener windowListener = new IWindowListener()
  {
    public void windowOpened(IWorkbenchWindow window)
    {
    }

    public void windowDeactivated(IWorkbenchWindow window)
    {
    }

    public void windowClosed(IWorkbenchWindow window)
    {
    }

    public void windowActivated(IWorkbenchWindow window)
    {
      if (getSite().getWorkbenchWindow() == window)
      {
        IWorkbenchPage activePage = window.getActivePage();
        if (activePage != null)
        {
          IWorkbenchPart activePart = activePage.getActivePart();
          if (activePart instanceof ContentOutline)
          {
            if (((ContentOutline)activePart).getCurrentPage() == contentOutlinePage)
            {
              handleActivate();
            }
          }
          else if (activePart instanceof PropertySheet)
          {
            if (propertySheetPages.contains(((PropertySheet)activePart).getCurrentPage()))
            {
              handleActivate();
            }
          }
          else if (activePart == SetupEditor.this)
          {
            handleActivate();
          }
        }
      }
    }
  };

  protected boolean isHandlingActivate;

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

  private ResourceMirror resourceMirror;

  private final ItemProvider loadingResourceInput = new ItemProvider(Collections.singleton(new ItemProvider("Loading resource...")));

  private final ItemProvider loadingResourceSetInput = new ItemProvider(Collections.singleton(new ItemProvider("Loading resource set...")));

  private DelegatingDialogSettings dialogSettings = new DelegatingDialogSettings();

  private IconReflectiveItemProvider reflectiveItemProvider;

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
   * Handles activation of the editor or it's associated views.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void handleActivateGen()
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

  protected void handleActivate()
  {
    if (!isHandlingActivate && resourceMirror == null)
    {
      isHandlingActivate = true;

      try
      {
        ResourceSet resourceSet = editingDomain.getResourceSet();
        URIConverter uriConverter = resourceSet.getURIConverter();
        final Set<IFile> files = new HashSet<IFile>();

        for (Resource resource : resourceSet.getResources())
        {
          if (resource.isLoaded())
          {
            URI uri = resource.getURI();
            URI normalizedURI = uriConverter.normalize(uri);

            if (normalizedURI.isPlatformResource())
            {
              files.add(EcorePlugin.getWorkspaceRoot().getFile(new Path(normalizedURI.toPlatformString(true))));
            }
            else if (normalizedURI.isFile())
            {
              Map<String, ?> attributes = uriConverter.getAttributes(normalizedURI,
                  Collections.singletonMap(URIConverter.OPTION_REQUESTED_ATTRIBUTES, Collections.singleton(URIConverter.ATTRIBUTE_TIME_STAMP)));
              Long timeStamp = (Long)attributes.get(URIConverter.ATTRIBUTE_TIME_STAMP);
              if (timeStamp != null && timeStamp != resource.getTimeStamp())
              {
                changedResources.add(resource);
              }
            }
          }
        }

        if (!files.isEmpty())
        {
          final AtomicBoolean needsRefresh = new AtomicBoolean();

          // Do the work within an operation because this is a long running activity that modifies the workbench.
          //
          IWorkspaceRunnable operation = new IWorkspaceRunnable()
          {
            public void run(IProgressMonitor monitor) throws CoreException
            {
              for (IFile file : files)
              {
                if (!file.isSynchronized(1))
                {
                  needsRefresh.set(true);
                  file.refreshLocal(1, monitor);
                }
              }
            }
          };

          try
          {
            EcorePlugin.getWorkspaceRoot().getWorkspace().run(operation, null);
          }
          catch (Exception exception)
          {
            // Something went wrong that shouldn't.
            //
            SetupEditorPlugin.INSTANCE.log(exception);
          }

          // If the refreshes must take place, the delta visitor will cause this method to be invoked again so exit early now.
          if (needsRefresh.get())
          {
            return;
          }
        }

        if (!removedResources.isEmpty())
        {
          List<Resource> removedResources = new ArrayList<Resource>(this.removedResources);
          if (!removedResources.remove(resourceSet.getResources().get(0)))
          {
            // Only the first resource is modifiable, so if other resources are removed, we can just unload them, an ignore them form further dirty handling.
            for (Resource resource : removedResources)
            {
              this.removedResources.remove(resource);
              resource.unload();
            }
          }
        }

        if (!changedResources.isEmpty())
        {
          // Only the first resource is modifiable, so if other resources are removed, we can just unload them, an ignore them form further dirty handling.
          List<Resource> changedResources = new ArrayList<Resource>(this.changedResources);
          if (!changedResources.remove(resourceSet.getResources().get(0)))
          {
            updateProblemIndication = false;
            for (Resource resource : changedResources)
            {
              this.changedResources.remove(resource);
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

        handleActivateGen();
      }
      finally
      {
        isHandlingActivate = false;
      }
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
  protected void updateProblemIndicationGen()
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

  protected void updateProblemIndication()
  {
    // Don't ever create or use the problem editor part.

    if (!resourceToDiagnosticMap.isEmpty())
    {
      Object input = selectionViewer.getInput();
      if (input instanceof Resource || input == loadingResourceInput)
      {
        ToggleViewerInputAction toggleViewerInputAction = getActionBarContributor().getToggleViewerInputAction();
        toggleViewerInputAction.setActiveWorkbenchPart(this);
        toggleViewerInputAction.run();
        toggleViewerInputAction.setActiveWorkbenchPart(this);
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
        final ResourceItemProviderAdapterFactory resourceItemProviderAdapterFactory = this;
        return new ResourceItemProvider(resourceItemProviderAdapterFactory)
        {
          private List<DelegatingWrapperItemProvider> children;

          @Override
          protected boolean isWrappingNeeded(Object object)
          {
            return true;
          }

          @Override
          protected Command createCopyCommand(EditingDomain domain, EObject owner, Helper helper)
          {
            return UnexecutableCommand.INSTANCE;
          }

          @Override
          public Collection<?> getChildren(Object object)
          {
            Resource resource = (Resource)object;
            List<EObject> contents = new ArrayList<EObject>(resource.getContents());

            ResourceSet resourceSet = resource.getResourceSet();
            if (resourceSet != null && resourceSet.getResources().get(0) != resource)
            {
              if (children == null)
              {
                children = new ArrayList<DelegatingWrapperItemProvider>();
                int index = 0;
                for (Object child : contents)
                {
                  children.add(new DelegatingWrapperItemProvider(child, resource, null, index++, resourceItemProviderAdapterFactory));
                }
              }

              return children;
            }

            return contents;
          }

          @Override
          public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
          {
            if (itemPropertyDescriptors == null)
            {
              super.getPropertyDescriptors(object);
              itemPropertyDescriptors.add(new ItemPropertyDescriptor(resourceItemProviderAdapterFactory, "Resolved URI", "The resolved URI of the resource",
                  (EStructuralFeature)null, false)
              {
                @Override
                public Object getFeature(Object object)
                {
                  return "resolvedURI";
                }

                @Override
                public Object getPropertyValue(Object object)
                {
                  Resource resource = (Resource)object;
                  URI uri = resource.getURI();
                  ResourceSet resourceSet = resource.getResourceSet();
                  if (resourceSet != null)
                  {
                    uri = resourceSet.getURIConverter().normalize(uri);
                  }

                  uri = SetupContext.resolveUser(uri);

                  return uri;
                }
              });

            }
            return itemPropertyDescriptors;
          }

          @Override
          public void dispose()
          {
            super.dispose();

            if (children != null)
            {
              for (IDisposable disposable : children)
              {
                disposable.dispose();
              }
            }
          }
        };
      }
    });

    reflectiveItemProvider = BaseEditUtil.replaceReflectiveItemProvider(adapterFactory);
    PreferenceTaskItemProvider.setShortenLabelText(adapterFactory);

    // Create the editing domain with a special command stack.
    //
    Map<Resource, Boolean> readOnlyMap = new HashMap<Resource, Boolean>()
    {
      private static final long serialVersionUID = 1L;

      @Override
      public Boolean get(Object key)
      {
        return editingDomain.getResourceSet().getResources().indexOf(key) != 0;
      }
    };

    List<? extends OomphTransferDelegate> delegates = OomphTransferDelegate.merge(OomphTransferDelegate.DELEGATES,
        new OomphTransferDelegate.FileTransferDelegate()
        {
          @Override
          protected void gather(EditingDomain domain, URI uri)
          {
            super.gather(domain, SetupContext.resolveUser(uri));
          }
        }, new OomphTransferDelegate.URLTransferDelegate()
        {
          @Override
          protected void gather(EditingDomain domain, URI uri)
          {
            super.gather(domain, SetupContext.resolveUser(uri));
          }
        });
    editingDomain = new OomphEditingDomain(adapterFactory, editingDomain.getCommandStack(), readOnlyMap, delegates);

    ResourceSet resourceSet = editingDomain.getResourceSet();
    SetupCoreUtil.configureResourceSet(resourceSet);

    // If the index's folder is redirected to the local file system...
    URIConverter uriConverter = resourceSet.getURIConverter();
    Map<URI, URI> uriMap = uriConverter.getURIMap();
    Map<URI, URI> workspaceMappings = new HashMap<URI, URI>();
    for (URI uri : uriMap.values())
    {
      if (uri.isFile() && !uri.isRelative())
      {
        try
        {
          java.net.URI locationURI = new java.net.URI(uri.toString());
          if (uri.hasTrailingPathSeparator())
          {
            for (IContainer container : EcorePlugin.getWorkspaceRoot().findContainersForLocationURI(locationURI))
            {
              // If there is, redirect the file system folder to the workspace folder.
              URI redirectedWorkspaceURI = URI.createPlatformResourceURI(container.getFullPath().toString(), true).appendSegment("");
              workspaceMappings.put(uri, redirectedWorkspaceURI);
              break;
            }
          }
          else
          {
            for (IFile file : EcorePlugin.getWorkspaceRoot().findFilesForLocationURI(locationURI))
            {
              // If there is, redirect the file system folder to the workspace folder.
              URI redirectedWorkspaceURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
              workspaceMappings.put(uri, redirectedWorkspaceURI);
              break;
            }
          }
        }
        catch (URISyntaxException ex)
        {
          // Ignore.
        }
      }
    }

    uriMap.putAll(workspaceMappings);

    // Add a listener to set the most recent command's affected objects to be the selection of the viewer with focus.
    //
    editingDomain.getCommandStack().addCommandStackListener(new CommandStackListener()
    {
      public void commandStackChanged(final EventObject event)
      {
        if (contentOutlinePage != null)
        {
          contentOutlinePage.update(2);
        }
      }
    });
  }

  public IconReflectiveItemProvider getReflectiveItemProvider()
  {
    return reflectiveItemProvider;
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
  public void setSelectionToViewerGen(Collection<?> collection)
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

  public void setSelectionToViewer(Collection<?> collection)
  {
    if (selectionViewer != null && !selectionViewer.getTree().isDisposed())
    {
      // If we're trying to select a resource in the selection viewer, make sure resources are visible there.
      if (currentViewer == selectionViewer)
      {
        for (Object object : collection)
        {
          if (object instanceof Resource)
          {
            toggleInput(true);
            break;
          }
        }
      }

      setSelectionToViewerGen(collection);
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
   * @generated NOT
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

    ((OomphEditingDomain)editingDomain).registerDragAndDrop(viewer);
  }

  /**
   * This is the method called to load a resource into the editing domain's resource set based on the editor's input.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  public void createModel()
  {
    URI resourceURI = EditUIUtil.getURI(getEditorInput());
    final ResourceSet resourceSet = editingDomain.getResourceSet();

    resourceMirror.perform(resourceURI);

    final Resource mainResource = resourceSet.getResource(resourceURI, false);
    EList<EObject> contents = mainResource.getContents();
    EObject rootObject = null;
    if (!contents.isEmpty())
    {
      rootObject = contents.get(0);
      if (!(rootObject instanceof Index))
      {
        resourceMirror.perform(SetupContext.INDEX_SETUP_URI);
      }
    }

    try
    {
      EcoreUtil.resolveAll(resourceSet);
    }
    catch (RuntimeException ex)
    {
      // Ignore.
    }

    for (Resource resource : resourceSet.getResources())
    {
      Diagnostic diagnostic = analyzeResourceProblems(resource, null);
      if (diagnostic.getSeverity() != Diagnostic.OK)
      {
        resourceToDiagnosticMap.put(resource, diagnostic);
      }
    }

    editingDomain.getResourceSet().eAdapters().add(problemIndicationAdapter);

    if (!resourceMirror.isCanceled() && rootObject != null)
    {
      Display display = getSite().getShell().getDisplay();
      display.asyncExec(new Runnable()
      {
        public void run()
        {
          EPackage ePackage = mainResource.getContents().get(0).eClass().getEPackage();
          URI ePackageResourceURI = ePackage.eResource().getURI();
          if (ePackageResourceURI.isHierarchical() && ePackageResourceURI.trimSegments(1).equals(LEGACY_MODELS))
          {
            List<EObject> migratedContents = new ArrayList<EObject>();

            try
            {
              SetupCoreUtil.migrate(mainResource, migratedContents);
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
      });
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
    boolean hasErrors = !resource.getErrors().isEmpty();
    if (hasErrors || !resource.getWarnings().isEmpty())
    {
      BasicDiagnostic basicDiagnostic = new BasicDiagnostic(hasErrors ? Diagnostic.ERROR : Diagnostic.WARNING, "org.eclipse.oomph.setup.editor", 0,
          getString("_UI_CreateModelError_message", resource.getURI()), new Object[] { exception == null ? (Object)resource : exception });
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
    // Create a page for the selection tree view.
    //
    Tree tree = new Tree(getContainer(), SWT.MULTI);
    selectionViewer = new TreeViewer(tree);
    setCurrentViewer(selectionViewer);

    selectionViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory)
    {
      @Override
      public Object getParent(Object object)
      {
        // Return the direct resource as the parent so that selection find the object directly in its own resource.
        if (object instanceof InternalEObject)
        {
          Resource resource = ((InternalEObject)object).eDirectResource();
          if (resource != null)
          {
            return resource;
          }
        }
        return super.getParent(object);
      }
    });

    selectionViewer.setLabelProvider(new DecoratingColumLabelProvider(new SetupLabelProvider(adapterFactory, selectionViewer),
        new DiagnosticDecorator(editingDomain, selectionViewer, dialogSettings))
    {
      @Override
      public String getText(Object element)
      {
        String text = super.getText(element);

        if (element instanceof SetupTask)
        {
          SetupTask setupTask = (SetupTask)element;
          EList<Scope> restrictions = setupTask.getRestrictions();
          if (!restrictions.isEmpty())
          {
            StringBuilder builder = new StringBuilder();
            for (Scope restriction : restrictions)
            {
              if (builder.length() != 0)
              {
                builder.append(", ");
              }

              String label = restriction.getLabel();
              if (StringUtil.isEmpty(label))
              {
                label = restriction.getName();
              }

              builder.append(label);
            }

            String string = builder.toString();
            if (text.contains(string))
            {
              string = "";
            }
            else
            {
              string = ": " + string;
            }

            text += "  [restricted" + string + "]";
          }
        }

        return text;
      }
    });

    selectionViewer.setInput(loadingResourceInput);

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
        if (getPageCount() > 0)
        {
          setActivePage(0);
        }
      }
    });

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

    doLoad();
  }

  protected void doLoad()
  {
    final Tree tree = selectionViewer.getTree();
    Job job = new Job("Loading Model")
    {
      @Override
      public boolean belongsTo(Object family)
      {
        return family == SetupEditorSupport.FAMILY_MODEL_LOAD;
      }

      @Override
      protected IStatus run(final IProgressMonitor monitor)
      {
        final ResourceSet resourceSet = editingDomain.getResourceSet();

        final ResourceMirror resourceMirror = new ResourceMirror(resourceSet)
        {
          @Override
          protected void run(String taskName, IProgressMonitor monitor)
          {
            SetupEditor.this.resourceMirror = this;
            createModel();
            dialogSettings.setLiveValidation(true);
            SetupEditor.this.resourceMirror = null;
          }
        };

        resourceMirror.begin(monitor);

        UIUtil.asyncExec(tree, new Runnable()
        {
          public void run()
          {
            try
            {
              tree.setRedraw(false);

              Resource resource = resourceSet.getResources().get(0);
              EList<EObject> contents = resource.getContents();
              EObject rootObject = contents.isEmpty() ? null : contents.get(0);

              selectionViewer.setInput(selectionViewer.getInput() == loadingResourceInput ? resource : resourceSet);

              if (rootObject != null)
              {
                selectionViewer.setSelection(new StructuredSelection(rootObject), true);
              }

              boolean canceled = resourceMirror.isCanceled();
              if (!canceled)
              {
                if (rootObject instanceof Project)
                {
                  EList<Stream> streams = ((Project)rootObject).getStreams();
                  if (streams.isEmpty())
                  {
                    selectionViewer.expandToLevel(rootObject, 1);
                  }
                  else
                  {
                    for (Stream branch : streams)
                    {
                      selectionViewer.expandToLevel(branch, 1);
                    }
                  }
                }
                else if (rootObject != null)
                {
                  selectionViewer.expandToLevel(rootObject, 1);
                }

                if (contentOutlinePage != null)
                {
                  contentOutlinePage.update(2);
                }

                getActionBarContributor().scheduleValidation();
              }

              updateProblemIndication();
            }
            finally
            {
              if (!tree.isDisposed())
              {
                tree.setRedraw(true);
              }
            }
          }
        });

        return Status.OK_STATUS;
      }
    };

    job.schedule();

    IProgressService progressService = getSite().getWorkbenchWindow().getWorkbench().getProgressService();
    progressService.showInDialog(null, job);
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
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Object getAdapterGen(Class key)
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

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public Object getAdapter(Class adapter)
  {
    Object result = FindAndReplaceTarget.getAdapter(adapter, this);
    if (result != null)
    {
      return result;
    }

    return getAdapterGen(adapter);
  }

  /**
   * @author Ed Merks
   */
  class OutlinePreviewPage extends ContentOutlinePage implements IAdaptable
  {
    private ILabelProvider labelProvider;

    private Trigger trigger;

    private Map<Object, Set<Object>> copyMap = new HashMap<Object, Set<Object>>();

    private Map<Object, Set<Object>> inverseCopyMap = new HashMap<Object, Set<Object>>();

    private List<Notifier> notifiers = new ArrayList<Notifier>();

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

            variableUsages.addAll(contextVariableTask.getChoices());
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
      labelProvider = new SetupLabelProvider(adapterFactory, contentOutlineViewer)
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
            Set<Object> copies = contentOutlinePage.getCopies(object);
            if (copies != null)
            {
              selection.addAll(copies);
            }
          }
        }
      });

      contentOutlineViewer.expandToLevel(2);
    }

    public Set<Object> getOriginals(Object object)
    {
      Set<Object> result = inverseCopyMap.get(object);
      return result == null ? Collections.singleton(object) : result;
    }

    public Set<Object> getCopies(Object object)
    {
      return copyMap.get(object);
    }

    public void update(int expandLevel)
    {
      if (labelProvider != null)
      {
        copyMap.clear();
        inverseCopyMap.clear();

        for (Notifier notifier : notifiers)
        {
          notifier.eAdapters().clear();
        }

        notifiers.clear();

        try
        {
          ResourceSet resourceSet = editingDomain.getResourceSet();
          EList<Resource> resources = resourceSet.getResources();
          if (!resources.isEmpty())
          {
            Project project = (Project)EcoreUtil.getObjectByType(resources.get(0).getContents(), SetupPackage.Literals.PROJECT);
            if (project != null)
            {
              ItemProvider input = getTriggeredTasks(project);
              getTreeViewer().setInput(input);
            }
          }
        }
        catch (Exception ex)
        {
          SetupEditorPlugin.INSTANCE.log(ex);
        }

        for (Map.Entry<Object, Set<Object>> entry : copyMap.entrySet())
        {
          Set<Object> values = entry.getValue();
          for (Object value : values)
          {
            Set<Object> eObjects = inverseCopyMap.get(value);
            if (eObjects == null)
            {
              eObjects = new HashSet<Object>();
              inverseCopyMap.put(value, eObjects);
            }

            eObjects.add(entry.getKey());
          }
        }

        getTreeViewer().expandToLevel(expandLevel);
      }
    }

    private List<String> sortStrings(Collection<? extends String> strings)
    {
      EList<Pair<SegmentSequence, String>> pairs = new BasicEList<Pair<SegmentSequence, String>>();
      for (String string : strings)
      {
        pairs.add(new Pair<SegmentSequence, String>(SegmentSequence.create(".", string), string));
      }

      @SuppressWarnings("unchecked")
      Pair<SegmentSequence, String>[] array = pairs.toArray(new Pair[pairs.size()]);
      return sort(array);
    }

    private List<VariableTask> sortVariables(Collection<? extends VariableTask> variables)
    {
      EList<Pair<SegmentSequence, VariableTask>> pairs = new BasicEList<Pair<SegmentSequence, VariableTask>>();
      for (VariableTask variable : variables)
      {
        pairs.add(new Pair<SegmentSequence, VariableTask>(SegmentSequence.create(".", variable.getName()), variable));
      }

      @SuppressWarnings("unchecked")
      Pair<SegmentSequence, VariableTask>[] array = pairs.toArray(new Pair[pairs.size()]);
      return sort(array);
    }

    private <T> List<T> sort(Pair<SegmentSequence, T>[] pairs)
    {
      Arrays.sort(pairs, new Comparator<Pair<SegmentSequence, T>>()
      {
        private final Comparator<String> comparator = CommonPlugin.INSTANCE.getComparator();

        public int compare(Pair<SegmentSequence, T> o1, Pair<SegmentSequence, T> o2)
        {
          SegmentSequence s1 = o1.getElement1();
          SegmentSequence s2 = o2.getElement1();
          if (s1 == null)
          {
            if (s2 == null)
            {
              return 0;
            }

            return -1;
          }

          if (s2 == null)
          {
            return 1;
          }

          int length1 = s1.segmentCount();
          int length2 = s2.segmentCount();

          int length = Math.min(length1, length2);

          for (int i = 0; i < length; ++i)
          {
            String e1 = StringUtil.safe(s1.segment(i));
            String e2 = StringUtil.safe(s2.segment(i));

            int result = comparator.compare(e1, e2);
            if (result != 0)
            {
              return result;
            }
          }

          return length1 - length2;
        }
      });

      List<T> result = new ArrayList<T>(pairs.length);
      for (Pair<SegmentSequence, T> pair : pairs)
      {
        result.add(pair.getElement2());
      }

      return result;
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

        ProductVersion version = null;
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
                  version = productVersion;
                  break LOOP;
                }
              }
            }
          }
        }

        if (version != null)
        {
          // Clear out the self induced installation location.
          for (SetupTask setupTask : version.getProduct().getSetupTasks())
          {
            if (setupTask instanceof VariableTask)
            {
              VariableTask variable = (VariableTask)setupTask;
              if ("installation.location".equals(variable.getName()))
              {
                EcoreUtil.delete(variable);
                break;
              }
            }
          }

          version.getSetupTasks().clear();

          SetupContext setupContext = SetupContext.create(version, stream);
          URIConverter uriConverter = getEditingDomain().getResourceSet().getURIConverter();

          SetupTaskPerformer setupTaskPerformer = new SetupTaskPerformer(uriConverter, SetupPrompter.CANCEL, trigger, setupContext, stream);
          List<SetupTask> triggeredSetupTasks = new ArrayList<SetupTask>(setupTaskPerformer.getTriggeredSetupTasks());
          setupTaskPerformer.redirectTriggeredSetupTasks();

          if (!triggeredSetupTasks.isEmpty())
          {
            for (EObject eObject : setupTaskPerformer.getCopyMap().values())
            {
              notifiers.add(eObject);

              Resource resource = ((InternalEObject)eObject).eDirectResource();
              if (resource != null && !resource.eAdapters().contains(editingDomainProvider))
              {
                resource.eAdapters().add(editingDomainProvider);
                notifiers.add(resource);
              }
            }

            ItemProvider undeclaredVariablesItem = new VariableContainer(setupTaskPerformer, "Undeclared Variables", UNDECLARED_VARIABLE_GROUP_IMAGE);
            EList<Object> undeclaredVariablesItemChildren = undeclaredVariablesItem.getChildren();
            Set<String> undeclaredVariables = setupTaskPerformer.getUndeclaredVariables();
            for (String key : sortStrings(undeclaredVariables))
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
            for (VariableTask contextVariableTask : sortVariables(unresolvedVariables))
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
            for (VariableTask contextVariableTask : sortVariables(resolvedVariables))
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

            for (Map.Entry<EObject, EObject> entry : setupTaskPerformer.getCopyMap().entrySet())
            {
              add(copyMap, entry.getKey(), entry.getValue());
            }

            add(copyMap, stream, branchItem);
          }
        }

        add(copyMap, project, projectItem);
      }

      for (Project subproject : project.getProjects())
      {
        projectItemChildren.add(getTriggeredTasks(subproject));
      }

      return projectItem;
    }

    private <K, V> void add(Map<K, Set<V>> map, K key, V value)
    {
      Set<V> set = map.get(key);
      if (set == null)
      {
        set = new HashSet<V>();
        map.put(key, set);
      }
      set.add(value);
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
          update(2);
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
            update(2);
          }
        });
      }

      getActionBarContributor().shareGlobalActions(this, actionBars);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAdapter(Class<T> adapter)
    {
      return (T)FindAndReplaceTarget.getAdapter(adapter, SetupEditor.this);
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
    PropertySheetPage propertySheetPage = new OomphPropertySheetPage(editingDomain, ExtendedPropertySheetPage.Decoration.LIVE, dialogSettings)
    {
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

  protected void doRevert()
  {
    Object input = selectionViewer.getInput();
    selectionViewer.setInput(input instanceof Resource ? loadingResourceInput : loadingResourceSetInput);

    dialogSettings.setLiveValidation(false);

    EList<Resource> resources = editingDomain.getResourceSet().getResources();
    for (Resource resource : resources)
    {
      resource.unload();
    }

    resources.clear();
    resourceToDiagnosticMap.clear();
    editingDomain.getCommandStack().flush();

    doLoad();
  }

  /**
   * This is for implementing {@link IEditorPart} and simply saves the model file.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
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
        EList<Resource> resources = editingDomain.getResourceSet().getResources();
        for (int i = 0; i < resources.size(); ++i)
        {
          Resource resource = resources.get(i);
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
  public void doSave(IProgressMonitor progressMonitor)
  {
    EList<Resource> resources = editingDomain.getResourceSet().getResources();
    for (int i = 1; i < resources.size();)
    {
      Resource resource = resources.get(i);
      if (resource.getContents().isEmpty() && !resource.getErrors().isEmpty())
      {
        resourceToDiagnosticMap.remove(resource);
        resources.remove(i);
        resource.unload();
      }
      else
      {
        ++i;
      }
    }

    doSaveGen(progressMonitor);

    getActionBarContributor().scheduleValidation();
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

  public void toggleInput()
  {
    toggleInput(false);
  }

  private void toggleInput(boolean forceResourceSet)
  {
    ISelection selection = selectionViewer.getSelection();
    Object[] expandedElements = selectionViewer.getExpandedElements();
    Object input = selectionViewer.getInput();
    if (input instanceof ResourceSet)
    {
      if (forceResourceSet)
      {
        return;
      }

      Resource resource = editingDomain.getResourceSet().getResources().get(0);
      selectionViewer.setInput(resource);
    }
    else if (input instanceof Resource)
    {
      selectionViewer.setInput(editingDomain.getResourceSet());
    }
    else if (input == loadingResourceInput)
    {
      if (forceResourceSet)
      {
        return;
      }

      selectionViewer.setInput(loadingResourceSetInput);
    }
    else if (input == loadingResourceSetInput)
    {
      selectionViewer.setInput(loadingResourceInput);
    }

    selectionViewer.setExpandedElements(expandedElements);
    selectionViewer.setSelection(selection);
  }

  /**
   * This is called during startup.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initGen(IEditorSite site, IEditorInput editorInput)
  {
    setSite(site);
    setInputWithNotify(editorInput);
    setPartName(editorInput.getName());
    site.setSelectionProvider(this);
    site.getPage().addPartListener(partListener);
    ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceChangeListener, IResourceChangeEvent.POST_CHANGE);
  }

  @Override
  public void init(IEditorSite site, IEditorInput editorInput)
  {
    final IResourceChangeListener delegateResourceChangeListener = resourceChangeListener;
    resourceChangeListener = new IResourceChangeListener()
    {
      public void resourceChanged(IResourceChangeEvent event)
      {
        // We don't want to process deltas while we're mirroring the resource set.
        // In particular, the loading of platform:/resource URIs can cause workspace refresh and that will cause this method to be called.
        // But that's pointless because we won't have any resource in the resource set that needs updating as a result.
        if (resourceMirror == null)
        {
          delegateResourceChangeListener.resourceChanged(event);
        }
      }
    };

    site.getWorkbenchWindow().getWorkbench().addWindowListener(windowListener);

    initGen(site, editorInput);
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
    IStatusLineManager statusLineManager = currentViewer != null && currentViewer == contentOutlineViewer ? contentOutlineStatusLineManager
        : getActionBars().getStatusLineManager();

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
   * @generated NOT
   */
  public SetupActionBarContributor getActionBarContributor()
  {
    return (SetupActionBarContributor)getEditorSite().getActionBarContributor();
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
  public void disposeGen()
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

  @Override
  public void dispose()
  {
    getSite().getWorkbenchWindow().getWorkbench().removeWindowListener(windowListener);
    disposeGen();
  }

  /**
   * Returns whether the outline view should be presented to the user.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected boolean showOutlineView()
  {
    return true;
  }

  public static class DelegatingDialogSettings implements IDialogSettings
  {
    private final IDialogSettings dialogSettings = SetupEditorPlugin.INSTANCE.getDialogSettings();

    private boolean liveValidation;

    public void setLiveValidation(boolean liveValidation)
    {
      this.liveValidation = liveValidation;
    }

    public IDialogSettings addNewSection(String name)
    {
      return dialogSettings.addNewSection(name);
    }

    public void addSection(IDialogSettings section)
    {
      dialogSettings.addSection(section);
    }

    public String get(String key)
    {
      return dialogSettings.get(key);
    }

    public String[] getArray(String key)
    {
      return dialogSettings.getArray(key);
    }

    public boolean getBoolean(String key)
    {
      if (DiagnosticDecorator.LiveValidator.LiveValidationAction.LIVE_VALIDATOR_DIALOG_SETTINGS_KEY.equals(key) && !liveValidation)
      {
        return false;
      }

      return dialogSettings.getBoolean(key);
    }

    public double getDouble(String key) throws NumberFormatException
    {
      return dialogSettings.getDouble(key);
    }

    public float getFloat(String key) throws NumberFormatException
    {
      return dialogSettings.getFloat(key);
    }

    public int getInt(String key) throws NumberFormatException
    {
      return dialogSettings.getInt(key);
    }

    public long getLong(String key) throws NumberFormatException
    {
      return dialogSettings.getLong(key);
    }

    public String getName()
    {
      return dialogSettings.getName();
    }

    public IDialogSettings getSection(String sectionName)
    {
      return dialogSettings.getSection(sectionName);
    }

    public IDialogSettings[] getSections()
    {
      return dialogSettings.getSections();
    }

    public void load(Reader reader) throws IOException
    {
      dialogSettings.load(reader);
    }

    public void load(String fileName) throws IOException
    {
      dialogSettings.load(fileName);
    }

    public void put(String key, String[] value)
    {
      dialogSettings.put(key, value);
    }

    public void put(String key, double value)
    {
      dialogSettings.put(key, value);
    }

    public void put(String key, float value)
    {
      dialogSettings.put(key, value);
    }

    public void put(String key, int value)
    {
      dialogSettings.put(key, value);
    }

    public void put(String key, long value)
    {
      dialogSettings.put(key, value);
    }

    public void put(String key, String value)
    {
      dialogSettings.put(key, value);
    }

    public void put(String key, boolean value)
    {
      dialogSettings.put(key, value);
    }

    public void save(Writer writer) throws IOException
    {
      dialogSettings.save(writer);
    }

    public void save(String fileName) throws IOException
    {
      dialogSettings.save(fileName);
    }
  }
}
