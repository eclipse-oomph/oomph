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
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.internal.setup.core.util.EMFUtil;
import org.eclipse.oomph.setup.Project;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.editor.ProjectTemplate;
import org.eclipse.oomph.setup.provider.SetupEditPlugin;
import org.eclipse.oomph.setup.ui.PropertiesViewer;
import org.eclipse.oomph.setup.ui.SetupLabelProvider;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.StringTokenizer;

/**
 * This is a simple wizard for creating a new model file.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SetupModelWizard extends Wizard implements INewWizard
{
  /**
   * The supported extensions for created files.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<String> FILE_EXTENSIONS = Collections.unmodifiableList(Arrays.asList(SetupEditorPlugin.INSTANCE.getString(
      "_UI_SetupEditorFilenameExtensions").split("\\s*,\\s*")));

  /**
   * A formatted list of supported file extensions, suitable for display.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final String FORMATTED_FILE_EXTENSIONS = SetupEditorPlugin.INSTANCE.getString("_UI_SetupEditorFilenameExtensions")
      .replaceAll("\\s*,\\s*", ", ");

  /**
   * This caches an instance of the model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SetupPackage setupPackage = SetupPackage.eINSTANCE;

  /**
   * This caches an instance of the model factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SetupFactory setupFactory = setupPackage.getSetupFactory();

  /**
   * This is the file creation page.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SetupModelWizardNewFileCreationPage newFileCreationPage;

  /**
   * This is the initial object creation page.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SetupModelWizardInitialObjectCreationPage initialObjectCreationPage;

  protected ProjectInitializationPage projectInitializationPage;

  protected TemplateUsagePage templateUsagePage;

  /**
   * Remember the selection during initialization for populating the default container.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IStructuredSelection selection;

  /**
   * Remember the workbench during initialization.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IWorkbench workbench;

  /**
   * Caches the names of the types that can be created as the root object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected List<String> initialObjectNames;

  /**
   * This just records the information.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void init(IWorkbench workbench, IStructuredSelection selection)
  {
    this.workbench = workbench;
    this.selection = selection;
    setWindowTitle(SetupEditorPlugin.INSTANCE.getString("_UI_Wizard_label"));
    setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(SetupEditorPlugin.INSTANCE.getImage("full/wizban/NewSetup")));
  }

  /**
   * The framework calls this to create the contents of the wizard.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public void addPages()
  {
    // Create a page, set the title, and the initial model file name.
    //
    newFileCreationPage = new SetupModelWizardNewFileCreationPage("Whatever", selection);
    newFileCreationPage.setTitle(SetupEditorPlugin.INSTANCE.getString("_UI_SetupModelWizard_label"));
    newFileCreationPage.setDescription(SetupEditorPlugin.INSTANCE.getString("_UI_SetupModelWizard_description"));
    newFileCreationPage.setFileName(SetupEditorPlugin.INSTANCE.getString("_UI_SetupEditorFilenameDefaultBase") + "." + FILE_EXTENSIONS.get(0));
    addPage(newFileCreationPage);

    // Try and get the resource selection to determine a current directory for the file dialog.
    //
    if (selection != null && !selection.isEmpty())
    {
      // Get the resource...
      //
      Object selectedElement = selection.iterator().next();
      if (selectedElement instanceof IResource)
      {
        // Get the resource parent, if its a file.
        //
        IResource selectedResource = (IResource)selectedElement;
        if (selectedResource.getType() == IResource.FILE)
        {
          selectedResource = selectedResource.getParent();
        }

        // This gives us a directory...
        //
        if (selectedResource instanceof IFolder || selectedResource instanceof IProject)
        {
          // Set this for the container.
          //
          newFileCreationPage.setContainerFullPath(selectedResource.getFullPath());

          // Make up a unique new name here.
          //
          String defaultModelBaseFilename = SetupEditorPlugin.INSTANCE.getString("_UI_SetupEditorFilenameDefaultBase");
          String defaultModelFilenameExtension = FILE_EXTENSIONS.get(0);
          String modelFilename = defaultModelBaseFilename + "." + defaultModelFilenameExtension;
          for (int i = 1; ((IContainer)selectedResource).findMember(modelFilename) != null; ++i)
          {
            modelFilename = defaultModelBaseFilename + i + "." + defaultModelFilenameExtension;
          }
          newFileCreationPage.setFileName(modelFilename);
        }
      }
    }

    projectInitializationPage = new ProjectInitializationPage("Whatever2");
    projectInitializationPage.setTitle(SetupEditorPlugin.INSTANCE.getString("_UI_SetupModelWizard_label"));
    projectInitializationPage.setDescription(SetupEditorPlugin.INSTANCE.getString("_UI_Wizard_initial_object_description2"));
    addPage(projectInitializationPage);

    templateUsagePage = new TemplateUsagePage("Whatever3");
    templateUsagePage.setTitle(SetupEditorPlugin.INSTANCE.getString("_UI_SetupModelWizard_label"));
    templateUsagePage.setDescription(SetupEditorPlugin.INSTANCE.getString("_UI_Wizard_initial_object_description3"));
    addPage(templateUsagePage);
  }

  /**
   * Get the file from the page.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IFile getModelFile()
  {
    return newFileCreationPage.getModelFile();
  }

  /**
   * Returns the names of the types that can be created as the root object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Collection<String> getInitialObjectNames()
  {
    if (initialObjectNames == null)
    {
      initialObjectNames = new ArrayList<String>();
      for (EClassifier eClassifier : setupPackage.getEClassifiers())
      {
        if (eClassifier instanceof EClass)
        {
          EClass eClass = (EClass)eClassifier;
          if (!eClass.isAbstract())
          {
            initialObjectNames.add(eClass.getName());
          }
        }
      }
      Collections.sort(initialObjectNames, CommonPlugin.INSTANCE.getComparator());
    }
    return initialObjectNames;
  }

  /**
   * Create a new model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  protected EObject createInitialModel()
  {
    Project project;

    if (projectInitializationPage.isUseTemplate())
    {
      project = EcoreUtil.copy(templateUsagePage.getProject());
    }
    else
    {
      project = SetupFactory.eINSTANCE.createProject();
      Stream masterBranch = SetupFactory.eINSTANCE.createStream();
      masterBranch.setName("master");
      project.getStreams().add(masterBranch);

      Stream maintenanceBranch = SetupFactory.eINSTANCE.createStream();
      maintenanceBranch.setName("maintenance");
      project.getStreams().add(maintenanceBranch);
    }

    project.setName(projectInitializationPage.getProjectName());
    project.setLabel(projectInitializationPage.getProjectLabel());
    return project;
  }

  @Override
  public boolean canFinish()
  {
    if (!newFileCreationPage.isPageComplete())
    {
      return false;
    }

    if (!projectInitializationPage.isPageComplete())
    {
      return false;
    }

    if (projectInitializationPage.isUseTemplate() /* && getContainer().getCurrentPage() == projectInitializationPage */)
    {
      if (!templateUsagePage.isPageComplete())
      {
        return false;
      }
    }

    return true;
  }

  /**
   * Do the work after everything is specified.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated NOT
   */
  @Override
  public boolean performFinish()
  {
    try
    {
      // Remember the file.
      //
      final IFile modelFile = getModelFile();

      // Do the work within an operation.
      //
      WorkspaceModifyOperation operation = new WorkspaceModifyOperation()
      {
        @Override
        protected void execute(IProgressMonitor progressMonitor)
        {
          try
          {
            // Create a resource set
            //
            ResourceSet resourceSet = EMFUtil.createResourceSet();

            // Get the URI of the model file.
            //
            URI fileURI = URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);

            // Create a resource for this file.
            //
            Resource resource = resourceSet.createResource(fileURI);

            // Add the initial model object to the contents.
            //
            EObject rootObject = createInitialModel();
            if (rootObject != null)
            {
              resource.getContents().add(rootObject);
            }

            // Save the contents of the resource to the file system.
            //
            Map<Object, Object> options = new HashMap<Object, Object>();
            options.put(XMLResource.OPTION_ENCODING, "UTF-8");
            resource.save(options);
          }
          catch (Exception exception)
          {
            SetupEditorPlugin.INSTANCE.log(exception);
          }
          finally
          {
            progressMonitor.done();
          }
        }
      };

      getContainer().run(false, false, operation);

      // Select the new file resource in the current view.
      //
      IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
      IWorkbenchPage page = workbenchWindow.getActivePage();
      final IWorkbenchPart activePart = page.getActivePart();
      if (activePart instanceof ISetSelectionTarget)
      {
        final ISelection targetSelection = new StructuredSelection(modelFile);
        getShell().getDisplay().asyncExec(new Runnable()
        {
          public void run()
          {
            ((ISetSelectionTarget)activePart).selectReveal(targetSelection);
          }
        });
      }

      // Open an editor on the new file.
      //
      try
      {
        page.openEditor(new FileEditorInput(modelFile), workbench.getEditorRegistry().getDefaultEditor(modelFile.getFullPath().toString()).getId());
      }
      catch (PartInitException exception)
      {
        MessageDialog.openError(workbenchWindow.getShell(), SetupEditorPlugin.INSTANCE.getString("_UI_OpenEditorError_label"), exception.getMessage());
        return false;
      }

      return true;
    }
    catch (Exception exception)
    {
      SetupEditorPlugin.INSTANCE.log(exception);
      return false;
    }
  }

  /**
   * This is the one page of the wizard.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public class SetupModelWizardNewFileCreationPage extends WizardNewFileCreationPage
  {
    /**
     * Pass in the selection.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SetupModelWizardNewFileCreationPage(String pageId, IStructuredSelection selection)
    {
      super(pageId, selection);
    }

    /**
     * The framework calls this to see if the file is correct.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected boolean validatePage()
    {
      if (super.validatePage())
      {
        String extension = new Path(getFileName()).getFileExtension();
        if (extension == null || !FILE_EXTENSIONS.contains(extension))
        {
          String key = FILE_EXTENSIONS.size() > 1 ? "_WARN_FilenameExtensions" : "_WARN_FilenameExtension";
          setErrorMessage(SetupEditorPlugin.INSTANCE.getString(key, new Object[] { FORMATTED_FILE_EXTENSIONS }));
          return false;
        }
        return true;
      }
      return false;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IFile getModelFile()
    {
      return ResourcesPlugin.getWorkspace().getRoot().getFile(getContainerFullPath().append(getFileName()));
    }
  }

  /**
   * This is the page where the type of object to create is selected.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public class SetupModelWizardInitialObjectCreationPage extends WizardPage
  {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected Combo initialObjectField;

    /**
     * @generated
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     */
    protected List<String> encodings;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected Combo encodingField;

    /**
     * Pass in the selection.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public SetupModelWizardInitialObjectCreationPage(String pageId)
    {
      super(pageId);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createControlGen(Composite parent)
    {
      Composite composite = new Composite(parent, SWT.NONE);
      {
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        layout.verticalSpacing = 12;
        composite.setLayout(layout);

        GridData data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.grabExcessVerticalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        composite.setLayoutData(data);
      }

      Label containerLabel = new Label(composite, SWT.LEFT);
      {
        containerLabel.setText(SetupEditorPlugin.INSTANCE.getString("_UI_ModelObject"));

        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        containerLabel.setLayoutData(data);
      }

      initialObjectField = new Combo(composite, SWT.BORDER);
      {
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        initialObjectField.setLayoutData(data);
      }

      for (String objectName : getInitialObjectNames())
      {
        initialObjectField.add(getLabel(objectName));
      }

      if (initialObjectField.getItemCount() == 1)
      {
        initialObjectField.select(0);
      }
      initialObjectField.addModifyListener(validator);

      Label encodingLabel = new Label(composite, SWT.LEFT);
      {
        encodingLabel.setText(SetupEditorPlugin.INSTANCE.getString("_UI_XMLEncoding"));

        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        encodingLabel.setLayoutData(data);
      }
      encodingField = new Combo(composite, SWT.BORDER);
      {
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        encodingField.setLayoutData(data);
      }

      for (String encoding : getEncodings())
      {
        encodingField.add(encoding);
      }

      encodingField.select(0);
      encodingField.addModifyListener(validator);

      setPageComplete(validatePage());
      setControl(composite);
    }

    public void createControl(Composite parent)
    {
      createControlGen(parent);

      String[] items = initialObjectField.getItems();
      for (int i = 0; i < items.length; ++i)
      {
        if (items[i].equals("Project"))
        {
          initialObjectField.select(i);
          break;
        }
      }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected ModifyListener validator = new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        setPageComplete(validatePage());
      }
    };

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected boolean validatePage()
    {
      return getInitialObjectName() != null && getEncodings().contains(encodingField.getText());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setVisible(boolean visible)
    {
      super.setVisible(visible);
      if (visible)
      {
        if (initialObjectField.getItemCount() == 1)
        {
          initialObjectField.clearSelection();
          encodingField.setFocus();
        }
        else
        {
          encodingField.clearSelection();
          initialObjectField.setFocus();
        }
      }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getInitialObjectName()
    {
      String label = initialObjectField.getText();

      for (String name : getInitialObjectNames())
      {
        if (getLabel(name).equals(label))
        {
          return name;
        }
      }
      return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getEncoding()
    {
      return encodingField.getText();
    }

    /**
     * Returns the label for the specified type name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected String getLabel(String typeName)
    {
      try
      {
        return SetupEditPlugin.INSTANCE.getString("_UI_" + typeName + "_type");
      }
      catch (MissingResourceException mre)
      {
        SetupEditorPlugin.INSTANCE.log(mre);
      }
      return typeName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected Collection<String> getEncodings()
    {
      if (encodings == null)
      {
        encodings = new ArrayList<String>();
        for (StringTokenizer stringTokenizer = new StringTokenizer(SetupEditorPlugin.INSTANCE.getString("_UI_XMLEncodingChoices")); stringTokenizer
            .hasMoreTokens();)
        {
          encodings.add(stringTokenizer.nextToken());
        }
      }
      return encodings;
    }
  }

  /**
   * @author Eike Stepper
   */
  public class ProjectInitializationPage extends WizardPage
  {
    protected Text nameField;

    protected Text labelField;

    protected Button useTemplateButton;

    public ProjectInitializationPage(String pageId)
    {
      super(pageId);
    }

    public String getProjectName()
    {
      return nameField.getText();
    }

    public String getProjectLabel()
    {
      return labelField.getText();
    }

    public boolean isUseTemplate()
    {
      return false;
      // return useTemplateButton.getSelection();
    }

    @Override
    public boolean canFlipToNextPage()
    {
      return isUseTemplate();
    }

    public void createControl(Composite parent)
    {
      GridLayout layout = new GridLayout();
      layout.numColumns = 1;
      layout.verticalSpacing = 10;

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(layout);
      UIUtil.grabVertical(UIUtil.applyGridData(composite));
      setControl(composite);

      Label containerLabel = new Label(composite, SWT.LEFT);
      containerLabel.setText(SetupEditorPlugin.INSTANCE.getString("_UI_Wizard_Name_label"));
      UIUtil.applyGridData(containerLabel);

      nameField = new Text(composite, SWT.BORDER);
      nameField.addModifyListener(new ModifyListener()
      {
        private String lastName;

        public void modifyText(ModifyEvent e)
        {
          String label = getProjectLabel();
          if (label.length() == 0 || label.equals(generateProjectLabel(lastName)))
          {
            label = generateProjectLabel(getProjectName());
            labelField.setText(label);
          }

          lastName = getProjectName();
          validatePage();
        }
      });
      UIUtil.applyGridData(nameField);

      Label labelLabel = new Label(composite, SWT.LEFT);
      labelLabel.setText(SetupEditorPlugin.INSTANCE.getString("_UI_Wizard_Label_label"));
      UIUtil.applyGridData(labelLabel);

      labelField = new Text(composite, SWT.BORDER);
      UIUtil.applyGridData(labelField);

      // useTemplateButton = new Button(composite, SWT.CHECK);
      // useTemplateButton.setText(SetupEditorPlugin.INSTANCE.getString("_UI_Wizard_UseTemplate_label"));
      // useTemplateButton.addSelectionListener(new SelectionAdapter()
      // {
      // @Override
      // public void widgetSelected(SelectionEvent e)
      // {
      // validatePage();
      // }
      // });
      // UIUtil.applyGridData(useTemplateButton).horizontalAlignment = SWT.LEFT;

      validatePage();
    }

    @Override
    public void setVisible(boolean visible)
    {
      super.setVisible(visible);
      if (visible && nameField.getText().length() == 0)
      {
        String fileName = newFileCreationPage.getModelFile().getName();
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot != -1)
        {
          fileName = fileName.substring(0, lastDot);
        }

        nameField.setText(fileName);
        nameField.selectAll();
        nameField.setFocus();
      }
    }

    protected void validatePage()
    {
      boolean pageValid = isPageValid();
      setPageComplete(pageValid);

      templateUsagePage.setVisible(isUseTemplate());
      getContainer().updateButtons();
    }

    protected boolean isPageValid()
    {
      setErrorMessage(null);

      String projectName = getProjectName();
      if (projectName.length() == 0)
      {
        return false;
      }

      if (!new Path("").isValidSegment(projectName))
      {
        setErrorMessage("The project name is invalid.");
        return false;
      }

      return true;
    }

    private String generateProjectLabel(String projectName)
    {
      String label = projectName.replace('.', ' ').replace('_', ' ').replace('/', ' ');
      label = label.replaceAll("([A-Za-z0-9])([A-Z][a-z])", "$1 $2");
      label = label.replaceAll("([a-z])([A-Z])", "$1 $2");

      StringBuilder builder = new StringBuilder(label);
      boolean space = false;

      for (int i = 0; i < builder.length(); i++)
      {
        char c = builder.charAt(i);
        if (c == ' ')
        {
          space = true;
        }
        else
        {
          if (space)
          {
            builder.setCharAt(i, Character.toUpperCase(c));
            space = false;
          }
        }
      }

      return builder.toString();
    }
  }

  /**
   * @author Eike Stepper
   */
  public class TemplateUsagePage extends WizardPage implements ProjectTemplate.Container
  {
    private final List<ProjectTemplate> templates = new ArrayList<ProjectTemplate>();

    private final Map<ProjectTemplate, Control> templateControls = new HashMap<ProjectTemplate, Control>();

    private ListViewer templatesViewer;

    private Composite templatesContainer;

    private StackLayout templatesStack;

    private TreeViewer preViewer;

    private PropertiesViewer propertiesViewer;

    public TemplateUsagePage(String pageId)
    {
      super(pageId);
      setPageComplete(false);

      int xxx;
      // for (String type : IPluginContainer.INSTANCE.getFactoryTypes(ProjectTemplate.PRODUCT_GROUP))
      // {
      // Factory factory = (Factory)IPluginContainer.INSTANCE.getFactory(ProjectTemplate.PRODUCT_GROUP, type);
      //
      // ProjectTemplate template = factory.createProjectTemplate();
      // template.init(this);
      // templates.add(template);
      // }
      //
      // Collections.sort(templates, new Comparator<ProjectTemplate>()
      // {
      // public int compare(ProjectTemplate t1, ProjectTemplate t2)
      // {
      // return t1.getLabel().compareTo(t2.getLabel());
      // }
      // });
    }

    public Project getProject()
    {
      ProjectTemplate template = getSelectedTemplate();
      return template.getProject();
    }

    public void createControl(Composite parent)
    {
      GridLayout layout = new GridLayout();
      layout.numColumns = 1;
      layout.verticalSpacing = 10;

      Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(layout);
      UIUtil.grabVertical(UIUtil.applyGridData(composite));
      setControl(composite);

      templatesViewer = new ListViewer(composite, SWT.BORDER);
      templatesViewer.setLabelProvider(new LabelProvider());
      templatesViewer.setContentProvider(ArrayContentProvider.getInstance());
      templatesViewer.setInput(templates);
      templatesViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        public void selectionChanged(SelectionChangedEvent event)
        {
          Control control = getSelectedTemplateControl();
          if (control != null)
          {
            templatesStack.topControl = control;
            templatesContainer.layout();

            Project project = getProject();
            preViewer.setInput(project);
          }

          validate();
        }
      });
      UIUtil.applyGridData(templatesViewer.getControl()).heightHint = 64;

      templatesStack = new StackLayout();

      templatesContainer = new Composite(composite, SWT.NONE);
      templatesContainer.setLayout(templatesStack);
      UIUtil.applyGridData(templatesContainer);

      SashForm sash = new SashForm(composite, SWT.VERTICAL);
      UIUtil.grabVertical(UIUtil.applyGridData(sash));

      preViewer = new TreeViewer(sash, SWT.BORDER);
      AdapterFactory adapterFactory = EMFUtil.createAdapterFactory();
      preViewer.setLabelProvider(new SetupLabelProvider(adapterFactory, preViewer));
      preViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
      preViewer.addSelectionChangedListener(new ISelectionChangedListener()
      {
        public void selectionChanged(SelectionChangedEvent event)
        {
          if (propertiesViewer != null)
          {
            IStructuredSelection selection = (IStructuredSelection)event.getSelection();
            if (selection.size() == 1)
            {
              Object element = selection.getFirstElement();
              propertiesViewer.setInput(element);
              return;
            }

            propertiesViewer.setInput(new Object());
          }
        }
      });
      UIUtil.grabVertical(UIUtil.applyGridData(preViewer.getControl()));

      propertiesViewer = new PropertiesViewer(sash, SWT.BORDER);
      sash.setWeights(new int[] { 2, 1 });

      for (ProjectTemplate template : templates)
      {
        Control control = template.createControl(templatesContainer);
        templateControls.put(template, control);
      }

      templatesViewer.setSelection(new StructuredSelection(templates.get(0)));
      validate();
    }

    @Override
    public void setVisible(boolean visible)
    {
      if (templatesViewer == null)
      {
        return;
      }

      super.setVisible(visible);
      if (visible)
      {
        templatesViewer.getControl().setFocus();
      }
    }

    public TreeViewer getPreViewer()
    {
      return preViewer;
    }

    public PropertiesViewer getPropertiesViewer()
    {
      return propertiesViewer;
    }

    public void validate()
    {
      boolean pageValid = isPageValid();
      setPageComplete(pageValid);
      getContainer().updateButtons();
    }

    protected boolean isPageValid()
    {
      try
      {
        ProjectTemplate template = getSelectedTemplate();
        if (template != null)
        {
          Project project = getProject();
          if (!template.isValid(project))
          {
            return false;
          }

          for (Stream branch : project.getStreams())
          {
            if (!template.isValid(branch))
            {
              return false;
            }
          }

          return true;
        }
      }
      catch (Exception ex)
      {
        SetupEditorPlugin.getPlugin().log(ex);
      }

      return false;
    }

    private ProjectTemplate getSelectedTemplate()
    {
      return (ProjectTemplate)((IStructuredSelection)templatesViewer.getSelection()).getFirstElement();
    }

    private Control getSelectedTemplateControl()
    {
      ProjectTemplate template = getSelectedTemplate();
      return templateControls.get(template);
    }
  }
}
