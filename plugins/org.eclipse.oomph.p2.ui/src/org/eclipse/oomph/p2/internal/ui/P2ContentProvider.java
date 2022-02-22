/*
 * Copyright (c) 2014, 2015 Eike Stepper (Loehne, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.oomph.p2.internal.ui;

import org.eclipse.oomph.p2.core.Agent;
import org.eclipse.oomph.p2.core.AgentManager;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.Profile;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Eike Stepper
 */
public class P2ContentProvider implements ITreeContentProvider
{
  protected static final Object[] NO_CHILDREN = new Object[0];

  private boolean showProfiles;

  public P2ContentProvider()
  {
  }

  public boolean isShowProfiles()
  {
    return showProfiles;
  }

  public void setShowProfiles(boolean showProfiles)
  {
    this.showProfiles = showProfiles;
  }

  @Override
  public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
  {
  }

  @Override
  public void dispose()
  {
  }

  @Override
  public Object[] getElements(Object element)
  {
    return getChildren(element);
  }

  @Override
  public Object getParent(Object element)
  {
    if (element instanceof Profile)
    {
      Profile profile = (Profile)element;
      return profile.getBundlePool();
    }

    if (element instanceof BundlePool)
    {
      BundlePool bundlePool = (BundlePool)element;
      return bundlePool.getAgent();
    }

    if (element instanceof Agent)
    {
      Agent agent = (Agent)element;
      return agent.getAgentManager();
    }

    return null;
  }

  @Override
  public boolean hasChildren(Object element)
  {
    return getChildren(element).length != 0;
  }

  @Override
  public Object[] getChildren(Object element)
  {
    List<Object> children = new ArrayList<>();

    if (element instanceof AgentManager)
    {
      AgentManager agentManager = (AgentManager)element;
      addChildrenOfAgentManager(agentManager, children);
    }

    if (element instanceof Agent)
    {
      Agent agent = (Agent)element;
      addChildrenOfAgent(agent, children);
    }

    if (element instanceof BundlePool)
    {
      BundlePool bundlePool = (BundlePool)element;
      addChildrenOfBundlePool(bundlePool, children);
    }

    return children.toArray();
  }

  protected void addChildrenOfAgentManager(AgentManager agentManager, List<Object> children)
  {
    children.addAll(agentManager.getAgents());
  }

  protected void addChildrenOfAgent(Agent agent, List<Object> children)
  {
    children.addAll(agent.getBundlePools());

    if (showProfiles)
    {
      children.addAll(agent.getProfiles());
    }
  }

  protected void addChildrenOfBundlePool(BundlePool bundlePool, List<Object> children)
  {
    if (showProfiles)
    {
      children.addAll(bundlePool.getProfiles());
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class AllBundlePools extends P2ContentProvider
  {
    @Override
    protected void addChildrenOfAgentManager(AgentManager agentManager, List<Object> children)
    {
      children.addAll(agentManager.getBundlePools());
    }
  }
}
