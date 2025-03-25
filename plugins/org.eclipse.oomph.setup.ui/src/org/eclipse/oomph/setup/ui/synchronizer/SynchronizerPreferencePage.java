/*
 * Copyright (c) 2015-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.oomph.internal.ui.AbstractPreferencePage;
import org.eclipse.oomph.setup.ui.SetupPropertyTester;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.synchronizer.SynchronizerManager.Impact;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Eike Stepper
 */
public class SynchronizerPreferencePage extends AbstractPreferencePage
{
  public static final String ID = "org.eclipse.oomph.setup.SynchronizerPreferencePage"; //$NON-NLS-1$

  private final PageHandler pageHandler;

  public SynchronizerPreferencePage()
  {
    pageHandler = SynchronizerManager.Availability.AVAILABLE ? new StorageHandler() : new DisabledHandler();
  }

  @Override
  protected Control doCreateContents(Composite parent)
  {
    return pageHandler.createContents(parent);
  }

  @Override
  protected void performDefaults()
  {
    pageHandler.performDefaults();
    super.performDefaults();
  }

  @Override
  protected void performApply()
  {
    pageHandler.performApply();
    super.performApply();
  }

  @Override
  public boolean performOk()
  {
    pageHandler.performOk();
    return super.performOk();
  }

  @Override
  public void createControl(Composite parent)
  {
    super.createControl(parent);
    pageHandler.updateButtons();
  }

  /**
   * @author Ed Merks
   */
  private abstract class PageHandler
  {
    public abstract Control createContents(Composite parent);

    public void updateButtons()
    {
    }

    public void performOk()
    {
    }

    public void performApply()
    {
    }

    public void performDefaults()
    {
    }
  }

  /**
   * @author Ed Merks
   */
  private class DisabledHandler extends PageHandler
  {
    @Override
    public Control createContents(Composite parent)
    {
      Text text = new Text(parent, SWT.READ_ONLY);
      text.setText(Messages.SynchronizerPreferencePage_disabledHandler_text);
      return text;
    }

    @Override
    public void updateButtons()
    {
      getApplyButton().setEnabled(false);
      getDefaultsButton().setEnabled(false);
    }
  }

  /**
   * @author Ed Merks
   */
  private class StorageHandler extends PageHandler
  {
    private Button enableButton;

    private Text location;

    private Button browseButton;

    private Button syncButton;

    private Button viewButton;

    private Button deleteButton;

    private boolean initialEnabled;

    private Path initialLocation;

    public StorageHandler()
    {
      initialEnabled = SynchronizerManager.INSTANCE.isSyncEnabled();
      initialLocation = SynchronizerManager.INSTANCE.getSyncLocation();
    }

