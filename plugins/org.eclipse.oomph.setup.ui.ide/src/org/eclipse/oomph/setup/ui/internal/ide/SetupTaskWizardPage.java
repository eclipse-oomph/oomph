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
package org.eclipse.oomph.setup.ui.internal.ide;

import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.codegen.util.CodeGenUtil;
import org.eclipse.emf.common.util.URI;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class SetupTaskWizardPage extends WizardPage
{
  private Text projectNameText;

  private Button containerLocationDefaultButton;

  private Text containerLocationText;

  private Button containerLocationBrowseButton;

  private Text qualifiedPackageNameText;

  private Text modelNameText;

  private Text taskNameText;

  private Button bootstrapButton;

  private Button startupButton;

  private Button manualButton;

  private Button priorityDefaultButton;

  private Text priorityText;

  private Text namespaceURIText;

  private Text schemaLocationText;

  private Text publicationLocationText;

  private Text enablementURLText;

  public SetupTaskWizardPage()
  {
    super("wizardPage");
    setTitle("Setup Task");
    setDescription("This wizard creates a new setup model extension.");
    setImageDescriptor(SetupUIIDEPlugin.INSTANCE.getImageDescriptor("extension_wiz.png"));
  }

  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 2;
    layout.verticalSpacing = 9;

    Label projectNameLabel = new Label(container, SWT.NULL);
    projectNameLabel.setText("Project Name:");
    projectNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    projectNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
    projectNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    projectNameText.setToolTipText("The name of the plugin project to contain the extension model.\n"
        + "Additional projects will be created for the edit support plugin and the extension feature.");

    Label containerLocationLabel = new Label(container, SWT.NONE);
    containerLocationLabel.setText("Container Location:");
    containerLocationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    Composite containerLocationComposite = new Composite(container, SWT.NONE);
    containerLocationComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    GridLayout containerLocationLayout = new GridLayout(3, false);
    containerLocationLayout.marginWidth = 0;
    containerLocationLayout.marginHeight = 0;
    containerLocationLayout.horizontalSpacing = 10;
    containerLocationComposite.setLayout(containerLocationLayout);

    containerLocationDefaultButton = new Button(containerLocationComposite, SWT.CHECK);
    containerLocationDefaultButton.setText("Default");
    containerLocationDefaultButton.setToolTipText("Whether the new projects will be created under the workspace location or not.");
    containerLocationDefaultButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        adjustContainerLocation();
      }
    });

    containerLocationText = new Text(containerLocationComposite, SWT.BORDER);
    containerLocationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    containerLocationText
        .setToolTipText("The physical disk location under which the new projects will be created.\n" + "The default is the workspace location.");

    containerLocationBrowseButton = new Button(containerLocationComposite, SWT.NONE);
    containerLocationBrowseButton.setText("Browse...");
    containerLocationBrowseButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        String filter = containerLocationText.getText();
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setText("Root Folder");
        dialog.setMessage("Pick a root folder in which to locate the new plug-in projects...");
        dialog.setFilterPath(filter);

        String directory = dialog.open();
        if (directory != null)
        {
          containerLocationText.setText(directory);
        }
      }
    });

    Label qualifiedPackageNameLabel = new Label(container, SWT.NULL);
    qualifiedPackageNameLabel.setText("Package Name:");
    qualifiedPackageNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    qualifiedPackageNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
    qualifiedPackageNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    qualifiedPackageNameText.setToolTipText("The Java package name in which the model interfaces will be generated.");

    Label modelNameLabel = new Label(container, SWT.NONE);
    modelNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
    modelNameLabel.setText("Model Name:");

    modelNameText = new Text(container, SWT.BORDER);
    modelNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    modelNameText.setToolTipText("The name of the model to be created. Java class name conventions apply.");

    Label taskNameLabel = new Label(container, SWT.NONE);
    taskNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
    taskNameLabel.setText("Task Name:");

    taskNameText = new Text(container, SWT.BORDER);
    taskNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    taskNameText.setToolTipText("The name of the setup task class to be created. Java class name conventions apply.");

    Label validTriggersLabel = new Label(container, SWT.NONE);
    validTriggersLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
    validTriggersLabel.setText("Valid Triggers:");

    Composite validTriggersComposite = new Composite(container, SWT.NONE);
    validTriggersComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    GridLayout validTriggersLayout = new GridLayout(3, false);
    validTriggersLayout.horizontalSpacing = 10;
    validTriggersLayout.marginWidth = 0;
    validTriggersLayout.marginHeight = 0;
    validTriggersComposite.setLayout(validTriggersLayout);

    bootstrapButton = new Button(validTriggersComposite, SWT.CHECK);
    bootstrapButton.setText("Bootstrap");
    bootstrapButton.setToolTipText("Whether the new setup task can perform on boostrap trigger.");

    startupButton = new Button(validTriggersComposite, SWT.CHECK);
    startupButton.setText("Startup");
    startupButton.setToolTipText("Whether the new setup task can perform on startup trigger.");

    manualButton = new Button(validTriggersComposite, SWT.CHECK);
    manualButton.setText("Manual");
    manualButton.setToolTipText("Whether the new setup task can perform on manual trigger.");

    Label priorityLabel = new Label(container, SWT.NONE);
    priorityLabel.setText("Priority:");
    priorityLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    Composite priorityComposite = new Composite(container, SWT.NONE);
    priorityComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    GridLayout priorityLayout = new GridLayout(2, false);
    priorityLayout.marginWidth = 0;
    priorityLayout.marginHeight = 0;
    priorityLayout.horizontalSpacing = 10;
    priorityComposite.setLayout(priorityLayout);

    priorityDefaultButton = new Button(priorityComposite, SWT.CHECK);
    priorityDefaultButton.setText("Default");
    priorityDefaultButton.setToolTipText("Whether the new setup task performs at default priority or not.");
    priorityDefaultButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        adjustPriority();
      }
    });

    priorityText = new Text(priorityComposite, SWT.BORDER);
    GridData priorityTextGridData = new GridData(SWT.LEFT, SWT.CENTER, false, false);
    priorityTextGridData.widthHint = 64;
    priorityText.setLayoutData(priorityTextGridData);
    priorityText.setToolTipText("The priority at which the new setup task will perform.\n" + "Lower values mean 'perform earlier'.");

    Label namespaceURILabel = new Label(container, SWT.NONE);
    namespaceURILabel.setText("Namespace URI:");
    namespaceURILabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    namespaceURIText = new Text(container, SWT.BORDER);
    namespaceURIText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    namespaceURIText.setToolTipText("The namespace URI of the model package to be created.\n" + "This value uniquely identifies the new extension model.");

    Label schemaLocationLabel = new Label(container, SWT.NONE);
    schemaLocationLabel.setText("Schema Location:");
    schemaLocationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    schemaLocationText = new Text(container, SWT.BORDER);
    schemaLocationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    schemaLocationText
        .setToolTipText("The public URL of the model schema to be created.\n" + "At runtime this URL must be accessible to the Oomph setup engine.\n"
            + "The EMF generator will create this public schema at the local 'Publication Location'.");

    Label publicationLocationLabel = new Label(container, SWT.NONE);
    publicationLocationLabel.setText("Publication Location:");
    publicationLocationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    publicationLocationText = new Text(container, SWT.BORDER);
    publicationLocationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    publicationLocationText.setToolTipText("A workspace-relative path where the EMF generator will create the public schema.");

    Label enablementURLLabel = new Label(container, SWT.NONE);
    enablementURLLabel.setText("Enablement URL:");
    enablementURLLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    enablementURLText = new Text(container, SWT.BORDER);
    enablementURLText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    enablementURLText.setToolTipText("The URL of the p2 repository that will contain the binary plugins and the feature of the new setup model extension.");

    initialize();

    projectNameText.addModifyListener(new ModifyListener()
    {
      private String lastValue;

      public void modifyText(ModifyEvent e)
      {
        lastValue = projectNameChanged(lastValue);
      }
    });

    listenTo(containerLocationText);
    listenTo(qualifiedPackageNameText);
    listenTo(modelNameText);
    listenTo(taskNameText);
    listenTo(priorityText);
    listenTo(namespaceURIText);
    listenTo(schemaLocationText);
    listenTo(publicationLocationText);
    listenTo(enablementURLText);

    dialogChanged();
    setControl(container);
  }

  public Map<String, String> getVariables()
  {
    String projectName = projectNameText.getText();
    String containerLocation = containerLocationText.getText();
    String qualifiedPackageName = qualifiedPackageNameText.getText();
    String modelName = modelNameText.getText();
    String taskName = taskNameText.getText();
    String formattedTaskName = CodeGenUtil.format(taskName, ' ', " ", false, false);
    String priority = priorityText.getText();
    String namespaceURI = namespaceURIText.getText();
    String schemaLocation = schemaLocationText.getText();
    String publicationLocation = publicationLocationText.getText();
    String enablementURL = enablementURLText.getText();

    final Map<String, String> variables = new HashMap<String, String>();
    variables.put("@ProjectName@", projectName);
    variables.put("@ProjectURI@", getProjectURI(projectName));
    variables.put("@ContainerLocation@", containerLocation);
    variables.put("@QualifiedPackageName@", qualifiedPackageName);
    variables.put("@PackageName@", getPackageName(qualifiedPackageName));
    variables.put("@PackagePath@", qualifiedPackageName.replace('.', '/'));
    variables.put("@BasePackage@", getBasePackage(qualifiedPackageName));
    variables.put("@ModelName@", modelName);
    variables.put("@TaskName@", taskName);
    variables.put("@taskName@", StringUtil.uncap(taskName));
    variables.put("@Task Name@", formattedTaskName);
    variables.put("@TASKNAME@", taskName.toUpperCase());
    variables.put("@TASK_NAME@", formattedTaskName.toUpperCase().replace(' ', '_'));
    variables.put("@taskname@", taskName.toLowerCase());
    variables.put("@task_name@", formattedTaskName.toLowerCase().replace(' ', '_'));
    variables.put("@task.name@", formattedTaskName.toLowerCase().replace(' ', '.'));
    variables.put("@ValidTriggers@", getValidTriggers());
    variables.put("@Priority@", Integer.parseInt(priority) == SetupTask.PRIORITY_DEFAULT ? "PRIORITY_DEFAULT" : priority);
    variables.put("@NamespaceURI@", namespaceURI);
    variables.put("@SchemaLocation@", schemaLocation);
    variables.put("@EnablementURL@", enablementURL);
    variables.put("@PublicationLocation@", publicationLocation);
    variables.put("@ProviderName@", getProviderName(qualifiedPackageName));
    return variables;
  }

  private void initialize()
  {
    containerLocationDefaultButton.setSelection(true);
    adjustContainerLocation();

    startupButton.setSelection(true);
    manualButton.setSelection(true);

    priorityDefaultButton.setSelection(true);
    adjustPriority();
  }

  private void listenTo(Text text)
  {
    text.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        dialogChanged();
      }
    });
  }

  private void adjustContainerLocation()
  {
    boolean checked = containerLocationDefaultButton.getSelection();
    containerLocationText.setEnabled(!checked);
    containerLocationBrowseButton.setEnabled(!checked);

    if (checked)
    {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      File folder = root.getLocation().toFile();
      containerLocationText.setText(folder.toString());
    }
  }

  private void adjustPriority()
  {
    boolean checked = priorityDefaultButton.getSelection();
    priorityText.setEnabled(!checked);

    if (checked)
    {
      priorityText.setText(Integer.toString(SetupTask.PRIORITY_DEFAULT));
    }
  }

  private String projectNameChanged(String lastValue)
  {
    adjustContainerLocation();

    String value = projectNameText.getText();
    if (lastValue == null || qualifiedPackageNameText.getText().equals(getQualifiedPackageName(lastValue)))
    {
      qualifiedPackageNameText.setText(getQualifiedPackageName(value));
    }

    if (lastValue == null || modelNameText.getText().equals(getModelName(getQualifiedPackageName(lastValue))))
    {
      modelNameText.setText(getModelName(getQualifiedPackageName(value)));
    }

    if (lastValue == null || taskNameText.getText().equals(getTaskName(lastValue)))
    {
      taskNameText.setText(getTaskName(value));
    }

    String uri = getProjectURI(value);

    String namespaceURISuffix = "/1.0";
    if (lastValue == null || namespaceURIText.getText().equals(getProjectURI(lastValue) + namespaceURISuffix))
    {
      namespaceURIText.setText(uri + namespaceURISuffix);
    }

    if (lastValue == null
        || schemaLocationText.getText().equals(getProjectURI(lastValue) + "/schemas/" + getModelName(getQualifiedPackageName(lastValue)) + "-1.0.ecore"))
    {
      schemaLocationText.setText(uri + "/schemas/" + getModelName(getQualifiedPackageName(value)) + "-1.0.ecore");
    }

    if (lastValue == null || publicationLocationText.getText().equals(getPublicationLocation(lastValue)))
    {
      publicationLocationText.setText(getPublicationLocation(value));
    }

    String enablementURLSuffix = "/updates";
    if (lastValue == null || enablementURLText.getText().equals(getProjectURI(lastValue, "download") + enablementURLSuffix))
    {
      enablementURLText.setText(getProjectURI(value, "download") + enablementURLSuffix);
    }

    lastValue = value;
    dialogChanged();
    return value;
  }

  private void dialogChanged()
  {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IWorkspaceRoot root = workspace.getRoot();

    String projectName = projectNameText.getText();
    IStatus projectNameStatus = workspace.validateName(projectName, IResource.PROJECT);
    if (!projectNameStatus.isOK())
    {
      String message = projectNameStatus.getMessage();
      message = StringUtil.uncap(message);
      message = message.replace("resource name", "name");
      message = message.replace("folder name", "name");
      message = message.replace("name", "project name");
      message = StringUtil.cap(message);
      updateStatus(message);
      return;
    }

    IProject project = root.getProject(projectName);
    if (project.exists())
    {
      updateStatus("Project '" + projectName + "' exists already.");
      return;
    }

    String containerLocation = containerLocationText.getText();
    if (containerLocation.length() == 0)
    {
      updateStatus("Container location is empty.");
      return;
    }

    List<File> projectLocations = SetupTaskWizard.getProjectLocations(projectName, containerLocation);
    for (File projectLocation : projectLocations)
    {
      if (projectLocation.exists())
      {
        updateStatus("Project location '" + projectLocation + "' exists already.");
        return;
      }
    }

    String qualifiedPackageName = qualifiedPackageNameText.getText();
    if (qualifiedPackageName.length() == 0)
    {
      updateStatus("Package name is empty.");
      return;
    }

    if (!JavaConventions.validatePackageName(qualifiedPackageName, JavaCore.VERSION_1_5, JavaCore.VERSION_1_5).isOK())
    {
      updateStatus("Package name '" + qualifiedPackageName + "' is not valid in Java.");
      return;
    }

    String modelName = modelNameText.getText();
    if (modelName.length() == 0)
    {
      updateStatus("Model name is empty.");
      return;
    }

    if (!JavaConventions.validateClassFileName(modelName + "Package.class", JavaCore.VERSION_1_5, JavaCore.VERSION_1_5).isOK())
    {
      updateStatus("Model name '" + modelName + "' is not valid in Java.");
      return;
    }

    String taskName = taskNameText.getText();
    if (taskName.length() == 0)
    {
      updateStatus("Task name is empty.");
      return;
    }

    if (!JavaConventions.validateClassFileName(taskName + ".class", JavaCore.VERSION_1_5, JavaCore.VERSION_1_5).isOK())
    {
      updateStatus("Task name '" + taskName + "' is not valid in Java.");
      return;
    }

    if (taskName.endsWith("Task"))
    {
      updateStatus("Task name should not end with 'Task'.");
      return;
    }

    try
    {
      String priority = priorityText.getText();
      Integer.parseInt(priority);
    }
    catch (Exception ex)
    {
      updateStatus("Priority is not a valid integer number.");
      return;
    }

    String namespaceURI = namespaceURIText.getText();
    if (namespaceURI.length() == 0)
    {
      updateStatus("Namespace URI is empty.");
      return;
    }

    try
    {
      URI.createURI(namespaceURI);
    }
    catch (Exception ex)
    {
      updateStatus("Namespace URI is not valid.");
      return;
    }

    updateStatus(null);
  }

  private void updateStatus(String message)
  {
    setErrorMessage(message);
    setPageComplete(message == null);
  }

  private String getValidTriggers()
  {
    StringBuilder builder = new StringBuilder();
    if (bootstrapButton.getSelection())
    {
      builder.append(Trigger.BOOTSTRAP);
    }

    if (startupButton.getSelection())
    {
      builder.append(builder.length() == 0 ? "" : " ");
      builder.append(Trigger.STARTUP);
    }

    if (manualButton.getSelection())
    {
      builder.append(builder.length() == 0 ? "" : " ");
      builder.append(Trigger.MANUAL);
    }

    return builder.toString();
  }

  private static String getProviderName(String projectName)
  {
    String[] segments = projectName.split("\\.");
    if (segments.length >= 2)
    {
      return StringUtil.cap(segments[1]) + "." + segments[0];
    }

    return StringUtil.cap(projectName.replace('.', ' '));
  }

  private static String getProjectURI(String projectName)
  {
    return getProjectURI(projectName, "www");
  }

  private static String getProjectURI(String projectName, String host)
  {
    String[] segments = projectName.split("\\.");
    if (segments.length >= 2)
    {
      String uri = "http://" + host + "." + segments[1] + "." + segments[0];
      for (int i = 2; i < segments.length; i++)
      {
        uri += "/" + segments[i];
      }

      return uri;
    }

    return "http://" + host + ".example.org/" + projectName.replace('.', '/');
  }

  private static String getPackageName(String qualifiedPackageName)
  {
    int lastDot = qualifiedPackageName.lastIndexOf('.');
    if (lastDot != -1)
    {
      return qualifiedPackageName.substring(lastDot + 1);
    }

    return qualifiedPackageName;
  }

  private static String getBasePackage(String qualifiedPackageName)
  {
    int lastDot = qualifiedPackageName.lastIndexOf('.');
    if (lastDot != -1)
    {
      return qualifiedPackageName.substring(0, lastDot);
    }

    return "";
  }

  private static String getModelName(String qualifiedPackageName)
  {
    int lastDot = qualifiedPackageName.lastIndexOf('.');
    if (lastDot != -1)
    {
      return StringUtil.cap(qualifiedPackageName.substring(lastDot + 1));
    }

    return StringUtil.cap(qualifiedPackageName);
  }

  private static String getQualifiedPackageName(String projectName)
  {
    return projectName.toLowerCase().replace(' ', '.');
  }

  private static String getTaskName(String projectName)
  {
    String[] segments = projectName.split("\\.");
    return StringUtil.cap(segments[segments.length - 1]);
  }

  private static String getPublicationLocation(String projectName)
  {
    return "/" + projectName + "/model/" + getModelName(getQualifiedPackageName(projectName)) + "-1.0.ecore";
  }
}
