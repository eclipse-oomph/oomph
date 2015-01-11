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
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManagerElement;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.internal.core.AgentManagerElementImpl;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Tree;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class AgentManagerComposite extends Composite
{
  private TreeViewer treeViewer;

  private Object selectedElement;

  private Button newAgentButton;

  private Button newPoolButton;

  private Button deleteButton;

  private Button analyzeButton;

  private Button showProfilesButton;

  public AgentManagerComposite(Composite parent, int style, final Object selection)
  {
    super(parent, style);
    setLayout(UIUtil.createGridLayout(2));

    final P2ContentProvider contentProvider = new P2ContentProvider();

    treeViewer = new TreeViewer(this, SWT.BORDER);
    treeViewer.setContentProvider(contentProvider);
    treeViewer.setLabelProvider(new P2LabelProvider());
    treeViewer.setSorter(new P2ViewerSorter());
    treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        selectedElement = ((IStructuredSelection)treeViewer.getSelection()).getFirstElement();
        elementChanged(selectedElement);
      }
    });

    Tree tree = treeViewer.getTree();
    tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    GridLayout buttonLayout = new GridLayout(1, false);
    buttonLayout.marginWidth = 0;
    buttonLayout.marginHeight = 0;

    Composite buttonComposite = new Composite(this, SWT.NONE);
    buttonComposite.setLayout(buttonLayout);
    buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
    buttonComposite.setBounds(0, 0, 64, 64);

    newAgentButton = new Button(buttonComposite, SWT.NONE);
    newAgentButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    newAgentButton.setText("New Agent...");
    newAgentButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        String path = openDirectoryDialog("Select the location of the new agent.", PropertiesUtil.USER_HOME);
        if (path != null)
        {
          Agent agent = P2Util.getAgentManager().addAgent(new File(path));
          BundlePool bundlePool = agent.addBundlePool(new File(path, BundlePool.DEFAULT_NAME));
          refreshFor(bundlePool);
        }
      }
    });

    newPoolButton = new Button(buttonComposite, SWT.NONE);
    newPoolButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    newPoolButton.setText("New Bundle Pool...");
    newPoolButton.setEnabled(false);
    newPoolButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Agent selectedAgent = (Agent)selectedElement;
        String path = openDirectoryDialog("Select the location of the new pool.", selectedAgent.getLocation().getAbsolutePath());
        if (path != null)
        {
          BundlePool bundlePool = selectedAgent.addBundlePool(new File(path));
          refreshFor(bundlePool);
        }
      }
    });

    deleteButton = new Button(buttonComposite, SWT.NONE);
    deleteButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    deleteButton.setText("Delete...");
    deleteButton.setEnabled(false);
    deleteButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        AgentManagerElementImpl agentManagerElement = (AgentManagerElementImpl)selectedElement;
        String type = agentManagerElement.getElementType();

        if (MessageDialog.openQuestion(getShell(), AgentManagerDialog.TITLE, "Are you sure to delete " + type + " " + agentManagerElement
            + "?\n\nThe physical " + type + " files will remain on disk even if you answer Yes."))
        {
          try
          {
            agentManagerElement.delete();
            treeViewer.refresh();
          }
          catch (Exception ex)
          {
            P2UIPlugin.INSTANCE.log(ex);
          }
        }
      }
    });

    analyzeButton = new Button(buttonComposite, SWT.NONE);
    analyzeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    analyzeButton.setText("Analyze...");
    analyzeButton.setEnabled(false);
    analyzeButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        AgentAnalyzerDialog dialog = new AgentAnalyzerDialog(getShell(), (Agent)selectedElement);
        dialog.open();

        treeViewer.refresh();
      }
    });

    showProfilesButton = new Button(buttonComposite, SWT.CHECK);
    showProfilesButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    showProfilesButton.setText("Show Profiles");
    showProfilesButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        boolean showProfiles = showProfilesButton.getSelection();
        contentProvider.setShowProfiles(showProfiles);
        treeViewer.refresh();
      }
    });

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        BusyIndicator.showWhile(getShell().getDisplay(), new Runnable()
        {
          public void run()
          {
            treeViewer.setInput(P2Util.getAgentManager());
            treeViewer.expandAll();
          }
        });

        if (selection != null)
        {
          treeViewer.setSelection(new StructuredSelection(selection));
        }
      }
    });
  }

  public Object getSelectedElement()
  {
    return selectedElement;
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    treeViewer.getTree().setEnabled(enabled);
    newAgentButton.setEnabled(enabled);
    newPoolButton.setEnabled(enabled);
    deleteButton.setEnabled(enabled);
    analyzeButton.setEnabled(enabled);
    super.setEnabled(enabled);
  }

  @Override
  protected void checkSubclass()
  {
    // Disable the check that prevents subclassing of SWT components
  }

  protected void elementChanged(Object element)
  {
    newPoolButton.setEnabled(element instanceof Agent);
    deleteButton.setEnabled(element instanceof AgentManagerElement && !((AgentManagerElement)element).isUsed());
    analyzeButton.setEnabled(element instanceof Agent);
  }

  private String openDirectoryDialog(String message, String path)
  {
    DirectoryDialog dialog = new DirectoryDialog(getShell());
    dialog.setText(AgentManagerDialog.TITLE);
    dialog.setMessage(message);
    dialog.setFilterPath(path);
    return dialog.open();
  }

  private void refreshFor(BundlePool bundlePool)
  {
    treeViewer.refresh();
    treeViewer.setExpandedState(bundlePool.getAgent(), true);
    treeViewer.setSelection(new StructuredSelection(bundlePool));
    treeViewer.getTree().setFocus();
  }
}
