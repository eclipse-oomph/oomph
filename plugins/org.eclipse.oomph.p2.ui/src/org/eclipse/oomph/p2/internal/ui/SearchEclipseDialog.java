/*
 * Copyright (c) 2018 Ed Merks (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Ed Merks - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.internal.ui.GeneralDragAdapter;
import org.eclipse.oomph.internal.ui.OomphTransferDelegate;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.P2Package;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.p2.internal.core.P2Index;
import org.eclipse.oomph.p2.internal.core.P2Index.Repository;
import org.eclipse.oomph.p2.provider.P2EditPlugin;
import org.eclipse.oomph.ui.DockableDialog;
import org.eclipse.oomph.ui.DockableDialog.Factory;
import org.eclipse.oomph.ui.FilteredTreeWithoutWorkbench;
import org.eclipse.oomph.ui.OomphDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.CollectionUtil;
import org.eclipse.oomph.util.OS;
import org.eclipse.oomph.util.StringUtil;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.ui.ImageURIRegistry;
import org.eclipse.emf.common.ui.viewer.ColumnViewerInformationControlToolTipSupport;
import org.eclipse.emf.common.ui.viewer.IStyledLabelDecorator;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.SegmentSequence;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.DecoratingColumLabelProvider;
import org.eclipse.emf.edit.ui.provider.DiagnosticDecorator;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.IPageListener;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.PatternFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ed Merks
 */
public abstract class SearchEclipseDialog extends OomphDialog
{
  protected static final int APPLY_ID = IDialogConstants.CLIENT_ID;

  protected final WorkbenchWindowSelectionTracker workbenchWindowSelectionTracker;

  private final DockableDialog.Dockable dockable;

  private TreeViewer capabilitiesViewer;

  protected TreeViewer detailsViewer;

  private Job detailsLoadJob;

  private Text filterText;

  private String initialFilterString;

  protected SearchEclipseDialog(IWorkbenchWindow workbenchWindow, String title)
  {
    super(workbenchWindow.getShell(), title, 700, 500, P2UIPlugin.INSTANCE, true);
    setShellStyle(getShellStyle() ^ SWT.APPLICATION_MODAL | SWT.MODELESS | SWT.RESIZE | SWT.MAX | (OS.INSTANCE.isWin() ? SWT.MIN : SWT.NONE));
    setBlockOnOpen(false);
    dockable = new DockableDialog.Dockable(this);
    workbenchWindowSelectionTracker = new WorkbenchWindowSelectionTracker(workbenchWindow)
    {
      @Override
      protected void selectionChanged(IWorkbenchPart part, ISelection selection)
      {
        SearchEclipseDialog.this.selectionChanged(part, selection);
      }
    };
  }

  public DockableDialog.Dockable getDockable()
  {
    return dockable;
  }

  public void setInitialFilterString(String initialFilterString)
  {
    this.initialFilterString = initialFilterString;
  }

  protected abstract void setSelected(Item item);

  protected abstract void handleDetailsLoad(Item capabilityItem);

  protected abstract void handleDetailsDoubleClick();

  protected abstract int getDetailsAutoExpandLevel();

  protected abstract void selectionChanged(IWorkbenchPart part, ISelection selection);

  protected abstract Image getShellImage();

  @Override
  protected String getImagePath()
  {
    return "wizban/AgentManager.png"; //$NON-NLS-1$
  }

  @Override
  protected int getContainerMargin()
  {
    return 10;
  }

  protected String getFilterString(Requirement requirement)
  {
    String namespace = requirement.getNamespace();
    String name = requirement.getName();
    StringBuilder filterString = new StringBuilder();
    if (!StringUtil.isEmpty(namespace))
    {
      filterString.append(namespace).append('/');
    }

    if (!StringUtil.isEmpty(name))
    {
      filterString.append(name);
    }

    return filterString.toString();
  }

