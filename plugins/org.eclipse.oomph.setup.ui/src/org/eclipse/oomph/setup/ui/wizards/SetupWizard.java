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
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.p2.internal.ui.P2ServiceUI;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.ResourceMirror;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.CollectionUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public abstract class SetupWizard extends Wizard implements IPageChangedListener
{
  public static final String HELP_FOLDER = "/org.eclipse.oomph.setup.doc/html/user/wizard/";

  static boolean ecfWorkAround;

  private Trigger trigger;

  private String triggerName;

  private Object lastPage;

  private ResourceSet resourceSet;

  private CatalogManager catalogManager;

  private SetupContext setupContext;

  private SetupTaskPerformer performer;

  private String vmPath;

  private IndexLoader indexLoader;

  private Runnable indexLoadedAction;

  private Runnable finishAction;

  private ComposedAdapterFactory adapterFactory;

  private boolean isCanceled;

  private Shell simpleShell;

  public SetupWizard()
  {
    this((SetupTaskPerformer)null);
  }

  public SetupWizard(SetupTaskPerformer performer)
  {
    initUI();
    if (performer == null)
    {
      resourceSet = SetupCoreUtil.createResourceSet();
      setTrigger(Trigger.STARTUP);
      if (SetupContext.WORKSPACE_LOCATION_URI != null)
      {
        setSetupContext(SetupContext.create(resourceSet));
      }
    }
    else
    {
      this.performer = performer;
      setTrigger(performer.getTrigger());
      setSetupContext(performer.getSetupContext());
      resourceSet = performer.getUser().eResource().getResourceSet();
    }
  }

  public SetupWizard(SetupContext setupContext)
  {
    initUI();
    setTrigger(Trigger.STARTUP);
    setSetupContext(setupContext);
    resourceSet = setupContext.getUser().eResource().getResourceSet();
  }

  private void initUI()
  {
    setDefaultPageImageDescriptor(SetupUIPlugin.INSTANCE.getImageDescriptor("install_wiz.png"));
    setNeedsProgressMonitor(false);
  }

  public abstract String getHelpPath();

  public ComposedAdapterFactory getAdapterFactory()
  {
    if (adapterFactory == null)
    {
      adapterFactory = BaseEditUtil.createAdapterFactory();
    }

    return adapterFactory;
  }

  public void setTrigger(Trigger trigger)
  {
    this.trigger = trigger;
  }

  public Trigger getTrigger()
  {
    return trigger;
  }

  public String getTriggerName()
  {
    if (triggerName != null)
    {
      return triggerName;
    }

    if (trigger != null)
    {
      return trigger.toString();
    }

    return "ALL";
  }

  public void setTriggerName(String triggerName)
  {
    this.triggerName = triggerName == null ? null : triggerName.toUpperCase();
  }

  public IndexLoader getIndexLoader()
  {
    return indexLoader;
  }

  public void setIndexLoader(IndexLoader indexLoader)
  {
    this.indexLoader = indexLoader;
    if (indexLoader != null)
    {
      indexLoader.setWizard(this);
    }
  }

  public void setIndexLoadedAction(Runnable indexLoadedAction)
  {
    this.indexLoadedAction = indexLoadedAction;
  }

  public void setFinishAction(Runnable finishAction)
  {
    this.finishAction = finishAction;
  }

  public ResourceSet getResourceSet()
  {
    return resourceSet;
  }

  public CatalogManager getCatalogManager()
  {
    if (catalogManager == null)
    {
      catalogManager = new CatalogManager();
    }

    return catalogManager;
  }

  public SetupContext getSetupContext()
  {
    return setupContext;
  }

  public void setSetupContext(SetupContext setupContext)
  {
    this.setupContext = setupContext;
  }

  public Installation getInstallation()
  {
    return setupContext == null ? null : setupContext.getInstallation();
  }

  public Workspace getWorkspace()
  {
    return setupContext == null ? null : setupContext.getWorkspace();
  }

  public User getUser()
  {
    return setupContext == null ? null : setupContext.getUser();
  }

  public SetupTaskPerformer getPerformer()
  {
    return performer;
  }

  public void setPerformer(SetupTaskPerformer performer)
  {
    this.performer = performer;
  }

  public String getVMPath()
  {
    return vmPath;
  }

  public void setVMPath(String vmPath)
  {
    this.vmPath = vmPath;
  }

  @Override
  public void setContainer(IWizardContainer newContainer)
  {
    IWizardContainer oldContainer = getContainer();
    if (oldContainer instanceof IPageChangeProvider)
    {
      ((IPageChangeProvider)oldContainer).removePageChangedListener(this);
    }

    super.setContainer(newContainer);

    if (newContainer instanceof IPageChangeProvider)
    {
      ((IPageChangeProvider)newContainer).addPageChangedListener(this);
    }
  }

  @Override
  public Shell getShell()
  {
    if (simpleShell != null)
    {
      return simpleShell;
    }

    return super.getShell();
  }

  public void setSimpleShell(Shell simpleShell)
  {
    this.simpleShell = simpleShell;
  }

  @Override
  public void createPageControls(Composite pageContainer)
  {
    super.createPageControls(pageContainer);

    if (SetupPropertyTester.getHandlingShell() == null)
    {
      SetupPropertyTester.setHandlingShell(pageContainer.getShell());
    }
  }

  @Override
  public void addPages()
  {
    IDialogSettings wizardSection = SetupUIPlugin.INSTANCE.getDialogSettings(getClass().getSimpleName());
    setDialogSettings(wizardSection);

    addPage(new VariablePage());
    addPage(new ConfirmationPage());
    addPage(new ProgressPage());
  }

  public void pageChanged(PageChangedEvent event)
  {
    Object targetPage = event.getSelectedPage();

    boolean forward = true;
    if (lastPage != null)
    {
      for (IWizardPage page : getPages())
      {
        if (page == lastPage)
        {
          break;
        }

        if (page == targetPage)
        {
          forward = false;
          break;
        }
      }

      if (lastPage instanceof SetupWizardPage)
      {
        ((SetupWizardPage)lastPage).leavePage(forward);
      }
    }

    // Remember new page before enterPage() to support page change in enterPage().
    lastPage = targetPage;

    if (isCanceled)
    {
      dispose();
      getContainer().getShell().dispose();
    }
    else if (targetPage instanceof SetupWizardPage)
    {
      SetupWizardPage setupWizardPage = (SetupWizardPage)targetPage;
      setupWizardPage.enterPage(forward);
    }
  }

  @Override
  public boolean performCancel()
  {
    for (IWizardPage page : getPages())
    {
      if (!((SetupWizardPage)page).performCancel())
      {
        return false;
      }
    }

    isCanceled = true;
    clearStartupProperties();

    return true;
  }

  @Override
  public boolean canFinish()
  {
    for (IWizardPage page : getPages())
    {
      if (!page.isPageComplete())
      {
        if (page instanceof ProgressPage && lastPage instanceof ConfirmationPage)
        {
          break;
        }

        return false;
      }
    }

    return true;
  }

  @Override
  public boolean performFinish()
  {
    if (lastPage instanceof ProgressPage)
    {
      clearStartupProperties();
      if (finishAction != null)
      {
        UIUtil.syncExec(finishAction);
      }

      return true;
    }

    ((SetupWizardPage)lastPage).advanceToNextPage();
    return false;
  }

  private void clearStartupProperties()
  {
    System.clearProperty(SetupProperties.PROP_SETUP_OFFLINE_STARTUP);
    System.clearProperty(SetupProperties.PROP_SETUP_MIRRORS_STARTUP);
  }

  public int openDialog(Shell parentShell)
  {
    WizardDialog dialog = new SetupWizardDialog(parentShell, this);
    return dialog.open();
  }

  public void reloadIndex()
  {
    reloadIndex(null);
  }

  public void reloadIndex(Set<Resource> updatedResources)
  {
    Set<Resource> excludedResources = new LinkedHashSet<Resource>();

    Resource selectionResource = getCatalogManager().getSelection().eResource();
    excludedResources.add(selectionResource);

    Installation installation = setupContext.getInstallation();
    if (installation != null)
    {
      excludedResources.add(installation.eResource());
    }

    Workspace workspace = setupContext.getWorkspace();
    if (workspace != null)
    {
      excludedResources.add(workspace.eResource());
    }

    User user = setupContext.getUser();
    excludedResources.add(user.eResource());

    Set<Resource> retainedResources = new HashSet<Resource>();
    EList<Resource> resources = resourceSet.getResources();
    for (Iterator<Resource> it = resources.iterator(); it.hasNext();)
    {
      Resource resource = it.next();
      if (!excludedResources.contains(resource) && (updatedResources == null || updatedResources.contains(resource)))
      {
        if ("ecore".equals(resource.getURI().fileExtension()))
        {
          it.remove();
        }
        else
        {
          resource.unload();
        }
      }
      else
      {
        retainedResources.add(resource);
      }
    }

    resources.remove(selectionResource);

    if (updatedResources == null)
    {
      ECFURIHandlerImpl.clearExpectedETags();
      resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITH_ETAG_CHECKING);
      resourceSet.getPackageRegistry().clear();
      loadIndex();
    }
    else
    {
      Set<URI> uris = new LinkedHashSet<URI>();
      for (Resource resource : resources)
      {
        uris.add(resource.getURI());
      }

      loadIndex(uris.toArray(new URI[uris.size()]));
    }

    for (Resource resource : excludedResources)
    {
      EcoreUtil.resolveAll(resource);
    }

    for (Resource resource : retainedResources)
    {
      EcoreUtil.resolveAll(resource);
    }

  }

  public void loadIndex()
  {
    loadIndex(SetupContext.INDEX_SETUP_URI, SetupContext.USER_SETUP_URI);
  }

  protected void loadIndex(URI... uris)
  {
    if (indexLoader == null)
    {
      indexLoader = new ProgressMonitorDialogIndexLoader();
      indexLoader.setWizard(this);
    }

    indexLoader.loadIndex(resourceSet, uris);
  }

  protected void indexLoaded(Index index)
  {
  }

  @Override
  public void dispose()
  {
    super.dispose();

    adapterFactory.dispose();
  }

  /**
   * @author Eike Stepper
   */
  public static abstract class IndexLoader
  {
    private SetupWizard wizard;

    private boolean reloading;

    private boolean reloaded;

    final void setWizard(SetupWizard wizard)
    {
      this.wizard = wizard;
    }

    public final SetupWizard getWizard()
    {
      return wizard;
    }

    public abstract void loadIndex(ResourceSet resourceSet, URI... uris);

    protected final void loadIndex(final ResourceSet resourceSet, final URI[] uris, IProgressMonitor monitor) throws InvocationTargetException,
        InterruptedException
    {
      ResourceMirror resourceMirror = new ResourceMirror(resourceSet)
      {
        @Override
        protected void run(String taskName, IProgressMonitor monitor)
        {
          perform(uris);
        }
      };

      resourceMirror.begin(monitor);

      if (resourceMirror.isCanceled())
      {
        Display display = wizard.getShell().getDisplay();
        display.asyncExec(new Runnable()
        {
          public void run()
          {
            resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
            Set<URI> uris = new LinkedHashSet<URI>();
            for (Resource resource : resourceSet.getResources())
            {
              URI uri = resource.getURI();
              if (!"ecore".equals(uri.fileExtension()))
              {
                uris.add(resource.getURI());
              }
            }

            loadIndex(resourceSet, uris.toArray(new URI[uris.size()]));
          }
        });
      }
      else
      {
        EcoreUtil.resolveAll(resourceSet);

        Resource resource = resourceSet.getResource(SetupContext.INDEX_SETUP_URI, false);
        final Index index = (Index)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.INDEX);

        Display display = wizard.getShell().getDisplay();
        display.asyncExec(new Runnable()
        {
          public void run()
          {
            indexLoaded(index);

            if (wizard.indexLoadedAction != null)
            {
              try
              {
                wizard.indexLoadedAction.run();
              }
              catch (Exception ex)
              {
                SetupUIPlugin.INSTANCE.log(ex);
              }
            }

            // If we're currently reloading...
            if (reloading)
            {
              // Then we're done now.
              reloading = false;
              reloaded = true;
            }
            else
            {
              final Shell shell = wizard.getShell();

              // We will be testing if any remote resources have changed.
              reloading = true;

              new Thread()
              {
                @Override
                public void run()
                {
                  // Collect a map of the remote URIs.
                  Map<EClass, Set<URI>> uriMap = new LinkedHashMap<EClass, Set<URI>>();
                  final Map<URI, Resource> resourceMap = new LinkedHashMap<URI, Resource>();
                  URIConverter uriConverter = resourceSet.getURIConverter();
                  for (Resource resource : resourceSet.getResources())
                  {
                    EList<EObject> contents = resource.getContents();
                    if (!contents.isEmpty())
                    {
                      // Allow subclasses to override which types of objects are of interest for reloading.
                      // The simple installer is only interested in the index, products, and product catalogs.
                      EClass eClass = contents.get(0).eClass();
                      if (shouldReload(eClass))
                      {
                        // If the scheme is remote...
                        URI uri = uriConverter.normalize(resource.getURI());
                        String scheme = uri.scheme();
                        if ("http".equals(scheme) || "https".equals(scheme))
                        {
                          // Group the URIs by object type so we can reload "the most import" types of objects first.
                          CollectionUtil.add(uriMap, eClass, uri);
                          resourceMap.put(uri, resource);
                        }
                      }
                    }
                  }

                  // Collect the URIs is order of importance.
                  Set<URI> resourceURIs = new LinkedHashSet<URI>();
                  for (EClass eClass : new EClass[] { SetupPackage.Literals.INDEX, SetupPackage.Literals.PRODUCT_CATALOG, SetupPackage.Literals.PRODUCT,
                      SetupPackage.Literals.PROJECT_CATALOG, SetupPackage.Literals.PROJECT })
                  {
                    Set<URI> uris = uriMap.remove(eClass);
                    if (uris != null)
                    {
                      resourceURIs.addAll(uris);
                    }
                  }

                  for (Set<URI> uris : uriMap.values())
                  {
                    resourceURIs.addAll(uris);
                  }

                  // If there are resources to consider...
                  if (!resourceURIs.isEmpty())
                  {
                    // Remember which resource actually need updating based on detected remote changes by the ETag mirror.
                    final Set<Resource> updatedResources = new HashSet<Resource>();
                    new ECFURIHandlerImpl.ETagMirror()
                    {
                      @Override
                      protected synchronized void cacheUpdated(URI uri)
                      {
                        updatedResources.add(resourceMap.get(uri));
                      }
                    }.begin(resourceURIs, new NullProgressMonitor());

                    // If our shell is still available and we have updated resources...
                    if (!shell.isDisposed() && !updatedResources.isEmpty())
                    {
                      shell.getDisplay().asyncExec(new Runnable()
                      {
                        public void run()
                        {
                          // If any of hte updated resources change a model, then reload the entire index.
                          for (Resource resource : updatedResources)
                          {
                            if (resource.getContents().get(0) instanceof EPackage)
                            {
                              wizard.reloadIndex();
                              return;
                            }
                          }

                          // Otherwise reload only the affected resources.
                          wizard.reloadIndex(updatedResources);
                        }
                      });
                    }
                    else
                    {
                      // Otherwise we're done reloading.
                      reloading = false;
                      reloaded = true;
                    }
                  }
                }
              }.start();
            }
          }
        });
      }
    }

    protected void indexLoaded(final Index index)
    {
      wizard.indexLoaded(index);
    }

    protected boolean shouldReload(EClass eClass)
    {
      return true;
    }

    public void awaitIndexLoad()
    {
      if (reloading && !reloaded)
      {
        try
        {
          waiting();

          Shell shell = wizard.getShell();
          Display display = shell.getDisplay();
          while (!reloaded)
          {
            if (!display.isDisposed() && !display.readAndDispatch())
            {
              display.sleep();
            }
          }
        }
        finally
        {
          finishedWaiting();
        }
      }
    }

    protected void waiting()
    {
      Shell shell = wizard.getShell();
      Display display = shell.getDisplay();
      shell.setCursor(display.getSystemCursor(SWT.CURSOR_WAIT));
    }

    protected void finishedWaiting()
    {
      Shell shell = wizard.getShell();
      Display display = shell.getDisplay();
      shell.setCursor(display.getSystemCursor(SWT.CURSOR_ARROW));
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class ProgressMonitorDialogIndexLoader extends IndexLoader
  {
    @Override
    public void loadIndex(final ResourceSet resourceSet, final URI... uris)
    {
      Shell shell = getWizard().getShell();
      ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell);

      try
      {
        progressMonitorDialog.run(true, true, new IRunnableWithProgress()
        {
          public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
          {
            loadIndex(resourceSet, uris, monitor);
          }
        });
      }
      catch (InvocationTargetException ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
      }
      catch (InterruptedException ex)
      {
        // Ignore.
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Installer extends SetupWizard
  {
    public static final P2ServiceUI SERVICE_UI = new P2ServiceUI();

    public Installer()
    {
      setTrigger(Trigger.BOOTSTRAP);
      getResourceSet().getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
      setSetupContext(SetupContext.createUserOnly(getResourceSet()));
      setWindowTitle("Eclipse Installer");
    }

    @Override
    public String getHelpPath()
    {
      return HELP_FOLDER + "DocInstallWizard.html";
    }

    @Override
    public void addPages()
    {
      addPage(new ProductPage());
      addPage(new ProjectPage());
      super.addPages();

      getShell().getDisplay().timerExec(500, new Runnable()
      {
        public void run()
        {
          loadIndex();
        }
      });
    }

    @Override
    protected void indexLoaded(Index index)
    {
      getCatalogManager().indexLoaded(index);
    }

    @Override
    public void setPerformer(SetupTaskPerformer performer)
    {
      super.setPerformer(performer);

      if (performer != null)
      {
        performer.put(UIServices.class, SERVICE_UI);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Importer extends SetupWizard implements IImportWizard
  {
    public Importer()
    {
      setTrigger(Trigger.MANUAL);
      setSetupContext(SetupContext.create(getResourceSet()));
      setWindowTitle("Eclipse Importer");
    }

    @Override
    public String getHelpPath()
    {
      return HELP_FOLDER + "DocImportWizard.html";
    }

    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
    }

    @Override
    public void addPages()
    {
      addPage(new ProjectPage());
      super.addPages();

      UIUtil.getDisplay().timerExec(500, new Runnable()
      {
        public void run()
        {
          getResourceSet().getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
          loadIndex();
        }
      });
    }

    @Override
    protected void indexLoaded(Index index)
    {
      getCatalogManager().indexLoaded(index);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Updater extends SetupWizard
  {
    public Updater(boolean manual)
    {
      setTrigger(manual ? Trigger.MANUAL : Trigger.STARTUP);
      setSetupContext(SetupContext.create(getResourceSet()));
      setWindowTitle("Eclipse Updater");
    }

    public Updater(SetupTaskPerformer performer)
    {
      super(performer);
    }

    public Updater(SetupContext setupContext)
    {
      super(setupContext);
    }

    @Override
    public String getHelpPath()
    {
      return HELP_FOLDER + "DocUpdateWizard.html";
    }
  }
}
