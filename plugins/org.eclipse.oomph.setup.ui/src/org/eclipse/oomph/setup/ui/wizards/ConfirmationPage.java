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

import org.eclipse.oomph.internal.setup.SetupProperties;
import org.eclipse.oomph.internal.setup.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.ui.PropertiesViewer;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedColorRegistry;

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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
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

  private boolean someTaskChecked;

  public ConfirmationPage()
  {
    super("ConfirmationPage");
    setTitle("Confirmation");
    setDescription("Review the tasks to be executed and optionally uncheck unwanted tasks.");
  }

  @Override
  protected Control createUI(final Composite parent)
  {
    GridLayout mainLayout = new GridLayout();
    mainLayout.marginHeight = 0;
    mainLayout.marginBottom = 5;

    Composite mainComposite = new Composite(parent, SWT.NONE);
    mainComposite.setLayout(mainLayout);

    SashForm horizontalSash = new SashForm(mainComposite, SWT.HORIZONTAL);
    UIUtil.grabVertical(UIUtil.applyGridData(horizontalSash));

    fillCheckPane(horizontalSash);

    SashForm verticalSash = new SashForm(horizontalSash, SWT.VERTICAL);

    fillChildrenPane(verticalSash);

    propertiesViewer = new PropertiesViewer(verticalSash, SWT.BORDER);

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

    offlineProperty = PropertiesUtil.getBoolean(SetupProperties.PROP_SETUP_OFFLINE);
    if (offlineProperty == null)
    {
      offlineButton = addCheckButton("Offline", "Avoid unnecessary network requests during the installation process", false, "offline");
    }

    mirrorsProperty = PropertiesUtil.getBoolean(SetupProperties.PROP_SETUP_MIRRORS);
    if (mirrorsProperty == null)
    {
      mirrorsButton = addCheckButton("Mirrors", "Make use of p2 mirrors during the installation process", true, "mirrors");
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
      someTaskChecked = true;

      if (overwriteButton != null)
      {
        File configurationLocation = getPerformer().getProductConfigurationLocation();
        if (!ObjectUtil.equals(configurationLocation, lastConfigurationLocation))
        {
          overwriteButton.setSelection(false);
          lastConfigurationLocation = configurationLocation;
        }

        configurationLocationExists = configurationLocation.exists();
        overwriteButton.setEnabled(configurationLocationExists);
      }

      validate();

      if (PropertiesUtil.isProperty(ProgressPage.PROP_SETUP_CONFIRM_SKIP))
      {
        System.clearProperty(ProgressPage.PROP_SETUP_CONFIRM_SKIP);
        advanceToNextPage();
      }
    }
    else
    {
      Set<URI> checkedElements = new HashSet<URI>();
      for (SetupTask setupTask : getPerformer().getTriggeredSetupTasks())
      {
        if (viewer.getChecked(setupTask))
        {
          checkedElements.add(EcoreUtil.getURI(setupTask));
        }
      }

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

      updateCheckStates();
    }

    viewer.expandAll();
  }

  private void initNeededSetupTasks()
  {
    try
    {
      getPerformer().initNeededSetupTasks();
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
      EList<SetupTask> neededSetupTasks = null;

      try
      {
        SetupTaskPerformer performer = getPerformer();
        performer.setOffline(isOffline());
        performer.setMirrors(isMirrors());

        Set<SetupTask> checkedTasks = getCheckedTasks();

        neededSetupTasks = performer.initNeededSetupTasks();
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

  private void fillCheckPane(Composite parent)
  {
    viewer = new CheckboxTreeViewer(parent, SWT.BORDER | SWT.NO_SCROLL | SWT.V_SCROLL);

    final Tree tree = viewer.getTree();
    tree.setLayoutData(new GridData(GridData.FILL_BOTH));

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
    viewer.setLabelProvider(new AdapterFactoryLabelProvider.ColorProvider(adapterFactory, normalForeground, normalBackground)
    {
      @Override
      public String getText(Object object)
      {
        if (object == ROOT_ELEMENT)
        {
          String trigger = getText(getTrigger()).toLowerCase();
          return StringUtil.cap(trigger) + " Tasks";
        }

        return super.getText(object);
      }

      @Override
      public Color getForeground(Object object)
      {
        return !(object instanceof SetupTask) || getPerformer().getNeededTasks().contains(object) ? normalForeground : disabledForeground;
      }
    });

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
    childrenViewer = new TreeViewer(verticalSash, SWT.BORDER | SWT.NO_SCROLL | SWT.V_SCROLL);
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

    final TreeColumn column = new TreeColumn(tree, SWT.NONE);
    column.setText("Nested Elements");
    column.setWidth(600);

    final ControlAdapter columnResizer = new ControlAdapter()
    {
      @Override
      public void controlResized(ControlEvent e)
      {
        Point size = tree.getSize();
        ScrollBar bar = tree.getVerticalBar();
        if (bar != null && bar.isVisible())
        {
          size.x -= bar.getSize().x;
        }

        column.setWidth(size.x);
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
          detail.setInput(selection);
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
    // setMessage(null);
    setErrorMessage(null);
    setPageComplete(false);

    if (!someTaskChecked)
    {
      // setMessage("Please check one or more tasks to continue with the installation process.", IMessageProvider.WARNING);
      setErrorMessage("Please check one or more tasks to continue with the installation process.");
      return;
    }

    if (configurationLocationExists && !overwriteButton.getSelection())
    {
      setErrorMessage("The folder " + lastConfigurationLocation
          + " exists.\n Please check the Overwrite button to rename it and continue with the installation process.");
      return;
    }

    setPageComplete(true);
  }

  private boolean isShowAll()
  {
    return showAllButton.getSelection();
  }

  private boolean isOffline()
  {
    if (PropertiesUtil.isProperty(ProgressPage.PROP_SETUP_OFFLINE_STARTUP))
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
    if (PropertiesUtil.isProperty(ProgressPage.PROP_SETUP_MIRRORS_STARTUP))
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
