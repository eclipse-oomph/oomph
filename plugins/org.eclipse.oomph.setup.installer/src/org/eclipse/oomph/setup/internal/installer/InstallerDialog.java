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

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.core.ProfileTransaction;
import org.eclipse.oomph.p2.core.ProfileTransaction.CommitContext;
import org.eclipse.oomph.p2.core.ProfileTransaction.Resolution;
import org.eclipse.oomph.setup.User;
import org.eclipse.oomph.setup.internal.core.SetupTaskPerformer;
import org.eclipse.oomph.setup.p2.impl.P2TaskImpl;
import org.eclipse.oomph.setup.ui.AbstractSetupDialog;
import org.eclipse.oomph.setup.ui.SetupUIPlugin;
import org.eclipse.oomph.setup.ui.UnsignedContentDialog;
import org.eclipse.oomph.setup.ui.wizards.ConfirmationPage;
import org.eclipse.oomph.setup.ui.wizards.ProgressPage;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard;
import org.eclipse.oomph.setup.ui.wizards.SetupWizard.Installer;
import org.eclipse.oomph.setup.ui.wizards.SetupWizardDialog;
import org.eclipse.oomph.ui.UICallback;
import org.eclipse.oomph.util.Confirmer;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.IRunnable;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.util.EList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.engine.IProvisioningPlan;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Eike Stepper
 */
public final class InstallerDialog extends SetupWizardDialog implements IPageChangedListener
{
  private static final String PROP_INSTALLER_UPDATE_URL = "oomph.installer.update.url";

  private static final String DEFAULT_INSTALLER_UPDATE_URL = "http://download.eclipse.org/oomph/products/repository";

  public static final String INSTALLER_UPDATE_URL = PropertiesUtil.getProperty(PROP_INSTALLER_UPDATE_URL, DEFAULT_INSTALLER_UPDATE_URL).replace('\\', '/');

  public static final int RETURN_RESTART = -4;

  private final boolean restarted;

  private ToolItem updateToolItem;

  private boolean updateSearching;

  private Resolution updateResolution;

  private IStatus updateError;

  private Link versionLink;

  public InstallerDialog(Shell parentShell, boolean restarted)
  {
    super(parentShell, new SetupWizard.Installer());
    this.restarted = restarted;
    addPageChangedListener(this);
  }

  public Installer getInstaller()
  {
    return (Installer)getWizard();
  }

  public void pageChanged(PageChangedEvent event)
  {
    if (event.getSelectedPage() instanceof ConfirmationPage)
    {
      updateSearching = false;
      updateResolution = null;
      updateError = null;
      setUpdateIcon(0);

      SetupTaskPerformer performer = getInstaller().getPerformer();
      performer.getBundles().add(SetupInstallerPlugin.INSTANCE.getBundle());
    }
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
        selfUpdate();
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

  private void selfUpdate()
  {
    updateError = null;
    setUpdateIcon(0);

    if (updateResolution == null)
    {
      initUpdateSearch();
    }
    else
    {
      final UICallback callback = new UICallback(getShell(), AbstractSetupDialog.SHELL_TEXT);
      callback.runInProgressDialog(false, new IRunnable()
      {
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          try
          {
            updateResolution.commit(monitor);

            callback.execInUI(true, new Runnable()
            {
              public void run()
              {
                callback.information(false, "Updates were installed. Press OK to restart.");

                close();
                setReturnCode(RETURN_RESTART);
              }
            });
          }
          catch (CoreException ex)
          {
            updateError = ex.getStatus();
          }
          finally
          {
            updateResolution = null;
            setUpdateIcon(0);
          }
        }
      });
    }
  }

