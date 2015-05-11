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

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.base.util.EAnnotations;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.internal.core.SetupCorePlugin;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.CollectionUtil;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class EnablementDialog extends AbstractSetupDialog
{
  private static final String TITLE = "Oomph Extension Installation";

  private final EClass eClass;

  private final String typeText;

  private final EList<SetupTask> enablementTasks;

  private final String defaultImageKey;

  private ComposedAdapterFactory adapterFactory;

  public EnablementDialog(Shell parentShell, EClass eClass, String typeText, EList<SetupTask> enablementTasks, String defaultImageKey)
  {
    super(parentShell, TITLE, 650, 400, SetupEditorPlugin.INSTANCE, false);
    this.eClass = eClass;
    this.typeText = typeText;
    this.enablementTasks = enablementTasks;
    this.defaultImageKey = defaultImageKey;

    adapterFactory = BaseEditUtil.createAdapterFactory();
  }

  @Override
  public boolean close()
  {
    adapterFactory.dispose();
    return super.close();
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Review the extension details and press Install to proceed with the installation.";
  }

  @Override
  protected int getContainerMargin()
  {
    return 10;
  }

  @Override
  protected void createUI(Composite parent)
  {
    initializeDialogUnits(parent);

    ItemProvider input = createTreeViewerInput();

    Composite mainComposite = new Composite(parent, SWT.NONE);
    mainComposite.setLayout(UIUtil.createGridLayout(2));
    mainComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    final Label extensionImageLabel = new Label(mainComposite, SWT.NONE);
    extensionImageLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    extensionImageLabel.setImage(SetupEditorPlugin.INSTANCE.getSWTImage(defaultImageKey));

    Label extensionTextLabel = new Label(mainComposite, SWT.NONE);
    extensionTextLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false));
    extensionTextLabel.setText("Install " + typeText + " extension:");

    final TreeViewer treeViewer = new TreeViewer(mainComposite, SWT.BORDER);
    treeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    treeViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
    treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
    treeViewer.setInput(input);

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        treeViewer.expandAll();

        Job iconLoader = new Job("IconLoader")
        {
          @Override
          protected IStatus run(IProgressMonitor monitor)
          {
            URI imageURI = EAnnotations.getImageURI(eClass);
            if (imageURI != null)
            {
              final Image image = ExtendedImageRegistry.INSTANCE.getImage(BaseEditUtil.getImage(imageURI));

              UIUtil.asyncExec(new Runnable()
              {
                public void run()
                {
                  extensionImageLabel.setImage(image);
                }
              });
            }

            return Status.OK_STATUS;
          }
        };

        iconLoader.setSystem(true);
        iconLoader.schedule();
      }
    });
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    createButton(parent, IDialogConstants.OK_ID, "Install", true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  private ItemProvider createTreeViewerInput()
  {
    Map<String, Set<Requirement>> requirements = new HashMap<String, Set<Requirement>>();
    List<Requirement> extraRequirements = new ArrayList<Requirement>();

    int size = enablementTasks.size();
    for (int i = 0; i < size; i++)
    {
      SetupTask task = enablementTasks.get(i);
      if (task instanceof P2Task)
      {
        P2Task p2Task = (P2Task)task;
        EList<Repository> repositories = p2Task.getRepositories();
        if (repositories.isEmpty())
        {
          extraRequirements.addAll(p2Task.getRequirements());
        }
        else
        {
          Repository repository = repositories.get(0);
          String url = repository.getURL();

          if (url.startsWith("${") && i + 1 < size)
          {
            SetupTask nextTask = enablementTasks.get(i + 1);
            if (nextTask instanceof VariableTask)
            {
              VariableTask variableTask = (VariableTask)nextTask;
              if (url.equals("${" + variableTask.getName() + "}"))
              {
                url = variableTask.getValue();
              }
            }
          }

          if (url.equals("${" + SetupProperties.PROP_UPDATE_URL + "}"))
          {
            url = SetupCorePlugin.UPDATE_URL;
          }

          CollectionUtil.addAll(requirements, url, p2Task.getRequirements());
        }
      }
    }

    List<String> urls = new ArrayList<String>(requirements.keySet());
    Collections.sort(urls);

    ItemProvider input = new ItemProvider(adapterFactory);
    EList<Object> children = input.getChildren();
    Image repositoryImage = SetupEditorPlugin.INSTANCE.getSWTImage("full/obj16/Repository");

    for (String url : urls)
    {
      ItemProvider repository = new ItemProvider(url, repositoryImage);
      repository.getChildren().addAll(requirements.get(url));
      children.add(repository);
    }

    children.addAll(extraRequirements);
    return input;
  }
}
