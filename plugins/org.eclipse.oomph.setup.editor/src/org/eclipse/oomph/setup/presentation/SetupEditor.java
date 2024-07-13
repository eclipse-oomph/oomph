/*
 * Copyright (c) 2014-2018 Eike Stepper (Loehne, Germany) and others.
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
import org.eclipse.oomph.base.BasePackage;
import org.eclipse.oomph.base.provider.AnnotationItemProvider;
import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.base.provider.BaseEditUtil.IconReflectiveItemProvider;
import org.eclipse.oomph.base.provider.BaseItemProviderAdapterFactory;
import org.eclipse.oomph.base.util.BaseResourceImpl;
import org.eclipse.oomph.edit.NoChildrenDelegatingWrapperItemProvider;
import org.eclipse.oomph.internal.setup.SetupPrompter;
import org.eclipse.oomph.internal.ui.FindAndReplaceTarget;
import org.eclipse.oomph.internal.ui.IRevertablePart;
import org.eclipse.oomph.internal.ui.OomphEditingDomain;
import org.eclipse.oomph.internal.ui.OomphPropertySheetPage;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.Argument;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.Configuration;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Macro;
import org.eclipse.oomph.setup.MacroTask;
import org.eclipse.oomph.setup.Parameter;
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
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl.AuthorizationHandler.Authorization;
import org.eclipse.oomph.setup.internal.core.util.ResourceMirror;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.p2.util.MarketPlaceListing;
import org.eclipse.oomph.setup.presentation.SetupActionBarContributor.ToggleViewerInputAction;
import org.eclipse.oomph.setup.provider.PreferenceTaskItemProvider;
import org.eclipse.oomph.setup.provider.SetupItemProviderAdapterFactory;
import org.eclipse.oomph.setup.ui.SetupEditorSupport;
import org.eclipse.oomph.setup.ui.SetupLabelProvider;
import org.eclipse.oomph.setup.ui.SetupTransferSupport;
import org.eclipse.oomph.setup.ui.ToolTipLabelProvider;
import org.eclipse.oomph.setup.ui.actions.ConfigureMarketPlaceListingAction;
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
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.edit.EMFEditPlugin;
import org.eclipse.emf.edit.command.AbstractOverrideableCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CopyCommand;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.command.DragAndDropFeedback;
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
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.IWrapperItemProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptorDecorator;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.eclipse.emf.edit.provider.resource.ResourceItemProvider;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceSetItemProvider;
import org.eclipse.emf.edit.ui.EMFEditUIPlugin;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedColorRegistry;
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
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.AuthenticationEvent;
import org.eclipse.swt.browser.AuthenticationListener;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
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
import org.eclipse.ui.IViewPart;
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
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
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
  private static final Object VARIABLE_GROUP_IMAGE = SetupEditorPlugin.INSTANCE.getImage("full/obj16/VariableGroup"); //$NON-NLS-1$

  private static final Pattern HEADER_PATTERN = Pattern.compile("(<h1)(>)"); //$NON-NLS-1$

  private static final Pattern IMAGE_PATTERN = Pattern.compile("(<img )(src=)"); //$NON-NLS-1$

  private static final URI COMPOSITE_OUTLINE_COLOR = URI.createURI("color://rgb/138/110/85"); //$NON-NLS-1$

  private static final Object UNDECLARED_VARIABLE_GROUP_IMAGE;

  static
  {
    List<Object> images = new ArrayList<>(2);
    images.add(VARIABLE_GROUP_IMAGE);
    images.add(EMFEditUIPlugin.INSTANCE.getImage("full/ovr16/error_ovr.gif")); //$NON-NLS-1$
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
  protected List<PropertySheetPage> propertySheetPages = new ArrayList<>();

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
  protected Collection<ISelectionChangedListener> selectionChangedListeners = new ArrayList<>();

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
    @Override
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

    @Override
    public void partBroughtToTop(IWorkbenchPart p)
    {
      // Ignore.
    }

    @Override
    public void partClosed(IWorkbenchPart p)
    {
      // Ignore.
    }

    @Override
    public void partDeactivated(IWorkbenchPart p)
    {
      // Ignore.
    }

    @Override
    public void partOpened(IWorkbenchPart p)
    {
      // Ignore.
    }
  };

  protected IWindowListener windowListener = new IWindowListener()
  {
    @Override
    public void windowOpened(IWorkbenchWindow window)
    {
    }

    @Override
    public void windowDeactivated(IWorkbenchWindow window)
    {
    }

    @Override
    public void windowClosed(IWorkbenchWindow window)
    {
    }

    @Override
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
  protected Collection<Resource> removedResources = new ArrayList<>();

  /**
   * Resources that have been changed since last activation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<Resource> changedResources = new ArrayList<>();

  /**
   * Resources that have been saved.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<Resource> savedResources = new ArrayList<>();

  /**
   * Map to store the diagnostic associated with a resource.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Map<Resource, Diagnostic> resourceToDiagnosticMap = new LinkedHashMap<>();

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
    protected boolean dispatching;

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
            dispatchUpdateProblemIndication();
            break;
          }
        }
      }
      else
      {
        super.notifyChanged(notification);
      }
    }

    protected void dispatchUpdateProblemIndication()
    {
      if (updateProblemIndication && !dispatching)
      {
        dispatching = true;
        getSite().getShell().getDisplay().asyncExec(new Runnable()
        {
          @Override
          public void run()
          {
            dispatching = false;
            updateProblemIndication();
          }
        });
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
      dispatchUpdateProblemIndication();
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
    @Override
    public void resourceChanged(IResourceChangeEvent event)
    {
      IResourceDelta delta = event.getDelta();
      try
      {
        class ResourceDeltaVisitor implements IResourceDeltaVisitor
        {
          protected ResourceSet resourceSet = editingDomain.getResourceSet();

          protected Collection<Resource> changedResources = new ArrayList<>();

          protected Collection<Resource> removedResources = new ArrayList<>();

          @Override
          public boolean visit(final IResourceDelta delta)
          {
            if (delta.getResource().getType() == IResource.FILE)
            {
              if (delta.getKind() == IResourceDelta.REMOVED || delta.getKind() == IResourceDelta.CHANGED)
              {
                // If this is a *.ecore resource, it will be demand loaded regardless of demandLoad == false.
                EList<Resource> resources = resourceSet.getResources();
                List<Resource> originalResources = new ArrayList<>(resources);
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
            @Override
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
            @Override
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

  private AtomicReference<ResourceMirror> resourceMirror;

  private final ItemProvider loadingResourceInput = new ItemProvider(Collections.singleton(new ItemProvider(Messages.SetupEditor_loadingResourceInput_text)));

  private final ItemProvider loadingResourceSetInput = new ItemProvider(
      Collections.singleton(new ItemProvider(Messages.SetupEditor_loadingResourceSetInput_text)));

  private DelegatingDialogSettings dialogSettings = new DelegatingDialogSettings();

  private IconReflectiveItemProvider reflectiveItemProvider;

  private Runnable reproxifier;

  protected SetupActionBarContributor.SetupWorkingSetsProvider workingSetsProvider = new SetupActionBarContributor.SetupWorkingSetsProvider();

  private AdapterFactoryContentProvider selectionViewerContentProvider;

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

  @Override
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
        final Set<IFile> files = new LinkedHashSet<>();

        for (Resource resource : resourceSet.getResources())
        {
          if (resource.isLoaded())
          {
            URI uri = resource.getURI();
            URI normalizedURI = uriConverter.normalize(uri);
            normalizedURI = SetupContext.resolve(normalizedURI);

            if (normalizedURI.isPlatformResource())
            {
              IFile file = EcorePlugin.getWorkspaceRoot().getFile(new Path(normalizedURI.toPlatformString(true)));
              if (!file.isSynchronized(1))
              {
                files.add(file);
              }
            }
            else if (normalizedURI.isFile())
            {
              if (!resource.getContents().isEmpty())
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
        }

        if (!files.isEmpty())
        {
          final AtomicBoolean needsRefresh = new AtomicBoolean();

          // Do the work within an operation because this is a long running activity that modifies the workbench.
          //
          IWorkspaceRunnable operation = new IWorkspaceRunnable()
          {
            @Override
            public void run(IProgressMonitor monitor) throws CoreException
            {
              for (IFile file : files)
              {
                if (monitor.isCanceled())
                {
                  return;
                }

                if (file.isSynchronized(1))
                {
                  needsRefresh.set(true);
                  file.refreshLocal(1, monitor);
                }
              }
            }
          };

          try
          {
            EcorePlugin.getWorkspaceRoot().getWorkspace().run(operation, new NullProgressMonitor());
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
          List<Resource> removedResources = new ArrayList<>(this.removedResources);
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
          // Only the first resource is modifiable, so if other resources are removed, we can just unload them, an ignore them from further dirty handling.
          List<Resource> changedResources = new ArrayList<>(this.changedResources);
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

            // Force updates such a revalidation and content out update.
            ReflectUtil.invokeMethod("notifyListeners", editingDomain.getCommandStack()); //$NON-NLS-1$
            selectionViewer.refresh();
          }
        }

        // Controlled resources will be explicitly marked as not read only.
        // But the call handleActivateGen will clear the map, so we need to ensure we restore the map, and only force recomputation for the primary resource.
        Map<Resource, Boolean> resourceToReadOnlyMap = editingDomain.getResourceToReadOnlyMap();
        Map<Resource, Boolean> resourceToReadOnlyMapCopy = new LinkedHashMap<>(resourceToReadOnlyMap);
        handleActivateGen();
        EList<Resource> resources = resourceSet.getResources();
        if (!resources.isEmpty())
        {
          resourceToReadOnlyMapCopy.remove(resources.get(0));
        }

        resourceToReadOnlyMap.putAll(resourceToReadOnlyMapCopy);
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
      ResourceSet resourceSet = editingDomain.getResourceSet();
      if (isDirty())
      {
        changedResources.addAll(resourceSet.getResources());
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
            resource.load(resourceSet.getLoadOptions());
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
      BasicDiagnostic diagnostic = new BasicDiagnostic(Diagnostic.OK, "org.eclipse.oomph.setup.editor", //$NON-NLS-1$
          0, null, new Object[] { editingDomain.getResourceSet() });
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
    return MessageDialog.openQuestion(getSite().getShell(), getString("_UI_FileConflict_label"), //$NON-NLS-1$
        getString("_WARN_FileConflict")); //$NON-NLS-1$
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
      @Override
      public void commandStackChanged(final EventObject event)
      {
        getContainer().getDisplay().asyncExec(new Runnable()
        {
          @Override
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
              if (propertySheetPage.getControl() == null || propertySheetPage.getControl().isDisposed())
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
    editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<>());
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
            List<EObject> contents = new ArrayList<>(resource.getContents());

            ResourceSet resourceSet = resource.getResourceSet();
            if (resourceSet != null && resourceSet.getResources().get(0) != resource)
            {
              if (children != null)
              {
                for (DelegatingWrapperItemProvider child : children)
                {
                  if (((EObject)child.getValue()).eIsProxy())
                  {
                    children = null;
                    break;
                  }
                }
              }

              if (children == null)
              {
                children = new ArrayList<>();
                int index = 0;
                for (EObject child : contents)
                {
                  children.add(new DelegatingWrapperItemProvider(child, resource, null, index++, resourceItemProviderAdapterFactory));
                }
              }

              return children;
            }

            return contents;
          }

          @Override
          public Object getImage(Object object)
          {
            Resource resource = (Resource)object;
            URI uri = resource.getURI();
            ResourceSet resourceSet = resource.getResourceSet();
            if (uri != null && resourceSet != null)
            {
              if (MarketPlaceListing.getMarketPlaceListing(uri, resourceSet.getURIConverter()) != null)
              {
                return SetupEditorPlugin.INSTANCE.getImage("marketplace16.png"); //$NON-NLS-1$
              }
            }

            return super.getImage(object);
          }

          @Override
          public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
          {
            if (itemPropertyDescriptors == null)
            {
              super.getPropertyDescriptors(object);
              itemPropertyDescriptors.add(new ItemPropertyDescriptor(resourceItemProviderAdapterFactory, Messages.SetupEditor_resolvedUriDescriptor_name,
                  Messages.SetupEditor_resolvedUriDescriptor_description, (EStructuralFeature)null, false)
              {
                @Override
                public Object getFeature(Object object)
                {
                  return "resolvedURI"; //$NON-NLS-1$
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

                @Override
                public boolean isPropertySet(Object object)
                {
                  return true;
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
              Collection<?> collection)
          {
            final List<CommandParameter> commandParameters = domain instanceof OomphEditingDomain
                ? new ArrayList<>(((OomphEditingDomain)domain).getCommandParameters())
                : Collections.<CommandParameter> emptyList();

            final Set<URI> uris = new LinkedHashSet<>();
            for (Object object : collection)
            {
              if (object instanceof URI)
              {
                URI uri = (URI)object;
                MarketPlaceListing marketPlaceListing = MarketPlaceListing.getMarketPlaceListing(uri, domain.getResourceSet().getURIConverter());
                uris.add(marketPlaceListing == null ? uri : marketPlaceListing.getListing());
              }
              else
              {
                return UnexecutableCommand.INSTANCE;
              }
            }

            final ResourceSet resourceSet = (ResourceSet)owner;
            class LoadResourceCommand extends AbstractOverrideableCommand implements AbstractCommand.NonDirtying, DragAndDropFeedback
            {
              protected List<Resource> resources;

              protected LoadResourceCommand(EditingDomain domain)
              {
                super(domain);
              }

              @Override
              protected boolean prepare()
              {
                return !uris.isEmpty();
              }

              @Override
              public void doExecute()
              {
                resources = new ArrayList<>();
                int index = 1;
                Macro macro = null;
                for (URI uri : uris)
                {
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
                    }
                  }

                  if (resource != null)
                  {
                    macro = (Macro)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.MACRO);

                    EList<Resource> resourceSetResources = resourceSet.getResources();
                    if (resourceSetResources.indexOf(resource) != 0)
                    {
                      resourceSetResources.move(index++, resource);
                    }

                    resources.add(resource);
                  }
                }

                if (macro != null && !commandParameters.isEmpty())
                {
                  CommandParameter commandParameter = commandParameters.get(commandParameters.size() - 1);
                  final Object feature = commandParameter.getFeature();
                  final Object rootOwner = commandParameter.getOwner();
                  final Macro finalMacro = macro;
                  UIUtil.asyncExec(getContainer(), new Runnable()
                  {
                    @Override
                    public void run()
                    {
                      // Drop the macro onto the original owner.
                      float location = 0.5f;
                      int operations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
                      int operation = DND.DROP_LINK;
                      if (feature instanceof DragAndDropCommand.Detail)
                      {
                        DragAndDropCommand.Detail detail = (DragAndDropCommand.Detail)feature;
                        location = detail.location;
                        operations = detail.operations;
                        operation = detail.operation;
                      }

                      Command dragAndDropCommand = DragAndDropCommand.create(domain, rootOwner, location, operations, operation,
                          Collections.singleton(finalMacro));
                      domain.getCommandStack().execute(dragAndDropCommand);
                    }
                  });
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
                return EMFEditPlugin.INSTANCE.getString("_UI_LoadResources_description"); //$NON-NLS-1$
              }

              @Override
              public String doGetLabel()
              {
                return EMFEditPlugin.INSTANCE.getString("_UI_LoadResources_label"); //$NON-NLS-1$
              }

              @Override
              public boolean validate(Object owner, float location, int operations, int operation, Collection<?> collection)
              {
                return true;
              }

              @Override
              public int getFeedback()
              {
                return FEEDBACK_SELECT;
              }

              @Override
              public int getOperation()
              {
                return DROP_COPY;
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
    Map<Resource, Boolean> readOnlyMap = new LinkedHashMap<>()
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

    editingDomain = new OomphEditingDomain(adapterFactory, editingDomain.getCommandStack(), readOnlyMap, SetupTransferSupport.USER_RESOLVING_DELEGATES)
    {
      @Override
      public void handledAdditions(final Collection<?> collection)
      {
        UIUtil.asyncExec(new Runnable()
        {
          @Override
          public void run()
          {
            for (Object object : collection)
            {
              if (object instanceof MacroTask)
              {
                MacroTask macroTask = (MacroTask)object;
                ConfigureMarketPlaceListingAction.configure(getSite().getShell(), editingDomain, macroTask);
              }
            }
          }
        });
      }
    };

    // Add a listener to set the most recent command's affected objects to be the selection of the viewer with focus.
    //
    editingDomain.getCommandStack().addCommandStackListener(new CommandStackListener()
    {
      @Override
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
        @Override
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
        List<Object> effectiveCollection = new ArrayList<>();
        boolean selectResource = false;
        for (Object object : collection)
        {
          if (object instanceof Resource)
          {
            selectResource = true;
            Object[] children = selectionViewerContentProvider.getChildren(object);
            if (children.length == 0)
            {
              effectiveCollection.add(object);
            }
            else
            {
              effectiveCollection.add(children[0]);
            }
          }
          else
          {
            effectiveCollection.add(object);
          }
        }

        if (selectResource)
        {
          toggleInput(true);
        }

        collection = effectiveCollection;
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
  @Override
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
      List<Point> result = new ArrayList<>();
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
          @Override
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
  @Override
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
    MenuManager contextMenu = new MenuManager("#PopUp") //$NON-NLS-1$
    {
      @Override
      @SuppressWarnings("restriction")
      protected boolean allowItem(IContributionItem itemToAdd)
      {
        String id = itemToAdd.getId();
        if (itemToAdd instanceof org.eclipse.ui.internal.PluginActionContributionItem)
        {
          // Hide these annoying actions.
          return id != null && !id.contains("debug.ui") && !"ValidationAction".equals(id) && !id.contains("mylyn"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        if (itemToAdd instanceof MenuManager)
        {
          // Hide all sub menus except our own.
          if (id != null)
          {
            if ("team.main".equals(id) || "replaceWithMenu".equals(id) || "compareWithMenu".equals(id)) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            {
              itemToAdd.setVisible(false);
            }
          }
        }

        return true;
      }
    };

    contextMenu.add(new Separator("additions")); //$NON-NLS-1$
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
    Map<URI, URI> workspaceMappings = new LinkedHashMap<>();
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
                URI redirectedWorkspaceURI = URI.createPlatformResourceURI(container.getFullPath().toString(), true).appendSegment(""); //$NON-NLS-1$
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
    resourceMirror.get().perform(resourceURI);

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

          final Map<EPackage, EPackage> packageProxies = new LinkedHashMap<>();

          {
            // Record a map from resolved EPackage to the original EPackage proxy.
            for (int i = 0, size = discoverablePackages.size(); i < size; ++i)
            {
              EPackage ePackageProxy = discoverablePackages.basicGet(i);
              EPackage ePackage = discoverablePackages.get(i);
              packageProxies.put(ePackage, ePackageProxy);
            }
          }

          @Override
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
            final Map<Object, Object> proxies = new LinkedHashMap<>();

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

            @Override
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

        resourceMirror.get().perform(SetupContext.INDEX_SETUP_URI);
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
      BasicDiagnostic basicDiagnostic = new BasicDiagnostic(hasErrors ? Diagnostic.ERROR : Diagnostic.WARNING, "org.eclipse.oomph.setup.editor", //$NON-NLS-1$
          0, getString("_UI_CreateModelError_message", resource.getURI()), //$NON-NLS-1$
          new Object[] { exception == null ? (Object)resource : exception });
      basicDiagnostic.merge(EcoreUtil.computeDiagnostic(resource, true));
      return basicDiagnostic;
    }
    else if (exception != null)
    {
      return new BasicDiagnostic(Diagnostic.ERROR, "org.eclipse.oomph.setup.editor", //$NON-NLS-1$
          0, getString("_UI_CreateModelError_message", resource.getURI()), //$NON-NLS-1$
          new Object[] { exception });
    }
    else
    {
      return Diagnostic.OK_INSTANCE;
    }
  }

  private void configure(ColumnViewer viewer, final Set<Resource> primaryResources, final Set<EObject> syntheticEObjects)
  {
    final SetupLocationListener locationListener = new SetupLocationListener(true);
    locationListener.setEditor(this);

    final ColumnViewerInformationControlToolTipSupport toolTipSupport = new ColumnViewerInformationControlToolTipSupport(viewer, locationListener);

    final AbstractHoverInformationControlManager hoverInformationControlManager = ReflectUtil.getValue("hoverInformationControlManager", toolTipSupport); //$NON-NLS-1$

    @SuppressWarnings("restriction")
    final org.eclipse.jface.internal.text.InformationControlReplacer informationControlReplacer = hoverInformationControlManager.getInternalAccessor()
        .getInformationControlReplacer();

    final IInformationControlCloser informationControlReplacerCloser = ReflectUtil.getValue("fInformationControlCloser", informationControlReplacer); //$NON-NLS-1$

    class Closer implements IInformationControlCloser, Listener
    {
      private IInformationControlCloser informationControlCloser;

      private Shell shell;

      public Closer(IInformationControlCloser informationControlCloser)
      {
        this.informationControlCloser = informationControlCloser;
      }

      @Override
      public void setSubjectControl(Control subject)
      {
        informationControlCloser.setSubjectControl(subject);
      }

      @Override
      public void setInformationControl(IInformationControl control)
      {
        if (shell != null)
        {
          shell.getDisplay().removeFilter(SWT.MouseEnter, this);
          shell.getDisplay().removeFilter(SWT.MouseMove, this);
          shell.getDisplay().removeFilter(SWT.MouseExit, this);
        }

        shell = control == null ? null : (Shell)ReflectUtil.getValue("fShell", control); //$NON-NLS-1$
        informationControlCloser.setInformationControl(control);
      }

      @Override
      public void start(Rectangle subjectArea)
      {
        informationControlCloser.start(subjectArea);
        shell.getDisplay().addFilter(SWT.MouseEnter, this);
        shell.getDisplay().addFilter(SWT.MouseMove, this);
        shell.getDisplay().addFilter(SWT.MouseExit, this);
      }

      @Override
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

      @Override
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
              ReflectUtil.invokeMethod("hideInformationControl", informationControlReplacer); //$NON-NLS-1$
            }
          }
        }
      }
    }

    ReflectUtil.setValue("fInformationControlCloser", informationControlReplacer, new Closer(informationControlReplacerCloser)); //$NON-NLS-1$

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
      protected void decorate(Map<Object, BasicDiagnostic> objects)
      {
        // Don't decorate the outline.
        if (primaryResources == null)
        {
          super.decorate(objects);
        }
      }

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
        if (diagnostic1.getCode() != diagnostic2.getCode() || diagnostic1.getSource() != diagnostic2.getSource()
            || diagnostic1.getSeverity() != diagnostic2.getSeverity() || !ObjectUtil.equals(diagnostic1.getMessage(), diagnostic2.getMessage()))
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
        if (result.indexOf("<h1>" + Messages.SetupEditor_closer_tooltip_problemsOnChildren + "</h1>\n") != index) //$NON-NLS-1$ //$NON-NLS-2$
        {
          result.insert(index, "<h1>" + Messages.SetupEditor_closer_tooltip_problems + "</h1>\n"); //$NON-NLS-1$ //$NON-NLS-2$
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

        result.append("<div style='word-break: break-all;'>"); //$NON-NLS-1$

        URI imageURI = ImageURIRegistry.INSTANCE.getImageURI(image);
        String labelText = setupLabelProvider.getText(object);
        if (!extend)
        {
          result.append("<a href='about:blank?extend' style='text-decoration: none; color: inherit;'>"); //$NON-NLS-1$
          result.append("<img style='padding-right: 2pt; margin-top: 2px; margin-bottom: -2pt;' src='"); //$NON-NLS-1$
          result.append(imageURI);
          result.append("'/><b>"); //$NON-NLS-1$
          result.append(DiagnosticDecorator.escapeContent(labelText));
          result.append("</b></a>"); //$NON-NLS-1$
        }
        else
        {
          EList<Object> path = new BasicEList<>();
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
            result.append("<div style='margin-left: ").append(indent).append("px;'>"); //$NON-NLS-1$ //$NON-NLS-2$
            ToolTipLabelProvider.renderHTMLPropertyValue(result, itemDelegator, element, true);
            result.append("</div>"); //$NON-NLS-1$

            indent += 10;
          }

          result.append("<div style='margin-left: ").append(indent).append("px;'>"); //$NON-NLS-1$ //$NON-NLS-2$
          result.append("<a href='about:blank?no-extend' style='text-decoration: none; color: inherit;'>"); //$NON-NLS-1$
          result.append("<img style='padding-right: 2pt; margin-top: 2px; margin-bottom: -2pt;' src='"); //$NON-NLS-1$
          result.append(imageURI);
          result.append("'/><b>"); //$NON-NLS-1$
          result.append(DiagnosticDecorator.escapeContent(labelText));
          result.append("</b></a>"); //$NON-NLS-1$
          result.append("</div>"); //$NON-NLS-1$

          indent += 10;

          for (Object child : itemDelegator.getChildren(object))
          {
            result.append("<div style='margin-left: ").append(indent).append("px;'>"); //$NON-NLS-1$ //$NON-NLS-2$
            ToolTipLabelProvider.renderHTMLPropertyValue(result, itemDelegator, child, true);
            result.append("</div>"); //$NON-NLS-1$
          }
        }

        result.append("</div>\n"); //$NON-NLS-1$

        List<IItemPropertyDescriptor> propertyDescriptors = new ArrayList<>();
        List<IItemPropertyDescriptor> underlyingPropertyDescriptors = itemDelegator.getPropertyDescriptors(object);
        if (underlyingPropertyDescriptors != null)
        {
          propertyDescriptors.addAll(underlyingPropertyDescriptors);
        }

        for (Iterator<IItemPropertyDescriptor> it = propertyDescriptors.iterator(); it.hasNext();)
        {
          IItemPropertyDescriptor itemPropertyDescriptor = it.next();
          String[] filterFlags = itemPropertyDescriptor.getFilterFlags(object);
          if (!showAdvancedProperties && filterFlags != null && filterFlags.length > 0 && "org.eclipse.ui.views.properties.expert".equals(filterFlags[0])) //$NON-NLS-1$
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
            if ("description".equals(eStructuralFeature.getName()) && propertyValue instanceof String) //$NON-NLS-1$
            {
              String description = propertyValue.toString();
              if (description != null)
              {
                result.append("<h1>" + Messages.SetupEditor_closer_tooltip_description + "</h1>"); //$NON-NLS-1$ //$NON-NLS-2$
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
            matcher.appendReplacement(improvedDiagnosticText, "$1" + "style='margin-bottom: -2pt;' " + "$2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          }

          matcher.appendTail(improvedDiagnosticText);

          result.append("\n").append(improvedDiagnosticText); //$NON-NLS-1$
        }

        String propertyTable = ToolTipLabelProvider.renderHTML(propertyDescriptors, object, true);
        if (propertyTable != null)
        {
          result.append("\n<h1>" + Messages.SetupEditor_closer_tooltip_properties + "</h1>\n"); //$NON-NLS-1$ //$NON-NLS-2$
          result.append('\n').append(propertyTable);
        }

        // Improve all the headers to add some nice spacing.
        StringBuffer improvedResult = new StringBuffer();
        Matcher matcher = HEADER_PATTERN.matcher(result);
        while (matcher.find())
        {
          matcher.appendReplacement(improvedResult, "$1" + " style='padding-bottom: 2pt; padding-top: 4pt;'" + "$2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        matcher.appendTail(improvedResult);

        String finalText = improvedResult.toString();

        try
        {
          AbstractHoverInformationControlManager hoverInformationControlManager = ReflectUtil.getValue("hoverInformationControlManager", toolTipSupport); //$NON-NLS-1$
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
          matcher.appendReplacement(improvedResult, "$1" + "style='padding-bottom: 1px;' " + "$2"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        }

        matcher.appendTail(improvedResult);
        result.append(improvedResult);
      }
    };

    // private static final URI FOREGROUND_COLOR = URI.createURI("color://rgb/85/113/138");

    final Color syntheticColor = ExtendedColorRegistry.INSTANCE.getColor(null, null, URI.createURI("color://rgb/85/113/138")); //$NON-NLS-1$
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
                builder.append(", "); //$NON-NLS-1$
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
              string = ""; //$NON-NLS-1$
            }
            else
            {
              string = ": " + string; //$NON-NLS-1$
            }

            text += "  [" + Messages.SetupEditor_viewer_restricted + string + "]"; //$NON-NLS-1$ //$NON-NLS-2$
          }
        }

        return text;
      }

      @Override
      public Color getForeground(Object element)
      {
        Color foreground = super.getForeground(element);
        if (foreground == null && syntheticEObjects != null && syntheticEObjects.contains(element))
        {
          foreground = syntheticColor;
        }

        return foreground;
      }

      @Override
      public Font getFont(Object object)
      {
        Font result = super.getFont(object);
        Object unwrappedObject = AdapterFactoryEditingDomain.unwrap(object);
        if (primaryResources != null && unwrappedObject instanceof EObject && !primaryResources.contains(((EObject)unwrappedObject).eResource()))
        {
          result = ExtendedFontRegistry.INSTANCE.getFont(result != null ? result : font, IItemFontProvider.ITALIC_FONT);
        }

        return result;
      }
    });

    final Control control = viewer.getControl();
    control.addMouseListener(new MouseListener()
    {
      @Override
      public void mouseDoubleClick(MouseEvent e)
      {
        try
        {
          IViewPart propertiesView = getSite().getPage().showView("org.eclipse.ui.views.PropertySheet", null, IWorkbenchPage.VIEW_VISIBLE); //$NON-NLS-1$
          if (propertiesView instanceof PropertySheet)
          {
            // If the properties view wasn't showing, but is present in a different perspective,
            // then it ends up being shown, but it doesn't show the current selection.
            // If we just set the selection, it thinks that's still the same selection so we must change it twice to ensure that the current selection is shown.
            PropertySheet propertySheet = (PropertySheet)propertiesView;
            propertySheet.selectionChanged(SetupEditor.this, new StructuredSelection());
            propertySheet.selectionChanged(SetupEditor.this, SetupEditor.this.getSelection());
          }
        }
        catch (PartInitException ex)
        {
          SetupEditorPlugin.INSTANCE.log(ex);
        }
      }

      @Override
      public void mouseDown(MouseEvent e)
      {
        // Do nothing
      }

      @Override
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

          ReflectUtil.setValue("currentCell", toolTipSupport, null); //$NON-NLS-1$

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

    configure(selectionViewer, null, null);

    selectionViewerContentProvider = new AdapterFactoryContentProvider(adapterFactory)
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
    };

    selectionViewer.setContentProvider(selectionViewerContentProvider);

    selectionViewer.setInput(loadingResourceInput);

    new AdapterFactoryTreeEditor(selectionViewer.getTree(), adapterFactory);

    int pageIndex = addPage(tree);

    setPageText(pageIndex, getString("_UI_SelectionPage_label")); //$NON-NLS-1$

    getSite().getShell().getDisplay().asyncExec(new Runnable()
    {
      @Override
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
    resourceMirror = new AtomicReference<>();
    final Tree tree = selectionViewer.getTree();
    Job job = new Job(Messages.SetupEditor_loadingModelJob_name)
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
            SetupEditor.this.resourceMirror.set(this);
            createModel();
            resolveProxies();
            dialogSettings.setLiveValidation(true);
            SetupEditor.this.resourceMirror = null;
          }
        };

        resourceMirror.begin(monitor);

        UIUtil.asyncExec(tree, new Runnable()
        {
          @Override
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
      setPageText(0, ""); //$NON-NLS-1$
      if (getContainer() instanceof CTabFolder)
      {
        Point point = getContainer().getSize();
        Rectangle clientArea = getContainer().getClientArea();
        getContainer().setSize(point.x, 2 * point.y - clientArea.height - clientArea.y);
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
      setPageText(0, getString("_UI_SelectionPage_label")); //$NON-NLS-1$
      if (getContainer() instanceof CTabFolder)
      {
        Point point = getContainer().getSize();
        Rectangle clientArea = getContainer().getClientArea();
        getContainer().setSize(point.x, clientArea.height + clientArea.y);
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
  @SuppressWarnings("all")
  public <T> T getAdapterGen(Class<T> key)
  {
    if (key.equals(IContentOutlinePage.class))
    {
      return showOutlineView() ? key.cast(getContentOutlinePage()) : null;
    }
    else if (key.equals(IPropertySheetPage.class))
    {
      return key.cast(getPropertySheetPage());
    }
    else if (key.equals(IGotoMarker.class))
    {
      return key.cast(this);
    }
    else
    {
      return super.getAdapter(key);
    }
  }

  @SuppressWarnings("all")
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

    private final Map<Object, Set<Object>> copyMap = new LinkedHashMap<>();

    private final Map<Object, Set<Object>> inverseCopyMap = new LinkedHashMap<>();

    private final Map<SetupTask, Set<VariableTask>> inducedIDVariables = new LinkedHashMap<>();

    private final List<Notifier> notifiers = new ArrayList<>();

    private final Set<EObject> syntheticObjects = new LinkedHashSet<>();

    private final Map<Object, Object> parents = new LinkedHashMap<>();

    private final List<Scope> previewableScopes = new UniqueEList<>();

    private final Set<Resource> primaryResources = new LinkedHashSet<>();

    private final Stream compositeStream = SetupFactory.eINSTANCE.createStream();

    private final Configuration compositeConfiguration = SetupFactory.eINSTANCE.createConfiguration();

    private final Macro compositeMacro = SetupFactory.eINSTANCE.createMacro();

    private OutlineItemProvider root;

    private AdapterFactoryEditingDomain.EditingDomainProvider editingDomainProvider = new AdapterFactoryEditingDomain.EditingDomainProvider(editingDomain);

    private ResourceLocator resourceLocator;

    private AdapterFactoryItemDelegator itemDelegator;

    private PreviewAction previewAction;

    public OutlinePreviewPage()
    {
      compositeStream.setLabel(Messages.SetupEditor_outlinePreviewPage_compositeStream_label);
      compositeStream.setName("composite"); //$NON-NLS-1$
      compositeStream.setDescription(Messages.SetupEditor_outlinePreviewPage_compositeStream_description);

      compositeConfiguration.setLabel(Messages.SetupEditor_outlinePreviewPage_compositeConfiguration_label);
      compositeConfiguration.setDescription(Messages.SetupEditor_outlinePreviewPage_compositeConfiguration_description);

      compositeMacro.setName("macros"); //$NON-NLS-1$
      compositeMacro.setLabel(Messages.SetupEditor_outlinePreviewPage_compositeMacro_label);
      compositeMacro.setDescription(Messages.SetupEditor_outlinePreviewPage_compositeMacro_description);
    }

    private class VariableContainer extends ItemProvider
    {
      private SetupTaskPerformer setupTaskPerformer;

      private Map<Pair<Object, Object>, IWrapperItemProvider> wrappers = new LinkedHashMap<>();

      public VariableContainer(SetupTaskPerformer setupTaskPerformer, String text, Object image)
      {
        super(text, image);
        this.setupTaskPerformer = setupTaskPerformer;
      }

      public SetupTaskPerformer getSetupTaskPerformer()
      {
        return setupTaskPerformer;
      }

      public IWrapperItemProvider wrapper(Object variable, Object child)
      {
        Pair<Object, Object> key = Pair.create(variable, child);
        IWrapperItemProvider wrapper = wrappers.get(key);
        if (wrapper == null)
        {
          wrapper = new NoChildrenDelegatingWrapperItemProvider(child, variable, SetupEditor.this.adapterFactory);
          add(inverseCopyMap, wrapper, child);
          wrappers.put(key, wrapper);
        }

        return wrapper;
      }
    }

    public List<Scope> getPreviewableScopes()
    {
      for (ListIterator<Scope> it = previewableScopes.listIterator(); it.hasNext();)
      {
        Scope scope = it.next();
        if (scope.eIsProxy())
        {
          EObject resolvedScope = EcoreUtil.resolve(scope, editingDomain.getResourceSet());
          if (!resolvedScope.eIsProxy() && resolvedScope instanceof Scope)
          {
            it.set((Scope)resolvedScope);
          }
          else
          {
            it.remove();
          }
        }
      }

      return previewableScopes;
    }

    @Override
    public void createControl(Composite parent)
    {
      super.createControl(parent);

      parent.addDisposeListener(new DisposeListener()
      {
        @Override
        public void widgetDisposed(DisposeEvent e)
        {
          contentOutlinePage = null;
        }
      });

      contentOutlineViewer = getTreeViewer();

      previewAction = new PreviewAction();

      contentOutlineViewer.addSelectionChangedListener(this);

      contentOutlineViewer.addDoubleClickListener(new IDoubleClickListener()
      {
        @Override
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
          return getVariableContainer(object) != null || super.hasChildren(object);
        }

        private VariableContainer getVariableContainer(Object object)
        {
          if (object instanceof VariableTask)
          {
            Object parent = parents.get(object);
            if (parent instanceof VariableContainer)
            {
              return (VariableContainer)parent;
            }
          }

          return null;
        }

        @Override
        public Object[] getChildren(Object object)
        {
          VariableContainer variableContainer = getVariableContainer(object);
          if (variableContainer != null)
          {
            VariableTask contextVariableTask = (VariableTask)object;
            String name = contextVariableTask.getName();
            if (!StringUtil.isEmpty(name))
            {
              boolean isMacro = false;
              List<Object> variableUsages = new UniqueEList<>();
              Set<Object> inverses = inverseCopyMap.get(object);
              if (inverses != null)
              {
                for (Object inverse : inverses)
                {
                  Object unwrappedInverse = AdapterFactoryEditingDomain.unwrap(inverse);
                  if (unwrappedInverse instanceof Parameter)
                  {
                    String actualName = ((Parameter)unwrappedInverse).getName();
                    Object unwrappedParent = AdapterFactoryEditingDomain.unwrap(selectionViewerContentProvider.getParent(inverse));
                    if (unwrappedParent instanceof Macro)
                    {
                      isMacro = true;
                      Macro macro = (Macro)unwrappedParent;
                      variableUsages.addAll(getMatchingUsages(variableContainer, object, macro, actualName));
                    }
                  }
                  else if (unwrappedInverse instanceof VariableTask)
                  {
                    VariableTask variableTask = (VariableTask)unwrappedInverse;
                    String actualName = variableTask.getName();
                    if (!name.equals(actualName))
                    {
                      EObject rootContainer = EcoreUtil.getRootContainer(variableTask);
                      if (rootContainer instanceof Macro)
                      {
                        isMacro = true;
                        variableUsages.addAll(getMatchingUsages(variableContainer, object, rootContainer, actualName));
                      }
                    }
                  }
                }
              }

              if (!isMacro)
              {
                variableUsages.addAll(getMatchingUsages(variableContainer, object, null, name));
              }

              return variableUsages.toArray();
            }
          }

          return super.getChildren(object);
        }

        private List<Object> getMatchingUsages(VariableContainer variableContainer, Object object, EObject context, String actualName)
        {
          List<Object> variableUsages = new ArrayList<>();
          SetupTaskPerformer setupTaskPerformer = variableContainer.getSetupTaskPerformer();
          for (Object original : copyMap.keySet())
          {
            Object unwrappedOriginal = AdapterFactoryEditingDomain.unwrap(original);
            if (unwrappedOriginal instanceof EObject)
            {
              EObject unwrappedOriginalEObject = (EObject)unwrappedOriginal;
              if (context == null)
              {
                Macro containingMacro = getContainingMacro(original);
                if (containingMacro != null && !setupTaskPerformer.getMacroCopyMap().containsKey(original))
                {
                  continue;
                }
              }
              else if (!EcoreUtil.isAncestor(context, unwrappedOriginalEObject))
              {
                continue;
              }

              if (SetupTaskPerformer.isVariableUsed(actualName, unwrappedOriginalEObject, false)
                  || SetupTaskPerformer.isFilterUsed(actualName, unwrappedOriginalEObject))
              {
                variableUsages.add(variableContainer.wrapper(object, original));
              }
            }
          }

          return variableUsages;
        }

        private Macro getContainingMacro(Object object)
        {
          if (object instanceof EObject)
          {
            for (EObject eContainer = ((EObject)object).eContainer(); eContainer != null; eContainer = eContainer.eContainer())
            {
              if (eContainer instanceof Macro)
              {
                return (Macro)eContainer;
              }
            }
          }

          return null;
        }

        @Override
        public Object getParent(Object object)
        {
          Object parent = parents.get(object);
          return parent != null ? parent : super.getParent(object);
        }
      };

      contentOutlineViewer.setContentProvider(contentProvider);

      configure(contentOutlineViewer, primaryResources, syntheticObjects);
      Menu menu = contentOutlineViewer.getControl().getMenu();
      IMenuManager menuManager = (IMenuManager)menu.getData(MenuManager.MANAGER_KEY);
      menuManager.addMenuListener(new IMenuListener()
      {
        @Override
        public void menuAboutToShow(IMenuManager manager)
        {
          manager.insertBefore("edit", previewAction); //$NON-NLS-1$
        }
      });

      labelProvider = (ILabelProvider)contentOutlineViewer.getLabelProvider();
      root = new OutlineItemProvider(null, false);

      selectionViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        @Override
        public void selectionChanged(SelectionChangedEvent event)
        {
          IStructuredSelection selection = (IStructuredSelection)event.getSelection();
          if (selectionViewer != null && selectionViewer.getControl().isFocusControl() && !selection.isEmpty() && contentOutlinePage != null)
          {
            ArrayList<Object> selectionList = new ArrayList<>();
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
          Object unwrappedObject = AdapterFactoryEditingDomain.unwrap(object);
          if (unwrappedObject instanceof CompoundTask || unwrappedObject instanceof Macro)
          {
            Collection<?> children = object instanceof ITreeItemContentProvider ? ((ITreeItemContentProvider)object).getChildren(object)
                : ((EObject)unwrappedObject).eContents();
            for (Object child : children)
            {
              collectSelection(selection, child);
            }
          }
          else
          {
            Set<Object> copies = contentOutlinePage.getCopies(object);
            for (Object copy : copies)
            {
              Object unwrappedCopy = AdapterFactoryEditingDomain.unwrap(copy);
              if (unwrappedCopy instanceof CompoundTask)
              {
                collectSelection(selection, copy);
              }
              else
              {
                selection.add(copy);
              }
            }
          }
        }
      });

      contentOutlineViewer.expandToLevel(2);
    }

    public Set<Object> getOriginals(Object object)
    {
      Set<Object> originals = inverseCopyMap.get(object);
      if (originals == null)
      {
        return copyMap.containsKey(object) ? Collections.singleton(object) : Collections.emptySet();
      }

      return originals;
    }

    public Set<Object> getCopies(Object object)
    {
      Set<Object> copies = copyMap.get(object);
      if (copies == null)
      {
        return inverseCopyMap.containsKey(object) ? Collections.singleton(object) : Collections.emptySet();

      }
      return copies;
    }

    private boolean isPreviewableScope(Object scope)
    {
      return scope instanceof Macro || scope instanceof Workspace || scope instanceof Installation || scope instanceof Stream || scope instanceof ProductVersion
          || scope instanceof User;
    }

    protected Index getIndex()
    {
      for (Resource resource : editingDomain.getResourceSet().getResources())
      {
        Index index = (Index)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.INDEX);
        if (index != null)
        {
          return index;
        }
      }

      return null;
    }

    public void update(int expandLevel)
    {
      if (labelProvider != null)
      {
        copyMap.clear();
        inverseCopyMap.clear();
        inducedIDVariables.clear();
        primaryResources.clear();
        syntheticObjects.clear();

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
            Resource resource = resources.get(0);
            primaryResources.add(resource);
            EList<EObject> contents = resource.getContents();
            if (!contents.isEmpty())
            {
              EObject rootEObject = contents.get(0);
              boolean hasEmptyInput = getTreeViewer().getInput() == null;
              if (hasEmptyInput)
              {
                if (isPreviewableScope(rootEObject))
                {
                  previewableScopes.add((Scope)rootEObject);
                }
                else if (rootEObject instanceof Project)
                {
                  previewableScopes.addAll(((Project)rootEObject).getStreams());
                }
                else if (rootEObject instanceof Product)
                {
                  previewableScopes.addAll(((Product)rootEObject).getVersions());
                }
              }

              List<Object> newRootChildren = new ArrayList<>();
              newRootChildren.add(reconcileOutline(root, null, rootEObject));
              if (rootEObject instanceof Workspace || rootEObject instanceof Installation || rootEObject instanceof Configuration)
              {
                Index index = getIndex();
                if (index != null)
                {
                  newRootChildren.add(reconcileOutline(root, null, index));
                }
              }

              OutlineItemProvider macroOutline = reconcileCompositeOutline(root, compositeMacro);
              if (macroOutline != null)
              {
                newRootChildren.add(macroOutline);
              }

              if (rootEObject instanceof Configuration || rootEObject instanceof Index)
              {
                OutlineItemProvider configurationOutline = reconcileCompositeOutline(root, compositeConfiguration);
                if (configurationOutline != null)
                {
                  newRootChildren.add(configurationOutline);
                }
              }

              ECollections.setEList(root.getChildren(), newRootChildren);

              if (hasEmptyInput)
              {
                getTreeViewer().setInput(root);
                if (!previewableScopes.isEmpty())
                {
                  Set<Object> elements = copyMap.get(previewableScopes.get(0));
                  if (elements != null)
                  {
                    for (Object object : elements)
                    {
                      if (object instanceof OutlineItemProvider)
                      {
                        getTreeViewer().setExpandedState(object, true);
                        break;
                      }
                    }
                  }
                }
              }
              else
              {
                getTreeViewer().refresh();
              }
            }
          }
        }
        catch (Exception ex)
        {
          SetupEditorPlugin.INSTANCE.log(ex);
        }

        // Invert the copy map.
        for (Map.Entry<Object, Set<Object>> entry : copyMap.entrySet())
        {
          Object original = entry.getKey();
          Set<Object> copies = entry.getValue();
          for (Object value : copies)
          {
            add(inverseCopyMap, value, original);
          }
        }

        // Map induced variables back to the task from which they are induced.
        for (Map.Entry<SetupTask, Set<VariableTask>> entry : inducedIDVariables.entrySet())
        {
          SetupTask setupTask = entry.getKey();
          Set<VariableTask> variables = entry.getValue();
          Set<Object> inverses = inverseCopyMap.get(setupTask);
          if (inverses != null)
          {
            for (VariableTask variable : variables)
            {
              addAll(inverseCopyMap, variable, inverses);
            }
          }
        }

        getTreeViewer().expandToLevel(expandLevel);
      }
    }

    private OutlineItemProvider reconcileCompositeOutline(OutlineItemProvider parent, EObject composite)
    {
      EClass eClass = composite.eClass();
      EList<Resource> resources = editingDomain.getResourceSet().getResources();
      OutlineItemProvider compositeOutline = null;
      List<OutlineItemProvider> newChildren = new ArrayList<>();
      for (int i = 1, size = resources.size(); i < size; ++i)
      {
        Resource otherResource = resources.get(i);
        EObject instance = (EObject)EcoreUtil.getObjectByType(otherResource.getContents(), eClass);
        if (instance != null)
        {
          if (compositeOutline == null)
          {
            compositeOutline = reconcileOutline(parent, null, composite);
          }

          newChildren.add(reconcileOutline(compositeOutline, null, instance));
        }
      }

      if (compositeOutline != null)
      {
        Collections.sort(newChildren, new Comparator<OutlineItemProvider>()
        {
          @Override
          public int compare(OutlineItemProvider o1, OutlineItemProvider o2)
          {
            return CommonPlugin.INSTANCE.getComparator().compare(o1.getText(), o2.getText());
          }
        });

        ECollections.setEList(compositeOutline.getChildren(), newChildren);
      }

      return compositeOutline;
    }

    private List<String> sortStrings(Collection<? extends String> strings)
    {
      EList<Pair<SegmentSequence, String>> pairs = new BasicEList<>();
      for (String string : strings)
      {
        pairs.add(new Pair<>(SegmentSequence.create(".", string), string)); //$NON-NLS-1$
      }

      @SuppressWarnings("unchecked")
      Pair<SegmentSequence, String>[] array = pairs.toArray(new Pair[pairs.size()]);
      return sort(array);
    }

    private List<VariableTask> sortVariables(Collection<? extends VariableTask> variables)
    {
      EList<Pair<SegmentSequence, VariableTask>> pairs = new BasicEList<>();
      for (VariableTask variable : variables)
      {
        pairs.add(new Pair<>(SegmentSequence.create(".", variable.getName()), variable)); //$NON-NLS-1$
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

        @Override
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

      List<T> result = new ArrayList<>(pairs.length);
      for (Pair<SegmentSequence, T> pair : pairs)
      {
        result.add(pair.getElement2());
      }

      return result;
    }

    private OutlineItemProvider reconcileOutline(OutlineItemProvider parentItemProvider, Scope context, EObject scope)
    {
      OutlineItemProvider outlineItemProvider = parentItemProvider.getOutlineItemProvider(scope);
      if (outlineItemProvider == null)
      {
        outlineItemProvider = new OutlineItemProvider(scope, context != null);
      }
      else
      {
        outlineItemProvider.update();
      }

      add(copyMap, scope, outlineItemProvider);

      List<Object> newChildren = new ArrayList<>();
      if (getPreviewableScopes().contains(scope))
      {
        newChildren.addAll(gatherSetupTasks(outlineItemProvider, context, (Scope)scope));
      }

      EClass eClass = scope.eClass();
      for (EReference eReference : eClass.getEAllContainments())
      {
        if (SetupPackage.Literals.SCOPE.isSuperTypeOf(eReference.getEReferenceType()))
        {
          if (eReference.isMany())
          {
            @SuppressWarnings("unchecked")
            List<Scope> scopes = (List<Scope>)scope.eGet(eReference);
            for (Scope childScope : scopes)
            {
              newChildren.add(reconcileOutline(outlineItemProvider, null, childScope));
            }
          }
          else
          {
            Scope childScope = (Scope)scope.eGet(eReference);
            if (childScope != null)
            {
              newChildren.add(reconcileOutline(outlineItemProvider, null, childScope));
            }
          }
        }
      }

      for (EReference eReference : eClass.getEAllReferences())
      {
        if (eReference == SetupPackage.Literals.WORKSPACE__STREAMS || eReference == SetupPackage.Literals.INSTALLATION__PRODUCT_VERSION)
        {
          if (eReference.isMany())
          {
            @SuppressWarnings("unchecked")
            List<Scope> scopes = (List<Scope>)scope.eGet(eReference);
            if (!scopes.isEmpty())
            {
              newChildren.add(reconcileOutline(outlineItemProvider, (Scope)scope, compositeStream));

              for (Scope childScope : scopes)
              {
                newChildren.add(reconcileOutline(outlineItemProvider, (Scope)scope, childScope));
              }
            }
          }
          else
          {
            Scope childScope = (Scope)scope.eGet(eReference);
            if (childScope != null)
            {
              newChildren.add(reconcileOutline(outlineItemProvider, (Scope)scope, childScope));
            }
          }
        }
      }

      ECollections.setEList(outlineItemProvider.getChildren(), newChildren);

      return outlineItemProvider;
    }

    private class OutlineItemProvider extends ItemProvider implements IWrapperItemProvider, IItemPropertySource
    {
      private final EObject eObject;

      private boolean qualified;

      public OutlineItemProvider(EObject eObject, boolean qualified)
      {
        super(qualified ? ((Scope)eObject).getQualifiedLabel() : labelProvider.getText(eObject), labelProvider.getImage(eObject));
        this.eObject = eObject;
        this.qualified = qualified;
      }

      public void update()
      {
        setText(qualified ? ((Scope)eObject).getQualifiedLabel() : labelProvider.getText(eObject));
      }

      public OutlineItemProvider getOutlineItemProvider(EObject eObject)
      {
        for (Object child : getChildren())
        {
          if (child instanceof OutlineItemProvider && ((OutlineItemProvider)child).eObject == eObject)
          {
            return (OutlineItemProvider)child;
          }
        }

        return null;
      }

      @Override
      public Object getFont(Object object)
      {
        if (eObject == compositeStream || eObject == compositeConfiguration || eObject == compositeMacro)
        {
          return ITALIC_FONT;
        }

        return super.getFont(object);
      }

      @Override
      public Object getForeground(Object object)
      {
        if (eObject == compositeStream || eObject == compositeConfiguration || eObject == compositeMacro)
        {
          return COMPOSITE_OUTLINE_COLOR;
        }

        return super.getForeground(object);
      }

      @Override
      public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object)
      {
        List<IItemPropertyDescriptor> descriptors = itemDelegator.getPropertyDescriptors(eObject);

        List<IItemPropertyDescriptor> result = new ArrayList<>();
        for (IItemPropertyDescriptor descriptor : descriptors)
        {
          result.add(new ItemPropertyDescriptorDecorator(eObject, descriptor));
        }

        return result;
      }

      @Override
      public IItemPropertyDescriptor getPropertyDescriptor(Object object, Object propertyID)
      {
        IItemPropertyDescriptor descriptor = itemDelegator.getPropertyDescriptor(eObject, propertyID);
        if (descriptor != null)
        {
          return new ItemPropertyDescriptorDecorator(eObject, descriptor);
        }

        return null;
      }

      @Override
      public Object getEditableValue(Object object)
      {
        return itemDelegator.getEditableValue(eObject);
      }

      @Override
      public Object getValue()
      {
        return eObject;
      }

      @Override
      public Object getOwner()
      {
        return getParent();
      }

      @Override
      public EStructuralFeature getFeature()
      {
        return null;
      }

      @Override
      public int getIndex()
      {
        return 0;
      }

      @Override
      public void setIndex(int index)
      {
      }
    }

    private List<Object> gatherSetupTasks(OutlineItemProvider container, Scope context, Scope scope)
    {
      List<Object> result = new ArrayList<>();
      ProductVersion version = scope instanceof ProductVersion ? (ProductVersion)scope : null;
      Stream stream = scope instanceof Stream ? (Stream)scope : null;
      Macro macro = scope instanceof Macro ? (Macro)scope : null;

      if (version == null)
      {
        EObject rootContainer = null;
        if (stream != null)
        {
          Project project = stream.getProject();
          if (project != null)
          {
            ProjectCatalog projectCatalog = project.getProjectCatalog();
            rootContainer = EcoreUtil.getRootContainer(projectCatalog);
          }
        }

        if (!(rootContainer instanceof Index))
        {
          Index index = getIndex();
          if (index != null)
          {
            rootContainer = index;
          }
        }

        if (rootContainer instanceof Index)
        {
          Index index = (Index)rootContainer;
          for (ProductCatalog productCatalog : index.getProductCatalogs())
          {
            // The first should be the self product catalog, so find the last version which should be the empty version.
            for (Product product : productCatalog.getProducts())
            {
              // This should be the self product, so find the last version which should be the empty version.
              for (ProductVersion productVersion : product.getVersions())
              {
                version = productVersion;
              }
            }

            break;
          }
        }

        if (version == null)
        {
          return result;
        }
      }

      SetupContext setupContext = SetupContext.create(version, stream);

      EcoreUtil.Copier copier = new EcoreUtil.Copier();
      if (scope instanceof Workspace)
      {
        Workspace workspaceCopy = (Workspace)copier.copy(scope);
        copier.copyReferences();
        workspaceCopy.getStreams().clear();

        Resource fakeWorkspaceResource = new BaseResourceImpl(scope.eResource().getURI());
        fakeWorkspaceResource.getContents().add(workspaceCopy);
        setupContext = SetupContext.create(setupContext.getInstallation(), workspaceCopy, setupContext.getUser());
      }
      else if (scope instanceof Installation)
      {
        Installation installationCopy = (Installation)copier.copy(scope);
        copier.copyReferences();
        installationCopy.setProductVersion(version);

        Resource fakeInstallationResource = new BaseResourceImpl(scope.eResource().getURI());
        fakeInstallationResource.getContents().add(installationCopy);
        setupContext = SetupContext.create(installationCopy, setupContext.getWorkspace(), setupContext.getUser());
      }
      else if (scope instanceof User)
      {
        User userCopy = (User)copier.copy(scope);
        copier.copyReferences();

        Resource fakeUserResource = new BaseResourceImpl(scope.eResource().getURI());
        fakeUserResource.getContents().add(userCopy);
        setupContext = SetupContext.create(setupContext.getInstallation(), setupContext.getWorkspace(), userCopy);
      }

      if (context instanceof Installation)
      {
        Installation installationCopy = (Installation)copier.copy(context);
        copier.copyReferences();
        installationCopy.setProductVersion(version);

        Resource fakeInstallationResource = new BaseResourceImpl(context.eResource().getURI());
        fakeInstallationResource.getContents().add(installationCopy);
        setupContext = SetupContext.create(installationCopy, setupContext.getWorkspace(), setupContext.getUser());
      }
      else if (context instanceof Workspace)
      {
        Installation installation = setupContext.getInstallation();
        Workspace workspaceCopy = (Workspace)copier.copy(context);
        if (stream == compositeStream)
        {
          EObject eContainer = context.eContainer();
          if (eContainer instanceof Configuration)
          {
            Configuration configuration = (Configuration)eContainer;
            Installation configurationInstallation = configuration.getInstallation();
            if (configurationInstallation != null)
            {
              Installation configurationInstallationCopy = (Installation)copier.copy(configurationInstallation);
              installation = configurationInstallationCopy;
              Resource fakeInstallationResource = new BaseResourceImpl(context.eResource().getURI());
              fakeInstallationResource.getContents().add(configurationInstallationCopy);
            }
          }

          copier.copyReferences();
        }
        else
        {
          copier.copyReferences();
          workspaceCopy.getStreams().clear();
          workspaceCopy.getStreams().add(stream);
        }

        Resource fakeWorkspaceResource = new BaseResourceImpl(context.eResource().getURI());
        fakeWorkspaceResource.getContents().add(workspaceCopy);
        setupContext = SetupContext.create(installation, workspaceCopy, setupContext.getUser());
      }

      MacroTask macroTask = null;
      List<VariableTask> macroTaskVariables = new ArrayList<>();
      if (macro != null)
      {
        Workspace workspace = setupContext.getWorkspace();
        macroTask = SetupFactory.eINSTANCE.createMacroTask();
        macroTask.setMacro(macro);
        workspace.getSetupTasks().add(macroTask);

        String id = macro.getName();
        macroTask.setID(StringUtil.isEmpty(id) ? "macro" : id); //$NON-NLS-1$
        EList<Parameter> parameters = macro.getParameters();
        EList<Argument> arguments = macroTask.getArguments();
        for (Parameter parameter : parameters)
        {
          VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
          String parameterName = parameter.getName();
          variable.setName(parameterName);
          workspace.getSetupTasks().add(variable);
          macroTaskVariables.add(variable);

          Argument argument = SetupFactory.eINSTANCE.createArgument();
          argument.setParameter(parameter);
          argument.setValue("${" + parameterName + "}"); //$NON-NLS-1$ //$NON-NLS-2$
          arguments.add(argument);
        }

        Resource resource = workspace.eResource();
        if (resource == null)
        {
          Resource fakeWorkspaceResource = new BaseResourceImpl(macro.eResource().getURI());
          fakeWorkspaceResource.getContents().add(workspace);
          resource = fakeWorkspaceResource;
        }

        primaryResources.add(resource);
      }

      ResourceSet resourceSet = getEditingDomain().getResourceSet();
      URIConverter uriConverter = resourceSet.getURIConverter();

      SetupTaskPerformer setupTaskPerformer;

      try
      {
        setupTaskPerformer = SetupTaskPerformer.create(uriConverter, SetupPrompter.OK, trigger, setupContext, false, true);
      }
      catch (Exception ex)
      {
        return result;
      }

      List<SetupTask> triggeredSetupTasks = new ArrayList<>(setupTaskPerformer.getTriggeredSetupTasks());

      if (!triggeredSetupTasks.isEmpty())
      {
        // Propagate the copied mappings to the performer's copy map.
        final Map<EObject, Set<EObject>> performerCopyMap = setupTaskPerformer.getCopyMap();
        for (Map.Entry<EObject, EObject> entry : copier.entrySet())
        {
          EObject key = entry.getKey();
          EObject value = entry.getValue();
          performerCopyMap.put(key, performerCopyMap.get(value));
        }

        URI baseURI = URI.createURI("performer:/" + scope.getQualifiedName()); //$NON-NLS-1$
        URI uri = baseURI.appendSegment("tasks.setup"); //$NON-NLS-1$
        URI scopeURI = context == null ? scope.eResource().getURI() : context.eResource().getURI();

        Resource fakeResource = new BaseResourceImpl(uri);
        fakeResource.eAdapters().add(editingDomainProvider);
        EList<EObject> fakeResourceContents = fakeResource.getContents();
        resourceLocator.map(uri, fakeResource);

        Set<EObject> eObjects = new LinkedHashSet<>();
        for (Set<EObject> copies : performerCopyMap.values())
        {
          eObjects.addAll(copies);
        }

        Map<EObject, Set<EObject>> performerMacroCopyMap = setupTaskPerformer.getMacroCopyMap();
        for (Set<EObject> copies : performerMacroCopyMap.values())
        {
          eObjects.addAll(copies);
        }

        for (EObject eObject : eObjects)
        {
          notifiers.add(eObject);

          Resource resource = ((InternalEObject)eObject).eDirectResource();
          if (resource != null && !resource.eAdapters().contains(editingDomainProvider))
          {
            resource.eAdapters().add(editingDomainProvider);
            notifiers.add(resource);

            URI originalURI = resource.getURI();
            URI newURI = baseURI.appendSegment(originalURI.scheme() + ":").appendSegments(originalURI.segments()); //$NON-NLS-1$
            resource.setURI(newURI);

            if (scopeURI.equals(originalURI))
            {
              primaryResources.add(resource);
            }

            resourceLocator.map(newURI, resource);
          }

          resource = eObject.eResource();
          if (resource == null || resource == fakeResource)
          {
            EClass eClass = eObject.eClass();
            EAttribute eIDAttribute = eClass.getEIDAttribute();
            if (eIDAttribute != null)
            {
              eObject.eUnset(eIDAttribute);
            }

            if (eObject.eContainer() == null)
            {
              fakeResourceContents.add(eObject);
            }

            syntheticObjects.add(eObject);
          }
        }

        ItemProvider undeclaredVariablesItem = new VariableContainer(setupTaskPerformer, Messages.SetupEditor_outlinePreviewPage_undeclaredVariables,
            UNDECLARED_VARIABLE_GROUP_IMAGE);
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
          result.add(undeclaredVariablesItem);
        }

        Map<String, VariableTask> variablesMap = new LinkedHashMap<>();

        ItemProvider unresolvedVariablesItem = new VariableContainer(setupTaskPerformer, Messages.SetupEditor_outlinePreviewPage_unresolvedVariables,
            VARIABLE_GROUP_IMAGE);
        EList<Object> unresolvedVariablesItemChildren = unresolvedVariablesItem.getChildren();
        List<VariableTask> unresolvedVariables = setupTaskPerformer.getUnresolvedVariables();
        for (VariableTask variable : sortVariables(unresolvedVariables))
        {
          if (variable.eContainer() == null && variable.eResource() == null)
          {
            fakeResourceContents.add(variable);
          }

          unresolvedVariablesItemChildren.add(variable);
          parents.put(variable, unresolvedVariablesItem);
          variablesMap.put(variable.getName(), variable);
        }

        if (!unresolvedVariablesItemChildren.isEmpty())
        {
          result.add(unresolvedVariablesItem);
        }

        ItemProvider resolvedVariablesItem = new VariableContainer(setupTaskPerformer, Messages.SetupEditor_outlinePreviewPage_resolvedVariables,
            VARIABLE_GROUP_IMAGE);
        EList<Object> resolvedVariablesItemChildren = resolvedVariablesItem.getChildren();
        List<VariableTask> resolvedVariables = setupTaskPerformer.getResolvedVariables();
        for (VariableTask variable : sortVariables(resolvedVariables))
        {
          if (variable.eContainer() == null && variable.eResource() == null)
          {
            fakeResourceContents.add(variable);
          }

          resolvedVariablesItemChildren.add(variable);
          parents.put(variable, resolvedVariablesItem);
          variablesMap.put(variable.getName(), variable);
        }

        if (!resolvedVariablesItemChildren.isEmpty())
        {
          result.add(resolvedVariablesItem);
        }

        result.addAll(triggeredSetupTasks);

        for (SetupTask setupTask : triggeredSetupTasks)
        {
          parents.put(setupTask, container);
        }

        // Establish a mapping from wrapper item providers for macro tasks to their underlying wrapped objects.
        Map<Object, Set<Object>> macroTaskMap = new LinkedHashMap<>();
        Map<Object, Set<Object>> argumentBindingMap = new LinkedHashMap<>();
        for (EObject eObject : performerCopyMap.keySet())
        {
          if (eObject instanceof MacroTask)
          {
            MacroTask originalMacroTask = (MacroTask)eObject;
            String id = originalMacroTask.getID();
            if (id != null)
            {
              if (originalMacroTask == macroTask)
              {
                // Start directly from the macro we are previewing.
                gatherMacroChild(macroTaskMap, argumentBindingMap, true, id, macro);
                for (VariableTask variableTask : macroTaskVariables)
                {
                  String name = variableTask.getName();
                  String qualifiedName = SetupTaskPerformer.createQualifiedName(macroTask.getID(), name);
                  Set<Object> argumentsVariables = macroTaskMap.get(qualifiedName);
                  if (argumentsVariables != null)
                  {
                    addAll(macroTaskMap, variableTask, argumentsVariables);
                  }
                }
              }
              else if (macroTask == null || !EcoreUtil.isAncestor(macroTask, originalMacroTask))
              {
                // The above guard ensures that we don't walk nested macro task children multiple times, but only once starting from the root.
                Object[] children = selectionViewerContentProvider.getChildren(originalMacroTask);
                gatherMacroChildren(macroTaskMap, argumentBindingMap, false, id, children);
              }
            }
          }
        }

        // Propagate the performer's macro copy map to the macro task map.
        for (Map.Entry<EObject, Set<EObject>> entry : performerMacroCopyMap.entrySet())
        {
          EObject original = entry.getKey();
          Set<Object> macroTaskMappings = macroTaskMap.get(original);
          if (macroTaskMappings != null)
          {
            Set<EObject> copies = entry.getValue();
            for (EObject copy : copies)
            {
              addAll(macroTaskMap, copy, macroTaskMappings);
            }
          }
        }

        // Copy over the performer's copy map into the overall copy map.
        for (Map.Entry<EObject, Set<EObject>> entry : performerCopyMap.entrySet())
        {
          EObject original = entry.getKey();
          Set<EObject> copies = entry.getValue();
          addAll(copyMap, original, copies);

          Set<Object> macroTaskMappings = macroTaskMap.get(original);
          if (macroTaskMappings != null)
          {
            // Include any mappings from the macro task map.
            for (Object object : macroTaskMappings)
            {
              addAll(copyMap, object, copies);
            }
          }

          if (original instanceof VariableTask)
          {
            VariableTask variableTask = (VariableTask)original;
            String name = variableTask.getName();
            Set<Object> variableMappings = macroTaskMap.get(name);
            if (variableMappings != null)
            {
              // Include mappings from the parameter wrappers to the copies of the induced parameter variables.
              for (Object object : variableMappings)
              {
                addAll(copyMap, object, copies);
              }
            }
          }
        }

        // Copy the performer's macro copy map to the overall copy map.
        for (Map.Entry<EObject, Set<EObject>> entry : performerMacroCopyMap.entrySet())
        {
          EObject key = entry.getKey();
          Set<EObject> copies = entry.getValue();
          addAll(copyMap, key, copies);

          for (EObject eObject : copies)
          {
            // Also propagate any subsequent copies to flatten the map's indirections.
            Set<Object> otherCopies = copyMap.get(eObject);
            if (otherCopies != null)
            {
              addAll(copyMap, key, otherCopies);
            }
          }
        }

        // Copy over all the argument bindings to the overall copy map.
        for (Map.Entry<Object, Set<Object>> entry : argumentBindingMap.entrySet())
        {
          Object argument = entry.getKey();
          Set<Object> bindings = entry.getValue();
          for (Object binding : bindings)
          {
            Set<Object> copiedBindings = copyMap.get(binding);
            if (copiedBindings != null)
            {
              for (Object copiedBinding : copiedBindings)
              {
                add(copyMap, argument, AdapterFactoryEditingDomain.unwrap(copiedBinding));
              }
            }
          }
        }

        for (SetupTask setupTask : triggeredSetupTasks)
        {
          String id = setupTask.getID();
          if (!StringUtil.isEmpty(id))
          {
            for (EAttribute eAttribute : setupTask.eClass().getEAllAttributes())
            {
              if (eAttribute != SetupPackage.Literals.SETUP_TASK__ID && !eAttribute.isMany() && eAttribute.getEType().getInstanceClass() == String.class)
              {
                String variableName = id + "." + ExtendedMetaData.INSTANCE.getName(eAttribute); //$NON-NLS-1$
                VariableTask variable = variablesMap.get(variableName);
                if (variable != null)
                {
                  add(inducedIDVariables, setupTask, variable);
                }
              }
            }
          }
        }
      }

      return result;
    }

    private void gatherMacroChildren(Map<Object, Set<Object>> macroTaskMap, Map<Object, Set<Object>> argumentBindingMap, boolean handleParameters, String id,
        Object children[])
    {
      for (Object child : children)
      {
        gatherMacroChild(macroTaskMap, argumentBindingMap, handleParameters, id, child);
      }
    }

    private void gatherMacroChild(Map<Object, Set<Object>> macroTaskMap, Map<Object, Set<Object>> argumentBindingMap, boolean handleParameters, String id,
        Object object)
    {
      Object unwrappedObject = AdapterFactoryEditingDomain.unwrap(object);
      if (unwrappedObject instanceof EObject)
      {
        EObject eObject = (EObject)unwrappedObject;
        if (eObject.eResource() != null)
        {
          add(macroTaskMap, eObject, object);

          Object[] children = selectionViewerContentProvider.getChildren(object);
          if (eObject instanceof MacroTask)
          {
            MacroTask macroTask = (MacroTask)eObject;
            String macroTaskID = macroTask.getID();
            if (macroTaskID != null)
            {
              String qualifiedTaskID = id == null ? macroTaskID : id + "*" + macroTaskID; //$NON-NLS-1$
              gatherMacroChildren(macroTaskMap, argumentBindingMap, false, qualifiedTaskID, children);
            }
          }
          else if (eObject instanceof Parameter)
          {
            Parameter parameter = (Parameter)eObject;
            String parameterName = parameter.getName();
            String qualifiedParameterName = SetupTaskPerformer.createQualifiedName(id, parameterName);
            Set<Object> argumentBindings = macroTaskMap.get(qualifiedParameterName);
            if (argumentBindings != null)
            {
              addAll(argumentBindingMap, object, argumentBindings);
            }

            add(macroTaskMap, qualifiedParameterName, parameter);
            gatherMacroChildren(macroTaskMap, argumentBindingMap, false, id, children);
          }
          else if (eObject instanceof Argument)
          {
            Argument argument = (Argument)eObject;
            Parameter parameter = argument.getParameter();
            if (parameter != null)
            {
              String parameterName = parameter.getName();
              String qualifiedParameterName = SetupTaskPerformer.createQualifiedName(id, parameterName);
              add(macroTaskMap, qualifiedParameterName, argument);
            }
            gatherMacroChildren(macroTaskMap, argumentBindingMap, false, id, children);
          }
          else
          {
            gatherMacroChildren(macroTaskMap, argumentBindingMap, eObject instanceof Macro, id, children);
          }
        }
      }
    }

    private <K, V> void add(Map<K, Set<V>> map, K key, V value)
    {
      Set<V> set = map.get(key);
      if (set == null)
      {
        set = new LinkedHashSet<>();
        map.put(key, set);
      }

      set.add(value);
    }

    private <K, V> void addAll(Map<K, Set<V>> map, K key, Set<? extends V> values)
    {
      Set<V> set = map.get(key);
      if (set == null)
      {
        set = new LinkedHashSet<>();
        map.put(key, set);
      }

      set.addAll(values);
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

      IToolBarManager toolBarManager = actionBars.getToolBarManager();

      toolBarManager.add(previewAction);

      toolBarManager.add(new Action(Messages.SetupEditor_outlinePreviewPage_action_showTasksForAllTriggers, IAction.AS_RADIO_BUTTON)
      {
        {
          setChecked(true);
          setImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(SetupEditorPlugin.INSTANCE.getImage("AllTrigger"))); //$NON-NLS-1$
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
        toolBarManager.add(new Action(NLS.bind(Messages.SetupEditor_outlinePreviewPage_action_showTasksForTrigger, label), IAction.AS_RADIO_BUTTON)
        {
          {
            setImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(SetupEditorPlugin.INSTANCE.getImage(StringUtil.cap(label) + "Trigger"))); //$NON-NLS-1$
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

    @Override
    @SuppressWarnings("all")
    public Object getAdapter(Class adapter)
    {
      return FindAndReplaceTarget.getAdapter(adapter, SetupEditor.this);
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

    protected class PreviewAction extends Action implements ISelectionChangedListener
    {
      final List<Scope> selectedPeviewableScopes = new ArrayList<>();

      final Map<Scope, Object> scopes = new LinkedHashMap<>();

      public PreviewAction()
      {
        super(Messages.SetupEditor_outlinePreviewPage_action_previewTriggeredTasks, IAction.AS_CHECK_BOX);
        setImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(SetupEditorPlugin.INSTANCE.getImage("preview.png"))); //$NON-NLS-1$
        contentOutlineViewer.addPostSelectionChangedListener(this);
      }

      @Override
      public void selectionChanged(SelectionChangedEvent event)
      {
        update((ITreeSelection)event.getSelection());
      }

      protected void update()
      {
        update((ITreeSelection)contentOutlineViewer.getSelection());
      }

      protected void update(ITreeSelection treeSelection)
      {
        selectedPeviewableScopes.clear();
        scopes.clear();

        for (TreePath treePath : treeSelection.getPaths())
        {
          Object object = treePath.getLastSegment();
          Object unwrappedObject = AdapterFactoryEditingDomain.unwrap(object);
          if (isPreviewableScope(unwrappedObject))
          {
            Scope scope = (Scope)unwrappedObject;
            selectedPeviewableScopes.add(scope);
            scopes.put(scope, treePath);
          }
        }

        if (selectedPeviewableScopes.isEmpty())
        {
          setEnabled(false);
          setChecked(false);
        }
        else
        {
          List<Scope> previewableScopes = getPreviewableScopes();
          boolean disjoint = Collections.disjoint(selectedPeviewableScopes, previewableScopes);
          setEnabled(true);
          setChecked(!disjoint);
        }
      }

      @Override
      public void run()
      {
        List<Scope> previewableScopes = getPreviewableScopes();
        if (!previewableScopes.containsAll(selectedPeviewableScopes))
        {
          previewableScopes.addAll(selectedPeviewableScopes);
          OutlinePreviewPage.this.update(2);

          for (Scope scope : selectedPeviewableScopes)
          {
            contentOutlineViewer.setExpandedState(scopes.get(scope), true);
          }
        }
        else
        {
          previewableScopes.removeAll(selectedPeviewableScopes);
          OutlinePreviewPage.this.update(2);
        }
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
        @Override
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
        @Override
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
    if (contentOutlinePage != null && contentOutlineViewer.getControl().isFocusControl() && selectionViewer != null && !selection.isEmpty()
        && selection instanceof IStructuredSelection)
    {
      Iterator<?> selectedElements = ((IStructuredSelection)selection).iterator();
      if (selectedElements.hasNext())
      {
        Object selectedElement = selectedElements.next();

        List<Object> selectionList = new ArrayList<>();
        selectionList.addAll(contentOutlinePage.getOriginals(selectedElement));
        while (selectedElements.hasNext())
        {
          selectionList.addAll(contentOutlinePage.getOriginals(selectedElements.next()));
        }

        if (!selectionList.isEmpty())
        {
          TreeViewer oldSectionViewer = selectionViewer;
          IStructuredSelection structuredSelection = createSelection(selectionList);
          selectionViewer = null;
          oldSectionViewer.setSelection(structuredSelection, true);
          selectionViewer = oldSectionViewer;
        }
      }
    }
  }

  protected IStructuredSelection createSelection(List<Object> objects)
  {
    // If we're only showing the one resource we can just use a structured selection.
    Object input = selectionViewer.getInput();
    if (input instanceof Resource)
    {
      return new StructuredSelection(objects);
    }

    // All but the first resource use delegating wrappers for children, so we need to find those wrappers and create a tree path based on those.
    List<TreePath> treePaths = new ArrayList<>();
    ITreeContentProvider treeContentProvider = (ITreeContentProvider)selectionViewer.getContentProvider();
    for (Object object : objects)
    {
      List<Object> path = new ArrayList<>();
      computePath(path, treeContentProvider, object);
      treePaths.add(new TreePath(path.toArray()));
    }

    return new TreeSelection(treePaths.toArray(new TreePath[treePaths.size()]));
  }

  protected Object computePath(List<Object> path, ITreeContentProvider treeContentProvider, Object object)
  {
    if (object instanceof Resource)
    {
      path.add(object);
      return object;
    }

    Object parent = treeContentProvider.getParent(object);
    if (parent == null)
    {
      // This is a root object, so we can't do anything else with it.
      path.add(object);
      return object;
    }

    // Compute the past recursively for the parent, using the wrapper for the parent when determining children.
    Object actualParent = computePath(path, treeContentProvider, parent);
    if (actualParent != null)
    {
      Object[] children = treeContentProvider.getChildren(actualParent);
      for (Object child : children)
      {
        // Find the corresponding wrapper, add it to the path, and return it.
        if (child == object || child instanceof IWrapperItemProvider && ((IWrapperItemProvider)child).getValue() == object)
        {
          path.add(child);
          return child;
        }
      }
    }

    return null;
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

  @Override
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
    final Map<Object, Object> saveOptions = new LinkedHashMap<>();
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
  protected void doSaveAsGen(URI uri, IEditorInput editorInput)
  {
    editingDomain.getResourceSet().getResources().get(0).setURI(uri);
    setInputWithNotify(editorInput);
    setPartName(editorInput.getName());
    IProgressMonitor progressMonitor = getActionBars().getStatusLineManager() != null ? getActionBars().getStatusLineManager().getProgressMonitor()
        : new NullProgressMonitor();
    doSave(progressMonitor);
  }

  protected void doSaveAs(URI uri, IEditorInput editorInput)
  {
    editingDomain.getResourceToReadOnlyMap().remove(editingDomain.getResourceSet().getResources().get(0));
    doSaveAsGen(uri, editorInput);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
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
    Object[] selection = ((IStructuredSelection)selectionViewer.getSelection()).toArray();
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
      getActionBarContributor().getToggleViewerInputAction().setChecked(false);
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
      getActionBarContributor().getToggleViewerInputAction().setChecked(true);
    }
    else if (input == loadingResourceSetInput)
    {
      selectionViewer.setInput(loadingResourceInput);
    }

    selectionViewer.setExpandedElements(expandedElements);
    selectionViewer.setSelection(new StructuredSelection(selection));
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
      @Override
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
  @Override
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
  @Override
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
  @Override
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
  @Override
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
            statusLineManager.setMessage(getString("_UI_NoObjectSelected")); //$NON-NLS-1$
            break;
          }
          case 1:
          {
            String text = new AdapterFactoryItemDelegator(adapterFactory).getText(collection.iterator().next());
            statusLineManager.setMessage(getString("_UI_SingleObjectSelected", text)); //$NON-NLS-1$
            break;
          }
          default:
          {
            statusLineManager.setMessage(getString("_UI_MultiObjectSelected", Integer.toString(collection.size()))); //$NON-NLS-1$
            break;
          }
        }
      }
      else
      {
        statusLineManager.setMessage(""); //$NON-NLS-1$
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
  @Override
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

    @Override
    public IDialogSettings addNewSection(String name)
    {
      return dialogSettings.addNewSection(name);
    }

    @Override
    public void addSection(IDialogSettings section)
    {
      dialogSettings.addSection(section);
    }

    @Override
    public String get(String key)
    {
      return dialogSettings.get(key);
    }

    @Override
    public String[] getArray(String key)
    {
      return dialogSettings.getArray(key);
    }

    @Override
    public boolean getBoolean(String key)
    {
      if (DiagnosticDecorator.LiveValidator.LiveValidationAction.LIVE_VALIDATOR_DIALOG_SETTINGS_KEY.equals(key) && !liveValidation)
      {
        return false;
      }

      return dialogSettings.getBoolean(key);
    }

    @Override
    public double getDouble(String key) throws NumberFormatException
    {
      return dialogSettings.getDouble(key);
    }

    @Override
    public float getFloat(String key) throws NumberFormatException
    {
      return dialogSettings.getFloat(key);
    }

    @Override
    public int getInt(String key) throws NumberFormatException
    {
      return dialogSettings.getInt(key);
    }

    @Override
    public long getLong(String key) throws NumberFormatException
    {
      return dialogSettings.getLong(key);
    }

    @Override
    public String getName()
    {
      return dialogSettings.getName();
    }

    @Override
    public IDialogSettings getSection(String sectionName)
    {
      return dialogSettings.getSection(sectionName);
    }

    @Override
    public IDialogSettings[] getSections()
    {
      return dialogSettings.getSections();
    }

    @Override
    public void load(Reader reader) throws IOException
    {
      dialogSettings.load(reader);
    }

    @Override
    public void load(String fileName) throws IOException
    {
      dialogSettings.load(fileName);
    }

    @Override
    public void put(String key, String[] value)
    {
      dialogSettings.put(key, value);
    }

    @Override
    public void put(String key, double value)
    {
      dialogSettings.put(key, value);
    }

    @Override
    public void put(String key, float value)
    {
      dialogSettings.put(key, value);
    }

    @Override
    public void put(String key, int value)
    {
      dialogSettings.put(key, value);
    }

    @Override
    public void put(String key, long value)
    {
      dialogSettings.put(key, value);
    }

    @Override
    public void put(String key, String value)
    {
      dialogSettings.put(key, value);
    }

    @Override
    public void put(String key, boolean value)
    {
      dialogSettings.put(key, value);
    }

    @Override
    public void save(Writer writer) throws IOException
    {
      dialogSettings.save(writer);
    }

    @Override
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

    private ToolItem showSetupItem;

    private ToolItem showToolTipsItem;

    private ToolItem liveValidationItem;

    private ToolTipObject toolTipObject;

    private List<ToolTipObject> toolTipObjects = new ArrayList<>();

    private int toolTipIndex = -1;

    private ToolItem backwardItem;

    private ToolItem forwardItem;

    private ToolItem showAdvancedPropertiesItem;

    private SetupEditor setupEditor;

    private boolean editorSpecific;

    private final Map<SetupEditor, DisposeListener> editorDisposeListeners = new LinkedHashMap<>();

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
      ReflectUtil.setValue("replacementInformationControlCreator", toolTipSupport, new AbstractReusableInformationControlCreator() //$NON-NLS-1$
      {
        @Override
        @SuppressWarnings("restriction")
        protected IInformationControl doCreateInformationControl(Shell parent)
        {
          IInformationControl informationControl;
          if (org.eclipse.jface.internal.text.html.BrowserInformationControl.isAvailable(parent))
          {
            String symbolicFont = ReflectUtil.invokeMethod("getSymbolicFont", toolTipSupport); //$NON-NLS-1$
            org.eclipse.jface.internal.text.html.BrowserInformationControl browserInformationControl = new org.eclipse.jface.internal.text.html.BrowserInformationControl(
                parent, symbolicFont, true)
            {
              @Override
              protected void createContent(Composite parent)
              {
                super.createContent(parent);
                content = browser = ReflectUtil.getValue("fBrowser", this); //$NON-NLS-1$
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
                content = noBrowser = ReflectUtil.getValue("fText", this); //$NON-NLS-1$
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

          Color foregroundColor = ReflectUtil.getValue("foregroundColor", toolTipSupport); //$NON-NLS-1$
          if (foregroundColor != null)
          {
            informationControl.setForegroundColor(foregroundColor);
          }

          Color backgroundColor = ReflectUtil.getValue("backgroundColor", toolTipSupport); //$NON-NLS-1$
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
          @Override
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

            List<ItemProvider> items = new ArrayList<>();
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
                menuItem.setText(Messages.SetupEditor_setupLocationListener_menu_forwardMore + " (" + count + ')'); //$NON-NLS-1$
                menuItem.setImage(SetupEditorPlugin.INSTANCE.getSWTImage("forward")); //$NON-NLS-1$
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
              menuItem.setText(Messages.SetupEditor_setupLocationListener_menu_backMore + " (" + count + ')'); //$NON-NLS-1$
              menuItem.setImage(SetupEditorPlugin.INSTANCE.getSWTImage("backward")); //$NON-NLS-1$
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

      backwardItem = createItem(SWT.DROP_DOWN, "backward", Messages.SetupEditor_setupLocationListener_backwardItem_tooltip, new NavigationListener(false)); //$NON-NLS-1$

      forwardItem = createItem(SWT.DROP_DOWN, "forward", Messages.SetupEditor_setupLocationListener_forwardItem_tooltip, new NavigationListener(true)); //$NON-NLS-1$

      new ToolItem(toolBar, SWT.SEPARATOR);

      if (editorSpecific)
      {
        showSetupItem = createItem(SWT.PUSH, "locate_value", Messages.SetupEditor_setupLocationListener_showSetupItem_tooltip, new SelectionAdapter() //$NON-NLS-1$
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            Object unwrappedObject = ToolTipObject.unwrap(toolTipObject);
            Object selectionViewerInput = setupEditor.selectionViewer.getInput();
            if (selectionViewerInput instanceof Resource)
            {
              if (unwrappedObject instanceof EObject)
              {
                EObject eObject = (EObject)unwrappedObject;
                Resource resource = eObject.eResource();
                if (resource != selectionViewerInput)
                {
                  setupEditor.toggleInput(true);
                }
              }
            }

            IStructuredSelection structuredSelection = setupEditor.createSelection(Collections.singletonList(unwrappedObject));
            setupEditor.selectionViewer.setSelection(structuredSelection, true);
            setupEditor.selectionViewer.getControl().setFocus();
          }
        });
      }

      editSetupItem = createItem(SWT.PUSH, "edit_setup", Messages.SetupEditor_setupLocationListener_editSetupItem_tooltip, new SelectionAdapter() //$NON-NLS-1$
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          Object unwrappedObject = ToolTipObject.unwrap(toolTipObject);
          setupEditor.getActionBarContributor().openInSetupEditor(unwrappedObject);
        }
      });

      editTextItem = createItem(SWT.PUSH, "edit_text", Messages.SetupEditor_setupLocationListener_editTextItem_tooltip, new SelectionAdapter() //$NON-NLS-1$
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          Object unwrappedObject = ToolTipObject.unwrap(toolTipObject);
          setupEditor.getActionBarContributor().openInTextEditor(unwrappedObject);
        }
      });

      showAdvancedPropertiesItem = createItem(SWT.CHECK, "filter_advanced_properties", //$NON-NLS-1$
          Messages.SetupEditor_setupLocationListener_showAdvancedPropertiesItem_tooltip, new SelectionAdapter()
          {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
              navigate(toolTipIndex);
            }
          });

      if (editorSpecific)
      {
        createItem(SWT.PUSH, "show_properties_view", Messages.SetupEditor_setupLocationListener_showPropertiesViewItem_tooltip, new SelectionAdapter() //$NON-NLS-1$
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            try
            {
              setupEditor.getSite().getWorkbenchWindow().getActivePage().showView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
            }
            catch (PartInitException ex)
            {
              SetupEditorPlugin.INSTANCE.log(ex);
            }
          }
        });

        createItem(SWT.PUSH, "open_browser", Messages.SetupEditor_setupLocationListener_openInfoBrowserItem_tooltip, new SelectionAdapter() //$NON-NLS-1$
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            setupEditor.getActionBarContributor().showInformationBrowser(ToolTipObject.unwrap(toolTipObject));
          }
        });

        new ToolItem(toolBar, SWT.SEPARATOR);

        showToolTipsItem = createItem(SWT.CHECK, "show_tooltips", Messages.SetupEditor_setupLocationListener_showToolTipsItem_tooltip, new SelectionAdapter() //$NON-NLS-1$
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            boolean show = showToolTipsItem.getSelection();
            SetupActionBarContributor.setShowTooltips(show);
          }
        });

        liveValidationItem = createItem(SWT.CHECK, "live_validation", Messages.SetupEditor_setupLocationListener_liveValidationItem_tooltip, //$NON-NLS-1$
            new SelectionAdapter()
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

            @Override
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
      toolTipObjects = new ArrayList<>(toolTipObjects.subList(0, toolTipIndex + 1));
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
      Object unwrappedToolTipObject = ToolTipObject.unwrap(toolTipObject);
      editSetupItem.setEnabled(SetupActionBarContributor.getEditURI(unwrappedToolTipObject, !editorSpecific) != null);
      boolean enabled = SetupActionBarContributor.getEditURI(unwrappedToolTipObject, true) != null;
      editTextItem.setEnabled(enabled);

      if (editorSpecific)
      {
        showSetupItem.setEnabled(enabled);
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
        if (!"about:blank".equals(url)) //$NON-NLS-1$
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
          originalURI = URI.createURI("about:blank" + originalURI); //$NON-NLS-1$
        }

        String query = originalURI.query();
        if (query != null)
        {
          originalURI = originalURI.trimQuery().appendQuery(query.replace("%5B", "[").replaceAll("%5D", "]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }

        String fragment = originalURI.fragment();
        if (fragment != null)
        {
          originalURI = originalURI.trimFragment().appendFragment(fragment.replace("%5B", "[").replaceAll("%5D", "]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }
      }

      URI uri = originalURI;
      Map<Object, Object> options = new LinkedHashMap<>();
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
            if ("path".equals(uri.scheme())) //$NON-NLS-1$
            {
              viewer = setupEditor.selectionViewer;
              super.changing(event);
            }
          }

          if (event.location.equals("about:blank")) //$NON-NLS-1$
          {
            // Force event processing; on the Mac this allows the browser to redraw the text.
            if (OS.INSTANCE.isMac())
            {
              UIUtil.asyncExec(browser, new Runnable()
              {
                @Override
                public void run()
                {
                  content.getDisplay().readAndDispatch();
                }
              });
            }
          }
          else if (event.location.startsWith("about:blank#")) //$NON-NLS-1$
          {
            event.doit = false;
          }
          else if ("about:blank?extend".equals(event.location)) //$NON-NLS-1$
          {
            ToolTipObject wrapper = toolTipObjects.get(toolTipIndex);
            ToolTipObject extendedWrapper = new ToolTipObject(wrapper.getWrappedObject(), this, wrapper.getSetupEditor(), true, false);
            toolTipObjects.set(toolTipIndex, extendedWrapper);
            navigate(toolTipIndex);

            event.doit = false;
          }
          else if ("about:blank?no-extend".equals(event.location)) //$NON-NLS-1$
          {
            ToolTipObject wrapper = toolTipObjects.get(toolTipIndex);
            ToolTipObject extendedWrapper = new ToolTipObject(wrapper.getWrappedObject(), this, wrapper.getSetupEditor(), false, false);
            toolTipObjects.set(toolTipIndex, extendedWrapper);
            navigate(toolTipIndex);

            event.doit = false;
          }
          else if ("property".equals(uri.scheme())) //$NON-NLS-1$
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
      CookieHandler cookieHandler = CookieManager.getDefault();
      if (cookieHandler instanceof CookieManager)
      {
        CookieManager cookieManager = (CookieManager)cookieHandler;
        CookieStore cookieStore = cookieManager.getCookieStore();
        if (cookieStore != null)
        {
          for (java.net.URI uri : cookieStore.getURIs())
          {
            for (HttpCookie httpCookie : cookieStore.get(uri))
            {
              Browser.setCookie(httpCookie.getValue(), uri.toString());
            }
          }
        }
      }

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

    @SuppressWarnings({ "restriction", "deprecation" })
    private String getFullHTML(String text)
    {
      if (setupEditor == null)
      {
        return text;
      }

      String styleSheet = toolTipSupport.getStyleSheet();
      String symbolicFont = (String)ReflectUtil.invokeMethod("getSymbolicFont", toolTipSupport); //$NON-NLS-1$

      FontData fontData = JFaceResources.getFontRegistry().getFontData(symbolicFont)[0];
      styleSheet = org.eclipse.jface.internal.text.html.HTMLPrinter.convertTopLevelFont(styleSheet, fontData);
      Color foregroundColor = content.getForeground();
      Color backgroundColor = content.getBackground();

      StringBuffer result = new StringBuffer(text);
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
        setText(Messages.SetupEditor_setupLocationListener_noHistory);
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
          CellLabelProvider toolTipProvider = (CellLabelProvider)setupEditor.selectionViewer.getLabelProvider();
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
      CellLabelProvider toolTipProvider = (CellLabelProvider)setupEditor.selectionViewer.getLabelProvider();
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
      return super.toString() + " -> " + wrappedObject; //$NON-NLS-1$
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
      @Override
      public void selectionChanged(IWorkbenchPart part, ISelection selection)
      {
        getDockable().setWorkbenchPart(part);
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
    public boolean handleWorkbenchPart(IWorkbenchPart part)
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
      getDockable().setWorkbenchPart(setupEditor);

      // If the input is a URI...
      LocationEvent event = new LocationEvent(browser == null ? noBrowser : browser);
      if (input instanceof URI)
      {
        // Handle it just like we are following it as a link and set it into the browser as a URL.
        event.location = input.toString();
        locationListener.changing(event);
        browser.setUrl(event.location);
      }
      else
      {
        // Otherwise it must be an appropriate object with an associated URI.
        URI uri = SetupActionBarContributor.getEditURI(ToolTipObject.unwrap(input), true);
        event.location = uri == null ? "about:blank" : uri.toString(); //$NON-NLS-1$
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
      return SetupEditorPlugin.INSTANCE.getDialogSettings("Browser"); //$NON-NLS-1$
    }

    @Override
    protected Control createContents(Composite parent)
    {
      getShell().setText(Messages.SetupEditor_setupLocationListener_setupInformationBrowser);

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
        @Override
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
      Factory<BrowserDialog> factory = new Factory<>()
      {
        @Override
        public BrowserDialog create(IWorkbenchWindow workbenchWindow)
        {
          return new BrowserDialog(workbenchWindow);
        }
      };

      return DockableDialog.openFor(BrowserDialog.class, factory, workbenchWindow);
    }
  }
}

class ExtendedBaseItemProviderAdapterFactory extends BaseItemProviderAdapterFactory
{

  @Override
  public Adapter createAnnotationAdapter()
  {
    if (annotationItemProvider == null)
    {
      annotationItemProvider = new AnnotationItemProvider(this)
      {
        @Override
        protected Collection<?> filterChoices(Collection<?> choices, EStructuralFeature feature, Object object)
        {
          Collection<?> result = super.filterChoices(choices, feature, object);
          if (feature == BasePackage.Literals.ANNOTATION__REFERENCES)
          {
            Annotation annotation = (Annotation)object;
            if (AnnotationConstants.ANNOTATION_CONFIGURATION_REFERENCE.equals(annotation.getSource()))
            {
              List<Configuration> configurations = new ArrayList<>();
              for (Object choice : choices)
              {
                if (choice instanceof Configuration)
                {
                  configurations.add((Configuration)choice);
                }
              }
              return configurations;
            }
          }

          return result;
        }

        @Override
        public String getText(Object object)
        {
          String result = super.getText(object);
          Annotation annotation = (Annotation)object;
          if (AnnotationConstants.ANNOTATION_CONFIGURATION_REFERENCE.equals(annotation.getSource()))
          {
            EList<EObject> references = annotation.getReferences();
            if (!references.isEmpty())
            {
              StringBuilder text = new StringBuilder(result);
              AdapterFactoryItemDelegator adapterFactoryItemDelegator = new AdapterFactoryItemDelegator(getRootAdapterFactory());
              for (EObject reference : references)
              {
                text.append(" - ").append(adapterFactoryItemDelegator.getText(reference)); //$NON-NLS-1$
                if (text.length() > 500)
                {
                  text.append("..."); //$NON-NLS-1$
                  break;
                }
              }

              return text.toString();
            }
          }

          return result;
        }

        @Override
        protected Command createAddCommand(EditingDomain domain, EObject owner, EStructuralFeature feature, Collection<?> collection, int index)
        {
          if (feature == BasePackage.Literals.ANNOTATION__CONTENTS)
          {
            return UnexecutableCommand.INSTANCE;
          }

          return super.createAddCommand(domain, owner, feature, collection, index);
        }

        @Override
        public void notifyChanged(Notification notification)
        {
          super.notifyChanged(notification);
          switch (notification.getFeatureID(Annotation.class))
          {
            case BasePackage.ANNOTATION__REFERENCES:
              fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
              return;
          }
        }
      };
    }

    return annotationItemProvider;
  }
}
