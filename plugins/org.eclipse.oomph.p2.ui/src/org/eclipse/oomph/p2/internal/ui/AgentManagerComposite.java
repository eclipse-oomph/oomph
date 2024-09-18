/*
 * Copyright (c) 2014-2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.internal.ui.GeneralDragAdapter;
import org.eclipse.oomph.internal.ui.OomphTransferDelegate;
import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.RepositoryType;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.AgentManagerElement;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.p2.internal.core.AgentAnalyzer;
import org.eclipse.oomph.p2.internal.core.AgentAnalyzer.AnalyzedArtifact;
import org.eclipse.oomph.p2.internal.core.AgentAnalyzer.AnalyzedBundlePool;
import org.eclipse.oomph.p2.internal.core.AgentAnalyzer.AnalyzedProfile;
import org.eclipse.oomph.p2.internal.core.AgentManagerElementImpl;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.OomphPlugin.Preference;
import org.eclipse.oomph.util.PropertiesUtil;
import org.eclipse.oomph.util.SubMonitor;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.provider.IItemFontProvider;
import org.eclipse.emf.edit.ui.provider.ExtendedFontRegistry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class AgentManagerComposite extends Composite
{
  // The standalone installer doesn't remember instance preferences in debug mode.
  private static final Preference PREF_SHOW_PROFILES = P2UIPlugin.INSTANCE.getConfigurationPreference("showProfiles"); //$NON-NLS-1$

  private static final int DND_OPERATIONS = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;

  private static final List<? extends OomphTransferDelegate> DND_DELEGATES = Collections.singletonList(new OomphTransferDelegate.TextTransferDelegate());

  private static final Transfer[] DND_TRANSFERS = new Transfer[] { DND_DELEGATES.get(0).getTransfer() };

  private TreeViewer treeViewer;

  private BundlePool selectedPool;

  private Object selectedElement;

  private Button clearButton;

  private Button refreshButton;

  private Button newAgentButton;

  private Button newPoolButton;

  private Button selectPoolButton;

  private Button deleteButton;

  private Button cleanupButton;

  private Button analyzeButton;

  private Button showProfilesButton;

  private Button profileDetailsButton;

  public AgentManagerComposite(Composite parent, int style)
  {
    this(parent, style, false, null);
  }

  public AgentManagerComposite(Composite parent, int style, final BundlePool selection)
  {
    this(parent, style, true, selection);
  }

  private AgentManagerComposite(Composite parent, int style, boolean poolSelection, final BundlePool selection)
  {
    super(parent, style);
    UIUtil.setTransparentBackgroundColor(this);
    setLayout(UIUtil.createGridLayout(2));
    selectedPool = selection;

    final P2ContentProvider contentProvider = new P2ContentProvider();

    treeViewer = new TreeViewer(this, SWT.BORDER);
    treeViewer.setContentProvider(contentProvider);
    treeViewer.setLabelProvider(new P2LabelProvider(poolSelection ? new IFontProvider()
    {
      @Override
      public Font getFont(Object element)
      {
        if (element == selectedPool)
        {
          return ExtendedFontRegistry.INSTANCE.getFont(treeViewer.getTree().getFont(), IItemFontProvider.BOLD_FONT);
        }

        return null;
      }
    } : null));
    treeViewer.setComparator(new P2ViewerSorter());
    treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      @Override
      public void selectionChanged(SelectionChangedEvent event)
      {
        selectedElement = ((IStructuredSelection)treeViewer.getSelection()).getFirstElement();
        elementChanged(selectedElement);
      }
    });

    Tree tree = treeViewer.getTree();
    tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    tree.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseDoubleClick(MouseEvent e)
      {
        if (selectedElement instanceof Profile)
        {
          showProfileDetails();
        }
        else if (selectPoolButton != null && selectPoolButton.isEnabled())
        {
          selectPoolButton.notifyListeners(SWT.Selection, new Event());
        }
        else
        {
          treeViewer.setExpandedState(selectedElement, !treeViewer.getExpandedState(selectedElement));
        }
      }
    });

    addDragSupport(treeViewer);

    tree.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        if (e.keyCode == SWT.DEL && deleteButton.isEnabled())
        {
          deletePressed();
        }
      }
    });

    GridLayout buttonLayout = new GridLayout(1, false);
    buttonLayout.marginWidth = 0;
    buttonLayout.marginHeight = 0;

    Composite buttonComposite = new Composite(this, SWT.NONE);
    buttonComposite.setLayout(buttonLayout);
    buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
    buttonComposite.setBounds(0, 0, 64, 64);

    newAgentButton = new Button(buttonComposite, SWT.NONE);
    newAgentButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    newAgentButton.setText(Messages.AgentManagerComposite_newAgent_button_text);
    newAgentButton.setToolTipText(Messages.AgentManagerComposite_newAgent_button_tooltip);
    newAgentButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        String path = openDirectoryDialog(Messages.AgentManagerComposite_newAgent_selectLocation, PropertiesUtil.getUserHome());
        if (path != null)
        {
          Agent agent = P2Util.getAgentManager().addAgent(new File(path));
          BundlePool bundlePool = agent.addBundlePool(new File(path, BundlePool.DEFAULT_NAME));
          refreshFor(bundlePool);
        }
      }
    });

    clearButton = new Button(buttonComposite, SWT.NONE);
    clearButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    clearButton.setText(Messages.AgentManagerComposite_clearCache_text);
    clearButton.setToolTipText(Messages.AgentManagerComposite_clearCache_tooltip);
    clearButton.setEnabled(false);
    clearButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        final Agent agent = getAgent(selectedElement);
        if (agent != null)
        {
          try
          {
            UIUtil.runInProgressDialog(getShell(), new IRunnableWithProgress()
            {
              @Override
              public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
              {
                agent.clearRepositoryCaches(monitor);
              }
            });
          }
          catch (InvocationTargetException ex)
          {
            ErrorDialog.open(ex);
          }
          catch (InterruptedException ex)
          {
            // Ignore.
          }
        }
      }
    });

    cleanupButton = new Button(buttonComposite, SWT.NONE);
    cleanupButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    cleanupButton.setText(Messages.AgentManagerComposite_cleanupAgent_text);
    cleanupButton.setToolTipText(Messages.AgentManagerComposite_cleanupAgent_tooltip);
    cleanupButton.setEnabled(false);
    cleanupButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Agent agent = getAgent(selectedElement);
        if (agent != null)
        {
          cleanup(agent);
          treeViewer.refresh();
        }
      }
    });

    analyzeButton = new Button(buttonComposite, SWT.NONE);
    analyzeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    analyzeButton.setText(Messages.AgentManagerComposite_analyzeAgent_text);
    analyzeButton.setToolTipText(Messages.AgentManagerComposite_analyzeAgent_tooltip);
    analyzeButton.setEnabled(false);
    analyzeButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Agent agent = getAgent(selectedElement);
        if (agent != null)
        {
          analyze(agent);
          treeViewer.refresh();
        }
      }
    });

    newPoolButton = new Button(buttonComposite, SWT.NONE);
    newPoolButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    newPoolButton.setText(Messages.AgentManagerComposite_newPool_button_text);
    newPoolButton.setToolTipText(Messages.AgentManagerComposite_newPool_button_tooltip);
    newPoolButton.setEnabled(false);
    newPoolButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Agent selectedAgent = (Agent)selectedElement;
        String path = openDirectoryDialog(Messages.AgentManagerComposite_newPool_selectLocation, selectedAgent.getLocation().getAbsolutePath());
        if (path != null)
        {
          BundlePool bundlePool = selectedAgent.addBundlePool(new File(path));
          refreshFor(bundlePool);
        }
      }
    });

    if (poolSelection)
    {
      selectPoolButton = new Button(buttonComposite, SWT.NONE);
      selectPoolButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
      selectPoolButton.setText(Messages.AgentManagerComposite_selectPool_text);
      selectPoolButton.setToolTipText(Messages.AgentManagerComposite_selectPool_tooltip);
      selectPoolButton.setEnabled(false);
      selectPoolButton.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          selectedPool = (BundlePool)((IStructuredSelection)treeViewer.getSelection()).getFirstElement();
          elementChanged(selectedPool);
          treeViewer.refresh();
        }
      });
    }

    deleteButton = new Button(buttonComposite, SWT.NONE);
    deleteButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    deleteButton.setText(Messages.AgentManagerComposite_delete_text);
    deleteButton.setToolTipText(Messages.AgentManagerComposite_delete_tooltip);
    deleteButton.setEnabled(false);
    deleteButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        deletePressed();
      }
    });

    refreshButton = new Button(buttonComposite, SWT.NONE);
    refreshButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    refreshButton.setText(Messages.AgentManagerComposite_refresh_text);
    refreshButton.setToolTipText(Messages.AgentManagerComposite_refresh_tooltip);
    refreshButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        try
        {
          ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
          dialog.run(true, true, new IRunnableWithProgress()
          {
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
            {
              P2Util.getAgentManager().refreshAgents(monitor);
            }
          });
        }
        catch (InvocationTargetException ex)
        {
          P2UIPlugin.INSTANCE.log(ex);
        }
        catch (InterruptedException ex)
        {
          //$FALL-THROUGH$
        }

        treeViewer.refresh();
      }
    });

    new Label(buttonComposite, SWT.NONE);

    showProfilesButton = new Button(buttonComposite, SWT.CHECK);
    showProfilesButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
    showProfilesButton.setText(Messages.AgentManagerComposite_showProfiles_text);
    showProfilesButton.setToolTipText(Messages.AgentManagerComposite_showProfiles_tooltip);
    showProfilesButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        boolean showProfiles = showProfilesButton.getSelection();
        PREF_SHOW_PROFILES.set(showProfiles);

        profileDetailsButton.setVisible(showProfiles);

        contentProvider.setShowProfiles(showProfiles);
        treeViewer.refresh();

        if (selectedElement instanceof BundlePool)
        {
          treeViewer.setExpandedState(selectedElement, true);
        }

        profilesShown(showProfiles);
      }
    });

    profileDetailsButton = new Button(buttonComposite, SWT.NONE);
    profileDetailsButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    profileDetailsButton.setText(Messages.AgentManagerComposite_profileDetails_text);
    profileDetailsButton.setToolTipText(Messages.AgentManagerComposite_profilesDetails_tooltip);
    profileDetailsButton.setVisible(false);
    profileDetailsButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        showProfileDetails();
      }
    });

    if (PREF_SHOW_PROFILES.get(false))
    {
      showProfilesButton.setSelection(true);
      profileDetailsButton.setVisible(true);
      contentProvider.setShowProfiles(true);
      profilesShown(true);
    }

    UIUtil.asyncExec(new Runnable()
    {
      @Override
      public void run()
      {
        final AgentManager agentManager = P2Util.getAgentManager();

        BusyIndicator.showWhile(getShell().getDisplay(), new Runnable()
        {
          @Override
          public void run()
          {
            treeViewer.setInput(agentManager);
            treeViewer.expandAll();
          }
        });

        if (selection == null)
        {
          if (treeViewer.getTree().isEnabled())
          {
            Collection<Agent> agents = agentManager.getAgents();
            if (!agents.isEmpty())
            {
              treeViewer.setSelection(new StructuredSelection(agents.iterator().next()));
            }
          }
        }
        else
        {
          treeViewer.setSelection(new StructuredSelection(selection));
        }
      }
    });
  }

  @Override
  public boolean setFocus()
  {
    return treeViewer.getTree().setFocus();
  }

  public BundlePool getSelectedBundlePool()
  {
    return selectedPool;
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    treeViewer.getTree().setEnabled(enabled);

    refreshButton.setEnabled(enabled);
    newAgentButton.setEnabled(enabled);
    showProfilesButton.setEnabled(enabled);

    if (enabled)
    {
      selectedElement = ((IStructuredSelection)treeViewer.getSelection()).getFirstElement();
    }
    else
    {
      selectedElement = null;
    }

    elementChanged(selectedElement);

    super.setEnabled(enabled);
  }

  private void deletePressed()
  {
    AgentManagerElementImpl agentManagerElement = (AgentManagerElementImpl)selectedElement;

    String message = getDeleteConfirmationMessage(agentManagerElement);

    if (MessageDialog.openQuestion(getShell(), AgentManagerDialog.TITLE, message))
    {
      try
      {
        Object newSelection = null;
        IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
        Object element = selection.getFirstElement();
        if (element != null)
        {
          ITreeContentProvider contentProvider = (ITreeContentProvider)treeViewer.getContentProvider();
          Object parent = contentProvider.getParent(element);
          if (parent != null)
          {
            newSelection = parent;

            Object[] elements = contentProvider.getChildren(parent);
            treeViewer.getComparator().sort(treeViewer, elements);
            List<Object> children = Arrays.asList(elements);
            int index = children.indexOf(element);
            if (index != -1)
            {
              if (index + 1 < children.size())
              {
                newSelection = children.get(index + 1);
              }
              else if (index > 0)
              {
                newSelection = children.get(index - 1);
              }
            }
          }
        }

        agentManagerElement.delete();
        treeViewer.refresh();

        if (newSelection != null)
        {
          treeViewer.setSelection(new StructuredSelection(newSelection));
        }
      }
      catch (Exception ex)
      {
        P2UIPlugin.INSTANCE.log(ex);
      }
    }
  }

  private String getDeleteConfirmationMessage(AgentManagerElementImpl agentManagerElement)
  {
    if (agentManagerElement instanceof Agent)
    {
      return NLS.bind(Messages.AgentManagerComposite_deleteAgent_confirmation, agentManagerElement) + "\n\n" //$NON-NLS-1$
          + Messages.AgentManagerComposite_deleteAgent_filesWillRemainOnDisk;
    }
    else if (agentManagerElement instanceof BundlePool)
    {
      return NLS.bind(Messages.AgentManagerComposite_deleteBundlePool_confirmation, agentManagerElement) + "\n\n" //$NON-NLS-1$
          + Messages.AgentManagerComposite_deleteBundlePool_filesWillRemainOnDisk;
    }
    else
    {
      return NLS.bind(Messages.AgentManagerComposite_deleteProfile_confirmation, agentManagerElement);
    }
  }

  private void cleanup(final Agent agent)
  {
    final String title = Messages.AgentManagerComposite_cleanup_title;
    final AgentAnalyzer[] analyzer = { null };

    try
    {
      ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
      dialog.run(true, true, new IRunnableWithProgress()
      {
        @Override
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          SubMonitor progress = SubMonitor.convert(monitor, Messages.AgentManagerComposite_cleanup_analyzing, 100).detectCancelation();

          analyzer[0] = new AgentAnalyzer(agent, false, null, progress.newChild(90));
          analyzer[0].awaitAnalyzing(progress.newChild(10));
        }
      });
    }
    catch (Exception ex)
    {
      ErrorDialog.open(ex);
      return;
    }

    final Map<AnalyzedProfile, AnalyzedProfile> unusedProfiles = new IdentityHashMap<>();
    final Map<AnalyzedArtifact, AnalyzedArtifact> unusedArtifacts = new IdentityHashMap<>();

    for (AnalyzedBundlePool bundlePool : analyzer[0].getBundlePools().values())
    {
      for (AnalyzedProfile profile : bundlePool.getUnusedProfiles())
      {
        unusedProfiles.put(profile, profile);
      }

      for (AnalyzedArtifact artifact : bundlePool.getArtifacts())
      {
        if (isUnused(artifact, unusedProfiles))
        {
          unusedArtifacts.put(artifact, artifact);
        }
      }
    }

    if (unusedProfiles.isEmpty() && unusedArtifacts.isEmpty())
    {
      MessageDialog.openInformation(getShell(), title, Messages.AgentManagerComposite_cleanup_nothingToCleanUp);
      return;
    }

    final boolean showProfiles = showProfilesButton.getSelection();
    final int profiles = unusedProfiles.size();
    final int artifacts = unusedArtifacts.size();

    String message = getCleanupConfirmationMessage(profiles, artifacts);
    if (artifacts != 0)
    {
      message += "\n\n" + Messages.AgentManagerComposite_cleanup_deleteConfirm_artifactDeleteNote; //$NON-NLS-1$
    }

    if (MessageDialog.openQuestion(getShell(), title, message))
    {
      try
      {
        UIUtil.runInProgressDialog(getShell(), new IRunnableWithProgress()
        {
          @Override
          public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
          {
            SubMonitor progress = SubMonitor.convert(monitor, Messages.AgentManagerComposite_cleanup_deleting, profiles + artifacts).detectCancelation();

            if (profiles != 0)
            {
              progress.setTaskName(Messages.AgentManagerComposite_cleanup_deletingUnusedProfiles);
              for (AnalyzedProfile profile : unusedProfiles.keySet())
              {
                profile.delete(progress.newChild());

                if (showProfiles)
                {
                  UIUtil.syncExec(new Runnable()
                  {
                    @Override
                    public void run()
                    {
                      treeViewer.refresh();
                    }
                  });
                }
              }
            }

            if (artifacts != 0)
            {
              progress.setTaskName(Messages.AgentManagerComposite_cleanup_deletingUnusedArtifacts);
              for (AnalyzedArtifact artifact : unusedArtifacts.keySet())
              {
                artifact.delete(progress.newChild());
              }
            }
          }
        });
      }
      catch (InvocationTargetException ex)
      {
        ErrorDialog.open(ex);
      }
      catch (InterruptedException ex)
      {
        // Ignore.
      }
    }
  }

  private String getCleanupConfirmationMessage(int profiles, int artifacts)
  {
    switch (artifacts)
    {
      case 0:
        switch (profiles)
        {
          case 0:
            return ""; //$NON-NLS-1$
          case 1:
            return Messages.AgentManagerComposite_cleanup_deleteConfirm_profile;
          default:
            return NLS.bind(Messages.AgentManagerComposite_cleanup_deleteConfirm_profiles, profiles);
        }
      case 1:
        switch (profiles)
        {
          case 0:
            return Messages.AgentManagerComposite_cleanup_deleteConfirm_artifact;
          case 1:
            return Messages.AgentManagerComposite_cleanup_deleteConfirm_profileAndArtifact;
          default:
            return NLS.bind(Messages.AgentManagerComposite_cleanup_deleteConfirm_profilesAndArtifact, profiles);
        }
      default:
        switch (profiles)
        {
          case 0:
            return NLS.bind(Messages.AgentManagerComposite_cleanup_deleteConfirm_artifacts, artifacts);
          case 1:
            return NLS.bind(Messages.AgentManagerComposite_cleanup_deleteConfirm_profileAndArtifacts, artifacts);
          default:
            return NLS.bind(Messages.AgentManagerComposite_cleanup_deleteConfirm_profilesAndArtifacts, profiles, artifacts);
        }
    }
  }

  private void analyze(Agent agent)
  {
    AgentAnalyzerDialog dialog = new AgentAnalyzerDialog(getShell(), agent);
    dialog.open();
  }

  protected void elementChanged(Object element)
  {
    newPoolButton.setEnabled(element instanceof Agent);
    deleteButton.setEnabled(element instanceof AgentManagerElement && !((AgentManagerElement)element).isUsed());

    if (selectPoolButton != null)
    {
      selectPoolButton.setEnabled(element instanceof BundlePool);
    }

    Agent agent = getAgent(element);
    cleanupButton.setEnabled(agent != null);
    clearButton.setEnabled(agent != null);
    analyzeButton.setEnabled(agent != null);

    profileDetailsButton.setEnabled(element instanceof Profile);
  }

  protected void profilesShown(boolean profilesShown)
  {
  }

  private void showProfileDetails()
  {
    ProfileDetailsDialog dialog = new ProfileDetailsDialog(getShell(), (Profile)selectedElement);
    dialog.open();
  }

  private String openDirectoryDialog(String message, String path)
  {
    DirectoryDialog dialog = new DirectoryDialog(getShell());
    dialog.setText(AgentManagerDialog.TITLE);
    dialog.setMessage(message);
    dialog.setFilterPath(path);
    return dialog.open();
  }

  private void refreshFor(BundlePool bundlePool)
  {
    treeViewer.refresh();
    treeViewer.setExpandedState(bundlePool.getAgent(), true);
    treeViewer.setSelection(new StructuredSelection(bundlePool));
    treeViewer.getTree().setFocus();
  }

  private static Agent getAgent(Object element)
  {
    if (element instanceof Profile)
    {
      Profile profile = (Profile)element;
      return profile.getAgent();
    }

    if (element instanceof BundlePool)
    {
      BundlePool bundlePool = (BundlePool)element;
      return bundlePool.getAgent();
    }

    if (element instanceof Agent)
    {
      return (Agent)element;
    }

    return null;
  }

  private static boolean isUnused(AnalyzedArtifact artifact, Map<AnalyzedProfile, AnalyzedProfile> unusedProfiles)
  {
    for (AnalyzedProfile profile : artifact.getProfiles())
    {
      if (!unusedProfiles.containsKey(profile))
      {
        return false;
      }
    }

    return true;
  }

  static void addDragSupport(StructuredViewer viewer)
  {
    viewer.addDragSupport(DND_OPERATIONS, DND_TRANSFERS, new GeneralDragAdapter(viewer, new GeneralDragAdapter.DraggedObjectsFactory()
    {
      @Override
      public List<Object> createDraggedObjects(ISelection selection) throws Exception
      {
        List<Object> result = new ArrayList<>();
        for (Object object : ((IStructuredSelection)selection).toArray())
        {
          if (object instanceof BundlePool)
          {
            addRepository((BundlePool)object, RepositoryType.ARTIFACT, result);
          }
          else if (object instanceof Profile)
          {
            addRepository((Profile)object, RepositoryType.METADATA, result);
          }
          else if (object instanceof AgentAnalyzer.AnalyzedProfile)
          {
            addRepository(((AnalyzedProfile)object).getP2Profile(), RepositoryType.METADATA, result);
          }
          else if (object instanceof AgentAnalyzer.AnalyzedArtifact)
          {
            AgentAnalyzer.AnalyzedArtifact analyzedArtifact = (AgentAnalyzer.AnalyzedArtifact)object;
            String id = analyzedArtifact.getID();
            String version = analyzedArtifact.getVersion();
            result.add(P2Factory.eINSTANCE.createRequirement(id, new VersionRange(version)));
          }
          else if (object instanceof EObject)
          {
            result.add(object);
          }
        }

        return result;
      }

      private void addRepository(AgentManagerElement element, RepositoryType repositoryType, List<Object> result)
      {
        File location = element.getLocation();
        if (location != null)
        {
          Repository repository = P2Factory.eINSTANCE.createRepository(URI.createFileURI(location.toString()).toString());
          repository.setType(repositoryType);
          result.add(repository);
        }
      }
    }, DND_DELEGATES));
  }
}
