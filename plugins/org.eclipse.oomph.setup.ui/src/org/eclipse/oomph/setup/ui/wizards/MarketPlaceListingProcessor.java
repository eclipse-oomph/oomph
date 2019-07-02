/*
 * Copyright (c) 2019 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.setup.Argument;
import org.eclipse.oomph.setup.Installation;
import org.eclipse.oomph.setup.Macro;
import org.eclipse.oomph.setup.MacroTask;
import org.eclipse.oomph.setup.Parameter;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.SetupTaskContainer;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.p2.util.MarketPlaceListing;
import org.eclipse.oomph.setup.provider.SetupTaskContainerItemProvider;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.ToolTipLabelProvider;
import org.eclipse.oomph.ui.BackgroundProgressPart;
import org.eclipse.oomph.ui.OomphDialog;
import org.eclipse.oomph.ui.StatusDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.ui.DiagnosticComposite;
import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMIException;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.ProgressMonitorWrapper;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ed Merks
 */
public class MarketPlaceListingProcessor
{
  protected final Shell shell;

  protected final SetupWizard setupWizard;

  protected final Resource marketPlaceListingResource;

  protected final MarketPlaceListing marketPlaceListing;

  private final MarketPlaceListingStatus status;

  private boolean hasOptionalFeatures;

  public MarketPlaceListingProcessor(SetupWizard setupWizard)
  {
    this.setupWizard = setupWizard;
    Resource marketPlaceListingResource = null;
    MarketPlaceListing marketPlaceListing = null;
    URI uri = URI.createURI("unknown");
    for (Resource resource : setupWizard.getUnappliedConfigurationResources())
    {
      uri = resource.getURI();
      if (MarketPlaceListing.isMarketPlaceListing(uri))
      {
        marketPlaceListingResource = resource;
        marketPlaceListing = MarketPlaceListing.getMarketPlaceListing(uri, setupWizard.getResourceSet().getURIConverter());
        break;
      }
    }

    this.marketPlaceListing = marketPlaceListing;
    this.marketPlaceListingResource = marketPlaceListingResource;

    status = new MarketPlaceListingStatus("Problems were encountered processing the marketplace listing for '" + uri + "'");
    shell = this.setupWizard.getShell();
  }

  public MarketPlaceListingProcessor(Shell shell, MarketPlaceListing marketPlaceListing, Resource marketPlaceListingResource)
  {
    this.marketPlaceListing = marketPlaceListing;
    this.marketPlaceListingResource = marketPlaceListingResource;
    this.shell = shell;

    setupWizard = null;
    status = new MarketPlaceListingStatus("Problems were encountered processing the marketplace listing for '" + marketPlaceListingResource.getURI() + "'");
  }

  public boolean isMarketPlaceListing()
  {
    return marketPlaceListing != null;
  }

  public IStatus getStatus()
  {
    status.computeSeverity();

    int okCount = 0;
    IStatus result = null;
    for (IStatus status : status.getChildren())
    {
      if (status.isOK())
      {
        ++okCount;
      }
      else
      {
        result = status;
      }
    }

    if (okCount == 1)
    {
      return result;
    }

    return status;
  }

