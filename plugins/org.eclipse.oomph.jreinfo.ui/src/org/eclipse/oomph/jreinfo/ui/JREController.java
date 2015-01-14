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
package org.eclipse.oomph.jreinfo.ui;

import org.eclipse.oomph.jreinfo.JRE;
import org.eclipse.oomph.jreinfo.JREFilter;
import org.eclipse.oomph.jreinfo.JREManager;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.Request;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * @author Eike Stepper
 */
public abstract class JREController implements ISelectionChangedListener
{
  private static final String NO_JRE_FOUND = "No match; click to configure...";

  private final Label label;

  private final StructuredViewer viewer;

  private final Request.Handler downloadHandler;

  private String javaVersion;

  private int bitness = JREManager.BITNESS;

  private JREFilter jreFilter;

  private JRE jre;

  private boolean refreshing;

  public JREController(Label label, StructuredViewer viewer, Request.Handler downloadHandler)
  {
    this.label = label;
    this.viewer = viewer;
    this.downloadHandler = downloadHandler;

    viewer.addSelectionChangedListener(this);
  }

  public final StructuredViewer getViewer()
  {
    return viewer;
  }

  public final String getJavaVersion()
  {
    return javaVersion;
  }

  public final void setJavaVersion(String javaVersion)
  {
    if (!ObjectUtil.equals(this.javaVersion, javaVersion))
    {
      this.javaVersion = javaVersion;

      if (label != null)
      {
        String text = "Java";
        if (!StringUtil.isEmpty(javaVersion))
        {
          text += " " + javaVersion;
        }

        text += " VM";
        setLabel(text);
      }
    }

    refresh();
  }

  public final int getBitness()
  {
    return bitness;
  }

  public final void setBitness(int bitness)
  {
    if (this.bitness != bitness)
    {
      this.bitness = bitness;
    }

    refresh();
  }

  public final JRE getJRE()
  {
    return jre;
  }

  public final void setJRE(JRE jre)
  {
    doSetJRE(jre);
    setSelection(new StructuredSelection(jre));
  }

  public void setSelection(ISelection selection)
  {
    viewer.setSelection(selection);
    UIUtil.clearTextSelection(viewer);
  }

  public void configureJREs()
  {
    JREDialog dialog = new JREDialog(viewer.getControl().getShell(), downloadHandler)
    {
      @Override
      protected void createButtonsForButtonBar(Composite parent)
      {
        super.createButtonsForButtonBar(parent);
        Button button = getButton(IDialogConstants.OK_ID);
        if (button != null)
        {
          button.setEnabled(false);
        }
      }

      @Override
      protected void elementChanged(Object element)
      {
        Button button = getButton(IDialogConstants.OK_ID);
        if (button != null)
        {
          button.setEnabled(isMatchingJRE(element));
        }
      }

      private boolean isMatchingJRE(Object element)
      {
        if (element instanceof JRE)
        {
          JRE jre = (JRE)element;
          return jreFilter == null || jre.isMatch(jreFilter);
        }

        return false;
      }
    };

    dialog.setJREFilter(jreFilter);

    Object element = ((IStructuredSelection)viewer.getSelection()).getFirstElement();
    if (element instanceof JRE)
    {
      JRE jre = (JRE)element;
      dialog.setSelectedElement(jre);
    }

    if (dialog.open() == JREDialog.OK)
    {
      doSetJRE((JRE)dialog.getSelectedElement());
    }

    refresh();
  }

  public void selectionChanged(SelectionChangedEvent event)
  {
    Object element = ((IStructuredSelection)viewer.getSelection()).getFirstElement();
    if (element instanceof JRE)
    {
      doSetJRE((JRE)element);
      UIUtil.clearTextSelection(viewer);
    }
    else
    {
      doSetJRE(null);

      if (element == NO_JRE_FOUND && !refreshing)
      {
        configureJREs();
      }
    }
  }

  public void refresh()
  {
    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        refreshing = true;
        try
        {
          Object oldElement = ((IStructuredSelection)viewer.getSelection()).getFirstElement();
          JRE oldJRE = oldElement instanceof JRE ? (JRE)oldElement : jre;

          jreFilter = new JREFilter(javaVersion, bitness, null);
          Collection<JRE> jres = JREManager.INSTANCE.getJREs(jreFilter).values();

          viewer.setInput(jres);

          if (jres.isEmpty())
          {
            modelEmpty(true);
          }
          else
          {
            modelEmpty(false);

            JRE selection = getDefaultJRE(javaVersion);
            if (selection == null || !jres.contains(selection))
            {
              selection = null;
              if (oldJRE != null && jres.contains(oldJRE))
              {
                selection = oldJRE;
              }
            }

            if (selection == null || !jres.contains(selection))
            {
              selection = jres.iterator().next();
            }

            setSelection(new StructuredSelection(selection));
          }
        }
        finally
        {
          refreshing = false;
        }
      }
    });
  }

  protected void setLabel(String text)
  {
    label.setText(text);
  }

  protected void modelEmpty(boolean empty)
  {
    Control control = viewer.getControl();

    if (empty)
    {
      viewer.setInput(Collections.singletonList(NO_JRE_FOUND));
      setSelection(new StructuredSelection(NO_JRE_FOUND));
      control.setForeground(control.getDisplay().getSystemColor(SWT.COLOR_RED));
    }
    else
    {
      control.setForeground(null);
    }
  }

  private JRE getDefaultJRE(String javaVersion)
  {
    if (javaVersion != null)
    {
      String javaHome = JREManager.INSTANCE.getDefaultJRE(bitness, javaVersion);
      if (javaHome != null)
      {
        LinkedHashMap<File, JRE> jres = JREManager.INSTANCE.getJREs();
        return jres.get(new File(javaHome));
      }
    }

    return null;
  }

  private void doSetJRE(JRE jre)
  {
    this.jre = jre;

    if (jre != null)
    {
      String javaHome = jre.getJavaHome().getAbsolutePath();
      JREManager.INSTANCE.setDefaultJRE(bitness, javaVersion, javaHome);
    }
  }
}
