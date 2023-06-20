/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.installer.tests;

import static org.junit.Assert.assertTrue;

import org.eclipse.oomph.ui.tests.AbstractUITest;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;

/**
 * @author Eike Stepper
 */
@FixMethodOrder(MethodSorters.JVM)
public class InstallerTests extends AbstractUITest
{
  @Override
  public void setUp() throws Exception
  {

    super.setUp();

    File userHome = getUserHome();
    System.out.println(userHome);
  }

  @Test
  public void test1()
  {
    bot.treeWithId("productTree").getTreeItem("Eclipse.org").getNode("Eclipse Platform").select();
    bot.comboBox().setSelection("2023-09");

    bot.button("Next >").click();
    bot.button("Next >").click();
    bot.button("Next >").click();

    assertTrue("The finish button should be enabled", bot.button("Finish").isEnabled());

    SWTBotButton cancelButton = bot.button("Cancel");
    Display.getDefault().asyncExec(() -> {
      cancelButton.click();
    });
  }
}