  public boolean processMarketPlaceListing()
  {
    if (marketPlaceListing == null)
    {
      if (marketPlaceListingResource != null)
      {
        status.add(createResourceStatus(Collections.singleton(marketPlaceListingResource), SetupPackage.Literals.MACRO));
      }

      return false;
    }

    if (!marketPlaceListingResource.getErrors().isEmpty())
    {
      status.add(createResourceStatus(Collections.singleton(marketPlaceListingResource), SetupPackage.Literals.MACRO));
      return false;
    }

    if (marketPlaceListing.getUpdateSite() == null)
    {
      status.add(new Status(IStatus.ERROR, SetupUIPlugin.PLUGIN_ID, "The listing does not include an update site"));
      return false;
    }

    if (marketPlaceListing.getRequirements().isEmpty())
    {
      status.add(new Status(IStatus.ERROR, SetupUIPlugin.PLUGIN_ID, "The listing does not specify any installable units"));
      return false;
    }

    Point size = getSize();
    final Set<Requirement> checkedRequirements = new LinkedHashSet<Requirement>();
    OomphDialog marketPlaceListingDialog = new OomphDialog(shell, "", size.x, size.y, SetupUIPlugin.INSTANCE, false)
    {
      private ProgressMonitorPart progressMonitorPart;

      private Job repositoryLoaderJob;

      private CheckboxTableViewer requirementsViewer;

      private ICheckStateListener checkStateListener;

      {
        setShellStyle(SWT.SHELL_TRIM | SWT.BORDER | SWT.APPLICATION_MODAL | SWT.ON_TOP);
      }

      @Override
      protected String getShellText()
      {
        return "Marketplace Install";
      }

      @Override
      protected String getImagePath()
      {
        return "marketplace_banner.png";
      }

      @Override
      protected String getDefaultMessage()
      {
        return "Choose the features to install.";
      }

      @Override
      protected void createUI(final Composite parent)
      {
        setTitle(marketPlaceListing.getLabel());

        UIUtil.asyncExec(parent, new Runnable()
        {
          public void run()
          {
            getButton(IDialogConstants.OK_ID).setEnabled(false);
          }
        });

        List<Requirement> requirements = marketPlaceListing.getRequirements();
        final Set<Requirement> requiredRequirements = new HashSet<Requirement>();
        Set<Requirement> selectedRequirements = new HashSet<Requirement>();
        final Map<Requirement, IInstallableUnit> installableUnits = new LinkedHashMap<Requirement, IInstallableUnit>();
        for (Requirement requirement : requirements)
        {
          installableUnits.put(requirement, null);
          if (MarketPlaceListing.isRequired(requirement))
          {
            requiredRequirements.add(requirement);
            selectedRequirements.add(requirement);
          }
          else if (isSelected(requirement))
          {
            selectedRequirements.add(requirement);
          }
        }

        hasOptionalFeatures = requiredRequirements.size() < requirements.size();

        requirementsViewer = CheckboxTableViewer.newCheckList(parent, SWT.MULTI);
        requirementsViewer.getTable().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        final Font tableFont = requirementsViewer.getTable().getFont();
        final Font boldFont = ExtendedFontRegistry.INSTANCE.getFont(tableFont, IItemFontProvider.BOLD_FONT);
        final Font disabledFont = ExtendedFontRegistry.INSTANCE.getFont(tableFont, IItemFontProvider.ITALIC_FONT);
        final Font disabledBoldFont = ExtendedFontRegistry.INSTANCE.getFont(tableFont, IItemFontProvider.BOLD_ITALIC_FONT);
        ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

        ColumnViewerInformationControlToolTipSupport toolTipSupport = new ColumnViewerInformationControlToolTipSupport(requirementsViewer,
            new LocationListener()
            {
              public void changing(LocationEvent event)
              {
                if (!"about:blank".equals(event.location))
                {
                  OS.INSTANCE.openSystemBrowser(event.location);
                  event.doit = false;
                }
              }

              public void changed(LocationEvent event)
              {
              }
            });

        checkStateListener = new ICheckStateListener()
        {
          public void checkStateChanged(CheckStateChangedEvent event)
          {
            checkedRequirements.clear();
            for (Requirement requirement : requiredRequirements)
            {
              requirementsViewer.setChecked(requirement, true);
            }

            for (Object object : requirementsViewer.getCheckedElements())
            {
              if (installableUnits.get(object) != null)
              {
                checkedRequirements.add((Requirement)object);
              }
            }

            getButton(IDialogConstants.OK_ID).setEnabled(!checkedRequirements.isEmpty());
          }
        };
        requirementsViewer.addCheckStateListener(checkStateListener);

        class RequirementLabelProvider extends ToolTipLabelProvider
        {
          private RequirementLabelProvider(ComposedAdapterFactory adapterFactory, ColumnViewerInformationControlToolTipSupport toolTipSupport)
          {
            super(adapterFactory, toolTipSupport);
          }

          @Override
          public String getText(Object object)
          {
            IInstallableUnit installableUnit = installableUnits.get(object);
            if (installableUnit != null)
            {
              String name = installableUnit.getProperty(IInstallableUnit.PROP_NAME, null);
              if (name != null)
              {
                return name;
              }
            }

            return super.getText(object);
          }

          @Override
          public Image getImage(Object object)
          {
            Image result = super.getImage(object);
            IInstallableUnit installableUnit = installableUnits.get(object);
            if (installableUnit == null)
            {
              return ExtendedImageRegistry.INSTANCE.getImage(ImageDescriptor.createWithFlags(ImageDescriptor.createFromImage(result), SWT.IMAGE_DISABLE));
            }

            return result;
          }

          @Override
          public Font getFont(Object object)
          {
            if (installableUnits.get(object) == null)
            {
              return requiredRequirements.contains(object) ? disabledBoldFont : disabledFont;
            }

            return requiredRequirements.contains(object) ? boldFont : null;
          }

          @Override
          public String getToolTipText(Object element)
          {
            IInstallableUnit installableUnit = installableUnits.get(element);
            if (installableUnit != null)
            {
              String localBrandingImageURI = installableUnit.getProperty(IInstallableUnit.PROP_ICON);
              String brandingSiteURI = installableUnit.getProperty(IInstallableUnit.PROP_DOC_URL);
              if (brandingSiteURI == null)
              {
                brandingSiteURI = marketPlaceListing.getListing().toString();
              }

              String label = installableUnit.getProperty(IInstallableUnit.PROP_NAME, null);

              StringBuilder result = new StringBuilder();
              result.append("<span style='white-space: nowrap; font-size: 150%;'><b>");
              if (brandingSiteURI != null)
              {
                result.append("<a style='text-decoration: none; color: inherit;' href='");
                result.append(brandingSiteURI);
                result.append("'>");
              }

              if (localBrandingImageURI != null)
              {
                result.append("<img style='padding-top: 4px;' src='");
                result.append(localBrandingImageURI);
                result.append("' width='42' height='42' align='absmiddle'/>&nbsp;");
              }

              result.append(DiagnosticDecorator.escapeContent(label).replace(" ", "&nbsp;"));
              result.append("</b></span>");

              if (brandingSiteURI != null)
              {
                result.append("</a>");
              }

              String description = installableUnit.getProperty(IInstallableUnit.PROP_DESCRIPTION, null);
              if (!StringUtil.isEmpty(description))
              {
                result.append("<br/>");
                result.append("<span style='font-size: 50%;'><br/></span>");
                result.append(description);
                result.append("<br/>");
              }

              // Add extra invisible lines to convince the tool tip size calculation that the text is 3 lines longer.
              // result.append("<div style='height=0px; display:none;'>&nbsp;&nbsp;&nbsp;&nbps;&nbps;&nbps;&nbps;&nbps;&nbps;&nbps;<br/><br/></br></div>");
              result.append("<div style='height=0px; display:none;'>&nbps;&nbps;&nbps;&nbps;&nbps;<br/><br/></br></div>");

              return result.toString();
            }

            return "Loading...";
          }
        }

        requirementsViewer.setContentProvider(new ArrayContentProvider());
        requirementsViewer.setLabelProvider(new RequirementLabelProvider(adapterFactory, toolTipSupport));

        requirementsViewer.setInput(installableUnits.keySet());
        requirementsViewer.setCheckedElements(selectedRequirements.toArray());

        Composite progressArea = new Composite(parent, SWT.BORDER);
        FillLayout fillLayout = new FillLayout();
        fillLayout.marginHeight = 5;
        fillLayout.marginWidth = 5;
        progressArea.setLayout(fillLayout);
        progressMonitorPart = new BackgroundProgressPart(progressArea, null, true);

        final GridData progressAreaGridData = new GridData(GridData.FILL_HORIZONTAL);
        progressArea.setLayoutData(progressAreaGridData);

        repositoryLoaderJob = new Job("Repository Loader")
        {
          @Override
          protected IStatus run(IProgressMonitor monitor)
          {
            ProgressMonitorWrapper progressMonitorWrapper = new ProgressMonitorWrapper(monitor)
            {
              @Override
              public void beginTask(String name, int totalWork)
              {
                super.beginTask(name, totalWork);
                progressMonitorPart.beginTask(name, totalWork);
              }

              @Override
              public void done()
              {
                super.done();
                progressMonitorPart.done();
              }

              @Override
              public void setTaskName(String name)
              {
                super.setTaskName(name);
                progressMonitorPart.setTaskName(name);
              }

              @Override
              public void subTask(String name)
              {
                super.subTask(name);
                progressMonitorPart.subTask(name);
              }

              @Override
              public void worked(int work)
              {
                super.worked(work);
                progressMonitorPart.worked(work);
              }

              @Override
              public boolean isCanceled()
              {
                return super.isCanceled() || progressMonitorPart.isCanceled();
              }

              @Override
              public void setCanceled(boolean canceled)
              {
                super.setCanceled(canceled);
                progressMonitorPart.setCanceled(canceled);
              }

              @Override
              public void clearBlocked()
              {
                super.clearBlocked();
                progressMonitorPart.clearBlocked();
              }

              @Override
              public void internalWorked(double work)
              {
                super.internalWorked(work);
                progressMonitorPart.internalWorked(work);
              }

              @Override
              public void setBlocked(IStatus reason)
              {
                super.setBlocked(reason);
                progressMonitorPart.setBlocked(reason);
              }
            };

            String updateSite = marketPlaceListing.getUpdateSite().toString();
            IMetadataRepositoryManager metadataRepositoryManager = P2Util.getAgentManager().getCurrentAgent().getMetadataRepositoryManager();
            List<java.net.URI> originalKnownRepositories = Arrays.asList(metadataRepositoryManager.getKnownRepositories(IRepositoryManager.REPOSITORIES_ALL));
            try
            {
              final IMetadataRepository repository = metadataRepositoryManager.loadRepository(new java.net.URI(updateSite), progressMonitorWrapper);
              progressMonitorWrapper.done();
              UIUtil.asyncExec(parent, new Runnable()
              {
                public void run()
                {
                  handleRepository(repository, installableUnits);
                  requirementsViewer.refresh();
                  checkStateListener.checkStateChanged(null);
                }
              });
            }
            catch (final ProvisionException ex)
            {
              UIUtil.asyncExec(parent, new Runnable()
              {
                public void run()
                {
                  new StatusDialog(parent.getShell(), "Problems", "Problem Loading Repository",
                      new Status(IStatus.ERROR, SetupUIPlugin.PLUGIN_ID, ex.getLocalizedMessage(), ex), DiagnosticComposite.ERROR_WARNING_MASK).open();
                }
              });
            }
            catch (OperationCanceledException ex)
            {
              progressMonitorWrapper.done();
            }
            catch (final URISyntaxException ex)
            {
              UIUtil.asyncExec(parent, new Runnable()
              {
                public void run()
                {
                  new StatusDialog(parent.getShell(), "Problems", "Problem Loading Repository",
                      new Status(IStatus.ERROR, SetupUIPlugin.PLUGIN_ID, ex.getLocalizedMessage(), ex), DiagnosticComposite.ERROR_WARNING_MASK).open();
                }
              });
            }
            finally
            {
              java.net.URI[] finalKnownRepositories = metadataRepositoryManager.getKnownRepositories(IRepositoryManager.REPOSITORIES_ALL);
              for (java.net.URI uri : finalKnownRepositories)
              {
                if (!originalKnownRepositories.contains(uri))
                {
                  metadataRepositoryManager.removeRepository(uri);
                }
              }
            }

            UIUtil.asyncExec(parent, new Runnable()
            {
              public void run()
              {
                progressAreaGridData.exclude = true;
                parent.layout();
                requirementsViewer.getTable().setFocus();
              }
            });

            return Status.OK_STATUS;
          }
        };

        repositoryLoaderJob.schedule();
        progressMonitorPart.attachToCancelComponent(null);

        UIUtil.asyncExec(parent, new Runnable()
        {
          public void run()
          {
            requirementsViewer.setSelection(new StructuredSelection(installableUnits.keySet().iterator().next()));
          }
        });
      }

      private void handleRepository(IMetadataRepository repository, Map<Requirement, IInstallableUnit> requiredUnits)
      {
        for (Map.Entry<Requirement, IInstallableUnit> entry : requiredUnits.entrySet())
        {
          Requirement requirement = entry.getKey();
          IQueryResult<IInstallableUnit> queryResult = repository.query(QueryUtil.createIUQuery(requirement.getName()), null);
          for (IInstallableUnit installableUnit : P2Util.asIterable(queryResult))
          {
            entry.setValue(installableUnit);
            break;
          }
        }
      }

      @Override
      protected void createButtonsForButtonBar(Composite parent)
      {
        if (hasOptionalFeatures)
        {
          createButton(parent, IDialogConstants.CLIENT_ID, "Select &All", true);
          createButton(parent, IDialogConstants.CLIENT_ID + 1, "&Deselect All", true);
        }

        super.createButtonsForButtonBar(parent);
      }

      @Override
      protected void buttonPressed(int buttonId)
      {
        if (buttonId == IDialogConstants.CLIENT_ID)
        {
          requirementsViewer.setCheckedElements(((Collection<?>)requirementsViewer.getInput()).toArray());
          requirementsViewer.getTable().notifyListeners(SWT.Selection, new Event());
          checkStateListener.checkStateChanged(null);
        }
        else if (buttonId == IDialogConstants.CLIENT_ID + 1)
        {
          requirementsViewer.setCheckedElements(new Object[0]);
          checkStateListener.checkStateChanged(null);
        }
        else
        {
          super.buttonPressed(buttonId);
        }
      }

      @Override
      protected void cancelPressed()
      {
        super.cancelPressed();
        if (repositoryLoaderJob != null)
        {
          repositoryLoaderJob.cancel();
        }
      }
    };

    if (marketPlaceListingDialog.open() == Window.OK)
    {
      applyMarketPlaceListing(checkedRequirements);
      return true;
    }

    return false;
  }

