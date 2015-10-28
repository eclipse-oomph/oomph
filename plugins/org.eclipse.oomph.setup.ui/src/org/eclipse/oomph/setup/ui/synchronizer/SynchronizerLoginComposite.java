/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.ui.synchronizer;

import org.eclipse.oomph.setup.internal.sync.SynchronizerCredentials;
import org.eclipse.oomph.setup.internal.sync.SynchronizerService;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;

import java.net.URI;

/**
 * @author Eike Stepper
 */
public class SynchronizerLoginComposite extends Composite
{
  private final ModifyListener listener = new ModifyListener()
  {
    public void modifyText(ModifyEvent e)
    {
      credentials = new SynchronizerCredentials(usernameText.getText(), passwordText.getText());
      validate();
    }
  };

  private SynchronizerService service;

  private SynchronizerCredentials credentials;

  private Label usernameLabel;

  private Text usernameText;

  private Label passwordLabel;

  private Text passwordText;

  private Link signupLink;

  public SynchronizerLoginComposite(Composite parent, int style, int marginWidth, int marginHeight)
  {
    super(parent, style);

    GridLayout layout = UIUtil.createGridLayout(getGridColumns());
    layout.marginWidth = marginWidth;
    layout.marginHeight = marginHeight;

    setLayout(layout);

    createUI(this, layout.numColumns);
  }

  public SynchronizerService getService()
  {
    return service;
  }

  public void setService(SynchronizerService service)
  {
    signupLink.setVisible(false);

    this.service = service;
    if (service != null)
    {
      URI signupURI = service.getSignupURI();
      if (signupURI != null)
      {
        String label = getServiceLabel(service);

        signupLink.setText("<a>Create an " + label + " account</a>");
        signupLink.setVisible(true);
      }

      setCredentials(service.getCredentials());
    }
  }

  public SynchronizerCredentials getCredentials()
  {
    return credentials;
  }

  public void setCredentials(SynchronizerCredentials credentials)
  {
    this.credentials = credentials;
    if (credentials != null)
    {
      usernameText.setText(StringUtil.safe(credentials.getUsername()));
      passwordText.setText(StringUtil.safe(credentials.getPassword()));
    }
    else
    {
      usernameText.setText(StringUtil.EMPTY);
      passwordText.setText(StringUtil.EMPTY);
    }
  }

  public int getGridColumns()
  {
    return 2;
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    usernameLabel.setEnabled(enabled);
    usernameText.setEnabled(enabled);
    passwordLabel.setEnabled(enabled);
    passwordText.setEnabled(enabled);
    signupLink.setEnabled(enabled);
  }

  @Override
  public boolean setFocus()
  {
    if (usernameText != null && usernameText.setFocus())
    {
      return true;
    }

    return super.setFocus();
  }

  protected void createUI(Composite parent, int columns)
  {
    usernameLabel = new Label(parent, SWT.NONE);
    usernameLabel.setText("User name:");

    usernameText = new Text(parent, SWT.BORDER);
    usernameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, columns - 1, 1));
    usernameText.addModifyListener(listener);

    passwordLabel = new Label(parent, SWT.NONE);
    passwordLabel.setText("Password:");

    passwordText = new Text(parent, SWT.BORDER | SWT.PASSWORD);
    passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, columns - 1, 1));
    passwordText.addModifyListener(listener);

    new Label(parent, SWT.NONE);

    signupLink = new Link(parent, SWT.NONE);
    signupLink.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, columns - 1, 1));
    signupLink.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        String uri = service.getSignupURI().toString();
        if (!OS.INSTANCE.openSystemBrowser(uri))
        {
          String serviceLabel = getServiceLabel(service);
          MessageDialog.openInformation(getShell(), "System Browser Not Found", "Go to " + uri + " to create an " + serviceLabel + " account.");
        }
      }
    });
  }

  protected void validate()
  {
  }

  private String getServiceLabel(SynchronizerService service)
  {
    String label = service.getLabel();
    if (StringUtil.isEmpty(label))
    {
      label = service.getServiceURI().toString();
    }

    return label;
  }
}
