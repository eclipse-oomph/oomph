/*
 * Copyright (c) 2015, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.jreinfo.ui;

import org.eclipse.oomph.internal.ui.AbstractPreferencePage;
import org.eclipse.oomph.jreinfo.JREFilter;
import org.eclipse.oomph.util.Request;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author Eike Stepper
 */
public class JREPreferencePage extends AbstractPreferencePage
{
  public JREPreferencePage()
  {
    noDefaultAndApplyButton();
  }

  @Override
  protected Control doCreateContents(Composite parent)
  {
    JREComposite jreComposite = new JREComposite(parent, SWT.NONE, Request.Handler.SYSTEM_BROWSER, null, null);
    jreComposite.setJREFilter(new JREFilter(null, null, (Integer)null));
    return jreComposite;
  }
}
