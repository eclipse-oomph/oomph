/*
 * Copyright (c) 2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.ui.wizards;

import org.eclipse.oomph.targlets.internal.ui.TargletsUIPlugin;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.pde.TargetPlatformUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import java.io.File;

/**
 * @author Eike Stepper
 */
public class TargetDefinitionExportWizardPage extends WizardPage
{
  private static final String DEFAULT_MESSAGE = "Select a target definition and enter a folder to which to export.";

  private TableViewer targetDefinitionViewer;

  private ITargetDefinition targetDefinition;

  private ITargetHandle activeTargetHandle;

  private Text exportFolderText;

  private File exportFolder;

  public TargetDefinitionExportWizardPage()
  {
    super("page");
  }

  public ITargetDefinition getTargetDefinition()
  {
    return targetDefinition;
  }

  public File getExportFolder()
  {
    return exportFolder;
  }

  public void createControl(Composite parent)
  {
    setTitle(TargetDefinitionExportWizard.TITLE);
    setMessage(DEFAULT_MESSAGE);

    Composite container = new Composite(parent, SWT.NULL);
    setControl(container);
    container.setLayout(new GridLayout(1, false));

    Label targetPlatformLabel = new Label(container, SWT.NONE);
    targetPlatformLabel.setText("Target platform:");

    targetDefinitionViewer = new TableViewer(container, SWT.BORDER | SWT.V_SCROLL);
    targetDefinitionViewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    targetDefinitionViewer.setContentProvider(new ArrayContentProvider());
    targetDefinitionViewer.setLabelProvider(new TargetLabelProvider());
    targetDefinitionViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        validatePage();
      }
    });

    Label exportFolderLabel = new Label(container, SWT.NONE);
    exportFolderLabel.setText("Export folder:");

    GridLayout compositeGridLayout = new GridLayout(2, false);
    compositeGridLayout.marginHeight = 0;
    compositeGridLayout.marginWidth = 0;

    Composite composite = new Composite(container, SWT.NONE);
    composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    composite.setLayout(compositeGridLayout);

    exportFolderText = new Text(composite, SWT.BORDER);
    exportFolderText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
    exportFolderText.addModifyListener(new ModifyListener()
    {
      public void modifyText(ModifyEvent e)
      {
        validatePage();
      }
    });

    Button browseButton = new Button(composite, SWT.NONE);
    browseButton.setText("Browse...");
    browseButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setText("Export Folder");
        dialog.setMessage("Select a folder to which to export:");

        String value = exportFolderText.getText();
        if (value.length() != 0)
        {
          try
          {
            File existingFolder = IOUtil.getExistingFolder(new File(value));
            if (existingFolder != null)
            {
              dialog.setFilterPath(existingFolder.getAbsolutePath());
            }
          }
          catch (Exception ex)
          {
            TargletsUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
          }
        }

        String dir = dialog.open();
        if (dir != null)
        {
          exportFolderText.setText(dir);
          validatePage();
        }
      }
    });

    setPageComplete(false);

    new Job("Determine Target Definitions")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        try
        {
          final ITargetDefinition[] targetDefinitions = TargetPlatformUtil.getTargetDefinitions(monitor);
          if (targetDefinitions.length != 0)
          {
            ITargetDefinition activeTargetDefinition = TargetPlatformUtil.getActiveTargetDefinition();
            if (activeTargetDefinition != null)
            {
              activeTargetHandle = activeTargetDefinition.getHandle();

              boolean found = false;
              for (ITargetDefinition targetDefinition : targetDefinitions)
              {
                if (targetDefinition.getHandle().equals(activeTargetHandle))
                {
                  activeTargetDefinition = targetDefinition;
                  found = true;
                  break;
                }
              }

              if (!found)
              {
                activeTargetDefinition = null;
              }
            }

            UIUtil.syncExec(getShell(), new Runnable()
            {
              public void run()
              {
                targetDefinitionViewer.setInput(targetDefinitions);
              }
            });

            final ITargetDefinition defaultTargetDefinition = activeTargetDefinition != null ? activeTargetDefinition : targetDefinitions[0];
            if (defaultTargetDefinition != null)
            {
              UIUtil.syncExec(getShell(), new Runnable()
              {
                public void run()
                {
                  targetDefinitionViewer.setSelection(new StructuredSelection(defaultTargetDefinition));
                }
              });
            }
          }
        }
        catch (final Exception ex)
        {
          UIUtil.syncExec(getShell(), new Runnable()
          {
            public void run()
            {
              setErrorMessage(ex.getMessage());
            }
          });

          return TargletsUIPlugin.INSTANCE.getStatus(ex);
        }

        UIUtil.syncExec(getShell(), new Runnable()
        {
          public void run()
          {
            validatePage();
          }
        });

        return Status.OK_STATUS;
      }
    }.schedule();
  }

  protected void validatePage()
  {
    IStructuredSelection selection = (IStructuredSelection)targetDefinitionViewer.getSelection();
    targetDefinition = (ITargetDefinition)selection.getFirstElement();
    if (targetDefinition == null)
    {
      setMessage("Select a target definition to export.");
      setPageComplete(false);
      return;
    }

    String path = exportFolderText.getText();
    if (path.length() == 0)
    {
      setMessage("Enter a folder to which to export.");
      setPageComplete(false);
      return;
    }

    try
    {
      exportFolder = new File(path);
    }
    catch (Exception ex)
    {
      setErrorMessage(ex.getMessage());
      setPageComplete(false);
      return;
    }

    setMessage(DEFAULT_MESSAGE);
    setPageComplete(true);
  }

  /**
   * @author Eike Stepper
   */
  private final class TargetLabelProvider extends LabelProvider implements IFontProvider
  {
    private final Font baseFont = targetDefinitionViewer.getControl().getFont();

    @Override
    public Image getImage(Object element)
    {
      if (element instanceof ITargetDefinition)
      {
        return TargletsUIPlugin.INSTANCE.getSWTImage("target_obj");
      }

      return null;
    }

    @Override
    public String getText(Object element)
    {
      if (element instanceof ITargetDefinition)
      {
        ITargetDefinition targetDefinition = (ITargetDefinition)element;
        String text = targetDefinition.getName();
        if (targetDefinition.getHandle().equals(activeTargetHandle))
        {
          text += " (Active)";
        }

        return text;
      }

      return "";
    }

    public Font getFont(Object element)
    {
      if (element instanceof ITargetDefinition)
      {
        ITargetDefinition targetDefinition = (ITargetDefinition)element;
        if (targetDefinition.getHandle().equals(activeTargetHandle))
        {
          return TargletsUIPlugin.getBoldFont(baseFont);
        }
      }

      return null;
    }
  }
}