  protected boolean isSelected(Requirement requirement)
  {
    return MarketPlaceListing.isSelected(requirement);
  }

  protected Point getSize()
  {
    Point size = shell.getSize();
    if (setupWizard == null)
    {
      size.x /= 2;
      size.y /= 2;
    }
    return size;
  }

  protected String getCorrespondParameterName(Requirement requirement)
  {
    String name = requirement.getName();
    return name.substring(0, name.length() - Requirement.FEATURE_SUFFIX.length()) + ".enabled";
  }

  protected void applyMarketPlaceListing(Set<Requirement> checkedRequirements)
  {
    SetupContext setupContext = SetupContext.create(setupWizard.getResourceSet(), null);
    Installation setupInstallation = setupContext.getInstallation();

    applySetupTasks(setupInstallation, checkedRequirements);

    setupWizard.addAppliedConfigurationResource(marketPlaceListingResource);
  }

  protected void applySetupTasks(SetupTaskContainer targetSetupTaskContainer, Set<Requirement> checkedRequirements)
  {
    Macro macro = (Macro)EcoreUtil.getObjectByType(marketPlaceListingResource.getContents(), SetupPackage.Literals.MACRO);
    if (macro != null)
    {
      MacroTask macroTask = createMacroTask(targetSetupTaskContainer, macro, checkedRequirements);
      EList<SetupTask> setupTasks = targetSetupTaskContainer.getSetupTasks();
      setupTasks.add(macroTask);
    }
  }

