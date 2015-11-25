/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui;

import org.eclipse.oomph.base.provider.BaseEditUtil;
import org.eclipse.oomph.base.provider.BaseEditUtil.IconReflectiveItemProvider;
import org.eclipse.oomph.base.util.EAnnotations;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.ProfileDefinition;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.setup.SetupPackage;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.VariableTask;
import org.eclipse.oomph.setup.impl.DynamicSetupTaskImpl;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupCorePlugin;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.internal.core.util.SetupCoreUtil;
import org.eclipse.oomph.setup.p2.P2Task;
import org.eclipse.oomph.setup.ui.wizards.ConfirmationPage;
import org.eclipse.oomph.ui.BackgroundProgressPart;
import org.eclipse.oomph.ui.ButtonBar;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.ProgressMonitorPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Eike Stepper
 */
public class EnablementComposite extends Composite
{
  private final ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

  private final IconReflectiveItemProvider iconItemProvider = BaseEditUtil.replaceReflectiveItemProvider(adapterFactory);

  private TreeViewer treeViewer;

  private Button offlineButton;

  private Boolean offlineProperty;

  private Button mirrorsButton;

  private Boolean mirrorsProperty;

  private ProgressMonitorPart progressMonitorPart;

  private InputData inputData;

  private InstallOperation installOperation;

