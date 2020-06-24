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
import org.eclipse.osgi.util.NLS;
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
    super("wizardPage"); //$NON-NLS-1$
    setTitle(Messages.SetupTaskWizardPage_title);
    setDescription(Messages.SetupTaskWizardPage_description);
    setImageDescriptor(SetupUIIDEPlugin.INSTANCE.getImageDescriptor("extension_wiz.png")); //$NON-NLS-1$
  }

  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 2;
    layout.verticalSpacing = 9;

    Label projectNameLabel = new Label(container, SWT.NULL);
    projectNameLabel.setText(Messages.SetupTaskWizardPage_projectName_text);
    projectNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    projectNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
    projectNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    projectNameText.setToolTipText(Messages.SetupTaskWizardPage_projectName_tooltip);

    Label containerLocationLabel = new Label(container, SWT.NONE);
    containerLocationLabel.setText(Messages.SetupTaskWizardPage_containerLocation_text);
    containerLocationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    Composite containerLocationComposite = new Composite(container, SWT.NONE);
    containerLocationComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    GridLayout containerLocationLayout = new GridLayout(3, false);
    containerLocationLayout.marginWidth = 0;
    containerLocationLayout.marginHeight = 0;
    containerLocationLayout.horizontalSpacing = 10;
    containerLocationComposite.setLayout(containerLocationLayout);

    containerLocationDefaultButton = new Button(containerLocationComposite, SWT.CHECK);
    containerLocationDefaultButton.setText(Messages.SetupTaskWizardPage_containerLocationButton_text);
    containerLocationDefaultButton.setToolTipText(Messages.SetupTaskWizardPage_containerLocationButton_tooltip);
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
    containerLocationText.setToolTipText(Messages.SetupTaskWizardPage_containerLocation_tooltip);

    containerLocationBrowseButton = new Button(containerLocationComposite, SWT.NONE);
    containerLocationBrowseButton.setText(Messages.SetupTaskWizardPage_containerLocationBrowseButton_text);
    containerLocationBrowseButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        String filter = containerLocationText.getText();
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setText(Messages.SetupTaskWizardPage_containerLocationBrowseDialog_text);
        dialog.setMessage(Messages.SetupTaskWizardPage_containerLocationBrowseDialog_message);
        dialog.setFilterPath(filter);

        String directory = dialog.open();
        if (directory != null)
        {
          containerLocationText.setText(directory);
        }
      }
    });

    Label qualifiedPackageNameLabel = new Label(container, SWT.NULL);
    qualifiedPackageNameLabel.setText(Messages.SetupTaskWizardPage_packageName_text);
    qualifiedPackageNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    qualifiedPackageNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
    qualifiedPackageNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    qualifiedPackageNameText.setToolTipText(Messages.SetupTaskWizardPage_packageName_tooltip);

    Label modelNameLabel = new Label(container, SWT.NONE);
    modelNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
    modelNameLabel.setText(Messages.SetupTaskWizardPage_modelName_text);

    modelNameText = new Text(container, SWT.BORDER);
    modelNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    modelNameText.setToolTipText(Messages.SetupTaskWizardPage_modelName_tooltip);

    Label taskNameLabel = new Label(container, SWT.NONE);
    taskNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
    taskNameLabel.setText(Messages.SetupTaskWizardPage_taskName_text);

    taskNameText = new Text(container, SWT.BORDER);
    taskNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    taskNameText.setToolTipText(Messages.SetupTaskWizardPage_taskName_tooltip);

    Label validTriggersLabel = new Label(container, SWT.NONE);
    validTriggersLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
    validTriggersLabel.setText(Messages.SetupTaskWizardPage_validTriggers_text);

    Composite validTriggersComposite = new Composite(container, SWT.NONE);
    validTriggersComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    GridLayout validTriggersLayout = new GridLayout(3, false);
    validTriggersLayout.horizontalSpacing = 10;
    validTriggersLayout.marginWidth = 0;
    validTriggersLayout.marginHeight = 0;
    validTriggersComposite.setLayout(validTriggersLayout);

    bootstrapButton = new Button(validTriggersComposite, SWT.CHECK);
    bootstrapButton.setText(Messages.SetupTaskWizardPage_bootstrapButton_text);
    bootstrapButton.setToolTipText(Messages.SetupTaskWizardPage_bootstrapButton_tooltip);

    startupButton = new Button(validTriggersComposite, SWT.CHECK);
    startupButton.setText(Messages.SetupTaskWizardPage_startupButton_text);
    startupButton.setToolTipText(Messages.SetupTaskWizardPage_startupButton_tooltip);

    manualButton = new Button(validTriggersComposite, SWT.CHECK);
    manualButton.setText(Messages.SetupTaskWizardPage_manualButton_text);
    manualButton.setToolTipText(Messages.SetupTaskWizardPage_manualButton_tooltip);

    Label priorityLabel = new Label(container, SWT.NONE);
    priorityLabel.setText(Messages.SetupTaskWizardPage_priority_text);
    priorityLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    Composite priorityComposite = new Composite(container, SWT.NONE);
    priorityComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    GridLayout priorityLayout = new GridLayout(2, false);
    priorityLayout.marginWidth = 0;
    priorityLayout.marginHeight = 0;
    priorityLayout.horizontalSpacing = 10;
    priorityComposite.setLayout(priorityLayout);

    priorityDefaultButton = new Button(priorityComposite, SWT.CHECK);
    priorityDefaultButton.setText(Messages.SetupTaskWizardPage_priorityDefaultButton_text);
    priorityDefaultButton.setToolTipText(Messages.SetupTaskWizardPage_priorityDefaultButton_tooltip);
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
    priorityText.setToolTipText(Messages.SetupTaskWizardPage_priority_tooltip);

    Label namespaceURILabel = new Label(container, SWT.NONE);
    namespaceURILabel.setText(Messages.SetupTaskWizardPage_namespaceUri_text);
    namespaceURILabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    namespaceURIText = new Text(container, SWT.BORDER);
    namespaceURIText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    namespaceURIText.setToolTipText(Messages.SetupTaskWizardPage_namespaceUri_tooltip);

    Label schemaLocationLabel = new Label(container, SWT.NONE);
    schemaLocationLabel.setText(Messages.SetupTaskWizardPage_schemaLocation_text);
    schemaLocationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    schemaLocationText = new Text(container, SWT.BORDER);
    schemaLocationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    schemaLocationText.setToolTipText(Messages.SetupTaskWizardPage_schemaLocation_tooltip);

    Label publicationLocationLabel = new Label(container, SWT.NONE);
    publicationLocationLabel.setText(Messages.SetupTaskWizardPage_publicationLocation_text);
    publicationLocationLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    publicationLocationText = new Text(container, SWT.BORDER);
    publicationLocationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    publicationLocationText.setToolTipText(Messages.SetupTaskWizardPage_publicationLocation_tooltip);

    Label enablementURLLabel = new Label(container, SWT.NONE);
    enablementURLLabel.setText(Messages.SetupTaskWizardPage_enablementUrl_text);
    enablementURLLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

    enablementURLText = new Text(container, SWT.BORDER);
    enablementURLText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    enablementURLText.setToolTipText(Messages.SetupTaskWizardPage_enablementUrl_tooltip);

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
    String formattedTaskName = CodeGenUtil.format(taskName, ' ', " ", false, false); //$NON-NLS-1$
    String priority = priorityText.getText();
    String namespaceURI = namespaceURIText.getText();
    String schemaLocation = schemaLocationText.getText();
    String publicationLocation = publicationLocationText.getText();
    String enablementURL = enablementURLText.getText();

    final Map<String, String> variables = new HashMap<String, String>();
    variables.put("@ProjectName@", projectName); //$NON-NLS-1$
    variables.put("@ProjectURI@", getProjectURI(projectName)); //$NON-NLS-1$
    variables.put("@ContainerLocation@", containerLocation); //$NON-NLS-1$
    variables.put("@QualifiedPackageName@", qualifiedPackageName); //$NON-NLS-1$
    variables.put("@PackageName@", getPackageName(qualifiedPackageName)); //$NON-NLS-1$
    variables.put("@PackagePath@", qualifiedPackageName.replace('.', '/')); //$NON-NLS-1$
    variables.put("@BasePackage@", getBasePackage(qualifiedPackageName)); //$NON-NLS-1$
    variables.put("@ModelName@", modelName); //$NON-NLS-1$
    variables.put("@TaskName@", taskName); //$NON-NLS-1$
    variables.put("@taskName@", StringUtil.uncap(taskName)); //$NON-NLS-1$
    variables.put("@Task Name@", formattedTaskName); //$NON-NLS-1$
    variables.put("@TASKNAME@", taskName.toUpperCase()); //$NON-NLS-1$
    variables.put("@TASK_NAME@", formattedTaskName.toUpperCase().replace(' ', '_')); //$NON-NLS-1$
    variables.put("@taskname@", taskName.toLowerCase()); //$NON-NLS-1$
    variables.put("@task_name@", formattedTaskName.toLowerCase().replace(' ', '_')); //$NON-NLS-1$
    variables.put("@task.name@", formattedTaskName.toLowerCase().replace(' ', '.')); //$NON-NLS-1$
    variables.put("@ValidTriggers@", getValidTriggers()); //$NON-NLS-1$
    variables.put("@Priority@", Integer.parseInt(priority) == SetupTask.PRIORITY_DEFAULT ? "PRIORITY_DEFAULT" : priority); //$NON-NLS-1$ //$NON-NLS-2$
    variables.put("@NamespaceURI@", namespaceURI); //$NON-NLS-1$
    variables.put("@SchemaLocation@", schemaLocation); //$NON-NLS-1$
    variables.put("@EnablementURL@", enablementURL); //$NON-NLS-1$
    variables.put("@PublicationLocation@", publicationLocation); //$NON-NLS-1$
    variables.put("@ProviderName@", getProviderName(qualifiedPackageName)); //$NON-NLS-1$
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

    String namespaceURISuffix = "/1.0"; //$NON-NLS-1$
    if (lastValue == null || namespaceURIText.getText().equals(getProjectURI(lastValue) + namespaceURISuffix))
    {
      namespaceURIText.setText(uri + namespaceURISuffix);
    }

    if (lastValue == null
        || schemaLocationText.getText().equals(getProjectURI(lastValue) + "/schemas/" + getModelName(getQualifiedPackageName(lastValue)) + "-1.0.ecore")) //$NON-NLS-1$ //$NON-NLS-2$
    {
      schemaLocationText.setText(uri + "/schemas/" + getModelName(getQualifiedPackageName(value)) + "-1.0.ecore"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    if (lastValue == null || publicationLocationText.getText().equals(getPublicationLocation(lastValue)))
    {
      publicationLocationText.setText(getPublicationLocation(value));
    }

    String enablementURLSuffix = "/updates"; //$NON-NLS-1$
    if (lastValue == null || enablementURLText.getText().equals(getProjectURI(lastValue, "download") + enablementURLSuffix)) //$NON-NLS-1$
    {
      enablementURLText.setText(getProjectURI(value, "download") + enablementURLSuffix); //$NON-NLS-1$
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
      message = message.replace("resource name", "name"); //$NON-NLS-1$ //$NON-NLS-2$
      message = message.replace("folder name", "name"); //$NON-NLS-1$ //$NON-NLS-2$
      message = message.replace("name", Messages.SetupTaskWizardPage_status_projectName); //$NON-NLS-1$
      message = StringUtil.cap(message);
      updateStatus(message);
      return;
    }

    IProject project = root.getProject(projectName);
    if (project.exists())
    {
      updateStatus(NLS.bind(Messages.SetupTaskWizardPage_status_projectExists, projectName));
      return;
    }

    String containerLocation = containerLocationText.getText();
    if (containerLocation.length() == 0)
    {
      updateStatus(Messages.SetupTaskWizardPage_status_containerLocationEmpty);
      return;
    }

    List<File> projectLocations = SetupTaskWizard.getProjectLocations(projectName, containerLocation);
    for (File projectLocation : projectLocations)
    {
      if (projectLocation.exists())
      {
        updateStatus(NLS.bind(Messages.SetupTaskWizardPage_status_projectLocationExists, projectLocation));
        return;
      }
    }

    String qualifiedPackageName = qualifiedPackageNameText.getText();
    if (qualifiedPackageName.length() == 0)
    {
      updateStatus(Messages.SetupTaskWizardPage_status_packageNameEmpty);
      return;
    }

    if (!JavaConventions.validatePackageName(qualifiedPackageName, JavaCore.VERSION_1_5, JavaCore.VERSION_1_5).isOK())
    {
      updateStatus(NLS.bind(Messages.SetupTaskWizardPage_status_packageNameNotValid, qualifiedPackageName));
      return;
    }

    String modelName = modelNameText.getText();
    if (modelName.length() == 0)
    {
      updateStatus(Messages.SetupTaskWizardPage_status_modelNameEmpty);
      return;
    }

    if (!JavaConventions.validateClassFileName(modelName + "Package.class", JavaCore.VERSION_1_5, JavaCore.VERSION_1_5).isOK()) //$NON-NLS-1$
    {
      updateStatus(NLS.bind(Messages.SetupTaskWizardPage_status_modelNameNotValid, modelName));
      return;
    }

    String taskName = taskNameText.getText();
    if (taskName.length() == 0)
    {
      updateStatus(Messages.SetupTaskWizardPage_status_taskNameEmpty);
      return;
    }

    if (!JavaConventions.validateClassFileName(taskName + ".class", JavaCore.VERSION_1_5, JavaCore.VERSION_1_5).isOK()) //$NON-NLS-1$
    {
      updateStatus(NLS.bind(Messages.SetupTaskWizardPage_status_taskNameNotValid, taskName));
      return;
    }

    if (taskName.endsWith("Task")) //$NON-NLS-1$
    {
      updateStatus(Messages.SetupTaskWizardPage_status_taskShouldEndWithTask);
      return;
    }

    try
    {
      String priority = priorityText.getText();
      Integer.parseInt(priority);
    }
    catch (Exception ex)
    {
      updateStatus(Messages.SetupTaskWizardPage_status_priorityNotValid);
      return;
    }

    String namespaceURI = namespaceURIText.getText();
    if (namespaceURI.length() == 0)
    {
      updateStatus(Messages.SetupTaskWizardPage_status_namespaceUriEmpty);
      return;
    }

    try
    {
      URI.createURI(namespaceURI);
    }
    catch (Exception ex)
    {
      updateStatus(Messages.SetupTaskWizardPage_status_namespaceUriNotValid);
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
      builder.append(builder.length() == 0 ? "" : " "); //$NON-NLS-1$ //$NON-NLS-2$
      builder.append(Trigger.STARTUP);
    }

    if (manualButton.getSelection())
    {
      builder.append(builder.length() == 0 ? "" : " "); //$NON-NLS-1$ //$NON-NLS-2$
      builder.append(Trigger.MANUAL);
    }

    return builder.toString();
  }

  private static String getProviderName(String projectName)
  {
    String[] segments = projectName.split("\\."); //$NON-NLS-1$
    if (segments.length >= 2)
    {
      return StringUtil.cap(segments[1]) + "." + segments[0]; //$NON-NLS-1$
    }

    return StringUtil.cap(projectName.replace('.', ' '));
  }

  private static String getProjectURI(String projectName)
  {
    return getProjectURI(projectName, "www"); //$NON-NLS-1$
  }

  private static String getProjectURI(String projectName, String host)
  {
    String[] segments = projectName.split("\\."); //$NON-NLS-1$
    if (segments.length >= 2)
    {
      String uri = "http://" + host + '.' + segments[1] + '.' + segments[0]; //$NON-NLS-1$
      for (int i = 2; i < segments.length; i++)
      {
        uri += "/" + segments[i]; //$NON-NLS-1$
      }

      return uri;
    }

    return "http://" + host + ".example.org/" + projectName.replace('.', '/'); //$NON-NLS-1$ //$NON-NLS-2$
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

    return ""; //$NON-NLS-1$
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
    String[] segments = projectName.split("\\."); //$NON-NLS-1$
    return StringUtil.cap(segments[segments.length - 1]);
  }

  private static String getPublicationLocation(String projectName)
  {
    return '/' + projectName + "/model/" + getModelName(getQualifiedPackageName(projectName)) + "-1.0.ecore"; //$NON-NLS-1$ //$NON-NLS-2$
  }
}
