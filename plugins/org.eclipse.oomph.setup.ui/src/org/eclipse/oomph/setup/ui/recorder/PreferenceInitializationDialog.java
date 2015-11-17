/*
 * Copyright (c) 2015 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.recorder;

import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PreferencesUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ed Merks
 */
public class PreferenceInitializationDialog extends AbstractSetupDialog
{
  private static final String TITLE = "Preference Initialization";

  private final PreferenceManager preferenceManager;

  private CheckboxTreeViewer checkboxTreeViewer;

  private PreferenceDialog preferenceDialog;

  private FilteredTree filteredTree;

  public PreferenceInitializationDialog(PreferenceDialog preferenceDialog, PreferenceManager preferenceManager)
  {
    super(preferenceDialog.getShell(), "Preference Pages", 500, 600, SetupUIPlugin.INSTANCE, true);
    this.preferenceDialog = preferenceDialog;
    this.preferenceManager = preferenceManager;
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Select the preference pages to initialize.";
  }

  @Override
  public String getHelpPath()
  {
    return SetupUIPlugin.INSTANCE.getSymbolicName() + "/html/PreferenceInitializationHelp.html";
  }

  @Override
  protected void createUI(Composite parent)
  {
    final Object root = new Object();

    final Set<String> initializedPreferencePages = RecorderManager.INSTANCE.getInitializedPreferencePages();
    checkboxTreeViewer = new ContainerCheckedTreeViewer(parent, SWT.NONE);
    checkboxTreeViewer.setContentProvider(new ITreeContentProvider()
    {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public void dispose()
      {
      }

      public boolean hasChildren(Object element)
      {
        return true;
      }

      public Object getParent(Object element)
      {
        return null;
      }

      public Object[] getElements(Object inputElement)
      {
        return new Object[] { root };
      }

      public Object[] getChildren(Object parentElement)
      {
        List<IPreferenceNode> nodes = new ArrayList<IPreferenceNode>();
        if (parentElement == root)
        {
          nodes.addAll(Arrays.asList(preferenceManager.getRootSubNodes()));
        }
        else
        {
          IPreferenceNode preferenceNode = (IPreferenceNode)parentElement;
          nodes.addAll(Arrays.asList(preferenceNode.getSubNodes()));
        }

        return filter(nodes);
      }

      private Object[] filter(List<IPreferenceNode> nodes)
      {
        for (Iterator<IPreferenceNode> it = nodes.iterator(); it.hasNext();)
        {
          IPreferenceNode preferenceNode = it.next();
          if (initializedPreferencePages.contains(preferenceNode.getId()) && getChildren(preferenceNode).length == 0)
          {
            it.remove();
          }
        }

        return nodes.toArray();
      }
    });

    checkboxTreeViewer.setLabelProvider(new LabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        if (element == root)
        {
          return "All Pages";
        }

        IPreferenceNode preferenceNode = (IPreferenceNode)element;
        return preferenceNode.getLabelText();
      }
    });

    filteredTree = ReflectUtil.getValue("filteredTree", preferenceDialog);
    final TreeViewer viewer = filteredTree.getViewer();
    checkboxTreeViewer.setComparator(viewer.getComparator());

    checkboxTreeViewer.setInput(preferenceManager);

    checkboxTreeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));

    checkboxTreeViewer.expandAll();

    checkboxTreeViewer.setSubtreeChecked(root, true);

    Set<String> ignoredPreferencePages = RecorderManager.INSTANCE.getIgnoredPreferencePages();
    for (IPreferenceNode preferenceNode : preferenceManager.getElements(PreferenceManager.PRE_ORDER))
    {
      if (ignoredPreferencePages.contains(preferenceNode.getId()))
      {
        checkboxTreeViewer.setChecked(preferenceNode, false);
      }
    }

    showFirstTimeHelp(this);
  }

  @Override
  protected void okPressed()
  {
    Set<String> initializedOrCheckedPreferencePages = RecorderManager.INSTANCE.getInitializedPreferencePages();

    Object[] checkedElements = checkboxTreeViewer.getCheckedElements();
    for (Object object : checkedElements)
    {
      if (object instanceof IPreferenceNode)
      {
        IPreferenceNode preferenceNode = (IPreferenceNode)object;
        initializedOrCheckedPreferencePages.add(preferenceNode.getId());
      }
    }

    final Set<String> ignoredPreferencePages = new LinkedHashSet<String>();
    for (IPreferenceNode preferenceNode : preferenceManager.getElements(PreferenceManager.PRE_ORDER))
    {
      String id = preferenceNode.getId();
      if (!initializedOrCheckedPreferencePages.contains(id))
      {
        ignoredPreferencePages.add(id);
      }
    }

    RecorderManager.INSTANCE.setIgnoredPreferencePages(ignoredPreferencePages);

    super.okPressed();

    if (checkedElements.length <= 1)
    {
      RecorderManager.INSTANCE.disposeInitializeItem();
    }
    else
    {
      final TreeViewer viewer = filteredTree.getViewer();
      final Tree tree = viewer.getTree();
      filteredTree.getFilterControl().setText("");
      viewer.getSorter();

      IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
      IPreferenceNode selectedNode = (IPreferenceNode)selection.getFirstElement();
      final String id = selectedNode == null ? null : selectedNode.getId();

      try
      {
        ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(preferenceDialog.getShell())
        {
          @Override
          protected void configureShell(Shell shell)
          {
            super.configureShell(shell);
            shell.setText(TITLE);
          }
        };

        progressMonitorDialog.run(true, true, new IRunnableWithProgress()
        {
          public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
          {
            boolean cancel = false;
            final AtomicBoolean abort = new AtomicBoolean(false);
            final Set<String> initializedPreferencePages = RecorderManager.INSTANCE.getInitializedPreferencePages();
            try
            {
              final List<TreeItem> nodes = new ArrayList<TreeItem>();
              UIUtil.syncExec(new Runnable()
              {
                public void run()
                {
                  viewer.expandAll();
                  for (TreeItem object : tree.getItems())
                  {
                    visit(nodes, object);
                  }

                  for (Iterator<TreeItem> it = nodes.iterator(); it.hasNext();)
                  {
                    TreeItem treeItem = it.next();
                    Object data = treeItem.getData();
                    if (data instanceof IPreferenceNode)
                    {
                      IPreferenceNode preferenceNode = (IPreferenceNode)data;
                      String id = preferenceNode.getId();
                      if (ignoredPreferencePages.contains(id) || initializedPreferencePages.contains(id))
                      {
                        it.remove();
                      }
                    }
                  }
                }
              });

              monitor.beginTask("Visiting preference pages", nodes.size());

              for (final TreeItem treeItem : nodes)
              {
                if (monitor.isCanceled())
                {
                  cancel = true;
                  break;
                }

                UIUtil.syncExec(new Runnable()
                {
                  public void run()
                  {
                    StringBuilder description = new StringBuilder();
                    for (TreeItem item = treeItem; item != null; item = item.getParentItem())
                    {
                      if (description.length() != 0)
                      {
                        description.insert(0, " -> ");
                      }

                      description.insert(0, item.getText());
                    }

                    monitor.subTask(description.toString());
                    IPreferenceNode preferenceNode = (IPreferenceNode)treeItem.getData();
                    viewer.setSelection(new StructuredSelection(preferenceNode));
                    IPreferencePage currentPage = (IPreferencePage)ReflectUtil.invokeMethod("getCurrentPage", preferenceDialog);
                    if (currentPage != null && !currentPage.okToLeave())
                    {
                      monitor.setCanceled(true);
                      abort.set(true);
                    }
                    else
                    {
                      initializedPreferencePages.add(preferenceNode.getId());
                    }
                  }
                });

                monitor.worked(1);
              }

              monitor.done();
            }
            finally
            {
              RecorderManager.INSTANCE.setInitializedPreferencePages(initializedPreferencePages);

              if (!abort.get())
              {
                final boolean finalCancel = cancel;
                UIUtil.asyncExec(new Runnable()
                {
                  public void run()
                  {
                    RecorderManager.INSTANCE.cancelRecording();

                    // Keep posting events to the display thread until this dialog shell itself is disposed.
                    // Also dispose any new child shells that are created.
                    final Shell shell = preferenceDialog.getShell();
                    final List<Shell> children = Arrays.asList(shell.getShells());
                    Runnable runnable = new Runnable()
                    {
                      public void run()
                      {
                        Shell[] shells = shell.getShells();
                        for (Shell child : shells)
                        {
                          if (!children.contains(child) && shell.isVisible())
                          {
                            child.dispose();
                          }
                        }

                        UIUtil.asyncExec(shell, this);
                      }
                    };

                    UIUtil.asyncExec(shell, runnable);

                    ReflectUtil.invokeMethod(finalCancel ? "cancelPressed" : "okPressed", preferenceDialog);
                    RecorderTransaction transaction = RecorderTransaction.getInstance();
                    if (transaction != null)
                    {
                      transaction.close();
                    }

                    UIUtil.asyncExec(new Runnable()
                    {
                      public void run()
                      {
                        PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(null, id, null, null);
                        dialog.open();
                      }
                    });
                  }
                });

                if (cancel)
                {
                  throw new InterruptedException();
                }
              }
            }
          }
        });
      }
      catch (InvocationTargetException ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
      }
      catch (InterruptedException ex)
      {
        //$FALL-THROUGH$
      }
    }
  }

  protected void visit(List<TreeItem> nodes, TreeItem treeItem)
  {
    nodes.add(treeItem);
    for (TreeItem child : treeItem.getItems())
    {
      visit(nodes, child);
    }
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }
}
