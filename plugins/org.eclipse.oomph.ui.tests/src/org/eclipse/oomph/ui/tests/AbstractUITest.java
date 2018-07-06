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
package org.eclipse.oomph.ui.tests;

import org.eclipse.oomph.tests.AbstractTest;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.SWTBot;

import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public abstract class AbstractUITest extends AbstractTest
{
  protected static SWTBot bot;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
    bot = new SWTBot().shell("Eclipse Installer").bot();
    // bot = new ShellTrackingBot();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
    bot = null;
  }

  /**
   * @author Eike Stepper
   */
  public static class ShellTrackingBot extends SWTBot
  {
    private final Listener skinListener = new Listener()
    {
      public void handleEvent(Event event)
      {
        if (event.widget instanceof Shell)
        {
          Shell shell = (Shell)event.widget;
          addShell(shell);
        }
      }
    };

    private final ShellListener shellListener = new ShellListener()
    {
      public void shellActivated(ShellEvent e)
      {
        Shell shell = (Shell)e.widget;
        activeShell = shell;
      }

      public void shellDeactivated(ShellEvent e)
      {
        // Do nothing.
      }

      public void shellIconified(ShellEvent e)
      {
        Shell shell = (Shell)e.widget;
        shells.put(shell, true);
      }

      public void shellDeiconified(ShellEvent e)
      {
        Shell shell = (Shell)e.widget;
        shells.put(shell, false);
      }

      public void shellClosed(ShellEvent e)
      {
        final Shell shell = (Shell)e.widget;
        if (!shell.isDisposed())
        {
          display.syncExec(new Runnable()
          {
            public void run()
            {
              shell.removeShellListener(shellListener);
            }
          });
        }

        if (shell == activeShell)
        {
          activeShell = null;
        }

        shells.remove(shell);
      }
    };

    private final Map<Shell, Boolean> shells = new HashMap<Shell, Boolean>();

    private Shell activeShell;

    public ShellTrackingBot()
    {
      display.syncExec(new Runnable()
      {
        public void run()
        {
          for (Shell shell : display.getShells())
          {
            addShell(shell);
          }

          display.addListener(SWT.Skin, skinListener);
        }
      });
    }

    @Override
    public <T extends Widget> T widget(Matcher<T> matcher, Widget parentWidget, int index)
    {
      restoreShells();
      return super.widget(matcher, parentWidget, index);
    }

    @Override
    public <T extends Widget> T widget(Matcher<T> matcher, int index)
    {
      restoreShells();
      return super.widget(matcher, index);
    }

    public void restoreShells()
    {
      display.syncExec(new Runnable()
      {
        public void run()
        {
          for (Map.Entry<Shell, Boolean> entry : shells.entrySet())
          {
            Shell shell = entry.getKey();
            boolean minimized = entry.getValue();
            if (shell.getMinimized() != minimized)
            {
              shell.setMinimized(minimized);
            }
          }

          if (activeShell != null)
          {
            if (activeShell.isDisposed())
            {
              activeShell = null;
            }
            else
            {
              activeShell.forceActive();
              activeShell.forceFocus();
            }
          }
        }
      });
    }

    private void addShell(Shell shell)
    {
      if (!shells.containsKey(shell))
      {
        shells.put(shell, shell.getMinimized());
        shell.addShellListener(shellListener);
      }
    }
  }
}
