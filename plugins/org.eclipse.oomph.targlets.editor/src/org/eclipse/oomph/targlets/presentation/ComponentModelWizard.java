/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.presentation;

import org.eclipse.oomph.base.util.BaseResourceFactoryImpl;
import org.eclipse.oomph.targlets.ComponentDefinition;
import org.eclipse.oomph.targlets.FeatureGenerator;
import org.eclipse.oomph.targlets.TargletFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a simple wizard for creating a new model file.
 */
public abstract class ComponentModelWizard extends Wizard implements INewWizard
{
  /**
   * Remember the selection during initialization for populating the default container.
   */
  private IStructuredSelection selection;

  /**
   * Remember the workbench during initialization.
   */
  private IWorkbench workbench;

  private IFile file;

  /**
   * This just records the information.
   */
  @Override
  public void init(IWorkbench workbench, IStructuredSelection selection)
  {
    this.workbench = workbench;
    this.selection = selection;
    setWindowTitle(getModelName());
    setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(getImage()));
  }

  /**
   * The framework calls this to create the contents of the wizard.
   */
  @Override
  public void addPages()
  {
    String modelName = getModelName().toLowerCase();
    String message = null;

    if (selection != null && !selection.isEmpty())
    {
      Object element = selection.getFirstElement();
      if (element instanceof IResource)
      {
        IProject project = ((IResource)element).getProject();
        if (!project.isAccessible())
        {
          message = NLS.bind(Messages.ComponentModelWizard_ProjectNotAccessible_message, modelName);
        }
        else
        {
          file = project.getFile(getFileName());
          if (file.exists())
          {
            message = NLS.bind(Messages.ComponentModelWizard_AlreadyExists_message, modelName, modelName);
          }
          else
          {
            message = getErrorMessage(file);
          }
        }
      }
    }

    if (file == null && message == null)
    {
      message = NLS.bind(Messages.ComponentModelWizard_SelectProject_message, modelName);
    }

    if (message != null)
    {
      file = null;
      addPage(new ErrorPage(getModelName(), message));
    }
    else
    {
      IWizardPage page = createPage(file);
      page.setTitle(getModelName());
      page.setDescription(NLS.bind(Messages.ComponentModelWizard_CreateFile_message, modelName));

      addPage(page);
    }
  }

  @Override
  public boolean canFinish()
  {
    return file != null && super.canFinish();
  }

  @Override
  public boolean performFinish()
  {
    try
    {
      WorkspaceModifyOperation operation = new WorkspaceModifyOperation()
      {
        @Override
        protected void execute(IProgressMonitor progressMonitor)
        {
          try
          {
            ResourceSet resourceSet = new ResourceSetImpl();
            resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new BaseResourceFactoryImpl());

            URI fileURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);

            Resource resource = resourceSet.createResource(fileURI);
            EObject rootObject = createModel();
            if (rootObject != null)
            {
              resource.getContents().add(rootObject);
            }

            Map<Object, Object> options = new HashMap<>();
            options.put(XMLResource.OPTION_ENCODING, "UTF-8"); //$NON-NLS-1$
            resource.save(options);
          }
          catch (Exception exception)
          {
            TargletEditorPlugin.INSTANCE.log(exception);
          }
          finally
          {
            progressMonitor.done();
          }
        }
      };

      IWizardContainer container = getContainer();
      container.run(false, false, operation);

      IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
      IWorkbenchPage page = workbenchWindow.getActivePage();
      final IWorkbenchPart activePart = page.getActivePart();
      if (activePart instanceof ISetSelectionTarget)
      {
        final ISelection targetSelection = new StructuredSelection(file);
        getShell().getDisplay().asyncExec(new Runnable()
        {
          @Override
          public void run()
          {
            ((ISetSelectionTarget)activePart).selectReveal(targetSelection);
          }
        });
      }

      try
      {
        page.openEditor(new FileEditorInput(file), workbench.getEditorRegistry().getDefaultEditor(file.getFullPath().toString()).getId());
      }
      catch (PartInitException exception)
      {
        MessageDialog.openError(workbenchWindow.getShell(), TargletEditorPlugin.INSTANCE.getString("_UI_OpenEditorError_label"), exception.getMessage()); //$NON-NLS-1$
        return false;
      }

      return true;
    }
    catch (Exception exception)
    {
      TargletEditorPlugin.INSTANCE.log(exception);
      return false;
    }
  }

  protected abstract Object getImage();

  protected abstract String getModelName();

  protected abstract String getFileName();

  protected abstract EObject createModel();

  protected abstract IWizardPage createPage(IFile file);

  protected abstract String getErrorMessage(IFile file);

  /**
   * This is the error page of the wizard.
   *
   * @author Eike Stepper
   */
  public static class ErrorPage extends WizardPage
  {
    private String message;

    public ErrorPage(String title, String message)
    {
      super(Messages.ComponentModelWizard_Error_label);
      this.message = message;
      setTitle(title);
      setErrorMessage(TargletEditorPlugin.INSTANCE.getString("_UI_ErrorPage_description")); //$NON-NLS-1$
    }

    @Override
    public void createControl(Composite parent)
    {
      Label label = new Label(parent, SWT.WRAP);
      label.setText(message);
      setControl(label);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class CDef extends ComponentModelWizard
  {
    private ComponentDefinition model;

    @Override
    protected Object getImage()
    {
      return TargletEditorPlugin.INSTANCE.getImage("full/wizban/cdef_wiz.png"); //$NON-NLS-1$
    }

    @Override
    protected String getModelName()
    {
      return TargletEditorPlugin.INSTANCE.getString("_UI_CDef_ModelName"); //$NON-NLS-1$
    }

    @Override
    protected String getFileName()
    {
      return "component.def"; //$NON-NLS-1$
    }

    @Override
    protected EObject createModel()
    {
      return model;
    }

    @Override
    protected IWizardPage createPage(IFile file)
    {
      model = TargletFactory.eINSTANCE.createComponentDefinition();
      model.setID(file.getProject().getName());
      model.setVersion(Version.createOSGi(1, 0, 0));

      return new WizardPage(getModelName())
      {
        @Override
        public void createControl(Composite parent)
        {
          Composite composite = new Composite(parent, SWT.NONE);
          composite.setLayout(new GridLayout(2, false));

          Label idLabel = new Label(composite, SWT.NONE);
          idLabel.setText(Messages.ComponentModelWizard_ComponentID_label);
          idLabel.setLayoutData(new GridData());

          final Text idText = new Text(composite, SWT.BORDER);
          idText.setText(model.getID() == null ? "" : model.getID()); //$NON-NLS-1$
          idText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
          idText.addModifyListener(new ModifyListener()
          {
            @Override
            public void modifyText(ModifyEvent e)
            {
              model.setID(idText.getText());
              validatePage();
            }
          });

          Label versionLabel = new Label(composite, SWT.NONE);
          versionLabel.setText(Messages.ComponentModelWizard_ComponentVersion_label);
          versionLabel.setLayoutData(new GridData());

          final Text versionText = new Text(composite, SWT.BORDER);
          versionText.setText(model.getVersion() == null ? "" : model.getVersion().toString()); //$NON-NLS-1$
          versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
          versionText.addModifyListener(new ModifyListener()
          {
            @Override
            public void modifyText(ModifyEvent e)
            {
              try
              {
                model.setVersion(Version.create(versionText.getText()));
              }
              catch (Exception ex)
              {
                //$FALL-THROUGH$
              }

              validatePage();
            }
          });

          validatePage();
          setControl(composite);
        }

        private void validatePage()
        {
          setPageComplete(model.getID() != null && model.getID().length() != 0 && model.getVersion() != null);
        }
      };
    }

    @Override
    protected String getErrorMessage(IFile file)
    {
      IProject project = file.getProject();
      if (project.getFile("META-INF/MANIFEST.MF").exists()) //$NON-NLS-1$
      {
        return getErrorMessage(project, Messages.ComponentModelWizard_PluginComponent_message);
      }

      if (project.getFile(FeatureGenerator.FEATURE_XML).exists())
      {
        return getErrorMessage(project, Messages.ComponentModelWizard_FeatureComponent_message);
      }

      return null;
    }

    private String getErrorMessage(IProject project, String message)
    {
      if (project.getFile("component.ext").exists()) //$NON-NLS-1$
      {
        return message + "\n" + Messages.ComponentModelWizard_ProjectAlreadyComponent_message; //$NON-NLS-1$
      }

      return message + "\n" + Messages.ComponentModelWizard_SelectOrCreate_message; //$NON-NLS-1$
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class CExt extends ComponentModelWizard
  {
    @Override
    protected Object getImage()
    {
      return TargletEditorPlugin.INSTANCE.getImage("full/wizban/cext_wiz.png"); //$NON-NLS-1$
    }

    @Override
    protected String getModelName()
    {
      return TargletEditorPlugin.INSTANCE.getString("_UI_CExt_ModelName"); //$NON-NLS-1$
    }

    @Override
    protected String getFileName()
    {
      return "component.ext"; //$NON-NLS-1$
    }

    @Override
    protected EObject createModel()
    {
      return TargletFactory.eINSTANCE.createComponentExtension();
    }

    @Override
    protected IWizardPage createPage(IFile file)
    {
      return new WizardPage(getModelName())
      {
        @Override
        public void createControl(Composite parent)
        {
          Label label = new Label(parent, SWT.WRAP);
          label.setText(Messages.ComponentModelWizard_Finish_message);
          setControl(label);
        }
      };
    }

    @Override
    protected String getErrorMessage(IFile file)
    {
      IProject project = file.getProject();
      if (project.getFile("component.def").exists()) //$NON-NLS-1$
      {
        return Messages.ComponentModelWizard_ComponentDefinitionExists_message;
      }

      return null;
    }
  }
}
