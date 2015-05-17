/*
 * Copyright (c) 2015 The Eclipse Foundation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Yatta Solutions - [466264] initial API and implementation
 */
package org.eclipse.oomph.setup.internal.installer;

import org.eclipse.oomph.ui.UIUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author Andreas Scharf
 */
public class SimpleReadmePage extends SimpleInstallerPage
{
  private java.net.URI readmeURI;

  private Browser browser;

  public SimpleReadmePage(Composite parent, SimpleInstallerDialog dialog)
  {
    super(parent, dialog, true);
  }

  @Override
  protected void createContent(Composite container)
  {
    GridLayout layout = new GridLayout(1, false);
    layout.marginLeft = 17;
    layout.marginRight = 11;
    layout.marginTop = 39;
    layout.marginBottom = 30;
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    layout.verticalSpacing = 0;

    container.setLayout(layout);
    container.setBackgroundMode(SWT.INHERIT_FORCE);
    container.setBackground(AbstractSimpleDialog.COLOR_WHITE);

    Label title = new Label(container, SWT.NONE);
    title.setText("README");
    title.setForeground(UIUtil.getEclipseThemeColor());
    title.setFont(SetupInstallerPlugin.getFont(SimpleInstallerDialog.getDefaultFont(), URI.createURI("font:///12/bold")));
    title.setLayoutData(GridDataFactory.swtDefaults().create());

    browser = new Browser(container, SWT.NONE);
    browser.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).indent(0, 20).create());
    browser.addLocationListener(new LocationAdapter()
    {
      @Override
      public void changed(LocationEvent event)
      {
        styleBrowser();
      }
    });
  }

  private void styleBrowser()
  {
    StringBuilder styleInjection = new StringBuilder();
    styleInjection.append("var newStyle = document.createElement(\"style\");\n");
    styleInjection.append("newStyle.setAttribute(\"type\", \"text/css\");\n");
    styleInjection.append("newStyle.innerHTML = \"body{overflow-x:hidden}\"\n");
    styleInjection.append("document.getElementsByTagName(\"head\")[0].appendChild(newStyle);\n");
    browser.execute(styleInjection.toString());
  }

  public java.net.URI getReadmeURI()
  {
    return readmeURI;
  }

  public void setReadmeURI(java.net.URI readmeURI)
  {
    if (this.readmeURI != readmeURI)
    {
      this.readmeURI = readmeURI;

      if (readmeURI != null)
      {
        browser.setUrl(readmeURI.toString());
      }
    }
  }
}
