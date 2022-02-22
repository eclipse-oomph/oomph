/*
 * Copyright (c) 2014, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.projectconfig.presentation;

import org.eclipse.oomph.internal.ui.AbstractPreferencePage;
import org.eclipse.oomph.projectconfig.WorkspaceConfiguration;
import org.eclipse.oomph.projectconfig.presentation.sync.ProjectConfigSynchronizerPreferences;
import org.eclipse.oomph.projectconfig.presentation.sync.ProjectConfigSynchronizerPreferences.PropertyModificationHandling;
import org.eclipse.oomph.projectconfig.util.ProjectConfigUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import java.lang.reflect.Method;

/**
 * @author Eike Stepper
 */
public class ProjectConfigPreferencePage extends AbstractPreferencePage
{
  private IWorkbench workbench;

  private boolean configurationManagementAutomatic = ProjectConfigSynchronizerPreferences.isConfigurationManagementAutomatic();

  private boolean configurationValidationPrompt = ProjectConfigSynchronizerPreferences.isConfigurationValidationPrompt();

  private PropertyModificationHandling propertyModificationHandling = ProjectConfigSynchronizerPreferences.getPropertyModificationHandling();

  private Button automaticPreferenceManagementButton;

  private Button ignoreErrorsButton;

  private Button promptErrorsButton;

  private Button overwriteButton;

  private Button propagate;

  private Button promptButton;

  private Group errorHandlingGroup;

  private Group modificationHandlingGroup;

  public ProjectConfigPreferencePage()
  {
    noDefaultAndApplyButton();
  }

  @Override
  public void init(IWorkbench workbench)
  {
    this.workbench = workbench;
  }

  private void update()
  {
    errorHandlingGroup.setEnabled(configurationManagementAutomatic);
    ignoreErrorsButton.setEnabled(configurationManagementAutomatic);
    promptErrorsButton.setEnabled(configurationManagementAutomatic);

    modificationHandlingGroup.setEnabled(configurationManagementAutomatic);
    overwriteButton.setEnabled(configurationManagementAutomatic);
    propagate.setEnabled(configurationManagementAutomatic);
    promptButton.setEnabled(configurationManagementAutomatic);
  }

