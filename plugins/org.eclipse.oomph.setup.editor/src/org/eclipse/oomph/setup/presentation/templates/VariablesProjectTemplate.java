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
package org.eclipse.oomph.setup.presentation.templates;

import org.eclipse.oomph.setup.editor.ProjectTemplate;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * @author Eike Stepper
 */
public class VariablesProjectTemplate extends ProjectTemplate
{
  private static final int DEFAULT_BRANCHES = 3;

  private Spinner branches;

  private Button variables;

  public VariablesProjectTemplate()
  {
    super("Create multiple branches with optional variables");
  }

  @Override
  public Control createControl(Composite parent)
  {
    GridLayout layout = new GridLayout();
    layout.numColumns = 2;
    layout.verticalSpacing = 10;

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(layout);
    UIUtil.applyGridData(composite);

    new Label(composite, SWT.NONE).setText("Branches:");
    branches = new Spinner(composite, SWT.BORDER);
    branches.setMinimum(1);
    branches.setMaximum(10);
    branches.setIncrement(1);
    branches.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        updateModel();
      }
    });

    new Label(composite, SWT.NONE);
    variables = new Button(composite, SWT.CHECK);
    variables.setText("Reuse project-level tasks in branches");
    variables.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        getProject(true);
        updateModel();
      }
    });

    branches.setSelection(DEFAULT_BRANCHES); // Causes updateModel()
    return composite;
  }

  protected void updateModel()
  {
    try
    {
      String string = branches.getText();
      int value = Integer.parseInt(string);
      if (branches.getMinimum() <= value && value <= branches.getMaximum())
      {
        int xxx;
        // updateModel(value, variables.getSelection());
      }
    }
    catch (Exception ex)
    {
      //$FALL-THROUGH$
    }

    getContainer().validate();

    final TreeViewer preViewer = getContainer().getPreViewer();
    preViewer.getControl().getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        preViewer.expandAll();
      }
    });
  }

  // private void updateModel(int branches, boolean variables)
  // {
  // Project project = getProject();
  // EList<Stream> projectBranches = project.getStreams();
  // int branchesToDelete = projectBranches.size() - branches;
  // if (branchesToDelete > 0)
  // {
  // for (int i = 0; i < branchesToDelete; i++)
  // {
  // projectBranches.remove(1);
  // }
  // }
  // else
  // {
  // int branchesToAdd = branches - projectBranches.size();
  // int start = 0;
  //
  // if (variables)
  // {
  // EList<SetupTask> projectTasks = project.getSetupTasks();
  // if (projectTasks.isEmpty())
  // {
  // VariableTask branchNameVariable = SetupFactory.eINSTANCE.createVariableTask();
  // branchNameVariable.setName("git.branch.name");
  // projectTasks.add(branchNameVariable);
  //
  // GitCloneTask gitClone = createGitCloneTask("${git.branch.name}", "${setup.branch.dir/git/project1.git}");
  // project.getSetupTasks().add(gitClone);
  //
  // MaterializationTask materialization = createMaterializationTask();
  // project.getSetupTasks().add(materialization);
  //
  // createBranch(0, "master", true);
  // start = 1;
  // }
  //
  // for (int i = start; i < branchesToAdd; i++)
  // {
  // createBranch(1, "1." + projectBranches.size() + "-maintenance", true);
  // }
  // }
  // else
  // {
  // if (projectBranches.isEmpty())
  // {
  // createBranch(0, "master", false);
  // start = 1;
  // }
  //
  // for (int i = start; i < branchesToAdd; i++)
  // {
  // createBranch(1, "1." + projectBranches.size() + "-maintenance", false);
  // }
  // }
  // }
  // }
  //
  // private void createBranch(int index, String name, boolean variables)
  // {
  // Stream branch = SetupFactory.eINSTANCE.createStream();
  // branch.setName(name);
  // getProject().getStreams().add(index, branch);
  //
  // if (variables)
  // {
  // VariableTask branchNameOverride = SetupFactory.eINSTANCE.createVariableTask();
  // branchNameOverride.setName("git.branch.name");
  // branchNameOverride.setValue(name);
  // branch.getSetupTasks().add(branchNameOverride);
  // }
  // else
  // {
  // // GitCloneTask gitClone = createGitCloneTask(name, "${setup.branch.dir/git/project1.git}");
  // // branch.getSetupTasks().add(gitClone);
  //
  // MaterializationTask materialization = createMaterializationTask();
  // branch.getSetupTasks().add(materialization);
  // }
  // }
  //
  // private GitCloneTask createGitCloneTask(String name, String location)
  // {
  // GitCloneTask gitClone = SetupFactory.eINSTANCE.createGitCloneTask();
  // gitClone.setCheckoutBranch(name);
  // gitClone.setLocation(location);
  // gitClone.setUserID("${git.user.id}");
  // gitClone.setRemoteURI("ssh://git.foo.com/project1.git");
  // gitClone.setRemoteName("origin");
  // return gitClone;
  // }
  //
  // private MaterializationTask createMaterializationTask()
  // {
  // Component rootComponent = SetupFactory.eINSTANCE.createComponent();
  // rootComponent.setName("com.foo.project.all");
  //
  // AutomaticSourceLocator sourceLocator = SetupFactory.eINSTANCE.createAutomaticSourceLocator();
  // sourceLocator.setRootFolder("${setup.branch.dir/git/project.git}");
  //
  // Repository repository = P2Factory.eINSTANCE.createRepository();
  // repository.setURL(SetupConstants.TRAIN_URL);
  //
  // MaterializationTask materialization = SetupFactory.eINSTANCE.createMaterializationTask();
  // materialization.setTargetPlatform("${setup.branch.dir/tp}");
  // materialization.getRootComponents().add(rootComponent);
  // materialization.getSourceLocators().add(sourceLocator);
  // materialization.getRepositories().add(repository);
  // return materialization;
  // }

  // /**
  // * @author Eike Stepper
  // */
  // public static final class Factory extends ProjectTemplate.Factory
  // {
  // public Factory()
  // {
  // super("variables");
  // }
  //
  // @Override
  // public ProjectTemplate createProjectTemplate()
  // {
  // return new VariablesProjectTemplate();
  // }
  // }
}
