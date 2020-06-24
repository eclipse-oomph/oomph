/*
 * Copyright (c) 2015, 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.jreinfo.ui;

import org.eclipse.oomph.internal.jreinfo.JREInfoPlugin;
import org.eclipse.oomph.jreinfo.JRE;
import org.eclipse.oomph.jreinfo.JREFilter;
import org.eclipse.oomph.jreinfo.JREManager;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.IOUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.Request;

import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Eike Stepper
 */
public class JREComposite extends Composite
{
  private static final Image IMAGE_GROUP = JREInfoUIPlugin.INSTANCE.getSWTImage("group"); //$NON-NLS-1$

  private static final Image IMAGE_JRE = JREInfoUIPlugin.INSTANCE.getSWTImage("jre"); //$NON-NLS-1$

  private static final Image IMAGE_JDK = JREInfoUIPlugin.INSTANCE.getSWTImage("jdk"); //$NON-NLS-1$

  private static final int EXTRA_WIDTH = OS.INSTANCE.isLinux() ? 10 : 0;

  private static final String[] GROUPS = { Messages.JREComposite_group_system, Messages.JREComposite_group_user };

  private static final Object[] EMPTY = new Object[0];

  private final JREContentProvider contentProvider = new JREContentProvider();

  private final Request.Handler downloadHandler;

  private Object selectedElement;

  private JREFilter filter;

  private JRE[] extras = {};

  private TreeViewer treeViewer;

  private Button browseButton;

  private Button downloadButton;

  private Button removeButton;

  private Button refreshButton;

