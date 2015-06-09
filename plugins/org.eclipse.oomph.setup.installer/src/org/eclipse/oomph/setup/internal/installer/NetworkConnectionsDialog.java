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
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.util.ReflectUtil;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.net.ProxyPreferencePage;

import java.lang.reflect.Method;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class NetworkConnectionsDialog extends AbstractPreferenceDialog
{
  public static final String TITLE = "Network Connections";

  public static final String DESCRIPTION = "Adjust your network connection settings";

  public NetworkConnectionsDialog(Shell parentShell)
  {
    super(parentShell, TITLE);
  }

  @Override
  protected String getShellText()
  {
    return TITLE;
  }

  @Override
  protected String getDefaultMessage()
  {
    return DESCRIPTION + ".";
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
      final Composite mainComposite = new Composite(parent, SWT.NONE);
      mainComposite.setLayout(new GridLayout(1, false));
      mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

      final ScrolledComposite scrolledComposite = new ScrolledComposite(mainComposite, SWT.VERTICAL);
      scrolledComposite.setExpandHorizontal(true);
      scrolledComposite.setExpandVertical(true);
      scrolledComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

      final Composite composite = new Composite(scrolledComposite, SWT.NONE);
      GridLayout layout = new GridLayout(1, false);
      layout.marginWidth = 5;
      layout.marginHeight = 5;
      composite.setLayout(layout);

      scrolledComposite.setContent(composite);
      composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

      final int providerHeight = createProviderComposite(composite);
      final int proxyEntriesHeight = createProxyEntriesComposite(composite);
      createNonProxiedHostsComposite(composite, proxyEntriesHeight);

      applyDialogFont(composite);
      initializeValues();

      ControlAdapter resizeListener = new ControlAdapter()
      {
        int height = providerHeight + 2 * proxyEntriesHeight - 10;

        @Override
        public void controlResized(ControlEvent event)
        {
          int width = mainComposite.getClientArea().x;
          Point size = composite.computeSize(width, height - 10);
          scrolledComposite.setMinSize(size);
        }
      };

      scrolledComposite.addControlListener(resizeListener);
      composite.addControlListener(resizeListener);
      composite.notifyListeners(SWT.Resize, new Event());

      return composite;
    }

    private int createProviderComposite(Composite composite)
    {
      Method method = ReflectUtil.getMethod(ProxyPreferencePage.class, "createProviderComposite", Composite.class);
      ReflectUtil.invokeMethod(method, this, composite);
      Control control = getLastChild(composite);
      Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
      return size.y;
    }

    private int createProxyEntriesComposite(Composite composite)
    {
      Method method = ReflectUtil.getMethod(ProxyPreferencePage.class, "createProxyEntriesComposite", Composite.class);
      ReflectUtil.invokeMethod(method, this, composite);

      Control control = getLastChild(composite);
      Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
      GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
      layoutData.minimumHeight = size.y;
      control.setLayoutData(layoutData);
      return size.y;
    }

    private Control getLastChild(Composite composite)
    {
      Control[] children = composite.getChildren();
      Control control = children[children.length - 1];
      return control;
    }

    private void createNonProxiedHostsComposite(Composite composite, int height)
    {
      Method method = ReflectUtil.getMethod(ProxyPreferencePage.class, "createNonProxiedHostsComposite", Composite.class);
      ReflectUtil.invokeMethod(method, this, composite);

      Control control = getLastChild(composite);
      GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
      layoutData.minimumHeight = height;
      control.setLayoutData(layoutData);
    }

    private void initializeValues()
    {
      Method method = ReflectUtil.getMethod(ProxyPreferencePage.class, "initializeValues");
      ReflectUtil.invokeMethod(method, this);
    }
  }
}
