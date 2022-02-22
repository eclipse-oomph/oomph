/*
 * Copyright (c) 2014, 2016 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.preferences.presentation;

import org.eclipse.oomph.internal.ui.AbstractPreferencePage;
import org.eclipse.oomph.preferences.provider.PreferencesItemProviderAdapterFactory;
import org.eclipse.oomph.preferences.util.PreferencesUtil;

import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;

import java.lang.reflect.Method;

/**
 * @author Eike Stepper
 */
public class AllPreferencesPreferencePage extends AbstractPreferencePage
{
  private IWorkbench workbench;

  public AllPreferencesPreferencePage()
  {
    noDefaultAndApplyButton();
  }

  @Override
  public void init(IWorkbench workbench)
  {
    this.workbench = workbench;
  }

  @Override
  protected Control doCreateContents(Composite parent)
  {
    GridLayout layout = new GridLayout();
    layout.marginWidth = 0;
    layout.marginHeight = 0;
    layout.numColumns = 1;

    Composite composite = new Composite(parent, SWT.NONE);
    composite.setLayout(layout);

    TreeViewer treeViewer = new TreeViewer(composite);
    PreferencesItemProviderAdapterFactory adapterFactory = new PreferencesItemProviderAdapterFactory();
    treeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
    treeViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
    treeViewer.setInput(PreferencesUtil.getRootPreferenceNode());
    treeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

    return composite;
  }

  @Override
  protected void contributeButtons(Composite parent)
  {
    super.contributeButtons(parent);

    GridLayout gridLayout = (GridLayout)parent.getLayout();
    gridLayout.numColumns += 1;

    Button editButton = new Button(parent, SWT.PUSH);
    editButton.setText(Messages.AllPreferencesPreferencePage_Edit_label);

    Dialog.applyDialogFont(editButton);
    int widthHint = convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
    Point minButtonSize = editButton.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
    GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
    data.widthHint = Math.max(widthHint, minButtonSize.x);

    editButton.setLayoutData(data);
    editButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        // Invoke the close method on the preference dialog, but avoid using internal API, so do it reflectively.
        IPreferencePageContainer container = getContainer();

        try
        {
          Method method = container.getClass().getMethod("close"); //$NON-NLS-1$
          method.invoke(container);
        }
        catch (Throwable ex)
        {
          PreferencesEditorPlugin.INSTANCE.log(ex);
        }

        openWorkingSetsEditor();
      }
    });

  }

  protected void openWorkingSetsEditor()
  {
    final IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
    Display display = activeWorkbenchWindow.getShell().getDisplay();
    display.asyncExec(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          IEditorInput editorInput = new URIEditorInput(PreferencesUtil.ROOT_PREFERENCE_NODE_URI.trimSegments(1).appendSegment("All.preferences"), //$NON-NLS-1$
              Messages.AllPreferencesPreferencePage_AllPreferences_title);
          IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
          activePage.openEditor(editorInput, "org.eclipse.oomph.preferences.presentation.PreferencesEditorID"); //$NON-NLS-1$
          activePage.showView(IPageLayout.ID_PROP_SHEET);
        }
        catch (Exception ex)
        {
          PreferencesEditorPlugin.INSTANCE.log(ex);
        }
      }
    });
  }
}
