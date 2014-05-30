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

import org.eclipse.oomph.internal.setup.core.SetupContext;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.internal.setup.core.util.CatalogManager;
import org.eclipse.oomph.internal.setup.core.util.ECFURIHandlerImpl;
import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.internal.setup.core.util.ResourceMirror;
import org.eclipse.oomph.setup.Index;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.BasicResourceHandler;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Eike Stepper
 */
public abstract class SetupWizard extends Wizard implements IPageChangedListener
{
  static boolean ecfWorkAround;

  private Trigger trigger;

  private Object lastPage;

  private ResourceSet resourceSet;

  private CatalogManager catalogManager;

  private SetupContext setupContext;

  private SetupTaskPerformer performer;

  private Runnable finishAction;

  private ComposedAdapterFactory adapterFactory;

  public SetupWizard()
  {
    this(null);
  }

  public SetupWizard(SetupTaskPerformer performer)
  {
    setWindowTitle(AbstractSetupDialog.SHELL_TEXT);
    setDefaultPageImageDescriptor(SetupUIPlugin.INSTANCE.getImageDescriptor("install_wiz"));
    setNeedsProgressMonitor(false);
    if (performer == null)
    {
      resourceSet = EMFUtil.createResourceSet();
      setTrigger(Trigger.STARTUP);
    }
    else
    {
      SetupWizard.this.performer = performer;
      setTrigger(performer.getTrigger());
      setSetupContext(performer.getSetupContext());
      resourceSet = performer.getUser().eResource().getResourceSet();
    }
  }

  public ComposedAdapterFactory getAdapterFactory()
  {
    if (adapterFactory == null)
    {
      adapterFactory = EMFUtil.createAdapterFactory();
    }

    return adapterFactory;
  }

  public void setTrigger(Trigger trigger)
  {
    SetupWizard.this.trigger = trigger;
  }

  public Trigger getTrigger()
  {
    return trigger;
  }

  public void setFinishAction(Runnable finishAction)
  {
    SetupWizard.this.finishAction = finishAction;
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

  public void setSetupContext(SetupContext setupContext)
  {
    SetupWizard.this.setupContext = setupContext;
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
    SetupWizard.this.performer = performer;
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
  public void addPages()
  {
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

    if (targetPage instanceof SetupWizardPage)
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
      ((SetupWizardPage)page).performCancel();
    }

    return true;
  }

  @Override
  public boolean performFinish()
  {
    if (finishAction != null)
    {
      UIUtil.asyncExec(finishAction);
    }

    return true;
  }

  public int openDialog(Shell parentShell)
  {
    WizardDialog dialog = new WizardDialog(parentShell, this);
    return dialog.open();
  }

  protected void reloadIndex()
  {
    Resource selectionResource = getCatalogManager().getSelection().eResource();
    Set<Resource> excludedResources = new HashSet<Resource>();
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

    EList<Resource> resources = resourceSet.getResources();
    for (Resource resource : resources)
    {
      if (!excludedResources.contains(resource))
      {
        resource.unload();
      }
    }

    resources.remove(selectionResource);

    ECFURIHandlerImpl.clearExpectedETags();
    resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITH_ETAG_CHECKING);
    resourceSet.getPackageRegistry().clear();
    loadIndex();
  }

  protected void loadIndex()
  {
    loadIndex(SetupContext.INDEX_SETUP_URI, SetupContext.USER_SETUP_URI);
  }

  protected void loadIndex(final URI... uris)
  {
    ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(getShell());
    try
    {
      progressMonitorDialog.run(true, true, new IRunnableWithProgress()
      {
        public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          final EList<Resource> resources = resourceSet.getResources();

          final String taskName = resourceSet.getLoadOptions().get(ECFURIHandlerImpl.OPTION_CACHE_HANDLING) == ECFURIHandlerImpl.CacheHandling.CACHE_WITHOUT_ETAG_CHECKING ? "Loading from local cache "
              : "Loading from internet ";
          monitor.beginTask(taskName, resources.size() < 3 ? IProgressMonitor.UNKNOWN : resources.size());

          final AtomicInteger counter = new AtomicInteger(1);
          final AtomicBoolean mirrorCanceled = new AtomicBoolean();
          final ResourceMirror resourceMirror = new ResourceMirror(resourceSet);

          XMLResource.ResourceHandler resourceHandler = new BasicResourceHandler()
          {
            @Override
            public synchronized void preLoad(XMLResource resource, InputStream inputStream, Map<?, ?> options)
            {
              synchronized (resourceSet)
              {
                monitor.subTask("Loading " + resource.getURI());
                monitor.worked(1);
                monitor.setTaskName(taskName + counter.getAndIncrement() + " of " + resources.size());
                if (monitor.isCanceled() && !mirrorCanceled.get())
                {
                  mirrorCanceled.set(true);
                  resourceMirror.cancel();
                }
              }
            }
          };

          resourceSet.getLoadOptions().put(XMLResource.OPTION_RESOURCE_HANDLER, resourceHandler);
          resourceMirror.mirror(uris);
          resourceSet.getLoadOptions().remove(XMLResource.OPTION_RESOURCE_HANDLER);

          resourceMirror.dispose();

          if (mirrorCanceled.get())
          {
            getShell().getDisplay().asyncExec(new Runnable()
            {
              public void run()
              {
                resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITHOUT_ETAG_CHECKING);
                Set<URI> uris = new LinkedHashSet<URI>();
                for (Resource resource : resources)
                {
                  uris.add(resource.getURI());
                }

                loadIndex(uris.toArray(new URI[uris.size()]));
              }
            });

          }
          else
          {
            Resource resource = resourceSet.getResource(SetupContext.INDEX_SETUP_URI, false);
            final Index index = (Index)EcoreUtil.getObjectByType(resource.getContents(), SetupPackage.Literals.INDEX);
            if (index != null)
            {
              getShell().getDisplay().asyncExec(new Runnable()
              {
                public void run()
                {
                  indexLoaded(index);
                }
              });
            }
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
  public static class Installer extends SetupWizard
  {
    public Installer()
    {
      super();
      setTrigger(Trigger.BOOTSTRAP);
      setSetupContext(SetupContext.createUserOnly(getResourceSet()));
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
  public static class Importer extends SetupWizard implements IImportWizard
  {
    public Importer()
    {
      setTrigger(Trigger.MANUAL);
      setSetupContext(SetupContext.create(getResourceSet()));
    }

    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
      setWindowTitle("Setup Import Wizard");
    }

    @Override
    public void addPages()
    {
      addPage(new ProjectPage());
      super.addPages();
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
    }

    public Updater(SetupTaskPerformer performer)
    {
      super(performer);
    }
  }
}
