/**
 * Copyright (c) 2024 ACTICO GmbH, Germany and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 */
package org.eclipse.oomph.maven.presentation;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.BaseFactory;
import org.eclipse.oomph.base.ModelElement;
import org.eclipse.oomph.base.provider.BaseItemProviderAdapterFactory;
import org.eclipse.oomph.maven.Coordinate;
import org.eclipse.oomph.maven.DOMElement;
import org.eclipse.oomph.maven.MavenFactory;
import org.eclipse.oomph.maven.Project;
import org.eclipse.oomph.maven.Realm;
import org.eclipse.oomph.maven.provider.MavenItemProviderAdapterFactory;
import org.eclipse.oomph.maven.provider.RealmItemProvider;
import org.eclipse.oomph.maven.provider.ReferenceGroup;
import org.eclipse.oomph.maven.util.MavenValidator;
import org.eclipse.oomph.maven.util.MavenValidator.ElementEdit;
import org.eclipse.oomph.maven.util.POMXMLUtil;
import org.eclipse.oomph.maven.util.POMXMLUtil.TextRegion;
import org.eclipse.oomph.predicates.provider.PredicatesItemProviderAdapterFactory;
import org.eclipse.oomph.resources.ResourcesUtil;
import org.eclipse.oomph.resources.provider.ResourcesItemProviderAdapterFactory;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.emf.common.command.AbstractCommand;
import org.eclipse.emf.common.command.AbstractCommand.NonDirtying;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.ui.ImageURIRegistry;
import org.eclipse.emf.common.ui.MarkerHelper;
import org.eclipse.emf.common.ui.editor.ProblemEditorPart;
import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;
import org.eclipse.emf.common.ui.viewer.IUndecoratingLabelProvider;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.action.EditingDomainActionBarContributor;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.dnd.EditingDomainViewerDropAdapter;
import org.eclipse.emf.edit.ui.dnd.LocalTransfer;
import org.eclipse.emf.edit.ui.dnd.ViewerDragAdapter;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.DelegatingStyledCellLabelProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator.DiagnosticAdapter;
import org.eclipse.emf.edit.ui.provider.UnwrappingSelectionProvider;
import org.eclipse.emf.edit.ui.util.EditUIMarkerHelper;
import org.eclipse.emf.edit.ui.util.EditUIUtil;
import org.eclipse.emf.edit.ui.util.FindAndReplaceTarget;
import org.eclipse.emf.edit.ui.util.IRevertablePart;
import org.eclipse.emf.edit.ui.view.ExtendedPropertySheetPage;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.text.AbstractHoverInformationControlManager;
import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertySheetPage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This is an example of a Maven model editor.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MavenEditor extends MultiPageEditorPart
    implements IEditingDomainProvider, ISelectionProvider, IMenuListener, IViewerProvider, IGotoMarker, IRevertablePart
{
  private static final String ANNOTATION_SOURCE_PROBLEM_PREFIX = "problem:/"; //$NON-NLS-1$

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
   * @generated
   */
  protected IContentOutlinePage contentOutlinePage;

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

  private Consumer<Resource> liveValidateScheduler;

  /**
   * This listens for when the outline becomes active
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected IPartListener partListener = new IPartListener()
  {
    @Override
    public void partActivated(IWorkbenchPart p)
    {
      if (p instanceof ContentOutline)
      {
        if (((ContentOutline)p).getCurrentPage() == contentOutlinePage)
        {
          getActionBarContributor().setActiveEditor(MavenEditor.this);

          setCurrentViewer(contentOutlineViewer);
        }
      }
      else if (p instanceof PropertySheet)
      {
        if (propertySheetPages.contains(((PropertySheet)p).getCurrentPage()))
        {
          getActionBarContributor().setActiveEditor(MavenEditor.this);
          handleActivate();
        }
      }
      else if (p == MavenEditor.this)
      {
        handleActivate();
      }
      else
      {
        handleOtherPartActivate(p);
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
   * @generated
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
                      DiagnosticDecorator.Styled.DiagnosticAdapter.update(resource,
                          markerHelper.getMarkerDiagnostics(resource, (IFile)delta.getResource(), false));
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
                getSite().getPage().closeEditor(MavenEditor.this, false);
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
              if (getSite().getPage().getActiveEditor() == MavenEditor.this)
              {
                handleActivate();
              }
            }
          });
        }
      }
      catch (CoreException exception)
      {
        MavenEditorPlugin.INSTANCE.log(exception);
      }
    }
  };

  private boolean showHoverLinks;

  private ColumnViewerInformationControlToolTipSupport toolTipSupport;

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
        getSite().getPage().closeEditor(MavenEditor.this, false);
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
  protected void updateProblemIndication()
  {
    if (updateProblemIndication)
    {
      BasicDiagnostic diagnostic = new BasicDiagnostic(Diagnostic.OK, "org.eclipse.oomph.maven.editor", //$NON-NLS-1$
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
          MavenEditorPlugin.INSTANCE.log(exception);
        }
      }

      if (markerHelper.hasMarkers(editingDomain.getResourceSet()))
      {
        try
        {
          markerHelper.updateMarkers(diagnostic);
        }
        catch (CoreException exception)
        {
          MavenEditorPlugin.INSTANCE.log(exception);
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
    return MessageDialog.openQuestion(getSite().getShell(), getString("_UI_FileConflict_label"), //$NON-NLS-1$
        getString("_WARN_FileConflict")); //$NON-NLS-1$
  }

  /**
   * This creates a model editor.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public MavenEditor()
  {
    super();
    initializeEditingDomain();
  }

  /**
   * This sets up the editing domain for the model editor.
   * <!-- begin-user-doc -->
   * This is here in case there are generator changes.  It's not actually used.
   * <!-- end-user-doc -->
   * @generated
   */
  protected void initializeEditingDomainGen()
  {
    // Create an adapter factory that yields item providers.
    //
    adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

    adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new MavenItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new BaseItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new PredicatesItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new ResourcesItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

    // Create the command stack that will notify this editor as commands are executed.
    //
    BasicCommandStack commandStack = new BasicCommandStack()
    {
      @Override
      public void execute(Command command)
      {
        // Cancel live validation before executing a command that will trigger a new round of validation.
        //
        if (!(command instanceof AbstractCommand.NonDirtying))
        {
          DiagnosticDecorator.Styled.cancel(editingDomain);
        }
        super.execute(command);
      }
    };

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
    // Create an adapter factory that yields item providers.
    //
    adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

    adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new MavenItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new BaseItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new PredicatesItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new ResourcesItemProviderAdapterFactory());
    adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

    // Create the command stack that will notify this editor as commands are executed.
    //
    BasicCommandStack commandStack = new BasicCommandStack()
    {
      @Override
      public void execute(Command command)
      {
        // Cancel live validation before executing a command that will trigger a new round of validation.
        //
        if (!(command instanceof AbstractCommand.NonDirtying) || command instanceof MavenActionBarContributor.ReconcileRealmAction.ReconcileCommand
            || command instanceof NonDirtyingCompoundCommand)
        {
          DiagnosticDecorator.Styled.cancel(editingDomain);
        }

        if (!(command instanceof MavenActionBarContributor.ReconcileRealmAction.ReconcileCommand))
        {
          Realm realm = getRealm();
          if (realm != null)
          {
            CompoundCommand compoundCommand = NonDirtyingCompoundCommand.create(command);
            compoundCommand.append(new MavenActionBarContributor.ReconcileRealmAction.ReconcileCommand(realm));
            command = compoundCommand;
          }
        }

        super.execute(command);
      }
    };

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

              // Schedule the live validator for the reconcile command.
              //
              if (shouldSchedule(mostRecentCommand))
              {
                for (Resource resource : editingDomain.getResourceSet().getResources())
                {
                  liveValidateScheduler.accept(resource);
                }
              }
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

      private boolean shouldSchedule(Command mostRecentCommand)
      {
        if (mostRecentCommand instanceof MavenActionBarContributor.ReconcileRealmAction.ReconcileCommand)
        {
          return true;
        }

        if (mostRecentCommand instanceof CompoundCommand compoundCommand)
        {
          for (Command command : compoundCommand.getCommandList())
          {
            if (shouldSchedule(command))
            {
              return true;
            }
          }
        }

        return false;
      }
    });

    // Create the editing domain with a special command stack.
    //
    editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<>())
    {
      @Override
      public Object getWrapper(Object object)
      {
        return object;
      }
    };
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
   * @generated
   */
  protected void createContextMenuFor(StructuredViewer viewer)
  {
    MenuManager contextMenu = new MenuManager("#PopUp"); //$NON-NLS-1$
    contextMenu.add(new Separator("additions")); //$NON-NLS-1$
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

  private void openEditor(Path location, Element element)
  {
    IWorkbenchPage page = getEditorSite().getPage();

    IEditorDescriptor descriptor = page.getWorkbenchWindow().getWorkbench().getEditorRegistry().getDefaultEditor(location.getFileName().toString());
    String id = descriptor.getId();

    IFile[] files = ResourcesUtil.findFilesForLocationURI(location.toUri());
    IEditorInput input = files.length == 0 ? new FileStoreEditorInput(EFS.getLocalFileSystem().fromLocalFile(location.toFile()))
        : new FileEditorInput(files[0]);
    try
    {
      for (IEditorReference editorReference : page.getEditorReferences())
      {
        if (id.equals(editorReference.getId()))
        {
          IWorkbenchPart part = editorReference.getPart(false);
          if (part != null && page.isPartVisible(part))
          {
            page.activate(part);
            break;
          }
        }
      }

      IEditorPart editor = IDE.openEditor(page, input, descriptor.getId());
      if (editor != null)
      {
        TextRegion selection = POMXMLUtil.getSelection(element);
        if (selection != null)
        {
          ISelectionProvider selectionProvider = editor.getEditorSite().getSelectionProvider();
          selectionProvider.setSelection(new TextSelection(selection.start(), selection.length()));
        }
      }
    }
    catch (PartInitException ex)
    {
      MavenEditorPlugin.INSTANCE.log(ex);
    }
  }

  /**
   * This is the method called to load a resource into the editing domain's resource set based on the editor's input.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createModelGen()
  {
    URI resourceURI = EditUIUtil.getURI(getEditorInput(), editingDomain.getResourceSet().getURIConverter());
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
    Realm realm = getRealm();
    if (realm != null)
    {
      realm.reconcile();
    }
  }

  protected Realm getRealm()
  {
    EList<Resource> resources = editingDomain.getResourceSet().getResources();
    if (!resources.isEmpty())
    {
      EList<EObject> contents = resources.get(0).getContents();
      if (!contents.isEmpty())
      {
        EObject eObject = contents.get(0);
        if (eObject instanceof Realm realm)
        {
          return realm;
        }
      }
    }

    return null;
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
      BasicDiagnostic basicDiagnostic = new BasicDiagnostic(hasErrors ? Diagnostic.ERROR : Diagnostic.WARNING, "org.eclipse.oomph.maven.editor", //$NON-NLS-1$
          0, getString("_UI_CreateModelError_message", resource.getURI()), //$NON-NLS-1$
          new Object[] { exception == null ? (Object)resource : exception });
      basicDiagnostic.merge(EcoreUtil.computeDiagnostic(resource, true));
      return basicDiagnostic;
    }
    else if (exception != null)
    {
      return new BasicDiagnostic(Diagnostic.ERROR, "org.eclipse.oomph.maven.editor", //$NON-NLS-1$
          0, getString("_UI_CreateModelError_message", resource.getURI()), //$NON-NLS-1$
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
   * This is retained to update {@link #createPages()} should the generator generate improvements in the future.
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPagesGen()
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

      selectionViewer.setUseHashlookup(true);
      selectionViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
      selectionViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(
          new DecoratingColumLabelProvider.StyledLabelProvider(new AdapterFactoryLabelProvider.StyledLabelProvider(adapterFactory, selectionViewer),
              new DiagnosticDecorator.Styled(editingDomain, selectionViewer, MavenEditorPlugin.getPlugin().getDialogSettings()))));
      selectionViewer.setInput(editingDomain.getResourceSet());
      selectionViewer.setSelection(new StructuredSelection(editingDomain.getResourceSet().getResources().get(0)), true);

      new AdapterFactoryTreeEditor(selectionViewer.getTree(), adapterFactory);
      new ColumnViewerInformationControlToolTipSupport(selectionViewer,
          new DiagnosticDecorator.Styled.EditingDomainLocationListener(editingDomain, selectionViewer));

      createContextMenuFor(selectionViewer);
      int pageIndex = addPage(tree);
      setPageText(pageIndex, getString("_UI_SelectionPage_label")); //$NON-NLS-1$

      getSite().getShell().getDisplay().asyncExec(new Runnable()
      {
        @Override
        public void run()
        {
          if (!getContainer().isDisposed())
          {
            setActivePage(0);
          }
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
      @Override
      public void run()
      {
        updateProblemIndication();
      }
    });
  }

  @Override
  public void createPages()
  {
    getEditingDomain().getResourceSet().eAdapters().add(new DiagnosticAdapter()
    {
      @Override
      protected void updateDiagnostic(Diagnostic diagnostic)
      {
        for (Diagnostic childDiagnostic : diagnostic.getChildren())
        {
          Resource resource = (Resource)childDiagnostic.getData().get(0);
          Set<EObject> staleAnnotations = new LinkedHashSet<>();
          for (TreeIterator<EObject> it = resource.getAllContents(); it.hasNext();)
          {
            EObject eObject = it.next();
            if (eObject instanceof Annotation annotation)
            {
              String source = annotation.getSource();
              if (source != null && source.startsWith(ANNOTATION_SOURCE_PROBLEM_PREFIX))
              {
                staleAnnotations.add(eObject);
              }
            }
          }

          for (EObject eObject : staleAnnotations)
          {
            EObject eContainer = eObject.eContainer();
            eContainer.eSetDeliver(false);
            EcoreUtil.delete(eObject, false);
            eContainer.eSetDeliver(true);
          }

          for (Diagnostic grandChildDiagnostic : childDiagnostic.getChildren())
          {
            List<?> data = grandChildDiagnostic.getData();
            if (!data.isEmpty())
            {
              Object object = data.get(0);
              if (object instanceof ModelElement modelElement && isTransientlyContained(modelElement))
              {
                try
                {
                  modelElement.eSetDeliver(false);

                  String message = grandChildDiagnostic.getMessage();
                  Annotation annotation = BaseFactory.eINSTANCE.createAnnotation();
                  annotation.setSource(ANNOTATION_SOURCE_PROBLEM_PREFIX + URI.encodeSegment(DiagnosticDecorator.strip(message), false));
                  modelElement.getAnnotations().add(annotation);

                  ElementEdit elementEdit = MavenValidator.ElementEdit.of(grandChildDiagnostic);
                  if (elementEdit != null)
                  {
                    Annotation elementEditAnnotation = BaseFactory.eINSTANCE.createAnnotation();
                    elementEditAnnotation.setSource(ANNOTATION_SOURCE_PROBLEM_PREFIX
                        + URI.encodeSegment("ElementEdit: " + elementEdit.element().getLocalName() + " -> " + elementEdit.value(), false)); //$NON-NLS-1$//$NON-NLS-2$
                    modelElement.getAnnotations().add(elementEditAnnotation);

                    associatedElementEdit(annotation, elementEdit);
                    associatedElementEdit(elementEditAnnotation, elementEdit);
                  }
                }
                finally
                {
                  modelElement.eSetDeliver(true);
                }
              }
            }
          }
        }
      }

      protected boolean isTransientlyContained(EObject eObject)
      {
        for (EObject container = eObject; container != null; container = container.eContainer())
        {
          EStructuralFeature eContainingFeature = eObject.eContainingFeature();
          if (eContainingFeature.isTransient())
          {
            return true;

          }
        }

        return false;
      }

      @Override
      protected void handleResourceDiagnostics(List<Resource> resources)
      {
      }
    });

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

      toolTipSupport = new ColumnViewerInformationControlToolTipSupport(selectionViewer,
          new DiagnosticDecorator.Styled.EditingDomainLocationListener(editingDomain, selectionViewer));

      selectionViewer.setUseHashlookup(true);
      selectionViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));

      selectionViewer.setLabelProvider(createLabelProvider());
      selectionViewer.setInput(editingDomain.getResourceSet());
      selectionViewer.setSelection(new StructuredSelection(editingDomain.getResourceSet().getResources().get(0)), true);

      new AdapterFactoryTreeEditor(selectionViewer.getTree(), adapterFactory);

      createContextMenuFor(selectionViewer);
      int pageIndex = addPage(tree);
      setPageText(pageIndex, getString("_UI_SelectionPage_label")); //$NON-NLS-1$

      getSite().getShell().getDisplay().asyncExec(new Runnable()
      {
        @Override
        public void run()
        {
          if (!getContainer().isDisposed())
          {
            setActivePage(0);
          }
        }
      });

      tree.addMouseListener(new MouseAdapter()
      {
        @Override
        public void mouseDoubleClick(MouseEvent event)
        {
          if (event.button == 1)
          {
            try
            {
              Element element = getElement();
              if (element != null)
              {
                Path location = POMXMLUtil.getLocation(element);
                if (location != null)
                {
                  openEditor(location, element);
                  return;
                }
              }

              getEditorSite().getPage().showView("org.eclipse.ui.views.PropertySheet"); //$NON-NLS-1$
            }
            catch (PartInitException exception)
            {
              MavenEditorPlugin.INSTANCE.log(exception);
            }
          }
        }

        @Override
        public void mouseUp(final MouseEvent e)
        {
          if ((e.stateMask & SWT.MOD3) != 0)
          {
            try
            {
              showHoverLinks = true;
              Event exitEvent = new Event();
              exitEvent.display = e.display;
              exitEvent.x = -1;
              exitEvent.y = -1;
              tree.notifyListeners(SWT.MouseExit, exitEvent);

              ReflectUtil.setValue("currentCell", toolTipSupport, null); //$NON-NLS-1$

              Event event = new Event();
              event.display = e.display;
              event.x = e.x;
              event.y = e.y;
              tree.notifyListeners(SWT.MouseHover, event);
            }
            finally
            {
              showHoverLinks = false;
            }
          }
        }

        private Element getElement()
        {
          Object element = AdapterFactoryEditingDomain.unwrap(selectionViewer.getStructuredSelection().getFirstElement());
          if (element instanceof Project project)
          {
            return project.getElement(POMXMLUtil.xpath(Coordinate.ARTIFACT_ID));
          }

          if (element instanceof DOMElement domElement)
          {
            return domElement.getElement();
          }

          if (element instanceof Annotation annotation)
          {
            ElementEdit elementEdit = getAssociatedElementEdit(annotation);
            if (elementEdit != null)
            {
              return elementEdit.element();
            }
          }

          return null;
        }
      });

      tree.addKeyListener(new KeyAdapter()
      {
        @Override
        public void keyReleased(KeyEvent e)
        {
          if (e.keyCode == SWT.CR)
          {
            UIUtil.toggleExpandOneLevel(tree, e.stateMask == SWT.NONE, 2000);
          }
        }
      });

      selectionViewer.getControl().addFocusListener(new FocusListener()
      {
        private IBaseLabelProvider originalSelectionViewerLabelProvider = selectionViewer.getLabelProvider();

        @Override
        public void focusLost(FocusEvent e)
        {
        }

        @Override
        public void focusGained(FocusEvent e)
        {
          IBaseLabelProvider labelProvider = selectionViewer.getLabelProvider();
          if (labelProvider != originalSelectionViewerLabelProvider)
          {
            selectionViewer.setLabelProvider(originalSelectionViewerLabelProvider = createLabelProvider());
          }
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
      @Override
      public void run()
      {
        updateProblemIndication();
      }
    });
  }

  private ILabelProvider createLabelProvider()
  {
    return new DelegatingStyledCellLabelProvider(
        new DecoratingColumLabelProvider.StyledLabelProvider(new AdapterFactoryLabelProvider.StyledLabelProvider(adapterFactory, selectionViewer),
            new DiagnosticDecorator.Styled(editingDomain, selectionViewer, MavenEditorPlugin.getPlugin().getDialogSettings())
            {
              private ILabelProvider labelProvider;

              {
                liveValidateScheduler = getLiveValidator()::scheduleValidation;
              }

              Function<Object, String> featureTextProvider = new RealmItemProvider(new MavenItemProviderAdapterFactory())
              {
                @Override
                protected String getFeatureText(Object feature)
                {
                  return super.getFeatureText(feature);
                }
              }::getFeatureText;

              protected ILabelProvider getLabelProvider()
              {
                if (labelProvider == null)
                {
                  labelProvider = (ILabelProvider)viewer.getLabelProvider();
                  if (labelProvider instanceof IUndecoratingLabelProvider)
                  {
                    final IUndecoratingLabelProvider undecoratingLabelProvider = (IUndecoratingLabelProvider)labelProvider;
                    labelProvider = new ILabelProvider()
                    {
                      @Override
                      public void removeListener(ILabelProviderListener listener)
                      {
                        undecoratingLabelProvider.removeListener(listener);
                      }

                      @Override
                      public boolean isLabelProperty(Object element, String property)
                      {
                        return undecoratingLabelProvider.isLabelProperty(element, property);
                      }

                      @Override
                      public void dispose()
                      {
                        undecoratingLabelProvider.dispose();
                      }

                      @Override
                      public void addListener(ILabelProviderListener listener)
                      {
                        undecoratingLabelProvider.addListener(listener);
                      }

                      @Override
                      public String getText(Object element)
                      {
                        return undecoratingLabelProvider.getUndecoratedText(element);
                      }

                      @Override
                      public Image getImage(Object element)
                      {
                        return undecoratingLabelProvider.getUndecoratedImage(element);
                      }
                    };
                  }
                }

                return labelProvider;
              }

              @Override
              public String getToolTipText(Object element)
              {
                if (showHoverLinks)
                {
                  Object unwrappedElement = AdapterFactoryEditingDomain.unwrap(element);
                  if (element != unwrappedElement && unwrappedElement instanceof EObject eObject)
                  {
                    return process(buildReferences(build(new StringBuilder(), eObject), eObject).toString());
                  }

                  if (element instanceof EObject eObject)
                  {
                    return process(buildReferences(new StringBuilder(), eObject).toString());
                  }
                }

                return process(super.getToolTipText(element));
              }

              @Override
              protected BasicDiagnostic decorate(Map<Object, BasicDiagnostic> objects, ITreeContentProvider treeContentProvider, Set<Object> visited,
                  Object object, List<Integer> path)
              {
                if (object instanceof ReferenceGroup referenceGroup && !referenceGroup.getFeature().isContainment())
                {
                  return null;
                }

                return super.decorate(objects, treeContentProvider, visited, object, path);
              }

              private StringBuilder buildReferences(StringBuilder result, EObject eObject)
              {
                EList<EObject> eCrossReferences = eObject.eCrossReferences();
                if (!eCrossReferences.isEmpty())
                {
                  for (EObject eCrossReference : eCrossReferences)
                  {
                    build(result, eCrossReference);
                  }
                }

                return result;
              }

              @SuppressWarnings("nls")
              private StringBuilder build(StringBuilder result, EObject eObject)
              {
                result.append("<div style='white-space: nowrap;'>");
                for (int i = 0; i <= 0; ++i)
                {
                  result.append("&#160;&#160;&#160;");
                }

                ILabelProvider labelProvider = getLabelProvider();
                String text = escapeContent(labelProvider.getText(eObject));
                Image image = labelProvider.getImage(eObject);
                result.append(featureTextProvider.apply(eObject.eContainmentFeature()));
                result.append("<img src='");
                result.append(ImageURIRegistry.INSTANCE.getImageURI(image));
                result.append("'/> <a href=\"");
                result.append(EcoreUtil.getURI(eObject));
                result.append("\">");
                result.append(text);
                result.append("</a></div>\n");
                return result;
              }

              private String process(String result)
              {
                if (result != null)
                {
                  try
                  {
                    AbstractHoverInformationControlManager hoverInformationControlManager = ReflectUtil.getValue("hoverInformationControlManager", //$NON-NLS-1$
                        toolTipSupport);
                    Point size = UIUtil.caclcuateSize(result);
                    hoverInformationControlManager.setSizeConstraints(size.x, Math.max(3, size.y * 4 / 3), true, false);
                  }
                  catch (Throwable throwable)
                  {
                    // Ignore.
                  }
                }

                return result;
              }
            }));
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
  @Override
  public <T> T getAdapter(Class<T> key)
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
    else if (key.equals(IFindReplaceTarget.class))
    {
      return FindAndReplaceTarget.getAdapter(key, this, MavenEditorPlugin.getPlugin());
    }
    else
    {
      return super.getAdapter(key);
    }
  }

  /**
   * This accesses a cached version of the content outliner.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IContentOutlinePage getContentOutlinePage()
  {
    if (contentOutlinePage == null)
    {
      // The content outline is just a tree.
      //
      class MyContentOutlinePage extends ContentOutlinePage
      {
        @Override
        public void createControl(Composite parent)
        {
          super.createControl(parent);
          contentOutlineViewer = getTreeViewer();
          contentOutlineViewer.addSelectionChangedListener(this);

          // Set up the tree viewer.
          //
          contentOutlineViewer.setUseHashlookup(true);
          contentOutlineViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
          contentOutlineViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(
              new DecoratingColumLabelProvider.StyledLabelProvider(new AdapterFactoryLabelProvider.StyledLabelProvider(adapterFactory, contentOutlineViewer),
                  new DiagnosticDecorator.Styled(editingDomain, contentOutlineViewer, MavenEditorPlugin.getPlugin().getDialogSettings()))));
          contentOutlineViewer.setInput(editingDomain.getResourceSet());

          new ColumnViewerInformationControlToolTipSupport(contentOutlineViewer,
              new DiagnosticDecorator.Styled.EditingDomainLocationListener(editingDomain, contentOutlineViewer));

          // Make sure our popups work.
          //
          createContextMenuFor(contentOutlineViewer);

          if (!editingDomain.getResourceSet().getResources().isEmpty())
          {
            // Select the root object in the view.
            //
            contentOutlineViewer.setSelection(new StructuredSelection(editingDomain.getResourceSet().getResources().get(0)), true);
          }
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
          getActionBarContributor().shareGlobalActions(this, actionBars);
        }
      }

      contentOutlinePage = new MyContentOutlinePage();

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
   * @generated
   */
  public IPropertySheetPage getPropertySheetPage()
  {
    PropertySheetPage propertySheetPage = new ExtendedPropertySheetPage(editingDomain, ExtendedPropertySheetPage.Decoration.LIVE,
        MavenEditorPlugin.getPlugin().getDialogSettings(), 0, true)
    {
      @Override
      public void setSelectionToViewer(List<?> selection)
      {
        MavenEditor.this.setSelectionToViewer(selection);
        MavenEditor.this.setFocus();
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
   * @generated
   */
  public void handleContentOutlineSelection(ISelection selection)
  {
    if (selectionViewer != null && !selection.isEmpty() && selection instanceof IStructuredSelection)
    {
      Iterator<?> selectedElements = ((IStructuredSelection)selection).iterator();
      if (selectedElements.hasNext())
      {
        // Get the first selected element.
        //
        Object selectedElement = selectedElements.next();

        ArrayList<Object> selectionList = new ArrayList<>();
        selectionList.add(selectedElement);
        while (selectedElements.hasNext())
        {
          selectionList.add(selectedElements.next());
        }

        // Set the selection to the widget.
        //
        selectionViewer.setSelection(new StructuredSelection(selectionList));
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
   * This is for implementing {@link IRevertablePart}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void doRevert()
  {
    DiagnosticDecorator.cancel(editingDomain);

    ResourceSet resourceSet = editingDomain.getResourceSet();
    List<Resource> resources = resourceSet.getResources();
    List<Resource> unloadedResources = new ArrayList<>();
    updateProblemIndication = false;
    for (int i = 0; i < resources.size(); ++i)
    {
      Resource resource = resources.get(i);
      if (resource.isLoaded())
      {
        resource.unload();
        unloadedResources.add(resource);
      }
    }

    resourceToDiagnosticMap.clear();
    for (Resource resource : unloadedResources)
    {
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

    editingDomain.getCommandStack().flush();

    if (AdapterFactoryEditingDomain.isStale(editorSelection))
    {
      setSelection(StructuredSelection.EMPTY);
    }

    updateProblemIndication = true;
    updateProblemIndication();
  }

  /**
   * This is for implementing {@link IEditorPart} and simply saves the model file.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void doSave(IProgressMonitor progressMonitor)
  {
    // Save only resources that have actually changed.
    //
    final Map<Object, Object> saveOptions = new HashMap<>();
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
        List<Resource> resources = editingDomain.getResourceSet().getResources();
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
      MavenEditorPlugin.INSTANCE.log(exception);
    }
    updateProblemIndication = true;
    updateProblemIndication();
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
  @Override
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
    return MavenEditorPlugin.INSTANCE.getString(key);
  }

  /**
   * This looks up a string in plugin.properties, making a substitution.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static String getString(String key, Object s1)
  {
    return MavenEditorPlugin.INSTANCE.getString(key, new Object[] { s1 });
  }

  /**
   * This implements {@link org.eclipse.jface.action.IMenuListener} to help fill the context menus with contributions from the Edit menu.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
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
    linkListener.dispose();
    disposeGen();
  }

  /**
   * Returns whether the outline view should be presented to the user.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected boolean showOutlineView()
  {
    return false;
  }

  @SuppressWarnings("nls")
  private static final Set<String> LINK_WITH_VIEWER_IDS = Set.of(//
      "org.eclipse.egit.ui.RepositoriesView", //
      "org.eclipse.ui.navigator.ProjectExplorer", //
      "org.eclipse.jdt.ui.PackageExplorer" //
  );

  protected void handleOtherPartActivate(IWorkbenchPart part)
  {
    String id = part.getSite().getId();
    if (LINK_WITH_VIEWER_IDS.contains(id))
    {
      ISelectionProvider selectionProvider = part.getAdapter(ISelectionProvider.class);
      if (selectionProvider != null)
      {
        linkListener.listenTo(selectionProvider);
      }
      else
      {

        try
        {
          TreeViewer treeViewer = ReflectUtil.invokeMethod("getCommonViewer", part); //$NON-NLS-1$
          linkListener.listenTo(treeViewer);
        }
        catch (RuntimeException ex)
        {
          //$FALL-THROUGH$
        }
      }
    }
  }

  private static void associatedElementEdit(EObject eObject, ElementEdit elementEdit)
  {
    eObject.eAdapters().add(new ElementEditAdapter(elementEdit));
  }

  private static ElementEdit getAssociatedElementEdit(EObject eObject)
  {
    ElementEditAdapter adapter = (ElementEditAdapter)EcoreUtil.getAdapter(eObject.eAdapters(), ElementEdit.class);
    return adapter == null ? null : adapter.elementEdit;
  }

  private static class ElementEditAdapter extends AdapterImpl
  {
    public ElementEdit elementEdit;

    public ElementEditAdapter(ElementEdit elementEdit)
    {
      this.elementEdit = elementEdit;
    }

    @Override
    public boolean isAdapterForType(Object type)
    {
      return type == ElementEdit.class;
    }

  }

  private LinkListener linkListener = new LinkListener();

  private class LinkListener implements ISelectionChangedListener
  {
    private final Set<ISelectionProvider> selectionProviders = new HashSet<>();

    @Override
    public void selectionChanged(SelectionChangedEvent event)
    {
      IStructuredSelection selection = event.getStructuredSelection();
      if (selection != null)
      {
        Object firstElement = selection.getFirstElement();
        Path path = getPath(firstElement);
        Project mavenProject = handleLocation(path);
        if (mavenProject != null)
        {
          Realm realm = getRealm();
          Project projectIgnoreVersion = realm.getProjectIgnoreVersion(mavenProject);
          if (projectIgnoreVersion != null)
          {
            setSelectionToViewer(List.of(projectIgnoreVersion));
          }
        }
      }
    }

    public Path getPath(Object element)
    {
      if (element instanceof IResource resource)
      {
        IPath location = resource.getLocation();
        if (location != null)
        {
          return location.toPath();
        }
      }
      else
      {
        try
        {
          IPath path = ReflectUtil.invokeMethod("getPath", element); //$NON-NLS-1$
          Path fileSystemPath = path.toPath();
          if (Files.exists(fileSystemPath))
          {
            return fileSystemPath;
          }

          IResource resource = EcorePlugin.getWorkspaceRoot().findMember(path);
          if (resource != null)
          {
            IPath location = resource.getLocation();
            if (location != null)
            {
              return location.toPath();
            }
          }
        }
        catch (Exception ex)
        {
          //$FALL-THROUGH$
        }

        try
        {
          return getPath(ReflectUtil.invokeMethod("getProject", element)); //$NON-NLS-1$
        }
        catch (Exception ex)
        {
          //$FALL-THROUGH$
        }

        if (element instanceof IAdaptable adaptable)
        {
          IWorkbenchAdapter workbenchAdapter = adaptable.getAdapter(IWorkbenchAdapter.class);
          if (workbenchAdapter != null)
          {
            Object parent = workbenchAdapter.getParent(element);
            return getPath(parent);
          }
        }
      }

      return null;

    }

    @SuppressWarnings("nls")
    public Project handleLocation(Path path)
    {
      if (path == null)
      {
        return null;
      }

      if (Files.isRegularFile(path))
      {
        String fileName = path.getFileName().toString();
        if (fileName.endsWith(".jar"))
        {
          Project result = handleLocation(path.resolve("../" + fileName.replaceAll(".jar$", ".pom")));
          if (result != null)
          {
            return result;
          }
        }
        else if (fileName.equals("pom.xml") || fileName.endsWith(".pom"))
        {
          return getMavenProject(path);
        }
      }
      else if (Files.isDirectory(path))
      {
        Path pom = path.resolve("pom.xml"); //$NON-NLS-1$
        if (Files.isRegularFile(pom))
        {
          return handleLocation(pom);
        }
      }

      return handleLocation(path.getParent());
    }

    public Project getMavenProject(Path path)
    {
      try
      {
        Document document = POMXMLUtil.parseDocument(path);
        Project mavenProject = MavenFactory.eINSTANCE.createProject();
        mavenProject.setLocation(path.toString());
        Element pom = document.getDocumentElement();
        mavenProject.setElement(pom);
        return mavenProject;
      }
      catch (IOException | SAXException ex)
      {
        return null;
      }
    }

    public void listenTo(ISelectionProvider selectionProvider)
    {
      if (selectionProviders.add(selectionProvider))
      {
        selectionProvider.addSelectionChangedListener(this);
      }
    }

    void dispose()
    {
      for (ISelectionProvider selectionProvider : selectionProviders)
      {
        selectionProvider.removeSelectionChangedListener(this);
      }
    }
  }

  private static class NonDirtyingCompoundCommand extends CompoundCommand implements NonDirtying
  {
    public NonDirtyingCompoundCommand()
    {
      super(0);
    }

    public static CompoundCommand create(Command command)
    {
      CompoundCommand result = command instanceof NonDirtying ? new NonDirtyingCompoundCommand() : new CompoundCommand(0);
      result.append(command);
      return result;
    }
  }
}