  @Override
  protected Control doCreateContents(Composite parent)
  {
    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    layout.numColumns = 1;

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(layout);

    automaticPreferenceManagementButton = new Button(composite, SWT.CHECK);
    automaticPreferenceManagementButton.setText(Messages.ProjectConfigPreferencePage_AutomaticPreferenceManagement_label);
    automaticPreferenceManagementButton.setSelection(configurationManagementAutomatic);
    automaticPreferenceManagementButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        configurationManagementAutomatic = !configurationManagementAutomatic;
        update();
      }
    });

    {
      errorHandlingGroup = new Group(composite, SWT.NONE);
      errorHandlingGroup.setLayout(new GridLayout());
      GridData layoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
      layoutData.horizontalIndent = 10;
      errorHandlingGroup.setLayoutData(layoutData);
      errorHandlingGroup.setText(Messages.ProjectConfigPreferencePage_InvalidConfigurationHandling_label);

      {
        ignoreErrorsButton = new Button(errorHandlingGroup, SWT.RADIO);
        ignoreErrorsButton.setText(Messages.ProjectConfigPreferencePage_IgnoreErrors_label);
        if (!configurationValidationPrompt)
        {
          ignoreErrorsButton.setSelection(true);
        }

        ignoreErrorsButton.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            configurationValidationPrompt = false;
          }
        });
      }

      {
        promptErrorsButton = new Button(errorHandlingGroup, SWT.RADIO);
        promptErrorsButton.setText(Messages.ProjectConfigPreferencePage_PromptFixErrors_label);
        if (configurationValidationPrompt)
        {
          promptErrorsButton.setSelection(true);
        }

        promptErrorsButton.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            configurationValidationPrompt = true;
          }
        });
      }
    }

    {
      modificationHandlingGroup = new Group(composite, SWT.NONE);
      modificationHandlingGroup.setLayout(new GridLayout());
      GridData layoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
      layoutData.horizontalIndent = 10;
      modificationHandlingGroup.setLayoutData(layoutData);
      modificationHandlingGroup.setText(Messages.ProjectConfigPreferencePage_ManagedPropertyModificationHandling_label);

      {
        overwriteButton = new Button(modificationHandlingGroup, SWT.RADIO);
        overwriteButton.setText(Messages.ProjectConfigPreferencePage_Overwite_label);
        if (propertyModificationHandling == PropertyModificationHandling.OVERWRITE)
        {
          overwriteButton.setSelection(true);
        }

        overwriteButton.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            propertyModificationHandling = PropertyModificationHandling.OVERWRITE;
          }
        });
      }

      {
        propagate = new Button(modificationHandlingGroup, SWT.RADIO);
        propagate.setText(Messages.ProjectConfigPreferencePage_Propagate_label);
        if (propertyModificationHandling == PropertyModificationHandling.PROPAGATE)
        {
          propagate.setSelection(true);
        }

        propagate.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            propertyModificationHandling = PropertyModificationHandling.PROPAGATE;
          }
        });
      }

      {
        promptButton = new Button(modificationHandlingGroup, SWT.RADIO);
        promptButton.setText(Messages.ProjectConfigPreferencePage_Prompt_label);
        if (propertyModificationHandling == PropertyModificationHandling.PROMPT)
        {
          promptButton.setSelection(true);
        }

        promptButton.addSelectionListener(new SelectionAdapter()
        {
          @Override
          public void widgetSelected(SelectionEvent e)
          {
            propertyModificationHandling = PropertyModificationHandling.PROMPT;
          }
        });
      }
    }

    update();

    Label label = new Label(composite, SWT.NONE);
    label.setText(Messages.ProjectConfigPreferencePage_ManageConfigurations_label);

    TreeViewer treeViewer = new TreeViewer(composite);
    AdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
    treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
    treeViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
    treeViewer.setInput(ProjectConfigUtil.getWorkspaceConfiguration().eResource());
    treeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

    return composite;
  }

  @Override
  protected void contributeButtons(Composite parent)
  {
    super.contributeButtons(parent);

    GridLayout gridLayout = (GridLayout)parent.getLayout();
    gridLayout.numColumns += 2;

    {
      Button applyButton = new Button(parent, SWT.PUSH);
      applyButton.setText(Messages.ProjectConfigPreferencePage_Apply_label);

      Dialog.applyDialogFont(applyButton);
      int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
      Point minButtonSize = applyButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
      GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
      data.widthHint = Math.max(widthHint, minButtonSize.x);

      applyButton.setLayoutData(data);
      applyButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          performOk();

          WorkspaceConfiguration workspaceConfiguration = ProjectConfigUtil.getWorkspaceConfiguration();
          workspaceConfiguration.updatePreferenceProfileReferences();
          workspaceConfiguration.applyPreferenceProfiles();
        }
      });
    }

    {
      Button editButton = new Button(parent, SWT.PUSH);
      editButton.setText(Messages.ProjectConfigPreferencePage_Edit_label);

      Dialog.applyDialogFont(editButton);
      int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
      Point minButtonSize = editButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
      GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
      data.widthHint = Math.max(widthHint, minButtonSize.x);

      editButton.setLayoutData(data);
      editButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          performOk();

          // Invoke the close method on the preference dialog, but avoid using internal API, so do it reflectively.
          IPreferencePageContainer container = getContainer();

          try
          {
            Method method = container.getClass().getMethod("close"); //$NON-NLS-1$
            method.invoke(container);
          }
          catch (Throwable ex)
          {
            ProjectConfigEditorPlugin.INSTANCE.log(ex);
          }

          openWorkingSetsEditor();
        }
      });
    }
  }

  protected void openWorkingSetsEditor()
  {
    final IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
    Display display = activeWorkbenchWindow.getShell().getDisplay();
    display.asyncExec(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          IEditorInput editorInput = new URIEditorInput(ProjectConfigUtil.PROJECT_CONFIG_URI.appendSegment("All.projectconfig"), //$NON-NLS-1$
              Messages.ProjectConfigPreferencePage_ProjectPreferenceConfiguration_label);
          IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
          activePage.openEditor(editorInput, "org.eclipse.oomph.projectconfig.presentation.ProjectConfigEditorID"); //$NON-NLS-1$
          activePage.showView(IPageLayout.ID_PROP_SHEET);
        }
        catch (Exception ex)
        {
          ProjectConfigEditorPlugin.INSTANCE.log(ex);
        }
      }
    });
  }

  @Override
  public boolean performOk()
  {
    ProjectConfigSynchronizerPreferences.setConfigurationManagementAutomatic(configurationManagementAutomatic);
    ProjectConfigSynchronizerPreferences.setConfigurationValidationPrompt(configurationValidationPrompt);
    ProjectConfigSynchronizerPreferences.setPropertyModificationHandling(propertyModificationHandling);
    ProjectConfigSynchronizerPreferences.flush();

    ProjectConfigEditorPlugin.getPlugin().update();
    return true;
  }
}
