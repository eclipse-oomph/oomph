/*
 * Copyright (c) 2014-2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.core.CertificateConfirmer;
import org.eclipse.oomph.p2.core.CertificateConfirmer.TrustInfoWithPolicy;
import org.eclipse.oomph.p2.core.DelegatingUIServices;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.internal.p2.ui.ProvUIActivator;
import org.eclipse.equinox.internal.p2.ui.ProvUIMessages;
import org.eclipse.equinox.internal.p2.ui.dialogs.UserValidationDialog;
import org.eclipse.equinox.internal.p2.ui.viewers.CertificateLabelProvider;
import org.eclipse.equinox.internal.provisional.security.ui.X509CertificateViewDialog;
import org.eclipse.equinox.p2.core.UIServices;
import org.eclipse.equinox.p2.ui.LoadMetadataRepositoryJob;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeNode;
import org.eclipse.jface.viewers.TreeNodeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.SelectionDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The default GUI-based implementation of {@link UIServices}.
 * The service declaration is made in the serviceui_component.xml file.
 */
@SuppressWarnings("restriction")
public abstract class P2ServiceUI extends DelegatingUIServices
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

  public P2ServiceUI()
  {
  }

  @Override
  public AuthenticationInfo getUsernamePassword(final String location)
  {
    UIServices delegate = getDelegate();
    if (delegate != null)
    {
      return delegate.getUsernamePassword(location);
    }

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

  @Override
  public AuthenticationInfo getUsernamePassword(final String location, final AuthenticationInfo previousInfo)
  {
    UIServices delegate = getDelegate();
    if (delegate != null)
    {
      delegate.getUsernamePassword(location);
    }

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
      return new TrustInfoWithPolicy(trusted, persistTrust, trustUnsigned, false);
    }

    // We've established trust for unsigned content, now examine the untrusted chains.
    final AtomicBoolean remember = new AtomicBoolean();
    final AtomicBoolean always = new AtomicBoolean();
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

            @Override
            protected Button createButton(Composite parent, int id, String label, boolean defaultButton)
            {
              if (IDialogConstants.SELECT_ALL_ID == id)
              {
                ((GridData)parent.getLayoutData()).horizontalAlignment = SWT.FILL;
                ((GridLayout)parent.getLayout()).numColumns++;
                final Button rememberButton = new Button(parent, SWT.CHECK);
                rememberButton.setText(Messages.P2ServiceUI_certs_rememberAccepted);
                rememberButton.setFont(JFaceResources.getDialogFont());
                GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
                rememberButton.setLayoutData(data);
                rememberButton.addSelectionListener(new SelectionListener()
                {
                  public void widgetSelected(SelectionEvent e)
                  {
                    remember.set(rememberButton.getSelection());
                  }

                  public void widgetDefaultSelected(SelectionEvent e)
                  {
                    remember.set(rememberButton.getSelection());
                  }
                });
              }
              else if (IDialogConstants.OK_ID == id)
              {
                ((GridData)parent.getLayoutData()).horizontalAlignment = SWT.FILL;
                ((GridLayout)parent.getLayout()).numColumns++;
                final Button alwaysButton = new Button(parent, SWT.CHECK);
                alwaysButton.setText(Messages.P2ServiceUI_certs_alwaysAccept);
                alwaysButton.setFont(JFaceResources.getDialogFont());
                GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
                alwaysButton.setLayoutData(data);
                alwaysButton.addSelectionListener(new SelectionListener()
                {
                  public void widgetSelected(SelectionEvent e)
                  {
                    always.set(alwaysButton.getSelection());
                  }

                  public void widgetDefaultSelected(SelectionEvent e)
                  {
                    always.set(alwaysButton.getSelection());
                  }
                });

              }

              return super.createButton(parent, id, label, defaultButton);
            }
          };

          trustCertificateDialog.open();
          Object[] dialogResult = trustCertificateDialog.getResult();
          Certificate[] values = new Certificate[dialogResult == null ? 0 : dialogResult.length];
          for (int i = 0; i < values.length; i++)
          {
            values[i] = (Certificate)((TreeNode)dialogResult[i]).getValue();
          }

          result[0] = values;
        }
      });

      trusted = (Certificate[])result[0];
    }

    return new CertificateConfirmer.TrustInfoWithPolicy(trusted, remember.get(), trustUnsigned, always.get());
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

            // During a self-update, there will be a progress dialog and if it's not the parent of any created dialog, that dialog will be unusable.
            for (Shell child : result[0].getShells())
            {
              if (child.isVisible() && child.getData() instanceof Dialog)
              {
                result[0] = child;
                break;
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

  /**
   * A dialog that displays a certificate chain and asks the user if they trust the certificate providers.
   * Copied from org.eclipse.equinox.internal.p2.ui.dialogs.TrustCertificateDialog because it was changed in a way that could not be backward compatible.
   */
  private static class TrustCertificateDialog extends SelectionDialog
  {
    private Object inputElement;

    private IStructuredContentProvider contentProvider;

    private ILabelProvider labelProvider;

    private final static int SIZING_SELECTION_WIDGET_HEIGHT = 250;

    private final static int SIZING_SELECTION_WIDGET_WIDTH = 300;

    CheckboxTableViewer listViewer;

    private TreeViewer certificateChainViewer;

    private Button detailsButton;

    protected TreeNode parentElement;

    protected Object selectedCertificate;

    public TrustCertificateDialog(Shell parentShell, Object input, ILabelProvider labelProvider, ITreeContentProvider contentProvider)
    {
      super(parentShell);
      inputElement = input;
      this.contentProvider = contentProvider;
      this.labelProvider = labelProvider;
      setTitle(ProvUIMessages.TrustCertificateDialog_Title);
      setMessage(ProvUIMessages.TrustCertificateDialog_Message);
      setShellStyle(SWT.DIALOG_TRIM | SWT.MODELESS | SWT.RESIZE | getDefaultOrientation());
    }

    @Override
    protected Control createDialogArea(Composite parent)
    {
      Composite composite = createUpperDialogArea(parent);
      certificateChainViewer = new TreeViewer(composite, SWT.BORDER);
      GridLayout layout = new GridLayout();
      certificateChainViewer.getTree().setLayout(layout);
      GridData data = new GridData(GridData.FILL_BOTH);
      data.grabExcessHorizontalSpace = true;
      certificateChainViewer.getTree().setLayoutData(data);
      certificateChainViewer.setAutoExpandLevel(AbstractTreeViewer.ALL_LEVELS);
      certificateChainViewer.setContentProvider(new TreeNodeContentProvider());
      certificateChainViewer.setLabelProvider(new CertificateLabelProvider());
      certificateChainViewer.addSelectionChangedListener(getChainSelectionListener());
      if (inputElement instanceof Object[])
      {
        ISelection selection = null;
        Object[] nodes = (Object[])inputElement;
        if (nodes.length > 0)
        {
          selection = new StructuredSelection(nodes[0]);
          certificateChainViewer.setInput(new TreeNode[] { (TreeNode)nodes[0] });
          selectedCertificate = nodes[0];
        }
        listViewer.setSelection(selection);
      }
      listViewer.addDoubleClickListener(getDoubleClickListener());
      listViewer.addSelectionChangedListener(getParentSelectionListener());
      createButtons(composite);
      return composite;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
      createButton(parent, IDialogConstants.OK_ID, ProvUIMessages.TrustCertificateDialog_AcceptSelectedButtonLabel, true);
      createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
      super.getOkButton().setEnabled(false);
    }

    private void createButtons(Composite composite)
    {
      Composite buttonComposite = new Composite(composite, SWT.NONE);
      buttonComposite.setLayout(new RowLayout());
      // Details button to view certificate chain
      detailsButton = new Button(buttonComposite, SWT.NONE);
      detailsButton.setText(ProvUIMessages.TrustCertificateDialog_Details);
      detailsButton.addSelectionListener(new SelectionListener()
      {
        public void widgetDefaultSelected(SelectionEvent e)
        {
          Object o = selectedCertificate;
          if (selectedCertificate instanceof TreeNode)
          {
            o = ((TreeNode)selectedCertificate).getValue();
          }
          if (o instanceof X509Certificate)
          {
            X509Certificate cert = (X509Certificate)o;
            X509CertificateViewDialog certificateDialog = new X509CertificateViewDialog(getShell(), cert);
            certificateDialog.open();
          }
        }

        public void widgetSelected(SelectionEvent e)
        {
          widgetDefaultSelected(e);
        }
      });

      Button exportButton = new Button(buttonComposite, SWT.NONE);
      exportButton.setText(ProvUIMessages.TrustCertificateDialog_Export);
      exportButton.addSelectionListener(new SelectionListener()
      {
        public void widgetDefaultSelected(SelectionEvent e)
        {
          Object o = selectedCertificate;
          if (selectedCertificate instanceof TreeNode)
          {
            o = ((TreeNode)selectedCertificate).getValue();
          }
          if (o instanceof X509Certificate)
          {
            X509Certificate cert = (X509Certificate)o;
            FileDialog destination = new FileDialog(detailsButton.getShell(), SWT.SAVE);
            destination.setFilterExtensions(new String[] { "*.der" }); //$NON-NLS-1$
            destination.setText(ProvUIMessages.TrustCertificateDialog_Export);
            String path = destination.open();
            if (path == null)
            {
              return;
            }
            File destinationFile = new File(path);
            FileOutputStream output = null;

            try
            {
              output = new FileOutputStream(destinationFile);
              output.write(cert.getEncoded());
            }
            catch (IOException ex)
            {
              ProvUIActivator.getDefault().getLog().log(new Status(IStatus.ERROR, ProvUIActivator.PLUGIN_ID, ex.getMessage(), ex));
            }
            catch (CertificateEncodingException ex)
            {
              ProvUIActivator.getDefault().getLog().log(new Status(IStatus.ERROR, ProvUIActivator.PLUGIN_ID, ex.getMessage(), ex));
            }
            finally
            {
              IOUtil.closeSilent(output);
            }
          }
        }

        public void widgetSelected(SelectionEvent e)
        {
          widgetDefaultSelected(e);
        }
      });
    }

    private Composite createUpperDialogArea(Composite parent)
    {
      Composite composite = (Composite)super.createDialogArea(parent);
      initializeDialogUnits(composite);
      createMessageArea(composite);

      listViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER);
      GridData data = new GridData(GridData.FILL_BOTH);
      data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
      data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;
      listViewer.getTable().setLayoutData(data);

      listViewer.setLabelProvider(labelProvider);
      listViewer.setContentProvider(contentProvider);

      addSelectionButtons(composite);

      listViewer.setInput(inputElement);

      if (!getInitialElementSelections().isEmpty())
      {
        checkInitialSelections();
      }

      Dialog.applyDialogFont(composite);

      return composite;
    }

    /**
     * Visually checks the previously-specified elements in this dialog's list
     * viewer.
     */
    private void checkInitialSelections()
    {
      Iterator<?> itemsToCheck = getInitialElementSelections().iterator();
      while (itemsToCheck.hasNext())
      {
        listViewer.setChecked(itemsToCheck.next(), true);
      }
    }

    /**
     * Add the selection and deselection buttons to the dialog.
     *
     * @param composite org.eclipse.swt.widgets.Composite
     */
    private void addSelectionButtons(Composite composite)
    {
      Composite buttonComposite = new Composite(composite, SWT.NONE);
      GridLayout layout = new GridLayout();
      layout.numColumns = 0;
      layout.marginWidth = 0;
      layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
      buttonComposite.setLayout(layout);
      buttonComposite.setLayoutData(new GridData(SWT.END, SWT.TOP, true, false));

      Button selectButton = createButton(buttonComposite, IDialogConstants.SELECT_ALL_ID, ProvUIMessages.TrustCertificateDialog_SelectAll, false);

      SelectionListener listener = new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          listViewer.setAllChecked(true);
          getOkButton().setEnabled(true);
        }
      };
      selectButton.addSelectionListener(listener);

      Button deselectButton = createButton(buttonComposite, IDialogConstants.DESELECT_ALL_ID, ProvUIMessages.TrustCertificateDialog_DeselectAll, false);

      listener = new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          listViewer.setAllChecked(false);
          getOkButton().setEnabled(false);
        }
      };
      deselectButton.addSelectionListener(listener);
    }

    private ISelectionChangedListener getChainSelectionListener()
    {
      return new ISelectionChangedListener()
      {
        public void selectionChanged(SelectionChangedEvent event)
        {
          ISelection selection = event.getSelection();
          if (selection instanceof StructuredSelection)
          {
            selectedCertificate = ((StructuredSelection)selection).getFirstElement();
          }
        }
      };
    }

    public TreeViewer getCertificateChainViewer()
    {
      return certificateChainViewer;
    }

    private IDoubleClickListener getDoubleClickListener()
    {
      return new IDoubleClickListener()
      {
        public void doubleClick(DoubleClickEvent event)
        {
          StructuredSelection selection = (StructuredSelection)event.getSelection();
          Object selectedElement = selection.getFirstElement();
          if (selectedElement instanceof TreeNode)
          {
            TreeNode treeNode = (TreeNode)selectedElement;
            // create and open dialog for certificate chain
            X509CertificateViewDialog certificateViewDialog = new X509CertificateViewDialog(getShell(), (X509Certificate)treeNode.getValue());
            certificateViewDialog.open();
          }
        }
      };
    }

    private ISelectionChangedListener getParentSelectionListener()
    {
      return new ISelectionChangedListener()
      {

        public void selectionChanged(SelectionChangedEvent event)
        {
          ISelection selection = event.getSelection();
          if (selection instanceof StructuredSelection)
          {
            TreeNode firstElement = (TreeNode)((StructuredSelection)selection).getFirstElement();
            getCertificateChainViewer().setInput(new TreeNode[] { firstElement });
            getOkButton().setEnabled(listViewer.getChecked(firstElement));
            getCertificateChainViewer().refresh();
          }
        }
      };
    }

    /**
     * The <code>ListSelectionDialog</code> implementation of this
     * <code>Dialog</code> method builds a list of the selected elements for later
     * retrieval by the client and closes this dialog.
     */
    @Override
    protected void okPressed()
    {
      // Get the input children.
      Object[] children = contentProvider.getElements(inputElement);

      // Build a list of selected children.
      if (children != null)
      {
        ArrayList<Object> list = new ArrayList<Object>();
        for (Object element : children)
        {
          if (listViewer.getChecked(element))
          {
            list.add(element);
          }
        }
        setResult(list);
      }
      super.okPressed();
    }
  }
}
