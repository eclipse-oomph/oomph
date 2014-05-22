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
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.internal.core.AgentAnalyzer;
import org.eclipse.oomph.p2.internal.core.AgentAnalyzer.Artifact;
import org.eclipse.oomph.p2.internal.core.AgentAnalyzer.BundlePool;
import org.eclipse.oomph.p2.internal.core.AgentAnalyzer.Handler;
import org.eclipse.oomph.p2.internal.core.AgentAnalyzer.Profile;
import org.eclipse.oomph.ui.ErrorDialog;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ObjectUtil;
import org.eclipse.oomph.util.SubMonitor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class AgentAnalyzerComposite extends Composite
{
  private static final int TABLE_STYLE = SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.NO_SCROLL | SWT.V_SCROLL;

  private final Set<ISelectionProvider> changingSelection = new HashSet<ISelectionProvider>();

  private final Agent agent;

  private TableViewer bundlePoolViewer;

  private BundlePoolContentProvider bundlePoolContentProvider;

  private TableViewer artifactViewer;

  private ArtifactContentProvider artifactContentProvider;

  private TableViewer profileViewer;

  private ProfileContentProvider profileContentProvider;

  private Button selectAllArtifactsButton;

  private Button repairArtifactsButton;

  private Button deleteArtifactsButton;

  private AgentAnalyzer analyzer;

  private BundlePool currentBundlePool;

  public AgentAnalyzerComposite(final Composite parent, int margin, int style, Agent agent)
  {
    super(parent, style);
    this.agent = agent;

    GridLayout gridLayout = new GridLayout();
    gridLayout.marginWidth = margin;
    gridLayout.marginHeight = margin;
    setLayout(gridLayout);

    createBundlePoolViewer();

    SashForm verticalSashForm = new SashForm(this, SWT.SMOOTH | SWT.VERTICAL);
    verticalSashForm.setLayoutData(new GridData(GridData.FILL_BOTH));

    createArtifactViewer(verticalSashForm);
    createProfileViewer(verticalSashForm);

    verticalSashForm.setWeights(new int[] { 3, 1 });

    addDisposeListener(new DisposeListener()
    {
      public void widgetDisposed(DisposeEvent e)
      {
        if (analyzer != null)
        {
          analyzer.dispose();
        }
      }
    });

    setEnabled(false);
    getDisplay().asyncExec(new Runnable()
    {
      public void run()
      {
        initAnalyzer();
        setEnabled(true);

        bundlePoolContentProvider.setInput(bundlePoolViewer, analyzer);
      }
    });
  }

  private void createBundlePoolViewer()
  {
    Composite bundlePoolComposite = new Composite(this, SWT.NONE);
    GridLayout bundlePoolLayout = new GridLayout();
    bundlePoolLayout.marginWidth = 0;
    bundlePoolLayout.marginHeight = 0;
    bundlePoolComposite.setLayout(bundlePoolLayout);
    bundlePoolComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

    bundlePoolViewer = new TableViewer(bundlePoolComposite, TABLE_STYLE);
    Table bundlePoolTable = bundlePoolViewer.getTable();
    bundlePoolTable.setHeaderVisible(true);
    GridData bundlePoolData = new GridData(SWT.FILL, SWT.FILL, true, false);
    bundlePoolData.heightHint = 84;
    bundlePoolTable.setLayoutData(bundlePoolData);

    TableColumn bundlePoolColumn = new TableViewerColumn(bundlePoolViewer, SWT.NONE).getColumn();
    bundlePoolColumn.setText("Bundle Pool");
    bundlePoolColumn.setWidth(305);
    bundlePoolColumn.setResizable(false);

    TableColumn artifactsColumn = new TableViewerColumn(bundlePoolViewer, SWT.NONE).getColumn();
    artifactsColumn.setText("Artifacts");
    artifactsColumn.setAlignment(SWT.RIGHT);
    artifactsColumn.setWidth(63);

    TableColumn unusedArtifactsColumn = new TableViewerColumn(bundlePoolViewer, SWT.NONE).getColumn();
    unusedArtifactsColumn.setText("Unused Artifacts");
    unusedArtifactsColumn.setAlignment(SWT.RIGHT);
    unusedArtifactsColumn.setWidth(105);

    TableColumn damagedArtifactsColumn = new TableViewerColumn(bundlePoolViewer, SWT.NONE).getColumn();
    damagedArtifactsColumn.setText("Damaged Artifacts");
    damagedArtifactsColumn.setAlignment(SWT.RIGHT);
    damagedArtifactsColumn.setWidth(125);

    TableColumn profilesColumn = new TableViewerColumn(bundlePoolViewer, SWT.NONE).getColumn();
    profilesColumn.setText("Profiles");
    profilesColumn.setAlignment(SWT.RIGHT);
    profilesColumn.setWidth(55);

    bundlePoolContentProvider = new BundlePoolContentProvider();
    bundlePoolViewer.setContentProvider(bundlePoolContentProvider);
    bundlePoolViewer.setLabelProvider(new TableLabelProvider(getDisplay()));
    bundlePoolViewer.addSelectionChangedListener(new SelectionChangedListener()
    {
      @Override
      protected void doSelectionChanged(SelectionChangedEvent event)
      {
        currentBundlePool = (BundlePool)((IStructuredSelection)event.getSelection()).getFirstElement();

        artifactViewer.setSelection(StructuredSelection.EMPTY);
        artifactContentProvider.setInput(artifactViewer, currentBundlePool);

        profileViewer.setSelection(StructuredSelection.EMPTY);
        profileContentProvider.setInput(profileViewer, currentBundlePool);
      }
    });

    new Label(bundlePoolComposite, SWT.NONE);
    bundlePoolContentProvider.setInput(bundlePoolViewer, null); // trigger resizeColumns
  }

  private void createArtifactViewer(Composite parent)
  {
    Composite artifactComposite = new Composite(parent, SWT.NONE);
    GridLayout artifactLayout = new GridLayout();
    artifactLayout.marginWidth = 0;
    artifactLayout.marginHeight = 0;
    artifactComposite.setLayout(artifactLayout);

    artifactViewer = new TableViewer(artifactComposite, TABLE_STYLE | SWT.MULTI);
    Table artifactTable = artifactViewer.getTable();
    artifactTable.setHeaderVisible(true);
    artifactTable.setLayoutData(new GridData(GridData.FILL_BOTH));
    new SelectAllAdapter(artifactViewer);

    TableColumn artifactColumn = new TableViewerColumn(artifactViewer, SWT.NONE).getColumn();
    artifactColumn.setText("Artifact");
    artifactColumn.setWidth(365);
    artifactColumn.setResizable(false);

    TableColumn versionVersion = new TableViewerColumn(artifactViewer, SWT.NONE).getColumn();
    versionVersion.setText("Version");
    versionVersion.setWidth(205);

    TableColumn profilesColumn = new TableViewerColumn(artifactViewer, SWT.NONE).getColumn();
    profilesColumn.setText("Profiles");
    profilesColumn.setAlignment(SWT.RIGHT);
    profilesColumn.setWidth(56);

    artifactContentProvider = new ArtifactContentProvider();
    artifactViewer.setContentProvider(artifactContentProvider);
    artifactViewer.setLabelProvider(new TableLabelProvider(getDisplay()));
    artifactViewer.addSelectionChangedListener(new SelectionChangedListener()
    {
      @Override
      protected void doSelectionChanged(SelectionChangedEvent event)
      {
        updateArtifactButtons();
      }

      @Override
      protected void triggerOtherSelections(SelectionChangedEvent event)
      {
        if (!changingSelection.contains(profileViewer))
        {
          Set<Profile> profiles = new HashSet<Profile>();
          for (Artifact artifact : getSelectedArtifacts())
          {
            profiles.addAll(artifact.getProfiles());
          }

          profileViewer.setSelection(new StructuredSelection(profiles.toArray()));
        }
      }
    });

    Composite artifactButtonBar = new Composite(artifactComposite, SWT.NONE);
    artifactButtonBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    GridLayout artifactButtonBarLayout = new GridLayout(5, false);
    artifactButtonBarLayout.marginWidth = 0;
    artifactButtonBarLayout.marginHeight = 0;
    artifactButtonBar.setLayout(artifactButtonBarLayout);

    final Combo filterCombo = new Combo(artifactButtonBar, SWT.READ_ONLY);
    filterCombo.add(ArtifactContentProvider.SHOW_ALL);
    filterCombo.add(ArtifactContentProvider.SHOW_UNUSED);
    filterCombo.add(ArtifactContentProvider.SHOW_DAMAGED);
    filterCombo.select(0);
    filterCombo.pack();
    filterCombo.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        String selection = filterCombo.getText();
        artifactContentProvider.setFilter(selection);
      }
    });

    selectAllArtifactsButton = new Button(artifactButtonBar, SWT.NONE);
    selectAllArtifactsButton.setText("Select All");
    selectAllArtifactsButton.setEnabled(false);
    selectAllArtifactsButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        artifactViewer.setSelection(new StructuredSelection(currentBundlePool.getArtifacts()));
      }
    });

    new Label(artifactButtonBar, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

    repairArtifactsButton = new Button(artifactButtonBar, SWT.NONE);
    repairArtifactsButton.setText("Repair Selected");
    repairArtifactsButton.setEnabled(false);
    repairArtifactsButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Artifact[] artifacts = getSelectedArtifacts();
        repairArtifacts(artifacts);
      }
    });

    deleteArtifactsButton = new Button(artifactButtonBar, SWT.NONE);
    deleteArtifactsButton.setText("Delete Selected...");
    deleteArtifactsButton.setEnabled(false);
    deleteArtifactsButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Artifact[] artifacts = getSelectedArtifacts();
        int len = artifacts.length;
        String message = "Do you really want to delete " + len + " artifact" + (len == 1 ? "" : "s") + "?\n\n"
            + "Note 1: Unused artifacts can always safely be deleted. "
            + "They will be deleted physically from your disk and logically from your bundle pool.\n\n"
            + "Note 2: Artifacts used in your profiles will be deleted only physically from your disk. "
            + "Repairing them is possible if they are still available for download from a p2 repository.";

        if (MessageDialog.openQuestion(getShell(), AgentAnalyzerDialog.TITLE, message))
        {
          deleteArtifacts(artifacts);
        }
      }
    });

    new Label(artifactComposite, SWT.NONE);
    artifactContentProvider.setInput(artifactViewer, null); // trigger resizeColumns
  }

  private void createProfileViewer(Composite parent)
  {
    Composite profileComposite = new Composite(parent, SWT.NONE);
    GridLayout profileLayout = new GridLayout();
    profileLayout.marginWidth = 0;
    profileLayout.marginHeight = 0;
    profileComposite.setLayout(profileLayout);

    profileViewer = new TableViewer(profileComposite, TABLE_STYLE | SWT.MULTI);
    Table profileTable = profileViewer.getTable();
    profileTable.setHeaderVisible(true);
    profileTable.setLayoutData(new GridData(GridData.FILL_BOTH));
    new SelectAllAdapter(profileViewer);

    TableColumn profileColumn = new TableViewerColumn(profileViewer, SWT.NONE).getColumn();
    profileColumn.setText("Profile");
    profileColumn.setWidth(447);
    profileColumn.setResizable(false);

    TableColumn artifactsColumn = new TableViewerColumn(profileViewer, SWT.NONE).getColumn();
    artifactsColumn.setText("Artifacts");
    artifactsColumn.setAlignment(SWT.RIGHT);
    artifactsColumn.setWidth(62);

    TableColumn damagedArtifactsColumn = new TableViewerColumn(profileViewer, SWT.NONE).getColumn();
    damagedArtifactsColumn.setText("Damaged Artifacts");
    damagedArtifactsColumn.setAlignment(SWT.RIGHT);
    damagedArtifactsColumn.setWidth(120);

    TableColumn rootsColumn = new TableViewerColumn(profileViewer, SWT.NONE).getColumn();
    rootsColumn.setText("Roots");
    rootsColumn.setAlignment(SWT.RIGHT);
    rootsColumn.setWidth(50);

    TableColumn repositoriesColumn = new TableViewerColumn(profileViewer, SWT.NONE).getColumn();
    repositoriesColumn.setText("Repositories");
    repositoriesColumn.setAlignment(SWT.RIGHT);
    repositoriesColumn.setWidth(82);

    profileContentProvider = new ProfileContentProvider();
    profileViewer.setContentProvider(profileContentProvider);
    profileViewer.setLabelProvider(new TableLabelProvider(getDisplay()));
    profileViewer.addSelectionChangedListener(new SelectionChangedListener()
    {
      @Override
      protected void doSelectionChanged(SelectionChangedEvent event)
      {
        // Do nothing
      }

      @Override
      protected void triggerOtherSelections(SelectionChangedEvent event)
      {
        if (!changingSelection.contains(artifactViewer))
        {
          Set<Artifact> artifacts = new HashSet<Artifact>();
          for (Profile profile : getSelectedProfiles())
          {
            artifacts.addAll(profile.getArtifacts());
          }

          artifactViewer.setSelection(new StructuredSelection(new ArrayList<Artifact>(artifacts)));
        }
      }
    });

    profileContentProvider.setInput(profileViewer, null); // trigger resizeColumns
  }

  @Override
  protected void checkSubclass()
  {
    // Disable the check that prevents subclassing of SWT components
  }

  private void initAnalyzer()
  {
    analyzer = new AgentAnalyzer(agent, new Handler()
    {
      public void analyzerChanged(final AgentAnalyzer analyzer)
      {
        if (analyzer == AgentAnalyzerComposite.this.analyzer)
        {
          asyncExec(new Runnable()
          {
            public void run()
            {
              bundlePoolContentProvider.refresh();
              artifactContentProvider.refresh();
              profileContentProvider.refresh();

              getDisplay().asyncExec(new Runnable()
              {
                public void run()
                {
                  Object[] elements = bundlePoolContentProvider.getElements(analyzer);
                  if (elements.length != 0)
                  {
                    bundlePoolViewer.setSelection(new StructuredSelection(elements[0]));
                  }
                }
              });
            }
          });
        }
      }

      public void bundlePoolChanged(final BundlePool bundlePool, final boolean artifacts, final boolean profiles)
      {
        asyncExec(new Runnable()
        {
          public void run()
          {
            bundlePoolViewer.update(bundlePool, null);

            if (bundlePool == currentBundlePool)
            {
              if (artifacts)
              {
                artifactContentProvider.refresh();
              }

              if (profiles)
              {
                profileContentProvider.refresh();
              }
            }
          }
        });
      }

      public void profileChanged(final Profile profile)
      {
        if (profile.getBundlePool() == currentBundlePool)
        {
          asyncExec(new Runnable()
          {
            public void run()
            {
              profileViewer.update(profile, null);
            }
          });
        }
      }

      public void artifactChanged(final Artifact artifact)
      {
        if (artifact.getBundlePool() == currentBundlePool)
        {
          asyncExec(new Runnable()
          {
            public void run()
            {
              if (ObjectUtil.equals(artifactContentProvider.getFilter(), TableContentProvider.SHOW_ALL))
              {
                artifactViewer.update(artifact, null);
              }
              else
              {
                artifactContentProvider.refresh();
              }
            }
          });
        }
      }
    });
  }

  private void asyncExec(Runnable runnable)
  {
    if (!isDisposed())
    {
      Display display = getDisplay();
      UIUtil.asyncExec(display, runnable);
    }
  }

  private Artifact[] getSelectedArtifacts()
  {
    IStructuredSelection selection = (IStructuredSelection)artifactViewer.getSelection();

    @SuppressWarnings("unchecked")
    List<Artifact> artifacts = (List<Artifact>)(List<?>)selection.toList();
    return artifacts.toArray(new Artifact[artifacts.size()]);
  }

  private Profile[] getSelectedProfiles()
  {
    IStructuredSelection selection = (IStructuredSelection)profileViewer.getSelection();

    @SuppressWarnings("unchecked")
    List<Profile> profiles = (List<Profile>)(List<?>)selection.toList();
    return profiles.toArray(new Profile[profiles.size()]);
  }

  private boolean updateButton(Button button, String text, int count, String suffix)
  {
    button.setEnabled(count != 0);
    if (count != 0)
    {
      text += " " + count;
    }

    text += suffix;

    if (!button.getText().equals(text))
    {
      button.setText(text);
      return true;
    }

    return false;
  }

  private void updateArtifactButtons()
  {
    boolean changed = false;

    if (currentBundlePool != null)
    {
      Object[] elements = artifactContentProvider.getElements(currentBundlePool);
      changed |= updateButton(selectAllArtifactsButton, "Select All", elements.length, "");
    }

    Artifact[] artifacts = getSelectedArtifacts();
    int count = artifacts.length;

    changed |= updateButton(deleteArtifactsButton, "Delete", count, " Selected...");
    changed |= updateButton(repairArtifactsButton, "Repair", count, " Selected");

    if (changed)
    {
      Composite parent = repairArtifactsButton.getParent();
      parent.pack();
      parent.getParent().layout();
    }
  }

  private void deleteArtifacts(final Artifact[] artifacts)
  {
    try
    {
      UIUtil.runInProgressDialog(getShell(), new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          SubMonitor progress = SubMonitor.convert(monitor, "Deleting artifacts", artifacts.length).detectCancelation();

          for (Artifact artifact : artifacts)
          {
            artifact.delete(progress.newChild());
          }
        }
      });

      artifactContentProvider.refresh();
      artifactViewer.setSelection(StructuredSelection.EMPTY);
    }
    catch (InvocationTargetException ex)
    {
      ErrorDialog.open(ex);
    }
    catch (InterruptedException ex)
    {
      throw new OperationCanceledException();
    }
  }

  private void repairArtifacts(final Artifact[] artifacts)
  {
    try
    {
      final List<Artifact> remainingArtifacts = new ArrayList<Artifact>();

      UIUtil.runInProgressDialog(getShell(), new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
        {
          SubMonitor progress = SubMonitor.convert(monitor, Artifact.REPAIR_TASK_NAME, artifacts.length).detectCancelation();

          for (Artifact artifact : artifacts)
          {
            if (!artifact.repair(null, progress.newChild()))
            {
              remainingArtifacts.add(artifact);
            }
          }
        }
      });

      artifactContentProvider.refresh();
      artifactViewer.setSelection(StructuredSelection.EMPTY);

      boolean firstTime = true;
      Set<URI> repositories = new HashSet<URI>(analyzer.getRepositoryURIs());

      while (!remainingArtifacts.isEmpty())
      {
        AdditionalURIPrompterDialog dialog = new AdditionalURIPrompterDialog(getShell(), firstTime, remainingArtifacts, repositories);
        if (dialog.open() == AdditionalURIPrompterDialog.CANCEL)
        {
          break;
        }

        firstTime = false;
        analyzer.getRepositoryURIs().addAll(repositories);

        final Set<URI> checkedRepositories = dialog.getCheckedRepositories();
        UIUtil.runInProgressDialog(getShell(), new IRunnableWithProgress()
        {
          public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
          {
            SubMonitor progress = SubMonitor.convert(monitor, Artifact.REPAIR_TASK_NAME, artifacts.length).detectCancelation();

            for (Iterator<Artifact> it = remainingArtifacts.iterator(); it.hasNext();)
            {
              Artifact artifact = it.next();
              if (artifact.repair(checkedRepositories, progress.newChild()))
              {
                it.remove();
              }
            }
          }
        });

        repositories.removeAll(checkedRepositories);
        artifactContentProvider.refresh();
      }
    }
    catch (InvocationTargetException ex)
    {
      ErrorDialog.open(ex);
    }
    catch (InterruptedException ex)
    {
      throw new OperationCanceledException();
    }
  }

  /**
   * @author Eike Stepper
   */
  private abstract class SelectionChangedListener implements ISelectionChangedListener
  {
    public final void selectionChanged(SelectionChangedEvent event)
    {
      doSelectionChanged(event);

      if (changingSelection.add(event.getSelectionProvider()))
      {
        try
        {
          triggerOtherSelections(event);
        }
        finally
        {
          changingSelection.remove(event.getSelectionProvider());
        }
      }
    }

    protected abstract void doSelectionChanged(SelectionChangedEvent event);

    protected void triggerOtherSelections(SelectionChangedEvent event)
    {
      // Subclasses may override
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class SelectAllAdapter extends KeyAdapter
  {
    private final StructuredViewer viewer;

    public SelectAllAdapter(StructuredViewer viewer)
    {
      this.viewer = viewer;
      viewer.getControl().addKeyListener(this);
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
      if ((e.stateMask & SWT.CONTROL) != 0 && e.keyCode == 'a')
      {
        IStructuredContentProvider contentProvider = (IStructuredContentProvider)viewer.getContentProvider();
        viewer.setSelection(new StructuredSelection(contentProvider.getElements(viewer.getInput())));
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  public static final class TableLabelProvider extends LabelProvider implements ITableLabelProvider, IColorProvider
  {
    private final Color gray;

    private boolean singleColumn;

    public TableLabelProvider(Display display, boolean singleColumn)
    {
      this.singleColumn = singleColumn;
      gray = display.getSystemColor(SWT.COLOR_DARK_GRAY);
    }

    public TableLabelProvider(Display display)
    {
      this(display, false);
    }

    public String getColumnText(Object element, int columnIndex)
    {
      if (element instanceof BundlePool)
      {
        BundlePool bundlePool = (BundlePool)element;
        switch (columnIndex)
        {
          case 0:
            return bundlePool.getLocation().getAbsolutePath();
          case 1:
            return Integer.toString(bundlePool.getArtifactCount());
          case 2:
            return Integer.toString(bundlePool.getUnusedArtifactsCount());
          case 3:
            int percent = bundlePool.getDamagedArtifactsPercent();
            return Integer.toString(bundlePool.getDamagedArtifactsCount()) + (percent == 100 ? "" : " (" + percent + "%)");
          case 4:
            return Integer.toString(bundlePool.getProfilesCount());
        }
      }
      else if (element instanceof Artifact)
      {
        Artifact artifact = (Artifact)element;
        switch (columnIndex)
        {
          case 0:
            if (singleColumn)
            {
              return artifact.getID() + " " + artifact.getVersion();
            }

            return artifact.getID();
          case 1:
            return artifact.getVersion();
          case 2:
            return Integer.toString(artifact.getProfiles().size());
        }
      }
      else if (element instanceof Profile)
      {
        Profile profile = (Profile)element;
        switch (columnIndex)
        {
          case 0:
            return profile.getID();
          case 1:
            return Integer.toString(profile.getArtifacts().size());
          case 2:
            return Integer.toString(profile.getDamagedArtifactsCount());
          case 3:
            return Integer.toString(0);
          case 4:
            return Integer.toString(profile.getRepositoryURIs().size());
        }
      }

      return String.valueOf(element);
    }

    public Image getColumnImage(Object element, int columnIndex)
    {
      if (columnIndex == 0)
      {
        if (element instanceof BundlePool)
        {
          BundlePool bundlePool = (BundlePool)element;
          String key = "bundlePool";
          if (bundlePool.getDamagedArtifactsCount() != 0)
          {
            key += "Damaged";
          }

          return getPluginImage(key);
        }

        if (element instanceof Artifact)
        {
          Artifact artifact = (Artifact)element;
          String key = "artifact" + artifact.getType();
          if (artifact.isDamaged())
          {
            key += "Damaged";
          }

          return getPluginImage(key);
        }

        if (element instanceof Profile)
        {
          Profile profile = (Profile)element;
          String key = "profile" + profile.getType();
          if (profile.isDamaged())
          {
            key += "Damaged";
          }

          return getPluginImage(key);
        }

        if (element instanceof URI)
        {
          return getPluginImage("repository");
        }
      }

      return null;
    }

    public Color getForeground(Object element)
    {
      if (element instanceof Artifact)
      {
        Artifact artifact = (Artifact)element;
        if (artifact.isUnused())
        {
          return gray;
        }
      }

      return null;
    }

    public Color getBackground(Object element)
    {
      return null;
    }

    static Set<String> keys = new HashSet<String>();

    private static Image getPluginImage(String key)
    {
      return P2UIPlugin.INSTANCE.getSWTImage("obj16/" + key + "");
    }
  }

  /**
   * @author Eike Stepper
   */
  private static abstract class TableContentProvider extends ControlAdapter implements IStructuredContentProvider, ILazyContentProvider
  {
    public static final String SHOW_ALL = "Show All";

    private static final int FIRST_TIME = -1;

    private final ControlListener columnListener = new ControlAdapter()
    {
      @Override
      public void controlResized(ControlEvent e)
      {
        resizeColumns(true);
      }
    };

    private TableViewer tableViewer;

    private Object input;

    private String filter = SHOW_ALL;

    private int lastWidth = FIRST_TIME;

    public void setInput(TableViewer viewer, Object input)
    {
      if (tableViewer == null)
      {
        tableViewer = viewer;
        tableViewer.getTable().addControlListener(this);
      }

      if (input != null)
      {
        tableViewer.setInput(input);
        refresh();
      }

      ScrollBar verticalBar = tableViewer.getTable().getVerticalBar();
      if (verticalBar != null)
      {
        verticalBar.setSelection(verticalBar.getMinimum());
      }

      resizeColumns();
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
      input = newInput;
    }

    public void dispose()
    {
      input = null;
      tableViewer = null;
    }

    public void updateElement(int index)
    {
      Object[] elements = getElements(input);
      Object element = index < elements.length ? elements[index] : null;
      tableViewer.replace(element, index);
    }

    public void refresh()
    {
      if (tableViewer != null && input != null)
      {
        tableViewer.refresh();
        tableViewer.setItemCount(getElements(input).length);
      }
    }

    public String getFilter()
    {
      return filter;
    }

    public void setFilter(String filter)
    {
      this.filter = filter;
      UIUtil.asyncExec(tableViewer.getTable().getDisplay(), new Runnable()
      {
        public void run()
        {
          tableViewer.setSelection(StructuredSelection.EMPTY);
          refresh();
          resizeColumns();
        }
      });
    }

    @Override
    public void controlResized(ControlEvent e)
    {
      resizeColumns(false);
    }

    public void resizeColumns()
    {
      Table table = tableViewer.getTable();
      if (!table.isDisposed())
      {
        UIUtil.asyncExec(table.getDisplay(), new Runnable()
        {
          public void run()
          {
            resizeColumns(true);
          }
        });
      }
    }

    private void resizeColumns(boolean force)
    {
      Table table = tableViewer.getTable();
      int tableWidth = table.getSize().x;
      if (force || tableWidth != lastWidth)
      {
        boolean firstTime = lastWidth == FIRST_TIME;
        lastWidth = tableWidth;

        ScrollBar bar = table.getVerticalBar();
        if (bar != null && bar.isVisible())
        {
          tableWidth -= bar.getSize().x;
        }

        final TableColumn[] columns = table.getColumns();
        for (int i = 1; i < columns.length; i++)
        {
          TableColumn column = columns[i];
          tableWidth -= column.getWidth();

          if (firstTime)
          {
            column.addControlListener(columnListener);
          }
        }

        columns[0].setWidth(tableWidth);
      }
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class BundlePoolContentProvider extends TableContentProvider
  {
    public Object[] getElements(Object input)
    {
      Map<File, BundlePool> map = ((AgentAnalyzer)input).getBundlePools();
      BundlePool[] bundlePools = map.values().toArray(new BundlePool[map.size()]);
      Arrays.sort(bundlePools);
      return bundlePools;
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class ArtifactContentProvider extends TableContentProvider
  {
    public static final String SHOW_UNUSED = "Unused";

    public static final String SHOW_DAMAGED = "Damaged";

    public Object[] getElements(Object input)
    {
      String filter = getFilter();
      if (ObjectUtil.equals(filter, SHOW_UNUSED))
      {
        return ((BundlePool)input).getUnusedArtifacts();
      }
      else if (ObjectUtil.equals(filter, SHOW_DAMAGED))
      {
        return ((BundlePool)input).getDamagedArtifacts();
      }

      return ((BundlePool)input).getArtifacts();
    }

    @Override
    public void refresh()
    {
      super.refresh();
      updateArtifactButtons();
    }
  }

  /**
   * @author Eike Stepper
   */
  private final class ProfileContentProvider extends TableContentProvider
  {
    public Object[] getElements(Object input)
    {
      return ((BundlePool)input).getProfiles();
    }
  }
}
