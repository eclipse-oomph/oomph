/*
 * Copyright (c) 2014, 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.internal.p2.ui.ProvUIActivator;
import org.eclipse.equinox.internal.p2.ui.ProvUIMessages;
import org.eclipse.equinox.internal.p2.ui.dialogs.TrustCertificateDialog;
import org.eclipse.equinox.internal.p2.ui.dialogs.UserValidationDialog;
import org.eclipse.equinox.internal.p2.ui.viewers.CertificateLabelProvider;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.equinox.p2.ui.LoadMetadataRepositoryJob;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * The default GUI-based implementation of {@link UIServices}.
 * The service declaration is made in the serviceui_component.xml file.
 */
@SuppressWarnings("restriction")
public class P2ServiceUI extends UIServices
{
  static class OkCancelErrorDialog extends ErrorDialog
  {
    public OkCancelErrorDialog(Shell parentShell, String dialogTitle, String message, IStatus status, int displayMask)
    {
      super(parentShell, dialogTitle, message, status, displayMask);
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
      createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
      createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, true);
      createDetailsButton(parent);
    }
  }

  @Override
  public AuthenticationInfo getUsernamePassword(final String location)
  {
    final AuthenticationInfo[] result = new AuthenticationInfo[1];
    if (!suppressAuthentication())
    {
      UIUtil.getDisplay().syncExec(new Runnable()
      {
        public void run()
        {
          String message = NLS.bind(ProvUIMessages.ServiceUI_LoginDetails, location);
          UserValidationDialog dialog = new UserValidationDialog(getShell(), ProvUIMessages.ServiceUI_LoginRequired, null, message);
          int dialogCode = dialog.open();
          if (dialogCode == Window.OK)
          {
            result[0] = dialog.getResult();
          }
          else if (dialogCode == Window.CANCEL)
          {
            result[0] = AUTHENTICATION_PROMPT_CANCELED;
          }
        }
      });
    }

    return result[0];
  }

  private boolean suppressAuthentication()
  {
    Job job = Job.getJobManager().currentJob();
    if (job != null)
    {
      return job.getProperty(LoadMetadataRepositoryJob.SUPPRESS_AUTHENTICATION_JOB_MARKER) != null;
    }

    return false;
  }

  @Override
  public TrustInfo getTrustInfo(Certificate[][] untrustedChains, final String[] unsignedDetail)
  {
    boolean trustUnsigned = true;
    boolean persistTrust = false;
    Certificate[] trusted = new Certificate[0];

    // Some day we may summarize all of this in one UI, or perhaps we'll have a preference to honor regarding
    // unsigned content. For now we prompt separately first as to whether unsigned detail should be trusted
    final Shell shell = getShell();
    if (shell != null && unsignedDetail != null && unsignedDetail.length > 0)
    {
      final boolean[] result = new boolean[] { false };
      shell.getDisplay().syncExec(new Runnable()
      {
        public void run()
        {
          OkCancelErrorDialog dialog = new OkCancelErrorDialog(shell, ProvUIMessages.ServiceUI_warning_title, null, createStatus(), IStatus.WARNING);
          result[0] = dialog.open() == IDialogConstants.OK_ID;
        }

        private IStatus createStatus()
        {
          MultiStatus parent = new MultiStatus(ProvUIActivator.PLUGIN_ID, 0, ProvUIMessages.ServiceUI_unsigned_message, null);
          for (int i = 0; i < unsignedDetail.length; i++)
          {
            parent.add(new Status(IStatus.WARNING, ProvUIActivator.PLUGIN_ID, unsignedDetail[i]));
          }
          return parent;
        }
      });

      trustUnsigned = result[0];
    }

    // For now, there is no need to show certificates if there was unsigned content and we don't trust it.
    if (!trustUnsigned)
    {
      return new TrustInfo(trusted, persistTrust, trustUnsigned);
    }

    // We've established trust for unsigned content, now examine the untrusted chains.
    if (shell != null && untrustedChains != null && untrustedChains.length > 0)
    {
      final Object[] result = new Object[1];
      final TreeNode[] input = createTreeNodes(untrustedChains);

      UIUtil.getDisplay().syncExec(new Runnable()
      {
        public void run()
        {
          ILabelProvider labelProvider = new CertificateLabelProvider();
          TreeNodeContentProvider contentProvider = new TreeNodeContentProvider();
          TrustCertificateDialog trustCertificateDialog = new TrustCertificateDialog(shell, input, labelProvider, contentProvider)
          {
            @Override
            protected void configureShell(Shell shell)
            {
              Image[] defaultImages = getDefaultImages();
              if (defaultImages.length > 0)
              {
                List<Image> nonDisposedImages = new ArrayList<Image>(defaultImages.length);
                for (Image defaultImage : defaultImages)
                {
                  if (defaultImage != null && !defaultImage.isDisposed())
                  {
                    nonDisposedImages.add(defaultImage);
                  }
                }

                if (!nonDisposedImages.isEmpty())
                {
                  Image[] array = new Image[nonDisposedImages.size()];
                  nonDisposedImages.toArray(array);
                  shell.setImages(array);
                }
              }

              Layout layout = getLayout();
              if (layout != null)
              {
                shell.setLayout(layout);
              }

              shell.setText(ProvUIMessages.TrustCertificateDialog_Title);
            }
          };

          trustCertificateDialog.open();
          Certificate[] values = new Certificate[trustCertificateDialog.getResult() == null ? 0 : trustCertificateDialog.getResult().length];
          for (int i = 0; i < values.length; i++)
          {
            values[i] = (Certificate)((TreeNode)trustCertificateDialog.getResult()[i]).getValue();
          }

          result[0] = values;
        }
      });

      persistTrust = true;
      trusted = (Certificate[])result[0];
    }

    return new TrustInfo(trusted, persistTrust, trustUnsigned);
  }

  private TreeNode[] createTreeNodes(Certificate[][] certificates)
  {
    TreeNode[] children = new TreeNode[certificates.length];
    for (int i = 0; i < certificates.length; i++)
    {
      TreeNode head = new TreeNode(certificates[i][0]);
      TreeNode parent = head;
      children[i] = head;
      for (int j = 0; j < certificates[i].length; j++)
      {
        TreeNode node = new TreeNode(certificates[i][j]);
        node.setParent(parent);
        parent.setChildren(new TreeNode[] { node });
        parent = node;
      }
    }
    return children;
  }

  @Override
  public AuthenticationInfo getUsernamePassword(final String location, final AuthenticationInfo previousInfo)
  {
    final AuthenticationInfo[] result = new AuthenticationInfo[1];
    if (!suppressAuthentication())
    {
      final Shell shell = getShell();
      if (shell != null)
      {
        UIUtil.getDisplay().syncExec(new Runnable()
        {
          public void run()
          {
            String message = null;
            if (previousInfo.saveResult())
            {
              message = NLS.bind(ProvUIMessages.ProvUIMessages_SavedNotAccepted_EnterFor_0, location);
            }
            else
            {
              message = NLS.bind(ProvUIMessages.ProvUIMessages_NotAccepted_EnterFor_0, location);
            }

            UserValidationDialog dialog = new UserValidationDialog(previousInfo, shell, ProvUIMessages.ServiceUI_LoginRequired, null, message);
            int dialogCode = dialog.open();
            if (dialogCode == Window.OK)
            {
              result[0] = dialog.getResult();
            }
            else if (dialogCode == Window.CANCEL)
            {
              result[0] = AUTHENTICATION_PROMPT_CANCELED;
            }
          }
        });
      }
    }

    return result[0];
  }

  protected Shell getShell()
  {
    try
    {
      final Shell shell = UIUtil.getShell();
      final Shell[] result = new Shell[] { shell };
      if (shell != null)
      {
        UIUtil.syncExec(shell, new Runnable()
        {
          public void run()
          {
            for (Composite parent = shell.getParent(); parent != null; parent = parent.getParent())
            {
              if (parent instanceof Shell)
              {
                result[0] = (Shell)parent;
              }
            }
          }
        });
      }

      return result[0];
    }
    catch (Throwable ex)
    {
      return null;
    }
  }
}