  private void initUpdateSearch()
  {
    updateSearching = true;

    Thread updateIconSetter = new UpdateIconSetter();
    updateIconSetter.start();

    Thread updateSearcher = new UpdateSearcher();
    updateSearcher.start();
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
          if (updateSearching)
          {
            updateToolItem.setToolTipText("Checking for updates...");
            updateToolItem.setDisabledImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_searching" + icon + ""));
            updateToolItem.setEnabled(false);
          }
          else if (updateError != null)
          {
            StringBuilder builder = new StringBuilder();
            formatStatus(builder, "", updateError);
            updateToolItem.setToolTipText(builder.toString());
            updateToolItem.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_error"));
            updateToolItem.setEnabled(true);
          }
          else if (updateResolution != null)
          {
            updateToolItem.setToolTipText("Install available updates");
            updateToolItem.setImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_update" + icon + ""));
            updateToolItem.setEnabled(true);
          }
          else
          {
            updateToolItem.setToolTipText("No updates available");
            updateToolItem.setDisabledImage(SetupInstallerPlugin.INSTANCE.getSWTImage("install_update_disabled"));
            updateToolItem.setEnabled(false);
          }
        }
        catch (Exception ex)
        {
          // Ignore
        }
      }

      private void formatStatus(StringBuilder builder, String indent, IStatus status)
      {
        if (builder.length() != 0)
        {
          builder.append('\n');
        }

        builder.append(indent);
        builder.append(status.getMessage());

        for (IStatus child : status.getChildren())
        {
          formatStatus(builder, indent + "   ", child);
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
            updateSearching = false;
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

      String buildID = null;
      InputStream source = null;

      try
      {
        URL url = SetupInstallerPlugin.INSTANCE.getBundle().getResource("about.mappings");
        if (url != null)
        {
          source = url.openStream();

          Properties properties = new Properties();
          properties.load(source);

          buildID = (String)properties.get("0");
          if (buildID != null && buildID.startsWith("$"))
          {
            buildID = null;
          }
        }
      }
      catch (IOException ex)
      {
        //$FALL-THROUGH$
      }
      finally
      {
        IOUtil.closeSilent(source);
      }

      for (IInstallableUnit iu : P2Util.asIterable(profile.query(QueryUtil.createIUQuery(SetupUIPlugin.INSTALLER_PRODUCT_ID), null)))
      {
        String label;

        Version version = iu.getVersion();
        if (buildID != null && version.getSegmentCount() > 3)
        {
          label = version.getSegment(0) + "." + version.getSegment(1) + "." + version.getSegment(2);
        }
        else
        {
          label = version.toString();
        }

        if (buildID != null)
        {
          label += " Build " + buildID;
        }

        return label;
      }

      return null;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class UpdateIconSetter extends Thread
  {
    public UpdateIconSetter()
    {
      super("Update Icon Setter");
    }

    @Override
    public void run()
    {
      try
      {
        for (int i = 0; updateSearching || updateResolution != null; i = ++i % 20)
        {
          if (updateToolItem == null || updateToolItem.isDisposed())
          {
            return;
          }

          int icon = i > 7 ? 0 : i;
          setUpdateIcon(icon);
          sleep(80);
        }

        setUpdateIcon(0);
      }
      catch (Exception ex)
      {
        SetupInstallerPlugin.INSTANCE.log(ex);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class UpdateSearcher extends Thread
  {
    public UpdateSearcher()
    {
      super("Update Searcher");
    }

    @Override
    public void run()
    {
      try
      {
        Agent agent = P2Util.getAgentManager().getCurrentAgent();
        Profile profile = agent.getCurrentProfile();
        ProfileTransaction transaction = profile.change();

        EList<Repository> repositories = transaction.getProfileDefinition().getRepositories();
        final boolean firstTime = repositories.isEmpty();
        if (firstTime)
        {
          repositories.add(P2Factory.eINSTANCE.createRepository(InstallerDialog.INSTALLER_UPDATE_URL));
        }

        CommitContext commitContext = new CommitContext()
        {
          private IProvisioningPlan provisioningPlan;

          @Override
          public boolean handleProvisioningPlan(IProvisioningPlan provisioningPlan, Map<IInstallableUnit, DeltaType> iuDeltas,
              Map<IInstallableUnit, Map<String, Pair<Object, Object>>> propertyDeltas, List<IMetadataRepository> metadataRepositories) throws CoreException
          {
            if (firstTime && iuDeltas.isEmpty() && propertyDeltas.size() <= 1)
            {
              // Cancel if only the repository addition would be committed.
              return false;
            }

            this.provisioningPlan = provisioningPlan;
            return true;
          }

          @Override
          public Confirmer getUnsignedContentConfirmer()
          {
            User user = getInstaller().getUser();
            P2TaskImpl.processLicenses(provisioningPlan, ProgressPage.LICENSE_CONFIRMER, user, true, new NullProgressMonitor());
            provisioningPlan = null;

            return UnsignedContentDialog.createUnsignedContentConfirmer(user, true);
          }
        };

        updateResolution = transaction.resolve(commitContext, null);
      }
      catch (CoreException ex)
      {
        updateError = ex.getStatus();
      }
      finally
      {
        updateSearching = false;
      }
    }
  }
}
