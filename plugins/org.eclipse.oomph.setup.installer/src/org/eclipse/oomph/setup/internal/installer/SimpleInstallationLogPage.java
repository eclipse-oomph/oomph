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
import org.eclipse.oomph.util.IOUtil;

import org.eclipse.emf.common.util.URI;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;

import java.io.File;

/**
 * @author Andreas Scharf
 */
public class SimpleInstallationLogPage extends SimpleInstallerPage
{
  private static final int SCROLL_SPEED = 8;

  private ScrolledComposite scrolledComposite;

  private Text text;

  private File installationLogFile;

  public SimpleInstallationLogPage(Composite parent, SimpleInstallerDialog dialog)
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
    container.setBackground(SetupInstallerPlugin.COLOR_WHITE);

    Label title = new Label(container, SWT.NONE);
    title.setText("INSTALLATION LOG");
    title.setForeground(UIUtil.COLOR_PURPLE);
    title.setFont(SetupInstallerPlugin.getFont(SimpleInstallerDialog.getDefaultFont(), URI.createURI("font:///12/bold")));
    title.setLayoutData(GridDataFactory.swtDefaults().create());

    scrolledComposite = new ScrolledComposite(container, SWT.V_SCROLL | SWT.H_SCROLL);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);
    text = new Text(scrolledComposite, SWT.MULTI | SWT.READ_ONLY);
    scrolledComposite.setContent(text);
    scrolledComposite.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).indent(0, 20).create());

    // Workaround for bug 93472 (Content of ScrolledComposite doesn't get scrolled by mousewheel).
    // Setting the focus on the scroller doesn't work, that is why we forward the mouse wheel event.
    text.addListener(SWT.MouseVerticalWheel, new Listener()
    {
      public void handleEvent(Event event)
      {
        int value = event.count * SCROLL_SPEED;
        ScrollBar vbar = scrolledComposite.getVerticalBar();
        vbar.setSelection(vbar.getSelection() - value);

        Listener[] selectionListeners = vbar.getListeners(SWT.Selection);
        for (Listener listener : selectionListeners)
        {
          listener.handleEvent(event);
        }
      }
    });
  }

  public File getReadmeURI()
  {
    return installationLogFile;
  }

  public void setInstallationLogFile(File installationLogFile)
  {
    if (this.installationLogFile != installationLogFile)
    {
      this.installationLogFile = installationLogFile;

      if (this.installationLogFile != null && this.installationLogFile.exists())
      {
        text.setText(readLog());
      }
      else
      {
        text.setText("No installation log available.");
      }

      scrolledComposite.setMinSize(text.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    }
  }

  private String readLog()
  {
    return new String(IOUtil.readFile(installationLogFile));
  }
}
