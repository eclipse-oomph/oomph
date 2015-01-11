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
package org.eclipse.oomph.setup.ui.wizards;

import org.eclipse.oomph.base.util.BaseUtil;
import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.ui.AccessUtil;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.Workspace;
import org.eclipse.oomph.setup.internal.core.SetupContext;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.ui.PropertiesViewer;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.ReflectUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedColorRegistry;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class ConfirmationPage extends SetupWizardPage
{
  private static final Object INPUT = new Object();

  private static final Object ROOT_ELEMENT = new Object();

  private CheckboxTreeViewer viewer;

  private TreeViewer childrenViewer;

  private PropertiesViewer propertiesViewer;

  private Button showAllButton;

  private Button offlineButton;

  private Boolean offlineProperty;

  private Button mirrorsButton;

  private Boolean mirrorsProperty;

  private Button overwriteButton;

  private File lastConfigurationLocation;

  private boolean configurationLocationExists;

  private Button switchWorkspaceButton;

  private File currentWorkspaceLocation;

  private File newWorkspaceLocation;

  private boolean someTaskChecked;

  private ControlAdapter columnResizer;

  private AdapterFactoryLabelProvider.ColorProvider labelProvider;

  public ConfirmationPage()
  {
    super("ConfirmationPage");
    setTitle("Confirmation");
    setDescription("Review the tasks to be executed and optionally uncheck unwanted tasks.");
  }

  @Override
  protected Control createUI(final Composite parent)
  {
    Composite mainComposite = new Composite(parent, SWT.NONE);
    mainComposite.setLayout(UIUtil.createGridLayout(1));

    SashForm horizontalSash = new SashForm(mainComposite, SWT.SMOOTH | SWT.HORIZONTAL);
    UIUtil.grabVertical(UIUtil.applyGridData(horizontalSash));
    AccessUtil.setKey(horizontalSash, "hsash");

    fillCheckPane(horizontalSash);

    SashForm verticalSash = new SashForm(horizontalSash, SWT.SMOOTH | SWT.VERTICAL);
    AccessUtil.setKey(verticalSash, "vsash");

    fillChildrenPane(verticalSash);

    propertiesViewer = new PropertiesViewer(verticalSash, SWT.BORDER);
    addHelpCallout(propertiesViewer.getTable(), 3);

    connectMasterDetail(viewer, childrenViewer);
    connectMasterDetail(viewer, propertiesViewer);
    connectMasterDetail(childrenViewer, propertiesViewer);

    horizontalSash.setWeights(new int[] { 3, 2 });

    setPageComplete(true);
    return mainComposite;
  }

  @Override
  protected void createCheckButtons()
  {
    showAllButton = addCheckButton("Show all triggered tasks", "Show unneeded tasks in addition to the needed tasks", false, "showAll");
    showAllButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        viewer.refresh();
      }
    });
    AccessUtil.setKey(showAllButton, "showAllTasks");

    offlineProperty = PropertiesUtil.getBoolean(SetupProperties.PROP_SETUP_OFFLINE);
    if (offlineProperty == null)
    {
      offlineButton = addCheckButton("Offline", "Avoid unnecessary network requests during the installation process", false,
          "toggleCommand:org.eclipse.oomph.ui.ToggleOfflineMode");
      AccessUtil.setKey(offlineButton, "offline");
    }

    mirrorsProperty = PropertiesUtil.getBoolean(SetupProperties.PROP_SETUP_MIRRORS);
    if (mirrorsProperty == null)
    {
      mirrorsButton = addCheckButton("Mirrors", "Make use of p2 mirrors during the installation process", true, "mirrors");
      AccessUtil.setKey(mirrorsButton, "mirrors");
    }

    if (getTrigger() == Trigger.BOOTSTRAP)
    {
      overwriteButton = addCheckButton("Overwrite", "Rename the existing configuration folder during the installation process", false, null);
      overwriteButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          validate();
        }
      });
      AccessUtil.setKey(overwriteButton, "overwrite");
    }
    else if (getWorkspace() != null)
    {
      switchWorkspaceButton = addCheckButton("Switch workspace", "Switch to a different workspace", false, null);
      switchWorkspaceButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          validate();
        }
      });
      AccessUtil.setKey(switchWorkspaceButton, "switch");
    }
  }

  @Override
  public void enterPage(final boolean forward)
  {
    if (forward)
    {
      initNeededSetupTasks();

      viewer.setInput(INPUT);
      viewer.setSubtreeChecked(ROOT_ELEMENT, true);
      someTaskChecked = getPerformer().getTriggeredSetupTasks().size() > 0;

      checkOverwrite();

      if (switchWorkspaceButton != null)
      {
        newWorkspaceLocation = getPerformer().getWorkspaceLocation();
        URI currentWorkspaceLocationURI = CommonPlugin.resolve(URI.createURI(Platform.getInstanceLocation().getURL().toString()));
        if (currentWorkspaceLocationURI.isFile())
        {
          currentWorkspaceLocation = new File(currentWorkspaceLocationURI.toFileString());
          boolean workspaceLocationChanged = newWorkspaceLocation != null && !newWorkspaceLocation.equals(currentWorkspaceLocation);
          switchWorkspaceButton.setVisible(workspaceLocationChanged);
          viewer.getControl().setEnabled(!workspaceLocationChanged);
        }
      }

      validate();

      if (getTrigger() == Trigger.STARTUP)
      {
        advanceToNextPage();
      }
    }
    else
    {
      Set<URI> checkedElements = new HashSet<URI>();
      SetupTaskPerformer performer = getPerformer();
      for (SetupTask setupTask : performer.getTriggeredSetupTasks())
      {
        if (viewer.getChecked(setupTask))
        {
          checkedElements.add(EcoreUtil.getURI(setupTask));
        }
      }
      boolean hasSuccessfullyPerformed = performer.hasSuccessfullyPerformed();

      SetupWizardPage promptPage = (SetupWizardPage)getPreviousPage();
      promptPage.enterPage(false);
      promptPage.leavePage(true);

      initNeededSetupTasks();

      viewer.refresh();

      for (SetupTask setupTask : getPerformer().getTriggeredSetupTasks())
      {
        if (checkedElements.contains(EcoreUtil.getURI(setupTask)))
        {
          viewer.setChecked(setupTask, true);
        }
      }

      if (hasSuccessfullyPerformed)
      {
        lastConfigurationLocation = null;
        checkOverwrite();
      }
      else if (overwriteButton != null)
      {
        // If we've not successfully perform and we try to perform for the current configuration location again, we'll want to overwrite it.
        overwriteButton.setSelection(true);
      }

      updateCheckStates();
    }

    viewer.expandAll();
  }

  private void checkOverwrite()
  {
    if (overwriteButton != null)
    {
      File configurationLocation = getPerformer().getProductConfigurationLocation();
      if (!ObjectUtil.equals(configurationLocation, lastConfigurationLocation))
      {
        overwriteButton.setSelection(false);
        lastConfigurationLocation = configurationLocation;
      }

      configurationLocationExists = configurationLocation.exists();
      overwriteButton.setVisible(configurationLocationExists);
    }
  }

  private void initNeededSetupTasks()
  {
    try
    {
      getPerformer().initNeededSetupTasks(new NullProgressMonitor());
    }
    catch (final Exception ex)
    {
      UIUtil.asyncExec(new Runnable()
      {
        public void run()
        {
          ErrorDialog.open(ex);
        }
      });
    }
  }

  @Override
  public void leavePage(boolean forward)
  {
    if (forward)
    {
      if (switchWorkspaceButton != null && switchWorkspaceButton.getSelection())
      {
        try
        {
          Class<?> openWorkspaceActionClass = CommonPlugin.loadClass("org.eclipse.ui.ide", "org.eclipse.ui.internal.ide.actions.OpenWorkspaceAction");
          Class<?> chooseWorkspaceDataClass = CommonPlugin.loadClass("org.eclipse.ui.ide", "org.eclipse.ui.internal.ide.ChooseWorkspaceData");
          Constructor<?> chooseWorkspaceDataConstructor = ReflectUtil.getConstructor(chooseWorkspaceDataClass, String.class);
          Object chooseWorkspaceData = chooseWorkspaceDataConstructor.newInstance(currentWorkspaceLocation.toString());

          Object openWorkspaceAction = openWorkspaceActionClass.getConstructors()[0].newInstance(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
          Class<?>[] declaredClasses = openWorkspaceActionClass.getDeclaredClasses();
          for (Class<?> declaredClass : declaredClasses)
          {
            if ("WorkspaceMRUAction".equals(declaredClass.getSimpleName()))
            {
              Constructor<?> workspaceMRUActionConstructor = ReflectUtil.getConstructor(declaredClass, openWorkspaceActionClass, String.class,
                  chooseWorkspaceDataClass);
              Action workspaceMRUAction = (Action)workspaceMRUActionConstructor.newInstance(openWorkspaceAction, newWorkspaceLocation.toString(),
                  chooseWorkspaceData);
              saveLocalFiles(getPerformer());
              workspaceMRUAction.run();
              getWizard().performCancel();
              break;
            }
          }
        }
        catch (Exception ex)
        {
          SetupUIPlugin.INSTANCE.log(ex);
          ErrorDialog.open(ex);
        }
      }
      else
      {
        EList<SetupTask> neededSetupTasks = null;

        try
        {
          SetupTaskPerformer performer = getPerformer();
          performer.setMirrors(isMirrors());
          performer.setOffline(isOffline());

          Set<SetupTask> checkedTasks = getCheckedTasks();

          neededSetupTasks = performer.initNeededSetupTasks(new NullProgressMonitor());
          neededSetupTasks.retainAll(checkedTasks);
        }
        catch (Exception ex)
        {
          if (neededSetupTasks != null)
          {
            neededSetupTasks.clear();
          }

          SetupUIPlugin.INSTANCE.log(ex);
          ErrorDialog.open(ex);
        }
      }
    }
  }

  private void saveLocalFiles(SetupTaskPerformer performer) throws Exception
  {
    // Get the user with the recorded changes and the original user.
    User user = getUser();
    User originalUser = SetupContext.createUserOnly(getResourceSet()).getUser();

    // Get the workspace with the changes and copy it.
    Workspace workspace = getWorkspace();
    Workspace copiedWorkspace = EcoreUtil.copy(workspace);

    // Find the compound task with the recorded changes.
    CompoundTask workspaceRestrictedCompoundTask = null;
    for (Iterator<EObject> it = user.eAllContents(); it.hasNext();)
    {
      EObject eObject = it.next();
      if (eObject instanceof CompoundTask)
      {
        CompoundTask compoundTask = (CompoundTask)eObject;
        if (compoundTask.getRestrictions().contains(workspace))
        {
          // Make a copy of it.
          workspaceRestrictedCompoundTask = EcoreUtil.copy(compoundTask);
          break;
        }
      }
    }

    // If we can't find it, something is seriously wrong.
    if (workspaceRestrictedCompoundTask == null)
    {
      throw new Exception("Workspace compound task could not be found");
    }

    // Put the workspace copy in a resource at the right location and save it.
    URI workspaceResourceURI = URI.createFileURI(new File(performer.getWorkspaceLocation(), ".metadata/.plugins/org.eclipse.oomph.setup/workspace.setup")
        .toString());
    Resource workspaceResource = getResourceSet().getResourceFactoryRegistry().getFactory(workspaceResourceURI).createResource(workspaceResourceURI);
    workspaceResource.getContents().add(copiedWorkspace);
    BaseUtil.saveEObject(copiedWorkspace);

    // Change the restriction to be restricted to the new workspace copy.
    workspaceRestrictedCompoundTask.getRestrictions().clear();
    workspaceRestrictedCompoundTask.getRestrictions().add(copiedWorkspace);
    workspaceRestrictedCompoundTask.setName(labelProvider.getText(copiedWorkspace));

    // Add the restricted compound task to the original user, and save it.
    originalUser.getSetupTasks().add(workspaceRestrictedCompoundTask);
    BaseUtil.saveEObject(originalUser);
  }

  private void fillCheckPane(Composite parent)
  {
    viewer = new CheckboxTreeViewer(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

    final Tree tree = viewer.getTree();
    tree.setLayoutData(new GridData(GridData.FILL_BOTH));
    addHelpCallout(tree, 1);

    viewer.setContentProvider(new ITreeContentProvider()
    {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public void dispose()
      {
      }

      public boolean hasChildren(Object element)
      {
        return element == ConfirmationPage.this || element == ROOT_ELEMENT;
      }

      public Object getParent(Object element)
      {
        if (element instanceof SetupTask)
        {
          return ROOT_ELEMENT;
        }

        if (element == ROOT_ELEMENT)
        {
          return INPUT;
        }

        return null;
      }

      public Object[] getElements(Object element)
      {
        return getChildren(element);
      }

      public Object[] getChildren(Object element)
      {
        List<Object> children = new ArrayList<Object>();

        if (element == INPUT)
        {
          children.add(ROOT_ELEMENT);
        }
        else if (element == ROOT_ELEMENT)
        {
          children.addAll(isShowAll() ? getPerformer().getTriggeredSetupTasks() : getPerformer().getNeededTasks());
        }

        return children.toArray();
      }
    });

    final Color normalForeground = viewer.getControl().getForeground();
    Color normalBackground = viewer.getControl().getBackground();
    final Color disabledForeground = ExtendedColorRegistry.INSTANCE.getColor(normalForeground, normalBackground, IItemColorProvider.GRAYED_OUT_COLOR);
    AdapterFactory adapterFactory = getAdapterFactory();
    labelProvider = new AdapterFactoryLabelProvider.ColorProvider(adapterFactory, normalForeground, normalBackground)
    {
      @Override
      public String getText(Object object)
      {
        if (object == ROOT_ELEMENT)
        {
          Trigger trigger = getTrigger();
          return StringUtil.cap(getText(trigger).toLowerCase()) + " Tasks";
        }

        return super.getText(object);
      }

      @Override
      public Image getImage(Object object)
      {
        if (object == ROOT_ELEMENT)
        {
          Trigger trigger = getTrigger();
          String key = StringUtil.cap(trigger.toString().toLowerCase()) + "Trigger";
          return SetupUIPlugin.INSTANCE.getSWTImage(key);
        }

        return super.getImage(object);
      }

      @Override
      public Color getForeground(Object object)
      {
        return !(object instanceof SetupTask) || getPerformer().getNeededTasks().contains(object) ? normalForeground : disabledForeground;
      }
    };
    viewer.setLabelProvider(labelProvider);

    viewer.addCheckStateListener(new ICheckStateListener()
    {
      public void checkStateChanged(CheckStateChangedEvent event)
      {
        boolean checked = event.getChecked();

        final Object element = event.getElement();
        if (element == ROOT_ELEMENT)
        {
          viewer.setSubtreeChecked(ROOT_ELEMENT, checked);
        }

        updateCheckStates();
      }
    });

    viewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        Object element = ((IStructuredSelection)event.getSelection()).getFirstElement();
        if (element == ROOT_ELEMENT)
        {
          viewer.setExpandedState(ROOT_ELEMENT, !viewer.getExpandedState(ROOT_ELEMENT));
        }
        else
        {
          viewer.setCheckedElements(new Object[] { element });
          updateCheckStates();
        }
      }
    });
  }

  private void fillChildrenPane(SashForm verticalSash)
  {
    childrenViewer = new TreeViewer(verticalSash, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    AdapterFactory adapterFactory = getAdapterFactory();
    childrenViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
    childrenViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory)
    {
      @Override
      public Object[] getElements(Object object)
      {
        List<Object> result = new ArrayList<Object>();
        for (Object child : super.getElements(object))
        {
          if (!(child instanceof SetupTask))
          {
            result.add(child);
          }
        }

        return result.toArray();
      }
    });

    final Tree tree = childrenViewer.getTree();
    tree.setHeaderVisible(true);
    addHelpCallout(tree, 2);

    final TreeColumn column = new TreeColumn(tree, SWT.NONE);
    column.setText("Nested Elements");
    columnResizer = new ControlAdapter()
    {
      @Override
      public void controlResized(ControlEvent e)
      {
        try
        {
          tree.setRedraw(false);

          Rectangle clientArea = tree.getClientArea();
          int clientWidth = clientArea.width - clientArea.x;

          // We get events during setInput where the tree items are disposed because the view is empty and then the column can't be packed.
          TreeItem[] items = tree.getItems();
          if (items.length > 0 && !items[0].isDisposed())
          {
            column.pack();
            int width = column.getWidth();
            if (width < clientWidth)
            {
              column.setWidth(clientWidth);
            }
          }
          else
          {
            column.setWidth(clientWidth);
          }

        }
        finally
        {
          tree.setRedraw(true);
        }
      }
    };

    tree.addControlListener(columnResizer);
    tree.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        columnResizer.controlResized(null);
      }
    });

    childrenViewer.setInput(new Object());
  }

  private void connectMasterDetail(final TreeViewer master, final Viewer detail)
  {
    master.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        if (detail != null)
        {
          Object selection = ((IStructuredSelection)master.getSelection()).getFirstElement();
          Control control = detail.getControl();
          try
          {
            control.setRedraw(false);
            detail.setInput(selection);
            columnResizer.controlResized(null);
          }
          finally
          {
            control.setRedraw(true);
          }
        }
      }
    });
  }

  private Set<SetupTask> getCheckedTasks()
  {
    Set<SetupTask> tasks = new HashSet<SetupTask>();
    for (Object object : viewer.getCheckedElements())
    {
      if (object instanceof SetupTask)
      {
        SetupTask task = (SetupTask)object;
        tasks.add(task);
      }
    }

    return tasks;
  }

  private void updateCheckStates()
  {
    Set<SetupTask> checkedTasks = getCheckedTasks();
    int size = checkedTasks.size();

    viewer.setChecked(ROOT_ELEMENT, size == getPerformer().getTriggeredSetupTasks().size());

    someTaskChecked = size != 0;
    validate();
  }

  private void validate()
  {
    setErrorMessage(null);
    setPageComplete(false);

    if (switchWorkspaceButton != null && switchWorkspaceButton.isVisible() && switchWorkspaceButton.getSelection())
    {
      setMessage("The IDE will be restarted with the new workspace " + getPerformer().getWorkspaceLocation() + ".", IMessageProvider.WARNING);
    }
    else
    {
      setMessage(null);
    }

    if (!someTaskChecked)
    {
      if (getWizard().getPerformer().getNeededTasks().size() == 0)
      {
        setMessage("No tasks need to perform.", IMessageProvider.WARNING);
      }
      else
      {
        setErrorMessage("Please check one or more tasks to continue with the installation process.");
      }

      return;
    }

    if (configurationLocationExists && !overwriteButton.getSelection())
    {
      setErrorMessage("The folder " + lastConfigurationLocation
          + " exists.\n Please check the Overwrite button to rename it and continue with the installation process.");
      return;
    }
    else if (newWorkspaceLocation != null && !ObjectUtil.equals(newWorkspaceLocation, currentWorkspaceLocation) && !switchWorkspaceButton.getSelection())
    {
      setErrorMessage("The workspace location is changed to " + getPerformer().getWorkspaceLocation()
          + ".  Please check the 'Switch workspace' button to restarted the IDE, switch to the new workspace, and continue the installation process.");
      return;
    }

    setPageComplete(true);
    setButtonState(IDialogConstants.NEXT_ID, false);
  }

  private boolean isShowAll()
  {
    return showAllButton.getSelection();
  }

  private boolean isOffline()
  {
    if (PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_OFFLINE_STARTUP))
    {
      return true;
    }

    if (offlineProperty != null)
    {
      return offlineProperty;
    }

    return offlineButton.getSelection();
  }

  private boolean isMirrors()
  {
    if (PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_MIRRORS_STARTUP))
    {
      return true;
    }

    if (mirrorsProperty != null)
    {
      return mirrorsProperty;
    }

    return mirrorsButton.getSelection();
  }
}
