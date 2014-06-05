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

import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.net.ProxyPreferencePage;

import java.lang.reflect.Method;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class ProxyPreferenceDialog extends AbstractPreferenceDialog
{
  public ProxyPreferenceDialog(Shell parentShell)
  {
    super(parentShell, "Network Proxy Settings");
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Adjust your network proxy settings.";
  }

  @Override
  protected PreferencePage createPreferencePage()
  {
    return new ProxyPreferencePageWithoutHelp();
  }

  /**
   * @author Eike Stepper
   */
  private final class ProxyPreferencePageWithoutHelp extends ProxyPreferencePage
  {
    public ProxyPreferencePageWithoutHelp()
    {
      noDefaultAndApplyButton();
    }

    @Override
    protected Control createContents(Composite parent)
    {
      Composite composite = new Composite(parent, SWT.NONE);
      GridLayout layout = new GridLayout(1, false);
      layout.marginWidth = 5;
      layout.marginHeight = 5;
      composite.setLayout(layout);

      createProviderComposite(composite);
      createProxyEntriesComposite(composite);
      createNonProxiedHostsComposite(composite);

      applyDialogFont(composite);
      initializeValues();

      return composite;
    }

    private void createProviderComposite(Composite composite)
    {
      Method method = ReflectUtil.getMethod(ProxyPreferencePage.class, "createProviderComposite", Composite.class);
      ReflectUtil.invokeMethod(method, this, composite);
    }

    private void createProxyEntriesComposite(Composite composite)
    {
      Method method = ReflectUtil.getMethod(ProxyPreferencePage.class, "createProxyEntriesComposite", Composite.class);
      ReflectUtil.invokeMethod(method, this, composite);
    }

    private void createNonProxiedHostsComposite(Composite composite)
    {
      Method method = ReflectUtil.getMethod(ProxyPreferencePage.class, "createNonProxiedHostsComposite", Composite.class);
      ReflectUtil.invokeMethod(method, this, composite);
    }

    private void initializeValues()
    {
      Method method = ReflectUtil.getMethod(ProxyPreferencePage.class, "initializeValues");
      ReflectUtil.invokeMethod(method, this);
    }
  }
}
