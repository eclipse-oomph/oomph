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
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.setup.ui.wizards.SetupWizardDialog;
import org.eclipse.oomph.ui.UICallback;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * @author Eike Stepper
 */
public final class InstallerDialog extends SetupWizardDialog
{
  public static final int RETURN_RESTART = -4;

  private final boolean restarted;

  private ToolItem updateToolItem;

  private InstallerUpdateSearchState updateSearchState;

  private IStatus updateSearchError;

  private Link versionLink;

  public InstallerDialog(Shell parentShell, boolean restarted)
  {
    super(parentShell, new SetupWizard.Installer());
    this.restarted = restarted;
  }

  @Override
  protected Control createHelpControl(Composite parent)
  {
    Control helpControl = super.createHelpControl(parent);
    setProductVersionLink(parent);
    return helpControl;
  }

  @Override
  protected void createToolItemsForToolBar(ToolBar toolBar)
  {
    createToolItem(toolBar, "install_prefs_proxy", "Network proxy settings").addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Dialog dialog = new ProxyPreferenceDialog(getShell());
        dialog.open();
      }
    });

    createToolItem(toolBar, "install_prefs_ssh2", "SSH2 settings").addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Dialog dialog = new SSH2PreferenceDialog(getShell());
        dialog.open();
      }
    });

    updateToolItem = createToolItem(toolBar, "install_update0", "Update");
    updateToolItem.setDisabledImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_searching0"));
    updateToolItem.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        if (updateSearchError == null)
        {
          update(false);
        }
        else
        {
        }
      }
    });
  }

  protected final ToolItem createToolItem(ToolBar toolBar, String iconPath, String toolTip)
  {
    ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
    if (iconPath == null)
    {
      toolItem.setText(toolTip);
    }
    else
    {
      Image image = SetupInstallerPlugin.INSTANCE.getSWTImage(iconPath);
      toolItem.setImage(image);
      toolItem.setToolTipText(toolTip);
    }

    return toolItem;
  }

  protected boolean update(final boolean needsEarlyConfirmation)
  {
    Runnable postInstall = new Runnable()
    {
      public void run()
      {
        setUpdateIcon(0);
      }
    };

    Runnable restartHandler = new Runnable()
    {
      public void run()
      {
        close();
        setReturnCode(RETURN_RESTART);
      }
    };

    UICallback callback = new UICallback(getShell(), AbstractSetupDialog.SHELL_TEXT);
    return UpdateUtil.update(callback, needsEarlyConfirmation, true, postInstall, restartHandler);
  }

  private void initUpdateSearch()
  {
    updateSearchState = InstallerUpdateSearchState.SEARCHING;

    new Thread("Update Icon Setter")
    {
      @Override
      public void run()
      {
        try
        {
          for (int i = 0; isRunning(); i = ++i % 20)
          {
            if (updateToolItem == null || updateToolItem.isDisposed())
            {
              return;
            }

            int icon = i > 7 ? 0 : i;
            setUpdateIcon(icon);
            sleep(80);
          }

          if (updateSearchError == null)
          {
            setUpdateIcon(0);
          }
        }
        catch (Exception ex)
        {
          SetupInstallerPlugin.INSTANCE.log(ex);
        }
      }

      private boolean isRunning()
      {
        return updateSearchState != InstallerUpdateSearchState.DONE && updateSearchState != InstallerUpdateSearchState.ERROR;
      }
    }.start();

    new Thread("Update Searcher")
    {
      @Override
      public void run()
      {
        try
        {
          IProvisioningAgent agent = SetupInstallerPlugin.INSTANCE.getService(IProvisioningAgent.class);

          try
          {
            IStatus status = UpdateUtil.checkForUpdates(agent, true, null, SubMonitor.convert(null));
            if (status == UpdateUtil.UPDATE_FOUND_STATUS)
            {
              updateSearchState = InstallerUpdateSearchState.FOUND;
            }
            else
            {
              if (status.isOK())
              {
                updateSearchState = InstallerUpdateSearchState.DONE;
              }
              else
              {
                updateSearchState = InstallerUpdateSearchState.ERROR;
                updateSearchError = status;
                SetupInstallerPlugin.INSTANCE.log(status);
              }
            }
          }
          finally
          {
            SetupInstallerPlugin.INSTANCE.ungetService(agent);
          }
        }
        catch (Exception ex)
        {
          // Likely due to early exit. Ignore
        }
      }
    }.start();
  }

  private void setUpdateIcon(final int icon)
  {
    updateToolItem.getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        if (updateToolItem == null || updateToolItem.isDisposed())
        {
          return;
        }

        try
        {
          switch (updateSearchState)
          {
            case SEARCHING:
              updateToolItem.setToolTipText("Checking for updates...");
              updateToolItem.setDisabledImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_searching" + icon + ""));
              updateToolItem.setEnabled(false);
              break;

            case FOUND:
              updateToolItem.setToolTipText("Install available updates");
              updateToolItem.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_update" + icon + ""));
              updateToolItem.setEnabled(true);
              break;

            case DONE:
              updateToolItem.setToolTipText("No updates available");
              updateToolItem.setDisabledImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_update_disabled"));
              updateToolItem.setEnabled(false);
              break;

            case ERROR:
              updateToolItem.setToolTipText("Review update problems");
              updateToolItem.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_error"));
              updateToolItem.setEnabled(true);
              break;
          }
        }
        catch (Exception ex)
        {
          // Ignore
        }
      }
    });
  }

  private void setProductVersionLink(Composite parent)
  {
    GridLayout parentLayout = (GridLayout)parent.getLayout();
    parentLayout.numColumns++;
    parentLayout.horizontalSpacing = 10;

    versionLink = new Link(parent, SWT.NO_FOCUS);
    versionLink.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER | GridData.VERTICAL_ALIGN_CENTER));
    versionLink.setToolTipText("About");

    Thread thread = new ProductVersionSetter();
    thread.start();
  }

  /**
   * @author Eike Stepper
   */
  private final class ProductVersionSetter extends Thread
  {
    private boolean selfHosting;

    public ProductVersionSetter()
    {
      super("Product Version Setter");
    }

    @Override
    public void run()
    {
      try
      {
        final String version = getProductVersion();
        if (version != null)
        {
          if (selfHosting)
          {
            updateSearchState = InstallerUpdateSearchState.DONE;
            setUpdateIcon(0);
          }
          else if (!restarted)
          {
            initUpdateSearch();
          }

          versionLink.getDisplay().asyncExec(new Runnable()
          {
            public void run()
            {
              try
              {
                versionLink.addSelectionListener(new SelectionAdapter()
                {
                  @Override
                  public void widgetSelected(SelectionEvent e)
                  {
                    new AboutDialog(getShell(), version).open();
                  }
                });

                versionLink.setText("<a>" + version + "</a>"); //$NON-NLS-1$
                versionLink.getParent().layout();
              }
              catch (Exception ex)
              {
                SetupInstallerPlugin.INSTANCE.log(ex);
              }
            }
          });
        }
      }
      catch (Exception ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }

    private String getProductVersion()
    {
      Agent agent = P2Util.getAgentManager().getCurrentAgent();

      IProfile profile = agent.getProfileRegistry().getProfile(IProfileRegistry.SELF);
      if (profile == null || "SelfHostingProfile".equals(profile.getProfileId()))
      {
        selfHosting = true;
        return "Self Hosting";
      }

      for (IInstallableUnit iu : profile.query(QueryUtil.createIUQuery(SetupUIPlugin.INSTALLER_PRODUCT_ID), null))
      {
        return iu.getVersion().toString();
      }

      return null;
    }
  }
}
