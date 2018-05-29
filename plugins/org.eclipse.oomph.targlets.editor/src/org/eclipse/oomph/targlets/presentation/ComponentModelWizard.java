/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
          message = "The selected project is not accessible.\nSelect an accessible project to create the new " + modelName + " in.";
        }
        else
        {
          file = project.getFile(getFileName());
          if (file.exists())
          {
            message = "A " + modelName + " file does already exist in the selected project.\nDelete it first if you want to create a new " + modelName
                + " in this project.";
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
      message = "No project is selected.\nSelect a project to create the new " + modelName + " in.";
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
      page.setDescription("Create a new " + modelName + " file.");

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

            Map<Object, Object> options = new HashMap<Object, Object>();
            options.put(XMLResource.OPTION_ENCODING, "UTF-8");
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
        MessageDialog.openError(workbenchWindow.getShell(), TargletEditorPlugin.INSTANCE.getString("_UI_OpenEditorError_label"), exception.getMessage());
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
      super("Error");
      this.message = message;
      setTitle(title);
      setErrorMessage(TargletEditorPlugin.INSTANCE.getString("_UI_ErrorPage_description"));
    }

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
      return TargletEditorPlugin.INSTANCE.getImage("full/wizban/cdef_wiz.png");
    }

    @Override
    protected String getModelName()
    {
      return TargletEditorPlugin.INSTANCE.getString("_UI_CDef_ModelName");
    }

    @Override
    protected String getFileName()
    {
      return "component.def";
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
        public void createControl(Composite parent)
        {
          Composite composite = new Composite(parent, SWT.NONE);
          composite.setLayout(new GridLayout(2, false));

          Label idLabel = new Label(composite, SWT.NONE);
          idLabel.setText("Component ID:");
          idLabel.setLayoutData(new GridData());

          final Text idText = new Text(composite, SWT.BORDER);
          idText.setText(model.getID() == null ? "" : model.getID());
          idText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
          idText.addModifyListener(new ModifyListener()
          {
            public void modifyText(ModifyEvent e)
            {
              model.setID(idText.getText());
              validatePage();
            }
          });

          Label versionLabel = new Label(composite, SWT.NONE);
          versionLabel.setText("Component Version:");
          versionLabel.setLayoutData(new GridData());

          final Text versionText = new Text(composite, SWT.BORDER);
          versionText.setText(model.getVersion() == null ? "" : model.getVersion().toString());
          versionText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
          versionText.addModifyListener(new ModifyListener()
          {
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
      if (project.getFile("META-INF/MANIFEST.MF").exists())
      {
        return getErrorMessage(project, "The selected project appears to be a plugin component.");
      }

      if (project.getFile(FeatureGenerator.FEATURE_XML).exists())
      {
        return getErrorMessage(project, "The selected project appears to be a feature component.");
      }

      return null;
    }

    private String getErrorMessage(IProject project, String message)
    {
      if (project.getFile("component.ext").exists())
      {
        return message + "\nSelect a project that is not already a component.";
      }

      return message + "\nEither select a project that is not already a component or create a component extension.";
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
      return TargletEditorPlugin.INSTANCE.getImage("full/wizban/cext_wiz.png");
    }

    @Override
    protected String getModelName()
    {
      return TargletEditorPlugin.INSTANCE.getString("_UI_CExt_ModelName");
    }

    @Override
    protected String getFileName()
    {
      return "component.ext";
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
        public void createControl(Composite parent)
        {
          Label label = new Label(parent, SWT.WRAP);
          label.setText("Press Finish to create the component extension file.");
          setControl(label);
        }
      };
    }

    @Override
    protected String getErrorMessage(IFile file)
    {
      IProject project = file.getProject();
      if (project.getFile("component.def").exists())
      {
        return "A component definition already exists in the selected project.\nEdit the 'component.def' file directly if you want to change the dependencies of this component.";
      }

      return null;
    }
  }
}
