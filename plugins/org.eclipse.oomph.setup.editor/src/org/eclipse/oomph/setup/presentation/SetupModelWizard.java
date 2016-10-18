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
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.setup.SetupFactory;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.editor.SetupTemplate;
import org.eclipse.oomph.setup.presentation.templates.GenericSetupTemplate;
import org.eclipse.oomph.setup.provider.SetupEditPlugin;
import org.eclipse.oomph.setup.ui.SetupLabelProvider;
import org.eclipse.oomph.ui.DelegatingLabelDecorator;
import org.eclipse.oomph.ui.LabelDecorator;
import org.eclipse.oomph.ui.PropertiesViewer;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
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
 * @generated NOT
 */
public abstract class SetupModelWizard extends Wizard implements INewWizard
{
  /**
   * The supported extensions for created files.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<String> FILE_EXTENSIONS = Collections
      .unmodifiableList(Arrays.asList(SetupEditorPlugin.INSTANCE.getString("_UI_SetupEditorFilenameExtensions").split("\\s*,\\s*")));

  /**
   * A formatted list of supported file extensions, suitable for display.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final String FORMATTED_FILE_EXTENSIONS = SetupEditorPlugin.INSTANCE.getString("_UI_SetupEditorFilenameExtensions").replaceAll("\\s*,\\s*",
      ", ");

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
   * This is the initial object creation page.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected SetupModelWizardInitialObjectCreationPage initialObjectCreationPage;

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
   * @generated NOT
   */
  public void init(IWorkbench workbench, IStructuredSelection selection)
  {
    this.workbench = workbench;
    this.selection = selection;
    setWindowTitle(SetupEditorPlugin.INSTANCE.getString("_UI_Wizard_label"));
    setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(SetupEditorPlugin.INSTANCE.getImage("full/wizban/NewSetup.png")));
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
    templateUsagePage = new TemplateUsagePage("Whatever3");
    templateUsagePage.setDescription(SetupEditorPlugin.INSTANCE.getString("_UI_Wizard_initial_object_description"));
    configureTemplateUsagePage(templateUsagePage);
    addPage(templateUsagePage);
  }

  protected abstract void configureTemplateUsagePage(TemplateUsagePage templateUsagePage);

  public IContainer getDefaultContainer()
  {
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
          return (IContainer)selectedResource;
        }
      }
    }

    return null;
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

  @Override
  public boolean canFinish()
  {
    return templateUsagePage.isPageComplete();
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
      final Resource modelResource = templateUsagePage.getResource();
      WorkspaceModifyOperation operation = new WorkspaceModifyOperation()
      {
        @Override
        protected void execute(IProgressMonitor progressMonitor)
        {
          try
          {
            modelResource.save(null);
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

      IFile modelFile = EcorePlugin.getWorkspaceRoot().getFile(new Path(modelResource.getURI().toPlatformString(true)));

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
  public class TemplateUsagePage extends WizardPage implements SetupTemplate.Container
  {
    private final List<SetupTemplate> templates = new ArrayList<SetupTemplate>();

    private final Map<SetupTemplate, Control> templateControls = new HashMap<SetupTemplate, Control>();

    private ComboViewer templatesViewer;

    private Composite templatesContainer;

    private StackLayout templatesStack;

    private TreeViewer previewer;

    private DelegatingLabelDecorator delegatingLabelDecorator;

    private PropertiesViewer propertiesViewer;

    private Button previewButton;

    private SashForm sash;

    private int sashHeight = 400;

    public TemplateUsagePage(String pageId)
    {
      super(pageId);
      setPageComplete(false);
    }

    public void addTemplate(String label, String templateFileName)
    {
      URI templateFolder = URI.createPlatformPluginURI(SetupEditorPlugin.PLUGIN_ID, false).appendSegment("templates");
      URI templateFile = templateFolder.appendSegment(templateFileName).appendFragment("/");

      SetupTemplate template = new GenericSetupTemplate(label, templateFile);
      template.init(this);
      templates.add(template);
    }

    public Resource getResource()
    {
      SetupTemplate template = getSelectedTemplate();
      return template == null ? null : template.getResource();
    }

    public LabelDecorator getDecorator()
    {
      SetupTemplate template = getSelectedTemplate();
      return template == null ? null : template.getDecorator();
    }

    public String getDefaultLocation()
    {
      IContainer defaultContainer = getWizard().getDefaultContainer();
      return defaultContainer == null ? null : defaultContainer.getFullPath().toString();
    }

    protected void updatePreviewer()
    {
      Resource resource = getResource();
      if (previewer != null)
      {
        delegatingLabelDecorator.setLabelDecorator(getDecorator());
        propertiesViewer.getDelegatingLabelDecorator().setLabelDecorator(getDecorator());
        previewer.setInput(new ItemProvider(Collections.singleton(resource)));
        getSelectedTemplate().updatePreview();
      }
    }

    public void createControl(Composite parent)
    {
      GridLayout layout = new GridLayout();
      layout.numColumns = 1;
      layout.verticalSpacing = 10;

      final Composite composite = new Composite(parent, SWT.NONE);
      composite.setLayout(layout);
      UIUtil.grabVertical(UIUtil.applyGridData(composite));
      setControl(composite);

      templatesViewer = new ComboViewer(composite, SWT.BORDER);
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

            updatePreviewer();
          }

          validate();
        }
      });
      UIUtil.applyGridData(templatesViewer.getControl()).heightHint = 64;

      templatesStack = new StackLayout();

      templatesContainer = new Composite(composite, SWT.NONE);
      templatesContainer.setLayout(templatesStack);
      UIUtil.applyGridData(templatesContainer).heightHint = 200;

      previewButton = new Button(composite, SWT.PUSH);
      previewButton.setText("Preview >>>");
      GridData data = setButtonLayoutData(previewButton);
      data.horizontalAlignment = GridData.BEGINNING;
      previewButton.setLayoutData(data);
      previewButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          Shell shell = getShell();
          Point shellSize = shell.getSize();
          if (sash == null)
          {
            sash = new SashForm(composite, SWT.SMOOTH | SWT.VERTICAL);
            UIUtil.grabVertical(UIUtil.applyGridData(sash));

            previewer = new TreeViewer(sash, SWT.BORDER | SWT.MULTI);
            AdapterFactory adapterFactory = BaseEditUtil.createAdapterFactory();
            delegatingLabelDecorator = new DelegatingLabelDecorator();
            previewer.setLabelProvider(new PreviewerLabelProvider(new SetupLabelProvider(adapterFactory, previewer), delegatingLabelDecorator));
            previewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
            previewer.addSelectionChangedListener(new ISelectionChangedListener()
            {
              public void selectionChanged(SelectionChangedEvent event)
              {
                if (propertiesViewer != null)
                {
                  IStructuredSelection selection = (IStructuredSelection)event.getSelection();
                  if (selection.size() == 1)
                  {
                    Object element = selection.getFirstElement();
                    Control control = propertiesViewer.getControl();
                    try
                    {
                      control.setRedraw(false);
                      propertiesViewer.setInput(element);
                    }
                    finally
                    {
                      control.setRedraw(true);
                    }
                    return;
                  }

                  propertiesViewer.setInput(new Object());
                }
              }
            });
            UIUtil.grabVertical(UIUtil.applyGridData(previewer.getControl()));

            propertiesViewer = new PropertiesViewer(sash, SWT.BORDER);
            propertiesViewer.getDelegatingLabelDecorator().setLabelDecorator(getDecorator());
            sash.setWeights(new int[] { 2, 1 });

            SetupTemplate template = getSelectedTemplate();
            if (template != null)
            {
              updatePreviewer();
            }

            shell.setSize(shellSize.x, shellSize.y + sashHeight);
            composite.layout();

            previewButton.setText("<<< Preview");
          }
          else
          {
            sashHeight = sash.getSize().y;

            sash.dispose();
            sash = null;
            previewer = null;
            delegatingLabelDecorator = null;
            propertiesViewer = null;

            composite.layout();
            shell.setSize(shellSize.x, shellSize.y - sashHeight);

            previewButton.setText("Preview >>>");
          }
        }
      });

      for (SetupTemplate template : templates)
      {
        Control control = template.createControl(templatesContainer);
        templateControls.put(template, control);
      }
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

      if (visible && templatesViewer.getSelection().isEmpty() && !templates.isEmpty())
      {
        templatesViewer.setSelection(new StructuredSelection(templates.get(0)));
      }
    }

    public TreeViewer getPreviewer()
    {
      return previewer;
    }

    public PropertiesViewer getPropertiesViewer()
    {
      return propertiesViewer;
    }

    public void validate()
    {
      String message = getValidationMessage();
      setErrorMessage(StringUtil.isEmpty(message) ? null : message);

      setPageComplete(message == null);
      getContainer().updateButtons();
    }

    @Override
    public SetupModelWizard getWizard()
    {
      return (SetupModelWizard)super.getWizard();
    }

    public String getValidationMessage()
    {
      try
      {
        SetupTemplate template = getSelectedTemplate();
        if (template != null)
        {
          return template.getMessage();
        }
      }
      catch (Exception ex)
      {
        SetupEditorPlugin.getPlugin().log(ex);
      }

      return "No template selected";
    }

    private SetupTemplate getSelectedTemplate()
    {
      return (SetupTemplate)((IStructuredSelection)templatesViewer.getSelection()).getFirstElement();
    }

    private Control getSelectedTemplateControl()
    {
      SetupTemplate template = getSelectedTemplate();
      return templateControls.get(template);
    }
  }

  public static class PreviewerLabelProvider extends DecoratingColumLabelProvider
  {
    private LabelDecorator labelDecorator;

    public PreviewerLabelProvider(ILabelProvider labelProvider, LabelDecorator labelDecorator)
    {
      super(labelProvider, labelDecorator);
      this.labelDecorator = labelDecorator;
    }

    @Override
    public Font getFont(Object element)
    {
      return labelDecorator.decorateFont(fontProvider.getFont(element), element);
    }

    @Override
    public Color getBackground(Object element)
    {
      return labelDecorator.decorateBackground(colorProvider.getBackground(element), element);
    }

    @Override
    public Color getForeground(Object element)
    {
      return labelDecorator.decorateForeground(colorProvider.getForeground(element), element);
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class NewProjectWizard extends SetupModelWizard
  {
    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {
      super.init(workbench, selection);
      setWindowTitle(SetupEditorPlugin.INSTANCE.getString("_UI_Wizard_label"));
      setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(SetupEditorPlugin.INSTANCE.getImage("full/wizban/NewSetup.png")));
    }

    @Override
    protected void configureTemplateUsagePage(TemplateUsagePage templateUsagePage)
    {
      templateUsagePage.setTitle(SetupEditorPlugin.INSTANCE.getString("_UI_SetupModelWizard_label"));
      templateUsagePage.addTemplate("Eclipse Project", "@EclipseProjectTemplate@.setup");
      templateUsagePage.addTemplate("Simple Project", "@SimpleProjectTemplate@.setup");
      templateUsagePage.addTemplate("Github Project", "@GithubProjectTemplate@.setup");
    }
  }

  /**
   * @author Ed Merks
   */
  public static class NewConfigurationWizard extends SetupModelWizard
  {
    @Override
    protected void configureTemplateUsagePage(TemplateUsagePage templateUsagePage)
    {
      templateUsagePage.setTitle(SetupEditorPlugin.INSTANCE.getString("_UI_SetupModelWizard_label3"));
      templateUsagePage.addTemplate("Simple Configuration", "@ConfigurationTemplate@.setup");
    }
  }

  /**
   * @author Ed Merks
   */
  public static class NewIndexWizard extends SetupModelWizard
  {
    @Override
    protected void configureTemplateUsagePage(TemplateUsagePage templateUsagePage)
    {
      templateUsagePage.setTitle(SetupEditorPlugin.INSTANCE.getString("_UI_SetupModelWizard_label4"));
      templateUsagePage.addTemplate("Simple Index", "@IndexTemplate@.setup");
    }
  }

  /**
   * @author Ed Merks
   */
  public static class NewProductCatalogWizard extends SetupModelWizard
  {
    @Override
    protected void configureTemplateUsagePage(TemplateUsagePage templateUsagePage)
    {
      templateUsagePage.setTitle(SetupEditorPlugin.INSTANCE.getString("_UI_SetupModelWizard_label5"));
      templateUsagePage.addTemplate("Simple Product Catalog", "@ProductCatalogTemplate@.setup");
    }
  }

  /**
   * @author Ed Merks
   */
  public static class NewProjectCatalogWizard extends SetupModelWizard
  {
    @Override
    protected void configureTemplateUsagePage(TemplateUsagePage templateUsagePage)
    {
      templateUsagePage.setTitle(SetupEditorPlugin.INSTANCE.getString("_UI_SetupModelWizard_label6"));
      templateUsagePage.addTemplate("Simple Project Catalog", "@ProjectCatalogTemplate@.setup");
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class NewProductWizard extends SetupModelWizard
  {
    @Override
    protected void configureTemplateUsagePage(TemplateUsagePage templateUsagePage)
    {
      templateUsagePage.setTitle(SetupEditorPlugin.INSTANCE.getString("_UI_SetupModelWizard_label2"));
      templateUsagePage.addTemplate("Simple User Product", "@UserProductTemplate@.setup");
    }
  }
}
