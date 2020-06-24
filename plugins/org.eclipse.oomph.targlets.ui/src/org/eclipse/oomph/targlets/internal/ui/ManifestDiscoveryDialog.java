/*
 * Copyright (c) 2017 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.targlets.internal.ui;

import org.eclipse.oomph.p2.P2Factory;
import org.eclipse.oomph.p2.Repository;
import org.eclipse.oomph.p2.RepositoryList;
import org.eclipse.oomph.p2.Requirement;
import org.eclipse.oomph.p2.VersionSegment;
import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.P2Util;
import org.eclipse.oomph.p2.core.RepositoryProviderMap;
import org.eclipse.oomph.p2.internal.ui.P2UIPlugin;
import org.eclipse.oomph.targlets.Targlet;
import org.eclipse.oomph.targlets.core.ITargletContainer;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.IProvidedCapability;
import org.eclipse.equinox.p2.metadata.Version;
import org.eclipse.equinox.p2.metadata.VersionRange;
import org.eclipse.equinox.p2.query.CompoundQueryable;
import org.eclipse.equinox.p2.query.IQueryable;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.pde.internal.ui.IPDEUIConstants;
import org.eclipse.pde.internal.ui.PDEUIMessages;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredItemsSelectionDialog;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class ManifestDiscoveryDialog extends FilteredItemsSelectionDialog
{
  private static final String DIALOG_SETTINGS = "org.eclipse.oomph.targlets.ui.PluginSelectionDialog"; //$NON-NLS-1$

  private final ILabelProvider labelProvider;

  private final String namespace;

  private final List<ITargletContainer> targletContainers;

  public ManifestDiscoveryDialog(Shell parentShell, String namespace, List<ITargletContainer> targletContainers, boolean multipleSelection)
  {
    super(parentShell, multipleSelection);
    this.namespace = namespace;
    this.targletContainers = targletContainers;

    if ("org.eclipse.update.feature".equals(namespace)) //$NON-NLS-1$
    {
      setTitle(Messages.ManifestDiscoveryDialog_title_featureDiscovery);
      setMessage(PDEUIMessages.FeatureBlock_SelectFeatures);
    }
    else if ("java.package".equals(namespace)) //$NON-NLS-1$
    {
      setTitle(Messages.ManifestDiscoveryDialog_title_packageDiscovery);
      setMessage(PDEUIMessages.ImportPackageSection_exported);
    }
    else
    {
      setTitle(Messages.ManifestDiscoveryDialog_title_pluginDiscovery);
      setMessage(PDEUIMessages.PluginSelectionDialog_message);
    }

    labelProvider = new ManifestDiscoveryLabelProvider(namespace);
    setListLabelProvider(labelProvider);
  }

  @Override
  public boolean close()
  {
    labelProvider.dispose();
    return super.close();
  }

  @Override
  protected Control createExtendedContentArea(Composite parent)
  {
    return null;
  }

  @Override
  protected ItemsFilter createFilter()
  {
    return new ManifestDiscoveryItemsFilter();
  }

  @Override
  protected void fillContentProvider(AbstractContentProvider contentProvider, ItemsFilter itemsFilter, IProgressMonitor monitor) throws CoreException
  {
    Agent agent = P2Util.getAgentManager().getCurrentAgent();
    IMetadataRepositoryManager repositoryManager = agent.getMetadataRepositoryManager();
    RepositoryProviderMap.Metadata repositoryProviderMap = new RepositoryProviderMap.Metadata(repositoryManager);

    try
    {
      Set<String> urls = new HashSet<String>();
      for (ITargletContainer targletContainer : targletContainers)
      {
        for (Targlet targlet : targletContainer.getTarglets())
        {
          RepositoryList activeRepositoryList = targlet.getActiveRepositoryList();
          if (activeRepositoryList != null)
          {
            for (Repository repository : activeRepositoryList.getRepositories())
            {
              try
              {
                String url = repository.getURL();
                urls.add(url);
              }
              catch (Exception ex)
              {
                TargletsUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
                continue;
              }
            }
          }
        }
      }

      SubMonitor progress = SubMonitor.convert(monitor, 10 * urls.size() + 1);

      List<IQueryable<IInstallableUnit>> queryables = new ArrayList<IQueryable<IInstallableUnit>>();
      for (String url : urls)
      {
        try
        {
          IMetadataRepository queryable = repositoryProviderMap.getRepository(new URI(url), progress.newChild(10));
          queryables.add(queryable);
        }
        catch (Exception ex)
        {
          TargletsUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
          continue;
        }
      }

      for (ITargletContainer targletContainer : targletContainers)
      {
        for (Targlet targlet : targletContainer.getTarglets())
        {
          RepositoryList activeRepositoryList = targlet.getActiveRepositoryList();
          if (activeRepositoryList != null)
          {
            for (Repository repository : activeRepositoryList.getRepositories())
            {
              String url = repository.getURL();
              if (urls.add(url))
              {
                try
                {
                  IMetadataRepository queryable = repositoryProviderMap.getRepository(new URI(url));
                  queryables.add(queryable);
                }
                catch (URISyntaxException ex)
                {
                  TargletsUIPlugin.INSTANCE.log(ex, IStatus.WARNING);
                  continue;
                }
              }
            }
          }
        }
      }

      @SuppressWarnings("unchecked")
      IQueryable<IInstallableUnit>[] array = queryables.toArray(new IQueryable[queryables.size()]);
      CompoundQueryable<IInstallableUnit> compoundQueryable = new CompoundQueryable<IInstallableUnit>(array);

      for (IInstallableUnit iu : P2Util.asIterable(compoundQueryable.query(QueryUtil.createLatestIUQuery(), progress.newChild(1))))
      {
        if (!iu.getId().endsWith(".source")) //$NON-NLS-1$
        {
          for (IProvidedCapability capability : iu.getProvidedCapabilities())
          {
            if (namespace.equals(capability.getNamespace()))
            {
              String name = capability.getName();
              VersionRange versionRange = P2Factory.eINSTANCE.createVersionRange(capability.getVersion(), VersionSegment.QUALIFIER, false);

              Requirement requirement = P2Factory.eINSTANCE.createRequirement();
              requirement.setNamespace(namespace);
              requirement.setName(name);
              requirement.setVersionRange(versionRange);

              contentProvider.add(requirement, itemsFilter);
            }
          }
        }
      }
    }
    finally
    {
      repositoryProviderMap.dispose();
      monitor.done();
    }
  }

  @Override
  protected IDialogSettings getDialogSettings()
  {
    return TargletsUIPlugin.INSTANCE.getDialogSettings(DIALOG_SETTINGS);
  }

  @Override
  public String getElementName(Object item)
  {
    return ((Requirement)item).getName();
  }

  @Override
  protected Comparator<?> getItemsComparator()
  {
    return new ManifestDiscoveryComparator();
  }

  @Override
  protected IStatus validateItem(Object item)
  {
    return new Status(IStatus.OK, IPDEUIConstants.PLUGIN_ID, 0, "", null); //$NON-NLS-1$
  }

  /**
   * @author Eike Stepper
   */
  private final class ManifestDiscoveryItemsFilter extends ItemsFilter
  {
    public ManifestDiscoveryItemsFilter()
    {
      String pattern = patternMatcher.getPattern();
      if (pattern.indexOf("*") != 0 && pattern.indexOf("?") != 0 && pattern.indexOf(".") != 0) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      {
        pattern = "*" + pattern; //$NON-NLS-1$
        patternMatcher.setPattern(pattern);
      }
    }

    @Override
    public boolean isConsistentItem(Object item)
    {
      return true;
    }

    @Override
    public boolean matchItem(Object item)
    {
      return matches(((Requirement)item).getName());
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class ManifestDiscoveryComparator implements Comparator<Object>
  {
    public ManifestDiscoveryComparator()
    {
    }

    public int compare(Object o1, Object o2)
    {
      return ((Requirement)o1).getName().compareTo(((Requirement)o2).getName());
    }
  }

  /**
   * @author Eike Stepper
   */
  private static final class ManifestDiscoveryLabelProvider extends LabelProvider
  {
    private static final Image FEATURE_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/artifactFeature"); //$NON-NLS-1$

    private static final Image PLUGIN_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("obj16/artifactPlugin"); //$NON-NLS-1$

    private static final Image PACKAGE_IMAGE = P2UIPlugin.INSTANCE.getSWTImage("full/obj16/Requirement_Package"); //$NON-NLS-1$

    private final String namespace;

    public ManifestDiscoveryLabelProvider(String namespace)
    {
      this.namespace = namespace;
    }

    @Override
    public Image getImage(Object element)
    {
      if ("osgi.bundle".equals(namespace)) //$NON-NLS-1$
      {
        return PLUGIN_IMAGE;
      }

      if ("org.eclipse.update.feature".equals(namespace)) //$NON-NLS-1$
      {
        return FEATURE_IMAGE;
      }

      if ("java.package".equals(namespace)) //$NON-NLS-1$
      {
        return PACKAGE_IMAGE;
      }

      return super.getImage(element);
    }

    @Override
    public String getText(Object element)
    {
      if (element instanceof Requirement)
      {
        Requirement requirement = (Requirement)element;
        String text = requirement.getName();

        Version version = requirement.getVersionRange().getMinimum();
        if (version != null && !version.equals(Version.emptyVersion))
        {
          text += " (" + version + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }

        return text;
      }

      return super.getText(element);
    }
  }
}
