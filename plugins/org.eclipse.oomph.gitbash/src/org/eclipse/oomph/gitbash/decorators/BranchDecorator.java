/*
 * Copyright (c) 2014 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.gitbash.decorators;

import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jgit.lib.BranchTrackingStatus;
import org.eclipse.jgit.lib.ConfigConstants;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.swt.graphics.Image;

import java.lang.reflect.Method;

/**
 * @author Eike Stepper
 */
@SuppressWarnings("restriction")
public class BranchDecorator implements ILabelDecorator
{
  private static final String DEFAULT_PATH = "refs/heads/"; //$NON-NLS-1$

  private static final Method FORMAT_METHOD = getFormatMethod();

  public BranchDecorator()
  {
  }

  public void dispose()
  {
  }

  public boolean isLabelProperty(Object element, String property)
  {
    return false;
  }

  public void addListener(ILabelProviderListener listener)
  {
  }

  public void removeListener(ILabelProviderListener listener)
  {
  }

  public Image decorateImage(Image image, Object element)
  {
    return null;
  }

  public String decorateText(String text, Object element)
  {
    try
    {
      if (element instanceof org.eclipse.egit.ui.internal.repository.tree.RefNode)
      {
        org.eclipse.egit.ui.internal.repository.tree.RefNode node = (org.eclipse.egit.ui.internal.repository.tree.RefNode)element;
        if (node.getType() == org.eclipse.egit.ui.internal.repository.tree.RepositoryTreeNodeType.REF && isLocal(node))
        {
          String decoration = getDecoration(node);
          if (decoration != null)
          {
            return text + " [" + decoration + "]"; //$NON-NLS-1$ //$NON-NLS-2$
          }
        }
      }
    }
    catch (Throwable t)
    {
      //$FALL-THROUGH$
    }

    return null;
  }

  private boolean isLocal(org.eclipse.egit.ui.internal.repository.tree.RepositoryTreeNode<?> node)
  {
    if (node.getType() == org.eclipse.egit.ui.internal.repository.tree.RepositoryTreeNodeType.LOCAL)
    {
      return true;
    }

    node = node.getParent();
    if (node != null)
    {
      return isLocal(node);
    }

    return false;
  }

  private String getDecoration(org.eclipse.egit.ui.internal.repository.tree.RefNode node)
  {
    String branchName = Repository.shortenRefName(node.getObject().getName());
    Repository repository = node.getRepository();
    StoredConfig config = repository.getConfig();

    String branch = config.getString(ConfigConstants.CONFIG_BRANCH_SECTION, branchName, ConfigConstants.CONFIG_KEY_MERGE);

    if (branch != null)
    {
      String remote = config.getString(ConfigConstants.CONFIG_BRANCH_SECTION, branchName, ConfigConstants.CONFIG_KEY_REMOTE);

      boolean rebaseFlag = config.getBoolean(ConfigConstants.CONFIG_BRANCH_SECTION, branchName, ConfigConstants.CONFIG_KEY_REBASE, false);

      if (branch.startsWith(DEFAULT_PATH))
      {
        branch = branch.substring(DEFAULT_PATH.length());
      }

      String prefix = ".".equals(remote) ? "" : remote + "/"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
      String result = (rebaseFlag ? "REBASE" : "MERGE") + ": " + prefix + branch; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$

      try
      {
        BranchTrackingStatus trackingStatus = BranchTrackingStatus.of(repository, branchName);
        if (trackingStatus != null && (trackingStatus.getAheadCount() != 0 || trackingStatus.getBehindCount() != 0))
        {
          result += " " + formatBranchTrackingStatus(trackingStatus); //$NON-NLS-1$
        }
      }
      catch (Throwable t)
      {
        //$FALL-THROUGH$
      }

      return result;
    }

    return null;
  }

  private static String formatBranchTrackingStatus(BranchTrackingStatus status) throws Exception
  {
    // A possible NPE or other exceptions will be caught by the caller.
    return (String)FORMAT_METHOD.invoke(null, status);
  }

  private static Method getFormatMethod()
  {
    Class<?> c = null;

    try
    {
      c = Class.forName("org.eclipse.egit.ui.internal.GitLabels"); // EGit 3.6++ //$NON-NLS-1$
    }
    catch (Throwable t)
    {
      //$FALL-THROUGH$
    }

    if (c == null)
    {
      try
      {
        c = Class.forName("org.eclipse.egit.ui.internal.GitLabelProvider"); // EGit 3.5-- //$NON-NLS-1$
      }
      catch (Throwable t)
      {
        //$FALL-THROUGH$
      }
    }

    try
    {
      return c.getDeclaredMethod("formatBranchTrackingStatus", c, BranchTrackingStatus.class); //$NON-NLS-1$
    }
    catch (Throwable t)
    {
      return null;
    }
  }
}
