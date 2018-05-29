/*
 * Copyright (c) 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.setup.installer.tests;

import org.eclipse.oomph.ui.tests.AbstractUITest;

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
    // SWTBotShell shell = bot.activeShell();
    //
    // // Create a project
    // bot.menu("File").menu("New").menu("Project...").click();
    // bot.shell("New Project").activate();
    // bot.tree().expandNode("General").select("Project");
    // bot.button("Next >").click();
    // bot.textWithLabel("Project name:").setText("test project");
    // bot.button("Finish").click();
    // shell.activate();

    // Widget widget = bot.widget(withId("productTree"));

    bot.treeWithId("productTree").getTreeItem("Eclipse.org").getNode("Eclipse IDE for Java Developers").select();
    bot.comboBox().setSelection("Latest (Mars)");
    // bot.toolbarButtonWithTooltip("Manage Virtual Machines...").click();
    // bot.tree().getTreeItem("System").getNode("C:\\Program Files\\Java\\jdk1.7.0_75\\jre (Current)").select();
    // bot.button("OK").click();
    bot.button("Next >").click();
    bot.button("Next >").click();
    // bot.textWithLabel("Installation folder name:").setText("java-latest2");
    bot.button("Next >").click();
    bot.button("Finish").click();
    bot.checkBox("Launch automatically").click();
  }
}