  protected MacroTask createMacroTask(SetupTaskContainer targetSetupTaskContainer, Macro macro, Set<Requirement> checkedRequirements)
  {
    Set<String> checkedParameterNames = new HashSet<String>();
    for (Requirement requirement : checkedRequirements)
    {
      checkedParameterNames.add(getCorrespondParameterName(requirement));
    }

    MacroTask macroTask = SetupTaskContainerItemProvider.createMacroTask(targetSetupTaskContainer, macro);
    for (Argument argument : macroTask.getArguments())
    {
      Parameter parameter = argument.getParameter();
      String name = parameter.getName();
      argument.setValue(checkedParameterNames.contains(name) ? "true" : "false");
    }

    return macroTask;
  }

  protected IStatus createResourceStatus(Collection<? extends Resource> resources, EClass expectedEClass)
  {
    StringBuilder uris = new StringBuilder();
    List<IStatus> childStatuses = new ArrayList<IStatus>();
    for (Resource resource : resources)
    {
      if (uris.length() != 0)
      {
        uris.append(' ');
      }

      uris.append(resource.getURI());

      EList<Resource.Diagnostic> errors = resource.getErrors();
      if (errors.isEmpty())
      {
        EList<EObject> contents = resource.getContents();
        if (contents.isEmpty())
        {
          childStatuses.add(new Status(IStatus.ERROR, SetupUIPlugin.PLUGIN_ID, "The resource is empty"));
        }
        else
        {
          childStatuses.add(new Status(IStatus.ERROR, SetupUIPlugin.PLUGIN_ID, "The resource contains a " + contents.get(0).eClass().getName()));
        }
      }
      else
      {
        for (Resource.Diagnostic diagnostic : errors)
        {
          String message = diagnostic.getMessage();
          Throwable throwable = null;
          if (diagnostic instanceof Throwable)
          {
            throwable = (Throwable)diagnostic;
            if (throwable instanceof XMIException)
            {
              Throwable cause = throwable.getCause();
              if (cause != null)
              {
                XMIException xmiException = (XMIException)throwable;
                message = cause.getMessage();
                int line = xmiException.getLine();
                if (line != 0)
                {
                  message += " (" + line + ", " + xmiException.getColumn() + ")";
                }
              }
            }
          }

          childStatuses.add(new Status(IStatus.ERROR, SetupUIPlugin.PLUGIN_ID, message, throwable));
        }
      }
    }

    return new MultiStatus(SetupUIPlugin.PLUGIN_ID, 0, childStatuses.toArray(new IStatus[childStatuses.size()]),
        "No " + expectedEClass.getName() + " could be loaded from " + uris, null);
  }

  private static class MarketPlaceListingStatus extends MultiStatus
  {
    public MarketPlaceListingStatus(Resource resource)
    {
      super(SetupUIPlugin.PLUGIN_ID, 0, "Processing " + resource.getURI(), null);
    }

    public MarketPlaceListingStatus(String message)
    {
      super(SetupUIPlugin.PLUGIN_ID, 0, message, null);
    }

    public int computeSeverity()
    {
      for (IStatus status : getChildren())
      {
        int newSev = status instanceof MarketPlaceListingStatus ? ((MarketPlaceListingStatus)status).computeSeverity() : status.getSeverity();
        if (newSev > getSeverity())
        {
          setSeverity(newSev);
        }
      }

      return getSeverity();
    }
  }
}
