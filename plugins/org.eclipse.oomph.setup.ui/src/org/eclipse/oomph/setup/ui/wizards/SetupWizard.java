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
import org.eclipse.oomph.p2.internal.ui.P2ServiceUI;
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
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

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
    this((SetupTaskPerformer)null);
  }

  public SetupWizard(SetupTaskPerformer performer)
  {
    initUI();
    if (performer == null)
    {
      resourceSet = EMFUtil.createResourceSet();
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
    setWindowTitle(AbstractSetupDialog.SHELL_TEXT);
    setDefaultPageImageDescriptor(SetupUIPlugin.INSTANCE.getImageDescriptor("install_wiz.png"));
    setNeedsProgressMonitor(false);
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
    this.trigger = trigger;
  }

  public Trigger getTrigger()
  {
    return trigger;
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
    Set<Resource> excludedResources = new HashSet<Resource>();

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

    EList<Resource> resources = resourceSet.getResources();
    for (Iterator<Resource> it = resources.iterator(); it.hasNext();)
    {
      Resource resource = it.next();
      if (!excludedResources.contains(resource))
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
    }

    resources.remove(selectionResource);

    ECFURIHandlerImpl.clearExpectedETags();
    resourceSet.getLoadOptions().put(ECFURIHandlerImpl.OPTION_CACHE_HANDLING, ECFURIHandlerImpl.CacheHandling.CACHE_WITH_ETAG_CHECKING);
    resourceSet.getPackageRegistry().clear();
    loadIndex();

    for (Resource resource : excludedResources)
    {
      EcoreUtil.resolveAll(resource);
    }
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
          ResourceMirror resourceMirror = new ResourceMirror.WithProgress(resourceSet, monitor)
          {
            @Override
            public void run()
            {
              mirror(uris);
            }
          };

          if (resourceMirror.isCanceled())
          {
            getShell().getDisplay().asyncExec(new Runnable()
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
    public static final P2ServiceUI SERVICE_UI = new P2ServiceUI();

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

    public Updater(SetupContext setupContext)
    {
      super(setupContext);
    }
  }
}
