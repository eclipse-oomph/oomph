/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.ui;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.Tree;

/**
 * @author Eike Stepper
 */
public abstract class ActivityAdapter
{
  private final TraverseListener traverseListener = new TraverseListener()
  {
    public final void keyTraversed(TraverseEvent e)
    {
      handleActivity();
    }
  };

  private final KeyListener keyListener = new KeyListener()
  {

    public final void keyPressed(KeyEvent e)
    {
      handleActivity();
    }

    public final void keyReleased(KeyEvent e)
    {
      handleActivity();
    }
  };

  private final MouseListener mouseListener = new MouseListener()
  {

    public final void mouseUp(MouseEvent e)
    {
      handleActivity();
    }

    public final void mouseDown(MouseEvent e)
    {
      handleActivity();
    }

    public final void mouseDoubleClick(MouseEvent e)
    {
      handleActivity();
    }
  };

  private final SelectionListener selectionListener = new SelectionListener()
  {

    public final void widgetSelected(SelectionEvent e)
    {
      handleActivity();
    }

    public final void widgetDefaultSelected(SelectionEvent e)
    {
      handleActivity();
    }
  };

  private final ModifyListener modifyListener = new ModifyListener()
  {

    public final void modifyText(ModifyEvent e)
    {
      handleActivity();
    }
  };

  public void attach(Control control)
  {
    if (control instanceof Button)
    {
      Button notifier = (Button)control;
      notifier.addSelectionListener(selectionListener);
    }

    if (control instanceof Text)
    {
      Text notifier = (Text)control;
      notifier.addTraverseListener(traverseListener);
      notifier.addKeyListener(keyListener);
      notifier.addMouseListener(mouseListener);
      notifier.addSelectionListener(selectionListener);
      notifier.addModifyListener(modifyListener);
    }

    if (control instanceof Link)
    {
      Link notifier = (Link)control;
      notifier.addTraverseListener(traverseListener);
      notifier.addKeyListener(keyListener);
      notifier.addMouseListener(mouseListener);
      notifier.addSelectionListener(selectionListener);
    }

    if (control instanceof Combo)
    {
      Combo notifier = (Combo)control;
      notifier.addTraverseListener(traverseListener);
      notifier.addKeyListener(keyListener);
      notifier.addMouseListener(mouseListener);
      notifier.addSelectionListener(selectionListener);
      notifier.addModifyListener(modifyListener);
    }

    if (control instanceof Tree)
    {
      Tree notifier = (Tree)control;
      notifier.addTraverseListener(traverseListener);
      notifier.addKeyListener(keyListener);
      notifier.addMouseListener(mouseListener);
      notifier.addSelectionListener(selectionListener);
    }

    if (control instanceof Table)
    {
      Table notifier = (Table)control;
      notifier.addTraverseListener(traverseListener);
      notifier.addKeyListener(keyListener);
      notifier.addMouseListener(mouseListener);
      notifier.addSelectionListener(selectionListener);
    }

    if (control instanceof ToolBar)
    {
      ToolBar notifier = (ToolBar)control;
      notifier.addTraverseListener(traverseListener);
      notifier.addKeyListener(keyListener);
      notifier.addMouseListener(mouseListener);
    }

    if (control instanceof Composite)
    {
      Composite composite = (Composite)control;
      for (Control child : composite.getChildren())
      {
        attach(child);
      }
    }
  }

  protected abstract void handleActivity();
}
