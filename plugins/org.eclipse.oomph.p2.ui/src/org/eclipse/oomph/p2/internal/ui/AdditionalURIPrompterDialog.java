/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.internal.core.AgentAnalyzer.AnalyzedArtifact;
import org.eclipse.oomph.ui.OomphDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Eike Stepper
 */
public final class AdditionalURIPrompterDialog extends OomphDialog implements ICheckStateListener
{
  private final Set<URI> checkedRepositories = new HashSet<URI>();

  private boolean firstTime;

  private List<AnalyzedArtifact> artifacts;

  private Set<URI> repositories;

  private CheckboxTableViewer repositoryViewer;

  public AdditionalURIPrompterDialog(Shell parentShell, boolean firstTime, List<AnalyzedArtifact> artifacts, Set<URI> repositories)
  {
    super(parentShell, AgentAnalyzerDialog.TITLE, 600, 500, P2UIPlugin.INSTANCE, false);
    setShellStyle(SWT.TITLE | SWT.MAX | SWT.RESIZE | SWT.BORDER | SWT.APPLICATION_MODAL);
    this.firstTime = firstTime;
    this.artifacts = artifacts;
    this.repositories = repositories;
  }

  public Set<URI> getCheckedRepositories()
  {
    return checkedRepositories;
  }

  @Override
  protected String getShellText()
  {
    return AgentAnalyzerDialog.TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Some artifacts could not be downloaded from the " + (firstTime ? "repositories listed in the profiles" : "previously checked repositories") + ".\n"
        + "Please check additional repositories and try again.";
  }

  @Override
  protected String getImagePath()
  {
    return "wizban/AgentAnalyzer.png";
  }

  @Override
  protected void createUI(Composite parent)
  {
    Composite mainComposite = new Composite(parent, SWT.NONE);
    mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
    GridLayout mainLayout = new GridLayout();
    mainLayout.marginWidth = 10;
    mainLayout.marginHeight = 10;
    mainComposite.setLayout(mainLayout);

    SashForm sashForm = new SashForm(mainComposite, SWT.SMOOTH | SWT.VERTICAL);
    sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

    Composite artifactComposite = new Composite(sashForm, SWT.NONE);
    GridLayout artifactLayout = new GridLayout();
    artifactLayout.marginWidth = 0;
    artifactLayout.marginHeight = 0;
    artifactComposite.setLayout(artifactLayout);

    Label artifactLabel = new Label(artifactComposite, SWT.NONE);
    artifactLabel.setText("Remaining Damaged Artifacts:");

    TableViewer artifactViewer = new TableViewer(artifactComposite, SWT.BORDER | SWT.FULL_SELECTION);
    artifactViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
    artifactViewer.setComparator(new ViewerComparator());
    artifactViewer.setLabelProvider(new AgentAnalyzerComposite.TableLabelProvider(parent.getDisplay(), true));
    artifactViewer.setContentProvider(new ArrayContentProvider());
    artifactViewer.setInput(artifacts);

    Composite repositoryComposite = new Composite(sashForm, SWT.NONE);
    GridLayout repositoryLayout = new GridLayout();
    repositoryLayout.marginTop = 10;
    repositoryLayout.marginWidth = 0;
    repositoryLayout.marginHeight = 0;
    repositoryComposite.setLayout(repositoryLayout);

    Composite repositoryHeader = new Composite(repositoryComposite, SWT.NONE);
    repositoryHeader.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    GridLayout repositoryHeaderLayout = new GridLayout(2, false);
    repositoryHeaderLayout.marginWidth = 0;
    repositoryHeaderLayout.marginHeight = 0;
    repositoryHeader.setLayout(repositoryHeaderLayout);

    Label repositoryLabel = new Label(repositoryHeader, SWT.NONE);
    repositoryLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    repositoryLabel.setText("Additional Repositories:");

    ToolBar toolBar = new ToolBar(repositoryHeader, SWT.FLAT | SWT.RIGHT);
    toolBar.setBounds(0, 0, 89, 23);

    ToolItem checkAll = new ToolItem(toolBar, SWT.NONE);
    checkAll.setToolTipText("Check all repositories");
    checkAll.setImage(getDefaultImage("tool16/checkAll"));

    ToolItem uncheckAll = new ToolItem(toolBar, SWT.NONE);
    uncheckAll.setToolTipText("Uncheck all repositories");
    uncheckAll.setImage(getDefaultImage("tool16/uncheckAll"));

    repositoryViewer = CheckboxTableViewer.newCheckList(repositoryComposite, SWT.BORDER | SWT.FULL_SELECTION);
    repositoryViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
    repositoryViewer.setComparator(new ViewerComparator());
    repositoryViewer.setLabelProvider(new AgentAnalyzerComposite.TableLabelProvider(parent.getDisplay()));
    repositoryViewer.setContentProvider(new ArrayContentProvider());
    repositoryViewer.setInput(repositories);
    repositoryViewer.addCheckStateListener(this);

    checkAll.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        repositoryViewer.setAllChecked(true);
        checkStateChanged(null);
      }
    });

    uncheckAll.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        repositoryViewer.setAllChecked(false);
        checkStateChanged(null);
      }
    });

    Composite uriComposite = new Composite(repositoryComposite, SWT.NONE);
    GridLayout uriLayout = new GridLayout(3, false);
    uriLayout.marginWidth = 0;
    uriLayout.marginHeight = 0;
    uriComposite.setLayout(uriLayout);
    uriComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    Label uriLabel = new Label(uriComposite, SWT.NONE);
    uriLabel.setText("URI:");

    final AtomicReference<URI> enteredURI = new AtomicReference<URI>();
    final Text uriText = new Text(uriComposite, SWT.BORDER);
    uriText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    final Button uriButton = new Button(uriComposite, SWT.NONE);
    uriButton.setText("Add");
    uriButton.setToolTipText("Add the entered URI to the repositories list");
    uriButton.setEnabled(false);
    uriButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        URI uri = enteredURI.get();
        repositories.add(uri);

        repositoryViewer.refresh();
        repositoryViewer.setChecked(uri, true);
        checkStateChanged(null);

        uriText.setText("");
        uriText.setFocus();
      }
    });

    uriText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        URI uri;

        try
        {
          String text = uriText.getText();
          uri = text.length() == 0 ? null : new URI(text);
        }
        catch (Exception ex)
        {
          uri = null;
        }

        enteredURI.set(uri);
        uriButton.setEnabled(uri != null);
      }
    });

    sashForm.setWeights(new int[] { 1, 2 });
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    super.createButtonsForButtonBar(parent);
    checkStateChanged(null);

  }

  public void checkStateChanged(CheckStateChangedEvent event)
  {
    checkedRepositories.clear();
    for (Object object : repositoryViewer.getCheckedElements())
    {
      checkedRepositories.add((URI)object);
    }

    getButton(IDialogConstants.OK_ID).setEnabled(!checkedRepositories.isEmpty());
  }
}
