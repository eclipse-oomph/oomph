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
import org.eclipse.oomph.p2.core.AgentManagerElement;
import org.eclipse.oomph.p2.core.BundlePool;
import org.eclipse.oomph.p2.core.Profile;
import org.eclipse.oomph.ui.UIUtil;
import org.eclipse.oomph.util.ObjectUtil;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public final class P2LabelProvider extends LabelProvider implements IColorProvider, IFontProvider
{
  private static final String EXTENSION_POINT_ID = P2UIPlugin.INSTANCE.getSymbolicName() + ".profileTypes"; //$NON-NLS-1$

  private static final Map<String, Image> profileImages = new HashMap<>();

  private IFontProvider fontProvider;

  private boolean absolutePools;

  public P2LabelProvider()
  {
  }

  public P2LabelProvider(IFontProvider fontProvider)
  {
    this.fontProvider = fontProvider;
  }

  public boolean isAbsolutePools()
  {
    return absolutePools;
  }

  public void setAbsolutePools(boolean absolutePools)
  {
    this.absolutePools = absolutePools;
  }

  @Override
  public Image getImage(Object element)
  {
    if (element instanceof Agent)
    {
      return P2UIPlugin.INSTANCE.getSWTImage("obj16/agent"); //$NON-NLS-1$
    }

    if (element instanceof BundlePool)
    {
      return P2UIPlugin.INSTANCE.getSWTImage("obj16/bundlePool"); //$NON-NLS-1$
    }

    if (element instanceof Profile)
    {
      return getProfileImage((Profile)element);
    }

    return null;
  }

  @Override
  public String getText(Object element)
  {
    if (element instanceof BundlePool && !absolutePools)
    {
      BundlePool bundlePool = (BundlePool)element;
      String bundlePoolPath = bundlePool.getLocation().getAbsolutePath();

      Agent agent = bundlePool.getAgent();
      String agentPath = agent.getLocation().getAbsolutePath() + File.pathSeparator;

      if (bundlePoolPath.startsWith(agentPath))
      {
        bundlePoolPath = bundlePoolPath.substring(agentPath.length());
      }

      return bundlePoolPath;
    }

    if (element instanceof Profile)
    {
      return ((Profile)element).getProfileId();
    }

    return element.toString();
  }

  @Override
  public Color getForeground(Object element)
  {
    if (element instanceof AgentManagerElement)
    {
      AgentManagerElement agentManagerElement = (AgentManagerElement)element;
      if (!agentManagerElement.isUsed())
      {
        return UIUtil.getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
      }
    }

    return null;
  }

  @Override
  public Color getBackground(Object element)
  {
    return null;
  }

  @Override
  public Font getFont(Object element)
  {
    return fontProvider == null ? null : fontProvider.getFont(element);
  }

  private static Image loadProfileImage(String profileType)
  {
    IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
    for (IConfigurationElement configurationElement : extensionRegistry.getConfigurationElementsFor(EXTENSION_POINT_ID))
    {
      String name = configurationElement.getAttribute("name"); //$NON-NLS-1$
      if (ObjectUtil.equals(name, profileType))
      {
        String icon = configurationElement.getAttribute("icon"); //$NON-NLS-1$
        if (icon != null)
        {
          ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(configurationElement.getNamespaceIdentifier(), icon);
          return P2UIPlugin.INSTANCE.getSWTImage(descriptor);
        }
      }
    }

    return P2UIPlugin.INSTANCE.getSWTImage("obj16/profileUnknown"); //$NON-NLS-1$
  }

  public static Image getProfileImage(Profile profile)
  {
    String profileType = profile.getType();
    Image image = profileImages.get(profileType);
    if (image == null)
    {
      image = loadProfileImage(profileType);
      profileImages.put(profileType, image);
    }

    return image;
  }
}
