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
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.Trigger;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.PropertiesViewer;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

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

  private static final boolean SKIP_CONFIRM = PropertiesUtil.isProperty(SetupProperties.PROP_SETUP_CONFIRM_SKIP);

  private CheckboxTreeViewer viewer;

  private TreeViewer childrenViewer;

  private PropertiesViewer propertiesViewer;

  private Button offlineButton;

  public ConfirmationPage()
  {
    super("ConfirmationPage");
    setTitle("Confirmation");
    setDescription("Review the tasks to be executed and optionally deselect unwanted tasks.");
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

    offlineButton = new Button(mainComposite, SWT.CHECK);
    offlineButton.setText("Offline");
    offlineButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
    offlineButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        if (offlineButton.getSelection())
        {
          MessageDialog.openInformation(getShell(), AbstractSetupDialog.SHELL_TEXT, "Offline installation will be implemented soon. Stay tuned...");
        }
      }
    });

    setPageComplete(true);
    return mainComposite;
  }

  @Override
  public void enterPage(final boolean forward)
  {
    if (forward)
    {
      viewer.setInput(INPUT);
      viewer.setSubtreeChecked(ROOT_ELEMENT, true);

      if (getTrigger() == Trigger.STARTUP && SKIP_CONFIRM)
      {
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

  @Override
  public void leavePage(boolean forward)
  {
    if (forward)
    {
      try
      {
        Set<SetupTask> checkedTasks = getCheckedTasks();

        EList<SetupTask> neededSetupTasks = getPerformer().initNeededSetupTasks();
        neededSetupTasks.retainAll(checkedTasks);
      }
      catch (Exception ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
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
          children.addAll(getPerformer().getTriggeredSetupTasks());
        }

        return children.toArray();
      }
    });

    AdapterFactory adapterFactory = getAdapterFactory();
    viewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory)
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
    setPageComplete(size != 0);
  }
}
