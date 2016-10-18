/*
 * Copyright (c) 2015, 2016 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.setup.presentation;

import org.eclipse.oomph.internal.ui.UIPlugin;
import org.eclipse.oomph.preferences.PreferenceNode;
import org.eclipse.oomph.preferences.PreferencesFactory;
import org.eclipse.oomph.preferences.Property;
import org.eclipse.oomph.preferences.util.PreferencesUtil;
import org.eclipse.oomph.setup.CompoundTask;
import org.eclipse.oomph.setup.PreferenceTask;
import org.eclipse.oomph.setup.SetupTask;
import org.eclipse.oomph.setup.ui.recorder.RecorderTransaction;
import org.eclipse.oomph.util.IORuntimeException;
import org.eclipse.oomph.util.Pair;
import org.eclipse.oomph.util.PropertiesUtil;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.AbstractTreeIterator;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Ed Merks
 */
public class PreferenceCaptureDialog extends Dialog
{
  private static final URI INSTANCE_URI = URI.createURI("//" + PreferencesUtil.INSTANCE_NODE);

  protected ILabelProvider labelProvider;

  protected FilteringAdapterFactoryContentProvider availablePreferencesContentProvider;

  protected FilteringAdapterFactoryContentProvider selectedPreferencesContentProvider;

  protected Set<Object> included = new HashSet<Object>();

  protected boolean fromEclipsePreferenceFile;

  /**
   * @since 2.6
   */
  public PreferenceCaptureDialog(Shell parent, AdapterFactory adapterFactory, boolean fromEclipsePreferenceFile)
  {
    super(parent);

    setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);

    this.fromEclipsePreferenceFile = fromEclipsePreferenceFile;

    labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
    availablePreferencesContentProvider = new FilteringAdapterFactoryContentProvider(adapterFactory, false);
    selectedPreferencesContentProvider = new FilteringAdapterFactoryContentProvider(adapterFactory, true);
  }

  @Override
  protected void configureShell(Shell shell)
  {
    super.configureShell(shell);
    shell.setText(fromEclipsePreferenceFile ? "Import Preferences" : "Capture Preferences");
    shell.setImage(SetupEditorPlugin.INSTANCE.getSWTImage(fromEclipsePreferenceFile ? "preference_importer" : "preference_picker"));
  }

  @Override
  protected IDialogSettings getDialogBoundsSettings()
  {
    return SetupEditorPlugin.INSTANCE.getDialogSettings(fromEclipsePreferenceFile ? "PreferenceImport" : "PreferenceCapture");
  }

  protected List<?> getAvailablePreferences()
  {
    PreferenceNode rootPreferenceNode = PreferencesUtil.getRootPreferenceNode(Collections.unmodifiableSet(new LinkedHashSet<String>(
        Arrays.asList(new String[] { PreferencesUtil.BUNDLE_DEFAULTS_NODE, PreferencesUtil.DEFAULT_NODE, PreferencesUtil.INSTANCE_NODE }))), false);

    Map<URI, Pair<String, String>> preferences = new HashMap<URI, Pair<String, String>>();
    for (PreferenceNode preferenceNode : rootPreferenceNode.getChildren())
    {
      traverse(preferences, preferenceNode);
    }

    return RecorderTransaction.record(preferences);
  }

  protected void traverse(Map<URI, Pair<String, String>> preferences, PreferenceNode preferenceNode)
  {
    for (PreferenceNode childPreferenceNode : preferenceNode.getChildren())
    {
      traverse(preferences, childPreferenceNode);
    }

    for (Property property : preferenceNode.getProperties())
    {
      preferences.put(INSTANCE_URI.appendSegments(property.getRelativePath().segments()), new Pair<String, String>(null, property.getValue()));
    }
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    final ItemProvider input = new ItemProvider();

    Composite contents = (Composite)super.createDialogArea(parent);

    GridLayout contentsGridLayout = (GridLayout)contents.getLayout();
    contentsGridLayout.numColumns = 3;

    GridData contentsGridData = (GridData)contents.getLayoutData();
    contentsGridData.horizontalAlignment = SWT.FILL;
    contentsGridData.verticalAlignment = SWT.FILL;

    Text preferenceFileText = null;

    if (fromEclipsePreferenceFile)
    {
      Group preferenceFileGroup = new Group(contents, SWT.NONE);
      preferenceFileGroup.setText("Eclipse Preference File");
      preferenceFileGroup.setLayout(new GridLayout(2, false));
      preferenceFileGroup.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 3, 1));

      final Text finalPreferenceFileText = preferenceFileText = new Text(preferenceFileGroup, SWT.BORDER);
      preferenceFileText.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 1, 1));

      Button preferenceFileBrowseButton = new Button(preferenceFileGroup, SWT.PUSH);
      preferenceFileBrowseButton.setText("Browse...");
      preferenceFileBrowseButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          FileDialog dialog = new FileDialog(getShell(), SWT.OPEN | SWT.MULTI);
          dialog.setFilterExtensions(new String[] { "*.epf" });
          String result = dialog.open();
          if (result != null)
          {
            finalPreferenceFileText.setText(result);
          }
        }
      });
    }

    Group filterGroupComposite = new Group(contents, SWT.NONE);
    filterGroupComposite.setText("Filter Available Preferences");
    filterGroupComposite.setLayout(new GridLayout(1, false));
    filterGroupComposite.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 3, 1));

    Composite availablePreferencesComposite = new Composite(contents, SWT.NONE);
    {
      GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
      data.horizontalAlignment = SWT.END;
      availablePreferencesComposite.setLayoutData(data);

      GridLayout layout = new GridLayout();
      data.horizontalAlignment = SWT.FILL;
      layout.marginHeight = 0;
      layout.marginWidth = 0;
      layout.numColumns = 1;
      availablePreferencesComposite.setLayout(layout);
    }

    Label availablePreferencesLabel = new Label(availablePreferencesComposite, SWT.NONE);
    availablePreferencesLabel.setText("Available Preferences");
    GridData availablePreferencesLabelGridData = new GridData();
    availablePreferencesLabelGridData.verticalAlignment = SWT.FILL;
    availablePreferencesLabelGridData.horizontalAlignment = SWT.FILL;
    availablePreferencesLabel.setLayoutData(availablePreferencesLabelGridData);

    PatternFilter filter = new PatternFilter();
    filter.setIncludeLeadingWildcard(true);
    final FilteredTree availablePreferencesTree = new FilteredTree(availablePreferencesComposite, SWT.MULTI | SWT.BORDER, filter, true);
    GridData availablePreferencesTreeGridData = new GridData();
    availablePreferencesTreeGridData.widthHint = Display.getCurrent().getBounds().width / 5;
    availablePreferencesTreeGridData.heightHint = Display.getCurrent().getBounds().height / 3;
    availablePreferencesTreeGridData.verticalAlignment = SWT.FILL;
    availablePreferencesTreeGridData.horizontalAlignment = SWT.FILL;
    availablePreferencesTreeGridData.grabExcessHorizontalSpace = true;
    availablePreferencesTreeGridData.grabExcessVerticalSpace = true;
    availablePreferencesTree.setLayoutData(availablePreferencesTreeGridData);

    Control filterControl = availablePreferencesTree.getChildren()[0];
    filterControl.setParent(filterGroupComposite);
    filterControl.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    final TreeViewer availablePreferencesTreeViewer = availablePreferencesTree.getViewer();
    availablePreferencesTreeViewer.setContentProvider(availablePreferencesContentProvider);
    availablePreferencesTreeViewer.setLabelProvider(labelProvider);
    if (!fromEclipsePreferenceFile)
    {
      input.getChildren().addAll(getAvailablePreferences());
    }

    availablePreferencesTreeViewer.setInput(input);

    Composite controlButtons = new Composite(contents, SWT.NONE);
    GridData controlButtonsGridData = new GridData();
    controlButtonsGridData.verticalAlignment = SWT.FILL;
    controlButtonsGridData.horizontalAlignment = SWT.FILL;
    controlButtons.setLayoutData(controlButtonsGridData);

    GridLayout controlsButtonGridLayout = new GridLayout();
    controlButtons.setLayout(controlsButtonGridLayout);

    new Label(controlButtons, SWT.NONE);

    final Button addButton = new Button(controlButtons, SWT.PUSH);
    addButton.setText("Add");
    GridData addButtonGridData = new GridData();
    addButtonGridData.verticalAlignment = SWT.FILL;
    addButtonGridData.horizontalAlignment = SWT.FILL;
    addButton.setLayoutData(addButtonGridData);

    final Button removeButton = new Button(controlButtons, SWT.PUSH);
    removeButton.setText("Remove");
    GridData removeButtonGridData = new GridData();
    removeButtonGridData.verticalAlignment = SWT.FILL;
    removeButtonGridData.horizontalAlignment = SWT.FILL;
    removeButton.setLayoutData(removeButtonGridData);

    Label spaceLabel = new Label(controlButtons, SWT.NONE);
    GridData spaceLabelGridData = new GridData();
    spaceLabelGridData.verticalSpan = 2;
    spaceLabel.setLayoutData(spaceLabelGridData);

    Composite selectedPreferencesComposite = new Composite(contents, SWT.NONE);
    {
      GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
      data.horizontalAlignment = SWT.END;
      selectedPreferencesComposite.setLayoutData(data);

      GridLayout layout = new GridLayout();
      data.horizontalAlignment = SWT.FILL;
      layout.marginHeight = 0;
      layout.marginWidth = 0;
      layout.numColumns = 1;
      selectedPreferencesComposite.setLayout(layout);
    }

    Label selectedPreferencesLabel = new Label(selectedPreferencesComposite, SWT.NONE);
    selectedPreferencesLabel.setText("Selected Preferences");
    GridData selectedPreferencesLabelGridData = new GridData();
    selectedPreferencesLabelGridData.horizontalSpan = 2;
    selectedPreferencesLabelGridData.horizontalAlignment = SWT.FILL;
    selectedPreferencesLabelGridData.verticalAlignment = SWT.FILL;
    selectedPreferencesLabel.setLayoutData(selectedPreferencesLabelGridData);

    final Tree selectedPreferencesTree = new Tree(selectedPreferencesComposite, SWT.MULTI | SWT.BORDER);
    GridData selectedPreferencesTreeGridData = new GridData();
    selectedPreferencesTreeGridData.widthHint = Display.getCurrent().getBounds().width / 5;
    selectedPreferencesTreeGridData.heightHint = Display.getCurrent().getBounds().height / 3;
    selectedPreferencesTreeGridData.verticalAlignment = SWT.FILL;
    selectedPreferencesTreeGridData.horizontalAlignment = SWT.FILL;
    selectedPreferencesTreeGridData.grabExcessHorizontalSpace = true;
    selectedPreferencesTreeGridData.grabExcessVerticalSpace = true;
    selectedPreferencesTree.setLayoutData(selectedPreferencesTreeGridData);

    final TreeViewer selectedPreferencesTreeViewer = new TreeViewer(selectedPreferencesTree);
    selectedPreferencesTreeViewer.setContentProvider(selectedPreferencesContentProvider);
    selectedPreferencesTreeViewer.setLabelProvider(labelProvider);
    selectedPreferencesTreeViewer.setInput(input);

    availablePreferencesTreeViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        if (addButton.isEnabled())
        {
          addButton.notifyListeners(SWT.Selection, null);
        }
      }
    });

    selectedPreferencesTreeViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      public void doubleClick(DoubleClickEvent event)
      {
        if (removeButton.isEnabled())
        {
          removeButton.notifyListeners(SWT.Selection, null);
        }
      }
    });

    addButton.addSelectionListener(new FilteringSelectionAdapter(selectedPreferencesTreeViewer, availablePreferencesTreeViewer, input, true));
    removeButton.addSelectionListener(new FilteringSelectionAdapter(selectedPreferencesTreeViewer, availablePreferencesTreeViewer, input, false));

    if (preferenceFileText != null)
    {
      preferenceFileText.addModifyListener(new ModifyListener()
      {
        public void modifyText(ModifyEvent e)
        {
          included.clear();
          EList<Object> children = input.getChildren();
          children.clear();

          try
          {
            Map<URI, Pair<String, String>> preferences = new HashMap<URI, Pair<String, String>>();
            Map<String, String> loadProperties = PropertiesUtil.loadProperties(new File(((Text)e.widget).getText()));
            for (Map.Entry<String, String> entry : loadProperties.entrySet())
            {
              String key = entry.getKey();
              if (key.startsWith("/"))
              {
                preferences.put(PreferencesFactory.eINSTANCE.createURI(key), new Pair<String, String>(null, entry.getValue()));
              }
            }

            List<SetupTask> availablePreferences = RecorderTransaction.record(preferences);
            children.addAll(availablePreferences);
          }
          catch (IORuntimeException ex)
          {
            children.add(new ItemProvider(ex.getMessage(), UIPlugin.INSTANCE.getImage("error")));
          }

          availablePreferencesTreeViewer.refresh();
          selectedPreferencesTreeViewer.refresh();
        }
      });
    }

    return contents;
  }

  @Override
  public boolean close()
  {
    availablePreferencesContentProvider.dispose();
    selectedPreferencesContentProvider.dispose();
    return super.close();
  }

  public Map<URI, Pair<String, String>> getResult()
  {
    Map<URI, Pair<String, String>> result = new HashMap<URI, Pair<String, String>>();
    for (Object object : included)
    {
      PreferenceTask preferenceTask = (PreferenceTask)object;
      result.put(PreferencesFactory.eINSTANCE.createURI(preferenceTask.getKey()), new Pair<String, String>(null, preferenceTask.getValue()));
    }

    return result;
  }

  /**
   * @author Ed Merks
   */
  private class FilteringSelectionAdapter extends SelectionAdapter
  {
    private final TreeViewer selectedPreferencesTreeViewer;

    private final TreeViewer availablePreferencesTreeViewer;

    private final ItemProvider input;

    private boolean add;

    private FilteringSelectionAdapter(TreeViewer selectedPreferencesTreeViewer, TreeViewer availablePreferencesTreeViewer, ItemProvider input, boolean add)
    {
      this.selectedPreferencesTreeViewer = selectedPreferencesTreeViewer;
      this.availablePreferencesTreeViewer = availablePreferencesTreeViewer;
      this.input = input;
      this.add = add;
    }

    @Override
    public void widgetSelected(SelectionEvent event)
    {
      IStructuredSelection selection = (IStructuredSelection)(add ? availablePreferencesTreeViewer : selectedPreferencesTreeViewer).getSelection();
      List<Object> newSelection = new ArrayList<Object>();

      for (Object object : selection.toArray())
      {
        Object[] children = (add ? availablePreferencesContentProvider : selectedPreferencesContentProvider).getChildren(object);
        if (children.length != 0)
        {
          List<Object> choices = Arrays.asList(children);
          newSelection.addAll(choices);
        }
        else
        {
          newSelection.add(object);
        }
      }

      Set<Object> nonCandidates = new HashSet<Object>(newSelection);
      Object candidate = null;
      Object bestCandidate = null;

      for (TreeIterator<Object> it = (add ? availablePreferencesContentProvider : selectedPreferencesContentProvider).getAllContents(input, false); it
          .hasNext();)
      {
        candidate = it.next();
        if (nonCandidates.remove(candidate))
        {
          if (!(bestCandidate instanceof PreferenceTask))
          {
            bestCandidate = null;
          }
        }
        else
        {
          if (!(candidate instanceof CompoundTask))
          {
            bestCandidate = candidate;
          }

          if (nonCandidates.isEmpty())
          {
            break;
          }
        }
      }

      if (add)
      {
        included.addAll(newSelection);
      }
      else
      {
        included.removeAll(newSelection);
      }

      for (Object object : input.getChildren())
      {
        if (object instanceof CompoundTask)
        {
          CompoundTask compoundTask = (CompoundTask)object;
          EList<SetupTask> setupTasks = compoundTask.getSetupTasks();
          if (newSelection.containsAll(setupTasks))
          {
            newSelection.removeAll(setupTasks);
            newSelection.add(compoundTask);
          }
        }
      }

      selectedPreferencesTreeViewer.refresh();
      availablePreferencesTreeViewer.refresh();

      (add ? selectedPreferencesTreeViewer : availablePreferencesTreeViewer).setSelection(new StructuredSelection(newSelection));

      if (bestCandidate == null && candidate != null)
      {
        bestCandidate = candidate;
      }

      if (bestCandidate != null)
      {
        (add ? availablePreferencesTreeViewer : selectedPreferencesTreeViewer).setSelection(new StructuredSelection(bestCandidate));
      }
    }
  }

  /**
   * @author Ed Merks
   */
  private final class FilteringAdapterFactoryContentProvider extends AdapterFactoryContentProvider
  {
    private boolean include;

    private FilteringAdapterFactoryContentProvider(AdapterFactory adapterFactory, boolean include)
    {
      super(adapterFactory);
      this.include = include;
    }

    public TreeIterator<Object> getAllContents(Object object, boolean includeRoot)
    {
      return new AbstractTreeIterator<Object>(object, includeRoot)
      {
        private static final long serialVersionUID = 1L;

        @Override
        protected Iterator<? extends Object> getChildren(Object object)
        {
          return Arrays.asList(object instanceof ItemProvider ? FilteringAdapterFactoryContentProvider.this.getElements(object)
              : FilteringAdapterFactoryContentProvider.this.getChildren(object)).iterator();
        }
      };
    }

    @Override
    public Object[] getElements(Object object)
    {
      List<Object> elements = ((ItemProvider)object).getChildren();
      List<Object> result = new ArrayList<Object>();

      for (Object element : elements)
      {
        if (include)
        {
          for (Object child : getChildren(element))
          {
            if (included.contains(child))
            {
              result.add(element);
              break;
            }
          }
        }
        else if (element instanceof ItemProvider)
        {
          result.add(element);
        }
        else if (!included.containsAll(Arrays.asList(getChildren(element))))
        {
          result.add(element);
        }
      }

      return result.toArray();
    }

    @Override
    public Object[] getChildren(Object object)
    {
      Object[] children = super.getChildren(object);
      List<Object> result = new ArrayList<Object>();

      for (Object child : children)
      {
        if (included.contains(child) == include)
        {
          result.add(child);
        }
      }

      return result.toArray();
    }
  }
}