  @Override
  protected void createUI(Composite composite)
  {
    getShell().setImage(getShellImage());

    SashForm sashForm = new SashForm(composite, SWT.SMOOTH | SWT.VERTICAL);
    sashForm.setLayout(new GridLayout());
    sashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

    Composite treeComposite = new Composite(sashForm, SWT.NONE);
    treeComposite.setLayout(UIUtil.createGridLayout(1));

    Composite filterComposite = new Composite(treeComposite, SWT.NONE);
    filterComposite.setLayout(UIUtil.createGridLayout(2));
    filterComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    Composite filterPlaceholder = new Composite(filterComposite, SWT.NONE);
    filterPlaceholder.setLayout(UIUtil.createGridLayout(1));
    filterPlaceholder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    ToolBar filterToolBar = new ToolBar(filterComposite, SWT.FLAT | SWT.RIGHT);

    final ToolItem collapseAllButton = new ToolItem(filterToolBar, SWT.NONE);
    collapseAllButton.setToolTipText(Messages.SearchEclipseDialog_collapseAllButton_tooltip);
    collapseAllButton.setImage(P2UIPlugin.INSTANCE.getSWTImage("collapse-all")); //$NON-NLS-1$
    collapseAllButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        capabilitiesViewer.collapseAll();
      }
    });

    final ItemFilter filter = new ItemFilter();
    filter.setIncludeLeadingWildcard(true);

    final FilteredTreeWithoutWorkbench filteredTree = new FilteredTreeWithoutWorkbench(treeComposite, SWT.BORDER, filter, filter);
    filteredTree.setExpansionCount(100);

    Control filterControl = filteredTree.getChildren()[0];
    filterControl.setParent(filterPlaceholder);
    filterText = filteredTree.getFilterControl();
    DropTarget dropTarget = new DropTarget(filterText, DND.DROP_COPY);
    dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
    dropTarget.addDropListener(new DropTargetAdapter()
    {
      @Override
      public void dragEnter(DropTargetEvent event)
      {
        event.detail = DND.DROP_COPY;
        event.feedback = DND.FEEDBACK_NONE;
      }

      @Override
      public void dragOver(DropTargetEvent event)
      {
        event.detail = DND.DROP_COPY;
        event.feedback = DND.FEEDBACK_NONE;
      }

      @Override
      public void dragOperationChanged(DropTargetEvent event)
      {
        event.detail = DND.DROP_COPY;
        event.feedback = DND.FEEDBACK_NONE;
      }

      @Override
      public void drop(final DropTargetEvent event)
      {
        if (TextTransfer.getInstance().isSupportedType(event.currentDataType))
        {
          String text = (String)event.data;
          AdapterFactory adapterFactory = new ComposedAdapterFactory();
          EditingDomain domain = new AdapterFactoryEditingDomain(adapterFactory, null);
          Collection<?> values = new OomphTransferDelegate.TextTransferDelegate().getValue(domain, text);
          if (!values.isEmpty())
          {
            text = null;
            for (Object value : values)
            {
              if (value instanceof Requirement)
              {
                text = getFilterString((Requirement)value);
                break;
              }
            }
          }

          if (!StringUtil.isEmpty(text))
          {
            filterText.setText(text);
            filterText.setSelection(filterText.getText().length());
          }
        }
      }
    });

    final AdapterFactory adapterFactory = new ComposedAdapterFactory();
    capabilitiesViewer = filteredTree.getViewer();
    capabilitiesViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new ToolTipLabelProvider(adapterFactory, capabilitiesViewer, filter)));
    capabilitiesViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
    capabilitiesViewer.setUseHashlookup(true);

    new ColumnViewerInformationControlToolTipSupport(capabilitiesViewer, null);

    final Tree capabilitiesTree = capabilitiesViewer.getTree();
    capabilitiesTree.setLayoutData(new GridData(GridData.FILL_BOTH));

    capabilitiesViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      @Override
      public void doubleClick(DoubleClickEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)capabilitiesViewer.getSelection();
        Item item = (Item)selection.getFirstElement();
        boolean expanded = capabilitiesViewer.getExpandedState(item);
        capabilitiesViewer.setExpandedState(item, !expanded);

        if (!item.isConcrete())
        {
          while (item != null)
          {
            for (Item child : item.getItems())
            {
              if (filter.isElementVisible(capabilitiesViewer, child))
              {
                if (child.isConcrete())
                {
                  capabilitiesViewer.setSelection(new StructuredSelection(child), true);
                  item = null;
                  break;
                }

                capabilitiesViewer.setExpandedState(child, true);
                item = child;
                break;
              }
            }
          }
        }
      }
    });

    capabilitiesViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      @Override
      public void selectionChanged(SelectionChangedEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        Item item = (Item)selection.getFirstElement();
        if (item != null && item.isCapability())
        {
          String namespace = item.getNamespace();
          String name = item.getName();
          loadDetails(namespace, name);
        }
        else
        {
          detailsViewer.setInput(null);
        }

        setSelected(item);
      }
    });

    capabilitiesViewer.addDragSupport(RepositoryExplorer.DND_OPERATIONS, RepositoryExplorer.DND_TRANSFERS,
        new GeneralDragAdapter(capabilitiesViewer, new GeneralDragAdapter.DraggedObjectsFactory()
        {
          @Override
          public List<Object> createDraggedObjects(ISelection selection) throws Exception
          {
            List<Object> result = new ArrayList<>();
            for (Object object : ((IStructuredSelection)selection).toArray())
            {
              if (object instanceof Item)
              {
                Item item = (Item)object;
                if (item.isCapability())
                {
                  result.add(createRequirement(item));
                }
              }
            }

            return result;
          }

          private Requirement createRequirement(Item item)
          {
            Requirement requirement = P2Factory.eINSTANCE.createRequirement(item.getName());
            requirement.setNamespace(item.getNamespace());
            return requirement;
          }
        }, RepositoryExplorer.DND_DELEGATES));

    Composite detailsComposite = new Composite(sashForm, SWT.NONE);
    detailsComposite.setLayout(new FillLayout());
    detailsComposite.setForeground(capabilitiesTree.getForeground());
    detailsComposite.setBackground(capabilitiesTree.getBackground());
    detailsViewer = new TreeViewer(detailsComposite);
    detailsViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new ToolTipLabelProvider(adapterFactory, detailsViewer, null)));
    detailsViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));

    detailsViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      @Override
      public void selectionChanged(SelectionChangedEvent event)
      {
        IStructuredSelection selection = (IStructuredSelection)event.getSelection();
        Item item = (Item)selection.getFirstElement();
        setSelected(item);
      }
    });

    detailsViewer.addDoubleClickListener(new IDoubleClickListener()
    {
      @Override
      public void doubleClick(DoubleClickEvent event)
      {
        handleDetailsDoubleClick();
      }
    });

    detailsViewer.addDragSupport(RepositoryExplorer.DND_OPERATIONS, RepositoryExplorer.DND_TRANSFERS,
        new GeneralDragAdapter(detailsViewer, new GeneralDragAdapter.DraggedObjectsFactory()
        {
          @Override
          public List<Object> createDraggedObjects(ISelection selection) throws Exception
          {
            List<Object> result = new ArrayList<>();
            for (Object object : ((IStructuredSelection)selection).toArray())
            {
              if (object instanceof Item)
              {
                Item item = (Item)object;
                if (item.isRepository())
                {
                  result.add(P2Factory.eINSTANCE.createRepository(item.getText()));
                }
                else if (item.isCapability())
                {
                  result.add(createRequirement(item));
                }
                else
                {
                  try
                  {
                    if (item.isVersionRange())
                    {
                      Item parent = item.getParent();
                      while (parent.isVersionRange())
                      {
                        parent = parent.getParent();
                      }

                      Requirement requirement = P2Factory.eINSTANCE.createRequirement(parent.getName());
                      requirement.setNamespace(parent.getNamespace());
                      requirement.setVersionRange(item.versionRange);

                      result.add(requirement);
                    }
                    else
                    {
                      Requirement requirement = createRequirement(item.getParent());
                      Version version = Version.create(item.getText());
                      if (!Version.emptyVersion.equals(version))
                      {
                        requirement.setVersionRange(new VersionRange(version, true, version, true));
                      }

                      result.add(requirement);
                    }
                  }
                  catch (RuntimeException ex)
                  {
                    //$FALL-THROUGH$
                  }
                }
              }
            }

            return result;
          }

          private Requirement createRequirement(Item item)
          {
            Requirement requirement = P2Factory.eINSTANCE.createRequirement(item.getName());
            requirement.setNamespace(item.getNamespace());
            return requirement;
          }
        }, RepositoryExplorer.DND_DELEGATES));

    sashForm.setWeights(new int[] { 14, 5 });

    UIUtil.asyncExec(composite, new Runnable()
    {
      @Override
      public void run()
      {
        setSelected(null);
      }
    });

    loadModel();
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    Button applyButton = createButton(parent, APPLY_ID, Messages.SearchEclipseDialog_applyButton_text, true);
    applyButton.setEnabled(false);
    applyButton.setImage(getDefaultApplyImage());

    super.createButtonsForButtonBar(parent);

    Button okButton = getButton(IDialogConstants.OK_ID);
    okButton.setText(Messages.SearchEclipseDialog_okButton_text);
  }

  protected void updateButtons(boolean enabled)
  {
    Button okButton = getButton(IDialogConstants.OK_ID);
    if (okButton != null)
    {
      ApplyHandler applyHandler = getApplyHandler();
      okButton.setEnabled(enabled && applyHandler != null);

      Button applyButton = getButton(APPLY_ID);
      applyButton.setEnabled(enabled && applyHandler != null);

      if (applyHandler == null)
      {
        applyButton.setImage(getDefaultApplyImage());
      }
      else
      {
        okButton.setToolTipText(applyHandler.getToolTipText());

        applyButton.setImage(applyHandler.getImage());
        applyButton.setToolTipText(applyHandler.getToolTipText());
      }
    }
  }

  protected void loadModel()
  {
    Item root = Item.createItem();
    root.getChildren().add(Item.createNamespaceItem(Messages.SearchEclipseDialog_loadingItem_namespace));
    capabilitiesViewer.setInput(root);

    Job job = new Job(Messages.SearchEclipseDialog_capabilityLoaderJob_name)
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        final Item root = Item.createItem();
        EList<Object> children = root.getChildren();
        Map<String, Set<String>> capabilities = new LinkedHashMap<>(P2Index.INSTANCE.getCapabilities());
        if (capabilities.isEmpty())
        {
          children.add(Item.createNamespaceItem(Messages.SearchEclipseDialog_indexUnavailableItem_namespace));
        }
        else
        {
          Set<String> flavors = capabilities.get("org.eclipse.equinox.p2.flavor"); //$NON-NLS-1$
          Set<String> capabilityKeys = capabilities.keySet();
          RepositoryExplorer.minimizeNamespaces(flavors, capabilityKeys);
          capabilityKeys.remove("org.eclipse.equinox.p2.flavor"); //$NON-NLS-1$
          capabilityKeys.remove("A.PDE.Target.Platform"); //$NON-NLS-1$
          for (Entry<String, Set<String>> entry : capabilities.entrySet())
          {
            String namespace = entry.getKey();
            Item namespaceItem = Item.createNamespaceItem(namespace);
            children.add(namespaceItem);

            Map<SegmentSequence, Item> hierarchicalChildren = new LinkedHashMap<>();
            SegmentSequence baseName = SegmentSequence.create("."); //$NON-NLS-1$
            hierarchicalChildren.put(baseName, namespaceItem);
            for (String value : entry.getValue())
            {
              SegmentSequence qualifiedName = SegmentSequence.create(".", value); //$NON-NLS-1$
              SegmentSequence partialName = baseName;
              Item parent = namespaceItem;
              for (String segment : qualifiedName.segments())
              {
                partialName = partialName.append(segment);
                Item itemProvider = hierarchicalChildren.get(partialName);
                if (itemProvider == null)
                {
                  itemProvider = Item.create(namespace, partialName);
                  hierarchicalChildren.put(partialName, itemProvider);
                  parent.getChildren().add(itemProvider);
                }

                parent = itemProvider;
              }

              parent.setConcrete();
            }
          }

          root.sort();
        }

        UIUtil.asyncExec(capabilitiesViewer.getControl(), new Runnable()
        {
          @Override
          public void run()
          {
            capabilitiesViewer.setInput(root);
            if (initialFilterString != null)
            {
              filterText.setText(initialFilterString);
              filterText.setSelection(initialFilterString.length());
            }
          }
        });

        return Status.OK_STATUS;
      }
    };

    job.setSystem(true);
    job.schedule();
  }

  protected void loadDetails(final String namespace, final String name)
  {
    if (detailsLoadJob != null)
    {
      detailsLoadJob.cancel();
    }

    Item root = Item.createItem();
    root.getChildren().add(Item.createNamespaceItem(Messages.SearchEclipseDialog_loadingItem_namespace));
    detailsViewer.setInput(root);

    detailsLoadJob = new Job(Messages.SearchEclipseDialog_detailsLoadJob_name)
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        final Item input = Item.createItem();
        Item capabilityItem = Item.create(namespace, name);
        input.getChildren().add(capabilityItem);
        handleDetailsLoad(capabilityItem);
        if (!monitor.isCanceled())
        {
          UIUtil.asyncExec(detailsViewer.getControl(), new Runnable()
          {
            @Override
            public void run()
            {
              detailsViewer.setInput(input);
              detailsViewer.expandToLevel(getDetailsAutoExpandLevel());
            }
          });
        }

        return Status.OK_STATUS;
      }
    };

    detailsLoadJob.setSystem(true);
    detailsLoadJob.schedule();
  }

  @Override
  public boolean close()
  {
    workbenchWindowSelectionTracker.dispose();
    return super.close();
  }

  @Override
  protected void buttonPressed(int buttonId)
  {
    if (buttonId == APPLY_ID)
    {
      ApplyHandler applyHandler = getApplyHandler();
      if (applyHandler != null)
      {
        applyHandler.apply();
      }
    }
    else
    {
      super.buttonPressed(buttonId);
    }
  }

  @Override
  protected void okPressed()
  {
    buttonPressed(APPLY_ID);
    super.okPressed();
  }

  protected ApplyHandler getApplyHandler()
  {
    IWorkbenchPart activePart = workbenchWindowSelectionTracker.getActivePart();
    if (activePart instanceof RepositoryExplorer)
    {
      return getApplyHandler((RepositoryExplorer)activePart);
    }

    ISelection selection = workbenchWindowSelectionTracker.getSelection();
    if (selection instanceof IStructuredSelection)
    {
      Object firstElement = ((IStructuredSelection)selection).getFirstElement();
      EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(firstElement);
      if (domain != null && firstElement != null)
      {
        return getApplyHandler(domain, firstElement);
      }
    }

    return null;
  }

  protected Image getDefaultApplyImage()
  {
    IWorkbenchPart activePart = workbenchWindowSelectionTracker.getActivePart();
    ISelection selection = workbenchWindowSelectionTracker.getSelection();
    if (selection instanceof IStructuredSelection)
    {
      Object firstElement = ((IStructuredSelection)selection).getFirstElement();
      EditingDomain domain = AdapterFactoryEditingDomain.getEditingDomainFor(firstElement);
      if (domain != null && firstElement != null)
      {
        return new ApplyHandler(domain, firstElement, Messages.SearchEclipseDialog_applyHandler_tooltipPrefix)
        {
          @Override
          public void apply()
          {
          }
        }.getImage();
      }
    }

    return new ApplyHandler(activePart)
    {
      @Override
      public void apply()
      {
      }
    }.getImage();
  }

  protected ApplyHandler getApplyHandler(RepositoryExplorer repositoryExplorer)
  {
    return null;
  }

  protected ApplyHandler getApplyHandler(EditingDomain domain, Object target)
  {
    return null;
  }

  /**
   * @author Ed Merks
   */
  private static final class ItemFilter extends PatternFilter implements FilteredTreeWithoutWorkbench.ExpansionFilter
  {
    private static final Pattern WILDCARD_FILTER_PATTERN = Pattern.compile("(\\\\.|[*?/.])"); //$NON-NLS-1$

    private Pattern filterPattern;

    private List<Pattern> prefixFilterPatterns;

    public List<Pattern> getPrefixFilterPatterns()
    {
      return prefixFilterPatterns;
    }

    @Override
    public void setPattern(String patternString)
    {
      super.setPattern(patternString);
      if (patternString == null)
      {
        filterPattern = null;
        prefixFilterPatterns = Collections.emptyList();
      }
      else
      {
        prefixFilterPatterns = new ArrayList<>();
        StringBuffer pattern = new StringBuffer("(\\Q"); //$NON-NLS-1$
        if (patternString.indexOf('/') == -1)
        {
          patternString = '/' + patternString;
        }

        Matcher matcher = WILDCARD_FILTER_PATTERN.matcher(patternString);
        while (matcher.find())
        {
          String separator = matcher.group(1);
          if (separator.length() == 2)
          {
            matcher.appendReplacement(pattern, ""); //$NON-NLS-1$
            if ("\\E".equals(separator)) //$NON-NLS-1$
            {
              pattern.append("\\E\\\\E\\Q"); //$NON-NLS-1$
            }
            else if ("\\\\".equals(separator)) //$NON-NLS-1$
            {
              pattern.append("\\E\\\\\\Q"); //$NON-NLS-1$
            }
            else
            {
              pattern.append(separator.charAt(1));
            }
          }
          else
          {
            char separatorChar = separator.charAt(0);
            String tail;
            switch (separatorChar)
            {
              case '*':
                tail = ".*?"; //$NON-NLS-1$
                break;
              case '?':
                tail = "."; //$NON-NLS-1$
                break;
              case '/':
                if (matcher.start(1) == 0)
                {
                  tail = "/.*?"; //$NON-NLS-1$
                }
                else
                {
                  tail = ".*?/.*?"; //$NON-NLS-1$
                }
                break;
              case '.':
                tail = "(\\.)"; //$NON-NLS-1$
                break;
              default:
                throw new IllegalStateException("Pattern " + WILDCARD_FILTER_PATTERN + " should match a single character"); //$NON-NLS-1$ //$NON-NLS-2$
            }

            matcher.appendReplacement(pattern, "\\\\E)"); //$NON-NLS-1$
            prefixFilterPatterns.add(Pattern.compile(pattern.toString(), Pattern.CASE_INSENSITIVE));
            pattern.append(tail).append("(\\Q"); //$NON-NLS-1$
          }
        }

        matcher.appendTail(pattern);
        pattern.append("\\E)"); //$NON-NLS-1$

        filterPattern = Pattern.compile(pattern.toString(), Pattern.CASE_INSENSITIVE);
        prefixFilterPatterns.add(filterPattern);
        Collections.reverse(prefixFilterPatterns);
      }
    }

    @Override
    protected boolean isLeafMatch(Viewer viewer, Object element)
    {
      if (element == null)
      {
        return false;
      }

      // Match against the fully qualified name of the item, not just the label text.
      Item item = (Item)element;
      String text = item.getQualifiedName();
      return wordMatches(text);
    }

    @Override
    protected boolean wordMatches(String text)
    {
      if (filterPattern == null)
      {
        return true;
      }

      if (text == null)
      {
        return false;
      }

      return filterPattern.matcher(text).find();
    }

    @Override
    public boolean shouldExpand(Object element)
    {
      // Don't expand an item if that item directly matches the pattern, unless it's not a capability item.
      Item item = (Item)element;
      return !isLeafMatch(null, element) || !item.isCapability();
    }
  }

  /**
   * @author Ed Merks
   */
  private static class Item extends ItemProvider
  {
    private static final Comparator<String> STRING_COMPARATOR = CommonPlugin.INSTANCE.getComparator();

    private static final Comparator<Item> COMPARATOR = new Comparator<>()
    {
      @Override
      public int compare(Item item1, Item item2)
      {
        if (item1.versionRange != null && item2.versionRange != null)
        {
          Version minimum1 = item1.versionRange.getMinimum();
          Version minimum2 = item2.versionRange.getMinimum();
          boolean qualifier1 = minimum1.getSegmentCount() >= 4 && !"".equals(minimum1.getSegment(3)); //$NON-NLS-1$
          boolean qualifier2 = minimum2.getSegmentCount() >= 4 && !"".equals(minimum2.getSegment(3)); //$NON-NLS-1$
          if (qualifier1 && !qualifier2)
          {
            return 1;
          }
          if (qualifier2 && !qualifier1)
          {
            return -1;
          }

          int result = minimum1.compareTo(minimum2);
          if (result == 0)
          {
            return -item1.versionRange.getMaximum().compareTo(item2.versionRange.getMaximum());
          }
          return -result;
        }

        String text1 = item1.getText();
        String text2 = item2.getText();
        if (text1.length() >= 1 && text2.length() >= 1)
        {
          boolean isDigit1 = Character.isDigit(text1.charAt(0));
          boolean isDigit2 = Character.isDigit(text2.charAt(0));
          if (isDigit1 && !isDigit2)
          {
            return 1;
          }

          if (!isDigit1 && isDigit2)
          {
            return -1;
          }
        }

        return STRING_COMPARATOR.compare(text1, text2);
      }
    };

    private static final Image VERSION_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/version"); //$NON-NLS-1$

    private static final Image NAMESPACE_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/folder"); //$NON-NLS-1$

    private static final Image CAPABILITY_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/capability"); //$NON-NLS-1$

    private static final Image REPOSITORY_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/repository"); //$NON-NLS-1$

    private static final Image JAVA_PACKAGE_IMAGE = ExtendedImageRegistry.INSTANCE.getImage(P2EditPlugin.INSTANCE.getImage("full/obj16/Requirement_Package")); //$NON-NLS-1$

    private static final Image BUNDLE_IMAGE = ExtendedImageRegistry.INSTANCE.getImage(P2EditPlugin.INSTANCE.getImage("full/obj16/Requirement_Plugin")); //$NON-NLS-1$

    private static final Image FEATURE_IMAGE = ExtendedImageRegistry.INSTANCE.getImage(P2EditPlugin.INSTANCE.getImage("full/obj16/Requirement_Feature")); //$NON-NLS-1$

    private String namespace;

    private String name;

    private String decoration;

    private boolean concrete;

    private boolean repository;

    private VersionRange versionRange;

    private Item()
    {
    }

    private Item(String namespace)
    {
      super(namespace, NAMESPACE_IMAGE);
      this.namespace = namespace;
    }

    private Item(String namespace, SegmentSequence partialName)
    {
      super(URI.decode(partialName.lastSegment()), getImage(namespace, partialName.toString()));
      this.namespace = namespace;
      name = partialName.toString();
    }

    private Item(String namespace, String name)
    {
      super(namespace + "/" + URI.decode(name), getImage(namespace, name)); //$NON-NLS-1$
      this.namespace = namespace;
      this.name = name;
    }

    private Item(Version version)
    {
      super(version.toString(), VERSION_IMAGE);
    }

    private Item(VersionRange versionRange)
    {
      super(versionRange.toString(), VERSION_IMAGE);
      this.versionRange = versionRange;
    }

    private Item(Repository repository)
    {
      super(repository.getLocation().toString(), REPOSITORY_IMAGE);
      int capabilityCount = repository.getCapabilityCount();
      decoration = " " //$NON-NLS-1$
          + (capabilityCount == 1 ? Messages.SearchEclipseDialog_capability : NLS.bind(Messages.SearchEclipseDialog_capabilities, capabilityCount));
      this.repository = true;
    }

    public String getNamespace()
    {
      return namespace;
    }

    public String getName()
    {
      return name;
    }

    public String getQualifiedName()
    {
      StringBuilder result = new StringBuilder();
      if (namespace != null)
      {
        result.append(namespace);
      }

      if (name != null)
      {
        if (namespace != null)
        {
          result.append('/');
        }

        result.append(URI.decode(name));
      }

      return result.toString();
    }

    public boolean isCapability()
    {
      return namespace != null && name != null;
    }

    public boolean isRepository()
    {
      return repository;
    }

    public void setConcrete()
    {
      setImage(ExtendedImageRegistry.INSTANCE.getImage(new CapabilityComposedImage(getImage())));
      concrete = true;
    }

    public boolean isConcrete()
    {
      return concrete;
    }

    public boolean isVersionRange()
    {
      return versionRange != null;
    }

    public static Item createRepository(Repository repository)
    {
      Item item = new Item(repository);
      EList<Object> children = item.getChildren();
      for (Repository child : repository.getChildren())
      {
        children.add(createRepository(child));
      }

      return item;
    }

    public static Item createVersionRange(VersionRange versionRange)
    {
      return new Item(versionRange);
    }

    public static Item createVersion(Version version)
    {
      return new Item(version);
    }

    public static Item create(String namespace, SegmentSequence partialName)
    {
      return new Item(namespace, partialName);
    }

    public static Item create(String namespace, String name)
    {
      return new Item(namespace, name);
    }

    public static Item createItem()
    {
      return new Item();
    }

    public static Item createNamespaceItem(String namespace)
    {
      return new Item(namespace);
    }

    @Override
    public Object getStyledText(Object object)
    {
      if (decoration != null)
      {
        org.eclipse.emf.edit.provider.StyledString styledLabel = new org.eclipse.emf.edit.provider.StyledString();
        styledLabel.append(getText());
        styledLabel.append(" "); //$NON-NLS-1$
        styledLabel.append(decoration, org.eclipse.emf.edit.provider.StyledString.Style.DECORATIONS_STYLER);
        return styledLabel;
      }

      return super.getStyledText(object);
    }

    @Override
    public Item getParent()
    {
      return (Item)super.getParent();
    }

    public EList<Item> getItems()
    {
      @SuppressWarnings("unchecked")
      EList<Item> children = (EList<Item>)(EList<?>)getChildren();
      return children;
    }

    public void sort()
    {
      EList<Item> children = getItems();
      ECollections.sort(children, COMPARATOR);
      for (Item child : children)
      {
        child.sort();
      }
    }

    private static Image getImage(String namespace, String name)
    {
      if ("java.package".equals(namespace)) //$NON-NLS-1$
      {
        return JAVA_PACKAGE_IMAGE;
      }

      if (IInstallableUnit.NAMESPACE_IU_ID.equals(namespace))
      {
        if (name.endsWith(Requirement.FEATURE_SUFFIX))
        {
          return FEATURE_IMAGE;
        }

        return BUNDLE_IMAGE;
      }

      if ("org.eclipse.update.feature".equals(namespace)) //$NON-NLS-1$
      {
        return FEATURE_IMAGE;
      }

      if ("osgi.fragment".equals(namespace) || "osgi.bundle".equals(namespace)) //$NON-NLS-1$ //$NON-NLS-2$
      {
        return BUNDLE_IMAGE;
      }

      return CAPABILITY_IMAGE;
    }
  }

  /**
   * @author Ed Merks
   */
  private static final class CapabilityComposedImage extends ComposedImage
  {
    private static final Image CONCRETE_CAPABILITY_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("ovr16/concrete_capability"); //$NON-NLS-1$

    private CapabilityComposedImage(Object image)
    {
      super(Arrays.asList(new Object[] { image, CONCRETE_CAPABILITY_IMAGE }));
    }

    @Override
    public List<ComposedImage.Point> getDrawPoints(Size size)
    {
      List<ComposedImage.Point> result = super.getDrawPoints(size);
      if (result.size() > 1)
      {
        result.get(1).x += 9;
        result.get(1).y += 9;
      }

      return result;
    }
  }

  /**
   * @author Ed Merks
   */
  public static class ToolTipLabelProvider extends DecoratingColumLabelProvider.StyledLabelProvider
  {
    public ToolTipLabelProvider(AdapterFactory adapterFactory, final Viewer viewer, final ItemFilter itemFilter)
    {
      super(new AdapterFactoryLabelProvider.StyledLabelProvider(adapterFactory, viewer), new IStyledLabelDecorator()
      {
        private final Styler bold = new Styler()
        {
          private final Font boldFont = ExtendedFontRegistry.INSTANCE.getFont(viewer.getControl().getFont(), IItemFontProvider.BOLD_FONT);

          @Override
          public void applyStyles(TextStyle textStyle)
          {
            textStyle.font = boldFont;
          }
        };

        @Override
        public void removeListener(ILabelProviderListener listener)
        {
        }

        @Override
        public boolean isLabelProperty(Object element, String property)
        {
          return true;
        }

        @Override
        public void dispose()
        {
        }

        @Override
        public void addListener(ILabelProviderListener listener)
        {
        }

        @Override
        public String decorateText(String text, Object element)
        {
          return text;
        }

        @Override
        public Image decorateImage(Image image, Object element)
        {
          return image;
        }

        @Override
        public StyledString decorateStyledText(StyledString styledString, Object element)
        {
          if (itemFilter != null)
          {
            List<Pattern> prefixFilterPatterns = itemFilter.getPrefixFilterPatterns();
            if (prefixFilterPatterns != null)
            {
              Item item = (Item)element;
              String text = item.getText();
              String name = item.getQualifiedName();
              for (Pattern prefixFilterPattern : prefixFilterPatterns)
              {
                Matcher matcher = prefixFilterPattern.matcher(name);
                if (matcher.find())
                {
                  // System.err.println("" + name);
                  // System.err.println("" + text);
                  StyledString styledLabel = new StyledString();
                  int groupCount = matcher.groupCount();
                  StringBuilder styleRun = null;
                  boolean previousMatches = false;
                  for (int i = name.length() - text.length(); i < name.length(); ++i)
                  {
                    boolean matches = false;
                    for (int j = 1; j <= groupCount; ++j)
                    {
                      int start = matcher.start(j);
                      int end = matcher.end(j);
                      if (i >= start && i < end)
                      {
                        matches = true;
                        break;
                      }
                    }

                    if (previousMatches != matches)
                    {
                      if (styleRun != null)
                      {
                        if (previousMatches)
                        {
                          styledLabel.append(styleRun.toString(), bold);
                        }
                        else
                        {
                          styledLabel.append(styleRun.toString());
                        }
                        styleRun = null;
                      }

                      previousMatches = matches;
                    }

                    if (styleRun == null)
                    {
                      styleRun = new StringBuilder();
                    }

                    styleRun.append(name.charAt(i));

                    // System.err.print(matches ? '^' : name.charAt(i));
                  }

                  if (styleRun != null)
                  {
                    if (previousMatches)
                    {
                      styledLabel.append(styleRun.toString(), bold);
                    }
                    else
                    {
                      styledLabel.append(styleRun.toString());
                    }
                  }

                  // System.err.println();

                  return styledLabel;
                }
              }
            }
          }

          return styledString;
        }
      });
    }

    @Override
    public String getToolTipText(Object element)
    {
      Item item = (Item)element;
      if (item.isCapability())
      {
        StringBuilder result = new StringBuilder();
        result.append(DiagnosticDecorator
            .enquote("<img src='" + ImageURIRegistry.INSTANCE.getImageURI(ExtendedImageRegistry.INSTANCE.getImage(item.getImage())) + "'/> ")); //$NON-NLS-1$ //$NON-NLS-2$
        result.append("&nbsp;"); //$NON-NLS-1$
        result.append(item.getNamespace());
        result.append('/');
        result.append(URI.decode(item.getName()));
        return DiagnosticDecorator.strip(result.toString());
      }

      return null;
    }
  }

  /**
   * @author Ed Merks
   */
  public static class Repositories extends SearchEclipseDialog
  {
    public static final String MESSAGE = Messages.SearchEclipseDialog_repositoriesDialog_message;

    public static final String TITLE = Messages.SearchEclipseDialog_repositoriesDialog_title;

    private String selectedRepository;

    public Repositories(IWorkbenchWindow workbenchWindow)
    {
      super(workbenchWindow, TITLE);
    }

    @Override
    public String getHelpPath()
    {
      return P2UIPlugin.INSTANCE.getSymbolicName() + "/html/SearchEclipseRepositoriesHelp.html"; //$NON-NLS-1$
    }

    public String getSelectedRepository()
    {
      return selectedRepository;
    }

    public void setSelectedRepository(String selectedRepository)
    {
      this.selectedRepository = selectedRepository;
      updateButtons(selectedRepository != null);
    }

    @Override
    protected void setSelected(Item item)
    {
      setSelectedRepository(item != null && item.isRepository() ? item.getText() : null);
    }

    @Override
    protected void selectionChanged(IWorkbenchPart part, ISelection selection)
    {
      setSelectedRepository(selectedRepository);
    }

    @Override
    protected Image getShellImage()
    {
      return P2UIPlugin.INSTANCE.getSWTImage("tool16/search_repository.png"); //$NON-NLS-1$
    }

    @Override
    protected String getShellText()
    {
      return TITLE;
    }

    @Override
    protected String getDefaultMessage()
    {
      return MESSAGE + "."; //$NON-NLS-1$
    }

    @Override
    protected void handleDetailsDoubleClick()
    {
      if (selectedRepository != null)
      {
        okPressed();
      }
      else
      {
        IStructuredSelection selection = (IStructuredSelection)detailsViewer.getSelection();
        Item item = (Item)selection.getFirstElement();
        boolean expanded = detailsViewer.getExpandedState(item);
        detailsViewer.setExpandedState(item, !expanded);
      }
    }

    @Override
    protected void handleDetailsLoad(Item capabilityItem)
    {
      Map<Repository, Set<Version>> capabilitiesFromSimpleRepositories = P2Index.INSTANCE.lookupCapabilities(capabilityItem.getNamespace(),
          capabilityItem.getName());
      Map<Repository, Set<Version>> capabilitiesFromComposedRepositories = P2Index.INSTANCE
          .generateCapabilitiesFromComposedRepositories(capabilitiesFromSimpleRepositories);
      CollectionUtil.addAll(capabilitiesFromComposedRepositories, capabilitiesFromSimpleRepositories);

      Map<Version, Item> versionItems = new TreeMap<>();
      for (Map.Entry<Repository, Set<Version>> entry : capabilitiesFromComposedRepositories.entrySet())
      {
        Repository key = entry.getKey();
        for (Version version : entry.getValue())
        {
          Item versionItem = versionItems.get(version);
          if (versionItem == null)
          {
            versionItem = Item.createVersion(version);
            versionItems.put(version, versionItem);
          }

          versionItem.getChildren().add(Item.createRepository(key));
        }
      }

      for (Item versionItem : versionItems.values())
      {
        versionItem.sort();
      }

      capabilityItem.getChildren().addAll(versionItems.values());
      ECollections.reverse(capabilityItem.getChildren());
    }

    @Override
    protected int getDetailsAutoExpandLevel()
    {
      return 2;
    }

    @Override
    protected ApplyHandler getApplyHandler(final EditingDomain domain, final Object target)
    {
      if (target instanceof org.eclipse.oomph.p2.Repository)
      {
        return new ApplyHandler(domain, target, Messages.SearchEclipseDialog_repositoryApplyHandler_tooltipPrefix)
        {
          @Override
          public void apply()
          {
            Command setCommand = SetCommand.create(domain, target, P2Package.Literals.REPOSITORY__URL, selectedRepository);
            domain.getCommandStack().execute(setCommand);
          }
        };
      }

      if (target instanceof EObject)
      {
        EClass eClass = ((EObject)target).eClass();
        for (final EReference eReference : eClass.getEAllContainments())
        {
          if (eReference.getEType() == P2Package.Literals.REPOSITORY && eReference.isContainment() && eReference.isMany())
          {
            return new ApplyHandler(domain, target, Messages.SearchEclipseDialog_eObjectApplyHandler_tooltipPrefix)
            {
              @Override
              public void apply()
              {
                org.eclipse.oomph.p2.Repository repository = P2Factory.eINSTANCE.createRepository();
                repository.setURL(selectedRepository);
                Command addCommand = AddCommand.create(domain, target, eReference, repository);
                domain.getCommandStack().execute(addCommand);
              }
            };
          }
        }
      }

      return null;
    }

    @Override
    protected ApplyHandler getApplyHandler(final RepositoryExplorer repositoryExplorer)
    {
      return new ApplyHandler(repositoryExplorer)
      {
        @Override
        public void apply()
        {
          repositoryExplorer.activateAndLoadRepository(selectedRepository);
        }
      };
    }

    /**
     * Returns the instance for this workbench window, if there is one.
     */
    public static Repositories getFor(IWorkbenchWindow workbenchWindow)
    {
      return DockableDialog.getFor(Repositories.class, workbenchWindow);
    }

    /**
     * Close the instance for this workbench window, if there is one.
     */
    public static void closeFor(IWorkbenchWindow workbenchWindow)
    {
      DockableDialog.closeFor(Repositories.class, workbenchWindow);
    }

    /**
     * Reopen or create the instance for this workbench window.
     */
    public static Repositories openFor(final IWorkbenchWindow workbenchWindow)
    {
      Factory<Repositories> factory = new Factory<>()
      {
        @Override
        public Repositories create(IWorkbenchWindow workbenchWindow)
        {
          return new Repositories(workbenchWindow);
        }
      };

      return DockableDialog.openFor(Repositories.class, factory, workbenchWindow);
    }
  }

  /**
   *
   * @author Ed Merks
   */
  public static class Requirements extends SearchEclipseDialog
  {
    public static final String TITLE = Messages.SearchEclipseDialog_requirementsDialog_title;

    public static final String MESSAGE = Messages.SearchEclipseDialog_requirementsDialog_message;

    private Requirement selectedRequirement;

    public Requirements(IWorkbenchWindow workbenchWindow)
    {
      super(workbenchWindow, TITLE);
    }

    @Override
    public String getHelpPath()
    {
      return P2UIPlugin.INSTANCE.getSymbolicName() + "/html/SearchEclipseRequirementsHelp.html"; //$NON-NLS-1$
    }

    public Requirement getSelectedRequirement()
    {
      return selectedRequirement;
    }

    public void setSelectedRequirement(Requirement selectedRequirement)
    {
      this.selectedRequirement = selectedRequirement;
      updateButtons(selectedRequirement != null);
    }

    @Override
    protected void selectionChanged(IWorkbenchPart part, ISelection selection)
    {
      setSelectedRequirement(selectedRequirement);
    }

    public void setInitialFilterString(Requirement requirement)
    {
      setInitialFilterString(getFilterString(requirement));
    }

    @Override
    protected void setSelected(Item item)
    {
      Requirement requirement = null;
      if (item != null)
      {
        if (item.isConcrete())
        {
          requirement = P2Factory.eINSTANCE.createRequirement(item.getName());
          requirement.setNamespace(item.getNamespace());
        }
        else if (item.isVersionRange())
        {
          Item parent = item.getParent();
          while (parent.isVersionRange())
          {
            parent = parent.getParent();
          }
          requirement = P2Factory.eINSTANCE.createRequirement(parent.getName());
          requirement.setNamespace(parent.getNamespace());
          requirement.setVersionRange(item.versionRange);
        }
      }

      setSelectedRequirement(requirement);
    }

    @Override
    protected Image getShellImage()
    {
      return P2UIPlugin.INSTANCE.getSWTImage("tool16/search_requirement.png"); //$NON-NLS-1$
    }

    @Override
    protected String getShellText()
    {
      return TITLE;
    }

    @Override
    protected String getDefaultMessage()
    {
      return MESSAGE + "."; //$NON-NLS-1$
    }

    @Override
    protected void handleDetailsDoubleClick()
    {
      IStructuredSelection selection = (IStructuredSelection)detailsViewer.getSelection();
      Item item = (Item)selection.getFirstElement();
      boolean expanded = detailsViewer.getExpandedState(item);
      detailsViewer.setExpandedState(item, !expanded);
    }

    @Override
    protected void handleDetailsLoad(Item capabilityItem)
    {
      Map<Repository, Set<Version>> capabilitiesFromSimpleRepositories = P2Index.INSTANCE.lookupCapabilities(capabilityItem.getNamespace(),
          capabilityItem.getName());
      Map<VersionRange, Item> versionRangeItems = new LinkedHashMap<>();
      for (Set<Version> versions : capabilitiesFromSimpleRepositories.values())
      {
        for (Version version : versions)
        {
          Item parent = capabilityItem;
          for (VersionSegment versionSegment : VersionSegment.VALUES)
          {
            VersionRange versionRange = P2Factory.eINSTANCE.createVersionRange(version, versionSegment, true);
            Item versionRangeItem = versionRangeItems.get(versionRange);
            if (versionRangeItem == null)
            {
              versionRangeItem = Item.createVersionRange(versionRange);
              parent.getChildren().add(versionRangeItem);
              versionRangeItems.put(versionRange, versionRangeItem);
            }

            if (Version.emptyVersion.equals(version))
            {
              break;
            }

            VersionRange restrictedVersionRange = P2Factory.eINSTANCE.createVersionRange(version, versionSegment, false);
            Item restrictedVersionRangeItem = versionRangeItems.get(restrictedVersionRange);
            if (restrictedVersionRangeItem == null)
            {
              restrictedVersionRangeItem = Item.createVersionRange(restrictedVersionRange);
              versionRangeItem.getChildren().add(restrictedVersionRangeItem);
              versionRangeItems.put(restrictedVersionRange, restrictedVersionRangeItem);
            }

            if (versionSegment == VersionSegment.QUALIFIER)
            {
              restrictedVersionRange = new VersionRange(version, true, P2Factory.eINSTANCE.createVersionRange(version, VersionSegment.MINOR).getMaximum(),
                  false);
              restrictedVersionRangeItem = versionRangeItems.get(restrictedVersionRange);
              if (restrictedVersionRangeItem == null)
              {
                restrictedVersionRangeItem = Item.createVersionRange(restrictedVersionRange);
                versionRangeItem.getChildren().add(restrictedVersionRangeItem);
                versionRangeItems.put(restrictedVersionRange, restrictedVersionRangeItem);
              }

              restrictedVersionRange = new VersionRange(version, true, P2Factory.eINSTANCE.createVersionRange(version, VersionSegment.MICRO).getMaximum(),
                  false);
              restrictedVersionRangeItem = versionRangeItems.get(restrictedVersionRange);
              if (restrictedVersionRangeItem == null)
              {
                restrictedVersionRangeItem = Item.createVersionRange(restrictedVersionRange);
                versionRangeItem.getChildren().add(restrictedVersionRangeItem);
                versionRangeItems.put(restrictedVersionRange, restrictedVersionRangeItem);
              }
            }

            parent = versionRangeItem;
          }
        }
      }

      capabilityItem.sort();
    }

    @Override
    protected int getDetailsAutoExpandLevel()
    {
      return 3;
    }

    @Override
    protected ApplyHandler getApplyHandler(final EditingDomain domain, final Object target)
    {
      if (target instanceof Requirement)
      {
        return new ApplyHandler(domain, target, Messages.SearchEclipseDialog_requirementApplyHandler_tooltipPrefix)
        {
          @Override
          public void apply()
          {
            Requirement requirement = (Requirement)target;
            CompoundCommand compoundCommand = new CompoundCommand(CompoundCommand.MERGE_COMMAND_ALL);
            compoundCommand.append(SetCommand.create(domain, requirement, P2Package.Literals.REQUIREMENT__NAME, selectedRequirement.getName()));
            compoundCommand.append(SetCommand.create(domain, requirement, P2Package.Literals.REQUIREMENT__NAMESPACE, selectedRequirement.getNamespace()));
            compoundCommand
                .append(SetCommand.create(domain, requirement, P2Package.Literals.REQUIREMENT__VERSION_RANGE, selectedRequirement.getVersionRange()));
            domain.getCommandStack().execute(compoundCommand);
          }
        };
      }

      if (target instanceof EObject)
      {
        EClass eClass = ((EObject)target).eClass();
        for (final EReference eReference : eClass.getEAllContainments())
        {
          if (eReference.getEType() == P2Package.Literals.REQUIREMENT && eReference.isContainment() && eReference.isMany())
          {
            return new ApplyHandler(domain, target, Messages.SearchEclipseDialog_eObjectApplyHandler_tooltipPrefix)
            {
              @Override
              public void apply()
              {
                Command addCommand = AddCommand.create(domain, target, eReference, selectedRequirement);
                domain.getCommandStack().execute(addCommand);
              }
            };
          }
        }
      }

      return null;
    }

    /**
     * Returns the instance for this workbench window, if there is one.
     */
    public static Requirements getFor(IWorkbenchWindow workbenchWindow)
    {
      return DockableDialog.getFor(Requirements.class, workbenchWindow);
    }

    /**
     * Close the instance for this workbench window, if there is one.
     */
    public static void closeFor(IWorkbenchWindow workbenchWindow)
    {
      DockableDialog.closeFor(Requirements.class, workbenchWindow);
    }

    /**
     * Reopen or create the instance for this workbench window.
     */
    public static Requirements openFor(final IWorkbenchWindow workbenchWindow)
    {
      Factory<Requirements> factory = new Factory<>()
      {
        @Override
        public Requirements create(IWorkbenchWindow workbenchWindow)
        {
          return new Requirements(workbenchWindow);
        }
      };

      return DockableDialog.openFor(Requirements.class, factory, workbenchWindow);
    }
  }

  /**
   * @author Ed Merks
   */
  private static abstract class WorkbenchWindowSelectionTracker implements IPageListener, IPartListener, ISelectionChangedListener
  {
    private IWorkbenchPage workbenchPage;

    private IWorkbenchPart workbenchPart;

    public WorkbenchWindowSelectionTracker(IWorkbenchWindow workbenchWindow)
    {
      workbenchWindow.addPageListener(this);
      IWorkbenchPage activePage = workbenchWindow.getActivePage();
      if (activePage != null)
      {
        pageActivated(activePage);
        IWorkbenchPart activePart = activePage.getActivePart();
        partActivated(activePart);
      }
    }

    public IWorkbenchPart getActivePart()
    {
      return workbenchPart;
    }

    public ISelection getSelection()
    {
      if (workbenchPart instanceof ISelectionProvider)
      {
        return ((ISelectionProvider)workbenchPart).getSelection();
      }

      return null;
    }

    protected abstract void selectionChanged(IWorkbenchPart part, ISelection selection);

    @Override
    public void pageActivated(IWorkbenchPage page)
    {
      if (workbenchPage != null)
      {
        workbenchPage.removePartListener(this);
      }

      workbenchPage = page;
      if (workbenchPage != null)
      {
        workbenchPage.addPartListener(this);
      }
    }

    @Override
    public void pageClosed(IWorkbenchPage page)
    {
    }

    @Override
    public void pageOpened(IWorkbenchPage page)
    {
    }

    @Override
    public void partActivated(IWorkbenchPart part)
    {
      if (workbenchPart instanceof ISelectionProvider)
      {
        ISelectionProvider selectionProvider = (ISelectionProvider)workbenchPart;
        selectionProvider.removeSelectionChangedListener(this);
      }

      workbenchPart = part;
      if (workbenchPart instanceof ISelectionProvider)
      {
        ISelectionProvider selectionProvider = (ISelectionProvider)workbenchPart;
        selectionProvider.addSelectionChangedListener(this);
        selectionChanged(workbenchPart, selectionProvider.getSelection());
      }
      else if (workbenchPart != null)
      {
        selectionChanged(workbenchPart, null);
      }
    }

    @Override
    public void partBroughtToTop(IWorkbenchPart part)
    {
    }

    @Override
    public void partClosed(IWorkbenchPart part)
    {
    }

    @Override
    public void partDeactivated(IWorkbenchPart part)
    {
    }

    @Override
    public void partOpened(IWorkbenchPart part)
    {
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event)
    {
      selectionChanged(workbenchPart, event.getSelection());
    }

    public void dispose()
    {
      pageActivated(null);
      partActivated(null);
    }
  }

  protected static abstract class ApplyHandler
  {
    private final Image image;

    private final String toolTipText;

    public ApplyHandler(IWorkbenchPart workbenchPart)
    {
      image = workbenchPart.getTitleImage();
      toolTipText = NLS.bind(Messages.SearchEclipseDialog_workbenchPathApplyHandler_tooltip, workbenchPart.getTitle());
    }

    public ApplyHandler(EditingDomain domain, Object target, String toolTipPrefix)
    {
      AdapterFactoryLabelProvider adapterFactoryLabelProvider = new AdapterFactoryLabelProvider(((AdapterFactoryEditingDomain)domain).getAdapterFactory());
      image = adapterFactoryLabelProvider.getImage(target);
      toolTipText = toolTipPrefix + " " + adapterFactoryLabelProvider.getText(target); //$NON-NLS-1$
      adapterFactoryLabelProvider.dispose();
    }

    public Image getImage()
    {
      return image;
    }

    public String getToolTipText()
    {
      return toolTipText;
    }

    public abstract void apply();
  }
}

// class CombineImages
// {
// public static void doit()
// {
// try
// {
// List<Image> images = new ArrayList<Image>();
// // Image image1 = P2UIPlugin.INSTANCE.getSWTImage("obj16/repository.gif");
// Image image1 = ExtendedImageRegistry.INSTANCE.getImage(P2EditPlugin.INSTANCE.getImage("full/obj16/Requirement"));
// images.add(image1);
// Image image2 = P2UIPlugin.INSTANCE.getSWTImage("tool16/search2.png");
// images.add(image2);
// Image image = ExtendedImageRegistry.INSTANCE.getImage(new ComposedImage(images));
// URI imageURI = ImageURIRegistry.INSTANCE.getImageURI(image);
// System.err.println("###" + imageURI);
// }
// catch (Exception ex)
// {
// ex.printStackTrace();
// }
// }
// }
