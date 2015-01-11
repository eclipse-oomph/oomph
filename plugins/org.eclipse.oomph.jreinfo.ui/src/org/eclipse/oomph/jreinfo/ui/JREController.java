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

import java.util.Collection;
import java.util.Collections;

/**
 * @author Eike Stepper
 */
public abstract class JREController implements ISelectionChangedListener
{
  private static final String NO_JRE_FOUND = "No match; click to configure...";

  private final Label label;

  private final StructuredViewer viewer;

  private final Request.Handler downloadHandler;

  private boolean settingInput;

  private String javaVersion;

  private int bitness = JREManager.BITNESS;

  private JREFilter jreFilter;

  private JRE jre;

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

      refresh();
    }
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
      refresh();
    }
  }

  public final JRE getJRE()
  {
    return jre;
  }

  public final void setJRE(JRE jre)
  {
    this.jre = jre;
    viewer.setSelection(new StructuredSelection(jre));
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
      jre = (JRE)dialog.getSelectedElement();
    }

    refresh();
  }

  public void selectionChanged(SelectionChangedEvent event)
  {
    if (settingInput)
    {
      return;
    }

    Object element = ((IStructuredSelection)viewer.getSelection()).getFirstElement();
    if (element instanceof JRE)
    {
      jre = (JRE)element;
    }
    else
    {
      jre = null;

      if (element == NO_JRE_FOUND)
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
        if (!isModelInitialized())
        {
          return;
        }

        Object oldElement = ((IStructuredSelection)viewer.getSelection()).getFirstElement();
        JRE oldJRE = oldElement instanceof JRE ? (JRE)oldElement : jre;

        jreFilter = new JREFilter(javaVersion, bitness, null);
        Collection<JRE> jres = JREManager.INSTANCE.getJREs(jreFilter).values();

        try
        {
          settingInput = true;
          viewer.setInput(jres);

          if (jres.isEmpty())
          {
            modelEmpty(true);
          }
          else
          {
            modelEmpty(false);
            if (oldJRE != null && jres.contains(oldJRE))
            {
              viewer.setSelection(new StructuredSelection(oldJRE));
            }
            else
            {
              viewer.setSelection(new StructuredSelection(jres.iterator().next()));
            }
          }
        }
        finally
        {
          settingInput = false;
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
      viewer.setSelection(new StructuredSelection(NO_JRE_FOUND));
      control.setForeground(control.getDisplay().getSystemColor(SWT.COLOR_RED));
    }
    else
    {
      control.setForeground(null);
    }
  }

  protected abstract boolean isModelInitialized();
}
