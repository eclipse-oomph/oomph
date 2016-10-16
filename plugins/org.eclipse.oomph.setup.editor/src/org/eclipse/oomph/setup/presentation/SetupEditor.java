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

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.base.provider.BaseEditUtil.IconReflectiveItemProvider;
import org.eclipse.oomph.base.provider.BaseItemProviderAdapterFactory;
import org.eclipse.oomph.base.util.BaseResourceImpl;
import org.eclipse.oomph.internal.base.BasePlugin;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.ui.FindAndReplaceTarget;
import org.eclipse.oomph.internal.ui.IRevertablePart;
import org.eclipse.oomph.internal.ui.OomphEditingDomain;
import org.eclipse.oomph.internal.ui.OomphPropertySheetPage;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Configuration;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
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
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.AuthorizationHandler.Authorization;
import org.eclipse.oomph.setup.internal.core.util.ResourceMirror;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.presentation.SetupActionBarContributor.ToggleViewerInputAction;
import org.eclipse.oomph.setup.provider.PreferenceTaskItemProvider;
import org.eclipse.oomph.setup.provider.SetupItemProviderAdapterFactory;
import org.eclipse.oomph.setup.ui.SetupEditorSupport;
import org.eclipse.oomph.setup.ui.SetupLabelProvider;
import org.eclipse.oomph.setup.ui.SetupTransferSupport;
import org.eclipse.oomph.setup.ui.ToolTipLabelProvider;
import org.eclipse.oomph.ui.DockableDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;
import org.eclipse.oomph.workingsets.presentation.WorkingSetsActionBarContributor;
import org.eclipse.oomph.workingsets.presentation.WorkingSetsActionBarContributor.PreviewDialog.WorkingSetsProvider;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.command.AbstractCommand;
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
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.ui.ImageURIRegistry;
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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edit.EMFEditPlugin;
import org.eclipse.emf.edit.command.AbstractOverrideableCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CopyCommand;
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
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptorDecorator;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProvider;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceSetItemProvider;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.emf.edit.ui.provider.UnwrappingSelectionProvider;
import org.eclipse.emf.edit.ui.util.EditUIMarkerHelper;
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
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.AbstractHoverInformationControlManager;
import org.eclipse.jface.text.AbstractInformationControlManager.IInformationControlCloser;
import org.eclipse.jface.text.AbstractReusableInformationControlCreator;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.IToolTipProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.AuthenticationEvent;
import org.eclipse.swt.browser.AuthenticationListener;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPage;
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
import java.net.HttpCookie;
import java.net.URISyntaxException;
import java.net.URL;
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
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is an example of a Setup model editor.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated not
 */
