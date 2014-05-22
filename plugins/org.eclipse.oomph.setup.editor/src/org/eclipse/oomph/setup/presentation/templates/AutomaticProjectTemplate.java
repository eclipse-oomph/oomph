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

import org.eclipse.oomph.setup.Stream;
import org.eclipse.oomph.setup.editor.ProjectTemplate;
import org.eclipse.oomph.setup.presentation.SetupEditorPlugin;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class AutomaticProjectTemplate extends ProjectTemplate
{
  // private static final Pattern GIT_URL_PATTERN = Pattern.compile("\\[remote \"([^\"]*)\"].*?url *= *([^\\r\\n]*)", Pattern.DOTALL | Pattern.MULTILINE);

  private static final String BRANCH_NAME = "master";

  private static final String REMOVE_TEXT = "&Remove";

  private static final String ASSOCIATED_ELEMENTS = "associated-elements";

  private static String lastFolder;

  private final Stream branch;

  private int folders;

  // private DocumentBuilder documentBuilder;

  public AutomaticProjectTemplate()
  {
    super("Analyze folders such as a Git working trees");

    IDialogSettings section = getSettings();
    lastFolder = section.get("lastFolder");

    branch = addBranch();
    branch.eAdapters().add(new EContentAdapter()
    {
      @Override
      public void notifyChanged(Notification notification)
      {
        super.notifyChanged(notification);
        if (!notification.isTouch())
        {
          final TreeViewer preViewer = getContainer().getPreViewer();
          if (preViewer != null)
          {
            preViewer.getControl().getDisplay().asyncExec(new Runnable()
            {
              public void run()
              {
                preViewer.setExpandedState(branch, true);
              }
            });
          }
        }
      }
    });
  }

  @Override
  public boolean isValid(Stream branch)
  {
    return super.isValid(branch) && folders > 1;
  }

  @Override
  public Control createControl(Composite parent)
  {
    GridLayout layout = new GridLayout();
    layout.numColumns = 3;
    layout.verticalSpacing = 5;

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(layout);
    UIUtil.applyGridData(composite);

    Text branchText = addBranchControl(composite);
    branchText.setText(BRANCH_NAME);

    addFolderControl(composite);
    return composite;
  }

  protected Text addBranchControl(Composite composite)
  {
    new Label(composite, SWT.NONE).setText("Branch:");

    final Text branchText = new Text(composite, SWT.BORDER);
    UIUtil.applyGridData(branchText);
    branchText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        branch.setName(branchText.getText());
        getContainer().validate();
      }
    });

    new Label(composite, SWT.NONE);
    return branchText;
  }

  protected Text addFolderControl(final Composite composite)
  {
    ++folders;

    final Label label = new Label(composite, SWT.NONE);
    label.setText("Folder:");

    final Text text = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
    UIUtil.applyGridData(text);

    final Button button = new Button(composite, SWT.NONE);
    button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    button.setBounds(0, 0, 75, 25);
    button.setText("&Add...");
    button.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        final Shell shell = text.getShell();

        if (button.getText().equals(REMOVE_TEXT))
        {
          @SuppressWarnings("unchecked")
          List<EObject> elements = (List<EObject>)text.getData(ASSOCIATED_ELEMENTS);
          for (EObject element : elements)
          {
            EcoreUtil.delete(element, true);
          }

          label.dispose();
          text.dispose();
          button.dispose();

          changeShellHeight(shell, -20);
          composite.getParent().getParent().layout();

          --folders;
          getContainer().validate();
          return;
        }

        DirectoryDialog dialog = new DirectoryDialog(shell);
        dialog.setText(AbstractSetupDialog.SHELL_TEXT);
        dialog.setMessage("Select a folder to analyze:");
        if (lastFolder != null)
        {
          dialog.setFilterPath(lastFolder);
        }

        final String folder = dialog.open();
        if (folder != null)
        {
          lastFolder = folder;

          IDialogSettings section = getSettings();
          section.put("lastFolder", lastFolder);

          try
          {
            ProgressMonitorDialog dlg = new ProgressMonitorDialog(shell);
            dlg.run(true, true, new IRunnableWithProgress()
            {
              public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
              {
                monitor.beginTask("Analyzing " + folder, IProgressMonitor.UNKNOWN);

                try
                {
                  final List<EObject> elements = new ArrayList<EObject>();

                  int xxx;
                  // analyzeFolder(new File(folder), elements, monitor);

                  text.getDisplay().syncExec(new Runnable()
                  {
                    public void run()
                    {
                      text.setData(ASSOCIATED_ELEMENTS, elements);
                      updateControl(text, button, folder);
                    }
                  });
                }
                catch (OperationCanceledException ex)
                {
                  throw new InterruptedException();
                }
                catch (Exception ex)
                {
                  throw new InvocationTargetException(ex);
                }
                finally
                {
                  monitor.done();
                }
              }

              private void updateControl(Text text, Button button, String folder)
              {
                text.setText(folder);
                button.setText(REMOVE_TEXT);

                addFolderControl(composite);

                changeShellHeight(shell, 20);
                composite.getParent().getParent().layout();
                getContainer().validate();
              }
            });
          }
          catch (Exception ex)
          {
            SetupEditorPlugin.INSTANCE.log(ex);
            MessageDialog.openError(shell, AbstractSetupDialog.SHELL_TEXT, "An error occured. The Error Log contains more information.");
          }
        }
      }
    });

    return text;
  }

  // private void analyzeFolder(File folder, List<EObject> elements, IProgressMonitor monitor) throws Exception
  // {
  // Set<String> variableNames = new HashSet<String>();
  //
  // EList<SetupTask> projectTasks = getProject().getSetupTasks();
  // for (SetupTask setupTask : projectTasks)
  // {
  // if (setupTask instanceof VariableTask)
  // {
  // VariableTask variable = (VariableTask)setupTask;
  // variableNames.add(variable.getName());
  // }
  // }
  //
  // Set<String> newVariableNames = new HashSet<String>();
  // GitCloneTask gitCloneTask = analyzeGit(folder, elements, newVariableNames);
  //
  // String location = gitCloneTask == null ? folder.getAbsolutePath() : gitCloneTask.getLocation();
  // analyzeMaterialization(branch, folder, location, elements, newVariableNames, gitCloneTask, monitor);
  //
  // List<String> list = new ArrayList<String>(newVariableNames);
  // Collections.sort(list);
  //
  // for (String variableName : list)
  // {
  // if (!variableNames.contains(variableName))
  // {
  // VariableTask variable = SetupFactory.eINSTANCE.createVariableTask();
  // variable.setName(variableName);
  //
  // projectTasks.add(variable);
  // }
  // }
  // }
  //
  // private GitCloneTask analyzeGit(File folder, List<EObject> elements, Set<String> variableNames)
  // {
  // File git = new File(folder, ".git");
  // if (git.isDirectory())
  // {
  // File config = new File(git, "config");
  // if (config.isFile())
  // {
  // String content = IOUtil.readTextFile(config);
  //
  // Map<String, String> uris = new LinkedHashMap<String, String>();
  // Matcher matcher = GIT_URL_PATTERN.matcher(content);
  // while (matcher.find())
  // {
  // uris.put(matcher.group(1), matcher.group(2));
  // }
  //
  // if (!uris.isEmpty())
  // {
  // String remoteName = "origin";
  // String mainURI = uris.get(remoteName);
  // if (mainURI == null)
  // {
  // Map.Entry<String, String> entry = uris.entrySet().iterator().next();
  // remoteName = entry.getKey();
  // mainURI = entry.getValue();
  // }
  //
  // URI baseURI = URI.createURI(mainURI);
  //
  // String userID = baseURI.userInfo();
  // if (!StringUtil.isEmpty(userID))
  // {
  // String host = baseURI.host();
  // if (!StringUtil.isEmpty(baseURI.port()))
  // {
  // host += ":" + baseURI.port();
  // }
  //
  // baseURI = URI.createHierarchicalURI(baseURI.scheme(), host, baseURI.device(), baseURI.segments(), baseURI.query(), baseURI.fragment());
  // }
  //
  // String cloneName = new Path(baseURI.path()).lastSegment();
  // if (cloneName.endsWith(".git"))
  // {
  // cloneName = cloneName.substring(0, cloneName.length() - ".git".length());
  // }
  //
  // int x;
  // // TODO this is not correct anymore.
  // String location = "${setup.branch.dir/git/" + cloneName + "}";
  //
  // GitCloneTask task = SetupFactory.eINSTANCE.createGitCloneTask();
  // task.setLocation(location);
  // task.setUserID("${git.user.id}");
  // task.setRemoteURI(baseURI.toString());
  // task.setRemoteName(remoteName);
  // task.setCheckoutBranch("master");
  //
  // branch.getSetupTasks().add(task);
  // elements.add(task);
  //
  // variableNames.add("git.user.id");
  // return task;
  // }
  // }
  // }
  //
  // return null;
  // }
  //
  // private void analyzeMaterialization(SetupTaskContainer mainContainer, File folder, String location, List<EObject> elements, Set<String> variableNames,
  // SetupTask requirement, IProgressMonitor monitor) throws Exception
  // {
  // DocumentBuilder documentBuilder = getDocumentBuilder();
  // MaterializationSniffer.analyzeMaterialization(mainContainer, folder, location, elements, variableNames, requirement, documentBuilder, monitor);
  // }
  //
  // private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException
  // {
  // if (documentBuilder == null)
  // {
  // documentBuilder = XMLUtil.createDocumentBuilder();
  // }
  //
  // return documentBuilder;
  // }

  private static void changeShellHeight(Shell shell, int delta)
  {
    // Point size = shell.getSize();
    // size.y += delta;
    // shell.setSize(size);
  }

  private static IDialogSettings getSettings()
  {
    IDialogSettings dialogSettings = SetupEditorPlugin.getPlugin().getDialogSettings();
    IDialogSettings section = dialogSettings.getSection(AutomaticProjectTemplate.class.getName());
    if (section == null)
    {
      section = dialogSettings.addNewSection(AutomaticProjectTemplate.class.getName());
    }

    return section;
  }

  // /**
  // * @author Eike Stepper
  // */
  // public static final class Factory extends ProjectTemplate.Factory
  // {
  // public Factory()
  // {
  // super("automatic");
  // }
  //
  // @Override
  // public ProjectTemplate createProjectTemplate()
  // {
  // return new AutomaticProjectTemplate();
  // }
  // }
}