  public JREComposite(Composite parent, int style, Request.Handler downloadHandler, JREFilter filter, final Object selection)
  {
    super(parent, style);
    this.downloadHandler = downloadHandler;
    this.filter = filter;
    selectedElement = selection;

    UIUtil.setTransparentBackgroundColor(this);
    setLayout(UIUtil.createGridLayout(2));
    TreeColumnLayout treeLayout = new TreeColumnLayout();

    Composite composite = new Composite(this, SWT.NONE);
    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    composite.setLayout(treeLayout);

    treeViewer = new TreeViewer(composite, SWT.BORDER | SWT.MULTI);
    treeViewer.setContentProvider(contentProvider);
    treeViewer.setLabelProvider(new JRELabelProvider(treeViewer.getTree().getFont()));
    treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      public void selectionChanged(SelectionChangedEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
        boolean removable = true;

        int size = selection.size();
        if (size == 0)
        {
          selectedElement = null;
          removable = false;
        }
        else if (size == 1)
        {
          selectedElement = selection.getFirstElement();
          removable = isExtraJRE(selectedElement);
        }
        else
        {
          selectedElement = null;

          for (Object element : selection.toArray())
          {
            if (!isExtraJRE(element))
            {
              removable = false;
              break;
            }
          }
        }

        removeButton.setEnabled(removable);
        elementChanged(selectedElement);
      }
    });

    treeViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        if (selectedElement instanceof JRE)
        {
          doubleClicked((JRE)selectedElement);
        }
      }
    });

    Tree tree = treeViewer.getTree();
    tree.setHeaderVisible(true);
    tree.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.keyCode == SWT.DEL)
        {
          removePressed();
        }
      }
    });

    TreeColumn locationColumn = new TreeColumn(tree, SWT.LEFT);
    locationColumn.setText(Messages.JREComposite_column_location);
    treeLayout.setColumnData(locationColumn, new ColumnWeightData(100));

    TreeColumn versionColumn = new TreeColumn(tree, SWT.LEFT);
    versionColumn.setText(Messages.JREComposite_column_version);
    treeLayout.setColumnData(versionColumn, new ColumnWeightData(1, 60 + EXTRA_WIDTH));

    TreeColumn bitnessColumn = new TreeColumn(tree, SWT.LEFT);
    bitnessColumn.setText(Messages.JREComposite_column_bitness);
    treeLayout.setColumnData(bitnessColumn, new ColumnWeightData(1, 60 + EXTRA_WIDTH));

    TreeColumn typeColumn = new TreeColumn(tree, SWT.LEFT);
    typeColumn.setText(Messages.JREComposite_column_type);
    treeLayout.setColumnData(typeColumn, new ColumnWeightData(1, 50 + EXTRA_WIDTH));

    Composite buttonComposite = new Composite(this, SWT.NONE);
    buttonComposite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));
    buttonComposite.setLayout(UIUtil.createGridLayout(1));

    browseButton = new Button(buttonComposite, SWT.NONE);
    browseButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    browseButton.setText(Messages.JREComposite_button_browse);
    browseButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        browsePressed();
      }
    });

    if (downloadHandler != null)
    {
      downloadButton = new Button(buttonComposite, SWT.NONE);
      downloadButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
      downloadButton.setText(Messages.JREComposite_button_download);
      downloadButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          downloadPressed();
        }
      });
    }

    removeButton = new Button(buttonComposite, SWT.NONE);
    removeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    removeButton.setText(Messages.JREComposite_button_remove);
    removeButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        removePressed();
      }
    });

    refreshButton = new Button(buttonComposite, SWT.NONE);
    refreshButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    refreshButton.setText(Messages.JREComposite_button_refresh);
    refreshButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        refreshPressed();
      }
    });

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        BusyIndicator.showWhile(getShell().getDisplay(), new Runnable()
        {
          public void run()
          {
            treeViewer.setInput(JREManager.INSTANCE);
            treeViewer.expandAll();
          }
        });

        if (selectedElement == null)
        {
          removeButton.setEnabled(false);
        }
        else
        {
          treeViewer.setSelection(new StructuredSelection(selectedElement));
        }
      }
    });
  }

  public JREFilter getJREFilter()
  {
    return filter;
  }

  public void setJREFilter(JREFilter filter)
  {
    this.filter = filter;

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        treeViewer.refresh();
      }
    });
  }

  public Object getSelectedElement()
  {
    return selectedElement;
  }

  public void setSelectedElement(final Object selectedElement)
  {
    this.selectedElement = selectedElement;

    UIUtil.asyncExec(new Runnable()
    {
      public void run()
      {
        treeViewer.setSelection(new StructuredSelection(selectedElement));
        elementChanged(selectedElement);
      }
    });
  }

  protected void elementChanged(Object element)
  {
  }

  protected void browsePressed()
  {
    File rootFolder = null;

    File file = new File(JREInfoPlugin.INSTANCE.getUserLocation().append("search.txt").toOSString()); //$NON-NLS-1$
    List<String> lines = IOUtil.readLines(file, "UTF-8"); //$NON-NLS-1$
    if (!lines.isEmpty())
    {
      rootFolder = new File(lines.get(0));
    }

    while (rootFolder != null && !rootFolder.isDirectory())
    {
      rootFolder = rootFolder.getParentFile();
    }

    String filterPath = rootFolder == null ? PropertiesUtil.getUserHome() : rootFolder.getAbsolutePath();

    DirectoryDialog dialog = new DirectoryDialog(getShell());
    dialog.setText(JREDialog.TITLE);
    dialog.setMessage(Messages.JREComposite_browseDialog_pickRootFolder);
    dialog.setFilterPath(filterPath);

    final String dir = dialog.open();
    if (dir != null)
    {
      if (!dir.equals(filterPath))
      {
        IOUtil.writeLines(file, "UTF-8", Collections.singletonList(dir)); //$NON-NLS-1$
      }

      try
      {
        final AtomicReference<Set<JRE>> added = new AtomicReference<Set<JRE>>();

        UIUtil.runInProgressDialog(getShell(), new IRunnableWithProgress()
        {
          public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
          {
            added.set(JREManager.INSTANCE.addExtraJavaHomes(dir, monitor));
          }
        });

        Set<JRE> jres = added.get();
        int size = jres == null ? 0 : jres.size();
        if (size != 0)
        {
          treeViewer.refresh();
          treeViewer.setExpandedState(GROUPS[1], true);
          treeViewer.setSelection(new StructuredSelection(new ArrayList<JRE>(jres)));
        }

        MessageDialog.openInformation(getShell(), JREDialog.TITLE, getVMFoundMessage(size));
      }
      catch (InvocationTargetException ex)
      {
        Throwable cause = ex.getCause();
        if (cause == null)
        {
          cause = ex;
        }

        JREInfoPlugin.INSTANCE.log(cause);
        ErrorDialog.open(cause);
      }
      catch (InterruptedException ex)
      {
        //$FALL-THROUGH$
      }
    }
  }

  protected void downloadPressed()
  {
    Request request = new Request("http://download.eclipse.org/oomph/jre/"); //$NON-NLS-1$

    JREFilter filter = getJREFilter();
    if (filter != null)
    {
      request.put("vm", filter.getQuery()); //$NON-NLS-1$
    }

    downloadHandler.handleRequest(request);
  }

  protected void removePressed()
  {
    Set<String> javaHomes = new HashSet<String>();

    IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
    for (Object element : selection.toArray())
    {
      if (isExtraJRE(element))
      {
        javaHomes.add(((JRE)element).getJavaHome().getAbsolutePath());
      }
    }

    int size = javaHomes.size();
    if (size != 0)
    {
      JREManager.INSTANCE.removeExtraJavaHomes(javaHomes.toArray(new String[size]));
      treeViewer.refresh();
      treeViewer.setSelection(StructuredSelection.EMPTY);
    }
  }

  protected void refreshPressed()
  {
    JREManager.INSTANCE.refresh(true);
    treeViewer.refresh();
  }

  protected void doubleClicked(JRE jre)
  {
    // Do nothing.
  }

  private String getVMFoundMessage(int count)
  {
    switch (count)
    {
      case 0:
        return Messages.JREComposite_browseDialog_noNewVmsFound;
      case 1:
        return Messages.JREComposite_browseDialog_oneNewVmFound;
      default:
        return NLS.bind(Messages.JREComposite_browseDialog_newVmsFound, Integer.toString(count));
    }
  }

  private boolean isExtraJRE(Object jre)
  {
    for (int i = 0; i < extras.length; i++)
    {
      if (extras[i] == jre)
      {
        return true;
      }
    }

    return false;
  }

  /**
   * @author Eike Stepper
   */
  private final class JREContentProvider implements ITreeContentProvider
  {
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    public Object[] getChildren(Object element)
    {
      if (element == JREManager.INSTANCE)
      {
        return GROUPS;
      }

      if (element == GROUPS[0])
      {
        return JREManager.INSTANCE.getJREs(null, false);
      }

      if (element == GROUPS[1])
      {
        extras = JREManager.INSTANCE.getJREs(null, true);
        return extras;
      }

      return EMPTY;
    }

    public boolean hasChildren(Object element)
    {
      return getChildren(element).length != 0;
    }

    public Object[] getElements(Object element)
    {
      return getChildren(element);
    }

    public Object getParent(Object element)
    {
      return null;
    }

    public void dispose()
    {
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class JRELabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider, ITableFontProvider
  {
    private Font font;

    public JRELabelProvider(Font font)
    {
      this.font = font;
    }

    public Image getColumnImage(Object element, int columnIndex)
    {
      if (columnIndex == 0)
      {
        if (element instanceof String)
        {
          return IMAGE_GROUP;
        }

        if (element instanceof JRE)
        {
          return ((JRE)element).isJDK() ? IMAGE_JDK : IMAGE_JRE;
        }
      }

      return null;
    }

    public String getColumnText(Object element, int columnIndex)
    {
      if (element instanceof String)
      {
        if (columnIndex == 0)
        {
          return (String)element;
        }

        return null;
      }

      if (element instanceof JRE)
      {
        JRE jre = (JRE)element;

        switch (columnIndex)
        {
          case 0:
            return jre.toString();
          case 1:
            return jre.getMajor() + "." + jre.getMinor() + "." + jre.getMicro(); //$NON-NLS-1$ //$NON-NLS-2$
          case 2:
            return jre.getBitness() + ' ' + Messages.JREComposite_bit;
          case 3:
            return jre.isJDK() ? "JDK" : "JRE"; //$NON-NLS-1$ //$NON-NLS-2$
        }
      }

      return null;
    }

    public Color getForeground(Object element)
    {
      if (filter != null && element instanceof JRE)
      {
        JRE jre = (JRE)element;
        if (!jre.isMatch(filter))
        {
          return getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
        }
      }

      return null;
    }

    public Color getBackground(Object element)
    {
      return null;
    }

    public Font getFont(Object element, int columnIndex)
    {
      if (element instanceof JRE)
      {
        JRE jre = (JRE)element;
        if (jre.isCurrent())
        {
          return ExtendedFontRegistry.INSTANCE.getFont(font, IItemFontProvider.BOLD_FONT);
        }
      }

      return null;
    }
  }
}