  public EnablementComposite(Composite parent, int style)
  {
    super(parent, SWT.NONE);
    setLayout(UIUtil.createGridLayout(1));

    treeViewer = new TreeViewer(this, style);
    treeViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
    treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));

    Tree tree = treeViewer.getTree();
    tree.setLayoutData(new GridData(GridData.FILL_BOTH));
    AccessUtil.setKey(tree, "tree");

    ButtonBar buttonBar = new ButtonBar(this)
    {
      @Override
      protected IDialogSettings getDialogSettings()
      {
        return EnablementComposite.this.getDialogSettings();
      }
    };

    offlineProperty = PropertiesUtil.getBoolean(SetupProperties.PROP_SETUP_OFFLINE);
    if (offlineProperty == null)
    {
      offlineButton = buttonBar.addCheckButton("Offline", "Avoid unnecessary network requests during the installation process", false,
          "toggleCommand:org.eclipse.oomph.ui.ToggleOfflineMode");
      AccessUtil.setKey(offlineButton, "offline");
    }

    mirrorsProperty = PropertiesUtil.getBoolean(SetupProperties.PROP_SETUP_MIRRORS);
    if (mirrorsProperty == null)
    {
      mirrorsButton = buttonBar.addCheckButton("Mirrors", "Make use of p2 mirrors during the installation process", true, "mirrors");
      AccessUtil.setKey(mirrorsButton, "mirrors");
    }

    addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
        adapterFactory.dispose();
      }
    });
  }

  public final boolean isOffline()
  {
    return ConfirmationPage.getProperty(SetupProperties.PROP_SETUP_OFFLINE_STARTUP, offlineProperty, offlineButton);
  }

  public final boolean isMirrors()
  {
    return ConfirmationPage.getProperty(SetupProperties.PROP_SETUP_MIRRORS_STARTUP, mirrorsProperty, mirrorsButton);
  }

  public final InputData setInput(EList<SetupTask> tasks)
  {
    return setInput(getEnablementTasks(tasks));
  }

  public final InputData setInput(Map<EClass, EList<SetupTask>> enablementTasks)
  {
    if (enablementTasks.isEmpty())
    {
      treeViewer.setInput(null);
      inputData = null;
      return null;
    }

    inputData = new InputData();

    final Map<EClass, ClassItemProvider> classItemProviders = new HashMap<EClass, ClassItemProvider>();
    ItemProvider root = new ItemProvider(adapterFactory);
    EList<Object> children = root.getChildren();

    for (Map.Entry<EClass, EList<SetupTask>> entry : enablementTasks.entrySet())
    {
      EClass eClass = entry.getKey();
      EList<SetupTask> list = entry.getValue();

      inputData.enablementTasks.addAll(list);

      String typeText = EAnnotations.getText(eClass);
      if (typeText == null)
      {
        try
        {
          EObject dynamicSetupTask = EcoreUtil.create(eClass);
          typeText = iconItemProvider.getTypeText(dynamicSetupTask);
        }
        catch (Throwable ex)
        {
          typeText = eClass.getName();
        }
      }

      ClassItemProvider classItemProvider = new ClassItemProvider(adapterFactory, eClass, typeText, list);
      children.add(classItemProvider);
      classItemProviders.put(eClass, classItemProvider);
    }

    if (inputData.enablementTasks.isEmpty())
    {
      treeViewer.setInput(null);
      inputData = null;
      return null;
    }

    treeViewer.setInput(root);

    final Tree tree = treeViewer.getTree();
    UIUtil.asyncExec(tree, new Runnable()
    {
      public void run()
      {
        treeViewer.expandAll();

        final Queue<ClassItemProvider> queue = new ConcurrentLinkedQueue<ClassItemProvider>(classItemProviders.values());
        int jobs = Math.max(queue.size(), 10);

        for (int i = 0; i < jobs; i++)
        {
          Job iconLoader = new Job("IconLoader-" + i)
          {
            @Override
            protected IStatus run(IProgressMonitor monitor)
            {
              ClassItemProvider classItemProvider;
              while ((classItemProvider = queue.poll()) != null && !tree.isDisposed() && !monitor.isCanceled())
              {
                classItemProvider.loadImage();
              }

              return Status.OK_STATUS;
            }
          };

          iconLoader.setSystem(true);
          iconLoader.schedule();
        }
      }
    });

    return inputData;
  }

  public InstallOperation install(final InstallHandler handler)
  {
    if (inputData == null || inputData.repositories.isEmpty() && inputData.requirements.isEmpty())
    {
      return null;
    }

    installOperation = new InstallOperation(inputData);
    enableButtons(false);

    progressMonitorPart = new BackgroundProgressPart(this, null, true)
    {
      @Override
      public boolean isCanceled()
      {
        return installOperation.canceled || super.isCanceled();
      }
    };

    progressMonitorPart.attachToCancelComponent(null);
    progressMonitorPart.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    AccessUtil.setKey(progressMonitorPart, "progress");
    layout();

    Job job = new Job("Install extensions")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        try
        {
          ResourceSet resourceSet = SetupCoreUtil.createResourceSet();
          SetupContext setupContext = SetupContext.createUserOnly(resourceSet);
          User user = setupContext.getUser();

          Agent agent = P2Util.getAgentManager().getCurrentAgent();
          Profile profile = agent.getCurrentProfile();
          ProfileTransaction transaction = profile.change();

          SelfCommitContext commitContext = new SelfCommitContext(user);
          transaction = commitContext.migrateProfile(transaction);

          boolean mirrors = isMirrors();
          transaction.setMirrors(mirrors);

          ProfileDefinition profileDefinition = transaction.getProfileDefinition();

          EList<Repository> profileRepositories = profileDefinition.getRepositories();
          for (String url : inputData.repositories)
          {
            profileRepositories.add(P2Factory.eINSTANCE.createRepository(url));
          }

          EList<Requirement> profileRequirements = profileDefinition.getRequirements();
          for (Requirement requirement : inputData.requirements)
          {
            profileRequirements.add(requirement);
          }

          transaction.commit(commitContext, progressMonitorPart);
        }
        catch (OperationCanceledException ex)
        {
          installOperation.exception = ex;
          installOperation.canceled = true;
          //$FALL-THROUGH$
        }
        catch (CoreException ex)
        {
          installOperation.exception = ex;
          return ex.getStatus();
        }
        catch (Throwable ex)
        {
          installOperation.exception = ex;
          return SetupUIPlugin.INSTANCE.getStatus(ex);
        }
        finally
        {
          installOperation.done = true;

          UIUtil.syncExec(new Runnable()
          {
            public void run()
            {
              try
              {
                enableButtons(true);
                progressMonitorPart.dispose();
                layout();

                if (handler != null)
                {
                  if (installOperation.exception instanceof OperationCanceledException)
                  {
                    handler.installCanceled();
                  }
                  else if (installOperation.exception != null)
                  {
                    handler.installFailed(installOperation.exception);
                  }
                  else
                  {
                    handler.installSucceeded();
                  }
                }
              }
              catch (Exception ex)
              {
                //$FALL-THROUGH$
              }
            }
          });

          progressMonitorPart = null;
          installOperation = null;
        }

        return Status.OK_STATUS;
      }
    };

    job.setSystem(true);
    job.schedule();

    return installOperation;
  }

  protected IDialogSettings getDialogSettings()
  {
    return SetupUIPlugin.INSTANCE.getDialogSettings(EnablementComposite.class.getSimpleName());
  }

  private void enableButtons(boolean enabled)
  {
    offlineButton.setEnabled(enabled);
    mirrorsButton.setEnabled(enabled);
  }

  public static Map<EClass, EList<SetupTask>> getEnablementTasks(EList<SetupTask> tasks)
  {
    Map<EClass, EList<SetupTask>> result = new HashMap<EClass, EList<SetupTask>>();

    for (SetupTask task : tasks)
    {
      if (task instanceof DynamicSetupTaskImpl)
      {
        EClass eClass = task.eClass();
        if (!result.containsKey(eClass))
        {
          EList<SetupTask> enablementTasks = SetupTaskPerformer.createEnablementTasks(eClass, true);
          if (enablementTasks != null && !enablementTasks.isEmpty())
          {
            result.put(eClass, enablementTasks);
          }
        }
      }
    }

    return result;
  }

  /**
   * @author Eike Stepper
   */
  private final class ClassItemProvider extends ItemProvider
  {
    private final EClass eClass;

    public ClassItemProvider(AdapterFactory adapterFactory, EClass eClass, String typeText, EList<SetupTask> enablementTasks)
    {
      super(adapterFactory, typeText,
          SetupUIPlugin.INSTANCE.getImage(SetupPackage.Literals.SETUP_TASK.isSuperTypeOf(eClass) ? "full/obj16/SetupTask" : "full/obj16/EObject"));
      this.eClass = eClass;

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
            inputData.requirements.addAll(p2Task.getRequirements());
          }
        }
      }

      List<String> urls = new ArrayList<String>(requirements.keySet());
      Collections.sort(urls);
      inputData.repositories.addAll(urls);

      EList<Object> children = getChildren();
      Image repositoryImage = SetupUIPlugin.INSTANCE.getSWTImage("full/obj16/Repository");

      for (String url : urls)
      {
        ItemProvider repository = new ItemProvider(url, repositoryImage);
        repository.getChildren().addAll(requirements.get(url));
        children.add(repository);
      }

      children.addAll(extraRequirements);
    }

    public void loadImage()
    {
      URI imageURI = EAnnotations.getImageURI(eClass);
      if (imageURI != null)
      {
        Image image = ExtendedImageRegistry.INSTANCE.getImage(BaseEditUtil.getImage(imageURI));
        setImage(image);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class InputData
  {
    final EList<SetupTask> enablementTasks = new BasicEList<SetupTask>();

    final Set<String> repositories = new HashSet<String>();

    final Set<Requirement> requirements = new HashSet<Requirement>();

    public InputData()
    {
    }

    public EList<SetupTask> getEnablementTasks()
    {
      return enablementTasks;
    }

    public Set<String> getRepositories()
    {
      return repositories;
    }

    public Set<Requirement> getRequirements()
    {
      return requirements;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class InstallOperation
  {
    private final InputData inputData;

    boolean canceled;

    boolean done;

    Throwable exception;

    InstallOperation(InputData inputData)
    {
      this.inputData = inputData;
    }

    public void cancel()
    {
      canceled = true;
    }

    public boolean isCanceled()
    {
      return canceled;
    }

    public boolean isDone()
    {
      return done;
    }

    public Throwable getException()
    {
      return exception;
    }

    public InputData getInputData()
    {
      return inputData;
    }
  }

  /**
   * @author Eike Stepper
   */
  public interface InstallHandler
  {
    public void installSucceeded();

    public void installFailed(Throwable t);

    public void installCanceled();
  }
}