public class SetupEditor extends MultiPageEditorPart implements IEditingDomainProvider, ISelectionProvider, IMenuListener, IViewerProvider, IGotoMarker,
    IRevertablePart, WorkingSetsActionBarContributor.PreviewDialog.Previewable
{
  private static final URI LEGACY_MODELS = URI.createURI("platform:/plugin/" + BasePlugin.INSTANCE.getSymbolicName() + "/model/legacy");

  private static final URI LEGACY_EXAMPLE_URI = URI.createURI("file:/example.setup");

  private static final Object VARIABLE_GROUP_IMAGE = SetupEditorPlugin.INSTANCE.getImage("full/obj16/VariableGroup");

  private static final Pattern HEADER_PATTERN = Pattern.compile("(<h1)(>)");

  private static final Pattern IMAGE_PATTERN = Pattern.compile("(<img )(src=)");

  private static final Object UNDECLARED_VARIABLE_GROUP_IMAGE;

  static
  {
    List<Object> images = new ArrayList<Object>(2);
    images.add(VARIABLE_GROUP_IMAGE);
    images.add(EMFEditUIPlugin.INSTANCE.getImage("full/ovr16/error_ovr.gif"));
    ComposedImage composedImage = new ErrorOverlayImage(images);
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
    @SuppressWarnings("restriction")
    public void partActivated(IWorkbenchPart p)
    {
      if (p instanceof ContentOutline)
      {
        if (((ContentOutline)p).getCurrentPage() == contentOutlinePage)
        {
          getActionBarContributor().setActiveEditor(SetupEditor.this);
          ((org.eclipse.ui.internal.EditorSite)getEditorSite()).activateActionBars(true);

          setCurrentViewer(contentOutlineViewer);
        }
      }
      else if (p instanceof PropertySheet)
      {
        if (propertySheetPages.contains(((PropertySheet)p).getCurrentPage()))
        {
          getActionBarContributor().setActiveEditor(SetupEditor.this);
          ((org.eclipse.ui.internal.EditorSite)getEditorSite()).activateActionBars(true);
          handleActivate();
        }
      }
      else if (p == SetupEditor.this)
      {
        handleActivate();
        setCurrentViewer(selectionViewer);
      }
      else if (!(p instanceof SetupEditor))
      {
        ((org.eclipse.ui.internal.EditorSite)getEditorSite()).deactivateActionBars(true);
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
   * @generated NOT
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
                // If this is a *.ecore resource, it will be demand loaded regardless of demandLoad == false.
                EList<Resource> resources = resourceSet.getResources();
                List<Resource> originalResources = new ArrayList<Resource>(resources);
                Resource resource = null;
                try
                {
                  resource = resourceSet.getResource(URI.createPlatformResourceURI(delta.getFullPath().toString(), true), false);
                }
                catch (RuntimeException ex)
                {
                  // Ignore.
                }

                // If something was demand loaded, remove it, or if it's not in the resource set because it loaded with failures,
                // and proceed as if it weren't demand loaded.
                if (resources.retainAll(originalResources) || !resources.contains(resource))
                {
                  resource = null;
                }

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
      catch (

      CoreException exception)
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

  private Runnable reproxifier;

  protected SetupActionBarContributor.SetupWorkingSetsProvider workingSetsProvider = new SetupActionBarContributor.SetupWorkingSetsProvider();

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

  public WorkingSetsProvider getWorkingSetsProvider()
  {
    return workingSetsProvider;
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
    if (!getContainer().isDisposed() && !isHandlingActivate && resourceMirror == null)
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
            normalizedURI = SetupContext.resolve(normalizedURI);

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
          catch (OperationCanceledException exception)
          {
            //$FALL-THROUGH$
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
          protected Command createCopyCommand(EditingDomain domain, EObject owner, CopyCommand.Helper helper)
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

                  uri = SetupContext.resolve(uri);

                  if (uri.isPlatform())
                  {
                    try
                    {
                      URL resolveURL = FileLocator.resolve(new URL(uri.toString()));
                      uri = URI.createURI(resolveURL.toString());
                    }
                    catch (IOException ex)
                    {
                    }
                  }

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

      @Override
      public Adapter createResourceSetAdapter()
      {
        return new ResourceSetItemProvider(this)
        {
          @Override
          protected Command createDragAndDropCommand(EditingDomain domain, Object owner, float location, int operations, int operation,
              final Collection<?> collection)
          {
            final ResourceSet resourceSet = (ResourceSet)owner;
            class LoadResourceCommand extends AbstractOverrideableCommand implements AbstractCommand.NonDirtying
            {
              protected LoadResourceCommand(EditingDomain domain)
              {
                super(domain);
              }

              protected List<Resource> resources;

              @Override
              protected boolean prepare()
              {
                for (Object object : collection)
                {
                  if (!(object instanceof URI))
                  {
                    return false;
                  }
                }
                return true;
              }

              @Override
              public void doExecute()
              {
                resources = new ArrayList<Resource>();
                for (Object object : collection)
                {
                  URI uri = (URI)object;
                  Resource resource = resourceSet.getResource(uri, false);
                  if (resource == null)
                  {
                    try
                    {
                      resource = resourceSet.getResource(uri, true);
                    }
                    catch (RuntimeException exception)
                    {
                      resource = resourceSet.getResource(uri, false);
                      EMFEditPlugin.INSTANCE.log(exception);
                    }
                  }

                  if (resource != null)
                  {
                    EList<Resource> resourceSetResources = resourceSet.getResources();
                    if (resourceSetResources.indexOf(resource) != 0)
                    {
                      resourceSetResources.move(1, resource);
                    }

                    resources.add(resource);
                  }
                }
              }

              @Override
              public void doUndo()
              {
                resourceSet.getResources().removeAll(resources);
                resources = null;
              }

              @Override
              public void doRedo()
              {
                doExecute();
              }

              @Override
              public Collection<?> doGetAffectedObjects()
              {
                return resources == null ? Collections.singleton(resourceSet) : resources;
              }

              @Override
              public String doGetDescription()
              {
                return EMFEditPlugin.INSTANCE.getString("_UI_LoadResources_description");
              }

              @Override
              public String doGetLabel()
              {
                return EMFEditPlugin.INSTANCE.getString("_UI_LoadResources_label");
              }
            }

            return new LoadResourceCommand(domain);
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
        Boolean result = super.get(key);
        if (result == null)
        {
          EList<Resource> resources = editingDomain.getResourceSet().getResources();
          if (resources.indexOf(key) != 0)
          {
            return Boolean.TRUE;
          }
        }

        return result;
      }
    };

    editingDomain = new OomphEditingDomain(adapterFactory, editingDomain.getCommandStack(), readOnlyMap, SetupTransferSupport.USER_RESOLVING_DELEGATES);

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
   * @author Ed Merks
   */
  private static class ErrorOverlayImage extends ComposedImage
  {
    private ErrorOverlayImage(Collection<?> images)
    {
      super(images);
    }

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
    // Refuse to add the popup extender because we don't want all those things polluting the menu.
    MenuManager contextMenu = new MenuManager("#PopUp")
    {
      @Override
      @SuppressWarnings("restriction")
      protected boolean allowItem(IContributionItem itemToAdd)
      {
        String id = itemToAdd.getId();
        if (itemToAdd instanceof org.eclipse.ui.internal.PluginActionContributionItem)
        {
          // Hide these annoying actions.
          return id != null && !id.contains("debug.ui") && !"ValidationAction".equals(id) && !id.contains("mylyn");
        }

        if (itemToAdd instanceof MenuManager)
        {
          // Hide all sub menus except our own.
          if (id != null)
          {
            if ("team.main".equals(id) || "replaceWithMenu".equals(id) || "compareWithMenu".equals(id))
            {
              itemToAdd.setVisible(false);
            }
          }
        }

        return true;
      }
    };

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
    final ResourceSet resourceSet = editingDomain.getResourceSet();

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
              if (container.isAccessible())
              {
                // If there is, redirect the file system folder to the workspace folder.
                URI redirectedWorkspaceURI = URI.createPlatformResourceURI(container.getFullPath().toString(), true).appendSegment("");
                workspaceMappings.put(uri, redirectedWorkspaceURI);
                break;
              }
            }
          }
          else
          {
            for (IFile file : EcorePlugin.getWorkspaceRoot().findFilesForLocationURI(locationURI))
            {
              if (file.isAccessible())
              {
                // If there is, redirect the file system folder to the workspace folder.
                URI redirectedWorkspaceURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
                workspaceMappings.put(uri, redirectedWorkspaceURI);
                break;
              }
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

    URI resourceURI = SetupEditorSupport.getURI(getEditorInput(), resourceSet.getURIConverter());
    resourceMirror.perform(resourceURI);

    final Resource mainResource = resourceSet.getResource(resourceURI, false);
    EList<EObject> contents = mainResource.getContents();
    EObject rootObject = null;
    if (!contents.isEmpty())
    {
      rootObject = contents.get(0);
      if (rootObject instanceof Index)
      {
        final Index index = (Index)rootObject;

        // Record the proxies in the discoverable packages list so they can be restored before saving.
        // Our special model handling has the effect that resolving a proxy reference to a model resource
        // will resolve to the generated package when its implementation is available in the installation.
        // But we don't want to serialize a reference to that, we want to preserve the original form in which the model was referenced.
        reproxifier = new Runnable()
        {
          final InternalEList<EPackage> discoverablePackages = (InternalEList<EPackage>)index.getDiscoverablePackages();

          final Map<EPackage, EPackage> packageProxies = new LinkedHashMap<EPackage, EPackage>();

          {
            // Record a map from resolved EPackage to the original EPackage proxy.
            for (int i = 0, size = discoverablePackages.size(); i < size; ++i)
            {
              EPackage ePackageProxy = discoverablePackages.basicGet(i);
              EPackage ePackage = discoverablePackages.get(i);
              packageProxies.put(ePackage, ePackageProxy);
            }
          }

          public void run()
          {
            // Before saving, we can run this to restore the original proxies.
            for (int i = 0, size = discoverablePackages.size(); i < size; ++i)
            {
              EPackage ePackage = discoverablePackages.basicGet(i);
              EPackage ePackageProxy = packageProxies.get(ePackage);
              if (ePackageProxy != null)
              {
                discoverablePackages.set(i, ePackageProxy);
              }
            }
          }
        };
      }
      else
      {
        if (rootObject instanceof Configuration)
        {
          final Configuration configuration = (Configuration)rootObject;
          final Installation installation = configuration.getInstallation();
          final Workspace workspace = configuration.getWorkspace();

          // Record the proxies in configuration's workspace and installation.
          // Often these are added to the index, e.g., to the user product index or the user project catalog extension projects,
          // and then the reference will be changed to be via the index:/
          // But we don't want to serialize a reference to that, we want to preserve the original form in which the model was referenced,
          // so that this configuration.
          reproxifier = new Runnable()
          {
            final Map<Object, Object> proxies = new LinkedHashMap<Object, Object>();

            private final Adapter proxyListener = new AdapterImpl()
            {
              @Override
              public void notifyChanged(Notification notification)
              {
                if (notification.getEventType() == Notification.RESOLVE)
                {
                  Object feature = notification.getFeature();
                  if (feature == SetupPackage.Literals.INSTALLATION__PRODUCT_VERSION || feature == SetupPackage.Literals.WORKSPACE__STREAMS)
                  {
                    proxies.put(notification.getNewValue(), notification.getOldValue());
                  }
                }
              }
            };

            {
              if (installation != null)
              {
                installation.eAdapters().add(proxyListener);
              }

              if (workspace != null)
              {
                workspace.eAdapters().add(proxyListener);

              }
            }

            public void run()
            {
              if (installation != null)
              {
                EObject productVersion = (EObject)installation.eGet(SetupPackage.Literals.INSTALLATION__PRODUCT_VERSION, false);
                if (productVersion != null)
                {
                  ProductVersion productVersionProxy = (ProductVersion)proxies.get(productVersion);
                  if (productVersionProxy != null)
                  {
                    installation.setProductVersion(productVersionProxy);
                  }
                }
              }

              if (workspace != null)
              {
                InternalEList<Stream> streams = (InternalEList<Stream>)workspace.getStreams();
                for (int i = 0, size = streams.size(); i < size; ++i)
                {
                  Stream stream = streams.basicGet(i);
                  Stream streamProxy = (Stream)proxies.get(stream);
                  if (streamProxy != null)
                  {
                    streams.set(i, streamProxy);
                  }
                }
              }
            }
          };
        }

        resourceMirror.perform(SetupContext.INDEX_SETUP_URI);
      }
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
          Resource packageResource = ePackage.eResource();
          if (packageResource != null)
          {
            URI ePackageResourceURI = packageResource.getURI();
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

  private void configure(ColumnViewer viewer, final boolean foreignResourceDecoration)
  {
    final SetupLocationListener locationListener = new SetupLocationListener(true);
    locationListener.setEditor(this);

    final ColumnViewerInformationControlToolTipSupport toolTipSupport = new ColumnViewerInformationControlToolTipSupport(viewer, locationListener);

    final AbstractHoverInformationControlManager hoverInformationControlManager = ReflectUtil.getValue("hoverInformationControlManager", toolTipSupport);

    @SuppressWarnings("restriction")
    final org.eclipse.jface.internal.text.InformationControlReplacer informationControlReplacer = hoverInformationControlManager.getInternalAccessor()
        .getInformationControlReplacer();

    final IInformationControlCloser informationControlReplacerCloser = ReflectUtil.getValue("fInformationControlCloser", informationControlReplacer);

    class Closer implements IInformationControlCloser, Listener
    {
      private IInformationControlCloser informationControlCloser;

      private Shell shell;

      public Closer(IInformationControlCloser informationControlCloser)
      {
        this.informationControlCloser = informationControlCloser;
      }

      public void setSubjectControl(Control subject)
      {
        informationControlCloser.setSubjectControl(subject);
      }

      public void setInformationControl(IInformationControl control)
      {
        if (shell != null)
        {
          shell.getDisplay().removeFilter(SWT.MouseEnter, this);
          shell.getDisplay().removeFilter(SWT.MouseMove, this);
          shell.getDisplay().removeFilter(SWT.MouseExit, this);
        }

        shell = control == null ? null : (Shell)ReflectUtil.getValue("fShell", control);
        informationControlCloser.setInformationControl(control);
      }

      public void start(Rectangle subjectArea)
      {
        informationControlCloser.start(subjectArea);
        shell.getDisplay().addFilter(SWT.MouseEnter, this);
        shell.getDisplay().addFilter(SWT.MouseMove, this);
        shell.getDisplay().addFilter(SWT.MouseExit, this);
      }

      public void stop()
      {
        informationControlCloser.stop();
        if (shell != null)
        {
          shell.getDisplay().removeFilter(SWT.MouseEnter, this);
          shell.getDisplay().removeFilter(SWT.MouseMove, this);
          shell.getDisplay().removeFilter(SWT.MouseExit, this);
        }
      }

      public void handleEvent(Event event)
      {
        if (event.widget instanceof Control)
        {
          if (shell.getDisplay().getActiveShell() != shell)
          {
            Control control = (Control)event.widget;
            Point location = control.toDisplay(event.x, event.y);
            Rectangle bounds = shell.getBounds();
            Rectangle trim = shell.computeTrim(bounds.x, bounds.y, bounds.width, bounds.height);
            if (!trim.contains(location))
            {
              ReflectUtil.invokeMethod("hideInformationControl", informationControlReplacer);
            }
          }
        }
      }
    }

    ReflectUtil.setValue("fInformationControlCloser", informationControlReplacer, new Closer(informationControlReplacerCloser));

    locationListener.setToolTipSupport(toolTipSupport);

    final SetupLabelProvider setupLabelProvider = new SetupLabelProvider(adapterFactory, viewer);

    DiagnosticDecorator diagnosticDecorator = new DiagnosticDecorator(editingDomain, viewer, dialogSettings)
    {
      final AdapterFactoryItemDelegator itemDelegator = new AdapterFactoryItemDelegator(adapterFactory)
      {
        @Override
        public String getText(Object object)
        {
          if (object instanceof EClass)
          {
            return ((EClass)object).getName();
          }

          return super.getText(object);
        }
      };

      @Override
      protected BasicDiagnostic decorate(Map<Object, BasicDiagnostic> decorations, Object object, Diagnostic diagnostic, List<Integer> path)
      {
        if (diagnostic != null)
        {
          BasicDiagnostic oldDiagnostic = decorations.get(object);
          if (oldDiagnostic != null)
          {
            for (Diagnostic oldChildDiagnostic : oldDiagnostic.getChildren())
            {
              if (equals(diagnostic, oldChildDiagnostic))
              {
                // Avoid adding structural equal duplicates.
                // Because objects are reachable multiple times via cross resource containment,
                // we generally end up with duplicates.
                return oldDiagnostic;
              }
            }
          }
        }

        return super.decorate(decorations, object, diagnostic, path);
      }

      protected boolean equals(Diagnostic diagnostic1, Diagnostic diagnostic2)
      {
        if (diagnostic1.getCode() != diagnostic2.getCode())
        {
          return false;
        }

        if (diagnostic1.getSource() != diagnostic2.getSource())
        {
          return false;
        }

        if (diagnostic1.getSeverity() != diagnostic2.getSeverity())
        {
          return false;
        }

        if (!ObjectUtil.equals(diagnostic1.getMessage(), diagnostic2.getMessage()))
        {
          return false;
        }

        if (!diagnostic1.getData().equals(diagnostic2.getData()))
        {
          return false;
        }

        List<Diagnostic> children1 = diagnostic1.getChildren();
        List<Diagnostic> children2 = diagnostic1.getChildren();
        if (children1.size() != children2.size())
        {
          return false;
        }

        for (int i = 0, size = children1.size(); i < size; ++i)
        {
          Diagnostic child1 = children1.get(i);
          Diagnostic child2 = children2.get(i);
          if (!equals(child1, child2))
          {
            return false;
          }
        }

        return true;
      }

      @Override
      protected void buildToolTipText(StringBuilder result, ILabelProvider labelProvider, Diagnostic diagnostic, Object object)
      {
        int index = result.length();
        super.buildToolTipText(result, labelProvider, diagnostic, object);

        // We want to insert a header if the first header is the one for problems on children.
        if (result.indexOf("<h1>Problems on Children</h1>\n") != index)
        {
          result.insert(index, "<h1>Problems</h1>\n");
        }
      }

      @Override
      public String getToolTipText(Object object)
      {
        boolean extend = false;
        boolean showAdvancedProperties = false;
        SetupLocationListener effectiveLocationListener;
        if (object instanceof ToolTipObject)
        {
          ToolTipObject wrapper = (ToolTipObject)object;
          object = wrapper.getWrappedObject();
          effectiveLocationListener = wrapper.getLocationListener();
          wrapper.getLocationListener().setToolTipObject(object, wrapper.getSetupEditor(), toolTipSupport);
          extend = wrapper.isExtended();
          showAdvancedProperties = wrapper.isShowAdvancedProperties();
        }
        else
        {
          // If the option is disabled, don't show the tool tip.
          if (!SetupActionBarContributor.isShowTooltips() || getActionBarContributor().isInformationBrowserShowing())
          {
            return null;
          }

          effectiveLocationListener = locationListener;
        }

        Image image = setupLabelProvider.getImage(object);
        if (image == null)
        {
          return null;
        }

        effectiveLocationListener.setToolTipObject(object, SetupEditor.this, toolTipSupport);

        StringBuilder result = new StringBuilder();

        result.append("<div style='word-break: break-all;'>");

        URI imageURI = ImageURIRegistry.INSTANCE.getImageURI(image);
        String labelText = setupLabelProvider.getText(object);
        if (!extend)
        {
          result.append("<a href='about:blank?extend' style='text-decoration: none; color: inherit;'>");
          result.append("<img style='padding-right: 2pt; margin-top: 2px; margin-bottom: -2pt;' src='");
          result.append(imageURI);
          result.append("'/><b>");
          result.append(DiagnosticDecorator.escapeContent(labelText));
          result.append("</b></a>");
        }
        else
        {
          EList<Object> path = new BasicEList<Object>();
          for (Object parent = itemDelegator.getParent(object); parent != null; parent = itemDelegator.getParent(parent))
          {
            if (parent instanceof ResourceSet)
            {
              break;
            }

            path.add(0, parent);
          }

          int indent = 0;

          for (Object element : path)
          {
            result.append("<div style='margin-left: ").append(indent).append("px;'>");
            ToolTipLabelProvider.renderHTMLPropertyValue(result, itemDelegator, element, true);
            result.append("</div>");

            indent += 10;
          }

          result.append("<div style='margin-left: ").append(indent).append("px;'>");
          result.append("<a href='about:blank?no-extend' style='text-decoration: none; color: inherit;'>");
          result.append("<img style='padding-right: 2pt; margin-top: 2px; margin-bottom: -2pt;' src='");
          result.append(imageURI);
          result.append("'/><b>");
          result.append(DiagnosticDecorator.escapeContent(labelText));
          result.append("</b></a>");
          result.append("</div>");

          indent += 10;

          for (Object child : itemDelegator.getChildren(object))
          {
            result.append("<div style='margin-left: ").append(indent).append("px;'>");
            ToolTipLabelProvider.renderHTMLPropertyValue(result, itemDelegator, child, true);
            result.append("</div>");
          }
        }

        result.append("</div>\n");

        List<IItemPropertyDescriptor> propertyDescriptors = new ArrayList<IItemPropertyDescriptor>();
        List<IItemPropertyDescriptor> underlyingPropertyDescriptors = itemDelegator.getPropertyDescriptors(object);
        if (underlyingPropertyDescriptors != null)
        {
          propertyDescriptors.addAll(underlyingPropertyDescriptors);
        }

        for (Iterator<IItemPropertyDescriptor> it = propertyDescriptors.iterator(); it.hasNext();)
        {
          IItemPropertyDescriptor itemPropertyDescriptor = it.next();
          String[] filterFlags = itemPropertyDescriptor.getFilterFlags(object);
          if (!showAdvancedProperties && filterFlags != null && filterFlags.length > 0 && "org.eclipse.ui.views.properties.expert".equals(filterFlags[0]))
          {
            it.remove();
            continue;
          }

          Object feature = itemPropertyDescriptor.getFeature(object);
          Object propertyValue = itemDelegator.getEditableValue(itemPropertyDescriptor.getPropertyValue(object));
          if (feature instanceof EStructuralFeature)
          {
            // Filter out the description property.
            EStructuralFeature eStructuralFeature = (EStructuralFeature)feature;
            if ("description".equals(eStructuralFeature.getName()) && propertyValue instanceof String)
            {
              String description = propertyValue.toString();
              if (description != null)
              {
                result.append("<h1>Description</h1>");
                result.append(description);
                it.remove();
                continue;
              }
            }
            else if (feature == SetupPackage.Literals.SETUP_TASK__DISABLED)
            {
              it.remove();
              continue;
            }
          }

          // Filter out any properties that have no value.
          String valueLabel = itemPropertyDescriptor.getLabelProvider(object).getText(propertyValue);
          if (StringUtil.isEmpty(valueLabel))
          {
            it.remove();
          }
        }

        String diagnosticText = super.getToolTipText(object);
        if (diagnosticText != null)
        {
          // Improve the layout for the images so the text lines up better.
          Matcher matcher = IMAGE_PATTERN.matcher(diagnosticText);
          StringBuffer improvedDiagnosticText = new StringBuffer();
          while (matcher.find())
          {
            matcher.appendReplacement(improvedDiagnosticText, "$1" + "style='margin-bottom: -2pt;' " + "$2");
          }

          matcher.appendTail(improvedDiagnosticText);

          result.append("\n").append(improvedDiagnosticText);
        }

        String propertyTable = ToolTipLabelProvider.renderHTML(propertyDescriptors, object, true);
        if (propertyTable != null)
        {
          result.append("\n<h1>Properties</h1>\n");
          result.append('\n').append(propertyTable);
        }

        // Improve all the headers to add some nice spacing.
        StringBuffer improvedResult = new StringBuffer();
        Matcher matcher = HEADER_PATTERN.matcher(result);
        while (matcher.find())
        {
          matcher.appendReplacement(improvedResult, "$1" + " style='padding-bottom: 2pt; padding-top: 4pt;'" + "$2");
        }

        matcher.appendTail(improvedResult);

        String finalText = improvedResult.toString();

        try
        {
          AbstractHoverInformationControlManager hoverInformationControlManager = ReflectUtil.getValue("hoverInformationControlManager", toolTipSupport);
          Point size = UIUtil.caclcuateSize(finalText);
          hoverInformationControlManager.setSizeConstraints(size.x, size.y + (propertyDescriptors.size() + 1) / 3 + 1, true, false);
        }
        catch (Throwable throwable)
        {
          // Ignore.
        }

        if (UIUtil.isBrowserAvailable())
        {
          return finalText;
        }

        return UIUtil.getRenderableHTML(finalText);
      }

      @Override
      protected void buildToolTipMessage(StringBuilder result, ILabelProvider labelProvider, Object object, Diagnostic diagnostic, int indentation)
      {
        StringBuilder builder = new StringBuilder();
        super.buildToolTipMessage(builder, labelProvider, object, diagnostic, indentation);

        // Fix the image for the small diagnostic severity icons that they don't have so much padding applied like the larger images.
        StringBuffer improvedResult = new StringBuffer();
        Matcher matcher = IMAGE_PATTERN.matcher(builder);
        if (matcher.find())
        {
          matcher.appendReplacement(improvedResult, "$1" + "style='padding-bottom: 1px;' " + "$2");
        }

        matcher.appendTail(improvedResult);
        result.append(improvedResult);
      }
    };

    final Font font = viewer.getControl().getFont();
    viewer.setLabelProvider(new DecoratingColumLabelProvider(setupLabelProvider, diagnosticDecorator)
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

      @Override
      public Font getFont(Object object)
      {
        Font result = super.getFont(object);
        if (foreignResourceDecoration && object instanceof SetupTask)
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
    });

    final Control control = viewer.getControl();
    control.addMouseListener(new MouseListener()
    {
      public void mouseDoubleClick(MouseEvent e)
      {
        try
        {
          getSite().getPage().showView("org.eclipse.ui.views.PropertySheet", null, IWorkbenchPage.VIEW_VISIBLE);
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

      public void mouseUp(final MouseEvent e)
      {
        if ((e.stateMask & SWT.MOD3) != 0)
        {
          final boolean showTooltips = SetupActionBarContributor.isShowTooltips();
          SetupActionBarContributor.setShowTooltips(true);
          Event exitEvent = new Event();
          exitEvent.display = e.display;
          exitEvent.x = -1;
          exitEvent.y = -1;
          control.notifyListeners(SWT.MouseExit, exitEvent);

          ReflectUtil.setValue("currentCell", toolTipSupport, null);

          Event event = new Event();
          event.display = e.display;
          event.x = e.x;
          event.y = e.y;
          control.notifyListeners(SWT.MouseHover, event);

          SetupActionBarContributor.setShowTooltips(showTooltips);
        }
      }
    });

    createContextMenuFor(viewer);
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
    final Tree tree = new Tree(getContainer(), SWT.MULTI);
    selectionViewer = new TreeViewer(tree);
    setCurrentViewer(selectionViewer);

    configure(selectionViewer, false);

    selectionViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory)
    {
      @Override
      public Object getParent(Object object)
      {
        // Return the direct resource as the parent so that selection finds the object directly in its own resource.
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

    selectionViewer.setInput(loadingResourceInput);

    new AdapterFactoryTreeEditor(selectionViewer.getTree(), adapterFactory);

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
        SetupCoreUtil.configureResourceSet(resourceSet);

        final ResourceMirror resourceMirror = new ResourceMirror(resourceSet)
        {
          @Override
          protected void run(String taskName, IProgressMonitor monitor)
          {
            SetupEditor.this.resourceMirror = this;
            createModel();
            resolveProxies();
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

    private ResourceLocator resourceLocator;

    private AdapterFactoryItemDelegator itemDelegator;

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

      parent.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          contentOutlinePage = null;
        }
      });

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

      itemDelegator = new AdapterFactoryItemDelegator(adapterFactory);

      AdapterFactoryContentProvider contentProvider = new AdapterFactoryContentProvider(adapterFactory)
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
                if (eObject.eResource() == resource && setupTaskPerformer.isVariableUsed(name, eObject, false))
                {
                  variableUsages.add(eObject);
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
      };

      contentOutlineViewer.setContentProvider(contentProvider);

      configure(contentOutlineViewer, true);

      labelProvider = (ILabelProvider)contentOutlineViewer.getLabelProvider();

      selectionViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        public void selectionChanged(SelectionChangedEvent event)
        {
          IStructuredSelection selection = (IStructuredSelection)event.getSelection();
          if (selectionViewer != null && !selection.isEmpty() && contentOutlinePage != null)
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

        ResourceSet resourceSet = editingDomain.getResourceSet();
        if (resourceLocator != null)
        {
          resourceLocator.dispose();
        }

        resourceLocator = new ResourceLocator(resourceSet);

        for (Notifier notifier : notifiers)
        {
          notifier.eAdapters().clear();
        }

        notifiers.clear();

        try
        {
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

    private ItemProvider getTriggeredTasks(final Project project)
    {
      class ProjectItemProvider extends ItemProvider implements IWrapperItemProvider
      {
        public ProjectItemProvider()
        {
          super(labelProvider.getText(project), labelProvider.getImage(project));
        }

        public Object getValue()
        {
          return project;
        }

        public Object getOwner()
        {
          return project;
        }

        public EStructuralFeature getFeature()
        {
          return null;
        }

        public int getIndex()
        {
          return 0;
        }

        public void setIndex(int index)
        {
        }
      }

      final ItemProvider projectItem = new ProjectItemProvider();

      EList<Object> projectItemChildren = projectItem.getChildren();
      EList<Stream> streams = project.getStreams();
      for (final Stream stream : streams)
      {
        class BranchItemProvider extends ItemProvider implements IItemPropertySource
        {
          public BranchItemProvider()
          {
            super(labelProvider.getText(stream), labelProvider.getImage(stream));
          }

          public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
          {
            List<IItemPropertyDescriptor> descriptors = itemDelegator.getPropertyDescriptors(stream);

            List<IItemPropertyDescriptor> result = new ArrayList<IItemPropertyDescriptor>();
            for (IItemPropertyDescriptor descriptor : descriptors)
            {
              result.add(new ItemPropertyDescriptorDecorator(stream, descriptor));
            }

            return result;
          }

          public IItemPropertyDescriptor getPropertyDescriptor(Object object, Object propertyID)
          {
            IItemPropertyDescriptor descriptor = itemDelegator.getPropertyDescriptor(stream, propertyID);
            if (descriptor != null)
            {
              return new ItemPropertyDescriptorDecorator(stream, descriptor);
            }

            return null;
          }

          public Object getEditableValue(Object object)
          {
            return itemDelegator.getEditableValue(stream);
          }
        }

        final ItemProvider branchItem = new BranchItemProvider();
        projectItemChildren.add(branchItem);

        ProductVersion version = null;
        ProjectCatalog projectCatalog = project.getProjectCatalog();
        if (projectCatalog != null)
        {
          EObject rootContainer = EcoreUtil.getRootContainer(projectCatalog);
          if (!(rootContainer instanceof Index))
          {
            for (Resource resource : editingDomain.getResourceSet().getResources())
            {
              Object index = EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.INDEX);
              if (index != null)
              {
                rootContainer = (EObject)index;
              }
            }
          }

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
          ResourceSet resourceSet = getEditingDomain().getResourceSet();
          URIConverter uriConverter = resourceSet.getURIConverter();

          SetupTaskPerformer setupTaskPerformer = new SetupTaskPerformer(uriConverter, SetupPrompter.CANCEL, trigger, setupContext, stream);
          List<SetupTask> triggeredSetupTasks = new ArrayList<SetupTask>(setupTaskPerformer.getTriggeredSetupTasks());
          setupTaskPerformer.redirectTriggeredSetupTasks();

          if (!triggeredSetupTasks.isEmpty())
          {
            URI baseURI = URI.createURI("performer:/" + stream.getQualifiedName());
            URI uri = baseURI.appendSegment("tasks.setup");

            Resource fakeResource = new BaseResourceImpl(uri);
            fakeResource.eAdapters().add(editingDomainProvider);
            EList<EObject> fakeResourceContents = fakeResource.getContents();
            resourceLocator.map(uri, fakeResource);

            for (EObject eObject : setupTaskPerformer.getCopyMap().values())
            {
              notifiers.add(eObject);

              Resource resource = ((InternalEObject)eObject).eDirectResource();
              if (resource != null && !resource.eAdapters().contains(editingDomainProvider))
              {
                resource.eAdapters().add(editingDomainProvider);
                notifiers.add(resource);

                URI originalURI = resource.getURI();
                URI newURI = baseURI.appendSegment(originalURI.scheme() + ":").appendSegments(originalURI.segments());
                resource.setURI(newURI);
                resourceLocator.map(newURI, resource);
              }

              resource = eObject.eResource();
              if (resource == null || resource == fakeResource)
              {
                try
                {
                  EcoreUtil.setID(eObject, null);
                }
                catch (IllegalArgumentException ex)
                {
                  // Ignore.
                }

                if (eObject.eContainer() == null)
                {
                  fakeResourceContents.add(eObject);
                }
              }
            }

            ItemProvider undeclaredVariablesItem = new VariableContainer(setupTaskPerformer, "Undeclared Variables", UNDECLARED_VARIABLE_GROUP_IMAGE);
            EList<Object> undeclaredVariablesItemChildren = undeclaredVariablesItem.getChildren();
            Set<String> undeclaredVariables = setupTaskPerformer.getUndeclaredVariables();
            for (String key : sortStrings(undeclaredVariables))
            {
              VariableTask contextVariableTask = SetupFactory.eINSTANCE.createVariableTask();
              fakeResourceContents.add(contextVariableTask);
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
              if (contextVariableTask.eContainer() == null && contextVariableTask.eResource() == null)
              {
                fakeResourceContents.add(contextVariableTask);
              }

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
              if (contextVariableTask.eContainer() == null && contextVariableTask.eResource() == null)
              {
                fakeResourceContents.add(contextVariableTask);
              }

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

    public SetupEditor getSetupEditor()
    {
      return SetupEditor.this;
    }

    public class ResourceLocator extends ResourceSetImpl.MappedResourceLocator
    {
      public ResourceLocator(ResourceSet resourceSet)
      {
        super((ResourceSetImpl)resourceSet);
      }

      @Override
      public void map(URI normalizedURI, Resource resource)
      {
        super.map(normalizedURI, resource);
      }

      @Override
      public void dispose()
      {
        super.dispose();
      }
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

      getSite().getShell().getDisplay().asyncExec(new Runnable()
      {
        public void run()
        {
          if (contentOutlinePage != null && resourceMirror == null)
          {
            contentOutlinePage.update(2);
          }
        }
      });

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

  public void doRevert()
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
      ISchedulingRule currentRule = Job.getJobManager().currentRule();

      // This runs the options, and shows progress.
      //
      new ProgressMonitorDialog(getSite().getShell()).run(currentRule == null, false, operation);

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

    // Restore the indexes original proxies before saving.
    // Because of our special model handling, references to the packages resolve to the actual generated package resources,
    // but we'd like to preserve the original proxy URI when saving and index.
    if (reproxifier != null)
    {
      reproxifier.run();
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
   * @generated NOT
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

  /**
   * @author Ed Merks
   */
  private static class SetupLocationListener extends ColumnViewerInformationControlToolTipSupport.PathLocationListener
  {
    private static boolean styleSheetInitialized;

    private Browser browser;

    private StyledText noBrowser;

    private Composite content;

    private ToolBar toolBar;

    private ToolItem editTextItem;

    private ToolItem editSetupItem;

    private ToolItem showToolTipsItem;

    private ToolItem liveValidationItem;

    private ToolTipObject toolTipObject;

    private List<ToolTipObject> toolTipObjects = new ArrayList<ToolTipObject>();

    private int toolTipIndex = -1;

    private ToolItem backwardItem;

    private ToolItem forwardItem;

    private ToolItem showAdvancedPropertiesItem;

    private SetupEditor setupEditor;

    private boolean editorSpecific;

    private final Map<SetupEditor, DisposeListener> editorDisposeListeners = new HashMap<SetupEditor, DisposeListener>();

    private ColumnViewerInformationControlToolTipSupport toolTipSupport;

    private URI mostRecentChangingLocation;

    private Canvas canvas;

    private SetupLocationListener(boolean editorSpecific)
    {
      super(null);
      this.editorSpecific = editorSpecific;
    }

    public void setToolTipSupport(ColumnViewerInformationControlToolTipSupport toolTipSupport)
    {
      this.toolTipSupport = toolTipSupport;
      initializeCreator();
      initializeStyleSheet();
    }

    private void initializeCreator()
    {
      ReflectUtil.setValue("replacementInformationControlCreator", toolTipSupport, new AbstractReusableInformationControlCreator()
      {
        @Override
        @SuppressWarnings("restriction")
        protected IInformationControl doCreateInformationControl(Shell parent)
        {
          IInformationControl informationControl;
          if (org.eclipse.jface.internal.text.html.BrowserInformationControl.isAvailable(parent))
          {
            String symbolicFont = ReflectUtil.invokeMethod("getSymbolicFont", toolTipSupport);
            org.eclipse.jface.internal.text.html.BrowserInformationControl browserInformationControl = new org.eclipse.jface.internal.text.html.BrowserInformationControl(
                parent, symbolicFont, true)
            {
              @Override
              protected void createContent(Composite parent)
              {
                super.createContent(parent);
                content = browser = ReflectUtil.getValue("fBrowser", this);
                createToolBar();
              }

              @Override
              public Rectangle computeTrim()
              {
                Rectangle trim = super.computeTrim();
                trim.height += toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;
                return trim;
              }
            };

            initializeCreator();
            browserInformationControl.addLocationListener(SetupLocationListener.this);
            informationControl = browserInformationControl;
          }
          else
          {
            informationControl = new DefaultInformationControl(parent, (ToolBarManager)null)
            {
              @Override
              protected void createContent(Composite parent)
              {
                super.createContent(parent);
                content = noBrowser = ReflectUtil.getValue("fText", this);
                createToolBar();
              }

              @Override
              public Rectangle computeTrim()
              {
                Rectangle trim = super.computeTrim();
                trim.height += toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).y;
                return trim;
              }
            };
          }

          Color foregroundColor = ReflectUtil.getValue("foregroundColor", toolTipSupport);
          if (foregroundColor != null)
          {
            informationControl.setForegroundColor(foregroundColor);
          }

          Color backgroundColor = ReflectUtil.getValue("backgroundColor", toolTipSupport);
          if (backgroundColor != null)
          {
            informationControl.setBackgroundColor(backgroundColor);
          }

          return informationControl;
        }
      });
    }

    public void createToolBar(Browser browser, StyledText noBrowser)
    {
      this.browser = browser;
      this.noBrowser = noBrowser;
      content = browser == null ? noBrowser : browser;

      createToolBar();
    }

    private void createToolBar()
    {
      Composite parent = content.getParent();
      if (browser != null)
      {
        applyCookies();

        browser.addAuthenticationListener(new AuthenticationListener()
        {
          public void authenticate(AuthenticationEvent event)
          {
            if (mostRecentChangingLocation != null)
            {
              Authorization authorization = SetupCoreUtil.AUTHORIZATION_HANDLER.authorize(mostRecentChangingLocation);
              if (authorization.isAuthorized())
              {
                event.user = authorization.getUser();
                event.password = authorization.getPassword();
                return;
              }
            }

            event.doit = false;
          }
        });
      }

      // Change the parent to use a grid layout.
      GridLayout gridLayout = new GridLayout();
      gridLayout.marginHeight = 0;
      gridLayout.marginWidth = 0;
      gridLayout.horizontalSpacing = 0;
      gridLayout.verticalSpacing = 0;
      parent.setLayout(gridLayout);

      // The main control should fill everything as much as possible.
      GridData controlGridData = new GridData();
      controlGridData.verticalAlignment = GridData.FILL;
      controlGridData.grabExcessVerticalSpace = true;
      controlGridData.horizontalAlignment = GridData.FILL;
      controlGridData.grabExcessHorizontalSpace = true;
      content.setLayoutData(controlGridData);

      // The tool bar should just be as tall as it needs to be and fill the horizontal space.
      toolBar = new ToolBar(parent, SWT.FLAT | SWT.HORIZONTAL | SWT.SHADOW_OUT);
      GridData toolBarGridData = new GridData();
      toolBarGridData.verticalAlignment = GridData.FILL;
      toolBarGridData.horizontalAlignment = GridData.FILL;
      toolBarGridData.grabExcessHorizontalSpace = true;
      toolBar.setLayoutData(toolBarGridData);

      class NavigationListener extends SelectionAdapter
      {
        private final boolean forward;

        public NavigationListener(boolean forward)
        {
          this.forward = forward;
        }

        @Override
        public void widgetSelected(SelectionEvent e)
        {
          if (e.detail == SWT.ARROW)
          {
            Rectangle bounds = (forward ? forwardItem : backwardItem).getBounds();
            Point location = new Point(bounds.x, bounds.y + bounds.height);
            location = toolBar.toDisplay(location);

            List<ItemProvider> items = new ArrayList<ItemProvider>();
            for (int i = toolTipObjects.size() - 1; i >= 0; --i)
            {
              ToolTipObject wrapper = toolTipObjects.get(i);
              ILabelProvider labelProvider = (ILabelProvider)wrapper.getSetupEditor().selectionViewer.getLabelProvider();

              Object wrappedObject = wrapper.getWrappedObject();
              String text;
              if (wrappedObject instanceof URI)
              {
                text = wrappedObject.toString();
              }
              else
              {
                URI uri = SetupActionBarContributor.getEditURI(wrappedObject, true);
                if (uri == null)
                {
                  text = labelProvider.getText(wrappedObject);
                }
                else
                {
                  text = uri.toString();
                  text = labelProvider.getText(wrappedObject);
                }
              }

              Image image = labelProvider.getImage(wrappedObject);
              items.add(new ItemProvider(text, image));
            }

            int limit = 21;
            int size = items.size();
            int index = size - toolTipIndex - 1;

            int start = 0;
            int end = size;
            if (size > limit)
            {
              if (index - limit / 2 < 0)
              {
                start = 0;
              }
              else
              {
                start = index - limit / 2;
              }

              end = start + limit;
              if (end > size)
              {
                end = size;
                start = end - limit;
                if (start < 0)
                {
                  start = 0;
                }
              }
            }

            showMenu(items, location, index, start, end, limit);
          }
          else if (forward)
          {
            navigate(toolTipIndex + 1);
          }
          else
          {
            navigate(toolTipIndex - 1);
          }
        }

        private void showMenu(final List<ItemProvider> items, final Point location, final int index, final int start, final int end, final int limit)
        {
          Menu menu = new Menu(content.getShell());
          final int size = items.size();
          int adjustedStart = start;
          if (end == size)
          {
            if (start > 0)
            {
              --adjustedStart;
            }
          }

          int adjustedEnd = end;
          if (start == 0)
          {
            if (end < size)
            {
              ++adjustedEnd;
            }
          }

          for (int i = 0; i < size; ++i)
          {
            if (i < adjustedStart)
            {
              if (i == adjustedStart - 1)
              {
                MenuItem menuItem = new MenuItem(menu, SWT.RADIO);
                int count = (adjustedStart + limit - 1) / limit;
                menuItem.setText("Forward More (" + count + ')');
                menuItem.setImage(SetupEditorPlugin.INSTANCE.getSWTImage("forward"));
                menuItem.addSelectionListener(new SelectionAdapter()
                {
                  @Override
                  public void widgetSelected(SelectionEvent e)
                  {
                    int newStart = start - limit;
                    if (newStart < 0)
                    {
                      newStart = 0;
                    }

                    showMenu(items, location, index, newStart, newStart + limit, limit);
                  }
                });
              }
            }
            else if (i == adjustedEnd)
            {
              MenuItem menuItem = new MenuItem(menu, SWT.RADIO);
              int count = (size - adjustedEnd + limit - 1) / limit;
              menuItem.setText("Back More (" + count + ')');
              menuItem.setImage(SetupEditorPlugin.INSTANCE.getSWTImage("backward"));
              menuItem.addSelectionListener(new SelectionAdapter()
              {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                  int newEnd = end + limit;
                  if (newEnd > size)
                  {
                    newEnd = size;
                  }

                  showMenu(items, location, index, newEnd - limit, newEnd, limit);
                }
              });
              break;
            }
            else
            {
              MenuItem menuItem = new MenuItem(menu, SWT.RADIO);
              ItemProvider item = items.get(i);
              menuItem.setText(item.getText());
              Image image = (Image)item.getImage();
              if (image != null)
              {
                menuItem.setImage(image);
              }

              if (i == index)
              {
                menuItem.setSelection(true);
                menu.setDefaultItem(menuItem);
              }

              final int itemIndex = i;
              menuItem.addSelectionListener(new SelectionAdapter()
              {
                @Override
                public void widgetSelected(SelectionEvent e)
                {
                  navigate(size - itemIndex - 1);
                }
              });
            }
          }

          menu.setLocation(location.x, location.y);
          menu.setVisible(true);
        }
      }

      backwardItem = createItem(SWT.DROP_DOWN, "backward", "Back", new NavigationListener(false));

      forwardItem = createItem(SWT.DROP_DOWN, "forward", "Forward", new NavigationListener(true));

      new ToolItem(toolBar, SWT.SEPARATOR);

      editSetupItem = createItem(SWT.PUSH, "edit_setup", "Open in Setup Editor", new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          Object unwrappedObject = ToolTipObject.unwrap(toolTipObject);
          setupEditor.getActionBarContributor().openInSetupEditor(unwrappedObject);
        }
      });

      editTextItem = createItem(SWT.PUSH, "edit_text", "Open in Text Editor", new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          Object unwrappedObject = ToolTipObject.unwrap(toolTipObject);
          setupEditor.getActionBarContributor().openInTextEditor(unwrappedObject);
        }
      });

      showAdvancedPropertiesItem = createItem(SWT.CHECK, "filter_advanced_properties", "Show Advanced Properties", new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          navigate(toolTipIndex);
        }
      });

      if (editorSpecific)
      {
        createItem(SWT.PUSH, "show_properties_view", "Show Properties View", new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            try
            {
              setupEditor.getSite().getWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.PropertySheet");
            }
            catch (PartInitException ex)
            {
              SetupEditorPlugin.INSTANCE.log(ex);
            }
          }
        });

        createItem(SWT.PUSH, "open_browser", "Open Information Browser", new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            setupEditor.getActionBarContributor().showInformationBrowser(ToolTipObject.unwrap(toolTipObject));
          }
        });

        new ToolItem(toolBar, SWT.SEPARATOR);

        showToolTipsItem = createItem(SWT.CHECK, "show_tooltips", "Show Tooltips (Alt-Mouse-Click)", new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            boolean show = showToolTipsItem.getSelection();
            SetupActionBarContributor.setShowTooltips(show);
          }
        });

        liveValidationItem = createItem(SWT.CHECK, "live_validation", "Live Validation", new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            boolean liveValidation = liveValidationItem.getSelection();
            setupEditor.getActionBarContributor().setLiveValidation(liveValidation);
          }
        });
      }
    }

    private void initializeStyleSheet()
    {
      if (!styleSheetInitialized)
      {
        // String styleSheet = toolTipSupport.getStyleSheet();
        // ReflectUtil.setValue("defaultStyleSheet", toolTipSupport,
        // styleSheet + "\n" + //
        // ".hover-link: \t{ text-decoration: none; color: inherit; }\n" + //
        // "hover-link:hover \t{ text-decoration: underline; }\n" + //
        // "\n");
        styleSheetInitialized = true;
      }
    }

    protected void setEditor(SetupEditor setupEditor)
    {
      this.setupEditor = setupEditor;

      if (!editorSpecific && setupEditor != null)
      {
        DisposeListener disposeListener = editorDisposeListeners.get(setupEditor);
        if (disposeListener == null)
        {
          disposeListener = new DisposeListener()
          {
            private SetupEditor setupEditor = SetupLocationListener.this.setupEditor;

            public void widgetDisposed(DisposeEvent e)
            {
              editorDisposeListeners.remove(setupEditor);

              int index = 0;
              int count = 0;
              for (Iterator<ToolTipObject> it = toolTipObjects.iterator(); it.hasNext();)
              {
                ToolTipObject wrapper = it.next();
                if (wrapper.getSetupEditor() == setupEditor)
                {
                  it.remove();
                  if (index < toolTipIndex)
                  {
                    ++count;
                  }
                }

                ++index;
              }

              toolTipIndex -= count;
              navigate(toolTipObjects.size() == 0 ? -1 : toolTipIndex);
            }
          };

          setupEditor.getContainer().addDisposeListener(disposeListener);
          editorDisposeListeners.put(setupEditor, disposeListener);
        }
      }
    }

    public void setToolTipObject(Object toolTipObject, SetupEditor setupEditor, ColumnViewerInformationControlToolTipSupport toolTipSupport)
    {
      this.toolTipObject = new ToolTipObject(toolTipObject, this, setupEditor, false, false);

      setEditor(setupEditor);
      setToolTipSupport(toolTipSupport);
      toolTipObjects = new ArrayList<ToolTipObject>(toolTipObjects.subList(0, toolTipIndex + 1));
      if (toolTipIndex == -1 || toolTipObjects.get(toolTipIndex).getWrappedObject() != toolTipObject)
      {
        toolTipObjects.add(this.toolTipObject);
        toolTipIndex = toolTipObjects.size() - 1;
      }
    }

    private void updateEnablement()
    {
      backwardItem.setEnabled(toolTipIndex > 0);
      forwardItem.setEnabled(toolTipIndex + 1 < toolTipObjects.size());
      editSetupItem.setEnabled(SetupActionBarContributor.getEditURI(ToolTipObject.unwrap(toolTipObject), !editorSpecific) != null);
      editTextItem.setEnabled(SetupActionBarContributor.getEditURI(ToolTipObject.unwrap(toolTipObject), true) != null);

      if (editorSpecific)
      {
        showToolTipsItem.setSelection(SetupActionBarContributor.isShowTooltips());
        liveValidationItem.setSelection(setupEditor.getActionBarContributor().isLiveValidation());
      }
    }

    @Override
    public void changed(LocationEvent event)
    {
      // This only ever happens on the Mac.
      if (canvas != null)
      {
        canvas.dispose();
        canvas = null;
        GridData gridData = (GridData)content.getLayoutData();
        gridData.exclude = false;
        content.getParent().layout();
      }

      if (browser != null)
      {
        String url = browser.getUrl();
        if (!"about:blank".equals(url))
        {
          URI uri = URI.createURI(event.location);
          uri = ECFURIHandlerImpl.transform(uri, null);

          if (toolTipIndex >= 0 && toolTipIndex < toolTipObjects.size() && !toolTipObjects.get(toolTipIndex).getWrappedObject().toString().equals(url))
          {
            setToolTipObject(URI.createURI(url), setupEditor, toolTipSupport);
            toolTipIndex = toolTipObjects.size() - 1;
            updateEnablement();
          }
        }
      }
    }

    @Override
    public void changing(LocationEvent event)
    {
      // Transform the URI if necessary, checking if a login URI must be accessed first.
      URI originalURI = URI.createURI(event.location);

      if (OS.INSTANCE.isMac())
      {
        // On the Mac, the query URI isn't resolved against the blank page URI.
        if (originalURI.trimQuery().isCurrentDocumentReference())
        {
          originalURI = URI.createURI("about:blank" + originalURI);
        }

        String query = originalURI.query();
        if (query != null)
        {
          originalURI = originalURI.trimQuery().appendQuery(query.replace("%5B", "[").replaceAll("%5D", "]"));
        }

        String fragment = originalURI.fragment();
        if (fragment != null)
        {
          originalURI = originalURI.trimFragment().appendFragment(fragment.replace("%5B", "[").replaceAll("%5D", "]"));
        }
      }

      URI uri = originalURI;
      Map<Object, Object> options = new HashMap<Object, Object>();
      uri = ECFURIHandlerImpl.transform(uri, options);
      event.location = uri.toString();

      // Record this event because the authentication listener is given a bogus location different from this most recent changing notification's location.
      mostRecentChangingLocation = uri;

      Object source = event.getSource();
      if (source instanceof Composite)
      {
        content = (Composite)source;

        if (content instanceof Browser)
        {
          browser = (Browser)content;
        }
        else if (content instanceof StyledText)
        {
          noBrowser = (StyledText)content;
        }

        if (setupEditor != null)
        {
          ResourceSet resourceSet = setupEditor.editingDomain.getResourceSet();
          URI trimmedURI = uri.trimFragment();
          Resource resource = resourceSet.getResource(trimmedURI, false);

          // It might be a reference to the logical schema location of a generated model.
          if (resource == null)
          {
            for (Object value : resourceSet.getPackageRegistry().values())
            {
              if (value instanceof EPackage)
              {
                EPackage ePackage = (EPackage)value;
                if (ePackage.eResource().getURI().equals(trimmedURI))
                {
                  resource = ePackage.eResource();
                  break;
                }
              }
            }
          }

          Object selection = resource;
          if (resource != null)
          {
            String fragment = uri.fragment();
            if (fragment != null)
            {
              EObject eObject = resource.getEObject(fragment);
              if (eObject != null)
              {
                selection = eObject;
              }
            }

            setSelection(selection);
            event.doit = false;
          }

          if (event.doit)
          {
            if ("path".equals(uri.scheme()))
            {
              viewer = setupEditor.selectionViewer;
              super.changing(event);
            }
          }

          if (event.location.equals("about:blank"))
          {
            // Force event processing; on the Mac this allows the browser to redraw the text.
            if (OS.INSTANCE.isMac())
            {
              UIUtil.asyncExec(browser, new Runnable()
              {
                public void run()
                {
                  content.getDisplay().readAndDispatch();
                }
              });
            }
          }
          else if (event.location.startsWith("about:blank#"))
          {
            event.doit = false;
          }
          else if ("about:blank?extend".equals(event.location))
          {
            ToolTipObject wrapper = toolTipObjects.get(toolTipIndex);
            ToolTipObject extendedWrapper = new ToolTipObject(wrapper.getWrappedObject(), this, wrapper.getSetupEditor(), true, false);
            toolTipObjects.set(toolTipIndex, extendedWrapper);
            navigate(toolTipIndex);

            event.doit = false;
          }
          else if ("about:blank?no-extend".equals(event.location))
          {
            ToolTipObject wrapper = toolTipObjects.get(toolTipIndex);
            ToolTipObject extendedWrapper = new ToolTipObject(wrapper.getWrappedObject(), this, wrapper.getSetupEditor(), false, false);
            toolTipObjects.set(toolTipIndex, extendedWrapper);
            navigate(toolTipIndex);

            event.doit = false;
          }
          else if ("property".equals(uri.scheme()))
          {
            ToolTipObject wrapper = toolTipObjects.get(toolTipIndex);
            setupEditor.getActionBarContributor().openInPropertiesView(wrapper.getSetupEditor(), wrapper.getWrappedObject(), URI.decode(uri.segment(0)));

            event.doit = false;
          }
          else if (!originalURI.equals(uri))
          {
            // If the URI was transformed, we don't want to change to the original one but rather navigate to the transformed URI.
            if (browser != null)
            {
              browser.setUrl(uri.toString());
            }
            event.doit = false;
          }
        }

        updateEnablement();
      }
    }

    private void applyCookies()
    {
      List<java.net.URI> uris = ECFURIHandlerImpl.COOKIE_STORE.getURIs();
      for (java.net.URI cookieURI : uris)
      {
        String url = cookieURI.toString();
        for (HttpCookie httpCookie : ECFURIHandlerImpl.COOKIE_STORE.get(cookieURI))
        {
          Browser.setCookie(httpCookie.getValue(), url);
        }
      }
    }

    private ToolItem createItem(int style, String imageKey, String toolTipText, SelectionListener selectionListener)
    {
      ToolItem toolItem = new ToolItem(toolBar, style);
      toolItem.setImage(SetupEditorPlugin.INSTANCE.getSWTImage(imageKey));
      toolItem.setToolTipText(toolTipText);
      toolItem.addSelectionListener(selectionListener);
      return toolItem;
    }

    @SuppressWarnings("restriction")
    private String getFullHTML(String text)
    {
      if (setupEditor == null)
      {
        return text;
      }

      StringBuffer result = new StringBuffer(text);
      String styleSheet = toolTipSupport.getStyleSheet();
      String symbolicFont = (String)ReflectUtil.invokeMethod("getSymbolicFont", toolTipSupport);

      FontData fontData = JFaceResources.getFontRegistry().getFontData(symbolicFont)[0];
      styleSheet = org.eclipse.jface.internal.text.html.HTMLPrinter.convertTopLevelFont(styleSheet, fontData);
      Color foregroundColor = content.getForeground();
      Color backgroundColor = content.getBackground();

      org.eclipse.jface.internal.text.html.HTMLPrinter.insertPageProlog(result, 0, foregroundColor == null ? null : foregroundColor.getRGB(),
          backgroundColor == null ? null : backgroundColor.getRGB(), styleSheet);
      org.eclipse.jface.internal.text.html.HTMLPrinter.addPageEpilog(result);
      return result.toString();
    }

    protected void navigate(int index)
    {
      if (index == -1)
      {
        setEditor(null);
        toolTipObject = null;
        toolTipIndex = -1;
        setText("No history");
      }
      else
      {
        List<ToolTipObject> toolTipObjects = this.toolTipObjects;

        toolTipObject = toolTipObjects.get(index);

        setEditor(toolTipObject.getSetupEditor());

        Object wrappedObject = toolTipObject.getWrappedObject();
        if (wrappedObject instanceof URI)
        {
          if (browser != null)
          {
            browser.setUrl(wrappedObject.toString());
          }
        }
        else
        {
          IToolTipProvider toolTipProvider = (IToolTipProvider)setupEditor.selectionViewer.getLabelProvider();
          String toolTipText = toolTipProvider.getToolTipText(new ToolTipObject(toolTipObject, showAdvancedPropertiesItem.getSelection()));
          setText(toolTipText);
        }

        this.toolTipObjects = toolTipObjects;
        toolTipIndex = index;
      }

      backwardItem.setEnabled(toolTipIndex > 0);
      forwardItem.setEnabled(toolTipIndex + 1 < toolTipObjects.size());
    }

    protected void setText(String text)
    {
      if (!StringUtil.isEmpty(text))
      {
        if (browser != null)
        {
          browser.setText(getFullHTML(text), true);

          // On the Mac and Linux, the browser turns white for a while, which is ugly.
          // So here we temporarily cover it with a canvas with the proper background color.
          if (!OS.INSTANCE.isWin() && canvas == null)
          {
            Composite parent = browser.getParent();

            GridData gridData = (GridData)browser.getLayoutData();
            gridData.exclude = true;
            canvas = new Canvas(parent, SWT.NONE);

            GridData canvasGridData = new GridData();
            canvasGridData.verticalAlignment = GridData.FILL;
            canvasGridData.grabExcessVerticalSpace = true;
            canvasGridData.horizontalAlignment = GridData.FILL;
            canvasGridData.grabExcessHorizontalSpace = true;

            canvas.moveAbove(browser);
            canvas.setLayoutData(canvasGridData);
            canvas.setBackground(browser.getBackground());
            parent.layout();
          }
        }
        else
        {
          @SuppressWarnings("restriction")
          org.eclipse.jface.internal.text.html.HTMLTextPresenter htmlTextPresenter = new org.eclipse.jface.internal.text.html.HTMLTextPresenter(false);
          org.eclipse.jface.text.TextPresentation textPresentation = new org.eclipse.jface.text.TextPresentation();
          @SuppressWarnings("restriction")
          String updatePresentation = htmlTextPresenter.updatePresentation(noBrowser, text, textPresentation, Integer.MAX_VALUE, Integer.MAX_VALUE);
          noBrowser.setText(updatePresentation);
          TextPresentation.applyTextPresentation(textPresentation, noBrowser);
        }
      }
    }

    @Override
    protected void setSelection(Object object)
    {
      IToolTipProvider toolTipProvider = (IToolTipProvider)setupEditor.selectionViewer.getLabelProvider();
      String toolTipText = toolTipProvider.getToolTipText(new ToolTipObject(object, this, setupEditor, false, showAdvancedPropertiesItem.getSelection()));
      setText(toolTipText);
    }

    public void dispose()
    {
      for (Entry<SetupEditor, DisposeListener> entry : editorDisposeListeners.entrySet())
      {
        entry.getKey().getContainer().removeDisposeListener(entry.getValue());
      }
    }
  }

  /**
   * @author Ed Merks
   */
  private static final class ToolTipObject
  {
    private final Object wrappedObject;

    private final SetupLocationListener locationListener;

    private final SetupEditor setupEditor;

    private final boolean extended;

    private final boolean showAdvancedProperties;

    public ToolTipObject(Object wrappedObject, SetupLocationListener locationListener, SetupEditor setupEditor, boolean extended,
        boolean showAdvancedProperties)
    {
      this.wrappedObject = wrappedObject;
      this.locationListener = locationListener;
      this.setupEditor = setupEditor;
      this.extended = extended;
      this.showAdvancedProperties = showAdvancedProperties;
    }

    public ToolTipObject(ToolTipObject toolTipObject, boolean showAdvancedProperties)
    {
      wrappedObject = toolTipObject.wrappedObject;
      locationListener = toolTipObject.locationListener;
      setupEditor = toolTipObject.setupEditor;
      extended = toolTipObject.extended;
      this.showAdvancedProperties = showAdvancedProperties;
    }

    public Object getWrappedObject()
    {
      return wrappedObject;
    }

    public SetupLocationListener getLocationListener()
    {
      return locationListener;
    }

    public SetupEditor getSetupEditor()
    {
      return setupEditor;
    }

    public boolean isExtended()
    {
      return extended;
    }

    public boolean isShowAdvancedProperties()
    {
      return showAdvancedProperties;
    }

    @Override
    public String toString()
    {
      return super.toString() + " -> " + wrappedObject;
    }

    public static Object unwrap(Object object)
    {
      if (object instanceof ToolTipObject)
      {
        return ((ToolTipObject)object).getWrappedObject();
      }

      return object;
    }
  }

  /**
   * A Dialog for browsing tool tip information in a non-modal shell associated with a workbench window.
   * Generally the constructor is not used to create an instance but rather the {@link #openFor(IWorkbenchWindow) open} method which maintains one instance per workbench window..
   *
   * @author Ed Merks
   */
  public static class BrowserDialog extends DockableDialog
  {
    private final SetupLocationListener locationListener = new SetupLocationListener(false);

    /**
     * Listens to any interesting selection changes in the workbench.
     */
    private final ISelectionListener selectionListener = new ISelectionListener()
    {
      public void selectionChanged(IWorkbenchPart part, ISelection selection)
      {
        setWorkbenchPart(part);
        if (locationListener.setupEditor != null)
        {
          if (selection instanceof IStructuredSelection)
          {
            Object object = ((IStructuredSelection)selection).getFirstElement();
            if (object != null)
            {
              locationListener.setSelection(object);
            }
          }
        }
      }
    };

    private Browser browser;

    private StyledText noBrowser;

    protected BrowserDialog(IWorkbenchWindow workbenchWindow)
    {
      super(workbenchWindow);
    }

    @Override
    protected boolean handleWorkbenchPart(IWorkbenchPart part)
    {
      // Determines if the part corresponds to one that can show a setup editor's information via its selection.
      SetupEditor setupEditor = null;
      if (part instanceof SetupEditor)
      {
        setupEditor = (SetupEditor)part;
      }
      else if (part instanceof ContentOutline)
      {
        ContentOutline contentOutline = (ContentOutline)part;
        IPage page = contentOutline.getCurrentPage();
        if (page instanceof SetupEditor.OutlinePreviewPage)
        {
          SetupEditor.OutlinePreviewPage outlinePreviewPage = (OutlinePreviewPage)page;
          setupEditor = outlinePreviewPage.getSetupEditor();
        }
      }

      locationListener.setEditor(setupEditor);

      return setupEditor != null;
    }

    /**
     * Updates the contents based on the setup editor's input object.
     */
    protected void setInput(SetupEditor setupEditor, Object input)
    {
      setWorkbenchPart(setupEditor);

      // If the input is a URI...
      LocationEvent event = new LocationEvent(browser == null ? noBrowser : browser);
      if (input instanceof URI)
      {
        // Handle it just like we are following it as a link and set it into the browser as a URL.
        event.location = ((URI)input).toString();
        locationListener.changing(event);
        browser.setUrl(event.location);
      }
      else
      {
        // Otherwise it must be an appropriate object with an associated URI.
        URI uri = SetupActionBarContributor.getEditURI(ToolTipObject.unwrap(input), true);
        event.location = uri == null ? "about:blank" : uri.toString();
        locationListener.changing(event);

        // If there is no URI, treat it as if we were selected by navigating from a page.
        if (uri == null)
        {
          locationListener.setSelection(input);
        }
      }
    }

    @Override
    protected IDialogSettings getDialogBoundsSettings()
    {
      return SetupEditorPlugin.INSTANCE.getDialogSettings("Browser");
    }

    @Override
    protected Control createContents(Composite parent)
    {
      getShell().setText("Setup Information Browser");

      // Create this just like the column viewer tooltip information control so that the we can reuse the logic in the setup location listener.
      Composite composite = new Composite(parent, SWT.NONE);
      FillLayout layout = new FillLayout();
      composite.setLayout(layout);
      GridData layoutData = new GridData(GridData.FILL_BOTH);
      layoutData.widthHint = 800;
      layoutData.heightHint = 600;
      composite.setLayoutData(layoutData);
      applyDialogFont(composite);

      initializeDialogUnits(composite);

      Composite content;
      if (UIUtil.isBrowserAvailable())
      {
        content = browser = new Browser(composite, SWT.NONE);
        browser.addLocationListener(locationListener);
      }
      else
      {
        content = noBrowser = new StyledText(composite, SWT.V_SCROLL | SWT.H_SCROLL);
        noBrowser.setAlwaysShowScrollBars(false);
      }

      Display display = parent.getDisplay();
      content.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
      content.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));

      // Hook up the selection listener and be sure we clean it up when the browser is disposed.
      final ISelectionService selectionService = getWorkbenchWindow().getSelectionService();
      selectionService.addPostSelectionListener(selectionListener);
      content.addDisposeListener(new DisposeListener()
      {
        public void widgetDisposed(DisposeEvent e)
        {
          locationListener.dispose();
          selectionService.removePostSelectionListener(selectionListener);
        }
      });

      locationListener.createToolBar(browser, noBrowser);

      return composite;
    }

    /**
     * Returns the instance for this workbench window, if there is one.
     */
    public static BrowserDialog getFor(IWorkbenchWindow workbenchWindow)
    {
      return DockableDialog.getFor(BrowserDialog.class, workbenchWindow);
    }

    /**
     * Close the instance for this workbench window, if there is one.
     */
    public static void closeFor(IWorkbenchWindow workbenchWindow)
    {
      DockableDialog.closeFor(BrowserDialog.class, workbenchWindow);
    }

    /**
     * Reopen or create the instance for this workbench window.
     */
    public static BrowserDialog openFor(final IWorkbenchWindow workbenchWindow)
    {
      Factory<BrowserDialog> factory = new Factory<BrowserDialog>()
      {
        public BrowserDialog create(IWorkbenchWindow workbenchWindow)
        {
          return new BrowserDialog(workbenchWindow);
        }
      };

      return DockableDialog.openFor(BrowserDialog.class, factory, workbenchWindow);
    }
  }
}
