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
package org.eclipse.oomph.setup.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import java.io.File;

public class UnsignedContentDialog extends AbstractConfirmDialog
{
  private final String[] unsignedContent;

  public UnsignedContentDialog(String[] unsignedContent)
  {
    super("Unsigned Content", 600, 400, "Remember choice");
    this.unsignedContent = unsignedContent;
  }

  @Override
  protected String getDefaultMessage()
  {
    return "Accept unsigned content before the software can be installed.";
  }

  @Override
  protected void createUI(Composite parent)
  {
    initializeDialogUnits(parent);

    TreeViewer viewer = new TreeViewer(parent, SWT.FULL_SELECTION);
    viewer.setContentProvider(new ITreeContentProvider()
    {
      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }

      public void dispose()
      {
      }

      public Object getParent(Object element)
      {
        return null;
      }

      public Object[] getElements(Object element)
      {
        return getChildren(element);
      }

      public Object[] getChildren(Object element)
      {
        if (element == UnsignedContentDialog.this)
        {
          return unsignedContent;
        }

        return new Object[0];
      }

      public boolean hasChildren(Object element)
      {
        return element == UnsignedContentDialog.this;
      }
    });

    viewer.setLabelProvider(new UnsignedContentLabelProvider());
    viewer.setComparator(new ViewerComparator());
    viewer.setInput(this);

    Control control = viewer.getControl();
    control.setLayoutData(new GridData(GridData.FILL_BOTH));
    Dialog.applyDialogFont(control);
  }

  /**
   * @author Eike Stepper
   */
  private final class UnsignedContentLabelProvider extends LabelProvider
  {
    @Override
    public Image getImage(Object element)
    {
      File file = new File((String)element);
      if (file.isDirectory())
      {
        return SetupUIPlugin.INSTANCE.getSWTImage("unsigned-directory");
      }

      if (file.isFile())
      {
        return SetupUIPlugin.INSTANCE.getSWTImage("unsigned-file");
      }

      return null;
    }

    @Override
    public String getText(Object element)
    {
      return (String)element;
    }
  }
}