    @Override
    public Control createContents(Composite parent)
    {
      GridLayout layout = new GridLayout(3, false);
      layout.marginWidth = 0;
      layout.marginHeight = 0;

      final Composite main = new Composite(parent, SWT.NONE);
      main.setLayout(layout);

      enableButton = new Button(main, SWT.CHECK);

      enableButton.setText(Messages.SynchronizerPreferencePage_synchronizeTo_text);
      enableButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          updateEnablement();
        }
      });

      location = new Text(main, SWT.BORDER | SWT.SINGLE);
      location.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));

      browseButton = new Button(main, SWT.PUSH);
      browseButton.setText(Messages.SynchronizerPreferencePage_Browse_label);
      browseButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

      syncButton = new Button(main, SWT.PUSH);
      syncButton.setText(Messages.SynchronizerPreferencePage_syncButton_text);
      syncButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, layout.numColumns, 1));
      syncButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          performApply();

          final Shell shell = getShell();

          UIUtil.asyncExec(shell, new Runnable()
          {
            @Override
            public void run()
            {
              try
              {
                Object data = shell.getData();
                if (data instanceof PreferenceDialog)
                {
                  PreferenceDialog preferenceDialog = (PreferenceDialog)data;
                  ReflectUtil.invokeMethod("okPressed", preferenceDialog); //$NON-NLS-1$
                }
              }
              catch (Throwable ex)
              {
                SetupUIPlugin.INSTANCE.log(ex);
              }
            }
          });

          UIUtil.asyncExec(shell.getDisplay(), new Runnable()
          {
            @Override
            public void run()
            {
              try
              {
                Shell shell = SetupPropertyTester.getHandlingShell();
                if (shell != null)
                {
                  shell.setVisible(true);
                }
                else
                {
                  Impact impact = SynchronizerManager.INSTANCE.performFullSynchronization();
                  if (impact != null && impact.hasLocalImpact())
                  {
                    SetupWizard.Updater.perform(false);
                  }
                }
              }
              catch (Throwable ex)
              {
                SetupUIPlugin.INSTANCE.log(ex);
              }
            }
          });
        }
      });

      viewButton = new Button(main, SWT.PUSH);
      viewButton.setText(Messages.SynchronizerPreferencePage_viewButton_text);
      viewButton.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, layout.numColumns, 1));
      viewButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
          try
          {
            dialog.run(true, false, new IRunnableWithProgress()
            {
              @Override
              public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
              {
                try
                {
                  File file = File.createTempFile("preference-synchornization-remote", ".xml"); //$NON-NLS-1$ //$NON-NLS-2$
                  if (SynchronizerManager.INSTANCE.getRemoteDataProvider().retrieve(file))
                  {
                  }

                  final String data = IOUtil.readUTF8(file);

                  UIUtil.asyncExec(new Runnable()
                  {
                    @Override
                    public void run()
                    {
                      final Point size = getShell().getSize();
                      Dialog dialog = new Dialog(getShell())
                      {
                        @Override
                        protected void configureShell(Shell newShell)
                        {
                          super.configureShell(newShell);
                          newShell.setText(Messages.SynchronizerPreferencePage_shellText);
                        }

                        @Override
                        protected boolean isResizable()
                        {
                          return true;
                        }

                        @Override
                        protected void createButtonsForButtonBar(Composite parent)
                        {
                          createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CLOSE_LABEL, true);
                        }

                        @Override
                        protected Control createDialogArea(Composite parent)
                        {
                          Composite composite = (Composite)super.createDialogArea(parent);
                          StyledText text = new StyledText(composite, SWT.WRAP | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY | SWT.BORDER);
                          text.setForeground(composite.getForeground());
                          text.setBackground(composite.getBackground());
                          GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
                          gridData.widthHint = size.x * 3 / 4;
                          gridData.heightHint = size.y * 3 / 4;
                          text.setLayoutData(gridData);
                          text.setText(data);

                          try
                          {
                            Pattern xmlPattern = Pattern.compile("<\\??/?[^>]+\\??/?>", Pattern.DOTALL); //$NON-NLS-1$
                            Pattern attributePattern = Pattern.compile("\\s+([^=\">]+)=\"([^\"]*)\"", Pattern.DOTALL); //$NON-NLS-1$
                            int index = 0;
                            List<StyleRange> styleRanges = new ArrayList<>();
                            for (Matcher xmlMatcher = xmlPattern.matcher(data), attributeMatcher = attributePattern.matcher(data); index != -1
                                && xmlMatcher.find(index);)
                            {
                              int xmlEnd = xmlMatcher.end();
                              if (data.charAt(xmlMatcher.start() + 1) != '?')
                              {
                                while (attributeMatcher.find(index) && attributeMatcher.end() < xmlEnd)
                                {
                                  String attributeName = attributeMatcher.group(1);
                                  boolean metaAttribute = attributeName.contains(":"); //$NON-NLS-1$
                                  if (!metaAttribute || attributeName.equals("xsi:type")) //$NON-NLS-1$
                                  {
                                    StyleRange styleRange = new StyleRange();
                                    styleRange.start = attributeMatcher.start(2);
                                    styleRange.length = attributeMatcher.end(2) - styleRange.start;
                                    styleRange.fontStyle = metaAttribute ? SWT.ITALIC | SWT.BOLD : SWT.BOLD;
                                    styleRanges.add(styleRange);
                                  }

                                  index = attributeMatcher.end();
                                }
                              }

                              index = data.indexOf('<', xmlEnd);
                            }

                            text.setStyleRanges(styleRanges.toArray(new StyleRange[styleRanges.size()]));
                          }
                          catch (RuntimeException ex)
                          {
                            // Ignore styling problems.
                          }

                          return composite;
                        }
                      };

                      dialog.setBlockOnOpen(false);
                      dialog.open();
                    }
                  });
                }
                catch (Exception ex)
                {
                  SetupUIPlugin.INSTANCE.log(ex);
                }
              }
            });
          }
          catch (Exception ex)
          {
            SetupUIPlugin.INSTANCE.log(ex);
          }
        }
      });

      enableButton.setSelection(initialEnabled);
      location.setText(initialLocation.toString());

      UIUtil.asyncExec(new Runnable()
      {
        @Override
        public void run()
        {
          updateEnablement();
        }
      });

      return main;
    }

    @Override
    public void performDefaults()
    {
      if (enableButton != null)
      {
        enableButton.setSelection(initialEnabled);
        location.setText(initialLocation.toString());
        updateEnablement();
      }
    }

    @Override
    public void performApply()
    {
      if (enableButton != null)
      {
        updateEnablement();
      }
    }

    @Override
    public void performOk()
    {
      if (enableButton != null)
      {
        SynchronizerManager.INSTANCE.setSyncEnabled(initialEnabled = enableButton.getSelection());
        SynchronizerManager.INSTANCE.setSyncLocation(initialLocation = Path.of(location.getText()));
      }
    }

    private boolean needsApply()
    {
      try
      {
        if (enableButton != null && enableButton.getSelection() != initialEnabled)
        {
          return true;
        }

        if (location != null && !location.getText().equals(initialLocation.toString()))
        {
          return true;
        }
      }
      catch (Exception ex)
      {
        SetupUIPlugin.INSTANCE.log(ex);
      }

      return false;
    }

    private void updateEnablement()
    {
      boolean enabled = enableButton != null ? enableButton.getSelection() : false;
      if (location != null)
      {
        location.setEnabled(enabled);
        browseButton.setEnabled(enabled);
      }

      if (syncButton != null)
      {
        syncButton.setEnabled(enabled);
      }

      if (viewButton != null)
      {
        viewButton.setEnabled(enabled);
      }

      if (deleteButton != null)
      {
        deleteButton.setEnabled(enabled);
      }

      boolean needsApply = needsApply();

      Button defaultsButton = getDefaultsButton();
      if (defaultsButton != null)
      {
        defaultsButton.setEnabled(needsApply);
      }

      Button applyButton = getApplyButton();
      if (applyButton != null)
      {
        applyButton.setEnabled(needsApply);
      }
    }
  }
}
