/*
 * Copyright (c) 2015, 2017 Ed Merks (Berlin, Germany) and others.
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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.ContainerCheckedTreeViewer;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PreferencesUtil;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
    checkboxTreeViewer = new ContainerCheckedTreeViewer(parent, SWT.NONE | SWT.MULTI);
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

    checkboxTreeViewer.getTree().addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.character == ' ')
        {
          IStructuredSelection selection = (IStructuredSelection)checkboxTreeViewer.getSelection();
          boolean check = true;
          for (Object object : selection.toArray())
          {
            if (checkboxTreeViewer.getChecked(object) || checkboxTreeViewer.getGrayed(object))
            {
              check = false;
              break;
            }
          }

          for (Object object : selection.toArray())
          {
            checkboxTreeViewer.setChecked(object, check);
          }

          e.doit = false;
        }
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
    @SuppressWarnings("all")
    List<IPreferenceNode> preferenceNodes = preferenceManager.getElements(PreferenceManager.PRE_ORDER);
    for (IPreferenceNode preferenceNode : preferenceNodes)
    {
      if (ignoredPreferencePages.contains(preferenceNode.getId()))
      {
        checkboxTreeViewer.setChecked(preferenceNode, false);
      }
    }

    showFirstTimeHelp(this);
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected void okPressed()
  {
    Set<String> initializedOrCheckedPreferencePages = RecorderManager.INSTANCE.getInitializedPreferencePages();
    final Set<String> checkedPreferencePages = new LinkedHashSet<String>();

    Object[] checkedElements = checkboxTreeViewer.getCheckedElements();
    for (Object object : checkedElements)
    {
      if (object instanceof IPreferenceNode)
      {
        IPreferenceNode preferenceNode = (IPreferenceNode)object;
        String id = preferenceNode.getId();
        initializedOrCheckedPreferencePages.add(id);
        checkedPreferencePages.add(id);
      }
    }

    final Set<String> ignoredPreferencePages = new LinkedHashSet<String>();
    @SuppressWarnings("all")
    List<IPreferenceNode> preferenceNodes = preferenceManager.getElements(PreferenceManager.PRE_ORDER);
    for (IPreferenceNode preferenceNode : preferenceNodes)
    {
      String id = preferenceNode.getId();
      if (!initializedOrCheckedPreferencePages.contains(id))
      {
        ignoredPreferencePages.add(id);
      }
    }

    RecorderManager.INSTANCE.setIgnoredPreferencePages(ignoredPreferencePages);

    super.okPressed();

    if (checkedElements.length == 0)
    {
      RecorderManager.INSTANCE.disposeInitializeItem();
    }
    else
    {
      new Initializer(preferenceDialog, checkedPreferencePages, ignoredPreferencePages).run();
    }
  }

  /**
   * @author Ed Merks
   */
  private static class Initializer implements Runnable
  {
    private final String originalID;

    private PreferenceDialog preferenceDialog;

    private FilteredTree filteredTree;

    private TreeViewer viewer;

    final Set<String> initializedPreferencePages = RecorderManager.INSTANCE.getInitializedPreferencePages();

    final Map<String, IPreferenceNode> nodes = new LinkedHashMap<String, IPreferenceNode>();

    final Set<IPreferenceNode> visitedNodes = new HashSet<IPreferenceNode>();

    public Initializer(PreferenceDialog preferenceDialog, Set<String> checkedPreferencePages, Set<String> ignoredPreferencePages)
    {
      setPreferenceDialog(preferenceDialog);

      filteredTree.getFilterControl().setText("");

      IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
      IPreferenceNode selectedNode = (IPreferenceNode)selection.getFirstElement();
      originalID = selectedNode == null ? null : selectedNode.getId();

      // Build a map of all nodes we need to visit.
      final Tree tree = viewer.getTree();
      viewer.expandAll();
      for (TreeItem object : tree.getItems())
      {
        visit(nodes, object);
      }

      for (Iterator<IPreferenceNode> it = nodes.values().iterator(); it.hasNext();)
      {
        IPreferenceNode preferenceNode = it.next();
        String id = preferenceNode.getId();
        checkedPreferencePages.remove(id);
        if (ignoredPreferencePages.contains(id) || initializedPreferencePages.contains(id))
        {
          it.remove();
        }
      }

      // Any checked page that doesn't really exist in the expanded tree we'll treat as if we've visited it already.
      initializedPreferencePages.addAll(checkedPreferencePages);
    }

    private void setPreferenceDialog(PreferenceDialog preferenceDialog)
    {
      this.preferenceDialog = preferenceDialog;
      filteredTree = ReflectUtil.getValue("filteredTree", preferenceDialog);
      viewer = filteredTree.getViewer();
    }

    protected void visit(Map<String, IPreferenceNode> nodes, TreeItem treeItem)
    {
      Object data = treeItem.getData();
      if (data instanceof IPreferenceNode)
      {
        IPreferenceNode preferenceNode = (IPreferenceNode)data;
        StringBuilder description = new StringBuilder();
        for (TreeItem item = treeItem; item != null; item = item.getParentItem())
        {
          if (description.length() != 0)
          {
            description.insert(0, " -> ");
          }

          description.insert(0, item.getText());
        }
        nodes.put(description.toString(), preferenceNode);
      }

      for (TreeItem child : treeItem.getItems())
      {
        visit(nodes, child);
      }
    }

    public void run()
    {
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
            final Set<IPreferenceNode> badPages = new HashSet<IPreferenceNode>();
            try
            {
              monitor.beginTask("Visiting preference pages", nodes.size());

              Map<String, IPreferenceNode> remainingNodes = new LinkedHashMap<String, IPreferenceNode>(nodes);
              remainingNodes.values().removeAll(visitedNodes);
              monitor.worked(visitedNodes.size());

              int count = 0;
              for (final Map.Entry<String, IPreferenceNode> entry : remainingNodes.entrySet())
              {
                if (monitor.isCanceled())
                {
                  cancel = true;
                  break;
                }

                // Close the dialog and reopen it periodically to avoid running out of SWT handles.
                if (++count > 100)
                {
                  monitor.setCanceled(true);
                  abort.set(true);
                  continue;
                }

                visitedNodes.add(entry.getValue());
                UIUtil.syncExec(new Runnable()
                {
                  public void run()
                  {
                    monitor.subTask(entry.getKey());
                    IPreferenceNode preferenceNode = entry.getValue();
                    String id = preferenceNode.getId();
                    try
                    {
                      viewer.setSelection(new StructuredSelection(preferenceNode));
                    }
                    catch (Throwable throwable)
                    {
                      // Log any problem creating the page.
                      SetupUIPlugin.INSTANCE.log(throwable, IStatus.WARNING);
                    }

                    // Check if the current page is valid.
                    IPreferencePage currentPage = (IPreferencePage)ReflectUtil.invokeMethod("getCurrentPage", preferenceDialog);
                    if (currentPage != null && !currentPage.okToLeave())
                    {
                      // Log the fact that this page is ill behaved.
                      Bundle bundle = FrameworkUtil.getBundle(currentPage.getClass());
                      SetupUIPlugin.INSTANCE.log(new Status(IStatus.WARNING, bundle == null ? SetupUIPlugin.PLUGIN_ID : bundle.getSymbolicName(),
                          "The preference page " + entry.getKey() + " comes up in an invalid state"));

                      // Remember this bad page and reopen the dialog without this bad page as the current page.
                      badPages.add(preferenceNode);
                      monitor.setCanceled(true);
                      abort.set(true);
                    }

                    initializedPreferencePages.add(id);
                  }
                });

                monitor.worked(1);
              }

              monitor.done();
            }
            finally
            {
              // Record the preferences we've already initialized.
              RecorderManager.INSTANCE.setInitializedPreferencePages(initializedPreferencePages);

              UIUtil.asyncExec(new Runnable()
              {
                public void run()
                {
                  // Don't record any preferences that are changing just because we've visited a page.
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

                  // Set the page for the bad nodes to null so we can exit the dialog with an OK.
                  Set<IPreferencePage> pages = new HashSet<IPreferencePage>();
                  for (IPreferenceNode node : badPages)
                  {
                    IPreferencePage page = node.getPage();
                    pages.add(page);
                    ReflectUtil.setValue("page", node, null);
                  }

                  ReflectUtil.invokeMethod("okPressed", preferenceDialog);

                  // Close the current transaction.
                  RecorderTransaction transaction = RecorderTransaction.getInstance();
                  if (transaction != null)
                  {
                    transaction.close();
                  }

                  // Reopen the dialog.
                  UIUtil.asyncExec(new Runnable()
                  {
                    public void run()
                    {
                      PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(null, originalID, null, null);

                      // If we aborted, continue processing once the dialog comes up.
                      if (abort.get())
                      {
                        // Reset the dialog to the newly created one.
                        setPreferenceDialog(dialog);
                        UIUtil.asyncExec(Initializer.this);
                      }

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
}
