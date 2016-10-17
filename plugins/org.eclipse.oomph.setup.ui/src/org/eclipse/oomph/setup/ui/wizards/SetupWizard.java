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
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.base.Annotation;
import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.ui.OomphTransferDelegate;
import org.eclipse.oomph.p2.internal.core.CacheUsageConfirmer;
import org.eclipse.oomph.p2.internal.ui.CacheUsageConfirmerUI;
import org.eclipse.oomph.setup.AnnotationConstants;
import org.eclipse.oomph.setup.Configuration;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Scope;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.CatalogManager;
import org.eclipse.oomph.setup.internal.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.setup.internal.core.util.IndexManager;
import org.eclipse.oomph.setup.internal.core.util.ResourceMirror;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.p2.util.P2TaskUISevices;
import org.eclipse.oomph.setup.ui.P2TaskUIServicesPrompter;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.SetupTransferSupport;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.SetupWizardPage.WizardFinisher;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.OS;

import org.eclipse.emf.common.ui.ImageURIRegistry;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.IPageChangeProvider;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
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

  private SetupTransferSupport setupTransferSupport;

  private Configuration configuration;

  private final List<Resource> configurationResources = new ArrayList<Resource>();

  private SetupContext setupContext;

  private SetupTaskPerformer performer;

  private String vmPath;

  private OS os;

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
      setPerformer(performer);
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

  public SetupTransferSupport getTransferSupport()
  {
    if (setupTransferSupport == null)
    {
      setupTransferSupport = new SetupTransferSupport();
    }

    return setupTransferSupport;
  }

  public Collection<? extends Resource> getConfigurationResources()
  {
    return new ArrayList<Resource>(configurationResources);
  }

  public void setConfigurationResources(Collection<? extends Resource> configurationResources)
  {
    if (!this.configurationResources.equals(new ArrayList<Resource>(configurationResources)))
    {
      configuration = null;
      this.configurationResources.clear();
      this.configurationResources.addAll(configurationResources);
    }
  }

  public Configuration getConfiguration()
  {
    if (configuration == null)
    {
      ResourceSet resourceSet = getResourceSet();
      for (Resource resource : configurationResources)
      {
        if (!resource.isLoaded())
        {
          URI uri = resource.getURI();
          if ("zip".equals(uri.fileExtension()))
          {
            configuration = SetupFactory.eINSTANCE.createConfiguration();
            reloadIndex(URI.createURI("archive:" + uri + "!/"));
            return configuration;
          }
          else if (SetupContext.INDEX_SETUP_NAME.equals(uri.lastSegment()))
          {
            configuration = SetupFactory.eINSTANCE.createConfiguration();
            reloadIndex(uri);
            return configuration;
          }

          Resource localResource = resourceSet.getResource(uri, false);
          if (localResource != null)
          {
            resourceSet.getResources().remove(localResource);
          }

          resourceSet.getResources().add(resource);
          try
          {
            resourceSet.getResource(uri, true);
          }
          catch (RuntimeException ex)
          {
            // Ignore.
          }

          Configuration configuration = (Configuration)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.CONFIGURATION);
          this.configuration = configuration;
          break;
        }

        final Configuration configuration = (Configuration)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.CONFIGURATION);
        if (configuration != null)
        {
          OomphTransferDelegate.TextTransferDelegate delegate = new OomphTransferDelegate.TextTransferDelegate()
          {
            {
              eObjects = Collections.<EObject> singleton(configuration);
            }
          };

          String xml = (String)delegate.getData();
          if (xml != null)
          {
            final URI syntheticConfigurationResourceURI = URI.createURI("dummy:/Configuration.setup");
            Resource localResource = resourceSet.getResource(syntheticConfigurationResourceURI, false);
            if (localResource != null)
            {
              resourceSet.getResources().remove(localResource);
            }

            localResource = resourceSet.createResource(syntheticConfigurationResourceURI);
            try
            {
              localResource.load(new URIConverter.ReadableInputStream(xml), resourceSet.getLoadOptions());
              Configuration localConfiguration = (Configuration)EcoreUtil.getObjectByType(localResource.getContents(), SetupPackage.Literals.CONFIGURATION);
              ResourceMirror resourceMirror = new ResourceMirror.WithProductImages(resourceSet)
              {
                @Override
                protected void run(String taskName, IProgressMonitor monitor)
                {
                  perform(syntheticConfigurationResourceURI);
                  resolveProxies();
                }

                @Override
                protected boolean await(long timeout) throws InterruptedException
                {
                  // We must do this so that if there is a password prompt for the configuration being loaded, the UI thread can display it.
                  Display display = getShell().getDisplay();
                  while (!super.await(500))
                  {
                    if (display.isDisposed())
                    {
                      break;
                    }

                    while (display.readAndDispatch())
                    {
                      // Keep processing events until there are none, in which case the display can sleep, but we can check the await status again.
                    }
                  }

                  return true;
                }
              };

              resourceMirror.begin(new NullProgressMonitor());

              this.configuration = localConfiguration;
              break;
            }
            catch (IOException ex)
            {
              // Ignore.
            }
          }
        }
      }
    }

    return configuration;
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

    if (performer != null)
    {
      performer.put(CacheUsageConfirmer.class, new CacheUsageConfirmerUI());
      performer.put(P2TaskUISevices.class, new P2TaskUIServicesPrompter());
    }
  }

  public String getVMPath()
  {
    return getOS().isCurrentOS() ? vmPath : null;
  }

  public void setVMPath(String vmPath)
  {
    this.vmPath = vmPath;
  }

  public OS getOS()
  {
    return os == null ? OS.INSTANCE : os;
  }

  public void setOS(OS os)
  {
    this.os = os;
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

  public boolean isSimple()
  {
    return simpleShell != null;
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
    IDialogSettings wizardSection = SetupUIPlugin.INSTANCE.getDialogSettings(SetupWizard.class.getSimpleName());
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

  public IWizardPage getCurrentPage()
  {
    IWizardContainer container = getContainer();
    if (container != null)
    {
      return container.getCurrentPage();
    }

    return null;
  }

  @Override
  public boolean canFinish()
  {
    IWizardPage currentPage = getCurrentPage();
    if (currentPage instanceof SetupWizardPage)
    {
      WizardFinisher wizardFinisher = ((SetupWizardPage)currentPage).getWizardFinisher();
      if (wizardFinisher != null)
      {
        return true;
      }
    }

    for (IWizardPage page : getPages())
    {
      if (!page.isPageComplete())
      {
        if (page instanceof ProgressPage && currentPage instanceof ConfirmationPage)
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
    IWizardPage currentPage = getCurrentPage();
    if (currentPage instanceof SetupWizardPage)
    {
      WizardFinisher wizardFinisher = ((SetupWizardPage)currentPage).getWizardFinisher();
      if (wizardFinisher != null)
      {
        return wizardFinisher.performFinish();
      }
    }

    if (currentPage instanceof ProgressPage)
    {
      clearStartupProperties();
      if (finishAction != null)
      {
        UIUtil.syncExec(finishAction);
      }

      return true;
    }

    if (currentPage instanceof SetupWizardPage)
    {
      ((SetupWizardPage)currentPage).gotoNextPage();
    }

    return false;
  }

  @Override
  public boolean performCancel()
  {
    for (IWizardPage page : getPages())
    {
      if (page instanceof SetupWizardPage)
      {
        SetupWizardPage setupWizardPage = (SetupWizardPage)page;
        if (!setupWizardPage.performCancel())
        {
          return false;
        }
      }
    }

    isCanceled = true;
    clearStartupProperties();

    return true;
  }

  public void sendStats(boolean success)
  {
    for (IWizardPage page : getPages())
    {
      if (page instanceof SetupWizardPage)
      {
        SetupWizardPage setupWizardPage = (SetupWizardPage)page;
        setupWizardPage.sendStats(success);
      }
    }
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

  public void reloadIndex(final URI indexLocationURI)
  {
    if (indexLoader == null)
    {
      indexLoader = new ProgressMonitorDialogIndexLoader();
      indexLoader.setWizard(this);
    }

    // Do this later so that the modal context of the progress dialog, if there is one, is within IndexLoader.awaitIndexLoad() event loop.
    UIUtil.asyncExec(getShell(), new Runnable()
    {
      public void run()
      {
        indexLoader.reloadIndex(indexLocationURI);
      }
    });
  }

  protected void reloadIndexResources(Set<Resource> updatedResources)
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

    boolean ecoreChanged = false;
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
          ecoreChanged = true;
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

    if (ecoreChanged)
    {
      for (Resource resource : retainedResources)
      {
        if ("ecore".equals(resource.getURI().fileExtension()))
        {
          resources.remove(resource);
        }
        else if (resource != selectionResource)
        {
          resource.unload();
        }
      }
    }

    if (updatedResources == null || ecoreChanged)
    {
      if (updatedResources == null)
      {
        ECFURIHandlerImpl.clearExpectedETags();
      }

      resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITH_ETAG_CHECKING);
      resourceSet.getPackageRegistry().clear();
      loadIndex(false, SetupContext.INDEX_SETUP_URI, SetupContext.USER_SETUP_URI);
    }
    else
    {
      Set<URI> uris = new LinkedHashSet<URI>();
      for (Resource resource : resources)
      {
        uris.add(resource.getURI());
      }

      loadIndex(false, uris.toArray(new URI[uris.size()]));
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
    loadIndex(true, SetupContext.INDEX_SETUP_URI, SetupContext.USER_SETUP_URI);
  }

  protected void loadIndex(final boolean configure, final URI... uris)
  {
    if (indexLoader == null)
    {
      indexLoader = new ProgressMonitorDialogIndexLoader();
      indexLoader.setWizard(this);
    }

    // Do this later so that the modal context of the progress dialog, if there is one, is within IndexLoader.awaitIndexLoad() event loop.
    UIUtil.asyncExec(getShell(), new Runnable()
    {
      public void run()
      {
        indexLoader.loadIndex(configure, resourceSet, uris);
      }
    });
  }

  protected void indexLoaded(Index index)
  {
    setSetupContext(SetupContext.createInstallationWorkspaceAndUser(resourceSet));
  }

  @Override
  public void dispose()
  {
    super.dispose();

    if (adapterFactory != null)
    {
      adapterFactory.dispose();
      adapterFactory = null;
    }
  }

  public static URI getBrandingSiteURI(Scope scope)
  {
    if (scope != null)
    {
      Annotation annotation = scope.getAnnotation(AnnotationConstants.ANNOTATION_BRANDING_INFO);
      if (annotation != null)
      {
        String detail = annotation.getDetails().get(AnnotationConstants.KEY_SITE_URI);
        if (detail != null)
        {
          return URI.createURI(detail);
        }
      }

      return getBrandingSiteURI(scope.getParentScope());
    }

    return null;
  }

  public static String getLocalBrandingImageURI(Scope scope)
  {
    try
    {
      URI imageURI = SetupCoreUtil.getBrandingImageURI(scope);
      return getImageURI(imageURI);
    }
    catch (Exception ex)
    {
      SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
    }

    URI imageURI = SetupCoreUtil.getEclipseBrandingImage();
    return getImageURI(imageURI);
  }

  public static Image getBrandingImage(Scope scope)
  {
    try
    {
      URI imageURI = SetupCoreUtil.getBrandingImageURI(scope);
      return getImage(imageURI);
    }
    catch (Exception ex)
    {
      SetupUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
    }

    URI imageURI = SetupCoreUtil.getEclipseBrandingImage();
    return getImage(imageURI);
  }

  private static String getImageURI(URI imageURI)
  {
    Image remoteImage = getImage(imageURI);
    return ImageURIRegistry.INSTANCE.getImageURI(remoteImage).toString();
  }

  private static Image getImage(URI imageURI)
  {
    Object image = BaseEditUtil.getImage(imageURI);
    return ExtendedImageRegistry.INSTANCE.getImage(image);
  }

  private static boolean hasModalChild(Shell parentShell, Shell excludedShell)
  {
    if (!parentShell.isDisposed())
    {
      for (Shell otherChildShell : parentShell.getShells())
      {
        if (otherChildShell.isVisible() && excludedShell != otherChildShell && (otherChildShell.getStyle() & SWT.APPLICATION_MODAL) != 0)
        {
          if (otherChildShell.getData("IndexLoaderDialogShell") != null)
          {
            otherChildShell.setVisible(false);
          }
          else
          {
            return true;
          }
        }
      }

      Composite grandParent = parentShell.getParent();
      if (grandParent instanceof Shell)
      {
        return hasModalChild((Shell)grandParent, parentShell);
      }
    }

    return false;
  }

  private static void setProgressMonitorVisible(final Shell shell, final ProgressMonitorDialog progressMonitorDialog, Runnable runnable)
  {
    // If the dialog shell is already missing or disposed, the loading is already completed, so no need to show in that case.
    Shell dialogShell = progressMonitorDialog.getShell();
    if (dialogShell != null && !dialogShell.isDisposed())
    {
      if (hasModalChild(shell, dialogShell))
      {
        UIUtil.timerExec(200, runnable);
      }
      else
      {
        dialogShell.setData("IndexLoaderDialogShell", progressMonitorDialog);
        dialogShell.setVisible(true);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static abstract class IndexLoader
  {
    private SetupWizard wizard;

    private volatile boolean loading = true;

    private volatile boolean reloading;

    private volatile boolean reloaded;

    protected boolean showETagProgress;

    final void setWizard(SetupWizard wizard)
    {
      this.wizard = wizard;
    }

    public final SetupWizard getWizard()
    {
      return wizard;
    }

    public abstract void loadIndex(IRunnableWithProgress runnable, int delay);

    public void loadIndex(final boolean configure, final ResourceSet resourceSet, final URI... uris)
    {
      loadIndex(new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          if (configure)
          {
            new IndexManager().configure(resourceSet);
          }

          loadIndex(resourceSet, uris, monitor);
        }
      }, 3000);
    }

    protected final void loadIndex(final ResourceSet resourceSet, final URI[] uris, IProgressMonitor monitor)
        throws InvocationTargetException, InterruptedException
    {
      loading = true;

      ResourceMirror resourceMirror = new ResourceMirror.WithProductImages(resourceSet)
      {
        @Override
        protected void run(String taskName, IProgressMonitor monitor)
        {
          perform(uris);
          resolveProxies();
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

            loadIndex(false, resourceSet, uris.toArray(new URI[uris.size()]));
          }
        });
      }
      else
      {
        Resource resource = resourceSet.getResource(SetupContext.INDEX_SETUP_URI, false);
        final Index index = (Index)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.INDEX);
        if (index == null)
        {
          logErrors(resource);
        }

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
              loading = false;
              reloading = false;
              reloaded = true;
            }
            else
            {
              final Shell shell = wizard.getShell();

              // We will be testing if any remote resources have changed.
              reloading = true;
              loading = false;

              UIUtil.asyncExec(new Runnable()
              {
                public void run()
                {
                  if (hasModalChild(shell, null))
                  {
                    UIUtil.timerExec(200, this);
                    return;
                  }

                  final ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell);
                  progressMonitorDialog.setOpenOnRun(false);
                  try
                  {
                    progressMonitorDialog.run(true, true, new IRunnableWithProgress()
                    {
                      public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                      {
                        // Collect a map of the remote URIs.
                        Map<EClass, Set<URI>> uriMap = new LinkedHashMap<EClass, Set<URI>>();
                        final Map<URI, Set<Resource>> resourceMap = new LinkedHashMap<URI, Set<Resource>>();
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
                              if (uri.isArchive())
                              {
                                String authority = uri.authority();
                                if (authority.startsWith("http:") || authority.startsWith("https:"))
                                {
                                  URI archiveURI = URI.createURI(authority.substring(0, authority.length() - 1));
                                  CollectionUtil.add(uriMap, eClass, archiveURI);
                                  CollectionUtil.add(resourceMap, archiveURI, resource);
                                }
                              }
                              else if ("http".equals(scheme) || "https".equals(scheme))
                              {
                                // Group the URIs by object type so we can reload "the most import" types of objects first.
                                CollectionUtil.add(uriMap, eClass, uri);
                                CollectionUtil.add(resourceMap, uri, resource);
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
                        int size = resourceURIs.size();
                        if (size != 0)
                        {
                          if (showETagProgress)
                          {
                            UIUtil.asyncExec(new Runnable()
                            {
                              public void run()
                              {
                                UIUtil.timerExec(1000, new Runnable()
                                {
                                  public void run()
                                  {
                                    setProgressMonitorVisible(shell, progressMonitorDialog, this);
                                  }
                                });
                              }
                            });
                          }

                          // Remember which resource actually need updating based on detected remote changes by the ETag mirror.
                          final Set<Resource> updatedResources = new HashSet<Resource>();
                          new ECFURIHandlerImpl.ETagMirror()
                          {
                            @Override
                            protected synchronized void cacheUpdated(URI uri)
                            {
                              Set<Resource> resources = resourceMap.get(uri);
                              if (resources != null)
                              {
                                updatedResources.addAll(resources);
                              }
                            }
                          }.begin(resourceURIs, monitor);

                          // If our shell is still available and we have updated resources...
                          if (!shell.isDisposed() && !updatedResources.isEmpty())
                          {
                            shell.getDisplay().asyncExec(new Runnable()
                            {
                              public void run()
                              {
                                // Reload only the affected resources.
                                wizard.reloadIndexResources(updatedResources);
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
                        else
                        {
                          // Otherwise we're done reloading.
                          reloading = false;
                          reloaded = true;
                        }
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
              });
            }
          }
        });
      }
    }

    public void reloadIndex(final URI indexLocationURI)
    {
      loadIndex(new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          if (indexLocationURI != null)
          {
            new IndexManager().configureForProxy(getWizard().getResourceSet(), indexLocationURI);
          }

          getWizard().reloadIndexResources(null);
        }
      }, 1000);
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
      if (loading || reloading && !reloaded)
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
      shell.setCursor(shell.getDisplay().getSystemCursor(SWT.CURSOR_WAIT));
    }

    protected void finishedWaiting()
    {
      Shell shell = wizard.getShell();
      shell.setCursor(null);
    }

    private void logErrors(Resource resource)
    {
      try
      {
        for (Diagnostic diagnostic : resource.getErrors())
        {
          try
          {
            if (diagnostic instanceof Throwable)
            {
              Throwable throwable = (Throwable)diagnostic;
              SetupUIPlugin.INSTANCE.log(throwable);
            }
            else
            {
              SetupUIPlugin.INSTANCE.log(diagnostic.getMessage(), IStatus.ERROR);
            }
          }
          catch (Exception ex)
          {
            //$FALL-THROUGH$
          }
        }
      }
      catch (Exception ex)
      {
        //$FALL-THROUGH$
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class ProgressMonitorDialogIndexLoader extends IndexLoader
  {
    public ProgressMonitorDialogIndexLoader()
    {
      showETagProgress = true;
    }

    @Override
    public void loadIndex(IRunnableWithProgress runnable, int delay)
    {
      final Shell shell = getWizard().getShell();
      final ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(shell);
      progressMonitorDialog.setOpenOnRun(false);

      try
      {
        // Delay showing the progress dialog for three seconds.
        UIUtil.timerExec(delay, new Runnable()
        {
          public void run()
          {
            setProgressMonitorVisible(shell, progressMonitorDialog, this);
          }
        });

        progressMonitorDialog.run(true, true, runnable);
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
  public static class Importer extends SetupWizard implements IImportWizard
  {
    public static final String WIZARD_ID = "org.eclipse.oomph.setup.ui.ImportWizard";

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
      Shell performingShell = SetupPropertyTester.getPerformingShell();
      if (performingShell != null)
      {
        String title = getWindowTitle();
        addPage(new ExistingProcessPage(title));
        return;
      }

      addPage(new ProjectPage(new SelectionMemento()));
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
      super.indexLoaded(index);
      getCatalogManager().indexLoaded(index);
    }

    /**
     * @author Eike Stepper
     */
    private static final class ExistingProcessPage extends WizardPage
    {
      public ExistingProcessPage(String title)
      {
        super("ExistingProcess");
        setTitle(title);
        setErrorMessage("The " + title + " cannot be opened because another setup process is already active.");
      }

      public void createControl(Composite parent)
      {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new GridLayout());
        setControl(container);

        GridData explanationGridData = new GridData(SWT.CENTER, SWT.BOTTOM, true, true);
        explanationGridData.widthHint = 320;

        Label explanation = new Label(container, SWT.WRAP);
        explanation.setLayoutData(explanationGridData);
        explanation.setText("Another setup process is already active. If the dialog of that other process is minimized you can see it in the status bar:");

        Label image = new Label(container, SWT.BORDER);
        image.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
        image.setImage(SetupUIPlugin.INSTANCE.getSWTImage("existing_process.png"));

        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        Label suggestion = new Label(container, SWT.WRAP);
        suggestion.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false));
        suggestion.setText("Complete that other process before importing projects.");

        Button button = new Button(container, SWT.NONE);
        button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
        button.setText("Open Existing Setup Process");
        button.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            String title = getTitle();
            String message = "Meanwhile the other setup process has finished.";

            Shell currentShell = getShell();
            Shell parentShell = (Shell)currentShell.getParent();
            currentShell.dispose();

            Shell performingShell = SetupPropertyTester.getPerformingShell();
            if (performingShell == null)
            {
              IWizardDescriptor descriptor = PlatformUI.getWorkbench().getImportWizardRegistry().findWizard(WIZARD_ID);
              if (descriptor == null)
              {
                MessageDialog.openInformation(parentShell, title, message);
              }
              else
              {
                if (MessageDialog.openQuestion(parentShell, title, message + "\nDo you want to open the " + title + " again?"))
                {
                  try
                  {
                    IWizard wizard = descriptor.createWizard();
                    WizardDialog wizardDialog = new WizardDialog(parentShell, wizard);
                    wizardDialog.setTitle(title);
                    wizardDialog.open();
                  }
                  catch (CoreException ex)
                  {
                    SetupUIPlugin.INSTANCE.log(ex);
                  }
                }
              }
            }
            else
            {
              boolean visible = !performingShell.isVisible();
              performingShell.setVisible(visible);

              if (SetupPropertyTester.getPerformingStatus() != null)
              {
                SetupPropertyTester.setPerformingShell(null);
              }

              if (visible)
              {
                performingShell.setFocus();
              }
            }
          }
        });

        new Label(container, SWT.NONE).setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, true));
        setPageComplete(false);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class Updater extends SetupWizard
  {
    protected boolean openInBackground;

    public Updater(boolean manual)
    {
      setTrigger(manual ? Trigger.MANUAL : Trigger.STARTUP);
      setSetupContext(SetupContext.create(getResourceSet()));
      setWindowTitle("Eclipse Updater");
    }

    public Updater(SetupTaskPerformer performer)
    {
      super(performer);
      openInBackground = true;
      setWindowTitle("Eclipse Updater");
    }

    public Updater(SetupContext setupContext)
    {
      super(setupContext);
      setWindowTitle("Eclipse Updater");
    }

    @Override
    public String getHelpPath()
    {
      return HELP_FOLDER + "DocUpdateWizard.html";
    }

    @Override
    public int openDialog(Shell parentShell)
    {
      if (openInBackground)
      {
        SetupWizardDialog dialog = new SetupWizardDialog(parentShell, this);
        return dialog.openInBackground();
      }

      return super.openDialog(parentShell);
    }

    public static void perform(boolean manual)
    {
      SetupWizard updater = new SetupWizard.Updater(manual)
      {
        @Override
        public void createPageControls(Composite pageContainer)
        {
          loadIndex();
          super.createPageControls(pageContainer);
        }
      };

      Shell shell = UIUtil.getShell();
      updater.openDialog(shell);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class SelectionMemento implements Externalizable
  {
    private URI productVersion;

    private List<URI> streams;

    public SelectionMemento()
    {
    }

    public URI getProductVersion()
    {
      return productVersion;
    }

    public void setProductVersion(URI productVersion)
    {
      this.productVersion = productVersion;
    }

    public List<URI> getStreams()
    {
      return streams;
    }

    public void setStreams(List<URI> streams)
    {
      this.streams = streams;
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
      if (productVersion != null)
      {
        out.writeBoolean(true);
        out.writeUTF(productVersion.toString());
      }
      else
      {
        out.writeBoolean(false);
      }

      if (streams != null)
      {
        out.writeInt(streams.size());
        for (URI stream : streams)
        {
          out.writeUTF(stream.toString());
        }
      }
      else
      {
        out.writeInt(-1);
      }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
      if (in.readBoolean())
      {
        productVersion = URI.createURI(in.readUTF());
      }

      int size = in.readInt();
      if (size != -1)
      {
        streams = new ArrayList<URI>();
        for (int i = 0; i < size; i++)
        {
          URI stream = URI.createURI(in.readUTF());
          streams.add(stream);
        }
      }
    }
  }
}
