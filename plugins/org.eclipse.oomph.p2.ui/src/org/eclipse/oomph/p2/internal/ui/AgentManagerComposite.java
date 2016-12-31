/*
 * Copyright (c) 2014-2016 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.internal.ui.GeneralDragAdapter;
import org.eclipse.oomph.internal.ui.OomphTransferDelegate;
import org.eclipse.oomph.p2.P2Factory;
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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
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
  private static final Preference PREF_SHOW_PROFILES = P2UIPlugin.INSTANCE.getConfigurationPreference("showProfiles");

  private static final int DND_OPERATIONS = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;

  private static final List<? extends OomphTransferDelegate> DND_DELEGATES = Collections.singletonList(new OomphTransferDelegate.TextTransferDelegate());

  private static final Transfer[] DND_TRANSFERS = new Transfer[] { DND_DELEGATES.get(0).getTransfer() };

  private TreeViewer treeViewer;

  private Object selectedElement;

  private Button refreshButton;

  private Button newAgentButton;

  private Button newPoolButton;

  private Button deleteButton;

  private Button cleanupButton;

  private Button analyzeButton;

  private Button showProfilesButton;

  private Button profileDetailsButton;

  public AgentManagerComposite(Composite parent, int style, final Object selection)
  {
    super(parent, style);
    setBackground(getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
    setLayout(UIUtil.createGridLayout(2));

    final P2ContentProvider contentProvider = new P2ContentProvider();

    treeViewer = new TreeViewer(this, SWT.BORDER);
    treeViewer.setContentProvider(contentProvider);
    treeViewer.setLabelProvider(new P2LabelProvider());
    treeViewer.setComparator(new P2ViewerSorter());
    treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
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
    newAgentButton.setText("New Agent...");
    newAgentButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        String path = openDirectoryDialog("Select the location of the new agent.", PropertiesUtil.getUserHome());
        if (path != null)
        {
          Agent agent = P2Util.getAgentManager().addAgent(new File(path));
          BundlePool bundlePool = agent.addBundlePool(new File(path, BundlePool.DEFAULT_NAME));
          refreshFor(bundlePool);
        }
      }
    });

    cleanupButton = new Button(buttonComposite, SWT.NONE);
    cleanupButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    cleanupButton.setText("Cleanup Agent...");
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
    analyzeButton.setText("Analyze Agent...");
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
    newPoolButton.setText("New Bundle Pool...");
    newPoolButton.setEnabled(false);
    newPoolButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Agent selectedAgent = (Agent)selectedElement;
        String path = openDirectoryDialog("Select the location of the new pool.", selectedAgent.getLocation().getAbsolutePath());
        if (path != null)
        {
          BundlePool bundlePool = selectedAgent.addBundlePool(new File(path));
          refreshFor(bundlePool);
        }
      }
    });

    deleteButton = new Button(buttonComposite, SWT.NONE);
    deleteButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
    deleteButton.setText("Delete...");
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
    refreshButton.setText("Refresh");
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
    showProfilesButton.setText("Show Profiles");
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
    profileDetailsButton.setText("Details...");
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
      public void run()
      {
        final AgentManager agentManager = P2Util.getAgentManager();

        BusyIndicator.showWhile(getShell().getDisplay(), new Runnable()
        {
          public void run()
          {
            treeViewer.setInput(agentManager);
            treeViewer.expandAll();
          }
        });

        if (selection == null)
        {
          Collection<Agent> agents = agentManager.getAgents();
          if (!agents.isEmpty())
          {
            treeViewer.setSelection(new StructuredSelection(agents.iterator().next()));
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

  public Object getSelectedElement()
  {
    return selectedElement;
  }

  @Override
  public void setEnabled(boolean enabled)
  {
    treeViewer.getTree().setEnabled(enabled);

    refreshButton.setEnabled(enabled);
    newAgentButton.setEnabled(enabled);
    showProfilesButton.setEnabled(enabled);

    elementChanged(selectedElement);

    super.setEnabled(enabled);
  }

  private void deletePressed()
  {
    AgentManagerElementImpl agentManagerElement = (AgentManagerElementImpl)selectedElement;
    String type = agentManagerElement.getElementType();

    String message = "Are you sure to delete " + type + " " + agentManagerElement + "?";
    if (!(agentManagerElement instanceof Profile))
    {
      message += "\n\nThe physical " + type + " files will remain on disk even if you answer Yes.";
    }

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

  private void cleanup(final Agent agent)
  {
    final String title = "Cleanup";
    final AgentAnalyzer[] analyzer = { null };

    try
    {
      ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
      dialog.run(true, true, new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          SubMonitor progress = SubMonitor.convert(monitor, "Analyzing...", 100).detectCancelation();

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

    final Map<AnalyzedProfile, AnalyzedProfile> unusedProfiles = new IdentityHashMap<AnalyzedProfile, AnalyzedProfile>();
    final Map<AnalyzedArtifact, AnalyzedArtifact> unusedArtifacts = new IdentityHashMap<AnalyzedArtifact, AnalyzedArtifact>();

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
      MessageDialog.openInformation(getShell(), title, "Nothing to clean up.");
      return;
    }

    final boolean showProfiles = showProfilesButton.getSelection();
    final int profiles = unusedProfiles.size();
    final int artifacts = unusedArtifacts.size();
    String message = "Do you want to delete ";

    if (profiles != 0)
    {
      message += profiles + " unused profile" + (profiles == 1 ? "" : "s");
    }

    if (artifacts != 0)
    {
      if (profiles != 0)
      {
        message += " and ";
      }

      message += artifacts + " unused artifact" + (artifacts == 1 ? "" : "s");
    }

    message += "?";

    if (artifacts != 0)
    {
      message += "\n\n" + "Note: Unused artifacts can always safely be deleted. "
          + "They will be deleted physically from your disk and logically from your bundle pool.";
    }

    if (MessageDialog.openQuestion(getShell(), title, message))
    {
      try
      {
        UIUtil.runInProgressDialog(getShell(), new IRunnableWithProgress()
        {
          public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
          {
            SubMonitor progress = SubMonitor.convert(monitor, "Deleting...", profiles + artifacts).detectCancelation();

            if (profiles != 0)
            {
              progress.setTaskName("Deleting unused profiles...");
              for (AnalyzedProfile profile : unusedProfiles.keySet())
              {
                profile.delete(progress.newChild());

                if (showProfiles)
                {
                  UIUtil.syncExec(new Runnable()
                  {
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
              progress.setTaskName("Deleting unused artifacts...");
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

  private void analyze(Agent agent)
  {
    AgentAnalyzerDialog dialog = new AgentAnalyzerDialog(getShell(), agent);
    dialog.open();
  }

  protected void elementChanged(Object element)
  {
    newPoolButton.setEnabled(element instanceof Agent);
    deleteButton.setEnabled(element instanceof AgentManagerElement && !((AgentManagerElement)element).isUsed());

    Agent agent = getAgent(element);
    cleanupButton.setEnabled(agent != null);
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
      public List<Object> createDraggedObjects(ISelection selection) throws Exception
      {
        List<Object> result = new ArrayList<Object>();
        for (Object object : ((IStructuredSelection)selection).toArray())
        {
          if (object instanceof AgentAnalyzer.AnalyzedProfile)
          {
            AgentAnalyzer.AnalyzedProfile analyzedProfile = (AnalyzedProfile)object;
            object = analyzedProfile.getP2Profile();
          }

          if (object instanceof Profile)
          {
            Profile profile = (Profile)object;
            File location = profile.getLocation();
            if (location != null)
            {
              result.add(P2Factory.eINSTANCE.createRepository(URI.createFileURI(location.toString()).toString()));
            }
          }
          else if (object instanceof AgentAnalyzer.AnalyzedProfile)
          {
            AgentAnalyzer.AnalyzedProfile analyzedProfile = (AnalyzedProfile)object;
            analyzedProfile.getP2Profile();
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
    }, DND_DELEGATES));
  }
}
